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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.ModuleMavenURIUtils;

/**
 * 
 * created by wchen on Sep 18, 2017 Detailled comment
 *
 */
public class ConfigModuleDialog extends TitleAreaDialog implements IConfigModuleDialog {

    private Label warningLabel;

    private GridData warningLayoutData;

    private Text nameTxt;

    private Button platfromRadioBtn;

    private Combo platformCombo;

    private Button repositoryRadioBtn;

    private Button installRadioBtn;

    private Text jarPathTxt;

    private Button browseButton;

    private Button findByNameRadioBtn;

    private Button findByURIRadioBtn;

    private Text findByURITxt;

    private Text defaultUriTxt;

    private Button copyURIButton;

    private Text customUriText;

    private Button useCustomBtn;

    private Button detectButton;

    private String urlToUse;

    private String defaultURI;

    private String moduleName = "";

    private String cusormURIValue = "";

    private String defaultURIValue = "";

    private Set<String> jarsAvailable;

    /**
     * DOC wchen InstallModuleDialog constructor comment.
     * 
     * @param parentShell
     */
    public ConfigModuleDialog(Shell parentShell, String initValue) {
        super(parentShell);
        setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
        if (initValue != null && !"".equals(initValue)) {
            moduleName = initValue;
            ModuleNeeded testModuel = new ModuleNeeded("", initValue, "", true);
            defaultURIValue = testModuel.getDefaultMavenURI();
            String customMavenUri = testModuel.getCustomMavenUri();
            if (customMavenUri != null) {
                cusormURIValue = customMavenUri;
            }
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ConfigModuleDialog.text"));//$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginTop = 10;
        layout.marginLeft = 20;
        layout.marginRight = 20;
        layout.marginBottom = 100;
        layout.marginHeight = 0;
        container.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(data);
        createWarningLabel(container);

        Composite radioContainer = new Composite(container, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        radioContainer.setLayout(layout);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        radioContainer.setLayoutData(data);
        createPlatformGroup(radioContainer);
        createRepositoryGroup(radioContainer, container);

        createMavenURIGroup(container);
        return parent;
    }

    private void createMavenURIGroup(Composite parent) {
        Composite mvnContainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginBottom = 5;
        layout.numColumns = 3;
        mvnContainer.setLayout(layout);
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        mvnContainer.setLayoutData(layoutData);
        createMavenURIComposite(mvnContainer);
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        if (!StringUtils.isEmpty(moduleName) && !jarsAvailable.contains(moduleName)) {
            setPlatformGroupEnabled(false);
            installRadioBtn.setSelection(false);
            findByURIRadioBtn.setSelection(false);
            findByNameRadioBtn.setSelection(true);
            nameTxt.setText(moduleName);
            setRepositoryGroupEnabled(true);
        } else {
            setPlatformGroupEnabled(true);
            setRepositoryGroupEnabled(false);
        }
        return control;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#open()
     */
    @Override
    public int open() {
        int open = super.open();
        return open;
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
        warningLabel.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
        warningLabel.setBackground(warningColor);
        warningLayoutData.exclude = true;
    }

    private void layoutWarningComposite(boolean exclude, String defaultMvnURI) {
        warningLayoutData.exclude = exclude;
        warningLabel.setText(Messages.getString("InstallModuleDialog.warning", defaultMvnURI));
        // warningLabel.getParent().getParent().getParent().layout();
        Composite parent = warningLabel.getParent().getParent();
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.marginBottom = 10;
        parent.layout();
        // layoutChildernComp(parent);
    }

    private void createPlatformGroup(Composite composite) {
        platfromRadioBtn = new Button(composite, SWT.RADIO);
        platfromRadioBtn.setText(Messages.getString("ConfigModuleDialog.platfromBtn"));

        platformCombo = new Combo(composite, SWT.READ_ONLY);
        platformCombo.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
        platfromRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setPlatformGroupEnabled(true);
                setRepositoryGroupEnabled(false);
                setupMavenURIByModuleName(platformCombo.getText());
            }
        });

        jarsAvailable = new HashSet<String>();
        Set<ModuleNeeded> unUsedModules = ModulesNeededProvider.getAllManagedModules();
        for (ModuleNeeded module : unUsedModules) {
            if (module.getStatus() == ELibraryInstallStatus.INSTALLED) {
                jarsAvailable.add(module.getModuleName());
            }
        }
        String[] moduleValueArray = jarsAvailable.toArray(new String[jarsAvailable.size()]);
        Comparator<String> comprarator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        };
        Arrays.sort(moduleValueArray, comprarator);
        platformCombo.setItems(moduleValueArray);
        if (!StringUtils.isEmpty(moduleName) && jarsAvailable.contains(moduleName)) {
            platformCombo.setText(moduleName);
        } else {
            platformCombo.setText(moduleValueArray[0]);
        }
        platformCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = platformCombo.getText();
                setupMavenURIByModuleName(platformCombo.getText());
            }
        });
    }

    private void setPlatformGroupEnabled(boolean enable) {
        platfromRadioBtn.setSelection(enable);
        platformCombo.setEnabled(enable);
        if (enable) {
            detectButton.setEnabled(false);
            moduleName = platformCombo.getText();
            setupMavenURIByModuleName(moduleName);
            useCustomBtn.setEnabled(false);
            customUriText.setEnabled(false);
            setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
            getButton(IDialogConstants.OK_ID).setEnabled(true);
        }
    }

    private void createRepositoryGroup(Composite radioContainer, Composite container) {
        repositoryRadioBtn = new Button(radioContainer, SWT.RADIO);
        repositoryRadioBtn.setText(Messages.getString("ConfigModuleDialog.repositoryBtn"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        repositoryRadioBtn.setLayoutData(data);

        // Group repGroupSubComp = new Group(container, SWT.SHADOW_IN);
        Composite repGroupSubComp = new Composite(container, SWT.BORDER);
        GridLayout layout = new GridLayout();
        layout.marginTop = 0;
        layout.numColumns = 3;
        repGroupSubComp.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalIndent = 30;
        repGroupSubComp.setLayoutData(data);

        createInstallNew(repGroupSubComp);

        createFindByName(repGroupSubComp);

        createFindByURI(repGroupSubComp);

        repositoryRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setPlatformGroupEnabled(false);
                setRepositoryGroupEnabled(true);
            }
        });

    }

    private void createInstallNew(Composite repGroupSubComp) {
        installRadioBtn = new Button(repGroupSubComp, SWT.RADIO);
        installRadioBtn.setText(Messages.getString("ConfigModuleDialog.installNewBtn"));
        installRadioBtn.setSelection(true);

        jarPathTxt = new Text(repGroupSubComp, SWT.BORDER);
        jarPathTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

        browseButton = new Button(repGroupSubComp, SWT.PUSH);
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
                File file = new File(jarPathTxt.getText());
                moduleName = file.getName();
                setupMavenURIByModuleName(moduleName);
                if (useCustomBtn.getSelection()) {
                    customUriText.setEnabled(true);
                }
                checkErrorForInstall();
            }
        });
        installRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setInstallNewGroupEnabled(installRadioBtn.getSelection());
            }
        });
    }

    private void createFindByName(Composite repGroupSubComp) {
        findByNameRadioBtn = new Button(repGroupSubComp, SWT.RADIO);
        findByNameRadioBtn.setText(Messages.getString("ConfigModuleDialog.findExistByNameBtn"));

        nameTxt = new Text(repGroupSubComp, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        nameTxt.setLayoutData(data);

        nameTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = nameTxt.getText().trim();
                setupMavenURIByModuleName(moduleName);
                checkErrorForFindExistingByName();
            }
        });
        findByNameRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setFindByNameGroupEnabled(findByNameRadioBtn.getSelection());
            }

        });
    }

    private void createFindByURI(Composite repGroupSubComp) {
        findByURIRadioBtn = new Button(repGroupSubComp, SWT.RADIO);
        findByURIRadioBtn.setText(Messages.getString("ConfigModuleDialog.findExistByURIBtn"));

        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        findByURITxt = new Text(repGroupSubComp, SWT.BORDER);
        findByURITxt.setLayoutData(data);
        findByURITxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                String uri = findByURITxt.getText().trim();
                boolean validateMvnURI = ModuleMavenURIUtils.validateMvnURI(uri);
                if (!validateMvnURI) {
                    setMessage(Messages.getString("InstallModuleDialog.error.findbyURI"), IMessageProvider.ERROR);
                    useCustomBtn.setSelection(false);
                    defaultUriTxt.setText("");
                    customUriText.setText("");
                    return;
                }
                ModuleNeeded testModule = new ModuleNeeded("", "", true, uri);
                moduleName = testModule.getModuleName();
                uri = testModule.getDefaultMavenURI();
                ModuleNeeded found = null;
                for (ModuleNeeded module : ModulesNeededProvider.getAllManagedModules()) {
                    if (moduleName.equals(module.getModuleName())) {
                        found = module;
                        break;
                    }
                }
                if (found != null) {
                    String defualtURIFromModule = found.getDefaultMavenURI();
                    String customURIFromModule = found.getCustomMavenUri();
                    defaultUriTxt.setText(defualtURIFromModule);
                    if (uri.equalsIgnoreCase(defualtURIFromModule) && customURIFromModule != null) {
                        useCustomBtn.setSelection(false);
                        customUriText.setText("");
                        layoutWarningComposite(false, defaultUriTxt.getText().trim());
                    } else if (!uri.equals(defualtURIFromModule)
                            || (customURIFromModule != null && !customURIFromModule.equals(uri))) {
                        customUriText.setText(uri);
                        useCustomBtn.setSelection(true);
                        layoutWarningComposite(false, defaultUriTxt.getText().trim());
                    }
                } else {
                    setupMavenURIByModuleName(moduleName);
                    if (!uri.equals(defaultUriTxt.getText())) {
                        customUriText.setText(uri);
                        useCustomBtn.setSelection(true);
                    }
                }
                checkInstallStatusErrorForFindExisting();
            }
        });
        findByURIRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setFindByURIGroupEnabled(findByURIRadioBtn.getSelection());
            }

        });
    }

    private void setInstallNewGroupEnabled(boolean enable) {
        jarPathTxt.setEnabled(enable);
        browseButton.setEnabled(enable);
        if (enable) {
            moduleName = new File(jarPathTxt.getText()).getName();
            setupMavenURIByModuleName(moduleName);
            useCustomBtn.setEnabled(true);
            if (useCustomBtn.getSelection()) {
                customUriText.setEnabled(true);
            }
            checkErrorForInstall();
        }
    }

    private void setFindByNameGroupEnabled(boolean enable) {
        nameTxt.setEnabled(enable);
        if (enable) {
            moduleName = nameTxt.getText().trim();
            setupMavenURIByModuleName(moduleName);
            useCustomBtn.setEnabled(false);
            customUriText.setEnabled(false);
            checkErrorForFindExistingByName();
        }
    }

    private void setFindByURIGroupEnabled(boolean enable) {
        findByURITxt.setEnabled(enable);
        if (enable) {
            useCustomBtn.setEnabled(false);
            customUriText.setEnabled(false);

            String uri = findByURITxt.getText().trim();
            boolean validateMvnURI = ModuleMavenURIUtils.validateMvnURI(uri);
            if (!validateMvnURI) {
                setMessage(Messages.getString("InstallModuleDialog.error.findbyURI"), IMessageProvider.ERROR);
                useCustomBtn.setSelection(false);
                defaultUriTxt.setText("");
                customUriText.setText("");
                return;
            }
            moduleName = MavenUrlHelper.generateModuleNameByMavenURI(uri);
            setupMavenURIByModuleName(moduleName);
            // set current uri as new cusotm uri
            if (!uri.equals(defaultUriTxt.getText().trim())) {
                customUriText.setText(uri);
                useCustomBtn.setSelection(true);
                layoutWarningComposite(false, defaultUriTxt.getText().trim());
            }
            checkInstallStatusErrorForFindExisting();

        }
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

        useCustomBtn = new Button(composite, SWT.CHECK);
        gdData = new GridData();
        useCustomBtn.setLayoutData(gdData);
        useCustomBtn.setSelection(!ModuleMavenURIUtils.MVNURI_TEMPLET.equals(cusormURIValue));
        useCustomBtn.setText(Messages.getString("InstallModuleDialog.customUri"));

        customUriText = new Text(composite, SWT.BORDER);
        gdData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gdData.horizontalSpan = 2;
        customUriText.setLayoutData(gdData);
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
                layoutWarningComposite(false, defaultUriTxt.getText());
                if (useCustomBtn.getSelection()) {
                    customUriText.setEnabled(true);
                    if ("".equals(customUriText.getText())) {
                        customUriText.setText(ModuleMavenURIUtils.MVNURI_TEMPLET);
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
        if (installRadioBtn.getSelection()) {
            handleDetectPressedForInstall();
        } else {
            handleDetectPressedForFindExsting();
        }
    }

    private void handleDetectPressedForInstall() {
        boolean deployed = checkInstalledStatusInMaven();
        if (deployed) {
            setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
        } else {
            setMessage(Messages.getString("ConfigModuleDialog.install.message", moduleName), IMessageProvider.INFORMATION);
        }
    }

    private void handleDetectPressedForFindExsting() {
        boolean deployed = checkInstalledStatusInMaven();
        if (deployed) {
            setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
        } else {
            setMessage(Messages.getString("ConfigModuleDialog.jarNotInstalled.error"), IMessageProvider.ERROR);
        }
    }

    private boolean checkInstalledStatusInMaven() {
        String uri = null;
        if (useCustomBtn.getSelection()) {
            uri = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
        } else {
            uri = defaultUriTxt.getText().trim();
        }
        boolean validateMvnURI = ModuleMavenURIUtils.validateMvnURI(uri);
        if (!validateMvnURI) {
            return false;
        }
        return ModuleMavenURIUtils.checkInstalledStatus(uri);
    }

    private void handleButtonPressed() {
        FileDialog dialog = new FileDialog(getShell());
        dialog.setText(Messages.getString("ConfigModuleDialog.install.message", moduleName)); //$NON-NLS-1$

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
     * @see org.talend.librariesmanager.ui.dialogs.InstallModuleDialog#checkFieldsError()
     */
    private boolean checkFieldsError() {
        if (repositoryRadioBtn.getSelection()) {
            if (installRadioBtn.getSelection()) {
                boolean statusOK = checkErrorForInstall();
                if (!statusOK) {
                    return false;
                }
            } else if (findByNameRadioBtn.getSelection()) {
                boolean statusOK = checkErrorForFindExistingByName();
                if (!statusOK) {
                    return false;
                }
            } else if (findByURIRadioBtn.getSelection()) {
                boolean statusOK = checkInstallStatusErrorForFindExisting();
                if (!statusOK) {
                    return false;
                }
            }

        }

        setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
        getButton(IDialogConstants.OK_ID).setEnabled(true);
        return true;
    }

    private boolean checkErrorForInstall() {
        if (!new File(jarPathTxt.getText()).exists()) {
            setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
            return false;
        }
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        ELibraryInstallStatus status = null;
        String mvnURI2Detect = "";
        if (useCustomBtn.getSelection()) {
            // if use custom uri:validate custom uri + check deploy status
            String errorMessage = ModuleMavenURIUtils.validateCustomMvnURI(originalText, customURIWithType);
            if (errorMessage != null) {
                setMessage(errorMessage, IMessageProvider.ERROR);
                return false;
            }

            status = ModuleStatusProvider.getDeployStatus(customURIWithType);
            if (status == ELibraryInstallStatus.DEPLOYED) {
                setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
                return false;
            }
            mvnURI2Detect = customURIWithType;
        } else {
            status = ModuleStatusProvider.getDeployStatus(originalText);
            if (status == ELibraryInstallStatus.DEPLOYED) {
                setMessage(Messages.getString("InstallModuleDialog.error.jarexsit"), IMessageProvider.ERROR);
                return false;
            }
            mvnURI2Detect = originalText;
        }

        // check deploy status from remote
        boolean statusOK = checkDetectButtonStatus(status, mvnURI2Detect);
        if (!statusOK) {
            return false;
        }

        setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;
    }

    private boolean checkErrorForFindExistingByName() {
        if (!moduleName.contains(".") || moduleName.endsWith(".")) {
            setMessage(Messages.getString("ConfigModuleDialog.moduleName.error"), IMessageProvider.ERROR);
            return false;
        }
        return checkInstallStatusErrorForFindExisting();
    }

    private boolean checkInstallStatusErrorForFindExisting() {
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        ELibraryInstallStatus status = null;
        String mavenURI2Detect = "";
        if (useCustomBtn.getSelection()) {
            // if use custom uri: validate custom uri + check deploy status
            String message = ModuleMavenURIUtils.validateCustomMvnURI(originalText, customURIWithType);
            if (message != null) {
                setMessage(message, IMessageProvider.ERROR);
                return false;
            }
            status = ModuleStatusProvider.getDeployStatus(customURIWithType);
            mavenURI2Detect = customURIWithType;
        } else {
            status = ModuleStatusProvider.getDeployStatus(originalText);
            mavenURI2Detect = originalText;
        }

        if (status == null) {
            setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI2Detect), IMessageProvider.ERROR);
            return false;
        }
        if (status != ELibraryInstallStatus.DEPLOYED) {
            NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            if (customNexusServer != null) {
                setMessage(Messages.getString("InstallModuleDialog.error.detectMvnURI", mavenURI2Detect), IMessageProvider.ERROR);
                return false;
            } else {
                setMessage(Messages.getString("ConfigModuleDialog.jarNotInstalled.error"), IMessageProvider.ERROR);
                return false;
            }
        }
        setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
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

    private void setRepositoryGroupEnabled(boolean enable) {
        repositoryRadioBtn.setSelection(enable);
        installRadioBtn.setEnabled(enable);
        findByNameRadioBtn.setEnabled(enable);
        findByURIRadioBtn.setEnabled(enable);
        if (enable) {
            detectButton.setEnabled(true);
            boolean hasDefaultSelection = installRadioBtn.getSelection() || findByNameRadioBtn.getSelection()
                    || findByURIRadioBtn.getSelection();
            setInstallNewGroupEnabled(!hasDefaultSelection || installRadioBtn.getSelection());
            setFindByNameGroupEnabled(findByNameRadioBtn.getSelection());
            setFindByURIGroupEnabled(findByURIRadioBtn.getSelection());
        } else {
            setInstallNewGroupEnabled(enable);
            setFindByNameGroupEnabled(enable);
            setFindByURIGroupEnabled(enable);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        String originalURI = null;
        String customURI = null;
        originalURI = defaultUriTxt.getText().trim();
        defaultURI = originalURI;
        if (useCustomBtn.getSelection()) {
            customURI = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
        }
        urlToUse = !StringUtils.isEmpty(customURI) ? customURI : originalURI;
        if (repositoryRadioBtn.getSelection()) {
            if (installRadioBtn.getSelection()) {
                final File file = new File(jarPathTxt.getText().trim());
                final IRunnableWithProgress acceptOursProgress = new IRunnableWithProgress() {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.beginTask("Install module " + file.getName(), 100);
                        monitor.worked(10);
                        DisplayUtils.getDisplay().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    LibManagerUiPlugin.getDefault().getLibrariesService().deployLibrary(file.toURL(), urlToUse);
                                } catch (IOException e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                        });
                        monitor.done();
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

        ModuleNeeded testModule = new ModuleNeeded("", "", true, originalURI);
        String oldCustomUri = testModule.getCustomMavenUri();
        boolean saveCustomMap = !StringUtils.equals(customURI, oldCustomUri);
        Set<String> modulesNeededNames = ModulesNeededProvider.getAllManagedModuleNames();
        boolean isCutomJar = !ModulesNeededProvider.getAllModuleNamesFromIndex().contains(moduleName);
        if (isCutomJar && customURI == null) {
            // key and value will be the same for custom jar if without custom uri
            customURI = urlToUse;
        }
        if (!modulesNeededNames.contains(moduleName)) {
            ModulesNeededProvider.addUnknownModules(moduleName, originalURI, false);
            saveCustomMap = true;
        }

        // change the custom uri
        if (saveCustomMap) {
            testModule.setCustomMavenUri(customURI);
            ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            libManagerService.saveCustomMavenURIMap();
        }

        LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
        setReturnCode(OK);
        close();
    }

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
        return defaultURI;
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

    private void setupMavenURIByModuleName(String moduleName) {
        ModuleNeeded moduel = new ModuleNeeded("", moduleName, "", true);
        if (StringUtils.isEmpty(moduel.getModuleName())) {
            defaultUriTxt.setText("");
            useCustomBtn.setSelection(false);
            customUriText.setEnabled(false);
            customUriText.setText("");
            defaultURIValue = "";
            cusormURIValue = "";
            return;
        }
        defaultURIValue = moduel.getDefaultMavenURI();
        cusormURIValue = moduel.getCustomMavenUri();
        if (cusormURIValue == null) {
            cusormURIValue = "";
        }
        defaultUriTxt.setText(moduel.getDefaultMavenURI());
        boolean useCustom = !StringUtils.isEmpty(cusormURIValue);
        useCustomBtn.setSelection(useCustom);
        // customUriText.setEnabled(useCustom);
        customUriText.setText(cusormURIValue);

    }

}
