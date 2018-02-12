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
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.joblet.model.JobletFactory;
import org.talend.designer.joblet.model.JobletProcess;

import static org.mockito.Mockito.*;

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
        Assert.assertTrue(relations.size() > 0);

        Relation processRelation = relations.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(AbstractProcessItemRelationshipHandlerTest.ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relations.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.isEmpty()); // no relations
    }
}
