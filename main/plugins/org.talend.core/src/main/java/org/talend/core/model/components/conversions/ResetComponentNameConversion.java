// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

public class ResetComponentNameConversion extends RenameComponentConversion{

    private String newName;
    
    private String oldName;

    public ResetComponentNameConversion(String newName, String oldName) {
        super(newName);
        this.newName = newName;
        this.oldName = oldName;
    }

    public void transform(NodeType node) {
        node.setComponentName(newName);
        ProcessType item = (ProcessType) node.eContainer();
        String oldNodeUniqueName = ComponentUtilities.getNodeUniqueName(node);
        String newNodeUniqueName = oldNodeUniqueName.replaceAll(oldName, newName);
        ComponentUtilities.setNodeUniqueName(node, newNodeUniqueName);
        replaceAllInAllNodesParameterValue(item, oldNodeUniqueName, newNodeUniqueName);
    }

}
