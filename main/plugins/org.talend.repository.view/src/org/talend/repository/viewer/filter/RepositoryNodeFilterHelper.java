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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.NavigatorFilterService;
import org.eclipse.ui.internal.navigator.filters.UpdateActiveExtensionsOperation;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorFilterService;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.repository.RepositoryViewPlugin;
import org.talend.repository.navigator.RepoViewCommonViewer;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class RepositoryNodeFilterHelper {

    static class SelectPerspectivesProvider {

        @Inject
        private EModelService fModelService;

        @Inject
        private MWindow fWindow;

        @Inject
        private MApplication fApp;

        private MPerspectiveStack fPerspectiveStack;

        /**
         * copied from RestoreAllRegisteredPerspectivesProvider
         */
        MPerspectiveStack getMPerspectiveStack() {
            if (fPerspectiveStack != null) {
                return fPerspectiveStack;
            }
            MUIElement baseElement = fWindow;
            if (fWindow == null) {
                baseElement = fApp;
            }
            if (baseElement != null) {
                if (fPerspectiveStack == null) {
                    List<MPerspectiveStack> perspStackList = fModelService.findElements(baseElement, null,
                            MPerspectiveStack.class, null);
                    if (perspStackList.size() > 0) {// there must be only one perspectiveStack.
                        fPerspectiveStack = perspStackList.get(0);
                        return fPerspectiveStack;
                    }
                }
                if (fWindow != null) {
                    for (MWindowElement child : fWindow.getChildren()) {
                        if (child instanceof MPerspectiveStack) {
                            fPerspectiveStack = (MPerspectiveStack) child;
                            return fPerspectiveStack;
                        }
                    }
                }
            }
            return null;
        }

        public String getCurrentPerspectiveId() {
            final MPerspectiveStack stack = getMPerspectiveStack();
            if (stack != null) {
                MPerspective persp = stack.getSelectedElement();
                if (persp != null) {
                    return persp.getElementId();
                }
            }
            return null;
        }
    }

    public static final String ITEM_SEPARATOR = "--";

    private static final String PERSPECTIVE_SEPARATOR = ":";

    public static void filter(final CommonViewer commonViewer, boolean activedFilter, boolean activedPerspectiveFilter) {

        processContentExtensions(commonViewer, activedFilter, activedPerspectiveFilter);
        processCommonFilters(commonViewer, activedFilter);

        if (commonViewer instanceof RepoViewCommonViewer) {
            ((RepoViewCommonViewer) commonViewer).fireRefreshNodePerspectiveLisenter();
        }

    }

    private static void processContentExtensions(final CommonViewer commonViewer, boolean activeFilter,
            boolean activedPerspectiveFilter) {
        final INavigatorContentService contentService = commonViewer.getNavigatorContentService();

        String[] visibleExtensionIds = contentService.getVisibleExtensionIds();

        List<String> visibleIDsForPecpective = new ArrayList<String>();
        List<String> visibleIdsForActiveFilter = new ArrayList<String>();

        if (visibleExtensionIds != null) {
            visibleIdsForActiveFilter.addAll(Arrays.asList(visibleExtensionIds));
        }

        if (activedPerspectiveFilter) {
            String perspectiveId = getPerspectiveId();
            if (perspectiveId != null) {
                PerspectiveFilterHelper helper = new PerspectiveFilterHelper();
                helper.setTreeViewer(commonViewer);
                helper.setNavigatorContentService(contentService);
                helper.setActionProviderId(PerspectiveFilterActionProvider.ID);
                String[] pvExtensions = helper.getExtensionIdsToActivate(perspectiveId);

                if (pvExtensions != null && pvExtensions.length > 0) {
                    visibleIDsForPecpective = Arrays.asList(pvExtensions);
                }
            }
            visibleIdsForActiveFilter.retainAll(visibleIDsForPecpective);
        }

        String[] filteredContents = RepositoryNodeFilterHelper.getFilterByNodeValues();
        List<String> filteredContentsList = new ArrayList<String>();
        if (filteredContents != null) {
            filteredContentsList = Arrays.asList(filteredContents);
        }

        List<String> checkedExtensions = new ArrayList<String>();
        if (activeFilter) {
            for (String id : visibleIdsForActiveFilter) {
                if (!filteredContentsList.contains(id)) {
                    checkedExtensions.add(id);
                }
            }
        } else {
            checkedExtensions.addAll(visibleIdsForActiveFilter);
        }
        String[] contentExtensionIdsToActivate = (String[]) checkedExtensions.toArray(new String[checkedExtensions.size()]);
        UpdateActiveExtensionsOperation updateExtensions = new UpdateActiveExtensionsOperation(commonViewer,
                contentExtensionIdsToActivate);
        updateExtensions.execute(null, null);
    }

    private static void processCommonFilters(final CommonViewer commonViewer, boolean activedFilter) {
        // final ICommonFilterDescriptor[] visibleFilterDescriptors =
        // commonViewer.getNavigatorContentService().getFilterService()
        // .getVisibleFilterDescriptors();
        //
        // String[] filters = new String[visibleFilterDescriptors.length];
        //
        // for (int i = 0; i < visibleFilterDescriptors.length; i++) {
        // final String id = visibleFilterDescriptors[i].getId();
        // filters[i] = id;
        // }
        // UpdateActiveFiltersOperation updateFilters = new UpdateActiveFiltersOperation(commonViewer, filters);
        // updateFilters.execute(null, null);
        INavigatorFilterService filterService = commonViewer.getNavigatorContentService().getFilterService();
        if (filterService instanceof NavigatorFilterService) {
            ((NavigatorFilterService) filterService).updateViewer();
        }
    }

    public static String getPerspectiveId() {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
                IPerspectiveDescriptor perspective = activePage.getPerspective();
                if (perspective != null) {
                    String perspectiveId = perspective.getId();
                    return perspectiveId;
                }
            }
        }

        SelectPerspectivesProvider provider = new SelectPerspectivesProvider();
        IEclipseContext activeContext = ((IEclipseContext) PlatformUI.getWorkbench().getService(IEclipseContext.class))
                .getActiveLeaf();
        if (activeContext != null) {
            ContextInjectionFactory.inject(provider, activeContext); // inject
        }
        return provider.getCurrentPerspectiveId();
    }

    public static IPreferenceStore getPreferenceStore() {
        return RepositoryViewPlugin.getDefault().getPreferenceStore();
    }

    public static String getFilterByNodeKey(String perspectiveId, boolean activedPerspectiveFilter) {
        if (activedPerspectiveFilter && perspectiveId != null) {
            return IRepositoryPrefConstants.FILTER_BY_NODE + PERSPECTIVE_SEPARATOR + perspectiveId;
        } else {
            return IRepositoryPrefConstants.FILTER_BY_NODE;
        }

    }

    public static String[] getFilterByNodeValues() {
        Set<String> filteredSet = new HashSet<String>();
        String[] filteredContents = null;
        // collect for all perspectives
        final IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
        for (IPerspectiveDescriptor p : perspectives) {
            filteredContents = getFiltersByPreferenceKey(getFilterByNodeKey(p.getId(), true));
            if (filteredContents != null) {
                filteredSet.addAll(Arrays.asList(filteredContents));
            }
        }
        // add all
        filteredContents = getFiltersByPreferenceKey(getFilterByNodeKey(null, false));
        if (filteredContents != null) {
            filteredSet.addAll(Arrays.asList(filteredContents));
        }
        return filteredSet.toArray(new String[0]);
    }

    public static String[] getFiltersByPreferenceKey(String key) {
        String allValues = getPreferenceStore().getString(key);
        if (allValues == null || "".equals(allValues)) {
            return null;
        }
        String[] split = allValues.split(ITEM_SEPARATOR);
        return split;
    }

    public static String[] convertFromString(String patterns, String separator) {
        StringTokenizer tokenizer = new StringTokenizer(patterns, separator, true);
        int tokenCount = tokenizer.countTokens();
        List result = new ArrayList(tokenCount);
        boolean escape = false;
        boolean append = false;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (separator.equals(token)) {
                if (!escape)
                    escape = true;
                else {
                    addPattern(result, separator);
                    append = true;
                }
            } else {
                if (!append)
                    result.add(token);
                else
                    addPattern(result, token);
                append = false;
                escape = false;
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    private static void addPattern(List list, String pattern) {
        if (list.isEmpty())
            list.add(pattern);
        else {
            int index = list.size() - 1;
            list.set(index, ((String) list.get(index)) + pattern);
        }
    }

    /**
     * DOC ycbai Comment method "filterRemovedNavigatorContents".
     * 
     * @param visibleExtensionIds
     * @return
     */
    public static String[] filterRemovedNavigatorContents(String[] visibleExtensionIds) {
        List<String> resultIds = new ArrayList<String>();
        List<String> needRemovedExtensionIds = getExtensionIdsNeedRemove(visibleExtensionIds);
        for (String extensionId : visibleExtensionIds) {
            if (!needRemovedExtensionIds.contains(extensionId)) {
                resultIds.add(extensionId);
            }
        }

        return resultIds.toArray(new String[resultIds.size()]);
    }

    /**
     * DOC ycbai Comment method "getExtensionIdsNeedRemove".
     * 
     * @param visibleExtensionIds
     * @return
     */
    public static List<String> getExtensionIdsNeedRemove(String[] visibleExtensionIds) {
        List<String> extensionIds = Arrays.asList(visibleExtensionIds);
        List<String> needRemovedExtensionIds = new ArrayList<String>();
        for (String extensionId : extensionIds) {
            addRemoveExtensionId(extensionIds, needRemovedExtensionIds, extensionId, ".removed"); //$NON-NLS-1$
            addRemoveExtensionId(extensionIds, needRemovedExtensionIds, extensionId, ".fake.for.activation"); //$NON-NLS-1$
        }

        return needRemovedExtensionIds;
    }

    /**
     * DOC ycbai Comment method "addRemoveExtensionId".
     * 
     * @param extensionIds
     * @param needRemovedExtensionIds
     * @param extensionId
     * @param removeFlag
     */
    private static void addRemoveExtensionId(List<String> extensionIds, List<String> needRemovedExtensionIds, String extensionId,
            String removeFlag) {
        if (extensionId != null && removeFlag != null && extensionId.toLowerCase().endsWith(removeFlag.toLowerCase())) {
            needRemovedExtensionIds.add(extensionId);
            String realExtensionId = extensionId.substring(0, extensionId.lastIndexOf(removeFlag));
            if (extensionIds.contains(realExtensionId)) {
                needRemovedExtensionIds.add(realExtensionId);
            }
        }
    }

    public static boolean isActivedFilter() {
        final IPreferenceStore preferenceStore = RepositoryViewPlugin.getDefault().getPreferenceStore();
        return preferenceStore.getBoolean(IRepositoryPrefConstants.USE_FILTER);
    }

    public static void setActivedFilter(boolean activedFilter) {
        final IPreferenceStore preferenceStore = RepositoryViewPlugin.getDefault().getPreferenceStore();
        preferenceStore.setValue(IRepositoryPrefConstants.USE_FILTER, activedFilter);
    }
}
