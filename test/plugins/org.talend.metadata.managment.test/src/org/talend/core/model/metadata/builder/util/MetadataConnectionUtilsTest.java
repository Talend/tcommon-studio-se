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
package org.talend.core.model.metadata.builder.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;

/**
 * created by ggu on Jul 12, 2012 Detailled comment
 * 
 */
public class MetadataConnectionUtilsTest {

    private void PTODO() {
        // after test, will remove this method
        Assert.fail("PTODO: Please finish this test.");
    }

    // @Test
    public void testCheckConnection4MetadataConnection() throws Exception {
        PTODO();
    }

    // @Test
    public void testCheckConnection4DatabaseConnection() throws Exception {
        PTODO();
    }

    // @Test
    public void testGetSoftwareSystem() throws Exception {
        PTODO();
    }

    @Test
    public void testGetSybaseDBProductsName() {
        Assert.assertEquals(5, MetadataConnectionUtils.getSybaseDBProductsName().length);
    }

    // @Test
    public void testGetClassDriver() throws Exception {
        PTODO();
    }

    // @Test
    public void testGetCommonQueryStr() throws Exception {
        PTODO();
    }

    @Test
    public void testCreateDataType() {
        TdSqlDataType dataType1 = MetadataConnectionUtils.createDataType(1, null, 2, 1);
        Assert.assertNotNull(dataType1);
        Assert.assertNull(dataType1.getName()); // null name
        Assert.assertEquals(1, dataType1.getJavaDataType());
        Assert.assertEquals(2, dataType1.getNumericPrecision());
        Assert.assertEquals(1, dataType1.getNumericPrecisionRadix());

        TdSqlDataType dataType2 = MetadataConnectionUtils.createDataType(0, "test", 1, 2); //$NON-NLS-1$
        Assert.assertNotNull(dataType2);
        Assert.assertEquals("test", dataType2.getName()); //$NON-NLS-1$
        Assert.assertEquals(0, dataType2.getJavaDataType());
        Assert.assertEquals(1, dataType2.getNumericPrecision());
        Assert.assertEquals(2, dataType2.getNumericPrecisionRadix());
    }

    // @Test
    public void testGetXtentisBindingStub4MetadataConnection() {
        PTODO();
    }

    // @Test
    public void testGetXtentisBindingStub4MDMConnection() {
        PTODO();
    }

    // @Test
    public void testCreateTechnicalName() {
        Assert.assertEquals("no_name", MetadataConnectionUtils.createTechnicalName(null)); //$NON-NLS-1$
        // need test it by powermock for the SMPL_DATE_FMT
        PTODO();
    }

    // @Test
    public void testGetPackageFilter4Database() {
        PTODO();
    }

    // @Test
    public void testGetPackageFilter4MDM() {
        PTODO();
    }

    // @Test
    public void testFillConnectionInformation() {
        PTODO();
    }

    // @Test
    public void testSetMDMConnectionParameter() {
        PTODO();
    }

    // @Test
    public void testFillDbConnectionInformation() {
        PTODO();
    }

    /**
     * 
     * test for judging if it is hive connection. normal case.
     * 
     * @throws SQLException
     */
    @Test
    public void testIsHive_1() throws SQLException {
        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        when(dbMetadata.getDatabaseProductName()).thenReturn("Hive");//$NON-NLS-1$
        boolean hive = MetadataConnectionUtils.isHive(dbMetadata);
        assertTrue(hive);
    }

    /**
     * 
     * test for judging if it is hive connection. abnormal case
     * 
     * @throws SQLException
     */
    @Test
    public void testIsHive_2() throws SQLException {
        DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
        when(dbMetadata.getDatabaseProductName()).thenReturn("Mysql");//$NON-NLS-1$
        boolean hive = MetadataConnectionUtils.isHive(dbMetadata);
        assertFalse(hive);
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.utils.MetadataConnectionUtils#getMetadataColumnName(orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     */
    @Test
    public void testGetMetadataColumnNameMetadataColumn() {
        String name1 = "name1"; //$NON-NLS-1$
        String name2 = "name2"; //$NON-NLS-1$

        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        metadataColumn.setId(name1);

        String elementName = MetadataConnectionUtils.getMetadataColumnName(metadataColumn);
        assertTrue(name1.equals(elementName));

        metadataColumn.setId(name2);
        elementName = MetadataConnectionUtils.getMetadataColumnName(metadataColumn);
        assertFalse(name1.equals(elementName));
        assertTrue(name2.equals(elementName));
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.utils.MetadataConnectionUtils#getMetadataColumnName(orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     */
    @Test
    public void testGetMetadataColumnNameTdColumn() {
        String name1 = "name1"; //$NON-NLS-1$
        String name2 = "name2"; //$NON-NLS-1$

        TdColumn tdColumn = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn.setName(name1);

        String elementName = MetadataConnectionUtils.getMetadataColumnName(tdColumn);
        assertTrue(name1.equals(elementName));

        tdColumn.setName(name2);
        elementName = MetadataConnectionUtils.getMetadataColumnName(tdColumn);
        assertFalse(name1.equals(elementName));
        assertTrue(name2.equals(elementName));
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.utils.MetadataConnectionUtils#getMetadataColumnName(orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     */
    @Test
    public void testGetMetadataColumnNameTdXmlElementType() {
        String name1 = "name1"; //$NON-NLS-1$
        String name2 = "name2"; //$NON-NLS-1$

        TdXmlElementType tdXmlElementType = org.talend.cwm.xml.XmlFactory.eINSTANCE.createTdXmlElementType();
        tdXmlElementType.setName(name1);

        String elementName = MetadataConnectionUtils.getMetadataColumnName(tdXmlElementType);
        assertTrue(name1.equals(elementName));

        tdXmlElementType.setName(name2);
        elementName = MetadataConnectionUtils.getMetadataColumnName(tdXmlElementType);
        assertFalse(name1.equals(elementName));
        assertTrue(name2.equals(elementName));
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.utils.MetadataConnectionUtils#getMetadataColumnName(orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     */
    @Test
    public void testGetMetadataColumnNameNull() {
        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        String elementName = MetadataConnectionUtils.getMetadataColumnName(metadataColumn);
        assertNull(elementName);

        TdColumn tdColumn = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdColumn();
        elementName = MetadataConnectionUtils.getMetadataColumnName(tdColumn);
        assertNull(elementName);

        TdXmlElementType tdXmlElementType = org.talend.cwm.xml.XmlFactory.eINSTANCE.createTdXmlElementType();
        elementName = MetadataConnectionUtils.getMetadataColumnName(tdXmlElementType);
        assertNull(elementName);
    }
}
