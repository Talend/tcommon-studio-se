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
package org.talend.designer.maven.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;


public class CodeM2CacheManagerTest {

    private RoutineItem testRoutine;

    private ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    @Before
    public void setUp() throws Exception {
        testRoutine = createTempRoutineItem();
        // in case other tests A/D/M routines and didn't do clean up.
        updateCodeProject();
    }

    @After
    public void tearDown() throws Exception {
        if (testRoutine != null) {
            ProxyRepositoryFactory.getInstance().deleteObjectPhysical(new RepositoryViewObject(testRoutine.getProperty()));
        }
    }

    @Test
    public void testNeedUpdateCodeProject() throws Exception {
        Project project = ProjectManager.getInstance().getCurrentProject();
        // add
        factory.create(testRoutine, new Path(""));
        assertTrue(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
        updateCodeProject();

        // modify
        testRoutine.getContent().setInnerContent("content changed!".getBytes());
        factory.save(testRoutine);
        assertTrue(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
        updateCodeProject();

        // delete logical
        factory.deleteObjectLogical(factory.getLastVersion(testRoutine.getProperty().getId()));
        assertTrue(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
        updateCodeProject();

        // restore
        factory.restoreObject(factory.getLastVersion(testRoutine.getProperty().getId()), new Path(""));
        assertTrue(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
        updateCodeProject();

        // delete physical
        factory.deleteObjectPhysical(factory.getLastVersion(testRoutine.getProperty().getId()));
        assertTrue(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
        testRoutine = null;
        updateCodeProject();

    }

    private void updateCodeProject() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        CodeM2CacheManager.updateCodeProjectCache(project, ERepositoryObjectType.ROUTINES);
        assertFalse(CodeM2CacheManager.needUpdateCodeProject(project, ERepositoryObjectType.ROUTINES));
    }

    private RoutineItem createTempRoutineItem() {
        RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        routineItem.setProperty(myProperty);
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(false);
        itemState.setPath("");
        routineItem.setState(itemState);
        myProperty.setLabel("testRoutineUpdate");
        myProperty.setId(factory.getNextId());
        myProperty.setVersion("0.1");
        routineItem.setContent(PropertiesFactory.eINSTANCE.createByteArray());
        routineItem.getContent().setInnerContent("myRoutineContent".getBytes());
        return routineItem;
    }

}
