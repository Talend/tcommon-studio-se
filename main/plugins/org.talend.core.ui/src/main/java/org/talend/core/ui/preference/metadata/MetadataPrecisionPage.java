package org.talend.core.ui.preference.metadata;

import java.io.File;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.utils.PluginUtil;

public class MetadataPrecisionPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    IntegerFieldEditor xmlColumnsLimit;

    private static final String DISABLED_SCHEMES_KEY = "jdk.http.auth.tunneling.disabledSchemes";

    public MetadataPrecisionPage() {
        setPreferenceStore(CoreUIPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(ITalendCorePrefConstants.FORBIDDEN_MAPPING_LENGTH_PREC_LOGIC,
                "Forbidden mappingfile length and precision logic", getFieldEditorParent()));

        Composite fieldEditorParent = getFieldEditorParent();
        xmlColumnsLimit = new IntegerFieldEditor(ITalendCorePrefConstants.MAXIMUM_AMOUNT_OF_COLUMNS_FOR_XML,
                "Maximum amount of columns for xml", fieldEditorParent);
        Text textControl = xmlColumnsLimit.getTextControl(fieldEditorParent);
        textControl.setToolTipText("Set the maximum number of schema table columns the xml metadata support. ");
        xmlColumnsLimit.setValidRange(1, Short.MAX_VALUE);
        addField(xmlColumnsLimit);

        addField(new BooleanFieldEditor(ITalendCorePrefConstants.METADATA_PREFERENCE_PAGE_ENABLE_BASIC,
                Messages.getString("MetadataPreferencePage.EnableBasic.name"), getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub

    }

    public boolean performOk() {
        getPreferenceStore().setValue(ITalendCorePrefConstants.MAXIMUM_AMOUNT_OF_COLUMNS_FOR_XML, xmlColumnsLimit.getIntValue());
        boolean stored = getPreferenceStore().getBoolean(ITalendCorePrefConstants.METADATA_PREFERENCE_PAGE_ENABLE_BASIC);
        boolean ok = super.performOk();
        boolean checked = getPreferenceStore().getBoolean(ITalendCorePrefConstants.METADATA_PREFERENCE_PAGE_ENABLE_BASIC);
        if (stored != checked) {
            try {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                boolean confirm = MessageDialog.openConfirm(window.getShell(), "Confirm", //$NON-NLS-1$
                        Messages.getString("MetadataPreferencePage.MessageDialog.Restart")); //$NON-NLS-1$
                File configFile = PluginUtil.getStudioConfigFile();
                Properties configProperties = PluginUtil.readProperties(configFile);
                if (checked) {
                    configProperties.setProperty(DISABLED_SCHEMES_KEY, "");
                } else {
                    configProperties.remove(DISABLED_SCHEMES_KEY);
                }
                PluginUtil.saveProperties(configFile, configProperties, null);
                if (confirm) {
                    PlatformUI.getWorkbench().restart();
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return ok;
    }
}
