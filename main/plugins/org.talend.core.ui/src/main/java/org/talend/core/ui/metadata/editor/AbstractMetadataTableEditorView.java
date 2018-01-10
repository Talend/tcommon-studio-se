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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.CellEditorValueAdapterFactory;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.CheckColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.ColumnCellModifier;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.IColumnColorProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.IColumnLabelProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.ExtendedComboBoxCellEditor;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.DummyMetadataTalendTypeFilter;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataTalendTypeFilter;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.update.ConnectionColumnUpdateManager;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.celleditor.JavaTypeComboValueAdapter;
import org.talend.core.ui.proposal.JavaSimpleDateFormatProposalProvider;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <B> Type of beans in table
 */
public abstract class AbstractMetadataTableEditorView<B> extends AbstractDataTableEditorView<B> {

    private static Logger log = Logger.getLogger(AbstractMetadataTableEditorView.class);

    public static final Color READONLY_CELL_BG_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME"; //$NON-NLS-1$

    public static final String ID_COLUMN_KEY = "ID_COLUMN_KEY"; //$NON-NLS-1$

    public static final String ID_COLUMN_TYPE = "ID_COLUMN_TYPE"; //$NON-NLS-1$

    public static final String ID_COLUMN_DBTYPE = "ID_COLUMN_DBTYPE"; //$NON-NLS-1$

    public static final String ID_COLUMN_NULLABLE = "ID_COLUMN_NULLABLE"; //$NON-NLS-1$

    public static final String ID_COLUMN_PATTERN = "ID_COLUMN_PATTERN"; //$NON-NLS-1$

    public static final String ID_COLUMN_DEFAULT = "ID_COLUMN_DEFAULT"; //$NON-NLS-1$

    public static final String ID_COLUMN_COMMENT = "ID_COLUMN_COMMENT"; //$NON-NLS-1$

    public static final String ID_COLUMN_ORIGINALLENGTH = "ID_COLUMN_ORIGINALLENGTH"; //$NON-NLS-1$

    public static final String ID_COLUMN_IMPLIEDDECIMAL = "ID_COLUMN_IMPLIEDDECIMAL"; //$NON-NLS-1$

    public static final String ID_COLUMN_SIGNED = "ID_COLUMN_SIGNED"; //$NON-NLS-1$

    public static final String ID_COLUMN_LENGHT = "ID_COLUMN_LENGHT"; //$NON-NLS-1$

    public static final String ID_COLUMN_PRECISION = "ID_COLUMN_PRECISION"; //$NON-NLS-1$

    public static final String ID_COLUMN_DBCOLUMNNAME = "ID_COLUMN_DBCOLUMNNAME"; //$NON-NLS-1$

    public static final String ID_COLUMN_RELATIONSHIP_TYPE = "ID_COLUMN_RELATIONSHIP_TYPE";//$NON-NLS-1$

    public static final String ID_COLUMN_RELATED_ENTITY = "ID_COLUMN_RELATED_ENTITY";//$NON-NLS-1$

    public static final String ID_COLUMN_ADDITIONAL_FIELD = "ID_COLUMN_ADDITIONAL_FIELD";

    public static final String ID_COLUMN_USEFUL = "ID_COLUMN_USEFUL";

    protected boolean showDbColumnName, showOriginalLength;

    protected boolean showDbTypeColumn;

    protected boolean showTalendTypeColumn = true;

    protected boolean showDbTypeColumnAtLeftPosition;

    protected boolean dbTypeColumnWritable;

    protected boolean dbColumnNameWritable;

    private boolean showPatternColumn = true;

    private boolean showKeyColumn = true;

    private boolean showNullableColumn = true;

    private boolean showLengthColumn = true;

    private boolean showAdditionalFieldColumn = true;

    private boolean showPrecisionColumn = true;

    private boolean showCommentColumn = true;

    protected String dbmsId;

    private List<String> additionalFields;

    private boolean isRepository = false;

    private boolean isSapSpecialSchema = false;

    private boolean showDefaultColumn = true;

