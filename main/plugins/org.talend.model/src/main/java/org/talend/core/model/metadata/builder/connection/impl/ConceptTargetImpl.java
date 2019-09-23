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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Concept Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptTargetImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptTargetImpl#getTargetName <em>Target Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptTargetImpl#getRelativeLoopExpression <em>Relative Loop Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConceptTargetImpl extends EObjectImpl implements ConceptTarget {

    /**
     * The default value of the '{@link #getTargetName() <em>Target Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetName()
     * @generated
     * @ordered
     */
    protected static final String TARGET_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTargetName() <em>Target Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetName()
     * @generated
     * @ordered
     */
    protected String targetName = TARGET_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getRelativeLoopExpression() <em>Relative Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelativeLoopExpression()
     * @generated
     * @ordered
     */
    protected static final String RELATIVE_LOOP_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRelativeLoopExpression() <em>Relative Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelativeLoopExpression()
     * @generated
     * @ordered
     */
    protected String relativeLoopExpression = RELATIVE_LOOP_EXPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConceptTargetImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CONCEPT_TARGET;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Concept getSchema() {
        if (eContainerFeatureID() != ConnectionPackage.CONCEPT_TARGET__SCHEMA)
            return null;
        return (Concept) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Concept basicGetSchema() {
        if (eContainerFeatureID() != ConnectionPackage.CONCEPT_TARGET__SCHEMA)
            return null;
        return (Concept) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSchema(Concept newSchema, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newSchema, ConnectionPackage.CONCEPT_TARGET__SCHEMA, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSchema(Concept newSchema) {
        if (newSchema != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.CONCEPT_TARGET__SCHEMA && newSchema != null)) {
            if (EcoreUtil.isAncestor(this, newSchema))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newSchema != null)
                msgs = ((InternalEObject) newSchema).eInverseAdd(this, ConnectionPackage.CONCEPT__CONCEPT_TARGETS, Concept.class,
                        msgs);
            msgs = basicSetSchema(newSchema, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT_TARGET__SCHEMA, newSchema, newSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetName(String newTargetName) {
        String oldTargetName = targetName;
        targetName = newTargetName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT_TARGET__TARGET_NAME, oldTargetName,
                    targetName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRelativeLoopExpression() {
        return relativeLoopExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRelativeLoopExpression(String newRelativeLoopExpression) {
        String oldRelativeLoopExpression = relativeLoopExpression;
        relativeLoopExpression = newRelativeLoopExpression;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION,
                    oldRelativeLoopExpression, relativeLoopExpression));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetSchema((Concept) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            return basicSetSchema(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.CONCEPT__CONCEPT_TARGETS, Concept.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            if (resolve)
                return getSchema();
            return basicGetSchema();
        case ConnectionPackage.CONCEPT_TARGET__TARGET_NAME:
            return getTargetName();
        case ConnectionPackage.CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION:
            return getRelativeLoopExpression();
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
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            setSchema((Concept) newValue);
            return;
        case ConnectionPackage.CONCEPT_TARGET__TARGET_NAME:
            setTargetName((String) newValue);
            return;
        case ConnectionPackage.CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION:
            setRelativeLoopExpression((String) newValue);
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
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            setSchema((Concept) null);
            return;
        case ConnectionPackage.CONCEPT_TARGET__TARGET_NAME:
            setTargetName(TARGET_NAME_EDEFAULT);
            return;
        case ConnectionPackage.CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION:
            setRelativeLoopExpression(RELATIVE_LOOP_EXPRESSION_EDEFAULT);
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
        case ConnectionPackage.CONCEPT_TARGET__SCHEMA:
            return basicGetSchema() != null;
        case ConnectionPackage.CONCEPT_TARGET__TARGET_NAME:
            return TARGET_NAME_EDEFAULT == null ? targetName != null : !TARGET_NAME_EDEFAULT.equals(targetName);
        case ConnectionPackage.CONCEPT_TARGET__RELATIVE_LOOP_EXPRESSION:
            return RELATIVE_LOOP_EXPRESSION_EDEFAULT == null ? relativeLoopExpression != null
                    : !RELATIVE_LOOP_EXPRESSION_EDEFAULT.equals(relativeLoopExpression);
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
        result.append(" (targetName: ");
        result.append(targetName);
        result.append(", RelativeLoopExpression: ");
        result.append(relativeLoopExpression);
        result.append(')');
        return result.toString();
    }

} //ConceptTargetImpl
