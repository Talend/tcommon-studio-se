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
package org.talend.metadata.managment.ui.wizard.metadata.connection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.repository.model.RepositoryConstants;

public class Step0WizardPage extends PropertiesWizardPage {

    private ERepositoryObjectType type;

    public Step0WizardPage(Property property, IPath destinationPath, ERepositoryObjectType type, boolean readOnly,
            boolean editPath) {
        super("WizardPage", property, destinationPath, readOnly, editPath); //$NON-NLS-1$
        this.type = type;

        setTitle(Messages.getString("Step0WizardPage.title2")); //$NON-NLS-1$
        setDescription(Messages.getString("Step0WizardPage.description2")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        super.createControl(container);

        setControl(container);
        updateContent();
        addListeners();

        updatePageComplete();

    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return type;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            this.nameText.setFocus();
        }
    }

    @Override
    protected String getPropertyLabel(String name) {
        String label = name;
        for (String toReplace : RepositoryConstants.ITEM_FORBIDDEN_IN_LABEL) {
            label = label.replace(toReplace, "_"); //$NON-NLS-1$
        }
        return label;
    }
}
