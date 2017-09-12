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
package org.talend.repository.metadata.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.ui.utils.workbench.preferences.ComboFieldEditor;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
    
    public DatabasePreferencePage() {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench) {
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(IDatabasePrefConstants.SQL_SYNTAX, "SQL 92"); //$NON-NLS-1$
        store.setDefault(IDatabasePrefConstants.STRING_QUOTE, "\""); //$NON-NLS-1$
        store.setDefault(IDatabasePrefConstants.NULL_CHAR, "000"); //$NON-NLS-1$
        
        setPreferenceStore(store);
    }

    @Override
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        createForMapper(parent);
        parent.pack();
    }
    
    protected Composite createForMapper(Composite parent) {
        Group group = createGroup(parent);
        group.setText("Database properties");
        Composite composite = createComposite(group);
        createFieldEditors2(composite);
        GridLayout layout = createLayout();
        composite.setLayout(layout);
//        
//        
//        RadioGroupFieldEditor standardSQLField = new RadioGroupFieldEditor(
//                IDatabasePrefConstants.STANDARD_SQL,
//                "", 2, new String[][] { { Messages.getString("DatabaseForm.StandardSQL"), "" + 24 }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
//                        { Messages.getString("DatabaseForm.SystemSQL"), "" + 32 } }, getFieldEditorParent()); //$NON-NLS-1$ //$NON-NLS-2$
//        addField(standardSQLField);
//        
//        
//        RadioGroupFieldEditor sqlModelField = new RadioGroupFieldEditor(
//                IDatabasePrefConstants.STANDARD_SQL,
//                Messages.getString("DatabaseForm.sqlMode"), 2, new String[][] { { Messages.getString("DatabaseForm.yes"), "" + 24 }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
//                        { Messages.getString("DatabaseForm.no"), "" + 32 } }, getFieldEditorParent()); //$NON-NLS-1$ //$NON-NLS-2$
//        addField(sqlModelField);
        return group;
    }
    
    protected GridLayout createLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 8;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 8;
        return layout;
    }
    
    protected void addFontAndColorFieldsForMapper(Composite composite) {
        createFieldEditors2(composite);
    }
    
    public void createFieldEditors2(Composite composite) {
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        store.getString(IDatabasePrefConstants.STRING_QUOTE);

        SQL_SYNTAX[] syntaxs = SQL_SYNTAX.values();
        String[][] strComboValues = new String[syntaxs.length][2];

        for (int i = 0; i < syntaxs.length; i++) {
            strComboValues[i][0] = syntaxs[i].getDisplayName();
            strComboValues[i][1] = syntaxs[i].getName();
        }

        ComboFieldEditor syntaxField = new ComboFieldEditor(IDatabasePrefConstants.SQL_SYNTAX,
                "SQL Syntax", strComboValues, composite); 
        addField(syntaxField);
        
        StringFieldEditor quoteField = new StringFieldEditor(IDatabasePrefConstants.STRING_QUOTE,
                "String Quote", composite);
        addField(quoteField);
        
        StringFieldEditor charField = new StringFieldEditor(IDatabasePrefConstants.NULL_CHAR,
                "Null Char", composite);
        addField(charField);
    }
    
    protected Composite createComposite(Group group) {
        Composite composite = new Composite(group, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);
        return composite;
    }
    
    protected Group createGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 3;
        layoutData.verticalSpan = 15;
        group.setLayoutData(layoutData);
        group.setLayout(new GridLayout(3, false));
        return group;
    }

}
