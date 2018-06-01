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
package org.talend.repository.ui.actions;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IJobSettingsView;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Used to manage contextual actions on repository objects.<br/>
 * 
 * $Id: AContextualAction.java 219 2006-10-24 13:45:54 +0000 (星期二, 24 十月 2006) smallet $
 * 
 */
public abstract class AContextualAction extends Action implements ITreeContextualAction {

    private int level; // Used to manage order of actions in contextual menu

    private boolean readAction = false;;

    private boolean editAction = false;

    private boolean propertiesAction = false;

    private boolean isToolbar = false;

    private ISelectionProvider specialSelectionProvider = null;

    private String groupId;

    private String neededVersion;

    private boolean avoidUnloadResources;

    private boolean unloadResourcesAfter;

    protected RepositoryNode repositoryNode;

    private Item oldItem;

    private IRepositoryNode node;

    private ISelection selection;

    @Override
    public boolean isEditAction() {
        return editAction;
    }

    @Override
    public void setEditAction(boolean editAction) {
        this.editAction = editAction;
    }

    @Override
    public boolean isReadAction() {
        return readAction;
    }

    @Override
    public void setReadAction(boolean readAction) {
        this.readAction = readAction;
    }

    @Override
    public boolean isPropertiesAction() {
        return propertiesAction;
    }

    @Override
    public void setPropertiesAction(boolean propertiesAction) {
        this.propertiesAction = propertiesAction;
    }

    /**
     * Getter for level.
     * 
     * @return the level
     */
    @Override
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the level.
     * 
     * @param level the level to set
     */
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#isVisible()
     */
    @Override
    public boolean isVisible() {
        return isEnabled();
    }

    /**
     * Returns if this action is accessible by double-click.
     * 
     * @return <code>true</code> if action is accessible by double-click, <code>true</code> otherwise
     */
    @Override
    public final boolean isDoubleClickAction() {
        return getClassForDoubleClick() != null;
    }

    /**
     * Returns the class on wich this action may be call by double-click.
     * 
     * @return the class on wich this action may be call by double-click
     */
    @Override
    public Class getClassForDoubleClick() {
        return null;
    }

    /**
     * Getter for neededVersion.
     * 
     * @return the neededVersion
     */
    public String getNeededVersion() {
        return this.neededVersion;
    }

    /**
     * Sets the neededVersion.
     * 
     * @param neededVersion the neededVersion to set
     */
    public void setNeededVersion(String neededVersion) {
        this.neededVersion = neededVersion;
    }

    /**
     * Convenience method user to refresh view on wich action had been called.
     */
    public void refresh() {
        // TDI-21143 : Studio repository view : remove all refresh call to repo view
        // IRepositoryView viewPart = getViewPart();
        // if (viewPart != null) {
        // viewPart.refresh();
        // }
    }

    /**
     * Convenience method user to refresh view (starting with the given element) on wich action had been called.
     * 
     * @param obj - object to start the refresh on
     */
    public void refresh(Object obj) {
        IRepositoryView viewPart = getViewPart();
        if (viewPart != null) {
            // TDI-21143 : Studio repository view : remove all refresh call to repo view
            // viewPart.refresh(obj);
            viewPart.expand(obj, true);
        }
    }

    private IWorkbenchPart workbenchPart = null;

    /**
     * Getter for workbenchPart.
     * 
     * @return the workbenchPart
     */
    public IWorkbenchPart getWorkbenchPart() {
        return this.workbenchPart;
    }

    /**
     * Sets the workbenchPart.
     * 
     * @param workbenchPart the workbenchPart to set
     */
    @Override
    public void setWorkbenchPart(IWorkbenchPart workbenchPart) {
        this.workbenchPart = workbenchPart;
    }

    public ISelection getSelection() {
        if (this.selection != null) {
            return this.selection;
        }
        return initSelection();
    }

