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
package org.talend.repository.ui.wizards.metadata.connection.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.repository.metadata.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public class SelectDatabaseJarDialog extends Dialog {

    private static final String COMMA = ";"; //$NON-NLS-1$

    private static final String EMPTY = ""; //$NON-NLS-1$

    private String jarsStr;

    private ListViewer jarsViewer;

    private List<String> jarsList = new ArrayList<String>();

    private Button addBtn, delBtn;

    public SelectDatabaseJarDialog(Shell parentShell, String jarsStr) {
        super(parentShell);
        this.setShellStyle(getShellStyle() | SWT.RESIZE | SWT.DIALOG_TRIM);
        this.jarsStr = jarsStr;
        init();
    }

    private void init() {
        String[] split = jarsStr.split(COMMA);
        if (split != null) {
            for (String s : split) {
                if (!EMPTY.equals(s.trim())) {
                    jarsList.add(s.trim());
                }
            }
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("SelectDatabaseJarDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new GridLayout(2, false));

        jarsViewer = new ListViewer(composite);
        jarsViewer.setContentProvider(new ArrayContentProvider());
        jarsViewer.setLabelProvider(new LabelProvider() {

            @Override
            public Image getImage(Object element) {
                String text = getText(element);
                if (text != null && !EMPTY.equals(text)) {
                    if (!new File(text).exists()) {
                        return ImageProvider.getImage(EImage.WARNING_SMALL);
                    }
                }
                return super.getImage(element);
            }

        });
        jarsViewer.setInput(jarsList);
        // jarsViewer.getList().setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        layoutData.minimumHeight = 200;
        layoutData.minimumWidth = 300;
        layoutData.heightHint = 200;
        layoutData.widthHint = 300;
        jarsViewer.getList().setLayoutData(layoutData);

        Composite buttnComp = new Composite(composite, SWT.NONE);
        buttnComp.setLayout(new GridLayout(1, false));
        buttnComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        addBtn = new Button(buttnComp, SWT.PUSH);
        addBtn.setText(Messages.getString("SelectDatabaseJarDialog.add")); //$NON-NLS-1$
        addBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                dialog.setFilterExtensions(new String[] { "*.jar", "*.zip", "*.*", "*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                String userDir = System.getProperty("user.dir"); //$NON-NLS-1$
                String pathSeparator = System.getProperty("file.separator"); //$NON-NLS-1$
                dialog.setFilterPath(userDir + pathSeparator + "lib" + pathSeparator + "java"); //$NON-NLS-1$ //$NON-NLS-2$
                String path = dialog.open();
                if (path == null) {
                    return;
                }
                if (!jarsList.contains(path)) {
                    jarsList.add(path);
                    jarsViewer.setInput(jarsList);
                } else {
                    MessageDialog.openWarning(
                            getShell(),
                            Messages.getString("SelectDatabaseJarDialog.warningTitle"), Messages.getString("SelectDatabaseJarDialog.warningMessage")); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

        });
        delBtn = new Button(buttnComp, SWT.PUSH);
        delBtn.setText(Messages.getString("SelectDatabaseJarDialog.delete")); //$NON-NLS-1$
        delBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                for (Object o : ((StructuredSelection) jarsViewer.getSelection()).toList()) {
                    jarsList.remove(o);
                }
                jarsViewer.setInput(jarsList);
            }
        });
        return composite;
    }

    public String getJarsString() {
        return this.jarsStr;
    }

    @Override
    protected void okPressed() {
        jarsStr = EMPTY;
        for (String s : jarsList) {
            jarsStr += s + COMMA;
        }
        int length = jarsStr.length();
        if (length > 0) {
            char c = jarsStr.charAt(length - 1);
            if (COMMA.equals(new Character(c).toString())) {
                jarsStr = jarsStr.substring(0, length - 1);
            }
        }
        super.okPressed();
    }

}
