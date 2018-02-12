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
package org.talend.repository.items.importexport.wizard.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.repository.items.importexport.handlers.model.EmptyFolderImportItem;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportNodesBuilder {

    /**
     * technicalLabel name with project nodes.
     */
    private Map<String, ProjectImportNode> projectNodesMap = new HashMap<String, ProjectImportNode>();

    private Map<String, StandardJobImportNode> standardNodesMap = new HashMap<String, StandardJobImportNode>();

    private List<ImportItem> allImportItemRecords = new ArrayList<ImportItem>();

    private List<ItemImportNode> allImportItemNode = new ArrayList<ItemImportNode>();

    private static IHadoopClusterService hadoopClusterService = null;

    static {
        hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
    }

    public List<ProjectImportNode> getProjectNodes() {
        List<ProjectImportNode> list = new ArrayList(this.projectNodesMap.values());
        // sort by the project name.
        Collections.sort(list);
        return list;
    }

    public ImportItem[] getAllImportItemRecords() {
        return this.allImportItemRecords.toArray(new ImportItem[0]);
    }

    /**
     * Getter for allImportItemNode.
     * 
     * @return the allImportItemNode
     */
    public List<ItemImportNode> getAllImportItemNode() {
        return this.allImportItemNode;
    }

    public void clear() {
        this.allImportItemNode.clear();
        this.allImportItemRecords.clear();
        this.projectNodesMap.clear();
        this.standardNodesMap.clear();
    }

    public void addItems(List<ImportItem> items) {
        if (items != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                Map<ImportItem, List<ImportItem>> itemMap = getTestCaseItemMap(items);
                for (ImportItem ir : itemMap.keySet()) {
                    List<ImportItem> children = itemMap.get(ir);
                    addItem(ir, children);
                }
            } else {
                for (ImportItem ir : items) {
                    addItem(ir);
                }
            }
        }
    }

    private Map<ImportItem, List<ImportItem>> getTestCaseItemMap(List<ImportItem> items) {
        Map<ImportItem, List<ImportItem>> map = new HashMap<ImportItem, List<ImportItem>>();
        ITestContainerProviderService testContainerService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            testContainerService = (ITestContainerProviderService) GlobalServiceRegister.getDefault().getService(
                    ITestContainerProviderService.class);
        }
        if (items != null && testContainerService != null) {
            Map<String, List<ImportItem>> itemMap = new HashMap<String, List<ImportItem>>();
            for (ImportItem ir : items) {
                if (ir.getItem() != null && !testContainerService.isTestContainerItem(ir.getItem())) {
                    String id = ir.getProperty().getId();
                    // may have different versions
                    List<ImportItem> itemList = itemMap.get(id);
                    if (itemList == null) {
                        itemList = new LinkedList<ImportItem>();
                        itemMap.put(id, itemList);
                    }
                    if (!itemList.contains(ir)) {
                        itemList.add(ir);
                    }
                    map.put(ir, new ArrayList<ImportItem>());
                }
            }
            Set<String> keys = itemMap.keySet();
            for (String key : keys) {
                // 1. get the last version
                List<ImportItem> itemList = itemMap.get(key);
                ImportItem importItem = null;
                Item item = null;
                String version = ""; //$NON-NLS-1$
                /**
                 * should keep same with conflict view order
                 */
                boolean useLastVersion = true;
                Iterator<ImportItem> iter = itemList.iterator();
                while (iter.hasNext()) {
                    ImportItem curImportItem = iter.next();
                    Item curItem = curImportItem.getItem();
                    String curVersion = curItem.getProperty().getVersion();
                    if (item == null) {
                        item = curItem;
                        version = curVersion;
                        importItem = curImportItem;
                    } else {
                        if (curVersion != null && 0 < (curVersion.compareTo(version) * (useLastVersion ? 1 : -1))) {
                            item = curItem;
                            version = curVersion;
                            importItem = curImportItem;
                        }
                    }
                }

                if (item == null) {
                    continue;
                }
                List<ImportItem> children = new ArrayList<ImportItem>();
                for (ImportItem child : items) {
                    Item childItem = child.getItem();
                    if (childItem == null) {
                        continue;
                    }
                    boolean isTestContainer = testContainerService.isTestContainerItem(childItem);
                    if (isTestContainer) {
                        String path = childItem.getState().getPath();
                        if (path != null && path.contains("/")) {
                            int index = path.indexOf("/");
                            path = path.substring(index + 1);
                            if (path.equals(item.getProperty().getId())) {
                                children.add(child);
                            }
                        }
                    }
                }
                map.put(importItem, children);
            }
        }
        return map;
    }

    public void addItem(ImportItem itemRecord) {
        addItem(itemRecord, null);
    }

    public void addItem(ImportItem itemRecord, List<ImportItem> children) {
        if (itemRecord != null) {
            this.allImportItemRecords.add(itemRecord);

            final Project project = itemRecord.getItemProject();
            if (project == null) {
                return; // must have project
            }
            final String technicalLabel = project.getTechnicalLabel();

            ProjectImportNode projectImportNode = this.projectNodesMap.get(technicalLabel);
            if (projectImportNode == null) {
                projectImportNode = new ProjectImportNode(project);
                this.projectNodesMap.put(technicalLabel, projectImportNode);
            }
            final Item item = itemRecord.getItem();
            if (item != null && hadoopClusterService != null && hadoopClusterService.isHadoopSubItem(item)) {
                return;
            }
            final ERepositoryObjectType itemType = itemRecord.getRepositoryType();

            // set for type
            ImportNode typeImportNode = findAndCreateParentTypeNode(projectImportNode, itemType);

            // set for type
            ImportNode parentImportNode = typeImportNode; // by default, in under type node.
            if (parentImportNode == null) {
                parentImportNode = projectImportNode;
            }

            if (ERepositoryObjectType.PROCESS.equals(itemType) && ERepositoryObjectType.findParentType(itemType) == null
                    && PluginChecker.isTIS()) { // if tos, no standard node
                // handle the standard job and create a standard node folder
                // set for type
                StandardJobImportNode standJobImportNode = this.standardNodesMap.get(technicalLabel);
                if (standJobImportNode == null) {
                    standJobImportNode = new StandardJobImportNode(itemType);
                    this.standardNodesMap.put(technicalLabel, standJobImportNode);
                    typeImportNode.addChild(standJobImportNode);
                }
                parentImportNode = standJobImportNode;
                typeImportNode = standJobImportNode;

            }
            if (itemRecord instanceof EmptyFolderImportItem) {
                IPath path = new Path(item.getState().getPath());
                path = path.append(itemRecord.getLabel());
                parentImportNode = findAndCreateFolderNode(typeImportNode, path);
                parentImportNode.setItemRecord(itemRecord);
            } else {
                String path = item.getState().getPath();
                if (StringUtils.isNotEmpty(path)) { // if has path, will find the real path node.
                    parentImportNode = findAndCreateFolderNode(typeImportNode, new Path(path));
                }
                ItemImportNode itemNode = new ItemImportNode(itemRecord);
                parentImportNode.addChild(itemNode);
                allImportItemNode.add(itemNode);
                if (children != null) {
                    for (ImportItem childRecord : children) {
                        ItemImportNode childNode = new ItemImportNode(childRecord);
                        itemNode.addChild(childNode);
                    }
                }
            }
        }
    }

    private ImportNode findAndCreateFolderNode(ImportNode parentNode, IPath path) {
        if (path.segmentCount() > 0 && parentNode instanceof FolderImportNode) {
            String first = path.segment(0);
            FolderImportNode subFolderImportNode = ((FolderImportNode) parentNode).getSubFolders().get(first);
            if (subFolderImportNode == null) {
                subFolderImportNode = new FolderImportNode(first);
                parentNode.addChild(subFolderImportNode);
            }
            return findAndCreateFolderNode(subFolderImportNode, path.removeFirstSegments(1));
        } else { // the last one
            return parentNode;
        }
    }

    private TypeImportNode findAndCreateParentTypeNode(ProjectImportNode projectNode, ERepositoryObjectType curType) {

        ERepositoryObjectType parentParentType = ERepositoryObjectType.findParentType(curType);
        if (parentParentType == null) { // is root type, try to find from project node
            TypeImportNode typeImportNode = findAndCreateTypeNode(projectNode, curType, true);
            return typeImportNode;
        }

        // try to find parent parent node from project node
        TypeImportNode findParentParentTypeNode = findAndCreateTypeNode(projectNode, parentParentType, false);
        if (findParentParentTypeNode == null) {
            findParentParentTypeNode = findAndCreateParentTypeNode(projectNode, parentParentType);
        }
        TypeImportNode typeImportNode = findAndCreateTypeNode(findParentParentTypeNode, curType, true);
        return typeImportNode;

    }

    private TypeImportNode findAndCreateTypeNode(ImportNode parentNode, ERepositoryObjectType curType, boolean creatingInParent) {
        if (parentNode != null && curType != null) {
            Map<ERepositoryObjectType, TypeImportNode> typeNodesChildrenMap = null;
            if (parentNode instanceof ProjectImportNode) {
                typeNodesChildrenMap = ((ProjectImportNode) parentNode).getTypeNodesChildrenMap();
            } else if (parentNode instanceof TypeImportNode) {
                typeNodesChildrenMap = ((TypeImportNode) parentNode).getTypeNodesChildrenMap();
            }
            if (typeNodesChildrenMap == null) {
                return null;
            }

            TypeImportNode typeImportNode = typeNodesChildrenMap.get(curType);
            if (typeImportNode != null) {
                return typeImportNode;
            } else {
                if (creatingInParent) {
                    // not found, create new one.
                    typeImportNode = new TypeImportNode(curType);
                    typeNodesChildrenMap.put(curType, typeImportNode);
                    parentNode.addChild(typeImportNode);
                    return typeImportNode;
                } else { // try the all type nodes
                    for (ERepositoryObjectType type : typeNodesChildrenMap.keySet()) {
                        TypeImportNode childTypeNode = typeNodesChildrenMap.get(type);
                        TypeImportNode findTypeImportNode = findAndCreateTypeNode(childTypeNode, curType, false);
                        if (findTypeImportNode != null) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
}
