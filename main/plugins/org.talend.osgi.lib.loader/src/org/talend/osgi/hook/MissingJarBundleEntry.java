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
package org.talend.osgi.hook;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.osgi.storage.bundlefile.BundleEntry;

/**
 * This class is created to tell the wrappper Factory that we have a missing Jar entry and that is should create a
 * MissingJarBundleFile.
 *
 */
class MissingJarBundleEntry extends BundleEntry {

    public final String path;

    /**
     * DOC sgandon MissingJarBundleEntry constructor comment.
     * 
     * @param path
     */
    public MissingJarBundleEntry(String path) {
        this.path = path;
    }

    // all the following method shall never be used.
    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public String getName() {
        // we return a final slash on purpose so that
        // org.eclipse.osgi.internal.loader.classpath.ClasspathManager.getClasspath(String, Generation)
        // will be able to create a BundleFile
        return path + "/"; //$NON-NLS-1$
    }

    @Override
    public URL getLocalURL() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public URL getFileURL() {
        try {
            return new URL( "file:" + path ); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            // should never happend so throw a runtime in case it does
            throw new RuntimeException( e );
        }
    }

}
