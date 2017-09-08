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
package org.talend.commons.runtime.model.emf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.talend.commons.exception.PersistenceException;

/***/
public class EmfHelper {

    /**
     * 
     */
    public static final String STRING_MAX_SIZE_ANNOTATION_KEY = "string.max.size";

    /**
     * 
     */
    public static final String UI_CONSTRAINTS_ANNOTATION_URL = "htttp://talend.org/UiConstraints";

    private static Logger log = Logger.getLogger(EmfHelper.class);

    @SuppressWarnings("unchecked")
    public static void visitChilds(EObject object) {
        visitChilds(object, new HashSet<String>());
    }

    public static void visitChilds(EObject object, Set<String> visitedObjects) {
        if (visitedObjects.contains(object.eClass().getName() + ";" + object.getClass().getPackage().getName())) {
            return;
        }
        visitedObjects.add(object.eClass().getName() + ";" + object.getClass().getPackage().getName());
        List<EObject> toVisit = new ArrayList<EObject>();
        for (Object element : object.eClass().getEAllReferences()) {
            EReference reference = (EReference) element;
            if (reference.isMany()) {
                List list = (List) object.eGet(reference);
                for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
                    Object get = iterator2.next();
                    if (get instanceof EObject) {
                        toVisit.add((EObject) get);
                    }
                }
            } else {
                Object get = object.eGet(reference);
                if (get instanceof EObject) {
                    toVisit.add((EObject) get);
                }
            }
        }

        for (EObject eObject : toVisit) {
            visitChilds(eObject, visitedObjects);
        }
    }

    public static void removeProxy(EObject object) {
        removeProxy(object, new HashSet<String>());
    }

    private static void removeProxy(EObject object, Set<String> visitedObjects) {
        if (visitedObjects.contains(object.eClass().getName() + ";" + object.hashCode())) {
            return;
        }
        visitedObjects.add(object.eClass().getName() + ";" + object.hashCode());
        List<EObject> toVisit = new ArrayList<EObject>();
        for (Object element : object.eClass().getEAllReferences()) {
            EReference reference = (EReference) element;
            if (reference.isMany()) {
                List list = (List) object.eGet(reference);
                for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
                    Object get = iterator2.next();
                    if (get instanceof EObject) {
                        if (((EObjectImpl) get).eIsProxy()) {
                            ((EObjectImpl) get).eSetProxyURI(null);
                        }
                        toVisit.add((EObject) get);
                    }
                }
            } else {
                Object get = object.eGet(reference);
                if (get instanceof EObject) {
                    if (((EObjectImpl) get).eIsProxy()) {
                        ((EObjectImpl) get).eSetProxyURI(null);
                    }
                    toVisit.add((EObject) get);
                }
            }
        }

        for (EObject eObject : toVisit) {
            removeProxy(eObject, visitedObjects);
        }

    }

    /**
     * Load emf model from file.
     * 
     * 
     * @param pkg EPackage of your emf model.
     * @param file The file that contains your emf model.
     * @return A list of emf object.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadEmfModel(EPackage pkg, String file) throws IOException {
        ResourceSet resourceSet = getResourceSet(pkg);

        List<T> list = new ArrayList<T>();

        URI uri = URI.createFileURI(file);
        Resource resource = resourceSet.getResource(uri, true);
        resource.load(null);

        for (EObject obj : resource.getContents()) {
            list.add((T) obj);
        }
        return list;
    }

    /**
     * Save emf model to file.
     * 
     * @param pkg EPackage of your emf model.
     * @param models A list of emf object.
     * @param file The file that will store your emf model.
     * @throws IOException
     */
    public static void saveEmfModel(EPackage pkg, List<? extends EObject> models, String file) throws IOException {
        ResourceSet resourceSet = getResourceSet(pkg);

        URI uri = URI.createFileURI(file);

        Resource resource = resourceSet.createResource(uri);

        for (EObject model : models) {
            resource.getContents().add(model);
        }

        resource.save(null);
    }

    private static ResourceSet getResourceSet(EPackage pkg) {
        ResourceSet resourceSet = new ResourceSetImpl();

        Resource.Factory.Registry registry = resourceSet.getResourceFactoryRegistry();
        registry.getExtensionToFactoryMap().put("ecore", //$NON-NLS-1$
                new EcoreResourceFactoryImpl());
        registry.getExtensionToFactoryMap().put("xml", //$NON-NLS-1$
                new XMLResourceFactoryImpl());
        registry.getExtensionToFactoryMap().put("xmi", //$NON-NLS-1$
                new XMIResourceFactoryImpl());

        EPackage.Registry reg = resourceSet.getPackageRegistry();
        reg.put(pkg.getNsURI(), pkg);

        return resourceSet;

    }

    public static void saveResource(Resource resource) throws PersistenceException {
        saveResource(resource, null);
    }

    public static void saveResource(Resource resource, OutputStream outputStream) throws PersistenceException {
        if (resource == null) {
            return;
        }
        if (resource.getURI() == null) {
            return;
        }

        HashMap options = new HashMap(2);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        options.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);
        try {
            if (outputStream == null) {
                resource.save(options);
            } else {
                resource.save(outputStream, options);
            }

        } catch (IOException e) {
            throw new PersistenceException(e);
        } catch (RuntimeException e) {
            // MOD sgandon 31/03/2010 : removed print stack trace to use log4j warn in log
            log.warn("could not save resource: " + resource.getURI() + "\nLet us try to save using xml version 1.1", e);
            // if use the xml version 1.0 to store failed, try to use the xml
            // version 1.1 to store again
            options.put(XMLResource.OPTION_XML_VERSION, "1.1"); //$NON-NLS-1$

            try {
                if (outputStream == null) {
                    resource.save(options);
                } else {
                    resource.save(outputStream, options);
                }
            } catch (IOException e1) {
                throw new PersistenceException(e);
            }
        }
    }

    public static void loadResource(Resource resource, InputStream inputStream, Map<?, ?> options) throws IOException {
        resource.load(inputStream, options);
    }

    /**
     * return the String size limit for the given ecore feature. This looks for an annotation url :
     * htttp://talend.org/UiConstraints and search for the key string.max.size
     * 
     * @param feature the ecore feature to get the size of, never null
     * @param defaultValue the default value returned if limit not found in feature
     * @return the string limit found or the default value
     */
    public static int getStringMaxSize(EStructuralFeature feature, int defaultValue) {
        Assert.isNotNull(feature);
        int result = defaultValue;
        EAnnotation guiAnnotation = feature.getEAnnotation(UI_CONSTRAINTS_ANNOTATION_URL);
        if (guiAnnotation != null) {
            String docuValue = guiAnnotation.getDetails().get(STRING_MAX_SIZE_ANNOTATION_KEY);
            try {
                result = Integer.parseInt(docuValue);
            } catch (Exception e) { // if conversion fail return default value
                log.error("Could not get max size for attribute " + feature, e);
            }
        } // else return default value
        return result;
    }

}
