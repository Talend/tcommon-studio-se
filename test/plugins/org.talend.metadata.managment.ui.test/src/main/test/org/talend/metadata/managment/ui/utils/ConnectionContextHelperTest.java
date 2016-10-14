// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.ui.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;

/**
 * created by ycbai on 2016年9月2日
 * Detailled comment
 *
 */
public class ConnectionContextHelperTest {

    @Test
    public void testIsContextMode() {
        String testValue = "value1"; //$NON-NLS-1$
        String testContextValue = "context.value1"; //$NON-NLS-1$

        Connection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        connection.setContextMode(false);
        assertFalse(ConnectionContextHelper.isContextMode(connection, testValue));
        assertFalse(ConnectionContextHelper.isContextMode(connection, testContextValue));

        connection.setContextMode(true);
        assertFalse(ConnectionContextHelper.isContextMode(connection, testValue));
        assertTrue(ConnectionContextHelper.isContextMode(connection, testContextValue));
    }

}
