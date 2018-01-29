/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.talend.core.model.properties.PropertiesFactory
 * @model kind="package"
 * @generated
 */
public interface PropertiesPackage extends EPackage {

    /**
     * The package name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "properties"; //$NON-NLS-1$

    /**
     * The package namespace URI.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.talend.org/properties"; //$NON-NLS-1$

    /**
     * The package namespace name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "TalendProperties"; //$NON-NLS-1$

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    PropertiesPackage eINSTANCE = org.talend.core.model.properties.impl.PropertiesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.StatusImpl <em>Status</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.talend.core.model.properties.impl.StatusImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getStatus()
     * @generated
     */
    int STATUS = 3;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ProjectImpl <em>Project</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ProjectImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProject()
     * @generated
     */
    int PROJECT = 0;

    /**
     * The feature id for the '<em><b>Technical Status</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__TECHNICAL_STATUS = 0;

    /**
     * The feature id for the '<em><b>Documentation Status</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__DOCUMENTATION_STATUS = 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__ID = 2;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__LABEL = 3;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__DESCRIPTION = 4;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__LANGUAGE = 5;

    /**
     * The feature id for the '<em><b>Technical Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__TECHNICAL_LABEL = 6;

    /**
     * The feature id for the '<em><b>Local</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__LOCAL = 7;

    /**
     * The feature id for the '<em><b>Folders</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__FOLDERS = 8;

    /**
     * The feature id for the '<em><b>Deleted</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__DELETED = 9;

    /**
     * The feature id for the '<em><b>Delete Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__DELETE_DATE = 10;

    /**
     * The feature id for the '<em><b>Creation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__CREATION_DATE = 11;

    /**
     * The feature id for the '<em><b>Author</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__AUTHOR = 12;

    /**
     * The feature id for the '<em><b>User Authorization</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__USER_AUTHORIZATION = 13;

    /**
     * The feature id for the '<em><b>Allowed Components</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__ALLOWED_COMPONENTS = 14;

    /**
     * The feature id for the '<em><b>Referenced Projects</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__REFERENCED_PROJECTS = 15;

    /**
     * The feature id for the '<em><b>Available Ref Project</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__AVAILABLE_REF_PROJECT = 16;

    /**
     * The feature id for the '<em><b>Master Job Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__MASTER_JOB_ID = 17;

    /**
     * The feature id for the '<em><b>Spago Bi Server</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__SPAGO_BI_SERVER = 18;

    /**
     * The feature id for the '<em><b>Product Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__PRODUCT_VERSION = 19;

    /**
     * The feature id for the '<em><b>Components Settings</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__COMPONENTS_SETTINGS = 20;

    /**
     * The feature id for the '<em><b>Url</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__URL = 21;

    /**
     * The feature id for the '<em><b>Stat And Logs Settings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__STAT_AND_LOGS_SETTINGS = 22;

    /**
     * The feature id for the '<em><b>Implicit Context Settings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__IMPLICIT_CONTEXT_SETTINGS = 23;

    /**
     * The feature id for the '<em><b>Hide Password</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__HIDE_PASSWORD = 24;

    /**
     * The feature id for the '<em><b>Items Relations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__ITEMS_RELATIONS = 25;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__REFERENCE = 26;

    /**
     * The feature id for the '<em><b>Custom Component Settings</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__CUSTOM_COMPONENT_SETTINGS = 27;

    /**
     * The feature id for the '<em><b>Deleted Folders</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__DELETED_FOLDERS = 28;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__TYPE = 29;

    /**
     * The feature id for the '<em><b>Items Relation Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__ITEMS_RELATION_VERSION = 30;

    /**
     * The feature id for the '<em><b>Exchange User</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__EXCHANGE_USER = 31;

    /**
     * The feature id for the '<em><b>Migration Tasks</b></em>' attribute list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__MIGRATION_TASKS = 32;

    /**
     * The feature id for the '<em><b>Migration Task</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT__MIGRATION_TASK = 33;

    /**
     * The feature id for the '<em><b>Big Data</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int PROJECT__BIG_DATA = 34;

				/**
     * The number of structural features of the '<em>Project</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_FEATURE_COUNT = 35;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.PropertyImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProperty()
     * @generated
     */
    int PROPERTY = 5;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ItemImpl <em>Item</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.talend.core.model.properties.impl.ItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItem()
     * @generated
     */
    int ITEM = 6;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.BusinessProcessItemImpl <em>Business Process Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.BusinessProcessItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getBusinessProcessItem()
     * @generated
     */
    int BUSINESS_PROCESS_ITEM = 9;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ItemStateImpl <em>Item State</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ItemStateImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemState()
     * @generated
     */
    int ITEM_STATE = 4;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.FileItemImpl <em>File Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.FileItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileItem()
     * @generated
     */
    int FILE_ITEM = 10;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.DocumentationItemImpl <em>Documentation Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.DocumentationItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDocumentationItem()
     * @generated
     */
    int DOCUMENTATION_ITEM = 12;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.RoutineItemImpl <em>Routine Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.RoutineItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRoutineItem()
     * @generated
     */
    int ROUTINE_ITEM = 13;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ByteArrayImpl <em>Byte Array</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ByteArrayImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getByteArray()
     * @generated
     */
    int BYTE_ARRAY = 11;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ConnectionItemImpl <em>Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getConnectionItem()
     * @generated
     */
    int CONNECTION_ITEM = 14;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.DelimitedFileConnectionItemImpl <em>Delimited File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.DelimitedFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDelimitedFileConnectionItem()
     * @generated
     */
    int DELIMITED_FILE_CONNECTION_ITEM = 17;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.PositionalFileConnectionItemImpl <em>Positional File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.PositionalFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPositionalFileConnectionItem()
     * @generated
     */
    int POSITIONAL_FILE_CONNECTION_ITEM = 18;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.RegExFileConnectionItemImpl <em>Reg Ex File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.RegExFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRegExFileConnectionItem()
     * @generated
     */
    int REG_EX_FILE_CONNECTION_ITEM = 19;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.CSVFileConnectionItemImpl <em>CSV File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.CSVFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCSVFileConnectionItem()
     * @generated
     */
    int CSV_FILE_CONNECTION_ITEM = 20;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.DatabaseConnectionItemImpl <em>Database Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.DatabaseConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDatabaseConnectionItem()
     * @generated
     */
    int DATABASE_CONNECTION_ITEM = 21;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ProcessItemImpl <em>Process Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ProcessItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProcessItem()
     * @generated
     */
    int PROCESS_ITEM = 29;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserImpl <em>User</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.talend.core.model.properties.impl.UserImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUser()
     * @generated
     */
    int USER = 31;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.FolderItemImpl <em>Folder Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.FolderItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFolderItem()
     * @generated
     */
    int FOLDER_ITEM = 32;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserRoleImpl <em>User Role</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.UserRoleImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRole()
     * @generated
     */
    int USER_ROLE = 30;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ComponentImpl <em>Component</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ComponentImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getComponent()
     * @generated
     */
    int COMPONENT = 33;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.XmlFileConnectionItemImpl <em>Xml File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.XmlFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getXmlFileConnectionItem()
     * @generated
     */
    int XML_FILE_CONNECTION_ITEM = 23;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.NotationHolderImpl <em>Notation Holder</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.NotationHolderImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getNotationHolder()
     * @generated
     */
    int NOTATION_HOLDER = 34;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ProjectComponentAuthorisationImpl <em>Project Component Authorisation</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ProjectComponentAuthorisationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProjectComponentAuthorisation()
     * @generated
     */
    int PROJECT_COMPONENT_AUTHORISATION = 1;

    /**
     * The feature id for the '<em><b>Project</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_COMPONENT_AUTHORISATION__PROJECT = 0;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_COMPONENT_AUTHORISATION__COMPONENT = 1;

    /**
     * The number of structural features of the '<em>Project Component Authorisation</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_COMPONENT_AUTHORISATION_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ProjectReferenceImpl <em>Project Reference</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ProjectReferenceImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProjectReference()
     * @generated
     */
    int PROJECT_REFERENCE = 2;

    /**
     * The feature id for the '<em><b>Project</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_REFERENCE__PROJECT = 0;

    /**
     * The feature id for the '<em><b>Branch</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_REFERENCE__BRANCH = 1;

    /**
     * The feature id for the '<em><b>Referenced Project</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PROJECT_REFERENCE__REFERENCED_PROJECT = 2;

    /**
     * The feature id for the '<em><b>Referenced Branch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PROJECT_REFERENCE__REFERENCED_BRANCH = 3;

    /**
     * The number of structural features of the '<em>Project Reference</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECT_REFERENCE_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS__LABEL = 0;

    /**
     * The feature id for the '<em><b>Code</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS__CODE = 1;

    /**
     * The number of structural features of the '<em>Status</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Path</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__PATH = 0;

    /**
     * The feature id for the '<em><b>Deleted</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__DELETED = 1;

    /**
     * The feature id for the '<em><b>Locked</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__LOCKED = 2;

    /**
     * The feature id for the '<em><b>Locker</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__LOCKER = 3;

    /**
     * The feature id for the '<em><b>Lock Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__LOCK_DATE = 4;

    /**
     * The feature id for the '<em><b>Commit Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_STATE__COMMIT_DATE = 5;

    /**
     * The number of structural features of the '<em>Item State</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int ITEM_STATE_FEATURE_COUNT = 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__LABEL = 1;

    /**
     * The feature id for the '<em><b>Purpose</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__PURPOSE = 2;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__DESCRIPTION = 3;

    /**
     * The feature id for the '<em><b>Creation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__CREATION_DATE = 4;

    /**
     * The feature id for the '<em><b>Modification Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PROPERTY__MODIFICATION_DATE = 5;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__VERSION = 6;

    /**
     * The feature id for the '<em><b>Status Code</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__STATUS_CODE = 7;

    /**
     * The feature id for the '<em><b>Item</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__ITEM = 8;

    /**
     * The feature id for the '<em><b>Author</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__AUTHOR = 9;

    /**
     * The feature id for the '<em><b>Informations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__INFORMATIONS = 10;

    /**
     * The feature id for the '<em><b>Max Information Level</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__MAX_INFORMATION_LEVEL = 11;

    /**
     * The feature id for the '<em><b>Old Status Code</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__OLD_STATUS_CODE = 12;

    /**
     * The feature id for the '<em><b>Additional Properties</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__ADDITIONAL_PROPERTIES = 13;

    /**
     * The feature id for the '<em><b>Display Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__DISPLAY_NAME = 14;

    /**
     * The number of structural features of the '<em>Property</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_FEATURE_COUNT = 15;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ReferenceItemImpl <em>Reference Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ReferenceItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getReferenceItem()
     * @generated
     */
    int REFERENCE_ITEM = 96;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__PROPERTY = 0;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__STATE = 1;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__PARENT = 2;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__REFERENCE_RESOURCES = 3;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__FILE_EXTENSION = 4;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM__NEED_VERSION = 5;

    /**
     * The number of structural features of the '<em>Item</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_FEATURE_COUNT = 6;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LinkDocumentationItemImpl <em>Link Documentation Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LinkDocumentationItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkDocumentationItem()
     * @generated
     */
    int LINK_DOCUMENTATION_ITEM = 7;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__NAME = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__EXTENSION = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Link</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM__LINK = ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Link Documentation Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_DOCUMENTATION_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LinkTypeImpl <em>Link Type</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LinkTypeImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkType()
     * @generated
     */
    int LINK_TYPE = 8;

    /**
     * The feature id for the '<em><b>URI</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_TYPE__URI = 0;

    /**
     * The feature id for the '<em><b>State</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_TYPE__STATE = 1;

    /**
     * The number of structural features of the '<em>Link Type</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int LINK_TYPE_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Notation</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__NOTATION = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Semantic</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__SEMANTIC = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Notation Holder</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__NOTATION_HOLDER = ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Svg Business Process Item</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM__SVG_BUSINESS_PROCESS_ITEM = ITEM_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Business Process Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int BUSINESS_PROCESS_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SnippetVariableImpl <em>Snippet Variable</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SnippetVariableImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSnippetVariable()
     * @generated
     */
    int SNIPPET_VARIABLE = 15;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SnippetItemImpl <em>Snippet Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SnippetItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSnippetItem()
     * @generated
     */
    int SNIPPET_ITEM = 16;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SAPConnectionItemImpl <em>SAP Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SAPConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSAPConnectionItem()
     * @generated
     */
    int SAP_CONNECTION_ITEM = 22;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LdifFileConnectionItemImpl <em>Ldif File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LdifFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLdifFileConnectionItem()
     * @generated
     */
    int LDIF_FILE_CONNECTION_ITEM = 24;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExcelFileConnectionItemImpl <em>Excel File Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExcelFileConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExcelFileConnectionItem()
     * @generated
     */
    int EXCEL_FILE_CONNECTION_ITEM = 25;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.EbcdicConnectionItemImpl <em>Ebcdic Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.EbcdicConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getEbcdicConnectionItem()
     * @generated
     */
    int EBCDIC_CONNECTION_ITEM = 26;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.MDMConnectionItemImpl <em>MDM Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.MDMConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMDMConnectionItem()
     * @generated
     */
    int MDM_CONNECTION_ITEM = 27;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ValidationRulesConnectionItemImpl <em>Validation Rules Connection Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ValidationRulesConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getValidationRulesConnectionItem()
     * @generated
     */
    int VALIDATION_RULES_CONNECTION_ITEM = 28;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserProjectAuthorizationImpl <em>User Project Authorization</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.UserProjectAuthorizationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserProjectAuthorization()
     * @generated
     */
    int USER_PROJECT_AUTHORIZATION = 35;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ContextItemImpl <em>Context Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ContextItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getContextItem()
     * @generated
     */
    int CONTEXT_ITEM = 36;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SpagoBiServerImpl <em>Spago Bi Server</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SpagoBiServerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSpagoBiServer()
     * @generated
     */
    int SPAGO_BI_SERVER = 37;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LicenseImpl <em>License</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LicenseImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLicense()
     * @generated
     */
    int LICENSE = 38;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.GenericSchemaConnectionItemImpl <em>Generic Schema Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.GenericSchemaConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getGenericSchemaConnectionItem()
     * @generated
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM = 39;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserModuleAuthorizationImpl <em>User Module Authorization</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.UserModuleAuthorizationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserModuleAuthorization()
     * @generated
     */
    int USER_MODULE_AUTHORIZATION = 40;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LDAPSchemaConnectionItemImpl <em>LDAP Schema Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LDAPSchemaConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLDAPSchemaConnectionItem()
     * @generated
     */
    int LDAP_SCHEMA_CONNECTION_ITEM = 41;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SalesforceSchemaConnectionItemImpl <em>Salesforce Schema Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SalesforceSchemaConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSalesforceSchemaConnectionItem()
     * @generated
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM = 42;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.DashboardConnectionImpl <em>Dashboard Connection</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.DashboardConnectionImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDashboardConnection()
     * @generated
     */
    int DASHBOARD_CONNECTION = 43;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.ExecutionTriggerable <em>Execution Triggerable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.ExecutionTriggerable
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTriggerable()
     * @generated
     */
    int EXECUTION_TRIGGERABLE = 44;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionServerImpl <em>Execution Server</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionServerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionServer()
     * @generated
     */
    int EXECUTION_SERVER = 62;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionTaskImpl <em>Execution Task</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionTaskImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTask()
     * @generated
     */
    int EXECUTION_TASK = 50;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionTaskCmdPrmImpl <em>Execution Task Cmd Prm</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionTaskCmdPrmImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskCmdPrm()
     * @generated
     */
    int EXECUTION_TASK_CMD_PRM = 52;

    int EXEC_STATISTICS_ENABLED = 46;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionTaskJobPrmImpl <em>Execution Task Job Prm</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionTaskJobPrmImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskJobPrm()
     * @generated
     */
    int EXECUTION_TASK_JOB_PRM = 53;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.TalendTriggerImpl <em>Talend Trigger</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.TalendTriggerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTalendTrigger()
     * @generated
     */
    int TALEND_TRIGGER = 56;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.CronTalendTriggerImpl <em>Cron Talend Trigger</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.CronTalendTriggerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCronTalendTrigger()
     * @generated
     */
    int CRON_TALEND_TRIGGER = 57;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.CronUITalendTriggerImpl <em>Cron UI Talend Trigger</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.CronUITalendTriggerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCronUITalendTrigger()
     * @generated
     */
    int CRON_UI_TALEND_TRIGGER = 58;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SimpleTalendTriggerImpl <em>Simple Talend Trigger</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SimpleTalendTriggerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSimpleTalendTrigger()
     * @generated
     */
    int SIMPLE_TALEND_TRIGGER = 59;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionVirtualServerImpl <em>Execution Virtual Server</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionVirtualServerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionVirtualServer()
     * @generated
     */
    int EXECUTION_VIRTUAL_SERVER = 63;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.FileTriggerImpl <em>File Trigger</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.FileTriggerImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileTrigger()
     * @generated
     */
    int FILE_TRIGGER = 60;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.FileTriggerMaskImpl <em>File Trigger Mask</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.FileTriggerMaskImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileTriggerMask()
     * @generated
     */
    int FILE_TRIGGER_MASK = 61;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.JobletProcessItemImpl <em>Joblet Process Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.JobletProcessItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobletProcessItem()
     * @generated
     */
    int JOBLET_PROCESS_ITEM = 64;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.JobDocumentationItemImpl <em>Job Documentation Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.JobDocumentationItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobDocumentationItem()
     * @generated
     */
    int JOB_DOCUMENTATION_ITEM = 65;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.JobletDocumentationItemImpl <em>Joblet Documentation Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.JobletDocumentationItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobletDocumentationItem()
     * @generated
     */
    int JOBLET_DOCUMENTATION_ITEM = 66;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.WSDLSchemaConnectionItemImpl <em>WSDL Schema Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.WSDLSchemaConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getWSDLSchemaConnectionItem()
     * @generated
     */
    int WSDL_SCHEMA_CONNECTION_ITEM = 67;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SchemaInformationImpl <em>Schema Information</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SchemaInformationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSchemaInformation()
     * @generated
     */
    int SCHEMA_INFORMATION = 68;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.InformationImpl <em>Information</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.InformationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getInformation()
     * @generated
     */
    int INFORMATION = 69;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SQLPatternItemImpl <em>SQL Pattern Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SQLPatternItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSQLPatternItem()
     * @generated
     */
    int SQL_PATTERN_ITEM = 70;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ComponentSettingImpl <em>Component Setting</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ComponentSettingImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getComponentSetting()
     * @generated
     */
    int COMPONENT_SETTING = 71;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.TaskExecutionHistoryImpl <em>Task Execution History</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.TaskExecutionHistoryImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTaskExecutionHistory()
     * @generated
     */
    int TASK_EXECUTION_HISTORY = 54;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.StatAndLogsSettingsImpl <em>Stat And Logs Settings</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.StatAndLogsSettingsImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getStatAndLogsSettings()
     * @generated
     */
    int STAT_AND_LOGS_SETTINGS = 72;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ImplicitContextSettingsImpl <em>Implicit Context Settings</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ImplicitContextSettingsImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getImplicitContextSettings()
     * @generated
     */
    int IMPLICIT_CONTEXT_SETTINGS = 73;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SoaOperationImpl <em>Soa Operation</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SoaOperationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaOperation()
     * @generated
     */
    int SOA_OPERATION = 74;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SoaInputParameterImpl <em>Soa Input Parameter</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SoaInputParameterImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaInputParameter()
     * @generated
     */
    int SOA_INPUT_PARAMETER = 75;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SoaServiceImpl <em>Soa Service</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SoaServiceImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaService()
     * @generated
     */
    int SOA_SERVICE = 76;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.RulesItemImpl <em>Rules Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.RulesItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRulesItem()
     * @generated
     */
    int RULES_ITEM = 77;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserRightImpl <em>User Right</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.UserRightImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRight()
     * @generated
     */
    int USER_RIGHT = 78;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.RoleRightImpl <em>Role Right</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.RoleRightImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRoleRight()
     * @generated
     */
    int ROLE_RIGHT = 79;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.SVGBusinessProcessItemImpl <em>SVG Business Process Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.SVGBusinessProcessItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSVGBusinessProcessItem()
     * @generated
     */
    int SVG_BUSINESS_PROCESS_ITEM = 80;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.LinkRulesItemImpl <em>Link Rules Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.LinkRulesItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkRulesItem()
     * @generated
     */
    int LINK_RULES_ITEM = 81;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ItemRelationsImpl <em>Item Relations</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ItemRelationsImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemRelations()
     * @generated
     */
    int ITEM_RELATIONS = 82;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ItemRelationImpl <em>Item Relation</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ItemRelationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemRelation()
     * @generated
     */
    int ITEM_RELATION = 83;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.NotificationImpl <em>Notification</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.NotificationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getNotification()
     * @generated
     */
    int NOTIFICATION = 84;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.HL7ConnectionItemImpl <em>HL7 Connection Item</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.HL7ConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getHL7ConnectionItem()
     * @generated
     */
    int HL7_CONNECTION_ITEM = 85;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionPlanImpl <em>Execution Plan</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionPlanImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlan()
     * @generated
     */
    int EXECUTION_PLAN = 45;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartImpl <em>Execution Plan Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionPlanPartImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPart()
     * @generated
     */
    int EXECUTION_PLAN_PART = 46;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPrmImpl <em>Execution Plan Prm</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionPlanPrmImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPrm()
     * @generated
     */
    int EXECUTION_PLAN_PRM = 47;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartCmdPrmImpl <em>Execution Plan Part Cmd Prm</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionPlanPartCmdPrmImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPartCmdPrm()
     * @generated
     */
    int EXECUTION_PLAN_PART_CMD_PRM = 48;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartJobPrmImpl <em>Execution Plan Part Job Prm</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionPlanPartJobPrmImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPartJobPrm()
     * @generated
     */
    int EXECUTION_PLAN_PART_JOB_PRM = 49;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.PlanExecutionHistoryImpl <em>Plan Execution History</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.PlanExecutionHistoryImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPlanExecutionHistory()
     * @generated
     */
    int PLAN_EXECUTION_HISTORY = 55;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.UserRoleReferenceImpl <em>User Role Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.UserRoleReferenceImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRoleReference()
     * @generated
     */
    int USER_ROLE_REFERENCE = 86;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.HeaderFooterConnectionItemImpl <em>Header Footer Connection Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.HeaderFooterConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getHeaderFooterConnectionItem()
     * @generated
     */
    int HEADER_FOOTER_CONNECTION_ITEM = 87;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.FTPConnectionItemImpl <em>FTP Connection Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.FTPConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFTPConnectionItem()
     * @generated
     */
    int FTP_CONNECTION_ITEM = 88;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.CustomComponentSettingImpl <em>Custom Component Setting</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.CustomComponentSettingImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCustomComponentSetting()
     * @generated
     */
    int CUSTOM_COMPONENT_SETTING = 89;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.TDQItemImpl <em>TDQ Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.TDQItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTDQItem()
     * @generated
     */
    int TDQ_ITEM = 90;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.BRMSConnectionItemImpl <em>BRMS Connection Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.BRMSConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getBRMSConnectionItem()
     * @generated
     */
    int BRMS_CONNECTION_ITEM = 91;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.JobScriptItemImpl <em>Job Script Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.JobScriptItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobScriptItem()
     * @generated
     */
    int JOB_SCRIPT_ITEM = 92;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.EDIFACTConnectionItemImpl <em>EDIFACT Connection Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.EDIFACTConnectionItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getEDIFACTConnectionItem()
     * @generated
     */
    int EDIFACT_CONNECTION_ITEM = 93;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.TechnicalVariableImpl <em>Technical Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.TechnicalVariableImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTechnicalVariable()
     * @generated
     */
    int TECHNICAL_VARIABLE = 94;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ReferenceFileItemImpl <em>Reference File Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ReferenceFileItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getReferenceFileItem()
     * @generated
     */
    int REFERENCE_FILE_ITEM = 95;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__NAME = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__EXTENSION = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_ITEM__CONTENT = ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>File Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Inner Content</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BYTE_ARRAY__INNER_CONTENT = 0;

