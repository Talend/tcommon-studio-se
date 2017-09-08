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
package org.talend.repository.model;

import org.eclipse.core.runtime.Path;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * Defines some constants relative to repository such as file patterns.<br/>
 * 
 * $Id: RepositoryConstants.java 8 2006-10-02 09:09:23 +0000 (鏄熸湡涓� 02 �?鏈�2006) mhirt $
 * 
 */
public class RepositoryConstants {

    public static final String TABLE = "TABLE"; //$NON-NLS-1$

    public static final String TEMP_DIRECTORY = "temp"; //$NON-NLS-1$

    public static final String SETTING_DIRECTORY = ".settings"; //$NON-NLS-1$

    public static final String IMG_DIRECTORY = "images"; //$NON-NLS-1$

    public static final String IMG_DIRECTORY_OF_JOB_OUTLINE = "images/job_outlines"; //$NON-NLS-1$

    public static final String IMG_DIRECTORY_OF_JOBLET_OUTLINE = "images/joblet_outlines"; //$NON-NLS-1$

    public static final String DOCUMENTATION_GENERATED_PATH = "generated"; //$NON-NLS-1$

    public static final String JOB_DOCUMENTATION_PATH = "documentations/generated/jobs"; //$NON-NLS-1$

    public static final String JOBLET_DOCUMENTATION_PATH = "documentations/generated/joblets"; //$NON-NLS-1$

    public static final String SYSTEM_DIRECTORY = "system"; //$NON-NLS-1$

    public static final String USER_DEFINED = "UserDefined"; //$NON-NLS-1$

    public static final String CONTEXT_AND_VARIABLE_PATTERN = "^[a-zA-Z\\_]+[a-zA-Z0-9\\_]*$"; //$NON-NLS-1$

