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
package org.talend.metadata.managment.ui.dialog;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ResetDBTypesPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class CustomTableManagerOnlyForGenericSchema {

    public static final Color CUSTOM_CELL_BG_COLOR = new Color(null, new RGB(0, 0xF0, 0));

    public static final Color CELL_READ_ONLY_COLOR = new Color(null, new RGB(0, 0xB0, 0));

    public static final Color TABLE_READ_ONLY_COLOR = new Color(null, new RGB(185, 185, 185));

    public static final Color CELL_WRONG_DB_TYPE_COLOR = new Color(null, new RGB(0xFF, 0x80, 0));

    public static void addCustomManagementToTable(final MetadataEmfTableEditorView tableEditorView, final boolean readOnly) {
        CustomTableLabelProvider tableProvider = new CustomTableLabelProvider(tableEditorView, readOnly);
        tableEditorView.getTableViewerCreator().setLabelProvider(tableProvider);
        tableEditorView.getTableViewerCreator().init(tableEditorView.getTableViewerCreator().getInputList());
        tableEditorView.getTableViewerCreator().setCellModifier(
                new CustomTableCellModifier(tableEditorView.getTableViewerCreator()));
    }

    public static void addCustomManagementToToolBar(final MetadataEmfTableEditorView tableEditorView, final MetadataTable table,
            final boolean readOnly, final MetadataEmfTableEditorView linkedTableEditorView, final MetadataTable linkedTable,
            final boolean toPropagate) {
        tableEditorView.getTableViewerCreator().getTableViewer().addPostSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateToolBarButtonsOnSelection(event.getSelection(), tableEditorView, table, linkedTableEditorView, linkedTable,
                        readOnly);
            }

        });
        boolean isThereCustom = false;
        for (IMetadataColumn column : table.getListColumns()) {
            if (column.isCustom()) {
                isThereCustom = true;
            }
        }
        if (isThereCustom) {
            tableEditorView.getExtendedToolbar().getImportButton().getButton().setEnabled(false);
        }
        if (table.isReadOnly()) {
            tableEditorView.getExtendedToolbar().getAddButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getMoveDownButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getMoveUpButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getRemoveButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getPasteButton().getButton().setEnabled(false);
            ResetDBTypesPushButton resetDBTypesButton = tableEditorView.getExtendedToolbar().getResetDBTypesButton();
            if (resetDBTypesButton != null) {
                resetDBTypesButton.getButton().setEnabled(false);
            }
        } else {
            tableEditorView.getExtendedToolbar().getAddButton().getButton().addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    table.sortCustomColumns();
                    tableEditorView.getTableViewerCreator().getTableViewer().refresh();
                }

            });
        }
        SelectionListener customListener = new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                updateToolBarButtonsOnSelection(tableEditorView.getTableViewerCreator().getTableViewer().getSelection(),
                        tableEditorView, table, linkedTableEditorView, linkedTable, readOnly);
            }

        };
        tableEditorView.getExtendedToolbar().getRemoveButton().getButton().addSelectionListener(customListener);
        tableEditorView.getExtendedToolbar().getCopyButton().getButton().addSelectionListener(customListener);

        if (toPropagate) {
            if (linkedTable.isReadOnly()) {
                SelectionListener updateLinkedTableListener = new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {
                        MetadataToolHelper.copyTable(table, linkedTable);
                        linkedTableEditorView.getTableViewerCreator().getTableViewer().refresh();
                    }

                };
                tableEditorView.getExtendedToolbar().getButtons();
                for (Iterator iter = tableEditorView.getExtendedToolbar().getButtons().iterator(); iter.hasNext();) {
                    ExtendedPushButton element = (ExtendedPushButton) iter.next();
                    element.getButton().addSelectionListener(updateLinkedTableListener);
                }
            }
        }
    }

    private static void updateToolBarButtonsOnSelection(ISelection currentSelection, MetadataEmfTableEditorView tableEditorView,
            IMetadataTable table, final MetadataEmfTableEditorView linkedTableEditorView, final IMetadataTable linkedTable,
            boolean readOnly) {
        IStructuredSelection selection = (IStructuredSelection) currentSelection;

        boolean isThereCustom = false;
        boolean isThereReadOnly = false;
        for (Iterator iter = selection.iterator(); iter.hasNext();) {
            IMetadataColumn column = (IMetadataColumn) iter.next();
            if (column.isCustom()) {
                isThereCustom = true;
            }
            if (column.isReadOnly()) {
                isThereReadOnly = true;
            }
        }

        if (isThereReadOnly) {
            tableEditorView.getExtendedToolbar().getRemoveButton().getButton().setEnabled(false);
        }

        if (isThereCustom || table.isReadOnly()) {

            tableEditorView.getExtendedToolbar().getMoveDownButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getMoveUpButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getRemoveButton().getButton().setEnabled(false);
            tableEditorView.getExtendedToolbar().getPasteButton().getButton().setEnabled(false);
            ResetDBTypesPushButton resetDBTypesButton = tableEditorView.getExtendedToolbar().getResetDBTypesButton();
            if (resetDBTypesButton != null) {
                resetDBTypesButton.getButton().setEnabled(false);
            }
        }
        if (linkedTable != null) {
            if (linkedTable.isReadOnly()) {
                linkedTableEditorView.getExtendedToolbar().getPasteButton().getButton().setEnabled(false);
            }
        }

    }

    /**
     * DOC nrousseau CustomTableManager class global comment. Detailled comment <br/>
     * 
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private static class CustomTableLabelProvider extends DefaultTableLabelProvider {

        private boolean readOnly = false;

        MetadataEmfTableEditorView tableEditorView;

        public CustomTableLabelProvider(final MetadataEmfTableEditorView tableEditorView, final boolean readOnly) {
            super(tableEditorView.getTableViewerCreator());
            this.readOnly = readOnly;
            this.tableEditorView = tableEditorView;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.commons.ui.swt.tableviewer.behavior.DefaultTableLabelProvider#getBackground(java.lang.Object,
         * int)
         */
        @Override
        public Color getBackground(Object element, int columnIndex) {
            if (!(element instanceof MetadataColumn)) {
                return null;
            }
            MetadataColumn column = (MetadataColumn) element;
            TableViewerCreatorColumnNotModifiable tableColumn = (TableViewerCreatorColumnNotModifiable) tableViewerCreator
                    .getColumns().get(columnIndex);
            if (column.isReadOnly()) {
                return TABLE_READ_ONLY_COLOR;
            }
            // qli modified to fix the bug 6654.
            if (tableEditorView.isShowTalendTypeColumn() && tableEditorView.getCurrentDbms() != null) {
                if (tableColumn.getId().equals(AbstractMetadataTableEditorView.ID_COLUMN_DBTYPE)
                        && !"".equals(column.getSourceType()) //$NON-NLS-1$
                        && !TypesManager.checkDBType(tableEditorView.getCurrentDbms(), column.getTalendType(),
                                column.getSourceType())) {
                    return CELL_WRONG_DB_TYPE_COLOR;
                }
            }
            return super.getBackground(element, columnIndex);
        }
    }

    /**
     * DOC nrousseau class global comment. Detailled comment <br/>
     * 
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private static class CustomTableCellModifier extends DefaultCellModifier {

        public CustomTableCellModifier(TableViewerCreator tableViewerCreator) {
            super(tableViewerCreator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier#canModify(java.lang.Object,
         * java.lang.String)
         */
        @Override
        public boolean canModify(Object element, String property) {
            if (element instanceof IMetadataColumn) {
                IMetadataColumn column = (IMetadataColumn) element;
                if (column.isReadOnly() || (column.isCustom() && property.equals(AbstractMetadataTableEditorView.ID_COLUMN_NAME))) {
                    return false;
                }
            }
            return super.canModify(element, property);
        }
    }

}
