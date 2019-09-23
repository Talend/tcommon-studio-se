/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;

import org.talend.cwm.relational.*;

import orgomg.cwm.objectmodel.behavioral.BehavioralFeature;
import orgomg.cwm.objectmodel.behavioral.Method;

import orgomg.cwm.objectmodel.core.Attribute;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.DataType;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.StructuralFeature;

import orgomg.cwm.resource.record.Field;

import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Procedure;
import orgomg.cwm.resource.relational.SQLDataType;
import orgomg.cwm.resource.relational.SQLSimpleType;
import orgomg.cwm.resource.relational.Table;
import orgomg.cwm.resource.relational.Trigger;
import orgomg.cwm.resource.relational.View;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.relational.RelationalPackage
 * @generated
 */
public class RelationalSwitch<T> {

    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RelationalPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelationalSwitch() {
        if (modelPackage == null) {
            modelPackage = RelationalPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        } else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
        case RelationalPackage.TD_TABLE: {
            TdTable tdTable = (TdTable) theEObject;
            T result = caseTdTable(tdTable);
            if (result == null)
                result = caseMetadataTable(tdTable);
            if (result == null)
                result = caseTable(tdTable);
            if (result == null)
                result = caseAbstractMetadataObject(tdTable);
            if (result == null)
                result = caseNamedColumnSet(tdTable);
            if (result == null)
                result = caseClassifier(tdTable);
            if (result == null)
                result = caseColumnSet(tdTable);
            if (result == null)
                result = caseElement(tdTable);
            if (result == null)
                result = caseClass(tdTable);
            if (result == null)
                result = caseNamespace(tdTable);
            if (result == null)
                result = caseModelElement(tdTable);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_VIEW: {
            TdView tdView = (TdView) theEObject;
            T result = caseTdView(tdView);
            if (result == null)
                result = caseMetadataTable(tdView);
            if (result == null)
                result = caseView(tdView);
            if (result == null)
                result = caseAbstractMetadataObject(tdView);
            if (result == null)
                result = caseNamedColumnSet(tdView);
            if (result == null)
                result = caseClassifier(tdView);
            if (result == null)
                result = caseColumnSet(tdView);
            if (result == null)
                result = caseElement(tdView);
            if (result == null)
                result = caseClass(tdView);
            if (result == null)
                result = caseNamespace(tdView);
            if (result == null)
                result = caseModelElement(tdView);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_COLUMN: {
            TdColumn tdColumn = (TdColumn) theEObject;
            T result = caseTdColumn(tdColumn);
            if (result == null)
                result = caseMetadataColumn(tdColumn);
            if (result == null)
                result = caseAbstractMetadataObject(tdColumn);
            if (result == null)
                result = caseField(tdColumn);
            if (result == null)
                result = caseAttribute(tdColumn);
            if (result == null)
                result = caseElement(tdColumn);
            if (result == null)
                result = caseStructuralFeature(tdColumn);
            if (result == null)
                result = caseFeature(tdColumn);
            if (result == null)
                result = caseModelElement(tdColumn);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_SQL_DATA_TYPE: {
            TdSqlDataType tdSqlDataType = (TdSqlDataType) theEObject;
            T result = caseTdSqlDataType(tdSqlDataType);
            if (result == null)
                result = caseSQLSimpleType(tdSqlDataType);
            if (result == null)
                result = caseSQLDataType(tdSqlDataType);
            if (result == null)
                result = caseDataType(tdSqlDataType);
            if (result == null)
                result = caseClassifier(tdSqlDataType);
            if (result == null)
                result = caseNamespace(tdSqlDataType);
            if (result == null)
                result = caseModelElement(tdSqlDataType);
            if (result == null)
                result = caseElement(tdSqlDataType);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_TRIGGER: {
            TdTrigger tdTrigger = (TdTrigger) theEObject;
            T result = caseTdTrigger(tdTrigger);
            if (result == null)
                result = caseTrigger(tdTrigger);
            if (result == null)
                result = caseModelElement(tdTrigger);
            if (result == null)
                result = caseElement(tdTrigger);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_PROCEDURE: {
            TdProcedure tdProcedure = (TdProcedure) theEObject;
            T result = caseTdProcedure(tdProcedure);
            if (result == null)
                result = caseProcedure(tdProcedure);
            if (result == null)
                result = caseMethod(tdProcedure);
            if (result == null)
                result = caseBehavioralFeature(tdProcedure);
            if (result == null)
                result = caseFeature(tdProcedure);
            if (result == null)
                result = caseModelElement(tdProcedure);
            if (result == null)
                result = caseElement(tdProcedure);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        case RelationalPackage.TD_EXPRESSION: {
            TdExpression tdExpression = (TdExpression) theEObject;
            T result = caseTdExpression(tdExpression);
            if (result == null)
                result = caseExpression(tdExpression);
            if (result == null)
                result = caseElement(tdExpression);
            if (result == null)
                result = defaultCase(theEObject);
            return result;
        }
        default:
            return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Table</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdTable(TdTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td View</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td View</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdView(TdView object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Column</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Column</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdColumn(TdColumn object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Sql Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Sql Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdSqlDataType(TdSqlDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Trigger</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdTrigger(TdTrigger object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Procedure</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Procedure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdProcedure(TdProcedure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Td Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Td Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTdExpression(TdExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelElement(ModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Metadata Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Metadata Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractMetadataObject(AbstractMetadataObject object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespace(Namespace object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClassifier(Classifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Class</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClass(orgomg.cwm.objectmodel.core.Class object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Table</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataTable(MetadataTable object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Column Set</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Column Set</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseColumnSet(ColumnSet object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Named Column Set</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Named Column Set</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamedColumnSet(NamedColumnSet object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Table</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Table</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTable(Table object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseView(View object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeature(Feature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structural Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structural Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructuralFeature(StructuralFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttribute(Attribute object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Field</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseField(Field object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Column</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Column</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataColumn(MetadataColumn object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SQL Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SQL Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSQLDataType(SQLDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataType(DataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>SQL Simple Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>SQL Simple Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSQLSimpleType(SQLSimpleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Trigger</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Trigger</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTrigger(Trigger object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBehavioralFeature(BehavioralFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Method</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Method</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMethod(Method object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Procedure</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Procedure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcedure(Procedure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }

} //RelationalSwitch
