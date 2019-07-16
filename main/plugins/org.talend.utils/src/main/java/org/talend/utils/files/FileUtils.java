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
import java.io.PrintStream;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.talend.utils.string.StringUtilities;
import org.talend.utils.sugars.ReturnCode;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * DOC stephane class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 *
 */
public final class FileUtils {

    private static final String LS_FEATURE_KEY = "LS";

    private static final String LS_FEATURE_VERSION = "3.0";

    private static final String CORE_FEATURE_KEY = "Core";

    private static final String CORE_FEATURE_VERSION = "2.0";

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
                        if (thatInfo.getPrefix() == null) {
                            return name.endsWith(thatInfo.getSuffix());
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

    /**
     * DOC xlwang Comment method "createProjectFile".
     */
    public static void createProjectFile(String projectName, File tmpProjectFile) throws Exception {
        tmpProjectFile.createNewFile();
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream(tmpProjectFile);
            ps = new PrintStream(fos);
            ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            ps.println("<projectDescription>");
            ps.println("\t<name>" + projectName + "</name>");
            ps.println("\t<comment></comment>");
            ps.println("\t<projects></projects>");
            ps.println("\t<buildSpec></buildSpec>");
            ps.println("\t<natures>");
            ps.println("\t\t<nature>org.talend.core.talendnature</nature>");
            ps.println("\t</natures>");
            ps.println("</projectDescription>");
            ps.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    public static void writeXMLFile(Document document, Writer writer) {
        DOMImplementation implementation = document.getImplementation();

        if (implementation.hasFeature(LS_FEATURE_KEY, LS_FEATURE_VERSION)
                && implementation.hasFeature(CORE_FEATURE_KEY, CORE_FEATURE_VERSION)) {
            DOMImplementationLS implementationLS = (DOMImplementationLS) implementation.getFeature(LS_FEATURE_KEY,
                    LS_FEATURE_VERSION);
            LSSerializer serializer = implementationLS.createLSSerializer();
            DOMConfiguration configuration = serializer.getDomConfig();

            configuration.setParameter("well-formed", Boolean.TRUE);
            configuration.setParameter("comments", true);

            LSOutput output = implementationLS.createLSOutput();
            output.setEncoding("UTF-8");
            output.setCharacterStream(writer);
            serializer.write(document, output);
        }
    }
}
