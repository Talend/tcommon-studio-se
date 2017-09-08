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
package org.talend.commons.utils.tracer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * DOC amaumont class global comment. Detailled comment
 */
public class StatisticsTracerTest {

    private static final String MY_TRACER_TEST1 = "myTracerTest1";

    private static final String MY_TRACER_TEST2 = "myTracerTest2";

    private static Logger log = Logger.getLogger(StatisticsTracerTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testStatisticTracer() throws InterruptedException {

        String tracerId = "testStatisticTracer_" + MY_TRACER_TEST1;
        StatisticsTracer myTracerTest1 = StatisticsTracer.createTracer(tracerId);

        int sleepTime = 100;
        int executionsCount = 10;

        for (int i = 0; i < 10; i++) {
            long id = myTracerTest1.start();
            Thread.sleep(sleepTime);
            myTracerTest1.stop(id);
        }

        long averageWorkTime = myTracerTest1.getAverageWorkTime();
        assertTrue(averageWorkTime >= sleepTime && averageWorkTime < sleepTime + 10);

        int exepectedElapsedTimeSinceFirstStart = sleepTime * executionsCount;
        long elapsedTimeSinceFirstStart = myTracerTest1.getElapsedTimeSinceFirstStart();
        assertTrue(elapsedTimeSinceFirstStart >= exepectedElapsedTimeSinceFirstStart
                && elapsedTimeSinceFirstStart < exepectedElapsedTimeSinceFirstStart + 50);

        long countExecutions = myTracerTest1.getCountExecutions();
        assertEquals(executionsCount, countExecutions);

        StatisticsTracer.removeTracer(tracerId);

    }

    @Test
    public void testStatisticTracerSimple() throws InterruptedException {

        String tracerId = "testStatisticTracerSimple_" + MY_TRACER_TEST1;
        StatisticsTracer myTracerTest1 = StatisticsTracer.createTracer(tracerId);

        int sleepTime = 100;
        int executionsCount = 10;

        for (int i = 0; i < 10; i++) {
            myTracerTest1.start();
            Thread.sleep(sleepTime);
            myTracerTest1.stop();
        }

        long averageWorkTime = myTracerTest1.getAverageWorkTime();
        assertTrue(averageWorkTime >= sleepTime && averageWorkTime < sleepTime + 10);

        int exepectedElapsedTimeSinceFirstStart = sleepTime * executionsCount;
        long elapsedTimeSinceFirstStart = myTracerTest1.getElapsedTimeSinceFirstStart();
        assertTrue(elapsedTimeSinceFirstStart >= exepectedElapsedTimeSinceFirstStart
                && elapsedTimeSinceFirstStart < exepectedElapsedTimeSinceFirstStart + 50);

        long countExecutions = myTracerTest1.getCountExecutions();
        assertEquals(executionsCount, countExecutions);

        StatisticsTracer.removeTracer(tracerId);

    }

    @Test
    public void testStatisticTracerAndTraceToFile() throws InterruptedException, IOException {

        String tracerId = "testStatisticTracerAndTraceToFile_" + MY_TRACER_TEST1;
        StatisticsTracer myTracerTest1 = StatisticsTracer.createTracer(tracerId);

        testFolder.create();
        File newFile = testFolder.newFile("myTracerTest3");
        String pathFile = newFile.getPath();
        myTracerTest1.traceToFile(pathFile, false);

        int sleepTime = 100;
        int executionsCount = 10;
        String firstRowStr = "";
        firstRowStr = myTracerTest1.toDataRow();
        for (int i = 0; i < executionsCount; i++) {
            long id = myTracerTest1.start();
            Thread.sleep(sleepTime);
            myTracerTest1.stop(id);
            myTracerTest1.print();
        }

        long elapsedTimeSinceFirstStart = myTracerTest1.getElapsedTimeSinceFirstStart();

        long averageWorkTime = myTracerTest1.getAverageWorkTime();
        long totalTime = myTracerTest1.getRoundsTime();
        // Changed by Marvin Wang on Feb.15 for TDI-19166, refer to the method from
        // StatisticsTracer.getAverageWorkTime() that uses "/"
        assertTrue(averageWorkTime * executionsCount <= totalTime);
        // assertTrue(averageWorkTime >= sleepTime && averageWorkTime < sleepTime + 10);

        int exepectedElapsedTimeSinceFirstStart = sleepTime * executionsCount;
        assertTrue("elapsedTimeSinceFirstStart=" + elapsedTimeSinceFirstStart + ", exepectedElapsedTimeSinceFirstStart="
                + exepectedElapsedTimeSinceFirstStart, elapsedTimeSinceFirstStart >= exepectedElapsedTimeSinceFirstStart
                && elapsedTimeSinceFirstStart < exepectedElapsedTimeSinceFirstStart + 50);

        long countExecutions = myTracerTest1.getCountExecutions();
        assertEquals(executionsCount, countExecutions);

        File file = new File(pathFile);
        assertTrue(file.canRead());
        // assertTrue(file.length() > 550); Commentted by Marvin Wang on Feb.15.
        // Changed by Marvin Wang on Feb. 15 for TDI-19166, the "1" stands for wrap character.
        int fistRowByteLength = firstRowStr.getBytes().length;
        assertTrue(file.length() > (fistRowByteLength + 1) * executionsCount);
        file.delete();
        testFolder.delete();

        StatisticsTracer.removeTracer(tracerId);

    }

    @Test
    public void testStatisticTracerWithConcurrency() throws InterruptedException {

        String tracerId = "testStatisticTracerWithConcurrency" + MY_TRACER_TEST2;
        final StatisticsTracer myTracerTest1 = StatisticsTracer.createTracer(tracerId);

        final int sleepTime = 100;
        int executionsCount = 10;
        int concurrentExecutionsCount = 5;
        final CyclicBarrier barrierStart = new CyclicBarrier(concurrentExecutionsCount);
        final CyclicBarrier barrierEnd = new CyclicBarrier(concurrentExecutionsCount + 1);

        ThreadPoolExecutor threadPool = intializePool(concurrentExecutionsCount);

        Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    barrierStart.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 10; i++) {
                    long id = myTracerTest1.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myTracerTest1.stop(id);
                }

                // next reached barrierEnd
                try {
                    barrierEnd.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < executionsCount; i++) {
            threadPool.execute(command);
        }

        // first reached barrierEnd
        try {
            barrierEnd.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long averageWorkTime = myTracerTest1.getAverageWorkTime();
        boolean testAverageWorkTime = averageWorkTime >= sleepTime && averageWorkTime < sleepTime + 100;
        // log.info("averageWorkTime=" + averageWorkTime);
        // System.out.println("averageWorkTime=" + averageWorkTime);
        assertTrue(testAverageWorkTime);

        int expectedElapsedTimeSinceFirstStart = sleepTime * executionsCount;
        long elapsedTimeSinceFirstStart = myTracerTest1.getElapsedTimeSinceFirstStart();
        // log.info("elapsedTimeSinceFirstStart=" + elapsedTimeSinceFirstStart);
        // System.out.println("elapsedTimeSinceFirstStart=" + elapsedTimeSinceFirstStart);
        assertEquals(expectedElapsedTimeSinceFirstStart, elapsedTimeSinceFirstStart, 1500);

        // log.info("executionsCount=" + myTracerTest1.getCountExecutions());
        // System.out.println("executionsCount=" + myTracerTest1.getCountExecutions());
        assertEquals(executionsCount * concurrentExecutionsCount, myTracerTest1.getCountExecutions());

        StatisticsTracer.removeTracer(tracerId);
    }

    /**
     * DOC amaumont Comment method "intializePool".
     * 
     * @return
     */
    private ThreadPoolExecutor intializePool(int poolSize) {

        return (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize, new ThreadFactory() {

            ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

            /*
             * (non-Javadoc)
             * 
             * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
             */
            @Override
            public Thread newThread(Runnable r) {
                Thread newThread = defaultThreadFactory.newThread(r);
                newThread.setName(StatisticsTracerTest.class.getSimpleName() + "_" + newThread.getName());
                return newThread;
            }

        });

    }

}
