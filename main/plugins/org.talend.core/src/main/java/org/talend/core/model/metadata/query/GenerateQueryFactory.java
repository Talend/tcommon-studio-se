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
package org.talend.core.model.metadata.query;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.query.generator.AS400QueryGenerator;
import org.talend.core.model.metadata.query.generator.H2QueryGenerator;
import org.talend.core.model.metadata.query.generator.HiveQueryGenerator;
import org.talend.core.model.metadata.query.generator.ImpalaQueryGenerator;
import org.talend.core.model.metadata.query.generator.NetezzaQueryGenerator;
import org.talend.core.model.metadata.query.generator.NonDatabaseDefaultQueryGenerator;
import org.talend.core.model.metadata.query.generator.OldDelegateQueryGenerator;
import org.talend.core.model.metadata.query.generator.OracleQueryGenerator;
import org.talend.core.model.metadata.query.generator.PostgreQueryGenerator;

/**
 * ggu class global comment. Detailled comment
 */
public final class GenerateQueryFactory {

    private GenerateQueryFactory() {
    }

    public static IQueryGenerator getGenerator(final String dbType) {
        if (dbType == null) {
            return null;
        } else if ("General JDBC".equals(dbType)) { //$NON-NLS-1$
            return null;
        }
        return getGenerator(EDatabaseTypeName.getTypeFromDbType(dbType));
    }

    public static IQueryGenerator getGenerator(final EDatabaseTypeName dbType) {
        if (dbType == null) {
            return null;
        }
        switch (dbType) {
        case HIVE:
            return new HiveQueryGenerator(dbType);
        case H2:
            return new H2QueryGenerator(dbType);
        case IMPALA:
            return new ImpalaQueryGenerator(dbType);
        case NETEZZA:
            return new NetezzaQueryGenerator();
        case PLUSPSQL:
        case PSQL:
            return new PostgreQueryGenerator(dbType);
        case AS400:
            return new AS400QueryGenerator();
        case ACCESS:
        case IBMDB2:
        case IBMDB2ZOS:
        case MYSQL:
        case AMAZON_AURORA:
        case INGRES:
            return new NonDatabaseDefaultQueryGenerator(dbType);
        case ORACLE_OCI:
        case ORACLEFORSID:
        case ORACLESN:
        case SAS:
        case SAPHana:
            return new OracleQueryGenerator(dbType);
        case EXASOL:
        case FIREBIRD:
        case GENERAL_JDBC:
        case GODBC:
        case GREENPLUM:
        case HSQLDB:
        case HSQLDB_IN_PROGRESS:
        case HSQLDB_SERVER:
        case HSQLDB_WEBSERVER:
        case INFORMIX:
        case INTERBASE:
        case JAVADB:
        case JAVADB_DERBYCLIENT:
        case JAVADB_EMBEDED:
        case JAVADB_JCCJDBC:
        case MAXDB:
        case MSODBC:
        case MSSQL:
        case PARACCEL:
        case REDSHIFT:
        case SQLITE:
        case SYBASEASE:
        case SYBASEIQ:
        case TERADATA:
        case VERTICA:
        default:
            return new OldDelegateQueryGenerator(dbType);
        }
    }
}
