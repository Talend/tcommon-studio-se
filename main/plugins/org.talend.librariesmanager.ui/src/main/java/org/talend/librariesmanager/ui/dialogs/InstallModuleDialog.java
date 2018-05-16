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
package org.talend.librariesmanager.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
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
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.ModuleMavenURIUtils;

/**
 * created by wchen on Aug 16, 2017 Detailled comment
 *
 */
public class InstallModuleDialog extends TitleAreaDialog implements ICellEditorDialog, IConfigModuleDialog {

    private Text defaultUriTxt;

    private Button copyURIButton;

    private Text customUriText;

    private Button useCustomBtn;

    private Button detectButton;

    private Label warningLabel;

    private GridData warningLayoutData;

    private Text jarPathTxt;

    private Button browseButton;

    private ModuleNeeded module;

    private CustomURITextCellEditor cellEditor;

    private String moduleName = "";

    private String cusormURIValue = "";

    private String defaultURIValue = "";

    private final String MVNURI_TEMPLET = "mvn:<groupid>/<artifactId>/<version>/<type>";

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
        createMavenURIComposite(container);

        return parent;
    }

    private void createMavenURIComposite(Composite composite) {
        Label label2 = new Label(composite, SWT.NONE);
        label2.setText(Messages.getString("InstallModuleDialog.originalUri"));
        defaultUriTxt = new Text(composite, SWT.BORDER);
        GridData gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        defaultUriTxt.setLayoutData(gdData);
        defaultUriTxt.setEnabled(false);
        defaultUriTxt.setBackground(composite.getBackground());
        defaultUriTxt.setText(defaultURIValue);

        copyURIButton = new Button(composite, SWT.NONE);
        copyURIButton.setToolTipText(Messages.getString("InstallModuleDialog.copyURIBtn"));
        copyURIButton.setImage(ImageProvider.getImage(EImage.COPY_ICON));

        Composite customContainter = new Composite(composite, SWT.NONE);
        customContainter.setLayoutData(new GridData());
        GridLayout layout = new GridLayout();
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        // layout.numColumns = 2;
        customContainter.setLayout(layout);

        useCustomBtn = new Button(customContainter, SWT.CHECK);
        gdData = new GridData();
        useCustomBtn.setLayoutData(gdData);
        useCustomBtn.setSelection(!MVNURI_TEMPLET.equals(cusormURIValue));
        useCustomBtn.setText(Messages.getString("InstallModuleDialog.customUri"));

        customUriText = new Text(composite, SWT.BORDER);
        gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gdData.horizontalSpan = 2;
        customUriText.setLayoutData(gdData);
        customUriText.setEnabled(useCustomBtn.getSelection());
        if (customUriText.isEnabled()) {
            customUriText.setText(cusormURIValue);
        }

        detectButton = new Button(composite, SWT.NONE);
        detectButton.setText(Messages.getString("InstallModuleDialog.detectButton.text"));
        gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gdData.horizontalSpan = 3;
        detectButton.setLayoutData(gdData);

        useCustomBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // show the warning if useCustomBtn select/deselect
                layoutWarningComposite(false, defaultUriTxt.getText());
                if (useCustomBtn.getSelection()) {
                    customUriText.setEnabled(true);
                    if ("".equals(customUriText.getText())) {
                        customUriText.setText(cusormURIValue);
                    }
                } else {
                    customUriText.setEnabled(false);
                }
                checkFieldsError();
            }
        });

        customUriText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsError();
            }
        });

        detectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleDetectPressed();
            }
        });
        copyURIButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                ModuleMavenURIUtils.copyDefaultMavenURI(defaultUriTxt.getText());
            }
        });
    }

    private void handleDetectPressed() {
        boolean deployed = checkInstalledStatusInMaven();
        if (!"".equals(jarPathTxt.getText())) {
            if (deployed) {
                setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
            } else {
                setMessage(Messages.getString("ConfigModuleDialog.message",this.moduleName), IMessageProvider.INFORMATION);
            }
        } else {
            if (deployed) {
                setMessage(Messages.getString("ConfigModuleDialog.message",this.moduleName), IMessageProvider.INFORMATION);
            } else {
                setMessage(Messages.getString("InstallModuleDialog.error.notInstalled"), IMessageProvider.WARNING);
            }
        }
    }

    private boolean checkInstalledStatusInMaven() {
        String uri = null;
        if (useCustomBtn.getSelection()) {
            uri = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
        } else {
            uri = defaultUriTxt.getText().trim();
        }
        return ModuleMavenURIUtils.checkInstalledStatus(uri);
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

    private void layoutWarningComposite(boolean exclude, String defaultMavenURI) {
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

    private boolean checkFieldsError() {
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        ELibraryInstallStatus status = null;
        String mvnURI2Detect = "";
        if (useCustomBtn.getSelection()) {
            // if use custom uri:validate custom uri + check deploy status
            String errorMessage = ModuleMavenURIUtils.validateCustomMvnURI(originalText, customURIWithType);
            if (errorMessage != null) {
                detectButton.setEnabled(false);
                setMessage(errorMessage, IMessageProvider.ERROR);
                return false;
            }
            detectButton.setEnabled(true);

            status = ModuleStatusProvider.getDeployStatus(customURIWithType);
            mvnURI2Detect = customURIWithType;
        } else {
            status = ModuleStatusProvider.getDeployStatus(originalText);
            mvnURI2Detect = originalText;
        }

        if (!"".equals(jarPathTxt.getText())) {
            if (!new File(jarPathTxt.getText()).exists()) {
                setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
                return false;
            }
            if (status == ELibraryInstallStatus.DEPLOYED) {
                setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
                return false;
            }
            // check deploy status from remote
            boolean statusOK = checkDetectButtonStatus(status, mvnURI2Detect);
            if (!statusOK) {
                return false;
            }
        }

        setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;
    }

    private boolean checkDetectButtonStatus(ELibraryInstallStatus localStatus, String mavenURI) {
        NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        if (customNexusServer != null || localStatus == null) {
            setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI), IMessageProvider.ERROR);
            return false;
        }
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
        setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
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
        if (StringUtils.isEmpty(this.cusormURIValue)) {
            this.cusormURIValue = MVNURI_TEMPLET;
        }
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
        if (useCustomBtn.getSelection()) {
            newMVNURI = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
            if (cellEditor != null) {
                cellEditor.setConsumerExpression(newMVNURI);
                cellEditor.fireApplyEditorValue();
            }
        } else {
            newMVNURI = defaultUriTxt.getText().trim();
            if (cellEditor != null) {
                cellEditor.setConsumerExpression(defaultUriTxt.getText());
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
