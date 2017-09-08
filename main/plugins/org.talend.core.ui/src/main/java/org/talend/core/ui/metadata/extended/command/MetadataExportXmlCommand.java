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
import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataSchema;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataExportXmlCommand extends Command {

    private File file;

    private MetadataTableEditor extendedTableModel;

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTableModel
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataExportXmlCommand(MetadataTableEditor extendedTableModel, File file) {
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
        // export file if it not exists and if it exists, show up a confirm dialog.
        if (file != null) {
            if (file.exists()) {
                boolean flag = MessageDialog.openConfirm(null, Messages.getString("MetadataExportXmlCommand.title"), //$NON-NLS-1$
                        Messages.getString("MetadataExportXmlCommand.message")); //$NON-NLS-1$
                if (flag) {
                    excuteExportFile();
                } else {
                    return;
                }
            } else {
                excuteExportFile();
            }
        }
    }

    /**
     * show up a confirm dialog
     * 
     * DOC hfchang Comment method "excuteExportFile".
     */
    public void excuteExportFile() {
        try {
            file.createNewFile();
            if (extendedTableModel != null) {
                IMetadataTable currentTable = extendedTableModel.getMetadataTable();
                // get all the columns from the table
                if (currentTable != null) {
                    MetadataSchema.saveMetadataColumnToFile(file, currentTable);
                }
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        }
    }
}
