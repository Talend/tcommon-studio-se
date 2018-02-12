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

import org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler;
import org.talend.core.model.relationship.RelationshipItemBuilder;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class SchemaTypeParameterRelationshipHandler extends AbstractJobParameterInRepositoryRelationshipHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler#getRepositoryTypeName()
     */
    @Override
    protected String getRepositoryTypeName() {
        return "SCHEMA_TYPE"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler#getRepositoryTypeValueName
     * ()
     */
    @Override
    protected String getRepositoryTypeValueName() {
        return "REPOSITORY_SCHEMA_TYPE"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler#getRepositoryRelationType
     * ()
     */
    @Override
    protected String getRepositoryRelationType() {
        return RelationshipItemBuilder.SCHEMA_RELATION;
    }

}
