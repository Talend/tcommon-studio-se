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
package org.talend.repository.viewer.content.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ResourcePostChangeRunnableListener implements IResourceChangeListener {

    private final CommonViewer viewer;

    private List<IResourceDeltaVisitor> resourcevisitors = new ArrayList<IResourceDeltaVisitor>();

    public ResourcePostChangeRunnableListener(CommonViewer v) {
        super();
        this.viewer = v;
    }

    public boolean addVisitor(IResourceDeltaVisitor visitor) {
        if (visitor != null) {
            return resourcevisitors.add(visitor);
        }
        return false;
    }

    public boolean removeVisitor(IResourceDeltaVisitor visitor) {
        if (visitor != null) {
            return resourcevisitors.remove(visitor);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent
     * )
     */
    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        if (viewer == null || resourcevisitors == null || resourcevisitors.isEmpty()) {
            return;
        }
        final Control ctrl = viewer.getControl();
        if (ctrl == null || ctrl.isDisposed()) {
            return;
        }

        // visit
        final Collection<Runnable> runnables = new HashSet<>();
        final Collection<ResourceNode> pathToRefresh = new HashSet<>();
        for (IResourceDeltaVisitor visitor : resourcevisitors) {
            if (visitor instanceof RunnableResourceVisitor) {
                ((RunnableResourceVisitor) visitor).setRunnables(runnables);
            }
            if (visitor instanceof ResourceCollectorVisitor) {
                ((ResourceCollectorVisitor) visitor).setRunnables(pathToRefresh);
            }

            try {
                event.getDelta().accept(visitor);
            } catch (CoreException e) {
                //
            }
        }

        if (!runnables.isEmpty()) {
            // Are we in the UIThread? If so spin it until we are done
            if (ctrl.getDisplay().getThread() == Thread.currentThread()) {
                runUpdates(runnables);
            } else {
                ctrl.getDisplay().asyncExec(new Runnable() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see java.lang.Runnable#run()
                     */
                    @Override
                    public void run() {
                        // Abort if this happens after disposes
                        Control ctrl2 = viewer.getControl();
                        if (ctrl2 == null || ctrl2.isDisposed()) {
                            return;
                        }

                        runUpdates(runnables);

                    }
                });
            }
        }

        if (!pathToRefresh.isEmpty()) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    for (ResourceNode resourceNode : pathToRefresh) {
                        XmiResourceManager xrm = new XmiResourceManager();
                        if (xrm.isPropertyFile(resourceNode.getPath())) {
                            List<IRepositoryNode> nodes = new ArrayList<>();
                            nodes.add(resourceNode.getTopNode());
                            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(resourceNode.getPath()));
                            Property property = xrm.loadProperty(file);
                            if (property != null) {
                                IRepositoryNode itemNode = findItemNode(property.getId(), nodes);
                                if (itemNode != null) {
                                    IRepositoryViewObject object = itemNode.getObject();
                                    if (object != null) {
                                        // force a refresh
                                        object.getProperty();
                                    }
                                    if (itemNode != null && viewer != null && !viewer.getTree().isDisposed()) {
                                        viewer.refresh(itemNode, true);
                                    }
                                }
                            }
                        } else {
                            String folder = null;
                            if (resourceNode.getPath().startsWith(resourceNode.getTopNodePath())) {
                                folder = resourceNode.getPath().replace(resourceNode.getTopNodePath(), ""); //$NON-NLS-1$
                            } else {
                                // refresh from the root node
                                folder = "";
                            }
                            IRepositoryNode nodeToRefresh = findFolder(folder, resourceNode.getTopNode());
                            if (nodeToRefresh != null) {
                                if (nodeToRefresh != null && viewer != null && !viewer.getTree().isDisposed()) {
                                    viewer.refresh(nodeToRefresh, false);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Run all of the runnables that are the widget updates
     *
     * @param runnables
     */
    private void runUpdates(Collection<Runnable> runnables) {
        try {
            final Collection<Runnable> orderedRunnables = order(runnables);

            for (Runnable runnable : orderedRunnables) {
                runnable.run();
            }
        } catch (Exception e) {
            // in case of any exception during this update, do not throw exception to avoid to block anything in the
            // product.
            // only log it. cf: TDI-29991
            ExceptionHandler.process(e);
        }
    }

    /**
     * Make sure the bin top level node should be the last one to do runnable task.
     *
     * THis related to the ProjectRepositoryNode.addItemToRecycleBin, so must be after other nodes.
     *
     * @param originalRunnables
     */
    private Collection<Runnable> order(final Collection<Runnable> originalRunnables) {
        // make all runables are only for one top level node.
        Collection<Runnable> newRunnables = new ArrayList<Runnable>(originalRunnables);

        // for bin
        List<Runnable> binNodeRunnables = new ArrayList<Runnable>();
        Runnable mainBinNodeRunnable = null;

        Iterator<Runnable> iterator = newRunnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            if (runnable instanceof TopLevelNodeRunnable) {
                TopLevelNodeRunnable nodeRunnable = (TopLevelNodeRunnable) runnable;
                Object repNode = nodeRunnable.getTopLevelNode();
                if (repNode instanceof RepositoryNode) {
                    RepositoryNode topLevelNode = (RepositoryNode) repNode;
                    if (topLevelNode != null && topLevelNode.isBin()) {
                        if (ProjectManager.getInstance().isInCurrentMainProject(topLevelNode)) {
                            mainBinNodeRunnable = runnable;
                        } else { // others for ref-project
                            binNodeRunnables.add(runnable);
                        }
                        iterator.remove(); // remove the bin runnables.
                    }
                }
            }
        }

        // add all binNodes
        newRunnables.addAll(binNodeRunnables);
        if (mainBinNodeRunnable != null) { // main bin is last.
            newRunnables.add(mainBinNodeRunnable);
        }
        return newRunnables;
    }

    /**
     * DOC nrousseau Comment method "findItemNode".
     * 
     * @param id
     * @return
     */
    private IRepositoryNode findItemNode(String id, List<IRepositoryNode> nodes) {
        for (IRepositoryNode node : nodes) {
            if (id.equals(node.getId())) {
                return node;
            }
            ITreeContentProvider contentProvider = (ITreeContentProvider) viewer.getContentProvider();
            Object[] childrensObject = contentProvider.getElements(node);
            List<IRepositoryNode> childrens = new ArrayList<>();
            for (Object o : childrensObject) {
                if (o instanceof IRepositoryNode) {
                    childrens.add((IRepositoryNode) o);
                }
            }
            IRepositoryNode childNode = findItemNode(id, node.getChildren());
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    private IRepositoryNode findFolder(String path, IRepositoryNode repoNode) {
        for (IRepositoryNode childNode : repoNode.getChildren()) {
            if (childNode.getObject() instanceof Folder) {
                String startingPath = "/" + childNode.getLabel(); //$NON-NLS-1$
                if (path.startsWith(startingPath)) {
                    return findFolder(path.replace(startingPath, ""), childNode); //$NON-NLS-1$
                }
            }
        }
        return repoNode;
    }
}
