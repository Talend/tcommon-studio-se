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
package org.talend.utils.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.utils.files.FileDirCleaner;
import org.talend.utils.files.FileDirCleaner.SCAN_STRATEGY;

/**
 * 
 * DOC amaumont class global comment. Detailled comment
 */
public class TestFileDirCleaner {

    private static final int LEVELS_COUNT = 2;

    static String rootPath;

    static SimpleDateFormat sdf;

    static String datePattern;

    private static final int COUNT_FILES_DIRECTORIES = 4;

    static {
        rootPath = "/tmp/" + TestFileDirCleaner.class.getName();
        String datePattern = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(datePattern);
    }

    @Before
    public void setUp() throws Exception {
        removeFiles();
    }

    @After
    public void tearDown() throws Exception {
        // removeFiles();
    }

    /**
     * 
     * Tests on max entries
     * 
     */

    @Test
    public void testLeaveThreeFilesAndAllSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testLeaveThreeFilesAndThreeSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * 3 files only by directory max, 3 directories only by directory max, no check on elapsed time, clean files AND
         * directories, no recursive,
         */
        int expectedDeletedEntries = 2;
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, expectedDeletedEntries, null, null);
    }

    @Test
    public void testLeaveAllFilesAtRootAndThreeSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.DIRECTORIES;

        /*
         * 3 files only by directory max, 3 directories only by directory max, no check on elapsed time, clean files AND
         * directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, 1, null, null);
    }

    @Test
    public void testLeaveThreeFilesAndAllSubdirectoriesRecursively() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testLeaveThreeFilesAndThreeSubdirectoriesRecursively() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files AND directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testBug12804FileCleanerDoesNotCountCorrectlyTheFileOccurrencesToARelatedPattern() {

        int expectedEntriesByDirectoryAndByType = 1;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * 2 files only by directory max, no check on elapsed time, clean files ONLY, recursive search,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, 12, "file_level_2_index_\\d",
                "(dir_level_2_index_1)");

    }

    /**
     * 
     * Tests on max entries and elapsed time
     * 
     */

    @Test
    public void testCleanBeforeMarchAndCleanFebruaryLeaveThreeFilesAndAllSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndThreeSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files AND directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndAllSubdirectoriesRecursively() {

        int expectedEntriesByDirectoryAndByType = 3;
        long cleanAfterThisDuration = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, cleanAfterThisDuration, strategy);

    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndThreeSubdirectoriesRecursively() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * 3 files by directory max, 3 directories by directory max, clean files older than April 2008, clean files AND
         * directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    /**
     * 
     * Tests on elapsed time only
     * 
     */

    @Test
    public void testCleanBeforeMarchAndLeaveAllSubdirectoriesOnRootDirectory() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;

        /*
         * no clean about max entries, clean files older than April 2008, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testCleanBeforeMarchAtRootOnly() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * no clean about max entries, clean files older than April 2008, clean files AND directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    @Test
    public void testCleanBeforeMarchRecursively() {

        int expectedEntriesByDirectoryAndByType = 0;
        long cleanAfterThisDuration = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * no clean about max entries, clean files older than April 2008, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, cleanAfterThisDuration, strategy);

    }

    @Test
    public void testCleanBeforeMarchForAllRecursively() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * no clean about max entries, clean files older than April 2008, clean files AND directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    /**
     * 
     * Tests on max entries, with regexp on files
     * 
     */
    @Test
    public void testLeaveThreeFilesAndAllSubdirectoriesOnRootDirectoryWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 1;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;
        int expectedDeletedEntries = 0;
        String filesRegExpPattern = "(file_level_0_index_2|file_level_2_index_1|file_level_1_index_3)";

        /*
         * 3 files only by directory max, no check on elapsed time, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, expectedDeletedEntries,
                filesRegExpPattern, null);

    }

    /**
     * 
     * Tests on max entries, with regexp on directories to remove
     * 
     */
    @Test
    public void testLeaveThreeFilesAndAllSubdirectoriesOnRootDirectoryWithRegexpOnDirectoriesToRemove() {

        int expectedEntriesByDirectoryAndByType = 1;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;
        int expectedDeletedEntries = 24;
        String directoriesRegExpPattern = "dir_level_2_index_\\d";

        /*
         * 3 files only by directory max, no check on elapsed time, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, expectedDeletedEntries, null,
                directoriesRegExpPattern);

    }

    /**
     * 
     * Tests on max entries, with regexp on directories to remove
     * 
     */
    @Test
    public void testLeaveAllFilesAndCleanSubdirectoriesAtLevel2WithRegexpOnDirectoriesToRemove() {

        int expectedEntriesByDirectoryAndByType = 1;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.DIRECTORIES_RECURSIVELY;

        int expectedDeletedEntries = 8;
        String directoriesRegExpPattern = "dir_level_2_index_[1-3]";

        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy, expectedDeletedEntries, null,
                directoriesRegExpPattern);

    }

    @Test
    public void testLeaveThreeFilesAndThreeSubdirectoriesOnRootDirectoryWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files AND directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    @Test
    public void testLeaveThreeFilesAndAllSubdirectoriesRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testLeaveThreeFilesAndThreeSubdirectoriesRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = 0;
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * 3 files only by directory max, no check on elapsed time, clean files AND directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    /**
     * 
     * Tests on max entries and elapsed time, with regexp on files
     * 
     */

    @Test
    public void testCleanBeforeMarchAndCleanFebruaryLeaveThreeFilesAndAllSubdirectoriesOnRootDirectoryWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndThreeSubdirectoriesOnRootDirectoryWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files AND directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndAllSubdirectoriesRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long cleanAfterThisDuration = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * 3 files only by directory max, clean files older than April 2008, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, cleanAfterThisDuration, strategy);

    }

    @Test
    public void testCleanBeforeMarchAndLeaveThreeFilesAndThreeSubdirectoriesRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 3;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * 3 files by directory max, 3 directories by directory max, clean files older than April 2008, clean files AND
         * directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    /**
     * 
     * Tests on elapsed time only, with regexp on files
     * 
     */

    @Test
    public void testCleanBeforeMarchAndLeaveAllSubdirectoriesOnRootDirectoryWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES;

        /*
         * no clean about max entries, clean files older than April 2008, clean files only, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);

    }

    @Test
    public void testCleanBeforeMarchAtRootOnlyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES;

        /*
         * no clean about max entries, clean files older than April 2008, clean files AND directories, no recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    @Test
    public void testCleanBeforeMarchRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 0;
        long cleanAfterThisDuration = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_RECURSIVELY;

        /*
         * no clean about max entries, clean files older than April 2008, clean files only, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, cleanAfterThisDuration, strategy);

    }

    @Test
    public void testCleanBeforeMarchForAllRecursivelyWithRegexpOnFiles() {

        int expectedEntriesByDirectoryAndByType = 0;
        long maxDurationBeforeClean = getCleanDurationBeforeApril2008();
        SCAN_STRATEGY strategy = SCAN_STRATEGY.FILES_AND_DIRECTORIES_RECURSIVELY;

        /*
         * no clean about max entries, clean files older than April 2008, clean files AND directories, recursive,
         */
        runTest(expectedEntriesByDirectoryAndByType, maxDurationBeforeClean, strategy);
    }

    private void runTest(int expectedEntriesByDirectoryAndByType, long cleanAfterThisDuration, SCAN_STRATEGY strategy) {
        runTest(expectedEntriesByDirectoryAndByType, cleanAfterThisDuration, strategy, null, null, null);
    }

    private void runTest(int expectedEntriesByDirectoryAndByType, long cleanAfterThisDuration, SCAN_STRATEGY strategy,
            Integer expectedDeletedEntries, String filesRegExpPattern, String directoriesRegExpPattern) {
        try {
            generateFiles(LEVELS_COUNT);

            FileDirCleaner fileDirCleaner = new FileDirCleaner(true, strategy, expectedEntriesByDirectoryAndByType,
                    cleanAfterThisDuration);
            int deletedEntries = fileDirCleaner.clean(rootPath, filesRegExpPattern, directoriesRegExpPattern);

            if (expectedDeletedEntries != null && !expectedDeletedEntries.equals(deletedEntries)) {
                fail("expectedDeletedEntries is " + expectedDeletedEntries + " but real deleted entries is " + deletedEntries);
            }

            FileDirCheckerForFSManager checkerOperations = new FileDirCheckerForFSManager();
            checkerOperations.setDirectoriesCount(COUNT_FILES_DIRECTORIES);
            checkerOperations.setFilesCount(COUNT_FILES_DIRECTORIES);
            checkerOperations.setExpectedEntriesByDirectoryAndByType(expectedEntriesByDirectoryAndByType);
            checkerOperations.setCleanAfterThisDuration(cleanAfterThisDuration);
            checkerOperations.setCleanDirectories(strategy.isCleanDirectories());
            checkerOperations.setCleanFiles(strategy.isCleanFiles());
            checkerOperations.setRecursively(strategy.isRecursively());
            checkerOperations.setFilesRegExpPattern(filesRegExpPattern);
            checkerOperations.setDirectoriesRegExpPattern(directoriesRegExpPattern);

            FSManager fsManager = new FSManager(checkerOperations);
            fsManager.process(rootPath, LEVELS_COUNT, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getCleanDurationBeforeApril2008() {
        String cleanBeforeThisDateStr = "2008-03-30";
        Date cleanBeforeThisDate = null;
        try {
            cleanBeforeThisDate = sdf.parse(cleanBeforeThisDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        long cleanAfterThisDuration = System.currentTimeMillis() - cleanBeforeThisDate.getTime();

        return cleanAfterThisDuration / 1000;
    }

    private void generateFiles(int levelsCount) throws IOException {

        System.out.println("Temp files/directories created from rootPath: " + rootPath);

        FileGeneratorForFSManager operationGenerateFiles = new FileGeneratorForFSManager();
        FSManager fsManager = new FSManager(operationGenerateFiles);
        fsManager.process(rootPath, levelsCount, 0);

    }

    private void removeFiles() {

        try {
            org.apache.commons.io.FileUtils.forceDelete(new File(rootPath));
        } catch (IOException e) {
            System.out.println("Error when trying to remove root dir: " + rootPath);
        }
    }

    /**
     * 
     * DOC amaumont FileDirCleaner2Test class global comment. Detailled comment
     */
    public static class FSManager {

        int dirCount = COUNT_FILES_DIRECTORIES;

        int filesCount = COUNT_FILES_DIRECTORIES;

        private OperationForFSManager operation;

        public FSManager(OperationForFSManager operation) {
            super();
            this.operation = operation;
        }

        public void process(String fromPath, int levelsCount, int currentLevel) throws IOException {

            File dir = new File(fromPath);

            int matchingChildrenFilesCount = operation.processCountMatchingChildrenFiles(dir, currentLevel);

            this.operation.processDirectoryBegin(dir, currentLevel, 0, 0);

            for (int indexFile = 1; indexFile <= filesCount; indexFile++) {
                File file = new File(fromPath + "/" + "file_" + "level_" + currentLevel + "_index_" + indexFile);
                operation.processFile(file, currentLevel, indexFile, matchingChildrenFilesCount);
            }
            processRecursively(fromPath, levelsCount, currentLevel + 1);

            this.operation.processDirectoryEnd(dir, currentLevel, 0);

        }

        public void processRecursively(String fromPath, int levelsCount, int currentLevel) throws IOException {

            File parentDir = new File(fromPath);

            int matchingChildrenDirCount = operation.processCountMatchingChildrenDir(parentDir, currentLevel);

            for (int indexDir = 1; indexDir <= dirCount; indexDir++) {
                String pathDir = fromPath + "/" + "dir_" + "level_" + currentLevel + "_index_" + indexDir;
                File dir = new File(pathDir);

                int matchingChildrenFilesCount = operation.processCountMatchingChildrenFiles(dir, currentLevel);

                this.operation.processDirectoryBegin(dir, currentLevel, indexDir, matchingChildrenDirCount);
                System.out.println("Created directory: " + pathDir);
                for (int indexFile = 1; indexFile <= filesCount; indexFile++) {
                    File file = new File(pathDir + "/" + "file_" + "level_" + currentLevel + "_index_" + indexFile);
                    operation.processFile(file, currentLevel, indexFile, matchingChildrenFilesCount);
                }
                if (currentLevel < levelsCount) {
                    processRecursively(dir.getAbsolutePath(), levelsCount, currentLevel + 1);
                }
                this.operation.processDirectoryEnd(dir, currentLevel, indexDir);

            }

        }

    }

    /**
     * 
     * DOC amaumont FileDirCleaner2Test class global comment. Detailled comment
     */
    interface OperationForFSManager {

        public void processFile(File file, int currentLevel, int indexFile, int matchingChildrenFilesCount) throws IOException;

        public int processCountMatchingChildrenDir(File dir, int currentLevel);

        public int processCountMatchingChildrenFiles(File dir, int currentLevel);

        public void processDirectoryBegin(File dir, int currentLevel, int indexDir, int matchingChildrenDirCount);

        public void processDirectoryEnd(File dir, int currentLevel, int indexDir);

    }

    /**
     * 
     * DOC amaumont FileDirCleaner2Test class global comment. Detailled comment
     */
    class FileDirCheckerForFSManager implements OperationForFSManager {

        private int expectedEntriesByDirectoryAndByType;

        private long cleanAfterThisDuration;

        private boolean cleanDirectories;

        private boolean cleanFiles;

        private boolean recursively;

        private int filesCount;

        private int directoriesCount;

        private long currentTime;

        private String filesRegExpPattern;

        private String directoriesRegExpPattern;

        Set<String> dirsRemoved = new HashSet<String>();

        public FileDirCheckerForFSManager() {
            super();
            currentTime = System.currentTimeMillis();

        }

        /*
         * (non-Javadoc)
         * 
         * @see files.TestFileDirCleaner.OperationForFSManager#processCountMatchingChildrenDir(java.io.File)
         */
        @Override
        public int processCountMatchingChildrenDir(File dir, int currentLevel) {
            int countMatchingDir = 0;
            String fromPath = dir.getAbsolutePath();
            for (int indexDir = 1; indexDir <= directoriesCount; indexDir++) {
                String pathDir = fromPath + "/" + "dir_" + "level_" + currentLevel + "_index_" + indexDir;
                File childDir = new File(pathDir);
                boolean exists = childDir.exists();
                String dirName = childDir.getName();
                boolean dirMatches = directoriesRegExpPattern == null || dirName.matches(directoriesRegExpPattern);
                if (exists && dirMatches) {
                    countMatchingDir++;
                }
            }
            return countMatchingDir;
        }

        /*
         * (non-Javadoc)
         * 
         * @see files.TestFileDirCleaner.OperationForFSManager#processCountMatchingChildrenDir(java.io.File)
         */
        @Override
        public int processCountMatchingChildrenFiles(File dir, int currentLevel) {
            int countMatchingFiles = 0;
            String fromPath = dir.getAbsolutePath();
            for (int indexFile = 1; indexFile <= directoriesCount; indexFile++) {
                File file = new File(fromPath + "/" + "file_" + "level_" + currentLevel + "_index_" + indexFile);
                boolean exists = file.exists();
                String fileName = file.getName();
                boolean dirMatches = directoriesRegExpPattern == null || dir.getName().matches(directoriesRegExpPattern);
                boolean fileMatches = filesRegExpPattern == null || fileName.matches(filesRegExpPattern);
                if (exists && dirMatches && fileMatches) {
                    countMatchingFiles++;
                }
            }
            return countMatchingFiles;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processDirectoryBegin(java.io.File,
         * int)
         */
        @Override
        public void processDirectoryBegin(File dir, int currentLevel, int indexDir, int matchingChildrenDirCount) {

            if (currentLevel != 0) {
                boolean exists = dir.exists();
                boolean timeExceeded = cleanAfterThisDuration != 0
                        && currentTime - dir.lastModified() > cleanAfterThisDuration * 1000;
                boolean countExceeded = cleanDirectories && expectedEntriesByDirectoryAndByType != 0
                        && matchingChildrenDirCount > expectedEntriesByDirectoryAndByType;
                boolean countUnexpectedLow = expectedEntriesByDirectoryAndByType != 0
                        && matchingChildrenDirCount < expectedEntriesByDirectoryAndByType;

                String dirName = dir.getName();
                boolean dirMatches = directoriesRegExpPattern == null || dirName.matches(directoriesRegExpPattern);

                if (currentLevel == 1 && !recursively || recursively) {

                    if (exists && dirMatches && (timeExceeded || countExceeded) && cleanDirectories) {
                        fail("Directory '" + dir.getAbsolutePath() + "' SHOULD NOT exist when configuration is" + toString());
                    }

                    if (!exists && !timeExceeded && countUnexpectedLow
                            && !checkCurrentDirIsParentOfRemovedDir(dir.getAbsolutePath())) {
                        fail("Directory '" + dir.getAbsolutePath() + "' SHOULD exist when configuration is" + toString());
                    } else if (!exists) {
                        dirsRemoved.add(dir.getAbsolutePath());
                    }

                }

            }

        }

        @Override
        public String toString() {
            return "FileDirCheckerForFSManager[expectedEntriesByDirectoryAndByType=" + expectedEntriesByDirectoryAndByType
                    + ", maxDurationBeforeCleaning=" + cleanAfterThisDuration + ", cleanDirectories=" + cleanDirectories
                    + ", cleanFiles=" + cleanFiles + ", recursively=" + recursively + "]";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processDirectoryEnd(java.io.File, int)
         */
        @Override
        public void processDirectoryEnd(File dir, int currentLevel, int indexDir) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processFile(java.io.File, int)
         */
        @Override
        public void processFile(File file, int currentLevel, int indexFile, int matchingChildrenFilesCount) throws IOException {

            boolean exists = file.exists();

            boolean timeExceeded = cleanAfterThisDuration != 0
                    && currentTime - file.lastModified() > cleanAfterThisDuration * 1000;
            boolean countExceeded = expectedEntriesByDirectoryAndByType != 0
                    && matchingChildrenFilesCount > expectedEntriesByDirectoryAndByType;
            boolean countUnexpectedLow = expectedEntriesByDirectoryAndByType != 0
                    && matchingChildrenFilesCount < expectedEntriesByDirectoryAndByType;

            String fileName = file.getName();

            String parentDir = file.getParent();

            boolean parentDirMatches = directoriesRegExpPattern == null || parentDir.matches(directoriesRegExpPattern);
            boolean fileMatches = filesRegExpPattern == null || fileName.matches(filesRegExpPattern);

            if (currentLevel == 1 && !recursively || recursively) {

                if (exists && parentDirMatches && (recursively || !recursively && currentLevel == 0) && fileMatches
                        && (timeExceeded || countExceeded) && cleanFiles) {
                    fail("File '" + file.getAbsolutePath() + "' SHOULD NOT exist when configuration is " + toString());
                }

                String parentPath = file.getParent();

                if (!exists && !timeExceeded && countUnexpectedLow && !checkCurrentDirIsParentOfRemovedDir(parentPath)) {
                    fail("File '" + file.getAbsolutePath() + "' SHOULD exist when configuration is " + toString());
                }
            }

        }

        private boolean checkCurrentDirIsParentOfRemovedDir(String parentPath) {
            String[] array = dirsRemoved.toArray(new String[0]);
            for (String element : array) {
                if (parentPath.startsWith(element)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Getter for maxEntriesByDirectory.
         * 
         * @return the maxEntriesByDirectory
         */
        public int getExpectedEntriesByDirectoryAndByType() {
            return expectedEntriesByDirectoryAndByType;
        }

        /**
         * Sets the maxEntriesByDirectory.
         * 
         * @param expectedEntriesByDirectoryAndByType the maxEntriesByDirectory to set
         */
        public void setExpectedEntriesByDirectoryAndByType(int expectedEntriesByDirectoryAndByType) {
            this.expectedEntriesByDirectoryAndByType = expectedEntriesByDirectoryAndByType;
        }

        /**
         * Getter for maxDurationBeforeCleaning.
         * 
         * @return the maxDurationBeforeCleaning
         */
        public long getCleanAfterThisDuration() {
            return cleanAfterThisDuration;
        }

        /**
         * Sets the maxDurationBeforeCleaning.
         * 
         * @param maxDurationBeforeCleaning the maxDurationBeforeCleaning to set
         */
        public void setCleanAfterThisDuration(long maxDurationBeforeCleaning) {
            this.cleanAfterThisDuration = maxDurationBeforeCleaning;
        }

        /**
         * Getter for cleanFilesOnly.
         * 
         * @return the cleanFilesOnly
         */
        public boolean isCleanFiles() {
            return cleanFiles;
        }

        /**
         * Sets the cleanFilesOnly.
         * 
         * @param cleanFilesOnly the cleanFilesOnly to set
         */
        public void setCleanFiles(boolean cleanFilesOnly) {
            this.cleanFiles = cleanFilesOnly;
        }

        /**
         * Getter for cleanDirectories.
         * 
         * @return the cleanDirectories
         */
        public boolean isCleanDirectories() {
            return cleanDirectories;
        }

        /**
         * Sets the cleanDirectories.
         * 
         * @param cleanDirectories the cleanDirectories to set
         */
        public void setCleanDirectories(boolean cleanDirectories) {
            this.cleanDirectories = cleanDirectories;
        }

        /**
         * Getter for recursively.
         * 
         * @return the recursively
         */
        public boolean isRecursively() {
            return recursively;
        }

        /**
         * Sets the recursively.
         * 
         * @param recursively the recursively to set
         */
        public void setRecursively(boolean recursively) {
            this.recursively = recursively;
        }

        /**
         * Getter for filesCount.
         * 
         * @return the filesCount
         */
        public int getFilesCount() {
            return filesCount;
        }

        /**
         * Sets the filesCount.
         * 
         * @param filesCount the filesCount to set
         */
        public void setFilesCount(int filesCount) {
            this.filesCount = filesCount;
        }

        /**
         * Getter for directoriesCount.
         * 
         * @return the directoriesCount
         */
        public int getDirectoriesCount() {
            return directoriesCount;
        }

        /**
         * Sets the directoriesCount.
         * 
         * @param directoriesCount the directoriesCount to set
         */
        public void setDirectoriesCount(int directoriesCount) {
            this.directoriesCount = directoriesCount;
        }

        /**
         * Getter for filesRegExpPattern.
         * 
         * @return the filesRegExpPattern
         */
        public String getFilesRegExpPattern() {
            return filesRegExpPattern;
        }

        /**
         * Sets the filesRegExpPattern.
         * 
         * @param filesRegExpPattern the filesRegExpPattern to set
         */
        public void setFilesRegExpPattern(String filesRegExpPattern) {
            this.filesRegExpPattern = filesRegExpPattern;
        }

        /**
         * Getter for directoriesRegExpPattern.
         * 
         * @return the directoriesRegExpPattern
         */
        public String getDirectoriesRegExpPattern() {
            return directoriesRegExpPattern;
        }

        /**
         * Sets the directoriesRegExpPattern.
         * 
         * @param directoriesRegExpPattern the directoriesRegExpPattern to set
         */
        public void setDirectoriesRegExpPattern(String directoriesRegExpPattern) {
            this.directoriesRegExpPattern = directoriesRegExpPattern;
        }

    }

    /**
     * 
     * DOC amaumont FileDirCleaner2Test class global comment. Detailled comment
     */
    class FileGeneratorForFSManager implements OperationForFSManager {

        /*
         * (non-Javadoc)
         * 
         * @see files.TestFileDirCleaner.OperationForFSManager#processCountMatchingChildrenDir(java.io.File, int)
         */
        @Override
        public int processCountMatchingChildrenDir(File dir, int currentLevel) {
            /* generation does not require a valid value */
            return 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see files.TestFileDirCleaner.OperationForFSManager#processCountMatchingChildrenFiles(java.io.File, int)
         */
        @Override
        public int processCountMatchingChildrenFiles(File dir, int currentLevel) {
            /* generation does not require a valid value */
            return 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processDirectoryBegin(java.io.File,
         * int)
         */
        @Override
        public void processDirectoryBegin(File dir, int currentLevel, int indexDir, int matchingChildrenDirCount) {
            dir.mkdirs();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processDirectoryEnd(java.io.File, int)
         */
        @Override
        public void processDirectoryEnd(File dir, int currentLevel, int indexDir) {
            Date parsedDateDir = null;
            try {
                parsedDateDir = sdf.parse("2008-" + indexDir + "-01");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            boolean setLastModified = dir.setLastModified(parsedDateDir.getTime());
            System.out.println("setLastModified=" + parsedDateDir + ", ok? " + setLastModified);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.utils.files.FileDirCleaner2Test.OperationForFSManager#processFile(java.io.File, int)
         */
        @Override
        public void processFile(File file, int currentLevel, int indexFile, int matchingChildrenFilesCount) throws IOException {
            file.createNewFile();
            Date parsedDateFile = null;
            try {
                parsedDateFile = sdf.parse("2008-" + indexFile + "-01");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            file.setLastModified(parsedDateFile.getTime());
            System.out.println("Created file: " + file.getAbsolutePath());
        }

    }

}
