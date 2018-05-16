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
package org.talend.core.repository.utils;

import java.util.Iterator;
import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class DatabaseConnectionParameterUtil {

    public static String getTrueParamValue(DatabaseConnection conn, String paramValue) {
        String trueSchemaName = null;
        if (conn.isContextMode()) { // if connection is contextmode
            if (conn.getContextId() != null && !"".equals(conn.getContextId())) { // hywang modified for //$NON-NLS-1$
                // 8846
                String contextID = conn.getContextId();
                // paramValue = ContextParameterUtils.getVariableFromCode(paramValue);
                IRepositoryViewObject repObj;
                try {
                    repObj = ProxyRepositoryFactory.getInstance().getLastVersion(contextID);
                    if (repObj.getProperty().getItem() instanceof ContextItem) {
                        ContextItem contextItem = (ContextItem) repObj.getProperty().getItem();
                        List list = contextItem.getContext();
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            Object o = it.next();
                            if (o instanceof ContextType) {
                                ContextType contextType = (ContextType) o;
                                List contextList = contextType.getContextParameter();
                                for (int i = 0; i < contextList.size(); i++) {
                                    Object obj = contextList.get(i);
                                    if (obj instanceof ContextParameterType) {
                                        ContextParameterType type = (ContextParameterType) obj;
                                        if (type.getName().equals(paramValue)) {
                                            paramValue = type.getRawValue();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (PersistenceException e1) {
                    ExceptionHandler.process(e1);
                }
            }
        }
        trueSchemaName = paramValue;
        return trueSchemaName;
    }

    public static String getContextTrueValue(DatabaseConnection conn, String paramValue) {
        if (conn.isContextMode()) { // if connection is contextmode
            if (conn.getContextId() != null && !"".equals(conn.getContextId())) { // hywang modified for //$NON-NLS-1$
                // 8846
                String contextID = conn.getContextId();
                String tempVlaue = ContextParameterUtils.getVariableFromCode(paramValue);
                if (tempVlaue == null) {
                    return "";
                }
                IRepositoryViewObject repObj;
                try {
                    repObj = ProxyRepositoryFactory.getInstance().getLastVersion(contextID);
                    if (repObj != null && repObj.getProperty() != null && repObj.getProperty().getItem() instanceof ContextItem) {
                        ContextItem contextItem = (ContextItem) repObj.getProperty().getItem();
                        List list = contextItem.getContext();
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            Object o = it.next();
                            if (o instanceof ContextType) {
                                ContextType contextType = (ContextType) o;
                                List contextList = contextType.getContextParameter();
                                for (int i = 0; i < contextList.size(); i++) {
                                    Object obj = contextList.get(i);
                                    if (obj instanceof ContextParameterType) {
                                        ContextParameterType type = (ContextParameterType) obj;
                                        if (type.getName().equals(tempVlaue)) {
                                            String value = type.getRawValue();
                                            if (value == null) {
                                                paramValue = "";
                                            } else {
                                                paramValue = value;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (PersistenceException e1) {
                    ExceptionHandler.process(e1);
                }
            }
        }
        return paramValue;
    }
}
