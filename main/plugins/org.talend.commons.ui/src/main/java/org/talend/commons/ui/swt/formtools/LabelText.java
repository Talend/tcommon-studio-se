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
package org.talend.commons.ui.swt.formtools;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class LabelText {

    private Label label;

    private Text text;

    /**
     * DOC smallet LabelText constructor comment.
     * 
     * @param label
     * @param text
     */
    public LabelText(Label label, Text text) {
        super();
        this.label = label;
        this.text = text;
    }

    public String getText() {
        return this.text.getText();
    }

    public void setText(String string) {
        this.text.setText(string);
    }

    public void setVisible(boolean visible) {
        this.label.setVisible(visible);
        this.text.setVisible(visible);
    }

    public void addModifyListener(ModifyListener listener) {
        this.text.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        this.text.removeModifyListener(listener);
    }

    public String getLabel() {
        return this.label.getText();
    }

    public Text getTextControl() {
        return this.text;
    }

    public Label getLabelControl() {
        return this.label;
    }
}
