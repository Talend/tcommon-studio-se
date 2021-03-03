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
package org.talend.core.repository.ui.view;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.FakePropertyImpl;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ttao on Feb 28, 2019 Detailled comment
 *
 */
public class RepositoryDropAdapterTest {

    @Test
    public void testMoveRunnableRun() {
        Property property = new FakePropertyImpl();
        Item item = PropertiesFactory.eINSTANCE.createFolderItem();
        property.setItem(item);
        RepositoryViewObject object = new RepositoryViewObject(property, true);
        RepositoryNode node = new RepositoryNode(object, null, ENodeType.SIMPLE_FOLDER);
        Assert.assertEquals(node.getObjectType().getType(), ERepositoryObjectType.FOLDER.getType());
    }
}
