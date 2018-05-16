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
package org.talend.repository.metadata.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class TeradataPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

    @Override
    public void init(IWorkbench workbench) {
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(IDatabasePrefConstants.USE_SQL_MODEL, false); //$NON-NLS-1$
        setPreferenceStore(store);
    }

    @Override
    protected void createFieldEditors() {
        RadioGroupFieldEditor sqlModelField = new RadioGroupFieldEditor(
                IDatabasePrefConstants.STANDARD_SQL,
                Messages.getString("DatabaseForm.sqlMode"), 2, new String[][] { { Messages.getString("DatabaseForm.yes"), "" + 24 }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
                        { Messages.getString("DatabaseForm.no"), "" + 32 } }, getFieldEditorParent()); //$NON-NLS-1$ //$NON-NLS-2$
        addField(sqlModelField);
    }

}
