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
package org.talend.core.repository.link;

import org.eclipse.ui.IEditorInput;
import org.talend.repository.model.RepositoryNode;

public interface IRepoViewLinker {

    RepositoryNode getRelationNode(IEditorInput editorInput);

    boolean isRelation(IEditorInput editorInput, String repoNodeId);
}
