/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnectionProtocol;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>MDM Connection</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getUsername <em>Username</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getServer <em>Server</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getUniverse <em>Universe</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getDatamodel <em>Datamodel</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getDatacluster <em>Datacluster</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getSchemas <em>Schemas</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getProtocol <em>Protocol</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getContext <em>Context</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.MDMConnectionImpl#getServerUrl <em>Server Url</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MDMConnectionImpl extends ConnectionImpl implements MDMConnection {

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
     * The default value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected static final String SERVER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected String server = SERVER_EDEFAULT;

    /**
     * The default value of the '{@link #getUniverse() <em>Universe</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUniverse()
     * @generated
     * @ordered
     */
    protected static final String UNIVERSE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUniverse() <em>Universe</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUniverse()
     * @generated
     * @ordered
     */
    protected String universe = UNIVERSE_EDEFAULT;

    /**
     * The default value of the '{@link #getDatamodel() <em>Datamodel</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatamodel()
     * @generated
     * @ordered
     */
    protected static final String DATAMODEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDatamodel() <em>Datamodel</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatamodel()
     * @generated
     * @ordered
     */
    protected String datamodel = DATAMODEL_EDEFAULT;

    /**
     * The default value of the '{@link #getDatacluster() <em>Datacluster</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatacluster()
     * @generated
     * @ordered
     */
    protected static final String DATACLUSTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDatacluster() <em>Datacluster</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatacluster()
     * @generated
     * @ordered
     */
    protected String datacluster = DATACLUSTER_EDEFAULT;

    /**
     * The cached value of the '{@link #getSchemas() <em>Schemas</em>}' containment reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getSchemas()
     * @generated
     * @ordered
     */
    protected EList<Concept> schemas;

    /**
     * The default value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProtocol()
     * @generated
     * @ordered
     */
    protected static final MDMConnectionProtocol PROTOCOL_EDEFAULT = MDMConnectionProtocol.HTTP;

    /**
     * The cached value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProtocol()
     * @generated
     * @ordered
     */
    protected MDMConnectionProtocol protocol = PROTOCOL_EDEFAULT;

    /**
     * The default value of the '{@link #getContext() <em>Context</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getContext()
     * @generated
     * @ordered
     */
    protected static final String CONTEXT_EDEFAULT = "talend/TalendPort";

    /**
     * The cached value of the '{@link #getContext() <em>Context</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getContext()
     * @generated
     * @ordered
     */
    protected String context = CONTEXT_EDEFAULT;

    /**
     * The default value of the '{@link #getServerUrl() <em>Server Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServerUrl()
     * @generated
     * @ordered
     */
    protected static final String SERVER_URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServerUrl() <em>Server Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServerUrl()
     * @generated
     * @ordered
     */
    protected String serverUrl = SERVER_URL_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MDMConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.MDM_CONNECTION;
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__USERNAME, oldUsername,
                    username));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPassword(String newPassword) {
        String oldPassword = password;
        password = newPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__PASSWORD, oldPassword,
                    password));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__PORT, oldPort, port));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getServer() {
        return server;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setServer(String newServer) {
        String oldServer = server;
        server = newServer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__SERVER, oldServer, server));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getUniverse() {
        return universe;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUniverse(String newUniverse) {
        String oldUniverse = universe;
        universe = newUniverse;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__UNIVERSE, oldUniverse,
                    universe));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDatamodel() {
        return datamodel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDatamodel(String newDatamodel) {
        String oldDatamodel = datamodel;
        datamodel = newDatamodel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__DATAMODEL, oldDatamodel,
                    datamodel));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDatacluster() {
        return datacluster;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDatacluster(String newDatacluster) {
        String oldDatacluster = datacluster;
        datacluster = newDatacluster;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__DATACLUSTER, oldDatacluster,
                    datacluster));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Concept> getSchemas() {
        if (schemas == null) {
            schemas = new EObjectContainmentEList.Resolving<Concept>(Concept.class, this,
                    ConnectionPackage.MDM_CONNECTION__SCHEMAS);
        }
        return schemas;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MDMConnectionProtocol getProtocol() {
        return protocol;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setProtocol(MDMConnectionProtocol newProtocol) {
        MDMConnectionProtocol oldProtocol = protocol;
        protocol = newProtocol == null ? PROTOCOL_EDEFAULT : newProtocol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__PROTOCOL, oldProtocol,
                    protocol));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getConnectionString() {
        return getProtocol().getLiteral() + "://" + getServer() + (getPort() != null ? ":" + getPort() : "") + "/" + getContext(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getContext() {
        return context;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setContext(String newContext) {
        String oldContext = context;
        context = newContext;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__CONTEXT, oldContext, context));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServerUrl(String newServerUrl) {
        String oldServerUrl = serverUrl;
        serverUrl = newServerUrl;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.MDM_CONNECTION__SERVER_URL, oldServerUrl,
                    serverUrl));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.MDM_CONNECTION__SCHEMAS:
            return ((InternalEList<?>) getSchemas()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.MDM_CONNECTION__USERNAME:
            return getUsername();
        case ConnectionPackage.MDM_CONNECTION__PASSWORD:
            return getPassword();
        case ConnectionPackage.MDM_CONNECTION__PORT:
            return getPort();
        case ConnectionPackage.MDM_CONNECTION__SERVER:
            return getServer();
        case ConnectionPackage.MDM_CONNECTION__UNIVERSE:
            return getUniverse();
        case ConnectionPackage.MDM_CONNECTION__DATAMODEL:
            return getDatamodel();
        case ConnectionPackage.MDM_CONNECTION__DATACLUSTER:
            return getDatacluster();
        case ConnectionPackage.MDM_CONNECTION__SCHEMAS:
            return getSchemas();
        case ConnectionPackage.MDM_CONNECTION__PROTOCOL:
            return getProtocol();
        case ConnectionPackage.MDM_CONNECTION__CONTEXT:
            return getContext();
        case ConnectionPackage.MDM_CONNECTION__SERVER_URL:
            return getServerUrl();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.MDM_CONNECTION__USERNAME:
            setUsername((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__PASSWORD:
            setPassword((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__PORT:
            setPort((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__SERVER:
            setServer((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__UNIVERSE:
            setUniverse((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__DATAMODEL:
            setDatamodel((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__DATACLUSTER:
            setDatacluster((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__SCHEMAS:
            getSchemas().clear();
            getSchemas().addAll((Collection<? extends Concept>) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__PROTOCOL:
            setProtocol((MDMConnectionProtocol) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__CONTEXT:
            setContext((String) newValue);
            return;
        case ConnectionPackage.MDM_CONNECTION__SERVER_URL:
            setServerUrl((String) newValue);
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
        case ConnectionPackage.MDM_CONNECTION__USERNAME:
            setUsername(USERNAME_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__PASSWORD:
            setPassword(PASSWORD_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__PORT:
            setPort(PORT_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__SERVER:
            setServer(SERVER_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__UNIVERSE:
            setUniverse(UNIVERSE_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__DATAMODEL:
            setDatamodel(DATAMODEL_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__DATACLUSTER:
            setDatacluster(DATACLUSTER_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__SCHEMAS:
            getSchemas().clear();
            return;
        case ConnectionPackage.MDM_CONNECTION__PROTOCOL:
            setProtocol(PROTOCOL_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__CONTEXT:
            setContext(CONTEXT_EDEFAULT);
            return;
        case ConnectionPackage.MDM_CONNECTION__SERVER_URL:
            setServerUrl(SERVER_URL_EDEFAULT);
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
        case ConnectionPackage.MDM_CONNECTION__USERNAME:
            return USERNAME_EDEFAULT == null ? username != null : !USERNAME_EDEFAULT.equals(username);
        case ConnectionPackage.MDM_CONNECTION__PASSWORD:
            return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
        case ConnectionPackage.MDM_CONNECTION__PORT:
            return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
        case ConnectionPackage.MDM_CONNECTION__SERVER:
            return SERVER_EDEFAULT == null ? server != null : !SERVER_EDEFAULT.equals(server);
        case ConnectionPackage.MDM_CONNECTION__UNIVERSE:
            return UNIVERSE_EDEFAULT == null ? universe != null : !UNIVERSE_EDEFAULT.equals(universe);
        case ConnectionPackage.MDM_CONNECTION__DATAMODEL:
            return DATAMODEL_EDEFAULT == null ? datamodel != null : !DATAMODEL_EDEFAULT.equals(datamodel);
        case ConnectionPackage.MDM_CONNECTION__DATACLUSTER:
            return DATACLUSTER_EDEFAULT == null ? datacluster != null : !DATACLUSTER_EDEFAULT.equals(datacluster);
        case ConnectionPackage.MDM_CONNECTION__SCHEMAS:
            return schemas != null && !schemas.isEmpty();
        case ConnectionPackage.MDM_CONNECTION__PROTOCOL:
            return protocol != PROTOCOL_EDEFAULT;
        case ConnectionPackage.MDM_CONNECTION__CONTEXT:
            return CONTEXT_EDEFAULT == null ? context != null : !CONTEXT_EDEFAULT.equals(context);
        case ConnectionPackage.MDM_CONNECTION__SERVER_URL:
            return SERVER_URL_EDEFAULT == null ? serverUrl != null : !SERVER_URL_EDEFAULT.equals(serverUrl);
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
        result.append(" (Username: ");
        result.append(username);
        result.append(", Password: ");
        result.append(password);
        result.append(", Port: ");
        result.append(port);
        result.append(", Server: ");
        result.append(server);
        result.append(", Universe: ");
        result.append(universe);
        result.append(", Datamodel: ");
        result.append(datamodel);
        result.append(", Datacluster: ");
        result.append(datacluster);
        result.append(", protocol: ");
        result.append(protocol);
        result.append(", context: ");
        result.append(context);
        result.append(", serverUrl: ");
        result.append(serverUrl);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getPathname() {
        return pathname == null ? this.getConnectionString() : pathname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getConnectionTypeName()
     */
    public String getConnectionTypeName() {
        return "MDM"; //$NON-NLS-1$
    }
} // MDMConnectionImpl
