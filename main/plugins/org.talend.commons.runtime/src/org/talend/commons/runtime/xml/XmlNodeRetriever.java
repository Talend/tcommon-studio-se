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
package org.talend.commons.runtime.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.i18n.internal.Messages;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class XmlNodeRetriever {

    public static final String STRING_EMPTY = ""; //$NON-NLS-1$

    public static final String STRING_AT = "@"; //$NON-NLS-1$

    public static final String DEFAULT_PRE = "pre" + Math.random(); //$NON-NLS-1$

    private Document document;

    private XPath xpath;

    protected String currentLoopXPath;

    private NamespaceContext namespaceContext;

    private final Map<String, String> prefixToNamespace = new HashMap<String, String>();

    private XPathPrefixHandler prefixHandler;

    /**
     * DOC amaumont XMLNodeRetriever constructor comment.
     * 
     * @param string
     */
    public XmlNodeRetriever(String filePath, String loopXPath) {
        super();
        this.currentLoopXPath = loopXPath;
        initNamespaceContext();
        initSource(filePath);
    }

    /**
     * Default constructor.
     */
    public XmlNodeRetriever() {
        super();
    }

    /**
     * DOC amaumont Comment method "initNamespaceContext".
     */
    private void initNamespaceContext() {
        namespaceContext = new NamespaceContext() {

            @Override
            public String getNamespaceURI(String prefix) {
                String namespaceForPrefix = getNamespaceForPrefix(prefix);
                return namespaceForPrefix;
            }

            // Dummy implementation - not used!
            @Override
            public Iterator getPrefixes(String val) {
                return null;
            }

            // Dummy implemenation - not used!
            @Override
            public String getPrefix(String uri) {
                return null;
            }
        };

    }

    /**
     * DOC qzhang Comment method "getNamespaceForPrefix".
     * 
     * @param prefix
     * @return
     */
    protected String getNamespaceForPrefix(String prefix) {
        String namespace = prefixToNamespace.get(prefix);
        if (namespace != null) {
            return namespace;
        }
        return getDefaultNamespace();
    }

    /**
     * qzhang Comment method "getDefaultNamespace".
     * 
     * @return
     */
    private String getDefaultNamespace() {
        Node parent = document.getDocumentElement();
        int type = parent.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            NamedNodeMap nnm = parent.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node attr = nnm.item(i);
                String aname = attr.getNodeName();
                if (aname.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                    return attr.getNodeValue();
                }
            }
        }
        return XMLConstants.NULL_NS_URI;
    }

    /**
     * DOC amaumont Comment method "initSource".
     * 
     * @param filePath2
     */
    private synchronized void initSource(String filePath) {
        // Parse document containing schemas and validation roots
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        File file = new File(filePath);
        InputSource inputSource = new InputSource(file.toURI().toASCIIString());
        try {
            dbf.setNamespaceAware(true);
            db = dbf.newDocumentBuilder();
            document = db.parse(inputSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            inputSource.setEncoding("ISO-8859-1");
            if (db != null) {
                try {
                    document = db.parse(inputSource);
                } catch (SAXException e1) {
                    throw new RuntimeException(e);
                } catch (IOException e1) {
                    throw new RuntimeException(e);
                }
            }

        } finally {
            if (document != null) {
                prefixToNamespace.clear();
                initLastNodes(document.getDocumentElement());
                prefixToNamespace.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
                prefixHandler = new XPathPrefixHandler(document.getDocumentElement());
                // Create XPath factory for selecting schema and validation roots
                XPathFactory xpf = XPathFactory.newInstance();
                xpath = xpf.newXPath();
                // xpath.setNamespaceContext(namespaceContext);
                xpath.setNamespaceContext(prefixHandler.getNamespaceContext());
            }
        }
    }

    /**
     * DOC qzhang Comment method "initLastNodes".
     * 
     * @param node
     */
    private void initLastNodes(Node node) {
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            setPrefixToNamespace(node);
        }
        for (int i = 0; i < length; i++) {
            Node item = childNodes.item(i);
            if (item.getChildNodes().getLength() > 0) {
                initLastNodes(item);
            }
        }
    }

    /**
     * DOC qzhang Comment method "setPrefixToNamespace".
     * 
     * @param node
     */
    private void setPrefixToNamespace(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attr = nnm.item(i);
            String aname = attr.getNodeName();
            boolean isPrefix = aname.startsWith(XMLConstants.XMLNS_ATTRIBUTE + ":"); //$NON-NLS-1$
            if (isPrefix || aname.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                int index = aname.indexOf(':');
                String p = isPrefix ? aname.substring(index + 1) : XMLConstants.NULL_NS_URI;
                prefixToNamespace.put(p, attr.getNodeValue());
            }
        }
    }

    private String addDefaultNS(String xPathExpression) {
        if (XMLConstants.NULL_NS_URI.equals(getDefaultNamespace())) {
            return xPathExpression;
        } else {
            StringBuilder expr = new StringBuilder();
            String[] split = xPathExpression.split("/"); //$NON-NLS-1$
            for (String string : split) {
                if (!string.equals("") && string.indexOf(':') == -1 && string.indexOf('.') == -1) { //$NON-NLS-1$
                    expr.append(DEFAULT_PRE + ":"); //$NON-NLS-1$
                }
                expr.append(string + "/"); //$NON-NLS-1$
            }
            if (split.length > 0) {
                expr.deleteCharAt(expr.length() - 1);
            }
            return expr.toString();
        }
    }

    /**
     * 
     * DOC amaumont Comment method "retrieveListOfNodes".
     * 
     * @param xPathExpression
     * @return always a <code>List</code> empty or not
     * @throws XPathExpressionException
     */
    public List<Node> retrieveListOfNodes(String xPathExpression) throws XPathExpressionException {

        return retrieveNodeList(xPathExpression);
    }

    /**
     * DOC amaumont Comment method "retrieveNodeList".
     * 
     * @param xPathExpression
     * @param nodeList
     * @return <code>NodeList</code> or null if expression is invalid
     * @throws XPathExpressionException
     */
    public synchronized List<Node> retrieveNodeList(String xPathExpression) throws XPathExpressionException {

        xPathExpression = simplifyXPathExpression(xPathExpression);
        // xPathExpression = addDefaultNS(xPathExpression);
        xPathExpression = prefixHandler.addXPathPrefix(xPathExpression);

        List<Node> nodeList = new ArrayList<Node>();
        NodeList nodeListItem = null;
        // System.out.println("xPathExpression = "+xPathExpression);
        XPathExpression xpathSchema = xpath.compile(xPathExpression);

        nodeListItem = (NodeList) xpathSchema.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodeListItem.getLength(); i++) {
            nodeList.add(nodeListItem.item(i));
        }

        return nodeList;
    }

    /**
     * DOC amaumont Comment method "simplifyXPathExpression".
     * 
     * @param pathExpression
     * @return
     */
    private static String simplifyXPathExpression(String xpathExpression) {

        Perl5Matcher matcher = new Perl5Matcher();

        Perl5Compiler compiler = new Perl5Compiler();

        Pattern pattern = null;
        try {
            pattern = compiler.compile("(.*)/\\s*\\w+\\s*(/(\\.\\.|parent))(.*)"); //$NON-NLS-1$
        } catch (MalformedPatternException e) {
            CommonExceptionHandler.process(e);
        }

        Perl5Substitution substitution = new Perl5Substitution("$1$4", Perl5Substitution.INTERPOLATE_ALL); //$NON-NLS-1$

        int lengthOfPreviousXPath = 0;

        do {
            lengthOfPreviousXPath = xpathExpression.length();
            if (matcher.matches(xpathExpression, pattern)) {
                xpathExpression = Util.substitute(matcher, pattern, substitution, xpathExpression, Util.SUBSTITUTE_ALL);
            }
        } while (xpathExpression.length() != lengthOfPreviousXPath);

        return xpathExpression;
    }

    /**
     * DOC amaumont Comment method "retrieveNodeList".
     * 
     * @param xPathExpression
     * @param nodeList
     * @return <code>NodeList</code> or null if expression is invalid
     * @throws XPathExpressionException
     */
    public synchronized List<Node> retrieveNodeListFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        relativeXPathExpression = simplifyXPathExpression(relativeXPathExpression);
        // relativeXPathExpression = addDefaultNS(relativeXPathExpression);
        relativeXPathExpression = prefixHandler.addXPathPrefix(relativeXPathExpression, referenceNode);
        if (relativeXPathExpression == null) {
            return null;
        }
        List<Node> nodeList = new ArrayList<Node>();

        NodeList nodeListItem = (NodeList) xpath.evaluate(relativeXPathExpression, referenceNode, XPathConstants.NODESET);

        for (int i = 0; i < nodeListItem.getLength(); i++) {
            nodeList.add(nodeListItem.item(i));
        }
        return nodeList;
    }

    /**
     * DOC amaumont Comment method "retrieveNodeList".
     * 
     * @param xPathExpression
     * @param nodeList
     * @return <code>NodeList</code> or null if expression is invalid
     * @throws XPathExpressionException
     */
    public synchronized Node retrieveNode(String xPathExpression) throws XPathExpressionException {
        xPathExpression = simplifyXPathExpression(xPathExpression);
        XPathExpression xpathSchema = xpath.compile(xPathExpression);
        Node node = (Node) xpathSchema.evaluate(document, XPathConstants.NODE);
        return node;
    }

    /**
     * DOC amaumont Comment method "retrieveNodeList".
     * 
     * @param relativeXPathExpression
     * @param nodeList
     * @return <code>NodeList</code> or null if expression is invalid
     * @throws XPathExpressionException
     */
    public synchronized Node retrieveNodeFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        relativeXPathExpression = simplifyXPathExpression(relativeXPathExpression);
        Node node = (Node) xpath.evaluate(relativeXPathExpression, referenceNode, XPathConstants.NODE);
        return node;
    }

    /**
     * DOC amaumont Comment method "retrieveNodeList".
     * 
     * @param xPathExpression
     * @param nodeList
     * @return <code>NodeList</code> or null if expression is invalid
     * @throws XPathExpressionException
     */
    public synchronized Double retrieveNodeCount(String xPathExpression) throws XPathExpressionException {
        XPathExpression xpathSchema = xpath.compile(xPathExpression);
        Object countNode = xpathSchema.evaluate(document, XPathConstants.NUMBER);
        return (Double) countNode;
    }

    public static void main(String[] args) throws XPathExpressionException {

        String string = simplifyXPathExpression("/doc/members/member/.."); //$NON-NLS-1$

        System.out.println(string);

        if (true) {
            return;
        }

        String filePath = "C:/test_xml/test.xml"; //$NON-NLS-1$

        XmlNodeRetriever pathRetriever = new XmlNodeRetriever(filePath, ""); //$NON-NLS-1$

        String currentExpr = "child::node()"; //$NON-NLS-1$
        // String currentExpr = "/doc/members";

        String xPathExpression;
        String slash = "/"; //$NON-NLS-1$
        if (currentExpr.endsWith("/")) { //$NON-NLS-1$
            slash = ""; //$NON-NLS-1$
        }

        xPathExpression = currentExpr + slash + ""; //$NON-NLS-1$
        // xPathExpression = currentExpr + slash + "*" + " | " + currentExpr + slash + "@*";

        // 7689 nodes
        xPathExpression = "/doc/members/../members/member/child::*"; //$NON-NLS-1$
        xPathExpression = "/doc/members/../members/member/*"; //$NON-NLS-1$

        // 17925 nodes + text
        xPathExpression = "/doc/members/../members/member/child::node()"; //$NON-NLS-1$

        // 2547 attributes
        xPathExpression = "/doc/members/../members/member/@*"; //$NON-NLS-1$

        xPathExpression = "/doc/members/../members/member/*"; //$NON-NLS-1$
        xPathExpression = "/doc/members/member/../"; //$NON-NLS-1$

        String mainXPathNode = "/doc/members/member"; //$NON-NLS-1$
        String field1 = ".."; //$NON-NLS-1$
        String field2 = "summary/"; //$NON-NLS-1$

        System.out.println(Messages.getString("XmlNodeRetriever.mainExpression") + mainXPathNode); //$NON-NLS-1$
        Node mainNode = pathRetriever.retrieveNode(mainXPathNode);
        if (mainNode != null) {
            System.out.println(Messages.getString("XmlNodeRetriever.mainNode") + mainNode.getNodeName()); //$NON-NLS-1$
        }

        Node field1Node = pathRetriever.retrieveNodeFromNode(field1, mainNode);
        if (field1Node != null) {
            System.out.println(Messages.getString("XmlNodeRetriever.filed1Node") + field1Node.getNodeName()); //$NON-NLS-1$
        }

        Node field2Node = pathRetriever.retrieveNodeFromNode(field2, mainNode);
        if (field2Node != null) {
            System.out.println(Messages.getString("XmlNodeRetriever.field2Node") + field2Node.getNodeName()); //$NON-NLS-1$
        }

        String proposal1 = "../*"; //$NON-NLS-1$
        String proposal2 = "./member/see/*"; //$NON-NLS-1$
        proposal2 = "member/* | member/@*"; //$NON-NLS-1$
        List<Node> proposal1Nodes = pathRetriever.retrieveNodeListFromNode(proposal1, mainNode);
        if (field1Node != null) {
            int length = proposal1Nodes.size();
            System.out.println(Messages.getString("XmlNodeRetriever.prposal1Node") + length); //$NON-NLS-1$
            // int lstSize = length;
            // for (int i = 0; i < lstSize; i++) {
            // System.out.println(proposal1Nodes.item(i));
            // }
        }

        List<Node> proposal2Nodes = pathRetriever.retrieveNodeListFromNode(proposal2, mainNode);
        if (proposal2Nodes != null) {
            int length = proposal2Nodes.size();
            System.out.println(Messages.getString("XmlNodeRetriever.proposal2Node") + length); //$NON-NLS-1$
            // int lstSize = length;
            // for (int i = 0; i < lstSize; i++) {
            // System.out.println(proposal2Nodes.item(i));
            // }
        }

        // if (true) {
        // return;
        // }

        // String countXPathExpression = null;
        // countXPathExpression = xPathExpression + "[count(*)]";
        // countXPathExpression = "count(" + xPathExpression + ")";
        // countXPathExpression = xPathExpression + "self::count()";
        // System.out.println("countXPathExpression = " + countXPathExpression);
        // TimeMeasure.start("count");
        // Double count = pathRetriever.retrieveNodeCount(countXPathExpression);
        // TimeMeasure.end("count");
        //
        // System.out.println("count=" + count);
        //
        // TimeMeasure.start("nodeList");
        // NodeList nodeList2 = pathRetriever.retrieveNodeList(xPathExpression);
        // System.out.println("Count result : " + nodeList2.getLength());
        // TimeMeasure.end("nodeList");
        //
        if (true) {
            return;
        }

        System.out
                .println(Messages.getString("XmlNodeRetriever.xPathExpression") + xPathExpression + Messages.getString("XmlNodeRetriever.singleQuotes")); //$NON-NLS-1$ //$NON-NLS-2$
        List<Node> nodeList = pathRetriever.retrieveListOfNodes(xPathExpression);

        System.out.println(Messages.getString("XmlNodeRetriever.countResult") + nodeList.size()); //$NON-NLS-1$

        int lstSize = nodeList.size();
        int limit = 100;
        for (int i = 0; i < lstSize; i++) {
            if (i > limit) {
                break;
            }
            Node node = nodeList.get(i);
            System.out.println(node.getNodeName());

        }

        // for (Node node : nodeList) {
        // // System.out.println(node.getNodeName() + ":" + node.getFirstChild().getNodeValue());
        // System.out.println(node.getNodeName());
        // }

    }

    /**
     * DOC amaumont Comment method "getAbsoluteXPathFromNode".
     * 
     * @param node
     */
    public String getAbsoluteXPathFromNode(Node node) {
        return getAbsoluteXPathFromNode(node, ""); //$NON-NLS-1$
    }

    /**
     * DOC amaumont Comment method "getAbsoluteXPathFromNode".
     * 
     * 
     * @param node
     * @param string
     */
    private String getAbsoluteXPathFromNode(Node node, String currentXPath) {
        Node parentNode = null;
        String at = ""; //$NON-NLS-1$

        String elementName = node.getNodeName();
        if (node.getNodeName().equals("xs:element")) { //$NON-NLS-1$
            elementName = null;
            if (node.getAttributes().getLength() > 0) {
                Node nameAttribute = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
                if (nameAttribute != null) {
                    elementName = nameAttribute.getNodeValue();
                    at = STRING_EMPTY;
                }
            }
        } else if (node.getNodeName().equals("xs:attribute")) { //$NON-NLS-1$
            elementName = null;
            if (node.getAttributes().getLength() > 0) {
                Node nameAttribute = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
                if (nameAttribute != null) {
                    elementName = nameAttribute.getNodeValue();
                    at = STRING_AT;
                }
            }
        }
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            parentNode = ((Attr) node).getOwnerElement();
            at = STRING_AT;
        } else {
            parentNode = node.getParentNode();
            at = STRING_EMPTY;
        }
        if (elementName != null) {
            currentXPath = "/" + at + elementName + currentXPath; //$NON-NLS-1$
        }
        if (parentNode == node.getOwnerDocument()) {
            return currentXPath;
        } else {
            return getAbsoluteXPathFromNode(parentNode, currentXPath);
        }
    }

    /**
     * Getter for document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * DOC amaumont Comment method "setCurrentLoopXPath".
     * 
     * @param newValue
     */
    public void setCurrentLoopXPath(String currentLoopXPath) {
        this.currentLoopXPath = currentLoopXPath;
    }

    /**
     * Getter for currentLoopXPath.
     * 
     * @return the currentLoopXPath
     */
    public String getCurrentLoopXPath() {
        return this.currentLoopXPath;
    }

    /**
     * DOC amaumont Comment method "dispose".
     */
    public void dispose() {
        if (document != null) {
            // document.re
        }
    }
}
