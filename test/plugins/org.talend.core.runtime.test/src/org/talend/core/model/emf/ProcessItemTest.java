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
package org.talend.core.model.emf;

import org.eclipse.emf.ecore.EClass;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.impl.ProcessItemImpl;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProcessItemTest {

    private ProcessItem processItem;

    @Before
    public void setUp() throws Exception {
        processItem = PropertiesFactory.eINSTANCE.createProcessItem();
    }

    @After
    public void tearDown() throws Exception {
        processItem = null;
    }

    @Test
    public void testGetClass() {
        Class<? extends ProcessItem> clazz = processItem.getClass();

        Assert.assertNotSame(clazz, ProcessItem.class);

        Assert.assertEquals(clazz, ProcessItemImpl.class);// OK
        Assert.assertEquals(clazz.getName(), ProcessItemImpl.class.getName());
    }

    @Test
    public void testEClass() {
        EClass eClass = processItem.eClass();

        Assert.assertTrue(eClass == PropertiesPackage.Literals.PROCESS_ITEM);// OK

        Assert.assertEquals(eClass.getName(), "ProcessItem");
        Assert.assertEquals(eClass.getInstanceClass(), ProcessItem.class);// OK
        Assert.assertEquals(eClass.getInstanceClassName(), ProcessItem.class.getName());

        Assert.assertEquals(eClass.eContainer(), PropertiesPackage.eINSTANCE);
        Assert.assertEquals(eClass.getClassifierID(), PropertiesPackage.PROCESS_ITEM);
    }

    @Test
    public void testInstance() {
        Assert.assertTrue(PropertiesPackage.Literals.PROCESS_ITEM.isInstance(processItem));
    }

    // @Test
    public void testExxxFeature() {
        // can't use this way for talend, even used it from method "validateRoot" of class "XSDModelGroupImpl" in
        // "org.eclipse.xsd"
        Assert.assertTrue(processItem.eContainmentFeature() == PropertiesPackage.Literals.PROCESS_ITEM);
        // Assert.assertEquals(processItem.eContainingFeature(), PropertiesPackage.eINSTANCE.getProcessItem_Process());

    }
}
