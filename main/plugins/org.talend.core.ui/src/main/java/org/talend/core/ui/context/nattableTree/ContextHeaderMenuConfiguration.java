// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Menu;

/**
 * created by ldong on Jul 21, 2014 Detailled comment
 * 
 */
public class ContextHeaderMenuConfiguration extends HeaderMenuConfiguration {

    private final Menu modeMenu;

    private final ContextPopMenuBuilder contextMenuBuilder;

    /**
     * DOC Talend ContextHeaderMenuConfiguration constructor comment.
     * 
     * @param natTable
     */
    public ContextHeaderMenuConfiguration(NatTable natTable, IDataProvider dataProvider) {
        super(natTable);
        contextMenuBuilder = new ContextPopMenuBuilder(natTable);
        modeMenu = contextMenuBuilder.withChangeModeMenuItem(dataProvider).build();

        natTable.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                modeMenu.dispose();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration#createColumnHeaderMenu(org.eclipse.nebula
     * .widgets.nattable.NatTable)
     */
    @Override
    protected PopupMenuBuilder createRowHeaderMenu(NatTable natTable) {
        return contextMenuBuilder;
    }

}
