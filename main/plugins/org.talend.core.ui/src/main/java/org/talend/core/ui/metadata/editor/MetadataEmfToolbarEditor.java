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
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.ui.metadata.extended.command.ExtendedTableResetDBTypesCommand;
import org.talend.core.ui.metadata.extended.command.MetadataEmfExportXmlCommand;
import org.talend.core.ui.metadata.extended.command.MetadataEmfImportXmlCommand;
import org.talend.core.ui.metadata.extended.command.MetadataEmfPasteCommand;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * TGU same purpose as MetadataToolbarEditorView but uses EMF model directly $Id: MetadataToolbarEditorView2.java,v 1.1
 * 2006/08/02 19:43:45 tguiu Exp $
 * 
 */
public class MetadataEmfToolbarEditor extends ExtendedToolbarView {

    private String dbmsId;

    /**
     * DOC amaumont MatadataToolbarEditor constructor comment.
     * 
     * @param parent
     * @param style
     * @param metadataEditorView
     */
    public MetadataEmfToolbarEditor(Composite parent, int style, AbstractExtendedTableViewer<MetadataColumn> extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    public MetadataEmfToolbarEditor(Composite parent, int style, AbstractExtendedTableViewer<MetadataColumn> extendedTableViewer,
            String dbmsId) {
        this(parent, style, extendedTableViewer);
        this.dbmsId = dbmsId;
        if (dbmsId != null) {
            resetDBTypesButton = createResetDBTypesPushButton(dbmsId);
            updateEnabledStateOfButtons();
        }
    }

    protected ResetDBTypesPushButton createResetDBTypesPushButton(final String dbmsId) {
        return new ResetDBTypesPushButtonForExtendedTable(toolbar, extendedTableViewer, dbmsId) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel) {
                return new ExtendedTableResetDBTypesCommand(extendedTableModel, dbmsId, extendedTableViewer);
            }

        };
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
                MetadataEmfTableEditor tableEditorModel = (MetadataEmfTableEditor) getExtendedTableViewer()
                        .getExtendedControlModel();
                return tableEditorModel.createNewMetadataColumn(dbmsId);
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
                return new MetadataEmfPasteCommand(extendedTableModel, indexWhereInsert);
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
                return new MetadataEmfExportXmlCommand((MetadataEmfTableEditor) extendedTableModel, file, dbmsId);
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
                return new MetadataEmfImportXmlCommand(extendedTableModel, file);
            }

        };
    }

}
