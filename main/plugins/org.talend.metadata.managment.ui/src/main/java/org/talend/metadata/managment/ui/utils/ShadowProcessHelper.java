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
package org.talend.metadata.managment.ui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.WSDLParameter;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.repository.model.preview.IPreview;
import org.talend.core.repository.model.preview.LDAPSchemaBean;
import org.talend.core.repository.model.preview.WSDLSchemaBean;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.preview.AsynchronousPreviewHandler;
import org.talend.metadata.managment.ui.preview.ProcessDescription;

/**
 * Create a ProcessDescription to use in the step2 & step3 of CSV File Wizard on Shadow mode.
 * 
 * $Id: ShadowProcessHelper.java 51244 2010-11-15 03:28:34Z cli $
 * 
 */
public class ShadowProcessHelper {

    private static Logger log = Logger.getLogger(ShadowProcessHelper.class);

    /*
     * record the current preview.
     */
    private static IPreview currentPreview = null;

    private static final String[] TEXT_ENCLOSURE_DATA = { TalendQuoteUtils.addQuotes("\""), TalendQuoteUtils.addQuotes("\'"), //$NON-NLS-1$ //$NON-NLS-2$
            TalendQuoteUtils.addQuotes("\\\\") }; //$NON-NLS-1$

    private static final String[] ESCAPE_CHAR_DATA = { TalendQuoteUtils.addQuotes("\""), TalendQuoteUtils.addQuotes("\'"), //$NON-NLS-1$ //$NON-NLS-2$
            TalendQuoteUtils.addQuotes("\\\\") }; //$NON-NLS-1$

    public static void forceStopPreview() {
        if (currentPreview != null) {
            currentPreview.stopLoading();
            currentPreview = null;
        }
    }

