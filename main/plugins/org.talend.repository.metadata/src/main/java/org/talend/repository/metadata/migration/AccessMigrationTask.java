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
package org.talend.repository.metadata.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * created by kongxiaohan on Jun 25, 2015 Detailled comment
 *
 */
public class AccessMigrationTask extends AbstractItemMigrationTask {

    DatabaseConnection dbConn = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 6, 25, 16, 50, 30);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        boolean modified = false;
        if (item instanceof DatabaseConnectionItem) {
            Connection conn = ((DatabaseConnectionItem) item).getConnection();
            if (conn instanceof DatabaseConnection) {
                dbConn = (DatabaseConnection) conn;
                String dbType = dbConn.getDatabaseType();
                if (dbType.equals(EDatabaseConnTemplate.ACCESS.getDBTypeName())) {
                    modified = true;
                    dbConn.setDriverClass(EDatabase4DriverClassName.ACCESS.getDriverClass());
                    dbConn.setURL(getAccessUrl());
                    dbConn.setDbVersionString(""); //$NON-NLS-1$
                }
            }
        }
        try {
            if (modified) {
                ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                factory.save(item, true);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
        return ExecutionResult.SUCCESS_NO_ALERT;
    }

    private String getAccessUrl() {
        String s = DatabaseConnStrUtil.getURLString(EDatabaseConnTemplate.ACCESS.getDBTypeName(), dbConn.getDbVersionString(),
                "", dbConn.getUsername(), dbConn.getPassword(), dbConn.getPort(), dbConn.getSID(), dbConn.getFileFieldName(), //$NON-NLS-1$
                dbConn.getDatasourceName(), dbConn.getDBRootPath(), dbConn.getAdditionalParams());
        if (s != null) {
            return s;
        }
        return null;
    }
}
