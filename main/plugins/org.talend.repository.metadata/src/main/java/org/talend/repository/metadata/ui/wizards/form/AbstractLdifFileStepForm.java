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
package org.talend.repository.metadata.ui.wizards.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractLdifFileStepForm.java 302 2006-11-02 13:59:32 +0000 (jeu., 02 nov. 2006) cantoine $
 * 
 */
public abstract class AbstractLdifFileStepForm extends AbstractForm {

    protected int maximumRowsToPreview = RepositoryManagerHelper.getMaximumRowsToPreview();

    protected LdifFileConnection connection;

    private IMetadataContextModeManager contextModeManager;

    /**
     * DOC cantoine AbstractLdifFileStepForm constructor comment. Use to step1
     */
    public AbstractLdifFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
    }

    /**
     * DOC cantoine AbstractLdifFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractLdifFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC cantoine AbstractLdifFileStepForm constructor comment. Use to step1
     */
    public AbstractLdifFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        setConnectionItem(connectionItem);
    }

    protected LdifFileConnection getConnection() {
        return (LdifFileConnection) connectionItem.getConnection();
    }

    public IMetadataContextModeManager getContextModeManager() {
        return this.contextModeManager;
    }

    public void setContextModeManager(IMetadataContextModeManager contextModeManager) {
        this.contextModeManager = contextModeManager;
    }

    protected void collectParameters(boolean visible) {
        clearContextParams();
        addContextParams(EParamName.FilePath, visible);
    }
}
