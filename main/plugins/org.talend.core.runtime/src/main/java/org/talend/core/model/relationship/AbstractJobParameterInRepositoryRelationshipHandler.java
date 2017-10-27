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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractJobParameterInRepositoryRelationshipHandler extends AbstractJobParameterRelationshipHandler {

    public static final String IN_REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.relationship.AbstractParameterRelationshipHandler#collect(java.util.Map,
     * java.util.Map)
     */
    @Override
    protected Set<Relation> collect(Map<String, ElementParameterType> parametersMap, Map<?, ?> options) {
        Set<Relation> relationSet = new HashSet<Relation>();

        for (ElementParameterType paramType : parametersMap.values()) {
            if (paramType.getName().endsWith(":" + getRepositoryTypeName())) { //$NON-NLS-1$
                String name = paramType.getName().split(":")[0]; //$NON-NLS-1$
                ElementParameterType repositoryTypeParam = parametersMap.get(name + ":" //$NON-NLS-1$
                        + getRepositoryTypeName());
                if (repositoryTypeParam != null && IN_REPOSITORY.equals(repositoryTypeParam.getValue())) {
                    ElementParameterType repositoryTypeValueParam = parametersMap.get(name + ":" //$NON-NLS-1$
                            + getRepositoryTypeValueName());
                    if (repositoryTypeValueParam != null) {
                        String repositoryIdOrValue = repositoryTypeValueParam.getValue();
                        if (StringUtils.isNotEmpty(repositoryIdOrValue)) {
                            Relation addedRelation = new Relation();
                            addedRelation.setId(repositoryIdOrValue);
                            addedRelation.setType(getRepositoryRelationType());
                            addedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
                            relationSet.add(addedRelation);
                        }
                    }

                }
            }
        }

        return relationSet;
    }

    /**
     * Something like PROPERTY_TYPE
     */
    protected abstract String getRepositoryTypeName();

    /**
     * Something like REPOSITORY_PROPERTY_TYPE
     */
    protected abstract String getRepositoryTypeValueName();

    /**
     * get the type of relation type
     */
    protected abstract String getRepositoryRelationType();
}
