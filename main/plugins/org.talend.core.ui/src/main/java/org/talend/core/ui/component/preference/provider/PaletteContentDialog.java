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
package org.talend.core.ui.component.preference.provider;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.ui.component.preference.ComponentsFormatPreferencePage;
import org.talend.core.ui.i18n.Messages;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class PaletteContentDialog extends Dialog {

    private static final String DIALOG_TITLE = "Select the items in palette"; //$NON-NLS-1$

    private TreeViewer viewer;

    private PaletteRoot paletteRoot;

    private final ComponentsFormatPreferencePage page;

    /**
     * DOC yzhang PaletteContentDialog constructor comment.
     * 
     * @param parentShell
     */
    public PaletteContentDialog(Shell parentShell, ComponentsFormatPreferencePage page) {
        super(parentShell);
        setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
        this.page = page;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(DIALOG_TITLE);
        newShell.setSize(400, 600);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        addTreeViewer(composite);
        // addFileds(composite);
        return composite;
    }

    /**
     * yzhang Comment method "addViewer".
     * 
     * @param parent
     */
    private void addTreeViewer(Composite parent) {
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(new TalendPaletteTreeProvider());
        viewer.setLabelProvider(new TalendPaletteLabelProvider());
        viewer.setInput(page.getPaletteRoot());
        // qli modified to fix the bug 7074.
        viewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof PaletteSeparator) {
                    return false;
                }
                return true;
            }
        } });
        viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /**
     * yzhang Comment method "addFileds".
     * 
     * @param parent
     */
    private void addFileds(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelHint = new Label(panel, SWT.NONE);
        labelHint.setText(Messages.getString("PaletteContentDialog.hint")); //$NON-NLS-1$
        Text textHint = new Text(panel, SWT.BORDER);
        textHint.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelLabel = new Label(panel, SWT.NONE);
        labelLabel.setText(Messages.getString("PaletteContentDialog.lable")); //$NON-NLS-1$
        Text textLabel = new Text(panel, SWT.BORDER);
        textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelConnection = new Label(panel, SWT.NONE);
        labelConnection.setText(Messages.getString("PaletteContentDialog.connection")); //$NON-NLS-1$
        Text textConnection = new Text(panel, SWT.BORDER);
        textConnection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    }

    /**
     * yzhang Comment method "getViewer".
     * 
     * @return
     */
    public TreeViewer getViewer() {
        return this.viewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        page.setViewerInput(viewer.getSelection());
        super.okPressed();
    }

}
