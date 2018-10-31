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
package org.talend.designer.maven.model;

/**
 * created by ggu on 22 Jan 2015 Detailled comment
 *
 */
@SuppressWarnings("nls")
public class MavenSystemFolders {

    public static final ProjectSystemFolder JAVA = new ProjectSystemFolder("src/main/java", "target/classes");

    public static final ProjectSystemFolder JAVA_TEST = new ProjectSystemFolder("src/test/java", "target/test-classes");

    public static final ProjectSystemFolder RESOURCES = new ProjectSystemFolder("src/main/resources", "target/classes");

    public static final ProjectSystemFolder EXT_RESOURCES = new ProjectSystemFolder("src/main/ext-resources", "target/classes");
    
    public static final ProjectSystemFolder BUNDLE_RESOURCES = new ProjectSystemFolder("src/main/bundle-resources", "target/classes");

    public static final ProjectSystemFolder RESOURCES_TEST = new ProjectSystemFolder("src/test/resources", "target/test-classes");

    public static final ProjectSystemFolder ASSEMBLIES = new ProjectSystemFolder("src/main/assemblies");

    public static final ProjectSystemFolder TEMPLATES = new ProjectSystemFolder("src/main/templates");

    public static final ProjectSystemFolder ITEMS = new ProjectSystemFolder("src/main/items");

    public static final ProjectSystemFolder TEST_REPORTS = new ProjectSystemFolder("tests");

    public static final ProjectSystemFolder TARGET = new ProjectSystemFolder("target");

    public static final ProjectSystemFolder TEMP = new ProjectSystemFolder("temp");

    public static final ProjectSystemFolder[] SIMPLE_DIRS = { JAVA, RESOURCES };

    public static final ProjectSystemFolder[] TEST_DIRS = { JAVA_TEST, RESOURCES_TEST };

    public static final ProjectSystemFolder[] ALL_DIRS = { JAVA, JAVA_TEST, RESOURCES, RESOURCES_TEST };

    public static final ProjectSystemFolder[] ALL_DIRS_EXT = { JAVA, JAVA_TEST, RESOURCES, RESOURCES_TEST, EXT_RESOURCES };
}
