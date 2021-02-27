/**
 */
package org.talend.core.model.properties;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Routines Jar Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.RoutinesJarItem#getRoutinesJarType <em>Routines Jar Type</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.properties.PropertiesPackage#getRoutinesJarItem()
 * @model
 * @generated
 */
public interface RoutinesJarItem extends Item {
    /**
     * Returns the value of the '<em><b>Routines Jar Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Routines Jar Type</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Routines Jar Type</em>' reference.
     * @see #setRoutinesJarType(RoutinesJarType)
     * @see org.talend.core.model.properties.PropertiesPackage#getRoutinesJarItem_RoutinesJarType()
     * @model
     * @generated
     */
    RoutinesJarType getRoutinesJarType();

    /**
     * Sets the value of the '{@link org.talend.core.model.properties.RoutinesJarItem#getRoutinesJarType <em>Routines Jar Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Routines Jar Type</em>' reference.
     * @see #getRoutinesJarType()
     * @generated
     */
    void setRoutinesJarType(RoutinesJarType value);

} // RoutinesJarItem
