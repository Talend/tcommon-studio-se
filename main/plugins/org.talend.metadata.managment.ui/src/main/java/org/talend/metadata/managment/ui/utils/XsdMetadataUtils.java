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
package org.talend.metadata.managment.ui.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import orgomg.cwm.resource.record.RecordFactory;
import orgomg.cwm.resource.record.RecordFile;

/**
 * created by hcyi on Aug 29, 2014 Detailled comment
 *
 * related to: class PublishMetadataRunnable
 *
 */
public final class XsdMetadataUtils {

    private static final Pattern PATTERN_TOREPLACE = Pattern.compile("[^a-zA-Z0-9]"); //$NON-NLS-1$

    private static final Pattern PATTERN_PUNCT_EXCEPT_SLASH = Pattern.compile("(?![/])\\p{Punct}");//$NON-NLS-1$

    private static int orderId;

    private static boolean loopElementFound;

    public static void createMetadataFromXSD(QName parameter, File schemaFile) {
        XSDPopulationUtil2 populationUtil = new XSDPopulationUtil2();
        Collection<XmlFileConnectionItem> selectItems = new ArrayList<XmlFileConnectionItem>();
        try {
            createMetadataFromXSD(parameter, "", "", selectItems, schemaFile, populationUtil);//$NON-NLS-1$//$NON-NLS-2$
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createMetadataFromXSD(QName parameter, String connectionLabel, String portTypeName, String operationName,
            File schemaFile) {
        XSDPopulationUtil2 populationUtil = new XSDPopulationUtil2();
        Collection<XmlFileConnectionItem> selectItems = new ArrayList<XmlFileConnectionItem>();
        try {
            createMetadataFromXSD(parameter, connectionLabel, portTypeName, operationName, selectItems, schemaFile,
                    populationUtil);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * DOC hcyi Comment method "createMetadataFromXSD".
     *
     * @param parameter
     * @param portTypeName
     * @param operationName
     * @param schemaFile
     * @param selectItems
     * @param zip
     * @param populationUtil
     * @throws IOException
     */
    public static void createMetadataFromXSD(QName parameter, String portTypeName, String operationName,
            Collection<XmlFileConnectionItem> selectItems, File zip, XSDPopulationUtil2 populationUtil) throws IOException {
        createMetadataFromXSD(parameter, parameter.getLocalPart(), portTypeName, operationName, selectItems, zip, populationUtil);
    }

    /**
     * 
     * DOC wchen Comment method "createMetadataFromXSD".
     * 
     * @param parameter
     * @param connectionLabel
     * @param portTypeName
     * @param operationName
     * @param schemaFile
     * @param selectItems
     * @param fileForInnerContent
     * @param populationUtil
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void createMetadataFromXSD(QName parameter, String connectionLabel, String portTypeName, String operationName,
            Collection<XmlFileConnectionItem> selectItems, File fileForInnerContent, XSDPopulationUtil2 populationUtil)
            throws IOException {
        String targetNameSpace = parameter.getNamespaceURI();

        String name = /* componentName + "_"+ */parameter.getLocalPart();
        XmlFileConnection connection = null;
        Property connectionProperty = null;
        XmlFileConnectionItem connectionItem = null;
        String oldConnectionId = null;
        String oldTableId = null;
        IMetadataTable oldMetadataTable = null;
        Map<String, String> oldTableMap = null;
        if (!selectItems.isEmpty()) {
            boolean needRewrite = false;
            for (XmlFileConnectionItem item : selectItems) {
                connectionProperty = item.getProperty();
                if (connectionProperty.getLabel().equals(name)) {
                    oldConnectionId = connectionProperty.getId();
                    connectionItem = item;
                    connection = (XmlFileConnection) connectionItem.getConnection();
                    needRewrite = true;
                    Set<MetadataTable> tables = ConnectionHelper.getTables(connection);
                    MetadataTable oldTable = null;
                    if (tables.size() > 0) {
                        oldTable = tables.toArray(new MetadataTable[0])[0];
                        oldTableId = oldTable.getId();
                        oldMetadataTable = ConvertionHelper.convert(oldTable);
                    }
                    oldTableMap = RepositoryUpdateManager.getOldTableIdAndNameMap(connectionItem, oldTable, false);
                    break;
                }
            }
            if (!needRewrite && !isNameValidInXmlFileConnection(parameter, portTypeName, operationName)) {
                return;
            }
        }
        connection = ConnectionFactory.eINSTANCE.createXmlFileConnection();
        connection.setName(ERepositoryObjectType.METADATA_FILE_XML.getKey());
        connectionItem = PropertiesFactory.eINSTANCE.createXmlFileConnectionItem();
        connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setLabel(connectionLabel);
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(connection);
        connection.setInputModel(false);
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(fileForInnerContent);
        connection.setFileContent(byteArray.getInnerContent());

        connection.setXmlFilePath(fileForInnerContent.getName());
        connection.setTargetNameSpace(targetNameSpace);
        try {
            XSDSchema xsdSchema = null;
            if (XmlUtil.isWSDLFile(fileForInnerContent.getName())) {
                xsdSchema = populationUtil.getXSDSchemaFromNamespace(targetNameSpace);
            } else {
                xsdSchema = populationUtil.getXSDSchema(fileForInnerContent.getAbsolutePath());
            }
            if (xsdSchema == null) {
                return;
            }
            List<ATreeNode> rootNodes = populationUtil.getAllRootNodes(xsdSchema);
            ATreeNode node = null;
            // try to find the root element needed from XSD file.
            // note: if there is any prefix, it will get the node with the first correct name, no matter the prefix.
            // once the we can get the correct prefix value from the wsdl, this code should be modified.
            for (ATreeNode curNode : rootNodes) {
                String curName = (String) curNode.getValue();
                if (curName.contains(":")) { //$NON-NLS-1$
                    // if with prefix, don't care about it for now, just compare the name.
                    if (curName.split(":")[1].equals(name)) { //$NON-NLS-1$
                        node = curNode;
                        break;
                    }
                } else if (curName.equals(name)) {
                    node = curNode;
                    break;
                }
            }
            node = populationUtil.getSchemaTree(xsdSchema, node);
            orderId = 1;
            loopElementFound = false;
            if (ConnectionHelper.getTables(connection).isEmpty()) {
                MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
                if (oldTableId != null) {
                    table.setId(oldTableId);
                } else {
                    table.setId(ProxyRepositoryFactory.getInstance().getNextId());
                }
                RecordFile record = (RecordFile) ConnectionHelper.getPackage(connection.getName(), connection, RecordFile.class);
                if (record != null) { // hywang
                    PackageHelper.addMetadataTable(table, record);
                } else {
                    RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                    newrecord.setName(connection.getName());
                    ConnectionHelper.addPackage(newrecord, connection);
                    PackageHelper.addMetadataTable(table, newrecord);
                }
            }
            boolean haveElement = false;
            for (Object curNode : node.getChildren()) {
                if (((ATreeNode) curNode).getType() == ATreeNode.ELEMENT_TYPE) {
                    haveElement = true;
                    break;
                }
            }
            fillRootInfo(connection, node, "", !haveElement); //$NON-NLS-1$
        } catch (OdaException e) {
            ExceptionHandler.process(e);
        } catch (URISyntaxException e1) {
            ExceptionHandler.process(e1);
        }
        // save
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        connectionProperty.setId(factory.getNextId());
        try {
            // http://jira.talendforge.org/browse/TESB-3655 Remove possible
            // schema prefix
            String folderPath = getImportedXmlSchemaPath(targetNameSpace, portTypeName, operationName);
            IPath path = new Path(folderPath);
            factory.create(connectionItem, path, true); // consider this as migration will overwrite the old metadata if
            // existing in the same path
            if (oldConnectionId != null) {
                connectionItem.getProperty().setId(oldConnectionId);
                factory.save(connectionItem);
            }
            propagateSchemaChange(oldMetadataTable, oldTableMap, connection, connectionItem);
            ProxyRepositoryFactory.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (URISyntaxException e) {
            ExceptionHandler.process(e);
        }
    }

    private static void propagateSchemaChange(final IMetadataTable oldMetaTable, final Map<String, String> oldTableMap,
            final XmlFileConnection connection, final XmlFileConnectionItem connectionItem) {
        if (oldMetaTable == null) {
            return;
        }
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                MetadataTable newTable = ConnectionHelper.getTables(connection).toArray(new MetadataTable[0])[0];
                RepositoryUpdateManager.updateSingleSchema(connectionItem, newTable, oldMetaTable, oldTableMap);
            }
        });
    }

    private static void fillRootInfo(XmlFileConnection connection, ATreeNode node, String path, boolean inLoop) {
        XMLFileNode xmlNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
        xmlNode.setXMLPath(path + '/' + node.getValue());
        xmlNode.setOrder(orderId);
        orderId++;
        MappingTypeRetriever retriever;
        String nameWithoutPrefixForColumn;
        String curName = (String) node.getValue();
        if (curName.contains(":")) { //$NON-NLS-1$
            nameWithoutPrefixForColumn = curName.split(":")[1]; //$NON-NLS-1$
        } else {
            nameWithoutPrefixForColumn = curName;
        }
        retriever = MetadataTalendType.getMappingTypeRetriever("xsd_id"); //$NON-NLS-1$
        String originalDataType = node.getOriginalDataType();
        if (originalDataType != null && !originalDataType.startsWith("xs:")) { //$NON-NLS-1$
            originalDataType = "xs:" + originalDataType; //$NON-NLS-1$
        }
        xmlNode.setAttribute("attri"); //$NON-NLS-1$
        xmlNode.setType(retriever.getDefaultSelectedTalendType(node.getDataType()));
        MetadataColumn column = null;
        MetadataTable metadataTable = ConnectionHelper.getTables(connection).toArray(new MetadataTable[0])[0];
        switch (node.getType()) {
        case ATreeNode.ATTRIBUTE_TYPE:
            // fix for TDI-20390 and TDI-20671 ,XMLPath for attribute should only store attribute name but not full
            // xpath
            column = ConnectionFactory.eINSTANCE.createMetadataColumn();
            column.setTalendType(retriever.getDefaultSelectedTalendType(originalDataType));
            String uniqueName = extractColumnName(nameWithoutPrefixForColumn, metadataTable.getColumns());
            column.setLabel(uniqueName);
            xmlNode.setXMLPath("" + node.getValue()); //$NON-NLS-1$
            xmlNode.setRelatedColumn(uniqueName);
            metadataTable.getColumns().add(column);
            break;
        case ATreeNode.ELEMENT_TYPE:
            boolean haveElementOrAttributes = false;
            for (Object curNode : node.getChildren()) {
                if (((ATreeNode) curNode).getType() != ATreeNode.NAMESPACE_TYPE) {
                    haveElementOrAttributes = true;
                    break;
                }
            }
            if (!haveElementOrAttributes) {
                column = ConnectionFactory.eINSTANCE.createMetadataColumn();
                column.setTalendType(retriever.getDefaultSelectedTalendType(originalDataType));
                uniqueName = extractColumnName(nameWithoutPrefixForColumn, metadataTable.getColumns());
                column.setLabel(uniqueName);
                xmlNode.setAttribute("branch"); //$NON-NLS-1$
                xmlNode.setRelatedColumn(uniqueName);
                metadataTable.getColumns().add(column);
            } else {
                xmlNode.setAttribute("main"); //$NON-NLS-1$
            }
            break;
        case ATreeNode.NAMESPACE_TYPE:
            xmlNode.setAttribute("ns"); //$NON-NLS-1$
            // specific for namespace... no path set, there is only the prefix value.
            // this value is saved now in node.getDataType()
            xmlNode.setXMLPath(node.getDataType());
            xmlNode.setDefaultValue((String) node.getValue());
            break;
        case ATreeNode.OTHER_TYPE:
            break;
        }
        boolean subElementsInLoop = inLoop;
        // will try to get the first element (branch or main), and set it as loop.
        if ((!loopElementFound && path.split("/").length == 2 && node.getType() == ATreeNode.ELEMENT_TYPE) || subElementsInLoop) { //$NON-NLS-1$
            connection.getLoop().add(xmlNode);
            loopElementFound = true;
            subElementsInLoop = true;
        } else {
            connection.getRoot().add(xmlNode);
        }
        if (node.getChildren().length > 0) {
            for (Object curNode : node.getChildren()) {
                if (!path.contains("/" + (String) node.getValue() + "/")) { //$NON-NLS-1$ //$NON-NLS-2$
                    fillRootInfo(connection, (ATreeNode) curNode, path + '/' + node.getValue(), subElementsInLoop);
                }
            }
        }
    }

    private static String extractColumnName(String currentExpr, List<MetadataColumn> fullSchemaTargetList) {
        String columnName = currentExpr.startsWith("@") ? currentExpr.substring(1) : currentExpr; //$NON-NLS-1$
        columnName = PATTERN_TOREPLACE.matcher(columnName).replaceAll("_"); //$NON-NLS-1$
        UniqueStringGenerator<MetadataColumn> uniqueStringGenerator = new UniqueStringGenerator<MetadataColumn>(columnName,
                fullSchemaTargetList) {

            @Override
            protected String getBeanString(MetadataColumn bean) {
                return bean.getLabel();
            }
        };
        columnName = uniqueStringGenerator.getUniqueString();
        return columnName;
    }

    public static boolean isNameValidInXmlFileConnection(QName parameter, String portTypeName, String operationName) {
        try {
            XmlFileConnectionItem item = PropertiesFactory.eINSTANCE.createXmlFileConnectionItem();
            XmlFileConnection connection = ConnectionFactory.eINSTANCE.createXmlFileConnection();
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(ProxyRepositoryFactory.getInstance().getNextId());
            property.setLabel(parameter.getLocalPart());
            property.setVersion(VersionUtils.DEFAULT_VERSION);
            //
            ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
            String folderPath = getImportedXmlSchemaPath(parameter.getNamespaceURI(), portTypeName, operationName);
            itemState.setPath(folderPath);
            item.setConnection(connection);
            item.setProperty(property);
            item.setState(itemState);
            return ProxyRepositoryFactory.getInstance().isNameAvailable(property.getItem(), parameter.getLocalPart());
        } catch (PersistenceException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static String replaceAllLimited(String input) {
        if (input == null) {
            return input;
        }
        return PATTERN_PUNCT_EXCEPT_SLASH.matcher(input).replaceAll("-");//$NON-NLS-1$
    }

    public static String getImportedXmlSchemaPath(String namespace, String portType, String operation) throws URISyntaxException {
        if (namespace == null || portType == null || operation == null) {
            throw new URISyntaxException(namespace + " " + portType + " " + operation, //$NON-NLS-1$//$NON-NLS-2$
                    "The arguments can't be empty, please check");//$NON-NLS-1$
        }
        StringBuilder builder = new StringBuilder(replaceAllLimited(new URI(namespace).getRawSchemeSpecificPart()));
        if (portType.equals("") && operation.equals("")) {//$NON-NLS-1$//$NON-NLS-2$
            return builder.toString();
        }
        if (builder.length() > 0) {
            while (builder.charAt(0) == '/') {
                builder.deleteCharAt(0);
            }
            if (builder.charAt(builder.length() - 1) != '/') {
                builder.append('/');
            }
        }
        builder.append(portType).append('/').append(operation);
        return builder.toString();
    }
}
