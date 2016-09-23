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
package org.talend.core.database;

import org.talend.core.model.metadata.builder.database.EDatabaseSchemaOrCatalogMapping;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public enum EDatabaseTypeName {
    MYSQL(
          "MySQL", "MySQL", Boolean.FALSE, "MYSQL", "MYSQL", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    AMAZON_AURORA(
                  "Amazon Aurora", "Amazon Aurora", Boolean.FALSE, "AMAZON_AURORA", "AMAZON_AURORA", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    PSQL(
         "PostgreSQL", "PostgreSQL", Boolean.TRUE, "POSTGRESQL", "POSTGRE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    PLUSPSQL(
             "PostgresPlus", "PostgresPlus", Boolean.TRUE, "POSTGRESPLUS", "POSTGREPLUS", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ORACLEFORSID(
                 "ORACLE_SID", "Oracle with SID", Boolean.TRUE, "ORACLE", "DBORACLE", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ORACLESN(
             "ORACLE_SERVICE_NAME", "Oracle with service name", Boolean.TRUE, "ORACLE", "DBORACLE", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ORACLE_OCI(
               "ORACLE_OCI", "Oracle OCI", Boolean.TRUE, "ORACLE", "DBORACLE", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ORACLE_CUSTOM(
                  "ORACLE_CUSTOM", "Oracle Custom", Boolean.TRUE, "ORACLE", "DBORACLE", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$    
    /**
     * @deprecated odbc is not supported in java8
     */
    GODBC(
          "Generic ODBC", "Generic ODBC (Unsupported)", Boolean.FALSE, "ODBC", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    /**
     * @deprecated odbc is not supported in java8
     */
    MSODBC(
           "Microsoft SQL (Odbc driver)", "Microsoft SQL Server (Odbc driver, Unsupported)", Boolean.FALSE, "ODBC", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    IBMDB2(
           "IBM DB2", "IBM DB2", Boolean.TRUE, "IBM_DB2", "DB2", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    IBMDB2ZOS(
              "IBM DB2 ZOS", "IBM DB2 ZOS", Boolean.TRUE, "IBM_DB2", "DB2", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    SYBASEASE(
              "SybaseASE", "Sybase (ASE and IQ)", Boolean.TRUE, "SYBASE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    // this Sybase IQ not used.
    SYBASEIQ(
             "Sybase IQ", "Sybase IQ", Boolean.TRUE, "SYBASE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    MSSQL(
          "MSSQL", "Microsoft SQL Server", Boolean.TRUE, "SQL_SERVER", "MSSQL", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    MSSQL05_08(
               "MSSQL", "Microsoft SQL Server 2005/2008", Boolean.TRUE, "SQL_SERVER", "MSSQL", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    // this don't use in Branch 2.0
    HSQLDB("HSQLDB", "HSQLDB", Boolean.FALSE, "HSQLDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HSQLDB_SERVER(
                  "HSQLDB Server", "HSQLDB Server", Boolean.FALSE, "HSQLDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HSQLDB_WEBSERVER(
                     "HSQLDB WebServer", "HSQLDB WebServer", Boolean.FALSE, "HSQLDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HSQLDB_IN_PROGRESS(
                       "HSQLDB In-Process", "HSQLDB In-Process", Boolean.FALSE, "HSQLDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    JAVADB("JavaDB", "JavaDB", Boolean.FALSE, "JAVADB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    INGRES(
           "Ingres", "Ingres", Boolean.FALSE, "INGRES", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), // "INGRES"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    INTERBASE(
              "Interbase", "Interbase", Boolean.FALSE, "INTERBASE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), // "INTERBASE"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SQLITE("SQLite", "SQLite", Boolean.FALSE, "SQLITE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), // "SQLITE"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    FIREBIRD(
             "FireBird", "FireBird", Boolean.FALSE, "FIREBIRD", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), // "FIREBIRD"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    INFORMIX(
             "Informix", "Informix", Boolean.TRUE, "INFORMIX", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), // "INFORMIX"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    VECTORWISE(
               "VectorWise", "VectorWise", Boolean.FALSE, "VECTORWISE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$

    ACCESS(
           "Access", "Access", Boolean.FALSE, "ACCESS", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Default_Name), // "ACCESS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    TERADATA(
             "Teradata", "Teradata", Boolean.TRUE, "TERADATA", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), // "TERADATA"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    AS400("AS400", "AS400", Boolean.FALSE, "AS400", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Login), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    JAVADB_EMBEDED(
                   "JavaDB Embeded", "JavaDB Embeded", Boolean.FALSE, "JAVADB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    JAVADB_JCCJDBC(
                   "JavaDB JCCJDBC", "JavaDB JCCJDBC", Boolean.FALSE, "JAVADB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    JAVADB_DERBYCLIENT(
                       "JavaDB DerbyClient", "JavaDB DerbyClient", Boolean.FALSE, "JAVADB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    VERTICA(
            "Vertica", "Vertica", Boolean.TRUE, "VERTICA", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    MAXDB("MAXDB", "MaxDB", Boolean.FALSE, "MAXDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    GREENPLUM(
              "Greenplum", "Greenplum", Boolean.TRUE, "GREENPLUM", "GREENPLUM", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    PARACCEL(
             "ParAccel", "ParAccel", Boolean.TRUE, "PARACCEL", "PARACCEL", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    NETEZZA(
            "Netezza", "Netezza", Boolean.FALSE, "NETEZZA", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SAS("SAS", "SAS", Boolean.TRUE, "SAS", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    // General JDBC not support schema defalut
    GENERAL_JDBC(
                 "General JDBC", "General JDBC", Boolean.FALSE, "JDBC", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    EXASOL("Exasol", "Exasol", Boolean.TRUE, "Exasol", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    HIVE("Hive", "Hive", Boolean.FALSE, "HIVE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    SAPHana(
            "SAPHana", "SAPHana", Boolean.TRUE, "SAPHANA", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    H2("H2", "H2", Boolean.FALSE, "H2", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.None), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    REDSHIFT(
             "Redshift", "Redshift", Boolean.TRUE, "REDSHIFT", "REDSHIFT", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    IMPALA(
           "IMPALA", "Impala", Boolean.TRUE, "IMPALA", EDatabaseSchemaOrCatalogMapping.None, EDatabaseSchemaOrCatalogMapping.Schema), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 

    HBASE(
          "HBase", "HBase", Boolean.FALSE, "HBASE", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Column_Family, true), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    MAPRDB(
           "MapRDB", "MapRDB", Boolean.FALSE, "MAPRDB", EDatabaseSchemaOrCatalogMapping.Sid, EDatabaseSchemaOrCatalogMapping.Column_Family, true);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    // displayName is used in Java code.
    private String displayName;

    private Boolean isNeedSchema;

    // dbType is used in compnonent XML file.
    private String dbType;

    // product used for the mappings.
    private String product;

    // needs a mapping for bug 0004305
    private String xmlType;

    private boolean useProvider = false;

    private EDatabaseSchemaOrCatalogMapping catalogMappingField;

    private EDatabaseSchemaOrCatalogMapping schemaMappingField;

    public EDatabaseSchemaOrCatalogMapping getCatalogMappingField() {
        return this.catalogMappingField;
    }

    public EDatabaseSchemaOrCatalogMapping getSchemaMappingField() {
        return this.schemaMappingField;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Boolean isNeedSchema() {
        return this.isNeedSchema;
    }

    public String getXmlName() {
        return this.dbType;
    }

    public String getProduct() {
        return this.product;
    }

    public String getXMLType() {
        return this.xmlType;
    }

    EDatabaseTypeName(String dbType, String displayName, Boolean isNeedSchema, String product,
            EDatabaseSchemaOrCatalogMapping catalogMappingField, EDatabaseSchemaOrCatalogMapping schemaMappingField) {
        this.displayName = displayName;
        this.isNeedSchema = isNeedSchema;
        this.dbType = dbType;
        this.product = product;
        this.xmlType = product;
        this.catalogMappingField = catalogMappingField;
        this.schemaMappingField = schemaMappingField;
    }

    EDatabaseTypeName(String dbType, String displayName, Boolean isNeedSchema, String product,
            EDatabaseSchemaOrCatalogMapping catalogMappingField, EDatabaseSchemaOrCatalogMapping schemaMappingField,
            boolean useProvider) {
        this(dbType, displayName, isNeedSchema, product, catalogMappingField, schemaMappingField);
        this.useProvider = useProvider;
    }

    EDatabaseTypeName(String dbType, String displayName, Boolean isNeedSchema, String product, String xmlType,
            EDatabaseSchemaOrCatalogMapping catalogMappingField, EDatabaseSchemaOrCatalogMapping schemaMappingField) {
        this.displayName = displayName;
        this.isNeedSchema = isNeedSchema;
        this.dbType = dbType;
        this.product = product;
        this.xmlType = xmlType;
        this.catalogMappingField = catalogMappingField;
        this.schemaMappingField = schemaMappingField;
    }

    public static EDatabaseTypeName getTypeFromDbType(String dbType) {
        if (dbType == null) {
            return getTypeFromDispalyNameOriginal(dbType);
        }
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getXmlName().toUpperCase().equals(dbType.toUpperCase())) {
                return typename;
            }
            if (typename.getProduct().toUpperCase().equals(dbType.toUpperCase())) {
                return typename;
            }
        }
        return getTypeFromDispalyNameOriginal(dbType);
    }

    public static EDatabaseTypeName getTypeFromDbType(String dbType, boolean isDefault) {
        if (dbType == null) {
            return getTypeFromDisplayNameOriginal(dbType, isDefault);
        }
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getXmlName().toUpperCase().equals(dbType.toUpperCase())) {
                return typename;
            }
            if (typename.getProduct().toUpperCase().equals(dbType.toUpperCase())) {
                return typename;
            }
        }
        return getTypeFromDisplayNameOriginal(dbType, isDefault);
    }

    public static EDatabaseTypeName getTypeFromDisplayName(String displayName) {
        return getTypeFromDbType(displayName);
    }

    private static EDatabaseTypeName getTypeFromDispalyNameOriginal(String displayName) {
        if (displayName == null) {
            return MYSQL;
        }
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getDisplayName().toLowerCase().equals(displayName.toLowerCase())) {
                return typename;
            }
        }
        return MYSQL;
    }

    public static EDatabaseTypeName getTypeFromDisplayName(String displayName, boolean isDefault) {
        return getTypeFromDisplayNameOriginal(displayName, isDefault);
    }

    private static EDatabaseTypeName getTypeFromDisplayNameOriginal(String displayName, boolean isDefault) {
        if (displayName == null && isDefault) {
            return MYSQL;
        } else if (displayName == null) {
            return null;
        }
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getDisplayName().toLowerCase().equals(displayName.toLowerCase())) {
                return typename;
            }
        }
        return isDefault ? MYSQL : null;
    }

    /**
     * DOC zli Comment method "getTypeFromProductName".
     * 
     * @param productName
     * @return
     */
    public static EDatabaseTypeName getTypeFromProductName(String productName) {
        if (productName == null) {
            return MYSQL;
        }
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getProduct().equals(productName)) {
                return typename;
            }
        }
        return MYSQL;
    }

    /**
     * This is only for the component type, not for the repository.
     * 
     * @param dbType
     * @return
     */
    public static boolean supportDbType(String dbType) {
        for (EDatabaseTypeName typename : EDatabaseTypeName.values()) {
            if (typename.getXmlName().equals(dbType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUseProvider() {
        return useProvider;
    }

    public boolean isSupport() {
        boolean isSupport = true;

        if (EDatabaseTypeName.GODBC == this || EDatabaseTypeName.MSODBC == this) {
            boolean isSupportODBC = CoreRuntimePlugin.getInstance().getProjectPreferenceManager()
                    .getBoolean(IProjectSettingPreferenceConstants.METADATA_DBCONNECTION_ODBC_ENABLE);
            isSupport = isSupportODBC;
        }

        return isSupport;
    }
}
