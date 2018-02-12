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
package org.talend.repository.mdm.ui.wizard;

import org.eclipse.core.runtime.IPath;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;

/**
 * DOC talend class global comment. Detailled comment
 */
public class MdmStep0WizardPage extends Step0WizardPage {

    /**
     * DOC talend MdmStep0WizardPage constructor comment.
     * 
     * @param property
     * @param destinationPath
     * @param type
     * @param readOnly
     * @param editPath
     */
    public MdmStep0WizardPage(Property property, IPath destinationPath, ERepositoryObjectType type, boolean readOnly,
            boolean editPath) {
        super(property, destinationPath, type, readOnly, editPath);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((MDMWizard) getWizard()).setCurrentPage(this);
        }
    }

}
