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
package org.talend.core.ui.preference.collector;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.token.TokenCollectorFactory;

/**
 * ggu class global comment. Detailled comment
 */
public class TalendDataCollectorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public TalendDataCollectorPreferencePage() {
        setPreferenceStore(CoreUIPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.getString("TalendDataCollectorPreferencePage_Description")); //$NON-NLS-1$
    }

    @Override
    public void init(IWorkbench workbench) {

    }

    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED,
                Messages.getString("TalendDataCollectorPreferencePage_EnableCapture"), getFieldEditorParent())); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        boolean valueBeforeOk = preferenceStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED);

        boolean ok = super.performOk();

        if (valueBeforeOk != preferenceStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED)) {
            TokenCollectorFactory.getFactory().send();
        }
        return ok;
    }
}
