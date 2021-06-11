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
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableCaseCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;

public class MetadataCaseCommand extends ExtendedTableCaseCommand {

    
    /**
     * DOC MetadataCaseCommand constructor comment.
     *
     * @param extendedTableModel
     * @param beansToUppercase
     * @param selectionIndices
     * @param isUpperCase
     */
    public MetadataCaseCommand(ExtendedTableModel extendedTable, List beansToUppercase, int[] selectionIndices, boolean isUpperCase) {
        super(extendedTable, beansToUppercase, selectionIndices, isUpperCase);
    }

    @Override
    public void convertCase(ExtendedTableModel extendedTable, List copiedObjectsList, int[] selectionIndices, boolean isUpperCase) {
        int index = 0;
        for (Object current : copiedObjectsList) {
            //get refreshed element
            current = extendedTable.getTableViewer().getElementAt(selectionIndices[index]);
            if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                copy.setUsefulColumn(true);
                if (copy.getOriginalDbColumnName() != null && !StringUtils.isEmpty(copy.getOriginalDbColumnName())) {
                  copy.setOriginalDbColumnName(isUpperCase ? copy.getOriginalDbColumnName().toUpperCase() : copy.getOriginalDbColumnName().toLowerCase());
                }
                extendedTable.replace(copy, selectionIndices[index]);
            }
            // Add a new statement to fix the MetadataColumn type.
            else if (current instanceof MetadataColumn) {
                MetadataTableEditor tableEditor = (MetadataTableEditor) extendedTable;
                MetadataColumn metadataColumn = (MetadataColumn) current;
                MetadataColumn newColumnCopy = new ConnectionFactoryImpl().copy(metadataColumn, 
                        isUpperCase ? StringUtils.upperCase(metadataColumn.getName()) : StringUtils.lowerCase(metadataColumn.getName()));
                IMetadataColumn copy = (ConvertionHelper.convertToIMetaDataColumn(newColumnCopy)).clone();
                extendedTable.replace(copy, selectionIndices[index]);
            }
            index++;
        }
        extendedTable.getTableViewer().getTable().select(selectionIndices);
        extendedTable.getTableViewer().getTable().notifyListeners(SWT.Selection, new Event());
    }
}
