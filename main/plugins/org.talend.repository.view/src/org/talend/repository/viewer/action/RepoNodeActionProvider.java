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
package org.talend.repository.viewer.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.service.ICoreUIService;

public class RepoNodeActionProvider extends CommonActionProvider {

    static protected List<ITreeContextualAction> contextualsActions;

    static private RepoDoubleClickAction doubleClickAction;

    public RepoNodeActionProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite aSite) {

        super.init(aSite);
        makeActions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);

        addActions(manager);
    }

    /**
     * 
     * DOC ggu Comment method "addActions".
     * 
     * check and add all contextualsActions
     * 
     * @param manager
     */
    protected void addActions(IMenuManager manager) {
        // FIXME need check this service for other product. because the extension point is in org.talend.core.
        IStructuredSelection sel = (IStructuredSelection) getContext().getSelection();
        MenuManager[] menuManagerGroups = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreUIService.class)) {
            final ICoreUIService coreService = (ICoreUIService) GlobalServiceRegister.getDefault().getService(
                    ICoreUIService.class);
            if (coreService != null) {
                menuManagerGroups = coreService.getRepositoryContextualsActionGroups();
            }
        }

        Set<String> processedGroupIds = new HashSet<String>();
        // contextual actions
        addContextualActions(manager, sel, menuManagerGroups, processedGroupIds);

        // addition actions
        addAdditionActions(sel, manager, menuManagerGroups, processedGroupIds);

        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    protected void addContextualActions(IMenuManager manager, IStructuredSelection sel, MenuManager[] menuManagerGroups,
            Set<String> processedGroupIds) {
        ICommonViewerWorkbenchSite navWorkSite = ((ICommonViewerWorkbenchSite) getActionSite().getViewSite());
        for (ITreeContextualAction action : contextualsActions) {
            if (navWorkSite != null && navWorkSite.getSite() != null) {
                action.setWorkbenchPart(navWorkSite.getSite().getPart());
            }
            checkAndAddActionInMenu(action, sel, manager, menuManagerGroups, processedGroupIds);
        }
    }

    protected void checkAndAddActionInMenu(ITreeContextualAction action, IStructuredSelection sel, IMenuManager manager,
            MenuManager[] menuManagerGroups, Set<String> processedGroupIds) {
        action.init((TreeViewer) getActionSite().getStructuredViewer(), sel);
        if (action.isVisible() && action.isEnabled()) {
            IMenuManager groupMenu = findMenuManager(menuManagerGroups, action.getGroupId(), true); // find root
            if (groupMenu != null) { // existed
                final String rootId = groupMenu.getId();
                if (!processedGroupIds.contains(rootId)) {
                    manager.add(groupMenu);
                    processedGroupIds.add(rootId);
                }
            }
            groupMenu = findMenuManager(menuManagerGroups, action.getGroupId(), false); // find last child
            if (groupMenu != null) { // existed
                groupMenu.add(action);
            } else { // child
                manager.add(action);
            }
        }
    }

    protected void addAdditionActions(IStructuredSelection sel, IMenuManager manager, MenuManager[] menuManagerGroups,
            Set<String> processedGroupIds) {
        // do nothing
    }

    protected MenuManager findMenuManager(final MenuManager[] menuManagerGroups, String groupId, boolean findParent) {
        if (menuManagerGroups == null) {
            return null;
        }
        for (MenuManager groupMenu : menuManagerGroups) {
            if (groupMenu.getId().equals(groupId)) {
                if (findParent) {
                    final MenuManager parent = (MenuManager) groupMenu.getParent();
                    if (parent == null) {
                        return groupMenu;
                    } else {
                        return findMenuManager(menuManagerGroups, parent.getId(), findParent);
                    }
                } else {
                    return groupMenu;
                }
            }
        }
        return null;
    }

    protected void makeActions() {
        boolean isNewDoubleClickAction = false;
        if (doubleClickAction != null && doubleClickAction.getStructuredViewer() != null) {
            StructuredViewer structuredViewer = doubleClickAction.getStructuredViewer();
            ISelection selection = structuredViewer.getSelection();
            if (selection == null || selection.isEmpty()) {
                isNewDoubleClickAction = true;
            }
        }

        // FIXME need check this service for other product. because the extension point is in org.talend.core.
        if (contextualsActions == null || isNewDoubleClickAction) {
            ICommonViewerWorkbenchSite navWorkSite = ((ICommonViewerWorkbenchSite) getActionSite().getViewSite());
            IHandlerService handlerService = (IHandlerService) navWorkSite.getSite().getService(IHandlerService.class);
            IHandler handler = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreUIService.class)) {
                final ICoreUIService coreService = (ICoreUIService) GlobalServiceRegister.getDefault().getService(
                        ICoreUIService.class);
                if (coreService != null) {
                    contextualsActions = coreService.getRepositoryContextualsActions();
                    for (ITreeContextualAction action : contextualsActions) {
                        action.setWorkbenchPart(navWorkSite.getSite().getPart());
                        if (action.getActionDefinitionId() != null) {
                            // TODO ActionHandler is deprecated, should be changed.
                            handler = new ActionHandler(action);
                            handlerService.activateHandler(action.getActionDefinitionId(), handler);
                        }
                    }
                }
            }
            doubleClickAction = new RepoDoubleClickAction(getActionSite().getStructuredViewer(), contextualsActions);
        }// else already initialised.

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillActionBars(org.eclipse.ui.IActionBars)
     */
    @Override
    public void fillActionBars(IActionBars actionBars) {
        if (doubleClickAction != null && !doubleClickAction.getStructuredViewer().equals(getActionSite().getStructuredViewer())) {
            doubleClickAction.setStructuredViewer(getActionSite().getStructuredViewer());
            ICommonViewerWorkbenchSite navWorkSite = ((ICommonViewerWorkbenchSite) getActionSite().getViewSite());
            if (navWorkSite != null && navWorkSite.getSite() != null) {
                for (ITreeContextualAction action : contextualsActions) {
                    action.setWorkbenchPart(navWorkSite.getSite().getPart());
                }
            }
        }
        super.fillActionBars(actionBars);
        actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, doubleClickAction);

    }

}
