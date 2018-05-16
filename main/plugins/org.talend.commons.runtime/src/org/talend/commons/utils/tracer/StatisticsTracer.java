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
package org.talend.commons.utils.tracer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Timer to measure elapsed time of any process or between steps.
 * 
 * Can write in a file by adding the JVM argument
 * 
 * -DstatsTracerPathFile=/myPathFile
 * 
 * and calling the method print()
 * 
 * Example of use:
 * 
 * <pre>
 * StatisticsTracer myTracerTest1 = StatisticsTracer.getTracer(&quot;myTracerTest1&quot;);
 * 
 * myTracerTest1.traceToFile(&quot;/myPathFileToTrace&quot;, false);
 * 
 * int sleepTime = 100;
 * int roundsCount = 10;
 * 
 * for (int i = 0; i &lt; 10; i++) {
 *     myTracerTest1.start();
 *     Thread.sleep(sleepTime);
 *     myTracerTest1.stop();
 * }
 * 
 * long averageWorkTime = myTracerTest1.getAverageWorkTime();
 * long elapsedTimeSinceFirstStart = myTracerTest1.getElapsedTimeSinceFirstStart();
 * long countRounds = myTracerTest1.getCountExecutions();
 * 
 * StatisticsTracer.removeTracer(&quot;myTracerTest1&quot;);
 * </pre>
 */
public final class StatisticsTracer {

    private static final String TRACE_FILE_HEADER = "date;tracerName;counter;averageTimeMs;elapsedTimeMs;totalWithStoppedTimeMs";

    private static Map<String, StatisticsTracer> tracers = new HashMap<String, StatisticsTracer>();

    private static Map<Long, InternalTimer> internalTimers = new ConcurrentHashMap<Long, InternalTimer>();

    private String tracerId;

    private long startTime;

    private long roundsTime;

    private long minTime = Long.MAX_VALUE;

    private int roundOfMinTime = -1;

    private long maxTime = -1;

    private int roundOfMaxTime = -1;

    private static long idCounter;

    private int totalRounds;

    private int roundsInError;

    private boolean testMode = false;

    private int testModeIndex = 0;

    private int[] timesForTest;

    private Integer displayWithModuloRounds = defaultModulo;

    private static final Object[] STATIC_LOCK = new Object[0];

    private Writer tracerFileWriter;

    private static String staticTracerFilePath;

    private static int defaultModulo = 1;

    InternalTimer internalTimer = null;

    private long previousInternalTimerId;

    private String description;

    private Map<String, Object> parameters;

    private int warmupRounds;

    private boolean callGC;

    static {
        String statsTracerProperty = "statsTracerPathFile";
        String systemPropertyTracerPathFile = System.getProperty(statsTracerProperty);
        System.out.println("System property \"" + statsTracerProperty + "\" with value '" + systemPropertyTracerPathFile
                + "' used.");
        if (systemPropertyTracerPathFile != null) {
            staticTracerFilePath = systemPropertyTracerPathFile;
        }
    }

    public static void setDefaultModulo(int defaultModulo) {
        StatisticsTracer.defaultModulo = defaultModulo;
    }

    public class StatisticsTracerResult {

        private String tracerId;

        private String description;

        private Map<String, Object> parameters;

        private int totalRounds;

        private long averageRoundsTime;

        private long roundsTime;

        private long totalElapsedTime;

        private int roundsInError;

        private long minTime;

        private int roundOfMinTime;

        private long maxTime;

        private int roundOfMaxTime;

        private int warmupRounds;

        private boolean callGC;

        public StatisticsTracerResult(String tracerId, String description, Map<String, Object> parameters, int totalRounds,
                int warmupRounds, long averageRoundsTime, long roundsTime, long totalElapsedTime, int roundsInError,
                long minTime, int roundOfMinTime, long maxTime, int roundOfMaxTime, boolean callGC) {
            super();
            this.tracerId = tracerId;
            this.description = description;
            this.parameters = parameters;
            this.totalRounds = totalRounds;
            this.warmupRounds = warmupRounds;
            this.averageRoundsTime = averageRoundsTime;
            this.roundsTime = roundsTime;
            this.totalElapsedTime = totalElapsedTime;
            this.roundsInError = roundsInError;
            this.minTime = minTime;
            this.roundOfMinTime = roundOfMinTime;
            this.maxTime = maxTime;
            this.roundOfMaxTime = roundOfMaxTime;
            this.callGC = callGC;
        }

