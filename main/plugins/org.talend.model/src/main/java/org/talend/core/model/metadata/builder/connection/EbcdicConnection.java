/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ebcdic Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getMidFile <em>Mid File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getDataFile <em>Data File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getCodePage <em>Code Page</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getSourceFileStart <em>Source File Start</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getSourceFileEnd <em>Source File End</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection()
 * @model
 * @generated
 */
public interface EbcdicConnection extends FileConnection {

    /**
     * Returns the value of the '<em><b>Mid File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mid File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mid File</em>' attribute.
     * @see #setMidFile(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection_MidFile()
     * @model
     * @generated
     */
    String getMidFile();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getMidFile <em>Mid File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mid File</em>' attribute.
     * @see #getMidFile()
     * @generated
     */
    void setMidFile(String value);

    /**
     * Returns the value of the '<em><b>Data File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data File</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data File</em>' attribute.
     * @see #setDataFile(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection_DataFile()
     * @model
     * @generated
     */
    String getDataFile();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getDataFile <em>Data File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data File</em>' attribute.
     * @see #getDataFile()
     * @generated
     */
    void setDataFile(String value);

    /**
     * Returns the value of the '<em><b>Code Page</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Code Page</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Code Page</em>' attribute.
     * @see #setCodePage(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection_CodePage()
     * @model
     * @generated
     */
    String getCodePage();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getCodePage <em>Code Page</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Code Page</em>' attribute.
     * @see #getCodePage()
     * @generated
     */
    void setCodePage(String value);

    /**
     * Returns the value of the '<em><b>Source File Start</b></em>' attribute.
     * The default value is <code>"6"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source File Start</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source File Start</em>' attribute.
     * @see #setSourceFileStart(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection_SourceFileStart()
     * @model default="6"
     * @generated
     */
    String getSourceFileStart();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getSourceFileStart <em>Source File Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source File Start</em>' attribute.
     * @see #getSourceFileStart()
     * @generated
     */
    void setSourceFileStart(String value);

    /**
     * Returns the value of the '<em><b>Source File End</b></em>' attribute.
     * The default value is <code>"72"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source File End</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source File End</em>' attribute.
     * @see #setSourceFileEnd(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getEbcdicConnection_SourceFileEnd()
     * @model default="72"
     * @generated
     */
    String getSourceFileEnd();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.EbcdicConnection#getSourceFileEnd <em>Source File End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source File End</em>' attribute.
     * @see #getSourceFileEnd()
     * @generated
     */
    void setSourceFileEnd(String value);
} // EbcdicConnection