    /**
     * The number of structural features of the '<em>Byte Array</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int BYTE_ARRAY_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The number of structural features of the '<em>Documentation Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENTATION_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The feature id for the '<em><b>Built In</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__BUILT_IN = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Imports</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__IMPORTS = FILE_ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Package Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM__PACKAGE_TYPE = FILE_ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Routine Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int ROUTINE_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__CONNECTION = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM__TYPE_NAME = ITEM_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECTION_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_VARIABLE__NAME = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_VARIABLE__VALUE = 1;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_VARIABLE__DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_VARIABLE__ID = 3;

    /**
     * The number of structural features of the '<em>Snippet Variable</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_VARIABLE_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__NAME = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Content</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__CONTENT = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Variables</b></em>' reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM__VARIABLES = ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Snippet Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SNIPPET_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Delimited File Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELIMITED_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Positional File Connection Item</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITIONAL_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Reg Ex File Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REG_EX_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__PROPERTY = DELIMITED_FILE_CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__STATE = DELIMITED_FILE_CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__PARENT = DELIMITED_FILE_CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = DELIMITED_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__FILE_EXTENSION = DELIMITED_FILE_CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__NEED_VERSION = DELIMITED_FILE_CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__CONNECTION = DELIMITED_FILE_CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM__TYPE_NAME = DELIMITED_FILE_CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>CSV File Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CSV_FILE_CONNECTION_ITEM_FEATURE_COUNT = DELIMITED_FILE_CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Database Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int DATABASE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>SAP Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SAP_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Xml File Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int XML_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Ldif File Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int LDIF_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Excel File Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEL_FILE_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Ebcdic Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EBCDIC_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>MDM Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int MDM_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Validation Rules Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALIDATION_RULES_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Process</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_ITEM__PROCESS = ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Process Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PROCESS_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE__ID = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE__NAME = 1;

    /**
     * The feature id for the '<em><b>Localized Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE__LOCALIZED_LABEL = 2;

    /**
     * The feature id for the '<em><b>Fixed</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE__FIXED = 3;

    /**
     * The feature id for the '<em><b>Roles Rights</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int USER_ROLE__ROLES_RIGHTS = 4;

    /**
     * The number of structural features of the '<em>User Role</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int USER_ROLE_FEATURE_COUNT = 5;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__ID = 0;

    /**
     * The feature id for the '<em><b>Login</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LOGIN = 1;

    /**
     * The feature id for the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__PASSWORD = 2;

    /**
     * The feature id for the '<em><b>First Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__FIRST_NAME = 3;

    /**
     * The feature id for the '<em><b>Last Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LAST_NAME = 4;

    /**
     * The feature id for the '<em><b>Creation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__CREATION_DATE = 5;

    /**
     * The feature id for the '<em><b>Delete Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__DELETE_DATE = 6;

    /**
     * The feature id for the '<em><b>Deleted</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__DELETED = 7;

    /**
     * The feature id for the '<em><b>Allowed To Modify Components</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__ALLOWED_TO_MODIFY_COMPONENTS = 8;

    /**
     * The feature id for the '<em><b>Comment</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__COMMENT = 9;

    /**
     * The feature id for the '<em><b>Project Authorization</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__PROJECT_AUTHORIZATION = 10;

    /**
     * The feature id for the '<em><b>Module Authorization</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__MODULE_AUTHORIZATION = 11;

    /**
     * The feature id for the '<em><b>Preferred Dashboard Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__PREFERRED_DASHBOARD_CONNECTION = 12;

    /**
     * The feature id for the '<em><b>Last Admin Connection Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LAST_ADMIN_CONNECTION_DATE = 13;

    /**
     * The feature id for the '<em><b>Last Studio Connection Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LAST_STUDIO_CONNECTION_DATE = 14;

    /**
     * The feature id for the '<em><b>First Admin Connection Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__FIRST_ADMIN_CONNECTION_DATE = 15;

    /**
     * The feature id for the '<em><b>First Studio Connection Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__FIRST_STUDIO_CONNECTION_DATE = 16;

    /**
     * The feature id for the '<em><b>Admin Connexion Number</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__ADMIN_CONNEXION_NUMBER = 17;

    /**
     * The feature id for the '<em><b>Studio Connexion Number</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__STUDIO_CONNEXION_NUMBER = 18;

    /**
     * The feature id for the '<em><b>Authentication Info</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int USER__AUTHENTICATION_INFO = 19;

    /**
     * The feature id for the '<em><b>Ldap Login</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LDAP_LOGIN = 20;

    /**
     * The feature id for the '<em><b>Ldap Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LDAP_ID = 21;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__LANGUAGE = 22;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__TYPE = 23;

    /**
     * The feature id for the '<em><b>Additionnal Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__ADDITIONNAL_DATA = 24;

    /**
     * The feature id for the '<em><b>Technical</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER__TECHNICAL = 25;

    /**
     * The number of structural features of the '<em>User</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_FEATURE_COUNT = 26;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Children</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__CHILDREN = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOLDER_ITEM__TYPE = ITEM_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Folder Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FOLDER_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__LABEL = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__VERSION = 2;

    /**
     * The feature id for the '<em><b>Last Update Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int COMPONENT__LAST_UPDATE_DATE = 3;

    /**
     * The feature id for the '<em><b>Author</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__AUTHOR = 4;

    /**
     * The feature id for the '<em><b>Creation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__CREATION_DATE = 5;

    /**
     * The feature id for the '<em><b>Delete Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__DELETE_DATE = 6;

    /**
     * The feature id for the '<em><b>Deleted</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__DELETED = 7;

    /**
     * The feature id for the '<em><b>File Descriptor</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__FILE_DESCRIPTOR = 8;

    /**
     * The feature id for the '<em><b>Projects</b></em>' reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__PROJECTS = 9;

    /**
     * The number of structural features of the '<em>Component</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int COMPONENT_FEATURE_COUNT = 10;

    /**
     * The feature id for the '<em><b>Notation String</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTATION_HOLDER__NOTATION_STRING = 0;

    /**
     * The number of structural features of the '<em>Notation Holder</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTATION_HOLDER_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>User</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_PROJECT_AUTHORIZATION__USER = 0;

    /**
     * The feature id for the '<em><b>Project</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_PROJECT_AUTHORIZATION__PROJECT = 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_PROJECT_AUTHORIZATION__TYPE = 2;

    /**
     * The number of structural features of the '<em>User Project Authorization</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_PROJECT_AUTHORIZATION_FEATURE_COUNT = 3;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Context</b></em>' reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__CONTEXT = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Default Context</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM__DEFAULT_CONTEXT = ITEM_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Context Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CONTEXT_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Engine Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__ENGINE_NAME = 0;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__SHORT_DESCRIPTION = 1;

    /**
     * The feature id for the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__HOST = 2;

    /**
     * The feature id for the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__PORT = 3;

    /**
     * The feature id for the '<em><b>Login</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__LOGIN = 4;

    /**
     * The feature id for the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__PASSWORD = 5;

    /**
     * The feature id for the '<em><b>Application Context</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER__APPLICATION_CONTEXT = 6;

    /**
     * The number of structural features of the '<em>Spago Bi Server</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SPAGO_BI_SERVER_FEATURE_COUNT = 7;

    /**
     * The feature id for the '<em><b>License</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE__LICENSE = 0;

    /**
     * The feature id for the '<em><b>Customer Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE__CUSTOMER_NAME = 1;

    /**
     * The feature id for the '<em><b>Params</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE__PARAMS = 2;

    /**
     * The feature id for the '<em><b>Token</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE__TOKEN = 3;

    /**
     * The feature id for the '<em><b>Date Token Check</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE__DATE_TOKEN_CHECK = 4;

    /**
     * The number of structural features of the '<em>License</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LICENSE_FEATURE_COUNT = 5;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Generic Schema Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GENERIC_SCHEMA_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>User</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_MODULE_AUTHORIZATION__USER = 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_MODULE_AUTHORIZATION__TYPE = 1;

    /**
     * The number of structural features of the '<em>User Module Authorization</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_MODULE_AUTHORIZATION_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>LDAP Schema Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LDAP_SCHEMA_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Salesforce Schema Connection Item</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SALESFORCE_SCHEMA_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__LABEL = 1;

    /**
     * The feature id for the '<em><b>Dialect</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__DIALECT = 2;

    /**
     * The feature id for the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__HOST = 3;

    /**
     * The feature id for the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__PORT = 4;

    /**
     * The feature id for the '<em><b>Database</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__DATABASE = 5;

    /**
     * The feature id for the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__USERNAME = 6;

    /**
     * The feature id for the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__PASSWORD = 7;

    /**
     * The feature id for the '<em><b>Log Table</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__LOG_TABLE = 8;

    /**
     * The feature id for the '<em><b>Stat Table</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__STAT_TABLE = 9;

    /**
     * The feature id for the '<em><b>Flow Meter Table</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__FLOW_METER_TABLE = 10;

    /**
     * The feature id for the '<em><b>Additionnals Params</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__ADDITIONNALS_PARAMS = 11;

    /**
     * The feature id for the '<em><b>Datasource</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION__DATASOURCE = 12;

    /**
     * The number of structural features of the '<em>Dashboard Connection</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int DASHBOARD_CONNECTION_FEATURE_COUNT = 13;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__ID = 0;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__TRIGGERS = 1;

    /**
     * The feature id for the '<em><b>Id Quartz Job</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__ID_QUARTZ_JOB = 2;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__STATUS = 3;

    /**
     * The feature id for the '<em><b>Error Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__ERROR_STATUS = 4;

    /**
     * The feature id for the '<em><b>Concurrent Execution</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__CONCURRENT_EXECUTION = 5;

    /**
     * The feature id for the '<em><b>Processing State</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__PROCESSING_STATE = 6;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE__REQUEST_ID = 7;

    /**
     * The number of structural features of the '<em>Execution Triggerable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TRIGGERABLE_FEATURE_COUNT = 8;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__ID = EXECUTION_TRIGGERABLE__ID;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__TRIGGERS = EXECUTION_TRIGGERABLE__TRIGGERS;

    /**
     * The feature id for the '<em><b>Id Quartz Job</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__ID_QUARTZ_JOB = EXECUTION_TRIGGERABLE__ID_QUARTZ_JOB;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__STATUS = EXECUTION_TRIGGERABLE__STATUS;

    /**
     * The feature id for the '<em><b>Error Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__ERROR_STATUS = EXECUTION_TRIGGERABLE__ERROR_STATUS;

    /**
     * The feature id for the '<em><b>Concurrent Execution</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__CONCURRENT_EXECUTION = EXECUTION_TRIGGERABLE__CONCURRENT_EXECUTION;

    /**
     * The feature id for the '<em><b>Processing State</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__PROCESSING_STATE = EXECUTION_TRIGGERABLE__PROCESSING_STATE;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__REQUEST_ID = EXECUTION_TRIGGERABLE__REQUEST_ID;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__LABEL = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Exec Plan Parts</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__EXEC_PLAN_PARTS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Exec Plan Prms</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__EXEC_PLAN_PRMS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__DESCRIPTION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Execution Plan</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_FEATURE_COUNT = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__ID = 0;

    /**
     * The feature id for the '<em><b>Task</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__TASK = 1;

    /**
     * The feature id for the '<em><b>Execution Plan</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__EXECUTION_PLAN = 2;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__PARENT = 3;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__TYPE = 4;

    /**
     * The feature id for the '<em><b>Jvm Prms</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__JVM_PRMS = 5;

    /**
     * The feature id for the '<em><b>Context Prms</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__CONTEXT_PRMS = 6;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__STATUS = 7;

    /**
     * The feature id for the '<em><b>Start Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__START_DATE = 8;

    /**
     * The feature id for the '<em><b>End Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__END_DATE = 9;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__REQUEST_ID = 10;

    /**
     * The feature id for the '<em><b>Use Parallel</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__USE_PARALLEL = 11;

    /**
     * The feature id for the '<em><b>Max Threads</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART__MAX_THREADS = 12;

    /**
     * The number of structural features of the '<em>Execution Plan Part</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_FEATURE_COUNT = 13;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PRM__ID = 0;

    /**
     * The feature id for the '<em><b>Execution Plan</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PRM__EXECUTION_PLAN = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PRM__NAME = 2;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PRM__VALUE = 3;

    /**
     * The number of structural features of the '<em>Execution Plan Prm</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PRM_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_CMD_PRM__ID = 0;

    /**
     * The feature id for the '<em><b>Execution Plan Part</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_CMD_PRM__EXECUTION_PLAN_PART = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_CMD_PRM__NAME = 2;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_CMD_PRM__VALUE = 3;

    /**
     * The number of structural features of the '<em>Execution Plan Part Cmd Prm</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_CMD_PRM_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__ID = 0;

    /**
     * The feature id for the '<em><b>Execution Plan Part</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__EXECUTION_PLAN_PART = 1;

    /**
     * The feature id for the '<em><b>Override</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__OVERRIDE = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__NAME = 3;

    /**
     * The feature id for the '<em><b>Custom Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__CUSTOM_VALUE = 4;

    /**
     * The feature id for the '<em><b>Part Custom Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM__PART_CUSTOM_VALUE = 5;

    /**
     * The number of structural features of the '<em>Execution Plan Part Job Prm</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_PART_JOB_PRM_FEATURE_COUNT = 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ID = EXECUTION_TRIGGERABLE__ID;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__TRIGGERS = EXECUTION_TRIGGERABLE__TRIGGERS;

    /**
     * The feature id for the '<em><b>Id Quartz Job</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ID_QUARTZ_JOB = EXECUTION_TRIGGERABLE__ID_QUARTZ_JOB;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__STATUS = EXECUTION_TRIGGERABLE__STATUS;

    /**
     * The feature id for the '<em><b>Error Status</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ERROR_STATUS = EXECUTION_TRIGGERABLE__ERROR_STATUS;

    /**
     * The feature id for the '<em><b>Concurrent Execution</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__CONCURRENT_EXECUTION = EXECUTION_TRIGGERABLE__CONCURRENT_EXECUTION;

    /**
     * The feature id for the '<em><b>Processing State</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__PROCESSING_STATE = EXECUTION_TRIGGERABLE__PROCESSING_STATE;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__REQUEST_ID = EXECUTION_TRIGGERABLE__REQUEST_ID;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LABEL = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__DESCRIPTION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Execution Server</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__EXECUTION_SERVER = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Project</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__PROJECT = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Branch</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__BRANCH = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Context</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__CONTEXT = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Job Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__JOB_VERSION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Regenerate Job On Change</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__REGENERATE_JOB_ON_CHANGE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ACTIVE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Last Script Generation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LAST_SCRIPT_GENERATION_DATE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Generated Svn Revision</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__GENERATED_SVN_REVISION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Id Remote Job</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ID_REMOTE_JOB = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Id Remote Job Execution</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ID_REMOTE_JOB_EXECUTION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Checksum Archive</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__CHECKSUM_ARCHIVE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 13;

    /**
     * The feature id for the '<em><b>Job Script Archive Filename</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__JOB_SCRIPT_ARCHIVE_FILENAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 14;

    /**
     * The feature id for the '<em><b>Last Run Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LAST_RUN_DATE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 15;

    /**
     * The feature id for the '<em><b>Last Deployment Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LAST_DEPLOYMENT_DATE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 16;

    /**
     * The feature id for the '<em><b>Last Ended Run Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LAST_ENDED_RUN_DATE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 17;

    /**
     * The feature id for the '<em><b>Cmd Prms</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__CMD_PRMS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 18;

    /**
     * The feature id for the '<em><b>Esb Properties Prms</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ESB_PROPERTIES_PRMS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 19;

    /**
     * The feature id for the '<em><b>Job Prms</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__JOB_PRMS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 20;

    /**
     * The feature id for the '<em><b>Job Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__JOB_ID = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 21;

    /**
     * The feature id for the '<em><b>Virtual Server</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__VIRTUAL_SERVER = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 22;

    /**
     * The feature id for the '<em><b>Max Concurrent Executions</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__MAX_CONCURRENT_EXECUTIONS = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 23;

    /**
     * The feature id for the '<em><b>Generated Project Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__GENERATED_PROJECT_NAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 24;

    /**
     * The feature id for the '<em><b>Generated Job Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__GENERATED_JOB_NAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 25;

    /**
     * The feature id for the '<em><b>Generated Job Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__GENERATED_JOB_VERSION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 26;

    /**
     * The feature id for the '<em><b>Apply Context To Children</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__APPLY_CONTEXT_TO_CHILDREN = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 27;

    /**
     * The feature id for the '<em><b>Error Stack Trace</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ERROR_STACK_TRACE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 28;

    /**
     * The feature id for the '<em><b>Last Triggering Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__LAST_TRIGGERING_DATE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 29;

    /**
     * The feature id for the '<em><b>Exec Statistics Enabled</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__EXEC_STATISTICS_ENABLED = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 30;

    /**
     * The feature id for the '<em><b>Add Statistics Code Enabled</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ADD_STATISTICS_CODE_ENABLED = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 31;

    /**
     * The feature id for the '<em><b>Owner Scheduler Instance Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__OWNER_SCHEDULER_INSTANCE_ID = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 32;

    /**
     * The feature id for the '<em><b>On Unknown State Job</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__ON_UNKNOWN_STATE_JOB = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 33;

    /**
     * The feature id for the '<em><b>Use Latest Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__USE_LATEST_VERSION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 34;

    /**
     * The feature id for the '<em><b>Application Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__APPLICATION_TYPE = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 35;

    /**
     * The feature id for the '<em><b>Repository Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__REPOSITORY_NAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 36;

    /**
     * The feature id for the '<em><b>Features File Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__FEATURES_FILE_URL = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 37;

    /**
     * The feature id for the '<em><b>Feature Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__FEATURE_NAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 38;

    /**
     * The feature id for the '<em><b>Feature Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__FEATURE_VERSION = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 39;

    /**
     * The feature id for the '<em><b>Application Group</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__APPLICATION_GROUP = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 40;

    /**
     * The feature id for the '<em><b>Bundle Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__BUNDLE_NAME = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 41;

    /**
     * The feature id for the '<em><b>Property Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK__PROPERTY_ID = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 42;

    /**
     * The number of structural features of the '<em>Execution Task</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_FEATURE_COUNT = EXECUTION_TRIGGERABLE_FEATURE_COUNT + 43;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExecutionTaskPropertiesImpl <em>Execution Task Properties</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExecutionTaskPropertiesImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskProperties()
     * @generated
     */
    int EXECUTION_TASK_PROPERTIES = 51;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_PROPERTIES__ID = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_PROPERTIES__NAME = 1;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_PROPERTIES__VALUE = 2;

    /**
     * The feature id for the '<em><b>Execution Task</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_PROPERTIES__EXECUTION_TASK = 3;

    /**
     * The number of structural features of the '<em>Execution Task Properties</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_PROPERTIES_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM__ID = 0;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM__ACTIVE = 1;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM__PARAMETER = 2;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM__DESCRIPTION = 3;

    /**
     * The feature id for the '<em><b>Execution Task</b></em>' container reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM__EXECUTION_TASK = 4;

    /**
     * The number of structural features of the '<em>Execution Task Cmd Prm</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_CMD_PRM_FEATURE_COUNT = 5;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__LABEL = 1;

    /**
     * The feature id for the '<em><b>Override</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__OVERRIDE = 2;

    /**
     * The feature id for the '<em><b>Execution Task</b></em>' container reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__EXECUTION_TASK = 3;

    /**
     * The feature id for the '<em><b>Original Value</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__ORIGINAL_VALUE = 4;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__DEFAULT_VALUE = 5;

    /**
     * The feature id for the '<em><b>Item Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM__ITEM_TYPE = 6;

    /**
     * The number of structural features of the '<em>Execution Task Job Prm</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_TASK_JOB_PRM_FEATURE_COUNT = 7;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__ID = 0;

    /**
     * The feature id for the '<em><b>Basic Status</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__BASIC_STATUS = 1;

    /**
     * The feature id for the '<em><b>Detailed Status</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__DETAILED_STATUS = 2;

    /**
     * The feature id for the '<em><b>Task Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TASK_LABEL = 3;

    /**
     * The feature id for the '<em><b>Task Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TASK_DESCRIPTION = 4;

    /**
     * The feature id for the '<em><b>Project Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__PROJECT_NAME = 5;

    /**
     * The feature id for the '<em><b>Talend Job Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TALEND_JOB_NAME = 6;

    /**
     * The feature id for the '<em><b>Talend Job Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TALEND_JOB_ID = 7;

    /**
     * The feature id for the '<em><b>Talend Job Version</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TALEND_JOB_VERSION = 8;

    /**
     * The feature id for the '<em><b>Context Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__CONTEXT_NAME = 9;

    /**
     * The feature id for the '<em><b>Virtual Server Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__VIRTUAL_SERVER_NAME = 10;

    /**
     * The feature id for the '<em><b>Execution Server Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXECUTION_SERVER_NAME = 11;

    /**
     * The feature id for the '<em><b>Execution Server Host</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXECUTION_SERVER_HOST = 12;

    /**
     * The feature id for the '<em><b>Execution Server Cmd Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXECUTION_SERVER_CMD_PORT = 13;

    /**
     * The feature id for the '<em><b>Execution Server File Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXECUTION_SERVER_FILE_PORT = 14;

    /**
     * The feature id for the '<em><b>Execution Server Monitoring Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXECUTION_SERVER_MONITORING_PORT = 15;

    /**
     * The feature id for the '<em><b>Apply Context To Children</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__APPLY_CONTEXT_TO_CHILDREN = 16;

    /**
     * The feature id for the '<em><b>Triggered By</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TRIGGERED_BY = 17;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TRIGGER_TYPE = 18;

    /**
     * The feature id for the '<em><b>Trigger Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TRIGGER_NAME = 19;

    /**
     * The feature id for the '<em><b>Trigger Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TRIGGER_DESCRIPTION = 20;

    /**
     * The feature id for the '<em><b>Task Error Stack Trace</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TASK_ERROR_STACK_TRACE = 21;

    /**
     * The feature id for the '<em><b>Id Quartz Job</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__ID_QUARTZ_JOB = 22;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__ID_QUARTZ_TRIGGER = 23;

    /**
     * The feature id for the '<em><b>Last Job Generation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__LAST_JOB_GENERATION_DATE = 24;

    /**
     * The feature id for the '<em><b>Job Archive Filename</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__JOB_ARCHIVE_FILENAME = 25;

    /**
     * The feature id for the '<em><b>File Trigger File Mask</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_MASK = 26;

    /**
     * The feature id for the '<em><b>File Trigger File Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_NAME = 27;

    /**
     * The feature id for the '<em><b>File Trigger Folder Path</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__FILE_TRIGGER_FOLDER_PATH = 28;

    /**
     * The feature id for the '<em><b>File Trigger Triggered File Path</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__FILE_TRIGGER_TRIGGERED_FILE_PATH = 29;

    /**
     * The feature id for the '<em><b>Expected Triggering Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__EXPECTED_TRIGGERING_DATE = 30;

    /**
     * The feature id for the '<em><b>Task Start Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TASK_START_DATE = 31;

    /**
     * The feature id for the '<em><b>Task End Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__TASK_END_DATE = 32;

    /**
     * The feature id for the '<em><b>Server Job Start Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__SERVER_JOB_START_DATE = 33;

    /**
     * The feature id for the '<em><b>Server Job End Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__SERVER_JOB_END_DATE = 34;

    /**
     * The feature id for the '<em><b>Id Remote Job</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__ID_REMOTE_JOB = 35;

    /**
     * The feature id for the '<em><b>Id Remote Job Execution</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__ID_REMOTE_JOB_EXECUTION = 36;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__REQUEST_ID = 37;

    /**
     * The feature id for the '<em><b>Resuming Mode</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__RESUMING_MODE = 38;

    /**
     * The feature id for the '<em><b>Context Values</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__CONTEXT_VALUES = 39;

    /**
     * The feature id for the '<em><b>Jvm Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__JVM_VALUES = 40;

    /**
     * The feature id for the '<em><b>Parent Task Exec Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__PARENT_TASK_EXEC_ID = 41;

    /**
     * The feature id for the '<em><b>Parent Plan Exec Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY__PARENT_PLAN_EXEC_ID = 42;

    /**
     * The number of structural features of the '<em>Task Execution History</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TASK_EXECUTION_HISTORY_FEATURE_COUNT = 43;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ID = TASK_EXECUTION_HISTORY__ID;

    /**
     * The feature id for the '<em><b>Basic Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__BASIC_STATUS = TASK_EXECUTION_HISTORY__BASIC_STATUS;

    /**
     * The feature id for the '<em><b>Detailed Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__DETAILED_STATUS = TASK_EXECUTION_HISTORY__DETAILED_STATUS;

    /**
     * The feature id for the '<em><b>Task Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TASK_LABEL = TASK_EXECUTION_HISTORY__TASK_LABEL;

    /**
     * The feature id for the '<em><b>Task Description</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TASK_DESCRIPTION = TASK_EXECUTION_HISTORY__TASK_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Project Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__PROJECT_NAME = TASK_EXECUTION_HISTORY__PROJECT_NAME;

    /**
     * The feature id for the '<em><b>Talend Job Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TALEND_JOB_NAME = TASK_EXECUTION_HISTORY__TALEND_JOB_NAME;

    /**
     * The feature id for the '<em><b>Talend Job Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TALEND_JOB_ID = TASK_EXECUTION_HISTORY__TALEND_JOB_ID;

    /**
     * The feature id for the '<em><b>Talend Job Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TALEND_JOB_VERSION = TASK_EXECUTION_HISTORY__TALEND_JOB_VERSION;

    /**
     * The feature id for the '<em><b>Context Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__CONTEXT_NAME = TASK_EXECUTION_HISTORY__CONTEXT_NAME;

    /**
     * The feature id for the '<em><b>Virtual Server Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__VIRTUAL_SERVER_NAME = TASK_EXECUTION_HISTORY__VIRTUAL_SERVER_NAME;

    /**
     * The feature id for the '<em><b>Execution Server Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXECUTION_SERVER_NAME = TASK_EXECUTION_HISTORY__EXECUTION_SERVER_NAME;

    /**
     * The feature id for the '<em><b>Execution Server Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXECUTION_SERVER_HOST = TASK_EXECUTION_HISTORY__EXECUTION_SERVER_HOST;

    /**
     * The feature id for the '<em><b>Execution Server Cmd Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXECUTION_SERVER_CMD_PORT = TASK_EXECUTION_HISTORY__EXECUTION_SERVER_CMD_PORT;

    /**
     * The feature id for the '<em><b>Execution Server File Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXECUTION_SERVER_FILE_PORT = TASK_EXECUTION_HISTORY__EXECUTION_SERVER_FILE_PORT;

    /**
     * The feature id for the '<em><b>Execution Server Monitoring Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXECUTION_SERVER_MONITORING_PORT = TASK_EXECUTION_HISTORY__EXECUTION_SERVER_MONITORING_PORT;

    /**
     * The feature id for the '<em><b>Apply Context To Children</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__APPLY_CONTEXT_TO_CHILDREN = TASK_EXECUTION_HISTORY__APPLY_CONTEXT_TO_CHILDREN;

    /**
     * The feature id for the '<em><b>Triggered By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TRIGGERED_BY = TASK_EXECUTION_HISTORY__TRIGGERED_BY;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TRIGGER_TYPE = TASK_EXECUTION_HISTORY__TRIGGER_TYPE;

    /**
     * The feature id for the '<em><b>Trigger Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TRIGGER_NAME = TASK_EXECUTION_HISTORY__TRIGGER_NAME;

    /**
     * The feature id for the '<em><b>Trigger Description</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TRIGGER_DESCRIPTION = TASK_EXECUTION_HISTORY__TRIGGER_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Task Error Stack Trace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TASK_ERROR_STACK_TRACE = TASK_EXECUTION_HISTORY__TASK_ERROR_STACK_TRACE;

    /**
     * The feature id for the '<em><b>Id Quartz Job</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ID_QUARTZ_JOB = TASK_EXECUTION_HISTORY__ID_QUARTZ_JOB;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ID_QUARTZ_TRIGGER = TASK_EXECUTION_HISTORY__ID_QUARTZ_TRIGGER;

    /**
     * The feature id for the '<em><b>Last Job Generation Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__LAST_JOB_GENERATION_DATE = TASK_EXECUTION_HISTORY__LAST_JOB_GENERATION_DATE;

    /**
     * The feature id for the '<em><b>Job Archive Filename</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__JOB_ARCHIVE_FILENAME = TASK_EXECUTION_HISTORY__JOB_ARCHIVE_FILENAME;

    /**
     * The feature id for the '<em><b>File Trigger File Mask</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__FILE_TRIGGER_FILE_MASK = TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_MASK;

    /**
     * The feature id for the '<em><b>File Trigger File Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__FILE_TRIGGER_FILE_NAME = TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_NAME;

    /**
     * The feature id for the '<em><b>File Trigger Folder Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__FILE_TRIGGER_FOLDER_PATH = TASK_EXECUTION_HISTORY__FILE_TRIGGER_FOLDER_PATH;

    /**
     * The feature id for the '<em><b>File Trigger Triggered File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__FILE_TRIGGER_TRIGGERED_FILE_PATH = TASK_EXECUTION_HISTORY__FILE_TRIGGER_TRIGGERED_FILE_PATH;

    /**
     * The feature id for the '<em><b>Expected Triggering Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__EXPECTED_TRIGGERING_DATE = TASK_EXECUTION_HISTORY__EXPECTED_TRIGGERING_DATE;

    /**
     * The feature id for the '<em><b>Task Start Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TASK_START_DATE = TASK_EXECUTION_HISTORY__TASK_START_DATE;

    /**
     * The feature id for the '<em><b>Task End Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__TASK_END_DATE = TASK_EXECUTION_HISTORY__TASK_END_DATE;

    /**
     * The feature id for the '<em><b>Server Job Start Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__SERVER_JOB_START_DATE = TASK_EXECUTION_HISTORY__SERVER_JOB_START_DATE;

    /**
     * The feature id for the '<em><b>Server Job End Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__SERVER_JOB_END_DATE = TASK_EXECUTION_HISTORY__SERVER_JOB_END_DATE;

    /**
     * The feature id for the '<em><b>Id Remote Job</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ID_REMOTE_JOB = TASK_EXECUTION_HISTORY__ID_REMOTE_JOB;

    /**
     * The feature id for the '<em><b>Id Remote Job Execution</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ID_REMOTE_JOB_EXECUTION = TASK_EXECUTION_HISTORY__ID_REMOTE_JOB_EXECUTION;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__REQUEST_ID = TASK_EXECUTION_HISTORY__REQUEST_ID;

    /**
     * The feature id for the '<em><b>Resuming Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__RESUMING_MODE = TASK_EXECUTION_HISTORY__RESUMING_MODE;

    /**
     * The feature id for the '<em><b>Context Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__CONTEXT_VALUES = TASK_EXECUTION_HISTORY__CONTEXT_VALUES;

    /**
     * The feature id for the '<em><b>Jvm Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__JVM_VALUES = TASK_EXECUTION_HISTORY__JVM_VALUES;

    /**
     * The feature id for the '<em><b>Parent Task Exec Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__PARENT_TASK_EXEC_ID = TASK_EXECUTION_HISTORY__PARENT_TASK_EXEC_ID;

    /**
     * The feature id for the '<em><b>Parent Plan Exec Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__PARENT_PLAN_EXEC_ID = TASK_EXECUTION_HISTORY__PARENT_PLAN_EXEC_ID;

    /**
     * The feature id for the '<em><b>Original Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__ORIGINAL_LABEL = TASK_EXECUTION_HISTORY_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Current Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY__CURRENT_LABEL = TASK_EXECUTION_HISTORY_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Plan Execution History</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PLAN_EXECUTION_HISTORY_FEATURE_COUNT = TASK_EXECUTION_HISTORY_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__ID = 0;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__ACTIVE = 1;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__LABEL = 2;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__DESCRIPTION = 3;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__TRIGGER_TYPE = 4;

    /**
     * The feature id for the '<em><b>Execution Triggerable</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__EXECUTION_TRIGGERABLE = 5;

    /**
     * The feature id for the '<em><b>Start Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__START_TIME = 6;

    /**
     * The feature id for the '<em><b>End Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__END_TIME = 7;

    /**
     * The feature id for the '<em><b>Previous Fire Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__PREVIOUS_FIRE_TIME = 8;

    /**
     * The feature id for the '<em><b>Final Fire Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__FINAL_FIRE_TIME = 9;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__ID_QUARTZ_TRIGGER = 10;

    /**
     * The feature id for the '<em><b>Resume Pause Updated</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__RESUME_PAUSE_UPDATED = 11;

    /**
     * The feature id for the '<em><b>Previously Paused</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER__PREVIOUSLY_PAUSED = 12;

    /**
     * The number of structural features of the '<em>Talend Trigger</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TALEND_TRIGGER_FEATURE_COUNT = 13;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__ID = TALEND_TRIGGER__ID;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__ACTIVE = TALEND_TRIGGER__ACTIVE;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__LABEL = TALEND_TRIGGER__LABEL;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__DESCRIPTION = TALEND_TRIGGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__TRIGGER_TYPE = TALEND_TRIGGER__TRIGGER_TYPE;

    /**
     * The feature id for the '<em><b>Execution Triggerable</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__EXECUTION_TRIGGERABLE = TALEND_TRIGGER__EXECUTION_TRIGGERABLE;

    /**
     * The feature id for the '<em><b>Start Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__START_TIME = TALEND_TRIGGER__START_TIME;

    /**
     * The feature id for the '<em><b>End Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__END_TIME = TALEND_TRIGGER__END_TIME;

    /**
     * The feature id for the '<em><b>Previous Fire Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__PREVIOUS_FIRE_TIME = TALEND_TRIGGER__PREVIOUS_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Final Fire Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__FINAL_FIRE_TIME = TALEND_TRIGGER__FINAL_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__ID_QUARTZ_TRIGGER = TALEND_TRIGGER__ID_QUARTZ_TRIGGER;

    /**
     * The feature id for the '<em><b>Resume Pause Updated</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__RESUME_PAUSE_UPDATED = TALEND_TRIGGER__RESUME_PAUSE_UPDATED;

    /**
     * The feature id for the '<em><b>Previously Paused</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__PREVIOUSLY_PAUSED = TALEND_TRIGGER__PREVIOUSLY_PAUSED;

    /**
     * The feature id for the '<em><b>Cron Expression</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER__CRON_EXPRESSION = TALEND_TRIGGER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Cron Talend Trigger</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_TALEND_TRIGGER_FEATURE_COUNT = TALEND_TRIGGER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__ID = TALEND_TRIGGER__ID;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__ACTIVE = TALEND_TRIGGER__ACTIVE;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LABEL = TALEND_TRIGGER__LABEL;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__DESCRIPTION = TALEND_TRIGGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__TRIGGER_TYPE = TALEND_TRIGGER__TRIGGER_TYPE;

    /**
     * The feature id for the '<em><b>Execution Triggerable</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__EXECUTION_TRIGGERABLE = TALEND_TRIGGER__EXECUTION_TRIGGERABLE;

    /**
     * The feature id for the '<em><b>Start Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__START_TIME = TALEND_TRIGGER__START_TIME;

    /**
     * The feature id for the '<em><b>End Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__END_TIME = TALEND_TRIGGER__END_TIME;

    /**
     * The feature id for the '<em><b>Previous Fire Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__PREVIOUS_FIRE_TIME = TALEND_TRIGGER__PREVIOUS_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Final Fire Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__FINAL_FIRE_TIME = TALEND_TRIGGER__FINAL_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__ID_QUARTZ_TRIGGER = TALEND_TRIGGER__ID_QUARTZ_TRIGGER;

    /**
     * The feature id for the '<em><b>Resume Pause Updated</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__RESUME_PAUSE_UPDATED = TALEND_TRIGGER__RESUME_PAUSE_UPDATED;

    /**
     * The feature id for the '<em><b>Previously Paused</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__PREVIOUSLY_PAUSED = TALEND_TRIGGER__PREVIOUSLY_PAUSED;

    /**
     * The feature id for the '<em><b>List Days Of Week</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_WEEK = TALEND_TRIGGER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>List Days Of Month</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_MONTH = TALEND_TRIGGER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>List Months</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_MONTHS = TALEND_TRIGGER_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>List Years</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_YEARS = TALEND_TRIGGER_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>List Hours</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_HOURS = TALEND_TRIGGER_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>List Minutes</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER__LIST_MINUTES = TALEND_TRIGGER_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Cron UI Talend Trigger</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CRON_UI_TALEND_TRIGGER_FEATURE_COUNT = TALEND_TRIGGER_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__ID = TALEND_TRIGGER__ID;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__ACTIVE = TALEND_TRIGGER__ACTIVE;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__LABEL = TALEND_TRIGGER__LABEL;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__DESCRIPTION = TALEND_TRIGGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__TRIGGER_TYPE = TALEND_TRIGGER__TRIGGER_TYPE;

    /**
     * The feature id for the '<em><b>Execution Triggerable</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__EXECUTION_TRIGGERABLE = TALEND_TRIGGER__EXECUTION_TRIGGERABLE;

    /**
     * The feature id for the '<em><b>Start Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__START_TIME = TALEND_TRIGGER__START_TIME;

    /**
     * The feature id for the '<em><b>End Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__END_TIME = TALEND_TRIGGER__END_TIME;

    /**
     * The feature id for the '<em><b>Previous Fire Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__PREVIOUS_FIRE_TIME = TALEND_TRIGGER__PREVIOUS_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Final Fire Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__FINAL_FIRE_TIME = TALEND_TRIGGER__FINAL_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__ID_QUARTZ_TRIGGER = TALEND_TRIGGER__ID_QUARTZ_TRIGGER;

    /**
     * The feature id for the '<em><b>Resume Pause Updated</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__RESUME_PAUSE_UPDATED = TALEND_TRIGGER__RESUME_PAUSE_UPDATED;

    /**
     * The feature id for the '<em><b>Previously Paused</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__PREVIOUSLY_PAUSED = TALEND_TRIGGER__PREVIOUSLY_PAUSED;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__REPEAT_COUNT = TALEND_TRIGGER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Repeat Interval</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__REPEAT_INTERVAL = TALEND_TRIGGER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Times Triggered</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER__TIMES_TRIGGERED = TALEND_TRIGGER_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Simple Talend Trigger</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_TALEND_TRIGGER_FEATURE_COUNT = TALEND_TRIGGER_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__ID = SIMPLE_TALEND_TRIGGER__ID;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__ACTIVE = SIMPLE_TALEND_TRIGGER__ACTIVE;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__LABEL = SIMPLE_TALEND_TRIGGER__LABEL;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__DESCRIPTION = SIMPLE_TALEND_TRIGGER__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Trigger Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__TRIGGER_TYPE = SIMPLE_TALEND_TRIGGER__TRIGGER_TYPE;

    /**
     * The feature id for the '<em><b>Execution Triggerable</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__EXECUTION_TRIGGERABLE = SIMPLE_TALEND_TRIGGER__EXECUTION_TRIGGERABLE;

    /**
     * The feature id for the '<em><b>Start Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__START_TIME = SIMPLE_TALEND_TRIGGER__START_TIME;

    /**
     * The feature id for the '<em><b>End Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__END_TIME = SIMPLE_TALEND_TRIGGER__END_TIME;

    /**
     * The feature id for the '<em><b>Previous Fire Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__PREVIOUS_FIRE_TIME = SIMPLE_TALEND_TRIGGER__PREVIOUS_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Final Fire Time</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__FINAL_FIRE_TIME = SIMPLE_TALEND_TRIGGER__FINAL_FIRE_TIME;

    /**
     * The feature id for the '<em><b>Id Quartz Trigger</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__ID_QUARTZ_TRIGGER = SIMPLE_TALEND_TRIGGER__ID_QUARTZ_TRIGGER;

    /**
     * The feature id for the '<em><b>Resume Pause Updated</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__RESUME_PAUSE_UPDATED = SIMPLE_TALEND_TRIGGER__RESUME_PAUSE_UPDATED;

    /**
     * The feature id for the '<em><b>Previously Paused</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__PREVIOUSLY_PAUSED = SIMPLE_TALEND_TRIGGER__PREVIOUSLY_PAUSED;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__REPEAT_COUNT = SIMPLE_TALEND_TRIGGER__REPEAT_COUNT;

    /**
     * The feature id for the '<em><b>Repeat Interval</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__REPEAT_INTERVAL = SIMPLE_TALEND_TRIGGER__REPEAT_INTERVAL;

    /**
     * The feature id for the '<em><b>Times Triggered</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__TIMES_TRIGGERED = SIMPLE_TALEND_TRIGGER__TIMES_TRIGGERED;

    /**
     * The feature id for the '<em><b>File Trigger Masks</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER__FILE_TRIGGER_MASKS = SIMPLE_TALEND_TRIGGER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>File Trigger</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_FEATURE_COUNT = SIMPLE_TALEND_TRIGGER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__ID = 0;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__ACTIVE = 1;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__LABEL = 2;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__DESCRIPTION = 3;

    /**
     * The feature id for the '<em><b>File Trigger</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__FILE_TRIGGER = 4;

    /**
     * The feature id for the '<em><b>Folder Path</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__FOLDER_PATH = 5;

    /**
     * The feature id for the '<em><b>File Mask</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__FILE_MASK = 6;

    /**
     * The feature id for the '<em><b>Context Parameter Base Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__CONTEXT_PARAMETER_BASE_NAME = 7;

    /**
     * The feature id for the '<em><b>Check File Server</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__CHECK_FILE_SERVER = 8;

    /**
     * The feature id for the '<em><b>Exist</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__EXIST = 9;

    /**
     * The feature id for the '<em><b>Created</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__CREATED = 10;

    /**
     * The feature id for the '<em><b>Modified</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK__MODIFIED = 11;

    /**
     * The number of structural features of the '<em>File Trigger Mask</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TRIGGER_MASK_FEATURE_COUNT = 12;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__LABEL = 1;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__HOST = 3;

    /**
     * The feature id for the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__PORT = 4;

    /**
     * The feature id for the '<em><b>File Transfert Port</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__FILE_TRANSFERT_PORT = 5;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__ACTIVE = 6;

    /**
     * The feature id for the '<em><b>Monitoring Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__MONITORING_PORT = 7;

    /**
     * The feature id for the '<em><b>Timeout Unknown State</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__TIMEOUT_UNKNOWN_STATE = 8;

    /**
     * The feature id for the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__USERNAME = 9;

    /**
     * The feature id for the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__PASSWORD = 10;

    /**
     * The feature id for the '<em><b>Jmx Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__JMX_URL = 11;

    /**
     * The feature id for the '<em><b>Web Console Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__WEB_CONSOLE_URL = 12;

    /**
     * The feature id for the '<em><b>Talend Runtime</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__TALEND_RUNTIME = 13;

    /**
     * The feature id for the '<em><b>Mgmt Server Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__MGMT_SERVER_PORT = 14;

    /**
     * The feature id for the '<em><b>Mgmt Reg Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__MGMT_REG_PORT = 15;

    /**
     * The feature id for the '<em><b>Admin Console Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__ADMIN_CONSOLE_PORT = 16;

    /**
     * The feature id for the '<em><b>Use SSL</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__USE_SSL = 17;

    /**
     * The feature id for the '<em><b>Instance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER__INSTANCE = 18;

    /**
     * The number of structural features of the '<em>Execution Server</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_SERVER_FEATURE_COUNT = 19;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER__LABEL = 1;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER__DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER__ACTIVE = 3;

    /**
     * The feature id for the '<em><b>Execution Servers</b></em>' reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER__EXECUTION_SERVERS = 4;

    /**
     * The number of structural features of the '<em>Execution Virtual Server</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_VIRTUAL_SERVER_FEATURE_COUNT = 5;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Joblet Process</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__JOBLET_PROCESS = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Icon</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM__ICON = ITEM_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Joblet Process Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_PROCESS_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The number of structural features of the '<em>Job Documentation Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_DOCUMENTATION_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The number of structural features of the '<em>Joblet Documentation Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int JOBLET_DOCUMENTATION_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>WSDL Schema Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_SCHEMA_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_INFORMATION__VERSION = 0;

    /**
     * The number of structural features of the '<em>Schema Information</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_INFORMATION_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFORMATION__LEVEL = 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFORMATION__TYPE = 1;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFORMATION__TEXT = 2;

    /**
     * The number of structural features of the '<em>Information</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int INFORMATION_FEATURE_COUNT = 3;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The feature id for the '<em><b>System</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__SYSTEM = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Elt Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM__ELT_NAME = FILE_ITEM_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>SQL Pattern Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_SETTING__NAME = 0;

    /**
     * The feature id for the '<em><b>Hidden</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_SETTING__HIDDEN = 1;

    /**
     * The feature id for the '<em><b>Family</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_SETTING__FAMILY = 2;

    /**
     * The number of structural features of the '<em>Component Setting</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_SETTING_FEATURE_COUNT = 3;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int STAT_AND_LOGS_SETTINGS__PARAMETERS = 0;

    /**
     * The number of structural features of the '<em>Stat And Logs Settings</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int STAT_AND_LOGS_SETTINGS_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLICIT_CONTEXT_SETTINGS__PARAMETERS = 0;

    /**
     * The number of structural features of the '<em>Implicit Context Settings</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPLICIT_CONTEXT_SETTINGS_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__LABEL = 1;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Project</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__PROJECT = 3;

    /**
     * The feature id for the '<em><b>Context</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__CONTEXT = 4;

    /**
     * The feature id for the '<em><b>Job Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__JOB_VERSION = 5;

    /**
     * The feature id for the '<em><b>Job Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__JOB_NAME = 6;

    /**
     * The feature id for the '<em><b>Active</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__ACTIVE = 7;

    /**
     * The feature id for the '<em><b>Last Script Generation Date</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__LAST_SCRIPT_GENERATION_DATE = 8;

    /**
     * The feature id for the '<em><b>Job Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__JOB_ID = 9;

    /**
     * The feature id for the '<em><b>Apply Context To Children</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__APPLY_CONTEXT_TO_CHILDREN = 10;

    /**
     * The feature id for the '<em><b>Input Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__INPUT_PARAMETERS = 11;

    /**
     * The feature id for the '<em><b>Jvm Parameters</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__JVM_PARAMETERS = 12;

    /**
     * The feature id for the '<em><b>Xms</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__XMS = 13;

    /**
     * The feature id for the '<em><b>Xmx</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__XMX = 14;

    /**
     * The feature id for the '<em><b>Min Job Instances</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SOA_OPERATION__MIN_JOB_INSTANCES = 15;

    /**
     * The feature id for the '<em><b>Max Job Instances</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SOA_OPERATION__MAX_JOB_INSTANCES = 16;

    /**
     * The feature id for the '<em><b>Idle TTL for All Instances</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__IDLE_TTL_FOR_ALL_INSTANCES = 17;

    /**
     * The feature id for the '<em><b>Idle TTL for Additional Instances</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__IDLE_TTL_FOR_ADDITIONAL_INSTANCES = 18;

    /**
     * The feature id for the '<em><b>Queue Max Size</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__QUEUE_MAX_SIZE = 19;

    /**
     * The feature id for the '<em><b>Request In Queue TTL</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__REQUEST_IN_QUEUE_TTL = 20;

    /**
     * The feature id for the '<em><b>Service</b></em>' container reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SOA_OPERATION__SERVICE = 21;

    /**
     * The feature id for the '<em><b>Return Style</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__RETURN_STYLE = 22;

    /**
     * The feature id for the '<em><b>Branch</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION__BRANCH = 23;

    /**
     * The number of structural features of the '<em>Soa Operation</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_OPERATION_FEATURE_COUNT = 24;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__LABEL = 1;

    /**
     * The feature id for the '<em><b>Operation</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__OPERATION = 2;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__DEFAULT_VALUE = 3;

    /**
     * The feature id for the '<em><b>Exposed Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__EXPOSED_NAME = 4;

    /**
     * The feature id for the '<em><b>Exposed</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER__EXPOSED = 5;

    /**
     * The number of structural features of the '<em>Soa Input Parameter</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_INPUT_PARAMETER_FEATURE_COUNT = 6;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__ID = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__LABEL = 1;

    /**
     * The feature id for the '<em><b>Name Space</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__NAME_SPACE = 2;

    /**
     * The feature id for the '<em><b>Contact</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__CONTACT = 3;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__DESCRIPTION = 4;

    /**
     * The feature id for the '<em><b>Creation</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__CREATION = 5;

    /**
     * The feature id for the '<em><b>Modification</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__MODIFICATION = 6;

    /**
     * The feature id for the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__PORT = 7;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__TYPE = 8;

    /**
     * The feature id for the '<em><b>Style</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__STYLE = 9;

    /**
     * The feature id for the '<em><b>Used Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__USED_TYPE = 10;

    /**
     * The feature id for the '<em><b>Param Style</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__PARAM_STYLE = 11;

    /**
     * The feature id for the '<em><b>Operations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__OPERATIONS = 12;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOA_SERVICE__STATUS = 13;

    /**
     * The number of structural features of the '<em>Soa Service</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SOA_SERVICE_FEATURE_COUNT = 14;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RULES_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The number of structural features of the '<em>Rules Item</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int RULES_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_RIGHT__ID = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_RIGHT__NAME = 1;

    /**
     * The feature id for the '<em><b>Description</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_RIGHT__DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Roles Rights</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int USER_RIGHT__ROLES_RIGHTS = 3;

    /**
     * The number of structural features of the '<em>User Right</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int USER_RIGHT_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Role</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROLE_RIGHT__ROLE = 0;

    /**
     * The feature id for the '<em><b>User Right</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROLE_RIGHT__USER_RIGHT = 1;

    /**
     * The number of structural features of the '<em>Role Right</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int ROLE_RIGHT_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The feature id for the '<em><b>Business Process Item</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM__BUSINESS_PROCESS_ITEM = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>SVG Business Process Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_BUSINESS_PROCESS_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__NAME = ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__EXTENSION = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Link</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM__LINK = ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Link Rules Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_RULES_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Base Item</b></em>' containment reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATIONS__BASE_ITEM = 0;

    /**
     * The feature id for the '<em><b>Related Items</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATIONS__RELATED_ITEMS = 1;

    /**
     * The number of structural features of the '<em>Item Relations</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATIONS_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATION__ID = 0;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATION__VERSION = 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATION__TYPE = 2;

    /**
     * The number of structural features of the '<em>Item Relation</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ITEM_RELATION_FEATURE_COUNT = 3;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTIFICATION__ID = 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTIFICATION__TYPE = 1;

    /**
     * The feature id for the '<em><b>Props</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTIFICATION__PROPS = 2;

    /**
     * The feature id for the '<em><b>Enabled</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NOTIFICATION__ENABLED = 3;

    /**
     * The number of structural features of the '<em>Notification</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int NOTIFICATION_FEATURE_COUNT = 4;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>HL7 Connection Item</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int HL7_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>User</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE_REFERENCE__USER = 0;

    /**
     * The feature id for the '<em><b>Role</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE_REFERENCE__ROLE = 1;

    /**
     * The number of structural features of the '<em>User Role Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USER_ROLE_REFERENCE_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>Header Footer Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_FOOTER_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>FTP Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FTP_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CUSTOM_COMPONENT_SETTING__NAME = 0;

    /**
     * The feature id for the '<em><b>Share</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CUSTOM_COMPONENT_SETTING__SHARE = 1;

    /**
     * The number of structural features of the '<em>Custom Component Setting</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CUSTOM_COMPONENT_SETTING_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__PROPERTY = ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__STATE = ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__PARENT = ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__REFERENCE_RESOURCES = ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__FILE_EXTENSION = ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__NEED_VERSION = ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Filename</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM__FILENAME = ITEM_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>TDQ Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TDQ_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>BRMS Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRMS_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__PROPERTY = FILE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__STATE = FILE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__PARENT = FILE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__REFERENCE_RESOURCES = FILE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__FILE_EXTENSION = FILE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__NEED_VERSION = FILE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__NAME = FILE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__EXTENSION = FILE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM__CONTENT = FILE_ITEM__CONTENT;

    /**
     * The number of structural features of the '<em>Job Script Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOB_SCRIPT_ITEM_FEATURE_COUNT = FILE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__PROPERTY = CONNECTION_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__STATE = CONNECTION_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__PARENT = CONNECTION_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__REFERENCE_RESOURCES = CONNECTION_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__FILE_EXTENSION = CONNECTION_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__NEED_VERSION = CONNECTION_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Connection</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__CONNECTION = CONNECTION_ITEM__CONNECTION;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM__TYPE_NAME = CONNECTION_ITEM__TYPE_NAME;

    /**
     * The number of structural features of the '<em>EDIFACT Connection Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDIFACT_CONNECTION_ITEM_FEATURE_COUNT = CONNECTION_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNICAL_VARIABLE__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNICAL_VARIABLE__VALUE = 1;

    /**
     * The number of structural features of the '<em>Technical Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TECHNICAL_VARIABLE_FEATURE_COUNT = 2;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_ITEM__STATE = 0;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_ITEM__PARENT = 1;

    /**
     * The feature id for the '<em><b>Reload From File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_ITEM__RELOAD_FROM_FILE = 2;

    /**
     * The number of structural features of the '<em>Reference Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_ITEM_FEATURE_COUNT = 3;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__STATE = REFERENCE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__PARENT = REFERENCE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reload From File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__RELOAD_FROM_FILE = REFERENCE_ITEM__RELOAD_FROM_FILE;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__NAME = REFERENCE_ITEM_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__EXTENSION = REFERENCE_ITEM_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM__CONTENT = REFERENCE_ITEM_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Reference File Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_FILE_ITEM_FEATURE_COUNT = REFERENCE_ITEM_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ExchangeUserImpl <em>Exchange User</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ExchangeUserImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExchangeUser()
     * @generated
     */
    int EXCHANGE_USER = 97;

