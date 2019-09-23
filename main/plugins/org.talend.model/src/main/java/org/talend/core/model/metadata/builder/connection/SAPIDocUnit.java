/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAPI Doc Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getProgramId <em>Program Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getGatewayService <em>Gateway Service</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#isUseXmlOutput <em>Use Xml Output</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getXmlFile <em>Xml File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#isUseHtmlOutput <em>Use Html Output</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getHtmlFile <em>Html File</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit()
 * @model
 * @generated
 */
public interface SAPIDocUnit extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Connection</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SAPConnection#getIDocs <em>IDocs</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection</em>' container reference.
     * @see #setConnection(SAPConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_Connection()
     * @see org.talend.core.model.metadata.builder.connection.SAPConnection#getIDocs
     * @model opposite="IDocs" transient="false"
     * @generated
     */
    SAPConnection getConnection();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getConnection <em>Connection</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Connection</em>' container reference.
     * @see #getConnection()
     * @generated
     */
    void setConnection(SAPConnection value);

    /**
     * Returns the value of the '<em><b>Program Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Program Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Program Id</em>' attribute.
     * @see #setProgramId(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_ProgramId()
     * @model
     * @generated
     */
    String getProgramId();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getProgramId <em>Program Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Program Id</em>' attribute.
     * @see #getProgramId()
     * @generated
     */
    void setProgramId(String value);

    /**
     * Returns the value of the '<em><b>Gateway Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Gateway Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Gateway Service</em>' attribute.
     * @see #setGatewayService(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_GatewayService()
     * @model
     * @generated
     */
    String getGatewayService();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getGatewayService <em>Gateway Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Gateway Service</em>' attribute.
     * @see #getGatewayService()
     * @generated
     */
    void setGatewayService(String value);

    /**
     * Returns the value of the '<em><b>Use Xml Output</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Xml Output</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Xml Output</em>' attribute.
     * @see #setUseXmlOutput(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_UseXmlOutput()
     * @model
     * @generated
     */
    boolean isUseXmlOutput();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#isUseXmlOutput <em>Use Xml Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Xml Output</em>' attribute.
     * @see #isUseXmlOutput()
     * @generated
     */
    void setUseXmlOutput(boolean value);

    /**
     * Returns the value of the '<em><b>Xml File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Xml File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Xml File</em>' attribute.
     * @see #setXmlFile(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_XmlFile()
     * @model
     * @generated
     */
    String getXmlFile();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getXmlFile <em>Xml File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xml File</em>' attribute.
     * @see #getXmlFile()
     * @generated
     */
    void setXmlFile(String value);

    /**
     * Returns the value of the '<em><b>Use Html Output</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Html Output</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Html Output</em>' attribute.
     * @see #setUseHtmlOutput(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_UseHtmlOutput()
     * @model
     * @generated
     */
    boolean isUseHtmlOutput();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#isUseHtmlOutput <em>Use Html Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Html Output</em>' attribute.
     * @see #isUseHtmlOutput()
     * @generated
     */
    void setUseHtmlOutput(boolean value);

    /**
     * Returns the value of the '<em><b>Html File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Html File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Html File</em>' attribute.
     * @see #setHtmlFile(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPIDocUnit_HtmlFile()
     * @model
     * @generated
     */
    String getHtmlFile();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.SAPIDocUnit#getHtmlFile <em>Html File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Html File</em>' attribute.
     * @see #getHtmlFile()
     * @generated
     */
    void setHtmlFile(String value);

} // SAPIDocUnit
