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
package org.talend.core.model.metadata;

import java.util.List;

import org.talend.core.CorePlugin;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.query.GenerateQueryFactory;
import org.talend.core.model.metadata.query.IQueryGenerator;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class QueryUtil {

    public static final String DEFAULT_TABLE_NAME = "_MyTable_"; //$NON-NLS-1$

    public static final String DEFAULT_SCHEMA_NAME = "NONE"; //$NON-NLS-1$

    // public static final String CONTEXT = "context";

    private static final String ENTER = "\n"; //$NON-NLS-1$

    private static final String SPACE = " "; //$NON-NLS-1$

    private static final String CON = TalendTextUtils.getStringConnect();

    public static boolean isContextQuery = false;

    public static boolean needFormatSQL(String dbType) {
        if (dbType != null) {
            EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(dbType);
            if (type != null) {
                switch (type) {
                case NETEZZA:
                case ORACLE_OCI:
                case ORACLEFORSID:
                case ORACLESN:
                case PSQL:
                case PLUSPSQL:
                case AS400:
                case ACCESS:
                case MYSQL:
                case AMAZON_AURORA:
                case IBMDB2:
                case IBMDB2ZOS:
                case HIVE:
                case IMPALA:
                    /*
                     * if not work on DefaultQueryGenerator.
                     */
                    return false; // no need
                default: // need
                    return true;
                }
            }
        }
        return false;
    }

    public static String generateNewQuery(IElement node, IMetadataTable repositoryMetadata, String dbType, String schema,
            String realTableName) {
        IQueryGenerator generator = GenerateQueryFactory.getGenerator(dbType);
        if (generator != null) {
            generator.setParameters(node, repositoryMetadata, schema, realTableName);
            return generator.generateQuery();
        }
        return generateNewQueryDelegate(node, repositoryMetadata, dbType, schema, realTableName);
    }

    public static String generateNewQuery(IElement node, IMetadataTable repositoryMetadata, boolean isJdbc, String dbType,
            String schema, String realTableName) {
        IQueryGenerator generator = GenerateQueryFactory.getGenerator(dbType);
        if (generator != null) {
            generator.setParameters(node, repositoryMetadata, schema, realTableName, isJdbc);
            return generator.generateQuery();
        }
        return generateNewQueryDelegate(node, repositoryMetadata, dbType, schema, realTableName);
    }

    public static String generateNewQuery(IElement node, IMetadataTable repositoryMetadata, String dbType, String schema,
            String realTableName, boolean standardSyntax) {
        IQueryGenerator generator = GenerateQueryFactory.getGenerator(dbType);
        if (generator != null) {
            generator.setParameters(node, repositoryMetadata, schema, realTableName);
            return generator.generateQuery();
        }
        return generateNewQueryDelegate(node, repositoryMetadata, dbType, schema, realTableName, standardSyntax);
    }

    public static String generateNewQueryDelegate(IElement node, IMetadataTable repositoryMetadata, String dbType, String schema,
            String realTableName) {
        String tableName = getTableName(node, repositoryMetadata, schema, dbType, realTableName);
        return generateNewQuery(repositoryMetadata, dbType, tableName, realTableName);
    }

    public static String generateNewQueryDelegate(IElement node, IMetadataTable repositoryMetadata, String dbType, String schema,
            String realTableName, boolean standardSyntax) {

        String tableName = getTableName(node, repositoryMetadata, schema, dbType, realTableName);
        return generateNewQuery(repositoryMetadata, dbType, tableName, standardSyntax);
    }

    /**
     * TODO need to check logic
     * <p>
     * DOC YeXiaowei Comment method "generateNewQuery".
     * 
     * @param repositoryMetadata
     * @param dbType
     * @param tableName
     * @param standardSyntax
     * @return
     */
    public static String generateNewQuery(final IMetadataTable repositoryMetadata, final String dbType, final String tableName,
            final boolean standardSyntax) {

        List<IMetadataColumn> metaDataColumnList = repositoryMetadata.getListColumns();
        int index = metaDataColumnList.size();
        if (index == 0) {
            return ""; //$NON-NLS-1$
        }

        isContextQuery = false;
        if (isContext(tableName)) {
            isContextQuery = true;
        }

        String columnsQuery = ""; //$NON-NLS-1$
        for (int i = 0; i < metaDataColumnList.size(); i++) {
            IMetadataColumn metaDataColumn = metaDataColumnList.get(i);
            String columnName = TalendTextUtils.addQuotesWithSpaceFieldForSQLString(metaDataColumn.getOriginalDbColumnName(),
                    dbType, true);
            if (!isContainDeclareString(columnName, TalendTextUtils.getStringDeclare())) {
                columnName = TalendTextUtils.declareString(columnName);
            }
            String columnStr = columnName;

            if (i != index - 1) {
                columnStr = checkAndConcatString(columnStr, TalendTextUtils.declareString("," + SPACE)); //$NON-NLS-1$
            }
            String declareString = TalendTextUtils.declareString("/"); //$NON-NLS-1$
            if (standardSyntax) {
                declareString = TalendTextUtils.declareString("."); //$NON-NLS-1$
            }

            columnStr = checkAndConcatString(declareString, columnStr);
            columnStr = checkAndConcatString(tableName, columnStr);

            if ("".equals(columnsQuery.trim())) { //$NON-NLS-1$
                columnsQuery = columnStr;
            } else {
                columnsQuery = checkAndConcatString(columnsQuery, columnStr);
            }

        }

        String query = TalendTextUtils.declareString("SELECT "); //$NON-NLS-1$
        if (isContextQuery) { // new line
            String end = ENTER + CON + checkAndConcatString(TalendTextUtils.declareString(" FROM "), TalendTextUtils //$NON-NLS-1$
                    .addQuotesWithSpaceFieldForSQLString(tableName, dbType, !isContextQuery));

            end = replaceTheSchemaString(end);

            query = checkAndConcatString(query, columnsQuery) + end;
        } else {
            String end = checkAndConcatString(TalendTextUtils.declareString(" FROM "), TalendTextUtils //$NON-NLS-1$
                    .addQuotesWithSpaceFieldForSQLString(tableName, dbType, !isContextQuery));
            end = replaceTheSchemaString(end);

            query = checkAndConcatString(checkAndConcatString(query, columnsQuery), end);
        }

        return query;
    }

    public static String generateNewQuery(final IMetadataTable repositoryMetadata, final String dbType,
            final String tableNameIfNeed, final String... realTableName) {
        if (repositoryMetadata == null) {
            return ""; //$NON-NLS-1$
        }

        List<IMetadataColumn> metaDataColumnList = repositoryMetadata.getListColumns();
        int index = metaDataColumnList.size();
        if (index == 0) {
            return ""; //$NON-NLS-1$
        }
        String tableNameWithQuoteIfNeed = TalendQuoteUtils.addQuotesIfNotExist(tableNameIfNeed);
        isContextQuery = false;
        if (isContext(tableNameWithQuoteIfNeed)) {
            isContextQuery = true;
        }

        String columnsQuery = ""; //$NON-NLS-1$

        for (int i = 0; i < metaDataColumnList.size(); i++) {
            IMetadataColumn metaDataColumn = metaDataColumnList.get(i);
            String columnName = quoteStringValue(metaDataColumn.getOriginalDbColumnName(), dbType);

            String columnStr = columnName;
            if (i != index - 1) {
                columnStr = checkAndConcatString(columnStr, TalendTextUtils.declareString("," + SPACE)); //$NON-NLS-1$
            }

            columnStr = checkAndConcatString(TalendTextUtils.declareString("."), columnStr); //$NON-NLS-1$
            columnStr = checkAndConcatString(tableNameWithQuoteIfNeed, columnStr);

            if ("".equals(columnsQuery.trim())) { //$NON-NLS-1$
                columnsQuery = columnStr;
            } else {
                columnsQuery = checkAndConcatString(columnsQuery, columnStr);
            }

        }
        boolean isCheck = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.SQL_ADD_QUOTE);
        String query = TalendTextUtils.declareString("SELECT "); //$NON-NLS-1$
        if (isContextQuery) { // new line
            String end = ENTER + CON + checkAndConcatString(TalendTextUtils.declareString(" FROM "), tableNameWithQuoteIfNeed); //$NON-NLS-1$

            end = replaceTheSchemaString(end);

            query = checkAndConcatString(query, columnsQuery) + end;
        } else if (dbType != null && dbType.equals(EDatabaseTypeName.INFORMIX.getDisplayName())) { // hywang add for
            // bug0007563
            String declareString = TalendTextUtils.getStringDeclare();
            String end = ""; //$NON-NLS-1$
            if (!isCheck) { // hywang add isCheck for informix
                end = checkAndConcatString(TalendTextUtils.declareString(" FROM "), declareString + realTableName[0] //$NON-NLS-1$
                        + declareString);
            } else {
                end = checkAndConcatString(
                        TalendTextUtils.declareString(" FROM "), declareString + realTableName[0].substring(2, realTableName[0].length() - 2) //$NON-NLS-1$
                                + declareString);
            }
            query = checkAndConcatString(checkAndConcatString(query, columnsQuery), end);
        } else {
            String end = checkAndConcatString(TalendTextUtils.declareString(" FROM "), tableNameWithQuoteIfNeed); //$NON-NLS-1$

            end = replaceTheSchemaString(end);

            query = checkAndConcatString(checkAndConcatString(query, columnsQuery), end);
        }
        return query;
    }

    /*
     * if there is no schema in table string and not add quote, It's no effect.
     */
    private static String replaceTheSchemaString(String schema) {
        if (schema == null) {
            return null;
        }
        if (isContextQuery) {
            boolean isCheck = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.SQL_ADD_QUOTE);
            if (!isCheck) {
                String addStr = TalendTextUtils.getStringDeclare();
                return schema.replaceFirst(addStr + "\\" + CON + addStr, ""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return schema;
    }

    public static String getTableName(IElement node, IMetadataTable repositoryMetadata, String schema, String dbType,
            String realTableName) {
        String currentTableName = null;
        boolean flag = false;
        String dbTableName = getDbTableName(node);

        if (dbTableName != null) {
            switch (LanguageManager.getCurrentLanguage()) {
            case JAVA:
                if (dbTableName.contains(TalendTextUtils.QUOTATION_MARK)) {
                    if (dbTableName.startsWith(TalendTextUtils.QUOTATION_MARK)
                            && dbTableName.endsWith(TalendTextUtils.QUOTATION_MARK) && dbTableName.length() > 2) {
                        currentTableName = dbTableName.substring(1, dbTableName.length() - 1);
                        // if (dbType != null && (dbType.equals("NETEZZA") || dbType.equals("Netezza"))) {
                        // if (dbName != null && schema != null) {
                        // currentTableName = dbName + "." + schema + "." + currentTableName;
                        // } else if (dbName != null && schema == null) {
                        // currentTableName = dbName + ".." + currentTableName;
                        // }
                        // }
                        flag = true;
                    } else {
                        currentTableName = null;
                    }
                } else {
                    currentTableName = dbTableName;
                    if (null != currentTableName && !("".equals(currentTableName))) { //$NON-NLS-1$
                        flag = true;
                    }
                }
                break;
            default:
                if (dbTableName.contains(TalendTextUtils.SINGLE_QUOTE)) {
                    if (dbTableName.startsWith(TalendTextUtils.SINGLE_QUOTE)
                            && dbTableName.endsWith(TalendTextUtils.SINGLE_QUOTE) && dbTableName.length() > 2) {
                        currentTableName = dbTableName.substring(1, dbTableName.length() - 1);
                        flag = true;
                    } else {
                        currentTableName = null;
                    }
                } else {
                    currentTableName = dbTableName;
                    if (null != currentTableName && !("".equals(currentTableName))) { //$NON-NLS-1$
                        flag = true;
                    }
                }
            }
        }
        if (!flag) {
            currentTableName = realTableName;
        }
        if (currentTableName == null) {
            currentTableName = DEFAULT_TABLE_NAME;
        }
        if (schema != null && schema.length() > 0) {
            // Quote is added to schema and table when call getSchemaName() method
            currentTableName = getSchemaName(schema, dbType, currentTableName);
            String productName = EDatabaseTypeName.getTypeFromDbType(dbType).getProduct();
            if (dbType != null && productName != null && productName.equals("JAVADB")) {
                currentTableName = quoteStringValue(currentTableName, dbType);
            }
        } else {
            // If no schema, you also need to add quote to table name
            if (isContext(currentTableName)) {
                currentTableName = quoteVariableRefrence(currentTableName, dbType);
            } else {
                currentTableName = quoteStringValue(currentTableName, dbType);
            }
        }

        return currentTableName;
    }

    private static String getSchemaName(String schema, String dbType, String currentTableName) {
        String prefix;
        String suffix;
        EDatabaseTypeName typeFromDbType = EDatabaseTypeName.getTypeFromDbType(dbType);
        if (typeFromDbType.isNeedSchema()) {
            // wzhang modified to fix bug 7879. value of oracle schema can't attach quotation marks.
            boolean isOracle = (typeFromDbType == EDatabaseTypeName.ORACLEFORSID || EDatabaseTypeName.ORACLESN == typeFromDbType || EDatabaseTypeName.ORACLE_OCI == typeFromDbType);
            if (isContext(schema)) {
                String schemaStr = quoteVariableRefrence(schema, dbType);
                if (isOracle) {
                    schemaStr = schema;
                }
                if (isContext(currentTableName)) {
                    prefix = checkAndConcatString(schemaStr, TalendTextUtils.declareString(".")); //$NON-NLS-1$
                    suffix = quoteVariableRefrence(currentTableName, dbType);
                } else {
                    prefix = schemaStr;
                    suffix = checkAndConcatString(TalendTextUtils.declareString("."), quoteStringValue(currentTableName, dbType)); //$NON-NLS-1$
                }
            } else {
                String schemaStr = quoteStringValue(schema, dbType);
                if (isOracle) {
                    boolean isCheck = CorePlugin.getDefault().getPreferenceStore()
                            .getBoolean(ITalendCorePrefConstants.SQL_ADD_QUOTE);
                    if (isCheck) {
                        schemaStr = TalendTextUtils.addQuotes(schema);
                    }
                }
                prefix = checkAndConcatString(schemaStr, TalendTextUtils.declareString(".")); //$NON-NLS-1$
                if (isContext(currentTableName)) {
                    suffix = quoteVariableRefrence(currentTableName, dbType);
                } else {
                    suffix = quoteStringValue(currentTableName, dbType);
                }
            }
            currentTableName = checkAndConcatString(prefix, suffix);
        }
        return currentTableName;
    }

    private static String quoteVariableRefrence(String ref, String dbType) {
        return TalendTextUtils.addQuotesWithSpaceFieldForSQLStringForce(ref, dbType, false);
    }

    private static String quoteStringValue(String value, String dbType) {
        return TalendTextUtils.addQuotesWithSpaceFieldForSQLStringForce(TalendTextUtils.declareString(value), dbType, true);
    }

    private static String getDbTableName(IElement node) {
        if (node != null) { // for job settings extra.(feature 2710)
            IElementParameter param = node.getElementParameterFromField(EParameterFieldType.DBTABLE);
            if (param != null && param.isShow(node.getElementParameters())) {
                return (String) param.getValue();
            }
        }
        return null;
    }

    private static String getDbName(Element node) {
        String dbName = null;
        if (node != null) { // for job settings extra.(feature 2710)
            IElementParameter param = node.getElementParameter("DBNAME");
            if (param != null && param.isShow(node.getElementParameters())) {
                dbName = TalendTextUtils.removeQuotes((String) param.getValue());
            }
        }
        return dbName;
    }

    private static String checkAndConcatString(String str1, String str2) {
        if (str1 == null) {
            str1 = ""; //$NON-NLS-1$
        }
        if (str2 == null) {
            str2 = ""; //$NON-NLS-1$
        }

        String declareString = TalendTextUtils.getStringDeclare();

        if (isContainDeclareString(str1, declareString) && isContainDeclareString(str2, declareString)) {
            str1 = TalendTextUtils.removeQuotes(str1, declareString);
            str2 = TalendTextUtils.removeQuotes(str2, declareString);

            return declareString + str1 + str2 + declareString;
        }
        return str1 + CON + str2;

    }

    private static boolean isContainDeclareString(String str, String declareString) {
        if (str == null || declareString == null) {
            return false;
        }
        return str.startsWith(declareString) && str.endsWith(declareString);
    }

    private static boolean isContext(String str) {
        if (str == null) {
            return false;
        }
        // return str.indexOf(QueryUtil.CONTEXT) != -1; // maybe, it's not exact
        return ContextParameterUtils.containContextVariables(str);
    }

    /**
     * 
     * ggu Comment method "checkAndAddQuotes".
     * 
     * if the query start with quote, will not add quotes.
     */
    public static String checkAndAddQuotes(String query) {
        if (query == null) {
            query = ""; //$NON-NLS-1$
        }
        query = query.trim();
        // modified by hyWang
        if (!query.startsWith(TalendTextUtils.getQuoteChar()) || !TalendTextUtils.isCommonString(query)) { // perhaps,
            // need
            // improve
            return TalendTextUtils.addSQLQuotes(query);
        }
        return query; // keep it
    }

    /**
     * Wanghaoyu Comment method "checkIfIsNoQuotesAtAll"
     * 
     * to check a query if has any quote
     */
    public static boolean checkIfIsNoQuotesAtAll(String query) {
        if (!query.contains("\"")) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    public static boolean checkIfHasSpecialEscapeValue(String query) {
        if (query.contains("\\n") || query.contains("\\r") || query.contains("\\t")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return true;
        }
        return false;
    }
}
