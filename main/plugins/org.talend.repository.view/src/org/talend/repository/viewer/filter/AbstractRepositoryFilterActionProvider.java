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

import org.talend.repository.viewer.action.AbstractRepositoryActionProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractRepositoryFilterActionProvider extends AbstractRepositoryActionProvider {

    public AbstractRepositoryFilterActionProvider() {
        super();
    }

    protected boolean isActivedFilter() {
        return RepositoryNodeFilterHelper.isActivedFilter();
    }

    protected void setActivedFilter(boolean activedFilter) {
        RepositoryNodeFilterHelper.setActivedFilter(activedFilter);
    }

    protected boolean isActivedPerspectiveFilter() {
        return PerspectiveFilterHelper.isActivedPerspectiveFilter();
    }

    protected void setActivedPerspectiveFilter(boolean activedPerspectiveFilter) {
        PerspectiveFilterHelper.setActivedPerspectiveFilter(activedPerspectiveFilter);
    }

}
