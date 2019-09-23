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
import org.talend.core.model.metadata.builder.connection.XMLFileNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>XML File Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getXMLPath <em>XML Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getRelatedColumn <em>Related Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getOrder <em>Order</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XMLFileNodeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XMLFileNodeImpl extends EObjectImpl implements XMLFileNode {

    /**
     * The default value of the '{@link #getXMLPath() <em>XML Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLPath()
     * @generated
     * @ordered
     */
    protected static final String XML_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXMLPath() <em>XML Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLPath()
     * @generated
     * @ordered
     */
    protected String xmlPath = XML_PATH_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XMLFileNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.XML_FILE_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXMLPath() {
        return xmlPath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXMLPath(String newXMLPath) {
        String oldXMLPath = xmlPath;
        xmlPath = newXMLPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__XML_PATH, oldXMLPath, xmlPath));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__RELATED_COLUMN,
                    oldRelatedColumn, relatedColumn));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__DEFAULT_VALUE,
                    oldDefaultValue, defaultValue));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__ATTRIBUTE, oldAttribute,
                    attribute));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__ORDER, oldOrder, order));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_NODE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.XML_FILE_NODE__XML_PATH:
            return getXMLPath();
        case ConnectionPackage.XML_FILE_NODE__RELATED_COLUMN:
            return getRelatedColumn();
        case ConnectionPackage.XML_FILE_NODE__DEFAULT_VALUE:
            return getDefaultValue();
        case ConnectionPackage.XML_FILE_NODE__ATTRIBUTE:
            return getAttribute();
        case ConnectionPackage.XML_FILE_NODE__ORDER:
            return getOrder();
        case ConnectionPackage.XML_FILE_NODE__TYPE:
            return getType();
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
        case ConnectionPackage.XML_FILE_NODE__XML_PATH:
            setXMLPath((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_NODE__RELATED_COLUMN:
            setRelatedColumn((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_NODE__DEFAULT_VALUE:
            setDefaultValue((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_NODE__ATTRIBUTE:
            setAttribute((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_NODE__ORDER:
            setOrder((Integer) newValue);
            return;
        case ConnectionPackage.XML_FILE_NODE__TYPE:
            setType((String) newValue);
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
        case ConnectionPackage.XML_FILE_NODE__XML_PATH:
            setXMLPath(XML_PATH_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_NODE__RELATED_COLUMN:
            setRelatedColumn(RELATED_COLUMN_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_NODE__DEFAULT_VALUE:
            setDefaultValue(DEFAULT_VALUE_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_NODE__ATTRIBUTE:
            setAttribute(ATTRIBUTE_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_NODE__ORDER:
            setOrder(ORDER_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_NODE__TYPE:
            setType(TYPE_EDEFAULT);
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
        case ConnectionPackage.XML_FILE_NODE__XML_PATH:
            return XML_PATH_EDEFAULT == null ? xmlPath != null : !XML_PATH_EDEFAULT.equals(xmlPath);
        case ConnectionPackage.XML_FILE_NODE__RELATED_COLUMN:
            return RELATED_COLUMN_EDEFAULT == null ? relatedColumn != null : !RELATED_COLUMN_EDEFAULT.equals(relatedColumn);
        case ConnectionPackage.XML_FILE_NODE__DEFAULT_VALUE:
            return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
        case ConnectionPackage.XML_FILE_NODE__ATTRIBUTE:
            return ATTRIBUTE_EDEFAULT == null ? attribute != null : !ATTRIBUTE_EDEFAULT.equals(attribute);
        case ConnectionPackage.XML_FILE_NODE__ORDER:
            return order != ORDER_EDEFAULT;
        case ConnectionPackage.XML_FILE_NODE__TYPE:
            return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
        result.append(" (XMLPath: ");
        result.append(xmlPath);
        result.append(", RelatedColumn: ");
        result.append(relatedColumn);
        result.append(", DefaultValue: ");
        result.append(defaultValue);
        result.append(", Attribute: ");
        result.append(attribute);
        result.append(", Order: ");
        result.append(order);
        result.append(", Type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //XMLFileNodeImpl
