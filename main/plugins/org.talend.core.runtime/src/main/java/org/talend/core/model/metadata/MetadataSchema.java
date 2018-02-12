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
package org.talend.core.model.metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.runtime.xml.XSDValidator;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.cwm.helper.TaggedValueHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import orgomg.cwm.objectmodel.core.TaggedValue;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Metadata Schema.
 * 
 * $Id: MetadataSchema.java 51271 2010-11-15 08:40:42Z nrousseau $
 * 
 */
public class MetadataSchema {

    /**
     * 
     */
    // private static final String CORE_PLUGIN_ID = "org.talend.core";

    private static final String CORE_RUNTIME_PLUGIN_ID = "org.talend.core.runtime";

    private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage"; //$NON-NLS-1$

    private static final String SCHEMA_XSD = "talend_metadata_columns_schema.xsd"; //$NON-NLS-1$

    private static final String TARGETSCHEMA_XSD = "talend_targetschema_columns_schema.xsd"; //$NON-NLS-1$

    private static final String SCHEMA_VALIDATOR = "http://java.sun.com/xml/jaxp/properties/schemaSource"; //$NON-NLS-1$

    protected static MetadataSchema instance = new MetadataSchema();

    /**
     * default constructor. Must not be used
     */
    protected MetadataSchema() {

    }

    /**
     * Load Metadatas form a file.
     * 
     * @param file to read datas
     * @param oldTable precedent table, will be cloned
     * @return IMetadataTable setted with datas from file
     * @throws IOException if file cannot be read
     * @throws SAXException if sax exception occured
     * @throws ParserConfigurationException if dom exception occured
     */
    @Deprecated
    public static IMetadataTable loadMetadaFromFile(final File file, IMetadataTable oldTable)
            throws ParserConfigurationException, SAXException, IOException {
        final IMetadataTable table = new MetadataTable();
        table.setComment(oldTable.getComment());
        table.setId(oldTable.getId());
        table.setLabel(oldTable.getLabel());
        table.setParent(oldTable.getParent());
        table.setTableName(oldTable.getTableName());
        final List<IMetadataColumn> listColumns = initializeColumns(file);
        table.setListColumns(listColumns);
        return table;
    }

    /**
     * Load MetadataColumn from a file.
     * 
     * @param file file to load
     * @return List MetadataColumn to set
     * @throws ParserConfigurationException if dom exception occured
     * @throws SAXException if sax exception occured
     * @throws IOException if file cannot be read
     */
    public static List<org.talend.core.model.metadata.builder.connection.MetadataColumn> loadMetadataColumnFromFile(
            final File file) throws ParserConfigurationException, SAXException, IOException {
        final List<org.talend.core.model.metadata.builder.connection.MetadataColumn> listColumns = initializeColumns2(file);
        return listColumns;
    }

    /**
     * Load SchemaTarget from a file.
     * 
     * @param file file to load
     * @return List SchemaTarget to set
     * @throws ParserConfigurationException if dom exception occured
     * @throws SAXException if sax exception occured
     * @throws IOException if file cannot be read
     */
    public static List<org.talend.core.model.metadata.builder.connection.SchemaTarget> loadTargetSchemaColumnFromFile(
            final File file) throws ParserConfigurationException, SAXException, IOException {
        final List<org.talend.core.model.metadata.builder.connection.SchemaTarget> listSchemaTargets = initializeSchemaTarget2(file);
        return listSchemaTargets;
    }

    /**
     * Initalize MetadataColumns available in a file.
     * 
     * @param file where MeatadataColumns data are available
     * @return IMetadataTable setted with datas from file
     * @throws ParserConfigurationException if dom exception occured
     * @throws SAXException if sax exception occured
     * @throws IOException if file cannot be read
     */
    @Deprecated
    public static List<IMetadataColumn> initializeColumns(final File file) throws ParserConfigurationException, SAXException,
            IOException {
        return instance.initializeAllColumns(file);
    }

