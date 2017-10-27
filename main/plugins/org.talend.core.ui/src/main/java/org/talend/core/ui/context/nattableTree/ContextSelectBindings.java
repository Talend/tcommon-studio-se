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

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.action.CellEditDragMode;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellEditorMouseEventMatcher;
import org.eclipse.swt.events.MouseEvent;

/**
 * created by ldong on Jul 23, 2014 Detailled comment
 * 
 */
public class ContextSelectBindings extends DefaultEditBindings {

    private IDataProvider dataProvider;

    public ContextSelectBindings(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void configureUiBindings(UiBindingRegistry pUiBindingRegistry) {
        CellEditorMouseEventMatcher mouseEventMatcher = new CellEditorMouseEventMatcher() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.eclipse.nebula.widgets.nattable.ui.matcher.CellEditorMouseEventMatcher#matches(org.eclipse.nebula
             * .widgets.nattable.NatTable, org.eclipse.swt.events.MouseEvent,
             * org.eclipse.nebula.widgets.nattable.layer.LabelStack)
             */
            @Override
            public boolean matches(NatTable natTable, MouseEvent event, LabelStack regionLabels) {
                return false;
            }

        };
        pUiBindingRegistry.registerSingleClickBinding(mouseEventMatcher, new MouseEditAction());
        pUiBindingRegistry.registerFirstMouseDragMode(mouseEventMatcher, new CellEditDragMode());
    }
}
