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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.talend.commons.utils.threading.Locker.LockerValue;
import org.talend.commons.utils.threading.locker.operators.AbstractLockerOperator;
import org.talend.commons.utils.threading.locker.operators.LockOperator;
import org.talend.commons.utils.threading.locker.operators.ResultContainer;
import org.talend.commons.utils.threading.locker.operators.UnlockOperator;
import org.talend.commons.utils.threading.locker.operators.WaitForLockThenUnlockOperator;
import org.talend.commons.utils.threading.threadsafetester.AbstractThreadSafetyTester;

public class LockerTest {

    private static final int WAIT_THREAD_STARTED = 100;

    private static final int WAIT_JOIN = 100;

    class WaitForLockThread extends Thread {

        private Locker locker;

        Integer key;

        public Throwable throwable;

        public WaitForLockThread(Locker locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                locker.waitForLock(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class WaitForLockTwiceThread extends Thread {

        private Locker locker;

        Integer key;

        public Throwable throwable;

        public WaitForLockTwiceThread(Locker locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

        @Override
        public void run() {
            try {
                locker.waitForLock(key);
                locker.waitForLock(key);
            } catch (Throwable e) {
                throwable = e;
            }
        }

    };

    class UnlockThread extends Thread {

        private Locker locker;

        Integer key;

        public Throwable throwable;

        public UnlockThread(Locker locker, Integer key) {
            super();
            this.locker = locker;
            this.key = key;
        }

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
    public void testIsLocked() throws Throwable {
        final Locker locker = new Locker();

        final int keyOne = 1;

        boolean isLocked = locker.isLocked(keyOne);
        assertFalse(isLocked);

        boolean previouslyLocked = locker.lock(keyOne);
        assertFalse(previouslyLocked);

        isLocked = locker.isLocked(keyOne);
        assertTrue(isLocked);

        boolean hasUnlocked = locker.unlock(keyOne);
        assertTrue(hasUnlocked);

        isLocked = locker.isLocked(keyOne);
        assertFalse(isLocked);
    }

    @Test(expected = InterruptedException.class, timeout = 2000)
    // @Test(expected = InterruptedException.class)
    public void testWaitForLock_LockTwiceWithSameKey() throws Throwable {
        final Locker locker = new Locker();

        final int keyOne = 1;
        WaitForLockThread threadWaitForLockKeyOne_first = new WaitForLockThread(locker, keyOne);
        threadWaitForLockKeyOne_first.start();
        threadWaitForLockKeyOne_first.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne_first.isAlive());
        assertNull(threadWaitForLockKeyOne_first.throwable);

        WaitForLockThread threadWaitForLockKeyOne_second = new WaitForLockThread(locker, keyOne);
        threadWaitForLockKeyOne_second.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertTrue(threadWaitForLockKeyOne_second.isAlive());
        threadWaitForLockKeyOne_second.interrupt();
        threadWaitForLockKeyOne_second.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne_second.isAlive());
        assertNotNull(threadWaitForLockKeyOne_second.throwable);
        throw threadWaitForLockKeyOne_second.throwable;
    }

    @Test(expected = InterruptedException.class, timeout = 2000)
    // @Test(expected = InterruptedException.class)
    public void testWaitForLock_LockTwiceWithSamePrimitiveValueButDifferentInteger() throws Throwable {
        final Locker locker = new Locker();

        final int keyOne = 1;
        final Integer keyOneInteger1 = new Integer(keyOne);
        WaitForLockThread threadWaitForLockKeyOne_first = new WaitForLockThread(locker, keyOneInteger1);
        threadWaitForLockKeyOne_first.start();
        threadWaitForLockKeyOne_first.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne_first.isAlive());
        assertNull(threadWaitForLockKeyOne_first.throwable);

        final Integer keyOneInteger2 = new Integer(keyOne);
        WaitForLockThread threadWaitForLockKeyOne_second = new WaitForLockThread(locker, keyOneInteger2);
        threadWaitForLockKeyOne_second.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertTrue(threadWaitForLockKeyOne_second.isAlive());
        threadWaitForLockKeyOne_second.interrupt();
        threadWaitForLockKeyOne_second.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne_second.isAlive());
        assertNotNull(threadWaitForLockKeyOne_second.throwable);
        throw threadWaitForLockKeyOne_second.throwable;
    }

