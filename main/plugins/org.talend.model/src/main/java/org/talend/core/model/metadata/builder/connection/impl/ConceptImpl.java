/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;

import org.talend.cwm.relational.impl.TdTableImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Concept</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getLoopExpression <em>Loop Expression</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getLoopLimit <em>Loop Limit</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getConceptTargets <em>Concept Targets</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#isInputModel <em>Input Model</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getConceptType <em>Concept Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ConceptImpl#getXPathPrefix <em>XPath Prefix</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConceptImpl extends TdTableImpl implements Concept {

    /**
     * The default value of the '{@link #getLoopExpression() <em>Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoopExpression()
     * @generated
     * @ordered
     */
    protected static final String LOOP_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLoopExpression() <em>Loop Expression</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoopExpression()
     * @generated
     * @ordered
     */
    protected String loopExpression = LOOP_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getLoopLimit() <em>Loop Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoopLimit()
     * @generated
     * @ordered
     */
    protected static final Integer LOOP_LIMIT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLoopLimit() <em>Loop Limit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoopLimit()
     * @generated
     * @ordered
     */
    protected Integer loopLimit = LOOP_LIMIT_EDEFAULT;

    /**
     * The cached value of the '{@link #getConceptTargets() <em>Concept Targets</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptTargets()
     * @generated
     * @ordered
     */
    protected EList<ConceptTarget> conceptTargets;

    /**
     * The default value of the '{@link #isInputModel() <em>Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isInputModel()
     * @generated
     * @ordered
     */
    protected static final boolean INPUT_MODEL_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isInputModel() <em>Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isInputModel()
     * @generated
     * @ordered
     */
    protected boolean inputModel = INPUT_MODEL_EDEFAULT;

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
     * The cached value of the '{@link #getRoot() <em>Root</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoot()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> root;

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
     * The default value of the '{@link #getConceptType() <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptType()
     * @generated
     * @ordered
     */
    protected static final MdmConceptType CONCEPT_TYPE_EDEFAULT = MdmConceptType.INPUT;

    /**
     * The cached value of the '{@link #getConceptType() <em>Concept Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptType()
     * @generated
     * @ordered
     */
    protected MdmConceptType conceptType = CONCEPT_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getXPathPrefix() <em>XPath Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXPathPrefix()
     * @generated
     * @ordered
     */
    protected static final String XPATH_PREFIX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXPathPrefix() <em>XPath Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXPathPrefix()
     * @generated
     * @ordered
     */
    protected String xPathPrefix = XPATH_PREFIX_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConceptImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.CONCEPT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLoopExpression() {
        return loopExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLoopExpression(String newLoopExpression) {
        String oldLoopExpression = loopExpression;
        loopExpression = newLoopExpression;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT__LOOP_EXPRESSION, oldLoopExpression,
                    loopExpression));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Integer getLoopLimit() {
        return loopLimit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLoopLimit(Integer newLoopLimit) {
        Integer oldLoopLimit = loopLimit;
        loopLimit = newLoopLimit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT__LOOP_LIMIT, oldLoopLimit, loopLimit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ConceptTarget> getConceptTargets() {
        if (conceptTargets == null) {
            conceptTargets = new EObjectContainmentWithInverseEList.Resolving<ConceptTarget>(ConceptTarget.class, this,
                    ConnectionPackage.CONCEPT__CONCEPT_TARGETS, ConnectionPackage.CONCEPT_TARGET__SCHEMA);
        }
        return conceptTargets;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isInputModel() {
        return inputModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputModel(boolean newInputModel) {
        boolean oldInputModel = inputModel;
        inputModel = newInputModel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT__INPUT_MODEL, oldInputModel,
                    inputModel));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getGroup() {
        if (group == null) {
            group = new EObjectContainmentEList<XMLFileNode>(XMLFileNode.class, this, ConnectionPackage.CONCEPT__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getRoot() {
        if (root == null) {
            root = new EObjectContainmentEList.Resolving<XMLFileNode>(XMLFileNode.class, this, ConnectionPackage.CONCEPT__ROOT);
        }
        return root;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getLoop() {
        if (loop == null) {
            loop = new EObjectContainmentEList<XMLFileNode>(XMLFileNode.class, this, ConnectionPackage.CONCEPT__LOOP);
        }
        return loop;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MdmConceptType getConceptType() {
        return conceptType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConceptType(MdmConceptType newConceptType) {
        MdmConceptType oldConceptType = conceptType;
        conceptType = newConceptType == null ? CONCEPT_TYPE_EDEFAULT : newConceptType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT__CONCEPT_TYPE, oldConceptType,
                    conceptType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXPathPrefix() {
        return xPathPrefix;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXPathPrefix(String newXPathPrefix) {
        String oldXPathPrefix = xPathPrefix;
        xPathPrefix = newXPathPrefix;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.CONCEPT__XPATH_PREFIX, oldXPathPrefix,
                    xPathPrefix));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getConceptTargets()).basicAdd(otherEnd, msgs);
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
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            return ((InternalEList<?>) getConceptTargets()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONCEPT__GROUP:
            return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONCEPT__ROOT:
            return ((InternalEList<?>) getRoot()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.CONCEPT__LOOP:
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
        case ConnectionPackage.CONCEPT__LOOP_EXPRESSION:
            return getLoopExpression();
        case ConnectionPackage.CONCEPT__LOOP_LIMIT:
            return getLoopLimit();
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            return getConceptTargets();
        case ConnectionPackage.CONCEPT__INPUT_MODEL:
            return isInputModel();
        case ConnectionPackage.CONCEPT__GROUP:
            return getGroup();
        case ConnectionPackage.CONCEPT__ROOT:
            return getRoot();
        case ConnectionPackage.CONCEPT__LOOP:
            return getLoop();
        case ConnectionPackage.CONCEPT__CONCEPT_TYPE:
            return getConceptType();
        case ConnectionPackage.CONCEPT__XPATH_PREFIX:
            return getXPathPrefix();
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
        case ConnectionPackage.CONCEPT__LOOP_EXPRESSION:
            setLoopExpression((String) newValue);
            return;
        case ConnectionPackage.CONCEPT__LOOP_LIMIT:
            setLoopLimit((Integer) newValue);
            return;
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            getConceptTargets().clear();
            getConceptTargets().addAll((Collection<? extends ConceptTarget>) newValue);
            return;
        case ConnectionPackage.CONCEPT__INPUT_MODEL:
            setInputModel((Boolean) newValue);
            return;
        case ConnectionPackage.CONCEPT__GROUP:
            getGroup().clear();
            getGroup().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.CONCEPT__ROOT:
            getRoot().clear();
            getRoot().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.CONCEPT__LOOP:
            getLoop().clear();
            getLoop().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.CONCEPT__CONCEPT_TYPE:
            setConceptType((MdmConceptType) newValue);
            return;
        case ConnectionPackage.CONCEPT__XPATH_PREFIX:
            setXPathPrefix((String) newValue);
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
        case ConnectionPackage.CONCEPT__LOOP_EXPRESSION:
            setLoopExpression(LOOP_EXPRESSION_EDEFAULT);
            return;
        case ConnectionPackage.CONCEPT__LOOP_LIMIT:
            setLoopLimit(LOOP_LIMIT_EDEFAULT);
            return;
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            getConceptTargets().clear();
            return;
        case ConnectionPackage.CONCEPT__INPUT_MODEL:
            setInputModel(INPUT_MODEL_EDEFAULT);
            return;
        case ConnectionPackage.CONCEPT__GROUP:
            getGroup().clear();
            return;
        case ConnectionPackage.CONCEPT__ROOT:
            getRoot().clear();
            return;
        case ConnectionPackage.CONCEPT__LOOP:
            getLoop().clear();
            return;
        case ConnectionPackage.CONCEPT__CONCEPT_TYPE:
            setConceptType(CONCEPT_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.CONCEPT__XPATH_PREFIX:
            setXPathPrefix(XPATH_PREFIX_EDEFAULT);
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
        case ConnectionPackage.CONCEPT__LOOP_EXPRESSION:
            return LOOP_EXPRESSION_EDEFAULT == null ? loopExpression != null : !LOOP_EXPRESSION_EDEFAULT.equals(loopExpression);
        case ConnectionPackage.CONCEPT__LOOP_LIMIT:
            return LOOP_LIMIT_EDEFAULT == null ? loopLimit != null : !LOOP_LIMIT_EDEFAULT.equals(loopLimit);
        case ConnectionPackage.CONCEPT__CONCEPT_TARGETS:
            return conceptTargets != null && !conceptTargets.isEmpty();
        case ConnectionPackage.CONCEPT__INPUT_MODEL:
            return inputModel != INPUT_MODEL_EDEFAULT;
        case ConnectionPackage.CONCEPT__GROUP:
            return group != null && !group.isEmpty();
        case ConnectionPackage.CONCEPT__ROOT:
            return root != null && !root.isEmpty();
        case ConnectionPackage.CONCEPT__LOOP:
            return loop != null && !loop.isEmpty();
        case ConnectionPackage.CONCEPT__CONCEPT_TYPE:
            return conceptType != CONCEPT_TYPE_EDEFAULT;
        case ConnectionPackage.CONCEPT__XPATH_PREFIX:
            return XPATH_PREFIX_EDEFAULT == null ? xPathPrefix != null : !XPATH_PREFIX_EDEFAULT.equals(xPathPrefix);
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
        result.append(" (LoopExpression: ");
        result.append(loopExpression);
        result.append(", LoopLimit: ");
        result.append(loopLimit);
        result.append(", inputModel: ");
        result.append(inputModel);
        result.append(", conceptType: ");
        result.append(conceptType);
        result.append(", xPathPrefix: ");
        result.append(xPathPrefix);
        result.append(')');
        return result.toString();
    }

} //ConceptImpl
