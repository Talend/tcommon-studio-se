// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.expressionbuilder.ICellEditorDialog;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by wchen on Aug 16, 2017 Detailled comment
 *
 */
public class InstallModuleDialog extends TitleAreaDialog implements ICellEditorDialog, IConfigModuleDialog {

    private Label warningLabel;

    private GridData warningLayoutData;

    private Text jarPathTxt;

    private Button browseButton;

    private MavenURIComposite installNewRUIComposite;

    private ModuleNeeded module;

    private CustomURITextCellEditor cellEditor;

    private String moduleName = "";

    private String cusormURIValue = "";

    private String defaultURIValue = "";

    /**
     * DOC wchen InstallModuleDialog constructor comment.
     */
    public InstallModuleDialog(Shell parentShell) {
        super(parentShell);
    }

    public InstallModuleDialog(Shell parentShell, CustomURITextCellEditor cellEditor) {
        super(parentShell);
        setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
        this.cellEditor = cellEditor;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("InstallModuleDialog.title"));//$NON-NLS-1$

    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginTop = 20;
        layout.marginLeft = 20;
        layout.marginRight = 20;
        layout.marginBottom = 100;
        layout.numColumns = 3;
        container.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(data);

        createWarningLabel(container);
        createJarPathComposite(container);
        installNewRUIComposite = new MavenURIComposite(this, moduleName, defaultURIValue, cusormURIValue);
        installNewRUIComposite.createMavenURIComposite(container);

        return parent;
    }

    private void createWarningLabel(Composite container) {
        Composite warningComposite = new Composite(container, SWT.NONE);
        warningComposite.setBackground(warningColor);
        warningLayoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        warningLayoutData.horizontalSpan = ((GridLayout) container.getLayout()).numColumns;
        warningComposite.setLayoutData(warningLayoutData);
        GridLayout layout = new GridLayout();
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.numColumns = 2;
        warningComposite.setLayout(layout);
        Label imageLabel = new Label(warningComposite, SWT.NONE);
        imageLabel.setImage(ImageProvider.getImage(EImage.WARNING_ICON));
        imageLabel.setBackground(warningColor);

        warningLabel = new Label(warningComposite, SWT.WRAP);
        warningLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        warningLabel.setBackground(warningColor);
        warningLayoutData.exclude = true;
    }

    @Override
    public void layoutWarningComposite(boolean exclude, String defaultMavenURI) {
        warningLayoutData.exclude = exclude;
        warningLabel.setText(Messages.getString("InstallModuleDialog.warning", defaultMavenURI));
        warningLabel.getParent().getParent().layout();
    }

    private void createJarPathComposite(Composite container) {
        Label label1 = new Label(container, SWT.NONE);
        label1.setText(Messages.getString("InstallModuleDialog.newJar"));
        jarPathTxt = new Text(container, SWT.BORDER);
        jarPathTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

        browseButton = new Button(container, SWT.PUSH);
        browseButton.setText("...");//$NON-NLS-1$
        browseButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleButtonPressed();
            }
        });
        jarPathTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsError();
            }
        });
    }

    /**
     * 
     * DOC wchen Comment method "checkInstallCompositeError".
     * 
     * @return false if has error
     */
    @Override
    public boolean checkFieldsError() {
        if (!new File(jarPathTxt.getText()).exists()) {
            setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
            return false;
        }
        boolean statuOK = installNewRUIComposite.checkFieldsError();
        if (!statuOK) {
            return false;
        }

        setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.TitleAreaDialog#setMessage(java.lang.String, int)
     */
    @Override
    public void setMessage(String newMessage, int newType) {
        super.setMessage(newMessage, newType);
        if (newType == IMessageProvider.ERROR) {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
        } else {
            getButton(IDialogConstants.OK_ID).setEnabled(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
        return control;
    }

    private void handleButtonPressed() {
        FileDialog dialog = new FileDialog(getShell());
        dialog.setText(Messages.getString("InstallModuleDialog.title")); //$NON-NLS-1$

        String filePath = this.jarPathTxt.getText().trim();
        if (filePath.length() == 0) {
            dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
        } else {
            File file = new File(filePath);
            if (file.exists()) {
                dialog.setFilterPath(new Path(filePath).toOSString());
            }
        }

        String result = dialog.open();
        if (result != null) {
            this.jarPathTxt.setText(result);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.runtime.expressionbuilder.ICellEditorDialog#openDialog(java.lang.Object)
     */
    @Override
    public void openDialog(Object obj) {
        this.module = cellEditor.getModule();
        this.moduleName = module.getModuleName();
        this.defaultURIValue = module.getDefaultMavenURI();
        this.cusormURIValue = module.getCustomMavenUri();

        open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        String newMVNURI = null;
        if (installNewRUIComposite.useCustomBtn.getSelection()) {
            newMVNURI = MavenUrlHelper.addTypeForMavenUri(installNewRUIComposite.customUriText.getText().trim(), moduleName);
            if (cellEditor != null) {
                cellEditor.setConsumerExpression(newMVNURI);
                cellEditor.fireApplyEditorValue();
            }
        } else {
            newMVNURI = installNewRUIComposite.defaultUriTxt.getText().trim();
            if (cellEditor != null) {
                cellEditor.setConsumerExpression(installNewRUIComposite.defaultUriTxt.getText());
                cellEditor.fireApplyEditorValue();
            }
        }

        if (!"".equals(jarPathTxt.getText().trim())) {
            final String mvnURI = newMVNURI;
            File file = new File(jarPathTxt.getText().trim());
            if (file.exists()) {
                final IRunnableWithProgress acceptOursProgress = new IRunnableWithProgress() {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            monitor.beginTask("Install module " + file.getName(), 100);
                            monitor.worked(30);
                            LibManagerUiPlugin.getDefault().getLibrariesService().deployLibrary(file.toURL(), mvnURI);
                            monitor.done();
                        } catch (IOException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                };

                ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell());
                try {
                    dialog.run(true, true, acceptOursProgress);
                } catch (Throwable e) {
                    if (!(e instanceof TimeoutException)) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        super.okPressed();
        LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
        setMessage("");
        return super.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.dialogs.IConfigModuleDialog#getModuleName()
     */
    @Override
    public String getModuleName() {
        return moduleName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.dialogs.IConfigModuleDialog#getMavenURI()
     */
    @Override
    public String getMavenURI() {
        return null;
    }
}
