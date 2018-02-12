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
package org.talend.repository.metadata.ui.wizards.form;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.FileConnectionContextUtils.EFileParamName;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Element;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileWizard;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractXmlFileStepForm.java 48226 2010-09-14 10:04:12Z hywang $
 * 
 */
public abstract class AbstractXmlFileStepForm extends AbstractXmlStepForm {

    protected XmlFileConnection connection;

    protected int order = 1;

    protected boolean xsdPathChanged = false;

    /**
     * DOC cantoine AbstractXmlFileStepForm constructor comment. Use to step1
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
    }

    /**
     * DOC cantoine AbstractXmlFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC cantoine AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractXmlFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
    }

    protected XmlFileConnection getConnection() {
        return (XmlFileConnection) connectionItem.getConnection();
    }

    @Override
    protected void exportAsContext() {
        collectConnParams();
        super.exportAsContext();
    }

    protected void collectConnParams() {
        if (getConnection().isInputModel()) {
            addContextParams(EFileParamName.File, true);
            addContextParams(EFileParamName.Encoding, true);
            addContextParams(EParamName.XPathQuery, true);
        } else {
            addContextParams(EParamName.OutputFilePath, true);
        }

    }

    protected void initMetadataTable(List<FOXTreeNode> list, EList columnList) {
        int maxColumnsNumber = CoreUIPlugin.getDefault().getPreferenceStore()
                .getInt(ITalendCorePrefConstants.MAXIMUM_AMOUNT_OF_COLUMNS_FOR_XML);
        for (FOXTreeNode node : list) {
            if (columnList.size() > maxColumnsNumber) {
                return;
            }
            MetadataEmfTableEditor editor = new MetadataEmfTableEditor();
            if (node instanceof Element) {
                String label = node.getLabel();
                if (!node.hasChildren() && label != null && !label.equals("")) {
                    String columnName = label;
                    if (columnName.contains(":")) { //$NON-NLS-1$
                        columnName = columnName.split(":")[1]; //$NON-NLS-1$
                    }
                    columnName = columnName.replaceAll("[^a-zA-Z0-9]", "_");
                    String dataType = node.getDataType();
                    MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                    metadataColumn.setLabel(editor.getNextGeneratedColumnName(columnName, columnList));
                    metadataColumn.setOriginalField(label);
                    metadataColumn.setTalendType(dataType);
                    metadataColumn.setPattern("\"dd-MM-yyyy\""); //$NON-NLS-1$
                    metadataColumn.setLength(node.getDataMaxLength());
                    metadataColumn.setPrecision(node.getPrecisionValue());
                    columnList.add(metadataColumn);
                    node.setColumn(ConvertionHelper.convertToIMetaDataColumn(metadataColumn));
                }
            }
            if (node instanceof Attribute) {
                String label = node.getLabel();
                if (label != null && !label.equals("")) {
                    String columnName = label;
                    if (columnName.contains(":")) { //$NON-NLS-1$
                        columnName = columnName.split(":")[1]; //$NON-NLS-1$
                    }
                    columnName = columnName.replaceAll("[^a-zA-Z0-9]", "_");
                    String dataType = node.getDataType();
                    MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                    metadataColumn.setLabel(editor.getNextGeneratedColumnName(columnName, columnList));
                    metadataColumn.setOriginalField(label);
                    metadataColumn.setTalendType(dataType);
                    metadataColumn.setPattern("\"dd-MM-yyyy\""); //$NON-NLS-1$
                    metadataColumn.setLength(node.getDataMaxLength());
                    metadataColumn.setPrecision(node.getPrecisionValue());
                    columnList.add(metadataColumn);
                    node.setColumn(ConvertionHelper.convertToIMetaDataColumn(metadataColumn));
                }
            }
            if (node.hasChildren()) {
                List<FOXTreeNode> children = node.getChildren();
                initMetadataTable(children, columnList);
            }
        }
    }

    protected void initNodeOrder(FOXTreeNode node) {
        if (node == null) {
            return;
        }
        FOXTreeNode parent = node.getParent();
        if (parent == null) {
            node.setOrder(1);
            order = 1;
        }
        List<FOXTreeNode> childNode = node.getChildren();
        for (FOXTreeNode child : childNode) {
            child.setOrder(order);
            order++;
            if (child.getChildren().size() > 0) {
                initNodeOrder(child);
            }
        }
    }

    protected void tableLoader(Element element, String parentPath, List<XMLFileNode> table, String defaultValue) {
        XMLFileNode xmlFileNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
        String currentPath = parentPath + "/" + element.getLabel();
        xmlFileNode.setXMLPath(currentPath);
        xmlFileNode.setRelatedColumn(element.getColumnLabel());
        xmlFileNode.setAttribute(element.isMain() ? "main" : "branch");
        xmlFileNode.setDefaultValue(defaultValue);
        xmlFileNode.setType(element.getDataType());
        xmlFileNode.setOrder(element.getOrder());
        table.add(xmlFileNode);
        for (FOXTreeNode att : element.getAttributeChildren()) {
            xmlFileNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
            xmlFileNode.setXMLPath(att.getLabel());
            xmlFileNode.setRelatedColumn(att.getColumnLabel());
            xmlFileNode.setAttribute("attri");
            xmlFileNode.setDefaultValue(att.getDefaultValue());
            xmlFileNode.setType(att.getDataType());
            xmlFileNode.setOrder(att.getOrder());
            table.add(xmlFileNode);
        }
        for (FOXTreeNode att : element.getNameSpaceChildren()) {
            xmlFileNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
            xmlFileNode.setXMLPath(att.getLabel());
            xmlFileNode.setRelatedColumn(att.getColumnLabel());
            xmlFileNode.setAttribute("ns");
            xmlFileNode.setDefaultValue(att.getDefaultValue());
            xmlFileNode.setType(att.getDataType());
            xmlFileNode.setOrder(att.getOrder());
            table.add(xmlFileNode);
        }
        List<FOXTreeNode> children = element.getElementChildren();
        for (FOXTreeNode child : children) {
            if (!child.isGroup() && !child.isLoop()) {
                tableLoader((Element) child, currentPath, table, child.getDefaultValue());
            }
        }

    }

    protected void updateConnectionProperties(FOXTreeNode foxTreeNode) {
        EList root = getConnection().getRoot();
        EList loop = getConnection().getLoop();
        EList group = getConnection().getGroup();
        root.clear();
        loop.clear();
        group.clear();
        if (foxTreeNode != null) {
            initNodeOrder(foxTreeNode);
            tableLoader((Element) foxTreeNode, "", root, foxTreeNode.getDefaultValue());
            Element loopNode = (Element) TreeUtil.getLoopNode(foxTreeNode);
            if (loopNode != null) {
                String path = TreeUtil.getPath(loopNode);
                tableLoader(loopNode, path.substring(0, path.lastIndexOf("/")), loop, loopNode.getDefaultValue());
            }
            Element groupNode = (Element) TreeUtil.getGroupNode(foxTreeNode);
            if (groupNode != null) {
                String path = TreeUtil.getPath(groupNode);
                tableLoader(groupNode, path.substring(0, path.lastIndexOf("/")), group, groupNode.getDefaultValue());
            }
        }
    }

    public XSDSchema updateXSDSchema(String file) {
        XSDSchema xsdSchema = null;
        XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
        if (!xsdPathChanged && wizard.getXSDSchema() != null) {
            xsdSchema = wizard.getXSDSchema();
        } else {
            if (file != null) {
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), true);
                    file = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, file));
                }
                if (new File(file).exists()) {
                    xsdSchema = TreeUtil.getXSDSchema(file);
                } else {
                    String xsdPath = getXSDXMLFilePath();
                    if (xsdPath != null) {
                        xsdSchema = TreeUtil.getXSDSchema(xsdPath);
                    }
                }
                wizard.setXsModel(xsdSchema);
                wizard.getFoxNodesMap().clear();
            }
        }
        return xsdSchema;
    }

    public XSDSchema getXSDSchema(String file) {
        XSDSchema xsdSchema = null;
        if (new File(file).exists()) {
            xsdSchema = TreeUtil.getXSDSchema(file);
        } else {
            String xsdPath = getXSDXMLFilePath();
            if (xsdPath != null) {
                xsdSchema = TreeUtil.getXSDSchema(xsdPath);
            }
        }
        return xsdSchema;
    }

    public List<ATreeNode> updateRootNodes(XSDSchema xsdSchema, boolean force) {
        List<ATreeNode> rootNodes = new ArrayList<ATreeNode>();
        if (xsdSchema == null) {
            return rootNodes;
        }
        XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
        if (wizard.getRootNodes() == null || force) {
            try {
                rootNodes = new XSDPopulationUtil2().getAllRootNodes(xsdSchema);
                ((XmlFileWizard) getPage().getWizard()).setRootNodes(rootNodes);
            } catch (OdaException e) {
                ExceptionHandler.process(e);
            }
        } else {
            rootNodes = wizard.getRootNodes();
        }

        return rootNodes;
    }

    protected List<FOXTreeNode> getCorrespondingFoxTreeNodes(ATreeNode selectedRootNode, boolean resolved) {
        XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
        Map<String, List<FOXTreeNode>> foxNodesMap = wizard.getFoxNodesMap();
        String key = String.valueOf(selectedRootNode.getValue());
        List<FOXTreeNode> foxTreeNodes = foxNodesMap.get(key);
        if (foxTreeNodes == null) {
            XSDSchema xsdSchema = wizard.getXSDSchema();
            if (xsdSchema == null) {
                return new ArrayList<FOXTreeNode>();
            }
            foxTreeNodes = TreeUtil.getFoxTreeNodesByRootNode(xsdSchema, selectedRootNode, resolved);
            foxNodesMap.put(key, foxTreeNodes);
        }

        return foxTreeNodes;
    }

    protected void updateTreeNodes(String xmlFilePath) {
        if (xmlFilePath == null || "".equals(xmlFilePath)) {
            return;
        }
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            xmlFilePath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, xmlFilePath));
        }
        updateRootNodes(updateXSDSchema(xmlFilePath), false);
    }

    protected ATreeNode getAdaptRootNode(List<ATreeNode> rootNodes) {
        ATreeNode rootNode = null;
        if (rootNodes == null || rootNodes.size() == 0) {
            return rootNode;
        }
        if (rootNodes.size() > 0) {
            XMLFileNode selectedNode = getConnection().getRoot().get(0);
            if (selectedNode != null) {
                String xmlPath = selectedNode.getXMLPath();
                if (xmlPath != null && xmlPath.length() > 0) {
                    xmlPath = xmlPath.substring(xmlPath.lastIndexOf("/") + 1); //$NON-NLS-1$
                    for (int i = 0; i < rootNodes.size(); i++) {
                        ATreeNode node = rootNodes.get(i);
                        if (xmlPath.equals(node.getValue())) {
                            rootNode = node;
                            break;
                        }
                    }
                }
            }
        }
        return rootNode;
    }

    protected String getXSDXMLFilePath() {
        if (getConnection().getFileContent() == null || getConnection().getFileContent().length == 0) {
            return null;
        }
        byte[] bytes = getConnection().getFileContent();
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return null;
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp"; //$NON-NLS-1$
        String fileName = ""; //$NON-NLS-1$
        if (getConnection().getXmlFilePath() != null && XmlUtil.isXMLFile(getConnection().getXmlFilePath())) {
            fileName = StringUtil.TMP_XML_FILE;
        } else if (getConnection().getXmlFilePath() != null && XmlUtil.isXSDFile(getConnection().getXmlFilePath())) {
            fileName = StringUtil.TMP_XSD_FILE;
        }
        File temfile = new File(temPath + File.separator + fileName);

        if (!temfile.exists()) {
            try {
                temfile.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(temfile);
            outStream.write(bytes);
            outStream.close();
        } catch (FileNotFoundException e1) {
            ExceptionHandler.process(e1);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        String tempXmlXsdPath = temfile.getPath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
            tempXmlXsdPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, tempXmlXsdPath));
        }

        return tempXmlXsdPath;
    }

    protected ATreeNode getDefaultRootNode(List<ATreeNode> rootNodes) {
        if (rootNodes != null && rootNodes.size() > 0) {
            EList<XMLFileNode> root = getConnection().getRoot();
            XMLFileNode selectedNode = null;
            if (root != null && root.size() > 0) {
                selectedNode = root.get(0);
            } else {
                EList<XMLFileNode> loop = getConnection().getLoop();
                if (loop != null && loop.size() > 0) {
                    selectedNode = loop.get(0);
                }
            }
            if (selectedNode != null) {
                String xmlPath = selectedNode.getXMLPath();
                if (xmlPath != null && xmlPath.length() > 0) {
                    xmlPath = xmlPath.substring(xmlPath.lastIndexOf("/") + 1);
                    for (int i = 0; i < rootNodes.size(); i++) {
                        ATreeNode node = rootNodes.get(i);
                        if (xmlPath.equals(node.getValue())) {
                            return node;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void redrawLinkers() {
    }

    @Override
    public void updateConnection() {
    }

    @Override
    public void updateStatus() {
    }

    @Override
    public List<FOXTreeNode> getTreeData() {
        return null;
    }

    @Override
    public void setSelectedText(String label) {
    }

    @Override
    public MetadataTable getMetadataTable() {
        return null;
    }

    @Override
    public TableViewer getSchemaViewer() {
        return null;
    }

    @Override
    public MetadataTable getMetadataOutputTable() {
        return null;
    }

}
