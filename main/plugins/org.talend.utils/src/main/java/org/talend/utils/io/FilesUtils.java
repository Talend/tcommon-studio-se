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
package org.talend.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 *
 */
public final class FilesUtils {

    private static final int BUFFER_SIZE = 64 * 1024;

    public static final String GITKEEP = ".gitkeep"; //$NON-NLS-1$

    public static final String SVN_FOLDER_NAMES[] = new String[] { ".svn", "_svn" }; //$NON-NLS-1$ //$NON-NLS-2$

    public static boolean isSVNFolder(String name) {
        if (name != null) {
            String checkedName = name.toLowerCase();
            for (String element : SVN_FOLDER_NAMES) {
                if (element.equals(checkedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSVNFolder(File file) {
        if (file != null) {
            return isSVNFolder(file.getName());
        }
        return false;
    }

    private FilesUtils() {
        super();
    }

    public static boolean isEmptyFolder(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length <= 0) {
                return true;
            }
        }
        return false;
    }

    public static void copyFolder(File source, File target, boolean emptyTargetBeforeCopy, final FileFilter sourceFolderFilter,
            final FileFilter sourceFileFilter, boolean copyFolder) throws IOException {
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
        File[] folders = source.listFiles(folderFilter);
        if (folders != null) {
            for (File current : folders) {
                if (copyFolder) {
                    File newFolder = new File(target, current.getName());
                    newFolder.mkdir();
                    copyFolder(current, newFolder, emptyTargetBeforeCopy, sourceFolderFilter, sourceFileFilter, copyFolder);
                } else {
                    copyFolder(current, target, emptyTargetBeforeCopy, sourceFolderFilter, sourceFileFilter, copyFolder);
                }
            }
        }

        File[] files = source.listFiles(fileFilter);
        if (files != null) {
            for (File current : files) {
                File out = new File(target, current.getName());
                copyFile(current, out);
            }
        }
    }

    private static void emptyFolder(File toEmpty) {
        for (File current : toEmpty.listFiles()) {
            if (current.isDirectory()) {
                emptyFolder(current);
            }
            current.delete();
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        // Need to recopy the file in one of these cases:
        // 1. target doesn't exists (never copied)
        // 2. target exists but source has been modified recently

        if (!target.exists() || source.lastModified() > target.lastModified()) {
            InputStream in = new FileInputStream(source);
            copyFile(in, target);
        }
    }

    public static void copyFile(InputStream source, File target) throws IOException {
        final File parentFile = target.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        OutputStream out = new FileOutputStream(target);
        try {
            IOUtils.copy(source, out);
        } finally {
            try {
                source.close();
            } catch (Exception e) {
                //
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                //
            }
        }

    }

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
                InputStream in = new FileInputStream(source);
                copyFile(in, tarpath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO check that this method is not a duplicate of org.talend.utils.files.FileUtils#replaceInFile(String path,
    // String oldString, String newString)
    public static void replaceInFile(String regex, String fileName, String replacement) throws IOException {
        InputStream in = new FileInputStream(fileName);
        StringBuffer buffer = new StringBuffer();
        try {
            InputStreamReader inR = new InputStreamReader(in);
            BufferedReader buf = new BufferedReader(inR);
            String line;
            while ((line = buf.readLine()) != null) {
                buffer.append(line.replaceAll(regex, replacement)).append("\n");
            }
        } finally {
            in.close();
        }

        OutputStream os = new FileOutputStream(fileName);
        os.write(buffer.toString().getBytes());
        os.close();
    }

    public static FileFilter getExcludeSystemFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !isSVNFolder(pathname);
            }

        };
        return filter;
    }

    public static FileFilter getAcceptJARFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.toString().endsWith(".jar");
            }

        };
        return filter;
    }

    public static FileFilter getAcceptPMFilesFilter() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.toString().endsWith(".pm");
            }

        };
        return filter;
    }

    public static boolean createFolder(String path) {
        File folderPath = new File(path);
        return createFolder(folderPath);
    }

    public static boolean createFolder(File path) {
        if (!path.exists()) {
            return path.mkdir();
        }
        if (!path.isDirectory()) {
            return path.mkdir();
        }
        return true;
    }

    public static int getLastSeparatorLocation(String pathName) {
        int lastseparator = -1;
        if (pathName.contains("\\")) {
            lastseparator = pathName.lastIndexOf("\\");
        }
        if (pathName.contains("/")) {
            int temp = pathName.lastIndexOf("/");
            if (temp > lastseparator) {
                lastseparator = temp;
            }
        }
        return lastseparator;
    }

    /**
     * Load in a list all lines of the given file.
     *
     * @throws IOException
     */
    public static List<String> getContentLines(String filePath) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        List<String> lines = new ArrayList<>();
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
     * Create all the folders if they don't exist for the following path.
     *
     * @param path
     * @return the return code with a non null error message if the state is false. When the state is ok, there is no
     * message.
     */
    public static ReturnCode createFoldersIfNotExists(String path) {
        return createFoldersIfNotExists(path, false);
    }

    /**
     * .
     *
     * @param path
     * @param pathIsFilePath if true the given path has a filename at last segment so this segment is not processed
     * @return the return code with a non null error message if the state is false. When the state is ok, there is no
     * message.
     */
    public static ReturnCode createFoldersIfNotExists(String path, boolean pathIsFilePath) {
        ReturnCode rc = new ReturnCode();
        File filePath = new File(path);
        File fileFolder = filePath;
        if (pathIsFilePath) {
            fileFolder = filePath.getParentFile();
        }

        if (!fileFolder.exists()) {
            if (!fileFolder.mkdirs()) {
                rc.setOk(false);
                rc.setMessage("Failed to create the directory: " + fileFolder.getAbsolutePath());
            }
        }
        return rc;
    }

    /*
     * zip file with apache to avoid inappropriate encoding
     */

    public static void zipDirWithApache(String zipFileName, String inputFile) throws Exception {
        org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(new FileOutputStream(zipFileName));
        zipDirWithApache(out, new File(inputFile), "");
        out.close();
    }

    private static void zipDirWithApache(org.apache.tools.zip.ZipOutputStream out, File f, String base) throws Exception {
        String baseValue = base;
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(baseValue + "/"));
            baseValue = baseValue.length() == 0 ? "" : baseValue + "/";
            for (File element : fl) {
                zipDirWithApache(out, element, baseValue + element.getName());
            }
        } else {
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(baseValue));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
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

    public static String extractPathFolderFromFilePath(String filePath) {
        File completePath = new File(filePath);
        return completePath.getParent();
    }

    public static long getChecksumAlder32(File file) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            return getChecksumAlder32(bufferedInputStream);
        } catch (IOException e) {
            throw e;
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
    }

    public static long getChecksumAlder32(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return 0;
        }
        CheckedInputStream cis = null;
        try {
            // Compute Adler-32 checksum
            cis = new CheckedInputStream(inputStream, new Adler32());
            byte[] tempBuf = new byte[128];
            while (cis.read(tempBuf) >= 0) {
                // do nothing
            }
            return cis.getChecksum().getValue();
        } catch (IOException e) {
            throw e;
        } finally {
            if (cis != null) {
                cis.close();
            }
        }
    }

    public static String getFileMD5Hex(File inputFile) throws IOException {
        if (inputFile == null || !inputFile.exists()) {
            return null;
        }

        final FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            return getStreamMD5Hex(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static String getStreamMD5Hex(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        return DigestUtils.md5Hex(inputStream);
    }

    /**
     * DOC amaumont Comment method "getBytes".
     *
     * @param archiveFile
     * @throws IOException
     */
    public static byte[] getBytes(File archiveFile) throws IOException {
        long length = archiveFile.length();

        int lengthFinalSize = 0;

        if (length > Integer.MAX_VALUE) {
            throw new IllegalStateException("Capacity is over !");
        } else {
            lengthFinalSize = (int) length;
        }

        FileInputStream fis = new FileInputStream(archiveFile);
        int bufferSize = 1024;
        byte[] buf = new byte[bufferSize];
        int readBytes = 0;

        ByteArrayOutputStream bos = new ByteArrayOutputStream(lengthFinalSize);

        while ((readBytes = fis.read(buf)) != -1) {
            bos.write(buf, 0, readBytes);
        }

        return bos.toByteArray();
    }

    /**
     * DOC amaumont Comment method "getFile".
     *
     * @param jobScriptArchive
     * @throws IOException
     */
    public static File getFile(String filePath, byte[] jobScriptArchive) throws IOException {
        File file = new File(filePath);

        ByteArrayInputStream bis = new ByteArrayInputStream(jobScriptArchive, 0, jobScriptArchive.length);

        FileOutputStream fos = new FileOutputStream(file);

        int bufferSize = 1024;
        byte[] buf = new byte[bufferSize];

        int readBytes = 0;

        while ((readBytes = bis.read(buf)) != -1) {
            fos.write(buf, 0, readBytes);
        }

        bis.close();
        fos.close();

        return file;
    }

    private static void printFailure(ReturnCode rc) {
        if (!rc.isOk()) {
            System.err.println("Failure: " + rc.getMessage());
        }
    }

    /**
     * zip a new file with specified name to the user folder.
     *
     * @param sourceFileName
     * @param zippedFileName
     * @throws IOException
     */
    public static void zip(String sourceFileName, String zippedFileName) throws IOException {
        zip(new File(sourceFileName), zippedFileName, null);
    }

    /**
     * zip a new file with specified name to the user folder.
     *
     * @param sourceFileName
     * @param zippedFileName
     * @param fileFilter optional
     * @throws IOException
     */
    public static void zip(String sourceFileName, String zippedFileName, FileFilter fileFilter) throws IOException {
        zip(new File(sourceFileName), zippedFileName, fileFilter);
    }

    /**
     * zip the file to the user folder.
     *
     * @param sourceFile
     * @param zippedFileName
     * @throws IOException
     */
    public static void zip(File sourceFile, String zippedFileName) throws IOException {
        zip(sourceFile, zippedFileName, null);
    }

    /**
     * zip the file to the user folder.
     *
     * @param sourceFile
     * @param zippedFileName
     * @param fileFilter optional
     * @throws IOException
     */
    public static void zip(File sourceFile, String zippedFileName, FileFilter fileFilter) throws IOException {
        if (sourceFile.isDirectory()) {
            zips(sourceFile.listFiles(fileFilter), zippedFileName, fileFilter);
        } else {
            zips(new File[] { sourceFile }, zippedFileName, fileFilter);
        }
    }

    /**
     *
     * Method "jar".
     *
     * @param manifest
     * @param sourceDir
     * @param zip
     * @throws IOException
     */
    public static void jar(Manifest manifest, File sourceDir, File zip) throws IOException {
        jar(manifest, sourceDir, zip, null);
    }

    /**
     *
     * Method "jar".
     *
     * @param manifest
     * @param sourceDir
     * @param zip
     * @param fileFilter optional
     * @throws IOException
     */
    public static void jar(Manifest manifest, File sourceDir, File zip, FileFilter fileFilter) throws IOException {
        ZipOutputStream out = new JarOutputStream(new FileOutputStream(zip), manifest);
        try {
            zips(sourceDir.listFiles(fileFilter), out, fileFilter);
        } finally {
            out.close();
        }
    }

    /**
     *
     * DOC zshen Comment method "zips".
     *
     * @param sourceFile
     * @param zippedFileName
     * @param fileFilter optional
     * @throws IOException
     */
    public static void zips(File[] sourceFile, String zippedFileName) throws IOException {
        zips(sourceFile, zippedFileName, null);
    }

    /**
     *
     * DOC zshen Comment method "zips".
     *
     * @param sourceFile
     * @param zippedFileName
     * @param fileFilter optional
     * @throws IOException
     */
    public static void zips(File[] sourceFile, String zippedFileName, FileFilter fileFilter) throws IOException {
        OutputStream fos = new FileOutputStream(zippedFileName);
        ZipOutputStream out = new ZipOutputStream(fos);
        try {
            zips(sourceFile, out, fileFilter);
        } finally {
            // http://stackoverflow.com/questions/4681459/closing-zipoutputstream
            if (sourceFile.length > 0) {
                out.close();
            } else {
                fos.close();
            }
        }
    }

    private static void zips(File[] sourceFile, ZipOutputStream out, FileFilter fileFilter) throws IOException {
        for (File theFile : sourceFile) {
            zips(out, theFile, "", fileFilter); //$NON-NLS-1$
        }
    }

    /**
     *
     * DOC zshen Comment method "zip".
     *
     * @param out
     * @param f
     * @param base
     * @param fileFilter optional
     * @throws IOException
     */
    private static void zips(ZipOutputStream out, File f, String base, FileFilter fileFilter) throws IOException {
        if (f.isDirectory()) {
            base += f.getName() + '/';
            out.putNextEntry(new ZipEntry(base));
            for (File element : f.listFiles(fileFilter)) {
                zips(out, element, base, fileFilter);
            }
        } else {
            out.putNextEntry(new ZipEntry(base + f.getName()));
            InputStream in = new FileInputStream(f);

            byte[] b = new byte[BUFFER_SIZE];
            int readBytes = 0;
            while ((readBytes = in.read(b, 0, BUFFER_SIZE)) != -1) {
                out.write(b, 0, readBytes);
            }
            in.close();
            out.flush();
        }
    }

    public static void zipFiles(String source, String target) throws Exception {
        zip(source, target);
    }

    public static void zipFolderRecursion(String sourceFileName, String zippedFileName) throws IOException {
        zip(sourceFileName, zippedFileName, null);
    }

    public static void zipFolderRecursion(String sourceFileName, String zippedFileName, FileFilter fileFilter) throws Exception {
        zip(sourceFileName, zippedFileName, fileFilter);
    }

    public static void unzip(String zipFile, String targetFolder, String... fileSuffixes) throws Exception {
        unzip(zipFile, targetFolder, true, fileSuffixes);
    }

    /**
     * Unzip the component file to the user folder.
     *
     * @param zipFile The component zip file
     * @param targetFolder The user folder
     * @param overwrite if overwrite existing files
     * @param fileSuffixes Case-insensitive Suffixes , if these parameter are set, only the files named with these
     * suffixes will be extracted
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static void unzip(String zipFile, String targetFolder, boolean overwrite, String... fileSuffixes) throws Exception {
        Exception exception = null;
        ZipFile zip = new ZipFile(zipFile);
        byte[] buf = new byte[8192];

        try {
            Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zip.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();

                File file = new File(targetFolder, entry.getName());
                validateDestPath(targetFolder, file.getPath());
                boolean exist = file.exists();
                if (entry.isDirectory()) {
                    if (!exist) {
                        file.mkdir();
                    }
                } else {
                    if (!overwrite && exist) {
                        continue;
                    }
                    if (fileSuffixes.length > 0 && !isReservedFile(file.getName(), fileSuffixes)) {
                        continue;
                    }
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
     * Validates whether destPathStr is inside targetDir or not.
     * 
     * @param targetDir target directory
     * @param destPathStr destination file path
     */
    public static void validateDestPath(String targetDir, String destPathStr) throws IOException {
        if (targetDir != null && !targetDir.isEmpty() && destPathStr != null && !destPathStr.isEmpty()) {
            Path targetPathNormalized = Paths.get(new File(targetDir).toPath().toString()).normalize();
            Path destPathNormalized = Paths.get(new File(destPathStr).toPath().toString()).normalize();
            if (!destPathNormalized.startsWith(targetPathNormalized)) {
                throw new IOException("Invalid output path: " + destPathStr);
            }
        }
    }

    private static boolean isReservedFile(String name, String[] fileSuffixes) {
        if (name != null) {
            String checkedName = name.toLowerCase();
            for (String element : fileSuffixes) {
                if (element.equals(checkedName) || checkedName.endsWith(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** fullPath,eg: "D:\\ALL_integrate_patch_v4.2.2.r63143-20110722.zip **/
    public static void downloadFileFromWeb(URL resourceURL, String fullPath) {
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
     *
     * Delete the sub folders, if true, will delete current folder also.
     */
    public static void deleteFolder(File file, boolean withCurrentFolder) {
        if (file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            for (File file2 : files) {
                deleteFile(file2, true);
            }
            if (withCurrentFolder) {
                file.delete();
            }
        }
    }

    /**
     * check multi-files in same folder.
     *
     * The base file must be existed first, then, will check the files in this folder (if base file is directory) or
     * with same folder (if the base file is file).
     *
     */
    public static boolean allInSameFolder(File baseFile, String... fileNames) {
        if (baseFile != null && baseFile.exists()) {
            if (fileNames == null || fileNames.length == 0) { // only check base file
                return true;
            }
            File baseFoler = baseFile;
            if (baseFile.isFile()) {
                baseFoler = baseFile.getParentFile();
            }

            for (String fileName : fileNames) {
                if (fileName != null) {
                    File subFile = new File(baseFoler, fileName);
                    if (!subFile.exists()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
