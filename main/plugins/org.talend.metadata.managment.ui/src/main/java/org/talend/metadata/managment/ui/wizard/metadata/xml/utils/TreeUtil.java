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
package org.talend.metadata.managment.ui.wizard.metadata.xml.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xs.XSModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xsd.XSDSchema;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.NodeCreationObserver;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.SchemaPopulationUtil;
import org.talend.datatools.xml.utils.XSDPopulationUtil;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.datatools.xml.utils.XSDUtils;
import org.talend.metadata.managment.ui.dialog.RootNodeSelectDialog;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Element;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.NameSpaceNode;
import org.talend.repository.ProjectManager;

/**
 * DOC ke class global comment. Detailled comment <br/>
 * 
 */
public class TreeUtil {

    /**
     * DOC ke Comment method "checkLoopNode".
     * 
     * @param node
     * @return
     */
    public static boolean checkTreeLoopNode(FOXTreeNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof Attribute) {
            return false;
        }

        Element e = (Element) node;

        if (e.getParent() == null) {
            return false;
        }

        if (e.isGroup()) {
            return true;
        }

        return true;

    }

    public static boolean canSetAsLoop(FOXTreeNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof Attribute) {
            return false;
        }

        if (node instanceof NameSpaceNode) {
            return false;
        }

        Element e = (Element) node;

        if (e.getParent() == null) {
            List<FOXTreeNode> children = node.getChildren();
            if (children != null) {
                for (FOXTreeNode child : children) {
                    if (child instanceof Element) {
                        return false;
                    }
                }
            }

        }

        if (e.isGroup()) {
            return true;
        }

        return true;

    }

    /**
     * DOC ke Comment method "guessAndSetLoopNode".
     * 
     * @param root
     */
    public static FOXTreeNode guessAndSetLoopNode(FOXTreeNode root) {
        FOXTreeNode loopNode = root;
        Element node = (Element) loopNode;
        if (node.getElementChildren().size() > 1) {
            node.setLoop(true);
            return node;
        }
        for (FOXTreeNode att : node.getAttributeChildren()) {
            if (att.getColumn() != null) {
                node.setLoop(true);
                return node;
            }
        }
        if (node.getElementChildren().size() == 0) {
            return null;
        }
        do {
            node = (Element) node.getElementChildren().get(0);
            if (node.getColumn() != null) {
                node.setLoop(true);
                return node;
            }
            if (node.getElementChildren().size() > 1) {
                node.setLoop(true);
                return node;
            }
            for (FOXTreeNode att : node.getAttributeChildren()) {
                if (att.getColumn() != null) {
                    node.setLoop(true);
                    return node;
                }
            }
            if (node.getElementChildren().size() == 0) {
                return null;
            }
        } while (true);
    }

    /**
     * DOC ke Comment method "setAsLoopNode".
     * 
     * @param node
     */
    public static void setAsLoopNode(FOXTreeNode node) {
        if (checkTreeLoopNode(node)) {
            node.setLoop(true);
        }
    }

    /**
     * DOC ke Comment method "clearLoopNode".
     * 
     * @param root
     */
    public static void clearLoopNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            if (e.isLoop()) {
                e.setLoop(false);
            } else {
                for (FOXTreeNode child : e.getElementChildren()) {
                    clearLoopNode(child);
                }
            }
        }
    }

    /**
     * DOC xzhang Comment method "clearMainNode".
     * 
     * @param root
     */
    public static void clearMainNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            if (e.isMain()) {
                e.setMain(false);
            }
            for (FOXTreeNode child : e.getElementChildren()) {
                clearMainNode(child);
            }
        }
    }

    /**
     * DOC xzhang Comment method "clearMainNode".
     * 
     * @param root
     */
    public static void upsetMainNode(FOXTreeNode loop) {
        if (loop instanceof Element) {
            FOXTreeNode parent = loop;
            while (parent != null) {
                parent.setMain(true);
                parent = parent.getParent();
            }

        }
    }

    /**
     * DOC ke Comment method "getLoopNode".
     * 
     * @param root
     * @return
     */
    public static FOXTreeNode getLoopNode(FOXTreeNode root) {
        if (root != null && root instanceof Element) {
            Element e = (Element) root;
            if (e.isLoop()) {
                return e;
            }
            for (FOXTreeNode child : e.getElementChildren()) {
                FOXTreeNode loopNode = getLoopNode(child);
                if (loopNode != null) {
                    return loopNode;
                }
            }
        }
        return null;
    }

    /**
     * DOC ke Comment method "getGroupNode".
     * 
     * @param root
     * @return
     */
    public static FOXTreeNode getGroupNode(FOXTreeNode root) {
        if (root != null && root instanceof Element) {
            Element e = (Element) root;
            if (e.isLoop()) {
                return null;
            }
            if (e.isGroup()) {
                return e;
            }
            for (FOXTreeNode child : e.getElementChildren()) {
                FOXTreeNode loopNode = getGroupNode(child);
                if (loopNode != null) {
                    return loopNode;
                }
            }
        }
        return null;
    }

    /**
     * DOC ke Comment method "betweenGroupAndLoop".
     * 
     * @param node
     * @return whether the currnt node is between the group node and loop node in the tree hierachy.
     */
    public static boolean isSubLoopNode(FOXTreeNode node) {
        if (node instanceof Attribute || node.isGroup() || node.isLoop()) {
            return false;
        }
        boolean flag = false;
        FOXTreeNode temp = node.getParent();
        while (temp != null) {
            if (temp.isGroup()) {
                flag = true;
                break;
            }
            if (temp.isLoop()) {
                return false;
            }
            temp = temp.getParent();
        }
        if (flag) {
            if (TreeUtil.findDownLoopNode(node) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC ke Comment method "clearSubGroupNode".
     * 
     * @param node
     */
    public static void clearSubGroupNode(FOXTreeNode node) {
        if (node instanceof Attribute) {
            return;
        }
        if (node.isGroup()) {
            node.setGroup(false);
            return;
        }
        if (node.isLoop()) {
            return;
        }
        for (FOXTreeNode child : node.getChildren()) {
            clearSubGroupNode(child);
        }
    }

    /**
     * DOC ke Comment method "guessLoopWithGroup".
     * 
     * @param node
     * @return
     */
    public static boolean guessLoopWithGroup(FOXTreeNode node) {
        if (node instanceof Attribute) {
            return false;
        }
        boolean result = false;
        if (!node.isGroup()) {
            FOXTreeNode tNode = node;
            while (true) {
                if (tNode.isGroup()) {
                    node = tNode;
                    result = true;
                    break;
                }
                if (((Element) tNode).getElementChildren().size() == 0) {
                    break;
                }
                tNode = ((Element) tNode).getElementChildren().get(0);
            }
        } else {
            result = true;
        }
        if (!result) {
            return false;
        }
        Element e = (Element) node;
        do {
            if (e.getElementChildren().size() > 1 || e.hasLink()) {
                e.setLoop(true);
                return true;
            }
            if (e.getElementChildren().size() == 0) {
                return true;
            }
            e = (Element) e.getElementChildren().get(0);
        } while (true);
    }

    /**
     * DOC ke Comment method "checkTreeGoupNode".
     * 
     * @param node
     * @return
     */
    public static boolean checkTreeGoupNode(FOXTreeNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof Attribute) {
            return false;
        }

        if (node.getParent() == null) {
            return false;
        }

        FOXTreeNode loop = findDownLoopNode(node);
        if (loop == null || loop == node) {
            return false;
        }
        return true;
    }

    /*
     * find the loop node in "treenode" and its children
     */
    public static FOXTreeNode findDownLoopNode(FOXTreeNode treeNode) {
        if (treeNode instanceof Element) {
            Element e = (Element) treeNode;
            if (e.isLoop()) {
                return e;
            }
            for (FOXTreeNode child : e.getChildren()) {
                FOXTreeNode tmp = findDownLoopNode(child);
                if (tmp != null) {
                    return tmp;
                }
            }
        }
        return null;
    }

    public static FOXTreeNode findUpGroupNode(FOXTreeNode treeNode) {
        if (treeNode != null) {
            if (treeNode.isGroup()) {
                return treeNode;
            }
            if (treeNode instanceof Element) {
                return findUpGroupNode(treeNode.getParent());
            }
        }
        return null;
    }

    public static String getPath(FOXTreeNode treeNode) {
        StringBuffer path = new StringBuffer();
        FOXTreeNode tmp = treeNode;
        while (tmp != null) {
            path.insert(0, "/" + tmp.getLabel()); //$NON-NLS-1$
            tmp = tmp.getParent();
        }
        return path.toString();
    }

    public static List<FOXTreeNode> getFoxTreeNodes(String filePath) {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        if (filePath == null || filePath.length() <= 0) {
            return list;
        }

        try {
            ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(filePath, true, 0);
            FOXTreeNode root = cloneATreeNode(treeNode, XmlUtil.isXSDFile(filePath));
            if (root instanceof Element) {
                root = ((Element) root).getElementChildren().get(0);
                root.setParent(null);
                list.add(root);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;
    }

    public static boolean getFoxTreeNodesForXmlMap(String filePath, Shell shell, List<FOXTreeNode> nodeList) throws Exception {
        if (XmlUtil.isXSDFile(filePath)) {
            XSDSchema xsdSchema = getXSDSchema(filePath);
            List<ATreeNode> allTreeNodes = new XSDPopulationUtil2().getAllRootNodes(xsdSchema);
            ATreeNode selectedTreeNode = null;
            if (allTreeNodes != null && !allTreeNodes.isEmpty()) {
                if (allTreeNodes.size() > 1) {
                    RootNodeSelectDialog dialog = new RootNodeSelectDialog(shell, allTreeNodes);
                    if (dialog.open() == IDialogConstants.OK_ID) {
                        selectedTreeNode = dialog.getSelectedNode();
                    } else {
                        return false;
                    }
                } else {
                    selectedTreeNode = allTreeNodes.get(0);
                }
                // nodeList.addAll(getFoxTreeNodesByRootNode(xsdSchema, selectedTreeNode));
                nodeList.addAll(getFoxTreeNodesByRootNode(xsdSchema, selectedTreeNode, false, true, true));
            }
        } else {
            getFoxTreeNodesForXmlMap(filePath, nodeList);
        }
        return true;

    }

    public static List<FOXTreeNode> getFoxTreeNodesForXmlMap(String filePath, String absoluteXPathQuery, String targetNamespace,
            boolean includeAbsSubs) throws Exception {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        if (filePath == null) {
            return list;
        }
        XSDPopulationUtil2 popUtil = XSDUtils.getXsdHander(filePath);
        popUtil.setIncludeAbsSubs(includeAbsSubs);
        if (filePath.endsWith(".zip") || XmlUtil.isXSDFile(filePath) || XmlUtil.isWSDLFile(filePath)) {
            XSDSchema xsModel = getXSDSchema(popUtil, filePath, targetNamespace);
            List<ATreeNode> allTreeNodes = popUtil.getAllRootNodes(xsModel);
            ATreeNode selectedTreeNode = null;
            if (allTreeNodes != null && !allTreeNodes.isEmpty()) {
                if (allTreeNodes.size() > 1) {
                    String[] split = absoluteXPathQuery.split("/");
                    if (split.length > 1) {
                        boolean found = false;
                        for (int i = 0; i < allTreeNodes.size(); i++) {
                            if (split[1] != null && split[1].equals(allTreeNodes.get(i).getValue())) {
                                selectedTreeNode = allTreeNodes.get(i);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            for (int i = 0; i < allTreeNodes.size(); i++) {
                                ATreeNode node = allTreeNodes.get(i);
                                String[] nodeValue = ((String) node.getValue()).split(":");
                                if (nodeValue.length > 1) {
                                    if (split[1].equals(nodeValue[1])) {
                                        List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                                        selectedTreeNode = allTreeNodes.get(i);
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (selectedTreeNode == null) {
                        selectedTreeNode = allTreeNodes.get(0);
                    }
                } else {
                    selectedTreeNode = allTreeNodes.get(0);
                }

                // list = getFoxTreeNodesByRootNode(xsModel, selectedTreeNode);
                list = getFoxTreeNodesByRootNode(popUtil, xsModel, selectedTreeNode, false, true, true);
            }
        } else {
            getFoxTreeNodesForXmlMap(filePath, list);
        }
        return list;

    }

    public static List<FOXTreeNode> getFoxTreeNodesForXmlMap(String filePath, String absoluteXPathQuery, boolean includeAbsSubs)
            throws Exception {
        return getFoxTreeNodesForXmlMap(filePath, absoluteXPathQuery, filePath, includeAbsSubs);
    }

    public static List<FOXTreeNode> getFoxTreeNodesForXmlMap(String filePath, String absoluteXPathQuery) throws Exception {
        return getFoxTreeNodesForXmlMap(filePath, absoluteXPathQuery, false);
    }

    private static void getFoxTreeNodesForXmlMap(String filePath, List<FOXTreeNode> list) throws OdaException,
            URISyntaxException, IOException {
        ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(filePath, true, 0);
        FOXTreeNode root = cloneATreeNode(treeNode, XmlUtil.isXSDFile(filePath));
        if (root instanceof Element) {
            if (root instanceof Element) {
                root = ((Element) root).getElementChildren().get(0);
                root.setParent(null);
                list.add(root);
                // Change dataType for the namespace if it is xml file
                if (XmlUtil.isXMLFile(filePath)) {
                    List<? extends FOXTreeNode> nameSpaceChildren = ((Element) root).getNameSpaceChildren();
                    for (FOXTreeNode nameSpaceChild : nameSpaceChildren) {
                        if (nameSpaceChild instanceof NameSpaceNode) {
                            nameSpaceChild.setDataType(JavaDataTypeHelper.getTalendTypeOfValue(nameSpaceChild.getDataType()));
                        }
                    }
                }
            }
        }
    }

    private static ATreeNode getSelectedTreeNode(List<ATreeNode> allTreeNodes, String absoluteXPathQuery, Shell shell) {
        ATreeNode selectedTreeNode = null;
        if (allTreeNodes.size() > 1) {
            if (absoluteXPathQuery == null) {
                RootNodeSelectDialog dialog = new RootNodeSelectDialog(shell, allTreeNodes);
                if (dialog.open() == IDialogConstants.OK_ID) {
                    selectedTreeNode = dialog.getSelectedNode();
                }
            } else {
                String[] split = absoluteXPathQuery.split("/");
                if (split.length > 1) {
                    for (int i = 0; i < allTreeNodes.size(); i++) {

                        if (split[1] != null && split[1].equals(allTreeNodes.get(i).getValue())) {
                            selectedTreeNode = allTreeNodes.get(i);
                            break;
                        }
                    }
                }
                if (selectedTreeNode == null) {
                    selectedTreeNode = allTreeNodes.get(0);
                }

            }
        } else {
            selectedTreeNode = allTreeNodes.get(0);
        }
        return selectedTreeNode;
    }

    public static List<FOXTreeNode> getFoxTreeNodes(String filePath, String selectedEntity, boolean forMDM) {
        List<String> attList = new ArrayList<String>();
        attList.add(selectedEntity);
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        try {
            XSDSchema xsdSchema = TreeUtil.getXSDSchema(filePath);
            List<ATreeNode> rootNodes = new XSDPopulationUtil2().getAllRootNodes(xsdSchema);
            ATreeNode selectedNode = null;
            if (!rootNodes.isEmpty()) {
                for (Object obj : rootNodes) {
                    ATreeNode node = (ATreeNode) obj;
                    if (node.getValue() != null && node.getValue().equals(selectedEntity)) {
                        selectedNode = node;
                        break;
                    }
                }
                if (selectedNode != null) {
                    return TreeUtil.getFoxTreeNodesByRootNode(xsdSchema, selectedNode, false);
                }
            }

            // normally the code bellow is not usefull , keep it incase xsd is parsed failed by the new way

            if (filePath == null) {
                return list;
            }
            if (selectedEntity == null || "".equals(selectedEntity)) {
                return getFoxTreeNodes(filePath);
            } else {
                ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(filePath, true, forMDM, 0, attList);
                if (treeNode != null) {
                    if (forMDM) {
                        if (selectedEntity.equals(treeNode.getValue())) {
                            selectedNode = treeNode;
                        }
                    }

                    for (Object obj : treeNode.getChildren()) {
                        if (obj instanceof ATreeNode) {
                            ATreeNode node = (ATreeNode) obj;
                            if (selectedEntity.equals(node.getValue())) {
                                selectedNode = node;
                                break;
                            }
                        }
                    }

                    if (selectedNode != null) {
                        FOXTreeNode root = cloneATreeNode(treeNode, XmlUtil.isXSDFile(filePath));
                        if (root instanceof Element) {
                            root.setParent(null);
                            list.add(root);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return list;

    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSModel xsModel, ATreeNode selectedRootNode) {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        if (xsModel == null || selectedRootNode == null) {
            return list;
        }

        ATreeNode treeNode = null;
        try {
            ATreeNode rootNode = SchemaPopulationUtil.getSchemaTree(xsModel, selectedRootNode, true);
            if (rootNode.getChildren().length > 0) {
                for (Object obj : rootNode.getChildren()) {
                    if (obj instanceof ATreeNode) {
                        treeNode = (ATreeNode) obj;
                    }
                }
            }
            if (treeNode == null) {
                return list;
            }

            FOXTreeNode root = cloneATreeNode(treeNode, true);
            if (root instanceof Element) {
                root.setParent(null);
                list.add(root);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return list;
    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSModel xsModel, ATreeNode selectedRootNode, boolean resolved) {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        if (xsModel == null || selectedRootNode == null) {
            return list;
        }

        ATreeNode treeNode = null;
        try {
            if (resolved) {
                treeNode = selectedRootNode;
            } else {
                ATreeNode rootNode = SchemaPopulationUtil.getSchemaTree(xsModel, selectedRootNode, true);
                if (rootNode.getChildren().length > 0) {
                    for (Object obj : rootNode.getChildren()) {
                        if (obj instanceof ATreeNode) {
                            treeNode = (ATreeNode) obj;
                            break;
                        }
                    }
                }
            }

            if (treeNode == null) {
                return list;
            }

            FOXTreeNode root = cloneATreeNode(treeNode, true);
            if (root instanceof Element) {
                root.setParent(null);
                list.add(root);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return list;
    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSDSchema xsdSchema, ATreeNode selectedRootNode) {
        return getFoxTreeNodesByRootNode(xsdSchema, selectedRootNode, false, false, false);
    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSDSchema xsdSchema, ATreeNode selectedRootNode, boolean resolved) {
        return getFoxTreeNodesByRootNode(xsdSchema, selectedRootNode, resolved, false, false);
    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSDPopulationUtil2 populator, XSDSchema xsdSchema,
            ATreeNode selectedRootNode, boolean resolved, boolean supportChoice, boolean supportSubstitution) {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        if (xsdSchema == null || selectedRootNode == null) {
            return list;
        }

        ATreeNode treeNode = null;
        try {
            if (resolved) {
                treeNode = selectedRootNode;
            } else {
                treeNode = SchemaPopulationUtil.getSchemaTree(populator, xsdSchema, selectedRootNode, supportChoice,
                        supportSubstitution);
            }

            if (treeNode == null) {
                return list;
            }

            FOXTreeNode root = cloneATreeNode(treeNode, true);
            if (root instanceof Element) {
                root.setParent(null);
                list.add(root);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return list;

    }

    public static List<FOXTreeNode> getFoxTreeNodesByRootNode(XSDSchema xsdSchema, ATreeNode selectedRootNode, boolean resolved,
            boolean supportChoice, boolean supportSubstitution) {
        return getFoxTreeNodesByRootNode(null, xsdSchema, selectedRootNode, resolved, supportChoice, supportSubstitution);
    }

    public static XSModel getXSModel(String fileName) {
        XSModel model = null;
        try {
            String newFilePath;
            try {
                newFilePath = CopyDeleteFileUtilForWizard.copyToTemp(fileName);
            } catch (PersistenceException e1) {
                newFilePath = fileName;
            }
            model = XSDPopulationUtil.getXSModel(newFilePath);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return model;
    }

    public static XSDSchema getXSDSchema(String fileName) {
        return getXSDSchema(null, fileName);
    }

    public static XSDSchema getXSDSchema(XSDPopulationUtil2 popUtil, String fileName) {
        return getXSDSchema(popUtil, fileName, fileName);
    }

    public static XSDSchema getXSDSchema(XSDPopulationUtil2 popUtil, String fileName, String targetNamespace) {
        XSDSchema schema = null;
        if (popUtil == null) {
            popUtil = XSDUtils.getXsdHander(fileName);
        }
        try {
            if (fileName.endsWith(".zip")) {
                schema = getSchemaFromZip(popUtil, fileName);
            } else if (XmlUtil.isWSDLFile(fileName)) {
                if (XSDUtils.isWsdlHandlerRegistred()) {
                    schema = popUtil.getXSDSchema(targetNamespace);
                } else {
                    Map<String, File> nsToSchema = XSDUtils.getNSToSchemaMapFromWSDL(getTempPath().toPortableString(), fileName);
                    Iterator<String> iterator = nsToSchema.keySet().iterator();
                    File schemaFile = null;
                    while (iterator.hasNext()) {
                        String ns = iterator.next();
                        File tempXSDFile = nsToSchema.get(ns);
                        popUtil.addSchema(tempXSDFile.getAbsolutePath());
                        if (targetNamespace != null && targetNamespace.equals(ns)) {
                            schemaFile = tempXSDFile;
                        }
                    }
                    if (schemaFile != null) {
                        schema = popUtil.getXSDSchema(schemaFile.getAbsolutePath());
                    }
                }
            } else {
                String newFilePath;
                try {
                    newFilePath = CopyDeleteFileUtilForWizard.copyToTemp(fileName);
                } catch (PersistenceException e1) {
                    newFilePath = fileName;
                }
                schema = popUtil.getXSDSchema(newFilePath);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return schema;
    }

    private static IPath getTempPath() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        IPath path = new Path(fsProject.getLocationURI().getPath());
        path = path.append("temp");
        return path;
    }

    private static XSDSchema getSchemaFromZip(XSDPopulationUtil2 popUtil, String fileName) throws IOException, URISyntaxException {
        IPath path = getTempPath();
        path = path.append("unzip_" + new Path(fileName).lastSegment());
        String unzipPath = path.toPortableString();
        File zip = new File(fileName);
        if (zip.exists()) {
            try {
                FilesUtils.unzip(zip.getAbsolutePath(), unzipPath);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        File unzipFile = new File(unzipPath);
        if (unzipFile.exists() && unzipFile.isDirectory()) {
            File[] tempXSDFiles = unzipFile.listFiles();
            File schemaFile = tempXSDFiles[0];
            String schemaFileNameHint = null;
            String[] zipNameArray = zip.getName().split("_"); //$NON-NLS-1$
            if (zipNameArray.length >= 2) {
                schemaFileNameHint = zipNameArray[0];
            }
            for (File tempXSDFile : tempXSDFiles) {
                popUtil.addSchema(tempXSDFile.getAbsolutePath());
                if (schemaFileNameHint != null && tempXSDFile.getName().startsWith(schemaFileNameHint)) {
                    schemaFile = tempXSDFile;
                }
            }
            return popUtil.getXSDSchema(schemaFile.getAbsolutePath());
        }
        return null;
    }

    public static FOXTreeNode cloneATreeNode(ATreeNode aNode, boolean isXsd) {
        List<ATreeNode> aNodes = NodeCreationObserver.getList();
        Map<ATreeNode,FOXTreeNode> mapOldToNewNode = new HashMap<>();
        for (ATreeNode treeNode : aNodes) {
            if (isXsd && treeNode.getValue() instanceof String) {
                String currentPath = treeNode.getValue() + "[" + treeNode.getDataType() + "]";
                if (treeNode.getParent() != null) {
                    String parentPath = "/" + treeNode.getParent().getValue() + "[" + treeNode.getParent().getDataType() + "]";
                    if (parentPath.contains("/" + currentPath + "/")) {
                        continue;
                    }
                }
            }
            FOXTreeNode node = null;
            if (treeNode.getType() == ATreeNode.ATTRIBUTE_TYPE) {
                node = new Attribute();
            } else {
                node = new Element();
            }
            if (treeNode.getType() == ATreeNode.NAMESPACE_TYPE) {
                node = new NameSpaceNode();
                node.setLabel(treeNode.getDataType());
                node.setDefaultValue((String) treeNode.getValue());
            } else {
                node.setLabel(treeNode.getValue() == null ? null : treeNode.getValue().toString());
                node.setChoice(treeNode.isChoice());
                node.setSubstitution(treeNode.isSubstitution());
                node.setOptional(treeNode.isOptional());
                // init the unique to guess first loop element when create mdmoutput wizard
                node.getUniqueNames().clear();
                node.getUniqueNames().addAll(treeNode.getUniqueNames());
            }
            if (isXsd) {
                MappingTypeRetriever retriever = MetadataTalendType.getMappingTypeRetriever("xsd_id"); //$NON-NLS-1$
                String originalDataType = treeNode.getOriginalDataType();
                if (originalDataType != null && !originalDataType.startsWith("xs:")) { //$NON-NLS-1$
                    originalDataType = "xs:" + originalDataType; //$NON-NLS-1$
                }
                node.setDataType(retriever.getDefaultSelectedTalendType(originalDataType));
            } else {
                node.setDataType(treeNode.getDataType());
            }
            node.setDataMaxLength(treeNode.getDataMaxLength());
            node.setPrecisionValue(treeNode.getPrecisionValue());
            mapOldToNewNode.put(treeNode, node);
        }
        for (ATreeNode treeNode : aNodes) {
            FOXTreeNode current = mapOldToNewNode.get(treeNode);
            for (Object childObject : treeNode.getChildren()) {
                ATreeNode childNode = (ATreeNode) childObject;
                current.addChild(mapOldToNewNode.get(childNode));
            }
        }
        return mapOldToNewNode.get(aNode);
    }

    public static FOXTreeNode getRootFOXTreeNode(FOXTreeNode node) {
        if (node != null) {
            FOXTreeNode parent = node.getParent();
            if (parent == null) {
                return node;
            }
            return getRootFOXTreeNode(parent);
        }
        return null;
    }

    public static List<FOXTreeNode> parseMDMUpdateReport(Shell shell, boolean needConfirm) {
        boolean fileExist = false;
        String filePath = ""; //$NON-NLS-1$
        try {
            Bundle b = Platform.getBundle("org.talend.repository.mdm"); //$NON-NLS-1$
            if (b == null) {
                return null;
            }
            URL fileUrl = FileLocator.find(b, new Path("/resources/UpdateReport.xsd"), null); //$NON-NLS-1$
            if (fileUrl == null) {
                return null;
            }
            URL url = FileLocator.toFileURL(fileUrl);
            if (url != null) {
                filePath = copyToTempFile(url, "UpdateReport.xsd"); //$NON-NLS-1$
                fileExist = true;
            }
        } catch (IOException e) {
            fileExist = false;
        }
        if (!fileExist) {
            return null;
        }
        boolean pass = true;
        if (needConfirm) {
            pass = MessageDialog.openConfirm(shell, "Update Report", "Do you want to add the Update Report content?");
        }
        if (pass) {
            try {
                if (XmlUtil.isXSDFile(filePath)) {
                    XSDSchema xsdSchema = getXSDSchema(filePath);
                    List<ATreeNode> allTreeNodes = new XSDPopulationUtil2().getAllRootNodes(xsdSchema);
                    if (!allTreeNodes.isEmpty()) {
                        return getFoxTreeNodesByRootNode(xsdSchema, allTreeNodes.get(0));
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        return null;
    }

    private static String copyToTempFile(URL url, String fileName) {
        try {
            IPath tempPath = new Path(System.getProperty("user.dir")).append("temp"); //$NON-NLS-1$ //$NON-NLS-2$
            File tempFile = tempPath.toFile();
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File newFile = new File(tempFile, fileName);

            FileInputStream srcStream = new FileInputStream(url.getPath());
            FilesUtils.copyFile(srcStream, newFile);

            return newFile.getAbsolutePath();
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        return null;
    }
}
