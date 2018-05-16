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
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.cwm.relational.util.RelationalSwitch;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.QueryColumnSet;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwm.resource.relational.Table;
import orgomg.cwm.resource.relational.View;

/**
 * @author scorreia
 * 
 * Utility class for handling ColumnSets.
 */
public final class ColumnSetHelper {

    private ColumnSetHelper() {
    }

    /**
     * Method "addColumn" adds the given column to the given column set.
     * 
     * @param column the column to add
     * @param columnSet the column set
     * @return true if the column set has changed.
     */
    public static boolean addColumn(TdColumn column, ColumnSet columnSet) {
        return columnSet.getFeature().add(column);
    }

    /**
     * removes the column from the columnSet and remove the column from the primary key and the foreign key that
     * contains it. this eventually removes the private key and foreign key from the table if the are no more refering
     * to any column
     * 
     * @param column to be removed
     * @param columnSet the set to remove the column from
     */
    public static void removeColumn(TdColumn column, ColumnSet columnSet) {
        // first remove PK if it exists
        if (ColumnHelper.isPrimaryKey(column)) {
            ColumnHelper.removeColumnFromTablePrimaryKey(column);
        }

        // remove foreign key if it exists
        if (ColumnHelper.isForeignKey(column)) {
            Set<ForeignKey> foreignKeySet = ColumnHelper.removeForeignKey(column);
            if (foreignKeySet != null) {
                for (ForeignKey foreignKey : foreignKeySet) {
                    if (foreignKey != null && foreignKey.getFeature().size() <= 0) {
                        TableHelper.removeForeignKey((Table) columnSet, foreignKey);
                    }
                }
            }
        }
        columnSet.getFeature().remove(column);
    }

    /**
     * 
     * DOC mzhao 2009-03-12 Remove all columns from this column set.
     * 
     * @param column
     * @param columnSet
     * @return true if remove successfully, false or else.
     */
    public static void clearAllColumns(ColumnSet columnSet) {
        columnSet.getFeature().clear();
    }

    public static List<TdColumn> getColumns(ColumnSet columnSet) {
        return ColumnHelper.getColumns(columnSet.getFeature());
    }

    /**
     * Method "addColumns".
     * 
     * @param columnSet the column set in which to add the columns (must not be null)
     * @param columns the columns to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     */
    public static boolean addColumns(ColumnSet columnSet, Collection<? extends TdColumn> columns) {
        assert columnSet != null;
        assert columns != null;
        List<Feature> features = columnSet.getFeature();
        if (features != null && features.size() > 0) {
            features.clear();
        }
        Resource eResource = columnSet.eResource();
        if (eResource != null) {
            eResource.getContents().addAll(columns);
        }
        return features.addAll(columns);
    }

    /**
     * Method "setColumns" replaces the previous columns by the new ones.
     * 
     * @param columnSet the column set in which to add the columns (must not be null)
     * @param columns the columns to add (must not be null)
     * @return true if the content of the table changed as a result of the call.
     */
    public static boolean setColumns(ColumnSet columnSet, Collection<? extends TdColumn> columns) {
        assert columnSet != null;
        assert columns != null;
        columnSet.getFeature().clear();
        return columnSet.getFeature().addAll(columns);
    }

    /**
     * Method "createQueryColumnSet".
     * 
     * @return a Query column set
     */
    public static QueryColumnSet createQueryColumnSet() {
        return orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createQueryColumnSet();
    }

    /**
     * DMethod "createQueryColumnSet" creates a column set from given columns.
     * 
     * @param columns the columns
     * @return the column set
     */
    public static QueryColumnSet createQueryColumnSet(TdColumn... columns) {
        QueryColumnSet columnSet = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createQueryColumnSet();
        for (TdColumn column : columns) {
            addColumn(column, columnSet);
        }
        return columnSet;
    }

    /**
     * Method "getParentCatalogOrSchema" returns the owner of the element (Catalog or Schema).
     * 
     * @param element (can be null)
     * @return the Catalog or of Schema or null
     */
    public static Package getParentCatalogOrSchema(ModelElement element) {
        if (element == null) {
            return null;
        }
        Namespace namespace = element.getNamespace();
        return PackageHelper.getCatalogOrSchema(namespace);
    }

    /**
     * Method "fillColumnSets". TODO scorreia this method has not been tested yet!!
     * 
     * @param <T> the type of elements to find (either Table, View, ColumnSet)
     * @param catalog the catalog if the tables are stored directly in catalog (or null)
     * @param schema the schema if the tables are stored directly in schema (or null)
     * @param output the list of searched elements (Tables, TdTables....)
     * @param tClassifierId the the classifierId of the searched elements (e.g. {@link RelationalPackage#TD_VIEW})
     * @return true if elements have been found and potentially added to the output list.
     */
    public static <T extends ColumnSet> boolean fillColumnSets(Catalog catalog, Schema schema, Collection<T> output,
            final int tClassifierId) {
        // --- precondition
        if (catalog == null && schema == null) {
            return false;
        }

        RelationalSwitch<T> relationalSwitch = new RelationalSwitch<T>() {

            @Override
            protected T doSwitch(int classifierID, EObject theEObject) {
                if (theEObject.eClass().getClassifierID() != tClassifierId) {
                    return null;
                } else {
                    return super.doSwitch(classifierID, theEObject);
                }
            }

            @Override
            public T caseColumnSet(ColumnSet object) {
                return castObject(object);
            }

            @Override
            public T caseTable(Table object) {
                return castObject(object);
            }

            @Override
            public T caseTdTable(TdTable object) {
                return castObject(object);
            }

            @Override
            public T caseTdView(TdView object) {
                return castObject(object);
            }

            @Override
            public T caseView(View object) {
                return castObject(object);
            }

            @SuppressWarnings("unchecked")
            private T castObject(Object object) {
                return (T) object;
            }

        };

        EList<ModelElement> elements = (schema != null) ? schema.getOwnedElement() : catalog.getOwnedElement();
        if (elements.isEmpty()) {
            // no element found
            return false;
        }
        for (EObject elt : elements) {
            T columnSet = relationalSwitch.doSwitch(elt);
            if (columnSet != null) {
                output.add(columnSet);
            }
        }
        return true;

    }

    /**
     * Method "getTableNames".
     * 
     * @param columnSet a set of columns (that could come from several Tables or views)
     * @return the list of container names (tables, views) which the columns belong to (not null).
     */
    public static String[] getColumnSetNames(ColumnSet columnSet) {
        List<TdColumn> columns = ColumnSetHelper.getColumns(columnSet);
        return getColumnSetNames(columns);
    }

    /**
     * Method "getTableNames".
     * 
     * @param columnSet a set of columns (that could come from several Tables or views)
     * @return the list of container names (tables, views) which the columns belong to (not null).
     */
    public static String[] getColumnSetNames(Collection<? extends TdColumn> columns) {
        List<String> tableNames = new ArrayList<String>();
        for (TdColumn tdColumn : columns) {
            String tableName = ColumnHelper.getColumnSetFullName(tdColumn);
            if (tableName != null) {
                tableNames.add(tableName);
            }
        }
        return tableNames.toArray(new String[tableNames.size()]);
    }

    /**
     * DOC bZhou Comment method "isFromSamePackage".
     * 
     * this method is used to judge columnset whether from same package.
     * 
     * @param sets
     * @return
     */
    public static boolean isFromSamePackage(ColumnSet... sets) {
        Set<Package> container = new HashSet<Package>();

        for (ColumnSet set : sets) {
            container.add(getParentCatalogOrSchema(set));
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
}
