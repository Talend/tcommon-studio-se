// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class TextElementParameter extends ObjectElementParameter {

    public TextElementParameter(String name, String value) {
        super(name, value);
    }

    public EParameterFieldType getFieldType() {
        return EParameterFieldType.TEXT;
    }
}
