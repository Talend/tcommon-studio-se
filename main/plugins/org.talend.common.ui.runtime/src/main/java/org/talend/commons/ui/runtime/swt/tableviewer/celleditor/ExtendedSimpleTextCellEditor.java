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
package org.talend.commons.ui.runtime.swt.tableviewer.celleditor;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * created by hcyi on Jun 14, 2017
 * Detailled comment
 *
 */
public class ExtendedSimpleTextCellEditor extends TextCellEditor {

    public ExtendedSimpleTextCellEditor(Composite parent) {
        super(parent);
    }

    @Override
    public void focusLost() {
        super.focusLost();
    }
}
