// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.datatools.xml.utils.ATreeNode;

/**
 * created by cmeng on Jul 1, 2015 Detailled comment
 *
 */
public abstract class AbstractTreePopulator {

    private String encoding;

    protected String filePath;

    protected static int limit;
    
    protected TreeViewer treeViewer;

    abstract public boolean populateTree(String filePath, ATreeNode treeNode);

    abstract public boolean populateTree(String filePath, ATreeNode treeNode, String selectedEntity);

    abstract public void configureDefaultTreeViewer();

    abstract public TreeItem getTreeItem(String absolutePath);

    abstract public String getAbsoluteXPath(TreeItem treeItem);

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Getter for filePath.
     *
     * @return the filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    public static int getMaximumRowsToPreview() {
        return RepositoryManagerHelper.getMaximumRowsToPreview();
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit to set
     */
    public void setLimit(int lit) {
        limit = lit;
    }

    /**
     * Getter for limit.
     *
     * @return the limit
     */
    public static int getLimit() {
        return limit;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
	public List<ATreeNode> getAllNodes() {
		List<ATreeNode> nodes = new ArrayList<ATreeNode>();
		Object input = treeViewer.getInput();
		if(input instanceof List) {
			for(Object obj : (List)input) {
				if(obj instanceof ATreeNode) {
					addNode(((ATreeNode)obj), nodes);
				}
			}
		}else if(input instanceof Object[]) {
			for(Object obj : (Object[])input) {
				if(obj instanceof ATreeNode) {
					addNode(((ATreeNode)obj), nodes);
				}
			}
		}
		return nodes;
	}
	
	private void addNode(ATreeNode node, List<ATreeNode> nodes){
		nodes.add(node);
		if(node.getChildren() != null && node.getChildren().length > 0) {
			nodes.addAll(getChildren(node));
		}
	}
	
	private List<ATreeNode> getChildren(ATreeNode node){
		List<ATreeNode> nodes = new ArrayList<ATreeNode>();
		for(Object obj : node.getChildren()) {
			if(obj instanceof ATreeNode){
				nodes.add((ATreeNode)obj);
				if(((ATreeNode)obj).getChildren() != null && ((ATreeNode)obj).getChildren().length > 0) {
					nodes.addAll(getChildren((ATreeNode)obj));
				}
			}
		}
		return nodes;
	}

}
