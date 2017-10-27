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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
public class JavaDBExtractManagerTest extends AbstractTest4ExtractManager {

    @Before
    public void setUp() throws Exception {
        /*
         * can be JAVADB, JAVADB_JCCJDBC,JAVADB_DERBYCLIENT
         * 
         * specially test the JAVADB_EMBEDED in @see JavaDBExtractManager4EmbededTest
         */
        init(EDatabaseTypeName.JAVADB_JCCJDBC);
    }

    @Test
    public void testCloseConnectionForDerby() throws Exception {
        Assert.assertNotNull(getExtractManger());

        Assert.assertFalse(getExtractManger().closeConnectionForDerby(null));

        Driver driver = mock(Driver.class);
        DriverShim driverShim = spy(new DriverShim(driver));
        when(driver.connect(anyString(), (Properties) Mockito.isNull())).thenReturn(any(Connection.class));

        Assert.assertTrue(getExtractManger().closeConnectionForDerby(driverShim));
        verify(driver).connect(anyString(), (Properties) Mockito.isNull());
    }

    @Override
    @Test
    public void testCloseConnect4Null() throws Exception {
        Assert.assertNotNull(getExtractManger());
        // all are null
        Assert.assertFalse(getExtractManger().closeConnection(null, null));
        /*
         * don't test it, will test it in testCloseConnect and testCloseConnect4Exception. Because will close the derby
         * always
         */
        // null metadataconncion,
        // DriverShim driverShim = mock(DriverShim.class);
        // Assert.assertFalse(getExtractManger().closeConnection(null, driverShim));

        // null driver
        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        // when(metadataConn.getDriverClass()).thenReturn(null);
        Assert.assertFalse(getExtractManger().closeConnection(metadataConn, null));

        // null driver class
        // Assert.assertFalse(getExtractManger().closeConnection(metadataConn, driverShim));
        // verify(metadataConn).getDriverClass();
    }

    @Override
    protected void testCloseConnect_Verify(IMetadataConnection metadataConn, DriverShim wapperDriver) throws Exception {
        Assert.assertTrue(getExtractManger().closeConnection(metadataConn, wapperDriver));
        verify(wapperDriver).connect(anyString(), any(Properties.class));
    }

    @Test
    public void testCloseConnect4Exception() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        EDatabase4DriverClassName db4DriverClass = EDatabase4DriverClassName.indexOfByDbType(getExtractManger().getDbType()
                .getXmlName());
        Assert.assertNotNull(db4DriverClass);
        when(metadataConn.getDriverClass()).thenReturn(db4DriverClass.getDriverClass());

        Driver driver = mock(Driver.class);
        DriverShim wapperDriver = spy(new DriverShim(driver));
        doThrow(new SQLException()).when(driver).connect(anyString(), (Properties) Mockito.isNull());

        Assert.assertFalse(getExtractManger().closeConnection(metadataConn, wapperDriver));
        verify(wapperDriver).connect(anyString(), (Properties) Mockito.isNull());
        verify(driver).connect(anyString(), (Properties) Mockito.isNull());
    }

}
