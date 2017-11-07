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
import org.eclipse.swt.widgets.Group;
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
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

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

    private MavenURIComposite mavenURIComposite;

    private Button findRadioBtn;

    private GridData installNewLayoutData;

    private GridData findExistLayoutdata;

    private Composite pathTextContainer;

    private Composite nameTextContainer;

    private String initValue;

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
        this.initValue = initValue;
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
        mavenURIComposite = new MavenURIComposite(this, moduleName, defaultURIValue, cusormURIValue);
        mavenURIComposite.createMavenURIComposite(mvnContainer);
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        if (!StringUtils.isEmpty(moduleName) && !jarsAvailable.contains(moduleName)) {
            setPlatformGroupEnabled(false);
            setRepositoryGroupEnabled(true);
            installRadioBtn.setSelection(false);
            findRadioBtn.setSelection(true);
            showFindExisting();
            nameTxt.setText(moduleName);
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
        warningLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        warningLabel.setBackground(warningColor);
        warningLayoutData.exclude = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.dialogs.IConfigModuleDialog#layoutWarningComposite()
     */
    @Override
    public void layoutWarningComposite(boolean exclude, String defaultMvnURI) {
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
                mavenURIComposite.setupMavenURIByModuleName(platformCombo.getText());
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
        if (!StringUtils.isEmpty(initValue) && jarsAvailable.contains(initValue)) {
            platformCombo.setText(initValue);
        } else {
            platformCombo.setText(moduleValueArray[0]);
        }
        if (!StringUtils.isEmpty(initValue)) {
            ModuleNeeded testModuel = new ModuleNeeded("", initValue, "", true);
            defaultURIValue = testModuel.getDefaultMavenURI();
            String customMavenUri = testModuel.getCustomMavenUri();
            if (customMavenUri != null) {
                cusormURIValue = customMavenUri;
            }
        }
        platformCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = platformCombo.getText();
                mavenURIComposite.setupMavenURIByModuleName(platformCombo.getText());
            }
        });
    }

    private void setPlatformGroupEnabled(boolean enable) {
        platfromRadioBtn.setSelection(enable);
        platformCombo.setEnabled(enable);
        if (enable) {
            if (platfromRadioBtn.getSelection()) {
                mavenURIComposite.setInstall(false);
                mavenURIComposite.setFindInRepository(false);
                mavenURIComposite.setupMavenURIByModuleName(platformCombo.getText());
            }
            setMessage(Messages.getString("ConfigModuleDialog.message"), IMessageProvider.INFORMATION);
            getButton(IDialogConstants.OK_ID).setEnabled(true);
        }
    }

    private void createRepositoryGroup(Composite radioContainer, Composite container) {
        repositoryRadioBtn = new Button(radioContainer, SWT.RADIO);
        repositoryRadioBtn.setText(Messages.getString("ConfigModuleDialog.repositoryBtn"));
        GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        repositoryRadioBtn.setLayoutData(data);

        Group repGroupSubComp = new Group(container, SWT.SHADOW_IN);
        GridLayout layout = new GridLayout();
        repGroupSubComp.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalIndent = 30;
        repGroupSubComp.setLayoutData(data);

        installRadioBtn = new Button(repGroupSubComp, SWT.RADIO);
        installRadioBtn.setText(Messages.getString("ConfigModuleDialog.installNewBtn"));
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        installRadioBtn.setLayoutData(data);
        installRadioBtn.setSelection(true);

        findRadioBtn = new Button(repGroupSubComp, SWT.RADIO);
        findRadioBtn.setText(Messages.getString("ConfigModuleDialog.findExistBtn"));
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        findRadioBtn.setLayoutData(data);

        pathTextContainer = new Composite(repGroupSubComp, SWT.NONE);
        layout = new GridLayout();
        layout.marginLeft = 0;
        layout.numColumns = 3;
        pathTextContainer.setLayout(layout);
        installNewLayoutData = new GridData(GridData.FILL_BOTH);
        pathTextContainer.setLayoutData(installNewLayoutData);
        createInstallNewComposite(pathTextContainer);

        nameTextContainer = new Composite(repGroupSubComp, SWT.NONE);
        layout = new GridLayout();
        layout.marginLeft = 0;
        layout.numColumns = 3;
        nameTextContainer.setLayout(layout);
        findExistLayoutdata = new GridData(GridData.FILL_BOTH);
        nameTextContainer.setLayoutData(findExistLayoutdata);
        createFindExistingModuleComposite(nameTextContainer);
        findExistLayoutdata.exclude = true;

        repositoryRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setPlatformGroupEnabled(false);
                setRepositoryGroupEnabled(true);
            }
        });
        installRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (installRadioBtn.getSelection()) {
                    installNewLayoutData.exclude = false;
                    pathTextContainer.setVisible(true);

                    findExistLayoutdata.exclude = true;
                    nameTextContainer.setVisible(false);

                    repGroupSubComp.layout();
                    mavenURIComposite.setInstall(true);
                    checkFieldsError();
                }
            }
        });
        findRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                showFindExisting();
            }

        });

    }

    private void showFindExisting() {
        if (findRadioBtn.getSelection()) {
            installNewLayoutData.exclude = true;
            pathTextContainer.setVisible(false);

            findExistLayoutdata.exclude = false;
            nameTextContainer.setVisible(true);

            nameTextContainer.getParent().layout();
            mavenURIComposite.setInstall(false);
            checkFieldsError();
        }

    }

    private void createInstallNewComposite(Composite composite) {
        createJarPathComposite(composite);
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
                File file = new File(jarPathTxt.getText());
                moduleName = file.getName();
                if (!"".equals(moduleName)) {
                    mavenURIComposite.setupMavenURIByModuleName(moduleName);
                }
                checkFieldsError();
            }
        });
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

    private void createFindExistingModuleComposite(Composite composite) {
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText(Messages.getString("ConfigModuleDialog.moduleName"));
        nameTxt = new Text(composite, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        nameTxt.setLayoutData(data);

        nameTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = nameTxt.getText().trim();
                if (!"".equals(moduleName)) {
                    mavenURIComposite.setupMavenURIByModuleName(moduleName);
                }
                checkFieldsError();
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.librariesmanager.ui.dialogs.InstallModuleDialog#checkFieldsError()
     */
    @Override
    public boolean checkFieldsError() {
        if (repositoryRadioBtn.getSelection()) {
            if (installRadioBtn.getSelection()) {
                if (!new File(jarPathTxt.getText()).exists()) {
                    setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
                    return false;
                }
            } else {
                if (!moduleName.contains(".") || moduleName.endsWith(".")) {
                    setMessage(Messages.getString("ConfigModuleDialog.moduleName.error"), IMessageProvider.ERROR);
                    mavenURIComposite.detectButton.setEnabled(false);
                    return false;
                }
            }
        }
        boolean statusOK = mavenURIComposite.checkFieldsError();
        if (!statusOK) {
            return false;
        }

        setMessage(Messages.getString("ConfigModuleDialog.message"), IMessageProvider.INFORMATION);
        getButton(IDialogConstants.OK_ID).setEnabled(true);
        return true;
    }

    private void setRepositoryGroupEnabled(boolean enable) {
        repositoryRadioBtn.setSelection(enable);
        // install
        installRadioBtn.setEnabled(enable);
        jarPathTxt.setEnabled(enable);
        browseButton.setEnabled(enable);
        // find existing
        findRadioBtn.setEnabled(enable);
        nameTxt.setEnabled(enable);
        if (enable) {
            mavenURIComposite.setFindInRepository(true);
            mavenURIComposite.customUriText.setEnabled(mavenURIComposite.useCustomBtn.getSelection());
            if (installRadioBtn.getSelection()) {
                mavenURIComposite.setInstall(true);
            } else {
                mavenURIComposite.setInstall(false);
            }
            checkFieldsError();

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
        originalURI = mavenURIComposite.defaultUriTxt.getText().trim();
        defaultURI = originalURI;
        if (mavenURIComposite.useCustomBtn.getSelection()) {
            customURI = MavenUrlHelper.addTypeForMavenUri(mavenURIComposite.customUriText.getText().trim(), moduleName);
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

}
