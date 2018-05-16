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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.IContextManager;

/**
 * Wizard page collecting informations to create a new IDocumentation. <br/>
 * 
 * $Id: DocumentationPage.java 1935 2007-02-09 05:58:57 +0000 (ven., 09 févr. 2007) bqian $
 * 
 */
public class ContextPage extends WizardPage {

    IContextManager contextManager;

    boolean readOnly;

    protected ContextPage(String pageName, IContextManager contextManager, boolean readOnly) {
        super(pageName);
        this.contextManager = contextManager;
        this.readOnly = readOnly;
    }

    public void createControl(Composite parent) {
        this.setControl(new ContextForm(parent, SWT.NONE, null, contextManager, readOnly));
    }

}
