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
package org.talend.registration.wizards.register.ui.htmlcontent;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Priority;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.runtime.utils.io.SHA1Util;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.service.IExchangeService;
import org.talend.core.ui.token.TokenCollectorFactory;
import org.talend.registration.i18n.Messages;
import org.talend.registration.register.RegisterManagement;
import org.talend.registration.wizards.register.TalendForgeDialog;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by kongxiaohan on Mar 26, 2015 Detailled comment
 *
 */
public class TalendForgeRegistAction implements IIntroAction {

    public static final String ACTION_TYPE = "type"; //$NON-NLS-1$

    public static final String ACTION_CREATE_ACCOUNT = "CREATE_ACCOUNT"; //$NON-NLS-1$

    public static final String ACTION_USE_EXSITING_ACCOUNT = "USE_EXSITING_ACCOUNT"; //$NON-NLS-1$

    public static final String ACTION_CHECK_EMAIL = "CHECK_EMAIL"; //$NON-NLS-1$

    public static final String ACTION_SKIP = "SKIP"; //$NON-NLS-1$

    public static final String FIELD_USER_NAME = "USER_NAME"; //$NON-NLS-1$

    public static final String FIELD_PASSWORD = "PASSWORD"; //$NON-NLS-1$

    public static final String FIELD_EMAIL = "EMAIL"; //$NON-NLS-1$

    public static final String FIELD_COUNTRY = "COUNTRY"; //$NON-NLS-1$

