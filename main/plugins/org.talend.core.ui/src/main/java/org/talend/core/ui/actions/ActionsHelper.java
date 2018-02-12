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
package org.talend.core.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.MenuManager;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: ActionsHelper.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ActionsHelper {

    private static final Comparator COMP = new ActionsLevelComparator();

    private static List<ITreeContextualAction> actionsToReturn = new ArrayList<ITreeContextualAction>();

    @SuppressWarnings("unchecked")
    public static synchronized List<ITreeContextualAction> getRepositoryContextualsActions() {
        if (actionsToReturn.isEmpty()) {
            IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                    "org.talend.core.repositoryContextualsActions", //$NON-NLS-1$
                    "Action"); //$NON-NLS-1$
            List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(actionExtensionPoint);

            for (IConfigurationElement current : extension) {
                try {
                    ITreeContextualAction currentAction = (ITreeContextualAction) current.createExecutableExtension("class"); //$NON-NLS-1$
                    try {
                        int level = Integer.parseInt(current.getAttribute("level")); //$NON-NLS-1$
                        currentAction.setLevel(level);
                    } catch (NumberFormatException e) {
                        currentAction.setLevel(1000);
                    } finally {
                        currentAction.setId(current.getAttribute("id")); //$NON-NLS-1$
                        currentAction.setReadAction("true".equals(current.getAttribute("isReadAction"))); //$NON-NLS-1$ //$NON-NLS-2$
                        currentAction.setEditAction("true".equals(current.getAttribute("isEditAction"))); //$NON-NLS-1$ //$NON-NLS-2$
                        currentAction.setPropertiesAction("true".equals(current.getAttribute("isPropertiesAction"))); //$NON-NLS-1$ //$NON-NLS-2$
                        if (!"".equals(current.getAttribute("groupId")) && current.getAttribute("groupId") != null) {//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            currentAction.setGroupId(current.getAttribute("groupId"));
                        }

                        actionsToReturn.add(currentAction);
                    }
                } catch (CoreException e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                }
            }
            Collections.sort(actionsToReturn, COMP);
        }
        return actionsToReturn;
    }

    public static MenuManager[] getRepositoryContextualsActionGroups() {

        IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                "org.talend.core.repositoryContextualsActions", //$NON-NLS-1$
                "Group"); //$NON-NLS-1$
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(actionExtensionPoint);

        Set<RepositoryContextualsActionGroup> tempSet = new HashSet<RepositoryContextualsActionGroup>();

        for (IConfigurationElement current : extension) {
            final String id = current.getAttribute("id");
            final String name = current.getAttribute("name");
            final String parentId = current.getAttribute("parentId");
            if (id != null && name != null) {
                tempSet.add(new RepositoryContextualsActionGroup(id, name, parentId));
            }
        }

        //
        Set<MenuManager> groups = new HashSet<MenuManager>();
        for (RepositoryContextualsActionGroup group : tempSet) {
            final MenuManager menuManger = group.getMenuManger();
            RepositoryContextualsActionGroup parentGroup = findGroup(tempSet, group.getParentId());
            if (parentGroup != null) {
                menuManger.setParent(parentGroup.getMenuManger());
                parentGroup.getMenuManger().add(menuManger);
            }
            groups.add(menuManger);
        }
        return groups.toArray(new MenuManager[0]);
    }

    private static RepositoryContextualsActionGroup findGroup(Set<RepositoryContextualsActionGroup> tempSet, String id) {
        if (id != null) {
            for (RepositoryContextualsActionGroup group : tempSet) {
                if (group.getMenuManger().getId().equals(id)) {
                    return group;
                }
            }
        }
        return null;
    }

    private static MenuManager findParentNameByParentId(String parentId, List<IConfigurationElement> extension,
            MenuManager relation) {
        String parentName = null;
        String grandFatherId = null;
        MenuManager tempFather = null;
        for (IConfigurationElement current : extension) {
            String id = current.getAttribute("id");
            if (id.equals(parentId)) {
                parentName = current.getAttribute("name");
                grandFatherId = current.getAttribute("parentId");
                tempFather = new MenuManager(parentName, id);
                relation.setParent(tempFather);
                if (grandFatherId != null) {
                    findParentNameByParentId(grandFatherId, extension, (MenuManager) relation.getParent());
                }
                break;
            }
        }
        return relation;
    }

    /**
     * 
     * hywang ActionsHelper class global comment. Detailled comment
     */
    static class RepositoryContextualsActionGroup {

        private String parentId;

        private MenuManager menuManger;

        public RepositoryContextualsActionGroup(String id, String name, String parentId) {
            super();
            assert (id != null && name != null);
            this.menuManger = new MenuManager(name, id);
            this.parentId = parentId;
        }

        public String getParentId() {
            return this.parentId;
        }

        public MenuManager getMenuManger() {
            return this.menuManger;
        }

    }

    /**
     * 
     * DOC smallet ActionsHelper class global comment. Detailled comment <br/>
     * 
     * $Id: ActionsHelper.java 7038 2007-11-15 14:05:48Z plegall $
     * 
     */
    private static class ActionsLevelComparator implements Comparator<ITreeContextualAction> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(ITreeContextualAction o1, ITreeContextualAction o2) {
            Integer l1 = o1.getLevel();
            Integer l2 = o2.getLevel();
            return l1.compareTo(l2);
        }

    }

    /**
     * 
     * DOC xye Comment method "getActionById".
     * 
     * @param id
     * @return
     */
    public static ITreeContextualAction getActionById(String id) {
        if (id == null || "".equals(id)) {
            return null;
        }
        List<ITreeContextualAction> actions = getRepositoryContextualsActions();
        if (actions != null && !actions.isEmpty()) {
            for (ITreeContextualAction action : actions) {
                if (id.equals(action.getId())) {
                    return action;
                }
            }
        }
        return null;
    }
}
