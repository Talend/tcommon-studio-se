// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.navigator.link;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.ILinkHelper;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.link.IRepoViewLinker;
import org.talend.core.repository.link.RepoViewLinkerRegistryReader;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class RepoViewLinkHelper implements ILinkHelper {

    private final RepoViewLinkerRegistryReader repoViewLinkerReader;

    public RepoViewLinkHelper() {
        this.repoViewLinkerReader = RepoViewLinkerRegistryReader.getInstance();
    }

    protected RepoViewLinkerRegistryReader getRepoViewLinkerReader() {
        return this.repoViewLinkerReader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.ILinkHelper#findSelection(org.eclipse.ui.IEditorInput)
     */
    @Override
    public IStructuredSelection findSelection(IEditorInput anInput) {
        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();

        IRepoViewLinker[] allRepoViewLinkers = getRepoViewLinkerReader().getAllRepoViewLinkers();
        for (IRepoViewLinker linker : allRepoViewLinkers) {
            RepositoryNode relationNode = linker.getRelationNode(anInput);
            if (relationNode != null && !nodes.contains(relationNode)) {
                nodes.add(relationNode);
            }
        }
        return new StructuredSelection(nodes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.ILinkHelper#activateEditor(org.eclipse.ui.IWorkbenchPage,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        Object firstElement = aSelection.getFirstElement();
        if (firstElement == null || !(firstElement instanceof RepositoryNode)) {
            return;
        }
        RepositoryNode repNode = (RepositoryNode) firstElement;
        IRepositoryViewObject object = repNode.getObject();
        if (object == null) {
            return;
        }
        final String repId = object.getId(); // repository item id
        final String version = object.getVersion();
        if (repId == null || version == null) {
            return;
        }
        IEditorReference[] editorReferences = aPage.getEditorReferences();
        if (editorReferences == null) {
            return;
        }
        for (IEditorReference er : editorReferences) {
            try {
                IEditorInput editorInput = er.getEditorInput();
                if (isRelation(editorInput, repId, version)) {
                    IEditorPart editor = null;
                    // aPage.bringToTop(er.getPart(false));
                    if ((editor = aPage.findEditor(editorInput)) != null) {
                        aPage.bringToTop(editor);
                    }
                    break;
                }
            } catch (PartInitException e) {
                ExceptionHandler.process(e);
            }
        }

    }

    protected boolean isRelation(IEditorInput editorInput, String repoNodeId, String version) {
        IRepoViewLinker[] allRepoViewLinkers = getRepoViewLinkerReader().getAllRepoViewLinkers();
        for (IRepoViewLinker linker : allRepoViewLinkers) {
            if (linker.isRelation(editorInput, repoNodeId, version)) {
                return true;
            }
        }
        return false;
    }

}
