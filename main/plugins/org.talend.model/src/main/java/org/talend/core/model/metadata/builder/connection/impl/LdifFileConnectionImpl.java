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
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.SchemaAttribute;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ldif File Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LdifFileConnectionImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LdifFileConnectionImpl#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LdifFileConnectionImpl#getLimitEntry <em>Limit Entry</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LdifFileConnectionImpl#isUseLimit <em>Use Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.LdifFileConnectionImpl#getServer <em>Server</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LdifFileConnectionImpl extends ConnectionImpl implements LdifFileConnection {

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected EList<String> value;

    /**
     * The default value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected static final String FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected String filePath = FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getLimitEntry() <em>Limit Entry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitEntry()
     * @generated
     * @ordered
     */
    protected static final int LIMIT_ENTRY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getLimitEntry() <em>Limit Entry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimitEntry()
     * @generated
     * @ordered
     */
    protected int limitEntry = LIMIT_ENTRY_EDEFAULT;

    /**
     * The default value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected static final boolean USE_LIMIT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseLimit() <em>Use Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseLimit()
     * @generated
     * @ordered
     */
    protected boolean useLimit = USE_LIMIT_EDEFAULT;

    /**
     * The default value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected static final String SERVER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServer() <em>Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServer()
     * @generated
     * @ordered
     */
    protected String server = SERVER_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LdifFileConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.LDIF_FILE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getValue() {
        if (value == null) {
            value = new EDataTypeUniqueEList<String>(String.class, this, ConnectionPackage.LDIF_FILE_CONNECTION__VALUE);
        }
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilePath(String newFilePath) {
        String oldFilePath = filePath;
        filePath = newFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDIF_FILE_CONNECTION__FILE_PATH, oldFilePath,
                    filePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getLimitEntry() {
        return limitEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLimitEntry(int newLimitEntry) {
        int oldLimitEntry = limitEntry;
        limitEntry = newLimitEntry;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDIF_FILE_CONNECTION__LIMIT_ENTRY,
                    oldLimitEntry, limitEntry));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseLimit() {
        return useLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseLimit(boolean newUseLimit) {
        boolean oldUseLimit = useLimit;
        useLimit = newUseLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDIF_FILE_CONNECTION__USE_LIMIT, oldUseLimit,
                    useLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServer() {
        return server;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServer(String newServer) {
        String oldServer = server;
        server = newServer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.LDIF_FILE_CONNECTION__SERVER, oldServer,
                    server));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.LDIF_FILE_CONNECTION__VALUE:
            return getValue();
        case ConnectionPackage.LDIF_FILE_CONNECTION__FILE_PATH:
            return getFilePath();
        case ConnectionPackage.LDIF_FILE_CONNECTION__LIMIT_ENTRY:
            return getLimitEntry();
        case ConnectionPackage.LDIF_FILE_CONNECTION__USE_LIMIT:
            return isUseLimit();
        case ConnectionPackage.LDIF_FILE_CONNECTION__SERVER:
            return getServer();
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
        case ConnectionPackage.LDIF_FILE_CONNECTION__VALUE:
            getValue().clear();
            getValue().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__FILE_PATH:
            setFilePath((String) newValue);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__LIMIT_ENTRY:
            setLimitEntry((Integer) newValue);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__USE_LIMIT:
            setUseLimit((Boolean) newValue);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__SERVER:
            setServer((String) newValue);
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
        case ConnectionPackage.LDIF_FILE_CONNECTION__VALUE:
            getValue().clear();
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__FILE_PATH:
            setFilePath(FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__LIMIT_ENTRY:
            setLimitEntry(LIMIT_ENTRY_EDEFAULT);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__USE_LIMIT:
            setUseLimit(USE_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.LDIF_FILE_CONNECTION__SERVER:
            setServer(SERVER_EDEFAULT);
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
        case ConnectionPackage.LDIF_FILE_CONNECTION__VALUE:
            return value != null && !value.isEmpty();
        case ConnectionPackage.LDIF_FILE_CONNECTION__FILE_PATH:
            return FILE_PATH_EDEFAULT == null ? filePath != null : !FILE_PATH_EDEFAULT.equals(filePath);
        case ConnectionPackage.LDIF_FILE_CONNECTION__LIMIT_ENTRY:
            return limitEntry != LIMIT_ENTRY_EDEFAULT;
        case ConnectionPackage.LDIF_FILE_CONNECTION__USE_LIMIT:
            return useLimit != USE_LIMIT_EDEFAULT;
        case ConnectionPackage.LDIF_FILE_CONNECTION__SERVER:
            return SERVER_EDEFAULT == null ? server != null : !SERVER_EDEFAULT.equals(server);
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
        result.append(" (value: ");
        result.append(value);
        result.append(", FilePath: ");
        result.append(filePath);
        result.append(", LimitEntry: ");
        result.append(limitEntry);
        result.append(", UseLimit: ");
        result.append(useLimit);
        result.append(", Server: ");
        result.append(server);
        result.append(')');
        return result.toString();
    }

} //LdifFileConnectionImpl
