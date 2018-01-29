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
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.ui.preference.metadata.MetadataTypeLengthConstants;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.core.utils.CsvArray;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.metadata.MetadataContextModeManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractWSDLSchemaStepForm;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class WebServiceStep2Form extends AbstractWSDLSchemaStepForm {

    private static Logger log = Logger.getLogger(WebServiceStep2Form.class);

    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private UtilsButton cancelButton;

    private UtilsButton guessButton;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    private Label informationLabel;

    private LabelledText metadataNameText;

    private LabelledText metadataCommentText;

    private boolean readOnly;

    private int maximumRowsToPreview = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore()
            .getInt(ITalendCorePrefConstants.PREVIEW_LIMIT);

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     */
    public WebServiceStep2Form(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null, null);
    }

    public WebServiceStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager,
            MetadataTable metadataTable) {

        super(parent, connectionItem, metadataTable, null);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm();
    }

    @Override
    protected void addFields() {

        // Header Fields
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, 60);
        metadataNameText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataName")); //$NON-NLS-1$
        metadataCommentText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataComment")); //$NON-NLS-1$

        // Group MetaData
        Group groupMetaData = Form.createGroup(this, 1, Messages.getString("FileStep3.groupMetadata"), 280); //$NON-NLS-1$
        Composite compositeMetaData = Form.startNewGridLayout(groupMetaData, 1);

        // Composite Guess
        Composite compositeGuessButton = Form.startNewDimensionnedGridLayout(compositeMetaData, 2, WIDTH_GRIDDATA_PIXEL, 40);
        informationLabel = new Label(compositeGuessButton, SWT.NONE);
        informationLabel
                .setText(Messages.getString("FileStep3.informationLabel") + "                                                  "); //$NON-NLS-1$ //$NON-NLS-2$
        informationLabel.setSize(500, HEIGHT_BUTTON_PIXEL);

        guessButton = new UtilsButton(compositeGuessButton, Messages.getString("FileStep3.guess"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        guessButton.setToolTipText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$

        // Composite MetadataTableEditorView
        Composite compositeTable = Form.startNewDimensionnedGridLayout(compositeMetaData, 1, WIDTH_GRIDDATA_PIXEL, 200);
        compositeTable.setLayout(new FillLayout());
        metadataEditor = new MetadataEmfTableEditor(Messages.getString("FileStep3.metadataDescription")); //$NON-NLS-1$
        tableEditorView = new MetadataEmfTableEditorView(compositeTable, SWT.NONE);

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
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event guessButton
        guessButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        if (MessageDialog.openConfirm(getShell(), Messages.getString("FileStep3.guessConfirmation"), Messages //$NON-NLS-1$
                                .getString("FileStep3.guessConfirmationMessage"))) { //$NON-NLS-1$
                            // runShadowProcess();
                        }
                    } else {
                        guessButton.setEnabled(false);
                    }

                } else {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        // runShadowProcess();
                    } else {
                        guessButton.setEnabled(false);
                    }
                }
            }

        });
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

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {
        // init the metadata Table
        Set<org.talend.core.model.metadata.builder.connection.MetadataTable> tables = ConnectionHelper.getTables(connection);
        Iterator it = tables.iterator();
        while (it.hasNext()) {
            org.talend.core.model.metadata.builder.connection.MetadataTable metadatatable = (org.talend.core.model.metadata.builder.connection.MetadataTable) it
                    .next();
            if (metadatatable.getLabel().equals("Output")) {
                metadataTable = metadatatable;
            }
        }
        if (metadataTable != null) {
            String label = MetadataToolHelper.validateValue(metadataTable.getLabel());
            metadataNameText.setText(label);
            metadataCommentText.setText(metadataTable.getComment());
            metadataEditor.setMetadataTable(metadataTable);
            tableEditorView.setMetadataEditor(metadataEditor);
            tableEditorView.getTableViewerCreator().layout();
        }
        if (getConnection().isReadOnly()) {
            adaptFormToReadOnly();
        } else {
            updateStatus(IStatus.OK, null);
        }
    }

    /**
     * run a ShadowProcess to determined the Metadata.
     */
    protected void runShadowProcess() {
        initGuessSchema();
        try {
            informationLabel.setText("   " + Messages.getString("FileStep3.guessProgress")); //$NON-NLS-1$ //$NON-NLS-2$

            // get the XmlArray width an adapt ProcessDescription
            ProcessDescription processDescription = getProcessDescription();
            CsvArray csvArray = ShadowProcessHelper.getCsvArray(processDescription, "WSDL_SCHEMA"); //$NON-NLS-1$
            if (csvArray == null) {
                informationLabel.setText("   " + Messages.getString("FileStep3.guessFailure")); //$NON-NLS-1$ //$NON-NLS-2$

            } else {
                refreshMetaDataTable(csvArray, processDescription);
            }

        } catch (CoreException e) {
            if (getParent().getChildren().length == 1) {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileStep3.guessFailureTip2"), e.getMessage()); //$NON-NLS-1$
            } else {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip"), e.getMessage()); //$NON-NLS-1$
            }
            log.error(Messages.getString("FileStep3.guessFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        checkFieldsValue();
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

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getMetadataEditor().addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });
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

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getOriginalValueConnection());
        return processDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        guessButton.setEnabled(!isReadOnly());
        metadataNameText.setReadOnly(isReadOnly());
        metadataCommentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());
        boolean inputModel = ((WSDLSchemaConnection) connectionItem.getConnection()).isIsInputModel();
        metadataNameText.setReadOnly(!inputModel);
    }

    /**
     * DOC ocarbone Comment method "refreshMetaData".
     * 
     * @param csvArray
     */
    public void refreshMetaDataTable(final CsvArray csvArray, ProcessDescription processDescription) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

        // clear all items
        tableEditorView.getMetadataEditor().removeAll();

        List<MetadataColumn> columns = new ArrayList<MetadataColumn>();

        if (csvArray == null) {
            return;
        } else {

            List<String[]> csvRows = csvArray.getRows();
            if (csvRows.size() == 0) {
                return;
            }
            String[] fields = null;
            try {
                fields = csvRows.get(0);
            } catch (Exception e) {
                MessageBoxExceptionHandler.process(e);
                return;
            }
            // int numberOfCol = fields.size();

            Integer numberOfCol = getRightFirstRow(csvRows);

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;

            // the first rows is used to define the label of any metadata
            String[] label = new String[numberOfCol.intValue()];
            for (int i = 0; i < numberOfCol; i++) {
                label[i] = DEFAULT_LABEL + i;
                if (firstRowToExtractMetadata == 0) {
                    label[i] = "" + processDescription.getSchema().get(0).getListColumns().get(i); //$NON-NLS-1$
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
                columns.add(i, metadataColumn);
            }
        }
        tableEditorView.getMetadataEditor().addAll(columns);
        checkFieldsValue();
        tableEditorView.getTableViewerCreator().layout();
        tableEditorView.getTableViewerCreator().getTable().deselectAll();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {
            initialize();
            if ((tableEditorView.getMetadataEditor().getBeanCount() <= 0)) {
                // runShadowProcess();
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            if (visible) {
                collectContextParams(visible);
            }
        }
    }

    protected void initGuessSchema() {
        if (getParent().getChildren().length == 1) { // only open table
            if (getContextModeManager() == null) { // first
                setContextModeManager(new MetadataContextModeManager());
                ConnectionContextHelper.checkContextMode(connectionItem);
            }
            if (connectionItem.getConnection().isContextMode()) {
                ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(getShell(),
                        connectionItem.getConnection());
                getContextModeManager().setSelectedContextType(contextTypeForContextMode);
            }

        }
    }
}
