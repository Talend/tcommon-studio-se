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
package org.talend.core.ui.metadata.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.composite.ThreeCompositesSashForm;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataDialog.java 4400 2007-07-10 07:59:06 +0000 (星期二, 10 七月 2007) bqian $
 * 
 */
public class MetadataDialogForMerge extends Dialog {

    private static final String ELT_LABEL = "ELT"; //$NON-NLS-1$

    private static final String INPUTNODE_KEY = "inputNode"; //$NON-NLS-1$

    private static final String INPUTMETATABLE_KEY = "inputMetaTable"; //$NON-NLS-1$

    private static final String INPUTMETAVIEW_KEY = "inputMetaView"; //$NON-NLS-1$

    private static final String INPUTREADONLY_KEY = "inputReadOnly"; //$NON-NLS-1$

    @Override
    protected void setShellStyle(int newShellStyle) {
        newShellStyle = newShellStyle | SWT.RESIZE;
        super.setShellStyle(newShellStyle);
    }

    private MetadataTableEditorView outputMetaView;

    private MetadataTableEditorView inputMetaView;

    private Point size;

    private IMetadataTable outputMetaTable;

    private IMetadataTable inputMetaTable;

    private String text = ""; //$NON-NLS-1$

    private String titleOutput = ""; //$NON-NLS-1$

    private String titleInput = ""; //$NON-NLS-1$

    private boolean inputReadOnly = false;

    private boolean outputReadOnly = false;

    private Map<IMetadataColumn, String> changedNameColumns = new HashMap<IMetadataColumn, String>();

    private CommandStack commandStack;

    private INode inputNode;

    private INode outputNode;

    private String inputFamily;

    private String outputFamily;

    private ThreeCompositesSashForm compositesSachForm;

    private Map<INode, Map<IMetadataTable, Boolean>> inputInfos;

    private TableItem[] tableItem;

    private List<IMetadataColumn> list;

    private IMetadataColumn column;

    MetadataTableEditor metadataTableEditor;

    Button copyToInput;

    Button copySelectionToInput;

    public void init(Shell parent, IMetadataTable inputMetaTable, INode inputNode, IMetadataTable outputMetaTable,
            INode outputNode, CommandStack commandStack) {

        this.inputMetaTable = inputMetaTable;
        this.inputNode = inputNode;
        if (inputNode != null) {
            this.titleInput = inputMetaTable.getTableName();
            this.inputFamily = inputNode.getComponent().getOriginalFamilyName();
        }
        this.outputNode = outputNode;
        if (outputNode != null) {
            this.titleOutput = outputNode.getUniqueName();
            this.outputFamily = outputNode.getComponent().getOriginalFamilyName();
        }
        this.outputMetaTable = outputMetaTable;
        this.commandStack = commandStack;

        if (inputMetaTable == null) {
            size = new Point(550, 400);
        } else {
            size = new Point(1000, 400);
        }
    }

    public MetadataDialogForMerge(Shell parent, IMetadataTable inputMetaTable, INode inputNode, IMetadataTable outputMetaTable,
            INode outputNode, CommandStack commandStack) {
        super(parent);
        init(parent, inputMetaTable, inputNode, outputMetaTable, outputNode, commandStack);
    }

    public MetadataDialogForMerge(Shell parent, Map<INode, Map<IMetadataTable, Boolean>> inputInfos,
            IMetadataTable outputMetaTable, INode outputNode, CommandStack commandStack) {
        super(parent);
        this.inputInfos = inputInfos;
        INode inputNode = null;
        IMetadataTable inputMetaTable = null;
        // zli modify for bug 7221
        if (inputInfos != null && !inputInfos.isEmpty()) {
            int size2 = inputInfos.keySet().size();

            for (int i = 0; i < size2; i++) {
                INode object = (INode) inputInfos.keySet().toArray()[i];
                List<? extends IConnection> outgoingConnections = object.getOutgoingConnections();
                if (outgoingConnections.size() > 0) {
                    EConnectionType lineStyle = outgoingConnections.get(0).getLineStyle();
                    if (lineStyle.equals(EConnectionType.FLOW_MAIN)) {
                        inputNode = (INode) inputInfos.keySet().toArray()[i];
                        Map<IMetadataTable, Boolean> oneInput = inputInfos.get(inputNode);
                        inputMetaTable = (IMetadataTable) oneInput.keySet().toArray()[0];
                        inputReadOnly = oneInput.get(inputMetaTable);
                    }
                    if (lineStyle.equals(EConnectionType.FLOW_MERGE)) {
                        if (outgoingConnections.get(0).getInputId() == 1) {
                            inputNode = (INode) inputInfos.keySet().toArray()[i];
                            Map<IMetadataTable, Boolean> oneInput = inputInfos.get(inputNode);
                            inputMetaTable = (IMetadataTable) oneInput.keySet().toArray()[0];
                            inputReadOnly = oneInput.get(inputMetaTable);
                        }
                    }
                }
            }
        }

        init(parent, inputMetaTable, inputNode, outputMetaTable, outputNode, commandStack);
    }

