/**
 */
package org.talend.core.model.properties;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Routines Jar Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.RoutinesJarType#getMvnUrl <em>Mvn Url</em>}</li>
 *   <li>{@link org.talend.core.model.properties.RoutinesJarType#getImports <em>Imports</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.properties.PropertiesPackage#getRoutinesJarType()
 * @model
 * @generated
 */
public interface RoutinesJarType extends EObject {
    /**
     * Returns the value of the '<em><b>Mvn Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mvn Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mvn Url</em>' attribute.
     * @see #setMvnUrl(String)
     * @see org.talend.core.model.properties.PropertiesPackage#getRoutinesJarType_MvnUrl()
     * @model
     * @generated
     */
    String getMvnUrl();

    /**
     * Sets the value of the '{@link org.talend.core.model.properties.RoutinesJarType#getMvnUrl <em>Mvn Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mvn Url</em>' attribute.
     * @see #getMvnUrl()
     * @generated
     */
    void setMvnUrl(String value);

    /**
     * Returns the value of the '<em><b>Imports</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.designer.core.model.utils.emf.component.IMPORTType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Imports</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Imports</em>' containment reference list.
     * @see org.talend.core.model.properties.PropertiesPackage#getRoutinesJarType_Imports()
     * @model type="org.talend.designer.core.model.utils.emf.component.IMPORTType" containment="true"
     * @generated
     */
    EList getImports();

} // RoutinesJarType
