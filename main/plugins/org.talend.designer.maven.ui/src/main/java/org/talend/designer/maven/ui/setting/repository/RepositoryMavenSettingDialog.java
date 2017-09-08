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
package org.talend.designer.maven.ui.setting.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.IProjectSettingContainer;
import org.talend.core.ui.images.RepositoryImageProvider;
import org.talend.designer.maven.ui.i18n.Messages;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.viewer.ui.provider.RepoCommonViewerProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryMavenSettingDialog extends PreferenceDialog implements IProjectSettingContainer {

    private final RepositoryMavenSettingManager mavenSettingManager;

    private RepositoryNode selectedNode;

    private TreeViewer fakeTreeViewer;

    public RepositoryMavenSettingDialog(Shell parentShell, RepositoryMavenSettingManager mavenSettingManager) {
        super(parentShell, mavenSettingManager);
        this.mavenSettingManager = mavenSettingManager;
    }

    public void setNode(RepositoryNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ConfigFolderLevelMavenPomAction_ActionTitle")); //$NON-NLS-1$
        if (this.selectedNode != null) {
            newShell.setImage(ImageProvider.getImage(RepositoryImageProvider.getIcon(this.selectedNode.getContentType())));
        }
        newShell.setSize(1000, 700);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createTreeAreaContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createTreeAreaContents(Composite parent) {
        // create fake repo view
        RepoCommonViewerProvider provider = new RepoCommonViewerProvider();
        fakeTreeViewer = provider.createViewer(parent);

        ERepositoryObjectType[] supportTypes = RepositoryMavenSettingManager.REGISTRY.getSupportTypes();

        List<ERepositoryObjectType> contentTypes = null;
        if (supportTypes == null || DesignerMavenUiHelper.onlyWithselectedType && this.selectedNode != null) {
            contentTypes = new ArrayList<ERepositoryObjectType>();
            contentTypes.add(this.selectedNode.getContentType());
        } else if (supportTypes != null) {
            contentTypes = new ArrayList<ERepositoryObjectType>();
            contentTypes.addAll(Arrays.asList(supportTypes));
        }
        fakeTreeViewer.addFilter(new RepositoryContextTypesFilter(contentTypes));
        // FIXME, maybe should enhance the performance more here to init the nodes, when expand the preference node.
        fakeTreeViewer.expandAll(); // in order to load all nodes
        mavenSettingManager.init(fakeTreeViewer);
        fakeTreeViewer.getTree().dispose(); // remove the tree.

        Control contents = super.createTreeAreaContents(parent);

        // if PreferenceDialog will work well. won't work for FilteredPreferenceDialog
        this.getTreeViewer().expandAll();

        return contents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.dialogs.FilteredPreferenceDialog#findNodeMatching(java.lang.String)
     */
    @Override
    protected IPreferenceNode findNodeMatching(String nodeId) {
        if (nodeId == null) {
            return null;
        }
        List<IPreferenceNode> nodes = this.getPreferenceManager().getElements(PreferenceManager.POST_ORDER);
        for (Iterator<IPreferenceNode> i = nodes.iterator(); i.hasNext();) {
            IPreferenceNode node = i.next();
            if (node.getId().equals(nodeId)) {
                return node;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#cancelPressed()
     */
    @Override
    protected void cancelPressed() {
        super.cancelPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#okPressed()
     */
    @Override
    protected void okPressed() {
        RepositoryWorkUnit rwu = new RepositoryWorkUnit(null) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                final Shell shell = RepositoryMavenSettingDialog.this.getShell();
                shell.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        RepositoryMavenSettingDialog.super.okPressed();
                    }
                });
            }
        };
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(rwu);
    }

    @Override
    public boolean addChildrenPreferenceNodes(String parentId, List<IPreferenceNode> childrenNodes) {
        IPreferenceNode findNodeMatching = findNodeMatching(parentId);
        if (findNodeMatching != null && childrenNodes != null) {
            IPreferenceNode[] subNodes = findNodeMatching.getSubNodes();

            // remove old one to make sure the add nodes is front.
            if (subNodes != null) {
                for (IPreferenceNode n : subNodes) {
                    findNodeMatching.remove(n);
                }
            }
            // add new nodes.
            for (IPreferenceNode n : childrenNodes) {
                findNodeMatching.add(n);
            }
            // add back the original nodes.
            for (IPreferenceNode n : subNodes) {
                findNodeMatching.add(n);
            }

            this.getTreeViewer().refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeChildrenPreferenceNodes(String parentId, List<String> childrenIds) {
        IPreferenceNode findNodeMatching = findNodeMatching(parentId);
        if (findNodeMatching != null && childrenIds != null) {
            // remove existed nodes.
            List<IPreferenceNode> recordRemovedNodes = new ArrayList<IPreferenceNode>();
            boolean nonExisted = false;
            for (String childId : childrenIds) {
                IPreferenceNode removedNode = findNodeMatching.remove(childId);
                if (removedNode != null) {
                    recordRemovedNodes.add(removedNode);
                } else {
                    nonExisted = true;
                    break;
                }
            }
            // add back the delete nodes
            if (nonExisted) {
                for (IPreferenceNode node : recordRemovedNodes) {
                    findNodeMatching.add(node);
                }
                return false;
            } else {
                this.getTreeViewer().refresh();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean openPage(String nodeId, Object data) {
        if (this instanceof IWorkbenchPreferenceContainer) {
            ((IWorkbenchPreferenceContainer) this).openPage(nodeId, data);
        } else { // impl by self
            final IPreferenceNode node = findNodeMatching(nodeId);
            if (node != null) {
                getTreeViewer().setSelection(new StructuredSelection(node));
                showPage(node);
            }
            IPreferencePage page = getCurrentPage();
            if (page instanceof PreferencePage) {
                ((PreferencePage) page).applyData(data);
            }
            return true;
        }
        return false;
    }

}
