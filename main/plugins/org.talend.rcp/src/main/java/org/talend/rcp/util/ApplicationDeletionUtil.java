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
package org.talend.rcp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * cli class global comment. Detailled comment
 */
public final class ApplicationDeletionUtil {

    private ApplicationDeletionUtil() {
    }

    public static void removeAndResetPreferencePages(IWorkbenchWindow window, final List<String> prefsId ,boolean needReset) {
        if (prefsId == null || prefsId.isEmpty()) {
            return;
        }
        if (window == null) {
            window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        if (window == null) {
            return;
        }
        List<IPreferenceNode> prefsToDelete = new ArrayList<IPreferenceNode>(); 
        List<IPreferenceNode> prefsToDeleteParents = new ArrayList<IPreferenceNode>();
        IWorkbench workbench = window.getWorkbench();
        if (workbench == null) {
            return;
        }
        PreferenceManager preferenceManager = workbench.getPreferenceManager();

        
        for (IPreferenceNode node : preferenceManager.getRootSubNodes()) {
            IPreferenceNode existPreferenceNode = existPreferenceNode(prefsToDeleteParents, null, node, prefsId);
            if (existPreferenceNode != null) {
                prefsToDelete.add(existPreferenceNode);
            }
        }
        
        // reset mdm preference ,see TMDM-3215
        if(needReset){
           resetMDMNode(preferenceManager,prefsToDeleteParents);
        }
        
        if (!prefsToDelete.isEmpty()) {
            List<IPreferenceNode> tmpPrefsToDelete = new ArrayList<IPreferenceNode>();
            for (IPreferenceNode node : preferenceManager.getRootSubNodes()) {
                for (IPreferenceNode delNode : prefsToDelete) {
                    if (node.remove(delNode)) {
                        tmpPrefsToDelete.add(delNode);
                    }
                }
            }
            prefsToDelete.removeAll(tmpPrefsToDelete);
            if (!prefsToDelete.isEmpty()) {
                for (IPreferenceNode parent : prefsToDeleteParents) {
                    for (IPreferenceNode delNode : prefsToDelete) {
                        parent.remove(delNode);
                    }
                }
            }
        }
    }

    private static void resetMDMNode(PreferenceManager preferenceManager, List<IPreferenceNode> prefsToDeleteParents) {

        IPreferenceNode mdmNode = null;
        IPreferenceNode newRepositoryNode = null;
        
    	for (IPreferenceNode node : preferenceManager.getRootSubNodes()) {
             String[] nPrefsId = { "org.talend.mdm.repository.ui.preferences.AutoDeployPreferencePage" }; //$NON-NLS-1$ 
             IPreferenceNode existPreferenceNode = existPreferenceNode(prefsToDeleteParents, null, node, Arrays.asList(nPrefsId));
             if (existPreferenceNode != null) {
             	mdmNode = existPreferenceNode; 
            }
        }
         
        for (IPreferenceNode node : preferenceManager.getRootSubNodes()) {
        	String[] nPrefsId = { "org.talend.mdm.repository.ui.preferences.RepositoryPreferencePage" }; //$NON-NLS-1$ 
            IPreferenceNode existPreferenceNode = existPreferenceNode(prefsToDeleteParents, null, node, Arrays.asList(nPrefsId));
            if (existPreferenceNode != null) {
         	   newRepositoryNode = existPreferenceNode; 
            }
        } 
        if(newRepositoryNode!=null && mdmNode!=null)
            newRepositoryNode.add(mdmNode);
	}

	private static IPreferenceNode existPreferenceNode(List<IPreferenceNode> prefsToDeleteParents, IPreferenceNode parentNode,
            IPreferenceNode node, List<String> prefsId) {
        if (node != null) {
            if (prefsId.contains(node.getId())) {
                if (parentNode != null) {
                    prefsToDeleteParents.add(parentNode);
                }
                return node;
            }

            for (IPreferenceNode child : node.getSubNodes()) {
                IPreferenceNode existPreferenceNode = existPreferenceNode(prefsToDeleteParents, node, child, prefsId);
                if (existPreferenceNode != null) {
                    return existPreferenceNode;
                }
            }
        }
        return null;
    }
}
