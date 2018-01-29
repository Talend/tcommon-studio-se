/**
 */
package org.talend.librariesmanager.emf.librariesindex;

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Custom URI Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.librariesmanager.emf.librariesindex.CustomURIMap#getUriMap <em>Uri Map</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage#getCustomURIMap()
 * @model
 * @generated
 */
public interface CustomURIMap extends EObject {
    /**
     * Returns the value of the '<em><b>Uri Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uri Map</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uri Map</em>' map.
     * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage#getCustomURIMap_UriMap()
     * @model mapType="org.talend.librariesmanager.emf.librariesindex.jarToRelativePath<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
     * @generated
     */
    EMap<String, String> getUriMap();

} // CustomURIMap