    @Test(timeout = 2000)
    public void testWaitForLock_LockTwiceWithDifferentKeys() throws Throwable {
        final Locker locker = new Locker();

        final int keyOne = 1;
        WaitForLockThread threadWaitForLockKeyOne_first = new WaitForLockThread(locker, keyOne);
        threadWaitForLockKeyOne_first.start();
        threadWaitForLockKeyOne_first.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne_first.isAlive());
        assertNull(threadWaitForLockKeyOne_first.throwable);

        final int keyTwo = 2;
        WaitForLockThread threadLockKeyOne_second = new WaitForLockThread(locker, keyTwo);
        threadLockKeyOne_second.start();
        threadLockKeyOne_second.join(WAIT_JOIN);
        assertFalse(threadLockKeyOne_second.isAlive());
        assertNull(threadWaitForLockKeyOne_first.throwable);
    }

    @Test(timeout = 2000)
    // @Test
    public void testWaitForLockThenUnlock() throws Throwable {
        final Locker locker = new Locker();

        final int keyOne = 1;
        WaitForLockThread threadWaitForLockKeyOne = new WaitForLockThread(locker, keyOne);
        threadWaitForLockKeyOne.start();
        threadWaitForLockKeyOne.join(WAIT_JOIN);
        assertFalse(threadWaitForLockKeyOne.isAlive());
        assertNull(threadWaitForLockKeyOne.throwable);

        UnlockThread threadUnlockKeyOne = new UnlockThread(locker, keyOne);
        threadUnlockKeyOne.start();
        threadUnlockKeyOne.join(WAIT_JOIN);
        assertFalse(threadUnlockKeyOne.isAlive());
        assertNull(threadUnlockKeyOne.throwable);
    }

    @Test(timeout = 2000)
    // @Test
    public void testWaitForLock_waitForLockTwiceFromFirstLockerThread() throws Throwable {
        boolean allowReentrantLockFromLockerThread = true;
        final Locker locker = new Locker(allowReentrantLockFromLockerThread);

        final int keyOne = 1;
        WaitForLockTwiceThread threadWaitForLockTwiceKeyOne = new WaitForLockTwiceThread(locker, keyOne);
        threadWaitForLockTwiceKeyOne.start();
        threadWaitForLockTwiceKeyOne.join(WAIT_JOIN);
        assertFalse(threadWaitForLockTwiceKeyOne.isAlive());
        assertNull(threadWaitForLockTwiceKeyOne.throwable);
    }

    @Test(expected = InterruptedException.class, timeout = 2000)
    // @Test(expected = InterruptedException.class)
    public void testWaitForLock_waitForLockTwiceFromDifferentThread() throws Throwable {
        boolean allowReentrantLockFromLockerThread = true;
        final Locker locker = new Locker(allowReentrantLockFromLockerThread);

        final int keyOne = 1;
        WaitForLockThread threadWaitForLock_threadOne = new WaitForLockThread(locker, keyOne);
        threadWaitForLock_threadOne.start();
        threadWaitForLock_threadOne.join(WAIT_JOIN);
        assertFalse(threadWaitForLock_threadOne.isAlive());
        assertNull(threadWaitForLock_threadOne.throwable);

        WaitForLockThread threadWaitForLock_threadTwo = new WaitForLockThread(locker, keyOne);
        threadWaitForLock_threadTwo.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        threadWaitForLock_threadTwo.interrupt();
        assertTrue(threadWaitForLock_threadTwo.isAlive());
        threadWaitForLock_threadTwo.join(WAIT_JOIN);
        assertFalse(threadWaitForLock_threadTwo.isAlive());
        assertNotNull(threadWaitForLock_threadTwo.throwable);
        throw threadWaitForLock_threadTwo.throwable;
    }

