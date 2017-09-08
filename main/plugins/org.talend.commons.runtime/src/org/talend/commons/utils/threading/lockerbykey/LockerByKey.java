// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.threading.lockerbykey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.talend.commons.utils.StringUtils;

/**
 * This class is useful to lock some part of code from the provided key.
 * 
 * This class has the same behaviour that <code>java.util.concurrent.locks.ReentrantLock</code> except additionally it
 * expect keys to lock the parts of code.
 * 
 * It uses internally a <code>java.util.concurrent.ConcurrentHashMap</code> to store locks from keys <code>KP</code> and
 * the <code>java.util.concurrent.locks.ReentrantLock</code> as properties of a value wrapper. <br/>
 * 
 * @see java.util.concurrent.locks.ReentrantLock
 * 
 * @param <KP> type of the key
 */
public class LockerByKey<KP> implements ILockerByKey<KP> {

    private static Logger log = Logger.getLogger(LockerByKey.class);

    private ConcurrentHashMap<InternalKeyLock<KP>, LockerValue<KP>> mapKeyLockToValueLock = new ConcurrentHashMap<InternalKeyLock<KP>, LockerValue<KP>>();

    private final Object lockAllOperations = new Object();

    private AtomicInteger counter = new AtomicInteger();

    private AtomicInteger runningOperations = new AtomicInteger();

    private final static int DEFAULT_CLEAN_PERIOD = 500;

    private final static boolean DEFAULT_FAIR = true;

    private int cleanPeriod;

    private boolean fair;

    private volatile boolean blockAllOperations;

    private volatile boolean shuttingDown;

    private volatile boolean stopped;

    private static boolean detectSuspectLocksStatic = false;

    private boolean detectSuspectLocks = false;

    private boolean forceShutdown;

    static {
        String optionKey = "detectSuspectLocks";
        String detectSuspectLocksStr = System.getProperty(optionKey);
        if (detectSuspectLocksStr != null && detectSuspectLocksStr.length() > 0) {
            detectSuspectLocksStatic = Boolean.parseBoolean(detectSuspectLocksStr);
        }
        if (detectSuspectLocksStatic) {
            log.info("System property \"" + optionKey + "\"=" + detectSuspectLocksStatic);
        }
    }

