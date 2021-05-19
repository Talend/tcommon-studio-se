/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.component_cache.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentInfo;

import org.talend.designer.core.model.utils.emf.component.IMPORTType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getOriginalFamilyName <em>Original Family Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getPluginExtension <em>Plugin Extension</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#isIsTechnical <em>Is Technical</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getPluginDependencies <em>Plugin Dependencies</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getComponentNames <em>Component Names</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getImportType <em>Import Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#isIsVisibleInComponentDefinition <em>Is Visible In Component Definition</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getUriString <em>Uri String</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getPathSource <em>Path Source</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getRepositoryType <em>Repository Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getSourceBundleName <em>Source Bundle Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.impl.ComponentInfoImpl#getProviderId <em>Provider Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentInfoImpl extends EObjectImpl implements ComponentInfo {
    /**
     * The default value of the '{@link #getOriginalFamilyName() <em>Original Family Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginalFamilyName()
     * @generated
     * @ordered
     */
    protected static final String ORIGINAL_FAMILY_NAME_EDEFAULT = "";

    /**
     * The cached value of the '{@link #getOriginalFamilyName() <em>Original Family Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginalFamilyName()
     * @generated
     * @ordered
     */
    protected String originalFamilyName = ORIGINAL_FAMILY_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getPluginExtension() <em>Plugin Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginExtension()
     * @generated
     * @ordered
     */
    protected static final String PLUGIN_EXTENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPluginExtension() <em>Plugin Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginExtension()
     * @generated
     * @ordered
     */
    protected String pluginExtension = PLUGIN_EXTENSION_EDEFAULT;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #isIsTechnical() <em>Is Technical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsTechnical()
     * @generated
     * @ordered
     */
    protected static final boolean IS_TECHNICAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsTechnical() <em>Is Technical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsTechnical()
     * @generated
     * @ordered
     */
    protected boolean isTechnical = IS_TECHNICAL_EDEFAULT;

    /**
     * The cached value of the '{@link #getPluginDependencies() <em>Plugin Dependencies</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginDependencies()
     * @generated
     * @ordered
     */
    protected EList<String> pluginDependencies;

    /**
     * The cached value of the '{@link #getComponentNames() <em>Component Names</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentNames()
     * @generated
     * @ordered
     */
    protected EList<String> componentNames;

    /**
     * The cached value of the '{@link #getImportType() <em>Import Type</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImportType()
     * @generated
     * @ordered
     */
    protected EList<IMPORTType> importType;

    /**
     * The default value of the '{@link #isIsVisibleInComponentDefinition() <em>Is Visible In Component Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsVisibleInComponentDefinition()
     * @generated
     * @ordered
     */
    protected static final boolean IS_VISIBLE_IN_COMPONENT_DEFINITION_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isIsVisibleInComponentDefinition() <em>Is Visible In Component Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsVisibleInComponentDefinition()
     * @generated
     * @ordered
     */
    protected boolean isVisibleInComponentDefinition = IS_VISIBLE_IN_COMPONENT_DEFINITION_EDEFAULT;

    /**
     * The default value of the '{@link #getUriString() <em>Uri String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUriString()
     * @generated
     * @ordered
     */
    protected static final String URI_STRING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUriString() <em>Uri String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUriString()
     * @generated
     * @ordered
     */
    protected String uriString = URI_STRING_EDEFAULT;

    /**
     * The default value of the '{@link #getPathSource() <em>Path Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPathSource()
     * @generated
     * @ordered
     */
    protected static final String PATH_SOURCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPathSource() <em>Path Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPathSource()
     * @generated
     * @ordered
     */
    protected String pathSource = PATH_SOURCE_EDEFAULT;

    /**
     * The default value of the '{@link #getRepositoryType() <em>Repository Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepositoryType()
     * @generated
     * @ordered
     */
    protected static final String REPOSITORY_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRepositoryType() <em>Repository Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepositoryType()
     * @generated
     * @ordered
     */
    protected String repositoryType = REPOSITORY_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceBundleName() <em>Source Bundle Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceBundleName()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_BUNDLE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceBundleName() <em>Source Bundle Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceBundleName()
     * @generated
     * @ordered
     */
    protected String sourceBundleName = SOURCE_BUNDLE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getProviderId() <em>Provider Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProviderId()
     * @generated
     * @ordered
     */
    protected static final String PROVIDER_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProviderId() <em>Provider Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProviderId()
     * @generated
     * @ordered
     */
    protected String providerId = PROVIDER_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComponentInfoImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ComponentCachePackage.Literals.COMPONENT_INFO;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOriginalFamilyName() {
        return originalFamilyName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOriginalFamilyName(String newOriginalFamilyName) {
        String oldOriginalFamilyName = originalFamilyName;
        originalFamilyName = newOriginalFamilyName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__ORIGINAL_FAMILY_NAME, oldOriginalFamilyName, originalFamilyName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPluginExtension() {
        return pluginExtension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPluginExtension(String newPluginExtension) {
        String oldPluginExtension = pluginExtension;
        pluginExtension = newPluginExtension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__PLUGIN_EXTENSION, oldPluginExtension, pluginExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsTechnical() {
        return isTechnical;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsTechnical(boolean newIsTechnical) {
        boolean oldIsTechnical = isTechnical;
        isTechnical = newIsTechnical;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__IS_TECHNICAL, oldIsTechnical, isTechnical));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getPluginDependencies() {
        if (pluginDependencies == null) {
            pluginDependencies = new EDataTypeUniqueEList<String>(String.class, this, ComponentCachePackage.COMPONENT_INFO__PLUGIN_DEPENDENCIES);
        }
        return pluginDependencies;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getComponentNames() {
        if (componentNames == null) {
            componentNames = new EDataTypeUniqueEList<String>(String.class, this, ComponentCachePackage.COMPONENT_INFO__COMPONENT_NAMES);
        }
        return componentNames;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IMPORTType> getImportType() {
        if (importType == null) {
            importType = new EObjectContainmentEList<IMPORTType>(IMPORTType.class, this, ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE);
        }
        return importType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsVisibleInComponentDefinition() {
        return isVisibleInComponentDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsVisibleInComponentDefinition(boolean newIsVisibleInComponentDefinition) {
        boolean oldIsVisibleInComponentDefinition = isVisibleInComponentDefinition;
        isVisibleInComponentDefinition = newIsVisibleInComponentDefinition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION, oldIsVisibleInComponentDefinition, isVisibleInComponentDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUriString() {
        return uriString;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUriString(String newUriString) {
        String oldUriString = uriString;
        uriString = newUriString;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__URI_STRING, oldUriString, uriString));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPathSource() {
        return pathSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPathSource(String newPathSource) {
        String oldPathSource = pathSource;
        pathSource = newPathSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__PATH_SOURCE, oldPathSource, pathSource));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRepositoryType() {
        return repositoryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRepositoryType(String newRepositoryType) {
        String oldRepositoryType = repositoryType;
        repositoryType = newRepositoryType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__REPOSITORY_TYPE, oldRepositoryType, repositoryType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSourceBundleName() {
        return sourceBundleName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceBundleName(String newSourceBundleName) {
        String oldSourceBundleName = sourceBundleName;
        sourceBundleName = newSourceBundleName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__SOURCE_BUNDLE_NAME, oldSourceBundleName, sourceBundleName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProviderId(String newProviderId) {
        String oldProviderId = providerId;
        providerId = newProviderId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentCachePackage.COMPONENT_INFO__PROVIDER_ID, oldProviderId, providerId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE:
                return ((InternalEList<?>)getImportType()).basicRemove(otherEnd, msgs);
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
            case ComponentCachePackage.COMPONENT_INFO__ORIGINAL_FAMILY_NAME:
                return getOriginalFamilyName();
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_EXTENSION:
                return getPluginExtension();
            case ComponentCachePackage.COMPONENT_INFO__VERSION:
                return getVersion();
            case ComponentCachePackage.COMPONENT_INFO__IS_TECHNICAL:
                return isIsTechnical();
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_DEPENDENCIES:
                return getPluginDependencies();
            case ComponentCachePackage.COMPONENT_INFO__COMPONENT_NAMES:
                return getComponentNames();
            case ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE:
                return getImportType();
            case ComponentCachePackage.COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION:
                return isIsVisibleInComponentDefinition();
            case ComponentCachePackage.COMPONENT_INFO__URI_STRING:
                return getUriString();
            case ComponentCachePackage.COMPONENT_INFO__PATH_SOURCE:
                return getPathSource();
            case ComponentCachePackage.COMPONENT_INFO__REPOSITORY_TYPE:
                return getRepositoryType();
            case ComponentCachePackage.COMPONENT_INFO__SOURCE_BUNDLE_NAME:
                return getSourceBundleName();
            case ComponentCachePackage.COMPONENT_INFO__TYPE:
                return getType();
            case ComponentCachePackage.COMPONENT_INFO__PROVIDER_ID:
                return getProviderId();
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
            case ComponentCachePackage.COMPONENT_INFO__ORIGINAL_FAMILY_NAME:
                setOriginalFamilyName((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_EXTENSION:
                setPluginExtension((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__VERSION:
                setVersion((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__IS_TECHNICAL:
                setIsTechnical((Boolean)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_DEPENDENCIES:
                getPluginDependencies().clear();
                getPluginDependencies().addAll((Collection<? extends String>)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__COMPONENT_NAMES:
                getComponentNames().clear();
                getComponentNames().addAll((Collection<? extends String>)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE:
                getImportType().clear();
                getImportType().addAll((Collection<? extends IMPORTType>)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION:
                setIsVisibleInComponentDefinition((Boolean)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__URI_STRING:
                setUriString((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PATH_SOURCE:
                setPathSource((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__REPOSITORY_TYPE:
                setRepositoryType((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__SOURCE_BUNDLE_NAME:
                setSourceBundleName((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__TYPE:
                setType((String)newValue);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PROVIDER_ID:
                setProviderId((String)newValue);
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
            case ComponentCachePackage.COMPONENT_INFO__ORIGINAL_FAMILY_NAME:
                setOriginalFamilyName(ORIGINAL_FAMILY_NAME_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_EXTENSION:
                setPluginExtension(PLUGIN_EXTENSION_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__IS_TECHNICAL:
                setIsTechnical(IS_TECHNICAL_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_DEPENDENCIES:
                getPluginDependencies().clear();
                return;
            case ComponentCachePackage.COMPONENT_INFO__COMPONENT_NAMES:
                getComponentNames().clear();
                return;
            case ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE:
                getImportType().clear();
                return;
            case ComponentCachePackage.COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION:
                setIsVisibleInComponentDefinition(IS_VISIBLE_IN_COMPONENT_DEFINITION_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__URI_STRING:
                setUriString(URI_STRING_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PATH_SOURCE:
                setPathSource(PATH_SOURCE_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__REPOSITORY_TYPE:
                setRepositoryType(REPOSITORY_TYPE_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__SOURCE_BUNDLE_NAME:
                setSourceBundleName(SOURCE_BUNDLE_NAME_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case ComponentCachePackage.COMPONENT_INFO__PROVIDER_ID:
                setProviderId(PROVIDER_ID_EDEFAULT);
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
            case ComponentCachePackage.COMPONENT_INFO__ORIGINAL_FAMILY_NAME:
                return ORIGINAL_FAMILY_NAME_EDEFAULT == null ? originalFamilyName != null : !ORIGINAL_FAMILY_NAME_EDEFAULT.equals(originalFamilyName);
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_EXTENSION:
                return PLUGIN_EXTENSION_EDEFAULT == null ? pluginExtension != null : !PLUGIN_EXTENSION_EDEFAULT.equals(pluginExtension);
            case ComponentCachePackage.COMPONENT_INFO__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case ComponentCachePackage.COMPONENT_INFO__IS_TECHNICAL:
                return isTechnical != IS_TECHNICAL_EDEFAULT;
            case ComponentCachePackage.COMPONENT_INFO__PLUGIN_DEPENDENCIES:
                return pluginDependencies != null && !pluginDependencies.isEmpty();
            case ComponentCachePackage.COMPONENT_INFO__COMPONENT_NAMES:
                return componentNames != null && !componentNames.isEmpty();
            case ComponentCachePackage.COMPONENT_INFO__IMPORT_TYPE:
                return importType != null && !importType.isEmpty();
            case ComponentCachePackage.COMPONENT_INFO__IS_VISIBLE_IN_COMPONENT_DEFINITION:
                return isVisibleInComponentDefinition != IS_VISIBLE_IN_COMPONENT_DEFINITION_EDEFAULT;
            case ComponentCachePackage.COMPONENT_INFO__URI_STRING:
                return URI_STRING_EDEFAULT == null ? uriString != null : !URI_STRING_EDEFAULT.equals(uriString);
            case ComponentCachePackage.COMPONENT_INFO__PATH_SOURCE:
                return PATH_SOURCE_EDEFAULT == null ? pathSource != null : !PATH_SOURCE_EDEFAULT.equals(pathSource);
            case ComponentCachePackage.COMPONENT_INFO__REPOSITORY_TYPE:
                return REPOSITORY_TYPE_EDEFAULT == null ? repositoryType != null : !REPOSITORY_TYPE_EDEFAULT.equals(repositoryType);
            case ComponentCachePackage.COMPONENT_INFO__SOURCE_BUNDLE_NAME:
                return SOURCE_BUNDLE_NAME_EDEFAULT == null ? sourceBundleName != null : !SOURCE_BUNDLE_NAME_EDEFAULT.equals(sourceBundleName);
            case ComponentCachePackage.COMPONENT_INFO__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case ComponentCachePackage.COMPONENT_INFO__PROVIDER_ID:
                return PROVIDER_ID_EDEFAULT == null ? providerId != null : !PROVIDER_ID_EDEFAULT.equals(providerId);
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
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (originalFamilyName: ");
        result.append(originalFamilyName);
        result.append(", pluginExtension: ");
        result.append(pluginExtension);
        result.append(", version: ");
        result.append(version);
        result.append(", isTechnical: ");
        result.append(isTechnical);
        result.append(", PluginDependencies: ");
        result.append(pluginDependencies);
        result.append(", componentNames: ");
        result.append(componentNames);
        result.append(", isVisibleInComponentDefinition: ");
        result.append(isVisibleInComponentDefinition);
        result.append(", uriString: ");
        result.append(uriString);
        result.append(", pathSource: ");
        result.append(pathSource);
        result.append(", repositoryType: ");
        result.append(repositoryType);
        result.append(", sourceBundleName: ");
        result.append(sourceBundleName);
        result.append(", type: ");
        result.append(type);
        result.append(", providerId: ");
        result.append(providerId);
        result.append(')');
        return result.toString();
    }

} //ComponentInfoImpl
