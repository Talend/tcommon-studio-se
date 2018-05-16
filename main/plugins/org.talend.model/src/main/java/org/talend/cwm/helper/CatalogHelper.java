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
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Utility class for handling Catalog and its children.
 */
public final class CatalogHelper {

    private CatalogHelper() {
    }

    /**
     * Method "createCatalog" creates a catalog with the given name.
     * 
     * @param name the name of the catalog
     * @return the new catalog
     */
    public static Catalog createCatalog(String name) {
        Catalog catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        catalog.setName(name);
        return catalog;
    }

    public static List<Catalog> getCatalogs(Collection<? extends EObject> elements) {
        List<Catalog> catalogs = new ArrayList<Catalog>();
        for (EObject pack : elements) {
            Catalog cat = SwitchHelpers.CATALOG_SWITCH.doSwitch(pack);
            if (cat != null) {
                catalogs.add(cat);
            }
        }
        return catalogs;
    }

    /**
     * 
     * Get a catalog by Connection and catalog name.
     * 
     * @param conn
     * @param catalogName
     * @return
     */
    public static Catalog getCatalog(Connection conn, String catalogName) {
        if (conn == null || catalogName == null) {
            return null;
        }
        Catalog catalog = null;
        EList<Package> elements = conn.getDataPackage();
        for (EObject obj : elements) {
            Catalog cat = SwitchHelpers.CATALOG_SWITCH.doSwitch(obj);
            if (cat != null && catalogName.equalsIgnoreCase(cat.getName())) {
                catalog = cat;
                break;
            }
        }
        return catalog;
    }

    /**
     * Method "getParentCatalog" returns a Catalog if the element is directly owned by a catalog.
     * 
     * @param element (can be null)
     * @return the Catalog or null
     */
    public static Catalog getParentCatalog(ModelElement element) {
        if (element == null) {
            return null;
        }
        Namespace namespace = element.getNamespace();
        if (namespace == null) {
            return null;
        }
        return SwitchHelpers.CATALOG_SWITCH.doSwitch(namespace);
    }

    public static boolean addSchemas(Collection<Schema> schemas, Catalog catalog) {
        return addPackages(schemas, catalog);
    }

    public static boolean addSchemas(Schema schema, Catalog catalog) {
        return addPackage(schema, catalog);
    }

    public static boolean addTables(Collection<TdTable> tables, Catalog catalog) {
        return addPackages(tables, catalog);
    }

    public static List<TdTable> getTables(Catalog catalog) {
        return TableHelper.getTables(catalog.getOwnedElement());
    }

    public static List<Schema> getSchemas(Catalog catalog) {
        return SchemaHelper.getSchemas(catalog.getOwnedElement());
    }

    // ADDED add a method addViews to add all the views to special catalog
    public static boolean addViews(Collection<TdView> views, Catalog catalog) {
        return addPackages(views, catalog);
    }

    public static List<TdView> getViews(Catalog catalog) {
        return ViewHelper.getViews(catalog.getOwnedElement());
    }

    private static boolean addPackages(Collection<? extends ModelElement> elements, Catalog catalog) {
        boolean added = false;
        if ((catalog != null) && (elements != null)) {
            List<ModelElement> elementList = catalog.getOwnedElement();
            // MOD xqliu 2010-10-22 bug 16499: reload table/view will remove table informations
            // if (elementList != null && elementList.size() > 0) {
            // elementList.clear();
            // }
            // ~ 16499
            Resource eResource = catalog.eResource();
            if (eResource != null) {
                eResource.getContents().addAll(elements);
            }
            added = elementList.addAll(elements);
        }
        return added;
    }

    private static boolean addPackage(ModelElement element, Catalog catalog) {
        boolean added = false;
        if ((catalog != null) && (element != null)) {
            List<ModelElement> elementList = catalog.getOwnedElement();
            // MOD xqliu 2010-10-22 bug 16499: reload table/view will remove table informations
            // if (elementList != null && elementList.size() > 0) {
            // elementList.clear();
            // }
            // ~ 16499
            Resource eResource = catalog.eResource();
            if (eResource != null) {
                eResource.getContents().add(element);
            }
            added = elementList.add(element);
        }
        return added;
    }
}
