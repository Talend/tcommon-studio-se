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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.DbDefaultLengthAndPrecision;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IConvertionConstants;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.proposal.JavaSimpleDateFormatProposalProvider;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * TGU same purpose as MetadataTableEditorView but uses EMF model directly
 * 
 * $Id: MetadataEmfTableEditorView.java 46940 2010-08-18 06:58:15Z wyang $
 * 
 */
public class MetadataEmfTableEditorView extends AbstractMetadataTableEditorView<MetadataColumn> {

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME"; //$NON-NLS-1$

    /**
     * You must initialize graphicals components by calling <code>initGraphicComponents()</code>.
     * 
     * @param parent
     * @param style
     */
    public MetadataEmfTableEditorView(Composite parent, int style) {
        super(parent, style, false);
        initGraphicComponents();
    }

    /**
     * DOC amaumont MetadataEmfTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param initGraphicsComponents
     */
    public MetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle, boolean initGraphicsComponents) {
        super(parentComposite, mainCompositeStyle, initGraphicsComponents);
    }

    /**
     * Graphics components are automatically initialized.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     */
    public MetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<MetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, true);
    }

    /**
     * 
     * DOC wchen MetadataEmfTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     * @param labelVisible
     */
    public MetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<MetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible, boolean labelVisible) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, labelVisible);
    }

    public MetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<MetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible,
            boolean labelVisible, boolean initComponent) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, labelVisible, initComponent);
    }

    /**
     * Graphics components are automatically initialized.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     */
    public MetadataEmfTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<MetadataColumn> extendedTableModel) {
        super(parentComposite, mainCompositeStyle, extendedTableModel);
    }

    public MetadataEmfTableEditor getMetadataEditor() {
        return (MetadataEmfTableEditor) getExtendedTableModel();
    }

    public void setMetadataEditor(MetadataEmfTableEditor metadataTableEditor) {
        setExtendedTableModel(metadataTableEditor);
    }

    @Override
    public TableViewerCreator<MetadataColumn> getTableViewerCreator() {
        return getExtendedTableViewer().getTableViewerCreator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView#getNullableAccessor()
     */
    @Override
    protected IBeanPropertyAccessors getNullableAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, Boolean>() {

            @Override
            public Boolean get(MetadataColumn bean) {
                return bean.isNullable() ? Boolean.TRUE : Boolean.FALSE;
            }

            @Override
            public void set(MetadataColumn bean, Boolean value) {
                bean.setNullable(value);
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getCommentAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getComment();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setComment(value);
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, Integer> getOriginalLengthAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, Integer>() {

            @Override
            public Integer get(MetadataColumn bean) {
                return Long.valueOf(bean.getOriginalLength()).intValue();
            }

            @Override
            public void set(MetadataColumn bean, Integer value) {
                if (value != null) {
                    bean.setOriginalLength(value);
                } else {
                    bean.setOriginalLength(-1);
                }
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getDefaultValueAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                String value = bean.getDefaultValue();
                return value;
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setDefaultValue(value);
            }
        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, Integer> getPrecisionAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, Integer>() {

            @Override
            public Integer get(MetadataColumn bean) {
                // String dbmsId = getCurrentDbms();
                // if (dbmsId != null && bean.getPrecision() < 0) {
                // Dbms dbms = MetadataTalendType.getDbms(dbmsId);
                // List<DbDefaultLengthAndPrecision> dlpList = dbms.getDefaultLengthPrecision();
                // for (DbDefaultLengthAndPrecision dlp : dlpList) {
                // if (dlp.getDbTypeName().equals(bean.getSourceType()))
                // if (dlp.getDefaultPrecision() == null) {
                // bean.setPrecision(0);
                // } else {
                // bean.setPrecision(dlp.getDefaultPrecision());
                // }
                // }
                // }
                return Long.valueOf(bean.getPrecision()).intValue();
            }

            @Override
            public void set(MetadataColumn bean, Integer value) {
                if (value != null) {
                    bean.setPrecision(value);
                } else {
                    bean.setPrecision(-1);
                }
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, Integer> getLengthAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, Integer>() {

            @Override
            public Integer get(MetadataColumn bean) {
                // String dbmsId = getCurrentDbms();
                // if (dbmsId != null && bean.getLength() <= 0) {
                // Dbms dbms = MetadataTalendType.getDbms(dbmsId);
                // List<DbDefaultLengthAndPrecision> dlpList = dbms.getDefaultLengthPrecision();
                // for (DbDefaultLengthAndPrecision dlp : dlpList) {
                // if (dlp.getDbTypeName().equals(bean.getSourceType())) {
                // if (dlp.getDefaultLength() == null) {
                // bean.setLength(0);
                // } else {
                // bean.setLength(dlp.getDefaultLength());
                // }
                // }
                // }
                // }
                return Long.valueOf(bean.getLength()).intValue();
            }

            @Override
            public void set(MetadataColumn bean, Integer value) {
                if (value != null) {
                    bean.setLength(value);
                    // if not set Original Length
                    if (Long.valueOf(bean.getOriginalLength()) == -1) {
                        bean.setOriginalLength(value);
                    }
                } else {
                    bean.setLength(-1);
                }
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getPatternAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getPattern();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setPattern(value); // MetadataEmfTableEditorView.this.getJavaDateTypeForDefaultPattern(bean));
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, Boolean> getKeyAccesor() {
        return new IBeanPropertyAccessors<MetadataColumn, Boolean>() {

            @Override
            public Boolean get(MetadataColumn bean) {
                return new Boolean(bean.isKey());
            }

            @Override
            public void set(MetadataColumn bean, Boolean value) {
                bean.setKey(value);
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getLabelAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getLabel();
            }

            @Override
            public void set(MetadataColumn bean, String value) {

                if (bean.getLabel().equals(bean.getOriginalField())) {
                    bean.setOriginalField(value);
                }
                bean.setLabel(value);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView#validateColumnName(java.lang.String, int)
     */
    @Override
    protected String validateColumnName(String newValue, int beanPosition) {
        return getMetadataEditor().validateColumnName(newValue, beanPosition);
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getTalendTypeAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getTalendType();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                String oldTalendType = bean.getTalendType();
                bean.setTalendType(value);
                if (!oldTalendType.equals(value)) {
                    bean.setLength(-1);
                    String typeLength = getCurrentTypeLength(value);
                    if (typeLength != null && !typeLength.equals("")) { //$NON-NLS-1$
                        bean.setLength(Integer.parseInt(typeLength));
                    }
                }
                String dbms = getCurrentDbms();
                if (showDbTypeColumn && (dbms != null)) {
                    String oldDbType = bean.getSourceType();
                    String oldDefaultDbType = null;
                    if (!"".equals(oldDbType)) { //$NON-NLS-1$
                        oldDefaultDbType = TypesManager.getDBTypeFromTalendType(dbms, oldTalendType);
                    }
                    if (oldDbType == null || oldDbType.equals(oldDefaultDbType) || "".equals(oldDbType)) { //$NON-NLS-1$
                        bean.setSourceType(TypesManager.getDBTypeFromTalendType(dbms, value));
                    }
                }
                if (currentBeanHasJavaDateType(bean) || isCurrentBeanHasType(bean, "id_Dynamic")) { //$NON-NLS-1$
                    bean.setPattern(new JavaSimpleDateFormatProposalProvider().getProposals(null, 0)[0].getContent());
                    // adaptLengthAndPrecision(bean, dbms);
                }
            }
        };
    }

    private void adaptLengthAndPrecision(MetadataColumn bean, String dbms) {
        Dbms dbmsD = MetadataTalendType.getDbms(dbms);
        List<DbDefaultLengthAndPrecision> dlpList = dbmsD.getDefaultLengthPrecision();
        for (DbDefaultLengthAndPrecision dlp : dlpList) {
            if (dlp.getDbTypeName().equals(bean.getSourceType())) {
                bean.setLength(dlp.getDefaultLength());
                bean.setPrecision(dlp.getDefaultPrecision());
            }
        }

    }

    @Override
    protected IBeanPropertyAccessors getDbTypeAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getSourceType();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setSourceType(value);
                // String dbms = getCurrentDbms();
                // if (dbms != null) {
                // adaptLengthAndPrecision(bean, dbms);
                // }
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected ExtendedToolbarView initToolBar() {
        return new MetadataEmfToolbarEditor(getMainComposite(), SWT.NONE, this.getExtendedTableViewer(), getCurrentDbms());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView#configureDefaultColumn(org.talend.commons.
     * ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void configureDefaultColumn(TableViewerCreator<MetadataColumn> tableViewerCreator) {
        super.configureDefaultColumn(tableViewerCreator);
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getDbColumnNameAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getOriginalField();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setOriginalField(value);
            }

        };
    }

    public String getCurrentTypeLength(String value) {
        return CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore().getString(value.toUpperCase());
    }

    @Override
    protected boolean canModifyDBColumn(Object bean) {
        // TODO
        if (bean instanceof MetadataColumnImpl) {
            if (((MetadataColumnImpl) bean).getLabel().equals(((MetadataColumnImpl) bean).getOriginalField())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getRelatedEntityAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getRelatedEntity();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setRelatedEntity(value);
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getRelationshipTypeAccessor() {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                return bean.getRelationshipType();
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                bean.setRelationshipType(value);
            }

        };
    }

    @Override
    protected IBeanPropertyAccessors<MetadataColumn, String> getAdditionalFieldAccessor(final String field) {
        return new IBeanPropertyAccessors<MetadataColumn, String>() {

            @Override
            public String get(MetadataColumn bean) {
                String value = null;
                for (TaggedValue tgValue : bean.getTaggedValue()) {
                    String tag = tgValue.getTag();
                    if (tag != null && tag.startsWith(IConvertionConstants.ADDITIONAL_FIELD_PREFIX) && tag.endsWith(field)) {
                        value = tgValue.getValue();
                        break;
                    }
                }
                return value;
            }

            @Override
            public void set(MetadataColumn bean, String value) {
                // try to find the tag if exists
                for (TaggedValue tgValue : bean.getTaggedValue()) {
                    String tag = tgValue.getTag();
                    if (tag != null && tag.startsWith(IConvertionConstants.ADDITIONAL_FIELD_PREFIX) && tag.endsWith(field)) {
                        tgValue.setValue(value);
                        return;
                    }
                }
                // if no tag exists, create a new one
                TaggedValue tgValue = orgomg.cwm.objectmodel.core.CoreFactory.eINSTANCE.createTaggedValue();
                tgValue.setTag(IConvertionConstants.ADDITIONAL_FIELD_PREFIX + field);
                tgValue.setValue(value);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView#setTableViewerCreatorOptions(org.talend.commons
     * .ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<MetadataColumn> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setLazyLoad(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView#getUsefulAccessor()
     */
    @Override
    protected IBeanPropertyAccessors<MetadataColumn, Boolean> getUsefulAccessor() {
        // TODO Auto-generated method stub
        return null;
    }
}
