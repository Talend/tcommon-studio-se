// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.items.importexport.ui.wizard.imports;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ImportItemsWizard extends Wizard implements IImportWizard {

    private ImportItemsWizardPage mainPage;

    private IWorkbench workbench;

    private IStructuredSelection selection;

    @Override
    public void init(IWorkbench w, IStructuredSelection s) {
        this.workbench = w;
        this.selection = s;
    }

    @Override
    public void addPages() {
        super.addPages();
        mainPage = new ImportItemsWizardPage(this.getWindowTitle(), this.selection);
        addPage(mainPage);
    }

    @Override
    public boolean performCancel() {
        return mainPage.performCancel();
    }

    @Override
    public boolean performFinish() {
        return mainPage.performFinish();
    }

}
