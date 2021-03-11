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
package org.talend.repository.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.utils.RepositoryNodeSortUtil;

/**
 * @author hwang
 *
 */
public class RepositoryNodeSortUtilTest {
	
	@Test
    public void testGetSortVersion() {
		Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId("property1"); //$NON-NLS-1$
        property1.setVersion("2.9"); //$NON-NLS-1$
        property1.setLabel("test1");//$NON-NLS-1$
        ProcessItem item1 = PropertiesFactory.eINSTANCE.createProcessItem();
        ItemState state = PropertiesFactory.eINSTANCE.createItemState();
        state.setDeleted(false);
        item1.setState(state);
        property1.setItem(item1);
        IRepositoryViewObject object1 = new RepositoryViewObject(property1, true);
        
        property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId("property1"); //$NON-NLS-1$
        property1.setVersion("0.3"); //$NON-NLS-1$
        property1.setLabel("test1");//$NON-NLS-1$
        item1 = PropertiesFactory.eINSTANCE.createProcessItem();
        state = PropertiesFactory.eINSTANCE.createItemState();
        state.setDeleted(false);
        item1.setState(state);
        property1.setItem(item1);
        IRepositoryViewObject object2 = new RepositoryViewObject(property1, true);
        
        property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId("property1"); //$NON-NLS-1$
        property1.setVersion("2.11"); //$NON-NLS-1$
        property1.setLabel("test1");//$NON-NLS-1$
        item1 = PropertiesFactory.eINSTANCE.createProcessItem();
        state = PropertiesFactory.eINSTANCE.createItemState();
        state.setDeleted(false);
        item1.setState(state);
        property1.setItem(item1);
        IRepositoryViewObject object3 = new RepositoryViewObject(property1, true);
        
        List<IRepositoryViewObject> temp = new ArrayList<IRepositoryViewObject>();
        RepositoryNodeSortUtil util = new RepositoryNodeSortUtil();
        
        temp = new ArrayList<IRepositoryViewObject>();
        temp.add(object3);
        temp.add(object2);
        temp.add(object1);
        List<IRepositoryViewObject>  result = util.getSortVersion(temp);
        assertTrue("0.3".equals(result.get(0).getVersion()));
        assertTrue("2.9".equals(result.get(1).getVersion()));
        assertTrue("2.11".equals(result.get(2).getVersion()));
	}
}
