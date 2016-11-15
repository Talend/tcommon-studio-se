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
package org.talend.core.ui.utils;

import org.talend.core.model.metadata.IDatabaseConstant;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;

public class MetaDataDialogUtil {

    public static final String PROPERTY = "PROPERTY"; //$NON-NLS-1$

    public static boolean isRedShiftNode(INode node) {
        if (node != null) {
            IElementParameter param = node.getElementParameter(PROPERTY);
            if (param != null && param.getFieldType() == EParameterFieldType.PROPERTY_TYPE
                    && IDatabaseConstant.REDSHIFT.equals(param.getRepositoryValue())) {
                return true;
            }
        }
        return false;
    }
}
