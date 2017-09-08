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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.preview.ExcelSchemaBean;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.FileConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.FileConnectionContextUtils.EFileParamName;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.IRefreshable;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractExcelFileStepForm;

/**
 * @author xye
 * 
 */
public class ExcelFileStep2Form extends AbstractExcelFileStepForm implements IRefreshable {

    /**
     * DOC YeXiaowei ExcelFileStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     */
    public ExcelFileStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

    public ExcelFileStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager,
            ExcelSchemaBean bean) {
        super(parent, connectionItem);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
        this.bean = bean;
    }

    private static Logger log = Logger.getLogger(ExcelFileStep2Form.class);

    private static final String EMPTY_VALUE = Messages.getString("FileStep2.empty"); //$NON-NLS-1$

    private static final String[] ESCAPE_CHAR_DATA = { EMPTY_VALUE,
            TalendQuoteUtils.addQuotes("\""), TalendQuoteUtils.addQuotes("\'"), TalendQuoteUtils.addQuotes("\\\\") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String[] STRING_NUMBERS_DATA = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            "14", "15", "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

    /**
     * Main Fields.
     */
    private LabelledCombo encodingCombo;

    private LabelledCheckboxCombo rowsToSkipHeaderCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipFooterCheckboxCombo;

    private Button advanceSeparatorCheckbox;

    private LabelledText thousandSeparaotrText;

    private LabelledText decimalSeparatorText;

    private LabelledText firstColumnText;

    private LabelledText lastColumnText;

    /**
     * Fields use to preview.
     */

    private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview excelProcessPreview;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private UtilsButton cancelButton;

    private boolean readOnly;

    private List<String> originSchemaColumns = new ArrayList<String>();

    /**
     * Output tab.
     */
    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    private ExcelSchemaBean bean;

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        // Fields to the Group Delimited File Settings
        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
            getConnection().setEncoding(encodingCombo.getText());
        }

        // Fields to the Group Rows To Skip
        boolean flag = false;
        flag = initRowsToSkip(rowsToSkipHeaderCheckboxCombo, getConnection().getHeaderValue());
        getConnection().setUseHeader(flag);
        flag = initRowsToSkip(rowsToSkipFooterCheckboxCombo, getConnection().getFooterValue());
        getConnection().setUseFooter(flag);
        flag = initRowsToSkip(rowsToSkipLimitCheckboxCombo, getConnection().getLimitValue());
        getConnection().setUseLimit(flag);

        firstRowIsCaptionCheckbox.setSelection(getConnection().isFirstLineCaption());
        advanceSeparatorCheckbox.setSelection(getConnection().isAdvancedSpearator());

        if (isContextMode()) {
            firstColumnText.setText(getConnection().getFirstColumn());
            lastColumnText.setText(getConnection().getLastColumn());
            thousandSeparaotrText.setText(getConnection().getThousandSeparator());
            decimalSeparatorText.setText(getConnection().getDecimalSeparator());

        } else {
            int firstColumn = ConnectionContextHelper.convertValue(getConnection().getFirstColumn());
            firstColumnText.setText(firstColumn < 0 ? "1" : getConnection().getFirstColumn()); //$NON-NLS-1$
            getConnection().setFirstColumn(firstColumnText.getText());

            int lastColumn = ConnectionContextHelper.convertValue(getConnection().getLastColumn());
            lastColumnText.setText(lastColumn < 0 ? "" : getConnection().getLastColumn()); //$NON-NLS-1$

            String ts = this.getConnection().getThousandSeparator();
            if (ts == null || ts.equals("")) { //$NON-NLS-1$
                thousandSeparaotrText.setText("\',\'"); //$NON-NLS-1$
                getConnection().setThousandSeparator(thousandSeparaotrText.getText());
            } else {
                thousandSeparaotrText.setText(ts);
            }

            String ds = this.getConnection().getDecimalSeparator();
            if (ds == null || ds.equals("")) { //$NON-NLS-1$
                decimalSeparatorText.setText("\'.\'"); //$NON-NLS-1$
                getConnection().setDecimalSeparator(decimalSeparatorText.getText());
            } else {
                decimalSeparatorText.setText(ds);
            }
            thousandSeparaotrText.setEnabled(advanceSeparatorCheckbox.getSelection());
            decimalSeparatorText.setEnabled(advanceSeparatorCheckbox.getSelection());
        }
        checkFieldsValue();
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        encodingCombo.setReadOnly(isReadOnly());
        // rowsToSkipHeaderCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipFooterCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
        firstRowIsCaptionCheckbox.setEnabled(!isReadOnly());
    }

    /**
     * add Field to Group Delimited File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupDelimitedFileSettings(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupDelimitedFileSettings"), height); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.heightHint = -1;
        groupLayoutData.minimumHeight = -1;
        groupLayoutData.minimumWidth = -1;
        groupLayoutData.widthHint = -1;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeFileDelimitor = Form.startNewDimensionnedGridLayout(group, 4, width, height);
        // for the layout of group has been changed to FillLayout from GridLayout, the layout data must be changed too
        compositeFileDelimitor.setLayoutData(null);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                .getString("FileStep2.encodingTip"), encodingData, 3, true, SWT.NONE); //$NON-NLS-1$

        advanceSeparatorCheckbox = new Button(compositeFileDelimitor, SWT.CHECK);
        advanceSeparatorCheckbox.setText(Messages.getString("ExcelFileStep2Form.advancesSeparator")); //$NON-NLS-1$

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        advanceSeparatorCheckbox.setLayoutData(data);

        thousandSeparaotrText = new LabelledText(compositeFileDelimitor,
                Messages.getString("ExcelFileStep2Form.thousandsSeparator"), 3); //$NON-NLS-1$

        decimalSeparatorText = new LabelledText(compositeFileDelimitor,
                Messages.getString("ExcelFileStep2Form.decimalSeparator"), 3); //$NON-NLS-1$

        // Only visible if current project is not perl project
        advanceSeparatorCheckbox.setVisible(!isPerlProject());
        thousandSeparaotrText.setVisible(!isPerlProject());
        decimalSeparatorText.setVisible(!isPerlProject());
        group.pack();
    }

    private void addGroupDieOnErrorSettings(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("ExcelFileStep2Form.metadataSetting")); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.heightHint = -1;
        groupLayoutData.minimumHeight = -1;
        groupLayoutData.minimumWidth = -1;
        groupLayoutData.widthHint = -1;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeExt = Form.startNewDimensionnedGridLayout(group, 4, width, height);
        // for the layout of group has been changed to FillLayout from GridLayout, the layout data must be changed too.
        compositeExt.setLayoutData(null);

        firstColumnText = new LabelledText(compositeExt, Messages.getString("ExcelFileStep2Form.firstColumn"), 3); //$NON-NLS-1$
        firstColumnText.setText("1"); // Default 1 //$NON-NLS-1$

        lastColumnText = new LabelledText(compositeExt, Messages.getString("ExcelFileStep2Form.lastColumn"), 3); // Default no value //$NON-NLS-1$
        group.pack();
    }

    private void addGroupRowsToSkip(final Composite mainComposite, final int width, final int height) {
        // compositerowsToSkip Main Fields
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FileStep2.groupRowsToSkip"), height); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.heightHint = -1;
        groupLayoutData.minimumHeight = -1;
        groupLayoutData.minimumWidth = -1;
        groupLayoutData.widthHint = -1;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeRowsToSkip = Form.startNewDimensionnedGridLayout(group, 3, width - 100, height);
        // for the layout of group has been changed to FillLayout from GridLayout, the layout data must be changed too.
        compositeRowsToSkip.setLayoutData(null);

        // Information rowsToSkip
        Label info = new Label(compositeRowsToSkip, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        info.setLayoutData(gridData);
        info.setText(Messages.getString("FileStep2.rowsToSkipTip")); //$NON-NLS-1$

        // Header
        rowsToSkipHeaderCheckboxCombo = new LabelledCheckboxCombo(compositeRowsToSkip, Messages.getString("FileStep2.header"), //$NON-NLS-1$
                Messages.getString("FileStep2.headerTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$

        // Footer
        rowsToSkipFooterCheckboxCombo = new LabelledCheckboxCombo(compositeRowsToSkip, Messages.getString("FileStep2.footer"), //$NON-NLS-1$
                Messages.getString("FileStep2.footerTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$      

        group.pack();
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
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupLimitOfRows"), height); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.heightHint = -1;
        groupLayoutData.minimumHeight = -1;
        groupLayoutData.minimumWidth = -1;
        groupLayoutData.widthHint = -1;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeLimit = Form.startNewDimensionnedGridLayout(group, 3, width, height);
        // for the layout of group has been changed to FillLayout from GridLayout, the layout data must be changed too.
        compositeLimit.setLayoutData(null);

        // Information Limit
        Label info = new Label(compositeLimit, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        info.setLayoutData(gridData);
        info.setText(Messages.getString("FileStep2.groupLimitOfRowsTip")); //$NON-NLS-1$

        // Limit
        rowsToSkipLimitCheckboxCombo = new LabelledCheckboxCombo(compositeLimit, Messages.getString("FileStep2.limit"), Messages //$NON-NLS-1$
                .getString("FileStep2.limitTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$
        group.pack();
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
        GridData tabFolderLayoutData = (GridData) tabFolder.getLayoutData();
        tabFolderLayoutData.heightHint = height;

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("ExcelFileStep2Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("ExcelFileStep2Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        // composite Delimited File Preview
        // previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$
        Composite compositeDelimitedFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Delimited File Preview Info
        firstRowIsCaptionCheckbox = new Button(compositeDelimitedFilePreviewButton, SWT.CHECK);
        firstRowIsCaptionCheckbox.setText(Messages.getString("FileStep2.firstRowsIsCaption")); //$NON-NLS-1$
        firstRowIsCaptionCheckbox.setAlignment(SWT.LEFT);

        previewButton = new Button(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeDelimitedFilePreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, width, height);

        // Delimited File Preview
        excelProcessPreview = new ShadowProcessPreview(compositeDelimitedFilePreview, null, width, height - 10);
        excelProcessPreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    @Override
    protected void addFields() {

        Composite mainComposite = Form.startNewGridLayout(this, 2);

        addGroupDelimitedFileSettings(mainComposite, 280, 120);
        addGroupRowsToSkip(mainComposite, 300, 120);
        addGroupDieOnErrorSettings(mainComposite, 280, 67);
        addGroupLimit(mainComposite, 300, 67);
        GridLayout mainCompositeLayout = (GridLayout) mainComposite.getLayout();
        mainCompositeLayout.marginBottom = 5;

        Composite mainComposite2 = Form.startNewGridLayout(this, 1);

        addGroupFileViewer(mainComposite2, 600, 200);

        if (!isInWizard()) {
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /**
     * Set field advanced separator, decimal separator , thousand separator DOC YeXiaowei Comment method
     * "getProcessDescription".
     * 
     * @return
     */
    private ProcessDescription getProcessDescription(FileExcelConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);

        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        Integer i = 0;
        if (originalValueConnection.isUseHeader()) {
            i = ConnectionContextHelper.convertValue(originalValueConnection.getHeaderValue());
        }
        if (originalValueConnection.isFirstLineCaption()) {
            i--;
        }
        processDescription.setHeaderRow(i);

        if (originalValueConnection.isUseFooter()) {
            processDescription.setFooterRow(ConnectionContextHelper.convertValue(originalValueConnection.getFooterValue()));
        }

        // adapt the limit to the preview
        processDescription.setLimitRows(maximumRowsToPreview);
        if (originalValueConnection.isUseLimit()) {
            i = new Integer(originalValueConnection.getLimitValue());
            if (originalValueConnection.isFirstLineCaption()) {
                i++;
            }
            if (i < maximumRowsToPreview) {
                processDescription.setLimitRows(i);
            }
        }

        processDescription.setEncoding(TalendQuoteUtils.addQuotes(originalValueConnection.getEncoding()));
        processDescription.setRemoveEmptyRow(originalValueConnection.isRemoveEmptyRow());

        if (bean == null) {
            bean = new ExcelSchemaBean();
        }

        bean.setSheetName(originalValueConnection.getSheetName());
        bean.setFirstColumn(originalValueConnection.getFirstColumn());
        bean.setLastColumn(originalValueConnection.getLastColumn());

        bean.setAdvancedSeparator(originalValueConnection.isAdvancedSpearator());
        bean.setThousandSeparator(originalValueConnection.getThousandSeparator());
        bean.setDecimalSeparator(originalValueConnection.getDecimalSeparator());

        bean.setSelectAllSheets(originalValueConnection.isSelectAllSheets());
        bean.setSheetsList(originalValueConnection.getSheetList());

        processDescription.setExcelSchemaBean(bean);

        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        excelProcessPreview.clearTablePreview();
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
        addFieldsListenersGroupDelimitedFileSettings();
        addFieldsListenersGroupsRowToSkipAndLimit();
        addFieldsListenersDieOnError();
        addFieldsListenersGroupsFileViewer();
    }

    /**
     * add Controls to group File Viewer.
     */
    private void addFieldsListenersGroupsFileViewer() {
        // Manage rowsToSkipHeader when firstRowIsCaption is checked
        firstRowIsCaptionCheckbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {

                getConnection().setFirstLineCaption(firstRowIsCaptionCheckbox.getSelection());
                if (!isContextMode()) {
                    if (firstRowIsCaptionCheckbox.getSelection()) {
                        // when firstRowIsCaption is checked
                        if (rowsToSkipHeaderCheckboxCombo.isEmpty()) {
                            // at least, rowsToSkipHeader = 1
                            rowsToSkipHeaderCheckboxCombo.setText("1"); //$NON-NLS-1$
                            getConnection().setHeaderValue("" + 1); //$NON-NLS-1$
                        } else {
                            // rowsToSkipHeader ++
                            int value = new Integer(rowsToSkipHeaderCheckboxCombo.getText());
                            value++;
                            String newValue = new String("" + value); //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.setText(newValue);
                            getConnection().setHeaderValue(rowsToSkipHeaderCheckboxCombo.getText());
                        }
                    } else {
                        // when firstRowIsCaption isn't checked
                        if (rowsToSkipHeaderCheckboxCombo.getText().equals("1")) { //$NON-NLS-1$
                            // rowsToSkipHeader is unusable
                            rowsToSkipHeaderCheckboxCombo.deselectAll();
                            getConnection().setHeaderValue("" + 0); //$NON-NLS-1$
                        } else {
                            // rowsToSkipHeader --
                            int value = new Integer(rowsToSkipHeaderCheckboxCombo.getText());
                            value--;
                            String newValue = new String("" + value); //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.setText(newValue);
                            getConnection().setHeaderValue(rowsToSkipHeaderCheckboxCombo.getText());
                        }
                    }
                    checkFieldsValue();
                }
            }
        });
    }

    private void addFieldsListenersDieOnError() {
        firstColumnText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setFirstColumn(firstColumnText.getText());
                    checkFieldsValue();
                }
            }

        });

        lastColumnText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setLastColumn(lastColumnText.getText());
                    checkFieldsValue();
                }
            }
        });
    }

    private void chopSchemaColumn() {
        String text = firstColumnText.getText();
        if (isContextMode() && getContextModeManager() != null) {
            text = getContextModeManager().getOriginalValue(text);
        }
        int first = getIntFromString(text);

        text = lastColumnText.getText();
        if (isContextMode() && getContextModeManager() != null) {
            text = getContextModeManager().getOriginalValue(text);
        }
        int last = getIntFromString(lastColumnText.getText());

        if (last <= 0 || last > originSchemaColumns.size()) {
            last = originSchemaColumns.size();
        }

        List<String> schemaColumns = new ArrayList<String>();
        for (int i = first - 1; i < last; i++) {
            schemaColumns.add(originSchemaColumns.get(i));
        }

        getConnection().getSheetColumns().clear();
        getConnection().getSheetColumns().addAll(schemaColumns);
    }

    /**
     * DOC xye Comment method "setCharFlag".
     * 
     * @param escapeCharFlag2
     * @param text
     */
    protected void setCharFlag(Label label, String string) {
        // PTODO OCA : when is utils in the Flag ?
        label.setText(string);
    }

    /**
     * add Controls to group Row To Skip and Limit.
     */
    private void addFieldsListenersGroupsRowToSkipAndLimit() {
        // Event modify
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipHeaderCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipFooterCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        // Event : keyPressed
        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();

            labelledCheckboxCombo.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (!isContextMode()) {
                        String string = String.valueOf(e.character);
                        // Check if input is number, backspace key and delete key of keyboard.
                        if (!(string.matches("[0-9]*")) && e.keyCode != 8 && e.keyCode != SWT.DEL) { //$NON-NLS-1$
                            e.doit = false;
                        }
                    }
                }
            });
        }

        // Event : Modify (to control the use of Ctrl V)
        rowsToSkipHeaderCheckboxCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!rowsToSkipHeaderCheckboxCombo.isEmpty()) {
                        if (!rowsToSkipHeaderCheckboxCombo.isInteger()
                                || rowsToSkipHeaderCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.deselectAll();
                            getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                            getConnection().setHeaderValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("ExcelFileStep2Form.onlyNumber")); //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.getCombo().setFocus();

                            // if rowsHeaderToSkip isn't integer or is equals to 0, the firstRowIsCaptionCheckbox is
                            // unusable.
                            firstRowIsCaptionCheckbox.setSelection(false);
                            getConnection().setFirstLineCaption(false);
                            return;
                        } else {
                            getConnection().setHeaderValue(rowsToSkipHeaderCheckboxCombo.getText().trim());
                            getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                            checkFieldsValue();
                        }
                    } else {
                        getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                        getConnection().setHeaderValue("" + 0); //$NON-NLS-1$
                        checkFieldsValue();
                    }
                }
            }
        });

        rowsToSkipFooterCheckboxCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!rowsToSkipFooterCheckboxCombo.isEmpty()) {
                        if (!rowsToSkipFooterCheckboxCombo.isInteger()
                                || rowsToSkipFooterCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                            rowsToSkipFooterCheckboxCombo.deselectAll();
                            getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                            getConnection().setFooterValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("ExcelFileStep2Form.onlyNumber")); //$NON-NLS-1$
                            rowsToSkipFooterCheckboxCombo.getCombo().setFocus();
                        } else {
                            getConnection().setFooterValue(rowsToSkipFooterCheckboxCombo.getText().trim());
                        }
                    } else {
                        getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                        getConnection().setFooterValue("" + 0); //$NON-NLS-1$
                    }
                    checkFieldsValue();
                }
            }
        });

        rowsToSkipLimitCheckboxCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!rowsToSkipLimitCheckboxCombo.isEmpty()) {
                        if (!rowsToSkipLimitCheckboxCombo.isInteger()
                                || rowsToSkipLimitCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                            rowsToSkipLimitCheckboxCombo.deselectAll();
                            getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                            getConnection().setLimitValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("ExcelFileStep2Form.onlyNumber")); //$NON-NLS-1$
                            rowsToSkipLimitCheckboxCombo.getCombo().setFocus();
                        } else {
                            getConnection().setLimitValue(rowsToSkipLimitCheckboxCombo.getText());
                        }
                    } else {
                        getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                        getConnection().setLimitValue("" + 0); //$NON-NLS-1$
                    }
                    checkFieldsValue();
                }
            }
        });

        // If nothing in rowsToSkipHeader, the firstRowIsCaption mustn't be checked
        rowsToSkipHeaderCheckboxCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                String text = rowsToSkipHeaderCheckboxCombo.getText();
                if (isContextMode() && getContextModeManager() != null) {
                    text = getContextModeManager().getOriginalValue(text);
                }
                if ((!rowsToSkipHeaderCheckboxCombo.isChecked()) || text.trim().equals("0")) { //$NON-NLS-1$
                    firstRowIsCaptionCheckbox.setSelection(false);
                    getConnection().setFirstLineCaption(false);
                }

                getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                checkRowToSkip();
            }
        });
        rowsToSkipFooterCheckboxCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                checkRowToSkip();
            }
        });
        rowsToSkipLimitCheckboxCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                checkRowToSkip();
            }
        });
    }

    /**
     * add Controls of Group Delimited File Settings.
     */
    private void addFieldsListenersGroupDelimitedFileSettings() {
        // Event encodingCombo
        encodingCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setEncoding(encodingCombo.getText());
                    checkFieldsValue();
                }
            }
        });

        advanceSeparatorCheckbox.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {

                boolean select = advanceSeparatorCheckbox.getSelection();
                getConnection().setAdvancedSpearator(select);
                if (!isContextMode()) {
                    decimalSeparatorText.setEnabled(select);
                    thousandSeparaotrText.setEnabled(select);
                    getConnection().setThousandSeparator(thousandSeparaotrText.getText());
                    getConnection().setDecimalSeparator(decimalSeparatorText.getText());
                    checkFieldsValue();
                }
            }
        });

        decimalSeparatorText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setDecimalSeparator(decimalSeparatorText.getText());
                    checkFieldsValue();
                }
            }

        });

        thousandSeparaotrText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setThousandSeparator(thousandSeparaotrText.getText());
                    checkFieldsValue();
                }
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
        previewButton.setEnabled(false);

        // Labelled Checkbox Combo (Row to Skip and Limit)
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipHeaderCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipFooterCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();
            // if the checkbox is checked, check Numeric value
            if (labelledCheckboxCombo.getCheckbox().getSelection()) {
                if (labelledCheckboxCombo.getText() == "") { //$NON-NLS-1$
                    updateStatus(IStatus.ERROR,
                            labelledCheckboxCombo.getLabelText() + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$ //$NON-NLS-2$
                    return false;
                }
            }
        }

        if (!checkAdvancedSetting()) {
            return false;
        }

        if (!checkFristAndLastColumn()) {
            return false;
        }

        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);
        updateStatus(IStatus.OK, null);
        return true;
    }

    private int getIntFromString(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean checkFristAndLastColumn() {
        if (isContextMode()) {
            return true;
        }
        int first = getIntFromString(firstColumnText.getText());

        int last = -1;
        if (StringUtils.isBlank(lastColumnText.getText())) {
            last = originSchemaColumns.size();
        } else {
            last = getIntFromString(lastColumnText.getText());
        }
        // see the bug 5446,qli comment.
        if (last < first || last <= 0 || first <= 0) {
            updateStatus(IStatus.ERROR, "Last column or First column parameter error"); //$NON-NLS-1$
            return false;
        }
        if (originSchemaColumns.size() < last) {
            updateStatus(IStatus.ERROR, "Last column parameter error. Bigger than schmea columns size"); //$NON-NLS-1$
            return false;
        }

        return true;

    }

    /**
     * DOC YeXiaowei Comment method "checkAdvancedSetting".
     */
    private boolean checkAdvancedSetting() {
        if (isContextMode()) {
            return true;
        }
        String thouandsSeparator = thousandSeparaotrText.getText();
        if (thouandsSeparator == null) {
            updateStatus(IStatus.ERROR, "Thousand Separator" + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }

        String decimalSeparator = decimalSeparatorText.getText();
        if (decimalSeparator == null) {
            updateStatus(IStatus.ERROR, "Decimal Separator" + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }

        return true;
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 8214 2008-01-19 02:15:27Z qwei $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        String previewInformationLabelMsg = null;

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        boolean firstRowIsCatption = false;

        @Override
        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop")); //$NON-NLS-1$

            clearPreview();
            String filePath = getConnection().getFilePath();
            FileExcelConnection originalValueConnection = null;
            if (isContextMode()) {
                boolean found = false;
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(getShell(), getConnection(),
                        getConnection().getContextName(), false);
                if (contextType != null) {
                    if (getContextModeManager() != null) {
                        getContextModeManager().setSelectedContextType(contextType);
                        filePath = getContextModeManager().getOriginalValue(getConnection().getFilePath());
                        filePath = TalendQuoteUtils.removeQuotes(filePath);
                        found = true;
                    }
                    setConnectionProperties();
                    originalValueConnection = (FileExcelConnection) FileConnectionContextUtils.cloneOriginalValueConnection(
                            getConnection(), contextType);
                }
                if (!found) {
                    filePath = null;
                }
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
            firstRowIsCatption = firstRowIsCaptionCheckbox.getSelection();
            if (originalValueConnection == null) {
                originalValueConnection = getConnection();
            }
            processDescription = getProcessDescription(originalValueConnection);
            updateStatus(IStatus.ERROR, null);
            return true;
        }

        @Override
        public void nonUIProcessInThread() {
            // get the XmlArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_EXCEL", true); //$NON-NLS-1$
                if (csvArray == null) {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$

                    // refresh TablePreview on this step
                    previewInformationLabelMsg = ""; //$NON-NLS-1$
                }
            } catch (Exception ex) {
                setException(ex);
                previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                log.error(Messages.getString("FileStep2.previewFailure") + " " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
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
            previewInformationLabel.setText(previewInformationLabelMsg);
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
                excelProcessPreview.refreshTablePreview(csvArray, getConnection().isFirstLineCaption(), processDescription);
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
                // chopSchemaColumn();
                processor.execute();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {

        super.setVisible(visible);

        if (super.isVisible()) {

            // Fields to the Group Delimited File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(getConnection().getEncoding());
            } else {
                encodingCombo.select(0);
                getConnection().setEncoding(encodingCombo.getText());
            }

            advanceSeparatorCheckbox.setSelection(getConnection().isAdvancedSpearator());
            if (!isContextMode()) {
                thousandSeparaotrText.setEnabled(advanceSeparatorCheckbox.getSelection());
                decimalSeparatorText.setEnabled(advanceSeparatorCheckbox.getSelection());
            }
            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            String filePath = getConnection().getFilePath();
            if (isContextMode() && getContextModeManager() != null) {
                filePath = getContextModeManager().getOriginalValue(filePath);
            }
            // if the don't select all and first element is not the select sheet name, should order.
            String sheetName = getConnection().getSheetName();
            ArrayList sheetList = getConnection().getSheetList();
            if (sheetList.contains(sheetName) && sheetList.size() > 1) {
                sheetList.remove(sheetName);
                sheetList.add(0, sheetName);
            }
            if ((!"".equals(filePath)) && (filePath != null)) { //$NON-NLS-1$
                saveOriginShcemaColumns();
                // chopSchemaColumn();
                if (!isContextMode()) {
                    refreshPreview();
                }
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }

            adaptFormToEditable();

        }
    }

    /**
     * DOC YeXiaowei Comment method "saveOriginShcemaColumns".
     */
    private void saveOriginShcemaColumns() {
        List<String> columns = getConnection().getSheetColumns();
        originSchemaColumns.clear();
        originSchemaColumns.addAll(columns);

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
        encodingCombo.setReadOnly(isContextMode());

        thousandSeparaotrText.setEditable(!isContextMode());
        decimalSeparatorText.setEditable(!isContextMode());
        firstColumnText.setEditable(!isContextMode());
        lastColumnText.setEditable(!isContextMode());

        checkRowToSkip();
    }

    private void checkRowToSkip() {
        if (isContextMode()) {
            rowsToSkipHeaderCheckboxCombo.getCombo().setEnabled(!isContextMode());
            rowsToSkipLimitCheckboxCombo.getCombo().setEnabled(!isContextMode());
            rowsToSkipFooterCheckboxCombo.getCombo().setEnabled(!isContextMode());
        }
        rowsToSkipHeaderCheckboxCombo.getCheckbox().setEnabled(!isContextMode());
        rowsToSkipLimitCheckboxCombo.getCheckbox().setEnabled(!isContextMode());
        rowsToSkipFooterCheckboxCombo.getCheckbox().setEnabled(!isContextMode());

    }

    private void setConnectionProperties() {
        String filePath = null;
        if (isContextMode() && getContextModeManager() != null) {
            filePath = getContextModeManager().getOriginalValue(getConnection().getFilePath());
            filePath = TalendQuoteUtils.removeQuotes(filePath);
            filePath = PathUtils.getPortablePath(filePath);
        } else {
            filePath = PathUtils.getPortablePath(getConnection().getFilePath());
        }
        ExcelReader excelReader;
        try {
            excelReader = new ExcelReader(filePath);
            String[] sheets = excelReader.getSheetNames();

            String sheetOrigin = getConnection().getSheetName();
            if (sheetOrigin == null || sheetOrigin.equals("") || !Arrays.asList(sheets).contains(sheetOrigin)) { //$NON-NLS-1$
                getConnection().setSheetName(sheets[0]);
            }

            final List<String[]> input = excelReader.readSheet(getConnection().getSheetName());
            if (input == null) {
                return;
            }
            int maxLength = 0;
            for (int i = 0, z = input.size(); i < z; i++) {
                int x = input.get(i).length;
                if (x > maxLength) {
                    maxLength = x;
                }
            }
            String[] names = ExcelReader.getColumnsTitle(maxLength);
            List columns = getConnection().getSheetColumns();
            columns.clear();

            for (String name : names) {
                columns.add(name);
            }
        } catch (BiffException e) {
            //
        } catch (IOException e) {
            //
        }

    }

    @Override
    protected void collectConnParams() {
        super.collectConnParams();
        addContextParams(EFileParamName.FirstColumn, true);
        addContextParams(EFileParamName.LastColumn, true);
        addContextParams(EFileParamName.Header, rowsToSkipHeaderCheckboxCombo.isChecked());
        addContextParams(EFileParamName.Footer, rowsToSkipFooterCheckboxCombo.isChecked());
        addContextParams(EFileParamName.Limit, rowsToSkipLimitCheckboxCombo.isChecked());

        boolean isJava = LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA;
        addContextParams(EFileParamName.ThousandSeparator, isJava);
        addContextParams(EFileParamName.DecimalSeparator, isJava);
    }

    @Override
    protected void processWhenDispose() {
        if (processor != null) {
            processor.forceStop();
        }
    }
}
