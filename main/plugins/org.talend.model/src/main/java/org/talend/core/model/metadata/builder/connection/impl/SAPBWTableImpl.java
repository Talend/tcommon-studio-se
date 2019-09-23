/**
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SAPBW Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl#getModelType <em>Model Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl#isActive <em>Active</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl#getSourceSystemName <em>Source System Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl#getInfoAreaName <em>Info Area Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl#getInnerIOType <em>Inner IO Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPBWTableImpl extends SAPTableImpl implements SAPBWTable {

    /**
     * The default value of the '{@link #getModelType() <em>Model Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModelType()
     * @generated
     * @ordered
     */
    protected static final String MODEL_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getModelType() <em>Model Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModelType()
     * @generated
     * @ordered
     */
    protected String modelType = MODEL_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #isActive() <em>Active</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isActive()
     * @generated
     * @ordered
     */
    protected static final boolean ACTIVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isActive() <em>Active</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isActive()
     * @generated
     * @ordered
     */
    protected boolean active = ACTIVE_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceSystemName() <em>Source System Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceSystemName()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_SYSTEM_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceSystemName() <em>Source System Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceSystemName()
     * @generated
     * @ordered
     */
    protected String sourceSystemName = SOURCE_SYSTEM_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getInfoAreaName() <em>Info Area Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInfoAreaName()
     * @generated
     * @ordered
     */
    protected static final String INFO_AREA_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInfoAreaName() <em>Info Area Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInfoAreaName()
     * @generated
     * @ordered
     */
    protected String infoAreaName = INFO_AREA_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getInnerIOType() <em>Inner IO Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInnerIOType()
     * @generated
     * @ordered
     */
    protected static final String INNER_IO_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInnerIOType() <em>Inner IO Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInnerIOType()
     * @generated
     * @ordered
     */
    protected String innerIOType = INNER_IO_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SAPBWTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAPBW_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getModelType() {
        return modelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setModelType(String newModelType) {
        String oldModelType = modelType;
        modelType = newModelType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE__MODEL_TYPE, oldModelType,
                    modelType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isActive() {
        return active;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setActive(boolean newActive) {
        boolean oldActive = active;
        active = newActive;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE__ACTIVE, oldActive, active));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSourceSystemName() {
        return sourceSystemName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceSystemName(String newSourceSystemName) {
        String oldSourceSystemName = sourceSystemName;
        sourceSystemName = newSourceSystemName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE__SOURCE_SYSTEM_NAME,
                    oldSourceSystemName, sourceSystemName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInfoAreaName() {
        return infoAreaName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInfoAreaName(String newInfoAreaName) {
        String oldInfoAreaName = infoAreaName;
        infoAreaName = newInfoAreaName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE__INFO_AREA_NAME, oldInfoAreaName,
                    infoAreaName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInnerIOType() {
        return innerIOType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInnerIOType(String newInnerIOType) {
        String oldInnerIOType = innerIOType;
        innerIOType = newInnerIOType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPBW_TABLE__INNER_IO_TYPE, oldInnerIOType,
                    innerIOType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAPBW_TABLE__MODEL_TYPE:
            return getModelType();
        case ConnectionPackage.SAPBW_TABLE__ACTIVE:
            return isActive();
        case ConnectionPackage.SAPBW_TABLE__SOURCE_SYSTEM_NAME:
            return getSourceSystemName();
        case ConnectionPackage.SAPBW_TABLE__INFO_AREA_NAME:
            return getInfoAreaName();
        case ConnectionPackage.SAPBW_TABLE__INNER_IO_TYPE:
            return getInnerIOType();
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
        case ConnectionPackage.SAPBW_TABLE__MODEL_TYPE:
            setModelType((String) newValue);
            return;
        case ConnectionPackage.SAPBW_TABLE__ACTIVE:
            setActive((Boolean) newValue);
            return;
        case ConnectionPackage.SAPBW_TABLE__SOURCE_SYSTEM_NAME:
            setSourceSystemName((String) newValue);
            return;
        case ConnectionPackage.SAPBW_TABLE__INFO_AREA_NAME:
            setInfoAreaName((String) newValue);
            return;
        case ConnectionPackage.SAPBW_TABLE__INNER_IO_TYPE:
            setInnerIOType((String) newValue);
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
        case ConnectionPackage.SAPBW_TABLE__MODEL_TYPE:
            setModelType(MODEL_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.SAPBW_TABLE__ACTIVE:
            setActive(ACTIVE_EDEFAULT);
            return;
        case ConnectionPackage.SAPBW_TABLE__SOURCE_SYSTEM_NAME:
            setSourceSystemName(SOURCE_SYSTEM_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAPBW_TABLE__INFO_AREA_NAME:
            setInfoAreaName(INFO_AREA_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAPBW_TABLE__INNER_IO_TYPE:
            setInnerIOType(INNER_IO_TYPE_EDEFAULT);
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
        case ConnectionPackage.SAPBW_TABLE__MODEL_TYPE:
            return MODEL_TYPE_EDEFAULT == null ? modelType != null : !MODEL_TYPE_EDEFAULT.equals(modelType);
        case ConnectionPackage.SAPBW_TABLE__ACTIVE:
            return active != ACTIVE_EDEFAULT;
        case ConnectionPackage.SAPBW_TABLE__SOURCE_SYSTEM_NAME:
            return SOURCE_SYSTEM_NAME_EDEFAULT == null ? sourceSystemName != null : !SOURCE_SYSTEM_NAME_EDEFAULT
                    .equals(sourceSystemName);
        case ConnectionPackage.SAPBW_TABLE__INFO_AREA_NAME:
            return INFO_AREA_NAME_EDEFAULT == null ? infoAreaName != null : !INFO_AREA_NAME_EDEFAULT.equals(infoAreaName);
        case ConnectionPackage.SAPBW_TABLE__INNER_IO_TYPE:
            return INNER_IO_TYPE_EDEFAULT == null ? innerIOType != null : !INNER_IO_TYPE_EDEFAULT.equals(innerIOType);
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
        result.append(" (modelType: ");
        result.append(modelType);
        result.append(", active: ");
        result.append(active);
        result.append(", sourceSystemName: ");
        result.append(sourceSystemName);
        result.append(", infoAreaName: ");
        result.append(infoAreaName);
        result.append(", innerIOType: ");
        result.append(innerIOType);
        result.append(')');
        return result.toString();
    }

} //SAPBWTableImpl
