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
package org.talend.core.ui.context;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
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
import org.talend.core.ui.editor.command.ContextAddParameterCommand;
import org.talend.core.ui.editor.command.ContextChangeDefaultCommand;
import org.talend.core.ui.editor.command.ContextRemoveParameterCommand;
import org.talend.core.ui.editor.command.ContextRenameParameterCommand;
import org.talend.core.ui.editor.command.ContextTemplateModifyCommand;

/**
 * This class must be extended for implementing the specific context composite. <br/>
 * 
 */
public abstract class ContextComposite extends Composite implements IContextModelManager {

    private boolean readOnly;

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
        refresh();
    }

    public void setTabEnable(boolean enable) {

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
        if (enable) {
            tableNebulas.setEnabled(true);
        }
    }

    @Override
    public void refresh() {
        refreshView();
    }

    private void refreshView() {
        if (getContextManager() == null) {
            // this.setEnabled(false);
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

    public void switchSettingsView(String oldName, String newName) {
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
