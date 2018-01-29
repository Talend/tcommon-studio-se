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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.datatools.xml.utils.ATreeNode;

/**
 * created by cmeng on Jul 1, 2015 Detailled comment
 *
 */
public class JsonTreePopulator extends AbstractTreePopulator {

    private TreeViewer treeViewer;

    private String jsonValue;

    public JsonTreePopulator(TreeViewer treeViewer) {
        super();
        this.treeViewer = treeViewer;
    }

    @Override
    public boolean populateTree(String strFilePath, ATreeNode treeNode) {
        return populateTree(strFilePath, treeNode, null, true);
    }

    @Override
    public boolean populateTree(String strFilePath, ATreeNode treeNode, String selectedEntity) {
        return populateTree(strFilePath, treeNode, selectedEntity, true);
    }

    /**
     * 
     * DOC cmeng Comment method "populateTree".
     * 
     * @param strValue filePath or jsonValue depending on parameter isFilePath
     * @param treeNode
     * @param selectedEntity
     * @param isFilePath
     * @return
     */
    public boolean populateTree(String strValue, ATreeNode treeNode, Object selectedEntity, boolean isFilePath) {
        if (isFilePath) {
            File jsonFilePath = new File(strValue);
            filePath = strValue;
            return populateTree(treeNode, selectedEntity, jsonFilePath);
        } else {
            return populateTree(treeNode, selectedEntity, strValue);
        }
    }

    public boolean populateTree(ATreeNode treeNode, Object selectedEntity, String jsonValue) {
        if (jsonValue == null || jsonValue.isEmpty()) {
            return false;
        }
        if (selectedEntity instanceof JsonTreeNode) {
            SchemaPopulationUtil.fetchTreeNode((JsonTreeNode) selectedEntity, 1);
            treeNode = (ATreeNode) selectedEntity;
        } else {
            treeNode = SchemaPopulationUtil.getSchemaTree(jsonValue, limit);
        }
        if (treeNode == null) {
            return false;
        } else {
            List<JsonTreeNode> input = new ArrayList<JsonTreeNode>();
            input.add((JsonTreeNode) treeNode);
            treeViewer.setInput(input);
            treeViewer.expandToLevel(3);
        }
        this.jsonValue = jsonValue;
        return true;
    }

    public boolean populateTree(ATreeNode treeNode, Object selectedEntity, File fileValue) {
        if (fileValue == null || !fileValue.exists() || !fileValue.isFile()) {
            return false;
        }
        if (selectedEntity instanceof JsonTreeNode) {
            SchemaPopulationUtil.fetchTreeNode((JsonTreeNode) selectedEntity, 1);
            treeNode = (ATreeNode) selectedEntity;
        } else {
            treeNode = SchemaPopulationUtil.getSchemaTree(fileValue, limit);
        }
        if (treeNode == null) {
            return false;
        } else {
            List<JsonTreeNode> input = new ArrayList<JsonTreeNode>();
            input.add((JsonTreeNode) treeNode);
            treeViewer.setInput(input);
            treeViewer.expandToLevel(3);
        }
        return true;

        // FileReader fileReader = null;
        // BufferedReader bufferedReader = null;
        // try {
        // fileReader = new FileReader(fileValue);
        // bufferedReader = new BufferedReader(fileReader);
        // String line = null;
        // StringBuffer jsonBuffer = new StringBuffer();
        // while ((line = bufferedReader.readLine()) != null) {
        //                jsonBuffer.append(line).append("\n"); //$NON-NLS-1$
        // }
        // return populateTree(treeNode, selectedEntity, jsonBuffer.toString());
        // } catch (Exception e) {
        // CommonExceptionHandler.process(e);
        // return false;
        // } finally {
        // if (fileReader != null) {
        // try {
        // fileReader.close();
        // } catch (IOException e) {
        // // nothing need to do
        // }
        // }
        //
        // if (bufferedReader != null) {
        // try {
        // bufferedReader.close();
        // } catch (IOException e) {
        // // nothing need to do
        // }
        // }
        // }
    }

    @Override
    public void configureDefaultTreeViewer() {
        if (treeViewer.getContentProvider() != null) {
            treeViewer.setInput(null);
        }
        treeViewer.setContentProvider(new JsonTreeNodeContentProvider());
        treeViewer.setLabelProvider(new JsonTreeLabelProvider());
    }

    @Override
    public TreeItem getTreeItem(String absolutePath) {
        if (absolutePath == null || absolutePath.isEmpty()) {
            return null;
        }
        TreeItem[] items = treeViewer.getTree().getItems();
        if (items == null || items.length == 0) {
            return null;
        }

        return getTreeItem(items, SchemaPopulationUtil.getFilteredJsonPath(absolutePath));
    }

    public List<JsonTreeNode> getRootJsonTreeNodes() {
        TreeItem[] items = treeViewer.getTree().getItems();
        if (items == null || items.length <= 0) {
            return null;
        }
        List<JsonTreeNode> rootJsonTreeNodes = new ArrayList<JsonTreeNode>(items.length);
        for (TreeItem item : items) {
            rootJsonTreeNodes.add((JsonTreeNode) item.getData());
        }
        return rootJsonTreeNodes;
    }

    private TreeItem getTreeItem(TreeItem[] treeItems, String absolutePath) {
        if (treeItems == null || treeItems.length == 0) {
            return null;
        }
        for (TreeItem treeItem : treeItems) {
            Object obj = treeItem.getData();
            if (obj instanceof JsonTreeNode) {
                String jsonPath = SchemaPopulationUtil.getFilteredJsonPath(((JsonTreeNode) obj).getJsonPath());
                if (absolutePath.equals(jsonPath)) {
                    return treeItem;
                } else if (absolutePath.startsWith(jsonPath + ".")) { //$NON-NLS-1$
                    return getTreeItem(treeItem.getItems(), absolutePath);
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    @Override
    public String getAbsoluteXPath(TreeItem treeItem) {
        Object jsonObj = treeItem.getData();
        if (jsonObj instanceof JsonTreeNode) {
            return ((JsonTreeNode) jsonObj).getJsonPath();
        }
        return null;
    }
}
