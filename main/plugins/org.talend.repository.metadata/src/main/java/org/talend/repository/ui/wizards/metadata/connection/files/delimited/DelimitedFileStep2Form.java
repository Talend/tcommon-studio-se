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
package org.talend.repository.ui.wizards.metadata.connection.files.delimited;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.eclipse.swt.events.SelectionListener;
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
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.properties.ConnectionItem;
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
import org.talend.repository.metadata.ui.wizards.form.AbstractDelimitedFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class DelimitedFileStep2Form extends AbstractDelimitedFileStepForm implements IRefreshable {

    private static Logger log = Logger.getLogger(DelimitedFileStep2Form.class);

    private static final String EMPTY_VALUE = Messages.getString("FileStep2.empty"); //$NON-NLS-1$

    private static final String[] TEXT_ENCLOSURE_DATA = { EMPTY_VALUE,
            TalendQuoteUtils.addQuotes("\""), TalendQuoteUtils.addQuotes("\'"), TalendQuoteUtils.addQuotes("\\\\") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String[] ESCAPE_CHAR_DATA = { EMPTY_VALUE,
            TalendQuoteUtils.addQuotes("\""), TalendQuoteUtils.addQuotes("\'"), TalendQuoteUtils.addQuotes("\\\\") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String[] STRING_NUMBERS_DATA = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            "14", "15", "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

    /**
     * Main Fields.
     */
    private LabelledCombo encodingCombo;

    private LabelledCombo fieldSeparatorCombo;

    private LabelledText fieldSeparatorText;

    private LabelledCombo rowSeparatorCombo;

    private LabelledCombo textEnclosureCombo;

    private LabelledCheckboxCombo rowsToSkipHeaderCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipFooterCheckboxCombo;

    private LabelledText rowSeparatorText;

    private LabelledCombo escapeCharCombo;

    private Button csvRadio;

    private Button delimitedRadio;

    private Button emptyRowsToSkipCheckbox;

    private Label escapeCharFlag;

    private Label textEnclosureFlag;

    private Button splitwayRecordForJavaFID;

    private Label splitwayRecordForJavaFIDFlag;

    /**
     * Fields use to preview.
     */

    private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview delimitedFilePreview;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    /**
     * Another.
     */

    private UtilsButton cancelButton;

    private boolean readOnly;

    /**
     * Output tab.
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
    public DelimitedFileStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem);
        setContextModeManager(contextModeManager);
        setupForm(true);
    }

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

        }

        fieldSeparatorCombo.setText(getConnection().getFieldSeparatorType().getName());
        fieldSeparatorText.setText(getConnection().getFieldSeparatorValue());
        fieldSeparatorText.setEditable(false);

        rowSeparatorCombo.setText(getConnection().getRowSeparatorType().getLiteral());
        rowSeparatorText.setText(getConnection().getRowSeparatorValue());
        rowSeparatorText.setEditable(false);
        if (!isContextMode()) {
            // adpat Separator Combo and Text
            fieldSeparatorManager();
            rowSeparatorManager();
        }
        // Fields to the Group Rows To Skip
        boolean flag = false;
        flag = initRowsToSkip(rowsToSkipHeaderCheckboxCombo, getConnection().getHeaderValue());
        getConnection().setUseHeader(flag);
        flag = initRowsToSkip(rowsToSkipFooterCheckboxCombo, getConnection().getFooterValue());
        getConnection().setUseFooter(flag);
        flag = initRowsToSkip(rowsToSkipLimitCheckboxCombo, getConnection().getLimitValue());
        getConnection().setUseLimit(flag);

        emptyRowsToSkipCheckbox.setSelection(getConnection().isRemoveEmptyRow());
        firstRowIsCaptionCheckbox.setSelection(getConnection().isFirstLineCaption());

        // Fields to the Group Escape Char Settings
        String textEnclosure = getConnection().getTextEnclosure();
        String textEnclosureValue = ShadowProcessHelper.getValueFromArray(textEnclosure, TEXT_ENCLOSURE_DATA);
        if (textEnclosureValue == null) {
            textEnclosureCombo.select(0);
        } else {
            textEnclosureCombo.setText(textEnclosureValue);
        }
        String escapeChar = getConnection().getEscapeChar();
        String escapeCharValue = ShadowProcessHelper.getValueFromArray(escapeChar, ESCAPE_CHAR_DATA);
        if (escapeCharValue == null) {
            escapeCharCombo.select(0);
        } else {
            escapeCharCombo.setText(escapeCharValue);
        }

        splitwayRecordForJavaFID.setSelection(getConnection().isSplitRecord());

        if (Escape.DELIMITED.equals(getConnection().getEscapeType())) {
            csvRadio.setSelection(false);
            delimitedRadio.setSelection(true);
            textEnclosureCombo.setEnabled(false);
            escapeCharCombo.setEnabled(false);
            splitwayRecordForJavaFID.setEnabled(true);
        }
        if (Escape.CSV.equals(getConnection().getEscapeType())) {
            csvRadio.setSelection(true);
            delimitedRadio.setSelection(false);
            splitwayRecordForJavaFID.setEnabled(false);
        }

        String s = getConnection().getEscapeChar();
        if (!(s == null) && !s.equals("") && !s.equals(EMPTY_VALUE)) { //$NON-NLS-1$
            escapeCharCombo.setText(s);
        }
        s = getConnection().getTextEnclosure();
        if (!(s == null) && !s.equals("") && !s.equals(EMPTY_VALUE)) { //$NON-NLS-1$
            textEnclosureCombo.setText(s);
        }

        // clearSelection of the selected combo
        encodingCombo.clearSelection();
        fieldSeparatorCombo.clearSelection();
        rowSeparatorCombo.clearSelection();
        escapeCharCombo.clearSelection();
        textEnclosureCombo.clearSelection();

        emptyRowsToSkipCheckbox.setSelection(getConnection().isRemoveEmptyRow());
        checkFieldsValue();
        if (isContextMode()) {
            adaptFormToEditable();
        }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        encodingCombo.setReadOnly(isReadOnly());
        fieldSeparatorCombo.setReadOnly(isReadOnly());
        fieldSeparatorText.setReadOnly(isReadOnly());
        rowSeparatorCombo.setReadOnly(isReadOnly());
        rowSeparatorText.setReadOnly(isReadOnly());
        delimitedRadio.setEnabled(!isReadOnly());
        // rowsToSkipHeaderCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipFooterCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
        emptyRowsToSkipCheckbox.setEnabled(!isReadOnly());
        firstRowIsCaptionCheckbox.setEnabled(!isReadOnly());
        if (isReadOnly()) {
            escapeCharCombo.setReadOnly(true);
            textEnclosureCombo.setReadOnly(true);
            splitwayRecordForJavaFID.setEnabled(!isReadOnly());
        } else {
            textEnclosureCombo.setEnabled(csvRadio.getSelection());
            escapeCharCombo.setEnabled(csvRadio.getSelection());
            splitwayRecordForJavaFID.setEnabled(!csvRadio.getSelection());
        }
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
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupDelimitedFileSettings"), SWT.DEFAULT); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.minimumWidth = SWT.DEFAULT;
        groupLayoutData.widthHint = SWT.DEFAULT;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeFileDelimitor = Form.startNewDimensionnedGridLayout(group, 4, width, height);
        // for the layout of group has been changed from GridLayout to FillLayout, layout data must be changed too.
        compositeFileDelimitor.setLayoutData(null);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                .getString("FileStep2.encodingTip"), encodingData, 3, true, SWT.NONE); //$NON-NLS-1$

        // Goto Next Line
        // new Label(compositeFileDelimitor, SWT.NONE);
        // new Label(compositeFileDelimitor, SWT.NONE);

        // Field Separator Combo & Text
        String[] fieldSeparatorData = getFieldSeparatorStyleSupportByLanguage();

        fieldSeparatorCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.fieldSeparator"), Messages //$NON-NLS-1$
                .getString("FileStep2.fieldSeparatorDelimitedTip"), fieldSeparatorData, 1, true, SWT.READ_ONLY); //$NON-NLS-1$

        fieldSeparatorText = new LabelledText(compositeFileDelimitor, "", 1, true, SWT.RIGHT); //$NON-NLS-1$

        // Dimension of columb of Separator Text
        GridData gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gridData.minimumWidth = 80;
        fieldSeparatorText.setLayoutData(gridData);

        // Row Separator Combo & Text
        String[] rowSeparatorData = { RowSeparator.STANDART_EOL_LITERAL.getLiteral(),
                RowSeparator.CUSTOM_STRING_LITERAL.getLiteral() };
        rowSeparatorCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.rowSeparator"), Messages //$NON-NLS-1$
                .getString("FileStep2.rowSeparatorTip"), rowSeparatorData, 1, true, SWT.READ_ONLY); //$NON-NLS-1$
        rowSeparatorText = new LabelledText(compositeFileDelimitor, "", 1, true, SWT.RIGHT); //$NON-NLS-1$
        group.pack();
    }

    /**
     * CUSTOM_REGEX is not supported in java mode.
     * <p>
     * DOC YeXiaowei Comment method "getFieldSeparatorStyleSupportByLanguage".
     * 
     * @return
     */
    private String[] getFieldSeparatorStyleSupportByLanguage() {
        ECodeLanguage language = LanguageManager.getCurrentLanguage();
        String[] styles = { FieldSeparator.SEMICOLON_LITERAL.getName(), FieldSeparator.COMMA_LITERAL.getName(),
                FieldSeparator.TABULATION_LITERAL.getName(), FieldSeparator.SPACE_LITERAL.getName(),
                FieldSeparator.ALT_65_LITERAL.getName(), FieldSeparator.CUSTOM_ANSI_LITERAL.getName(),
                FieldSeparator.CUSTOM_UTF8_LITERAL.getName(), FieldSeparator.CUSTOM_REG_EXP_LITERAL.getName() };
        switch (language) {
        case JAVA:
            String[] javaStyles = new String[styles.length - 1];
            System.arraycopy(styles, 0, javaStyles, 0, javaStyles.length);
            return javaStyles;
        default: // PERL
            return styles;
        }
    }

    private void addGroupRowsToSkip(final Composite mainComposite, final int width, final int height) {
        // compositerowsToSkip Main Fields
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FileStep2.groupRowsToSkip"), SWT.DEFAULT); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.minimumWidth = SWT.DEFAULT;
        groupLayoutData.widthHint = SWT.DEFAULT;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeRowsToSkip = Form.startNewDimensionnedGridLayout(group, 3, width - 100, height);
        // for the layout of group has been changed from GridLayout to FillLayout, layout data must be changed too
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

        // Empty row
        emptyRowsToSkipCheckbox = new Button(compositeRowsToSkip, SWT.CHECK);
        emptyRowsToSkipCheckbox.setText(Messages.getString("FileStep2.removeEmptyRow")); //$NON-NLS-1$
        emptyRowsToSkipCheckbox.setAlignment(SWT.LEFT);
        gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gridData.horizontalSpan = 3;
        emptyRowsToSkipCheckbox.setLayoutData(gridData);

    }

    /**
     * add Field to Group Escape Char.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupEscapeChar(final Composite mainComposite, final int width, final int height) {

        // Composite Escape Char
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupEscapeCharSettings"), SWT.DEFAULT); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.minimumWidth = SWT.DEFAULT;
        groupLayoutData.widthHint = SWT.DEFAULT;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeEscapeChar = Form.startNewDimensionnedGridLayout(group, 3, width, height);
        // for the layout of group has been changed from GridLayout to FillLayout, layout data must be changed too.
        compositeEscapeChar.setLayoutData(null);

        // CSV or Positionel Radio
        csvRadio = new Button(compositeEscapeChar, SWT.RADIO);
        csvRadio.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getConnection().setCsvOption(csvRadio.getSelection());
                if (csvRadio.getSelection()) {
                    splitwayRecordForJavaFID.setSelection(false);
                    getConnection().setSplitRecord(false);
                }
            }
        });
        csvRadio.setText(Messages.getString("FileStep2.csv")); //$NON-NLS-1$
        delimitedRadio = new Button(compositeEscapeChar, SWT.RADIO);
        delimitedRadio.setText(Messages.getString("FileStep2.delimited")); //$NON-NLS-1$
        GridData gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gridData.horizontalSpan = 2;
        delimitedRadio.setLayoutData(gridData);

        // escape Char Combo
        escapeCharCombo = new LabelledCombo(compositeEscapeChar, Messages.getString("FileStep2.escapeChar"), Messages //$NON-NLS-1$
                .getString("FileStep2.escapeCharTip"), ESCAPE_CHAR_DATA, 1, false, SWT.READ_ONLY); //$NON-NLS-1$
        escapeCharFlag = new Label(compositeEscapeChar, SWT.NONE);
        escapeCharFlag.setText("                            "); //$NON-NLS-1$

        // Text Enclosure Combo
        textEnclosureCombo = new LabelledCombo(compositeEscapeChar, Messages.getString("FileStep2.textEnclosure"), Messages //$NON-NLS-1$
                .getString("FileStep2.textEnclosureTip"), TEXT_ENCLOSURE_DATA, 1, false, SWT.READ_ONLY); //$NON-NLS-1$
        textEnclosureFlag = new Label(compositeEscapeChar, SWT.NONE);
        textEnclosureFlag.setText("                            "); //$NON-NLS-1$

        // split record
        splitwayRecordForJavaFID = new Button(compositeEscapeChar, SWT.CHECK);
        splitwayRecordForJavaFID.setText(Messages.getString("FileStep2.splitwayRecordForJavaFID")); //$NON-NLS-1$
        splitwayRecordForJavaFID.setToolTipText(Messages.getString("FileStep2.splitwayRecordForJavaFIDTip")); //$NON-NLS-1$
        splitwayRecordForJavaFID.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getConnection().setSplitRecord(splitwayRecordForJavaFID.getSelection());
            }
        });

        String languageName = LanguageManager.getCurrentLanguage().getName();
        if (languageName.equals("perl")) { //$NON-NLS-1$
            splitwayRecordForJavaFID.setVisible(false);
        }

        splitwayRecordForJavaFIDFlag = new Label(compositeEscapeChar, SWT.NONE);
        splitwayRecordForJavaFIDFlag.setText("                            "); //$NON-NLS-1$

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
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupLimitOfRows"), SWT.DEFAULT); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.minimumWidth = SWT.DEFAULT;
        groupLayoutData.widthHint = SWT.DEFAULT;
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
        previewTabItem.setText(Messages.getString("FileStep2Form.preview"));
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("FileStep2Form.output"));

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);
        // composite Delimited File Preview
        // previewGroup = Form.createGroup(tabFolder, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$
        Composite compositeDelimitedFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, SWT.DEFAULT,
                SWT.DEFAULT);
        GridData groupLayoutData = (GridData) compositeDelimitedFilePreviewButton.getLayoutData();
        groupLayoutData.grabExcessVerticalSpace = false;
        // height = height - HEIGHT_BUTTON_PIXEL - 15;

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

        Composite compositeDelimitedFilePreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, SWT.DEFAULT,
                SWT.DEFAULT);
        groupLayoutData = (GridData) compositeDelimitedFilePreview.getLayoutData();
        groupLayoutData.grabExcessVerticalSpace = true;

        // Delimited File Preview
        delimitedFilePreview = new ShadowProcessPreview(compositeDelimitedFilePreview, null, width, height - 10);
        delimitedFilePreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    @Override
    protected void addFields() {

        // compositeFileDelimitor Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);

        addGroupDelimitedFileSettings(mainComposite, 400, 105);
        addGroupRowsToSkip(mainComposite, 300, 105);
        addGroupEscapeChar(mainComposite, 400, 110);
        addGroupLimit(mainComposite, 300, 110);
        addGroupFileViewer(this, 700, 170);

        GridLayout mainCompositeLayout = (GridLayout) mainComposite.getLayout();
        mainCompositeLayout.marginBottom = 10;

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
    private ProcessDescription getProcessDescription(DelimitedFileConnection delimitedConn) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(delimitedConn);

        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        int i = -1;
        if (delimitedConn.isUseHeader()) {
            i = ConnectionContextHelper.convertValue(delimitedConn.getHeaderValue());
        }

        if (i != -1 && firstRowIsCaptionCheckbox.getSelection()) {
            i--;
        }
        processDescription.setHeaderRow(i);

        // adapt the limit to the preview
        processDescription.setLimitRows(maximumRowsToPreview);
        i = -1;
        if (delimitedConn.isUseLimit()) {
            i = ConnectionContextHelper.convertValue(delimitedConn.getLimitValue());
        }
        if (i != -1) {
            if (firstRowIsCaptionCheckbox.getSelection()) {
                i++;
            }
            if (i < maximumRowsToPreview) {
                processDescription.setLimitRows(i);
            }
        }

        processDescription.setSplitRecord(getConnection().isSplitRecord());

        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        delimitedFilePreview.clearTablePreview();
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
        addFieldsListenersGroupsEscapeChar();
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
                            getConnection().setHeaderValue(newValue);
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
                            getConnection().setHeaderValue(newValue);
                        }
                    }
                    checkFieldsValue();
                }
            }
        });
    }

    /**
     * add Controls to group Escape Char.
     */
    private void addFieldsListenersGroupsEscapeChar() {
        // Radio csvRadio/delimitedRadio : Event modify
        ArrayList<Button> radio2Control = new ArrayList<Button>();
        radio2Control.add(csvRadio);
        radio2Control.add(delimitedRadio);

        Iterator<Button> iButton;
        Button button;

        for (iButton = radio2Control.iterator(); iButton.hasNext();) {
            button = iButton.next();
            // RADIO ONLY
            button.addSelectionListener(new SelectionListener() {

                String escapeCharComboOldValue = ""; //$NON-NLS-1$

                String textEnclosureComboOldValue = ""; //$NON-NLS-1$

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    Boolean b = !(csvRadio.getSelection());
                    getConnection().setEscapeType(b ? Escape.DELIMITED : Escape.CSV);
                    textEnclosureCombo.setEnabled(!b);
                    escapeCharCombo.setEnabled(!b);
                    splitwayRecordForJavaFID.setEnabled(b);
                    if (b) {
                        escapeCharComboOldValue = escapeCharCombo.getText();
                        textEnclosureComboOldValue = textEnclosureCombo.getText();
                        textEnclosureCombo.select(0);
                        escapeCharCombo.select(0);
                        textEnclosureFlag.setText("                 "); //$NON-NLS-1$
                        escapeCharFlag.setText("                 "); //$NON-NLS-1$
                        checkFieldsValue();
                    } else {
                        // select the old value to the two fields
                        if ((!"".equals(escapeCharComboOldValue)) && (!escapeCharComboOldValue.equals(EMPTY_VALUE))) { //$NON-NLS-1$
                            escapeCharCombo.setText(escapeCharComboOldValue);
                            setCharFlag(escapeCharFlag, escapeCharCombo.getText());
                        }
                        if ((!"".equals(textEnclosureComboOldValue)) && (!textEnclosureComboOldValue.equals(EMPTY_VALUE))) { //$NON-NLS-1$
                            textEnclosureCombo.setText(textEnclosureComboOldValue);
                            setCharFlag(textEnclosureFlag, textEnclosureCombo.getText());
                        }
                    }
                }
            });
        }

        // Radio and Checkbox: Event modify
        emptyRowsToSkipCheckbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getConnection().setRemoveEmptyRow(emptyRowsToSkipCheckbox.getSelection());
                checkFieldsValue();
            }
        });

        // Escape Char Combo
        escapeCharCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (escapeCharCombo.getText() != null && !("").equals(escapeCharCombo.getText()) //$NON-NLS-1$
                        && !(EMPTY_VALUE).equals(escapeCharCombo.getText())) {
                    getConnection().setEscapeChar(escapeCharCombo.getText());
                } else {
                    getConnection().setEscapeChar(null);
                }
                checkFieldsValue();
            }
        });
        textEnclosureCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (textEnclosureCombo.getText() != null && !("").equals(textEnclosureCombo.getText()) //$NON-NLS-1$
                        && !(EMPTY_VALUE).equals(textEnclosureCombo.getText())) {
                    getConnection().setTextEnclosure(textEnclosureCombo.getText());
                } else {
                    getConnection().setTextEnclosure(null);
                }
                checkFieldsValue();
            }
        });
    }

    /**
     * DOC ocarbone Comment method "setCharFlag".
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

                            updateStatus(IStatus.ERROR, Messages.getString("DelimitedFileStep2Form.onlyNumber")); //$NON-NLS-1$
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

                            updateStatus(IStatus.ERROR, Messages.getString("DelimitedFileStep2Form.onlyNumber")); //$NON-NLS-1$
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

                            updateStatus(IStatus.ERROR, Messages.getString("DelimitedFileStep2Form.14")); //$NON-NLS-1$
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

        // empty Rows To Skip
        emptyRowsToSkipCheckbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getConnection().setRemoveEmptyRow(emptyRowsToSkipCheckbox.getSelection());
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
                getConnection().setEncoding(encodingCombo.getText());
                checkFieldsValue();
            }
        });

        // Separator Combo (field and row)
        fieldSeparatorCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                // Label Custom of fieldSeparatorText
                fieldSeparatorManager();
            }
        });
        rowSeparatorCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                // Label Custom of rowSeparatorText
                rowSeparatorManager();
            }
        });

        // Separator Text (field and row)
        fieldSeparatorText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                getConnection().setFieldSeparatorValue(fieldSeparatorText.getText());
                checkFieldsValue();
            }
        });
        fieldSeparatorText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // Boolean quoteIsEscape = false;
                // if ((e.character) == Character.valueOf('"')) {
                // Point selection = fieldSeparatorText.getSelection();
                // if (selection.x > 0) {
                // if (fieldSeparatorText.getText().substring(selection.x - 1, selection.x).equals("\\")) {
                // //$NON-NLS-1$
                // quoteIsEscape = true;
                // }
                // }
                // if (!quoteIsEscape) {
                // updateStatus(IStatus.ERROR, Messages.getString("FileStep2.quoteDelimitedTip")); //$NON-NLS-1$
                // }
                // e.doit = quoteIsEscape;
                // }
            }
        });

        rowSeparatorText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                getConnection().setRowSeparatorValue(rowSeparatorText.getText());
                checkFieldsValue();
            }
        });
        rowSeparatorText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // Boolean quoteIsEscape = false;
                // if ((e.character) == Character.valueOf('"')) {
                // Point selection = rowSeparatorText.getSelection();
                // if (selection.x > 0) {
                // if (rowSeparatorText.getText().substring(selection.x - 1, selection.x).equals("\\")) { //$NON-NLS-1$
                // quoteIsEscape = true;
                // }
                // }
                // if (!quoteIsEscape) {
                // updateStatus(IStatus.ERROR, Messages.getString("FileStep2.quoteDelimitedTip")); //$NON-NLS-1$
                // }
                // e.doit = quoteIsEscape;
                // }
            }
        });

    }

    /**
     * rowSeparator : Adapt Custom Label and set the field Text.
     */
    protected void rowSeparatorManager() {
        if (isContextMode()) {
            return;
        }
        RowSeparator separator = RowSeparator.getByName(rowSeparatorCombo.getText());
        getConnection().setRowSeparatorType(separator);

        if (rowSeparatorCombo.getSelectionIndex() == 1) {
            // Adapt Custom Label
            rowSeparatorText.setLabelText(rowSeparatorCombo.getText());
            rowSeparatorText.setEditable(true);
            rowSeparatorText.forceFocus();
            rowSeparatorText.setReadOnly(false);
        } else {
            // set the Flag width the char value of the Combo
            // { "Standard EOL", "Custom String" };
            if (rowSeparatorCombo.getSelectionIndex() == 0) {
                if (getConnection().getFormat().toString().equals(FileFormat.MAC_LITERAL.getName())) {
                    rowSeparatorText.setText(TalendQuoteUtils.QUOTATION_MARK + "\\r" + TalendQuoteUtils.QUOTATION_MARK); //$NON-NLS-1$
                } else {
                    rowSeparatorText.setText(TalendQuoteUtils.QUOTATION_MARK + "\\n" + TalendQuoteUtils.QUOTATION_MARK); //$NON-NLS-1$
                }
            }
            // Init Custom Label
            rowSeparatorText.setLabelText(Messages.getString("FileStep2.correspondingCharacter")); //$NON-NLS-1$
            getConnection().setRowSeparatorValue(rowSeparatorText.getText());
            rowSeparatorText.setEditable(true);
            rowSeparatorText.setReadOnly(true);
            rowSeparatorText.getTextControl().getParent().layout();
        }
    }

    /**
     * fieldSeparator : Adapt Custom Label and set the field Text.
     */
    protected void fieldSeparatorManager() {
        if (isContextMode()) {
            return;
        }
        FieldSeparator seperator = FieldSeparator.getByName(fieldSeparatorCombo.getText());
        getConnection().setFieldSeparatorType(seperator);

        if (fieldSeparatorCombo.getSelectionIndex() >= 5) {
            // Adapt Custom Label
            fieldSeparatorText.setLabelText(fieldSeparatorCombo.getText());
            fieldSeparatorText.setEditable(true);
            fieldSeparatorText.setText(getConnection().getFieldSeparatorValue());
            fieldSeparatorText.forceFocus();
            fieldSeparatorText.setReadOnly(false);
        } else {
            // set the Flag width the char value of the Combo
            // { "Tabulation", "Semicolon", "Comma", "Space", "''(Alt 65, #A4)", "Custom ANSI", "Custom UTF8",
            switch (fieldSeparatorCombo.getSelectionIndex()) {
            case 0:
                fieldSeparatorText.setText(TalendQuoteUtils.addQuotes(";")); //$NON-NLS-1$
                break;
            case 1:
                fieldSeparatorText.setText(TalendQuoteUtils.addQuotes(",")); //$NON-NLS-1$
                break;
            case 2:
                fieldSeparatorText.setText(TalendQuoteUtils.addQuotes("\\t")); //$NON-NLS-1$
                break;
            case 3:
                fieldSeparatorText.setText(TalendQuoteUtils.addQuotes(" ")); //$NON-NLS-1$
                break;
            case 4:
                fieldSeparatorText.setText(TalendQuoteUtils.addQuotes("''")); //$NON-NLS-1$
                break;
            default:
                break;
            }

            // Init Custom Label
            getConnection().setFieldSeparatorValue(fieldSeparatorText.getText());
            fieldSeparatorText.setLabelText(Messages.getString("FileStep2.correspondingCharacter")); //$NON-NLS-1$
            fieldSeparatorText.setReadOnly(true);
            fieldSeparatorText.getTextControl().getParent().layout();
        }
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

        // Separator Combo (field and row)
        if ("".equals(fieldSeparatorText.getText())) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorAlert")); //$NON-NLS-1$
            return false;
        }
        if (fieldSeparatorText.getText().equals("\\") || fieldSeparatorText.getText().endsWith("\\")) { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorIncomplete")); //$NON-NLS-1$
            return false;
        }

        if ("".equals(rowSeparatorText.getText())) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.rowSeparatorAlert")); //$NON-NLS-1$
            return false;
        }
        if (rowSeparatorText.getText().equals("\\") || rowSeparatorText.getText().endsWith("\\")) { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.rowSeparatorIncomplete")); //$NON-NLS-1$
            return false;
        }

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

        // escape Char Combo
        if (escapeCharCombo.getText() == "") { // || escapeCharCombo.getText().equals("\\") || //$NON-NLS-1$
            // escapeCharCombo.getText().endsWith("\\")
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.escapeCharAlert")); //$NON-NLS-1$
            return false;
        }
        if (textEnclosureCombo.getText() == "") { // || textEnclosureCombo.getText().equals("\\") || //$NON-NLS-1$
            // textEnclosureCombo.getText().endsWith("\\")
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.textEnclosureAlert")); //$NON-NLS-1$
            return false;
        }

        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);
        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 45911 2010-07-23 11:30:58Z hywang $
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
            DelimitedFileConnection originalValueConnection = null;
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
                    originalValueConnection = (DelimitedFileConnection) FileConnectionContextUtils.cloneOriginalValueConnection(
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
                if (Escape.CSV.equals(getConnection().getEscapeType())) {

                    csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_CSV", true); //$NON-NLS-1$
                    if (csvArray == null) {
                        previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$
                        // refresh TablePreview on this step
                        previewInformationLabelMsg = ""; //$NON-NLS-1$
                    }
                } else {
                    csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_DELIMITED", true); //$NON-NLS-1$
                    if (csvArray == null) {
                        previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$

                        // refresh TablePreview on this step
                        previewInformationLabelMsg = ""; //$NON-NLS-1$
                    }
                }

            } catch (Exception e) {
                setException(e);
                previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
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
            previewInformationLabel.setText(previewInformationLabelMsg);
            if (getException() != null) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        handleErrorOutput(outputComposite, tabFolder, outputTabItem);
                    }
                });

                return;

            }

            if (csvArray != null) {
                delimitedFilePreview.refreshTablePreview(csvArray, firstRowIsCatption);
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
            // Adapt the UI rowSeparator to the file format
            rowSeparatorManager();

            // Fields to the Group Delimited File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(getConnection().getEncoding());
            } else {
                encodingCombo.select(0);
            }

            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if (!isContextMode()) {
                if ((!"".equals(getConnection().getFilePath())) && (getConnection().getFilePath() != null)) { //$NON-NLS-1$
                    refreshPreview();
                }
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
            if (isContextMode()) {
                adaptFormToEditable();
            }
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
        encodingCombo.setReadOnly(isContextMode());
        fieldSeparatorCombo.setReadOnly(isContextMode());
        rowSeparatorCombo.setReadOnly(isContextMode());

        fieldSeparatorText.setEditable(!isContextMode());
        rowSeparatorText.setEditable(!isContextMode());

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

    @Override
    protected void collectConnParams() {
        super.collectConnParams();
        addContextParams(EFileParamName.RowSeparator, true);
        addContextParams(EFileParamName.FieldSeparator, true);
        addContextParams(EFileParamName.Header, rowsToSkipHeaderCheckboxCombo.isChecked());
        addContextParams(EFileParamName.Footer, rowsToSkipFooterCheckboxCombo.isChecked());
        addContextParams(EFileParamName.Limit, rowsToSkipLimitCheckboxCombo.isChecked());
    }

    @Override
    protected void processWhenDispose() {
        if (processor != null) {
            processor.forceStop();
        }
    }

}
