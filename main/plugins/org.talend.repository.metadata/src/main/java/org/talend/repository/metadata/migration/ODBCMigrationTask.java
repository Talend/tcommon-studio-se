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

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

/**
 * created by cmeng on Jun 2, 2015 Detailled comment
 *
 */
public class ODBCMigrationTask extends AbstractItemMigrationTask {

    protected Boolean hasODBCConnection = null;

    protected static final String MS_SQLSERVER_ODBC_DISPLAY_NAME_OLD_VERSION = "Microsoft SQL Server (Odbc driver)"; //$NON-NLS-1$

    protected static final String GENERIC_ODBC_DISPLAY_NAME_OLD_VERSION = "Generic ODBC"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 6, 2, 11, 30, 30);
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
                DatabaseConnection dbConn = (DatabaseConnection) conn;
                String dbType = dbConn.getDatabaseType();
                boolean isGenericOdbc = GENERIC_ODBC_DISPLAY_NAME_OLD_VERSION.equals(dbType);
                boolean isMSSqlServerOdbc = MS_SQLSERVER_ODBC_DISPLAY_NAME_OLD_VERSION.equals(dbType);
                if (hasODBCConnection != Boolean.TRUE && (isGenericOdbc || isMSSqlServerOdbc)) {
                    hasODBCConnection = Boolean.TRUE;
                    ProjectPreferenceManager projectPreferenceManager = CoreRuntimePlugin.getInstance()
                            .getProjectPreferenceManager();
                    projectPreferenceManager.setValue(IProjectSettingPreferenceConstants.METADATA_DBCONNECTION_ODBC_ENABLE, true);
                    projectPreferenceManager.save();
                }
                if (isMSSqlServerOdbc) {
                    modified = true;
                    dbConn.setDatabaseType(EDatabaseTypeName.MSODBC.getDisplayName());
                } else if (isGenericOdbc) {
                    modified = true;
                    dbConn.setDatabaseType(EDatabaseTypeName.GODBC.getDisplayName());
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
    // protected static Boolean isSupportODBC = null;
    // protected static boolean isSupportODBC() {
    // if (isSupportODBC != null) {
    // return isSupportODBC;
    // }
    // isSupportODBC = new Boolean(true);
    //        String javaVersion = System.getProperty("java.version"); //$NON-NLS-1$
    // if (javaVersion != null) {
    // org.talend.commons.utils.Version v = new org.talend.commons.utils.Version(javaVersion);
    // if (v.getMajor() == 1 && v.getMinor() > 7) { // more than JDK 1.7
    // isSupportODBC = new Boolean(false);
    // }
    // }
    // return isSupportODBC;
    // }
}
