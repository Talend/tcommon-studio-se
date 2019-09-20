/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.relational.RelationalPackage
 * @generated
 */
public interface RelationalFactory extends EFactory {

    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    RelationalFactory eINSTANCE = org.talend.cwm.relational.impl.RelationalFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Td Table</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Table</em>'.
     * @generated
     */
    TdTable createTdTable();

    /**
     * Returns a new object of class '<em>Td View</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td View</em>'.
     * @generated
     */
    TdView createTdView();

    /**
     * Returns a new object of class '<em>Td Column</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Column</em>'.
     * @generated
     */
    TdColumn createTdColumn();

    /**
     * Returns a new object of class '<em>Td Sql Data Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Sql Data Type</em>'.
     * @generated
     */
    TdSqlDataType createTdSqlDataType();

    /**
     * Returns a new object of class '<em>Td Trigger</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Trigger</em>'.
     * @generated
     */
    TdTrigger createTdTrigger();

    /**
     * Returns a new object of class '<em>Td Procedure</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Procedure</em>'.
     * @generated
     */
    TdProcedure createTdProcedure();

    /**
     * Returns a new object of class '<em>Td Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Td Expression</em>'.
     * @generated
     */
    TdExpression createTdExpression();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    RelationalPackage getRelationalPackage();

} //RelationalFactory
