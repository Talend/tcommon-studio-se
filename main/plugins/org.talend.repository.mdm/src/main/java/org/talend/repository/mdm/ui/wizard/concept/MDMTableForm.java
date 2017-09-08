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
package org.talend.repository.mdm.ui.wizard.concept;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.repository.mdm.i18n.Messages;
import org.talend.repository.mdm.util.MDMUtil;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.TreePopulator;

/**
 * 
 *
 */
public class MDMTableForm extends AbstractMDMFileStepForm {

    private static Logger log = Logger.getLogger(MDMTableForm.class);

    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private UtilsButton cancelButton;

    private UtilsButton guessButton;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    private Label informationLabel;

    private final MetadataTable metadataTable;

    private LabelledText metadataNameText;

    private LabelledText metadataCommentText;

    private boolean readOnly;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     */
    public MDMTableForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, String[] existingNames) {
        super(parent, connectionItem, metadataTable, existingNames);
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
        metadataEditor.setMetadataTable(metadataTable);
        tableEditorView.setMetadataEditor(metadataEditor);
        tableEditorView.getTableViewerCreator().layout();
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        guessButton.setEnabled(!isReadOnly());
        metadataNameText.setReadOnly(isReadOnly());
        metadataCommentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());
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

        guessButton = new UtilsButton(compositeGuessButton,
                Messages.getString("FileStep3.guess"), WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL); //$NON-NLS-1$
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

    private void changeConceptName(String newName) {
        Concept concept = MDMUtil.getConcept((MDMConnection) connectionItem.getConnection(), metadataTable);
        if ("".equals(newName)) {
            concept.setLabel(newName);
            // Caz if the label of concept is empty, concept.getLabel() will get the concept name.
            concept.setName(newName);
        } else
            concept.setLabel(newName);
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // metadataNameText : Event modifyText
        metadataNameText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                MetadataToolHelper.validateSchema(metadataNameText.getText());
                changeConceptName(metadataNameText.getText());
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

            public void modifyText(final ModifyEvent e) {
                metadataTable.setComment(metadataCommentText.getText());
            }
        });

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getMetadataEditor().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });
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
                            runShadowProcess();
                        }
                    } else {
                        guessButton.setEnabled(false);
                    }

                } else {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        runShadowProcess();
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
     * run a ShadowProcess to determined the Metadata.
     */
    protected void runShadowProcess() {
        MDMConnection connection2 = getConnection();
        refreshMetaDataTable(((Concept) connection2.getSchemas().get(0)).getConceptTargets());
        checkFieldsValue();
        return;
    }

    private void prepareColumnsFromXSD(List<MetadataColumn> columns, List<ConceptTarget> schemaTarget) {
        Composite composite = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
        composite.setVisible(false);
        TreePopulator treePopulator = new TreePopulator(new Tree(composite, SWT.None));
        ATreeNode node = null;
        MappingTypeRetriever retriever = MetadataTalendType.getMappingTypeRetriever("xsd_id"); //$NON-NLS-1$
        for (ConceptTarget schema : schemaTarget) {
            String relativeXpath = schema.getRelativeLoopExpression();
            String fullPath = schema.getSchema().getLoopExpression();
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                        true);
                fullPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, fullPath));
            }
            // adapt relative path
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
            TreeItem treeItem = treePopulator.getTreeItem(fullPath);
            if (treeItem != null) {
                ATreeNode curNode = (ATreeNode) treeItem.getData();
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                metadataColumn.setLabel(tableEditorView.getMetadataEditor().getNextGeneratedColumnName(schema.getTargetName()));

                if (curNode == null || retriever == null) {
                    metadataColumn.setTalendType(MetadataTalendType.getDefaultTalendType());
                } else {

                    metadataColumn.setTalendType(retriever.getDefaultSelectedTalendType("xs:" + curNode.getOriginalDataType())); //$NON-NLS-1$
                }
                columns.add(metadataColumn);
            }
        }
    }

    /**
     * DOC ocarbone Comment method "refreshMetaData".
     * 
     * @param csvArray
     */
    public void refreshMetaDataTable(List<ConceptTarget> schemaTarget) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

        // clear all items
        tableEditorView.getMetadataEditor().removeAll();

        List<MetadataColumn> columns = new ArrayList<MetadataColumn>();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
        }
        prepareColumnsFromXSD(columns, schemaTarget);

        tableEditorView.getMetadataEditor().addAll(columns);
        checkFieldsValue();
        tableEditorView.getTableViewerCreator().layout();
        tableEditorView.getTableViewerCreator().getTable().deselectAll();
        informationLabel.setText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$
        return;
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
            if (tableEditorView.getMetadataEditor().getBeanCount() <= 0) {
                runShadowProcess();
            }

            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }
}
