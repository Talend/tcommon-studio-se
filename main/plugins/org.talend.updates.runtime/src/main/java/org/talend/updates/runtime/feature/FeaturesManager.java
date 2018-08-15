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
package org.talend.updates.runtime.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.updates.runtime.engine.ExtraFeaturesUpdatesFactory;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.ExtraFeature;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManager {

    private List<ExtraFeature> featuresCache;

    private List<ExtraFeature> updatesCache;

    private ExtraFeaturesUpdatesFactory extraFeaturesFactory;

    public FeaturesManager() {
        extraFeaturesFactory = new ExtraFeaturesUpdatesFactory();
        featuresCache = new ArrayList<>();
    }

    public SearchResult searchFeatures(IProgressMonitor monitor, SearchOption searchOption)
            throws Exception {
        if (searchOption.isClearCache()) {
            Set<ExtraFeature> componentFeatures = new LinkedHashSet<>();
            getExtraFeatureFactory().retrieveAllComponentFeatures(monitor, componentFeatures);
            featuresCache.clear();
            featuresCache = new ArrayList<>(componentFeatures);
        }
        int page = searchOption.getPage();
        int pageSize = searchOption.getPageSize();
        int start = page * pageSize;
        int end = start + pageSize;
        ExtraFeature[] copyOfRange = Arrays.copyOfRange(featuresCache.toArray(new ExtraFeature[0]), start, end);
        SearchOption option = searchOption.clone();
        option.setClearCache(false);
        SearchResult result = new SearchResult(option, Arrays.asList(copyOfRange));
        result.setTotalSize(featuresCache.size());
        result.setPageSize(pageSize);
        result.setCurrentPage(page);
        return result;
    }

    private Collection<InstalledFeature> getInstalledFeatures(IProgressMonitor monitor) {
        Collection<InstalledFeature> installedFeatures = null;
        return installedFeatures;
    }

    public SearchResult checkUpdates(IProgressMonitor monitor, SearchOption searchOption) {
        SearchResult result = null;
        return result;
    }

    private ExtraFeaturesUpdatesFactory getExtraFeatureFactory() {
        return this.extraFeaturesFactory;
    }

    public static class InstalledFeature {

    }

    public static class SearchOption implements Cloneable {

        private Type type;

        private Category category;

        private String keyword;

        private int page;

        private int pageSize;

        private boolean clearCache = true;

        public SearchOption(Type type, Category category, String keyword) {
            this.type = type;
            this.category = category;
            this.keyword = keyword;
            this.page = -1;
            this.pageSize = -1;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Category getCategory() {
            return this.category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getPage() {
            return this.page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public boolean isClearCache() {
            return this.clearCache;
        }

        public void setClearCache(boolean clearCache) {
            this.clearCache = clearCache;
        }

        @Override
        public SearchOption clone() throws CloneNotSupportedException {
            return (SearchOption) super.clone();
        }
    }

    public static class SearchResult {

        private SearchOption searchOption;

        private int totalSize;

        private int currentPage;

        private int pageSize;

        private Collection<ExtraFeature> currentPageResult;

        public SearchResult(SearchOption searchOption, Collection<ExtraFeature> result) {
            this.searchOption = searchOption;
            this.currentPageResult = result;
            this.totalSize = -1;
            this.currentPage = -1;
            this.pageSize = -1;
        }

        public int getTotalSize() {
            return this.totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        /**
         * Start with 0
         * 
         * @return
         */
        public int getCurrentPage() {
            return this.currentPage;
        }

        /**
         * Start with 0
         * 
         * @param currentPage
         */
        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public Collection<ExtraFeature> getCurrentPageResult() {
            return this.currentPageResult;
        }

        public void setCurrentPageResult(Collection<ExtraFeature> currentPageResult) {
            this.currentPageResult = currentPageResult;
        }

        public int getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public SearchOption getSearchOption() {
            return this.searchOption;
        }

        public void setSearchOption(SearchOption searchOption) {
            this.searchOption = searchOption;
        }

        public boolean hasMultiplePages() {
            return 1 < getTotalPageSize();
        }

        public int getTotalPageSize() {
            if (pageSize <= 0 || totalSize <= 0) {
                return 1;
            }
            if (pageSize < totalSize) {
                return Double.valueOf(Math.ceil(totalSize * 1.0 / pageSize)).intValue();
            } else {
                return 1;
            }
        }
    }

}
