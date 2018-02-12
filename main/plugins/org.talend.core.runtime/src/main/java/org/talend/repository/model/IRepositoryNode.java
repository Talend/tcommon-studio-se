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
package org.talend.repository.model;

import java.util.List;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface IRepositoryNode {

    /**
     * Represents differents type of node.<br/>
     * 
     * $Id: RepositoryNode.java 914 2006-12-08 08:28:53 +0000 (鏄熸湡浜� 08 锟�?浜屾湀 2006) bqian $
     * 
     */
    public enum ENodeType {
        SYSTEM_FOLDER,
        // Represents a folder created by the system (Process, Documentation). Thoses folders cannot be removed, moved
        // or rename by users.
        STABLE_SYSTEM_FOLDER,
        // Same as SYSTEM_FOLDER, but users cannot create sub-folders.
        SIMPLE_FOLDER,
        // Represents a folder created by a user. Those folders can be rename, moved or removed.
        REPOSITORY_ELEMENT,
        // Represents an object such as a process or a table.
        REFERENCED_PROJECT,
        // Represents a referenced project;
        RELATED_FOLDER,
        // Represents a folder related with others;
        TDQ_REPOSITORY_ELEMENT;
    }

    /**
     * 
     * Represents available properties on a node.<br/>
     * 
     * $Id: RepositoryNode.java 914 2006-12-08 08:28:53 +0000 (鏄熸湡浜� 08 锟�?浜屾湀 2006) bqian $
     * 
     */
    public enum EProperties {
        LABEL,
        CONTENT_TYPE;
    }

    public IProjectRepositoryNode getRoot();

    public RepositoryNode getParent();

    public List<IRepositoryNode> getChildren();

    public IRepositoryViewObject getObject();

    public ERepositoryObjectType getContentType();

    public String getLabel();

    public ENodeType getType();

    public ERepositoryObjectType getObjectType();

    public String getId();

    public Object getProperties(EProperties key);

    public boolean hasChildren();

    public boolean isBin();

    public void dispose();

    public boolean isDisposed();

    /**
     * 
     * if withDeleted is 'true',will get all children which contain logical delete elements.
     * 
     * @param withDeleted
     * @return
     */
    public List<IRepositoryNode> getChildren(boolean withDeleted);

    /**
     * get the display text show in the DQ Repostory view(now just used for DQ).
     * 
     * @return
     */
    public String getDisplayText();
}
