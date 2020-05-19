// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.link.ContextLinkService;
import org.talend.core.model.context.link.ContextParamLink;
import org.talend.core.model.context.link.ItemContextLink;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ContextUtils {

    private static final Logger LOGGER = Logger.getLogger(ContextUtils.class);

    private static final Set<String> JAVA_KEYWORDS = new HashSet<String>(Arrays.asList("abstract", "continue", "for", "new", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
            "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
            "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
            "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

    /**
     *
     * ggu Comment method "isJavaKeyWords".
     *
     */
    public static boolean isJavaKeyWords(final String name) {
        if (Platform.isRunning()) {
            IStatus status = JavaConventions.validateFieldName(name, JavaCore.getOption(JavaCore.COMPILER_SOURCE),
                    JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE));
            if (status.getSeverity() == IStatus.ERROR) {
                return true;
            }
        } else {// MOD sizhaoliu TDQ-9679 avoid calling JavaCore class when this method is called in components
            return name == null ? false : JAVA_KEYWORDS.contains(name.toLowerCase());
        }
        return false;
    }

    /**
     *
     * update the JobContextParameter form repository ContextItem by context name.
     *
     */
    public static boolean updateParameterFromRepository(Item sourceItem, IContextParameter contextParam, String contextName) {
        return updateParameterFromRepository(sourceItem, contextParam, contextName, null);
    }

    public static boolean updateParameterFromRepository(Item sourceItem, IContextParameter contextParam, String contextName,
            Map<String, String> renameMap) {
        if (sourceItem == null || contextParam == null) {
            return false;
        }
        // not found, use default.
        ContextType contextType = getContextTypeByName(sourceItem, contextName);

        if (contextType != null) {
            String paramName = contextParam.getName();
            String newName = ContextUtils.getNewNameFromRenameMap(renameMap, paramName);
            if (newName != null) {
                paramName = newName;
            }
            ContextParameterType parameterType = getContextParameterTypeByName(contextType, paramName);
            // found parameter, update it.
            if (parameterType != null) {
                contextParam.setComment(parameterType.getComment());
                contextParam.setPrompt(parameterType.getPrompt());
                contextParam.setPromptNeeded(parameterType.isPromptNeeded());
                contextParam.setType(parameterType.getType());
                contextParam.setValue(parameterType.getRawValue());
                if (!StringUtils.equals(contextParam.getName(), parameterType.getName())) {
                    contextParam.setName(parameterType.getName());
                }
                return true;
            }
        }
        return false;
    }

    /**
     *
     * get ContextType from the repository ContextItem by context name.
     *
     * if not found, check the byDefault to return default context or not.
     *
     *
     */
    @SuppressWarnings("unchecked")
    public static ContextType getContextTypeByName(ContextItem sourceItem, final String contextName, boolean byDefault) {
        if (sourceItem == null) {
            return null;
        }
        List<ContextType> contextTypeList = sourceItem.getContext();

        if (byDefault) {
            return getContextTypeByName(contextTypeList, contextName, sourceItem.getDefaultContext());
        }

        return getContextTypeByName(contextTypeList, contextName, null);
    }

    public static ContextType getContextTypeByName(List<ContextType> contextTypeList, final String contextName) {

        return getContextTypeByName(contextTypeList, contextName, null);

    }

    public static ContextType getContextTypeByName(List<ContextType> contextTypeList, final String contextName,
            final String defaultContextName) {
        if (checkObject(contextTypeList)) {
            return null;
        }
        if (checkObject(contextName) && checkObject(defaultContextName)) {
            return null;
        }
        ContextType contextType = null;
        ContextType defaultContextType = null;
        for (ContextType type : contextTypeList) {
            // Modified by Marvin Wang on Jun. 21, 2012 for bug TDI-21009. To avoid case sensitive.
            if (contextName != null && type.getName() != null && type.getName().toLowerCase().equals(contextName.toLowerCase())) {
                contextType = type;
            } else if (defaultContextName != null && type.getName() != null
                    && type.getName().toLowerCase().equals(defaultContextName.toLowerCase())) {
                defaultContextType = type;
            }
        }
        // not found the name of context, get the default context.
        if (contextType == null && defaultContextType != null) {
            contextType = defaultContextType;
        }
        return contextType;
    }

    /**
     *
     * get ContextParameterType form a ContextType by parameter name.
     */
    @SuppressWarnings("unchecked")
    public static ContextParameterType getContextParameterTypeByName(ContextType contextType, final String paramName) {
        if (contextType == null || paramName == null) {
            return null;
        }

        ContextParameterType parameterType = null;
        for (ContextParameterType param : (List<ContextParameterType>) contextType.getContextParameter()) {
            if (param.getName().equals(paramName)) {
                parameterType = param;
                break;
            }
        }
        return parameterType;
    }

    public static ContextParameterType getContextParameterTypeById(ContextType contextType, final String uuId,
            boolean isFromContextItem) {
        if (contextType == null || uuId == null) {
            return null;
        }

        ContextParameterType parameterType = null;
        for (ContextParameterType param : (List<ContextParameterType>) contextType.getContextParameter()) {
            String paramId = null;
            if (isFromContextItem) {
                paramId = ResourceHelper.getUUID(param);
            } else {
                paramId = param.getInternalId();
            }
            if (uuId.equals(paramId)) {
                parameterType = param;
                break;
            }
        }
        return parameterType;
    }

    @SuppressWarnings("unchecked")
    private static boolean checkObject(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.isEmpty()) {
                return true;
            }
        }
        if (obj instanceof String) {
            String string = (String) obj;
            if ("".equals(string.trim())) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     *
     * get ContextItem by the id.
     *
     * @deprecated by bug 13184
     */
    @Deprecated
    public static ContextItem getContextItemById(String contextId) {
        if (checkObject(contextId)) {
            return null;
        }

        List<ContextItem> contextItemList = getAllContextItem();

        return getContextItemById(contextItemList, contextId);
    }

    public static ContextItem getContextItemById2(String contextId) {
        if (IContextParameter.BUILT_IN.equals(contextId)) {
            return null;
        }
        if (checkObject(contextId)) {
            return null;
        }

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            final IRepositoryViewObject lastVersion = factory.getLastVersion(contextId);
            if (lastVersion != null) {
                final Item item = lastVersion.getProperty().getItem();
                if (item != null && item instanceof ContextItem) {
                    return (ContextItem) item;
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    public static ContextItem getContextItemById(List<ContextItem> contextItemList, String contextId) {
        if (IContextParameter.BUILT_IN.equals(contextId)) {
            return null;
        }
        if (checkObject(contextItemList) || checkObject(contextId)) {
            return null;
        }
        for (ContextItem item : contextItemList) {
            String id = item.getProperty().getId();
            if (id.equals(contextId)) {
                return item;
            }
        }

        return null;
    }

    /**
     *
     * get ContextItem by name.
     *
     * @deprecated by bug 13184
     */
    @Deprecated
    public static ContextItem getContextItemByName(String name) {
        if (checkObject(name)) {
            return null;
        }
        List<ContextItem> contextItemList = getAllContextItem();

        return getContextItemByName(contextItemList, name);

    }

    /**
     *
     * ggu Comment method "getContextItemByName".
     *
     * @deprecated by bug 13184
     */
    @Deprecated
    public static ContextItem getContextItemByName(List<ContextItem> contextItemList, String name) {
        if (checkObject(contextItemList) || checkObject(name)) {
            return null;
        }
        for (ContextItem item : contextItemList) {
            if (item.getProperty().getLabel().equals(name)) {
                return item;
            }
        }

        return null;
    }

    /**
     *
     * get all of repository ContextItem(not include deleted item).
     *
     */
    public static List<ContextItem> getAllContextItem() {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        List<ContextItem> contextItemList = null;
        try {
            contextItemList = factory.getContextItem();
        } catch (PersistenceException e) {
            return null;
        }
        return contextItemList;
    }

    /**
     *
     * get JobContext from ContextManager by name.
     *
     * if not found, check the byDefault to return default context or not.
     */
    public static IContext getContextByName(IContextManager contextManager, final String contextName, boolean byDefault) {
        if (checkObject(contextManager)) {
            return null;
        }
        if (contextName != null) {
            if (byDefault) {
                return contextManager.getContext(contextName);
            } else {
                for (IContext context : contextManager.getListContext()) {
                    if (context.getName().equals(contextName)) {
                        return context;
                    }
                }
            }
        }

        return null;
    }

    /**
     * DOC xqliu Comment method "updateParameter".
     *
     * @param sourceParam
     * @param targetParam
     */
    public static void updateParameter(IContextParameter sourceParam, IContextParameter targetParam) {
        if (checkObject(sourceParam) || checkObject(targetParam)) {
            return;
        }

        targetParam.setName(sourceParam.getName());
        targetParam.setPrompt(sourceParam.getPrompt());
        boolean exists = false;
        ECodeLanguage curLanguage = LanguageManager.getCurrentLanguage();
        if (curLanguage == ECodeLanguage.JAVA) {
            exists = true;
            try {
                ContextParameterJavaTypeManager.getJavaTypeFromId(sourceParam.getType());
            } catch (IllegalArgumentException e) {
                exists = false;
            }
        } else {
            String[] existingTypes;
            existingTypes = ContextParameterJavaTypeManager.getPerlTypesLabels();
            for (String existingType : existingTypes) {
                if (existingType.equals(sourceParam.getType())) {
                    exists = true;
                }
            }
        }
        if (exists) {
            targetParam.setType(sourceParam.getType());
        } else {
            targetParam.setType(MetadataTalendType.getDefaultTalendType());
        }
        targetParam.setValue(sourceParam.getValue());
        targetParam.setPromptNeeded(sourceParam.isPromptNeeded());
        targetParam.setComment(sourceParam.getComment());
        targetParam.setInternalId(sourceParam.getInternalId());
    }

    /**
     *
     * update the JobContextParameter form the ContextParameterType.
     */
    public static void updateParameter(ContextParameterType sourceParam, IContextParameter targetParam) {
        if (checkObject(sourceParam) || checkObject(targetParam)) {
            return;
        }

        targetParam.setName(sourceParam.getName());
        targetParam.setPrompt(sourceParam.getPrompt());
        boolean exists = false;
        ECodeLanguage curLanguage = LanguageManager.getCurrentLanguage();
        if (curLanguage == ECodeLanguage.JAVA) {
            exists = true;
            try {
                ContextParameterJavaTypeManager.getJavaTypeFromId(sourceParam.getType());
            } catch (IllegalArgumentException e) {
                exists = false;
            }
        } else {
            String[] existingTypes;
            existingTypes = ContextParameterJavaTypeManager.getPerlTypesLabels();
            for (String existingType : existingTypes) {
                if (existingType.equals(sourceParam.getType())) {
                    exists = true;
                }
            }
        }
        if (exists) {
            targetParam.setType(sourceParam.getType());
        } else {
            targetParam.setType(MetadataTalendType.getDefaultTalendType());
        }
        targetParam.setValue(sourceParam.getRawValue());
        targetParam.setPromptNeeded(sourceParam.isPromptNeeded());
        targetParam.setComment(sourceParam.getComment());
        targetParam.setInternalId(sourceParam.getInternalId());
    }

    public static Map<String, Item> getRepositoryContextItemIdMapping() {
        List<ContextItem> contextItemList = getAllContextItem();

        Map<String, Item> itemMap = new HashMap<String, Item>();

        if (checkObject(contextItemList)) {
            return itemMap;
        }

        for (ContextItem item : contextItemList) {
            itemMap.put(item.getProperty().getId(), item);
        }
        return itemMap;
    }

    /**
     *
     * get the repository context item,now contextId can be either joblet node or context node.
     */
    public static Item getRepositoryContextItemById(String contextId) {
        if (IContextParameter.BUILT_IN.equals(contextId)) {
            return null;
        }
        if (checkObject(contextId)) {
            return null;
        }

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            final IRepositoryViewObject lastVersion = factory.getLastVersion(contextId);
            if (lastVersion != null) {
                final Item item = lastVersion.getProperty().getItem();
                if (item != null) {
                    return item;
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     *
     * ggu Comment method "getContextItemVarName".
     *
     * get the variable name of the ContextItem
     */
    public static Set<String> getContextVarNames(ContextItem item) {
        if (item == null) {
            return Collections.emptySet();
        }

        ContextType contextType = ContextUtils.getContextTypeByName(item, item.getDefaultContext(), true);

        return getContextVarNames(contextType);
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getContextVarNames(ContextType contextType) {
        if (contextType == null) {
            return Collections.emptySet();
        }
        Set<String> varNameSet = new HashSet<String>();
        for (ContextParameterType paramType : (List<ContextParameterType>) contextType.getContextParameter()) {
            varNameSet.add(paramType.getName());
        }
        return varNameSet;
    }

    /**
     *
     * ggu Comment method "sameValueContextParameter".
     *
     * not contain the source
     */
    public static boolean samePropertiesForContextParameter(IContextParameter sourceParam, ContextParameterType targetParamType) {
        if (targetParamType == null && sourceParam == null) {
            return true;
        }
        if (targetParamType != null && sourceParam != null) {
            // if (!sourceParam.getName().equals(targetParamType.getName())) {
            // return false;
            // }
            if (sourceParam.getComment() == null) {
                sourceParam.setComment(""); //$NON-NLS-1$
            }
            if (targetParamType.getComment() == null) {
                targetParamType.setComment(""); //$NON-NLS-1$
            }
            if (!sourceParam.getComment().equals(targetParamType.getComment())) {
                return false;
            }
            if (sourceParam.getPrompt() == null) {
                sourceParam.setPrompt(""); //$NON-NLS-1$
            }
            if (targetParamType.getPrompt() == null) {
                targetParamType.setPrompt(""); //$NON-NLS-1$
            }
            if (!sourceParam.getPrompt().equals(targetParamType.getPrompt())) {
                return false;
            }
            if (!sourceParam.getType().equals(targetParamType.getType())) {
                return false;
            }
            if (sourceParam.isPromptNeeded() != targetParamType.isPromptNeeded()) {
                return false;
            }
            // need check the raw value, because in sourceParam, it's raw
            if (!sourceParam.getValue().equals(targetParamType.getRawValue())) {
                return false;
            }

            return true;
        }
        return false;
    }

    public static boolean isPropagateContextVariable() {
        if (PluginChecker.isOnlyTopLoaded()) {
            return false;
        }

        // preference name must match TalendDesignerPrefConstants.PROPAGATE_CONTEXT_VARIABLE
        return Boolean.parseBoolean(
                CoreRuntimePlugin.getInstance().getDesignerCoreService().getPreferenceStore("propagateContextVariable")); //$NON-NLS-1$
    }

    /**
     *
     * ggu Comment method "addInContextModelForProcessItem".
     */
    public static boolean addInContextModelForProcessItem(Item item, Map<String, Set<String>> contextVars,
            List<ContextItem> allContextItems) {
        ProcessType processType = null;
        if (item instanceof ProcessItem) {
            processType = ((ProcessItem) item).getProcess();
        } else if (item instanceof JobletProcessItem) {
            processType = ((JobletProcessItem) item).getJobletProcess();
        }
        boolean added = false;
        if (processType != null) {
            if (allContextItems == null) {
                allContextItems = ContextUtils.getAllContextItem();
            }
            for (String id : contextVars.keySet()) {
                ConnectionItem connItem = MetadataToolHelper.getConnectionItemByItemId(id, false);
                if (connItem != null) {
                    String contextId = connItem.getConnection().getContextId();
                    if (contextId != null) {
                        Set<String> set = contextVars.get(id);
                        if (set != null) {
                            ContextItem contextItem = getContextItemById(allContextItems, contextId);
                            ContextType contextType = getContextTypeByName(contextItem, contextItem.getDefaultContext(), true);
                            JobContextManager processJobManager = new JobContextManager(processType.getContext(),
                                    processType.getDefaultContext());

                            boolean modified = false;
                            for (String varName : set) {
                                ContextParameterType contextParameterType = ContextUtils
                                        .getContextParameterTypeByName(contextType, varName);
                                IContextParameter contextParameter = processJobManager.getDefaultContext()
                                        .getContextParameter(varName);
                                if (contextParameter == null) { // added
                                    addContextParameterType(processJobManager, contextItem, contextParameterType);
                                    modified = true;
                                }
                            }
                            if (modified) {
                                processJobManager.saveToEmf(processType.getContext(), true);
                                added = true;
                            }
                        }

                    }
                }
            }
        }
        return added;
    }

    public static void addContextParameterType(IContextManager manager, ContextItem contextItem,
            ContextParameterType setContextParameterType) {
        for (IContext context : manager.getListContext()) {
            ContextParameterType foundParam = getContextParameterType(contextItem, setContextParameterType, context.getName(),
                    false);
            if (foundParam == null) {
                // not found, set the default
                foundParam = getContextParameterType(contextItem, setContextParameterType, context.getName(), true);
            }
            if (foundParam != null) {
                JobContextParameter jobParam = createJobContextParameter(contextItem, foundParam);
                IContextParameter existedContextParameter = getExistedContextParameter(manager, foundParam.getName(), context);
                if (existedContextParameter == null) {
                    context.getContextParameterList().add(jobParam);
                }
            }
        }
    }

    public static IContextParameter getExistedContextParameter(IContextManager manager, String paramName, IContext context) {
        if (context == null) {
            context = manager.getDefaultContext();
        }
        return context.getContextParameter(paramName);
    }

    @SuppressWarnings("unchecked")
    private static ContextParameterType getContextParameterType(ContextItem item,
            ContextParameterType defaultContextParameterType, String typeName, boolean defaultType) {
        if (checkObject(item) || checkObject(defaultContextParameterType) || checkObject(typeName)) {
            return null;
        }
        if (defaultType) { // default ContextType
            typeName = item.getDefaultContext();
        }
        for (Object obj : item.getContext()) {
            ContextType type = (ContextType) obj;
            if (type.getName().equals(typeName)) {
                for (ContextParameterType param : (List<ContextParameterType>) type.getContextParameter()) {
                    if (param.getName().equals(defaultContextParameterType.getName())) {
                        return param;
                    }
                }
                break;
            }
        }

        return null;
    }

    /*
     * create the JobContextParameter form the contextParamType of contextItem.
     */
    private static JobContextParameter createJobContextParameter(ContextItem contextItem, ContextParameterType contextParamType) {
        if (checkObject(contextItem) || checkObject(contextParamType)) {
            return null;
        }
        JobContextParameter contextParam = new JobContextParameter();

        contextParam.setName(contextParamType.getName());
        contextParam.setPrompt(contextParamType.getPrompt());
        boolean exists = true;
        try {
            ContextParameterJavaTypeManager.getJavaTypeFromId(contextParamType.getType());
        } catch (IllegalArgumentException e) {
            exists = false;
        }
        if (exists) {
            contextParam.setType(contextParamType.getType());
        } else {
            contextParam.setType(MetadataTalendType.getDefaultTalendType());
        }
        // specially for Password type to get raw value.
        contextParam.setValue(contextParamType.getRawValue());

        contextParam.setPromptNeeded(contextParamType.isPromptNeeded());
        contextParam.setComment(contextParamType.getComment());
        contextParam.setInternalId(contextParamType.getInternalId());
        contextParam.setSource(contextItem.getProperty().getId());
        return contextParam;
    }

    /**
     * Get the context type from item (ContextItem/JobletProcessItem/ProcessItem), If the name is null will use default
     * context
     * 
     * @param item
     * @param contextName
     * @return
     */
    public static ContextType getContextTypeByName(Item item, String contextName) {
        if (item instanceof ContextItem) {
            ContextItem contextItem = (ContextItem) item;
            if (contextName == null) {
                contextName = contextItem.getDefaultContext();
            }
            return ContextUtils.getContextTypeByName(contextItem, contextName, true);
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletProcessItem = (JobletProcessItem) item;
            return ContextUtils.getContextTypeByName((List<ContextType>) jobletProcessItem.getJobletProcess().getContext(),
                    contextName, jobletProcessItem.getJobletProcess().getDefaultContext());
        } else if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            return ContextUtils.getContextTypeByName((List<ContextType>) processItem.getProcess().getContext(), contextName,
                    processItem.getProcess().getDefaultContext());
        }
        return null;
    }

    public static String getDefaultContextName(Item item) {
        if (item instanceof ContextItem) {
            ContextItem contextItem = (ContextItem) item;
            return contextItem.getDefaultContext();
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletProcessItem = (JobletProcessItem) item;
            return jobletProcessItem.getJobletProcess().getDefaultContext();
        } else if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            return processItem.getProcess().getDefaultContext();
        }
        return null;
    }

    public static EList getAllContextType(Item item) {
        if (item instanceof ContextItem) {
            ContextItem contextItem = (ContextItem) item;
            return contextItem.getContext();
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletProcessItem = (JobletProcessItem) item;
            return jobletProcessItem.getJobletProcess().getContext();
        } else if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            return processItem.getProcess().getContext();
        }
        return null;
    }

    public static Map<String, String> getContextParamterRenamedMap(Item item) {
        ItemContextLink itemContextLink = null;
        try {
            itemContextLink = ContextLinkService.getInstance().loadContextLinkFromJson(item);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        if (itemContextLink != null) {
            if (item instanceof ConnectionItem) {
                return compareConnectionContextParamName((ConnectionItem) item, itemContextLink);
            } else {
                return compareContextParamName(item, itemContextLink);
            }
        }

        return Collections.EMPTY_MAP;
    }

    private static Map<String, String> compareContextParamName(Item processItem, ItemContextLink itemContextLink) {
        List<ContextType> contextTypeList = getAllContextType(processItem);
        return compareContextParamName(contextTypeList, itemContextLink);
    }

    public static Map<String, String> compareContextParamName(List<ContextType> contextTypeList,
            ItemContextLink itemContextLink) {
        Map<String, String> renamedMap = new HashMap<String, String>();
        Map<String, Item> tempItemMap = new HashMap<String, Item>();
        for (ContextType contextType : contextTypeList) {
            for (Object obj : contextType.getContextParameter()) {
                if (obj instanceof ContextParameterType) {
                    ContextParameterType contextParameterType = (ContextParameterType) obj;
                    ContextParamLink paramLink = itemContextLink.findContextParamLinkByName(
                            contextParameterType.getRepositoryContextId(), contextType.getName(), contextParameterType.getName());
                    if (paramLink != null) {
                        Item item = tempItemMap.get(contextParameterType.getRepositoryContextId());
                        if (item == null) {
                            item = ContextUtils.getRepositoryContextItemById(contextParameterType.getRepositoryContextId());
                            tempItemMap.put(contextParameterType.getRepositoryContextId(), item);
                        }
                        if (item != null) {
                            final ContextType repoContextType = ContextUtils.getContextTypeByName(item, contextType.getName());
                            ContextParameterType repoContextParam = ContextUtils.getContextParameterTypeById(repoContextType,
                                    paramLink.getId(), item instanceof ContextItem);
                            if (repoContextParam != null
                                    && !StringUtils.equals(repoContextParam.getName(), contextParameterType.getName())) {
                                renamedMap.put(repoContextParam.getName(), contextParameterType.getName());
                            }
                        }
                    }
                }
            }
        }
        return renamedMap;
    }

    private static Map<String, String> compareConnectionContextParamName(ConnectionItem connectionItem,
            ItemContextLink itemContextLink) {
        Map<String, String> renamedMap = new HashMap<String, String>();
        if (connectionItem.getConnection().isContextMode()) {
            ContextItem contextItem = ContextUtils.getContextItemById2(connectionItem.getConnection().getContextId());
            if (contextItem != null) {
                ContextType contextType = ContextUtils.getContextTypeByName(contextItem,
                        connectionItem.getConnection().getContextName(), false);
                if (contextType != null) {
                    for (Object obj : contextType.getContextParameter()) {
                        if (obj instanceof ContextParameterType) {
                            ContextParameterType paramType = (ContextParameterType) obj;
                            ContextParamLink paramLink = itemContextLink.findContextParamLinkById(
                                    connectionItem.getConnection().getContextId(),
                                    connectionItem.getConnection().getContextName(), ResourceHelper.getUUID(paramType));
                            if (paramLink != null && !StringUtils.equals(paramType.getName(), paramLink.getName())) {
                                renamedMap.put(paramType.getName(), paramLink.getName());
                            }
                        }
                    }
                }
            }
        }
        return renamedMap;
    }

    /**
     * 
     * @param itemId
     * @param contextType
     * @return rename map. Key is new name and value is old name.
     */
    public static Map<String, String> calculateRenamedMapFromLinkFile(String projectLabel, String itemId, IContext context,
            Item repoContextItem) {
        Map<String, String> renamedMap = new HashMap<String, String>();
        Map<String, Item> idToItemMap = new HashMap<String, Item>();
        if (repoContextItem != null) {
            idToItemMap.put(repoContextItem.getProperty().getId(), repoContextItem);
        }
        try {
            ItemContextLink itemContextLink = ContextLinkService.getInstance().doLoadContextLinkFromJson(projectLabel, itemId);
            if (itemContextLink != null) {
                for (Object obj : context.getContextParameterList()) {
                    if (obj instanceof IContextParameter) {
                        IContextParameter parameterType = (IContextParameter) obj;
                        ContextParamLink parameterLink = itemContextLink.findContextParamLinkByName(parameterType.getSource(),
                                context.getName(), parameterType.getName());
                        if (parameterLink != null) {
                            Item item = idToItemMap.get(parameterType.getSource());
                            if (item == null) {
                                item = getRepositoryContextItemById(parameterType.getSource());
                                idToItemMap.put(parameterType.getSource(), item);
                            }
                            if (item != null) {
                                ContextType contextType = ContextUtils.getContextTypeByName(item, context.getName());
                                ContextParameterType repoParameterType = ContextUtils.getContextParameterTypeById(contextType,
                                        parameterLink.getId(), item instanceof ContextItem);
                                if (repoParameterType != null
                                        && !StringUtils.equals(repoParameterType.getName(), parameterType.getName())) {
                                    renamedMap.put(repoParameterType.getName(), parameterType.getName());
                                }

                            }
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return renamedMap;
    }

    /**
     *
     * DOC hcw ProcessUpdateManager class global comment. Detailled comment
     */
    public static class ContextItemParamMap {

        private Map<Item, Set<String>> map = new HashMap<Item, Set<String>>();

        public void add(Item item, String param) {
            Set<String> params = map.get(item);
            if (params == null) {
                params = new HashSet<String>();
                map.put(item, params);
            }
            params.add(param);
        }

        @SuppressWarnings("unchecked")
        public Set<String> get(Item item) {
            Set<String> params = map.get(item);
            return (params == null) ? Collections.EMPTY_SET : params;

        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public Set<Item> getContexts() {
            return map.keySet();
        }
    }

    public static boolean compareContextParameter(Item contextItem, ContextType contextType, IContextParameter param,
            ContextParamLink paramLink, Map<Item, Map<String, String>> repositoryRenamedMap, Map<Item, Set<String>> existedParams,
            ContextItemParamMap unsameMap, ContextItemParamMap deleteParams, boolean onlySimpleShow, boolean isDefaultContext) {
        boolean builtin = true;
        String paramName = param.getName();
        if (paramLink != null && paramLink.getId() != null && contextType != null) {// Compare use UUID
            String paramId = paramLink.getId();
            ContextParameterType contextParameterType = null;
            contextParameterType = getContextParameterTypeById(contextType, paramId, contextItem instanceof ContextItem);
            if (contextParameterType != null) {
                if (!StringUtils.equals(contextParameterType.getName(), paramName)) {
                    if (isDefaultContext) {
                        Map<String, String> renameMap = repositoryRenamedMap.get(contextItem);
                        if (renameMap == null) {
                            renameMap = new HashMap<String, String>();
                            repositoryRenamedMap.put(contextItem, renameMap);
                        }
                        renameMap.put(contextParameterType.getName(), paramName);
                    }
                } else {
                    if (isDefaultContext) {
                        if (existedParams.get(contextItem) == null) {
                            existedParams.put(contextItem, new HashSet<String>());
                        }
                        existedParams.get(contextItem).add(paramName);
                    }
                    if (onlySimpleShow || !samePropertiesForContextParameter(param, contextParameterType)) {
                        unsameMap.add(contextItem, paramName);
                    }
                }
                builtin = false;
            } else {
                // delete context variable
                if (isPropagateContextVariable() && isDefaultContext) {
                    deleteParams.add(contextItem, paramName);
                    builtin = false;
                }
            }
        } else { // Compare use Name
            final ContextParameterType contextParameterType = ContextUtils.getContextParameterTypeByName(contextType, paramName);
            if (contextParameterType != null) {
                Item repositoryContext = contextItem;
                if (isDefaultContext) {
                    if (existedParams.get(contextItem) == null) {
                        existedParams.put(repositoryContext, new HashSet<String>());
                    }
                    existedParams.get(repositoryContext).add(paramName);
                }
                if (onlySimpleShow || !ContextUtils.samePropertiesForContextParameter(param, contextParameterType)) {
                    unsameMap.add(contextItem, paramName);
                }
                builtin = false;
            } else {
                // delete context variable
                if (ContextUtils.isPropagateContextVariable()) {
                    deleteParams.add(contextItem, paramName);
                    builtin = false;
                }
            }

        }
        return builtin && isDefaultContext;
    }

    public static String getParamId(IContextParameter param, ContextParamLink paramLink) {
        if (paramLink != null) {
            return paramLink.getId();
        }
        if (param != null) {
            return param.getInternalId();
        }
        return null;
    }

    public static Item findContextItem(List<ContextItem> allContextItem, String source) {
        if (allContextItem != null) {
            for (ContextItem contextItem : allContextItem) {
                if (StringUtils.equals(contextItem.getProperty().getId(), source)) {
                    return contextItem;
                }
            }
        }
        return getRepositoryContextItemById(source);
    }

    public static String getNewNameFromRenameMap(Map<String, String> renameMap, String oldName) {
        if (renameMap != null) {
            for (String key : renameMap.keySet()) {
                String value = renameMap.get(key);
                if (StringUtils.equals(value, oldName)) {
                    return key;
                }
            }
        }
        return null;
    }

    public static boolean isBuildInParameter(ContextParameterType paramType) {
        if (paramType.getRepositoryContextId() == null || IContextParameter.BUILT_IN.equals(paramType.getRepositoryContextId())) {
            return true;
        }
        return false;
    }
}
