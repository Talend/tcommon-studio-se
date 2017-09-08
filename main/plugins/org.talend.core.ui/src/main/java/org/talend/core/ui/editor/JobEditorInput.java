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
package org.talend.core.ui.editor;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ResourceModelHelper;
import org.talend.core.repository.ui.editor.RepositoryEditorInput;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.ILastVersionChecker;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;

/**
 * ggu class global comment. Detailled comment
 * 
 * refactor for job/jobletEditorInput
 */
public abstract class JobEditorInput extends RepositoryEditorInput {

    protected IProcess2 loadedProcess;

    public JobEditorInput(Item item, boolean load) throws PersistenceException {
        this(item, load, null, null);
    }

    public JobEditorInput(Item item, boolean load, Boolean lastVersion) throws PersistenceException {
        this(item, load, lastVersion, null);
    }

    public JobEditorInput(Item item, boolean load, Boolean lastVersion, Boolean readonly) throws PersistenceException {
        super(initFile(item), item);
        loadedProcess = createProcess();
        checkInit(lastVersion, readonly, load);
    }

    public void checkInit(Boolean lastVersion, Boolean readonly, boolean load) throws PersistenceException {
        if (loadedProcess instanceof ILastVersionChecker) {
            ((ILastVersionChecker) loadedProcess).setLastVersion(lastVersion);
        }
        if (load) {
            loadProcess();
        } else {

            saveProcessBefore();
            saveProcess(null, null, true);
        }
        if (readonly == null) {
            readonly = checkReadOnly();
            setReadOnly(readonly);
        } else {
            setReadOnly(readonly);
        }
    }

    /**
     * DOC mhelleboid Comment method "initFile".
     * 
     * @throws PersistenceException
     */
    private static IFile initFile(Item item) throws PersistenceException {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = ResourceModelHelper.getProject(project);
        IFolder folder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, false);
        if (!folder.exists()) {
            ResourceUtils.createFolder(folder);
        }
        String prefix = "tmp" + ERepositoryObjectType.getItemType(item).name(); //$NON-NLS-1$
        return ResourceUtils.getFile(folder, prefix + item.getProperty().getId(), false);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if (loadedProcess != null) {
            loadedProcess.setReadOnly(readOnly);
        }
    }

    public IProcess2 getLoadedProcess() {
        return loadedProcess;
    }

    protected void loadProcess() throws PersistenceException {
        loadedProcess.loadXmlFile(true);
        // loadedProcess.checkLoadNodes();
    }

    public boolean saveProcess(IProgressMonitor monitor, IPath path) {
        return saveProcess(monitor, path, false);
    }

    public boolean saveProcess(final IProgressMonitor monitor, IPath path, final boolean avoidSaveRelations) {
        try {
            if (monitor != null) {
                monitor.beginTask("save process", 100); //$NON-NLS-1$
            }
            // getFile().refreshLocal(IResource.DEPTH_ONE, monitor);

            // loadedProcess.setXmlStream(getFile().getContents());

            IRepositoryService service = CoreRuntimePlugin.getInstance().getRepositoryService();
            final IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();

            if (path != null) {
                // factory.createProcess(project, loadedProcess, path);
            } else {

                RepositoryWorkUnit rwu = new RepositoryWorkUnit("save process") {

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        resetItem();
                        ProcessType processType;
                        try {
                            processType = loadedProcess.saveXmlFile();
                        } catch (IOException e) {
                            throw new PersistenceException(e);
                        }
                        if (monitor != null) {
                            monitor.worked(40);
                        }
                        if (getItem() instanceof JobletProcessItem) {
                            ((JobletProcessItem) getItem()).setJobletProcess((JobletProcess) processType);
                        } else if (getItem() instanceof ProcessItem) {
                            ((ProcessItem) getItem()).setProcess(processType);
                        }
                        factory.save(getItem());
                        loadedProcess.setProperty(getItem().getProperty());
                        // 9035

                        if (!avoidSaveRelations) {
                            Item item = getItem();
                            RelationshipItemBuilder relationshipItemBuilder = getRelationshipItemBuilder(item);
                            relationshipItemBuilder.addOrUpdateItem(item);
                        }
                    }
                };
                rwu.setAvoidUnloadResources(true);
                rwu.setAvoidSvnUpdate(true);
                rwu.setAvoidUpdateLocks(true);
                factory.executeRepositoryWorkUnit(rwu);
                rwu.throwPersistenceExceptionIfAny();
                // factory.save(getItem());
                // loadedProcess.setProperty(getItem().getProperty());
                // // 9035
                // RelationshipItemBuilder.getInstance().addOrUpdateItem(getItem());
                if (monitor != null) {
                    monitor.worked(50);
                }
            }

            if (monitor != null) {
                monitor.worked(10);
            }
            return true;
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
            if (monitor != null) {
                monitor.setCanceled(true);
            }
            return false;
        } finally {
            if (monitor != null) {
                monitor.done();
            }
        }
    }

    protected RelationshipItemBuilder getRelationshipItemBuilder(Item item) {
        RelationshipItemBuilder relationshipItemBuilder = null;
        org.talend.core.model.general.Project currentProject = ProjectManager.getInstance().getCurrentProject();

        if (item != null) {
            org.talend.core.model.properties.Project propProject = ProjectManager.getInstance().getProject(item);
            if (propProject.getLabel().equalsIgnoreCase(currentProject.getTechnicalLabel())) {
                relationshipItemBuilder = RelationshipItemBuilder.getInstance(currentProject, true);
            } else {
                org.talend.core.model.general.Project project = new org.talend.core.model.general.Project(propProject);
                relationshipItemBuilder = RelationshipItemBuilder.getInstance(project, true);
            }
        }
        if (relationshipItemBuilder == null) {
            relationshipItemBuilder = RelationshipItemBuilder.getInstance();
        }
        return relationshipItemBuilder;
    }

    /**
     * 
     * ggu Comment method "resetItem".
     * 
     * bug 10925/11491
     */
    public void resetItem() throws PersistenceException {
        if (getItem().getProperty().eResource() == null || getItem().eResource() == null) {
            IRepositoryService service = CoreRuntimePlugin.getInstance().getRepositoryService();
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
            //
            // Property updated = factory.getUptodateProperty(getItem().getProperty());
            Property updatedProperty = null;
            try {
                factory.initialize();
                IRepositoryViewObject repositoryViewObject = factory.getLastVersion(new Project(ProjectManager.getInstance()
                        .getProject(getItem().getProperty().getItem())), getItem().getProperty().getId());
                if (repositoryViewObject != null) {
                    updatedProperty = repositoryViewObject.getProperty();
                    setItem(updatedProperty.getItem());
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

            // update the property of the node repository object
            // object.setProperty(updatedProperty);

            // setItem(updatedProperty.getItem());
        }
    }

    public boolean checkReadOnly() throws PersistenceException {
        return loadedProcess.checkReadOnly();
    }

    public boolean setForceReadOnly(boolean readonly) {
        if (readonly) {
            loadedProcess.setReadOnly(readonly);
            return true;
        } else {
            try {
                return checkReadOnly();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return false;
            }
        }
    }

    public void setLoadedProcess(IProcess2 loadedProcess) {
        this.loadedProcess = loadedProcess;
    }

    @Override
    public String getToolTipText() {
        return this.getName();
    }

    protected abstract IProcess2 createProcess();

    protected abstract void saveProcessBefore();

    @Override
    public void dispose() {
        super.dispose();
        this.loadedProcess = null;
    }

}