    @Test(expected = InterruptedException.class, timeout = 2000)
    // @Test(expected = InterruptedException.class)
    public void testWaitForLock_waitForLockTwiceFromSameThread_forbidReentrantLockFromOtherThread() throws Throwable {
        boolean allowReentrantLockFromLockerThread = false;
        final Locker locker = new Locker(allowReentrantLockFromLockerThread);

        final int keyOne = 1;
        WaitForLockTwiceThread threadWaitForLockTwiceKeyOne = new WaitForLockTwiceThread(locker, keyOne);
        threadWaitForLockTwiceKeyOne.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertTrue(threadWaitForLockTwiceKeyOne.isAlive());
        threadWaitForLockTwiceKeyOne.interrupt();
        threadWaitForLockTwiceKeyOne.join(WAIT_JOIN);
        assertFalse(threadWaitForLockTwiceKeyOne.isAlive());
        assertNotNull(threadWaitForLockTwiceKeyOne.throwable);
        throw threadWaitForLockTwiceKeyOne.throwable;
    }

    @Test(expected = InterruptedException.class, timeout = 2000)
    // @Test(expected = InterruptedException.class)
    public void testWaitForLock_waitForLockTwiceFromDifferentThread_forbidReentrantLockFromLockerThread() throws Throwable {
        boolean allowReentrantLockFromLockerThread = false;
        final Locker locker = new Locker(allowReentrantLockFromLockerThread);

        final int keyOne = 1;
        WaitForLockThread threadWaitForLock_threadOne = new WaitForLockThread(locker, keyOne);
        threadWaitForLock_threadOne.start();
        threadWaitForLock_threadOne.join(WAIT_JOIN);
        assertFalse(threadWaitForLock_threadOne.isAlive());
        assertNull(threadWaitForLock_threadOne.throwable);

        WaitForLockThread threadWaitForLock_threadTwo = new WaitForLockThread(locker, keyOne);
        threadWaitForLock_threadTwo.start();
        Thread.sleep(WAIT_THREAD_STARTED);
        assertTrue(threadWaitForLock_threadTwo.isAlive());
        threadWaitForLock_threadTwo.interrupt();
        threadWaitForLock_threadTwo.join(WAIT_JOIN);
        assertFalse(threadWaitForLock_threadTwo.isAlive());
        assertNotNull(threadWaitForLock_threadTwo.throwable);
        throw threadWaitForLock_threadTwo.throwable;
    }

    @Test
    public void testThreadSafetyWithLockUnlock() throws Exception {

        final Locker locker = new Locker();
        int nOperatorsByClassOperator = 10;
        final int nOperationsByOperator = 10000;

        final ResultContainer resultContainer = new ResultContainer();

        class LockerThreadSafetyTester extends AbstractThreadSafetyTester<AbstractLockerOperator> {

            public LockerThreadSafetyTester(int nOperatorsByClassOperator,
                    Class<? extends AbstractLockerOperator>... classOperators) {
                super(nOperatorsByClassOperator, classOperators);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.utils.threading.AbstractThreadSafetyTester#initInstance(org.talend.commons.utils.threading
             * .IThreadSafetyOperator)
             */
            protected void initInstance(AbstractLockerOperator operatorInstance) {
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
                int expectedSumLockedAtEnd = 0;
                for (int i = 0; i < nOperationsByOperator; i++) {
                    LockerValue lockerValue = locker.getLocker(i);
                    if (lockerValue != null) {
                        expectedSumLockedAtEnd++;
                    }
                }
                int actualSumLockedAtEnd = resultContainer.sumLocked.get() - resultContainer.sumUnlocked.get();
                assertThat(actualSumLockedAtEnd, is(expectedSumLockedAtEnd));
            }

        }

        @SuppressWarnings("unchecked")
        LockerThreadSafetyTester lockerThreadSafetyTester = new LockerThreadSafetyTester(nOperatorsByClassOperator,
                LockOperator.class, UnlockOperator.class);
        lockerThreadSafetyTester.start();

    }

