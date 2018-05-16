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
package org.talend.repository.ui.wizards.metadata.connection.genericshema;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.ui.preference.metadata.MetadataTypeLengthConstants;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.core.utils.CsvArray;
import org.talend.metadata.managment.ui.dialog.CustomTableManagerOnlyForGenericSchema;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class GenericSchemaStep2Form extends AbstractForm {

    protected ConnectionItem connectionItem;

    protected GenericSchemaConnection connection;

    private WizardPage page = null;

    private static Logger log = Logger.getLogger(GenericSchemaStep2Form.class);

    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private UtilsButton cancelButton;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    private Label informationLabel;

    private MetadataTable metadataTable;

    private LabelledText metadataNameText;

    private LabelledText metadataCommentText;

    private Label mappingTypeLabel;

    private Button mappingTypeCheckBox;

    private Combo mappingTypeCombo;

    private Dbms[] dbmsArray;

    private IListenableListListener listenableListListener;

    private boolean readOnly;

    private Composite compositeTable;

    private Composite compositeMetaData;

    private Group groupMetaData;

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public GenericSchemaStep2Form(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    /**
     * DOC ocarbone AbstractDelimitedFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public GenericSchemaStep2Form(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    // /**
    // * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
    // */
    // public GenericSchemaStep2Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
    // String[] existingNames) {
    // super(parent, SWT.NONE, existingNames);
    // this.connectionItem = connectionItem;
    // }

    protected GenericSchemaConnection getConnection() {
        return (GenericSchemaConnection) connectionItem.getConnection();
    }

    /**
     * Getter for page.
     * 
     * @return the page
     */
    public WizardPage getWizardPage() {
        return this.page;
    }

    /**
     * Sets the page.
     * 
     * @param page the page to set
     */
    public void setWizardPage(WizardPage page) {
        this.page = page;
    }

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     */
    public GenericSchemaStep2Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        // init the metadata Table
        String label = MetadataToolHelper.validateValue(metadataTable.getLabel());
        metadataNameText.setText(label);
        metadataCommentText.setText(metadataTable.getComment());

        boolean mappingTypeUsed = getConnection().isMappingTypeUsed();
        mappingTypeCheckBox.setSelection(mappingTypeUsed);

        String mappingTypeId = getConnection().getMappingTypeId();
        String mappingTypeLabel = null;
        if (mappingTypeId != null) {
            mappingTypeLabel = getMappingTypeLabelById(mappingTypeId);
            tableEditorView.getTable().getParent().dispose();
            tableEditorView.setShowDbTypeColumn(true, false, true);
            tableEditorView.setShowDbColumnName(true, true);
            tableEditorView.setCurrentDbms(mappingTypeId);
            tableEditorView.initGraphicComponents();
        } else {
            tableEditorView.getTable().getParent().dispose();
            tableEditorView.setShowDbTypeColumn(false, false, false);
            tableEditorView.setShowDbColumnName(false, false);
            tableEditorView.initGraphicComponents();
        }

        mappingTypeCombo.setText(mappingTypeLabel == null ? "" : mappingTypeLabel); //$NON-NLS-1$

        metadataEditor.setMetadataTable(metadataTable);

        tableEditorView.setMetadataEditor(metadataEditor);
        metadataEditor.setEditorView(tableEditorView);
        // mappingTypeCheckBox.setSelection(mappingTypeUsed);
        mappingTypeCombo.setEnabled(mappingTypeUsed);
        CustomTableManagerOnlyForGenericSchema.addCustomManagementToTable(tableEditorView, false);

        tableEditorView.getTableViewerCreator().layout();

        listenableListListener = new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        };

        if (getConnection().isReadOnly()) {
            adaptFormToReadOnly();
        } else {
            updateStatus(IStatus.OK, null);
        }
    }

    /**
     * DOC Administrator Comment method "getMappingTypeLabelById".
     * 
     * @param mappingTypeId
     * @return
     */
    private String getMappingTypeLabelById(String mappingTypeId) {
        dbmsArray = MetadataTalendType.getAllDbmsArray();

        for (Dbms element : dbmsArray) {
            String indexId = element.getId();
            if (mappingTypeId.equals(indexId)) {
                return element.getLabel();
            }
        }
        return null;
    }

    /**
     * DOC Administrator Comment method "getMappingTypeIdByLabel".
     */
    private String getMappingTypeIdByLabel(String label) {
        dbmsArray = MetadataTalendType.getAllDbmsArray();

        for (Dbms element : dbmsArray) {
            String indexLabel = element.getLabel();
            if (label.equals(indexLabel)) {
                return element.getId();
            }
        }
        return null;
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        // guessButton.setEnabled(!isReadOnly());
        metadataNameText.setReadOnly(isReadOnly());
        // mappingTypeCombo.setEnabled(!isReadOnly());
        metadataCommentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());
        mappingTypeCheckBox.setEnabled(!isReadOnly());
        mappingTypeCombo.setEnabled(!isReadOnly());
    }

    @Override
    protected void addFields() {

        // Header Fields
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, 60);
        metadataNameText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataName")); //$NON-NLS-1$
        metadataCommentText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataComment")); //$NON-NLS-1$
        Dbms[] dbms = MetadataTalendType.getAllDbmsArray();
        String[] dbmsItems = new String[(dbms.length)];
        for (int i = 0; i < dbms.length; i++) {
            dbmsItems[i] = dbms[i].getLabel();
        }
        Composite mappingTypeComposite = Form.startNewDimensionnedGridLayout(this, 3, WIDTH_GRIDDATA_PIXEL, 60);

        mappingTypeLabel = new Label(mappingTypeComposite, SWT.NONE);
        mappingTypeLabel.setText(Messages.getString("GenericSchemaStep2Form.SelectDatabaseType")); //$NON-NLS-1$

        mappingTypeCheckBox = new Button(mappingTypeComposite, SWT.CHECK);

        mappingTypeCombo = new Combo(mappingTypeComposite, SWT.NONE);
        mappingTypeCombo.setItems(dbmsItems);
        groupMetaData = Form.createGroup(this, 1, Messages.getString("FileStep3.groupMetadata"), 280); //$NON-NLS-1$
        compositeMetaData = Form.startNewGridLayout(groupMetaData, 1);

        // Composite Guess
        // Composite compositeGuessButton = Form.startNewDimensionnedGridLayout(compositeMetaData, 2,
        // WIDTH_GRIDDATA_PIXEL, 40);
        // informationLabel = new Label(compositeGuessButton, SWT.NONE);
        // informationLabel
        // .setText(Messages.getString("FileStep3.informationLabel") + " "); //$NON-NLS-1$ //$NON-NLS-2$
        // informationLabel.setSize(500, HEIGHT_BUTTON_PIXEL);
        //
        // guessButton = new UtilsButton(compositeGuessButton, Messages.getString("FileStep3.guess"),
        // WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
        // HEIGHT_BUTTON_PIXEL);
        // guessButton.setToolTipText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$

        // Composite MetadataTableEditorView
        compositeTable = Form.startNewDimensionnedGridLayout(compositeMetaData, 1, WIDTH_GRIDDATA_PIXEL, 200);
        compositeTable.setLayout(new FillLayout());
        metadataEditor = new MetadataEmfTableEditor(metadataTable, Messages.getString("FileStep3.metadataDescription")); //$NON-NLS-1$

        tableEditorView = new MetadataEmfTableEditorView(compositeTable, SWT.NONE);

        // tableEditorView.getTable().getParent().dispose();
        // tableEditorView.setShowDbTypeColumn(true, false, true);
        // tableEditorView.setShowDbColumnName(true, true);
        // tableEditorView.initGraphicComponents();

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // metadataNameText : Event modifyText
        metadataNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                MetadataToolHelper.validateSchema(metadataNameText.getText());
                metadataTable.setLabel(metadataNameText.getText());
                checkFieldsValue();
            }
        });
        // metadataNameText : Event KeyListener
        metadataNameText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                MetadataToolHelper.checkSchema(getShell(), e);
            }
        });

        // metadataCommentText : Event modifyText
        metadataCommentText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                metadataTable.setComment(metadataCommentText.getText());
            }
        });

        // mappingTypeCheckBox : Event Selection
        mappingTypeCheckBox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                boolean isMappingIdUsed = false;
                if (mappingTypeCheckBox.getSelection()) {

                    tableEditorView.setShowDbTypeColumn(true, false, true);
                    tableEditorView.setShowDbColumnName(true, true);

                    getConnection().setMappingTypeUsed(true);
                    String mappingTypeId = null;
                    if (tableEditorView.getCurrentDbms() != null && tableEditorView.getCurrentDbms().trim() != "") { //$NON-NLS-1$
                        mappingTypeId = getMappingTypeLabelById(tableEditorView.getCurrentDbms().trim());
                    } else {
                        mappingTypeId = mappingTypeCombo.getText().trim();
                    }
                    isMappingIdUsed = mappingTypeId != null && mappingTypeId.length() > 0;
                    if (isMappingIdUsed) {
                        mappingTypeCombo.setText(mappingTypeId);
                    } else {
                        mappingTypeCombo.select(0);
                        isMappingIdUsed = true;
                    }
                    String mappingTypeValue = mappingTypeCombo.getText();
                    getConnection().setMappingTypeId(getMappingTypeIdByLabel(mappingTypeValue));
                    tableEditorView.setCurrentDbms(getMappingTypeIdByLabel(mappingTypeValue));
                } else {
                    tableEditorView.setShowDbTypeColumn(false, false, false);
                    tableEditorView.setShowDbColumnName(false, false);
                    getConnection().setMappingTypeUsed(false);
                    mappingTypeCombo.setEnabled(false);
                    getConnection().setMappingTypeId(null);
                }
                if (tableEditorView.getMainComposite() != null && !tableEditorView.getMainComposite().isDisposed()) {
                    tableEditorView.getMainComposite().dispose();
                }

                tableEditorView.initGraphicComponents();

                mappingTypeCombo.setEnabled(isMappingIdUsed);

                CustomTableManagerOnlyForGenericSchema.addCustomManagementToTable(tableEditorView, false);
                tableEditorView.setExtendedTableModel(metadataEditor);
                tableEditorView.getExtendedToolbar().updateEnabledStateOfButtons();
                compositeTable.layout();
                tableEditorView.getMetadataEditor().addAfterOperationListListener(listenableListListener);

            }
        });

        mappingTypeCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableEditorView.setShowDbTypeColumn(true, false, true);
                tableEditorView.setShowDbColumnName(true, true);
                getConnection().setMappingTypeUsed(true);
                String mappingTypeIdByLabel = getMappingTypeIdByLabel(mappingTypeCombo.getText());
                getConnection().setMappingTypeId(mappingTypeIdByLabel);
                tableEditorView.setCurrentDbms(mappingTypeIdByLabel);

                if (tableEditorView.getMainComposite() != null && !tableEditorView.getMainComposite().isDisposed()) {
                    tableEditorView.getMainComposite().dispose();
                }
                tableEditorView.initGraphicComponents();
                CustomTableManagerOnlyForGenericSchema.addCustomManagementToTable(tableEditorView, false);
                tableEditorView.setExtendedTableModel(metadataEditor);
                tableEditorView.getExtendedToolbar().updateEnabledStateOfButtons();
                compositeTable.layout();

                tableEditorView.getMetadataEditor().addAfterOperationListListener(listenableListListener);
            }
        });
        tableEditorView.getMetadataEditor().addAfterOperationListListener(listenableListListener);
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }

    }

    // /**
    // * create ProcessDescription and set it.
    // *
    // * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
    // *
    // *
    // * @return processDescription
    // */
    // private ProcessDescription getProcessDescription() {
    //
    // ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getConnection());
    //
    // // Adapt Header width firstRowIsCaption to preview the first line on caption or not
    // if (getConnection().isFirstLineCaption()) {
    // processDescription.setHeaderRow(getConnection().getHeaderValue() - 1);
    // }
    //
    // // adapt the limit to the extract sames rows of preview
    // processDescription.setLimitRows(maximumRowsToPreview);
    // if (getConnection().isUseLimit()) {
    // Integer i = getConnection().getLimitValue();
    // if (getConnection().isFirstLineCaption()) {
    // i++;
    // }
    // if (i < maximumRowsToPreview) {
    // processDescription.setLimitRows(i);
    // }
    // }
    // return processDescription;
    // }
    //
    // /**
    // * run a ShadowProcess to determined the Metadata.
    // */
    // protected void runShadowProcess() {
    //
    // // if no file, the process don't be executed
    // if (getConnection().getFilePath() == null || getConnection().getFilePath().equals("")) { //$NON-NLS-1$
    // informationLabel.setText(" " + Messages.getString("FileStep3.filepathAlert") //$NON-NLS-1$ //$NON-NLS-2$
    // + " "); //$NON-NLS-1$
    // return;
    // }
    //
    // try {
    // informationLabel.setText(" " + Messages.getString("FileStep3.guessProgress")); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // // get the XmlArray width an adapt ProcessDescription
    // if (Escape.CSV_LITERAL.equals(getConnection().getEscapeType())) {
    // CsvArray csvArray = ShadowProcessHelper.getCsvArray(getProcessDescription(), "FILE_CSV"); //$NON-NLS-1$
    // if (csvArray == null) {
    // informationLabel.setText(" " + Messages.getString("FileStep3.guessFailure")); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // } else {
    // refreshMetaDataTable(csvArray);
    // }
    //
    // } else {
    // CsvArray csvArray = ShadowProcessHelper.getCsvArray(getProcessDescription(), "FILE_DELIMITED"); //$NON-NLS-1$
    // if (csvArray == null) {
    // informationLabel.setText(" " + Messages.getString("FileStep3.guessFailure")); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // } else {
    // refreshMetaDataTable(csvArray);
    // }
    // }
    //
    // } catch (CoreException e) {
    // if (getParent().getChildren().length == 1) {
    // new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip") + "\n"
    // //$NON-NLS-1$ //$NON-NLS-2$
    // + Messages.getString("FileStep3.guessFailureTip2"), e.getMessage()); //$NON-NLS-1$
    // } else {
    // new ErrorDialogWidthDetailArea(getShell(), PID,
    // Messages.getString("FileStep3.guessFailureTip"), e.getMessage()); //$NON-NLS-1$
    // }
    // log.error(Messages.getString("FileStep3.guessFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
    // }
    // checkFieldsValue();
    // }

    /**
     * DOC ocarbone Comment method "refreshMetaData".
     * 
     * @param csvArray
     */
    public void refreshMetaDataTable(final CsvArray csvArray) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

        // clear all items
        tableEditorView.getMetadataEditor().removeAll();

        if (csvArray == null || csvArray.getRows().isEmpty()) {
            return;
        } else {

            List<String[]> csvRows = csvArray.getRows();
            String[] fields = csvRows.get(0);
            // int numberOfCol = fields.size();

            Integer numberOfCol = getRightFirstRow(csvRows);

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;
            // if (getConnection().isFirstLineCaption()) {
            // firstRowToExtractMetadata = 1;
            // }

            // the first rows is used to define the label of any metadata
            String[] label = new String[numberOfCol.intValue()];
            for (int i = 0; i < numberOfCol; i++) {
                label[i] = DEFAULT_LABEL + i;
                if (firstRowToExtractMetadata == 1) {
                    // String value = fields.get(i).getValue();
                    // if (!value.equals("")) {
                    // label[i] = value;
                    // }
                    if (numberOfCol <= fields.length) {// if current field size is greater than or equals bigest column
                        // size
                        if (fields[i] != null && !("").equals(fields[i])) { //$NON-NLS-1$
                            label[i] = fields[i].trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                            label[i] = MetadataToolHelper.validateColumnName(label[i], i);
                        } else {
                            label[i] = DEFAULT_LABEL + " " + i; //$NON-NLS-1$ 
                        }
                    } else {// current field size is less than bigest column size
                        if (i < fields.length) {
                            if (fields[i] != null && !("").equals(fields[i])) { //$NON-NLS-1$
                                label[i] = fields[i].trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                            } else {
                                label[i] = DEFAULT_LABEL + " " + i; //$NON-NLS-1$ 
                            }
                        } else {
                            label[i] = DEFAULT_LABEL + " " + i; //$NON-NLS-1$ 
                        }
                    }
                }
            }

            for (int i = 0; i < numberOfCol.intValue(); i++) {
                // define the first currentType and assimile it to globalType
                String globalType = null;
                int lengthValue = 0;
                int precisionValue = 0;

                int current = firstRowToExtractMetadata;
                while (globalType == null) {
                    if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                        if (i >= csvRows.get(current).length) {
                            globalType = "id_String"; //$NON-NLS-1$
                        } else {
                            globalType = JavaDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;
                            if (current == csvRows.size()) {
                                globalType = "id_String"; //$NON-NLS-1$
                            }
                        }
                    } else {
                        if (i >= csvRows.get(current).length) {
                            globalType = "String"; //$NON-NLS-1$
                        } else {
                            globalType = PerlDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;
                            if (current == csvRows.size()) {
                                globalType = "String"; //$NON-NLS-1$
                            }
                        }
                    }
                }

                // for another lines
                for (int f = firstRowToExtractMetadata; f < csvRows.size(); f++) {
                    fields = csvRows.get(f);
                    if (fields.length > i) {
                        String value = fields[i];
                        if (!value.equals("")) { //$NON-NLS-1$
                            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                                if (!JavaDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = JavaDataTypeHelper.getCommonType(globalType,
                                            JavaDataTypeHelper.getTalendTypeOfValue(value));
                                }
                            } else {
                                if (!PerlDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = PerlDataTypeHelper.getCommonType(globalType,
                                            PerlDataTypeHelper.getTalendTypeOfValue(value));
                                }
                            }
                            if (lengthValue < value.length()) {
                                lengthValue = value.length();
                            }
                            int positionDecimal = 0;
                            if (value.indexOf(',') > -1) {
                                positionDecimal = value.lastIndexOf(',');
                                precisionValue = lengthValue - positionDecimal;
                            } else if (value.indexOf('.') > -1) {
                                positionDecimal = value.lastIndexOf('.');
                                precisionValue = lengthValue - positionDecimal;
                            }
                        } else {
                            IPreferenceStore preferenceStore = null;
                            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreUIService.class)) {
                                IDesignerCoreUIService designerCoreUiService = (IDesignerCoreUIService) GlobalServiceRegister
                                        .getDefault().getService(IDesignerCoreUIService.class);
                                preferenceStore = designerCoreUiService.getPreferenceStore();
                            }
                            if (preferenceStore != null
                                    && preferenceStore.getString(MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE) != null
                                    && !preferenceStore.getString(MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE).equals("")) { //$NON-NLS-1$
                                globalType = preferenceStore.getString(MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE);
                                if (preferenceStore.getString(MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH) != null
                                        && !preferenceStore.getString(MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH)
                                                .equals("")) { //$NON-NLS-1$
                                    lengthValue = Integer.parseInt(preferenceStore
                                            .getString(MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH));
                                }
                            }
                        }
                    }
                }

                // define the metadataColumn to field i
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                // Convert javaType to TalendType
                String talendType = null;
                talendType = globalType;
                metadataColumn.setTalendType(talendType);
                metadataColumn.setLength(lengthValue);
                metadataColumn.setPrecision(0);
                // Check the label and add it to the table
                metadataColumn.setLabel(tableEditorView.getMetadataEditor().getNextGeneratedColumnName(label[i]));
                tableEditorView.getMetadataEditor().add(metadataColumn, i);
            }
        }
        checkFieldsValue();
        tableEditorView.getTableViewerCreator().layout();
        tableEditorView.getTableViewerCreator().getTableViewer().refresh();
        informationLabel.setText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$
    }

    // CALCULATE THE NULBER OF COLUMNS IN THE PREVIEW
    public Integer getRightFirstRow(List<String[]> csvRows) {

        Integer numbersOfColumns = null;
        int parserLine = csvRows.size();
        if (parserLine > 50) {
            parserLine = 50;
        }
        for (int i = 0; i < parserLine; i++) {
            if (csvRows.get(i) != null) {
                String[] nbRow = csvRows.get(i);
                // List<XmlField> nbRowFields = nbRow.getFields();
                if (numbersOfColumns == null || nbRow.length >= numbersOfColumns) {
                    numbersOfColumns = nbRow.length;
                }
            }
        }
        return numbersOfColumns;
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    @Override
    protected boolean checkFieldsValue() {
        if (metadataNameText.getCharCount() == 0) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlert")); //$NON-NLS-1$
            return false;
        } else if (!MetadataToolHelper.isValidSchemaName(metadataNameText.getText())) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlertIllegalChar")); //$NON-NLS-1$
            return false;
        } else if (isNameAllowed(metadataNameText.getText())) {
            updateStatus(IStatus.ERROR, Messages.getString("CommonWizard.nameAlreadyExist")); //$NON-NLS-1$
            return false;
        }

        if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {
            updateStatus(IStatus.OK, null);
            return true;
        }
        updateStatus(IStatus.ERROR, Messages.getString("FileStep3.itemAlert")); //$NON-NLS-1$
        return false;
    }

    public void saveMetaData() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }
}
