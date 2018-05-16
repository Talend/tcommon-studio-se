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
package org.talend.commons.ui.utils.workbench.preferences;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 *  
 * $Id: OneLineComboFieldEditor.java 2007-2-8,下午01:35:31 ftang $
 * 
 */
public class OneLineComboFieldEditor extends ComboFieldEditor {

    public OneLineComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
        super(name, labelText, entryNamesAndValues, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.workbench.preferences.ComboFieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
     * int)
     */
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = HORIZONTAL_GAP;
        parent.setLayout(layout);
        
        Control control = getLabelControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns;
        control.setLayoutData(gd);
        control = getComboBoxControl(parent);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = numColumns;
      
        
        control.setLayoutData(gd);
    }

}
