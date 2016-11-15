// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class MdmStep0WizardPage extends Step0WizardPage {

    public MdmStep0WizardPage(Property property, IPath destinationPath, ERepositoryObjectType type, boolean readOnly,
            boolean editPath) {
        super(property, destinationPath, type, readOnly, editPath);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((MDMWizard) getWizard()).setCurrentPage(this);
        }
    }

}
