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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.avro.Schema;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.components.IComponentConstants;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.impl.ConnectionItemImpl;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.IRoutinesService;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.utils.KeywordsValidator;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;

import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * ggu class global comment. Detailled comment
 */
public final class MetadataToolHelper {

    private static final int MIN = 192;

    private static final int MAX = 255;

    /**
     * 
     * DOC wchen Comment method "getMetadataTableFromConnection".
     * 
     * @param conn
     * @return
     * @deprecated deprecated by getMetadataTableFromConnection(final Connection conn,String tableName) , sap tableName
     * might be sapfunction/tableType/tableName
     */
    @Deprecated
    public static EList getMetadataTableFromConnection(final Connection conn) {
        return getMetadataTableFromConnection(conn, null);
    }

    public static EList getMetadataTableFromConnection(final Connection conn, String tableName) {
        if (conn == null) {
            return null;
        }
        // return conn.getTables();
        EList tables = new BasicEList();
        if (conn instanceof SAPConnection) {
            final SAPConnection sapConnection = (SAPConnection) conn;
            // get tables from connection and funciton output by default if tableName is null
            if (tableName == null) {
                // add sap tables
                tables.addAll(ConnectionHelper.getTables(conn));
                // add function param output tables
                final List<SAPFunctionUnit> functions = sapConnection.getFuntions();
                for (int i = 0; i < functions.size(); i++) {
                    tables.addAll((functions.get(i)).getTables());
                }
                return tables;
            } else {
                String[] split = tableName.split("/");
                if (split.length == 1) {
                    // only add sap tables
                    tables.addAll(ConnectionHelper.getTables(conn));
                } else if (split.length == 3) {
                    // only add function param tables by function label and table type
                    String functionLabel = split[0];
                    String tableType = split[1];
                    final List<SAPFunctionUnit> functions = sapConnection.getFuntions();
                    for (int i = 0; i < functions.size(); i++) {
                        SAPFunctionUnit function = functions.get(i);
                        if (functionLabel.equals(function.getLabel())) {
                            if (MetadataSchemaType.INPUT.name().equals(tableType)) {
                                tables.addAll(function.getInputTables());
                            } else {
                                tables.addAll(function.getTables());
                            }

                        }

                    }
                }

            }
        } else {
            // tables in connection
            tables.addAll(ConnectionHelper.getTables(conn));
        }
        return tables;

    }

