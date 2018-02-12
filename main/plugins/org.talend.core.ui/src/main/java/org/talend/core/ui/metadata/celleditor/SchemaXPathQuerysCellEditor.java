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
package org.talend.core.ui.metadata.celleditor;

import java.util.Map;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.INode;
import org.talend.core.ui.metadata.celleditor.xpathquery.SchemaXPathQuerysDialog;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SchemaXPathQuerysCellEditor extends DialogCellEditor {

    private INode node;

    private AbstractDataTableEditorView tableEditorView;

    private Map mapping;

    public SchemaXPathQuerysCellEditor(Composite parent, INode node) {
        super(parent, SWT.NONE);
        this.node = node;
    }

    public AbstractDataTableEditorView getTableEditorView() {
        return this.tableEditorView;
    }

    private TableViewer getTableViewer() {
        if (this.tableEditorView != null) {
            AbstractExtendedTableViewer extendedTableViewer = this.tableEditorView.getExtendedTableViewer();
            if (extendedTableViewer != null) {
                TableViewerCreator tableViewerCreator = extendedTableViewer.getTableViewerCreator();
                if (tableViewerCreator != null) {
                    return tableViewerCreator.getTableViewer();
                }
            }
        }
        return null;
    }

    public void setTableEditorView(AbstractDataTableEditorView tableEditorView) {
        this.tableEditorView = tableEditorView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        // itemDisplayCodename
        Object firstElement = ((StructuredSelection) this.getTableViewer().getSelection()).getFirstElement();
        mapping = ((Map) firstElement);

        String[] listItemsDisplayCodeName = node.getElementParameter("SCHEMAS").getListItemsDisplayCodeName(); //$NON-NLS-1$
        Object object = ((Map) firstElement).get(listItemsDisplayCodeName[0]);
        IMetadataTable curMetadataTable = findMetadataTable(node, object.toString());

        if (object != null && !"".equals(object.toString()) && curMetadataTable != null) { //$NON-NLS-1$
            Shell activeShell = Display.getCurrent().getActiveShell();
            SchemaXPathQuerysDialog schemaXPathQuerysDialog = new SchemaXPathQuerysDialog(activeShell, node, curMetadataTable,
                    mapping);

            if (schemaXPathQuerysDialog.open() == Window.OK) {
                getTableViewer().refresh();
                return schemaXPathQuerysDialog.getValues();
            }
        }
        return null;
    }

    public static IMetadataTable findMetadataTable(INode node, String tableName) {
        for (IMetadataTable table : node.getMetadataList()) {
            // need check the compare for table name or label, seems the schema cell modification is used by label.
            String tabRow = table.getTableName();
            if (tabRow != null) {
                if (tabRow.equals(tableName)) {
                    return table;
                } else {
                    String tabRowLabel = table.getLabel();
                    if (tabRowLabel != null) {
                        if (tabRowLabel.equals(tableName)) {
                            return table;
                        }
                    }

                }
            }
        }
        return null;
    }
}
