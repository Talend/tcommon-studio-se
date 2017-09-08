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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class SelectContextVariablesPushButton extends ExtendedPushButton {

    public SelectContextVariablesPushButton(Composite parent, AbstractExtendedTableViewer extendedViewer) {
        this(parent, extendedViewer, ImageProvider.getImage(EImage.EMPTY));
    }

    public SelectContextVariablesPushButton(Composite parent, AbstractExtendedTableViewer extendedViewer, Image image) {
        super(parent, extendedViewer, Messages.getString("SelectContextVariablesPushButton.Label"), image); //$NON-NLS-1$
    }

    @Override
    protected Command getCommandToExecute() {
        return null;
    }

}
