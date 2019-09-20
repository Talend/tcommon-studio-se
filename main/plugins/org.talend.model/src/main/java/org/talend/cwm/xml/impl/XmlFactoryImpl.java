/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.talend.cwm.xml.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XmlFactoryImpl extends EFactoryImpl implements XmlFactory {

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static XmlFactory init() {
        try {
            XmlFactory theXmlFactory = (XmlFactory) EPackage.Registry.INSTANCE.getEFactory(XmlPackage.eNS_URI);
            if (theXmlFactory != null) {
                return theXmlFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new XmlFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XmlFactoryImpl() {
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
        case XmlPackage.TD_XML_ELEMENT_TYPE:
            return createTdXmlElementType();
        case XmlPackage.TD_XML_CONTENT:
            return createTdXmlContent();
        case XmlPackage.TD_XML_SCHEMA:
            return createTdXmlSchema();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlElementType createTdXmlElementType() {
        TdXmlElementTypeImpl tdXmlElementType = new TdXmlElementTypeImpl();
        return tdXmlElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlContent createTdXmlContent() {
        TdXmlContentImpl tdXmlContent = new TdXmlContentImpl();
        return tdXmlContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlSchema createTdXmlSchema() {
        TdXmlSchemaImpl tdXmlSchema = new TdXmlSchemaImpl();
        return tdXmlSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XmlPackage getXmlPackage() {
        return (XmlPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static XmlPackage getPackage() {
        return XmlPackage.eINSTANCE;
    }

} //XmlFactoryImpl
