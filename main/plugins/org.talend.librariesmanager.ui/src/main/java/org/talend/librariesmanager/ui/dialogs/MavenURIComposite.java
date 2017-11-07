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
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.CustomMavenURIValidator;

/**
 * created by wchen on Sep 25, 2017 Detailled comment
 *
 */
public class MavenURIComposite {

    Text defaultUriTxt;

    Text customUriText;

    Button useCustomBtn;

    Button detectButton;

    protected String moduleName = "";

    protected String cusormURIValue = "";

    protected String defaultURIValue = "";

    protected IConfigModuleDialog moduleDialog;

    protected final String MVNURI_TEMPLET = "mvn:<groupid>/<artifactId>/<version>/<type>";

    boolean isInstall = true;

    boolean findInRepository = true;

    public MavenURIComposite(IConfigModuleDialog moduleDialog, String moduleName, String defaultURIValue, String cusormURIValue) {
        this.moduleDialog = moduleDialog;
        this.moduleName = moduleName;
        this.defaultURIValue = defaultURIValue;
        this.cusormURIValue = cusormURIValue;
        if (StringUtils.isEmpty(this.cusormURIValue)) {
            this.cusormURIValue = MVNURI_TEMPLET;
        }
    }

    public void createMavenURIComposite(Composite composite) {
        Label label2 = new Label(composite, SWT.NONE);
        label2.setText(Messages.getString("InstallModuleDialog.originalUri"));
        defaultUriTxt = new Text(composite, SWT.BORDER);
        GridData gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gdData.horizontalSpan = 2;
        defaultUriTxt.setLayoutData(gdData);
        defaultUriTxt.setEnabled(false);
        defaultUriTxt.setBackground(composite.getBackground());
        defaultUriTxt.setText(defaultURIValue);

        Composite customContainter = new Composite(composite, SWT.NONE);
        customContainter.setLayoutData(new GridData());
        GridLayout layout = new GridLayout();
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.numColumns = 2;
        customContainter.setLayout(layout);

        useCustomBtn = new Button(customContainter, SWT.CHECK);
        gdData = new GridData();
        useCustomBtn.setLayoutData(gdData);
        useCustomBtn.setSelection(!MVNURI_TEMPLET.equals(cusormURIValue));

        Label label3 = new Label(customContainter, SWT.NONE);
        label3.setText(Messages.getString("InstallModuleDialog.customUri"));
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
        detectButton.setEnabled(false);
        gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gdData.horizontalSpan = 3;
        detectButton.setLayoutData(gdData);

        useCustomBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // show the warning if useCustomBtn select/deselect
                moduleDialog.layoutWarningComposite(false, defaultUriTxt.getText());
                if (useCustomBtn.getSelection()) {
                    customUriText.setEnabled(true);
                    if ("".equals(customUriText.getText())) {
                        customUriText.setText(cusormURIValue);
                    }
                } else {
                    customUriText.setEnabled(false);
                }
                moduleDialog.checkFieldsError();
            }
        });

        customUriText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleDialog.checkFieldsError();
            }
        });

        detectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleDetectPressed();
            }
        });
    }

    protected boolean checkFieldsError() {
        if (isInstall) {
            return checkErrorForInstall();
        } else {
            return checkErrorForFindExisting();
        }
    }

    private boolean checkErrorForInstall() {
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        ELibraryInstallStatus status = null;
        String mvnURI2Detect = "";
        if (useCustomBtn.getSelection()) {
            // if use custom uri:validate custom uri + check deploy status
            String errorMessage = CustomMavenURIValidator.validateCustomMvnURI(originalText, customURIWithType);
            if (errorMessage != null) {
                detectButton.setEnabled(false);
                moduleDialog.setMessage(errorMessage, IMessageProvider.ERROR);
                return false;
            }

            status = getMavenURIInstallStatus(customURIWithType);
            if (status == ELibraryInstallStatus.DEPLOYED) {
                moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
                return false;
            }
            mvnURI2Detect = customURIWithType;
        } else {
            status = getMavenURIInstallStatus(originalText);
            if (status == ELibraryInstallStatus.DEPLOYED) {
                moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
                return false;
            }
            mvnURI2Detect = originalText;
        }

        // check deploy status from remote
        boolean statusOK = checkDetectButtonStatus(status, mvnURI2Detect);
        if (!statusOK) {
            return false;
        }

        detectButton.setEnabled(false);
        moduleDialog.setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;
    }

    private boolean checkErrorForFindExisting() {
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        ELibraryInstallStatus status = null;
        String mavenURI2Detect = "";
        if (useCustomBtn.getSelection()) {
            // if use custom uri: validate custom uri + check deploy status
            String message = CustomMavenURIValidator.validateCustomMvnURI(originalText, customURIWithType);
            if (message != null) {
                detectButton.setEnabled(false);
                moduleDialog.setMessage(message, IMessageProvider.ERROR);
                return false;
            }
            status = getMavenURIInstallStatus(customURIWithType);
            mavenURI2Detect = customURIWithType;
        } else {
            status = getMavenURIInstallStatus(originalText);
            mavenURI2Detect = originalText;
        }
        if (findInRepository && status == null) {
            moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI2Detect),
                    IMessageProvider.ERROR);
            detectButton.setEnabled(true);
            return false;
        }
        if (findInRepository && status != ELibraryInstallStatus.DEPLOYED) {
            NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            if (customNexusServer != null) {
                moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI2Detect),
                        IMessageProvider.ERROR);
                detectButton.setEnabled(true);
                return false;
            } else {
                moduleDialog.setMessage(Messages.getString("ConfigModuleDialog.jarNotInstalled.error"), IMessageProvider.ERROR);
                return false;
            }
        }
        detectButton.setEnabled(false);
        moduleDialog.setMessage(Messages.getString("ConfigModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;

    }

    protected ELibraryInstallStatus getMavenURIInstallStatus(String mvnURI) {
        ELibraryInstallStatus deployStatus = ModuleStatusProvider.getDeployStatus(mvnURI);
        // if (deployStatus == null) {
        // ILibraryManagerService libManagerService = (ILibraryManagerService)
        // GlobalServiceRegister.getDefault().getService(
        // ILibraryManagerService.class);
        // libManagerService.resolveStatusLocally(mvnURI);
        // deployStatus = ModuleStatusProvider.getDeployStatus(mvnURI);
        // }
        return deployStatus;
    }

    protected boolean checkDetectButtonStatus(ELibraryInstallStatus localStatus, String mavenURI) {
        NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        if (customNexusServer != null || localStatus == null) {
            detectButton.setEnabled(true);
            moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI),
                    IMessageProvider.ERROR);
            return false;
        }
        return true;
    }

    protected void handleDetectPressed() {
        if (isInstall) {
            handleDetectPressedForInstall();
        } else {
            handleDetectPressedForFindExsting();
        }
    }

    private void handleDetectPressedForInstall() {
        boolean deployed = checkInstalledStatus();
        if (deployed) {
            moduleDialog.setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
        } else {
            moduleDialog.setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        }
    }

    private void handleDetectPressedForFindExsting() {
        boolean deployed = checkInstalledStatus();
        if (deployed) {
            moduleDialog.setMessage(Messages.getString("ConfigModuleDialog.message"), IMessageProvider.INFORMATION);
        } else {
            moduleDialog.setMessage(Messages.getString("ConfigModuleDialog.jarNotInstalled.error"), IMessageProvider.ERROR);
        }
    }

    protected boolean checkInstalledStatus() {
        String uri = null;
        if (useCustomBtn.getSelection()) {
            uri = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
        } else {
            uri = defaultUriTxt.getText().trim();
        }
        final String mvnURI = uri;
        ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        String jarPathFromMaven = libManagerService.getJarPathFromMaven(mvnURI);
        final boolean[] deployStatus = new boolean[] { false };
        if (jarPathFromMaven != null) {
            deployStatus[0] = true;
        } else {
            final IRunnableWithProgress acceptOursProgress = new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
                    if (customNexusServer != null) {
                        File resolveJar = null;
                        try {
                            resolveJar = libManagerService.resolveJar(customNexusServer, mvnURI);
                        } catch (Exception e) {
                            deployStatus[0] = false;
                        }
                        if (resolveJar != null) {
                            deployStatus[0] = true;
                            DisplayUtils.getDisplay().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
                                }
                            });
                        }
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

        if (useCustomBtn.getSelection() && !deployStatus[0]) {
            ModuleStatusProvider.putDeployStatus(mvnURI, ELibraryInstallStatus.NOT_DEPLOYED);
            ModuleStatusProvider.putStatus(mvnURI, ELibraryInstallStatus.NOT_INSTALLED);
        }

        return deployStatus[0];
    }

    public void setupMavenURIByModuleName(String moduleName) {
        ModuleNeeded moduel = new ModuleNeeded("", moduleName, "", true);
        defaultUriTxt.setText(moduel.getDefaultMavenURI());
        String customMavenUri = moduel.getCustomMavenUri();
        if (customMavenUri != null) {
            useCustomBtn.setSelection(true);
            customUriText.setEnabled(true);
            customUriText.setText(customMavenUri);
        } else {
            useCustomBtn.setSelection(false);
            customUriText.setEnabled(false);
            customUriText.setText("");
        }
    }

    /**
     * Sets the isInstall.
     * 
     * @param isInstall the isInstall to set
     */
    public void setInstall(boolean isInstall) {
        this.isInstall = isInstall;
    }

    /**
     * Sets the findInRepository.
     * 
     * @param findInRepository the findInRepository to set
     */
    public void setFindInRepository(boolean findInRepository) {
        this.findInRepository = findInRepository;
    }

}