    public static final String PROJECT_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9 \\-_]*$"; //$NON-NLS-1$

    public static final String CODE_ITEM_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9\\_]*$"; //$NON-NLS-1$

    public static final String ROUTINES_ITEM_PATTERN = "^[a-zA-Z\\_]+[a-zA-Z0-9\\_]*$"; //$NON-NLS-1$

    //    public static final String SCHEMA_NAME_PATTERN = "^[a-zA-Z0-9\\_]*$";; //$NON-NLS-1$

    public static final String COLUMN_NAME_PATTERN = "^[a-zA-Z_][a-zA-Z_0-9]*$"; //$NON-NLS-1$

    //    public static final String REPOSITORY_SCHEMA_PATTERN = "^[a-zA-Z0-9\\_]+$"; //$NON-NLS-1$

    public static final String SIMPLE_FOLDER_PATTERN = "[a-zA-Z0-9\\_-]+$"; //$NON-NLS-1$ //this added for xml metadata folder

    public static final String FOLDER_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9\\_]*$"; //$NON-NLS-1$

    public static final String REPOSITORY_ITEM_PATTERN_INTERN = "a-zA-Z0-9\\.\\-\\_\\ \\(\\)\\[\\]="; //$NON-NLS-1$

    public static final String REPOSITORY_ITEM_PATTERN = "^[" + REPOSITORY_ITEM_PATTERN_INTERN + "]+$"; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String REPOSITORY_ITEM_WILDCARD = "^[" + REPOSITORY_ITEM_PATTERN_INTERN + "\\*\\?]+$"; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String PATH_WILDCARD = "^[a-zA-Z\\*\\?/]+[a-zA-Z0-9\\_\\*\\?/]*$"; //$NON-NLS-1$

    public static final String PORT_ITEM_PATTERN = "^[0-9]"; //$NON-NLS-1$

    public static final String MAIL_PATTERN = "(?:[a-zA-ZÀ-ÿ0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-ZÀ-ÿ0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-ZÀ-ÿ0-9](?:[a-zA-ZÀ-ÿ0-9-]*[a-zA-ZÀ-ÿ0-9])?\\.)+[a-zA-ZÀ-ÿ0-9](?:[a-zA-ZÀ-ÿ0-9-]*[a-zA-ZÀ-ÿ0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-ZÀ-ÿ0-9-]*[a-zA-ZÀ-ÿ0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"; //$NON-NLS-1$

    public static final String NOT_SET_STATUS = "not_set_status"; //$NON-NLS-1$

    public static final String REPOSITORY_LOCAL_ID = "local"; //$NON-NLS-1$

    public static final String REPOSITORY_REMOTE_ID = "remote"; //$NON-NLS-1$

    public static final String REPOSITORY_URL = "url"; //$NON-NLS-1$

    public static final String TDQ_ALL_ITEM_PATTERN = ".*"; //$NON-NLS-1$

    // GLIU: add for TESB-3837
    public static final String SERVICES_NAME_PATTERN = "[a-zA-Z_][a-zA-Z0-9\\.\\-_]*"; //$NON-NLS-1$

    public static final String MDM_ITEM_PATTERN = ".*"; //$NON-NLS-1$

    public static final String METADATA_NAME_PATTERN = ".*"; //$NON-NLS-1$

    public static final String[] ITEM_FORBIDDEN_IN_LABEL = { "~", "!", "`", "#", "^", "&", "*", "\\", "/", "?", ":", ";", "\"", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            ".", "(", ")", "，", "。", "'", "￥", "‘", "”", "、", "《", "，", "》", "<", ">", " " }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$

    public static String getPattern(ERepositoryObjectType type) {
        if (type == ERepositoryObjectType.FOLDER) {
            return FOLDER_PATTERN;
        } else if (ERepositoryObjectType.getAllTypesOfProcess().contains(type) || type == ERepositoryObjectType.JOBLET || type == ERepositoryObjectType.JOB_SCRIPT) {
            return CODE_ITEM_PATTERN;
        } else if (type == ERepositoryObjectType.ROUTINES || type == ERepositoryObjectType.PIG_UDF
                || "BEANS".equals(type.getType())) {
            // for bug 10356
            return ROUTINES_ITEM_PATTERN;
        } else if (type == ERepositoryObjectType.TDQ_JRAXML_ELEMENT || type == ERepositoryObjectType.TDQ_DATA_PROFILING
                || type == ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT || type == ERepositoryObjectType.TDQ_REPORT_ELEMENT
                || type == ERepositoryObjectType.TDQ_LIBRARIES || type == ERepositoryObjectType.TDQ_PATTERN_ELEMENT
                || type == ERepositoryObjectType.TDQ_PATTERN_REGEX || type == ERepositoryObjectType.TDQ_PATTERN_SQL
                || type == ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT || type == ERepositoryObjectType.TDQ_RULES
                || type == ERepositoryObjectType.TDQ_RULES_SQL || type == ERepositoryObjectType.TDQ_INDICATOR_ELEMENT) {
            return TDQ_ALL_ITEM_PATTERN;
        } else if (type == ERepositoryObjectType.METADATA_CONNECTIONS || type == ERepositoryObjectType.METADATA_FILE_DELIMITED
                || type == ERepositoryObjectType.METADATA_FILE_POSITIONAL || type == ERepositoryObjectType.METADATA_FILE_REGEXP
                || type == ERepositoryObjectType.METADATA_FILE_XML || type == ERepositoryObjectType.METADATA_FILE_EXCEL
                || type == ERepositoryObjectType.METADATA_FILE_LDIF || type == ERepositoryObjectType.METADATA_LDAP_SCHEMA
                || type == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA
                || type == ERepositoryObjectType.METADATA_GENERIC_SCHEMA || type == ERepositoryObjectType.METADATA_MDMCONNECTION
                || type == ERepositoryObjectType.METADATA_FILE_HL7 || type == ERepositoryObjectType.METADATA_FILE_RULES
                || type == ERepositoryObjectType.METADATA_FILE_EBCDIC || type == ERepositoryObjectType.METADATA_WSDL_SCHEMA
                || type == ERepositoryObjectType.METADATA_VALIDATION_RULES || type == ERepositoryObjectType.METADATA_FILE_FTP
                || type == ERepositoryObjectType.METADATA_EDIFACT) {
            return METADATA_NAME_PATTERN;
        }
        // GLIU: add for TESB-3837
        else if (type != null && "SERVICES".equals(type.getType())) { //$NON-NLS-1$
            return SERVICES_NAME_PATTERN;
        } else if (type != null && type.getType() != null && type.getType().startsWith("MDM.")) { //$NON-NLS-1$
            return MDM_ITEM_PATTERN;
        } else {
            return TDQ_ALL_ITEM_PATTERN;
        }
    }

    public static boolean isSystemFolder(String label) {
        if (label == null) {
            return false;
        }
        return label.equalsIgnoreCase(SYSTEM_DIRECTORY);
    }

    public static boolean isGeneratedFolder(String label) {
        if (label == null) {
            return false;
        }
        return ERepositoryObjectType.GENERATED != null
                && label.equalsIgnoreCase(new Path(ERepositoryObjectType.GENERATED.getFolder()).lastSegment());
    }

    public static boolean isJobsFolder(String label) {
        if (label == null) {
            return false;
        }
        return ERepositoryObjectType.JOB_DOC != null
                && label.equalsIgnoreCase(new Path(ERepositoryObjectType.JOB_DOC.getFolder()).lastSegment());
    }

    /**
     * DOC tang Comment method "isJobletsFolder".
     * 
     * @param label
     * @return
     */
    public static boolean isJobletsFolder(String label) {
        if (label == null) {
            return false;
        }
        return ERepositoryObjectType.JOBLET_DOC != null
                && label.equalsIgnoreCase(new Path(ERepositoryObjectType.JOBLET_DOC.getFolder()).lastSegment());
    }
}
