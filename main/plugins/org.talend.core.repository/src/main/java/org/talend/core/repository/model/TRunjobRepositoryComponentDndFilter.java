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
package org.talend.core.repository.model;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.DefaultRepositoryComponentDndFilter;
import org.talend.core.repository.RepositoryComponentManager;
import org.talend.core.repository.RepositoryComponentSetting;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TRunjobRepositoryComponentDndFilter extends DefaultRepositoryComponentDndFilter {

    public TRunjobRepositoryComponentDndFilter() {
    }

    public String getRepositoryType(Item item, ERepositoryObjectType type) {
        RepositoryComponentSetting setting = RepositoryComponentManager.getSetting(item, type);
        if (setting != null) {
            return setting.getDefaultComponent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.IRepositoryComponentDndFilter#valid(org.talend.core.model.properties.Item,
     * org.talend.core.model.repository.ERepositoryObjectType, org.talend.repository.model.RepositoryNode,
     * org.talend.core.model.components.IComponent, java.lang.String)
     */
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        // tRunJob
        if (component != null && repositoryType != null && repositoryType.equals(component.getName())) {
            return true;
        }
        return false;
    }
}
