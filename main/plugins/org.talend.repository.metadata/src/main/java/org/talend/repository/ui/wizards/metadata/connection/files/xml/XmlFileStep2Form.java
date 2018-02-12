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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.xml.XmlArray;
import org.talend.core.ui.metadata.editor.XmlExtractorFieldModel;
import org.talend.core.ui.metadata.editor.XmlExtractorLoopModel;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.XPathPopulationUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.preview.AsynchronousPreviewHandler;
import org.talend.metadata.managment.ui.preview.IPreviewHandlerListener;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.preview.ShadowProcessPreview;
import org.talend.metadata.managment.ui.preview.StoppablePreviewLoader;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;
import org.talend.metadata.managment.ui.wizard.IRefreshable;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlFileStepForm;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.ExtractionFieldsWithXPathEditorView;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.ExtractionLoopWithXPathEditorView;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.XmlToXPathLinker;

/**
 * @author ocarbone
 * 
 */
public class XmlFileStep2Form extends AbstractXmlFileStepForm implements IRefreshable {

    private static Logger log = Logger.getLogger(XmlFileStep2Form.class);

    /**
     * Main Fields.
     */

    private transient Tree availableXmlTree;

    private ATreeNode treeNode;

    private XmlExtractorFieldModel fieldsModel;

    private ExtractionLoopWithXPathEditorView loopTableEditorView;

    private ExtractionFieldsWithXPathEditorView fieldsTableEditorView;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview xmlFilePreview;

    private Text fileXmlText;

    protected boolean filePathIsDone;

    private UtilsButton cancelButton;

    private boolean readOnly;

    private SashForm xmlToSchemaSash;

    private XmlToXPathLinker linker;

    private TreePopulator treePopulator;

    private XmlExtractorLoopModel loopModel;

    private XmlXPathLoopDescriptor xmlXPathLoopDescriptor;

    private IPreviewHandlerListener previewHandlerListener;

    private static Boolean firstTimeWizardOpened = null;

    private String xmlFilePath;

    private String tempXmlXsdPath;

    private Group schemaTargetGroup;

    /**
     * Output tab.
     */
    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private CTabItem fileTabItem;

    private Composite outputComposite;

