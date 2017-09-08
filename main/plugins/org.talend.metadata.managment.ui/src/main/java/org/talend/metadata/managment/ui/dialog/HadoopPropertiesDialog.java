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
 * created by ycbai on 2014年12月23日 Detailled comment
 *
 */
public class HadoopPropertiesDialog extends PropertiesDialog {

    public HadoopPropertiesDialog(Shell parentShell, List<Map<String, Object>> initProperties) {
        super(parentShell, null, initProperties);
    }

    public HadoopPropertiesDialog(Shell parentShell, List<Map<String, Object>> initPropertiesOfParent,
            List<Map<String, Object>> initProperties) {
        super(parentShell, initPropertiesOfParent, initProperties);
    }

    @Override
    protected String getTitle() {
        return Messages.getString("HadoopPropertiesDialog.title"); //$NON-NLS-1$
    }

    @Override
    protected String getParentTitle() {
        return Messages.getString("HadoopPropertiesDialog.parentProperties.title"); //$NON-NLS-1$
    }

    @Override
    protected String getDesc() {
        return Messages.getString("HadoopPropertiesDialog.desc"); //$NON-NLS-1$
    }

}
