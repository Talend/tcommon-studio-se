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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.EAliasesDereference;
import org.talend.core.repository.model.EAuthenticationMethod;
import org.talend.core.repository.model.EReferrals;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractLDAPSchemaStepForm;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaStep2Form extends AbstractLDAPSchemaStepForm {

    /** The combo to select the authentication method */
    private Combo authenticationMethodCombo;

    /** The bind user combo with the history of recently used bind users */
    private Combo bindPrincipalCombo;

    /** The text widget to input bind password */
    private Text bindPasswordText;

    /** The checkbox to choose if the bind password should be saved on disk */
    private Button saveBindPasswordButton;

    /** The button to check the authentication parameters */
    private Button checkPrincipalPasswordAuthButton;;

    private Group authParamGroup;

    private String password;

    /** The checkbox to fetch the base DN's from namingContexts whenever opening the connection */
    private Button autoFetchBaseDnsButton;

    /** The button to fetch the base DN's from namingContexts attribute */
    private Button fetchBaseDnsButton;

    /** The combo that displays the fetched base DN's */
    private Combo baseDNCombo;

    /** The finding button. */
    private Button findingButton;

    /** The search button. */
    private Button searchButton;

    /** The never button. */
    private Button neverButton;

    /** The always button. */
    private Button alwaysButton;

    /** The ignore button. */
    private Button ignoreButton;

    /** The follow button. */
    private Button followButton;

    /** The count limit text. */
    private Text countLimitText;

    /** The time limit text. */
    private Text timeLimitText;

    private String alertForFetchBaseDNs = Messages.getString("LDAPSchemaStep2Form.alertMessage"); //$NON-NLS-1$

    public LDAPSchemaStep2Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, String[] tableNames,
            IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, metadataTable, tableNames);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        addAuthMethodInput(this);

        addAuthParameterInput(this);

        addBaseDNInput(this);

        addLimitInput(this);
    }

    /**
     * DOC Administrator Comment method "addAuthParameterInput".
     */
    private void addAuthParameterInput(Composite inputComposite) {
        Composite composite2 = BaseWidgetUtils.createColumnContainer(inputComposite, 1, 1);
        // composite2.setEnabled(false);
        authParamGroup = BaseWidgetUtils.createGroup(composite2,
                Messages.getString("LDAPSchemaStep2Form.AuthenticationParameter"), 1); //$NON-NLS-1$
        Composite composite = BaseWidgetUtils.createColumnContainer(authParamGroup, 3, 1);

        // composite.setEnabled(false);
        BaseWidgetUtils.createLabel(composite, Messages.getString("LDAPSchemaStep2Form.BindDnOrUser"), 1); //$NON-NLS-1$
        // String[] dnHistory = HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_PRINCIPAL_HISTORY);
        String[] dnHistory = new String[] {};
        bindPrincipalCombo = BaseWidgetUtils.createCombo(composite, dnHistory, -1, 2);

        BaseWidgetUtils.createLabel(composite, Messages.getString("LDAPSchemaStep2Form.BindPassword"), 1); //$NON-NLS-1$
        bindPasswordText = BaseWidgetUtils.createPasswordText(composite, "", 2); //$NON-NLS-1$

        BaseWidgetUtils.createSpacer(composite, 1);
        saveBindPasswordButton = BaseWidgetUtils.createCheckbox(composite,
                Messages.getString("LDAPSchemaStep2Form.SavePassword"), 1); //$NON-NLS-1$
        saveBindPasswordButton.setSelection(true);
        saveBindPasswordButton.setEnabled(true);

        checkPrincipalPasswordAuthButton = new Button(composite, SWT.PUSH);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalAlignment = SWT.RIGHT;
        checkPrincipalPasswordAuthButton.setLayoutData(gd);
        checkPrincipalPasswordAuthButton.setText(Messages.getString("LDAPSchemaStep2Form.CheckAuthentication")); //$NON-NLS-1$
        checkPrincipalPasswordAuthButton.setEnabled(true);
    }

    /**
     * DOC Administrator Comment method "addAuthMethodInput".
     */
    private void addAuthMethodInput(Composite composite) {
        Composite composite1 = BaseWidgetUtils.createColumnContainer(composite, 1, 1);

        Group group1 = BaseWidgetUtils.createGroup(composite1, Messages.getString("LDAPSchemaStep2Form.AuthenticationMethod"), 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group1, 1, 1);

        String[] authMethods = new String[] { EAuthenticationMethod.ANONYMOUS.getName(), EAuthenticationMethod.SIMPLE.getName() };
        // "DIGEST-MD5 (SASL)", "CRAM-MD5 (SASL)"
        authenticationMethodCombo = BaseWidgetUtils.createReadonlyCombo(groupComposite, authMethods, 1, 2);
    }

    /**
     * Adds the base DN input.
     * 
     * @param parent the parent
     */
    private void addBaseDNInput(Composite parent) {
        Composite composite = BaseWidgetUtils.createColumnContainer(parent, 1, 1);

        Group group = BaseWidgetUtils.createGroup(composite, Messages.getString("LDAPSchemaStep2Form.BaseDN"), 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group, 3, 1);
        GridData gd;

        autoFetchBaseDnsButton = BaseWidgetUtils.createCheckbox(groupComposite,
                Messages.getString("LDAPSchemaStep2Form.GetBaseDNS"), 2); //$NON-NLS-1$
        autoFetchBaseDnsButton.setSelection(true);
        fetchBaseDnsButton = new Button(groupComposite, SWT.PUSH);
        fetchBaseDnsButton.setText(Messages.getString("LDAPSchemaStep2Form.FetchBaseDNs")); //$NON-NLS-1$
        fetchBaseDnsButton.setEnabled(true);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        fetchBaseDnsButton.setLayoutData(gd);

        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("LDAPSchemaStep2Form.Base.DN"), 1); //$NON-NLS-1$
        baseDNCombo = BaseWidgetUtils.createCombo(groupComposite, new String[0], 0, 2);
    }

    /**
     * Adds the limit input.
     * 
     * @param parent the parent
     */
    public void addLimitInput(Composite parent) {
        Composite composite = BaseWidgetUtils.createColumnContainer(parent, 3, 1);

        addAliasDereferenceGroupInput(composite);

        addReferralsGroupInput(composite);

        addLimitGroupInput(composite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        final LDAPSchemaConnection connection = (LDAPSchemaConnection) connectionItem.getConnection();

        // if (connection == null) {
        // connection.setProtocol(EAuthenticationMethod.ANONYMOUS.getName());
        // }

        bindPrincipalCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    connection.setBindPrincipal(bindPrincipalCombo.getText().trim());
                }
            }
        });

        bindPasswordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    password = bindPasswordText.getText().trim();
                    if (saveBindPasswordButton.getSelection() == true) {
                        connection.setSavePassword(true);
                        connection.setBindPassword(connection.getValue(password, true));
                    }
                }
            }
        });
        authenticationMethodCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (authenticationMethodCombo.getText().equals(EAuthenticationMethod.ANONYMOUS.getName())) {
                    refreshAuthParamGroup(connection, false);
                    connection.setUseAuthen(false);
                    String selectedDN = connection.getSelectedDN();
                    if (selectedDN == null || selectedDN.length() == 0) {
                        updateStatus(IStatus.ERROR, alertForFetchBaseDNs);
                    }
                } else {
                    refreshAuthParamGroup(connection, true);
                    connection.setUseAuthen(true);
                }
            }
        });

        checkPrincipalPasswordAuthButton.addSelectionListener(new SelectionAdapter() {

            boolean isOK = false;

            @Override
            public void widgetSelected(SelectionEvent event) {

                // System.out.println(connection);
                try {
                    IRunnableWithProgress op = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) {

                            connection.setUseAuthen(true);
                            isOK = LDAPConnectionUtils.checkParam(getOriginalValueConnection(), false);

                        }
                    };
                    new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(true, false, op);
                } catch (InvocationTargetException e) {
                    updateStatus(IStatus.ERROR, null);
                    MessageBoxExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    updateStatus(IStatus.ERROR, null);
                    MessageBoxExceptionHandler.process(e);
                }

                if (!isContextMode()) {
                    fetchBaseDnsButton.setEnabled(isOK);
                }
                connection.setUseAuthen(true);
                if (isOK) {
                    if (!isContextMode()) {
                        saveDialogSettings();
                    }
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep2Form.checkPara"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep2Form.checkSuccessful")); //$NON-NLS-1$
                    String baseDN = baseDNCombo.getText();
                    if(baseDN == null || baseDN.length()<=0){
                        updateStatus(IStatus.ERROR, alertForFetchBaseDNs);
                    }
                } else {

                    MessageDialog.openError(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep2Form.checkPara"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep2Form.checkFailed")); //$NON-NLS-1$
                    updateStatus(IStatus.ERROR, null);
                }
            }
        });

        autoFetchBaseDnsButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    // set enabled/disabled state of fields and buttons
                    boolean isSelection = autoFetchBaseDnsButton.getSelection();
                    baseDNCombo.setEnabled(!isSelection);
                    connection.setGetBaseDNsFromRoot(isSelection);
                }
            }
        });

        fetchBaseDnsButton.addSelectionListener(new SelectionAdapter() {

            boolean isOK = false;

            List<String> dnList = null;

            @Override
            public void widgetSelected(SelectionEvent event) {

                try {
                    IRunnableWithProgress op = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) {

                            dnList = fetchBaseDNsList();
                        }
                    };
                    new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(true, false, op);
                } catch (InvocationTargetException e) {
                    updateStatus(IStatus.ERROR, null);
                    MessageBoxExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    updateStatus(IStatus.ERROR, null);
                    MessageBoxExceptionHandler.process(e);
                }

                if (dnList != null && dnList.size() > 0) {
                    isOK = true;
                    int length = dnList.size();
                    String[] baseDNarray = dnList.toArray(new String[length]);
                    baseDNCombo.setItems((baseDNarray));
                    baseDNCombo.select(0);
                    connection.getBaseDNs().addAll(Arrays.asList(baseDNarray));
                    connection.setSelectedDN(baseDNarray[0]);
                    // System.out.println(connection);
                } else {
                    isOK = false;
                }

                if (isOK) {
                    if (!isContextMode()) {
                        saveDialogSettings();
                    }
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep2Form.fetchDNS"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep2Form.fetchSuccessful")); //$NON-NLS-1$
                    updateStatus(IStatus.OK, null);
                } else {
                    MessageDialog.openError(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep2Form.fetchDNS"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep2Form.fetchFailed")); //$NON-NLS-1$
                    updateStatus(IStatus.ERROR, null);
                }
            }
        });

        // baseDNCombo.addSelectionListener(new SelectionAdapter() {
        //
        // public void widgetSelected(SelectionEvent e) {
        // connection.setSelectedDN(baseDNCombo.getText());
        // }
        //
        // });

        baseDNCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (baseDNCombo.getText() != null) {
                    connection.setSelectedDN(baseDNCombo.getText());
                }
            }

        });

        findingButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (findingButton.getSelection()) {
                    connection.setAliases(EAliasesDereference.FINDING.getRepositoryName());
                }
            }
        });

        searchButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (searchButton.getSelection()) {
                    connection.setAliases(EAliasesDereference.SEARCHING.getRepositoryName());
                }
            }
        });

        alwaysButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (alwaysButton.getSelection()) {
                    connection.setAliases(EAliasesDereference.ALWAYS.getRepositoryName());
                }
            }
        });

        neverButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (neverButton.getSelection()) {
                    connection.setAliases(EAliasesDereference.NEVER.getRepositoryName());
                }
            }
        });

        ignoreButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (ignoreButton.getSelection()) {
                    connection.setReferrals(EReferrals.IGNORE.getRepositoryName());
                }

            }
        });

        followButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (followButton.getSelection()) {
                    connection.setReferrals(EReferrals.FOLLOW.getRepositoryName());
                }
            }
        });

        countLimitText.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                if (!isContextMode()) {
                    if (!e.text.matches("[0-9]*")) { //$NON-NLS-1$
                        e.doit = false;
                    }
                }
            }
        });
        countLimitText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    String countLimit = countLimitText.getText();
                    if (countLimit != null && countLimit.length() > 0) {
                        connection.setCountLimit(countLimit);
                    }
                }
            }
        });

        timeLimitText.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                if (!isContextMode()) {
                    if (!e.text.matches("[0-9]*")) { //$NON-NLS-1$
                        e.doit = false;
                    }
                }
            }
        });
        timeLimitText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    String timeLimit = timeLimitText.getText();
                    if (timeLimit != null && timeLimit.length() > 0) {
                        connection.setTimeOutLimit(timeLimit);
                    }
                }
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {

        boolean isSampleAuthMethod = authenticationMethodCombo.getText().equals(EAuthenticationMethod.SIMPLE.getName());

        String[] items = bindPrincipalCombo.getItems();
        if (isSampleAuthMethod && (items == null || items.length == 0 || items[0] == null)
                && (bindPrincipalCombo.getText() == null || bindPrincipalCombo.getText().equals(""))) { //$NON-NLS-1$
            // bindPrincipalCombo.forceFocus();
            this.checkPrincipalPasswordAuthButton.setEnabled(false);
            this.fetchBaseDnsButton.setEnabled(false);
            updateStatus(IStatus.ERROR, "Bind DN or user name must be specified"); //$NON-NLS-1$
            return false;
        }
        // } else if (isSampleAuthMethod && (bindPasswordText.getText() == null ||
        // bindPasswordText.getText().equals(""))) {
        // // bindPasswordText.forceFocus();
        // this.checkPrincipalPasswordAuthButton.setEnabled(false);
        // this.fetchBaseDnsButton.setEnabled(false);
        // updateStatus(IStatus.ERROR, "Bind password must be specified"); //$NON-NLS-1$
        // return false;
        // }
        else if (isSampleAuthMethod && (bindPasswordText.getText() == null || "".equals(bindPasswordText.getText()))) {
            updateStatus(IStatus.ERROR, Messages.getString("LDAPSchemaStep2Form.verifyAuthentication")); //$NON-NLS-1$
            checkPrincipalPasswordAuthButton.setEnabled(true);
            return false;
        } else {
            this.checkPrincipalPasswordAuthButton.setEnabled(true);
            // updateStatus(IStatus.OK, null);
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        LDAPSchemaConnection connection = (LDAPSchemaConnection) this.connectionItem.getConnection();
        String protocol = connection.getProtocol();
        if (protocol != null && protocol.equals(EAuthenticationMethod.ANONYMOUS.getName())) {
            this.authenticationMethodCombo.setText(protocol);
            refreshAuthParamGroup(connection, false);
        } else {
            this.authenticationMethodCombo.setText(EAuthenticationMethod.SIMPLE.getName());
            refreshAuthParamGroup(connection, true);
        }

        String bindPrincipal = connection.getBindPrincipal();

        this.bindPrincipalCombo.setItems(HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_PRINCIPAL_HISTORY));

        if (bindPrincipal != null && bindPrincipal.length() > 0) {
            this.bindPrincipalCombo.setText(bindPrincipal);
        }
        String password = connection.getValue(connection.getBindPassword(), false);
        if (password != null && password.length() > 0) {
            this.bindPasswordText.setText(password);
            if (connection.isSavePassword()) {
                saveBindPasswordButton.setSelection(true);
            }
        }

        boolean isGetBaseDNsFromRoot = connection.isGetBaseDNsFromRoot();
        this.autoFetchBaseDnsButton.setSelection(isGetBaseDNsFromRoot);
        this.baseDNCombo.setEnabled(!isGetBaseDNsFromRoot);

        EList baseDNs = connection.getBaseDNs();
        String selectedDN = connection.getSelectedDN();
        if (!baseDNs.contains(selectedDN)) {
            if (selectedDN == null) {
                baseDNs.add("");
            } else {
                baseDNs.add(selectedDN);
            }
        }
        int size = baseDNs.size();
        if (baseDNs != null && size > 0 && baseDNs.get(0) != null) {
            this.baseDNCombo.setItems((String[]) baseDNs.toArray(new String[size]));
            for (int i = 0; i < size; i++) {
                if (baseDNs.get(i).equals(selectedDN)) {
                    this.baseDNCombo.select(i);
                    break;
                }
            }
        }

        String aliases = connection.getAliases();
        if (aliases == null) {

            connection.setAliases(EAliasesDereference.ALWAYS.getRepositoryName());
        } else {
            if (aliases.equals(EAliasesDereference.ALWAYS.getRepositoryName())) {
                this.alwaysButton.setSelection(true);
                this.findingButton.setSelection(false);
                this.neverButton.setSelection(false);
                this.searchButton.setSelection(false);

            } else if (aliases.equals(EAliasesDereference.FINDING.getRepositoryName())) {
                this.findingButton.setSelection(true);
                this.alwaysButton.setSelection(false);
                this.neverButton.setSelection(false);
                this.searchButton.setSelection(false);
            } else if (aliases.equals(EAliasesDereference.NEVER.getRepositoryName())) {
                this.neverButton.setSelection(true);
                this.findingButton.setSelection(false);
                this.alwaysButton.setSelection(false);
                this.searchButton.setSelection(false);

            } else if (aliases.equals(EAliasesDereference.SEARCHING.getRepositoryName())) {
                this.searchButton.setSelection(true);
                this.alwaysButton.setSelection(false);
                this.findingButton.setSelection(false);
                this.neverButton.setSelection(false);
            }
        }
        String referrals = connection.getReferrals();
        if (referrals == null) {
            connection.setReferrals(EReferrals.IGNORE.getRepositoryName());
        } else {
            if (referrals.equals(EReferrals.IGNORE.getRepositoryName())) {
                ignoreButton.setSelection(true);
                followButton.setSelection(false);
            } else if (referrals.equals(EReferrals.FOLLOW.getRepositoryName())) {
                followButton.setSelection(true);
                ignoreButton.setSelection(false);
            }
        }

        String timeOutLimit = connection.getTimeOutLimit();
        if (timeOutLimit == null) {
            connection.setTimeOutLimit("0"); //$NON-NLS-1$
            this.timeLimitText.setText("0"); //$NON-NLS-1$
        } else {
            this.timeLimitText.setText(timeOutLimit);
        }

        String countLimit = connection.getCountLimit();
        if (countLimit == null) {
            connection.setCountLimit("100"); //$NON-NLS-1$
            this.countLimitText.setText("100"); //$NON-NLS-1$
        } else {
            this.countLimitText.setText(countLimit);
        }

        if (connection.getFilter() == null) {
            connection.setFilter(ConnectionUIConstants.DEFAULT_FILTER);
        }

        checkFieldsValue();

    }

    /**
     * Comment method "saveDialogSettings".
     */
    public void saveDialogSettings() {
        HistoryUtils.save(ConnectionUIConstants.DIALOGSETTING_KEY_PRINCIPAL_HISTORY, this.bindPrincipalCombo.getText());
    }

    /**
     * Creates the widget.
     * 
     * @param parent the parent
     */
    public void addAliasDereferenceGroupInput(Composite parent) {

        Group aliasesDereferenceGroup = BaseWidgetUtils.createGroup(parent, "Aliases Dereferencing", 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(aliasesDereferenceGroup, 1, 1);

        findingButton = BaseWidgetUtils.createRadiobutton(groupComposite, EAliasesDereference.FINDING.getDisplayName(), 1);

        searchButton = BaseWidgetUtils.createRadiobutton(groupComposite, EAliasesDereference.SEARCHING.getDisplayName(), 1);

        neverButton = BaseWidgetUtils.createRadiobutton(groupComposite, EAliasesDereference.NEVER.getDisplayName(), 1);

        alwaysButton = BaseWidgetUtils.createRadiobutton(groupComposite, EAliasesDereference.ALWAYS.getDisplayName(), 1);

        // Selected by default
        alwaysButton.setSelection(true);
    }

    /**
     * Creates the widget.
     * 
     * @param parent the parent
     */
    public void addReferralsGroupInput(Composite parent) {

        Group referralsGroup = BaseWidgetUtils.createGroup(parent, "Referrals Handling", 1); //$NON-NLS-1$
        Composite groupComposite = BaseWidgetUtils.createColumnContainer(referralsGroup, 1, 1);

        ignoreButton = BaseWidgetUtils.createRadiobutton(groupComposite, EReferrals.IGNORE.getDisplayName(), 1);

        ignoreButton.setSelection(true);

        followButton = BaseWidgetUtils.createRadiobutton(groupComposite, EReferrals.FOLLOW.getDisplayName(), 1);
    }

    /**
     * Creates the widget.
     * 
     * @param parent the parent
     */
    public void addLimitGroupInput(Composite parent) {

        Group limitGroup = BaseWidgetUtils.createGroup(parent, "Limits", 1); //$NON-NLS-1$
        GridLayout gl = new GridLayout(2, false);
        limitGroup.setLayout(gl);

        // Count limit
        Label countLimitLabel = BaseWidgetUtils.createLabel(limitGroup, "&Count Limit:", 1); //$NON-NLS-1$
        countLimitText = BaseWidgetUtils.createText(limitGroup, "", 1); //$NON-NLS-1$

        // Time limit
        Label timeLimitLabel = BaseWidgetUtils.createLabel(limitGroup, "&Time Limit:", 1); //$NON-NLS-1$
        timeLimitText = BaseWidgetUtils.createText(limitGroup, "", 1); //$NON-NLS-1$
    }

    /**
     * Comment method "refreshAuthParamGroup".
     * 
     * @param connection
     */
    private void refreshAuthParamGroup(final LDAPSchemaConnection connection, boolean isEnabledAllowed) {
        authParamGroup.setEnabled(isEnabledAllowed);
        if (!isContextMode()) {
            bindPrincipalCombo.setEnabled(isEnabledAllowed);
            bindPasswordText.setEnabled(isEnabledAllowed);
        }
        saveBindPasswordButton.setEnabled(isEnabledAllowed);
        checkPrincipalPasswordAuthButton.setEnabled(false);
        if (isEnabledAllowed) {
            connection.setProtocol(EAuthenticationMethod.SIMPLE.getName());

            String principalText = bindPrincipalCombo.getText();
            String passwordText = bindPasswordText.getText();
            if (principalText != null && principalText.length() > 0 && passwordText != null && passwordText.length() > 0) {
                checkPrincipalPasswordAuthButton.setEnabled(true);
                fetchBaseDnsButton.setEnabled(true);
            } else {
                fetchBaseDnsButton.setEnabled(false);
            }
        } else {
            connection.setProtocol(EAuthenticationMethod.ANONYMOUS.getName());
            fetchBaseDnsButton.setEnabled(true);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (isContextMode()) {
            adaptFormToEditable();
        }
        if (visible) {
            collectContextParams(visible);
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();

        bindPasswordText.setEditable(!isContextMode());
        countLimitText.setEditable(!isContextMode());
        timeLimitText.setEditable(!isContextMode());

        if (isContextMode()) {
            bindPrincipalCombo.setEnabled(!isContextMode());
            fetchBaseDnsButton.setEnabled(!isContextMode());
            baseDNCombo.setEnabled(!isContextMode());
            // clear the echo
            bindPasswordText.setEchoChar('\0');
        } else {
            bindPasswordText.setEchoChar('*');
        }
    }

    @Override
    protected void exportAsContext() {
        super.exportAsContext();
        if (getContextModeManager() != null) {
            getContextModeManager().setDefaultContextType(getConnection());
        }
    }

    /**
     * ftang Comment method "fetchBaseDNsList".
     */
    private List<String> fetchBaseDNsList() {
        boolean isOK = LDAPConnectionUtils.checkParam(getOriginalValueConnection(), false);

        if (isOK) {
            return LDAPConnectionUtils.fetchBaseDNs();
        }
        return null;
    }

}