    /**
     * Create a ProcessDescription and set it width the value of FileConnection. Particularity : field FieldSeparator,
     * RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param FileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final FileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();

        processDescription.setFilepath(TalendQuoteUtils.addQuotes(PathUtils.getPortablePath(connection.getFilePath())));

        processDescription.setServer(TalendQuoteUtils.addQuotes(connection.getServer()));

        processDescription.setRowSeparator(connection.getRowSeparatorValue());

        processDescription.setFieldSeparator(connection.getFieldSeparatorValue());

        processDescription.setEncoding(connection.getEncoding());

        // we make differences between Pattern in DELIMITED, CSV and REGEX FileConnection
        if (connection.getEscapeChar() != null || connection.getTextEnclosure() != null) {
            processDescription.setPattern(connection.getFieldSeparatorValue());
        } else {
            processDescription.setPattern(connection.getFieldSeparatorValue());
        }

        processDescription.setHeaderRow(getFilePropertyValue(connection.getHeaderValue()));
        processDescription.setFooterRow(getFilePropertyValue(connection.getFooterValue()));
        processDescription.setLimitRows(getFilePropertyValue(connection.getLimitValue()));

        processDescription.setCSVOption(connection.isCsvOption());
        String escapeCharValue = getValueFromArray(connection.getEscapeChar(), ESCAPE_CHAR_DATA);
        if (escapeCharValue != null && !connection.getEscapeChar().equals("Empty")) //$NON-NLS-1$
        {
            processDescription.setEscapeCharacter(escapeCharValue);
        } else {
            processDescription.setEscapeCharacter(TalendQuoteUtils.addQuotes("")); //$NON-NLS-1$
        }

        String textEnclosureValue = getValueFromArray(connection.getTextEnclosure(), TEXT_ENCLOSURE_DATA);
        if (textEnclosureValue != null && !connection.getTextEnclosure().equals("Empty")) //$NON-NLS-1$
        {
            processDescription.setTextEnclosure(textEnclosureValue);
        } else {
            processDescription.setTextEnclosure(TalendQuoteUtils.addQuotes("")); //$NON-NLS-1$
        }

        processDescription.setRemoveEmptyRow(connection.isRemoveEmptyRow());
        processDescription.setEncoding(TalendQuoteUtils.addQuotes(connection.getEncoding()));

        return processDescription;
    }

    private static int getFilePropertyValue(String value) {
        if (value == null) {
            return 0;
        }
        int i = 0;
        try {
            i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            //
        }
        return i;
    }

    public static ProcessDescription getProcessDescription(final SalesforceSchemaConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        return processDescription;

    }

    public static String getValueFromArray(String value, String[] array) {
        if (value == null || array.length == 0) {
            return null;
        }
        for (String str : array) {
            if (value.equals(str)) {
                return str;
            }
        }
        return null;
    }

    /**
     * Create a ProcessDescription and set it width the value of XmlFileConnection.
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param XmlFileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final XmlFileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        processDescription.setFilepath(TalendQuoteUtils.addQuotes(PathUtils.getPortablePath(connection.getXmlFilePath())));
        processDescription.setLoopQuery(TalendQuoteUtils.addQuotes(connection.getSchema().get(0).getAbsoluteXPathQuery()));
        if (connection.getSchema().get(0).getLimitBoucle() != null
                && !("").equals(connection.getSchema().get(0).getLimitBoucle()) //$NON-NLS-1$
                && (connection.getSchema().get(0).getLimitBoucle().intValue()) != 0) {
            processDescription.setLoopLimit(connection.getSchema().get(0).getLimitBoucle());
        }

        List<Map<String, String>> mapping = new ArrayList<Map<String, String>>();

        List<SchemaTarget> schemaTargets = connection.getSchema().get(0).getSchemaTargets();

        if (schemaTargets != null && !schemaTargets.isEmpty()) {
            Iterator<SchemaTarget> iterate = schemaTargets.iterator();
            while (iterate.hasNext()) {
                SchemaTarget schemaTarget = iterate.next();
                Map<String, String> lineMapping = new HashMap<String, String>();
                lineMapping.put("QUERY", TalendQuoteUtils.addQuotes(schemaTarget.getRelativeXPathQuery())); //$NON-NLS-1$ 
                mapping.add(lineMapping);
            }
        }
        processDescription.setMapping(mapping);
        if (connection.getEncoding() != null && !("").equals(connection.getEncoding())) { //$NON-NLS-1$
            processDescription.setEncoding(TalendQuoteUtils.addQuotes(connection.getEncoding()));
        } else {
            processDescription.setEncoding(TalendQuoteUtils.addQuotes("UTF-8")); //$NON-NLS-1$
        }

        return processDescription;
    }

    public static ProcessDescription getProcessDescription(final FileExcelConnection connection) {

        ProcessDescription processDescription = new ProcessDescription();

        processDescription.setFilepath(TalendQuoteUtils.addQuotes(PathUtils.getPortablePath(connection.getFilePath())));

        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();

        IMetadataTable table = new MetadataTable();

        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        // for bug 13364
        String firstColumn = connection.getFirstColumn();
        String lastColumn = connection.getLastColumn();
        List<String> sheetColumns = connection.getSheetColumns();

        int first = 1;
        if (firstColumn != null && !"".equals(firstColumn.trim())) {
            first = Integer.parseInt(firstColumn.trim());
        }
        int last = 0;
        if (lastColumn != null && !"".equals(lastColumn.trim())) {
            last = Integer.parseInt(lastColumn.trim());
        }
        if (last <= 0 || last > sheetColumns.size()) {
            last = sheetColumns.size();
        }

        List<String> newSchemaColumns = new ArrayList<String>();
        for (int i = first - 1; i < last; i++) {
            newSchemaColumns.add(sheetColumns.get(i));
        }

        if (newSchemaColumns != null && !newSchemaColumns.isEmpty()) {
            Iterator<String> iterate = newSchemaColumns.iterator();
            int i = 0;
            while (iterate.hasNext()) {
                i++;
                IMetadataColumn iMetadataColumn = new MetadataColumn();
                iMetadataColumn.setLabel(iterate.next());
                iMetadataColumn.setKey(false);
                iMetadataColumn.setLength(0);
                iMetadataColumn.setNullable(false);
                iMetadataColumn.setType("String"); //$NON-NLS-1$
                iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                schema.add(iMetadataColumn);
            }
        } else {

            IMetadataColumn iMetadataDn = new MetadataColumn();
            iMetadataDn.setLabel("A"); //$NON-NLS-1$
            iMetadataDn.setKey(false);
            iMetadataDn.setLength(0);
            iMetadataDn.setNullable(false);
            iMetadataDn.setType("String"); //$NON-NLS-1$
            iMetadataDn.setTalendType("id_String"); //$NON-NLS-1$

            schema.add(iMetadataDn);
        }

        table.setTableName("tFileInputExcel"); //$NON-NLS-1$
        table.setListColumns(schema);
        tableSchema.add(table);

        processDescription.setSchema(tableSchema);

        return processDescription;
    }

    /**
     * Create a ProcessDescription and set it width the value of LdifFileConnection.
     * 
     * This method is usefull to adapt a processDescription before run the shadow process.
     * 
     * @param LdifFileConnection
     * 
     * @return ProcessDescription
     */
    public static ProcessDescription getProcessDescription(final LdifFileConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        processDescription.setFilepath(TalendQuoteUtils.addQuotes(PathUtils.getPortablePath(connection.getFilePath())));
        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();

        IMetadataTable table = new MetadataTable();

        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        if (connection.getValue() != null && !connection.getValue().isEmpty()) {
            Iterator<String> iterate = connection.getValue().iterator();

            while (iterate.hasNext()) {

                IMetadataColumn iMetadataColumn = new MetadataColumn();
                String name = iterate.next();
                iMetadataColumn.setLabel(name.replaceAll("-", "_")); //$NON-NLS-1$ //$NON-NLS-2$
                iMetadataColumn.setOriginalDbColumnName(name);
                iMetadataColumn.setKey(false);
                iMetadataColumn.setLength(0);
                iMetadataColumn.setNullable(false);
                iMetadataColumn.setType("String"); //$NON-NLS-1$
                iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                schema.add(iMetadataColumn);
            }

        } else {

            IMetadataColumn iMetadataDn = new MetadataColumn();
            iMetadataDn.setLabel("dn"); //$NON-NLS-1$
            iMetadataDn.setKey(false);
            iMetadataDn.setLength(0);
            iMetadataDn.setNullable(false);
            iMetadataDn.setType("String"); //$NON-NLS-1$
            iMetadataDn.setTalendType("id_String"); //$NON-NLS-1$

            schema.add(iMetadataDn);
        }

        table.setTableName("tFileInputLDIF"); //$NON-NLS-1$
        table.setListColumns(schema);
        tableSchema.add(table);
        processDescription.setSchema(tableSchema);

        // PTODO cantoine : create encoding field for LDIF fileConnection
        processDescription.setEncoding(TalendQuoteUtils.addQuotes("UTF-8")); //$NON-NLS-1$

        return processDescription;
    }

