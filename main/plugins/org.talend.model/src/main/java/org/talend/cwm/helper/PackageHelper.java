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
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * Helper class for getting elements of Schema or Catalog indifferently.
 */
public final class PackageHelper {

    private PackageHelper() {
    }

    /**
     * returns the table owned by this package and only this one
     * 
     * @param thePackage a Catalog or Schema (must not be null)
     * @return the tables contained in the given element.
     */
    public static List<TdTable> getTables(Package thePackage) {
        assert thePackage != null;
        return TableHelper.getTables(thePackage.getOwnedElement());
    }

    /**
     * returns the views owned by this package and only this one
     * 
     * @param thePackage a Catalog or Schema (must not be null)
     * @return the views contained in the given element.
     */
    public static List<TdView> getViews(Package thePackage) {
        assert thePackage != null;
        return ViewHelper.getViews(thePackage.getOwnedElement());
    }

    /**
     * Method "getTables" extracts the tables from the list.
     * 
     * @param elements any elements that could contain TdTables
     * @return the list of TdTables found in the given list (never null, but can be empty).
     */
    @SuppressWarnings("unchecked")
    public static <T extends ModelElement> List<T> getOwnedElements(Package pack, Class<T> theClass) {
        assert pack != null;
        assert theClass != null;
        List<T> tables = new ArrayList<T>(pack.getOwnedElement().size());
        for (ModelElement elt : pack.getOwnedElement()) {
            if (elt != null && theClass.isAssignableFrom(elt.getClass())) {
                tables.add((T) elt);
            }
        }
        return tables;
    }

    /**
     * DOC bZhou Comment method "getNmaedColumnSets".
     * 
     * @param catOrSchema
     * @return
     */
    public static List<NamedColumnSet> getNmaedColumnSets(Package catOrSchema) {
        List<NamedColumnSet> setList = new ArrayList<NamedColumnSet>();
        setList.addAll(getTables(catOrSchema));
        setList.addAll(getViews(catOrSchema));
        return setList;
    }

    public static Package getCatalogOrSchema(EObject element) {
        if (element == null) {
            return null;
        }

        Catalog res = SwitchHelpers.CATALOG_SWITCH.doSwitch(element);
        if (res != null) {
            return res;
        }
        return SwitchHelpers.SCHEMA_SWITCH.doSwitch(element);
    }

    /**
     * DOC sgandon Comment method "addColumnSets".
     * 
     * @param columnSets
     * @param packageElement
     * @return
     * @deprecated use addMatadataTable instead
     */
    public static boolean addColumnSets(Collection<ColumnSet> columnSets, Package packageElement) {
        return packageElement.getOwnedElement().addAll(columnSets);
    }

    /**
     * DOC sgandon Comment method "addColumnSet".
     * 
     * @param columnSet
     * @param packageElement
     * @return
     * @deprecated use addMatadataTable instead
     */
    public static boolean addColumnSet(ColumnSet columnSet, Package packageElement) {
        return packageElement.getOwnedElement().add(columnSet);
    }

    /**
     * adds a MetadataTable to the package
     * 
     * @param table table to be added
     * @param aPackage the package to add to
     * @return true if success
     */
    public static boolean addMetadataTable(MetadataTable table, Package aPackage) {
        return aPackage.getOwnedElement().add(table);
    }

    /**
     * adds a MetadataTable to the package
     * 
     * @param tables tables to add
     * @param aPackage the package to add to
     * @return true if success
     */
    public static boolean addMetadataTable(Collection<MetadataTable> tables, Package aPackage) {
        return aPackage.getOwnedElement().addAll(tables);
    }

    public static boolean removeColumnSet(ColumnSet columnSet, Package packageElement) {
        return packageElement.getOwnedElement().remove(columnSet);
    }

    public static boolean removeColumnSets(Collection<ColumnSet> columnSets, Package packageElement) {
        return packageElement.getOwnedElement().removeAll(columnSets);
    }

    /**
     * the the package that hold the table if any or null.
     * 
     * @param element never null
     * @return the Catalog or of Schema or null
     */
    public static Package getParentPackage(MetadataTable table) {
        assert table != null;
        Namespace namespace = table.getNamespace();
        return PackageHelper.getCatalogOrSchema(namespace);
    }

    /**
     * the the package that hold the table if any or null.
     * 
     * @param element never null
     * @return the Catalog or of Schema or null
     */
    public static Package getParentPackage(Package subpackge) {
        assert subpackge != null;
        Namespace namespace = subpackge.getNamespace();
        return PackageHelper.getCatalogOrSchema(namespace);
    }

    /**
     * This returs the table from this package and all the owned package of this package.
     * 
     * @param pack the package to look for table and sub packages (never null)
     * @param resultSet the set filled with the Table found (never null)
     */
    public static void getAllTables(Package pack, Set<MetadataTable> resultSet) {
        assert pack != null;
        assert resultSet != null;
        // add owned table
        resultSet.addAll(getOwnedElements(pack, MetadataTable.class));
        // add sub packge tables by recursing
        List<Package> ownedPackages = getOwnedElements(pack, Package.class);
        for (Package aPackage : ownedPackages) {
            getAllTables(aPackage, resultSet);
        }
    }

    /**
     * Change return type from "Set" to"List" to keep the order of tables. Should notice that we can only use this
     * method if there is only one datapackage in the connection.
     * 
     * @param pack the package to look for table and sub packages (never null)
     * @param resultSet the set filled with the Table found (never null)
     */
    public static void getAllTablesWithOrders(Package pack, List<MetadataTable> resultList) {
        assert pack != null;
        assert resultList != null;
        // add owned table
        resultList.addAll(getOwnedElements(pack, MetadataTable.class));
        // add sub packge tables by recursing
        List<Package> ownedPackages = getOwnedElements(pack, Package.class);
        for (Package aPackage : ownedPackages) {
            getAllTablesWithOrders(aPackage, resultList);
        }
    }

    /**
     * adds package to package
     * 
     * @param packAdded the added package (never null)
     * @param packOwner the packed that will own packAdded (never null)
     */
    public static void addPackageToPackage(Package packAdded, Package packOwner) {
        assert packAdded != null;
        assert packOwner != null;
        packOwner.getOwnedElement().add(packAdded);
    }

}