    public MetadataDialogForMerge(Shell parent, IMetadataTable outputMetaTable, INode outputNode, CommandStack commandStack) {
        this(parent, null, null, outputMetaTable, outputNode, commandStack);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        this.commandStack = new CommandStackForComposite(newShell);
        newShell.setSize(size);
        newShell.setText(text);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    protected void createTabItem(INode inputNode, CTabFolder folderInput, boolean showTalendTypeColumnForInput) {

        Map<IMetadataTable, Boolean> oneInput = inputInfos.get(inputNode);
        inputMetaTable = (IMetadataTable) oneInput.keySet().toArray()[0];
        inputReadOnly = oneInput.get(inputMetaTable);
        titleInput = inputNode.getUniqueName();

        CTabItem item = new CTabItem(folderInput, SWT.NONE);
        item.setText(titleInput);

        Composite compositeleft = new Composite(folderInput, SWT.NONE);
        compositeleft.setLayoutData(new GridData(GridData.FILL_BOTH));
        compositeleft.setLayout(new GridLayout());

        metadataTableEditor = new MetadataTableEditor(inputMetaTable, titleInput + " (Input)"); //$NON-NLS-1$
        inputMetaView = new MetadataTableEditorView(compositeleft, SWT.NONE, metadataTableEditor, inputReadOnly, true, false,
                false);
        MetadataDialog.initializeMetadataTableView(inputMetaView, inputNode, inputMetaTable);
        inputMetaView.setShowTalendTypeColumn(showTalendTypeColumnForInput);
        inputMetaView.initGraphicComponents();
        inputMetaView.getExtendedTableViewer().setCommandStack(commandStack);

        inputMetaView.setGridDataSize(size.x / 2 - 50, size.y - 150);

        CustomTableManager.addCustomManagementToTable(inputMetaView, inputReadOnly);
        CustomTableManager.addCustomManagementToToolBar(inputMetaView, inputMetaTable, inputReadOnly, outputMetaView,
                outputMetaTable, outputNode.getComponent().isSchemaAutoPropagated());

        item.setControl(compositeleft);
        item.setData(INPUTNODE_KEY, inputNode);
        item.setData(INPUTMETATABLE_KEY, inputMetaTable);
        item.setData(INPUTMETAVIEW_KEY, inputMetaView);
        item.setData(INPUTREADONLY_KEY, inputReadOnly);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // MetadataTableEditor metadataTableEditor;
        boolean showTalendTypeColumnForInput = !(inputFamily != null && inputFamily.startsWith(ELT_LABEL));
        boolean showTalendTypeColumnForOutput = !outputFamily.startsWith(ELT_LABEL);

        if (inputMetaTable == null) {
            composite.setLayout(new FillLayout());
            metadataTableEditor = new MetadataTableEditor(outputMetaTable, titleOutput, true);
            outputMetaView = new MetadataTableEditorView(composite, SWT.NONE, metadataTableEditor, outputReadOnly, true, true,
                    false);
            outputMetaView.setIsRepository(isRepository(outputNode));
            MetadataDialog.initializeMetadataTableView(outputMetaView, outputNode, outputMetaTable);
            outputMetaView.setShowTalendTypeColumn(showTalendTypeColumnForOutput);
            outputMetaView.initGraphicComponents();
            outputMetaView.getExtendedTableViewer().setCommandStack(commandStack);
        } else {
            compositesSachForm = new ThreeCompositesSashForm(composite, SWT.NONE);

            GridLayout gridLayout = new GridLayout(1, false);
            gridLayout.marginBottom = 0;
            gridLayout.marginHeight = 0;
            gridLayout.marginLeft = 0;
            gridLayout.marginRight = 0;
            gridLayout.marginTop = 0;
            gridLayout.marginWidth = 0;
            gridLayout.horizontalSpacing = 0;
            composite.setLayout(gridLayout);
            GridData gridData = new GridData(GridData.FILL_BOTH);
            composite.setLayoutData(gridData);

            // use the CTablFolder for the tUnite component
            final CTabFolder folderInput = new CTabFolder(compositesSachForm.getLeftComposite(), SWT.BORDER);
            folderInput.setLayoutData(new GridData(GridData.FILL_BOTH));
            folderInput.setSimple(false);
            folderInput.setUnselectedImageVisible(false);
            folderInput.setUnselectedCloseVisible(false);

            Set<INode> inputNodeskey = inputInfos.keySet();

            // zli modify for bug 7221
            INode mainNode = null;
            for (INode inputNode : inputNodeskey) {
                List<? extends IConnection> outgoingConnections = inputNode.getOutgoingConnections();
                if (outgoingConnections.size() > 0) {
                    EConnectionType lineStyle = outgoingConnections.get(0).getLineStyle();
                    if (lineStyle.equals(EConnectionType.FLOW_MAIN)) {
                        mainNode = inputNode;
                        createTabItem(inputNode, folderInput, showTalendTypeColumnForInput);
                    }
                }
            }
            for (INode inputNode : inputNodeskey) {
                if (inputNode != mainNode) {
                    createTabItem(inputNode, folderInput, showTalendTypeColumnForInput);
                }

            }

            folderInput.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    CTabFolder source = (CTabFolder) e.getSource();
                    CTabItem tabItem = source.getSelection();
                    inputNode = (INode) tabItem.getData(INPUTNODE_KEY);
                    inputMetaTable = (IMetadataTable) tabItem.getData(INPUTMETATABLE_KEY);
                    inputMetaView = (MetadataTableEditorView) tabItem.getData(INPUTMETAVIEW_KEY);
                    inputFamily = inputNode.getComponent().getOriginalFamilyName();
                    inputReadOnly = (Boolean) tabItem.getData(INPUTREADONLY_KEY);

                    if (inputReadOnly || inputMetaTable.isReadOnly()) {
                        copyToInput.setEnabled(false);
                        copySelectionToInput.setEnabled(false);
                    } else {
                        copyToInput.setEnabled(true);
                        copySelectionToInput.setEnabled(true);
                    }
                }
            });

