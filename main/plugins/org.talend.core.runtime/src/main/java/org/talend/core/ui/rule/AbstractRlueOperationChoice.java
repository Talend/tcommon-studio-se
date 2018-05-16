// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.rule;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * DOC hwang class global comment. Detailled comment
 */
public abstract class AbstractRlueOperationChoice extends SelectionDialog {

    protected boolean isCancel = false;

    protected boolean isRepositoryBtnChecked = false;

    protected String selectedRuleFileName;

    protected boolean isCheckViewRules = false;

    protected AbstractRlueOperationChoice(Shell parentShell) {
        super(parentShell);
    }

    public boolean isRepositoryBtnChecked() {
        return this.isRepositoryBtnChecked;
    }

    public boolean isCancel() {
        return this.isCancel;
    }

    public String getSelectedRuleFileName() {
        return this.selectedRuleFileName;
    }

    public boolean isCheckViewRules() {
        return this.isCheckViewRules;
    }

}
