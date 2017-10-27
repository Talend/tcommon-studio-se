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
package org.talend.core.model.update.extension;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.UpdateResult;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;

/**
 * created by ggu on Mar 27, 2014 Detailled comment
 * 
 */
public abstract class AbstractRepositoryNodeUpdateManagerProvider extends AbstractRepositoryUpdateManagerProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.update.extension.IRepositoryUpdateManagerProvider#validateAction(org.eclipse.jface.viewers
     * .TreeViewer, org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public boolean validateAction(TreeViewer viewer, IStructuredSelection selection) {
        if (super.validateAction(viewer, selection)) {
            Object obj = selection.getFirstElement();
            if (obj instanceof IRepositoryNode) {
                return validRepositoryNode((IRepositoryNode) obj);
            }
        }
        return false;
    }

    protected boolean validRepositoryNode(IRepositoryNode repNode) {
        // not in current project
        if (!ProjectManager.getInstance().isInCurrentMainProject(repNode)) {
            // element
            if (ENodeType.REPOSITORY_ELEMENT.equals(repNode.getType())) {
                ERepositoryObjectType objectType = repNode.getObjectType();
                if (validRepositoryType(objectType) && isDeletedNode(repNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "validRepositoryType".
     * 
     * @param objectType repository type.
     * @return
     */
    protected abstract boolean validRepositoryType(ERepositoryObjectType objectType);

    /**
     * 
     * DOC ggu Comment method "isDeletedNode".
     * 
     * @param repNode
     * @return
     */
    protected boolean isDeletedNode(IRepositoryNode repNode) {
        Item item = repNode.getObject().getProperty().getItem();
        ERepositoryStatus status = factory.getStatus(item);
        if (!status.equals(ERepositoryStatus.DELETED)) {
            return true;
        }
        return false;
    }

    @Override
    public List<UpdateResult> retrieveUpdateResults(IProgressMonitor monitor, IStructuredSelection selection) {
        if (selection != null) {
            Object node = selection.getFirstElement();
            if (node != null && node instanceof IRepositoryNode) {
                IRepositoryNode repNode = (IRepositoryNode) node;
                IRepositoryViewObject object = repNode.getObject();
                return retrieveUpdateResults(monitor, object);
            }
        }
        return null;
    }

    /**
     * 
     * DOC ggu Comment method "retrieveUpdateResults".
     * 
     * 
     * retrieve the view object, like item, metadata table, query, etc.
     * 
     * @param monitor
     * @param object
     * @return
     */
    protected abstract List<UpdateResult> retrieveUpdateResults(IProgressMonitor monitor, IRepositoryViewObject object);
}
