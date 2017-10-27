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
package org.talend.core.ui.metadata.extended.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.MetadataColumnsAndDbmsId;
import org.talend.core.model.metadata.MetadataSchema;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.xml.sax.SAXException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataEmfImportXmlCommand extends Command {

    private File file;

    private ExtendedTableModel extendedTableModel;

    private List<MetadataColumn> removed;

    private List<MetadataColumn> added;

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTableModel
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataEmfImportXmlCommand(ExtendedTableModel extendedTableModel, File file) {
        super();
        this.file = file;
        this.extendedTableModel = extendedTableModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#execute()
     */
    @Override
    public void execute() {
        try {
            // // load the schema
            removed = new ArrayList<MetadataColumn>(extendedTableModel.getBeansList());
            extendedTableModel.removeAll(removed);

            MetadataColumnsAndDbmsId<MetadataColumn> metadataColumnsAndDbmsId = MetadataSchema
                    .loadMetadataColumnsAndDbmsIdFromFile(file);
            added = metadataColumnsAndDbmsId.getMetadataColumns();
            extendedTableModel.addAll(added);
            if (extendedTableModel instanceof MetadataEmfTableEditor) {
                MetadataEmfTableEditor editor = (MetadataEmfTableEditor) extendedTableModel;
                String dbmsId = metadataColumnsAndDbmsId.getDbmsId();
                if (dbmsId != null && dbmsId.trim() != "" && editor.getEditorView() != null) { //$NON-NLS-1$
                    editor.getEditorView().setCurrentDbms(dbmsId);
                }
            }

        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        } catch (SAXException e) {
            // bug 17654:import the xml file as the schema will throw error.
            ExceptionHandler.processForSchemaImportXml(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }
}
