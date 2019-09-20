/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Header Footer Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#isIsHeader <em>Is Header</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getImports <em>Imports</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getMainCode <em>Main Code</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getLibraries <em>Libraries</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHeaderFooterConnection()
 * @model
 * @generated
 */
public interface HeaderFooterConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Is Header</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Header</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Header</em>' attribute.
     * @see #setIsHeader(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHeaderFooterConnection_IsHeader()
     * @model
     * @generated
     */
    boolean isIsHeader();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#isIsHeader <em>Is Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Header</em>' attribute.
     * @see #isIsHeader()
     * @generated
     */
    void setIsHeader(boolean value);

    /**
     * Returns the value of the '<em><b>Imports</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Imports</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Imports</em>' attribute.
     * @see #setImports(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHeaderFooterConnection_Imports()
     * @model
     * @generated
     */
    String getImports();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getImports <em>Imports</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Imports</em>' attribute.
     * @see #getImports()
     * @generated
     */
    void setImports(String value);

    /**
     * Returns the value of the '<em><b>Main Code</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Main Code</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Main Code</em>' attribute.
     * @see #setMainCode(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHeaderFooterConnection_MainCode()
     * @model
     * @generated
     */
    String getMainCode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getMainCode <em>Main Code</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Main Code</em>' attribute.
     * @see #getMainCode()
     * @generated
     */
    void setMainCode(String value);

    /**
     * Returns the value of the '<em><b>Libraries</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Libraries</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Libraries</em>' attribute.
     * @see #setLibraries(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getHeaderFooterConnection_Libraries()
     * @model
     * @generated
     */
    String getLibraries();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.HeaderFooterConnection#getLibraries <em>Libraries</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Libraries</em>' attribute.
     * @see #getLibraries()
     * @generated
     */
    void setLibraries(String value);

} // HeaderFooterConnection
