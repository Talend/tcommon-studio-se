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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.CloneConnectionUtils;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.IConnParamName;

/**
 * ggu class global comment. Detailled comment
 */
public final class FileConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    public enum EFileParamName implements IConnParamName {
        // Server,
        File,
        RowSeparator,
        Encoding,
        Limit,
        Header,
        Footer,
        //
        FieldSeparator,
        RegExpression, // regular
        // excel
        ThousandSeparator,
        DecimalSeparator,
        LastColumn,
        FirstColumn,
    }

    static List<IContextParameter> getFileVariables(String prefixName, FileConnection conn, Set<IConnParamName> paramSet) {
        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EFileParamName) {
                EFileParamName fileParam = (EFileParamName) param;
                paramName = prefixName + fileParam;
                switch (fileParam) {
                case File:
                    String filePath = conn.getFilePath();
                    if (LANGUAGE == ECodeLanguage.PERL) {
                        filePath = TalendQuoteUtils.addQuotes(filePath);
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, filePath, JavaTypesManager.FILE);
                    break;
                case Encoding:
                    // qli modified to fix the bug 6995.
                    String encoding = conn.getEncoding();
                    if (LANGUAGE.equals(ECodeLanguage.PERL)) {
                        encoding = TalendQuoteUtils.addQuotes(encoding);
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, encoding);
                    break;
                case Limit:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getLimitValue(), JavaTypesManager.INTEGER);
                    break;
                case Header:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getHeaderValue(), JavaTypesManager.INTEGER);
                    break;
                case Footer:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getFooterValue(), JavaTypesManager.INTEGER);
                    break;
                case RowSeparator:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                            || conn instanceof RegexpFileConnection) {
                        ConnectionContextHelper.createParameters(varList, paramName, conn.getRowSeparatorValue());
                    }
                    break;
                case FieldSeparator:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection) {
                        ConnectionContextHelper.createParameters(varList, paramName, conn.getFieldSeparatorValue());
                    }
                    break;
                case RegExpression:
                    if (conn instanceof RegexpFileConnection) {
                        ConnectionContextHelper.createParameters(varList, paramName, conn.getFieldSeparatorValue());
                    }
                    break;
                default:
                    if (conn instanceof FileExcelConnection) {
                        FileExcelConnection excelConn = (FileExcelConnection) conn;
                        switch (fileParam) {
                        case FirstColumn:
                            ConnectionContextHelper.createParameters(varList, paramName, excelConn.getFirstColumn(),
                                    JavaTypesManager.INTEGER);
                            break;
                        case LastColumn:
                            ConnectionContextHelper.createParameters(varList, paramName, excelConn.getLastColumn(),
                                    JavaTypesManager.INTEGER);
                            break;
                        case ThousandSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                ConnectionContextHelper.createParameters(varList, paramName, excelConn.getThousandSeparator());
                            }
                            break;
                        case DecimalSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                ConnectionContextHelper.createParameters(varList, paramName, excelConn.getDecimalSeparator());
                            }
                            break;
                        default:
                        }
                    }
                    break;
                }
            }
        }

        return varList;
    }

    static void setPropertiesForContextMode(String prefixName, FileConnection conn, Set<IConnParamName> paramSet) {
        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EFileParamName) {
                EFileParamName fileParam = (EFileParamName) param;
                paramName = prefixName + fileParam;
                switch (fileParam) {
                case File:
                    conn.setFilePath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Encoding:
                    conn.setEncoding(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Limit:
                    conn.setLimitValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Header:
                    conn.setHeaderValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case Footer:
                    conn.setFooterValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    break;
                case RowSeparator:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                            || conn instanceof RegexpFileConnection) {
                        conn.setRowSeparatorValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                    }
                    break;
                case FieldSeparator:
                case RegExpression:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                            || conn instanceof RegexpFileConnection) {
                        conn.setFieldSeparatorValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                        if (conn instanceof DelimitedFileConnection) {
                            ((DelimitedFileConnection) conn).setFieldSeparatorType(FieldSeparator.CUSTOM_UTF8_LITERAL);
                        }
                    }
                    break;
                default:
                    if (conn instanceof FileExcelConnection) {
                        FileExcelConnection excelConn = (FileExcelConnection) conn;
                        switch (fileParam) {
                        case FirstColumn:
                            excelConn.setFirstColumn(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                            break;
                        case LastColumn:
                            excelConn.setLastColumn(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                            break;
                        case ThousandSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                excelConn.setThousandSeparator(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                            }
                            break;
                        case DecimalSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                excelConn.setDecimalSeparator(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                            }
                            break;
                        default:
                        }
                    }
                    break;
                }
            }
        }

    }

    static void setPropertiesForExistContextMode(FileConnection conn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        String fileVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EFileParamName) {
                fileVariableName = "";
                EFileParamName fileParam = (EFileParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(fileParam.name())) {
                                fileVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }

                switch (fileParam) {
                case File:
                    conn.setFilePath(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    break;
                case Encoding:
                    conn.setEncoding(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    break;
                case Limit:
                    conn.setLimitValue(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    break;
                case Header:
                    conn.setHeaderValue(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    break;
                case Footer:
                    conn.setFooterValue(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    break;
                case RowSeparator:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                            || conn instanceof RegexpFileConnection) {
                        conn.setRowSeparatorValue(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                    }
                    break;
                case FieldSeparator:
                case RegExpression:
                    if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                            || conn instanceof RegexpFileConnection) {
                        conn.setFieldSeparatorValue(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                        if (conn instanceof DelimitedFileConnection) {
                            ((DelimitedFileConnection) conn).setFieldSeparatorType(FieldSeparator.CUSTOM_UTF8_LITERAL);
                        }
                    }
                    break;
                default:
                    if (conn instanceof FileExcelConnection) {
                        FileExcelConnection excelConn = (FileExcelConnection) conn;
                        switch (fileParam) {
                        case FirstColumn:
                            excelConn.setFirstColumn(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                            break;
                        case LastColumn:
                            excelConn.setLastColumn(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                            break;
                        case ThousandSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                excelConn
                                        .setThousandSeparator(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                            }
                            break;
                        case DecimalSeparator:
                            if (LANGUAGE == ECodeLanguage.JAVA) {
                                excelConn.setDecimalSeparator(ContextParameterUtils.getNewScriptCode(fileVariableName, LANGUAGE));
                            }
                            break;
                        default:
                        }
                    }
                    break;
                }
            }
        }

    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * @deprecated use instead CloneConnectionUtils.cloneOriginalValueConnection
     */
    @Deprecated
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
        return CloneConnectionUtils.cloneOriginalValueConnection(fileConn, false);
    }

    /**
     * @deprecated use instead CloneConnectionUtils.cloneOriginalValueConnection
     */
    @Deprecated
    public static FileConnection cloneOriginalValueConnection(Shell shell, FileConnection fileConn, boolean defaultContext) {

        return CloneConnectionUtils.cloneOriginalValueConnection(fileConn, defaultContext);
    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * @deprecated use instead CloneConnectionUtils.cloneOriginalValueConnection
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn, ContextType contextType) {
        return CloneConnectionUtils.cloneOriginalValueConnection(fileConn, contextType);
    }

    static void revertPropertiesForContextMode(FileConnection fileConn, ContextType contextType) {
        if (fileConn == null || contextType == null) {
            return;
        }
        String filePath = ContextParameterUtils.getOriginalValue(contextType, fileConn.getFilePath());
        String encoding = ContextParameterUtils.getOriginalValue(contextType, fileConn.getEncoding());
        String headValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getHeaderValue());
        String footerValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getFooterValue());
        String limitValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getLimitValue());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        // qli modified to fix the bug 6995.
        encoding = TalendQuoteUtils.removeQuotes(encoding);
        fileConn.setFilePath(filePath);
        fileConn.setEncoding(encoding);
        fileConn.setHeaderValue(headValue);
        fileConn.setFooterValue(footerValue);
        fileConn.setLimitValue(limitValue);

        //
        if (fileConn instanceof DelimitedFileConnection || fileConn instanceof PositionalFileConnection
                || fileConn instanceof RegexpFileConnection) {
            String fieldSeparatorValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getFieldSeparatorValue());
            String rowSeparatorValue = ContextParameterUtils.getOriginalValue(contextType, fileConn.getRowSeparatorValue());

            fileConn.setFieldSeparatorValue(fieldSeparatorValue);
            fileConn.setRowSeparatorValue(rowSeparatorValue);

        }
        // excel
        if (fileConn instanceof FileExcelConnection) {
            FileExcelConnection excelConnection = (FileExcelConnection) fileConn;

            String thousandSeparator = ContextParameterUtils
                    .getOriginalValue(contextType, excelConnection.getThousandSeparator());
            String decimalSeparator = ConnectionContextHelper
                    .getOriginalValue(contextType, excelConnection.getDecimalSeparator());
            String firstColumn = ContextParameterUtils.getOriginalValue(contextType, excelConnection.getFirstColumn());
            String lastColumn = ContextParameterUtils.getOriginalValue(contextType, excelConnection.getLastColumn());

            excelConnection.setThousandSeparator(thousandSeparator);
            excelConnection.setDecimalSeparator(decimalSeparator);
            excelConnection.setFirstColumn(firstColumn);
            excelConnection.setLastColumn(lastColumn);
        }
    }

    public static void checkAndInitRowsToSkip() {

    }

    /**
     * 
     * DOC zshen Comment method "retrieveFileConnection".
     * 
     * @param sourceFileConnection
     * @param targetFileConnection
     */
    public static void retrieveFileConnection(FileConnection sourceFileConnection, FileConnection targetFileConnection) {
        if (sourceFileConnection == null || targetFileConnection == null) {
            return;
        }
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(null, sourceFileConnection, null, false);
        retrieveFileConnection(sourceFileConnection, targetFileConnection, contextType);
    }

    /**
     * 
     * DOC zshen Comment method "retrieveFileConnection".
     * 
     * @param sourceFileConnection
     * @param targetFileConnection
     * @param contextType
     * 
     * retrieve FileConnection from sourceFileConnection to targetFileConnection
     */
    public static void retrieveFileConnection(FileConnection sourceFileConnection, FileConnection targetFileConnection,
            ContextType contextType) {
        if (sourceFileConnection == null || targetFileConnection == null) {
            return;
        }

        String filePath = ContextParameterUtils.getOriginalValue(contextType, sourceFileConnection.getFilePath());
        String encoding = ContextParameterUtils.getOriginalValue(contextType, sourceFileConnection.getEncoding());
        String headValue = ContextParameterUtils.getOriginalValue(contextType, sourceFileConnection.getHeaderValue());
        String footerValue = ContextParameterUtils.getOriginalValue(contextType, sourceFileConnection.getFooterValue());
        String limitValue = ContextParameterUtils.getOriginalValue(contextType, sourceFileConnection.getLimitValue());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        // qli modified to fix the bug 6995.
        encoding = TalendQuoteUtils.removeQuotes(encoding);
        targetFileConnection.setFilePath(filePath);
        targetFileConnection.setEncoding(encoding);
        targetFileConnection.setHeaderValue(headValue);
        targetFileConnection.setFooterValue(footerValue);
        targetFileConnection.setLimitValue(limitValue);

        //
        if (sourceFileConnection instanceof DelimitedFileConnection || sourceFileConnection instanceof PositionalFileConnection
                || sourceFileConnection instanceof RegexpFileConnection) {
            String fieldSeparatorValue = ContextParameterUtils.getOriginalValue(contextType,
                    sourceFileConnection.getFieldSeparatorValue());
            String rowSeparatorValue = ContextParameterUtils.getOriginalValue(contextType,
                    sourceFileConnection.getRowSeparatorValue());

            targetFileConnection.setFieldSeparatorValue(fieldSeparatorValue);
            targetFileConnection.setRowSeparatorValue(rowSeparatorValue);

            if (sourceFileConnection instanceof DelimitedFileConnection) {
                ((DelimitedFileConnection) targetFileConnection)
                        .setFieldSeparatorType(((DelimitedFileConnection) sourceFileConnection).getFieldSeparatorType());
            }
        }
        // excel
        if (sourceFileConnection instanceof FileExcelConnection) {
            FileExcelConnection excelConnection = (FileExcelConnection) sourceFileConnection;
            FileExcelConnection cloneExcelConnection = (FileExcelConnection) targetFileConnection;

            String thousandSeparator = ContextParameterUtils
                    .getOriginalValue(contextType, excelConnection.getThousandSeparator());
            String decimalSeparator = ContextParameterUtils.getOriginalValue(contextType, excelConnection.getDecimalSeparator());
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
        }
        targetFileConnection.setFieldSeparatorValue(sourceFileConnection.getFieldSeparatorValue());
        targetFileConnection.setRowSeparatorType(sourceFileConnection.getRowSeparatorType());
        targetFileConnection.setRowSeparatorValue(sourceFileConnection.getRowSeparatorValue());

        targetFileConnection.setRowSeparatorType(sourceFileConnection.getRowSeparatorType());

        targetFileConnection.setCsvOption(sourceFileConnection.isCsvOption());
        targetFileConnection.setEscapeChar(sourceFileConnection.getEscapeChar());
        targetFileConnection.setEscapeType(sourceFileConnection.getEscapeType());
        targetFileConnection.setFirstLineCaption(sourceFileConnection.isFirstLineCaption());
        targetFileConnection.setFormat(sourceFileConnection.getFormat());
        targetFileConnection.setRemoveEmptyRow(sourceFileConnection.isRemoveEmptyRow());
        targetFileConnection.setServer(sourceFileConnection.getServer());
        targetFileConnection.setTextEnclosure(sourceFileConnection.getTextEnclosure());
        targetFileConnection.setTextIdentifier(sourceFileConnection.getTextIdentifier());
        targetFileConnection.setUseFooter(sourceFileConnection.isUseFooter());
        targetFileConnection.setUseHeader(sourceFileConnection.isUseHeader());
        targetFileConnection.setUseLimit(sourceFileConnection.isUseLimit());

    }

    /**
     * 
     * DOC klliu Comment method "isFilePathAvailable".
     * 
     * @param fileStr
     * @param connection
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static BufferedReader isFilePathAvailable(String fileStr, FileConnection connection) throws IOException,
            UnsupportedEncodingException, FileNotFoundException {
        BufferedReader in;
        File file = new File(fileStr);
        Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
        if (connection.getEncoding() == null || connection.getEncoding().equals("")) { //$NON-NLS-1$
            connection.setEncoding(guessedCharset.displayName());
        }
        // read the file width the limit : MAXIMUM_ROWS_TO_PREVIEW
        in = new BufferedReader(new InputStreamReader(new FileInputStream(fileStr), guessedCharset.displayName()));

        return in;
    }

}
