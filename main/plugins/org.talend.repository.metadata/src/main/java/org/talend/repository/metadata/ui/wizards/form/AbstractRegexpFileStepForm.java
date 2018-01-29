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

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.properties.ConnectionItem;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractRegexpFileStepForm.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public abstract class AbstractRegexpFileStepForm extends AbstractFileStepForm {

    /**
     * DOC tguiu AbstractRegexpFileStepForm constructor comment. Use to step1
     */
    public AbstractRegexpFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    /**
     * DOC ocarbone AbstractRegexpFileStepForm constructor comment. Use to step2
     * 
     * @param parent
     * @param connection2
     */
    public AbstractRegexpFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractRegexpFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    protected RegexpFileConnection getConnection() {
        return (RegexpFileConnection) super.getConnection();
    }

}
