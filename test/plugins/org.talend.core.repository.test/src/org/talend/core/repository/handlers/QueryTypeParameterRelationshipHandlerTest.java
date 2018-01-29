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

import org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler;
import org.talend.core.model.relationship.RelationshipItemBuilder;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class QueryTypeParameterRelationshipHandlerTest extends AbstractRepositoryTypeParameterRelationshipHandlerTest {

    @Override
    protected AbstractJobParameterInRepositoryRelationshipHandler createHandler() {
        return new QueryTypeParameterRelationshipHandler();
    }

    @Override
    protected String getRepositoryTypeName() {
        return "QUERYSTORE:QUERYSTORE_TYPE";
    }

    @Override
    protected String getRepositoryTypeValueName() {
        return "QUERYSTORE:REPOSITORY_QUERYSTORE_TYPE";
    }

    @Override
    protected String getRepositoryRelationType() {
        return RelationshipItemBuilder.QUERY_RELATION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractRepositoryTypeParameterRelationshipHandlerTest#
     * getRepositoryTypeValuesValue()
     */
    @Override
    protected String getRepositoryTypeValuesValue() {
        return "ABCyVUx8NF4EewOtnVeZxqg - select";
    }
}
