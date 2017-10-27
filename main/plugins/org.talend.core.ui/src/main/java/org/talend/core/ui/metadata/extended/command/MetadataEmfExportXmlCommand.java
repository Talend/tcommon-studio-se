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

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.metadata.MetadataSchema;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataEmfExportXmlCommand extends Command {

    private String dbmsId = null;

    private File file;

    private MetadataEmfTableEditor extendedTableModel;

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTableModel
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataEmfExportXmlCommand(MetadataEmfTableEditor extendedTableModel, File file) {
        super();
        this.file = file;
        this.extendedTableModel = extendedTableModel;
    }

    public MetadataEmfExportXmlCommand(MetadataEmfTableEditor extendedTableModel, File file, String dbmsId) {
        super();
        this.file = file;
        this.extendedTableModel = extendedTableModel;
        this.dbmsId = dbmsId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#execute()
     */
    @Override
    public void execute() {
        try {
            if (file != null) {
                file.createNewFile();
                if (extendedTableModel != null) {
                    MetadataTable currentTable = extendedTableModel.getMetadataTable();
                    // get all the columns from the table
                    if (currentTable != null) {
                        MetadataSchema.saveMetadataColumnToFile(file, currentTable, dbmsId);

                    }
                }
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        }

    }

}
