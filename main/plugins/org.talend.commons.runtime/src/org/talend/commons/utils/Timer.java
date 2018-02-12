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
package org.talend.commons.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class Timer {

    private static Map<String, Timer> timers = new HashMap<String, Timer>();

    private String name;

    private long startTime;

    private long stopTime;

    private ArrayList<Long> times = new ArrayList<Long>();

    private long timeDelta;

    private Timer(String name) {
        this.name = name;
    }

    public static Timer getTimer(String name) {
        if (timers.keySet().contains(name)) {
            return timers.get(name);
        } else {
            Timer timer = new Timer(name);
            timers.put(name, timer);
            return timer;
        }
    }

    public void start() {
        startTime = new Date().getTime();
    }

    public void start(PrintWriter printWriter) {
        start();
        printWriter.println("Start " + name); //$NON-NLS-1$
    }

    public void stop() {
        stopTime = new Date().getTime();
        timeDelta = stopTime - startTime;
        times.add(timeDelta);
    }

    public void stop(PrintWriter printWriter) {
        stop();
        printWriter.println("Stop  " + name); //$NON-NLS-1$
    }

    public void print() {
        System.out.println(toString());
    }

    public void print(PrintWriter printWriter) {
        printWriter.println(toString());
    }

    public String toString() {
        if (times.size() == 1) {
            return "Print " + name + " : " + timeDelta + " ms"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            long totalTime = computeTotalTime();
            long averageTime = totalTime / times.size();
            return "Print " + name + " : " + timeDelta + " (" + times.size() + " executions avg : " + averageTime + " tot : " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    + totalTime + ")"; //$NON-NLS-1$
        }
    }

    private long computeTotalTime() {
        long totalTime = 0;
        for (Long time : times) {
            totalTime += time;
        }
        return totalTime;
    }

    public void clear() {
        times.clear();
    }

    public long getLastTime() {
        return timeDelta;
    }
}
