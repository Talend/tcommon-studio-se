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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.utils.SwitchContextGroupNameImpl;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class ContextWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(ContextWizard.class);

    private PropertiesWizardPage contextWizardPage0;

    private Property contextProperty;

    private ContextItem contextItem;

    private IContextManager contextManager;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    /**
     * 
     * this constructor only for context mode. (feature 2449)
     */
    public ContextWizard(final String contextName, boolean creation, ISelection selection, final List<IContextParameter> paramList) {
        this(PlatformUI.getWorkbench(), creation, (RepositoryNode) ((IStructuredSelection) selection).getFirstElement(), false);
        initContextMode(contextName, paramList);
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
    public ContextWizard(IWorkbench workbench, boolean creation, RepositoryNode repositoryNode, boolean forceReadOnly) {
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
        setWindowTitle(Messages.getString("ContextWizard.Title")); //$NON-NLS-1$
        contextWizardPage0 = new Step0WizardPage(contextProperty, pathToSave, ERepositoryObjectType.CONTEXT,
                !isRepositoryObjectEditable(), creation);
        contextWizardPage0.setTitle(Messages.getString("ContextWizard.step0Title")); //$NON-NLS-1$
        contextWizardPage0.setDescription(Messages.getString("ContextWizard.step0Description")); //$NON-NLS-1$
        addPage(contextWizardPage0);
        if (creation || !isRepositoryObjectEditable()) {
            contextWizardPage0.setPageComplete(false);
        }

        ContextPage contextPage = new ContextPage("test", contextManager, !isRepositoryObjectEditable()); //$NON-NLS-1$
        contextPage.setTitle(Messages.getString("ContextWizard.contextPageTitle")); //$NON-NLS-1$
        contextPage.setDescription(Messages.getString("ContextWizard.contextPageDescription")); //$NON-NLS-1$
        addPage(contextPage);
        if (!isRepositoryObjectEditable()) {
            contextPage.setPageComplete(false);
        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        // TimeMeasure.display = true;
        // TimeMeasure.measureActive = true;
        // TimeMeasure.begin("performFinish");
        boolean formIsPerformed = contextManager.getListContext().size() != 0;
        // if (delimitedFileWizardPage3 == null) {
        // formIsPerformed = delimitedFileWizardPage2.isPageComplete();
        // } else {
        // formIsPerformed = delimitedFileWizardPage3.isPageComplete();
        // }

        if (formIsPerformed) {
            // IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    contextProperty.setId(nextId);
                    contextManager.saveToEmf(contextItem.getContext());
                    contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
                    final IPath path = contextWizardPage0.getDestinationPath();
                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                factory.create(contextItem, path);
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    };
                    IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

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

                        public void run(IProgressMonitor monitor) throws CoreException {
                            try {
                                factory.save(contextItem);
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    };
                    IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

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
            // TDQ-14491 refresh DQ view if it is on DQ perspective
            ITDQRepositoryService tdqRepService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                tdqRepService =
                        (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                                ITDQRepositoryService.class);
            }
            if (tdqRepService != null) {
                // udpate all ana/reports who used this context
                if (!creation) {
                    tdqRepService.updateAllContextInAnalysisAndReport(contextItem, (JobContextManager) contextManager);
                }
                if (CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
                    tdqRepService.refresh(contextItem);
                }
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
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    public ContextItem getContextItem() {
        return this.contextItem;
    }

    public IContextManager getContextManager() {
        return this.contextManager;
    }

    public void setContextManager(IContextManager contextManager) {
        this.contextManager = contextManager;
    }

}