    /**
     * The feature id for the '<em><b>Username</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCHANGE_USER__USERNAME = 0;

    /**
     * The feature id for the '<em><b>Login</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCHANGE_USER__LOGIN = 1;

    /**
     * The feature id for the '<em><b>Password</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCHANGE_USER__PASSWORD = 2;

    /**
     * The number of structural features of the '<em>Exchange User</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCHANGE_USER_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.ArtifactNotificationImpl <em>Artifact Notification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.ArtifactNotificationImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getArtifactNotification()
     * @generated
     */
    int ARTIFACT_NOTIFICATION = 98;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT_NOTIFICATION__NAME = 0;

    /**
     * The number of structural features of the '<em>Artifact Notification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARTIFACT_NOTIFICATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.AdditionalInfoMapImpl <em>Additional Info Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.AdditionalInfoMapImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getAdditionalInfoMap()
     * @generated
     */
    int ADDITIONAL_INFO_MAP = 99;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDITIONAL_INFO_MAP__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDITIONAL_INFO_MAP__VALUE = 1;

    /**
     * The number of structural features of the '<em>Additional Info Map</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDITIONAL_INFO_MAP_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.MigrationTaskImpl <em>Migration Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.MigrationTaskImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMigrationTask()
     * @generated
     */
    int MIGRATION_TASK = 100;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATION_TASK__ID = 0;

    /**
     * The feature id for the '<em><b>Breaks</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATION_TASK__BREAKS = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATION_TASK__VERSION = 2;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATION_TASK__STATUS = 3;

    /**
     * The number of structural features of the '<em>Migration Task</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATION_TASK_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.impl.PigudfItemImpl <em>Pigudf Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.impl.PigudfItemImpl
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPigudfItem()
     * @generated
     */
    int PIGUDF_ITEM = 101;

