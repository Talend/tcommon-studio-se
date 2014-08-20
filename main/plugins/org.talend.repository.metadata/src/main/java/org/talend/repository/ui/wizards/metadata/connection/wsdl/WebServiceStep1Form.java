// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.service.IWebServiceTos;
import org.talend.repository.ui.swt.utils.AbstractWSDLSchemaStepForm;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class WebServiceStep1Form extends AbstractWSDLSchemaStepForm {

    private IWebServiceTos webService;

    private ConnectionItem connectionItem;

    private IMetadataContextModeManager contextModeManager;

    /**
     * DOC Administrator WebServiceStep1Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param existingNames
     */
    public WebServiceStep1Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames, IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, metadataTable, existingNames);
        this.connectionItem = connectionItem;
        this.contextModeManager = contextModeManager;
        // List<? extends IElementParameter> parameters = ComponentsFactoryProvider.getInstance().get("tWebService")
        // .createElementParameters(connector);
        // connector.setElementParameters(parameters);
        setConnectionItem(connectionItem);
        setContextModeManager(contextModeManager);

        setupForm(true);
        // initWebserviceUI();
        // initInputMetaCopy();
        // initOutputMetaCopy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }

    @Override
    protected void adaptFormToEditable() {
        webService.getWSDLLabel(isContextMode()).setReadOnly(isContextMode());
        webService.getWSDLLabel(isContextMode()).setText(((WSDLSchemaConnection) connectionItem.getConnection()).getWSDL());

        super.adaptFormToEditable();
    }

    private void addExportContextButton() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        // TODO Auto-generated method stub
        // IComponent connector = ComponentsFactoryProvider.getInstance().get("tWebService");
        Composite panel = new Composite(this, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 7;
        layout.marginWidth = 7;
        layout.verticalSpacing = 4;
        layout.horizontalSpacing = 4;
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IWebServiceTos.class)) {
            webService = (IWebServiceTos) GlobalServiceRegister.getDefault().getService(IWebServiceTos.class);
            webService.getWebServiceUI(panel, connectionItem);
            webService.getTable().addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    checkFieldsValue();
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        if (getCurrentFunction() == false) {
            updateStatus(IStatus.ERROR, "Please Select a Operation!");
            return false;
        } else {
            updateStatus(IStatus.OK, null);
            return true;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        checkFieldsValue();
    }

    public void updateStatus() {
        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        // TODO Auto-generated method stub

    }

    public Boolean getCurrentFunction() {
        return webService.getCurrentFunction();
    }

    @Override
    protected void exportAsContext() {
        super.exportAsContext();
        if (getContextModeManager() != null) {
            getContextModeManager().setDefaultContextType(getConnection());
        }
    }
}
