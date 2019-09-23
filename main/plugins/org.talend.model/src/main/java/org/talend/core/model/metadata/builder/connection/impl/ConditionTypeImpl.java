/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.talend.core.model.metadata.builder.connection.ConditionType;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.Function;
import org.talend.core.model.metadata.builder.connection.LogicalOperator;
import org.talend.core.model.metadata.builder.connection.Operator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Condition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConditionTypeImpl#getInputColumn <em>Input Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConditionTypeImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConditionTypeImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConditionTypeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConditionTypeImpl extends EObjectImpl implements ConditionType {

    /**
     * The default value of the '{@link #getInputColumn() <em>Input Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputColumn()
     * @generated
     * @ordered
     */
    protected static final String INPUT_COLUMN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInputColumn() <em>Input Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputColumn()
     * @generated
     * @ordered
     */
    protected String inputColumn = INPUT_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #getFunction() <em>Function</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFunction()
     * @generated
     * @ordered
     */
    protected static final Function FUNCTION_EDEFAULT = Function.EMPTY;

    /**
     * The cached value of the '{@link #getFunction() <em>Function</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFunction()
     * @generated
     * @ordered
     */
    protected Function function = FUNCTION_EDEFAULT;

    /**
     * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperator()
     * @generated
     * @ordered
     */
    protected static final Operator OPERATOR_EDEFAULT = Operator.EQUALS;

    /**
     * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperator()
     * @generated
     * @ordered
     */
    protected Operator operator = OPERATOR_EDEFAULT;

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConditionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CONDITION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInputColumn() {
        return inputColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputColumn(String newInputColumn) {
        String oldInputColumn = inputColumn;
        inputColumn = newInputColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONDITION_TYPE__INPUT_COLUMN, oldInputColumn,
                    inputColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Function getFunction() {
        return function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFunction(Function newFunction) {
        Function oldFunction = function;
        function = newFunction == null ? FUNCTION_EDEFAULT : newFunction;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONDITION_TYPE__FUNCTION, oldFunction,
                    function));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperator(Operator newOperator) {
        Operator oldOperator = operator;
        operator = newOperator == null ? OPERATOR_EDEFAULT : newOperator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONDITION_TYPE__OPERATOR, oldOperator,
                    operator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONDITION_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.CONDITION_TYPE__INPUT_COLUMN:
            return getInputColumn();
        case ConnectionPackage.CONDITION_TYPE__FUNCTION:
            return getFunction();
        case ConnectionPackage.CONDITION_TYPE__OPERATOR:
            return getOperator();
        case ConnectionPackage.CONDITION_TYPE__VALUE:
            return getValue();
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
        case ConnectionPackage.CONDITION_TYPE__INPUT_COLUMN:
            setInputColumn((String) newValue);
            return;
        case ConnectionPackage.CONDITION_TYPE__FUNCTION:
            setFunction((Function) newValue);
            return;
        case ConnectionPackage.CONDITION_TYPE__OPERATOR:
            setOperator((Operator) newValue);
            return;
        case ConnectionPackage.CONDITION_TYPE__VALUE:
            setValue((String) newValue);
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
        case ConnectionPackage.CONDITION_TYPE__INPUT_COLUMN:
            setInputColumn(INPUT_COLUMN_EDEFAULT);
            return;
        case ConnectionPackage.CONDITION_TYPE__FUNCTION:
            setFunction(FUNCTION_EDEFAULT);
            return;
        case ConnectionPackage.CONDITION_TYPE__OPERATOR:
            setOperator(OPERATOR_EDEFAULT);
            return;
        case ConnectionPackage.CONDITION_TYPE__VALUE:
            setValue(VALUE_EDEFAULT);
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
        case ConnectionPackage.CONDITION_TYPE__INPUT_COLUMN:
            return INPUT_COLUMN_EDEFAULT == null ? inputColumn != null : !INPUT_COLUMN_EDEFAULT.equals(inputColumn);
        case ConnectionPackage.CONDITION_TYPE__FUNCTION:
            return function != FUNCTION_EDEFAULT;
        case ConnectionPackage.CONDITION_TYPE__OPERATOR:
            return operator != OPERATOR_EDEFAULT;
        case ConnectionPackage.CONDITION_TYPE__VALUE:
            return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
        result.append(" (inputColumn: ");
        result.append(inputColumn);
        result.append(", function: ");
        result.append(function);
        result.append(", operator: ");
        result.append(operator);
        result.append(", value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

} //ConditionTypeImpl
