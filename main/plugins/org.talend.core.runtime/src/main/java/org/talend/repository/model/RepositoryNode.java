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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.ui.IActionFilter;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.images.RepositoryImageProvider;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.actions.ActionFilterDelegateFactory;

/**
 * Node used to fill the repository view TreeViewer. Each node has a type defines in ENodeType enum. Object isn't stored
 * in the node but retrieve using the provider at each getObject call.<br/>
 * 
 * $Id: RepositoryNode.java 914 2006-12-08 08:28:53 +0000 (星期五, 08 �??二月 2006) bqian $
 * 
 */
public class RepositoryNode implements IRepositoryNode, IActionFilter {

    public static final String NO_ID = "-1"; //$NON-NLS-1$

    private String id;

    private IRepositoryViewObject object;

    private RepositoryNode parent;

    private List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();

    protected ENodeType type;

    private Map<EProperties, Object> properties = new ConcurrentHashMap<EProperties, Object>();

    private IProjectRepositoryNode root;

    // private String projectTechnicalLabel;

    private IImage icon;

    private boolean initialized = false;

    protected volatile boolean isDisposed = false;

    private boolean enableDisposed = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#isInitialized()
     */
    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Constructor with an object.
     * 
     * TODO SML Will be removed
     * 
     * @param id
     * @param object
     * @param parent Parent Node
     * @param root Root Node (project)
     * @param type
     */
    public RepositoryNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type) {
        super();
        this.id = (object == null ? NO_ID : object.getId());
        this.object = object;
        this.parent = parent;
        this.type = type;

        setRoot((parent == null ? null : parent.getRoot()));
        if (object != null) {
            object.setRepositoryNode(this);
        }
    }

    @Override
    public String toString() {
        switch (getType()) {
        case REPOSITORY_ELEMENT:
            return getObject().toString();
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);
            return service.getRepositoryPath(this).toString() + ",type=" + getContentType(); //$NON-NLS-1$
        default:
            return getType().toString() + '-' + getProperties(EProperties.LABEL);
        }
    }

    public String getRepositoryPath() {
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return service.getRepositoryPath(this).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        if (true) {
            // FIXME SML Remove when mhelleboid attach item to folders
            IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);

            List<IRepositoryNode> hiddens = service.getBrandingConfiguration().getHiddenRepositoryCategory(this, "DI");

            for (IRepositoryNode node : hiddens) {
                Iterator<IRepositoryNode> it = children.iterator();
                while (it.hasNext()) {
                    IRepositoryNode curNode = it.next();
                    if (curNode.equals(node)) {
                        it.remove();
                    }
                }
            }
            return children;
        }
        if (type == ENodeType.STABLE_SYSTEM_FOLDER || type == ENodeType.SYSTEM_FOLDER) {
            return children;
        }

        List<IRepositoryNode> toReturn = new ArrayList<IRepositoryNode>();

        for (IRepositoryViewObject currentChild : getObject().getChildren()) {
            RepositoryNode repositoryNode = new RepositoryNode(currentChild, this, ENodeType.REPOSITORY_ELEMENT);
            toReturn.add(repositoryNode);
        }

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getObject()
     */
    @Override
    public IRepositoryViewObject getObject() {
        return this.object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getParent()
     */
    @Override
    public RepositoryNode getParent() {
        return this.parent;
    }

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    public void setParent(RepositoryNode parent) {
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getType()
     */
    @Override
    public ENodeType getType() {
        // TDQ-11426: for dq exchange node, the object is null
        if (type == ENodeType.REPOSITORY_ELEMENT && getObject() != null
                && getObject().getRepositoryObjectType() == ERepositoryObjectType.FOLDER) {
            return ENodeType.SIMPLE_FOLDER;
        }
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getObjectType()
     */
    @Override
    public ERepositoryObjectType getObjectType() {
        if (getObject() != null) {
            return getObject().getRepositoryObjectType();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getContentType()
     */
    @Override
    public ERepositoryObjectType getContentType() {
        if (getObject() != null) {
            if (getObject() instanceof Folder) {
                return ((Folder) getObject()).getContentType();
            } else {
                if (getParent() != null) {
                    return getParent().getContentType();
                }
                return null;
            }
        }
        return (ERepositoryObjectType) getProperties(EProperties.CONTENT_TYPE);
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    public void setType(ENodeType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        switch (getType()) {
        case REFERENCED_PROJECT:
            return properties.get(EProperties.LABEL).toString();
        case REPOSITORY_ELEMENT:
        case SIMPLE_FOLDER:
            final ERepositoryObjectType objectType = getObjectType();
            if (objectType != null) {
                objectType.toString();
            }
        default:
            final ERepositoryObjectType contentType = getContentType();
            if (contentType != null) {
                if ("SVN".equals(contentType.toString())) {
                    return getProperties(EProperties.LABEL).toString();
                }
                if (contentType.equals(ERepositoryObjectType.PROCESS)) {
                    return getProperties(EProperties.LABEL).toString();
                }
                if (ERepositoryObjectType.TDQ_INDICATOR_ELEMENT.equals(contentType)
                        || ERepositoryObjectType.TDQ_PATTERN_ELEMENT.equals(contentType)
                        || ERepositoryObjectType.TDQ_RULES.equals(contentType)) {
                    return getObject().getLabel();
                }
                return contentType.toString();
            }
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getIcon()
     */
    public IImage getIcon() {
        if (this.icon == null) {
            switch (getType()) {
            case REPOSITORY_ELEMENT:
            case SIMPLE_FOLDER:
                return RepositoryImageProvider.getIcon(getObjectType());
            default:
                return RepositoryImageProvider.getIcon(getContentType());
            }
        }
        return this.icon;
    }

    public void setProperties(EProperties key, Object value) {
        if (value != null && key != null) {
            properties.put(key, value);
        }

    }

    // TODO SML Remove theses props
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getProperties(org.talend.
     * repository.model.RepositoryNode.EProperties)
     */
    @Override
    public Object getProperties(EProperties key) {
        return properties.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#isBin()
     */
    @Override
    public boolean isBin() {
        return false;
    }

    private static final int PRIME = 31;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = PRIME * result + ((this.properties == null) ? 0 : this.properties.hashCode());
        result = PRIME * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepositoryNode other = (RepositoryNode) obj;
        if (this.type != ENodeType.SIMPLE_FOLDER) {
            if (null != this.id && null != other.id && !this.id.equals(other.id)) {
                return false;
            }
        }
        if (this.properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!this.properties.equals(other.properties)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!this.parent.equals(other.parent)) {
            return false;
        }
        if (this.object == null) {
            if (other.object != null) {
                return false;
            }
        } else if (other.object == null) {
            if (this.object != null) {
                return false;
            }
        } else {
            if (object.getVersion() == null) {
                if (other.object.getVersion() != null) {
                    return false;
                }
            } else if (!object.getVersion().equals(other.object.getVersion())) {
                return false;
            }
        }
        if (this.getLabel() == null) {
            if (other.getLabel() != null) {
                return false;
            }// else keep checking
        } else if (!this.getLabel().equals(other.getLabel())) {
            return false;
        }// else keep checking
        return true;
    }

    @Override
    public IProjectRepositoryNode getRoot() {
        return this.root;
    }

    /**
     * Sets the root.
     * 
     * @param root the root to set
     */
    public void setRoot(IProjectRepositoryNode root) {
        this.root = root;
    }

    /**
     * it is used to TDQ ,some subClasses in TDQ will overwrite it. if withDeleted is true,will contain some logical
     * delete elements; if withDeleted is false,will not catain some logical delete elements.
     * 
     */
    @Override
    public List<IRepositoryNode> getChildren(boolean withDeleted) {
        return getChildren();
    }

    public boolean canExpandForDoubleClick() {
        return true;
    }

    public void setIcon(IImage icon) {
        this.icon = icon;
    }

    /*
     * Declarative visibility filter of extension points.
     * 
     * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public boolean testAttribute(Object target, String name, String value) {
        RepositoryNode node = (RepositoryNode) target;
        // The 2 following declarations are used by TDQ-3800:
        // Add the contextual pop-up menu to SurvivorshipFileItem nodes.
        // see also plugin.xml of org.talend.survivorship.designer
        if ("NodeType".equals(name)) { //$NON-NLS-1$
            if (value.equals(node.getType().toString())) {
                return true;
            }
        }
        if ("ContentType".equals(name)) { //$NON-NLS-1$
            ERepositoryObjectType contentType = node.getContentType();
            if (contentType != null && value.equals(contentType.getType())) {
                return true;
            }
        }
        
        return ActionFilterDelegateFactory.getInstance().testAttribute(name, node);
    }

    public boolean isEnableDisposed() {
        return enableDisposed;
    }

    public void setEnableDisposed(boolean enableDisposed) {
        this.enableDisposed = enableDisposed;
    }

    @Override
    public void dispose() {
        /*
         * FIXME, if later, can dispose all children, the flag enableDisposed will be not useful. So far, because many
         * actions reference this object, so can't dispose it, only dispose the StableRepositoryNode.
         */
        boolean doDispose = isEnableDisposed();

        // StableRepositoryNode
        if (getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
            doDispose = true;
        }
        if (doDispose) {
            this.id = null;
            this.root = null;
            this.parent = null;
            this.object = null;

            this.initialized = false;

            if (this.properties != null) {
                this.properties.clear();
            }
        }
        // dispose the children .
        if (this.children != null) {
            for (IRepositoryNode child : this.children) {
                /*
                 * In fact, won't dispose the children. because the flag 'enableDisposed' is false always. So far, only
                 * dispose the StableRepositoryNode.
                 */
                child.dispose();
            }
            if (doDispose) {
                this.children.clear();
            }
        }
        if (doDispose) {
            this.isDisposed = true;
        }
    }

    @Override
    public boolean isDisposed() {
        return this.isDisposed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel();
    }

    @Override
    public boolean shouldCollectRepositoryNode() {
        // root node of type
        return this.getParent() instanceof IProjectRepositoryNode;
    }
}
