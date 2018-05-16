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
package org.talend.core.runtime.repository.build;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.ui.ITestContainerProviderService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TestCaseBuildProviderTest {

    class TestCaseBuildProviderTestClass extends TestCaseBuildProvider {

    }

    @Before
    public void before() {
        Assert.assertTrue("Make sure the related test case bundles are loaded. for example: org.talend.testcontainer.core.ui",
                GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class));
    }

    @Test
    public void test_valid_empty() {
        TestCaseBuildProviderTestClass provider = new TestCaseBuildProviderTestClass();
        Assert.assertFalse(provider.valid(null));
        Assert.assertFalse(provider.valid(Collections.emptyMap()));
    }

    @Test
    public void test_valid_nonItem() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.ITEM, new Object());

        TestCaseBuildProviderTestClass provider = new TestCaseBuildProviderTestClass();
        Assert.assertFalse(provider.valid(parameters));
    }

    @Test
    public void test_valid_nonTestCaseItem() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.ITEM, PropertiesFactory.eINSTANCE.createProcessItem());

        TestCaseBuildProviderTestClass provider = new TestCaseBuildProviderTestClass();
        Assert.assertFalse(provider.valid(parameters));
    }

    @Test
    public void test_valid() {
        Map<String, Object> parameters = new HashMap<String, Object>();

        ERepositoryObjectType objType = ERepositoryObjectType.valueOf("TEST_CONTAINER");
        Assert.assertNotNull(objType);

        Item item = null;
        for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
            item = handler.createNewItem(objType);
            if (item != null) {
                break;
            }
        }
        Assert.assertNotNull(item);

        parameters.put(IBuildParametes.ITEM, item);

        TestCaseBuildProviderTestClass provider = new TestCaseBuildProviderTestClass();
        Assert.assertTrue(provider.valid(parameters));
    }
}
