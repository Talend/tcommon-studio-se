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
package org.talend.core.model.metadata;

import org.talend.core.model.process.INode;
import org.talend.core.runtime.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ColumnNameChangedExt extends ColumnNameChanged {

    private INode changedNode = null;

    public ColumnNameChangedExt(INode changedNode, String oldName, String newName) {
        super(oldName, newName);
        this.changedNode = changedNode;
    }

    public INode getChangedNode() {
        return changedNode;
    }

    public void setChangedNode(INode changedNode) {
        this.changedNode = changedNode;
    }

    @Override
    public String toString() {
        return Messages.getString(
                "ColumnNameChangedExt.columnChanged", this.changedNode.getUniqueName(), this.getOldName(), this.getNewName()); //$NON-NLS-1$
    }

}
