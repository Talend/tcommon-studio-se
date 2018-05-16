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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractRepositoryActionProvider extends CommonActionProvider {

    private boolean contributedToViewMenu = false;

    private List<AContextualAction> actionsList = new ArrayList<AContextualAction>();

    public AbstractRepositoryActionProvider() {
        super();

    }

    protected INavigatorContentService getNavigatorContentService() {
        return getActionSite().getContentService();
    }

    protected CommonViewer getCommonViewer() {
        return (CommonViewer) getActionSite().getStructuredViewer();
    }

    protected CommonNavigator getCommonNavigator() {
        return getCommonViewer().getCommonNavigator();
    }

    protected IStructuredSelection getSelection() {
        return (IStructuredSelection) getContext().getSelection();
    }

    @Override
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);

        makeContextualActions();
    }

    protected void addContextualAction(AContextualAction action) {
        if (action != null) {
            actionsList.add(action);
        }
    }

    protected AContextualAction[] getContextualActions() {
        return this.actionsList.toArray(new AContextualAction[0]);
    }

    /**
     * 
     * DOC ggu Comment method "makeActions".
     * 
     * will add some actions in the actionsList.
     */
    protected void makeContextualActions() {

    }

    @Override
    public void fillActionBars(IActionBars actionBars) {
        if (!contributedToViewMenu) {
            try {
                super.fillActionBars(actionBars);

                final IMenuManager menuManager = actionBars.getMenuManager();
                fillMenus(menuManager);

                final IToolBarManager toolBarManager = actionBars.getToolBarManager();
                fillToolBar(toolBarManager);
            } finally {
                contributedToViewMenu = true;
            }
        }
    }

    protected void fillMenus(final IMenuManager menuManager) {
        //
    }

    protected void fillToolBar(final IToolBarManager toolBarManager) {
        //
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);
        for (AContextualAction action : getContextualActions()) {
            addActionInManager(manager, action);
        }
    }

    private void addActionInManager(IMenuManager manager, AContextualAction action) {
        action.init(getCommonViewer(), getSelection());
        if (action.isVisible() && action.isEnabled()) {
            manager.add(action);
        }
    }

}
