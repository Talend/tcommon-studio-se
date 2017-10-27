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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.utils.SwitchContextGroupNameImpl;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

public class ContextModeWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private ContextModeSelectPage contextModePage;

    private Property contextProperty;

    private ContextItem contextItem;

    private IContextManager contextManager;

    /**
     * this one base on contextManager keep the changeable context manager for reuse context
     */
    private IContextManager reuseContextManager;

    private Set<String> connectionVariables;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private boolean isCreateContext = true;

    private List<IWizardPage> dynamicWizardPages = new ArrayList<IWizardPage>();

    private List<ConectionAdaptContextVariableModel> adaptModels = new ArrayList<ConectionAdaptContextVariableModel>();

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    /**
     * 
     * this constructor only for context mode. (feature 2449)
     */
    public ContextModeWizard(final String contextName, boolean creation, ISelection selection,
            final List<IContextParameter> paramList) {
        this(PlatformUI.getWorkbench(), creation, (RepositoryNode) ((IStructuredSelection) selection).getFirstElement(), false);
        initContextMode(contextName, paramList);
    }

    /**
     * 
     * this constructor only for context mode. (feature TDI-29250)
     */
    public ContextModeWizard(final String contextName, boolean creation, ISelection selection,
            final List<IContextParameter> paramList, Set<String> connectionVaribles) {
        this(PlatformUI.getWorkbench(), creation, (RepositoryNode) ((IStructuredSelection) selection).getFirstElement(), false);
        initContextMode(contextName, paramList);
        this.connectionVariables = connectionVaribles;
    }

    private void initContextMode(final String contextName, final List<IContextParameter> paramList) {
        if (creation && contextName != null && contextProperty != null) {
            contextProperty.setLabel(contextName);
        }
        if (paramList != null && contextManager != null) {
            for (IContext context : contextManager.getListContext()) {
                for (IContextParameter param : paramList) {
                    boolean existed = false;
                    for (IContextParameter existedParam : context.getContextParameterList()) {
                        if (existedParam.getName().equals(param.getName())) {
                            existed = true;
                            break;
                        }
                    }
                    if (!existed) {
                        IContextParameter toAdd = param.clone();
                        toAdd.setContext(context);
                        context.getContextParameterList().add(toAdd);
                    }
                }
            }
        }
    }

    /**
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    public ContextModeWizard(IWorkbench workbench, boolean creation, RepositoryNode repositoryNode, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
        if (repositoryNode != null) {
            IRepositoryService service = CoreRuntimePlugin.getInstance().getRepositoryService();
            pathToSave = service.getRepositoryPath(repositoryNode);
        } else { // set root
            pathToSave = new Path(""); //$NON-NLS-1$
        }
        setWindowTitle(""); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.CONTEXT_WIZ));
        setNeedsProgressMonitor(true);

        if (creation) {
            contextItem = PropertiesFactory.eINSTANCE.createContextItem();
            contextProperty = PropertiesFactory.eINSTANCE.createProperty();
            contextProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            contextProperty.setStatusCode(""); //$NON-NLS-1$

            contextItem.setProperty(contextProperty);

            contextManager = new JobContextManager();
        } else {

            RepositoryNode node = repositoryNode;
            if (node == null && selection != null) {
                node = (RepositoryNode) ((IStructuredSelection) selection).getFirstElement();
            }
            if (node != null) {
                IRepositoryViewObject object;
                if (node.getObject() instanceof RepositoryObject) {
                    object = node.getObject();
                } else {
                    object = node.getObject();
                }
                setRepositoryObject(object);
                isRepositoryObjectEditable();
                initLockStrategy();

                contextItem = (ContextItem) object.getProperty().getItem();
                originaleObjectLabel = contextItem.getProperty().getLabel();
                this.originalVersion = contextItem.getProperty().getVersion();
                this.originalDescription = contextItem.getProperty().getDescription();
                this.originalPurpose = contextItem.getProperty().getPurpose();
                this.originalStatus = contextItem.getProperty().getStatusCode();
                contextProperty = contextItem.getProperty();
                contextManager = new JobContextManager(contextItem.getContext(), contextItem.getDefaultContext());
            }
        }
        initLockStrategy();
    }

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("ContextModeWizard.Title")); //$NON-NLS-1$
        contextModePage = new ContextModeSelectPage(contextManager, contextItem, connectionVariables, creation,
                isRepositoryObjectEditable(), pathToSave);
        addPage(contextModePage);
        contextModePage.setPageComplete(true);
        setForcePreviousAndNextButtons(true);
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        boolean formIsPerformed = contextManager.getListContext().size() != 0;
        if (formIsPerformed) {
            try {
                if (creation && isCreateContext) {
                    String nextId = factory.getNextId();
                    contextProperty.setId(nextId);
                    contextManager.saveToEmf(contextItem.getContext());
                    contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
                    final IPath path = ((PropertiesWizardPage) contextModePage.getPropertiesPage()).getDestinationPath();
                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        @Override
                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                factory.create(contextItem, path);
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    };
                    IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            try {
                                ISchedulingRule schedulingRule = workspace.getRoot();
                                // the update the project files need to be done in the workspace runnable to avoid all
                                // notification
                                // of changes before the end of the modifications.
                                workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                            } catch (CoreException e) {
                                throw new InvocationTargetException(e);
                            }

                        }
                    };
                    try {
                        new ProgressMonitorDialog(null).run(true, true, iRunnableWithProgress);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        //
                    }
                } else {
                    contextItem = contextModePage.getReuseItem();
                    contextItem.getContext().clear();
                    contextManager = reuseContextManager;
                    contextManager.saveToEmf(contextItem.getContext());
                    contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
                    if (contextManager instanceof JobContextManager) {
                        JobContextManager manager = (JobContextManager) contextManager;
                        if (manager.isModified()) {
                            // the function has moved to UpdateContextParameterCommand for update manager(bug 3993).
                            // update the tRunJob reference
                            // UpdateContextReferenceHelper.updateJobContextReference((JobContextManager)
                            // contextManager,
                            // contextItem);

                            // update
                            // TDQ-3901:Update the contextName property in the connection item file.
                            Map<String, String> contextGroupRenamedMap = new HashMap<String, String>();
                            Map<IContext, String> renameGroupContextMap = manager.getRenameGroupContext();
                            for (IContext context : renameGroupContextMap.keySet()) {
                                String oldContextGroupName = renameGroupContextMap.get(context);
                                contextGroupRenamedMap.put(oldContextGroupName, context.getName());
                            }
                            RepositoryUpdateManager.updateContext((JobContextManager) contextManager, contextItem);
                            if (!contextGroupRenamedMap.isEmpty()) {
                                SwitchContextGroupNameImpl.getInstance().updateContextForConnectionItems(contextGroupRenamedMap,
                                        contextItem);
                            }
                        }
                    }
                    // contextItem.setProperty(ProxyRepositoryFactory.getInstance().getUptodateProperty(contextItem.getProperty()));
                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        @Override
                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                factory.save(contextItem);
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    };
                    IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            try {
                                ISchedulingRule schedulingRule = workspace.getRoot();
                                // the update the project files need to be done in the workspace runnable to avoid all
                                // notification
                                // of changes before the end of the modifications.
                                workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                            } catch (CoreException e) {
                                throw new InvocationTargetException(e);
                            }

                        }
                    };
                    try {
                        new ProgressMonitorDialog(null).run(true, true, iRunnableWithProgress);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        //
                    }

                    updateRelatedView();

                }
                closeLockStrategy();
                ProxyRepositoryFactory.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());
                // TimeMeasure.end("performFinish");
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean performCancel() {
        contextItem.getProperty().setVersion(this.originalVersion);
        contextItem.getProperty().setLabel(this.originaleObjectLabel);
        contextItem.getProperty().setDisplayName(this.originaleObjectLabel);
        contextItem.getProperty().setDescription(this.originalDescription);
        contextItem.getProperty().setPurpose(this.originalPurpose);
        contextItem.getProperty().setStatusCode(this.originalStatus);
        return super.performCancel();
    }

    /**
     * DOC bqian Comment method "updateProcessContextManager".
     */
    private void updateRelatedView() {
        IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (designerCoreService != null) {
            designerCoreService.switchToCurContextsView();
            // for tRunJob component
            designerCoreService.switchToCurComponentSettingsView();
            // for 2608
            designerCoreService.switchToCurJobSettingsView();
        }
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    public ContextItem getContextItem() {
        return this.contextItem;
    }

    public IContextManager getContextManager() {
        return this.contextManager;
    }

    public IContextManager getResueContextManager() {
        return this.reuseContextManager;
    }

    public void setContextManager(IContextManager contextManager) {
        this.contextManager = contextManager;
    }

    public void setReuseContextManager(IContextManager contextManager) {
        this.reuseContextManager = contextManager;
    }

    public List<IWizardPage> getDynamicWizardPages() {
        return dynamicWizardPages;
    }

    public void setDynamicWizardPages(List<IWizardPage> dynamicWizardPages) {
        this.dynamicWizardPages = dynamicWizardPages;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage = null;
        if (dynamicWizardPages != null) {
            if (page instanceof ContextModeSelectPage) {
                if (page instanceof ContextModeSelectPage) {
                    nextPage = dynamicWizardPages.get(0);
                }
            } else if (page instanceof Step0WizardPage) {
                nextPage = dynamicWizardPages.get(1);
            } else if (page instanceof ShowRepositoryContextsPage) {
                int curStep = ((ShowRepositoryContextsPage) page).step;
                for (IWizardPage dynamicPage : dynamicWizardPages) {
                    if (dynamicPage instanceof ContextAdaptConectionSelectPage) {
                        if (((ContextAdaptConectionSelectPage) dynamicPage).step == curStep + 1) {
                            nextPage = dynamicPage;
                        }
                    }
                }
            } else if (page instanceof ContextAdaptConectionSelectPage) {
                int curStep = ((ContextAdaptConectionSelectPage) page).step;
                for (IWizardPage dynamicPage : dynamicWizardPages) {
                    if (dynamicPage instanceof ContextVariableValuePage) {
                        if (((ContextVariableValuePage) dynamicPage).step == curStep + 1) {
                            nextPage = dynamicPage;
                        }
                    }
                }
            }
        }
        if (nextPage != null) {
            return nextPage;
        }
        return super.getNextPage(page);
    }

    @Override
    public void setForcePreviousAndNextButtons(boolean b) {
        super.setForcePreviousAndNextButtons(b);
    }

    public List<ConectionAdaptContextVariableModel> getAdaptModels() {
        return adaptModels;
    }

    public void setAdaptModels(List<ConectionAdaptContextVariableModel> adaptModels) {
        this.adaptModels = adaptModels;
    }

    public void clearAdaptModels() {
        this.adaptModels.clear();
    }

    public boolean isCreateContext() {
        return isCreateContext;
    }

    public void setCreateContext(boolean isCreateContext) {
        this.isCreateContext = isCreateContext;
    }

    @Override
    public boolean canFinish() {
        if (dynamicWizardPages.size() == 0) {
            return false;
        }
        for (IWizardPage page : dynamicWizardPages) {
            if (!page.isPageComplete()) {
                return false;
            }
        }
        return true;
    }
}
