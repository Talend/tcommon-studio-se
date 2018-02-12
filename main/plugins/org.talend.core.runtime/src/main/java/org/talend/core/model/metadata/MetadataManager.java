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

package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.EPackageType;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.xml.TdXmlSchema;

import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.record.RecordFile;

/**
 * hywang class global comment. Detailled comment
 */
public class MetadataManager {

    public static final String TYPE_TABLE = "TABLE"; //$NON-NLS-1$

    public static final String TYPE_VIEW = "VIEW"; //$NON-NLS-1$

    public static final String TYPE_SYNONYM = "SYNONYM"; //$NON-NLS-1$

    public static final String TYPE_CALCULATION_VIEW = "CALCULATION VIEW"; //$NON-NLS-1$

    public static final String TYPE_ALIAS = "ALIAS"; //$NON-NLS-1$

    public static void addContents(ConnectionItem item, Resource itemResource) {
        List recordfiles = new ArrayList();
        List generics = new ArrayList();
        List xmlSchemas = new ArrayList();
        List catalogsorSchemas = new ArrayList();
        // MOD zshen for feature 14891 use same repository API with TOS to persistent metadata.
        List components = new ArrayList();
        // MOD mzhao handle dependencies.
        List<Dependency> dependencies = new ArrayList<Dependency>();

        getTypedPackges(item, recordfiles, EPackageType.RecordFile);
        getTypedPackges(item, generics, EPackageType.Generic);
        getTypedPackges(item, xmlSchemas, EPackageType.XML_Schema);
        getTypedPackges(item, catalogsorSchemas, EPackageType.DB_Schema);
        getTypedPackges(item, components, EPackageType.TDQ_compont);
        getTypedPackges(item, dependencies, EPackageType.Dependency);
        itemResource.getContents().add(item.getConnection());
        if (!recordfiles.isEmpty()) {
            itemResource.getContents().addAll(recordfiles); // 13221
        }
        if (!generics.isEmpty()) {
            itemResource.getContents().addAll(generics);
        }
        if (!xmlSchemas.isEmpty()) {
            itemResource.getContents().addAll(xmlSchemas);
        }
        if (!catalogsorSchemas.isEmpty()) {
            itemResource.getContents().addAll(catalogsorSchemas);
        }
        if (!components.isEmpty()) {
            itemResource.getContents().addAll(components);
        }
        if (!dependencies.isEmpty()) {
            itemResource.getContents().addAll(dependencies);
        }
    }

    public static void addPackges(Item item, List<EObject> objects) {
        if (item instanceof ConnectionItem) {
            ConnectionItem connItem = (ConnectionItem) item;
            List recordfiles = new ArrayList();
            List generics = new ArrayList();
            List xmlSchemas = new ArrayList();
            List catalogsorSchemas = new ArrayList();

            getTypedPackges(connItem, recordfiles, EPackageType.RecordFile);
            getTypedPackges(connItem, generics, EPackageType.Generic);
            getTypedPackges(connItem, xmlSchemas, EPackageType.XML_Schema);
            getTypedPackges(connItem, catalogsorSchemas, EPackageType.DB_Schema);
            if (!recordfiles.isEmpty()) {
                objects.addAll(recordfiles); // 13221
            }
            if (!generics.isEmpty()) {
                objects.addAll(generics);
            }
            if (!xmlSchemas.isEmpty()) {
                objects.addAll(xmlSchemas);
            }
            if (!catalogsorSchemas.isEmpty()) {
                objects.addAll(catalogsorSchemas);
            }
        }
    }

    public static void getTypedPackges(ConnectionItem item, List returnlist, EPackageType pkgType) {

        switch (pkgType) {
        case Generic:
            for (int i = 0; i < item.getConnection().getDataPackage().size(); i++) {
                if (item.getConnection().getDataPackage().get(i) instanceof GenericPackage) {
                    GenericPackage gpkg = (GenericPackage) item.getConnection().getDataPackage().get(i);
                    returnlist.add(gpkg);
                }
            }
            break;
        case DB_Catalog:
        case DB_Schema:
            Collection<? extends ModelElement> catalogsorSchemas = ConnectionHelper.getCatalogs(item.getConnection());
            if (catalogsorSchemas.size() == 0) {
                catalogsorSchemas = ConnectionHelper.getSchema(item.getConnection());
            }
            returnlist.addAll(catalogsorSchemas);
            break;

        case RecordFile:
            for (int i = 0; i < item.getConnection().getDataPackage().size(); i++) {
                if (item.getConnection().getDataPackage().get(i) instanceof RecordFile) {
                    RecordFile rf = (RecordFile) item.getConnection().getDataPackage().get(i);
                    // add for TDI-22857
                    item.getConnection().setName(item.getProperty().getDisplayName());
                    if (rf != null) {
                        rf.setName("default"); //$NON-NLS-1$
                    }
                    returnlist.add(rf);
                }
            }
            break;
        case XML_Schema: // for mdm
            for (int i = 0; i < item.getConnection().getDataPackage().size(); i++) {
                if (item.getConnection().getDataPackage().get(i) instanceof TdXmlSchema) {
                    TdXmlSchema xml = (TdXmlSchema) item.getConnection().getDataPackage().get(i);
                    returnlist.add(xml);
                }
            }
            break;
        case TDQ_compont:
            if (item.getConnection().getComponent() instanceof Component) {
                Component component = item.getConnection().getComponent();
                returnlist.add(component);
            }
            break;
        case Dependency:
            List<Dependency> dependencies = item.getConnection().getSupplierDependency();
            returnlist.addAll(dependencies);
            break;
        default:
        }
    }

}
