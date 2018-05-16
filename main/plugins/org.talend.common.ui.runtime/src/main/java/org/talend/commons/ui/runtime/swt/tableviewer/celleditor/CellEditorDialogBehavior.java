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
package org.talend.commons.ui.runtime.swt.tableviewer.celleditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.runtime.expressionbuilder.ICellEditorDialog;
import org.talend.commons.ui.runtime.expressionbuilder.IExtendedCellEditorBehavior;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.ws.WindowSystem;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: CellEditorDialogBehavior.java 上午10:08:35 2007-8-16 +0000 (2007-8-16) yzhang $
 * 
 */
public class CellEditorDialogBehavior implements IExtendedCellEditorBehavior {

    private ExtendedTextCellEditor extendedTextCellEditor;

    private ICellEditorDialog cellEditorDialog;

    /**
     * Sets the cellEditorDialog.
     * 
     * @param cellEditorDialog the cellEditorDialog to set
     */
    public void setCellEditorDialog(ICellEditorDialog cellEditorDialog) {
        this.cellEditorDialog = cellEditorDialog;
    }

    /**
     * yzhang CellEditorDialogBehavior constructor comment.
     */
    public CellEditorDialogBehavior() {
        super();
    }

    private Composite panel;

    public ExtendedTextCellEditor getExtendedTextCellEditor() {
        return extendedTextCellEditor;
    }

    public void setExtendedTextCellEditor(ExtendedTextCellEditor extendedTextCellEditor) {
        this.extendedTextCellEditor = extendedTextCellEditor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.rowgenerator.ui.tabs.IExtendedCellEditorBehavior#createBehaviorControls(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public Control createBehaviorControls(Composite parent) {

        panel = new Composite(parent, SWT.NONE);

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 2;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        panel.setLayout(gridLayout);

        GridData gd = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        panel.setLayoutData(gd);

        GridData controlGD = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        // Set a default height for the text control on OS X
        if (WindowSystem.isOSX()) {
            controlGD.heightHint = 14;
        }
        Control text = extendedTextCellEditor.createText(panel);
        text.setLayoutData(controlGD);

        GridData buttonGD = new GridData();
        buttonGD.heightHint = panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        Button button = new Button(panel, SWT.NONE);
        button.setLayoutData(buttonGD);
        button.setText(Messages.getString("CellEditorDialogBehavior.textContent")); //$NON-NLS-1$

        button.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                if (cellEditorDialog != null) {

                    cellEditorDialog.openDialog(extendedTextCellEditor);

                }
            }
        });

        return panel;

    }

}
