// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public abstract class QuotePushButtonForExtendedTable extends QuotePushButton implements IExtendedTablePushButton {

    private String quote = null;
    private boolean isAddingQuote = true;
    private EnableStateListenerForTableButton enableStateHandler;

    /**
     * DOC SchemaTargetAddPushButton constructor comment.
     *
     * @param parent
     * @param extendedControlViewer
     */
    public QuotePushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#beforeCommandExecution()
     */
    @Override
    protected void beforeCommandExecution() {
        QuoteManipulateDialog dlg = new QuoteManipulateDialog(getButton().getShell());
        if (dlg.open() == Window.OK) {
            this.quote = dlg.getQuote();
            this.isAddingQuote = dlg.isAddingQuote();
        }
    }

    protected Command getCommandToExecute() {
        if (quote == null) return null;
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        TableViewer tableViewer = extendedTableViewer.getTableViewerCreator().getTableViewer();
        ISelection selection = tableViewer.getSelection();
        StructuredSelection structuredSelection = (StructuredSelection) selection;
        Object[] objects = structuredSelection.toArray();
        int[] selectionIndices = extendedTableViewer.getTableViewerCreator().getTable().getSelectionIndices();
        return getCommandToExecute(extendedTableModel, Arrays.asList(objects), selectionIndices, quote, isAddingQuote );
    }

    protected abstract Command getCommandToExecute(ExtendedTableModel extendedTable, List beansToUppercase, int[] selectionIndices, String quote, boolean isAddingQuote);

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ui.extended.button.IExtendedTablePushButton#getExtendedTableViewer()
     */
    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }
    
    @Override
    public boolean getEnabledState() {
        return super.getEnabledState() && this.enableStateHandler.getEnabledState();
    }
    
    class QuoteManipulateDialog extends Dialog {
        
        private Button addBtn;
        private Button removeBtn;
        private Text quoteTxt;
        private Button okBtn;
        boolean isAddingQuote = true;
        String quote = "";

        public QuoteManipulateDialog(Shell parentShel) {
            super(parentShel);
        }
        
        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
         */
        protected void configureShell(Shell shell) {
            super.configureShell(shell);
            shell.setText(Messages.getString("QuoteManipulateDialog.title"));
        }
        
        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
         */
        protected void createButtonsForButtonBar(Composite parent) {
            // create OK and Cancel buttons by default
            okBtn = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
            createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
            okBtn.setEnabled(false);
        }
        
        /*
         * (non-Javadoc) Method declared on Dialog.
         */
        protected Control createDialogArea(Composite parent) {
            Composite composite = new Composite(parent, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.numColumns = 4;
            composite.setLayout(layout);
            GridData layoutData = new GridData(GridData.FILL_BOTH);
            layoutData.widthHint = 400;
            composite.setLayoutData(layoutData);
            addBtn = new Button(composite, SWT.RADIO);
            addBtn.setSelection(true);
            Label label = new Label(composite, SWT.WRAP);
            label.setText(Messages.getString("QuoteManipulateDialog.addQuote")); //$NON-NLS-1$
            
            removeBtn = new Button(composite, SWT.RADIO);
            Label label1 = new Label(composite, SWT.WRAP);
            label1.setText(Messages.getString("QuoteManipulateDialog.removeQuote")); //$NON-NLS-1$
            
            quoteTxt = new Text(composite, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            layoutData.horizontalSpan = 4;
            quoteTxt.setLayoutData(layoutData);
            quoteTxt.setText("");
            quoteTxt.setFocus();
            
            addBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    isAddingQuote = true;
                    quoteTxt.setFocus();
                    quote = quoteTxt.getText();
                }
            });
            
            removeBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    isAddingQuote = false;
                    quoteTxt.setFocus();
                    quote = quoteTxt.getText();
                }
            });
            
            quoteTxt.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    quote = quoteTxt.getText();
                    if ( quote != null && quote.length() > 0) {
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setEnabled(false);
                    }
                }
            });
            
            
            return composite;
        }
        
        public boolean isAddingQuote() {
            return this.isAddingQuote;
        }
        
        public String getQuote() {
            return this.quote;
        }
    }
}
