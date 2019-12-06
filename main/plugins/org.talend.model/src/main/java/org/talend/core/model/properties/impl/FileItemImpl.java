/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.impl;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.helper.ByteArrayResource;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>File Item</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.core.model.properties.impl.FileItemImpl#getName <em>Name</em>}</li>
 * <li>{@link org.talend.core.model.properties.impl.FileItemImpl#getExtension <em>Extension</em>}</li>
 * <li>{@link org.talend.core.model.properties.impl.FileItemImpl#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class FileItemImpl extends ItemImpl implements FileItem {

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getExtension() <em>Extension</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getExtension()
     * @generated
     * @ordered
     */
    protected static final String EXTENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getExtension() <em>Extension</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getExtension()
     * @generated
     * @ordered
     */
    protected String extension = EXTENSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getContent() <em>Content</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getContent()
     * @generated
     * @ordered
     */
    protected ByteArray content;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected FileItemImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.FILE_ITEM;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.FILE_ITEM__NAME, oldName, name));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getExtension() {
        return extension;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setExtension(String newExtension) {
        String oldExtension = extension;
        extension = newExtension;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.FILE_ITEM__EXTENSION, oldExtension,
                    extension));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ByteArray getContent() {
        if (content != null && content.eIsProxy()) {
            InternalEObject oldContent = (InternalEObject) content;
            content = (ByteArray) eResolveProxy(oldContent);
            if (content != oldContent) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.FILE_ITEM__CONTENT,
                            oldContent, content));
                }
            }
        }
        return content;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ByteArray basicGetContent() {
        return content;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setContent(ByteArray newContent) {
        ByteArray oldContent = content;
        content = newContent;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.FILE_ITEM__CONTENT, oldContent,
                    content));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.FILE_ITEM__NAME:
            return getName();
        case PropertiesPackage.FILE_ITEM__EXTENSION:
            return getExtension();
        case PropertiesPackage.FILE_ITEM__CONTENT:
            if (resolve) {
                return getContent();
            }
            return basicGetContent();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case PropertiesPackage.FILE_ITEM__NAME:
            setName((String) newValue);
            return;
        case PropertiesPackage.FILE_ITEM__EXTENSION:
            setExtension((String) newValue);
            return;
        case PropertiesPackage.FILE_ITEM__CONTENT:
            setContent((ByteArray) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case PropertiesPackage.FILE_ITEM__NAME:
            setName(NAME_EDEFAULT);
            return;
        case PropertiesPackage.FILE_ITEM__EXTENSION:
            setExtension(EXTENSION_EDEFAULT);
            return;
        case PropertiesPackage.FILE_ITEM__CONTENT:
            setContent((ByteArray) null);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case PropertiesPackage.FILE_ITEM__NAME:
            return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
        case PropertiesPackage.FILE_ITEM__EXTENSION:
            return EXTENSION_EDEFAULT == null ? extension != null : !EXTENSION_EDEFAULT.equals(extension);
        case PropertiesPackage.FILE_ITEM__CONTENT:
            return content != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(", extension: ");
        result.append(extension);
        result.append(')');
        return result.toString();
    }

    @Override
    public EObject eResolveProxy(InternalEObject proxy) {
        if (!proxy.eClass().equals(PropertiesPackage.eINSTANCE.getByteArray())) {
            return super.eResolveProxy(proxy);
        }

        URI proxyUri = proxy.eProxyURI();
        URI resourceUri = proxyUri.trimFragment();
        // bug 0020095
        if (eResource() == null) {
            return super.eResolveProxy(proxy);
        }
        ResourceSet resourceSet = eResource().getResourceSet();
        ByteArrayResource byteArrayResource = null;

        URIConverter theURIConverter = resourceSet.getURIConverter();
        URI normalizedURI = theURIConverter.normalize(resourceUri);
        // TUP-1814:because the routine proxyUri is File type,need handle with it.
        if ((proxyUri.isPlatform() && proxyUri.segmentCount() > 1 && "resource".equals(proxyUri.segment(0)))
                || proxyUri.isFile()) {
            List<Resource> resources = resourceSet.getResources();
            synchronized (resources) {
                for (Resource resource : resources) {
                    if (theURIConverter.normalize(resource.getURI()).equals(normalizedURI)) {
                        byteArrayResource = (ByteArrayResource) resource;
                        break;
                    }
                }
            }

            if (byteArrayResource != null && byteArrayResource.getContents().isEmpty()
                    && byteArrayResource.isLoaded()) {
                resources.remove(byteArrayResource);
                byteArrayResource = null;
            }

            if (byteArrayResource == null) {
                byteArrayResource = new ByteArrayResource(resourceUri);
                resourceSet.getResources().add(byteArrayResource);
            }

            try {
                byteArrayResource.load(null);
            } catch (IOException e) {
            }
        } else {
            List<Resource> resources = resourceSet.getResources();
            synchronized (resources) {
                for (Resource resource : resources) {
                    if (theURIConverter.normalize(resource.getURI()).equals(normalizedURI)) {
                        byteArrayResource = (ByteArrayResource) resource;
                        break;
                    }
                }
            }
        }

        if (byteArrayResource != null && byteArrayResource.isLoaded()) {
            EObject object = byteArrayResource.getEObject(proxyUri.fragment().toString());
            if (object != null) {
                return object;
            }
        }

        throw new UnsupportedOperationException();
    }

} // FileItemImpl
