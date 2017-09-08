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
package org.talend.core.model.repository;

import org.talend.core.model.properties.Item;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * ggu class global comment. Detailled comment
 */
public interface IRepositoryEditorInput {

    public void setItem(Item item);

    public Item getItem();

    public String getId();

    public String getVersion();

    public void setReadOnly(boolean readOnly);

    public boolean isReadOnly();

    public RepositoryNode getRepositoryNode();

    public void setRepositoryNode(IRepositoryNode repositoryNode);
}
