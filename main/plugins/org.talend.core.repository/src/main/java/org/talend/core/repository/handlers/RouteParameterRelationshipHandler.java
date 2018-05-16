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
package org.talend.core.repository.handlers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.relationship.AbstractJobParameterRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;

public class RouteParameterRelationshipHandler extends AbstractJobParameterRelationshipHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.AbstractParameterRelationshipHandler#collect(java.util.Map,
     * java.util.Map)
     */
    @SuppressWarnings("nls")
    @Override
    protected Set<Relation> collect(Map<String, ElementParameterType> parametersMap, Map<?, ?> options) {
        Set<Relation> relationSet = new HashSet<Relation>();

        // job version
        String jobVersion = RelationshipItemBuilder.LATEST_VERSION;
        ElementParameterType processVersionParamType = parametersMap.get("SELECTED_JOB_NAME:PROCESS_TYPE_VERSION");
        if (processVersionParamType == null) {
            processVersionParamType = parametersMap.get("PROCESS_TYPE_VERSION");
        }
        if (processVersionParamType != null) {
            jobVersion = processVersionParamType.getValue();
        }

        //
        ElementParameterType processParamType = parametersMap.get("SELECTED_JOB_NAME:PROCESS_TYPE_PROCESS");
        if (processParamType == null) {
            processParamType = parametersMap.get("PROCESS_TYPE_PROCESS");
        }
        if (processParamType != null) {
            String jobIds = processParamType.getValue();
            String[] jobsArr = jobIds.split(RelationshipItemBuilder.COMMA);
            for (String jobId : jobsArr) {
                if (StringUtils.isNotEmpty(jobId)) {
                    Relation addedRelation = new Relation();
                    addedRelation.setId(jobId);
                    addedRelation.setType(RelationshipItemBuilder.JOB_RELATION);
                    addedRelation.setVersion(jobVersion);
                    relationSet.add(addedRelation);
                }
            }
        }

        return relationSet;
    }

}
