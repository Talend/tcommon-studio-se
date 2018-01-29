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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import orgomg.cwm.foundation.businessinformation.BusinessinformationFactory;
import orgomg.cwm.foundation.businessinformation.Description;
import orgomg.cwm.foundation.businessinformation.Document;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class ModelElementHelper {

    private ModelElementHelper() {
    }

    /**
     * As specified in CWM document at p. 67, the dependency kind can be of two types "Usage" or "Abstraction", but can
     * also be of other types.
     */
    public static final String USAGE = "Usage"; //$NON-NLS-1$

    /**
     * Method "createUsageDependencyOn".
     * 
     * Example Analysis depends on the exitence of a DataProvider. This method must be called
     * createUsageDependencyOn(Analysis, DataProvider). The created dependency is added to the DataProvider in its
     * "client dependencies" and to the Analysis in its "supplier dependencies". See OMG CWM spec paragraph 4.3.2.7.
     * 
     * @param kind the kind of dependency
     * @param clientElement the element that requires the requiredElement
     * @param supplierElement the required element
     * @return the Dependency of clientElement on requiredElement
     */
    public static Dependency createDependencyOn(String kind, ModelElement clientElement, ModelElement supplierElement) {
        Dependency dependency = CoreFactory.eINSTANCE.createDependency();
        dependency.setKind(kind);
        dependency.getClient().add(clientElement);
        dependency.getSupplier().add(supplierElement);
        return dependency;
    }

    /**
     * Method "removeSupplierDependencies". The given element (supplier) is an element required by other elements (the
     * clients). This method gets all the dependencies that link the supplier to the clients. Then for each client, the
     * dependency toward the supplier is removed. And finally the list of dependencies that link the supplier to its
     * clients is suppressed.
     * 
     * @param supplierElement an element that is required by other elements
     * @return
     */
    public static boolean removeSupplierDependencies(ModelElement supplierElement) {
        boolean ok = true;
        EList<Dependency> supplierDependencies = supplierElement.getSupplierDependency();
        for (Dependency dependency : supplierDependencies) {
            // first remove each dependency object from the clients elements that depend on the supplier
            dependency.getClient().clear();
        }
        supplierDependencies.clear();
        return ok;
    }

    /**
     * Method "getDependencyBetween" the dependency that relates the supplier to the client. This method looks into the
     * list of dependencies of both the supplier and the client.
     * 
     * @param kind the kind of dependency looked for (could be null)
     * @param clientElement
     * @param supplierElement
     * @return the dependency that relates the supplier to the client or null if none is found.
     */
    public static Dependency getDependencyBetween(String kind, ModelElement clientElement, ModelElement supplierElement) {
        EList<Dependency> supplierDependencies = supplierElement.getSupplierDependency();
        for (Dependency dependency : supplierDependencies) {
            String depKind = dependency.getKind();
            // 2008-04-28 scorreia instance of clientElement can be different from the client element inside the
            // list of clients of the dependency => we should not use "contains" here, but rather the object id
            if (ResourceHelper.resolveObject(dependency.getClient(), clientElement) != null) {
                if (kind == null) {
                    if (depKind == null) { // both null
                        return dependency;
                    } else {
                        continue; // not both are null
                    }
                }
                // else kind is not null and can be compared
                if (kind.compareTo(dependency.getKind()) == 0) {
                    return dependency;
                }
            }
        }
        // not found in the supplier, look into the client
        EList<Dependency> clientDependencies = clientElement.getClientDependency();
        for (Dependency dependency : clientDependencies) {
            String depKind = dependency.getKind();
            // 2008-04-28 scorreia instance of clientElement can be different from the client element inside the
            // list of clients of the dependency => we should not use "contains" here, but rather the object id
            if (ResourceHelper.resolveObject(dependency.getSupplier(), supplierElement) != null) {
                if (kind == null) {
                    if (depKind == null) { // both null
                        return dependency;
                    } else {
                        continue; // not both are null
                    }
                }
                // else kind is not null and can be compared
                if (kind.compareTo(dependency.getKind()) == 0) {
                    return dependency;
                }
            }
        }
        return null;
    }

    /**
     * DOC bZhou Comment method "iterateClientDependencies".
     * 
     * @param element
     * @param returnElements
     */
    public static void iterateClientDependencies(ModelElement element, List<ModelElement> returnElements) {
        if (returnElements != null) {
            for (Dependency dependency : element.getClientDependency()) {

                if (dependency.eIsProxy()) {
                    returnElements.add(dependency);
                    continue;
                }

                EList<ModelElement> supplier = dependency.getSupplier();
                if (supplier != null) {
                    for (ModelElement subElement : supplier) {
                        iterateClientDependencies(subElement, returnElements);
                    }

                    returnElements.addAll(supplier);
                }
            }
        }
    }

    /**
     * DOC bZhou Comment method "iterateSupplyDependencies".
     * 
     * @param element
     * @param returnElements
     */
    public static void iterateSupplyDependencies(ModelElement element, List<ModelElement> returnElements) {
        if (returnElements != null) {
            for (Dependency dependency : element.getSupplierDependency()) {

                if (dependency.eIsProxy()) {
                    returnElements.add(dependency);
                    continue;
                }

                EList<ModelElement> clientList = dependency.getClient();
                if (clientList != null) {
                    for (ModelElement subElement : clientList) {
                        iterateSupplyDependencies(subElement, returnElements);
                    }

                    returnElements.addAll(clientList);
                }
            }
        }
    }

    public static boolean isFromSameConnection(List<ModelElement> elements) {
        assert elements != null;

        Set<DataProvider> dataProviderSets = new HashSet<DataProvider>();
        for (ModelElement element : elements) {
            dataProviderSets.add(getConnection(element));
        }
        return dataProviderSets.size() == 1;
    }

    public static boolean isFromSameTable(List<ModelElement> elements) {
        assert elements != null;
        ModelElement modelElement = elements.get(0);
        if (modelElement instanceof TdColumn) {
            List<TdColumn> columns = new ArrayList<TdColumn>();
            for (ModelElement element : elements) {
                columns.add((TdColumn) element);
            }
            return ColumnHelper.isFromSameTable(columns);
        } else if (modelElement instanceof TdXmlElementType) {
            List<TdXmlElementType> xmlElements = new ArrayList<TdXmlElementType>();
            for (ModelElement element : elements) {
                xmlElements.add((TdXmlElementType) element);
            }
            return XmlElementHelper.isFromSameTable(xmlElements);
        }
        return false;
    }

    public static final Connection getConnection(ModelElement element) {
        if (element instanceof TdColumn) {
            return ConnectionHelper.getConnection((TdColumn) element);
        }
        if (element instanceof TdXmlElementType) {
            return ConnectionHelper.getConnection((TdXmlElementType) element);
        }
        if (element instanceof MetadataTable) {
            return TableHelper.getFirstConnection((MetadataTable) element);
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "getContainer".
     * 
     * @param modelElement
     * @return
     */
    @SuppressWarnings("deprecation")
    public static ModelElement getContainer(ModelElement modelElement) {
        if (modelElement instanceof TdColumn) {
            return ColumnHelper.getColumnSetOwner(modelElement);
        } else if (modelElement instanceof TdXmlElementType) {
            return XmlElementHelper.getParentElement((TdXmlElementType) modelElement);
        } else if (modelElement instanceof MetadataColumn) {
            return ColumnHelper.getColumnOwnerAsMetadataTable((MetadataColumn) modelElement);
        }
        return null;
    }

    /**
     * Return the first description for the modelelement, creates one if none is found
     * 
     * @return the first description of the ModelElement, never null
     */
    public static Description getFirstDescription(ModelElement modelElement) {
        Description description = null;
        if (modelElement.getDescription().isEmpty()) {
            description = BusinessinformationFactory.eINSTANCE.createDescription();
            modelElement.getDescription().add(description);
        } else {
            description = modelElement.getDescription().get(0);
        }
        return description;
    }

    /**
     * Return the first Document for the modelelement, creates one if none is found
     * 
     * @return the first Document of the ModelElement, never null
     */
    public static Document getFirstDocument(ModelElement modelElement) {
        Document document = null;
        if (modelElement.getDescription().isEmpty()) {
            document = BusinessinformationFactory.eINSTANCE.createDocument();
            modelElement.getDocument().add(document);
        } else {
            document = modelElement.getDocument().get(0);
        }
        return document;
    }

    public static final Connection getTdDataProvider(ModelElement element) {
        if (element instanceof TdColumn) {
            return ConnectionHelper.getTdDataProvider((TdColumn) element);
        }
        if (element instanceof TdXmlElementType) {
            TdXmlSchema ownedDocument = ((TdXmlElementType) element).getOwnedDocument();
            return ownedDocument != null ? ConnectionHelper.getTdDataProvider(ownedDocument) : null;
        }
        return null;
    }

    /**
     * 
     * DOC qiongli Comment method "getModleElementName".
     * 
     * @param modelElement
     * @return
     */
    public static String getName(ModelElement mElement) {
        String name = mElement.getName();
        if (name == null) {
            if (mElement instanceof MetadataTable) {
                name = ((MetadataTable) mElement).getLabel();
            } else if (mElement instanceof MetadataColumn) {
                name = ((MetadataColumn) mElement).getLabel();
            }
        }
        return name;
    }

    /**
     * 
     * @param mElement1
     * @param mElement2
     * @return compare the uuid between both ModelElement
     */
    public static boolean compareUUID(ModelElement mElement1, ModelElement mElement2) {
        if (mElement1 != null && mElement2 != null && !mElement1.eIsProxy() && !mElement2.eIsProxy()) {
            return mElement1.eResource().getURIFragment(mElement1).equals(mElement2.eResource().getURIFragment(mElement2));
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "getIFile".
     * 
     * @param modelElement
     * @return
     */
    public static IFile getIFile(ModelElement modelElement) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String platformString = modelElement.eResource().getURI().toPlatformString(true);
        return root.getFile(new Path(platformString));
    }
}
