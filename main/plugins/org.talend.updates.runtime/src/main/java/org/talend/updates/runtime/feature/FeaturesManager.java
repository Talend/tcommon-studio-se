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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.threading.TalendCustomThreadPoolExecutor;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.engine.ExtraFeaturesUpdatesFactory;
import org.talend.updates.runtime.engine.P2Manager;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureUpdateNotification;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManager {

    private final Object searchThreadPoolExecutorLock = new Object();

    private final Object updateThreadPoolExecutorLock = new Object();

    private final Object featuresCacheLock = new Object();

    private TalendCustomThreadPoolExecutor searchThreadPoolExecutor;

    private TalendCustomThreadPoolExecutor updateThreadPoolExecutor;

    private List<ExtraFeature> featuresCache;

    private ExtraFeaturesUpdatesFactory extraFeaturesFactory;

    public FeaturesManager() {
        extraFeaturesFactory = new ExtraFeaturesUpdatesFactory();
        featuresCache = new ArrayList<>();
    }

    public SearchResult searchFeatures(IProgressMonitor monitor, SearchOption searchOption)
            throws Exception {
        /**
         * Currently we only support NEXUS, so can use featuresCache, later we may need to change the logic here to
         * support other server types
         */
        // if (searchOption.isClearCache()) {
        // featuresCache.clear();
        // }
        List<ExtraFeature> features = getFeaturesCache(monitor);
        String keyword = searchOption.getKeyword();
        List<ExtraFeature> filteredFeatures = new LinkedList<>();
        if (features != null) {
            Type type = searchOption.getType();
            Category category = searchOption.getCategory();
            if (type == Type.ALL && category == Category.ALL && StringUtils.isBlank(keyword)) {
                filteredFeatures.addAll(features);
            } else {
                Iterator<ExtraFeature> iterator = features.iterator();
                while (iterator.hasNext()) {
                    ExtraFeature next = iterator.next();
                    if (type != Type.ALL) {
                        if (!next.getTypes().contains(type)) {
                            continue;
                        }
                    }
                    if (category != Category.ALL) {
                        if (!next.getCategories().contains(category)) {
                            continue;
                        }
                    }
                    if (StringUtils.isBlank(keyword)) {
                        filteredFeatures.add(next);
                    } else {
                        if (next.getName().toLowerCase().contains(keyword)
                                || next.getDescription().toLowerCase().contains(keyword)) {
                            filteredFeatures.add(next);
                        }
                    }
                }
            }
        }
        int page = searchOption.getPage();
        int pageSize = searchOption.getPageSize();
        int start = page * pageSize;
        int end = start + pageSize;
        ExtraFeature[] copyOfRange = Arrays.copyOfRange(filteredFeatures.toArray(new ExtraFeature[0]), start, end);
        SearchOption option = searchOption.clone();
        option.setClearCache(false);
        SearchResult result = new SearchResult(option, Arrays.asList(copyOfRange));
        result.setTotalSize(filteredFeatures.size());
        result.setPageSize(pageSize);
        result.setCurrentPage(page);
        return result;
    }

    private List<ExtraFeature> getFeaturesCache(IProgressMonitor monitor) throws Exception {
        if (featuresCache == null || featuresCache.isEmpty()) {
            synchronized (featuresCacheLock) {
                if (featuresCache == null || featuresCache.isEmpty()) {
                    featuresCache = new ArrayList<>(retrieveAllOfficalFeatures(monitor));
                }
            }
        }
        return featuresCache;
    }

    /**
     * All offical features from server, except third part features such as exchange.
     */
    private Collection<ExtraFeature> retrieveAllOfficalFeatures(IProgressMonitor monitor) throws Exception {
        Set<ExtraFeature> componentFeatures = new LinkedHashSet<>();

        // currently we only support component, change it when we want to support other types.
        getExtraFeatureFactory().retrieveAllComponentFeatures(monitor, componentFeatures);

        return new LinkedList<>(componentFeatures);
    }

    private Collection<ExtraFeature> getUpdates(IProgressMonitor monitor) throws Exception {
        List<ExtraFeature> updates = new LinkedList<>();
        List<ExtraFeature> features = getFeaturesCache(monitor);
        try {
            Collection<ExtraFeature> p2Updates = getP2Updates(monitor, features);
            if (p2Updates != null) {
                updates.addAll(p2Updates);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        try {
            Collection<ExtraFeature> tcompv1Updates = getTCompv1Updates(monitor, features);
            if (tcompv1Updates != null) {
                updates.addAll(tcompv1Updates);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        Collections.sort(updates);
        return updates;
    }

    private Collection<ExtraFeature> getP2Updates(IProgressMonitor monitor, Collection<ExtraFeature> features) throws Exception {
        Collection<ExtraFeature> p2Updates = new LinkedList<>();
        if (features != null && !features.isEmpty()) {
            Collection<IInstallableUnit> installedP2Features = P2Manager.getInstance().getInstalledP2Features(monitor);
            Map<String, IInstallableUnit> installedMap = new HashMap<>();
            for (IInstallableUnit iu : installedP2Features) {
                String key = iu.getId();
                if (installedMap.containsKey(key)) {
                    IInstallableUnit existedIU = installedMap.get(key);
                    Version existedVersion = null;
                    Version newVersion = null;
                    if (existedIU != null) {
                        existedVersion = existedIU.getVersion();
                    }
                    if (iu != null) {
                        newVersion = iu.getVersion();
                    }
                    ExceptionHandler.log(key + " has multiple versions: " + existedVersion + ", " + newVersion);
                }
                installedMap.put(key, iu);
            }
            for (ExtraFeature feature : features) {
                if (feature.getTypes().contains(Type.TCOMP_V1)) {
                    continue;
                }
                IInstallableUnit iu = installedMap.get(feature.getId());
                if (iu != null) {
                    Version iuVersion = iu.getVersion();
                    Version featVersion = Version.create(feature.getVersion());
                    if (0 < featVersion.compareTo(iuVersion)) {
                        p2Updates.add(feature);
                    }
                }
            }
        }
        return p2Updates;
    }

    private Collection<ExtraFeature> getTCompv1Updates(IProgressMonitor monitor, Collection<ExtraFeature> features)
            throws Exception {
        ITaCoKitUpdateService instance = ITaCoKitUpdateService.getInstance();
        if (instance != null) {
            return instance.filterUpdatableFeatures(features, monitor);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    public SearchResult searchUpdates(IProgressMonitor monitor, SearchOption searchOption) throws Exception {
        Collection<ExtraFeature> updates = null;
        // if (searchOption.isClearCache()) {
        // updatesCache.clear();
        // }
        updates = getUpdates(monitor);
        SearchResult result = new SearchResult(searchOption, updates);
        result.setTotalSize(updates.size());
        result.setPageSize(updates.size());
        result.setCurrentPage(0);
        return result;
    }

    public Map<ExtraFeature, IStatus> installUpdates(IProgressMonitor monitor) throws Exception {
        SearchOption option = new SearchOption(Type.ALL, Category.ALL, ""); //$NON-NLS-1$
        SearchResult searchResult = searchUpdates(monitor, option);
        // keep the order
        Map<ExtraFeature, IStatus> resultMap = new LinkedHashMap<>();
        if (searchResult != null) {
            Collection<ExtraFeature> updates = searchResult.getCurrentPageResult();
            if (updates != null && !updates.isEmpty()) {
                for (ExtraFeature update : updates) {
                    IStatus status = null;
                    try {
                        if (update.canBeInstalled(monitor)) {
                            status = update.install(monitor, new ArrayList<>());
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                    if (status == null) {
                        String name = ""; //$NON-NLS-1$
                        String version = ""; //$NON-NLS-1$
                        try {
                            name = update.getName();
                            version = update.getVersion();
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                        status = new Status(IStatus.ERROR, Constants.PLUGIN_ID,
                                Messages.getString("ComponentsManager.form.updates.notification.execute.status.failed", //$NON-NLS-1$
                                        name, version));
                    }
                    resultMap.put(update, status);
                }
            }
        }
        return resultMap;
    }

    public FeatureUpdateNotification createUpdateNotificationItem() {
        FeatureUpdateNotification update = new FeatureUpdateNotification();
        update.setTitle(Messages.getString("ComponentsManager.form.showUpdate.label.title")); //$NON-NLS-1$
        update.setDescription(Messages.getString("ComponentsManager.form.showUpdate.label.description")); //$NON-NLS-1$
        return update;
    }

    private ExtraFeaturesUpdatesFactory getExtraFeatureFactory() {
        return this.extraFeaturesFactory;
    }

    private TalendCustomThreadPoolExecutor createThreadPoolExecutor() {
        return new TalendCustomThreadPoolExecutor(60);
    }

    public ThreadPoolExecutor getSearchThreadPoolExecutor() {
        if (searchThreadPoolExecutor != null) {
            return searchThreadPoolExecutor;
        }
        synchronized (searchThreadPoolExecutorLock) {
            if (searchThreadPoolExecutor == null) {
                searchThreadPoolExecutor = createThreadPoolExecutor();
            }
        }
        return searchThreadPoolExecutor;
    }

    public ThreadPoolExecutor getUpdateThreadPoolExecutor() {
        if (updateThreadPoolExecutor != null) {
            return updateThreadPoolExecutor;
        }
        synchronized (updateThreadPoolExecutorLock) {
            if (updateThreadPoolExecutor == null) {
                updateThreadPoolExecutor = createThreadPoolExecutor();
            }
        }
        return updateThreadPoolExecutor;
    }

    public void clearAllThreadPool() {
        clearSearchThreadPool();
        clearUpdateThreadPool();
    }

    public void clearSearchThreadPool() {
        if (searchThreadPoolExecutor != null) {
            synchronized (searchThreadPoolExecutorLock) {
                if (searchThreadPoolExecutor != null) {
                    TalendCustomThreadPoolExecutor tmp = searchThreadPoolExecutor;
                    searchThreadPoolExecutor = null;
                    tmp.clearThreads();
                }
            }
        }
    }

    public void clearUpdateThreadPool() {
        if (updateThreadPoolExecutor != null) {
            synchronized (updateThreadPoolExecutorLock) {
                if (updateThreadPoolExecutor != null) {
                    TalendCustomThreadPoolExecutor tmp = updateThreadPoolExecutor;
                    updateThreadPoolExecutor = null;
                    tmp.clearThreads();
                }
            }
        }
    }

    public void clear() {
        clearAllThreadPool();
        synchronized (featuresCacheLock) {
            if (featuresCache != null) {
                featuresCache.clear();
            }
        }
        P2Manager.getInstance().clear();
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
