/***********************************************************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 **********************************************************************************************************************/

package org.talend.datatools.xml.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;

/**
 * This class is used to offer GUI a utility to get an tree from certain xml/xsd file.
 */
public class SchemaPopulationUtil {

    /**
     * @param fileName
     * @throws URISyntaxException
     * @throws IOException
     */
    public static ATreeNode getSchemaTree(String fileName, boolean includeAttribute, boolean forMDM,
            int numberOfElementsAccessiable, List<String> attList) throws OdaException, URISyntaxException, IOException {
        ATreeNode node = null;
        NodeCreationObserver.start();
        if (fileName.toUpperCase().endsWith(".XSD")) {
            node = XSDFileSchemaTreePopulator.getSchemaTree(fileName, includeAttribute, forMDM, attList);
        } else if (fileName.toUpperCase().endsWith(".DTD")) {
            node = new DTDFileSchemaTreePopulator().getSchemaTree(fileName, includeAttribute);
        } else {
            node = new XMLFileSchemaTreePopulator(numberOfElementsAccessiable).getSchemaTree(fileName, includeAttribute);
        }
        NodeCreationObserver.stop();
        return node;
    }

    public static ATreeNode getSchemaTree(String fileName, boolean includeAttribute, int numberOfElementsAccessiable)
            throws OdaException, URISyntaxException, IOException {
        ATreeNode node = null;
        NodeCreationObserver.start();
        if (fileName.toUpperCase().endsWith(".XSD")) {
            node = XSDFileSchemaTreePopulator.getSchemaTree(fileName, includeAttribute);
        } else if (fileName.toUpperCase().endsWith(".DTD")) {
            node = new DTDFileSchemaTreePopulator().getSchemaTree(fileName, includeAttribute);
        } else {
            node = new XMLFileSchemaTreePopulator(numberOfElementsAccessiable).getSchemaTree(fileName, includeAttribute);
        }
        NodeCreationObserver.stop();
        return node;
    }

    /**
     * DOC ycbai Comment method "getSchemaTree".
     * 
     * Only for xsd.
     * 
     * @param xsModel
     * @param selectedNode
     * @param includeAttribute
     * @return
     * @throws OdaException
     * @throws URISyntaxException
     * @throws IOException
     */
    public static ATreeNode getSchemaTree(XSModel xsModel, ATreeNode selectedNode, boolean includeAttribute) throws OdaException,
            URISyntaxException, IOException {
        ATreeNode node = null;
        NodeCreationObserver.start();
        node = XSDFileSchemaTreePopulator.getSchemaTree(xsModel, selectedNode, includeAttribute);
        NodeCreationObserver.stop();
        return node;
    }

    public static ATreeNode getSchemaTree(XSDSchema schema, ATreeNode selectedNode) throws OdaException, URISyntaxException,
            IOException {
        return getSchemaTree(null, schema, selectedNode);
    }

    public static ATreeNode getSchemaTree(XSDPopulationUtil2 popUtil, XSDSchema schema, ATreeNode selectedNode)
            throws OdaException, URISyntaxException, IOException {
        ATreeNode node = null;
        NodeCreationObserver.start();
        if (popUtil == null) {
            popUtil = new XSDPopulationUtil2();
        }
        node = popUtil.getSchemaTree(schema, selectedNode);
        NodeCreationObserver.stop();
        return node;
    }

    public static ATreeNode getSchemaTree(XSDPopulationUtil2 popUtil, XSDSchema schema, ATreeNode selectedNode,
            boolean supportChoice, boolean supportSubstitution) throws OdaException, URISyntaxException, IOException {
        ATreeNode node = null;
        NodeCreationObserver.start();
        if (popUtil == null) {
            popUtil = new XSDPopulationUtil2();
        }
        node = popUtil.getSchemaTree(schema, selectedNode, supportChoice, supportSubstitution);
        NodeCreationObserver.stop();
        return node;
    }

    public static ATreeNode getSchemaTree(XSDSchema schema, ATreeNode selectedNode, boolean supportChoice,
            boolean supportSubstitution) throws OdaException, URISyntaxException, IOException {
        return getSchemaTree(null, schema, selectedNode, supportChoice, supportSubstitution);
    }

}

/**
 * This class is used to populate an XML schema tree from an xml file.
 * 
 */
/**
 * DOC nrousseau class global comment. Detailled comment
 */
final class XMLFileSchemaTreePopulator implements ISaxParserConsumer {

    //
    private int rowCount;

    private final ATreeNode root;

    private SaxParser sp;

    private boolean includeAttribute = true;

    private final int numberOfElementsAccessiable;

    private Map<String, String> prefixToNamespace = new HashMap<String, String>();

    private Map<String, ATreeNode> nodePathMap = new HashMap<String, ATreeNode>();

    Thread spThread;