        /**
         * Getter for tracerId.
         * 
         * @return the tracerId
         */
        public String getTracerId() {
            return this.tracerId;
        }

        /**
         * Getter for description.
         * 
         * @return the description
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * Getter for parameters.
         * 
         * @return the parameters
         */
        public Map<String, Object> getParameters() {
            return this.parameters;
        }

        /**
         * Getter for totalRounds.
         * 
         * @return the totalRounds
         */
        public int getTotalRounds() {
            return this.totalRounds;
        }

        /**
         * Getter for averageTime.
         * 
         * @return the averageTime
         */
        public long getAverageRoundsTime() {
            return this.averageRoundsTime;
        }

        /**
         * Getter for roundsTime.
         * 
         * @return the roundsTime
         */
        public long getRoundsTime() {
            return this.roundsTime;
        }

        /**
         * Getter for totalElapsedTime.
         * 
         * @return the totalElapsedTime
         */
        public long getTotalElapsedTime() {
            return this.totalElapsedTime;
        }

        /**
         * Getter for roundsInError.
         * 
         * @return the roundsInError
         */
        public int getRoundsInError() {
            return this.roundsInError;
        }

        /**
         * Getter for minTime.
         * 
         * @return the minTime
         */
        public long getMinTime() {
            return this.minTime;
        }

        /**
         * Getter for roundOfMinTime.
         * 
         * @return the roundOfMinTime
         */
        public int getRoundOfMinTime() {
            return this.roundOfMinTime;
        }

        /**
         * Getter for maxTime.
         * 
         * @return the maxTime
         */
        public long getMaxTime() {
            return this.maxTime;
        }

        /**
         * Getter for roundOfMaxTime.
         * 
         * @return the roundOfMaxTime
         */
        public int getRoundOfMaxTime() {
            return this.roundOfMaxTime;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String separator = "; ";
            sb.append("Print ");
            boolean writeFieldName = true;
            appendFieldValues(sb, separator, writeFieldName);
            return sb.toString();
        }

        private void appendFieldValues(StringBuilder sb, String separator, boolean writeFieldName) {
            sb.append(separator);
            if (writeFieldName) {
                sb.append("tracerId=");
            }
            sb.append(tracerId);
            if (description != null) {
                sb.append(separator);
                sb.append("description=");
                sb.append(description);
            }
            if (parameters != null) {
                sb.append(separator);
                sb.append("parameters=[");
                sb.append(description);
                Set<String> parametersKeys = parameters.keySet();
                for (String parameterKey : parametersKeys) {
                    Object object = parameters.get(parameterKey);
                    sb.append(", ").append(parameterKey).append("=").append(object);
                }
                sb.append("]").append(description);
            }
            sb.append(separator);
            if (writeFieldName) {
                sb.append("totalRounds=");
            }
            sb.append(totalRounds);

            sb.append(separator);
            if (writeFieldName) {
                sb.append("warmupRounds=");
            }
            sb.append(warmupRounds);

            sb.append(separator);
            if (writeFieldName) {
                sb.append("averageRoundsTime=");
            }
            sb.append(averageRoundsTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("minTime=");
            }
            sb.append(minTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("roundOfMinTime=");
            }
            sb.append(roundOfMinTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("maxTime=");
            }
            sb.append(maxTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("roundOfMaxTime=");
            }
            sb.append(roundOfMaxTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("roundsTime=");
            }
            sb.append(roundsTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("totalElapsedTime=");
            }
            sb.append(totalElapsedTime);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("roundsInError=");
            }
            sb.append(roundsInError);
            sb.append(separator);
            if (writeFieldName) {
                sb.append("callGC=");
            }
            sb.append(callGC);
        }

