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
package org.talend.cwm.helper;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * @author scorreia
 * 
 * Helper on resources.
 */
public final class ResourceHelper {

    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private ResourceHelper() {
    }

    /**
     * Method "getUUID".
     * 
     * @param object a EMF object
     * @return the universal id as stored in the resource or null if not found.
     */
    public static String getUUID(EObject object) {
        if (object == null) {
            return null;
        }
        if (object.eIsProxy()) {
            return ((InternalEObject) object).eProxyURI().fragment();
        }
        Resource resource = object.eResource();
        if (resource == null || !(resource instanceof XMLResource)) {
            return null;
        }
        XMLResource xmlResource = (XMLResource) resource;
        return xmlResource.getID(object);
    }

    public static void setUUid(EObject object, String uuid) {
        if (object == null || uuid == null) {
            return;
        }
        Resource resource = object.eResource();
        if (resource != null && resource instanceof XMLResource) {
            XMLResource xmlResource = (XMLResource) resource;
            xmlResource.setID(object, uuid);
        }
    }

    public static boolean areSame(EObject o1, EObject o2) {
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.eResource() != null && o2.eResource() != null) {
            return StringUtils.equals(ResourceHelper.getUUID(o1), ResourceHelper.getUUID(o2));
        }
        // else
        return false;
    }

    /**
     * Method "referenceObject" search in given collection for the searched object by id.
     * 
     * @param container a possible container of another instance of the given searcheObject. All object in the container
     * are supposed to be contained in a same resource.
     * @param proxy the object to search by id
     * @return the found object or null
     */
    @SuppressWarnings("deprecation")
    public static EObject resolveObject(Collection<? extends EObject> container, EObject proxy) {
        // get resource in which to look for the object
        if (container.isEmpty()) {
            return null;
        }
        for (EObject object : container) {
            if (object == null) {
                continue;
            }
            // found non null object
            Resource containerResource = object.eResource();
            if (containerResource == null) {
                continue;
            }
            // try normal comparison first
            if (container.contains(proxy)) {
                return proxy;
            }
            // try to remove it by id
            XMLResource depRes = containerResource instanceof XMLResource ? (XMLResource) containerResource : null;
            if (depRes != null) {
                String id = ResourceHelper.getUUID(proxy);
                if (id == null) {
                    return null;
                }
                // scorreia: warning suppressed because no other method seem to render the same service
                EObject resolvedObject = depRes.getIDToEObjectMap().get(id);
                if (resolvedObject != null) {
                    return resolvedObject;
                }
            }

        }
        return null;
    }
}
