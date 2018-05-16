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
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.relationship.AbstractItemRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

import static org.mockito.Mockito.*;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobContextItemRelationshipHandlerTest extends AbstractProcessItemRelationshipHandlerTest {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractProcessItemRelationshipHandlerTest#createHandler()
     */
    @Override
    protected AbstractItemRelationshipHandler createHandler() {
        return new JobContextItemRelationshipHandler();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.handlers.AbstractProcessItemRelationshipHandlerTest#getHandler()
     */
    @Override
    protected JobContextItemRelationshipHandler getHandler() {
        return (JobContextItemRelationshipHandler) super.getHandler();
    }

    @SuppressWarnings("nls")
    @Test
    public void testCollect4SameRepository() {
        processItem = spy(processItem);
        ProcessType procType = TalendFileFactory.eINSTANCE.createProcessType();
        when(processItem.getProcess()).thenReturn(procType);

        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        procType.getContext().add(contextType);

        ContextParameterType paramType0 = TalendFileFactory.eINSTANCE.createContextParameterType();
        paramType0.setRepositoryContextId("ABC");
        contextType.getContextParameter().add(paramType0);

        ContextParameterType paramType1 = TalendFileFactory.eINSTANCE.createContextParameterType();
        paramType1.setRepositoryContextId("ABC");
        contextType.getContextParameter().add(paramType1);

        Set<Relation> relations = getHandler().collect(processItem);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 1);

        Relation relation = relations.iterator().next();
        Assert.assertEquals("ABC", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.CONTEXT_RELATION, relation.getType());
        //
    }

    @SuppressWarnings("nls")
    @Test
    public void testCollect4DiffRepository() {
        processItem = spy(processItem);
        ProcessType procType = TalendFileFactory.eINSTANCE.createProcessType();
        when(processItem.getProcess()).thenReturn(procType);

        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        procType.getContext().add(contextType);

        ContextParameterType paramType0 = TalendFileFactory.eINSTANCE.createContextParameterType();
        paramType0.setRepositoryContextId("ABC");
        contextType.getContextParameter().add(paramType0);

        ContextParameterType paramType1 = TalendFileFactory.eINSTANCE.createContextParameterType();
        paramType1.setRepositoryContextId("XYZ");
        contextType.getContextParameter().add(paramType1);

        Set<Relation> relations = getHandler().collect(processItem);
        Assert.assertNotNull(relations);
        Assert.assertFalse(relations.isEmpty());
        Assert.assertTrue(relations.size() == 2);

        //
        Iterator<Relation> iterator = relations.iterator();

        Relation relation = iterator.next();
        Assert.assertEquals("ABC", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.CONTEXT_RELATION, relation.getType());

        relation = iterator.next();
        Assert.assertEquals("XYZ", relation.getId());
        Assert.assertEquals(RelationshipItemBuilder.LATEST_VERSION, relation.getVersion());
        Assert.assertEquals(RelationshipItemBuilder.CONTEXT_RELATION, relation.getType());
    }

}
