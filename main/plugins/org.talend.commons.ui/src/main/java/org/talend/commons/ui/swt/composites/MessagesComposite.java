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
package org.talend.commons.ui.swt.composites;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.utils.image.ColorUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MessagesComposite extends Composite {

    protected static final int H_SPACE_WIDTH = 3;

    protected static final int V_SPACE_WIDTH = 5;

    protected static final String BLANK = ""; //$NON-NLS-1$

    protected Label messageImage, messageLabel;

    protected boolean hidden = true;

    protected Color backgroundColor, foregroundColor;

    public MessagesComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(3, false));

        messageImage = new Label(this, SWT.NONE);
        // FormData imageData = new FormData();
        // imageData.left = new FormAttachment(0, H_SPACE_WIDTH);
        // imageData.top = new FormAttachment(0, V_SPACE_WIDTH);
        // messageImage.setLayoutData(imageData);

        messageLabel = new Label(this, SWT.NONE);
        // FormData labelData = new FormData();
        // labelData.left = new FormAttachment(messageImage, H_SPACE_WIDTH);
        // labelData.top = new FormAttachment(0, V_SPACE_WIDTH);
        // messageLabel.setLayoutData(labelData);
        messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    }

    /**
     * 
     * DOC ggu Comment method "updateTopMessages".
     * 
     * @param messages
     * @param status is one of IStatus.INFO, IStatus.WARNING, IStatus.ERROR
     */
    public void updateTopMessages(String messages, int status) {

        hidden = true;

        if (messageLabel != null && !messageLabel.isDisposed()) {
            if (messages != null && !messages.isEmpty()) {
                messageLabel.setText(messages);
                messageLabel.setToolTipText(messages);
                hidden = false;
            } else { // clean the message and hide top message area.
                messageLabel.setText(BLANK);
                messageLabel.setToolTipText(BLANK);
            }
        }

        backgroundColor = null;
        foregroundColor = null;

        Image image = null;
        if (!hidden) {
            switch (status) {
            case IStatus.INFO:
                image = ImageProvider.getImage(EImage.INFORMATION_ICON);
                break;
            case IStatus.WARNING:
                image = ImageProvider.getImage(EImage.WARNING_ICON);
                backgroundColor = ColorUtils.getCacheColor(new RGB(255, 175, 10));
                // foregroundColor=
                break;
            case IStatus.ERROR:
                image = ImageProvider.getImage(EImage.ERROR_ICON);
                backgroundColor = ColorUtils.getCacheColor(new RGB(238, 64, 0));
                // foregroundColor=
                break;
            default:
            }
        }
        setBackground(backgroundColor);

        if (messageImage != null && !messageImage.isDisposed()) {
            messageImage.setImage(image);
            messageImage.setBackground(backgroundColor);
        }
        if (messageLabel != null && !messageLabel.isDisposed()) {
            messageLabel.setBackground(backgroundColor);
            messageLabel.setForeground(foregroundColor);
        }

    }

}
