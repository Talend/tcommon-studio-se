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
package org.talend.repository.tester;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ycbai on 2013-7-24 Detailled comment
 * 
 */
public class SchemaColumnFolderNodeTester extends SubNodeTester {

    private static final String IS_SCHEMA_COLUMN_FOLDER = "isSchemaColumnFolder"; //$NON-NLS-1$

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
            if (IS_SCHEMA_COLUMN_FOLDER.equals(property)) {
                return isSchemaColumnFolder(repositoryNode);
            }
        }
        return null;
    }

    public boolean isSchemaColumn(RepositoryNode repositoryNode) {
        return isTypeNode(repositoryNode, ERepositoryObjectType.METADATA_CON_COLUMN);
    }

    public boolean isSchemaColumnFolder(RepositoryNode repositoryNode) {
        if (repositoryNode.getType() == IRepositoryNode.ENodeType.STABLE_SYSTEM_FOLDER) {
            if (repositoryNode.getChildren().size() > 0) {
                return isSchemaColumn((RepositoryNode) repositoryNode.getChildren().get(0));
            }
            return isTypeNode(repositoryNode.getParent(), ERepositoryObjectType.METADATA_CON_TABLE);
        }
        return false;
    }

}
