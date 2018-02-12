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
 * DOC talend2 class global comment. Detailled comment
 */
public class CheckAndAddContextDNDCommand extends Command {

    private List<ContextType> contexts = new ArrayList<ContextType>();

    private String defaultContextName;

    private String contextItemId;

    private IContextManager ctxManager;

    private Set<String> addedVars = new HashSet<String>();

    private List<ContextParameterType> newAddParameter = new ArrayList<ContextParameterType>();

    private ContextManagerHelper helper;

    public CheckAndAddContextDNDCommand(List<ContextType> contexts, String defaultContextName, String contextItemId,
            IContextManager ctxManager) {
        this.contexts = contexts;
        this.defaultContextName = defaultContextName;
        this.contextItemId = contextItemId;
        this.ctxManager = ctxManager;
        this.helper = new ContextManagerHelper(ctxManager);
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

    public void execute() {

        Map<String, List<ContextParameterType>> map = getContextNameParamsMap(contexts);
        if (map.isEmpty()) {
            return;
        }
        ctxManager.getListContext().clear();

        for (String key : map.keySet()) {
            List<ContextParameterType> list = map.get(key);
            newAddParameter.addAll(list);
            JobContext jobContext = new JobContext(key);
            for (ContextParameterType contextImpl : list) {
                JobContextParameter contextParam = new JobContextParameter();
                ContextUtils.updateParameter(contextImpl, contextParam);
                if (contextItemId != null) {
                    contextParam.setSource(contextItemId);
                }
                contextParam.setContext(jobContext);
                jobContext.getContextParameterList().add(contextParam);
            }
            ctxManager.getListContext().add(jobContext);
            if (key.equals(defaultContextName)) {
                ctxManager.setDefaultContext(jobContext);
            }
        }

    }

    public void redo() {
        execute();
        this.helper.refreshContextView();
    }

    public void undo() {

        ContextManagerHelper helper = new ContextManagerHelper(ctxManager);
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
