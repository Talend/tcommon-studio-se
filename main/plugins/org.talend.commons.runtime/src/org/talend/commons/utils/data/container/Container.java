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
package org.talend.commons.utils.data.container;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.i18n.internal.Messages;

/**
 * Utilities class use to hold data in a tree like structure.<br/>
 * <br/>
 * 
 * This structure assure id unicity in the whole structure using the addMember method.<br/>
 * <br/>
 * 
 * @param <K> - DOC SML
 * @param <V> - type the container manages
 * 
 * $Id$
 * 
 */
public class Container<K, V> {

    private String label;

    private String id;

    private Container<K, V> parent;

    private List<Container<K, V>> subContainer = new ArrayList<Container<K, V>>();

    private List<K> members = new ArrayList<K>();

    private Object property;

    /**
     * Private constructor used to create a sub-container.
     * 
     * @param label
     */
    protected Container(String label, Container<K, V> parent) {
        super();
        this.label = label;
        this.parent = parent;
    }

    /**
     * Retrieves the root of the structure this container belongs.
     * 
     * @return the structure root as a container
     */
    public RootContainer<K, V> getRoot() {
        if (!hasParent()) {
            return (RootContainer<K, V>) this;
        } else {
            return getParent().getRoot();
        }
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for id.
     * 
     * @return the id
     */
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

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public Container<K, V> getParent() {
        return this.parent;
    }

    /**
     * Used to know if a container has a parent or not. If not, it's the root of a structure.
     * 
     * @return <i>true</i> if it has a parent, <i>false</i> if not
     */
    public boolean hasParent() {
        return this.parent != null;
    }

    public IPath getPath() {
        if (!hasParent()) {
            return new Path(""); //$NON-NLS-1$
        }
        IPath toReturn = getParent().getPath();
        return toReturn.append(getLabel());
    }

    public IPath getPath(K key) {
        Container<K, V> parent2 = getRoot().getParent(key);
        if (parent2 == null) {
            return null;
        }
        return parent2.getPath();
    }

    public boolean isEmpty() {
        return !hasMembers() && !hasSubContainers();
    }

    /**
     * Used to know if a container has members.
     * 
     * @return <i>true</i> if it has members, <i>false</i> if not
     */
    public boolean hasMembers() {
        return !members.isEmpty();
    }

    /**
     * Returns the number of members this container has.
     * 
     * @return the number of memebers
     */
    public int membersSize() {
        return members.size();
    }

    /**
     * Getter for members.
     * 
     * @return the members
     */
    public List<V> getMembers() {
        RootContainer<K, V> root = getRoot();
        List<V> toReturn = new ArrayList<V>();

        for (K key : members) {
            toReturn.add(root.getAbsoluteMember(key));
        }

        return toReturn;
    }

    /**
     * Returns a specific member of the container by its key.
     * 
     * @param key - the key to retrieve
     * @return the member with the searched id or null if no member with this id exists
     */
    public V getMember(K key) {
        Content<K, V> object = getRoot().getAbsoluteMembers().getObject(key);
        if (object == null) {
            return null;
        }
        return object.getContent();
    }

    /**
     * Adds a member to this container. Checks id unicity in structure.
     * 
     * @param member - the member to add
     * @throws BusinessException if a member with this id already exists in the tree. If member with the same id exists
     * in this container, no exception is thrown and param member replace old member with same id
     */
    public void addMember(K key, V member) throws BusinessException {
        // Check id unicity in the tree
        V objectInThisContainer = getMember(key);

        if (objectInThisContainer == null) {
            V objectInAllTree = getRoot().getAbsoluteMember(key);
            if (objectInAllTree != null) {
                String msg = Messages.getString("utils.data.container", key); //$NON-NLS-1$
                throw new BusinessException(msg);
            }
            members.add(key);
        }

        getRoot().getAbsoluteMembers().put(this, key, member);
    }

    /**
     * Remove an object from the container given its key if present. Based on the remove method of Map.
     * 
     * @param key - the key of the member to remove
     */
    public void removeMember(Object key) {
        members.remove(key);
        getRoot().getAbsoluteMembers().remove(key);
    }

    /**
     * Indicates if this container has sub-containers.
     * 
     * @return <i>true</i> if it has sub-conntainer or <i>false</i> if not.
     */
    public boolean hasSubContainers() {
        return !subContainer.isEmpty();
    }

    /**
     * Returns the number of member in this container.
     * 
     * @return the number of member in this container.
     */
    public int subContainersSize() {
        return subContainer.size();
    }

    /**
     * Getter for subContainer.
     * 
     * @return the subContainer
     */
    public List<Container<K, V>> getSubContainer() {
        return this.subContainer;
    }

    public Container<K, V> getSubContainer(String containerLabel) {
        for (Container<K, V> current : subContainer) {
            if (StringUtils.equals(current.getLabel(), containerLabel)) {
                return current;
            }
        }
        return null;
    }

    /**
     * Adds a sub-container to this container. The new container is instantiate given its label and attach the the
     * container as a child.
     * 
     * @param name - the label of the new sub-container
     * @return the newly instantiate container
     */
    public Container<K, V> addSubContainer(String name) {
        for (Container<K, V> existingContainer : subContainer) {
            if (existingContainer.getLabel().equals(name)) {
                return existingContainer;
            }
        }
        Container<K, V> toReturn = new Container<K, V>(name, this);
        subContainer.add(toReturn);
        return toReturn;
    }

    /**
     * Removes a sub container from this container if present. All sub-container and members are also removed.
     * 
     * @param container - the container to remove.
     */
    public void removeSubContainer(Container<K, V> container) {
        subContainer.remove(container);
    }

    public void setProperty(Object property) {
        this.property = property;
    }

    public Object getProperty() {
        return this.property;
    }

}
