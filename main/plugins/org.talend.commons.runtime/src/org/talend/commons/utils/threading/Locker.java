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
package org.talend.commons.utils.threading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.collections.list.SynchronizedList;
import org.apache.log4j.Logger;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.data.bean.IGetterPropertyAccessor;
import org.talend.commons.utils.data.map.MultiLazyValuesMap;

/**
 * class Locker.<br/>
 * 
 * @deprecated use {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey<KP>}, or
 * {@link org.talend.commons.utils.threading.lockerbykey.LockerByKeyUnrestricted<KP>} if unlock is required from an
 * other thread.
 * @param <B> bean which contains the property id
 * @param <KP> type of the key/property
 */
@Deprecated
public class Locker<B, KP> {

    private static final String UNDEFINED_CONTEXT_INFO = "UNDEFINED"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(Locker.class);

    private Map<InternalKeyLock, LockerValue> lockKeyToThreadsMap = Collections
            .synchronizedMap(new HashMap<InternalKeyLock, LockerValue>());

    private InternalKeyLock<KP> matchingKey = new InternalKeyLock<KP>();

    private Executor treadsPool;

    /**
     * 
     * DOC amaumont Locker class global comment. Detailled comment <br/>
     * 
     * @param <KP>
     */
    class InternalKeyLock<KP> {

        private String contextInfo;

        private KP key;

        /**
         * DOC amaumont InternalKeyLock constructor comment.
         * 
         * @param key2
         */
        public InternalKeyLock(KP key) {
            this.key = key;
        }

        /**
         * DOC amaumont InternalKeyLock constructor comment.
         * 
         * @param key2
         * @param contextInfo2
         */
        public InternalKeyLock(KP key, String contextInfo) {
            this(key);
            this.contextInfo = contextInfo;
        }

        /**
         * DOC amaumont InternalKeyLock constructor comment.
         */
        public InternalKeyLock() {
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

        @Override
        public String toString() {
            return StringUtils.replacePrms(
                    "InternalKeyLock: thread={0}, key={1}, contextInfo={2}", Thread.currentThread().getName(), key, contextInfo); //$NON-NLS-1$
        }
    }

    /**
     * 
     * DOC amaumont Locker class global comment. Detailled comment <br/>
     * 
     * @param <KP>
     */
    public class LockerValue {

        private String contextInfo;

        private Thread thread;

        private KP key;

        /**
         * DOC amaumont InternalKeyLock constructor comment.
         * 
         * @param thread
         * @param contextInfo
         */
        public LockerValue(Thread thread, KP key, String contextInfo) {
            this.thread = thread;
            this.key = key;
            this.contextInfo = contextInfo;
        }

        @Override
        public String toString() {
            return StringUtils.replacePrms(
                    "LockerValue: threadName={0}, key={1}, contextInfo={2}", thread.getName(), String.valueOf(key), contextInfo); //$NON-NLS-1$
        }

        /**
         * Getter for contextInfo.
         * 
         * @return the contextInfo
         */
        public String getContextInfo() {
            return contextInfo;
        }

        /**
         * Getter for thread.
         * 
         * @return the thread
         */
        public Thread getThread() {
            return thread;
        }

        /**
         * Getter for key.
         * 
         * @return the key
         */
        public KP getKey() {
            return key;
        }

    }

    private MultiLazyValuesMap waitingThreadsByKey = new MultiLazyValuesMap(new Hashtable()) {

        @Override
        public Collection instanciateNewCollection() {
            return SynchronizedList.decorate(new ArrayList());
        }

    };

    private IGetterPropertyAccessor<B, KP> getterId;

    private boolean allowReentrantLockFromLockerThread = true;

    /**
     * DOC amaumont Locker constructor comment.
     * 
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey}
     */
    @Deprecated
    public Locker() {
        super();
    }

    /**
     * 
     * Constructor Locker.
     * 
     * @param allowReentrantLockFromLockerThread default is true
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey}
     */
    @Deprecated
    public Locker(boolean allowReentrantLockFromLockerThread) {
        super();
        this.allowReentrantLockFromLockerThread = allowReentrantLockFromLockerThread;
    }

