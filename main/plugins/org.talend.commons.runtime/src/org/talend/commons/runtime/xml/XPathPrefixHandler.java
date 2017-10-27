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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class XPathPrefixHandler {

    private Map<String, List<NodeInfo>> nameNodesMap;

    private Map<String, NodeInfo> pathNodesMap;

    private Map<String, String> dataTypeFromXSD;

    private CustomNamespaceContext namespaceContext;

    private boolean isXSDFile;

    public XPathPrefixHandler(Element root) {
        this.isXSDFile = false;
        nameNodesMap = new HashMap<String, List<NodeInfo>>();
        pathNodesMap = new HashMap<String, NodeInfo>();
        dataTypeFromXSD = new HashMap<String, String>();
        namespaceContext = new CustomNamespaceContext();
        collectNodes(root, "", 0); //$NON-NLS-1$
        sortByLevel(nameNodesMap);

        if (isXSDFile) {
            replaceReferences();
        }
    }

    /**
     * DOC chuang Comment method "sortByLevel".
     * 
     * @param nameNodesMap2
     */
    private void sortByLevel(Map<String, List<NodeInfo>> map) {
        for (List<NodeInfo> list : map.values()) {
            Collections.sort(list, new Comparator<NodeInfo>() {

                public int compare(NodeInfo o1, NodeInfo o2) {
                    return o1.level - o2.level;
                }
            });
        }

    }

    public CustomNamespaceContext getNamespaceContext() {
        return namespaceContext;
    }

    private String getQualifiedName(String prefix, String name) {
        StringBuilder buf = new StringBuilder();
        if (name.indexOf("@") < 0) { //$NON-NLS-1$
            if (StringUtils.isEmpty(prefix)) {
                // no namespace
                buf.append(name);
            } else {
                buf.append(prefix).append(":").append(name); //$NON-NLS-1$
            }
        } else {
            // buf.append("@").append(prefix).append(":").append(name.substring(1));
            // attribute does not need to add prefix
            buf.append(name);
        }
        return buf.toString();
    }

    /**
     * DOC chuang Comment method "addXPathPrefix".
     * 
     * @param relativeXPathExpression
     * @param referenceNode
     * @return
     */
    public String addXPathPrefix(String relativeXPathExpression, Node node) {
        if (namespaceContext.getNamespaceCount() == 0) {
            return relativeXPathExpression;
        }
        if (relativeXPathExpression.startsWith("/")) { //$NON-NLS-1$
            // absolute path
            return addXPathPrefix(relativeXPathExpression);
        }

        List<NodeInfo> list = nameNodesMap.get(node.getNodeName());
        if (list == null) {
            return null;
        }
        NodeInfo info = getNodeInfo(node, list);
        String path = getQualifiedXPath(info.path);
        String expr = info.path + "/" + relativeXPathExpression; //$NON-NLS-1$
        String result = addXPathPrefix(expr);
        return extractRelativePath(result, relativeXPathExpression);
        // return result.substring(path.length() + 1);
    }

    /**
     * DOC chuang Comment method "extractRelativePath".
     * 
     * @param result
     * @param relativeXPathExpression
     * @return
     */
    private String extractRelativePath(String absolutePath, String relativePath) {
        String[] aPath = absolutePath.split("/"); //$NON-NLS-1$
        String[] rPath = relativePath.split("/"); //$NON-NLS-1$
        String[] path = new String[rPath.length];
        int i = aPath.length - rPath.length;
        for (int j = 0; j < rPath.length; j++, i++) {
            path[j] = aPath[i];
        }
        return StringUtils.join(path, "/"); //$NON-NLS-1$
    }

    /**
     * DOC chuang Comment method "getNodeInfo".
     * 
     * @param node
     * @param list
     */
    private NodeInfo getNodeInfo(Node node, List<NodeInfo> list) {
        NodeInfo info = null;

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).namespace.equals(node.getNamespaceURI())) {
                info = list.get(i);
                break;
            } else if (StringUtils.isEmpty(list.get(i).namespace) && StringUtils.isEmpty(node.getNamespaceURI())) {
                info = list.get(i);
                break;
            }
        }
        return info;
    }

    public NodeInfo getNodeInfo(String path) {
        return pathNodesMap.get(path);
    }

    public String getDataTypeFromXSD(String path) {
        return dataTypeFromXSD.get(path);
    }

    private String getQualifiedXPath(String path) {
        if (path.equals("/") || path.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            return ""; //$NON-NLS-1$
        }
        int pos = path.lastIndexOf("/"); //$NON-NLS-1$
        String pre = getQualifiedXPath(path.substring(0, pos));
        String namespace = pathNodesMap.get(path).namespace;
        return pre + "/" + getQualifiedName(namespaceContext.getPrefix(namespace), path.substring(pos + 1)); //$NON-NLS-1$
    }

    public String addXPathPrefix(String xPathExpression) {
        if (namespaceContext.getNamespaceCount() == 0) {
            return xPathExpression;
        }

        String[] names = xPathExpression.split("/"); //$NON-NLS-1$
        PathSegment[] paths = createPathSegments(names);

        int totalFixed = resolvePath(paths);
        while (totalFixed < names.length) {
            int fixed = 0;
            for (int i = 0; i < paths.length; i++) {
                if (!paths[i].resolved) {
                    // several node with same name
                    List<NodeInfo> nodes = nameNodesMap.get(paths[i].originalPath);
                    if (nodes != null)
                        for (NodeInfo node : nodes) {
                            if (validatePath(paths, i, node)) {
                                fixed++;
                                totalFixed++;
                                paths[i].resolved = true;
                                paths[i].info = node;
                                paths[i].transformPath = getQualifiedName(namespaceContext.getPrefix(node.namespace),
                                        paths[i].originalPath);
                                break;
                            }
                        }
                }
            }
            if (fixed == 0) {
                // avoid dead loop
                break;
            }
        }

        if (totalFixed < names.length) {
            // try to fix some unknown segment
            fixUnknownPath(paths);
        }

        // convert to xpath string
        StringBuilder expr = new StringBuilder();
        for (PathSegment p : paths) {
            expr.append(p.transformPath + "/"); //$NON-NLS-1$
        }
        if (names.length > 0) {
            expr.deleteCharAt(expr.length() - 1);
        }
        return expr.toString();
    }

    /**
     * DOC chuang Comment method "fixXPath".
     * 
     * @param paths
     */
    private void fixUnknownPath(PathSegment[] paths) {
        for (int i = 0; i < paths.length; i++) {
            PathSegment seg = paths[i];
            if (seg.resolved == true) {
                continue;
            }
            List<NodeInfo> nodes = nameNodesMap.get(paths[i].originalPath);
            if (nodes != null) {
                paths[i].transformPath = namespaceContext.getPrefix(nodes.get(0).namespace) + ":" + paths[i].originalPath; //$NON-NLS-1$
                paths[i].resolved = true;
            }
        }

    }

    /**
     * DOC chuang Comment method "validate".
     * 
     * @param paths
     * @param pos
     * @param node
     * @return
     */
    private boolean validatePath(PathSegment[] paths, int pos, NodeInfo node) {
        int level = 0;
        for (int i = pos + 1; i < paths.length; i++) {
            PathSegment seg = paths[i];
            if (seg.originalPath.equals("..")) { //$NON-NLS-1$
                // get to parent node
                level--;
            } else if (!seg.originalPath.equals(".")) { //$NON-NLS-1$
                // get to child node
                level++;
            }

            if (!seg.resolved || seg.info == null || level < 1) {
                continue;
            }
            // check xpath of two node
            if (seg.info.level < node.level || seg.info.path.indexOf(node.path) < 0) {
                return false;
            }
        }

        level = 0;
        for (int i = pos - 1; i >= 0; i--) {
            PathSegment seg = paths[i];
            if (seg.originalPath.equals("..")) { //$NON-NLS-1$
                // get to parent node
                level--;
            } else if (!seg.originalPath.equals(".")) { //$NON-NLS-1$
                // get to child node
                if (level < 0) {
                    level++;
                    continue;
                }
            }
            if (!seg.resolved || seg.info == null) {
                continue;
            }
            // check xpath of two node
            if (node.level < seg.info.level || node.path.indexOf(seg.info.path) < 0) {
                return false;
            }

        }

        return true;
    }

    /**
     * DOC chuang Comment method "resolve".
     * 
     * @param path
     */
    private int resolvePath(PathSegment[] path) {
        int fixed = 0;
        for (PathSegment p : path) {
            List<NodeInfo> list = nameNodesMap.get(p.originalPath);
            if (p.originalPath.indexOf(":") > -1) { //$NON-NLS-1$
                p.resolved = true;
                p.transformPath = p.originalPath;
                /* handle NPE for 0020293 */
                if (list != null) {
                    p.info = list.get(0);
                } else
                    p.info = null;
                fixed++;
            } else if (p.originalPath.indexOf(".") > -1 || p.originalPath.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                p.resolved = true;
                p.transformPath = p.originalPath;
                p.info = null;
                fixed++;
            } else {
                List<NodeInfo> nodes = list;
                // has only one node
                if (nodes != null && nodes.size() == 1) {
                    p.resolved = true;
                    p.info = nodes.get(0);
                    p.transformPath = getQualifiedName(namespaceContext.getPrefix(p.info.namespace), p.originalPath);
                    fixed++;
                }
            }
        }
        return fixed;
    }

    /**
     * DOC chuang Comment method "createPathSegments".
     * 
     * @param names
     * @return
     */
    private PathSegment[] createPathSegments(String[] names) {
        PathSegment[] path = new PathSegment[names.length];
        for (int i = 0; i < names.length; i++) {
            path[i] = new PathSegment(names[i]);
        }
        return path;
    }

    /**
     * 
     * DOC chuang XPathPrefixHandler class global comment. Detailled comment
     */
    static class PathSegment {

        String originalPath;

        String transformPath;

        boolean resolved;

        NodeInfo info;

        PathSegment(String path) {
            originalPath = path;
            transformPath = null;
            resolved = false;
            info = null;
        }

    }

    /**
     * DOC chuang Comment method "getPrefix".
     * 
     * @param name
     * @return
     */
    private String getPrefix(String name) {
        List<NodeInfo> nodes = nameNodesMap.get(name);
        NodeInfo info = nodes.get(0);
        return namespaceContext.getPrefix(info.namespace);
    }

    /**
     * DOC chuang Comment method "collectNodes".
     * 
     * @param root2
     * @param string
     */
    private void collectNodes(Node node, String path, int level) {

        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            String elementName = node.getNodeName();

            if (isXSDFile) {
                elementName = null;
                if (node.getNodeName().equals("xs:element")) { //$NON-NLS-1$
                    if (node.getAttributes().getLength() > 0) {
                        Node nameAttribute = node.getAttributes().getNamedItem("name"); //$NON-NLS-1$
                        if (nameAttribute != null) {
                            elementName = nameAttribute.getNodeValue();
                        }
                        Node refAttribute = node.getAttributes().getNamedItem("ref"); //$NON-NLS-1$
                        if (refAttribute != null) {
                            elementName = "#" + refAttribute.getNodeValue(); // add a mark to set as ref element. //$NON-NLS-1$
                        }
                    }
                }
            }

            String currentPath = null;
            if (elementName != null) {
                currentPath = new StringBuilder(path).append("/").append(elementName).toString(); //$NON-NLS-1$
                // if (pathNodesMap.get(currentPath) != null) {
                // return;
                // }
            }
            if (currentPath == null) {
                currentPath = path;
            }
            NodeInfo info = new NodeInfo(node, currentPath);
            info.level = level;
            computeNamespace(info, path);
            // System.out.println(node.getNodeName() + " " + info.path + " " + info.namespace + "\n");
            if (elementName != null) {
                if (pathNodesMap.get(currentPath) == null) {
                    pathNodesMap.put(currentPath, info);
                }
                if (isXSDFile) {
                    elementName = null;
                    if (node.getNodeName().equals("xs:element")) { //$NON-NLS-1$
                        if (node.getAttributes().getLength() > 0) {
                            Node nameAttribute = node.getAttributes().getNamedItem("type"); //$NON-NLS-1$
                            if (nameAttribute != null) {
                                dataTypeFromXSD.put(currentPath, nameAttribute.getNodeValue());
                            }
                        }
                    }
                }
                addNodeInfoToMap(elementName, info);
            }

            // visit child nodes
            NodeList childNodes = node.getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = childNodes.item(i);
                collectNodes(item, currentPath, level + 1);
            }
        }
    }

    /**
     * DOC nrousseau Comment method "replaceReferences".
     */
    private void replaceReferences() {
        Map<String, NodeInfo> newPathNodesMap = new HashMap<String, NodeInfo>();

        for (String path : pathNodesMap.keySet()) {
            if (path.contains("#")) { //$NON-NLS-1$
                replaceReferences(path, newPathNodesMap);
            }
        }

        pathNodesMap.putAll(newPathNodesMap);
    }

    private void replaceReferences(String path, Map<String, NodeInfo> newPathNodesMap) {
        String newPath = path.substring(0, path.indexOf("#") - 1); //$NON-NLS-1$
        String objectToReplace = "/" + path.substring(path.indexOf("#") + 1); //$NON-NLS-1$ //$NON-NLS-2$
        for (String refPath : pathNodesMap.keySet()) {
            if (refPath.equals(objectToReplace) || refPath.startsWith(objectToReplace + "/")) { //$NON-NLS-1$
                String newFullPath = newPath + refPath;

                if (!newFullPath.contains("#")) { //$NON-NLS-1$
                    if (dataTypeFromXSD.containsKey(refPath)) {
                        dataTypeFromXSD.put(newFullPath, dataTypeFromXSD.get(refPath));
                    }
                    newPathNodesMap.put(newFullPath, pathNodesMap.get(refPath));
                } else {
                    replaceReferences(newFullPath, newPathNodesMap);
                }
            }
        }
    }

    /**
     * DOC chuang Comment method "addInfoToMap".
     * 
     * @param node
     * @param info
     */
    private void addNodeInfoToMap(String name, NodeInfo info) {
        List<NodeInfo> nodeInfos = nameNodesMap.get(name);
        if (nodeInfos == null) {
            nodeInfos = new ArrayList<NodeInfo>();
            nameNodesMap.put(name, nodeInfos);
        }
        nodeInfos.add(info);
    }

    /**
     * DOC chuang Comment method "getNamespace".
     * 
     * @param node
     * @param parentPath
     * @return
     */
    private void computeNamespace(NodeInfo info, String parentPath) {
        Node node = info.node;
        List<Node> attributes = new ArrayList<Node>();
        // return node.getNamespaceURI();
        String defaultNamespace = null;
        if (isXSDFile) {
            if (node.getNodeName().equals("xs:attribute")) { //$NON-NLS-1$
                attributes.add(node);
            }
        }
        NamedNodeMap nodeMap = node.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node attr = nodeMap.item(i);
            String attrName = attr.getNodeName();
            boolean isPrefix = attrName.startsWith(XMLConstants.XMLNS_ATTRIBUTE) && (!isXSDFile || !attrName.equals("xmlns:xs")); //$NON-NLS-1$
            if (isPrefix) {
                if (attrName.length() == XMLConstants.XMLNS_ATTRIBUTE.length()) {
                    defaultNamespace = attr.getNodeValue();
                    namespaceContext.addNamespaceURI(XMLConstants.NULL_NS_URI, attr.getNodeValue());
                } else {
                    int index = attrName.indexOf(':');
                    String prefix = attrName.substring(index + 1);
                    namespaceContext.addNamespaceURI(prefix, attr.getNodeValue());
                }
            } else if (!isXSDFile) {
                attributes.add(attr);
            }
        }

        if (defaultNamespace != null) {
            info.defaultNamespace = defaultNamespace;
        } else {
            // same as parent
            if (parentPath.equals("")) { //$NON-NLS-1$
                // no namespace
                info.defaultNamespace = ""; //$NON-NLS-1$
            } else {
                info.defaultNamespace = pathNodesMap.get(parentPath).defaultNamespace;
            }
        }

        String part[] = node.getNodeName().split(":"); //$NON-NLS-1$
        if (part.length > 1) {
            info.namespace = namespaceContext.getNamespaceURI(part[0]);
        } else {
            info.namespace = info.defaultNamespace;
        }

        collectAttributes(info, attributes);
    }

    /**
     * DOC chuang Comment method "collectAttributes".
     * 
     * @param info
     * @param attributes
     */
    private void collectAttributes(NodeInfo info, List<Node> attributes) {
        for (Node attr : attributes) {
            String elementName = attr.getNodeName();
            if (isXSDFile) {
                if (attr.getAttributes().getLength() > 0) {
                    Node nameAttribute = attr.getAttributes().getNamedItem("name"); //$NON-NLS-1$
                    if (nameAttribute != null) {
                        elementName = nameAttribute.getNodeValue();
                    }
                }
            }
            String currentPath = new StringBuilder(info.path).append("/@").append(elementName).toString(); //$NON-NLS-1$
            if (pathNodesMap.get(currentPath) != null) {
                continue;
            }

            if (isXSDFile) {
                if (attr.getAttributes().getLength() > 0) {
                    Node nameAttribute = attr.getAttributes().getNamedItem("type"); //$NON-NLS-1$
                    if (nameAttribute != null) {
                        dataTypeFromXSD.put(currentPath, nameAttribute.getNodeValue());
                    }
                }
            }
            NodeInfo attrInfo = new NodeInfo(attr, currentPath);
            attrInfo.level = info.level + 1;
            attrInfo.defaultNamespace = info.defaultNamespace;
            int pos = elementName.indexOf(':');
            if (pos > -1) {
                attrInfo.namespace = namespaceContext.getNamespaceURI(elementName.substring(0, pos));
            } else {
                attrInfo.namespace = info.defaultNamespace;
            }
            pathNodesMap.put(currentPath, attrInfo);
            addNodeInfoToMap("@" + elementName, attrInfo); //$NON-NLS-1$
        }
    }

    /**
     * 
     * DOC chuang XPathPrefixHandler class global comment. Detailled comment
     */
    static class NodeInfo {

        Node node;

        String path;

        int level;

        String namespace;

        String defaultNamespace;

        NodeInfo(Node node, String path) {
            super();
            this.node = node;
            this.path = path;
        }

    };

    /**
     * 
     * DOC chuang XPathPrefixHandler class global comment. Detailled comment
     */
    static class CustomNamespaceContext implements NamespaceContext {

        private BidiMap prefixToNamespace = new DualHashBidiMap();

        private static final String DEFAULT_PREFIX = "default_ns_"; //$NON-NLS-1$

        private int defaultNamespaceCount = 0;

        private int namespaceCount = 0;

        public int getNamespaceCount() {
            return namespaceCount;
        }

        public void addNamespaceURI(String prefix, String namespace) {
            if (prefix == null || namespace == null) {
                return;
            }
            namespaceCount++;
            if (prefix.equals("")) { //$NON-NLS-1$
                // default namespace
                defaultNamespaceCount++;
                prefixToNamespace.put(DEFAULT_PREFIX + defaultNamespaceCount, namespace);
            } else {
                prefixToNamespace.put(prefix, namespace);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
         */
        public String getNamespaceURI(String prefix) {
            return (String) prefixToNamespace.get(prefix);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
         */
        public String getPrefix(String namespaceURI) {
            if (namespaceURI == null || "".equals(namespaceURI)) {
                return ""; //$NON-NLS-1$
            }
            return (String) prefixToNamespace.getKey(namespaceURI);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
         */
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    }

}
