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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 
 * DOC amaumont class global comment. Detailled comment
 */
public class StatisticsStackTracer {

    private Map<KeyStatisticTracer, StatisticsTracer> mapKeyToTimer = new HashMap<KeyStatisticTracer, StatisticsTracer>();

    static Random random = new Random(System.currentTimeMillis());

    private int counter;

    private boolean stoppingDisplay;

    /**
     * 
     * DOC amaumont StatisticsTracer class global comment. Detailled comment
     */
    static class KeyStatisticTracer {

        String stackTrace;

        String timerName;

        public KeyStatisticTracer(String stackTrace, String timerName) {
            super();
            this.stackTrace = stackTrace;
            this.timerName = timerName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((stackTrace == null) ? 0 : stackTrace.hashCode());
            result = prime * result + ((timerName == null) ? 0 : timerName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeyStatisticTracer other = (KeyStatisticTracer) obj;
            if (stackTrace == null) {
                if (other.stackTrace != null)
                    return false;
            } else if (!stackTrace.equals(other.stackTrace))
                return false;
            if (timerName == null) {
                if (other.timerName != null)
                    return false;
            } else if (!timerName.equals(other.timerName))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "KeyStatisticTracer [stackTrace=" + stackTrace + ", timerName=" + timerName + "]";
        }

    }

    public synchronized StatisticsTracer getTracer(String timerName) {
        String stackTrace = getStackTrace(-1);
        KeyStatisticTracer keyStatisticTracer = new KeyStatisticTracer(stackTrace, timerName);
        StatisticsTracer timeMeasurer = mapKeyToTimer.get(keyStatisticTracer);
        if (timeMeasurer == null) {
            counter++;
            timeMeasurer = StatisticsTracer.getTracer(buildInternalTimerName(timerName, stackTrace));
            mapKeyToTimer.put(keyStatisticTracer, timeMeasurer);
        }
        return timeMeasurer;
    }

    private String buildInternalTimerName(String timerName, String stackTrace) {
        return "Timer '" + timerName + "' (" + stackTrace.hashCode() + ") => \n" + stackTrace;
    }

    public String getStackTrace() {
        return getStackTrace(-1);
    }

    public String getStackTrace(int keepNlastLevels) {

        StackTraceElement[] traces = new Exception().getStackTrace();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (keepNlastLevels != -1 && traces.length - keepNlastLevels > 0) {
            i = traces.length - keepNlastLevels;
        }

        for (; i < traces.length; i++) {
            StackTraceElement stackTraceElement = traces[i];
            sb.append(stackTraceElement.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void print(int minimalExecToDisplay) {
        Set<KeyStatisticTracer> keySet = mapKeyToTimer.keySet();
        System.out.println("##########################################################");
        List<StatisticsTracer> list = new ArrayList<StatisticsTracer>();
        for (KeyStatisticTracer keyStatisticTracer : keySet) {
            String keyString = keyStatisticTracer.toString();
            StatisticsTracer timeMeasurer = mapKeyToTimer.get(keyStatisticTracer);
            if (timeMeasurer.getCountExecutions() > minimalExecToDisplay) {
                list.add(timeMeasurer);
            }
        }

        Collections.sort(list, new Comparator<StatisticsTracer>() {

            public int compare(StatisticsTracer o1, StatisticsTracer o2) {
                return (int) (o1.getCountExecutions() - o2.getCountExecutions());
            }

        });

        int listListSize = list.size();
        for (int i = 0; i < listListSize; i++) {
            StatisticsTracer timeMeasurer = list.get(i);
            String timeString = timeMeasurer.toString();
            System.out.println("---------------------------------------------------------" + "\n" + timeString);
        }
        System.out.println("##########################################################");
    }

    public void enablePrintByTime(final int displayTimeSeconds, final int minimalExecsToDisplay) {

        Thread displayThread = new Thread("StatisticsStackTracer_display") {

            @Override
            public void run() {

                while (!stoppingDisplay) {

                    try {
                        Thread.sleep(displayTimeSeconds * 1000);
                    } catch (InterruptedException e) {
                        stoppingDisplay = true;
                    }
                    print(minimalExecsToDisplay);
                }

                stoppingDisplay = false;
            }
        };
        displayThread.start();

    }

    public void disablePrint() {
        stoppingDisplay = true;
    }

}
