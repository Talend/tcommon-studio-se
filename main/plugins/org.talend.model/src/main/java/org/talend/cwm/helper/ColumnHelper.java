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
package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import orgomg.cwm.foundation.keysindexes.KeyRelationship;
import orgomg.cwm.foundation.keysindexes.UniqueKey;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.Attribute;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Utility class for handling (Td)Columns.
 */
public final class ColumnHelper {

    private static Logger log = Logger.getLogger(ColumnHelper.class);

    private ColumnHelper() {
    }

    /**
     * Method "createColumn" creates a column with the given name.
     * 
     * @param name the name of the column
     * @return the created column.
     * @deprecated use createTdColumn() instead
     */
    @Deprecated
    public static TdColumn createColumn(String name) {
        return createTdColumn(name);
    }

    /**
     * Method "createColumn" creates a column with the given name.
     * 
     * @param name the name of the column
     * @return the created column.
     */
    public static TdColumn createTdColumn(String name) {
        TdColumn column = RelationalFactory.eINSTANCE.createTdColumn();
        column.setName(name);
        column.setLabel(name);
        return column;
    }

    /**
     * Method "getColumns" returns the columns that are in the list.
     * 
     * @param elements a list with various elements
     * @return the list of TdColumn contained in the given list
     */
    public static List<TdColumn> getColumns(EList<? extends EObject> elements) {
        List<TdColumn> columns = new ArrayList<TdColumn>();
        for (EObject elt : elements) {
            if (elt != null) {
                TdColumn col = SwitchHelpers.COLUMN_SWITCH.doSwitch(elt);
                if (col != null) {
                    columns.add(col);
                }
            }
        }
        return columns;
    }

    /**
     * Method "getColumnNames".
     * 
     * @param columnSet
     * @return the list of column names (with their table names specified)
     */
    public static String[] getColumnFullNames(ColumnSet columnSet) {
        List<TdColumn> columns = ColumnSetHelper.getColumns(columnSet);
        return getColumnFullNames(columns);
    }

    /**
     * Method "getColumnNames".
     * 
     * @param columnSet
     * @return the list of column names (with their table names specified)
     */
    public static String[] getColumnFullNames(Collection<? extends TdColumn> columns) {
        Set<String> columnNames = new HashSet<String>();
        for (TdColumn column : columns) {
            String colName = getFullName(column);
            columnNames.add(colName);
        }

        return columnNames.toArray(new String[columnNames.size()]);
    }

    /**
     * Method "getFullName" the name of the column with the table (or view) name in front of it. E.g. "Table.TdColumn".
     * 
     * @param column
     * @return the name of the column or null
     */
    public static String getFullName(TdColumn column) {
        String tableName = getTableFullName(column);
        if (tableName != null) {
            return tableName + "." + column.getName(); //$NON-NLS-1$
        }
        return column.getName();
    }

    /**
     * Method "getColumnSetFulName" the name of the container of the column. E.g. a table or a view.
     * 
     * @param column a column
     * @return the name of the container of the column or null
     * @deprecated use getTableFullName()
     */
    @Deprecated
    public static String getColumnSetFullName(MetadataColumn column) {
        return getTableFullName(column);
    }

    /**
     * The name of the container of the column. E.g. a table or a view.
     * 
     * @param column a column
     * @return the name of the container of the column or null
     */
    public static String getTableFullName(MetadataColumn column) {
        MetadataTable table = getColumnOwnerAsMetadataTable(column);
        return table != null ? table.getName() : null;
    }

    /**
     * Method "getColumnSetOwner".
     * 
     * @param column
     * @return the owner of the given column or null
     * @deprecated use {@link #getColumnOwnerAsColumnSet(ModelElement)}
     */
    @Deprecated
    public static ColumnSet getColumnSetOwner(ModelElement column) {
        return getColumnOwnerAsColumnSet(column);
    }

