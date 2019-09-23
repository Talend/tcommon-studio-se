/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Database Connection</b></em>'. <!-- end-user-doc
 * -->
 *
 * <!-- begin-model-doc --> Defines a connection to a Database <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDatabaseType <em>Database Type
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDriverJarPath <em>Driver Jar Path
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDriverClass <em>Driver Class</em>}
 * </li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getURL <em>URL</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDbVersionString <em>Db Version
 * String</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getPort <em>Port</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getUsername <em>Username</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getPassword <em>Password</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getServerName <em>Server Name</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDatasourceName <em>Datasource Name
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getFileFieldName <em>File Field Name
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getSID <em>SID</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getSqlSynthax <em>Sql Synthax</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getStringQuote <em>String Quote</em>}
 * </li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getNullChar <em>Null Char</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDbmsId <em>Dbms Id</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getProductId <em>Product Id</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDBRootPath <em>DB Root Path</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getAdditionalParams <em>Additional
 * Params</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isStandardSQL <em>Standard SQL</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSystemSQL <em>System SQL</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcConns <em>Cdc Conns</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcTypeMode <em>Cdc Type Mode
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSQLMode <em>SQL Mode</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getUiSchema <em>Ui Schema</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection()
 * @model
 * @generated
 */
public interface DatabaseConnection extends Connection {

    /**
     * Returns the value of the '<em><b>Database Type</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Database Type</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> logical type of the DB (for instance MySQL) <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Database Type</em>' attribute.
     * @see #setDatabaseType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DatabaseType()
     * @model
     * @generated
     */
    String getDatabaseType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDatabaseType <em>Database Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Database Type</em>' attribute.
     * @see #getDatabaseType()
     * @generated
     */
    void setDatabaseType(String value);

    /**
     * Returns the value of the '<em><b>Driver Jar Path</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Driver Jar Path</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> absolute path to the jar that may be used for Generic JDBC
     * connection <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Driver Jar Path</em>' attribute.
     * @see #setDriverJarPath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DriverJarPath()
     * @model
     * @generated
     */
    String getDriverJarPath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDriverJarPath <em>Driver Jar Path</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Driver Jar Path</em>' attribute.
     * @see #getDriverJarPath()
     * @generated
     */
    void setDriverJarPath(String value);

    /**
     * Returns the value of the '<em><b>Driver Class</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Driver Class</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> initial class for generic JDBC connection <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Driver Class</em>' attribute.
     * @see #setDriverClass(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DriverClass()
     * @model
     * @generated
     */
    String getDriverClass();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDriverClass <em>Driver Class</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Driver Class</em>' attribute.
     * @see #getDriverClass()
     * @generated
     */
    void setDriverClass(String value);

    /**
     * Returns the value of the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>URL</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> the connection base URL for JDBC protocol. It is a concatenation
     * of DatabaseType, ServerName, Port and other attributes of this class <!-- end-model-doc -->
     * 
     * @return the value of the '<em>URL</em>' attribute.
     * @see #setURL(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_URL()
     * @model
     * @generated
     */
    String getURL();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getURL <em>URL</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>URL</em>' attribute.
     * @see #getURL()
     * @generated
     */
    void setURL(String value);

    /**
     * Returns the value of the '<em><b>Db Version String</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Db Version String</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> may hold the version of the Database for a given Database type
     * (for instance MySQL_4 or MySQL_5) <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Db Version String</em>' attribute.
     * @see #setDbVersionString(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DbVersionString()
     * @model
     * @generated
     */
    String getDbVersionString();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDbVersionString <em>Db Version String</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Db Version String</em>' attribute.
     * @see #getDbVersionString()
     * @generated
     */
    void setDbVersionString(String value);

    /**
     * Returns the value of the '<em><b>Port</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Port</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> port used for the Database Connection <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Port</em>' attribute.
     * @see #setPort(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_Port()
     * @model
     * @generated
     */
    String getPort();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getPort <em>Port</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Port</em>' attribute.
     * @see #getPort()
     * @generated
     */
    void setPort(String value);

    /**
     * Returns the value of the '<em><b>Username</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Username</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> user name used for DB connection authentification <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Username</em>' attribute.
     * @see #setUsername(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_Username()
     * @model
     * @generated
     */
    String getUsername();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getUsername <em>Username</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Username</em>' attribute.
     * @see #getUsername()
     * @generated
     */
    void setUsername(String value);

    /**
     * Returns the value of the '<em><b>Password</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Password</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> password used for DB connection authentification <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Password</em>' attribute.
     * @see #setPassword(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_Password()
     * @model
     * @generated
     */
    String getPassword();

