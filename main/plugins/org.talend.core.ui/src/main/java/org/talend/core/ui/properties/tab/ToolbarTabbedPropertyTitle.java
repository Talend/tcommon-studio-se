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
package org.talend.core.ui.properties.tab;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyTitle;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ToolbarTabbedPropertyTitle extends TabbedPropertyTitle {

    private Map<Integer, Button> buttons = new HashMap<Integer, Button>();

    public ToolbarTabbedPropertyTitle(Composite parent, TabbedPropertySheetWidgetFactory factory) {
        super(parent, factory);

        createButtons();
        resetLayoutData();
        addListeners();
        setButtonsHidden(true);
    }

    protected void createButtons() {
        //
    }

    protected Button createButton(int id, String label) {
        Button button = new Button(this, SWT.PUSH);
        button.setText(label);
        button.setFont(JFaceResources.getDialogFont());
        button.setData(new Integer(id));
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                buttonPressed(((Integer) event.widget.getData()).intValue());
            }
        });
        buttons.put(new Integer(id), button);
        return button;
    }

    protected void resetLayoutData() {
        Control[] children = this.getChildren();
        for (int i = children.length - 1; i >= 0; i--) {
            Control c = children[i];
            FormData formData = new FormData();
            if (i == children.length - 1) { // last one
                formData.right = new FormAttachment(100, 0);
            } else {
                Control afterControl = children[i + 1];
                formData.right = new FormAttachment(afterControl, -8);

            }
            if (c instanceof CLabel) {
                formData.left = new FormAttachment(0, 0);
                formData.top = new FormAttachment(0, 0);
                formData.bottom = new FormAttachment(100, 0);
            }
            c.setLayoutData(formData);
        }
    }

    protected void addListeners() {
        CLabel label = null;
        Control[] children = this.getChildren();
        if (children != null) {
            for (Control element : children) {
                if (element instanceof CLabel) {
                    label = (CLabel) element;
                    break;
                }
            }
        }
        if (label != null && !label.isDisposed()) {
            final CLabel eventLabel = label;
            Listener visibleListener = new Listener() {

                @Override
                public void handleEvent(Event event) {
                    if (event.widget == eventLabel) {
                        if (event.type == SWT.Show) {
                            setButtonsHidden(false);
                        } else if (event.type == SWT.Hide) {
                            setButtonsHidden(true);
                        }
                    }
                }

            };
            label.addListener(SWT.Show, visibleListener);
            label.addListener(SWT.Hide, visibleListener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyTitle#drawTitleBackground(org.eclipse.swt.
     * events.PaintEvent)
     */
    @Override
    protected void drawTitleBackground(PaintEvent e) {
        super.drawTitleBackground(e);

    }

    protected Button getButton(int buttonId) {
        return this.buttons.get(buttonId);
    }

    protected void buttonPressed(int buttonId) {
        // actions
    }

    public void setButtonsHidden(boolean hidden) {
        for (Integer id : this.buttons.keySet()) {
            Button button = this.buttons.get(id);
            if (button != null && !button.isDisposed()) {
                button.setVisible(!hidden);
            }
        }
    }
}
