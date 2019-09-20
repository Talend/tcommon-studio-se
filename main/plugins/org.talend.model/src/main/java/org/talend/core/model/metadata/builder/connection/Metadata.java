/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Metadata#getConnections <em>Connections</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadata()
 * @model
 * @generated
 */
public interface Metadata extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Connections</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.Connection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connections</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadata_Connections()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<Connection> getConnections();

} // Metadata
