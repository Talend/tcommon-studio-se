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
package org.talend.repository.viewer.filter;

import java.util.Arrays;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.navigator.INavigatorContentDescriptor;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.repository.RepositoryViewPlugin;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PerspectiveFilterHelper {

    private PerspectiveFilterRegistryReader perspectiveFilterExtensionsReader;

    private INavigatorContentService navigatorContentService;

    private String actionProviderId;

    private TreeViewer treeViewer;

    public PerspectiveFilterHelper() {
    }

    public void setNavigatorContentService(INavigatorContentService navigatorContentService) {
        this.navigatorContentService = navigatorContentService;
    }

    public void setTreeViewer(TreeViewer treeViewer) {
        this.treeViewer = treeViewer;
    }

    protected INavigatorContentService getNavigatorContentService() {
        return navigatorContentService;
    }

    protected TreeViewer getTreeViewer() {
        return treeViewer;
    }

    protected String getActionProviderId() {
        return actionProviderId;
    }

    public void setActionProviderId(String actionProviderId) {
        this.actionProviderId = actionProviderId;
    }

    public void doFiltering(final String perspectiveId) {
        if (isActivedPerspectiveFilter()) {
            filterView(perspectiveId, false);
        } else {
            unfilterView(false);
        }
    }

    /**
     * filter on the given perspective
     * 
     * @param perspectiveId
     * @param restoring
     */
    private void filterView(String perspectiveId, boolean restoring) {
        refreshNavigatorContents(
                RepositoryNodeFilterHelper.filterRemovedNavigatorContents(getExtensionIdsToActivate(perspectiveId)), restoring);
    }

    private void unfilterView(boolean restoring) {
        refreshNavigatorContents(
                RepositoryNodeFilterHelper.filterRemovedNavigatorContents(getNavigatorContentService().getVisibleExtensionIds()),
                restoring);
    }

    /**
     * Only activate the view content providers with the extension ID belonging to extensionIdsToActivate array.
     * 
     * 
     * @param extensionIdsToActivate
     * @param restoring, used because setting new selection cause renentrance and may create 2 action types when
     * restoring state
     */
    protected void refreshNavigatorContents(String[] extensionIdsToActivate, boolean restoring) {
        boolean updateExtensionActivation = false;
        String[] contentExtensionsToActivate = Arrays.copyOf(extensionIdsToActivate, extensionIdsToActivate.length);
        // we sort the array in order to use Array.binarySearch();
        Arrays.sort(contentExtensionsToActivate);

        IStructuredSelection ssel = null;
        TreeViewer commonViewer = getTreeViewer();
        INavigatorContentService contentService = getNavigatorContentService();
        try {

            commonViewer.getControl().setRedraw(false);
            // store the selection
            ISelection selection = commonViewer.getSelection();
            if (selection instanceof IStructuredSelection) {
                ssel = (IStructuredSelection) selection;
            }

            INavigatorContentDescriptor[] visibleContentDescriptors = contentService.getVisibleExtensions();

            int indexofContentExtensionIdToBeActivated;
            /* is there a delta? */
            for (int i = 0; i < visibleContentDescriptors.length && !updateExtensionActivation; i++) {
                indexofContentExtensionIdToBeActivated = Arrays.binarySearch(contentExtensionsToActivate,
                        visibleContentDescriptors[i].getId());
                /*
                 * Either we have a filter that should be active that isn't XOR a filter that shouldn't be active that
                 * is currently
                 */
                if (indexofContentExtensionIdToBeActivated >= 0 ^ contentService.isActive(visibleContentDescriptors[i].getId())) {
                    updateExtensionActivation = true;
                }
            }

            /* If so, update */
            if (updateExtensionActivation) {

                contentService.getActivationService().activateExtensions(contentExtensionsToActivate, true);
                contentService.getActivationService().persistExtensionActivations();
                // try to keep expanded elements state
                Object[] expandedElements = commonViewer.getExpandedElements();

                contentService.update();
                commonViewer.refresh();
                if (!restoring) {
                    Object[] originalObjects = ssel != null ? ssel.toArray() : new Object[0];

                    commonViewer.setExpandedElements(expandedElements);
                    // restore the selection.
                    IStructuredSelection newSelection = new StructuredSelection(originalObjects);
                    commonViewer.setSelection(newSelection, true);
                }
            }

        } finally {
            commonViewer.getControl().setRedraw(true);
        }

    }

    protected String[] getExtensionIdsToActivate(String perspectiveId) {
        if (perspectiveFilterExtensionsReader == null) {
            perspectiveFilterExtensionsReader = new PerspectiveFilterRegistryReader(getActionProviderId());
            perspectiveFilterExtensionsReader.readRegistry();
        }
        INavigatorContentService contentService = getNavigatorContentService();
        String[] filteredIds = perspectiveFilterExtensionsReader.getContentProviderFilteredIds(contentService, perspectiveId);
        return filteredIds;
    }

    public static boolean isActivedPerspectiveFilter() {
        final IPreferenceStore preferenceStore = RepositoryViewPlugin.getDefault().getPreferenceStore();
        return preferenceStore.getBoolean(IRepositoryPrefConstants.USE_PERSPECTIVE_FILTER);
    }

    public static void setActivedPerspectiveFilter(boolean activedPerspectiveFilter) {
        final IPreferenceStore preferenceStore = RepositoryViewPlugin.getDefault().getPreferenceStore();
        preferenceStore.setValue(IRepositoryPrefConstants.USE_PERSPECTIVE_FILTER, activedPerspectiveFilter);
    }
}
