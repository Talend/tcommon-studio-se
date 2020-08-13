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
package org.talend.utils.files;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileDirCleaner.
 *
 * Either it cleans according number of items, or according date, or both, by checking only items which match the regexp
 * and the chosen strategy.
 */
public class FileDirCleaner {

    private static Logger log = LoggerFactory.getLogger(FileDirCleaner.class);

    private long currentTime;

    private CleanResult cleanResult;

    private String filesRegExpPattern;

    private String directoriesRegExpPattern;

    private int maxEntriesByDirectoryAndByType;

    private long maxDurationBeforeCleaning;

    private boolean cleanDirectories;

    private boolean cleanFiles;

    private boolean recursively;

    private boolean doAction = false;

    private FileDirCleanerFilter filter;

    /**
     *
     * SCAN_STRATEGY. Match according the chosen strategy: <br>
     * - FILES: scan and match only the direct children files from the given <code>pathDir</code> not recursively <br>
     * - DIRECTORIES: scan and match only the directories from the given <code>pathDir</code> not recursively <br>
     * - FILES_AND_DIRECTORIES: scan and match direct children directories and the direct children files from the given
     * <code>pathDir</code> not recursively <br>
     * - FILES_RECURSIVELY: scan and match the files from the given <code>pathDir</code> recursively <br>
     * - DIRECTORIES_RECURSIVELY: scan and match directories and files from the given <code>pathDir</code> recursively <br>
     * - FILES_AND_DIRECTORIES_RECURSIVELY: scan and match directories and files from the given <code>pathDir</code>
     * recursively
     *
     */
    public enum SCAN_STRATEGY {
        FILES(false, true, false),
        DIRECTORIES(true, false, false),
        FILES_AND_DIRECTORIES(true, true, false),
        FILES_RECURSIVELY(false, true, true),
        DIRECTORIES_RECURSIVELY(true, false, true),
        FILES_AND_DIRECTORIES_RECURSIVELY(true, true, true);

        private boolean cleanDirectories;

        private boolean cleanFiles;

        private boolean recursively;

        private SCAN_STRATEGY(boolean cleanDirectories, boolean cleanFiles, boolean recursively) {
            this.cleanFiles = cleanFiles;
            this.cleanDirectories = cleanDirectories;
            this.recursively = recursively;
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
         * Getter for cleanFilesOnly.
         *
         * @return the cleanFilesOnly
         */
        public boolean isCleanFiles() {
            return cleanFiles;
        }

        /**
         * Getter for recursively.
         *
         * @return the recursively
         */
        public boolean isRecursively() {
            return recursively;
        }
    }

    /**
     *
     * DOC amaumont FileDirCleaner class global comment. Detailled comment
     */
    static class CleanResult {

        Throwable firstException;

        int countExceptions;

        int deletedEntries;

        boolean alreadyLogged;
    }

    /**
     *
     * FileDirCleaner constructor.
     *
     * @param doAction
     * @param maxEntriesByDirectoryAndByType
     * @param maxDurationBeforeCleaning
     *
     * Default clean strategy is STRATEGY.CLEAN_FILES_ONLY
     */
    public FileDirCleaner(boolean doAction, int maxEntriesByDirectoryAndByType, long maxDurationBeforeCleaning) {
        this(doAction, SCAN_STRATEGY.FILES, maxEntriesByDirectoryAndByType, maxDurationBeforeCleaning);
    }

    /**
     *
     * DOC amaumont FileDirCleaner constructor comment.
     *
     * @param doAction TODO
     * @param strategy strategy to use
     * @param maxEntriesByDirectory max number of files to keep and max number of directories to keep (if
     * <code>cleanFilesOnly</code> is false), example: 5 will keep 5 files and 5 directories max
     * @param cleanAfterThisDuration in seconds, clean files and directories (if <code>cleanFilesOnly</code> is false)
     * according their last modified value
     */
    public FileDirCleaner(boolean doAction, SCAN_STRATEGY strategy, int maxEntriesByDirectory, long cleanAfterThisDuration) {
        super();
        this.doAction = doAction;
        this.maxEntriesByDirectoryAndByType = maxEntriesByDirectory;
        this.maxDurationBeforeCleaning = cleanAfterThisDuration;
        this.cleanDirectories = strategy.isCleanDirectories();
        this.cleanFiles = strategy.isCleanFiles();
        this.recursively = strategy.isRecursively();
    }

