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
package org.talend.core.model.metadata;

/**
 * DOC hwang class global comment. Detailled comment
 */
public interface ISAPConstant {

    public static final String REF_TYPE = "-TYPE"; //$NON-NLS-1$

    public static final String REPOSITORY_VALUE = "SAP"; //$NON-NLS-1$

    public static final String TABLE_SCHEMAS = "SCHEMAS"; //$NON-NLS-1$

    public static final String MAPPING_INPUT = "MAPPING_INPUT"; //$NON-NLS-1$

    public static final String PARENT_ROW = "PARENT_ROW"; //$NON-NLS-1$

    public static final String SAP_FUNCTION = "SAP_FUNCTION"; //$NON-NLS-1$

    public static final String REF_ATTR_REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    public static final String FIELD_SCHEMA = "SCHEMA"; //$NON-NLS-1$

    public static final String FIELD_SAP_ITERATE_OUT_TYPE = "SAP_ITERATE_OUT_TYPE"; //$NON-NLS-1$

    public static final String FIELD_SAP_TABLE_NAME = "SAP_TABLE_NAME";//$NON-NLS-1$

    public static final String FIELD_MAPPING = "MAPPING";//$NON-NLS-1$

    public static final String TYPE = "TYPE"; //$NON-NLS-1$

    public static final String NAME = "NAME"; //$NON-NLS-1$

    public static final String CHANGING = "CHANGING";//$NON-NLS-1$

    /*
     * 
     */
    public static final String PROPERTY = "PROPERTY"; //$NON-NLS-1$

    public static final String PROPERTY_TYPE = "PROPERTY_TYPE"; //$NON-NLS-1$

    public static final String REPOSITORY_PROPERTY_TYPE = "REPOSITORY_PROPERTY_TYPE"; //$NON-NLS-1$

    public static final String PARAM_INPUT = "Input"; //$NON-NLS-1$

    public static final String PARAM_OUTPUT = "Output"; //$NON-NLS-1$

    public static final String PARAM_TEST = "Test"; //$NON-NLS-1$

    public static final String PARAM_SINGLE = "single"; //$NON-NLS-1$

    public static final String PARAM_STRUCTURE = "structure"; //$NON-NLS-1$

    public static final String PARAM_TABLE = "table"; //$NON-NLS-1$

    public static final String PARAM_TYPE_SEPARATOR = "."; //$NON-NLS-1$

    public static final String PARAM_PATH__SEPARATOR = "/"; //$NON-NLS-1$

    public static final String XSD_PARAM_EXTENSION = "xsd"; //$NON-NLS-1$

    public static final String XML_PARAM_EXTENSION = "xml"; //$NON-NLS-1$

    public static final String XML_ITEM_TAG = "item"; //$NON-NLS-1$

    public static final String SINGLE_PARAM_TABLE_NAME = "SINGLE_PARAM";

    public static final String NAMESAPCE_RUI = "http://SAPConnections";

    public static final String SCHEMA_SUFIX = "_Schema";

    public static final String INPUT_XML_META_NAME = PARAM_INPUT + SCHEMA_SUFIX;

    public static final String OUTPUT_XML_META_NAME = PARAM_OUTPUT + SCHEMA_SUFIX;

    // Hana database properties
    public static final String PROP_USE_HANA = "db.useHana";//$NON-NLS-1$

    public static final String PROP_DB_HOST = "db.host";//$NON-NLS-1$

    public static final String PROP_DB_PORT = "db.port";//$NON-NLS-1$

    public static final String PROP_DB_SCHEMA = "db.schema";//$NON-NLS-1$

    public static final String PROP_DB_USERNAME = "db.username";//$NON-NLS-1$

    public static final String PROP_DB_PASSWORD = "db.password";//$NON-NLS-1$
}
