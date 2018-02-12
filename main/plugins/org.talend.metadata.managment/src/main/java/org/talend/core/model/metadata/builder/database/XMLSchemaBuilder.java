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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.cwm.xml.TdXmlContent;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.XmlFactory;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public final class XMLSchemaBuilder {

    private EList<XSDElementDeclaration> globalXSDElemDeclarations = null;

    private static Map<TdXmlSchema, XMLSchemaBuilder> schemaBuilderMap = new HashMap<TdXmlSchema, XMLSchemaBuilder>();

    private XMLSchemaBuilder() {

    }

    public static XMLSchemaBuilder getSchemaBuilder(TdXmlSchema document) {
        XMLSchemaBuilder builder = schemaBuilderMap.get(document);
        if (builder == null) {
            builder = new XMLSchemaBuilder();
            schemaBuilderMap.put(document, builder);
        }
        return builder;
    }

    /**
     * 
     * DOC mzhao Comment method "getRootElements".
     * 
     * @param xsdSchema
     * @return
     */
    public List<ModelElement> getRootElements(TdXmlSchema document) {
        IFolder mdmFolder = ReponsitoryContextBridge.getRootProject().getFolder(new Path(PluginConstant.MDM_PATH));
        IFolder connFolder = ReponsitoryContextBridge.getRootProject().getFolder(new Path(PluginConstant.CONN_PATH));
        String uri = isMdm(document) ? mdmFolder.getFile(document.getXsdFilePath()).getFullPath().toString() : connFolder
                .getFile(document.getXsdFilePath()).getFullPath().toString();
        Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("*", new XSDResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XSDResourceFactoryImpl());
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResource(URI.createPlatformResourceURI(uri, false), true);
        XSDSchema xsdSchema = null;
        for (Iterator<Resource> resources = resourceSet.getResources().iterator(); resources.hasNext();/* no-op */) {
            // Return the first schema object found, which is the main schema
            // loaded from the provided schemaURL
            Resource resource = (Resource) resources.next();
            if (resource instanceof XSDResourceImpl) {
                XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
                // This returns a org.eclipse.xsd.XSDSchema object
                xsdSchema = xsdResource.getSchema();
                break;
            }
        }
        if (xsdSchema == null) {
            return Collections.emptyList();
        }
        globalXSDElemDeclarations = xsdSchema.getElementDeclarations();
        EList<XSDElementDeclaration> copiedXsdElemDecl = new BasicEList<XSDElementDeclaration>(globalXSDElemDeclarations);
        Collections.copy(copiedXsdElemDecl, globalXSDElemDeclarations);
        Iterator<XSDElementDeclaration> rootItEleDecl = copiedXsdElemDecl.iterator();

        List<String> referenceNames = new ArrayList<String>();
        XSDElementDeclaration xsdEleDecl = null;
        while (rootItEleDecl.hasNext()) {
            xsdEleDecl = rootItEleDecl.next();
            recursiveFilterRefElements(referenceNames, xsdEleDecl);
        }
        rootItEleDecl = copiedXsdElemDecl.iterator();
        while (rootItEleDecl.hasNext()) {
            xsdEleDecl = rootItEleDecl.next();
            if (referenceNames.contains(xsdEleDecl.getName())) {
                rootItEleDecl.remove();
            }
        }
        // Adapt to CWM model.
        EList<ModelElement> tdXmlEleTypeList = adaptToCWMModel(copiedXsdElemDecl.iterator(), document);
        document.getOwnedElement().addAll(tdXmlEleTypeList);
        return tdXmlEleTypeList;
    }

    private EList<ModelElement> adaptToCWMModel(Iterator<XSDElementDeclaration> rootItEleDecl, TdXmlSchema document) {
        EList<ModelElement> tdXmlEleTypeList = new BasicEList<ModelElement>();
        TdXmlElementType xmlElement = null;
        XSDElementDeclaration xsdElementDecleration = null;
        while (rootItEleDecl.hasNext()) {
            xsdElementDecleration = rootItEleDecl.next();
            xmlElement = getElementFromXsdDeclaration(xsdElementDecleration);
            // xmlElement.setContent(value)
            xmlElement.setOwnedDocument(document);
            tdXmlEleTypeList.add(xmlElement);
        }
        return tdXmlEleTypeList;

    }

    private TdXmlElementType getElementFromXsdDeclaration(XSDElementDeclaration xsdElementDecleration) {
        TdXmlElementType xmlElement = XmlFactory.eINSTANCE.createTdXmlElementType();
        xmlElement.setName(xsdElementDecleration.getName());

        if (xsdElementDecleration.getTypeDefinition() instanceof XSDSimpleTypeDefinition) {
            XSDSimpleTypeDefinition xsdSimpleTypeDefinition = (XSDSimpleTypeDefinition) xsdElementDecleration.getTypeDefinition();
            String primitype = xsdSimpleTypeDefinition.getName();
            xmlElement.setJavaType(primitype);
        }
        if (xsdElementDecleration.getElement().getAttributeNode("ref") != null) {
            xmlElement.setName(xsdElementDecleration.getElement().getAttribute("ref"));
            // Get reference xsd element declaration
            xsdElementDecleration = findFromGlobalXSDEleDeclarations(xmlElement.getName());
        }
        xmlElement.setXsdElementDeclaration(xsdElementDecleration);
        // xmlElement.setXmlContent(XmlFactory.eINSTANCE.createTdXMLContent());
        return xmlElement;
    }

    private XSDElementDeclaration findFromGlobalXSDEleDeclarations(String name) {
        if (globalXSDElemDeclarations != null) {
            for (XSDElementDeclaration xsdEleDecl : globalXSDElemDeclarations) {
                if (xsdEleDecl.getName().equals(name)) {
                    return xsdEleDecl;
                }
            }
        }
        return null;
    }

    private void recursiveFilterRefElements(List<String> referenceNames, XSDElementDeclaration xsdElementDeclearation) {
        XSDTypeDefinition xsdTypeDef = xsdElementDeclearation.getTypeDefinition();
        if (xsdTypeDef instanceof XSDComplexTypeDefinition) {
            XSDComplexTypeDefinition xsdComplexTypeDef = (XSDComplexTypeDefinition) xsdTypeDef;
            XSDTerm term = xsdComplexTypeDef.getComplexType().getTerm();
            if (term instanceof XSDModelGroup) {
                XSDModelGroup modGroup = (XSDModelGroup) term;
                EList<XSDParticle> paticleList = modGroup.getContents();
                for (XSDParticle paticle : paticleList) {
                    if (paticle.getContent() instanceof XSDElementDeclaration) {
                        XSDElementDeclaration xed = (XSDElementDeclaration) paticle.getContent();
                        if (xed.getElement().getAttributeNode("ref") != null) {
                            referenceNames.add(xed.getElement().getAttribute("ref"));
                        } else {
                            // Recursive to find reference elements.
                            recursiveFilterRefElements(referenceNames, xed);
                        }

                    }
                }
            }
        }
    }

    public List<TdXmlElementType> getChildren(TdXmlElementType parent) {
        // XSDElementDeclaration xsdElementDeclearation = (XSDElementDeclaration) parent.getXsdElementDeclaration();
        EObject eObject = parent.getXsdElementDeclaration();
        XSDElementDeclaration xsdElementDeclearation = null;
        if (eObject instanceof XSDElementDeclaration) {
            xsdElementDeclearation = (XSDElementDeclaration) eObject;
        }
        TdXmlContent createTdXMLContent = XmlFactory.eINSTANCE.createTdXmlContent();
        if (xsdElementDeclearation != null) {
            XSDTypeDefinition xsdTypeDef = xsdElementDeclearation.getTypeDefinition();
            if (xsdTypeDef instanceof XSDComplexTypeDefinition) {
                XSDComplexTypeDefinition xsdComplexTypeDef = (XSDComplexTypeDefinition) xsdTypeDef;
                XSDTerm term = xsdComplexTypeDef.getComplexType().getTerm();
                if (term instanceof XSDModelGroup) {
                    XSDModelGroup modGroup = (XSDModelGroup) term;
                    EList<XSDParticle> paticleList = modGroup.getContents();
                    for (XSDParticle paticle : paticleList) {
                        if (paticle.getContent() instanceof XSDElementDeclaration) {
                            XSDElementDeclaration xed = (XSDElementDeclaration) paticle.getContent();
                            TdXmlElementType xmlElem = getElementFromXsdDeclaration(xed);
                            xmlElem.setOwnedDocument(parent.getOwnedDocument());
                            createTdXMLContent.getXmlElements().add(xmlElem);

                        }
                    }
                }
            }
        }
        parent.setXmlContent(createTdXMLContent);
        return parent.getXmlContent().getXmlElements();
    }

    public ReturnCode isLeafNode(TdXmlElementType element) {
        ReturnCode code = new ReturnCode();
        // XSDElementDeclaration xsdElementDeclearation = (XSDElementDeclaration) element.getXsdElementDeclaration();
        EObject eObject = element.getXsdElementDeclaration();
        XSDElementDeclaration xsdElementDeclearation = null;
        if (eObject instanceof XSDElementDeclaration) {
            xsdElementDeclearation = (XSDElementDeclaration) eObject;
        }
        if (xsdElementDeclearation != null) {
            XSDTypeDefinition xsdTypeDef = xsdElementDeclearation.getTypeDefinition();
            if (xsdTypeDef instanceof XSDSimpleTypeDefinition) {
                code.setOk(Boolean.FALSE);
            }
        }
        return code;
    }

    private boolean isMdm(TdXmlSchema document) {
        EList<DataManager> dataManagers = document.getDataManager();
        if (dataManagers != null && dataManagers.size() > 0) {
            DataManager dataManager = dataManagers.get(0);
            if (dataManager != null) {
                return dataManager instanceof MDMConnection;
            }
        }
        return false;
    }
}