            Label label1 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
            GridDataFactory.swtDefaults().hint(42, 18).applyTo(label1);
            Composite buttonComposite = new Composite(compositesSachForm.getMidComposite(), SWT.BORDER);
            Label label2 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
            GridDataFactory.swtDefaults().hint(42, 0).applyTo(label2);

            gridLayout = new GridLayout(1, true);
            buttonComposite.setLayout(gridLayout);
            gridData = new GridData(GridData.FILL_BOTH);
            // gridData.verticalAlignment = GridData.CENTER;
            buttonComposite.setLayoutData(gridData);

            Composite buttonComposite2 = new Composite(buttonComposite, SWT.NONE);

            gridLayout = new GridLayout(1, true);
            gridLayout.marginBottom = 0;
            gridLayout.marginHeight = 0;
            gridLayout.marginLeft = 0;
            gridLayout.marginRight = 0;
            gridLayout.marginTop = 0;
            gridLayout.marginWidth = 0;
            buttonComposite2.setLayout(gridLayout);
            gridData = new GridData(GridData.FILL_BOTH);
            gridData.verticalAlignment = GridData.CENTER;
            buttonComposite2.setLayoutData(gridData);

            // qli comment
            // Input => Output(the selection)
            Button copySelectionToOutput = new Button(buttonComposite2, SWT.NONE);
            copySelectionToOutput.setImage(ImageProvider.getImage(EImage.RIGHT_ICON));
            copySelectionToOutput.setToolTipText(Messages.getString("MetadataDialog.CopySelectionToOutput")); //$NON-NLS-1$
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(copySelectionToOutput);

