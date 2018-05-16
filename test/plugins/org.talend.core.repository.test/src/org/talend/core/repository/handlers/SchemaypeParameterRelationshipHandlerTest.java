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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class SchemaypeParameterRelationshipHandlerTest extends AbstractRepositoryTypeParameterRelationshipHandlerTest {

    @Override
    protected AbstractJobParameterInRepositoryRelationshipHandler createHandler() {
        return new SchemaTypeParameterRelationshipHandler();
    }

    @Override
    protected String getRepositoryTypeName() {
        return "SCHEMA:SCHEMA_TYPE";
    }

    @Override
    protected String getRepositoryTypeValueName() {
        return "SCHEMA:REPOSITORY_SCHEMA_TYPE";
    }

    @Override
    protected String getRepositoryRelationType() {
        return RelationshipItemBuilder.SCHEMA_RELATION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractRepositoryTypeParameterRelationshipHandlerTest#
     * getRepositoryTypeValuesValue()
     */
    @Override
    protected String getRepositoryTypeValuesValue() {
        return "ABCyVUx8NF4EewOtnVeZxqg - metadata1";
    }

    @Test
    public void testFind4OtherSchema() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType repParamType = TalendFileFactory.eINSTANCE.createElementParameterType();
        repParamType.setName("SCHEMA_OTHER:SCHEMA_TYPE");
        repParamType.setValue(AbstractJobParameterInRepositoryRelationshipHandler.IN_REPOSITORY);
        parametersMap.put(repParamType.getName(), repParamType);

        ElementParameterType repValueParamType = TalendFileFactory.eINSTANCE.createElementParameterType();
        repValueParamType.setName("SCHEMA_OTHER:REPOSITORY_SCHEMA_TYPE");
        repValueParamType.setValue(getRepositoryTypeValuesValue());
        parametersMap.put(repValueParamType.getName(), repValueParamType);

        checkAndTestParameters(parametersMap);

    }
}
