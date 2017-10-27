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
package org.talend.commons.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MessagesWithActionComposite extends MessagesComposite {

    public String CSS_MESSAGES_WITH_ACTION_COMPOSITE_WARN = "MessagesWithActionComposite-Warn"; //$NON-NLS-1$

    public String CSS_MESSAGES_WITH_ACTION_COMPOSITE_ERROR = "MessagesWithActionComposite-Error"; //$NON-NLS-1$

    private Button actionBtn;

    public MessagesWithActionComposite(Composite parent, int style) {
        super(parent, style);
        actionBtn = new Button(this, SWT.PUSH);
        actionBtn.setLayoutData(new GridData());
        // default
        // updateActionButton(null, ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        // use text always
        updateActionButton("...", null); //$NON-NLS-1$
    }

    @Override
    public void updateTopMessages(String messages, int status) {
        super.updateTopMessages(messages, status);

        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.setBackground(backgroundColor);
        }
        setActionButtonVisible(!hidden);
        changeBackgroundColor(status);
    }

    protected void changeBackgroundColor(int status) {
        // inherit this method to use;
    }

    public void setActionButtonVisible(boolean show) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.setVisible(show);
            GridData layoutData = (GridData) actionBtn.getLayoutData();
            if (layoutData == null) {
                layoutData = new GridData();
            }
            layoutData.exclude = !show;
            actionBtn.setLayoutData(layoutData);
            actionBtn.getParent().layout();
            // if (show) {
            // FormData btnData = new FormData();
            // btnData.right = new FormAttachment(100, -H_SPACE_WIDTH);
            // btnData.top = new FormAttachment(0, V_SPACE_WIDTH);
            // actionBtn.setLayoutData(btnData);
            // }
        }
    }

    public void updateActionButton(String title, Image image) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            if (title != null) {
                actionBtn.setText(title);
            } else {
                actionBtn.setText(BLANK);
            }
            /*
             * Don't set the image, there is a different effect between win and linux.
             * 
             * in win, if set a non-null image first. then try to set a null image, there is a unknown image to be set
             * still. more strange.
             */
            // actionBtn.setImage(image);
        }
    }

    public void addActionListener(SelectionListener listener) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.addSelectionListener(listener);
        }
    }

    public void removeActionListener(SelectionListener listener) {
        if (actionBtn != null && !actionBtn.isDisposed()) {
            actionBtn.removeSelectionListener(listener);
        }
    }
}
