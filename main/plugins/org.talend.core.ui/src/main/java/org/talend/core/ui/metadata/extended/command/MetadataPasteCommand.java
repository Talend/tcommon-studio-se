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
package org.talend.core.ui.metadata.extended.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataPasteCommand extends ExtendedTablePasteCommand {

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataPasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(extendedTable, indexStartAdd);
    }

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTable
     * @param instanceOfType
     */
    public MetadataPasteCommand(ExtendedTableModel extendedTable) {
        super(extendedTable);
    }

    public String getUniqueString(ArrayList list, String columnName) {
        int lstSize = list.size();
        String[] labels = new String[lstSize];
        for (int i = 0; i < lstSize; i++) {
            labels[i] = list.get(i).toString();
        }
        boolean found = false;
        int indexNewColumn = 0;
        String newColumnName = null;
        boolean firstTime = true;
        while (!found) {
            newColumnName = columnName + (firstTime ? "" : (++indexNewColumn)); //$NON-NLS-1$
            firstTime = false;
            boolean allAreDifferent = true;
            for (int j = 0; j < labels.length; j++) {
                String label = labels[j];
                if (label.equals(newColumnName)) {
                    allAreDifferent = false;
                    break;
                }
            }
            if (allAreDifferent) {
                found = true;
            }
        }
        return newColumnName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand#createPastableBeansList(java
     * .util.List)
     */
    @Override
    public List createPastableBeansList(ExtendedTableModel extendedTable, List copiedObjectsList) {
        ArrayList addItemList = new ArrayList();
        ArrayList list = new ArrayList();
        ArrayList<String> labelsExisted = getLabelsExisted(extendedTable);
        ArrayList<String> DBColumnsExisted = getDBColumnsExisted(extendedTable);
        for (Object current : copiedObjectsList) {
            if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                copy.setUsefulColumn(true);
                String nextGeneratedColumnName = ((MetadataTableEditor) extendedTable)
                        .getNextGeneratedColumnName(copy.getLabel());
                if (labelsExisted.contains(nextGeneratedColumnName)) {
                    nextGeneratedColumnName = validateColumnName(nextGeneratedColumnName, labelsExisted);
                }
                labelsExisted.add(nextGeneratedColumnName);
                copy.setLabel(nextGeneratedColumnName);
                if (copy.getOriginalDbColumnName() != null && !StringUtils.isEmpty(copy.getOriginalDbColumnName())) {
                    String nextGeneratedDbColumnName = validateColumnName(copy.getOriginalDbColumnName(), DBColumnsExisted);
                    copy.setOriginalDbColumnName(nextGeneratedDbColumnName);
                }
                addItemList.add(copy);
            }
            // Add a new statement to fix the MetadataColumn type.
            else if (current instanceof MetadataColumn) {
                MetadataTableEditor tableEditor = (MetadataTableEditor) extendedTable;
                MetadataColumn metadataColumn = (MetadataColumn) current;
                String nextGeneratedColumnName = metadataColumn.getLabel();
                String tempNewColumnName = ""; //$NON-NLS-1$
                boolean iMetaColumnUnique = false;
                boolean metaColumnUnique = false;
                while (iMetaColumnUnique == false || metaColumnUnique == false) {
                    nextGeneratedColumnName = tableEditor.getNextGeneratedColumnName(nextGeneratedColumnName, null);
                    iMetaColumnUnique = true;
                    metaColumnUnique = false;
                    if (list.size() == 0)
                        metaColumnUnique = true;
                    else {
                        tempNewColumnName = this.getUniqueString(list, nextGeneratedColumnName);
                        if (tempNewColumnName.equals(nextGeneratedColumnName))
                            metaColumnUnique = true;
                        else {
                            metaColumnUnique = false;
                            nextGeneratedColumnName = tempNewColumnName;
                        }
                    }
                }
                MetadataColumn newColumnCopy = new ConnectionFactoryImpl().copy(metadataColumn, nextGeneratedColumnName);
                IMetadataColumn copy = (ConvertionHelper.convertToIMetaDataColumn(newColumnCopy)).clone();
                copy.setLabel(nextGeneratedColumnName);
                addItemList.add(copy);
                list.add(nextGeneratedColumnName);

            }
        }
        return addItemList;
    }

    public ArrayList<String> getLabelsExisted(ExtendedTableModel extendedTable) {
        ArrayList<String> labelsExisted = new ArrayList<String>();
        for (Object obj : extendedTable.getBeansList()) {
            if (obj instanceof IMetadataColumn) {
                labelsExisted.add(((IMetadataColumn) obj).getLabel());
            }
        }
        return labelsExisted;
    }

    public ArrayList<String> getDBColumnsExisted(ExtendedTableModel extendedTable) {
        ArrayList<String> DBColumnsExisted = new ArrayList<String>();
        for (Object obj : extendedTable.getBeansList()) {
            if (obj instanceof IMetadataColumn) {
                DBColumnsExisted.add(((IMetadataColumn) obj).getOriginalDbColumnName());
            }
        }
        return DBColumnsExisted;
    }
}
