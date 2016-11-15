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

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.talend.repository.model.RepositoryNode;

public abstract class AbstractFileEditorInputLinker extends AbstractEditorInputWithItemIdLinker {

    @Override
    public RepositoryNode getRelationNode(IEditorInput editorInput) {
        if (editorInput != null && editorInput instanceof IFileEditorInput) {
        	IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            final IFile file = fileEditorInput.getFile();
            if (checkFileExtension(file)) {
                return getRepoNodeFromEditor(getEditor(fileEditorInput));
            }
        }
        return null;
    }

    protected boolean checkFileExtension(IFile file) {
        return true; // ignore by default
    }

}
