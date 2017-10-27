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
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobContextItemRelationshipHandler extends AbstractJobItemRelationshipHandler {

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

        Set<String> repositoryIds = new HashSet<String>();
        for (Object o : processType.getContext()) {
            ContextType context = (ContextType) o;
            for (Object o2 : context.getContextParameter()) {
                ContextParameterType contextParam = (ContextParameterType) o2;
                if (contextParam.getRepositoryContextId() != null && contextParam.getRepositoryContextId().length() > 0) {
                    repositoryIds.add(contextParam.getRepositoryContextId());
                }
            }
            // only process one context group
            break;
        }

        Set<Relation> relationSet = new HashSet<Relation>();
        for (String repId : repositoryIds) {
            Relation addedRelation = new Relation();
            addedRelation.setId(repId);
            addedRelation.setType(RelationshipItemBuilder.CONTEXT_RELATION);
            addedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
            relationSet.add(addedRelation);
        }
        return relationSet;
    }

}
