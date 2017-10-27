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
package org.talend.core.ui.metadata.editor;

import org.eclipse.jface.viewers.TextCellEditor;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.core.ui.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public class InegerCellEditorListener extends DialogErrorForCellEditorListener {

    public InegerCellEditorListener(TextCellEditor cellEditor, TableViewerCreatorColumn column) {
        super(cellEditor, column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener#newValidValueTyped(int,
     * java.lang.Object, java.lang.Object, org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE)
     */
    @Override
    public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener#validateValue(java.lang.String,
     * int)
     */
    @Override
    public String validateValue(String value, int beanPosition) {
        final String empty = ""; //$NON-NLS-1$
        if (value != null && !empty.equals(value)) {
            String title = empty;
            if (column != null) {
                title = column.getTitle();
            }
            try {
                int i = Integer.parseInt(value);
                if (i < 0) {
                    return Messages.getString("InegerCellEditorListener.NegativeNumberMessage", title); //$NON-NLS-1$
                }
            } catch (NumberFormatException e) {
                return Messages.getString("InegerCellEditorListener.NumeralMessage", title); //$NON-NLS-1$
            }
        }
        return null;
    }

}
