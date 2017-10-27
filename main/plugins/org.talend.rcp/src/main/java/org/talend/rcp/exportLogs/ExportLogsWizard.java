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
package org.talend.rcp.exportLogs;

import org.eclipse.jface.wizard.Wizard;

/**
 * DOC wzhang class global comment. Detailled comment
 */
public class ExportLogsWizard extends Wizard {

    private ExportLogsWizardPage logPage;

    public ExportLogsWizard() {
        super();
    }

    public void addPages() {
        super.addPages();
        logPage = new ExportLogsWizardPage(getWindowTitle());
        addPage(logPage);
    }

    public boolean performFinish() {
        return logPage.performFinish();
    }

    public boolean performCancel() {
        return logPage.performCancel();
    }

}
