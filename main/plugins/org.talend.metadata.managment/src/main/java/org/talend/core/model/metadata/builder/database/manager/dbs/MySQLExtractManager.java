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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.metadata.managment.utils.DatabaseConstant;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MySQLExtractManager extends ExtractManager {

    private static Logger log = Logger.getLogger(MySQLExtractManager.class);

    public MySQLExtractManager() {
        super(EDatabaseTypeName.MYSQL);
    }

    @Override
    protected void checkUniqueKeyConstraint(String lable, Map<String, String> primaryKeys, Connection connection) {
        try {
            if (connection != null && connection.getMetaData().getDriverName().contains(DatabaseConstant.MYSQL_PRODUCT_NAME)) {// MySql
                ResultSet keys = null;
                PreparedStatement statement = null;
                ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
                try {
                    statement = extractMeta.getConn().prepareStatement("SHOW INDEX FROM `" //$NON-NLS-1$
                            + lable + "` WHERE Non_unique=0 AND Key_name != \'PRIMARY\';"); //$NON-NLS-1$
                    extractMeta.setQueryStatementTimeout(statement);
                    if (statement.execute()) {
                        keys = statement.getResultSet();
                        while (keys.next()) {
                            String field = keys.getString("COLUMN_NAME"); //$NON-NLS-1$
                            primaryKeys.put(field, "PRIMARY KEY"); //$NON-NLS-1$
                        }
                    }

                } catch (Exception e) {
                    log.error(e.toString());
                } finally {
                    if (keys != null) {
                        keys.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.process(e);
        }
    }
}
