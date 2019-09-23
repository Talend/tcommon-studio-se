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
import org.talend.core.model.metadata.builder.connection.EDIFACTColumn;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EDIFACT Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EDIFACTColumnImpl#getEDIColumnName <em>EDI Column Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EDIFACTColumnImpl#getEDIXpath <em>EDI Xpath</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EDIFACTColumnImpl extends MetadataColumnImpl implements EDIFACTColumn {

    /**
     * The default value of the '{@link #getEDIColumnName() <em>EDI Column Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEDIColumnName()
     * @generated
     * @ordered
     */
    protected static final String EDI_COLUMN_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEDIColumnName() <em>EDI Column Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEDIColumnName()
     * @generated
     * @ordered
     */
    protected String ediColumnName = EDI_COLUMN_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getEDIXpath() <em>EDI Xpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEDIXpath()
     * @generated
     * @ordered
     */
    protected static final String EDI_XPATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEDIXpath() <em>EDI Xpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEDIXpath()
     * @generated
     * @ordered
     */
    protected String ediXpath = EDI_XPATH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EDIFACTColumnImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.EDIFACT_COLUMN;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEDIColumnName() {
        return ediColumnName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEDIColumnName(String newEDIColumnName) {
        String oldEDIColumnName = ediColumnName;
        ediColumnName = newEDIColumnName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EDIFACT_COLUMN__EDI_COLUMN_NAME,
                    oldEDIColumnName, ediColumnName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEDIXpath() {
        return ediXpath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEDIXpath(String newEDIXpath) {
        String oldEDIXpath = ediXpath;
        ediXpath = newEDIXpath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EDIFACT_COLUMN__EDI_XPATH, oldEDIXpath,
                    ediXpath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.EDIFACT_COLUMN__EDI_COLUMN_NAME:
            return getEDIColumnName();
        case ConnectionPackage.EDIFACT_COLUMN__EDI_XPATH:
            return getEDIXpath();
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
        case ConnectionPackage.EDIFACT_COLUMN__EDI_COLUMN_NAME:
            setEDIColumnName((String) newValue);
            return;
        case ConnectionPackage.EDIFACT_COLUMN__EDI_XPATH:
            setEDIXpath((String) newValue);
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
        case ConnectionPackage.EDIFACT_COLUMN__EDI_COLUMN_NAME:
            setEDIColumnName(EDI_COLUMN_NAME_EDEFAULT);
            return;
        case ConnectionPackage.EDIFACT_COLUMN__EDI_XPATH:
            setEDIXpath(EDI_XPATH_EDEFAULT);
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
        case ConnectionPackage.EDIFACT_COLUMN__EDI_COLUMN_NAME:
            return EDI_COLUMN_NAME_EDEFAULT == null ? ediColumnName != null : !EDI_COLUMN_NAME_EDEFAULT.equals(ediColumnName);
        case ConnectionPackage.EDIFACT_COLUMN__EDI_XPATH:
            return EDI_XPATH_EDEFAULT == null ? ediXpath != null : !EDI_XPATH_EDEFAULT.equals(ediXpath);
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
        result.append(" (EDIColumnName: ");
        result.append(ediColumnName);
        result.append(", EDIXpath: ");
        result.append(ediXpath);
        result.append(')');
        return result.toString();
    }

} //EDIFACTColumnImpl