    /**
     * The repository view selection.
     * 
     * @return the selection
     */
    private ISelection initSelection() {
        if (specialSelectionProvider != null) {
            return specialSelectionProvider.getSelection();
        }

        // useful for version property tab
        IWorkbenchPart activePart = getActivePage().getActivePart();

        if (activePart instanceof PropertySheet) {
            return getActivePage().getSelection();
        }

        if (activePart instanceof IJobSettingsView) {
            // wzhang modified to fix 8097.
            ISelection selection = ((IJobSettingsView) activePart).getSelection();
            if (selection == null) {
                selection = getRepositorySelection();
            }
            return selection;
        }
        if (activePart instanceof IRepositoryView) {
            workbenchPart = activePart;
            ISelection selection = ((IRepositoryView) activePart).getViewer().getSelection();
            if (!selection.isEmpty()) {
                return ((IRepositoryView) activePart).getViewer().getSelection();
            } else {
                if (node != null) {
                    selection = new StructuredSelection(node);
                }
                return selection;
            }
        }

        if (workbenchPart != null) {
            if (workbenchPart instanceof IRepositoryView) {
                IRepositoryView view = (IRepositoryView) workbenchPart;
                return view.getViewer().getSelection();
            }
        }
        if (getActivePage().getActiveEditor() == null) {
            workbenchPart = activePart;
            if (workbenchPart instanceof IRepositoryView) {
                IRepositoryView view = (IRepositoryView) workbenchPart;
                return view.getViewer().getSelection();
            }
            return null;
        }

        IWorkbenchPartSite site = getActivePage().getActiveEditor().getSite();
        ISelectionProvider selectionProvider = site.getSelectionProvider();
        // if selectionProvider is null then avoid a NPE is required,see TDI-18275
        return selectionProvider == null ? null : selectionProvider.getSelection();
    }

    /**
     * 
     * Returns the repository view..
     * 
     * @return - the repository biew
     */
    public IRepositoryView getViewPart() {
        if (workbenchPart != null) {
            if (workbenchPart instanceof IRepositoryView) {
                return (IRepositoryView) workbenchPart;
            }
        }
        return RepositoryManagerHelper.findRepositoryView();
    }

    protected IWorkbench getWorkbench() {
        IWorkbenchWindow workbenchWindow = getWorkbenchWindow();
        IWorkbench workbench = null;
        if (workbenchWindow != null) {
            workbench = workbenchWindow.getWorkbench();
        }
        return workbench;
    }

