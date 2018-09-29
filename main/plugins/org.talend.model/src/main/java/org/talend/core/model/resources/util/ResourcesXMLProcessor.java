/**
 */
package org.talend.core.model.resources.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import org.talend.core.model.resources.ResourcesPackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ResourcesXMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourcesXMLProcessor() {
        super((EPackage.Registry.INSTANCE));
        ResourcesPackage.eINSTANCE.eClass();
    }
    
    /**
     * Register for "*" and "xml" file extensions the ResourcesResourceFactoryImpl factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new ResourcesResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new ResourcesResourceFactoryImpl());
        }
        return registrations;
    }

} //ResourcesXMLProcessor