    /**
     * return the owner of the column or null if none avaialble
     * 
     * @param column to find the owner of
     * @return the owner of the given column or null
     */
    public static ColumnSet getColumnOwnerAsColumnSet(ModelElement column) {
        assert column != null;
        Classifier owner = (Classifier) column.eContainer();
        if (owner == null) {
            return null;
        }

        ColumnSet set = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(owner);
        MetadataTable mdColumn = SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(owner);
        if (null == set && mdColumn != null) {
            return null;
        }
        return set;
    }

    /**
     * return the owner of the column or null if none avaialble ADD yyi 2011-03-01 17871
     * 
     * @param column to find the owner of
     * @return the owner of the given column or null
     */
    public static MetadataTable getColumnOwnerAsMetadataTable(ModelElement column) {
        assert column != null;
        Classifier owner = (Classifier) column.eContainer();
        if (owner == null) {
            return null;
        }

        MetadataTable mdColumn = SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(owner);
        if (mdColumn != null) {
            return mdColumn;
        }
        return null;
    }

    /**
     * return the table owning this colum if the is any or null.<br>
     * Null result may mean the colum has no owner or the owner of the column is not a table (it may be a view)
     * 
     * @param column colum to fing the owner of, never null
     * @return the table containing this column or null
     */
    public static TdTable getColumnOwnerAsTdTable(TdColumn column) {
        assert column != null;
        return SwitchHelpers.TABLE_SWITCH.doSwitch(column.getOwner());
    }

    /**
     * return the metadata table owning this colum if the is any or null.<br>
     * Null result may mean the colum has no owner
     * 
     * @param metadataColumnImpl
     * @return the table owner or null
     */
    public static MetadataTable getColumnOwnerAsMetadataTable(MetadataColumn column) {
        assert column != null;
        if (column.getOwner() == null) {
            return null;
        }
        return SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(column.getOwner());
    }

    /**
     * set the column to the given column set
     * 
     * @param column the colum to be owned (never null)
     * @param colSet the Table or View to be the owner of the column may be null
     */
    public static void setColumnOwner(TdColumn column, ColumnSet colSet) {
        assert column != null;
        column.setOwner(colSet);
    }

    /**
     * return the default value for the given column or null if none.<br>
     * 
     * @param column a column
     * @return the default value of a column or null
     */
    public static String getDefaultValue(Attribute attribute) {
        assert attribute != null;
        Expression initialValue = attribute.getInitialValue();
        return initialValue != null ? initialValue.getBody() : null;
    }

    /**
     * set the default value for the column
     * 
     * @param column a column
     * @return the default value of a column or null
     */
    public static void setDefaultValue(Attribute attribute, String value) {
        assert attribute != null;
        Expression initialValue = attribute.getInitialValue();
        if (initialValue == null) {
            initialValue = CoreFactory.eINSTANCE.createExpression();
        }
        initialValue.setBody(value);
        attribute.setInitialValue(initialValue);
    }

    /**
     * Method "isPrimaryKey".
     * 
     * @param column a column
     * @return true if the given column is a primary key
     */
    public static boolean isPrimaryKey(TdColumn column) {
        assert column != null;
        return getPrimaryKey(column) != null;
    }

    /**
     * return the PrimaryKey object related to this column
     * 
     * @param column a column
     * @return the primary key object if this column is a primary key or null
     */
    public static PrimaryKey getPrimaryKey(TdColumn column) {
        assert column != null;
        EList<UniqueKey> uniqueKeys = column.getUniqueKey();
        for (UniqueKey uniqueKey : uniqueKeys) {
            if (uniqueKey != null) {
                PrimaryKey pk = SwitchHelpers.PRIMARY_KEY_SWITCH.doSwitch(uniqueKey);
                if (pk != null) {
                    return pk;
                }
            }
        }
        return null;
    }

