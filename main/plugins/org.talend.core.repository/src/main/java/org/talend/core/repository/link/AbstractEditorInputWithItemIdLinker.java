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
package org.talend.core.repository.link;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public abstract class AbstractEditorInputWithItemIdLinker extends AbstractRepoViewLinker {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#getRelationNode(org.eclipse.ui.IEditorInput)
     */
    @Override
    public RepositoryNode getRelationNode(IEditorInput editorInput) {
        return getRepoNodeFromEditor(getEditor(editorInput));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#isRelation(org.eclipse.ui.IEditorInput, java.lang.String)
     */
    @Override
    public boolean isRelation(IEditorInput editorInput, String repoNodeId) {
        return isRelation(editorInput, repoNodeId, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#isRelation(org.eclipse.ui.IEditorInput, java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean isRelation(IEditorInput editorInput, String repoNodeId, String version) {
        RepositoryNode relationNode = getRelationNode(editorInput);
        boolean isRelation = false;
        if (relationNode != null && repoNodeId != null && repoNodeId.equals(relationNode.getId())) {
            isRelation = true;
            if (version != null && relationNode.getObject() != null) {
                isRelation = isRelation && version.equals(relationNode.getObject().getVersion());
            }
        }
        return isRelation;

    }

    protected IEditorPart getEditor(IEditorInput editorInput) {
        if (editorInput != null) {
            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                if (activePage != null) {
                    /*
                     * There is a warning
                     * 
                     * !MESSAGE Warning: Detected recursive attempt by part
                     * org.talend.repository.services.utils.LocalWSDLEditor to create itself (this is probably, but not
                     * necessarily, a bug)
                     */
                    // wsdlEditor = activePage.findEditor(editorInput);

                    // iterator
                    IEditorReference[] editorReferences = activePage.getEditorReferences();
                    if (editorReferences != null) {
                        for (IEditorReference er : editorReferences) {
                            try {
                                if (editorInput.equals(er.getEditorInput())) {
                                    return er.getEditor(false);
                                }
                            } catch (PartInitException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }

                }
            }
        }

        return null;
    }

    protected RepositoryNode getRepoNodeFromEditor(IEditorPart editorPart) {
        if (editorPart != null && EditorPart.class.isInstance(editorPart) && isValidEditor((EditorPart) editorPart)) {
            EditorPart editor = ((EditorPart) editorPart);
            String partItemIdKey = getPartItemIdKey();
            if (partItemIdKey != null) {
                String itemId = editor.getPartProperty(partItemIdKey);
                return searchRepoViewNode(itemId);
            }
        }
        return null;
    }

    protected String getPartItemIdKey() {
        return null;
    }

    protected boolean isValidEditor(EditorPart editorPart) {
        return false;
    }

}
