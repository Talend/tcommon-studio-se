/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.impl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.properties.DashboardConnection;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.UserModuleAuthorization;
import org.talend.core.model.properties.UserProjectAuthorization;
import org.talend.core.model.properties.UserRole;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>User</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLogin <em>Login</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getFirstName <em>First Name</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLastName <em>Last Name</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getCreationDate <em>Creation Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getDeleteDate <em>Delete Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#isDeleted <em>Deleted</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#isAllowedToModifyComponents <em>Allowed To Modify Components</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getProjectAuthorization <em>Project Authorization</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getModuleAuthorization <em>Module Authorization</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getPreferredDashboardConnection <em>Preferred Dashboard Connection</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLastAdminConnectionDate <em>Last Admin Connection Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLastStudioConnectionDate <em>Last Studio Connection Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getFirstAdminConnectionDate <em>First Admin Connection Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getFirstStudioConnectionDate <em>First Studio Connection Date</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getAdminConnexionNumber <em>Admin Connexion Number</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getStudioConnexionNumber <em>Studio Connexion Number</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getAuthenticationInfo <em>Authentication Info</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLdapLogin <em>Ldap Login</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLdapId <em>Ldap Id</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#getAdditionnalData <em>Additionnal Data</em>}</li>
 *   <li>{@link org.talend.core.model.properties.impl.UserImpl#isTechnical <em>Technical</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserImpl extends EObjectImpl implements User {

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
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
     * This is true if the Id attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean idESet;

    /**
     * The default value of the '{@link #getLogin() <em>Login</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLogin()
     * @generated
     * @ordered
     */
    protected static final String LOGIN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLogin() <em>Login</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getLogin()
     * @generated
     * @ordered
     */
    protected String login = LOGIN_EDEFAULT;

    /**
     * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected static final byte[] PASSWORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPassword()
     * @generated
     * @ordered
     */
    protected byte[] password = PASSWORD_EDEFAULT;

    /**
     * The default value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getFirstName()
     * @generated
     * @ordered
     */
    protected static final String FIRST_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getFirstName()
     * @generated
     * @ordered
     */
    protected String firstName = FIRST_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLastName()
     * @generated
     * @ordered
     */
    protected static final String LAST_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLastName()
     * @generated
     * @ordered
     */
    protected String lastName = LAST_NAME_EDEFAULT;

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
     * The default value of the '{@link #getDeleteDate() <em>Delete Date</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
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
     * This is true if the Deleted attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean deletedESet;

    /**
     * The default value of the '{@link #isAllowedToModifyComponents() <em>Allowed To Modify Components</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isAllowedToModifyComponents()
     * @generated
     * @ordered
     */
    protected static final boolean ALLOWED_TO_MODIFY_COMPONENTS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAllowedToModifyComponents() <em>Allowed To Modify Components</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isAllowedToModifyComponents()
     * @generated
     * @ordered
     */
    protected boolean allowedToModifyComponents = ALLOWED_TO_MODIFY_COMPONENTS_EDEFAULT;

    /**
     * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComment()
     * @generated
     * @ordered
     */
    protected static final String COMMENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComment()
     * @generated
     * @ordered
     */
    protected String comment = COMMENT_EDEFAULT;

    /**
     * The cached value of the '{@link #getProjectAuthorization() <em>Project Authorization</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProjectAuthorization()
     * @generated
     * @ordered
     */
    protected EList projectAuthorization;

    /**
     * The cached value of the '{@link #getModuleAuthorization() <em>Module Authorization</em>}' reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getModuleAuthorization()
     * @generated
     * @ordered
     */
    protected EList moduleAuthorization;

    /**
     * The cached value of the '{@link #getPreferredDashboardConnection() <em>Preferred Dashboard Connection</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getPreferredDashboardConnection()
     * @generated
     * @ordered
     */
    protected DashboardConnection preferredDashboardConnection;

    /**
     * The default value of the '{@link #getLastAdminConnectionDate() <em>Last Admin Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLastAdminConnectionDate()
     * @generated
     * @ordered
     */
    protected static final Date LAST_ADMIN_CONNECTION_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastAdminConnectionDate() <em>Last Admin Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLastAdminConnectionDate()
     * @generated
     * @ordered
     */
    protected Date lastAdminConnectionDate = LAST_ADMIN_CONNECTION_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getLastStudioConnectionDate() <em>Last Studio Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLastStudioConnectionDate()
     * @generated
     * @ordered
     */
    protected static final Date LAST_STUDIO_CONNECTION_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastStudioConnectionDate() <em>Last Studio Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getLastStudioConnectionDate()
     * @generated
     * @ordered
     */
    protected Date lastStudioConnectionDate = LAST_STUDIO_CONNECTION_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getFirstAdminConnectionDate() <em>First Admin Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getFirstAdminConnectionDate()
     * @generated
     * @ordered
     */
    protected static final Date FIRST_ADMIN_CONNECTION_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFirstAdminConnectionDate() <em>First Admin Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getFirstAdminConnectionDate()
     * @generated
     * @ordered
     */
    protected Date firstAdminConnectionDate = FIRST_ADMIN_CONNECTION_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getFirstStudioConnectionDate() <em>First Studio Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getFirstStudioConnectionDate()
     * @generated
     * @ordered
     */
    protected static final Date FIRST_STUDIO_CONNECTION_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFirstStudioConnectionDate() <em>First Studio Connection Date</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getFirstStudioConnectionDate()
     * @generated
     * @ordered
     */
    protected Date firstStudioConnectionDate = FIRST_STUDIO_CONNECTION_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getAdminConnexionNumber() <em>Admin Connexion Number</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAdminConnexionNumber()
     * @generated
     * @ordered
     */
    protected static final int ADMIN_CONNEXION_NUMBER_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getAdminConnexionNumber() <em>Admin Connexion Number</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAdminConnexionNumber()
     * @generated
     * @ordered
     */
    protected int adminConnexionNumber = ADMIN_CONNEXION_NUMBER_EDEFAULT;

    /**
     * This is true if the Admin Connexion Number attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean adminConnexionNumberESet;

    /**
     * The default value of the '{@link #getStudioConnexionNumber() <em>Studio Connexion Number</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getStudioConnexionNumber()
     * @generated
     * @ordered
     */
    protected static final int STUDIO_CONNEXION_NUMBER_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStudioConnexionNumber() <em>Studio Connexion Number</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getStudioConnexionNumber()
     * @generated
     * @ordered
     */
    protected int studioConnexionNumber = STUDIO_CONNEXION_NUMBER_EDEFAULT;

    /**
     * This is true if the Studio Connexion Number attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean studioConnexionNumberESet;

    /**
     * The default value of the '{@link #getAuthenticationInfo() <em>Authentication Info</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAuthenticationInfo()
     * @generated
     * @ordered
     */
    protected static final String AUTHENTICATION_INFO_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAuthenticationInfo() <em>Authentication Info</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAuthenticationInfo()
     * @generated
     * @ordered
     */
    protected String authenticationInfo = AUTHENTICATION_INFO_EDEFAULT;

    /**
     * The default value of the '{@link #getLdapLogin() <em>Ldap Login</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLdapLogin()
     * @generated
     * @ordered
     */
    protected static final String LDAP_LOGIN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLdapLogin() <em>Ldap Login</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLdapLogin()
     * @generated
     * @ordered
     */
    protected String ldapLogin = LDAP_LOGIN_EDEFAULT;

    /**
     * The default value of the '{@link #getLdapId() <em>Ldap Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLdapId()
     * @generated
     * @ordered
     */
    protected static final String LDAP_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLdapId() <em>Ldap Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLdapId()
     * @generated
     * @ordered
     */
    protected String ldapId = LDAP_ID_EDEFAULT;

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
     * The default value of the '{@link #getAdditionnalData() <em>Additionnal Data</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionnalData()
     * @generated
     * @ordered
     */
    protected static final String ADDITIONNAL_DATA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAdditionnalData() <em>Additionnal Data</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionnalData()
     * @generated
     * @ordered
     */
    protected String additionnalData = ADDITIONNAL_DATA_EDEFAULT;

    /**
     * The default value of the '{@link #isTechnical() <em>Technical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTechnical()
     * @generated
     * @ordered
     */
    protected static final boolean TECHNICAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isTechnical() <em>Technical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isTechnical()
     * @generated
     * @ordered
     */
    protected boolean technical = TECHNICAL_EDEFAULT;

    /**
     * This is true if the Technical attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean technicalESet;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected UserImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.USER;
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
        boolean oldIdESet = idESet;
        idESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__ID, oldId, id, !oldIdESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetId() {
        int oldId = id;
        boolean oldIdESet = idESet;
        id = ID_EDEFAULT;
        idESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, PropertiesPackage.USER__ID, oldId, ID_EDEFAULT, oldIdESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetId() {
        return idESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLogin() {
        return login;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLogin(String newLogin) {
        String oldLogin = login;
        login = newLogin;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LOGIN, oldLogin, login));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPassword(byte[] newPassword) {
        byte[] oldPassword = password;
        password = newPassword;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__PASSWORD, oldPassword, password));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFirstName(String newFirstName) {
        String oldFirstName = firstName;
        firstName = newFirstName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__FIRST_NAME, oldFirstName, firstName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLastName(String newLastName) {
        String oldLastName = lastName;
        lastName = newLastName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LAST_NAME, oldLastName, lastName));
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__CREATION_DATE, oldCreationDate, creationDate));
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__DELETE_DATE, oldDeleteDate, deleteDate));
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
        boolean oldDeletedESet = deletedESet;
        deletedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__DELETED, oldDeleted, deleted, !oldDeletedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetDeleted() {
        boolean oldDeleted = deleted;
        boolean oldDeletedESet = deletedESet;
        deleted = DELETED_EDEFAULT;
        deletedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, PropertiesPackage.USER__DELETED, oldDeleted, DELETED_EDEFAULT, oldDeletedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetDeleted() {
        return deletedESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isAllowedToModifyComponents() {
        return allowedToModifyComponents;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAllowedToModifyComponents(boolean newAllowedToModifyComponents) {
        boolean oldAllowedToModifyComponents = allowedToModifyComponents;
        allowedToModifyComponents = newAllowedToModifyComponents;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__ALLOWED_TO_MODIFY_COMPONENTS, oldAllowedToModifyComponents, allowedToModifyComponents));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getComment() {
        return comment;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setComment(String newComment) {
        String oldComment = comment;
        comment = newComment;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__COMMENT, oldComment, comment));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList getProjectAuthorization() {
        if (projectAuthorization == null) {
            projectAuthorization = new EObjectWithInverseResolvingEList(UserProjectAuthorization.class, this, PropertiesPackage.USER__PROJECT_AUTHORIZATION, PropertiesPackage.USER_PROJECT_AUTHORIZATION__USER);
        }
        return projectAuthorization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList getModuleAuthorization() {
        if (moduleAuthorization == null) {
            moduleAuthorization = new EObjectWithInverseResolvingEList(UserModuleAuthorization.class, this, PropertiesPackage.USER__MODULE_AUTHORIZATION, PropertiesPackage.USER_MODULE_AUTHORIZATION__USER);
        }
        return moduleAuthorization;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DashboardConnection getPreferredDashboardConnection() {
        if (preferredDashboardConnection != null && preferredDashboardConnection.eIsProxy()) {
            InternalEObject oldPreferredDashboardConnection = (InternalEObject)preferredDashboardConnection;
            preferredDashboardConnection = (DashboardConnection)eResolveProxy(oldPreferredDashboardConnection);
            if (preferredDashboardConnection != oldPreferredDashboardConnection) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION, oldPreferredDashboardConnection, preferredDashboardConnection));
            }
        }
        return preferredDashboardConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DashboardConnection basicGetPreferredDashboardConnection() {
        return preferredDashboardConnection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPreferredDashboardConnection(DashboardConnection newPreferredDashboardConnection) {
        DashboardConnection oldPreferredDashboardConnection = preferredDashboardConnection;
        preferredDashboardConnection = newPreferredDashboardConnection;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION, oldPreferredDashboardConnection, preferredDashboardConnection));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Date getLastAdminConnectionDate() {
        return lastAdminConnectionDate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLastAdminConnectionDate(Date newLastAdminConnectionDate) {
        Date oldLastAdminConnectionDate = lastAdminConnectionDate;
        lastAdminConnectionDate = newLastAdminConnectionDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LAST_ADMIN_CONNECTION_DATE, oldLastAdminConnectionDate, lastAdminConnectionDate));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Date getLastStudioConnectionDate() {
        return lastStudioConnectionDate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLastStudioConnectionDate(Date newLastStudioConnectionDate) {
        Date oldLastStudioConnectionDate = lastStudioConnectionDate;
        lastStudioConnectionDate = newLastStudioConnectionDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LAST_STUDIO_CONNECTION_DATE, oldLastStudioConnectionDate, lastStudioConnectionDate));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Date getFirstAdminConnectionDate() {
        return firstAdminConnectionDate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFirstAdminConnectionDate(Date newFirstAdminConnectionDate) {
        Date oldFirstAdminConnectionDate = firstAdminConnectionDate;
        firstAdminConnectionDate = newFirstAdminConnectionDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__FIRST_ADMIN_CONNECTION_DATE, oldFirstAdminConnectionDate, firstAdminConnectionDate));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Date getFirstStudioConnectionDate() {
        return firstStudioConnectionDate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setFirstStudioConnectionDate(Date newFirstStudioConnectionDate) {
        Date oldFirstStudioConnectionDate = firstStudioConnectionDate;
        firstStudioConnectionDate = newFirstStudioConnectionDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__FIRST_STUDIO_CONNECTION_DATE, oldFirstStudioConnectionDate, firstStudioConnectionDate));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public int getAdminConnexionNumber() {
        return adminConnexionNumber;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAdminConnexionNumber(int newAdminConnexionNumber) {
        int oldAdminConnexionNumber = adminConnexionNumber;
        adminConnexionNumber = newAdminConnexionNumber;
        boolean oldAdminConnexionNumberESet = adminConnexionNumberESet;
        adminConnexionNumberESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER, oldAdminConnexionNumber, adminConnexionNumber, !oldAdminConnexionNumberESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetAdminConnexionNumber() {
        int oldAdminConnexionNumber = adminConnexionNumber;
        boolean oldAdminConnexionNumberESet = adminConnexionNumberESet;
        adminConnexionNumber = ADMIN_CONNEXION_NUMBER_EDEFAULT;
        adminConnexionNumberESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER, oldAdminConnexionNumber, ADMIN_CONNEXION_NUMBER_EDEFAULT, oldAdminConnexionNumberESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetAdminConnexionNumber() {
        return adminConnexionNumberESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public int getStudioConnexionNumber() {
        return studioConnexionNumber;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setStudioConnexionNumber(int newStudioConnexionNumber) {
        int oldStudioConnexionNumber = studioConnexionNumber;
        studioConnexionNumber = newStudioConnexionNumber;
        boolean oldStudioConnexionNumberESet = studioConnexionNumberESet;
        studioConnexionNumberESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER, oldStudioConnexionNumber, studioConnexionNumber, !oldStudioConnexionNumberESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStudioConnexionNumber() {
        int oldStudioConnexionNumber = studioConnexionNumber;
        boolean oldStudioConnexionNumberESet = studioConnexionNumberESet;
        studioConnexionNumber = STUDIO_CONNEXION_NUMBER_EDEFAULT;
        studioConnexionNumberESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER, oldStudioConnexionNumber, STUDIO_CONNEXION_NUMBER_EDEFAULT, oldStudioConnexionNumberESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStudioConnexionNumber() {
        return studioConnexionNumberESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getAuthenticationInfo() {
        return authenticationInfo;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAuthenticationInfo(String newAuthenticationInfo) {
        String oldAuthenticationInfo = authenticationInfo;
        authenticationInfo = newAuthenticationInfo;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__AUTHENTICATION_INFO, oldAuthenticationInfo, authenticationInfo));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLdapLogin() {
        return ldapLogin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLdapLogin(String newLdapLogin) {
        String oldLdapLogin = ldapLogin;
        ldapLogin = newLdapLogin;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LDAP_LOGIN, oldLdapLogin, ldapLogin));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLdapId() {
        return ldapId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLdapId(String newLdapId) {
        String oldLdapId = ldapId;
        ldapId = newLdapId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LDAP_ID, oldLdapId, ldapId));
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__LANGUAGE, oldLanguage, language));
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAdditionnalData() {
        return additionnalData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAdditionnalData(String newAdditionnalData) {
        String oldAdditionnalData = additionnalData;
        additionnalData = newAdditionnalData;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__ADDITIONNAL_DATA, oldAdditionnalData, additionnalData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isTechnical() {
        return technical;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTechnical(boolean newTechnical) {
        boolean oldTechnical = technical;
        technical = newTechnical;
        boolean oldTechnicalESet = technicalESet;
        technicalESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.USER__TECHNICAL, oldTechnical, technical, !oldTechnicalESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetTechnical() {
        boolean oldTechnical = technical;
        boolean oldTechnicalESet = technicalESet;
        technical = TECHNICAL_EDEFAULT;
        technicalESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, PropertiesPackage.USER__TECHNICAL, oldTechnical, TECHNICAL_EDEFAULT, oldTechnicalESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetTechnical() {
        return technicalESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                return ((InternalEList)getProjectAuthorization()).basicAdd(otherEnd, msgs);
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                return ((InternalEList)getModuleAuthorization()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                return ((InternalEList)getProjectAuthorization()).basicRemove(otherEnd, msgs);
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                return ((InternalEList)getModuleAuthorization()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PropertiesPackage.USER__ID:
                return new Integer(getId());
            case PropertiesPackage.USER__LOGIN:
                return getLogin();
            case PropertiesPackage.USER__PASSWORD:
                return getPassword();
            case PropertiesPackage.USER__FIRST_NAME:
                return getFirstName();
            case PropertiesPackage.USER__LAST_NAME:
                return getLastName();
            case PropertiesPackage.USER__CREATION_DATE:
                return getCreationDate();
            case PropertiesPackage.USER__DELETE_DATE:
                return getDeleteDate();
            case PropertiesPackage.USER__DELETED:
                return isDeleted() ? Boolean.TRUE : Boolean.FALSE;
            case PropertiesPackage.USER__ALLOWED_TO_MODIFY_COMPONENTS:
                return isAllowedToModifyComponents() ? Boolean.TRUE : Boolean.FALSE;
            case PropertiesPackage.USER__COMMENT:
                return getComment();
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                return getProjectAuthorization();
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                return getModuleAuthorization();
            case PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION:
                if (resolve) return getPreferredDashboardConnection();
                return basicGetPreferredDashboardConnection();
            case PropertiesPackage.USER__LAST_ADMIN_CONNECTION_DATE:
                return getLastAdminConnectionDate();
            case PropertiesPackage.USER__LAST_STUDIO_CONNECTION_DATE:
                return getLastStudioConnectionDate();
            case PropertiesPackage.USER__FIRST_ADMIN_CONNECTION_DATE:
                return getFirstAdminConnectionDate();
            case PropertiesPackage.USER__FIRST_STUDIO_CONNECTION_DATE:
                return getFirstStudioConnectionDate();
            case PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER:
                return new Integer(getAdminConnexionNumber());
            case PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER:
                return new Integer(getStudioConnexionNumber());
            case PropertiesPackage.USER__AUTHENTICATION_INFO:
                return getAuthenticationInfo();
            case PropertiesPackage.USER__LDAP_LOGIN:
                return getLdapLogin();
            case PropertiesPackage.USER__LDAP_ID:
                return getLdapId();
            case PropertiesPackage.USER__LANGUAGE:
                return getLanguage();
            case PropertiesPackage.USER__TYPE:
                return getType();
            case PropertiesPackage.USER__ADDITIONNAL_DATA:
                return getAdditionnalData();
            case PropertiesPackage.USER__TECHNICAL:
                return isTechnical() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PropertiesPackage.USER__ID:
                setId(((Integer)newValue).intValue());
                return;
            case PropertiesPackage.USER__LOGIN:
                setLogin((String)newValue);
                return;
            case PropertiesPackage.USER__PASSWORD:
                setPassword((byte[])newValue);
                return;
            case PropertiesPackage.USER__FIRST_NAME:
                setFirstName((String)newValue);
                return;
            case PropertiesPackage.USER__LAST_NAME:
                setLastName((String)newValue);
                return;
            case PropertiesPackage.USER__CREATION_DATE:
                setCreationDate((Date)newValue);
                return;
            case PropertiesPackage.USER__DELETE_DATE:
                setDeleteDate((Date)newValue);
                return;
            case PropertiesPackage.USER__DELETED:
                setDeleted(((Boolean)newValue).booleanValue());
                return;
            case PropertiesPackage.USER__ALLOWED_TO_MODIFY_COMPONENTS:
                setAllowedToModifyComponents(((Boolean)newValue).booleanValue());
                return;
            case PropertiesPackage.USER__COMMENT:
                setComment((String)newValue);
                return;
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                getProjectAuthorization().clear();
                getProjectAuthorization().addAll((Collection)newValue);
                return;
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                getModuleAuthorization().clear();
                getModuleAuthorization().addAll((Collection)newValue);
                return;
            case PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION:
                setPreferredDashboardConnection((DashboardConnection)newValue);
                return;
            case PropertiesPackage.USER__LAST_ADMIN_CONNECTION_DATE:
                setLastAdminConnectionDate((Date)newValue);
                return;
            case PropertiesPackage.USER__LAST_STUDIO_CONNECTION_DATE:
                setLastStudioConnectionDate((Date)newValue);
                return;
            case PropertiesPackage.USER__FIRST_ADMIN_CONNECTION_DATE:
                setFirstAdminConnectionDate((Date)newValue);
                return;
            case PropertiesPackage.USER__FIRST_STUDIO_CONNECTION_DATE:
                setFirstStudioConnectionDate((Date)newValue);
                return;
            case PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER:
                setAdminConnexionNumber(((Integer)newValue).intValue());
                return;
            case PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER:
                setStudioConnexionNumber(((Integer)newValue).intValue());
                return;
            case PropertiesPackage.USER__AUTHENTICATION_INFO:
                setAuthenticationInfo((String)newValue);
                return;
            case PropertiesPackage.USER__LDAP_LOGIN:
                setLdapLogin((String)newValue);
                return;
            case PropertiesPackage.USER__LDAP_ID:
                setLdapId((String)newValue);
                return;
            case PropertiesPackage.USER__LANGUAGE:
                setLanguage((String)newValue);
                return;
            case PropertiesPackage.USER__TYPE:
                setType((String)newValue);
                return;
            case PropertiesPackage.USER__ADDITIONNAL_DATA:
                setAdditionnalData((String)newValue);
                return;
            case PropertiesPackage.USER__TECHNICAL:
                setTechnical(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case PropertiesPackage.USER__ID:
                unsetId();
                return;
            case PropertiesPackage.USER__LOGIN:
                setLogin(LOGIN_EDEFAULT);
                return;
            case PropertiesPackage.USER__PASSWORD:
                setPassword(PASSWORD_EDEFAULT);
                return;
            case PropertiesPackage.USER__FIRST_NAME:
                setFirstName(FIRST_NAME_EDEFAULT);
                return;
            case PropertiesPackage.USER__LAST_NAME:
                setLastName(LAST_NAME_EDEFAULT);
                return;
            case PropertiesPackage.USER__CREATION_DATE:
                setCreationDate(CREATION_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__DELETE_DATE:
                setDeleteDate(DELETE_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__DELETED:
                unsetDeleted();
                return;
            case PropertiesPackage.USER__ALLOWED_TO_MODIFY_COMPONENTS:
                setAllowedToModifyComponents(ALLOWED_TO_MODIFY_COMPONENTS_EDEFAULT);
                return;
            case PropertiesPackage.USER__COMMENT:
                setComment(COMMENT_EDEFAULT);
                return;
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                getProjectAuthorization().clear();
                return;
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                getModuleAuthorization().clear();
                return;
            case PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION:
                setPreferredDashboardConnection((DashboardConnection)null);
                return;
            case PropertiesPackage.USER__LAST_ADMIN_CONNECTION_DATE:
                setLastAdminConnectionDate(LAST_ADMIN_CONNECTION_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__LAST_STUDIO_CONNECTION_DATE:
                setLastStudioConnectionDate(LAST_STUDIO_CONNECTION_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__FIRST_ADMIN_CONNECTION_DATE:
                setFirstAdminConnectionDate(FIRST_ADMIN_CONNECTION_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__FIRST_STUDIO_CONNECTION_DATE:
                setFirstStudioConnectionDate(FIRST_STUDIO_CONNECTION_DATE_EDEFAULT);
                return;
            case PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER:
                unsetAdminConnexionNumber();
                return;
            case PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER:
                unsetStudioConnexionNumber();
                return;
            case PropertiesPackage.USER__AUTHENTICATION_INFO:
                setAuthenticationInfo(AUTHENTICATION_INFO_EDEFAULT);
                return;
            case PropertiesPackage.USER__LDAP_LOGIN:
                setLdapLogin(LDAP_LOGIN_EDEFAULT);
                return;
            case PropertiesPackage.USER__LDAP_ID:
                setLdapId(LDAP_ID_EDEFAULT);
                return;
            case PropertiesPackage.USER__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
                return;
            case PropertiesPackage.USER__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case PropertiesPackage.USER__ADDITIONNAL_DATA:
                setAdditionnalData(ADDITIONNAL_DATA_EDEFAULT);
                return;
            case PropertiesPackage.USER__TECHNICAL:
                unsetTechnical();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PropertiesPackage.USER__ID:
                return isSetId();
            case PropertiesPackage.USER__LOGIN:
                return LOGIN_EDEFAULT == null ? login != null : !LOGIN_EDEFAULT.equals(login);
            case PropertiesPackage.USER__PASSWORD:
                return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
            case PropertiesPackage.USER__FIRST_NAME:
                return FIRST_NAME_EDEFAULT == null ? firstName != null : !FIRST_NAME_EDEFAULT.equals(firstName);
            case PropertiesPackage.USER__LAST_NAME:
                return LAST_NAME_EDEFAULT == null ? lastName != null : !LAST_NAME_EDEFAULT.equals(lastName);
            case PropertiesPackage.USER__CREATION_DATE:
                return CREATION_DATE_EDEFAULT == null ? creationDate != null : !CREATION_DATE_EDEFAULT.equals(creationDate);
            case PropertiesPackage.USER__DELETE_DATE:
                return DELETE_DATE_EDEFAULT == null ? deleteDate != null : !DELETE_DATE_EDEFAULT.equals(deleteDate);
            case PropertiesPackage.USER__DELETED:
                return isSetDeleted();
            case PropertiesPackage.USER__ALLOWED_TO_MODIFY_COMPONENTS:
                return allowedToModifyComponents != ALLOWED_TO_MODIFY_COMPONENTS_EDEFAULT;
            case PropertiesPackage.USER__COMMENT:
                return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
            case PropertiesPackage.USER__PROJECT_AUTHORIZATION:
                return projectAuthorization != null && !projectAuthorization.isEmpty();
            case PropertiesPackage.USER__MODULE_AUTHORIZATION:
                return moduleAuthorization != null && !moduleAuthorization.isEmpty();
            case PropertiesPackage.USER__PREFERRED_DASHBOARD_CONNECTION:
                return preferredDashboardConnection != null;
            case PropertiesPackage.USER__LAST_ADMIN_CONNECTION_DATE:
                return LAST_ADMIN_CONNECTION_DATE_EDEFAULT == null ? lastAdminConnectionDate != null : !LAST_ADMIN_CONNECTION_DATE_EDEFAULT.equals(lastAdminConnectionDate);
            case PropertiesPackage.USER__LAST_STUDIO_CONNECTION_DATE:
                return LAST_STUDIO_CONNECTION_DATE_EDEFAULT == null ? lastStudioConnectionDate != null : !LAST_STUDIO_CONNECTION_DATE_EDEFAULT.equals(lastStudioConnectionDate);
            case PropertiesPackage.USER__FIRST_ADMIN_CONNECTION_DATE:
                return FIRST_ADMIN_CONNECTION_DATE_EDEFAULT == null ? firstAdminConnectionDate != null : !FIRST_ADMIN_CONNECTION_DATE_EDEFAULT.equals(firstAdminConnectionDate);
            case PropertiesPackage.USER__FIRST_STUDIO_CONNECTION_DATE:
                return FIRST_STUDIO_CONNECTION_DATE_EDEFAULT == null ? firstStudioConnectionDate != null : !FIRST_STUDIO_CONNECTION_DATE_EDEFAULT.equals(firstStudioConnectionDate);
            case PropertiesPackage.USER__ADMIN_CONNEXION_NUMBER:
                return isSetAdminConnexionNumber();
            case PropertiesPackage.USER__STUDIO_CONNEXION_NUMBER:
                return isSetStudioConnexionNumber();
            case PropertiesPackage.USER__AUTHENTICATION_INFO:
                return AUTHENTICATION_INFO_EDEFAULT == null ? authenticationInfo != null : !AUTHENTICATION_INFO_EDEFAULT.equals(authenticationInfo);
            case PropertiesPackage.USER__LDAP_LOGIN:
                return LDAP_LOGIN_EDEFAULT == null ? ldapLogin != null : !LDAP_LOGIN_EDEFAULT.equals(ldapLogin);
            case PropertiesPackage.USER__LDAP_ID:
                return LDAP_ID_EDEFAULT == null ? ldapId != null : !LDAP_ID_EDEFAULT.equals(ldapId);
            case PropertiesPackage.USER__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
            case PropertiesPackage.USER__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case PropertiesPackage.USER__ADDITIONNAL_DATA:
                return ADDITIONNAL_DATA_EDEFAULT == null ? additionnalData != null : !ADDITIONNAL_DATA_EDEFAULT.equals(additionnalData);
            case PropertiesPackage.USER__TECHNICAL:
                return isSetTechnical();
        }
        return super.eIsSet(featureID);
    }

    public String toString() {
        if (firstName != null && lastName != null)
            return lastName + ", " + firstName; //$NON-NLS-1$

        return login;
    }

    String gitAuthentification;

    @Override
    public String getGitAuthenticationInfo() {
        return gitAuthentification;
    }

    @Override
    public void setGitAuthenticationInfo(String value) {
        this.gitAuthentification = value;
    }

}
