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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.datatools.xml.utils.ATreeNode;

/**
 * created by cmeng on Jul 1, 2015 Detailled comment
 *
 */
public abstract class AbstractTreePopulator {

    protected String filePath;

    protected static int limit;

    abstract public boolean populateTree(String filePath, ATreeNode treeNode);

    abstract public boolean populateTree(String filePath, ATreeNode treeNode, String selectedEntity);

    abstract public void configureDefaultTreeViewer();

    abstract public TreeItem getTreeItem(String absolutePath);

    abstract public String getAbsoluteXPath(TreeItem treeItem);

    /**
     * Getter for filePath.
     * 
     * @return the filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    public static int getMaximumRowsToPreview() {
        return RepositoryManagerHelper.getMaximumRowsToPreview();
    }

    /**
     * Sets the limit.
     * 
     * @param limit the limit to set
     */
    public void setLimit(int lit) {
        limit = lit;
    }

    /**
     * Getter for limit.
     * 
     * @return the limit
     */
    public static int getLimit() {
        return limit;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
