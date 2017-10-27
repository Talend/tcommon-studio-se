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
package org.talend.librariesmanager.utils;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialogWithProgress;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by Administrator on 2012-9-17 Detailled comment
 * 
 */
public class ModulesInstaller {

    private static Logger log = Logger.getLogger(ModulesInstaller.class);

    public static void installModules(final Shell shell, final String[] jarNames) {
        if (jarNames.length == 0) {
            return;
        }
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                String text = Messages.getString("ModulesInstaller_text"); //$NON-NLS-1$
                String title = Messages.getString("ModulesInstaller_title"); //$NON-NLS-1$
                ExternalModulesInstallDialogWithProgress dialog = new ExternalModulesInstallDialogWithProgress(shell, text,
                        title, SWT.APPLICATION_MODAL);
                dialog.showDialog(true, jarNames);
            }
        });
    }

    public static void installModules(final Shell shell, final Collection<ModuleNeeded> requiredModules) {
        if (requiredModules.size() == 0) {
            return;
        }
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                String text = Messages.getString("ModulesInstaller_text"); //$NON-NLS-1$
                String title = Messages.getString("ModulesInstaller_title"); //$NON-NLS-1$
                ExternalModulesInstallDialogWithProgress dialog = new ExternalModulesInstallDialogWithProgress(shell, text,
                        title, SWT.APPLICATION_MODAL);
                dialog.showDialog(true, requiredModules);
            }
        });
    }

}
