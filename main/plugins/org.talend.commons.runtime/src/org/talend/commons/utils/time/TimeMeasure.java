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
package org.talend.commons.utils.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.CommonsPlugin;
import org.talend.commons.runtime.debug.TalendDebugHandler;

/**
 * Timer to measure elapsed time of any process or between steps.
 * 
 * $Id$
 * 
 */
public class TimeMeasure {
    
    // PTODO create junit test class
    private static HashMap<String, TimeStack> timers;

    private static int indent = 0;

    /**
     * measureActive is true by default. A true value means that all methods calls are processed else no one.
     */
    public static boolean measureActive = false;

    /**
     * display is true by default. A true value means that all informations are displayed.
     */
    public static boolean display = false;

    public static boolean displaySteps = false;

    public static boolean isLogToFile = false;

    public static boolean printMemoryUsed = false;

    public static ITimeMeasureLogger logger;

    public static String logFilePath;

    // key represent the idTimer,value map represent the log rows.
    private static Map<String, List<Map<Integer, Object>>> logValue = new HashMap<String, List<Map<Integer, Object>>>();

    /**
     * 
     * DOC amaumont Comment method "start".
     * 
     * @param idTimer
     */
    public static void begin(String idTimer) {
        if (!measureActive) {
            return;
        }
        init();
        if (timers.containsKey(idTimer)) {
            if (display) {
                log(indent(indent) + "Warning (start): timer " + idTimer + " already exists"); //$NON-NLS-1$  //$NON-NLS-2$
            }
        } else {
            indent++;
            TimeStack times = new TimeStack();
            timers.put(idTimer, times);
            if (display) {
                log(indent(indent) + "Start '" + idTimer + "' ..."); //$NON-NLS-1$  //$NON-NLS-2$
            }
        }
    }
    
    private static void log (String message) {
        TalendDebugHandler.debug(message);
    }

