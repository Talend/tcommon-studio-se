/**
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPBWTableField;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAPBW Table Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableFieldImpl#getLogicalName <em>Logical Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPBWTableFieldImpl extends SAPTableFieldImpl implements SAPBWTableField {

    /**
     * The default value of the '{@link #getLogicalName() <em>Logical Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLogicalName()
     * @generated
     * @ordered
     */
    protected static final String LOGICAL_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLogicalName() <em>Logical Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLogicalName()
     * @generated
     * @ordered
     */
    protected String logicalName = LOGICAL_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPBWTableFieldImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAPBW_TABLE_FIELD;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLogicalName() {
        return logicalName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLogicalName(String newLogicalName) {
        String oldLogicalName = logicalName;
        logicalName = newLogicalName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE_FIELD__LOGICAL_NAME,
                    oldLogicalName, logicalName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAPBW_TABLE_FIELD__LOGICAL_NAME:
            return getLogicalName();
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
        case ConnectionPackage.SAPBW_TABLE_FIELD__LOGICAL_NAME:
            setLogicalName((String) newValue);
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
        case ConnectionPackage.SAPBW_TABLE_FIELD__LOGICAL_NAME:
            setLogicalName(LOGICAL_NAME_EDEFAULT);
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
        case ConnectionPackage.SAPBW_TABLE_FIELD__LOGICAL_NAME:
            return LOGICAL_NAME_EDEFAULT == null ? logicalName != null : !LOGICAL_NAME_EDEFAULT.equals(logicalName);
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
        result.append(" (logicalName: ");
        result.append(logicalName);
        result.append(')');
        return result.toString();
    }

} //SAPBWTableFieldImpl
