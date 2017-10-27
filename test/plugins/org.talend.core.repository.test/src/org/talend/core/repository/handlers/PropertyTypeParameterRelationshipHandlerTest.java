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
public class PropertyTypeParameterRelationshipHandlerTest extends AbstractRepositoryTypeParameterRelationshipHandlerTest {

    @Override
    protected AbstractJobParameterInRepositoryRelationshipHandler createHandler() {
        return new PropertyTypeParameterRelationshipHandler();
    }

    @Override
    protected String getRepositoryTypeName() {
        return "PROPERTY:" + PropertyTypeParameterRelationshipHandler.PROPERTY_PROPERTY_TYPE; //$NON-NLS-1$
    }

    @Override
    protected String getRepositoryTypeValueName() {
        return "PROPERTY:" + PropertyTypeParameterRelationshipHandler.PROPERTY_REPOSITORY_PROPERTY_TYPE; //$NON-NLS-1$
    }

    @Override
    protected String getRepositoryRelationType() {
        return RelationshipItemBuilder.PROPERTY_RELATION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractRepositoryTypeParameterRelationshipHandlerTest#
     * getRepositoryTypeValuesValue()
     */
    @Override
    protected String getRepositoryTypeValuesValue() {
        return "ABCyVUx8NF4EewOtnVeZxqg";
    }
}
