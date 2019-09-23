/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.impl;

import java.util.HashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdExpression;

import orgomg.cwm.objectmodel.core.impl.ExpressionImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.cwm.relational.impl.TdExpressionImpl#getVersion <em>Version</em>}</li>
 * <li>{@link org.talend.cwm.relational.impl.TdExpressionImpl#getModificationDate <em>Modification Date</em>}</li>
 * <li>{@link org.talend.cwm.relational.impl.TdExpressionImpl#getName <em>Name</em>}</li>
 * <li>{@link org.talend.cwm.relational.impl.TdExpressionImpl#getExpressionVariableMap <em>Expression Variable
 * Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdExpressionImpl extends ExpressionImpl implements TdExpression {

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #getModificationDate() <em>Modification Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getModificationDate()
     * @generated
     * @ordered
     */
    protected static final String MODIFICATION_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getModificationDate() <em>Modification Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getModificationDate()
     * @generated
     * @ordered
     */
    protected String modificationDate = MODIFICATION_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getExpressionVariableMap() <em>Expression Variable Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getExpressionVariableMap()
     * @generated
     * @ordered
     */
    protected HashMap<String, String> expressionVariableMap;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TdExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RelationalPackage.Literals.TD_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_EXPRESSION__VERSION, oldVersion,
                    version));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getModificationDate() {
        return modificationDate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setModificationDate(String newModificationDate) {
        String oldModificationDate = modificationDate;
        modificationDate = newModificationDate;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_EXPRESSION__MODIFICATION_DATE,
                    oldModificationDate, modificationDate));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, RelationalPackage.TD_EXPRESSION__NAME, oldName,
                    name));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public HashMap<String, String> getExpressionVariableMap() {
        return expressionVariableMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setExpressionVariableMap(HashMap<String, String> newExpressionVariableMap) {
        HashMap<String, String> oldExpressionVariableMap = expressionVariableMap;
        expressionVariableMap = newExpressionVariableMap;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET,
                    RelationalPackage.TD_EXPRESSION__EXPRESSION_VARIABLE_MAP, oldExpressionVariableMap,
                    expressionVariableMap));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case RelationalPackage.TD_EXPRESSION__VERSION:
            return getVersion();
        case RelationalPackage.TD_EXPRESSION__MODIFICATION_DATE:
            return getModificationDate();
        case RelationalPackage.TD_EXPRESSION__NAME:
            return getName();
        case RelationalPackage.TD_EXPRESSION__EXPRESSION_VARIABLE_MAP:
            return getExpressionVariableMap();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case RelationalPackage.TD_EXPRESSION__VERSION:
            setVersion((String) newValue);
            return;
        case RelationalPackage.TD_EXPRESSION__MODIFICATION_DATE:
            setModificationDate((String) newValue);
            return;
        case RelationalPackage.TD_EXPRESSION__NAME:
            setName((String) newValue);
            return;
        case RelationalPackage.TD_EXPRESSION__EXPRESSION_VARIABLE_MAP:
            setExpressionVariableMap((HashMap<String, String>) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case RelationalPackage.TD_EXPRESSION__VERSION:
            setVersion(VERSION_EDEFAULT);
            return;
        case RelationalPackage.TD_EXPRESSION__MODIFICATION_DATE:
            setModificationDate(MODIFICATION_DATE_EDEFAULT);
            return;
        case RelationalPackage.TD_EXPRESSION__NAME:
            setName(NAME_EDEFAULT);
            return;
        case RelationalPackage.TD_EXPRESSION__EXPRESSION_VARIABLE_MAP:
            setExpressionVariableMap((HashMap<String, String>) null);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case RelationalPackage.TD_EXPRESSION__VERSION:
            return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
        case RelationalPackage.TD_EXPRESSION__MODIFICATION_DATE:
            return MODIFICATION_DATE_EDEFAULT == null ? modificationDate != null
                    : !MODIFICATION_DATE_EDEFAULT.equals(modificationDate);
        case RelationalPackage.TD_EXPRESSION__NAME:
            return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
        case RelationalPackage.TD_EXPRESSION__EXPRESSION_VARIABLE_MAP:
            return expressionVariableMap != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (version: ");
        result.append(version);
        result.append(", modificationDate: ");
        result.append(modificationDate);
        result.append(", name: ");
        result.append(name);
        result.append(", expressionVariableMap: ");
        result.append(expressionVariableMap);
        result.append(')');
        return result.toString();
    }

    /**
     * Judge whether two expression take same database type and same version
     * 
     * @generated not
     */
    @Override
    public boolean sameVersionAndType(TdExpression targetEx) {
        boolean sameLanguage = this.getLanguage().equals(targetEx.getLanguage());
        String currentVersion = this.getVersion();
        String targetVersion = targetEx.getVersion();
        boolean sameVersion = currentVersion != null && currentVersion.equals(targetVersion);
        sameVersion = sameVersion || currentVersion == null && targetVersion == null;
        return sameLanguage && sameVersion;
    }

} // TdExpressionImpl
