// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

/**
 * 
 * Class LockerByKeyUnrestricted.
 * 
 * This class has the same behaviours that {@link LockerByKey} except
 * 
 * 
 */
public class LockerByKeyUnrestricted<KP> implements ILockerByKey<KP> {

    private static final String NOT_ALREADY_LOCKED_MESSAGE = "Already unlocked by an other thread or never locked, ensure all the unlock() operations of this locker are called after their lock has really locked:";

    private static Logger log = Logger.getLogger(LockerByKeyUnrestricted.class);

    private ExecutorService threadPool;

    private LockerByKey<KP> locker;

    private final Object lockAllOperations = new Object();

    private AtomicInteger counter = new AtomicInteger();

    private AtomicInteger runningOperations = new AtomicInteger();

    private final static int DEFAULT_CLEAN_PERIOD = 500;

    private final static boolean DEFAULT_FAIR = true;

    private volatile boolean blockAllOperations;

    private volatile boolean shuttingDown;

    private volatile boolean stopped;

    private int cleanPeriod;

    /**
     * LockerByKey constructor.
     */
    public LockerByKeyUnrestricted() {
        this(DEFAULT_FAIR, DEFAULT_CLEAN_PERIOD);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
    public LockerByKeyUnrestricted(boolean fair) {
        this(fair, DEFAULT_CLEAN_PERIOD);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param cleanPeriod in number of operations, it means that an automatic clean will be done for each
     * <code>cleanPeriod</code> number of unlock operation.
     */
    public LockerByKeyUnrestricted(int cleanPeriod) {
        this(DEFAULT_FAIR, cleanPeriod);
    }

    /**
     * 
     * Constructor LockerByKey.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy
     * @param cleanPeriod in number of operations, it means that an automatic clean will be done for each
     * <code>cleanPeriod</code> number of unlock operation.
     */
    public LockerByKeyUnrestricted(boolean fair, int cleanPeriod) {
        super();
        if (cleanPeriod <= 0) {
            throw new IllegalArgumentException("The cleanPeriod value has to be greater than 0");
        }
        boolean cleanDisabled = true;
        this.locker = new LockerByKey<KP>(fair, cleanDisabled);
        this.threadPool = intializePool(LockerByKeyUnrestricted.class.getSimpleName());
        this.cleanPeriod = cleanPeriod;
    }

    protected ExecutorService intializePool(final String poolName) {
        ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadFactory() {

            ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread newThread = defaultThreadFactory.newThread(r);
                newThread.setName(poolName + "_" + newThread.getName());
                return newThread;
            }

        });
        return threadPool;
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
    public void lockInterruptibly(final KP key) throws InterruptedException {
        blockOperationIfRequired();
        incrementRunningOperations();
        try {
            locker.lockInterruptibly(key);
        } finally {
            decrementRunningOperations();
        }
    }

    /**
     * 
     * Method "lockInterruptiblyUnrestricted".
     * 
     * @param key
     * @throws InterruptedException
     * @see java.util.concurrent.locks.ReentrantLock#lockInterruptibly()
     */
    public void lockInterruptiblyUnrestricted(final KP key) throws InterruptedException {
        checkStopped();
        blockOperationIfRequired();

        LockerValue<KP> lockerValue = null;
        LockerValueHandler handler = null;

        if (tryLockUnrestricted(key)) {
            return;
        }

        incrementRunningOperations();

        /* Test if already locked by the same thread */
        lockerValue = locker.getLockerValue(key);
        if (locker != null) {
            handler = lockerValue.getHandler();
            if (handler != null && Thread.currentThread() == handler.getCallerThreadLocker()) {
                decrementRunningOperations();
                return;
            }
        }

        try {
            final Thread threadLocker = Thread.currentThread();
            final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            final AtomicBoolean hasError = new AtomicBoolean();
            Callable<Boolean> callable = new Callable<Boolean>() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.util.concurrent.Callable#call()
                 */
                @Override
                public Boolean call() throws Exception {
                    try {
                        locker.lockInterruptibly(key);
                    } catch (Exception e) {
                        hasError.set(true);
                        throw e;
                    } finally {
                        // STEP 1
                        cyclicBarrier.await();
                    }
                    // STEP 2
                    cyclicBarrier.await();
                    boolean unlocked = locker.unlock(key);
                    return unlocked;
                }

            };
            Future<Boolean> futureTask = threadPool.submit(callable);
            try {
                // STEP 1
                cyclicBarrier.await();
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

            if (hasError.get()) {
                try {
                    futureTask.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause != null && cause instanceof InterruptedException) {
                        throw (InterruptedException) cause;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }

            lockerValue = locker.getLockerValue(key);
            lockerValue.addHandler(new LockerValueHandler(futureTask, cyclicBarrier, threadLocker));
        } finally {
            decrementRunningOperations();
        }
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
    public boolean tryLock(final KP key) {
        blockOperationIfRequired();
        incrementRunningOperations();
        try {
            return locker.tryLock(key);
        } finally {
            decrementRunningOperations();
        }
    }

    /**
     * Method "tryLockUnrestricted".
     * 
     * @param key
     * @return {@code true} if the lock was free and was acquired by the current thread, or the lock was already held by
     * the current thread; and {@code false} otherwise
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @see java.util.concurrent.locks.ReentrantLock#tryLock()
     */
    public boolean tryLockUnrestricted(final KP key) {
        checkStopped();
        blockOperationIfRequired();
        incrementRunningOperations();
        boolean tryLockResultBoolean;
        try {
            final AtomicBoolean tryLockResult = new AtomicBoolean();
            final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            Callable<Boolean> callable = new Callable<Boolean>() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.util.concurrent.Callable#call()
                 */
                @Override
                public Boolean call() throws Exception {
                    boolean locked;
                    try {
                        locked = locker.tryLock(key);
                        tryLockResult.set(locked);
                    } finally {
                        // STEP 1
                        cyclicBarrier.await();
                    }
                    if (locked) {
                        // STEP 2
                        cyclicBarrier.await();
                        return locker.unlock(key);
                    } else {
                        return false;
                    }
                }

            };
            Future<Boolean> futureTask = threadPool.submit(callable);
            try {
                // STEP 1
                cyclicBarrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tryLockResultBoolean = tryLockResult.get();
            if (tryLockResultBoolean) {
                LockerValue<KP> lockerValue = locker.getLockerValue(key);
                Thread callerThreadLocker = Thread.currentThread();
                lockerValue.addHandler(new LockerValueHandler(futureTask, cyclicBarrier, callerThreadLocker));
            }
        } finally {
            decrementRunningOperations();
        }
        return tryLockResultBoolean;
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
    public boolean tryLock(final KP key, final long timeout) throws InterruptedException {
        return locker.tryLock(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Method "tryLockUnrestricted".
     * 
     * @param key
     * @param timeout the time to wait for the lock in milliseconds
     * @return true if the lock was free and was acquired by the current thread, or the lock was already held by the
     * current thread; and false if the waiting time elapsed before the lock could be acquired
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @see java.util.concurrent.locks.ReentrantLock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    public boolean tryLockUnrestricted(final KP key, final long timeout) throws InterruptedException {
        return tryLockUnrestricted(key, timeout, TimeUnit.MILLISECONDS);
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
    public boolean tryLock(final KP key, final long timeout, final TimeUnit unit) throws InterruptedException {
        blockOperationIfRequired();
        incrementRunningOperations();
        try {
            return locker.tryLock(key, timeout, unit);
        } finally {
            decrementRunningOperations();
        }
    }

    /**
     * Method "tryLockUnrestricted".
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
    public boolean tryLockUnrestricted(final KP key, final long timeout, final TimeUnit unit) throws InterruptedException {
        checkStopped();
        blockOperationIfRequired();
        incrementRunningOperations();
        boolean tryLockResultBoolean = false;
        try {
            final AtomicBoolean tryLockResult = new AtomicBoolean();
            final AtomicReference<InterruptedException> interruptedExceptionFromTryRef = new AtomicReference<InterruptedException>();
            final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            Callable<Boolean> callable = new Callable<Boolean>() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.util.concurrent.Callable#call()
                 */
                @Override
                public Boolean call() throws Exception {
                    boolean locked = false;
                    try {
                        locked = locker.tryLock(key, timeout, unit);
                        tryLockResult.set(locked);
                    } catch (InterruptedException e) {
                        interruptedExceptionFromTryRef.set(e);
                        return false;
                    } finally {
                        // STEP 1
                        cyclicBarrier.await();
                    }
                    if (locked) {
                        // STEP 2
                        cyclicBarrier.await();
                        return locker.unlock(key);
                    } else {
                        return false;
                    }
                }

            };
            Future<Boolean> futureTask = threadPool.submit(callable);
            try {
                // STEP 1
                cyclicBarrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            InterruptedException interruptedExceptionFromTry = interruptedExceptionFromTryRef.get();
            if (interruptedExceptionFromTry != null) {
                throw interruptedExceptionFromTry;
            }
            tryLockResultBoolean = tryLockResult.get();
            if (tryLockResultBoolean) {
                LockerValue<KP> lockerValue = locker.getLockerValue(key);
                Thread threadLocker = Thread.currentThread();
                lockerValue.addHandler(new LockerValueHandler(futureTask, cyclicBarrier, threadLocker));
                locker.traceStackForDebugging(lockerValue);
            }
        } finally {
            decrementRunningOperations();
        }

        return tryLockResultBoolean;
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * To detect incorrect unlocking, this method may return an <code>IllegalStateException</code> when the lock has
     * been already unlocked or it never been locked.
     * 
     * @param key
     * @return true if the key has been found to release the lock; and false otherwise
     * @throws IllegalStateException
     * @see java.util.concurrent.locks.ReentrantLock#unlock()
     */
    @Override
    public boolean unlock(final KP key) {
        boolean returnedValue = false;
        try {
            checkStopped();
            blockOperationIfRequired();
            incrementRunningOperations();
            returnedValue = locker.unlock(key);
        } finally {
            decrementRunningOperations();
        }
        cleanAccordingOperations();
        return returnedValue;
    }

    /**
     * Method "unlockUnrestricted". Unlock the operations with the provided key.
     * 
     * To detect incorrect unlocking, this method may return an <code>IllegalStateException</code> when the lock has
     * been already unlocked or it never been locked.
     * 
     * @param key
     * @return true if the key has been found to release the lock; and false otherwise
     * @throws IllegalStateException
     * @see java.util.concurrent.locks.ReentrantLock#unlock()
     */
    public boolean unlockUnrestricted(final KP key) {
        checkStopped();
        blockOperationIfRequired();
        Boolean resultFuture;
        incrementRunningOperations();
        try {
            LockerValue<KP> lockerValue = locker.getLockerValue(key);
            if (lockerValue == null) {
                throw new IllegalStateException(NOT_ALREADY_LOCKED_MESSAGE + " key=" + key);
            }
            LockerValueHandler handler = lockerValue.getHandlerAndRemove();
            if (handler == null) {
                throw new UnsupportedOperationException(
                        "Either you have to use the restricted unlock() method to unlock, or you have to use '*Lock*Unrestricted()' methods to lock !");
            }
            CyclicBarrier barrier = handler.getBarrier();
            try {
                // STEP 2
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Future<Boolean> future = handler.getFuture();
            if (future.isCancelled()) {
                return false;
            }
            resultFuture = null;
            try {
                resultFuture = future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            decrementRunningOperations();
        }
        cleanAccordingOperations();
        return resultFuture;
    }

    @Override
    public int getCleanPeriod() {
        return cleanPeriod;
    }

    private void checkStopped() {
        if (stopped || shuttingDown) {
            throw new IllegalStateException("This locker is already stopped or is shutting down !");
        }
    }

    private void cleanAccordingOperations() {
        synchronized (lockAllOperations) {
            int cleanPeriod = getCleanPeriod();
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
            waitForRunningOperationsEnded();
            locker.clean();
            resumeAllOperations();
        }
    }

    private void waitForRunningOperationsEnded() {
        blockAllOperations();
        boolean breakAtNext = false;
        while (true) {
            Collection<LockerValue<KP>> values = locker.getMapKeyLockToValueLock().values();
            int waitingThreads = 0;
            for (LockerValue<KP> lockerValue : values) {
                waitingThreads += lockerValue.getLock().getQueueLength();
            }
            if (runningOperations.get() - waitingThreads <= 0) {
                if (breakAtNext) {
                    break;
                }
                breakAtNext = true;
            } else {
                breakAtNext = false;
            }
            try {
                Thread.sleep(10);
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

    @Override
    public synchronized void shutdownNow() {
        shuttingDown = true;
        locker.shutdownNow();
        threadPool.shutdownNow();
        stopped = true;
    }

    @Override
    public synchronized void shutdown() {
        shuttingDown = true;
        locker.shutdown();
        threadPool.shutdown();
        stopped = true;
    }

    @Override
    public LockerValue<KP> getLockerValue(KP key) {
        return locker.getLockerValue(key);
    }

    @Override
    public boolean isLocked(KP key) {
        return locker.isLocked(key);
    }

    @Override
    public List<LockerValue<KP>> getSuspectLocks(long timeDetectionLimitMs) {
        return locker.getSuspectLocks(timeDetectionLimitMs);
    }

    @Override
    public void setDetectSuspectLocks(boolean detectSuspectLocks) {
        locker.setDetectSuspectLocks(detectSuspectLocks);
    }

    @Override
    public boolean isDetectSuspectLocks() {
        return locker.isDetectSuspectLocks();
    }

}
