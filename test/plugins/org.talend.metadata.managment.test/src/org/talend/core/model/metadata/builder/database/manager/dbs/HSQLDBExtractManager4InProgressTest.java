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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
public class HSQLDBExtractManager4InProgressTest extends HSQLDBExtractManagerTest {

    @Override
    @Before
    public void setUp() throws Exception {
        init(EDatabaseTypeName.HSQLDB_IN_PROGRESS);
    }

    @Override
    @Test
    public void testCloseConnectionForDerby() throws Exception {
        // because it's specially for HSQLDB_IN_PROGRESS, so need do test too.
        super.testCloseConnectionForDerby();
    }

    @Override
    protected void testCloseConnect_Verify(IMetadataConnection metadataConn, DriverShim wapperDriver) throws Exception {
        Assert.assertTrue(getExtractManger().closeConnection(metadataConn, wapperDriver));
        verify(wapperDriver).connect(anyString(), (Properties) Mockito.isNull());
    }

    @Override
    protected void testCloseConnect4Exception_Verify(IMetadataConnection metadataConn, DriverShim wapperDriver) throws Exception {
        super.testCloseConnect4Exception_Verify(metadataConn, wapperDriver);
        verify(wapperDriver).connect(anyString(), (Properties) Mockito.isNull());
    }

    @Override
    protected void verifyConnection4ReturnColumns4DontCreateConnection(Connection conn) throws SQLException {
        verify(conn, times(2)).isClosed();
        verify(conn, never()).close();
    }
}
