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
package org.talend.commons.ui.swt.preferences;

import java.util.List;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <B> beans
 */
public abstract class AbstractPreferenceTableEditorView<B> extends FieldEditor {

    private AbstractDataTableEditorView<B> tableEditorView;

    private AbstractPreferencesHelperForTable preferencesHelper;

    /**
     * DOC amaumont AbstractPreferenceTableEditor constructor comment.
     * 
     * @param name
     * @param labelText
     * @param parent
     */
    public AbstractPreferenceTableEditorView(String name, String labelText, Composite parent,
            AbstractPreferencesHelperForTable preferencesHelper) {
        super(name, labelText, parent);
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    protected void createControl(Composite parent) {
        super.createControl(parent);
        tableEditorView = new AbstractDataTableEditorView<B>(parent, SWT.NONE, null, false, true, false) {

            @Override
            protected void createColumns(TableViewerCreator<B> tableViewerCreator, Table table) {
                AbstractPreferenceTableEditorView.this.createColumns(tableViewerCreator, table);
            }

            @Override
            protected ExtendedToolbarView initToolBar() {
                return AbstractPreferenceTableEditorView.this.initToolBar(this.getMainComposite(), this
                        .getExtendedTableViewer());
            }

        };
    }

    /**
     * DOC amaumont Comment method "initToolBar".
     * 
     * @param extendedTableViewier
     * @return
     */
    protected ExtendedToolbarView initToolBar(Composite parent, AbstractExtendedTableViewer<B> extendedTableViewier) {
        return null;
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doLoad() {
        tableEditorView.setExtendedTableModel(createModel());
        String stringList = getPreferenceStore().getString(getPreferenceName());
        loadData(stringList);
    }

    /**
     * DOC amaumont Comment method "loadData".
     * 
     * @param stringList
     */
    private void loadData(String stringList) {
        if (stringList != null && !"".equals(stringList)) { //$NON-NLS-1$
            List<B> beans = getPreferencesHelper().getBeans(stringList,
                    getPreferencesHelper().getBeanPropertyAccessors(), this.tableEditorView.getExtendedTableModel());
            tableEditorView.getExtendedTableModel().registerDataList(beans);
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doLoadDefault() {
        if (getTableViewerCreator() != null) {
            String stringList = getPreferenceStore().getDefaultString(getPreferenceName());
            loadData(stringList);
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doStore() {
        String s = getPreferencesHelper().getStorableString(getList(),
                getPreferencesHelper().getBeanPropertyAccessors());
        if (s != null) {
            getPreferenceStore().setValue(getPreferenceName(), s);
        }
    }

    /**
     * DOC amaumont Comment method "getList".
     * 
     * @return
     */
    private List<B> getList() {
        return tableEditorView.getExtendedTableModel().getBeansList();
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    public int getNumberOfControls() {
        return 1;
    }

    @Override
    protected void adjustForNumColumns(int numColumns) {
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
    }

    protected abstract void createColumns(TableViewerCreator<B> tableViewerCreator, Table table);

    public AbstractDataTableEditorView<B> getTableEditorView() {
        return this.tableEditorView;
    }

    public TableViewerCreator<B> getTableViewerCreator() {
        return getTableEditorView().getTableViewerCreator();
    }

    public AbstractPreferencesHelperForTable getPreferencesHelper() {
        return this.preferencesHelper;
    }

    public void setPreferencesHelper(AbstractPreferencesHelperForTable preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public abstract ExtendedTableModel<B> createModel();

}
