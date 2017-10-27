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
package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.utils.sql.metadata.constants.TableType;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.record.RecordFile;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.Table;
import orgomg.cwm.resource.relational.impl.RelationalFactoryImpl;

/**
 * @author scorreia
 * 
 * Helper for Table class.
 */
public final class TableHelper extends SubItemHelper {

    private TableHelper() {
    }

    /**
     * Method "getTables" extracts the tables from the list.
     * 
     * @param elements any elements that could contain TdTables
     * @return the list of TdTables found in the given list (never null, but can be empty).
     */
    public static List<TdTable> getTables(Collection<? extends EObject> elements) {
        List<TdTable> tables = new ArrayList<TdTable>();
        for (EObject elt : elements) {
            TdTable table = SwitchHelpers.TABLE_SWITCH.doSwitch(elt);
            if (table != null) {
                // TUP-1102 filter the synonym type, 20131009,yyin
                if (StringUtils.equalsIgnoreCase(TableType.SYNONYM.name(), table.getTableType())) {
                    continue;
                }// ~
                tables.add(table);
            }
        }
        return tables;
    }

    /**
     * Method "getColumns" returns the columns of a table.
     * 
     * @param table a table
     * @return the list of TdColumn contained in the table
     */
    public static List<TdColumn> getColumns(TdTable table) {
        return ColumnHelper.getColumns(table.getFeature());
    }

    /**
     * Method "addColumns".
     * 
     * @param table the table in which to add the columns (must not be null)
     * @param columns the columns to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     */
    public static boolean addColumns(TdTable table, Collection<TdColumn> columns) {
        return ColumnSetHelper.addColumns(table, columns);
    }

    /**
     * Method "addColumn" adds a column to the given table.
     * 
     * @param table the table in which the column is added (must not be null)
     * @param column the column to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     */
    public static boolean addColumn(TdTable table, TdColumn column) {
        assert table != null;
        assert column != null;
        return table.getFeature().add(column);
    }

    /**
     * Method "getParentTable".
     * 
     * @param column a column
     * @return the table containing this column or null
     * @deprecated use ColumnHelper.getTdTableOwner();
     */
    @Deprecated
    public static TdTable getParentTable(TdColumn column) {
        return ColumnHelper.getColumnOwnerAsTdTable(column);
    }

    /**
     * Method "addPrimaryKey".
     * 
     * @param table
     * @param pk the primary key of the table
     */
    public static PrimaryKey addPrimaryKey(TdTable table, PrimaryKey pk) {
        assert table != null;
        assert pk != null;
        PrimaryKey primaryKey = getPrimaryKey(table);
        // PrimaryKey thePrimaryKey = getPrimaryKey(table);
        // MOD zshen for bug 12842
        String newPrimaryKeyName = pk.getName();
        if (primaryKey != null) {
            if (primaryKey.getName().equals(newPrimaryKeyName)) {
                primaryKey.getFeature().addAll(pk.getFeature());
            }
            StructuralFeature[] structuralFeaturethe = primaryKey.getFeature().toArray(
                    new StructuralFeature[primaryKey.getFeature().size()]);
            for (StructuralFeature primaryKeyColumn : structuralFeaturethe) {
                TdColumn theColumn = (TdColumn) (primaryKeyColumn);
                theColumn.getUniqueKey().clear();
                theColumn.getUniqueKey().add(primaryKey);
            }
            return primaryKey;

        }
        table.getOwnedElement().add(pk);
        return pk;
    }

    /**
     * Method "addPrimaryKey".
     * 
     * @param table
     * @param pk the primary key of the table
     */
    public static PrimaryKey addPrimaryKey(TdTable table, TdColumn col) {
        assert table != null;
        assert col != null;
        PrimaryKey primaryKey = getPrimaryKey(table);
        if (primaryKey == null) {
            primaryKey = RelationalFactoryImpl.eINSTANCE.createPrimaryKey();
            table.getOwnedElement().add(primaryKey);
        }
        primaryKey.getFeature().add(col);
        StructuralFeature[] structuralFeaturethe = primaryKey.getFeature().toArray(
                new StructuralFeature[primaryKey.getFeature().size()]);
        for (StructuralFeature primaryKeyColumn : structuralFeaturethe) {
            TdColumn theColumn = (TdColumn) (primaryKeyColumn);
            theColumn.getUniqueKey().clear();
            theColumn.getUniqueKey().add(primaryKey);
        }

        return primaryKey;
    }

