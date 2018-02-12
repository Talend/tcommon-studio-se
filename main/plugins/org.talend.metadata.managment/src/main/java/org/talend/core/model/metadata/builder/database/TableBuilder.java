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
package org.talend.core.model.metadata.builder.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdTable;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sql.metadata.constants.GetForeignKey;
import org.talend.utils.sql.metadata.constants.GetPrimaryKey;
import org.talend.utils.sql.metadata.constants.TableType;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;

/**
 * @author scorreia
 * 
 * A class for creating Tables from a connection. By default, no column is retrieved.
 * @deprecated this class does not seem to be used anymore. Remove it?
 */
public class TableBuilder extends AbstractTableBuilder<TdTable> {

    private static Logger log = Logger.getLogger(TableBuilder.class);

    private Map<String, PrimaryKey> column2pk = new HashMap<String, PrimaryKey>();

    private Map<String, Set<ForeignKey>> column2foreign = new HashMap<String, Set<ForeignKey>>();

    /**
     * map [name of the PK -> PK].
     */
    private Map<String, PrimaryKey> name2pk = new HashMap<String, PrimaryKey>();

    private Map<String, ForeignKey> name2fk = new HashMap<String, ForeignKey>();

    public TableBuilder(Connection conn) {
        super(conn, TableType.TABLE);
    }

    /**
     * TableBuilder constructor.
     * 
     * @param conn the connection from which the tables will be created.
     * @deprecated this method does not seem to be used anymore. Remove it?
     */
    public TableBuilder(org.talend.core.model.metadata.builder.connection.Connection conn) {
        super(conn, TableType.TABLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.builders.AbstractTableBuilder#createTable()
     */
    @Override
    protected TdTable createTable() {
        return RelationalFactory.eINSTANCE.createTdTable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.builders.AbstractTableBuilder#createTable(java.lang.String, java.lang.String,
     * java.sql.ResultSet)
     */
    @Override
    protected TdTable createTable(String catalogName, String schemaPattern, ResultSet tablesSet) throws SQLException {
        TdTable table = super.createTable(catalogName, schemaPattern, tablesSet);
        table.setTableType(ExtractMetaDataFromDataBase.ETableTypes.TABLETYPE_TABLE.getName());
        table.setLabel(table.getName());
        return table;
    }

    /**
     * Method "getPrimaryKeys".
     * 
     * @param catalogName the catalog name (can be null)
     * @param schemaPattern the schema name (can be null)
     * @param tableName the name of a table
     * @return the primary keys found given the parameters. According the CWM documentation, there is only one primary
     * key per table (primary key may be composed by several columns).
     * @throws SQLException
     */
    public List<PrimaryKey> getPrimaryKeys(String catalogName, String schemaPattern, String tableName) throws SQLException {
        assert catalogName != null || schemaPattern != null;

        List<PrimaryKey> pks = new ArrayList<PrimaryKey>();
        ResultSet primaryKeys = null;
        try {
            // MOD xqliu 2009-07-13 bug 7888
            primaryKeys = ConnectionUtils.getConnectionMetadata(connection).getPrimaryKeys(catalogName, schemaPattern, tableName);
            // ~
            try {
                while (primaryKeys.next()) {
                    PrimaryKey pk = createPrimaryKey(primaryKeys);
                    pks.add(pk);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (primaryKeys != null) {
                    primaryKeys.close();
                }
            }
        } catch (Exception e1) {
            log.warn("Cannot get primary keys with this database driver.", e1);
        }
        return pks;
    }

    /**
     * Method "getPrimaryKey".
     * 
     * @param columnName a column name
     * @return the primary key if any attached to this column. The primary keys must have been created by this table
     * builder.
     */
    public PrimaryKey getPrimaryKey(String columnName) {
        return column2pk.get(columnName);
    }

    /**
     * Method "getForeignKey".
     * 
     * @param columnName a column name
     * @return the foreign key if any attached to this column. The foreign keys must have been created by this table
     * builder.
     */
    public Set<ForeignKey> getForeignKey(String columnName) {
        return column2foreign.get(columnName);
    }

    public List<ForeignKey> getForeignKeys(String catalogName, String schemaPattern, String tableName) throws SQLException {
        List<ForeignKey> fks = new ArrayList<ForeignKey>();
        ResultSet foreignKeys = null;
        try {
            // MOD xqliu 2009-07-13 bug 7888
            foreignKeys = ConnectionUtils.getConnectionMetadata(connection)
                    .getImportedKeys(catalogName, schemaPattern, tableName);
            // ~
            try {
                while (foreignKeys.next()) {
                    ForeignKey fk = createForeignKey(foreignKeys);
                    fks.add(fk);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (foreignKeys != null) {
                    foreignKeys.close();
                }
            }
        } catch (Exception e1) {
            log.warn("Cannot get foreign key with this database driver.", e1);
        }
        // ~
        return fks;
    }

    private ForeignKey createForeignKey(ResultSet foreignKeys) throws SQLException {
        String name = foreignKeys.getString(GetForeignKey.FK_NAME.name());
        String colName = foreignKeys.getString(GetForeignKey.FKCOLUMN_NAME.name());
        ForeignKey foreignKey = this.geFK(name);
        Set<ForeignKey> foreignSet = column2foreign.get(colName);
        if (foreignSet == null) {
            foreignSet = new HashSet<ForeignKey>();
        }
        foreignSet.add(foreignKey);
        column2foreign.put(colName, foreignSet);
        return foreignKey;
    }

    private PrimaryKey createPrimaryKey(ResultSet primaryKeys) throws SQLException {
        String pkName = primaryKeys.getString(GetPrimaryKey.PK_NAME.name());
        String colName = primaryKeys.getString(GetPrimaryKey.COLUMN_NAME.name());
        PrimaryKey primaryKey = this.getPK(pkName);
        column2pk.put(colName, primaryKey);
        return primaryKey;
    }

    private PrimaryKey getPK(String pkName) {
        PrimaryKey primaryKey = this.name2pk.get(pkName);
        if (primaryKey == null) {
            primaryKey = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createPrimaryKey();
            primaryKey.setName(pkName);
            this.name2pk.put(pkName, primaryKey);
        }
        return primaryKey;
    }

    private ForeignKey geFK(String name) {
        ForeignKey foreignKey = this.name2fk.get(name);
        if (foreignKey == null) {
            foreignKey = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createForeignKey();
            foreignKey.setName(name);
            this.name2fk.put(name, foreignKey);
        }
        return foreignKey;
    }

}
