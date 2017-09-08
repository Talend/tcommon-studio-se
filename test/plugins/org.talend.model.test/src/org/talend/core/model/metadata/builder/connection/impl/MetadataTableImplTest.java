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
package org.talend.core.model.metadata.builder.connection.impl;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;


/**
 * created by zshen on Jul 17, 2013
 * Detailled comment
 *
 */
public class MetadataTableImplTest {

    private static String metadataTableName = "myName";

    private static String metadataTableLabel = "myLabel";

    private static String metadataTableDefaultLable = "metadata";

    /**
     * DOC zshen Comment method "setUpBeforeClass".
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDownAfterClass".
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 1 create metadataTable and with same time set label and name case.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase1() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setLabel(metadataTableLabel);
        createMetadataTable.setName(metadataTableName);
        Assert.assertTrue(createMetadataTable.getName().equals(metadataTableName));
        Assert.assertTrue(createMetadataTable.getLabel().equals(metadataTableLabel));
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 2 create metadataTable only and don't set label and name case.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase2() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        Assert.assertTrue(createMetadataTable.getName().equals(metadataTableDefaultLable));
        Assert.assertTrue(createMetadataTable.getLabel().equals(metadataTableDefaultLable));
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 3 create metadataTable and set label case.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase3() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setLabel(metadataTableLabel);
        Assert.assertTrue(createMetadataTable.getName().equals(metadataTableLabel));
        Assert.assertTrue(createMetadataTable.getLabel().equals(metadataTableLabel));
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 4 create metadataTable and set name case.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase4() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setName(metadataTableName);
        Assert.assertTrue(createMetadataTable.getName().equals(metadataTableName));
        Assert.assertTrue(createMetadataTable.getLabel().equals(metadataTableDefaultLable));
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 5 create metadataTable and make lable is null.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase5() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setLabel(null);
        Assert.assertTrue(createMetadataTable.getName() == null);
        Assert.assertTrue(createMetadataTable.getLabel() == null);
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 6 create metadataTable and make name is null.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase6() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setName(null);
        Assert.assertTrue(createMetadataTable.getName().equals(metadataTableDefaultLable));
        Assert.assertTrue(createMetadataTable.getLabel().equals(metadataTableDefaultLable));
    }

/**
     * Test method for {@link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getName()
     * @link org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getLabel()}.
     *
     * test case 7 create metadataTable and make both name and label are null.
     *
     */
    @Test
    public void testGetNameAndGetLabelcase7() {
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.setName(null);
        createMetadataTable.setLabel(null);
        Assert.assertTrue(createMetadataTable.getName() == null);
        Assert.assertTrue(createMetadataTable.getLabel() == null);
    }



}
