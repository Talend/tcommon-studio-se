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
package org.talend.commons.ui.runtime.image;

/**
 * 
 * DOC smallet ImageProvider class global comment. Detailled comment <br/>
 * 
 * $Id: ImageProvider.java 418 2006-11-13 16:01:26 +0000 (lun., 13 nov. 2006) cantoine $
 * 
 */
public enum ECoreImage implements IImage {

    PROJECT_ICON("/icons1/prj_obj.gif"), //$NON-NLS-1$

    COPY_ICON("/icons1/copy.gif"), //$NON-NLS-1$

    COMPARE_ICON("/icons1/compare_view.gif"), //$NON-NLS-1$
    TEST_CONTAINER("/icons1/testContainer.png"), //$NON-NLS-1$
    STANDARD_ICON("/icons1/palette.png"), //$NON-NLS-1$

    PALETTE_FILTER("/icons1/studio_6.0-palette-filter.png"), //$NON-NLS-1$

    STANDARD_DISICON("/icons1/standard_dis.png"), //$NON-NLS-1$

    FAVORITE_DISICON("/icons1/favorite_dis.png"), //$NON-NLS-1$

    FAVORITE_ICON("/icons1/palette_fav.png"), //$NON-NLS-1$

    TALEND_PICTO("/icons1/talend-picto-small.gif"), //$NON-NLS-1$

    DEFAULT_WIZ("/icons1/defaultWizard.png"), //$NON-NLS-1$

    PROJECT_WIZ("/icons1/project_wiz.png"), //$NON-NLS-1$

    RECYCLE_BIN_EMPTY_ICON("/icons1/recyclebinempty.png"), //$NON-NLS-1$
    RECYCLE_BIN_FULL_ICON("/icons1/recyclebinfull.png"), //$NON-NLS-1$
    FOLDER_OPEN_ICON("/icons1/folder_open.png"), //$NON-NLS-1$
    FOLDER_CLOSE_ICON("/icons1/folder_close.png"), //$NON-NLS-1$
    FOLDER_WIZ("/icons1/folder_wiz.gif"), //$NON-NLS-1$

    SVN_ROOT_ICON("/icons1/svn.gif"), //$NON-NLS-1$
    BUSINESS_PROCESS_ICON("/icons1/businessProcess.png"), //$NON-NLS-1$
    BUSINESS_PROCESS_WIZ("/icons1/business_wiz.png"), //$NON-NLS-1$
    PROCESS_ICON("/icons1/process_icon.png"), //$NON-NLS-1$
    PROCESS_ICON_EDITOR("/icons1/process_icon_editor.png"), //$NON-NLS-1$
    PROCESS_WIZ("/icons1/process_wiz.png"), //$NON-NLS-1$
    PROCESS_TEMPLATE_ICON("/icons1/process_icon.png"), //$NON-NLS-1$
    PROCESS_TEMPLATE_WIZ("/icons1/process_template_wiz.png"), //$NON-NLS-1$
    PROCESS_STANDARD_GENERIC_CATEGORY_CLOSE_ICON("/icons1/process_standard_generic_category_close.png"), //$NON-NLS-1$
    PROCESS_STANDARD_GENERIC_CATEGORY_OPEN_ICON("/icons1/process_standard_generic_category_open.png"), //$NON-NLS-1$
    PROCESS_STREAMING_GENERIC_CATEGORY_CLOSE_ICON("/icons1/process_streaming_generic_category_close.png"), //$NON-NLS-1$
    PROCESS_STREAMING_GENERIC_CATEGORY_OPEN_ICON("/icons1/process_streaming_generic_category_open.png"), //$NON-NLS-1$
    PROCESS_BATCH_GENERIC_CATEGORY_CLOSE_ICON("/icons1/process_batch_generic_category_close.png"), //$NON-NLS-1$
    PROCESS_BATCH_GENERIC_CATEGORY_OPEN_ICON("/icons1/process_batch_generic_category_open.png"), //$NON-NLS-1$
    PROCESS_STREAMING_STORM_ICON("/icons1/process_streaming_storm.png"), //$NON-NLS-1$
    PROCESS_STREAMING_SPARK_ICON("/icons1/process_streaming_spark.png"), //$NON-NLS-1$
    PROCESS_BATCH_MR_ICON("/icons1/process_batch_map_reduce.png"), //$NON-NLS-1$
    PROCESS_BATCH_SPARK_ICON("/icons1/process_batch_spark.png"), //$NON-NLS-1$
    SERVICES_ICON("/icons1/services.png"), //$NON-NLS-1$
    JOBLET_ICON("/icons1/joblet_icon.png"), //$NON-NLS-1$
    JOBLET_STANDARD_ICON("/icons1/joblet_standard.png"), //$NON-NLS-1$
    CONTEXT_ICON("/icons1/context_icon.png"), //$NON-NLS-1$
    CONTEXT_CONF_ICON("/icons1/context_conf_icon.gif"), //$NON-NLS-1$
    CONTEXT_WIZ("/icons1/context_wiz.png"), //$NON-NLS-1$
    CODE_ICON("/icons1/code.png"), //$NON-NLS-1$
    ROUTES_ICON("/icons1/routes_icon.png"), //$NON-NLS-1$
    ROUTINE_ICON("/icons1/routine.png"), //$NON-NLS-1$
    ROUTINE_EDITOR_ICON("/icons1/routine_editor.png"), //$NON-NLS-1$
    JOB_SCRIPTS_ICON("/icons1/job_scripts.png"), //$NON-NLS-1$
    JOB_SCRIPTS_TOOLBAR_ICON("/icons1/job_script_toolbar.png"), //$NON-NLS-1$
    FTP_ICON("/icons1/ftp.png"), //$NON-NLS-1$
    ROUTINE_WIZ("/icons1/routine_wiz.png"), //$NON-NLS-1$
    SNIPPETS_ICON("/icons1/snippet.png"), //$NON-NLS-1$
    // Note Component icon
    NOTE_SMALL_ICON("/icons1/Note_icon16.png"), //$NON-NLS-1$
    NOTE_MIDDLE_ICON("/icons1/Note_icon24.png"), //$NON-NLS-1$
    NOTE_BIG_ICON("/icons1/Note_icon32.png"), //$NON-NLS-1$

