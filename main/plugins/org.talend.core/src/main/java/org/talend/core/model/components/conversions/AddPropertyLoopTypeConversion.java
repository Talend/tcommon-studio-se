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
package org.talend.core.model.components.conversions;

import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class AddPropertyLoopTypeConversion implements IComponentConversion {

    private String field = "CLOSED_LIST"; //$NON-NLS-1$

    private String name = "LOOPTYPE"; //$NON-NLS-1$

    public AddPropertyLoopTypeConversion() {
        super();
    }

    public void transform(NodeType node) {
        ComponentUtilities.addNodeProperty(node, name, field);
        ComponentUtilities.setNodeValue(node, name, "FORLOOP"); //$NON-NLS-1$
    }

}
