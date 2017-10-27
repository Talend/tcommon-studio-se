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
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * ggu class global comment. Detailled comment
 */
public abstract class AbstractLDAPSchemaStepForm extends AbstractForm {

    protected MetadataTable metadataTable;

    protected LDAPSchemaConnection connection;

    private WizardPage page = null;

    private IMetadataContextModeManager contextModeManager;

    public AbstractLDAPSchemaStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
        this.connection = getConnection();
        this.metadataTable = metadataTable;
    }

    protected LDAPSchemaConnection getConnection() {
        return (LDAPSchemaConnection) connectionItem.getConnection();
    }

    protected LDAPSchemaConnection getOriginalValueConnection() {
        if (isContextMode() && getContextModeManager() != null) {
            return (LDAPSchemaConnection) OtherConnectionContextUtils.cloneOriginalValueLDAPSchemaConnection(getConnection(),
                    getContextModeManager().getSelectedContextType());
        }
        return getConnection();

    }

    protected void collectContextParams(boolean visible) {
        clearContextParams();
        addContextParams(EParamName.Host, visible);
        addContextParams(EParamName.Port, visible);
        addContextParams(EParamName.BindPrincipal, visible);
        addContextParams(EParamName.BindPassword, visible);

        addContextParams(EParamName.CountLimit, visible);
        addContextParams(EParamName.TimeOutLimit, visible);
        addContextParams(EParamName.Filter, visible);
        addContextParams(EParamName.BaseDN, visible);
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