    /**
     * parse a file describe by a fileConnection in XmlArray. Simple method to run the shadow process from the
     * fileConnection.
     * 
     * @param fileConnection
     * @return xmlArray
     * @throws CoreException
     */
    public static CsvArray getCsvArray(final FileConnection fileConnection, String type) throws CoreException {
        return getCsvArray(getProcessDescription(fileConnection), type);
    }

    /**
     * parse a file describe by a processDescription in XmlArray.
     * 
     * @param processDescription
     * @return xmlArray
     */
    public static CsvArray getCsvArray(final ProcessDescription processDescription, String type) throws CoreException {

        CsvArray csvArray = null;

        IPreview preview = createPreview();

        if (preview != null) {
            csvArray = preview.preview(processDescription, type);
        }
        return csvArray;
    }

    /**
     * parse a file describe by a processDescription in XmlArray.
     * 
     * @param processDescription
     * @return xmlArray
     */
    public static CsvArray getCsvArray(final ProcessDescription processDescription, String type, boolean errorOutoutAsException)
            throws CoreException {

        CsvArray csvArray = null;

        IPreview preview = createPreview();

        if (preview != null) {
            csvArray = preview.preview(processDescription, type, errorOutoutAsException);
        }
        return csvArray;
    }

    /**
     * DOC amaumont Comment method "createPreview".
     * 
     * @param configurationElements
     * @return
     * @throws CoreException
     */
    private static IPreview createPreview() throws CoreException {
        return createPreview(null);
    }

