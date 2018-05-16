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
package org.talend.metadata.managment.ui.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.IESBService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.expressionbuilder.ExpressionPersistance;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class PropertiesWizard extends Wizard {

    protected PropertiesWizardPage mainPage;

    protected IRepositoryObject object;

    protected final IPath path;

    protected boolean alreadyEditedByUser = false;

    private final String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    protected String lastVersionFound;

    private boolean unlockRequired = true;// A flag to indicate if the item which property is edit is required to
                                          // unlock.

    public PropertiesWizard(IRepositoryViewObject repositoryViewObject, IPath path, boolean useLastVersion) {
        super();

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.PROPERTIES_WIZ));
        // properties wizard editable status not working for ref items ,because econtainer is null;
        if (repositoryViewObject != null && repositoryViewObject.getProperty() != null) {
            this.object = new RepositoryObject(repositoryViewObject.getProperty());
            Property property = object.getProperty();
            Item item = property.getItem();
            if (property.eResource() == null || item != null && item.eContainer() == null) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {

                    IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                            IProxyRepositoryService.class);
                    IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
                    try {
                        ItemState state = item.getState();
                        if (useLastVersion) {
                            if (state != null && state.getPath() != null) {
                                this.object = (IRepositoryObject) factory.getLastVersion(new Project(ProjectManager.getInstance()
                                        .getProject(item)), property.getId(), state.getPath(), object.getRepositoryObjectType());
                                lastVersionFound = this.object.getVersion();
                            } else {
                                this.object = (IRepositoryObject) factory.getLastVersion(new Project(ProjectManager.getInstance()
                                        .getProject(item)), property.getId());
                                lastVersionFound = this.object.getVersion();
                            }
                        } else {
                            if (state != null && state.getPath() != null) {
                                this.lastVersionFound = factory.getLastVersion(
                                        new Project(ProjectManager.getInstance().getProject(item)), property.getId(),
                                        state.getPath(), object.getRepositoryObjectType()).getVersion();
                            } else {
                                this.lastVersionFound = factory.getLastVersion(
                                        new Project(ProjectManager.getInstance().getProject(item)), property.getId())
                                        .getVersion();
                            }
                            this.object.setProperty(factory.getUptodateProperty(property));
                        }
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        if (this.object == null) {
            this.object = new RepositoryObject(repositoryViewObject.getProperty());
        }
        this.originaleObjectLabel = this.object.getLabel();
        this.originalVersion = this.object.getVersion();
        this.originalDescription = this.object.getDescription();
        this.originalPurpose = this.object.getPurpose();
        this.originalStatus = this.object.getStatusCode();
        this.path = path;
        lockObject();
    }

    private void lockObject() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
            try {
                boolean isOpened = false;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
                    ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
                    isOpened = coreService.isOpenedItemInEditor(object);
                }
                if (!repositoryFactory.getStatus(object).equals(ERepositoryStatus.LOCK_BY_OTHER) && isOpened) {
                    // means if editor is opened, locked by user or opened as read only. (3 cases possible normally)
                    alreadyEditedByUser = true;
                } else {
                    repositoryFactory.lock(object);
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            } catch (BusinessException e) {
                // Nothing to do
            }
        }
    }

    private void unlockObject() {
        if (!alreadyEditedByUser) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);
                IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
                try {
                    repositoryFactory.unlock(object);

                    // if we have updated the version, the object will change, so we still need to unlock the original
                    // version of the object.
                    if (!object.getProperty().getVersion().equals(originalVersion)) {
                        List<IRepositoryViewObject> list = repositoryFactory.getAllVersion(object.getProperty().getId());
                        for (IRepositoryViewObject obj : list) {
                            if (obj.getProperty().getVersion().equals(originalVersion)) {
                                repositoryFactory.unlock(obj);
                                break;
                            }
                        }
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                } catch (LoginException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    protected boolean isReadOnly() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
            return !repositoryFactory.getStatus(object).isEditable() || alreadyEditedByUser;
        }
        return true;
    }

    @Override
    public void addPages() {
        mainPage = new PropertiesWizardPage("WizardPage", object.getProperty(), path, isReadOnly(), false, lastVersionFound) { //$NON-NLS-1$

            @Override
            public void createControl(Composite parent) {
                Composite container = new Composite(parent, SWT.NONE);
                GridLayout layout = new GridLayout(2, false);
                container.setLayout(layout);

                if (alreadyEditedByUser) {
                    Label label = new Label(container, SWT.NONE);
                    label.setForeground(ColorConstants.red);
                    label.setText(Messages.getString("PropertiesWizard.alreadyLockedByUser")); //$NON-NLS-1$
                    GridData gridData = new GridData();
                    gridData.horizontalSpan = 2;
                    label.setLayoutData(gridData);
                }

                super.createControl(container);

                setControl(container);
                updateContent();
                addListeners();
                setPageComplete(false);
            }

            @Override
            protected void evaluateTextField() {
                super.evaluateTextField();
                if (nameStatus.getSeverity() == IStatus.OK) {
                    ERepositoryObjectType type = getRepositoryObjectType();
                    if (type == ERepositoryObjectType.PROCESS) {
                        evaluateNameInRoutine();
                    } else if (type == ERepositoryObjectType.ROUTINES || type == ERepositoryObjectType.METADATA_FILE_RULES) {
                        evaluateNameInJob();
                    } else {
                        String namePattern = type.getNamePattern();
                        if (namePattern == null || "".equals(namePattern.trim())) {
                            return;
                        }
                        Pattern pattern = Pattern.compile(namePattern);
                        if (pattern.matcher(nameText.getText()).matches()) {
                            return;
                        }
                        nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.NameIsInvalid")); //$NON-NLS-1$
                        updatePageStatus();
                    }
                }
            }

            @Override
            public ERepositoryObjectType getRepositoryObjectType() {
                return object.getRepositoryObjectType();
            }
        };
        addPage(mainPage);
        setWindowTitle(Messages.getString("EditProcessPropertiesWizard.wizardTitle")); //$NON-NLS-1$
    }

    @Override
    public boolean performFinish() {
        if (alreadyEditedByUser) {
            return false;
        }

        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

            @Override
            public void run(final IProgressMonitor monitor) throws CoreException {
                try {
                    IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                    // changed by hqzhang for TDI-19527, label=displayName
                    object.getProperty().setLabel(object.getProperty().getDisplayName());
                    proxyRepositoryFactory.save(object.getProperty(), originaleObjectLabel, originalVersion);
                    ExpressionPersistance.getInstance().jobNameChanged(originaleObjectLabel, object.getLabel());

                    if (!originalVersion.equals(object.getVersion())) {
                        RelationshipItemBuilder.getInstance().addOrUpdateItem(object.getProperty().getItem());
                    }
                    proxyRepositoryFactory.saveProject(ProjectManager.getInstance().getCurrentProject());
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
                        IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
                        service.editJobName(originaleObjectLabel, object.getLabel());
                    }
                    // unlockObject();
                    // alreadyEditedByUser = true; // to avoid 2 calls of unlock
                } catch (PersistenceException pe) {
                    throw new CoreException(new Status(IStatus.ERROR, FrameworkUtil.getBundle(this.getClass()).getSymbolicName(),
                            "persistance error", pe)); //$NON-NLS-1$
                }
            }
        };
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        try {
            ISchedulingRule schedulingRule = workspace.getRoot();
            // the update the project files need to be done in the workspace runnable to avoid all notification
            // of changes before the end of the modifications.
            workspace.run(runnable, schedulingRule, IWorkspace.AVOID_UPDATE, null);
            return true;
        } catch (CoreException e) {
            MessageBoxExceptionHandler.process(e.getCause());
            return false;
        }
    }

    /**
     * Use to replace in all tRunJob, the old job name by the new one.
     */
    // private void manageRunJobRenaming(String newName, String oldName) {
    // System.out.println("Rename " + oldName + "->" + newName); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // IComponentFilter filter1 = new PropertyComponentFilter("tRunJob", "PROCESS_TYPE_PROCESS", oldName); //$NON-NLS-1$
    // //$NON-NLS-2$
    //
    // IComponentConversion updateCompProperty = new UpdatePropertyComponentConversion("PROCESS_TYPE_PROCESS", newName);
    // //$NON-NLS-1$
    // IComponentConversion updateRequiredProperty = new UpdateRequiredProperty(oldName, newName);
    //
    // try {
    // ModifyComponentsAction.searchAndModify(filter1, Arrays.<IComponentConversion> asList(updateCompProperty,
    // updateRequiredProperty));
    // } catch (Exception e) {
    // ExceptionHandler.process(e, Level.ERROR);
    // }
    // }
    /**
     * Update the "required" property.
     * 
     * <required> <job context="Default" name="newJobName"/> </required>
     */
    // private class UpdateRequiredProperty implements IComponentConversion {
    //
    // private final String oldJobName;
    //
    // private final String newJobName;
    //
    // public UpdateRequiredProperty(String oldJobName, String newJobName) {
    // super();
    // this.oldJobName = oldJobName;
    // this.newJobName = newJobName;
    // }
    //
    // public void transform(NodeType node) {
    // ProcessType item = (ProcessType) node.eContainer();
    // renameJobInRequiredProperty(item, newJobName);
    // }
    //
    // private void renameJobInRequiredProperty(ProcessType item, String newJobName) {
    // RequiredType required = item.getRequired();
    // for (Object o : required.getJob()) {
    // JobType job = (JobType) o;
    // if (job.getName().equals(oldJobName)) {
    // job.setName(newJobName);
    // }
    // }
    // }
    // }
    @Override
    public boolean performCancel() {
        if (!alreadyEditedByUser) {
            object.getProperty().setVersion(this.originalVersion);
            object.getProperty().setLabel(this.originaleObjectLabel);
            object.getProperty().setDisplayName(originaleObjectLabel);
            object.getProperty().setDescription(this.originalDescription);
            object.getProperty().setPurpose(this.originalPurpose);
            object.getProperty().setStatusCode(this.originalStatus);
        }
        return true;
    }

    @Override
    public void dispose() {
        if (isUnlockRequired()) {
            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                @Override
                public void run(final IProgressMonitor monitor) throws CoreException {
                    unlockObject();
                }
            };
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            ISchedulingRule schedulingRule = workspace.getRoot();
            // the update the project files need to be done in the workspace runnable to avoid all notification
            // of changes before the end of the modifications.
            try {
                workspace.run(runnable, schedulingRule, IWorkspace.AVOID_UPDATE, null);

            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        super.dispose();
    }

    /**
     * This method is used to control if the item which property is edited is required to unlock after closing the
     * wizard. It is invoked by {@link #dispose()} method. The wizard that extends {@link PropertiesWizard} could
     * override this method. Added by Marvin Wang on Jan 31, 2013.
     * 
     * @return <code>true</code> as default to unlock the object. Otherwise, <code>false</code>.
     */
    protected boolean isUnlockRequired() {
        return unlockRequired;
    }

    /**
     * Getter for mainPage.
     * 
     * @return the mainPage
     */
    public PropertiesWizardPage getMainPage() {
        return this.mainPage;
    }

    /**
     * Sets the mainPage.
     * 
     * @param mainPage the mainPage to set
     */
    public void setMainPage(PropertiesWizardPage mainPage) {
        this.mainPage = mainPage;
    }

    /**
     * Getter for object.
     * 
     * @return the object
     */
    public IRepositoryObject getObject() {
        return this.object;
    }

    /**
     * Sets the object.
     * 
     * @param object the object to set
     */
    public void setObject(IRepositoryObject object) {
        this.object = object;
    }

    /**
     * Sets the unlockRequired.
     * 
     * @param unlockRequired the unlockRequired to set
     */
    public void setUnlockRequired(boolean unlockRequired) {
        this.unlockRequired = unlockRequired;
    }

    /**
     * Getter for alreadyEditedByUser.
     * 
     * @return the alreadyEditedByUser
     */
    public boolean isAlreadyEditedByUser() {
        return this.alreadyEditedByUser;
    }

    /**
     * Sets the alreadyEditedByUser.
     * 
     * @param alreadyEditedByUser the alreadyEditedByUser to set
     */
    public void setAlreadyEditedByUser(boolean alreadyEditedByUser) {
        this.alreadyEditedByUser = alreadyEditedByUser;
    }
}
