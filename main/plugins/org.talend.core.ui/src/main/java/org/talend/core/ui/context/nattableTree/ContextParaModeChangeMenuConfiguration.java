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
package org.talend.core.ui.context.nattableTree;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;
import org.talend.core.ui.i18n.Messages;

/**
 * created by ldong on Aug 14, 2014 Detailled comment
 * 
 */
public class ContextParaModeChangeMenuConfiguration extends AbstractUiBindingConfiguration {

    private final Menu modeMenu;

    public ContextParaModeChangeMenuConfiguration(final NatTable natTable, final IDataProvider dataProvider,
            final ISelectionProvider selection) {
        modeMenu = new ContextPopMenuBuilder(natTable).withChangeModeMenuItem(dataProvider, selection).build();

        modeMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuHidden(MenuEvent e) {

            }

            @Override
            public void menuShown(MenuEvent e) {
                // control the two menu item enable or disable for different rows when right click the menu
                Menu menu = (Menu) e.getSource();
                NatEventData natEventData = (NatEventData) menu.getData();
                NatTable nt = natEventData.getNatTable();
                int rowPosition = natEventData.getRowPosition();
                int rowIndex = nt.getRowIndexByPosition(rowPosition);
                @SuppressWarnings("unchecked")
                ContextTreeNode treeNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(rowIndex);
                if (treeNode.getTreeData() instanceof ContextTableTabParentModel) {
                    ContextTableTabParentModel parentModel = (ContextTableTabParentModel) treeNode.getTreeData();
                    if (parentModel.hasChildren()) {
                        for (MenuItem item : menu.getItems()) {
                            item.setEnabled(false);
                        }
                    } else {
                        for (MenuItem item : menu.getItems()) {
                            if (item.getText().equals(Messages.getString("ContextTreeTable.AddToRepository_label"))) {
                                item.setEnabled(true);
                            } else {
                                item.setEnabled(false);
                            }
                        }
                    }
                } else {
                    for (MenuItem item : menu.getItems()) {
                        if (item.getText().equals(Messages.getString("ContextTreeTable.AddToBuildIn_label"))) {
                            item.setEnabled(true);
                        } else {
                            item.setEnabled(false);
                        }
                    }
                }
            }
        });

        natTable.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                modeMenu.dispose();
            }
        });
    }

    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        // add a mouse binding to pop up "add to build-in" menu item
        uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, null, 3), new PopupMenuAction(modeMenu));
    }

}