    /**
     * 
     * DOC amaumont Comment method "end".
     * 
     * @param idTimer
     * @return total elapsed time since start in ms
     */
    public static long end(String idTimer) {
        if (!measureActive) {
            return 0;
        }
        init();
        if (!timers.containsKey(idTimer)) {
            if (display) {
                log(indent(indent) + "Warning (end): timer " + idTimer + " doesn't exist"); //$NON-NLS-1$  //$NON-NLS-2$
            }
            return -1;
        } else {
            TimeStack timeStack = timers.get(idTimer);
            timers.remove(idTimer);
            long elapsedTimeSinceLastRequest = timeStack.getLastStepElapsedTime();
            if (display && displaySteps) {
                log(indent(indent) + "End '" + idTimer + "', elapsed time since last request: " //$NON-NLS-1$  //$NON-NLS-2$
                        + elapsedTimeSinceLastRequest + " ms "); //$NON-NLS-1$
            }
            long totalElapsedTime = timeStack.getTotalElapsedTime();
            if (display) {
                if (printMemoryUsed) {
                    // GC must be forced when check memory, or we can't mesure the difference
                    Runtime.getRuntime().gc();
                    long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                    log(indent(indent)
                            + "End '" + idTimer + "', total elapsed time: " + totalElapsedTime + " ms, " //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                            + " current memory [" + usedMemory + "] bytes"); //$NON-NLS-1$  //$NON-NLS-2$
                } else {
                    log(indent(indent) + "End '" + idTimer + "', total elapsed time: " + totalElapsedTime + " ms "); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                }
            }
            indent--;
            if (isLogToFile && logger != null) {
                logger.logToFile(logValue, logFilePath);
                logValue.clear();
            }
            return totalElapsedTime;
        }
    }

    /**
     * 
     * DOC amaumont Comment method "timeSinceStart".
     * 
     * @param idTimer
     * @return total elapsed time since start in ms
     */
    public static long timeSinceBegin(String idTimer) {
        if (!measureActive) {
            return 0;
        }
        init();
        if (!timers.containsKey(idTimer)) {
            if (display) {
                log(indent(indent) + "Warning (end): timer " + idTimer + " does'nt exist"); //$NON-NLS-1$  //$NON-NLS-2$
            }
            return -1;
        } else {
            long time = timers.get(idTimer).getTotalElapsedTime();
            if (display) {
                if (printMemoryUsed) {
                    // GC must be forced when check memory, or we can't mesure the difference
                    Runtime.getRuntime().gc();
                    long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                    log(indent(indent) + "-> '" + idTimer + "', elapsed time since start: " + time + " ms," //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                            + " current memory [" + usedMemory + "] bytes"); //$NON-NLS-1$  //$NON-NLS-2$
                } else {
                    log(indent(indent) + "-> '" + idTimer + "', elapsed time since start: " + time + " ms "); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                }
            }
            return time;
        }
    }

    /**
     * 
     * DOC amaumont Comment method "timeStep".
     * 
     * @param idTimer
     * @return elapsed time since previous step in ms
     */
    public static long step(String idTimer, String stepName) {
        if (!measureActive) {
            return 0;
        }
        init();
        if (!timers.containsKey(idTimer)) {
            if (display) {
                log(indent(indent) + "Warning (end): timer " + idTimer + " does'nt exist"); //$NON-NLS-1$  //$NON-NLS-2$
            }
            return -1;
        } else {
            TimeStack timeStack = timers.get(idTimer);
            timeStack.addStep();
            /*
             * trace the timeline of every step,problem is that the code below " Calendar ca = Calendar.getInstance();
             * Date now = ca.getTime();" will cost almost 13ms~15ms
             */
            Calendar ca = Calendar.getInstance();
            Date now = ca.getTime();
            long time = timeStack.getLastStepElapsedTime();
            if (display && displaySteps) {
                long usedMemory = 0;
                if (printMemoryUsed) {
                    // GC must be forced when check memory, or we can't mesure the difference
                    Runtime.getRuntime().gc();
                    usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                }
                String timerStepName = idTimer + "', step name '" + stepName; //$NON-NLS-1$
                if (printMemoryUsed) {
                    log(indent(indent)
                            + "-> '" + timerStepName + "', elapsed time since previous step: " + time + " ms," + //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                            " current memory [" + usedMemory + "] bytes"); //$NON-NLS-1$  //$NON-NLS-2$
                } else {
                    log(indent(indent)
                            + "-> '" + timerStepName + "', elapsed time since previous step: " + time + " ms"); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                }
                if (isLogToFile) {
                    boolean foundTimerKey = false;
                    for (String keyTimer : logValue.keySet()) {
                        if (keyTimer.equals(idTimer)) {
                            /* rows */
                            List<Map<Integer, Object>> values = logValue.get(keyTimer);
                            if (values != null) {
                                Map<Integer, Object> newRowValue = new HashMap<Integer, Object>();
                                // step
                                newRowValue.put(ELogFileColumnConstant.STEP.locationY, timerStepName);
                                // timeused
                                newRowValue.put(ELogFileColumnConstant.TIME_USED.locationY, time);
                                // memory used
                                if (printMemoryUsed) {
                                    newRowValue.put(ELogFileColumnConstant.MEMO_USED.locationY, usedMemory);
                                }
                                // current time
                                newRowValue.put(ELogFileColumnConstant.TIMETRACE.locationY, now);
                                values.add(newRowValue);
                            }
                            foundTimerKey = true;
                            break;
                        }
                    }
                    if (!foundTimerKey) {
                        List<Map<Integer, Object>> newvalues = new ArrayList<Map<Integer, Object>>();
                        Map<Integer, Object> newRowValue = new HashMap<Integer, Object>();
                        // step
                        newRowValue.put(ELogFileColumnConstant.STEP.locationY, timerStepName);
                        // timeused
                        newRowValue.put(ELogFileColumnConstant.TIME_USED.locationY, time);
                        // memory used
                        if (printMemoryUsed) {
                            newRowValue.put(ELogFileColumnConstant.MEMO_USED.locationY, usedMemory);
                        }
                        // current time
                        newRowValue.put(ELogFileColumnConstant.TIMETRACE.locationY, now);
                        newvalues.add(newRowValue);
                        logValue.put(idTimer, newvalues);
                    }
                }
            }
            return time;
        }
    }

    public static void pause(String idTimer) {
        if (!measureActive) {
            return;
        }
        init();
        if (!timers.containsKey(idTimer)) {
            if (display) {
                log(indent(indent) + "Warning (end): timer " + idTimer + " does'nt exist"); //$NON-NLS-1$  //$NON-NLS-2$
            }
            return;
        } else {
            TimeStack time = timers.get(idTimer);
            // long time = times.getElapsedTimeSinceLastRequest();
            time.pause();
            if (display) {
                // do nothing... yet
            }
        }
    }

    public static void resume(String idTimer) {
        if (!measureActive) {
            return;
        }
        init();
        if (!timers.containsKey(idTimer)) {
            begin(idTimer);
            // if (display) {
            //                log(indent(indent) + "Warning (end): timer " + idTimer + " does'nt exist"); //$NON-NLS-1$  //$NON-NLS-2$
            // }
            return;
        } else {
            TimeStack times = timers.get(idTimer);
            // long time = times.getLastStepElapsedTime();
            times.resume();
            if (display) {
                // do nothing... yet
            }
        }
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private static void init() {
        if (timers == null) {
            timers = new HashMap<String, TimeStack>();
        }
    }

    public static String indent(final int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            stringBuilder.append("  "); //$NON-NLS-1$
        }
        return stringBuilder.toString();
    }

    /* this enum define the attributes of columns in a log file */
    public enum ELogFileColumnConstant {

        TITLE(0, 0, "Welcome to CommandLine performance test"), //$NON-NLS-1$
        STEP(0, 1, "Step"), //$NON-NLS-1$
        TIME_USED(1, 1, "TimeUsed(ms)"), //$NON-NLS-1$
        MEMO_USED(2, 1, "memoryUsed(bytes)"), //$NON-NLS-1$
        TIMETRACE(3, 1, "timeLine"); //$NON-NLS-1$

        public int locationY;

        public int locationX;

        public String label;

        private ELogFileColumnConstant(int locationY, int locationX, String label) {
            this.locationY = locationY;
            this.locationX = locationX;
            this.label = label;
        }
    }

    /* right now main() can't log to file,if want to test log file,run application CommandLineTestApplication */
    public static void main(String[] args) {
        try {
            TimeMeasure.begin("a"); //$NON-NLS-1$
            // TimeMeasure.end("b");
            Thread.sleep(500);
            TimeMeasure.step("a", "1"); //$NON-NLS-1$ //$NON-NLS-2$
            Thread.sleep(800);
            TimeMeasure.pause("a"); //$NON-NLS-1$
            Thread.sleep(600);
            TimeMeasure.step("a", "2"); //$NON-NLS-1$ //$NON-NLS-2$
            TimeMeasure.resume("a"); //$NON-NLS-1$
            Thread.sleep(2000);
            TimeMeasure.end("a"); //$NON-NLS-1$
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
