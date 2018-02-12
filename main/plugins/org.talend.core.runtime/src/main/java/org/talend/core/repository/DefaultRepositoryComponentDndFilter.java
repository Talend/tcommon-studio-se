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
public class DefaultRepositoryComponentDndFilter implements IRepositoryComponentDndFilter {

    protected static final String MAP = "MAP";//$NON-NLS-1$

    public DefaultRepositoryComponentDndFilter() {
        super();
    }

    @Override
    public String getRepositoryType(Item item, ERepositoryObjectType type) {
        RepositoryComponentSetting setting = RepositoryComponentManager.getSetting(item, type);
        if (setting != null) {
            return setting.getRepositoryType();
        }
        return null;
    }

    @Override
    public boolean except(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        return false; // default, no except components to valid
    }

    @Override
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (component == null || repositoryType == null) {
            return false;
        }
        String componentProductname = component.getRepositoryType();

        if (componentProductname != null && repositoryType.endsWith(componentProductname)
                && validSub(item, type, seletetedNode, component, repositoryType)) {
            return true;
        }
        return false;
    }

    protected boolean validSub(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        boolean tableWithMap = true;
        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            if (component.getName().toUpperCase().endsWith(MAP)) {
                tableWithMap = false;
            }
        }
        return tableWithMap;
    }

    @Override
    public List<IComponent> sort(List<IComponent> components, Item item, ERepositoryObjectType type, RepositoryNode seletetedNode) {
        return components;
    }

}
