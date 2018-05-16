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
package org.talend.core.model.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import orgomg.cwm.objectmodel.core.Package;

/**
 * created by nrousseau on Sep 4, 2013 Detailled comment
 * 
 */
public class CloneConnectionUtils {

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * perhaps, if connection is in context mode, will open dialog to choose context sets.
     */
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
        return cloneOriginalValueConnection(fileConn, false);
    }

    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn, boolean defaultContext) {
        if (fileConn == null) {
            return null;
        }
        String contextName = fileConn.getContextName();
        ContextType contextType = null;
        ContextItem contextItem = ContextUtils.getContextItemById2(fileConn.getContextId());
        if (contextItem != null) {
            if (contextName == null || defaultContext) {
                contextName = contextItem.getDefaultContext();
            }
            contextType = ContextUtils.getContextTypeByName(contextItem, contextName, true);
        }
        return cloneOriginalValueConnection(fileConn, contextType);
    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn, ContextType contextType) {
        if (fileConn == null) {
            return null;
        }

        FileConnection cloneConn = null;
        if (fileConn instanceof DelimitedFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        } else if (fileConn instanceof PositionalFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createPositionalFileConnection();
        } else if (fileConn instanceof RegexpFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createRegexpFileConnection();
        } else if (fileConn instanceof FileExcelConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createFileExcelConnection();
        } else if (fileConn instanceof HL7Connection) {
            cloneConn = ConnectionFactory.eINSTANCE.createHL7Connection();
        } else if (fileConn instanceof EbcdicConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createEbcdicConnection();
        }

        if (cloneConn != null) {
            String filePath = ContextParameterUtils.getOriginalValue(contextType, fileConn.getFilePath());
            String encoding = ContextParameterUtils.getOriginalValue(contextType, fileConn.getEncoding());
            String headValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getHeaderValue());
            String footerValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getFooterValue());
            String limitValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getLimitValue());

            filePath = TalendQuoteUtils.removeQuotes(filePath);
            // qli modified to fix the bug 6995.
            encoding = TalendQuoteUtils.removeQuotes(encoding);
            cloneConn.setFilePath(filePath);
            cloneConn.setEncoding(encoding);
            cloneConn.setHeaderValue(headValue);
            cloneConn.setFooterValue(footerValue);
            cloneConn.setLimitValue(limitValue);

            //
            if (fileConn instanceof DelimitedFileConnection || fileConn instanceof PositionalFileConnection
                    || fileConn instanceof RegexpFileConnection) {
                String fieldSeparatorValue = ContextParameterUtils.getOriginalValue(contextType,
                        fileConn.getFieldSeparatorValue());
                String rowSeparatorValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getRowSeparatorValue());

                cloneConn.setFieldSeparatorValue(fieldSeparatorValue);
                cloneConn.setRowSeparatorValue(rowSeparatorValue);

                if (fileConn instanceof DelimitedFileConnection) {
                    ((DelimitedFileConnection) cloneConn).setFieldSeparatorType(((DelimitedFileConnection) fileConn)
                            .getFieldSeparatorType());
                }
            }
            // excel
            if (fileConn instanceof FileExcelConnection) {
                FileExcelConnection excelConnection = (FileExcelConnection) fileConn;
                FileExcelConnection cloneExcelConnection = (FileExcelConnection) cloneConn;

                String thousandSeparator = ContextParameterUtils.getOriginalValue(contextType,
                        excelConnection.getThousandSeparator());
                String decimalSeparator = ContextParameterUtils.getOriginalValue(contextType,
                        excelConnection.getDecimalSeparator());
                String firstColumn = ContextParameterUtils.getOriginalValue(contextType, excelConnection.getFirstColumn());
                String lastColumn = ContextParameterUtils.getOriginalValue(contextType, excelConnection.getLastColumn());

                cloneExcelConnection.setThousandSeparator(thousandSeparator);
                cloneExcelConnection.setDecimalSeparator(decimalSeparator);
                cloneExcelConnection.setFirstColumn(firstColumn);
                cloneExcelConnection.setLastColumn(lastColumn);

                cloneExcelConnection.setSelectAllSheets(excelConnection.isSelectAllSheets());
                cloneExcelConnection.setSheetName(excelConnection.getSheetName());

                ArrayList sheetList = excelConnection.getSheetList();
                cloneExcelConnection.setSheetList((ArrayList) sheetList.clone());

                EList sheetColumns = excelConnection.getSheetColumns();
                if (sheetColumns != null && sheetColumns instanceof BasicEList) {
                    cloneExcelConnection.getSheetColumns().addAll((EList) ((BasicEList) sheetColumns).clone());
                }

                cloneExcelConnection.setAdvancedSpearator(excelConnection.isAdvancedSpearator());

                cloneConn.setFieldSeparatorValue(fileConn.getFieldSeparatorValue());
                cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());
                cloneConn.setRowSeparatorValue(fileConn.getRowSeparatorValue());
            }

            cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());

            cloneConn.setCsvOption(fileConn.isCsvOption());
            cloneConn.setEscapeChar(fileConn.getEscapeChar());
            cloneConn.setEscapeType(fileConn.getEscapeType());
            cloneConn.setFirstLineCaption(fileConn.isFirstLineCaption());
            cloneConn.setFormat(fileConn.getFormat());
            cloneConn.setRemoveEmptyRow(fileConn.isRemoveEmptyRow());
            cloneConn.setServer(fileConn.getServer());
            cloneConn.setTextEnclosure(fileConn.getTextEnclosure());
            cloneConn.setTextIdentifier(fileConn.getTextIdentifier());
            cloneConn.setUseFooter(fileConn.isUseFooter());
            cloneConn.setUseHeader(fileConn.isUseHeader());
            cloneConn.setUseLimit(fileConn.isUseLimit());

            cloneConnectionProperties(fileConn, cloneConn);
        }
        return cloneConn;
    }

    @SuppressWarnings("unchecked")
    public static void cloneConnectionProperties(Connection sourceConn, Connection targetConn) {
        if (sourceConn == null || targetConn == null) {
            return;
        }
        cloneConnectionProperties((AbstractMetadataObject) sourceConn, (AbstractMetadataObject) targetConn);

        // not clone
        // targetConn.setContextId(sourceConn.getContextId());
        // targetConn.setContextMode(sourceConn.isContextMode());
        targetConn.setVersion(sourceConn.getVersion());
        QueriesConnection queryConnection = sourceConn.getQueries();
        if (queryConnection != null) {
            QueriesConnection cloneQueriesConnection = ConnectionFactory.eINSTANCE.createQueriesConnection();

            cloneQueriesConnection.getQuery().clear();
            List<Query> queries = queryConnection.getQuery();
            for (Query query : queries) {
                Query cloneQuery = ConnectionFactory.eINSTANCE.createQuery();
                cloneConnectionProperties(query, cloneQuery);
                cloneQuery.setValue(query.getValue());

                cloneQuery.setQueries(cloneQueriesConnection);
                cloneQueriesConnection.getQuery().add(cloneQuery);
            }

            cloneQueriesConnection.setConnection(targetConn);
            targetConn.setQueries(cloneQueriesConnection);

        }
        // no need to clean targetconn's datapackage because when clone the connection,the datapacage won't be cloned.

        // Package pkg = (Package) ConnectionHelper.getPackage(targetConn.getName(), targetConn, Package.class);

        Set<MetadataTable> tables = ConnectionHelper.getTables(sourceConn);
        for (MetadataTable table : tables) {
            MetadataTable cloneTable = ConnectionFactory.eINSTANCE.createMetadataTable();

            cloneConnectionProperties(table, cloneTable);

            cloneTable.setActivatedCDC(table.isActivatedCDC());
            cloneTable.setAttachedCDC(table.isAttachedCDC());
            cloneTable.setTableType(table.getTableType());
            cloneTable.setSourceName(table.getSourceName());

            cloneTable.getColumns().clear();

            List<MetadataColumn> columns = table.getColumns();
            for (MetadataColumn column : columns) {
                MetadataColumn cloneColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();

                cloneConnectionProperties(column, cloneColumn);

                cloneColumn.setDefaultValue(column.getDefaultValue());
                cloneColumn.setDisplayField(column.getDisplayField());
                cloneColumn.setKey(column.isKey());
                cloneColumn.setLength(column.getLength());
                cloneColumn.setNullable(column.isNullable());
                cloneColumn.setOriginalField(column.getOriginalField());
                cloneColumn.setPattern(column.getPattern());
                cloneColumn.setPrecision(column.getPrecision());
                cloneColumn.setSourceType(column.getSourceType());
                cloneColumn.setTalendType(column.getTalendType());

                cloneColumn.setTable(cloneTable);
                cloneTable.getColumns().add(cloneColumn);
            }
            if (cloneTable.getNamespace() instanceof Package) {
                Package pkg = (Package) cloneTable.getNamespace();
                pkg.getDataManager().add(targetConn);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private static void cloneConnectionProperties(AbstractMetadataObject sourceObj, AbstractMetadataObject targetObj) {
        if (sourceObj == null || targetObj == null) {
            return;
        }
        targetObj.setComment(sourceObj.getComment());
        targetObj.setDivergency(sourceObj.isDivergency());
        targetObj.setId(sourceObj.getId());
        targetObj.setLabel(sourceObj.getLabel());
        // targetObj.setReadOnly(sourceObj.isReadOnly()); //can't set
        targetObj.setSynchronised(sourceObj.isSynchronised());
        HashMap properties = sourceObj.getProperties();
        if (properties != null) {
            targetObj.setProperties((HashMap) properties.clone());
        }
    }

}
