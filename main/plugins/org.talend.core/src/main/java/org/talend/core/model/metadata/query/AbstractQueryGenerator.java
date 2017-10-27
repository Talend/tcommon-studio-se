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
package org.talend.core.model.metadata.query;

import java.util.List;

import org.talend.core.CorePlugin;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * ggu class global comment. Detailled comment
 */
public abstract class AbstractQueryGenerator implements IQueryGenerator {

    private final EDatabaseTypeName dbType;

    private IElement element;

    private IMetadataTable metadataTable;

    protected String schema;

    protected String realTableName;

    protected boolean isJdbc;

    protected final boolean originalSqlQuoteSetting;

    public AbstractQueryGenerator(EDatabaseTypeName dbType) {
        super();
        this.dbType = dbType;
        originalSqlQuoteSetting = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.SQL_ADD_QUOTE);
    }

    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName) {
        this.element = element;
        this.metadataTable = metadataTable;
        this.schema = schema;
        this.realTableName = realTableName;
    }

    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName, boolean isJdbc) {
        this.element = element;
        this.metadataTable = metadataTable;
        this.schema = schema;
        this.realTableName = realTableName;
        this.isJdbc = isJdbc;
    }

    protected EDatabaseTypeName getDBType() {
        return this.dbType;
    }

    protected String getDBTypeName() {
        return this.dbType.getDisplayName();
    }

    protected String getSchema() {
        return this.schema;
    }

    protected void setSchema(String schema) {
        this.schema = schema;
    }

    protected IElement getElement() {
        return this.element;
    }

    protected IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }

    protected String getRealTableName() {
        return this.realTableName;
    }

    protected void setRealTableName(String realTableName) {
        this.realTableName = realTableName;
    }

    private IElement getUseExistedConnetion(IElement currentElem) {
        IElementParameter param = currentElem.getElementParameter("USE_EXISTING_CONNECTION"); //$NON-NLS-1$
        if (param != null) {
            Object value = param.getValue();
            boolean used = false;
            if (value instanceof Boolean) {
                used = (Boolean) value;
            } else if (value instanceof String) {
                used = Boolean.parseBoolean((String) value);
            }
            if (used) {
                IElementParameter elementParameter = currentElem.getElementParameterFromField(EParameterFieldType.COMPONENT_LIST);
                if (elementParameter != null && elementParameter.getName().equals("CONNECTION")) { //$NON-NLS-1$
                    String connNodeName = (String) elementParameter.getValue();
                    if (connNodeName != null && currentElem instanceof INode) {
                        IProcess process = ((INode) currentElem).getProcess();
                        if (process != null) {
                            for (INode node : process.getGraphicalNodes()) {
                                if (connNodeName.equals(node.getUniqueName())) {
                                    return node;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * ggu Comment method "getDBTableName".
     * 
     * moved from QueryUtil
     */
    protected String getDBTableName(IElement elem) {
        if (elem != null) { // for job settings extra.(feature 2710)
            IElementParameter param = elem.getElementParameterFromField(EParameterFieldType.DBTABLE);
            if (param != null) {
                if (param.isShow(elem.getElementParameters())) {
                    String value = (String) param.getValue();
                    if (value != null) {
                        String newDBTableName = processDBTableName(value);
                        if (!EMPTY.equals(newDBTableName)) {
                            return newDBTableName;
                        }
                    }
                }
            }
        }
        return QueryUtil.DEFAULT_TABLE_NAME;
    }

    /**
     * 
     * if need, should override.
     */
    protected String processDBTableName(String tabelName) {
        if (tabelName != null) {
            if (ContextParameterUtils.containContextVariables(tabelName)) {
                return tabelName;
            } else {
                return TalendTextUtils.removeQuotes(tabelName);
            }
        }
        return null;
    }

    /**
     * 
     * ggu Comment method "getDBName".
     * 
     * moved from QueryUtil
     */
    protected String getDBName(IElement elem) {
        if (elem != null) {
            IElementParameter param = elem.getElementParameter("DBNAME"); //$NON-NLS-1$
            if (param != null) {
                if (param.isShow(elem.getElementParameters())) {
                    String value = (String) param.getValue();
                    if (value != null) {
                        return processDBName(value);
                    }
                } else { // when use an existed connection
                    IElement usedElem = getUseExistedConnetion(elem);
                    if (usedElem != null) {
                        return getDBName(usedElem);
                    }

                }
            }
        }
        return EMPTY;
    }

    /**
     * 
     * if need, should override.
     */
    protected String processDBName(String dbName) {
        if (dbName != null) {
            if (ContextParameterUtils.containContextVariables(dbName)) {
                return dbName;
            } else {
                return TalendTextUtils.removeQuotes(dbName);
            }
        }
        return null;
    }

    protected String getSchema(IElement elem) {
        if (elem != null) {
            for (IElementParameter param : elem.getElementParameters()) {
                if ("SCHEMA".equals(param.getRepositoryValue())) { //$NON-NLS-1$
                    if (param.isShow(elem.getElementParameters())) {
                        String value = (String) param.getValue();
                        if (value != null) {
                            return processSchema(value);
                        }
                    } else { // when use an existed connection
                        IElement usedElem = getUseExistedConnetion(elem);
                        if (usedElem != null) {
                            return getSchema(usedElem);
                        }
                    }
                }
            }
        }
        return EMPTY;
    }

    protected String processSchema(String schema) {
        if (schema != null) {
            if (ContextParameterUtils.containContextVariables(schema)) {
                return schema;
            } else {
                return TalendTextUtils.removeQuotes(schema);
            }
        }
        return null;
    }

    protected char getSQLFieldConnector() {
        return '.';
    }

    /**
     * 
     * ggu Comment method "setForceAddQuote".
     * 
     * if you want to set it, must call this first "true".
     */
    public void setForceAddQuote(boolean sqlQuoteFlag) {
        CorePlugin.getDefault().getPreferenceStore().setValue(ITalendCorePrefConstants.SQL_ADD_QUOTE, sqlQuoteFlag);
    }

    protected boolean forceAddQuote() {
        return CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.SQL_ADD_QUOTE);
    }

    protected void revertAddQuoteSetting() {
        CorePlugin.getDefault().getPreferenceStore().setValue(ITalendCorePrefConstants.SQL_ADD_QUOTE, originalSqlQuoteSetting);
    }

    /**
     * 
     * ggu Comment method "getDatabaseFieldQuote".
     * 
     * more similar with TalendTextUtils.addQuotesForSQLString
     * 
     * @param left, main for Access
     */
    protected String getDatabaseFieldQuote(boolean left) {
        String quoteByDBType = TalendTextUtils.getQuoteByDBType(getDBTypeName(), left);

        if (TalendTextUtils.QUOTATION_MARK.equals(quoteByDBType)) {
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                return "\\" + TalendTextUtils.QUOTATION_MARK; //$NON-NLS-1$
            } else { // need check, if there is problem for perl
                return TalendTextUtils.QUOTATION_MARK;
            }
            /**
             * in fact, no need
             */
            // } else if (TalendTextUtils.ANTI_QUOTE.equals(quoteByDBType)) {
            // return TalendTextUtils.ANTI_QUOTE;
            // } else if (TalendTextUtils.SINGLE_QUOTE.equals(quoteByDBType)) {
            // return TalendTextUtils.SINGLE_QUOTE;
            // } else if (TalendTextUtils.LBRACKET.equals(quoteByDBType) ||
            // TalendTextUtils.RBRACKET.equals(quoteByDBType)) {
            // if (left) {
            // return TalendTextUtils.LBRACKET;
            // } else {
            // return TalendTextUtils.RBRACKET;
            // }
        } else {
            return quoteByDBType;
        }
    }

    protected String getTableNameWithDBAndSchema(final String dbName, final String schema, String tableName) {
        if (tableName == null || EMPTY.equals(tableName.trim())) {
            tableName = QueryUtil.DEFAULT_TABLE_NAME;
        }

        final StringBuffer tableNameWithDBAndSchema = new StringBuffer();

        //
        if (dbName != null && !EMPTY.equals(dbName)) {
            tableNameWithDBAndSchema.append(checkContextAndAddQuote(dbName));
            tableNameWithDBAndSchema.append(getSQLFieldConnector());
        }
        //
        if (schema != null && !EMPTY.equals(schema)) {
            tableNameWithDBAndSchema.append(checkContextAndAddQuote(schema));
            tableNameWithDBAndSchema.append(getSQLFieldConnector());
        }
        //
        tableNameWithDBAndSchema.append(checkContextAndAddQuote(tableName));

        return tableNameWithDBAndSchema.toString();
    }

    protected String checkContextAndAddQuote(String field) {
        StringBuffer fieldSB = new StringBuffer();

        if (ContextParameterUtils.containContextVariables(field)) {
            if (forceAddQuote()) {
                final String quoteByDBType = getDatabaseFieldQuote(true);
                fieldSB.append(quoteByDBType);
            }
            fieldSB.append(TalendTextUtils.getStringDeclare());
            fieldSB.append(TalendTextUtils.getStringConnect());

            fieldSB.append(field);

            fieldSB.append(TalendTextUtils.getStringConnect());
            fieldSB.append(TalendTextUtils.getStringDeclare());

            if (forceAddQuote()) {
                final String quoteByDBType = getDatabaseFieldQuote(false);
                fieldSB.append(quoteByDBType);
            }
        } else {
            fieldSB.append(addQuotesForSQL(field));
        }

        return fieldSB.toString();
    }

    /**
     * 
     * ggu Comment method "addQuotesForSQL".
     * 
     * need improve and check later
     */
    protected String addQuotesForSQL(String field) {
        // "\"abc\""
        String quoteStr = TalendTextUtils.addQuotesWithSpaceFieldForSQLStringForce(field, getDBTypeName(), true);
        // \"abc\"
        quoteStr = TalendTextUtils.removeQuotes(quoteStr);
        return quoteStr;
    }

    /**
     * 
     * ggu Comment method "processResultSQL".
     * 
     */
    protected String processResultSQL(String sql) {
        if (sql != null) {
            // unused like +""
            String suffix = TalendTextUtils.getStringConnect() + TalendTextUtils.getQuoteChar() + TalendTextUtils.getQuoteChar();
            if (sql.endsWith(suffix)) {
                sql = sql.substring(0, sql.length() - suffix.length());
            }
        }
        return sql;
    }

    public final String generateQuery() {
        try {
            return generateQueryDelegate();
        } finally {
            afterGenerateQuery();
        }
    }

    protected void afterGenerateQuery() {
        revertAddQuoteSetting();
    }

    protected String generateQueryDelegate() {
        if (getMetadataTable() != null && !getMetadataTable().getListColumns().isEmpty()) {
            String dbName = null;
            String tableName = null;
            String schemaName = null;
            if (getElement() != null) {
                dbName = getDBName(getElement());
                tableName = getDBTableName(getElement());
                schemaName = getSchema(getElement());
                if (isJdbc && (schemaName == null || "".equals(schemaName))) {
                    schemaName = schema;
                }
            } else {
                tableName = realTableName;
                schemaName = schema;
            }
            final String tableNameWithDBAndSchema = getTableNameWithDBAndSchema(dbName, schemaName, tableName);
            String columnField = generateColumnFields(tableNameWithDBAndSchema);
            if (dbType == EDatabaseTypeName.HIVE) {
                columnField = generateColumnFields(tableName);
            }

            StringBuffer sql = new StringBuffer(100);
            sql.append(TalendTextUtils.getQuoteChar());
            sql.append(SQL_SELECT);
            sql.append(SPACE);
            // columns
            sql.append(columnField);
            //
            sql.append(ENTER);
            sql.append(SQL_FROM);
            sql.append(SPACE);
            sql.append(tableNameWithDBAndSchema);

            sql.append(TalendTextUtils.getQuoteChar());

            return processResultSQL(sql.toString());

            //
        }

        return EMPTY;

    }

    protected String generateColumnFields(final String tableNameWithDBAndSchema) {
        //
        StringBuffer fieldsSQL = new StringBuffer(100);
        List<IMetadataColumn> listColumns = getMetadataTable().getListColumns();
        for (int i = 0; i < listColumns.size(); i++) {
            IMetadataColumn column = listColumns.get(i);
            fieldsSQL.append(ENTER);
            fieldsSQL.append(SPACE);
            fieldsSQL.append(SPACE);
            fieldsSQL.append(tableNameWithDBAndSchema);
            fieldsSQL.append(getSQLFieldConnector());

            fieldsSQL.append(addQuotesForSQL(column.getOriginalDbColumnName()));
            if (i < listColumns.size() - 1) {
                fieldsSQL.append(SQL_SPLIT_FIELD);
                fieldsSQL.append(SPACE);
            }
        }
        return fieldsSQL.toString();
    }
}
