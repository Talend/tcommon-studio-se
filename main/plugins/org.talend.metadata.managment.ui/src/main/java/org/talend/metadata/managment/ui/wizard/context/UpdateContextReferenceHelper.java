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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.process.IGEFProcess;
import org.talend.core.ui.process.UpdateRunJobComponentContextHelper;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
@Deprecated
public final class UpdateContextReferenceHelper {

    /**
     * only update tRunJob component.
     * 
     * the function has moved to UpdateContextParameterCommand for update manager(bug 3993).
     * 
     */
    @Deprecated
    public static synchronized void updateJobContextReference(JobContextManager curRepositoryManager, ContextItem curContextItem) {
        final Set<String> repositoryVarsSet = getCurRepositoryContextVarsName(curRepositoryManager);
        final Map<String, String> renamedMap = curRepositoryManager.getNameMap();

        // String curContextName = curContextItem.getProperty().getId();
        String curContextId = curContextItem.getProperty().getId();
        if (renamedMap != null && !renamedMap.isEmpty()) {
            // if rename the context variable naem, update the item context.
            updateProcessItemforVariablesReference(curRepositoryManager, curContextId, repositoryVarsSet, renamedMap);
            // update the opened context.
            updateOpenedJobforVariablesReference(curRepositoryManager, curContextId, repositoryVarsSet, renamedMap);
        }
    }

    /*
     * update the opened job context.
     */
    private static void updateOpenedJobforVariablesReference(JobContextManager curRepositoryManager, final String contextId,
            final Set<String> repositoryVarsSet, final Map<String, String> renamedMap) {
        if (contextId == null) {
            return;
        }

        IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        List<IProcess2> processes = CoreRuntimePlugin.getInstance().getDesignerCoreService().getOpenedProcess(reference);
        for (IProcess2 process : processes) {
            IContextManager openedContextManager = process.getContextManager();

            boolean modified = false; // checked the done again flag.

            // update paramters reference for context. for 2608
            Map<String, String> realRenamedVarMap = new HashMap<String, String>();
            boolean recordFlag = false;

            for (IContext context : openedContextManager.getListContext()) {
                for (IContextParameter contextParameter : context.getContextParameterList()) {
                    if (!contextParameter.isBuiltIn()) {
                        String source = contextParameter.getSource();
                        if (source.equals(contextId)) {
                            // check variable reference of current repository context
                            final String paramName = contextParameter.getName();
                            String newName = getRenamedVarName(paramName, renamedMap);
                            if (newName != null && !newName.equals(paramName)) {
                                // for 2608
                                if (!recordFlag && !realRenamedVarMap.containsKey(newName)) {
                                    realRenamedVarMap.put(newName, paramName);
                                }

                                // have renamed this variable.
                                // moved to method checkRenamedContext of ProcessUpdateManger

                                // contextParameter.setName(newName);
                                // updateVariableAttributions(curRepositoryManager, contextParameter,
                                // context.getName());

                                // } else if (!isExistedVarInCurRepositoryContext(contextParameter.getName(),
                                // repositoryVarsSet)) {
                                // check the nonexistent variable and set to built-in.
                                // contextParameter.setSource(IContextParameter.BUILT_IN);
                                // } else {
                                // update other variable value.
                                // updateVariableAttributions(curRepositoryManager, contextParameter,
                                // context.getName());

                            }
                            modified = true;
                        }
                    }
                }
                recordFlag = true;
                if (!modified) {
                    // not existed current source variables.
                    break;
                }
            }
            if (modified) {
                // update tRunJob component reference
                UpdateRunJobComponentContextHelper.updateOpenedJobRunJobComponentReference(processes, realRenamedVarMap, process
                        .getProperty().getId(), null);
                // update parameter for current job and nodes in it. for 2608
                // UpdateContextVariablesHelper.updateProcessForRenamed(process, realRenamedVarMap);

                // update the job state
                if (process instanceof IGEFProcess) {
                    IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
                    if (designerCoreUIService != null) {
                        designerCoreUIService.executeCommand((IGEFProcess) process, new Command() {
                        });
                    }
                }
            }
        }

    }

    private static Set<String> getCurRepositoryContextVarsName(JobContextManager curRepositoryManager) {
        if (curRepositoryManager == null) {
            return Collections.emptySet();
        }
        Set<String> varNameSet = new HashSet<String>();
        for (IContextParameter param : curRepositoryManager.getDefaultContext().getContextParameterList()) {
            varNameSet.add(param.getName());
        }
        return varNameSet;
    }

    private static String getRenamedVarName(final String varName, Map<String, String> renamedMap) {
        if (varName == null || renamedMap == null || renamedMap.isEmpty()) {
            return null;
        }

        Set<String> keySet = renamedMap.keySet();
        for (String newName : keySet) {
            String oldName = renamedMap.get(newName);
            if (varName.equals(oldName)) {
                return newName;
            }
        }
        return null;
    }

