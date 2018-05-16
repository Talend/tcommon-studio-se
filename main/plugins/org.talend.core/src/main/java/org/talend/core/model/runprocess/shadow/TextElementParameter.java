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
package org.talend.core.model.runprocess.shadow;

import org.talend.core.model.process.EParameterFieldType;

/**
 * cli class global comment. Detailled comment
 */
public class TextElementParameter extends ObjectElementParameter {

    public TextElementParameter(String name, String value) {
        super(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getField()
     */
    public EParameterFieldType getFieldType() {
        return EParameterFieldType.TEXT;
    }
}
