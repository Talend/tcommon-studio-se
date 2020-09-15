// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.mdm.ui.wizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.designerproperties.MDMVersions;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.mdm.AbsMdmConnectionHelper;
import org.talend.metadata.managment.mdm.S60MdmConnectionHelper;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.mdm.i18n.Messages;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMForm extends AbstractForm {

    // control fields
    private LabelledText mdmUsernameText;

    private LabelledText mdmPasswordText;

    private LabelledText serverURLText;

    private UtilsButton checkButton;

    // other variables
    private boolean readOnly;

    private boolean verified = false;

    private MDMWizardPage page;

    private Object stub;

    private boolean creation;

    private static final String SERVER_RUL_S56 = "http://localhost:8180/talend/TalendPort";

    private static final String SERVER_RUL_S60 = "http://localhost:8180/talendmdm/services/soap";

    /**
     * DOC hwang MDMForm constructor comment.
     *
     * @param parent
     * @param style
     * @param existingNames
     */
    protected MDMForm(Composite parent, ConnectionItem connectionItem, String[] existingNames, MDMWizardPage page,
            boolean creation) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.page = page;
        this.creation = creation;
        setConnectionItem(connectionItem); // must be first.
        setupForm(true);
        layoutForm();
    }

    private void layoutForm() {
        GridLayout layout = (GridLayout) getLayout();
        layout.marginHeight = 0;
        setLayout(layout);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        mdmUsernameText.setReadOnly(readOnly);
        mdmPasswordText.setReadOnly(readOnly);
        serverURLText.setReadOnly(readOnly);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        Group mdmParameterGroup = new Group(this, SWT.NULL);
        mdmParameterGroup.setText(Messages.getString("MDMForm_link_para")); //$NON-NLS-1$
        GridLayout layoutGroup = new GridLayout();
        layoutGroup.numColumns = 2;
        mdmParameterGroup.setLayout(layoutGroup);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        mdmParameterGroup.setLayoutData(gridData);

        mdmUsernameText = new LabelledText(mdmParameterGroup, Messages.getString("MDMForm_userName"), true); //$NON-NLS-1$
        mdmPasswordText = new LabelledText(mdmParameterGroup, Messages.getString("MDMForm_pass"), 1, SWT.BORDER | SWT.PASSWORD); //$NON-NLS-1$
        serverURLText = new LabelledText(mdmParameterGroup, Messages.getString("MDMForm_server_url"), true); //$NON-NLS-1$

        addCheckButton(mdmParameterGroup);
        checkFieldsValue();
        if (!verified) {
            page.setPageComplete(false);
        }
    }

    private void addCheckButton(Composite parent) {
        Composite compositeCheckButton = Form.startNewGridLayout(parent, 1, false, SWT.CENTER, SWT.TOP);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.CENTER;
        compositeCheckButton.setLayoutData(data);

        GridLayout layout2 = (GridLayout) compositeCheckButton.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        checkButton = new UtilsButton(compositeCheckButton, Messages.getString("MDMForm_check"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        checkButton.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        mdmUsernameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setUsername(mdmUsernameText.getText());
                checkFieldsValue();
            }

        });
        mdmPasswordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                MDMConnection conn = getConnection();
                conn.setPassword(conn.getValue(mdmPasswordText.getText(), true));
                checkFieldsValue();
            }

        });
        serverURLText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setServerUrl(serverURLText.getText());
                checkFieldsValue();
            }

        });

    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        checkButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             *
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                checkMDMConnection();
            }

        });

    }

    private void checkMDMConnection() {
        String username = mdmUsernameText.getText();
        String password = mdmPasswordText.getText();
        String serverUrl = serverURLText.getText();
        if (username == null || password == null || serverUrl == null) {
            page.setPageComplete(false);
            return;
        }
        if (isContextMode()) {
            username = ConnectionContextHelper.getParamValueOffContext(getConnection(), username);
            password = ConnectionContextHelper.getParamValueOffContext(getConnection(), password);
            serverUrl = ConnectionContextHelper.getParamValueOffContext(getConnection(), serverUrl);
        }

        AbsMdmConnectionHelper helper = null;
        helper = new S60MdmConnectionHelper();

        try {
            // SSLContext sslcontext = StudioSSLContextProvider.getContext();
            // HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());

            stub = helper.checkConnection(serverUrl, null, username, password);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            stub = null;
        }
        if (stub != null) {
            page.setPageComplete(true);
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.getString("MDMForm_success"),
                    Messages.getString("MDMForm_connect_successful"));
        } else {
            String titleMsg = Messages.getString("MDMForm_error_message");
            String mainMsg = Messages.getString("MDMForm_connection_failure");
            MessageDialog.openError(getShell(), titleMsg, mainMsg);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        if (mdmUsernameText.getCharCount() == 0) {
            updateStatus(IStatus.ERROR, Messages.getString("MDMForm_username_null")); //$NON-NLS-1$
            checkButton.setEnabled(false);
            return false;
        }

        if (mdmPasswordText.getCharCount() == 0) {
            updateStatus(IStatus.ERROR, Messages.getString("MDMForm_pass_null")); //$NON-NLS-1$
            checkButton.setEnabled(false);
            return false;
        }

        if (serverURLText.getCharCount() == 0) {
            updateStatus(IStatus.ERROR, Messages.getString("MDMForm_server_null")); //$NON-NLS-1$
            checkButton.setEnabled(false);
            return false;
        }

        checkButton.setEnabled(true);
        updateStatus(IStatus.OK, null);
        if (!verified) {
            page.setPageComplete(false);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        MDMConnection mdmConn = getConnection();
        if (isContextMode()) {
            adaptFormToEditable();
            mdmUsernameText.setText(mdmConn.getUsername());
            mdmPasswordText.setText(mdmConn.getPassword());
            serverURLText.setText(mdmConn.getServerUrl());
        } else {
            mdmConn.setVersion(MDMVersions.MDM_S60.getKey());
            mdmConn.setServerUrl(SERVER_RUL_S60);
            mdmUsernameText.setText(mdmConn.getUsername());
            mdmPasswordText.setText(mdmConn.getValue(mdmConn.getPassword(), false));
            serverURLText.setText(mdmConn.getServerUrl());
        }
    }

    protected MDMConnection getConnection() {
        return (MDMConnection) connectionItem.getConnection();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (isContextMode()) {
            adaptFormToEditable();
        }

        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }

        checkFieldsValue();
    }

    public Object getXtentisBindingStub() {
        return this.stub;
    }

    public String getUserName() {
        return mdmUsernameText.getText();
    }

    public String getPassword() {
        return mdmPasswordText.getText();
    }

    @Override
    protected void exportAsContext() {
        collectConnParams();
        super.exportAsContext();
    }

    protected void collectConnParams() {
        addContextParams(EParamName.MDM_URL, true);
        addContextParams(EParamName.MDM_Username, true);
        addContextParams(EParamName.MDM_Password, true);
    }

}
