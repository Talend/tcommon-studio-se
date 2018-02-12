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
package org.talend.core.model.relationship;

import java.util.Collection;
import java.util.Collections;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractJobItemRelationshipHandler extends AbstractItemRelationshipHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.AbstractRelationshipHandler#valid(org.talend.core.model.properties.Item)
     */
    @Override
    public boolean valid(Item baseItem) {
        if (baseItem instanceof ProcessItem) {
            return true;
        }
        if (baseItem instanceof JobletProcessItem) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.relationship.AbstractItemRelationshipHandler#getBaseItemType(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public String getBaseItemType(Item baseItem) {
        if (baseItem instanceof ProcessItem) {
            return RelationshipItemBuilder.JOB_RELATION;
        }
        if (baseItem instanceof JobletProcessItem) {
            return RelationshipItemBuilder.JOBLET_RELATION;
        }
        return null;

    }

    protected ProcessType getProcessType(Item baseItem) {
        if (baseItem instanceof ProcessItem) {
            return ((ProcessItem) baseItem).getProcess();
        }
        if (baseItem instanceof JobletProcessItem) {
            return ((JobletProcessItem) baseItem).getJobletProcess();
        }
        return null;
    }

    @Override
    public Collection<ERepositoryObjectType> getSupportReoObjTypes(String relationType) {
        if (RelationshipItemBuilder.JOB_RELATION.equals(relationType)) {
            return ERepositoryObjectType.getAllTypesOfProcess();
        } else if (RelationshipItemBuilder.JOBLET_RELATION.equals(relationType)) {
            return ERepositoryObjectType.getAllTypesOfJoblet();
        } else {
            return Collections.emptySet();
        }
    }

}
