// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class DirectoryCellEditor extends DialogCellEditor {

    public DirectoryCellEditor(Composite parent) {
        super(parent);
    }

    public DirectoryCellEditor(Composite parent, int style) {
        super(parent, style);
    }

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