    /**
     * qzhang Comment method "initializeAllColumns".
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public List<IMetadataColumn> initializeAllColumns(final File file) throws IOException, ParserConfigurationException,
            SAXException {
        final List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        Set<String> columnsAlreadyAdded = new HashSet<String>();
        if (file != null) {
            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(SCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            final Document document = XSDValidator.checkXSD(file, schema);
            final NodeList nodes = document.getElementsByTagName("column"); //$NON-NLS-1$
            for (int i = 0; i < nodes.getLength(); i++) {
                IMetadataColumn metadataColumn = new MetadataColumn();
                final Node nodetoParse = nodes.item(i);
                final NamedNodeMap nodeMap = nodetoParse.getAttributes();
                metadataColumn = initializeOneColumn(metadataColumn, nodeMap);

                if (!columnsAlreadyAdded.contains(metadataColumn.getLabel())) {
                    listColumns.add(metadataColumn);
                    columnsAlreadyAdded.add(metadataColumn.getLabel());
                }
            }
        }
        return listColumns;
    }

    /**
     * qzhang Comment method "initializeColumn".
     * 
     * @param metadataColumn
     * @param nodeMap
     * @return
     */
    protected IMetadataColumn initializeOneColumn(final IMetadataColumn metadataColumn, final NamedNodeMap nodeMap) {
        final Node label = nodeMap.getNamedItem("label"); //$NON-NLS-1$
        final Node originalDbColumnName = nodeMap.getNamedItem("originalDbColumnName"); //$NON-NLS-1$
        final Node key = nodeMap.getNamedItem("key"); //$NON-NLS-1$
        final Node type = nodeMap.getNamedItem("talendType"); //$NON-NLS-1$
        final Node sourceType = nodeMap.getNamedItem("type"); //$NON-NLS-1$
        final Node length = nodeMap.getNamedItem("length"); //$NON-NLS-1$
        final Node nullable = nodeMap.getNamedItem("nullable"); //$NON-NLS-1$
        final Node precision = nodeMap.getNamedItem("precision"); //$NON-NLS-1$
        final Node defaultValue = nodeMap.getNamedItem("default"); //$NON-NLS-1$
        final Node comment = nodeMap.getNamedItem("comment"); //$NON-NLS-1$
        final Node pattern = nodeMap.getNamedItem("pattern"); //$NON-NLS-1$
        final Node originalLength = nodeMap.getNamedItem("originalLength");//$NON-NLS-1$
        // see feature 4456

        String nodeValue = MetadataToolHelper.validateColumnName(label.getNodeValue(), 0);

        metadataColumn.setLabel(nodeValue);
        metadataColumn.setKey(Boolean.parseBoolean(key.getNodeValue()));
        metadataColumn.setTalendType(getNewTalendType(type.getNodeValue()));
        if (sourceType != null) {
            metadataColumn.setType(sourceType.getNodeValue());
        }
        if (originalDbColumnName != null && !"".equals(originalDbColumnName.getNodeValue().toString())) { // hywang for //$NON-NLS-1$
            // 0007919
            metadataColumn.setOriginalDbColumnName(originalDbColumnName.getNodeValue());
        } else {
            metadataColumn.setOriginalDbColumnName(nodeValue);
        }
        if (originalLength != null && originalLength.getNodeValue() != null) {
            try {
                metadataColumn.setOriginalLength(Integer.parseInt(originalLength.getNodeValue()));
            } catch (final NumberFormatException e) {
                metadataColumn.setOriginalLength(null);
            }
        } else {
            metadataColumn.setOriginalLength(null);
        }

        if (length.getNodeValue() != null) {
            try {
                metadataColumn.setLength(Integer.parseInt(length.getNodeValue()));
            } catch (final NumberFormatException e) {
                metadataColumn.setLength(null);
            }
        } else {
            metadataColumn.setLength(null);
        }
        if (precision.getNodeValue() != null) {
            try {
                metadataColumn.setPrecision(Integer.parseInt(precision.getNodeValue()));
            } catch (final NumberFormatException e) {
                metadataColumn.setPrecision(null);
            }
        } else {
            metadataColumn.setPrecision(null);
        }

        metadataColumn.setNullable(Boolean.parseBoolean(nullable.getNodeValue()));
        metadataColumn.setDefault(defaultValue.getNodeValue());
        metadataColumn.setComment(comment.getNodeValue());
        if (pattern.getNodeValue() != null) {
            metadataColumn.setPattern(pattern.getNodeValue());
        }
        return metadataColumn;
    }

