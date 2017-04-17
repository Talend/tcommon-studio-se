/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.impl;

import java.util.Collection;
import java.util.Date;

import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.model.properties.CustomComponentSetting;
import org.talend.core.model.properties.ExchangeUser;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectComponentAuthorisation;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.UserProjectAuthorization;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Project</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getTechnicalStatus <em>Technical Status</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getDocumentationStatus <em>Documentation Status</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getTechnicalLabel <em>Technical Label</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#isLocal <em>Local</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getFolders <em>Folders</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#isDeleted <em>Deleted</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getDeleteDate <em>Delete Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getCreationDate <em>Creation Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getUserAuthorization <em>User Authorization</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getAllowedComponents <em>Allowed Components</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getReferencedProjects <em>Referenced Projects</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getAvailableRefProject <em>Available Ref Project</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getMasterJobId <em>Master Job Id</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getSpagoBiServer <em>Spago Bi Server</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getProductVersion <em>Product Version</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getComponentsSettings <em>Components Settings</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getUrl <em>Url</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getStatAndLogsSettings <em>Stat And Logs Settings</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getImplicitContextSettings <em>Implicit Context Settings</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#isHidePassword <em>Hide Password</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getItemsRelations <em>Items Relations</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#isReference <em>Reference</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getCustomComponentSettings <em>Custom Component Settings</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getDeletedFolders <em>Deleted Folders</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getItemsRelationVersion <em>Items Relation Version</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getExchangeUser <em>Exchange User</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getMigrationTasks <em>Migration Tasks</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#getMigrationTask <em>Migration Task</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.ProjectImpl#isBigData <em>Big Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProjectImpl extends EObjectImpl implements Project {

    /**
	 * The cached value of the '{@link #getTechnicalStatus() <em>Technical Status</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTechnicalStatus()
	 * @generated
	 * @ordered
	 */
    protected EList technicalStatus;

    /**
	 * The cached value of the '{@link #getDocumentationStatus() <em>Documentation Status</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDocumentationStatus()
	 * @generated
	 * @ordered
	 */
    protected EList documentationStatus;

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final int ID_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected int id = ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
    protected static final String LABEL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
    protected String label = LABEL_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
    protected String language = LANGUAGE_EDEFAULT;

    /**
	 * The default value of the '{@link #getTechnicalLabel() <em>Technical Label</em>}' attribute.
	 * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
	 * @see #getTechnicalLabel()
	 * @generated
	 * @ordered
	 */
    protected static final String TECHNICAL_LABEL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTechnicalLabel() <em>Technical Label</em>}' attribute.
	 * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
	 * @see #getTechnicalLabel()
	 * @generated
	 * @ordered
	 */
    protected String technicalLabel = TECHNICAL_LABEL_EDEFAULT;

    /**
	 * The default value of the '{@link #isLocal() <em>Local</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #isLocal()
	 * @generated
	 * @ordered
	 */
    protected static final boolean LOCAL_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isLocal() <em>Local</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #isLocal()
	 * @generated
	 * @ordered
	 */
    protected boolean local = LOCAL_EDEFAULT;

    /**
     * The cached value of the '{@link #getFolders() <em>Folders</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFolders()
     * @generated
     * @ordered
     */
    protected EList folders;

    /**
	 * The default value of the '{@link #isDeleted() <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #isDeleted()
	 * @generated
	 * @ordered
	 */
    protected static final boolean DELETED_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isDeleted() <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #isDeleted()
	 * @generated
	 * @ordered
	 */
    protected boolean deleted = DELETED_EDEFAULT;

    /**
	 * The default value of the '{@link #getDeleteDate() <em>Delete Date</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDeleteDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date DELETE_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDeleteDate() <em>Delete Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getDeleteDate()
	 * @generated
	 * @ordered
	 */
    protected Date deleteDate = DELETE_DATE_EDEFAULT;

    /**
	 * The default value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date CREATION_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
    protected Date creationDate = CREATION_DATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' reference.
	 * <!-- begin-user-doc --> <!--
     * end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
    protected User author;

    /**
     * The cached value of the '{@link #getUserAuthorization() <em>User Authorization</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getUserAuthorization()
     * @generated
     * @ordered
     */
    protected EList userAuthorization;

    /**
     * The cached value of the '{@link #getAllowedComponents() <em>Allowed Components</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAllowedComponents()
     * @generated
     * @ordered
     */
    protected EList allowedComponents;

    /**
     * The cached value of the '{@link #getReferencedProjects() <em>Referenced Projects</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getReferencedProjects()
     * @generated
     * @ordered
     */
    protected EList referencedProjects;

    /**
     * The cached value of the '{@link #getAvailableRefProject() <em>Available Ref Project</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAvailableRefProject()
     * @generated
     * @ordered
     */
    protected EList availableRefProject;

    /**
	 * The default value of the '{@link #getMasterJobId() <em>Master Job Id</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMasterJobId()
	 * @generated
	 * @ordered
	 */
    protected static final String MASTER_JOB_ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMasterJobId() <em>Master Job Id</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMasterJobId()
	 * @generated
	 * @ordered
	 */
    protected String masterJobId = MASTER_JOB_ID_EDEFAULT;

    /**
     * The cached value of the '{@link #getSpagoBiServer() <em>Spago Bi Server</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSpagoBiServer()
     * @generated
     * @ordered
     */
    protected EList spagoBiServer;

    /**
	 * The default value of the '{@link #getProductVersion() <em>Product Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getProductVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String PRODUCT_VERSION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getProductVersion() <em>Product Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getProductVersion()
	 * @generated
	 * @ordered
	 */
    protected String productVersion = PRODUCT_VERSION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getComponentsSettings() <em>Components Settings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getComponentsSettings()
	 * @generated
	 * @ordered
	 */
    protected EList componentsSettings;

    /**
	 * The default value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
    protected static final String URL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
    protected String url = URL_EDEFAULT;

    /**
	 * The cached value of the '{@link #getStatAndLogsSettings() <em>Stat And Logs Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getStatAndLogsSettings()
	 * @generated
	 * @ordered
	 */
    protected StatAndLogsSettings statAndLogsSettings;

    /**
	 * The cached value of the '{@link #getImplicitContextSettings() <em>Implicit Context Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getImplicitContextSettings()
	 * @generated
	 * @ordered
	 */
    protected ImplicitContextSettings implicitContextSettings;

    /**
	 * The default value of the '{@link #isHidePassword() <em>Hide Password</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isHidePassword()
	 * @generated
	 * @ordered
	 */
    protected static final boolean HIDE_PASSWORD_EDEFAULT = true;

    /**
	 * The cached value of the '{@link #isHidePassword() <em>Hide Password</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isHidePassword()
	 * @generated
	 * @ordered
	 */
    protected boolean hidePassword = HIDE_PASSWORD_EDEFAULT;

    /**
	 * The cached value of the '{@link #getItemsRelations() <em>Items Relations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getItemsRelations()
	 * @generated
	 * @ordered
	 */
    protected EList itemsRelations;

    /**
	 * The default value of the '{@link #isReference() <em>Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isReference()
	 * @generated
	 * @ordered
	 */
    protected static final boolean REFERENCE_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isReference() <em>Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isReference()
	 * @generated
	 * @ordered
	 */
    protected boolean reference = REFERENCE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getCustomComponentSettings() <em>Custom Component Settings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCustomComponentSettings()
	 * @generated
	 * @ordered
	 */
    protected EList customComponentSettings;

    /**
	 * The cached value of the '{@link #getDeletedFolders() <em>Deleted Folders</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDeletedFolders()
	 * @generated
	 * @ordered
	 */
    protected EList deletedFolders;

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
	 * The default value of the '{@link #getItemsRelationVersion() <em>Items Relation Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getItemsRelationVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String ITEMS_RELATION_VERSION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getItemsRelationVersion() <em>Items Relation Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getItemsRelationVersion()
	 * @generated
	 * @ordered
	 */
    protected String itemsRelationVersion = ITEMS_RELATION_VERSION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getExchangeUser() <em>Exchange User</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getExchangeUser()
	 * @generated
	 * @ordered
	 */
    protected ExchangeUser exchangeUser;

    /**
     * The cached value of the '{@link #getMigrationTasks() <em>Migration Tasks</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMigrationTasks()
     * @generated
     * @ordered
     */
    protected EList migrationTasks;

    /**
	 * The cached value of the '{@link #getMigrationTask() <em>Migration Task</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMigrationTask()
	 * @generated
	 * @ordered
	 */
    protected EList migrationTask;

    /**
	 * The default value of the '{@link #isBigData() <em>Big Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBigData()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BIG_DATA_EDEFAULT = true;

				/**
	 * The cached value of the '{@link #isBigData() <em>Big Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBigData()
	 * @generated
	 * @ordered
	 */
	protected boolean bigData = BIG_DATA_EDEFAULT;

				/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected ProjectImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return PropertiesPackage.Literals.PROJECT;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getTechnicalStatus() {
		if (technicalStatus == null) {
			technicalStatus = new EObjectContainmentEList(Status.class, this, PropertiesPackage.PROJECT__TECHNICAL_STATUS);
		}
		return technicalStatus;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getDocumentationStatus() {
		if (documentationStatus == null) {
			documentationStatus = new EObjectContainmentEList(Status.class, this, PropertiesPackage.PROJECT__DOCUMENTATION_STATUS);
		}
		return documentationStatus;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public int getId() {
		return id;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(int newId) {
		int oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__ID, oldId, id));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getLabel() {
		return label;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__LABEL, oldLabel, label));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
		return description;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__DESCRIPTION, oldDescription, description));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getLanguage() {
		return language;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setLanguage(String newLanguage) {
		String oldLanguage = language;
		language = newLanguage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__LANGUAGE, oldLanguage, language));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public User getAuthor() {
		if (author != null && author.eIsProxy()) {
			InternalEObject oldAuthor = (InternalEObject)author;
			author = (User)eResolveProxy(oldAuthor);
			if (author != oldAuthor) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.PROJECT__AUTHOR, oldAuthor, author));
			}
		}
		return author;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public User basicGetAuthor() {
		return author;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setAuthor(User newAuthor) {
		User oldAuthor = author;
		author = newAuthor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__AUTHOR, oldAuthor, author));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getUserAuthorization() {
		if (userAuthorization == null) {
			userAuthorization = new EObjectWithInverseResolvingEList(UserProjectAuthorization.class, this, PropertiesPackage.PROJECT__USER_AUTHORIZATION, PropertiesPackage.USER_PROJECT_AUTHORIZATION__PROJECT);
		}
		return userAuthorization;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAllowedComponents() {
		if (allowedComponents == null) {
			allowedComponents = new EObjectWithInverseResolvingEList(ProjectComponentAuthorisation.class, this, PropertiesPackage.PROJECT__ALLOWED_COMPONENTS, PropertiesPackage.PROJECT_COMPONENT_AUTHORISATION__PROJECT);
		}
		return allowedComponents;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getReferencedProjects() {
		if (referencedProjects == null) {
			referencedProjects = new EObjectWithInverseResolvingEList(ProjectReference.class, this, PropertiesPackage.PROJECT__REFERENCED_PROJECTS, PropertiesPackage.PROJECT_REFERENCE__PROJECT);
		}
		return referencedProjects;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAvailableRefProject() {
		if (availableRefProject == null) {
			availableRefProject = new EObjectWithInverseResolvingEList(ProjectReference.class, this, PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT, PropertiesPackage.PROJECT_REFERENCE__REFERENCED_PROJECT);
		}
		return availableRefProject;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getMigrationTasks() {
		if (migrationTasks == null) {
			migrationTasks = new EDataTypeUniqueEList(String.class, this, PropertiesPackage.PROJECT__MIGRATION_TASKS);
		}
		return migrationTasks;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getMigrationTask() {
		if (migrationTask == null) {
			migrationTask = new EObjectContainmentEList(MigrationTask.class, this, PropertiesPackage.PROJECT__MIGRATION_TASK);
		}
		return migrationTask;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isBigData() {
		return bigData;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBigData(boolean newBigData) {
		boolean oldBigData = bigData;
		bigData = newBigData;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__BIG_DATA, oldBigData, bigData));
	}

				/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getMasterJobId() {
		return masterJobId;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setMasterJobId(String newMasterJobId) {
		String oldMasterJobId = masterJobId;
		masterJobId = newMasterJobId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__MASTER_JOB_ID, oldMasterJobId, masterJobId));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getSpagoBiServer() {
		if (spagoBiServer == null) {
			spagoBiServer = new EObjectContainmentEList(SpagoBiServer.class, this, PropertiesPackage.PROJECT__SPAGO_BI_SERVER);
		}
		return spagoBiServer;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getProductVersion() {
		return productVersion;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProductVersion(String newProductVersion) {
		String oldProductVersion = productVersion;
		productVersion = newProductVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__PRODUCT_VERSION, oldProductVersion, productVersion));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getComponentsSettings() {
		if (componentsSettings == null) {
			componentsSettings = new EObjectContainmentEList(ComponentSetting.class, this, PropertiesPackage.PROJECT__COMPONENTS_SETTINGS);
		}
		return componentsSettings;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getUrl() {
		return url;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUrl(String newUrl) {
		String oldUrl = url;
		url = newUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__URL, oldUrl, url));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public StatAndLogsSettings getStatAndLogsSettings() {
		return statAndLogsSettings;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetStatAndLogsSettings(StatAndLogsSettings newStatAndLogsSettings, NotificationChain msgs) {
		StatAndLogsSettings oldStatAndLogsSettings = statAndLogsSettings;
		statAndLogsSettings = newStatAndLogsSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS, oldStatAndLogsSettings, newStatAndLogsSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatAndLogsSettings(StatAndLogsSettings newStatAndLogsSettings) {
		if (newStatAndLogsSettings != statAndLogsSettings) {
			NotificationChain msgs = null;
			if (statAndLogsSettings != null)
				msgs = ((InternalEObject)statAndLogsSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS, null, msgs);
			if (newStatAndLogsSettings != null)
				msgs = ((InternalEObject)newStatAndLogsSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS, null, msgs);
			msgs = basicSetStatAndLogsSettings(newStatAndLogsSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS, newStatAndLogsSettings, newStatAndLogsSettings));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ImplicitContextSettings getImplicitContextSettings() {
		return implicitContextSettings;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetImplicitContextSettings(ImplicitContextSettings newImplicitContextSettings, NotificationChain msgs) {
		ImplicitContextSettings oldImplicitContextSettings = implicitContextSettings;
		implicitContextSettings = newImplicitContextSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS, oldImplicitContextSettings, newImplicitContextSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setImplicitContextSettings(ImplicitContextSettings newImplicitContextSettings) {
		if (newImplicitContextSettings != implicitContextSettings) {
			NotificationChain msgs = null;
			if (implicitContextSettings != null)
				msgs = ((InternalEObject)implicitContextSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS, null, msgs);
			if (newImplicitContextSettings != null)
				msgs = ((InternalEObject)newImplicitContextSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS, null, msgs);
			msgs = basicSetImplicitContextSettings(newImplicitContextSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS, newImplicitContextSettings, newImplicitContextSettings));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isHidePassword() {
		return hidePassword;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHidePassword(boolean newHidePassword) {
		boolean oldHidePassword = hidePassword;
		hidePassword = newHidePassword;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__HIDE_PASSWORD, oldHidePassword, hidePassword));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getItemsRelations() {
		if (itemsRelations == null) {
			itemsRelations = new EObjectContainmentEList(ItemRelations.class, this, PropertiesPackage.PROJECT__ITEMS_RELATIONS);
		}
		return itemsRelations;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isReference() {
		return reference;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReference(boolean newReference) {
		boolean oldReference = reference;
		reference = newReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__REFERENCE, oldReference, reference));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getCustomComponentSettings() {
		if (customComponentSettings == null) {
			customComponentSettings = new EObjectContainmentEList(CustomComponentSetting.class, this, PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS);
		}
		return customComponentSettings;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getDeletedFolders() {
		if (deletedFolders == null) {
			deletedFolders = new EDataTypeUniqueEList(String.class, this, PropertiesPackage.PROJECT__DELETED_FOLDERS);
		}
		return deletedFolders;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__TYPE, oldType, type));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getItemsRelationVersion() {
		return itemsRelationVersion;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setItemsRelationVersion(String newItemsRelationVersion) {
		String oldItemsRelationVersion = itemsRelationVersion;
		itemsRelationVersion = newItemsRelationVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__ITEMS_RELATION_VERSION, oldItemsRelationVersion, itemsRelationVersion));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ExchangeUser getExchangeUser() {
		if (exchangeUser != null && exchangeUser.eIsProxy()) {
			InternalEObject oldExchangeUser = (InternalEObject)exchangeUser;
			exchangeUser = (ExchangeUser)eResolveProxy(oldExchangeUser);
			if (exchangeUser != oldExchangeUser) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.PROJECT__EXCHANGE_USER, oldExchangeUser, exchangeUser));
			}
		}
		return exchangeUser;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ExchangeUser basicGetExchangeUser() {
		return exchangeUser;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExchangeUser(ExchangeUser newExchangeUser) {
		ExchangeUser oldExchangeUser = exchangeUser;
		exchangeUser = newExchangeUser;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__EXCHANGE_USER, oldExchangeUser, exchangeUser));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getTechnicalLabel() {
		return technicalLabel;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setTechnicalLabel(String newTechnicalLabel) {
		String oldTechnicalLabel = technicalLabel;
		technicalLabel = newTechnicalLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__TECHNICAL_LABEL, oldTechnicalLabel, technicalLabel));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isLocal() {
		return local;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setLocal(boolean newLocal) {
		boolean oldLocal = local;
		local = newLocal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__LOCAL, oldLocal, local));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList getFolders() {
		if (folders == null) {
			folders = new EObjectResolvingEList(FolderItem.class, this, PropertiesPackage.PROJECT__FOLDERS);
		}
		return folders;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isDeleted() {
		return deleted;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setDeleted(boolean newDeleted) {
		boolean oldDeleted = deleted;
		deleted = newDeleted;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__DELETED, oldDeleted, deleted));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Date getDeleteDate() {
		return deleteDate;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setDeleteDate(Date newDeleteDate) {
		Date oldDeleteDate = deleteDate;
		deleteDate = newDeleteDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__DELETE_DATE, oldDeleteDate, deleteDate));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Date getCreationDate() {
		return creationDate;
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setCreationDate(Date newCreationDate) {
		Date oldCreationDate = creationDate;
		creationDate = newCreationDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.PROJECT__CREATION_DATE, oldCreationDate, creationDate));
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				return ((InternalEList)getUserAuthorization()).basicAdd(otherEnd, msgs);
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				return ((InternalEList)getAllowedComponents()).basicAdd(otherEnd, msgs);
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				return ((InternalEList)getReferencedProjects()).basicAdd(otherEnd, msgs);
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				return ((InternalEList)getAvailableRefProject()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__TECHNICAL_STATUS:
				return ((InternalEList)getTechnicalStatus()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__DOCUMENTATION_STATUS:
				return ((InternalEList)getDocumentationStatus()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				return ((InternalEList)getUserAuthorization()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				return ((InternalEList)getAllowedComponents()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				return ((InternalEList)getReferencedProjects()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				return ((InternalEList)getAvailableRefProject()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__SPAGO_BI_SERVER:
				return ((InternalEList)getSpagoBiServer()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__COMPONENTS_SETTINGS:
				return ((InternalEList)getComponentsSettings()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS:
				return basicSetStatAndLogsSettings(null, msgs);
			case PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS:
				return basicSetImplicitContextSettings(null, msgs);
			case PropertiesPackage.PROJECT__ITEMS_RELATIONS:
				return ((InternalEList)getItemsRelations()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS:
				return ((InternalEList)getCustomComponentSettings()).basicRemove(otherEnd, msgs);
			case PropertiesPackage.PROJECT__MIGRATION_TASK:
				return ((InternalEList)getMigrationTask()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__TECHNICAL_STATUS:
				return getTechnicalStatus();
			case PropertiesPackage.PROJECT__DOCUMENTATION_STATUS:
				return getDocumentationStatus();
			case PropertiesPackage.PROJECT__ID:
				return new Integer(getId());
			case PropertiesPackage.PROJECT__LABEL:
				return getLabel();
			case PropertiesPackage.PROJECT__DESCRIPTION:
				return getDescription();
			case PropertiesPackage.PROJECT__LANGUAGE:
				return getLanguage();
			case PropertiesPackage.PROJECT__TECHNICAL_LABEL:
				return getTechnicalLabel();
			case PropertiesPackage.PROJECT__LOCAL:
				return isLocal() ? Boolean.TRUE : Boolean.FALSE;
			case PropertiesPackage.PROJECT__FOLDERS:
				return getFolders();
			case PropertiesPackage.PROJECT__DELETED:
				return isDeleted() ? Boolean.TRUE : Boolean.FALSE;
			case PropertiesPackage.PROJECT__DELETE_DATE:
				return getDeleteDate();
			case PropertiesPackage.PROJECT__CREATION_DATE:
				return getCreationDate();
			case PropertiesPackage.PROJECT__AUTHOR:
				if (resolve) return getAuthor();
				return basicGetAuthor();
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				return getUserAuthorization();
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				return getAllowedComponents();
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				return getReferencedProjects();
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				return getAvailableRefProject();
			case PropertiesPackage.PROJECT__MASTER_JOB_ID:
				return getMasterJobId();
			case PropertiesPackage.PROJECT__SPAGO_BI_SERVER:
				return getSpagoBiServer();
			case PropertiesPackage.PROJECT__PRODUCT_VERSION:
				return getProductVersion();
			case PropertiesPackage.PROJECT__COMPONENTS_SETTINGS:
				return getComponentsSettings();
			case PropertiesPackage.PROJECT__URL:
				return getUrl();
			case PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS:
				return getStatAndLogsSettings();
			case PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS:
				return getImplicitContextSettings();
			case PropertiesPackage.PROJECT__HIDE_PASSWORD:
				return isHidePassword() ? Boolean.TRUE : Boolean.FALSE;
			case PropertiesPackage.PROJECT__ITEMS_RELATIONS:
				return getItemsRelations();
			case PropertiesPackage.PROJECT__REFERENCE:
				return isReference() ? Boolean.TRUE : Boolean.FALSE;
			case PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS:
				return getCustomComponentSettings();
			case PropertiesPackage.PROJECT__DELETED_FOLDERS:
				return getDeletedFolders();
			case PropertiesPackage.PROJECT__TYPE:
				return getType();
			case PropertiesPackage.PROJECT__ITEMS_RELATION_VERSION:
				return getItemsRelationVersion();
			case PropertiesPackage.PROJECT__EXCHANGE_USER:
				if (resolve) return getExchangeUser();
				return basicGetExchangeUser();
			case PropertiesPackage.PROJECT__MIGRATION_TASKS:
				return getMigrationTasks();
			case PropertiesPackage.PROJECT__MIGRATION_TASK:
				return getMigrationTask();
			case PropertiesPackage.PROJECT__BIG_DATA:
				return isBigData() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__TECHNICAL_STATUS:
				getTechnicalStatus().clear();
				getTechnicalStatus().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__DOCUMENTATION_STATUS:
				getDocumentationStatus().clear();
				getDocumentationStatus().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__ID:
				setId(((Integer)newValue).intValue());
				return;
			case PropertiesPackage.PROJECT__LABEL:
				setLabel((String)newValue);
				return;
			case PropertiesPackage.PROJECT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case PropertiesPackage.PROJECT__LANGUAGE:
				setLanguage((String)newValue);
				return;
			case PropertiesPackage.PROJECT__TECHNICAL_LABEL:
				setTechnicalLabel((String)newValue);
				return;
			case PropertiesPackage.PROJECT__LOCAL:
				setLocal(((Boolean)newValue).booleanValue());
				return;
			case PropertiesPackage.PROJECT__FOLDERS:
				getFolders().clear();
				getFolders().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__DELETED:
				setDeleted(((Boolean)newValue).booleanValue());
				return;
			case PropertiesPackage.PROJECT__DELETE_DATE:
				setDeleteDate((Date)newValue);
				return;
			case PropertiesPackage.PROJECT__CREATION_DATE:
				setCreationDate((Date)newValue);
				return;
			case PropertiesPackage.PROJECT__AUTHOR:
				setAuthor((User)newValue);
				return;
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				getUserAuthorization().clear();
				getUserAuthorization().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				getAllowedComponents().clear();
				getAllowedComponents().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				getReferencedProjects().clear();
				getReferencedProjects().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				getAvailableRefProject().clear();
				getAvailableRefProject().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__MASTER_JOB_ID:
				setMasterJobId((String)newValue);
				return;
			case PropertiesPackage.PROJECT__SPAGO_BI_SERVER:
				getSpagoBiServer().clear();
				getSpagoBiServer().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__PRODUCT_VERSION:
				setProductVersion((String)newValue);
				return;
			case PropertiesPackage.PROJECT__COMPONENTS_SETTINGS:
				getComponentsSettings().clear();
				getComponentsSettings().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__URL:
				setUrl((String)newValue);
				return;
			case PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS:
				setStatAndLogsSettings((StatAndLogsSettings)newValue);
				return;
			case PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS:
				setImplicitContextSettings((ImplicitContextSettings)newValue);
				return;
			case PropertiesPackage.PROJECT__HIDE_PASSWORD:
				setHidePassword(((Boolean)newValue).booleanValue());
				return;
			case PropertiesPackage.PROJECT__ITEMS_RELATIONS:
				getItemsRelations().clear();
				getItemsRelations().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__REFERENCE:
				setReference(((Boolean)newValue).booleanValue());
				return;
			case PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS:
				getCustomComponentSettings().clear();
				getCustomComponentSettings().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__DELETED_FOLDERS:
				getDeletedFolders().clear();
				getDeletedFolders().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__TYPE:
				setType((String)newValue);
				return;
			case PropertiesPackage.PROJECT__ITEMS_RELATION_VERSION:
				setItemsRelationVersion((String)newValue);
				return;
			case PropertiesPackage.PROJECT__EXCHANGE_USER:
				setExchangeUser((ExchangeUser)newValue);
				return;
			case PropertiesPackage.PROJECT__MIGRATION_TASKS:
				getMigrationTasks().clear();
				getMigrationTasks().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__MIGRATION_TASK:
				getMigrationTask().clear();
				getMigrationTask().addAll((Collection)newValue);
				return;
			case PropertiesPackage.PROJECT__BIG_DATA:
				setBigData(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__TECHNICAL_STATUS:
				getTechnicalStatus().clear();
				return;
			case PropertiesPackage.PROJECT__DOCUMENTATION_STATUS:
				getDocumentationStatus().clear();
				return;
			case PropertiesPackage.PROJECT__ID:
				setId(ID_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__LANGUAGE:
				setLanguage(LANGUAGE_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__TECHNICAL_LABEL:
				setTechnicalLabel(TECHNICAL_LABEL_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__LOCAL:
				setLocal(LOCAL_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__FOLDERS:
				getFolders().clear();
				return;
			case PropertiesPackage.PROJECT__DELETED:
				setDeleted(DELETED_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__DELETE_DATE:
				setDeleteDate(DELETE_DATE_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__CREATION_DATE:
				setCreationDate(CREATION_DATE_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__AUTHOR:
				setAuthor((User)null);
				return;
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				getUserAuthorization().clear();
				return;
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				getAllowedComponents().clear();
				return;
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				getReferencedProjects().clear();
				return;
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				getAvailableRefProject().clear();
				return;
			case PropertiesPackage.PROJECT__MASTER_JOB_ID:
				setMasterJobId(MASTER_JOB_ID_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__SPAGO_BI_SERVER:
				getSpagoBiServer().clear();
				return;
			case PropertiesPackage.PROJECT__PRODUCT_VERSION:
				setProductVersion(PRODUCT_VERSION_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__COMPONENTS_SETTINGS:
				getComponentsSettings().clear();
				return;
			case PropertiesPackage.PROJECT__URL:
				setUrl(URL_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS:
				setStatAndLogsSettings((StatAndLogsSettings)null);
				return;
			case PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS:
				setImplicitContextSettings((ImplicitContextSettings)null);
				return;
			case PropertiesPackage.PROJECT__HIDE_PASSWORD:
				setHidePassword(HIDE_PASSWORD_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__ITEMS_RELATIONS:
				getItemsRelations().clear();
				return;
			case PropertiesPackage.PROJECT__REFERENCE:
				setReference(REFERENCE_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS:
				getCustomComponentSettings().clear();
				return;
			case PropertiesPackage.PROJECT__DELETED_FOLDERS:
				getDeletedFolders().clear();
				return;
			case PropertiesPackage.PROJECT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__ITEMS_RELATION_VERSION:
				setItemsRelationVersion(ITEMS_RELATION_VERSION_EDEFAULT);
				return;
			case PropertiesPackage.PROJECT__EXCHANGE_USER:
				setExchangeUser((ExchangeUser)null);
				return;
			case PropertiesPackage.PROJECT__MIGRATION_TASKS:
				getMigrationTasks().clear();
				return;
			case PropertiesPackage.PROJECT__MIGRATION_TASK:
				getMigrationTask().clear();
				return;
			case PropertiesPackage.PROJECT__BIG_DATA:
				setBigData(BIG_DATA_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PropertiesPackage.PROJECT__TECHNICAL_STATUS:
				return technicalStatus != null && !technicalStatus.isEmpty();
			case PropertiesPackage.PROJECT__DOCUMENTATION_STATUS:
				return documentationStatus != null && !documentationStatus.isEmpty();
			case PropertiesPackage.PROJECT__ID:
				return id != ID_EDEFAULT;
			case PropertiesPackage.PROJECT__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case PropertiesPackage.PROJECT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case PropertiesPackage.PROJECT__LANGUAGE:
				return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
			case PropertiesPackage.PROJECT__TECHNICAL_LABEL:
				return TECHNICAL_LABEL_EDEFAULT == null ? technicalLabel != null : !TECHNICAL_LABEL_EDEFAULT.equals(technicalLabel);
			case PropertiesPackage.PROJECT__LOCAL:
				return local != LOCAL_EDEFAULT;
			case PropertiesPackage.PROJECT__FOLDERS:
				return folders != null && !folders.isEmpty();
			case PropertiesPackage.PROJECT__DELETED:
				return deleted != DELETED_EDEFAULT;
			case PropertiesPackage.PROJECT__DELETE_DATE:
				return DELETE_DATE_EDEFAULT == null ? deleteDate != null : !DELETE_DATE_EDEFAULT.equals(deleteDate);
			case PropertiesPackage.PROJECT__CREATION_DATE:
				return CREATION_DATE_EDEFAULT == null ? creationDate != null : !CREATION_DATE_EDEFAULT.equals(creationDate);
			case PropertiesPackage.PROJECT__AUTHOR:
				return author != null;
			case PropertiesPackage.PROJECT__USER_AUTHORIZATION:
				return userAuthorization != null && !userAuthorization.isEmpty();
			case PropertiesPackage.PROJECT__ALLOWED_COMPONENTS:
				return allowedComponents != null && !allowedComponents.isEmpty();
			case PropertiesPackage.PROJECT__REFERENCED_PROJECTS:
				return referencedProjects != null && !referencedProjects.isEmpty();
			case PropertiesPackage.PROJECT__AVAILABLE_REF_PROJECT:
				return availableRefProject != null && !availableRefProject.isEmpty();
			case PropertiesPackage.PROJECT__MASTER_JOB_ID:
				return MASTER_JOB_ID_EDEFAULT == null ? masterJobId != null : !MASTER_JOB_ID_EDEFAULT.equals(masterJobId);
			case PropertiesPackage.PROJECT__SPAGO_BI_SERVER:
				return spagoBiServer != null && !spagoBiServer.isEmpty();
			case PropertiesPackage.PROJECT__PRODUCT_VERSION:
				return PRODUCT_VERSION_EDEFAULT == null ? productVersion != null : !PRODUCT_VERSION_EDEFAULT.equals(productVersion);
			case PropertiesPackage.PROJECT__COMPONENTS_SETTINGS:
				return componentsSettings != null && !componentsSettings.isEmpty();
			case PropertiesPackage.PROJECT__URL:
				return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
			case PropertiesPackage.PROJECT__STAT_AND_LOGS_SETTINGS:
				return statAndLogsSettings != null;
			case PropertiesPackage.PROJECT__IMPLICIT_CONTEXT_SETTINGS:
				return implicitContextSettings != null;
			case PropertiesPackage.PROJECT__HIDE_PASSWORD:
				return hidePassword != HIDE_PASSWORD_EDEFAULT;
			case PropertiesPackage.PROJECT__ITEMS_RELATIONS:
				return itemsRelations != null && !itemsRelations.isEmpty();
			case PropertiesPackage.PROJECT__REFERENCE:
				return reference != REFERENCE_EDEFAULT;
			case PropertiesPackage.PROJECT__CUSTOM_COMPONENT_SETTINGS:
				return customComponentSettings != null && !customComponentSettings.isEmpty();
			case PropertiesPackage.PROJECT__DELETED_FOLDERS:
				return deletedFolders != null && !deletedFolders.isEmpty();
			case PropertiesPackage.PROJECT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case PropertiesPackage.PROJECT__ITEMS_RELATION_VERSION:
				return ITEMS_RELATION_VERSION_EDEFAULT == null ? itemsRelationVersion != null : !ITEMS_RELATION_VERSION_EDEFAULT.equals(itemsRelationVersion);
			case PropertiesPackage.PROJECT__EXCHANGE_USER:
				return exchangeUser != null;
			case PropertiesPackage.PROJECT__MIGRATION_TASKS:
				return migrationTasks != null && !migrationTasks.isEmpty();
			case PropertiesPackage.PROJECT__MIGRATION_TASK:
				return migrationTask != null && !migrationTask.isEmpty();
			case PropertiesPackage.PROJECT__BIG_DATA:
				return bigData != BIG_DATA_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: ");
		result.append(id);
		result.append(", label: ");
		result.append(label);
		result.append(", description: ");
		result.append(description);
		result.append(", language: ");
		result.append(language);
		result.append(", technicalLabel: ");
		result.append(technicalLabel);
		result.append(", local: ");
		result.append(local);
		result.append(", deleted: ");
		result.append(deleted);
		result.append(", deleteDate: ");
		result.append(deleteDate);
		result.append(", creationDate: ");
		result.append(creationDate);
		result.append(", masterJobId: ");
		result.append(masterJobId);
		result.append(", productVersion: ");
		result.append(productVersion);
		result.append(", url: ");
		result.append(url);
		result.append(", hidePassword: ");
		result.append(hidePassword);
		result.append(", reference: ");
		result.append(reference);
		result.append(", deletedFolders: ");
		result.append(deletedFolders);
		result.append(", type: ");
		result.append(type);
		result.append(", itemsRelationVersion: ");
		result.append(itemsRelationVersion);
		result.append(", migrationTasks: ");
		result.append(migrationTasks);
		result.append(", bigData: ");
		result.append(bigData);
		result.append(')');
		return result.toString();
	}

} // ProjectImpl
