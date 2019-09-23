/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;
import java.util.ArrayList;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>BRMS Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getXmlField <em>Xml Field</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getUrlName <em>Url Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getTacWebappName <em>Tac Webapp Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getClassName <em>Class Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getModuleUsed <em>Module Used</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.BRMSConnectionImpl#getPackage <em>Package</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BRMSConnectionImpl extends ConnectionImpl implements BRMSConnection {

    /**
     * The default value of the '{@link #getXmlField() <em>Xml Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlField()
     * @generated
     * @ordered
     */
    protected static final String XML_FIELD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmlField() <em>Xml Field</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlField()
     * @generated
     * @ordered
     */
    protected String xmlField = XML_FIELD_EDEFAULT;

    /**
     * The default value of the '{@link #getUrlName() <em>Url Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUrlName()
     * @generated
     * @ordered
     */
    protected static final String URL_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUrlName() <em>Url Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUrlName()
     * @generated
     * @ordered
     */
    protected String urlName = URL_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getTacWebappName() <em>Tac Webapp Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTacWebappName()
     * @generated
     * @ordered
     */
    protected static final String TAC_WEBAPP_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTacWebappName() <em>Tac Webapp Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTacWebappName()
     * @generated
     * @ordered
     */
    protected String tacWebappName = TAC_WEBAPP_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClassName()
     * @generated
     * @ordered
     */
    protected static final String CLASS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getClassName() <em>Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClassName()
     * @generated
     * @ordered
     */
    protected String className = CLASS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getModuleUsed() <em>Module Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModuleUsed()
     * @generated
     * @ordered
     */
    protected static final String MODULE_USED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getModuleUsed() <em>Module Used</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModuleUsed()
     * @generated
     * @ordered
     */
    protected String moduleUsed = MODULE_USED_EDEFAULT;

    /**
     * The cached value of the '{@link #getRoot() <em>Root</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoot()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> root;

    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> group;

    /**
     * The cached value of the '{@link #getLoop() <em>Loop</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoop()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> loop;

    /**
     * The default value of the '{@link #getPackage() <em>Package</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPackage()
     * @generated
     * @ordered
     */
    protected static final String PACKAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPackage() <em>Package</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPackage()
     * @generated
     * @ordered
     */
    protected String package_ = PACKAGE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BRMSConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.BRMS_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXmlField() {
        return xmlField;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXmlField(String newXmlField) {
        String oldXmlField = xmlField;
        xmlField = newXmlField;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__XML_FIELD, oldXmlField,
                    xmlField));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUrlName() {
        return urlName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUrlName(String newUrlName) {
        String oldUrlName = urlName;
        urlName = newUrlName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__URL_NAME, oldUrlName,
                    urlName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTacWebappName() {
        return tacWebappName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTacWebappName(String newTacWebappName) {
        String oldTacWebappName = tacWebappName;
        tacWebappName = newTacWebappName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__TAC_WEBAPP_NAME,
                    oldTacWebappName, tacWebappName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getClassName() {
        return className;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setClassName(String newClassName) {
        String oldClassName = className;
        className = newClassName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__CLASS_NAME, oldClassName,
                    className));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getModuleUsed() {
        return moduleUsed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setModuleUsed(String newModuleUsed) {
        String oldModuleUsed = moduleUsed;
        moduleUsed = newModuleUsed;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__MODULE_USED, oldModuleUsed,
                    moduleUsed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getRoot() {
        if (root == null) {
            root = new EObjectContainmentEList.Resolving<XMLFileNode>(XMLFileNode.class, this,
                    ConnectionPackage.BRMS_CONNECTION__ROOT);
        }
        return root;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getGroup() {
        if (group == null) {
            group = new EObjectContainmentEList.Resolving<XMLFileNode>(XMLFileNode.class, this,
                    ConnectionPackage.BRMS_CONNECTION__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getLoop() {
        if (loop == null) {
            loop = new EObjectContainmentEList.Resolving<XMLFileNode>(XMLFileNode.class, this,
                    ConnectionPackage.BRMS_CONNECTION__LOOP);
        }
        return loop;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPackage() {
        return package_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPackage(String newPackage) {
        String oldPackage = package_;
        package_ = newPackage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.BRMS_CONNECTION__PACKAGE, oldPackage,
                    package_));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.BRMS_CONNECTION__ROOT:
            return ((InternalEList<?>) getRoot()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.BRMS_CONNECTION__GROUP:
            return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.BRMS_CONNECTION__LOOP:
            return ((InternalEList<?>) getLoop()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.BRMS_CONNECTION__XML_FIELD:
            return getXmlField();
        case ConnectionPackage.BRMS_CONNECTION__URL_NAME:
            return getUrlName();
        case ConnectionPackage.BRMS_CONNECTION__TAC_WEBAPP_NAME:
            return getTacWebappName();
        case ConnectionPackage.BRMS_CONNECTION__CLASS_NAME:
            return getClassName();
        case ConnectionPackage.BRMS_CONNECTION__MODULE_USED:
            return getModuleUsed();
        case ConnectionPackage.BRMS_CONNECTION__ROOT:
            return getRoot();
        case ConnectionPackage.BRMS_CONNECTION__GROUP:
            return getGroup();
        case ConnectionPackage.BRMS_CONNECTION__LOOP:
            return getLoop();
        case ConnectionPackage.BRMS_CONNECTION__PACKAGE:
            return getPackage();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.BRMS_CONNECTION__XML_FIELD:
            setXmlField((String) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__URL_NAME:
            setUrlName((String) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__TAC_WEBAPP_NAME:
            setTacWebappName((String) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__CLASS_NAME:
            setClassName((String) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__MODULE_USED:
            setModuleUsed((String) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__ROOT:
            getRoot().clear();
            getRoot().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__GROUP:
            getGroup().clear();
            getGroup().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__LOOP:
            getLoop().clear();
            getLoop().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.BRMS_CONNECTION__PACKAGE:
            setPackage((String) newValue);
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
        case ConnectionPackage.BRMS_CONNECTION__XML_FIELD:
            setXmlField(XML_FIELD_EDEFAULT);
            return;
        case ConnectionPackage.BRMS_CONNECTION__URL_NAME:
            setUrlName(URL_NAME_EDEFAULT);
            return;
        case ConnectionPackage.BRMS_CONNECTION__TAC_WEBAPP_NAME:
            setTacWebappName(TAC_WEBAPP_NAME_EDEFAULT);
            return;
        case ConnectionPackage.BRMS_CONNECTION__CLASS_NAME:
            setClassName(CLASS_NAME_EDEFAULT);
            return;
        case ConnectionPackage.BRMS_CONNECTION__MODULE_USED:
            setModuleUsed(MODULE_USED_EDEFAULT);
            return;
        case ConnectionPackage.BRMS_CONNECTION__ROOT:
            getRoot().clear();
            return;
        case ConnectionPackage.BRMS_CONNECTION__GROUP:
            getGroup().clear();
            return;
        case ConnectionPackage.BRMS_CONNECTION__LOOP:
            getLoop().clear();
            return;
        case ConnectionPackage.BRMS_CONNECTION__PACKAGE:
            setPackage(PACKAGE_EDEFAULT);
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
        case ConnectionPackage.BRMS_CONNECTION__XML_FIELD:
            return XML_FIELD_EDEFAULT == null ? xmlField != null : !XML_FIELD_EDEFAULT.equals(xmlField);
        case ConnectionPackage.BRMS_CONNECTION__URL_NAME:
            return URL_NAME_EDEFAULT == null ? urlName != null : !URL_NAME_EDEFAULT.equals(urlName);
        case ConnectionPackage.BRMS_CONNECTION__TAC_WEBAPP_NAME:
            return TAC_WEBAPP_NAME_EDEFAULT == null ? tacWebappName != null : !TAC_WEBAPP_NAME_EDEFAULT.equals(tacWebappName);
        case ConnectionPackage.BRMS_CONNECTION__CLASS_NAME:
            return CLASS_NAME_EDEFAULT == null ? className != null : !CLASS_NAME_EDEFAULT.equals(className);
        case ConnectionPackage.BRMS_CONNECTION__MODULE_USED:
            return MODULE_USED_EDEFAULT == null ? moduleUsed != null : !MODULE_USED_EDEFAULT.equals(moduleUsed);
        case ConnectionPackage.BRMS_CONNECTION__ROOT:
            return root != null && !root.isEmpty();
        case ConnectionPackage.BRMS_CONNECTION__GROUP:
            return group != null && !group.isEmpty();
        case ConnectionPackage.BRMS_CONNECTION__LOOP:
            return loop != null && !loop.isEmpty();
        case ConnectionPackage.BRMS_CONNECTION__PACKAGE:
            return PACKAGE_EDEFAULT == null ? package_ != null : !PACKAGE_EDEFAULT.equals(package_);
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
        result.append(" (xmlField: ");
        result.append(xmlField);
        result.append(", urlName: ");
        result.append(urlName);
        result.append(", tacWebappName: ");
        result.append(tacWebappName);
        result.append(", className: ");
        result.append(className);
        result.append(", moduleUsed: ");
        result.append(moduleUsed);
        result.append(", package: ");
        result.append(package_);
        result.append(')');
        return result.toString();
    }

} //BRMSConnectionImpl
