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
package org.talend.core.model.param;

/**
 * cli class global comment. Detailled comment
 */
public enum EParamertPrefix {
    PROPERTY_TYPE_IMPLICIT_CONTEXT, // process extra param
    PROPERTY_TYPE, // process stats and logs param
    PROPERTY, // node
    QUERYSTORE, // query
    SCHEMA, // schema
    ENCODING, // Encoding
    VALIDATION_RULE, // validation rule
    ;

    public String getName() {
        return name();
    }
}
