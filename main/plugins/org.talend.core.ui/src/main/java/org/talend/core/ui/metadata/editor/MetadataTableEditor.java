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

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.preference.metadata.MetadataTypeLengthConstants;
import org.talend.core.ui.services.IDesignerCoreUIService;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataTableEditor.java 51244 2010-11-15 03:28:34Z cli $
 * 
 */
public class MetadataTableEditor extends ExtendedTableModel<IMetadataColumn> {

    private IMetadataTable metadataTable;

    private boolean isNeedShowAllColumn = false;

    public MetadataTableEditor() {
        super();
    }

    public MetadataTableEditor(IMetadataTable metadataTable, String titleName) {
        super(titleName);
        this.metadataTable = metadataTable;
        initFromMetadataTable();
    }

    public MetadataTableEditor(IMetadataTable metadataTable, String titleName, boolean isNeedShowAllColumn) {
        super(titleName);
        this.metadataTable = metadataTable;
        this.isNeedShowAllColumn = isNeedShowAllColumn;
        initFromMetadataTable();
    }

    public void initFromMetadataTable() {
        initData();
    }

    private void initData() {
        if (this.metadataTable != null) {
            if (isNeedShowAllColumn) {
                registerDataList(this.metadataTable.getListColumns(true));
            } else {
                registerDataList(this.metadataTable.getListColumns());
            } 
        }
    }

    public String getTitleName() {
        return super.getName();
    }

    public List<IMetadataColumn> getMetadataColumnList() {
        return getBeansList();
    }

    public IMetadataTable getMetadataTable() {
        return this.metadataTable;
    }

    /**
     * set MetadaTable.
     * 
     * @param metadataEditorTable
     */
    public void setMetadataTable(IMetadataTable metadataTable) {
        this.metadataTable = metadataTable;
        initFromMetadataTable();
    }

    /**
     * 
     * DOC amaumont Comment method "validateColumnName".
     * 
     * @param columnName
     * @return true if columnName has a valid value
     */
    public String validateColumnName(String columnName, int beanPosition) {
        if (columnName == null) {
            return Messages.getString("MetadataTableEditor.ColumnNameIsNull"); //$NON-NLS-1$
        }

        if (!MetadataToolHelper.isValidColumnName(columnName)) {
            return ""; //$NON-NLS-1$ 
        }

        int lstSize = getBeansList().size();
        for (int i = 0; i < lstSize; i++) {
            if (columnName.equals(getBeansList().get(i).getLabel()) && i != beanPosition) {
                return Messages.getString("MetadataTableEditor.ColumnNameExists", columnName); //$NON-NLS-1$
            } else if (columnName.toLowerCase().equals(getBeansList().get(i).getLabel().toLowerCase()) && i != beanPosition) {
                String index = columnName.substring(0, 1);
                String last = getBeansList().get(i).getLabel().substring(0, 1);
                if (index.toLowerCase().equals(last.toLowerCase())) {
                    return Messages.getString("MetadataTableEditor.ColumnNameIsInvalid", columnName); //$NON-NLS-1$
                }
            }

        }
        return null;
    }

    public String getNextGeneratedColumnName() {
        return getNextGeneratedColumnName("newColumn", getBeansList()); //$NON-NLS-1$
    }

    public String getNextGeneratedColumnName(String columnLabel) {
        return getNextGeneratedColumnName(columnLabel, getBeansList());
    }

    public String getNextGeneratedColumnName(String oldColumnName, List<IMetadataColumn> metadataColumns) {
        UniqueStringGenerator<IMetadataColumn> uniqueStringGenerator;
        if (metadataColumns != null) {
            uniqueStringGenerator = new UniqueStringGenerator<IMetadataColumn>(oldColumnName, metadataColumns) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
                 */
                @Override
                protected String getBeanString(IMetadataColumn bean) {
                    return bean.getLabel();
                }

            };
        } else {
            uniqueStringGenerator = new UniqueStringGenerator<IMetadataColumn>(oldColumnName, getBeansList()) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
                 */
                @Override
                protected String getBeanString(IMetadataColumn bean) {
                    return bean.getLabel();
                }

            };
        }

        // UniqueStringGenerator<IMetadataColumn> uniqueStringGenerator = new
        // UniqueStringGenerator<IMetadataColumn>(oldColumnName,
        // metadataColumns) {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
        // */
        // @Override
        // protected String getBeanString(IMetadataColumn bean) {
        // return bean.getLabel();
        // }
        //
        // };

        return uniqueStringGenerator.getUniqueString();
    }

    public IMetadataColumn createNewMetadataColumn() {
        MetadataColumn metadataColumn = new MetadataColumn();
        String columnName = getNextGeneratedColumnName();
        metadataColumn.setLabel(columnName);
        metadataColumn.setNullable(true);
        metadataColumn.setOriginalDbColumnName(columnName);

        IPreferenceStore preferenceStore = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreUIService.class)) {
            IDesignerCoreUIService designerCoreUiService = (IDesignerCoreUIService) GlobalServiceRegister.getDefault()
                    .getService(IDesignerCoreUIService.class);
            preferenceStore = designerCoreUiService.getPreferenceStore();
        }
        if (preferenceStore != null && preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE) != null
                && !preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE).equals("")) { //$NON-NLS-1$
            metadataColumn.setTalendType(preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_TYPE));
            if (metadataTable.getDbms() != null) {
                metadataColumn.setType(TypesManager.getDBTypeFromTalendType(metadataTable.getDbms(),
                        metadataColumn.getTalendType()));
            }
            if (preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH) != null
                    && !preferenceStore.getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH).equals("")) { //$NON-NLS-1$
                metadataColumn.setLength(Integer.parseInt(preferenceStore
                        .getString(MetadataTypeLengthConstants.FIELD_DEFAULT_LENGTH)));
            }
        } else {
            metadataColumn.setTalendType(JavaTypesManager.getDefaultJavaType().getId());
            if (metadataTable.getDbms() != null) {
                metadataColumn.setType(TypesManager.getDBTypeFromTalendType(metadataTable.getDbms(),
                        metadataColumn.getTalendType()));
            }
        }

        return metadataColumn;
    }
}
