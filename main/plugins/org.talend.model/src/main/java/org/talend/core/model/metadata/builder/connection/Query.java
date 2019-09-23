/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Query#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Query#getQueries <em>Queries</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.Query#isContextMode <em>Context Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getQuery()
 * @model
 * @generated
 */
public interface Query extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getQuery_Value()
     * @model
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Query#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Queries</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.QueriesConnection#getQuery <em>Query</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Queries</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Queries</em>' container reference.
     * @see #setQueries(QueriesConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getQuery_Queries()
     * @see org.talend.core.model.metadata.builder.connection.QueriesConnection#getQuery
     * @model opposite="query" transient="false"
     * @generated
     */
    QueriesConnection getQueries();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Query#getQueries <em>Queries</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Queries</em>' container reference.
     * @see #getQueries()
     * @generated
     */
    void setQueries(QueriesConnection value);

    /**
     * Returns the value of the '<em><b>Context Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Context Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Context Mode</em>' attribute.
     * @see #setContextMode(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getQuery_ContextMode()
     * @model
     * @generated
     */
    boolean isContextMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.Query#isContextMode <em>Context Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Context Mode</em>' attribute.
     * @see #isContextMode()
     * @generated
     */
    void setContextMode(boolean value);

} // Query
