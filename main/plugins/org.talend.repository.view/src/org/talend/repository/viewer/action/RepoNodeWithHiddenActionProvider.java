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

import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

public class RepoNodeWithHiddenActionProvider extends RepoNodeActionProvider {

    private String[] hiddenIds;

    private boolean hideAction;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.view.di.viewer.action.DIRepositoryNodeActionProvider#fillContextMenu(org.eclipse.jface.
     * action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager manager) {
        hideAction = false;
        IStructuredSelection sel = (IStructuredSelection) getContext().getSelection();
        Object obj = sel.getFirstElement();
        if (obj != null && obj instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) obj;
            ProjectRepositoryNode root = (ProjectRepositoryNode) node.getRoot();
            if (root != null && enableHideAction(node)) {
                hideAction = true;
            }
        }
        super.fillContextMenu(manager);
    }

    protected boolean enableHideAction(RepositoryNode node) {
        return false;
    }

    protected String[] initHiddenActionIds() {
        return new String[0];
    }

    private String[] getHiddenActionIds() {
        if (hiddenIds == null) {
            hiddenIds = initHiddenActionIds();
        }
        if (hiddenIds == null) {
            return new String[0];
        }
        return hiddenIds;
    }

    @Override
    protected void checkAndAddActionInMenu(ITreeContextualAction action, IStructuredSelection sel, IMenuManager manager,
            MenuManager[] menuManagerGroups, Set<String> processedGroupIds) {
        if (hideAction) {
            String actionId = action.getId();
            if (actionId != null && ArrayUtils.contains(getHiddenActionIds(), actionId)) {
                return;
            }
        }
        super.checkAndAddActionInMenu(action, sel, manager, menuManagerGroups, processedGroupIds);
    }
}