    /**
     * 
     * Get the password directly without decryption.
     * 
     * If context mode, use the getPassword directly.
     * 
     * @not generated
     * @return
     */
    String getRawPassword();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getPassword <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Password</em>' attribute.
     * @see #getPassword()
     * @generated
     */
    void setPassword(String value);

    /**
     * Set the raw value and encrypt it.
     * 
     * @param value
     */
    void setRawPassword(String value);

    /**
     * Returns the value of the '<em><b>Server Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Server Name</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> IP adress or machine name of the DB server to connect to. <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Server Name</em>' attribute.
     * @see #setServerName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_ServerName()
     * @model
     * @generated
     */
    String getServerName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getServerName <em>Server Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Server Name</em>' attribute.
     * @see #getServerName()
     * @generated
     */
    void setServerName(String value);

    /**
     * Returns the value of the '<em><b>Datasource Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datasource Name</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Datasource Name</em>' attribute.
     * @see #setDatasourceName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DatasourceName()
     * @model
     * @generated
     */
    String getDatasourceName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDatasourceName <em>Datasource Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Datasource Name</em>' attribute.
     * @see #getDatasourceName()
     * @generated
     */
    void setDatasourceName(String value);

    /**
     * Returns the value of the '<em><b>File Field Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Field Name</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> Database file used for DB such as SqlLite <!-- end-model-doc -->
     * 
     * @return the value of the '<em>File Field Name</em>' attribute.
     * @see #setFileFieldName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_FileFieldName()
     * @model
     * @generated
     */
    String getFileFieldName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getFileFieldName <em>File Field Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>File Field Name</em>' attribute.
     * @see #getFileFieldName()
     * @generated
     */
    void setFileFieldName(String value);

    /**
     * Returns the value of the '<em><b>SID</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>SID</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> Logical name of the Database <!-- end-model-doc -->
     * 
     * @return the value of the '<em>SID</em>' attribute.
     * @see #setSID(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_SID()
     * @model
     * @generated
     */
    String getSID();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getSID <em>SID</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>SID</em>' attribute.
     * @see #getSID()
     * @generated
     */
    void setSID(String value);

    /**
     * Returns the value of the '<em><b>Sql Synthax</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sql Synthax</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sql Synthax</em>' attribute.
     * @see #setSqlSynthax(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_SqlSynthax()
     * @model
     * @generated
     */
    String getSqlSynthax();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getSqlSynthax <em>Sql Synthax</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Sql Synthax</em>' attribute.
     * @see #getSqlSynthax()
     * @generated
     */
    void setSqlSynthax(String value);

    /**
     * Returns the value of the '<em><b>String Quote</b></em>' attribute. The default value is <code>"\""</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>String Quote</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>String Quote</em>' attribute.
     * @see #setStringQuote(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_StringQuote()
     * @model default="\""
     * @generated
     */
    String getStringQuote();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getStringQuote <em>String Quote</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>String Quote</em>' attribute.
     * @see #getStringQuote()
     * @generated
     */
    void setStringQuote(String value);

    /**
     * Returns the value of the '<em><b>Null Char</b></em>' attribute. The default value is <code>"000"</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Null Char</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Null Char</em>' attribute.
     * @see #setNullChar(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_NullChar()
     * @model default="000"
     * @generated
     */
    String getNullChar();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getNullChar <em>Null Char</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Null Char</em>' attribute.
     * @see #getNullChar()
     * @generated
     */
    void setNullChar(String value);

    /**
     * Returns the value of the '<em><b>Dbms Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dbms Id</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dbms Id</em>' attribute.
     * @see #setDbmsId(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DbmsId()
     * @model
     * @generated
     */
    String getDbmsId();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDbmsId <em>Dbms Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Dbms Id</em>' attribute.
     * @see #getDbmsId()
     * @generated
     */
    void setDbmsId(String value);

    /**
     * Returns the value of the '<em><b>Product Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Product Id</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Product Id</em>' attribute.
     * @see #setProductId(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_ProductId()
     * @model
     * @generated
     */
    String getProductId();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getProductId <em>Product Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Product Id</em>' attribute.
     * @see #getProductId()
     * @generated
     */
    void setProductId(String value);

    /**
     * Returns the value of the '<em><b>DB Root Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>DB Root Path</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>DB Root Path</em>' attribute.
     * @see #setDBRootPath(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_DBRootPath()
     * @model
     * @generated
     */
    String getDBRootPath();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getDBRootPath <em>DB Root Path</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>DB Root Path</em>' attribute.
     * @see #getDBRootPath()
     * @generated
     */
    void setDBRootPath(String value);

