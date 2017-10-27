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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextComposite;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ContextRepositoryComposite extends ContextComposite {

    public ContextRepositoryComposite(Composite parent, IContextManager contextManager) {
        super(parent, contextManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.JobContextComposite#onContextAddParameter(org.talend.core.model.process.IContextManager
     * , org.talend.core.model.process.IContextParameter)
     */
    @Override
    public void onContextAddParameter(IContextManager contextManager, IContextParameter contextParam) {
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            IContext context = contextManager.getListContext().get(i);

            IContextParameter toAdd = contextParam.clone();
            toAdd.setContext(context);
            context.getContextParameterList().add(toAdd);
        }

        String addedNewName = contextParam.getName();

        if (((JobContextManager) contextManager).isOriginalParameter(addedNewName)) {
            setModifiedFlag(contextManager);
        }
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.JobContextComposite#onContextRenameParameter(org.talend.core.model.process.IContextManager
     * , java.lang.String, java.lang.String)
     */
    @Override
    public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
        boolean found;
        List<IContextParameter> listParams;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                if (listParams.get(j).getName().equals(oldName)) {
                    listParams.get(j).setName(newName);
                    // see 0003889: Context script code not refreshed.
                    String scriptCode = listParams.get(j).getScriptCode().replaceAll(oldName, newName);
                    listParams.get(j).setScriptCode(scriptCode);

                    // if the user haven't modified prompt, change it
                    if (listParams.get(j).getPrompt().equals(oldName + "?")) { //$NON-NLS-1$
                        listParams.get(j).setPrompt(newName + "?"); //$NON-NLS-1$
                    }

                    found = true;
                }
            }
        }
        JobContextManager manager = (JobContextManager) contextManager;
        manager.addNewName(newName, oldName);

        // record the modified operation.
        if (manager.isOriginalParameter(newName)) {
            setModifiedFlag(contextManager);
        }

        // refresh();
    }

    // public void onJobRenameParameter(IContextManager contextManager, String oldName, String newName) {
    // boolean found;
    // List<IContextParameter> listParams;
    //
    // for (int i = 0; i < contextManager.getListContext().size(); i++) {
    // listParams = contextManager.getListContext().get(i).getContextParameterList();
    // found = false;
    // for (int j = 0; j < listParams.size() && !found; j++) {
    // if (listParams.get(j).getName().equals(oldName)) {
    // listParams.get(j).setName(newName);
    // found = true;
    // }
    // }
    // }
    // refresh();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.JobContextComposite#onContextRemoveParameter(org.talend.core.model.process.IContextManager
     * , java.lang.String)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, String contextParamName) {
        Set<String> names = new HashSet<String>();
        names.add(contextParamName);
        onContextRemoveParameter(contextManager, names);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.JobContextComposite#onContextChangeDefault(org.talend.core.model.process.IContextManager
     * , org.talend.core.model.process.IContext)
     */
    @Override
    public void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
        contextManager.setDefaultContext(newDefault);
        // record the modified operation.
        setModifiedFlag(contextManager);
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#onContextModify(org.talend.core.model.process.IContextManager,
     * org.talend.core.model.process.IContextParameter)
     */
    @Override
    public void onContextModify(IContextManager contextManager, IContextParameter parameter) {
        propagateType(contextManager, parameter);
        // record the modified operation.
        setModifiedFlag(contextManager);
        refresh();
        ;
    }

    private void propagateType(IContextManager contextManager, IContextParameter param) {
        for (IContext context : contextManager.getListContext()) {
            IContextParameter paramToModify = context.getContextParameter(param.getName());
            paramToModify.setType(param.getType());
            paramToModify.setComment(param.getComment());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextAdd(org.talend.core.ui.context.JobContextComposite,
     * org.talend.core.model.process.IContext, org.eclipse.swt.custom.CCombo)
     */
    // public void onContextAdd(JobContextComposite composite, IContext newContext, CCombo combo) {
    // IContextManager contextManager = composite.getContextManager();
    // List<IContext> listContext = contextManager.getListContext();
    // listContext.add(newContext);
    // composite.addContext(newContext);
    //
    // String[] stringList = new String[listContext.size()];
    // for (int i = 0; i < listContext.size(); i++) {
    // stringList[i] = listContext.get(i).getName();
    // }
    //
    // combo.setItems(stringList);
    // contextManager.fireContextsChangedEvent();
    // }
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // org.talend.core.ui.context.JobContextComposite#onContextRemove(org.talend.core.ui.context.JobContextComposite,
    // * java.lang.String, org.eclipse.swt.custom.CCombo)
    // */
    // public void onContextRemove(JobContextComposite composite, String contextName, CCombo combo) {
    // IContextManager contextManager = composite.getContextManager();
    // List<IContext> listContext = contextManager.getListContext();
    // Map<IContext, TableViewerCreator> tableViewerCreatorMap = composite.getTableViewerCreatorMap();
    // CTabFolder tabFolder = composite.getTabFolder();
    // IContext context = null;
    //
    // boolean found = false;
    // for (int i = 0; i < listContext.size() && !found; i++) {
    // if (listContext.get(i).getName().equals(contextName)) {
    // context = listContext.get(i);
    // found = true;
    // }
    // }
    // found = false;
    // for (int i = 0; i < tabFolder.getItemCount() && !found; i++) {
    // if (tabFolder.getItem(i).getText().equals(contextName)) {
    // tabFolder.getItem(i).dispose();
    // found = true;
    // }
    // }
    //
    // listContext.remove(context);
    // tableViewerCreatorMap.remove(context);
    //
    // String[] stringList = new String[listContext.size()];
    // for (int i = 0; i < listContext.size(); i++) {
    // stringList[i] = listContext.get(i).getName();
    // }
    // combo.setItems(stringList);
    // contextManager.fireContextsChangedEvent();
    // }
    private void setModifiedFlag(IContextManager contextManager) {
        if (contextManager != null && contextManager instanceof JobContextManager) {
            JobContextManager manager = (JobContextManager) contextManager;
            // record the modified operation.
            manager.setModified(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
     * IContextManager, java.util.List)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames) {
        if (contextManager == null || paramNames == null || paramNames.isEmpty()) {
            return;
        }
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            List<IContextParameter> listParams = contextManager.getListContext().get(i).getContextParameterList();
            boolean found = false;
            List<IContextParameter> movedList = new ArrayList<IContextParameter>();

            for (int j = 0; j < listParams.size(); j++) {
                IContextParameter contextParameter = listParams.get(j);
                if (paramNames.contains(contextParameter.getName())) {
                    movedList.add(contextParameter);
                    found = true;
                }
                if (movedList.size() == paramNames.size()) { // has finished search
                    break;
                }
            }
            if (found) {
                listParams.removeAll(movedList);
            } else { // not find anything in first
                return;
            }
        }
        // record the modified operation.
        setModifiedFlag(contextManager);
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
     * IContextManager, java.lang.String, java.lang.String)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, String paramName, String sourceId) {
        Set<String> paramNames = new HashSet<String>();
        paramNames.add(paramName);
        onContextRemoveParameter(contextManager, paramNames, sourceId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
     * IContextManager, java.util.Set, java.lang.String)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames, String sourceId) {
        if (paramNames != null && paramNames.size() > 0) {
            Iterator<String> iterator = paramNames.iterator();
            while (iterator.hasNext()) {
                String contextParaName = iterator.next();
                removeParameterFromContext(contextManager, sourceId, contextParaName);
            }
        }
        // record the modified operation.
        setModifiedFlag(contextManager);
        refresh();
    }

    /**
     * This method is used to remove the <code>JobContextParameter</code> in <code>JobContext</code>, using the
     * combination of <code>sourceId</code> and <code>name</code> can identify the unique
     * <code>JobContextParameter</code>.
     * 
     * @param sourceId
     * @param name
     */
    private void removeParameterFromContext(IContextManager contextManager, String sourceId, String name) {
        List<IContext> list = contextManager.getListContext();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                IContext context = list.get(i);
                List<IContextParameter> contextParameters = context.getContextParameterList();
                List<IContextParameter> movedList = new ArrayList<IContextParameter>();
                if (contextParameters != null && contextParameters.size() > 0) {
                    for (int j = 0; j < contextParameters.size(); j++) {
                        IContextParameter contextPara = contextParameters.get(j);
                        String tempSourceId = contextPara.getSource();
                        String tempParaName = contextPara.getName();
                        if (tempSourceId.equals(sourceId) && tempParaName.equals(name)) {
                            movedList.add(contextPara);
                            contextParameters.remove(j);
                            break;
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#onContextRenameParameter(org.talend.core.model.process.
     * IContextManager, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void onContextRenameParameter(IContextManager contextManager, String sourceId, String oldName, String newName) {
        boolean found;
        List<IContextParameter> listParams;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                IContextParameter contextPara = listParams.get(j);
                String tempName = contextPara.getName();
                String tempSourceId = contextPara.getSource();
                if (tempName.equals(oldName) && tempSourceId.equals(sourceId)) {
                    contextPara.setName(newName);
                    // see 0003889: Context script code not refreshed.
                    String scriptCode = contextPara.getScriptCode().replaceAll(oldName, newName);
                    contextPara.setScriptCode(scriptCode);

                    // if the user haven't modified prompt, change it
                    if (contextPara.getPrompt().equals(oldName + "?")) { //$NON-NLS-1$
                        contextPara.setPrompt(newName + "?"); //$NON-NLS-1$
                    }

                    found = true;
                }
            }
        }
        JobContextManager manager = (JobContextManager) contextManager;
        manager.addNewName(newName, oldName);

        // record the modified operation.
        if (manager.isOriginalParameter(newName)) {
            setModifiedFlag(contextManager);
        }
    }

}