    // By default, we don't filter on anything.
    private MetadataTalendTypeFilter metadataTalendTypeFilter = new DummyMetadataTalendTypeFilter();

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     */
    public AbstractMetadataTableEditorView() {
        super();
    }

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param initGraphicsComponents
     */
    public AbstractMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle, boolean initGraphicsComponents) {
        super(parentComposite, mainCompositeStyle, initGraphicsComponents);
    }

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     * @param labelVisible
     * @param initGraphicsComponents
     */
    public AbstractMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<B> extendedTableModel, boolean readOnly, boolean toolbarVisible, boolean labelVisible,
            boolean initGraphicsComponents) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, labelVisible,
                initGraphicsComponents);
    }

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     * @param labelVisible
     */
    public AbstractMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<B> extendedTableModel, boolean readOnly, boolean toolbarVisible, boolean labelVisible) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, labelVisible);
    }

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     */
    public AbstractMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<B> extendedTableModel) {
        super(parentComposite, mainCompositeStyle, extendedTableModel);
    }

    /**
     * DOC amaumont AbstractMetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     */
    public AbstractMetadataTableEditorView(Composite parentComposite, int mainCompositeStyle) {
        super(parentComposite, mainCompositeStyle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#createColumns(org.talend.commons.ui
     * .swt.tableviewer.TableViewerCreator, org.eclipse.swt.widgets.Table)
     */
    @Override
    protected void createColumns(TableViewerCreator<B> tableViewerCreator, Table table) {
        // //////////////////////////////////////////////////////////////////////////////////////
        tableViewerCreator.setReadOnly(false);
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWidth(15);

        if (isRepository) {
            configureUsefulColumn(tableViewerCreator);
        }
        // //////////////////////////////////////////////////////////////////////////////////////

        configureNameColumn(tableViewerCreator);

        if (showDbColumnName) {
            configureDbColumnName(tableViewerCreator);
        }

        // //////////////////////////////////////////////////////////////////////////////////////
        if (showKeyColumn) {
            configureKeyColumn(tableViewerCreator);
        }

        // //////////////////////////////////////////////////////////////////////////////////////

        configureTypeColumns(tableViewerCreator);

        // final CellEditorValueAdapter finalComboValueAdapter =
        // comboValueAdapter;
        // final String[] finalArrayTalendTypes = arrayTalendTypes;
        //
        // column.setTableEditorContent(new TableEditorContent() {
        //
        // /* (non-Javadoc)
        // * @see
        // org.talend.commons.ui.swt.tableviewer.tableeditor.TableEditorContent#initialize(org.eclipse.swt.widgets.Table,
        // org.eclipse.swt.custom.TableEditor,
        // org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn,
        // java.lang.Object, java.lang.Object)
        // */
        // @Override
        // public Control initialize(Table table, TableEditor tableEditor,
        // TableViewerCreatorColumn currentColumn,
        // Object currentRowObject, Object currentCellValue) {
        // CCombo combo = new CCombo(table, SWT.FLAT | SWT.READ_ONLY);
        // combo.setItems(finalArrayTalendTypes);
        // // combo.setText(String.valueOf(currentCellValue));
        // combo.computeSize(SWT.DEFAULT, table.getItemHeight());
        // // Set attributes of the editor
        // tableEditor.grabHorizontal = true;
        // tableEditor.minimumHeight = combo.getSize().y - 4;
        // tableEditor.minimumWidth = combo.getSize().x;
        // return combo;
        // }
        //
        // });

        // //////////////////////////////////////////////////////////////////////////////////////
        if (showNullableColumn) {
            configureNullableColumn(tableViewerCreator);
        }

        // //////////////////////////////////////////////////////////////////////////////////////

        configurePatternColumn(tableViewerCreator);

        // //////////////////////////////////////////////////////////////////////////////////////

        if (showLengthColumn) {
            configureLengthColumn(tableViewerCreator);
        }

        if (showOriginalLength) {
            configureOriginalLengthColumn(tableViewerCreator);
        }

        if (showAdditionalFieldColumn) {
            configureAdditionalFieldColumns(tableViewerCreator);
        }
        // //////////////////////////////////////////////////////////////////////////////////////

        if (showPrecisionColumn) {
            configurePrecisionColumn(tableViewerCreator);
        }

        // //////////////////////////////////////////////////////////////////////////////////////
        if (showDefaultColumn) {
            configureDefaultColumn(tableViewerCreator);
        }

        // //////////////////////////////////////////////////////////////////////////////////////

        if (showCommentColumn) {
            configureCommentColumn(tableViewerCreator);
        }

        if (PluginChecker.isDatacertPluginLoaded()) {
            // Datacert related column configuration set ups
            configureRelationshipType(tableViewerCreator);
            configureRelatedEntity(tableViewerCreator);
        }
    }

    protected void configureUsefulColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.UsefulTitle"));
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.UsefulTitle"));
        column.setId(ID_COLUMN_USEFUL);
        column.setBeanPropertyAccessors(getUsefulAccessor());
        column.setWeight(30);
        column.setModifiable(true);
        column.setMinimumWidth(20);
        column.setDisplayedValue(""); //$NON-NLS-1$
        column.setSortable(true);
        column.setTableColumnSelectionListener(new CheckColumnSelectionListener(column, tableViewerCreator));
        column.setImageHeader(ImageProvider.getImage(EImage.CHECKED_ICON));
        CheckboxTableEditorContent nullableCheckbox = new CheckboxTableEditorContent();
        nullableCheckbox.setToolTipText(Messages.getString("MetadataTableEditorView.UsefulTitle"));
        column.setTableEditorContent(nullableCheckbox);
    }

    protected abstract IBeanPropertyAccessors<B, Boolean> getUsefulAccessor();

    /**
     * DOC amaumont Comment method "configureCommentColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureCommentColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.CommentTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.CommentTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_COMMENT);
        column.setBeanPropertyAccessors(getCommentAccessor());
        column.setWeight(10);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(20);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
    }

    /**
     * DOC amaumont Comment method "getCommentAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getCommentAccessor();

    /**
     * DOC ldong Comment method "configureCommentColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureOriginalLengthColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.OriginalLengthTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.OriginalLengthTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_ORIGINALLENGTH);
        column.setBeanPropertyAccessors(getOriginalLengthAccessor());
        column.setModifiable(!isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(10);
        TextCellEditor textCellEditor = new TextCellEditor(tableViewerCreator.getTable());
        textCellEditor.addListener(new InegerCellEditorListener(textCellEditor, column));
        column.setCellEditor(textCellEditor, CellEditorValueAdapterFactory.getPositiveIntAdapter());
    }

    /**
     * DOC ldong Comment method "getCommentAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, Integer> getOriginalLengthAccessor();

    protected void configureAdditionalFieldColumns(TableViewerCreator<B> tableViewerCreator) {
        if (additionalFields != null) {
            for (String field : additionalFields) {
                TableViewerCreatorColumn column;
                column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setId(ID_COLUMN_ADDITIONAL_FIELD + "_" + field); //$NON-NLS-1$
                column.setTitle(field);
                column.setToolTipHeader(field);
                column.setBeanPropertyAccessors(getAdditionalFieldAccessor(field));
                column.setWeight(10);
                column.setModifiable(true);
                column.setMinimumWidth(20);
                column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
            }
        }
    }

    /**
     * DOC ldong Comment method "getCommentAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getAdditionalFieldAccessor(String field);

    /**
     * DOC ldong Comment method "getCommentAccessor".
     * 
     * @return
     */
    // protected abstract IBeanPropertyAccessors<B, String> getSignedDecimalAccessor();

    /**
     * DOC amaumont Comment method "configureDefaultColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureDefaultColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        if (isSapSpecialSchema) {
            column.setTitle(Messages.getString("MetadataTableEditorView.ValueTitle")); //$NON-NLS-1$
        } else {
            column.setTitle(Messages.getString("MetadataTableEditorView.DefaultTitle")); //$NON-NLS-1$
        }
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.DefaultTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_DEFAULT);
        column.setBeanPropertyAccessors(getDefaultValueAccessor());
        column.setWeight(8);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(30);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
    }

    /**
     * DOC amaumont Comment method "getDefaultValueAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getDefaultValueAccessor();

    /**
     * DOC amaumont Comment method "configurePrecisionColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configurePrecisionColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.PrecisionTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.PrecisionTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_PRECISION);
        column.setBeanPropertyAccessors(getPrecisionAccessor());
        column.setModifiable(!isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(10);
        TextCellEditor textCellEditor = new TextCellEditor(tableViewerCreator.getTable());
        textCellEditor.addListener(new InegerCellEditorListener(textCellEditor, column));
        column.setCellEditor(textCellEditor, CellEditorValueAdapterFactory.getPositiveIntAdapter());
    }

    /**
     * DOC amaumont Comment method "getPrecisionAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, Integer> getPrecisionAccessor();

    /**
     * DOC amaumont Comment method "configureLengthColumn".
     * 
     * @param tableViewerCreator
     * @param positiveIntValueAdapter
     */
    protected void configureLengthColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.LengthTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.LengthTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_LENGHT);
        column.setBeanPropertyAccessors(getLengthAccessor());
        column.setModifiable(!isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(10);
        TextCellEditor textCellEditor = new TextCellEditor(tableViewerCreator.getTable());
        textCellEditor.addListener(new InegerCellEditorListener(textCellEditor, column));
        column.setCellEditor(textCellEditor, CellEditorValueAdapterFactory.getPositiveIntAdapter());
    }

    /**
     * DOC amaumont Comment method "getLengthAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, Integer> getLengthAccessor();

    /**
     * DOC amaumont Comment method "configurePatternColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configurePatternColumn(TableViewerCreator<B> tableViewerCreator) {
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA && showPatternColumn) {
            final TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
            String patternTitle = Messages.getString("MetadataTableEditorView.PatternTitle"); //$NON-NLS-1$
            column.setTitle(patternTitle);
            column.setToolTipHeader(patternTitle);
            column.setId(ID_COLUMN_PATTERN);
            column.setBeanPropertyAccessors(getPatternAccessor());
            column.setModifiable(!isReadOnly());
            column.setWeight(16);
            final ColumnCellModifier columnCellModifier = new ColumnCellModifier(column) {

                @Override
                public boolean canModify(Object bean) {
                    boolean typeIsDate = currentBeanHasJavaDateType(bean) && !isReadOnly();
                    boolean typeIsDynamic = isCurrentBeanHasType(bean, "id_Dynamic") && !isReadOnly(); //$NON-NLS-1$
                    return typeIsDate || typeIsDynamic;
                }

            };
            column.setColorProvider(new IColumnColorProvider() {

                @Override
                public Color getBackgroundColor(Object bean) {
                    if (!columnCellModifier.canModify(bean)) {
                        return READONLY_CELL_BG_COLOR;
                    }
                    return null;
                }

                @Override
                public Color getForegroundColor(Object bean) {
                    return null;
                }

            });
            column.setLabelProvider(new IColumnLabelProvider() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.ui.swt.tableviewer.behavior.IColumnLabelProvider#getLabel(java.lang.Object)
                 */
                @Override
                public String getLabel(Object bean) {
                    if (!currentBeanHasJavaDateType(bean) && !isCurrentBeanHasType(bean, "id_Dynamic")) {
                        return ""; //$NON-NLS-1$
                    }
                    return null;
                }

            });
            column.setColumnCellModifier(columnCellModifier);

            JavaSimpleDateFormatProposalProvider proposalProvider = new JavaSimpleDateFormatProposalProvider();
            TextCellEditorWithProposal patternCellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(), column);
            ContentProposalAdapterExtended contentProposalAdapter = patternCellEditor.getContentProposalAdapter();
            contentProposalAdapter.setFilterStyle(ContentProposalAdapterExtended.FILTER_NONE);
            contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapterExtended.PROPOSAL_INSERT);
            patternCellEditor.setContentProposalProvider(proposalProvider);
            column.setCellEditor(patternCellEditor, CellEditorValueAdapterFactory.getNullToEmptyStringTextAdapater());

        }
    }

    /**
     * DOC amaumont Comment method "getPatternAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getPatternAccessor();

    /**
     * DOC amaumont Comment method "configureNullableColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureNullableColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        String nullableTitle = Messages.getString("MetadataTableEditorView.NullableTitle"); //$NON-NLS-1$
        column.setTitle(nullableTitle);
        column.setToolTipHeader(nullableTitle);
        column.setId(ID_COLUMN_NULLABLE);
        column.setBeanPropertyAccessors(getNullableAccessor());
        column.setModifiable(!isReadOnly());
        column.setWidth(56);
        column.setDisplayedValue(""); //$NON-NLS-1$
        column.setSortable(true);
        column.setTableColumnSelectionListener(new CheckColumnSelectionListener(column, tableViewerCreator));
        column.setImageHeader(ImageProvider.getImage(EImage.CHECKED_ICON));
        CheckboxTableEditorContent nullableCheckbox = new CheckboxTableEditorContent();
        nullableCheckbox.setToolTipText(nullableTitle);
        column.setTableEditorContent(nullableCheckbox);
    }

    /**
     * DOC amaumont Comment method "getNullableAccessors".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, Boolean> getNullableAccessor();

    /**
     * DOC amaumont Comment method "configureTypeColumns".
     * 
     * @param tableViewerCreator
     * @param comboValueAdapter
     * @param arrayTalendTypes
     */
    protected void configureTypeColumns(TableViewerCreator<B> tableViewerCreator) {
        if (showDbTypeColumn) {
            if (showDbTypeColumnAtLeftPosition) {
                configureDbTypeColumn(tableViewerCreator);
                if (showTalendTypeColumn) {
                    configureTalendTypeColumn(tableViewerCreator);
                }
            } else {
                if (showTalendTypeColumn) {
                    configureTalendTypeColumn(tableViewerCreator);
                }
                configureDbTypeColumn(tableViewerCreator);
            }
        } else {
            configureTalendTypeColumn(tableViewerCreator);
        }
    }

    /**
     * DOC amaumont Comment method "configureKeyColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureKeyColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.KeyTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.KeyTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_KEY);
        column.setModifiable(!isReadOnly());
        column.setBeanPropertyAccessors(getKeyAccesor());
        column.setWidth(35);
        column.setDisplayedValue(""); //$NON-NLS-1$
        CheckboxTableEditorContent keyCheckbox = new CheckboxTableEditorContent();
        column.setTableEditorContent(keyCheckbox);
        keyCheckbox.setToolTipText(Messages.getString("MetadataTableEditorView.KeyTitle")); //$NON-NLS-1$
    }

    /**
     * DOC amaumont Comment method "getKeyAccesor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, Boolean> getKeyAccesor();

    /**
     * DOC amaumont Comment method "configureNameColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureNameColumn(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(ID_COLUMN_NAME);
        column.setTitle(Messages.getString("MetadataTableEditorView.ColumnTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.ColumnTitle")); //$NON-NLS-1$

        column.setBeanPropertyAccessors(getLabelAccessor());
        final Image imageKey = ImageProvider.getImage(EImage.KEY_ICON);
        final Image imageEmpty = ImageProvider.getImage(EImage.EMPTY);
        column.setImageProvider(new IColumnImageProvider() {

            @Override
            public Image getImage(Object element) {
                if (getKeyAccesor().get((B) element)) {
                    return imageKey;
                } else {
                    return imageEmpty;
                }
            }

        });
        column.setWeight(25);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(45);
        final TextCellEditor cellEditor = new TextCellEditorExtendTab(tableViewerCreator.getTable());
        cellEditor.addListener(new DialogErrorForCellEditorListener(cellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
                if (!newValue.equals(previousValue)) {
                    ConnectionColumnUpdateManager.getInstance().addNewName((String) newValue, (String) previousValue);
                }
            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                return validateColumnName(newValue, beanPosition);
            }

        });
        column.setCellEditor(cellEditor);
    }

    public class TextCellEditorExtendTab extends TextCellEditor {

        public TextCellEditorExtendTab(Composite parent) {
            super(parent);
        }

        @Override
        protected void keyReleaseOccured(KeyEvent keyEvent) {
            super.keyReleaseOccured(keyEvent);
            if (keyEvent.character == '\t') {
                fireApplyEditorValue();
                deactivate();
            }
        }

    }

    /**
     * DOC amaumont Comment method "configureNameColumn".
     * 
     * @param tableViewerCreator
     */
    protected void configureDbColumnName(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(ID_COLUMN_DBCOLUMNNAME);
        column.setTitle("Db Column"); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.ColumnTitle")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(getDbColumnNameAccessor());
        column.setWeight(25);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(45);
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreator.getTable());
        column.setCellEditor(cellEditor);
        column.setColumnCellModifier(new ColumnCellModifier(column) {

            @Override
            public boolean canModify(Object bean) {
                return super.canModify(bean);
            }

        });
        if (!dbColumnNameWritable) {
            column.setColorProvider(new IColumnColorProvider() {

                @Override
                public Color getBackgroundColor(Object bean) {
                    return READONLY_CELL_BG_COLOR;
                }

                @Override
                public Color getForegroundColor(Object bean) {
                    return null;
                }

            });
        }
    }

    protected boolean canModifyDBColumn(Object bean) {
        return dbColumnNameWritable;
    }

    /**
     * DOC amaumont Comment method "validateColumnName".
     * 
     * @param newValue
     * @param beanPosition
     * @return
     */
    protected abstract String validateColumnName(String newValue, int beanPosition);

    /**
     * DOC amaumont Comment method "getLabelAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getLabelAccessor();

    protected abstract IBeanPropertyAccessors<B, String> getDbColumnNameAccessor();

    protected ExtendedComboBoxCellEditor talendTypeComboEditor;

    protected ExtendedComboBoxCellEditor dbTypeComboEditor;

    /**
     * DOC amaumont Comment method "initTalendTypeColumn".
     * 
     * @param tableViewerCreator
     * @param comboValueAdapter
     * @param arrayTalendTypes
     */
    private TableViewerCreatorColumn configureTalendTypeColumn(final TableViewerCreator<B> tableViewerCreator) {

        CellEditorValueAdapter comboValueAdapter = null;
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();

        if (codeLanguage == ECodeLanguage.JAVA) {
            comboValueAdapter = new JavaTypeComboValueAdapter<B>(JavaTypesManager.getDefaultJavaType(), getNullableAccessor(),
                    metadataTalendTypeFilter);
        } else if (codeLanguage == ECodeLanguage.PERL) {
            comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapterForComboCellEditor();
        }

        String[] arrayTalendTypes = new String[0];
        try {
            arrayTalendTypes = MetadataTalendType.getTalendTypesLabels();
            if (metadataTalendTypeFilter != null) {
                arrayTalendTypes = metadataTalendTypeFilter.filter(arrayTalendTypes);
            }
        } catch (NoClassDefFoundError e) {
            // shouln't be happend
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (ExceptionInInitializerError e) {
            // shouln't be happend
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.TypleTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.TypleTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_TYPE);
        column.setBeanPropertyAccessors(getTalendTypeAccessor());
        column.setModifiable(!isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(30);
        talendTypeComboEditor = new ExtendedComboBoxCellEditor(tableViewerCreator.getTable(), arrayTalendTypes,
                SWT.READ_ONLY);
        CCombo typeCombo = (CCombo) talendTypeComboEditor.getControl();
        typeCombo.setEditable(false);
        column.setCellEditor(talendTypeComboEditor, comboValueAdapter);
        return column;
    }

    /**
     * DOC amaumont Comment method "getTalendType".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors<B, String> getTalendTypeAccessor();

    /**
     * DOC amaumont Comment method "initDbTypeColumn".
     * 
     * @param tableViewerCreator
     */
    private TableViewerCreatorColumn configureDbTypeColumn(final TableViewerCreator<B> tableViewerCreator) {

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.DBTypeTitle")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.DBTypeTitle")); //$NON-NLS-1$
        column.setId(ID_COLUMN_DBTYPE);
        column.setBeanPropertyAccessors(getDbTypeAccessor());
        column.setModifiable(dbTypeColumnWritable && !isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(60);

        if (dbTypeColumnWritable) {

            CellEditorValueAdapter comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapterForComboCellEditor();
            String[] arrayDbTypes = new String[0];
            arrayDbTypes = MetadataTalendType.getDbTypes(getCurrentDbms());
            // System.out.println("currentDbms:" + getCurrentDbms() + "
            // dbTypes:" + arrayDbTypes);
            dbTypeComboEditor = new ExtendedComboBoxCellEditor(tableViewerCreator.getTable(), arrayDbTypes, SWT.READ_ONLY);
            CCombo typeCombo = (CCombo) dbTypeComboEditor.getControl();
            typeCombo.setEditable(false);
            column.setCellEditor(dbTypeComboEditor, comboValueAdapter);
        } else {
            column.setColorProvider(new IColumnColorProvider() {

                @Override
                public Color getBackgroundColor(Object bean) {
                    return READONLY_CELL_BG_COLOR;
                }

                @Override
                public Color getForegroundColor(Object bean) {
                    return null;
                }

            });
        }

        return column;

    }

    /**
     * DOC amaumont Comment method "getDbTypeAccessor".
     * 
     * @return
     */
    protected abstract IBeanPropertyAccessors getDbTypeAccessor();

    public void notifyFocusLost() {
        if (talendTypeComboEditor != null) {
            talendTypeComboEditor.focusLost();
        }
        if (dbTypeComboEditor != null) {
            dbTypeComboEditor.focusLost();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#setTableViewerCreatorOptions(org.talend
     * .commons.ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<B> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setFirstVisibleColumnIsSelection(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected abstract ExtendedToolbarView initToolBar();

    public void setShowDbTypeColumn(boolean showDbTypeColumn, boolean showDbTypeColumnAtLeftPosition, boolean writable) {
        this.showDbTypeColumn = showDbTypeColumn;
        this.showDbTypeColumnAtLeftPosition = showDbTypeColumnAtLeftPosition;
        this.dbTypeColumnWritable = writable;
    }

    public void setShowDbColumnName(boolean showDbColumnName, boolean writable) {
        this.showDbColumnName = showDbColumnName;
        this.dbColumnNameWritable = writable;
    }

    /**
     * DOC ocarbone Comment method "setGridDataSize".
     * 
     * @param minimumWidth
     * @param minimumHeight
     */
    @Override
    public void setGridDataSize(final int minimumWidth, final int minimumHeight) {
        getMainComposite().setSize(minimumWidth, minimumHeight);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = minimumWidth;
        gridData.minimumHeight = minimumHeight;
        getMainComposite().setLayoutData(gridData);

    }

    /**
     * Getter for showPatternColumn.
     * 
     * @return the showPatternColumn
     */
    public boolean isShowPatternColumn() {
        return this.showPatternColumn;
    }

    /**
     * Sets the showPatternColumn.
     * 
     * @param showPatternColumn the showPatternColumn to set
     */
    public void setShowPatternColumn(boolean showPatternColumn) {
        this.showPatternColumn = showPatternColumn;
    }

    /**
     * DOC amaumont Comment method "currentBeanHasJavaDateType".
     * 
     * @param element
     * @return
     */
    public boolean currentBeanHasJavaDateType(Object element) {
        String talendType = getTalendTypeAccessor().get((B) element);
        boolean typeIsDate = JavaTypesManager.DATE.getId().equals(talendType);
        return typeIsDate;
    }

    /**
     * DOC ycbai Comment method "isCurrentBeanHasType".
     * 
     * @param element
     * @param typeId
     * @return
     */
    public boolean isCurrentBeanHasType(Object element, String typeId) {
        if (element == null || typeId == null) {
            return false;
        }
        String talendType = getTalendTypeAccessor().get((B) element);
        return typeId.equals(talendType);
    }

    public boolean isShowTalendTypeColumn() {
        return this.showTalendTypeColumn;
    }

    public void setShowTalendTypeColumn(boolean showTalenTypeColumn) {
        this.showTalendTypeColumn = showTalenTypeColumn;
    }

    public String getCurrentDbms() {
        if (!this.showDbTypeColumn) {
            return null;
        }
        if (dbmsId == null) {
            return "mysql_id"; //$NON-NLS-1$
        }
        return dbmsId;
    }

    public void setCurrentDbms(String dbmsId) {
        this.dbmsId = dbmsId;
    }

    /*
     * When the type is Date in java, set the default pattern.
     */
    public String getJavaDateOrDynTypeForDefaultPattern(Object bean) {
        if (currentBeanHasJavaDateType(bean) || isCurrentBeanHasType(bean, "id_Dynamic")) { //$NON-NLS-1$
            return new JavaSimpleDateFormatProposalProvider().getProposals(null, 0)[0].getContent();
        }
        return ""; //$NON-NLS-1$
    }

    protected abstract IBeanPropertyAccessors<B, String> getRelationshipTypeAccessor();

    protected abstract IBeanPropertyAccessors<B, String> getRelatedEntityAccessor();

    protected void configureRelationshipType(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.RelationshipType")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.RelationshipType")); //$NON-NLS-1$
        column.setId(ID_COLUMN_RELATIONSHIP_TYPE);
        column.setBeanPropertyAccessors(getRelationshipTypeAccessor());
        column.setWeight(10);
        column.setModifiable(true);
        column.setMinimumWidth(20);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
    }

    protected void configureRelatedEntity(TableViewerCreator<B> tableViewerCreator) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataTableEditorView.RelatedEntity")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("MetadataTableEditorView.RelatedEntity")); //$NON-NLS-1$
        column.setId(ID_COLUMN_RELATED_ENTITY);
        column.setBeanPropertyAccessors(getRelatedEntityAccessor());
        column.setWeight(10);
        column.setModifiable(true);
        column.setMinimumWidth(20);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
    }

    public void setShowOriginalLength(boolean showOriginalLength) {
        this.showOriginalLength = showOriginalLength;
    }

    public void setShowAdditionalFields(boolean showImpliedDecimal) {
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("ImpliedDecimal");
        fieldList.add("Signed");
        setAdditionalFields(fieldList);
    }

    public void setAdditionalFields(List<String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public void setIsRepository(boolean isRepository) {
        this.isRepository = isRepository;
    }

    public boolean isRepository() {
        return this.isRepository;
    }

    public void setShowKeyColumn(boolean showKeyColumn) {
        this.showKeyColumn = showKeyColumn;
    }

    public void setShowNullableColumn(boolean showNullableColumn) {
        this.showNullableColumn = showNullableColumn;
    }

    public void setShowLengthColumn(boolean showLengthColumn) {
        this.showLengthColumn = showLengthColumn;
    }

    public void setShowAdditionalFieldColumn(boolean showAdditionalFieldColumn) {
        this.showAdditionalFieldColumn = showAdditionalFieldColumn;
    }

    public void setShowPrecisionColumn(boolean showPrecisionColumn) {
        this.showPrecisionColumn = showPrecisionColumn;
    }

    public void setShowCommentColumn(boolean showCommentColumn) {
        this.showCommentColumn = showCommentColumn;
    }

    /**
     * Getter for isSapSpecialSchema.
     * 
     * @return the isSapSpecialSchema
     */
    public boolean isSapSpecialSchema() {
        return this.isSapSpecialSchema;
    }

    /**
     * Sets the isSapSpecialSchema.
     * 
     * @param isSapSpecialSchema the isSapSpecialSchema to set
     */
    public void setSapSpecialSchema(boolean isSapSpecialSchema) {
        this.isSapSpecialSchema = isSapSpecialSchema;
    }

    /**
     * Getter for showDefaultColumn.
     * 
     * @return the showDefaultColumn
     */
    public boolean isShowDefaultColumn() {
        return this.showDefaultColumn;
    }

    /**
     * Sets the showDefaultColumn.
     * 
     * @param showDefaultColumn the showDefaultColumn to set
     */
    public void setShowDefaultColumn(boolean showDefaultColumn) {
        this.showDefaultColumn = showDefaultColumn;
    }

    /**
     * Sets the metadataTalendTypeFilter.
     * 
     * @param metadataTalendTypeFilter the metadataTalendTypeFilter to set
     */
    public void setMetadataTalendTypeFilter(MetadataTalendTypeFilter metadataTalendTypeFilter) {
        this.metadataTalendTypeFilter = metadataTalendTypeFilter;
    }
}
