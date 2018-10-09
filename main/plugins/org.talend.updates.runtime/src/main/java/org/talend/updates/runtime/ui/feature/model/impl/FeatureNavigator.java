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
package org.talend.updates.runtime.ui.feature.model.impl;

import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.ui.feature.model.IFeatureNavigator;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureNavigator extends AbstractFeatureItem implements IFeatureNavigator {

    private SearchResult searchResult;

    private INavigatorCallBack navigatorCallBack;

    public FeatureNavigator(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public INavigatorCallBack getNavigatorCallBack() {
        return navigatorCallBack;
    }

    public void setNavigatorCallBack(INavigatorCallBack callBack) {
        this.navigatorCallBack = callBack;
    }

}
