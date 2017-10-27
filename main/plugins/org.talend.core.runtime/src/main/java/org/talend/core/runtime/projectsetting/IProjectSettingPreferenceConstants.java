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
package org.talend.core.runtime.projectsetting;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public interface IProjectSettingPreferenceConstants {

    public static final String TEMPLATE_JOB_INFO = "template_job_info_script";

    public static final String TEMPLATE_SH = "template_sh_script";

    public static final String TEMPLATE_BAT = "template_bat_script";

    /*
     * project
     */
    public static final String TEMPLATE_PROJECT_POM = "template_project_pom_maven_script";

    /*
     * Standalone Job
     */
    public static final String TEMPLATE_STANDALONE_JOB_POM = "template_standalonejob_pom_maven_script";

    public static final String TEMPLATE_STANDALONE_JOB_ASSEMBLY = "template_standalonejob_assembly_maven_script";

    /*
     * OSGi Bundle
     */
    public static final String TEMPLATE_OSGI_BUNDLE_POM = "template_osgi_bundle_pom_maven_script";

    /*
     * route karaf
     */
    public static final String TEMPLATE_ROUTES_KARAF_POM = "template_routes_karaf_pom_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_ROUTES_KARAF_BUNDLE = "template_routes_karaf_bundle_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_ROUTES_KARAF_FEATURE = "template_routes_karaf_feature_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_ROUTES_KARAF_PARENT = "template_routes_karaf_parent_maven_script"; //$NON-NLS-1$

    /*
     * services karaf
     */
    public static final String TEMPLATE_SERVICES_KARAF_POM = "template_services_karaf_pom_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_SERVICES_KARAF_BUNDLE = "template_services_karaf_bundle_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_SERVICES_KARAF_FEATURE = "template_services_karaf_feature_maven_script"; //$NON-NLS-1$

    public static final String TEMPLATE_SERVICES_KARAF_PARENT = "template_services_karaf_parent_maven_script"; //$NON-NLS-1$

    /*
     * Enable ODBC or not, used for DatabaseForm and stats&logs
     */
    public static final String METADATA_DBCONNECTION_ODBC_ENABLE = "metadata.dbconnection.odbc.enable"; //$NON-NLS-1$
}
