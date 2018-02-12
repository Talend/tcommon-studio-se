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
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public abstract class SaveAsGenericSchemaPushButton extends ExtendedPushButton {

    public SaveAsGenericSchemaPushButton(Composite parent, AbstractExtendedTableViewer extendedViewer) {
        super(parent, extendedViewer, Messages.getString("SaveAsGenericSchemaPushButton.saveAsSchema"), // Messages.getString("ExportPushButton.ExportButton.Tip"), //$NON-NLS-1$
                ImageProvider.getImage(EImage.SAVE_ICON)); //$NON-NLS-1$
    }

    @Override
    protected abstract Command getCommandToExecute();
}
