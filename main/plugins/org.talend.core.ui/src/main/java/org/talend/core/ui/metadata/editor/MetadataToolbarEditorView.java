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
package org.talend.core.ui.metadata.editor;

import java.io.File;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ExportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ExportPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ResetDBTypesPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ResetDBTypesPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.SaveAsGenericSchemaPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.SaveAsGenericSchemaPushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.metadata.extended.command.ExtendedTableResetDBTypesCommand;
import org.talend.core.ui.metadata.extended.command.MetadataExportXmlCommand;
import org.talend.core.ui.metadata.extended.command.MetadataImportXmlCommand;
import org.talend.core.ui.metadata.extended.command.MetadataPasteCommand;
import org.talend.core.ui.metadata.extended.command.SaveAsGenericSchemaCommand;
import org.talend.repository.model.RepositoryNode;

/**
 * $Id: MetadataToolbarEditorView.java 46952 2010-08-18 08:41:09Z nrousseau $
 * 
 */
public class MetadataToolbarEditorView extends ExtendedToolbarView {

    /**
     * DOC amaumont MetadataToolbarEditorView constructor comment.
     * 
     * @param parent
     * @param style
     * @param extendedTableViewer
     */
    public MetadataToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    public MetadataToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer, String dbmsId) {
        super(parent, style, extendedTableViewer);
        if (dbmsId != null) {
            resetDBTypesButton = createResetDBTypesPushButton(dbmsId);
        }
        RepositoryNode node = (RepositoryNode) CoreRuntimePlugin.getInstance().getRepositoryService()
                .getRootRepositoryNode(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
        if (node != null) {
            saveAsGenericSchemaButton = createSaveAsGenericSchemaButton(dbmsId);
        }
        updateEnabledStateOfButtons();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createAddPushButton()
     */
    @Override
    protected AddPushButton createAddPushButton() {
        return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            protected Object getObjectToAdd() {
                MetadataTableEditor tableEditorModel = (MetadataTableEditor) getExtendedTableViewer().getExtendedControlModel();
                return tableEditorModel.createNewMetadataColumn();
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public PastePushButton createPastePushButton() {
        return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                return new MetadataPasteCommand(extendedTableModel, indexWhereInsert);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createExportPushButton()
     */
    @Override
    protected ExportPushButton createExportPushButton() {
        return new ExportPushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
                return new MetadataExportXmlCommand((MetadataTableEditor) extendedTableModel, file);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public ImportPushButton createImportPushButton() {
        return new ImportPushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
                return new MetadataImportXmlCommand(extendedTableModel, file);
            }

        };
    }

    protected ResetDBTypesPushButton createResetDBTypesPushButton(final String dbmsId) {
        return new ResetDBTypesPushButtonForExtendedTable(toolbar, extendedTableViewer, dbmsId) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel) {
                return new ExtendedTableResetDBTypesCommand(extendedTableModel, dbmsId, extendedTableViewer);
            }
        };
    }

    protected SaveAsGenericSchemaPushButton createSaveAsGenericSchemaButton(final String dbmsId) {
        return new SaveAsGenericSchemaPushButtonForExtendedTable(toolbar, extendedTableViewer, dbmsId) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, String dbmsId) {
                return new SaveAsGenericSchemaCommand(extendedTableModel, dbmsId);
            }

        };
    }
}
