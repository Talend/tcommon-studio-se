/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>SAP Connection</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getHost <em>Host</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getClient <em>Client</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getSystemNumber <em>System Number</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getFuntions <em>Funtions</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getCurrentFucntion <em>Current Fucntion</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getIDocs <em>IDocs</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getJcoVersion <em>Jco Version</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getAdditionalProperties <em>Additional Properties</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getBWAdvancedDataStoreObjects <em>BW Advanced Data Store Objects</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getBWDataSources <em>BW Data Sources</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getBWDataStoreObjects <em>BW Data Store Objects</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getBWInfoCubes <em>BW Info Cubes</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getBWInfoObjects <em>BW Info Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection()
 * @model
 * @generated
 */
public interface SAPConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Host</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Host</em>' attribute.
     * @see #setHost(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Host()
     * @model
     * @generated
     */
    String getHost();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getHost <em>Host</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Host</em>' attribute.
     * @see #getHost()
     * @generated
     */
    void setHost(String value);

    /**
     * Returns the value of the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Username</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Username</em>' attribute.
     * @see #setUsername(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Username()
     * @model
     * @generated
     */
    String getUsername();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getUsername <em>Username</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Username</em>' attribute.
     * @see #getUsername()
     * @generated
     */
    void setUsername(String value);

    /**
     * Returns the value of the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Password</em>' attribute.
     * @see #setPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Password()
     * @model
     * @generated
     */
    String getPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getPassword <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Password</em>' attribute.
     * @see #getPassword()
     * @generated
     */
    void setPassword(String value);

    /**
     * Returns the value of the '<em><b>Client</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Client</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Client</em>' attribute.
     * @see #setClient(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Client()
     * @model
     * @generated
     */
    String getClient();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getClient <em>Client</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Client</em>' attribute.
     * @see #getClient()
     * @generated
     */
    void setClient(String value);

    /**
     * Returns the value of the '<em><b>System Number</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>System Number</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>System Number</em>' attribute.
     * @see #setSystemNumber(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_SystemNumber()
     * @model
     * @generated
     */
    String getSystemNumber();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getSystemNumber <em>System Number</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>System Number</em>' attribute.
     * @see #getSystemNumber()
     * @generated
     */
    void setSystemNumber(String value);

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Language</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Language()
     * @model
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

    /**
     * Returns the value of the '<em><b>Funtions</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPFunctionUnit}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Funtions</em>' containment reference list isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Funtions</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_Funtions()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPFunctionUnit> getFuntions();

    /**
     * Returns the value of the '<em><b>Current Fucntion</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Current Fucntion</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Current Fucntion</em>' attribute.
     * @see #setCurrentFucntion(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_CurrentFucntion()
     * @model
     * @generated
     */
    String getCurrentFucntion();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getCurrentFucntion <em>Current Fucntion</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Current Fucntion</em>' attribute.
     * @see #getCurrentFucntion()
     * @generated
     */
    void setCurrentFucntion(String value);

    /**
     * Returns the value of the '<em><b>IDocs</b></em>' containment reference list. The list contents are of type
     * {@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit}. It is bidirectional and its opposite is '
     * {@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getConnection <em>Connection</em>}'. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>IDocs</em>' reference isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>IDocs</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_IDocs()
     * @see org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getConnection
     * @model type="org.talend.core.model.metadata.builder.connection.SAPIDocUnit" opposite="connection"
     * containment="true"
     * @generated
     */
    EList<SAPIDocUnit> getIDocs();

    /**
     * Returns the value of the '<em><b>Jco Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Jco Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Jco Version</em>' attribute.
     * @see #setJcoVersion(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_JcoVersion()
     * @model
     * @generated
     */
    String getJcoVersion();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getJcoVersion <em>Jco Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Jco Version</em>' attribute.
     * @see #getJcoVersion()
     * @generated
     */
    void setJcoVersion(String value);

    /**
     * Returns the value of the '<em><b>Additional Properties</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Properties</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Properties</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_AdditionalProperties()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<AdditionalConnectionProperty> getAdditionalProperties();

    /**
     * Returns the value of the '<em><b>BW Advanced Data Store Objects</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPBWTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BW Advanced Data Store Objects</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BW Advanced Data Store Objects</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_BWAdvancedDataStoreObjects()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPBWTable> getBWAdvancedDataStoreObjects();

    /**
     * Returns the value of the '<em><b>BW Data Sources</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPBWTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BW Data Sources</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BW Data Sources</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_BWDataSources()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPBWTable> getBWDataSources();

    /**
     * Returns the value of the '<em><b>BW Data Store Objects</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPBWTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BW Data Store Objects</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BW Data Store Objects</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_BWDataStoreObjects()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPBWTable> getBWDataStoreObjects();

    /**
     * Returns the value of the '<em><b>BW Info Cubes</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPBWTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BW Info Cubes</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BW Info Cubes</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_BWInfoCubes()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPBWTable> getBWInfoCubes();

    /**
     * Returns the value of the '<em><b>BW Info Objects</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPBWTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BW Info Objects</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BW Info Objects</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPConnection_BWInfoObjects()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPBWTable> getBWInfoObjects();

} // SAPConnection
