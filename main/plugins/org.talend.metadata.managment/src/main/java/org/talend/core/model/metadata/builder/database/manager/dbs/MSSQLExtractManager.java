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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.core.ICoreService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.metadata.managment.utils.ManagementTextUtils;
import org.talend.utils.sql.metadata.constants.GetColumn;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MSSQLExtractManager extends ExtractManager {

    private static Logger log = Logger.getLogger(MSSQLExtractManager.class);

    public MSSQLExtractManager(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public String getTableNameBySynonyms(Connection conn, String tableName) {

        Statement sta = null;
        ResultSet resultSet = null;

        try {
            if (conn != null && conn.getMetaData().getDatabaseProductName().equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
                String sql = "SELECT object_id ,parent_object_id as parentid, name AS object_name ,   base_object_name as base_name from sys.synonyms where  name ='"
                        + tableName + "'";
                sta = conn.createStatement();
                ExtractMetaDataUtils.getInstance().setQueryStatementTimeout(sta);
                resultSet = sta.executeQuery(sql);
                while (resultSet.next()) {
                    String baseName = resultSet.getString("base_name").trim();
                    if (baseName.contains(".") && baseName.length() > 2) {
                        return baseName.substring(baseName.indexOf(".") + 2, baseName.length() - 1);
                    }
                    return baseName;
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (sta != null) {
                    sta.close();
                }
            } catch (SQLException e) {
                log.equals(e.toString());
            }
        }
        return null;
    }

    @Override
    protected void fillSynonyms(IMetadataConnection metadataConnection, List<TdColumn> metadataColumns, NamedColumnSet table,
            String tableName, DatabaseMetaData dbMetaData) throws SQLException {
        if (metadataConnection == null || dbMetaData == null) {
            return;
        }
        // TDI-19758
        if (dbMetaData.getDatabaseProductName().equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
            // get TABLE_CATALOG ,TABLE_SCHEMA ,TABLE_NAME from base_object_name of sys.synonyms
            ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
            String str = tableName;
            String TABLE_SCHEMA = null;
            String TABLE_NAME = null;
            String splitstr = str;
            int position = 0;
            int count = 0;
            if (tableName != null) {
                while (str.contains(".")) {
                    count++;
                    splitstr = splitstr.substring(splitstr.indexOf("]") + 3, splitstr.length());
                }
                if (str.indexOf("[") == 0) {
                    TABLE_NAME = str.substring(1, str.length() - 1);
                } else if (str.indexOf("[") == -1) {
                    TABLE_NAME = tableName;
                } else {
                    if (count == 2) {
                        str = str.substring(str.indexOf("]") + 3, str.length());
                    }
                    position = str.indexOf("]");
                    TABLE_SCHEMA = str.substring(0, position);
                    TABLE_NAME = str.substring(position + 3, str.length());
                }
            }
            // need to retrieve columns of synonym by useing sql rather than get them from jdbc metadata
            String synSQL = "select * from INFORMATION_SCHEMA.COLUMNS where  TABLE_NAME =\'" + TABLE_NAME + "\'";
            if (null != TABLE_SCHEMA) {
                synSQL += "\nand TABLE_SCHEMA =\'" + TABLE_SCHEMA + "\'";
            }
            if (!("").equals(metadataConnection.getDatabase())) {
                synSQL += "\nand TABLE_CATALOG =\'" + metadataConnection.getDatabase() + "\'";
            }
            Statement sta = extractMeta.getConn().createStatement();
            extractMeta.setQueryStatementTimeout(sta);
            ResultSet columns = sta.executeQuery(synSQL);
            String typeName = null;
            int index = 0;
            List<String> columnLabels = new ArrayList<String>();
            try {
                while (columns.next()) {
                    int column_size = 0;
                    String lenString = null;
                    long numPrecRadix = 0;
                    String columnName = columns.getString(GetColumn.COLUMN_NAME.name());
                    TdColumn column = ColumnHelper.createTdColumn(columnName);

                    String label = column.getLabel();
                    label = ManagementTextUtils.filterSpecialChar(label);
                    String label2 = label;
                    final ICoreService coreService = getCoreService();
                    if (coreService != null && coreService.isKeyword(label)) {
                        label = "_" + label; //$NON-NLS-1$
                    }

                    label = MetadataToolHelper.validateColumnName(label, index, columnLabels);
                    column.setLabel(label);
                    column.setOriginalField(label2);

                    // no need for mssql
                    // if (!ExtractMetaDataUtils.needFakeDatabaseMetaData(metadataConnection.getDbType(),
                    // metadataConnection.isSqlMode())) {
                    // dataType = columns.getInt(GetColumn.DATA_TYPE.name());
                    typeName = columns.getString(GetColumn.DATA_TYPE.name());
                    // }
                    try {
                        lenString = "NUMERIC_PRECISION";
                        column_size = columns.getInt("NUMERIC_PRECISION");
                        if (columns.getString("CHARACTER_MAXIMUM_LENGTH") != null) {
                            column_size = columns.getInt("CHARACTER_MAXIMUM_LENGTH");
                            lenString = "CHARACTER_MAXIMUM_LENGTH";
                        }
                        column.setLength(column_size);
                        numPrecRadix = columns.getLong("NUMERIC_SCALE");
                        column.setPrecision(numPrecRadix);
                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }

                    DatabaseConnection dbConnection = (DatabaseConnection) ConnectionHelper.getConnection(table);
                    String dbmsId = dbConnection == null ? null : dbConnection.getDbmsId();
                    if (dbmsId != null) {
                        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbmsId);
                        String talendType = mappingTypeRetriever.getDefaultSelectedTalendType(typeName,
                                extractMeta.getIntMetaDataInfo(columns, lenString),
                                extractMeta.getIntMetaDataInfo(columns, "NUMERIC_SCALE")); //$NON-NLS-1$
                        column.setTalendType(talendType);
                        String defaultSelectedDbType = MetadataTalendType.getMappingTypeRetriever(dbConnection.getDbmsId())
                                .getDefaultSelectedDbType(talendType);
                        column.setSourceType(defaultSelectedDbType);
                    }
                    try {
                        column.setNullable("YES".equals(columns.getString("IS_NULLABLE"))); //$NON-NLS-1$
                    } catch (Exception e) {
                        log.error(e);
                    }
                    metadataColumns.add(column);
                    columnLabels.add(column.getLabel());
                    index++;
                }
            } finally {
                columns.close();
            }
        }

    }

    @Override
    protected void checkPrecision(TdColumn metadataColumn, String tableName, String dbType, Integer intMetaDataInfo) {
        if ("INT IDENTITY".equals(dbType)) { //$NON-NLS-1$  // && metadataColumn.isKey()

            // for MSSQL bug16852
            // get inent_seed get_incr for schema's length, precision.
            ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
            Integer ident1 = 0;
            Integer ident2 = 0;
            ResultSet resultSet = null;
            PreparedStatement statement = null;
            try {
                statement = extractMeta.getConn().prepareStatement(
                        " select IDENT_SEED ( '" + tableName + "')," + "IDENT_INCR ( '" + tableName + "')"); //$NON-NLS-1$ 
                extractMeta.setQueryStatementTimeout(statement);
                if (statement.execute()) {
                    resultSet = statement.getResultSet();
                    while (resultSet.next()) {
                        String st1 = resultSet.getString(1);
                        String st2 = resultSet.getString(2);
                        Integer valueOf1 = Integer.valueOf(st1);
                        if (valueOf1 != null) {
                            ident1 = valueOf1;
                        }
                        Integer valueOf2 = Integer.valueOf(st2);
                        if (valueOf2 != null) {
                            ident2 = valueOf2;
                        }

                    }
                }

            } catch (Exception e) {
                log.error(e.toString());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        log.error(e.toString());
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        log.error(e.toString());
                    }
                }
            }

            metadataColumn.setLength(ident1);
            metadataColumn.setPrecision(ident2);
        } else {
            super.checkPrecision(metadataColumn, tableName, dbType, intMetaDataInfo);
        }
    }

}
