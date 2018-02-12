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

import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.talend.repository.i18n.Messages;

/**
 * Provide a menu to enable perspective filtering, that is according to extension point
 * org.talend.cnf.perspective.filter definition the CNF view associated with this provider will see it's content
 * filtered.
 */
public class PerspectiveFilterActionProvider extends AbstractRepositoryFilterActionProvider {

    /**
     * maybe, not good for fixed id
     */
    public static final String ID = "org.talend.repository.viewer.actionbar.menu.perspectiveFilter"; //$NON-NLS-1$

    private PerspectiveFilterAction perspectiveFilterAction = new PerspectiveFilterAction(this,
            Messages.getString("PerspectiveFilterActionProvider.PerspectiveContentFilter"));

    public PerspectiveFilterActionProvider() {
    }

    @Override
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);

        perspectiveFilterAction.setChecked(isActivedPerspectiveFilter());
    }

    /**
     * set the filtering state of the viewer and update the content of the viewer accordingly.
     * 
     * @param filtering, if true filtering is active (false make filtering unactive)
     */
    public void setFiltering(boolean activedPerspectiveFilter, boolean restoring) {
        if (activedPerspectiveFilter != isActivedPerspectiveFilter()) {
            setActivedPerspectiveFilter(activedPerspectiveFilter);
            RepositoryNodeFilterHelper.filter(getCommonViewer(), isActivedFilter(), activedPerspectiveFilter);

        }
    }

}
