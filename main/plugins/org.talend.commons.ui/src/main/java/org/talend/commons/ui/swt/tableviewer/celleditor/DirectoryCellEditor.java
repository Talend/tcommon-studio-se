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
package org.talend.commons.ui.swt.tableviewer.celleditor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * DOC qiang.zhang class global comment. Detailled comment <br/>
 * 
 */
public class DirectoryCellEditor extends DialogCellEditor {

    /**
     * DOC qiang.zhang DirectoryCellEditor constructor comment.
     * 
     * @param parent
     */
    public DirectoryCellEditor(Composite parent) {
        super(parent);
    }

    /**
     * DOC qiang.zhang DirectoryCellEditor constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DirectoryCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        DirectoryDialog dialog = new DirectoryDialog(cellEditorWindow.getShell());
        String path = dialog.open();
        if (path != null) {
            path = path.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return path;
    }

}
