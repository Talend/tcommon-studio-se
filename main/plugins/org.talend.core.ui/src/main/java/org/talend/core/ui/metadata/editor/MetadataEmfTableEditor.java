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

import java.util.List;
import java.util.Observable;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.preference.metadata.MetadataTypeLengthConstants;
import org.talend.core.ui.services.IDesignerCoreUIService;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataEmfTableEditor.java 46726 2010-08-13 05:32:21Z nrousseau $
 * 
 */
public class MetadataEmfTableEditor extends ExtendedTableModel<MetadataColumn> {

    private String defaultLabel = "newColumn"; //$NON-NLS-1$

    private static int indexNewColumn;

    public static final String VALID_CHAR_COLUMN_NAME = "a-zA-Z_0-9"; //$NON-NLS-1$

    private MetadataTable metadataTable;

    private MetadataEmfTableEditorView editorView;

    // Added TDQ-8360 20140408 yyin
    private Observable updateNodifier;

    public MetadataEmfTableEditor(String titleName) {
        super(titleName);
    }

    public MetadataEmfTableEditor() {
        super();
    }

    public MetadataEmfTableEditor(MetadataTable metadataTable, String titleName) {
        super(titleName);
        setMetadataTable(metadataTable);
    }

    public String getTitleName() {
        return getName();
    }

    public List<MetadataColumn> getMetadataColumnList() {
        return getBeansList();
    }

    public MetadataTable getMetadataTable() {
        return this.metadataTable;
    }

    /**
     * set MetadataTable.
     * 
     * @param metadataEditorTable
     */
    public void setMetadataTable(MetadataTable metadataTable) {
        this.metadataTable = metadataTable;
        registerDataList(this.metadataTable.getColumns());
    }

    /**
     * 
     * DOC amaumont Comment method "validateColumnName". Check columName width the list of columns to be see on the
     * table
     * 
     * @param columnName
     * @param beanPosition
     * @return
     */
    public String validateColumnName(String columnName, int beanPosition) {
        // Added TDQ-8360 20140408 yyin
        if (updateNodifier != null) {
            updateNodifier.notifyObservers();
        }
        return validateColumnName(columnName, beanPosition, getBeansList());
    }

    /**
     * 
     * DOC amaumont Comment method "validateColumnName".
     * 
     * @param columnName
     * @param beanPosition
     * @param List <MetadataColumn>
     * @return
     */
    public String validateColumnName(String columnName, int beanPosition, List<MetadataColumn> metadataColumns) {
        if (columnName == null) {
            return Messages.getString("MetadataEmfTableEditor.ColumnNameIsNullError"); //$NON-NLS-1$
        }

        if (!MetadataToolHelper.isValidColumnName(columnName)) {
            return ""; //$NON-NLS-1$ 
        }

        int lstSize = metadataColumns.size();
        for (int i = 0; i < lstSize; i++) {
            if (columnName.equals(metadataColumns.get(i).getLabel()) && i != beanPosition) {
                return Messages.getString("MetadataEmfTableEditor.ColumnNameExists", columnName); //$NON-NLS-1$ 
            } else if (columnName.toLowerCase().equals(metadataColumns.get(i).getLabel().toLowerCase()) && i != beanPosition) {
                String index = columnName.substring(0, 1);
                String last = metadataColumns.get(i).getLabel().substring(0, 1);
                if (index.toLowerCase().equals(last.toLowerCase())) {
                    return Messages.getString("MetadataTableEditor.ColumnNameIsInvalid", columnName);
                }
            }
        }
        return null;
    }

    /**
     * DOC ocarbone. getValidateColumnName return a valid label to a column (not empty, no doublon, no illegal char).
     * 
     * @param columnLabel
     * @param List <MetadataColumn>
     * @return string
     */
    public String getNextGeneratedColumnName(String columnLabel) {
        // Added TDQ-8360 20140408 yyin
        if (updateNodifier != null) {
            updateNodifier.notifyObservers();
        }
        return getNextGeneratedColumnName(columnLabel, getBeansList());
    }

    /**
     * DOC ocarbone. getValidateColumnName return a valid label to a column (not empty, no doublon, no illegal char).
     * 
     * @param columnLabel
     * @param i
     * @param List <MetadataColumn>
     * @return string
     */
    public String getNextGeneratedColumnName(String oldColumnName, List<MetadataColumn> metadataColumns) {
        UniqueStringGenerator<MetadataColumn> uniqueStringGenerator;
        if (metadataColumns != null) {
            uniqueStringGenerator = new UniqueStringGenerator<MetadataColumn>(oldColumnName, metadataColumns) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
                 */
                @Override
                protected String getBeanString(MetadataColumn bean) {
                    return bean.getLabel();
                }

            };
        } else {
            uniqueStringGenerator = new UniqueStringGenerator<MetadataColumn>(oldColumnName, getBeansList()) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
                 */
                @Override
                protected String getBeanString(MetadataColumn bean) {
                    return bean.getLabel();
                }

            };
        }

        return uniqueStringGenerator.getUniqueString();
    }

    /**
     * DOC amaumont Comment method "createNewMetadataColumn".
     * 
     * @return
     */
    public MetadataColumn createNewMetadataColumn(String dbmsId) {
        MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();

        String columnName = getNextGeneratedColumnName(defaultLabel);
        metadataColumn.setLabel(columnName);
        metadataColumn.setOriginalField(columnName);

        IPreferenceStore preferenceStore = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreUIService.class)) {
            IDesignerCoreUIService designerCoreUiService = (IDesignerCoreUIService) GlobalServiceRegister.getDefault()
                    .getService(IDesignerCoreUIService.class);
            preferenceStore = designerCoreUiService.getPreferenceStore();
        }

        if (preferenceStore != null && preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE) != null
                && !preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE).equals("")) { //$NON-NLS-1$
            metadataColumn.setTalendType(preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE));
            if (preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH) != null
                    && !preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH).equals("")) { //$NON-NLS-1$
                metadataColumn.setLength(Integer.parseInt(preferenceStore
                        .getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH)));
            }

        } else {
            metadataColumn.setTalendType(JavaTypesManager.getDefaultJavaType().getId());
            if (dbmsId != null) {
                metadataColumn.setSourceType(TypesManager.getDBTypeFromTalendType(dbmsId, metadataColumn.getTalendType()));
            }
        }

        return metadataColumn;
    }

    /**
     * Getter for defaultLabel.
     * 
     * @return the defaultLabel
     */
    public String getDefaultLabel() {
        return this.defaultLabel;
    }

    /**
     * Sets the defaultLabel.
     * 
     * @param defaultLabel the defaultLabel to set
     */
    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    /**
     * Getter for editorView.
     * 
     * @return the editorView
     */
    public MetadataEmfTableEditorView getEditorView() {
        return editorView;
    }

    /**
     * Sets the editorView.
     * 
     * @param editorView the editorView to set
     */
    public void setEditorView(MetadataEmfTableEditorView editorView) {
        this.editorView = editorView;
    }

    @Override
    public List<MetadataColumn> remove(int[] indexArray) {
        // Added TDQ-8360 20140408 yyin
        if (updateNodifier != null) {
            updateNodifier.notifyObservers();
        }
        return super.remove(indexArray);
    }

    public void addSchemaObservable(Observable observable) {
        this.updateNodifier = observable;
    }

}
