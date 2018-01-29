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
package org.talend.registration.wizards.register;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.talend.commons.exception.BusinessException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.TalendBrowserLaunchHelper;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.registration.RegistrationPlugin;
import org.talend.registration.i18n.Messages;
import org.talend.registration.register.RegisterManagement;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC zli class global comment. Detailled comment
 */
public class RegisterWizardPage1 extends AbstractBasicWizardDialog {

    private int countryToSelect = 0;

    private Text emailText;

    private IStatus emailStatus;

    private Combo countryCombo;

    private String emailStr;

    private String countryStr;

    private String oldEmail;

    private String oldCountry;

    private String proxyHost = null;

    private String proxyPort = null;

    private String proxyUser = null;

    private String proxyPassword = null;

    private boolean isProxyEnable = false;

    private Button nextButton;

    /**
     * DOC zli RegisterWizardPage1 constructor comment.
     * 
     * @param parentShell
     * @param wizard
     */
    public RegisterWizardPage1(Shell parentShell, IWizard wizard) {
        super(parentShell, wizard);
    }

    public RegisterWizardPage1(Shell parentShell, IWizard wizard, String email, String country) {
        super(parentShell, wizard);
        this.oldEmail = email;
        this.oldCountry = country;
    }

    @Override
    protected void createBottomComposite(Composite composite, int style) {
        Composite bottom = new Composite(composite, SWT.NONE);
        GridLayout gd = new GridLayout();
        bottom.setLayout(gd);
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String string = Messages.getString("RegisterWizardPage.legalbottominformations", brandingService.getCorporationName());
        createLegalInfos(composite, 1, string);
        createSpacer(composite, 1);
    }

