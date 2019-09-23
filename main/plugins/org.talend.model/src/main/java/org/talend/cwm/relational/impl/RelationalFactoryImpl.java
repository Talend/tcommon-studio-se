/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.impl;

import java.util.HashMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.talend.cwm.relational.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RelationalFactoryImpl extends EFactoryImpl implements RelationalFactory {

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static RelationalFactory init() {
        try {
            RelationalFactory theRelationalFactory = (RelationalFactory) EPackage.Registry.INSTANCE
                    .getEFactory(RelationalPackage.eNS_URI);
            if (theRelationalFactory != null) {
                return theRelationalFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new RelationalFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelationalFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case RelationalPackage.TD_TABLE:
            return createTdTable();
        case RelationalPackage.TD_VIEW:
            return createTdView();
        case RelationalPackage.TD_COLUMN:
            return createTdColumn();
        case RelationalPackage.TD_SQL_DATA_TYPE:
            return createTdSqlDataType();
        case RelationalPackage.TD_TRIGGER:
            return createTdTrigger();
        case RelationalPackage.TD_PROCEDURE:
            return createTdProcedure();
        case RelationalPackage.TD_EXPRESSION:
            return createTdExpression();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
        case RelationalPackage.JAVA_HASH_MAP:
            return createjavaHashMapFromString(eDataType, initialValue);
        default:
            throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
        case RelationalPackage.JAVA_HASH_MAP:
            return convertjavaHashMapToString(eDataType, instanceValue);
        default:
            throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdTable createTdTable() {
        TdTableImpl tdTable = new TdTableImpl();
        return tdTable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdView createTdView() {
        TdViewImpl tdView = new TdViewImpl();
        return tdView;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdColumn createTdColumn() {
        TdColumnImpl tdColumn = new TdColumnImpl();
        return tdColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdSqlDataType createTdSqlDataType() {
        TdSqlDataTypeImpl tdSqlDataType = new TdSqlDataTypeImpl();
        return tdSqlDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdTrigger createTdTrigger() {
        TdTriggerImpl tdTrigger = new TdTriggerImpl();
        return tdTrigger;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdProcedure createTdProcedure() {
        TdProcedureImpl tdProcedure = new TdProcedureImpl();
        return tdProcedure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdExpression createTdExpression() {
        TdExpressionImpl tdExpression = new TdExpressionImpl();
        return tdExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> createjavaHashMapFromString(EDataType eDataType, String initialValue) {
        return (HashMap<String, String>) super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertjavaHashMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelationalPackage getRelationalPackage() {
        return (RelationalPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static RelationalPackage getPackage() {
        return RelationalPackage.eINSTANCE;
    }

} //RelationalFactoryImpl
