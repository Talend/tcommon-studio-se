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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.relationship.AbstractJobParameterInRepositoryRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public abstract class AbstractRepositoryTypeParameterRelationshipHandlerTest extends AbstractParameterRelationshipHandlerTest {

    public static final String BUILTIN = "BUILT_IN";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractParameterRelationshipHandlerTest#createHandler()
     */
    @Override
    protected abstract AbstractJobParameterInRepositoryRelationshipHandler createHandler();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractParameterRelationshipHandlerTest#getHandler()
     */
    @Override
    protected AbstractJobParameterInRepositoryRelationshipHandler getHandler() {
        return (AbstractJobParameterInRepositoryRelationshipHandler) super.getHandler();
    }

    protected Map<String, ElementParameterType> createParameters() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType repParamType = TalendFileFactory.eINSTANCE.createElementParameterType();
        repParamType.setName(getRepositoryTypeName());
        repParamType.setValue(AbstractJobParameterInRepositoryRelationshipHandler.IN_REPOSITORY);
        parametersMap.put(repParamType.getName(), repParamType);

        ElementParameterType repValueParamType = TalendFileFactory.eINSTANCE.createElementParameterType();
        repValueParamType.setName(getRepositoryTypeValueName());
        repValueParamType.setValue(getRepositoryTypeValuesValue());
        parametersMap.put(repValueParamType.getName(), repValueParamType);

        return parametersMap;
    }

    protected abstract String getRepositoryTypeName();

    protected abstract String getRepositoryTypeValueName();

    protected abstract String getRepositoryTypeValuesValue();

    protected abstract String getRepositoryRelationType();

    @Test
    public void testFind4BuiltIn() {
        Map<String, ElementParameterType> paramsMap = createParameters();
        ElementParameterType repParam = paramsMap.get(getRepositoryTypeName());
        if (repParam != null) {
            repParam.setValue(BUILTIN);
            // repParam = spy(repParam);
            // when(repParam.getValue()).thenReturn(BUILTIN);
        }
        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), paramsMap, null);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());
    }

    @Test
    public void testFind() {
        Map<String, ElementParameterType> paramsMap = createParameters();
        checkAndTestParameters(paramsMap);
    }

    protected void checkAndTestParameters(Map<String, ElementParameterType> paramsMap) {
        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), paramsMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(ITEM_ID, processRelation.getId());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());
        Assert.assertEquals("0.1", processRelation.getVersion());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
        Assert.assertTrue(set.size() == 1);

        Relation repRelation = set.iterator().next();
        Assert.assertNotNull(repRelation);
        Assert.assertEquals(getRepositoryTypeValuesValue(), repRelation.getId());
        Assert.assertEquals(getRepositoryRelationType(), repRelation.getType());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, repRelation.getVersion());
    }

}
