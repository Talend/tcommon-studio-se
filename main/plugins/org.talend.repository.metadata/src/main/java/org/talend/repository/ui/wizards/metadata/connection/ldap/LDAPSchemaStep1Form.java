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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.EEncryptionMethod;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractLDAPSchemaStepForm;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaStep1Form extends AbstractLDAPSchemaStepForm {

    // /** The connection name text widget */
    // private Text nameText;

    /** The host name combo with the history of recently used host names */
    private Combo hostCombo;

    /** The host combo with the history of recently used ports */
    private Combo portCombo;

    /** The combo to select the encryption method */
    private Combo encryptionMethodCombo;

    /** The button to check the connection parameters */
    private Button checkConnectionButton;

    /**
     * LDAPSchemaStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param tableNames
     */
    public LDAPSchemaStep1Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, String[] tableNames,
            IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, metadataTable, tableNames);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Composite composite = BaseWidgetUtils.createColumnContainer(this, 1, 1);

        // Composite nameComposite = BaseWidgetUtils.createColumnContainer(composite, 2, 1);
        // BaseWidgetUtils.createLabel(nameComposite, "Connection name:", 1);
        // nameText = BaseWidgetUtils.createText(nameComposite, "", 1);

        BaseWidgetUtils.createSpacer(composite, 1);

        Group group = BaseWidgetUtils.createGroup(composite, Messages.getString("LDAPSchemaStep1Form.NetWorkParameter"), 1); //$NON-NLS-1$

        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group, 3, 1);
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("LDAPSchemaStep1Form.HostName"), 1); //$NON-NLS-1$
        String[] hostHistory = new String[] {};
        hostCombo = BaseWidgetUtils.createCombo(groupComposite, hostHistory, -1, 2);

        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("LDAPSchemaStep1Form.Port"), 1); //$NON-NLS-1$
        String[] portHistory = new String[] {};
        portCombo = BaseWidgetUtils.createCombo(groupComposite, portHistory, -1, 2);

        String[] encMethods = new String[] { EEncryptionMethod.NO_ENCRYPTION_METHOD.getName(),
                EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName(), EEncryptionMethod.STARTTSL_EXTENSION_METHOD.getName() };
        int index = 0;
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("LDAPSchemaStep1Form.EncryptionMethod"), 1); //$NON-NLS-1$
        encryptionMethodCombo = BaseWidgetUtils.createReadonlyCombo(groupComposite, encMethods, index, 2);
        // BaseWidgetUtils.createSpacer(groupComposite, 1);
        BaseWidgetUtils.createLabel(groupComposite, Messages.getString("LDAPSchemaStep1Form.CheckConnection"), //$NON-NLS-1$
                2);

        BaseWidgetUtils.createSpacer(groupComposite, 2);
        checkConnectionButton = new Button(groupComposite, SWT.PUSH);
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.verticalAlignment = SWT.BOTTOM;
        checkConnectionButton.setLayoutData(gd);
        checkConnectionButton.setText(Messages.getString("LDAPSchemaStep1Form.CheckNetWorkParameter")); //$NON-NLS-1$

        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        hostCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    connection.setHost(hostCombo.getText().trim());
                }

            }
        });

        portCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!isContextMode()) {
                    checkFieldsValue();
                    connection.setPort(portCombo.getText().trim());
                }
            }
        });

        encryptionMethodCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                checkFieldsValue();

                connection.setEncryptionMethodName(encryptionMethodCombo.getText().trim());
            }

        });

        checkConnectionButton.addSelectionListener(new SelectionAdapter() {

            boolean isOK = false;

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (isContextMode() && getContextModeManager() != null) {
                    ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(getShell(),
                            connectionItem.getConnection());
                    getContextModeManager().setSelectedContextType(contextTypeForContextMode);
                }
                try {
                    IRunnableWithProgress op = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) {
                            isOK = LDAPConnectionUtils.checkParam(getOriginalValueConnection(), true);
                        }
                    };
                    new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, op);
                } catch (InvocationTargetException e) {
                    MessageBoxExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    MessageBoxExceptionHandler.process(e);
                }

                if (isOK) {
                    if (!isContextMode()) {
                        saveDialogSettings();
                    }
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep1Form.CheckNetWorkParameter"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep1Form.connnectionSuccessed")); //$NON-NLS-1$
                    updateStatus(IStatus.OK, null);
                } else {
                    MessageDialog.openError(Display.getDefault().getActiveShell(),
                            Messages.getString("LDAPSchemaStep1Form.CheckNetWorkParameter"), //$NON-NLS-1$
                            Messages.getString("LDAPSchemaStep1Form.connectionFailed")); //$NON-NLS-1$
                    updateStatus(IStatus.ERROR, null);
                }
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
        //

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        String host = null;
        String port = null;
        if (isContextMode()) {
            ContextType contextType = getContextModeManager().getSelectedContextType();
            EList contextParameters = contextType.getContextParameter();
            for (Object obj : contextParameters) {
                if (obj instanceof ContextParameterType) {
                    ContextParameterType contextParameterType = (ContextParameterType) obj;
                    if (connection.getHost().endsWith(contextParameterType.getName())) {
                        host = contextParameterType.getValue();
                        continue;
                    }
                    if (connection.getPort().endsWith(contextParameterType.getName())) {
                        port = contextParameterType.getValue();
                        continue;
                    }
                }
            }
        } else {
            host = hostCombo.getText();
            port = portCombo.getText();
        }

        if (host == null || host.equals("")) { //$NON-NLS-1$
            this.checkConnectionButton.setEnabled(false);
            updateStatus(IStatus.ERROR, "Host name must be specified."); //$NON-NLS-1$
            return false;
        } else {
            if (port == null || port.equals("")) { //$NON-NLS-1$
                this.checkConnectionButton.setEnabled(false);
                updateStatus(IStatus.ERROR, "Port must be specified."); //$NON-NLS-1$
                return false;
            } else if (!port.matches("[0-9]*") || port.length() > 6) { //$NON-NLS-1$
                this.checkConnectionButton.setEnabled(false);
                updateStatus(IStatus.ERROR, "Port must be number and max length is 6."); //$NON-NLS-1$
                return false;
            } else {
                this.checkConnectionButton.setEnabled(true);
                updateStatus(IStatus.ERROR, null);
                return true;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        LDAPSchemaConnection connection = (LDAPSchemaConnection) this.connectionItem.getConnection();
        String host = connection.getHost();
        this.hostCombo.setItems(HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_HOST_HISTORY));
        this.hostCombo.setText(host == null ? "" : host); //$NON-NLS-1$
        String port = connection.getPort();
        this.portCombo.setItems(HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_PORT_HISTORY));
        this.portCombo.setText(port == null ? "" : port); //$NON-NLS-1$
        String encryptionMethodName = connection.getEncryptionMethodName();
        this.encryptionMethodCombo.setText(encryptionMethodName == null ? "" : encryptionMethodName); //$NON-NLS-1$

        if (connection.getEncryptionMethodName() == null) {
            connection.setEncryptionMethodName(EEncryptionMethod.NO_ENCRYPTION_METHOD.getName());
        } else {
            updateStatus(IStatus.OK, null);
        }

        this.checkConnectionButton.setEnabled(false);
        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        hostCombo.setEnabled(!isReadOnly());
        portCombo.setEnabled(!isReadOnly());
        encryptionMethodCombo.setEnabled(!isReadOnly());
        updateStatus(IStatus.OK, ""); //$NON-NLS-1$

    }

    /**
     * Storing the history of combo.
     */
    public void saveDialogSettings() {
        HistoryUtils.save(ConnectionUIConstants.DIALOGSETTING_KEY_HOST_HISTORY, hostCombo.getText());
        HistoryUtils.save(ConnectionUIConstants.DIALOGSETTING_KEY_PORT_HISTORY, portCombo.getText());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        adaptFormToReadOnly();
        if (visible) {
            initialize();
            adaptFormToEditable();
            checkFieldsValue();
        }

    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        hostCombo.setEnabled(!isContextMode());
        portCombo.setEnabled(!isContextMode());
    }

}
