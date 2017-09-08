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
package org.talend.core.model.metadata.builder.database;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;

/**
 * created by mzhao on Nov 12, 2012 Detailled comment
 * 
 */
public class JavaSqlFactoryTest {

    /**
     * Test method for
     * {@link org.talend.core.model.metadata.builder.database.JavaSqlFactory#getPassword(org.talend.core.model.metadata.builder.connection.Connection)}
     * .
     */
    @Test
    public void testGetPassword() {
        DatabaseConnection conn = ConnectionPackage.eINSTANCE.getConnectionFactory().createDatabaseConnection();
        String pwd = JavaSqlFactory.getPassword(conn);
        assertEquals("", pwd); //$NON-NLS-1$
        conn.setRawPassword(""); //$NON-NLS-1$
        pwd = JavaSqlFactory.getPassword(conn);
        assertEquals("", pwd); //$NON-NLS-1$
        conn.setRawPassword("talend4ever"); //$NON-NLS-1$
        pwd = JavaSqlFactory.getPassword(conn);
        assertEquals("talend4ever", pwd); //$NON-NLS-1$

    }

}
