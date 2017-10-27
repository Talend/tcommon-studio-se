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
package org.talend.core.ui.context.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.editor.command.ContextRemoveParameterCommand;
import org.talend.core.ui.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * created by ldong on Sep 12, 2014 Detailled comment
 * 
 */
public class ContextBuiltinToRepositoryCommand extends Command {

    private List<IContextParameter> params = new ArrayList<IContextParameter>();

    private IContextManager contextManager;

    private ContextItem item;

    private ContextManagerHelper helper;

    /**
     * DOC ldong ContextBuiltinToRepositoryCommand constructor comment.
     * 
     * @param params
     * @param contextManager
     * @param item
     */
    public ContextBuiltinToRepositoryCommand(List<IContextParameter> params, IContextManager contextManager, ContextItem item) {
        super();
        this.params = params;
        this.contextManager = contextManager;
        this.item = item;
        this.helper = new ContextManagerHelper(contextManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        IContext jobDefaultContext = contextManager.getDefaultContext(); // get default context from job
        List<IContext> jobContextlist = contextManager.getListContext(); // get all context from job
        List<String> jobContextNames = getJobContextGroupNames(jobContextlist);
        if (item == null) {
            return;
        }
        EList contextList = item.getContext();

        boolean conflictflag = false;

        JobContextManager itemManager = new JobContextManager();
        itemManager.loadFromEmf(contextList, item.getDefaultContext());
        // this loop is used to get all context groups
        for (int i = 0, n = contextList.size(); i < n; i++) {
            ContextType type = (ContextType) contextList.get(i);
            if (type == null) {
                return;
            }
            EList contextParameters = type.getContextParameter(); // parameters from current contextType
            List<IContextParameter> allParameters = getAllExistContextParameters(contextParameters, params);
            // find one same context group,this group will get the
            // values from the same group of designer
            if (jobContextNames.contains(type.getName())) {
                int index = jobContextNames.indexOf(type.getName());
                IContext currentContext = jobContextlist.get(index);
                for (IContextParameter param : params) {
                    IContextParameter selectedParam = currentContext.getContextParameter(param.getName());
                    if (!allParameters.contains(param)) {
                        ContextParameterType parameter = TalendFileFactory.eINSTANCE.createContextParameterType();
                        copyContextParameter(item, contextParameters, parameter, selectedParam);
                    } else {
                        for (int k = 0; k < contextParameters.size(); k++) {
                            ContextParameterType parameter = (ContextParameterType) contextParameters.get(k);
                            if (parameter.getName().equals(param.getName())) {
                                conflictflag = true;
                                break;
                            }
                        }
                        if (conflictflag) {
                            // now if add the build-in param into repository context group,if already exist same
                            // one,just update the relation
                            addRelationForContextParameter(item, contextParameters, selectedParam);
                        }

                    }
                }
                // this group will get the vaules from default group of designer
            } else if (!jobContextNames.contains(type.getName()) && type.getName() != null) {
                for (IContextParameter param : params) {
                    IContextParameter selectedParam = jobDefaultContext.getContextParameter(param.getName());
                    if (!allParameters.contains(param)) {
                        ContextParameterType parameter = TalendFileFactory.eINSTANCE.createContextParameterType();
                        copyContextParameter(item, contextParameters, parameter, selectedParam);
                    } else {
                        for (int k = 0; k < contextParameters.size(); k++) {
                            ContextParameterType parameter = (ContextParameterType) contextParameters.get(k);
                            if (parameter.getName().equals(param.getName())) {
                                conflictflag = true;
                                break;
                            }
                        }
                        if (conflictflag) {
                            addRelationForContextParameter(item, contextParameters, selectedParam);
                        }
                    }
                }
            }
        }
        try {
            ProxyRepositoryFactory.getInstance().save(item, false);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    private List<IContextParameter> getAllExistContextParameters(EList contextParameters, List<IContextParameter> params) {
        List<IContextParameter> allParameters = new ArrayList<IContextParameter>();
        for (int i = 0; i < contextParameters.size(); i++) {
            ContextParameterType parameter = (ContextParameterType) contextParameters.get(i);
            for (IContextParameter param : params) {
                if (parameter.getName().equals(param.getName())) {
                    allParameters.add(param);
                }
            }
        }
        return allParameters;
    }

    /**
     * DOC xye Comment method "copyContextParameter".
     * 
     * @param context
     * @param item
     * @param contextParameters
     * @param parameter
     * @param param
     */
    @SuppressWarnings("unchecked")
    private void copyContextParameter(ContextItem item, EList contextParameters, ContextParameterType parameter,
            IContextParameter selectedParam) {
        parameter.setName(selectedParam.getName());
        parameter.setComment(selectedParam.getComment());
        parameter.setPrompt(selectedParam.getPrompt());
        parameter.setPromptNeeded(selectedParam.isPromptNeeded());
        parameter.setType(selectedParam.getType());
        parameter.setValue(selectedParam.getValue());
        contextParameters.add(parameter);
        selectedParam.setSource(item.getProperty().getId());
    }

    /**
     * DOC ldong Comment method "addRelationForContextParameter".
     * 
     * @param contextItem
     * @param parameterList
     * @param existParam
     */
    @SuppressWarnings("unchecked")
    private void addRelationForContextParameter(ContextItem contextItem, EList parameterList, IContextParameter existParam) {
        Iterator contextParamItor = parameterList.iterator();
        while (contextParamItor.hasNext()) {
            ContextParameterType defaultContextParamType = (ContextParameterType) contextParamItor.next();

            if (defaultContextParamType.getName().equals(existParam.getName())) {
                // existed.then create the relation and remove from job context parameters and update from the emf new
                // one

                boolean isContinue = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
                        Messages.getString("ContextTreeTable.AddToRepository_label"), //$NON-NLS-1$
                        Messages.getString("ContextBuiltinToRepositoryCommand.addRelation")); //$NON-NLS-1$

                if (isContinue) {

                    new AddContextGroupRelationCommand(contextManager, existParam, contextItem).execute();

                    new ContextRemoveParameterCommand(contextManager, defaultContextParamType.getName(), existParam.getSource())
                            .execute();
                    helper.addContextParameterType(defaultContextParamType);
                }
            }
        }
    }

    /**
     * DOC hyWang Comment method "getJobContextGroupNames".
     */
    private List<String> getJobContextGroupNames(List<IContext> jobContextlist) {
        List<String> jobContextNames = new ArrayList<String>();
        // parameters from designer of default context
        for (int k = 0; k < jobContextlist.size(); k++) {
            jobContextNames.add(k, jobContextlist.get(k).getName());
        }
        return jobContextNames;
    }

}