    private static boolean isExistedVarInCurRepositoryContext(final String varName, final Set<String> repositoryVarsSet) {
        if (repositoryVarsSet == null || repositoryVarsSet.isEmpty()) {
            return false;
        }
        if (repositoryVarsSet.contains(varName)) {
            return true;
        }
        return false;
    }

    /*
     * if not existed current variable in the context. get the default to set.
     */
    private static boolean updateVariableAttributions(JobContextManager curRepositoryManager, Object modifiedParameter,
            final String jobContextname) {
        if (curRepositoryManager == null || modifiedParameter == null) {
            return false;
        }
        IContext context = null;
        if (jobContextname != null) {
            context = curRepositoryManager.getContext(jobContextname);
        }
        if (context == null) {
            context = curRepositoryManager.getDefaultContext();
        }
        if (context != null) {
            if (modifiedParameter instanceof IContextParameter) {
                // update the opened job
                IContextParameter jobContextParameter = (IContextParameter) modifiedParameter;
                IContextParameter parameter = context.getContextParameter(jobContextParameter.getName());

                jobContextParameter.setComment(parameter.getComment());
                jobContextParameter.setPrompt(parameter.getPrompt());
                jobContextParameter.setPromptNeeded(parameter.isPromptNeeded());
                jobContextParameter.setScriptCode(parameter.getScriptCode());
                jobContextParameter.setType(parameter.getType());
                jobContextParameter.setValue(parameter.getValue());
                if (parameter.getValueList() != null) {
                    jobContextParameter.setValueList(parameter.getValueList());
                }

                return true;
            } else if (modifiedParameter instanceof ContextParameterType) {
                // update the item
                ContextParameterType itemParameterType = (ContextParameterType) modifiedParameter;
                IContextParameter parameter = context.getContextParameter(itemParameterType.getName());

                itemParameterType.setComment(parameter.getComment());
                itemParameterType.setPrompt(parameter.getPrompt());
                itemParameterType.setPromptNeeded(parameter.isPromptNeeded());
                itemParameterType.setType(parameter.getType());
                itemParameterType.setRawValue(parameter.getValue());
                return true;

            }
        }
        return false;
    }

    /*
     * update the item.
     */
    private static void updateProcessItemforVariablesReference(JobContextManager curRepositoryManager, final String curContextId,
            final Set<String> repositoryVarsSet, final Map<String, String> renamedMap) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        try {
            List<IRepositoryViewObject> repositoryObjList = factory.getAll(ERepositoryObjectType.PROCESS, true);
            for (IRepositoryViewObject repositoryObj : repositoryObjList) {
                List<IRepositoryViewObject> allVersion = factory.getAllVersion(repositoryObj.getId());
                for (IRepositoryViewObject object : allVersion) {
                    ProcessItem item = (ProcessItem) object.getProperty().getItem();

                    if (item != null) {
                        boolean modified = false; // checked the done again flag.

                        // update paramters reference for context. for 2608
                        Map<String, String> realRenamedVarMap = new HashMap<String, String>();
                        boolean recordFlag = false;

                        for (ContextType contextType : (List<ContextType>) item.getProcess().getContext()) {
                            for (ContextParameterType parameterType : (List<ContextParameterType>) contextType
                                    .getContextParameter()) {
                                String repositoryId = parameterType.getRepositoryContextId();
                                if (repositoryId != null && repositoryId.equals(curContextId)) {
                                    // found the current reference variable
                                    final String paramName = parameterType.getName();
                                    String newName = getRenamedVarName(paramName, renamedMap);
                                    if (newName != null && !newName.equals(paramName)) {
                                        // for 2608
                                        if (!recordFlag && !realRenamedVarMap.containsKey(newName)) {
                                            realRenamedVarMap.put(newName, paramName);
                                        }

                                        // have renamed this variable.
                                        // moved to method checkRenamedContext of ProcessUpdateManger

                                        // parameterType.setName(newName);
                                        // updateVariableAttributions(curRepositoryManager, parameterType,
                                        // contextType.getName());
                                        // } else if (!isExistedVarInCurRepositoryContext(parameterType.getName(),
                                        // repositoryVarsSet)) {
                                        // check the nonexistent variable and set to built-in.
                                        // parameterType.setRepositoryContextId(null);
                                        // } else {
                                        // update other variable value.
                                        // updateVariableAttributions(curRepositoryManager, parameterType,
                                        // contextType.getName());
                                    }
                                    modified = true;
                                }
                            }
                            recordFlag = true;
                            if (!modified) {
                                break;
                            }
                        }
                        if (modified) {
                            // update tRunJob component reference
                            UpdateRunJobComponentContextHelper.updateItemRunJobComponentReference(factory, realRenamedVarMap,
                                    item.getProperty().getId(), null);

                            // update parameter reference for current item and nodes. for 2608
                            // UpdateContextVariablesHelper.updateProcessForRenamed(item.getProcess(),
                            // realRenamedVarMap);

                            factory.save(item);
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

    }
}
