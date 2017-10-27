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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveDownPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveDownPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveUpPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveUpPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.extended.command.MetadataEmfPasteCommand;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileMetadataEmfToolbarEditor extends ExtendedToolbarView {

    private String dbmsId;

    private List<MetadataColumn> needUpdateInRelationMap = new ArrayList<MetadataColumn>();

    private XmlFileSchema2TreeLinker linker;

    public XmlFileMetadataEmfToolbarEditor(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    public XmlFileMetadataEmfToolbarEditor(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer,
            XmlFileSchema2TreeLinker linker) {
        super(parent, style, extendedTableViewer);
        this.linker = linker;
    }

    @Override
    protected AddPushButton createAddPushButton() {
        return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            protected Object getObjectToAdd() {
                MetadataEmfTableEditor tableEditorModel = (MetadataEmfTableEditor) getExtendedTableViewer()
                        .getExtendedControlModel();
                if (tableEditorModel.getMetadataTable() == null) {
                    tableEditorModel.setMetadataTable(ConnectionFactory.eINSTANCE.createMetadataTable());
                }
                MetadataColumn metadatacolumn = tableEditorModel.createNewMetadataColumn(dbmsId);
                return metadatacolumn;
            }

        };
    }

    @Override
    public PastePushButton createPastePushButton() {
        return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                return new MetadataEmfPasteCommand(extendedTableModel, indexWhereInsert);
            }

        };
    }

    // @Override
    // protected ExportPushButton createExportPushButton() {
    // return new ExportPushButtonForExtendedTable(toolbar, extendedTableViewer) {
    //
    // @Override
    // protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
    // return new MetadataEmfExportXmlCommand((MetadataEmfTableEditor) extendedTableModel, file, dbmsId);
    // }
    //
    // };
    // }

    // @Override
    // public ImportPushButton createImportPushButton() {
    // return new ImportPushButtonForExtendedTable(toolbar, extendedTableViewer) {
    //
    // @Override
    // protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
    // return new MetadataEmfImportXmlCommand(extendedTableModel, file);
    // }
    //
    // };
    // }

    @Override
    protected RemovePushButton createRemovePushButton() {
        return new RemovePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected void beforeCommandExecution() {
                List beanList = this.getExtendedTableViewer().getExtendedTableModel().getBeansList();
                for (int index : this.getExtendedTableViewer().getTable().getSelectionIndices()) {
                    MetadataColumn column = (MetadataColumn) beanList.get(index);
                    needUpdateInRelationMap.add(column);
                }
            }

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                //                String key = ""; //$NON-NLS-N$ //$NON-NLS-1$
                // IStructuredSelection selection = (IStructuredSelection)
                // linker.getMainui().getMetaTableViewer().getSelection();
                // if (selection.getFirstElement() != null && selection.getFirstElement() instanceof IModel) {
                // key = ((IModel) selection.getFirstElement()).getDisplayName();
                // }
                // if (!needUpdateInRelationMap.isEmpty()) {
                // for (MetadataColumn col : needUpdateInRelationMap) {
                // linker.getManager().updateRelationMapping(key, col, false);
                // }
                // }
                // linker.getMainui().redrawLinkers();
                linker.getBackgroundRefresher().refreshBackground();
            }

        };
    }

    @Override
    protected MoveDownPushButton createMoveDownPushButton() {
        return new MoveDownPushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                // linker.getMainui().redrawLinkers();
                // linker.getBackgroundRefresher().refreshBackground();
            }
        };
    }

    @Override
    protected MoveUpPushButton createMoveUpPushButton() {
        return new MoveUpPushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                // linker.getMainui().redrawLinkers();
                // linker.getBackgroundRefresher().refreshBackground();
            }

        };
    }

}
