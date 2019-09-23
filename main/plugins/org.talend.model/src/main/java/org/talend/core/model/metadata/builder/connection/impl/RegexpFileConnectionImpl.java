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
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Regexp File Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.RegexpFileConnectionImpl#getFieldSeparatorType <em>Field Separator Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegexpFileConnectionImpl extends FileConnectionImpl implements RegexpFileConnection {

    /**
     * The default value of the '{@link #getFieldSeparatorType() <em>Field Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFieldSeparatorType()
     * @generated
     * @ordered
     */
    protected static final FieldSeparator FIELD_SEPARATOR_TYPE_EDEFAULT = FieldSeparator.TABULATION_LITERAL;

    /**
     * The cached value of the '{@link #getFieldSeparatorType() <em>Field Separator Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFieldSeparatorType()
     * @generated
     * @ordered
     */
    protected FieldSeparator fieldSeparatorType = FIELD_SEPARATOR_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RegexpFileConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.REGEXP_FILE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FieldSeparator getFieldSeparatorType() {
        return fieldSeparatorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFieldSeparatorType(FieldSeparator newFieldSeparatorType) {
        FieldSeparator oldFieldSeparatorType = fieldSeparatorType;
        fieldSeparatorType = newFieldSeparatorType == null ? FIELD_SEPARATOR_TYPE_EDEFAULT : newFieldSeparatorType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE,
                    oldFieldSeparatorType, fieldSeparatorType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            return getFieldSeparatorType();
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
        case ConnectionPackage.REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            setFieldSeparatorType((FieldSeparator) newValue);
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
        case ConnectionPackage.REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            setFieldSeparatorType(FIELD_SEPARATOR_TYPE_EDEFAULT);
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
        case ConnectionPackage.REGEXP_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            return fieldSeparatorType != FIELD_SEPARATOR_TYPE_EDEFAULT;
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
        result.append(" (FieldSeparatorType: ");
        result.append(fieldSeparatorType);
        result.append(')');
        return result.toString();
    }

} //RegexpFileConnectionImpl
