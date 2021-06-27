// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.items.importexport.handlers.imports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.relationship.Relation;

/**
 * DOC jding  class global comment. Detailled comment
 */
public class ImportDependencyRelationsHelper {

    private static final ImportDependencyRelationsHelper instance = new ImportDependencyRelationsHelper();

    public static ImportDependencyRelationsHelper getInstance() {
        return instance;
    }

    private Map<String, Map<Relation, Set<Relation>>> projectRelationsCache = new HashMap<String, Map<Relation, Set<Relation>>>();

    public Map<Relation, Set<Relation>> getImportItemsRelations(String projectFilePath) {
        Map<Relation, Set<Relation>> relationsMap = new HashMap<Relation, Set<Relation>>();
        if (projectRelationsCache.get(projectFilePath) != null) {
            relationsMap = projectRelationsCache.get(projectFilePath);
        }
        return relationsMap;
    }

    public void loadRelations(String projectFilePath, List itemRelationsList) {
        if (projectRelationsCache.get(projectFilePath) != null) {
            return;
        }

        projectRelationsCache.put(projectFilePath, new HashMap<Relation, Set<Relation>>());
        Map<Relation, Set<Relation>> relationsMap = projectRelationsCache.get(projectFilePath);

        for (Object o : itemRelationsList) {
            ItemRelations relations = (ItemRelations) o;
            Relation baseItem = new Relation();

            baseItem.setId(relations.getBaseItem().getId());
            baseItem.setType(relations.getBaseItem().getType());
            baseItem.setVersion(relations.getBaseItem().getVersion());

            relationsMap.put(baseItem, new HashSet<Relation>());
            for (Object o2 : relations.getRelatedItems()) {
                ItemRelation emfRelatedItem = (ItemRelation) o2;

                Relation relatedItem = new Relation();
                relatedItem.setId(emfRelatedItem.getId());
                relatedItem.setType(emfRelatedItem.getType());
                relatedItem.setVersion(emfRelatedItem.getVersion());

                relationsMap.get(baseItem).add(relatedItem);
            }
        }
    }

    public void clear() {
        projectRelationsCache.clear();
    }
}
