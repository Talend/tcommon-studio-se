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
package org.talend.metadata.managment.ui.dialog;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.props.PropertiesTableView;
import org.talend.metadata.managment.ui.props.SparkPropertiesFieldModel;

/**
 * created by hcyi on Feb 17, 2017 Detailled comment
 *
 */
public class SparkPropertiesDialog extends PropertiesDialog {

    public SparkPropertiesDialog(Shell parentShell, List<Map<String, Object>> initProperties) {
        super(parentShell, null, initProperties);
    }

    public SparkPropertiesDialog(Shell parentShell, List<Map<String, Object>> initPropertiesOfParent,
            List<Map<String, Object>> initProperties) {
        super(parentShell, initPropertiesOfParent, initProperties);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite compositeTable = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.horizontalSpacing = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        compositeTable.setLayout(layout);
        compositeTable.setLayoutData(new GridData(GridData.FILL_BOTH));

        SparkPropertiesFieldModel model = new SparkPropertiesFieldModel(initProperties, ""); //$NON-NLS-1$
        propertiesTableView = new PropertiesTableView(compositeTable, model);
        propertiesTableView.setReadOnly(isReadOnly());
        Composite fieldTableEditorComposite = propertiesTableView.getMainComposite();
        GridData tableData = new GridData(GridData.FILL_BOTH);
        tableData.heightHint = 200;
        fieldTableEditorComposite.setLayoutData(tableData);
        return parent;
    }

    @Override
    protected String getLabelTitle() {
        return ""; //$NON-NLS-1$
    }

    @Override
    protected String getTitle() {
        return Messages.getString("SparkPropertiesDialog.title"); //$NON-NLS-1$
    }

    @Override
    protected String getParentTitle() {
        return Messages.getString("SparkPropertiesDialog.parentProperties.title"); //$NON-NLS-1$
    }

    @Override
    protected String getDesc() {
        return Messages.getString("SparkPropertiesDialog.desc"); //$NON-NLS-1$
    }
}
