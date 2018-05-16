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
package org.talend.repository.viewer.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.repository.constants.Constant;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * this handle content that root node is of type ProjectRepositoryNode
 * */
public abstract class ProjectRepoAbstractContentProvider extends FolderListenerSingleTopContentProvider {

    private AdapterImpl deleteFolderListener;

    private Project project;

    private IPropertyChangeListener mergeRefListener;

    private ServiceRegistration projectReloadService;

    private final class DeletedFolderListener extends AdapterImpl {

        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getFeature() == PropertiesPackage.eINSTANCE.getProject_DeletedFolders()) {
                if (viewer != null && viewer.getControl() != null) {
                    viewer.getControl().getDisplay().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            refreshTopLevelNodes();

                        }
                    });
                }// else the viewer as not control so not displayed
            }// else no a change in deleted Folders

        }
    }

    /*
     * only called with a ProjectRepoNode as a parent. register a listener to the current project to be notified of the
     * deleted folders
     * 
     * @see
     * org.talend.repository.example.viewer.content.LegacyRepositoryContentProvider#getRootNode(org.talend.repository
     * .model.ProjectRepositoryNode)
     */
    @Override
    protected RepositoryNode getInitialTopLevelNode(RepositoryNode element) {
        // this is called only if element is of type ProjectRepositoryNode
        ProjectRepositoryNode projRepo = getProjectRepositoryNode(element);
        setupDeleteFolderListener(projRepo);
        registerProjectReloadServiceListener();
        return getTopLevelNodeFromProjectRepositoryNode(projRepo);
    }

    /**
     * DOC sgandon Comment method "setupDeleteFolderListener".
     * 
     * @param projRepo
     */
    protected void setupDeleteFolderListener(ProjectRepositoryNode projRepo) {

        if (projRepo.getProject() != null) {
            // add a lister for the removed folders.
            if (deleteFolderListener == null) {
                project = projRepo.getProject().getEmfProject();
                project.eAdapters().add(getDeleteListener());
            }// else listener already attached
        }// no project set so no need to listen

    }
    
    private AdapterImpl getDeleteListener() {
        if (deleteFolderListener == null) {
            deleteFolderListener = new DeletedFolderListener();
        }
        return deleteFolderListener;
    }

    /**
     * DOC sgandon Comment method "registerLockUnlockServiceListener".
     */
    private void registerProjectReloadServiceListener() {
        if (projectReloadService == null) {
            BundleContext bundleContext = CoreRepositoryPlugin.getDefault().getBundle().getBundleContext();
            projectReloadService = bundleContext.registerService(
                    EventHandler.class.getName(),
                    new EventHandler() {

                        @Override
                        public void handleEvent(Event event) {
                            if (event.getProperty(Constant.PROJECT_RELOAD_PROPERTY_KEY) != null) {
                                org.talend.core.model.properties.Project newProjectInstance = (org.talend.core.model.properties.Project) event
                                        .getProperty(Constant.PROJECT_RELOAD_PROPERTY_KEY);
                                // remove the listener on the old project instance
                                if (getProject() != null) {
                                    getProject().eAdapters().remove(getDeleteListener());
                                }

                                // set the new project instance and re-attach the listener
                                setProject(newProjectInstance);
                                getProject().eAdapters().add(getDeleteListener());
                            }
                        }
                    },
                    new Hashtable<String, String>(Collections.singletonMap(EventConstants.EVENT_TOPIC,
                            Constant.REPOSITORY_ITEM_EVENT_PREFIX + "*"))); //$NON-NLS-1$
        }// else already unlock service listener already registered
    }

    /**
     * return the project repository from the element, never null
     * 
     * @param element
     */
    abstract protected ProjectRepositoryNode getProjectRepositoryNode(RepositoryNode element);

    /**
     * DOC sgandon Comment method "getTopLevelNodeFromProjectRepositoryNode".
     * 
     * @param element
     * @return
     */
    abstract protected RepositoryNode getTopLevelNodeFromProjectRepositoryNode(ProjectRepositoryNode projectNode);

    // /**
    // *
    // * DOC ggu Comment method "getTopLevelNodeType".
    // *
    // * @return
    // */
    // abstract protected ERepositoryObjectType getTopLevelNodeType();

    @Override
    protected boolean isRootNodeType(Object element) {
        return (element instanceof ProjectRepositoryNode)
                || Platform.getAdapterManager().getAdapter(element, ProjectRepositoryNode.class) != null;
    }

    @Override
    protected RepositoryNode extractPotentialRootNode(Object element) {
        RepositoryNode potentialRootNode = null;
        if (element instanceof ProjectRepositoryNode) {
            potentialRootNode = (ProjectRepositoryNode) element;
        } else {
            potentialRootNode = (ProjectRepositoryNode) Platform.getAdapterManager().getAdapter(element,
                    ProjectRepositoryNode.class);
        }
        return potentialRootNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.FolderListenerSingleTopContentProvider#inputChanged(org.eclipse.jface.viewers
     * .Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        super.inputChanged(arg0, arg1, arg2);
        // register a listener to refresh when merge items is activated

        registerMergeRefListener();
    }

    /**
     * DOC sgandon Comment method "registerMergeRefListgener".
     */
    private void registerMergeRefListener() {
        if (mergeRefListener == null) {
            mergeRefListener = new IPropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    if (IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT.equals(event.getProperty())) {
                        refreshTopLevelNodes();
                    }// else ignor other properties

                }
            };
            // the merge only for DI repository,need to judge null for other product
            IRepositoryService repositoryService = CoreRuntimePlugin.getInstance().getRepositoryService();
            if (repositoryService != null) {
                IPreferenceStore preferenceStore = repositoryService.getRepositoryPreferenceStore();
                preferenceStore.addPropertyChangeListener(mergeRefListener);
            }

        }
    }

    /**
     * DOC sgandon Comment method "refreshTopLevelNodes".
     */
    protected void refreshTopLevelNodes() {
        Set<RepositoryNode> topLevelNodes = getTopLevelNodes();
        if (topLevelNodes == null) {
            return;
        }
        for (RepositoryNode topLevelNode : topLevelNodes) {
            refreshTopLevelNode(topLevelNode);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.FolderListenerSingleTopContentProvider#dispose()
     */
    @Override
    public void dispose() {
        if (deleteFolderListener != null) {
            project.eAdapters().remove(deleteFolderListener);
            deleteFolderListener = null;
        }// else nothing to remove
        if (mergeRefListener != null) {
            IRepositoryService repositoryService = CoreRuntimePlugin.getInstance().getRepositoryService();
            if (repositoryService != null) {
                IPreferenceStore preferenceStore = repositoryService.getRepositoryPreferenceStore();
                preferenceStore.removePropertyChangeListener(mergeRefListener);
            }
            mergeRefListener = null;
        }
        super.dispose();
    }

    /**
     * Getter for project.
     * 
     * @return the project
     */
    protected Project getProject() {
        return this.project;
    }

    /**
     * Sets the project.
     * 
     * @param project the project to set
     */
    protected void setProject(Project project) {
        this.project = project;
    }

}
