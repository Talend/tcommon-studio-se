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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.relationship.IItemRelationshipHandler;
import org.talend.core.model.relationship.IParameterRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.relationship.RelationshipRegistryReader;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobAndNodesParametersRelationshipHandler implements IItemRelationshipHandler {

    protected boolean valid(Item baseItem) {
        if (baseItem instanceof ProcessItem) {
            return true;
        }
        if (baseItem instanceof JobletProcessItem) {
            return true;
        }
        return false;
    }

    protected String getBaseItemType(Item baseItem) {
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
        final ProcessType processType = getProcessType(baseItem);
        if (processType == null) {
            return Collections.emptyMap();
        }

        Map<Relation, Set<Relation>> relationsMap = new HashMap<Relation, Set<Relation>>();
        Set<Relation> relationSet = new HashSet<Relation>();

        Relation relation = new Relation();
        relation.setId(baseItem.getProperty().getId());
        relation.setType(getBaseItemType(baseItem));
        relation.setVersion(baseItem.getProperty().getVersion());
        relationsMap.put(relation, relationSet);

        // jobsetting parameters
        if (processType.getParameters() != null) {
            Map<String, ElementParameterType> jobParametersMap = getParameterTypesMap(processType.getParameters()
                    .getElementParameter());
            Map<String, Object> jobOptions = new HashMap<String, Object>();
            jobOptions.put(RelationshipItemBuilder.OPTION_KEY_TYPE, RelationshipItemBuilder.OPTION_TYPE_JOB);
            findRelations(baseItem, jobParametersMap, relationsMap, jobOptions);
        }
        //
        final EList nodesList = processType.getNode();
        if (nodesList != null) {
            for (Object o : nodesList) {
                if (o instanceof NodeType) {
                    NodeType node = (NodeType) o;
                    Map<String, ElementParameterType> nodeParametersMap = getParameterTypesMap(node.getElementParameter());
                    Map<String, Object> jobOptions = new HashMap<String, Object>();
                    jobOptions.put(RelationshipItemBuilder.OPTION_KEY_TYPE, RelationshipItemBuilder.OPTION_TYPE_NODE);
                    jobOptions.put(RelationshipItemBuilder.OPTION_KEY_NODE, node);
                    findRelations(baseItem, nodeParametersMap, relationsMap, jobOptions);
                }
            }
        }
        return relationsMap;

    }

    private void findRelations(Item baseItem, Map<String, ElementParameterType> jobParametersMap,
            Map<Relation, Set<Relation>> relationsMap, Map<String, Object> jobOptions) {
        IParameterRelationshipHandler[] parameterRelationshipHandlers = RelationshipRegistryReader.getInstance()
                .getParameterRelationshipHandlers();
        for (IParameterRelationshipHandler handler : parameterRelationshipHandlers) {
            Map<Relation, Set<Relation>> relations = handler.find(baseItem, jobParametersMap, jobOptions);
            RelationshipItemBuilder.mergeRelationship(relationsMap, relations);
        }
    }

    @SuppressWarnings("rawtypes")
    private Map<String, ElementParameterType> getParameterTypesMap(EList list) {
        if (list == null) {
            return Collections.emptyMap();
        }

        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();
        for (Object o : list) {
            if (o instanceof ElementParameterType) {
                ElementParameterType paramType = (ElementParameterType) o;
                parametersMap.put(paramType.getName(), paramType);
            }
        }
        return parametersMap;
    }
}
