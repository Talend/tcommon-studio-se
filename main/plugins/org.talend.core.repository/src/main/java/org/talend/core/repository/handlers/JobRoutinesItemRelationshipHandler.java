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
package org.talend.core.repository.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.talend.core.model.properties.Item;
import org.talend.core.model.relationship.AbstractJobItemRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
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
            for (Object o : processType.getParameters().getRoutinesParameter()) {
                RoutinesParameterType itemInfor = (RoutinesParameterType) o;

                Relation addedRelation = new Relation();
                addedRelation.setId(itemInfor.getName());
                addedRelation.setType(RelationshipItemBuilder.ROUTINE_RELATION);
                addedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
                relationSet.add(addedRelation);

            }
        }

        return relationSet;
    }

}
