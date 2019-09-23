/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.talend.core.model.metadata.builder.connection.ConditionType;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.LogicalOperator;
import org.talend.core.model.metadata.builder.connection.RuleType;
import org.talend.core.model.metadata.builder.connection.ValidationRulesConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Validation Rules Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsSelect <em>Is Select</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsInsert <em>Is Insert</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsUpdate <em>Is Update</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsDelete <em>Is Delete</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getBaseSchema <em>Base Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getBaseColumnNames <em>Base Column Names</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getRefSchema <em>Ref Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getRefColumnNames <em>Ref Column Names</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getJavaCondition <em>Java Condition</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getSqlCondition <em>Sql Condition</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getLogicalOperator <em>Logical Operator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getConditions <em>Conditions</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#getInnerJoins <em>Inner Joins</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsDisallow <em>Is Disallow</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.ValidationRulesConnectionImpl#isIsRejectLink <em>Is Reject Link</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValidationRulesConnectionImpl extends ConnectionImpl implements ValidationRulesConnection {

    /**
     * The default value of the '{@link #isIsSelect() <em>Is Select</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSelect()
     * @generated
     * @ordered
     */
    protected static final boolean IS_SELECT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsSelect() <em>Is Select</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSelect()
     * @generated
     * @ordered
     */
    protected boolean isSelect = IS_SELECT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsInsert() <em>Is Insert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsInsert()
     * @generated
     * @ordered
     */
    protected static final boolean IS_INSERT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsInsert() <em>Is Insert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsInsert()
     * @generated
     * @ordered
     */
    protected boolean isInsert = IS_INSERT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsUpdate() <em>Is Update</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsUpdate()
     * @generated
     * @ordered
     */
    protected static final boolean IS_UPDATE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsUpdate() <em>Is Update</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsUpdate()
     * @generated
     * @ordered
     */
    protected boolean isUpdate = IS_UPDATE_EDEFAULT;

    /**
     * The default value of the '{@link #isIsDelete() <em>Is Delete</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDelete()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DELETE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDelete() <em>Is Delete</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDelete()
     * @generated
     * @ordered
     */
    protected boolean isDelete = IS_DELETE_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final RuleType TYPE_EDEFAULT = RuleType.REFERENCE;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected RuleType type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getBaseSchema() <em>Base Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseSchema()
     * @generated
     * @ordered
     */
    protected static final String BASE_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBaseSchema() <em>Base Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseSchema()
     * @generated
     * @ordered
     */
    protected String baseSchema = BASE_SCHEMA_EDEFAULT;

    /**
     * The cached value of the '{@link #getBaseColumnNames() <em>Base Column Names</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseColumnNames()
     * @generated
     * @ordered
     */
    protected EList<String> baseColumnNames;

    /**
     * The default value of the '{@link #getRefSchema() <em>Ref Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefSchema()
     * @generated
     * @ordered
     */
    protected static final String REF_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRefSchema() <em>Ref Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefSchema()
     * @generated
     * @ordered
     */
    protected String refSchema = REF_SCHEMA_EDEFAULT;

    /**
     * The cached value of the '{@link #getRefColumnNames() <em>Ref Column Names</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefColumnNames()
     * @generated
     * @ordered
     */
    protected EList<String> refColumnNames;

    /**
     * The default value of the '{@link #getJavaCondition() <em>Java Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaCondition()
     * @generated
     * @ordered
     */
    protected static final String JAVA_CONDITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJavaCondition() <em>Java Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaCondition()
     * @generated
     * @ordered
     */
    protected String javaCondition = JAVA_CONDITION_EDEFAULT;

    /**
     * The default value of the '{@link #getSqlCondition() <em>Sql Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSqlCondition()
     * @generated
     * @ordered
     */
    protected static final String SQL_CONDITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSqlCondition() <em>Sql Condition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSqlCondition()
     * @generated
     * @ordered
     */
    protected String sqlCondition = SQL_CONDITION_EDEFAULT;

    /**
     * The default value of the '{@link #getLogicalOperator() <em>Logical Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLogicalOperator()
     * @generated
     * @ordered
     */
    protected static final LogicalOperator LOGICAL_OPERATOR_EDEFAULT = LogicalOperator.AND;

    /**
     * The cached value of the '{@link #getLogicalOperator() <em>Logical Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLogicalOperator()
     * @generated
     * @ordered
     */
    protected LogicalOperator logicalOperator = LOGICAL_OPERATOR_EDEFAULT;

    /**
     * The cached value of the '{@link #getConditions() <em>Conditions</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConditions()
     * @generated
     * @ordered
     */
    protected EList<ConditionType> conditions;

    /**
     * The cached value of the '{@link #getInnerJoins() <em>Inner Joins</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInnerJoins()
     * @generated
     * @ordered
     */
    protected EMap<String, String> innerJoins;

    /**
     * The default value of the '{@link #isIsDisallow() <em>Is Disallow</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDisallow()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DISALLOW_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDisallow() <em>Is Disallow</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDisallow()
     * @generated
     * @ordered
     */
    protected boolean isDisallow = IS_DISALLOW_EDEFAULT;

    /**
     * The default value of the '{@link #isIsRejectLink() <em>Is Reject Link</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsRejectLink()
     * @generated
     * @ordered
     */
    protected static final boolean IS_REJECT_LINK_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsRejectLink() <em>Is Reject Link</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsRejectLink()
     * @generated
     * @ordered
     */
    protected boolean isRejectLink = IS_REJECT_LINK_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ValidationRulesConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.VALIDATION_RULES_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsSelect() {
        return isSelect;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsSelect(boolean newIsSelect) {
        boolean oldIsSelect = isSelect;
        isSelect = newIsSelect;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_SELECT,
                    oldIsSelect, isSelect));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsInsert() {
        return isInsert;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsInsert(boolean newIsInsert) {
        boolean oldIsInsert = isInsert;
        isInsert = newIsInsert;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_INSERT,
                    oldIsInsert, isInsert));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsUpdate() {
        return isUpdate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsUpdate(boolean newIsUpdate) {
        boolean oldIsUpdate = isUpdate;
        isUpdate = newIsUpdate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_UPDATE,
                    oldIsUpdate, isUpdate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsDelete() {
        return isDelete;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsDelete(boolean newIsDelete) {
        boolean oldIsDelete = isDelete;
        isDelete = newIsDelete;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DELETE,
                    oldIsDelete, isDelete));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RuleType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(RuleType newType) {
        RuleType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__TYPE, oldType,
                    type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBaseSchema() {
        return baseSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBaseSchema(String newBaseSchema) {
        String oldBaseSchema = baseSchema;
        baseSchema = newBaseSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_SCHEMA,
                    oldBaseSchema, baseSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getBaseColumnNames() {
        if (baseColumnNames == null) {
            baseColumnNames = new EDataTypeUniqueEList<String>(String.class, this,
                    ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES);
        }
        return baseColumnNames;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRefSchema() {
        return refSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRefSchema(String newRefSchema) {
        String oldRefSchema = refSchema;
        refSchema = newRefSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_SCHEMA,
                    oldRefSchema, refSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getRefColumnNames() {
        if (refColumnNames == null) {
            refColumnNames = new EDataTypeUniqueEList<String>(String.class, this,
                    ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES);
        }
        return refColumnNames;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJavaCondition() {
        return javaCondition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJavaCondition(String newJavaCondition) {
        String oldJavaCondition = javaCondition;
        javaCondition = newJavaCondition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__JAVA_CONDITION,
                    oldJavaCondition, javaCondition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSqlCondition() {
        return sqlCondition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSqlCondition(String newSqlCondition) {
        String oldSqlCondition = sqlCondition;
        sqlCondition = newSqlCondition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__SQL_CONDITION,
                    oldSqlCondition, sqlCondition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLogicalOperator(LogicalOperator newLogicalOperator) {
        LogicalOperator oldLogicalOperator = logicalOperator;
        logicalOperator = newLogicalOperator == null ? LOGICAL_OPERATOR_EDEFAULT : newLogicalOperator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR, oldLogicalOperator, logicalOperator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ConditionType> getConditions() {
        if (conditions == null) {
            conditions = new EObjectContainmentEList.Resolving<ConditionType>(ConditionType.class, this,
                    ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS);
        }
        return conditions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EMap<String, String> getInnerJoins() {
        if (innerJoins == null) {
            innerJoins = new EcoreEMap<String, String>(ConnectionPackage.Literals.INNER_JOIN_MAP, InnerJoinMapImpl.class, this,
                    ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS);
        }
        return innerJoins;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsDisallow() {
        return isDisallow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsDisallow(boolean newIsDisallow) {
        boolean oldIsDisallow = isDisallow;
        isDisallow = newIsDisallow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DISALLOW,
                    oldIsDisallow, isDisallow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsRejectLink() {
        return isRejectLink;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsRejectLink(boolean newIsRejectLink) {
        boolean oldIsRejectLink = isRejectLink;
        isRejectLink = newIsRejectLink;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_REJECT_LINK,
                    oldIsRejectLink, isRejectLink));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS:
            return ((InternalEList<?>) getConditions()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS:
            return ((InternalEList<?>) getInnerJoins()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_SELECT:
            return isIsSelect();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_INSERT:
            return isIsInsert();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_UPDATE:
            return isIsUpdate();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DELETE:
            return isIsDelete();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__TYPE:
            return getType();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_SCHEMA:
            return getBaseSchema();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES:
            return getBaseColumnNames();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_SCHEMA:
            return getRefSchema();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES:
            return getRefColumnNames();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__JAVA_CONDITION:
            return getJavaCondition();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__SQL_CONDITION:
            return getSqlCondition();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR:
            return getLogicalOperator();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS:
            return getConditions();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS:
            if (coreType)
                return getInnerJoins();
            else
                return getInnerJoins().map();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DISALLOW:
            return isIsDisallow();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_REJECT_LINK:
            return isIsRejectLink();
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
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_SELECT:
            setIsSelect((Boolean) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_INSERT:
            setIsInsert((Boolean) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_UPDATE:
            setIsUpdate((Boolean) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DELETE:
            setIsDelete((Boolean) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__TYPE:
            setType((RuleType) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_SCHEMA:
            setBaseSchema((String) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES:
            getBaseColumnNames().clear();
            getBaseColumnNames().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_SCHEMA:
            setRefSchema((String) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES:
            getRefColumnNames().clear();
            getRefColumnNames().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__JAVA_CONDITION:
            setJavaCondition((String) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__SQL_CONDITION:
            setSqlCondition((String) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR:
            setLogicalOperator((LogicalOperator) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS:
            getConditions().clear();
            getConditions().addAll((Collection<? extends ConditionType>) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS:
            ((EStructuralFeature.Setting) getInnerJoins()).set(newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DISALLOW:
            setIsDisallow((Boolean) newValue);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_REJECT_LINK:
            setIsRejectLink((Boolean) newValue);
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
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_SELECT:
            setIsSelect(IS_SELECT_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_INSERT:
            setIsInsert(IS_INSERT_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_UPDATE:
            setIsUpdate(IS_UPDATE_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DELETE:
            setIsDelete(IS_DELETE_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__TYPE:
            setType(TYPE_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_SCHEMA:
            setBaseSchema(BASE_SCHEMA_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES:
            getBaseColumnNames().clear();
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_SCHEMA:
            setRefSchema(REF_SCHEMA_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES:
            getRefColumnNames().clear();
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__JAVA_CONDITION:
            setJavaCondition(JAVA_CONDITION_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__SQL_CONDITION:
            setSqlCondition(SQL_CONDITION_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR:
            setLogicalOperator(LOGICAL_OPERATOR_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS:
            getConditions().clear();
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS:
            getInnerJoins().clear();
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DISALLOW:
            setIsDisallow(IS_DISALLOW_EDEFAULT);
            return;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_REJECT_LINK:
            setIsRejectLink(IS_REJECT_LINK_EDEFAULT);
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
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_SELECT:
            return isSelect != IS_SELECT_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_INSERT:
            return isInsert != IS_INSERT_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_UPDATE:
            return isUpdate != IS_UPDATE_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DELETE:
            return isDelete != IS_DELETE_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__TYPE:
            return type != TYPE_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_SCHEMA:
            return BASE_SCHEMA_EDEFAULT == null ? baseSchema != null : !BASE_SCHEMA_EDEFAULT.equals(baseSchema);
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__BASE_COLUMN_NAMES:
            return baseColumnNames != null && !baseColumnNames.isEmpty();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_SCHEMA:
            return REF_SCHEMA_EDEFAULT == null ? refSchema != null : !REF_SCHEMA_EDEFAULT.equals(refSchema);
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__REF_COLUMN_NAMES:
            return refColumnNames != null && !refColumnNames.isEmpty();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__JAVA_CONDITION:
            return JAVA_CONDITION_EDEFAULT == null ? javaCondition != null : !JAVA_CONDITION_EDEFAULT.equals(javaCondition);
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__SQL_CONDITION:
            return SQL_CONDITION_EDEFAULT == null ? sqlCondition != null : !SQL_CONDITION_EDEFAULT.equals(sqlCondition);
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__LOGICAL_OPERATOR:
            return logicalOperator != LOGICAL_OPERATOR_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__CONDITIONS:
            return conditions != null && !conditions.isEmpty();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__INNER_JOINS:
            return innerJoins != null && !innerJoins.isEmpty();
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_DISALLOW:
            return isDisallow != IS_DISALLOW_EDEFAULT;
        case ConnectionPackage.VALIDATION_RULES_CONNECTION__IS_REJECT_LINK:
            return isRejectLink != IS_REJECT_LINK_EDEFAULT;
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
        result.append(" (isSelect: ");
        result.append(isSelect);
        result.append(", isInsert: ");
        result.append(isInsert);
        result.append(", isUpdate: ");
        result.append(isUpdate);
        result.append(", isDelete: ");
        result.append(isDelete);
        result.append(", type: ");
        result.append(type);
        result.append(", baseSchema: ");
        result.append(baseSchema);
        result.append(", baseColumnNames: ");
        result.append(baseColumnNames);
        result.append(", refSchema: ");
        result.append(refSchema);
        result.append(", refColumnNames: ");
        result.append(refColumnNames);
        result.append(", javaCondition: ");
        result.append(javaCondition);
        result.append(", sqlCondition: ");
        result.append(sqlCondition);
        result.append(", logicalOperator: ");
        result.append(logicalOperator);
        result.append(", isDisallow: ");
        result.append(isDisallow);
        result.append(", isRejectLink: ");
        result.append(isRejectLink);
        result.append(')');
        return result.toString();
    }

} //ValidationRulesConnectionImpl
