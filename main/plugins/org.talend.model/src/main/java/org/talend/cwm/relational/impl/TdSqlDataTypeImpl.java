/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdSqlDataType;

import orgomg.cwm.resource.relational.enumerations.NullableType;

import orgomg.cwm.resource.relational.impl.SQLSimpleTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Sql Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#getJavaDataType <em>Java Data Type</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#getNullable <em>Nullable</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#isUnsignedAttribute <em>Unsigned Attribute</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#isCaseSensitive <em>Case Sensitive</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#isAutoIncrement <em>Auto Increment</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#getLocalTypeName <em>Local Type Name</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdSqlDataTypeImpl#getSearchable <em>Searchable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdSqlDataTypeImpl extends SQLSimpleTypeImpl implements TdSqlDataType {

    /**
     * The default value of the '{@link #getJavaDataType() <em>Java Data Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaDataType()
     * @generated
     * @ordered
     */
    protected static final int JAVA_DATA_TYPE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getJavaDataType() <em>Java Data Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaDataType()
     * @generated
     * @ordered
     */
    protected int javaDataType = JAVA_DATA_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getNullable() <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNullable()
     * @generated
     * @ordered
     */
    protected static final NullableType NULLABLE_EDEFAULT = NullableType.COLUMN_NO_NULLS;

    /**
     * The cached value of the '{@link #getNullable() <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNullable()
     * @generated
     * @ordered
     */
    protected NullableType nullable = NULLABLE_EDEFAULT;

    /**
     * The default value of the '{@link #isUnsignedAttribute() <em>Unsigned Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUnsignedAttribute()
     * @generated
     * @ordered
     */
    protected static final boolean UNSIGNED_ATTRIBUTE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUnsignedAttribute() <em>Unsigned Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUnsignedAttribute()
     * @generated
     * @ordered
     */
    protected boolean unsignedAttribute = UNSIGNED_ATTRIBUTE_EDEFAULT;

    /**
     * The default value of the '{@link #isCaseSensitive() <em>Case Sensitive</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCaseSensitive()
     * @generated
     * @ordered
     */
    protected static final boolean CASE_SENSITIVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isCaseSensitive() <em>Case Sensitive</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCaseSensitive()
     * @generated
     * @ordered
     */
    protected boolean caseSensitive = CASE_SENSITIVE_EDEFAULT;

    /**
     * The default value of the '{@link #isAutoIncrement() <em>Auto Increment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAutoIncrement()
     * @generated
     * @ordered
     */
    protected static final boolean AUTO_INCREMENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAutoIncrement() <em>Auto Increment</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAutoIncrement()
     * @generated
     * @ordered
     */
    protected boolean autoIncrement = AUTO_INCREMENT_EDEFAULT;

    /**
     * The default value of the '{@link #getLocalTypeName() <em>Local Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocalTypeName()
     * @generated
     * @ordered
     */
    protected static final String LOCAL_TYPE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLocalTypeName() <em>Local Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocalTypeName()
     * @generated
     * @ordered
     */
    protected String localTypeName = LOCAL_TYPE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSearchable() <em>Searchable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSearchable()
     * @generated
     * @ordered
     */
    protected static final short SEARCHABLE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getSearchable() <em>Searchable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSearchable()
     * @generated
     * @ordered
     */
    protected short searchable = SEARCHABLE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdSqlDataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RelationalPackage.Literals.TD_SQL_DATA_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getJavaDataType() {
        return javaDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJavaDataType(int newJavaDataType) {
        int oldJavaDataType = javaDataType;
        javaDataType = newJavaDataType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__JAVA_DATA_TYPE,
                    oldJavaDataType, javaDataType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NullableType getNullable() {
        return nullable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNullable(NullableType newNullable) {
        NullableType oldNullable = nullable;
        nullable = newNullable == null ? NULLABLE_EDEFAULT : newNullable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__NULLABLE, oldNullable,
                    nullable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUnsignedAttribute() {
        return unsignedAttribute;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUnsignedAttribute(boolean newUnsignedAttribute) {
        boolean oldUnsignedAttribute = unsignedAttribute;
        unsignedAttribute = newUnsignedAttribute;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE,
                    oldUnsignedAttribute, unsignedAttribute));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCaseSensitive(boolean newCaseSensitive) {
        boolean oldCaseSensitive = caseSensitive;
        caseSensitive = newCaseSensitive;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__CASE_SENSITIVE,
                    oldCaseSensitive, caseSensitive));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAutoIncrement(boolean newAutoIncrement) {
        boolean oldAutoIncrement = autoIncrement;
        autoIncrement = newAutoIncrement;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__AUTO_INCREMENT,
                    oldAutoIncrement, autoIncrement));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLocalTypeName() {
        return localTypeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLocalTypeName(String newLocalTypeName) {
        String oldLocalTypeName = localTypeName;
        localTypeName = newLocalTypeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME,
                    oldLocalTypeName, localTypeName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public short getSearchable() {
        return searchable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSearchable(short newSearchable) {
        short oldSearchable = searchable;
        searchable = newSearchable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_SQL_DATA_TYPE__SEARCHABLE, oldSearchable,
                    searchable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case RelationalPackage.TD_SQL_DATA_TYPE__JAVA_DATA_TYPE:
            return getJavaDataType();
        case RelationalPackage.TD_SQL_DATA_TYPE__NULLABLE:
            return getNullable();
        case RelationalPackage.TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE:
            return isUnsignedAttribute();
        case RelationalPackage.TD_SQL_DATA_TYPE__CASE_SENSITIVE:
            return isCaseSensitive();
        case RelationalPackage.TD_SQL_DATA_TYPE__AUTO_INCREMENT:
            return isAutoIncrement();
        case RelationalPackage.TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME:
            return getLocalTypeName();
        case RelationalPackage.TD_SQL_DATA_TYPE__SEARCHABLE:
            return getSearchable();
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
        case RelationalPackage.TD_SQL_DATA_TYPE__JAVA_DATA_TYPE:
            setJavaDataType((Integer) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__NULLABLE:
            setNullable((NullableType) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE:
            setUnsignedAttribute((Boolean) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__CASE_SENSITIVE:
            setCaseSensitive((Boolean) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__AUTO_INCREMENT:
            setAutoIncrement((Boolean) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME:
            setLocalTypeName((String) newValue);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__SEARCHABLE:
            setSearchable((Short) newValue);
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
        case RelationalPackage.TD_SQL_DATA_TYPE__JAVA_DATA_TYPE:
            setJavaDataType(JAVA_DATA_TYPE_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__NULLABLE:
            setNullable(NULLABLE_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE:
            setUnsignedAttribute(UNSIGNED_ATTRIBUTE_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__CASE_SENSITIVE:
            setCaseSensitive(CASE_SENSITIVE_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__AUTO_INCREMENT:
            setAutoIncrement(AUTO_INCREMENT_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME:
            setLocalTypeName(LOCAL_TYPE_NAME_EDEFAULT);
            return;
        case RelationalPackage.TD_SQL_DATA_TYPE__SEARCHABLE:
            setSearchable(SEARCHABLE_EDEFAULT);
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
        case RelationalPackage.TD_SQL_DATA_TYPE__JAVA_DATA_TYPE:
            return javaDataType != JAVA_DATA_TYPE_EDEFAULT;
        case RelationalPackage.TD_SQL_DATA_TYPE__NULLABLE:
            return nullable != NULLABLE_EDEFAULT;
        case RelationalPackage.TD_SQL_DATA_TYPE__UNSIGNED_ATTRIBUTE:
            return unsignedAttribute != UNSIGNED_ATTRIBUTE_EDEFAULT;
        case RelationalPackage.TD_SQL_DATA_TYPE__CASE_SENSITIVE:
            return caseSensitive != CASE_SENSITIVE_EDEFAULT;
        case RelationalPackage.TD_SQL_DATA_TYPE__AUTO_INCREMENT:
            return autoIncrement != AUTO_INCREMENT_EDEFAULT;
        case RelationalPackage.TD_SQL_DATA_TYPE__LOCAL_TYPE_NAME:
            return LOCAL_TYPE_NAME_EDEFAULT == null ? localTypeName != null : !LOCAL_TYPE_NAME_EDEFAULT.equals(localTypeName);
        case RelationalPackage.TD_SQL_DATA_TYPE__SEARCHABLE:
            return searchable != SEARCHABLE_EDEFAULT;
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
        result.append(" (javaDataType: ");
        result.append(javaDataType);
        result.append(", nullable: ");
        result.append(nullable);
        result.append(", unsignedAttribute: ");
        result.append(unsignedAttribute);
        result.append(", caseSensitive: ");
        result.append(caseSensitive);
        result.append(", autoIncrement: ");
        result.append(autoIncrement);
        result.append(", localTypeName: ");
        result.append(localTypeName);
        result.append(", searchable: ");
        result.append(searchable);
        result.append(')');
        return result.toString();
    }

} //TdSqlDataTypeImpl
