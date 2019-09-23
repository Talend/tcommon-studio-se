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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Queries Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.QueriesConnectionImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.QueriesConnectionImpl#getQuery <em>Query</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueriesConnectionImpl extends EObjectImpl implements QueriesConnection {

    /**
     * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuery()
     * @generated
     * @ordered
     */
    protected EList<Query> query;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected QueriesConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.QUERIES_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Connection getConnection() {
        if (eContainerFeatureID() != ConnectionPackage.QUERIES_CONNECTION__CONNECTION)
            return null;
        return (Connection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConnection(Connection newConnection, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newConnection, ConnectionPackage.QUERIES_CONNECTION__CONNECTION, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(Connection newConnection) {
        if (newConnection != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.QUERIES_CONNECTION__CONNECTION && newConnection != null)) {
            if (EcoreUtil.isAncestor(this, newConnection))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newConnection != null)
                msgs = ((InternalEObject) newConnection).eInverseAdd(this, ConnectionPackage.CONNECTION__QUERIES,
                        Connection.class, msgs);
            msgs = basicSetConnection(newConnection, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.QUERIES_CONNECTION__CONNECTION,
                    newConnection, newConnection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Query> getQuery() {
        if (query == null) {
            query = new EObjectContainmentWithInverseEList.Resolving<Query>(Query.class, this,
                    ConnectionPackage.QUERIES_CONNECTION__QUERY, ConnectionPackage.QUERY__QUERIES);
        }
        return query;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetConnection((Connection) otherEnd, msgs);
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getQuery()).basicAdd(otherEnd, msgs);
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            return basicSetConnection(null, msgs);
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            return ((InternalEList<?>) getQuery()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.CONNECTION__QUERIES, Connection.class, msgs);
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            return getConnection();
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            return getQuery();
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            setConnection((Connection) newValue);
            return;
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            getQuery().clear();
            getQuery().addAll((Collection<? extends Query>) newValue);
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            setConnection((Connection) null);
            return;
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            getQuery().clear();
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
        case ConnectionPackage.QUERIES_CONNECTION__CONNECTION:
            return getConnection() != null;
        case ConnectionPackage.QUERIES_CONNECTION__QUERY:
            return query != null && !query.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //QueriesConnectionImpl
