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
package org.talend.metadata.managment.ui.wizard.documentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.runtime.IPath;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkType;
import org.talend.core.repository.constants.FileConstants;

/**
 * ggu class global comment. Detailled comment
 */
public final class LinkUtils {

    public static final String DOT = "."; //$NON-NLS-1$

    public static final String NETWORK_PAGE_PATTERN = "(html|htm|php|asp|jsp|shtml)"; //$NON-NLS-1$

    public static final String COMPRESSION_FILE_PATTERN = "(rar|zip|jar|arj|arc|cab)|(tar|Z|tgz|gz|bz2|bz|deb|rpm|lha)"; //$NON-NLS-1$

    public static final String EXEC_FILE_PATTERN = "(exe|com)"; //$NON-NLS-1$

    /*
     * match "/file.html" or "/file"
     */
    public static final String NET_FILE_PATTERN = "(/\\w+(\\.(" + NETWORK_PAGE_PATTERN + "|" + COMPRESSION_FILE_PATTERN + "|" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            + EXEC_FILE_PATTERN + "))?)$"; //$NON-NLS-1$

    /*
     * such as: "http://www.talend.com/..." or "http://193.189.143.143/..."
     */
    public static final String HTTP_PATTERN = "^(http://([\\w-]+\\.)+[\\w-]+)(/\\S*)?"; //$NON-NLS-1$

    /**
     * 
     */
    public enum LinkInfo {
        FILE_NOT_FOUND,
        URL_ERROR,
        NET_ERROR,
        LINK_OK,
    }

    /**
     * 
     * ggu Comment method "existedLink".
     * 
     * the link is valid or not.
     */
    public static boolean validateLink(LinkType link) {
        if (link == null) {
            return false;
        }
        String uri = link.getURI();
        if (isRemoteFile(uri)) {
            if (testRemoteFile(uri) == LinkInfo.LINK_OK) {
                return true;
            }
        } else if (existedFile(uri)) {
            return true;
        }

        return false;
    }

    /**
     * 
     * ggu Comment method "isFile".
     * 
     * check the file
     */
    public static boolean isFile(final String file) {
        if (file == null) {
            return false;
        }
        if (isFile(new File(file.trim()))) {
            return true;
        }
        return false;
    }

    public static boolean isFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.isFile()) {
            return true;
        }
        return false;
    }

    public static boolean isFile(final IPath path) {
        if (path == null) {
            return false;
        }
        if (isFile(path.toFile())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "existedFile".
     * 
     * the file is existed or not.
     */
    public static boolean existedFile(final File file) {
        if (!isFile(file)) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean existedFile(final String file) {
        if (file == null) {
            return false;
        }
        if (existedFile(new File(file.trim()))) {
            return true;
        }
        return false;
    }

    public static boolean existedFile(final IPath path) {
        if (path == null) {
            return false;
        }
        if (existedFile(path.toFile())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "isPropertyFile".
     * 
     * check the file is property file or not.
     */
    public static boolean isPropertyFile(IPath path) {
        if (!isFile(path)) {
            return false;
        }
        return FileConstants.PROPERTIES_EXTENSION.equals(path.getFileExtension());
    }

    public static boolean isPropertyFile(File file) {
        if (!isFile(file)) {
            return false;
        }
        return file.getAbsolutePath().endsWith(FileConstants.PROPERTIES_EXTENSION);
    }

    /**
     * 
     * ggu Comment method "isItemFile".
     * 
     * check the file is item file or not.
     */
    public static boolean isItemFile(IPath path) {
        if (!isFile(path)) {
            return false;
        }
        return FileConstants.ITEM_EXTENSION.equals(path.getFileExtension());
    }

    public static boolean isItemFile(File file) {
        if (!isFile(file)) {
            return false;
        }
        return file.getAbsolutePath().endsWith(FileConstants.ITEM_EXTENSION);
    }

    /**
     * 
     * ggu Comment method "isLinkDocumentationItem".
     * 
     */
    public static boolean isLinkDocumentationItem(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof LinkDocumentationItem) {
            return true;
        }
        return false;
    }

    public static boolean isDocumentationItem(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof DocumentationItem) {
            return true;
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "getItemPath".
     * 
     * get the related Item file.
     */
    public static IPath getItemPath(IPath path) {
        if (!isFile(path)) {
            return null;
        }
        return path.removeFileExtension().addFileExtension(FileConstants.ITEM_EXTENSION);
    }

    /**
     * 
     * ggu Comment method "getPropertiesPath".
     * 
     * get the related property file.
     */
    public static IPath getPropertyPath(IPath path) {
        if (!isFile(path)) {
            return null;
        }
        return path.removeFileExtension().addFileExtension(FileConstants.PROPERTIES_EXTENSION);
    }

    public static boolean isRemoteFile(final String remoteFile) {
        if (remoteFile == null) {
            return false;
        }
        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;

        try {
            pattern = compiler.compile(HTTP_PATTERN);
            if (matcher.contains(remoteFile, pattern)) {
                return true;
            }
        } catch (MalformedPatternException e) {
            return false;
        }
        return false;

    }

    public static LinkInfo testRemoteFile(String uri) {
        if (uri == null) {
            return LinkInfo.URL_ERROR;
        }
        uri = uri.trim();
        if (!isRemoteFile(uri)) {
            return LinkInfo.URL_ERROR;
        }
        try {
            URL url = new URL(uri);
            return testRemoteFile(url);
        } catch (MalformedURLException e) {
            return LinkInfo.URL_ERROR;
        }
    }

    public static LinkInfo testRemoteFile(final URL url) {
        if (url == null) {
            return LinkInfo.URL_ERROR;
        }
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.connect();
            httpConn.getContent();
            // if (!httpConn.getURL().equals(url)) {
            // return LinkInfo.FILE_NOT_FOUND;
            // }
        } catch (FileNotFoundException e) {
            return LinkInfo.FILE_NOT_FOUND;
        } catch (IOException e) {
            return LinkInfo.NET_ERROR;
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return LinkInfo.LINK_OK;
    }

    /**
     * 
     * ggu Comment method "checkLinkFile".
     * 
     * @param uri
     * @return
     */
    public static boolean checkLinkFile(final String uri) {
        if (uri == null) {
            return false;
        }
        if (!isRemoteFile(uri)) {
            return false;
        }
        try {

            URL url = new URL(uri);
            final String file = url.getFile();
            if (file != null) {
                if (file.equals(url.getPath())) {
                    // match "http://xx.xx.xx"
                    if ("".equals(file)) { //$NON-NLS-1$
                        return true;
                    }
                    // match "http://xx.xx.xx/" or "http://xx.xx.xx/yy/"
                    if (file.endsWith("/")) { //$NON-NLS-1$
                        return true;
                    }
                    Perl5Matcher matcher = new Perl5Matcher();
                    Perl5Compiler compiler = new Perl5Compiler();
                    Pattern pattern;

                    pattern = compiler.compile(NET_FILE_PATTERN);
                    // match "http://xx.xx.xx/yy/zz" or "http://xx.xx.xx/yy/zz/abc.htm"
                    if (matcher.contains(file, pattern)) {
                        return true;
                    }
                } else { // match the "http://xxxx.xx/a.php?a=b"
                    return true;
                }
            }
        } catch (MalformedPatternException e) {
            //
        } catch (MalformedURLException e) {
            //
        }
        return false;
    }
}
