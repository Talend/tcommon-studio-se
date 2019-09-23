/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.talend.cwm.xml.*;

import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;

import orgomg.cwm.objectmodel.instance.Extent;
import orgomg.cwm.objectmodel.instance.Instance;

import orgomg.cwm.resource.xml.Content;
import orgomg.cwm.resource.xml.ElementType;
import orgomg.cwm.resource.xml.Schema;
import orgomg.cwm.resource.xml.Document;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.xml.XmlPackage
 * @generated
 */
public class XmlAdapterFactory extends AdapterFactoryImpl {

    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static XmlPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XmlAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = XmlPackage.eINSTANCE;
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
    protected XmlSwitch<Adapter> modelSwitch = new XmlSwitch<Adapter>() {

        @Override
        public Adapter caseTdXmlElementType(TdXmlElementType object) {
            return createTdXmlElementTypeAdapter();
        }

        @Override
        public Adapter caseTdXmlContent(TdXmlContent object) {
            return createTdXmlContentAdapter();
        }

        @Override
        public Adapter caseTdXmlSchema(TdXmlSchema object) {
            return createTdXmlSchemaAdapter();
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
        public Adapter caseClassifier(Classifier object) {
            return createClassifierAdapter();
        }

        @Override
        public Adapter caseClass(orgomg.cwm.objectmodel.core.Class object) {
            return createClassAdapter();
        }

        @Override
        public Adapter caseElementType(ElementType object) {
            return createElementTypeAdapter();
        }

        @Override
        public Adapter caseContent(Content object) {
            return createContentAdapter();
        }

        @Override
        public Adapter casePackage(orgomg.cwm.objectmodel.core.Package object) {
            return createPackageAdapter();
        }

        @Override
        public Adapter caseSchema(Schema object) {
            return createSchemaAdapter();
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
     * Creates a new adapter for an object of class '{@link org.talend.cwm.xml.TdXmlElementType <em>Td Xml Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.xml.TdXmlElementType
     * @generated
     */
    public Adapter createTdXmlElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.xml.TdXmlContent <em>Td Xml Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.xml.TdXmlContent
     * @generated
     */
    public Adapter createTdXmlContentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.cwm.xml.TdXmlSchema <em>Td Xml Schema</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.cwm.xml.TdXmlSchema
     * @generated
     */
    public Adapter createTdXmlSchemaAdapter() {
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
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.xml.Content <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.xml.Content
     * @generated
     */
    public Adapter createContentAdapter() {
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
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.xml.ElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.xml.ElementType
     * @generated
     */
    public Adapter createElementTypeAdapter() {
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
     * Creates a new adapter for an object of class '{@link orgomg.cwm.resource.xml.Schema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.resource.xml.Schema
     * @generated
     */
    public Adapter createSchemaAdapter() {
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

} //XmlAdapterFactory
