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
package org.talend.core.prefs;

/**
 * Core preferences. Detailled comment <br/>
 * 
 * $Id: ITalendCorePrefConstants.java 46402 2010-08-06 08:19:31Z wzhang $
 * 
 */
public interface ITalendCorePrefConstants {

    /** Path to the directory of temporary files. */
    public static final String FILE_PATH_TEMP = "filePathTemp"; //$NON-NLS-1$

    /** Perl interpreter. */
    public static final String PERL_INTERPRETER = "perlInterpreter"; //$NON-NLS-1$

    /** Java interpreter. */
    public static final String JAVA_INTERPRETER = "javaInterpreter"; //$NON-NLS-1$

    /** Lanugage selector. */
    public static final String LANGUAGE_SELECTOR = "languageSelector"; //$NON-NLS-1$

    /** Perl library directory. */
    // public static final String PERL_LIB = "perlLib";
    /** Known users list. */
    public static final String USERS = "users"; //$NON-NLS-1$

    public static final String CONNECTIONS = "connections"; //$NON-NLS-1$

    /** Last used connection. */
    public static final String LAST_USED_CONNECTION = "lastUSedConnection"; //$NON-NLS-1$

    /** Last used project. */
    public static final String LAST_USED_PROJECT = "lastUSedProject"; //$NON-NLS-1$

    public static final String LAST_USED_SVN_BRANCH = "lastUSedSVNBranch"; //$NON-NLS-1$

    /** Last used user. */
    public static final String LAST_USED_USER = "lastUSedUser"; //$NON-NLS-1$

    /** Preview Limit. */
    public static final String PREVIEW_LIMIT = "previewLimit"; //$NON-NLS-1$

    public static final String WORKSPACE_TASKS_DONE = "workspaceTasksDone"; //$NON-NLS-1$

    public static final String RUN_IN_MULTI_THREAD = "runInMultiThread"; //$NON-NLS-1$

    public static final String ALWAYS_WELCOME = "alwaysWelcome"; //$NON-NLS-1$

    public static final String CONTEXT_GROUP_BY_SOURCE = "groupBySource"; //$NON-NLS-1$

    public static final String SQL_ADD_QUOTE = "addSqlQuote"; //$NON-NLS-1$

    public static final String SQL_ADD_WARNING = "addSqlWarning"; //$NON-NLS-1$

    public static final String AS400_SQL_SEG = "AS400Sqlseg"; //$NON-NLS-1$

    public static final String DOC_GENERATION = "doc_generation"; //$NON-NLS-1$

    public static final String IREPORT_PATH = "ireport_path"; //$NON-NLS-1$

    public static final String DOC_HIDEPASSWORDS = "doc_hidepasswords"; //$NON-NLS-1$

    public static final String DOC_GENERATESOURCECODE = "doc_generatesourcecode"; //$NON-NLS-1$

    public static final String DOC_GENERATE_LASTEST_VERSION = "doc_generatelastestversion"; //$NON-NLS-1$

    public static final String LINE_SEPERATOR_STR = "lineSeperator"; //$NON-NLS-1$

    public static final String DEACTIVE_REPOSITORY_UPDATE = "DEACTIVE_REPOSITORY_UPDATE"; //$NON-NLS-1$

    public static final String DB_CONNECTION_TIMEOUT_ACTIVED = "db_conn_timeout_actived"; //$NON-NLS-1$
    
    public static final String DB_CONNECTION_TIMEOUT = "db_conn_timeout"; //$NON-NLS-1$

    public static final String HBASE_OR_MAPRDB_SCAN_LIMIT = "hbase_or_maprdb_scan_limit"; //$NON-NLS-1$

    public static final String DOC_USER_LOGO = "doc_user_logo"; //$NON-NLS-1$

    public static final String DOC_COMPANY_NAME = "company_name"; //$NON-NLS-1$

    public static final String ADD_SYSTEM_ROUTINES = "add_system_routines"; //$NON-NLS-1$

    public static final String ADD_USER_ROUTINES = "add_user_routines"; //$NON-NLS-1$

    public static final String USE_CSS_TEMPLATE = "use_css_template"; //$NON-NLS-1$

    public static final String CSS_FILE_PATH = "css_file_path"; //$NON-NLS-1$

    public static final String FORBIDDEN_MAPPING_LENGTH_PREC_LOGIC = "forbidden_mapping_length_prec_logic"; //$NON-NLS-1$

    public static final String DATA_COLLECTOR_ENABLED = "active_data_collector"; //$NON-NLS-1$

    public static final String DATA_COLLECTOR_UPLOAD_PERIOD = "active_data_collector_times"; //$NON-NLS-1$

    public static final String DATA_COLLECTOR_LAST_TIME = "data_collector_last_time"; //$NON-NLS-1$

    public static final String DATA_COLLECTOR_PREVIEW = "data_collector_preview"; //$NON-NLS-1$

    public static final String DATA_COLLECTOR = "data_collector"; //$NON-NLS-1$

    public static final String EXCHANGE_CHECK_TIS_VERSION = "exchange_check_tis_version"; //$NON-NLS-1$

