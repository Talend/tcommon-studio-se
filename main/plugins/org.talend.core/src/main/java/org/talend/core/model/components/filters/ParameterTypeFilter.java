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
package org.talend.core.model.components.filters;

import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ParameterTypeFilter implements IComponentFilter {

    private String parameterTypeName;

    /**
     * yzhang ParameterTypeFilter constructor comment.
     */
    public ParameterTypeFilter(String name) {
        this.parameterTypeName = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.filters.IComponentFilter#accept(org.talend.designer.core.model.utils.emf.talendfile.NodeType)
     */
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
