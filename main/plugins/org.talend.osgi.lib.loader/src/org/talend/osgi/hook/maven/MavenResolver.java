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
package org.talend.osgi.hook.maven;

import java.io.File;
import java.io.IOException;

/**
 * this interface is used by this OSGI extension fragment to get the maven service to resolve maven URI to local files.
 * We use a service because OSGI extension fragment cannot import package nor require bundles. And we need to use the
 * pax-url API to resolve the maven URI
 *
 */
public interface MavenResolver {

    /**
     * return the file location of the artifact pointed by the maven URI.
     * 
     * @param mavenURI, the URI to locate the artifact.
     * @return the local file that was resolved (found or downloaded) identified by the maven uri.
     * @throws IOException
     */
    public File resolve(String mavenURI) throws IOException;
}
