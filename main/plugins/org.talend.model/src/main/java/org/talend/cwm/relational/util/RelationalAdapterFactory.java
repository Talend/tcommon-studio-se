/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.relational.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

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
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.relational.RelationalPackage
 * @generated
 */
public class RelationalAdapterFactory extends AdapterFactoryImpl {

    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RelationalPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelationalAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = RelationalPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RelationalSwitch<Adapter> modelSwitch = new RelationalSwitch<Adapter>() {

        @Override
        public Adapter caseTdTable(TdTable object) {
            return createTdTableAdapter();
        }

        @Override
        public Adapter caseTdView(TdView object) {
            return createTdViewAdapter();
        }

        @Override
        public Adapter caseTdColumn(TdColumn object) {
            return createTdColumnAdapter();
        }

        @Override
        public Adapter caseTdSqlDataType(TdSqlDataType object) {
            return createTdSqlDataTypeAdapter();
        }

        @Override
        public Adapter caseTdTrigger(TdTrigger object) {
            return createTdTriggerAdapter();
        }

        @Override
        public Adapter caseTdProcedure(TdProcedure object) {
            return createTdProcedureAdapter();
        }

        @Override
        public Adapter caseTdExpression(TdExpression object) {
            return createTdExpressionAdapter();
        }

        @Override
        public Adapter caseElement(Element object) {
            return createElementAdapter();
        }

        @Override
        public Adapter caseModelElement(ModelElement object) {
            return createModelElementAdapter();
        }

        @Override
        public Adapter caseAbstractMetadataObject(AbstractMetadataObject object) {
            return createAbstractMetadataObjectAdapter();
        }

        @Override
        public Adapter caseNamespace(Namespace object) {
            return createNamespaceAdapter();
        }

        @Override
        public Adapter caseClassifier(Classifier object) {
            return createClassifierAdapter();
        }

        @Override
        public Adapter caseClass(orgomg.cwm.objectmodel.core.Class object) {
            return createClassAdapter();
        }

        @Override
        public Adapter caseMetadataTable(MetadataTable object) {
            return createMetadataTableAdapter();
        }

        @Override
        public Adapter caseColumnSet(ColumnSet object) {
            return createColumnSetAdapter();
        }

        @Override
        public Adapter caseNamedColumnSet(NamedColumnSet object) {
            return createNamedColumnSetAdapter();
        }

        @Override
        public Adapter caseTable(Table object) {
            return createTableAdapter();
        }

        @Override
        public Adapter caseView(View object) {
            return createViewAdapter();
        }

        @Override
        public Adapter caseFeature(Feature object) {
            return createFeatureAdapter();
        }

        @Override
        public Adapter caseStructuralFeature(StructuralFeature object) {
            return createStructuralFeatureAdapter();
        }

        @Override
        public Adapter caseAttribute(Attribute object) {
            return createAttributeAdapter();
        }

        @Override
        public Adapter caseField(Field object) {
            return createFieldAdapter();
        }

        @Override
        public Adapter caseMetadataColumn(MetadataColumn object) {
            return createMetadataColumnAdapter();
        }

        @Override
        public Adapter caseSQLDataType(SQLDataType object) {
            return createSQLDataTypeAdapter();
        }

        @Override
        public Adapter caseDataType(DataType object) {
            return createDataTypeAdapter();
        }

        @Override
        public Adapter caseSQLSimpleType(SQLSimpleType object) {
            return createSQLSimpleTypeAdapter();
        }

        @Override
        public Adapter caseTrigger(Trigger object) {
            return createTriggerAdapter();
        }

        @Override
        public Adapter caseBehavioralFeature(BehavioralFeature object) {
            return createBehavioralFeatureAdapter();
        }

        @Override
        public Adapter caseMethod(Method object) {
            return createMethodAdapter();
        }

        @Override
        public Adapter caseProcedure(Procedure object) {
            return createProcedureAdapter();
        }

        @Override
        public Adapter caseExpression(Expression object) {
            return createExpressionAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdTable <em>Td Table</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdTable
     * @generated
     */
    public Adapter createTdTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdView <em>Td View</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdView
     * @generated
     */
    public Adapter createTdViewAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdColumn <em>Td Column</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdColumn
     * @generated
     */
    public Adapter createTdColumnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdSqlDataType <em>Td Sql Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdSqlDataType
     * @generated
     */
    public Adapter createTdSqlDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdTrigger <em>Td Trigger</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdTrigger
     * @generated
     */
    public Adapter createTdTriggerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdProcedure <em>Td Procedure</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdProcedure
     * @generated
     */
    public Adapter createTdProcedureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.relational.TdExpression <em>Td Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.relational.TdExpression
     * @generated
     */
    public Adapter createTdExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Element <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Element
     * @generated
     */
    public Adapter createElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.ModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.ModelElement
     * @generated
     */
    public Adapter createModelElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.AbstractMetadataObject <em>Abstract Metadata Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject
     * @generated
     */
    public Adapter createAbstractMetadataObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Namespace <em>Namespace</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Namespace
     * @generated
     */
    public Adapter createNamespaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Classifier <em>Classifier</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Classifier
     * @generated
     */
    public Adapter createClassifierAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Class <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Class
     * @generated
     */
    public Adapter createClassAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.MetadataTable <em>Metadata Table</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable
     * @generated
     */
    public Adapter createMetadataTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.ColumnSet <em>Column Set</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.ColumnSet
     * @generated
     */
    public Adapter createColumnSetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.NamedColumnSet <em>Named Column Set</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.NamedColumnSet
     * @generated
     */
    public Adapter createNamedColumnSetAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.Table <em>Table</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.Table
     * @generated
     */
    public Adapter createTableAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.View <em>View</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.View
     * @generated
     */
    public Adapter createViewAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Feature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Feature
     * @generated
     */
    public Adapter createFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.StructuralFeature <em>Structural Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.StructuralFeature
     * @generated
     */
    public Adapter createStructuralFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Attribute <em>Attribute</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Attribute
     * @generated
     */
    public Adapter createAttributeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.record.Field <em>Field</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.record.Field
     * @generated
     */
    public Adapter createFieldAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn <em>Metadata Column</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.core.model.metadata.builder.connection.MetadataColumn
     * @generated
     */
    public Adapter createMetadataColumnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.SQLDataType <em>SQL Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.SQLDataType
     * @generated
     */
    public Adapter createSQLDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.DataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.DataType
     * @generated
     */
    public Adapter createDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.SQLSimpleType <em>SQL Simple Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.SQLSimpleType
     * @generated
     */
    public Adapter createSQLSimpleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.Trigger <em>Trigger</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.Trigger
     * @generated
     */
    public Adapter createTriggerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.behavioral.BehavioralFeature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.behavioral.BehavioralFeature
     * @generated
     */
    public Adapter createBehavioralFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.behavioral.Method <em>Method</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.behavioral.Method
     * @generated
     */
    public Adapter createMethodAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.relational.Procedure <em>Procedure</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.relational.Procedure
     * @generated
     */
    public Adapter createProcedureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Expression
     * @generated
     */
    public Adapter createExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //RelationalAdapterFactory
