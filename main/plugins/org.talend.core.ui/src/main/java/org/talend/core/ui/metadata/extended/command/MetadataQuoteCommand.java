// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableQuoteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;

public class MetadataQuoteCommand extends ExtendedTableQuoteCommand {

    
    /**
     * DOC MetadataQuoteCommand constructor comment.
     *
     * @param extendedTableModel
     * @param beansToQuote
     * @param selectionIndices
     */
    public MetadataQuoteCommand(ExtendedTableModel extendedTable, List beansToQuote, int[] selectionIndices, String quote, boolean isAddingQuote) {
        super(extendedTable, beansToQuote, selectionIndices, quote, isAddingQuote);
    }

    @Override
    public void toQuote(ExtendedTableModel extendedTable, List copiedObjectsList, int[] selectionIndices, String quote, boolean isAddingQuote) {
        int index = 0;
        for (Object current : copiedObjectsList) {
            //get refreshed element
            current = extendedTable.getTableViewer().getElementAt(selectionIndices[index]);
            if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                copy.setUsefulColumn(true);
                String oldDbColumnName = copy.getOriginalDbColumnName();
                if (oldDbColumnName != null) {
                    String newDbColumnName = oldDbColumnName;
                    if (isAddingQuote) {
                        newDbColumnName = quote + newDbColumnName + quote;
                    } else {
                         newDbColumnName = StringUtils.removeStart(oldDbColumnName, quote);
                         newDbColumnName = StringUtils.removeEnd(newDbColumnName, quote);
                    }
                    copy.setOriginalDbColumnName(newDbColumnName);
                    extendedTable.replace(copy, selectionIndices[index]);
                }
            }
            // Add a new statement to fix the MetadataColumn type.
            else if (current instanceof MetadataColumn) {
                MetadataTableEditor tableEditor = (MetadataTableEditor) extendedTable;
                MetadataColumn metadataColumn = (MetadataColumn) current;
                String oldName = metadataColumn.getName();
                if (oldName != null) {
                    String newName = oldName;
                    if (isAddingQuote) {
                        newName = quote + oldName + quote;
                    }  else {
                        newName = StringUtils.removeStart(oldName, quote);
                        newName = StringUtils.removeEnd(newName, quote);
                    }
                    MetadataColumn newColumnCopy = new ConnectionFactoryImpl().copy(metadataColumn, newName);
                    IMetadataColumn copy = (ConvertionHelper.convertToIMetaDataColumn(newColumnCopy)).clone();
                    extendedTable.replace(copy, selectionIndices[index]);
                }
            }
            index++;
        }
        extendedTable.getTableViewer().getTable().select(selectionIndices);
        extendedTable.getTableViewer().getTable().notifyListeners(SWT.Selection, new Event());
    }
}