    /**
     * Method "removePrimaryKey".
     * 
     * @param column
     * @return the primary key object or null
     * @deprecated use removeColumnFromTablePrimaryKey instead
     */
    @Deprecated
    public static PrimaryKey removePrimaryKey(TdColumn column) {
        assert column != null;
        PrimaryKey primaryKey = getPrimaryKey(column);
        if (primaryKey != null) {
            removeColumnFromTablePrimaryKey(column);
        } // else not a column in the primary key
        return primaryKey;
    }

    /**
     * 
     * DOC mzhao Comment method "isForeignKey",Feature 8690.
     * 
     * @param column
     * @return
     */
    public static boolean isForeignKey(TdColumn column) {
        assert column != null;
        return getForeignKey(column) != null && getForeignKey(column).size() > 0;
    }

    /**
     * 
     * DOC mzhao Comment method "getForeignKey",Feature 8690.
     * 
     * @param column
     * @return
     */
    public static Set<ForeignKey> getForeignKey(TdColumn column) {
        assert column != null;
        EList<KeyRelationship> foreignKeys = column.getKeyRelationship();
        Set<ForeignKey> foreignKeySet = new HashSet<ForeignKey>();
        for (KeyRelationship foreignKey : foreignKeys) {
            if (foreignKey != null) {
                ForeignKey fk = SwitchHelpers.FOREIGN_KEY_SWITCH.doSwitch(foreignKey);
                if (fk != null) {
                    foreignKeySet.add(fk);
                }
            }
        }
        return foreignKeySet;
    }

    /**
     * Method "removeForeignKey".
     * 
     * @param column
     * @return the removed Foreign key or null
     */
    public static Set<ForeignKey> removeForeignKey(TdColumn column) {
        assert column != null;
        Set<ForeignKey> foreignKeySet = getForeignKey(column);
        if (foreignKeySet != null) {
            for (ForeignKey foreignKey : foreignKeySet) {
                column.getKeyRelationship().remove(foreignKey);
            }
            return foreignKeySet;

        }
        // else
        return null;
    }

    /**
     * DOC bZhou Comment method "isFromSameConnection".
     * 
     * @param columns
     * @return
     */
    public static boolean isFromSameConnection(List<TdColumn> columns) {
        assert columns != null;

        Set<DataProvider> dataProviderSets = new HashSet<DataProvider>();
        for (TdColumn column : columns) {
            ColumnSet columnSetOwner = getColumnSetOwner(column);
            DataProvider dp = ConnectionHelper.getConnection(columnSetOwner);
            dataProviderSets.add(dp);
        }
        return dataProviderSets.size() == 1;
    }

    /**
     * DOC bZhou Comment method "isFromSameTable".
     * 
     * @param columns
     * @return
     */
    public static boolean isFromSameTable(List<TdColumn> columns) {
        assert columns != null;

        Set<ColumnSet> columnSets = new HashSet<ColumnSet>();
        for (TdColumn column : columns) {
            ColumnSet columnSetOwner = getColumnSetOwner(column);
            columnSets.add(columnSetOwner);
        }
        return columnSets.size() == 1;
    }

