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
package org.talend.repository.mdm.ui.wizard.concept;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.xml.XmlArray;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.XmlFactory;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.XPathPopulationUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.mdm.model.MDMXSDExtractorFieldModel;
import org.talend.repository.mdm.model.MDMXSDExtractorLoopModel;
import org.talend.repository.mdm.ui.wizard.dnd.MDMLinker;
import org.talend.repository.mdm.ui.wizard.table.ExtractionFieldsWithMDMEditorView;
import org.talend.repository.mdm.ui.wizard.table.ExtractionLoopWithMDMEditorView;
import org.talend.repository.mdm.util.MDMUtil;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.TreePopulator;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class MdmReceiveForm extends AbstractMDMFileStepForm {

    private static Logger log = Logger.getLogger(MDMXSDFileForm.class);

    /**
     * Main Fields.
     */
    private transient Tree availableXmlTree;

    private MDMXSDExtractorFieldModel fieldsModel;

    private ExtractionLoopWithMDMEditorView loopTableEditorView;

    private ExtractionFieldsWithMDMEditorView fieldsTableEditorView;

    private Text fileXmlText;

    protected boolean filePathIsDone;

    private UtilsButton cancelButton;

    private SashForm xmlToSchemaSash;

    private MDMLinker linker;

    private TreePopulator treePopulator;

    private MDMXSDExtractorLoopModel loopModel;

    private Concept concept;

    private static Boolean firstTimeWizardOpened = null;

    private Group schemaTargetGroup;

    private boolean isTemplateExist;

    private WizardPage wizardPage;

    private String oldSelectedEntity;

    private CCombo prefixCombo;

    private boolean creation;

    private boolean populated;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public MdmReceiveForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, Concept concept,
            WizardPage wizardPage, boolean creation) {
        super(parent, connectionItem);
        this.metadataTable = metadataTable;
        this.wizardPage = wizardPage;
        this.concept = concept;
        this.creation = creation;
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {
        File file = MDMUtil.getTempTemplateXSDFile();
        xsdFilePath = file.getParentFile().getAbsolutePath();
        if (!file.exists()) {
            try {
                super.initialize();
            } catch (Exception e) {
                isTemplateExist = false;
            }
        } else {
            xsdFilePath = file.getAbsolutePath();
        }
        isTemplateExist = true;
        this.treePopulator = new TreePopulator(availableXmlTree);

        checkFieldsValue();

        if (concept == null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            concept = ConnectionFactory.eINSTANCE.createConcept();
            getConnection().getSchemas().add(concept);
        }

        loopModel.setConcept(concept);
        if (concept.getLoopLimit() == null) {
            concept.setLoopLimit(-1);
            XmlArray.setLimitToDefault();
            concept.setLoopLimit(XmlArray.getRowLimit());

        }
        fieldsModel.setConcept(concept.getConceptTargets());
        fieldsTableEditorView.getTableViewerCreator().layout();

        if (concept.getXPathPrefix() == null || "".equals(concept.getXPathPrefix())) { //$NON-NLS-1$
            prefixCombo.select(1);
            concept.setXPathPrefix(XPathPrefix.NONE_ITEM.getPrefix());
        } else {
            prefixCombo.setText(getXPathPrefix(concept.getXPathPrefix()));
        }

        if (isContextMode()) {
            adaptFormToEditable();
        }

    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        if (isContextMode()) {
            prefixCombo.setBackground(null);
        } else {
            prefixCombo.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        }
        prefixCombo.setEditable(isContextMode());
        loopTableEditorView.setReadOnly(isContextMode());
        this.fieldsTableEditorView.setReadOnly(isContextMode());
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

        addGroupXmlViewer(sash2, 300, 110);

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
     * add Field to Group Xml File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupXmlFileSettings(final Composite mainComposite, final int width, final int height) {

        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("MdmReceiveForm.sourceSchema"), height); //$NON-NLS-1$
        group.setBackground(null);

        availableXmlTree = new Tree(group, SWT.MULTI | SWT.BORDER);

        // availableXmlTree.setVisible(false);
        GridData gridData2 = new GridData(GridData.FILL_BOTH);
        availableXmlTree.setLayoutData(gridData2);
    }

    private void addGroupSchemaTarget(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        schemaTargetGroup = Form.createGroup(mainComposite, 1, Messages.getString("MdmReceiveForm.groupSchemaTarget"), height); //$NON-NLS-1$

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

        // schemaTargetGroup.setBackgroundMode(SWT.INHERIT_FORCE);

        XPathPrefix[] values = XPathPrefix.values();
        String[] xPathPrefixData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            xPathPrefixData[j] = values[j].getDisplayName();
        }

        Composite composite = new Composite(schemaTargetGroup, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label = new Label(composite, SWT.NONE);
        label.setText("XPath Prefix");
        prefixCombo = new CCombo(composite, SWT.BORDER);
        prefixCombo.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        prefixCombo.setItems(xPathPrefixData);
        prefixCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        CommandStackForComposite commandStack = new CommandStackForComposite(schemaTargetGroup);

        loopModel = new MDMXSDExtractorLoopModel("Xpath loop expression"); //$NON-NLS-1$

        loopTableEditorView = new ExtractionLoopWithMDMEditorView(loopModel, schemaTargetGroup);
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
        fieldsModel = new MDMXSDExtractorFieldModel("Fields to extract"); //$NON-NLS-1$
        fieldsTableEditorView = new ExtractionFieldsWithMDMEditorView(fieldsModel, schemaTargetGroup);
        fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        final Composite fieldTableEditorComposite = fieldsTableEditorView.getMainComposite();
        fieldTableEditorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
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
    private void addGroupXmlViewer(final Composite parent, final int width, int height) {
        // Group File Viewer
        Group group = Form.createGroup(parent, 1, Messages.getString("MdmReceiveForm.groupFileViewer"), height); //$NON-NLS-1$
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, width, HEIGHT_BUTTON_PIXEL);

        fileXmlText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = width;
        gridData.minimumHeight = HEIGHT_BUTTON_PIXEL;
        fileXmlText.setLayoutData(gridData);
        fileXmlText.setToolTipText(Messages.getString("MdmReceiveForm.fileViewerTip", TreePopulator.getMaximumRowsToPreview())); //$NON-NLS-1$
        fileXmlText.setEditable(false);
        fileXmlText.setText(Messages.getString("MdmReceiveForm.fileViewerAlert")); //$NON-NLS-1$
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        prefixCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (concept != null) {
                    concept.setXPathPrefix(getXPathPrefixByDisplayName(prefixCombo.getText()));
                    checkFieldsValue();
                }
            }
        });

        // add listener to tableMetadata (listen the event of the toolbars)
        fieldsTableEditorView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });

        fieldsTableEditorView.getExtendedTableModel().addModifiedBeanListener(new IModifiedBeanListener<ConceptTarget>() {

            @Override
            public void handleEvent(ModifiedBeanEvent<ConceptTarget> event) {

                updateStatus(IStatus.OK, null);
                String msg = fieldsTableEditorView.checkColumnNames();
                if (!StringUtils.isEmpty(msg)) {
                    updateStatus(IStatus.ERROR, msg);
                }
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
        updateStatus(IStatus.OK, null);

        String msg = fieldsTableEditorView.checkColumnNames();
        if (!StringUtils.isEmpty(msg)) {
            updateStatus(IStatus.ERROR, msg);
            return false;
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
                            labelledCheckboxCombo.getLabelText() + Messages.getString("MdmReceiveForm.mustBePrecised")); //$NON-NLS-1$
                    return false;
                }
            }
        }

        // String pathStr = getConnection().getXmlFilePath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            // pathStr = TalendTextUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, pathStr));
        }
        if (concept == null || concept.getConceptTargets().isEmpty()) {
            updateStatus(IStatus.ERROR, null);
            return false;
        }

        if ("".equals(prefixCombo.getText())) {
            updateStatus(IStatus.ERROR, "Xpath prefix is required");
            return false;
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
     * checkFileFieldsValue active fileViewer if file exist.
     * 
     * @throws IOException
     */
    private void checkFilePathAndManageIt() {
        updateStatus(IStatus.OK, null);
        filePathIsDone = false;
        //        if (getConnection().getXmlFilePath() == "") { //$NON-NLS-1$
        // fileXmlText
        //                    .setText(Messages.getString("MdmReceiveForm.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        //                            + Messages.getString("MdmReceiveForm.fileViewerTip2")); //$NON-NLS-1$
        // } else {
        fileXmlText.setText(Messages.getString("MdmReceiveForm.fileViewerProgress")); //$NON-NLS-1$

        StringBuilder previewRows = new StringBuilder();
        BufferedReader in = null;

        // String pathStr = "";

        try {
            // pathStr = getConnection().getXmlFilePath();
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                        true);
                // pathStr = TalendTextUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                // pathStr));
            }

            File file = new File(xsdFilePath);
            Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);

            String str;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(xsdFilePath), guessedCharset.displayName()));

            while ((str = in.readLine()) != null) {
                previewRows.append(str + "\n"); //$NON-NLS-1$
            }

            // show lines
            fileXmlText.setText(new String(previewRows));
            filePathIsDone = true;

        } catch (Exception e) {
            String msgError = Messages.getString("MdmReceiveForm.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 

            //$NON-NLS-4$ //$NON-NLS-5$
            if (e instanceof FileNotFoundException) {
                msgError = msgError + Messages.getString("MdmReceiveForm.fileNotFoundException"); //$NON-NLS-1$
            } else if (e instanceof EOFException) {
                msgError = msgError + Messages.getString("MdmReceiveForm.eofException"); //$NON-NLS-1$
            } else if (e instanceof IOException) {
                msgError = msgError + Messages.getString("MdmReceiveForm.fileLocked"); //$NON-NLS-1$
            } else {
                msgError = msgError + Messages.getString("MdmReceiveForm.noExist"); //$NON-NLS-1$
            }
            fileXmlText.setText(msgError);
            if (!isReadOnly()) {
                updateStatus(IStatus.ERROR, msgError);
            }
            log.error(msgError + " " + e.getMessage()); //$NON-NLS-1$
        } finally {
            String msgError = Messages.getString("MdmReceiveForm.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 

            //$NON-NLS-4$ //$NON-NLS-5$
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                msgError = msgError + Messages.getString("MdmReceiveForm.fileLocked"); //$NON-NLS-1$
            }
        }
        checkFieldsValue();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {// super.isVisible()) {
            if (this.linker != null) {
                this.linker.removeAllLinks();
            }

            if (concept.getXPathPrefix() == null || "".equals(concept.getXPathPrefix())) { //$NON-NLS-1$
                prefixCombo.select(1);
                concept.setXPathPrefix(XPathPrefix.NONE_ITEM.getPrefix());
            } else {
                prefixCombo.setText(getXPathPrefix(concept.getXPathPrefix()));
            }
            populateTree();

        }
    }

    private void populateTree() {
        if (populated) {
            return;
        }
        populated = true;
        String selectedEntity = null;
        if (wizardPage != null && wizardPage.getPreviousPage() instanceof MdmConceptWizardPage2) {
            selectedEntity = ((MdmConceptWizardPage2) wizardPage.getPreviousPage()).getSelectedEntity();
        }

        ATreeNode selectedTreeNode = getSelectedTreeNode(xsdFilePath, selectedEntity);
        List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
        CreateConceptWizard wizard = ((CreateConceptWizard) getPage().getWizard());
        this.treePopulator.populateTree(wizard.getXSDSchema(), selectedTreeNode, treeNodes);

        ScrollBar verticalBar = availableXmlTree.getVerticalBar();
        if (verticalBar != null) {
            verticalBar.setSelection(0);
        }
        // fix bug: when the xml file is changed, the linker doesn't work.
        resetStatusIfNecessary(selectedEntity);

        if (this.linker == null) {
            this.linker = new MDMLinker(this.xmlToSchemaSash, isTemplateExist);
            this.linker.init(availableXmlTree, loopTableEditorView, fieldsTableEditorView, treePopulator);
            loopTableEditorView.setLinker(this.linker);
            fieldsTableEditorView.setLinker(this.linker);
        } else {
            this.linker.init(treePopulator);
            this.linker.createLinks();
        }
        checkFilePathAndManageIt();

        if (isContextMode()) {
            adaptFormToEditable();
        }

    }

    private void resetStatusIfNecessary(String selectedEntity) {
        String oraginalPath = ""; //$NON-NLS-1$
        if (xsdFilePath != null && selectedEntity != oldSelectedEntity) {
            CommandStackForComposite commandStack = new CommandStackForComposite(schemaTargetGroup);
            loopTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
            fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);

            getConnection().getSchemas().remove(concept);
            Concept temp = concept;

            // IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            // concept = ConnectionFactory.eINSTANCE.createConcept();
            // concept.setLabel(temp.getLabel());
            // concept.setConceptType(temp.getConceptType());
            // if (conceptName != null) {
            // concept.setLabel(conceptName);
            // concept.setId(factory.getNextId());
            // }
            getConnection().getSchemas().add(concept);

            loopModel.setConcept(concept);
            XmlArray.setLimitToDefault();
            concept.setLoopLimit(XmlArray.getRowLimit());

            fieldsModel.setConcept(concept.getConceptTargets());
            fieldsTableEditorView.getTableViewerCreator().layout();
            if (linker != null) {
                linker.init(treePopulator);
            }
            oldSelectedEntity = selectedEntity;
        }

    }

    @Override
    protected void createTable() {
        if (concept == null) {
            return;
        }
        populateTree();

        if (metadataTable == null) {
            metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
        }
        metadataTable.setLabel(concept.getLabel());

        Map<String, MetadataColumn> colsMap = new HashMap<String, MetadataColumn>();
        EList<MetadataColumn> columns = metadataTable.getColumns();
        for (MetadataColumn column : columns) {
            colsMap.put(column.getLabel(), column);
        }

        MappingTypeRetriever retriever = MetadataTalendType.getMappingTypeRetriever("xsd_id"); //$NON-NLS-1$
        List<ConceptTarget> targetList = concept.getConceptTargets();
        List<MetadataColumn> metadataColumns = new ArrayList<MetadataColumn>();
        // Commentted by Marvin Wang on May 21, 2012. If clearing the columns(features), it will cause the columns you
        // added can not be shown in table when editing column again.
        // metadataTable.getColumns().clear();
        for (ConceptTarget target : targetList) {
            String relativeXpath = target.getRelativeLoopExpression();
            String fullPath = target.getSchema().getLoopExpression();
            if (fullPath.contains("/") && metadataTable.getSourceName() == null) { //$NON-NLS-1$
                String source = fullPath.split("/")[1]; //$NON-NLS-1$
                metadataTable.setSourceName(source);
            }

            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                        true);
                fullPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, fullPath));
            }
            // adapt relative path
            if (relativeXpath != null) {
                if (!".".equals(relativeXpath)) {
                    String[] relatedSplitedPaths = relativeXpath.split("\\.\\./"); //$NON-NLS-1$
                    if (relatedSplitedPaths.length > 1) {
                        int pathsToRemove = relatedSplitedPaths.length - 1;
                        String[] fullPathSplited = fullPath.split("/"); //$NON-NLS-1$
                        fullPath = ""; //$NON-NLS-1$
                        for (int i = 1; i < (fullPathSplited.length - pathsToRemove); i++) {
                            fullPath += "/" + fullPathSplited[i]; //$NON-NLS-1$
                        }
                        fullPath += "/" + relatedSplitedPaths[pathsToRemove]; //$NON-NLS-1$
                    } else {
                        fullPath += "/" + relativeXpath; //$NON-NLS-1$
                    }
                }
            }

            TreeItem treeItem = treePopulator.getTreeItem(fullPath);
            if (treeItem != null) {
                ATreeNode curNode = (ATreeNode) treeItem.getData();
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                String targetName = target.getTargetName();
                MetadataColumn originalColumn = colsMap.get(targetName);
                if (originalColumn != null) {
                    try {
                        BeanUtils.copyProperties(metadataColumn, originalColumn);
                    } catch (Exception e) {
                        // do nothing.
                    }
                }
                metadataColumn.setLabel(targetName);
                // metadataColumn.setTalendType(target.getTargetName());

                if (curNode == null || retriever == null) {
                    metadataColumn.setTalendType(MetadataTalendType.getDefaultTalendType());
                } else {
                    String currentNodeType = curNode.getOriginalDataType();
                    if (currentNodeType != null && !currentNodeType.startsWith("xs:")) {
                        currentNodeType = "xs:" + currentNodeType;
                    }
                    metadataColumn.setTalendType(retriever.getDefaultSelectedTalendType(currentNodeType));
                }
                // Changed by Marvin Wang on May 21, 2012. Refer to the line above which is commentted
                // "metadataTable.getColumns().clear();".
                int index = removeOriginalColumn(targetName);
                if (index < 0) {
                    metadataTable.getColumns().add(metadataColumn);
                } else {
                    metadataTable.getColumns().add(index, metadataColumn);
                }
                // if (!metadataTable.getColumns().contains(metadataColumn)) {
                // metadataTable.getColumns().add(metadataColumn);
                // }
                metadataColumns.add(metadataColumn);
            }
        }
        if (!ConnectionHelper.getTables(getConnection()).contains(metadataTable)) {
            TdXmlSchema d = (TdXmlSchema) ConnectionHelper.getPackage(
                    ((MDMConnection) connectionItem.getConnection()).getDatacluster(), connectionItem.getConnection(),
                    TdXmlSchema.class);
            if (d != null) {
                d.getOwnedElement().add(metadataTable);
            } else {
                TdXmlSchema newXmlDoc = XmlFactory.eINSTANCE.createTdXmlSchema();
                newXmlDoc.setName(((MDMConnection) connectionItem.getConnection()).getDatacluster());
                ConnectionHelper.addPackage(newXmlDoc, connectionItem.getConnection());
                PackageHelper.addMetadataTable(metadataTable, newXmlDoc);
            }
            // ConnectionHelper.getTables(getConnection()).add(metadataTable);
        }
        // if (!getConnection().getTables().contains(metadataTable)) {
        // getConnection().getTables().add(metadataTable);
        // }
    }

    private String getXPathPrefix(String prefix) {
        if (prefix == null) {
            return "\"\"";
        }
        final XPathPrefix[] values = XPathPrefix.values();
        for (XPathPrefix value : values) {
            if (value.getPrefix().equals(prefix)) {
                return value.getDisplayName();
            }
        }
        return prefix;
    }

    private String getXPathPrefixByDisplayName(String dispalyName) {
        final XPathPrefix[] values = XPathPrefix.values();
        for (XPathPrefix value : values) {
            if (value == XPathPrefix.USER_DEFINED_ITEM) {
                continue;
            }
            if (value.getDisplayName().equals(dispalyName)) {
                return value.getPrefix();
            }
        }
        return dispalyName;
    }

}
