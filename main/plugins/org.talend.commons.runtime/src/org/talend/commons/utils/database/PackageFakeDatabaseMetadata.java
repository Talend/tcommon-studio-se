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
package org.talend.commons.utils.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.talend.fakejdbc.FakeDatabaseMetaData;

/**
 * created by zshen on Apr 15, 2013 Detailled comment
 * 
 */
public class PackageFakeDatabaseMetadata extends FakeDatabaseMetaData {

    protected Connection connection;

    protected DatabaseMetaData metaData;

    /**
     * DOC xqliu SybaseDatabaseMetaData constructor comment.
     */
    public PackageFakeDatabaseMetadata(Connection connection) throws SQLException {
        this.connection = connection;
        this.metaData = connection.getMetaData();
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return this.metaData.getCatalogs();
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
            throws SQLException {
        return this.metaData.getColumnPrivileges(catalog, schema, table, columnNamePattern);
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        return this.metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        return this.metaData.getDatabaseMajorVersion();
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        return this.metaData.getDatabaseMinorVersion();
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        return this.metaData.getDatabaseProductName();
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return this.metaData.getDatabaseProductVersion();
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        return this.metaData.getDefaultTransactionIsolation();
    }

    @Override
    public int getDriverMajorVersion() {
        return this.metaData.getDriverMajorVersion();
    }

    @Override
    public int getDriverMinorVersion() {
        return this.metaData.getDriverMinorVersion();
    }

    @Override
    public String getDriverName() throws SQLException {
        return this.metaData.getDriverName();
    }

    @Override
    public String getDriverVersion() throws SQLException {
        return this.metaData.getDriverVersion();
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return this.metaData.getPrimaryKeys(catalog, schema, table);
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return this.metaData.getSchemas();
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return this.metaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return this.metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        return this.metaData.getTableTypes();
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        return this.metaData.getTypeInfo();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.metaData.unwrap(iface);
    }
}
