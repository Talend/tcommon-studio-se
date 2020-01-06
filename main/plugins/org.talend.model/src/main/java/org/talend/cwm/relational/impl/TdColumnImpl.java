package org.talend.cwm.relational.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Td Column</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.cwm.relational.impl.TdColumnImpl#getSqlDataType <em>Sql Data Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TdColumnImpl extends MetadataColumnImpl implements TdColumn {

    /**
     * The cached value of the '{@link #getSqlDataType() <em>Sql Data Type</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSqlDataType()
     * @generated
     * @ordered
     */
    protected TdSqlDataType sqlDataType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected TdColumnImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RelationalPackage.Literals.TD_COLUMN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TdSqlDataType getSqlDataType() {
        if (sqlDataType != null && sqlDataType.eIsProxy()) {
            InternalEObject oldSqlDataType = (InternalEObject) sqlDataType;
            sqlDataType = (TdSqlDataType) eResolveProxy(oldSqlDataType);
            if (sqlDataType != oldSqlDataType) {
                InternalEObject newSqlDataType = (InternalEObject) sqlDataType;
                NotificationChain msgs = oldSqlDataType.eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - RelationalPackage.TD_COLUMN__SQL_DATA_TYPE, null, null);
                if (newSqlDataType.eInternalContainer() == null) {
                    msgs = newSqlDataType.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RelationalPackage.TD_COLUMN__SQL_DATA_TYPE,
                            null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RelationalPackage.TD_COLUMN__SQL_DATA_TYPE,
                            oldSqlDataType, sqlDataType));
            }
        }
        return sqlDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TdSqlDataType basicGetSqlDataType() {
        return sqlDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSqlDataType(TdSqlDataType newSqlDataType, NotificationChain msgs) {
        TdSqlDataType oldSqlDataType = sqlDataType;
        sqlDataType = newSqlDataType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    RelationalPackage.TD_COLUMN__SQL_DATA_TYPE, oldSqlDataType, newSqlDataType);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSqlDataType(TdSqlDataType newSqlDataType) {
        if (newSqlDataType != sqlDataType) {
            NotificationChain msgs = null;
            if (sqlDataType != null)
                msgs = ((InternalEObject) sqlDataType).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - RelationalPackage.TD_COLUMN__SQL_DATA_TYPE, null, msgs);
            if (newSqlDataType != null)
                msgs = ((InternalEObject) newSqlDataType).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - RelationalPackage.TD_COLUMN__SQL_DATA_TYPE, null, msgs);
            msgs = basicSetSqlDataType(newSqlDataType, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_COLUMN__SQL_DATA_TYPE, newSqlDataType,
                    newSqlDataType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setContentType(String contentType) {
        TaggedValueHelper.setTaggedValue(this, TaggedValueHelper.DATA_CONTENT_TYPE_TAGGED_VAL, contentType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getContentType() {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DATA_CONTENT_TYPE_TAGGED_VAL, this.getTaggedValue());
        if (tv == null) {
            return "";
        }
        return tv.getValue();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public int getJavaType() {
        return getSqlDataType().getJavaDataType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case RelationalPackage.TD_COLUMN__SQL_DATA_TYPE:
            return basicSetSqlDataType(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case RelationalPackage.TD_COLUMN__SQL_DATA_TYPE:
            if (resolve)
                return getSqlDataType();
            return basicGetSqlDataType();
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
        case RelationalPackage.TD_COLUMN__SQL_DATA_TYPE:
            setSqlDataType((TdSqlDataType) newValue);
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
        case RelationalPackage.TD_COLUMN__SQL_DATA_TYPE:
            setSqlDataType((TdSqlDataType) null);
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
        case RelationalPackage.TD_COLUMN__SQL_DATA_TYPE:
            return sqlDataType != null;
        }
        return super.eIsSet(featureID);
    }

} // TdColumnImpl