    // SUBROUTINE_ICON("/icons1/subroutine.gif"),
    DOCUMENTATION_ICON("/icons1/documentation.png"), //$NON-NLS-1$
    DOCUMENTATION_SINGLE_ICON("/icons1/Documentation_feuillet.png"), //$NON-NLS-1$
    DOCUMENTATION_WIZ("/icons1/documentation_wiz.png"), //$NON-NLS-1$

    METADATA_ICON("/icons1/metadata.png"), //$NON-NLS-1$
    METADATA_CONNECTION_ICON("/icons1/connection.gif"), //$NON-NLS-1$
    METADATA_CONNECTION_WIZ("/icons1/connection_wiz.png"), //$NON-NLS-1$
    METADATA_CDC_CONN_ICON("/icons1/cdc_conn.png"), //$NON-NLS-1$

    METADATA_HL7_CONNECTION_ICON("/icons1/hl7.png"), //$NON-NLS-1$
    METADATA_HL7_CONNECTION_WIZ("/icons1/hl7.png"), //$NON-NLS-1$

    METADATA_BRMS_CONNECTION_ICON("/icons1/rules_wiz.gif"), //$NON-NLS-1$
    METADATA_BRMS_CONNECTION_WIZ("/icons1/rules_wiz.gif"), //$NON-NLS-1$

    METADATA_EBCDIC_CONNECTION_ICON("/icons1/copybook.png"), //$NON-NLS-1$
    METADATA_EBCDIC_CONNECTION_WIZ("/icons1/copybook_wiz.png"), //$NON-NLS-1$

    METADATA_MDM_CONNECTION_ICON("/icons1/MDM_16px.png"), //$NON-NLS-1$
    METADATA_MDM_CONNECTION_WIZ("/icons1/talendMDM_wiz.png"), //$NON-NLS-1$

    METADATA_RULES_ICON("/icons1/rules_wiz.gif"), //$NON-NLS-1$
    METADATA_RULES_WIZ("/icons1/rules_wiz.gif"), //$NON-NLS-1$

    METADATA_SAPCONNECTION_ICON("/icons1/sapconnection.png"), //$NON-NLS-1$
    METADATA_SAPCONNECTION_WIZ("/icons1/connection_wiz.gif"), //$NON-NLS-1$

    METADATA_HEADERFOOTER_ICON("/icons1/headerfooter_icon32.png"), //$NON-NLS-1$

    METADATA_SQLPATTERN_ICON("/icons1/SQLPattern.png"), //$NON-NLS-1$
    METADATA_SQLPATTERN_ICON_EDITOR("/icons1/SQLPattern_editor.png"), //$NON-NLS-1$
    METADATA_SQLPATTERN_WIZ("/icons1/defaultWizard.png"), //$NON-NLS-1$

