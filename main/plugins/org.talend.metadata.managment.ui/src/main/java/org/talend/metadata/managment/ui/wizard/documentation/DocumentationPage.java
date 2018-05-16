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
package org.talend.metadata.managment.ui.wizard.documentation;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkType;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.documentation.LinkUtils.LinkInfo;

/**
 * Wizard page collecting informations to create a new IDocumentation. <br/>
 * 
 * $Id: DocumentationPage.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class DocumentationPage extends PropertiesWizardPage {

    /** Filename text. */
    private Text filenameText;

    /** CutFilename text. */
    private Text originalFilenameText;

    /** Browse button. */
    private Button browseBtn;

    private IStatus filenameStatus;

    private Button checkLinkBtn;

    private Item documentationItem;

    private String orignalName;

    private String orignalDescription;

    private String orignalversion;

    private String orignalStatus;

    private String orignalpurpose;

    /**
     * Constructs a new DocumentationCreatePage.
     */
    public DocumentationPage(Property property, IPath destinationPath) {
        super("DocumentationCreatePage", property, destinationPath); //$NON-NLS-1$
        setTitle(Messages.getString("DocumentationPage.thisTitle.document")); //$NON-NLS-1$
        this.filenameStatus = createOkStatus();
        documentationItem = property.getItem();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        GridData data;

        // Source file
        Label filenameLab = new Label(container, SWT.NONE);
        filenameLab.setText(Messages.getString("DocumentationPage.fileNameTabText.sourcDocFile")); //$NON-NLS-1$

        Composite filenameContainer = new Composite(container, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = layout.numColumns - 1;
        filenameContainer.setLayoutData(data);
        GridLayout filenameLayout = new GridLayout(2, false);
        filenameLayout.marginHeight = 0;
        filenameLayout.marginWidth = 0;
        filenameContainer.setLayout(filenameLayout);

        filenameText = new Text(filenameContainer, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        filenameText.setLayoutData(data);

        // add for bug TDQ-1580
        setOrignalName(property.getDisplayName());
        setOrignalDescription(property.getDescription());
        setOrignalpurpose(property.getPurpose());
        setOrignalStatus(property.getStatusCode());
        setOrignalversion(property.getVersion());

        super.createControl(container);

        // Original Name
        Label originalNameLab = new Label(container, SWT.NONE);
        originalNameLab.setText(Messages.getString("DocumentationPage.originalNameLabText.orgName")); //$NON-NLS-1$

        originalFilenameText = new Text(container, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = layout.numColumns - 1;
        originalFilenameText.setLayoutData(data);
        originalFilenameText.setEditable(false);
        originalFilenameText.setEnabled(false);

        // option
        Group option = new Group(container, SWT.NONE);
        option.setText(Messages.getString("DocumentationPage.option")); //$NON-NLS-1$
        layout = new GridLayout();
        option.setLayout(layout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        option.setLayoutData(data);
        // check link
        checkLinkBtn = new Button(option, SWT.CHECK);
        checkLinkBtn.setText(Messages.getString("DocumentationPage.LinkLabel")); //$NON-NLS-1$
        evaluateCheckLinkDoc();

        browseBtn = new Button(filenameContainer, SWT.PUSH);
        switchCheck(false);
        setControl(container);
        updateContent();
        evaluateFields();
        addListeners();
        updatePageComplete();
        setPageComplete(false);
    }

    private void evaluateCheckLinkDoc() {
        if (isUpdate() && checkLinkBtn != null && !checkLinkBtn.isDisposed()) {
            checkLinkBtn.setEnabled(false);
            if (LinkUtils.isLinkDocumentationItem(documentationItem)) {
                checkLinkBtn.setSelection(false);
            } else {
                checkLinkBtn.setSelection(true);
            }
        } else {
            checkLinkBtn.setSelection(true); // by default
            // set the default
            documentationItem = PropertiesFactory.eINSTANCE.createDocumentationItem();
            documentationItem.setProperty(property);
        }
    }

    protected void evaluateFields() {
        super.evaluateFields();
        enabledLinkMode(filenameText.getText());
        evaluateFileNameField();
    }

    protected void evaluateFileNameField() {

        IPath filePath = null;
        if (filenameText.getText().length() == 0) {
            if (isUpdate()) {
                filenameStatus = createOkStatus();
            } else {
                filenameStatus = createStatus(IStatus.ERROR, Messages.getString("DocumentationPage.sourceDocIsNotSet")); //$NON-NLS-1$
            }
        } else {
            if (LinkUtils.isRemoteFile(filenameText.getText())) {
                switchCheck(true);
            } else { // local file
                switchCheck(false);
                filePath = new Path(filenameText.getText());
                if (!LinkUtils.existedFile(filePath)) {
                    filenameStatus = createStatus(IStatus.ERROR, Messages.getString("DocumentationPage.sourceDocDoNotExist")); //$NON-NLS-1$
                    filePath = null;
                } else {
                    filenameStatus = createOkStatus();
                }
            }
        }

        getDocumentation().setDocFilePath(filePath);

        // if (nameText == null || nameText.getText().length() == 0) {
        if (!isNameModifiedByUser() && !isUpdate()) {
            property.setLabel(nameText.getText());
        }
        originalFilenameText.setText(getDocumentation().getDocOriginalName());
        // if (nameText.getText().compareTo("") == 0) {
        if (!isNameModifiedByUser() && !isUpdate()) {
            nameText.setText(checkSpaceName(getDocumentation().getDocOriginalName()));
            if (!isUpdate()) {
                setNameModifiedByUser(false);
            }
        }

        updatePageStatus();
    }

    protected void addListeners() {
        super.addListeners();
        checkLinkBtn.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }

            public void widgetSelected(SelectionEvent e) {
                evaluateFields();
                testRemoteFile();
            }
        });
        filenameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                evaluateFields();
            }
        });
        filenameText.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                //
            }

            public void focusLost(FocusEvent e) {
                enabledLinkMode(filenameText.getText());
                testRemoteFile();

            }
        });
        browseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (LinkUtils.isRemoteFile(filenameText.getText())) {
                    testRemoteFile();
                } else {
                    FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
                    String filename = dlg.open();
                    if (filename != null) {
                        filenameText.setText(filename);
                        evaluateFileNameField();
                    }
                }
            }
        });
    }

    protected void updateContent() {
        super.updateContent();
        originalFilenameText.setText(StringUtils.trimToEmpty(getDocumentation().getDocOriginalName()));
        if (isUpdate() && LinkUtils.isLinkDocumentationItem(documentationItem)) {
            LinkType link = ((LinkDocumentationItem) documentationItem).getLink();
            if (link != null) {
                filenameText.setText(StringUtils.trimToEmpty(link.getURI()));
                testRemoteFile();
            }
        }
    }

    private IDocumentationContext getDocumentation() {
        return (IDocumentationContext) getWizard();
    }

    protected IStatus[] getStatuses() {
        IStatus[] toReturn = super.getStatuses();
        return (IStatus[]) ArrayUtils.addAll(toReturn, new IStatus[] { filenameStatus });
    }

    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.DOCUMENTATION;
    }

    /**
     * 
     * ggu Comment method "checkSpaceName".
     * 
     * replace the "space" character
     */
    private String checkSpaceName(final String name) {
        if (name == null) {
            return ""; //$NON-NLS-1$
        }
        return name.trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    public boolean isLinkDocumentation() {
        if (checkLinkBtn != null && !checkLinkBtn.isDisposed()) {
            return !checkLinkBtn.getSelection();
        }
        return false;
    }

    private void switchCheck(boolean link) {
        if (link) {
            browseBtn.setText(Messages.getString("DocumentationPage.checkLabel")); //$NON-NLS-1$
            browseBtn.setToolTipText(Messages.getString("DocumentationPage.checkTipText")); //$NON-NLS-1$
        } else {
            browseBtn.setText(Messages.getString("DocumentationPage.browseBtnText.browse")); //$NON-NLS-1$
            browseBtn.setToolTipText(""); //$NON-NLS-1$
        }
    }

    private void testRemoteFile() {
        if (LinkUtils.isRemoteFile(filenameText.getText())) {

            LinkInfo info = LinkUtils.testRemoteFile(filenameText.getText());

            switch (info) {
            case FILE_NOT_FOUND:
            case URL_ERROR:
            case NET_ERROR:
                filenameStatus = createStatus(IStatus.ERROR, Messages.getString("DocumentationPage.sourceDocDoNotExist")); //$NON-NLS-1$
                break;
            case LINK_OK:
                filenameStatus = createOkStatus();
                break;
            default:
                break;
            }

            getDocumentation().setDocFilePath(new Path(filenameText.getText().trim()));
            updatePageStatus();
            if (!isUpdate()) {
                nameText.setText(checkSpaceName(getDocumentation().getDocOriginalName()));
            }

        }
    }

    private void switchLinkMode() {
        if (isLinkDocumentation()) {
            documentationItem = PropertiesFactory.eINSTANCE.createLinkDocumentationItem();
        } else {
            documentationItem = PropertiesFactory.eINSTANCE.createDocumentationItem();
        }
        documentationItem.setProperty(property);
    }

    /**
     * 
     * ggu Comment method "enabledLinkMode".
     * 
     * auto switch to the link mode
     */
    private void enabledLinkMode(final String uri) {
        if (uri == null) {
            return;
        }
        if (!isUpdate() && checkLinkBtn != null && !checkLinkBtn.isDisposed()) {
            if (LinkUtils.checkLinkFile(uri)) {
                checkLinkBtn.setEnabled(false);
                checkLinkBtn.setSelection(false);
            } else {
                checkLinkBtn.setEnabled(true);
            }
            switchLinkMode();
        }
    }

    public String getOrignalName() {
        return orignalName;
    }

    public void setOrignalName(String orignalName) {
        this.orignalName = orignalName;
    }

    public String getOrignalDescription() {
        return orignalDescription;
    }

    public void setOrignalDescription(String orignalDescription) {
        this.orignalDescription = orignalDescription;
    }

    public String getOrignalversion() {
        return orignalversion;
    }

    public void setOrignalversion(String orignalversion) {
        this.orignalversion = orignalversion;
    }

    public String getOrignalStatus() {
        return orignalStatus;
    }

    public void setOrignalStatus(String orignalStatus) {
        this.orignalStatus = orignalStatus;
    }

    public void setOrignalpurpose(String orignalpurpose) {
        this.orignalpurpose = orignalpurpose;
    }

    public String getOrignalpurpose() {
        return orignalpurpose;
    }

}
