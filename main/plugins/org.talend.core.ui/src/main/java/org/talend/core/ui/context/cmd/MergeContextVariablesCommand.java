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
package org.talend.core.ui.context.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.editor.command.ContextRemoveParameterCommand;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ContextTypeImpl;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MergeContextVariablesCommand extends Command {

    private IContextManager ctxManager = null;

    private List<ContextType> contexts = new ArrayList<ContextType>();

    private String defaultContextName = null;

    private String contextItemId = null;

    private Set<String> addedVars = new HashSet<String>();

    private Set<String> contextGoupNameSet = new HashSet<String>();

    private boolean fillFlag;

    private ContextManagerHelper helper;

    private List<ContextParameterType> newAddParameter = new ArrayList<ContextParameterType>();

    public MergeContextVariablesCommand(List<ContextType> contexts, String defaultContextName, String contextItemId,
            IContextManager ctxManager, Set<String> addedVars, Set<String> contextGoupNameSet, boolean fillFlag) {
        this.contexts = contexts;
        this.defaultContextName = defaultContextName;
        this.contextItemId = contextItemId;
        this.addedVars = addedVars;
        this.ctxManager = ctxManager;
        this.fillFlag = fillFlag;
        this.contextGoupNameSet = contextGoupNameSet;
        this.helper = new ContextManagerHelper(ctxManager);
    }

    @Override
    public void execute() {

        Map<String, List<ContextParameterType>> map = getContextNameParamsMap(contexts);
        if (map.isEmpty()) {
            return;
        }
        Set<String> existGroupNameSet = new HashSet<String>();
        Set<String> addedContextGroupNames = new HashSet<String>();
        Set<String> alreadyUpdateNameSet = new HashSet<String>();
        for (IContext con : ctxManager.getListContext()) {
            existGroupNameSet.add(con.getName());
        }
        if (contextGoupNameSet.isEmpty()) {
            contextGoupNameSet.add(defaultContextName);
        }
        for (String key : map.keySet()) {
            for (String groupName : contextGoupNameSet) {
                boolean isExtraGroup = false;
                for (String existGroup : existGroupNameSet) {
                    if (key.equals(existGroup)) {
                        isExtraGroup = true;
                        alreadyUpdateNameSet.add(existGroup);
                        break;
                    }
                }
                if (key.equals(groupName) || isExtraGroup) {
                    List<ContextParameterType> list = map.get(key);
                    newAddParameter.addAll(list);
                    JobContext jobContext = new JobContext(key);
                    boolean isExistContext = false;
                    if (isExtraGroup) {
                        for (IContext con : ctxManager.getListContext()) {
                            if (key.equals(con.getName())) {
                                if (con instanceof JobContext) {
                                    jobContext = (JobContext) con;
                                    isExistContext = true;
                                    break;
                                }
                            }
                        }
                    }
                    // add the new context's parameter into the new context group
                    setContextParameter(contextItemId, addedVars, list, jobContext);
                    if (!isExistContext) {
                        ctxManager.getListContext().add(jobContext);
                        addedContextGroupNames.add(jobContext.getName());
                    }
                    break;
                }
            }
        }
        // if job context group is not in current add's context,then update context group value to default group value
        existGroupNameSet.removeAll(alreadyUpdateNameSet);
        List<ContextParameterType> list = map.get(defaultContextName);
        if (list == null) {
            return;
        }
        for (String existGroup : existGroupNameSet) {
            for (IContext con : ctxManager.getListContext()) {
                if ((existGroup).equals(con.getName())) {
                    if (con instanceof JobContext) {
                        JobContext jobContext = (JobContext) con;
                        setContextParameter(contextItemId, addedVars, list, jobContext);
                    }
                }
            }
        }
        if (fillFlag) {
            // if the new conext group not in the job's context, then update the new context group value to the default
            // group value of the job
            List<IContextParameter> contextParameterList = ctxManager.getDefaultContext().getContextParameterList();
            if (contextParameterList == null) {
                return;
            }
            Set<String> existVars = new HashSet<String>();
            for (IContextParameter ctxPara : contextParameterList) {
                String ctxParaName = ctxPara.getName();
                if (!addedVars.contains(ctxParaName)) {
                    existVars.add(ctxParaName);
                }
            }
            for (String addedGroup : addedContextGroupNames) {
                for (IContext con : ctxManager.getListContext()) {
                    if (addedGroup.equals(con.getName())) {
                        if (con instanceof JobContext) {
                            JobContext jobContext = (JobContext) con;
                            setContextParameter(existVars, contextParameterList, jobContext);
                        }
                    }
                }
            }
        }

    }

    /**
     * add the variables from ctxParams into JobContext.
     * 
     * @param addedVars the variables need to adding
     * @param ctxParams the list of IContextParameter
     * @param jobContext the JobContext
     */
    private static void setContextParameter(Set<String> addedVars, List<IContextParameter> ctxParams, JobContext jobContext) {
        for (IContextParameter contextImpl : ctxParams) {
            for (String var : addedVars) {
                if (var.equals(contextImpl.getName())) {
                    JobContextParameter contextParam = new JobContextParameter();
                    ContextUtils.updateParameter(contextImpl, contextParam);
                    if (contextImpl.getSource() != null) {
                        contextParam.setSource(contextImpl.getSource());
                    }
                    contextParam.setContext(jobContext);
                    jobContext.getContextParameterList().add(contextParam);
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "getContextNameParamsMap".
     * 
     * @param contexts
     * @return
     */
    private static Map<String, List<ContextParameterType>> getContextNameParamsMap(List<ContextType> contexts) {
        Map<String, List<ContextParameterType>> map = new HashMap<String, List<ContextParameterType>>();
        Iterator<ContextType> iterator = contexts.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ContextTypeImpl) {
                ContextTypeImpl contextTypeImpl = (ContextTypeImpl) obj;
                String name = contextTypeImpl.getName();
                EList<ContextParameterType> contextParameters = contextTypeImpl.getContextParameter();
                Iterator<ContextParameterType> contextParas = contextParameters.iterator();
                List<ContextParameterType> list = new ArrayList<ContextParameterType>();
                while (contextParas.hasNext()) {
                    ContextParameterType contextParameterType = contextParas.next();
                    list.add(contextParameterType);
                }
                map.put(name, list);
            }
        }
        return map;
    }

    /**
     * add the variables from ctxParams into JobContext.
     * 
     * @param contextItemId the ContextItem's property Id
     * @param addedVars the variables need to adding
     * @param ctxParams the list of IContextParameter
     * @param jobContext the JobContext
     */
    public static void setContextParameter(String contextItemId, Set<String> addedVars, List<ContextParameterType> ctxParams,
            JobContext jobContext) {
        for (ContextParameterType contextImpl : ctxParams) {
            for (String var : addedVars) {
                if (var.equals(contextImpl.getName()) && jobContext.getContextParameter(var) == null) {
                    JobContextParameter contextParam = new JobContextParameter();
                    ContextUtils.updateParameter(contextImpl, contextParam);
                    if (contextItemId != null) {
                        contextParam.setSource(contextItemId);
                    }
                    contextParam.setContext(jobContext);
                    jobContext.getContextParameterList().add(contextParam);
                }
            }
        }
    }

    @Override
    public void redo() {
        execute();
        this.helper.refreshContextView();
    }

    @Override
    public void undo() {

        List<IContext> removeList = new ArrayList<IContext>();
        for (IContext con : ctxManager.getListContext()) {
            if (addedVars.contains(con.getName())) {
                removeList.add(con);
            }
        }
        ctxManager.getListContext().removeAll(removeList);

        for (ContextParameterType defaultContextParamType : newAddParameter) {
            ContextItem contextItem = (ContextItem) helper.getParentContextItem(defaultContextParamType);
            if (contextItem == null) {
                continue;
            }

            IContextParameter paramExisted = helper.getExistedContextParameter(defaultContextParamType.getName());
            if (paramExisted != null) {
                // modelManager.onContextRemoveParameter(manager, defaultContextParamType.getName(),
                // paramExisted.getSource());
                new ContextRemoveParameterCommand(ctxManager, defaultContextParamType.getName(), paramExisted.getSource())
                        .execute();
            }
        }
        helper.refreshContextView();

    }

}
