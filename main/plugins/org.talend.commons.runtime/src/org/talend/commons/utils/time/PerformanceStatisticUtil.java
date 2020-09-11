// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Random;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.CommonExceptionHandler;

/**
 * DOC sbliu class global comment. Detailled comment
 */
public class PerformanceStatisticUtil {

    private static final DecimalFormat DF = new DecimalFormat("###.##");

    private static final int MEGABYTE = 1024 * 1024;// MB = 1024*1024 byte

    private static final int KILOBYTE = 1024;// kb=1024 byte

    private static final int numOfBlocks = 512;

    private static final int blockSizeKb = 512;

    private static final String dataFile = "testio.data";

    private static String recordingFileName = "performance_record";

    private static File recordingFile = null;

    private static enum BlockSequence {
        SEQUENTIAL,
        RANDOM;
    }

    public static enum StatisticKeys {

        IO_COUNT("I/O.count"), // io count
        IO_W_MB_SEC("I/O.write"), // write speed MB
        IO_R_MB_SEC("I/O.read"), // read speed MB
        IO_W_AVERAGE_MB_SEC("I/O.write.average"), // average speed of write MB
        IO_R_AVERAGE_MB_SEC("I/O.read.average"), // average speed of read

        STARTUP_AVERAGE("startup.average"),
        STARTUP_MAX("startup.max"),
        STARTUP_COUNT("startup.count");

        private String key;

        StatisticKeys(String _key) {
            key = _key;
        }

        public String get() {
            return key;
        }
    }

    public static void recordStartupEpapsedTime(double elapsedTimeInSeconds) {
        File file = getRecordingFile();

        Properties props = read(file, true);
        String propCount = props.getProperty(StatisticKeys.STARTUP_COUNT.get(), "0");
        String propMax = props.getProperty(StatisticKeys.STARTUP_MAX.get(), "0");
        String propAverage = props.getProperty(StatisticKeys.STARTUP_AVERAGE.get(), "0");

        int iPropCount = Integer.parseInt(propCount);
        double iPropMax = Double.parseDouble(propMax);
        double iPropAverage = Double.parseDouble(propAverage);

        iPropMax = iPropMax > elapsedTimeInSeconds ? iPropMax : elapsedTimeInSeconds;
        iPropAverage = (iPropAverage * iPropCount + elapsedTimeInSeconds) / (iPropCount + 1);
        iPropCount++;

        props.setProperty(StatisticKeys.STARTUP_COUNT.get(), "" + iPropCount);
        props.setProperty(StatisticKeys.STARTUP_MAX.get(), "" + iPropMax);
        props.setProperty(StatisticKeys.STARTUP_AVERAGE.get(), "" + iPropAverage);

        store(file, props);
    }

    public static File getRecordingFile() {
        if (recordingFile != null) {
            return recordingFile;
        }

        String configurationLocation = Platform.getConfigurationLocation().getURL().getPath();
        File file = new File(configurationLocation + "/" + recordingFileName);
        return file;
    }

    public static void setRecordingFile(File _recordingFile) {
        recordingFile = _recordingFile;
    }