    /**
     * Returns the value of the '<em><b>Additional Params</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Params</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> parameters that are to be added to the connection URL <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Additional Params</em>' attribute.
     * @see #setAdditionalParams(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_AdditionalParams()
     * @model
     * @generated
     */
    String getAdditionalParams();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getAdditionalParams <em>Additional Params</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Params</em>' attribute.
     * @see #getAdditionalParams()
     * @generated
     */
    void setAdditionalParams(String value);

    /**
     * Returns the value of the '<em><b>Standard SQL</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Standard SQL</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Standard SQL</em>' attribute.
     * @see #setStandardSQL(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_StandardSQL()
     * @model
     * @generated
     */
    boolean isStandardSQL();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isStandardSQL <em>Standard SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Standard SQL</em>' attribute.
     * @see #isStandardSQL()
     * @generated
     */
    void setStandardSQL(boolean value);

    /**
     * Returns the value of the '<em><b>System SQL</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>System SQL</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>System SQL</em>' attribute.
     * @see #setSystemSQL(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_SystemSQL()
     * @model
     * @generated
     */
    boolean isSystemSQL();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSystemSQL <em>System SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>System SQL</em>' attribute.
     * @see #isSystemSQL()
     * @generated
     */
    void setSystemSQL(boolean value);

    /**
     * Returns the value of the '<em><b>Cdc Conns</b></em>' containment reference. It is bidirectional and its opposite
     * is '{@link org.talend.core.model.metadata.builder.connection.CDCConnection#getConnection <em>Connection</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cdc Conns</em>' containment reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> reference to CDC definition for this connection <!-- end-model-doc
     * -->
     * 
     * @return the value of the '<em>Cdc Conns</em>' containment reference.
     * @see #setCdcConns(CDCConnection)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_CdcConns()
     * @see org.talend.core.model.metadata.builder.connection.CDCConnection#getConnection
     * @model opposite="connection" containment="true" resolveProxies="true"
     * @generated
     */
    CDCConnection getCdcConns();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcConns <em>Cdc Conns</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Cdc Conns</em>' containment reference.
     * @see #getCdcConns()
     * @generated
     */
    void setCdcConns(CDCConnection value);

    /**
     * Returns the value of the '<em><b>Cdc Type Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cdc Type Mode</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cdc Type Mode</em>' attribute.
     * @see #setCdcTypeMode(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_CdcTypeMode()
     * @model
     * @generated
     */
    String getCdcTypeMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getCdcTypeMode <em>Cdc Type Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Cdc Type Mode</em>' attribute.
     * @see #getCdcTypeMode()
     * @generated
     */
    void setCdcTypeMode(String value);

    /**
     * Returns the value of the '<em><b>SQL Mode</b></em>' attribute. The default value is <code>"true"</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>SQL Mode</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>SQL Mode</em>' attribute.
     * @see #isSetSQLMode()
     * @see #unsetSQLMode()
     * @see #setSQLMode(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_SQLMode()
     * @model default="true" unsettable="true"
     * @generated
     */
    boolean isSQLMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSQLMode <em>SQL Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>SQL Mode</em>' attribute.
     * @see #isSetSQLMode()
     * @see #unsetSQLMode()
     * @see #isSQLMode()
     * @generated
     */
    void setSQLMode(boolean value);

    /**
     * Unsets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSQLMode <em>SQL Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetSQLMode()
     * @see #isSQLMode()
     * @see #setSQLMode(boolean)
     * @generated
     */
    void unsetSQLMode();

    /**
     * Returns whether the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#isSQLMode <em>SQL Mode</em>}' attribute is set.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return whether the value of the '<em>SQL Mode</em>' attribute is set.
     * @see #unsetSQLMode()
     * @see #isSQLMode()
     * @see #setSQLMode(boolean)
     * @generated
     */
    boolean isSetSQLMode();

    /**
     * Returns the value of the '<em><b>Ui Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ui Schema</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ui Schema</em>' attribute.
     * @see #setUiSchema(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_UiSchema()
     * @model
     * @generated
     */
    String getUiSchema();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.DatabaseConnection#getUiSchema <em>Ui Schema</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Ui Schema</em>' attribute.
     * @see #getUiSchema()
     * @generated
     */
    void setUiSchema(String value);

    /**
     * Returns the value of the '<em><b>Parameters</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameters</em>' map isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameters</em>' map.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getDatabaseConnection_Parameters()
     * @model mapType="org.talend.core.model.metadata.builder.connection.AdditionalProperties<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
     * @generated
     */
    EMap<String, String> getParameters();

} // DatabaseConnection