    public FileDirCleaner(boolean doAction, SCAN_STRATEGY strategy, int maxEntriesByDirectory, long cleanAfterThisDuration,
            boolean isCleanLibs) {
        super();
        this.doAction = doAction;
        this.maxEntriesByDirectoryAndByType = maxEntriesByDirectory;
        this.maxDurationBeforeCleaning = cleanAfterThisDuration;
        this.cleanDirectories = strategy.isCleanDirectories();
        this.cleanFiles = strategy.isCleanFiles();
        this.recursively = strategy.isRecursively();
    }

    final Comparator<File> datComparatorFiles = new Comparator<File>() {

        public int compare(File o1, File o2) {

            // if (o1.isDirectory() && o2.isFile()) {
            // return -1;
            // } else if (o1.isFile() && o2.isDirectory()) {
            // return 1;
            // } else {
            long compareResult = o1.lastModified() - o2.lastModified();
            if (compareResult == 0) {
                return 0;
            }
            return (int) (compareResult / Math.abs(compareResult));
            // }
        }

    };

    public int clean(String pathDir) {
        return clean(pathDir, null, null);
    }

    public int clean(String pathDir, String filesRegExpPattern) {
        return clean(pathDir, filesRegExpPattern, null);
    }

    /**
     *
     * "clean".
     *
     * @param pathDir, required
     * @param filesRegExpPattern, optional
     * @param directoriesRegExpPattern, optional
     * @return
     */
    public int clean(String pathDir, String filesRegExpPattern, String directoriesRegExpPattern) {
        return clean(pathDir, filesRegExpPattern, directoriesRegExpPattern, null);
    }

    /**
     *
     * "clean".
     *
     * @param pathDir, required
     * @param filesRegExpPattern, optional
     * @param directoriesRegExpPattern, optional
     * @return
     */
    public int clean(String pathDir, String filesRegExpPattern, String directoriesRegExpPattern, FileDirCleanerFilter filter) {
        if (pathDir == null) {
            throw new IllegalArgumentException("pathFolder can't be null");
        }
        this.cleanResult = new CleanResult();
        this.directoriesRegExpPattern = directoriesRegExpPattern;
        this.filesRegExpPattern = filesRegExpPattern;
        this.currentTime = System.currentTimeMillis();
        this.filter = filter;
        File dir = new File(pathDir);
        if (dir.isDirectory()) {
            cleanFilesDirRecursively(dir, true);
        }
        return cleanResult.deletedEntries;
    }

