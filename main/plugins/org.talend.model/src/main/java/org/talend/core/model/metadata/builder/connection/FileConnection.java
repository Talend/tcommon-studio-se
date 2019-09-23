/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getServer <em>Server</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFormat <em>Format</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFieldSeparatorValue <em>Field Separator Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getRowSeparatorType <em>Row Separator Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getRowSeparatorValue <em>Row Separator Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getTextIdentifier <em>Text Identifier</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseHeader <em>Use Header</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getHeaderValue <em>Header Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseFooter <em>Use Footer</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFooterValue <em>Footer Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getLimitValue <em>Limit Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isFirstLineCaption <em>First Line Caption</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isRemoveEmptyRow <em>Remove Empty Row</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEscapeType <em>Escape Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEscapeChar <em>Escape Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#getTextEnclosure <em>Text Enclosure</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileConnection#isCsvOption <em>Csv Option</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection()
 * @model abstract="true"
 * @generated
 */
public interface FileConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Server</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Server</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Server</em>' attribute.
     * @see #setServer(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_Server()
     * @model required="true"
     * @generated
     */
    String getServer();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getServer <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Server</em>' attribute.
     * @see #getServer()
     * @generated
     */
    void setServer(String value);

    /**
     * Returns the value of the '<em><b>File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File Path</em>' attribute.
     * @see #setFilePath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_FilePath()
     * @model required="true"
     * @generated
     */
    String getFilePath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFilePath <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File Path</em>' attribute.
     * @see #getFilePath()
     * @generated
     */
    void setFilePath(String value);

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.FileFormat}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.FileFormat
     * @see #setFormat(FileFormat)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_Format()
     * @model required="true"
     * @generated
     */
    FileFormat getFormat();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.FileFormat
     * @see #getFormat()
     * @generated
     */
    void setFormat(FileFormat value);

    /**
     * Returns the value of the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Encoding</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Encoding</em>' attribute.
     * @see #setEncoding(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_Encoding()
     * @model required="true"
     * @generated
     */
    String getEncoding();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEncoding <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Encoding</em>' attribute.
     * @see #getEncoding()
     * @generated
     */
    void setEncoding(String value);

    /**
     * Returns the value of the '<em><b>Field Separator Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Field Separator Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Field Separator Value</em>' attribute.
     * @see #setFieldSeparatorValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_FieldSeparatorValue()
     * @model required="true"
     * @generated
     */
    String getFieldSeparatorValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFieldSeparatorValue <em>Field Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Field Separator Value</em>' attribute.
     * @see #getFieldSeparatorValue()
     * @generated
     */
    void setFieldSeparatorValue(String value);

    /**
     * Returns the value of the '<em><b>Row Separator Type</b></em>' attribute.
     * The default value is <code>"Standart_EOL = 1"</code>.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.RowSeparator}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Row Separator Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Row Separator Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.RowSeparator
     * @see #setRowSeparatorType(RowSeparator)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_RowSeparatorType()
     * @model default="Standart_EOL = 1" required="true"
     * @generated
     */
    RowSeparator getRowSeparatorType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getRowSeparatorType <em>Row Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Row Separator Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.RowSeparator
     * @see #getRowSeparatorType()
     * @generated
     */
    void setRowSeparatorType(RowSeparator value);

    /**
     * Returns the value of the '<em><b>Row Separator Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Row Separator Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Row Separator Value</em>' attribute.
     * @see #setRowSeparatorValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_RowSeparatorValue()
     * @model
     * @generated
     */
    String getRowSeparatorValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getRowSeparatorValue <em>Row Separator Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Row Separator Value</em>' attribute.
     * @see #getRowSeparatorValue()
     * @generated
     */
    void setRowSeparatorValue(String value);

    /**
     * Returns the value of the '<em><b>Text Identifier</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Identifier</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Identifier</em>' attribute.
     * @see #setTextIdentifier(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_TextIdentifier()
     * @model default=""
     * @generated
     */
    String getTextIdentifier();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getTextIdentifier <em>Text Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Identifier</em>' attribute.
     * @see #getTextIdentifier()
     * @generated
     */
    void setTextIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Use Header</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Header</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Header</em>' attribute.
     * @see #setUseHeader(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_UseHeader()
     * @model
     * @generated
     */
    boolean isUseHeader();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseHeader <em>Use Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Header</em>' attribute.
     * @see #isUseHeader()
     * @generated
     */
    void setUseHeader(boolean value);

    /**
     * Returns the value of the '<em><b>Header Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Header Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Header Value</em>' attribute.
     * @see #setHeaderValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_HeaderValue()
     * @model
     * @generated
     */
    String getHeaderValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getHeaderValue <em>Header Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Header Value</em>' attribute.
     * @see #getHeaderValue()
     * @generated
     */
    void setHeaderValue(String value);

    /**
     * Returns the value of the '<em><b>Use Footer</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Footer</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Footer</em>' attribute.
     * @see #setUseFooter(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_UseFooter()
     * @model
     * @generated
     */
    boolean isUseFooter();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseFooter <em>Use Footer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Footer</em>' attribute.
     * @see #isUseFooter()
     * @generated
     */
    void setUseFooter(boolean value);

    /**
     * Returns the value of the '<em><b>Footer Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Footer Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Footer Value</em>' attribute.
     * @see #setFooterValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_FooterValue()
     * @model
     * @generated
     */
    String getFooterValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getFooterValue <em>Footer Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Footer Value</em>' attribute.
     * @see #getFooterValue()
     * @generated
     */
    void setFooterValue(String value);

    /**
     * Returns the value of the '<em><b>Use Limit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use Limit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Use Limit</em>' attribute.
     * @see #setUseLimit(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_UseLimit()
     * @model
     * @generated
     */
    boolean isUseLimit();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isUseLimit <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Limit</em>' attribute.
     * @see #isUseLimit()
     * @generated
     */
    void setUseLimit(boolean value);

    /**
     * Returns the value of the '<em><b>Limit Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Limit Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Limit Value</em>' attribute.
     * @see #setLimitValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_LimitValue()
     * @model
     * @generated
     */
    String getLimitValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getLimitValue <em>Limit Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Limit Value</em>' attribute.
     * @see #getLimitValue()
     * @generated
     */
    void setLimitValue(String value);

    /**
     * Returns the value of the '<em><b>First Line Caption</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>First Line Caption</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>First Line Caption</em>' attribute.
     * @see #setFirstLineCaption(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_FirstLineCaption()
     * @model
     * @generated
     */
    boolean isFirstLineCaption();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isFirstLineCaption <em>First Line Caption</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>First Line Caption</em>' attribute.
     * @see #isFirstLineCaption()
     * @generated
     */
    void setFirstLineCaption(boolean value);

    /**
     * Returns the value of the '<em><b>Remove Empty Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Remove Empty Row</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Remove Empty Row</em>' attribute.
     * @see #setRemoveEmptyRow(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_RemoveEmptyRow()
     * @model
     * @generated
     */
    boolean isRemoveEmptyRow();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isRemoveEmptyRow <em>Remove Empty Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remove Empty Row</em>' attribute.
     * @see #isRemoveEmptyRow()
     * @generated
     */
    void setRemoveEmptyRow(boolean value);

    /**
     * Returns the value of the '<em><b>Escape Type</b></em>' attribute.
     * The literals are from the enumeration {@link org.talend.core.model.metadata.builder.connection.Escape}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Escape Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Escape Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Escape
     * @see #setEscapeType(Escape)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_EscapeType()
     * @model required="true"
     * @generated
     */
    Escape getEscapeType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEscapeType <em>Escape Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Escape Type</em>' attribute.
     * @see org.talend.core.model.metadata.builder.connection.Escape
     * @see #getEscapeType()
     * @generated
     */
    void setEscapeType(Escape value);

    /**
     * Returns the value of the '<em><b>Escape Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Escape Char</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Escape Char</em>' attribute.
     * @see #setEscapeChar(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_EscapeChar()
     * @model
     * @generated
     */
    String getEscapeChar();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getEscapeChar <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Escape Char</em>' attribute.
     * @see #getEscapeChar()
     * @generated
     */
    void setEscapeChar(String value);

    /**
     * Returns the value of the '<em><b>Text Enclosure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Enclosure</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Enclosure</em>' attribute.
     * @see #setTextEnclosure(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_TextEnclosure()
     * @model
     * @generated
     */
    String getTextEnclosure();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#getTextEnclosure <em>Text Enclosure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Enclosure</em>' attribute.
     * @see #getTextEnclosure()
     * @generated
     */
    void setTextEnclosure(String value);

    /**
     * Returns the value of the '<em><b>Csv Option</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Csv Option</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Csv Option</em>' attribute.
     * @see #setCsvOption(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileConnection_CsvOption()
     * @model
     * @generated
     */
    boolean isCsvOption();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileConnection#isCsvOption <em>Csv Option</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Csv Option</em>' attribute.
     * @see #isCsvOption()
     * @generated
     */
    void setCsvOption(boolean value);

} // FileConnection
