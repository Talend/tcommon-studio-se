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
import org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Input SAP Function Parameter Table</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.InputSAPFunctionParameterTableImpl#getFunctionUnit <em>Function Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputSAPFunctionParameterTableImpl extends SAPFunctionParameterTableImpl implements InputSAPFunctionParameterTable {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected InputSAPFunctionParameterTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.INPUT_SAP_FUNCTION_PARAMETER_TABLE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionUnit getFunctionUnit() {
        if (eContainerFeatureID() != ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT)
            return null;
        return (SAPFunctionUnit) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionUnit basicGetFunctionUnit() {
        if (eContainerFeatureID() != ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT)
            return null;
        return (SAPFunctionUnit) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFunctionUnit(SAPFunctionUnit newFunctionUnit, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newFunctionUnit,
                ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFunctionUnit(SAPFunctionUnit newFunctionUnit) {
        if (newFunctionUnit != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT && newFunctionUnit != null)) {
            if (EcoreUtil.isAncestor(this, newFunctionUnit))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newFunctionUnit != null)
                msgs = ((InternalEObject) newFunctionUnit).eInverseAdd(this,
                        ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE, SAPFunctionUnit.class, msgs);
            msgs = basicSetFunctionUnit(newFunctionUnit, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT, newFunctionUnit, newFunctionUnit));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetFunctionUnit((SAPFunctionUnit) otherEnd, msgs);
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            return basicSetFunctionUnit(null, msgs);
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE,
                    SAPFunctionUnit.class, msgs);
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            if (resolve)
                return getFunctionUnit();
            return basicGetFunctionUnit();
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            setFunctionUnit((SAPFunctionUnit) newValue);
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            setFunctionUnit((SAPFunctionUnit) null);
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
        case ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT:
            return basicGetFunctionUnit() != null;
        }
        return super.eIsSet(featureID);
    }

    public boolean isReadOnly() {
        Connection connection = getFunctionUnit().getConnection();
        return connection == null ? false : connection.isReadOnly();
    }

} // InputSAPFunctionParameterTableImpl