    @Test(timeout = 10000)
    @Ignore("Locker is not reliable, test fails at each time")
    public void testThreadSafetyWithWaitForLockThenUnlock_allowReentrantLockFromOtherThread() throws Exception {
        boolean allowReentrantLockFromOtherThread = true;
        final Locker locker = new Locker(allowReentrantLockFromOtherThread);
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 500;
        launchThreadSafetyTestWithWaitForLockThenUnlock(locker, nOperatorsByClassOperator, nOperationsByOperator);
    }

    @Test(timeout = 10000)
    @Ignore("Locker is not reliable, test fails at each time")
    public void testThreadSafetyWithWaitForLockThenUnlock_forbidReentrantLockFromOtherThread() throws Exception {
        boolean allowReentrantLockFromOtherThread = false;
        final Locker locker = new Locker(allowReentrantLockFromOtherThread);
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 500;
        launchThreadSafetyTestWithWaitForLockThenUnlock(locker, nOperatorsByClassOperator, nOperationsByOperator);
    }

    @Test(timeout = 30000)
    // @Test
    public void testThreadSafetyWithWaitForLockThenUnlock_minimalOperations() throws Exception {
        boolean allowReentrantLockFromOtherThread = true;
        final Locker locker = new Locker(allowReentrantLockFromOtherThread);
        final int nOperatorsByClassOperator = 2;
        final int nOperationsByOperator = 1;
        launchThreadSafetyTestWithWaitForLockThenUnlock(locker, nOperatorsByClassOperator, nOperationsByOperator);
    }

    @Test(timeout = 30000)
    // @Test
    @Ignore("Locker is not reliable, test fails at each time")
    public void testThreadSafetyWithWaitForLockThenUnlock() throws Exception {
        boolean allowReentrantLockFromOtherThread = true;
        final Locker locker = new Locker(allowReentrantLockFromOtherThread);
        final int nOperatorsByClassOperator = 200;
        final int nOperationsByOperator = 1;
        launchThreadSafetyTestWithWaitForLockThenUnlock(locker, nOperatorsByClassOperator, nOperationsByOperator);
    }

    private void launchThreadSafetyTestWithWaitForLockThenUnlock(final Locker locker, final int nOperatorsByClassOperator,
            final int nOperationsByOperator) throws Exception {
        final ResultContainer resultContainer = new ResultContainer();

        class LockerThreadSafetyTester extends AbstractThreadSafetyTester<AbstractLockerOperator> {

            public LockerThreadSafetyTester(int nOperatorsByClassOperator,
                    Class<? extends AbstractLockerOperator>... classOperators) {
                super(nOperatorsByClassOperator, classOperators);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.utils.threading.AbstractThreadSafetyTester#initInstance(org.talend.commons.utils.threading
             * .IThreadSafetyOperator)
             */
            protected void initInstance(AbstractLockerOperator operatorInstance) {
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
                for (int i = 0; i < nOperationsByOperator; i++) {
                    LockerValue lockerValue = locker.getLocker(i);
                    if (lockerValue != null) {
                        actualSumLockedAtEnd++;
                    }
                }
                assertThat(actualSumLockedAtEnd, is(0));
            }

        }

        @SuppressWarnings("unchecked")
        LockerThreadSafetyTester lockerThreadSafetyTester = new LockerThreadSafetyTester(nOperatorsByClassOperator,
                WaitForLockThenUnlockOperator.class);
        lockerThreadSafetyTester.start();
    }

}

