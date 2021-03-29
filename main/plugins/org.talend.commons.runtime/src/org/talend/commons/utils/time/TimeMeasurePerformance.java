// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.time;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class TimeMeasurePerformance extends TimeMeasure{
    static private Logger logger;
    
    private static HashMap<String, TimeStack> timers;
    
    private static long startTime = -1L;

    private static int indent = 0;
    
    public static void begin(String idTimer, String description) {
        startTime = System.nanoTime();
        
        init();
        if (timers.containsKey(idTimer)) {
            log(indent(indent) + "Warning (start): timer " + idTimer + " already exists"); //$NON-NLS-1$  //$NON-NLS-2$
        } else {
            indent++;
            TimeStack times = new TimeStack();
            timers.put(idTimer, times);
            
            String message = "Start '" + idTimer + "' ...";
            if (description != null) {
                message = "Start '" + idTimer + "', " + description + " ...";
            }
            log(indent(indent) + message); //$NON-NLS-1$  //$NON-NLS-2$
        }
    }
    
    private static void init() {
        if (timers == null) {
            timers = new HashMap<String, TimeStack>();
        }
        
        if(logger == null) {
            configureLogger();
        }
    }

    private static void log (String message) {
        logger.info(message);
    }
    
    public static long end(String idTimer) {
        init();
        if (!timers.containsKey(idTimer)) {
            log(indent(indent) + "Warning (end): timer " + idTimer + " doesn't exist"); //$NON-NLS-1$  //$NON-NLS-2$
            return -1;
        } else {
            TimeStack timeStack = timers.get(idTimer);
            timers.remove(idTimer);
            long elapsedTimeSinceLastRequest = timeStack.getLastStepElapsedTime();
            log(indent(indent) + "End '" + idTimer + "', elapsed time since last request: " //$NON-NLS-1$  //$NON-NLS-2$
                    + elapsedTimeSinceLastRequest + " ms "); //$NON-NLS-1$
            
            long totalElapsedTime = timeStack.getTotalElapsedTime();
            
            log(indent(indent) + "End '" + idTimer + "', total elapsed time: " + totalElapsedTime + " ms "); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
            
            indent--;
            return totalElapsedTime;
        }
    }
    
    public static long step(String idTimer, String stepName) {
        init();
        if (!timers.containsKey(idTimer)) {
            log(indent(indent) + "Warning (end): timer " + idTimer + " does'nt exist"); //$NON-NLS-1$  //$NON-NLS-2$
            return -1;
        } else {
            TimeStack timeStack = timers.get(idTimer);
            timeStack.addStep();
            /*
             * trace the timeline of every step,problem is that the code below " Calendar ca = Calendar.getInstance();
             * Date now = ca.getTime();" will cost almost 13ms~15ms
             */
            long time = timeStack.getLastStepElapsedTime();
            String timerStepName = idTimer + "', step name '" + stepName; //$NON-NLS-1$
            
            log(indent(indent)
                    + "-> '" + timerStepName + "', elapsed time since previous step: " + time + " ms"); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
            
            return time;
        }
    }
    
    private static void configureLogger() {
        try {
            PerformanceLogManager logManager = new PerformanceLogManager();
            logger = logManager.getLogger(TimeMeasurePerformance.class.getName());
        } catch (Exception e) {
            throw new RuntimeException("Error while initializing log properties.", e);
        }
    }
    
    public static void afterStartup() {
        double elapsedTimeInSeconds = (double)(System.nanoTime() - startTime)/1000000000;
        PerformanceStatisticUtil.recordStartupEpapsedTime(elapsedTimeInSeconds);
        PerformanceStatisticUtil.measureIO();
    }
}
