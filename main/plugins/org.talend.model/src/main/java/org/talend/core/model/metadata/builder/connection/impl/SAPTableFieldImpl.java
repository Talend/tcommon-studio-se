/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPTableField;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAP Table Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPTableFieldImpl#getBusinessName <em>Business Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPTableFieldImpl#getRefTable <em>Ref Table</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPTableFieldImpl extends MetadataColumnImpl implements SAPTableField {

    /**
     * The default value of the '{@link #getBusinessName() <em>Business Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBusinessName()
     * @generated
     * @ordered
     */
    protected static final String BUSINESS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBusinessName() <em>Business Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBusinessName()
     * @generated
     * @ordered
     */
    protected String businessName = BUSINESS_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getRefTable() <em>Ref Table</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefTable()
     * @generated
     * @ordered
     */
    protected EList<String> refTable;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPTableFieldImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_TABLE_FIELD;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBusinessName(String newBusinessName) {
        String oldBusinessName = businessName;
        businessName = newBusinessName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_TABLE_FIELD__BUSINESS_NAME,
                    oldBusinessName, businessName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getRefTable() {
        if (refTable == null) {
            refTable = new EDataTypeUniqueEList<String>(String.class, this, ConnectionPackage.SAP_TABLE_FIELD__REF_TABLE);
        }
        return refTable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAP_TABLE_FIELD__BUSINESS_NAME:
            return getBusinessName();
        case ConnectionPackage.SAP_TABLE_FIELD__REF_TABLE:
            return getRefTable();
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
        case ConnectionPackage.SAP_TABLE_FIELD__BUSINESS_NAME:
            setBusinessName((String) newValue);
            return;
        case ConnectionPackage.SAP_TABLE_FIELD__REF_TABLE:
            getRefTable().clear();
            getRefTable().addAll((Collection<? extends String>) newValue);
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
        case ConnectionPackage.SAP_TABLE_FIELD__BUSINESS_NAME:
            setBusinessName(BUSINESS_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAP_TABLE_FIELD__REF_TABLE:
            getRefTable().clear();
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
        case ConnectionPackage.SAP_TABLE_FIELD__BUSINESS_NAME:
            return BUSINESS_NAME_EDEFAULT == null ? businessName != null : !BUSINESS_NAME_EDEFAULT.equals(businessName);
        case ConnectionPackage.SAP_TABLE_FIELD__REF_TABLE:
            return refTable != null && !refTable.isEmpty();
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
        result.append(" (businessName: ");
        result.append(businessName);
        result.append(", refTable: ");
        result.append(refTable);
        result.append(')');
        return result.toString();
    }

} //SAPTableFieldImpl