    /**
     * DOC amaumont Comment method "createPreview".
     * 
     * @param configurationElements
     * @return
     * @throws CoreException
     */
    private static IPreview createPreview(String type) throws CoreException {
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        // use the org.talend.repository.filepreview_provider
        IConfigurationElement[] configurationElements = registry
                .getConfigurationElementsFor("org.talend.core.runtime.filepreview_provider"); //$NON-NLS-1$
        // When start a new preview. need stop before preview.
        forceStopPreview();

        IPreview preview = null;
        if (configurationElements.length > 0) {
            preview = (IPreview) configurationElements[0].createExecutableExtension("class"); //$NON-NLS-1$
        }

        for (IConfigurationElement configurationElement : configurationElements) {
            String fileType = configurationElement.getAttribute("type"); //$NON-NLS-1$

            // to do this just make sure the code behaviour is same like before
            if (type == null && fileType != null) {
                // fileType is not null which means this previewer is for some specified file type, not good for the
                // current type(null value, means should use the default)
                continue;
            } else if (type != null) { // want to create a specified previewer for a specified type
                if (fileType == null) { // means this is a gereral previewer, not good for a specified type
                    continue;
                } else if (!fileType.equals(type)) {
                    continue;
                }
            }

            IPreview pre = (IPreview) configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
            if (!PluginChecker.isOnlyTopLoaded() && !pre.isTopPreview()) {
                preview = pre;
            }
        }

        if (preview == null) {
            log.error(Messages.getString("ShadowProcessHelper.logError.previewIsNull01") //$NON-NLS-1$
                    + Messages.getString("ShadowProcessHelper.logError.previewIsNull02")); //$NON-NLS-1$
        }
        currentPreview = preview;
        return preview;
    }

    public static AsynchronousPreviewHandler<CsvArray> createPreviewHandler(String type) throws CoreException {
        IPreview preview = createPreview(type);
        return new AsynchronousPreviewHandler<CsvArray>(preview);
    }

    public static AsynchronousPreviewHandler<CsvArray> createPreviewHandler() throws CoreException {
        return createPreviewHandler(null);
    }

