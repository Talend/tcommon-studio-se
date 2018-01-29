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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MetadataEmfPasteCommand extends ExtendedTablePasteCommand {

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataEmfPasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(extendedTable, indexStartAdd);
    }

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTable
     * @param instanceOfType
     */
    public MetadataEmfPasteCommand(ExtendedTableModel extendedTable) {
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
        MetadataEmfTableEditor tableEditor = (MetadataEmfTableEditor) extendedTable;
        for (Object current : copiedObjectsList) {
            if (current instanceof MetadataColumn) {
                // create a new column as a copy of this column
                MetadataColumn metadataColumn = (MetadataColumn) current;
                String nextGeneratedColumnName = tableEditor.getNextGeneratedColumnName(metadataColumn.getLabel());
                if (labelsExisted.contains(nextGeneratedColumnName)) {
                    nextGeneratedColumnName = validateColumnName(nextGeneratedColumnName, labelsExisted);
                }
                labelsExisted.add(nextGeneratedColumnName);
                MetadataColumn newColumnCopy = new ConnectionFactoryImpl().copy(metadataColumn, nextGeneratedColumnName);
                newColumnCopy.setLabel(nextGeneratedColumnName);
                if (metadataColumn.getName() != null && !StringUtils.isEmpty(metadataColumn.getName())) {
                    String nextGeneratedDbColumnName = validateColumnName(metadataColumn.getName(), DBColumnsExisted);
                    newColumnCopy.setName(nextGeneratedDbColumnName);
                }
                addItemList.add(newColumnCopy);
            } else if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                String nextGeneratedColumnName = copy.getLabel();
                String tempNewColumnName = ""; //$NON-NLS-1$
                boolean iMetaColumnUnique = false;
                boolean metaColumnUnique = false;
                while (iMetaColumnUnique == false || metaColumnUnique == false) {
                    nextGeneratedColumnName = tableEditor.getNextGeneratedColumnName(nextGeneratedColumnName, null);
                    metaColumnUnique = true;
                    iMetaColumnUnique = false;
                    if (list.size() == 0)
                        iMetaColumnUnique = true;
                    else {
                        tempNewColumnName = this.getUniqueString(list, nextGeneratedColumnName);
                        if (tempNewColumnName.equals(nextGeneratedColumnName))
                            iMetaColumnUnique = true;
                        else {
                            iMetaColumnUnique = false;
                            nextGeneratedColumnName = tempNewColumnName;
                        }
                    }
                }
                MetadataColumn newColumnCopy = new ConnectionFactoryImpl().copy(ConvertionHelper.convertToMetaDataColumn(copy),
                        nextGeneratedColumnName);
                newColumnCopy.setLabel(nextGeneratedColumnName);
                addItemList.add(newColumnCopy);
                list.add(nextGeneratedColumnName);
            }
        }
        return addItemList;
    }

    private ArrayList<String> getLabelsExisted(ExtendedTableModel extendedTable) {
        ArrayList<String> labelsExisted = new ArrayList<String>();
        for (Object obj : extendedTable.getBeansList()) {
            if (obj instanceof MetadataColumn) {
                labelsExisted.add(((MetadataColumn) obj).getLabel());
            }
        }
        return labelsExisted;
    }

    private ArrayList<String> getDBColumnsExisted(ExtendedTableModel extendedTable) {
        ArrayList<String> DBColumnsExisted = new ArrayList<String>();
        for (Object obj : extendedTable.getBeansList()) {
            if (obj instanceof MetadataColumn) {
                DBColumnsExisted.add(((MetadataColumn) obj).getName());
            }
        }
        return DBColumnsExisted;
    }
}
