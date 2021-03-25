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
package org.talend.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.properties.RoutinesJarType;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;

public class CodesJarResourceCacheTest {

    @Test
    public void testGetCodesJarById() throws Exception {
        RoutinesJarItem jarItem = PropertiesFactory.eINSTANCE.createRoutinesJarItem();
        String id = ProxyRepositoryFactory.getInstance().getNextId();
        try {
                RoutinesJarType jarType = PropertiesFactory.eINSTANCE.createRoutinesJarType();
                jarItem.setRoutinesJarType(jarType);
                ItemState state = PropertiesFactory.eINSTANCE.createItemState();
                state.setPath("");
                jarItem.setState(state);
                Property jarProperty = PropertiesFactory.eINSTANCE.createProperty();
                jarProperty.setId(id);
                jarProperty.setLabel("CodesJarResourceCacheTest_testGetCodesJarById_routinejar1");
                jarProperty.setVersion("1.0");
                jarProperty.setItem(jarItem);
                ProxyRepositoryFactory.getInstance().create(jarItem, new Path(""));
                CodesJarInfo info = CodesJarResourceCache.getCodesJarById(id);
                assertNotNull(info);
                assertEquals(info.getId(), jarProperty.getId());
                assertEquals(info.getLabel(), jarProperty.getLabel());
                assertEquals(info.getVersion(), jarProperty.getVersion());
                assertEquals(info.getType(), ERepositoryObjectType.getItemType(jarItem));
        } finally {
            ProxyRepositoryFactory.getInstance().deleteObjectPhysical(new RepositoryObject(jarItem.getProperty()));
        }
    }

    @Test
    public void testGetCodesJarByLabel() throws Exception {
        RoutinesJarItem jarItem = PropertiesFactory.eINSTANCE.createRoutinesJarItem();
        String id = ProxyRepositoryFactory.getInstance().getNextId();
        String label = "CodesJarResourceCacheTest_testGetCodesJarById_routinejar2";
        try {
                RoutinesJarType jarType = PropertiesFactory.eINSTANCE.createRoutinesJarType();
                jarItem.setRoutinesJarType(jarType);
                ItemState state = PropertiesFactory.eINSTANCE.createItemState();
                state.setPath("");
                jarItem.setState(state);
                Property jarProperty = PropertiesFactory.eINSTANCE.createProperty();
                jarProperty.setId(id);
                jarProperty.setLabel(label);
                jarProperty.setVersion("1.0");
                jarProperty.setItem(jarItem);
                ProxyRepositoryFactory.getInstance().create(jarItem, new Path(""));
                CodesJarInfo info = CodesJarResourceCache.getCodesJarByLabel(ERepositoryObjectType.ROUTINESJAR,
                        ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), label);
                assertNotNull(info);
                assertEquals(info.getId(), jarProperty.getId());
                assertEquals(info.getLabel(), jarProperty.getLabel());
                assertEquals(info.getVersion(), jarProperty.getVersion());
                assertEquals(info.getType(), ERepositoryObjectType.getItemType(jarItem));
        } finally {
            ProxyRepositoryFactory.getInstance().deleteObjectPhysical(new RepositoryObject(jarItem.getProperty()));
        }
    }

    @Test
    public void testGetCodesJarByInnerCode() throws Exception {
        RoutinesJarItem jarItem = PropertiesFactory.eINSTANCE.createRoutinesJarItem();
        try {
            {
                // create routine jar
                RoutinesJarType jarType = PropertiesFactory.eINSTANCE.createRoutinesJarType();
                jarItem.setRoutinesJarType(jarType);
                ItemState state = PropertiesFactory.eINSTANCE.createItemState();
                state.setPath("");
                jarItem.setState(state);
            }
            Property jarProperty = PropertiesFactory.eINSTANCE.createProperty();
            jarProperty.setId(ProxyRepositoryFactory.getInstance().getNextId());
            jarProperty.setLabel("PomIdsHelperTest_testGetCodesJarGroupIdByInnerCode_routinejar3");
            jarProperty.setVersion("1.0");
            jarProperty.setItem(jarItem);
            ProxyRepositoryFactory.getInstance().create(jarItem, new Path(""));

            RoutineItem innerRoutineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            {
                ItemState state = PropertiesFactory.eINSTANCE.createItemState();
                state.setPath("");
                innerRoutineItem.setState(state);
                ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
                byteArray.setInnerContent("".getBytes());
                innerRoutineItem.setContent(byteArray);
                Property property = PropertiesFactory.eINSTANCE.createProperty();
                property.setId(ProxyRepositoryFactory.getInstance().getNextId());
                property.setLabel("PomIdsHelperTest_testGetCodesJarGroupIdByInnerCode_innerRoutine1");
                property.setVersion("1.0");
                property.setItem(innerRoutineItem);
                ProxyRepositoryFactory.getInstance().create(innerRoutineItem,
                        new Path("PomIdsHelperTest_testGetCodesJarGroupIdByInnerCode_routinejar3"));
            }
            CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(innerRoutineItem);
            assertNotNull(info);
            assertEquals(info.getId(), jarProperty.getId());
            assertEquals(info.getLabel(), jarProperty.getLabel());
            assertEquals(info.getVersion(), jarProperty.getVersion());
            assertEquals(info.getType(), ERepositoryObjectType.getItemType(jarItem));
        } finally {
            ProxyRepositoryFactory.getInstance().deleteObjectPhysical(new RepositoryObject(jarItem.getProperty()));
        }
    }

}
