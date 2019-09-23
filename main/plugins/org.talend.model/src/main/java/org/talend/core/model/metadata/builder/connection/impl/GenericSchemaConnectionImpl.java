/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generic Schema Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.GenericSchemaConnectionImpl#isMappingTypeUsed <em>Mapping Type Used</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.GenericSchemaConnectionImpl#getMappingTypeId <em>Mapping Type Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GenericSchemaConnectionImpl extends ConnectionImpl implements GenericSchemaConnection {

    /**
     * The default value of the '{@link #isMappingTypeUsed() <em>Mapping Type Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMappingTypeUsed()
     * @generated
     * @ordered
     */
    protected static final boolean MAPPING_TYPE_USED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isMappingTypeUsed() <em>Mapping Type Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMappingTypeUsed()
     * @generated
     * @ordered
     */
    protected boolean mappingTypeUsed = MAPPING_TYPE_USED_EDEFAULT;

    /**
     * The default value of the '{@link #getMappingTypeId() <em>Mapping Type Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMappingTypeId()
     * @generated
     * @ordered
     */
    protected static final String MAPPING_TYPE_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMappingTypeId() <em>Mapping Type Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMappingTypeId()
     * @generated
     * @ordered
     */
    protected String mappingTypeId = MAPPING_TYPE_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GenericSchemaConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.GENERIC_SCHEMA_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isMappingTypeUsed() {
        return mappingTypeUsed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMappingTypeUsed(boolean newMappingTypeUsed) {
        boolean oldMappingTypeUsed = mappingTypeUsed;
        mappingTypeUsed = newMappingTypeUsed;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED,
                    oldMappingTypeUsed, mappingTypeUsed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMappingTypeId() {
        return mappingTypeId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMappingTypeId(String newMappingTypeId) {
        String oldMappingTypeId = mappingTypeId;
        mappingTypeId = newMappingTypeId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID,
                    oldMappingTypeId, mappingTypeId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED:
            return isMappingTypeUsed();
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID:
            return getMappingTypeId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED:
            setMappingTypeUsed((Boolean) newValue);
            return;
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID:
            setMappingTypeId((String) newValue);
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
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED:
            setMappingTypeUsed(MAPPING_TYPE_USED_EDEFAULT);
            return;
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID:
            setMappingTypeId(MAPPING_TYPE_ID_EDEFAULT);
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
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_USED:
            return mappingTypeUsed != MAPPING_TYPE_USED_EDEFAULT;
        case ConnectionPackage.GENERIC_SCHEMA_CONNECTION__MAPPING_TYPE_ID:
            return MAPPING_TYPE_ID_EDEFAULT == null ? mappingTypeId != null : !MAPPING_TYPE_ID_EDEFAULT.equals(mappingTypeId);
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
        result.append(" (mappingTypeUsed: ");
        result.append(mappingTypeUsed);
        result.append(", mappingTypeId: ");
        result.append(mappingTypeId);
        result.append(')');
        return result.toString();
    }

} //GenericSchemaConnectionImpl