    public static final String FIELD_IS_HELP_IMPROVE = "IS_HELP_IMPROVE"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    @Override
    public void run(IIntroSite site, Properties params) {
        if (ACTION_CREATE_ACCOUNT.equals(params.get(ACTION_TYPE))) {
            String userName = params.getProperty(FIELD_USER_NAME);
            String password = params.getProperty(FIELD_PASSWORD);
            String email = params.getProperty(FIELD_EMAIL);
            String contry = params.getProperty(FIELD_COUNTRY);
            String isHelpImprove = params.getProperty(FIELD_IS_HELP_IMPROVE);

            boolean success = false;
            IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
            try {
                success = RegisterManagement.getInstance().createUser(email, userName, password, "", "", contry, false, "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        "", ""); //$NON-NLS-1$ //$NON-NLS-2$
                if (success) {
                    String connectionEmail = null;
                    String userEmail = TalendForgeDialog.getCurrentTalendForgeDialog().getUserEmail();
                    if (userEmail != null) {
                        connectionEmail = userEmail;
                    } else {
                        connectionEmail = "test@talend.com"; //$NON-NLS-1$
                    }
                    prefStore.setValue(connectionEmail, email + ":" + userName + ":" + SHA1Util.hex_sha1(password)); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } catch (BusinessException e) {
                CommonExceptionHandler.process(e, Priority.WARN);
            } finally {
                if (success) {
                    DisplayUtils.getDisplay().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            MessageDialog.openInformation(DisplayUtils.getDefaultShell(),
                                    Messages.getString("TalendForgeDialog.MessageTitle"), //$NON-NLS-1$
                                    Messages.getString("TalendForgeDialog.Message")); //$NON-NLS-1$
                            TalendForgeDialog.getCurrentTalendForgeDialog().okPressed();
                        }
                    });
                    improve(prefStore, isHelpImprove);
                }
            }
        } else if (ACTION_USE_EXSITING_ACCOUNT.equals(params.get(ACTION_TYPE))) {
            String username = params.getProperty(FIELD_USER_NAME);
            String password = params.getProperty(FIELD_PASSWORD);
            String isHelpImprove = params.getProperty(FIELD_IS_HELP_IMPROVE);

            boolean success = false;
            try {
                success = RegisterManagement.getInstance().createUser(username, password, "", "", "", false, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
            } catch (BusinessException e1) {
                CommonExceptionHandler.process(e1);
            }

            if (success) {
                improve(PlatformUI.getPreferenceStore(), isHelpImprove);
                // check the password
                boolean isUserPassRight = true;
                if (PluginChecker.isExchangeSystemLoaded()) {
                    IExchangeService service = (IExchangeService) GlobalServiceRegister.getDefault().getService(
                            IExchangeService.class);
                    final String checkUserAndPass = service.checkUserAndPass(username, SHA1Util.hex_sha1(password));
                    if (checkUserAndPass != null) {
                        isUserPassRight = false;
                        final Display display = DisplayUtils.getDisplay();
                        DisplayUtils.getDisplay().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                MessageDialog.openInformation(display.getActiveShell(),
                                        Messages.getString("TalendForgeDialog.MessageTitle"), checkUserAndPass); //$NON-NLS-1$
                            }
                        });
                    }
                }
                if (isUserPassRight) {
                    IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
                    String connectionEmail = null;
                    String userEmail = TalendForgeDialog.getCurrentTalendForgeDialog().getUserEmail();
                    if (userEmail != null) {
                        connectionEmail = userEmail;
                    } else {
                        connectionEmail = "test@talend.com"; //$NON-NLS-1$
                    }
                    prefStore.setValue(connectionEmail, "notused" + ":" + username + ":" + SHA1Util.hex_sha1(password)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    // bug TDI-19619,when connect correct,no need openInformation.
                    // MessageDialog.openInformation(getShell(),
                    // Messages.getString("TalendForgeDialog.MessageTitle"),
                    // Messages.getString("TalendForgeDialog.ConnectSuccessMessage"));
                    TalendForgeDialog.getCurrentTalendForgeDialog().okPressed();
                }
            }
        } else if (ACTION_SKIP.equals(params.get(ACTION_TYPE))) {
            DisplayUtils.getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    TalendForgeDialog.getCurrentTalendForgeDialog().okPressed();
                }
            });
        } else if (ACTION_CHECK_EMAIL.equals(params.get(ACTION_TYPE))) {
            String email = params.getProperty(FIELD_EMAIL);
            String checkEmailJavaScript = "     var email = document.getElementById('email').value;\r\n" + "\r\n"
                    + "        var isOK = false;\r\n" + "        \r\n" + "        if (email != \"\") {\r\n"
                    + "            if (#1){\r\n" + "                isOK = true;\r\n" + "            }\r\n" + "        }\r\n"
                    + "\r\n" + "        if (isOK){\r\n"
                    + "            document.getElementById('emailErrorHint').style.display = 'none';\r\n"
                    + "            document.getElementById('emailOKImage').style.display = 'block';\r\n"
                    + "            isCreateAccountEmailOK = true;\r\n" + "        } else {\r\n"
                    + "            document.getElementById('emailErrorHint').style.display = 'block';\r\n"
                    + "            document.getElementById('emailOKImage').style.display = 'none';\r\n"
                    + "            isCreateAccountEmailOK = false;\r\n" + "        }\r\n"
                    + "        refreshCreateAccountButtton();\r\n";
            TalendForgeDialog.getCurrentTalendForgeDialog().getBrowser()
                    .execute(checkEmailJavaScript.replaceAll("#1", "" + isEmailValid(email)));
        }
    }

    protected void improve(IPreferenceStore preferenceStore, String improve) {
        boolean isImprove = Boolean.valueOf(improve);
        preferenceStore.setValue(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED, isImprove);
        if (preferenceStore instanceof ScopedPreferenceStore) {
            try {
                ((ScopedPreferenceStore) preferenceStore).save();
            } catch (IOException e1) {
                CommonExceptionHandler.process(e1);
            }
        }

        if (isImprove) {
            TokenCollectorFactory.getFactory().send(true);
        }
    }

    protected boolean isEmailValid(String email) {
        if (email != null && !"".equals(email)) {
            if (!Pattern.matches(RepositoryConstants.MAIL_PATTERN, email)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
