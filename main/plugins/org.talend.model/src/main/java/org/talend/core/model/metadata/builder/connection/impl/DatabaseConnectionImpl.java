/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Database Connection</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDatabaseType <em>Database Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDriverJarPath <em>Driver Jar Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDriverClass <em>Driver Class</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getURL <em>URL</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDbVersionString <em>Db Version String</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getServerName <em>Server Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDatasourceName <em>Datasource Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getFileFieldName <em>File Field Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getSID <em>SID</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getSqlSynthax <em>Sql Synthax</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getStringQuote <em>String Quote</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getNullChar <em>Null Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDbmsId <em>Dbms Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getProductId <em>Product Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getDBRootPath <em>DB Root Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getAdditionalParams <em>Additional Params</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#isStandardSQL <em>Standard SQL</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#isSystemSQL <em>System SQL</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getCdcConns <em>Cdc Conns</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getCdcTypeMode <em>Cdc Type Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#isSQLMode <em>SQL Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getUiSchema <em>Ui Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DatabaseConnectionImpl#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatabaseConnectionImpl extends ConnectionImpl implements DatabaseConnection {

    /**
     * The default value of the '{@link #getDatabaseType() <em>Database Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatabaseType()
     * @generated
     * @ordered
     */
    protected static final String DATABASE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDatabaseType() <em>Database Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatabaseType()
     * @generated
     * @ordered
     */
    protected String databaseType = DATABASE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getDriverJarPath() <em>Driver Jar Path</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDriverJarPath()
     * @generated
     * @ordered
     */
    protected static final String DRIVER_JAR_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDriverJarPath() <em>Driver Jar Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDriverJarPath()
     * @generated
     * @ordered
     */
    protected String driverJarPath = DRIVER_JAR_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getDriverClass() <em>Driver Class</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDriverClass()
     * @generated
     * @ordered
     */
    protected static final String DRIVER_CLASS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDriverClass() <em>Driver Class</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDriverClass()
     * @generated
     * @ordered
     */
    protected String driverClass = DRIVER_CLASS_EDEFAULT;

    /**
     * The default value of the '{@link #getURL() <em>URL</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getURL()
     * @generated
     * @ordered
     */
    protected static final String URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getURL() <em>URL</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getURL()
     * @generated
     * @ordered
     */
    protected String url = URL_EDEFAULT;

    /**
     * The default value of the '{@link #getDbVersionString() <em>Db Version String</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDbVersionString()
     * @generated
     * @ordered
     */
    protected static final String DB_VERSION_STRING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDbVersionString() <em>Db Version String</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDbVersionString()
     * @generated
     * @ordered
     */
    protected String dbVersionString = DB_VERSION_STRING_EDEFAULT;

    /**
     * The default value of the '{@link #getPort() <em>Port</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected static final String PORT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPort() <em>Port</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected String port = PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getUsername() <em>Username</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUsername()
     * @generated
     * @ordered
     */
    protected static final String USERNAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUsername() <em>Username</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUsername()
     * @generated
     * @ordered
     */
    protected String username = USERNAME_EDEFAULT;

    /**
     * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected static final String PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected String password = PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getServerName() <em>Server Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getServerName()
     * @generated
     * @ordered
     */
    protected static final String SERVER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServerName() <em>Server Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getServerName()
     * @generated
     * @ordered
     */
    protected String serverName = SERVER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getDatasourceName() <em>Datasource Name</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDatasourceName()
     * @generated
     * @ordered
     */
    protected static final String DATASOURCE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDatasourceName() <em>Datasource Name</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDatasourceName()
     * @generated
     * @ordered
     */
    protected String datasourceName = DATASOURCE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getFileFieldName() <em>File Field Name</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getFileFieldName()
     * @generated
     * @ordered
     */
    protected static final String FILE_FIELD_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileFieldName() <em>File Field Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileFieldName()
     * @generated
     * @ordered
     */
    protected String fileFieldName = FILE_FIELD_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSID() <em>SID</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getSID()
     * @generated
     * @ordered
     */
    protected static final String SID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSID() <em>SID</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getSID()
     * @generated
     * @ordered
     */
    protected String sid = SID_EDEFAULT;

    /**
     * The default value of the '{@link #getSqlSynthax() <em>Sql Synthax</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSqlSynthax()
     * @generated
     * @ordered
     */
    protected static final String SQL_SYNTHAX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSqlSynthax() <em>Sql Synthax</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getSqlSynthax()
     * @generated
     * @ordered
     */
    protected String sqlSynthax = SQL_SYNTHAX_EDEFAULT;

    /**
     * The default value of the '{@link #getStringQuote() <em>String Quote</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStringQuote()
     * @generated
     * @ordered
     */
    protected static final String STRING_QUOTE_EDEFAULT = "\""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getStringQuote() <em>String Quote</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getStringQuote()
     * @generated
     * @ordered
     */
    protected String stringQuote = STRING_QUOTE_EDEFAULT;

    /**
     * The default value of the '{@link #getNullChar() <em>Null Char</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getNullChar()
     * @generated
     * @ordered
     */
    protected static final String NULL_CHAR_EDEFAULT = "000"; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getNullChar() <em>Null Char</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getNullChar()
     * @generated
     * @ordered
     */
    protected String nullChar = NULL_CHAR_EDEFAULT;

    /**
     * The default value of the '{@link #getDbmsId() <em>Dbms Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDbmsId()
     * @generated
     * @ordered
     */
    protected static final String DBMS_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDbmsId() <em>Dbms Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDbmsId()
     * @generated
     * @ordered
     */
    protected String dbmsId = DBMS_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getProductId() <em>Product Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProductId()
     * @generated
     * @ordered
     */
    protected static final String PRODUCT_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProductId() <em>Product Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProductId()
     * @generated
     * @ordered
     */
    protected String productId = PRODUCT_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getDBRootPath() <em>DB Root Path</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDBRootPath()
     * @generated
     * @ordered
     */
    protected static final String DB_ROOT_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDBRootPath() <em>DB Root Path</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDBRootPath()
     * @generated
     * @ordered
     */
    protected String dbRootPath = DB_ROOT_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getAdditionalParams() <em>Additional Params</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAdditionalParams()
     * @generated
     * @ordered
     */
    protected static final String ADDITIONAL_PARAMS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAdditionalParams() <em>Additional Params</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAdditionalParams()
     * @generated
     * @ordered
     */
    protected String additionalParams = ADDITIONAL_PARAMS_EDEFAULT;

    /**
     * The default value of the '{@link #isStandardSQL() <em>Standard SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isStandardSQL()
     * @generated
     * @ordered
     */
    protected static final boolean STANDARD_SQL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStandardSQL() <em>Standard SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isStandardSQL()
     * @generated
     * @ordered
     */
    protected boolean standardSQL = STANDARD_SQL_EDEFAULT;

    /**
     * The default value of the '{@link #isSystemSQL() <em>System SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSystemSQL()
     * @generated
     * @ordered
     */
    protected static final boolean SYSTEM_SQL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSystemSQL() <em>System SQL</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSystemSQL()
     * @generated
     * @ordered
     */
    protected boolean systemSQL = SYSTEM_SQL_EDEFAULT;

    /**
     * The cached value of the '{@link #getCdcConns() <em>Cdc Conns</em>}' containment reference.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getCdcConns()
     * @generated
     * @ordered
     */
    protected CDCConnection cdcConns;

    /**
     * The default value of the '{@link #getCdcTypeMode() <em>Cdc Type Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCdcTypeMode()
     * @generated
     * @ordered
     */
    protected static final String CDC_TYPE_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCdcTypeMode() <em>Cdc Type Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCdcTypeMode()
     * @generated
     * @ordered
     */
    protected String cdcTypeMode = CDC_TYPE_MODE_EDEFAULT;

    /**
     * The default value of the '{@link #isSQLMode() <em>SQL Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSQLMode()
     * @generated
     * @ordered
     */
    protected static final boolean SQL_MODE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isSQLMode() <em>SQL Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSQLMode()
     * @generated
     * @ordered
     */
    protected boolean sqlMode = SQL_MODE_EDEFAULT;

    /**
     * This is true if the SQL Mode attribute has been set.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sqlModeESet;

    /**
     * The default value of the '{@link #getUiSchema() <em>Ui Schema</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUiSchema()
     * @generated
     * @ordered
     */
    protected static final String UI_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUiSchema() <em>Ui Schema</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUiSchema()
     * @generated
     * @ordered
     */
    protected String uiSchema = UI_SCHEMA_EDEFAULT;

    /**
     * The cached value of the '{@link #getParameters() <em>Parameters</em>}' map.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getParameters()
     * @generated
     * @ordered
     */
    protected EMap<String, String> parameters;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected DatabaseConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.DATABASE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDatabaseType(String newDatabaseType) {
        String oldDatabaseType = databaseType;
        databaseType = newDatabaseType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE,
                    oldDatabaseType, databaseType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDriverJarPath() {
        return driverJarPath;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDriverJarPath(String newDriverJarPath) {
        String oldDriverJarPath = driverJarPath;
        driverJarPath = newDriverJarPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH,
                    oldDriverJarPath, driverJarPath));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDriverClass(String newDriverClass) {
        String oldDriverClass = driverClass;
        driverClass = newDriverClass;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS,
                    oldDriverClass, driverClass));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getURL() {
        return url;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setURL(String newURL) {
        String oldURL = url;
        url = newURL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__URL, oldURL, url));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDbVersionString() {
        return dbVersionString;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDbVersionString(String newDbVersionString) {
        String oldDbVersionString = dbVersionString;
        dbVersionString = newDbVersionString;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING,
                    oldDbVersionString, dbVersionString));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getPort() {
        return port;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPort(String newPort) {
        String oldPort = port;
        port = newPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__PORT, oldPort, port));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getUsername() {
        return username;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUsername(String newUsername) {
        String oldUsername = username;
        username = newUsername;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__USERNAME, oldUsername,
                    username));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getPassword() {
        return password;
    }

    public String getRawPassword() {
        return getValue(password, false);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPassword(String newPassword) {
        String oldPassword = password;
        password = newPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__PASSWORD, oldPassword,
                    password));
    }

    public void setRawPassword(String value) {
        setPassword(getValue(value, true));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setServerName(String newServerName) {
        String oldServerName = serverName;
        serverName = newServerName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME,
                    oldServerName, serverName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDatasourceName() {
        return datasourceName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDatasourceName(String newDatasourceName) {
        String oldDatasourceName = datasourceName;
        datasourceName = newDatasourceName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME,
                    oldDatasourceName, datasourceName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getFileFieldName() {
        return fileFieldName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFileFieldName(String newFileFieldName) {
        String oldFileFieldName = fileFieldName;
        fileFieldName = newFileFieldName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME,
                    oldFileFieldName, fileFieldName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getSID() {
        return sid;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSID(String newSID) {
        String oldSID = sid;
        sid = newSID;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__SID, oldSID, sid));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getSqlSynthax() {
        return sqlSynthax;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSqlSynthax(String newSqlSynthax) {
        String oldSqlSynthax = sqlSynthax;
        sqlSynthax = newSqlSynthax;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX,
                    oldSqlSynthax, sqlSynthax));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getStringQuote() {
        return stringQuote;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setStringQuote(String newStringQuote) {
        String oldStringQuote = stringQuote;
        stringQuote = newStringQuote;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE,
                    oldStringQuote, stringQuote));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getNullChar() {
        return nullChar;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setNullChar(String newNullChar) {
        String oldNullChar = nullChar;
        nullChar = newNullChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR, oldNullChar,
                    nullChar));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDbmsId() {
        return dbmsId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDbmsId(String newDbmsId) {
        String oldDbmsId = dbmsId;
        dbmsId = newDbmsId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DBMS_ID, oldDbmsId,
                    dbmsId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getProductId() {
        return productId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setProductId(String newProductId) {
        String oldProductId = productId;
        productId = newProductId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID,
                    oldProductId, productId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDBRootPath() {
        return dbRootPath;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDBRootPath(String newDBRootPath) {
        String oldDBRootPath = dbRootPath;
        dbRootPath = newDBRootPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH,
                    oldDBRootPath, dbRootPath));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getAdditionalParams() {
        return additionalParams;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAdditionalParams(String newAdditionalParams) {
        String oldAdditionalParams = additionalParams;
        additionalParams = newAdditionalParams;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS,
                    oldAdditionalParams, additionalParams));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isStandardSQL() {
        return standardSQL;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setStandardSQL(boolean newStandardSQL) {
        boolean oldStandardSQL = standardSQL;
        standardSQL = newStandardSQL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL,
                    oldStandardSQL, standardSQL));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSystemSQL() {
        return systemSQL;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSystemSQL(boolean newSystemSQL) {
        boolean oldSystemSQL = systemSQL;
        systemSQL = newSystemSQL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL,
                    oldSystemSQL, systemSQL));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CDCConnection getCdcConns() {
        if (cdcConns != null && cdcConns.eIsProxy()) {
            InternalEObject oldCdcConns = (InternalEObject) cdcConns;
            cdcConns = (CDCConnection) eResolveProxy(oldCdcConns);
            if (cdcConns != oldCdcConns) {
                InternalEObject newCdcConns = (InternalEObject) cdcConns;
                NotificationChain msgs = oldCdcConns.eInverseRemove(this, ConnectionPackage.CDC_CONNECTION__CONNECTION,
                        CDCConnection.class, null);
                if (newCdcConns.eInternalContainer() == null) {
                    msgs = newCdcConns.eInverseAdd(this, ConnectionPackage.CDC_CONNECTION__CONNECTION, CDCConnection.class, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS,
                            oldCdcConns, cdcConns));
            }
        }
        return cdcConns;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CDCConnection basicGetCdcConns() {
        return cdcConns;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCdcConns(CDCConnection newCdcConns, NotificationChain msgs) {
        CDCConnection oldCdcConns = cdcConns;
        cdcConns = newCdcConns;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS, oldCdcConns, newCdcConns);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setCdcConns(CDCConnection newCdcConns) {
        if (newCdcConns != cdcConns) {
            NotificationChain msgs = null;
            if (cdcConns != null)
                msgs = ((InternalEObject) cdcConns).eInverseRemove(this, ConnectionPackage.CDC_CONNECTION__CONNECTION,
                        CDCConnection.class, msgs);
            if (newCdcConns != null)
                msgs = ((InternalEObject) newCdcConns).eInverseAdd(this, ConnectionPackage.CDC_CONNECTION__CONNECTION,
                        CDCConnection.class, msgs);
            msgs = basicSetCdcConns(newCdcConns, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS, newCdcConns,
                    newCdcConns));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getCdcTypeMode() {
        return cdcTypeMode;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setCdcTypeMode(String newCdcTypeMode) {
        String oldCdcTypeMode = cdcTypeMode;
        cdcTypeMode = newCdcTypeMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE,
                    oldCdcTypeMode, cdcTypeMode));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSQLMode() {
        return sqlMode;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSQLMode(boolean newSQLMode) {
        boolean oldSQLMode = sqlMode;
        sqlMode = newSQLMode;
        boolean oldSQLModeESet = sqlModeESet;
        sqlModeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__SQL_MODE, oldSQLMode,
                    sqlMode, !oldSQLModeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void unsetSQLMode() {
        boolean oldSQLMode = sqlMode;
        boolean oldSQLModeESet = sqlModeESet;
        sqlMode = SQL_MODE_EDEFAULT;
        sqlModeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ConnectionPackage.DATABASE_CONNECTION__SQL_MODE, oldSQLMode,
                    SQL_MODE_EDEFAULT, oldSQLModeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSQLMode() {
        return sqlModeESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getUiSchema() {
        return uiSchema;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUiSchema(String newUiSchema) {
        String oldUiSchema = uiSchema;
        uiSchema = newUiSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA, oldUiSchema,
                    uiSchema));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getParameters() {
        if (parameters == null) {
            parameters = new EcoreEMap<String, String>(ConnectionPackage.Literals.ADDITIONAL_PROPERTIES,
                    AdditionalPropertiesImpl.class, this, ConnectionPackage.DATABASE_CONNECTION__PARAMETERS);
        }
        return parameters;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            if (cdcConns != null)
                msgs = ((InternalEObject) cdcConns).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS, null, msgs);
            return basicSetCdcConns((CDCConnection) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            return basicSetCdcConns(null, msgs);
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            return ((InternalEList<?>) getParameters()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGetGen(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE:
            return getDatabaseType();
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH:
            return getDriverJarPath();
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS:
            return getDriverClass();
        case ConnectionPackage.DATABASE_CONNECTION__URL:
            return getURL();
        case ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING:
            return getDbVersionString();
        case ConnectionPackage.DATABASE_CONNECTION__PORT:
            return getPort();
        case ConnectionPackage.DATABASE_CONNECTION__USERNAME:
            return getUsername();
        case ConnectionPackage.DATABASE_CONNECTION__PASSWORD:
            return getPassword();
        case ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME:
            return getServerName();
        case ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME:
            return getDatasourceName();
        case ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME:
            return getFileFieldName();
        case ConnectionPackage.DATABASE_CONNECTION__SID:
            return getSID();
        case ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX:
            return getSqlSynthax();
        case ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE:
            return getStringQuote();
        case ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR:
            return getNullChar();
        case ConnectionPackage.DATABASE_CONNECTION__DBMS_ID:
            return getDbmsId();
        case ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID:
            return getProductId();
        case ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH:
            return getDBRootPath();
        case ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS:
            return getAdditionalParams();
        case ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL:
            return isStandardSQL();
        case ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL:
            return isSystemSQL();
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            if (resolve)
                return getCdcConns();
            return basicGetCdcConns();
        case ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE:
            return getCdcTypeMode();
        case ConnectionPackage.DATABASE_CONNECTION__SQL_MODE:
            return isSQLMode();
        case ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA:
            return getUiSchema();
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            if (coreType)
                return getParameters();
            else
                return getParameters().map();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE:
            return getDatabaseType();
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH:
            return getDriverJarPath();
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS:
            return getDriverClass();
        case ConnectionPackage.DATABASE_CONNECTION__URL:
            return getURL();
        case ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING:
            return getDbVersionString();
        case ConnectionPackage.DATABASE_CONNECTION__PORT:
            return getPort();
        case ConnectionPackage.DATABASE_CONNECTION__USERNAME:
            return getUsername();
        case ConnectionPackage.DATABASE_CONNECTION__PASSWORD:
            return getPassword();
        case ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME:
            return getServerName();
        case ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME:
            return getDatasourceName();
        case ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME:
            return getFileFieldName();
        case ConnectionPackage.DATABASE_CONNECTION__SID:
            return getSID();
        case ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX:
            return getSqlSynthax();
        case ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE:
            return getStringQuote();
        case ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR:
            return getNullChar();
        case ConnectionPackage.DATABASE_CONNECTION__DBMS_ID:
            return getDbmsId();
        case ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID:
            return getProductId();
        case ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH:
            return getDBRootPath();
        case ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS:
            return getAdditionalParams();
        case ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL:
            return isStandardSQL();
        case ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL:
            return isSystemSQL();
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            if (resolve)
                return getCdcConns();
            return basicGetCdcConns();
        case ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE:
            return getCdcTypeMode();
        case ConnectionPackage.DATABASE_CONNECTION__SQL_MODE:
            return isSQLMode();
        case ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA:
            return getUiSchema();
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            if (coreType)
                return getParameters();
            else
                return getParameters().map();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE:
            setDatabaseType((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH:
            setDriverJarPath((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS:
            setDriverClass((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__URL:
            setURL((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING:
            setDbVersionString((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PORT:
            setPort((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__USERNAME:
            setUsername((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PASSWORD:
            setPassword((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME:
            setServerName((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME:
            setDatasourceName((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME:
            setFileFieldName((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SID:
            setSID((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX:
            setSqlSynthax((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE:
            setStringQuote((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR:
            setNullChar((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DBMS_ID:
            setDbmsId((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID:
            setProductId((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH:
            setDBRootPath((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS:
            setAdditionalParams((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL:
            setStandardSQL((Boolean) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL:
            setSystemSQL((Boolean) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            setCdcConns((CDCConnection) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE:
            setCdcTypeMode((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SQL_MODE:
            setSQLMode((Boolean) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA:
            setUiSchema((String) newValue);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            ((EStructuralFeature.Setting) getParameters()).set(newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE:
            setDatabaseType(DATABASE_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH:
            setDriverJarPath(DRIVER_JAR_PATH_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS:
            setDriverClass(DRIVER_CLASS_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__URL:
            setURL(URL_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING:
            setDbVersionString(DB_VERSION_STRING_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PORT:
            setPort(PORT_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__USERNAME:
            setUsername(USERNAME_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PASSWORD:
            setPassword(PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME:
            setServerName(SERVER_NAME_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME:
            setDatasourceName(DATASOURCE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME:
            setFileFieldName(FILE_FIELD_NAME_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SID:
            setSID(SID_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX:
            setSqlSynthax(SQL_SYNTHAX_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE:
            setStringQuote(STRING_QUOTE_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR:
            setNullChar(NULL_CHAR_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DBMS_ID:
            setDbmsId(DBMS_ID_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID:
            setProductId(PRODUCT_ID_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH:
            setDBRootPath(DB_ROOT_PATH_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS:
            setAdditionalParams(ADDITIONAL_PARAMS_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL:
            setStandardSQL(STANDARD_SQL_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL:
            setSystemSQL(SYSTEM_SQL_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            setCdcConns((CDCConnection) null);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE:
            setCdcTypeMode(CDC_TYPE_MODE_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__SQL_MODE:
            unsetSQLMode();
            return;
        case ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA:
            setUiSchema(UI_SCHEMA_EDEFAULT);
            return;
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            getParameters().clear();
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.DATABASE_CONNECTION__DATABASE_TYPE:
            return DATABASE_TYPE_EDEFAULT == null ? databaseType != null : !DATABASE_TYPE_EDEFAULT.equals(databaseType);
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_JAR_PATH:
            return DRIVER_JAR_PATH_EDEFAULT == null ? driverJarPath != null : !DRIVER_JAR_PATH_EDEFAULT.equals(driverJarPath);
        case ConnectionPackage.DATABASE_CONNECTION__DRIVER_CLASS:
            return DRIVER_CLASS_EDEFAULT == null ? driverClass != null : !DRIVER_CLASS_EDEFAULT.equals(driverClass);
        case ConnectionPackage.DATABASE_CONNECTION__URL:
            return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
        case ConnectionPackage.DATABASE_CONNECTION__DB_VERSION_STRING:
            return DB_VERSION_STRING_EDEFAULT == null ? dbVersionString != null : !DB_VERSION_STRING_EDEFAULT
                    .equals(dbVersionString);
        case ConnectionPackage.DATABASE_CONNECTION__PORT:
            return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
        case ConnectionPackage.DATABASE_CONNECTION__USERNAME:
            return USERNAME_EDEFAULT == null ? username != null : !USERNAME_EDEFAULT.equals(username);
        case ConnectionPackage.DATABASE_CONNECTION__PASSWORD:
            return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
        case ConnectionPackage.DATABASE_CONNECTION__SERVER_NAME:
            return SERVER_NAME_EDEFAULT == null ? serverName != null : !SERVER_NAME_EDEFAULT.equals(serverName);
        case ConnectionPackage.DATABASE_CONNECTION__DATASOURCE_NAME:
            return DATASOURCE_NAME_EDEFAULT == null ? datasourceName != null : !DATASOURCE_NAME_EDEFAULT.equals(datasourceName);
        case ConnectionPackage.DATABASE_CONNECTION__FILE_FIELD_NAME:
            return FILE_FIELD_NAME_EDEFAULT == null ? fileFieldName != null : !FILE_FIELD_NAME_EDEFAULT.equals(fileFieldName);
        case ConnectionPackage.DATABASE_CONNECTION__SID:
            return SID_EDEFAULT == null ? sid != null : !SID_EDEFAULT.equals(sid);
        case ConnectionPackage.DATABASE_CONNECTION__SQL_SYNTHAX:
            return SQL_SYNTHAX_EDEFAULT == null ? sqlSynthax != null : !SQL_SYNTHAX_EDEFAULT.equals(sqlSynthax);
        case ConnectionPackage.DATABASE_CONNECTION__STRING_QUOTE:
            return STRING_QUOTE_EDEFAULT == null ? stringQuote != null : !STRING_QUOTE_EDEFAULT.equals(stringQuote);
        case ConnectionPackage.DATABASE_CONNECTION__NULL_CHAR:
            return NULL_CHAR_EDEFAULT == null ? nullChar != null : !NULL_CHAR_EDEFAULT.equals(nullChar);
        case ConnectionPackage.DATABASE_CONNECTION__DBMS_ID:
            return DBMS_ID_EDEFAULT == null ? dbmsId != null : !DBMS_ID_EDEFAULT.equals(dbmsId);
        case ConnectionPackage.DATABASE_CONNECTION__PRODUCT_ID:
            return PRODUCT_ID_EDEFAULT == null ? productId != null : !PRODUCT_ID_EDEFAULT.equals(productId);
        case ConnectionPackage.DATABASE_CONNECTION__DB_ROOT_PATH:
            return DB_ROOT_PATH_EDEFAULT == null ? dbRootPath != null : !DB_ROOT_PATH_EDEFAULT.equals(dbRootPath);
        case ConnectionPackage.DATABASE_CONNECTION__ADDITIONAL_PARAMS:
            return ADDITIONAL_PARAMS_EDEFAULT == null ? additionalParams != null : !ADDITIONAL_PARAMS_EDEFAULT
                    .equals(additionalParams);
        case ConnectionPackage.DATABASE_CONNECTION__STANDARD_SQL:
            return standardSQL != STANDARD_SQL_EDEFAULT;
        case ConnectionPackage.DATABASE_CONNECTION__SYSTEM_SQL:
            return systemSQL != SYSTEM_SQL_EDEFAULT;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS:
            return cdcConns != null;
        case ConnectionPackage.DATABASE_CONNECTION__CDC_TYPE_MODE:
            return CDC_TYPE_MODE_EDEFAULT == null ? cdcTypeMode != null : !CDC_TYPE_MODE_EDEFAULT.equals(cdcTypeMode);
        case ConnectionPackage.DATABASE_CONNECTION__SQL_MODE:
            return isSetSQLMode();
        case ConnectionPackage.DATABASE_CONNECTION__UI_SCHEMA:
            return UI_SCHEMA_EDEFAULT == null ? uiSchema != null : !UI_SCHEMA_EDEFAULT.equals(uiSchema);
        case ConnectionPackage.DATABASE_CONNECTION__PARAMETERS:
            return parameters != null && !parameters.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (DatabaseType: ");
        result.append(databaseType);
        result.append(", DriverJarPath: ");
        result.append(driverJarPath);
        result.append(", DriverClass: ");
        result.append(driverClass);
        result.append(", URL: ");
        result.append(url);
        result.append(", dbVersionString: ");
        result.append(dbVersionString);
        result.append(", Port: ");
        result.append(port);
        result.append(", Username: ");
        result.append(username);
        result.append(", Password: ");
        result.append(password);
        result.append(", ServerName: ");
        result.append(serverName);
        result.append(", DatasourceName: ");
        result.append(datasourceName);
        result.append(", FileFieldName: ");
        result.append(fileFieldName);
        result.append(", SID: ");
        result.append(sid);
        result.append(", SqlSynthax: ");
        result.append(sqlSynthax);
        result.append(", StringQuote: ");
        result.append(stringQuote);
        result.append(", NullChar: ");
        result.append(nullChar);
        result.append(", DbmsId: ");
        result.append(dbmsId);
        result.append(", ProductId: ");
        result.append(productId);
        result.append(", DBRootPath: ");
        result.append(dbRootPath);
        result.append(", AdditionalParams: ");
        result.append(additionalParams);
        result.append(", StandardSQL: ");
        result.append(standardSQL);
        result.append(", SystemSQL: ");
        result.append(systemSQL);
        result.append(", cdcTypeMode: ");
        result.append(cdcTypeMode);
        result.append(", SQLMode: ");
        if (sqlModeESet)
            result.append(sqlMode);
        else
            result.append("<unset>");
        result.append(", UiSchema: ");
        result.append(uiSchema);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getConnectionTypeName()
     */
    public String getConnectionTypeName() {
        return getDatabaseType();
    }

} // DatabaseConnectionImpl
