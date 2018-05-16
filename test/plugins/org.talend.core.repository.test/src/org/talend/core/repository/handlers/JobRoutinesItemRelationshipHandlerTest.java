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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.relationship.AbstractItemRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

import static org.mockito.Mockito.*;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobRoutinesItemRelationshipHandlerTest extends AbstractProcessItemRelationshipHandlerTest {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractProcessItemRelationshipHandlerTest#createHandler()
     */
    @Override
    protected AbstractItemRelationshipHandler createHandler() {
        return new JobRoutinesItemRelationshipHandler();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractProcessItemRelationshipHandlerTest#getHandler()
     */
    @Override
    protected JobRoutinesItemRelationshipHandler getHandler() {
        return (JobRoutinesItemRelationshipHandler) super.getHandler();
    }

    @SuppressWarnings("nls")
    @Test
    public void testCollect() {
        processItem = spy(processItem);
        ProcessType processType = TalendFileFactory.eINSTANCE.createProcessType();
        when(processItem.getProcess()).thenReturn(processType);

        ParametersType paramType = TalendFileFactory.eINSTANCE.createParametersType();
        processType.setParameters(paramType);

        RoutinesParameterType routinesType = TalendFileFactory.eINSTANCE.createRoutinesParameterType();
        routinesType.setName("ABC");
        paramType.getRoutinesParameter().add(routinesType);

        routinesType = TalendFileFactory.eINSTANCE.createRoutinesParameterType();
        routinesType.setName("XYZ");
        processType.getParameters().getRoutinesParameter().add(routinesType);

        Set<Relation> relations = getHandler().collect(processItem);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 2);

        Iterator<Relation> iterator = relations.iterator();

        Relation relation = iterator.next();
        Assert.assertEquals("ABC", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.ROUTINE_RELATION, relation.getType());

        relation = iterator.next();
        Assert.assertEquals("XYZ", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.ROUTINE_RELATION, relation.getType());

        // test find
        Map<Relation, Set<Relation>> relationsMap = getHandler().find(processItem);
        Assert.assertNotNull(relationsMap);
        Assert.assertFalse(relationsMap.isEmpty());
        Assert.assertTrue(relationsMap.size() == 1);

        Relation processRelation = relationsMap.keySet().iterator().next();
        Assert.assertNotNull(processRelation);
        Assert.assertEquals(ITEM_ID, processRelation.getId());
        Assert.assertEquals("0.1", processRelation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.JOB_RELATION, processRelation.getType());

        Set<Relation> set = relationsMap.get(processRelation);
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
        Assert.assertTrue(set.size() == 2);

        Iterator<Relation> routineIte = set.iterator();
        relation = routineIte.next();
        Assert.assertEquals("ABC", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.ROUTINE_RELATION, relation.getType());

        relation = routineIte.next();
        Assert.assertEquals("XYZ", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.ROUTINE_RELATION, relation.getType());
    }
}
