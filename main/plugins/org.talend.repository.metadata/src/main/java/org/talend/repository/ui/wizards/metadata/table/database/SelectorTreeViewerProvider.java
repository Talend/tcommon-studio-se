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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.repository.model.provider.AbstractMetadataExtractorViewProvider;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.connection.manager.ImpalaConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.utils.sql.ConnectionUtils;

/**
 * wzhang class global comment. Detailled comment
 */
public class SelectorTreeViewerProvider extends AbstractMetadataExtractorViewProvider {

    public SelectorTreeViewerProvider() {
        super();
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

    @Override
    public Object[] getElements(Object inputElement) {
        List list = (List) inputElement;
        if (list != null && list.size() == 1) {
            Object obj = list.get(0);
            if (obj instanceof TableNode) {
                TableNode node = (TableNode) obj;
                if (node.getType() == TableNode.SCHEMA && " ".equals(node.getValue())) { //$NON-NLS-1$
                    return getChildren(node);
                }
            }
        }

        return list.toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        TableNode tableNode = (TableNode) parentElement;
        List<TableNode> child = tableNode.getChildren();
        boolean extended = false;
        if (!child.isEmpty()) {
            for (TableNode node : child) {
                if (node.getType() == TableNode.TABLE) {
                    extended = true;
                    break;
                }
            }
        }
        // if extended is true, means table already got,no need to get again.
        if (extended) {
            return child.toArray();
        }
        IMetadataConnection metadataConn = tableNode.getMetadataConn();

        Connection conn = null;
        Driver driver = null;

        DatabaseMetaData dbMetaData = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConn.getDbType())) {
            try {
                dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataConn);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        } else if (EDatabaseTypeName.IMPALA.getDisplayName().equalsIgnoreCase(metadataConn.getDbType())) {
            try {
                dbMetaData = ImpalaConnectionManager.getInstance().createConnection(metadataConn).getMetaData();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        } else {
            List list = extractMeta.getConnectionList(metadataConn);
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof Connection) {
                        conn = (Connection) list.get(i);
                    }
                    if (list.get(i) instanceof DriverShim) {
                        driver = (DriverShim) list.get(i);
                    }
                }
            }
            dbMetaData = extractMeta.getDatabaseMetaData(conn, metadataConn.getDbType(), metadataConn.isSqlMode(),
                    metadataConn.getDatabase());
        }

        int type = tableNode.getType();
        orgomg.cwm.objectmodel.core.Package pack = null;

        List<MetadataTable> tableList = new ArrayList<MetadataTable>();

        if (type == tableNode.CATALOG) {
            if (tableNode.getChildren().isEmpty()) {
                pack = tableNode.getCatalog();
            }
        } else if (type == tableNode.SCHEMA) {
            pack = tableNode.getSchema();
        }
        try {
            if (pack != null) {
                TableInfoParameters paras = tableNode.getParas();
                List<ETableTypes> paraType = paras.getTypes();
                Set<String> availableTableTypes = new HashSet<String>();
                for (ETableTypes tableType : paraType) {
                    availableTableTypes.add(tableType.getName());
                }

                // get all tables/views depending the filter selected

                Set<String> tableNameFilter = null;

                if (!paras.isUsedName()) {
                    tableNameFilter = new HashSet<String>();
                    if (paras.getSqlFiter() != null && !"".equals(paras.getSqlFiter())) { //$NON-NLS-1$
                        Statement stmt = extractMeta.getConn().createStatement();
                        extractMeta.setQueryStatementTimeout(stmt);
                        ResultSet rsTables = stmt.executeQuery(paras.getSqlFiter());
                        while (rsTables.next()) {
                            String nameKey = rsTables.getString(1).trim();
                            tableNameFilter.add(nameKey);
                        }
                        rsTables.close();
                        stmt.close();
                    }
                } else {
                    tableNameFilter = paras.getNameFilters();
                }

                List<MetadataTable> tempListTables = new ArrayList<MetadataTable>();
                MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(metadataConn);
                for (String filter : tableNameFilter) {
                    tempListTables = dbInstance.fillAll(pack, dbMetaData, metadataConn, null, filter,
                            availableTableTypes.toArray(new String[] {}));
                    for (MetadataTable table : tempListTables) {
                        boolean contains = false;
                        for (MetadataTable inListTable : tableList) {
                            if (inListTable.getName().equals(table.getName())) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            tableList.add(table);
                        }
                    }
                }
                if (tableNameFilter.isEmpty()) {
                    tempListTables = dbInstance.fillAll(pack, dbMetaData, metadataConn, null, null,
                            availableTableTypes.toArray(new String[] {}));
                    for (MetadataTable table : tempListTables) {
                        boolean contains = false;
                        for (MetadataTable inListTable : tableList) {
                            if (inListTable.getName().equals(table.getName())) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            tableList.add(table);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            String dbType = metadataConn.getDbType();
            // bug 22619
            String driverClass = metadataConn.getDriverClass();
            if (conn != null) {
                ConnectionUtils.closeConnection(conn);
            }
            // for specific db such as derby
            if (driver != null) {
                if ((driverClass != null && driverClass.equals(EDatabase4DriverClassName.JAVADB_EMBEDED.getDriverClass()))
                        || (dbType != null && (dbType.equals(EDatabaseTypeName.JAVADB_EMBEDED.getDisplayName())
                                || dbType.equals(EDatabaseTypeName.JAVADB_DERBYCLIENT.getDisplayName())
                                || dbType.equals(EDatabaseTypeName.JAVADB_JCCJDBC.getDisplayName()) || dbType
                                    .equals(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName())))) {
                    try {
                        driver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
                    } catch (SQLException e) {
                        // exception of shutdown success. no need to catch.
                    }
                }
            }
        }

        transferToTableNode(tableList, tableNode);
        List<TableNode> children = tableNode.getChildren();
        return children.toArray();
    }

    @Override
    public Object getParent(Object element) {
        TableNode tableNode = (TableNode) element;
        return tableNode.getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        TableNode tableNode = (TableNode) element;
        int type = tableNode.getType();
        if (type == TableNode.CATALOG || type == TableNode.SCHEMA) {
            return true;
        }
        return !tableNode.getChildren().isEmpty();
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        TableNode tableNode = (TableNode) element;
        int type = tableNode.getType();
        List<Object> columnData = tableNode.getColumnDataList();
        switch (columnIndex) {
        case 0:
            String value = tableNode.getValue();
            if (value == null) {
                return "";
            }
            return value;
        case 1:
            if (type == TableNode.CATALOG) {
                return "CATALOG";
            } else if (type == TableNode.SCHEMA) {
                return "SCHEMA";
            } else {
                return tableNode.getItemType();
            }
        default:
            if (columnData != null && columnIndex < columnData.size()) {
                Object columnObj = columnData.get(columnIndex);
                if (columnObj != null) {
                    return columnObj.toString();
                }
            }
            return ""; //$NON-NLS-1$
        }
    }

    private void transferToTableNode(List<MetadataTable> list, TableNode parentNode) {
        if (list != null && !list.isEmpty()) {
            for (MetadataTable table : list) {
                if (table instanceof TdTable) {
                    TdTable td = (TdTable) table;
                    TableNode tableNode = new TableNode();
                    tableNode.setType(TableNode.TABLE);
                    tableNode.setValue(td.getLabel());
                    tableNode.setItemType(td.getTableType());
                    tableNode.setTable(td);
                    tableNode.setParent(parentNode);
                    parentNode.addChild(tableNode);
                } else if (table instanceof TdView) {
                    TdView tv = (TdView) table;
                    TableNode tableNode = new TableNode();
                    tableNode.setType(TableNode.TABLE);
                    tableNode.setValue(tv.getLabel());
                    tableNode.setItemType(tv.getTableType());
                    tableNode.setView(tv);
                    tableNode.setParent(parentNode);
                    parentNode.addChild(tableNode);
                }
            }
        }
    }

}
