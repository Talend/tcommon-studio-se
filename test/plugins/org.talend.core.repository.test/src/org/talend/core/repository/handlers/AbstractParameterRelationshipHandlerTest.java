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
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.AbstractParameterRelationshipHandler;
import org.talend.core.model.relationship.Relation;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public abstract class AbstractParameterRelationshipHandlerTest {

    public static final String ITEM_ID = "_yVUx8NF4EeG5wOtnVeZxqg";

    private AbstractParameterRelationshipHandler handler;

    private ProcessItem processItem;

    @Before
    public void setUp() {
        handler = createHandler();
        processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        processItem.setProperty(property);
        property.setItem(processItem);

        property.setId(ITEM_ID);
        property.setVersion("0.1");

    }

    protected AbstractParameterRelationshipHandler getHandler() {
        return this.handler;
    }

    protected ProcessItem getProcessItem() {
        return this.processItem;
    }

    protected abstract AbstractParameterRelationshipHandler createHandler();

    @Test
    public void testFind4NullArguments() {
        Map<Relation, Set<Relation>> relations = handler.find(null, null, null);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());

        relations = handler.find(processItem, null, null);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());

        relations = handler.find(processItem, new HashMap<String, ElementParameterType>(), null);
        Assert.assertNotNull(relations);
        Assert.assertTrue(relations.isEmpty());

    }

}
