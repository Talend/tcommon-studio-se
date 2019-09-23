/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAP Function Parameter Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterTableImpl#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPFunctionParameterTableImpl extends AbstractMetadataObjectImpl implements SAPFunctionParameterTable {

    /**
     * The cached value of the '{@link #getColumns() <em>Columns</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumns()
     * @generated
     * @ordered
     */
    protected EList<SAPFunctionParameterColumn> columns;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPFunctionParameterTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_FUNCTION_PARAMETER_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SAPFunctionParameterColumn> getColumns() {
        if (columns == null) {
            columns = new EObjectContainmentWithInverseEList.Resolving<SAPFunctionParameterColumn>(
                    SAPFunctionParameterColumn.class, this, ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE);
        }
        return columns;
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getColumns()).basicAdd(otherEnd, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            return ((InternalEList<?>) getColumns()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            return getColumns();
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            getColumns().clear();
            getColumns().addAll((Collection<? extends SAPFunctionParameterColumn>) newValue);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            getColumns().clear();
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS:
            return columns != null && !columns.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //SAPFunctionParameterTableImpl
