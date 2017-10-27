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
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractDelimitedFileStepForm.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public abstract class AbstractDelimitedFileStepForm extends AbstractFileStepForm {

    private WizardPage page = null;

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    /**
     * DOC ocarbone AbstractDelimitedFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractDelimitedFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    protected DelimitedFileConnection getConnection() {
        return (DelimitedFileConnection) super.getConnection();
    }

    /**
     * Getter for page.
     * 
     * @return the page
     */
    public WizardPage getWizardPage() {
        return this.page;
    }

    /**
     * Sets the page.
     * 
     * @param page the page to set
     */
    public void setWizardPage(WizardPage page) {
        this.page = page;
    }
}
