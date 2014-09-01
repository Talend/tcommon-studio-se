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

import org.eclipse.gef.commands.Command;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.ui.NatEventData;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.action.ContextBuiltinToRepositoryAction;
import org.talend.core.ui.context.model.table.ContextTableTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;
import org.talend.core.ui.i18n.Messages;

/**
 * created by ldong on Aug 14, 2014 Detailled comment
 * 
 */
public class ContextMenuItemProviders extends MenuItemProviders {

    // the item of change the context parameter's mode such as from repositroy to build-in
    public static IMenuItemProvider changeModeMenuItemProvider(final IDataProvider dataProvider) {
        return new IMenuItemProvider() {

            @Override
            public void addMenuItem(final NatTable natTable, Menu popupMenu) {
                final MenuItem changeModeMenuItem = new MenuItem(popupMenu, SWT.PUSH);
                changeModeMenuItem.setText(Messages.getString("ContextTreeTable.AddToBuildIn_label")); //$NON-NLS-1$
                changeModeMenuItem.setEnabled(false);

                changeModeMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        NatEventData natEventData = getNatEventData(e);
                        NatTable nt = natEventData.getNatTable();
                        int rowPosition = natEventData.getRowPosition();
                        int rowIndex = nt.getRowIndexByPosition(rowPosition);
                        @SuppressWarnings("unchecked")
                        ContextTreeNode treeNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider)
                                .getRowObject(rowIndex);
                        IContextModelManager manager = treeNode.getManager();
                        if (treeNode.getTreeData() instanceof ContextTableTabChildModel) {
                            ContextTableTabChildModel childModel = (ContextTableTabChildModel) treeNode.getTreeData();
                            String sourceId = childModel.getSourceId();
                            if (manager.getContextManager() != null) {
                                if (!sourceId.equals(IContextParameter.BUILT_IN)) {
                                    Command cmd = new ContextParaChangeModeCommand(manager, treeNode, childModel
                                            .getContextParameter());
                                    if (manager.getCommandStack() == null) {
                                        cmd.execute();
                                    } else {
                                        manager.getCommandStack().execute(cmd);
                                    }
                                }
                            }
                        }
                    }
                });

                final MenuItem changeModeMenuItem1 = new MenuItem(popupMenu, SWT.PUSH);
                changeModeMenuItem1.setText(Messages.getString("ContextTreeTable.AddToRepository_label")); //$NON-NLS-1$
                changeModeMenuItem1.setEnabled(false);

                changeModeMenuItem1.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        NatEventData natEventData = getNatEventData(e);
                        NatTable nt = natEventData.getNatTable();
                        int rowPosition = natEventData.getRowPosition();
                        int rowIndex = nt.getRowIndexByPosition(rowPosition);
                        @SuppressWarnings("unchecked")
                        ContextTreeNode treeNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider)
                                .getRowObject(rowIndex);
                        IContextModelManager manager = treeNode.getManager();
                        if (treeNode.getTreeData() instanceof ContextTableTabParentModel) {
                            ContextTableTabParentModel paraModel = (ContextTableTabParentModel) treeNode.getTreeData();
                            String sourceId = paraModel.getSourceId();
                            if (sourceId.equals(IContextParameter.BUILT_IN)) {
                                ContextBuiltinToRepositoryAction action = new ContextBuiltinToRepositoryAction(manager);
                                action.init(natTable, treeNode.getTreeData());
                                action.run();
                            }
                        }
                    }
                });
            }
        };
    }
}
