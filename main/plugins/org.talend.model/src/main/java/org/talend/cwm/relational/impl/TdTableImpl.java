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
import org.talend.cwm.relational.TdTable;

import orgomg.cwm.resource.relational.Column;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.SQLStructuredType;
import orgomg.cwm.resource.relational.Table;
import orgomg.cwm.resource.relational.Trigger;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#getUsingTrigger <em>Using Trigger</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#getOptionScopeColumn <em>Option Scope Column</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#isIsTemporary <em>Is Temporary</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#getTemporaryScope <em>Temporary Scope</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#isIsSystem <em>Is System</em>}</li>
 *   <li>{@link org.talend.cwm.relational.impl.TdTableImpl#getTrigger <em>Trigger</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdTableImpl extends MetadataTableImpl implements TdTable {

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
     * The default value of the '{@link #isIsTemporary() <em>Is Temporary</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsTemporary()
     * @generated
     * @ordered
     */
    protected static final boolean IS_TEMPORARY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsTemporary() <em>Is Temporary</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsTemporary()
     * @generated
     * @ordered
     */
    protected boolean isTemporary = IS_TEMPORARY_EDEFAULT;

    /**
     * The default value of the '{@link #getTemporaryScope() <em>Temporary Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporaryScope()
     * @generated
     * @ordered
     */
    protected static final String TEMPORARY_SCOPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTemporaryScope() <em>Temporary Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporaryScope()
     * @generated
     * @ordered
     */
    protected String temporaryScope = TEMPORARY_SCOPE_EDEFAULT;

    /**
     * The default value of the '{@link #isIsSystem() <em>Is System</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSystem()
     * @generated
     * @ordered
     */
    protected static final boolean IS_SYSTEM_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsSystem() <em>Is System</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSystem()
     * @generated
     * @ordered
     */
    protected boolean isSystem = IS_SYSTEM_EDEFAULT;

    /**
     * The cached value of the '{@link #getTrigger() <em>Trigger</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTrigger()
     * @generated
     * @ordered
     */
    protected EList<Trigger> trigger;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdTableImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RelationalPackage.Literals.TD_TABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Trigger> getUsingTrigger() {
        if (usingTrigger == null) {
            usingTrigger = new EObjectWithInverseResolvingEList.ManyInverse<Trigger>(Trigger.class, this,
                    RelationalPackage.TD_TABLE__USING_TRIGGER,
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
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RelationalPackage.TD_TABLE__TYPE, oldType, type));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_TABLE__TYPE,
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
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_TABLE__TYPE, newType, newType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Column> getOptionScopeColumn() {
        if (optionScopeColumn == null) {
            optionScopeColumn = new EObjectWithInverseResolvingEList<Column>(Column.class, this,
                    RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN,
                    orgomg.cwm.resource.relational.RelationalPackage.COLUMN__OPTION_SCOPE_COLUMN_SET);
        }
        return optionScopeColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsTemporary() {
        return isTemporary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsTemporary(boolean newIsTemporary) {
        boolean oldIsTemporary = isTemporary;
        isTemporary = newIsTemporary;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_TABLE__IS_TEMPORARY, oldIsTemporary,
                    isTemporary));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTemporaryScope() {
        return temporaryScope;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporaryScope(String newTemporaryScope) {
        String oldTemporaryScope = temporaryScope;
        temporaryScope = newTemporaryScope;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_TABLE__TEMPORARY_SCOPE, oldTemporaryScope,
                    temporaryScope));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsSystem() {
        return isSystem;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsSystem(boolean newIsSystem) {
        boolean oldIsSystem = isSystem;
        isSystem = newIsSystem;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_TABLE__IS_SYSTEM, oldIsSystem, isSystem));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Trigger> getTrigger() {
        if (trigger == null) {
            trigger = new EObjectWithInverseResolvingEList<Trigger>(Trigger.class, this, RelationalPackage.TD_TABLE__TRIGGER,
                    orgomg.cwm.resource.relational.RelationalPackage.TRIGGER__TABLE);
        }
        return trigger;
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getUsingTrigger()).basicAdd(otherEnd, msgs);
        case RelationalPackage.TD_TABLE__TYPE:
            if (type != null)
                msgs = ((InternalEObject) type).eInverseRemove(this,
                        orgomg.cwm.resource.relational.RelationalPackage.SQL_STRUCTURED_TYPE__COLUMN_SET,
                        SQLStructuredType.class, msgs);
            return basicSetType((SQLStructuredType) otherEnd, msgs);
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getOptionScopeColumn()).basicAdd(otherEnd, msgs);
        case RelationalPackage.TD_TABLE__TRIGGER:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getTrigger()).basicAdd(otherEnd, msgs);
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            return ((InternalEList<?>) getUsingTrigger()).basicRemove(otherEnd, msgs);
        case RelationalPackage.TD_TABLE__TYPE:
            return basicSetType(null, msgs);
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            return ((InternalEList<?>) getOptionScopeColumn()).basicRemove(otherEnd, msgs);
        case RelationalPackage.TD_TABLE__TRIGGER:
            return ((InternalEList<?>) getTrigger()).basicRemove(otherEnd, msgs);
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            return getUsingTrigger();
        case RelationalPackage.TD_TABLE__TYPE:
            if (resolve)
                return getType();
            return basicGetType();
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            return getOptionScopeColumn();
        case RelationalPackage.TD_TABLE__IS_TEMPORARY:
            return isIsTemporary();
        case RelationalPackage.TD_TABLE__TEMPORARY_SCOPE:
            return getTemporaryScope();
        case RelationalPackage.TD_TABLE__IS_SYSTEM:
            return isIsSystem();
        case RelationalPackage.TD_TABLE__TRIGGER:
            return getTrigger();
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            getUsingTrigger().clear();
            getUsingTrigger().addAll((Collection<? extends Trigger>) newValue);
            return;
        case RelationalPackage.TD_TABLE__TYPE:
            setType((SQLStructuredType) newValue);
            return;
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            getOptionScopeColumn().clear();
            getOptionScopeColumn().addAll((Collection<? extends Column>) newValue);
            return;
        case RelationalPackage.TD_TABLE__IS_TEMPORARY:
            setIsTemporary((Boolean) newValue);
            return;
        case RelationalPackage.TD_TABLE__TEMPORARY_SCOPE:
            setTemporaryScope((String) newValue);
            return;
        case RelationalPackage.TD_TABLE__IS_SYSTEM:
            setIsSystem((Boolean) newValue);
            return;
        case RelationalPackage.TD_TABLE__TRIGGER:
            getTrigger().clear();
            getTrigger().addAll((Collection<? extends Trigger>) newValue);
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            getUsingTrigger().clear();
            return;
        case RelationalPackage.TD_TABLE__TYPE:
            setType((SQLStructuredType) null);
            return;
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            getOptionScopeColumn().clear();
            return;
        case RelationalPackage.TD_TABLE__IS_TEMPORARY:
            setIsTemporary(IS_TEMPORARY_EDEFAULT);
            return;
        case RelationalPackage.TD_TABLE__TEMPORARY_SCOPE:
            setTemporaryScope(TEMPORARY_SCOPE_EDEFAULT);
            return;
        case RelationalPackage.TD_TABLE__IS_SYSTEM:
            setIsSystem(IS_SYSTEM_EDEFAULT);
            return;
        case RelationalPackage.TD_TABLE__TRIGGER:
            getTrigger().clear();
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
        case RelationalPackage.TD_TABLE__USING_TRIGGER:
            return usingTrigger != null && !usingTrigger.isEmpty();
        case RelationalPackage.TD_TABLE__TYPE:
            return type != null;
        case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
            return optionScopeColumn != null && !optionScopeColumn.isEmpty();
        case RelationalPackage.TD_TABLE__IS_TEMPORARY:
            return isTemporary != IS_TEMPORARY_EDEFAULT;
        case RelationalPackage.TD_TABLE__TEMPORARY_SCOPE:
            return TEMPORARY_SCOPE_EDEFAULT == null ? temporaryScope != null : !TEMPORARY_SCOPE_EDEFAULT.equals(temporaryScope);
        case RelationalPackage.TD_TABLE__IS_SYSTEM:
            return isSystem != IS_SYSTEM_EDEFAULT;
        case RelationalPackage.TD_TABLE__TRIGGER:
            return trigger != null && !trigger.isEmpty();
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
            case RelationalPackage.TD_TABLE__USING_TRIGGER:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__USING_TRIGGER;
            case RelationalPackage.TD_TABLE__TYPE:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__TYPE;
            case RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN:
                return orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__OPTION_SCOPE_COLUMN;
            default:
                return -1;
            }
        }
        if (baseClass == Table.class) {
            switch (derivedFeatureID) {
            case RelationalPackage.TD_TABLE__IS_TEMPORARY:
                return orgomg.cwm.resource.relational.RelationalPackage.TABLE__IS_TEMPORARY;
            case RelationalPackage.TD_TABLE__TEMPORARY_SCOPE:
                return orgomg.cwm.resource.relational.RelationalPackage.TABLE__TEMPORARY_SCOPE;
            case RelationalPackage.TD_TABLE__IS_SYSTEM:
                return orgomg.cwm.resource.relational.RelationalPackage.TABLE__IS_SYSTEM;
            case RelationalPackage.TD_TABLE__TRIGGER:
                return orgomg.cwm.resource.relational.RelationalPackage.TABLE__TRIGGER;
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
                return RelationalPackage.TD_TABLE__USING_TRIGGER;
            case orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__TYPE:
                return RelationalPackage.TD_TABLE__TYPE;
            case orgomg.cwm.resource.relational.RelationalPackage.NAMED_COLUMN_SET__OPTION_SCOPE_COLUMN:
                return RelationalPackage.TD_TABLE__OPTION_SCOPE_COLUMN;
            default:
                return -1;
            }
        }
        if (baseClass == Table.class) {
            switch (baseFeatureID) {
            case orgomg.cwm.resource.relational.RelationalPackage.TABLE__IS_TEMPORARY:
                return RelationalPackage.TD_TABLE__IS_TEMPORARY;
            case orgomg.cwm.resource.relational.RelationalPackage.TABLE__TEMPORARY_SCOPE:
                return RelationalPackage.TD_TABLE__TEMPORARY_SCOPE;
            case orgomg.cwm.resource.relational.RelationalPackage.TABLE__IS_SYSTEM:
                return RelationalPackage.TD_TABLE__IS_SYSTEM;
            case orgomg.cwm.resource.relational.RelationalPackage.TABLE__TRIGGER:
                return RelationalPackage.TD_TABLE__TRIGGER;
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
        result.append(" (isTemporary: ");
        result.append(isTemporary);
        result.append(", temporaryScope: ");
        result.append(temporaryScope);
        result.append(", isSystem: ");
        result.append(isSystem);
        result.append(')');
        return result.toString();
    }

} //TdTableImpl
