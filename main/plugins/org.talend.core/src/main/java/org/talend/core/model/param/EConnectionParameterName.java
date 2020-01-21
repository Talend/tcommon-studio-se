// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.param;

import org.talend.core.i18n.Messages;

/**
 * qzhang class global comment. Detailled comment <br/>
 *
 */
public enum EConnectionParameterName {
    SERVER_NAME(Messages.getString("ConnectionParameterName.HostLabel")), //$NON-NLS-1$
    PORT(Messages.getString("ConnectionParameterName.PortLabel")), //$NON-NLS-1$
    SID(Messages.getString("ConnectionParameterName.DatabaseLabel")), //$NON-NLS-1$
    SCHEMA(Messages.getString("ConnectionParameterName.SchemaLabel")), //$NON-NLS-1$
    USERNAME(Messages.getString("ConnectionParameterName.UserLabel")), //$NON-NLS-1$
    PASSWORD(Messages.getString("ConnectionParameterName.PasswordLabel")), //$NON-NLS-1$
    FILE(Messages.getString("ConnectionParameterName.DbfileLabel")), //$NON-NLS-1$
    DIRECTORY(Messages.getString("ConnectionParameterName.DbPathLabel")), //$NON-NLS-1$
    PROPERTIES_STRING("Additional JDBC Parameters"), //$NON-NLS-1$
    DATASOURCE("DataSource"), //$NON-NLS-1$
    DRIVER_JAR("Driver jar"), //$NON-NLS-1$
    DRIVER_CLASS("Driver class"), //$NON-NLS-1$
    LOCAL_SERVICE_NAME("Local Service Name"), //$NON-NLS-1$
    URL("Url"), //$NON-NLS-1$
    HTTPS("https"), //$NON-NLS-1$
    DBPATH("DBPATH"),//$NON-NLS-1$

    // TDQ-17294 msjian: for tSnowflakeInput
    ACCOUNT("connection.account"), //$NON-NLS-1$
    WAREHOUSE("connection.warehouse"), //$NON-NLS-1$
    DB("connection.db"), //$NON-NLS-1$
    SCHEMA_NAME("connection.schemaName"), //$NON-NLS-1$
    JDBC_PARAMETER("connection.jdbcParameters"), //$NON-NLS-1$
    USER_ID("connection.userPassword.userId"), //$NON-NLS-1$
    USER_PASSWORD("connection.userPassword.password"), //$NON-NLS-1$
    SNOWFLAKE_SCHEMA("table.main.schema"), //$NON-NLS-1$
    QUERY("query"), //$NON-NLS-1$
    // TDQ-17294~

    GENERIC_TABLENAME("tableSelection.tablename"),//$NON-NLS-1$
    GENERIC_USERNAME("connection.userPassword.userId"),//$NON-NLS-1$
    GENERIC_PASSWORD("connection.userPassword.password"),//$NON-NLS-1$
    GENERIC_URL("connection.jdbcUrl"),//$NON-NLS-1$
    GENERIC_DRIVER_JAR("connection.driverTable"),//$NON-NLS-1$
    GENERIC_DRIVER_CLASS("connection.driverClass"), //$NON-NLS-1$
    GENERIC_MAPPING_FILE("mappingFile"); //$NON-NLS-1$


    private String displayName;

    /**
     * qzhang ConnectionParameterName constructor comment.
     */
    EConnectionParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
