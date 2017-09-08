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
package org.talend.designer.maven.model;

import org.eclipse.m2e.core.internal.IMavenConstants;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
@SuppressWarnings("nls")
public interface TalendMavenConstants {

    static final String POM_VERSION = "4.0.0";

    /* same as MavenArtifactComponent.JAR */
    static final String PACKAGING_JAR = "jar";

    /* same as MavenArtifactComponent.POM */
    static final String PACKAGING_POM = "pom";

    static final String POM_NAME = "pom";

    static final String XML_EXT = ".xml";

    static final String POM_FILE_NAME = IMavenConstants.POM_FILE_NAME; // POM_NAME+XML_EXT

    static final String ASSEMBLY_NAME = "assembly";

    static final String ASSEMBLY_FILE_NAME = IProjectSettingTemplateConstants.ASSEMBLY_FILE_NAME;

    /**
     * 
     */
    static final String PREFIX_ARG = "-D";

    static final String PREFIX_PROFILE = "-P";

    static final String PREFIX_THREAD = "-T";

    static final String PREFIX_THREAD_VALUE = "1C";

    /**
     * goal
     */
    static final String GOAL_CLEAN = "clean";

    static final String GOAL_COMPILE = "compile";

    static final String GOAL_TEST = "test";

    static final String GOAL_TEST_COMPILE = "test-compile";

    static final String GOAL_REFRESH = "refresh"; // custom one for talend use, needed to do a refresh of artifacts

    static final String GOAL_PACKAGE = "package";

    static final String GOAL_INSTALL = "install";

    /**
     * Profiles
     */
    static final String PROFILE_DEFAULT_SETTING = "default-settings";

    static final String PROFILE_INCLUDE_JAVA_SOURCES = "include-java-sources";

    static final String PROFILE_INCLUDE_MAVEN_RESOURCES = "include-maven-resources";

    static final String PROFILE_INCLUDE_ITEMS = "include-items";

    static final String PROFILE_INCLUDE_LIBS = "include-libs";

    static final String PROFILE_INCLUDE_BINARIES = "include-binaries";

    static final String PROFILE_INCLUDE_LOG4J = "include-log4j";

    static final String PROFILE_INCLUDE_RUNNING_LOG4J = "include-running-log4j";

    static final String PROFILE_INCLUDE_CONTEXTS = "include-contexts";

    static final String PROFILE_INCLUDE_TEST_SOURCES = "include-test-sources";

    static final String PROFILE_INCLUDE_TEST_REPORTS = "include-test-reports";

    static final String PROFILE_INCLUDE_XMLMAPPINGS = "include-xmlMappings";

    static final String PROFILE_INCLUDE_RUNNING_XMLMAPPINGS = "include-running-xmlMappings";

    static final String PROFILE_INCLUDE_RULES = "include-rules";

    static final String PROFILE_INCLUDE_PIGUDFS_JAVA_SOURCES = "include-pigudfs-java-sources";

    static final String PROFILE_INCLUDE_PIGUDFS_BINARIES = "include-pigudfs-binaries";

    static final String PROFILE_PACKAGING_AND_ASSEMBLY = "packaging-and-assembly";

    static final String PROFILE_INCLUDE_MICROSERVICE_CONFIGS = "include-microservice-configs";

    static final String PROFILE_INCLUDE_MICROSERVICE_RUNNING_CONFIGS = "include-microservice-running-configs";

    static final String PROFILE_INCLUDE_SURVIVORSHIP_RULES = "include-survivorship-rules";

    /**
     * argments
     */
    static final String ARG_SKIPTESTS = "-DskipTests";

    /* same as the JavaUtils.JAVA_PROJECT_NAME */
    static final String PROJECT_NAME = ".Java";

    static final String CURRENT_PATH = ".";

    static final String DEFAULT_ENCODING = "UTF-8";

    static final String DEFAULT_GROUP_ID = "org.talend";

    static final String DEFAULT_VERSION = "6.0.0";

    static final String DEFAULT_CODE = "code";

    static final String DEFAULT_BEAN = "bean";

    static final String DEFAULT_PIGUDF = "pigudf";

    static final String DEFAULT_JOB = "job";

    static final String DEFAULT_TEST = "test";

    static final String DEFAULT_MASTER = "master";

    /*
     * for .Java/pom.xml
     */

    // static final String DEFAULT_CODE_PROJECT_GROUP_ID = DEFAULT_GROUP_ID + '.' + DEFAULT_MASTER;

    static final String DEFAULT_CODE_PROJECT_ARTIFACT_ID = DEFAULT_CODE + ".Master";

    /*
     * for routines
     */
    // static final String DEFAULT_ROUTINES_GROUP_ID = DEFAULT_GROUP_ID + '.' + DEFAULT_CODE;

    static final String DEFAULT_ROUTINES_ARTIFACT_ID = JavaUtils.ROUTINE_JAR_NAME;

    static final String DEFAULT_BEANS_ARTIFACT_ID = JavaUtils.BEANS_JAR_NAME;

    static final String DEFAULT_PIGUDFS_ARTIFACT_ID = JavaUtils.PIGUDFS_JAR_NAME;

    /*
     * for job
     */
    // static final String DEFAULT_JOB_GROUP_ID = DEFAULT_GROUP_ID + '.' + DEFAULT_JOB;

    /*
     * for test container
     */
    static final String DEFAULT_JUNIT_ARTIFACT_GROUP = "junit";

    static final String DEFAULT_JUNIT_ARTIFACT_ID = "junit";

    static final String DEFAULT_JUNIT_ARTIFACT_VERSION = "3.8.1";

    static final String DEFAULT_JUNIT_ARTIFACT_SCOPE = "test";

}
