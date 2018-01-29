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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.utils.security.CryptoHelper;
import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.foundation.softwaredeployment.DeployedComponent;
import orgomg.cwm.foundation.softwaredeployment.DeployedSoftwareSystem;
import orgomg.cwm.foundation.softwaredeployment.Machine;
import orgomg.cwm.foundation.softwaredeployment.ProviderConnection;
import orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage;
import orgomg.cwm.objectmodel.core.CorePackage;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Connection</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getOwnedElement <em>Owned Element</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getImportedElement <em>Imported Element</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getDataManager <em>Data Manager</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getPathname <em>Pathname</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getMachine <em>Machine</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getDeployedSoftwareSystem <em>Deployed Software System</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getComponent <em>Component</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#isIsCaseSensitive <em>Is Case Sensitive</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getClientConnection <em>Client Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getDataPackage <em>Data Package</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getResourceConnection <em>Resource Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getQueries <em>Queries</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#isContextMode <em>Context Mode</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getContextId <em>Context Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getContextName <em>Context Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getCompProperties <em>Comp Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectionImpl extends AbstractMetadataObjectImpl implements Connection {

    /**
     * The cached value of the '{@link #getOwnedElement() <em>Owned Element</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getOwnedElement()
     * @generated
     * @ordered
     */
    protected EList<ModelElement> ownedElement;

    /**
     * The cached value of the '{@link #getImportedElement() <em>Imported Element</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getImportedElement()
     * @generated
     * @ordered
     */
    protected EList<ModelElement> importedElement;

    /**
     * The cached value of the '{@link #getDataManager() <em>Data Manager</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataManager()
     * @generated
     * @ordered
     */
    protected EList<DataManager> dataManager;

    /**
     * The default value of the '{@link #getPathname() <em>Pathname</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPathname()
     * @generated
     * @ordered
     */
    protected static final String PATHNAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPathname() <em>Pathname</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPathname()
     * @generated
     * @ordered
     */
    protected String pathname = PATHNAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getDeployedSoftwareSystem() <em>Deployed Software System</em>}' reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getDeployedSoftwareSystem()
     * @generated
     * @ordered
     */
    protected EList<DeployedSoftwareSystem> deployedSoftwareSystem;

    /**
     * The cached value of the '{@link #getComponent() <em>Component</em>}' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComponent()
     * @generated
     * @ordered
     */
    protected Component component;

    /**
     * The default value of the '{@link #isIsCaseSensitive() <em>Is Case Sensitive</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #isIsCaseSensitive()
     * @generated
     * @ordered
     */
    protected static final boolean IS_CASE_SENSITIVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsCaseSensitive() <em>Is Case Sensitive</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #isIsCaseSensitive()
     * @generated
     * @ordered
     */
    protected boolean isCaseSensitive = IS_CASE_SENSITIVE_EDEFAULT;

    /**
     * The cached value of the '{@link #getClientConnection() <em>Client Connection</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getClientConnection()
     * @generated
     * @ordered
     */
    protected EList<ProviderConnection> clientConnection;

    /**
     * The cached value of the '{@link #getDataPackage() <em>Data Package</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataPackage()
     * @generated
     * @ordered
     */
    protected EList<orgomg.cwm.objectmodel.core.Package> dataPackage;

    /**
     * The cached value of the '{@link #getResourceConnection() <em>Resource Connection</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getResourceConnection()
     * @generated
     * @ordered
     */
    protected EList<ProviderConnection> resourceConnection;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getQueries() <em>Queries</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueries()
     * @generated
     * @ordered
     */
    protected QueriesConnection queries;

    /**
     * The default value of the '{@link #isContextMode() <em>Context Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isContextMode()
     * @generated
     * @ordered
     */
    protected static final boolean CONTEXT_MODE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isContextMode() <em>Context Mode</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isContextMode()
     * @generated
     * @ordered
     */
    protected boolean contextMode = CONTEXT_MODE_EDEFAULT;

    /**
     * The default value of the '{@link #getContextId() <em>Context Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getContextId()
     * @generated
     * @ordered
     */
    protected static final String CONTEXT_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getContextId() <em>Context Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getContextId()
     * @generated
     * @ordered
     */
    protected String contextId = CONTEXT_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getContextName() <em>Context Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContextName()
     * @generated
     * @ordered
     */
    protected static final String CONTEXT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getContextName() <em>Context Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getContextName()
     * @generated
     * @ordered
     */
    protected String contextName = CONTEXT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getCompProperties() <em>Comp Properties</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompProperties()
     * @generated
     * @ordered
     */
    protected static final String COMP_PROPERTIES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCompProperties() <em>Comp Properties</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompProperties()
     * @generated
     * @ordered
     */
    protected String compProperties = COMP_PROPERTIES_EDEFAULT;

    protected boolean readOnly = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CONNECTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ModelElement> getOwnedElement() {
        if (ownedElement == null) {
            ownedElement = new EObjectContainmentWithInverseEList<ModelElement>(ModelElement.class, this,
                    ConnectionPackage.CONNECTION__OWNED_ELEMENT, CorePackage.MODEL_ELEMENT__NAMESPACE);
        }
        return ownedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ModelElement> getImportedElement() {
        if (importedElement == null) {
            importedElement = new EObjectWithInverseResolvingEList.ManyInverse<ModelElement>(ModelElement.class, this,
                    ConnectionPackage.CONNECTION__IMPORTED_ELEMENT, CorePackage.MODEL_ELEMENT__IMPORTER);
        }
        return importedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<DataManager> getDataManager() {
        if (dataManager == null) {
            dataManager = new EObjectWithInverseResolvingEList.ManyInverse<DataManager>(DataManager.class, this,
                    ConnectionPackage.CONNECTION__DATA_MANAGER, SoftwaredeploymentPackage.DATA_MANAGER__DATA_PACKAGE);
        }
        return dataManager;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getPathname() {
        return pathname;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPathname(String newPathname) {
        String oldPathname = pathname;
        pathname = newPathname;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__PATHNAME, oldPathname, pathname));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Machine getMachine() {
        if (eContainerFeatureID() != ConnectionPackage.CONNECTION__MACHINE)
            return null;
        return (Machine) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMachine(Machine newMachine, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newMachine, ConnectionPackage.CONNECTION__MACHINE, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setMachine(Machine newMachine) {
        if (newMachine != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.CONNECTION__MACHINE && newMachine != null)) {
            if (EcoreUtil.isAncestor(this, newMachine))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newMachine != null)
                msgs = ((InternalEObject) newMachine).eInverseAdd(this, SoftwaredeploymentPackage.MACHINE__DEPLOYED_COMPONENT,
                        Machine.class, msgs);
            msgs = basicSetMachine(newMachine, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__MACHINE, newMachine, newMachine));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<DeployedSoftwareSystem> getDeployedSoftwareSystem() {
        if (deployedSoftwareSystem == null) {
            deployedSoftwareSystem = new EObjectWithInverseResolvingEList.ManyInverse<DeployedSoftwareSystem>(
                    DeployedSoftwareSystem.class, this, ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM,
                    SoftwaredeploymentPackage.DEPLOYED_SOFTWARE_SYSTEM__DEPLOYED_COMPONENT);
        }
        return deployedSoftwareSystem;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Component getComponent() {
        if (component != null && component.eIsProxy()) {
            InternalEObject oldComponent = (InternalEObject) component;
            component = (Component) eResolveProxy(oldComponent);
            if (component != oldComponent) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.CONNECTION__COMPONENT,
                            oldComponent, component));
            }
        }
        return component;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Component basicGetComponent() {
        return component;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComponent(Component newComponent, NotificationChain msgs) {
        Component oldComponent = component;
        component = newComponent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.CONNECTION__COMPONENT, oldComponent, newComponent);
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
    public void setComponent(Component newComponent) {
        if (newComponent != component) {
            NotificationChain msgs = null;
            if (component != null)
                msgs = ((InternalEObject) component).eInverseRemove(this, SoftwaredeploymentPackage.COMPONENT__DEPLOYMENT,
                        Component.class, msgs);
            if (newComponent != null)
                msgs = ((InternalEObject) newComponent).eInverseAdd(this, SoftwaredeploymentPackage.COMPONENT__DEPLOYMENT,
                        Component.class, msgs);
            msgs = basicSetComponent(newComponent, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__COMPONENT, newComponent,
                    newComponent));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsCaseSensitive() {
        return isCaseSensitive;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setIsCaseSensitive(boolean newIsCaseSensitive) {
        boolean oldIsCaseSensitive = isCaseSensitive;
        isCaseSensitive = newIsCaseSensitive;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE,
                    oldIsCaseSensitive, isCaseSensitive));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ProviderConnection> getClientConnection() {
        if (clientConnection == null) {
            clientConnection = new EObjectWithInverseResolvingEList<ProviderConnection>(ProviderConnection.class, this,
                    ConnectionPackage.CONNECTION__CLIENT_CONNECTION, SoftwaredeploymentPackage.PROVIDER_CONNECTION__DATA_MANAGER);
        }
        return clientConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<orgomg.cwm.objectmodel.core.Package> getDataPackage() {
        if (dataPackage == null) {
            dataPackage = new EObjectWithInverseResolvingEList.ManyInverse<orgomg.cwm.objectmodel.core.Package>(
                    orgomg.cwm.objectmodel.core.Package.class, this, ConnectionPackage.CONNECTION__DATA_PACKAGE,
                    CorePackage.PACKAGE__DATA_MANAGER);
        }
        return dataPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<ProviderConnection> getResourceConnection() {
        if (resourceConnection == null) {
            resourceConnection = new EObjectContainmentWithInverseEList<ProviderConnection>(ProviderConnection.class, this,
                    ConnectionPackage.CONNECTION__RESOURCE_CONNECTION,
                    SoftwaredeploymentPackage.PROVIDER_CONNECTION__DATA_PROVIDER);
        }
        return resourceConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean newReadOnly) {
        readOnly = newReadOnly;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public QueriesConnection getQueries() {
        if (queries != null && queries.eIsProxy()) {
            InternalEObject oldQueries = (InternalEObject) queries;
            queries = (QueriesConnection) eResolveProxy(oldQueries);
            if (queries != oldQueries) {
                InternalEObject newQueries = (InternalEObject) queries;
                NotificationChain msgs = oldQueries.eInverseRemove(this, ConnectionPackage.QUERIES_CONNECTION__CONNECTION,
                        QueriesConnection.class, null);
                if (newQueries.eInternalContainer() == null) {
                    msgs = newQueries.eInverseAdd(this, ConnectionPackage.QUERIES_CONNECTION__CONNECTION, QueriesConnection.class,
                            msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.CONNECTION__QUERIES, oldQueries,
                            queries));
            }
        }
        return queries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public QueriesConnection basicGetQueries() {
        return queries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQueries(QueriesConnection newQueries, NotificationChain msgs) {
        QueriesConnection oldQueries = queries;
        queries = newQueries;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__QUERIES,
                    oldQueries, newQueries);
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
    public void setQueries(QueriesConnection newQueries) {
        if (newQueries != queries) {
            NotificationChain msgs = null;
            if (queries != null)
                msgs = ((InternalEObject) queries).eInverseRemove(this, ConnectionPackage.QUERIES_CONNECTION__CONNECTION,
                        QueriesConnection.class, msgs);
            if (newQueries != null)
                msgs = ((InternalEObject) newQueries).eInverseAdd(this, ConnectionPackage.QUERIES_CONNECTION__CONNECTION,
                        QueriesConnection.class, msgs);
            msgs = basicSetQueries(newQueries, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__QUERIES, newQueries, newQueries));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isContextMode() {
        return contextMode;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setContextMode(boolean newContextMode) {
        boolean oldContextMode = contextMode;
        contextMode = newContextMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__CONTEXT_MODE, oldContextMode,
                    contextMode));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getContextId() {
        return contextId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setContextId(String newContextId) {
        String oldContextId = contextId;
        contextId = newContextId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__CONTEXT_ID, oldContextId,
                    contextId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setContextName(String newContextName) {
        String oldContextName = contextName;
        contextName = newContextName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__CONTEXT_NAME, oldContextName,
                    contextName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCompProperties() {
        return compProperties;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCompProperties(String newCompProperties) {
        String oldCompProperties = compProperties;
        compProperties = newCompProperties;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONNECTION__COMP_PROPERTIES,
                    oldCompProperties, compProperties));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getConnectionTypeName() {
        return ""; //$NON-NLS-1$
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedElement()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getImportedElement()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDataManager()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__MACHINE:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetMachine((Machine) otherEnd, msgs);
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDeployedSoftwareSystem()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__COMPONENT:
            if (component != null)
                msgs = ((InternalEObject) component).eInverseRemove(this, SoftwaredeploymentPackage.COMPONENT__DEPLOYMENT,
                        Component.class, msgs);
            return basicSetComponent((Component) otherEnd, msgs);
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getClientConnection()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getDataPackage()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getResourceConnection()).basicAdd(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__QUERIES:
            if (queries != null)
                msgs = ((InternalEObject) queries).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.CONNECTION__QUERIES, null, msgs);
            return basicSetQueries((QueriesConnection) otherEnd, msgs);
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
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            return ((InternalEList<?>) getOwnedElement()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            return ((InternalEList<?>) getImportedElement()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            return ((InternalEList<?>) getDataManager()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__MACHINE:
            return basicSetMachine(null, msgs);
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            return ((InternalEList<?>) getDeployedSoftwareSystem()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__COMPONENT:
            return basicSetComponent(null, msgs);
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            return ((InternalEList<?>) getClientConnection()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            return ((InternalEList<?>) getDataPackage()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            return ((InternalEList<?>) getResourceConnection()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONNECTION__QUERIES:
            return basicSetQueries(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
        case ConnectionPackage.CONNECTION__MACHINE:
            return eInternalContainer().eInverseRemove(this, SoftwaredeploymentPackage.MACHINE__DEPLOYED_COMPONENT, Machine.class,
                    msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            return getOwnedElement();
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            return getImportedElement();
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            return getDataManager();
        case ConnectionPackage.CONNECTION__PATHNAME:
            return getPathname();
        case ConnectionPackage.CONNECTION__MACHINE:
            return getMachine();
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            return getDeployedSoftwareSystem();
        case ConnectionPackage.CONNECTION__COMPONENT:
            if (resolve)
                return getComponent();
            return basicGetComponent();
        case ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE:
            return isIsCaseSensitive();
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            return getClientConnection();
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            return getDataPackage();
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            return getResourceConnection();
        case ConnectionPackage.CONNECTION__VERSION:
            return getVersion();
        case ConnectionPackage.CONNECTION__QUERIES:
            if (resolve)
                return getQueries();
            return basicGetQueries();
        case ConnectionPackage.CONNECTION__CONTEXT_MODE:
            return isContextMode();
        case ConnectionPackage.CONNECTION__CONTEXT_ID:
            return getContextId();
        case ConnectionPackage.CONNECTION__CONTEXT_NAME:
            return getContextName();
        case ConnectionPackage.CONNECTION__COMP_PROPERTIES:
            return getCompProperties();
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
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            getOwnedElement().clear();
            getOwnedElement().addAll((Collection<? extends ModelElement>) newValue);
            return;
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            getImportedElement().clear();
            getImportedElement().addAll((Collection<? extends ModelElement>) newValue);
            return;
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            getDataManager().clear();
            getDataManager().addAll((Collection<? extends DataManager>) newValue);
            return;
        case ConnectionPackage.CONNECTION__PATHNAME:
            setPathname((String) newValue);
            return;
        case ConnectionPackage.CONNECTION__MACHINE:
            setMachine((Machine) newValue);
            return;
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            getDeployedSoftwareSystem().clear();
            getDeployedSoftwareSystem().addAll((Collection<? extends DeployedSoftwareSystem>) newValue);
            return;
        case ConnectionPackage.CONNECTION__COMPONENT:
            setComponent((Component) newValue);
            return;
        case ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE:
            setIsCaseSensitive((Boolean) newValue);
            return;
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            getClientConnection().clear();
            getClientConnection().addAll((Collection<? extends ProviderConnection>) newValue);
            return;
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            getDataPackage().clear();
            getDataPackage().addAll((Collection<? extends orgomg.cwm.objectmodel.core.Package>) newValue);
            return;
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            getResourceConnection().clear();
            getResourceConnection().addAll((Collection<? extends ProviderConnection>) newValue);
            return;
        case ConnectionPackage.CONNECTION__VERSION:
            setVersion((String) newValue);
            return;
        case ConnectionPackage.CONNECTION__QUERIES:
            setQueries((QueriesConnection) newValue);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_MODE:
            setContextMode((Boolean) newValue);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_ID:
            setContextId((String) newValue);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_NAME:
            setContextName((String) newValue);
            return;
        case ConnectionPackage.CONNECTION__COMP_PROPERTIES:
            setCompProperties((String) newValue);
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
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            getOwnedElement().clear();
            return;
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            getImportedElement().clear();
            return;
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            getDataManager().clear();
            return;
        case ConnectionPackage.CONNECTION__PATHNAME:
            setPathname(PATHNAME_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__MACHINE:
            setMachine((Machine) null);
            return;
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            getDeployedSoftwareSystem().clear();
            return;
        case ConnectionPackage.CONNECTION__COMPONENT:
            setComponent((Component) null);
            return;
        case ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE:
            setIsCaseSensitive(IS_CASE_SENSITIVE_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            getClientConnection().clear();
            return;
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            getDataPackage().clear();
            return;
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            getResourceConnection().clear();
            return;
        case ConnectionPackage.CONNECTION__VERSION:
            setVersion(VERSION_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__QUERIES:
            setQueries((QueriesConnection) null);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_MODE:
            setContextMode(CONTEXT_MODE_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_ID:
            setContextId(CONTEXT_ID_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__CONTEXT_NAME:
            setContextName(CONTEXT_NAME_EDEFAULT);
            return;
        case ConnectionPackage.CONNECTION__COMP_PROPERTIES:
            setCompProperties(COMP_PROPERTIES_EDEFAULT);
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
        case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
            return ownedElement != null && !ownedElement.isEmpty();
        case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
            return importedElement != null && !importedElement.isEmpty();
        case ConnectionPackage.CONNECTION__DATA_MANAGER:
            return dataManager != null && !dataManager.isEmpty();
        case ConnectionPackage.CONNECTION__PATHNAME:
            return PATHNAME_EDEFAULT == null ? pathname != null : !PATHNAME_EDEFAULT.equals(pathname);
        case ConnectionPackage.CONNECTION__MACHINE:
            return getMachine() != null;
        case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
            return deployedSoftwareSystem != null && !deployedSoftwareSystem.isEmpty();
        case ConnectionPackage.CONNECTION__COMPONENT:
            return component != null;
        case ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE:
            return isCaseSensitive != IS_CASE_SENSITIVE_EDEFAULT;
        case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
            return clientConnection != null && !clientConnection.isEmpty();
        case ConnectionPackage.CONNECTION__DATA_PACKAGE:
            return dataPackage != null && !dataPackage.isEmpty();
        case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
            return resourceConnection != null && !resourceConnection.isEmpty();
        case ConnectionPackage.CONNECTION__VERSION:
            return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
        case ConnectionPackage.CONNECTION__QUERIES:
            return queries != null;
        case ConnectionPackage.CONNECTION__CONTEXT_MODE:
            return contextMode != CONTEXT_MODE_EDEFAULT;
        case ConnectionPackage.CONNECTION__CONTEXT_ID:
            return CONTEXT_ID_EDEFAULT == null ? contextId != null : !CONTEXT_ID_EDEFAULT.equals(contextId);
        case ConnectionPackage.CONNECTION__CONTEXT_NAME:
            return CONTEXT_NAME_EDEFAULT == null ? contextName != null : !CONTEXT_NAME_EDEFAULT.equals(contextName);
        case ConnectionPackage.CONNECTION__COMP_PROPERTIES:
            return COMP_PROPERTIES_EDEFAULT == null ? compProperties != null : !COMP_PROPERTIES_EDEFAULT.equals(compProperties);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Namespace.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.CONNECTION__OWNED_ELEMENT:
                return CorePackage.NAMESPACE__OWNED_ELEMENT;
            default:
                return -1;
            }
        }
        if (baseClass == orgomg.cwm.objectmodel.core.Package.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.CONNECTION__IMPORTED_ELEMENT:
                return CorePackage.PACKAGE__IMPORTED_ELEMENT;
            case ConnectionPackage.CONNECTION__DATA_MANAGER:
                return CorePackage.PACKAGE__DATA_MANAGER;
            default:
                return -1;
            }
        }
        if (baseClass == DeployedComponent.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.CONNECTION__PATHNAME:
                return SoftwaredeploymentPackage.DEPLOYED_COMPONENT__PATHNAME;
            case ConnectionPackage.CONNECTION__MACHINE:
                return SoftwaredeploymentPackage.DEPLOYED_COMPONENT__MACHINE;
            case ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM:
                return SoftwaredeploymentPackage.DEPLOYED_COMPONENT__DEPLOYED_SOFTWARE_SYSTEM;
            case ConnectionPackage.CONNECTION__COMPONENT:
                return SoftwaredeploymentPackage.DEPLOYED_COMPONENT__COMPONENT;
            default:
                return -1;
            }
        }
        if (baseClass == DataManager.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE:
                return SoftwaredeploymentPackage.DATA_MANAGER__IS_CASE_SENSITIVE;
            case ConnectionPackage.CONNECTION__CLIENT_CONNECTION:
                return SoftwaredeploymentPackage.DATA_MANAGER__CLIENT_CONNECTION;
            case ConnectionPackage.CONNECTION__DATA_PACKAGE:
                return SoftwaredeploymentPackage.DATA_MANAGER__DATA_PACKAGE;
            default:
                return -1;
            }
        }
        if (baseClass == DataProvider.class) {
            switch (derivedFeatureID) {
            case ConnectionPackage.CONNECTION__RESOURCE_CONNECTION:
                return SoftwaredeploymentPackage.DATA_PROVIDER__RESOURCE_CONNECTION;
            default:
                return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Namespace.class) {
            switch (baseFeatureID) {
            case CorePackage.NAMESPACE__OWNED_ELEMENT:
                return ConnectionPackage.CONNECTION__OWNED_ELEMENT;
            default:
                return -1;
            }
        }
        if (baseClass == orgomg.cwm.objectmodel.core.Package.class) {
            switch (baseFeatureID) {
            case CorePackage.PACKAGE__IMPORTED_ELEMENT:
                return ConnectionPackage.CONNECTION__IMPORTED_ELEMENT;
            case CorePackage.PACKAGE__DATA_MANAGER:
                return ConnectionPackage.CONNECTION__DATA_MANAGER;
            default:
                return -1;
            }
        }
        if (baseClass == DeployedComponent.class) {
            switch (baseFeatureID) {
            case SoftwaredeploymentPackage.DEPLOYED_COMPONENT__PATHNAME:
                return ConnectionPackage.CONNECTION__PATHNAME;
            case SoftwaredeploymentPackage.DEPLOYED_COMPONENT__MACHINE:
                return ConnectionPackage.CONNECTION__MACHINE;
            case SoftwaredeploymentPackage.DEPLOYED_COMPONENT__DEPLOYED_SOFTWARE_SYSTEM:
                return ConnectionPackage.CONNECTION__DEPLOYED_SOFTWARE_SYSTEM;
            case SoftwaredeploymentPackage.DEPLOYED_COMPONENT__COMPONENT:
                return ConnectionPackage.CONNECTION__COMPONENT;
            default:
                return -1;
            }
        }
        if (baseClass == DataManager.class) {
            switch (baseFeatureID) {
            case SoftwaredeploymentPackage.DATA_MANAGER__IS_CASE_SENSITIVE:
                return ConnectionPackage.CONNECTION__IS_CASE_SENSITIVE;
            case SoftwaredeploymentPackage.DATA_MANAGER__CLIENT_CONNECTION:
                return ConnectionPackage.CONNECTION__CLIENT_CONNECTION;
            case SoftwaredeploymentPackage.DATA_MANAGER__DATA_PACKAGE:
                return ConnectionPackage.CONNECTION__DATA_PACKAGE;
            default:
                return -1;
            }
        }
        if (baseClass == DataProvider.class) {
            switch (baseFeatureID) {
            case SoftwaredeploymentPackage.DATA_PROVIDER__RESOURCE_CONNECTION:
                return ConnectionPackage.CONNECTION__RESOURCE_CONNECTION;
            default:
                return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (pathname: ");
        result.append(pathname);
        result.append(", isCaseSensitive: ");
        result.append(isCaseSensitive);
        result.append(", version: ");
        result.append(version);
        result.append(", ContextMode: ");
        result.append(contextMode);
        result.append(", ContextId: ");
        result.append(contextId);
        result.append(", contextName: ");
        result.append(contextName);
        result.append(", compProperties: ");
        result.append(compProperties);
        result.append(')');
        return result.toString();
    }

    /**
     * @generated NOT
     */
    public String getValue(String value, boolean encrypt) {
        if (!isContextMode() && value != null && value.length() > 0) {
            String newValue = null;
            if (encrypt) {
                newValue = CryptoHelper.getDefault().encrypt(value);
            } else {
                newValue = CryptoHelper.getDefault().decrypt(value);
            }
            if (newValue != null) { // if enable to encrypt/decrypt will return the new value.
                return newValue;
            }
        }
        return value;
    }
} // ConnectionImpl
