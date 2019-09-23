/**
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAP Function Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#isChanging <em>Changing</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getTestValue <em>Test Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterImpl#isTableResideInTables <em>Table Reside In Tables</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPFunctionParameterImpl extends EObjectImpl implements SAPFunctionParameter {

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDescription()
     * @generated
     * @ordered
     */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDescription()
     * @generated
     * @ordered
     */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
     * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected static final String LENGTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected String length = LENGTH_EDEFAULT;

    /**
     * The default value of the '{@link #isChanging() <em>Changing</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChanging()
     * @generated
     * @ordered
     */
    protected static final boolean CHANGING_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isChanging() <em>Changing</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChanging()
     * @generated
     * @ordered
     */
    protected boolean changing = CHANGING_EDEFAULT;

    /**
     * The default value of the '{@link #getTestValue() <em>Test Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTestValue()
     * @generated
     * @ordered
     */
    protected static final String TEST_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTestValue() <em>Test Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTestValue()
     * @generated
     * @ordered
     */
    protected String testValue = TEST_VALUE_EDEFAULT;

    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<SAPFunctionParameter> children;

    /**
     * The default value of the '{@link #isTableResideInTables() <em>Table Reside In Tables</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTableResideInTables()
     * @generated
     * @ordered
     */
    protected static final boolean TABLE_RESIDE_IN_TABLES_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isTableResideInTables() <em>Table Reside In Tables</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTableResideInTables()
     * @generated
     * @ordered
     */
    protected boolean tableResideInTables = TABLE_RESIDE_IN_TABLES_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPFunctionParameterImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_FUNCTION_PARAMETER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__DESCRIPTION,
                    oldDescription, description));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLength() {
        return length;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLength(String newLength) {
        String oldLength = length;
        length = newLength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__LENGTH, oldLength,
                    length));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isChanging() {
        return changing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setChanging(boolean newChanging) {
        boolean oldChanging = changing;
        changing = newChanging;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__CHANGING,
                    oldChanging, changing));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTestValue() {
        return testValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTestValue(String newTestValue) {
        String oldTestValue = testValue;
        testValue = newTestValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER__TEST_VALUE,
                    oldTestValue, testValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SAPFunctionParameter> getChildren() {
        if (children == null) {
            children = new EObjectContainmentEList.Resolving<SAPFunctionParameter>(SAPFunctionParameter.class, this,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isTableResideInTables() {
        return tableResideInTables;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTableResideInTables(boolean newTableResideInTables) {
        boolean oldTableResideInTables = tableResideInTables;
        tableResideInTables = newTableResideInTables;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES, oldTableResideInTables, tableResideInTables));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN:
            return ((InternalEList<?>) getChildren()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__NAME:
            return getName();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TYPE:
            return getType();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__DESCRIPTION:
            return getDescription();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__LENGTH:
            return getLength();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHANGING:
            return isChanging();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TEST_VALUE:
            return getTestValue();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN:
            return getChildren();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES:
            return isTableResideInTables();
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__NAME:
            setName((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TYPE:
            setType((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__DESCRIPTION:
            setDescription((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__LENGTH:
            setLength((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHANGING:
            setChanging((Boolean) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TEST_VALUE:
            setTestValue((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN:
            getChildren().clear();
            getChildren().addAll((Collection<? extends SAPFunctionParameter>) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES:
            setTableResideInTables((Boolean) newValue);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__NAME:
            setName(NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TYPE:
            setType(TYPE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__DESCRIPTION:
            setDescription(DESCRIPTION_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__LENGTH:
            setLength(LENGTH_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHANGING:
            setChanging(CHANGING_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TEST_VALUE:
            setTestValue(TEST_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN:
            getChildren().clear();
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES:
            setTableResideInTables(TABLE_RESIDE_IN_TABLES_EDEFAULT);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__NAME:
            return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TYPE:
            return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__DESCRIPTION:
            return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__LENGTH:
            return LENGTH_EDEFAULT == null ? length != null : !LENGTH_EDEFAULT.equals(length);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHANGING:
            return changing != CHANGING_EDEFAULT;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TEST_VALUE:
            return TEST_VALUE_EDEFAULT == null ? testValue != null : !TEST_VALUE_EDEFAULT.equals(testValue);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__CHILDREN:
            return children != null && !children.isEmpty();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER__TABLE_RESIDE_IN_TABLES:
            return tableResideInTables != TABLE_RESIDE_IN_TABLES_EDEFAULT;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", type: ");
        result.append(type);
        result.append(", description: ");
        result.append(description);
        result.append(", length: ");
        result.append(length);
        result.append(", changing: ");
        result.append(changing);
        result.append(", testValue: ");
        result.append(testValue);
        result.append(", tableResideInTables: ");
        result.append(tableResideInTables);
        result.append(')');
        return result.toString();
    }

} //SAPFunctionParameterImpl
