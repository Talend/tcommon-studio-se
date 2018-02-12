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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (忙聵聼忙聹聼盲潞聰, 29 盲鹿聺忙聹聢 2006) nrousseau $
 * 
 */
public final class SAPConnectionUtils {

    /**
     * 
     * DOC YeXiaowei Comment method "isFunctionUnitExist".
     * 
     * @param connection
     * @param function
     * @return
     */
    public static SAPFunctionUnit findExistFunctionUnit(SAPConnection connection, final String functionName) {
        if (connection.getFuntions() == null || connection.getFuntions().isEmpty()) {
            return null;
        }

        for (int i = 0; i < connection.getFuntions().size(); i++) {
            SAPFunctionUnit unit = connection.getFuntions().get(i);
            if (unit.getLabel().equals(functionName)) {
                return unit;
            }
        }
        return null;
    }

    /**
     * DOC zli Comment method "findExistIDocUnit".
     * 
     * @param connection
     * @param iDocName
     * @return
     */
    public static SAPIDocUnit findExistIDocUnit(SAPConnection connection, final String iDocName) {
        if (connection.getIDocs() == null || connection.getIDocs().isEmpty()) {
            return null;
        }

        for (int i = 0; i < connection.getIDocs().size(); i++) {
            SAPIDocUnit unit = connection.getIDocs().get(i);
            if (unit.getName().equals(iDocName)) {
                return unit;
            }
        }
        return null;
    }

    /**
     * 
     * DOC YeXiaowei Comment method "sameParamterTableWith".
     * 
     * @param unit
     * @param table
     * @param value2
     * @return
     */
    public static boolean sameParamterTableWith(final SAPFunctionUnit unit, final List<Map<String, Object>> value2, boolean input) {
        List<Map<String, Object>> source = new ArrayList<Map<String, Object>>();
        RepositoryToComponentProperty.getSAPInputAndOutputValue(unit.getConnection(), source, unit.getLabel(), input);
        if (value2 == null) {
            return false;
        }
        if (source.size() == value2.size()) {
            if (!input) {
                for (Map<String, Object> value : source) {
                    // Output is base on schema
                    if (value.get("SCHEMA_COLUMN") == null) { //$NON-NLS-1$
                        value.put("SCHEMA_COLUMN", TalendTextUtils.removeQuotes((String) value.get("SAP_PARAMETER_NAME"))); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            } else {
                for (Map<String, Object> value : source) {
                    if (value.get("SAP_PARAMETER_VALUE") == null) { //$NON-NLS-1$
                        value.put("SAP_PARAMETER_VALUE", ""); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }

                for (Map<String, Object> value : value2) {
                    if (value.get("SAP_PARAMETER_VALUE") == null) { //$NON-NLS-1$
                        value.put("SAP_PARAMETER_VALUE", ""); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }

            return source.containsAll(value2) && value2.containsAll(source);
        } else {
            return false;
        }
    }

    /**
     * 
     * DOC YeXiaowei Comment method "sameParameterTable".
     * 
     * @param source
     * @param dest
     */
    public static boolean sameParameterTable(final SAPFunctionParameterTable source, final SAPFunctionParameterTable dest) {
        if (source == null && dest == null) {
            return false;
        }

        if ((source == null && dest != null) || (source != null && dest == null)) {
            return false;
        }
        // Just compare columns
        EList sourceColumns = source.getColumns();
        EList destColumns = dest.getColumns();
        if (sourceColumns == null && destColumns == null) {
            return true;
        }
        if (sourceColumns == null || destColumns == null) {
            return false;
        }
        if (sourceColumns.size() != destColumns.size()) {
            return false;
        }

        for (int i = 0; i < sourceColumns.size(); i++) {
            SAPFunctionParameterColumn sColumn = (SAPFunctionParameterColumn) sourceColumns.get(i);
            boolean existInDest = false;
            for (int j = 0; j < destColumns.size(); j++) {
                SAPFunctionParameterColumn dColumn = (SAPFunctionParameterColumn) sourceColumns.get(j);
                if (sameParameterColumn(sColumn, dColumn)) {
                    existInDest = true;
                    break;
                }
            }
            if (!existInDest) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * DOC YeXiaowei Comment method "sameParameterColumn".
     * 
     * @param source
     * @param dest
     * @return
     */
    private static boolean sameParameterColumn(final SAPFunctionParameterColumn source, final SAPFunctionParameterColumn dest) {
        return compareStringFiled(source, dest);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "compareStringFiled".
     * 
     * @param source
     * @param dest
     * @return
     */
    private static boolean compareStringFiled(Object source, Object dest) {
        try {
            Method[] methods = source.getClass().getDeclaredMethods();
            if (methods == null) {
                return false;
            }
            for (Method method : methods) {
                if (method.getReturnType().equals(java.lang.String.class)) {
                    if (method.getParameterTypes() == null) {
                        // Need compare
                        Object sValue = method.invoke(source, new Object[] {});
                        Object dValue = method.invoke(dest, new Object[] {});
                        if (!sValue.equals(dValue)) {
                            return false;
                        }
                    }
                }
            }

        } catch (SecurityException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return false;
    }
}
