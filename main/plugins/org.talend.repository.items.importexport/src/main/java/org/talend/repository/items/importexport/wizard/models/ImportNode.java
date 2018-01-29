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
package org.talend.repository.items.importexport.wizard.models;

import java.util.ArrayList;
import java.util.List;

import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class ImportNode implements Comparable<ImportNode> {

    private ImportNode parentNode;

    private ProjectImportNode projectNode;

    private List<ImportNode> children = new ArrayList<ImportNode>();

    private boolean visible;

    protected ImportItem itemRecord;

    public ImportNode() {
        super();
        this.visible = true; // default, all should be visible.
    }

    public abstract String getName();

    public String getDisplayLabel() {
        return getName();
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    public List<ImportNode> getChildren() {
        return this.children;
    }

    public void addChild(ImportNode node) {
        if (node != null) {
            this.children.add(node);
            node.setParentNode(this);
            node.setProjectNode(this.getProjectNode());
        }
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public ImportNode getParentNode() {
        return this.parentNode;
    }

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    protected void setParentNode(ImportNode parent) {
        this.parentNode = parent;
    }

    public ProjectImportNode getProjectNode() {
        return this.projectNode;
    }

    protected void setProjectNode(ProjectImportNode projectNode) {
        this.projectNode = projectNode;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isVisibleWithChildren() {
        if (isVisible()) {
            List<ImportNode> children2 = this.getChildren();
            if (children2.isEmpty()) {
                return true; // if leaf node, just return the visible.
            }
            for (ImportNode node : children2) {
                if (node.isVisibleWithChildren()) { // if on children is visible, current should be visible also.
                    return true;
                }
            }
            // if all children is not visible, current is not visible too.
            return false;
        }
        return false;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImportNode other = (ImportNode) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ImportNode anotherNode) {
        final String label = this.getName();
        if (label == null) {
            return -1;
        }
        final String label2 = anotherNode.getName();
        if (label2 == null) {
            return 1;
        }
        return label.compareTo(label2);
    }

    public ImportItem getItemRecord() {
        return this.itemRecord;
    }

    /**
     * Sets the itemRecord.
     * 
     * @param itemRecord the itemRecord to set
     */
    public void setItemRecord(ImportItem itemRecord) {
        this.itemRecord = itemRecord;
    }

}
