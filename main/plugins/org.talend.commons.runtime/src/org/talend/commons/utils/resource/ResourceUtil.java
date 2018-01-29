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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.URIUtil;

/**
 * class ResourceUtil. Helper to handle resource URL from java or Eclipse.
 */
public class ResourceUtil {

    private static final String BUNDLERESOURCE = "bundleresource"; //$NON-NLS-1$

    private static final String BUNDLEENTRY = "bundleentry"; //$NON-NLS-1$

    public static File convertResourceToFile(URL resource) throws IOException, URISyntaxException {
        File fileDir = null;
        if (BUNDLERESOURCE.equals(resource.getProtocol()) || BUNDLEENTRY.equals(resource.getProtocol())) {
            URL unescapedURL = FileLocator.toFileURL(resource);
            URI escapedURI = new URI(unescapedURL.getProtocol(), unescapedURL.getPath(), unescapedURL.getQuery());
            fileDir = URIUtil.toFile(escapedURI);
        } else {
            fileDir = new File(resource.getFile());
        }
        return fileDir;
    }

    /**
     * Method "getFileFromResource" returns a File from the given path relative to the class. When the path starts with
     * "/", the file can be located in a plugin (in the classpath of the plugin). This method must be used in plugin
     * mode. It will return null when it's called as a java application.
     * 
     * @param clazz a class in the same plugin as the file to find
     * @param path the path of the file.
     * @return the file when found. Can return null. Otherwise will throw an exception
     * @throws IOException
     * @throws URISyntaxException
     */
    public static File getFileFromResource(Class<?> clazz, String path) throws IOException, URISyntaxException {
        URL url = clazz.getResource(path);
        if (url == null) {
            return null;
        }
        URL fileURL = FileLocator.toFileURL(url);
        URI escapedUri = new URI(fileURL.getProtocol(), fileURL.getPath(), fileURL.getQuery());
        return new File(escapedUri);
    }
}
