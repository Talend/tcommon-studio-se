/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.xml.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.xml.TdXmlContent;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.XmlPackage;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.xml.impl.ElementTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Td Xml Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl#getXsdElementDeclaration <em>Xsd Element Declaration</em>}</li>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl#getOwnedDocument <em>Owned Document</em>}</li>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl#getJavaType <em>Java Type</em>}</li>
 *   <li>{@link org.talend.cwm.xml.impl.TdXmlElementTypeImpl#getXmlContent <em>Xml Content</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TdXmlElementTypeImpl extends ElementTypeImpl implements TdXmlElementType {

    /**
     * The cached value of the '{@link #getXsdElementDeclaration() <em>Xsd Element Declaration</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXsdElementDeclaration()
     * @generated
     * @ordered
     */
    protected EObject xsdElementDeclaration;

    /**
     * The cached value of the '{@link #getOwnedDocument() <em>Owned Document</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOwnedDocument()
     * @generated
     * @ordered
     */
    protected TdXmlSchema ownedDocument;

    /**
     * The default value of the '{@link #getJavaType() <em>Java Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaType()
     * @generated
     * @ordered
     */
    protected static final String JAVA_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJavaType() <em>Java Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaType()
     * @generated
     * @ordered
     */
    protected String javaType = JAVA_TYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getXmlContent() <em>Xml Content</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlContent()
     * @generated
     * @ordered
     */
    protected TdXmlContent xmlContent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TdXmlElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.TD_XML_ELEMENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getXsdElementDeclaration() {
        if (xsdElementDeclaration != null && xsdElementDeclaration.eIsProxy()) {
            InternalEObject oldXsdElementDeclaration = (InternalEObject) xsdElementDeclaration;
            xsdElementDeclaration = eResolveProxy(oldXsdElementDeclaration);
            if (xsdElementDeclaration != oldXsdElementDeclaration) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION, oldXsdElementDeclaration,
                            xsdElementDeclaration));
            }
        }
        return xsdElementDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject basicGetXsdElementDeclaration() {
        return xsdElementDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXsdElementDeclaration(EObject newXsdElementDeclaration) {
        EObject oldXsdElementDeclaration = xsdElementDeclaration;
        xsdElementDeclaration = newXsdElementDeclaration;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION,
                    oldXsdElementDeclaration, xsdElementDeclaration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlSchema getOwnedDocument() {
        if (ownedDocument != null && ownedDocument.eIsProxy()) {
            InternalEObject oldOwnedDocument = (InternalEObject) ownedDocument;
            ownedDocument = (TdXmlSchema) eResolveProxy(oldOwnedDocument);
            if (ownedDocument != oldOwnedDocument) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT,
                            oldOwnedDocument, ownedDocument));
            }
        }
        return ownedDocument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlSchema basicGetOwnedDocument() {
        return ownedDocument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOwnedDocument(TdXmlSchema newOwnedDocument) {
        TdXmlSchema oldOwnedDocument = ownedDocument;
        ownedDocument = newOwnedDocument;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT,
                    oldOwnedDocument, ownedDocument));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJavaType(String newJavaType) {
        String oldJavaType = javaType;
        javaType = newJavaType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.TD_XML_ELEMENT_TYPE__JAVA_TYPE, oldJavaType,
                    javaType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlContent getXmlContent() {
        if (xmlContent != null && xmlContent.eIsProxy()) {
            InternalEObject oldXmlContent = (InternalEObject) xmlContent;
            xmlContent = (TdXmlContent) eResolveProxy(oldXmlContent);
            if (xmlContent != oldXmlContent) {
                InternalEObject newXmlContent = (InternalEObject) xmlContent;
                NotificationChain msgs = oldXmlContent.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT, null, null);
                if (newXmlContent.eInternalContainer() == null) {
                    msgs = newXmlContent.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT,
                            null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT,
                            oldXmlContent, xmlContent));
            }
        }
        return xmlContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TdXmlContent basicGetXmlContent() {
        return xmlContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetXmlContent(TdXmlContent newXmlContent, NotificationChain msgs) {
        TdXmlContent oldXmlContent = xmlContent;
        xmlContent = newXmlContent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT, oldXmlContent, newXmlContent);
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
    public void setXmlContent(TdXmlContent newXmlContent) {
        if (newXmlContent != xmlContent) {
            NotificationChain msgs = null;
            if (xmlContent != null)
                msgs = ((InternalEObject) xmlContent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT, null, msgs);
            if (newXmlContent != null)
                msgs = ((InternalEObject) newXmlContent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT, null, msgs);
            msgs = basicSetXmlContent(newXmlContent, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT, newXmlContent,
                    newXmlContent));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setContentType(String contentType) {
        TaggedValueHelper.setTaggedValue(this, TaggedValueHelper.DATA_CONTENT_TYPE_TAGGED_VAL, contentType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public String getContentType() {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DATA_CONTENT_TYPE_TAGGED_VAL, this.getTaggedValue());
        if (tv == null) {
            return "";
        }
        return tv.getValue();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT:
            return basicSetXmlContent(null, msgs);
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
        case XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION:
            if (resolve)
                return getXsdElementDeclaration();
            return basicGetXsdElementDeclaration();
        case XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT:
            if (resolve)
                return getOwnedDocument();
            return basicGetOwnedDocument();
        case XmlPackage.TD_XML_ELEMENT_TYPE__JAVA_TYPE:
            return getJavaType();
        case XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT:
            if (resolve)
                return getXmlContent();
            return basicGetXmlContent();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION:
            setXsdElementDeclaration((EObject) newValue);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT:
            setOwnedDocument((TdXmlSchema) newValue);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__JAVA_TYPE:
            setJavaType((String) newValue);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT:
            setXmlContent((TdXmlContent) newValue);
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
        case XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION:
            setXsdElementDeclaration((EObject) null);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT:
            setOwnedDocument((TdXmlSchema) null);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__JAVA_TYPE:
            setJavaType(JAVA_TYPE_EDEFAULT);
            return;
        case XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT:
            setXmlContent((TdXmlContent) null);
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
        case XmlPackage.TD_XML_ELEMENT_TYPE__XSD_ELEMENT_DECLARATION:
            return xsdElementDeclaration != null;
        case XmlPackage.TD_XML_ELEMENT_TYPE__OWNED_DOCUMENT:
            return ownedDocument != null;
        case XmlPackage.TD_XML_ELEMENT_TYPE__JAVA_TYPE:
            return JAVA_TYPE_EDEFAULT == null ? javaType != null : !JAVA_TYPE_EDEFAULT.equals(javaType);
        case XmlPackage.TD_XML_ELEMENT_TYPE__XML_CONTENT:
            return xmlContent != null;
        }
        return super.eIsSet(featureID);
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
        result.append(" (javaType: ");
        result.append(javaType);
        result.append(')');
        return result.toString();
    }

} //TdXmlElementTypeImpl
