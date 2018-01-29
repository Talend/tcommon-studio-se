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
package org.talend.core.ui.context.nattableTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.table.ContextTableConstants;

/**
 * created by ldong on Jul 11, 2014 Detailled comment
 * 
 */
public class ContextRowDataListFixture {

    public static final String CONTEXT_NAME = "NAME";

    public static final String CONTEXT_TYPE = "TYPE";

    public static final String CONTEXT_PROMPT = "PROMPT";

    public static final String CONTEXT_VALUE = "VALUE";

    public static List<IContext> getContexts(IContextManager contextManger) {
        List<IContext> contexts = new ArrayList<IContext>();
        if (contextManger != null) {
            contexts = contextManger.getListContext();
        }
        return contexts;
    }

    public static String[] getPropertyNames(IContextModelManager manager) {
        List<IContext> contexts = getContexts(manager.getContextManager());
        int columnCount = contexts.size() * 3 + 3;
        String[] columnsName = new String[columnCount];
        int i = 0;
        columnsName[i++] = ContextTableConstants.COLUMN_NAME_PROPERTY;
        columnsName[i++] = ContextTableConstants.COLUMN_TYPE_PROPERTY;
        columnsName[i++] = ContextTableConstants.COLUMN_COMMENT_PROPERTY;
        if (contexts.size() > 0) {
            for (IContext context : contexts) {
                columnsName[i++] = ContextTableConstants.COLUMN_CONTEXT_VALUE;
                columnsName[i++] = ContextTableConstants.COLUMN_CHECK_PROPERTY;
                columnsName[i++] = ContextTableConstants.COLUMN_PROMPT_PROPERTY;
            }
            return columnsName;
        }
        return columnsName;
    }

    public static Map<String, String> getPropertyToLabelMap(IContextModelManager manager) {
        Map propertyToLabelMap = new LinkedHashMap();
        List<IContext> contexts = getContexts(manager.getContextManager());
        propertyToLabelMap.put(ContextTableConstants.COLUMN_NAME_PROPERTY, ContextTableConstants.COLUMN_NAME_PROPERTY);
        propertyToLabelMap.put(ContextTableConstants.COLUMN_TYPE_PROPERTY, ContextTableConstants.COLUMN_TYPE_PROPERTY);
        propertyToLabelMap.put(ContextTableConstants.COLUMN_PROMPT_PROPERTY, ContextTableConstants.COLUMN_PROMPT_PROPERTY);
        propertyToLabelMap.put(ContextTableConstants.COLUMN_CONTEXT_VALUE, ContextTableConstants.COLUMN_CONTEXT_VALUE);
        for (IContext context : contexts) {
            propertyToLabelMap.put(context.getName(), context.getName());
        }
        return propertyToLabelMap;
    }

    public static List<String> getPropertyNamesAsList(IContextModelManager manager) {
        return Arrays.asList(getPropertyNames(manager));
    }

    public static int getColumnIndexOfProperty(String propertyName, IContextModelManager manager) {
        return getPropertyNamesAsList(manager).indexOf(propertyName);
    }
}
