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
package org.talend.core.ui.preference.collector;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public class TalendDataCollectorUploadingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private StringFieldEditor lastUploadField;

    public TalendDataCollectorUploadingPreferencePage() {
        super();
        setPreferenceStore(CoreUIPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.getString("TalendDataCollectorUploadingPreferencePage_Description")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {

        Composite fieldEditorParent = getFieldEditorParent();

        IntegerFieldEditor uploadPeriodField = new IntegerFieldEditor(ITalendCorePrefConstants.DATA_COLLECTOR_UPLOAD_PERIOD,
                Messages.getString("TalendDataCollectorUploadingPreferencePage_UploadPeriod"), fieldEditorParent) { //$NON-NLS-1$

            @Override
            protected void createControl(Composite parent) {
                super.createControl(parent);
                Label label = new Label(parent, SWT.NONE);
                label.setText(Messages.getString("TalendDataCollectorUploadingPreferencePage_Days")); //$NON-NLS-1$
            }

            public int getNumberOfControls() {
                return 3;
            }

        };
        addField(uploadPeriodField);
        uploadPeriodField.setValidRange(0, Integer.MAX_VALUE);
        Text uploadPeriodText = uploadPeriodField.getTextControl(fieldEditorParent);
        GridData gridData1 = new GridData();
        gridData1.minimumWidth = 100;
        gridData1.widthHint = 100;
        uploadPeriodText.setLayoutData(gridData1);

        lastUploadField = new StringFieldEditor(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME,
                Messages.getString("TalendDataCollectorUploadingPreferencePage_LastUpload"), fieldEditorParent); //$NON-NLS-1$
        Text lastUploadText = lastUploadField.getTextControl(fieldEditorParent);
        lastUploadText.setEditable(false);
        lastUploadText.setEnabled(false);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        lastUploadText.setLayoutData(gridData);

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fieldEditorParent.setLayout(layout);

        addField(lastUploadField);
    }

    @Override
    protected void initialize() {
        super.initialize();
        // String lastTime = getPreferenceStore().getString(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME);
        // if(lastTime==null||"".equals(lastTime.trim())){
        // String current = TokenCollectorFactory.DATE_FORMAT.format(new Date());
        // lastUploadField.setStringValue(current);
        // getPreferenceStore().setValue(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME, current);
        // }
    }

}
