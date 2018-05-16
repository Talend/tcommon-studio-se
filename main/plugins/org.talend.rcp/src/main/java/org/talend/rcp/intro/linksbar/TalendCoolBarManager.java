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
package org.talend.rcp.intro.linksbar;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.provisional.action.CoolBarManager2;
import org.eclipse.swt.SWT;

/**
 * created by nrousseau on May 15, 2013 Detailled comment
 * 
 */
@Deprecated
public class TalendCoolBarManager extends CoolBarManager2 {

    public TalendCoolBarManager(int style) {
        super(style);

        IToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        toolBarManager.add(new LinksToolbarItem());
        add(new ToolBarContributionItem(toolBarManager, TalendActionBarPresentationFactory.COOLITEM_LINKS_ID));
    }

    @Override
    public void refresh() {
        IContributionItem linksCoolItem = find(TalendActionBarPresentationFactory.COOLITEM_LINKS_ID);
        int index = ArrayUtils.indexOf(getItems(), linksCoolItem);
        if (index != (getItems().length - 1)) {
            // if the coolbar is not the latest, just force to move to last one again.
            // don't deal with the case index = 0 and only have this one, since it's impossible !
            if (linksCoolItem != null) {
                remove(linksCoolItem);
                add(linksCoolItem);
            }
        }

        super.refresh();
    }
}
