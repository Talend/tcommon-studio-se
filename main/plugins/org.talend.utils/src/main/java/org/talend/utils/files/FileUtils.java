// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.talend.utils.string.StringUtilities;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC stephane class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static synchronized void replaceInFile(String path, String oldString, String newString)
            throws IOException, URISyntaxException {
        File file = new File(path);
        File tmpFile = new File(path + ".tmp");//$NON-NLS-1$

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        dis = new DataInputStream(bis);

        OutputStream tempOutputStream = new FileOutputStream(tmpFile);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(tempOutputStream, "UTF8")); //$NON-NLS-1$

        String line;
        int len = 0;
        String newLine;
        byte[] buf2 = new byte[1024];

        while (((len = dis.read(buf2))) != -1) {
            line = new String(buf2, 0, len);
            newLine = line.replace(oldString, newString);
            newLine = new String((newLine).getBytes(), "UTF8");//$NON-NLS-1$
            bufferedWriter.write(newLine);
            bufferedWriter.flush();
        }

        bufferedWriter.close();
        dis.close();

        file.delete();
        tmpFile.renameTo(file);
    }

    /**
     * Method "checkBracketsInFile" checks whether the parentheses are well balanced on each line of the given file.
     * 
     * @param path the path of the file to check
     * @return true when all lines contain well balanced parentheses.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static synchronized List<ReturnCode> checkBracketsInFile(String path) throws IOException, URISyntaxException {
        List<ReturnCode> returncodes = new ArrayList<>();
        File file = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;
        int lineNb = 0;

        while ((line = in.readLine()) != null) {
            ReturnCode checkBlocks = StringUtilities.checkBalancedParenthesis(line, '(', ')');
            lineNb++;
            if (!checkBlocks.isOk()) {
                String errorMsg = "Line " + lineNb + ": " + checkBlocks.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
                returncodes.add(new ReturnCode(errorMsg, false));
            }
        }

        in.close();
        return returncodes;
    }

    /**
     * Iterate over a folder and append the files that match the filter to a list given in parameter.
     * 
     * @param aFolder - the folder to iterate over.
     * @param fileList - the list to append into.
     * @param filenameFilter - the filename filter.
     */
    public static void getAllFilesFromFolder(File aFolder, List<File> fileList, FilenameFilter filenameFilter) {
        if (aFolder != null) {
            File[] folderFiles = aFolder.listFiles(filenameFilter);
            if (fileList != null && folderFiles != null) {
                Collections.addAll(fileList, folderFiles);
            }
            File[] allFolders = aFolder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File arg0) {
                    return arg0.isDirectory();
                }
            });
            if (allFolders != null) {
                for (File folder : allFolders) {
                    getAllFilesFromFolder(folder, fileList, filenameFilter);
                }
            }
        }
    }

    /**
     * Iterate over a folder and append the files that match the filter to an empty list.
     * 
     * @param aFolder - the folder to iterate over.
     * @param filenameFilter - the filename filter.
     * @return the list of files that match the filter.
     */
    public static List<File> getAllFilesFromFolder(File aFolder, FilenameFilter filenameFilter) {
        List<File> files = new ArrayList<>();
        getAllFilesFromFolder(aFolder, files, filenameFilter);
        return files;
    }

    /**
     * Iterate over a folder and append the files that match the filters to an empty list. The filters are a Map where
     * the key is the file prefix
     * 
     * @param aFolder - the folder to iterate over.
     * @param filterInfo - the filename filter.
     * @return the list of files that match the filter.
     */
    public static List<File> getAllFilesFromFolder(File aFolder, Set<FilterInfo> filterInfo) {
        List<File> files = new ArrayList<>();
        if (filterInfo != null) {
            for (FilterInfo info : filterInfo) {
                final FilterInfo thatInfo = info;
                files.addAll(getAllFilesFromFolder(aFolder, new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        if (name == null) {
                            return false;
                        }
                        return name.startsWith(thatInfo.getPrefix()) && name.endsWith(thatInfo.getSuffix());
                    }
                }));
            }
        }
        return files;
    }

    /**
     * Iterates over a folder files (not recursive) and delete those for which the filename matches the condition given
     * in the {@link Function}
     * 
     * @param folder the folder in which to delete the files
     * @param func a {@link Function} that will be used to filter on the files to delete, according to their name
     */
    public static void deleteFiles(final File folder, final Function<String, Boolean> func) {
        if (folder != null && func != null) {
            if (folder.exists()) {
                FilenameFilter filter = new FilenameFilter() {

                    @Override
                    public boolean accept(File _dir, String name) {
                        return func.apply(name);
                    }

                };
                List<File> filesToRemove = getAllFilesFromFolder(folder, filter);
                for (File fileToRemove : filesToRemove) {
                    fileToRemove.delete();
                }
            }
        }
    }

    /**
     * 
     * DOC ggu Comment method "createTmpFolder".
     * 
     * @param prefix
     * @param suffix
     * @return
     */
    public static File createTmpFolder(String prefix, String suffix) {
        File tempFolder = null;
        try {
            tempFolder = File.createTempFile(prefix, suffix);
            tempFolder.delete();
        } catch (IOException e) {
            String tempFolderName = prefix + System.currentTimeMillis() + suffix;
            tempFolder = createUserTmpFolder(tempFolderName);
        }
        tempFolder.mkdirs();
        return tempFolder;
    }

    /**
     * 
     * DOC ggu Comment method "createUserTmpFolder".
     * 
     * @param folderName
     * @return
     */
    public static File createUserTmpFolder(String folderName) {
        File tmpFolder = new File(System.getProperty("user.dir"), "temp/" + folderName); //$NON-NLS-1$ //$NON-NLS-2$
        tmpFolder.mkdirs();
        return tmpFolder;
    }

    public static String readFileByLines(String fileName) throws IOException {
        String fileContent = ""; //$NON-NLS-1$
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n"); //$NON-NLS-1$
                line++;
            }
            fileContent = sb.toString();
            reader.close();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return fileContent;
    }
}