    public static synchronized Properties read(File recordFile, boolean createIfNotExist) {
        Properties props = new Properties();
        if (recordFile != null && exist(recordFile, createIfNotExist)) {
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(recordFile);
                props.load(inStream);
            } catch (Exception e) {
                CommonExceptionHandler.log(e.getMessage());
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {//
                    }
                }
            }
        }

        return props;
    }

    public static synchronized void store(File recordFile, Properties props) {
        if (props == null) {
            return;
        }

        if (recordFile != null && exist(recordFile, true)) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(recordFile);
                props.store(outputStream, "");
            } catch (IOException e) {
                CommonExceptionHandler.log(e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }
    }

    private static boolean exist(File recordFile, boolean createIfNotExist) {
        boolean exists = recordFile.exists();
        if (!exists && createIfNotExist) {
            try {
                exists = recordFile.createNewFile();
                if (!exists) {
                    throw new FileNotFoundException(recordFile.getName());
                }
            } catch (Exception e) {
                CommonExceptionHandler.log(e.getMessage());
                return false;
            }
        }

        return exists;
    }

    public static void measureIO() {
        File file = getRecordingFile();
        Properties props = read(file, true);

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        File workspace = root.getLocation().makeAbsolute().toFile();
        File locationDir = new File(workspace, "temp"); // here is workspace/temp dir
        File testFile = detectTestDataFile(locationDir);

        if (testFile != null) {
            measureWrite(props, testFile);
            measureRead(props, testFile);

            store(file, props);
        }
    }

    private static void measureWrite(Properties props, File testFile) {
        BlockSequence blockSequence = BlockSequence.RANDOM;

        int blockSize = blockSizeKb * KILOBYTE;

        long startTime = System.nanoTime();
        long totalBytesWrittenInMark = writeIO(numOfBlocks, blockSequence, blockSize, testFile);
        long endTime = System.nanoTime();

        long elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
        double bwMbSec = mbWritten / sec;

        String ioCount = props.getProperty(StatisticKeys.IO_COUNT.get(), "0");
        String ioWAverageMbSec = props.getProperty(StatisticKeys.IO_W_AVERAGE_MB_SEC.get(), "0");
        String ioWMbSec = props.getProperty(StatisticKeys.IO_W_MB_SEC.get(), "0");

        int digital_ioCount = Integer.parseInt(ioCount);
        double digital_ioWAverageMbSec = Double.parseDouble(ioWAverageMbSec);
        double digital_ioWMbSec = Double.parseDouble(ioWMbSec);

        digital_ioWAverageMbSec = (digital_ioWAverageMbSec * digital_ioCount + bwMbSec) / (digital_ioCount + 1);
        digital_ioWMbSec = bwMbSec;

        props.setProperty(StatisticKeys.IO_W_AVERAGE_MB_SEC.get(), "" + DF.format(digital_ioWAverageMbSec));
        props.setProperty(StatisticKeys.IO_W_MB_SEC.get(), "" + DF.format(digital_ioWMbSec));
    }

    private static long writeIO(int numOfBlocks, BlockSequence blockSequence, int blockSize, File testFile) {
        byte[] blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }
        String mode = "rwd";// "rwd"

        long totalBytesWrittenInMark = 0;
        try {
            try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                for (int b = 0; b < numOfBlocks; b++) {
                    if (blockSequence == BlockSequence.RANDOM) {
                        int rLoc = randInt(0, numOfBlocks - 1);
                        rAccFile.seek(rLoc * blockSize);
                    } else {
                        rAccFile.seek(b * blockSize);
                    }
                    rAccFile.write(blockArr, 0, blockSize);
                    totalBytesWrittenInMark += blockSize;
                }
            }
        } catch (IOException e) {
            CommonExceptionHandler.log(e.getMessage());
        }

        return totalBytesWrittenInMark;
    }

    private static File detectTestDataFile(File location) {
        if (!location.exists()) {
            location.mkdirs();
        }

        File testFile = null;
        try {
            testFile = new File(location.getAbsolutePath() + File.separator + dataFile);
            testFile.deleteOnExit();
            testFile.createNewFile();
        } catch (IOException e) {
            CommonExceptionHandler.log(e.getMessage());
        }

        return testFile;
    }

    public static void measureRead(Properties props, File testFile) {
        BlockSequence blockSequence = BlockSequence.RANDOM;

        int blockSize = blockSizeKb * KILOBYTE;

        long startTime = System.nanoTime();

        long totalBytesReadInMark = readIO(numOfBlocks, blockSequence, blockSize, testFile);
        long endTime = System.nanoTime();
        long elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
        double bwMbSec = mbRead / sec;

        String ioCount = props.getProperty(StatisticKeys.IO_COUNT.get(), "0");
        String ioRAverageMbSec = props.getProperty(StatisticKeys.IO_R_AVERAGE_MB_SEC.get(), "0");
        String ioRMbSec = props.getProperty(StatisticKeys.IO_R_MB_SEC.get(), "0");

        int digital_ioCount = Integer.parseInt(ioCount);
        double digital_ioRAverageMbSec = Double.parseDouble(ioRAverageMbSec);
        double digital_ioRMbSec = Double.parseDouble(ioRMbSec);
        digital_ioRAverageMbSec = (digital_ioRAverageMbSec * digital_ioCount + bwMbSec) / (digital_ioCount + 1);
        digital_ioRMbSec = bwMbSec;
        digital_ioCount++;

        props.setProperty(StatisticKeys.IO_R_AVERAGE_MB_SEC.get(), "" + DF.format(digital_ioRAverageMbSec));
        props.setProperty(StatisticKeys.IO_R_MB_SEC.get(), "" + DF.format(digital_ioRMbSec));
        props.setProperty(StatisticKeys.IO_COUNT.get(), "" + digital_ioCount);
    }

    private static long readIO(int numOfBlocks, BlockSequence blockSequence, int blockSize, File testFile) {
        long totalBytesReadInMark = 0;

        byte[] blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }
        try {
            try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                for (int b = 0; b < numOfBlocks; b++) {
                    if (blockSequence == BlockSequence.RANDOM) {
                        int rLoc = randInt(0, numOfBlocks - 1);
                        rAccFile.seek(rLoc * blockSize);
                    } else {
                        rAccFile.seek(b * blockSize);
                    }
                    rAccFile.readFully(blockArr, 0, blockSize);
                    totalBytesReadInMark += blockSize;
                }
            }
        } catch (IOException e) {
            CommonExceptionHandler.log(e.getMessage());
        }
        return totalBytesReadInMark;
    }

    private static int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = new Random().nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
