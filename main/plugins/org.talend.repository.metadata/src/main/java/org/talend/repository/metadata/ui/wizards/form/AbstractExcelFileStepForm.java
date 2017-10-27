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
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;

/**
 * 
 * DOC yexiaowei class global comment. Detailled comment
 */
public abstract class AbstractExcelFileStepForm extends AbstractFileStepForm {

    public AbstractExcelFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    public AbstractExcelFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC cantoine AbstractLdifFileStepForm constructor comment. Use to step1
     */
    public AbstractExcelFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, connectionItem, existingNames);
    }

    protected FileExcelConnection getConnection() {
        return (FileExcelConnection) super.getConnection();
    }

    public boolean isPerlProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.PERL);
    }

}
