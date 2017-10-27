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
package org.talend.commons.utils.performance;

/**
 * 
 * Evaluate performance of cpu with a reference time of 300 ms for good performance. <br/>
 * 
 * $Id$
 * 
 */
public class PerformanceEvaluator {

    /**
     * if indice value less than this value, system has good performance.
     */
    public static final int GOOD_PERFORMANCE_INDICE = 350;

    public static final int TIME_TO_LAST = 100000000;

    public static final int INIT_TIME = 1000;

    private IPerformanceEvaluatorListener listener;

    /**
     * Warning : Now, only the last registered listener is active at the same time.
     * 
     */
    public void addListener(IPerformanceEvaluatorListener newListener) {
        this.listener = newListener;
    }

    /**
     * 
     * 
     * 
     */
    public void evaluate() {

        Thread t = new Thread() {

            public void run() {
                long timeStart = System.currentTimeMillis();
                for (int i = 0; i < TIME_TO_LAST; i++) {
                    // do nothing
                }
                long timeSpent = System.currentTimeMillis() - timeStart;
                PerformanceEvaluatorEvent event = new PerformanceEvaluatorEvent((int) timeSpent);
                // //System.out.println("timeSpent=" + (int) timeSpent);
                if (listener != null) {
                    listener.handleEvent(event);
                }
            }
        };
        t.start();

    }

    public static void main(String[] args) {
        final PerformanceEvaluator performanceEvaluator = new PerformanceEvaluator();
        (new Object() {

            void init() {
                performanceEvaluator.addListener(new IPerformanceEvaluatorListener() {

                    public void handleEvent(PerformanceEvaluatorEvent event) {
                        // System.out.println(event.getIndicePerformance());
                    }

                });
            }
        }).init();
        (new Thread() {

            @Override
            public void run() {
                try {
                    // to start evaluation after application loaded
                    Thread.sleep(INIT_TIME);
                } catch (InterruptedException e) {
                    // nothing
                }
                performanceEvaluator.evaluate();
            }
        }).start();

    }

}
