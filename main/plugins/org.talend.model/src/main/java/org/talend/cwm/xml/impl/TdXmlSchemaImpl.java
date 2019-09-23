/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.XmlPackage;

import orgomg.cwm.resource.xml.impl.SchemaImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Xml Schema</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlSchemaImpl#getXsdFilePath <em>Xsd File Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdXmlSchemaImpl extends SchemaImpl implements TdXmlSchema {

    /**
     * The default value of the '{@link #getXsdFilePath() <em>Xsd File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXsdFilePath()
     * @generated
     * @ordered
     */
    protected static final String XSD_FILE_PATH_EDEFAULT = "";

    /**
     * The cached value of the '{@link #getXsdFilePath() <em>Xsd File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXsdFilePath()
     * @generated
     * @ordered
     */
    protected String xsdFilePath = XSD_FILE_PATH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdXmlSchemaImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.TD_XML_SCHEMA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXsdFilePath() {
        return xsdFilePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXsdFilePath(String newXsdFilePath) {
        String oldXsdFilePath = xsdFilePath;
        xsdFilePath = newXsdFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.TD_XML_SCHEMA__XSD_FILE_PATH, oldXsdFilePath,
                    xsdFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case XmlPackage.TD_XML_SCHEMA__XSD_FILE_PATH:
            return getXsdFilePath();
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
        case XmlPackage.TD_XML_SCHEMA__XSD_FILE_PATH:
            setXsdFilePath((String) newValue);
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
        case XmlPackage.TD_XML_SCHEMA__XSD_FILE_PATH:
            setXsdFilePath(XSD_FILE_PATH_EDEFAULT);
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
        case XmlPackage.TD_XML_SCHEMA__XSD_FILE_PATH:
            return XSD_FILE_PATH_EDEFAULT == null ? xsdFilePath != null : !XSD_FILE_PATH_EDEFAULT.equals(xsdFilePath);
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
        result.append(" (xsdFilePath: ");
        result.append(xsdFilePath);
        result.append(')');
        return result.toString();
    }

} //TdXmlSchemaImpl
