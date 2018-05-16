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
package org.talend.core.ui.metadata.celleditor.xpathquery;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;

/**
 * cli class global comment. Detailled comment
 */
public class XPathQueryMetadataTableEditorViewExt extends MetadataTableEditorView {

    public static final String ID_COLUMN_XPATH_QUERY = "ID_COLUMN_XPATH_QUERY"; //$NON-NLS-1$

    public XPathQueryMetadataTableEditorViewExt(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<IMetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, true);
    }

    @Override
    protected ExtendedToolbarView initToolBar() {
        return new XPathQueryMetadataToolbarEditorViewExt(getMainComposite(), SWT.NONE, getExtendedTableViewer());
    }

    @Override
    protected void createColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator, Table table) {
        super.createColumns(tableViewerCreator, table);
        // ////////////////////////////////////////////
        configureXPathQueryColumns(tableViewerCreator);
    }

    @SuppressWarnings("unchecked")
    private void configureXPathQueryColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator) {
        final TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
        column.setId(ID_COLUMN_XPATH_QUERY);
        column.setTitle(Messages.getString("XPathQueryMetadataTableEditorViewExt.xpathQuerys")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<XPathQueryMetadataColumnExt, String>() {

            @Override
            public String get(XPathQueryMetadataColumnExt bean) {
                return bean.getXpathQuery() == null ? "" : bean.getXpathQuery(); //$NON-NLS-1$
            }

            @Override
            public void set(XPathQueryMetadataColumnExt bean, String value) {
                bean.setXpathQuery(value);
            }
        });
        column.setModifiable(true);
        column.setWeight(20);
        column.setWidth(50);
    }

    @Override
    public XPathQueryMetadataTableEditorExt getMetadataTableEditor() {
        return (XPathQueryMetadataTableEditorExt) getExtendedTableModel();
    }
}
