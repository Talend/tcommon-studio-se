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
package org.talend.repository.items.importexport.wizard.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderImportNode extends ImportNode {

    private String path;

    private Map<String, FolderImportNode> subFolders = new HashMap<String, FolderImportNode>();

    private List<ItemImportNode> items = new ArrayList<ItemImportNode>();

    public FolderImportNode(String path) {
        super();
        this.path = path;
    }

    @Override
    public String getName() {
        ImportNode parentNode = this.getParentNode();
        if (parentNode instanceof FolderImportNode) {
            return parentNode.getName() + '/' + getPath();
        } else {
            final ProjectImportNode projectNode = this.getProjectNode();
            if (projectNode != null) {
                return projectNode.getName() + '/' + getPath();
            }
        }
        return getPath();
    }

    @Override
    public String getDisplayLabel() {
        return getPath();
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public void addChild(ImportNode node) {
        if (node instanceof FolderImportNode) {
            FolderImportNode folderNode = (FolderImportNode) node;
            this.subFolders.put(folderNode.getPath(), folderNode);
        } else if (node instanceof ItemImportNode) {
            this.items.add((ItemImportNode) node);
        }
        super.addChild(node);
    }

    /**
     * Getter for subFolders.
     * 
     * @return the subFolders
     */
    protected Map<String, FolderImportNode> getSubFolders() {
        return this.subFolders;
    }

    /**
     * Getter for items.
     * 
     * @return the items
     */
    public List<ItemImportNode> getItems() {
        return this.items;
    }

}
