/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.properties;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.ItemRelation#getId <em>Id</em>}</li>
 *   <li>{@link org.talend.core.model.properties.ItemRelation#getVersion <em>Version</em>}</li>
 *   <li>{@link org.talend.core.model.properties.ItemRelation#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelation()
 * @model
 * @generated
 */
public interface ItemRelation extends EObject {
    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelation_Id()
	 * @model
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link org.talend.core.model.properties.ItemRelation#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelation_Version()
	 * @model
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link org.talend.core.model.properties.ItemRelation#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
    void setVersion(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelation_Type()
	 * @model
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link org.talend.core.model.properties.ItemRelation#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

} // ItemRelation
