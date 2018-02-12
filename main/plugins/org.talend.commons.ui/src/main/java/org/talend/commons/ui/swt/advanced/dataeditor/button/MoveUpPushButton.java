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
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableMoveCommand;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class MoveUpPushButton extends ExtendedPushButton {

    /**
     * DOC amaumont AddPushButton constructor comment.
     * 
     * @param parent
     * @param tooltip
     * @param image
     */
    public MoveUpPushButton(Composite parent, AbstractExtendedControlViewer extendedControlViewer) {
        super(parent, extendedControlViewer,
                Messages.getString("MoveUpPushButton.MoveUpButton.Tip"), ImageProvider.getImage(EImage.UP_ICON)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.macrotable.control.ExtendedPushButton#getCommandToExecute()
     */
    @Override
    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer viewer = (AbstractExtendedTableViewer) getExtendedControlViewer();
        return new ExtendedTableMoveCommand(viewer.getExtendedTableModel(), true, viewer.getTableViewerCreator()
                .getTable().getSelectionIndices());
    }

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