    /**
     * removes the key from the table and eventually removes all references from the key referenced colums <br>
     * //TODO handle the foreign key that may reference this primary key
     * 
     * @param columnSet the table in which the key is to be removed
     * @param key the key to be removed
     * @return true if the PK existed in the table
     */
    public static boolean removeTableKey(ColumnSet columnSet, PrimaryKey key) {
        assert columnSet != null;
        assert key != null;
        assert columnSet.getOwnedElement().contains(key);
        List<TdColumn> primaryKeyColumns = KeyHelper.getKeyRelatedColumns(key);
        for (TdColumn column : primaryKeyColumns) {
            ColumnHelper.removeKeyFromColumn(key, column);
        }
        return columnSet.getOwnedElement().remove(key);
    }

    /**
     * removes the key from the table and eventually removes all references from the key referenced colums <br>
     * 
     * @param columnSet the table in which the key is to be removed
     * @param key the key to be removed
     * @return true if the PK existed in the table
     */
    public static boolean removeTableKey(ColumnSet columnSet, ForeignKey key) {
        assert columnSet != null;
        assert key != null;
        assert columnSet.getOwnedElement().contains(key);
        List<TdColumn> primaryKeyColumns = KeyHelper.getKeyRelatedColumns(key);
        for (TdColumn column : primaryKeyColumns) {
            ColumnHelper.removeKeyFromColumn(key, column);
        }
        return columnSet.getOwnedElement().remove(key);
    }

    /**
     * Method "removeForeignKey".
     * 
     * @param table
     * @param foreignKey
     * @return true if the FK existed in the table
     */
    public static boolean removeForeignKey(Table table, ForeignKey foreignKey) {
        assert table != null;
        assert foreignKey != null;
        return table.getOwnedElement().remove(foreignKey);
    }

    /**
     * Method "addAllPrimaryKeys".
     * 
     * @param table
     * @param primaryKeys the primary keys of the table.
     * @deprecated because there can only be one PrimaryKey on same one table, so use addPrimaryKey instead
     */
    @Deprecated
    public static void addPrimaryKeys(ColumnSet table, List<PrimaryKey> primaryKeys) {
        assert table != null;
        assert primaryKeys != null;
        for (PrimaryKey primaryKey : primaryKeys) {
            addPrimaryKey((TdTable) table, primaryKey);
        }

    }

    /**
     * Method "addForeignKey".
     * 
     * @param table
     * @param foreignKey the foreign key of the given table
     */
    public static ForeignKey addForeignKey(TdTable table, ForeignKey foreignKey) {
        assert table != null;
        assert foreignKey != null;
        List<ForeignKey> foreignKeyList = getForeignKeys(table);
        String newForeignKeyName = foreignKey.getName();
        for (ForeignKey theForeignKey : foreignKeyList) {
            if (theForeignKey.getName().equals(newForeignKeyName)) {
                theForeignKey.getFeature().addAll(foreignKey.getFeature());
                StructuralFeature[] structuralFeaturethe = theForeignKey.getFeature().toArray(
                        new StructuralFeature[theForeignKey.getFeature().size()]);
                for (StructuralFeature foreignKeyColumn : structuralFeaturethe) {
                    TdColumn theColumn = (TdColumn) (foreignKeyColumn);
                    theColumn.getKeyRelationship().remove(foreignKey);
                    theColumn.getKeyRelationship().add(theForeignKey);
                }
                return theForeignKey;
            }
        }
        table.getOwnedElement().add(foreignKey);
        return foreignKey;
    }

    /**
     * Method "addForeignKeys".
     * 
     * @param table
     * @param foreignKeys the foreign keys of this table
     * @deprecated cause only working on TdTable and not on ColumnSet use addForeignKeys(Table, List<ForeignKey>)
     */
    @Deprecated
    public static void addForeignKeys(ColumnSet table, List<ForeignKey> foreignKeys) {
        assert table != null;
        assert foreignKeys != null;
        for (ForeignKey foreignKey : foreignKeys) {
            addForeignKey((TdTable) table, foreignKey);
        }

    }

    /**
     * Method "addForeignKeys".
     * 
     * @param table
     * @param foreignKeys the foreign keys of this table
     */
    public static boolean addForeignKeys(TdTable table, List<ForeignKey> foreignKeys) {
        assert table != null;
        assert foreignKeys != null;
        return table.getOwnedElement().addAll(foreignKeys);
    }

