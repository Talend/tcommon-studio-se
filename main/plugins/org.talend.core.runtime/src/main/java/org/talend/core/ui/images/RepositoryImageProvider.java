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
package org.talend.core.ui.images;

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.RepositoryContentManager;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryImageProvider {

    public static IImage getIcon(ERepositoryObjectType type) {

        if (type == ERepositoryObjectType.SVN_ROOT) {
            return ECoreImage.SVN_ROOT_ICON;
        } else if (type == ERepositoryObjectType.BUSINESS_PROCESS) {
            return ECoreImage.BUSINESS_PROCESS_ICON;
        } else if (type == ERepositoryObjectType.PROCESS) {
            return ECoreImage.PROCESS_ICON;
        } else if (type == ERepositoryObjectType.JOBLET) {
            return ECoreImage.JOBLET_STANDARD_ICON;
        } else if (type == ERepositoryObjectType.JOBLET_DESIGNS) {
            return ECoreImage.JOBLET_ICON;
        } else if (type == ERepositoryObjectType.CONTEXT) {
            return ECoreImage.CONTEXT_ICON;
        } else if (type == ERepositoryObjectType.CODE) {
            return ECoreImage.CODE_ICON;
        } else if (type == ERepositoryObjectType.ROUTINES) {
            return ECoreImage.ROUTINE_ICON;
        } else if (type == ERepositoryObjectType.PIG_UDF) {
            return ECoreImage.ROUTINE_ICON;
        } else if (type == ERepositoryObjectType.JOB_SCRIPT) {
            return ECoreImage.JOB_SCRIPTS_ICON;
        } else if (type == ERepositoryObjectType.SNIPPETS) {
            return ECoreImage.SNIPPETS_ICON;
        } else if (type == ERepositoryObjectType.DOCUMENTATION || type == ERepositoryObjectType.JOB_DOC
                || type == ERepositoryObjectType.JOBLET_DOC) {
            return ECoreImage.DOCUMENTATION_ICON;
        } else if (type == ERepositoryObjectType.METADATA) {
            return ECoreImage.METADATA_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CONNECTIONS) {
            return ECoreImage.METADATA_CONNECTION_ICON;
        } else if (type == ERepositoryObjectType.METADATA_SAPCONNECTIONS || type == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
            return ECoreImage.METADATA_SAPCONNECTION_ICON;
        } else if (type == ERepositoryObjectType.SQLPATTERNS) {
            return ECoreImage.METADATA_SQLPATTERN_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_TABLE || type == ERepositoryObjectType.METADATA_SAP_IDOC) {
            return ECoreImage.METADATA_TABLE_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_COLUMN) {
            return ECoreImage.METADATA_COLUMN_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_QUERY) {
            return ECoreImage.METADATA_QUERY_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_VIEW
                || type == ERepositoryObjectType.METADATA_CON_CALCULATION_VIEW) {
            return ECoreImage.METADATA_VIEW_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_SYNONYM) {
            return ECoreImage.METADATA_SYNONYM_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            return ECoreImage.METADATA_FILE_DELIMITED_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
            return ECoreImage.METADATA_FILE_POSITIONAL_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_REGEXP) {
            return ECoreImage.METADATA_FILE_REGEXP_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            return ECoreImage.METADATA_FILE_XML_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_EXCEL) {
            return ECoreImage.METADATA_FILE_EXCEL_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_LDIF) {
            return ECoreImage.METADATA_FILE_LDIF_ICON;
        } else if (type == ERepositoryObjectType.FOLDER) {
            return ECoreImage.FOLDER_OPEN_ICON;
        } else if (type == ERepositoryObjectType.REFERENCED_PROJECTS) {
            return ECoreImage.REFERENCED_ICON;
        } else if (type == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            return ECoreImage.METADATA_GENERIC_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_FTP) {
            return ECoreImage.FTP_ICON;
        } else if (type == ERepositoryObjectType.METADATA_LDAP_SCHEMA) {
            return ECoreImage.METADATA_LDAP_SCHEMA_ICON;
        } else if (type == ERepositoryObjectType.METADATA_WSDL_SCHEMA) {
            return ECoreImage.METADATA_WSDL_SCHEMA_ICON;
        } else if (type == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
            return ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON;
        } else if (type == ERepositoryObjectType.METADATA_SALESFORCE_MODULE) {
            return ECoreImage.METADATA_SALESFORCE_SCHEMA_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
            return ECoreImage.METADATA_EBCDIC_CONNECTION_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_HL7) {
            return ECoreImage.METADATA_HL7_CONNECTION_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_BRMS) {
            return ECoreImage.METADATA_BRMS_CONNECTION_ICON;
        } else if (type == ERepositoryObjectType.METADATA_MDMCONNECTION) {
            return ECoreImage.METADATA_MDM_CONNECTION_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_RULES) {
            return ECoreImage.METADATA_RULES_ICON;
        } else if (type == ERepositoryObjectType.METADATA_FILE_LINKRULES) {
            return ECoreImage.METADATA_RULES_ICON;
        } else if (type == ERepositoryObjectType.METADATA_RULES_MANAGEMENT) {
            return ECoreImage.METADATA_RULES_ICON;
        } else if (type == ERepositoryObjectType.METADATA_VALIDATION_RULES) {
            return ECoreImage.METADATA_ICON;
        } else if (type == ERepositoryObjectType.RECYCLE_BIN) {
            return ECoreImage.RECYCLE_BIN_EMPTY_ICON;
        } else if (type == ERepositoryObjectType.METADATA_EDIFACT) {
            return ECoreImage.METADATA_EDIFACT_ICON;
        } else if (type == ERepositoryObjectType.METADATA_CON_CDC) {
            return ECoreImage.CDC_SUBSCRIBER;

            // for the DQ types
        } else if (type == ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT) {
            return ECoreImage.TDQ_ANALYSIS_ICON;
        } else if (type == ERepositoryObjectType.TDQ_REPORT_ELEMENT) {
            return ECoreImage.TDQ_REPORT_ICON;
        } else if (type == ERepositoryObjectType.TDQ_JRAXML_ELEMENT) {
            return ECoreImage.TDQ_JRAXML_ICON;
        } else if (type == ERepositoryObjectType.TDQ_RULES_MATCHER) {
            return ECoreImage.TDQ_MATCH_RULE_ICON;
        } else if (type == ERepositoryObjectType.TDQ_RULES_SQL || type == ERepositoryObjectType.TDQ_RULES_PARSER
                || type == ERepositoryObjectType.TDQ_RULES) {
            return ECoreImage.TDQ_RULE_ICON;
        } else if (type == ERepositoryObjectType.TDQ_PATTERN_REGEX || type == ERepositoryObjectType.TDQ_PATTERN_SQL
                || type == ERepositoryObjectType.TDQ_PATTERN_ELEMENT) {
            return ECoreImage.TDQ_PATTERN_ICON;
        } else if (type == ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT) {
            return ECoreImage.TDQ_SOURCE_FILE_ICON;
        } else if (type == ERepositoryObjectType.TDQ_SYSTEM_INDICATORS || type == ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS
                || type == ERepositoryObjectType.TDQ_INDICATOR_ELEMENT
                || type == ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS_LIB
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_ADVANCED_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_BUSINESS_RULES
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_CORRELATION
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_OVERVIEW
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_MATCHING
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_ROW_COMPARISON
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_SIMPLE_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_SOUNDEX
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_SUMMARY_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_TEXT_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS
                || type == ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION) {
            return ECoreImage.TDQ_INDICATOR_ICON;
        } else if (type == ERepositoryObjectType.TDQ_DATA_PROFILING) {
            return ECoreImage.TDQ_DATA_PROFILING_ICON;
        } else if (type == ERepositoryObjectType.TDQ_LIBRARIES) {
            return ECoreImage.TDQ_LIBRARIES_ICON;
        } else {
            IImage image = null;
            for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                image = handler.getIcon(type);
                if (image != null) {
                    return image;
                }
            }
            return EImage.DEFAULT_IMAGE;
        }
    }

    public static Image getImage(ERepositoryObjectType type) {
        return ImageProvider.getImage(getIcon(type));
    }
}
