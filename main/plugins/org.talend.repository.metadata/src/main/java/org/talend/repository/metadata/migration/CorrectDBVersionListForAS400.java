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
package org.talend.repository.metadata.migration;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC talend class global comment. Detailled comment<br>
 * More information please see: https://jira.talendforge.org/browse/TDI-30337
 */
public class CorrectDBVersionListForAS400 extends AbstractItemMigrationTask {

    private static final String[] OLD_VERSION_LIST = new String[] { "V6R1 to V7R2", "V5R4 to V6R1", "V5R2 to V5R4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String[] NEW_VERSION_LIST = new String[] { "AS400_V6R1_V7R2", "AS400_V5R3_V6R1", "AS400_V5R2_V5R4" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static Map<String, String> versionMap;

    {
        versionMap = new HashMap<String, String>(OLD_VERSION_LIST.length);
        for (int i = 0; i < OLD_VERSION_LIST.length; i++) {
            versionMap.put(OLD_VERSION_LIST[i], NEW_VERSION_LIST[i]);
        }
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
                DatabaseConnection dbConn = (DatabaseConnection) conn;
                if (EDatabaseTypeName.AS400.getXmlName().equals(dbConn.getDatabaseType())) {
                    String oldVersion = dbConn.getDbVersionString();
                    if (!StringUtils.isEmpty(oldVersion)) {
                        String newVersion = versionMap.get(oldVersion);
                        if (!StringUtils.isEmpty(newVersion)) {
                            dbConn.setDbVersionString(newVersion);
                            modified = true;
                        }
                    }
                }
            }
        }
        try {
            if (modified) {
                ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                factory.save(item, true);
            }
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IProjectMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2014, 8, 19, 9, 20, 0);
        return gc.getTime();
    }

}
