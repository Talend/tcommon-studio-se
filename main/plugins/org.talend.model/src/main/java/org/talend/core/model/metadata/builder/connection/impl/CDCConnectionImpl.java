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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.CDCType;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CDC Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCConnectionImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCConnectionImpl#getCdcTypes <em>Cdc Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CDCConnectionImpl extends EObjectImpl implements CDCConnection {

    /**
     * The cached value of the '{@link #getCdcTypes() <em>Cdc Types</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCdcTypes()
     * @generated
     * @ordered
     */
    protected EList<CDCType> cdcTypes;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CDCConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CDC_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatabaseConnection getConnection() {
        if (eContainerFeatureID() != ConnectionPackage.CDC_CONNECTION__CONNECTION)
            return null;
        return (DatabaseConnection) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatabaseConnection basicGetConnection() {
        if (eContainerFeatureID() != ConnectionPackage.CDC_CONNECTION__CONNECTION)
            return null;
        return (DatabaseConnection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConnection(DatabaseConnection newConnection, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newConnection, ConnectionPackage.CDC_CONNECTION__CONNECTION, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(DatabaseConnection newConnection) {
        if (newConnection != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.CDC_CONNECTION__CONNECTION && newConnection != null)) {
            if (EcoreUtil.isAncestor(this, newConnection))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newConnection != null)
                msgs = ((InternalEObject) newConnection).eInverseAdd(this, ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS,
                        DatabaseConnection.class, msgs);
            msgs = basicSetConnection(newConnection, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CDC_CONNECTION__CONNECTION, newConnection,
                    newConnection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CDCType> getCdcTypes() {
        if (cdcTypes == null) {
            cdcTypes = new EObjectContainmentEList.Resolving<CDCType>(CDCType.class, this,
                    ConnectionPackage.CDC_CONNECTION__CDC_TYPES);
        }
        return cdcTypes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetConnection((DatabaseConnection) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            return basicSetConnection(null, msgs);
        case ConnectionPackage.CDC_CONNECTION__CDC_TYPES:
            return ((InternalEList<?>) getCdcTypes()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.DATABASE_CONNECTION__CDC_CONNS,
                    DatabaseConnection.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.CDC_CONNECTION__CDC_TYPES:
            return getCdcTypes();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            setConnection((DatabaseConnection) newValue);
            return;
        case ConnectionPackage.CDC_CONNECTION__CDC_TYPES:
            getCdcTypes().clear();
            getCdcTypes().addAll((Collection<? extends CDCType>) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            setConnection((DatabaseConnection) null);
            return;
        case ConnectionPackage.CDC_CONNECTION__CDC_TYPES:
            getCdcTypes().clear();
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.CDC_CONNECTION__CONNECTION:
            return basicGetConnection() != null;
        case ConnectionPackage.CDC_CONNECTION__CDC_TYPES:
            return cdcTypes != null && !cdcTypes.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CDCConnectionImpl
