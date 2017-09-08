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
package org.talend.core.runtime.repository.selection;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositorySelectionViewerTextFilter extends ViewerFilter {

    private String text = null;

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Changed by Marvin Wang on Feb. 27, 2012 for TDI-13811, remove the folder that does not include any jobs.
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (text == null || text.equals("") || "(?i).*".equals(text)) { //$NON-NLS-1$
            return true;
        }
        if (!(element instanceof RepositoryNode)) {
            return true;
        }
        RepositoryNode node = (RepositoryNode) element;
        if (node.getObject() == null) {
            return true;
        }

        IRepositoryViewObject repViewObj = node.getObject();
        if (repViewObj instanceof Folder) {
            return filterFolder(node);
        }

        String name = node.getObject().getProperty().getLabel();
        return name.matches(text);
    }

    /**
     * The input object is the node of <code>Folder</code>. DOC Added by Marvin Wang on Feb 27, 2012.
     * 
     * @param repNode
     * @return
     */
    private boolean filterFolder(IRepositoryNode repNode) {
        boolean jobExistingInFolder = hasJob(repNode);
        if (!jobExistingInFolder) {
            List<IRepositoryNode> children = repNode.getChildren();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getObject() instanceof Folder) {
                    if (hasJob(children.get(i))) {
                        jobExistingInFolder = true;
                        return jobExistingInFolder;
                    } else {
                        jobExistingInFolder = filterFolder(children.get(i));
                    }
                }
            }
        }
        return jobExistingInFolder;
    }

    /**
     * To find the matched job excepting folder, if the given repository node has a matched job, return
     * <code>true</code>. Otherwise, return <code>false</code>. DOC Added by Marvin Wang on Feb 27, 2012.
     * 
     * @param repNode
     * @return
     */
    private boolean hasJob(IRepositoryNode repNode) {
        boolean hasJob = false;
        List<IRepositoryNode> children = repNode.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                IRepositoryNode child = children.get(i);
                IRepositoryViewObject repViewObj = child.getObject();
                if (repViewObj instanceof RepositoryViewObject) {
                    String name = repViewObj.getProperty().getLabel();
                    hasJob = name.matches(text);
                    if (hasJob) {
                        break;
                    }
                } else if (repViewObj instanceof Folder) {
                    if (hasJob(children.get(i))) {
                        return true;
                    }
                }
            }
        }
        return hasJob;
    }

}
