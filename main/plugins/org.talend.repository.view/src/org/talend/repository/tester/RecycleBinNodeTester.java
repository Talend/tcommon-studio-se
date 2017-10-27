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
package org.talend.repository.tester;

import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RecycleBinNodeTester extends AbstractNodeTester {

    private static final String IS_RECYCLE_BIN = "isRecycleBin"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.tester.AbstractNodeTester#testProperty(java.lang.Object, java.lang.String,
     * java.lang.Object[], java.lang.Object)
     */
    @Override
    protected Boolean testProperty(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) receiver;
            if (IS_RECYCLE_BIN.equals(property)) {
                return isChildOfRecycleBin(repositoryNode);
            }
        }
        return null;
    }

    public boolean isRecycleBin(RepositoryNode repositoryNode) {
        return repositoryNode.isBin();
    }

    private boolean isChildOfRecycleBin(RepositoryNode repositoryNode) {
        if (repositoryNode != null) {
            if (isRecycleBin(repositoryNode)) {
                return true;
            }
            if (isChildOfRecycleBin(repositoryNode.getParent())) {
                return true;
            }
        }
        return false;
    }
}