    /**
     * DOC amaumont Locker constructor comment.
     * 
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey}
     */
    @Deprecated
    public Locker(IGetterPropertyAccessor<B, KP> getterId) {
        this();
        this.getterId = getterId;
        if (getterId == null) {
            throw new IllegalArgumentException("getterId can't be null"); //$NON-NLS-1$
        }
    }

    /**
     * 
     * DOC amaumont Locker constructor comment.
     * 
     * @param getterId
     * @param allowReentrantLockFromLockerThread default is true
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey}
     */
    @Deprecated
    public Locker(boolean allowReentrantLock, IGetterPropertyAccessor<B, KP> getterId) {
        this();
        this.getterId = getterId;
        if (getterId == null) {
            throw new IllegalArgumentException("getterId can't be null"); //$NON-NLS-1$
        }
        this.allowReentrantLockFromLockerThread = allowReentrantLock;
    }

    /**
     * 
     * DOC amaumont Comment method "isLockedBean".
     * 
     * @param bean
     * @return
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#isLocked(Object)}
     */
    @Deprecated
    public synchronized boolean isLockedBean(B bean) {
        checkBean(bean);
        KP key = getterId.get(bean);
        return isLocked(key);
    }

    /**
     * DOC amaumont Comment method "isLocked".
     * 
     * @param key
     * @return
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#isLocked(Object)}
     */
    @Deprecated
    public synchronized boolean isLocked(KP key) {
        check(key);
        matchingKey.key = key;
        LockerValue lockingValue = lockKeyToThreadsMap.get(matchingKey);
        if (lockingValue == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Lock.
     * 
     * @param bean
     * @return previous lock state, true was locked
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead replaced by
     * {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(Object)}
     */
    @Deprecated
    public synchronized boolean lockBean(B bean) {
        return lockBean(bean, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Lock.
     * 
     * @param bean
     * @return previous lock state, true was locked
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead replaced by
     * {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(Object)}
     */
    @Deprecated
    public synchronized boolean lockBean(B bean, String contextInfo) {
        checkBean(bean);
        KP key = getterId.get(bean);
        return lock(key, contextInfo);
    }

    /**
     * 
     * Method "lock". Lock the operations on the provided <code>key</code>.
     * 
     * @param key
     * @return true if already locked, false if the lock is a new one
     * @deprecated use instead replaced by
     * {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(Object)}
     */
    @Deprecated
    public boolean lock(KP key) {
        return lock(key, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * 
     * Method "lock". Lock the operations on the provided <code>key</code>.
     * 
     * @param key
     * @param contextInfo
     * @return true if already locked, false if the lock is a new one
     * @deprecated use instead replaced by
     * {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(Object)}
     */
    @Deprecated
    public boolean lock(KP key, String contextInfo) {
        check(key);
        if (log.isTraceEnabled()) {
            log.trace(StringUtils.replacePrms(
                    "Locking ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName(), key, contextInfo)); //$NON-NLS-1$
        }
        LockerValue valueLock = lockKeyToThreadsMap.put(new InternalKeyLock<KP>(key, contextInfo),
                new LockerValue(Thread.currentThread(), key, contextInfo));
        if (valueLock == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Lock if it is already unlocked.
     * 
     * @param bean
     * @return true if lock has been done or current thread has already locked the same bean, else false.
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object)}
     */
    @Deprecated
    public synchronized boolean lockIfUnlockedBean(B bean) {
        return lockIfUnlockedBean(bean, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Lock if it is already unlocked.
     * 
     * @param bean
     * @return true if lock has been done or current thread has already locked the same bean, else false.
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object)}
     */
    @Deprecated
    public synchronized boolean lockIfUnlockedBean(B bean, String contextInfo) {
        checkBean(bean);
        if (!isLockedBean(bean)) {
            lockBean(bean, contextInfo);
            return true;
        } else {
            KP key = getterId.get(bean);
            matchingKey.key = key;
            LockerValue valueLock = lockKeyToThreadsMap.get(matchingKey);
            if (allowReentrantLockFromLockerThread && valueLock != null && Thread.currentThread() == valueLock.thread) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Lock if it is already unlocked.
     * 
     * @param bean
     * @return true if lock has been done or current thread has already locked the same bean, else false.
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object)}
     */
    @Deprecated
    public synchronized boolean lockIfUnlocked(KP key) {
        return lockIfUnlocked(key, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Lock if it is already unlocked.
     * 
     * @param bean
     * @return true if lock has been done or current thread has already locked the same bean, else false.
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object)}
     */
    @Deprecated
    public synchronized boolean lockIfUnlocked(KP key, String contextInfo) {
        check(key);
        if (!isLocked(key)) {
            lock(key, contextInfo);
            return true;
        } else {
            matchingKey.key = key;
            LockerValue valueLock = lockKeyToThreadsMap.get(matchingKey);
            if (allowReentrantLockFromLockerThread && valueLock != null && Thread.currentThread() == valueLock.thread) {
                // System.out.println("Same thread");
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * DOC amaumont Comment method "check".
     * 
     * @param bean
     */
    private void checkBean(B bean) {
        if (bean == null) {
            throw new IllegalArgumentException("bean can't be null"); //$NON-NLS-1$
        }
    }

    /**
     * DOC amaumont Comment method "check".
     * 
     * @param key
     */
    private void check(KP key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null"); //$NON-NLS-1$
        }
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * @param bean
     * @return true if the key has unlocked, false if the key unlocked nothing
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#unlock(Object)}
     */
    @Deprecated
    public boolean unlockBean(B bean) {
        return unlockBean(bean, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * @param bean
     * @return true if the key has unlocked, false if the key unlocked nothing
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#unlock(Object)}
     */
    @Deprecated
    public synchronized boolean unlockBean(B bean, String contextInfo) {
        if (bean == null) {
            return false;
        }
        checkBean(bean);
        KP key = getterId.get(bean);
        return unlock(key, contextInfo);
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * @param key
     * @return true if the key has unlocked, false if the key unlocked nothing
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#unlock(Object)}
     */
    @Deprecated
    public boolean unlock(KP key) {
        return unlock(key, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Method "unlock". Unlock the operations with the provided key.
     * 
     * @param key
     * @return true if the key has unlocked, false if the key unlocked nothing
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#unlock(Object)}
     */
    @Deprecated
    public synchronized boolean unlock(KP key, String contextInfo) {
        check(key);
        if (log.isTraceEnabled()) {
            log.trace(StringUtils.replacePrms(
                    "Unlocking ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName(), key, contextInfo)); //$NON-NLS-1$
        }
        matchingKey.key = key;
        LockerValue valueLock = lockKeyToThreadsMap.remove(matchingKey);
        List<Thread> waitingThreads = (List<Thread>) waitingThreadsByKey.getCollection(key);
        if (waitingThreads != null && waitingThreads.size() > 0) {
            synchronized (waitingThreads.get(0)) {
                waitingThreads.get(0).notify();
            }
        }
        if (valueLock == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method "waitForLockBean". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * @param bean
     * @return true if the current thread has wait a time before able to lock, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(KP)}
     */
    @Deprecated
    public boolean waitForLockBean(B bean) throws InterruptedException {
        return waitForLockBean(bean, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Method "waitForLockBean". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * It allows by default reentrant locks from the first locker thread. If you don't want reentrant lock, you have to
     * set <code>allowReentrantLockFromLockerThread</code> = <code>false</code> from the <code>Locker</code>
     * constructor.
     * 
     * @param bean
     * @return true if the current thread has wait a time before able to lock, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(KP)}
     */
    @Deprecated
    public boolean waitForLockBean(B bean, String contextInfo) throws InterruptedException {
        checkBean(bean);
        if (!lockIfUnlockedBean(bean, contextInfo)) {
            synchronized (Thread.currentThread()) {

                waitingThreadsByKey.put(getterId.get(bean), Thread.currentThread());
                try {
                    if (log.isTraceEnabled()) {
                        log.trace(StringUtils.replacePrms(
                                "Waiting for unlocked ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName() //$NON-NLS-1$
                                , getterId.get(bean), contextInfo));
                    }
                    Thread.currentThread().wait();
                    if (log.isTraceEnabled()) {
                        log.trace(StringUtils.replacePrms(
                                "Waiting ended ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName() //$NON-NLS-1$
                                , getterId.get(bean), contextInfo));
                    }
                    waitForLockBean(bean);
                } catch (InterruptedException e) {
                    throw e;
                } finally {
                    waitingThreadsByKey.removeValue(getterId.get(bean), Thread.currentThread());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Method "waitForLock". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * It allows by default reentrant locks from the first locker thread. If you don't want reentrant lock, you have to
     * set <code>allowReentrantLockFromLockerThread</code> = <code>false</code> from the <code>Locker</code>
     * constructor.
     * 
     * @param bean
     * @return true if thread has wait a time, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(KP)}
     */
    @Deprecated
    public boolean waitForLock(KP key) throws InterruptedException {
        return waitForLock(key, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Method "waitForLock". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * It allows by default reentrant locks from the first locker thread. If you don't want reentrant lock, you have to
     * set <code>allowReentrantLockFromLockerThread</code> = <code>false</code> from the <code>Locker</code>
     * constructor.
     * 
     * @param key
     * @param waitTimeMax the maximum time to wait in milliseconds.
     * @return true if thread has wait a time, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object, long)}
     */
    @Deprecated
    public boolean waitForLock(KP key, Long waitTimeMax) throws InterruptedException {
        return waitForLock(key, waitTimeMax, UNDEFINED_CONTEXT_INFO);
    }

    /**
     * Method "waitForLock". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * It allows by default reentrant locks from the first locker thread. If you don't want reentrant lock, you have to
     * set <code>allowReentrantLockFromLockerThread</code> = <code>false</code> from the <code>Locker</code>
     * constructor.
     * 
     * @param key
     * @return true if thread has wait a time, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(KP)}
     */
    @Deprecated
    public boolean waitForLock(KP key, String contextInfo) throws InterruptedException {
        return waitForLock(key, null, contextInfo);
    }

    /**
     * Get locker.
     * 
     * @param bean
     * @return locker value.
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#getLockerValue(Object)}
     */
    @Deprecated
    public LockerValue getLocker(KP key) {
        check(key);
        matchingKey.key = key;
        LockerValue lockingValue = lockKeyToThreadsMap.get(matchingKey);
        return lockingValue;
    }

    /**
     * Method "waitForLock". Lock now if possible with the provided key, else wait for unlocked to lock.
     * 
     * It allows by default reentrant locks from the first locker thread. If you don't want reentrant lock, you have to
     * set <code>allowReentrantLockFromLockerThread</code> = <code>false</code> from the <code>Locker</code>
     * constructor.
     * 
     * @param key
     * @param waitTimeMax the maximum time to wait in milliseconds.
     * @return true if thread has wait a time, else false.
     * @throws InterruptedException
     * @throws IllegalArgumentException if bean is null
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#lockInterruptibly(KP)}
     * or {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey#tryLock(Object, long)} according the case
     * (without or with timeout)
     */
    @Deprecated
    public boolean waitForLock(KP key, final Long waitTimeMax, String contextInfo) throws InterruptedException {
        check(key);
        if (!lockIfUnlocked(key, contextInfo)) {

            waitingThreadsByKey.put(key, Thread.currentThread());
            try {
                if (log.isTraceEnabled()) {
                    log.trace(StringUtils.replacePrms(
                            "Waiting for unlocked ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName() //$NON-NLS-1$
                            , key, contextInfo));
                }
                final Thread mainThread = Thread.currentThread();
                if (waitTimeMax != null) {
                    if (treadsPool == null) {
                        initThreadsPool();
                    }

                    final Thread[] threadInterruptor = new Thread[1];
                    final boolean[] threadAlreadyNotified = new boolean[1];

                    treadsPool.execute(new Runnable() {

                        @Override
                        public void run() {
                            Thread internalThread = Thread.currentThread();
                            threadInterruptor[0] = internalThread;
                            synchronized (Thread.currentThread()) {
                                try {
                                    Thread.currentThread().wait(waitTimeMax);
                                    if (!threadAlreadyNotified[0]) {
                                        mainThread.interrupt();
                                    }
                                } catch (InterruptedException e) {
                                }
                            }
                        }

                    });
                    try {
                        synchronized (mainThread) {
                            mainThread.wait();
                            if (threadInterruptor[0] != null) {
                                threadInterruptor[0].interrupt();
                            }
                        }
                    } catch (InterruptedException e) {
                        throw e;
                    } finally {
                        threadAlreadyNotified[0] = true;
                    }
                } else {
                    synchronized (mainThread) {
                        mainThread.wait();
                    }
                }
                if (log.isTraceEnabled()) {
                    log.trace(StringUtils.replacePrms(
                            "Waiting ended ({0}) key={1}, contextInfo={2}...", Thread.currentThread().getName() //$NON-NLS-1$
                            , key, contextInfo));
                }
                waitForLock(key, contextInfo);
            } catch (InterruptedException e) {
                throw e;
            } finally {
                waitingThreadsByKey.removeValue(key, Thread.currentThread());
            }
            return true;
        }
        return false;
    }

    private void initThreadsPool() {
        treadsPool = Executors.newCachedThreadPool(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread newThread = Executors.defaultThreadFactory().newThread(r);
                newThread.setName(newThread.getName() + "_" + Locker.class.getSimpleName()); //$NON-NLS-1$
                return newThread;
            }

        });
    }

    /**
     * 
     * @deprecated use instead {@link org.talend.commons.utils.threading.lockerbykey.LockerByKey}
     */
    @Deprecated
    public synchronized void shutdown() {
        Object[] values = waitingThreadsByKey.values().toArray(new Object[0]);
        for (Object object : values) {
            if (object instanceof List) {
                List<Thread> list = (List<Thread>) object;
                for (Thread thread : list) {
                    try {
                        thread.interrupt();
                    } catch (SecurityException e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            } else {
                try {
                    ((Thread) object).interrupt();
                } catch (SecurityException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Locker";
    }

    public static void main(String[] args) {

        /**
         * 
         * DOC amaumont Locker class global comment. Detailled comment <br/>
         * 
         */
        class LabelValue {

            private Integer id;

            private String label;

            /**
             * DOC amaumont LabelValue constructor comment.
             * 
             * @param id
             * @param label
             */
            public LabelValue(Integer id, String label) {
                super();
                this.id = id;
                this.label = label;
            }

            /**
             * Getter for id.
             * 
             * @return the id
             */
            public Integer getId() {
                return this.id;
            }

            /**
             * Sets the id.
             * 
             * @param id the id to set
             */
            public void setId(Integer id) {
                this.id = id;
            }

            /**
             * Getter for label.
             * 
             * @return the label
             */
            public String getLabel() {
                return this.label;
            }

            /**
             * Sets the label.
             * 
             * @param label the label to set
             */
            public void setLabel(String label) {
                this.label = label;
            }

        }

        IGetterPropertyAccessor<LabelValue, Integer> getterPropertyAccessor = new IGetterPropertyAccessor<LabelValue, Integer>() {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.utils.data.bean.IGetterPropertyAccessor#get(java.lang.Object)
             */
            @Override
            public Integer get(LabelValue bean) {
                return bean.getId();
            }

        };

        final Locker<LabelValue, Integer> locker = new Locker<LabelValue, Integer>(getterPropertyAccessor);

        final LabelValue lb1 = new LabelValue(1, "LableValue1"); //$NON-NLS-1$

        new Thread() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                locker.lockBean(lb1);
                try {
                    Thread.sleep(10000);
                    locker.unlockBean(lb1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();

        new Thread() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    locker.waitForLockBean(lb1);
                    Thread.sleep(10000);
                    locker.unlockBean(lb1);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }.start();

    }

}
