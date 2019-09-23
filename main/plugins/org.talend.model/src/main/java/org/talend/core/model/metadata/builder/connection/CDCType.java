/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CDC Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCType#getLinkDB <em>Link DB</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCType#getSubscribers <em>Subscribers</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCType#getCdcConnection <em>Cdc Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.CDCType#getJournalName <em>Journal Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCType()
 * @model
 * @generated
 */
public interface CDCType extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Link DB</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Link DB</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ID of the .properties file related to the CDC database
     * <!-- end-model-doc -->
     * @return the value of the '<em>Link DB</em>' attribute.
     * @see #setLinkDB(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCType_LinkDB()
     * @model
     * @generated
     */
    String getLinkDB();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.CDCType#getLinkDB <em>Link DB</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Link DB</em>' attribute.
     * @see #getLinkDB()
     * @generated
     */
    void setLinkDB(String value);

    /**
     * Returns the value of the '<em><b>Subscribers</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SubscriberTable}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Subscribers</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Subscribers</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCType_Subscribers()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<SubscriberTable> getSubscribers();

    /**
     * Returns the value of the '<em><b>Cdc Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cdc Connection</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cdc Connection</em>' reference.
     * @see #setCdcConnection(CDCConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCType_CdcConnection()
     * @model
     * @generated
     */
    CDCConnection getCdcConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.CDCType#getCdcConnection <em>Cdc Connection</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cdc Connection</em>' reference.
     * @see #getCdcConnection()
     * @generated
     */
    void setCdcConnection(CDCConnection value);

    /**
     * Returns the value of the '<em><b>Journal Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Journal Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Journal Name</em>' attribute.
     * @see #setJournalName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getCDCType_JournalName()
     * @model
     * @generated
     */
    String getJournalName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.CDCType#getJournalName <em>Journal Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Journal Name</em>' attribute.
     * @see #getJournalName()
     * @generated
     */
    void setJournalName(String value);
} // CDCType
