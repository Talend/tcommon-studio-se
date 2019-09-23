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
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable;
import org.talend.cwm.helper.ModelElementHelper;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>SAP Function Parameter Column</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getParameterType <em>Parameter Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getStructureOrTableName <em>Structure Or Table Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionParameterColumnImpl#getParameterTable <em>Parameter Table</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPFunctionParameterColumnImpl extends AbstractMetadataObjectImpl implements SAPFunctionParameterColumn {

    /**
     * The default value of the '{@link #getParameterType() <em>Parameter Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterType()
     * @generated
     * @ordered
     */
    protected static final String PARAMETER_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getParameterType() <em>Parameter Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterType()
     * @generated
     * @ordered
     */
    protected String parameterType = PARAMETER_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getStructureOrTableName() <em>Structure Or Table Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getStructureOrTableName()
     * @generated
     * @ordered
     */
    protected static final String STRUCTURE_OR_TABLE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStructureOrTableName() <em>Structure Or Table Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getStructureOrTableName()
     * @generated
     * @ordered
     */
    protected String structureOrTableName = STRUCTURE_OR_TABLE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getDataType() <em>Data Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDataType()
     * @generated
     * @ordered
     */
    protected static final String DATA_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDataType() <em>Data Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDataType()
     * @generated
     * @ordered
     */
    protected String dataType = DATA_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected static final String LENGTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected String length = LENGTH_EDEFAULT;

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SAPFunctionParameterColumnImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_FUNCTION_PARAMETER_COLUMN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getParameterType() {
        return parameterType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setParameterType(String newParameterType) {
        String oldParameterType = parameterType;
        parameterType = newParameterType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE, oldParameterType, parameterType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getStructureOrTableName() {
        return structureOrTableName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setStructureOrTableName(String newStructureOrTableName) {
        String oldStructureOrTableName = structureOrTableName;
        structureOrTableName = newStructureOrTableName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME, oldStructureOrTableName,
                    structureOrTableName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDataType(String newDataType) {
        String oldDataType = dataType;
        dataType = newDataType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE,
                    oldDataType, dataType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLength() {
        return length;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLength(String newLength) {
        String oldLength = length;
        length = newLength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__LENGTH,
                    oldLength, length));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__VALUE,
                    oldValue, value));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameterTable getParameterTable() {
        if (eContainerFeatureID() != ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE)
            return null;
        return (SAPFunctionParameterTable) eContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParameterTable basicGetParameterTable() {
        if (eContainerFeatureID() != ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE)
            return null;
        return (SAPFunctionParameterTable) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParameterTable(SAPFunctionParameterTable newParameterTable, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newParameterTable,
                ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setParameterTable(SAPFunctionParameterTable newParameterTable) {
        if (newParameterTable != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE && newParameterTable != null)) {
            if (EcoreUtil.isAncestor(this, newParameterTable))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newParameterTable != null)
                msgs = ((InternalEObject) newParameterTable).eInverseAdd(this,
                        ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS, SAPFunctionParameterTable.class, msgs);
            msgs = basicSetParameterTable(newParameterTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE, newParameterTable, newParameterTable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setDescription(String description) {
        ModelElementHelper.getFirstDescription(this).setBody(description);
        // String oldDescription = description;
        // description = newDescription;
        // if (eNotificationRequired())
        // eNotify(new ENotificationImpl(this, Notification.SET,
        // ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DESCRIPTION, oldDescription, description));

    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetParameterTable((SAPFunctionParameterTable) otherEnd, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            return basicSetParameterTable(null, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.SAP_FUNCTION_PARAMETER_TABLE__COLUMNS,
                    SAPFunctionParameterTable.class, msgs);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE:
            return getParameterType();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME:
            return getStructureOrTableName();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE:
            return getDataType();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__LENGTH:
            return getLength();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__VALUE:
            return getValue();
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            if (resolve)
                return getParameterTable();
            return basicGetParameterTable();
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE:
            setParameterType((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME:
            setStructureOrTableName((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE:
            setDataType((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__LENGTH:
            setLength((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__VALUE:
            setValue((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            setParameterTable((SAPFunctionParameterTable) newValue);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE:
            setParameterType(PARAMETER_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME:
            setStructureOrTableName(STRUCTURE_OR_TABLE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE:
            setDataType(DATA_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__LENGTH:
            setLength(LENGTH_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__VALUE:
            setValue(VALUE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            setParameterTable((SAPFunctionParameterTable) null);
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
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TYPE:
            return PARAMETER_TYPE_EDEFAULT == null ? parameterType != null : !PARAMETER_TYPE_EDEFAULT.equals(parameterType);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__STRUCTURE_OR_TABLE_NAME:
            return STRUCTURE_OR_TABLE_NAME_EDEFAULT == null ? structureOrTableName != null : !STRUCTURE_OR_TABLE_NAME_EDEFAULT
                    .equals(structureOrTableName);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__DATA_TYPE:
            return DATA_TYPE_EDEFAULT == null ? dataType != null : !DATA_TYPE_EDEFAULT.equals(dataType);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__LENGTH:
            return LENGTH_EDEFAULT == null ? length != null : !LENGTH_EDEFAULT.equals(length);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__VALUE:
            return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
        case ConnectionPackage.SAP_FUNCTION_PARAMETER_COLUMN__PARAMETER_TABLE:
            return basicGetParameterTable() != null;
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
        result.append(" (ParameterType: ");
        result.append(parameterType);
        result.append(", StructureOrTableName: ");
        result.append(structureOrTableName);
        result.append(", DataType: ");
        result.append(dataType);
        result.append(", Length: ");
        result.append(length);
        result.append(", Value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        SAPFunctionParameterTable table = this.getParameterTable();
        return table == null ? false : table.isReadOnly();
    }

} // SAPFunctionParameterColumnImpl