    /**
     * 
     * 
     */
    XMLFileSchemaTreePopulator(int numberOfElementsAccessiable) {
        this.rowCount = 0;
        this.root = new ATreeNode();
        this.root.setValue("ROOT");
        this.numberOfElementsAccessiable = numberOfElementsAccessiable == 0 ? Integer.MAX_VALUE : numberOfElementsAccessiable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.datatools.enablement.oda.xml.util.ISaxParserConsumer#manipulateData(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void manipulateData(String path, String value) {
        guessColumnInfo(path, value);
    }

    /**
     * DOC cmeng Comment method "guessDataType".
     * 
     * @param path
     * @param value
     */
    private void guessColumnInfo(String path, String value) {
        // Most of the codes are copied from XmlFileStep3Form.refreshMetaDataTable(...)

        ATreeNode treeNode = nodePathMap.get(getTreamedPath(path));
        if (treeNode == null) {
            return;
        }

        // 1. set data type
        String dataType = treeNode.getDataType();
        if (StringUtils.isEmpty(dataType)) {
            dataType = JavaDataTypeHelper.getTalendTypeOfValue(value);
        } else {
            dataType = JavaDataTypeHelper.getCommonType(dataType, JavaDataTypeHelper.getTalendTypeOfValue(value));
        }
        try {
            treeNode.setDataType(dataType);
        } catch (OdaException e) {
            ExceptionHandler.process(e);
        }

        if (StringUtils.isEmpty(value)) {
            return;
        }
        // 2. set data length
        long dataLength = treeNode.getDataMaxLength();
        long curValueLength = value.length();
        if (dataLength < curValueLength) {
            dataLength = curValueLength;
        }
        treeNode.setDataMaxLength(dataLength);

        // 3. set precision
        int curPrecision = 0;
        long precisionValue = 0;
        if (value.indexOf(',') > -1) {
            curPrecision = value.lastIndexOf(',');
            precisionValue = dataLength - curPrecision;
        } else if (value.indexOf('.') > -1) {
            curPrecision = value.lastIndexOf('.');
            precisionValue = dataLength - curPrecision;
        }

        if (JavaTypesManager.FLOAT.getId().equals(dataType) || JavaTypesManager.DOUBLE.getId().equals(dataType)) {
            treeNode.setPrecisionValue(precisionValue);
        } else {
            treeNode.setPrecisionValue(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.datatools.enablement.oda.xml.util.ISaxParserConsumer#detectNewRow(java.lang.String,
     * java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void detectNewRow(String path, String prefix, String uri, boolean start) {
        String treamedPath = getTreamedPath(path);
        this.insertNode(treamedPath, prefix, uri);
        // If not attribute
        if (!isAttribute(path) && start) {
            rowCount++;
        }

        // Only parser the first 10000 elements
        if (rowCount >= numberOfElementsAccessiable) {
            assert sp != null;
            sp.setStart(false);
            sp.stopParsing();
        }

    }

    private String getTreamedPath(String path) {
        return path.replaceAll("\\Q[\\E\\d+\\Q]\\E", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Exam whether given path specified an attribute
     * 
     * @param path
     * @return
     */
    private boolean isAttribute(String path) {
        return path.matches(".*\\Q[@\\E.+\\Q]\\E.*");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.datatools.enablement.oda.xml.util.ISaxParserConsumer#wakeup()
     */
    @Override
    public synchronized void wakeup() {
        notify();
    }

    /**
     * Return the root node of a schema tree.
     * 
     * @param fileName
     * @return
     */
    public ATreeNode getSchemaTree(String fileName, boolean includeAttribute) {
        this.includeAttribute = includeAttribute;
        // try {
        // sp = new SaxParser(XMLDataInputStreamCreator.getCreator(fileName).createXMLDataInputStream(), this);
        sp = new SaxParser(fileName, this);

        spThread = new Thread(sp);
        spThread.start();
        while (sp.isAlive() && !sp.isSuspended()) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // } catch (OdaException e1) {
        // e1.printStackTrace();
        // }
        if (sp.isInvalidFile()) {
            throw new RuntimeException();
        }
        return root;
    }

    /**
     * Insert a node specified by the path.
     * 
     * @param treatedPath
     */
    private void insertNode(String treatedPath, String prefix, String uri) {
        boolean isAttribute = isAttribute(treatedPath);

        // Remove the leading "/" then split the path.
        String[] path = treatedPath.replaceFirst("/", "").split("/");

        // If the path specified an attribute then re-build the path array so
        // that it can divid element and
        // its attribute to two array items.
        if (isAttribute) {
            String[] temp = path[path.length - 1].split("\\Q[@\\E");

            assert temp.length == 2;

            String[] temp1 = new String[path.length + 1];
            for (int i = 0; i < path.length - 1; i++) {
                temp1[i] = path[i];
            }
            temp1[temp1.length - 2] = temp[0];
            temp1[temp1.length - 1] = temp[1].replaceAll("\\Q]\\E", "");
            path = temp1;
        }

        // The parentNode
        ATreeNode parentNode = root;

        // Iterate each path array element, find or create its countpart node
        // instance.
        for (int i = 0; i < path.length; i++) {
            // This variable hosts the node instance that matches the given path
            // array item value.
            ATreeNode matchedNode = null;

            for (int j = 0; j < parentNode.getChildren().length; j++) {
                if (((ATreeNode) parentNode.getChildren()[j]).getValue().equals(path[i])) {
                    matchedNode = (ATreeNode) parentNode.getChildren()[j];
                    break;
                }
            }
            if (matchedNode != null) {
                parentNode = matchedNode;
            } else {
                matchedNode = new ATreeNode();

                nodePathMap.put(treatedPath, matchedNode);

                if ((i == path.length - 1) && isAttribute) {
                    if (isAttribute && !this.includeAttribute) {
                        continue;
                    }
                    matchedNode.setType(ATreeNode.ATTRIBUTE_TYPE);
                } else {
                    matchedNode.setType(ATreeNode.ELEMENT_TYPE);
                }

                matchedNode.setValue(path[i]);
                parentNode.addChild(matchedNode);
                if (prefix != null) {
                    if (!prefixToNamespace.containsKey(prefix)) {
                        prefixToNamespace.put(prefix, uri);
                        ATreeNode namespaceNode = new ATreeNode();
                        try {
                            namespaceNode.setDataType(prefix);
                        } catch (OdaException e) {
                            // nothing
                        }
                        namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                        namespaceNode.setValue(uri);
                        ((ATreeNode) root.getChildren()[0]).addAsFirstChild(namespaceNode);
                    }
                } else if (uri != null && !"".equals(uri)) {
                    ATreeNode namespaceNode = new ATreeNode();
                    try {
                        namespaceNode.setDataType("");
                    } catch (OdaException e) {
                        // nothing
                    }
                    namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                    namespaceNode.setValue(uri);
                    matchedNode.addChild(namespaceNode);
                }
                parentNode = matchedNode;
            }
        }
    }
}

/**
 * This class is used to populate an XML schema tree from an xml file.
 * 
 */
final class XSDFileSchemaTreePopulator {

    private static boolean includeAttribute = true;

    /**
     * Populate the whole tree until all the leaves have no child.
     * 
     * @param root
     */
    private static void populateRoot(ATreeNode root) {
        Object[] toBeIterated = root.getChildren();
        for (Object element : toBeIterated) {
            Object value = ((ATreeNode) element).getDataType();
            List container = new ArrayList();
            if (value != null) {
                findNodeWithValue(root, value.toString(), container, new VisitingRecorder());
            }
            for (int j = 0; j < container.size(); j++) {
                if (((ATreeNode) container.get(j)).getChildren().length == 0) {
                    Object[] os = ((ATreeNode) element).getChildren();
                    for (int k = 0; k < os.length; k++) {
                        if (os[k] instanceof ATreeNode) {
                            if (!(((ATreeNode) os[k]).getDataType() != null && ((ATreeNode) os[k]).getDataType().equals(
                                    ((ATreeNode) container.get(j)).getDataType()))) {
                                ((ATreeNode) container.get(j)).addChild(os[k]);
                            }
                        }
                    }

                }
            }
        }
    }

    private static void populateRoot(ATreeNode root, ATreeNode selectedNode) {
        Object[] toBeIterated = root.getChildren();
        for (Object element : toBeIterated) {
            ATreeNode node = ((ATreeNode) element);
            Object nodeValue = node.getValue();
            if (nodeValue != null && !nodeValue.equals(selectedNode.getValue()) && !nodeValue.equals(selectedNode.getDataType())) {
                continue;
            }
            Object value = node.getDataType();
            List container = new ArrayList();
            findNodeWithValue(root, value.toString(), container, new VisitingRecorder());
            for (int j = 0; j < container.size(); j++) {
                if (((ATreeNode) container.get(j)).getChildren().length == 0) {
                    Object[] os = ((ATreeNode) element).getChildren();
                    for (int k = 0; k < os.length; k++) {
                        if (os[k] instanceof ATreeNode) {
                            if (!(((ATreeNode) os[k]).getDataType() != null && ((ATreeNode) os[k]).getDataType().equals(
                                    ((ATreeNode) container.get(j)).getDataType()))) {
                                ((ATreeNode) container.get(j)).addChild(os[k]);
                            }
                        }
                    }

                }
            }
            break;
        }
    }

    /**
     * Starting from a tree node, find all nodes with given value, and put it to container.
     * 
     * @param root
     * @param value
     * @param container
     */
    private static void findNodeWithValue(ATreeNode root, String value, List container, VisitingRecorder vr) {

        if (root.getType() == ATreeNode.ELEMENT_TYPE && !vr.visit(root.getValue().toString())) {
            return;
        }
        if (root.getDataType() != null && root.getDataType().equals(value)) {
            container.add(root);
        }
        Object[] children = root.getChildren();
        for (Object element : children) {
            if (element instanceof ATreeNode) {
                findNodeWithValue((ATreeNode) element, value, container, vr);
            }

        }
    }

    /**
     * Return the root node of a schema tree.
     * 
     * @param fileName
     * @return
     * @throws OdaException
     * @throws MalformedURLException
     * @throws URISyntaxException
     * @throws ParserConfigurationException 
     */
    public static ATreeNode getSchemaTree(String fileName, boolean incAttr, boolean forMDM, List<String> attList)
            throws OdaException, MalformedURLException, URISyntaxException {
        includeAttribute = incAttr;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        factory.setNamespaceAware(true);
        URI uri = null;
        File f = new File(fileName);
        if (f.exists()) {
            uri = f.toURI();
        } else {
            URL url = new URL(fileName);
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }

        // Then try to parse the input string as a url in web.
        if (uri == null) {
            uri = new URI(fileName);
        }

        // fixed a bug when parse one file contians Franch ,maybe need modification
        XMLSchemaLoader xsLoader = new XMLSchemaLoader();
        XSModel xsModel = xsLoader.loadURI(uri.toString());
        if (xsModel == null) {
            try {
                Grammar loadGrammar = xsLoader.loadGrammar(new XMLInputSource(null, uri.toString(), null, new FileInputStream(f),
                        "ISO-8859-1"));
                xsModel = ((XSGrammar) loadGrammar).toXSModel();
            } catch (XNIException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ATreeNode complexTypesRoot = populateComplexTypeTree(xsModel);

        XSNamedMap map = xsModel.getComponents(XSConstants.ELEMENT_DECLARATION);

        ATreeNode root = new ATreeNode();
        root.setValue("ROOT");
        for (int i = 0; i < map.getLength(); i++) {
            ATreeNode node = new ATreeNode();
            XSElementDecl element = (XSElementDecl) map.item(i);

            String namespace = element.getNamespace();
            XSObject unique = element.getIdentityConstraints().itemByName(namespace, element.getName());
            if (unique instanceof UniqueOrKey) {
                node.getUniqueNames().clear();
                UniqueOrKey uniqueOrKey = (UniqueOrKey) unique;
                String uniqueName = "";
                StringList fieldStrs = uniqueOrKey.getFieldStrs();
                for (int j = 0; j < fieldStrs.getLength(); j++) {
                    uniqueName = fieldStrs.item(j);
                    if (uniqueName != null && !"".equals(uniqueName)) {
                        uniqueName = uniqueName.replace("/", "").replace(".", "");
                        node.getUniqueNames().add(uniqueName);
                    }
                }
            }
            ATreeNode namespaceNode = null;
            if (namespace != null) {
                namespaceNode = new ATreeNode();
                namespaceNode.setDataType(namespace);
                namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                namespaceNode.setValue(namespace);
            }

            node.setValue(element.getName());
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(element.getName());

            if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
                XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition();
                // If the complex type is explicitly defined, that is, it has name.
                if (complexType.getName() != null) {
                    node.setDataType(complexType.getName());
                    ATreeNode n = findComplexElement(complexTypesRoot, complexType.getName());
                    if (namespaceNode != null) {
                        node.addChild(namespaceNode);
                    }
                    if (n != null) {
                        node.addChild(n.getChildren());
                    }
                }
                // If the complex type is implicitly defined, that is, it has no name.
                else {
                    // If the complex type is implicitly defined , no need to check it in explicitly complex type
                    addParticleAndAttributeInfo(node, complexType, complexTypesRoot, new VisitingRecorder());
                }
            }
            String nodeType = node.getOriginalDataType();
            if (forMDM) {
                Object nodeValue = node.getValue();
                if (nodeValue != null && !attList.contains(nodeValue) && nodeType != null && !attList.contains(nodeType)) {
                    continue;
                }
            } else {
                if (nodeType != null && !attList.contains(nodeType)) {
                    continue;
                }
            }
            if (forMDM && attList.size() == 1 && node.getValue().equals(attList.get(0))) {
                root = node;
            } else {
                root.addChild(node);
            }
        }

        // if no base element, display all complex types / attributes directly.
        if (map.getLength() == 0) {
            root.addChild(complexTypesRoot.getChildren());
        }

        populateRoot(root);
        return root;

    }

    /**
     * Return the root node of a schema tree.
     * 
     * @param fileName
     * @return
     * @throws OdaException
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public static ATreeNode getSchemaTree(String fileName, boolean incAttr) throws OdaException, MalformedURLException,
            URISyntaxException {
        includeAttribute = incAttr;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URI uri = null;
        File f = new File(fileName);
        if (f.exists()) {
            uri = f.toURI();
        } else {
            URL url = new URL(fileName);
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }

        // Then try to parse the input string as a url in web.
        if (uri == null) {
            uri = new URI(fileName);
        }

        // fixed a bug when parse one file contians Franch ,maybe need modification
        XMLSchemaLoader xsLoader = new XMLSchemaLoader();
        XSModel xsModel = xsLoader.loadURI(uri.toString());
        if (xsModel == null) {
            try {
                Grammar loadGrammar = xsLoader.loadGrammar(new XMLInputSource(null, uri.toString(), null, new FileInputStream(f),
                        "ISO-8859-1"));
                xsModel = ((XSGrammar) loadGrammar).toXSModel();
            } catch (XNIException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ATreeNode complexTypesRoot = populateComplexTypeTree(xsModel);

        XSNamedMap map = xsModel.getComponents(XSConstants.ELEMENT_DECLARATION);

        ATreeNode root = new ATreeNode();

        root.setValue("ROOT");
        for (int i = 0; i < map.getLength(); i++) {
            ATreeNode node = new ATreeNode();
            XSElementDecl element = (XSElementDecl) map.item(i);

            String namespace = element.getNamespace();
            XSObject unique = element.getIdentityConstraints().itemByName(namespace, element.getName());
            if (unique instanceof UniqueOrKey) {
                node.getUniqueNames().clear();
                UniqueOrKey uniqueOrKey = (UniqueOrKey) unique;
                String uniqueName = "";
                StringList fieldStrs = uniqueOrKey.getFieldStrs();
                for (int j = 0; j < fieldStrs.getLength(); j++) {
                    uniqueName = fieldStrs.item(j);
                    if (uniqueName != null && !"".equals(uniqueName)) {
                        uniqueName = uniqueName.replace("/", "").replace(".", "");
                        node.getUniqueNames().add(uniqueName);
                    }
                }
            }
            ATreeNode namespaceNode = null;
            if (namespace != null) {
                namespaceNode = new ATreeNode();
                namespaceNode.setDataType(namespace);
                namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                namespaceNode.setValue(namespace);
            }

            node.setValue(element.getName());
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(element.getName());
            if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
                XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition();
                // If the complex type is explicitly defined, that is, it has name.
                if (complexType.getName() != null) {
                    node.setDataType(complexType.getName());
                    ATreeNode n = findComplexElement(complexTypesRoot, complexType.getName());
                    if (namespaceNode != null) {
                        node.addChild(namespaceNode);
                    }
                    if (n != null) {
                        node.addChild(n.getChildren());
                    }
                }
                // If the complex type is implicitly defined, that is, it has no name.
                else {

                    addParticleAndAttributeInfo(node, complexType, complexTypesRoot, new VisitingRecorder());
                }
            }
            root.addChild(node);
        }

        // if no base element, display all complex types / attributes directly.
        if (map.getLength() == 0) {
            root.addChild(complexTypesRoot.getChildren());
        }

        populateRoot(root);
        return root;

    }

    /**
     * DOC ycbai Comment method "getSchemaTree".
     * 
     * @param xsModel
     * @param selectedNode
     * @param incAttr
     * @return
     * @throws OdaException
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public static ATreeNode getSchemaTree(XSModel xsModel, ATreeNode selectedNode, boolean incAttr) throws OdaException,
            MalformedURLException, URISyntaxException {
        includeAttribute = incAttr;

        ATreeNode complexTypesRoot = populateComplexTypeTree(xsModel, selectedNode);

        XSNamedMap map = xsModel.getComponents(XSConstants.ELEMENT_DECLARATION);

        ATreeNode root = new ATreeNode();

        root.setValue("ROOT");
        for (int i = 0; i < map.getLength(); i++) {
            ATreeNode node = new ATreeNode();
            XSElementDecl element = (XSElementDecl) map.item(i);
            String elementName = element.getName();
            if (elementName != null && !elementName.equals(selectedNode.getValue())
                    && !elementName.equals(selectedNode.getDataType())) {
                continue;
            }

            String namespace = element.getNamespace();
            ATreeNode namespaceNode = null;
            if (namespace != null) {
                namespaceNode = new ATreeNode();
                namespaceNode.setDataType(namespace);
                namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                namespaceNode.setValue(namespace);
            }

            node.setValue(elementName);
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(elementName);
            if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
                XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition();
                // If the complex type is explicitly defined, that is, it has name.
                if (complexType.getName() != null) {
                    node.setDataType(complexType.getName());
                    ATreeNode n = findComplexElement(complexTypesRoot, complexType.getName());
                    if (namespaceNode != null) {
                        node.addChild(namespaceNode);
                    }
                    if (n != null) {
                        node.addChild(n.getChildren());
                    }
                }
                // If the complex type is implicitly defined, that is, it has no name.
                else {

                    addParticleAndAttributeInfo(node, complexType, complexTypesRoot, new VisitingRecorder());
                }
            }
            root.addChild(node);
        }

        // if no base element, display all complex types / attributes directly.
        if (map.getLength() == 0) {
            Object[] children = complexTypesRoot.getChildren();
            if (children != null && children.length > 0) {
                for (Object obj : children) {
                    if (obj instanceof ATreeNode) {
                        ATreeNode treeNode = (ATreeNode) obj;
                        Object value = treeNode.getValue();
                        if (value != null && (value.equals(selectedNode.getValue()) || value.equals(selectedNode.getDataType()))) {
                            root.addChild(treeNode);
                            break;
                        }
                    }
                }
            }
        }

        populateRoot(root, selectedNode);
        return root;

    }

    public static XSModel getXSModel(String fileName) throws URISyntaxException, MalformedURLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URI uri = null;
        File f = new File(fileName);
        if (f.exists()) {
            uri = f.toURI();
        } else {
            URL url = new URL(fileName);
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }

        // Then try to parse the input string as a url in web.
        if (uri == null) {
            uri = new URI(fileName);
        }

        // fixed a bug when parse one file contians Franch ,maybe need modification
        XMLSchemaLoader xsLoader = new XMLSchemaLoader();
        XSModel xsModel = xsLoader.loadURI(uri.toString());
        if (xsModel == null) {
            try {
                Grammar loadGrammar = xsLoader.loadGrammar(new XMLInputSource(null, uri.toString(), null, new FileInputStream(f),
                        "ISO-8859-1"));
                xsModel = ((XSGrammar) loadGrammar).toXSModel();
            } catch (XNIException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xsModel;
    }

    /**
     * Add the particles and attributes that defined in an implicitly defined ComplexType to the node.
     * 
     * @param node
     * @param complexType
     * @throws OdaException
     */
    private static void addParticleAndAttributeInfo(ATreeNode node, XSComplexTypeDecl complexType, ATreeNode complexTypesRoot,
            VisitingRecorder vr) throws OdaException {
        if (!vr.visit(node.getValue().toString())) {
            return;
        }
        XSParticle particle = complexType.getParticle();
        if (particle != null) {
            addElementToNode(node, complexTypesRoot, (XSModelGroupImpl) particle.getTerm(), vr);
        }
        if (!includeAttribute) {
            return;
        }
        XSAttributeGroupDecl group = complexType.getAttrGrp();
        if (group != null) {
            XSObjectList list = group.getAttributeUses();
            for (int j = 0; j < list.getLength(); j++) {
                ATreeNode childNode = new ATreeNode();
                // add by wzhang, set the childNode type
                XSAttributeUseImpl attr = (XSAttributeUseImpl) list.item(j);
                String dataType = attr.fAttrDecl.getTypeDefinition().getName();
                // notes added by nma. two datatype definitions may need two different getting ways.
                if (dataType == null || dataType.length() == 0) {
                    dataType = ((XSAttributeUseImpl) list.item(j)).fAttrDecl.getTypeDefinition().getBaseType().getName();
                }
                if (dataType != null && dataType.length() > 0) {
                    childNode.setDataType(dataType);
                }
                childNode.setDataType(dataType);
                childNode.setValue(attr.getAttrDeclaration().getName());
                childNode.setType(ATreeNode.ATTRIBUTE_TYPE);
                node.addChild(childNode);
            }
        }
    }

    /**
     * 
     * @param node
     * @param complexTypesRoot
     * @param group
     * @throws OdaException
     */
    private static void addElementToNode(ATreeNode node, ATreeNode complexTypesRoot, XSModelGroupImpl group, VisitingRecorder vr)
            throws OdaException {
        if (!vr.visit(node.getValue().toString())) {
            return;
        }
        XSObjectList list = group.getParticles();
        for (int j = 0; j < list.getLength(); j++) {
            if (((XSParticleDecl) list.item(j)).getTerm() instanceof XSModelGroupImpl) {
                addElementToNode(node, complexTypesRoot, (XSModelGroupImpl) ((XSParticleDecl) list.item(j)).getTerm(), vr);
                continue;
            }
            ATreeNode childNode = new ATreeNode();
            childNode.setValue(((XSParticleDecl) list.item(j)).getTerm().getName());
            XSElementDecl term = null;
            if (((XSParticleDecl) list.item(j)).getTerm() instanceof XSElementDecl) {
                term = (XSElementDecl) ((XSParticleDecl) list.item(j)).getTerm();
            } else {
                continue;
            }
            String dataType = term.getTypeDefinition().getName();
            if (dataType == null || dataType.length() == 0) {
                dataType = childNode.getValue().toString();
            }
            childNode.setDataType(dataType);
            childNode.setType(ATreeNode.ELEMENT_TYPE);
            XSTypeDefinition xstype = term.getTypeDefinition();
            // Populate the complex data types under node.
            if ((!dataType.equals("anyType")) && xstype instanceof XSComplexTypeDecl) {
                // First do a recursive call to populate all child complex type of current node.
                if (xstype.getName() == null) {
                    addParticleAndAttributeInfo(childNode, (XSComplexTypeDecl) xstype, complexTypesRoot, vr);
                }
                ATreeNode n = findComplexElement(complexTypesRoot, dataType);
                if (n != null) {
                    childNode.addChild(n.getChildren());
                }
            }
            node.addChild(childNode);
        }
    }

    /**
     * Return the tree node instance that represents to the ComplexElement that featured by the given value.
     * 
     * @param root the tree node from which the search begin
     * @param value the name of the ComplexElement
     * @return
     */
    private static ATreeNode findComplexElement(ATreeNode root, String value) {
        Object[] os = root.getChildren();
        for (Object element : os) {
            if (((ATreeNode) element).getValue().equals(value)) {
                return (ATreeNode) element;
            }
        }
        return null;
    }

    /**
     * Populate a tree of ComplexTypes defined in an XSD file.
     * 
     * @param xsModel
     * @return the root node of the tree.
     * @throws OdaException
     */
    private static ATreeNode populateComplexTypeTree(XSModel xsModel) throws OdaException {
        XSNamedMap map = xsModel.getComponents(XSTypeDefinition.COMPLEX_TYPE);

        ATreeNode root = new ATreeNode();

        root.setValue("ROOT");
        root.setDataType("");
        Map<ATreeNode, XSParticle> childWithParticles = new HashMap<ATreeNode, XSParticle>();
        for (int i = 0; i < map.getLength(); i++) {
            ATreeNode node = new ATreeNode();
            XSComplexTypeDecl element = (XSComplexTypeDecl) map.item(i);
            if (element.getName().equals("anyType")) {
                continue;
            }
            node.setValue(element.getName());
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(element.getTypeName());
            root.addChild(node);

            if (includeAttribute) {
                XSAttributeGroupDecl group = element.getAttrGrp();
                if (group != null) {
                    XSObjectList list = group.getAttributeUses();
                    for (int j = 0; j < list.getLength(); j++) {
                        ATreeNode childNode = new ATreeNode();
                        childNode.setValue(((XSAttributeUseImpl) list.item(j)).getAttrDeclaration().getName());
                        childNode.setType(ATreeNode.ATTRIBUTE_TYPE);
                        // two different datatype definitions need two getting ways, added by nma.
                        String dataType = ((XSAttributeUseImpl) list.item(j)).fAttrDecl.getTypeDefinition().getName();
                        if (dataType == null || dataType.length() == 0) {
                            dataType = ((XSAttributeUseImpl) list.item(j)).fAttrDecl.getTypeDefinition().getBaseType().getName();
                        }
                        if (dataType != null && dataType.length() > 0) {
                            childNode.setDataType(dataType);
                        }
                        node.addChild(childNode);
                    }
                }
            }
            XSParticle particle = element.getParticle();
            if (particle != null) {
                childWithParticles.put(node, particle);
            }
        }

        for (ATreeNode node : childWithParticles.keySet()) {
            XSParticle particle = childWithParticles.get(node);
            if (particle != null) {
                XSObjectList list = ((XSModelGroupImpl) particle.getTerm()).getParticles();
                populateTreeNodeWithParticles(root, node, list);
            }
        }

        populateRoot(root);
        return root;
    }

    private static ATreeNode populateComplexTypeTree(XSModel xsModel, ATreeNode selectedNode) throws OdaException {
        XSNamedMap map = xsModel.getComponents(XSTypeDefinition.COMPLEX_TYPE);

        ATreeNode root = new ATreeNode();

        root.setValue("ROOT");
        root.setDataType("");
        Map<ATreeNode, XSParticle> childWithParticles = new HashMap<ATreeNode, XSParticle>();
        for (int i = 0; i < map.getLength(); i++) {
            ATreeNode node = new ATreeNode();
            XSComplexTypeDecl element = (XSComplexTypeDecl) map.item(i);
            if (element.getName().equals("anyType")) {
                continue;
            }
            node.setValue(element.getName());
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(element.getTypeName());
            root.addChild(node);

            if (includeAttribute) {
                XSAttributeGroupDecl group = element.getAttrGrp();
                if (group != null) {
                    XSObjectList list = group.getAttributeUses();
                    for (int j = 0; j < list.getLength(); j++) {
                        ATreeNode childNode = new ATreeNode();
                        childNode.setValue(((XSAttributeUseImpl) list.item(j)).getAttrDeclaration().getName());
                        childNode.setType(ATreeNode.ATTRIBUTE_TYPE);
                        // two different datatype definitions need two getting ways, added by nma.
                        String dataType = ((XSAttributeUseImpl) list.item(j)).fAttrDecl.getTypeDefinition().getName();
                        if (dataType == null || dataType.length() == 0) {
                            dataType = ((XSAttributeUseImpl) list.item(j)).fAttrDecl.getTypeDefinition().getBaseType().getName();
                        }
                        if (dataType != null && dataType.length() > 0) {
                            childNode.setDataType(dataType);
                        }
                        node.addChild(childNode);
                    }
                }
            }
            XSParticle particle = element.getParticle();
            if (particle != null) {
                childWithParticles.put(node, particle);
            }
        }

        for (ATreeNode node : childWithParticles.keySet()) {
            XSParticle particle = childWithParticles.get(node);
            if (particle != null) {
                XSObjectList list = ((XSModelGroupImpl) particle.getTerm()).getParticles();
                populateTreeNodeWithParticles(root, node, list);
            }
        }

        populateRoot(root);
        return root;
    }

    /**
     * 
     * @param node the node to which the elements defined in particles should be populated into
     * @param node2
     * @param list the XSObjectList which lists all particles.
     * @throws OdaException
     */
    private static void populateTreeNodeWithParticles(ATreeNode root, ATreeNode node, XSObjectList list) throws OdaException {
        for (int j = 0; j < list.getLength(); j++) {
            ATreeNode childNode = new ATreeNode();
            childNode.setValue(((XSParticleDecl) list.item(j)).getTerm().getName());

            if (((XSParticleDecl) list.item(j)).getTerm() instanceof XSElementDecl) {
                XSElementDecl element = ((XSElementDecl) ((XSParticleDecl) list.item(j)).getTerm());
                if (element.getTypeDefinition() != null && element.getTypeDefinition().getBaseType() != null) {
                    String dataType = element.getTypeDefinition().getBaseType().getName();
                    if (dataType.equals("anyType")) {
                        dataType = ((XSElementDecl) ((XSParticleDecl) list.item(j)).getTerm()).getTypeDefinition().getName();
                    }
                    if (dataType != null && dataType.length() > 0) {
                        childNode.setDataType(dataType);
                    }
                }

                childNode.setType(ATreeNode.ELEMENT_TYPE);
                node.addChild(childNode);

                // added by nrousseau to fix bug of partial loading of XSD file -- bug:9269
                String namespace = element.getNamespace();
                ATreeNode namespaceNode = null;
                if (namespace != null) {
                    namespaceNode = new ATreeNode();
                    namespaceNode.setDataType(namespace);
                    namespaceNode.setType(ATreeNode.NAMESPACE_TYPE);
                    namespaceNode.setValue(namespace);
                }

                if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
                    XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition();
                    // If the complex type is explicitly defined, that is, it has name.
                    if (complexType.getName() != null) {
                        childNode.setDataType(complexType.getName());
                        ATreeNode n = findComplexElement(childNode, complexType.getName());
                        if (n == null) {
                            n = findComplexElement(root, complexType.getName());
                        }
                        if (n != null) {
                            childNode.addChild(n.getChildren());
                        }
                    }
                    // If the complex type is implicitly defined, that is, it has no name.
                    else {
                        addParticleAndAttributeInfo(childNode, complexType, childNode, new VisitingRecorder());
                    }
                }
                // end of fix -- bug:9269
            }
            // If the particle itself is of XSModelGroupImpl type, which means that they have child types, then do
            // recursive job
            // until all the children are added to the node.
            else if (((XSParticleDecl) list.item(j)).getTerm() instanceof XSModelGroupImpl) {
                XSModelGroupImpl mGroup = (XSModelGroupImpl) ((XSParticleDecl) list.item(j)).getTerm();
                XSObjectList obs = mGroup.getParticles();
                populateTreeNodeWithParticles(root, node, obs);
            }
        }
    }
}

/**
 * Record the number of an element being visited.This is used to parse recursive elements.
 * 
 */
class VisitingRecorder {

    //
    private final Map map;

    /**
     * 
     * 
     */
    VisitingRecorder() {
        map = new HashMap();
    }

    /**
     * 
     * @param value
     * @return
     */
    public boolean visit(String value) {
        if (map.containsKey(value)) {
            int i = ((Integer) (map.get(value))).intValue();
            // If an element has been visited three times in a recursive call
            // assume it is a recursive element.
            if (i > 3) {
                return false;
            } else {
                map.put(value, new Integer(i + 1));
                return true;
            }
        } else {
            map.put(value, new Integer(0));
            return true;
        }
    }
}
