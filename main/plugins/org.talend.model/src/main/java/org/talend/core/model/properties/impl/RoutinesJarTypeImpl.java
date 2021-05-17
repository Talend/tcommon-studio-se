/**
 */
package org.talend.core.model.properties.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.RoutinesJarType;

import org.talend.designer.core.model.utils.emf.component.IMPORTType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Routines Jar Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.properties.impl.RoutinesJarTypeImpl#getMvnUrl <em>Mvn Url</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.RoutinesJarTypeImpl#getImports <em>Imports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RoutinesJarTypeImpl extends EObjectImpl implements RoutinesJarType {
    /**
     * The default value of the '{@link #getMvnUrl() <em>Mvn Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMvnUrl()
     * @generated
     * @ordered
     */
    protected static final String MVN_URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMvnUrl() <em>Mvn Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMvnUrl()
     * @generated
     * @ordered
     */
    protected String mvnUrl = MVN_URL_EDEFAULT;

    /**
     * The cached value of the '{@link #getImports() <em>Imports</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImports()
     * @generated
     * @ordered
     */
    protected EList imports;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RoutinesJarTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.ROUTINES_JAR_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMvnUrl() {
        return mvnUrl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMvnUrl(String newMvnUrl) {
        String oldMvnUrl = mvnUrl;
        mvnUrl = newMvnUrl;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ROUTINES_JAR_TYPE__MVN_URL, oldMvnUrl, mvnUrl));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getImports() {
        if (imports == null) {
            imports = new EObjectContainmentEList(IMPORTType.class, this, PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS);
        }
        return imports;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS:
                return ((InternalEList)getImports()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_TYPE__MVN_URL:
                return getMvnUrl();
            case PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS:
                return getImports();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_TYPE__MVN_URL:
                setMvnUrl((String)newValue);
                return;
            case PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS:
                getImports().clear();
                getImports().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_TYPE__MVN_URL:
                setMvnUrl(MVN_URL_EDEFAULT);
                return;
            case PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS:
                getImports().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PropertiesPackage.ROUTINES_JAR_TYPE__MVN_URL:
                return MVN_URL_EDEFAULT == null ? mvnUrl != null : !MVN_URL_EDEFAULT.equals(mvnUrl);
            case PropertiesPackage.ROUTINES_JAR_TYPE__IMPORTS:
                return imports != null && !imports.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (mvnUrl: ");
        result.append(mvnUrl);
        result.append(')');
        return result.toString();
    }

} //RoutinesJarTypeImpl
