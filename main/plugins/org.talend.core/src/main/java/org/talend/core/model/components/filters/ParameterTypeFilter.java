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
package org.talend.core.model.components.filters;

import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

public class ParameterTypeFilter implements IComponentFilter {

    private String parameterTypeName;

    public ParameterTypeFilter(String name) {
        this.parameterTypeName = name;
    }

    public boolean accept(NodeType node) {
        for (Object objElementParameter : node.getElementParameter()) {
            ElementParameterType elementParameterType = (ElementParameterType) objElementParameter;
            if (elementParameterType.getName().equals(parameterTypeName)) {
                return true;
            }
        }
        return false;
    }
}
