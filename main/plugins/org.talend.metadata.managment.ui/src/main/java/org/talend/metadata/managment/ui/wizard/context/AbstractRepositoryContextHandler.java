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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.ExtendedNodeConnectionContextUtils.EHadoopParamName;
import org.talend.metadata.managment.ui.utils.IRepositoryContextHandler;

/**
 * created by ldong on Mar 18, 2015 Detailled comment
 *
 */
public abstract class AbstractRepositoryContextHandler implements IRepositoryContextHandler {

    private final static String HADOOP_PROPERTY = "PROPERTY"; //$NON-NLS-1$

    private final static String HADOOP_VALUE = "VALUE"; //$NON-NLS-1$

    protected static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    protected String getCorrectVariableName(ContextItem contextItem, String originalVariableName, EHadoopParamName noSqlParam) {
        return getCorrectVariableName(contextItem, originalVariableName, noSqlParam.name());
    }
    
    protected String getCorrectVariableName(ContextItem contextItem, String originalVariableName, String noSqlParam) {
        Set<String> contextVarNames = ContextUtils.getContextVarNames(contextItem);
        if (contextVarNames != null && !contextVarNames.contains(originalVariableName)) {
            for (String varName : contextVarNames) {
                if (varName.endsWith(noSqlParam)) {
                    return varName;
                }
            }
        }
        return originalVariableName;
    }

    protected void createHadoopPropertiesContextVariable(String prefixName, List<IContextParameter> varList,
            String hadoopProperties) {
        List<Map<String, Object>> hadoopPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
        for (Map<String, Object> propertyMap : hadoopPropertiesList) {
            String propertyKey = (String) propertyMap.get(HADOOP_PROPERTY);
            String propertyValue = (String) propertyMap.get(HADOOP_VALUE);
            String keyWithPrefix = prefixName + ConnectionContextHelper.LINE + ContextParameterUtils.getValidParameterName(propertyKey);
            ConnectionContextHelper.createParameters(varList, keyWithPrefix, propertyValue);
        }
    }

    protected List<Map<String, Object>> transformHadoopPropertiesForContextMode(List<Map<String, Object>> hadoopPropertiesList,
            String prefixName) {
        for (Map<String, Object> propertyMap : hadoopPropertiesList) {
            String propertyKey = (String) propertyMap.get(HADOOP_PROPERTY);
            propertyMap.put(
                    HADOOP_VALUE,
                    ContextParameterUtils.getNewScriptCode(prefixName + ConnectionContextHelper.LINE
                            + ContextParameterUtils.getValidParameterName(propertyKey), LANGUAGE));
        }
        return hadoopPropertiesList;
    }

    protected List<Map<String, Object>> transformHadoopPropertiesForExistContextMode(
            List<Map<String, Object>> hadoopPropertiesList, String existPropertyKey, String existContextVariable) {
        for (Map<String, Object> propertyMap : hadoopPropertiesList) {
            String propertyKey = (String) propertyMap.get(HADOOP_PROPERTY);
            if (propertyKey.equals(existPropertyKey)) {
                propertyMap.put(HADOOP_VALUE, ContextParameterUtils.getNewScriptCode(existContextVariable, LANGUAGE));
            }
        }
        return hadoopPropertiesList;
    }

    protected List<Map<String, Object>> transformContextModeToHadoopProperties(List<Map<String, Object>> hadoopPropertiesList,
            ContextType contextType) {
        for (Map<String, Object> propertyMap : hadoopPropertiesList) {
            String contextPropertyValue = TalendQuoteUtils.removeQuotes(ContextParameterUtils.getOriginalValue(contextType,
                    (String) propertyMap.get(HADOOP_VALUE)));
            propertyMap.put(HADOOP_VALUE, contextPropertyValue);
        }
        return hadoopPropertiesList;
    }

    protected Set<String> getConAdditionProperties(List<Map<String, Object>> propertiesList) {
        Set<String> varList = new HashSet<String>();
        if (!propertiesList.isEmpty()) {
            for (Map<String, Object> propertyMap : propertiesList) {
                varList.add((String) propertyMap.get("PROPERTY"));
            }
        }
        return varList;
    }

    protected abstract void matchContextForAttribues(Connection conn, IConnParamName param, String contextVariableName);

    protected abstract void matchAdditionProperties(Connection conn,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap);
}
