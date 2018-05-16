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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.view;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.talend.commons.ui.swt.drawing.background.IBackgroundRefresher;
import org.talend.commons.ui.swt.linking.TreeToTablesLinker;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class XmlTree2SchemaLinker extends TreeToTablesLinker<Object, Object> {

    private XmlFileSchema2TreeLinker delegateLinker;

    private TableViewer tableViewer;

    private Table table;

    private TreeViewer treeViewer;

    private Tree tree;

    private XmlFileConnection connection;

    public XmlTree2SchemaLinker(Composite commonParent) {
        super(commonParent);
    }

    public void init(TreeViewer treeViewer, TableViewer tableViewer) {
        this.treeViewer = treeViewer;
        this.tableViewer = tableViewer;
        this.tree = treeViewer.getTree();
        this.table = tableViewer.getTable();

        init(tree, new Table[] { table }, delegateLinker.getBackgroundRefresher());
        initListeners();
    }

    public void init(final Tree tree, Table[] tables, IBackgroundRefresher backgroundRefresher) {
        super.init(tree, tables, delegateLinker, backgroundRefresher);
        tree.removeSelectionListener(getLinkableTree().getSelectionListener());
    }

    private void initListeners() {
        new XmlTree2SchemaDragAndDropHandler(this);
    }

    public void valueChanged(Widget widget) {
        delegateLinker.onXPathValueChanged(widget);
    }

    public void drawBackground(GC gc) {
        delegateLinker.drawBackground(gc);
    }

    public void updateLinksStyleAndControlsSelection(Control currentControl, boolean lastOne) {
        delegateLinker.updateLinksStyleAndControlsSelection(currentControl, lastOne);
    }

    public void updateConnection() {
        delegateLinker.getForm().updateConnection();
    }

    public void updateFormStatus() {
        delegateLinker.getForm().updateStatus();
    }

    public XmlFileConnection getConnection() {
        return this.connection;
    }

    public void setConnection(XmlFileConnection connection) {
        this.connection = connection;
    }

    public TableViewer getTableViewer() {
        return this.tableViewer;
    }

    public TreeViewer getTreeViewer() {
        return this.treeViewer;
    }

    public XmlFileSchema2TreeLinker getDelegateLinker() {
        return this.delegateLinker;
    }

    public void setDelegateLinker(XmlFileSchema2TreeLinker delegateLinker) {
        this.delegateLinker = delegateLinker;
    }
}
