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
import java.util.Map;
import java.util.Set;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IItemRelationshipHandler {

    /**
     * find the relation from the item, can any items. for example, process item
     * 
     * @param item
     * @return
     */
    Map<Relation, Set<Relation>> find(Item baseItem);

    /**
     * If not valid, no relations to return
     * 
     * @param baseItem
     * @return
     */
    boolean valid(Item baseItem);

    /**
     * get the base item's relation type
     * 
     * @return
     */
    String getBaseItemType(Item baseItem);

    Collection<ERepositoryObjectType> getSupportReoObjTypes(String relationType);

}