    /**
     *
     * "cleanFilesDirRecursively".
     *
     * @param dir
     * @param isRootDirectory
     */
    public void cleanFilesDirRecursively(File dir, boolean isRootDirectory) {
        try {
            File[] listFilesDirs = dir.listFiles();
            Arrays.sort(listFilesDirs, datComparatorFiles);
            int countMatchingDirs = 0;
            int countMatchingFiles = 0;
            int levelDeletedDir = 0;
            int levelDeletedFile = 0;
            for (File fileDirJob : listFilesDirs) {
                boolean isDirectory = fileDirJob.isDirectory();
                boolean fileMatches = false;
                boolean dirMatches = false;
                String fileDirName = fileDirJob.getName();
                if (isDirectory) {
                    dirMatches = directoriesRegExpPattern == null || fileDirName.matches(directoriesRegExpPattern);
                } else {
                    fileMatches = filesRegExpPattern == null || fileDirName.matches(filesRegExpPattern);
                }
                if (isDirectory && dirMatches) {
                    countMatchingDirs++;
                } else if (!isDirectory && fileMatches) {
                    countMatchingFiles++;
                }

            }

            boolean parentDirMatches = directoriesRegExpPattern == null || dir.getName().matches(directoriesRegExpPattern);

            for (File fileDirJob : listFilesDirs) {
                // System.out.println(fileDirJob.getAbsolutePath());
                String fileDirName = fileDirJob.getName();
                boolean fileMatches = false;
                boolean dirMatches = false;
                boolean isDirectory = fileDirJob.isDirectory();
                boolean tooManyDirs = (isRootDirectory || !isRootDirectory && recursively) && isDirectory
                        && maxEntriesByDirectoryAndByType > 0
                        && countMatchingDirs - levelDeletedDir > maxEntriesByDirectoryAndByType;
                boolean tooManyFiles = !isDirectory && maxEntriesByDirectoryAndByType > 0
                        && countMatchingFiles - levelDeletedFile > maxEntriesByDirectoryAndByType;
                boolean timeExceeded = maxDurationBeforeCleaning > 0
                        && currentTime - fileDirJob.lastModified() > maxDurationBeforeCleaning * 1000;
                try {
                    if (timeExceeded || tooManyDirs || tooManyFiles) {
                        if (isDirectory) {
                            dirMatches = directoriesRegExpPattern == null || fileDirName.matches(directoriesRegExpPattern);
                        } else {
                            fileMatches = filesRegExpPattern == null || fileDirName.matches(filesRegExpPattern);
                        }
                        if (isDirectory) {
                            if (cleanDirectories && dirMatches) {
                                if (checkFilter(fileDirJob)) {
                                    if (doAction) {
                                        org.apache.commons.io.FileUtils.deleteDirectory(fileDirJob);
                                        if (log.isDebugEnabled()) {
                                            log.debug("Deleted directory: " + fileDirJob);
                                        }
                                    } else {
                                        StringBuilder reason = new StringBuilder();
                                        String sep = "";
                                        if (timeExceeded) {
                                            reason.append("timeExceeded");
                                            sep = ", ";
                                        }
                                        if (tooManyDirs) {
                                            reason.append(sep + "tooManyDirs");
                                        }
                                        log.debug("'doAction' has to be true to remove recursively the directory ("
                                                + reason.toString() + "): " + fileDirJob);
                                    }
                                    cleanResult.deletedEntries++;
                                    levelDeletedDir++;
                                }
                            } else if (recursively) {
                                cleanFilesDirRecursively(fileDirJob, false);
                            }
                        } else {
                            if (cleanFiles && fileMatches && parentDirMatches) {
                                if (checkFilter(fileDirJob)) {
                                    if (doAction) {
                                        org.apache.commons.io.FileUtils.forceDelete(fileDirJob);
                                        if (log.isDebugEnabled()) {
                                            log.debug("Deleted file: " + fileDirJob);
                                        }
                                    } else {
                                        StringBuilder reason = new StringBuilder();
                                        String sep = "";
                                        if (timeExceeded) {
                                            reason.append("timeExceeded");
                                            sep = ", ";
                                        }
                                        if (tooManyFiles) {
                                            reason.append(sep + "tooManyFiles");
                                        }
                                        log.debug("'doAction' has to be true to remove the file (" + reason.toString() + "): "
                                                + fileDirJob);
                                    }
                                    cleanResult.deletedEntries++;
                                    levelDeletedFile++;
                                }
                            }
                        }
                    } else if (recursively && isDirectory) {
                        levelDeletedDir++;
                        cleanFilesDirRecursively(fileDirJob, false);
                    }
                } catch (Throwable t) {
                    cleanResult.countExceptions++;
                    if (cleanResult.firstException == null) {
                        cleanResult.firstException = t;
                    }
                }
            }
            if (cleanResult.firstException != null && !cleanResult.alreadyLogged) {
                log.warn("TempDataCleaner: " + cleanResult.countExceptions
                        + " error(s) have occured when trying to clean the following file or directory '" + dir.getAbsolutePath()
                        + "', the first error is the following : ", cleanResult.firstException);
                cleanResult.alreadyLogged = true;
            }

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean checkFilter(File fileDirJob) {
        boolean isDirectory = fileDirJob.isDirectory();
        if (filter != null) {
            if (!filter.acceptClean(fileDirJob)) {
                // log.info((isDirectory ? "Directory" : "File") + " refused to be cleaned: " + fileDirJob.getPath());
                return false;
            }
        }
        // log.info((isDirectory ? "Directory" : "File") + " accepted to be cleaned: " + fileDirJob.getPath());
        return true;
    }

}
