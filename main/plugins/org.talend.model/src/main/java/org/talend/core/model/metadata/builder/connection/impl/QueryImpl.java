/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Query</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.QueryImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.QueryImpl#getQueries <em>Queries</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.QueryImpl#isContextMode <em>Context Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryImpl extends AbstractMetadataObjectImpl implements Query {

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected QueryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.QUERY;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.QUERY__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public QueriesConnection getQueries() {
        if (eContainerFeatureID() != ConnectionPackage.QUERY__QUERIES)
            return null;
        return (QueriesConnection) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueriesConnection basicGetQueries() {
        if (eContainerFeatureID() != ConnectionPackage.QUERY__QUERIES)
            return null;
        return (QueriesConnection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQueries(QueriesConnection newQueries, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newQueries, ConnectionPackage.QUERY__QUERIES, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setQueries(QueriesConnection newQueries) {
        if (newQueries != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.QUERY__QUERIES && newQueries != null)) {
            if (EcoreUtil.isAncestor(this, newQueries))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newQueries != null)
                msgs = ((InternalEObject) newQueries).eInverseAdd(this, ConnectionPackage.QUERIES_CONNECTION__QUERY,
                        QueriesConnection.class, msgs);
            msgs = basicSetQueries(newQueries, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.QUERY__QUERIES, newQueries, newQueries));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.QUERY__CONTEXT_MODE, oldContextMode,
                    contextMode));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.QUERY__QUERIES:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
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
        case ConnectionPackage.QUERY__QUERIES:
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
        case ConnectionPackage.QUERY__QUERIES:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.QUERIES_CONNECTION__QUERY,
                    QueriesConnection.class, msgs);
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
        case ConnectionPackage.QUERY__VALUE:
            return getValue();
        case ConnectionPackage.QUERY__QUERIES:
            if (resolve)
                return getQueries();
            return basicGetQueries();
        case ConnectionPackage.QUERY__CONTEXT_MODE:
            return isContextMode();
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
        case ConnectionPackage.QUERY__VALUE:
            setValue((String) newValue);
            return;
        case ConnectionPackage.QUERY__QUERIES:
            setQueries((QueriesConnection) newValue);
            return;
        case ConnectionPackage.QUERY__CONTEXT_MODE:
            setContextMode((Boolean) newValue);
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
        case ConnectionPackage.QUERY__VALUE:
            setValue(VALUE_EDEFAULT);
            return;
        case ConnectionPackage.QUERY__QUERIES:
            setQueries((QueriesConnection) null);
            return;
        case ConnectionPackage.QUERY__CONTEXT_MODE:
            setContextMode(CONTEXT_MODE_EDEFAULT);
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
        case ConnectionPackage.QUERY__VALUE:
            return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
        case ConnectionPackage.QUERY__QUERIES:
            return basicGetQueries() != null;
        case ConnectionPackage.QUERY__CONTEXT_MODE:
            return contextMode != CONTEXT_MODE_EDEFAULT;
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
        result.append(" (value: ");
        result.append(value);
        result.append(", contextMode: ");
        result.append(contextMode);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        Connection connection = getQueries().getConnection();
        return connection == null ? false : connection.isReadOnly();
    }

} // QueryImpl
