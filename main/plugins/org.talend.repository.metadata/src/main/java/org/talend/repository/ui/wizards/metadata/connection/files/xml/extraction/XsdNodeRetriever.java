// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.runtime.xml.XmlNodeRetriever;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.TreePopulator;
import org.w3c.dom.Node;

public class XsdNodeRetriever extends XmlNodeRetriever {

    private TreePopulator treePopulator;

    public XsdNodeRetriever(String filePath, String loopXPath) {
        super(filePath, loopXPath);
    }

    public TreePopulator getTreePopulator() {
        return this.treePopulator;
    }

    public void setTreePopulator(TreePopulator treePopulator) {
        this.treePopulator = treePopulator;
    }

    @Override
    public synchronized Node retrieveNode(String pathExpression) throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNode(pathExpression);
    }

    @Override
    public synchronized Double retrieveNodeCount(String pathExpression) throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeCount(pathExpression);
    }

    @Override
    public synchronized Node retrieveNodeFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeFromNode(relativeXPathExpression, referenceNode);
    }

    @Override
    public synchronized List<Node> retrieveNodeList(String pathExpression) throws XPathExpressionException {
        TreeItem item = treePopulator.getTreeItem(pathExpression);
        if (item != null) {
            List<Node> nodeList = new ArrayList<Node>();
            ATreeNode aTreeNode = (ATreeNode) item.getData();
            nodeList.add((Node) aTreeNode.getValue());
            return nodeList;
        }
        return super.retrieveNodeList(pathExpression);
    }

    @Override
    public synchronized List<Node> retrieveNodeListFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeListFromNode(relativeXPathExpression, referenceNode);
    }

    @Override
    public String getAbsoluteXPathFromNode(Node node) {
        // TODO Auto-generated method stub
        return super.getAbsoluteXPathFromNode(node);
    }
}
