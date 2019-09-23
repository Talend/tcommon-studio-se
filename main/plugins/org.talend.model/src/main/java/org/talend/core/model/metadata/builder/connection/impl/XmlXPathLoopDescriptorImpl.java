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

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Xml XPath Loop Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl#getLimitBoucle <em>Limit Boucle</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl#getAbsoluteXPathQuery <em>Absolute XPath Query</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlXPathLoopDescriptorImpl#getSchemaTargets <em>Schema Targets</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XmlXPathLoopDescriptorImpl extends EObjectImpl implements XmlXPathLoopDescriptor {

    /**
     * The default value of the '{@link #getLimitBoucle() <em>Limit Boucle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitBoucle()
     * @generated
     * @ordered
     */
    protected static final Integer LIMIT_BOUCLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLimitBoucle() <em>Limit Boucle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitBoucle()
     * @generated
     * @ordered
     */
    protected Integer limitBoucle = LIMIT_BOUCLE_EDEFAULT;

    /**
     * The default value of the '{@link #getAbsoluteXPathQuery() <em>Absolute XPath Query</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbsoluteXPathQuery()
     * @generated
     * @ordered
     */
    protected static final String ABSOLUTE_XPATH_QUERY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAbsoluteXPathQuery() <em>Absolute XPath Query</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbsoluteXPathQuery()
     * @generated
     * @ordered
     */
    protected String absoluteXPathQuery = ABSOLUTE_XPATH_QUERY_EDEFAULT;

    /**
     * The cached value of the '{@link #getSchemaTargets() <em>Schema Targets</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaTargets()
     * @generated
     * @ordered
     */
    protected EList<SchemaTarget> schemaTargets;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XmlXPathLoopDescriptorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.XML_XPATH_LOOP_DESCRIPTOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Integer getLimitBoucle() {
        return limitBoucle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLimitBoucle(Integer newLimitBoucle) {
        Integer oldLimitBoucle = limitBoucle;
        limitBoucle = newLimitBoucle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE,
                    oldLimitBoucle, limitBoucle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAbsoluteXPathQuery() {
        return absoluteXPathQuery;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbsoluteXPathQuery(String newAbsoluteXPathQuery) {
        String oldAbsoluteXPathQuery = absoluteXPathQuery;
        absoluteXPathQuery = newAbsoluteXPathQuery;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY, oldAbsoluteXPathQuery, absoluteXPathQuery));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XmlFileConnection getConnection() {
        if (eContainerFeatureID() != ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION)
            return null;
        return (XmlFileConnection) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XmlFileConnection basicGetConnection() {
        if (eContainerFeatureID() != ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION)
            return null;
        return (XmlFileConnection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConnection(XmlFileConnection newConnection, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newConnection, ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(XmlFileConnection newConnection) {
        if (newConnection != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION && newConnection != null)) {
            if (EcoreUtil.isAncestor(this, newConnection))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newConnection != null)
                msgs = ((InternalEObject) newConnection).eInverseAdd(this, ConnectionPackage.XML_FILE_CONNECTION__SCHEMA,
                        XmlFileConnection.class, msgs);
            msgs = basicSetConnection(newConnection, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION,
                    newConnection, newConnection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SchemaTarget> getSchemaTargets() {
        if (schemaTargets == null) {
            schemaTargets = new EObjectContainmentWithInverseEList.Resolving<SchemaTarget>(SchemaTarget.class, this,
                    ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS, ConnectionPackage.SCHEMA_TARGET__SCHEMA);
        }
        return schemaTargets;
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetConnection((XmlFileConnection) otherEnd, msgs);
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getSchemaTargets()).basicAdd(otherEnd, msgs);
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            return basicSetConnection(null, msgs);
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            return ((InternalEList<?>) getSchemaTargets()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.XML_FILE_CONNECTION__SCHEMA,
                    XmlFileConnection.class, msgs);
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE:
            return getLimitBoucle();
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY:
            return getAbsoluteXPathQuery();
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            return getSchemaTargets();
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE:
            setLimitBoucle((Integer) newValue);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY:
            setAbsoluteXPathQuery((String) newValue);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            setConnection((XmlFileConnection) newValue);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            getSchemaTargets().clear();
            getSchemaTargets().addAll((Collection<? extends SchemaTarget>) newValue);
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE:
            setLimitBoucle(LIMIT_BOUCLE_EDEFAULT);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY:
            setAbsoluteXPathQuery(ABSOLUTE_XPATH_QUERY_EDEFAULT);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            setConnection((XmlFileConnection) null);
            return;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            getSchemaTargets().clear();
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
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__LIMIT_BOUCLE:
            return LIMIT_BOUCLE_EDEFAULT == null ? limitBoucle != null : !LIMIT_BOUCLE_EDEFAULT.equals(limitBoucle);
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__ABSOLUTE_XPATH_QUERY:
            return ABSOLUTE_XPATH_QUERY_EDEFAULT == null ? absoluteXPathQuery != null : !ABSOLUTE_XPATH_QUERY_EDEFAULT
                    .equals(absoluteXPathQuery);
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION:
            return basicGetConnection() != null;
        case ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__SCHEMA_TARGETS:
            return schemaTargets != null && !schemaTargets.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (LimitBoucle: ");
        result.append(limitBoucle);
        result.append(", AbsoluteXPathQuery: ");
        result.append(absoluteXPathQuery);
        result.append(')');
        return result.toString();
    }

} //XmlXPathLoopDescriptorImpl
