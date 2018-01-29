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
package org.talend.commons.ui.swt.geftree.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.talend.commons.ui.swt.geftree.TreeSelectManager;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * cli class global comment. Detailled comment
 */
public class TreeActionFactory {

    private final ActionRegistry actionRegistry = new ActionRegistry();

    private final Map<String, TreeActionGroup> groups = new HashMap<String, TreeActionGroup>();

    public ActionRegistry getActionRegistry() {
        return this.actionRegistry;
    }

    public void addGroup(TreeActionGroup group) {
        if (group != null) {
            this.groups.put(group.getId(), group);
        }
    }

    public Map<String, TreeActionGroup> getActionGroups() {
        return this.groups;
    }

    @SuppressWarnings("unchecked")
    public void buildContextMenu(IMenuManager menu) {
        final TreeBranch selection = TreeSelectManager.getManager().getSelection();

        Map<String, MenuManager> groups = new HashMap<String, MenuManager>();

        Iterator actions = actionRegistry.getActions();
        while (actions.hasNext()) {
            Object obj = actions.next();
            if (obj instanceof IAction) {
                IAction action = (IAction) obj;
                String groupId = null;
                if (action instanceof AbstractTreeAction) {
                    AbstractTreeAction treeAction = (AbstractTreeAction) action;
                    treeAction.init(selection);
                    groupId = treeAction.getGroupId();
                }
                if (action.isEnabled()) {
                    if (groupId != null) {
                        TreeActionGroup actionGroup = getActionGroups().get(groupId);
                        if (actionGroup != null) {
                            MenuManager subMenu = groups.get(groupId);
                            if (subMenu == null) {
                                subMenu = new MenuManager(actionGroup.getText());
                                groups.put(groupId, subMenu);

                                menu.add(new Separator(groupId));
                                menu.appendToGroup(groupId, subMenu);

                            }
                            subMenu.add(action);
                        } else { // add in root
                            menu.add(action);
                        }

                    } else { // add in root
                        menu.add(action);
                    }
                }
            }
        }
    }
}
