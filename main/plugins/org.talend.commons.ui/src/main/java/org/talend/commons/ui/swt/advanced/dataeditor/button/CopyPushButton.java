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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class CopyPushButton extends ExtendedPushButton {

    /**
     * DOC amaumont AddPushButton constructor comment.
     * 
     * @param parent
     * @param tooltip
     * @param image
     */
    public CopyPushButton(Composite parent, AbstractExtendedControlViewer extendedControlViewer) {
        super(parent, extendedControlViewer,
                Messages.getString("CopyPushButton.CopyButton.Tip"), ImageProvider.getImage(EImage.COPY_ICON)); //$NON-NLS-1$
    }

    protected void handleSelectionEvent(Event event) {
        Command commandToExecute = getCommandToExecute();
        if (commandToExecute != null) {
            commandToExecute.execute();
        }
    }

    protected abstract Command getCommandToExecute();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#getEnabledState()
     */
    @Override
    public boolean getEnabledState() {
        return super.getEnabledState() || !getExtendedControlViewer().isReadOnly();
    }

}