    /**
     * The feature id for the '<em><b>Property</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__PROPERTY = ROUTINE_ITEM__PROPERTY;

    /**
     * The feature id for the '<em><b>State</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__STATE = ROUTINE_ITEM__STATE;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__PARENT = ROUTINE_ITEM__PARENT;

    /**
     * The feature id for the '<em><b>Reference Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__REFERENCE_RESOURCES = ROUTINE_ITEM__REFERENCE_RESOURCES;

    /**
     * The feature id for the '<em><b>File Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__FILE_EXTENSION = ROUTINE_ITEM__FILE_EXTENSION;

    /**
     * The feature id for the '<em><b>Need Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__NEED_VERSION = ROUTINE_ITEM__NEED_VERSION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__NAME = ROUTINE_ITEM__NAME;

    /**
     * The feature id for the '<em><b>Extension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__EXTENSION = ROUTINE_ITEM__EXTENSION;

    /**
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__CONTENT = ROUTINE_ITEM__CONTENT;

    /**
     * The feature id for the '<em><b>Built In</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__BUILT_IN = ROUTINE_ITEM__BUILT_IN;

    /**
     * The feature id for the '<em><b>Imports</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__IMPORTS = ROUTINE_ITEM__IMPORTS;

    /**
     * The feature id for the '<em><b>Package Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM__PACKAGE_TYPE = ROUTINE_ITEM__PACKAGE_TYPE;

    /**
     * The number of structural features of the '<em>Pigudf Item</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PIGUDF_ITEM_FEATURE_COUNT = ROUTINE_ITEM_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.FolderType <em>Folder Type</em>}' enum. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.talend.core.model.properties.FolderType
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFolderType()
     * @generated
     */
    int FOLDER_TYPE = 102;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.UserProjectAuthorizationType <em>User Project Authorization Type</em>}' enum.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.UserProjectAuthorizationType
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserProjectAuthorizationType()
     * @generated
     */
    int USER_PROJECT_AUTHORIZATION_TYPE = 103;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.UserModuleAuthorizationType <em>User Module Authorization Type</em>}' enum.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.UserModuleAuthorizationType
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserModuleAuthorizationType()
     * @generated
     */
    int USER_MODULE_AUTHORIZATION_TYPE = 104;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.InformationLevel <em>Information Level</em>}' enum.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.talend.core.model.properties.InformationLevel
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getInformationLevel()
     * @generated
     */
    int INFORMATION_LEVEL = 105;

    /**
     * The meta object id for the '{@link org.talend.core.model.properties.MigrationStatus <em>Migration Status</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.core.model.properties.MigrationStatus
     * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMigrationStatus()
     * @generated
     */
    int MIGRATION_STATUS = 106;

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Status <em>Status</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Status</em>'.
     * @see org.talend.core.model.properties.Status
     * @generated
     */
    EClass getStatus();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Status#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.Status#getLabel()
     * @see #getStatus()
     * @generated
     */
    EAttribute getStatus_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Status#getCode <em>Code</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Code</em>'.
     * @see org.talend.core.model.properties.Status#getCode()
     * @see #getStatus()
     * @generated
     */
    EAttribute getStatus_Code();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Project <em>Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Project</em>'.
     * @see org.talend.core.model.properties.Project
     * @generated
     */
    EClass getProject();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.Project#getTechnicalStatus <em>Technical Status</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Technical Status</em>'.
     * @see org.talend.core.model.properties.Project#getTechnicalStatus()
     * @see #getProject()
     * @generated
     */
    EReference getProject_TechnicalStatus();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.Project#getDocumentationStatus <em>Documentation Status</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Documentation Status</em>'.
     * @see org.talend.core.model.properties.Project#getDocumentationStatus()
     * @see #getProject()
     * @generated
     */
    EReference getProject_DocumentationStatus();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.Project#getId()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.Project#getLabel()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.Project#getDescription()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see org.talend.core.model.properties.Project#getLanguage()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Language();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Project#getAuthor <em>Author</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Author</em>'.
     * @see org.talend.core.model.properties.Project#getAuthor()
     * @see #getProject()
     * @generated
     */
    EReference getProject_Author();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.Project#getUserAuthorization <em>User Authorization</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>User Authorization</em>'.
     * @see org.talend.core.model.properties.Project#getUserAuthorization()
     * @see #getProject()
     * @generated
     */
    EReference getProject_UserAuthorization();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.Project#getAllowedComponents <em>Allowed Components</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Allowed Components</em>'.
     * @see org.talend.core.model.properties.Project#getAllowedComponents()
     * @see #getProject()
     * @generated
     */
    EReference getProject_AllowedComponents();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.Project#getReferencedProjects <em>Referenced Projects</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Referenced Projects</em>'.
     * @see org.talend.core.model.properties.Project#getReferencedProjects()
     * @see #getProject()
     * @generated
     */
    EReference getProject_ReferencedProjects();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.Project#getAvailableRefProject <em>Available Ref Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Available Ref Project</em>'.
     * @see org.talend.core.model.properties.Project#getAvailableRefProject()
     * @see #getProject()
     * @generated
     */
    EReference getProject_AvailableRefProject();

    /**
     * Returns the meta object for the attribute list '{@link org.talend.core.model.properties.Project#getMigrationTasks <em>Migration Tasks</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Migration Tasks</em>'.
     * @see org.talend.core.model.properties.Project#getMigrationTasks()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_MigrationTasks();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.Project#getMigrationTask <em>Migration Task</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Migration Task</em>'.
     * @see org.talend.core.model.properties.Project#getMigrationTask()
     * @see #getProject()
     * @generated
     */
    EReference getProject_MigrationTask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#isBigData <em>Big Data</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Big Data</em>'.
     * @see org.talend.core.model.properties.Project#isBigData()
     * @see #getProject()
     * @generated
     */
	EAttribute getProject_BigData();

				/**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getMasterJobId <em>Master Job Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Master Job Id</em>'.
     * @see org.talend.core.model.properties.Project#getMasterJobId()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_MasterJobId();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.Project#getSpagoBiServer <em>Spago Bi Server</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Spago Bi Server</em>'.
     * @see org.talend.core.model.properties.Project#getSpagoBiServer()
     * @see #getProject()
     * @generated
     */
    EReference getProject_SpagoBiServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getProductVersion <em>Product Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Product Version</em>'.
     * @see org.talend.core.model.properties.Project#getProductVersion()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_ProductVersion();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.Project#getComponentsSettings <em>Components Settings</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Components Settings</em>'.
     * @see org.talend.core.model.properties.Project#getComponentsSettings()
     * @see #getProject()
     * @generated
     */
    EReference getProject_ComponentsSettings();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getUrl <em>Url</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Url</em>'.
     * @see org.talend.core.model.properties.Project#getUrl()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Url();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.talend.core.model.properties.Project#getStatAndLogsSettings <em>Stat And Logs Settings</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Stat And Logs Settings</em>'.
     * @see org.talend.core.model.properties.Project#getStatAndLogsSettings()
     * @see #getProject()
     * @generated
     */
    EReference getProject_StatAndLogsSettings();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.core.model.properties.Project#getImplicitContextSettings <em>Implicit Context Settings</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Implicit Context Settings</em>'.
     * @see org.talend.core.model.properties.Project#getImplicitContextSettings()
     * @see #getProject()
     * @generated
     */
    EReference getProject_ImplicitContextSettings();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#isHidePassword <em>Hide Password</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hide Password</em>'.
     * @see org.talend.core.model.properties.Project#isHidePassword()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_HidePassword();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.Project#getItemsRelations <em>Items Relations</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Items Relations</em>'.
     * @see org.talend.core.model.properties.Project#getItemsRelations()
     * @see #getProject()
     * @generated
     */
    EReference getProject_ItemsRelations();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#isReference <em>Reference</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see org.talend.core.model.properties.Project#isReference()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Reference();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.Project#getCustomComponentSettings <em>Custom Component Settings</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Custom Component Settings</em>'.
     * @see org.talend.core.model.properties.Project#getCustomComponentSettings()
     * @see #getProject()
     * @generated
     */
    EReference getProject_CustomComponentSettings();

    /**
     * Returns the meta object for the attribute list '{@link org.talend.core.model.properties.Project#getDeletedFolders <em>Deleted Folders</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Deleted Folders</em>'.
     * @see org.talend.core.model.properties.Project#getDeletedFolders()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_DeletedFolders();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.Project#getType()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getItemsRelationVersion <em>Items Relation Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Items Relation Version</em>'.
     * @see org.talend.core.model.properties.Project#getItemsRelationVersion()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_ItemsRelationVersion();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Project#getExchangeUser <em>Exchange User</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Exchange User</em>'.
     * @see org.talend.core.model.properties.Project#getExchangeUser()
     * @see #getProject()
     * @generated
     */
    EReference getProject_ExchangeUser();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getTechnicalLabel <em>Technical Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Technical Label</em>'.
     * @see org.talend.core.model.properties.Project#getTechnicalLabel()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_TechnicalLabel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#isLocal <em>Local</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Local</em>'.
     * @see org.talend.core.model.properties.Project#isLocal()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Local();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.Project#getFolders <em>Folders</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the reference list '<em>Folders</em>'.
     * @see org.talend.core.model.properties.Project#getFolders()
     * @see #getProject()
     * @generated
     */
    EReference getProject_Folders();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#isDeleted <em>Deleted</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Deleted</em>'.
     * @see org.talend.core.model.properties.Project#isDeleted()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_Deleted();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getDeleteDate <em>Delete Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Delete Date</em>'.
     * @see org.talend.core.model.properties.Project#getDeleteDate()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_DeleteDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Project#getCreationDate <em>Creation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation Date</em>'.
     * @see org.talend.core.model.properties.Project#getCreationDate()
     * @see #getProject()
     * @generated
     */
    EAttribute getProject_CreationDate();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Property <em>Property</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Property</em>'.
     * @see org.talend.core.model.properties.Property
     * @generated
     */
    EClass getProperty();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.Property#getId()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.Property#getLabel()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getPurpose <em>Purpose</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Purpose</em>'.
     * @see org.talend.core.model.properties.Property#getPurpose()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Purpose();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.Property#getDescription()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getCreationDate <em>Creation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation Date</em>'.
     * @see org.talend.core.model.properties.Property#getCreationDate()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_CreationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getModificationDate <em>Modification Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Modification Date</em>'.
     * @see org.talend.core.model.properties.Property#getModificationDate()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_ModificationDate();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Property#getAuthor <em>Author</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Author</em>'.
     * @see org.talend.core.model.properties.Property#getAuthor()
     * @see #getProperty()
     * @generated
     */
    EReference getProperty_Author();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.Property#getInformations <em>Informations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Informations</em>'.
     * @see org.talend.core.model.properties.Property#getInformations()
     * @see #getProperty()
     * @generated
     */
    EReference getProperty_Informations();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.Property#getMaxInformationLevel <em>Max Information Level</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Max Information Level</em>'.
     * @see org.talend.core.model.properties.Property#getMaxInformationLevel()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_MaxInformationLevel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getOldStatusCode <em>Old Status Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Old Status Code</em>'.
     * @see org.talend.core.model.properties.Property#getOldStatusCode()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_OldStatusCode();

    /**
     * Returns the meta object for the map '{@link org.talend.core.model.properties.Property#getAdditionalProperties <em>Additional Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Additional Properties</em>'.
     * @see org.talend.core.model.properties.Property#getAdditionalProperties()
     * @see #getProperty()
     * @generated
     */
    EReference getProperty_AdditionalProperties();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getDisplayName <em>Display Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Display Name</em>'.
     * @see org.talend.core.model.properties.Property#getDisplayName()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_DisplayName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.properties.Property#getVersion()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Property#getStatusCode <em>Status Code</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status Code</em>'.
     * @see org.talend.core.model.properties.Property#getStatusCode()
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_StatusCode();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Property#getItem <em>Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Item</em>'.
     * @see org.talend.core.model.properties.Property#getItem()
     * @see #getProperty()
     * @generated
     */
    EReference getProperty_Item();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Item <em>Item</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Item</em>'.
     * @see org.talend.core.model.properties.Item
     * @generated
     */
    EClass getItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Item#getProperty <em>Property</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Property</em>'.
     * @see org.talend.core.model.properties.Item#getProperty()
     * @see #getItem()
     * @generated
     */
    EReference getItem_Property();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Item#getState <em>State</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>State</em>'.
     * @see org.talend.core.model.properties.Item#getState()
     * @see #getItem()
     * @generated
     */
    EReference getItem_State();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Item#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parent</em>'.
     * @see org.talend.core.model.properties.Item#getParent()
     * @see #getItem()
     * @generated
     */
    EReference getItem_Parent();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.Item#getReferenceResources <em>Reference Resources</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Reference Resources</em>'.
     * @see org.talend.core.model.properties.Item#getReferenceResources()
     * @see #getItem()
     * @generated
     */
    EReference getItem_ReferenceResources();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Item#getFileExtension <em>File Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Extension</em>'.
     * @see org.talend.core.model.properties.Item#getFileExtension()
     * @see #getItem()
     * @generated
     */
    EAttribute getItem_FileExtension();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Item#isNeedVersion <em>Need Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Need Version</em>'.
     * @see org.talend.core.model.properties.Item#isNeedVersion()
     * @see #getItem()
     * @generated
     */
    EAttribute getItem_NeedVersion();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.LinkDocumentationItem <em>Link Documentation Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Link Documentation Item</em>'.
     * @see org.talend.core.model.properties.LinkDocumentationItem
     * @generated
     */
    EClass getLinkDocumentationItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.LinkDocumentationItem#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.LinkDocumentationItem#getName()
     * @see #getLinkDocumentationItem()
     * @generated
     */
    EAttribute getLinkDocumentationItem_Name();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.LinkDocumentationItem#getExtension <em>Extension</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Extension</em>'.
     * @see org.talend.core.model.properties.LinkDocumentationItem#getExtension()
     * @see #getLinkDocumentationItem()
     * @generated
     */
    EAttribute getLinkDocumentationItem_Extension();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.LinkDocumentationItem#getLink <em>Link</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Link</em>'.
     * @see org.talend.core.model.properties.LinkDocumentationItem#getLink()
     * @see #getLinkDocumentationItem()
     * @generated
     */
    EReference getLinkDocumentationItem_Link();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.LinkType <em>Link Type</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Link Type</em>'.
     * @see org.talend.core.model.properties.LinkType
     * @generated
     */
    EClass getLinkType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.LinkType#getURI <em>URI</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>URI</em>'.
     * @see org.talend.core.model.properties.LinkType#getURI()
     * @see #getLinkType()
     * @generated
     */
    EAttribute getLinkType_URI();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.LinkType#isState <em>State</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>State</em>'.
     * @see org.talend.core.model.properties.LinkType#isState()
     * @see #getLinkType()
     * @generated
     */
    EAttribute getLinkType_State();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.BusinessProcessItem <em>Business Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Business Process Item</em>'.
     * @see org.talend.core.model.properties.BusinessProcessItem
     * @generated
     */
    EClass getBusinessProcessItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.BusinessProcessItem#getNotation <em>Notation</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Notation</em>'.
     * @see org.talend.core.model.properties.BusinessProcessItem#getNotation()
     * @see #getBusinessProcessItem()
     * @generated
     */
    EReference getBusinessProcessItem_Notation();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.BusinessProcessItem#getSemantic <em>Semantic</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Semantic</em>'.
     * @see org.talend.core.model.properties.BusinessProcessItem#getSemantic()
     * @see #getBusinessProcessItem()
     * @generated
     */
    EReference getBusinessProcessItem_Semantic();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.BusinessProcessItem#getNotationHolder <em>Notation Holder</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Notation Holder</em>'.
     * @see org.talend.core.model.properties.BusinessProcessItem#getNotationHolder()
     * @see #getBusinessProcessItem()
     * @generated
     */
    EReference getBusinessProcessItem_NotationHolder();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.BusinessProcessItem#getSvgBusinessProcessItem <em>Svg Business Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Svg Business Process Item</em>'.
     * @see org.talend.core.model.properties.BusinessProcessItem#getSvgBusinessProcessItem()
     * @see #getBusinessProcessItem()
     * @generated
     */
    EReference getBusinessProcessItem_SvgBusinessProcessItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ItemState <em>Item State</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Item State</em>'.
     * @see org.talend.core.model.properties.ItemState
     * @generated
     */
    EClass getItemState();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemState#getPath <em>Path</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Path</em>'.
     * @see org.talend.core.model.properties.ItemState#getPath()
     * @see #getItemState()
     * @generated
     */
    EAttribute getItemState_Path();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemState#isDeleted <em>Deleted</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Deleted</em>'.
     * @see org.talend.core.model.properties.ItemState#isDeleted()
     * @see #getItemState()
     * @generated
     */
    EAttribute getItemState_Deleted();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemState#isLocked <em>Locked</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Locked</em>'.
     * @see org.talend.core.model.properties.ItemState#isLocked()
     * @see #getItemState()
     * @generated
     */
    EAttribute getItemState_Locked();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ItemState#getLocker <em>Locker</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Locker</em>'.
     * @see org.talend.core.model.properties.ItemState#getLocker()
     * @see #getItemState()
     * @generated
     */
    EReference getItemState_Locker();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemState#getLockDate <em>Lock Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lock Date</em>'.
     * @see org.talend.core.model.properties.ItemState#getLockDate()
     * @see #getItemState()
     * @generated
     */
    EAttribute getItemState_LockDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemState#getCommitDate <em>Commit Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Commit Date</em>'.
     * @see org.talend.core.model.properties.ItemState#getCommitDate()
     * @see #getItemState()
     * @generated
     */
    EAttribute getItemState_CommitDate();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.FileItem <em>File Item</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>File Item</em>'.
     * @see org.talend.core.model.properties.FileItem
     * @generated
     */
    EClass getFileItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileItem#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.FileItem#getName()
     * @see #getFileItem()
     * @generated
     */
    EAttribute getFileItem_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileItem#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extension</em>'.
     * @see org.talend.core.model.properties.FileItem#getExtension()
     * @see #getFileItem()
     * @generated
     */
    EAttribute getFileItem_Extension();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.FileItem#getContent <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Content</em>'.
     * @see org.talend.core.model.properties.FileItem#getContent()
     * @see #getFileItem()
     * @generated
     */
    EReference getFileItem_Content();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.DocumentationItem <em>Documentation Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Documentation Item</em>'.
     * @see org.talend.core.model.properties.DocumentationItem
     * @generated
     */
    EClass getDocumentationItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.RoutineItem <em>Routine Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Routine Item</em>'.
     * @see org.talend.core.model.properties.RoutineItem
     * @generated
     */
    EClass getRoutineItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.RoutineItem#isBuiltIn <em>Built In</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Built In</em>'.
     * @see org.talend.core.model.properties.RoutineItem#isBuiltIn()
     * @see #getRoutineItem()
     * @generated
     */
    EAttribute getRoutineItem_BuiltIn();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.RoutineItem#getImports <em>Imports</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the containment reference list '<em>Imports</em>'.
     * @see org.talend.core.model.properties.RoutineItem#getImports()
     * @see #getRoutineItem()
     * @generated
     */
    EReference getRoutineItem_Imports();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.RoutineItem#getPackageType <em>Package Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Package Type</em>'.
     * @see org.talend.core.model.properties.RoutineItem#getPackageType()
     * @see #getRoutineItem()
     * @generated
     */
    EAttribute getRoutineItem_PackageType();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ByteArray <em>Byte Array</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Byte Array</em>'.
     * @see org.talend.core.model.properties.ByteArray
     * @generated
     */
    EClass getByteArray();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ByteArray#getInnerContent <em>Inner Content</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Inner Content</em>'.
     * @see org.talend.core.model.properties.ByteArray#getInnerContent()
     * @see #getByteArray()
     * @generated
     */
    EAttribute getByteArray_InnerContent();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ConnectionItem <em>Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Connection Item</em>'.
     * @see org.talend.core.model.properties.ConnectionItem
     * @generated
     */
    EClass getConnectionItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ConnectionItem#getConnection <em>Connection</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Connection</em>'.
     * @see org.talend.core.model.properties.ConnectionItem#getConnection()
     * @see #getConnectionItem()
     * @generated
     */
    EReference getConnectionItem_Connection();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ConnectionItem#getTypeName <em>Type Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type Name</em>'.
     * @see org.talend.core.model.properties.ConnectionItem#getTypeName()
     * @see #getConnectionItem()
     * @generated
     */
    EAttribute getConnectionItem_TypeName();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SnippetVariable <em>Snippet Variable</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Snippet Variable</em>'.
     * @see org.talend.core.model.properties.SnippetVariable
     * @generated
     */
    EClass getSnippetVariable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SnippetVariable#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.SnippetVariable#getName()
     * @see #getSnippetVariable()
     * @generated
     */
    EAttribute getSnippetVariable_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SnippetVariable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.core.model.properties.SnippetVariable#getValue()
     * @see #getSnippetVariable()
     * @generated
     */
    EAttribute getSnippetVariable_Value();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SnippetVariable#getDescription <em>Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.SnippetVariable#getDescription()
     * @see #getSnippetVariable()
     * @generated
     */
    EAttribute getSnippetVariable_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SnippetVariable#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.SnippetVariable#getId()
     * @see #getSnippetVariable()
     * @generated
     */
    EAttribute getSnippetVariable_Id();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SnippetItem <em>Snippet Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Snippet Item</em>'.
     * @see org.talend.core.model.properties.SnippetItem
     * @generated
     */
    EClass getSnippetItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SnippetItem#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.SnippetItem#getName()
     * @see #getSnippetItem()
     * @generated
     */
    EAttribute getSnippetItem_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SnippetItem#getContent <em>Content</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Content</em>'.
     * @see org.talend.core.model.properties.SnippetItem#getContent()
     * @see #getSnippetItem()
     * @generated
     */
    EAttribute getSnippetItem_Content();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.SnippetItem#getVariables <em>Variables</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Variables</em>'.
     * @see org.talend.core.model.properties.SnippetItem#getVariables()
     * @see #getSnippetItem()
     * @generated
     */
    EReference getSnippetItem_Variables();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.DelimitedFileConnectionItem <em>Delimited File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Delimited File Connection Item</em>'.
     * @see org.talend.core.model.properties.DelimitedFileConnectionItem
     * @generated
     */
    EClass getDelimitedFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.PositionalFileConnectionItem <em>Positional File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Positional File Connection Item</em>'.
     * @see org.talend.core.model.properties.PositionalFileConnectionItem
     * @generated
     */
    EClass getPositionalFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.RegExFileConnectionItem <em>Reg Ex File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Reg Ex File Connection Item</em>'.
     * @see org.talend.core.model.properties.RegExFileConnectionItem
     * @generated
     */
    EClass getRegExFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.CSVFileConnectionItem <em>CSV File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>CSV File Connection Item</em>'.
     * @see org.talend.core.model.properties.CSVFileConnectionItem
     * @generated
     */
    EClass getCSVFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.DatabaseConnectionItem <em>Database Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Database Connection Item</em>'.
     * @see org.talend.core.model.properties.DatabaseConnectionItem
     * @generated
     */
    EClass getDatabaseConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SAPConnectionItem <em>SAP Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>SAP Connection Item</em>'.
     * @see org.talend.core.model.properties.SAPConnectionItem
     * @generated
     */
    EClass getSAPConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ProcessItem <em>Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Item</em>'.
     * @see org.talend.core.model.properties.ProcessItem
     * @generated
     */
    EClass getProcessItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ProcessItem#getProcess <em>Process</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Process</em>'.
     * @see org.talend.core.model.properties.ProcessItem#getProcess()
     * @see #getProcessItem()
     * @generated
     */
    EReference getProcessItem_Process();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.User <em>User</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>User</em>'.
     * @see org.talend.core.model.properties.User
     * @generated
     */
    EClass getUser();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getId <em>Id</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.User#getId()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLogin <em>Login</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Login</em>'.
     * @see org.talend.core.model.properties.User#getLogin()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Login();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getPassword <em>Password</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Password</em>'.
     * @see org.talend.core.model.properties.User#getPassword()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Password();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getFirstName <em>First Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>First Name</em>'.
     * @see org.talend.core.model.properties.User#getFirstName()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_FirstName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLastName <em>Last Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Name</em>'.
     * @see org.talend.core.model.properties.User#getLastName()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_LastName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getCreationDate <em>Creation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation Date</em>'.
     * @see org.talend.core.model.properties.User#getCreationDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_CreationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getDeleteDate <em>Delete Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Delete Date</em>'.
     * @see org.talend.core.model.properties.User#getDeleteDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_DeleteDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#isDeleted <em>Deleted</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Deleted</em>'.
     * @see org.talend.core.model.properties.User#isDeleted()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Deleted();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#isAllowedToModifyComponents <em>Allowed To Modify Components</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Allowed To Modify Components</em>'.
     * @see org.talend.core.model.properties.User#isAllowedToModifyComponents()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_AllowedToModifyComponents();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getComment <em>Comment</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Comment</em>'.
     * @see org.talend.core.model.properties.User#getComment()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Comment();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.User#getProjectAuthorization <em>Project Authorization</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Project Authorization</em>'.
     * @see org.talend.core.model.properties.User#getProjectAuthorization()
     * @see #getUser()
     * @generated
     */
    EReference getUser_ProjectAuthorization();

    /**
     * Returns the meta object for the reference list '
     * {@link org.talend.core.model.properties.User#getModuleAuthorization <em>Module Authorization</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Module Authorization</em>'.
     * @see org.talend.core.model.properties.User#getModuleAuthorization()
     * @see #getUser()
     * @generated
     */
    EReference getUser_ModuleAuthorization();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.User#getPreferredDashboardConnection <em>Preferred Dashboard Connection</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Preferred Dashboard Connection</em>'.
     * @see org.talend.core.model.properties.User#getPreferredDashboardConnection()
     * @see #getUser()
     * @generated
     */
    EReference getUser_PreferredDashboardConnection();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLastAdminConnectionDate <em>Last Admin Connection Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Admin Connection Date</em>'.
     * @see org.talend.core.model.properties.User#getLastAdminConnectionDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_LastAdminConnectionDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLastStudioConnectionDate <em>Last Studio Connection Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Studio Connection Date</em>'.
     * @see org.talend.core.model.properties.User#getLastStudioConnectionDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_LastStudioConnectionDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getFirstAdminConnectionDate <em>First Admin Connection Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>First Admin Connection Date</em>'.
     * @see org.talend.core.model.properties.User#getFirstAdminConnectionDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_FirstAdminConnectionDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getFirstStudioConnectionDate <em>First Studio Connection Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>First Studio Connection Date</em>'.
     * @see org.talend.core.model.properties.User#getFirstStudioConnectionDate()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_FirstStudioConnectionDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getAdminConnexionNumber <em>Admin Connexion Number</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Admin Connexion Number</em>'.
     * @see org.talend.core.model.properties.User#getAdminConnexionNumber()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_AdminConnexionNumber();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getStudioConnexionNumber <em>Studio Connexion Number</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Studio Connexion Number</em>'.
     * @see org.talend.core.model.properties.User#getStudioConnexionNumber()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_StudioConnexionNumber();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getAuthenticationInfo <em>Authentication Info</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Authentication Info</em>'.
     * @see org.talend.core.model.properties.User#getAuthenticationInfo()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_AuthenticationInfo();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLdapLogin <em>Ldap Login</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Ldap Login</em>'.
     * @see org.talend.core.model.properties.User#getLdapLogin()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_LdapLogin();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLdapId <em>Ldap Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Ldap Id</em>'.
     * @see org.talend.core.model.properties.User#getLdapId()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_LdapId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see org.talend.core.model.properties.User#getLanguage()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Language();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.User#getType()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#getAdditionnalData <em>Additionnal Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Additionnal Data</em>'.
     * @see org.talend.core.model.properties.User#getAdditionnalData()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_AdditionnalData();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.User#isTechnical <em>Technical</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Technical</em>'.
     * @see org.talend.core.model.properties.User#isTechnical()
     * @see #getUser()
     * @generated
     */
    EAttribute getUser_Technical();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.FolderItem <em>Folder Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Folder Item</em>'.
     * @see org.talend.core.model.properties.FolderItem
     * @generated
     */
    EClass getFolderItem();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.FolderItem#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the reference list '<em>Children</em>'.
     * @see org.talend.core.model.properties.FolderItem#getChildren()
     * @see #getFolderItem()
     * @generated
     */
    EReference getFolderItem_Children();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FolderItem#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.FolderItem#getType()
     * @see #getFolderItem()
     * @generated
     */
    EAttribute getFolderItem_Type();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Component <em>Component</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Component</em>'.
     * @see org.talend.core.model.properties.Component
     * @generated
     */
    EClass getComponent();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.Component#getId()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.Component#getLabel()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.properties.Component#getVersion()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getLastUpdateDate <em>Last Update Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Update Date</em>'.
     * @see org.talend.core.model.properties.Component#getLastUpdateDate()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_LastUpdateDate();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.Component#getProjects <em>Projects</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Projects</em>'.
     * @see org.talend.core.model.properties.Component#getProjects()
     * @see #getComponent()
     * @generated
     */
    EReference getComponent_Projects();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.Component#getAuthor <em>Author</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Author</em>'.
     * @see org.talend.core.model.properties.Component#getAuthor()
     * @see #getComponent()
     * @generated
     */
    EReference getComponent_Author();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getCreationDate <em>Creation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation Date</em>'.
     * @see org.talend.core.model.properties.Component#getCreationDate()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_CreationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getDeleteDate <em>Delete Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Delete Date</em>'.
     * @see org.talend.core.model.properties.Component#getDeleteDate()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_DeleteDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#isDeleted <em>Deleted</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Deleted</em>'.
     * @see org.talend.core.model.properties.Component#isDeleted()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Deleted();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Component#getFileDescriptor <em>File Descriptor</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Descriptor</em>'.
     * @see org.talend.core.model.properties.Component#getFileDescriptor()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_FileDescriptor();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.XmlFileConnectionItem <em>Xml File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Xml File Connection Item</em>'.
     * @see org.talend.core.model.properties.XmlFileConnectionItem
     * @generated
     */
    EClass getXmlFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.NotationHolder <em>Notation Holder</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Notation Holder</em>'.
     * @see org.talend.core.model.properties.NotationHolder
     * @generated
     */
    EClass getNotationHolder();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.NotationHolder#getNotationString <em>Notation String</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Notation String</em>'.
     * @see org.talend.core.model.properties.NotationHolder#getNotationString()
     * @see #getNotationHolder()
     * @generated
     */
    EAttribute getNotationHolder_NotationString();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ProjectComponentAuthorisation <em>Project Component Authorisation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Project Component Authorisation</em>'.
     * @see org.talend.core.model.properties.ProjectComponentAuthorisation
     * @generated
     */
    EClass getProjectComponentAuthorisation();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.ProjectComponentAuthorisation#getProject <em>Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Project</em>'.
     * @see org.talend.core.model.properties.ProjectComponentAuthorisation#getProject()
     * @see #getProjectComponentAuthorisation()
     * @generated
     */
    EReference getProjectComponentAuthorisation_Project();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.ProjectComponentAuthorisation#getComponent <em>Component</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Component</em>'.
     * @see org.talend.core.model.properties.ProjectComponentAuthorisation#getComponent()
     * @see #getProjectComponentAuthorisation()
     * @generated
     */
    EReference getProjectComponentAuthorisation_Component();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ProjectReference <em>Project Reference</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Project Reference</em>'.
     * @see org.talend.core.model.properties.ProjectReference
     * @generated
     */
    EClass getProjectReference();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ProjectReference#getProject <em>Project</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Project</em>'.
     * @see org.talend.core.model.properties.ProjectReference#getProject()
     * @see #getProjectReference()
     * @generated
     */
    EReference getProjectReference_Project();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ProjectReference#getBranch <em>Branch</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Branch</em>'.
     * @see org.talend.core.model.properties.ProjectReference#getBranch()
     * @see #getProjectReference()
     * @generated
     */
    EAttribute getProjectReference_Branch();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.ProjectReference#getReferencedProject <em>Referenced Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Referenced Project</em>'.
     * @see org.talend.core.model.properties.ProjectReference#getReferencedProject()
     * @see #getProjectReference()
     * @generated
     */
    EReference getProjectReference_ReferencedProject();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ProjectReference#getReferencedBranch <em>Referenced Branch</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Referenced Branch</em>'.
     * @see org.talend.core.model.properties.ProjectReference#getReferencedBranch()
     * @see #getProjectReference()
     * @generated
     */
    EAttribute getProjectReference_ReferencedBranch();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.LdifFileConnectionItem <em>Ldif File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Ldif File Connection Item</em>'.
     * @see org.talend.core.model.properties.LdifFileConnectionItem
     * @generated
     */
    EClass getLdifFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExcelFileConnectionItem <em>Excel File Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Excel File Connection Item</em>'.
     * @see org.talend.core.model.properties.ExcelFileConnectionItem
     * @generated
     */
    EClass getExcelFileConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.EbcdicConnectionItem <em>Ebcdic Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Ebcdic Connection Item</em>'.
     * @see org.talend.core.model.properties.EbcdicConnectionItem
     * @generated
     */
    EClass getEbcdicConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.MDMConnectionItem <em>MDM Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>MDM Connection Item</em>'.
     * @see org.talend.core.model.properties.MDMConnectionItem
     * @generated
     */
    EClass getMDMConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ValidationRulesConnectionItem <em>Validation Rules Connection Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Validation Rules Connection Item</em>'.
     * @see org.talend.core.model.properties.ValidationRulesConnectionItem
     * @generated
     */
    EClass getValidationRulesConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.UserProjectAuthorization <em>User Project Authorization</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>User Project Authorization</em>'.
     * @see org.talend.core.model.properties.UserProjectAuthorization
     * @generated
     */
    EClass getUserProjectAuthorization();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.UserProjectAuthorization#getUser <em>User</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>User</em>'.
     * @see org.talend.core.model.properties.UserProjectAuthorization#getUser()
     * @see #getUserProjectAuthorization()
     * @generated
     */
    EReference getUserProjectAuthorization_User();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.UserProjectAuthorization#getProject <em>Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Project</em>'.
     * @see org.talend.core.model.properties.UserProjectAuthorization#getProject()
     * @see #getUserProjectAuthorization()
     * @generated
     */
    EReference getUserProjectAuthorization_Project();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserProjectAuthorization#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.UserProjectAuthorization#getType()
     * @see #getUserProjectAuthorization()
     * @generated
     */
    EAttribute getUserProjectAuthorization_Type();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ContextItem <em>Context Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Context Item</em>'.
     * @see org.talend.core.model.properties.ContextItem
     * @generated
     */
    EClass getContextItem();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.ContextItem#getContext <em>Context</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Context</em>'.
     * @see org.talend.core.model.properties.ContextItem#getContext()
     * @see #getContextItem()
     * @generated
     */
    EReference getContextItem_Context();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ContextItem#getDefaultContext <em>Default Context</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default Context</em>'.
     * @see org.talend.core.model.properties.ContextItem#getDefaultContext()
     * @see #getContextItem()
     * @generated
     */
    EAttribute getContextItem_DefaultContext();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SpagoBiServer <em>Spago Bi Server</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Spago Bi Server</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer
     * @generated
     */
    EClass getSpagoBiServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SpagoBiServer#getEngineName <em>Engine Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Engine Name</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getEngineName()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_EngineName();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SpagoBiServer#getShortDescription <em>Short Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Short Description</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getShortDescription()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_ShortDescription();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SpagoBiServer#getHost <em>Host</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Host</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getHost()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_Host();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SpagoBiServer#getPort <em>Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Port</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getPort()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_Port();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SpagoBiServer#getLogin <em>Login</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Login</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getLogin()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_Login();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SpagoBiServer#getPassword <em>Password</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Password</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getPassword()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_Password();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SpagoBiServer#getApplicationContext <em>Application Context</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Application Context</em>'.
     * @see org.talend.core.model.properties.SpagoBiServer#getApplicationContext()
     * @see #getSpagoBiServer()
     * @generated
     */
    EAttribute getSpagoBiServer_ApplicationContext();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.License <em>License</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>License</em>'.
     * @see org.talend.core.model.properties.License
     * @generated
     */
    EClass getLicense();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.License#getLicense <em>License</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>License</em>'.
     * @see org.talend.core.model.properties.License#getLicense()
     * @see #getLicense()
     * @generated
     */
    EAttribute getLicense_License();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.License#getCustomerName <em>Customer Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Customer Name</em>'.
     * @see org.talend.core.model.properties.License#getCustomerName()
     * @see #getLicense()
     * @generated
     */
    EAttribute getLicense_CustomerName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.License#getParams <em>Params</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Params</em>'.
     * @see org.talend.core.model.properties.License#getParams()
     * @see #getLicense()
     * @generated
     */
    EAttribute getLicense_Params();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.License#getToken <em>Token</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Token</em>'.
     * @see org.talend.core.model.properties.License#getToken()
     * @see #getLicense()
     * @generated
     */
    EAttribute getLicense_Token();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.License#getDateTokenCheck <em>Date Token Check</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Date Token Check</em>'.
     * @see org.talend.core.model.properties.License#getDateTokenCheck()
     * @see #getLicense()
     * @generated
     */
    EAttribute getLicense_DateTokenCheck();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.GenericSchemaConnectionItem <em>Generic Schema Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Generic Schema Connection Item</em>'.
     * @see org.talend.core.model.properties.GenericSchemaConnectionItem
     * @generated
     */
    EClass getGenericSchemaConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.UserModuleAuthorization <em>User Module Authorization</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>User Module Authorization</em>'.
     * @see org.talend.core.model.properties.UserModuleAuthorization
     * @generated
     */
    EClass getUserModuleAuthorization();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.UserModuleAuthorization#getUser <em>User</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>User</em>'.
     * @see org.talend.core.model.properties.UserModuleAuthorization#getUser()
     * @see #getUserModuleAuthorization()
     * @generated
     */
    EReference getUserModuleAuthorization_User();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserModuleAuthorization#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.UserModuleAuthorization#getType()
     * @see #getUserModuleAuthorization()
     * @generated
     */
    EAttribute getUserModuleAuthorization_Type();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.LDAPSchemaConnectionItem <em>LDAP Schema Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>LDAP Schema Connection Item</em>'.
     * @see org.talend.core.model.properties.LDAPSchemaConnectionItem
     * @generated
     */
    EClass getLDAPSchemaConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SalesforceSchemaConnectionItem <em>Salesforce Schema Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Salesforce Schema Connection Item</em>'.
     * @see org.talend.core.model.properties.SalesforceSchemaConnectionItem
     * @generated
     */
    EClass getSalesforceSchemaConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.DashboardConnection <em>Dashboard Connection</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Dashboard Connection</em>'.
     * @see org.talend.core.model.properties.DashboardConnection
     * @generated
     */
    EClass getDashboardConnection();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getId()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getLabel()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getDialect <em>Dialect</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dialect</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getDialect()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Dialect();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getHost <em>Host</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Host</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getHost()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Host();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getPort <em>Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Port</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getPort()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Port();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getDatabase <em>Database</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Database</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getDatabase()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Database();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getUsername <em>Username</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Username</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getUsername()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Username();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getPassword <em>Password</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Password</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getPassword()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Password();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getLogTable <em>Log Table</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Log Table</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getLogTable()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_LogTable();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.DashboardConnection#getStatTable <em>Stat Table</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Stat Table</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getStatTable()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_StatTable();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.DashboardConnection#getFlowMeterTable <em>Flow Meter Table</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Flow Meter Table</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getFlowMeterTable()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_FlowMeterTable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.DashboardConnection#getAdditionnalsParams <em>Additionnals Params</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Additionnals Params</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getAdditionnalsParams()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_AdditionnalsParams();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.DashboardConnection#getDatasource <em>Datasource</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Datasource</em>'.
     * @see org.talend.core.model.properties.DashboardConnection#getDatasource()
     * @see #getDashboardConnection()
     * @generated
     */
    EAttribute getDashboardConnection_Datasource();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionTriggerable <em>Execution Triggerable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Triggerable</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable
     * @generated
     */
    EClass getExecutionTriggerable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getId()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_Id();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionTriggerable#getTriggers <em>Triggers</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Triggers</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getTriggers()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EReference getExecutionTriggerable_Triggers();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#getIdQuartzJob <em>Id Quartz Job</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id Quartz Job</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getIdQuartzJob()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_IdQuartzJob();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#getStatus <em>Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getStatus()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_Status();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#getErrorStatus <em>Error Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Error Status</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getErrorStatus()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_ErrorStatus();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#isConcurrentExecution <em>Concurrent Execution</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Concurrent Execution</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#isConcurrentExecution()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_ConcurrentExecution();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#isProcessingState <em>Processing State</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Processing State</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#isProcessingState()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_ProcessingState();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTriggerable#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTriggerable#getRequestId()
     * @see #getExecutionTriggerable()
     * @generated
     */
    EAttribute getExecutionTriggerable_RequestId();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionServer <em>Execution Server</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Server</em>'.
     * @see org.talend.core.model.properties.ExecutionServer
     * @generated
     */
    EClass getExecutionServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getId()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getLabel()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Label();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionServer#getDescription <em>Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getDescription()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getHost <em>Host</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Host</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getHost()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Host();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getPort <em>Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getPort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Port();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionServer#getFileTransfertPort <em>File Transfert Port</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>File Transfert Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getFileTransfertPort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_FileTransfertPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#isActive()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Active();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionServer#getMonitoringPort <em>Monitoring Port</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Monitoring Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getMonitoringPort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_MonitoringPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getTimeoutUnknownState <em>Timeout Unknown State</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Timeout Unknown State</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getTimeoutUnknownState()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_TimeoutUnknownState();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getUsername <em>Username</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Username</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getUsername()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Username();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getPassword <em>Password</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Password</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getPassword()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Password();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getJmxUrl <em>Jmx Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Jmx Url</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getJmxUrl()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_JmxUrl();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getWebConsoleUrl <em>Web Console Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Web Console Url</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getWebConsoleUrl()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_WebConsoleUrl();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#isTalendRuntime <em>Talend Runtime</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Talend Runtime</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#isTalendRuntime()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_TalendRuntime();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getMgmtServerPort <em>Mgmt Server Port</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mgmt Server Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getMgmtServerPort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_MgmtServerPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getMgmtRegPort <em>Mgmt Reg Port</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mgmt Reg Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getMgmtRegPort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_MgmtRegPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getAdminConsolePort <em>Admin Console Port</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Admin Console Port</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getAdminConsolePort()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_AdminConsolePort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#isUseSSL <em>Use SSL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Use SSL</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#isUseSSL()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_UseSSL();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionServer#getInstance <em>Instance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Instance</em>'.
     * @see org.talend.core.model.properties.ExecutionServer#getInstance()
     * @see #getExecutionServer()
     * @generated
     */
    EAttribute getExecutionServer_Instance();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionTask <em>Execution Task</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Task</em>'.
     * @see org.talend.core.model.properties.ExecutionTask
     * @generated
     */
    EClass getExecutionTask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLabel()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getDescription()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_Description();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.ExecutionTask#getExecutionServer <em>Execution Server</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Execution Server</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getExecutionServer()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_ExecutionServer();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionTask#getProject <em>Project</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Project</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getProject()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_Project();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getBranch <em>Branch</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Branch</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getBranch()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_Branch();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getContext <em>Context</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Context</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getContext()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_Context();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getJobVersion <em>Job Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Version</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getJobVersion()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_JobVersion();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isRegenerateJobOnChange <em>Regenerate Job On Change</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Regenerate Job On Change</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isRegenerateJobOnChange()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_RegenerateJobOnChange();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isActive()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_Active();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getLastScriptGenerationDate <em>Last Script Generation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Script Generation Date</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLastScriptGenerationDate()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_LastScriptGenerationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getGeneratedSvnRevision <em>Generated Svn Revision</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Generated Svn Revision</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getGeneratedSvnRevision()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_GeneratedSvnRevision();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getIdRemoteJob <em>Id Remote Job</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id Remote Job</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getIdRemoteJob()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_IdRemoteJob();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getIdRemoteJobExecution <em>Id Remote Job Execution</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id Remote Job Execution</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getIdRemoteJobExecution()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_IdRemoteJobExecution();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getChecksumArchive <em>Checksum Archive</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Checksum Archive</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getChecksumArchive()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ChecksumArchive();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getJobScriptArchiveFilename <em>Job Script Archive Filename</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Script Archive Filename</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getJobScriptArchiveFilename()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_JobScriptArchiveFilename();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getLastRunDate <em>Last Run Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Run Date</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLastRunDate()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_LastRunDate();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getLastDeploymentDate <em>Last Deployment Date</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Last Deployment Date</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLastDeploymentDate()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_LastDeploymentDate();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getLastEndedRunDate <em>Last Ended Run Date</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Last Ended Run Date</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLastEndedRunDate()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_LastEndedRunDate();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionTask#getCmdPrms <em>Cmd Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Cmd Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getCmdPrms()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_CmdPrms();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionTask#getEsbPropertiesPrms <em>Esb Properties Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Esb Properties Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getEsbPropertiesPrms()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_EsbPropertiesPrms();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionTask#getJobPrms <em>Job Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Job Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getJobPrms()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_JobPrms();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getJobId <em>Job Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getJobId()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_JobId();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.ExecutionTask#getVirtualServer <em>Virtual Server</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Virtual Server</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getVirtualServer()
     * @see #getExecutionTask()
     * @generated
     */
    EReference getExecutionTask_VirtualServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getMaxConcurrentExecutions <em>Max Concurrent Executions</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Concurrent Executions</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getMaxConcurrentExecutions()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_MaxConcurrentExecutions();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getGeneratedProjectName <em>Generated Project Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Generated Project Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getGeneratedProjectName()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_GeneratedProjectName();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getGeneratedJobName <em>Generated Job Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Generated Job Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getGeneratedJobName()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_GeneratedJobName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getGeneratedJobVersion <em>Generated Job Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Generated Job Version</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getGeneratedJobVersion()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_GeneratedJobVersion();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isApplyContextToChildren <em>Apply Context To Children</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Apply Context To Children</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isApplyContextToChildren()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ApplyContextToChildren();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getErrorStackTrace <em>Error Stack Trace</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Error Stack Trace</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getErrorStackTrace()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ErrorStackTrace();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTask#getLastTriggeringDate <em>Last Triggering Date</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Last Triggering Date</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getLastTriggeringDate()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_LastTriggeringDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isExecStatisticsEnabled <em>Exec Statistics Enabled</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exec Statistics Enabled</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isExecStatisticsEnabled()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ExecStatisticsEnabled();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isAddStatisticsCodeEnabled <em>Add Statistics Code Enabled</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Add Statistics Code Enabled</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isAddStatisticsCodeEnabled()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_AddStatisticsCodeEnabled();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getOwnerSchedulerInstanceId <em>Owner Scheduler Instance Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Owner Scheduler Instance Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getOwnerSchedulerInstanceId()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_OwnerSchedulerInstanceId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getOnUnknownStateJob <em>On Unknown State Job</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>On Unknown State Job</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getOnUnknownStateJob()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_OnUnknownStateJob();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#isUseLatestVersion <em>Use Latest Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Use Latest Version</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#isUseLatestVersion()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_UseLatestVersion();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getApplicationType <em>Application Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Application Type</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getApplicationType()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ApplicationType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getRepositoryName <em>Repository Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repository Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getRepositoryName()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_RepositoryName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getFeaturesFileUrl <em>Features File Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Features File Url</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getFeaturesFileUrl()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_FeaturesFileUrl();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getApplicationGroup <em>Application Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Application Group</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getApplicationGroup()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_ApplicationGroup();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getBundleName <em>Bundle Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Bundle Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getBundleName()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_BundleName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getPropertyId <em>Property Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Property Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getPropertyId()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_PropertyId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getFeatureName <em>Feature Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Feature Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getFeatureName()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_FeatureName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTask#getFeatureVersion <em>Feature Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Feature Version</em>'.
     * @see org.talend.core.model.properties.ExecutionTask#getFeatureVersion()
     * @see #getExecutionTask()
     * @generated
     */
    EAttribute getExecutionTask_FeatureVersion();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionTaskProperties <em>Execution Task Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Task Properties</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskProperties
     * @generated
     */
    EClass getExecutionTaskProperties();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskProperties#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskProperties#getId()
     * @see #getExecutionTaskProperties()
     * @generated
     */
    EAttribute getExecutionTaskProperties_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskProperties#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskProperties#getName()
     * @see #getExecutionTaskProperties()
     * @generated
     */
    EAttribute getExecutionTaskProperties_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskProperties#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskProperties#getValue()
     * @see #getExecutionTaskProperties()
     * @generated
     */
    EAttribute getExecutionTaskProperties_Value();

    /**
     * Returns the meta object for the container reference '{@link org.talend.core.model.properties.ExecutionTaskProperties#getExecutionTask <em>Execution Task</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Execution Task</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskProperties#getExecutionTask()
     * @see #getExecutionTaskProperties()
     * @generated
     */
    EReference getExecutionTaskProperties_ExecutionTask();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionTaskCmdPrm <em>Execution Task Cmd Prm</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Task Cmd Prm</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm
     * @generated
     */
    EClass getExecutionTaskCmdPrm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskCmdPrm#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm#getId()
     * @see #getExecutionTaskCmdPrm()
     * @generated
     */
    EAttribute getExecutionTaskCmdPrm_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskCmdPrm#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm#isActive()
     * @see #getExecutionTaskCmdPrm()
     * @generated
     */
    EAttribute getExecutionTaskCmdPrm_Active();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTaskCmdPrm#getParameter <em>Parameter</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Parameter</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm#getParameter()
     * @see #getExecutionTaskCmdPrm()
     * @generated
     */
    EAttribute getExecutionTaskCmdPrm_Parameter();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTaskCmdPrm#getDescription <em>Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm#getDescription()
     * @see #getExecutionTaskCmdPrm()
     * @generated
     */
    EAttribute getExecutionTaskCmdPrm_Description();

    /**
     * Returns the meta object for the container reference '
     * {@link org.talend.core.model.properties.ExecutionTaskCmdPrm#getExecutionTask <em>Execution Task</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the container reference '<em>Execution Task</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskCmdPrm#getExecutionTask()
     * @see #getExecutionTaskCmdPrm()
     * @generated
     */
    EReference getExecutionTaskCmdPrm_ExecutionTask();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionTaskJobPrm <em>Execution Task Job Prm</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Task Job Prm</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm
     * @generated
     */
    EClass getExecutionTaskJobPrm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskJobPrm#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getId()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskJobPrm#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getLabel()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskJobPrm#isOverride <em>Override</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Override</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#isOverride()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_Override();

    /**
     * Returns the meta object for the container reference '
     * {@link org.talend.core.model.properties.ExecutionTaskJobPrm#getExecutionTask <em>Execution Task</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the container reference '<em>Execution Task</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getExecutionTask()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EReference getExecutionTaskJobPrm_ExecutionTask();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionTaskJobPrm#getOriginalValue <em>Original Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Original Value</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getOriginalValue()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_OriginalValue();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskJobPrm#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default Value</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getDefaultValue()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_DefaultValue();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionTaskJobPrm#getItemType <em>Item Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Item Type</em>'.
     * @see org.talend.core.model.properties.ExecutionTaskJobPrm#getItemType()
     * @see #getExecutionTaskJobPrm()
     * @generated
     */
    EAttribute getExecutionTaskJobPrm_ItemType();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.TalendTrigger <em>Talend Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Talend Trigger</em>'.
     * @see org.talend.core.model.properties.TalendTrigger
     * @generated
     */
    EClass getTalendTrigger();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getId()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#isActive()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_Active();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getLabel()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getDescription()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getTriggerType <em>Trigger Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Trigger Type</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getTriggerType()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_TriggerType();

    /**
     * Returns the meta object for the container reference '{@link org.talend.core.model.properties.TalendTrigger#getExecutionTriggerable <em>Execution Triggerable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Execution Triggerable</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getExecutionTriggerable()
     * @see #getTalendTrigger()
     * @generated
     */
    EReference getTalendTrigger_ExecutionTriggerable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getStartTime <em>Start Time</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Time</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getStartTime()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_StartTime();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TalendTrigger#getEndTime <em>End Time</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Time</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getEndTime()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_EndTime();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TalendTrigger#getPreviousFireTime <em>Previous Fire Time</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Previous Fire Time</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getPreviousFireTime()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_PreviousFireTime();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TalendTrigger#getFinalFireTime <em>Final Fire Time</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Final Fire Time</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getFinalFireTime()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_FinalFireTime();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TalendTrigger#getIdQuartzTrigger <em>Id Quartz Trigger</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Id Quartz Trigger</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getIdQuartzTrigger()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_IdQuartzTrigger();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TalendTrigger#getResumePauseUpdated <em>Resume Pause Updated</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Resume Pause Updated</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#getResumePauseUpdated()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_ResumePauseUpdated();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TalendTrigger#isPreviouslyPaused <em>Previously Paused</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Previously Paused</em>'.
     * @see org.talend.core.model.properties.TalendTrigger#isPreviouslyPaused()
     * @see #getTalendTrigger()
     * @generated
     */
    EAttribute getTalendTrigger_PreviouslyPaused();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.CronTalendTrigger <em>Cron Talend Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Cron Talend Trigger</em>'.
     * @see org.talend.core.model.properties.CronTalendTrigger
     * @generated
     */
    EClass getCronTalendTrigger();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronTalendTrigger#getCronExpression <em>Cron Expression</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Cron Expression</em>'.
     * @see org.talend.core.model.properties.CronTalendTrigger#getCronExpression()
     * @see #getCronTalendTrigger()
     * @generated
     */
    EAttribute getCronTalendTrigger_CronExpression();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.CronUITalendTrigger <em>Cron UI Talend Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Cron UI Talend Trigger</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger
     * @generated
     */
    EClass getCronUITalendTrigger();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronUITalendTrigger#getListDaysOfWeek <em>List Days Of Week</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>List Days Of Week</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListDaysOfWeek()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListDaysOfWeek();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.CronUITalendTrigger#getListDaysOfMonth <em>List Days Of Month</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>List Days Of Month</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListDaysOfMonth()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListDaysOfMonth();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronUITalendTrigger#getListMonths <em>List Months</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>List Months</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListMonths()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListMonths();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronUITalendTrigger#getListYears <em>List Years</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>List Years</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListYears()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListYears();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronUITalendTrigger#getListHours <em>List Hours</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>List Hours</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListHours()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListHours();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.CronUITalendTrigger#getListMinutes <em>List Minutes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>List Minutes</em>'.
     * @see org.talend.core.model.properties.CronUITalendTrigger#getListMinutes()
     * @see #getCronUITalendTrigger()
     * @generated
     */
    EAttribute getCronUITalendTrigger_ListMinutes();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SimpleTalendTrigger <em>Simple Talend Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Simple Talend Trigger</em>'.
     * @see org.talend.core.model.properties.SimpleTalendTrigger
     * @generated
     */
    EClass getSimpleTalendTrigger();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SimpleTalendTrigger#getRepeatCount <em>Repeat Count</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Repeat Count</em>'.
     * @see org.talend.core.model.properties.SimpleTalendTrigger#getRepeatCount()
     * @see #getSimpleTalendTrigger()
     * @generated
     */
    EAttribute getSimpleTalendTrigger_RepeatCount();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SimpleTalendTrigger#getRepeatInterval <em>Repeat Interval</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Repeat Interval</em>'.
     * @see org.talend.core.model.properties.SimpleTalendTrigger#getRepeatInterval()
     * @see #getSimpleTalendTrigger()
     * @generated
     */
    EAttribute getSimpleTalendTrigger_RepeatInterval();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SimpleTalendTrigger#getTimesTriggered <em>Times Triggered</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Times Triggered</em>'.
     * @see org.talend.core.model.properties.SimpleTalendTrigger#getTimesTriggered()
     * @see #getSimpleTalendTrigger()
     * @generated
     */
    EAttribute getSimpleTalendTrigger_TimesTriggered();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionVirtualServer <em>Execution Virtual Server</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Virtual Server</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer
     * @generated
     */
    EClass getExecutionVirtualServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionVirtualServer#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer#getId()
     * @see #getExecutionVirtualServer()
     * @generated
     */
    EAttribute getExecutionVirtualServer_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionVirtualServer#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer#getLabel()
     * @see #getExecutionVirtualServer()
     * @generated
     */
    EAttribute getExecutionVirtualServer_Label();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.ExecutionVirtualServer#getDescription <em>Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer#getDescription()
     * @see #getExecutionVirtualServer()
     * @generated
     */
    EAttribute getExecutionVirtualServer_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionVirtualServer#isActive <em>Active</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer#isActive()
     * @see #getExecutionVirtualServer()
     * @generated
     */
    EAttribute getExecutionVirtualServer_Active();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.ExecutionVirtualServer#getExecutionServers <em>Execution Servers</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Execution Servers</em>'.
     * @see org.talend.core.model.properties.ExecutionVirtualServer#getExecutionServers()
     * @see #getExecutionVirtualServer()
     * @generated
     */
    EReference getExecutionVirtualServer_ExecutionServers();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.FileTrigger <em>File Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>File Trigger</em>'.
     * @see org.talend.core.model.properties.FileTrigger
     * @generated
     */
    EClass getFileTrigger();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.FileTrigger#getFileTriggerMasks <em>File Trigger Masks</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>File Trigger Masks</em>'.
     * @see org.talend.core.model.properties.FileTrigger#getFileTriggerMasks()
     * @see #getFileTrigger()
     * @generated
     */
    EReference getFileTrigger_FileTriggerMasks();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.FileTriggerMask <em>File Trigger Mask</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>File Trigger Mask</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask
     * @generated
     */
    EClass getFileTriggerMask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getId()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#isActive()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Active();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getLabel()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Label();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.FileTriggerMask#getDescription <em>Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getDescription()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Description();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.FileTriggerMask#getFileTrigger <em>File Trigger</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>File Trigger</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getFileTrigger()
     * @see #getFileTriggerMask()
     * @generated
     */
    EReference getFileTriggerMask_FileTrigger();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#getFolderPath <em>Folder Path</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Folder Path</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getFolderPath()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_FolderPath();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#getFileMask <em>File Mask</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Mask</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getFileMask()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_FileMask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#getContextParameterBaseName <em>Context Parameter Base Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Context Parameter Base Name</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getContextParameterBaseName()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_ContextParameterBaseName();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.FileTriggerMask#getCheckFileServer <em>Check File Server</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Check File Server</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#getCheckFileServer()
     * @see #getFileTriggerMask()
     * @generated
     */
    EReference getFileTriggerMask_CheckFileServer();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#isExist <em>Exist</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exist</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#isExist()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Exist();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#isCreated <em>Created</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Created</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#isCreated()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Created();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.FileTriggerMask#isModified <em>Modified</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Modified</em>'.
     * @see org.talend.core.model.properties.FileTriggerMask#isModified()
     * @see #getFileTriggerMask()
     * @generated
     */
    EAttribute getFileTriggerMask_Modified();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.JobletProcessItem <em>Joblet Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Joblet Process Item</em>'.
     * @see org.talend.core.model.properties.JobletProcessItem
     * @generated
     */
    EClass getJobletProcessItem();

    /**
     * Returns the meta object for the reference '
     * {@link org.talend.core.model.properties.JobletProcessItem#getJobletProcess <em>Joblet Process</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Joblet Process</em>'.
     * @see org.talend.core.model.properties.JobletProcessItem#getJobletProcess()
     * @see #getJobletProcessItem()
     * @generated
     */
    EReference getJobletProcessItem_JobletProcess();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.JobletProcessItem#getIcon <em>Icon</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Icon</em>'.
     * @see org.talend.core.model.properties.JobletProcessItem#getIcon()
     * @see #getJobletProcessItem()
     * @generated
     */
    EReference getJobletProcessItem_Icon();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.JobDocumentationItem <em>Job Documentation Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Job Documentation Item</em>'.
     * @see org.talend.core.model.properties.JobDocumentationItem
     * @generated
     */
    EClass getJobDocumentationItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.JobletDocumentationItem <em>Joblet Documentation Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Joblet Documentation Item</em>'.
     * @see org.talend.core.model.properties.JobletDocumentationItem
     * @generated
     */
    EClass getJobletDocumentationItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.WSDLSchemaConnectionItem <em>WSDL Schema Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>WSDL Schema Connection Item</em>'.
     * @see org.talend.core.model.properties.WSDLSchemaConnectionItem
     * @generated
     */
    EClass getWSDLSchemaConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SchemaInformation <em>Schema Information</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Schema Information</em>'.
     * @see org.talend.core.model.properties.SchemaInformation
     * @generated
     */
    EClass getSchemaInformation();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SchemaInformation#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.properties.SchemaInformation#getVersion()
     * @see #getSchemaInformation()
     * @generated
     */
    EAttribute getSchemaInformation_Version();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Information <em>Information</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Information</em>'.
     * @see org.talend.core.model.properties.Information
     * @generated
     */
    EClass getInformation();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Information#getLevel <em>Level</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Level</em>'.
     * @see org.talend.core.model.properties.Information#getLevel()
     * @see #getInformation()
     * @generated
     */
    EAttribute getInformation_Level();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Information#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.Information#getType()
     * @see #getInformation()
     * @generated
     */
    EAttribute getInformation_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Information#getText <em>Text</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text</em>'.
     * @see org.talend.core.model.properties.Information#getText()
     * @see #getInformation()
     * @generated
     */
    EAttribute getInformation_Text();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SQLPatternItem <em>SQL Pattern Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>SQL Pattern Item</em>'.
     * @see org.talend.core.model.properties.SQLPatternItem
     * @generated
     */
    EClass getSQLPatternItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SQLPatternItem#isSystem <em>System</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>System</em>'.
     * @see org.talend.core.model.properties.SQLPatternItem#isSystem()
     * @see #getSQLPatternItem()
     * @generated
     */
    EAttribute getSQLPatternItem_System();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SQLPatternItem#getEltName <em>Elt Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Elt Name</em>'.
     * @see org.talend.core.model.properties.SQLPatternItem#getEltName()
     * @see #getSQLPatternItem()
     * @generated
     */
    EAttribute getSQLPatternItem_EltName();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ComponentSetting <em>Component Setting</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Setting</em>'.
     * @see org.talend.core.model.properties.ComponentSetting
     * @generated
     */
    EClass getComponentSetting();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ComponentSetting#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ComponentSetting#getName()
     * @see #getComponentSetting()
     * @generated
     */
    EAttribute getComponentSetting_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ComponentSetting#isHidden <em>Hidden</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hidden</em>'.
     * @see org.talend.core.model.properties.ComponentSetting#isHidden()
     * @see #getComponentSetting()
     * @generated
     */
    EAttribute getComponentSetting_Hidden();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ComponentSetting#getFamily <em>Family</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Family</em>'.
     * @see org.talend.core.model.properties.ComponentSetting#getFamily()
     * @see #getComponentSetting()
     * @generated
     */
    EAttribute getComponentSetting_Family();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.StatAndLogsSettings <em>Stat And Logs Settings</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Stat And Logs Settings</em>'.
     * @see org.talend.core.model.properties.StatAndLogsSettings
     * @generated
     */
    EClass getStatAndLogsSettings();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.talend.core.model.properties.StatAndLogsSettings#getParameters <em>Parameters</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Parameters</em>'.
     * @see org.talend.core.model.properties.StatAndLogsSettings#getParameters()
     * @see #getStatAndLogsSettings()
     * @generated
     */
    EReference getStatAndLogsSettings_Parameters();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ImplicitContextSettings <em>Implicit Context Settings</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Implicit Context Settings</em>'.
     * @see org.talend.core.model.properties.ImplicitContextSettings
     * @generated
     */
    EClass getImplicitContextSettings();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.talend.core.model.properties.ImplicitContextSettings#getParameters <em>Parameters</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Parameters</em>'.
     * @see org.talend.core.model.properties.ImplicitContextSettings#getParameters()
     * @see #getImplicitContextSettings()
     * @generated
     */
    EReference getImplicitContextSettings_Parameters();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SoaOperation <em>Soa Operation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Soa Operation</em>'.
     * @see org.talend.core.model.properties.SoaOperation
     * @generated
     */
    EClass getSoaOperation();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getId()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getLabel()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getDescription()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Description();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.SoaOperation#getProject <em>Project</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Project</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getProject()
     * @see #getSoaOperation()
     * @generated
     */
    EReference getSoaOperation_Project();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getContext <em>Context</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Context</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getContext()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Context();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getJobVersion <em>Job Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Version</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getJobVersion()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_JobVersion();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getJobName <em>Job Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Name</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getJobName()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_JobName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#isActive <em>Active</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Active</em>'.
     * @see org.talend.core.model.properties.SoaOperation#isActive()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Active();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getLastScriptGenerationDate <em>Last Script Generation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Script Generation Date</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getLastScriptGenerationDate()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_LastScriptGenerationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getJobId <em>Job Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Id</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getJobId()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_JobId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#isApplyContextToChildren <em>Apply Context To Children</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Apply Context To Children</em>'.
     * @see org.talend.core.model.properties.SoaOperation#isApplyContextToChildren()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_ApplyContextToChildren();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.SoaOperation#getInputParameters <em>Input Parameters</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Input Parameters</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getInputParameters()
     * @see #getSoaOperation()
     * @generated
     */
    EReference getSoaOperation_InputParameters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getJvmParameters <em>Jvm Parameters</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Jvm Parameters</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getJvmParameters()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_JvmParameters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getXms <em>Xms</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Xms</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getXms()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Xms();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getXmx <em>Xmx</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Xmx</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getXmx()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Xmx();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SoaOperation#getMinJobInstances <em>Min Job Instances</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Min Job Instances</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getMinJobInstances()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_MinJobInstances();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SoaOperation#getMaxJobInstances <em>Max Job Instances</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Max Job Instances</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getMaxJobInstances()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_MaxJobInstances();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getIdleTTL_forAllInstances <em>Idle TTL for All Instances</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Idle TTL for All Instances</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getIdleTTL_forAllInstances()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_IdleTTL_forAllInstances();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getIdleTTL_forAdditionalInstances <em>Idle TTL for Additional Instances</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Idle TTL for Additional Instances</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getIdleTTL_forAdditionalInstances()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_IdleTTL_forAdditionalInstances();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getQueueMaxSize <em>Queue Max Size</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Queue Max Size</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getQueueMaxSize()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_QueueMaxSize();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SoaOperation#getRequestInQueueTTL <em>Request In Queue TTL</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Request In Queue TTL</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getRequestInQueueTTL()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_RequestInQueueTTL();

    /**
     * Returns the meta object for the container reference '{@link org.talend.core.model.properties.SoaOperation#getService <em>Service</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the container reference '<em>Service</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getService()
     * @see #getSoaOperation()
     * @generated
     */
    EReference getSoaOperation_Service();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getReturnStyle <em>Return Style</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Return Style</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getReturnStyle()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_ReturnStyle();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaOperation#getBranch <em>Branch</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Branch</em>'.
     * @see org.talend.core.model.properties.SoaOperation#getBranch()
     * @see #getSoaOperation()
     * @generated
     */
    EAttribute getSoaOperation_Branch();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SoaInputParameter <em>Soa Input Parameter</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Soa Input Parameter</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter
     * @generated
     */
    EClass getSoaInputParameter();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaInputParameter#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#getId()
     * @see #getSoaInputParameter()
     * @generated
     */
    EAttribute getSoaInputParameter_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaInputParameter#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#getLabel()
     * @see #getSoaInputParameter()
     * @generated
     */
    EAttribute getSoaInputParameter_Label();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.SoaInputParameter#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Operation</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#getOperation()
     * @see #getSoaInputParameter()
     * @generated
     */
    EReference getSoaInputParameter_Operation();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SoaInputParameter#getDefaultValue <em>Default Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Default Value</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#getDefaultValue()
     * @see #getSoaInputParameter()
     * @generated
     */
    EAttribute getSoaInputParameter_DefaultValue();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.SoaInputParameter#getExposedName <em>Exposed Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Exposed Name</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#getExposedName()
     * @see #getSoaInputParameter()
     * @generated
     */
    EAttribute getSoaInputParameter_ExposedName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaInputParameter#isExposed <em>Exposed</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exposed</em>'.
     * @see org.talend.core.model.properties.SoaInputParameter#isExposed()
     * @see #getSoaInputParameter()
     * @generated
     */
    EAttribute getSoaInputParameter_Exposed();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SoaService <em>Soa Service</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Soa Service</em>'.
     * @see org.talend.core.model.properties.SoaService
     * @generated
     */
    EClass getSoaService();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.SoaService#getId()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.SoaService#getLabel()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Label();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getNameSpace <em>Name Space</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name Space</em>'.
     * @see org.talend.core.model.properties.SoaService#getNameSpace()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_NameSpace();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getContact <em>Contact</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Contact</em>'.
     * @see org.talend.core.model.properties.SoaService#getContact()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Contact();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.SoaService#getDescription()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Description();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getCreation <em>Creation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation</em>'.
     * @see org.talend.core.model.properties.SoaService#getCreation()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Creation();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getModification <em>Modification</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Modification</em>'.
     * @see org.talend.core.model.properties.SoaService#getModification()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Modification();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getPort <em>Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Port</em>'.
     * @see org.talend.core.model.properties.SoaService#getPort()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Port();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.SoaService#getType()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getStyle <em>Style</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Style</em>'.
     * @see org.talend.core.model.properties.SoaService#getStyle()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Style();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getUsedType <em>Used Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Used Type</em>'.
     * @see org.talend.core.model.properties.SoaService#getUsedType()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_UsedType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getParamStyle <em>Param Style</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Param Style</em>'.
     * @see org.talend.core.model.properties.SoaService#getParamStyle()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_ParamStyle();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.SoaService#getOperations <em>Operations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Operations</em>'.
     * @see org.talend.core.model.properties.SoaService#getOperations()
     * @see #getSoaService()
     * @generated
     */
    EReference getSoaService_Operations();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.SoaService#getStatus <em>Status</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status</em>'.
     * @see org.talend.core.model.properties.SoaService#getStatus()
     * @see #getSoaService()
     * @generated
     */
    EAttribute getSoaService_Status();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.RulesItem <em>Rules Item</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Rules Item</em>'.
     * @see org.talend.core.model.properties.RulesItem
     * @generated
     */
    EClass getRulesItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.UserRight <em>User Right</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>User Right</em>'.
     * @see org.talend.core.model.properties.UserRight
     * @generated
     */
    EClass getUserRight();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRight#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.UserRight#getId()
     * @see #getUserRight()
     * @generated
     */
    EAttribute getUserRight_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRight#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.UserRight#getName()
     * @see #getUserRight()
     * @generated
     */
    EAttribute getUserRight_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRight#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.UserRight#getDescription()
     * @see #getUserRight()
     * @generated
     */
    EAttribute getUserRight_Description();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.UserRight#getRolesRights <em>Roles Rights</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Roles Rights</em>'.
     * @see org.talend.core.model.properties.UserRight#getRolesRights()
     * @see #getUserRight()
     * @generated
     */
    EReference getUserRight_RolesRights();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.RoleRight <em>Role Right</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Role Right</em>'.
     * @see org.talend.core.model.properties.RoleRight
     * @generated
     */
    EClass getRoleRight();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.RoleRight#getRole <em>Role</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Role</em>'.
     * @see org.talend.core.model.properties.RoleRight#getRole()
     * @see #getRoleRight()
     * @generated
     */
    EReference getRoleRight_Role();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.RoleRight#getUserRight <em>User Right</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>User Right</em>'.
     * @see org.talend.core.model.properties.RoleRight#getUserRight()
     * @see #getRoleRight()
     * @generated
     */
    EReference getRoleRight_UserRight();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.SVGBusinessProcessItem <em>SVG Business Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>SVG Business Process Item</em>'.
     * @see org.talend.core.model.properties.SVGBusinessProcessItem
     * @generated
     */
    EClass getSVGBusinessProcessItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.SVGBusinessProcessItem#getBusinessProcessItem <em>Business Process Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Business Process Item</em>'.
     * @see org.talend.core.model.properties.SVGBusinessProcessItem#getBusinessProcessItem()
     * @see #getSVGBusinessProcessItem()
     * @generated
     */
    EReference getSVGBusinessProcessItem_BusinessProcessItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.LinkRulesItem <em>Link Rules Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Link Rules Item</em>'.
     * @see org.talend.core.model.properties.LinkRulesItem
     * @generated
     */
    EClass getLinkRulesItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.LinkRulesItem#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.LinkRulesItem#getName()
     * @see #getLinkRulesItem()
     * @generated
     */
    EAttribute getLinkRulesItem_Name();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.LinkRulesItem#getLink <em>Link</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Link</em>'.
     * @see org.talend.core.model.properties.LinkRulesItem#getLink()
     * @see #getLinkRulesItem()
     * @generated
     */
    EReference getLinkRulesItem_Link();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ItemRelations <em>Item Relations</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Item Relations</em>'.
     * @see org.talend.core.model.properties.ItemRelations
     * @generated
     */
    EClass getItemRelations();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.core.model.properties.ItemRelations#getBaseItem <em>Base Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Base Item</em>'.
     * @see org.talend.core.model.properties.ItemRelations#getBaseItem()
     * @see #getItemRelations()
     * @generated
     */
    EReference getItemRelations_BaseItem();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.talend.core.model.properties.ItemRelations#getRelatedItems <em>Related Items</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Related Items</em>'.
     * @see org.talend.core.model.properties.ItemRelations#getRelatedItems()
     * @see #getItemRelations()
     * @generated
     */
    EReference getItemRelations_RelatedItems();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ItemRelation <em>Item Relation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Item Relation</em>'.
     * @see org.talend.core.model.properties.ItemRelation
     * @generated
     */
    EClass getItemRelation();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemRelation#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ItemRelation#getId()
     * @see #getItemRelation()
     * @generated
     */
    EAttribute getItemRelation_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemRelation#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.properties.ItemRelation#getVersion()
     * @see #getItemRelation()
     * @generated
     */
    EAttribute getItemRelation_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ItemRelation#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.ItemRelation#getType()
     * @see #getItemRelation()
     * @generated
     */
    EAttribute getItemRelation_Type();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.Notification <em>Notification</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Notification</em>'.
     * @see org.talend.core.model.properties.Notification
     * @generated
     */
    EClass getNotification();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Notification#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.Notification#getId()
     * @see #getNotification()
     * @generated
     */
    EAttribute getNotification_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Notification#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.Notification#getType()
     * @see #getNotification()
     * @generated
     */
    EAttribute getNotification_Type();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Notification#getProps <em>Props</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Props</em>'.
     * @see org.talend.core.model.properties.Notification#getProps()
     * @see #getNotification()
     * @generated
     */
    EAttribute getNotification_Props();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.Notification#isEnabled <em>Enabled</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Enabled</em>'.
     * @see org.talend.core.model.properties.Notification#isEnabled()
     * @see #getNotification()
     * @generated
     */
    EAttribute getNotification_Enabled();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.HL7ConnectionItem <em>HL7 Connection Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>HL7 Connection Item</em>'.
     * @see org.talend.core.model.properties.HL7ConnectionItem
     * @generated
     */
    EClass getHL7ConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.UserRoleReference <em>User Role Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>User Role Reference</em>'.
     * @see org.talend.core.model.properties.UserRoleReference
     * @generated
     */
    EClass getUserRoleReference();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.UserRoleReference#getUser <em>User</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>User</em>'.
     * @see org.talend.core.model.properties.UserRoleReference#getUser()
     * @see #getUserRoleReference()
     * @generated
     */
    EReference getUserRoleReference_User();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.UserRoleReference#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Role</em>'.
     * @see org.talend.core.model.properties.UserRoleReference#getRole()
     * @see #getUserRoleReference()
     * @generated
     */
    EReference getUserRoleReference_Role();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.HeaderFooterConnectionItem <em>Header Footer Connection Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Header Footer Connection Item</em>'.
     * @see org.talend.core.model.properties.HeaderFooterConnectionItem
     * @generated
     */
    EClass getHeaderFooterConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.FTPConnectionItem <em>FTP Connection Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>FTP Connection Item</em>'.
     * @see org.talend.core.model.properties.FTPConnectionItem
     * @generated
     */
    EClass getFTPConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.CustomComponentSetting <em>Custom Component Setting</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Custom Component Setting</em>'.
     * @see org.talend.core.model.properties.CustomComponentSetting
     * @generated
     */
    EClass getCustomComponentSetting();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.CustomComponentSetting#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.CustomComponentSetting#getName()
     * @see #getCustomComponentSetting()
     * @generated
     */
    EAttribute getCustomComponentSetting_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.CustomComponentSetting#isShare <em>Share</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Share</em>'.
     * @see org.talend.core.model.properties.CustomComponentSetting#isShare()
     * @see #getCustomComponentSetting()
     * @generated
     */
    EAttribute getCustomComponentSetting_Share();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.TDQItem <em>TDQ Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>TDQ Item</em>'.
     * @see org.talend.core.model.properties.TDQItem
     * @generated
     */
    EClass getTDQItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TDQItem#getFilename <em>Filename</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Filename</em>'.
     * @see org.talend.core.model.properties.TDQItem#getFilename()
     * @see #getTDQItem()
     * @generated
     */
    EAttribute getTDQItem_Filename();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.BRMSConnectionItem <em>BRMS Connection Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>BRMS Connection Item</em>'.
     * @see org.talend.core.model.properties.BRMSConnectionItem
     * @generated
     */
    EClass getBRMSConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.JobScriptItem <em>Job Script Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Job Script Item</em>'.
     * @see org.talend.core.model.properties.JobScriptItem
     * @generated
     */
    EClass getJobScriptItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.EDIFACTConnectionItem <em>EDIFACT Connection Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>EDIFACT Connection Item</em>'.
     * @see org.talend.core.model.properties.EDIFACTConnectionItem
     * @generated
     */
    EClass getEDIFACTConnectionItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.TechnicalVariable <em>Technical Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Technical Variable</em>'.
     * @see org.talend.core.model.properties.TechnicalVariable
     * @generated
     */
    EClass getTechnicalVariable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TechnicalVariable#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see org.talend.core.model.properties.TechnicalVariable#getKey()
     * @see #getTechnicalVariable()
     * @generated
     */
    EAttribute getTechnicalVariable_Key();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TechnicalVariable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.core.model.properties.TechnicalVariable#getValue()
     * @see #getTechnicalVariable()
     * @generated
     */
    EAttribute getTechnicalVariable_Value();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ReferenceItem <em>Reference Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference Item</em>'.
     * @see org.talend.core.model.properties.ReferenceItem
     * @generated
     */
    EClass getReferenceItem();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ReferenceItem#getState <em>State</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>State</em>'.
     * @see org.talend.core.model.properties.ReferenceItem#getState()
     * @see #getReferenceItem()
     * @generated
     */
    EReference getReferenceItem_State();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ReferenceItem#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parent</em>'.
     * @see org.talend.core.model.properties.ReferenceItem#getParent()
     * @see #getReferenceItem()
     * @generated
     */
    EReference getReferenceItem_Parent();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ReferenceItem#isReloadFromFile <em>Reload From File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reload From File</em>'.
     * @see org.talend.core.model.properties.ReferenceItem#isReloadFromFile()
     * @see #getReferenceItem()
     * @generated
     */
    EAttribute getReferenceItem_ReloadFromFile();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExchangeUser <em>Exchange User</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exchange User</em>'.
     * @see org.talend.core.model.properties.ExchangeUser
     * @generated
     */
    EClass getExchangeUser();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExchangeUser#getUsername <em>Username</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Username</em>'.
     * @see org.talend.core.model.properties.ExchangeUser#getUsername()
     * @see #getExchangeUser()
     * @generated
     */
    EAttribute getExchangeUser_Username();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExchangeUser#getLogin <em>Login</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Login</em>'.
     * @see org.talend.core.model.properties.ExchangeUser#getLogin()
     * @see #getExchangeUser()
     * @generated
     */
    EAttribute getExchangeUser_Login();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExchangeUser#getPassword <em>Password</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Password</em>'.
     * @see org.talend.core.model.properties.ExchangeUser#getPassword()
     * @see #getExchangeUser()
     * @generated
     */
    EAttribute getExchangeUser_Password();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ArtifactNotification <em>Artifact Notification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Artifact Notification</em>'.
     * @see org.talend.core.model.properties.ArtifactNotification
     * @generated
     */
    EClass getArtifactNotification();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ArtifactNotification#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ArtifactNotification#getName()
     * @see #getArtifactNotification()
     * @generated
     */
    EAttribute getArtifactNotification_Name();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>Additional Info Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Additional Info Map</em>'.
     * @see java.util.Map.Entry
     * @model keyDataType="org.eclipse.emf.ecore.EString"
     *        valueDataType="org.eclipse.emf.ecore.EString"
     * @generated
     */
    EClass getAdditionalInfoMap();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getAdditionalInfoMap()
     * @generated
     */
    EAttribute getAdditionalInfoMap_Key();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getAdditionalInfoMap()
     * @generated
     */
    EAttribute getAdditionalInfoMap_Value();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.MigrationTask <em>Migration Task</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Migration Task</em>'.
     * @see org.talend.core.model.properties.MigrationTask
     * @generated
     */
    EClass getMigrationTask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.MigrationTask#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.MigrationTask#getId()
     * @see #getMigrationTask()
     * @generated
     */
    EAttribute getMigrationTask_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.MigrationTask#getBreaks <em>Breaks</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Breaks</em>'.
     * @see org.talend.core.model.properties.MigrationTask#getBreaks()
     * @see #getMigrationTask()
     * @generated
     */
    EAttribute getMigrationTask_Breaks();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.MigrationTask#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see org.talend.core.model.properties.MigrationTask#getVersion()
     * @see #getMigrationTask()
     * @generated
     */
    EAttribute getMigrationTask_Version();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.MigrationTask#getStatus <em>Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status</em>'.
     * @see org.talend.core.model.properties.MigrationTask#getStatus()
     * @see #getMigrationTask()
     * @generated
     */
    EAttribute getMigrationTask_Status();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.PigudfItem <em>Pigudf Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Pigudf Item</em>'.
     * @see org.talend.core.model.properties.PigudfItem
     * @generated
     */
    EClass getPigudfItem();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ReferenceFileItem <em>Reference File Item</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference File Item</em>'.
     * @see org.talend.core.model.properties.ReferenceFileItem
     * @generated
     */
    EClass getReferenceFileItem();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ReferenceFileItem#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ReferenceFileItem#getName()
     * @see #getReferenceFileItem()
     * @generated
     */
    EAttribute getReferenceFileItem_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ReferenceFileItem#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extension</em>'.
     * @see org.talend.core.model.properties.ReferenceFileItem#getExtension()
     * @see #getReferenceFileItem()
     * @generated
     */
    EAttribute getReferenceFileItem_Extension();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ReferenceFileItem#getContent <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Content</em>'.
     * @see org.talend.core.model.properties.ReferenceFileItem#getContent()
     * @see #getReferenceFileItem()
     * @generated
     */
    EReference getReferenceFileItem_Content();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionPlan <em>Execution Plan</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan</em>'.
     * @see org.talend.core.model.properties.ExecutionPlan
     * @generated
     */
    EClass getExecutionPlan();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlan#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.talend.core.model.properties.ExecutionPlan#getLabel()
     * @see #getExecutionPlan()
     * @generated
     */
    EAttribute getExecutionPlan_Label();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionPlan#getExecPlanParts <em>Exec Plan Parts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Exec Plan Parts</em>'.
     * @see org.talend.core.model.properties.ExecutionPlan#getExecPlanParts()
     * @see #getExecutionPlan()
     * @generated
     */
    EReference getExecutionPlan_ExecPlanParts();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionPlan#getExecPlanPrms <em>Exec Plan Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Exec Plan Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionPlan#getExecPlanPrms()
     * @see #getExecutionPlan()
     * @generated
     */
    EReference getExecutionPlan_ExecPlanPrms();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlan#getDescription <em>Description</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Description</em>'.
     * @see org.talend.core.model.properties.ExecutionPlan#getDescription()
     * @see #getExecutionPlan()
     * @generated
     */
    EAttribute getExecutionPlan_Description();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionPlanPart <em>Execution Plan Part</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan Part</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart
     * @generated
     */
    EClass getExecutionPlanPart();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getId()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_Id();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPart#getTask <em>Task</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Task</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getTask()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EReference getExecutionPlanPart_Task();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPart#getExecutionPlan <em>Execution Plan</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Execution Plan</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getExecutionPlan()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EReference getExecutionPlanPart_ExecutionPlan();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPart#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parent</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getParent()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EReference getExecutionPlanPart_Parent();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getType()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_Type();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionPlanPart#getJvmPrms <em>Jvm Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Jvm Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getJvmPrms()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EReference getExecutionPlanPart_JvmPrms();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.core.model.properties.ExecutionPlanPart#getContextPrms <em>Context Prms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Context Prms</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getContextPrms()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EReference getExecutionPlanPart_ContextPrms();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getStatus <em>Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getStatus()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_Status();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getStartDate <em>Start Date</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Date</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getStartDate()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_StartDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getEndDate <em>End Date</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Date</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getEndDate()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_EndDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getRequestId()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_RequestId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#isUseParallel <em>Use Parallel</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Use Parallel</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#isUseParallel()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_UseParallel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPart#getMaxThreads <em>Max Threads</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Threads</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPart#getMaxThreads()
     * @see #getExecutionPlanPart()
     * @generated
     */
    EAttribute getExecutionPlanPart_MaxThreads();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionPlanPrm <em>Execution Plan Prm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan Prm</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPrm
     * @generated
     */
    EClass getExecutionPlanPrm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPrm#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPrm#getId()
     * @see #getExecutionPlanPrm()
     * @generated
     */
    EAttribute getExecutionPlanPrm_Id();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPrm#getExecutionPlan <em>Execution Plan</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Execution Plan</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPrm#getExecutionPlan()
     * @see #getExecutionPlanPrm()
     * @generated
     */
    EReference getExecutionPlanPrm_ExecutionPlan();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPrm#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPrm#getName()
     * @see #getExecutionPlanPrm()
     * @generated
     */
    EAttribute getExecutionPlanPrm_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPrm#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPrm#getValue()
     * @see #getExecutionPlanPrm()
     * @generated
     */
    EAttribute getExecutionPlanPrm_Value();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionPlanPartCmdPrm <em>Execution Plan Part Cmd Prm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan Part Cmd Prm</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartCmdPrm
     * @generated
     */
    EClass getExecutionPlanPartCmdPrm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getId()
     * @see #getExecutionPlanPartCmdPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartCmdPrm_Id();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getExecutionPlanPart <em>Execution Plan Part</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Execution Plan Part</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getExecutionPlanPart()
     * @see #getExecutionPlanPartCmdPrm()
     * @generated
     */
    EReference getExecutionPlanPartCmdPrm_ExecutionPlanPart();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getName()
     * @see #getExecutionPlanPartCmdPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartCmdPrm_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartCmdPrm#getValue()
     * @see #getExecutionPlanPartCmdPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartCmdPrm_Value();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm <em>Execution Plan Part Job Prm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan Part Job Prm</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm
     * @generated
     */
    EClass getExecutionPlanPartJobPrm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#getId()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartJobPrm_Id();

    /**
     * Returns the meta object for the reference '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#getExecutionPlanPart <em>Execution Plan Part</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Execution Plan Part</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#getExecutionPlanPart()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EReference getExecutionPlanPartJobPrm_ExecutionPlanPart();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#isOverride <em>Override</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Override</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#isOverride()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartJobPrm_Override();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#getName()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartJobPrm_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#getCustomValue <em>Custom Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Custom Value</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#getCustomValue()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartJobPrm_CustomValue();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.ExecutionPlanPartJobPrm#getPartCustomValue <em>Part Custom Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Part Custom Value</em>'.
     * @see org.talend.core.model.properties.ExecutionPlanPartJobPrm#getPartCustomValue()
     * @see #getExecutionPlanPartJobPrm()
     * @generated
     */
    EAttribute getExecutionPlanPartJobPrm_PartCustomValue();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.LinkRulesItem#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extension</em>'.
     * @see org.talend.core.model.properties.LinkRulesItem#getExtension()
     * @see #getLinkRulesItem()
     * @generated
     */
    EAttribute getLinkRulesItem_Extension();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.TaskExecutionHistory <em>Task Execution History</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Task Execution History</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory
     * @generated
     */
    EClass getTaskExecutionHistory();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getId()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_Id();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getBasicStatus <em>Basic Status</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Basic Status</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getBasicStatus()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_BasicStatus();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getDetailedStatus <em>Detailed Status</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Detailed Status</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getDetailedStatus()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_DetailedStatus();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTaskLabel <em>Task Label</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Task Label</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTaskLabel()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TaskLabel();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTaskDescription <em>Task Description</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Task Description</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTaskDescription()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TaskDescription();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getProjectName <em>Project Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Project Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getProjectName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ProjectName();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTalendJobName <em>Talend Job Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Talend Job Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTalendJobName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TalendJobName();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTalendJobId <em>Talend Job Id</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Talend Job Id</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTalendJobId()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TalendJobId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getTalendJobVersion <em>Talend Job Version</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Talend Job Version</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTalendJobVersion()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TalendJobVersion();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getContextName <em>Context Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Context Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getContextName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ContextName();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getContextValues <em>Context Values</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Context Values</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getContextValues()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ContextValues();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getJvmValues <em>Jvm Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Jvm Values</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getJvmValues()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_JvmValues();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getParentTaskExecId <em>Parent Task Exec Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parent Task Exec Id</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getParentTaskExecId()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ParentTaskExecId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getParentPlanExecId <em>Parent Plan Exec Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parent Plan Exec Id</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getParentPlanExecId()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ParentPlanExecId();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.PlanExecutionHistory <em>Plan Execution History</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Plan Execution History</em>'.
     * @see org.talend.core.model.properties.PlanExecutionHistory
     * @generated
     */
    EClass getPlanExecutionHistory();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.PlanExecutionHistory#getOriginalLabel <em>Original Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Original Label</em>'.
     * @see org.talend.core.model.properties.PlanExecutionHistory#getOriginalLabel()
     * @see #getPlanExecutionHistory()
     * @generated
     */
    EAttribute getPlanExecutionHistory_OriginalLabel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.PlanExecutionHistory#getCurrentLabel <em>Current Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Current Label</em>'.
     * @see org.talend.core.model.properties.PlanExecutionHistory#getCurrentLabel()
     * @see #getPlanExecutionHistory()
     * @generated
     */
    EAttribute getPlanExecutionHistory_CurrentLabel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getVirtualServerName <em>Virtual Server Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Virtual Server Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getVirtualServerName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_VirtualServerName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerName <em>Execution Server Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Execution Server Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExecutionServerName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerHost <em>Execution Server Host</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Execution Server Host</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerHost()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExecutionServerHost();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerCmdPort <em>Execution Server Cmd Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Execution Server Cmd Port</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerCmdPort()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExecutionServerCmdPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerFilePort <em>Execution Server File Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Execution Server File Port</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerFilePort()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExecutionServerFilePort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerMonitoringPort <em>Execution Server Monitoring Port</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Execution Server Monitoring Port</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExecutionServerMonitoringPort()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExecutionServerMonitoringPort();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#isApplyContextToChildren <em>Apply Context To Children</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Apply Context To Children</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#isApplyContextToChildren()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ApplyContextToChildren();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTriggeredBy <em>Triggered By</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Triggered By</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTriggeredBy()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TriggeredBy();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTriggerType <em>Trigger Type</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Trigger Type</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTriggerType()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TriggerType();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTriggerName <em>Trigger Name</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Trigger Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTriggerName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TriggerName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getTriggerDescription <em>Trigger Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Trigger Description</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTriggerDescription()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TriggerDescription();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getTaskErrorStackTrace <em>Task Error Stack Trace</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Task Error Stack Trace</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTaskErrorStackTrace()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TaskErrorStackTrace();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getIdQuartzJob <em>Id Quartz Job</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Id Quartz Job</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getIdQuartzJob()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_IdQuartzJob();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getIdQuartzTrigger <em>Id Quartz Trigger</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id Quartz Trigger</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getIdQuartzTrigger()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_IdQuartzTrigger();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getLastJobGenerationDate <em>Last Job Generation Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Last Job Generation Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getLastJobGenerationDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_LastJobGenerationDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getJobArchiveFilename <em>Job Archive Filename</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Job Archive Filename</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getJobArchiveFilename()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_JobArchiveFilename();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFileMask <em>File Trigger File Mask</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Trigger File Mask</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFileMask()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_FileTriggerFileMask();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFileName <em>File Trigger File Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Trigger File Name</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFileName()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_FileTriggerFileName();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFolderPath <em>File Trigger Folder Path</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Trigger Folder Path</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerFolderPath()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_FileTriggerFolderPath();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerTriggeredFilePath <em>File Trigger Triggered File Path</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>File Trigger Triggered File Path</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getFileTriggerTriggeredFilePath()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_FileTriggerTriggeredFilePath();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getExpectedTriggeringDate <em>Expected Triggering Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Expected Triggering Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getExpectedTriggeringDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ExpectedTriggeringDate();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTaskStartDate <em>Task Start Date</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Task Start Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTaskStartDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TaskStartDate();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getTaskEndDate <em>Task End Date</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Task End Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getTaskEndDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_TaskEndDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getServerJobStartDate <em>Server Job Start Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Server Job Start Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getServerJobStartDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ServerJobStartDate();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getServerJobEndDate <em>Server Job End Date</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Server Job End Date</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getServerJobEndDate()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ServerJobEndDate();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getIdRemoteJob <em>Id Remote Job</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Id Remote Job</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getIdRemoteJob()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_IdRemoteJob();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.TaskExecutionHistory#getIdRemoteJobExecution <em>Id Remote Job Execution</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id Remote Job Execution</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getIdRemoteJobExecution()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_IdRemoteJobExecution();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#getRequestId <em>Request Id</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#getRequestId()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_RequestId();

    /**
     * Returns the meta object for the attribute '
     * {@link org.talend.core.model.properties.TaskExecutionHistory#isResumingMode <em>Resuming Mode</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Resuming Mode</em>'.
     * @see org.talend.core.model.properties.TaskExecutionHistory#isResumingMode()
     * @see #getTaskExecutionHistory()
     * @generated
     */
    EAttribute getTaskExecutionHistory_ResumingMode();

    /**
     * Returns the meta object for enum '{@link org.talend.core.model.properties.FolderType <em>Folder Type</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for enum '<em>Folder Type</em>'.
     * @see org.talend.core.model.properties.FolderType
     * @generated
     */
    EEnum getFolderType();

    /**
     * Returns the meta object for enum '{@link org.talend.core.model.properties.UserProjectAuthorizationType <em>User Project Authorization Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for enum '<em>User Project Authorization Type</em>'.
     * @see org.talend.core.model.properties.UserProjectAuthorizationType
     * @generated
     */
    EEnum getUserProjectAuthorizationType();

    /**
     * Returns the meta object for enum '{@link org.talend.core.model.properties.UserModuleAuthorizationType <em>User Module Authorization Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for enum '<em>User Module Authorization Type</em>'.
     * @see org.talend.core.model.properties.UserModuleAuthorizationType
     * @generated
     */
    EEnum getUserModuleAuthorizationType();

    /**
     * Returns the meta object for enum '{@link org.talend.core.model.properties.InformationLevel <em>Information Level</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for enum '<em>Information Level</em>'.
     * @see org.talend.core.model.properties.InformationLevel
     * @generated
     */
    EEnum getInformationLevel();

    /**
     * Returns the meta object for enum '{@link org.talend.core.model.properties.MigrationStatus <em>Migration Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Migration Status</em>'.
     * @see org.talend.core.model.properties.MigrationStatus
     * @generated
     */
    EEnum getMigrationStatus();

    /**
     * Returns the meta object for class '{@link org.talend.core.model.properties.UserRole <em>User Role</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>User Role</em>'.
     * @see org.talend.core.model.properties.UserRole
     * @generated
     */
    EClass getUserRole();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRole#getId <em>Id</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.talend.core.model.properties.UserRole#getId()
     * @see #getUserRole()
     * @generated
     */
    EAttribute getUserRole_Id();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRole#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.talend.core.model.properties.UserRole#getName()
     * @see #getUserRole()
     * @generated
     */
    EAttribute getUserRole_Name();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRole#getLocalizedLabel <em>Localized Label</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Localized Label</em>'.
     * @see org.talend.core.model.properties.UserRole#getLocalizedLabel()
     * @see #getUserRole()
     * @generated
     */
    EAttribute getUserRole_LocalizedLabel();

    /**
     * Returns the meta object for the attribute '{@link org.talend.core.model.properties.UserRole#isFixed <em>Fixed</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fixed</em>'.
     * @see org.talend.core.model.properties.UserRole#isFixed()
     * @see #getUserRole()
     * @generated
     */
    EAttribute getUserRole_Fixed();

    /**
     * Returns the meta object for the reference list '{@link org.talend.core.model.properties.UserRole#getRolesRights <em>Roles Rights</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Roles Rights</em>'.
     * @see org.talend.core.model.properties.UserRole#getRolesRights()
     * @see #getUserRole()
     * @generated
     */
    EReference getUserRole_RolesRights();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    PropertiesFactory getPropertiesFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.StatusImpl <em>Status</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.StatusImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getStatus()
         * @generated
         */
        EClass STATUS = eINSTANCE.getStatus();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute STATUS__LABEL = eINSTANCE.getStatus_Label();

        /**
         * The meta object literal for the '<em><b>Code</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute STATUS__CODE = eINSTANCE.getStatus_Code();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ProjectImpl <em>Project</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ProjectImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProject()
         * @generated
         */
        EClass PROJECT = eINSTANCE.getProject();

        /**
         * The meta object literal for the '<em><b>Technical Status</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__TECHNICAL_STATUS = eINSTANCE.getProject_TechnicalStatus();

        /**
         * The meta object literal for the '<em><b>Documentation Status</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__DOCUMENTATION_STATUS = eINSTANCE.getProject_DocumentationStatus();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__ID = eINSTANCE.getProject_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__LABEL = eINSTANCE.getProject_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__DESCRIPTION = eINSTANCE.getProject_Description();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__LANGUAGE = eINSTANCE.getProject_Language();

        /**
         * The meta object literal for the '<em><b>Author</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROJECT__AUTHOR = eINSTANCE.getProject_Author();

        /**
         * The meta object literal for the '<em><b>User Authorization</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__USER_AUTHORIZATION = eINSTANCE.getProject_UserAuthorization();

        /**
         * The meta object literal for the '<em><b>Allowed Components</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__ALLOWED_COMPONENTS = eINSTANCE.getProject_AllowedComponents();

        /**
         * The meta object literal for the '<em><b>Referenced Projects</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__REFERENCED_PROJECTS = eINSTANCE.getProject_ReferencedProjects();

        /**
         * The meta object literal for the '<em><b>Available Ref Project</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__AVAILABLE_REF_PROJECT = eINSTANCE.getProject_AvailableRefProject();

        /**
         * The meta object literal for the '<em><b>Migration Tasks</b></em>' attribute list feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__MIGRATION_TASKS = eINSTANCE.getProject_MigrationTasks();

        /**
         * The meta object literal for the '<em><b>Migration Task</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__MIGRATION_TASK = eINSTANCE.getProject_MigrationTask();

        /**
         * The meta object literal for the '<em><b>Big Data</b></em>' attribute feature.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @generated
         */
		EAttribute PROJECT__BIG_DATA = eINSTANCE.getProject_BigData();

								/**
         * The meta object literal for the '<em><b>Master Job Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__MASTER_JOB_ID = eINSTANCE.getProject_MasterJobId();

        /**
         * The meta object literal for the '<em><b>Spago Bi Server</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__SPAGO_BI_SERVER = eINSTANCE.getProject_SpagoBiServer();

        /**
         * The meta object literal for the '<em><b>Product Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__PRODUCT_VERSION = eINSTANCE.getProject_ProductVersion();

        /**
         * The meta object literal for the '<em><b>Components Settings</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__COMPONENTS_SETTINGS = eINSTANCE.getProject_ComponentsSettings();

        /**
         * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__URL = eINSTANCE.getProject_Url();

        /**
         * The meta object literal for the '<em><b>Stat And Logs Settings</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__STAT_AND_LOGS_SETTINGS = eINSTANCE.getProject_StatAndLogsSettings();

        /**
         * The meta object literal for the '<em><b>Implicit Context Settings</b></em>' containment reference feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__IMPLICIT_CONTEXT_SETTINGS = eINSTANCE.getProject_ImplicitContextSettings();

        /**
         * The meta object literal for the '<em><b>Hide Password</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__HIDE_PASSWORD = eINSTANCE.getProject_HidePassword();

        /**
         * The meta object literal for the '<em><b>Items Relations</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__ITEMS_RELATIONS = eINSTANCE.getProject_ItemsRelations();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__REFERENCE = eINSTANCE.getProject_Reference();

        /**
         * The meta object literal for the '<em><b>Custom Component Settings</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__CUSTOM_COMPONENT_SETTINGS = eINSTANCE.getProject_CustomComponentSettings();

        /**
         * The meta object literal for the '<em><b>Deleted Folders</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__DELETED_FOLDERS = eINSTANCE.getProject_DeletedFolders();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__TYPE = eINSTANCE.getProject_Type();

        /**
         * The meta object literal for the '<em><b>Items Relation Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__ITEMS_RELATION_VERSION = eINSTANCE.getProject_ItemsRelationVersion();

        /**
         * The meta object literal for the '<em><b>Exchange User</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT__EXCHANGE_USER = eINSTANCE.getProject_ExchangeUser();

        /**
         * The meta object literal for the '<em><b>Technical Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__TECHNICAL_LABEL = eINSTANCE.getProject_TechnicalLabel();

        /**
         * The meta object literal for the '<em><b>Local</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__LOCAL = eINSTANCE.getProject_Local();

        /**
         * The meta object literal for the '<em><b>Folders</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROJECT__FOLDERS = eINSTANCE.getProject_Folders();

        /**
         * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__DELETED = eINSTANCE.getProject_Deleted();

        /**
         * The meta object literal for the '<em><b>Delete Date</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__DELETE_DATE = eINSTANCE.getProject_DeleteDate();

        /**
         * The meta object literal for the '<em><b>Creation Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT__CREATION_DATE = eINSTANCE.getProject_CreationDate();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.PropertyImpl <em>Property</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.PropertyImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProperty()
         * @generated
         */
        EClass PROPERTY = eINSTANCE.getProperty();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__ID = eINSTANCE.getProperty_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__LABEL = eINSTANCE.getProperty_Label();

        /**
         * The meta object literal for the '<em><b>Purpose</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__PURPOSE = eINSTANCE.getProperty_Purpose();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__DESCRIPTION = eINSTANCE.getProperty_Description();

        /**
         * The meta object literal for the '<em><b>Creation Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__CREATION_DATE = eINSTANCE.getProperty_CreationDate();

        /**
         * The meta object literal for the '<em><b>Modification Date</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__MODIFICATION_DATE = eINSTANCE.getProperty_ModificationDate();

        /**
         * The meta object literal for the '<em><b>Author</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROPERTY__AUTHOR = eINSTANCE.getProperty_Author();

        /**
         * The meta object literal for the '<em><b>Informations</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference PROPERTY__INFORMATIONS = eINSTANCE.getProperty_Informations();

        /**
         * The meta object literal for the '<em><b>Max Information Level</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute PROPERTY__MAX_INFORMATION_LEVEL = eINSTANCE.getProperty_MaxInformationLevel();

        /**
         * The meta object literal for the '<em><b>Old Status Code</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__OLD_STATUS_CODE = eINSTANCE.getProperty_OldStatusCode();

        /**
         * The meta object literal for the '<em><b>Additional Properties</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY__ADDITIONAL_PROPERTIES = eINSTANCE.getProperty_AdditionalProperties();

        /**
         * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__DISPLAY_NAME = eINSTANCE.getProperty_DisplayName();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__VERSION = eINSTANCE.getProperty_Version();

        /**
         * The meta object literal for the '<em><b>Status Code</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__STATUS_CODE = eINSTANCE.getProperty_StatusCode();

        /**
         * The meta object literal for the '<em><b>Item</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROPERTY__ITEM = eINSTANCE.getProperty_Item();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ItemImpl <em>Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItem()
         * @generated
         */
        EClass ITEM = eINSTANCE.getItem();

        /**
         * The meta object literal for the '<em><b>Property</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference ITEM__PROPERTY = eINSTANCE.getItem_Property();

        /**
         * The meta object literal for the '<em><b>State</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITEM__STATE = eINSTANCE.getItem_State();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITEM__PARENT = eINSTANCE.getItem_Parent();

        /**
         * The meta object literal for the '<em><b>Reference Resources</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITEM__REFERENCE_RESOURCES = eINSTANCE.getItem_ReferenceResources();

        /**
         * The meta object literal for the '<em><b>File Extension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ITEM__FILE_EXTENSION = eINSTANCE.getItem_FileExtension();

        /**
         * The meta object literal for the '<em><b>Need Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ITEM__NEED_VERSION = eINSTANCE.getItem_NeedVersion();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LinkDocumentationItemImpl <em>Link Documentation Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LinkDocumentationItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkDocumentationItem()
         * @generated
         */
        EClass LINK_DOCUMENTATION_ITEM = eINSTANCE.getLinkDocumentationItem();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_DOCUMENTATION_ITEM__NAME = eINSTANCE.getLinkDocumentationItem_Name();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_DOCUMENTATION_ITEM__EXTENSION = eINSTANCE.getLinkDocumentationItem_Extension();

        /**
         * The meta object literal for the '<em><b>Link</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference LINK_DOCUMENTATION_ITEM__LINK = eINSTANCE.getLinkDocumentationItem_Link();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LinkTypeImpl <em>Link Type</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LinkTypeImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkType()
         * @generated
         */
        EClass LINK_TYPE = eINSTANCE.getLinkType();

        /**
         * The meta object literal for the '<em><b>URI</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_TYPE__URI = eINSTANCE.getLinkType_URI();

        /**
         * The meta object literal for the '<em><b>State</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_TYPE__STATE = eINSTANCE.getLinkType_State();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.BusinessProcessItemImpl <em>Business Process Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.BusinessProcessItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getBusinessProcessItem()
         * @generated
         */
        EClass BUSINESS_PROCESS_ITEM = eINSTANCE.getBusinessProcessItem();

        /**
         * The meta object literal for the '<em><b>Notation</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference BUSINESS_PROCESS_ITEM__NOTATION = eINSTANCE.getBusinessProcessItem_Notation();

        /**
         * The meta object literal for the '<em><b>Semantic</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference BUSINESS_PROCESS_ITEM__SEMANTIC = eINSTANCE.getBusinessProcessItem_Semantic();

        /**
         * The meta object literal for the '<em><b>Notation Holder</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BUSINESS_PROCESS_ITEM__NOTATION_HOLDER = eINSTANCE.getBusinessProcessItem_NotationHolder();

        /**
         * The meta object literal for the '<em><b>Svg Business Process Item</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference BUSINESS_PROCESS_ITEM__SVG_BUSINESS_PROCESS_ITEM = eINSTANCE.getBusinessProcessItem_SvgBusinessProcessItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ItemStateImpl <em>Item State</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ItemStateImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemState()
         * @generated
         */
        EClass ITEM_STATE = eINSTANCE.getItemState();

        /**
         * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_STATE__PATH = eINSTANCE.getItemState_Path();

        /**
         * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_STATE__DELETED = eINSTANCE.getItemState_Deleted();

        /**
         * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_STATE__LOCKED = eINSTANCE.getItemState_Locked();

        /**
         * The meta object literal for the '<em><b>Locker</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference ITEM_STATE__LOCKER = eINSTANCE.getItemState_Locker();

        /**
         * The meta object literal for the '<em><b>Lock Date</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_STATE__LOCK_DATE = eINSTANCE.getItemState_LockDate();

        /**
         * The meta object literal for the '<em><b>Commit Date</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_STATE__COMMIT_DATE = eINSTANCE.getItemState_CommitDate();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.FileItemImpl <em>File Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.FileItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileItem()
         * @generated
         */
        EClass FILE_ITEM = eINSTANCE.getFileItem();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILE_ITEM__NAME = eINSTANCE.getFileItem_Name();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILE_ITEM__EXTENSION = eINSTANCE.getFileItem_Extension();

        /**
         * The meta object literal for the '<em><b>Content</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILE_ITEM__CONTENT = eINSTANCE.getFileItem_Content();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.DocumentationItemImpl <em>Documentation Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.DocumentationItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDocumentationItem()
         * @generated
         */
        EClass DOCUMENTATION_ITEM = eINSTANCE.getDocumentationItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.RoutineItemImpl <em>Routine Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.RoutineItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRoutineItem()
         * @generated
         */
        EClass ROUTINE_ITEM = eINSTANCE.getRoutineItem();

        /**
         * The meta object literal for the '<em><b>Built In</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ROUTINE_ITEM__BUILT_IN = eINSTANCE.getRoutineItem_BuiltIn();

        /**
         * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ROUTINE_ITEM__IMPORTS = eINSTANCE.getRoutineItem_Imports();

        /**
         * The meta object literal for the '<em><b>Package Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ROUTINE_ITEM__PACKAGE_TYPE = eINSTANCE.getRoutineItem_PackageType();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ByteArrayImpl <em>Byte Array</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ByteArrayImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getByteArray()
         * @generated
         */
        EClass BYTE_ARRAY = eINSTANCE.getByteArray();

        /**
         * The meta object literal for the '<em><b>Inner Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BYTE_ARRAY__INNER_CONTENT = eINSTANCE.getByteArray_InnerContent();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ConnectionItemImpl <em>Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getConnectionItem()
         * @generated
         */
        EClass CONNECTION_ITEM = eINSTANCE.getConnectionItem();

        /**
         * The meta object literal for the '<em><b>Connection</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference CONNECTION_ITEM__CONNECTION = eINSTANCE.getConnectionItem_Connection();

        /**
         * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONNECTION_ITEM__TYPE_NAME = eINSTANCE.getConnectionItem_TypeName();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SnippetVariableImpl <em>Snippet Variable</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SnippetVariableImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSnippetVariable()
         * @generated
         */
        EClass SNIPPET_VARIABLE = eINSTANCE.getSnippetVariable();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_VARIABLE__NAME = eINSTANCE.getSnippetVariable_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_VARIABLE__VALUE = eINSTANCE.getSnippetVariable_Value();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_VARIABLE__DESCRIPTION = eINSTANCE.getSnippetVariable_Description();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_VARIABLE__ID = eINSTANCE.getSnippetVariable_Id();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SnippetItemImpl <em>Snippet Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SnippetItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSnippetItem()
         * @generated
         */
        EClass SNIPPET_ITEM = eINSTANCE.getSnippetItem();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_ITEM__NAME = eINSTANCE.getSnippetItem_Name();

        /**
         * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SNIPPET_ITEM__CONTENT = eINSTANCE.getSnippetItem_Content();

        /**
         * The meta object literal for the '<em><b>Variables</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SNIPPET_ITEM__VARIABLES = eINSTANCE.getSnippetItem_Variables();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.DelimitedFileConnectionItemImpl <em>Delimited File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.DelimitedFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDelimitedFileConnectionItem()
         * @generated
         */
        EClass DELIMITED_FILE_CONNECTION_ITEM = eINSTANCE.getDelimitedFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.PositionalFileConnectionItemImpl <em>Positional File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.PositionalFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPositionalFileConnectionItem()
         * @generated
         */
        EClass POSITIONAL_FILE_CONNECTION_ITEM = eINSTANCE.getPositionalFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.RegExFileConnectionItemImpl <em>Reg Ex File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.RegExFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRegExFileConnectionItem()
         * @generated
         */
        EClass REG_EX_FILE_CONNECTION_ITEM = eINSTANCE.getRegExFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.CSVFileConnectionItemImpl <em>CSV File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.CSVFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCSVFileConnectionItem()
         * @generated
         */
        EClass CSV_FILE_CONNECTION_ITEM = eINSTANCE.getCSVFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.DatabaseConnectionItemImpl <em>Database Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.DatabaseConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDatabaseConnectionItem()
         * @generated
         */
        EClass DATABASE_CONNECTION_ITEM = eINSTANCE.getDatabaseConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SAPConnectionItemImpl <em>SAP Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SAPConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSAPConnectionItem()
         * @generated
         */
        EClass SAP_CONNECTION_ITEM = eINSTANCE.getSAPConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ProcessItemImpl <em>Process Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ProcessItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProcessItem()
         * @generated
         */
        EClass PROCESS_ITEM = eINSTANCE.getProcessItem();

        /**
         * The meta object literal for the '<em><b>Process</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROCESS_ITEM__PROCESS = eINSTANCE.getProcessItem_Process();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserImpl <em>User</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUser()
         * @generated
         */
        EClass USER = eINSTANCE.getUser();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__ID = eINSTANCE.getUser_Id();

        /**
         * The meta object literal for the '<em><b>Login</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__LOGIN = eINSTANCE.getUser_Login();

        /**
         * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__PASSWORD = eINSTANCE.getUser_Password();

        /**
         * The meta object literal for the '<em><b>First Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__FIRST_NAME = eINSTANCE.getUser_FirstName();

        /**
         * The meta object literal for the '<em><b>Last Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__LAST_NAME = eINSTANCE.getUser_LastName();

        /**
         * The meta object literal for the '<em><b>Creation Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__CREATION_DATE = eINSTANCE.getUser_CreationDate();

        /**
         * The meta object literal for the '<em><b>Delete Date</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__DELETE_DATE = eINSTANCE.getUser_DeleteDate();

        /**
         * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__DELETED = eINSTANCE.getUser_Deleted();

        /**
         * The meta object literal for the '<em><b>Allowed To Modify Components</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__ALLOWED_TO_MODIFY_COMPONENTS = eINSTANCE.getUser_AllowedToModifyComponents();

        /**
         * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__COMMENT = eINSTANCE.getUser_Comment();

        /**
         * The meta object literal for the '<em><b>Project Authorization</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference USER__PROJECT_AUTHORIZATION = eINSTANCE.getUser_ProjectAuthorization();

        /**
         * The meta object literal for the '<em><b>Module Authorization</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference USER__MODULE_AUTHORIZATION = eINSTANCE.getUser_ModuleAuthorization();

        /**
         * The meta object literal for the '<em><b>Preferred Dashboard Connection</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference USER__PREFERRED_DASHBOARD_CONNECTION = eINSTANCE.getUser_PreferredDashboardConnection();

        /**
         * The meta object literal for the '<em><b>Last Admin Connection Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__LAST_ADMIN_CONNECTION_DATE = eINSTANCE.getUser_LastAdminConnectionDate();

        /**
         * The meta object literal for the '<em><b>Last Studio Connection Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__LAST_STUDIO_CONNECTION_DATE = eINSTANCE.getUser_LastStudioConnectionDate();

        /**
         * The meta object literal for the '<em><b>First Admin Connection Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__FIRST_ADMIN_CONNECTION_DATE = eINSTANCE.getUser_FirstAdminConnectionDate();

        /**
         * The meta object literal for the '<em><b>First Studio Connection Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__FIRST_STUDIO_CONNECTION_DATE = eINSTANCE.getUser_FirstStudioConnectionDate();

        /**
         * The meta object literal for the '<em><b>Admin Connexion Number</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__ADMIN_CONNEXION_NUMBER = eINSTANCE.getUser_AdminConnexionNumber();

        /**
         * The meta object literal for the '<em><b>Studio Connexion Number</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute USER__STUDIO_CONNEXION_NUMBER = eINSTANCE.getUser_StudioConnexionNumber();

        /**
         * The meta object literal for the '<em><b>Authentication Info</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__AUTHENTICATION_INFO = eINSTANCE.getUser_AuthenticationInfo();

        /**
         * The meta object literal for the '<em><b>Ldap Login</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__LDAP_LOGIN = eINSTANCE.getUser_LdapLogin();

        /**
         * The meta object literal for the '<em><b>Ldap Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__LDAP_ID = eINSTANCE.getUser_LdapId();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER__LANGUAGE = eINSTANCE.getUser_Language();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__TYPE = eINSTANCE.getUser_Type();

        /**
         * The meta object literal for the '<em><b>Additionnal Data</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__ADDITIONNAL_DATA = eINSTANCE.getUser_AdditionnalData();

        /**
         * The meta object literal for the '<em><b>Technical</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER__TECHNICAL = eINSTANCE.getUser_Technical();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.FolderItemImpl <em>Folder Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.FolderItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFolderItem()
         * @generated
         */
        EClass FOLDER_ITEM = eINSTANCE.getFolderItem();

        /**
         * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference FOLDER_ITEM__CHILDREN = eINSTANCE.getFolderItem_Children();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FOLDER_ITEM__TYPE = eINSTANCE.getFolderItem_Type();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ComponentImpl <em>Component</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ComponentImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getComponent()
         * @generated
         */
        EClass COMPONENT = eINSTANCE.getComponent();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__ID = eINSTANCE.getComponent_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__LABEL = eINSTANCE.getComponent_Label();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__VERSION = eINSTANCE.getComponent_Version();

        /**
         * The meta object literal for the '<em><b>Last Update Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__LAST_UPDATE_DATE = eINSTANCE.getComponent_LastUpdateDate();

        /**
         * The meta object literal for the '<em><b>Projects</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT__PROJECTS = eINSTANCE.getComponent_Projects();

        /**
         * The meta object literal for the '<em><b>Author</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference COMPONENT__AUTHOR = eINSTANCE.getComponent_Author();

        /**
         * The meta object literal for the '<em><b>Creation Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__CREATION_DATE = eINSTANCE.getComponent_CreationDate();

        /**
         * The meta object literal for the '<em><b>Delete Date</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__DELETE_DATE = eINSTANCE.getComponent_DeleteDate();

        /**
         * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__DELETED = eINSTANCE.getComponent_Deleted();

        /**
         * The meta object literal for the '<em><b>File Descriptor</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__FILE_DESCRIPTOR = eINSTANCE.getComponent_FileDescriptor();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.XmlFileConnectionItemImpl <em>Xml File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.XmlFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getXmlFileConnectionItem()
         * @generated
         */
        EClass XML_FILE_CONNECTION_ITEM = eINSTANCE.getXmlFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.NotationHolderImpl <em>Notation Holder</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.NotationHolderImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getNotationHolder()
         * @generated
         */
        EClass NOTATION_HOLDER = eINSTANCE.getNotationHolder();

        /**
         * The meta object literal for the '<em><b>Notation String</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute NOTATION_HOLDER__NOTATION_STRING = eINSTANCE.getNotationHolder_NotationString();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ProjectComponentAuthorisationImpl <em>Project Component Authorisation</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ProjectComponentAuthorisationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProjectComponentAuthorisation()
         * @generated
         */
        EClass PROJECT_COMPONENT_AUTHORISATION = eINSTANCE.getProjectComponentAuthorisation();

        /**
         * The meta object literal for the '<em><b>Project</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROJECT_COMPONENT_AUTHORISATION__PROJECT = eINSTANCE.getProjectComponentAuthorisation_Project();

        /**
         * The meta object literal for the '<em><b>Component</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROJECT_COMPONENT_AUTHORISATION__COMPONENT = eINSTANCE.getProjectComponentAuthorisation_Component();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ProjectReferenceImpl <em>Project Reference</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ProjectReferenceImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getProjectReference()
         * @generated
         */
        EClass PROJECT_REFERENCE = eINSTANCE.getProjectReference();

        /**
         * The meta object literal for the '<em><b>Project</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference PROJECT_REFERENCE__PROJECT = eINSTANCE.getProjectReference_Project();

        /**
         * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute PROJECT_REFERENCE__BRANCH = eINSTANCE.getProjectReference_Branch();

        /**
         * The meta object literal for the '<em><b>Referenced Project</b></em>' reference feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECT_REFERENCE__REFERENCED_PROJECT = eINSTANCE.getProjectReference_ReferencedProject();

        /**
         * The meta object literal for the '<em><b>Referenced Branch</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROJECT_REFERENCE__REFERENCED_BRANCH = eINSTANCE.getProjectReference_ReferencedBranch();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LdifFileConnectionItemImpl <em>Ldif File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LdifFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLdifFileConnectionItem()
         * @generated
         */
        EClass LDIF_FILE_CONNECTION_ITEM = eINSTANCE.getLdifFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExcelFileConnectionItemImpl <em>Excel File Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExcelFileConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExcelFileConnectionItem()
         * @generated
         */
        EClass EXCEL_FILE_CONNECTION_ITEM = eINSTANCE.getExcelFileConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.EbcdicConnectionItemImpl <em>Ebcdic Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.EbcdicConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getEbcdicConnectionItem()
         * @generated
         */
        EClass EBCDIC_CONNECTION_ITEM = eINSTANCE.getEbcdicConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.MDMConnectionItemImpl <em>MDM Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.MDMConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMDMConnectionItem()
         * @generated
         */
        EClass MDM_CONNECTION_ITEM = eINSTANCE.getMDMConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ValidationRulesConnectionItemImpl <em>Validation Rules Connection Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ValidationRulesConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getValidationRulesConnectionItem()
         * @generated
         */
        EClass VALIDATION_RULES_CONNECTION_ITEM = eINSTANCE.getValidationRulesConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserProjectAuthorizationImpl <em>User Project Authorization</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserProjectAuthorizationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserProjectAuthorization()
         * @generated
         */
        EClass USER_PROJECT_AUTHORIZATION = eINSTANCE.getUserProjectAuthorization();

        /**
         * The meta object literal for the '<em><b>User</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference USER_PROJECT_AUTHORIZATION__USER = eINSTANCE.getUserProjectAuthorization_User();

        /**
         * The meta object literal for the '<em><b>Project</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference USER_PROJECT_AUTHORIZATION__PROJECT = eINSTANCE.getUserProjectAuthorization_Project();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_PROJECT_AUTHORIZATION__TYPE = eINSTANCE.getUserProjectAuthorization_Type();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ContextItemImpl <em>Context Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ContextItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getContextItem()
         * @generated
         */
        EClass CONTEXT_ITEM = eINSTANCE.getContextItem();

        /**
         * The meta object literal for the '<em><b>Context</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTEXT_ITEM__CONTEXT = eINSTANCE.getContextItem_Context();

        /**
         * The meta object literal for the '<em><b>Default Context</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTEXT_ITEM__DEFAULT_CONTEXT = eINSTANCE.getContextItem_DefaultContext();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SpagoBiServerImpl <em>Spago Bi Server</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SpagoBiServerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSpagoBiServer()
         * @generated
         */
        EClass SPAGO_BI_SERVER = eINSTANCE.getSpagoBiServer();

        /**
         * The meta object literal for the '<em><b>Engine Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__ENGINE_NAME = eINSTANCE.getSpagoBiServer_EngineName();

        /**
         * The meta object literal for the '<em><b>Short Description</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__SHORT_DESCRIPTION = eINSTANCE.getSpagoBiServer_ShortDescription();

        /**
         * The meta object literal for the '<em><b>Host</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__HOST = eINSTANCE.getSpagoBiServer_Host();

        /**
         * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__PORT = eINSTANCE.getSpagoBiServer_Port();

        /**
         * The meta object literal for the '<em><b>Login</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__LOGIN = eINSTANCE.getSpagoBiServer_Login();

        /**
         * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__PASSWORD = eINSTANCE.getSpagoBiServer_Password();

        /**
         * The meta object literal for the '<em><b>Application Context</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPAGO_BI_SERVER__APPLICATION_CONTEXT = eINSTANCE.getSpagoBiServer_ApplicationContext();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LicenseImpl <em>License</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LicenseImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLicense()
         * @generated
         */
        EClass LICENSE = eINSTANCE.getLicense();

        /**
         * The meta object literal for the '<em><b>License</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LICENSE__LICENSE = eINSTANCE.getLicense_License();

        /**
         * The meta object literal for the '<em><b>Customer Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LICENSE__CUSTOMER_NAME = eINSTANCE.getLicense_CustomerName();

        /**
         * The meta object literal for the '<em><b>Params</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LICENSE__PARAMS = eINSTANCE.getLicense_Params();

        /**
         * The meta object literal for the '<em><b>Token</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LICENSE__TOKEN = eINSTANCE.getLicense_Token();

        /**
         * The meta object literal for the '<em><b>Date Token Check</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LICENSE__DATE_TOKEN_CHECK = eINSTANCE.getLicense_DateTokenCheck();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.GenericSchemaConnectionItemImpl <em>Generic Schema Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.GenericSchemaConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getGenericSchemaConnectionItem()
         * @generated
         */
        EClass GENERIC_SCHEMA_CONNECTION_ITEM = eINSTANCE.getGenericSchemaConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserModuleAuthorizationImpl <em>User Module Authorization</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserModuleAuthorizationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserModuleAuthorization()
         * @generated
         */
        EClass USER_MODULE_AUTHORIZATION = eINSTANCE.getUserModuleAuthorization();

        /**
         * The meta object literal for the '<em><b>User</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference USER_MODULE_AUTHORIZATION__USER = eINSTANCE.getUserModuleAuthorization_User();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_MODULE_AUTHORIZATION__TYPE = eINSTANCE.getUserModuleAuthorization_Type();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LDAPSchemaConnectionItemImpl <em>LDAP Schema Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LDAPSchemaConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLDAPSchemaConnectionItem()
         * @generated
         */
        EClass LDAP_SCHEMA_CONNECTION_ITEM = eINSTANCE.getLDAPSchemaConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SalesforceSchemaConnectionItemImpl <em>Salesforce Schema Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SalesforceSchemaConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSalesforceSchemaConnectionItem()
         * @generated
         */
        EClass SALESFORCE_SCHEMA_CONNECTION_ITEM = eINSTANCE.getSalesforceSchemaConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.DashboardConnectionImpl <em>Dashboard Connection</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.DashboardConnectionImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getDashboardConnection()
         * @generated
         */
        EClass DASHBOARD_CONNECTION = eINSTANCE.getDashboardConnection();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__ID = eINSTANCE.getDashboardConnection_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__LABEL = eINSTANCE.getDashboardConnection_Label();

        /**
         * The meta object literal for the '<em><b>Dialect</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__DIALECT = eINSTANCE.getDashboardConnection_Dialect();

        /**
         * The meta object literal for the '<em><b>Host</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__HOST = eINSTANCE.getDashboardConnection_Host();

        /**
         * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__PORT = eINSTANCE.getDashboardConnection_Port();

        /**
         * The meta object literal for the '<em><b>Database</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__DATABASE = eINSTANCE.getDashboardConnection_Database();

        /**
         * The meta object literal for the '<em><b>Username</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__USERNAME = eINSTANCE.getDashboardConnection_Username();

        /**
         * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__PASSWORD = eINSTANCE.getDashboardConnection_Password();

        /**
         * The meta object literal for the '<em><b>Log Table</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__LOG_TABLE = eINSTANCE.getDashboardConnection_LogTable();

        /**
         * The meta object literal for the '<em><b>Stat Table</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__STAT_TABLE = eINSTANCE.getDashboardConnection_StatTable();

        /**
         * The meta object literal for the '<em><b>Flow Meter Table</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__FLOW_METER_TABLE = eINSTANCE.getDashboardConnection_FlowMeterTable();

        /**
         * The meta object literal for the '<em><b>Additionnals Params</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__ADDITIONNALS_PARAMS = eINSTANCE.getDashboardConnection_AdditionnalsParams();

        /**
         * The meta object literal for the '<em><b>Datasource</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute DASHBOARD_CONNECTION__DATASOURCE = eINSTANCE.getDashboardConnection_Datasource();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.ExecutionTriggerable <em>Execution Triggerable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.ExecutionTriggerable
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTriggerable()
         * @generated
         */
        EClass EXECUTION_TRIGGERABLE = eINSTANCE.getExecutionTriggerable();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__ID = eINSTANCE.getExecutionTriggerable_Id();

        /**
         * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TRIGGERABLE__TRIGGERS = eINSTANCE.getExecutionTriggerable_Triggers();

        /**
         * The meta object literal for the '<em><b>Id Quartz Job</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__ID_QUARTZ_JOB = eINSTANCE.getExecutionTriggerable_IdQuartzJob();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__STATUS = eINSTANCE.getExecutionTriggerable_Status();

        /**
         * The meta object literal for the '<em><b>Error Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__ERROR_STATUS = eINSTANCE.getExecutionTriggerable_ErrorStatus();

        /**
         * The meta object literal for the '<em><b>Concurrent Execution</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__CONCURRENT_EXECUTION = eINSTANCE.getExecutionTriggerable_ConcurrentExecution();

        /**
         * The meta object literal for the '<em><b>Processing State</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__PROCESSING_STATE = eINSTANCE.getExecutionTriggerable_ProcessingState();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TRIGGERABLE__REQUEST_ID = eINSTANCE.getExecutionTriggerable_RequestId();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionServerImpl <em>Execution Server</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionServerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionServer()
         * @generated
         */
        EClass EXECUTION_SERVER = eINSTANCE.getExecutionServer();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__ID = eINSTANCE.getExecutionServer_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__LABEL = eINSTANCE.getExecutionServer_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__DESCRIPTION = eINSTANCE.getExecutionServer_Description();

        /**
         * The meta object literal for the '<em><b>Host</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__HOST = eINSTANCE.getExecutionServer_Host();

        /**
         * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__PORT = eINSTANCE.getExecutionServer_Port();

        /**
         * The meta object literal for the '<em><b>File Transfert Port</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__FILE_TRANSFERT_PORT = eINSTANCE.getExecutionServer_FileTransfertPort();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__ACTIVE = eINSTANCE.getExecutionServer_Active();

        /**
         * The meta object literal for the '<em><b>Monitoring Port</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__MONITORING_PORT = eINSTANCE.getExecutionServer_MonitoringPort();

        /**
         * The meta object literal for the '<em><b>Timeout Unknown State</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__TIMEOUT_UNKNOWN_STATE = eINSTANCE.getExecutionServer_TimeoutUnknownState();

        /**
         * The meta object literal for the '<em><b>Username</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__USERNAME = eINSTANCE.getExecutionServer_Username();

        /**
         * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__PASSWORD = eINSTANCE.getExecutionServer_Password();

        /**
         * The meta object literal for the '<em><b>Jmx Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__JMX_URL = eINSTANCE.getExecutionServer_JmxUrl();

        /**
         * The meta object literal for the '<em><b>Web Console Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__WEB_CONSOLE_URL = eINSTANCE.getExecutionServer_WebConsoleUrl();

        /**
         * The meta object literal for the '<em><b>Talend Runtime</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__TALEND_RUNTIME = eINSTANCE.getExecutionServer_TalendRuntime();

        /**
         * The meta object literal for the '<em><b>Mgmt Server Port</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__MGMT_SERVER_PORT = eINSTANCE.getExecutionServer_MgmtServerPort();

        /**
         * The meta object literal for the '<em><b>Mgmt Reg Port</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__MGMT_REG_PORT = eINSTANCE.getExecutionServer_MgmtRegPort();

        /**
         * The meta object literal for the '<em><b>Admin Console Port</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__ADMIN_CONSOLE_PORT = eINSTANCE.getExecutionServer_AdminConsolePort();

        /**
         * The meta object literal for the '<em><b>Use SSL</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__USE_SSL = eINSTANCE.getExecutionServer_UseSSL();

        /**
         * The meta object literal for the '<em><b>Instance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_SERVER__INSTANCE = eINSTANCE.getExecutionServer_Instance();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionTaskImpl <em>Execution Task</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionTaskImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTask()
         * @generated
         */
        EClass EXECUTION_TASK = eINSTANCE.getExecutionTask();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__LABEL = eINSTANCE.getExecutionTask_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__DESCRIPTION = eINSTANCE.getExecutionTask_Description();

        /**
         * The meta object literal for the '<em><b>Execution Server</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TASK__EXECUTION_SERVER = eINSTANCE.getExecutionTask_ExecutionServer();

        /**
         * The meta object literal for the '<em><b>Project</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TASK__PROJECT = eINSTANCE.getExecutionTask_Project();

        /**
         * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__BRANCH = eINSTANCE.getExecutionTask_Branch();

        /**
         * The meta object literal for the '<em><b>Context</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__CONTEXT = eINSTANCE.getExecutionTask_Context();

        /**
         * The meta object literal for the '<em><b>Job Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__JOB_VERSION = eINSTANCE.getExecutionTask_JobVersion();

        /**
         * The meta object literal for the '<em><b>Regenerate Job On Change</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__REGENERATE_JOB_ON_CHANGE = eINSTANCE.getExecutionTask_RegenerateJobOnChange();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__ACTIVE = eINSTANCE.getExecutionTask_Active();

        /**
         * The meta object literal for the '<em><b>Last Script Generation Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__LAST_SCRIPT_GENERATION_DATE = eINSTANCE.getExecutionTask_LastScriptGenerationDate();

        /**
         * The meta object literal for the '<em><b>Generated Svn Revision</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__GENERATED_SVN_REVISION = eINSTANCE.getExecutionTask_GeneratedSvnRevision();

        /**
         * The meta object literal for the '<em><b>Id Remote Job</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__ID_REMOTE_JOB = eINSTANCE.getExecutionTask_IdRemoteJob();

        /**
         * The meta object literal for the '<em><b>Id Remote Job Execution</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__ID_REMOTE_JOB_EXECUTION = eINSTANCE.getExecutionTask_IdRemoteJobExecution();

        /**
         * The meta object literal for the '<em><b>Checksum Archive</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__CHECKSUM_ARCHIVE = eINSTANCE.getExecutionTask_ChecksumArchive();

        /**
         * The meta object literal for the '<em><b>Job Script Archive Filename</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__JOB_SCRIPT_ARCHIVE_FILENAME = eINSTANCE.getExecutionTask_JobScriptArchiveFilename();

        /**
         * The meta object literal for the '<em><b>Last Run Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__LAST_RUN_DATE = eINSTANCE.getExecutionTask_LastRunDate();

        /**
         * The meta object literal for the '<em><b>Last Deployment Date</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__LAST_DEPLOYMENT_DATE = eINSTANCE.getExecutionTask_LastDeploymentDate();

        /**
         * The meta object literal for the '<em><b>Last Ended Run Date</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__LAST_ENDED_RUN_DATE = eINSTANCE.getExecutionTask_LastEndedRunDate();

        /**
         * The meta object literal for the '<em><b>Cmd Prms</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXECUTION_TASK__CMD_PRMS = eINSTANCE.getExecutionTask_CmdPrms();

        /**
         * The meta object literal for the '<em><b>Esb Properties Prms</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TASK__ESB_PROPERTIES_PRMS = eINSTANCE.getExecutionTask_EsbPropertiesPrms();

        /**
         * The meta object literal for the '<em><b>Job Prms</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXECUTION_TASK__JOB_PRMS = eINSTANCE.getExecutionTask_JobPrms();

        /**
         * The meta object literal for the '<em><b>Job Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__JOB_ID = eINSTANCE.getExecutionTask_JobId();

        /**
         * The meta object literal for the '<em><b>Virtual Server</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TASK__VIRTUAL_SERVER = eINSTANCE.getExecutionTask_VirtualServer();

        /**
         * The meta object literal for the '<em><b>Max Concurrent Executions</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__MAX_CONCURRENT_EXECUTIONS = eINSTANCE.getExecutionTask_MaxConcurrentExecutions();

        /**
         * The meta object literal for the '<em><b>Generated Project Name</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__GENERATED_PROJECT_NAME = eINSTANCE.getExecutionTask_GeneratedProjectName();

        /**
         * The meta object literal for the '<em><b>Generated Job Name</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__GENERATED_JOB_NAME = eINSTANCE.getExecutionTask_GeneratedJobName();

        /**
         * The meta object literal for the '<em><b>Generated Job Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__GENERATED_JOB_VERSION = eINSTANCE.getExecutionTask_GeneratedJobVersion();

        /**
         * The meta object literal for the '<em><b>Apply Context To Children</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__APPLY_CONTEXT_TO_CHILDREN = eINSTANCE.getExecutionTask_ApplyContextToChildren();

        /**
         * The meta object literal for the '<em><b>Error Stack Trace</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__ERROR_STACK_TRACE = eINSTANCE.getExecutionTask_ErrorStackTrace();

        /**
         * The meta object literal for the '<em><b>Last Triggering Date</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__LAST_TRIGGERING_DATE = eINSTANCE.getExecutionTask_LastTriggeringDate();

        /**
         * The meta object literal for the '<em><b>Exec Statistics Enabled</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__EXEC_STATISTICS_ENABLED = eINSTANCE.getExecutionTask_ExecStatisticsEnabled();

        /**
         * The meta object literal for the '<em><b>Add Statistics Code Enabled</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__ADD_STATISTICS_CODE_ENABLED = eINSTANCE.getExecutionTask_AddStatisticsCodeEnabled();

        /**
         * The meta object literal for the '<em><b>Owner Scheduler Instance Id</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute EXECUTION_TASK__OWNER_SCHEDULER_INSTANCE_ID = eINSTANCE.getExecutionTask_OwnerSchedulerInstanceId();

        /**
         * The meta object literal for the '<em><b>On Unknown State Job</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__ON_UNKNOWN_STATE_JOB = eINSTANCE.getExecutionTask_OnUnknownStateJob();

        /**
         * The meta object literal for the '<em><b>Use Latest Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__USE_LATEST_VERSION = eINSTANCE.getExecutionTask_UseLatestVersion();

        /**
         * The meta object literal for the '<em><b>Application Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__APPLICATION_TYPE = eINSTANCE.getExecutionTask_ApplicationType();

        /**
         * The meta object literal for the '<em><b>Repository Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__REPOSITORY_NAME = eINSTANCE.getExecutionTask_RepositoryName();

        /**
         * The meta object literal for the '<em><b>Features File Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__FEATURES_FILE_URL = eINSTANCE.getExecutionTask_FeaturesFileUrl();

        /**
         * The meta object literal for the '<em><b>Application Group</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__APPLICATION_GROUP = eINSTANCE.getExecutionTask_ApplicationGroup();

        /**
         * The meta object literal for the '<em><b>Bundle Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__BUNDLE_NAME = eINSTANCE.getExecutionTask_BundleName();

        /**
         * The meta object literal for the '<em><b>Property Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__PROPERTY_ID = eINSTANCE.getExecutionTask_PropertyId();

        /**
         * The meta object literal for the '<em><b>Feature Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__FEATURE_NAME = eINSTANCE.getExecutionTask_FeatureName();

        /**
         * The meta object literal for the '<em><b>Feature Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK__FEATURE_VERSION = eINSTANCE.getExecutionTask_FeatureVersion();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionTaskPropertiesImpl <em>Execution Task Properties</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionTaskPropertiesImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskProperties()
         * @generated
         */
        EClass EXECUTION_TASK_PROPERTIES = eINSTANCE.getExecutionTaskProperties();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_PROPERTIES__ID = eINSTANCE.getExecutionTaskProperties_Id();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_PROPERTIES__NAME = eINSTANCE.getExecutionTaskProperties_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_PROPERTIES__VALUE = eINSTANCE.getExecutionTaskProperties_Value();

        /**
         * The meta object literal for the '<em><b>Execution Task</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_TASK_PROPERTIES__EXECUTION_TASK = eINSTANCE.getExecutionTaskProperties_ExecutionTask();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionTaskCmdPrmImpl <em>Execution Task Cmd Prm</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionTaskCmdPrmImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskCmdPrm()
         * @generated
         */
        EClass EXECUTION_TASK_CMD_PRM = eINSTANCE.getExecutionTaskCmdPrm();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_CMD_PRM__ID = eINSTANCE.getExecutionTaskCmdPrm_Id();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_CMD_PRM__ACTIVE = eINSTANCE.getExecutionTaskCmdPrm_Active();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_CMD_PRM__PARAMETER = eINSTANCE.getExecutionTaskCmdPrm_Parameter();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_CMD_PRM__DESCRIPTION = eINSTANCE.getExecutionTaskCmdPrm_Description();

        /**
         * The meta object literal for the '<em><b>Execution Task</b></em>' container reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXECUTION_TASK_CMD_PRM__EXECUTION_TASK = eINSTANCE.getExecutionTaskCmdPrm_ExecutionTask();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionTaskJobPrmImpl <em>Execution Task Job Prm</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionTaskJobPrmImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionTaskJobPrm()
         * @generated
         */
        EClass EXECUTION_TASK_JOB_PRM = eINSTANCE.getExecutionTaskJobPrm();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__ID = eINSTANCE.getExecutionTaskJobPrm_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__LABEL = eINSTANCE.getExecutionTaskJobPrm_Label();

        /**
         * The meta object literal for the '<em><b>Override</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__OVERRIDE = eINSTANCE.getExecutionTaskJobPrm_Override();

        /**
         * The meta object literal for the '<em><b>Execution Task</b></em>' container reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXECUTION_TASK_JOB_PRM__EXECUTION_TASK = eINSTANCE.getExecutionTaskJobPrm_ExecutionTask();

        /**
         * The meta object literal for the '<em><b>Original Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__ORIGINAL_VALUE = eINSTANCE.getExecutionTaskJobPrm_OriginalValue();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__DEFAULT_VALUE = eINSTANCE.getExecutionTaskJobPrm_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Item Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_TASK_JOB_PRM__ITEM_TYPE = eINSTANCE.getExecutionTaskJobPrm_ItemType();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.TalendTriggerImpl <em>Talend Trigger</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.TalendTriggerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTalendTrigger()
         * @generated
         */
        EClass TALEND_TRIGGER = eINSTANCE.getTalendTrigger();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__ID = eINSTANCE.getTalendTrigger_Id();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__ACTIVE = eINSTANCE.getTalendTrigger_Active();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__LABEL = eINSTANCE.getTalendTrigger_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__DESCRIPTION = eINSTANCE.getTalendTrigger_Description();

        /**
         * The meta object literal for the '<em><b>Trigger Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__TRIGGER_TYPE = eINSTANCE.getTalendTrigger_TriggerType();

        /**
         * The meta object literal for the '<em><b>Execution Triggerable</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TALEND_TRIGGER__EXECUTION_TRIGGERABLE = eINSTANCE.getTalendTrigger_ExecutionTriggerable();

        /**
         * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__START_TIME = eINSTANCE.getTalendTrigger_StartTime();

        /**
         * The meta object literal for the '<em><b>End Time</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__END_TIME = eINSTANCE.getTalendTrigger_EndTime();

        /**
         * The meta object literal for the '<em><b>Previous Fire Time</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__PREVIOUS_FIRE_TIME = eINSTANCE.getTalendTrigger_PreviousFireTime();

        /**
         * The meta object literal for the '<em><b>Final Fire Time</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__FINAL_FIRE_TIME = eINSTANCE.getTalendTrigger_FinalFireTime();

        /**
         * The meta object literal for the '<em><b>Id Quartz Trigger</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__ID_QUARTZ_TRIGGER = eINSTANCE.getTalendTrigger_IdQuartzTrigger();

        /**
         * The meta object literal for the '<em><b>Resume Pause Updated</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__RESUME_PAUSE_UPDATED = eINSTANCE.getTalendTrigger_ResumePauseUpdated();

        /**
         * The meta object literal for the '<em><b>Previously Paused</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TALEND_TRIGGER__PREVIOUSLY_PAUSED = eINSTANCE.getTalendTrigger_PreviouslyPaused();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.CronTalendTriggerImpl <em>Cron Talend Trigger</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.CronTalendTriggerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCronTalendTrigger()
         * @generated
         */
        EClass CRON_TALEND_TRIGGER = eINSTANCE.getCronTalendTrigger();

        /**
         * The meta object literal for the '<em><b>Cron Expression</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CRON_TALEND_TRIGGER__CRON_EXPRESSION = eINSTANCE.getCronTalendTrigger_CronExpression();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.CronUITalendTriggerImpl <em>Cron UI Talend Trigger</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.CronUITalendTriggerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCronUITalendTrigger()
         * @generated
         */
        EClass CRON_UI_TALEND_TRIGGER = eINSTANCE.getCronUITalendTrigger();

        /**
         * The meta object literal for the '<em><b>List Days Of Week</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_WEEK = eINSTANCE.getCronUITalendTrigger_ListDaysOfWeek();

        /**
         * The meta object literal for the '<em><b>List Days Of Month</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_DAYS_OF_MONTH = eINSTANCE.getCronUITalendTrigger_ListDaysOfMonth();

        /**
         * The meta object literal for the '<em><b>List Months</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_MONTHS = eINSTANCE.getCronUITalendTrigger_ListMonths();

        /**
         * The meta object literal for the '<em><b>List Years</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_YEARS = eINSTANCE.getCronUITalendTrigger_ListYears();

        /**
         * The meta object literal for the '<em><b>List Hours</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_HOURS = eINSTANCE.getCronUITalendTrigger_ListHours();

        /**
         * The meta object literal for the '<em><b>List Minutes</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CRON_UI_TALEND_TRIGGER__LIST_MINUTES = eINSTANCE.getCronUITalendTrigger_ListMinutes();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SimpleTalendTriggerImpl <em>Simple Talend Trigger</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SimpleTalendTriggerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSimpleTalendTrigger()
         * @generated
         */
        EClass SIMPLE_TALEND_TRIGGER = eINSTANCE.getSimpleTalendTrigger();

        /**
         * The meta object literal for the '<em><b>Repeat Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SIMPLE_TALEND_TRIGGER__REPEAT_COUNT = eINSTANCE.getSimpleTalendTrigger_RepeatCount();

        /**
         * The meta object literal for the '<em><b>Repeat Interval</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SIMPLE_TALEND_TRIGGER__REPEAT_INTERVAL = eINSTANCE.getSimpleTalendTrigger_RepeatInterval();

        /**
         * The meta object literal for the '<em><b>Times Triggered</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SIMPLE_TALEND_TRIGGER__TIMES_TRIGGERED = eINSTANCE.getSimpleTalendTrigger_TimesTriggered();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionVirtualServerImpl <em>Execution Virtual Server</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionVirtualServerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionVirtualServer()
         * @generated
         */
        EClass EXECUTION_VIRTUAL_SERVER = eINSTANCE.getExecutionVirtualServer();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_VIRTUAL_SERVER__ID = eINSTANCE.getExecutionVirtualServer_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_VIRTUAL_SERVER__LABEL = eINSTANCE.getExecutionVirtualServer_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_VIRTUAL_SERVER__DESCRIPTION = eINSTANCE.getExecutionVirtualServer_Description();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_VIRTUAL_SERVER__ACTIVE = eINSTANCE.getExecutionVirtualServer_Active();

        /**
         * The meta object literal for the '<em><b>Execution Servers</b></em>' reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXECUTION_VIRTUAL_SERVER__EXECUTION_SERVERS = eINSTANCE.getExecutionVirtualServer_ExecutionServers();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.FileTriggerImpl <em>File Trigger</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.FileTriggerImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileTrigger()
         * @generated
         */
        EClass FILE_TRIGGER = eINSTANCE.getFileTrigger();

        /**
         * The meta object literal for the '<em><b>File Trigger Masks</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference FILE_TRIGGER__FILE_TRIGGER_MASKS = eINSTANCE.getFileTrigger_FileTriggerMasks();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.FileTriggerMaskImpl <em>File Trigger Mask</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.FileTriggerMaskImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFileTriggerMask()
         * @generated
         */
        EClass FILE_TRIGGER_MASK = eINSTANCE.getFileTriggerMask();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__ID = eINSTANCE.getFileTriggerMask_Id();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__ACTIVE = eINSTANCE.getFileTriggerMask_Active();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__LABEL = eINSTANCE.getFileTriggerMask_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__DESCRIPTION = eINSTANCE.getFileTriggerMask_Description();

        /**
         * The meta object literal for the '<em><b>File Trigger</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILE_TRIGGER_MASK__FILE_TRIGGER = eINSTANCE.getFileTriggerMask_FileTrigger();

        /**
         * The meta object literal for the '<em><b>Folder Path</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__FOLDER_PATH = eINSTANCE.getFileTriggerMask_FolderPath();

        /**
         * The meta object literal for the '<em><b>File Mask</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__FILE_MASK = eINSTANCE.getFileTriggerMask_FileMask();

        /**
         * The meta object literal for the '<em><b>Context Parameter Base Name</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__CONTEXT_PARAMETER_BASE_NAME = eINSTANCE.getFileTriggerMask_ContextParameterBaseName();

        /**
         * The meta object literal for the '<em><b>Check File Server</b></em>' reference feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EReference FILE_TRIGGER_MASK__CHECK_FILE_SERVER = eINSTANCE.getFileTriggerMask_CheckFileServer();

        /**
         * The meta object literal for the '<em><b>Exist</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__EXIST = eINSTANCE.getFileTriggerMask_Exist();

        /**
         * The meta object literal for the '<em><b>Created</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__CREATED = eINSTANCE.getFileTriggerMask_Created();

        /**
         * The meta object literal for the '<em><b>Modified</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute FILE_TRIGGER_MASK__MODIFIED = eINSTANCE.getFileTriggerMask_Modified();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.JobletProcessItemImpl <em>Joblet Process Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.JobletProcessItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobletProcessItem()
         * @generated
         */
        EClass JOBLET_PROCESS_ITEM = eINSTANCE.getJobletProcessItem();

        /**
         * The meta object literal for the '<em><b>Joblet Process</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference JOBLET_PROCESS_ITEM__JOBLET_PROCESS = eINSTANCE.getJobletProcessItem_JobletProcess();

        /**
         * The meta object literal for the '<em><b>Icon</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference JOBLET_PROCESS_ITEM__ICON = eINSTANCE.getJobletProcessItem_Icon();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.JobDocumentationItemImpl <em>Job Documentation Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.JobDocumentationItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobDocumentationItem()
         * @generated
         */
        EClass JOB_DOCUMENTATION_ITEM = eINSTANCE.getJobDocumentationItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.JobletDocumentationItemImpl <em>Joblet Documentation Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.JobletDocumentationItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobletDocumentationItem()
         * @generated
         */
        EClass JOBLET_DOCUMENTATION_ITEM = eINSTANCE.getJobletDocumentationItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.WSDLSchemaConnectionItemImpl <em>WSDL Schema Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.WSDLSchemaConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getWSDLSchemaConnectionItem()
         * @generated
         */
        EClass WSDL_SCHEMA_CONNECTION_ITEM = eINSTANCE.getWSDLSchemaConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SchemaInformationImpl <em>Schema Information</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SchemaInformationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSchemaInformation()
         * @generated
         */
        EClass SCHEMA_INFORMATION = eINSTANCE.getSchemaInformation();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_INFORMATION__VERSION = eINSTANCE.getSchemaInformation_Version();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.InformationImpl <em>Information</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.InformationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getInformation()
         * @generated
         */
        EClass INFORMATION = eINSTANCE.getInformation();

        /**
         * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute INFORMATION__LEVEL = eINSTANCE.getInformation_Level();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute INFORMATION__TYPE = eINSTANCE.getInformation_Type();

        /**
         * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute INFORMATION__TEXT = eINSTANCE.getInformation_Text();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SQLPatternItemImpl <em>SQL Pattern Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SQLPatternItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSQLPatternItem()
         * @generated
         */
        EClass SQL_PATTERN_ITEM = eINSTANCE.getSQLPatternItem();

        /**
         * The meta object literal for the '<em><b>System</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SQL_PATTERN_ITEM__SYSTEM = eINSTANCE.getSQLPatternItem_System();

        /**
         * The meta object literal for the '<em><b>Elt Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SQL_PATTERN_ITEM__ELT_NAME = eINSTANCE.getSQLPatternItem_EltName();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ComponentSettingImpl <em>Component Setting</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ComponentSettingImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getComponentSetting()
         * @generated
         */
        EClass COMPONENT_SETTING = eINSTANCE.getComponentSetting();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_SETTING__NAME = eINSTANCE.getComponentSetting_Name();

        /**
         * The meta object literal for the '<em><b>Hidden</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_SETTING__HIDDEN = eINSTANCE.getComponentSetting_Hidden();

        /**
         * The meta object literal for the '<em><b>Family</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_SETTING__FAMILY = eINSTANCE.getComponentSetting_Family();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.StatAndLogsSettingsImpl <em>Stat And Logs Settings</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.StatAndLogsSettingsImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getStatAndLogsSettings()
         * @generated
         */
        EClass STAT_AND_LOGS_SETTINGS = eINSTANCE.getStatAndLogsSettings();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference STAT_AND_LOGS_SETTINGS__PARAMETERS = eINSTANCE.getStatAndLogsSettings_Parameters();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ImplicitContextSettingsImpl <em>Implicit Context Settings</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ImplicitContextSettingsImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getImplicitContextSettings()
         * @generated
         */
        EClass IMPLICIT_CONTEXT_SETTINGS = eINSTANCE.getImplicitContextSettings();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference IMPLICIT_CONTEXT_SETTINGS__PARAMETERS = eINSTANCE.getImplicitContextSettings_Parameters();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SoaOperationImpl <em>Soa Operation</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SoaOperationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaOperation()
         * @generated
         */
        EClass SOA_OPERATION = eINSTANCE.getSoaOperation();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__ID = eINSTANCE.getSoaOperation_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__LABEL = eINSTANCE.getSoaOperation_Label();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__DESCRIPTION = eINSTANCE.getSoaOperation_Description();

        /**
         * The meta object literal for the '<em><b>Project</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference SOA_OPERATION__PROJECT = eINSTANCE.getSoaOperation_Project();

        /**
         * The meta object literal for the '<em><b>Context</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__CONTEXT = eINSTANCE.getSoaOperation_Context();

        /**
         * The meta object literal for the '<em><b>Job Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__JOB_VERSION = eINSTANCE.getSoaOperation_JobVersion();

        /**
         * The meta object literal for the '<em><b>Job Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__JOB_NAME = eINSTANCE.getSoaOperation_JobName();

        /**
         * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__ACTIVE = eINSTANCE.getSoaOperation_Active();

        /**
         * The meta object literal for the '<em><b>Last Script Generation Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute SOA_OPERATION__LAST_SCRIPT_GENERATION_DATE = eINSTANCE.getSoaOperation_LastScriptGenerationDate();

        /**
         * The meta object literal for the '<em><b>Job Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__JOB_ID = eINSTANCE.getSoaOperation_JobId();

        /**
         * The meta object literal for the '<em><b>Apply Context To Children</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute SOA_OPERATION__APPLY_CONTEXT_TO_CHILDREN = eINSTANCE.getSoaOperation_ApplyContextToChildren();

        /**
         * The meta object literal for the '<em><b>Input Parameters</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SOA_OPERATION__INPUT_PARAMETERS = eINSTANCE.getSoaOperation_InputParameters();

        /**
         * The meta object literal for the '<em><b>Jvm Parameters</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__JVM_PARAMETERS = eINSTANCE.getSoaOperation_JvmParameters();

        /**
         * The meta object literal for the '<em><b>Xms</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__XMS = eINSTANCE.getSoaOperation_Xms();

        /**
         * The meta object literal for the '<em><b>Xmx</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__XMX = eINSTANCE.getSoaOperation_Xmx();

        /**
         * The meta object literal for the '<em><b>Min Job Instances</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__MIN_JOB_INSTANCES = eINSTANCE.getSoaOperation_MinJobInstances();

        /**
         * The meta object literal for the '<em><b>Max Job Instances</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__MAX_JOB_INSTANCES = eINSTANCE.getSoaOperation_MaxJobInstances();

        /**
         * The meta object literal for the '<em><b>Idle TTL for All Instances</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute SOA_OPERATION__IDLE_TTL_FOR_ALL_INSTANCES = eINSTANCE.getSoaOperation_IdleTTL_forAllInstances();

        /**
         * The meta object literal for the '<em><b>Idle TTL for Additional Instances</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute SOA_OPERATION__IDLE_TTL_FOR_ADDITIONAL_INSTANCES = eINSTANCE.getSoaOperation_IdleTTL_forAdditionalInstances();

        /**
         * The meta object literal for the '<em><b>Queue Max Size</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__QUEUE_MAX_SIZE = eINSTANCE.getSoaOperation_QueueMaxSize();

        /**
         * The meta object literal for the '<em><b>Request In Queue TTL</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__REQUEST_IN_QUEUE_TTL = eINSTANCE.getSoaOperation_RequestInQueueTTL();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' container reference feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EReference SOA_OPERATION__SERVICE = eINSTANCE.getSoaOperation_Service();

        /**
         * The meta object literal for the '<em><b>Return Style</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__RETURN_STYLE = eINSTANCE.getSoaOperation_ReturnStyle();

        /**
         * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_OPERATION__BRANCH = eINSTANCE.getSoaOperation_Branch();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SoaInputParameterImpl <em>Soa Input Parameter</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SoaInputParameterImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaInputParameter()
         * @generated
         */
        EClass SOA_INPUT_PARAMETER = eINSTANCE.getSoaInputParameter();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_INPUT_PARAMETER__ID = eINSTANCE.getSoaInputParameter_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_INPUT_PARAMETER__LABEL = eINSTANCE.getSoaInputParameter_Label();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference SOA_INPUT_PARAMETER__OPERATION = eINSTANCE.getSoaInputParameter_Operation();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_INPUT_PARAMETER__DEFAULT_VALUE = eINSTANCE.getSoaInputParameter_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Exposed Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_INPUT_PARAMETER__EXPOSED_NAME = eINSTANCE.getSoaInputParameter_ExposedName();

        /**
         * The meta object literal for the '<em><b>Exposed</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_INPUT_PARAMETER__EXPOSED = eINSTANCE.getSoaInputParameter_Exposed();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SoaServiceImpl <em>Soa Service</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SoaServiceImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSoaService()
         * @generated
         */
        EClass SOA_SERVICE = eINSTANCE.getSoaService();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__ID = eINSTANCE.getSoaService_Id();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__LABEL = eINSTANCE.getSoaService_Label();

        /**
         * The meta object literal for the '<em><b>Name Space</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__NAME_SPACE = eINSTANCE.getSoaService_NameSpace();

        /**
         * The meta object literal for the '<em><b>Contact</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__CONTACT = eINSTANCE.getSoaService_Contact();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__DESCRIPTION = eINSTANCE.getSoaService_Description();

        /**
         * The meta object literal for the '<em><b>Creation</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__CREATION = eINSTANCE.getSoaService_Creation();

        /**
         * The meta object literal for the '<em><b>Modification</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__MODIFICATION = eINSTANCE.getSoaService_Modification();

        /**
         * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__PORT = eINSTANCE.getSoaService_Port();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__TYPE = eINSTANCE.getSoaService_Type();

        /**
         * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__STYLE = eINSTANCE.getSoaService_Style();

        /**
         * The meta object literal for the '<em><b>Used Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__USED_TYPE = eINSTANCE.getSoaService_UsedType();

        /**
         * The meta object literal for the '<em><b>Param Style</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__PARAM_STYLE = eINSTANCE.getSoaService_ParamStyle();

        /**
         * The meta object literal for the '<em><b>Operations</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SOA_SERVICE__OPERATIONS = eINSTANCE.getSoaService_Operations();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute SOA_SERVICE__STATUS = eINSTANCE.getSoaService_Status();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.RulesItemImpl <em>Rules Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.RulesItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRulesItem()
         * @generated
         */
        EClass RULES_ITEM = eINSTANCE.getRulesItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserRightImpl <em>User Right</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserRightImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRight()
         * @generated
         */
        EClass USER_RIGHT = eINSTANCE.getUserRight();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_RIGHT__ID = eINSTANCE.getUserRight_Id();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_RIGHT__NAME = eINSTANCE.getUserRight_Name();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_RIGHT__DESCRIPTION = eINSTANCE.getUserRight_Description();

        /**
         * The meta object literal for the '<em><b>Roles Rights</b></em>' reference list feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EReference USER_RIGHT__ROLES_RIGHTS = eINSTANCE.getUserRight_RolesRights();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.RoleRightImpl <em>Role Right</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.RoleRightImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getRoleRight()
         * @generated
         */
        EClass ROLE_RIGHT = eINSTANCE.getRoleRight();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference ROLE_RIGHT__ROLE = eINSTANCE.getRoleRight_Role();

        /**
         * The meta object literal for the '<em><b>User Right</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference ROLE_RIGHT__USER_RIGHT = eINSTANCE.getRoleRight_UserRight();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.SVGBusinessProcessItemImpl <em>SVG Business Process Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.SVGBusinessProcessItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getSVGBusinessProcessItem()
         * @generated
         */
        EClass SVG_BUSINESS_PROCESS_ITEM = eINSTANCE.getSVGBusinessProcessItem();

        /**
         * The meta object literal for the '<em><b>Business Process Item</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SVG_BUSINESS_PROCESS_ITEM__BUSINESS_PROCESS_ITEM = eINSTANCE.getSVGBusinessProcessItem_BusinessProcessItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.LinkRulesItemImpl <em>Link Rules Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.LinkRulesItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getLinkRulesItem()
         * @generated
         */
        EClass LINK_RULES_ITEM = eINSTANCE.getLinkRulesItem();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_RULES_ITEM__NAME = eINSTANCE.getLinkRulesItem_Name();

        /**
         * The meta object literal for the '<em><b>Link</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference LINK_RULES_ITEM__LINK = eINSTANCE.getLinkRulesItem_Link();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ItemRelationsImpl <em>Item Relations</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ItemRelationsImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemRelations()
         * @generated
         */
        EClass ITEM_RELATIONS = eINSTANCE.getItemRelations();

        /**
         * The meta object literal for the '<em><b>Base Item</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ITEM_RELATIONS__BASE_ITEM = eINSTANCE.getItemRelations_BaseItem();

        /**
         * The meta object literal for the '<em><b>Related Items</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ITEM_RELATIONS__RELATED_ITEMS = eINSTANCE.getItemRelations_RelatedItems();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ItemRelationImpl <em>Item Relation</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ItemRelationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getItemRelation()
         * @generated
         */
        EClass ITEM_RELATION = eINSTANCE.getItemRelation();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_RELATION__ID = eINSTANCE.getItemRelation_Id();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_RELATION__VERSION = eINSTANCE.getItemRelation_Version();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ITEM_RELATION__TYPE = eINSTANCE.getItemRelation_Type();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.NotificationImpl <em>Notification</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.NotificationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getNotification()
         * @generated
         */
        EClass NOTIFICATION = eINSTANCE.getNotification();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute NOTIFICATION__ID = eINSTANCE.getNotification_Id();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute NOTIFICATION__TYPE = eINSTANCE.getNotification_Type();

        /**
         * The meta object literal for the '<em><b>Props</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute NOTIFICATION__PROPS = eINSTANCE.getNotification_Props();

        /**
         * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute NOTIFICATION__ENABLED = eINSTANCE.getNotification_Enabled();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.HL7ConnectionItemImpl <em>HL7 Connection Item</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.HL7ConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getHL7ConnectionItem()
         * @generated
         */
        EClass HL7_CONNECTION_ITEM = eINSTANCE.getHL7ConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserRoleReferenceImpl <em>User Role Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserRoleReferenceImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRoleReference()
         * @generated
         */
        EClass USER_ROLE_REFERENCE = eINSTANCE.getUserRoleReference();

        /**
         * The meta object literal for the '<em><b>User</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USER_ROLE_REFERENCE__USER = eINSTANCE.getUserRoleReference_User();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USER_ROLE_REFERENCE__ROLE = eINSTANCE.getUserRoleReference_Role();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.HeaderFooterConnectionItemImpl <em>Header Footer Connection Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.HeaderFooterConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getHeaderFooterConnectionItem()
         * @generated
         */
        EClass HEADER_FOOTER_CONNECTION_ITEM = eINSTANCE.getHeaderFooterConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.FTPConnectionItemImpl <em>FTP Connection Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.FTPConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFTPConnectionItem()
         * @generated
         */
        EClass FTP_CONNECTION_ITEM = eINSTANCE.getFTPConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.CustomComponentSettingImpl <em>Custom Component Setting</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.CustomComponentSettingImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getCustomComponentSetting()
         * @generated
         */
        EClass CUSTOM_COMPONENT_SETTING = eINSTANCE.getCustomComponentSetting();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CUSTOM_COMPONENT_SETTING__NAME = eINSTANCE.getCustomComponentSetting_Name();

        /**
         * The meta object literal for the '<em><b>Share</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CUSTOM_COMPONENT_SETTING__SHARE = eINSTANCE.getCustomComponentSetting_Share();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.TDQItemImpl <em>TDQ Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.TDQItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTDQItem()
         * @generated
         */
        EClass TDQ_ITEM = eINSTANCE.getTDQItem();

        /**
         * The meta object literal for the '<em><b>Filename</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TDQ_ITEM__FILENAME = eINSTANCE.getTDQItem_Filename();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.BRMSConnectionItemImpl <em>BRMS Connection Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.BRMSConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getBRMSConnectionItem()
         * @generated
         */
        EClass BRMS_CONNECTION_ITEM = eINSTANCE.getBRMSConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.JobScriptItemImpl <em>Job Script Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.JobScriptItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getJobScriptItem()
         * @generated
         */
        EClass JOB_SCRIPT_ITEM = eINSTANCE.getJobScriptItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.EDIFACTConnectionItemImpl <em>EDIFACT Connection Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.EDIFACTConnectionItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getEDIFACTConnectionItem()
         * @generated
         */
        EClass EDIFACT_CONNECTION_ITEM = eINSTANCE.getEDIFACTConnectionItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.TechnicalVariableImpl <em>Technical Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.TechnicalVariableImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTechnicalVariable()
         * @generated
         */
        EClass TECHNICAL_VARIABLE = eINSTANCE.getTechnicalVariable();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TECHNICAL_VARIABLE__KEY = eINSTANCE.getTechnicalVariable_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TECHNICAL_VARIABLE__VALUE = eINSTANCE.getTechnicalVariable_Value();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ReferenceItemImpl <em>Reference Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ReferenceItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getReferenceItem()
         * @generated
         */
        EClass REFERENCE_ITEM = eINSTANCE.getReferenceItem();

        /**
         * The meta object literal for the '<em><b>State</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_ITEM__STATE = eINSTANCE.getReferenceItem_State();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_ITEM__PARENT = eINSTANCE.getReferenceItem_Parent();

        /**
         * The meta object literal for the '<em><b>Reload From File</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_ITEM__RELOAD_FROM_FILE = eINSTANCE.getReferenceItem_ReloadFromFile();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExchangeUserImpl <em>Exchange User</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExchangeUserImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExchangeUser()
         * @generated
         */
        EClass EXCHANGE_USER = eINSTANCE.getExchangeUser();

        /**
         * The meta object literal for the '<em><b>Username</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCHANGE_USER__USERNAME = eINSTANCE.getExchangeUser_Username();

        /**
         * The meta object literal for the '<em><b>Login</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCHANGE_USER__LOGIN = eINSTANCE.getExchangeUser_Login();

        /**
         * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCHANGE_USER__PASSWORD = eINSTANCE.getExchangeUser_Password();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ArtifactNotificationImpl <em>Artifact Notification</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ArtifactNotificationImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getArtifactNotification()
         * @generated
         */
        EClass ARTIFACT_NOTIFICATION = eINSTANCE.getArtifactNotification();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARTIFACT_NOTIFICATION__NAME = eINSTANCE.getArtifactNotification_Name();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.AdditionalInfoMapImpl <em>Additional Info Map</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.AdditionalInfoMapImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getAdditionalInfoMap()
         * @generated
         */
        EClass ADDITIONAL_INFO_MAP = eINSTANCE.getAdditionalInfoMap();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ADDITIONAL_INFO_MAP__KEY = eINSTANCE.getAdditionalInfoMap_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ADDITIONAL_INFO_MAP__VALUE = eINSTANCE.getAdditionalInfoMap_Value();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.MigrationTaskImpl <em>Migration Task</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.MigrationTaskImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMigrationTask()
         * @generated
         */
        EClass MIGRATION_TASK = eINSTANCE.getMigrationTask();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MIGRATION_TASK__ID = eINSTANCE.getMigrationTask_Id();

        /**
         * The meta object literal for the '<em><b>Breaks</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MIGRATION_TASK__BREAKS = eINSTANCE.getMigrationTask_Breaks();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MIGRATION_TASK__VERSION = eINSTANCE.getMigrationTask_Version();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MIGRATION_TASK__STATUS = eINSTANCE.getMigrationTask_Status();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.PigudfItemImpl <em>Pigudf Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.PigudfItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPigudfItem()
         * @generated
         */
        EClass PIGUDF_ITEM = eINSTANCE.getPigudfItem();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ReferenceFileItemImpl <em>Reference File Item</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ReferenceFileItemImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getReferenceFileItem()
         * @generated
         */
        EClass REFERENCE_FILE_ITEM = eINSTANCE.getReferenceFileItem();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_FILE_ITEM__NAME = eINSTANCE.getReferenceFileItem_Name();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_FILE_ITEM__EXTENSION = eINSTANCE.getReferenceFileItem_Extension();

        /**
         * The meta object literal for the '<em><b>Content</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_FILE_ITEM__CONTENT = eINSTANCE.getReferenceFileItem_Content();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionPlanImpl <em>Execution Plan</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionPlanImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlan()
         * @generated
         */
        EClass EXECUTION_PLAN = eINSTANCE.getExecutionPlan();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN__LABEL = eINSTANCE.getExecutionPlan_Label();

        /**
         * The meta object literal for the '<em><b>Exec Plan Parts</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN__EXEC_PLAN_PARTS = eINSTANCE.getExecutionPlan_ExecPlanParts();

        /**
         * The meta object literal for the '<em><b>Exec Plan Prms</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN__EXEC_PLAN_PRMS = eINSTANCE.getExecutionPlan_ExecPlanPrms();

        /**
         * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN__DESCRIPTION = eINSTANCE.getExecutionPlan_Description();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartImpl <em>Execution Plan Part</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionPlanPartImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPart()
         * @generated
         */
        EClass EXECUTION_PLAN_PART = eINSTANCE.getExecutionPlanPart();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__ID = eINSTANCE.getExecutionPlanPart_Id();

        /**
         * The meta object literal for the '<em><b>Task</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART__TASK = eINSTANCE.getExecutionPlanPart_Task();

        /**
         * The meta object literal for the '<em><b>Execution Plan</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART__EXECUTION_PLAN = eINSTANCE.getExecutionPlanPart_ExecutionPlan();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART__PARENT = eINSTANCE.getExecutionPlanPart_Parent();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__TYPE = eINSTANCE.getExecutionPlanPart_Type();

        /**
         * The meta object literal for the '<em><b>Jvm Prms</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART__JVM_PRMS = eINSTANCE.getExecutionPlanPart_JvmPrms();

        /**
         * The meta object literal for the '<em><b>Context Prms</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART__CONTEXT_PRMS = eINSTANCE.getExecutionPlanPart_ContextPrms();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__STATUS = eINSTANCE.getExecutionPlanPart_Status();

        /**
         * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__START_DATE = eINSTANCE.getExecutionPlanPart_StartDate();

        /**
         * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__END_DATE = eINSTANCE.getExecutionPlanPart_EndDate();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__REQUEST_ID = eINSTANCE.getExecutionPlanPart_RequestId();

        /**
         * The meta object literal for the '<em><b>Use Parallel</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__USE_PARALLEL = eINSTANCE.getExecutionPlanPart_UseParallel();

        /**
         * The meta object literal for the '<em><b>Max Threads</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART__MAX_THREADS = eINSTANCE.getExecutionPlanPart_MaxThreads();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPrmImpl <em>Execution Plan Prm</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionPlanPrmImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPrm()
         * @generated
         */
        EClass EXECUTION_PLAN_PRM = eINSTANCE.getExecutionPlanPrm();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PRM__ID = eINSTANCE.getExecutionPlanPrm_Id();

        /**
         * The meta object literal for the '<em><b>Execution Plan</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PRM__EXECUTION_PLAN = eINSTANCE.getExecutionPlanPrm_ExecutionPlan();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PRM__NAME = eINSTANCE.getExecutionPlanPrm_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PRM__VALUE = eINSTANCE.getExecutionPlanPrm_Value();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartCmdPrmImpl <em>Execution Plan Part Cmd Prm</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionPlanPartCmdPrmImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPartCmdPrm()
         * @generated
         */
        EClass EXECUTION_PLAN_PART_CMD_PRM = eINSTANCE.getExecutionPlanPartCmdPrm();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_CMD_PRM__ID = eINSTANCE.getExecutionPlanPartCmdPrm_Id();

        /**
         * The meta object literal for the '<em><b>Execution Plan Part</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART_CMD_PRM__EXECUTION_PLAN_PART = eINSTANCE.getExecutionPlanPartCmdPrm_ExecutionPlanPart();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_CMD_PRM__NAME = eINSTANCE.getExecutionPlanPartCmdPrm_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_CMD_PRM__VALUE = eINSTANCE.getExecutionPlanPartCmdPrm_Value();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.ExecutionPlanPartJobPrmImpl <em>Execution Plan Part Job Prm</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.ExecutionPlanPartJobPrmImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getExecutionPlanPartJobPrm()
         * @generated
         */
        EClass EXECUTION_PLAN_PART_JOB_PRM = eINSTANCE.getExecutionPlanPartJobPrm();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_JOB_PRM__ID = eINSTANCE.getExecutionPlanPartJobPrm_Id();

        /**
         * The meta object literal for the '<em><b>Execution Plan Part</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN_PART_JOB_PRM__EXECUTION_PLAN_PART = eINSTANCE.getExecutionPlanPartJobPrm_ExecutionPlanPart();

        /**
         * The meta object literal for the '<em><b>Override</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_JOB_PRM__OVERRIDE = eINSTANCE.getExecutionPlanPartJobPrm_Override();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_JOB_PRM__NAME = eINSTANCE.getExecutionPlanPartJobPrm_Name();

        /**
         * The meta object literal for the '<em><b>Custom Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_JOB_PRM__CUSTOM_VALUE = eINSTANCE.getExecutionPlanPartJobPrm_CustomValue();

        /**
         * The meta object literal for the '<em><b>Part Custom Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTION_PLAN_PART_JOB_PRM__PART_CUSTOM_VALUE = eINSTANCE.getExecutionPlanPartJobPrm_PartCustomValue();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute LINK_RULES_ITEM__EXTENSION = eINSTANCE.getLinkRulesItem_Extension();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.TaskExecutionHistoryImpl <em>Task Execution History</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.TaskExecutionHistoryImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getTaskExecutionHistory()
         * @generated
         */
        EClass TASK_EXECUTION_HISTORY = eINSTANCE.getTaskExecutionHistory();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__ID = eINSTANCE.getTaskExecutionHistory_Id();

        /**
         * The meta object literal for the '<em><b>Basic Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__BASIC_STATUS = eINSTANCE.getTaskExecutionHistory_BasicStatus();

        /**
         * The meta object literal for the '<em><b>Detailed Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__DETAILED_STATUS = eINSTANCE.getTaskExecutionHistory_DetailedStatus();

        /**
         * The meta object literal for the '<em><b>Task Label</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TASK_LABEL = eINSTANCE.getTaskExecutionHistory_TaskLabel();

        /**
         * The meta object literal for the '<em><b>Task Description</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TASK_DESCRIPTION = eINSTANCE.getTaskExecutionHistory_TaskDescription();

        /**
         * The meta object literal for the '<em><b>Project Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__PROJECT_NAME = eINSTANCE.getTaskExecutionHistory_ProjectName();

        /**
         * The meta object literal for the '<em><b>Talend Job Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TALEND_JOB_NAME = eINSTANCE.getTaskExecutionHistory_TalendJobName();

        /**
         * The meta object literal for the '<em><b>Talend Job Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TALEND_JOB_ID = eINSTANCE.getTaskExecutionHistory_TalendJobId();

        /**
         * The meta object literal for the '<em><b>Talend Job Version</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TALEND_JOB_VERSION = eINSTANCE.getTaskExecutionHistory_TalendJobVersion();

        /**
         * The meta object literal for the '<em><b>Context Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__CONTEXT_NAME = eINSTANCE.getTaskExecutionHistory_ContextName();

        /**
         * The meta object literal for the '<em><b>Context Values</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__CONTEXT_VALUES = eINSTANCE.getTaskExecutionHistory_ContextValues();

        /**
         * The meta object literal for the '<em><b>Jvm Values</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__JVM_VALUES = eINSTANCE.getTaskExecutionHistory_JvmValues();

        /**
         * The meta object literal for the '<em><b>Parent Task Exec Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__PARENT_TASK_EXEC_ID = eINSTANCE.getTaskExecutionHistory_ParentTaskExecId();

        /**
         * The meta object literal for the '<em><b>Parent Plan Exec Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__PARENT_PLAN_EXEC_ID = eINSTANCE.getTaskExecutionHistory_ParentPlanExecId();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.PlanExecutionHistoryImpl <em>Plan Execution History</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.PlanExecutionHistoryImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getPlanExecutionHistory()
         * @generated
         */
        EClass PLAN_EXECUTION_HISTORY = eINSTANCE.getPlanExecutionHistory();

        /**
         * The meta object literal for the '<em><b>Original Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PLAN_EXECUTION_HISTORY__ORIGINAL_LABEL = eINSTANCE.getPlanExecutionHistory_OriginalLabel();

        /**
         * The meta object literal for the '<em><b>Current Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PLAN_EXECUTION_HISTORY__CURRENT_LABEL = eINSTANCE.getPlanExecutionHistory_CurrentLabel();

        /**
         * The meta object literal for the '<em><b>Virtual Server Name</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__VIRTUAL_SERVER_NAME = eINSTANCE.getTaskExecutionHistory_VirtualServerName();

        /**
         * The meta object literal for the '<em><b>Execution Server Name</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXECUTION_SERVER_NAME = eINSTANCE.getTaskExecutionHistory_ExecutionServerName();

        /**
         * The meta object literal for the '<em><b>Execution Server Host</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXECUTION_SERVER_HOST = eINSTANCE.getTaskExecutionHistory_ExecutionServerHost();

        /**
         * The meta object literal for the '<em><b>Execution Server Cmd Port</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXECUTION_SERVER_CMD_PORT = eINSTANCE.getTaskExecutionHistory_ExecutionServerCmdPort();

        /**
         * The meta object literal for the '<em><b>Execution Server File Port</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXECUTION_SERVER_FILE_PORT = eINSTANCE.getTaskExecutionHistory_ExecutionServerFilePort();

        /**
         * The meta object literal for the '<em><b>Execution Server Monitoring Port</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXECUTION_SERVER_MONITORING_PORT = eINSTANCE.getTaskExecutionHistory_ExecutionServerMonitoringPort();

        /**
         * The meta object literal for the '<em><b>Apply Context To Children</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__APPLY_CONTEXT_TO_CHILDREN = eINSTANCE.getTaskExecutionHistory_ApplyContextToChildren();

        /**
         * The meta object literal for the '<em><b>Triggered By</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TRIGGERED_BY = eINSTANCE.getTaskExecutionHistory_TriggeredBy();

        /**
         * The meta object literal for the '<em><b>Trigger Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TRIGGER_TYPE = eINSTANCE.getTaskExecutionHistory_TriggerType();

        /**
         * The meta object literal for the '<em><b>Trigger Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TRIGGER_NAME = eINSTANCE.getTaskExecutionHistory_TriggerName();

        /**
         * The meta object literal for the '<em><b>Trigger Description</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TRIGGER_DESCRIPTION = eINSTANCE.getTaskExecutionHistory_TriggerDescription();

        /**
         * The meta object literal for the '<em><b>Task Error Stack Trace</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TASK_ERROR_STACK_TRACE = eINSTANCE.getTaskExecutionHistory_TaskErrorStackTrace();

        /**
         * The meta object literal for the '<em><b>Id Quartz Job</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__ID_QUARTZ_JOB = eINSTANCE.getTaskExecutionHistory_IdQuartzJob();

        /**
         * The meta object literal for the '<em><b>Id Quartz Trigger</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__ID_QUARTZ_TRIGGER = eINSTANCE.getTaskExecutionHistory_IdQuartzTrigger();

        /**
         * The meta object literal for the '<em><b>Last Job Generation Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__LAST_JOB_GENERATION_DATE = eINSTANCE.getTaskExecutionHistory_LastJobGenerationDate();

        /**
         * The meta object literal for the '<em><b>Job Archive Filename</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__JOB_ARCHIVE_FILENAME = eINSTANCE.getTaskExecutionHistory_JobArchiveFilename();

        /**
         * The meta object literal for the '<em><b>File Trigger File Mask</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_MASK = eINSTANCE.getTaskExecutionHistory_FileTriggerFileMask();

        /**
         * The meta object literal for the '<em><b>File Trigger File Name</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__FILE_TRIGGER_FILE_NAME = eINSTANCE.getTaskExecutionHistory_FileTriggerFileName();

        /**
         * The meta object literal for the '<em><b>File Trigger Folder Path</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__FILE_TRIGGER_FOLDER_PATH = eINSTANCE.getTaskExecutionHistory_FileTriggerFolderPath();

        /**
         * The meta object literal for the '<em><b>File Trigger Triggered File Path</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__FILE_TRIGGER_TRIGGERED_FILE_PATH = eINSTANCE.getTaskExecutionHistory_FileTriggerTriggeredFilePath();

        /**
         * The meta object literal for the '<em><b>Expected Triggering Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__EXPECTED_TRIGGERING_DATE = eINSTANCE.getTaskExecutionHistory_ExpectedTriggeringDate();

        /**
         * The meta object literal for the '<em><b>Task Start Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TASK_START_DATE = eINSTANCE.getTaskExecutionHistory_TaskStartDate();

        /**
         * The meta object literal for the '<em><b>Task End Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__TASK_END_DATE = eINSTANCE.getTaskExecutionHistory_TaskEndDate();

        /**
         * The meta object literal for the '<em><b>Server Job Start Date</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__SERVER_JOB_START_DATE = eINSTANCE.getTaskExecutionHistory_ServerJobStartDate();

        /**
         * The meta object literal for the '<em><b>Server Job End Date</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__SERVER_JOB_END_DATE = eINSTANCE.getTaskExecutionHistory_ServerJobEndDate();

        /**
         * The meta object literal for the '<em><b>Id Remote Job</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__ID_REMOTE_JOB = eINSTANCE.getTaskExecutionHistory_IdRemoteJob();

        /**
         * The meta object literal for the '<em><b>Id Remote Job Execution</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__ID_REMOTE_JOB_EXECUTION = eINSTANCE.getTaskExecutionHistory_IdRemoteJobExecution();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__REQUEST_ID = eINSTANCE.getTaskExecutionHistory_RequestId();

        /**
         * The meta object literal for the '<em><b>Resuming Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TASK_EXECUTION_HISTORY__RESUMING_MODE = eINSTANCE.getTaskExecutionHistory_ResumingMode();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.FolderType <em>Folder Type</em>}' enum.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.FolderType
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getFolderType()
         * @generated
         */
        EEnum FOLDER_TYPE = eINSTANCE.getFolderType();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.UserProjectAuthorizationType <em>User Project Authorization Type</em>}' enum.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.UserProjectAuthorizationType
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserProjectAuthorizationType()
         * @generated
         */
        EEnum USER_PROJECT_AUTHORIZATION_TYPE = eINSTANCE.getUserProjectAuthorizationType();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.UserModuleAuthorizationType <em>User Module Authorization Type</em>}' enum.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.UserModuleAuthorizationType
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserModuleAuthorizationType()
         * @generated
         */
        EEnum USER_MODULE_AUTHORIZATION_TYPE = eINSTANCE.getUserModuleAuthorizationType();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.InformationLevel <em>Information Level</em>}' enum.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.InformationLevel
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getInformationLevel()
         * @generated
         */
        EEnum INFORMATION_LEVEL = eINSTANCE.getInformationLevel();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.MigrationStatus <em>Migration Status</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.core.model.properties.MigrationStatus
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getMigrationStatus()
         * @generated
         */
        EEnum MIGRATION_STATUS = eINSTANCE.getMigrationStatus();

        /**
         * The meta object literal for the '{@link org.talend.core.model.properties.impl.UserRoleImpl <em>User Role</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.talend.core.model.properties.impl.UserRoleImpl
         * @see org.talend.core.model.properties.impl.PropertiesPackageImpl#getUserRole()
         * @generated
         */
        EClass USER_ROLE = eINSTANCE.getUserRole();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_ROLE__ID = eINSTANCE.getUserRole_Id();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_ROLE__NAME = eINSTANCE.getUserRole_Name();

        /**
         * The meta object literal for the '<em><b>Localized Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute USER_ROLE__LOCALIZED_LABEL = eINSTANCE.getUserRole_LocalizedLabel();

        /**
         * The meta object literal for the '<em><b>Fixed</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute USER_ROLE__FIXED = eINSTANCE.getUserRole_Fixed();

        /**
         * The meta object literal for the '<em><b>Roles Rights</b></em>' reference list feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EReference USER_ROLE__ROLES_RIGHTS = eINSTANCE.getUserRole_RolesRights();

    }

} // PropertiesPackage
