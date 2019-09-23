/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.cwm.xml.TdXmlContent;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.XmlPackage;

import orgomg.cwm.resource.xml.impl.ContentImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Xml Content</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlContentImpl#getXmlElements <em>Xml Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdXmlContentImpl extends ContentImpl implements TdXmlContent {

    /**
     * The cached value of the '{@link #getXmlElements() <em>Xml Elements</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlElements()
     * @generated
     * @ordered
     */
    protected EList<TdXmlElementType> xmlElements;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdXmlContentImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.TD_XML_CONTENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TdXmlElementType> getXmlElements() {
        if (xmlElements == null) {
            xmlElements = new EObjectContainmentEList.Resolving<TdXmlElementType>(TdXmlElementType.class, this,
                    XmlPackage.TD_XML_CONTENT__XML_ELEMENTS);
        }
        return xmlElements;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case XmlPackage.TD_XML_CONTENT__XML_ELEMENTS:
            return ((InternalEList<?>) getXmlElements()).basicRemove(otherEnd, msgs);
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
        case XmlPackage.TD_XML_CONTENT__XML_ELEMENTS:
            return getXmlElements();
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
        case XmlPackage.TD_XML_CONTENT__XML_ELEMENTS:
            getXmlElements().clear();
            getXmlElements().addAll((Collection<? extends TdXmlElementType>) newValue);
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
        case XmlPackage.TD_XML_CONTENT__XML_ELEMENTS:
            getXmlElements().clear();
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
        case XmlPackage.TD_XML_CONTENT__XML_ELEMENTS:
            return xmlElements != null && !xmlElements.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TdXmlContentImpl
