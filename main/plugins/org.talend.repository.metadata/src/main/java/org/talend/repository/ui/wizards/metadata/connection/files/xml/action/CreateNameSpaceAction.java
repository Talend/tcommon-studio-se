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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.action;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.metadata.managment.ui.wizard.metadata.xml.dialog.NameSpaceDialog;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.NameSpaceNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlStepForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class CreateNameSpaceAction extends SelectionProviderAction {

    private TreeViewer xmlViewer;

    private AbstractXmlStepForm form;

    public CreateNameSpaceAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public CreateNameSpaceAction(TreeViewer xmlViewer, AbstractXmlStepForm form, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.form = form;
    }

    @Override
    public void run() {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null) {
            createChildNode(node);
        }
        form.updateConnection();
    }

    private void createChildNode(FOXTreeNode node) {
        String label = null;
        String defaultValue = null;
        while (!StringUtil.validateLabelForNameSpace(label) || !StringUtil.validateLabelForFixedValue(defaultValue)) {
            NameSpaceDialog nsDialog = new NameSpaceDialog(null);
            int status = nsDialog.open();
            if (status == nsDialog.OK) {
                defaultValue = nsDialog.getNSValue();
                if (defaultValue != null) {
                    defaultValue = defaultValue.trim();
                }
                label = nsDialog.getPrefix().trim();
            }
            if (status == nsDialog.CANCEL) {
                return;
            }
        }
        FOXTreeNode child = new NameSpaceNode(label);
        child.setDefaultValue(defaultValue);
        // add by wzhang. set the row name
        child.setRow(node.getRow());
        node.addChild(child);
        this.xmlViewer.refresh();
        xmlViewer.expandAll();
        form.redrawLinkers();
    }

}