    METADATA_TABLE_ICON("/icons1/table.gif"), //$NON-NLS-1$
    METADATA_COLUMN_ICON("/icons1/columns.gif"), //$NON-NLS-1$
    METADATA_TABLE_WIZ("/icons1/table_wiz.png"), //$NON-NLS-1$
    METADATA_VIEW_ICON("/icons1/view.gif"), //$NON-NLS-1$
    METADATA_SYNONYM_ICON("/icons1/synonym.gif"), //$NON-NLS-1$
    METADATA_QUERY_ICON("/icons1/query.gif"), //$NON-NLS-1$
    METADATA_FILE_DELIMITED_ICON("/icons1/filedelimited.gif"), //$NON-NLS-1$
    METADATA_FILE_DELIMITED_WIZ("/icons1/delimited_wiz.png"), //$NON-NLS-1$
    METADATA_FILE_POSITIONAL_ICON("/icons1/filepositional.gif"), //$NON-NLS-1$
    METADATA_FILE_POSITIONAL_WIZ("/icons1/positional_wiz.png"), //$NON-NLS-1$
    METADATA_FILE_REGEXP_ICON("/icons1/fileregexp.gif"), //$NON-NLS-1$
    METADATA_FILE_REGEXP_WIZ("/icons1/regexp_wiz.png"), //$NON-NLS-1$
    METADATA_FILE_XML_ICON("/icons1/filexml.gif"), //$NON-NLS-1$
    METADATA_FILE_XML_WIZ("/icons1/xml_wiz.png"), //$NON-NLS-1$
    METADATA_FILE_EXCEL_ICON("/icons1/fileexcel.gif"), //$NON-NLS-1$
    METADATA_FILE_EXCEL_WIZ("/icons1/excel_wiz.png"), //$NON-NLS-1$
    METADATA_FILE_LDIF_ICON("/icons1/fileldif.gif"), //$NON-NLS-1$
    METADATA_FILE_LDIF_WIZ("/icons1/ldif_wiz.png"), //$NON-NLS-1$
    METADATA_GENERIC_ICON("/icons1/genericSchema_icon.png"), //$NON-NLS-1$
    METADATA_LDAP_SCHEMA_ICON("/icons1/ldap_icon.png"), //$NON-NLS-1$
    METADATA_WSDL_SCHEMA_ICON("/icons1/wsdl_icon.png"), //$NON-NLS-1$
    METADATA_SALESFORCE_SCHEMA_ICON("/icons1/salesforce_icon.png"), //$NON-NLS-1$
    METADATA_VALIDATION_RULES_ICON("/icons1/metadata.png"), //$NON-NLS-1$
    METADATA_VALIDATION_RULES_WIZ("/icons1/delimited_wiz.png"), //$NON-NLS-1$
    METADATA_EDIFACT_ICON("/icons1/EDIField.png"), //$NON-NLS-1$
    MODULE_INSTALLED_ICON("/icons1/checked.gif"), //$NON-NLS-1$
    MODULE_ERROR_ICON("/icons1/error.gif"), //$NON-NLS-1$
    MODULE_WARNING_ICON("/icons1/warning.gif"), //$NON-NLS-1$
    MODULE_INFORMATION_ICON("/icons1/unknown.gif"), //$NON-NLS-1$
    MODULE_UNKNOWN_ICON("/icons1/unknown.gif"), //$NON-NLS-1$
    MODULE_REQUIRED_ICON("/icons1/checked.gif"), //$NON-NLS-1$
    MODULE_NOTREQUIRED_ICON("/icons1/empty.gif"), //$NON-NLS-1$

    LOCKED_USER_OVERLAY("/icons1/locked_green_overlay.gif"), //$NON-NLS-1$
    LOCKED_OTHER_OVERLAY("/icons1/locked_red_overlay.gif"), //$NON-NLS-1$
    NEW_OVERLAY("/icons1/new_overlay.gif"), //$NON-NLS-1$
    MASTER_OVERLAY("/icons1/etoile-master-job1.gif"), //$NON-NLS-1$
    DOC_PPT_OVERLAY("/icons1/docs/doc_ppt_overlay.gif"), //$NON-NLS-1$
    DOC_PDF_OVERLAY("/icons1/docs/doc_pdf_overlay.gif"), //$NON-NLS-1$
    DOC_EXCEL_OVERLAY("/icons1/docs/doc_excel_overlay.gif"), //$NON-NLS-1$
    DOC_WORD_OVERLAY("/icons1/docs/doc_word_overlay.gif"), //$NON-NLS-1$
    RECYCLE_BIN_OVERLAY("/icons1/recycle_bin_overlay.gif"), //$NON-NLS-1$
    DELETED_OVERLAY("/icons1/deleted_overlay.gif"), //$NON-NLS-1$
    IMPORT_JAR("/icons1/importjar.gif"), //$NON-NLS-1$
    REFERENCED_ICON("/icons1/referenced.png"), //$NON-NLS-1$

    CDC_ADDED_OVERLAY("/icons1/cdc_added_overlay.png"), //$NON-NLS-1$
    CDC_ACTIVATED_OVERLAY("/icons1/cdc_activated_overlay.png"), //$NON-NLS-1$
    CDC_SUBSCRIBER("/icons1/subscriber.jpg"), //$NON-NLS-1$