        /**
         * DOC amaumont Comment method "toDataRow".
         * 
         * @return
         */
        public String toDataRow() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());

            StringBuilder sb = new StringBuilder();
            String separator = ";";
            sb.append(date);
            boolean writeFieldName = false;
            appendFieldValues(sb, separator, writeFieldName);
            return sb.toString();
        }

    }

    /**
     * 
     * DOC amaumont TimeMeasurer class global comment. Detailled comment
     */
    class InternalTimer {

        private long internalStartTime;

        private long id;

        public InternalTimer(long id) {
            this.id = id;
        }

        public void start() {
            internalStartTime = getCurrentTime();
        }

        public long deltaTime() {
            return getCurrentTime() - internalStartTime;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + (int) (id ^ (id >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            InternalTimer other = (InternalTimer) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (id != other.id) {
                return false;
            }
            return true;
        }

        private StatisticsTracer getOuterType() {
            return StatisticsTracer.this;
        }

    }

    private StatisticsTracer(String tracerId, int warmupRounds, boolean callGC) {
        this.tracerId = tracerId;
        this.warmupRounds = warmupRounds;
        this.callGC = callGC;
        if (staticTracerFilePath != null) {
            traceToFile(staticTracerFilePath, false);
        }
    }

    public void traceToFile(String pathFile, boolean append) {
        initFile(pathFile, append);
    }

    public static synchronized StatisticsTracer createTracer(String tracerId) {
        int warmupRounds = 0;
        boolean callGC = false;
        return createTracer(tracerId, warmupRounds, callGC);
    }

    public static synchronized StatisticsTracer createTracer(String tracerId, int warmupRounds, boolean callGC) {
        if (tracers.keySet().contains(tracerId)) {
            throw new IllegalArgumentException("The StatisticsTracer with id=" + tracerId + " already exists.");
        } else {
            StatisticsTracer timer = new StatisticsTracer(tracerId, warmupRounds, callGC);
            tracers.put(tracerId, timer);
            return timer;
        }
    }

    public static synchronized StatisticsTracer getTracer(String tracerId) {
        if (tracers.keySet().contains(tracerId)) {
            return tracers.get(tracerId);
        } else {
            int warmupRounds = 0;
            boolean callGC = false;
            return createTracer(tracerId, warmupRounds, callGC);
        }
    }

    public static synchronized StatisticsTracer getTimerForTestOnly(String tracerId, int[] timesForTest) {
        if (tracers.keySet().contains(tracerId)) {
            return tracers.get(tracerId);
        } else {
            int warmupRounds = 0;
            boolean callGC = false;
            StatisticsTracer timer = new StatisticsTracer(tracerId, warmupRounds, callGC);
            timer.timesForTest = timesForTest;
            tracers.put(tracerId, timer);
            return timer;
        }
    }

    /**
     * 
     * DOC amaumont Comment method "start".
     * 
     * @return id of internalTimer
     */
    public synchronized long start() {
        if (totalRounds == 0) {
            this.startTime = getCurrentTime();
        }
        long internalTimerId = 0;
        InternalTimer internalTimer = null;
        synchronized (STATIC_LOCK) {
            internalTimerId = idCounter++;
        }
        internalTimer = new InternalTimer(internalTimerId);
        internalTimers.put(internalTimerId, internalTimer);
        internalTimer.start();
        this.previousInternalTimerId = internalTimerId;
        return internalTimerId;
    }

    private long getCurrentTime() {
        if (testMode) {
            int time = timesForTest[testModeIndex++];
            return time;
        } else {
            return System.currentTimeMillis();
        }
    }

    public void start(PrintWriter printWriter) {
        start();
        printWriter.println("Start " + tracerId); //$NON-NLS-1$
    }

    /**
     * 
     * Method "stopWithRoundInError".
     * 
     * Use this method when several threads may use the same StatisticTracer instance.
     * 
     * @param internalTimerId
     * @return the timeDelta of the round
     */
    public synchronized long stopWithRoundInError(long internalTimerId) {
        boolean roundInError = true;
        return stop(internalTimerId, roundInError);
    }

    /**
     * 
     * Method "stop".
     * 
     * Use this method when several threads may use the same StatisticTracer instance.
     * 
     * @param internalTimerId
     * @return the timeDelta of the round
     */
    public synchronized long stop(long internalTimerId) {
        boolean roundInError = false;
        return stop(internalTimerId, roundInError);
    }

    /**
     * 
     * Method "stop".
     * 
     * Use this method when several threads may use the same StatisticTracer instance.
     * 
     * @param internalTimerId
     * @param roundInError
     * @return the timeDelta of the round
     */
    private synchronized long stop(long internalTimerId, boolean roundInError) {
        long timeDelta = 0;
        InternalTimer internalTimer = internalTimers.get(internalTimerId);
        if (internalTimer != null) {
            totalRounds++;
            timeDelta = internalTimer.deltaTime();
            if (!roundInError) {
                roundsTime += timeDelta;
                if (timeDelta < minTime) {
                    minTime = timeDelta;
                    roundOfMinTime = totalRounds;
                }
                if (timeDelta > maxTime) {
                    maxTime = timeDelta;
                    roundOfMaxTime = totalRounds;
                }
            } else {
                roundsInError++;
            }
            internalTimers.remove(internalTimerId);
        } else {
            throw new RuntimeException(
                    "Can't stop the internal timer "
                            + internalTimerId
                            + ", it does not exist (anymore?), maybe you already called stop() for this internalTimerId or call stop() twice ?");
        }
        if (displayWithModuloRounds != null) {
            print(displayWithModuloRounds);
        }
        return timeDelta;
    }

    /**
     * 
     * Method "stop".
     * 
     * @param internalTimerId
     * @return the total time since the start
     */
    public long stop() {
        return stop(previousInternalTimerId);
    }

    public void stop(PrintWriter printWriter) {
        stop();
        printWriter.println("Stop  " + tracerId); //$NON-NLS-1$
    }

    public void print() {
        System.out.println(toString());
        writeDataRow();
    }

    public void print(PrintWriter printWriter) {
        printWriter.println(toString());
        writeDataRow();
    }

    private void writeDataRow() {
        if (tracerFileWriter != null) {
            try {
                tracerFileWriter.write("\n" + toDataRow());
                tracerFileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StatisticsTracerResult statisticsTracerResult = createResultInstance();
        return statisticsTracerResult.toString();
    }

    private StatisticsTracerResult createResultInstance() {
        long elapsedTimeSinceFirstStart = getElapsedTimeSinceFirstStart();
        long averageWorkTime = getAverageWorkTime();
        return new StatisticsTracerResult(tracerId, description, parameters, totalRounds, warmupRounds, averageWorkTime,
                roundsTime, elapsedTimeSinceFirstStart, roundsInError, minTime, roundOfMinTime, maxTime, roundOfMaxTime, callGC);
    }

    public String toDataRow() {
        StatisticsTracerResult str = createResultInstance();
        return str.toDataRow();
    }

    public long getElapsedTimeSinceFirstStart() {
        return getCurrentTime() - this.startTime;
    }

    public long getAverageWorkTime() {
        long averageTime = 0;
        if (totalRounds != 0) {
            int validRounds = totalRounds - roundsInError;
            if (validRounds == 0) {
                averageTime = -1;
            } else {
                averageTime = roundsTime / validRounds;
            }
        }
        return averageTime;
    }

    public long getCountExecutions() {
        return totalRounds;
    }

    public long getRoundsTime() {
        return roundsTime;
    }

    public void setModuloPrint(int displayWithModuloRounds) {
        this.displayWithModuloRounds = displayWithModuloRounds;
    }

    public void print(int displayWithModuloRounds) {
        if (displayWithModuloRounds != 0 && (getCountExecutions() % displayWithModuloRounds) == 0) {
            print();
        }
    }

    public static synchronized void removeTracer(String tracerId) {
        StatisticsTracer removed = tracers.remove(tracerId);
        if (removed.tracerFileWriter != null) {
            try {
                removed.tracerFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFile(String filePath, boolean append) {
        try {
            File file = new File(filePath);
            boolean exists = file.exists();
            tracerFileWriter = new FileWriter(file, append);
            if (!exists) {
                tracerFileWriter.write(TRACE_FILE_HEADER);
            }
            tracerFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
