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
import org.talend.core.model.relationship.AbstractParameterRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class ProcessTypeParameterRelationshipHandlerTest extends AbstractParameterRelationshipHandlerTest {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractParameterRelationshipHandlerTest#createHandler()
     */
    @Override
    protected AbstractParameterRelationshipHandler createHandler() {
        return new ProcessTypeParameterRelationshipHandler();
    }

    @Test
    public void testCollect4FullName() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType procTypeParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeParam.setName("PROCESS:PROCESS_TYPE_PROCESS");
        procTypeParam.setValue("_yVUx8NF4EeG5wOtnVeZ123");
        parametersMap.put(procTypeParam.getName(), procTypeParam);

        ElementParameterType procTypeVersionParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeVersionParam.setName("PROCESS:PROCESS_TYPE_VERSION");
        procTypeVersionParam.setValue("0.2");
        parametersMap.put(procTypeVersionParam.getName(), procTypeVersionParam);

        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), parametersMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.size() == 1);

        Relation childRelation = set.iterator().next();
        Assert.assertEquals("_yVUx8NF4EeG5wOtnVeZ123", childRelation.getId());
        Assert.assertEquals("0.2", childRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, childRelation.getType());
    }

    @Test
    public void testCollect4LastVersion() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType procTypeParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeParam.setName("PROCESS:PROCESS_TYPE_PROCESS");
        procTypeParam.setValue("_yVUx8NF4EeG5wOtnVeZ123");
        parametersMap.put(procTypeParam.getName(), procTypeParam);

        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), parametersMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.size() == 1);

        Relation childRelation = set.iterator().next();
        Assert.assertEquals("_yVUx8NF4EeG5wOtnVeZ123", childRelation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, childRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, childRelation.getType());
    }

    @Test
    public void testCollect4SimpleName() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType procTypeParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeParam.setName("PROCESS_TYPE_PROCESS");
        procTypeParam.setValue("_yVUx8NF4EeG5wOtnVeZ123");
        parametersMap.put(procTypeParam.getName(), procTypeParam);

        ElementParameterType procTypeVersionParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeVersionParam.setName("PROCESS_TYPE_VERSION");
        procTypeVersionParam.setValue("0.2");
        parametersMap.put(procTypeVersionParam.getName(), procTypeVersionParam);

        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), parametersMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.size() == 1);

        Relation childRelation = set.iterator().next();
        Assert.assertEquals("_yVUx8NF4EeG5wOtnVeZ123", childRelation.getId());
        Assert.assertEquals("0.2", childRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, childRelation.getType());
    }

    @Test
    public void testCollect4MultiProcess() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType procTypeParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeParam.setName("PROCESS:PROCESS_TYPE_PROCESS");
        procTypeParam.setValue("_yVUx8NF4EeG5wOtnVeZ123;_yVUx8NF4EeG5wOtnVeZ456;_yVUx8NF4EeG5wOtnVeZ789;_yVUx8NF4EeG5wOtnVeZ123");
        parametersMap.put(procTypeParam.getName(), procTypeParam);

        ElementParameterType procTypeVersionParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeVersionParam.setName("PROCESS:PROCESS_TYPE_VERSION");
        procTypeVersionParam.setValue("0.2");
        parametersMap.put(procTypeVersionParam.getName(), procTypeVersionParam);

        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), parametersMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertTrue(set.size() == 3);

        Relation childRelation = new Relation();
        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ123");
        childRelation.setVersion("0.2");
        childRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        Assert.assertTrue(set.contains(childRelation));

        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ456");
        Assert.assertTrue(set.contains(childRelation));

        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ789");
        Assert.assertTrue(set.contains(childRelation));
    }

    @Test
    public void testCollect4MultiProcessWithLastVersion() {
        Map<String, ElementParameterType> parametersMap = new HashMap<String, ElementParameterType>();

        ElementParameterType procTypeParam = TalendFileFactory.eINSTANCE.createElementParameterType();
        procTypeParam.setName("PROCESS:PROCESS_TYPE_PROCESS");
        procTypeParam.setValue("_yVUx8NF4EeG5wOtnVeZ123;_yVUx8NF4EeG5wOtnVeZ456;_yVUx8NF4EeG5wOtnVeZ789;_yVUx8NF4EeG5wOtnVeZ123");
        parametersMap.put(procTypeParam.getName(), procTypeParam);

        Map<Relation, Set<Relation>> relations = getHandler().find(getProcessItem(), parametersMap, null);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertTrue(set.size() == 3);

        Relation childRelation = new Relation();
        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ123");
        childRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
        childRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        Assert.assertTrue(set.contains(childRelation));

        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ456");
        Assert.assertTrue(set.contains(childRelation));

        childRelation.setId("_yVUx8NF4EeG5wOtnVeZ789");
        Assert.assertTrue(set.contains(childRelation));

    }

}
