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
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Delimited File Connection</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DelimitedFileConnectionImpl#getFieldSeparatorType <em>Field Separator Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.DelimitedFileConnectionImpl#isSplitRecord <em>Split Record</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DelimitedFileConnectionImpl extends FileConnectionImpl implements DelimitedFileConnection {

    /**
     * The default value of the '{@link #getFieldSeparatorType() <em>Field Separator Type</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFieldSeparatorType()
     * @generated
     * @ordered
     */
    protected static final FieldSeparator FIELD_SEPARATOR_TYPE_EDEFAULT = FieldSeparator.SEMICOLON_LITERAL;

    /**
     * The cached value of the '{@link #getFieldSeparatorType() <em>Field Separator Type</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFieldSeparatorType()
     * @generated
     * @ordered
     */
    protected FieldSeparator fieldSeparatorType = FIELD_SEPARATOR_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isSplitRecord() <em>Split Record</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSplitRecord()
     * @generated
     * @ordered
     */
    protected static final boolean SPLIT_RECORD_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSplitRecord() <em>Split Record</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSplitRecord()
     * @generated
     * @ordered
     */
    protected boolean splitRecord = SPLIT_RECORD_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected DelimitedFileConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.DELIMITED_FILE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public FieldSeparator getFieldSeparatorType() {
        return fieldSeparatorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFieldSeparatorType(FieldSeparator newFieldSeparatorType) {
        FieldSeparator oldFieldSeparatorType = fieldSeparatorType;
        fieldSeparatorType = newFieldSeparatorType == null ? FIELD_SEPARATOR_TYPE_EDEFAULT : newFieldSeparatorType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE, oldFieldSeparatorType, fieldSeparatorType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSplitRecord() {
        return splitRecord;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSplitRecord(boolean newSplitRecord) {
        boolean oldSplitRecord = splitRecord;
        splitRecord = newSplitRecord;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.DELIMITED_FILE_CONNECTION__SPLIT_RECORD,
                    oldSplitRecord, splitRecord));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            return getFieldSeparatorType();
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__SPLIT_RECORD:
            return isSplitRecord();
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
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            setFieldSeparatorType((FieldSeparator) newValue);
            return;
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__SPLIT_RECORD:
            setSplitRecord((Boolean) newValue);
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
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            setFieldSeparatorType(FIELD_SEPARATOR_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__SPLIT_RECORD:
            setSplitRecord(SPLIT_RECORD_EDEFAULT);
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
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__FIELD_SEPARATOR_TYPE:
            return fieldSeparatorType != FIELD_SEPARATOR_TYPE_EDEFAULT;
        case ConnectionPackage.DELIMITED_FILE_CONNECTION__SPLIT_RECORD:
            return splitRecord != SPLIT_RECORD_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (FieldSeparatorType: ");
        result.append(fieldSeparatorType);
        result.append(", splitRecord: ");
        result.append(splitRecord);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl#getConnectionTypeName()
     */
    @Override
    public String getConnectionTypeName() {
        return "File delimited"; //$NON-NLS-1$
    }

} // DelimitedFileConnectionImpl
