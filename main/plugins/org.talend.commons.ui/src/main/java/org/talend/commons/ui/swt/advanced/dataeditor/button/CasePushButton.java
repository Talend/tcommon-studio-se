// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;

public abstract class CasePushButton extends ExtendedPushButton {

    /**
     * DOC  CasePushButton constructor comment.
     *
     * @param parent
     * @param extendedControlViewer
     */
    public CasePushButton(Composite parent, AbstractExtendedControlViewer extendedControlViewer) {
        super(parent, extendedControlViewer,
                Messages.getString("CasePushButton.CaseButton.Tip"), ImageProvider.getImage(EImage.UPPERCASE_ICON)); //$NON-NLS-1$
    }

    protected abstract Command getCommandToExecute();

    /*
     * (non-Javadoc)
     *
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#getEnabledState()
     */
    @Override
    public boolean getEnabledState() {
        return super.getEnabledState() && !getExtendedControlViewer().isReadOnly();
    }

}
