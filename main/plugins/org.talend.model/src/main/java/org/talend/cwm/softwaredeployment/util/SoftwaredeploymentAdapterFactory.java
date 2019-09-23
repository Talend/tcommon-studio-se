/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.softwaredeployment.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.talend.cwm.softwaredeployment.*;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DeployedComponent;
import orgomg.cwm.foundation.softwaredeployment.Machine;
import orgomg.cwm.foundation.softwaredeployment.SoftwareSystem;

import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Subsystem;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage
 * @generated
 */
public class SoftwaredeploymentAdapterFactory extends AdapterFactoryImpl {

    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static SoftwaredeploymentPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SoftwaredeploymentAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = SoftwaredeploymentPackage.eINSTANCE;
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
    protected SoftwaredeploymentSwitch<Adapter> modelSwitch = new SoftwaredeploymentSwitch<Adapter>() {

        @Override
        public Adapter caseTdDataManager(TdDataManager object) {
            return createTdDataManagerAdapter();
        }

        @Override
        public Adapter caseTdSoftwareSystem(TdSoftwareSystem object) {
            return createTdSoftwareSystemAdapter();
        }

        @Override
        public Adapter caseTdMachine(TdMachine object) {
            return createTdMachineAdapter();
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
        public Adapter caseNamespace(Namespace object) {
            return createNamespaceAdapter();
        }

        @Override
        public Adapter casePackage(orgomg.cwm.objectmodel.core.Package object) {
            return createPackageAdapter();
        }

        @Override
        public Adapter caseDeployedComponent(DeployedComponent object) {
            return createDeployedComponentAdapter();
        }

        @Override
        public Adapter caseDataManager(DataManager object) {
            return createDataManagerAdapter();
        }

        @Override
        public Adapter caseClassifier(Classifier object) {
            return createClassifierAdapter();
        }

        @Override
        public Adapter caseSubsystem(Subsystem object) {
            return createSubsystemAdapter();
        }

        @Override
        public Adapter caseSoftwareSystem(SoftwareSystem object) {
            return createSoftwareSystemAdapter();
        }

        @Override
        public Adapter caseMachine(Machine object) {
            return createMachineAdapter();
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
     * Creates a new adapter for an object of class '{@link org.talend.cwm.softwaredeployment.TdDataManager <em>Td Data Manager</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.softwaredeployment.TdDataManager
     * @generated
     */
    public Adapter createTdDataManagerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.softwaredeployment.TdSoftwareSystem <em>Td Software System</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.softwaredeployment.TdSoftwareSystem
     * @generated
     */
    public Adapter createTdSoftwareSystemAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.softwaredeployment.TdMachine <em>Td Machine</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.softwaredeployment.TdMachine
     * @generated
     */
    public Adapter createTdMachineAdapter() {
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
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Package <em>Package</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Package
     * @generated
     */
    public Adapter createPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.DeployedComponent <em>Deployed Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.DeployedComponent
     * @generated
     */
    public Adapter createDeployedComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.DataManager <em>Data Manager</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.DataManager
     * @generated
     */
    public Adapter createDataManagerAdapter() {
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
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Subsystem <em>Subsystem</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Subsystem
     * @generated
     */
    public Adapter createSubsystemAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.SoftwareSystem <em>Software System</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.SoftwareSystem
     * @generated
     */
    public Adapter createSoftwareSystemAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.foundation.softwaredeployment.Machine <em>Machine</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.foundation.softwaredeployment.Machine
     * @generated
     */
    public Adapter createMachineAdapter() {
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

} //SoftwaredeploymentAdapterFactory
