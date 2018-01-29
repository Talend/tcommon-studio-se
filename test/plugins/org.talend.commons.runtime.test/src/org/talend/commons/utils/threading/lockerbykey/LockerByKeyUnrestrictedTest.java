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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.talend.commons.utils.threading.lockerbykey.operators.CleanOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.LockThenUnlockOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.LockThenUnlockUnrestrictedOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockThenUnlockOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockThenUnlockUnrestrictedOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockWithTimeoutThenUnlockOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockWithTimeoutThenUnlockUnrestrictedOperator;

import com.javamex.classmexer.MemoryUtil;

public class LockerByKeyUnrestrictedTest extends LockerByKeyTest {

    @Rule
    public TestRule globalTimeout = new Timeout(60000);

    private static final int WAIT_THREAD_STARTED = 100;

    private static final int WAIT_JOIN = 100;

    private static final boolean DEBUG = false;

    @Override
    protected ILockerByKey createLockerInstance() {
        // default implementation when running this TestCase
        return createLockerUnrestrictedInstance();
    }

    protected LockerByKeyUnrestricted createLockerUnrestrictedInstance() {
        // default implementation when running this TestCase
        return new LockerByKeyUnrestricted();
    }

    class LockInterruptiblyUnrestrictedThread extends Thread {

        private LockerByKeyUnrestricted locker;

        Integer key;

        public Throwable throwable;

