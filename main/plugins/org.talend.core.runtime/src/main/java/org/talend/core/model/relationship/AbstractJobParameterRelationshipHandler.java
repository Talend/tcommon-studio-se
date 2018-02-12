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

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractJobParameterRelationshipHandler extends AbstractParameterRelationshipHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.AbstractRelationshipHandler#valid(org.talend.core.model.properties.Item)
     */
    @Override
    protected boolean valid(Item baseItem) {
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
    protected String getBaseItemType(Item baseItem) {
        if (baseItem instanceof ProcessItem) {
            return RelationshipItemBuilder.JOB_RELATION;
        }
        if (baseItem instanceof JobletProcessItem) {
            return RelationshipItemBuilder.JOBLET_RELATION;
        }
        return null;

    }

}
