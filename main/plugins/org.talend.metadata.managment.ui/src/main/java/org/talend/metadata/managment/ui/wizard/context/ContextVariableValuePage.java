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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.metadata.managment.ui.i18n.Messages;

public class ContextVariableValuePage extends WizardPage {

    public static final int CONTEXT_COLUMN_WIDTH = 200;

    private IContextManager contextManager;

    private ContextRepositoryComposite reuseContextComp;

    public int step;

    public ContextVariableValuePage(IContextManager contextManager, int step) {
        super(Messages.getString("ReuseRepositoryContext.name")); //$NON-NLS-1$
        setTitle(Messages.getString("ContextVariableValuePage.title")); //$NON-NLS-1$
        setMessage(Messages.getString("ContextVariableValuePage.title")); //$NON-NLS-1$
        this.contextManager = contextManager;
        this.step = step;
    }

    @Override
    public void createControl(Composite parent) {
        initializeUI(parent);
    }

    private void initializeUI(Composite parent) {
        reuseContextComp = new ContextRepositoryComposite(parent, contextManager);
        reuseContextComp.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 120;
        gridData.widthHint = 100;
        reuseContextComp.setLayoutData(gridData);
        reuseContextComp.setReadOnly(false);
        reuseContextComp.refresh();
        setControl(reuseContextComp);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initContextVariables();
            refresh();
        }
    }

    private void initContextVariables() {
        ContextModeWizard currentWizard = null;
        if (getWizard() instanceof ContextModeWizard) {
            currentWizard = (ContextModeWizard) getWizard();
        }
        if (currentWizard != null) {
            initContextMode(currentWizard.getResueContextManager(), currentWizard.getAdaptModels());
        }
    }

    private void initContextMode(IContextManager contextManger, List<ConectionAdaptContextVariableModel> finalModels) {
        this.contextManager.getListContext().clear();
        for (IContext context : contextManger.getListContext()) {
            this.contextManager.getListContext().add(context);
        }
        this.contextManager.setDefaultContext(contextManger.getDefaultContext());
    }

    private void refresh() {
        if (this.getControl() instanceof ContextRepositoryComposite) {
            ContextRepositoryComposite currentComposite = (ContextRepositoryComposite) this.getControl();
            currentComposite.refresh();
        }
    }
}
