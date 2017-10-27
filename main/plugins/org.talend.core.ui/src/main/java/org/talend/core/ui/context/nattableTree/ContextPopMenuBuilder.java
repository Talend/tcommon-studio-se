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
package org.talend.core.ui.context.nattableTree;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;

/**
 * created by ldong on Aug 14, 2014 Detailled comment
 * 
 */
public class ContextPopMenuBuilder extends PopupMenuBuilder {

    /**
     * DOC ldong ContextPopMenuBuilder constructor comment.
     * 
     * @param parent
     */
    public ContextPopMenuBuilder(NatTable parent) {
        super(parent);
    }

    public PopupMenuBuilder withChangeModeMenuItem(IDataProvider dataProvider, ISelectionProvider selection) {
        return withMenuItemProvider(ContextMenuItemProviders.changeModeMenuItemProvider(dataProvider, selection));
    }

}
