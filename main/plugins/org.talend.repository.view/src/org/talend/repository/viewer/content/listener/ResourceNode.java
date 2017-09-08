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
package org.talend.repository.viewer.content.listener;

import org.talend.repository.model.IRepositoryNode;


/**
 * created by nrousseau on Jun 27, 2016
 * Detailled comment
 *
 */
public class ResourceNode {

    private String path;
    
    private IRepositoryNode topNode;
    
    private String topNodePath;

    
    /**
     * Getter for path.
     * @return the path
     */
    protected String getPath() {
        return this.path;
    }

    
    /**
     * Sets the path.
     * @param path the path to set
     */
    protected void setPath(String path) {
        this.path = path;
    }

    
    /**
     * Getter for topNode.
     * @return the topNode
     */
    protected IRepositoryNode getTopNode() {
        return this.topNode;
    }

    
    /**
     * Sets the topNode.
     * @param topNode the topNode to set
     */
    protected void setTopNode(IRepositoryNode topNode) {
        this.topNode = topNode;
    }
    
    /**
     * Getter for topNodePath.
     * @return the topNodePath
     */
    protected String getTopNodePath() {
        return this.topNodePath;
    }


    
    /**
     * Sets the topNodePath.
     * @param topNodePath the topNodePath to set
     */
    protected void setTopNodePath(String topNodePath) {
        this.topNodePath = topNodePath;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
        result = prime * result + ((this.topNodePath == null) ? 0 : this.topNodePath.hashCode());
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceNode other = (ResourceNode) obj;
        if (this.path == null) {
            if (other.path != null)
                return false;
        } else if (!this.path.equals(other.path))
            return false;
        if (this.topNodePath == null) {
            if (other.topNodePath != null)
                return false;
        } else if (!this.topNodePath.equals(other.topNodePath))
            return false;
        return true;
    }
}