    public static final String EXCHANGE_DOWNLOADED_CHECK_UPDATES = "exchange_downloaded_check_updates"; //$NON-NLS-1$

    public static final String MAXIMUM_AMOUNT_OF_COLUMNS_FOR_XML = "maximum_amount_of_columns_for_xml"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_PATH = "oozie_scheduler_path"; //$NON-NLS-1$

    public static final String OOZIE_SHCEDULER_HADOOP_DISTRIBUTION = "oozie_shceduler_hadoop_distribution"; //$NON-NLS-1$

    public static final String OOZIE_SHCEDULER_HADOOP_VERSION = "oozie_shceduler_hadoop_version"; //$NON-NLS-1$

    public static final String OOZIE_SHCEDULER_NAME_NODE_ENDPOINT = "oozie_scheduler_name_node_endpoint"; //$NON-NLS-1$

    public static final String OOZIE_SHCEDULER_JOB_TRACKER_ENDPOINT = "oozie_scheduler_job_tracker_endpoint"; //$NON-NLS-1$

    public static final String OOZIE_SHCEDULER_OOZIE_ENDPOINT = "oozie_scheduler_oozie_endpoint"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_USER_NAME = "oozie_scheduler_user_name"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_AUTH_MODE = "oozie_scheduler_auth_mode"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_GROUP = "oozie_scheduler_group"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_CUSTOM_JARS = "oozie_scheduler_hadoop_custom_jars"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_PRINCIPAL = "oozie_scheduler_hadoop_principal"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_KERBEROS = "oozie_scheduler_hadoop_kerberos"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_OOZIE_KERBEROS = "oozie_scheduler_oozie_kerberos"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_USE_KEYTAB = "oozie_scheduler_hadoop_use_keytab"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_USE_YARN = "oozie_scheduler_hadoop_use_yarn"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_KEYTAB_PRINCIPAL = "oozie_scheduler_hadoop_keytab_principal"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_KEYTAB_PATH = "oozie_scheduler_hadoop_keytab_path"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_HADOOP_PROPERTIES = "oozie_scheduler_hadoop_properties"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_PROPERTY_TYPE = "oozie_scheduler_property_type"; //$NON-NLS-1$

    public static final String OOZIE_SCHEDULER_RELATIVE_HADOOP_CLUSTER_ID = "oozie_scheduler_relative_hadoop_cluster_id"; //$NON-NLS-1$

    public static final String CoreUIPlugin_ID = "org.talend.core.ui"; //$NON-NLS-1$

    public static final String TOP_INSTALL_DONE = "top.install.done"; //$NON-NLS-1$

    public static final String SVN_UPDATE_INFO_AUTO_CHECK = "PerformancePreferencePage.svn_update_info_auto_check"; //$NON-NLS-1$

    public static final String SVN_UPDATE_INFO_AUTO_CHECK_TIME_INTERVAL = "PerformancePreferencePage.svn_update_info_auto_check_time_interval"; //$NON-NLS-1$

    public static final String AUTO_REFRESH_LOCKS = "PerformancePreferencePage.auto_refresh_locks"; //$NON-NLS-1$

    /**
     * <b>!!! NOTE !!!:</b><br/>
     * This prefrence <b>must</b> be stored in <b>PlatformUI prefrence</b>, since it will be used in login dialog while
     * coreUIPlugin maybe not loaded yet
     */
    public static final String LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP = "logonDialog.alwaysAskMeAtStartUp"; //$NON-NLS-1$

    /**
     * PlatformUI.getPreference().getBoolean("NOT_SHOW_ONBOARDING_AT_STARTUP")
     */
    public static final String PREFERENCE_NOT_SHOW_ONBOARDING_AT_STARTUP = "NOT_SHOW_ONBOARDING_AT_STARTUP"; //$NON-NLS-1$

    /**
     * TAC connection timeout (seconds)
     */
    public static final String PERFORMANCE_TAC_CONNECTION_TIMEOUT = "PERFORMANCE_TAC_CONNECTION_TIMEOUT"; //$NON-NLS-1$

    public static final String PERFORMANCE_TAC_READ_TIMEOUT = "PERFORMANCE_TAC_READ_TIMEOUT"; //$NON-NLS-1$

    public static final String PERFORMANCE_JAVA_PROCESS_CODE_FORMATE_TIMEOUT = "PERFORMANCE_JAVA_PROCESS_CODE_FORMATE_TIMEOUT"; //$NON-NLS-1$

    public static final String NEXUS_TIMEOUT = "NEXUS_TIMEOUT"; //$NON-NLS-1$

    public static final String NEXUS_REFRESH_FREQUENCY = "NEXUS_REFRESH_FREQUENCY"; //$NON-NLS-1$

    /**
     * AutoMap algorithm weight
     */

    public static final String LEVENSHTEIN_WEIGHT = "LEVENSHTEIN_WEIGHT"; //$NON-NLS-1$

    public static final String JACCARD_WEIGHT = "JACCARD_WEIGHT"; //$NON-NLS-1$

}
