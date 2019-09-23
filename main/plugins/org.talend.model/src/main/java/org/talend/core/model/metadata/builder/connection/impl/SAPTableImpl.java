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
import org.talend.core.model.metadata.builder.connection.SAPTable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAP Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPTableImpl#getTableSearchType <em>Table Search Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPTableImpl extends MetadataTableImpl implements SAPTable {

    /**
     * The default value of the '{@link #getTableSearchType() <em>Table Search Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTableSearchType()
     * @generated
     * @ordered
     */
    protected static final String TABLE_SEARCH_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTableSearchType() <em>Table Search Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTableSearchType()
     * @generated
     * @ordered
     */
    protected String tableSearchType = TABLE_SEARCH_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTableSearchType() {
        return tableSearchType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTableSearchType(String newTableSearchType) {
        String oldTableSearchType = tableSearchType;
        tableSearchType = newTableSearchType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_TABLE__TABLE_SEARCH_TYPE,
                    oldTableSearchType, tableSearchType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAP_TABLE__TABLE_SEARCH_TYPE:
            return getTableSearchType();
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
        case ConnectionPackage.SAP_TABLE__TABLE_SEARCH_TYPE:
            setTableSearchType((String) newValue);
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
        case ConnectionPackage.SAP_TABLE__TABLE_SEARCH_TYPE:
            setTableSearchType(TABLE_SEARCH_TYPE_EDEFAULT);
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
        case ConnectionPackage.SAP_TABLE__TABLE_SEARCH_TYPE:
            return TABLE_SEARCH_TYPE_EDEFAULT == null ? tableSearchType != null : !TABLE_SEARCH_TYPE_EDEFAULT
                    .equals(tableSearchType);
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
        result.append(" (tableSearchType: ");
        result.append(tableSearchType);
        result.append(')');
        return result.toString();
    }

} //SAPTableImpl
