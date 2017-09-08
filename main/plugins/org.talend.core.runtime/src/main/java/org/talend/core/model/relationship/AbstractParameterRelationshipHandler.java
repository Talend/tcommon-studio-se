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
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractParameterRelationshipHandler implements IParameterRelationshipHandler {

    protected abstract boolean valid(Item baseItem);

    protected abstract String getBaseItemType(Item baseItem);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.IParameterRelationshipHandler#find(org.talend.core.model.properties.Item,
     * java.util.Map, java.util.Map)
     */
    @Override
    public Map<Relation, Set<Relation>> find(Item baseItem, Map<String, ElementParameterType> parametersMap, Map<?, ?> options) {
        if (!valid(baseItem) || parametersMap == null || parametersMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Relation> collections = collect(parametersMap, options);
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
     * collect the relations from the parameters.
     * 
     * 
     * @param baseItem
     * @return
     */
    protected abstract Set<Relation> collect(Map<String, ElementParameterType> parametersMap, Map<?, ?> options);
}