    public static ConnectionItem getConnectionItemByItemId(String itemId, boolean deleted) {
        if (itemId == null || itemId.equals("")) { //$NON-NLS-1$
            return null;
        }
        final IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            final IRepositoryViewObject lastVersion = proxyRepositoryFactory.getLastVersion(itemId);
            if (lastVersion != null) {
                if (!deleted && proxyRepositoryFactory.getStatus(lastVersion) == ERepositoryStatus.DELETED) {
                    return null;
                }
                final Item item = lastVersion.getProperty().getItem();
                if (item != null && item instanceof ConnectionItem) {
                    return (ConnectionItem) item;
                }
            }
        } catch (PersistenceException e) {
            //
        }
        return null;
    }

    /**
     * qzhang Comment method "isBoolean".
     * 
     * @param value
     * @return
     */
    public static boolean isBoolean(final String value) {
        return value.equals(JavaTypesManager.BOOLEAN.getId());
    }

    /**
     * qzhang Comment method "isDirectory".
     * 
     * @param value
     * @return
     */
    public static boolean isDirectory(final String value) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            return value.equals(JavaTypesManager.DIRECTORY.getId());
        } else {
            return value.equals(ContextParameterJavaTypeManager.PERL_DIRECTORY);
        }
    }

    public static boolean isList(final String value) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            return value.equals(JavaTypesManager.VALUE_LIST.getId());
        } else {
            return value.equals(ContextParameterJavaTypeManager.PERL_VALUE_LIST);
        }
    }

    /**
     * qzhang Comment method "isDate".
     * 
     * @param value
     * @return
     */
    public static boolean isDate(final String value) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            return value.equals(JavaTypesManager.DATE.getId());
        } else {
            return value.equals(ContextParameterJavaTypeManager.PERL_DAY);
        }

    }

    /**
     * qzhang Comment method "isFile".
     * 
     * @param value
     * @return
     */
    public static boolean isFile(final String value) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            return value.equals(JavaTypesManager.FILE.getId());
        } else {
            return value.equals(ContextParameterJavaTypeManager.PERL_FILE);
        }
    }

    public static boolean isPassword(final String value) {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            return value.equals(JavaTypesManager.PASSWORD.getId());
        } else {
            return value.equals(ContextParameterJavaTypeManager.PERL_PASSWORD);
        }

    }

    public static boolean isValidSchemaName(String name) {
        if (name.contains(" ")) {
            return false;
        }
        return true;
    }

    public static boolean isValidColumnName(String name) {
        if (name == null) {
            return false;
        }
        if (!JavaConventions.validateFieldName(name, JavaCore.VERSION_1_6, JavaCore.VERSION_1_6).isOK()) {
            return false;
        }

        return isAllowSpecificCharacters() || Pattern.matches(RepositoryConstants.COLUMN_NAME_PATTERN, name);
    }

    private static boolean isAllowSpecificCharacters() {
        IEclipsePreferences coreUIPluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        return coreUIPluginNode.getBoolean(IRepositoryPrefConstants.ALLOW_SPECIFIC_CHARACTERS_FOR_SCHEMA_COLUMNS, false);
    }

    /**
     * 
     * qli Comment method "validateColumnName".
     * 
     * 
     */
    public static String validateColumnName(final String columnName, final int index) {
        String originalColumnName = new String(mapSpecialChar(columnName));
        final String underLine = "_"; //$NON-NLS-1$

        boolean isKeyword = KeywordsValidator.isKeyword(originalColumnName);

        String returnedColumnName = "";
        if (!isKeyword) {
            boolean isAllowSpecific = isAllowSpecificCharacters();

            // match RepositoryConstants.COLUMN_NAME_VALIDATED
            for (int i = 0; i < originalColumnName.length(); i++) {
                Character car = originalColumnName.charAt(i);
                // MOD yyin TDQ-4929 20120330
                if (car.toString().getBytes().length == 1 || !isAllowSpecific) {
                    // first character should have only a-z or A-Z or _
                    // other characters should have only a-z or A-Z or _ or 0-9
                    if (((car >= 'a') && (car <= 'z')) || ((car >= 'A') && (car <= 'Z')) || car == '_'
                            || ((car >= '0') && (car <= '9') && (i != 0))) {
                        returnedColumnName += car;
                    } else {
                        returnedColumnName += underLine;
                    }
                } else {
                    returnedColumnName += car;
                }
            }
        }
        if (isKeyword
                || org.apache.commons.lang.StringUtils.countMatches(returnedColumnName, underLine) > (originalColumnName.length() / 2)) {
            returnedColumnName = "Column" + index; //$NON-NLS-1$
        }

        return returnedColumnName;

    }

    public static String validateColumnName(final String columnName, final int index, List<String> labels) {
        String name = validateColumnName(columnName, index);
        UniqueStringGenerator<String> uniqueStringGenerator = new UniqueStringGenerator<String>(name, labels) {

            @Override
            protected String getBeanString(String bean) {
                return bean;
            }

        };
        return uniqueStringGenerator.getUniqueString();
    }

    /**
     * 
     * hwang Comment method "validateTableName".
     * 
     * 
     */
    public static String validateTableName(String tableName) {
        String originalTableName = new String(tableName);
        tableName = "";
        final String underLine = "_"; //$NON-NLS-1$

        boolean isKeyword = KeywordsValidator.isKeyword(originalTableName);

        boolean isAllowSpecific = isAllowSpecificCharacters();

        for (int i = 0; i < originalTableName.length(); i++) {
            Character car = originalTableName.charAt(i);
            if (car.toString().getBytes().length == 1 || !isAllowSpecific) {
                if (((car >= 'a') && (car <= 'z')) || ((car >= 'A') && (car <= 'Z')) || car == '_'
                        || ((car >= '0') && (car <= '9') && (i != 0))) {
                    tableName += car;
                } else {
                    tableName += underLine;
                }
            } else {
                tableName += car;
            }
        }
        if (isKeyword) {
            return tableName + "_1";
        }
        return tableName;

    }

    /**
     * wzhang Comment method "validataValue".
     */
    public static String validateValue(String columnName) {
        if (columnName == null) {
            return null;
        }
        columnName = mapSpecialChar(columnName);
        final String underLine = "_"; //$NON-NLS-1$
        if (columnName.matches("^\\d.*")) { //$NON-NLS-1$
            columnName = underLine + columnName;
        }

        String testColumnName = columnName.replaceAll("[^a-zA-Z0-9_]", underLine); //$NON-NLS-1$

        if (org.apache.commons.lang.StringUtils.countMatches(testColumnName, underLine) < (columnName.length() / 2)) {
            return testColumnName;
        }
        return columnName;
    }

    /**
     * zwzhao Comment method "validataValue".
     */
    public static String validateValueNoLengthLimit(String columnName) {
        if (columnName == null) {
            return null;
        }
        columnName = mapSpecialChar(columnName);
        final String underLine = "_"; //$NON-NLS-1$
        if (columnName.matches("^\\d.*")) { //$NON-NLS-1$
            columnName = underLine + columnName;
        }

        String testColumnName = columnName.replaceAll("[^a-zA-Z0-9_]", underLine); //$NON-NLS-1$

        return testColumnName;
    }

    /**
     * zli Comment method "validataValue".
     */
    public static String validateValueForDBType(String columnName) {
        if (columnName == null) {
            return null;
        }
        columnName = mapSpecialChar(columnName);
        final String underLine = "_"; //$NON-NLS-1$
        if (columnName.matches("^\\d.*")) { //$NON-NLS-1$
            columnName = underLine + columnName;
        }

        String testColumnName = columnName.replaceAll("[^a-zA-Z 0-9_]", underLine); //$NON-NLS-1$

        if (org.apache.commons.lang.StringUtils.countMatches(testColumnName, underLine) < (columnName.length() / 2)) {
            return testColumnName;
        }
        return columnName;
    }

    /**
     * 
     * qli Comment method "mapSpecialChar".
     * 
     * 
     */
    private  static String mapSpecialChar(String columnName) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRoutinesService.class)) {
            IRoutinesService service = (IRoutinesService) GlobalServiceRegister.getDefault().getService(IRoutinesService.class);
            if (service != null) {
                Vector map = service.getAccents();
                map.setElementAt("AE", 4);//$NON-NLS-1$
                map.setElementAt("OE", 22);//$NON-NLS-1$
                map.setElementAt("UE", 28);//$NON-NLS-1$
                map.setElementAt("ss", 31);//$NON-NLS-1$
                map.setElementAt("ae", 36);//$NON-NLS-1$
                map.setElementAt("oe", 54);//$NON-NLS-1$
                map.setElementAt("ue", 60);//$NON-NLS-1$
                Vector addedMap = new Vector();
                for (int i = 257; i < 304; i++) {
                    addedMap.add(String.valueOf((char) i));
                }
                map.addAll(addedMap);
                map.add("I");//$NON-NLS-1$

                return initSpecificMapping(columnName, map);
            }
        }
        return columnName;
    }

    /**
     * 
     * qli Comment method "initSpecificMapping".
     * 
     * 
     */
    private static String initSpecificMapping(String columnName, Vector map) {
        for (int i = 0; i < columnName.toCharArray().length; i++) {
            int carVal = columnName.charAt(i);
            if (carVal >= MIN && carVal <= MIN + map.size()) {
                String oldVal = String.valueOf(columnName.toCharArray()[i]);
                String newVal = (String) map.get(carVal - MIN);
                if (!(oldVal.equals(newVal))) {
                    columnName = columnName.replaceAll(oldVal, newVal);
                }
            }
        }

        return columnName;
    }

    /**
     * wzhang Comment method "validateSchema".
     */
    public static void validateSchema(String value) {
        if (value == null) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(),
                    Messages.getString("MetadataTool.nullValue"), Messages.getString("MetadataTool.nameNull")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
        if (!isValidSchemaName(value)) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(),
                    Messages.getString("MetadataTool.invalid"), Messages.getString("MetadataTool.schemaInvalid")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
    }

    /**
     * 
     * wzhang Comment method "validateSchemaValue".
     * 
     * @param value
     * @param beanPosition
     * @param list
     * @return
     */
    public static String validateSchemaValue(String value, int beanPosition, List<String> list) {
        if (value == null) {
            return Messages.getString("MetadataTool.schemaNull"); //$NON-NLS-1$
        }
        if (!isValidSchemaName(value)) {
            return Messages.getString("MetadataTool.schemaIn"); //$NON-NLS-1$
        }
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            if (value.equals(list.get(i)) && i != beanPosition) {
                return Messages.getString("MetadataTool.schemaExist"); //$NON-NLS-1$
            }
        }
        return null;
    }

    /**
     * wzhang Comment method "checkSchema".
     */
    public static void checkSchema(Shell shell, KeyEvent event) {
        if ((!Character.isIdentifierIgnorable(event.character)) && (event.character == ' ')) {
            event.doit = false;
            MessageDialog.openError(shell,
                    Messages.getString("MetadataTool.invalidChar"), Messages.getString("MetadataTool.errorMessage")); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public static void copyTable(IMetadataTable source, IMetadataTable target) {
        copyTable(source, target, null);
    }

    public static void copyTable(IMetadataTable source, IMetadataTable target, boolean avoidUsedColumnsFromInput) {
        copyTable(source, target, null, avoidUsedColumnsFromInput);
    }

    /**
     * @author wzhang Comment method "copyTable".
     * @param dbmsid
     * @param source
     * @param target
     */
    public static void copyTable(String dbmsId, IMetadataTable source, IMetadataTable target) {
        copyTable(source, target);
        setDBType(target, dbmsId);
    }

    /**
     * DOC wzhang Comment method "setDBType".
     */
    public static void setDBType(IMetadataTable metaTable, String dbmsid) {
        List<IMetadataColumn> listColumns = metaTable.getListColumns();
        for (IMetadataColumn column : listColumns) {
            String talendType = column.getTalendType();
            String type = column.getType();
            if (dbmsid != null) {
                if (!TypesManager.checkDBType(dbmsid, talendType, type)) {
                    column.setType(TypesManager.getDBTypeFromTalendType(dbmsid, talendType));
                }
            }
        }
    }

    public static void copyTable(IMetadataTable source, IMetadataTable target, String targetDbms) {
        copyTable(source, target, targetDbms, false);
    }

    public static void copyTable(IMetadataTable source, IMetadataTable target, String targetDbms,
            boolean avoidUsedColumnsFromInput) {
        if (source == null || target == null) {
            return;
        }
        List<IMetadataColumn> columnsToRemove = new ArrayList<IMetadataColumn>();
        List<String> readOnlycolumns = new ArrayList<String>();
        for (IMetadataColumn column : target.getListColumns(true)) {
            if (!column.isCustom()) {
                columnsToRemove.add(column);
            }
            if (column.isReadOnly()) {
                readOnlycolumns.add(column.getLabel());
            }
        }
        target.getListColumns().removeAll(columnsToRemove);
        target.getListUnusedColumns().removeAll(columnsToRemove);

        List<IMetadataColumn> columnsTAdd = new ArrayList<IMetadataColumn>();
        for (IMetadataColumn column : source.getListColumns(!avoidUsedColumnsFromInput)) {
            IMetadataColumn targetColumn = target.getColumn(column.getLabel());
            IMetadataColumn newTargetColumn = column.clone();
            if (targetColumn == null) {
                columnsTAdd.add(newTargetColumn);
                newTargetColumn.setReadOnly(target.isReadOnly() || readOnlycolumns.contains(newTargetColumn.getLabel()));
            } else {
                if (!targetColumn.isReadOnly()) {
                    target.getListColumns().remove(targetColumn);
                    newTargetColumn.setCustom(targetColumn.isCustom());
                    newTargetColumn.setCustomId(targetColumn.getCustomId());
                    columnsTAdd.add(newTargetColumn);
                }
            }
        }
        target.getListColumns().addAll(columnsTAdd);
        target.sortCustomColumns();
        target.setLabel(source.getLabel());
        target.setOriginalColumns(source.getOriginalColumns());
        Map<String, String> targetProperties = target.getAdditionalProperties();
        Map<String, String> sourceProperties = source.getAdditionalProperties();
        for (Entry<String, String> entry : sourceProperties.entrySet()) {
            targetProperties.put(entry.getKey(), entry.getValue());
        }
    }

    public static void copyTable(List<IMetadataColumn> sourceColumns, IMetadataTable target, List<IMetadataColumn> targetColumns) {
        if (sourceColumns == null || target == null || targetColumns == null) {
            return;
        }
        List<String> readOnlycolumns = new ArrayList<String>();
        for (IMetadataColumn column : targetColumns) {
            if (column.isReadOnly()) {
                readOnlycolumns.add(column.getLabel());
            }
        }

        List<IMetadataColumn> columnsTAdd = new ArrayList<IMetadataColumn>();
        for (IMetadataColumn column : sourceColumns) {
            IMetadataColumn targetColumn = null;
            for (IMetadataColumn tColumn : targetColumns) {
                if (column.getLabel().equals(tColumn.getLabel())) {
                    targetColumn = tColumn;
                }
            }
            IMetadataColumn newTargetColumn = column.clone();
            if (targetColumn == null) {
                columnsTAdd.add(newTargetColumn);
                newTargetColumn.setReadOnly(target.isReadOnly() || readOnlycolumns.contains(newTargetColumn.getLabel()));
            } else {
                if (!targetColumn.isReadOnly()) {
                    target.getListColumns().remove(targetColumn);
                    newTargetColumn.setCustom(targetColumn.isCustom());
                    newTargetColumn.setCustomId(targetColumn.getCustomId());
                    columnsTAdd.add(newTargetColumn);
                }
            }
        }
        targetColumns.addAll(columnsTAdd);
    }

    // public List getBeanListTest() {
    // org.talend.commons.ui.swt.extended.table.ExtendedTableModel.getBeansList();
    // }

    /**
     * 
     * DOC qli Comment method "copyTable".
     * 
     * @param sourceColumns,target,targetDbms
     * @return
     */
    public static void copyTable(List<IMetadataColumn> sourceColumns, IMetadataTable target, String targetDbms) {
        if (sourceColumns == null || target == null) {
            return;
        }

        Map<IMetadataColumn, String> columnsToRemoveMap = new HashMap<IMetadataColumn, String>();
        List<String> readOnlycolumns = new ArrayList<String>();

        for (IMetadataColumn inputColumn : sourceColumns) {
            for (IMetadataColumn outputcolumn : target.getListColumns()) {
                String outputColumnName = outputcolumn.getLabel();
                if (inputColumn.getLabel().toLowerCase().equals(("" + outputColumnName).toLowerCase())) {
                    columnsToRemoveMap.put(inputColumn, outputColumnName);
                }
                if (outputcolumn.isReadOnly()) {
                    readOnlycolumns.add(outputColumnName);
                }
            }
        }

        List<IMetadataColumn> columnsTAdd = new ArrayList<IMetadataColumn>();
        for (IMetadataColumn column : sourceColumns) {
            // IMetadataColumn targetColumn = target.getColumn(column.getLabel());
            IMetadataColumn targetColumn = null;
            String sameNameIgnoreCaseColumnLabel = columnsToRemoveMap.get(column);
            if (sameNameIgnoreCaseColumnLabel != null && !sameNameIgnoreCaseColumnLabel.trim().isEmpty()) {
                targetColumn = target.getColumn(sameNameIgnoreCaseColumnLabel);
            }
            IMetadataColumn newTargetColumn = column.clone();
            if (targetColumn == null) {
                columnsTAdd.add(newTargetColumn);
                newTargetColumn.setReadOnly(target.isReadOnly() || readOnlycolumns.contains(newTargetColumn.getLabel()));
            } else {
                if (!targetColumn.isReadOnly()) {
                    target.getListColumns().remove(targetColumn);
                    newTargetColumn.setCustom(targetColumn.isCustom());
                    newTargetColumn.setCustomId(targetColumn.getCustomId());
                    columnsTAdd.add(newTargetColumn);
                }
            }
        }
        target.getListColumns().addAll(columnsTAdd);
        target.sortCustomColumns();
    }

    public static MetadataTable getMetadataTableFromRepository(String metaRepositoryId) {
        org.talend.core.model.metadata.builder.connection.Connection connection;

        String[] names = metaRepositoryId.split(" - "); //$NON-NLS-1$
        if (names.length < 2) {
            return null;
        }
        String linkedRepository = names[0];
        String tableName = null;
        if (names.length == 2) {
            tableName = names[1];
        } else if (names.length > 2) {
            tableName = metaRepositoryId.substring(linkedRepository.length() + 3);
        }

        connection = getConnectionFromRepository(linkedRepository);

        if (connection != null) {
            if (connection instanceof SAPConnection) {
                // Changed by Marvin Wang on Jun. 19, 2012 for subtask TDI-21657.
                // return getMetadataTableFromSAPFunction((SAPConnection) connection, metaRepositoryId);
                if (tableName == null) {
                    return null;
                }
                if (tableName.contains("/")) { //$NON-NLS-1$
                    // if tableName contains "/", means the selected table name is from SAPFunction; else it is from SAP
                    // table, then just go the common codes
                    return getMetadataTableFromSAPFunction((SAPConnection) connection, metaRepositoryId);
                }
            }
            Set<MetadataTable> tables = null;
            IGenericWizardService wizardService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
                wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault()
                        .getService(IGenericWizardService.class);
            }
            if (wizardService != null && wizardService.isGenericConnection(connection)) {
                List<MetadataTable> metadataTables = wizardService.getMetadataTables(connection);
                tables = new HashSet<>(metadataTables);
            } else {
                tables = ConnectionHelper.getTables(connection);
            }
            for (Object tableObj : tables) {
                MetadataTable table = (MetadataTable) tableObj;
                if (table.getLabel().equals(tableName)) {
                    return table;
                }
            }
        }
        return null;
    }

    /**
     * Added by Marvin Wang on Jun. 20, 2012 for getting the <code>MetadataTable</code> by given parameters.
     * 
     * @param connectionId
     * @param functionId
     * @param tableName
     * @return
     */
    public static MetadataTable getMetadataTableFromSAPFunction(String connectionId, String functionId, String tableName) {
        org.talend.core.model.metadata.builder.connection.Connection connection;
        if (connectionId != null) {
            connection = getConnectionFromRepository(connectionId);
            if (connection != null && connection instanceof SAPConnection) {
                SAPConnection sapConn = (SAPConnection) connection;
                EList<SAPFunctionUnit> fuctions = sapConn.getFuntions();
                for (Object obj : fuctions) {
                    SAPFunctionUnit function = (SAPFunctionUnit) obj;
                    if (functionId != null && functionId.equals(function.getId())) {
                        for (Object object : function.getTables()) {
                            MetadataTable table = (MetadataTable) object;
                            if (tableName.equals(table.getLabel())) {
                                return table;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static MetadataTable getMetadataTableFromSAPFunction(SAPConnection connection, String repositoryId) {
        String functionName = null;
        String tableName = null;
        String[] names = repositoryId.split(" - "); //$NON-NLS-1$
        if (names.length == 2) {
            functionName = names[0];
            tableName = names[1];
        } else {
            return null;
        }

        String[] split = tableName.split("/"); //$NON-NLS-1$
        if (split.length == 3) {
            functionName = split[0];
            String type = split[1];
            tableName = split[2];
            for (Object obj : connection.getFuntions()) {
                SAPFunctionUnit function = (SAPFunctionUnit) obj;
                if (functionName.equals(function.getLabel())) {
                    List<MetadataTable> tables = null;
                    if (MetadataSchemaType.INPUT.name().equals(type)) {
                        tables = function.getInputTables();
                    } else {
                        tables = function.getTables();
                    }
                    for (Object object : tables) {
                        MetadataTable table = (MetadataTable) object;
                        if (tableName.equals(table.getLabel())) {
                            return table;
                        }
                    }
                }
            }
        }

        for (Object obj : connection.getFuntions()) {
            SAPFunctionUnit function = (SAPFunctionUnit) obj;
            if (functionName.equals(function.getLabel())) {
                for (Object object : function.getTables()) {
                    MetadataTable table = (MetadataTable) object;
                    if (tableName.equals(table.getLabel())) {
                        return table;
                    }
                }
            }
        }
        return null;
    }

    public static org.talend.core.model.metadata.builder.connection.Connection getConnectionFromRepository(String metaRepositoryid) {
        ConnectionItem connItem = getConnectionItemFromRepository(metaRepositoryid);
        if (connItem != null) {
            return connItem.getConnection();
        }
        return null;
    }

    public static ConnectionItem getConnectionItemFromRepository(String metaRepositoryid) {
        if (metaRepositoryid == null) {
            return null;
        }
        String connectionId = metaRepositoryid;
        // some calls can be done either with only the connection Id or with
        // informations from query or table
        String[] names = metaRepositoryid.split(" - "); //$NON-NLS-1$
        if (names.length == 2) {
            connectionId = names[0];
        }

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            IRepositoryViewObject object = factory.getLastVersion(connectionId);
            if (object == null) {
                return null;
            }
            if (factory.getStatus(object) != ERepositoryStatus.DELETED) {
                Item item = object.getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    return (ConnectionItem) item;
                }
            }
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    public static IMetadataTable getMetadataTableFromNodeLabel(INode node, String name) {
        if (node == null || name == null) {
            return null;
        }
        for (IMetadataTable metadata : node.getMetadataList()) {
            if (name.equals(metadata.getLabel())) {
                return metadata;
            }
        }
        return null;
    }

    public static IMetadataTable getMetadataTableFromNodeTableName(INode node, String name) {
        if (node == null || name == null) {
            return null;
        }
        for (IMetadataTable metadata : node.getMetadataList()) {
            // if (name.equals(metadata.getTableName())) {
            if (name.equals(metadata.getLabel()) || name.equals(metadata.getTableName())) {
                return metadata;
            }
        }
        return null;
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    //    private static final String VALIDATE_PATTERN_NAME = "^[a-zA-Z_][a-zA-Z_0-9]*$"; //$NON-NLS-1$

    //    private static final String VALIDATE_PATTERN_SCHEMA_NAME = "^[a-zA-Z_0-9][a-zA-Z_0-9]*$"; //$NON-NLS-1$

    public static List<ColumnNameChanged> getColumnNameChanged(IMetadataTable oldTable, IMetadataTable newTable) {
        List<ColumnNameChanged> columnNameChanged = new ArrayList<ColumnNameChanged>();
        for (int i = 0; i < oldTable.getListColumns().size(); i++) {
            IMetadataColumn originalColumn = oldTable.getListColumns().get(i);
            int oldIndex = oldTable.getListColumns().indexOf(originalColumn);
            // TDI-9847:since the SCD type3field's currentColumn and previousColumn has same ID,when edit schema,we
            // up/down outputSchema's column order,need add index to make a difference to get the right column
            IMetadataColumn clonedColumn = getColumn(newTable, originalColumn, oldIndex);
            if (clonedColumn != null) {
                if (!originalColumn.getLabel().equals(clonedColumn.getLabel())) {
                    columnNameChanged.add(new ColumnNameChanged(originalColumn.getLabel(), clonedColumn.getLabel()));
                }
            }
        }
        return columnNameChanged;
    }

    public static List<ColumnNameChanged> getColumnNameChangedExt(INode changedNode, IMetadataTable oldTable,
            IMetadataTable newTable) {
        if (changedNode == null || oldTable == null || newTable == null) {
            return Collections.EMPTY_LIST;
        }
        List<ColumnNameChanged> columnNameChanged = new ArrayList<ColumnNameChanged>();
        for (int i = 0; i < oldTable.getListColumns().size(); i++) {
            IMetadataColumn originalColumn = oldTable.getListColumns().get(i);
            int oldIndex = oldTable.getListColumns().indexOf(originalColumn);
            IMetadataColumn clonedColumn = getColumn(newTable, originalColumn, oldIndex);
            if (clonedColumn != null) {
                if (!originalColumn.getLabel().equals(clonedColumn.getLabel())) {
                    columnNameChanged.add(new ColumnNameChangedExt(changedNode, originalColumn.getLabel(), clonedColumn
                            .getLabel()));
                }
            }
        }
        return columnNameChanged;
    }

    private static IMetadataColumn getColumn(IMetadataTable table, IMetadataColumn originalColumn, int oldIndex) {
        if (originalColumn.getId() != null) {
            for (IMetadataColumn col : table.getListColumns()) {
                if (originalColumn.getId().equals(col.getId())) {
                    if (oldIndex != table.getListColumns().indexOf(col)) {
                        if (originalColumn.getLabel().equals(col.getLabel())) {
                            return col;
                        } else {
                            continue;
                        }
                    } else {
                        return col;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * DOC qli Comment method "copyTable".
     * 
     * @param sourceColumns,target
     * @return
     */
    public static void copyTable(List<IMetadataColumn> sourceColumns, IMetadataTable target) {
        copyTable(sourceColumns, target, "");
    }

    public static Query getQueryFromRepository(String metaRepositoryId) {
        org.talend.core.model.metadata.builder.connection.Connection connection;

        String[] names = metaRepositoryId.split(" - "); //$NON-NLS-1$
        String name2 = null;
        if (names.length < 2) {
            return null;
        }
        String linkedRepository = names[0];
        if (names.length == 2) {
            name2 = names[1];
        } else if (names.length > 2) {
            name2 = metaRepositoryId.substring(linkedRepository.length() + 3);
        }

        connection = getConnectionFromRepository(linkedRepository);

        if (connection != null) {
            QueriesConnection queriesConnection = connection.getQueries();
            if (queriesConnection == null) {
                return null;
            }
            EList<Query> queries = queriesConnection.getQuery();

            for (Query currentQuery : queries) {
                if (currentQuery.getLabel().equals(name2)) {
                    return currentQuery;
                }
            }
        }
        return null;

    }

    public static IMetadataTable getMetadataFromRepository(String metaRepositoryId) {
        MetadataTable table = getMetadataTableFromRepository(metaRepositoryId);
        if (table != null) {
            IMetadataTable convertedTable = convert(table);
            convertedTable.getAdditionalProperties().remove(IComponentConstants.COMPONENT_PROPERTIES_TAG);
            convertedTable.getAdditionalProperties().remove(IComponentConstants.COMPONENT_SCHEMA_TAG);
            return convertedTable;
        }
        return null;

    }

    /**
     * qzhang Comment method "getNewMetadataColumns".
     * 
     * @param oldTable
     * @param newTable
     * @return
     */
    public static List<ColumnNameChanged> getNewMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        List<ColumnNameChanged> list = new ArrayList<ColumnNameChanged>();
        List<IMetadataColumn> newColumns = newTable.getListColumns();
        List<IMetadataColumn> oldColumns = oldTable.getListColumns();
        for (IMetadataColumn column : newColumns) {
            boolean isNew = true;
            for (IMetadataColumn ocolumn : oldColumns) {
                if (column.getLabel().equals(ocolumn.getLabel())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                list.add(new ColumnNameChanged("", column.getLabel())); //$NON-NLS-1$
            }
        }
        return list;
    }

    /**
     * qzhang Comment method "getRemoveMetadataColumns".
     * 
     * @param oldTable
     * @param newTable
     * @return
     */
    public static List<ColumnNameChanged> getRemoveMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        List<ColumnNameChanged> list = new ArrayList<ColumnNameChanged>();
        List<IMetadataColumn> newColumns = newTable.getListColumns();
        List<IMetadataColumn> oldColumns = oldTable.getListColumns();
        for (IMetadataColumn column : oldColumns) {
            boolean isNew = true;
            for (IMetadataColumn ocolumn : newColumns) {
                if (column.getLabel().equals(ocolumn.getLabel())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                list.add(new ColumnNameChanged(column.getLabel(), "")); //$NON-NLS-1$
            }
        }
        return list;
    }

    public static void initilializeSchemaFromElementParameters(IMetadataTable metadataTable,
            List<IElementParameter> elementParameters) {
        IElementParameter mappingParameter = getMappingParameter(elementParameters);
        for (int i = 0; i < elementParameters.size(); i++) {
            IElementParameter param = elementParameters.get(i);
            if ((param.getFieldType().equals(EParameterFieldType.SCHEMA_TYPE)
                    || param.getFieldType().equals(EParameterFieldType.SCHEMA_REFERENCE))
                    && param.getContext().equals(metadataTable.getAttachedConnector())) {
                if (param.getValue() instanceof IMetadataTable) {
                    initilializeSchema(metadataTable, elementParameters, mappingParameter, param);
                } else if (param.getFieldType() == EParameterFieldType.SCHEMA_REFERENCE) {
                    initilializeNewSchema(metadataTable, mappingParameter, param);
                    if (!param.getDefaultValues().isEmpty()) {
                        Schema schema = (Schema) param.getDefaultValues().get(0).getDefaultValue();
                        org.talend.core.model.metadata.builder.connection.MetadataTable defaultEmfTable = MetadataToolAvroHelper
                                .convertFromAvro(schema);
                        IMetadataTable defaultTable = MetadataToolHelper.convert(defaultEmfTable);
                        for (IMetadataColumn currentColumn : metadataTable.getListColumns()) {
                            IMetadataColumn defaultColumn = defaultTable.getColumn(currentColumn.getLabel());
                            if (defaultColumn != null) {
                                currentColumn.setCustom(defaultColumn.isCustom());
                                currentColumn.setReadOnly(defaultColumn.isReadOnly());
                            }
                        }
                        metadataTable.setReadOnly(defaultTable.isReadOnly());
                    }
                }
            }

        }
        metadataTable.sortCustomColumns();
    }

    public static void initilializeNewSchema(IMetadataTable metadataTable, IElementParameter mappingParameter,
            IElementParameter param) {
        Object value = param.getValue();
        if (value instanceof String) {
            IElement element = param.getElement();
            if (element instanceof INode) {
                INode node = (INode) element;
                ComponentProperties properties = node.getComponentProperties();
                if (properties != null) {
                    Object schemaObj = properties.getValuedProperty(param.getName()).getValue();
                    if (schemaObj instanceof Schema) {
                        Schema schema = (Schema) schemaObj;
                        org.talend.core.model.metadata.builder.connection.MetadataTable emfTable = MetadataToolAvroHelper
                                .convertFromAvro(schema);
                        IMetadataTable newTable = MetadataToolHelper.convert(emfTable);
                        initilializeSchema(metadataTable, newTable, mappingParameter, node);
                    }
                }
            }
        }
    }

    public static void initilializeSchema(IMetadataTable metadataTable, List<IElementParameter> elementParameters,
            IElementParameter mappingParameter, IElementParameter param) {
        param.setValueToDefault(elementParameters);
        IMetadataTable newTable = (IMetadataTable) param.getValue();
        initilializeSchema(metadataTable, newTable, mappingParameter, param.getElement());
    }

    public static void initilializeSchema(IMetadataTable metadataTable, IMetadataTable newTable,
            IElementParameter mappingParameter, IElement element) {
        String metadataTableName = metadataTable.getTableName();
        if (mappingParameter != null) {
            if (mappingParameter.getValue() != null && (!mappingParameter.getValue().equals(""))) { //$NON-NLS-1$
                newTable.setDbms((String) mappingParameter.getValue());
            }
        }
        metadataTable.setReadOnly(newTable.isReadOnly());

        metadataTable.setReadOnlyColumnPosition(newTable.getReadOnlyColumnPosition());

        // if all the table is read only then remove all columns to
        // set the one defined in the emf component
        // if (metadataTable.isReadOnly()) {
        // metadataTable.getListColumns().clear();
        // }
        for (int k = 0; k < newTable.getListColumns().size(); k++) {
            IMetadataColumn newColumn = newTable.getListColumns().get(k);
            IMetadataColumn oldColumn = metadataTable.getColumn(newColumn.getLabel());
            if (element instanceof INode && oldColumn == null) {
                INode node = (INode) element;
                if (node.getComponent().getName().equals("tGenKeyHadoop")) { //$NON-NLS-1$
                    int lastIndexOf = node.getLabel().lastIndexOf("_"); //$NON-NLS-1$
                    oldColumn = metadataTable.getColumn(newColumn.getLabel() + node.getLabel().substring(lastIndexOf));
                }
            }

            boolean update = true;
            if (metadataTableName != null && !metadataTableName.equals(newTable.getTableName())) {
                update = newColumn.isCustom();
            }
            if (oldColumn != null && update) {
                // if column exists, then override read only /
                // custom
                oldColumn.setReadOnly(newColumn.isReadOnly());
                oldColumn.setCustom(newColumn.isCustom());
                oldColumn.setCustomId(newColumn.getCustomId());
                if (newColumn.isReadOnly()) { // if read only,
                    // override
                    // everything
                    oldColumn.setKey(newColumn.isKey());
                    oldColumn.setNullable(newColumn.isNullable());
                    oldColumn.setLength(newColumn.getLength());
                    oldColumn.setPrecision(newColumn.getPrecision());
                    oldColumn.setPattern(newColumn.getPattern());
                    oldColumn.setType(newColumn.getType());
                    oldColumn.setTalendType(newColumn.getTalendType());
                    oldColumn.setComment(newColumn.getComment());
                }
            }
        }
    }

    public static IElementParameter getMappingParameter(List<IElementParameter> elementParameters) {
        for (int i = 0; i < elementParameters.size(); i++) {
            IElementParameter param = elementParameters.get(i);
            if (param.getFieldType().equals(EParameterFieldType.MAPPING_TYPE)) {
                return param;
            }
        }
        return null;
    }

    /**
     * DOC qzhang Comment method "copyTable".
     * 
     * @param source
     * @param target
     */
    public static void copyTable(IMetadataTable source, MetadataType target) {
        List<ColumnType> colTypes = target.getColumn();
        colTypes.clear();
        for (IMetadataColumn column : source.getListColumns()) {
            ColumnType createColumnType = TalendFileFactory.eINSTANCE.createColumnType();
            createColumnType.setComment(column.getComment());
            createColumnType.setDefaultValue(column.getDefault());
            createColumnType.setKey(column.isKey());
            if (column.getLength() == null) {
                createColumnType.setLength(-1);
                // createColumnType.unsetLength();
            } else {
                createColumnType.setLength(column.getLength());
            }
            if (column.getPrecision() == null) {
                createColumnType.setPrecision(-1);
                // createColumnType.unsetPrecision();
            } else {
                createColumnType.setPrecision(column.getPrecision());
            }
            if (!column.getLabel().equals(column.getOriginalDbColumnName())) {
                createColumnType.setOriginalDbColumnName(column.getOriginalDbColumnName());
            }
            createColumnType.setName(column.getLabel());
            createColumnType.setNullable(column.isNullable());
            createColumnType.setPattern(column.getPattern());

            createColumnType.setSourceType(column.getType());
            createColumnType.setType(column.getTalendType());

            if (PluginChecker.isDatacertPluginLoaded()) {
                createColumnType.setRelatedEntity(column.getRelatedEntity());
                createColumnType.setRelationshipType(column.getRelationshipType());
            }
            colTypes.add(createColumnType);
        }

    }

    /**
     * @param node
     * @param name
     * @return
     * @deprecated use getMetadataTableFromNodeLabel instead
     */
    @Deprecated
    public static IMetadataTable getMetadataTableFromNode(INode node, String name) {
        return getMetadataTableFromNodeLabel(node, name);
    }

    public static boolean hasCustomColumns(IMetadataTable table) {
        if (table == null) {
            return false;
        }
        for (IMetadataColumn column : table.getListColumns()) {
            if (column.isCustom()) {
                return true;
            }
        }
        return false;
    }

    public static Collection<IRepositoryViewObject> getContextDependenciesOfMetadataConnection(Collection<Item> items) {
        Collection<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        try {
            for (Item item : items) {
                if (item == null) {
                    continue;
                }
                if (item instanceof ConnectionItemImpl) {
                    Connection connection = ((ConnectionItemImpl) item).getConnection();
                    if (connection != null) {
                        IRepositoryViewObject lastVersion = null;
                        if (connection.getContextId() != null) {
                            IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                                    IRepositoryService.class);
                            lastVersion = service.getProxyRepositoryFactory().getLastVersion(connection.getContextId());
                        }
                        if (lastVersion != null) {
                            repositoryObjects.add(lastVersion);
                        }
                    }

                }

            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return repositoryObjects;
    }

    /**
     * 
     * cli Comment method "processFieldLength".
     * 
     */
    public static void processFieldsLength(EObject obj) {
        if (obj != null) {

            // if the comment length of metadata object is more than 255. will be cut
            if (obj instanceof AbstractMetadataObject) {
                AbstractMetadataObject metadataObj = (AbstractMetadataObject) obj;
                String comment = processFieldsLength(metadataObj.getComment());
                if (comment != null) {
                    metadataObj.setComment(comment);
                }
            } else if (obj instanceof MetadataType) {
                MetadataType type = (MetadataType) obj;
                String comment = processFieldsLength(type.getComment());
                if (comment != null) {
                    type.setComment(comment);
                }
            } else if (obj instanceof ColumnType) {
                ColumnType type = (ColumnType) obj;
                String comment = processFieldsLength(type.getComment());
                if (comment != null) {
                    type.setComment(comment);
                }
            }
        }
    }

    private static String processFieldsLength(String comment) {
        final int max = 255;
        if (comment != null && comment.length() > max) {
            final String dots = "..."; //$NON-NLS-1$
            comment = comment.substring(0, max - 4);
            if (!comment.endsWith(dots)) {
                comment += dots;
            }
            return comment;
        }
        return null; // don't process
    }

    public static SAPFunctionUnit getSAPFunctionFromRepository(String functionRepositoryId) {
        org.talend.core.model.metadata.builder.connection.Connection connection;

        String[] names = functionRepositoryId.split(" - "); //$NON-NLS-1$
        String name2 = null;
        if (names.length < 2) {
            return null;
        }
        String linkedRepository = names[0];
        if (names.length == 2) {
            name2 = names[1];
        } else if (names.length > 2) {
            name2 = functionRepositoryId.substring(linkedRepository.length() + 3);
        }

        connection = getConnectionFromRepository(linkedRepository);
        if (connection != null) {
            if (connection instanceof SAPConnection) {
                SAPConnection sapConn = (SAPConnection) connection;
                EList funtions = sapConn.getFuntions();
                for (Object obj : funtions) {
                    SAPFunctionUnit funciton = (SAPFunctionUnit) obj;
                    if (name2.equals(funciton.getLabel())) {
                        return funciton;
                    }

                }
            }
        }
        return null;
    }

    public static IMetadataTable convert(MetadataTable old) {
        ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
        IMetadataTable result = new org.talend.core.model.metadata.MetadataTable();
        result.setComment(old.getComment());
        result.setId(old.getId());
        result.setLabel(old.getLabel());
        String sourceName = old.getName();
        if (sourceName == null) {
            sourceName = old.getLabel();
        }
        result.setTableName(sourceName);
        result.setTableType(old.getTableType());
        List<IMetadataColumn> columns = new ArrayList<IMetadataColumn>(old.getColumns().size());
        for (TaggedValue tv : old.getTaggedValue()) {
            if (DiSchemaConstants.TALEND6_IS_READ_ONLY.equals(tv.getTag())) {
                result.setReadOnly(Boolean.valueOf(tv.getValue()));
                break;
            }
        }

        for (Object o : old.getColumns()) {
            org.talend.core.model.metadata.builder.connection.MetadataColumn column = (org.talend.core.model.metadata.builder.connection.MetadataColumn) o;
            IMetadataColumn newColumn = new org.talend.core.model.metadata.MetadataColumn();
            columns.add(newColumn);
            newColumn.setComment(column.getComment());
            newColumn.setDefault(column.getDefaultValue());
            newColumn.setKey(column.isKey());
            String label2 = column.getLabel();
            if (coreService != null) {
                if (coreService.isKeyword(label2)) {
                    label2 = "_" + label2; //$NON-NLS-1$
                }
            }
            newColumn.setLabel(label2);
            newColumn.setPattern(column.getPattern());
            
            if (column.getLength() < 0) {
                newColumn.setLength(null);
            } else {
                newColumn.setLength(Long.valueOf(column.getLength()).intValue());
            }
            if (column.getOriginalLength() < 0) {
                newColumn.setOriginalLength(null);
            } else {
                newColumn.setOriginalLength(Long.valueOf(column.getOriginalLength()).intValue());
            }

            if (column.getTaggedValue().size() > 0) {
                for (TaggedValue tv : column.getTaggedValue()) {
                    String additionalTag = tv.getTag();
                    if (additionalTag.startsWith("additionalField:")) {
                        String[] splits = additionalTag.split(":");
                        additionalTag = splits[1];
                    }
                    if (DiSchemaConstants.TALEND6_COLUMN_CUSTOM.equals(additionalTag)) {
                        newColumn.setCustom(Boolean.valueOf(tv.getValue()));
                    } else if (DiSchemaConstants.TALEND6_IS_READ_ONLY.equals(additionalTag)) {
                        newColumn.setReadOnly(Boolean.valueOf(tv.getValue()));
                    }else {
                        newColumn.getAdditionalField().put(additionalTag, tv.getValue());
                    }
                }
            }
            
            newColumn.setNullable(column.isNullable());
            if (column.getPrecision() < 0) {
                newColumn.setPrecision(null);
            } else {
                newColumn.setPrecision(Long.valueOf(column.getPrecision()).intValue());
            }
            newColumn.setTalendType(column.getTalendType());
            newColumn.setType(column.getSourceType());
            if (column.getName() == null || column.getName().equals("")) { //$NON-NLS-1$
                String label = label2;
                if (label != null && label.length() > 0) {
                    String substring = label.substring(1);
                    if (coreService != null) {
                        if (label.startsWith("_") && coreService.isKeyword(substring)) { //$NON-NLS-1$
                            label = substring;
                        }
                    }
                }
                newColumn.setOriginalDbColumnName(label);
            } else {
                newColumn.setOriginalDbColumnName(column.getName());
            }
            // columns.add(convertToIMetaDataColumn(column));
        }
        result.setListColumns(columns);
        Map<String, String> newProperties = result.getAdditionalProperties();
        EMap<String, String> oldProperties = old.getAdditionalProperties();
        for (Entry<String, String> entry : oldProperties) {
            newProperties.put(entry.getKey(), entry.getValue());
        }
        for (TaggedValue tv : old.getTaggedValue()) {
            String additionalTag = tv.getTag();
            result.getAdditionalProperties().put(additionalTag, tv.getValue());
        }
        return result;
    }
}
