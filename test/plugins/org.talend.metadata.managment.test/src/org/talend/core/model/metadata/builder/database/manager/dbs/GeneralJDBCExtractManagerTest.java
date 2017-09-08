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
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;

/**
 * created by ggu on Jul 6, 2012 Detailled comment
 * 
 */
public class GeneralJDBCExtractManagerTest extends AbstractTest4ExtractManager {

    @Before
    public void setUp() throws Exception {
        init(EDatabaseTypeName.GENERAL_JDBC);
    }

    @Override
    @Test
    public void testCloseConnect() throws Exception {
        Assert.assertNotNull(getExtractManger());

        IMetadataConnection metadataConn = mock(IMetadataConnection.class);
        /**
         * for general JDBC, there is no driverclass, will test this for JavaDB and HSQLDB in another test.
         */
        // EDatabase4DriverClassName db4DriverClass =
        // EDatabase4DriverClassName.indexOfByDbType(getExtractManger().getDbType()
        // .getXmlName());
        // Assert.assertNotNull(db4DriverClass);
        // when(metadataConn.getDriverClass()).thenReturn(db4DriverClass.getDriverClass());

        Driver driver = mock(Driver.class);
        DriverShim wapperDriver = spy(new DriverShim(driver));
        Connection connection = mock(Connection.class);
        doReturn(connection).when(driver).connect(anyString(), (Properties) Mockito.isNull());

        testCloseConnect_Verify(metadataConn, wapperDriver);
    }

    @Override
    protected void testCloseConnect_Verify(IMetadataConnection metadataConn, DriverShim wapperDriver) throws Exception {
        super.testCloseConnect_Verify(metadataConn, wapperDriver);
        verify(metadataConn).getDriverClass();
    }

    /**
     * 
     * DOC ggu Comment method "testCloseConnect4DirverClass_JavaDB".
     * 
     * need test the driverclass for java db
     * 
     * @throws Exception
     */
    // @Test
    public void testCloseConnect4DirverClass_JavaDB() throws Exception {
        PTODO();
    }

    /**
     * 
     * DOC ggu Comment method "testCloseConnect4DirverClass_HsqlDB".
     * 
     * need test the driverclass for HSQLDB
     * 
     * @throws Exception
     */
    // @Test
    public void testCloseConnect4DirverClass_HsqlDB() throws Exception {
        PTODO();
    }
}
