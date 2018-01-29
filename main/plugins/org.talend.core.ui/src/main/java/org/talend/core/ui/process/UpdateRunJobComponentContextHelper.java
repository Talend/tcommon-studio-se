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
package org.talend.core.ui.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class UpdateRunJobComponentContextHelper {

    private static final String PROCESS_TYPE_PROCESS = "PROCESS_TYPE_PROCESS"; //$NON-NLS-1$

    private static final String PROCESS_TYPE_CONTEXT = "PROCESS_TYPE_CONTEXT"; //$NON-NLS-1$

    private static final String CONTEXTPARAMS = "CONTEXTPARAMS"; //$NON-NLS-1$

    private static final String PARAM_NAME_COLUMN = "PARAM_NAME_COLUMN"; //$NON-NLS-1$

    private static final String PARAM_VALUE_COLUMN = "PARAM_VALUE_COLUMN"; //$NON-NLS-1$

    private static final String TRUN_JOB = "tRunJob"; //$NON-NLS-1$

    @SuppressWarnings("unchecked")
    public static synchronized void updateOpenedJobRunJobComponentReference(final List<IProcess2> openedProcesses,
            final Map<String, String> nameMap, final String refJobId, final Set<String> varNameSet) {
        if (openedProcesses == null || refJobId == null) {
            return;
        }
        for (IProcess2 process : openedProcesses) {
            if (process.getId().equals(refJobId)) {
                // ignore self
                continue;
            }
            boolean changed = false;
            for (INode node : foundRunJobNode(process)) {
                IElementParameter eleParam = node.getElementParameter(PROCESS_TYPE_PROCESS);
                // found type
                if (eleParam != null && refJobId.equals(eleParam.getValue())) {
                    IElementParameter contextParam = node.getElementParameter(CONTEXTPARAMS);
                    if (contextParam != null) {
                        List<Map> valuesList = (List<Map>) contextParam.getValue();
                        List<Map> removedMap = new ArrayList<Map>();

                        for (Map valueMap : valuesList) {
                            Object obj = valueMap.get(PARAM_NAME_COLUMN);
                            if (obj != null && obj instanceof String) {
                                String oldName = (String) obj;
                                String newName = foundNewName(nameMap, oldName);
                                // found renamed parameter
                                if (newName != null) {
                                    valueMap.put(PARAM_NAME_COLUMN, newName);
                                    changed = true;
                                } else {
                                    // deleted parameter update, not existed in reference job context
                                    if (varNameSet != null && !varNameSet.contains(oldName)) {
                                        removedMap.add(valueMap);
                                    }
                                }
                            }
                            // Change value only for the job which context variable is changed. Changed by Marvin Wang
                            // on Dec. 28, 2012 for bug TDI-24255.
                            if (process.getId().equals(refJobId)) {
                                // bug 9424
                                obj = valueMap.get(PARAM_VALUE_COLUMN);
                                if (obj != null && obj instanceof String) {
                                    String oldValue = (String) obj;
                                    String newValue = checkAndUpdateValue(nameMap, oldValue);
                                    if (newValue != null) { // update
                                        valueMap.put(PARAM_VALUE_COLUMN, newValue);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        if (!removedMap.isEmpty()) {
                            for (Map valueMap : removedMap) {
                                valuesList.remove(valueMap);
                                changed = true;
                            }
                        }
                    }
                }
            }
            // update the job state
            if (changed && process instanceof IGEFProcess) {
                IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
                if (designerCoreUIService != null) {
                    designerCoreUIService.executeCommand((IGEFProcess) process, new Command() {
                    });
                }
            }
        }
    }

    private static String checkAndUpdateValue(Map<String, String> nameMap, final String oldValue) {
        final ECodeLanguage language = LanguageManager.getCurrentLanguage();
        if (nameMap != null && !nameMap.isEmpty() && oldValue != null) {
            String updateValue = oldValue;
            for (String newName : nameMap.keySet()) {
                String oldName = nameMap.get(newName);
                if (oldName != null && !newName.equals(oldName)) {
                    String oldCode = ContextParameterUtils.getNewScriptCode(oldName, language);
                    String newCode = ContextParameterUtils.getNewScriptCode(newName, language);
                    updateValue = ContextParameterUtils.updateValue(updateValue, oldCode, newCode);
                }
            }
            if (!oldValue.equals(updateValue)) { // update
                return updateValue;
            }
        }
        return null;
    }

    private static List<INode> foundRunJobNode(IProcess process) {
        List<INode> matchingNodes = new ArrayList<INode>();
        for (INode node : (List<INode>) process.getGeneratingNodes()) {
            if (TRUN_JOB.equals(node.getComponent().getName())) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }

    public static synchronized void updateItemRunJobComponentReference(final IProxyRepositoryFactory factory,
            final Map<String, String> nameMap, final String refJobId, final Set<String> varNameSet) throws PersistenceException {
        if (factory == null || refJobId == null) {
            return;
        }
        List<IRepositoryViewObject> repositoryObjectList = new ArrayList<IRepositoryViewObject>();
        List<IRepositoryViewObject> allVersionList = new ArrayList<IRepositoryViewObject>();

        ERepositoryObjectType jobType = ERepositoryObjectType.PROCESS;
        if (jobType != null) {
            repositoryObjectList.addAll(factory.getAll(jobType, true));
        }
        ERepositoryObjectType jobletType = ERepositoryObjectType.JOBLET;
        if (jobletType != null) {
            repositoryObjectList.addAll(factory.getAll(jobletType, true));
        }
        // must match TalendDesignerPrefConstants.CHECK_ONLY_LAST_VERSION
        // gcui:check it is update all version or last version.if the item is locked don't update it also.
        boolean checkOnlyLastVersion = Boolean.parseBoolean(CoreRuntimePlugin.getInstance().getDesignerCoreService()
                .getPreferenceStore("checkOnlyLastVersion")); //$NON-NLS-1$

        allVersionList = new ArrayList<IRepositoryViewObject>((int) (repositoryObjectList.size() * 1.1));
        for (IRepositoryViewObject rObject : repositoryObjectList) {
            if (!checkOnlyLastVersion) {
                List<IRepositoryViewObject> allVersion = factory.getAllVersion(rObject.getId());
                for (IRepositoryViewObject object : allVersion) {
                    ERepositoryStatus status = factory.getStatus(object);
                    if (status != ERepositoryStatus.LOCK_BY_OTHER && status != ERepositoryStatus.LOCK_BY_USER) {
                        allVersionList.add(object);

                    }
                }
            } else {
                IRepositoryViewObject lastVersion = rObject;
                ERepositoryStatus status = factory.getStatus(lastVersion);
                if (status != ERepositoryStatus.LOCK_BY_OTHER && status != ERepositoryStatus.LOCK_BY_USER) {
                    allVersionList.add(lastVersion);
                }
            }
            for (IRepositoryViewObject repositoryObj : allVersionList) {
                Item item = repositoryObj.getProperty().getItem();
                if (item instanceof ProcessItem) {
                    ProcessItem processItem = (ProcessItem) item;
                    if (processItem.getProperty().getId().equals(refJobId)) {
                        // ignore self
                        continue;
                    }
                    boolean modified = false;
                    modified = updateRunJobComponent(processItem, nameMap, refJobId, varNameSet);

                    if (modified) {
                        factory.save(processItem);
                    }
                }
            }
        }

    }

    private static List<NodeType> foundRunJobNodeType(ProcessItem processItem) {
        List<NodeType> matchingNodes = new ArrayList<NodeType>();
        for (Object nodeObj : processItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) nodeObj;
            if (TRUN_JOB.equals(nodeType.getComponentName())) {
                matchingNodes.add(nodeType);
            }
        }
        return matchingNodes;
    }

    private static String foundNewName(Map<String, String> nameMap, String oldName) {
        if (oldName == null || nameMap == null) {
            return null;
        }
        for (String newName : nameMap.keySet()) {
            if (nameMap.get(newName).equals(oldName) && !newName.equals(oldName)) {
                return newName;
            }
        }
        return null;
    }

    private static boolean updateRunJobComponent(final ProcessItem processItem, final Map<String, String> nameMap,
            final String refJobId, final Set<String> varNameSet) {

        boolean modified = false;
        for (NodeType foundNode : foundRunJobNodeType(processItem)) {
            // found tRunJob node
            boolean found = false;
            for (ElementParameterType paramType : (List<ElementParameterType>) foundNode.getElementParameter()) {
                if (paramType.getName().endsWith(PROCESS_TYPE_PROCESS) && paramType.getValue().equals(refJobId)) {
                    found = true;
                    continue;
                }
                // found current node reference
                if (found && paramType.getName().equals(CONTEXTPARAMS)) {
                    List<ElementValueType> eleValueTypeList = paramType.getElementValue();
                    List<ElementValueType> movedRecord = new ArrayList<ElementValueType>();

                    for (int i = 0; i < eleValueTypeList.size(); i++) {
                        ElementValueType eleValueType = eleValueTypeList.get(i);
                        if (eleValueType.getElementRef().equals(PARAM_NAME_COLUMN)) {
                            String newName = foundNewName(nameMap, eleValueType.getValue());
                            if (newName != null) {
                                // renamed parameter update
                                eleValueType.setValue(newName);
                                modified = true;
                            } else {
                                // deleted parameter update, not existed in reference job context
                                if (varNameSet != null && !varNameSet.contains(eleValueType.getValue())) {
                                    ElementValueType valueType = eleValueTypeList.get(i + 1);
                                    if (valueType != null && valueType.getElementRef().equals(PARAM_VALUE_COLUMN)) {
                                        movedRecord.add(eleValueType);
                                        movedRecord.add(valueType);
                                        modified = true;
                                    }
                                }
                            }
                        }

                        // Change value only for the job which context variable is changed. Changed by Marvin Wang
                        // on Jan. 24, for bug TDI-24255. Context value should not be changed, because the context
                        // value is from the current job like "context.foo".
                        // bug 9424
                        // if (eleValueType.getElementRef().equals(PARAM_VALUE_COLUMN)) {
                        // String oldValue = eleValueType.getValue();
                        // String newValue = checkAndUpdateValue(nameMap, oldValue);
                        // if (newValue != null) { // update
                        // eleValueType.setValue(newValue);
                        // modified = true;
                        // }
                        // }

                    }
                    if (!movedRecord.isEmpty()) {
                        for (ElementValueType eleValueType : movedRecord) {
                            eleValueTypeList.remove(eleValueType);
                        }
                    }
                }
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    public static synchronized void updateRefJobRunJobComponentContext(final IProxyRepositoryFactory factory,
            final List<ProcessItem> refProcesses, final IProcess refProcess) {
        if (refProcesses == null || refProcess == null) {
            return;
        }
        String id = refProcess.getId();
        if (id == null) {
            return;
        }
        boolean changed = false;
        for (ProcessItem processItem : refProcesses) {
            if (processItem.getProperty().getId().equals(id)) {
                continue;
            }

            for (NodeType foundNode : foundRunJobNodeType(processItem)) {
                // found tRunJob node
                boolean found = false;

                for (ElementParameterType paramType : (List<ElementParameterType>) foundNode.getElementParameter()) {
                    String value = paramType.getValue();
                    if (paramType.getName().endsWith(PROCESS_TYPE_PROCESS) && value.equals(id)) {
                        found = true;
                        continue;
                    }
                    if (found && paramType.getName().endsWith(PROCESS_TYPE_CONTEXT)) {
                        IContextManager contextManager = refProcess.getContextManager();
                        List<IContext> listContext = contextManager.getListContext();
                        boolean exist = false;
                        for (IContext con : listContext) {
                            String name = con.getName();
                            if (value.equals(name)) {
                                exist = true;
                                break;
                            }
                        }

                        if (!exist) {
                            paramType.setValue(contextManager.getDefaultContext().getName());
                            changed = true;
                        } else {
                            paramType.setValue(value);
                        }
                        // save related job if default context changed
                        if (changed) {
                            try {
                                factory.save(processItem);
                            } catch (PersistenceException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }

    }
}
