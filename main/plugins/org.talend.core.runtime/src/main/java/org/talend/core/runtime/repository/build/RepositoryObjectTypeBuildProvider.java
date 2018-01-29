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
package org.talend.core.runtime.repository.build;

import java.util.Map;

import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class RepositoryObjectTypeBuildProvider extends AbstractBuildProvider {

    @Override
    public boolean valid(Map<String, Object> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return false;
        }

        ERepositoryObjectType type = null;

        Object object = parameters.get(PROCESS);
        if (object != null && object instanceof IProcess2) {
            Property property = ((IProcess2) object).getProperty();
            if (property != null) {
                type = ERepositoryObjectType.getType(property);
            }
        }

        if (type == null) {
            object = parameters.get(ITEM);
            if (object != null && object instanceof Item) {
                Property property = ((Item) object).getProperty();
                if (property != null) {
                    type = ERepositoryObjectType.getType(property);
                }
            }
        }
        if (type == null) {
            object = parameters.get(REPOSITORY_OBJECT);
            if (object != null && object instanceof IRepositoryViewObject) {
                type = ((IRepositoryViewObject) object).getRepositoryObjectType();
            }
        }

        if (type != null && type.equals(getObjectType())) {
            return true;
        }

        return false;
    }

    protected abstract ERepositoryObjectType getObjectType();
}
