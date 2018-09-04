/**
 */
package org.talend.core.model.resources;

import org.talend.core.model.properties.FileItem;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.resources.ResourceItem#getBindingExtension <em>Binding Extension</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.resources.ResourcesPackage#getResourceItem()
 * @model
 * @generated
 */
public interface ResourceItem extends FileItem {
    /**
     * Returns the value of the '<em><b>Binding Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Binding Extension</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Binding Extension</em>' attribute.
     * @see #setBindingExtension(String)
     * @see org.talend.core.model.resources.ResourcesPackage#getResourceItem_BindingExtension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     * @generated
     */
    String getBindingExtension();

    /**
     * Sets the value of the '{@link org.talend.core.model.resources.ResourceItem#getBindingExtension <em>Binding Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Binding Extension</em>' attribute.
     * @see #getBindingExtension()
     * @generated
     */
    void setBindingExtension(String value);

} // ResourceItem
