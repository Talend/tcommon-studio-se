/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.core.model.component_cache;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.talend.designer.core.model.utils.emf.component.IMPORTType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getOriginalFamilyName <em>Original Family Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getPluginExtension <em>Plugin Extension</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getVersion <em>Version</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getTranslatedFamilyName <em>Translated Family Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#isIsTechnical <em>Is Technical</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getPluginDependencies <em>Plugin Dependencies</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getComponentNames <em>Component Names</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getImportType <em>Import Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#isIsVisibleInComponentDefinition <em>Is Visible In Component Definition</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getUriString <em>Uri String</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getPathSource <em>Path Source</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getRepositoryType <em>Repository Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getSourceBundleName <em>Source Bundle Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getSha1 <em>Sha1</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getLongName <em>Long Name</em>}</li>
 *   <li>{@link org.talend.core.model.component_cache.ComponentInfo#getProviderClass <em>Provider Class</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo()
 * @model
 * @generated
 */
public interface ComponentInfo extends EObject {
    /**
     * Returns the value of the '<em><b>Original Family Name</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Original Family Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Original Family Name</em>' attribute.
     * @see #setOriginalFamilyName(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_OriginalFamilyName()
     * @model default=""
     * @generated
     */
    String getOriginalFamilyName();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getOriginalFamilyName <em>Original Family Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Original Family Name</em>' attribute.
     * @see #getOriginalFamilyName()
     * @generated
     */
    void setOriginalFamilyName(String value);

    /**
     * Returns the value of the '<em><b>Plugin Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Plugin Extension</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Plugin Extension</em>' attribute.
     * @see #setPluginExtension(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_PluginExtension()
     * @model
     * @generated
     */
    String getPluginExtension();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getPluginExtension <em>Plugin Extension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Plugin Extension</em>' attribute.
     * @see #getPluginExtension()
     * @generated
     */
    void setPluginExtension(String value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_Version()
     * @model
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Translated Family Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Translated Family Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Translated Family Name</em>' attribute.
     * @see #setTranslatedFamilyName(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_TranslatedFamilyName()
     * @model
     * @generated
     */
    String getTranslatedFamilyName();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getTranslatedFamilyName <em>Translated Family Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Translated Family Name</em>' attribute.
     * @see #getTranslatedFamilyName()
     * @generated
     */
    void setTranslatedFamilyName(String value);

    /**
     * Returns the value of the '<em><b>Is Technical</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Technical</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Technical</em>' attribute.
     * @see #setIsTechnical(boolean)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_IsTechnical()
     * @model default="false"
     * @generated
     */
    boolean isIsTechnical();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#isIsTechnical <em>Is Technical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Technical</em>' attribute.
     * @see #isIsTechnical()
     * @generated
     */
    void setIsTechnical(boolean value);

    /**
     * Returns the value of the '<em><b>Plugin Dependencies</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Plugin Dependencies</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Plugin Dependencies</em>' attribute list.
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_PluginDependencies()
     * @model
     * @generated
     */
    EList<String> getPluginDependencies();

    /**
     * Returns the value of the '<em><b>Component Names</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component Names</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component Names</em>' attribute list.
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_ComponentNames()
     * @model
     * @generated
     */
    EList<String> getComponentNames();

    /**
     * Returns the value of the '<em><b>Import Type</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.designer.core.model.utils.emf.component.IMPORTType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Import Type</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Import Type</em>' containment reference list.
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_ImportType()
     * @model containment="true"
     * @generated
     */
    EList<IMPORTType> getImportType();

    /**
     * Returns the value of the '<em><b>Is Visible In Component Definition</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Visible In Component Definition</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Visible In Component Definition</em>' attribute.
     * @see #setIsVisibleInComponentDefinition(boolean)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_IsVisibleInComponentDefinition()
     * @model default="true"
     * @generated
     */
    boolean isIsVisibleInComponentDefinition();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#isIsVisibleInComponentDefinition <em>Is Visible In Component Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Visible In Component Definition</em>' attribute.
     * @see #isIsVisibleInComponentDefinition()
     * @generated
     */
    void setIsVisibleInComponentDefinition(boolean value);

    /**
     * Returns the value of the '<em><b>Uri String</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uri String</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uri String</em>' attribute.
     * @see #setUriString(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_UriString()
     * @model
     * @generated
     */
    String getUriString();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getUriString <em>Uri String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uri String</em>' attribute.
     * @see #getUriString()
     * @generated
     */
    void setUriString(String value);

    /**
     * Returns the value of the '<em><b>Path Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Path Source</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Path Source</em>' attribute.
     * @see #setPathSource(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_PathSource()
     * @model
     * @generated
     */
    String getPathSource();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getPathSource <em>Path Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Path Source</em>' attribute.
     * @see #getPathSource()
     * @generated
     */
    void setPathSource(String value);

    /**
     * Returns the value of the '<em><b>Repository Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Repository Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Repository Type</em>' attribute.
     * @see #setRepositoryType(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_RepositoryType()
     * @model
     * @generated
     */
    String getRepositoryType();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getRepositoryType <em>Repository Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Repository Type</em>' attribute.
     * @see #getRepositoryType()
     * @generated
     */
    void setRepositoryType(String value);

    /**
     * Returns the value of the '<em><b>Source Bundle Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Bundle Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Bundle Name</em>' attribute.
     * @see #setSourceBundleName(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_SourceBundleName()
     * @model
     * @generated
     */
    String getSourceBundleName();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getSourceBundleName <em>Source Bundle Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Bundle Name</em>' attribute.
     * @see #getSourceBundleName()
     * @generated
     */
    void setSourceBundleName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_Type()
     * @model
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Sha1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sha1</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sha1</em>' attribute.
     * @see #setSha1(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_Sha1()
     * @model
     * @generated
     */
    String getSha1();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getSha1 <em>Sha1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sha1</em>' attribute.
     * @see #getSha1()
     * @generated
     */
    void setSha1(String value);

    /**
     * Returns the value of the '<em><b>Long Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Long Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Long Name</em>' attribute.
     * @see #setLongName(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_LongName()
     * @model
     * @generated
     */
    String getLongName();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getLongName <em>Long Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Long Name</em>' attribute.
     * @see #getLongName()
     * @generated
     */
    void setLongName(String value);

    /**
     * Returns the value of the '<em><b>Provider Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Provider Class</em>' attribute.
     * @see #setProviderClass(String)
     * @see org.talend.core.model.component_cache.ComponentCachePackage#getComponentInfo_ProviderClass()
     * @model
     * @generated
     */
    String getProviderClass();

    /**
     * Sets the value of the '{@link org.talend.core.model.component_cache.ComponentInfo#getProviderClass <em>Provider Class</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Provider Class</em>' attribute.
     * @see #getProviderClass()
     * @generated
     */
    void setProviderClass(String value);

} // ComponentInfo
