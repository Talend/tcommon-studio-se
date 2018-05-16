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
package org.talend.repository.ui.wizards.metadata.connection.files.regexp;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
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
import org.talend.repository.metadata.ui.wizards.form.AbstractRegexpFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class RegexpFileStep2Form extends AbstractRegexpFileStepForm implements IRefreshable {

    private static Logger log = Logger.getLogger(RegexpFileStep2Form.class);

    private static final String EMPTY_VALUE = Messages.getString("FileStep2.empty"); //$NON-NLS-1$

    private static final String[] STRING_NUMBERS_DATA = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            "14", "15", "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

    private static final String REGEXP_DEFAULT = TalendQuoteUtils.addQuotes("^(.*)$"); //$NON-NLS-1$

    /**
     * Main Fields.
     */
    private LabelledCombo encodingCombo;

    private Text fieldSeparatorType;

    private Text fieldSeparatorText;

    private LabelledCombo rowSeparatorCombo;

    private LabelledCheckboxCombo rowsToSkipHeaderCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipFooterCheckboxCombo;

    private LabelledText rowSeparatorText;

    private Button emptyRowsToSkipCheckbox;

    private Label escapeCharFlag;

    private Label textEnclosureFlag;

    /**
     * Fields use to preview.
     */
    private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview regexpFilePreview;

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
    public RegexpFileStep2Form(Composite parent, ConnectionItem connectionItem, IMetadataContextModeManager contextModeManager) {
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

        // Fields to the Group Regex File Settings
        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
        }

        // fieldSeparatorType.setText(getConnection().getFieldSeparatorType().getName());
        if (getConnection().getFieldSeparatorValue() != null) {
            fieldSeparatorText.setText(getConnection().getFieldSeparatorValue()); // +"\n tototototot"+"\n
            // ouuuuuuuuuuu"
        } else {
            fieldSeparatorText.setText(REGEXP_DEFAULT);
        }

        rowSeparatorCombo.setText(getConnection().getRowSeparatorType().getLiteral());
        rowSeparatorText.setText(getConnection().getRowSeparatorValue());
        if (!isContextMode()) {
            fieldSeparatorText.setEditable(true);
            rowSeparatorText.setEditable(true);

            // adpat Separator Combo and Text
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

        // clearSelection of the selected combo
        encodingCombo.clearSelection();
        // fieldSeparatorType.clearSelection();
        rowSeparatorCombo.clearSelection();

        emptyRowsToSkipCheckbox.setSelection(getConnection().isRemoveEmptyRow());
        checkFieldsValue();
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        encodingCombo.setReadOnly(isReadOnly());
        rowSeparatorCombo.setReadOnly(isReadOnly());
        rowSeparatorText.setReadOnly(isReadOnly());
        // rowsToSkipHeaderCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipFooterCheckboxCombo.setReadOnly(isReadOnly());
        // rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
        emptyRowsToSkipCheckbox.setEnabled(!isReadOnly());
        firstRowIsCaptionCheckbox.setEnabled(!isReadOnly());
    }

    /**
     * add Field to Group Regex File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupRegexFileSettings(final Composite mainComposite, final int width, final int height) {
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
        // for the change of the layout of group, the layout data must be changed too.
        compositeFileDelimitor.setLayoutData(null);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                .getString("FileStep2.encodingTip"), encodingData, 3, true, SWT.NONE); //$NON-NLS-1$

        // Row Separator Combo & Text
        String[] rowSeparatorData = { RowSeparator.STANDART_EOL_LITERAL.getLiteral(),
                RowSeparator.CUSTOM_STRING_LITERAL.getLiteral() };
        rowSeparatorCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.rowSeparator"), Messages //$NON-NLS-1$
                .getString("FileStep2.rowSeparatorTip"), rowSeparatorData, 1, true, SWT.READ_ONLY); //$NON-NLS-1$
        rowSeparatorText = new LabelledText(compositeFileDelimitor, "", 1, true, SWT.RIGHT); //$NON-NLS-1$
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
        // for the change of the layout of group, the layout data must changed too
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

        group.pack();
    }

    /**
     * add Field to Group Regex.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupRegex(final Composite mainComposite, final int width, final int height) {

        // Composite Regex
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("RegexpStep2.groupRegexSettings"), height); //$NON-NLS-1$
        // change the layout of group from GridLayout to FillLayout
        group.setLayout(new FillLayout());
        GridData groupLayoutData = (GridData) group.getLayoutData();
        groupLayoutData.heightHint = -1;
        groupLayoutData.minimumHeight = -1;
        groupLayoutData.minimumWidth = -1;
        groupLayoutData.widthHint = -1;
        groupLayoutData.grabExcessVerticalSpace = false;
        Composite compositeRegex = Form.startNewDimensionnedGridLayout(group, 1, width, height);
        // for the change of the layout of group , the layout data must be changed too.
        compositeRegex.setLayoutData(null);

        fieldSeparatorText = new Text(compositeRegex, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL); // |
        // SWT.WRAP

        // Dimension of columb of Separator Text
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = 80;
        gridData.minimumHeight = 70;

        fieldSeparatorText.setLayoutData(gridData);

        // Field Separator Text & Text
        fieldSeparatorType = new Text(compositeRegex, SWT.NONE);
        fieldSeparatorType.setVisible(false);
        fieldSeparatorType.setText(FieldSeparator.CUSTOM_REG_EXP_LITERAL.getName());
        FieldSeparator seperator = FieldSeparator.getByName(fieldSeparatorType.getText());
        getConnection().setFieldSeparatorType(seperator);

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
        // for the change of the layout of group, the layout data must be changed too.
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
        previewTabItem.setText(Messages.getString("RegexpFileStep2Form.preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("RegexpFileStep2Form.output")); //$NON-NLS-1$

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);

        // composite Regex File Preview
        // previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$
        Composite compositeRegexFilePreviewButton = Form.startNewDimensionnedGridLayout(previewComposite, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Regex File Preview Info
        firstRowIsCaptionCheckbox = new Button(compositeRegexFilePreviewButton, SWT.CHECK);
        firstRowIsCaptionCheckbox.setText(Messages.getString("FileStep2.firstRowsIsCaption")); //$NON-NLS-1$
        firstRowIsCaptionCheckbox.setAlignment(SWT.LEFT);

        previewButton = new Button(compositeRegexFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeRegexFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeRegexFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeRegexFilePreview = Form.startNewDimensionnedGridLayout(previewComposite, 1, width, height);

        // Regex File Preview
        regexpFilePreview = new ShadowProcessPreview(compositeRegexFilePreview, null, width, height - 10);
        regexpFilePreview.newTablePreview();

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    @Override
    protected void addFields() {

        // compositeFileDelimitor Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);

        addGroupRegexFileSettings(mainComposite, 400, 110);
        addGroupRowsToSkip(mainComposite, 300, 110);
        addGroupRegex(mainComposite, 400, 85);
        addGroupLimit(mainComposite, 300, 85);
        addGroupFileViewer(this, 700, 160);
        GridLayout mainCompositeLayout = (GridLayout) mainComposite.getLayout();
        mainCompositeLayout.marginBottom = 5;

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
    private ProcessDescription getProcessDescription(RegexpFileConnection originalValueConnection) {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(originalValueConnection);

        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        int i = -1;
        if (originalValueConnection.isUseHeader()) {
            i = ConnectionContextHelper.convertValue(originalValueConnection.getHeaderValue());
        }
        if (i != -1 && firstRowIsCaptionCheckbox.getSelection()) {
            i--;
        }
        processDescription.setHeaderRow(i);

        // adapt the limit to the preview
        processDescription.setLimitRows(maximumRowsToPreview);
        i = -1;
        if (originalValueConnection.isUseLimit()) {
            i = ConnectionContextHelper.convertValue(originalValueConnection.getLimitValue());
        }
        if (i != -1) {
            if (firstRowIsCaptionCheckbox.getSelection()) {
                i++;
            }
            if (i < maximumRowsToPreview) {
                processDescription.setLimitRows(i);
            }
        }

        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        regexpFilePreview.clearTablePreview();
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: RegexpFileStep2Form.java 38013 2010-03-05 14:21:59Z mhirt $
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
            RegexpFileConnection originalValueConnection = null;
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
                    originalValueConnection = (RegexpFileConnection) FileConnectionContextUtils.cloneOriginalValueConnection(
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
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_REGEXP", false); //$NON-NLS-1$
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
                Display.getDefault().syncExec(new Runnable() {

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
                regexpFilePreview.refreshTablePreview(csvArray, firstRowIsCaptionCheckbox.getSelection());
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
        addFieldsListenersGroupRegexFileSettings();
        addFieldsListenersGroupsRowToSkipAndLimit();
        addFieldsListenersGroupsRegex();
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

    /**
     * add Controls to group Escape Char.
     */
    private void addFieldsListenersGroupsRegex() {
        // // Escape Char Combo
        // escapeCharCombo.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(final ModifyEvent e) {
        // getConnection().setEscapeChar(escapeCharCombo.getText());
        // checkFieldsValue();
        // }
        // });
        // textEnclosureCombo.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(final ModifyEvent e) {
        // getConnection().setTextEnclosure(textEnclosureCombo.getText());
        // checkFieldsValue();
        // }
        // });
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

            // Event Key (numeric value only)
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
                        if (!rowsToSkipHeaderCheckboxCombo.isInteger() || rowsToSkipHeaderCheckboxCombo.getText().equals("0")) { //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.deselectAll();
                            getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                            getConnection().setHeaderValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("RegexpFileStep2Form.onlyNumber")); //$NON-NLS-1$
                            rowsToSkipHeaderCheckboxCombo.getCombo().setFocus();
                            // if rowsHeaderToSkip isn't integer or is equals to 0, the firstRowIsCaptionCheckbox is
                            // unusable.
                            firstRowIsCaptionCheckbox.setSelection(false);
                            getConnection().setFirstLineCaption(false);
                        } else {
                            getConnection().setHeaderValue(rowsToSkipHeaderCheckboxCombo.getText());
                            getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                        }
                    } else {
                        getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                        getConnection().setHeaderValue("" + 0); //$NON-NLS-1$
                    }
                    checkFieldsValue();

                }
            }
        });

        rowsToSkipFooterCheckboxCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!rowsToSkipFooterCheckboxCombo.isEmpty()) {
                        if (!rowsToSkipFooterCheckboxCombo.isInteger() || rowsToSkipFooterCheckboxCombo.getText().equals("0")) { //$NON-NLS-1$
                            rowsToSkipFooterCheckboxCombo.deselectAll();
                            getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                            getConnection().setFooterValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("RegexpFileStep2Form.onlyNumber")); //$NON-NLS-1$
                            rowsToSkipFooterCheckboxCombo.getCombo().setFocus();
                        } else {
                            getConnection().setFooterValue(rowsToSkipFooterCheckboxCombo.getText());
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
                        if (!rowsToSkipLimitCheckboxCombo.isInteger() || rowsToSkipLimitCheckboxCombo.getText().equals("0")) { //$NON-NLS-1$
                            rowsToSkipLimitCheckboxCombo.deselectAll();
                            getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                            getConnection().setLimitValue("" + 0); //$NON-NLS-1$

                            updateStatus(IStatus.ERROR, Messages.getString("RegexpFileStep2Form.onlyNumber")); //$NON-NLS-1$
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
                if ((!rowsToSkipHeaderCheckboxCombo.isChecked()) || text.equals("0")) { //$NON-NLS-1$
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
     * add Controls of Group Regex File Settings.
     */
    private void addFieldsListenersGroupRegexFileSettings() {
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

        rowSeparatorCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    // Label Custom of rowSeparatorText
                    rowSeparatorManager();
                }
            }
        });

        // Separator Text (field and row)
        fieldSeparatorText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setFieldSeparatorValue(fieldSeparatorText.getText());
                    checkFieldsValue();
                }
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
                if (!isContextMode()) {
                    getConnection().setRowSeparatorValue(rowSeparatorText.getText());
                    checkFieldsValue();
                }
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
        if (!isContextMode()) {
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

        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);
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

            // Fields to the Group Regex File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(getConnection().getEncoding());
            } else {
                encodingCombo.select(0);
            }

            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if (!isContextMode()) {
                if (getConnection().getFilePath() != null && !("").equals(getConnection().getFilePath())) { //$NON-NLS-1$
                    if (getConnection().getFieldSeparatorValue() == null) {
                        fieldSeparatorText.setText(REGEXP_DEFAULT);
                    }
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
        addContextParams(EFileParamName.RegExpression, true);
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
