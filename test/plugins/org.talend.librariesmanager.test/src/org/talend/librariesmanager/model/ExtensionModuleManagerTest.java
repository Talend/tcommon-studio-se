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
package org.talend.librariesmanager.model;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.general.ModuleNeeded;

/**
 * created by ycbai on 2014-5-8 Detailled comment
 *
 */
public class ExtensionModuleManagerTest {

    private static ExtensionModuleManager manager;

    @BeforeClass
    public static void setUp() throws Exception {
        manager = ExtensionModuleManager.getInstance();
    }

    @AfterClass
    public static void tearDown() {
        manager = null;
    }

    @Test
    public void testGetModuleNeededViaId() {
        /*
         * Get single module from id.
         */
        List<ModuleNeeded> moduleNeededs = manager.getModuleNeeded("test_jar1"); //$NON-NLS-1$
        Assert.assertNotNull(moduleNeededs);
        Assert.assertFalse(moduleNeededs.isEmpty());
        Assert.assertTrue(moduleNeededs.size() == 1);
        Assert.assertEquals("test_jar1.jar", moduleNeededs.get(0).getModuleName()); //$NON-NLS-1$
        Assert.assertEquals("test_jar1", moduleNeededs.get(0).getId()); //$NON-NLS-1$
    }

    @Test
    public void testGetModuleNeededViaName() {
        /*
         * Get single module from name.
         */
        List<ModuleNeeded> moduleNeededs = manager.getModuleNeeded("test_jar1.jar"); //$NON-NLS-1$
        Assert.assertNotNull(moduleNeededs);
        Assert.assertFalse(moduleNeededs.isEmpty());
        Assert.assertTrue(moduleNeededs.size() == 1);
        Assert.assertEquals("test_jar1.jar", moduleNeededs.get(0).getModuleName()); //$NON-NLS-1$
        Assert.assertEquals("test_jar1", moduleNeededs.get(0).getId()); //$NON-NLS-1$
    }

    @Test
    public void testGetModuleNeededViaGroup() {
        /*
         * Get modules from group.
         */
        List<ModuleNeeded> moduleNeededs = manager.getModuleNeeded("test_jars_group", true); //$NON-NLS-1$
        Assert.assertNotNull(moduleNeededs);
        Assert.assertTrue(moduleNeededs.size() == 3);
        Assert.assertEquals("test_jar1.jar", moduleNeededs.get(0).getModuleName()); //$NON-NLS-1$
        Assert.assertTrue("test_jar1".equals(moduleNeededs.get(0).getId())); //$NON-NLS-1$
        Assert.assertEquals("test_jar2.jar", moduleNeededs.get(1).getModuleName()); //$NON-NLS-1$
        Assert.assertTrue("test_jar2".equals(moduleNeededs.get(1).getId())); //$NON-NLS-1$
        Assert.assertEquals("test_jar3.jar", moduleNeededs.get(2).getModuleName()); //$NON-NLS-1$
        Assert.assertTrue("test_jar3".equals(moduleNeededs.get(2).getId())); //$NON-NLS-1$
    }
}
