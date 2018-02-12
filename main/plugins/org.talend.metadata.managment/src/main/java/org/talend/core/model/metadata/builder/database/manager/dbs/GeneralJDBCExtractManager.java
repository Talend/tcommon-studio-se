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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.MetadataConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.ExtractManagerFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class GeneralJDBCExtractManager extends ExtractManager {

    public GeneralJDBCExtractManager() {
        super(EDatabaseTypeName.GENERAL_JDBC);
    }

    @Override
    public String getTableComment(IMetadataConnection metadataConnection, ResultSet resultSet, String nameKey)
            throws SQLException {
        if (metadataConnection != null) {
            ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
            IMetadataConnection newMetadataConn = new MetadataConnection();

            newMetadataConn.setDbVersionString(metadataConnection.getDbVersionString());
            newMetadataConn.setDriverClass(metadataConnection.getDriverClass());
            newMetadataConn.setDriverJarPath(metadataConnection.getDriverJarPath());

            String driverClassName = metadataConnection.getDriverClass();
            String driverJar = metadataConnection.getDriverJarPath();
            if (driverJar != null && !"".equals(driverJar)) {
                if (driverJar.contains("\\")) {//$NON-NLS-1$
                    driverJar = driverJar.substring(driverJar.lastIndexOf("\\") + 1, driverJar.length());//$NON-NLS-1$
                }
            }
            //
            String dbType = metadataConnection.getDbType();
            if (driverJar != null && !EMPTY.equals(driverJar)) {
                EDatabaseVersion4Drivers dbversion4Driver = getDBVersionByClassNameAndDriverJar(driverClassName, driverJar);
                if (dbversion4Driver != null) {
                    newMetadataConn.setDbVersionString(dbversion4Driver.getVersionValue());
                }
                dbType = extractMeta.getDbTypeByClassNameAndDriverJar(driverClassName, driverJar);
            } else {
                dbType = extractMeta.getDbTypeByClassName(driverClassName);
            }
            if (dbType != null) {
                newMetadataConn.setDbType(dbType);
                EDatabaseTypeName databaseType = EDatabaseTypeName.getTypeFromDbType(dbType);
                ExtractManager extractManager = ExtractManagerFactory.create(databaseType);
                if (extractManager != null) {
                    return extractManager.getTableComment(newMetadataConn, resultSet, nameKey);
                }
            }
        }
        return super.getTableComment(metadataConnection, resultSet, nameKey);
    }

    public static EDatabaseVersion4Drivers getDBVersionByClassNameAndDriverJar(String driverClassName, String driverJar) {
        // same driver classes for databases
        List<EDatabase4DriverClassName> t4d = EDatabase4DriverClassName.indexOfByDriverClass(driverClassName);

        List<EDatabaseVersion4Drivers> dbVersion4Drivers = new ArrayList<EDatabaseVersion4Drivers>();
        if (driverJar != null) {
            // contain same jars
            for (EDatabaseVersion4Drivers v4d : EDatabaseVersion4Drivers.values()) {
                for (String driver : v4d.getProviderDrivers()) {
                    if (driverJar.contains(driver)) {
                        dbVersion4Drivers.add(v4d);
                    }
                }
            }
        }
        for (EDatabase4DriverClassName db4DriaverClassName : t4d) {
            for (EDatabaseVersion4Drivers dbversion4Driver : dbVersion4Drivers) {
                if (dbversion4Driver.supportDatabase(db4DriaverClassName.getDbType())) {
                    // same jar and same driver
                    return dbversion4Driver;
                }
            }
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.builder.database.manager.ExtractManager#closeConnection(org.talend.core.model.
     * metadata.IMetadataConnection, org.talend.core.model.metadata.builder.database.DriverShim)
     */
    @Override
    public boolean closeConnection(IMetadataConnection metadataConnection, DriverShim wapperDriver) {
        if (wapperDriver != null && metadataConnection != null) {
            String driverClass = metadataConnection.getDriverClass();
            if (driverClass != null) {
                List<EDatabase4DriverClassName> db4DriverClasses = EDatabase4DriverClassName.indexOfByDriverClass(driverClass);
                if (!db4DriverClasses.isEmpty()) {
                    // use the firest one
                    ExtractManager manager = ExtractManagerFactory.create(db4DriverClasses.get(0).getDbType());
                    if (manager != null) {
                        IMetadataConnection newMetadataConn = new MetadataConnection();

                        newMetadataConn.setDbType(manager.getDbType().getXmlName());
                        newMetadataConn.setDbVersionString(metadataConnection.getDbVersionString());
                        newMetadataConn.setDriverClass(metadataConnection.getDriverClass());
                        newMetadataConn.setDriverJarPath(metadataConnection.getDriverJarPath());

                        return manager.closeConnection(newMetadataConn, wapperDriver);
                    }
                }
            }
        }
        return super.closeConnection(metadataConnection, wapperDriver);
    }

}
