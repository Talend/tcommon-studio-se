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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Salesforce Module Unit</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceModuleUnitImpl#getMetadataTable <em>Metadata Table</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceModuleUnitImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceModuleUnitImpl#getTables <em>Tables</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SalesforceModuleUnitImpl#getModuleName <em>Module Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SalesforceModuleUnitImpl extends AbstractMetadataObjectImpl implements SalesforceModuleUnit {

    /**
     * The cached value of the '{@link #getMetadataTable() <em>Metadata Table</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMetadataTable()
     * @generated
     * @ordered
     */
    protected MetadataTable metadataTable;

    /**
     * The cached value of the '{@link #getTables() <em>Tables</em>}' containment reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getTables()
     * @generated
     * @ordered
     */
    protected EList<MetadataTable> tables;

    /**
     * The default value of the '{@link #getModuleName() <em>Module Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getModuleName()
     * @generated
     * @ordered
     */
    protected static final String MODULE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getModuleName() <em>Module Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getModuleName()
     * @generated
     * @ordered
     */
    protected String moduleName = MODULE_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SalesforceModuleUnitImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SALESFORCE_MODULE_UNIT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MetadataTable getMetadataTable() {
        if (metadataTable != null && metadataTable.eIsProxy()) {
            InternalEObject oldMetadataTable = (InternalEObject) metadataTable;
            metadataTable = (MetadataTable) eResolveProxy(oldMetadataTable);
            if (metadataTable != oldMetadataTable) {
                InternalEObject newMetadataTable = (InternalEObject) metadataTable;
                NotificationChain msgs = oldMetadataTable.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, null, null);
                if (newMetadataTable.eInternalContainer() == null) {
                    msgs = newMetadataTable.eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                            - ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, oldMetadataTable, metadataTable));
            }
        }
        return metadataTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MetadataTable basicGetMetadataTable() {
        return metadataTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadataTable(MetadataTable newMetadataTable, NotificationChain msgs) {
        MetadataTable oldMetadataTable = metadataTable;
        metadataTable = newMetadataTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, oldMetadataTable, newMetadataTable);
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
    public void setMetadataTable(MetadataTable newMetadataTable) {
        if (newMetadataTable != metadataTable) {
            NotificationChain msgs = null;
            if (metadataTable != null)
                msgs = ((InternalEObject) metadataTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, null, msgs);
            if (newMetadataTable != null)
                msgs = ((InternalEObject) newMetadataTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE, null, msgs);
            msgs = basicSetMetadataTable(newMetadataTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE,
                    newMetadataTable, newMetadataTable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SalesforceSchemaConnection getConnection() {
        if (eContainerFeatureID() != ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION)
            return null;
        return (SalesforceSchemaConnection) eContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SalesforceSchemaConnection basicGetConnection() {
        if (eContainerFeatureID() != ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION)
            return null;
        return (SalesforceSchemaConnection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConnection(SalesforceSchemaConnection newConnection, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newConnection, ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(SalesforceSchemaConnection newConnection) {
        if (newConnection != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION && newConnection != null)) {
            if (EcoreUtil.isAncestor(this, newConnection))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newConnection != null)
                msgs = ((InternalEObject) newConnection).eInverseAdd(this,
                        ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES, SalesforceSchemaConnection.class, msgs);
            msgs = basicSetConnection(newConnection, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION,
                    newConnection, newConnection));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataTable> getTables() {
        if (tables == null) {
            tables = new EObjectContainmentEList.Resolving<MetadataTable>(MetadataTable.class, this,
                    ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES);
        }
        return tables;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setModuleName(String newModuleName) {
        String oldModuleName = moduleName;
        moduleName = newModuleName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SALESFORCE_MODULE_UNIT__MODULE_NAME,
                    oldModuleName, moduleName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetConnection((SalesforceSchemaConnection) otherEnd, msgs);
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE:
            return basicSetMetadataTable(null, msgs);
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            return basicSetConnection(null, msgs);
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES:
            return ((InternalEList<?>) getTables()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.SALESFORCE_SCHEMA_CONNECTION__MODULES,
                    SalesforceSchemaConnection.class, msgs);
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE:
            if (resolve)
                return getMetadataTable();
            return basicGetMetadataTable();
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES:
            return getTables();
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__MODULE_NAME:
            return getModuleName();
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE:
            setMetadataTable((MetadataTable) newValue);
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            setConnection((SalesforceSchemaConnection) newValue);
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES:
            getTables().clear();
            getTables().addAll((Collection<? extends MetadataTable>) newValue);
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__MODULE_NAME:
            setModuleName((String) newValue);
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE:
            setMetadataTable((MetadataTable) null);
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            setConnection((SalesforceSchemaConnection) null);
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES:
            getTables().clear();
            return;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__MODULE_NAME:
            setModuleName(MODULE_NAME_EDEFAULT);
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
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__METADATA_TABLE:
            return metadataTable != null;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__CONNECTION:
            return basicGetConnection() != null;
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__TABLES:
            return tables != null && !tables.isEmpty();
        case ConnectionPackage.SALESFORCE_MODULE_UNIT__MODULE_NAME:
            return MODULE_NAME_EDEFAULT == null ? moduleName != null : !MODULE_NAME_EDEFAULT.equals(moduleName);
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
        result.append(" (moduleName: ");
        result.append(moduleName);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        return false;
    }

} // SalesforceModuleUnitImpl
