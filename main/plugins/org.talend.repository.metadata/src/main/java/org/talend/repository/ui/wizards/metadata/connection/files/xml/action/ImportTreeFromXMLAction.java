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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.metadata.managment.ui.dialog.RootNodeSelectDialog;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlStepForm;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.XmlFileOutputStep2Form;

/**
 * wzhang class global comment. Detailled comment
 */
public class ImportTreeFromXMLAction extends SelectionProviderAction {

    private TreeViewer xmlViewer;

    private AbstractXmlStepForm form;

    public ImportTreeFromXMLAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public ImportTreeFromXMLAction(TreeViewer xmlViewer, AbstractXmlStepForm form, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.form = form;
    }

    @Override
    public void run() {
        List<FOXTreeNode> newInput = new ArrayList<FOXTreeNode>();

        String filePath = getFilePath();
        if (filePath == null) {
            return;
        }

        boolean changed = true;
        try {
            if (XmlUtil.isXSDFile(filePath)) {
                XSDSchema xsdSchema = TreeUtil.getXSDSchema(filePath);
                List<ATreeNode> list = new XSDPopulationUtil2().getAllRootNodes(xsdSchema);
                if (list.size() > 1) {
                    RootNodeSelectDialog dialog = new RootNodeSelectDialog(xmlViewer.getControl().getShell(), list);
                    if (dialog.open() == IDialogConstants.OK_ID) {
                        ATreeNode selectedNode = dialog.getSelectedNode();
                        newInput = TreeUtil.getFoxTreeNodesByRootNode(xsdSchema, selectedNode);
                        if (form instanceof XmlFileOutputStep2Form) {
                            ((XmlFileOutputStep2Form) form).resetRootCombo();
                        }
                        changed = true;
                    } else {
                        changed = false;
                    }
                } else {
                    newInput = TreeUtil.getFoxTreeNodesByRootNode(xsdSchema, list.get(0));
                    changed = true;
                }
            } else {
                newInput = TreeUtil.getFoxTreeNodes(filePath);
                changed = true;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        if (newInput.size() == 0) {
            return;
        }

        if (changed) {
            List<FOXTreeNode> treeData = form.getTreeData();
            treeData.clear();
            treeData.addAll(newInput);
            xmlViewer.setInput(treeData);
            xmlViewer.refresh();
            xmlViewer.expandAll();
            form.updateStatus();
            form.redrawLinkers();
            form.updateConnection();
        }
    }

    private String getFilePath() {
        FileDialog f = new FileDialog(xmlViewer.getControl().getShell());
        String file = f.open();
        return file;
    }

    @Override
    public void selectionChanged(IStructuredSelection selection) {
        this.setEnabled(true);
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null) {
            form.setSelectedText(node.getLabel());
        }
    }

}