    private TreeViewer treeViewer;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public XmlFileStep2Form(Composite parent, ConnectionItem connectionItem) {
        super(parent, connectionItem);
        setupForm(true);

    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        if (treeViewer == null) {
            treeViewer = new TreeViewer(availableXmlTree);
            treeViewer.setContentProvider(new VirtualXmlTreeNodeContentProvider(treeViewer));
            treeViewer.setLabelProvider(new VirtualXmlTreeLabelProvider());
            treeViewer.setUseHashlookup(true);
            this.treePopulator = new TreePopulator(treeViewer);
        }
        checkFieldsValue();

        if (xmlXPathLoopDescriptor == null) {
            if (getConnection().getSchema() != null && !getConnection().getSchema().isEmpty()) {
                xmlXPathLoopDescriptor = getConnection().getSchema().get(0);
                xmlFilePath = getConnection().getXmlFilePath();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), connectionItem.getConnection().getContextName());
                    xmlFilePath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper
                            .getOriginalValue(contextType, xmlFilePath));
                }
            } else {
                xmlXPathLoopDescriptor = ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor();
                getConnection().getSchema().add(xmlXPathLoopDescriptor);
            }
        }

        loopModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor);
        if (xmlXPathLoopDescriptor.getLimitBoucle() == null) {
            xmlXPathLoopDescriptor.setLimitBoucle(-1);
            XmlArray.setLimitToDefault();
            xmlXPathLoopDescriptor.setLimitBoucle(XmlArray.getRowLimit());

        }
        fieldsModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor.getSchemaTargets());
        fieldsTableEditorView.getTableViewerCreator().layout();

        if (isContextMode()) {
            adaptFormToEditable();
        }

    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        // readOnly = isReadOnly();
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        loopTableEditorView.setReadOnly(isContextMode());
        // this.fieldsTableEditorView.setReadOnly(isContextMode());
        this.fieldsTableEditorView.setReadOnly(false);
    }

    @Override
    protected void addFields() {

        // compositeFile Main Fields
        // Composite mainComposite = Form.startNewGridLayout(this, 1);
        SashForm mainComposite = new SashForm(this, SWT.VERTICAL | SWT.SMOOTH);
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        if (firstTimeWizardOpened == null) {
            firstTimeWizardOpened = Boolean.TRUE;
        } else if (firstTimeWizardOpened.equals(Boolean.TRUE)) {
            firstTimeWizardOpened = Boolean.FALSE;
        }

        // Splitter
        this.xmlToSchemaSash = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        xmlToSchemaSash.setLayoutData(new GridData(GridData.FILL_BOTH));
        xmlToSchemaSash.setBackgroundMode(SWT.INHERIT_FORCE);

        addGroupXmlFileSettings(xmlToSchemaSash, 400, 110);
        addGroupSchemaTarget(xmlToSchemaSash, 300, 110);
        xmlToSchemaSash.setWeights(new int[] { 40, 60 });

        SashForm sash2 = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        sash2.setLayoutData(new GridData(GridData.FILL_BOTH));

        addGroupFileViewer(sash2, 400, 210);
        // addGroupXmlViewer(sash2, 300, 110);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
        mainComposite.setWeights(new int[] { 60, 40 });
    }

    /**
     * add Field to Group Xml File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupXmlFileSettings(final Composite mainComposite, final int width, final int height) {

        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.sourceSchema"), height); //$NON-NLS-1$
        group.setBackground(null);

        availableXmlTree = new Tree(group, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL);

        // availableXmlTree.setVisible(false);
        GridData gridData2 = new GridData(GridData.FILL_BOTH);
        availableXmlTree.setLayoutData(gridData2);
    }

    private void addGroupSchemaTarget(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        schemaTargetGroup = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.groupSchemaTarget"), height); //$NON-NLS-1$

        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            schemaTargetGroup.addListener(SWT.Paint, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), schemaTargetGroup, new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

        schemaTargetGroup.setBackgroundMode(SWT.INHERIT_FORCE);

        CommandStackForComposite commandStack = new CommandStackForComposite(schemaTargetGroup);

        loopModel = new XmlExtractorLoopModel(Messages.getString("XmlFileStep2Form.Xpath_loop_expression"));

        loopTableEditorView = new ExtractionLoopWithXPathEditorView(loopModel, schemaTargetGroup);
        loopTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.heightHint = 90;

        final Composite loopTableEditorComposite = loopTableEditorView.getMainComposite();
        loopTableEditorComposite.setLayoutData(data2);
        loopTableEditorComposite.setBackground(null);
        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            loopTableEditorComposite.addListener(SWT.Paint, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), loopTableEditorComposite, new Point(0,
                            0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

        // Messages.getString("FileStep3.metadataDescription")
        fieldsModel = new XmlExtractorFieldModel(Messages.getString("XmlFileStep2Form.Fields_to_extract"));
        fieldsTableEditorView = new ExtractionFieldsWithXPathEditorView(fieldsModel, schemaTargetGroup);
        fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        final Composite fieldTableEditorComposite = fieldsTableEditorView.getMainComposite();
        data2 = new GridData(GridData.FILL_BOTH);
        data2.heightHint = 180;
        fieldTableEditorComposite.setLayoutData(data2);
        fieldTableEditorComposite.setBackground(null);
        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            fieldTableEditorComposite.addListener(SWT.Paint, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), fieldTableEditorComposite, new Point(
                            0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

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
        // composite Xml File Preview
        // Group previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        // //$NON-NLS-1$
        // Composite compositeXmlFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width,
        // HEIGHT_BUTTON_PIXEL);
        // height = height - HEIGHT_BUTTON_PIXEL - 15;

        tabFolder = new CTabFolder(parent, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewTabItem = new CTabItem(tabFolder, SWT.BORDER);
        previewTabItem.setText(Messages.getString("XmlFileStep2Form.Preview")); //$NON-NLS-1$
        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(Messages.getString("XmlFileStep2Form.Output"));
        fileTabItem = new CTabItem(tabFolder, SWT.BORDER);
        fileTabItem.setText(Messages.getString("XmlFileStep2Form.fileViewer"));

        Composite previewComposite = Form.startNewGridLayout(tabFolder, 1);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);
        Composite compositeFileViewer = Form.startNewGridLayout(tabFolder, 1);

        // previewGroup.setLayout(new GridLayout());

        Composite preivewButtonPart = new Composite(previewComposite, SWT.NONE);
        preivewButtonPart.setLayout(new GridLayout(3, false));
        preivewButtonPart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Preview Button
        previewButton = new Button(preivewButtonPart, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        XmlArray.setLimitToDefault();
        previewInformationLabel = new Label(previewComposite, SWT.NONE);
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        // Xml File Preview
        xmlFilePreview = new ShadowProcessPreview(previewComposite, null, width, height - 10);
        xmlFilePreview.newTablePreview();

        // File View
        fileXmlText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = width;
        gridData.minimumHeight = HEIGHT_BUTTON_PIXEL;
        fileXmlText.setLayoutData(gridData);
        fileXmlText
                .setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + Messages.getString("FileStep1.fileViewerTip2")); //$NON-NLS-1$
        fileXmlText.setEditable(false);
        fileXmlText.setText(Messages.getString("FileStep1.fileViewerAlert")); //$NON-NLS-1$

        previewTabItem.setControl(previewComposite);
        outputTabItem.setControl(outputComposite);
        fileTabItem.setControl(compositeFileViewer);
        tabFolder.setSelection(previewTabItem);
        tabFolder.pack();
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupXmlViewer(final Composite parent, final int width, int height) {
        // Group File Viewer
        Group group = Form.createGroup(parent, 1, Messages.getString("FileStep1.groupFileViewer"), height); //$NON-NLS-1$
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, width, HEIGHT_BUTTON_PIXEL);

        fileXmlText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = width;
        gridData.minimumHeight = HEIGHT_BUTTON_PIXEL;
        fileXmlText.setLayoutData(gridData);
        fileXmlText
                .setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + Messages.getString("FileStep1.fileViewerTip2")); //$NON-NLS-1$
        fileXmlText.setEditable(false);
        fileXmlText.setText(Messages.getString("FileStep1.fileViewerAlert")); //$NON-NLS-1$
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
    private ProcessDescription getProcessDescription(boolean defaultContext) {
        XmlFileConnection connection2 = OtherConnectionContextUtils.getOriginalValueConnection(getConnection(),
                this.connectionItem, isContextMode(), defaultContext);
        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(connection2);
        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        xmlFilePreview.clearTablePreview();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        clearPreview();

        // if no file, the process don't be executed
        if (getConnection().getXmlFilePath() == null || getConnection().getXmlFilePath().equals("")) { //$NON-NLS-1$
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        // if incomplete settings, , the process don't be executed
        if (!checkFieldsValue()) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        // set row limit
        if (xmlXPathLoopDescriptor.getLimitBoucle() > 0 && xmlXPathLoopDescriptor.getLimitBoucle() < XmlArray.getRowLimit()) {
            XmlArray.setRowLimit(xmlXPathLoopDescriptor.getLimitBoucle());
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$

        AsynchronousPreviewHandler<CsvArray> previewHandler = null;
        try {
            previewHandler = ShadowProcessHelper.createPreviewHandler();
        } catch (CoreException e) {
            previewInError(e);
            return;
        }

        StoppablePreviewLoader previewLoader = new StoppablePreviewLoader<CsvArray>(previewHandler, previewInformationLabel) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.repository.ui.wizards.metadata.connection.files.xml.StoppablePreviewLoader#previewEnded(java
             * .lang.Object)
             */
            @Override
            protected void previewEnded(CsvArray result) {
                xmlFilePreview.refreshTablePreview(result, false, (getConnection().getSchema().get(0)).getSchemaTargets());
                if (xmlFilePreview.isCsvRowsEmpty()) {
                    clearPreview();
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            handleErrorOutput(outputComposite, tabFolder, outputTabItem);

                        }
                    });
                    log.error(Messages.getString("FileStep2.noresult")); //$NON-NLS-1$
                    previewButton.setEnabled(true);
                }
            }

            @Override
            public void previewInError(CoreException e) {
                XmlFileStep2Form.this.previewInError(e);
            }

        };

        previewLoader.load(getProcessDescription(false));

    }

    /**
     * DOC amaumont Comment method "previewInFileError".
     * 
     * @param e
     */
    protected void previewInError(CoreException e) {

        String errorMessage = null;
        if (e != null) {
            errorMessage = e.getMessage();
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
        new ErrorDialogWidthDetailArea(previewInformationLabel.getShell(), PID, Messages.getString("FileStep2.previewFailure"), //$NON-NLS-1$
                errorMessage);
        log.error(Messages.getString("FileStep2.previewFailure") + " " + errorMessage); //$NON-NLS-1$ //$NON-NLS-2$

    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // add listener to tableMetadata (listen the event of the toolbars)
        fieldsTableEditorView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });

        fieldsTableEditorView.getExtendedTableModel().addModifiedBeanListener(new IModifiedBeanListener<SchemaTarget>() {

            @Override
            public void handleEvent(ModifiedBeanEvent<SchemaTarget> event) {
                checkFieldsValue();
                // updateStatus(IStatus.OK, null);
                // String msg = fieldsTableEditorView.checkColumnNames();
                // if (!StringUtils.isEmpty(msg)) {
                // updateStatus(IStatus.ERROR, msg);
                // }
            }
        });
    }

    /**
     * get the standby XPath expression.
     * 
     * @return
     */
    protected List getSelectedXPath(TreeItem selected) {
        // TreeItem selected = this.selectedItem;
        String rootPath = ""; //$NON-NLS-1$
        if (selected.getData() instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) selected.getData();
            rootPath = "/" + selected.getText(); //$NON-NLS-1$
        }

        while (selected.getParentItem() != null) {
            selected = selected.getParentItem();
            if (selected.getData() instanceof ATreeNode) {
                ATreeNode node = (ATreeNode) selected.getData();
                if (node.getType() == ATreeNode.ELEMENT_TYPE) {
                    rootPath = "/" + selected.getText() + rootPath; //$NON-NLS-1$
                }
            }
        }
        return XPathPopulationUtil.populateRootPath(rootPath);

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

        String msg = fieldsTableEditorView.checkColumnNames();
        if (!StringUtils.isEmpty(msg)) {
            updateStatus(IStatus.ERROR, msg);
            return false;
        } else {
            previewButton.setEnabled(true);
        }

        // Labelled Checkbox Combo (Row to Skip and Limit)
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();
            // if the checkbox is checked, check Numeric value
            if (labelledCheckboxCombo.getCheckbox().getSelection()) {
                if (labelledCheckboxCombo.getText() == "") { //$NON-NLS-1$
                    updateStatus(IStatus.ERROR,
                            labelledCheckboxCombo.getLabelText() + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$
                    return false;
                }
            }
        }

        previewInformationLabel.setText(""); //$NON-NLS-1$

        String pathStr = getConnection().getXmlFilePath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                    connectionItem.getConnection().getContextName());
            pathStr = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
        }
        if (pathStr != null && XmlUtil.isXSDFile(pathStr)) {
            previewButton.setEnabled(false);
        } else {
            previewButton.setEnabled(true);
        }
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
                if (!previewButton.getText().equals(Messages.getString("FileStep2.wait"))) { //$NON-NLS-1$
                    previewButton.setText(Messages.getString("FileStep2.wait")); //$NON-NLS-1$
                    if (getConnection().getXmlFilePath() != null && getConnection().getXmlFilePath().length() != 0
                            && getConnection().getSchema() != null && !getConnection().getSchema().isEmpty()
                            && (getConnection().getSchema().get(0)).getAbsoluteXPathQuery() != null
                            && (getConnection().getSchema().get(0)).getAbsoluteXPathQuery().length() != 0
                            && (getConnection().getSchema().get(0)).getSchemaTargets() != null
                            && (getConnection().getSchema().get(0)).getSchemaTargets().size() != 0) {
                        refreshPreview();
                        outputTabItem.setControl(null);
                    } else {
                        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                        if (!previewButton.getEnabled()) {
                            // new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep2.noresult"),
                            // Messages //$NON-NLS-1$
                            // .getString("FileStep2.noresultDetailMessage")); //$NON-NLS-1$
                            clearPreview();
                            Display.getDefault().asyncExec(new Runnable() {

                                @Override
                                public void run() {
                                    handleErrorOutput(outputComposite, tabFolder, outputTabItem);

                                }
                            });
                            log.error(Messages.getString("FileStep2.noresult")); //$NON-NLS-1$
                            previewButton.setEnabled(true);
                        } else {
                            previewButton.setEnabled(false);
                        }
                    }
                } else {
                    previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
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
     * ftang Comment method "handleErrorOutput".
     */
    private void handleErrorOutput(Composite outputComposite, CTabFolder tabFolder, CTabItem outputTabItem) {
        outputComposite = Form.startNewGridLayout(tabFolder, 1);
        outputTabItem.setControl(outputComposite);
        Font font = new Font(Display.getDefault(), "courier", 8, SWT.NONE); //$NON-NLS-1$

        StyledText text = new StyledText(outputComposite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        text.setLayoutData(gridData);
        outputComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        String errorInfo = Messages.getString("FileStep2.noresult") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        errorInfo = errorInfo + Messages.getString("FileStep2.noresultDetailMessage") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

        text.setText(errorInfo);
        text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        text.setFont(font);

        tabFolder.setSelection(outputTabItem);
    }

    /**
     * checkFileFieldsValue active fileViewer if file exist.
     * 
     * @throws IOException
     */
    private void checkFilePathAndManageIt() {
        updateStatus(IStatus.OK, null);
        filePathIsDone = false;
        if (getConnection().getXmlFilePath() == "") { //$NON-NLS-1$
            fileXmlText
                    .setText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + Messages.getString("FileStep1.fileViewerTip2")); //$NON-NLS-1$
        } else {
            fileXmlText.setText(Messages.getString("FileStep1.fileViewerProgress")); //$NON-NLS-1$

            StringBuilder previewRows = new StringBuilder();
            BufferedReader in = null;

            String pathStr = ""; //$NON-NLS-1$

            try {
                if (tempXmlXsdPath != null && getConnection().getFileContent() != null
                        && getConnection().getFileContent().length > 0) {
                    pathStr = tempXmlXsdPath;
                } else {
                    pathStr = getConnection().getXmlFilePath();
                }
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), connectionItem.getConnection().getContextName());
                    pathStr = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
                }

                File file = new File(pathStr);
                if (!file.exists()) {
                    file.createNewFile();
                    FileOutputStream outStream;
                    try {
                        outStream = new FileOutputStream(file);
                        outStream.write(getConnection().getFileContent());
                        outStream.close();
                    } catch (FileNotFoundException e1) {
                        ExceptionHandler.process(e1);
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }
                }

                Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);

                String str;
                in = new BufferedReader(new InputStreamReader(new FileInputStream(pathStr), guessedCharset.displayName()));

                while ((str = in.readLine()) != null) {
                    previewRows.append(str + "\n"); //$NON-NLS-1$
                }

                // show lines
                fileXmlText.setText(new String(previewRows));
                filePathIsDone = true;

            } catch (Exception e) {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                if (e instanceof FileNotFoundException) {
                    msgError = msgError + Messages.getString("FileStep1.fileNotFoundException"); //$NON-NLS-1$
                } else if (e instanceof EOFException) {
                    msgError = msgError + Messages.getString("FileStep1.eofException"); //$NON-NLS-1$
                } else if (e instanceof IOException) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                } else {
                    msgError = msgError + Messages.getString("FileStep1.noExist"); //$NON-NLS-1$
                }
                fileXmlText.setText(msgError);
                if (!isReadOnly()) {
                    updateStatus(IStatus.ERROR, msgError);
                }
                log.error(msgError + " " + e.getMessage()); //$NON-NLS-1$
            } finally {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                }
            }
            checkFieldsValue();
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
            if (this.linker != null) {
                this.linker.removeAllLinks();
            }

            String pathStr = getConnection().getXmlFilePath();
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                        connectionItem.getConnection().getContextName());
                pathStr = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
            }
            if (!new File(pathStr).exists() && getConnection().getFileContent() != null
                    && getConnection().getFileContent().length > 0) {
                initFileContent();
                pathStr = tempXmlXsdPath;
            }

            if (XmlUtil.isXSDFile(pathStr)) {
                List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
                XSDSchema xsdSchema = updateXSDSchema(pathStr);
                ATreeNode treeRootNode = wizard.getTreeRootNode();
                if (treeRootNode != null) {
                    treeNode = treeRootNode;
                } else {
                    List<ATreeNode> rootNodes = wizard.getRootNodes();
                    if (rootNodes != null && rootNodes.size() > 0) {
                        treeRootNode = rootNodes.get(0);
                    }
                }
                treePopulator.populateTree(xsdSchema, treeRootNode, treeNodes);
                treePopulator.setFilePath(pathStr);
                if (treeNodes.size() > 0) {
                    treeNode = treeNodes.get(0);
                }
            } else {
                this.treePopulator.populateTree(pathStr, treeNode);
            }

            ScrollBar verticalBar = availableXmlTree.getVerticalBar();
            if (verticalBar != null) {
                verticalBar.setSelection(0);
            }
            // fix bug: when the xml file is changed, the linker doesn't work.
            resetStatusIfNecessary();

            if (this.linker == null) {
                this.linker = new XmlToXPathLinker(this.xmlToSchemaSash);
                this.linker.init(treeViewer.getTree(), loopTableEditorView, fieldsTableEditorView, treePopulator);
                loopTableEditorView.setLinker(this.linker);
                fieldsTableEditorView.setLinker(this.linker);
            } else {
                this.linker.init(treePopulator);
                this.linker.createLinks();
            }
            checkFilePathAndManageIt();

            if (pathStr != null && XmlUtil.isXSDFile(pathStr)) {
                previewButton.setEnabled(false);
                previewButton.setText(Messages.getString("XmlFileStep2Form.previewNotAvailable")); //$NON-NLS-1$
                previewButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                previewButton.getParent().layout();
            }
            if (isContextMode()) {
                adaptFormToEditable();
            }

        }
    }

    /**
     * see bug 0004206: bug on the loop limit case in XMLfile source metadata defining
     */
    private void resetStatusIfNecessary() {
        String curXmlPath = getConnection().getXmlFilePath();
        String oraginalPath = ""; //$NON-NLS-1$
        if (xmlFilePath != null && curXmlPath != null) {
            // change xml file
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                        connectionItem.getConnection().getContextName());
                oraginalPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, curXmlPath));
            }
            if ((!xmlFilePath.equals(curXmlPath) && !xmlFilePath.equals(oraginalPath)) || isXsdRootChange()) {
                // clear command stack
                CommandStackForComposite commandStack = new CommandStackForComposite(schemaTargetGroup);
                loopTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
                fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);

                getConnection().getSchema().remove(xmlXPathLoopDescriptor);
                xmlXPathLoopDescriptor = ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor();
                getConnection().getSchema().add(xmlXPathLoopDescriptor);

                loopModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor);
                XmlArray.setLimitToDefault();
                xmlXPathLoopDescriptor.setLimitBoucle(XmlArray.getRowLimit());

                fieldsModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor.getSchemaTargets());
                fieldsTableEditorView.getTableViewerCreator().layout();

                // reset linker
                if (linker != null) {
                    linker.init(treePopulator);
                }
                xmlFilePreview.removePreviewContent();
            }
        }
        if (isContextMode()) {
            xmlFilePath = oraginalPath;
        } else {
            xmlFilePath = curXmlPath;
        }
    }

    private boolean isXsdRootChange() {
        XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
        return wizard.isXsdRootChange();
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

    private void initFileContent() {
        byte[] bytes = getConnection().getFileContent();
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return;
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp";
        String fileName = "";

        String pathStr = getConnection().getXmlFilePath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            pathStr = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
        }
        if (pathStr != null && XmlUtil.isXMLFile(pathStr)) {
            fileName = StringUtil.TMP_XML_FILE;
        } else if (pathStr != null && XmlUtil.isXSDFile(pathStr)) {
            fileName = StringUtil.TMP_XSD_FILE;
        }
        File temfile = new File(temPath + File.separator + fileName);
        if (!temfile.exists()) {
            try {
                temfile.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }

        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(temfile);
            outStream.write(bytes);
            outStream.close();
        } catch (FileNotFoundException e1) {
            ExceptionHandler.process(e1);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        tempXmlXsdPath = temfile.getPath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
            tempXmlXsdPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, tempXmlXsdPath));
        }
        // this.treePopulator.populateTree(tempXmlXsdPath, treeNode);
    }

}