    /**
     * Method "addForeignKey".
     * 
     * @param table
     * @param foreignKey the foreign key of the given table
     */
    public static ForeignKey addForeignKey(TdTable table, ForeignKey foreignKey, TdColumn column) {
        assert table != null;
        assert foreignKey != null;
        List<ForeignKey> foreignKeyList = getForeignKeys(table);
        String newForeignKeyName = foreignKey.getName();
        for (ForeignKey theForeignKey : foreignKeyList) {
            if (theForeignKey.getName().equals(newForeignKeyName)) {
                theForeignKey.getFeature().add(column);
                StructuralFeature[] structuralFeaturethe = theForeignKey.getFeature().toArray(
                        new StructuralFeature[theForeignKey.getFeature().size()]);
                for (StructuralFeature foreignKeyColumn : structuralFeaturethe) {
                    TdColumn theColumn = (TdColumn) (foreignKeyColumn);
                    theColumn.getKeyRelationship().remove(foreignKey);
                    theColumn.getKeyRelationship().add(theForeignKey);
                }
                return theForeignKey;
            }
        }
        table.getOwnedElement().add(foreignKey);
        return foreignKey;
    }

    /**
     * Method "getPrimaryKeys".
     * 
     * @param table a table
     * @return a list of all primary keys of the given table
     * @deprecated use getPrimaryKey() instead
     */
    @Deprecated
    public static List<PrimaryKey> getPrimaryKeys(Table table) {
        List<PrimaryKey> primarykeys = new ArrayList<PrimaryKey>();
        EList<ModelElement> ownedElements = table.getOwnedElement();
        for (ModelElement modelElement : ownedElements) {
            PrimaryKey pk = SwitchHelpers.PRIMARY_KEY_SWITCH.doSwitch(modelElement);
            if (pk != null) {
                primarykeys.add(pk);
            }
        }
        return primarykeys;
    }

    /**
     * Method "getForeignKeys".
     * 
     * @param table a table
     * @return a list of all foreign keys of the given table
     */
    public static List<ForeignKey> getForeignKeys(TdTable table) {
        List<ForeignKey> foreignkeys = new ArrayList<ForeignKey>();
        EList<ModelElement> ownedElements = table.getOwnedElement();
        for (ModelElement modelElement : ownedElements) {
            ForeignKey pk = SwitchHelpers.FOREIGN_KEY_SWITCH.doSwitch(modelElement);
            if (pk != null) {
                foreignkeys.add(pk);
            }
        }
        return foreignkeys;
    }

    /**
     * Method "getParentCatalogOrSchema" returns the owner of the element (Catalog or Schema).
     * 
     * @param element (can be null)
     * @return the Catalog or of Schema or null
     * @deprecated use PackageHelper.getParentPackage()
     */
    @Deprecated
    public static Package getParentCatalogOrSchema(ModelElement element) {
        if (element == null) {
            return null;
        }
        Namespace namespace = element.getNamespace();
        return PackageHelper.getCatalogOrSchema(namespace);
    }

    /**
     * return the primary key associated with this table or null if there is none
     * 
     * @param table to search in for a PK
     * @return the PK found or null
     */
    public static PrimaryKey getPrimaryKey(TdTable table) {
        PrimaryKey result = null;
        EList<ModelElement> ownedElements = table.getOwnedElement();
        // look for the first primaryKey in the owned element list
        for (ModelElement modelElement : ownedElements) {
            PrimaryKey pk = SwitchHelpers.PRIMARY_KEY_SWITCH.doSwitch(modelElement);
            if (pk != null) {
                result = pk;
                break;
            }
        }
        return result;
    }

    /**
     * removes the column from the MetadataTable, if it is a TdColumn then try to remove it from the primary key and the
     * foreign key that contains it. this eventually removes the private key and foreign key from the table if the are
     * no more refering to any column
     * 
     * @param column to be removed
     * @param metadataTable the set to remove the column from
     */
    public static void removeColumn(MetadataColumn column, MetadataTable metadataTable) {
        if (column instanceof TdColumn) {// if of DbColumn then check for
            TdColumn tdColumn = (TdColumn) column;
            // first remove PK if it exists
            if (ColumnHelper.isPrimaryKey(tdColumn)) {
                ColumnHelper.removeColumnFromTablePrimaryKey(tdColumn);
            }

            // remove foreign key if it exists
            if (ColumnHelper.isForeignKey(tdColumn)) {
                ColumnHelper.removeColumnFromTableForeignKey(tdColumn);
            }
        }// else not a db column so simply removes the reference
        metadataTable.getFeature().remove(column);
    }