        public LockInterruptiblyUnrestrictedThread(LockerByKeyUnrestricted locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                locker.lockInterruptiblyUnrestricted(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class LockInterruptiblyUnrestrictedTwiceThread extends Thread {

        private LockerByKeyUnrestricted locker;

        Integer key;

        public Throwable throwable;

        public LockInterruptiblyUnrestrictedTwiceThread(LockerByKeyUnrestricted locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @Override
        public void run() {
            try {
                locker.lockInterruptiblyUnrestricted(key);
                locker.lockInterruptiblyUnrestricted(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class UnlockUnrestrictedThread extends Thread {

        private LockerByKeyUnrestricted locker;

        Integer key;

        public Throwable throwable;

        public UnlockUnrestrictedThread(LockerByKeyUnrestricted locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                locker.unlockUnrestricted(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    @SuppressWarnings("unchecked")
    // @Test(timeout = 2000)
    @Test()
    public void testUnrestricted_IsLocked_with_Lock_then_Unlock() throws Throwable {

        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        locker.lockInterruptiblyUnrestricted(keyOne);

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        boolean hasUnlocked = locker.unlockUnrestricted(keyOne);
        assertThat(hasUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_LockWithThread1_and_UnlockWithThread2() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        locker.lockInterruptiblyUnrestricted(keyOne);

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        // UNLOCK STEP
        Callable<Boolean> runnableUnlock = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                return locker.unlockUnrestricted(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnable2.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        Boolean resultUnlocked = futureUnlock.get(2, TimeUnit.SECONDS);
        assertThat(resultUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_TryLockWithThread1_and_UnlockWithThread2() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        boolean locked = locker.tryLockUnrestricted(keyOne);
        assertThat(locked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        // UNLOCK STEP
        Callable<Boolean> runnableUnlock = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                return locker.unlockUnrestricted(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnable2.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        Boolean resultUnlocked = futureUnlock.get(2, TimeUnit.SECONDS);
        assertThat(resultUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_TryLockTimeoutWithThread1_and_UnlockWithThread2() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        boolean locked = locker.tryLockUnrestricted(keyOne);
        assertThat(locked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        // UNLOCK STEP
        Callable<Boolean> runnableUnlock = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                return locker.unlockUnrestricted(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnable2.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        Boolean resultUnlocked = futureUnlock.get(2, TimeUnit.SECONDS);
        assertThat(resultUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

    }

    @SuppressWarnings("unchecked")
    // @Test(timeout = 2000)
    @Test()
    public void testUnrestricted_LockThread1_then_TryLockTimeoutWithThread2_then_UnlockThread1_then_UnlockWithThread3()
            throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();
        // final int baseTimeoutSeconds = 2;
        final int baseTimeoutSeconds = Integer.MAX_VALUE;

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        // STEP 1
        locker.lockInterruptiblyUnrestricted(keyOne);

        ExecutorService poolRunnableTryLockTimeout = Executors.newCachedThreadPool();

        // TRY LOCK TIMEOUT STEP
        Callable<Boolean> runnableTryLockTimeout = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                // STEP 2
                boolean locked = locker.tryLockUnrestricted(keyOne, baseTimeoutSeconds, TimeUnit.SECONDS);
                // STEP 4
                return locked;

            }
        };
        Future<Boolean> futureTryLockTimeout = poolRunnableTryLockTimeout.submit(runnableTryLockTimeout);
        Thread.sleep(WAIT_THREAD_STARTED);

        // STEP 3
        boolean unlocked = locker.unlockUnrestricted(keyOne);
        assertThat(unlocked, is(true));

        // STEP 5
        Boolean locked = futureTryLockTimeout.get(baseTimeoutSeconds, TimeUnit.SECONDS);
        assertThat(locked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        // UNLOCK STEP
        Callable<Boolean> runnableUnlock = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                // STEP 6
                return locker.unlockUnrestricted(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnable2.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        // STEP 7
        Boolean resultUnlocked = futureUnlock.get(baseTimeoutSeconds, TimeUnit.SECONDS);
        assertThat(resultUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000, expected = IllegalStateException.class)
    // @Test(expected = IllegalStateException.class)
    public void testUnrestricted_Unlock_with_noPreviousLock() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        locker.unlockUnrestricted(keyOne);
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_CleanEnabledDefault() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        for (int i = 0; i < 1000; i++) {
            locker.lockInterruptiblyUnrestricted(i);

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(true));

            boolean hasUnlocked = locker.unlockUnrestricted(i);
            assertThat(hasUnlocked, is(true));

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(false));
        }

        /*
         * Be warn, the last is not removed by the auto clean, need to call clean again to clear all.
         */
        for (int i = 0; i < 999; i++) {
            assertThat(locker.getLockerValue(i), is(nullValue()));
        }

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_CleanEnabledProvidedPeriod() throws Throwable {
        final LockerByKeyUnrestricted locker = new LockerByKeyUnrestricted(true, 600);

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        for (int i = 0; i < 1000; i++) {
            locker.lockInterruptiblyUnrestricted(i);

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(true));

            boolean hasUnlocked = locker.unlockUnrestricted(i);
            assertThat(hasUnlocked, is(true));

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(false));
        }

        /*
         * Be warn, the last is not removed by the auto clean, need to call clean again to clear all.
         */
        for (int i = 0; i < 1000; i++) {
            if (i < 600) {
                assertThat(locker.getLockerValue(i), is(nullValue()));
            } else {
                assertThat(locker.getLockerValue(i), is(notNullValue()));
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Shutdown_with_waiting_Lock() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        ExecutorService poolRunnable1 = Executors.newCachedThreadPool();
        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        final int keyOne = 1;
        final String expectedResult = "ended";
        Callable<String> runnable = new Callable<String>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public String call() throws Exception {
                locker.lockInterruptiblyUnrestricted(keyOne);
                return expectedResult;
            }
        };

        Future<String> future1 = poolRunnable1.submit(runnable);
        Thread.sleep(WAIT_THREAD_STARTED);
        String result1 = future1.get(2, TimeUnit.SECONDS);
        assertThat(result1, is(expectedResult));
        poolRunnable1.shutdown();

        Future<String> future2 = poolRunnable2.submit(runnable);
        Thread.sleep(WAIT_THREAD_STARTED);

        locker.shutdown();

        try {
            future2.get(2, TimeUnit.SECONDS);
            fail("Should be interrupted by the shutdown");
        } catch (Exception e) {
            boolean isInterruptedException = e.getCause() instanceof InterruptedException;
            if (!isInterruptedException) {
                e.printStackTrace();
                fail("Should be an InterruptedException, please read logs");
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Shutdown_TryLockWithTimeout() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        ExecutorService poolRunnable1 = Executors.newCachedThreadPool();
        ExecutorService poolRunnable2 = Executors.newCachedThreadPool();

        final int keyOne = 1;
        final String expectedResult = "ended";
        Callable<String> runnable = new Callable<String>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public String call() throws Exception {
                locker.tryLockUnrestricted(keyOne, 10000);
                return expectedResult;
            }
        };

        Future<String> future1 = poolRunnable1.submit(runnable);
        Thread.sleep(WAIT_THREAD_STARTED);
        String result1 = future1.get(2, TimeUnit.SECONDS);
        assertThat(result1, is(expectedResult));

        Future<String> future2 = poolRunnable2.submit(runnable);
        Thread.sleep(WAIT_THREAD_STARTED);

        locker.shutdown();

        try {
            future2.get(2, TimeUnit.SECONDS);
            fail("Should be interrupted by the shutdown");
        } catch (Exception e) {
            boolean isInterruptedException = e.getCause() instanceof InterruptedException;
            if (!isInterruptedException) {
                e.printStackTrace();
                fail("Should be an InterruptedException, please read logs");
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Unlock_withGetLockerValue() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;

        LockerValue lockerValue = locker.getLockerValue(keyOne);
        assertThat(lockerValue, is(nullValue()));

        locker.lockInterruptiblyUnrestricted(keyOne);

        LockerValue lockerValue1 = locker.getLockerValue(keyOne);
        assertThat(lockerValue1, is(notNullValue()));

        boolean hasUnlocked = locker.unlockUnrestricted(keyOne);
        assertThat(hasUnlocked, is(true));

        LockerValue lockerValue2 = locker.getLockerValue(keyOne);
        assertThat(lockerValue2, is(lockerValue1));

        locker.shutdown();
        LockerValue lockerValue3 = locker.getLockerValue(keyOne);
        assertThat(lockerValue3, is(nullValue()));

    }

    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Lock_LockTwiceWithSameKey() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedThread threadLockKeyOne_first = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        LockInterruptiblyUnrestrictedThread threadLockKeyOne_second = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLockKeyOne_second.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertThat(threadLockKeyOne_second.isAlive(), is(true));
        threadLockKeyOne_second.interrupt();
        threadLockKeyOne_second.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_second.isAlive(), is(false));
        assertThat(threadLockKeyOne_second.throwable, is(notNullValue()));
        assertThat(threadLockKeyOne_second.throwable, is(instanceOf(InterruptedException.class)));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Lock_LockTwiceWithSamePrimitiveValueButDifferentInteger() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        final Integer keyOneInteger1 = new Integer(keyOne);
        LockInterruptiblyUnrestrictedThread threadLockKeyOne_first = new LockInterruptiblyUnrestrictedThread(locker,
                keyOneInteger1);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        final Integer keyOneInteger2 = new Integer(keyOne);
        LockInterruptiblyUnrestrictedThread threadLockKeyOne_second = new LockInterruptiblyUnrestrictedThread(locker,
                keyOneInteger2);
        threadLockKeyOne_second.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertThat(threadLockKeyOne_second.isAlive(), is(true));
        threadLockKeyOne_second.interrupt();
        threadLockKeyOne_second.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_second.isAlive(), is(false));
        assertThat(threadLockKeyOne_second.throwable, is(notNullValue()));
        assertThat(threadLockKeyOne_second.throwable, is(instanceOf(InterruptedException.class)));
    }

    @Test(timeout = 2000)
    public void testUnrestricted_Lock_LockTwiceWithDifferentKeys() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedThread threadLockKeyOne_first = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        final int keyTwo = 2;
        LockInterruptiblyUnrestrictedThread threadLockKeyOne_second = new LockInterruptiblyUnrestrictedThread(locker, keyTwo);
        threadLockKeyOne_second.start();
        threadLockKeyOne_second.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_second.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

    }

    @Test(timeout = 2000)
    // @Test
    public void testUnrestricted_Lock_ThenUnlockFromOtherThread() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedThread threadLockKeyOne = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLockKeyOne.start();
        threadLockKeyOne.join(WAIT_JOIN);
        assertThat(threadLockKeyOne.isAlive(), is(false));
        if (threadLockKeyOne.throwable != null) {
            throw threadLockKeyOne.throwable;
        }

        UnlockUnrestrictedThread threadUnlockKeyOne = new UnlockUnrestrictedThread(locker, keyOne);
        threadUnlockKeyOne.start();
        threadUnlockKeyOne.join(WAIT_JOIN);
        assertThat(threadUnlockKeyOne.isAlive(), is(false));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Lock_LockTwiceFromDifferentThread() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedThread threadLock_threadOne = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLock_threadOne.start();
        threadLock_threadOne.join(WAIT_JOIN);
        assertThat(threadLock_threadOne.isAlive(), is(false));
        if (threadLock_threadOne.throwable != null) {
            throw threadLock_threadOne.throwable;
        }

        LockInterruptiblyUnrestrictedThread threadLock_threadTwo = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLock_threadTwo.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        threadLock_threadTwo.interrupt();
        assertThat(threadLock_threadTwo.isAlive(), is(true));
        threadLock_threadTwo.join(WAIT_JOIN);
        assertThat(threadLock_threadTwo.isAlive(), is(false));
        assertThat(threadLock_threadTwo.throwable, is(notNullValue()));
        assertThat(threadLock_threadTwo.throwable, is(instanceOf(InterruptedException.class)));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Lock_LockTwiceFromSameThread_allowReentrantLockFromOtherThread() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedTwiceThread threadLockTwiceKeyOne = new LockInterruptiblyUnrestrictedTwiceThread(locker,
                keyOne);
        threadLockTwiceKeyOne.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertThat(threadLockTwiceKeyOne.isAlive(), is(false));
        assertThat(threadLockTwiceKeyOne.throwable, is(nullValue()));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testUnrestricted_Lock_LockTwiceFromDifferentThread_forbidReentrantLockFromLockerThread() throws Throwable {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();

        final int keyOne = 1;
        LockInterruptiblyUnrestrictedThread threadLock_threadOne = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLock_threadOne.start();
        threadLock_threadOne.join(WAIT_JOIN);
        assertThat(threadLock_threadOne.isAlive(), is(false));
        if (threadLock_threadOne.throwable != null) {
            throw threadLock_threadOne.throwable;
        }

        LockInterruptiblyUnrestrictedThread threadLock_threadTwo = new LockInterruptiblyUnrestrictedThread(locker, keyOne);
        threadLock_threadTwo.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertThat(threadLock_threadTwo.isAlive(), is(true));
        threadLock_threadTwo.interrupt();
        threadLock_threadTwo.join(WAIT_JOIN);
        assertThat(threadLock_threadTwo.isAlive(), is(false));
        assertThat(threadLock_threadTwo.throwable, is(notNullValue()));
        assertThat(threadLock_threadTwo.throwable, is(instanceOf(InterruptedException.class)));
    }

    @Test(timeout = 1000)
    // @Test
    public void testUnrestricted_GetSuspectLocks() throws Exception {
        LockerByKeyUnrestricted<Integer> locker = createLockerUnrestrictedInstance();
        locker.setDetectSuspectLocks(true);
        int keyOne = 1;

        locker.lockInterruptibly(keyOne);
        List<LockerValue<Integer>> suspectLocks = locker.getSuspectLocks(1000);
        assertThat(suspectLocks.size(), is(0));

        Thread.sleep(100);
        suspectLocks = locker.getSuspectLocks(50);
        assertThat(suspectLocks.size(), is(1));

        locker.unlock(keyOne);
        suspectLocks = locker.getSuspectLocks(50);
        assertThat(suspectLocks.size(), is(0));
    }

    @Test(timeout = 30000)
    // @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testUnrestricted_ThreadSafety_LockThenUnlock_TryLockTimeoutThenUnlock_TryLockThenUnlock_autoClean()
            throws Exception {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 250;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = true;
        boolean warmupRound = false;
        launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                shutdownAtEnd, LockThenUnlockOperator.class, TryLockWithTimeoutThenUnlockOperator.class,
                TryLockThenUnlockOperator.class);
    }

    @Test(timeout = 30000)
    // @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testUnrestricted_ThreadSafety_LockThenUnlock_TryLockTimeoutThenUnlock_TryLockThenUnlock_with_randomClean()
            throws Exception {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 250;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = true;
        boolean warmupRound = false;
        launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                shutdownAtEnd, LockThenUnlockOperator.class, TryLockWithTimeoutThenUnlockOperator.class,
                TryLockThenUnlockOperator.class, CleanOperator.class);
    }

    @Test(timeout = 30000)
    // @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testUnrestricted_ThreadSafety_LockThenUnlock_TryLockTimeoutThenUnlock_TryLockThenUnlock_with_randomClean_mixedRestrictedUnrestricted()
            throws Exception {
        final LockerByKeyUnrestricted locker = createLockerUnrestrictedInstance();
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 250;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = true;
        boolean warmupRound = false;
        launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                shutdownAtEnd, LockThenUnlockOperator.class, LockThenUnlockUnrestrictedOperator.class,
                TryLockWithTimeoutThenUnlockOperator.class, TryLockWithTimeoutThenUnlockUnrestrictedOperator.class,
                TryLockThenUnlockOperator.class, TryLockThenUnlockUnrestrictedOperator.class, CleanOperator.class);
    }

    /**
     * Test to show the increasing memory with old Locker and stable memory with new LockerByKey
     * 
     * @throws Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    @Test(timeout = 30000)
    // @Test
    @Ignore("Ignored while JUnit buils is not modified to allow measure of memory")
    public void testMemory() throws Exception {
        ILockerByKey locker = createLockerInstance();

        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 50;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = false;

        long previousDeepMemoryUsageOfLocker = MemoryUtil.deepMemoryUsageOf(locker);
        System.out.println("deepMemoryUsageOfLocker=" + previousDeepMemoryUsageOfLocker);
        Set<Long> memoryResults = new HashSet<Long>();

        int maxMesureRounds = 10;
        for (int i = 0; i < maxMesureRounds; i++) {
            launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                    shutdownAtEnd, LockThenUnlockOperator.class, LockThenUnlockUnrestrictedOperator.class,
                    TryLockWithTimeoutThenUnlockOperator.class, TryLockWithTimeoutThenUnlockUnrestrictedOperator.class,
                    TryLockThenUnlockOperator.class, TryLockThenUnlockUnrestrictedOperator.class);
            locker.clean();
            System.gc();
            Thread.sleep(500); // memory seems more stable when waiting a bit
            long deepMemoryUsageOfLocker = MemoryUtil.deepMemoryUsageOf(locker);
            System.out.println("deepMemoryUsageOfLocker=" + deepMemoryUsageOfLocker);
            if (memoryResults.contains(deepMemoryUsageOfLocker)) {
                break;
            } else if (i == maxMesureRounds - 1) {
                fail("Used memory exceeds the expected: deepMemoryUsageOfLocker > deepMemoryUsageOfLockerAtStart "
                        + deepMemoryUsageOfLocker + " > " + previousDeepMemoryUsageOfLocker);
            }
            memoryResults.add(deepMemoryUsageOfLocker);
        }

        locker.shutdown();
    }
}
