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
package org.talend.core.repository;

import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

/**
 * ggu class global comment. Detailled comment
 */
public interface IRepositoryComponentDndFilter {

    public String getRepositoryType(Item item, ERepositoryObjectType type);

    /**
     * if true, will add this component to list; if false, will ignore it.
     */
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType);

    /**
     * 
     * if true, and have existed in list, will remove it; if false, will ignore it.
     */
    public boolean except(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType);

    public List<IComponent> sort(List<IComponent> components, Item item, ERepositoryObjectType type, RepositoryNode seletetedNode);
}
