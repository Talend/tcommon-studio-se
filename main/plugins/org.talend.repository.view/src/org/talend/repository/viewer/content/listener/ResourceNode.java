// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

    protected void setPath(String path) {
        this.path = path;
    }

    protected IRepositoryNode getTopNode() {
        return this.topNode;
    }

    protected void setTopNode(IRepositoryNode topNode) {
        this.topNode = topNode;
    }

    protected String getTopNodePath() {
        return this.topNodePath;
    }

    protected void setTopNodePath(String topNodePath) {
        this.topNodePath = topNodePath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
        result = prime * result + ((this.topNodePath == null) ? 0 : this.topNodePath.hashCode());
        return result;
    }

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
