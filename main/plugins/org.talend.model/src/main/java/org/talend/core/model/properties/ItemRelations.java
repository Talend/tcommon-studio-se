/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.properties;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item Relations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.ItemRelations#getBaseItem <em>Base Item</em>}</li>
 *   <li>{@link org.talend.core.model.properties.ItemRelations#getRelatedItems <em>Related Items</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelations()
 * @model
 * @generated
 */
public interface ItemRelations extends EObject {
    /**
	 * Returns the value of the '<em><b>Base Item</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Item</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Item</em>' containment reference.
	 * @see #setBaseItem(ItemRelation)
	 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelations_BaseItem()
	 * @model containment="true"
	 * @generated
	 */
    ItemRelation getBaseItem();

    /**
	 * Sets the value of the '{@link org.talend.core.model.properties.ItemRelations#getBaseItem <em>Base Item</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Item</em>' containment reference.
	 * @see #getBaseItem()
	 * @generated
	 */
    void setBaseItem(ItemRelation value);

    /**
	 * Returns the value of the '<em><b>Related Items</b></em>' containment reference list.
	 * The list contents are of type {@link org.talend.core.model.properties.ItemRelation}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Related Items</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Related Items</em>' containment reference list.
	 * @see org.talend.core.model.properties.PropertiesPackage#getItemRelations_RelatedItems()
	 * @model type="org.talend.core.model.properties.ItemRelation" containment="true"
	 * @generated
	 */
    EList getRelatedItems();

} // ItemRelations
