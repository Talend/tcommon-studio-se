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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.datatools.xml.utils.XSDUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.dialog.RootNodeSelectDialog;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlFileStepForm;
import orgomg.cwm.resource.record.RecordFactory;
import orgomg.cwm.resource.record.RecordFile;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileOutputStep1Form extends AbstractXmlFileStepForm {

    private static Logger log = Logger.getLogger(XmlFileOutputStep1Form.class);

    private Button noFileButton;

    private Button useFileButton;

    private LabelledFileField xmlXsdFilePath;

    private LabelledCombo encodingCombo;

    private Label labelLimitation;

    private String encoding;

    private Text commonNodesLimitation;

    private LabelledFileField outputFilePath;

    private transient Tree availableXmlTree;

    private Text fileContentText;

    private TreePopulator treePopulator;

    private ATreeNode treeNode;

    private final boolean creation;

    private boolean valid = true;

    private boolean validXsd = false;

    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    private String tempXmlXsdPath;

    private boolean isModifing = true;

    private boolean isInputModel = true;

    public XmlFileOutputStep1Form(boolean creation, Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        this.creation = creation;
        setupForm(true);
    }

    @Override
    protected void initialize() {
        getConnection().setInputModel(false);
        TreeViewer treeViewer = new TreeViewer(availableXmlTree);
        treeViewer.setContentProvider(new VirtualXmlTreeNodeContentProvider(treeViewer));
        treeViewer.setLabelProvider(new VirtualXmlTreeLabelProvider());
        treeViewer.setUseHashlookup(true);

        this.treePopulator = new TreePopulator(treeViewer);
        if (getConnection().getXmlFilePath() != null) {
            xmlXsdFilePath.setText(getConnection().getXmlFilePath().replace("\\\\", "\\"));
            checkFieldsValue();
            String xmlXsdPath = xmlXsdFilePath.getText();
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
                xmlXsdPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                        xmlXsdFilePath.getText()));
            }
            if (!new File(xmlXsdPath).exists() && getConnection().getFileContent() != null
                    && getConnection().getFileContent().length > 0) {
                initFileContent();
                xmlXsdPath = tempXmlXsdPath;
            }
            if (xmlXsdPath != null && !"".equals(xmlXsdPath)
                    && (XmlUtil.isWSDLFile(xmlXsdPath) || XmlUtil.isXSDFile(xmlXsdPath) || xmlXsdPath.endsWith(".zip"))) {
                try {
                    XSDPopulationUtil2 xsdPopulationUtil = XSDUtils.getXsdHander(xmlXsdPath);
                    String targetNamespace = getConnection().getTargetNameSpace();
                    XSDSchema xsdSchema = TreeUtil.getXSDSchema(xsdPopulationUtil, xmlXsdPath, targetNamespace);
                    List<ATreeNode> rootNodes = xsdPopulationUtil.getAllRootNodes(xsdSchema);
                    if (rootNodes.size() > 0) {
                        ATreeNode rootNode = getDefaultRootNode(rootNodes);
                        List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                        if (rootNode == null) {
                            valid = treePopulator.populateTree(xsdPopulationUtil, xsdSchema, rootNodes.get(0), treeNodes);
                        } else {
                            valid = treePopulator.populateTree(xsdPopulationUtil, xsdSchema, rootNode, treeNodes);
                            if (treeNodes.size() > 0) {
                                treeNode = treeNodes.get(0);
                            }
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            } else {
                valid = this.treePopulator.populateTree(xmlXsdPath, treeNode);
            }
        }

        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) {
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
        }
        encodingCombo.clearSelection();
        if (getConnection().getOutputFilePath() != null) {
            outputFilePath.setText(getConnection().getOutputFilePath());
        }
        adaptFormToEditable();
    }

    @Override
    protected void adaptFormToEditable() {
        noFileButton.setEnabled(!isContextMode());
        useFileButton.setEnabled(!isContextMode());
        encodingCombo.setReadOnly(!isContextMode());
        outputFilePath.setEditable(!isContextMode());
        super.adaptFormToEditable();
    }

    @Override
    protected void addFields() {
        createOutputSettingArea();
        createOutputFile(this, WIDTH_GRIDDATA_PIXEL, 50);

        SashForm sash = new SashForm(this, SWT.HORIZONTAL | SWT.SMOOTH);
        GridData sashData = new GridData(GridData.FILL_BOTH);
        sash.setLayoutData(sashData);
        createFileContentViewer(sash, 400, 100);
        createFileContentText(sash, 400, 100);
    }

    private void createOutputSettingArea() {
        Group group = Form.createGroup(this, 1, "Output Setting", 80);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 150;
        group.setLayoutData(data);

        Composite compositeButton = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 20);
        noFileButton = new Button(compositeButton, SWT.RADIO);
        noFileButton.setText("Create manually");
        useFileButton = new Button(compositeButton, SWT.RADIO);
        useFileButton.setText("Create from a file");

        Composite compositeOutput = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 20);
        String[] extensions = new String[] { "*.xml;*.xsd", "*.*", "*" };
        xmlXsdFilePath = new LabelledFileField(compositeOutput, "XML or XSD File", extensions);
        xmlXsdFilePath.setText("");

        if (creation) {
            useFileButton.setSelection(!isInputModel);
            noFileButton.setSelection(isInputModel);
        } else {
            noFileButton.setSelection(!isInputModel);
            useFileButton.setSelection(isInputModel);
        }

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }
        encodingCombo = new LabelledCombo(compositeOutput, Messages.getString("XmlFileOutputStep1Form.Encording"),
                Messages.getString("XmlFileOutputStep1Form.Encording"), encodingData, 1, true, SWT.NONE);
        encodingCombo.setText("");
        Composite limitation = new Composite(compositeOutput, SWT.NONE);
        limitation.setLayout(new GridLayout(2, false));
        labelLimitation = new Label(limitation, SWT.LEFT);
        labelLimitation.setText("Limit");
        commonNodesLimitation = new Text(limitation, SWT.BORDER);
        GridData gd = new GridData(18, 12);
        commonNodesLimitation.setLayoutData(gd);
        commonNodesLimitation.setText(String.valueOf(TreePopulator.getLimit()));
        labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), commonNodesLimitation.getText()));//$NON-NLS-1$
    }

    private void createOutputFile(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 1, "Output File Path", height);
        GridData fileData = new GridData(GridData.FILL_HORIZONTAL);
        fileData.heightHint = 60;
        group.setLayoutData(fileData);
        Composite compositeFile = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, height);
        String[] outputExtensions = new String[] { "*.xml" };
        outputFilePath = new LabelledFileField(compositeFile, "Output file", outputExtensions);
        outputFilePath.setText("");
    }

    private void createFileContentViewer(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 1, "File Viewer", height);
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, height);

        GridData gridData = new GridData(GridData.FILL_BOTH);

        availableXmlTree = new Tree(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
        availableXmlTree.setLayoutData(gridData);
    }

    private void createFileContentText(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 1, "File Content", height);
        Composite compositeFileContext = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, height);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        fileContentText = new Text(compositeFileContext, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        fileContentText.setLayoutData(gridData);
        fileContentText.setToolTipText("When Filepath is specified, you can read here the "
                + TreePopulator.getMaximumRowsToPreview() + " first rows of the file.");
        fileContentText.setText("Filepath must be specified to show the Data file");
    }

    private void updateConnection(String text) {
        if (text == null || "".equals(text)) {
            return;
        }

        List<FOXTreeNode> rootFoxTreeNodes = null;
        if (treeNode == null) {
            rootFoxTreeNodes = TreeUtil.getFoxTreeNodes(text);
        } else {
            rootFoxTreeNodes = getCorrespondingFoxTreeNodes(treeNode, true);
        }

        if (rootFoxTreeNodes.size() == 0) {
            return;
        }
        if (ConnectionHelper.getTables(getConnection()).isEmpty()) {
            MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
            RecordFile record = (RecordFile) ConnectionHelper.getPackage(getConnection().getName(), getConnection(),
                    RecordFile.class);
            if (record != null) { // hywang
                PackageHelper.addMetadataTable(table, record);
            } else {
                RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                newrecord.setName(connection.getName());
                ConnectionHelper.addPackage(newrecord, connection);
                PackageHelper.addMetadataTable(table, newrecord);
            }
        }
        EList schemaMetadataColumn = ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0].getColumns();
        schemaMetadataColumn.clear();
        initMetadataTable(rootFoxTreeNodes, schemaMetadataColumn);
        updateConnectionProperties(rootFoxTreeNodes.get(0));
    }

    @Override
    protected void addFieldsListeners() {
        xmlXsdFilePath.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (xmlXsdFilePath.getResult() == null) {
                    return;
                }
                String text = xmlXsdFilePath.getText();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), true);
                    text = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, text));
                }
                // getConnection().setXmlFilePath(PathUtils.getPortablePath(xmlXsdFilePath.getText()));
                File file = new File(text);
                if (file.exists()) {
                    List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                    if (XmlUtil.isXSDFile(text)) {
                        if (!validXsd) {
                            try {
                                XSDSchema xsdSchema = updateXSDSchema(text);
                                List<ATreeNode> list = updateRootNodes(xsdSchema, true);
                                if (list.size() > 1) {
                                    RootNodeSelectDialog dialog = new RootNodeSelectDialog(null, list);
                                    if (dialog.open() == IDialogConstants.OK_ID) {
                                        ATreeNode selectedNode = dialog.getSelectedNode();
                                        valid = treePopulator.populateTree(xsdSchema, selectedNode, treeNodes);
                                    } else {
                                        return;
                                    }
                                } else {
                                    valid = treePopulator.populateTree(xsdSchema, list.get(0), treeNodes);
                                }
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    } else {
                        valid = treePopulator.populateTree(text, treeNode);
                    }
                    checkFieldsValue();
                    if (!valid) {
                        return;
                    }
                    if (treeNodes.size() > 0) {
                        treeNode = treeNodes.get(0);
                    }

                    updateConnection(text);
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        xmlXsdFilePath.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                String text = xmlXsdFilePath.getText();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), true);
                    text = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, text));
                }

                if (getConnection().getXmlFilePath() != null && !getConnection().getXmlFilePath().equals(text)) {
                    getConnection().getLoop().clear();
                    getConnection().getRoot().clear();
                    getConnection().getGroup().clear();
                    xsdPathChanged = true;
                } else {
                    xsdPathChanged = false;
                }
                getConnection().setXmlFilePath(PathUtils.getPortablePath(xmlXsdFilePath.getText()));
                // updateConnection(text);

                StringBuilder fileContent = new StringBuilder();
                BufferedReader in = null;
                File file = null;
                if (tempXmlXsdPath != null && getConnection().getFileContent() != null
                        && getConnection().getFileContent().length > 0 && !isModifing) {
                    file = new File(tempXmlXsdPath);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e2) {
                            ExceptionHandler.process(e2);
                        }
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

                } else {
                    file = new File(text);
                }

                if (treePopulator.isValidFile(file.getPath())) {
                    setFileContent(file);
                }

                String str;
                try {
                    Charset guessCharset = CharsetToolkit.guessEncoding(file, 4096);
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file), guessCharset.displayName()));

                    while ((str = in.readLine()) != null) {
                        fileContent.append(str + "\n");
                        // for encoding
                        if (str.contains("encoding")) {
                            String regex = "^<\\?xml\\s*version=\\\"[^\\\"]*\\\"\\s*encoding=\\\"([^\\\"]*)\\\"\\?>$";
                            Perl5Compiler compiler = new Perl5Compiler();
                            Perl5Matcher matcher = new Perl5Matcher();
                            Pattern pattern = null;
                            try {
                                pattern = compiler.compile(regex);
                                if (matcher.contains(str, pattern)) {
                                    MatchResult matchResult = matcher.getMatch();
                                    if (matchResult != null) {
                                        encoding = matchResult.group(1);
                                    }
                                }
                            } catch (MalformedPatternException malE) {
                                ExceptionHandler.process(malE);
                            }
                        }
                    }

                    fileContentText.setText(new String(fileContent));

                } catch (Exception e) {
                    String msgError = Messages.getString("FileStep1.filepath") + " \""
                            + xmlXsdFilePath.getText().replace("\\\\", "\\") + "\"\n";
                    if (e instanceof FileNotFoundException) {
                        msgError = msgError + Messages.getString("FileStep1.fileNotFoundException");
                    } else if (e instanceof EOFException) {
                        msgError = msgError + Messages.getString("FileStep1.eofException"); //$NON-NLS-1$
                    } else if (e instanceof IOException) {
                        msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                    } else {
                        msgError = msgError + Messages.getString("FileStep1.noExist"); //$NON-NLS-1$
                    }
                    fileContentText.setText(msgError);
                    if (!isReadOnly()) {
                        updateStatus(IStatus.ERROR, msgError);
                    }
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception exception) {
                        ExceptionHandler.process(exception);
                    }
                }
                if (getConnection().getEncoding() == null || "".equals(getConnection().getEncoding())) {
                    getConnection().setEncoding(encoding);
                    if (encoding != null && !"".equals(encoding)) {
                        encodingCombo.setText(encoding);
                    } else {
                        encodingCombo.setText("UTF-8");
                    }
                }

                // if (tempXmlXsdPath != null && getConnection().getFileContent() != null
                // && getConnection().getFileContent().length > 0 && !isModifing) {
                // valid = treePopulator.populateTree(tempXmlXsdPath, treeNode);
                // } else {
                // valid = treePopulator.populateTree(text, treeNode);
                // }
                if (file.exists() && (xsdPathChanged || creation)) {
                    List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                    if (XmlUtil.isXSDFile(text)) {
                        try {
                            XSDSchema xsdSchema = updateXSDSchema(text);
                            List<ATreeNode> list = updateRootNodes(xsdSchema, true);
                            if (list.size() > 1) {
                                RootNodeSelectDialog dialog = new RootNodeSelectDialog(null, list);
                                if (dialog.open() == IDialogConstants.OK_ID) {
                                    ATreeNode selectedNode = dialog.getSelectedNode();
                                    valid = treePopulator.populateTree(xsdSchema, selectedNode, treeNodes);
                                    validXsd = true;
                                } else {
                                    return;
                                }
                            } else {
                                valid = treePopulator.populateTree(xsdSchema, list.get(0), treeNodes);
                            }
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    } else {
                        valid = treePopulator.populateTree(text, treeNode);
                    }
                    checkFieldsValue();
                    if (!valid) {
                        return;
                    }
                    if (treeNodes.size() > 0) {
                        treeNode = treeNodes.get(0);
                    }
                    updateConnection(text);
                }
                checkFieldsValue();
                isModifing = true;
            }
        });

        encodingCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setEncoding(encodingCombo.getText());
                checkFieldsValue();
            }
        });

        commonNodesLimitation.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                String str = commonNodesLimitation.getText();
                if ((!str.matches("\\d+")) || (Integer.valueOf(str) < 0)) {
                    commonNodesLimitation.setText(String.valueOf(treePopulator.getLimit()));
                    labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), commonNodesLimitation.getText()));//$NON-NLS-1$
                } else {
                    treePopulator.setLimit(Integer.valueOf(str));
                    labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), str));//$NON-NLS-1$
                }
                valid = treePopulator.populateTree(xmlXsdFilePath.getText(), treeNode);
                checkFieldsValue();
            }
        });
        commonNodesLimitation.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                commonNodesLimitation.setText(String.valueOf(TreePopulator.getLimit()));
                labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), commonNodesLimitation.getText()));//$NON-NLS-1$
            }

        });

        outputFilePath.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setOutputFilePath(PathUtils.getPortablePath(outputFilePath.getText()));
                checkFieldsValue();
            }
        });
    }

    @Override
    protected void addUtilsButtonListeners() {
        noFileButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                xmlXsdFilePath.setEditable(false);
                xmlXsdFilePath.setText("");
                encodingCombo.setEnabled(false);
                commonNodesLimitation.setEditable(false);
                availableXmlTree.setEnabled(false);
                fileContentText.setEnabled(false);
                getConnection().setXmlFilePath("");
                ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0].getColumns().clear();
                // ((MetadataTable) getConnection().getTables().get(0)).getColumns().clear();
                getConnection().getRoot().clear();
                getConnection().getLoop().clear();
                getConnection().getGroup().clear();
                checkFieldsValue();
            }
        });
        useFileButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                xmlXsdFilePath.setEditable(true);
                String text = xmlXsdFilePath.getText();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), true);
                    text = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, text));
                }
                getConnection().setXmlFilePath(text);
                updateConnection(text);
                encodingCombo.setEnabled(true);
                commonNodesLimitation.setEditable(true);
                availableXmlTree.setEnabled(true);
                fileContentText.setEnabled(true);
                fileContentText.setEditable(false);
                checkFieldsValue();
            }
        });
    }

    @Override
    protected boolean checkFieldsValue() {
        String xmlXsdFilePathText = xmlXsdFilePath.getText();
        String outputFilePathText = outputFilePath.getText();
        boolean editable = xmlXsdFilePath.getEditable();
        StringBuffer msgError = new StringBuffer();
        if (creation && !noFileButton.getSelection() && !useFileButton.getSelection()) {
            msgError.append("Should select one model\n");
        }
        if (creation && editable && xmlXsdFilePathText == "") {
            msgError.append("XML or XSD File must be specified\n");
        }
        if (!valid && creation) {
            if (xmlXsdFilePathText != null && !"".equals(xmlXsdFilePathText)) {
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper
                            .getContextTypeForContextMode(connectionItem.getConnection());
                    xmlXsdFilePathText = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                            xmlXsdFilePathText));
                }
                msgError.append(xmlXsdFilePathText + " is not found or the xml format is incorrect.\n");
            }
        }
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            outputFilePathText = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                    outputFilePathText));
        }
        // Valid File
        if (xmlXsdFilePathText != null && !xmlXsdFilePathText.equals("") && (xsdPathChanged || creation)) {
            if (treePopulator.isValidFile(xmlXsdFilePathText)) {
                if (!XmlUtil.isXMLFile(xmlXsdFilePathText) && !XmlUtil.isXSDFile(xmlXsdFilePathText)) {
                    msgError.append("XML or XSD File Path is incorrect or incomplete, it must be changed.\n");
                }
            } else {
                msgError.append("XML or XSD File Path is incorrect or incomplete, it must be changed.\n");
            }
        }
        if (outputFilePathText != null && !outputFilePathText.equals("") && !XmlUtil.isXMLFile(outputFilePathText)) {
            msgError.append("Output file is not a xml file\n");
        }
        if ("".equals(msgError.toString())) {
            updateStatus(IStatus.OK, null);
            return true;
        }
        updateStatus(IStatus.ERROR, msgError.toString());
        return false;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) {
                encodingCombo.setText(getConnection().getEncoding());
                isModifing = false;
                xmlXsdFilePath.setText(getConnection().getXmlFilePath());
                outputFilePath.setText(getConnection().getOutputFilePath());
            }
            String xmlFilePath = getConnection().getXmlFilePath();
            boolean enable = (xmlFilePath != null && !xmlFilePath.equals(""));
            if (!creation) {
                noFileButton.setSelection(!enable);
                useFileButton.setSelection(enable);
                if (XmlUtil.isXSDFile(xmlFilePath)) {
                    updateTreeNodes(xmlFilePath);
                }
            }
            if (!isContextMode()) {
                xmlXsdFilePath.setEditable(enable);
                encodingCombo.setEnabled(enable);
                commonNodesLimitation.setEditable(enable);
                availableXmlTree.setEnabled(enable);
                fileContentText.setEnabled(enable);
                outputFilePath.setEditable(true);
            } else {
                // noFileButton.setEnabled(!isContextMode());
                // useFileButton.setEnabled(!isContextMode());
                outputFilePath.setEditable(!isContextMode());
                xmlXsdFilePath.setEditable(!isContextMode());
                encodingCombo.setEnabled(!isContextMode());
                commonNodesLimitation.setEditable(!isContextMode());
                availableXmlTree.setEnabled(!isContextMode());
                fileContentText.setEnabled(!isContextMode());
            }
        }
    }

    @Override
    protected void adaptFormToReadOnly() {

    }

    private void setFileContent(File initFile) {
        InputStream stream = null;
        try {
            stream = initFile.toURL().openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            getConnection().setFileContent(innerContent);
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    private void initFileContent() {
        if (getConnection().getXmlFilePath() == null || "".equals(getConnection().getXmlFilePath())) {
            return;
        }
        byte[] bytes = getConnection().getFileContent();

        String temPath = getProjectTempPath();
        String fileName = "";

        String xmlXsdPath = getConnection().getXmlFilePath();
        if (isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
            xmlXsdPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, xmlXsdPath));
        }
        if (xmlXsdPath != null && XmlUtil.isXMLFile(xmlXsdPath)) {
            fileName = StringUtil.TMP_XML_FILE;
        } else if (xmlXsdPath != null && XmlUtil.isXSDFile(xmlXsdPath)) {
            fileName = StringUtil.TMP_XSD_FILE;
        } else if (xmlXsdPath != null && XmlUtil.isWSDLFile(xmlXsdPath)) {
            fileName = StringUtil.TMP_WSDL_FILE;
        } else if (xmlXsdPath.contains(".zip")) {
            fileName = new Path(xmlXsdPath).lastSegment();
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
        // valid = this.treePopulator.populateTree(tempXmlXsdPath, treeNode);

    }

    private String getProjectTempPath() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp";
        return temPath;
    }
}