    protected IWorkbenchWindow getWorkbenchWindow() {
        IWorkbenchWindow workbenchWindow = null;
        IRepositoryView viewPart = this.getViewPart();
        if (viewPart != null) {
            workbenchWindow = viewPart.getViewSite().getWorkbenchWindow();
        } else {
            workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        return workbenchWindow;
    }

    /**
     * Returns the currently active page for this workbench window.
     * 
     * @return the active page, or <code>null</code> if none
     */
    public IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    protected void notifySQLBuilder(List<IRepositoryViewObject> list) {
        CoreRuntimePlugin.getInstance().getRepositoryService().notifySQLBuilder(list);
    }

    /**
     * Getter for isToolbar.
     * 
     * @return the isToolbar
     */
    public boolean isToolbar() {
        return this.isToolbar;
    }

    /**
     * Sets the isToolbar.
     * 
     * @param isToolbar the isToolbar to set
     */
    public void setToolbar(boolean isToolbar) {
        this.isToolbar = isToolbar;
    }

    public void setCurrentRepositoryNode(RepositoryNode node) {
        this.node = node;
    }

    public RepositoryNode getCurrentRepositoryNode() {
        if (this.node != null) {
            return (RepositoryNode) this.node;
        }
        return initCurrentRepositoryNode();
    }

    /**
     * DOC qzhang Comment method "getCurrentRepositoryNode".
     * 
     * @return
     */
    protected RepositoryNode initCurrentRepositoryNode() {
        ISelection selection;
        IWorkbenchPage activePage = getActivePage();
        if (activePage == null) {
            selection = getSelection();
        } else {
            selection = getRepositorySelection();
        }
        // RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);
        // RepositoryNode fileDelimitedNode = metadataNode.getChildren().get(1); getSelection()
        if (selection == null) {
            return null;
        }
        Object obj = null;
        if (selection instanceof IStructuredSelection) {
            obj = ((IStructuredSelection) selection).getFirstElement();

        }
        if (obj == null) {
            selection = getSelection();
            if (selection != null && selection instanceof IStructuredSelection) {
                obj = ((IStructuredSelection) selection).getFirstElement();
            }
        }
        if (!(obj instanceof RepositoryNode)) {
            return null;
        }
        RepositoryNode node = (RepositoryNode) obj;
        if (node == null || node.getObject() == null || !(node.getObject() instanceof RepositoryNode)) {
            return node;
        }
        Property property = node.getObject().getProperty();
        Property updatedProperty = null;

        try {
            IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            proxyRepositoryFactory.initialize();

            updatedProperty = proxyRepositoryFactory.getLastVersion(
                    new Project(ProjectManager.getInstance().getProject(property.getItem())), property.getId()).getProperty();
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        // update the property of the node repository object
        // node.getObject().setProperty(updatedProperty);

        return node;
    }

    /**
     * DOC wzhang Comment method "getRepositorySelection".
     * 
     * @return
     */
    protected ISelection getRepositorySelection() {
        IRepositoryView repositoryViewPart = null;
        for (IViewReference viewRef : getActivePage().getViewReferences()) {
            if (viewRef.getView(false) instanceof IRepositoryView) {
                repositoryViewPart = (IRepositoryView) viewRef.getView(false);
                break;
            }
        }

        if (repositoryViewPart == null) {
            // comment by bug 10542
            // try {
            // throw new SystemException("Repository view not found");
            // } catch (SystemException e) {
            // ExceptionHandler.process(e);
            // }
            return null;
        }

        ISelection selection = repositoryViewPart.getViewer().getSelection();
        return selection;
    }

    /**
     * 
     * DOC ggu Comment method "getRepositoryNodeByDefault".
     * 
     * get the type of repository node for default.
     */
    public RepositoryNode getRepositoryNodeForDefault(ERepositoryObjectType type) {
        IRepositoryView repositoryView = getViewPart();
        if (repositoryView == null) {
            return null;
        }
        return searchRepositoryNode((IRepositoryNode) repositoryView.getRoot(), type);
    }

    private RepositoryNode searchRepositoryNode(IRepositoryNode root, ERepositoryObjectType type) {
        if (root == null || type == null) {
            return null;
        }
        IRepositoryNode foundNode = null;
        List<IRepositoryNode> chindren = root.getChildren();
        for (IRepositoryNode repositoryNode : chindren) {
            if (repositoryNode.getContentType() == type) {
                foundNode = repositoryNode;
            } else {
                foundNode = searchRepositoryNode(repositoryNode, type);
            }
            if (foundNode != null) {
                return (RepositoryNode) foundNode;
            }
        }
        return null;
    }

    /**
     * yzhang Comment method "isUnderUserDefined".
     * 
     * @param node
     * @return
     */
    protected boolean isUnderUserDefined(RepositoryNode node) {
        if (node.getType() == ENodeType.SYSTEM_FOLDER || node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
            return false;
        }
        Object obj = node.getProperties(EProperties.LABEL);
        if (obj instanceof String) {
            if (((String) obj).equals(RepositoryConstants.USER_DEFINED)) {
                return true;
            }
        }

        return isUnderUserDefined(node.getParent());
    }

    protected boolean isUnderDBConnection(RepositoryNode node) {
        if (node == null) {
            return false;
        }
        if ((node.getType() == ENodeType.SYSTEM_FOLDER || node.getType() == ENodeType.STABLE_SYSTEM_FOLDER)
                && node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CONNECTIONS) {
            return true;
        }
        return isUnderDBConnection(node.getParent());
    }

    protected boolean isUnderValidationRuleConnection(IRepositoryNode repNode) {
        if (repNode == null) {
            return false;
        }
        if ((repNode.getType() == ENodeType.SYSTEM_FOLDER || repNode.getType() == ENodeType.STABLE_SYSTEM_FOLDER)
                && repNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_VALIDATION_RULES) {
            return true;
        }
        return isUnderValidationRuleConnection(repNode.getParent());
    }

    /**
     * Display a "Save job" prompt dialog if the job eidtor of the selectedNode is unsaved.
     */
    protected void promptForSavingIfNecessary(RepositoryNode selectedNode) {
        try {
            IEditorReference[] references = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getEditorReferences();
            if (references == null || references.length == 0) {
                return;
            }

            String label = selectedNode.getObject().getProperty().getLabel();

            for (IEditorReference reference : references) {
                IEditorPart part = reference.getEditor(false);
                // find unsaved dialog
                if (part == null || part.isDirty() == false) {
                    continue;
                }

                IEditorInput input = part.getEditorInput();

                if (label.equals(input.getName())) {
                    // we have found an unsaved editor that matches the selected repository node
                    if (promptForSavingDialog(part) == ISaveablePart2.YES) {
                        part.doSave(new NullProgressMonitor());
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * Display a prompt dialog to ask the user if we should save the job before duplicating.
     * 
     * @param part
     * @return
     */
    protected int promptForSavingDialog(IEditorPart part) {
        String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };
        // String message = NLS.bind(WorkbenchMessages.EditorManager_saveChangesQuestion, part.getTitle());
        String message = Messages.getString("AContextualAction.EditorManager_saveChangesQuestion", //$NON-NLS-1$
                new Object[] { part.getTitle() });
        // Dialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), WorkbenchMessages.Save_Resource,
        // null, message,
        // MessageDialog.QUESTION, buttons, 0) {
        Dialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
                Messages.getString("AContextualAction.Save_Resource"), null, message, //$NON-NLS-1$
                MessageDialog.QUESTION, buttons, 0) {

            @Override
            protected int getShellStyle() {
                return SWT.NONE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | getDefaultOrientation();
            }
        };
        return dialog.open();
    }

    @Override
    public void setSpecialSelection(ISelectionProvider selectionProvider) {
        this.specialSelectionProvider = selectionProvider;
    }

    protected void updateNodeToLastVersion() {
    }

    public void setAvoidUnloadResources(boolean avoidUnloadResources) {
        this.avoidUnloadResources = avoidUnloadResources;
    }

    @Override
    public void run() {
        String name = "User action : " + getText(); //$NON-NLS-1$

        oldItem = null;
        // if (node == null) {
        // clean old node cache first
        node = null;
        node = getCurrentRepositoryNode();
        // }
        if (node != null) {
            IRepositoryViewObject object = node.getObject();
            if (object != null) {
                Property oldProperty = node.getObject().getProperty();
                if (oldProperty != null) {
                    oldItem = oldProperty.getItem();
                }
            }
        }
        final IRepositoryNode userSelectedNode = node;
        // clean old selection cache first
        selection = null;
        final ISelection userSelection = getSelection();

        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(name, this) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    node = userSelectedNode;
                    selection = userSelection;
                    if (node != null && node.getObject() != null) {
                        Property property = node.getObject().getProperty();
                        // only avoid NPE if item has been deleted in svn
                        if (property != null) {
                            doRun();
                        }
                    } else {
                        doRun();
                    }
                } finally {
                    node = null;
                    selection = null;
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(isAvoidUnloadResources());
        repositoryWorkUnit.setUnloadResourcesAfterRun(isUnloadResourcesAfter());
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
        oldItem = null;
    }

    protected abstract void doRun();

    protected boolean isLastVersion(IRepositoryNode repositoryObject) {
        if (repositoryObject.getObject() instanceof RepositoryViewObject) {
            return true;
        }
        try {
            if (repositoryObject.getObject() != null) {
                Property property = repositoryObject.getObject().getProperty();

                Item item = repositoryObject.getObject().getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
                        IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
                        if (service != null) {
                            boolean flag = service.isServiceItem(item.eClass().getClassifierID());
                            if (!flag) {
                                return true;
                            }
                        }
                    }
                }

                List<IRepositoryViewObject> allVersion = null;
                ItemState state = property.getItem().getState();
                boolean pathExist = false;
                if (state != null) {
                    String path = state.getPath();
                    if (path != null) {
                        File f = new File(path);
                        if (f.exists()) {
                            pathExist = true;
                        }
                    }
                }
                if (pathExist) {
                    allVersion = CoreRuntimePlugin
                            .getInstance()
                            .getProxyRepositoryFactory()
                            .getAllVersion(property.getId(), state.getPath(),
                                    repositoryObject.getObject().getRepositoryObjectType());
                } else {
                    allVersion = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getAllVersion(property.getId());
                }
                if (allVersion == null || allVersion.isEmpty()) {
                    return false;
                }
                String lastVersion = VersionUtils.DEFAULT_VERSION;

                for (IRepositoryViewObject object : allVersion) {
                    if (VersionUtils.compareTo(object.getVersion(), lastVersion) > 0) {
                        lastVersion = object.getVersion();
                    }
                }
                if (VersionUtils.compareTo(property.getVersion(), lastVersion) == 0) {
                    return true;
                }
            }
        } catch (PersistenceException e) {
            //
        }
        return false;
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isAvoidUnloadResources() {
        return avoidUnloadResources;
    }

    public Item getOldItem() {
        return this.oldItem;
    }

    /**
     * Sets the node.
     * 
     * @param node the node to set
     */
    public void setNode(IRepositoryNode node) {
        this.node = node;
    }

    /**
     * Getter for node.
     * 
     * @return the node
     */
    public IRepositoryNode getNode() {
        return this.node;
    }

    public boolean isUnloadResourcesAfter() {
        return unloadResourcesAfter;
    }

    public void setUnloadResourcesAfter(boolean unloadResourcesAfter) {
        this.unloadResourcesAfter = unloadResourcesAfter;
    }

    @Override
    public ITreeContextualAction clone() throws CloneNotSupportedException {
        return (ITreeContextualAction) super.clone();
    }
}