    @Override
    protected void rightComposite(Composite composite2, int style) {
        Composite composite = new Composite(composite2, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData());
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String string = Messages.getString("RegisterWizardPage.legalinformations", brandingService.getCorporationName());
        createLegalInfos(composite, 1, string);
        new Label(composite, SWT.NONE);
        Composite linkComposite = new Composite(composite, SWT.NONE);
        linkComposite.setLayout(new GridLayout());
        linkComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        FormToolkit toolkit = new FormToolkit(this.getShell().getDisplay());
        Hyperlink hyperlink = toolkit.createHyperlink(linkComposite, Messages.getString("RegisterWizardPage.moreInforms"),
                SWT.NONE);
        hyperlink.setBackground(linkComposite.getBackground());
        hyperlink.setLayoutData(new GridData());
        hyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                String url = "http://www.talendforge.org/communitybenefits.php";
                TalendBrowserLaunchHelper.openURL(url);

                // Runtime.getRuntime().exec("explorer http://www.talend.com/community/communityMember.php"); //
            }
        });

        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        setContactInformationComposite(composite, SWT.NONE);
    }

    protected void setContactInformationComposite(Composite composite, int style) {
        GridData layoutData;

        Composite informationsComposite = new Composite(composite, SWT.NONE);
        informationsComposite.setLayout(new GridLayout(3, false));
        informationsComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        Label informationLabel = new Label(informationsComposite, SWT.NONE);
        layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        informationLabel.setLayoutData(layoutData);
        informationLabel.setText(Messages.getString("RegisterWizardPage.contactInforms"));

        Label emailLabel = new Label(informationsComposite, SWT.NONE);
        emailLabel.setText(Messages.getString("RegisterWizardPage.email"));
        emailText = new Text(informationsComposite, SWT.BORDER);
        emailText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (oldEmail != null) {
            emailText.setText(oldEmail);
        }

        //
        new Label(informationsComposite, SWT.NONE);

        Label countryLable = new Label(informationsComposite, SWT.NONE);
        countryLable.setText(Messages.getString("RegisterWizardPage.country"));
        countryCombo = new Combo(informationsComposite, SWT.BORDER | SWT.READ_ONLY);
        countryCombo.setItems(initiateCountryList());
        countryCombo.select(countryToSelect);
        countryCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        addListeners();
    }

    public String getCountry() {
        if (countryCombo.getSelectionIndex() != -1) {
            String selectedCountry = countryCombo.getItem(countryCombo.getSelectionIndex());
            for (Locale locale : Locale.getAvailableLocales()) {
                if (locale.getDisplayCountry().compareTo(selectedCountry) == 0) {
                    return locale.getCountry();
                }
            }
        }
        return null;
    }

    private void addListeners() {
        emailText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                // checkFieldsValue();
                String email = emailText.getText();
                if (email != null && !"".equals(email)) {
                    if (!Pattern.matches(RepositoryConstants.MAIL_PATTERN, email)) {
                        nextButton.setEnabled(false);
                    } else {
                        nextButton.setEnabled(true);
                    }
                }
            }
        });
    }

    /**
     * DOC ocarbone Comment method "checkField".
     */
    protected void checkFieldsValue() {
        emailStatus = createOkStatus();
        // Email Name
        if (emailText.getText().length() == 0) {
            emailStatus = new Status(IStatus.ERROR, RegistrationPlugin.PLUGIN_ID, IStatus.OK,
                    Messages.getString("RegisterWizardPage.emailEmpty"), null); //$NON-NLS-1$
        } else {
            // Reg Exp validation
            if (!Pattern.matches(RepositoryConstants.MAIL_PATTERN, emailText.getText())) {
                emailStatus = new Status(IStatus.ERROR, RegistrationPlugin.PLUGIN_ID, IStatus.OK,
                        Messages.getString("RegisterWizardPage.emailNotValid"), null); //$NON-NLS-1$
            }
        }
        updatePageStatus();
    }

    private void updatePageStatus() {
        IStatus findMostSevere = findMostSevere();
        // setMessage(findMostSevere);
        // setPageComplete((findMostSevere != null) ? (findMostSevere.getSeverity() != IStatus.ERROR) : true);
    }

    private static IStatus createOkStatus() {
        return new Status(IStatus.OK, RegistrationPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }

    private IStatus findMostSevere() {
        IStatus status = null;
        if (emailStatus.getSeverity() == IStatus.ERROR) {
            status = emailStatus;
        }
        return status;
    }

    // private void setMessage(IStatus status) {
    // if ((status != null) && (IStatus.ERROR == status.getSeverity())) {
    // setErrorMessage(status.getMessage());
    //            setMessage(""); //$NON-NLS-1$
    // } else {
    // setMessage(DESCRIPTION);
    // setErrorMessage(null);
    // }
    // }

    public String getEmail() {
        return emailText.getText();
    }

    /**
     * DOC mhirt Comment method "initiateCountryList".
     * 
     * @return
     */
    private String[] initiateCountryList() {
        SortedSet<String> countryList = new TreeSet<String>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().compareTo("") != 0) { //$NON-NLS-1$
                countryList.add(locale.getDisplayCountry());
            }
        }

        String defaultCountry = Locale.getDefault().getDisplayCountry();
        int i = 0;

        if (oldCountry != null) {
            for (String country : countryList) {

                if (country.equals(oldCountry)) {
                    countryToSelect = i;
                    break;
                }
                i++;
            }
        } else {
            for (String country : countryList) {
                if (country.equals(defaultCountry)) {
                    countryToSelect = i;
                    break;
                }
                i++;
            }
        }
        return countryList.toArray(new String[] {});
    }

    @Override
    protected void buttonPressed(int buttonId) {

        if (IDialogConstants.NEXT_ID == buttonId) {
            emailStr = getEmail();
            setEmailStr(emailStr);
            countryStr = getCountry();
            boolean alreadyRegistered = false;
            String firstname = "";//$NON-NLS-N$
            String lastname = "";//$NON-NLS-N$
            String pseudonym = "";//$NON-NLS-N$
            String password = "";//$NON-NLS-N$

            try {
                String userInfos = RegisterManagement.getInstance().checkUser(emailStr, isProxyEnable, proxyHost, proxyPort,
                        proxyUser, proxyPassword);
                if (userInfos != null && !"".equals(userInfos)) {
                    alreadyRegistered = true;
                    String[] split = userInfos.split(",");//$NON-NLS-N$
                    if (split.length > 2) {

                        firstname = split[0];
                        if (split[0] != null && split[0].length() > 1 && split[0].startsWith("\"") && split[0].endsWith("\"")) {//$NON-NLS-N$//$NON-NLS-N$
                            firstname = split[0].substring(1, split[0].length() - 1);
                        }
                        if (split[1] != null && split[1].length() > 1 && split[1].startsWith("\"") && split[1].endsWith("\"")) {//$NON-NLS-N$//$NON-NLS-N$
                            lastname = split[1].substring(1, split[1].length() - 1);
                        }
                        if (split[2] != null && split[2].length() > 1 && split[2].startsWith("\"") && split[2].endsWith("\"")) {//$NON-NLS-N$//$NON-NLS-N$
                            pseudonym = split[2].substring(1, split[2].length() - 1);
                        }
                        //                        if (split[3] != null && split[3].length() > 1 && split[3].startsWith("\"") && split[3].endsWith("\"")) {//$NON-NLS-N$//$NON-NLS-N$
                        // password = split[3].substring(1, split[3].length() - 1);
                        // }
                    }

                }
            } catch (BusinessException e) {
                alreadyRegistered = false;
                // MessageBoxExceptionHandler.process(e);

            }

            close();
            RegisterWizard registerWizard = new RegisterWizard();
            RegisterWizardPage2 dialog = new RegisterWizardPage2(this.getShell(), registerWizard, emailStr, countryStr,
                    alreadyRegistered, firstname, lastname, pseudonym, password, isProxyEnable, proxyHost, proxyPort);
            dialog.open();
        } else if (IDialogConstants.CANCEL_ID == buttonId) {
            RegisterManagement.decrementTry();
            RegisterManagement.increaseFailRegisterTimes();
            close();
        } else if (IDialogConstants.HELP_ID == buttonId) {
            NetworkSettingDialog netSettingDialog = new NetworkSettingDialog(getParentShell());
            if (netSettingDialog.open() == Dialog.OK) {
                String[] proxyString = netSettingDialog.getProxyString();
                proxyHost = proxyString[0];
                proxyPort = proxyString[1];
                proxyUser = proxyString[2];
                proxyPassword = proxyString[3];
                if (proxyHost != null && !"".equals(proxyHost)) {//$NON-NLS-N$
                    isProxyEnable = true;
                }
            } else {
                proxyHost = null;
                proxyPort = null;
                proxyUser = null;
                proxyPassword = null;
                isProxyEnable = false;
            }
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {

        createButton(parent, IDialogConstants.HELP_ID, Messages.getString("RegisterWizardPage.netWorkSetting"), true);
        new Label(parent, SWT.NONE);
        createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("RegisterWizardPage.registerLater"), true);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        nextButton = createButton(parent, IDialogConstants.NEXT_ID, IDialogConstants.NEXT_LABEL, true);
        if (emailText.getText() != null && !"".equals(emailText.getText())) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
    }

    public String getEmailStr() {
        return this.emailStr;
    }

    public void setEmailStr(String emailStr) {
        this.emailStr = emailStr;
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, true);
        // layout.numColumns = 0; // this is incremented by createButton
        layout.makeColumnsEqualWidth = true;
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        composite.setLayout(layout);
        GridData data = new GridData();// GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_END
        composite.setLayoutData(data);
        composite.setFont(parent.getFont());

        // Add the buttons to the button bar.
        createButtonsForButtonBar(composite);
        return composite;
    }

    /**
     * DOC zli RegisterWizardPage1 class global comment. Detailled comment
     */
    class NetworkSettingDialog extends Dialog {

        private Text proxyHostText;

        private Text proxyPortText;

        private Text proxyUserText;

        private Text proxyPassworkText;

        private String[] proxyString;

        /**
         * DOC zli NetworSettingDialog constructor comment.
         * 
         * @param parentShell
         */
        protected NetworkSettingDialog(Shell parentShell) {
            super(parentShell);
        }

        @Override
        protected void configureShell(Shell newShell) {

            super.configureShell(newShell);
            newShell.setModified(true);
            newShell.setSize(350, 300);
            newShell.setText(Messages.getString("RegisterWizardPage.netWorkSetting")); //$NON-NLS-1$
        }

        @Override
        protected Control createDialogArea(Composite parent) {

            GridData layoutData;

            Composite center = new Composite(parent, SWT.NONE);
            center.setLayout(new GridLayout(2, false));
            layoutData = new GridData(GridData.FILL_BOTH);
            layoutData.horizontalIndent = 20;
            layoutData.verticalIndent = 40;

            center.setLayoutData(layoutData);

            Label proxyHostLabel = new Label(center, SWT.NONE);
            proxyHostLabel.setText(Messages.getString("RegisterWizardPage.proxyHost"));
            proxyHostText = new Text(center, SWT.BORDER);
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            proxyHostText.setLayoutData(layoutData);
            if (proxyHost != null) {
                proxyHostText.setText(proxyHost);
            }

            Label proxyPortLabel = new Label(center, SWT.NONE);
            proxyPortLabel.setText(Messages.getString("RegisterWizardPage.proxyPort"));
            proxyPortText = new Text(center, SWT.BORDER);
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            proxyPortText.setLayoutData(layoutData);
            if (proxyPort != null) {
                proxyPortText.setText(proxyPort);
            }

            Label proxyUserLabel = new Label(center, SWT.NONE);
            proxyUserLabel.setText(Messages.getString("RegisterWizardPage.proxyUser"));
            proxyUserText = new Text(center, SWT.BORDER);
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            proxyUserText.setLayoutData(layoutData);
            if (proxyUser != null) {
                proxyUserText.setText(proxyUser);
            }

            Label proxyPasswordLabel = new Label(center, SWT.NONE);
            proxyPasswordLabel.setText(Messages.getString("RegisterWizardPage.proxyPassword"));
            proxyPassworkText = new Text(center, SWT.BORDER);
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            proxyPassworkText.setLayoutData(layoutData);
            if (proxyPassword != null) {
                proxyPassworkText.setText(proxyPassword);
            }

            return center;

        }

        @Override
        protected void okPressed() {

            String[] proxyString = new String[4];
            proxyString[0] = proxyHostText.getText();
            proxyString[1] = proxyPortText.getText();
            proxyString[2] = proxyUserText.getText();
            proxyString[3] = proxyPassworkText.getText();
            setProxyString(proxyString);
            super.okPressed();
        }

        private String[] getProxyString() {
            return this.proxyString;
        }

        private void setProxyString(String[] proxyString) {
            this.proxyString = proxyString;
        }

        @Override
        protected void cancelPressed() {
            super.cancelPressed();
        }
    }

}
