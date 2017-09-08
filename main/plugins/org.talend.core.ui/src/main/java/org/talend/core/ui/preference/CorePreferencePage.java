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
package org.talend.core.ui.preference;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.model.xml.XmlArray;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.i18n.Messages;

/**
 * DOC msjian class global comment. Detailled comment <br/>
 * 
 * $Id: CorePreferencePage.java TDQ-3990 move from org.talend.core $
 * 
 */
public class CorePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    // private BooleanFieldEditor groupBySource =null;

    private List<FieldEditor> fields = new ArrayList<FieldEditor>();

    protected BooleanFieldEditor alwaysAskAtStartup;

    /**
     * Construct a new CorePreferencePage.
     */
    public CorePreferencePage() {
        super(GRID);

        // MOD msjian 2011-11-17 TDQ-3990: use the service to get the coreplugin(the coreplugin is differert between top
        // and tdq)
        IPreferenceStore store = null;
        // Set the preference store for the preference page.
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            ICoreService service = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
            store = service.getPreferenceStore();
        }
        // TDQ-3990 ~
        setPreferenceStore(store);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {

        Composite fieldEditorParent = getFieldEditorParent();
        DirectoryFieldEditor filePathTemp = new DirectoryFieldEditor(ITalendCorePrefConstants.FILE_PATH_TEMP,
                Messages.getString("CorePreferencePage.temporaryFiles"), fieldEditorParent); //$NON-NLS-1$
        addField(filePathTemp);
        FileFieldEditor javaInterpreter = new FileFieldEditor(ITalendCorePrefConstants.JAVA_INTERPRETER,
                Messages.getString("CorePreferencePage.javaInterpreter"), true, fieldEditorParent) {; //$NON-NLS-1$

            @Override
            protected boolean checkState() {
                return super.checkState();
            }
        };

        addField(javaInterpreter);

        IntegerFieldEditor previewLimit = new IntegerFieldEditor(ITalendCorePrefConstants.PREVIEW_LIMIT,
                Messages.getString("CorePreferencePage.previewLimit"), fieldEditorParent, 9); //$NON-NLS-1$
        previewLimit.setEmptyStringAllowed(false);
        previewLimit.setValidRange(1, 999999999);
        addField(previewLimit);

        BooleanFieldEditor runInMultiThread = new BooleanFieldEditor(ITalendCorePrefConstants.RUN_IN_MULTI_THREAD,
                Messages.getString("CorePreferencePage.runInMultiThread"), //$NON-NLS-1$
                fieldEditorParent);
        addField(runInMultiThread);

        DirectoryFieldEditor ireportPath = new DirectoryFieldEditor(ITalendCorePrefConstants.IREPORT_PATH,
                Messages.getString("CorePreferencePage.iReportPath"), fieldEditorParent); //$NON-NLS-1$
        addField(ireportPath);

        BooleanFieldEditor alwaysWelcome = new BooleanFieldEditor(ITalendCorePrefConstants.ALWAYS_WELCOME,
                Messages.getString("CorePreferencePage.alwaysWelcome.v2"), //$NON-NLS-1$
                fieldEditorParent);
        addField(alwaysWelcome);

        alwaysAskAtStartup = new BooleanFieldEditor(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP,
                Messages.getString("CorePreferencePage.alwaysAskAtStartup"), //$NON-NLS-1$
                fieldEditorParent);
        addField(alwaysAskAtStartup);

    }

    @Override
    protected void performApply() {
        super.performApply();
        PlatformUI.getPreferenceStore().setValue(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP,
                getPreferenceStore().getBoolean(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        if (ok) {
            XmlArray.setLimitToDefault();

            // CorePlugin.getDefault().getDesignerCoreService().switchToCurContextsView();
        }
        PlatformUI.getPreferenceStore().setValue(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP,
                getPreferenceStore().getBoolean(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP));
        return ok;
    }

    private void log(String s) {
        log.log(Level.INFO, s);
    }

    private static Logger log = Logger.getLogger(CorePreferencePage.class);

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
        getPreferenceStore().setValue(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP,
                PlatformUI.getPreferenceStore().getBoolean(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP));
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        getPreferenceStore().setValue(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP, true);
        PlatformUI.getPreferenceStore().setValue(ITalendCorePrefConstants.LOGON_DIALOG_ALWAYS_ASK_ME_AT_STARTUP, true);
        alwaysAskAtStartup.load();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
     */
    @Override
    public void addField(FieldEditor editor) {
        super.addField(editor);
        fields.add(editor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#checkState()
     */
    @Override
    protected void checkState() {
        super.checkState();
        int size = fields.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                FieldEditor editor = fields.get(i);
                if (!editor.isValid()) {
                    editor.setFocus();
                }
            }
        }
    }

}
