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
package org.talend.core.repository.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.NotifyingInternalEListImpl;

/**
 * created by nrousseau on Aug 7, 2013<br>
 * <br>
 * 
 * This class reimplement some of the original classes from the emf ResourceSetImpl, to try to have a synchronzed list.
 * It should avoid any concurrent access. <br>
 * <br>
 * <i>Note that when using method getResources, it's advised to do something like:</i><br>
 * List<Resource> resources = resourceSet.getResources();<br>
 * synchronized (resources) {<br>
 * ...<br>
 * }<br>
 * To ensure that there will be no concurrent access while using the list.
 */
public class TalendResourceSet extends ResourceSetImpl {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResources()
     */
    @Override
    public EList<Resource> getResources() {
        if (resources == null) {
            resources = new SynchronizedResourcesEList<Resource>();
        }
        return resources;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI, boolean)
     */
    @Override
    public Resource getResource(URI uri, boolean loadOnDemand) {
        Map<URI, Resource> map = getURIResourceMap();
        if (map != null) {
            Resource resource = map.get(uri);
            if (resource != null) {
                if (loadOnDemand && !resource.isLoaded()) {
                    demandLoadHelper(resource);
                }
                return resource;
            }
        }

        URIConverter theURIConverter = getURIConverter();
        URI normalizedURI = theURIConverter.normalize(uri);
        List<Resource> asyncResources = getResources();
        synchronized (asyncResources) {
            for (Resource resource : asyncResources) {
                if (resource == null) {
                    continue;
                }
                if (theURIConverter.normalize(resource.getURI()).equals(normalizedURI)) {
                    if (loadOnDemand && !resource.isLoaded()) {
                        demandLoadHelper(resource);
                    }

                    if (map != null) {
                        map.put(uri, resource);
                    }
                    return resource;
                }
            }
        }

        Resource delegatedResource = delegatedGetResource(uri, loadOnDemand);
        if (delegatedResource != null) {
            if (map != null) {
                map.put(uri, delegatedResource);
            }
            return delegatedResource;
        }

        if (loadOnDemand) {
            Resource resource = demandCreateResource(uri);
            if (resource == null) {
                throw new RuntimeException("Cannot create a resource for '" + uri + "'; a registered resource factory is needed");
            }

            demandLoadHelper(resource);

            if (map != null) {
                map.put(uri, resource);
            }
            return resource;
        }

        return null;
    }

    /**
     * A notifying list implementation for supporting {@link ResourceSet#getResources}.
     */
    private class SynchronizedResourcesEList<E extends Object & Resource> extends NotifyingInternalEListImpl<E> implements
            InternalEList<E> {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#add(java.lang.Object)
         */
        @Override
        public synchronized boolean add(E object) {
            if (object == null) {
                return false;
            }
            return super.add(object);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#add(int, java.lang.Object)
         */
        @Override
        public synchronized void add(int index, E object) {
            super.add(index, object);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#addAll(java.util.Collection)
         */
        @Override
        public synchronized boolean addAll(Collection<? extends E> collection) {
            return super.addAll(collection);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#addAll(int, java.util.Collection)
         */
        @Override
        public synchronized boolean addAll(int index, Collection<? extends E> collection) {
            return super.addAll(index, collection);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#remove(java.lang.Object)
         */
        @Override
        public synchronized boolean remove(Object object) {
            return super.remove(object);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.emf.common.util.AbstractEList#move(int, java.lang.Object)
         */
        @Override
        public synchronized void move(int index, E object) {
            super.move(index, object);
        }

        private static final long serialVersionUID = 1L;

        @Override
        protected boolean isNotificationRequired() {
            return TalendResourceSet.this.eNotificationRequired();
        }

        @Override
        protected Object[] newData(int capacity) {
            return new Resource[capacity];
        }

        @Override
        public Object getNotifier() {
            return TalendResourceSet.this;
        }

        @Override
        public int getFeatureID() {
            return RESOURCE_SET__RESOURCES;
        }

        @Override
        protected boolean useEquals() {
            return false;
        }

        @Override
        protected boolean hasInverse() {
            return true;
        }

        @Override
        protected boolean isUnique() {
            return true;
        }

        @Override
        protected NotificationChain inverseAdd(E object, NotificationChain notifications) {
            Resource.Internal resource = (Resource.Internal) object;
            return resource.basicSetResourceSet(TalendResourceSet.this, notifications);
        }

        @Override
        protected NotificationChain inverseRemove(E object, NotificationChain notifications) {
            Resource.Internal resource = (Resource.Internal) object;
            Map<URI, Resource> map = getURIResourceMap();
            if (map != null) {
                for (Iterator<Resource> i = map.values().iterator(); i.hasNext();) {
                    if (resource == i.next()) {
                        i.remove();
                    }
                }
            }
            return resource.basicSetResourceSet(null, notifications);
        }

        @Override
        public synchronized boolean contains(Object object) {
            return size <= 4 ? super.contains(object) : object instanceof Resource
                    && ((Resource) object).getResourceSet() == TalendResourceSet.this;
        }
    }

    /**
     * Returns a standard label with the list of resources.
     * 
     * @return the string form.
     */
    @Override
    public String toString() {
        return getClass().getName() + '@' + Integer.toHexString(hashCode()) + " resources="
                + (resources == null ? "[]" : resources.toString());
    }

}
