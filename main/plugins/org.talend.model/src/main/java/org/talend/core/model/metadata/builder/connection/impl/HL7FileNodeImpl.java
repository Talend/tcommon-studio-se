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

import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.HL7FileNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>HL7 File Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#getFilePath <em>File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#getOrder <em>Order</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#getRelatedColumn <em>Related Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7FileNodeImpl#isRepeatable <em>Repeatable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HL7FileNodeImpl extends EObjectImpl implements HL7FileNode {

    /**
     * The default value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected static final String FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilePath() <em>File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilePath()
     * @generated
     * @ordered
     */
    protected String filePath = FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getOrder() <em>Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrder()
     * @generated
     * @ordered
     */
    protected static final int ORDER_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getOrder() <em>Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrder()
     * @generated
     * @ordered
     */
    protected int order = ORDER_EDEFAULT;

    /**
     * The default value of the '{@link #getAttribute() <em>Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttribute()
     * @generated
     * @ordered
     */
    protected static final String ATTRIBUTE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAttribute() <em>Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttribute()
     * @generated
     * @ordered
     */
    protected String attribute = ATTRIBUTE_EDEFAULT;

    /**
     * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getRelatedColumn() <em>Related Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelatedColumn()
     * @generated
     * @ordered
     */
    protected static final String RELATED_COLUMN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRelatedColumn() <em>Related Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelatedColumn()
     * @generated
     * @ordered
     */
    protected String relatedColumn = RELATED_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #isRepeatable() <em>Repeatable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRepeatable()
     * @generated
     * @ordered
     */
    protected static final boolean REPEATABLE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isRepeatable() <em>Repeatable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRepeatable()
     * @generated
     * @ordered
     */
    protected boolean repeatable = REPEATABLE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HL7FileNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.HL7_FILE_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilePath(String newFilePath) {
        String oldFilePath = filePath;
        filePath = newFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__FILE_PATH, oldFilePath,
                    filePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getOrder() {
        return order;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrder(int newOrder) {
        int oldOrder = order;
        order = newOrder;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__ORDER, oldOrder, order));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAttribute(String newAttribute) {
        String oldAttribute = attribute;
        attribute = newAttribute;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__ATTRIBUTE, oldAttribute,
                    attribute));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(String newDefaultValue) {
        String oldDefaultValue = defaultValue;
        defaultValue = newDefaultValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__DEFAULT_VALUE,
                    oldDefaultValue, defaultValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRelatedColumn() {
        return relatedColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRelatedColumn(String newRelatedColumn) {
        String oldRelatedColumn = relatedColumn;
        relatedColumn = newRelatedColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__RELATED_COLUMN,
                    oldRelatedColumn, relatedColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isRepeatable() {
        return repeatable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRepeatable(boolean newRepeatable) {
        boolean oldRepeatable = repeatable;
        repeatable = newRepeatable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_FILE_NODE__REPEATABLE, oldRepeatable,
                    repeatable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.HL7_FILE_NODE__FILE_PATH:
            return getFilePath();
        case ConnectionPackage.HL7_FILE_NODE__ORDER:
            return getOrder();
        case ConnectionPackage.HL7_FILE_NODE__ATTRIBUTE:
            return getAttribute();
        case ConnectionPackage.HL7_FILE_NODE__DEFAULT_VALUE:
            return getDefaultValue();
        case ConnectionPackage.HL7_FILE_NODE__RELATED_COLUMN:
            return getRelatedColumn();
        case ConnectionPackage.HL7_FILE_NODE__REPEATABLE:
            return isRepeatable();
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
        case ConnectionPackage.HL7_FILE_NODE__FILE_PATH:
            setFilePath((String) newValue);
            return;
        case ConnectionPackage.HL7_FILE_NODE__ORDER:
            setOrder((Integer) newValue);
            return;
        case ConnectionPackage.HL7_FILE_NODE__ATTRIBUTE:
            setAttribute((String) newValue);
            return;
        case ConnectionPackage.HL7_FILE_NODE__DEFAULT_VALUE:
            setDefaultValue((String) newValue);
            return;
        case ConnectionPackage.HL7_FILE_NODE__RELATED_COLUMN:
            setRelatedColumn((String) newValue);
            return;
        case ConnectionPackage.HL7_FILE_NODE__REPEATABLE:
            setRepeatable((Boolean) newValue);
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
        case ConnectionPackage.HL7_FILE_NODE__FILE_PATH:
            setFilePath(FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.HL7_FILE_NODE__ORDER:
            setOrder(ORDER_EDEFAULT);
            return;
        case ConnectionPackage.HL7_FILE_NODE__ATTRIBUTE:
            setAttribute(ATTRIBUTE_EDEFAULT);
            return;
        case ConnectionPackage.HL7_FILE_NODE__DEFAULT_VALUE:
            setDefaultValue(DEFAULT_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.HL7_FILE_NODE__RELATED_COLUMN:
            setRelatedColumn(RELATED_COLUMN_EDEFAULT);
            return;
        case ConnectionPackage.HL7_FILE_NODE__REPEATABLE:
            setRepeatable(REPEATABLE_EDEFAULT);
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
        case ConnectionPackage.HL7_FILE_NODE__FILE_PATH:
            return FILE_PATH_EDEFAULT == null ? filePath != null : !FILE_PATH_EDEFAULT.equals(filePath);
        case ConnectionPackage.HL7_FILE_NODE__ORDER:
            return order != ORDER_EDEFAULT;
        case ConnectionPackage.HL7_FILE_NODE__ATTRIBUTE:
            return ATTRIBUTE_EDEFAULT == null ? attribute != null : !ATTRIBUTE_EDEFAULT.equals(attribute);
        case ConnectionPackage.HL7_FILE_NODE__DEFAULT_VALUE:
            return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
        case ConnectionPackage.HL7_FILE_NODE__RELATED_COLUMN:
            return RELATED_COLUMN_EDEFAULT == null ? relatedColumn != null : !RELATED_COLUMN_EDEFAULT.equals(relatedColumn);
        case ConnectionPackage.HL7_FILE_NODE__REPEATABLE:
            return repeatable != REPEATABLE_EDEFAULT;
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
        result.append(" (FilePath: ");
        result.append(filePath);
        result.append(", Order: ");
        result.append(order);
        result.append(", Attribute: ");
        result.append(attribute);
        result.append(", DefaultValue: ");
        result.append(defaultValue);
        result.append(", RelatedColumn: ");
        result.append(relatedColumn);
        result.append(", Repeatable: ");
        result.append(repeatable);
        result.append(')');
        return result.toString();
    }

} //HL7FileNodeImpl
