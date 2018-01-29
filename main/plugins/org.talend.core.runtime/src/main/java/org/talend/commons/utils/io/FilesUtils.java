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
package org.talend.commons.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.i18n.internal.Messages;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.repository.SVNConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class FilesUtils {

    private static final int BUFFER_SIZE = 64 * 1024;

    private static Logger logger = Logger.getLogger(FilesUtils.class);

    private FilesUtils() {
    }

    private static final String MIGRATION_FILE_EXT = ".mig"; //$NON-NLS-1$

    private static final String ANY_FILE_EXT = ".*"; //$NON-NLS-1$

    public static boolean isSVNFolder(String name) {
        return org.talend.utils.io.FilesUtils.isSVNFolder(name);
    }

    public static boolean isSVNFolder(File file) {
        return org.talend.utils.io.FilesUtils.isSVNFolder(file);
    }

    public static boolean isSVNFolder(IResource resource) {
        if (resource != null) {
            if (isSVNFolder(resource.getName())) {
                return true;
            } else { // check the resources under svn folder.
                IPath path = resource.getFullPath();
                if (path != null) {
                    for (int index = 0; index < path.segmentCount(); index++) {
                        String segment = path.segment(index);
                        if (isSVNFolder(segment)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param source
     * @param target
     * @param emptyTargetBeforeCopy
     * @param sourceFolderFilter
     * @param sourceFileFilter
     * @param copyFolder
     * @param synchronize if set to true, synchronize source and target, remove non existing folders/files.
     * @param monitorWrap
     * @throws IOException
     */
    public static void copyFolder(File source, File target, boolean emptyTargetBeforeCopy, final FileFilter sourceFolderFilter,
            final FileFilter sourceFileFilter, boolean copyFolder, boolean synchronize, IProgressMonitor... monitorWrap)
            throws IOException {
        copyFolder(source, target, target.getAbsolutePath(), emptyTargetBeforeCopy, sourceFolderFilter, sourceFileFilter,
                copyFolder, synchronize, monitorWrap);
    }

    /**
     * DOC nrousseau Comment method "copyFolder".
     * 
     * @param source
     * @param target
     * @param emptyTargetBeforeCopy
     * @param sourceFolderFilter
     * @param sourceFileFilter
     * @param copyFolder
     * @param monitorWrap
     * @throws IOException
     */
    public static void copyFolder(File source, File target, boolean emptyTargetBeforeCopy, final FileFilter sourceFolderFilter,
            final FileFilter sourceFileFilter, boolean copyFolder, IProgressMonitor... monitorWrap) throws IOException {
        copyFolder(source, target, target.getAbsolutePath(), emptyTargetBeforeCopy, sourceFolderFilter, sourceFileFilter,
                copyFolder, false, monitorWrap);
    }

    private static void copyFolder(File source, File target, String targetBaseFolder, boolean emptyTargetBeforeCopy,
            final FileFilter sourceFolderFilter, final FileFilter sourceFileFilter, boolean copyFolder, boolean synchronize,
            IProgressMonitor... monitorWrap) throws IOException {
        // cf bug 14658
        boolean needAvoidCopyItself = false;
        if (targetBaseFolder.equals(source.getAbsolutePath())) {
            needAvoidCopyItself = true;
        }
        IProgressMonitor monitor = null;
        if (monitorWrap != null && monitorWrap.length == 1) {
            monitor = monitorWrap[0];
        }

        if (!target.exists()) {
            target.mkdirs();
        }

        if (emptyTargetBeforeCopy) {
            emptyFolder(target);
        }

        FileFilter folderFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && (sourceFolderFilter == null || sourceFolderFilter.accept(pathname));
            }

        };
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && (sourceFileFilter == null || sourceFileFilter.accept(pathname));
            }

        };

        if (!copyFolder || (copyFolder && !needAvoidCopyItself)) {
            Map<String, File> foldersToDelete = new HashMap<String, File>();
            if (synchronize) {
                if (target.exists()) {
                    for (File file : target.listFiles()) {
                        if (file.isDirectory()) {
                            foldersToDelete.put(file.getName(), file);
                        }
                    }
                }
            }

            if (source.exists()) {
                for (File current : source.listFiles(folderFilter)) {
                    if (foldersToDelete.keySet().contains(current.getName())) {
                        foldersToDelete.remove(current.getName());
                    }
                    if (monitor != null && monitor.isCanceled()) {
                        throw new OperationCanceledException(Messages.getString("FilesUtils.operationCanceled")); //$NON-NLS-1$
                    }
                    if (copyFolder) {
                        File newFolder = new File(target, current.getName());
                        newFolder.mkdir();
                        copyFolder(current, newFolder, targetBaseFolder, emptyTargetBeforeCopy, sourceFolderFilter,
                                sourceFileFilter, copyFolder, synchronize);
                    } else {
                        copyFolder(current, target, targetBaseFolder, emptyTargetBeforeCopy, sourceFolderFilter,
                                sourceFileFilter, copyFolder, synchronize);
                    }
                }
            }
            if (synchronize) {
                for (File file : foldersToDelete.values()) {
                    removeFolder(file, true);
                }
            }
        }

        Map<String, File> filesToDelete = new HashMap<String, File>();
        if (synchronize) {
            if (target.exists()) {
                for (File file : target.listFiles()) {
                    if (!file.isDirectory()) {
                        filesToDelete.put(file.getName(), file);
                    }
                }
            }
        }
        if (source.exists()) {
            for (File current : source.listFiles(fileFilter)) {
                if (filesToDelete.keySet().contains(current.getName())) {
                    filesToDelete.remove(current.getName());
                }

                if (monitor != null && monitor.isCanceled()) {
                    throw new OperationCanceledException(""); //$NON-NLS-1$
                }
                File out = new File(target, current.getName());
                copyFile(current, out);
            }
        }
        if (synchronize) {
            for (File file : filesToDelete.values()) {
                file.delete();
            }
        }

    }

    public static void emptyFolder(File toEmpty) {
        if (!toEmpty.exists()) {
            return;
        }
        final File[] listFiles = toEmpty.listFiles(getExcludeSystemFilesFilter());
        for (File current : listFiles) {
            if (current.isDirectory()) {
                emptyFolder(current);
            }
            current.delete();
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        // Need to recopy the file in one of these cases:
        // 1. target doesn't exists (never copied)
        // 2. if the target exists, compare their sizes, once defferent, for the copy.
        // 2. target exists but source has been modified recently(not used right now)

        if (!target.exists() || source.length() != target.length() || source.lastModified() > target.lastModified()) {
            copyFile(new FileInputStream(source), target);
        }
    }

    public static void removeFile(File target) {
        if (target.exists() && target.isFile()) {
            target.delete();
        }
    }

    public static void copyFile(InputStream source, File target) throws IOException {
        FileOutputStream fos = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }

            fos = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = source.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } finally {
            try {
                source.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }

    }

    public static void replaceInFile(String regex, String fileName, String replacement) throws IOException {
        InputStream in = new FileInputStream(fileName);
        StringBuffer buffer = new StringBuffer();
        try {
            InputStreamReader inR = new InputStreamReader(in);
            BufferedReader buf = new BufferedReader(inR);
            String line;
            while ((line = buf.readLine()) != null) {
                buffer.append(StringUtils.replace(line, regex, replacement)).append("\n"); //$NON-NLS-1$
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            in.close();
        }

        OutputStream os = new FileOutputStream(fileName);
        os.write(buffer.toString().getBytes());
        os.close();
    }

    public static List<URL> getFilesFromFolder(Bundle bundle, String path, String extension) {
        return getFilesFromFolder(bundle, path, extension, true, false);
    }

    public static List<URL> getFilesFromFolder(Bundle bundle, String path, String extension, boolean absoluteURL, boolean nested) {
        List<URL> toReturn = new ArrayList<URL>();

        Enumeration entryPaths = bundle.getEntryPaths(path);
        if (entryPaths == null) {
            return toReturn;
        }
        for (Enumeration enumer = entryPaths; enumer.hasMoreElements();) {
            String fileName = (String) enumer.nextElement();
            if (fileName.endsWith(extension)) {
                URL url = bundle.getEntry(fileName);
                if (absoluteURL) {
                    try {
                        toReturn.add(FileLocator.toFileURL(url));
                    } catch (IOException e) {
                        CommonExceptionHandler.process(e);
                    }
                } else {
                    toReturn.add(url);
                }
            } else {
                if (nested) {
                    List<URL> subResult = getFilesFromFolder(bundle, fileName, extension, absoluteURL, nested);
                    toReturn.addAll(subResult);
                }
            }
        }
        return toReturn;
    }

    /**
     * DOC ycbai Comment method "getFiles".
     * 
     * @param file
     * @param extension
     * @param excludedFile
     * @param excludedFolder
     * @param nested
     * @return
     */
    public static List<File> getFiles(File file, String extension, String excludedFile, String excludedFolder, boolean nested) {
        List<File> results = new ArrayList<File>();
        if (file.isFile()) {
            boolean canAdd = true;
            if ((extension != null && !file.getName().endsWith(extension))
                    || (excludedFile != null && excludedFile.equals(file.getName()))) {
                canAdd = false;
            }
            if (canAdd) {
                results.add(file);
            }
        } else if (file.isDirectory() && nested) {
            if (excludedFolder != null && excludedFolder.equals(file.getName())) {
                return results;
            }
            File[] files = file.listFiles();
            for (File file2 : files) {
                results.addAll(getFiles(file2, extension, excludedFile, excludedFolder, nested));
            }
        }

        return results;
    }

    /**
     * DOC ycbai Comment method "getFileURLs".
     * 
     * @param file
     * @param extension
     * @param excludedFile
     * @param excludedFolder
     * @param nested
     * @return
     * @throws MalformedURLException
     */
    public static List<URL> getFileURLs(File file, String extension, String excludedFile, String excludedFolder, boolean nested)
            throws MalformedURLException {
        List<URL> urls = new ArrayList<URL>();
        List<File> files = getFiles(file, extension, excludedFile, excludedFolder, nested);
        for (File file2 : files) {
            urls.add(file2.toURI().toURL());
        }
        return urls;
    }

    /**
     * DOC ycbai Comment method "getFileURLs".
     * 
     * @param file
     * @return
     * @throws MalformedURLException
     */
    public static List<URL> getFileURLs(File file) throws MalformedURLException {
        return getFileURLs(file, null, null, null, true);
    }

    public static List<File> getFilesFromFolderByName(File file, String searchFileName, String[] extensions,
            String excludedFolder, boolean nested) {
        List<File> results = new ArrayList<File>();
        if (file.isFile()) {
            boolean consideredExt = false;
            boolean consideredName = false;
            if (extensions == null || extensions.length == 0) {
                consideredExt = true;
            } else {
                for (String ext : extensions) {
                    if (file.getName().endsWith(ext) || ext.endsWith("*")) {
                        consideredExt = true;
                        break;
                    }
                }
            }
            if (searchFileName == null) {
                consideredName = true;
            } else {
                if (searchFileName.equals(file.getName())) {
                    consideredName = true;
                }
            }
            if (consideredExt && consideredName) {
                results.add(file);
            }
        } else if (nested && file.isDirectory() && !isSVNFolder(file)) {
            if (excludedFolder != null && excludedFolder.equals(file.getName())) {
                return results;
            }
            File[] files = file.listFiles();
            for (File file2 : files) {
                results.addAll(getFilesFromFolderByName(file2, searchFileName, extensions, excludedFolder, nested));
            }
        }

        return results;
    }

    public static List<File> getJarFilesFromFolder(File file, String fileName) throws MalformedURLException {
        return getFilesFromFolderByName(file, fileName, new String[] { ANY_FILE_EXT }, null, true);
    }

    public static List<File> getJarFilesFromFolder(File file, String fileName, String excludedFolder)
            throws MalformedURLException {
        return getFilesFromFolderByName(file, fileName, new String[] { ANY_FILE_EXT }, excludedFolder, true);
    }

    public static List<File> getDllFilesFromFolder(File file, String fileName) throws MalformedURLException {
        return getFilesFromFolderByName(file, fileName, new String[] { ".dll" }, null, true); //$NON-NLS-1$
    }

    public static FileFilter getExcludeSystemFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !isSVNFolder(pathname) && !pathname.toString().endsWith(".dummy"); //$NON-NLS-1$
            }

        };
        return filter;
    }

    public static FileFilter getAcceptJARFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }

        };
        return filter;
    }

    public static FileFilter getAcceptModuleFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }

        };
        return filter;
    }

    public static String[] getAcceptJARFilesSuffix() {
        return new String[] { "*.*" };
    }

    public static FileFilter getAcceptPMFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.toString().endsWith(".pm"); //$NON-NLS-1$
            }

        };
        return filter;
    }

    /**
     * Load in a list all lines of the given file.
     * 
     * @throws IOException
     */
    public static List<String> getContentLines(String filePath) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        List<String> lines = new ArrayList<String>();
        try {
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } finally {
            in.close();
        }
        return lines;
    }

    /**
     * .
     * 
     * @param path
     * @param pathIsFilePath if true the given path has a filename at last segment so this segment is not processed
     * @deprecated use instead {@link org.talend.utils.io.FilesUtils#createFoldersIfNotExists(String, boolean)}
     */
    @Deprecated
    public static void createFoldersIfNotExists(String path, boolean pathIsFilePath) {
        File filePath = new File(path);
        File fileFolder = filePath;
        if (pathIsFilePath) {
            fileFolder = filePath.getParentFile();
        }

        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
    }

    public static void main(String[] args) {
        try {
            // createFoldersIfNotExists("c:\\test\\test1/test2", false);
            // createFoldersIfNotExists("c:\\test10\\test11/test20/test.pl", true);
            unzip("d:/tFileOutputPDF.zip", "d:/temp"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOC amaumont Comment method "removeDirectory".
     * 
     * @param b
     */
    public static boolean removeFolder(String pathFolder, boolean recursiveRemove) {
        File folder = new File(pathFolder);
        if (folder.isDirectory()) {
            return removeFolder(folder, recursiveRemove);
        }
        return false;
    }

    /**
     * DOC amaumont Comment method "removeFolder".
     * 
     * @param current
     * @param removeRecursivly
     */
    public static boolean removeFolder(File folder, boolean removeRecursivly) {
        if (removeRecursivly) {
            for (File current : folder.listFiles()) {
                if (current.isDirectory()) {
                    removeFolder(current, true);
                } else {
                    current.delete();
                }
            }
        }
        return folder.delete();
    }

    public static void removeEmptyFolder(File folder) {
        if (!folder.isDirectory()) {
            return;
        }
        File[] children = folder.listFiles();
        if (children == null) {
            folder.delete();
        } else {
            for (File current : children) {
                removeEmptyFolder(current);
            }
        }
        children = folder.listFiles();
        if (children == null || children.length == 0) {
            folder.delete();
        }
    }

    /**
     * 
     * Method "extractPathFolderFromFilePath".
     * 
     * @param filePath
     * @return
     * @deprecated use instead {@link org.talend.utils.io.FilesUtils#extractPathFolderFromFilePath(String)}
     */
    @Deprecated
    public static String extractPathFolderFromFilePath(String filePath) {
        File completePath = new File(filePath);
        return completePath.getParent();
    }

    /**
     * Unzip the component file to the user folder.
     * 
     * @param zipFile The component zip file
     * @param targetFolder The user folder
     * @return
     * @throws Exception
     */
    public static void unzip(String zipFile, String targetFolder) throws Exception {
        Exception exception = null;
        ZipFile zip = new ZipFile(zipFile);
        byte[] buf = new byte[8192];

        try {
            Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zip.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();

                File file = new File(targetFolder, entry.getName());

                if (entry.isDirectory()) {
                    if (!file.exists()) {
                        file.mkdir();
                    }
                } else {

                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    InputStream zin = zip.getInputStream(entry);
                    OutputStream fout = new FileOutputStream(file);
                    // check if parent folder exists
                    File dir = file.getParentFile();
                    if (dir.isDirectory() && !dir.exists()) {
                        dir.mkdirs();
                    }

                    try {
                        while (true) {
                            int bytesRead = zin.read(buf);
                            if (bytesRead == -1) { // end of file
                                break;
                            }
                            fout.write(buf, 0, bytesRead);

                        }
                        fout.flush();
                    } catch (Exception e) {
                        exception = e;
                        // stop looping
                        return;
                    } finally {
                        zin.close();
                        fout.close();
                    }
                }
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            zip.close();

            if (exception != null) {
                // notify caller with exception
                throw exception;
            }
        }
    }

    /**
     * DOC sgandon Comment method "getAllFilesFromFolder".
     * 
     * @param aFolder
     * @param fileList
     * @param filenameFilter
     */
    public static void getAllFilesFromFolder(File aFolder, List<File> fileList, FilenameFilter filenameFilter) {
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

    /**
     * DOC according to the replace string map to migrate files of given folders from old content to new ones.
     * 
     * @param migFolder folder to migrate
     * @param acceptFileExtentionNames extention name of the files which should to be migrated
     * @param replaceStringMap the replace string map {key=oldString, value=newString}
     * @param log logger to record logs
     * @return true if success, false if get exceptions
     */
    public static boolean migrateFolder(File migFolder, final String[] acceptFileExtentionNames,
            Map<String, String> replaceStringMap, Logger log) {
        boolean result = true;

        ArrayList<File> fileList = new ArrayList<File>();
        getAllFilesFromFolder(migFolder, fileList, new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                for (String extName : acceptFileExtentionNames) {
                    if (name.endsWith(extName)) {
                        return true;
                    }
                }
                return false;
            }
        });
        log.info("-------------- Migrating " + fileList.size() + " files"); //$NON-NLS-1$ //$NON-NLS-2$

        for (File sample : fileList) {
            result &= migrateFile(sample, replaceStringMap, log);
        }

        return result;
    }

    /**
     * DOC according to the replace string map to migrate file from old content to new ones.
     * 
     * @param sample
     * @param replaceStringMap
     * @param log
     * @return
     */
    public static boolean migrateFile(File sample, Map<String, String> replaceStringMap, Logger log) {
        log.info("-------------- Migrating : " + sample.getAbsolutePath()); //$NON-NLS-1$
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(sample));
            BufferedWriter fileWriter = new BufferedWriter(
                    new FileWriter(new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT)));

            while (fileReader.ready()) {
                String line = fileReader.readLine();
                for (String key : replaceStringMap.keySet()) {
                    line = line.replaceAll(key, replaceStringMap.get(key));
                }
                fileWriter.append(line);
                fileWriter.newLine();
            }

            fileWriter.flush();
            fileWriter.close();
            fileWriter = null;
            fileReader.close();
            fileReader = null;
            System.gc();
        } catch (Exception e) {
            log.error("!!!!!!!!!!!  Error transforming (" + sample.getAbsolutePath() + ")\n" + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }

        // remove original files and rename new ones to old ones
        boolean isDeleted = sample.delete();
        log.info(sample.getAbsolutePath() + (isDeleted ? " is deleted." : " failed to delete.")); //$NON-NLS-1$ //$NON-NLS-2$
        boolean isrenamed = new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT).renameTo(sample);
        log.info(sample.getAbsolutePath() + MIGRATION_FILE_EXT + (isrenamed ? " is renamed." : " failed to rename.")); //$NON-NLS-1$ //$NON-NLS-2$

        return true;
    }

    /**
     * get all file from the folder.
     * 
     * @param folder parrent folder
     * @param ext file's extention
     * @param recursive nested flag
     * @return
     */
    public static List<IFile> getFiles(IFolder folder, String ext, boolean recursive) {
        String fileExt = ext == null ? "" : ext.trim().length() > 0 ? ext.trim() : ""; //$NON-NLS-1$ //$NON-NLS-2$
        List<IFile> result = new ArrayList<IFile>();
        try {
            IResource[] members = folder.members();
            for (IResource res : members) {
                if (res instanceof IFolder) {
                    if (recursive) {
                        result.addAll(getFiles((IFolder) res, ext, recursive));
                    }
                } else if (res instanceof IFile) {
                    IFile file = (IFile) res;
                    if (fileExt.equals("") || fileExt.equals(file.getFileExtension())) { //$NON-NLS-1$
                        result.add(file);
                    }
                }
            }
        } catch (CoreException e) {
            logger.warn(e, e);
        }
        return result;
    }

    /** fullPath,eg: "D:\\ALL_integrate_patch_v4.2.2.r63143-20110722.zip **/
    public static void downloadFileFromWeb(URL resourceURL, String fullPath, final String username, final String password) {
        Authenticator defaultAuthenticator = NetworkUtil.getDefaultAuthenticator();
        // authentification for the url by using username and password,see bugTDI-18626
        Authenticator.setDefault(new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }

        });
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection conn = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        File fileTostore = null;
        try {
            conn = (HttpURLConnection) resourceURL.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);
            fileTostore = new File(fullPath);

            if (!fileTostore.exists()) {
                fileTostore.createNewFile();
            } else {
                fileTostore.delete();
                fileTostore.createNewFile();
            }
            fos = new FileOutputStream(fullPath);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buf = null;
                bis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    /**
     * copy the dirictory(include sub folders and files) from the source to the target, the copyed dirictory will be
     * under the target.
     * 
     * @param source
     * @param target
     */
    public static void copyDirectory(File source, File target) {
        File tarpath = new File(target, source.getName());
        if (source.isDirectory()) {
            tarpath.mkdir();
            File[] dir = source.listFiles();
            for (File element : dir) {
                copyDirectory(element, tarpath);
            }
        } else {
            try {
                InputStream is = new FileInputStream(source);
                OutputStream os = new FileOutputStream(tarpath);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                is.close();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * copy the dirictory(include sub folders and files, don't include ".svn" folders) from the source to the target,
     * the copyed dirictory will be under the target.
     * 
     * @param source
     * @param target
     */
    public static void copyDirectoryWithoutSvnFolder(File source, File target) {
        File tarpath = new File(target, source.getName());
        if (source.isDirectory()) {
            tarpath.mkdir();
            File[] dir = source.listFiles();
            for (File element : dir) {
                if (element.getName().equals(SVNConstant.SVN_FOLDER)) {
                    continue;
                }
                copyDirectoryWithoutSvnFolder(element, tarpath);
            }
        } else {
            try {
                InputStream is = new FileInputStream(source);
                OutputStream os = new FileOutputStream(tarpath);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                is.close();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(File file, boolean delete) {
        if (file.exists()) {
            if (file.isFile() && delete) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file2 : files) {
                    deleteFile(file2, true);
                }
            }
            if (delete) {
                file.delete();
            }
        }
    }

    /**
     * **/
    class RepositoryAuthentication extends Authenticator {

        private String userName;

        private String password;

        public RepositoryAuthentication(String userName, String password) {
            super();
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password.toCharArray());
        }
    }

    /**
     * 
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public static Document parse(String xmlFile) throws Exception {
        // MOD qiongli 2012-4-10,replace parameter of method parse String with File,it dosen't support String which
        // contain chinese.
        File file = new File(xmlFile);
        Document domTree = parse(file);
        return domTree;
    }

    /**
     * 
     * DOC qiongli Comment method "parse".
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static Document parse(File file) throws Exception {
        if (file == null || !file.exists()) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document domTree = db.parse(file);
        return domTree;
    }

    /**
     * 
     * @param xmlTree
     * @return
     */
    public static String getUUID(String xmlFile) {
        // MOD xqliu 2012-04-01 TDQ-4957
        String uuid = "";//$NON-NLS-1$
        Document xmlTree;
        try {
            xmlTree = parse(xmlFile);
            if (xmlTree != null) {
                Node namedItem = xmlTree.getFirstChild();
                if (namedItem != null) {
                    NodeList childNodes = namedItem.getChildNodes();
                    if (childNodes != null) {
                        int length = childNodes.getLength();
                        for (int i = 0; i < length; ++i) {
                            Node item = childNodes.item(i);
                            if (item != null && item.getNextSibling() != null && item.getNextSibling().getAttributes() != null) {
                                Node itemNode = item.getNextSibling().getAttributes().getNamedItem("xmi:id");//$NON-NLS-1$
                                if (itemNode != null) {
                                    uuid = itemNode.getNodeValue();
                                    break;
                                }
                            }
                        }
                    }
                }
                xmlTree = null;
            }
        } catch (Exception e) {
            logger.warn(e, e);
        }
        return uuid;
    }

    /**
     * rename the folder.
     * 
     * @param srcfolder
     * @param targetfolder
     */
    public static void renameFolder(File srcfolder, File targetfolder) {
        if (srcfolder.exists() && srcfolder.isDirectory()) {
            try {
                if (!targetfolder.exists()) {
                    targetfolder.mkdirs();
                }
                File[] listFiles = srcfolder.listFiles();
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        copyDirectory(file, targetfolder);
                    } else if (file.isFile()) {
                        copyFile(file, new File(targetfolder.getAbsolutePath() + File.separator + file.getName()));
                    }
                }
            } catch (Exception e) {
                logger.warn(e, e);
            }
            deleteFile(srcfolder, true);
        }
    }

    /**
     * DOC xqliu Comment method "getAllFilesFromFolder".
     * 
     * @param aFolder
     * @param filenameFilter
     * @return
     */
    public static List<File> getAllFilesFromFolder(File aFolder, FilenameFilter filenameFilter) {
        List<File> files = new ArrayList<File>();
        getAllFilesFromFolder(aFolder, files, filenameFilter);
        return files;
    }

    public static String getFileRealPath(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            filePath = filePath.replace("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
            file = new File(filePath);
            if (file.exists()) {
                return filePath;
            }
        }

        return filePath;
    }

    public static BufferedReader isFilePathAvailable(String fileStr, FileConnection connection) throws IOException,
            UnsupportedEncodingException, FileNotFoundException {
        BufferedReader in;
        File file = new File(fileStr);
        Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
        if (connection.getEncoding() == null || connection.getEncoding().equals("")) { //$NON-NLS-1$
            connection.setEncoding(guessedCharset.displayName());
        }
        // read the file width the limit : MAXIMUM_ROWS_TO_PREVIEW
        in = new BufferedReader(new InputStreamReader(new FileInputStream(fileStr), guessedCharset.displayName()));

        return in;
    }

    protected static List<IResource> getExistedResources(IResource file, boolean ignoreFileNameCase) throws CoreException {
        List<IResource> existedFiles = new ArrayList<>();
        if (ignoreFileNameCase) {
            final String currentFileName = file.getName();
            IContainer parentFile = file.getParent();
            if (parentFile.exists()) {
                for (IResource resource : parentFile.members()) {
                    if (resource.getName().equalsIgnoreCase(currentFileName)) {
                        existedFiles.add(resource);
                    }
                }
            }
        } else if (file.exists()) { // only add current file
            existedFiles.add(file);
        }
        return existedFiles;
    }

    public static void removeExistedResources(IProgressMonitor monitor, IResource currentResources, boolean ignoreFileNameCase,
            boolean overwrite) throws Exception {
        List<IResource> existedSameFiles = getExistedResources(currentResources, ignoreFileNameCase);

        // existed current one and not overwrite.
        if (existedSameFiles.contains(currentResources) && !overwrite) {
            throw new IOException("Can't overwrite the file: " + currentResources);
        }
        // delete all
        for (IResource resource : existedSameFiles) {
            resource.delete(true, monitor);
        }
    }

}
