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
package org.talend.commons.ui.utils.workbench.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.PlatformUI;

/**
 * created by ggu on Sep 25, 2014 Detailled comment
 *
 */
public class PreferencesHelper {

    public static boolean removePage(String prefId) {
        return removePages(new String[] { prefId });
    }

    /**
     * 
     * Use the BFS(Breadth First Search) to remove the preferences ids.
     */
    public static boolean removePages(String[] prefIds) {
        if (prefIds == null || prefIds.length == 0) {
            return false;
        }
        PreferenceManager preferenceManager = PlatformUI.getWorkbench().getPreferenceManager();

        List<String> removedIds = new ArrayList<String>();
        IPreferenceNode[] rootSubNodes = preferenceManager.getRootSubNodes();
        for (IPreferenceNode node : rootSubNodes) {
            String id = node.getId();
            if (ArrayUtils.contains(prefIds, id)) {
                preferenceManager.remove(node);
                removedIds.add(id);
            }
        }
        // do for children
        for (IPreferenceNode node : rootSubNodes) {
            removePages(node, prefIds, removedIds);
        }
        //
        List<String> doList = new ArrayList<String>(Arrays.asList(prefIds));
        doList.removeAll(removedIds);

        return doList.isEmpty(); // have done for all, so empty.
    }

    private static void removePages(IPreferenceNode parentNode, String[] prefIds, List<String> removedIds) {

        for (IPreferenceNode node : parentNode.getSubNodes()) {
            String id = node.getId();
            if (ArrayUtils.contains(prefIds, id)) {
                parentNode.remove(node);
                removedIds.add(id);
            }
        }
        // do for children
        for (IPreferenceNode node : parentNode.getSubNodes()) {
            removePages(node, prefIds, removedIds);
        }
    }
}