    ERROR_OVERLAY("/icons1/error_co.gif"), //$NON-NLS-1$
    WARN_OVERLAY("/icons1/warning_co.gif"), //$NON-NLS-1$

    TOGGLE_SUBJOB("/icons1/toggleSubjobs.png"), //$NON-NLS-1$
    TOGGLE_SUBJOB_DISABLED("/icons1/toggleSubjobDisabled.png"), //$NON-NLS-1$

    COMPONMENT_ASSIST("/icons1/assist.gif"), //$NON-NLS-1$

    TRACE_ON("/icons1/trace_on.png"), //$NON-NLS-1$
    TRACE_OFF("/icons1/trace_off.png"), //$NON-NLS-1$

    RESUMING_CHECKPOINT_ICON("/icons1/resuming_checkpoint_icon.png"), //$NON-NLS-1$
    PALETTE_OK_ICON("/icons1/palette_ok.png"), //$NON-NLS-1$
    PALETTE_CLEAR_ICON("/icons1/clear_toolbox.png"), //$NON-NLS-1$
    JOBLET_COMPONENT_ICON("/icons1/joblet_component.png"), //$NON-NLS-1$
    SPARK_JOBLET_COMPONENT_ICON("/icons1/spark_joblet_component.png"), //$NON-NLS-1$
    SPARK_STREAMING_JOBLET_COMPONENT_ICON("/icons1/spark_streaming_joblet_component.png"), //$NON-NLS-1$
    UNKNOWN(),
    PROGRESSBAR("/icons1/progressBar.gif"), //$NON-NLS-1$
    PROGRESSBARBlACK("/icons1/progressBar2.gif"), //$NON-NLS-1$
    PROGRESSGREEBAR("/icons1/greeBar.gif"), //$NON-NLS-1$
    PROGRESSGRAYBAR("/icons1/gray.gif"), //$NON-NLS-1$
    PROGRESSGRAYGEBAR("/icons1/graygeBar.gif"), //$NON-NLS-1$
    TRIANGLE("/icons1/triangle.gif"), // triangle.gif //$NON-NLS-1$
    COMPARE("/icons1/compare.gif"), //$NON-NLS-1$
    EXCHNAGETAB("/icons/exchangeTab.jpg"), //$NON-NLS-1$
    EXCHNAGEIMAGEMISSING("/icons/component_missing.gif"), //$NON-NLS-1$
    STATUS_OK("/icons/ok.png"), //$NON-NLS-1$
    MRGREEBAR("/icons1/mrGreeBar.gif"), //$NON-NLS-1$
    MRMAP("/icons1/map.gif"), //$NON-NLS-1$
    MRREDUCE("/icons1/reduce.gif"), //$NON-NLS-1$
    MRREDBAR("/icons1/mrRedBar.gif"), //$NON-NLS-1$
    MRGRAYBAR("/icons1/mrGrayBar.gif"), //$NON-NLS-1$
    CONN_HANDLE("/icons/handle.gif"), //$NON-NLS-1$
    GREEN_HANDLE("/icons/green_handle.gif"), //$NON-NLS-1$

    TDQ_ANALYSIS_ICON("/icons1/tdq/chart_bar.png"), //$NON-NLS-1$
    TDQ_RULE_ICON("/icons1/tdq/dqrule_red.png"), //$NON-NLS-1$
    TDQ_MATCH_RULE_ICON("/icons1/tdq/match_rule.png"), //$NON-NLS-1$
    TDQ_INDICATOR_ICON("/icons1/tdq/IndicatorDefinition.png"), //$NON-NLS-1$
    TDQ_PATTERN_ICON("/icons1/tdq/pattern.png"), //$NON-NLS-1$
    TDQ_JRAXML_ICON("/icons1/tdq/jrxml.png"), //$NON-NLS-1$
    TDQ_SOURCE_FILE_ICON("/icons1/tdq/editor.png"), //$NON-NLS-1$
    TDQ_REPORT_ICON("/icons1/tdq/report.png"), //$NON-NLS-1$
    TDQ_EXCHANGE_ICON("/icons1/tdq/ecosystem_view.png"), //$NON-NLS-1$
    TDQ_LIBRARIES_ICON("/icons1/tdq/libraries.png"), //$NON-NLS-1$
    TDQ_DATA_PROFILING_ICON("/icons1/tdq/server_chart.png"); //$NON-NLS-1$

    private String path;

    ECoreImage() {
        this.path = "/icons1/unknown.gif"; //$NON-NLS-1$
    }

    ECoreImage(String path) {
        this.path = path;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for clazz.
     * 
     * @return the clazz
     */
    @Override
    public Class getLocation() {
        return ECoreImage.class;
        // return CorePlugin.class;
    }

}
