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
package org.talend.repository.items.importexport.handlers.model;

import org.eclipse.core.runtime.IPath;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by wchen on Aug 3, 2016 Detailled comment
 *
 */
public class EmptyFolderImportItem extends ImportItem {

    /**
     * DOC wchen ImportFolderItem constructor comment.
     * 
     * @param path
     */
    public EmptyFolderImportItem(IPath path) {
        super(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.model.ImportItem#getRepositoryType()
     */
    @Override
    public ERepositoryObjectType getRepositoryType() {
        return repositoryType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.model.ImportItem#getLabel()
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * DOC wchen Comment method "setLabel".
     */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode())
                + ((this.label == null) ? 0 : this.label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ImportItem)) {
            return false;
        }
        ImportItem other = (ImportItem) obj;
        if (this.path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!this.path.equals(other.path)) {
            return false;
        }
        if (this.label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!this.label.equals(other.label)) {
            return false;
        }
        return true;
    }
}
