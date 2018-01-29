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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.NameSpaceNode;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlStepForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class RemoveGroupAction extends SelectionProviderAction {

    private TreeViewer xmlViewer;

    private AbstractXmlStepForm form;

    public RemoveGroupAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public RemoveGroupAction(TreeViewer xmlViewer, String text, AbstractXmlStepForm form) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.form = form;
    }

    @Override
    public void run() {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        node.setGroup(false);
        xmlViewer.refresh();
        form.updateStatus();
        form.updateConnection();
    }

    @Override
    public void selectionChanged(IStructuredSelection selection) {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node == null) {
            this.setEnabled(false);
            return;
        }
        if (node instanceof Attribute) {
            this.setEnabled(false);
            return;
        }

        if (node instanceof NameSpaceNode) {
            this.setEnabled(false);
            return;
        }
        this.setEnabled(node.isGroup());
    }
}
