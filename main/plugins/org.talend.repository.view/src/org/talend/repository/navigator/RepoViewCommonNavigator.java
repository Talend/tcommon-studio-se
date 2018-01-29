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
package org.talend.repository.navigator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.CommonViewerSorter;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip;
import org.talend.commons.utils.Timer;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.LockInfo;
import org.talend.core.model.update.EUpdateItemType;
import org.talend.core.model.update.IUpdateManager;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.provider.GitContentServiceProviderManager;
import org.talend.core.repository.services.IGitContentService;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.IJobletProviderService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.IRepositoryChangedListener;
import org.talend.repository.RepositoryChangedEvent;
import org.talend.repository.RepositoryViewPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.viewer.RepoViewPartListener;
import org.talend.repository.viewer.content.listener.ResourcePostChangeRunnableListener;
import org.talend.repository.viewer.filter.PerspectiveFilterHelper;
import org.talend.repository.viewer.filter.RepositoryNodeFilterHelper;
import org.talend.repository.viewer.filter.listener.RepoViewPerspectiveListener;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class RepoViewCommonNavigator extends CommonNavigator implements IRepositoryView, ITabbedPropertySheetPageContributor,
        IRepositoryChangedListener {

    /**
     * A savable to reflect the current editor status in the view
     * 
     */
    public class EditorSavable extends Saveable {

        /**
         * DOC sgandon EditorSavable constructor comment.
         * 
         * @param repoViewCommonNavigator
         */
        public EditorSavable() {
            // TODO Auto-generated constructor stub
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#getName()
         */
        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#getImageDescriptor()
         */
        @Override
        public ImageDescriptor getImageDescriptor() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#doSave(org.eclipse.core.runtime.IProgressMonitor)
         */
        @Override
        public void doSave(IProgressMonitor monitor) throws CoreException {
            ISaveablePart currentEditorSavablePart = getCurrentEditorSavablePart();
            if (currentEditorSavablePart != null) {
                currentEditorSavablePart.doSave(monitor);
                firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);

            }

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#isDirty()
         */
        @Override
        public boolean isDirty() {
            if (!isShouldCheckRepositoryDirty()) {
                return false;
            }
            ISaveablePart currentEditorSavablePart = getCurrentEditorSavablePart();
            return currentEditorSavablePart != null ? currentEditorSavablePart.isDirty() : false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object object) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.Saveable#hashCode()
         */
        @Override
        public int hashCode() {
            // TODO Auto-generated method stub
            return 0;
        }

        ISaveablePart getCurrentEditorSavablePart() {
            IWorkbench workbench = PlatformUI.getWorkbench();
            if (workbench != null && workbench.getActiveWorkbenchWindow() != null
                    && workbench.getActiveWorkbenchWindow().getActivePage() != null) {
                IEditorPart activeEditor = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                if (activeEditor != null) {
                    return activeEditor;
                }// else return the null
            }// else no workbench, or no active window or no activa page so return default
            return null;
        }

    }

    private static Logger log = Logger.getLogger(RepoViewCommonNavigator.class);

    protected CommonViewer viewer;

    private static List<ISelectionChangedListener> listenersNeedTobeAddedIntoTreeviewer = new ArrayList<ISelectionChangedListener>();

    private static boolean codeGenerationEngineInitialised;

    // protected boolean isFromFake = true;

    public static Object initialInput;

    private boolean noNeedUpdate = false;

    private RepoViewPerspectiveListener perspectiveListener;

    private RepoViewPartListener partListener;

    private ResourcePostChangeRunnableListener resourcePostChangeRunnableListener;

    private IGitContentService service;

    /**
     * yzhang Comment method "addPreparedListeners".
     * 
     * @param listeners
     */
    public void addPreparedListeners(ISelectionChangedListener listeners) {
        if (listeners != null) {
            listenersNeedTobeAddedIntoTreeviewer.add(listeners);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
     */
    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        // FIXME later, will check this to initialize
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            final IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);
            if (service != null) {
                service.initializePluginMode();

            }
        }
        if (!codeGenerationEngineInitialised && !CoreRepositoryPlugin.getDefault().isRCPMode()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                        ILibrariesService.class);
                if (libService != null && libService.isLibSynchronized()) {
                    libService.syncLibraries();
                }
            }
            codeGenerationEngineInitialised = true;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#createCommonViewerObject(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected CommonViewer createCommonViewerObject(Composite aParent) {
        return new RepoViewCommonViewer(this, getViewSite().getId(), aParent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    }

    /*
     * this by default return a ProjectRepositoryNode except if the public static variable named initialInput is set.
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#getInitialInput()
     */
    @Override
    protected Object getInitialInput() {

        if (initialInput == null) {
            initialInput = new TalendRepositoryRoot();
        }
        return initialInput;
    }

    @Override
    public void createPartControl(Composite parent) {
        service = GitContentServiceProviderManager.getGitContentService();
        if (service != null && service.isGIT()) {
            service.createDropdownCombo(parent);
        }

        super.createPartControl(parent);

        viewer = getCommonViewer();

        if (service != null && service.isGIT()) {
            viewer.getTree().addPaintListener(new PaintListener() {

                @Override
                public void paintControl(PaintEvent e) {
                    Point viewerPoint = viewer.getTree().getSize();
                    Point point = parent.getSize();
                    if (viewerPoint.x == point.x - 7 && viewerPoint.y == point.y - 40) {
                        return;
                    }
                    viewer.getTree().setSize(point.x - 7, point.y - 40);
                    viewer.getTree().showSelection();
                }
            });
        }

        if (viewer instanceof ITreeViewerListener) {
            viewer.addTreeListener((ITreeViewerListener) viewer);
        }
        getSite().setSelectionProvider(viewer);
        // if (isFromFake) {
        // refresh();
        // }
        // This only tree listener aim is to change open/close icons on folders :
        // TODO this should be done in the LabelProvider
        viewer.addTreeListener(new ITreeViewerListener() {

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                Object element = event.getElement();
                if (element instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) element;
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                            item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_CLOSE_ICON));
                        } else if (ENodeType.SYSTEM_FOLDER == node.getType()) {
                            if (node.getContentType() == ERepositoryObjectType.PROCESS) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_STANDARD_GENERIC_CATEGORY_CLOSE_ICON));
                            } else if (node.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_STREAMING_GENERIC_CATEGORY_CLOSE_ICON));
                            } else if (node.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_BATCH_GENERIC_CATEGORY_CLOSE_ICON));
                            }
                        }
                    }
                }
            }

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                Object element = event.getElement();
                if (element instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) element;
                    TreeItem item = getObject(viewer.getTree(), event.getElement());
                    if (item != null) {
                        if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                            item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_OPEN_ICON));
                        } else if (ENodeType.SYSTEM_FOLDER == node.getType()) {
                            // also see ImportItemsViewerLabelProvider.java
                            if (node.getContentType() == ERepositoryObjectType.PROCESS) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_STANDARD_GENERIC_CATEGORY_OPEN_ICON));
                            } else if (node.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_STREAMING_GENERIC_CATEGORY_OPEN_ICON));
                            } else if (node.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                                item.setImage(ImageProvider.getImage(ECoreImage.PROCESS_BATCH_GENERIC_CATEGORY_OPEN_ICON));
                            }
                        }
                    }
                }// else not a repo node so ignor it
            }
        });
        createTreeTooltip(viewer.getTree());

        viewer.getTree().addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                log.trace("Repository gain focus"); //$NON-NLS-1$
                IContextService contextService = (IContextService) RepositoryViewPlugin.getDefault().getWorkbench()
                        .getAdapter(IContextService.class);
                ca = contextService.activateContext("talend.repository"); //$NON-NLS-1$
            }

            @Override
            public void focusLost(FocusEvent e) {
                log.trace("Repository lost focus"); //$NON-NLS-1$
                if (ca != null) {
                    IContextService contextService = (IContextService) RepositoryViewPlugin.getDefault().getWorkbench()
                            .getAdapter(IContextService.class);
                    contextService.deactivateContext(ca);
                }
            }
        });

        if (listenersNeedTobeAddedIntoTreeviewer.size() > 0) {
            for (ISelectionChangedListener listener : listenersNeedTobeAddedIntoTreeviewer) {
                viewer.addSelectionChangedListener(listener);
            }
            listenersNeedTobeAddedIntoTreeviewer.clear();
        }

        CoreRepositoryPlugin.getDefault().registerRepositoryChangedListenerAsFirst(this);

        if (!CoreRepositoryPlugin.getDefault().isRCPMode()) {
            boolean pluginModel = true;
            /*
             * seems be true always.
             */
            // pluginModel= SwitchProjectAction.PLUGIN_MODEL;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IMigrationToolService.class)) {
                IMigrationToolService migrationService = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                        IMigrationToolService.class);
                if (migrationService != null) {
                    migrationService.executeMigration(pluginModel);
                }
            }
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                        IRunProcessService.class);
                if (runProcessService != null) {
                    runProcessService.deleteAllJobs(pluginModel);
                }
            }
            final RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY);
            final Project project = repositoryContext.getProject();

            final IWorkbenchWindow activedWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            activedWorkbenchWindow.getPartService().addPartListener(new IPartListener() {

                @Override
                public void partActivated(IWorkbenchPart part) {
                    if (part instanceof RepoViewCommonNavigator) {
                        String title = activedWorkbenchWindow.getShell().getText();
                        if (!title.contains("|")) { //$NON-NLS-1$
                            title += " | " + repositoryContext.getUser() + " | " + project.getLabel(); //$NON-NLS-1$ //$NON-NLS-2$
                            activedWorkbenchWindow.getShell().setText(title);
                        }
                    }
                }

                @Override
                public void partBroughtToTop(IWorkbenchPart part) {

                }

                @Override
                public void partClosed(IWorkbenchPart part) {

                }

                @Override
                public void partDeactivated(IWorkbenchPart part) {

                }

                @Override
                public void partOpened(IWorkbenchPart part) {
                    if (part instanceof RepoViewCommonNavigator) {
                        String title = activedWorkbenchWindow.getShell().getText();
                        if (!title.contains("|")) { //$NON-NLS-1$
                            title += " | " + repositoryContext.getUser() + " | " + project.getLabel(); //$NON-NLS-1$ //$NON-NLS-2$
                            activedWorkbenchWindow.getShell().setText(title);
                        }
                    }
                }

            });
        }
        expandTreeRootIfOnlyOneRoot();
        viewer.setSorter(new CommonViewerSorter());
        refreshContentDescription();

        // refresh for filters
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(getRepoViewPerspectiveListener());
        // when open need do perspective filter
        RepositoryNodeFilterHelper.filter(viewer, RepositoryNodeFilterHelper.isActivedFilter(),
                PerspectiveFilterHelper.isActivedPerspectiveFilter());

        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(getRepoViewPartListener());

        // resource post change listener.
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace != null) {
            workspace.addResourceChangeListener(getResourcePostChangerRunnableListener(), IResourceChangeEvent.POST_CHANGE);
        }
    }

    private RepoViewPerspectiveListener getRepoViewPerspectiveListener() {
        if (perspectiveListener == null) {
            perspectiveListener = new RepoViewPerspectiveListener(getCommonViewer());
        }
        return perspectiveListener;
    }

    private RepoViewPartListener getRepoViewPartListener() {
        if (partListener == null) {
            partListener = new RepoViewPartListener();
        }
        return partListener;
    }

    private ResourcePostChangeRunnableListener getResourcePostChangerRunnableListener() {
        if (resourcePostChangeRunnableListener == null) {
            resourcePostChangeRunnableListener = new ResourcePostChangeRunnableListener(getCommonViewer());
        }
        return resourcePostChangeRunnableListener;
    }

    public boolean addVisitor(IResourceDeltaVisitor visitor) {
        return getResourcePostChangerRunnableListener().addVisitor(visitor);
    }

    public boolean removeVisitor(IResourceDeltaVisitor visitor) {
        return getResourcePostChangerRunnableListener().removeVisitor(visitor);
    }

    /**
     * looks for an adapter on the input that adapts to INavigatorDescriptor and then changes the view descritor string
     */
    public void refreshContentDescription() {
        INavigatorDescriptor navDesc = getNavDesc();
        final String descriptor = navDesc.getDescriptor();
        if (navDesc == null) {
            setContentDescription("");
        } else if (service != null && service.isGIT()) {
            service.configureCombo(descriptor);
        } else {
            setContentDescription(descriptor);
        }
    }

    protected INavigatorDescriptor getNavDesc() {
        INavigatorDescriptor navDesc = (INavigatorDescriptor) Platform.getAdapterManager().getAdapter(
                getCommonViewer().getInput(), INavigatorDescriptor.class);
        return navDesc;
    }

    /**
     * DOC bqian Comment method "createTreeTooltip".
     * 
     * @param tree
     */
    private void createTreeTooltip(Tree tree) {
        new AbstractTreeTooltip(tree) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip#getTooltipContent(org.eclipse.swt.widgets.TreeItem)
             */
            @Override
            public String getTooltipContent(TreeItem item) {

                Object data = item.getData();
                if (data instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) data;
                    IRepositoryViewObject object = node.getObject();
                    if (object == null) {
                        return null;
                    }
                    // add for feature 10281
                    String content = null;
                    User currentLoginUser = ((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                            .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser();
                    String currentLogin = null;
                    if (currentLoginUser != null) {
                        currentLogin = currentLoginUser.getLogin();
                    }
                    String login = null;
                    String application = "studio";//$NON-NLS-1$
                    if (object.getRepositoryStatus() == ERepositoryStatus.LOCK_BY_OTHER) {
                        Property property = object.getProperty();
                        if (property != null) {
                            Item item2 = property.getItem();
                            if (item2 != null) {
                                LockInfo lockInfo = ProxyRepositoryFactory.getInstance().getLockInfo(item2);
                                if (!lockInfo.getUser().equals(currentLogin)) {
                                    login = lockInfo.getUser();
                                    application = lockInfo.getApplication();
                                }
                            }
                        }
                        if (login != null && !"".equals(login)) {//$NON-NLS-1$
                            content = Messages.getString("RepoViewCommonNavigator.Content", login, application); //$NON-NLS-1$ 
                        }
                    }
                    String description = object.getDescription();
                    if (content == null || "".equals(content)) { //$NON-NLS-1$
                        if (description == null || "".equals(description)) {//$NON-NLS-1$
                            return null;
                        }
                        return description;
                    } else {
                        if (description == null || "".equals(description)) {//$NON-NLS-1$
                            return content;
                        }
                        return content + "\n" + Messages.getString("RepoViewCommonNavigator.Desc") + description;//$NON-NLS-1$ //$NON-NLS-2$
                    }
                } else {
                    //
                }
                return null;
            }

            @Override
            protected void checkShellBounds(Shell tip, Event event) {
                Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                Point pt = table.toDisplay(event.x, event.y);
                tip.setBounds(pt.x + 10, pt.y + 5, size.x, size.y);
            }

        };

    }

    IContextActivation ca;

    private EditorSavable editorSavable = new EditorSavable();

    private boolean shouldCheckRepositoryDirty = true;

    public boolean isShouldCheckRepositoryDirty() {
        return this.shouldCheckRepositoryDirty;
    }

    public void setShouldCheckRepositoryDirty(boolean shouldCheckRepositoryDirty) {
        this.shouldCheckRepositoryDirty = shouldCheckRepositoryDirty;
    }

    private TreeItem getObject(Tree tree, Object objectToFind) {
        for (TreeItem item : tree.getItems()) {
            TreeItem toReturn = getObject(item, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    private TreeItem getObject(TreeItem parent, Object objectToFind) {
        for (TreeItem currentChild : parent.getItems()) {
            if (objectToFind.equals(currentChild.getData())) {
                return currentChild;
            }
            TreeItem toReturn = getObject(currentChild, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    public void dragFinished() {
        this.setNoNeedUpdate(false);
        refresh();
        LocalSelectionTransfer.getTransfer().setSelection(null);
        LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.repository.views.IRepositoryView#getViewer()
     */
    @Override
    public TreeViewer getViewer() {
        return viewer;
    }

    @Override
    public void refresh() {
        this.refresh(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.repository.views.IRepositoryView#refresh()
     */
    public void refresh(final boolean needInitialize) {
        /*
         * fix bug 4040. Sometimes Display.getCurrent.getActiveShell() get null result we not expect.
         */
        // Shell shell = Display.getCurrent().getActiveShell();
        Shell shell = getSite().getShell();

        if (shell == null) {
            return;
        }

        final Job job = new Job(Messages.getString("RepoViewCommonNavigator.refresh")) { //$NON-NLS-1$

            @Override
            public IStatus run(IProgressMonitor monitor) {
                try {
                    Timer timer = Timer.getTimer("repositoryView"); //$NON-NLS-1$
                    timer.start();
                    if (needInitialize) {
                        try {
                            final ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                            factory.initialize();
                        } catch (Exception e) {
                            throw new InvocationTargetException(e);
                        }
                    }
                    try {
                        ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                    } catch (CoreException e) {
                        throw new InvocationTargetException(e);
                    }
                    // TODO Why do we need to recreate a root here ????
                    // root = new ProjectRepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);

                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            // unsetting the selection will prevent the propertyView from displaying dirty data
                            viewer.setSelection(new TreeSelection());
                            viewer.refresh();
                            expandTreeRootIfOnlyOneRoot();
                        }
                    });

                    if (PluginChecker.isJobLetPluginLoaded()) {
                        IJobletProviderService jobletService = (IJobletProviderService) GlobalServiceRegister.getDefault()
                                .getService(IJobletProviderService.class);
                        if (jobletService != null) {
                            jobletService.loadComponentsFromProviders();
                        }
                    }

                    timer.stop();
                    // timer.print();
                } catch (InvocationTargetException e) {
                    ExceptionHandler.process(e);
                } catch (Exception e) {
                    MessageBoxExceptionHandler.process(e);
                }
                return Status.OK_STATUS;
            }
        };
        job.setUser(false);
        job.setRule(null);
        job.setPriority(Job.INTERACTIVE);
        job.schedule();

        ProgressDialog dialog = new ProgressDialog(shell, 3000) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                while (true) {
                    if (job.getResult() != null) {
                        return;
                    }
                    Thread.sleep(200);
                }
            }

            @Override
            protected ProgressMonitorDialog newProgressMonitorDialog(Shell shell) {
                return null;
            }

            @Override
            protected void openDialog(ProgressMonitorDialog dialog) {
                ProgressManager.getInstance().showInDialog(shell, job);
            }

            @Override
            protected void dialogRun(ProgressMonitorDialog dialog, IRunnableWithProgress op)
                    throws InvocationTargetException, InterruptedException {
                // nothing to do
            }
        };
        try {
            dialog.executeProcess();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * DOC sgandon Comment method "expandRootIfOnlyOneRoot".
     */
    protected void expandTreeRootIfOnlyOneRoot() {
        // if there is only one root element as in the svn repo then expand the first item
        TreeItem[] rootItems = getViewer().getTree().getItems();
        if (rootItems.length == 1) {
            viewer.setExpandedState(rootItems[0].getData(), true);
        }
    }

    @Override
    public void refreshView() {

        refresh(true);
        refreshContentDescription();

        List<IProcess2> openedProcessList = RepositoryManagerHelper.getOpenedProcess(RepositoryUpdateManager.getEditors());
        List<UpdateResult> updateAllResults = new ArrayList<UpdateResult>();
        IUpdateManager manager = null;
        for (IProcess2 proc : openedProcessList) {
            // UpdateJobletUtils.updateRelationship();
            manager = proc.getUpdateManager();
            List<UpdateResult> updateResults = manager.getUpdatesNeeded(EUpdateItemType.RELOAD);
            updateAllResults.addAll(updateResults);
        }
        if (manager != null) {
            manager.executeUpdates(updateAllResults);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.IRepositoryView#refresh(java.lang.Object)
     */
    @Override
    public void refresh(Object object) {

        if (object != null) {
            // maybe, no effect.
            viewer.refresh(object);
            viewer.setExpandedState(object, true);
        }
    }

    /**
     * only refresh the child of root node.
     */
    @Override
    public void refreshAllChildNodes(RepositoryNode rootNode) {
        if (rootNode != null) {
            rootNode.setInitialized(false);
            // if (!rootNode.getContentType().equals(ERepositoryObjectType.METADATA)) {
            rootNode.getChildren().clear();
            // }
            // for bug 11786
            if (rootNode.getParent() instanceof ProjectRepositoryNode) {
                ((ProjectRepositoryNode) rootNode.getParent()).clearNodeAndProjectCash();
            }

            // contentProvider.getChildren(rootNode); // retrieve child
            viewer.refresh(rootNode);
        }
    }

    @Override
    public void expand(Object object) {
        if (object == null) {
            return;
        }
        boolean state = getExpandedState(object);
        expand(object, !state);
    }

    @Override
    public boolean getExpandedState(Object object) {
        if (object == null) {
            return false;
        }
        boolean state = viewer.getExpandedState(object);
        return state;
    }

    @Override
    public void expand(Object object, boolean state) {
        if (object == null) {
            return;
        }
        viewer.setExpandedState(object, state);
    }

    @Override
    public IProjectRepositoryNode getRoot() {
        return ProjectRepositoryNode.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor#getContributorId()
     */
    @Override
    public String getContributorId() {
        return getSite().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }
        return super.getAdapter(adapter);
    }

    @Override
    public void repositoryChanged(RepositoryChangedEvent event) {
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.ui.views.IRepositoryView#containsRepositoryType(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public boolean containsRepositoryType(ERepositoryObjectType type) {
        return researchRootRepositoryNode(type) != null;
    }

    private RepositoryNode researchRootRepositoryNode(ERepositoryObjectType type) {

        if (type != null && getRoot() instanceof ProjectRepositoryNode) {
            ProjectRepositoryNode pRoot = (ProjectRepositoryNode) getRoot();
            return pRoot.getRootRepositoryNode(type);

        }
        return null;
    }

    public boolean isNoNeedUpdate() {
        return noNeedUpdate;
    }

    public void setNoNeedUpdate(boolean noNeedUpdate) {
        this.noNeedUpdate = noNeedUpdate;
    }

    /*
     * does not return true for the internal editor saveable
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#isSaveOnCloseNeeded()
     */
    @Override
    public boolean isSaveOnCloseNeeded() {
        Saveable[] saveables = super.getSaveables();
        for (Saveable saveable : saveables) {
            if (saveable.isDirty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * return the super active savable + the editor savable to reflect the editor state active is used for the save
     * button state
     * */
    @Override
    public Saveable[] getActiveSaveables() {
        Saveable[] superActiveSaveables = super.getActiveSaveables();
        Saveable[] allSavables = Arrays.copyOf(superActiveSaveables, superActiveSaveables.length + 1);
        allSavables[allSavables.length - 1] = editorSavable;
        return allSavables;
    }

    /*
     * return the super savable + the editor savable to reflect the editor state savable is used for the actually save
     * the state
     */
    @Override
    public Saveable[] getSaveables() {
        Saveable[] superSaveables = super.getSaveables();
        Saveable[] allSavables = Arrays.copyOf(superSaveables, superSaveables.length + 1);
        allSavables[allSavables.length - 1] = editorSavable;
        return allSavables;
    }

    @Override
    public void dispose() {
        final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            activeWorkbenchWindow.removePerspectiveListener(getRepoViewPerspectiveListener());
        }
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace != null) {
            workspace.removeResourceChangeListener(getResourcePostChangerRunnableListener());
        }
        super.dispose();
    }

}
