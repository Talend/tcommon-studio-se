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
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.CDCType;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.SubscriberTable;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>CDC Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCTypeImpl#getLinkDB <em>Link DB</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCTypeImpl#getSubscribers <em>Subscribers</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCTypeImpl#getCdcConnection <em>Cdc Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.CDCTypeImpl#getJournalName <em>Journal Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CDCTypeImpl extends AbstractMetadataObjectImpl implements CDCType {

    /**
     * The default value of the '{@link #getLinkDB() <em>Link DB</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLinkDB()
     * @generated
     * @ordered
     */
    protected static final String LINK_DB_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLinkDB() <em>Link DB</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLinkDB()
     * @generated
     * @ordered
     */
    protected String linkDB = LINK_DB_EDEFAULT;

    /**
     * The cached value of the '{@link #getSubscribers() <em>Subscribers</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSubscribers()
     * @generated
     * @ordered
     */
    protected EList<SubscriberTable> subscribers;

    /**
     * The cached value of the '{@link #getCdcConnection() <em>Cdc Connection</em>}' reference.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getCdcConnection()
     * @generated
     * @ordered
     */
    protected CDCConnection cdcConnection;

    /**
     * The default value of the '{@link #getJournalName() <em>Journal Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJournalName()
     * @generated
     * @ordered
     */
    protected static final String JOURNAL_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJournalName() <em>Journal Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJournalName()
     * @generated
     * @ordered
     */
    protected String journalName = JOURNAL_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected CDCTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CDC_TYPE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLinkDB() {
        return linkDB;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLinkDB(String newLinkDB) {
        String oldLinkDB = linkDB;
        linkDB = newLinkDB;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CDC_TYPE__LINK_DB, oldLinkDB, linkDB));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<SubscriberTable> getSubscribers() {
        if (subscribers == null) {
            subscribers = new EObjectContainmentEList.Resolving<SubscriberTable>(SubscriberTable.class, this,
                    ConnectionPackage.CDC_TYPE__SUBSCRIBERS);
        }
        return subscribers;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CDCConnection getCdcConnection() {
        if (cdcConnection != null && cdcConnection.eIsProxy()) {
            InternalEObject oldCdcConnection = (InternalEObject) cdcConnection;
            cdcConnection = (CDCConnection) eResolveProxy(oldCdcConnection);
            if (cdcConnection != oldCdcConnection) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.CDC_TYPE__CDC_CONNECTION,
                            oldCdcConnection, cdcConnection));
            }
        }
        return cdcConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CDCConnection basicGetCdcConnection() {
        return cdcConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setCdcConnection(CDCConnection newCdcConnection) {
        CDCConnection oldCdcConnection = cdcConnection;
        cdcConnection = newCdcConnection;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CDC_TYPE__CDC_CONNECTION, oldCdcConnection,
                    cdcConnection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJournalName() {
        return journalName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJournalName(String newJournalName) {
        String oldJournalName = journalName;
        journalName = newJournalName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CDC_TYPE__JOURNAL_NAME, oldJournalName,
                    journalName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CDC_TYPE__SUBSCRIBERS:
            return ((InternalEList<?>) getSubscribers()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.CDC_TYPE__LINK_DB:
            return getLinkDB();
        case ConnectionPackage.CDC_TYPE__SUBSCRIBERS:
            return getSubscribers();
        case ConnectionPackage.CDC_TYPE__CDC_CONNECTION:
            if (resolve)
                return getCdcConnection();
            return basicGetCdcConnection();
        case ConnectionPackage.CDC_TYPE__JOURNAL_NAME:
            return getJournalName();
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
        case ConnectionPackage.CDC_TYPE__LINK_DB:
            setLinkDB((String) newValue);
            return;
        case ConnectionPackage.CDC_TYPE__SUBSCRIBERS:
            getSubscribers().clear();
            getSubscribers().addAll((Collection<? extends SubscriberTable>) newValue);
            return;
        case ConnectionPackage.CDC_TYPE__CDC_CONNECTION:
            setCdcConnection((CDCConnection) newValue);
            return;
        case ConnectionPackage.CDC_TYPE__JOURNAL_NAME:
            setJournalName((String) newValue);
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
        case ConnectionPackage.CDC_TYPE__LINK_DB:
            setLinkDB(LINK_DB_EDEFAULT);
            return;
        case ConnectionPackage.CDC_TYPE__SUBSCRIBERS:
            getSubscribers().clear();
            return;
        case ConnectionPackage.CDC_TYPE__CDC_CONNECTION:
            setCdcConnection((CDCConnection) null);
            return;
        case ConnectionPackage.CDC_TYPE__JOURNAL_NAME:
            setJournalName(JOURNAL_NAME_EDEFAULT);
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
        case ConnectionPackage.CDC_TYPE__LINK_DB:
            return LINK_DB_EDEFAULT == null ? linkDB != null : !LINK_DB_EDEFAULT.equals(linkDB);
        case ConnectionPackage.CDC_TYPE__SUBSCRIBERS:
            return subscribers != null && !subscribers.isEmpty();
        case ConnectionPackage.CDC_TYPE__CDC_CONNECTION:
            return cdcConnection != null;
        case ConnectionPackage.CDC_TYPE__JOURNAL_NAME:
            return JOURNAL_NAME_EDEFAULT == null ? journalName != null : !JOURNAL_NAME_EDEFAULT.equals(journalName);
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
        result.append(" (linkDB: ");
        result.append(linkDB);
        result.append(", journalName: ");
        result.append(journalName);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        DatabaseConnection connection = getCdcConnection().getConnection();
        return connection == null ? true : connection.isReadOnly();
    }
} // CDCTypeImpl