    /**
     * Administrator Comment method "getProcessDescription".
     * 
     * @param connection
     * @return
     */
    public static ProcessDescription getProcessDescription(LDAPSchemaConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();

        IMetadataTable table = new MetadataTable();

        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();

        if (connection.getValue() != null && !connection.getValue().isEmpty()) {
            Iterator<String> iterate = connection.getValue().iterator();

            while (iterate.hasNext()) {
                IMetadataColumn iMetadataColumn = new MetadataColumn();
                String name = iterate.next();
                iMetadataColumn.setLabel(name.replaceAll("-", "_")); //$NON-NLS-1$ //$NON-NLS-2$
                iMetadataColumn.setOriginalDbColumnName(name);
                iMetadataColumn.setKey(false);
                iMetadataColumn.setLength(0);
                iMetadataColumn.setNullable(false);
                iMetadataColumn.setType("String"); //$NON-NLS-1$
                iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                schema.add(iMetadataColumn);
            }
        } else {

            IMetadataColumn iMetadataDn = new MetadataColumn();
            iMetadataDn.setLabel("dn"); //$NON-NLS-1$
            iMetadataDn.setKey(false);
            iMetadataDn.setLength(0);
            iMetadataDn.setNullable(false);
            iMetadataDn.setType("String"); //$NON-NLS-1$
            iMetadataDn.setTalendType("id_String"); //$NON-NLS-1$

            schema.add(iMetadataDn);
        }

        table.setTableName("tLDAPInput"); //$NON-NLS-1$
        table.setListColumns(schema);
        tableSchema.add(table);
        processDescription.setSchema(tableSchema);

        LDAPSchemaBean bean = new LDAPSchemaBean();
        // TODO: added properties here...
        bean.setHost(TalendQuoteUtils.addQuotes(connection.getHost().replaceAll("\\\\", "\\\\\\\\"))); //$NON-NLS-1$ //$NON-NLS-2$
        bean.setPort(connection.getPort());// int
        bean.setEncryMethod(TalendQuoteUtils.addQuotes(connection.getEncryptionMethodName()));
        bean.setAuthen(connection.isUseAuthen());
        bean.setAuthMethod(TalendQuoteUtils.addQuotes(connection.getProtocol()));
        String bindPrincipal = connection.getBindPrincipal();
        if (bindPrincipal != null) {
            bean.setUser(TalendQuoteUtils.addQuotes(bindPrincipal.replaceAll("\\\\", "\\\\\\\\"))); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String bindPassword = connection.getValue(connection.getBindPassword(), false);
        if (bindPassword != null) {
            bean.setPasswd(TalendQuoteUtils.addQuotes(bindPassword.replaceAll("\\\\", "\\\\\\\\"))); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String selectedDN = connection.getSelectedDN();
        if (selectedDN != null) {
            bean.setBaseDN(TalendQuoteUtils.addQuotes(selectedDN.replaceAll("\\\\", "\\\\\\\\"))); //$NON-NLS-1$ //$NON-NLS-2$
        }

        bean.setReferrals(connection.getReferrals());

        bean.setAliasDereferenring(connection.getAliases());

        bean.setMultiValueSeparator(TalendQuoteUtils.addQuotes(",")); //$NON-NLS-1$

        bean.setFilter(TalendQuoteUtils.addQuotes(connection.getFilter()));

        bean.setCountLimit(connection.getCountLimit()); // int
        bean.setTimeOutLimit(connection.getTimeOutLimit());// int

        processDescription.setLdapSchemaBean(bean);

        processDescription.setEncoding(TalendQuoteUtils.addQuotes("UTF-8")); //$NON-NLS-1$

        return processDescription;
    }

    public static ProcessDescription getProcessDescription(WSDLSchemaConnection connection) {
        ProcessDescription processDescription = new ProcessDescription();
        List<IMetadataTable> tableSchema = new ArrayList<IMetadataTable>();

        IMetadataTable table = new MetadataTable();
        IMetadataTable outtable = new MetadataTable();
        List<IMetadataColumn> schema = new ArrayList<IMetadataColumn>();
        if (connection.isIsInputModel()) {
            if (connection.getValue() != null && !connection.getValue().isEmpty()) {
                Iterator<String> iterate = connection.getValue().iterator();

                while (iterate.hasNext()) {
                    IMetadataColumn iMetadataColumn = new MetadataColumn();
                    iMetadataColumn.setLabel(iterate.next());
                    iMetadataColumn.setKey(false);
                    iMetadataColumn.setLength(0);
                    iMetadataColumn.setNullable(false);
                    iMetadataColumn.setType("String"); //$NON-NLS-1$
                    iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                    schema.add(iMetadataColumn);
                }
            } else {

                IMetadataColumn iMetadataDn = new MetadataColumn();
                iMetadataDn.setLabel(connection.getMethodName());
                iMetadataDn.setKey(false);
                iMetadataDn.setLength(0);
                iMetadataDn.setNullable(false);
                iMetadataDn.setType("String"); //$NON-NLS-1$
                iMetadataDn.setTalendType("id_String"); //$NON-NLS-1$

                schema.add(iMetadataDn);
            }

            table.setTableName("tWebServiceInput"); //$NON-NLS-1$
            table.setListColumns(schema);
            tableSchema.add(table);
        } else {
            if (connection.getParameterValue() != null && !connection.getParameterValue().isEmpty()) {
                List para = connection.getParameterValue();
                for (int i = 0; i < para.size(); i++) {
                    WSDLParameter parameter = (WSDLParameter) para.get(i);
                    if (parameter.getExpression() != null) {
                        IMetadataColumn iMetadataColumn = new MetadataColumn();
                        iMetadataColumn.setLabel(parameter.getExpression());
                        iMetadataColumn.setKey(false);
                        iMetadataColumn.setLength(0);
                        iMetadataColumn.setNullable(false);
                        iMetadataColumn.setType("String"); //$NON-NLS-1$
                        iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                        schema.add(iMetadataColumn);
                    }
                }
                table.setTableName("tWebServiceInput");
                table.setListColumns(schema);
                tableSchema.add(table);
            }
            if (connection.getOutputParameter() != null && !connection.getOutputParameter().isEmpty()) {
                List para = connection.getOutputParameter();
                for (int i = 0; i < para.size(); i++) {
                    WSDLParameter parameter = (WSDLParameter) para.get(i);
                    if (parameter.getColumn() != null) {
                        IMetadataColumn iMetadataColumn = new MetadataColumn();
                        iMetadataColumn.setLabel(parameter.getColumn());
                        iMetadataColumn.setKey(false);
                        iMetadataColumn.setLength(0);
                        iMetadataColumn.setNullable(false);
                        iMetadataColumn.setType("String"); //$NON-NLS-1$
                        iMetadataColumn.setTalendType("id_String"); //$NON-NLS-1$

                        schema.add(iMetadataColumn);
                    }
                }
                outtable.setTableName("OUTPUT");
                outtable.setListColumns(schema);
                tableSchema.add(outtable);
            }
        }

        processDescription.setSchema(tableSchema);
        WSDLSchemaBean bean = new WSDLSchemaBean();
        // TODO: added properties here..
        if (connection.isIsInputModel()) {
            bean.setWslUrl(TalendQuoteUtils.addQuotes(connection.getWSDL()));
        } else {
            bean.setWslUrl(connection.getWSDL());
        }
        bean.setEndpointURI(TalendQuoteUtils.addQuotes(connection.getEndpointURI()));
        bean.setMethod(TalendQuoteUtils.addQuotes(connection.getMethodName()));
        bean.setNeedAuth(connection.isNeedAuth());
        bean.setUserName(TalendQuoteUtils.addQuotes(connection.getUserName()));
        bean.setPassword(TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false)));
        bean.setParameters(connection.getParameters());
        bean.setUseProxy(connection.isUseProxy());
        bean.setProxyHost(TalendQuoteUtils.addQuotes(connection.getProxyHost()));
        bean.setProxyPort(TalendQuoteUtils.addQuotes(connection.getProxyPort()));
        bean.setProxyUser(TalendQuoteUtils.addQuotes(connection.getProxyUser()));
        bean.setProxyPassword(TalendQuoteUtils.addQuotes(connection.getValue(connection.getProxyPassword(), false)));
        bean.setTimeOut(connection.getTimeOut());
        bean.setIsInputModel(connection.isIsInputModel());
        bean.setServerName(connection.getServerName());
        bean.setServerNS(connection.getServerNameSpace());
        bean.setPortName(connection.getPortName());
        bean.setPortNS(connection.getPortNameSpace());
        processDescription.setWsdlSchemaBean(bean);
        if (connection.getEncoding() != null && !connection.getEncoding().equals("")) { //$NON-NLS-1$
            processDescription.setEncoding(connection.getEncoding());
        } else {
            processDescription.setEncoding(TalendQuoteUtils.addQuotes("UTF-8")); //$NON-NLS-1$
        }

        return processDescription;
    }
}
