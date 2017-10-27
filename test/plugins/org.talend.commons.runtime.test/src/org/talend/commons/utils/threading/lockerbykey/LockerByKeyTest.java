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
import org.junit.Test;
import org.talend.commons.utils.threading.lockerbykey.operators.AbstractLockerByKeyOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.CleanOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.LockThenUnlockOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.ResultContainer;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockThenUnlockOperator;
import org.talend.commons.utils.threading.lockerbykey.operators.TryLockWithTimeoutThenUnlockOperator;
import org.talend.commons.utils.threading.threadsafetester.AbstractThreadSafetyTester;

import com.javamex.classmexer.MemoryUtil;

public class LockerByKeyTest {

    // @Rule
    // public MethodRule globalTimeout = new Timeout(60000);

    private static final int WAIT_THREAD_STARTED = 200;

    private static final int WAIT_JOIN = 200;

    private static final boolean DEBUG = false;

//    static {
//        MemoryUtilAgentLoader.loadAgent();
//    }

    protected ILockerByKey createLockerInstance() {
        // default implementation when running this TestCase
        return new LockerByKey();
    }

    class LockInterruptiblyThread extends Thread {

        private ILockerByKey locker;

        Integer key;

        public Throwable throwable;

        public LockInterruptiblyThread(ILockerByKey locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                locker.lockInterruptibly(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class LockInterruptiblyTwiceThread extends Thread {

        private ILockerByKey locker;

        Integer key;

        public Throwable throwable;

        public LockInterruptiblyTwiceThread(ILockerByKey locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @Override
        public void run() {
            try {
                locker.lockInterruptibly(key);
                locker.lockInterruptibly(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class UnlockThread extends Thread {

        private ILockerByKey locker;

        Integer key;

        public Throwable throwable;

        public UnlockThread(ILockerByKey locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                locker.unlock(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testIsLocked_with_Lock_then_Unlock() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        locker.lockInterruptibly(keyOne);

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        boolean hasUnlocked = locker.unlock(keyOne);
        assertThat(hasUnlocked, is(true));

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));
    }

    @SuppressWarnings("unchecked")
    // @Test(timeout = 2000)
    @Test()
    public void testLockWithThread1_and_UnlockWithThread2() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        locker.lockInterruptibly(keyOne);

        isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(true));

        ExecutorService poolRunnableUnlock = Executors.newCachedThreadPool();

        // UNLOCK STEP
        Callable<Boolean> runnableUnlock = new Callable<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.Callable#call()
             */
            @Override
            public Boolean call() throws Exception {
                return locker.unlock(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnableUnlock.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        try {
            futureUnlock.get(2, TimeUnit.SECONDS);
            fail("expected = IllegalMonitorStateException.class");
        } catch (Exception e) {
            if (!(e.getCause() instanceof IllegalMonitorStateException)) {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testTryLockWithThread1_and_UnlockWithThread2() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        boolean locked = locker.tryLock(keyOne);
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
                return locker.unlock(keyOne);
            }
        };
        Future<Boolean> futureUnlock = poolRunnable2.submit(runnableUnlock);
        Thread.sleep(WAIT_THREAD_STARTED);

        try {
            futureUnlock.get(2, TimeUnit.SECONDS);
            fail("expected = IllegalMonitorStateException.class");
        } catch (Exception e) {
            if (!(e.getCause() instanceof IllegalMonitorStateException)) {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testUnlock_with_noPreviousLock() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        boolean unlocked = locker.unlock(keyOne);
        assertThat(unlocked, is(false));
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testCleanEnabledDefault() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        for (int i = 0; i < 1000; i++) {
            locker.lockInterruptibly(i);

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(true));

            boolean hasUnlocked = locker.unlock(i);
            assertThat(hasUnlocked, is(true));

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(false));
        }

        /*
         * Be warn, the last is not removed by the auto clean, need to call clean again to clear all.
         */
        for (int i = 0; i < 1000; i++) {
            assertThat(locker.getLockerValue(i), is(nullValue()));
        }

    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 2000)
    // @Test()
    public void testCleanEnabledProvidedPeriod() throws Throwable {
        final ILockerByKey<Integer> locker = new LockerByKey<Integer>(true, 600);

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertThat(isLocked, is(false));

        for (int i = 0; i < 1000; i++) {
            locker.lockInterruptibly(i);

            isLocked = locker.isLocked(i);
            assertThat(isLocked, is(true));

            boolean hasUnlocked = locker.unlock(i);
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
    public void testShutdown_Lock() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

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
                locker.lockInterruptibly(keyOne);
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
    public void testShutdown_TryLockWithTimeout() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

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
                locker.tryLock(keyOne, 10000);
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
    public void testUnlock_withGetLockerValue() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;

        LockerValue lockerValue = locker.getLockerValue(keyOne);
        assertThat(lockerValue, is(nullValue()));

        locker.lockInterruptibly(keyOne);

        LockerValue lockerValue1 = locker.getLockerValue(keyOne);
        assertThat(lockerValue1, is(notNullValue()));

        boolean hasUnlocked = locker.unlock(keyOne);
        assertThat(hasUnlocked, is(true));

        LockerValue lockerValue2 = locker.getLockerValue(keyOne);
        assertThat(lockerValue2, is(lockerValue1));

        locker.shutdown();
        LockerValue lockerValue3 = locker.getLockerValue(keyOne);
        assertThat(lockerValue3, is(nullValue()));

    }

    @Test(timeout = 2000)
    // @Test()
    public void testLock_LockTwiceWithSameKey() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyThread threadLockKeyOne_first = new LockInterruptiblyThread(locker, keyOne);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        LockInterruptiblyThread threadLockKeyOne_second = new LockInterruptiblyThread(locker, keyOne);
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
    public void testLock_LockTwiceWithSamePrimitiveValueButDifferentInteger() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        final Integer keyOneInteger1 = new Integer(keyOne);
        LockInterruptiblyThread threadLockKeyOne_first = new LockInterruptiblyThread(locker, keyOneInteger1);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        final Integer keyOneInteger2 = new Integer(keyOne);
        LockInterruptiblyThread threadLockKeyOne_second = new LockInterruptiblyThread(locker, keyOneInteger2);
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
    public void testLock_LockTwiceWithDifferentKeys() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyThread threadLockKeyOne_first = new LockInterruptiblyThread(locker, keyOne);
        threadLockKeyOne_first.start();
        threadLockKeyOne_first.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_first.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

        final int keyTwo = 2;
        LockInterruptiblyThread threadLockKeyOne_second = new LockInterruptiblyThread(locker, keyTwo);
        threadLockKeyOne_second.start();
        threadLockKeyOne_second.join(WAIT_JOIN);
        assertThat(threadLockKeyOne_second.isAlive(), is(false));
        if (threadLockKeyOne_first.throwable != null) {
            throw threadLockKeyOne_first.throwable;
        }

    }

    @Test(timeout = 2000)
    // @Test
    public void testLock_ThenUnlockFromOtherThread() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyThread threadLockKeyOne = new LockInterruptiblyThread(locker, keyOne);
        threadLockKeyOne.start();
        threadLockKeyOne.join(WAIT_JOIN);
        assertThat(threadLockKeyOne.isAlive(), is(false));
        if (threadLockKeyOne.throwable != null) {
            throw threadLockKeyOne.throwable;
        }

        UnlockThread threadUnlockKeyOne = new UnlockThread(locker, keyOne);
        threadUnlockKeyOne.start();
        threadUnlockKeyOne.join(WAIT_JOIN);
        assertThat(threadUnlockKeyOne.isAlive(), is(false));
        assertThat(threadUnlockKeyOne.throwable, is(instanceOf(IllegalMonitorStateException.class)));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testLock_LockTwiceFromDifferentThread() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyThread threadLock_threadOne = new LockInterruptiblyThread(locker, keyOne);
        threadLock_threadOne.start();
        threadLock_threadOne.join(WAIT_JOIN);
        assertThat(threadLock_threadOne.isAlive(), is(false));
        if (threadLock_threadOne.throwable != null) {
            throw threadLock_threadOne.throwable;
        }

        LockInterruptiblyThread threadLock_threadTwo = new LockInterruptiblyThread(locker, keyOne);
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
    public void testLock_LockTwiceFromSameThread_forbidReentrantLockFromOtherThread() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyTwiceThread threadLockTwiceKeyOne = new LockInterruptiblyTwiceThread(locker, keyOne);
        threadLockTwiceKeyOne.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertThat(threadLockTwiceKeyOne.isAlive(), is(false));
        assertThat(threadLockTwiceKeyOne.throwable, is(nullValue()));
    }

    @Test(timeout = 2000)
    // @Test()
    public void testLock_LockTwiceFromDifferentThread_forbidReentrantLockFromLockerThread() throws Throwable {
        final ILockerByKey locker = createLockerInstance();

        final int keyOne = 1;
        LockInterruptiblyThread threadLock_threadOne = new LockInterruptiblyThread(locker, keyOne);
        threadLock_threadOne.start();
        threadLock_threadOne.join(WAIT_JOIN);
        assertThat(threadLock_threadOne.isAlive(), is(false));
        if (threadLock_threadOne.throwable != null) {
            throw threadLock_threadOne.throwable;
        }

        LockInterruptiblyThread threadLock_threadTwo = new LockInterruptiblyThread(locker, keyOne);
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
    public void testGetSuspectLocks() throws Exception {
        final ILockerByKey<Integer> locker = createLockerInstance();
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test(timeout = 20000)
    // @Test
    public void testThreadSafety_LockThenUnlock() throws Exception {
        final ILockerByKey locker = createLockerInstance();
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 500;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = true;
        launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                shutdownAtEnd, LockThenUnlockOperator.class, TryLockWithTimeoutThenUnlockOperator.class,
                TryLockThenUnlockOperator.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test(timeout = 20000)
    // @Test
    public void testThreadSafety_LockThenUnlock_with_randomClean() throws Exception {
        final ILockerByKey locker = createLockerInstance();
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 500;
        boolean assertEntriesLessThanCleanPeriod = true;
        boolean shutdownAtEnd = true;
        launchThreadSafetyTest(locker, nOperatorsByClassOperator, nOperationsByOperator, assertEntriesLessThanCleanPeriod,
                shutdownAtEnd, LockThenUnlockOperator.class, TryLockWithTimeoutThenUnlockOperator.class,
                TryLockThenUnlockOperator.class, CleanOperator.class);
    }

    /**
     * Test to show the increasing memory with old Locker and stable memory with new LockerByKey
     * 
     * @throws Exception
     */
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
                    shutdownAtEnd, LockThenUnlockOperator.class, TryLockWithTimeoutThenUnlockOperator.class,
                    TryLockThenUnlockOperator.class);
            locker.clean();
            System.gc();
            Thread.sleep(500); // memory seems more stable when waiting a bit
            long deepMemoryUsageOfLocker = MemoryUtil.deepMemoryUsageOf(locker);
            System.out.println("deepMemoryUsageOfLocker=" + deepMemoryUsageOfLocker);
            if (memoryResults.contains(deepMemoryUsageOfLocker)) {
                break;
            } else if (i == maxMesureRounds - 1) {
                fail("Used memory is unstable after " + maxMesureRounds + " rounds");
            }
            memoryResults.add(deepMemoryUsageOfLocker);
        }

        locker.shutdown();
    }

    protected void launchThreadSafetyTest(final ILockerByKey locker, final int nOperatorsByClassOperator,
            final int nOperationsByOperator, boolean assertEntriesLessThanCleanPeriod, final boolean shutdownAtEnd,
            Class<? extends AbstractLockerByKeyOperator>... classOperators) throws Exception {
        final ResultContainer resultContainer = new ResultContainer();

        System.out.println("------------------------------------------------------------------------------------");

        class LockerThreadSafetyTester extends AbstractThreadSafetyTester<AbstractLockerByKeyOperator> {

            private boolean assertEntriesLessThanCleanPeriod;

            public LockerThreadSafetyTester(int nOperatorsByClassOperator, boolean assertEntriesLessThanCleanPeriod,
                    Class<? extends AbstractLockerByKeyOperator>... classOperators) {
                super(nOperatorsByClassOperator, classOperators);
                this.assertEntriesLessThanCleanPeriod = assertEntriesLessThanCleanPeriod;
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.utils.threading.AbstractThreadSafetyTester#initInstance(org.talend.commons.utils.threading
             * .IThreadSafetyOperator)
             */
            protected void initInstance(AbstractLockerByKeyOperator operatorInstance) {
                operatorInstance.setDebug(DEBUG);
                operatorInstance.setLocker(locker);
                operatorInstance.setnOperationsByOperator(nOperationsByOperator);
                operatorInstance.setResultContainer(resultContainer);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.utils.threading.AbstractThreadSafetyTester#assertFinal()
             */
            @Override
            protected void assertFinal() {
                int actualSumLockedAtEnd = 0;
                int actualSumLockersAtEnd = 0;
                for (int i = 0; i < nOperationsByOperator; i++) {
                    LockerValue lockerValue = locker.getLockerValue(i);
                    if (lockerValue != null) {
                        int queueLength = lockerValue.getLock().getQueueLength();
                        if (lockerValue != null) {
                            actualSumLockedAtEnd += queueLength;
                        }
                        actualSumLockersAtEnd++;
                    }
                }
                assertThat(actualSumLockedAtEnd, is(0));
                if (assertEntriesLessThanCleanPeriod) {
                    assertTrue("actualSumLockersAtEnd > locker.getCleanPeriod() where actualSumLockersAtEnd="
                            + actualSumLockersAtEnd + " and locker.getCleanPeriod()=" + locker.getCleanPeriod(),
                            actualSumLockersAtEnd < locker.getCleanPeriod());
                }

                if (shutdownAtEnd) {
                    locker.shutdown();
                } else {
                    locker.clean();
                }
                actualSumLockedAtEnd = 0;
                actualSumLockersAtEnd = 0;
                for (int i = 0; i < nOperationsByOperator; i++) {
                    LockerValue lockerValue = locker.getLockerValue(i);
                    if (lockerValue != null) {
                        int queueLength = lockerValue.getLock().getQueueLength();
                        if (lockerValue != null) {
                            actualSumLockedAtEnd += queueLength;
                        }
                        actualSumLockersAtEnd++;
                    }
                }
                assertThat(actualSumLockedAtEnd, is(0));
                assertThat(actualSumLockersAtEnd, is(0));

                assertThat(AbstractLockerByKeyOperator.getNotThreadSafeCounter(),
                        is(resultContainer.sumThreadSafeOperations.get()));
                System.out.println("Total of operations done: " + resultContainer.sumThreadSafeOperations.get());
                System.out.println("Average duration: "
                        + ((double) getDuration() / (double) resultContainer.sumThreadSafeOperations.get()) + " ms by operation");
            }

        }

        @SuppressWarnings("unchecked")
        LockerThreadSafetyTester lockerThreadSafetyTester = new LockerThreadSafetyTester(nOperatorsByClassOperator,
                assertEntriesLessThanCleanPeriod, classOperators);
        lockerThreadSafetyTester.start();
    }
}
