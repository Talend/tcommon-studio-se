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
package org.talend.repository.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.repository.constants.Constant;
import org.talend.core.repository.ui.actions.MoveObjectAction;
import org.talend.core.repository.ui.view.RepositoryDropAdapter;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.sorter.IRepositoryNodeSorter;
import org.talend.repository.view.sorter.RepositoryNodeSorterRegister;
import org.talend.repository.viewer.content.listener.IRefreshNodePerspectiveListener;
import org.talend.repository.viewer.ui.provider.INavigatorContentServiceProvider;

public class RepoViewCommonViewer extends CommonViewer implements INavigatorContentServiceProvider {

    private Map<String, Boolean> expanded = new HashMap<String, Boolean>();

    private final RepoViewCommonNavigator repViewCommonNavigator;

    private Listener dragDetectListener;

    private List<IRefreshNodePerspectiveListener> refreshNodeLisenters;

    private ServiceRegistration lockService;

    public RepoViewCommonNavigator getRepViewCommonNavigator() {
        return this.repViewCommonNavigator;
    }

    public RepoViewCommonViewer(RepoViewCommonNavigator repViewCommonNavigator, String aViewerId, Composite parent, int style) {
        super(aViewerId, parent, style);
        this.repViewCommonNavigator = repViewCommonNavigator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonViewer#init()
     */
    @Override
    protected void init() {
        super.init();
        registerLockUnlockServiceListener();
    }

    // @SuppressWarnings("restriction")
    // private void updateNavigatorContentState() {
    // INavigatorContentService contentService = getNavigatorContentService();
    // String[] visibleExtensionIds = contentService.getVisibleExtensionIds();
    // List<String> needRemovedExtensionIds = RepositoryNodeFilterHelper.getExtensionIdsNeedRemove(visibleExtensionIds);
    // if (contentService.getActivationService() instanceof NavigatorActivationService) {
    // NavigatorActivationService activationService = (NavigatorActivationService)
    // contentService.getActivationService();
    // activationService.setActive(needRemovedExtensionIds.toArray(new String[needRemovedExtensionIds.size()]), false);
    // activationService.persistExtensionActivations();
    // }
    // }

    private RepositoryNode getRepositoryNode(Item node) {
        Object data = node.getData();
        RepositoryNode repositoryNode = null;
        if (data instanceof RepositoryNode) {
            repositoryNode = (RepositoryNode) data;
        }
        return repositoryNode;
    }

    private boolean idIsValid(IRepositoryNode repositoryNode) {
        String id = repositoryNode.getId();
        return id != null && !RepositoryNode.NO_ID.equals(id);
    }

    @Override
    protected void initDragAndDrop() {
        int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
        Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };

        this.addDragSupport(ops, transfers, new DragSourceAdapter() {

            private static final long FFFFFFFFL = 0xFFFFFFFFL;

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragSetData(DragSourceEvent event) {
                repViewCommonNavigator.setNoNeedUpdate(true);
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragStart(DragSourceEvent event) {
                ISelection selection = RepoViewCommonViewer.this.getSelection();

                for (Object obj : ((StructuredSelection) selection).toArray()) {
                    if (obj instanceof RepositoryNode) {
                        RepositoryNode sourceNode = (RepositoryNode) obj;

                        // As i don't know how to get event operation i test on MoveOperation
                        event.doit = MoveObjectAction.getInstance().validateAction(sourceNode, null, true);
                    }
                }

                LocalSelectionTransfer.getTransfer().setSelection(selection);
                LocalSelectionTransfer.getTransfer().setSelectionSetTime(event.time & FFFFFFFFL);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.dnd.DragSourceAdapter#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
             */
            @Override
            public void dragFinished(DragSourceEvent event) {
                repViewCommonNavigator.dragFinished();
            }
        });
        RepositoryDropAdapter adapter = new RepositoryDropAdapter(this, getNavigatorContentService());
        adapter.setFeedbackEnabled(false);
        this.addDropSupport(ops | DND.DROP_DEFAULT, transfers, adapter);
    }

    @Override
    public void refresh(Object element) {
        if (repViewCommonNavigator != null && repViewCommonNavigator.isNoNeedUpdate()) {
            return;
        } else {
            super.refresh(element);
        }

    }

    @Override
    public INavigatorContentService getNavigatorContentService() {
        return super.getNavigatorContentService();
    }

    public void addRefreshNodePerspectiveLisenter(IRefreshNodePerspectiveListener listener) {
        if (refreshNodeLisenters == null) {
            refreshNodeLisenters = new ArrayList<IRefreshNodePerspectiveListener>();
        }
        if (listener != null) {
            refreshNodeLisenters.add(listener);
        }
    }

    public void removeRefreshNodePerspectiveLisenter(IRefreshNodePerspectiveListener listener) {
        if (refreshNodeLisenters == null) {
            return;
        }
        if (listener != null) {
            refreshNodeLisenters.remove(listener);
        }
    }

    public void fireRefreshNodePerspectiveLisenter() {
        if (refreshNodeLisenters != null) {
            for (IRefreshNodePerspectiveListener listener : refreshNodeLisenters) {
                listener.refreshNode();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSortedChildren(java.lang.Object)
     */
    @Override
    protected Object[] getSortedChildren(Object parentElementOrTreePath) {
        Object[] children = super.getSortedChildren(parentElementOrTreePath);
        // do special sorter for repository
        IRepositoryNodeSorter[] sorters = RepositoryNodeSorterRegister.getInstance().getSorters();
        if (sorters != null) {
            for (IRepositoryNodeSorter sorter : sorters) {
                sorter.sort(this, parentElementOrTreePath, children);
            }
        }
        return children;
    }

    /**
     * DOC sgandon Comment method "registerLockUnlockServiceListener".
     */
    private void registerLockUnlockServiceListener() {
        if (lockService == null) {
            BundleContext bundleContext = CoreRepositoryPlugin.getDefault().getBundle().getBundleContext();
            lockService = bundleContext.registerService(
                    EventHandler.class.getName(),
                    new EventHandler() {

                        @Override
                        public void handleEvent(Event event) {
                            if (event.getProperty(Constant.ITEM_EVENT_PROPERTY_KEY) != null) {
                                org.talend.core.model.properties.Item item = (org.talend.core.model.properties.Item) event
                                        .getProperty(Constant.ITEM_EVENT_PROPERTY_KEY);
                                refreshContentIfNecessary(item);
                            } else if (event.getProperty(Constant.FILE_LIST_EVENT_PROPERTY_KEY) != null) {
                                Collection<String> fileList = (Collection<String>) event
                                        .getProperty(Constant.FILE_LIST_EVENT_PROPERTY_KEY);
                                refreshContentIfNecessary(fileList);
                            }
                        }
                    },
                    new Hashtable<String, String>(Collections.singletonMap(EventConstants.EVENT_TOPIC,
                            Constant.REPOSITORY_ITEM_EVENT_PREFIX + "*"))); //$NON-NLS-1$
        }// else already unlock service listener already registered
    }

    /**
     * DOC sgandon Comment method "refreshContentIfNecessary".
     * 
     * @param item
     */
    private void refreshContentIfNecessary(org.talend.core.model.properties.Item item) {
        Resource propFileResouce = item.getProperty() != null ? item.getProperty().eResource() : null;
        if (propFileResouce != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    refreshNodeFromProperty(item.getProperty());
                }
            });
        }
    }

    private void refreshNodeFromProperty(org.talend.core.model.properties.Property property) {
        if (this.getTree() == null || this.getTree().isDisposed()) {
            return;
        }
        List<IRepositoryNode> nodes = new ArrayList<>();
        for (Object object : getExpandedElements()) {
            if (object instanceof IRepositoryNode) {
                nodes.add((IRepositoryNode) object);
            }
        }
        IRepositoryNode itemNode = findItemNode(property.getId(), nodes);
        if (itemNode != null) {
            IRepositoryViewObject object = itemNode.getObject();
            if (object != null) {
                // force a refresh
                object.getProperty();
            }
            if (itemNode != null && !getTree().isDisposed()) {
                refresh(itemNode, true);
            }
        }
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
            List<IRepositoryNode> childrens = new ArrayList<>();
            Widget repWidget = this.findItem(node);
            if (repWidget != null) {
                Item[] childrenItem = this.getChildren(repWidget);
                if (childrenItem != null) {
                    for (Item childItem : childrenItem) {
                        RepositoryNode repNode = this.getRepositoryNode(childItem);
                        if (repNode != null) {
                            childrens.add(repNode);
                        }
                    }
                }
            } else {
                ITreeContentProvider contentProvider = (ITreeContentProvider) getContentProvider();
                Object[] childrensObject = contentProvider.getElements(node);
                for (Object o : childrensObject) {
                    if (o instanceof IRepositoryNode) {
                        childrens.add((IRepositoryNode) o);
                    }
                }
            }
            IRepositoryNode childNode = findItemNode(id, childrens);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    protected void refreshContentIfNecessary(Collection<String> fileList) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                for (String fileUpdated : fileList) {
                    XmiResourceManager xrm = new XmiResourceManager();
                    if (xrm.isPropertyFile(fileUpdated)) {
                        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileUpdated));
                        if (file != null && file.isAccessible()) {
                            Property property = xrm.loadProperty(file);
                            refreshNodeFromProperty(property);
                        }
                    }
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonViewer#dispose()
     */
    @Override
    public void dispose() {
        if (lockService != null) {
            lockService.unregister();
            lockService = null;
        }// else service already unregistered or not event instanciated
        super.dispose();
    }

}
