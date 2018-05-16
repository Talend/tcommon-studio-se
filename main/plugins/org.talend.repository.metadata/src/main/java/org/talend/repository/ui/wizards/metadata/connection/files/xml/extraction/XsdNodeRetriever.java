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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.runtime.xml.XmlNodeRetriever;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.TreePopulator;
import org.w3c.dom.Node;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class XsdNodeRetriever extends XmlNodeRetriever {

    private TreePopulator treePopulator;

    /**
     * DOC nrousseau XsdNodeRetriever constructor comment.
     * 
     * @param filePath
     * @param loopXPath
     */
    public XsdNodeRetriever(String filePath, String loopXPath) {
        super(filePath, loopXPath);
    }

    /**
     * Getter for treePopulator.
     * 
     * @return the treePopulator
     */
    public TreePopulator getTreePopulator() {
        return this.treePopulator;
    }

    /**
     * Sets the treePopulator.
     * 
     * @param treePopulator the treePopulator to set
     */
    public void setTreePopulator(TreePopulator treePopulator) {
        this.treePopulator = treePopulator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#retrieveNode(java.lang.String)
     */
    @Override
    public synchronized Node retrieveNode(String pathExpression) throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNode(pathExpression);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#retrieveNodeCount(java.lang.String)
     */
    @Override
    public synchronized Double retrieveNodeCount(String pathExpression) throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeCount(pathExpression);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#retrieveNodeFromNode(java.lang.String, org.w3c.dom.Node)
     */
    @Override
    public synchronized Node retrieveNodeFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeFromNode(relativeXPathExpression, referenceNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#retrieveNodeList(java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#retrieveNodeListFromNode(java.lang.String, org.w3c.dom.Node)
     */
    @Override
    public synchronized List<Node> retrieveNodeListFromNode(String relativeXPathExpression, Node referenceNode)
            throws XPathExpressionException {
        // TODO Auto-generated method stub
        return super.retrieveNodeListFromNode(relativeXPathExpression, referenceNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.xml.XmlNodeRetriever#getAbsoluteXPathFromNode(org.w3c.dom.Node)
     */
    @Override
    public String getAbsoluteXPathFromNode(Node node) {
        // TODO Auto-generated method stub
        return super.getAbsoluteXPathFromNode(node);
    }
}
