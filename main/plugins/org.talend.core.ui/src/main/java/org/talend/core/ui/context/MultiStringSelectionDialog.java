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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.ui.i18n.Messages;

/**
 * A dialog that config the context value sets.
 * 
 */
public class MultiStringSelectionDialog extends ObjectSelectionDialog<String> {

    private static String defaultMesage = "Configure value of list"; //$NON-NLS-1$

    IInputValidator validator = null;

    @SuppressWarnings("restriction")
    public MultiStringSelectionDialog(Shell parentShell, String[] input) {
        super(parentShell, "Configure Values", defaultMesage, null); //$NON-NLS-1$
        setLabelProvider(getLabelProvider());
        if (input == null) {
            input = new String[0];
        }
        List<String> list = new ArrayList<String>(Arrays.asList(input));
        setData(list);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    private IInputValidator getInputValidator() {
        return new IInputValidator() {

            public String isValid(String newText) {
                return validateContextName(newText);
            }
        };
    }

    public List<String> getResultContexts() {
        return getData();
    }

    LabelProvider getLabelProvider() {
        return new LabelProvider();
    }

    public void createElement() {
        InputDialog inputDial = new InputDialog(getShell(), "New Value", //$NON-NLS-1$
                "Give a name for the new value", "", getInputValidator()); //$NON-NLS-1$ //$NON-NLS-2$

        inputDial.open();
        String returnValue = inputDial.getValue();
        if (returnValue == null) {
            return;
        }

        getAllValues().add(returnValue);
        refreshViewer();
    }

    private String validateContextName(String name) {
        if (name.length() == 0) {
            return Messages.getString("MultiStringSelectionDialog.nameEmpty"); //$NON-NLS-1$
        }
        return isContextExisting(name);
    }

    public List<String> getAllValues() {
        return getData();
    }

    public String[] getResultString() {
        return getAllValues().toArray(new String[0]);
    }

    private String isContextExisting(String name) {
        boolean exist = false;
        for (String value : getAllValues()) {
            if (value.equalsIgnoreCase(name)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            return Messages.getString("ContextProcessSection.30"); //$NON-NLS-1$
        } else {
            return null;
        }

    }

    protected void editSelectedElement() {
        String selectedValue = (String) (getSelection()).getFirstElement();
        InputDialog inputDial = new InputDialog(getShell(), "Rename", //$NON-NLS-1$
                "Give a new name for the value", "", getInputValidator()); //$NON-NLS-1$ //$NON-NLS-2$
        inputDial.open();
        String returnValue = inputDial.getValue();
        if (returnValue == null || returnValue.equals(selectedValue)) {
            return;
        }

        renameContext(selectedValue, returnValue);
        refreshViewer();
    }

    private void renameContext(String selectedValue, String newName) {
        int index = getAllValues().indexOf(selectedValue);
        if (index >= 0) {
            getAllValues().remove(index);
            getAllValues().add(index, newName);
        }
    }

    /**
     * Updates the modify buttons' enabled state based on the current seleciton.
     */
    protected void updateButtonAvailability() {
        super.updateButtonAvailability();
    }
}
