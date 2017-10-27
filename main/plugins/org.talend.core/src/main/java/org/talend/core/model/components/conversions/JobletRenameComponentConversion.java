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
package org.talend.core.model.components.conversions;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public class JobletRenameComponentConversion implements IComponentConversion {

    private String newName;

    public JobletRenameComponentConversion(String newName) {
        super();
        this.newName = newName;
    }

    public void transform(NodeType node) {
        // TODO Auto-generated method stub
        node.setComponentName(newName);
        ProcessType item = (ProcessType) node.eContainer();
        String oldNodeUniqueName = ComponentUtilities.getNodeUniqueName(node);
        String newNodeUniqueName = ComponentUtilities.generateUniqueNodeName(newName, item);
        replaceAllInAllNodesParameterValue(item, oldNodeUniqueName, newNodeUniqueName);
    }

    private static void replaceAllInAllNodesParameterValue(ProcessType item, String oldName, String newName) {
        for (Object o : item.getNode()) {
            NodeType nt = (NodeType) o;
            ComponentUtilities.replaceInNodeParameterValue(nt, oldName, newName);
            EList metaList = nt.getMetadata();
            if (metaList != null) {
                if (!metaList.isEmpty()) {
                    MetadataType firstMeta = (MetadataType) metaList.get(0);
                    if (firstMeta.getName().equals(oldName)) {
                        firstMeta.setName(newName);
                    }
                }
            }
        }
        for (Object o : item.getConnection()) {
            ConnectionType currentConnection = (ConnectionType) o;
            if (currentConnection.getSource().equals(oldName)) {
                currentConnection.setSource(newName);
            }
            if (currentConnection.getTarget().equals(oldName)) {
                currentConnection.setTarget(newName);
            }
            if (currentConnection.getMetaname().equals(oldName)) {
                currentConnection.setMetaname(newName);
            }
        }
    }

    public String getNewName() {
        return this.newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
