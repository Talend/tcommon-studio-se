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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Utility for handling Schema.
 */
public final class SchemaHelper {

    private SchemaHelper() {
    }

    public static Schema createSchema(String name) {
        Schema schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        schema.setName(name);
        return schema;
    }

    public static List<Schema> getSchemas(Collection<? extends EObject> elements) {
        List<Schema> schemas = new ArrayList<Schema>();
        for (EObject modelElement : new ArrayList<EObject>(elements)) {
            Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(modelElement);
            if (schema != null) {
                schemas.add(schema);
            }
        }
        return schemas;
    }

    /**
     * get schema by the name from collection which you pass.
     * 
     * @param elements packages of connection
     * @param schemaName the name of schema
     * @return
     */
    public static Schema getSchemaByName(Collection<? extends EObject> elements, String schemaName) {
        if (schemaName == null) {
            return null;
        }
        for (EObject modelElement : new ArrayList<EObject>(elements)) {
            Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(modelElement);
            if (schema != null && schemaName.equals(schema.getName())) {
                return schema;
            }
        }
        return null;
    }

    /**
     * 
     * Get a Schema by Connection and Schema name.
     * 
     * @param conn
     * @param schemaName
     * @return
     */
    public static Schema getSchema(Connection conn, String schemaName) {
        if (conn == null) {
            return null;
        }
        EList<Package> elements = conn.getDataPackage();
        return getSchemaByName(elements, schemaName);
    }

    public static List<TdTable> getTables(Schema schema) {
        // MOD xqliu 2009-04-27 bug 6507
        // TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.TABLE_FILTER, schema.getTaggedValue());
        // String tableFilter = tv == null ? null : tv.getValue();
        // return TableHelper.getTables(schema.getOwnedElement(), tableFilter);
        // ~
        return TableHelper.getTables(schema.getOwnedElement());
    }

    public static List<TdView> getViews(Schema schema) {
        // MOD xqliu 2009-04-27 bug 6507
        // TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.VIEW_FILTER, schema.getTaggedValue());
        // String viewFilter = tv == null ? null : tv.getValue();
        // return ViewHelper.getViews(schema.getOwnedElement(), viewFilter);
        // ~
        return ViewHelper.getViews(schema.getOwnedElement());
    }

    public static boolean addTables(Collection<TdTable> tables, Schema schema) {
        return addPackages(tables, schema);
    }

    public static boolean addViews(Collection<TdView> views, Schema schema) {
        return addPackages(views, schema);
    }

    /**
     * Method "getParentSchema" returns a schema if the element is directly owned by a schema.
     * 
     * @param element (can be null)
     * @return the Catalog or null
     */
    public static Schema getParentSchema(ModelElement element) {
        if (element == null) {
            return null;
        }
        final Namespace namespace = element.getNamespace();
        return (namespace != null) ? SwitchHelpers.SCHEMA_SWITCH.doSwitch(namespace) : null;
    }

    private static boolean addPackages(Collection<? extends ModelElement> elements, Schema schema) {
        boolean added = false;
        if ((schema != null) && (elements != null)) {
            List<ModelElement> elementList = schema.getOwnedElement();
            // DEL by msjian 2011-5-16 20875: "reload table list" for oracle have some issue
            // if (elementList != null && elementList.size() > 0) {
            // elementList.clear();
            // }
            Resource eResource = schema.eResource();
            if (eResource != null) {
                eResource.getContents().addAll(elements);
            }
            added = elementList.addAll(elements);
        }
        return added;
    }

}
