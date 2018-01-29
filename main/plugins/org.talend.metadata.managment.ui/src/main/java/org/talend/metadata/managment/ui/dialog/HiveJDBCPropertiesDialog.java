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
package org.talend.metadata.managment.ui.dialog;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.talend.metadata.managment.ui.i18n.Messages;

/**
 * 
 * created by ycbai on 2015年1月4日 Detailled comment
 *
 */
public class HiveJDBCPropertiesDialog extends PropertiesDialog {

    public HiveJDBCPropertiesDialog(Shell parentShell, List<Map<String, Object>> initProperties) {
        super(parentShell, null, initProperties);
    }

    public HiveJDBCPropertiesDialog(Shell parentShell, List<Map<String, Object>> initPropertiesOfParent,
            List<Map<String, Object>> initProperties) {
        super(parentShell, initPropertiesOfParent, initProperties);
    }

    @Override
    protected String getTitle() {
        return Messages.getString("HiveJDBCPropertiesDialog.title"); //$NON-NLS-1$
    }

    @Override
    protected String getDesc() {
        return Messages.getString("HiveJDBCPropertiesDialog.desc"); //$NON-NLS-1$
    }

}
