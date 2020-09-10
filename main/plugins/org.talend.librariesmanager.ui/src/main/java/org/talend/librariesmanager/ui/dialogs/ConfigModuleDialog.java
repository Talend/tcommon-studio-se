// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
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
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.ConfigModuleHelper;
import org.talend.librariesmanager.utils.DownloadModuleRunnableWithLicenseDialog;
import org.talend.librariesmanager.utils.JarDetector;
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

    private Label findByNameLabel;

    private Text defaultUriTxt;

    private Button copyURIButton;

    private Text customUriText;

    private Button useCustomBtn;

    private boolean useCustom;

    private String urlToUse;

    private String defaultURI;

    private String moduleName = "";

    private String cusormURIValue = "";

    private String defaultURIValue = "";

    private Set<String> jarsAvailable;
    
    private String customURI = null;

    private Button searchLocalBtn;

    private Button searchRemoteBtn;

    private Combo searchResultCombo;

    private AutoCompleteField resultField;

    private boolean isLocalSearch;

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
        layout.marginBottom = 60;
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
        createInstallNew(radioContainer);
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
        setPlatformGroupEnabled(true);
        validateInputForPlatform();
        setInstallNewGroupEnabled(false);
        setRepositoryGroupEnabled(false);
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

        platformCombo = new Combo(composite, SWT.SINGLE | SWT.BORDER);
        platformCombo.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));

        platfromRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setPlatformGroupEnabled(true);
                setInstallNewGroupEnabled(false);
                setRepositoryGroupEnabled(false);
                moduleName = platformCombo.getText();
                setupMavenURIByModuleName(moduleName);
                validateInputForPlatform();
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

        new AutoCompleteField(platformCombo, new ComboContentAdapter(), moduleValueArray);

        platformCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = platformCombo.getText();
                setupMavenURIByModuleName(moduleName);
                validateInputForPlatform();
            }
        });

    }

    private void setPlatformGroupEnabled(boolean enable) {
        platfromRadioBtn.setSelection(enable);
        platformCombo.setEnabled(enable);
        if (enable) {
            setupMavenURIByModuleName(moduleName);
            useCustomBtn.setEnabled(false);
            customUriText.setEnabled(false);
            validateInputFields();
            setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
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
        layout.numColumns = 4;
        repGroupSubComp.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalIndent = 30;
        repGroupSubComp.setLayoutData(data);

        createFindByName(repGroupSubComp);

        repositoryRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setPlatformGroupEnabled(false);
                setInstallNewGroupEnabled(false);
                setRepositoryGroupEnabled(true);
            }
        });

    }

    private void createInstallNew(Composite radioContainer) {

        installRadioBtn = new Button(radioContainer, SWT.RADIO);
        installRadioBtn.setText(Messages.getString("ConfigModuleDialog.installNewBtn"));

        Composite repGroupSubComp = new Composite(radioContainer, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginBottom = 0;
        layout.marginRight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        repGroupSubComp.setLayout(layout);
        repGroupSubComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

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
        installRadioBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setInstallNewGroupEnabled(true);
                setPlatformGroupEnabled(false);
                setRepositoryGroupEnabled(false);
            }
        });
    }

    private void createFindByName(Composite repGroupSubComp) {

        findByNameLabel = new Label(repGroupSubComp, SWT.NONE);
        findByNameLabel.setText(Messages.getString("ConfigModuleDialog.findExistByNameBtn"));

        nameTxt = new Text(repGroupSubComp, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        nameTxt.setLayoutData(data);

        nameTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validateInputForSearch();
            }
        });

        searchLocalBtn = new Button(repGroupSubComp, SWT.PUSH);
        searchLocalBtn.setText(Messages.getString("ConfigModuleDialog.searchLocalBtn"));
        searchLocalBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleSearch(true);
            }
        });

        searchRemoteBtn = new Button(repGroupSubComp, SWT.PUSH);
        searchRemoteBtn.setText(Messages.getString("ConfigModuleDialog.searchRemoteBtn"));
        searchRemoteBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleSearch(false);
            }
        });

        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 3;

        Label invisibleLabel = new Label(repGroupSubComp, SWT.NONE);
        invisibleLabel.setText(Messages.getString("ConfigModuleDialog.findExistByNameBtn"));
        invisibleLabel.setVisible(false);
        searchResultCombo = new Combo(repGroupSubComp, SWT.BORDER);
        searchResultCombo.setLayoutData(data);
        resultField = new AutoCompleteField(searchResultCombo, new ComboContentAdapter());

        searchResultCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                moduleName = searchResultCombo.getText();
                @SuppressWarnings("unchecked")
                Map<String, MavenArtifact> data = (Map<String, MavenArtifact>) searchResultCombo.getData();
                MavenArtifact art = data.get(moduleName);
                setupMavenURIByArtifact(art);
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
            validateInputFields();
        }
    }

    private void setFindByNameGroupEnabled(boolean enable) {
        nameTxt.setEnabled(enable);
        searchLocalBtn.setEnabled(enable);
        searchRemoteBtn.setEnabled(enable);
        searchResultCombo.setEnabled(enable);
        if (enable) {
            moduleName = nameTxt.getText().trim();
            useCustomBtn.setEnabled(false);
            customUriText.setEnabled(false);
            validateInputFields();
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

        useCustomBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useCustomBtn.getSelection()) {
                    // show the warning if useCustomBtn select/deselect
                    layoutWarningComposite(false, defaultUriTxt.getText());
                    customUriText.setEnabled(true);
                    if ("".equals(customUriText.getText())) {
                        customUriText.setText(ModuleMavenURIUtils.MVNURI_TEMPLET);
                    }
                } else {
                    customUriText.setEnabled(false);
                }
                validateInputForInstall();
            }
        });

        customUriText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validateInputFields();
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
        this.jarPathTxt.setText(result);
        File file = new File(result);
        moduleName = file.getName();

        final IRunnableWithProgress detectProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Dectect jar " + file.getName(), 100);
                monitor.worked(10);
                DisplayUtils.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            MavenArtifact art = JarDetector.parse(file);
                            String mvnUrl = MavenUrlHelper.generateMvnUrl(art);
                            defaultUriTxt.setText(mvnUrl);
                            if (StringUtils.isEmpty(defaultUriTxt.getText())) {
                                // default uri is empty
                                useCustomBtn.setSelection(true);
                                customUriText.setEnabled(true);
                                customUriText.setText(ModuleMavenURIUtils.MVNURI_TEMPLET);
                            } else {
                                useCustomBtn.setSelection(false);
                                customUriText.setEnabled(false);
                                customUriText.setText("");
                            }
                            validateInputFields();
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                });
                monitor.done();
            }
        };

        runProgress(detectProgress);
    }

    private void handleSearch(boolean local) {
        String name = nameTxt.getText();
        isLocalSearch = local;
        final IRunnableWithProgress progress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Search " + name, 100);
                monitor.worked(10);
                DisplayUtils.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            List<MavenArtifact> ret = null;
                            if (local) {
                                ret = ConfigModuleHelper.searchLocalArtifacts(name);
                            } else {
                                ret = ConfigModuleHelper.searchRemoteArtifacts(name);
                            }
                            String[] items = ConfigModuleHelper.toArray(ret);
                            Map<String, MavenArtifact> data = new HashMap<String, MavenArtifact>();
                            for (MavenArtifact art : ret) {
                                data.put(art.getFileName(false), art);
                            }
                            searchResultCombo.setData(data);
                            if (items.length > 0) {
                                searchResultCombo.setItems(items);
                                resultField.setProposals(items);
                            }
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                });
                monitor.done();
            }
        };

        runProgress(progress);

    }

    private void runProgress(IRunnableWithProgress progress) {
        ProgressMonitorDialog detectDialog = new ProgressMonitorDialog(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
            detectDialog.run(true, true, progress);
        } catch (Throwable e) {
            if (!(e instanceof TimeoutException)) {
                ExceptionHandler.process(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.librariesmanager.ui.dialogs.InstallModuleDialog#checkFieldsError()
     */
    private boolean validateInputFields() {
        boolean statusOK = true;
        if (installRadioBtn.getSelection()) {
             statusOK = validateInputForInstall();
         } else if (platfromRadioBtn.getSelection()) {
             statusOK = validateInputForPlatform();
        } else {
             statusOK =  validateInputForSearch();
        }
        if (!statusOK) {
            getButton(IDialogConstants.OK_ID).setEnabled(statusOK);
            return statusOK;
        }
        setMessage(Messages.getString("ConfigModuleDialog.message", moduleName), IMessageProvider.INFORMATION);
        getButton(IDialogConstants.OK_ID).setEnabled(statusOK);
        return statusOK;
    }

    private boolean validateInputForInstall() {
        if (!new File(jarPathTxt.getText()).exists()) {
            setMessage(Messages.getString("InstallModuleDialog.error.jarPath"), IMessageProvider.ERROR);
            return false;
        }
        String originalText = defaultUriTxt.getText().trim();
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customUriText.getText(), moduleName);
        useCustom = useCustomBtn.getSelection();
        if (useCustomBtn.getSelection()) {
            // if use custom uri:validate custom uri + check deploy status
            String errorMessage = ModuleMavenURIUtils.validateCustomMvnURI(originalText, customURIWithType);
            if (errorMessage != null) {
                setMessage(errorMessage, IMessageProvider.ERROR);
                return false;
            }
        }

        setMessage(Messages.getString("InstallModuleDialog.message"), IMessageProvider.INFORMATION);
        return true;
    }

    private boolean validateInputForSearch() {
        boolean disable = nameTxt.getText().trim().isEmpty();
        searchLocalBtn.setEnabled(!disable);
        searchRemoteBtn.setEnabled(!disable);
        if (disable) {
            setMessage(Messages.getString("ConfigModuleDialog.error.missingName"), IMessageProvider.ERROR);
            return false;
        }
        moduleName = searchResultCombo.getText().trim();
        boolean found = false;
        for (String item : searchResultCombo.getItems()) {
            if (item.equals(moduleName)) {
                found = true;
                break;
            }
        }
        if (StringUtils.isEmpty(moduleName) || !found) {
            setMessage(Messages.getString("ConfigModuleDialog.error.missingModule"), IMessageProvider.ERROR);
            return false;
        }
        return true;
    }

    private boolean validateInputForPlatform() {
        moduleName = platformCombo.getText().trim();
        boolean found = false;
        for (String item : platformCombo.getItems()) {
            if (item.equals(moduleName)) {
                found = true;
                break;
            }
        }
        if (StringUtils.isEmpty(moduleName) || !found) {
            setMessage(Messages.getString("ConfigModuleDialog.error.missingModule"), IMessageProvider.ERROR);
            return false;
        }
        return true;
    }

    private void setRepositoryGroupEnabled(boolean enable) {
        repositoryRadioBtn.setSelection(enable);
        setFindByNameGroupEnabled(enable);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        defaultURI = defaultUriTxt.getText().trim();
        customURI = customUriText.getText().trim();
        urlToUse = defaultURI;
        if (useCustomBtn.getSelection()) {
            customURI = MavenUrlHelper.addTypeForMavenUri(customUriText.getText().trim(), moduleName);
            urlToUse = !StringUtils.isEmpty(customURI) ? customURI : defaultURI;
        }
        if (installRadioBtn.getSelection()) {
            File jarFile = new File(jarPathTxt.getText().trim());
            MavenArtifact art = MavenUrlHelper.parseMvnUrl(urlToUse);
            moduleName = art.getFileName(false);
            String sha1New = ConfigModuleHelper.getSHA1(jarFile);
            art.setSha1(sha1New);
            // resolve jar locally
            File localFile = ConfigModuleHelper.resolveLocal(urlToUse);
            boolean install = false;
            if (localFile != null && localFile.exists()) {
                String sha1Local = ConfigModuleHelper.getSHA1(localFile);
                // already installed with different jar
                if (!sha1Local.equals(sha1New)) {
                    install = true;
                }
            } else {
                // just install
                install = true;
            }

            if (install) {
                final IRunnableWithProgress progress = new IRunnableWithProgress() {
                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.beginTask("Install and share " + jarFile, 100);
                        monitor.worked(10);
                        DisplayUtils.getDisplay().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    boolean deploy = true;
                                    // check remote
                                    List<MavenArtifact> remoteArtifacts = null;
                                    try {
                                        remoteArtifacts = ConfigModuleHelper.searchRemoteArtifacts(art.getGroupId(),
                                                art.getArtifactId(), art.getVersion());
                                    } catch (Exception e) {
                                        ExceptionHandler.process(e);
                                    }

                                    if (remoteArtifacts != null && !remoteArtifacts.isEmpty()) {
                                        if (ConfigModuleHelper.canFind(new HashSet<MavenArtifact>(remoteArtifacts), art)) {
                                            deploy = false;
                                        } else {
                                            // popup and ask, reinstall?
                                            deploy = MessageDialog.open(MessageDialog.CONFIRM, getShell(), "",
                                                    Messages.getString("ConfigModuleDialog.shareInfo"), SWT.NONE);
                                        }
                                    }

                                    ConfigModuleHelper.install(jarFile, urlToUse, deploy);
                                } catch (Exception e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                        });
                        monitor.done();
                    }
                };

                runProgress(progress);

            }

        } else if (repositoryRadioBtn.getSelection()) {
            if (!isLocalSearch) {
                boolean download = true;
                // resolve jar locally
                File localFile = ConfigModuleHelper.resolveLocal(urlToUse);
                if (localFile != null && localFile.exists()) {
                    // check sha1
                    String sha1Local = ConfigModuleHelper.getSHA1(localFile);
                    @SuppressWarnings("unchecked")
                    Map<String, MavenArtifact> data = (Map<String, MavenArtifact>) searchResultCombo.getData();
                    MavenArtifact art = data.get(moduleName);
                    if (sha1Local.equals(art.getSha1())) {
                        download = false;
                    }
                }
                if (download) {
                    // download
                    ModuleToInstall mod = new ModuleToInstall();
                    mod.setRequired(true);
                    mod.setMavenUri(defaultURI);
                    mod.setName(moduleName);
                    mod.setFromCustomNexus(true);

                    List<ModuleToInstall> toInstall = new ArrayList<ModuleToInstall>();
                    toInstall.add(mod);
                    DownloadModuleRunnableWithLicenseDialog downloadModuleRunnable = new DownloadModuleRunnableWithLicenseDialog(
                            toInstall, getShell());
                    runProgress(downloadModuleRunnable);
                }
            }
        }

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
        if (useCustom && customURI != null) {
            return customURI;
        }
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
        defaultUriTxt.setText(moduel.getDefaultMavenURI() == null ? "" : moduel.getDefaultMavenURI());
        boolean useCustom = !StringUtils.isEmpty(cusormURIValue);
        useCustomBtn.setSelection(useCustom);
        // customUriText.setEnabled(useCustom);
        customUriText.setText(cusormURIValue);
    }

    private void setupMavenURIByArtifact(MavenArtifact art) {
        defaultURIValue = MavenUrlHelper.generateMvnUrl(art);
        defaultUriTxt.setText(defaultURIValue);
        boolean useCustom = !StringUtils.isEmpty(cusormURIValue);
        useCustomBtn.setSelection(useCustom);
        customUriText.setText(cusormURIValue);
    }

}