    /**
     * DOC bZhou Comment method "getColumnFilter".
     * 
     * @param element
     * @return
     */
    public static String getColumnFilter(ModelElement element) {
        assert element != null;
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.COLUMN_FILTER, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setColumnFilter".
     * 
     * @param filter
     * @param element
     */
    public static void setColumnFilter(String filter, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.COLUMN_FILTER, filter);
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
     * This checks whether the column belongs to the primary key or not, if yes then do nothing. If it is not a primary
     * key, creates a PrimaryKey instance with no name (null), assign it to the column and to the associated table
     * Therefore This method must be called once the colSet is associated to a Table
     * 
     * @param column the column to be considered a primary key
     */
    public static void addColumnToTablePrimaryKey(TdColumn column) {
        assert column != null;
        TdTable table = getColumnOwnerAsTdTable(column);
        assert table != null;
        if (!isPrimaryKey(column)) {
            PrimaryKey primaryKey = TableHelper.getPrimaryKey(table);
            if (primaryKey == null) {// no pk for this table so create one
                // create a new PK, associate it with the table and the column
                primaryKey = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createPrimaryKey();
                TableHelper.addPrimaryKey(table, primaryKey);
            } // else there is already a PK so link it to the column
            ColumnHelper.addKeyToColumn(primaryKey, column);
        } // else already a primary key so ignor
    }

    /**
     * This adds the primary key to the colum key references
     * 
     * @param key the key to reference
     * @param column the column that need reference the key
     */
    public static void addKeyToColumn(PrimaryKey key, TdColumn column) {
        column.getUniqueKey().add(key);
    }

    /**
     * This removes the primary key from the colum key references
     * 
     * @param key the key to reference
     * @param column the column that need reference the key
     */
    public static void removeKeyFromColumn(PrimaryKey key, TdColumn column) {
        column.getUniqueKey().remove(key);
    }

    /**
     * This adds the foreign key to the colum key references
     * 
     * @param key the key to reference
     * @param column the column that need reference the key
     */
    public static void addKeyToColumn(ForeignKey key, TdColumn column) {
        column.getKeyRelationship().add(key);
    }

    /**
     * This removes the foreign key from the colum key references
     * 
     * @param key the key to reference
     * @param column the column that need reference the key
     */
    public static void removeKeyFromColumn(ForeignKey key, TdColumn column) {
        column.getKeyRelationship().remove(key);
    }

    /**
     * Removes the column from the foreign key. This removes the reference to the foreign key in the column and removes
     * the foreign key ref from the table if it is the last one. <br>
     * 
     * @param column the column to be removed from the foreign key
     */
    public static void removeColumnFromTableForeignKey(TdColumn column) {
        assert column != null;
        TdTable table = getColumnOwnerAsTdTable(column);
        assert table != null;
        if (isForeignKey(column)) {
            Set<ForeignKey> foreignKeySet = getForeignKey(column);
            Iterator<ForeignKey> iter = foreignKeySet.iterator();
            while (iter.hasNext()) {
                ForeignKey foreignKey = iter.next();
                removeKeyFromColumn(foreignKey, column);
                // check if key was only related to this column
                List<TdColumn> primaryKeyColumns = KeyHelper.getKeyRelatedColumns(foreignKey);
                if (primaryKeyColumns.size() == 0) {
                    TableHelper.removeTableKey(table, foreignKey);
                } // else key is linked to other columns so do nothing
            }
        } // else nothing to remove caus column is not a primaryKey
    }

    /**
     * Removes the column from the primary key. This removes the reference to the primary key in the column and removes
     * the Primary key ref from the table if it is the last one. <br>
     * //TODO handle the possible foreign keys that may be pointing to this primary key
     * 
     * @param column the column to be removed from the primary key
     */
    public static void removeColumnFromTablePrimaryKey(TdColumn column) {
        assert column != null;
        TdTable table = getColumnOwnerAsTdTable(column);
        assert table != null;
        if (isPrimaryKey(column)) {
            PrimaryKey primaryKey = getPrimaryKey(column);
            removeKeyFromColumn(primaryKey, column);
            // check if key was only related to this column
            List<TdColumn> primaryKeyColumns = KeyHelper.getKeyRelatedColumns(primaryKey);
            if (primaryKeyColumns.size() == 0) {
                TableHelper.removeTableKey(table, primaryKey);
            } // else key is linked to other columns so do nothing
        } // else nothing to remove caus column is not a primaryKey
    }

    public static MetadataColumn copyColumn(MetadataColumn column, String newId) {
        MetadataColumn newColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        newColumn.setName(newId);
        newColumn.setLabel(column.getLabel());
        newColumn.setKey(column.isKey());
        newColumn.setLength(column.getLength());
        newColumn.setDefaultValue(column.getDefaultValue());
        newColumn.setPrecision(column.getPrecision());
        newColumn.setSourceType(column.getSourceType());
        newColumn.setTalendType(column.getTalendType());
        newColumn.setNullable(column.isNullable());
        newColumn.setComment(column.getComment());
        newColumn.setPattern(column.getPattern());
        return newColumn;
    }

    /**
     * 
     * Get the column index by MetadataColumn.
     * 
     * @param mColumn
     * @return
     */
    public static Integer getColumnIndex(MetadataColumn mColumn) {
        MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable(mColumn);
        MetadataColumn mc = null;
        EList<MetadataColumn> columnLs = mTable.getColumns();
        Integer index = null;
        for (int i = 0; i < columnLs.size(); i++) {
            mc = columnLs.get(i);
            if (mColumn.equals(mc)) {
                index = Integer.valueOf(i);
                break;
            }
        }
        return index;
    }

    /**
     * 
     * Given connection and column set name, return a list of TdColumn.<BR>
     * Note that this method won't connect database, but iterate each existing element (catalog, schema,table) in
     * connection in order to get the columns.
     * 
     * @param connection
     * @param columnSetName
     * @return
     */
    public static List<TdColumn> genNewColumnListByTableName(Connection connection, String columnSetName) {
        List<TdColumn> columnList = new ArrayList<TdColumn>();
        Set<Catalog> catalogList = ConnectionHelper.getAllCatalogs(connection);
        Set<Schema> schemaList = ConnectionHelper.getAllSchemas(connection);
        if (catalogList != null && catalogList.size() != 0) {
            for (Catalog catalog : catalogList) {
                List<Schema> schemesInCatalog = CatalogHelper.getSchemas(catalog);
                if (schemesInCatalog != null && schemesInCatalog.size() != 0) {
                    // Connection has both catalog and schema.
                    for (Schema schemaInCat : schemesInCatalog) {
                        columnList = genNewColumnsFromPackageByTableName(schemaInCat, columnSetName);
                        if (columnList.size() > 0) {
                            return columnList;
                        }
                    }
                } else {
                    // Connection has catalog only
                    columnList = genNewColumnsFromPackageByTableName(catalog, columnSetName);
                    if (columnList.size() > 0) {
                        return columnList;
                    }
                }
            }
        } else if (schemaList != null && schemaList.size() != 0) {
            // Connection has schema only
            for (Schema schema : schemaList) {
                columnList = genNewColumnsFromPackageByTableName(schema, columnSetName);
                if (columnList.size() > 0) {
                    return columnList;
                }
            }
        }
        return columnList;
    }

    private static List<TdColumn> genNewColumnsFromPackageByTableName(orgomg.cwm.objectmodel.core.Package packageOrSchema,
            String columnSetName) {
        List<TdColumn> columnList = new ArrayList<TdColumn>();
        List<NamedColumnSet> columnSetList = PackageHelper.getNmaedColumnSets(packageOrSchema);
        for (NamedColumnSet colSet : columnSetList) {
            if (colSet == null || colSet.getName() == null) {
                continue;
            }
            if (colSet.getName().equals(columnSetName)) {
                List<TdColumn> oldColumns = ColumnSetHelper.getColumns(colSet);
                // Get a new instance of TdColumn.
                for (TdColumn oldCol : oldColumns) {
                    TdColumn newCol = RelationalPackage.eINSTANCE.getRelationalFactory().createTdColumn();
                    newCol = EcoreUtil.copy(oldCol);
                    columnList.add(newCol);
                }
                break;
            }
        }
        return columnList;
    }

    /**
     * Check whether all of columns is belong to same table
     * 
     * @param columns
     */
    public static boolean checkSameTable(ModelElement[] columns) {
        if (columns != null) {
            Set<MetadataTable> tableSet = new HashSet<MetadataTable>();
            for (ModelElement column : columns) {
                if (column == null) {
                    continue;
                }
                MetadataTable metadataTable = getColumnOwnerAsMetadataTable(column);
                if (metadataTable == null) {
                    continue;
                }
                tableSet.add(metadataTable);
                if (tableSet.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

}
