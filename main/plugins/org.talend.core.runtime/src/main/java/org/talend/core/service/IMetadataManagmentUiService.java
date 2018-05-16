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
package org.talend.core.service;

import org.eclipse.core.runtime.Path;
import org.talend.core.IService;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.RepositoryNode;

/**
 * ggu class global comment. Detailled comment
 */
public interface IMetadataManagmentUiService extends IService {

    public String getAndOpenXSDFileDialog(Path initPath);

    public ContextType getContextTypeForContextMode(Connection connection);

    public ContextType getContextTypeForContextMode(Connection connection, String selectedContext, boolean defaultContext);

    public String getOriginalValue(ContextType contextType, final String value);

    public void openRepositoryContextWizard(RepositoryNode repositoryNode);

    public boolean isContextMode(Connection connection, String value);

}
