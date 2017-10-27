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
package org.talend.core.model.relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.properties.Item;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractItemRelationshipHandler implements IItemRelationshipHandler {

    /**
     * If not valid, no relations to return
     * 
     * @param baseItem
     * @return
     */
    protected abstract boolean valid(Item baseItem);

    /**
     * get the base item's relation type
     * 
     * @return
     */
    protected abstract String getBaseItemType(Item baseItem);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.IItemRelationshipHandler#find(org.talend.core.model.properties.Item)
     */
    @Override
    public Map<Relation, Set<Relation>> find(Item baseItem) {
        if (!valid(baseItem)) {
            return Collections.emptyMap();
        }
        Set<Relation> collections = collect(baseItem);
        if (collections.isEmpty()) { // no result, so return empty
            return Collections.emptyMap();
        } else {
            Map<Relation, Set<Relation>> relationsMap = new HashMap<Relation, Set<Relation>>();

            Relation relation = new Relation();
            relation.setId(baseItem.getProperty().getId());
            relation.setType(getBaseItemType(baseItem));
            relation.setVersion(baseItem.getProperty().getVersion());
            relationsMap.put(relation, collections);

            return relationsMap;
        }

    }

    /**
     * collect the relations from the base item.
     * 
     * 
     * @param baseItem
     * @return
     */
    protected abstract Set<Relation> collect(Item baseItem);
}
