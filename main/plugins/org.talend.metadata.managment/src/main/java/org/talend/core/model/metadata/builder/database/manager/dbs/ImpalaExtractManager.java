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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.cwm.relational.TdColumn;
import org.talend.metadata.managment.connection.manager.ImpalaConnectionManager;
import org.talend.utils.sql.ConnectionUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImpalaExtractManager extends ExtractManager {

    public ImpalaExtractManager() {
        super(EDatabaseTypeName.IMPALA);
    }

    @Override
    protected Map<String, String> retrievePrimaryKeys(DatabaseMetaData dbMetaData, String catalogName, String schemaName,
            String tableName) throws SQLException {
        // no primary key (same as hive?)
        return Collections.emptyMap();
    }

    protected DatabaseMetaData createDatabaseMetaData(IMetadataConnection metadataConnection, boolean needCreateAndClose)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        return ImpalaConnectionManager.getInstance().createConnection(metadataConnection).getMetaData();
    }

    @Override
    public synchronized List<TdColumn> returnMetadataColumnsFormTable(IMetadataConnection metadataConnection, String tableLabel,
            boolean... dontCreateClose) {
        List<TdColumn> metadataColumns = new ArrayList<TdColumn>();

        Connection conn = null;
        try {
            conn = ImpalaConnectionManager.getInstance().createConnection(metadataConnection);
            DatabaseMetaData metaData = conn.getMetaData();
            tableLabel = checkTableLabel(tableLabel);

            List<String> cataAndShema = getTableCatalogAndSchema((DatabaseConnection) metadataConnection.getCurrentConnection(),
                    tableLabel);
            metadataColumns = extractColumns(metaData, metadataConnection, metadataConnection.getDbType(), cataAndShema.get(0),
                    cataAndShema.get(1), tableLabel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                ConnectionUtils.closeConnection(conn);
            }
        }

        return metadataColumns;
    }

    public List<String> returnTablesFormConnection(IMetadataConnection metadataConnection, TableInfoParameters tableInfoParameters) {
        getTableTypeMap().clear();
        List<String> itemTablesName = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = ImpalaConnectionManager.getInstance().createConnection(metadataConnection);
            DatabaseMetaData metaData = conn.getMetaData();
            if (!tableInfoParameters.isUsedName()) {
                if (tableInfoParameters.getSqlFiter() != null && !"".equals(tableInfoParameters.getSqlFiter())) { //$NON-NLS-1$
                    Statement stmt = conn.createStatement();
                    ResultSet rsTables = stmt.executeQuery(tableInfoParameters.getSqlFiter());
                    itemTablesName = ExtractMetaDataFromDataBase.getTableNamesFromQuery(rsTables, conn);
                    rsTables.close();
                    stmt.close();
                }
            } else {
                itemTablesName = retrieveItemTables(metadataConnection, tableInfoParameters, itemTablesName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    //
                }
            }
        }
        return itemTablesName;
    }

    protected ResultSet getResultSetFromTableInfo(TableInfoParameters tableInfo, String namePattern,
            IMetadataConnection iMetadataConnection, String schema) throws SQLException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        String tableNamePattern = "".equals(namePattern) ? null : namePattern; //$NON-NLS-1$
        String[] types = new String[tableInfo.getTypes().size()];
        for (int i = 0; i < types.length; i++) {
            final String selectedTypeName = tableInfo.getTypes().get(i).getName();
            types[i] = selectedTypeName;
        }
        DatabaseMetaData dbMetaData = ImpalaConnectionManager.getInstance().createConnection(iMetadataConnection).getMetaData();

        // rsTables = dbMetaData.getTables(null, ExtractMetaDataUtils.schema, tableNamePattern, types);
        ResultSet rsTableTypes = null;
        rsTableTypes = dbMetaData.getTableTypes();
        Set<String> availableTableTypes = new HashSet<String>();
        String[] neededTableTypes = { ETableTypes.TABLETYPE_TABLE.getName(), ETableTypes.TABLETYPE_VIEW.getName(),
                ETableTypes.TABLETYPE_SYNONYM.getName() };
        while (rsTableTypes.next()) {
            String currentTableType = StringUtils.trimToEmpty(rsTableTypes.getString(ExtractManager.TABLE_TYPE));
            if (ArrayUtils.contains(neededTableTypes, currentTableType)) {
                availableTableTypes.add(currentTableType);
            }
        }
        rsTableTypes.close();
        ResultSet rsTables = dbMetaData.getTables(null, schema, tableNamePattern, types);
        return rsTables;

    }

}
