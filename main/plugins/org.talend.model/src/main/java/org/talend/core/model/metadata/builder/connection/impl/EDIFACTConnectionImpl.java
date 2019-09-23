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
import org.talend.core.model.metadata.builder.connection.EDIFACTConnection;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EDIFACT Connection</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.EDIFACTConnectionImpl#getXmlName <em>Xml Name</em>}
 * </li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.EDIFACTConnectionImpl#getFileName <em>File Name
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.EDIFACTConnectionImpl#getXmlPath <em>Xml Path</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class EDIFACTConnectionImpl extends ConnectionImpl implements EDIFACTConnection {

    /**
     * The default value of the '{@link #getXmlName() <em>Xml Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlName()
     * @generated
     * @ordered
     */
    protected static final String XML_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmlName() <em>Xml Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlName()
     * @generated
     * @ordered
     */
    protected String xmlName = XML_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getFileName() <em>File Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getFileName()
     * @generated
     * @ordered
     */
    protected static final String FILE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileName() <em>File Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getFileName()
     * @generated
     * @ordered
     */
    protected String fileName = FILE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getXmlPath() <em>Xml Path</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlPath()
     * @generated
     * @ordered
     */
    protected static final String XML_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmlPath() <em>Xml Path</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlPath()
     * @generated
     * @ordered
     */
    protected String xmlPath = XML_PATH_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EDIFACTConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.EDIFACT_CONNECTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getXmlName() {
        return xmlName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setXmlName(String newXmlName) {
        String oldXmlName = xmlName;
        xmlName = newXmlName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EDIFACT_CONNECTION__XML_NAME, oldXmlName,
                    xmlName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFileName(String newFileName) {
        String oldFileName = fileName;
        fileName = newFileName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EDIFACT_CONNECTION__FILE_NAME, oldFileName,
                    fileName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getXmlPath() {
        return xmlPath;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setXmlPath(String newXmlPath) {
        String oldXmlPath = xmlPath;
        xmlPath = newXmlPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EDIFACT_CONNECTION__XML_PATH, oldXmlPath,
                    xmlPath));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.EDIFACT_CONNECTION__XML_NAME:
            return getXmlName();
        case ConnectionPackage.EDIFACT_CONNECTION__FILE_NAME:
            return getFileName();
        case ConnectionPackage.EDIFACT_CONNECTION__XML_PATH:
            return getXmlPath();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.EDIFACT_CONNECTION__XML_NAME:
            setXmlName((String) newValue);
            return;
        case ConnectionPackage.EDIFACT_CONNECTION__FILE_NAME:
            setFileName((String) newValue);
            return;
        case ConnectionPackage.EDIFACT_CONNECTION__XML_PATH:
            setXmlPath((String) newValue);
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
        case ConnectionPackage.EDIFACT_CONNECTION__XML_NAME:
            setXmlName(XML_NAME_EDEFAULT);
            return;
        case ConnectionPackage.EDIFACT_CONNECTION__FILE_NAME:
            setFileName(FILE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.EDIFACT_CONNECTION__XML_PATH:
            setXmlPath(XML_PATH_EDEFAULT);
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
        case ConnectionPackage.EDIFACT_CONNECTION__XML_NAME:
            return XML_NAME_EDEFAULT == null ? xmlName != null : !XML_NAME_EDEFAULT.equals(xmlName);
        case ConnectionPackage.EDIFACT_CONNECTION__FILE_NAME:
            return FILE_NAME_EDEFAULT == null ? fileName != null : !FILE_NAME_EDEFAULT.equals(fileName);
        case ConnectionPackage.EDIFACT_CONNECTION__XML_PATH:
            return XML_PATH_EDEFAULT == null ? xmlPath != null : !XML_PATH_EDEFAULT.equals(xmlPath);
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
        result.append(" (XmlName: ");
        result.append(xmlName);
        result.append(", FileName: ");
        result.append(fileName);
        result.append(", XmlPath: ");
        result.append(xmlPath);
        result.append(')');
        return result.toString();
    }

} // EDIFACTConnectionImpl
