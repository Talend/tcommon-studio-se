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
package org.talend.core.model.metadata.builder.database.manager;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.database.manager.dbs.AS400ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.AccessExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.GeneralJDBCExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.HSQLDBExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.HiveExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.IBMDB2ExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.ImpalaExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.JAVADBExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.MSSQLExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.MySQLExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.OracleExtractManager;
import org.talend.core.model.metadata.builder.database.manager.dbs.TeradataExtractManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ExtractManagerFactory {

    public static ExtractManager createByDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        EDatabaseTypeName dbType = null;
        for (EDatabaseTypeName type : EDatabaseTypeName.values()) {
            if (displayName.equals(type.getDisplayName())) {
                dbType = type;
                break;
            }
        }
        ExtractManager manager = create(dbType);
        if(manager != null){
            return manager;
        }
        return createExtraManager(displayName);
    }
    
    public static ExtractManager createExtraManager(String displayName){
        if (displayName == null) {
            return null;
        }
        if(displayName.equals(EDatabaseTypeName.GENERAL_JDBC.getProduct())){
            return new GeneralJDBCExtractManager();
        }
        return null;
    }

    public static ExtractManager create(EDatabaseTypeName dbType) {
        if (dbType == null) {
            return null;
        }
        switch (dbType) {
        case ACCESS:
            return new AccessExtractManager();
        case AS400:
            return new AS400ExtractManager();
        case GENERAL_JDBC:
            return new GeneralJDBCExtractManager();
        case HIVE:
            return new HiveExtractManager();
        case IMPALA:
            return new ImpalaExtractManager();
        case HSQLDB:
        case HSQLDB_SERVER:
        case HSQLDB_WEBSERVER:
        case HSQLDB_IN_PROGRESS:
            return new HSQLDBExtractManager(dbType);
        case IBMDB2:
        case IBMDB2ZOS:
            return new IBMDB2ExtractManager(dbType);
        case JAVADB:
        case JAVADB_EMBEDED:
        case JAVADB_DERBYCLIENT:
        case JAVADB_JCCJDBC:
            return new JAVADBExtractManager(dbType);
        case MSSQL:
        case MSSQL05_08:
            return new MSSQLExtractManager(dbType);
        case MYSQL:
            return new MySQLExtractManager();
        case ORACLEFORSID:
        case ORACLESN:
        case ORACLE_CUSTOM:
        case ORACLE_OCI:
            return new OracleExtractManager(dbType);
        case TERADATA:
            return new TeradataExtractManager();
        default:
            return new ExtractManager(dbType);
        }

    }
}