    /**
     * 
     * DOC mzhao 2009-03-12 Remove all columns from this column set.
     * 
     * @param column
     * @param metadataTable
     * @return true if remove successfully, false or else.
     */
    public static void clearAllColumns(MetadataTable metadataTable) {
        // convert to an array because we cannon remove elements from a collection during a for loop
        MetadataColumn[] columns = metadataTable.getColumns().toArray(new MetadataColumn[metadataTable.getColumns().size()]);
        for (MetadataColumn col : columns) {
            removeColumn(col, metadataTable);
        }
    }

    /**
     * Method "addColumns".
     * 
     * @param metadataTable the column set in which to add the columns (must not be null)
     * @param columns the columns to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     */
    public static boolean addColumns(MetadataTable metadataTable, Collection<? extends TdColumn> columns) {
        assert metadataTable != null;
        assert columns != null;
        return metadataTable.getFeature().addAll(columns);
    }

    /**
     * Method "setColumns" replaces the previous columns by the new ones.
     * 
     * @param metadataTable the column set in which to add the columns (must not be null)
     * @param columns the columns to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     * @deprecate use MetadataTable.getColumns.add()
     */
    public static boolean setColumns(MetadataTable metadataTable, Collection<? extends TdColumn> columns) {
        assert metadataTable != null;
        assert columns != null;
        metadataTable.getColumns().clear();
        return metadataTable.getColumns().addAll(columns);
    }

    // /**
    // * Method "getTableNames".
    // *
    // * @param metadataTable a set of columns (that could come from several Tables or views)
    // * @return the list of container names (tables, views) which the columns belong to (not null).
    // */
    // public static String[] getColumnSetNames(Collection<? extends MetadataColumn> columns) {
    // List<String> tableNames = new ArrayList<String>();
    // for (MetadataColumn tdColumn : columns) {
    // String tableName = ColumnHelper.getTableFullName(tdColumn);
    // if (tableName != null) {
    // tableNames.add(tableName);
    // }
    // }
    // return tableNames.toArray(new String[tableNames.size()]);
    // }

    /**
     * DOC bZhou Comment method "isFromSamePackage".
     * 
     * this method is used to judge columnset whether from same package.
     * 
     * @param sets
     * @return
     */
    public static boolean isFromSamePackage(MetadataTable... sets) {
        Set<Package> container = new HashSet<Package>();

        for (MetadataTable set : sets) {
            container.add(PackageHelper.getParentPackage(set));
        }

        return container.size() == 1;
    }

    /**
     * DOC bZhou Comment method "getTableFilter".
     * 
     * @param element
     * @return
     */
    public static String getTableFilter(ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.TABLE_FILTER, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setTableFilter".
     * 
     * @param filter
     * @param element
     */
    public static void setTableFilter(String filter, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.TABLE_FILTER, filter);
    }

    /**
     * DOC bZhou Comment method "getViewFilter".
     * 
     * @param element
     * @return
     */
    public static String getViewFilter(ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.VIEW_FILTER, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setViewFilter".
     * 
     * @param filter
     * @param element
     */
    public static void setViewFilter(String filter, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.VIEW_FILTER, filter);
    }

    /**
     * DOC bZhou Comment method "getComment".
     * 
     * @param element
     * @return
     */
    public static String getComment(ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.COMMENT, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setComment".
     * 
     * @param comment
     * @param element
     */
    public static void setComment(String comment, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.COMMENT, comment);
    }

    /**
     * return the first connection available that refers to this table by moving up to the last Package instance that
     * hold this table and finding the connection linked to it
     * 
     * @param metadataTable
     * @return
     */
    public static Connection getFirstConnection(MetadataTable metadataTable) {
        assert metadataTable != null;
        Connection connection = null;
        Namespace namespace = metadataTable.getNamespace();
        // for the file connection
        if (namespace != null && namespace instanceof RecordFile) {
            return ConnectionHelper.getConnection((RecordFile) namespace);
        }

        // for the mdm connection
        if (namespace != null && namespace instanceof TdXmlSchema) {
            return ConnectionHelper.getConnection((Package) namespace);
        }

        if (namespace != null) {
            Package thePackage = SwitchHelpers.PACKAGE_SWITCH.doSwitch(namespace);
            if (thePackage != null) {
                connection = getFirstPackageConnection(thePackage);
            }

        }// else class is not linked to any package
        return connection;
    }