    /**
     * Initalize MetadataColumns available in a file.
     * 
     * @param file where MeatadataColumns data are available
     * @return IMetadataTable setted with datas from file
     * @throws ParserConfigurationException if dom exception occured
     * @throws SAXException if sax exception occured
     * @throws IOException if file cannot be read
     */
    private static List<org.talend.core.model.metadata.builder.connection.MetadataColumn> initializeColumns2(final File file)
            throws ParserConfigurationException, SAXException, IOException {
        return loadMetadataColumnsAndDbmsIdFromFile(file).getMetadataColumns();
    }

    public static MetadataColumnsAndDbmsId<org.talend.core.model.metadata.builder.connection.MetadataColumn> loadMetadataColumnsAndDbmsIdFromFile(
            final File file) throws ParserConfigurationException, SAXException, IOException {
        final List<org.talend.core.model.metadata.builder.connection.MetadataColumn> listColumns = new ArrayList<org.talend.core.model.metadata.builder.connection.MetadataColumn>();
        String dbmsId = null;
        if (file != null) {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(SCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
            fabrique.setAttribute(SCHEMA_VALIDATOR, schema);
            fabrique.setValidating(true);

            final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            final Document document = analyseur.parse(file);
            dbmsId = document.getDocumentElement().getAttribute("dbmsId"); //$NON-NLS-1$

            final NodeList nodes = document.getElementsByTagName("column"); //$NON-NLS-1$
            Set<String> columnsAlreadyAdded = new HashSet<String>();

            for (int i = 0; i < nodes.getLength(); i++) {
                final org.talend.core.model.metadata.builder.connection.MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE
                        .createMetadataColumn();
                final Node nodetoParse = nodes.item(i);
                final NamedNodeMap nodeMap = nodetoParse.getAttributes();
                final Node label = nodeMap.getNamedItem("label"); //$NON-NLS-1$
                final Node key = nodeMap.getNamedItem("key"); //$NON-NLS-1$
                final Node type = nodeMap.getNamedItem("talendType"); //$NON-NLS-1$
                final Node sourceType = nodeMap.getNamedItem("type"); //$NON-NLS-1$
                final Node length = nodeMap.getNamedItem("length"); //$NON-NLS-1$
                final Node precision = nodeMap.getNamedItem("precision"); //$NON-NLS-1$
                final Node nullable = nodeMap.getNamedItem("nullable"); //$NON-NLS-1$
                final Node defaultValue = nodeMap.getNamedItem("default"); //$NON-NLS-1$
                final Node comment = nodeMap.getNamedItem("comment"); //$NON-NLS-1$
                final Node pattern = nodeMap.getNamedItem("pattern"); //$NON-NLS-1$
                final Node originalLength = nodeMap.getNamedItem("originalLength");
                final Node originalField = nodeMap.getNamedItem("originalDbColumnName"); //$NON-NLS-1$

                final Node impliedDecimal = nodeMap.getNamedItem("ImpliedDecimal"); //$NON-NLS-1$
                final Node signed = nodeMap.getNamedItem("Signed"); //$NON-NLS-1$

                // final Node function = nodeMap.getNamedItem("pattern"); //$NON-NLS-1$
                // final Node parameter = nodeMap.getNamedItem("pattern"); //$NON-NLS-1$
                // final Node preview = nodeMap.getNamedItem("pattern"); //$NON-NLS-1$

                String nodeValue = MetadataToolHelper.validateColumnName(label.getNodeValue(), 0);
                // metadataColumn.setLabel(nodeValue);
                metadataColumn.setLabel(nodeValue);
                // TDI-17966:Copybook schema wizard doesn't retrieve the copybook schema correctly
                UniqueStringGenerator<org.talend.core.model.metadata.builder.connection.MetadataColumn> uniqueLabelGenerator = new UniqueStringGenerator<org.talend.core.model.metadata.builder.connection.MetadataColumn>(
                        metadataColumn.getLabel(), listColumns) {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
                     */
                    @Override
                    protected String getBeanString(org.talend.core.model.metadata.builder.connection.MetadataColumn bean) {
                        return bean.getLabel();
                    }

                };
                metadataColumn.setLabel(uniqueLabelGenerator.getUniqueString());

                metadataColumn.setKey(Boolean.parseBoolean(key.getNodeValue()));
                metadataColumn.setTalendType(getNewTalendType(type.getNodeValue()));

                if (sourceType != null) {
                    metadataColumn.setSourceType(sourceType.getNodeValue());
                }
                if (length.getNodeValue() != null) {
                    try {
                        metadataColumn.setLength(Integer.parseInt(length.getNodeValue()));
                    } catch (final NumberFormatException e) {
                        metadataColumn.setLength(-1);
                    }
                } else {
                    metadataColumn.setLength(-1);
                }
                if (precision.getNodeValue() != null) {
                    try {
                        metadataColumn.setPrecision(Integer.parseInt(precision.getNodeValue()));
                    } catch (final NumberFormatException e) {
                        metadataColumn.setPrecision(-1);
                    }
                } else {
                    metadataColumn.setPrecision(-1);
                }
                metadataColumn.setNullable(Boolean.parseBoolean(nullable.getNodeValue()));
                metadataColumn.setDefaultValue(defaultValue.getNodeValue());
                metadataColumn.setComment(comment.getNodeValue());
                if (originalLength != null && originalLength.getNodeValue() != null) {
                    try {
                        metadataColumn.setOriginalLength(Integer.parseInt(originalLength.getNodeValue()));
                    } catch (final NumberFormatException e) {
                        metadataColumn.setOriginalLength(-1);
                    }

                } else {
                    metadataColumn.setOriginalLength(-1);
                }
                if (pattern.getNodeValue() != null) {
                    metadataColumn.setPattern(pattern.getNodeValue());
                }
                if (originalField.getNodeValue() != null) {
                    metadataColumn.setOriginalField(originalField.getNodeValue());
                } else {
                    metadataColumn.setOriginalField(metadataColumn.getLabel());
                }
                if (!columnsAlreadyAdded.contains(metadataColumn.getLabel())) {
                    listColumns.add(metadataColumn);
                    columnsAlreadyAdded.add(metadataColumn.getLabel());
                }

                if (impliedDecimal != null && impliedDecimal.getNodeValue() != null) {
                    TaggedValue impliedDc = TaggedValueHelper.createTaggedValue(
                            "additionalField:" + impliedDecimal.getNodeName(), impliedDecimal.getNodeValue()); //$NON-NLS-1$
                    metadataColumn.getTaggedValue().add(impliedDc);
                }

                if (signed != null && signed.getNodeValue() != null) {
                    TaggedValue sign = TaggedValueHelper.createTaggedValue(
                            "additionalField:" + signed.getNodeName(), signed.getNodeValue()); //$NON-NLS-1$
                    metadataColumn.getTaggedValue().add(sign);
                }
            }
        }
        return new MetadataColumnsAndDbmsId<org.talend.core.model.metadata.builder.connection.MetadataColumn>(listColumns, dbmsId);
    }

    /**
     * Initalize SchemaTargets available in a file.
     * 
     * @param file where SchemaTargets data are available
     * @return MetadataSchema setted with datas from file
     * @throws ParserConfigurationException if dom exception occured
     * @throws SAXException if sax exception occured
     * @throws IOException if file cannot be read
     */
    private static List<org.talend.core.model.metadata.builder.connection.SchemaTarget> initializeSchemaTarget2(final File file)
            throws ParserConfigurationException, SAXException, IOException {
        final List<org.talend.core.model.metadata.builder.connection.SchemaTarget> listSchemaTargets = new ArrayList<org.talend.core.model.metadata.builder.connection.SchemaTarget>();
        if (file != null) {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(TARGETSCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
            fabrique.setAttribute(SCHEMA_VALIDATOR, schema);
            fabrique.setValidating(true);

            final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            final Document document = analyseur.parse(file);
            final NodeList nodes = document.getElementsByTagName("schemaTargets"); //$NON-NLS-1$
            for (int i = 0; i < nodes.getLength(); i++) {
                final org.talend.core.model.metadata.builder.connection.SchemaTarget schemaTarget = ConnectionFactory.eINSTANCE
                        .createSchemaTarget();
                final Node nodetoParse = nodes.item(i);
                final NamedNodeMap nodeMap = nodetoParse.getAttributes();
                final Node xPathQuery = nodeMap.getNamedItem("XPathQuery"); //$NON-NLS-1$
                final Node tagName = nodeMap.getNamedItem("TagName"); //$NON-NLS-1$

                schemaTarget.setRelativeXPathQuery(xPathQuery.getNodeValue());
                schemaTarget.setTagName(tagName.getNodeValue());
                listSchemaTargets.add(schemaTarget);
            }
        }
        return listSchemaTargets;
    }

    public static boolean saveMetadataColumnToFile(File file,
            org.talend.core.model.metadata.builder.connection.MetadataTable table) throws IOException,
            ParserConfigurationException {
        return saveMetadataColumnToFile(file, table, null);
    }

    /**
     * Export MetadataColumn to the specified file.
     * 
     * @param file to save
     * @param table to export
     * @return boolean result
     * @throws IOException if file cannot be saved
     * @throws ParserConfigurationException if dom is not fully respected
     */
    public static boolean saveMetadataColumnToFile(File file,
            org.talend.core.model.metadata.builder.connection.MetadataTable table, String dbmsId) throws IOException,
            ParserConfigurationException {

        if (file != null) {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(SCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
            fabrique.setAttribute(SCHEMA_VALIDATOR, schema);
            fabrique.setValidating(true);

            final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            Document document = analyseur.newDocument();
            Element racine = document.createElement("schema"); //$NON-NLS-1$
            document.appendChild(racine);

            if (dbmsId != null && dbmsId.trim() != "") { //$NON-NLS-1$
                Attr idAttr = document.createAttribute("dbmsId"); //$NON-NLS-1$
                idAttr.setNodeValue(dbmsId);
                racine.setAttributeNode(idAttr);
            }

            for (Object list : table.getColumns()) {
                org.talend.core.model.metadata.builder.connection.MetadataColumn metadataColumn = (org.talend.core.model.metadata.builder.connection.MetadataColumn) list;
                Element column = document.createElement("column"); //$NON-NLS-1$
                racine.appendChild(column);

                Attr label = document.createAttribute("label"); //$NON-NLS-1$
                label.setNodeValue(metadataColumn.getLabel());
                column.setAttributeNode(label);

                Attr originalField = document.createAttribute("originalDbColumnName"); //$NON-NLS-1$
                originalField.setNodeValue(metadataColumn.getOriginalField());
                column.setAttributeNode(originalField);

                Attr key = document.createAttribute("key"); //$NON-NLS-1$
                key.setNodeValue(String.valueOf(metadataColumn.isKey()));
                column.setAttributeNode(key);

                Attr talendType = document.createAttribute("talendType"); //$NON-NLS-1$
                talendType.setNodeValue(metadataColumn.getTalendType());
                column.setAttributeNode(talendType);

                if (metadataColumn.getSourceType() != null) {
                    Attr sourceType = document.createAttribute("type"); //$NON-NLS-1$
                    sourceType.setNodeValue(metadataColumn.getSourceType());
                    column.setAttributeNode(sourceType);
                }

                Attr length = document.createAttribute("length"); //$NON-NLS-1$
                length.setNodeValue(String.valueOf(metadataColumn.getLength()));
                column.setAttributeNode(length);

                Attr precision = document.createAttribute("precision"); //$NON-NLS-1$
                precision.setNodeValue(String.valueOf(metadataColumn.getPrecision()));
                column.setAttributeNode(precision);

                Attr nullable = document.createAttribute("nullable"); //$NON-NLS-1$
                nullable.setNodeValue(String.valueOf(metadataColumn.isNullable()));
                column.setAttributeNode(nullable);

                Attr pattern = document.createAttribute("pattern"); //$NON-NLS-1$
                pattern.setNodeValue(String.valueOf(metadataColumn.getPattern()));
                column.setAttributeNode(pattern);

                Attr defaultValue = document.createAttribute("default"); //$NON-NLS-1$
                defaultValue.setNodeValue(metadataColumn.getDefaultValue());
                column.setAttributeNode(defaultValue);

                Attr comment = document.createAttribute("comment"); //$NON-NLS-1$
                comment.setNodeValue(metadataColumn.getComment());
                column.setAttributeNode(comment);
            }

            // save document to file
            saveDocumentByEncoding(document, file);

            return true;
        }
        return false;
    }

    /**
     * Export SchemaTarget to the specified file.
     * 
     * @param file to save
     * @param table to export
     * @return boolean result
     * @throws IOException if file cannot be saved
     * @throws ParserConfigurationException if dom is not fully respected
     */
    public static boolean saveSchemaTargetToFile(File file, org.talend.core.model.metadata.builder.connection.MetadataSchema table)
            throws IOException, ParserConfigurationException {

        if (file != null) {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(TARGETSCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
            fabrique.setAttribute(SCHEMA_VALIDATOR, schema);
            fabrique.setValidating(true);

            final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            Document document = analyseur.newDocument();
            Element racine = document.createElement("schema"); //$NON-NLS-1$
            document.appendChild(racine);

            for (Object list : table.getSchemaTargets()) {
                SchemaTarget schemaTarget = (SchemaTarget) list;
                Element column = document.createElement("schemaTargets"); //$NON-NLS-1$
                racine.appendChild(column);

                Attr xPathQuery = document.createAttribute("XPathQuery"); //$NON-NLS-1$
                xPathQuery.setNodeValue(schemaTarget.getRelativeXPathQuery());
                column.setAttributeNode(xPathQuery);

                Attr tagName = document.createAttribute("TagName"); //$NON-NLS-1$
                tagName.setNodeValue(String.valueOf(schemaTarget.getTagName()));
                column.setAttributeNode(tagName);

            }

            // use specific Xerces class to write DOM-data to a file:
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(file));
            serializer.serialize(document);
            return true;
        }
        return false;
    }

    /**
     * Export MetadataColumn to the specified file.
     * 
     * @param file to save
     * @param table to export
     * @return boolean result
     * @throws IOException if file cannot be saved
     * @throws ParserConfigurationException if dom is not fully respected
     */
    @Deprecated
    public static boolean saveMetadataColumnToFile(File file, IMetadataTable table) throws IOException,
            ParserConfigurationException {
        return instance.saveColumnsToFile(file, table);
    }

    /**
     * qzhang Comment method "saveColumnsToFile".
     * 
     * @param file
     * @param table
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public boolean saveColumnsToFile(File file, IMetadataTable table) throws IOException, ParserConfigurationException {
        if (file != null) {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            final Bundle b = Platform.getBundle(CORE_RUNTIME_PLUGIN_ID);
            final URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(SCHEMA_XSD), null));
            final File schema = new File(url.getPath());

            fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
            fabrique.setAttribute(SCHEMA_VALIDATOR, schema);
            fabrique.setValidating(true);

            final DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            Document document = analyseur.newDocument();
            Element racine = document.createElement("schema"); //$NON-NLS-1$
            document.appendChild(racine);

            if (table.getDbms() != null && table.getDbms().trim() != "") { //$NON-NLS-1$
                Attr dbmsId = document.createAttribute("dbmsId"); //$NON-NLS-1$
                dbmsId.setNodeValue(table.getDbms());
                racine.setAttributeNode(dbmsId);
            }

            for (IMetadataColumn metadataColumn : table.getListColumns()) {
                Element column = document.createElement("column"); //$NON-NLS-1$
                racine.appendChild(column);
                saveOneColumn(document, metadataColumn, column);
            }

            // save document
            saveDocumentByEncoding(document, file);

            return true;
        }
        return false;
    }

    /**
     * qzhang Comment method "saveOneColumn".
     * 
     * @param document
     * @param metadataColumn
     * @param column
     */
    protected void saveOneColumn(Document document, IMetadataColumn metadataColumn, Element column) {
        Attr label = document.createAttribute("label"); //$NON-NLS-1$
        label.setNodeValue(metadataColumn.getLabel());
        column.setAttributeNode(label);

        Attr dbColumnName = document.createAttribute("originalDbColumnName"); //$NON-NLS-1$
        dbColumnName.setNodeValue(metadataColumn.getOriginalDbColumnName());
        column.setAttributeNode(dbColumnName);

        Attr key = document.createAttribute("key"); //$NON-NLS-1$
        key.setNodeValue(String.valueOf(metadataColumn.isKey()));
        column.setAttributeNode(key);

        Attr talendType = document.createAttribute("talendType"); //$NON-NLS-1$
        talendType.setNodeValue(metadataColumn.getTalendType());
        column.setAttributeNode(talendType);

        if (metadataColumn.getType() != null) {
            Attr sourceType = document.createAttribute("type"); //$NON-NLS-1$
            sourceType.setNodeValue(metadataColumn.getType());
            column.setAttributeNode(sourceType);
        }

        Attr length = document.createAttribute("length"); //$NON-NLS-1$
        if (metadataColumn.getLength() == null) {
            length.setNodeValue("-1"); //$NON-NLS-1$
        } else {
            length.setNodeValue(String.valueOf(metadataColumn.getLength()));
        }
        column.setAttributeNode(length);

        Attr originalLength = document.createAttribute("originalLength"); //$NON-NLS-1$
        if (metadataColumn.getOriginalLength() == null) {
            originalLength.setNodeValue("-1"); //$NON-NLS-1$
        } else {
            originalLength.setNodeValue(String.valueOf(metadataColumn.getOriginalLength()));
        }
        column.setAttributeNode(originalLength);

        Attr precision = document.createAttribute("precision"); //$NON-NLS-1$
        if (metadataColumn.getPrecision() == null) {
            precision.setNodeValue("-1"); //$NON-NLS-1$
        } else {
            precision.setNodeValue(String.valueOf(metadataColumn.getPrecision()));
        }

        column.setAttributeNode(precision);

        Attr nullable = document.createAttribute("nullable"); //$NON-NLS-1$
        nullable.setNodeValue(String.valueOf(metadataColumn.isNullable()));
        column.setAttributeNode(nullable);

        Attr pattern = document.createAttribute("pattern"); //$NON-NLS-1$
        pattern.setNodeValue(String.valueOf(metadataColumn.getPattern()));
        column.setAttributeNode(pattern);

        Attr defaultValue = document.createAttribute("default"); //$NON-NLS-1$
        defaultValue.setNodeValue(metadataColumn.getDefault());
        column.setAttributeNode(defaultValue);

        Attr comment = document.createAttribute("comment"); //$NON-NLS-1$
        comment.setNodeValue(metadataColumn.getComment());
        column.setAttributeNode(comment);
    }

    /**
     * 
     * DOC ggu Comment method "getPerlNewType".
     * 
     * @param type
     * @return
     */
    private static String getNewTalendType(String type) {
        // return type;
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            return type;
        default:
            // Return the new types of Perl.
            return PerlTypesManager.getNewTypeName(type);
        }
    }

    /**
     * 
     * DOC ggu Comment method "saveDocumentByEncoding".
     * 
     * @param document
     * @param file
     * @throws IOException
     */
    private static void saveDocumentByEncoding(Document document, File file) throws IOException {
        if (document == null || file == null) {
            return;
        }
        // use specific Xerces class to write DOM-data to a file:
        XMLSerializer serializer = new XMLSerializer();
        OutputFormat outputFormat = new OutputFormat();
        outputFormat.setIndenting(true);
        serializer.setOutputFormat(outputFormat);

        // java.io.FileWriter fileWriter = new java.io.FileWriter(file);
        // serializer.setOutputCharStream(fileWriter);
        // try {
        // // firstly, use the local encoding
        // outputFormat.setEncoding(System.getProperty("file.encoding"));
        //
        // serializer.serialize(document);
        // } catch (java.io.UnsupportedEncodingException e) {
        // // Use default "UTF-8"
        // outputFormat.setEncoding("UTF-8");
        //
        // serializer.serialize(document);
        // }
        //
        // fileWriter.close();
        // fileWriter = null;
        OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        serializer.setOutputCharStream(output);
        serializer.serialize(document);
        output.close();

    }
}
