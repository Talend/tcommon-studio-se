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
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.dialog.RootNodeSelectDialog;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class XmlFileStep1Form extends AbstractXmlFileStepForm {

    private static Logger log = Logger.getLogger(XmlFileStep1Form.class);

    /**
     * Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    /**
     * Main Fields.
     */
    private LabelledFileField fileFieldXsd;

    private LabelledFileField fileFieldXml;

    private LabelledText fieldMaskXPattern;

    private Label labelIsGuess;

    private Button checkBoxIsGuess;

    /**
     * Another.
     */
    private boolean filePathIsDone;

    private transient Tree availableXmlTree;

    private ATreeNode treeNode;

    private UtilsButton cancelButton;

    private boolean readOnly;

    private boolean creation;

    private TreePopulator treePopulator;

    private LabelledCombo encodingCombo;

    private String encoding;

    private String tempXmlXsdPath;

    private boolean isModifing = true;

    private boolean validXsd = false;

    private String convertedFilePath;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public XmlFileStep1Form(boolean creation, Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        this.creation = creation;
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {
        getConnection().setInputModel(true);
        TreeViewer treeViewer = new TreeViewer(availableXmlTree);
        treeViewer.setContentProvider(new VirtualXmlTreeNodeContentProvider(treeViewer));
        treeViewer.setLabelProvider(new VirtualXmlTreeLabelProvider());
        treeViewer.setUseHashlookup(true);

        this.treePopulator = new TreePopulator(treeViewer);

        if (getConnection().getXmlFilePath() != null) {
            fileFieldXml.setText(getConnection().getXmlFilePath().replace("\\\\", "\\")); //$NON-NLS-1$ //$NON-NLS-2$
            // init the fileViewer
            // checkFieldsValue();
            convertedFilePath = fileFieldXml.getText();
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
                convertedFilePath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                        fileFieldXml.getText()));
            }
            if (!new File(convertedFilePath).exists() && getConnection().getFileContent() != null
                    && getConnection().getFileContent().length > 0) {
                initFileContent();
                convertedFilePath = tempXmlXsdPath;
            }
            if (XmlUtil.isXSDFile(convertedFilePath)) {
                try {
                    XSDSchema schema = TreeUtil.getXSDSchema(convertedFilePath);
                    List<ATreeNode> rootNodes = new XSDPopulationUtil2().getAllRootNodes(schema);
                    if (rootNodes.size() > 0) {
                        ATreeNode rootNode = getDefaultRootNode(rootNodes);
                        if (rootNode != null) {
                            List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                            valid = treePopulator.populateTree(schema, rootNode, treeNodes);
                            if (treeNodes.size() > 0) {
                                treeNode = treeNodes.get(0);
                            }
                        } else {
                            String xmlPath = getConnection().getSchema().get(0).getAbsoluteXPathQuery();
                            if (xmlPath != null && xmlPath.length() > 0) {
                                xmlPath = xmlPath.substring(xmlPath.lastIndexOf("/") + 1); //$NON-NLS-1$
                                boolean found = false;
                                for (int i = 0; i < rootNodes.size(); i++) {
                                    ATreeNode node = rootNodes.get(i);
                                    if (xmlPath.equals(node.getValue())) {
                                        List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                                        valid = treePopulator.populateTree(schema, node, treeNodes);
                                        if (treeNodes.size() > 0) {
                                            treeNode = treeNodes.get(0);
                                        }
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    for (int i = 0; i < rootNodes.size(); i++) {
                                        ATreeNode node = rootNodes.get(i);
                                        String[] nodeValue = ((String) node.getValue()).split(":");
                                        if (nodeValue.length > 1) {
                                            if (xmlPath.equals(nodeValue[1])) {
                                                List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                                                valid = treePopulator.populateTree(schema, node, treeNodes);
                                                if (treeNodes.size() > 0) {
                                                    treeNode = treeNodes.get(0);
                                                }
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            } else {
                valid = this.treePopulator.populateTree(convertedFilePath, treeNode);
            }

        }

        // Fields to the Group Delimited File Settings
        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
        }
        encodingCombo.clearSelection();

        // if (getConnection().getMaskXPattern() != null) {
        // fieldMaskXPattern.setText(getConnection().getMaskXPattern().replace("\\\\", "\\"));
        // }
        adaptFormToEditable();

    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        // fileFieldXsd.setReadOnly(isReadOnly());
        fileFieldXml.setReadOnly(isReadOnly());
        // fieldMaskXPattern.setReadOnly(isReadOnly());
        // checkBoxIsGuess.setReadOnly(isReadOnly());
        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    @Override
    protected void adaptFormToEditable() {
        fileFieldXml.setEditable(!isContextMode());
        encodingCombo.setReadOnly(isContextMode());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        // Group File Location
        Group group = Form.createGroup(this, 1, Messages.getString("FileStep2.groupDelimitedFileSettings"), 100); //$NON-NLS-1$
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 100);

        GridData gridDataFileLocation = new GridData(GridData.FILL_HORIZONTAL);
        gridDataFileLocation.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        group.setLayoutData(gridDataFileLocation);

        // file Field XSD
        //        String[] xsdExtensions = { "*.xsd", "*.*", "*" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // fileFieldXsd = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXsd"),
        // xsdExtensions);

        // checkBox IsGuess
        // checkBoxIsGuess = new Button(compositeFileLocation, SWT.CHECK);
        // labelIsGuess = new Label(compositeFileLocation, SWT.LEFT);
        // GridData gridDataLabel = new GridData();
        // gridDataLabel.horizontalSpan = 2;
        // labelIsGuess.setLayoutData(gridDataLabel);
        // labelIsGuess.setText(Messages.getString("XmlFileStep1.checkBoxIsGuess"));

        // file Field XML
        String[] xmlExtensions = { "*.xml;*.xsd", "*.*", "*" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
        fileFieldXml = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXml"), //$NON-NLS-1$
                xmlExtensions);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                .getString("FileStep2.encodingTip"), encodingData, 1, true, SWT.NONE); //$NON-NLS-1$

        Composite limitation = new Composite(compositeFileLocation, SWT.NONE);
        limitation.setLayout(new GridLayout(2, false));

        Label labelLimitation = new Label(limitation, SWT.LEFT);
        labelLimitation.setText(Messages.getString("XmlFileStep1Form.limitation")); //$NON-NLS-1$
        final Text commonNodesLimitation = new Text(limitation, SWT.BORDER);
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_FILL);
        gd.widthHint = 18;
        commonNodesLimitation.setLayoutData(gd);
        commonNodesLimitation.setText(String.valueOf(TreePopulator.getLimit()));
        labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), commonNodesLimitation.getText()));//$NON-NLS-1$

        commonNodesLimitation.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {

                String str = commonNodesLimitation.getText();

                if ((!str.matches("\\d+")) || (Integer.valueOf(str) < 0)) { //$NON-NLS-1$
                    commonNodesLimitation.setText(String.valueOf(treePopulator.getLimit()));
                    labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), commonNodesLimitation.getText()));//$NON-NLS-1$
                } else {
                    treePopulator.setLimit(Integer.valueOf(str));
                    labelLimitation.setToolTipText(MessageFormat.format(Messages.getString("XmlFileStep1Form.limitToolTip"), str));//$NON-NLS-1$
                }
                if (tempXmlXsdPath != null && getConnection().getFileContent() != null
                        && getConnection().getFileContent().length > 0) {
                    valid = treePopulator.populateTree(tempXmlXsdPath, treeNode);
                } else {
                    valid = treePopulator.populateTree(fileFieldXml.getText(), treeNode);
                }
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

        // field XmaskPattern
        // fieldMaskXPattern = new LabelledText(compositeFileLocation, Messages.getString("XmlFileStep1.maskXPattern"));

        // Group Schema Viewer
        group = Form.createGroup(this, 1, Messages.getString("XmlFileStep1.groupFileViewer"), 220); //$NON-NLS-1$
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 220);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        // gridData.minimumHeight = 150;

        availableXmlTree = new Tree(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
        availableXmlTree.setLayoutData(gridData);

        if (!isInWizard()) {
            // Composite BottomButton
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);

            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), //$NON-NLS-1$
                    WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    @Override
    protected void addUtilsButtonListeners() {

        if (!isInWizard()) {
            // Event cancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }
    }

    boolean valid = true;

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {

        // fileFieldXsd : Event modifyText
        // fileFieldXsd.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(final ModifyEvent e) {
        // getConnection().setXsdFilePath(fileFieldXsd.getText());
        // treePopulator.populateTree(fileFieldXsd.getText(), treeNode);
        // checkFieldsValue();
        // }
        // });

        fileFieldXml.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (fileFieldXml.getResult() == null) {
                    return;
                }
                String text = fileFieldXml.getText();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), true);
                    text = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, text));
                }
                // getConnection().setXmlFilePath(PathUtils.getPortablePath(xmlXsdFilePath.getText()));
                File file = new File(text);
                if (file.exists()) {
                    if (XmlUtil.isXSDFile(text)) {
                        if (!validXsd) {
                            List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                            try {
                                XSDSchema xsdSchema = updateXSDSchema(text);
                                List<ATreeNode> list = updateRootNodes(xsdSchema, true);
                                if (list.size() > 1) {
                                    RootNodeSelectDialog dialog = new RootNodeSelectDialog(null, list);
                                    if (dialog.open() == IDialogConstants.OK_ID) {
                                        ATreeNode selectedNode = dialog.getSelectedNode();
                                        valid = treePopulator.populateTree(xsdSchema, selectedNode, treeNodes);
                                        if (treeNodes.size() > 0) {
                                            treeNode = treeNodes.get(0);
                                        }
                                    } else {
                                        return;
                                    }
                                } else {
                                    valid = treePopulator.populateTree(xsdSchema, list.get(0), treeNodes);
                                    if (treeNodes.size() > 0) {
                                        treeNode = treeNodes.get(0);
                                    }
                                }
                            } catch (Exception ex) {
                                ExceptionHandler.process(ex);
                            }
                            XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
                            wizard.setRootNodes(treeNodes);
                            wizard.setTreeRootNode(treeNode);
                            List<FOXTreeNode> nodeList = getCorrespondingFoxTreeNodes(treeNode, true);
                            if (nodeList.size() > 0) {
                                FOXTreeNode foxTreeNode = nodeList.get(0);
                                EList root = getConnection().getRoot();
                                if (root == null) {
                                    return;
                                }
                                XMLFileNode xmlFileNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
                                String currentPath = "/" + foxTreeNode.getLabel();
                                xmlFileNode.setXMLPath(currentPath);
                                xmlFileNode.setRelatedColumn(foxTreeNode.getColumnLabel());
                                xmlFileNode.setAttribute(foxTreeNode.isMain() ? "main" : "branch");
                                xmlFileNode.setDefaultValue(foxTreeNode.getDefaultValue());
                                xmlFileNode.setType(foxTreeNode.getDataType());
                                XMLFileNode originalXmlNode = null;
                                if (root.size() > 0) {
                                    originalXmlNode = (XMLFileNode) root.get(0);
                                }
                                if (originalXmlNode != null && !currentPath.equals(originalXmlNode.getXMLPath())) {
                                    wizard.setXsdRootChange(true);
                                } else {
                                    wizard.setXsdRootChange(false);
                                }
                                root.clear();
                                root.add(xmlFileNode);
                            }
                        }
                    } else {
                        valid = treePopulator.populateTree(text, treeNode);
                    }
                }
                // add for bug TDI-20432
                checkFieldsValue();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        // fileFieldXml : Event modifyText
        fileFieldXml.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                String text = fileFieldXml.getText();
                if (isContextMode()) {
                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(
                            connectionItem.getConnection(), connectionItem.getConnection().getContextName());
                    text = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, text));
                }
                // no use to popoup for context mode because wizard is readonly
                if (getConnection().getXmlFilePath() != null && !getConnection().getXmlFilePath().equals(text)
                        && !isContextMode()) {
                    getConnection().getLoop().clear();
                    xsdPathChanged = true;
                } else {
                    xsdPathChanged = false;
                }
                getConnection().setXmlFilePath(PathUtils.getPortablePath(fileFieldXml.getText()));

                XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
                wizard.setTreeRootNode(treeNode);

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
                        } catch (IOException e3) {
                            ExceptionHandler.process(e3);
                        }
                    }

                } else {
                    file = new File(text);
                }

                if (treePopulator.isValidFile(file.getPath())) {
                    setFileContent(file);
                }

                try {

                    Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
                    String str;
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file), guessedCharset.displayName()));
                    while ((str = in.readLine()) != null) {
                        if (str.contains("encoding")) { //$NON-NLS-1$
                            String regex = "^<\\?xml\\s*version=\\\"[^\\\"]*\\\"\\s*encoding=\\\"([^\\\"]*)\\\"\\?>$"; //$NON-NLS-1$

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
                } catch (Exception ex) {
                    String fileStr = text;
                    String msgError = "XML" + " \"" + fileStr.replace("\\\\", "\\") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                            + "\"\n"; //$NON-NLS-1$
                    if (ex instanceof FileNotFoundException) {
                        msgError = msgError + Messages.getString("FileStep1.fileNotFoundException"); //$NON-NLS-1$
                    } else if (ex instanceof EOFException) {
                        msgError = msgError + Messages.getString("FileStep1.eofException"); //$NON-NLS-1$
                    } else if (ex instanceof IOException) {
                        msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                    } else {
                        msgError = msgError + Messages.getString("FileStep1.noExist"); //$NON-NLS-1$
                    }
                    if (!isReadOnly()) {
                        updateStatus(IStatus.ERROR, msgError);
                    }
                    // ExceptionHandler.process(ex);
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception ex2) {
                        ExceptionHandler.process(ex2);
                    }
                }
                if (getConnection().getEncoding() == null || "".equals(getConnection().getEncoding())) { //$NON-NLS-1$
                    getConnection().setEncoding(encoding);
                    if (encoding != null && !("").equals(encoding)) { //$NON-NLS-1$
                        encodingCombo.setText(encoding);
                    } else {
                        encodingCombo.setText("UTF-8"); //$NON-NLS-1$
                    }
                }
                // if (tempXmlXsdPath != null && getConnection().getFileContent() != null
                // && getConnection().getFileContent().length > 0 && !isModifing) {
                // valid = treePopulator.populateTree(tempXmlXsdPath, treeNode);
                // } else {
                // valid = treePopulator.populateTree(text, treeNode);
                // }
                if (file.exists() && (xsdPathChanged || creation)) {
                    if (XmlUtil.isXSDFile(text)) {
                        List<ATreeNode> treeNodes = new ArrayList<ATreeNode>();
                        try {
                            XSDSchema xsdSchema = updateXSDSchema(text);
                            List<ATreeNode> list = updateRootNodes(xsdSchema, true);
                            if (list.size() > 1) {
                                RootNodeSelectDialog dialog = new RootNodeSelectDialog(null, list);
                                if (dialog.open() == IDialogConstants.OK_ID) {
                                    ATreeNode selectedNode = dialog.getSelectedNode();
                                    valid = treePopulator.populateTree(xsdSchema, selectedNode, treeNodes);
                                    if (treeNodes.size() > 0) {
                                        treeNode = treeNodes.get(0);
                                    }
                                    validXsd = true;
                                } else {
                                    return;
                                }
                            } else {
                                valid = treePopulator.populateTree(xsdSchema, list.get(0), treeNodes);
                                if (treeNodes.size() > 0) {
                                    treeNode = treeNodes.get(0);
                                }
                            }
                        } catch (Exception ex) {
                            ExceptionHandler.process(ex);
                        }
                        XmlFileWizard wizard1 = ((XmlFileWizard) getPage().getWizard());
                        wizard1.setRootNodes(treeNodes);
                        wizard1.setTreeRootNode(treeNode);
                        List<FOXTreeNode> nodeList = getCorrespondingFoxTreeNodes(treeNode, true);
                        if (nodeList.size() > 0) {
                            FOXTreeNode foxTreeNode = nodeList.get(0);
                            EList root = getConnection().getRoot();
                            if (root == null) {
                                return;
                            }
                            XMLFileNode xmlFileNode = ConnectionFactory.eINSTANCE.createXMLFileNode();
                            String currentPath = "/" + foxTreeNode.getLabel();
                            xmlFileNode.setXMLPath(currentPath);
                            xmlFileNode.setRelatedColumn(foxTreeNode.getColumnLabel());
                            xmlFileNode.setAttribute(foxTreeNode.isMain() ? "main" : "branch");
                            xmlFileNode.setDefaultValue(foxTreeNode.getDefaultValue());
                            xmlFileNode.setType(foxTreeNode.getDataType());
                            XMLFileNode originalXmlNode = null;
                            if (root.size() > 0) {
                                originalXmlNode = (XMLFileNode) root.get(0);
                            }
                            if (originalXmlNode != null && !currentPath.equals(originalXmlNode.getXMLPath())) {
                                wizard1.setXsdRootChange(true);
                            } else {
                                wizard1.setXsdRootChange(false);
                            }
                            root.clear();
                            root.add(xmlFileNode);
                        }
                    } else {
                        valid = treePopulator.populateTree(text, treeNode);
                    }
                }
                checkFieldsValue();
                isModifing = true;
            }
        });

        // Event encodingCombo
        encodingCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                getConnection().setEncoding(encodingCombo.getText());
                checkFieldsValue();
            }
        });

    }

    /**
     * Ensures that fields are set.
     * 
     * @return
     */
    @Override
    protected boolean checkFieldsValue() {
        String xmlXsdFilePathText = fileFieldXml.getText();
        // The fields
        if (xmlXsdFilePathText == "") { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.filepathAlert")); //$NON-NLS-1$
            return false;
        }
        if (isContextMode()) {
            // ContextType contextType =
            // ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
            // xmlXsdFilePathText = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
            // xmlXsdFilePathText));
            xmlXsdFilePathText = convertedFilePath;
        }
        if (!valid) {
            updateStatus(IStatus.ERROR, Messages.getString("XmlFileStep1Form.notFound", xmlXsdFilePathText)); //$NON-NLS-1$
            return false;
        }

        // Valid File
        if (xmlXsdFilePathText != null && !xmlXsdFilePathText.equals("") && (xsdPathChanged || creation)) {
            if (treePopulator.isValidFile(xmlXsdFilePathText)) {
                if (!XmlUtil.isXMLFile(xmlXsdFilePathText) && !XmlUtil.isXSDFile(xmlXsdFilePathText)) {
                    updateStatus(IStatus.ERROR, "XML or XSD File Path is incorrect or incomplete, it must be changed.\n"); //$NON-NLS-1$
                    return false;
                }
            } else {
                updateStatus(IStatus.ERROR, "XML or XSD File Path is incorrect or incomplete, it must be changed.\n"); //$NON-NLS-1$
                return false;
            }
        }
        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
        if (super.isVisible()) {
            String filePath = getConnection().getXmlFilePath();
            // Fields to the Group Delimited File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(getConnection().getEncoding());
                isModifing = false;
                fileFieldXml.setText(filePath);
            } else {
                encodingCombo.select(0);
            }

            if (isContextMode()) {
                // ContextType contextType =
                // ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                // connectionItem.getConnection().getContextName());
                // filePath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                // filePath));
                filePath = convertedFilePath;
            }
            if (!creation && XmlUtil.isXSDFile(filePath)) {
                updateTreeNodes(filePath);
                XmlFileWizard wizard = ((XmlFileWizard) getPage().getWizard());
                if (wizard.getTreeRootNode() == null) {
                    wizard.setTreeRootNode(treeNode);
                }
            }

            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL
                    && GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                ILibrariesService moduleService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                        ILibrariesService.class);
                try {
                    ELibraryInstallStatus status = moduleService.getLibraryStatus("XML::LibXML"); //$NON-NLS-1$
                    if (status != ELibraryInstallStatus.INSTALLED) {
                        new ErrorDialogWidthDetailArea(
                                getShell(),
                                PID,
                                Messages.getString("FileStep.moduleFailure") + " XML::LibXML " + Messages.getString("FileStep.moduleFailureEnd"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                Messages.getString("FileStep.moduleDetailMessage")); //$NON-NLS-1$
                        log.error(Messages.getString("FileStep.moduleFailure") + " XML::LibXML " + Messages.getString("FileStep.moduleFailureEnd")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                } catch (BusinessException e) {
                    new ErrorDialogWidthDetailArea(
                            getShell(),
                            PID,
                            Messages.getString("FileStep.moduleFailure") + " XML::LibXML " + Messages.getString("FileStep.moduleFailureEnd"), e //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                    .getMessage());
                    log.error(Messages.getString("FileStep.moduleFailure") + " XML::LibXML " + Messages.getString("FileStep.moduleFailureEnd")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            }

            adaptFormToEditable();

        }
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
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp"; //$NON-NLS-1$
        String fileName = ""; //$NON-NLS-1$
        String filePath = getConnection().getXmlFilePath();
        if (getConnection().isContextMode()) {
            filePath = convertedFilePath;
        }
        if (getConnection().getXmlFilePath() != null && XmlUtil.isXMLFile(filePath)) {
            fileName = StringUtil.TMP_XML_FILE;
        } else if (getConnection().getXmlFilePath() != null && XmlUtil.isXSDFile(filePath)) {
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
        // if (isContextMode()) {
        // ContextType contextType =
        // ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
        // tempXmlXsdPath = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
        // tempXmlXsdPath));
        // }
        // valid = this.treePopulator.populateTree(tempXmlXsdPath, treeNode);
        // temfile.delete();
    }
}
