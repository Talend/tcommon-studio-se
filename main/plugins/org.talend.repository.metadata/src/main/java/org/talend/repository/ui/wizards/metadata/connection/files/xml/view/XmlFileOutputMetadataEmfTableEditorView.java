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

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.repository.metadata.i18n.Messages;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileOutputMetadataEmfTableEditorView extends AbstractDataTableEditorView<MetadataColumn> {

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME";

    // private TableViewerCreatorColumn nameColumn;

    private XmlFileSchema2TreeLinker linker;

    public XmlFileOutputMetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle,
            boolean initGraphicsComponents) {
        super(parentComposite, mainCompositeStyle, initGraphicsComponents);
    }

    @Override
    protected void createColumns(TableViewerCreator tableViewerCreator, Table table) {
        tableViewerCreator.setReadOnly(this.readOnly);

        configureNameColumn(tableViewerCreator);
        // if (showDbColumnName) {
        // configureDbColumnName(tableViewerCreator);
        // }
    }

    protected void configureNameColumn(TableViewerCreator<MetadataColumn> tableViewerCreator) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(ID_COLUMN_NAME);
        column.setTitle("Schema List"); //$NON-NLS-1$
        column.setWidth(100);
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.ColumnTitle")); //$NON-NLS-1$

        column.setBeanPropertyAccessors(getLabelAccessor());
        final Image imageKey = ImageProvider.getImage(EImage.KEY_ICON);
        final Image imageEmpty = org.talend.commons.ui.runtime.image.ImageProvider.getImage(EImage.EMPTY);
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreator.getTable());
        column.setCellEditor(cellEditor);
        cellEditor.addListener(new DialogErrorForCellEditorListener(cellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                return validateColumnName(newValue, beanPosition);
            }

        });

        column.setImageProvider(new IColumnImageProvider() {

            public Image getImage(Object element) {
                if (getKeyAccesor().get((MetadataColumn) element)) {
                    return imageKey;
                } else {
                    return imageEmpty;
                }
            }

        });
        column.setWeight(25);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(20);
        // nameColumn = column;
    }

    protected String validateColumnName(String newValue, int beanPosition) {
        return getMetadataEditor().validateColumnName(newValue, beanPosition);
    }

    public MetadataEmfTableEditor getMetadataEditor() {
        return (MetadataEmfTableEditor) getExtendedTableModel();
    }

    public void setMetadataEditor(MetadataEmfTableEditor metadataTableEditor) {
        setExtendedTableModel(metadataTableEditor);
    }

    protected IBeanPropertyAccessors<MetadataColumn, String> getLabelAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            public String get(MetadataColumn bean) {
                return bean.getLabel();
            }

            public void set(MetadataColumn bean, String value) {

                if (bean.getLabel().equals(bean.getOriginalField())) {
                    // bean.setOriginalField(value);
                }
                bean.setLabel(value);
            }

        };
    }

    protected IBeanPropertyAccessors<MetadataColumn, Boolean> getKeyAccesor() {
        return new IBeanPropertyAccessors<MetadataColumn, Boolean>() {

            public Boolean get(MetadataColumn bean) {
                return new Boolean(bean.isKey());
            }

            public void set(MetadataColumn bean, Boolean value) {
                bean.setKey(value);
            }

        };
    }

    @Override
    protected ExtendedToolbarView initToolBar() {
        // return new XmlFileMetadataEmfToolbarEditor(getMainComposite(), SWT.NONE, this.getExtendedTableViewer(),
        // linker);
        return null;
    }

    public XmlFileSchema2TreeLinker getLinker() {
        return this.linker;
    }

    public void setLinker(XmlFileSchema2TreeLinker linker) {
        this.linker = linker;
    }

}