    /**
     * LockerByKey constructor.
     */
    public LockerByKey() {
        this(DEFAULT_FAIR, DEFAULT_CLEAN_PERIOD);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
    public LockerByKey(boolean fair) {
        this(fair, DEFAULT_CLEAN_PERIOD);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param cleanPeriod in number of operations, it means that an automatic clean will be done for each
     * <code>cleanPeriod</code> number of unlock operation.
     */
    public LockerByKey(int cleanPeriod) {
        this(DEFAULT_FAIR, cleanPeriod);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy
     * @param cleanPeriod in number of operations, it means that an automatic clean will be done after each
     * <code>cleanPeriod</code> number of unlock operation.
     */
    public LockerByKey(boolean fair, int cleanPeriod) {
        super();
        this.fair = fair;
        if (cleanPeriod <= 0) {
            throw new IllegalArgumentException("The cleanPeriod value has to be greater than 0");
        }
        this.cleanPeriod = cleanPeriod;
        this.detectSuspectLocks = detectSuspectLocksStatic;
        if (this.detectSuspectLocks) {
            launchThreadDebugger();
        }
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy
     * @param cleanDisabled true to disable the clean completely <code>cleanPeriod</code> number of unlock operation.
     */
    protected LockerByKey(boolean fair, boolean cleanDisabled) {
        super();
        this.fair = fair;
        if (cleanDisabled) {
            this.cleanPeriod = 0;
        } else {
            this.cleanPeriod = DEFAULT_CLEAN_PERIOD;
        }
        this.detectSuspectLocks = detectSuspectLocksStatic;
        if (this.detectSuspectLocks) {
            launchThreadDebugger();
        }
    }

    private void launchThreadDebugger() {
        new Thread(this.getClass().getSimpleName() + "-ThreadDebugger-" + this.hashCode()) {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                while (!stopped && !shuttingDown) {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    long timeDetectionLimitMs = 30000L;
                    List<LockerValue<KP>> lockerValues = getSuspectLocks(timeDetectionLimitMs);
                    StringBuilder sb = new StringBuilder();
                    for (LockerValue<KP> lockerValue : lockerValues) {
                        long duration = System.currentTimeMillis() - lockerValue.getLockedTime();
                        StackTraceElement[] stackTraceOfLocker = lockerValue.getStackTraceOfLocker();
                        for (StackTraceElement trace : stackTraceOfLocker) {
                            StackTraceElement stackTraceElement = trace;
                            sb.append(stackTraceElement.toString());
                            sb.append("\n");
                        }
                        log.warn("Suspect lock done since " + duration + " ms by: " + sb.toString());
                    }
                }
            }
        }.start();

    }

    /**
     * Method "isLocked".
     * 
     * @param key
     * @return true if any thread holds this lock and false otherwise
     * 
     * @see java.util.concurrent.locks.ReentrantLock#isLocked()
     */
    @Override
    public boolean isLocked(KP key) {
        checkKey(key);
        LockerValue<KP> locker = getLockerValue(key);
        return locker != null && locker.getLock().isLocked();
    }

    /**
     * 
     * Method "lockInterruptibly".
     * 
     * @param key
     * @throws InterruptedException
     * @see java.util.concurrent.locks.ReentrantLock#lockInterruptibly()
     */
    @Override
    public void lockInterruptibly(KP key) throws InterruptedException {
        checkStopped();
        checkKey(key);
        blockOperationIfRequired();
        incrementRunningOperations();
        LockerValue<KP> lockerValue = prepareInternalLock(key);
        decrementRunningOperations();
        lockerValue.getLock().lockInterruptibly();
        traceStackForDebugging(lockerValue);
    }

    /**
     * Method "tryLock".
     * 
     * @param key
     * @return {@code true} if the lock was free and was acquired by the current thread, or the lock was already held by
     * the current thread; and {@code false} otherwise
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @see java.util.concurrent.locks.ReentrantLock#tryLock()
     */
    @Override
    public boolean tryLock(KP key) {
        if (stopped || shuttingDown) {
            return false;
        }
        checkKey(key);
        blockOperationIfRequired();
        incrementRunningOperations();
        LockerValue<KP> lockerValue = prepareInternalLock(key);
        decrementRunningOperations();
        boolean locked = lockerValue.getLock().tryLock();
        if (locked) {
            traceStackForDebugging(lockerValue);
        }
        return locked;
    }

    /**
     * Method "tryLock".
     * 
     * @param key
     * @param timeout the time to wait for the lock in milliseconds
     * @return true if the lock was free and was acquired by the current thread, or the lock was already held by the
     * current thread; and false if the waiting time elapsed before the lock could be acquired
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @see java.util.concurrent.locks.ReentrantLock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean tryLock(KP key, long timeout) throws InterruptedException {
        return tryLock(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Method "tryLock".
     * 
     * @param key
     * @param timeout the time to wait for the lock
     * @param unit the time unit of the timeout argument
     * @return true if the lock was free and was acquired by the current thread, or the lock was already held by the
     * current thread; and false if the waiting time elapsed before the lock could be acquired
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * 
     * @see java.util.concurrent.locks.ReentrantLock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean tryLock(KP key, long timeout, TimeUnit unit) throws InterruptedException {
        checkStopped();
        checkKey(key);
        blockOperationIfRequired();
        incrementRunningOperations();
        LockerValue<KP> lockerValue = prepareInternalLock(key);
        decrementRunningOperations();
        interruptIfStopping();
        boolean locked = lockerValue.getLock().tryLock(timeout, unit);
        if (locked) {
            traceStackForDebugging(lockerValue);
        }
        return locked;
    }

    private LockerValue<KP> prepareInternalLock(KP key) {
        InternalKeyLock<KP> internalKeyLock = new InternalKeyLock<KP>(key);
        LockerValue<KP> lockerValue = new LockerValue<KP>(key, fair);
        LockerValue<KP> previousLockerValue = null;
        previousLockerValue = mapKeyLockToValueLock.putIfAbsent(internalKeyLock, lockerValue);
        if (previousLockerValue != null) {
            lockerValue = previousLockerValue;
        }
        return lockerValue;
    }

    private void interruptIfStopping() throws InterruptedException {
        if (shuttingDown) {
            throw new InterruptedException("This LockerByKey is shutting down...");
        }
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * @param key
     * @return true if the key has been found to release the lock; and false otherwise
     * @see java.util.concurrent.locks.ReentrantLock#unlock()
     */
    @Override
    public boolean unlock(KP key) {
        checkKey(key);
        blockOperationIfRequired();
        incrementRunningOperations();
        LockerValue<KP> lockerValue = getLockerValue(key);
        boolean returnValue = false;
        if (lockerValue != null) {
            lockerValue.getLock().unlock();
            returnValue = true;
        }
        decrementRunningOperations();
        cleanAccordingOperations();
        return returnValue;
    }

    void traceStackForDebugging(LockerValue<KP> lockerValue) {
        if (this.detectSuspectLocks) {
            lockerValue.setStackTraceOfLocker(Thread.currentThread().getStackTrace());
            lockerValue.setLockedTime(System.currentTimeMillis());
        }
    }

    private void cleanAccordingOperations() {
        synchronized (lockAllOperations) {
            if (cleanPeriod > 0 && counter.incrementAndGet() % cleanPeriod == 0) {
                clean();
            }
        }
    }

    /**
     * 
     * Method "clean".
     * 
     * Clean the map which contains the lock wrappers.
     * 
     * Removed lock wrappers are these where lock is not locked by a thread and no one thread is waiting to obtain the
     * lock.
     * 
     * The default clean will do an automatic clean all 1000 unlock operation, you can disable or change this value from
     * the constructor.
     */
    @Override
    public void clean() {
        synchronized (lockAllOperations) {
            blockAllOperations();
            if (!(forceShutdown && shuttingDown)) {
                waitForRunningOperationsEnded();
            }
            Collection<LockerValue<KP>> values = mapKeyLockToValueLock.values();
            if (log.isTraceEnabled()) {
                log.trace("Cleaning " + this.toString() + " : " + values.size() + " keys/values ...");
            }
            InternalKeyLock<KP> internalKeyLock = new InternalKeyLock<KP>();
            for (LockerValue<KP> lockerValue : values) {
                ReentrantLock lock = lockerValue.getLock();
                LockerValueHandler handler = lockerValue.getHandler();
                if (!lock.hasQueuedThreads() && !lock.isLocked() && handler == null) {
                    internalKeyLock.setKey(lockerValue.getKey());
                    mapKeyLockToValueLock.remove(internalKeyLock);
                }
            }
            resumeAllOperations();
        }
    }

    private void checkStopped() {
        if (stopped || shuttingDown) {
            throw new IllegalStateException("This locker is already stopped or is shutting down !");
        }
    }

    /**
     * Method "check". Check if the key is not null.
     * 
     * @param key
     */
    private void checkKey(KP key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null"); //$NON-NLS-1$
        }
    }

    private void waitForRunningOperationsEnded() {
        while (runningOperations.get() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void resumeAllOperations() {
        this.blockAllOperations = false;
        lockAllOperations.notifyAll();
    }

    private void blockAllOperations() {
        this.blockAllOperations = true;
    }

    private void blockOperationIfRequired() {
        if (blockAllOperations) {
            synchronized (lockAllOperations) {
                if (blockAllOperations) {
                    try {
                        lockAllOperations.wait();
                    } catch (InterruptedException e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private void decrementRunningOperations() {
        runningOperations.decrementAndGet();
    }

    private void incrementRunningOperations() {
        runningOperations.incrementAndGet();
    }

    /**
     * Get locker.
     * 
     * @param bean
     * @return locker value.
     */
    @Override
    public LockerValue<KP> getLockerValue(KP key) {
        checkKey(key);
        InternalKeyLock<KP> internalKeyLock = new InternalKeyLock<KP>(key);
        return mapKeyLockToValueLock.get(internalKeyLock);
    }

    @Override
    public synchronized void shutdownNow() {
        forceShutdown = true;
        shutdown();
    }

    @Override
    public synchronized void shutdown() {
        shuttingDown = true;
        blockAllOperations();
        if (!forceShutdown) {
            waitForRunningOperationsEnded();
        }
        Collection<LockerValue<KP>> values = mapKeyLockToValueLock.values();
        for (LockerValue<KP> lockerValue : values) {
            Collection<Thread> queuedThreads = lockerValue.getLock().getQueuedThreads();
            for (Thread thread : queuedThreads) {
                thread.interrupt();
            }
        }
        clean();
        stopped = true;
    }

    @Override
    public String toString() {
        return "LockerByKey:" + super.toString();
    }

    /**
     * Getter for cleanFrequency.
     * 
     * @return the cleanFrequency
     */
    @Override
    public int getCleanPeriod() {
        return cleanPeriod;
    }

    /**
     * Getter for detectSuspectLocks.
     * 
     * @return the detectSuspectLocks
     */
    @Override
    public boolean isDetectSuspectLocks() {
        return this.detectSuspectLocks;
    }

    /**
     * Sets the detectSuspectLocks.
     * 
     * @param detectSuspectLocks the detectSuspectLocks to set
     */
    @Override
    public void setDetectSuspectLocks(boolean detectSuspectLocks) {
        this.detectSuspectLocks = detectSuspectLocks;
    }

    @Override
    public List<LockerValue<KP>> getSuspectLocks(long timeDetectionLimitMs) {
        if (this.detectSuspectLocks) {
            Collection<LockerValue<KP>> values = mapKeyLockToValueLock.values();
            List<LockerValue<KP>> stacks = new ArrayList<LockerValue<KP>>();
            for (LockerValue<KP> lockerValue : values) {
                long lockedTime = lockerValue.getLockedTime();
                long duration = System.currentTimeMillis() - lockedTime;
                if (lockedTime > 0 && duration > timeDetectionLimitMs && lockerValue.getLock().isLocked()) {
                    stacks.add(lockerValue);
                }
            }
            return stacks;
        } else {
            throw new UnsupportedOperationException(
                    "You have to enable the 'detectSuspectLocks' mode by using the JVM argument -DdetectSuspectLocks=true");
        }
    }

    /**
     * 
     * LockerByKey class.<br/>
     * 
     * @param <IKP> key
     */
    class InternalKeyLock<IKP> {

        private IKP key;

        public InternalKeyLock() {
        }

        /**
         * InternalKeyLock constructor comment.
         * 
         * @param key2
         */
        public InternalKeyLock(IKP key) {
            this.key = key;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InternalKeyLock other = (InternalKeyLock) obj;
            if (this.key == null) {
                if (other.key != null) {
                    return false;
                }
            } else if (!this.key.equals(other.key)) {
                return false;
            }
            return true;
        }

        public void setKey(IKP key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return StringUtils.replacePrms(InternalKeyLock.class.getSimpleName() + ": key={0}", key); //$NON-NLS-1$
        }
    }

    /**
     * Getter for mapKeyLockToValueLock.
     * 
     * @return the mapKeyLockToValueLock
     */
    Map<InternalKeyLock<KP>, LockerValue<KP>> getMapKeyLockToValueLock() {
        return new HashMap<InternalKeyLock<KP>, LockerValue<KP>>(mapKeyLockToValueLock);
    }

}
