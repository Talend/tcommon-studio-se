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
package org.talend.datatools.xml.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDNamedComponentImpl;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.talend.core.utils.IXSDPopulationUtil;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class XSDPopulationUtil2 implements IXSDPopulationUtil {

    public static final String CHOICE = "(choice)"; //$NON-NLS-1$

    public static final String SUBS = "(subs)"; //$NON-NLS-1$

    private Map<String, String> namespaceToPrefix = new HashMap<String, String>();

    private int prefixNumberGenerated;

    private boolean enableGeneratePrefix = false;

    private boolean includeAttribute = true;

    private boolean supportChoice = false;

    private boolean supportSubstitution = false;

    private boolean includeAbsSubs = false;

    private Map<XSDElementDeclaration, ATreeNode> particleToTreeNode = new HashMap<XSDElementDeclaration, ATreeNode>();

    protected ResourceSet resourceSet;

    public XSDPopulationUtil2() {
        resourceSet = new ResourceSetImpl();
    }

    public XSDSchema getXSDSchema(String fileName) throws URISyntaxException, MalformedURLException {
        return getXSDSchema(fileName, false);
    }

    public XSDSchema getXSDSchema(String fileName, boolean forceReload) throws URISyntaxException, MalformedURLException {
        // Create a resource set and load the main schema file into it.
        XSDResourceImpl mainXsdResource = (XSDResourceImpl) resourceSet.getResource(URI.createFileURI(fileName), true);

        boolean haveExternalDependenciesWithoutLocation = false;

        // check all resources if need to force the set location (in case of use of wsdl file without the location set)
        for (Resource resource : resourceSet.getResources()) {
            if (resource instanceof XSDResourceImpl) {
                XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
                for (EObject object : xsdResource.getSchema().getContents()) {
                    if (object instanceof XSDImport) {
                        XSDImport xsdImport = (XSDImport) object;
                        if (StringUtils.isEmpty(xsdImport.getSchemaLocation())) {
                            for (Resource resource2 : resourceSet.getResources()) {
                                if (resource2 instanceof XSDResourceImpl) {
                                    XSDResourceImpl xsdResource2 = (XSDResourceImpl) resource2;
                                    if (xsdResource2.getSchema().getTargetNamespace() != null
                                            && xsdResource2.getSchema().getTargetNamespace().equals(xsdImport.getNamespace())) {
                                        xsdImport.setSchemaLocation(xsdResource2.getSchema().getSchemaLocation());
                                        haveExternalDependenciesWithoutLocation = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        XSDSchema xsdSchema = mainXsdResource.getSchema();
        if (forceReload || haveExternalDependenciesWithoutLocation) {
            // force to set the element again, it will set automatically the dependencies
            xsdSchema.setElement(xsdSchema.getDocument().getDocumentElement());
        }
        return xsdSchema;
    }

    public void addSchema(String fileName) throws IOException {
        resourceSet.getResource(URI.createFileURI(fileName), true);
    }

    public List<ATreeNode> getAllRootNodes(XSDSchema xsdSchema) throws OdaException {
        List<ATreeNode> rootNodes = new ArrayList<ATreeNode>();
        List<String> rootNodesName = new ArrayList<String>();

        List all = new ArrayList(xsdSchema.getElementDeclarations());
        all.addAll(xsdSchema.getTypeDefinitions());
        all = XSDNamedComponentImpl.sortNamedComponents(all);

        for (Iterator i = all.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof XSDElementDeclaration) {
                ATreeNode node = new ATreeNode();
                XSDElementDeclaration xsdElementDeclaration = (XSDElementDeclaration) o;
                String elementName = xsdElementDeclaration.getQName();
                node.setType(ATreeNode.ELEMENT_TYPE);
                node.setDataType(xsdElementDeclaration.getName());
                XSDTypeDefinition xsdTypeDefinition = xsdElementDeclaration.getTypeDefinition();
                String prefix = null;
                String namespace = xsdElementDeclaration.getTargetNamespace();
                node.setCurrentNamespace(namespace);

                XSDTypeDefinition typeDef = xsdElementDeclaration.getTypeDefinition();

                if (namespace != null) {
                    prefix = xsdElementDeclaration.getQName().contains(":") ? xsdElementDeclaration.getQName().split(":")[0] : "";
                    if (prefix == null || prefix.isEmpty()) {
                        if (xsdSchema.getQNamePrefixToNamespaceMap().containsValue(typeDef.getTargetNamespace())) {
                            for (String key : xsdSchema.getQNamePrefixToNamespaceMap().keySet()) {
                                if (xsdSchema.getQNamePrefixToNamespaceMap().get(key).equals(typeDef.getTargetNamespace())) {
                                    prefix = key;
                                }
                            }
                        }
                        if (prefix != null && !prefix.isEmpty()) {
                            elementName = prefix + ":" + elementName;
                        }
                    }
                }
                node.setValue(elementName);
                if (xsdTypeDefinition == null) {
                    XSDComplexTypeDefinition generalType = xsdSchema.resolveComplexTypeDefinitionURI(xsdElementDeclaration
                            .getURI());
                    if (generalType.getContainer() != null) {
                        xsdTypeDefinition = generalType;
                    }
                }
                if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                    node.setDataType(xsdTypeDefinition.getName());
                }
                if (!rootNodes.contains(node) && !rootNodesName.contains(node.getValue())) {
                    rootNodesName.add((String) node.getValue());
                    rootNodes.add(node);
                }
            } else {
                XSDTypeDefinition xsdTypeDefinition = (XSDTypeDefinition) o;
                if (xsdTypeDefinition instanceof XSDComplexTypeDefinition) {
                    ATreeNode node = new ATreeNode();
                    String elementName = xsdTypeDefinition.getQName();
                    node.setValue(elementName);
                    node.setType(ATreeNode.ELEMENT_TYPE);
                    node.setDataType(xsdTypeDefinition.getName());
                    if (!rootNodes.contains(node) && !rootNodesName.contains(node.getValue())) {
                        rootNodesName.add((String) node.getValue());
                        rootNodes.add(node);
                    }
                }
            }
        }
        return rootNodes;
    }

    public XSDSchema getXSDSchemaFromNamespace(String namespace) {
        for (Resource resource : resourceSet.getResources()) {
            if (resource instanceof XSDResourceImpl) {
                XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
                XSDSchema schema = xsdResource.getSchema();
                if (schema.getTargetNamespace() != null && schema.getTargetNamespace().equals(namespace)) {
                    return schema;
                }
            }
        }
        return null;
    }

    private void addParticleDetail(XSDSchema xsdSchema, XSDParticle xsdParticle, ATreeNode parentNode, String currentPath)
            throws OdaException, IllegalAccessException, InvocationTargetException {
        XSDTerm xsdTerm = xsdParticle.getTerm();
        if (xsdTerm instanceof XSDElementDeclaration) {
            XSDElementDeclaration xsdElementDeclarationParticle = (XSDElementDeclaration) xsdTerm;
            if (particleToTreeNode.containsKey(xsdElementDeclarationParticle)) {
                ATreeNode originalTreeNode = particleToTreeNode.get(xsdElementDeclarationParticle);
                // create a duplicate from this one with the same childs and just return.
                // this will avoid to recheck the same thing again
                ATreeNode partNode = new ATreeNode();
                partNode.setCurrentNamespace(originalTreeNode.getCurrentNamespace());
                partNode.setValue(originalTreeNode.getValue());
                partNode.setType(ATreeNode.ELEMENT_TYPE);
                partNode.setDataType(originalTreeNode.getDataType());
                // for TUP-18382 should reuse children for different parent nodes
                // need every node of ATreeNode to be unique.
                List<ATreeNode> children = new ArrayList<>();
                for (Object child : originalTreeNode.getChildren()) {
                    if (child instanceof ATreeNode) {
                        children.add(((ATreeNode) child).copy());
                    }
                }
                partNode.addChild(children.toArray());
                parentNode.addChild(partNode);
                return;
            }
            ATreeNode partNode = new ATreeNode();
            particleToTreeNode.put(xsdElementDeclarationParticle, partNode);
            String elementName = xsdElementDeclarationParticle.getName();
            String prefix = null;
            String namespace = xsdElementDeclarationParticle.getTargetNamespace();
            XSDTypeDefinition typeDef = xsdElementDeclarationParticle.getTypeDefinition();
            if (typeDef == null) {
                XSDSchema schemaFromNamespace = getXSDSchemaFromNamespace(namespace);
                if (schemaFromNamespace == null) {
                    schemaFromNamespace = xsdSchema;
                }
                xsdElementDeclarationParticle = schemaFromNamespace.resolveElementDeclarationURI(xsdElementDeclarationParticle
                        .getURI());
                typeDef = xsdElementDeclarationParticle.getTypeDefinition();
            }
            String typeNamespace = typeDef.getTargetNamespace();
            if (typeNamespace != null && !typeNamespace.equals(namespace)) {
                XSDSchema schemaOfType = getXSDSchemaFromNamespace(typeNamespace);
                if (schemaOfType != null) {
                    XSDTypeDefinition typeDefinition = schemaOfType.resolveComplexTypeDefinitionURI(typeDef.getURI());
                    if (typeDefinition != null && typeDefinition.getContainer() != null) {
                        typeDef = typeDefinition;
                    }
                }
            }
            if (namespace != null) {
                prefix = namespaceToPrefix.get(namespace);
                if (prefix == null) {
                    prefix = ((XSDElementDeclaration) xsdTerm).getQName().contains(":") ? ((XSDElementDeclaration) xsdTerm)
                            .getQName().split(":")[0] : "";

                    if (isEnableGeneratePrefix() && (prefix == null || prefix.isEmpty())) {
                        // generate a new prefix
                        prefix = "p" + prefixNumberGenerated;
                        prefixNumberGenerated++;
                    }
                    // check if prefix already exists, if yes, generate a new one.
                    if (namespaceToPrefix.containsValue(prefix)) {
                        prefix = prefix + prefixNumberGenerated;
                        prefixNumberGenerated++;
                    }
                    if (prefix != null && !prefix.isEmpty()) {
                        namespaceToPrefix.put(namespace, prefix);
                    } else {
                        boolean namespaceFoundInParent = false;
                        ATreeNode node = parentNode;
                        Set<ATreeNode> checkedNodes = new HashSet<ATreeNode>();
                        do {
                            for (Object child : node.getChildren()) {
                                if (child instanceof ATreeNode) {
                                    ATreeNode childNode = (ATreeNode) child;
                                    if (childNode.getType() == ATreeNode.NAMESPACE_TYPE && namespace.equals(childNode.getValue())) {
                                        namespaceFoundInParent = true;
                                        break;
                                    }
                                }
                            }
                            checkedNodes.add(node);
                            node = node.getParent();
                        } while (node != null && !namespaceFoundInParent && !checkedNodes.contains(node));
                        if (!namespaceFoundInParent) {
                            ATreeNode namespaceNode = new ATreeNode();
                            namespaceNode.setDataType(""); //$NON-NLS-1$
                            namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                            namespaceNode.setValue(namespace);
                            partNode.addChild(namespaceNode);
                        }
                    }
                }
            }
            partNode.setCurrentNamespace(namespace);
            if (prefix != null && !prefix.isEmpty()) {
                elementName = prefix + ":" + elementName;
            }
            partNode.setValue(elementName);
            partNode.setType(ATreeNode.ELEMENT_TYPE);
            partNode.setDataType(xsdElementDeclarationParticle.getName());
            parentNode.addChild(partNode);
            boolean resolvedAsComplex = false;
            if (typeDef instanceof XSDComplexTypeDefinition) {
                XSDTypeDefinition xsdTypeDefinition = typeDef;
                String path = currentPath + elementName + "/";
                if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                    partNode.setDataType(xsdTypeDefinition.getQName());
                }
                addComplexTypeDetails(xsdSchema, partNode, xsdTypeDefinition, prefix, namespace, path);
                resolvedAsComplex = true;
            } else if (typeDef.getTargetNamespace() != null) {
                resolvedAsComplex = true;
                if (!currentPath.contains("/" + elementName + "/")) {
                    String path = currentPath + elementName + "/";
                    XSDComplexTypeDefinition generalType = xsdSchema.resolveComplexTypeDefinition(typeDef.getQName());
                    XSDTypeDefinition xsdTypeDefinition = xsdElementDeclarationParticle.getTypeDefinition();
                    if (generalType.getContainer() != null) {
                        xsdTypeDefinition = generalType;
                    }
                    if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                        partNode.setDataType(xsdTypeDefinition.getName());
                    }
                    if (xsdTypeDefinition instanceof XSDComplexTypeDefinition) {
                        addComplexTypeDetails(xsdSchema, partNode, xsdTypeDefinition, prefix, namespace, path);
                    } else {
                        resolvedAsComplex = false;
                    }
                }
            }
            if (!resolvedAsComplex) {
                String dataType = xsdElementDeclarationParticle.getTypeDefinition().getQName();
                if (!XSDConstants.isSchemaForSchemaNamespace(xsdElementDeclarationParticle.getTypeDefinition()
                        .getTargetNamespace()) && xsdElementDeclarationParticle.getTypeDefinition().getBaseType() != null) {
                    if (!"xs:anySimpleType".equals(xsdElementDeclarationParticle.getTypeDefinition().getBaseType().getQName())) {
                        dataType = xsdElementDeclarationParticle.getTypeDefinition().getBaseType().getQName();
                    }
                }
                partNode.setDataType(dataType);
            }
            handleOptionalAttribute(partNode, xsdParticle);
            addSubstitutionDetails(xsdSchema, partNode, xsdElementDeclarationParticle, null);
        } else if (xsdTerm instanceof XSDModelGroup) {
            XSDModelGroup xsdModelGroup = (XSDModelGroup) xsdTerm;
            ATreeNode node = addChoiceDetails(parentNode, xsdModelGroup);
            handleOptionalAttribute(node, xsdParticle);
            for (Object element : xsdModelGroup.getParticles()) {
                XSDParticle childParticle = (XSDParticle) element;
                addParticleDetail(xsdSchema, childParticle, node, currentPath);
            }
        }
    }

    private void handleOptionalAttribute(ATreeNode node, XSDParticle xsdParticle) {
        if (node == null || xsdParticle == null || xsdParticle.getElement() == null) {
            return;
        }
        node.setOptional("0".equals(xsdParticle.getElement().getAttribute("minOccurs"))); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private ATreeNode addChoiceDetails(ATreeNode parentNode, XSDModelGroup xsdModelGroup) {
        if (xsdModelGroup.getCompositor() == XSDCompositor.CHOICE_LITERAL) {
            if (supportChoice) {
                ATreeNode emptyNode = new ATreeNode();
                emptyNode.setValue(parentNode.getValue() + CHOICE);
                emptyNode.setLabel(CHOICE);
                emptyNode.setChoice(true);
                parentNode.addChild(emptyNode);
                return emptyNode;
            }
        }

        return parentNode;
    }

    public ATreeNode getSchemaTree(XSDSchema xsdSchema, ATreeNode selectedNode) {
        return getSchemaTree(xsdSchema, selectedNode, true, false, false);
    }

    public ATreeNode getSchemaTree(XSDSchema xsdSchema, ATreeNode selectedNode, boolean supportChoice, boolean supportSubstitution) {
        return getSchemaTree(xsdSchema, selectedNode, true, supportChoice, supportSubstitution);
    }

    public ATreeNode getSchemaTree(XSDSchema xsdSchema, ATreeNode selectedNode, boolean includeAttribute, boolean supportChoice,
            boolean supportSubstitution) {
        this.includeAttribute = includeAttribute;
        this.supportChoice = supportChoice;
        this.supportSubstitution = supportSubstitution;
        particleToTreeNode.clear();
        List<ATreeNode> rootNodes = new ArrayList<ATreeNode>();

        prefixNumberGenerated = 1;
        List all = new ArrayList(xsdSchema.getElementDeclarations());

        try {
            for (Iterator i = all.iterator(); i.hasNext();) {
                XSDElementDeclaration xsdElementDeclaration = (XSDElementDeclaration) i.next();
                String elementName = xsdElementDeclaration.getName();

                ATreeNode node = new ATreeNode();
                String prefix = null;
                String namespace = xsdElementDeclaration.getTargetNamespace();
                node.setCurrentNamespace(namespace);

                String originalElementName = xsdElementDeclaration.getName();
                XSDTypeDefinition typeDef = xsdElementDeclaration.getTypeDefinition();

                if (namespace != null) {
                    prefix = xsdElementDeclaration.getQName().contains(":") ? xsdElementDeclaration.getQName().split(":")[0] : "";
                    if (prefix == null || prefix.isEmpty()) {
                        if (xsdSchema.getQNamePrefixToNamespaceMap().containsValue(typeDef.getTargetNamespace())) {
                            for (String key : xsdSchema.getQNamePrefixToNamespaceMap().keySet()) {
                                if (xsdSchema.getQNamePrefixToNamespaceMap().get(key).equals(typeDef.getTargetNamespace())) {
                                    prefix = key;
                                }
                            }
                        }
                    }

                    if (isEnableGeneratePrefix() && (prefix == null || prefix.isEmpty())) {
                        // generate a new prefix
                        prefix = "p" + prefixNumberGenerated;
                        prefixNumberGenerated++;
                    }
                    if (prefix != null && !prefix.isEmpty()) {
                        elementName = prefix + ":" + xsdElementDeclaration.getName();
                        namespaceToPrefix.put(namespace, prefix);
                    } else {
                        ATreeNode namespaceNode = new ATreeNode();
                        namespaceNode.setDataType("");
                        namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                        namespaceNode.setValue(namespace);
                        node.addChild(namespaceNode);
                    }
                }
                if (!elementName.equals(selectedNode.getValue())
                        && (!(prefixNumberGenerated > 1 && originalElementName.equals(selectedNode.getValue())))) {
                    namespaceToPrefix.clear();
                    prefixNumberGenerated = 1;
                    continue;
                }

                node.setValue(elementName);
                node.setType(ATreeNode.ELEMENT_TYPE);
                node.setDataType(xsdElementDeclaration.getName());

                XSDTypeDefinition xsdTypeDefinition = xsdElementDeclaration.getTypeDefinition();
                if (xsdTypeDefinition == null) {
                    XSDComplexTypeDefinition generalType = xsdSchema.resolveComplexTypeDefinitionURI(xsdElementDeclaration
                            .getURI());
                    if (generalType.getContainer() != null) {
                        xsdTypeDefinition = generalType;
                    }
                }
                if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                    node.setDataType(xsdTypeDefinition.getName());
                }
                if (xsdTypeDefinition instanceof XSDComplexTypeDefinition) {
                    addComplexTypeDetails(xsdSchema, node, xsdTypeDefinition, prefix, namespace, "/" + elementName + "/");
                }
                List<String> namespaceList = new ArrayList(namespaceToPrefix.keySet());
                Collections.reverse(namespaceList);
                for (String currentNamespace : namespaceList) {
                    ATreeNode namespaceNode = null;
                    if (currentNamespace != null) {
                        prefix = namespaceToPrefix.get(currentNamespace);
                        namespaceNode = new ATreeNode();
                        namespaceNode.setDataType(prefix);
                        namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                        namespaceNode.setValue(currentNamespace);
                        node.addAsFirstChild(namespaceNode);
                    }
                }

                addSubstitutionDetails(xsdSchema, node, xsdElementDeclaration, null);

                rootNodes.add(node);
                break;
            }

            if (rootNodes.isEmpty()) {
                all = new ArrayList(xsdSchema.getTypeDefinitions());
                // all = XSDNamedComponentImpl.sortNamedComponents(all);

                for (Iterator i = all.iterator(); i.hasNext();) {
                    XSDTypeDefinition xsdTypeDefinition = (XSDTypeDefinition) i.next();
                    String elementName = xsdTypeDefinition.getQName();
                    if (!elementName.equals(selectedNode.getValue())) {
                        continue;
                    }
                    ATreeNode node = new ATreeNode();
                    node.setValue(elementName);
                    node.setType(ATreeNode.ELEMENT_TYPE);
                    node.setDataType(xsdTypeDefinition.getName());
                    // XSDComplexTypeDefinition generalType =
                    // xsdSchema.resolveComplexTypeDefinitionURI(xsdTypeDefinition.getURI());
                    // if (generalType.getContainer() != null) {
                    // xsdTypeDefinition = generalType;
                    // }
                    if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                        node.setDataType(xsdTypeDefinition.getQName());
                    }
                    if (xsdTypeDefinition instanceof XSDComplexTypeDefinition) {
                        String prefix = elementName.contains(":") ? elementName.split(":")[0] : "";
                        String namespace = xsdTypeDefinition.getTargetNamespace();
                        addComplexTypeDetails(xsdSchema, node, xsdTypeDefinition, prefix, namespace, "/" + elementName + "/");
                    }
                    if (xsdTypeDefinition.getTargetNamespace() != null) {
                        String prefix = namespaceToPrefix.get(xsdTypeDefinition.getTargetNamespace());
                        if (prefix != null) {
                            node.setValue(prefix + ":" + xsdTypeDefinition.getName());
                        }
                        node.setCurrentNamespace(xsdTypeDefinition.getTargetNamespace());
                    }
                    List<String> namespaceList = new ArrayList(namespaceToPrefix.keySet());
                    Collections.reverse(namespaceList);
                    for (String namespace : namespaceList) {
                        ATreeNode namespaceNode = null;
                        if (namespace != null) {
                            String prefix = namespaceToPrefix.get(namespace);
                            namespaceNode = new ATreeNode();
                            namespaceNode.setDataType(prefix);
                            namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                            namespaceNode.setValue(namespace);
                            node.addAsFirstChild(namespaceNode);
                        }
                    }

                    rootNodes.add(node);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            particleToTreeNode.clear();
        }

        return rootNodes.get(0);
    }

    private void addSubstitutionDetails(XSDSchema xsdSchema, ATreeNode parentNode, XSDElementDeclaration elementDeclaration,
            ATreeNode rootSubsNode) throws OdaException, IllegalAccessException, InvocationTargetException {
        boolean hasSubstitution = false;
        Set<ATreeNode> subsChildren = new HashSet<ATreeNode>();

        List<XSDElementDeclaration> directSubstitutionGroups = getDirectSubstitutionGroups(elementDeclaration);
        for (XSDElementDeclaration xsdElementDeclaration : directSubstitutionGroups) {
            String elementName = xsdElementDeclaration.getName();
            hasSubstitution = true;
            if (rootSubsNode == null) {
                rootSubsNode = parentNode;
            }
            ATreeNode node = new ATreeNode();
            String prefix = null;
            String namespace = xsdElementDeclaration.getTargetNamespace();
            node.setCurrentNamespace(namespace);

            XSDTypeDefinition typeDef = xsdElementDeclaration.getTypeDefinition();

            if (namespace != null) {
                prefix = xsdElementDeclaration.getQName().contains(":") ? xsdElementDeclaration.getQName().split(":")[0] : "";
                if (prefix == null || prefix.isEmpty()) {
                    if (xsdSchema.getQNamePrefixToNamespaceMap().containsValue(typeDef.getTargetNamespace())) {
                        for (String key : xsdSchema.getQNamePrefixToNamespaceMap().keySet()) {
                            if (xsdSchema.getQNamePrefixToNamespaceMap().get(key).equals(typeDef.getTargetNamespace())) {
                                prefix = key;
                            }
                        }
                    }
                }

                if (isEnableGeneratePrefix() && (prefix == null || prefix.isEmpty())) {
                    // generate a new prefix
                    prefix = "p" + prefixNumberGenerated;
                    prefixNumberGenerated++;
                }
                if (prefix != null && !prefix.isEmpty()) {
                    elementName = prefix + ":" + xsdElementDeclaration.getName();
                    namespaceToPrefix.put(namespace, prefix);
                } else {
                    ATreeNode namespaceNode = new ATreeNode();
                    namespaceNode.setDataType("");
                    namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                    namespaceNode.setValue(namespace);
                    node.addChild(namespaceNode);
                }
            }

            node.setValue(elementName);
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(xsdElementDeclaration.getName());

            XSDTypeDefinition xsdTypeDefinition = xsdElementDeclaration.getTypeDefinition();
            if (xsdTypeDefinition == null) {
                XSDComplexTypeDefinition generalType = xsdSchema.resolveComplexTypeDefinitionURI(xsdElementDeclaration.getURI());
                if (generalType.getContainer() != null) {
                    xsdTypeDefinition = generalType;
                }
            }
            if (xsdTypeDefinition != null && xsdTypeDefinition.getName() != null) {
                node.setDataType(xsdTypeDefinition.getName());
            }
            if (xsdTypeDefinition instanceof XSDComplexTypeDefinition) {
                addComplexTypeDetails(xsdSchema, node, xsdTypeDefinition, prefix, namespace, "/" + elementName + "/");
            }
            List<String> namespaceList = new ArrayList(namespaceToPrefix.keySet());
            Collections.reverse(namespaceList);
            for (String currentNamespace : namespaceList) {
                ATreeNode namespaceNode = null;
                if (currentNamespace != null) {
                    prefix = namespaceToPrefix.get(currentNamespace);
                    namespaceNode = new ATreeNode();
                    namespaceNode.setDataType(prefix);
                    namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                    namespaceNode.setValue(currentNamespace);
                    node.addAsFirstChild(namespaceNode);
                }
            }
            addSubstitutionDetails(xsdSchema, node, xsdElementDeclaration, rootSubsNode);

            subsChildren.add(node);
        }
        if (hasSubstitution) {
            if (supportSubstitution) {
                Object oldValue = parentNode.getValue();
                parentNode.setSubstitution(true);
                parentNode.setValue(oldValue + SUBS);
                Object[] originalChildren = parentNode.getChildren();
                parentNode.removeAllChildren();
                if (!elementDeclaration.isAbstract() || includeAbsSubs) {
                    ATreeNode cloneNode = new ATreeNode();
                    BeanUtils.copyProperties(cloneNode, parentNode);
                    cloneNode.setSubstitution(false);
                    cloneNode.setValue(oldValue);
                    cloneNode.addChild(originalChildren);
                    parentNode.addAsFirstChild(cloneNode);
                }
                parentNode.addChild(subsChildren.toArray());
            } else {
                if (rootSubsNode != null) {
                    ATreeNode parent = rootSubsNode.getParent();
                    if (parent != null) {
                        List children = Arrays.asList(parent.getChildren());
                        for (ATreeNode child : subsChildren) {
                            if (!children.contains(child)) {
                                parent.addChild(child);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<XSDElementDeclaration> getDirectSubstitutionGroups(XSDElementDeclaration elementDeclaration) {
        List<XSDElementDeclaration> subsGroupList = new ArrayList<XSDElementDeclaration>();
        if (hasSubstitutionGroup(elementDeclaration)) {
            List<XSDElementDeclaration> underSubsElements = new ArrayList<XSDElementDeclaration>();
            EList<XSDElementDeclaration> substitutionGroup = elementDeclaration.getSubstitutionGroup();
            subsGroupList = new ArrayList<XSDElementDeclaration>(substitutionGroup);
            for (XSDElementDeclaration xsdElementDeclaration : substitutionGroup) {
                String elementName = xsdElementDeclaration.getName();
                if (elementName != null && elementName.equals(elementDeclaration.getName())) {
                    subsGroupList.remove(xsdElementDeclaration);
                    continue;
                }
                if (hasSubstitutionGroup(xsdElementDeclaration)) {
                    EList<XSDElementDeclaration> subsGroup = xsdElementDeclaration.getSubstitutionGroup();
                    for (XSDElementDeclaration xsdElement : subsGroup) {
                        String eleName = xsdElement.getName();
                        if (eleName != null && eleName.equals(xsdElementDeclaration.getName())) {
                            continue;
                        }
                        underSubsElements.add(xsdElement);
                    }
                }
            }
            subsGroupList.removeAll(underSubsElements);
        }

        return subsGroupList;
    }

    private boolean hasSubstitutionGroup(XSDElementDeclaration elementDeclaration) {
        EList<XSDElementDeclaration> substitutionGroup = elementDeclaration.getSubstitutionGroup();
        if (substitutionGroup.size() > 0) {
            for (XSDElementDeclaration xsdElementDeclaration : substitutionGroup) {
                String elementName = xsdElementDeclaration.getName();
                if (elementName != null && !elementName.equals(elementDeclaration.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addComplexTypeDetails(XSDSchema xsdSchema, ATreeNode node, XSDTypeDefinition xsdTypeDefinition, String prefix,
            String namespace, String currentPath) throws OdaException, IllegalAccessException, InvocationTargetException {
        String prefixToUse = prefix;
        if (namespace != null && !namespaceToPrefix.containsKey(namespace)) {
            if (isEnableGeneratePrefix() && (prefix == null || prefix.isEmpty())) {
                // generate a new prefix
                prefixToUse = "p" + prefixNumberGenerated;
                prefixNumberGenerated++;
            }
            // check if prefix already exists, if yes, generate a new one.
            if (namespaceToPrefix.containsValue(prefix)) {
                prefixToUse = prefixToUse + prefixNumberGenerated;
                prefixNumberGenerated++;
            }
            if (prefixToUse != null && !prefixToUse.isEmpty()) {
                namespaceToPrefix.put(namespace, prefixToUse);
            }
        }

        XSDComplexTypeDefinition xsdComplexTypeDefinition = (XSDComplexTypeDefinition) xsdTypeDefinition;
        if (xsdComplexTypeDefinition.getContentType() instanceof XSDParticle) {
            addParticleDetail(xsdSchema, (XSDParticle) xsdComplexTypeDefinition.getContentType(), node, currentPath);
        }
        for (Object element : xsdComplexTypeDefinition.getAttributeUses()) {
            XSDAttributeUse xsdAttributeUse = (XSDAttributeUse) element;
            XSDAttributeDeclaration xsdAttributeDeclaration = xsdAttributeUse.getAttributeDeclaration();
            String attributeDeclarationName = xsdAttributeDeclaration.getName();
            ATreeNode childNode = new ATreeNode();
            childNode.setValue(attributeDeclarationName);
            childNode.setType(ATreeNode.ATTRIBUTE_TYPE);
            String dataType = xsdAttributeDeclaration.getTypeDefinition().getQName();
            XSDTypeDefinition baseType = xsdAttributeDeclaration.getTypeDefinition().getBaseType();
            if (!XSDConstants.isSchemaForSchemaNamespace(xsdAttributeDeclaration.getTypeDefinition().getTargetNamespace())) {
                if (baseType != null && !"xs:anySimpleType".equals(baseType.getQName())) { //$NON-NLS-1$
                    dataType = baseType.getQName();
                }
            }
            if (dataType != null && dataType.length() > 0) {
                childNode.setDataType(dataType);
            }
            node.addChild(childNode);
        }
    }

    public boolean isEnableGeneratePrefix() {
        return enableGeneratePrefix;
    }

    public void setEnableGeneratePrefix(boolean enableGeneratePrefix) {
        this.enableGeneratePrefix = enableGeneratePrefix;
    }

    /**
     * Sets the includeAbsSubs.
     * 
     * @param includeAbsSubs the includeAbsSubs to set
     */
    public void setIncludeAbsSubs(boolean includeAbsSubs) {
        this.includeAbsSubs = includeAbsSubs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.IXSDPopulationUtil#loadWSDL(java.lang.String)
     */
    @Override
    public void loadWSDL(String wsdlFile) throws IOException {
    }

}
