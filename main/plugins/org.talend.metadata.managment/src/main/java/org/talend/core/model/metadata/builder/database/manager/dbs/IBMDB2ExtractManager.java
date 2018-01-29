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
import java.sql.DatabaseMetaData;
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
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class IBMDB2ExtractManager extends ExtractManager {

    public static final String DATABASE_PRODUCT_NAME = "DB2/"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(IBMDB2ExtractManager.class);

    public IBMDB2ExtractManager(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public String getSchema(IMetadataConnection metadataConnection) {
        String schema = super.getSchema(metadataConnection);
        if (schema == null || EMPTY.equals(schema.trim())) {
            // for bug 13592
            if (metadataConnection != null) {
                String username = metadataConnection.getUsername();
                if (username != null) {
                    schema = username.toUpperCase();
                    metadataConnection.setSchema(schema);
                }
            }
        }
        return schema;
    }

    @Override
    public String getTableNameBySynonyms(Connection conn, String tableName) {
        Statement sta = null;
        ResultSet resultSet = null;

        try {
            if (conn != null && conn.getMetaData().getDatabaseProductName().startsWith(DATABASE_PRODUCT_NAME)) {
                String sql = "SELECT NAME,BASE_NAME FROM SYSIBM.SYSTABLES where TYPE='A' and  name ='" + tableName + "'";
                sta = conn.createStatement();
                ExtractMetaDataUtils.getInstance().setQueryStatementTimeout(sta);
                resultSet = sta.executeQuery(sql);
                while (resultSet.next()) {
                    String baseName = resultSet.getString("base_name").trim();
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

        // bug TDI-19547
        if (dbMetaData.getDatabaseProductName().startsWith(DATABASE_PRODUCT_NAME)) {
            ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
            // need to retrieve columns of synonym by useing sql rather than get them from jdbc metadata
            String synSQL = "SELECT a.*\n" + "FROM SYSCAT.COLUMNS a\n" + "LEFT OUTER JOIN SYSIBM.SYSTABLES b\n"
                    + "ON a.TABNAME = b.NAME\n" + "AND a.TABSCHEMA = b.CREATOR\n" + "where a.TABNAME =" + "\'" + tableName
                    + "\'\n";
            if (!("").equals(metadataConnection.getSchema())) {
                synSQL += "AND b.CREATOR =\'" + metadataConnection.getSchema() + "\'";
            }
            synSQL += "ORDER BY a.COLNO";
            Statement sta = extractMeta.getConn().createStatement();
            extractMeta.setQueryStatementTimeout(sta);
            ResultSet columns = sta.executeQuery(synSQL);
            String typeName = null;
            int index = 0;
            List<String> columnLabels = new ArrayList<String>();
            try {
                while (columns.next()) {
                    long numPrecRadix = 0;
                    String columnName = columns.getString("COLNAME");
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

                    if (!extractMeta.needFakeDatabaseMetaData(metadataConnection)) {
                        // dataType = columns.getInt(GetColumn.DATA_TYPE.name());
                        typeName = columns.getString("TYPENAME");
                    }
                    try {
                        int column_size = columns.getInt("LENGTH");
                        column.setLength(column_size);
                        numPrecRadix = columns.getLong("SCALE");
                        column.setPrecision(numPrecRadix);
                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }

                    DatabaseConnection dbConnection = (DatabaseConnection) ConnectionHelper.getConnection(table);
                    String dbmsId = dbConnection == null ? null : dbConnection.getDbmsId();
                    if (dbmsId != null) {
                        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbmsId);
                        String talendType = mappingTypeRetriever.getDefaultSelectedTalendType(typeName,
                                extractMeta.getIntMetaDataInfo(columns, "LENGTH"), extractMeta.getIntMetaDataInfo(columns, //$NON-NLS-1$
                                        "SCALE")); //$NON-NLS-1$
                        column.setTalendType(talendType);
                        String defaultSelectedDbType = MetadataTalendType.getMappingTypeRetriever(dbConnection.getDbmsId())
                                .getDefaultSelectedDbType(talendType);
                        column.setSourceType(defaultSelectedDbType);
                    }
                    try {
                        column.setNullable("Y".equals(columns.getString("NULLS"))); //$NON-NLS-1$
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

}
