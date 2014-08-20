// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.context;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess2;
import org.talend.designer.core.ui.editor.cmd.ContextAddParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextChangeDefaultCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRenameParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextTemplateModifyCommand;

/**
 * This class must be extended for implementing the specific context composite. <br/>
 * 
 */
public abstract class ContextComposite extends Composite implements IContextModelManager {

    private boolean readOnly;

    private ContextTemplateComposite template;

    private ContextTreeValuesComposite treeValues;

    private ContextTableValuesComposite tableValues;

    private ContextNebulaGridComposite tableNebulas;

    private CTabFolder tab;

    private boolean isRepositoryContext;

    private IContextManager contextManager;

    private static final int PAGE = 2;

    protected EditorPart part = null;

    /**
     * bqian ContextComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ContextComposite(Composite parent, IContextManager contextManager) {
        this(parent, contextManager, true);
    }

    public ContextComposite(Composite parent, boolean isRepositoryContext) {
        this(parent, null, isRepositoryContext);
    }

    public ContextComposite(Composite parent, IContextManager contextManager, boolean isRepositoryContext) {
        super(parent, SWT.NONE);
        this.contextManager = contextManager;
        this.isRepositoryContext = isRepositoryContext;
        this.setBackground(parent.getBackground());
        this.setLayout(new GridLayout());
        initializeUI();
    }

    public void setPart(EditorPart part) {
        this.part = part;
        refreshTemplateTab();
        refreshTableTab();
        refreshTreeTab();
    }

    public void setTabEnable(boolean enable) {

        // no need to set the ConextTreeValuesComposite and ConextTableValuesComposite. They can take care of
        // themselvies.
        boolean flag = false;
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            if (page.getActiveEditor() instanceof MultiPageEditorPart) {
                MultiPageEditorPart editor = (MultiPageEditorPart) page.getActiveEditor();
                if (editor != null) {
                    if (editor.getActivePage() == PAGE) {
                        flag = true;
                    }
                }
            }
        }
        if (flag) {
            tableNebulas.setEnabled(false);
        } else {
            tableNebulas.setEnabled(enable);
        }
    }

    @Override
    public void refresh() {
        refreshTab();
    }

    @Override
    public void refreshTemplateTab() {
        refreshTab();
    }

    private void refreshTab() {
        if (getContextManager() == null) {
            this.setEnabled(false);
            tableNebulas.clear();
            tableNebulas.setEnabled(isReadOnly());
        } else {
            this.setEnabled(true);
            setTabEnable(!isReadOnly());
            toolgeRefreshContextRelitiveComposite(tableNebulas);
        }

        if (getContextManager() != null) {
            getContextManager().fireContextsChangedEvent();
        }
    }

    @Override
    public void refreshTableTab() {
        refreshTab();
    }

    @Override
    public void refreshTreeTab() {
        refreshTab();
    }

    /**
     * 
     * DOC YeXiaowei Comment method "refreshContextEditComposite".
     * 
     * @param composite
     */
    private void toolgeRefreshContextRelitiveComposite(AbstractContextTabEditComposite composite) {
        if (composite == null) {
            return;
        }
        if (composite.isNeedRefresh()) {
            composite.refresh();
        }

        // set need refresh back to true
        composite.setNeedRefresh(true);
    }

    @Override
    public IContextManager getContextManager() {
        return this.contextManager;
    }

    /**
     * bqian Comment method "initializeUI".
     */
    protected void initializeUI() {

        tableNebulas = new ContextNebulaGridComposite(this, this);

        tableNebulas.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        tableNebulas.setLayoutData(gridData);
    }

    public CTabFolder getTableFolder() {
        return this.tab;
    }

    protected void layoutButtonBar() {
        this.layout();
    }

    private void creatTemplate(CTabFolder tab, CTabItem templateItem) {
        template = new ContextTemplateComposite(tab, this);
        templateItem.setControl(template);

    }

    private void creatTreeValues(CTabFolder tab, CTabItem treeValuesItem) {
        treeValues = new ContextTreeValuesComposite(tab, this);
        treeValuesItem.setControl(treeValues);
    }

    private void creatTableValues(CTabFolder tab, CTabItem tableValuesItem) {
        tableValues = new ContextTableValuesComposite(tab, this);
        tableValuesItem.setControl(tableValues);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#getProcess()
     */
    @Override
    public IProcess2 getProcess() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#getCommandStack()
     */
    @Override
    public CommandStack getCommandStack() {
        return null;
    }

    /**
     * Getter for isRepositoryContext.
     * 
     * @return the isRepositoryContext
     */
    @Override
    public boolean isRepositoryContext() {
        return this.isRepositoryContext;
    }

    public ContextTemplateComposite getContextTemplateComposite() {
        return this.template;
    }

    public ContextNebulaGridComposite getContextTableComposite() {
        return this.tableNebulas;
    }

    @Override
    public void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
        getCommandStack().execute(new ContextChangeDefaultCommand(contextManager, newDefault));
    }

    @Override
    public void onContextRenameParameter(IContextManager contextManager, String sourceId, String oldName, String newName) {
        if (contextManager instanceof JobContextManager) {
            JobContextManager manager = (JobContextManager) contextManager;
            manager.addNewName(newName, oldName);
            // record the modified operation.
            setModifiedFlag(contextManager);
        }
        getCommandStack().execute(new ContextRenameParameterCommand(contextManager, sourceId, oldName, newName));
        // update variable reference for current job, for 2608
        switchSettingsView(oldName, newName);
    }

    protected void switchSettingsView(String oldName, String newName) {
        // sub-class implement this method.
    }

    @Override
    public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
        if (contextManager instanceof JobContextManager) {
            JobContextManager manager = (JobContextManager) contextManager;
            manager.addNewName(newName, oldName);
            // record the modified operation.
            setModifiedFlag(contextManager);
        }
        getCommandStack().execute(new ContextRenameParameterCommand(contextManager, oldName, newName));
        // update variable reference for current job, for 2608
        switchSettingsView(oldName, newName);
    }

    @Override
    public void onContextModify(IContextManager contextManager, IContextParameter parameter) {
        // record the modified operation.
        setModifiedFlag(contextManager);
        getCommandStack().execute(new ContextTemplateModifyCommand(getProcess(), contextManager, parameter));
    }

    @Override
    public void onContextAddParameter(IContextManager contextManager, IContextParameter parameter) {
        getCommandStack().execute(new ContextAddParameterCommand(getContextManager(), parameter));
    }

    @Override
    public void onContextRemoveParameter(IContextManager contextManager, String paramName) {
        Set<String> names = new HashSet<String>();
        names.add(paramName);
        onContextRemoveParameter(contextManager, names);
    }

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
        // record the modified operation.
        setModifiedFlag(contextManager);
        getCommandStack().execute(new ContextRemoveParameterCommand(getContextManager(), paramNames));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
     * IContextManager, java.lang.String, java.lang.String)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, String paramName, String sourceId) {
        setModifiedFlag(contextManager);
        getCommandStack().execute(new ContextRemoveParameterCommand(getContextManager(), paramName, sourceId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
     * IContextManager, java.util.Set, java.lang.String)
     */
    @Override
    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames, String sourceId) {
        setModifiedFlag(contextManager);
        getCommandStack().execute(new ContextRemoveParameterCommand(getContextManager(), paramNames, sourceId));
    }
}
