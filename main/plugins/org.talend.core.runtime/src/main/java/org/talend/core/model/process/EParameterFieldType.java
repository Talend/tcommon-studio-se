// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: EParameterFieldType.java 40228 2010-04-13 05:28:25Z nrousseau $
 * 
 */
public enum EParameterFieldType {
    TEXT,
    PASSWORD,
    MEMO_SQL,
    MEMO_PERL,
    MEMO_JAVA,
    MEMO_IMPORT,
    MEMO_MESSAGE,
    CLOSED_LIST,
    OPENED_LIST,
    CHECK,
    RADIO,
    MEMO,
    SCHEMA_TYPE,
    SCHEMA_XPATH_QUERYS,
    QUERYSTORE_TYPE,
    GUESS_SCHEMA,
    PROPERTY_TYPE,
    EXTERNAL,
    FILE,
    VERSION,
    TABLE,
    DIRECTORY,
    PROCESS_TYPE,
    IMAGE,
    COLUMN_LIST,
    CONNECTION_LIST,
    PREV_COLUMN_LIST,
    CONTEXT_PARAM_NAME_LIST,
    LOOKUP_COLUMN_LIST,
    TECHNICAL, // means field not displayed directly
    ENCODING_TYPE,
    COMPONENT_LIST,
    MAPPING_TYPE,
    COLOR,
    DBTABLE,
    DATE,
    DBTYPE_LIST,
    LABEL,
    AS400_CHECK,
    MODULE_LIST,
    COMMAND,
    PALO_DIM_SELECTION,
    WSDL2JAVA,
    GENERATEGRAMMARCONTROLLER,
    GENERATE_SURVIVORSHIP_RULES_CONTROLLER, // sizhaoliu TDQ-3356
    ICON_SELECTION,
    RULE_TYPE, // hywang add for feature 6484
    TNS_EDITOR,
    BROWSE_REPORTS, // bzhou add for feature 9745
    REFRESH_REPORTS, // xqliu add for TDQ-8198
    JAVA_COMMAND,
    VALIDATION_RULE_TYPE,
    DCSCHEMA, // Datacert custom EparameterFieldType for DCSchemaController
    TREE_TABLE, // hcyi TDI-17115
    SURVIVOR_RELATION, // sizhaoliu TDQ-3724

    ROUTE_RESOURCE_TYPE, // Xiaopeng Li TESB-6226
    ROUTE_COMPONENT_TYPE,
    ROUTE_INPUT_PROCESS_TYPE, // like PROCESS_TYPE, but contains a tRouteInput inside

    REST_RESPONSE_SCHEMA_TYPE, // Used to specify the response body type

    FILE_HADOOP, // ycbai add for feature TDI-22111
    DIRECTORY_HADOOP, // ycbai add for feature TDI-22111
    BOTH_HADOOP, // ycbai add for feature TDI-22111
    HADOOP_JARS_DIALOG,
    HMAP_PATH, // wchen added for TDM
    MATCH_RULE_IMEX_CONTROLLER, // sizhaoliu TDQ-7910
    TABLE_BY_ROW, ;

    public String getName() {
        return toString();
    }

    public static EParameterFieldType getFieldTypeByName(String name) {
        for (int i = 0; i < EParameterFieldType.values().length; i++) {
            if (EParameterFieldType.values()[i].getName().equals(name)) {
                return EParameterFieldType.values()[i];
            }
        }
        return TEXT; // Default Value
    }
}
