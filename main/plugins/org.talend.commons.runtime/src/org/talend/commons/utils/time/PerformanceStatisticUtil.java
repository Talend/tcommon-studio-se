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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.CommonExceptionHandler;

/**
 * DOC sbliu class global comment. Detailled comment
 */
public class PerformanceStatisticUtil {

    private static final int MEGABYTE = 1024 * 1024;// MB = 1024*1024 byte

    private static final int KILOBYTE = 1024;// kb=1024 byte

    private static final int numOfBlocks = 256;

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

        Properties props = PropertiesFileUtil.read(file, true);
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

        PropertiesFileUtil.store(file, props);
    }

    public static File getRecordingFile() {
        if (recordingFile != null) {
            return recordingFile;
        }

        String configurationLocation = Platform.getConfigurationLocation().getURL().getPath();
        File file = new File(configurationLocation + "/data_collector/" + recordingFileName);
        File oldFile = new File(configurationLocation + "/" + recordingFileName);
        if(oldFile.exists()) {
            if(!file.exists()) {
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Files.move(Paths.get(oldFile.toURI()), Paths.get(file.toURI()), StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    CommonExceptionHandler.log(e.getMessage());
                }
            }
            
            try {
                Files.deleteIfExists(Paths.get(oldFile.toURI()));
            } catch (IOException e) {
                CommonExceptionHandler.log(e.getMessage());
            }
        }
        
        return file;
    }

    public static void setRecordingFile(File _recordingFile) {
        recordingFile = _recordingFile;
    }
    
    public static void reset() {
        File _recordingFile = getRecordingFile();
        try {
            Files.deleteIfExists(Paths.get(_recordingFile.toURI()));
        } catch (IOException e) {
            CommonExceptionHandler.log(e.getMessage());
        }
    }

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static boolean measureIOFinished = true;
    
    public static void waitUntilFinish() throws InterruptedException {
        lock.lock();

        try {
            if(!measureIOFinished) {
                condition.await(20, TimeUnit.SECONDS);
            }
        } finally {
            lock.unlock();
        }
    }
    
    public static void measureIO() {
        new Thread()  {
            public void run() {
                measureIOFinished = false;
                try {
                    _measureIO();
                } finally {
                    measureIOFinished = true;
                }
            }
        }.start();
    }

    private static void _measureIO() {
        File file = getRecordingFile();
        Properties props = PropertiesFileUtil.read(file, true);

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        File workspace = root.getLocation().makeAbsolute().toFile();
        File locationDir = new File(workspace, "temp"); // here is workspace/temp dir
        File testFile = detectTestDataFile(locationDir);

        if (testFile != null) {
            measureWrite(props, testFile);
            measureRead(props, testFile);

            PropertiesFileUtil.store(file, props);
        }
    }
    
    private static void measureWrite(Properties props, File testFile) {
        int blockSize = blockSizeKb * KILOBYTE;

        long startTime = System.nanoTime();
        long totalBytesWrittenInMark = writeIO(numOfBlocks, BlockSequence.RANDOM, blockSize, testFile);
        totalBytesWrittenInMark = totalBytesWrittenInMark + writeIO(numOfBlocks, BlockSequence.SEQUENTIAL, blockSize, testFile);
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

        props.setProperty(StatisticKeys.IO_W_AVERAGE_MB_SEC.get(), format(digital_ioWAverageMbSec));
        props.setProperty(StatisticKeys.IO_W_MB_SEC.get(), format(digital_ioWMbSec));
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
        int blockSize = blockSizeKb * KILOBYTE;

        long startTime = System.nanoTime();
        long totalBytesReadInMark = readIO(numOfBlocks, BlockSequence.RANDOM, blockSize, testFile);
        totalBytesReadInMark = totalBytesReadInMark + readIO(numOfBlocks, BlockSequence.SEQUENTIAL, blockSize, testFile);
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

        props.setProperty(StatisticKeys.IO_R_AVERAGE_MB_SEC.get(), format(digital_ioRAverageMbSec));
        props.setProperty(StatisticKeys.IO_R_MB_SEC.get(), format(digital_ioRMbSec));
        props.setProperty(StatisticKeys.IO_COUNT.get(), "" + digital_ioCount);
    }

    public static String format(double dvalue) {
        return BigDecimal.valueOf(dvalue).setScale(2, RoundingMode.HALF_UP).toString();
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
