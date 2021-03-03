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
package org.talend.core.repository.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.properties.Item;
import org.talend.core.model.relationship.AbstractJobItemRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobRoutinesItemRelationshipHandler extends AbstractJobItemRelationshipHandler {

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.core.model.relationship.AbstractItemRelationshipHandler#collect(org.talend.core.model.properties.Item)
     */
    @Override
    protected Set<Relation> collect(Item baseItem) {
        ProcessType processType = getProcessType(baseItem);
        if (processType == null) {
            return Collections.emptySet();
        }
        Set<Relation> relationSet = new HashSet<Relation>();

        if (processType.getParameters() != null && processType.getParameters().getRoutinesParameter() != null) {
            Map<String, String> currentSystemRoutinesMap = RelationshipItemBuilder.getInstance().getCurrentSystemRoutinesMap();
            for (Object o : processType.getParameters().getRoutinesParameter()) {
                RoutinesParameterType itemInfor = (RoutinesParameterType) o;
                if (itemInfor.getName() != null && currentSystemRoutinesMap.containsValue(itemInfor.getName())) {
                    // exclude system routines relation
                    continue;
                }

                Relation addedRelation = new Relation();
                if (ERepositoryObjectType.ROUTINESJAR != null
                        && ERepositoryObjectType.ROUTINESJAR.getType().equals(itemInfor.getType())) {
                    addedRelation.setId(itemInfor.getId());
                    addedRelation.setType(RelationshipItemBuilder.ROUTINES_JAR_RELATION);
                } else if (ERepositoryObjectType.BEANSJAR != null
                        && ERepositoryObjectType.BEANSJAR.getType().equals(itemInfor.getType())) {
                    addedRelation.setId(itemInfor.getId());
                    addedRelation.setType(RelationshipItemBuilder.BEANS_JAR_RELATION);
                } else {
                    addedRelation.setId(itemInfor.getName());
                    addedRelation.setType(RelationshipItemBuilder.ROUTINE_RELATION);
                }
                addedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
                relationSet.add(addedRelation);

            }
        }

        return relationSet;
    }

}
