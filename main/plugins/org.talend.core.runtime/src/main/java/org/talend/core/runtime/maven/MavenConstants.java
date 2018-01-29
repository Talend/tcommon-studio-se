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
package org.talend.core.runtime.maven;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public interface MavenConstants {

    static final String NAME_GROUP_ID = "GROUP_ID";

    static final String NAME_USER_VERSION = "USER_VERSION";
    
    static final String NAME_PUBLISH_AS_SNAPSHOT = "PUBLISH_AS_SNAPSHOT";

    static final String PROJECT_VERSION = "PROJECT_VERSION";

    static final String PROJECT_GROUPID = "PROJECT_GROUPID";

    static final String APPEND_FOLDER_TO_GROUPID = "APPEND_FOLDER_TO_GROUPID";

    static final String TYPE_JAR = "jar";

    static final String DEFAULT_GROUP_ID = "org.talend";

    static final String DEFAULT_VERSION = "6.0.0";

    static final String SNAPSHOT = "-SNAPSHOT";

    /*
     * for lib
     */
    static final String DEFAULT_LIB_GROUP_ID = DEFAULT_GROUP_ID + ".libraries";

    static final String DEFAULT_LIB_VERSION = "6.0.0";

    /*
     * for index
     */

    public static final String INDEX_GROUP_ID = "org.talend.libraries_index";

    public static final String INDEX_ARTIFACT_ID = "libraries_index";

    public static final String INDEX_PACKAGE = "zip";

    public static final String DOWNLOAD_MANUAL = "manual";

    static final String LOCAL_RESOLUTION_URL = "localrepositories://@id=talend.local";

    static final String PACKAGING_JAR = "jar";

    static final String PACKAGING_POM = "pom";

}
