// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC amaumont class global comment. Detailled comment
 */
public class TimeoutTaskTest {

    /**
     *
     */
    private static final String TEST_TASK = "TestTask";

    /**
     *
     */
    private static final String OK = "OK"; //$NON-NLS-1$

    @Test
    public void testResultOK() throws TimeoutException, InterruptedException, ExecutionException {
        TimeoutTask<String> timoutTask = new TimeoutTask<String>(TEST_TASK);
        Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws Exception {
                Thread.sleep(10);
                return OK;
            }
        };
        FutureTask<String> futureTask = timoutTask.run(callable, 20, false);
        Object result = futureTask.get();
        Assert.assertEquals(OK, result);
    }

    @Test(expected = TimeoutException.class)
    public void testTimeoutReached() throws TimeoutException, InterruptedException, ExecutionException {
        TimeoutTask<String> timoutTask = new TimeoutTask<String>(TEST_TASK);
        Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws Exception {
                Thread.sleep(20000);
                return OK;
            }
        };
        timoutTask.run(callable, 10, false);

    }

    @Test
    public void testTimeoutReached2() throws TimeoutException, InterruptedException, ExecutionException {
        TimeoutTask<String> timoutTask = new TimeoutTask<String>(TEST_TASK);
        Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws Exception {
                while (!Thread.interrupted()) {
                }
                return OK;
            }
        };
        try {
            FutureTask<String> futureTask = timoutTask.run(callable, 10, true);
            System.out.println(futureTask.get());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
