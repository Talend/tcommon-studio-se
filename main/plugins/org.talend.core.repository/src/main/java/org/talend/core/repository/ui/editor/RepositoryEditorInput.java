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
package org.talend.core.repository.ui.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: RepositoryEditorInput.java 1 2006-09-29 17:06:40 +0000 (æ˜ŸæœŸäº”, 29 ä¹�æœˆ 2006) nrousseau $
 * 
 */
public class RepositoryEditorInput extends FileEditorInput implements IRepositoryEditorInput {

    private Item item;

    private boolean readOnly;

    public RepositoryEditorInput(IFile file, Item item) {
        super(file);
        setItem(item);
    }

    @Override
    public String getName() {
        return item==null?null:item.getProperty().getLabel();
    }

    @Override
	public Item getItem() {
        return this.item;
    }

    @Override
	public void setItem(Item item) {
        this.item = item;
    }

    @Override
	public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
	public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
	public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepositoryEditorInput other = (RepositoryEditorInput) obj;
        if(item==null) {
        	return false;
        }
        if(!getId().equals(other.getId())){
        	return false;
        }
        if(!getVersion().equals(other.getVersion())){
        	return false;
        }
        return true;
    }

    @Override
	public int hashCode() {
        if (getItem() != null) {
            return super.hashCode() & getItem().hashCode();
        }
        return super.hashCode();
    }

    protected RepositoryNode repositoryNode;

    @Override
	public RepositoryNode getRepositoryNode() {
        return this.repositoryNode;
    }

    /**
     * DOC smallet Comment method "setNode".
     * 
     * @param node
     */
    @Override
	public void setRepositoryNode(IRepositoryNode repositoryNode) {
        if (repositoryNode != null) {
            this.repositoryNode = (RepositoryNode) repositoryNode;
        } else {
            // see bug 0005256: All folders got expanded after job creation in a folder
            this.repositoryNode = (RepositoryNode) CoreRuntimePlugin.getInstance().getRepositoryService()
                    .getRepositoryNode(getItem().getProperty().getId(), false);
        }
    }

    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        if (adapter.equals(RepositoryEditorInput.class)) {
            return this;
        }

        return super.getAdapter(adapter);
    }

    /**
     * Clear items from memory, make sure we don't keep anything in memory. Might be removed in the future if no problem
     * with memory management.
     */
    public void dispose() {
        this.repositoryNode = null;
        this.item = null;
    }

    @Override
	public String getId() {
        return item==null?null:item.getProperty().getId();
    }

    @Override
	public String getVersion() {
        return item==null?null:item.getProperty().getVersion();
    }

}
