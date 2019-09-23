/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl;

import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdView;

import orgomg.cwm.foundation.datatypes.QueryExpression;

import orgomg.cwm.resource.relational.Column;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.SQLStructuredType;
import orgomg.cwm.resource.relational.Trigger;
import orgomg.cwm.resource.relational.View;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#getUsingTrigger <em>Using Trigger</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#getOptionScopeColumn <em>Option Scope Column</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#isIsReadOnly <em>Is Read Only</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#isCheckOption <em>Check Option</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdViewImpl#getQueryExpression <em>Query Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdViewImpl extends MetadataTableImpl implements TdView {

    /**
     * The cached value of the '{@link #getUsingTrigger() <em>Using Trigger</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsingTrigger()
     * @generated
     * @ordered
     */
    protected EList<Trigger> usingTrigger;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected SQLStructuredType type;

    /**
     * The cached value of the '{@link #getOptionScopeColumn() <em>Option Scope Column</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOptionScopeColumn()
     * @generated
     * @ordered
     */
    protected EList<Column> optionScopeColumn;

    /**
     * The default value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected static final boolean IS_READ_ONLY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected boolean isReadOnly = IS_READ_ONLY_EDEFAULT;

    /**
     * The default value of the '{@link #isCheckOption() <em>Check Option</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCheckOption()
     * @generated
     * @ordered
     */
    protected static final boolean CHECK_OPTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isCheckOption() <em>Check Option</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCheckOption()
     * @generated
     * @ordered
     */
    protected boolean checkOption = CHECK_OPTION_EDEFAULT;

    /**
     * The cached value of the '{@link #getQueryExpression() <em>Query Expression</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryExpression()
     * @generated
     * @ordered
     */
    protected QueryExpression queryExpression;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdViewImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RelationalPackage.Literals.TD_VIEW;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Trigger> getUsingTrigger() {
        if (usingTrigger == null) {
            usingTrigger = new EObjectWithInverseResolvingEList.ManyInverse<Trigger>(Trigger.class, this,
                    RelationalPackage.TD_VIEW__USING_TRIGGER,
                    orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__USED_COLUMN_SET);
        }
        return usingTrigger;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SQLStructuredType getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (SQLStructuredType) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RelationalPackage.TD_VIEW__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SQLStructuredType basicGetType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetType(SQLStructuredType newType, NotificationChain msgs) {
        SQLStructuredType oldType = type;
        type = newType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_VIEW__TYPE,
                    oldType, newType);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(SQLStructuredType newType) {
        if (newType != type) {
            NotificationChain msgs = null;
            if (type != null)
                msgs = ((InternalEObject) type).eInverseRemove(this,
                        orgomg.cwm.resource.relational.RelationalPackage.SQL_STRUCTURED_TYPE__COLUMN_SET,
                        SQLStructuredType.class, msgs);
            if (newType != null)
                msgs = ((InternalEObject) newType).eInverseAdd(this,
                        orgomg.cwm.resource.relational.RelationalPackage.SQL_STRUCTURED_TYPE__COLUMN_SET,
                        SQLStructuredType.class, msgs);
            msgs = basicSetType(newType, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_VIEW__TYPE, newType, newType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Column> getOptionScopeColumn() {
        if (optionScopeColumn == null) {
            optionScopeColumn = new EObjectWithInverseResolvingEList<Column>(Column.class, this,
                    RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN,
                    orgomg.cwm.resource.relational.RelationalPackage.COLUMN__OPTION_SCOPE_COLUMN_SET);
        }
        return optionScopeColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsReadOnly() {
        return isReadOnly;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsReadOnly(boolean newIsReadOnly) {
        boolean oldIsReadOnly = isReadOnly;
        isReadOnly = newIsReadOnly;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_VIEW__IS_READ_ONLY, oldIsReadOnly,
                    isReadOnly));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isCheckOption() {
        return checkOption;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCheckOption(boolean newCheckOption) {
        boolean oldCheckOption = checkOption;
        checkOption = newCheckOption;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_VIEW__CHECK_OPTION, oldCheckOption,
                    checkOption));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryExpression getQueryExpression() {
        return queryExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQueryExpression(QueryExpression newQueryExpression, NotificationChain msgs) {
        QueryExpression oldQueryExpression = queryExpression;
        queryExpression = newQueryExpression;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    RelationalPackage.TD_VIEW__QUERY_EXPRESSION, oldQueryExpression, newQueryExpression);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQueryExpression(QueryExpression newQueryExpression) {
        if (newQueryExpression != queryExpression) {
            NotificationChain msgs = null;
            if (queryExpression != null)
                msgs = ((InternalEObject) queryExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - RelationalPackage.TD_VIEW__QUERY_EXPRESSION, null, msgs);
            if (newQueryExpression != null)
                msgs = ((InternalEObject) newQueryExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - RelationalPackage.TD_VIEW__QUERY_EXPRESSION, null, msgs);
            msgs = basicSetQueryExpression(newQueryExpression, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_VIEW__QUERY_EXPRESSION,
                    newQueryExpression, newQueryExpression));
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getUsingTrigger()).basicAdd(otherEnd, msgs);
        case RelationalPackage.TD_VIEW__TYPE:
            if (type != null)
                msgs = ((InternalEObject) type).eInverseRemove(this,
                        orgomg.cwm.resource.relational.RelationalPackage.SQL_STRUCTURED_TYPE__COLUMN_SET,
                        SQLStructuredType.class, msgs);
            return basicSetType((SQLStructuredType) otherEnd, msgs);
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getOptionScopeColumn()).basicAdd(otherEnd, msgs);
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            return ((InternalEList<?>) getUsingTrigger()).basicRemove(otherEnd, msgs);
        case RelationalPackage.TD_VIEW__TYPE:
            return basicSetType(null, msgs);
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            return ((InternalEList<?>) getOptionScopeColumn()).basicRemove(otherEnd, msgs);
        case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
            return basicSetQueryExpression(null, msgs);
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            return getUsingTrigger();
        case RelationalPackage.TD_VIEW__TYPE:
            if (resolve)
                return getType();
            return basicGetType();
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            return getOptionScopeColumn();
        case RelationalPackage.TD_VIEW__IS_READ_ONLY:
            return isIsReadOnly();
        case RelationalPackage.TD_VIEW__CHECK_OPTION:
            return isCheckOption();
        case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
            return getQueryExpression();
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            getUsingTrigger().clear();
            getUsingTrigger().addAll((Collection<? extends Trigger>) newValue);
            return;
        case RelationalPackage.TD_VIEW__TYPE:
            setType((SQLStructuredType) newValue);
            return;
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            getOptionScopeColumn().clear();
            getOptionScopeColumn().addAll((Collection<? extends Column>) newValue);
            return;
        case RelationalPackage.TD_VIEW__IS_READ_ONLY:
            setIsReadOnly((Boolean) newValue);
            return;
        case RelationalPackage.TD_VIEW__CHECK_OPTION:
            setCheckOption((Boolean) newValue);
            return;
        case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
            setQueryExpression((QueryExpression) newValue);
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            getUsingTrigger().clear();
            return;
        case RelationalPackage.TD_VIEW__TYPE:
            setType((SQLStructuredType) null);
            return;
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            getOptionScopeColumn().clear();
            return;
        case RelationalPackage.TD_VIEW__IS_READ_ONLY:
            setIsReadOnly(IS_READ_ONLY_EDEFAULT);
            return;
        case RelationalPackage.TD_VIEW__CHECK_OPTION:
            setCheckOption(CHECK_OPTION_EDEFAULT);
            return;
        case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
            setQueryExpression((QueryExpression) null);
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
        case RelationalPackage.TD_VIEW__USING_TRIGGER:
            return usingTrigger != null && !usingTrigger.isEmpty();
        case RelationalPackage.TD_VIEW__TYPE:
            return type != null;
        case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
            return optionScopeColumn != null && !optionScopeColumn.isEmpty();
        case RelationalPackage.TD_VIEW__IS_READ_ONLY:
            return isReadOnly != IS_READ_ONLY_EDEFAULT;
        case RelationalPackage.TD_VIEW__CHECK_OPTION:
            return checkOption != CHECK_OPTION_EDEFAULT;
        case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
            return queryExpression != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == ColumnSet.class) {
            switch (derivedFeatureID) {
            default:
                return -1;
            }
        }
        if (baseClass == NamedColumnSet.class) {
            switch (derivedFeatureID) {
            case RelationalPackage.TD_VIEW__USING_TRIGGER:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__USING_TRIGGER;
            case RelationalPackage.TD_VIEW__TYPE:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__TYPE;
            case RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__OPTION_SCOPE_COLUMN;
            default:
                return -1;
            }
        }
        if (baseClass == View.class) {
            switch (derivedFeatureID) {
            case RelationalPackage.TD_VIEW__IS_READ_ONLY:
                return orgomg.cwm.resource.relational.RelationalPackage.VIEW__IS_READ_ONLY;
            case RelationalPackage.TD_VIEW__CHECK_OPTION:
                return orgomg.cwm.resource.relational.RelationalPackage.VIEW__CHECK_OPTION;
            case RelationalPackage.TD_VIEW__QUERY_EXPRESSION:
                return orgomg.cwm.resource.relational.RelationalPackage.VIEW__QUERY_EXPRESSION;
            default:
                return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == ColumnSet.class) {
            switch (baseFeatureID) {
            default:
                return -1;
            }
        }
        if (baseClass == NamedColumnSet.class) {
            switch (baseFeatureID) {
            case orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__USING_TRIGGER:
                return RelationalPackage.TD_VIEW__USING_TRIGGER;
            case orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__TYPE:
                return RelationalPackage.TD_VIEW__TYPE;
            case orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__OPTION_SCOPE_COLUMN:
                return RelationalPackage.TD_VIEW__OPTION_SCOPE_COLUMN;
            default:
                return -1;
            }
        }
        if (baseClass == View.class) {
            switch (baseFeatureID) {
            case orgomg.cwm.resource.relational.RelationalPackage.VIEW__IS_READ_ONLY:
                return RelationalPackage.TD_VIEW__IS_READ_ONLY;
            case orgomg.cwm.resource.relational.RelationalPackage.VIEW__CHECK_OPTION:
                return RelationalPackage.TD_VIEW__CHECK_OPTION;
            case orgomg.cwm.resource.relational.RelationalPackage.VIEW__QUERY_EXPRESSION:
                return RelationalPackage.TD_VIEW__QUERY_EXPRESSION;
            default:
                return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (isReadOnly: ");
        result.append(isReadOnly);
        result.append(", checkOption: ");
        result.append(checkOption);
        result.append(')');
        return result.toString();
    }

} //TdViewImpl
