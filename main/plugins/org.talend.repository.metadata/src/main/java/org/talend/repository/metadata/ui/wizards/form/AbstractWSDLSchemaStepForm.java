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
package org.talend.repository.metadata.ui.wizards.form;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractWSDLSchemaStepForm extends AbstractForm {

    protected MetadataTable metadataTable;

    protected WSDLSchemaConnection connection;

    private WizardPage page = null;

    private IMetadataContextModeManager contextModeManager;

    public AbstractWSDLSchemaStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
        this.connection = getConnection();
        this.metadataTable = metadataTable;
    }

    protected WSDLSchemaConnection getConnection() {
        return (WSDLSchemaConnection) connectionItem.getConnection();
    }

    protected WSDLSchemaConnection getOriginalValueConnection() {
        if (isContextMode() && getContextModeManager() != null) {
            return OtherConnectionContextUtils.cloneOriginalValueWSDLSchemaConnection(getConnection(), getContextModeManager()
                    .getSelectedContextType());
        }
        return getConnection();

    }

    protected void collectContextParams(boolean visible) {
        clearContextParams();
        addContextParams(EParamName.WSDL, visible);
        if (getConnection().isIsInputModel()) {
            addContextParams(EParamName.MethodName, visible);
            addContextParams(EParamName.UserName, visible);
            addContextParams(EParamName.Password, visible);
            addContextParams(EParamName.ProxyHost, visible);
            addContextParams(EParamName.ProxyPort, visible);
            addContextParams(EParamName.ProxyUser, visible);
            addContextParams(EParamName.ProxyPassword, visible);
        }
        // addContextParams(EParamName.EndpointURI, visible);
        // addContextParams(EParamName.Encoding, visible);
    }

    public WizardPage getWizardPage() {
        return this.page;
    }

    public void setWizardPage(WizardPage page) {
        this.page = page;
    }

    public IMetadataContextModeManager getContextModeManager() {
        return this.contextModeManager;
    }

    public void setContextModeManager(IMetadataContextModeManager contextModeManager) {
        this.contextModeManager = contextModeManager;
    }
}
