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
package org.talend.repository.viewer.content;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.set.PredicatedSet;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

public abstract class SingleTopLevelContentProvider implements ITreeContentProvider {

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    protected static final Object[] NO_CHILDREN = new Object[0];

    // this is a temporary fix, the content provider should not hold the top level nodes at all.
    private Set<RepositoryNode> topLevelNodes = PredicatedSet.decorate(new HashSet<RepositoryNode>(), NotNullPredicate.INSTANCE);

    // private ProjectRepositoryNode projectRepositoryNode;

    /**
     * Getter for topLevelNode.
     * 
     * @return the topLevelNode
     */
    public Set<RepositoryNode> getTopLevelNodes() {
        return topLevelNodes;
    }

    // private RepositoryNode rootNode;

    /**
     * Getter for projectRepositoryNode.
     * 
     * @return the projectRepositoryNode
     */
    // public ProjectRepositoryNode getProjectRepositoryNode() {
    // return this.projectRepositoryNode;
    // }

    protected IProxyRepositoryFactory getFactory() {
        return factory;
    }

    @Override
    public Object[] getElements(Object inputElement) {// check for ProjectRepositoryNode parent
        return getChildren(inputElement);
    }

    /**
     * Called when getting the single root element for this Content Provider. This is called only once if the return
     * value is not null
     * 
     * @param the root node, never null
     * @return the first and single item object for this content provider
     */
    abstract protected RepositoryNode getInitialTopLevelNode(RepositoryNode theRootNode);

    /*
     * this must be called only with a node that is return true when isRootNodeType(repositoryNode) is called This will
     * ask for top level node and store is in the internal set.
     */
    protected RepositoryNode getAndStoreTopLevelNode(RepositoryNode repositoryNode) {
        RepositoryNode aTopLevelNode = getInitialTopLevelNode(repositoryNode);
        if (aTopLevelNode != null) {
            boolean isNewTopLevel = topLevelNodes.add(aTopLevelNode);
            if (isNewTopLevel) {// initialize it
                initilizeContentProviderWithTopLevelNode(aTopLevelNode);
            }// else already added so no need to initialised
        }// else not top level node so return null
        return aTopLevelNode;
    }

    /**
     * This is called the first time the TopLevelnode is created and store in this content provider. Used to initialized
     * content provider with the new topLevel. reset the top level node in this implmentation.
     * 
     * @param aTopLevelNode, never null
     */
    protected void initilizeContentProviderWithTopLevelNode(RepositoryNode aTopLevelNode) {
        resetTopLevelNode(aTopLevelNode);

    }

    /**
     * this is called when the model has changed and we need to reset the top level node for the next refresh. At the
     * next refresh the model will be constructed again.
     * */
    protected void resetTopLevelNode(RepositoryNode aTopLevelNode) {
        if (aTopLevelNode != null) {// reset top level node
            aTopLevelNode.setInitialized(false);
            aTopLevelNode.getChildren().clear();
        }
        // initRepositoryNode(topLevelNode2);
    }

    /**
     * this returnt the children for any type handle by this class. It first check if the element is a potential root
     * node, if so it store it. if element is the root node then getTopLevelNode is called to get the single children if
     * it is some other nodes it can handle is returns their children by delegating to the getRepositoryNodeChildren or
     * returns none.
     * */
    @Override
    public Object[] getChildren(Object element) {
        RepositoryNode theRootNode = null;
        // store the root node
        if (isRootNodeType(element)) {
            theRootNode = extractPotentialRootNode(element);
        }

        // if root then return the top level node
        if (theRootNode != null) {
            IRepositoryNode cachedTopLevelNode = getAndStoreTopLevelNode(theRootNode);
            return cachedTopLevelNode != null ? new Object[] { cachedTopLevelNode } : NO_CHILDREN;
        } else if (element instanceof RepositoryNode) {// else return children
            // // check that element is top level node or a children of it
            RepositoryNode repositoryNode = (RepositoryNode) element;
            return getRepositoryNodeChildren(repositoryNode);
        }// else not a root node and not a child not so ignors it
        return NO_CHILDREN;

    }

    /**
     * DOC sgandon Comment method "isRootNode".
     * 
     * @param element
     * @return
     */
    abstract protected boolean isRootNodeType(Object element);

    /**
     * extract a potention root node. This is called right after checking type by calling isRootNodeType so the check
     * does not need to be done again.
     * 
     * @param element used to find a potential root node
     * @return the potential rootNode or null.
     */
    abstract protected RepositoryNode extractPotentialRootNode(Object element);

    /**
     * DOC sgandon Comment method "getRepositoryNodeChildren".
     * 
     * @param element
     * @param repositoryNode
     * @return
     */
    protected Object[] getRepositoryNodeChildren(RepositoryNode repositoryNode) {
        if (!repositoryNode.isInitialized()) {
            if (repositoryNode.getParent() instanceof ProjectRepositoryNode) {
                // initialize repository from main project
                initializeChildren(repositoryNode);
            }// else sub sub node so no need to initialize
            repositoryNode.setInitialized(true);
        }// else already initialised
        return repositoryNode.getChildren().toArray();
    }

    protected void initializeChildren(RepositoryNode parent) {
        ((ProjectRepositoryNode) parent.getParent()).initializeChildren(parent);
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        Boolean boolean1 = factory.hasChildren(element);
        if (boolean1 != null) {
            return boolean1;
        } else {
            if (element instanceof RepositoryNode) {
                final RepositoryNode repositoryNode = (RepositoryNode) element;
                if (repositoryNode.isInitialized()) {
                    return repositoryNode.hasChildren();
                } else {
                    return getChildren(element).length > 0;
                }
            }
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
        // to help garbage collection
        topLevelNodes.clear();
        // topLevelNodes = null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoDirectChildrenNodeContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        // FIXME by TDI-22701, If the input model has been changed, so need clean
        topLevelNodes.clear();
    }

}
