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

import org.eclipse.swt.widgets.Shell;

/**
 * created by wchen on Sep 25, 2017 Detailled comment
 *
 */
public interface IInstallModuleDialog {

    public void setMessage(String newMessage, int newType);

    public Shell getShell();

    public void layoutWarningComposite();

    public boolean checkFieldsError();
}
