/**
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAP Function Param Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParamDataImpl#getInputRoot <em>Input Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParamDataImpl#getOutputRoot <em>Output Root</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SAPFunctionParamDataImpl extends EObjectImpl implements SAPFunctionParamData {

    /**
     * The cached value of the '{@link #getInputRoot() <em>Input Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputRoot()
     * @generated
     * @ordered
     */
    protected SAPFunctionParameter inputRoot;

    /**
     * The cached value of the '{@link #getOutputRoot() <em>Output Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputRoot()
     * @generated
     * @ordered
     */
    protected SAPFunctionParameter outputRoot;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPFunctionParamDataImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_FUNCTION_PARAM_DATA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameter getInputRoot() {
        if (inputRoot != null && inputRoot.eIsProxy()) {
            InternalEObject oldInputRoot = (InternalEObject) inputRoot;
            inputRoot = (SAPFunctionParameter) eResolveProxy(oldInputRoot);
            if (inputRoot != oldInputRoot) {
                InternalEObject newInputRoot = (InternalEObject) inputRoot;
                NotificationChain msgs = oldInputRoot.eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, null, null);
                if (newInputRoot.eInternalContainer() == null) {
                    msgs = newInputRoot.eInverseAdd(this,
                            EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, oldInputRoot, inputRoot));
            }
        }
        return inputRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameter basicGetInputRoot() {
        return inputRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInputRoot(SAPFunctionParameter newInputRoot, NotificationChain msgs) {
        SAPFunctionParameter oldInputRoot = inputRoot;
        inputRoot = newInputRoot;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, oldInputRoot, newInputRoot);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputRoot(SAPFunctionParameter newInputRoot) {
        if (newInputRoot != inputRoot) {
            NotificationChain msgs = null;
            if (inputRoot != null)
                msgs = ((InternalEObject) inputRoot).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, null, msgs);
            if (newInputRoot != null)
                msgs = ((InternalEObject) newInputRoot).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT, null, msgs);
            msgs = basicSetInputRoot(newInputRoot, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT,
                    newInputRoot, newInputRoot));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameter getOutputRoot() {
        if (outputRoot != null && outputRoot.eIsProxy()) {
            InternalEObject oldOutputRoot = (InternalEObject) outputRoot;
            outputRoot = (SAPFunctionParameter) eResolveProxy(oldOutputRoot);
            if (outputRoot != oldOutputRoot) {
                InternalEObject newOutputRoot = (InternalEObject) outputRoot;
                NotificationChain msgs = oldOutputRoot.eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, null, null);
                if (newOutputRoot.eInternalContainer() == null) {
                    msgs = newOutputRoot.eInverseAdd(this,
                            EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, oldOutputRoot, outputRoot));
            }
        }
        return outputRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameter basicGetOutputRoot() {
        return outputRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOutputRoot(SAPFunctionParameter newOutputRoot, NotificationChain msgs) {
        SAPFunctionParameter oldOutputRoot = outputRoot;
        outputRoot = newOutputRoot;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, oldOutputRoot, newOutputRoot);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputRoot(SAPFunctionParameter newOutputRoot) {
        if (newOutputRoot != outputRoot) {
            NotificationChain msgs = null;
            if (outputRoot != null)
                msgs = ((InternalEObject) outputRoot).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, null, msgs);
            if (newOutputRoot != null)
                msgs = ((InternalEObject) newOutputRoot).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT, null, msgs);
            msgs = basicSetOutputRoot(newOutputRoot, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT,
                    newOutputRoot, newOutputRoot));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT:
            return basicSetInputRoot(null, msgs);
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT:
            return basicSetOutputRoot(null, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT:
            if (resolve)
                return getInputRoot();
            return basicGetInputRoot();
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT:
            if (resolve)
                return getOutputRoot();
            return basicGetOutputRoot();
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
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT:
            setInputRoot((SAPFunctionParameter) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT:
            setOutputRoot((SAPFunctionParameter) newValue);
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
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT:
            setInputRoot((SAPFunctionParameter) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT:
            setOutputRoot((SAPFunctionParameter) null);
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
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__INPUT_ROOT:
            return inputRoot != null;
        case ConnectionPackage.SAP_FUNCTION_PARAM_DATA__OUTPUT_ROOT:
            return outputRoot != null;
        }
        return super.eIsSet(featureID);
    }

} //SAPFunctionParamDataImpl
