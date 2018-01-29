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
package org.talend.repository.ui.wizards.metadata.connection.files.ldif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.IRefreshable;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractLdifFileStepForm;

/**
 * @author cantoine
 * 
 */
public class LdifFileStep2Form extends AbstractLdifFileStepForm implements IRefreshable {

    private static Logger log = Logger.getLogger(LdifFileStep2Form.class);

    private static final String[] STRING_NUMBERS_DATA = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$
            "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /**
     * Fields use to preview.
     */
    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    public static List<String> itemTableName;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview ldifFilePreview;

    /**
     * Another.
     */

    private UtilsButton cancelButton;

    private boolean readOnly;

    private ExtendedTableModel<String> attributeModel;

    private AbstractExtendedTableViewer<String> tableEditorView;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    /**
     * For output tab.
     */

    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public LdifFileStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        int i = getConnection().getLimitEntry();
        if (i > 0) {
            rowsToSkipLimitCheckboxCombo.setText("" + getConnection().getLimitEntry()); //$NON-NLS-1$
        }
        checkFieldsValue();
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        // rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
    }

    /**
     * 
     * addGroupAttributes.
     */
    private void addGroupAttributes(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("LdifFileStep2Form.group.listAttributes"), height); //$NON-NLS-1$

        attributeModel = new ExtendedTableModel<String>();
        attributeModel.registerDataList(itemTableName);
        tableEditorView = new AbstractExtendedTableViewer<String>(attributeModel, group) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions
             * (org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<String> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setFirstColumnMasked(false);
                newTableViewerCreator.setCheckboxInFirstColumn(true);
            }

            @Override
            protected void createColumns(TableViewerCreator<String> tableViewerCreator, Table table) {
                TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setBeanPropertyAccessors(new IBeanPropertyAccessors<String, String>() {

                    @Override
                    public String get(String bean) {
                        return bean.toString();
                    }

                    @Override
                    public void set(String bean, String value) {
                    }

                });
                column.setTitle(Messages.getString("LdifFileStep2Form.columnTtitle.attributes")); //$NON-NLS-1$
                column.setWeight(100);

            }

        };
        // uncomment this block for activate the ToolBar MoveUp and MoveDown Lines
        // new ExtendedTableToolbarView(group, SWT.NONE, tableEditorView) {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see
        // org.talend.core.ui.extended.ExtendedTableToolbarView#createComponents(org.eclipse.swt.widgets.Composite)
        // */
        // @Override
        // protected void createComponents(Composite parent) {
        // moveUpButton = createMoveUpPushButton();
        // moveDownButton = createMoveDownPushButton();
        // }
        //
        // };

    }

    /**
     * populateLdifAttributes. method to populate the Table of Attributes to read the Ldif file
     * 
     */
    protected void populateLdifAttributes() throws IOException, Exception {

        itemTableName = new ArrayList<String>();

        String filePath = getConnection().getFilePath();
        if (isContextMode() && getContextModeManager() != null) {
            filePath = getContextModeManager().getOriginalValue(filePath);
            filePath = TalendQuoteUtils.removeQuotes(filePath);
        }

        Attributes entry = null;
        BufferedReader bufReader = null;

        bufReader = new BufferedReader(new FileReader(filePath), 1024);
        LDIFReader ldif = new LDIFReader(bufReader);
        itemTableName = new ArrayList<String>();

        // EVOLUTION cantoine : if we would add a LIMIT of ENTRY read, implement this limit by report with Limit Entry
        while ((entry = ldif.getNext()) != null) {
            NamingEnumeration idsEnum = entry.getIDs();
            while (idsEnum.hasMore()) {
                String attributeId = (String) idsEnum.next();
                if (!itemTableName.contains(attributeId)) {
                    itemTableName.add(attributeId);
                }
            }
        }
    }

    /**
     * checkTheRightAttributes.
     * 
     * @param getConnection().getValue() Checked Attribute Checked in EMF model
     */
    protected void checkTheRightAttributes(List<String> attribute) {

        TableItem[] tableItems = tableEditorView.getTableViewerCreator().getTable().getItems();
        for (TableItem tableItem : tableItems) {
            for (int i = 0; i < attribute.size(); i++) {
                String attributeName = attribute.get(i);
                if (attributeName != null && !("").equals(attributeName)) { //$NON-NLS-1$
                    if (tableItem.getText().equals(attributeName)) {
                        tableItem.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * add field to Group Limit.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupLimit(final Composite mainComposite, final int width, final int height) {
        // Composite Limited rows
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FileStep2.groupLimitOfRows"), height); //$NON-NLS-1$
        Composite compositeLimit = Form.startNewDimensionnedGridLayout(group, 3, width, height);

        // Information Limit
        Label info = new Label(compositeLimit, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        info.setLayoutData(gridData);
        info.setText(Messages.getString("FileStep2.groupLimitOfRowsTip")); //$NON-NLS-1$

        // Limit
        rowsToSkipLimitCheckboxCombo = new LabelledCheckboxCombo(compositeLimit, Messages.getString("FileStep2.limit"), Messages //$NON-NLS-1$
                .getString("FileStep2.limitTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupFileViewer(final Composite parent, final int width, int height) {

        tabFolder = new CTabFolder(parent, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("LdifFileStep2Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("LdifFileStep2Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        Composite compositeLdifFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Ldif File Preview Info
        previewButton = new Button(compositeLdifFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeLdifFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeLdifFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeLdifFilePreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, width, height);

        // Ldif File Preview
        ldifFilePreview = new ShadowProcessPreview(compositeLdifFilePreview, null, width, height - 10);
        ldifFilePreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    @Override
    protected void addFields() {

        // compositeFile Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);
        addGroupAttributes(mainComposite, 300, 115);
        // addGroupLimit(mainComposite, 300, 85);
        addGroupFileViewer(this, 700, 180);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * @param getConnection()
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription(LdifFileConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);
        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        ldifFilePreview.clearTablePreview();
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 4837 2007-07-27 05:40:31Z bqian $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        @Override
        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop")); //$NON-NLS-1$

            clearPreview();

            String filePath = getConnection().getFilePath();
            LdifFileConnection originalValueConnection = null;
            if (isContextMode() && getContextModeManager() != null) {
                filePath = getContextModeManager().getOriginalValue(getConnection().getFilePath());
                filePath = TalendQuoteUtils.removeQuotes(filePath);
                originalValueConnection = OtherConnectionContextUtils.cloneOriginalValueLdifFileConnection(getConnection(),
                        getContextModeManager().getSelectedContextType());
            }

            // if no file, the process don't be executed
            if (filePath == null || filePath.equals("")) { //$NON-NLS-1$
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            // if incomplete settings, , the process don't be executed
            if (!checkFieldsValue()) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
            if (originalValueConnection == null) {
                originalValueConnection = getConnection();
            }
            TableItem[] items = tableEditorView.getTableViewerCreator().getTable().getItems();
            for (TableItem item : items) {
                if (item.getChecked() && !getConnection().getValue().contains(item.getText())) {
                    getConnection().getValue().add(item.getText());
                }
            }
            processDescription = getProcessDescription(originalValueConnection);
            updateStatus(IStatus.ERROR, null);
            return true;
        }

        @Override
        public void nonUIProcessInThread() {
            // get the CsvArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_LDIF", true); //$NON-NLS-1$

            } catch (Exception e) {
                setException(e);
                log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        @Override
        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            if (getException() != null) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem);
                    }

                });
                return;
            }

            if (csvArray == null) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

                // refresh TablePreview on this step
                ldifFilePreview.refreshTablePreview(csvArray, false, processDescription);
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        @Override
        public void updateUIInThreadIfThreadFinally() {
            checkFieldsValue();
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                previewButton.setEnabled(true);
            }
        }

        @Override
        public void postProcessCancle() {
            checkFieldsValue();
            previewButton.setEnabled(false);
        }
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // addFieldsListenersGroupsRowToSkipAndLimit();
    }

    /**
     * DOC ocarbone Comment method "setCharFlag".
     * 
     * @param escapeCharFlag2
     * @param text
     */
    protected void setCharFlag(Label label, String string) {
        label.setText(string);
    }

    /**
     * add Controls to group Row To Skip and Limit.
     */
    private void addFieldsListenersGroupsRowToSkipAndLimit() {
        // Event modify
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        // Event : keyPressed
        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();

            // Event Key (numeric value only)
            labelledCheckboxCombo.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (Character.getNumericValue(e.character) >= 10) {
                        e.doit = false;
                    }
                }
            });
        }

        rowsToSkipLimitCheckboxCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!rowsToSkipLimitCheckboxCombo.isEmpty()) {
                    if (!rowsToSkipLimitCheckboxCombo.isInteger() || rowsToSkipLimitCheckboxCombo.getText().equals("0")) { //$NON-NLS-1$
                        rowsToSkipLimitCheckboxCombo.deselectAll();
                        getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                        getConnection().setLimitEntry(0);
                    } else {
                        getConnection().setLimitEntry(new Integer(rowsToSkipLimitCheckboxCombo.getText()));
                    }
                } else {
                    getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                    getConnection().setLimitEntry(0);
                }
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
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
        updateStatus(IStatus.OK, null);

        // Labelled Checkbox Combo (Row to Skip and Limit)
        // ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        // labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
        //
        // Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        // LabelledCheckboxCombo labelledCheckboxCombo;
        //
        // for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
        // labelledCheckboxCombo = iCheckboxCombo.next();
        // // if the checkbox is checked, check Numeric value
        // if (labelledCheckboxCombo.getCheckbox().getSelection()) {
        // if (labelledCheckboxCombo.getText() == "") {
        // updateStatus(IStatus.ERROR, labelledCheckboxCombo.getLabelText() +
        // Messages.getString("FileStep2.mustBePrecised"));
        // return false;
        // }
        // }
        // }
        previewInformationLabel.setText(""); //$NON-NLS-1$
        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refreshPreview();
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

        // Event checkBox action
        final Table table = tableEditorView.getTableViewerCreator().getTable();
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    boolean promptNeeded = tableItem.getChecked();
                    if (promptNeeded) {
                        getConnection().getValue().add(tableItem.getText());
                    } else {
                        getConnection().getValue().remove(tableItem.getText());
                    }
                }
                checkFieldsValue();
            }
        });
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

            try {
                populateLdifAttributes();
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("LdifFileStep2.previewFailure"), e.getMessage()); //$NON-NLS-1$
                log.error(Messages.getString("LdifFileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
                updateStatus(IStatus.ERROR, Messages.getString("LdifFileStep2.previewFailure")); //$NON-NLS-1$
            }
            attributeModel.registerDataList(itemTableName);

            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if (!isContextMode()) {
                if (getConnection().getFilePath() != null && !("").equals(getConnection().getFilePath()) //$NON-NLS-1$
                        && getConnection().getValue() != null && !getConnection().getValue().isEmpty()) {
                    refreshPreview();
                }
            }
            if (getConnection().getValue() != null && !getConnection().getValue().isEmpty()) {
                checkTheRightAttributes(getConnection().getValue());
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            adaptFormToEditable();
            collectParameters(visible);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.IRefreshable#refresh()
     */
    @Override
    public void refresh() {
        refreshPreview();

    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
    }

    @Override
    protected void exportAsContext() {
        super.exportAsContext();
        if (getContextModeManager() != null) {
            getContextModeManager().setDefaultContextType(getConnection());
        }
    }

    @Override
    protected void processWhenDispose() {
        if (processor != null) {
            processor.forceStop();
        }
    }
}