    /**
     * DOC sgandon Comment method "getPackageDataProvider".
     * 
     * @param result
     * @param thePackage
     * @return
     */
    private static Connection getFirstPackageConnection(Package thePackage) {
        assert thePackage != null;
        Connection connection = null;
        // check that package does not belong to another package
        Namespace parentNS = thePackage.getNamespace();
        if (parentNS != null) {
            Package parentPackage = SwitchHelpers.PACKAGE_SWITCH.doSwitch(thePackage.getNamespace());
            if (parentPackage != null) {
                connection = getFirstPackageConnection(parentPackage);
            }// else class is not linked to any package so get it's Connection
        }
        if (connection == null) {
            for (DataManager dm : thePackage.getDataManager()) {
                Connection conn = SwitchHelpers.CONNECTION_SWITCH.doSwitch(dm);
                if (conn != null) {
                    connection = conn;
                    break;
                }// else keep going
            }
        } // else connection as been found in recurstive call

        return connection;
    }

    /**
     * DOC tguiu Comment method "doGetTableNames".
     * 
     * @param connection
     * @return
     */
    private static List<String> doGetTableNames(Connection connection) {
        List<String> result = new ArrayList<String>(15);
        for (MetadataTable table : ConnectionHelper.getTables(connection)) {
            if (table == null) {
                continue;
            }
            result.add(table.getLabel());
        }
        return result;
    }

    private static List<String> doGetTableNames(SAPFunctionUnit functionUnit) {
        List<String> result = new ArrayList<String>(15);
        for (Object table : functionUnit.getTables()) {
            result.add(((MetadataTable) table).getLabel());
        }
        return result;
    }

    /**
     * 
     * DOC tguiu Comment method "findByLabel".
     * 
     * @deprecated it would be better to use find with some unique identifier
     * @param connection
     * @param label
     * @return
     */
    @Deprecated
    public static MetadataTable findByLabel(Connection connection, String label) {
        if (connection == null) {
            throw new IllegalArgumentException("null connection"); //$NON-NLS-1$
        }
        if (label == null || "".equals(label)) {
            throw new IllegalArgumentException("null/empty label"); //$NON-NLS-1$
        }
        Set<MetadataTable> tables = ConnectionHelper.getTables(connection);
        for (MetadataTable table : tables) {
            if (label.equals(table.getLabel())) {
                return table;
            }
        }
        return null;
    }

    public static MetadataTable findByLabel(SAPFunctionUnit functionUnit, String label) {
        if (functionUnit == null) {
            throw new IllegalArgumentException("null connection"); //$NON-NLS-1$
        }
        if (label == null || "".equals(label)) {
            throw new IllegalArgumentException("null/empty label"); //$NON-NLS-1$
        }
        EList tables = functionUnit.getTables();
        for (int i = 0; i < tables.size(); i++) {
            MetadataTable table = (MetadataTable) tables.get(i);
            if (label.equals(table.getLabel())) {
                return table;
            }
        }
        return null;
    }

    public static String[] getTableNames(Connection connection) {
        List<String> result = doGetTableNames(connection);
        return result.toArray(new String[result.size()]);
    }

    public static String[] getTableNames(Connection connection, String discardedValued) {
        List<String> result = doGetTableNames(connection);
        result.remove(discardedValued);
        return result.toArray(new String[result.size()]);
    }

    public static String[] getTableNames(SAPFunctionUnit functionUnit) {
        List<String> result = doGetTableNames(functionUnit);
        return result.toArray(new String[result.size()]);
    }

    public static String[] getTableNames(SAPFunctionUnit functionUnit, String discardedValued) {
        List<String> result = doGetTableNames(functionUnit);
        result.remove(discardedValued);
        return result.toArray(new String[result.size()]);
    }

    public static Map<String, Integer> getForeignKeysInformation(TdTable table) {
        Map<String, Integer> info = new HashMap<String, Integer>();

        for (ForeignKey foreign : getForeignKeys(table)) {
            info.put(foreign.getName(), foreign.getFeature().size());

        }
        return info;
    }
}
