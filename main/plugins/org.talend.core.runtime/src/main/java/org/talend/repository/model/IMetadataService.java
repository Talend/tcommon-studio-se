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
package org.talend.repository.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.core.IService;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.metadata.celleditor.EProcessTypeForRule;
import org.talend.core.ui.rule.AbstractRlueOperationChoice;

/**
 * DOC hwang class global comment. Detailled comment
 */
public interface IMetadataService extends IService {

    public WizardDialog getGenericSchemaWizardDialog(Shell shell, IWorkbench workbench, boolean creation, ISelection selection,
            String[] existingNames, boolean isSinglePageOnly);

    public Property getPropertyFromWizardDialog();

    public IPath getPathForSaveAsGenericSchema();

    public ConnectionItem openMetadataConnection(boolean creation, IRepositoryNode repoNode, INode node);

    public void openMetadataConnection(IRepositoryViewObject o, INode node);

    public void openEditSchemaWizard(String value);

    public void runCreateTableAction(RepositoryNode metadataNode);

    public AbstractRlueOperationChoice getOperationChoice(Shell shell, INode node, RulesItem[] repositoryRuleItems,
            LinkRulesItem[] linkRuleItems, EProcessTypeForRule rule, String ruleToEdit, boolean readOnly);

    public DatabaseMetaData findCustomizedJTDSDBMetadata(Connection conn);
}