            copySelectionToOutput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    // qli comment
                    // Input => Output(the selection)
                    // if the selectionline above zero.just run the method "copyTable(list, getOutputMetaData())".
                    tableItem = inputMetaView.getTable().getSelection();
                    list = new ArrayList<IMetadataColumn>();
                    for (TableItem element : tableItem) {
                        column = (IMetadataColumn) element.getData();
                        list.add(column);
                    }
                    if (tableItem.length > 0) {
                        MetadataToolHelper.copyTable(list, getOutputMetaData());
                        outputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            // Input => Output
            Button copyToOutput = new Button(buttonComposite2, SWT.NONE);
            copyToOutput.setImage(ImageProvider.getImage(EImage.RIGHTX_ICON));
            copyToOutput.setToolTipText(Messages.getString("MetadataDialog.CopyToOutput")); //$NON-NLS-1$
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(copyToOutput);
            copyToOutput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    MessageBox messageBox = new MessageBox(parent.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL);
                    messageBox.setText(Messages.getString("MetadataDialog.SchemaModification")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("MetadataDialog.Message")); //$NON-NLS-1$
                    if (messageBox.open() == SWT.OK) {
                        MetadataToolHelper.copyTable(getInputMetaData(), getOutputMetaData(), true);
                        outputMetaView.getTableViewerCreator().getTableViewer().refresh();
                    }
                }
            });

            Label lable1 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable1);
            Label lable2 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable2);
            Label lable3 = new Label(buttonComposite2, SWT.NONE);
            GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(lable3);

            // qli comment
            // Output => Input(the selection)
            copySelectionToInput = new Button(buttonComposite2, SWT.NONE);
            copySelectionToInput.setImage(ImageProvider.getImage(EImage.LEFT_ICON));
            copySelectionToInput.setToolTipText(Messages.getString("MetadataDialog.CopySelectionToInput.toolTipText")); //$NON-NLS-1$
            gridData.verticalAlignment = GridData.CENTER;
            copySelectionToInput.setLayoutData(gridData);
            copySelectionToInput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    // qli comment
                    // Output => Input(selection)
                    // if the selectionline above zero.just run the method "copyTable(list, getInputMetaData())".
                    tableItem = outputMetaView.getTable().getSelection();
                    list = new ArrayList<IMetadataColumn>();
                    for (TableItem element : tableItem) {
                        column = (IMetadataColumn) element.getData();
                        list.add(column);
                    }
                    if (tableItem.length > 0) {
                        MetadataToolHelper.copyTable(list, getInputMetaData());
                        inputMetaView.getTableViewerCreator().refresh();
                    }
                }
            });

            // Output => Input
            copyToInput = new Button(buttonComposite2, SWT.NONE);
            copyToInput.setImage(ImageProvider.getImage(EImage.LEFTX_ICON));
            copyToInput.setToolTipText(Messages.getString("MetadataDialog.CopyToInput.toolTipText")); //$NON-NLS-1$
            gridData = new GridData();
            gridData.verticalAlignment = GridData.CENTER;
            copyToInput.setLayoutData(gridData);
            copyToInput.addListener(SWT.Selection, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    MessageBox messageBox = new MessageBox(parent.getShell(), SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL);
                    messageBox.setText(Messages.getString("MetadataDialog.SchemaModification")); //$NON-NLS-1$
                    messageBox.setMessage(Messages.getString("MetadataDialog.TransferMessage")); //$NON-NLS-1$
                    if (messageBox.open() == SWT.OK) {
                        MetadataToolHelper.copyTable(getOutputMetaData(), getInputMetaData());
                        inputMetaView.getTableViewerCreator().getTableViewer().refresh();
                    }
                }
            });

            if (inputReadOnly || inputMetaTable.isReadOnly()) {
                copyToInput.setEnabled(false);
                copySelectionToInput.setEnabled(false);
            }

            // make sure there only one output schema
            final CTabFolder folderOutput = new CTabFolder(compositesSachForm.getRightComposite(), SWT.BORDER);
            folderOutput.setLayoutData(new GridData(GridData.FILL_BOTH));
            folderOutput.setSimple(false);
            folderOutput.setUnselectedImageVisible(false);
            folderOutput.setUnselectedCloseVisible(false);

            CTabItem item = new CTabItem(folderOutput, SWT.NONE);
            item.setText(titleOutput);

            Composite compositeRight = new Composite(folderOutput, SWT.NONE);
            compositeRight.setLayoutData(new GridData(GridData.FILL_BOTH));
            compositeRight.setLayout(new GridLayout());

            MetadataTableEditor metadataTableEditorForOutput;
            if (isRepository(outputNode)) {
                metadataTableEditorForOutput = new MetadataTableEditor(outputMetaTable, titleOutput + " (Output)", true);
            } else {
                metadataTableEditorForOutput = new MetadataTableEditor(outputMetaTable, titleOutput + " (Output)");
            }
            outputMetaView = new MetadataTableEditorView(compositeRight, SWT.NONE, metadataTableEditorForOutput, outputReadOnly,
                    true, false, false);
            outputMetaView.setIsRepository(isRepository(outputNode));
            MetadataDialog.initializeMetadataTableView(outputMetaView, outputNode, outputMetaTable);
            outputMetaView.setShowTalendTypeColumn(showTalendTypeColumnForOutput);
            outputMetaView.initGraphicComponents();
            outputMetaView.getExtendedTableViewer().setCommandStack(commandStack);
            outputMetaView.setGridDataSize(size.x / 2 - 50, size.y - 150);

            item.setControl(compositeRight);
            folderOutput.setSelection(item);

            if (outputReadOnly || outputMetaTable.isReadOnly()) {
                copyToOutput.setEnabled(false);
                copySelectionToOutput.setEnabled(false);
            }
            compositesSachForm.setGridDatas();

        }
        CustomTableManager.addCustomManagementToTable(outputMetaView, outputReadOnly);
        CustomTableManager.addCustomManagementToToolBar(outputMetaView, outputMetaTable, outputReadOnly, null, null, false);
        metadataTableEditor.addModifiedBeanListener(new IModifiedBeanListener<IMetadataColumn>() {

            @Override
            public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                if (outputMetaTable.isReadOnly() && outputNode.getComponent().isSchemaAutoPropagated()) {
                    MetadataToolHelper.copyTable(inputMetaTable, outputMetaTable);
                    outputMetaView.getTableViewerCreator().getTableViewer().refresh();
                }
                if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                    IMetadataColumn modifiedObject = event.bean;
                    if (modifiedObject != null) {
                        String originalLabel = changedNameColumns.get(modifiedObject);
                        if (originalLabel == null) {
                            changedNameColumns.put(modifiedObject, (String) event.previousValue);
                        }
                    }
                }

            }

        });
        return composite;
    }

    private boolean isRepository(INode node) {
        IElementParameter schemaParam = node.getElementParameter("SCHEMA_TYPE");
        if (schemaParam != null) {
            String schemaType = (String) schemaParam.getValue();
            if (schemaType.equals("REPOSITORY")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns input metadata.
     * 
     * @return
     */
    public IMetadataTable getInputMetaData() {
        if (inputMetaView == null) {
            return null;
        }
        return inputMetaView.getMetadataTableEditor().getMetadataTable();
    }

    /**
     * Returns all input metadata.
     * 
     * @return
     */
    public Map<INode, Map<IMetadataTable, Boolean>> getAllInputMetaData() {
        if (inputMetaView == null) {
            return null;
        }
        return inputInfos;
    }

    /**
     * Returns output metadata.
     * 
     * @return
     */
    public IMetadataTable getOutputMetaData() {
        return outputMetaView.getMetadataTableEditor().getMetadataTable();
    }

    public void setInputReadOnly(boolean inputReadOnly) {
        this.inputReadOnly = inputReadOnly;
    }

    public void setOutputReadOnly(boolean outputReadOnly) {
        this.outputReadOnly = outputReadOnly;
    }

    /**
     * Getter for inputReadOnly.
     * 
     * @return the inputReadOnly
     */
    public boolean isInputReadOnly() {
        return this.inputReadOnly;
    }

    /**
     * Getter for outputReadOnly.
     * 
     * @return the outputReadOnly
     */
    public boolean isOutputReadOnly() {
        return this.outputReadOnly;
    }

    public ThreeCompositesSashForm getCompositesSachForm() {
        return this.compositesSachForm;
    }

    public MetadataTableEditorView getInputMetaView() {
        return this.inputMetaView;
    }

}
