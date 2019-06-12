// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.sugars;

import org.talend.utils.sugars.TypedReturnCode;

/**
 * DOC stephane class global comment. Detailled comment
 */
public class StringReturnCode extends TypedReturnCode<String> {

    public String getValue() {
        return getObject();
    }

    public void setValue(String value) {
        setObject(value);
    }
}
