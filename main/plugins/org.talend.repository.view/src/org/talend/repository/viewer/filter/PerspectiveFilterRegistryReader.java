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
package org.talend.repository.viewer.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.core.utils.RegistryReader;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class PerspectiveFilterRegistryReader extends RegistryReader {

    public static class PerspectiveFilterDescription {

        /**
         * 
         */
        private static final String PATTERN = "pattern"; //$NON-NLS-1$

        /**
         * 
         */
        private static final String CONTENT_EXTENSION = "contentExtension"; //$NON-NLS-1$

        /**
         * 
         */
        private static final String INCLUDES = "includes"; //$NON-NLS-1$

        /**
         * 
         */
        static final String PERSPECTIVE_ID = "perspectiveId"; //$NON-NLS-1$

        /**
         * 
         */
        static final String ACTION_PROVIDER_ID = "actionProviderId"; //$NON-NLS-1$

        private static final String EXCLUDES = "excludes"; //$NON-NLS-1$

        private String actionProviderId;

        /**
         * Getter for actionProviderId.
         * 
         * @return the actionProviderId
         */
        public String getActionProviderId() {
            return this.actionProviderId;
        }

        private String perspectiveId;

        /**
         * Getter for perspectiveId.
         * 
         * @return the perspectiveId
         */
        public String getPerspectiveId() {
            return this.perspectiveId;
        }

        private Set<Pattern> actionProviderIncludes;

        private Set<Pattern> actionProviderExcludes;

        /**
         * Getter for actionProviderIncludes.
         * 
         * @return the actionProviderIncludes
         */
        public Set<Pattern> getActionProviderIncludes() {
            return this.actionProviderIncludes;
        }

        /**
         * DOC sgandon PerspectiveFilterRegistryReader.PerspectiveFilterDescription constructor comment.
         */
        public PerspectiveFilterDescription(IConfigurationElement element) {
            init(element);
        }

        /**
         * DOC sgandon Comment method "init".
         * 
         * @param element
         */
        private void init(IConfigurationElement element) {
            perspectiveId = element.getAttribute(PERSPECTIVE_ID);
            actionProviderId = element.getAttribute(ACTION_PROVIDER_ID);
            IConfigurationElement[] includesArray = element.getChildren(INCLUDES);
            actionProviderIncludes = new HashSet<Pattern>(includesArray.length);
            for (IConfigurationElement include : includesArray) {
                IConfigurationElement[] contentExtensions = include.getChildren(CONTENT_EXTENSION);
                for (IConfigurationElement contentExtension : contentExtensions) {
                    actionProviderIncludes.add(Pattern.compile(contentExtension.getAttribute(PATTERN)));
                }
            }
            IConfigurationElement[] excludesArray = element.getChildren(EXCLUDES);
            actionProviderExcludes = new HashSet<Pattern>(excludesArray.length);
            for (IConfigurationElement exclude : excludesArray) {
                IConfigurationElement[] contentExtensions = exclude.getChildren(CONTENT_EXTENSION);
                for (IConfigurationElement contentExtension : contentExtensions) {
                    actionProviderExcludes.add(Pattern.compile(contentExtension.getAttribute(PATTERN)));
                }
            }
        }

        /**
         * DOC sgandon Comment method "getActionProviderExcludes".
         * 
         * @return
         */
        public Set<Pattern> getActionProviderExcludes() {
            return this.actionProviderExcludes;
        }
    }

    private static Logger log = Logger.getLogger(PerspectiveFilterRegistryReader.class);

    /**
     * 
     */
    private static final String PLUGIN_ID = "org.talend.repository.viewer"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String PERSPECTIVE_FILTER_EXTENSION = "perspective.filter"; //$NON-NLS-1$

    private static final Object PERSPECTIVE_FILTER = "perspectiveFilter"; //$NON-NLS-1$

    protected Set<PerspectiveFilterDescription> perspectiveFilters;

    private final String actionProviderId;

    /**
     * DOC sgandon PerspectiveFilterRegistryReader constructor comment.
     * 
     * @param aPluginId
     * @param anExtensionPoint
     */
    protected PerspectiveFilterRegistryReader(String actionProviderId) {
        super(PLUGIN_ID, PERSPECTIVE_FILTER_EXTENSION);
        this.actionProviderId = actionProviderId;
        perspectiveFilters = new HashSet<PerspectiveFilterDescription>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.filter.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if (PERSPECTIVE_FILTER.equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    if (actionProviderId.equals(element.getAttribute(PerspectiveFilterDescription.ACTION_PROVIDER_ID))) {
                        perspectiveFilters.add(new PerspectiveFilterDescription(element));
                    }

                }

            });
        }
        return PERSPECTIVE_FILTER_EXTENSION.equals(element.getName());
    }

    /**
     * return the list of content providers ID for the given content services thar match the extension point for
     * perspective filter. if no filter is set then all contents IDs are returned.
     * 
     * @param contentService
     * @param perspectiveId
     * @return the list of contentProviders IDs filtered
     */
    public String[] getContentProviderFilteredIds(INavigatorContentService contentService, String perspectiveId) {
        Set<PerspectiveFilterDescription> filtersForPerspective = getFiltersForPerspective(perspectiveId);
        return computeContentProviderFilteredList(filtersForPerspective, contentService);
    }

    /**
     * use the patterns from the filters to determine if what content IDs to filter. if filters set is empty then all
     * content providers IDs are returned.
     * 
     * @param filtersForPerspective list of filters to apply to compute the contentProviders IDs,
     * @param contentService, list of content providers to be filtered.
     * @return all the content providers Id to be displayed
     */
    private String[] computeContentProviderFilteredList(Set<PerspectiveFilterDescription> filters,
            INavigatorContentService contentService) {
        String[] visibleExtensionIds = contentService.getVisibleExtensionIds();
        List<String> filteredIds = new ArrayList<String>(visibleExtensionIds.length);
        Pattern[] allIncludesPatterns = getAllIncludesPatterns(filters);
        for (String contentId : visibleExtensionIds) {
            for (Pattern pattern : allIncludesPatterns) {
                if (pattern.matcher(contentId).matches()) {
                    filteredIds.add(contentId);
                }
            }
        }
        // if none is filtered then all all of them
        if (filteredIds.isEmpty()) {
            for (String contentId : visibleExtensionIds) {
                filteredIds.add(contentId);
            }
        }
        // filter the content that are excluded
        Pattern[] allExcludesPatterns = getAllExcludesPatterns(filters);
        Iterator<String> filteredContentIte = filteredIds.iterator();
        while (filteredContentIte.hasNext()) {
            String contentId = filteredContentIte.next();
            for (Pattern pattern : allExcludesPatterns) {
                if (pattern.matcher(contentId).matches()) {
                    filteredContentIte.remove();
                    break; // breaks the for loop
                }
            }
        }
        return filteredIds.toArray(new String[filteredIds.size()]);
    }

    /**
     * DOC sgandon Comment method "getAllExcludesPatterns".
     * 
     * @param filters
     * @return
     */
    private Pattern[] getAllExcludesPatterns(Set<PerspectiveFilterDescription> filters) {
        List<Pattern> allExcludes = new ArrayList<Pattern>(filters.size());
        for (PerspectiveFilterDescription pfd : filters) {
            Set<Pattern> actionProviderIncludes = pfd.getActionProviderExcludes();
            allExcludes.addAll(actionProviderIncludes);
        }
        return allExcludes.toArray(new Pattern[allExcludes.size()]);
    }

    /**
     * DOC sgandon Comment method "getAllIncludesPatterns".
     * 
     * @param filters
     * @return
     */
    private Pattern[] getAllIncludesPatterns(Set<PerspectiveFilterDescription> filters) {
        List<Pattern> allIncludes = new ArrayList<Pattern>(filters.size());
        for (PerspectiveFilterDescription pfd : filters) {
            Set<Pattern> actionProviderIncludes = pfd.getActionProviderIncludes();
            allIncludes.addAll(actionProviderIncludes);
        }
        return allIncludes.toArray(new Pattern[allIncludes.size()]);
    }

    /**
     * return all the filters extensions for the given perspective.
     * 
     * @param perspectiveId
     * @return
     */
    private Set<PerspectiveFilterDescription> getFiltersForPerspective(String perspectiveId) {
        Set<PerspectiveFilterDescription> filtersForPerspective = new HashSet<PerspectiveFilterDescription>(
                perspectiveFilters.size());
        for (PerspectiveFilterDescription pfd : perspectiveFilters) {
            if (perspectiveId.equals(pfd.getPerspectiveId())) {
                filtersForPerspective.add(pfd);
            }
        }
        return filtersForPerspective;
    }
}
