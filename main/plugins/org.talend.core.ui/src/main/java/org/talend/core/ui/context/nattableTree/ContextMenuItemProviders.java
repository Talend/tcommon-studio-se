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

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.talend.core.ui.i18n.Messages;

/**
 * created by ldong on Aug 14, 2014 Detailled comment
 * 
 */
public class ContextMenuItemProviders extends MenuItemProviders {

    // the item of change the context parameter's mode such as from repositroy to build-in
    public static IMenuItemProvider changeModeMenuItemProvider(final IDataProvider dataProvider,
            final ISelectionProvider selection) {
        return new IMenuItemProvider() {

            @Override
            public void addMenuItem(final NatTable natTable, Menu popupMenu) {
                final MenuItem changeModeMenuItem = new MenuItem(popupMenu, SWT.PUSH);
                changeModeMenuItem.setText(Messages.getString("ContextTreeTable.AddToBuildIn_label")); //$NON-NLS-1$
                changeModeMenuItem.setEnabled(false);

                changeModeMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        IStructuredSelection selectRows = (IStructuredSelection) selection.getSelection();
                        if (selectRows.isEmpty()) {
                            ContextTreeNode treeNode = getTreeNodeByRightClickPostion(dataProvider, e);
                            handleRepositoryToBuiltIn(treeNode);
                        } else {
                            Object[] obj = getTreeNodesByMultipleSelection(selectRows);
                            for (Object object : obj) {
                                if (object == null) {
                                    return;
                                }
                                ContextTreeNode treeNode = (ContextTreeNode) object;
                                handleRepositoryToBuiltIn(treeNode);
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
                        IStructuredSelection selectRows = (IStructuredSelection) selection.getSelection();
                        if (selectRows.isEmpty()) {
                            ContextTreeNode treeNode = getTreeNodeByRightClickPostion(dataProvider, e);
                            Object[] obj = new Object[1];
                            obj[0] = treeNode;
                            handleBuiltInToRepository(natTable, obj);
                        } else {
                            Object[] obj = getTreeNodesByMultipleSelection(selectRows);
                            handleBuiltInToRepository(natTable, obj);
                        }
                    }
                });
            }
        };
    }

    private static void handleBuiltInToRepository(final NatTable natTable, Object[] realTreeNodes) {
        IContextModelManager modelManager = ((ContextTreeNode) realTreeNodes[0]).getManager();
        ContextBuiltinToRepositoryAction action = new ContextBuiltinToRepositoryAction(modelManager);
        action.init(natTable, realTreeNodes);
        action.run();
    }

    private static void handleRepositoryToBuiltIn(ContextTreeNode treeNode) {
        IContextModelManager manager = treeNode.getManager();
        Object rowData = treeNode.getTreeData();
        if (rowData instanceof ContextTableTabChildModel) {
            ContextTableTabChildModel childModel = (ContextTableTabChildModel) rowData;
            String sourceId = childModel.getSourceId();
            if (manager.getContextManager() != null) {
                if (!sourceId.equals(IContextParameter.BUILT_IN)) {
                    Command cmd = new ContextParaChangeModeCommand(manager, treeNode, childModel.getContextParameter());
                    if (manager.getCommandStack() == null) {
                        cmd.execute();
                    } else {
                        manager.getCommandStack().execute(cmd);
                    }
                }
            }
        }
    }

    private static ContextTreeNode getTreeNodeByRightClickPostion(final IDataProvider dataProvider, SelectionEvent e) {
        NatEventData natEventData = getNatEventData(e);
        NatTable nt = natEventData.getNatTable();
        int rowPosition = natEventData.getRowPosition();
        int rowIndex = nt.getRowIndexByPosition(rowPosition);
        @SuppressWarnings("unchecked")
        ContextTreeNode treeNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(rowIndex);
        return treeNode;
    }

    private static Object[] getTreeNodesByMultipleSelection(IStructuredSelection sels) {
        Object[] obj = new Object[sels.toList().size()];
        int i = 0;
        for (Object node : sels.toList().toArray()) {
            if (node instanceof ContextTreeNode) {
                obj[i++] = node;
            }

        }
        return obj;
    }
}
