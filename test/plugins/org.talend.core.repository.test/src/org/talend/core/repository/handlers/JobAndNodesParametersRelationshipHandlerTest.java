// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.joblet.model.JobletFactory;
import org.talend.designer.joblet.model.JobletProcess;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobAndNodesParametersRelationshipHandlerTest {

    private JobAndNodesParametersRelationshipHandler handler;

    @Before
    public void setUp() {
        handler = new JobAndNodesParametersRelationshipHandler();
    }

    @Test
    public void testValid4ProcessItem() {
        // can't use mock item. because "B instanceof A"
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        Assert.assertTrue(handler.valid(item));
    }

    @Test
    public void testValid4JobletProcessItem() {
        JobletProcessItem item = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        Assert.assertTrue(handler.valid(item));
    }

    @Test
    public void testGetBaseItemType4ProcessItem() {
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, handler.getBaseItemType(item));
    }

    @Test
    public void testGetBaseItemType4JobletProcessItem() {
        JobletProcessItem item = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        Assert.assertEquals(RelationshipItemBuilder.JOBLET_RELATION, handler.getBaseItemType(item));
    }

    @Test
    public void testGetProcessType4ProcessItem() {
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        ProcessType procType = TalendFileFactory.eINSTANCE.createProcessType();
        item.setProcess(procType);
        Assert.assertEquals(procType, handler.getProcessType(item));
    }

    @Test
    public void testGetProcessType4JobletProcessItem() {
        JobletProcessItem item = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        JobletProcess procType = JobletFactory.eINSTANCE.createJobletProcess();
        item.setJobletProcess(procType);
        Assert.assertEquals(procType, handler.getProcessType(item));
    }

    @Test
    public void testFind4Invalid() {
        Item item = mock(Item.class); // use invalid item to test
        Map<Relation, Set<Relation>> relations = handler.find(item);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());

        item = PropertiesFactory.eINSTANCE.createProcessItem(); // no process type
        relations = handler.find(item);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());
    }

    @SuppressWarnings("nls")
    @Test
    public void testFind4NoParameters() {
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        item = spy(item);

        Property property = mock(Property.class);
        when(item.getProperty()).thenReturn(property);
        when(property.getId()).thenReturn(AbstractProcessItemRelationshipHandlerTest.ITEM_ID);
        when(property.getVersion()).thenReturn("0.1");

        ProcessType procType = mock(ProcessType.class);
        when(item.getProcess()).thenReturn(procType);

        Map<Relation, Set<Relation>> relations = handler.find(item);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.values().isEmpty());

    }

    @Test
    public void testFindParameters() {
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setId(AbstractProcessItemRelationshipHandlerTest.ITEM_ID);
        property.setVersion("0.1");
        property.setLabel("test");
        item.setProperty(property);
        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        item.setProcess(process);

        NodeType node = TalendFileFactory.eINSTANCE.createNodeType();
        node.setComponentName("tRunJob");
        ElementParameterType param = TalendFileFactory.eINSTANCE.createElementParameterType();
        param.setName("PROCESS:PROCESS_TYPE_VERSION");
        param.setValue(RelationshipItemBuilder.LATEST_VERSION);
        String relatedId = "project" + ":" + ProxyRepositoryFactory.getInstance().getNextId();
        ElementParameterType param1 = TalendFileFactory.eINSTANCE.createElementParameterType();
        param1.setName("PROCESS:PROCESS_TYPE_PROCESS");
        param1.setValue(relatedId);

        node.getElementParameter().add(param);
        node.getElementParameter().add(param1);
        process.getNode().add(node);

        Map<Relation, Set<Relation>> relationsMap = handler.find(item);
        Assert.assertNotNull(relationsMap);
        Assert.assertTrue(relationsMap.values().size() > 0);
        Relation baseRelation = relationsMap.keySet().iterator().next();
        Assert.assertNotNull(baseRelation);
        Assert.assertEquals(property.getId(), baseRelation.getId());
        Assert.assertEquals(property.getVersion(), baseRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, baseRelation.getType());
        Set<Relation> relatedRelations = relationsMap.get(baseRelation);
        Assert.assertNotNull(relatedRelations);
        Assert.assertTrue(relatedRelations.size() > 0);
        Relation relatedRelation = relatedRelations.iterator().next();
        Assert.assertEquals(relatedId, relatedRelation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relatedRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, relatedRelation.getType());
    }
}
