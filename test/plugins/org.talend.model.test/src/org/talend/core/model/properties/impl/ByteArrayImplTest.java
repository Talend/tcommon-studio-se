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
package org.talend.core.model.properties.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ByteArrayImplTest {

    static final String PATH_FILEITEM = "resources/fileitem";

    /**
     * TUP-17046 for Routines item
     */
    static final String FILE_ISO_8859_15 = PATH_FILEITEM + '/' + "ISO_8859_15_0.1.item";

    static final String FILE_UTF_8 = PATH_FILEITEM + '/' + "UTF_8_0.1.item";

    private File workingFolder;

    @Before
    public void createWorkFolder() {
        workingFolder = FileUtils.createTmpFolder(ByteArrayImplTest.class.getSimpleName(), "");
    }

    @After
    public void clean() {
        if (workingFolder != null) {
            FilesUtils.deleteFolder(workingFolder, true);
        }
    }

    private File getBundleFile(IPath path) throws IOException {
        final URL url = FileLocator.find(Platform.getBundle("org.talend.model.test"), path, null);
        return new File(FileLocator.toFileURL(url).getPath());
    }

    private String getCRC32(File file) throws Exception {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = null;
        CheckedInputStream checkedinputstream = null;
        String crc = null;
        try {
            fileinputstream = new FileInputStream(file);
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
            while (checkedinputstream.read() != -1) {
            }
            crc = Long.toHexString(crc32.getValue()).toUpperCase();
        } finally {
            if (fileinputstream != null) {
                fileinputstream.close();
            }
            if (checkedinputstream != null) {
                checkedinputstream.close();
            }
        }
        return crc;
    }

    private String guessEncoding(File file) throws IOException {
        Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
        return guessedCharset.displayName();
    }

    // @Test
    // public void test_DifferentEncoding_SameContent() throws Exception {
    // final File iso8859File = getBundleFile(new Path(FILE_ISO_8859_15));
    // Assert.assertNotNull(iso8859File);
    // Assert.assertTrue(iso8859File.exists());
    //
    // final File utf8File = getBundleFile(new Path(FILE_UTF_8));
    // Assert.assertNotNull(utf8File);
    // Assert.assertTrue(utf8File.exists());
    //
    // final String iso8859Crc32 = getCRC32(iso8859File);
    // final String utf8Crc32 = getCRC32(utf8File);
    // Assert.assertNotEquals("The file content is different", iso8859Crc32, utf8Crc32);
    // }

    @Test
    public void test_setInnerContent_ISO_8859_15() throws Exception {
        do_setInnerContent(FILE_ISO_8859_15);
    }

    @Test
    public void test_setInnerContent_UTF_8() throws Exception {
        do_setInnerContent(FILE_UTF_8);
    }

    private void do_setInnerContent(String filePath) throws Exception {
        final Path path = new Path(filePath);
        final File bundleFile = getBundleFile(path);
        Assert.assertNotNull(bundleFile);
        Assert.assertTrue(bundleFile.exists());

        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(bundleFile);

        final File newFile = new File(workingFolder, path.lastSegment());
        byteArray.setInnerContentToFile(newFile);
        Assert.assertTrue(newFile.exists());

        final String originalCrc32 = getCRC32(bundleFile);
        final String newCrc32 = getCRC32(newFile);
        Assert.assertEquals("The file content is different", originalCrc32, newCrc32);
    }

    @Test
    public void test_encoding_ISO_8859_15() throws Exception {
        do_same_encoding(FILE_ISO_8859_15, "ISO-8859-15");
    }

    @Test
    public void test_encoding_UTF_8() throws Exception {
        do_same_encoding(FILE_UTF_8, "UTF-8");
    }

    private void do_same_encoding(String filePath, String encoding) throws Exception {
        final Path path = new Path(filePath);
        final File bundleFile = getBundleFile(path);
        Assert.assertNotNull(bundleFile);
        Assert.assertTrue(bundleFile.exists());

        // final String guessEncoding = guessEncoding(bundleFile);
        // Assert.assertEquals(encoding, guessEncoding); //don't support all charset

        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(bundleFile);

        byte[] innerContent = byteArray.getInnerContent();
        String str = new String(innerContent, encoding);
        Assert.assertTrue(str.contains("§"));

        final File newFile = new File(workingFolder, path.lastSegment());
        PrintWriter pw = new PrintWriter(newFile, encoding);
        pw.print(str);
        pw.close();
        final String originalCrc32 = getCRC32(bundleFile);
        final String newCrc32 = getCRC32(newFile);
        Assert.assertEquals("The file content is different", originalCrc32, newCrc32);
    }

    @Test
    public void test_ISO_8859_15_to_UTF_8() throws Exception {
        final Path path = new Path(FILE_ISO_8859_15);
        final File bundleFile = getBundleFile(path);
        Assert.assertNotNull(bundleFile);
        Assert.assertTrue(bundleFile.exists());
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(bundleFile);

        byte[] innerContent = byteArray.getInnerContent();
        String str = new String(innerContent, "ISO-8859-15");

        final File newFile = new File(workingFolder, "convertUTF8" + '.' + path.getFileExtension());
        final byte[] bytes = str.getBytes("UTF-8"); // convert to another encoding.
        byteArray.setInnerContent(bytes);
        byteArray.setInnerContentToFile(newFile);

        final String convertUTF8Crc32 = getCRC32(newFile);
        final File utf8File = getBundleFile(new Path(FILE_UTF_8));
        Assert.assertNotNull(utf8File);
        Assert.assertTrue(utf8File.exists());
        final String utf8Crc32 = getCRC32(utf8File);

        Assert.assertEquals("The file content is different", convertUTF8Crc32, utf8Crc32);
    }

}
