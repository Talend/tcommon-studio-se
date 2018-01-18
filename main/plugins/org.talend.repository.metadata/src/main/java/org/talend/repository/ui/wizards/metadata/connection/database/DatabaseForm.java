// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledDirectoryField;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.commons.ui.utils.loader.MyURLClassLoader;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.EDatabaseConnVar;
import org.talend.core.database.conn.template.DbConnStrForHive;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.exception.WarningSQLException;
import org.talend.core.hadoop.EHadoopCategory;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.hadoop.conf.EHadoopProperties;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.hadoop.version.custom.ECustomVersionType;
import org.talend.core.hadoop.version.custom.HadoopCustomVersionDefineDialog;
import org.talend.core.hadoop.version.custom.HadoopVersionControlUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.HotClassLoader;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.prefs.SSLPreferenceConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.runtime.hd.hive.HiveMetadataHelper;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.hive.EHiveExecutionTypes;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.dialog.HadoopPropertiesDialog;
import org.talend.metadata.managment.ui.dialog.HiveJDBCPropertiesDialog;
import org.talend.metadata.managment.ui.dialog.MappingFileSelectDialog;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils.EDBParamName;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.preferences.IDatabasePrefConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.dialog.AProgressMonitorDialogWithCancel;
import org.talend.utils.sql.ConnectionUtils;

/**
 * @author ocarbone
 * 
 */
public class DatabaseForm extends AbstractForm {

    /**
     * Composite.
     */
    private Composite compositeDbSettings;

    /**
     * Main Vars.
     */
    private ConnectionItem connectionItem;

    /**
     * Flags.
     */
    private boolean databaseSettingIsValide = false;

    private boolean readOnly;

    /*********** Fields for Hive Embedded **************/

    private Group hadoopPropGrp;

    private LabelledText nameNodeURLTxt;

    private LabelledText jobTrackerURLTxt;

    private Group metastorePropGrp;

    private LabelledText metastoreConnURLTxt;// javax.jdo.option.ConnectionURL

    private LabelledText metastoreConnDriverJar;

    private Button metastoreJarFilesButton;

    private LabelledCombo metastoreConnDriverName;// javax.jdo.option.ConnectionDriverName

    private LabelledText metastoreConnUserName;// javax.jdo.option.ConnectionUserName

    private LabelledText metastoreConnPassword; // javax.jdo.option.ConnectionPassword

    /***************************************************/

    /**
     * Main Fields.
     */

    private LabelledCombo dbVersionCombo;

    private LabelledCombo hiveModeCombo;

    private LabelledCombo hiveDistributionCombo;

    private LabelledCombo hiveVersionCombo;

    /**
     * Let user select which hive server to use. Including HiveServer1, and HiveServer2 now.
     */
    private LabelledCombo hiveServerVersionCombo;

    private LabelledText serverText;

    private LabelledText portText;

    private LabelledText usernameText;

    private LabelledText passwordText;

    private LabelledText sidOrDatabaseText;

    private LabelledText schemaText;

    private LabelledText datasourceText;

    private Button hiveCustomButton;

    private Button useYarnButton;

    private LabelledText urlConnectionStringText;

    private LabelledFileField fileField;

    private LabelledDirectoryField directoryField;

    private LabelledText additionParamText;

    private LabelledText additionalJDBCSettingsText;

    private Button useSSLEncryption;

    private Composite sslEncryptionDetailComposite;

    private LabelledFileField trustStorePath;

    private LabelledText trustStorePassword;

    private Composite sslClientAuthComposite;

    private Button needClientAuth;

    private Composite sslKeyStoreComposite;

    private LabelledFileField keyStorePath;

    private LabelledText keyStorePassword;

    private Button disableCBCProtection;

    /**
     * Fields for general jdbc
     */

    private LabelledText generalJdbcUrlText = null;

    private LabelledText generalJdbcUserText = null;

    private LabelledText generalJdbcPasswordText = null;

    private LabelledCombo generalJdbcClassNameText = null;

    private LabelledText generalJdbcDriverjarText = null;

    private LabelledText jDBCschemaText;

    private Button browseJarFilesButton = null;

    private Button browseClassButton = null;

    /**
     * Anothers Fields.
     */
    private UtilsButton checkButton;

    private Group hbaseSettingGroup;

    private LabelledCombo hbaseDistributionCombo;

    private LabelledCombo hbaseVersionCombo;

    private Button hbaseCustomButton;

    private Group maprdbSettingGroup;

    private LabelledCombo maprdbDistributionCombo;

    private LabelledCombo maprdbVersionCombo;

    private Button maprdbCustomButton;

    private Group impalaSettingGroup;

    private LabelledCombo impalaDistributionCombo;

    private LabelledCombo impalaVersionCombo;

    private Button impalaCustomButton;

    private Composite typeDbCompositeParent;

    private Composite generalDbCompositeParent;

    private Composite compositeGroupDbSettings;

    private Group hiveComposite;

    private Composite yarnComp;

    private LabelledText generalMappingFileText;

    // changed by hqzhang for TDI 19754
    private Button generalMappingSelectButton;

    // added by hqzhang for TDI 19754 start
    private LabelledText mappingFileText;

    private Button mappingSelectButton;

    // 19754 end
    private final boolean isCreation;

    private boolean first = true;

    private Composite newParent;

    private ScrolledComposite scrolledComposite;

    private ContextType selectedContextType;

    private final String originalUischema;

    private final String originalURL;

    private final Boolean originalIsNeedReload;

    private Composite hadoopLinkComp;

    private LabelledCombo hcPropertyTypeCombo;

    private Text hcRepositoryText;

    private Button hcSelectBtn;

    private List<HashMap<String, Object>> properties;

    private Group authenticationGrp;

    private Group encryptionGrp;

    private Group authenticationGrpForImpala;

    private Group hiveExecutionGrp;

    private LabelledCombo hiveExecutionEngineCombo;

    private Button useKerberos;

    private Button useKerberosForImpala;

    private LabelledText hivePrincipalTxt;

    private Button useMaprTForHive;

    private Composite authenticationMaprTComForHive;

    private Composite authenticationUserPassComForHive;

    private LabelledText maprTUsernameForHiveTxt;

    private LabelledText maprTPasswordForHiveTxt;

    private LabelledText maprTClusterForHiveTxt;

    private LabelledText maprTDurationForHiveTxt;

    private LabelledText impalaPrincipalTxt;

    private LabelledText hbaseMasterPrincipalTxt;

    private LabelledText hbaseRSPrincipalTxt;

    private LabelledText maprdbMasterPrincipalTxt;

    private LabelledText maprdbRSPrincipalTxt;

    private LabelledText metastoreUrlTxt;

    private LabelledText driverJarTxt;

    private Button browseDriverJarBtn;

    private LabelledCombo driverClassTxt;

    private Button browseDriverClassButton;

    private LabelledText usernameTxt;

    private LabelledText passwordTxt;

    private Composite keyTabComponent;

    private LabelledText principalTxt;

    private LabelledFileField keytabTxt;

    private Button useKeyTab;

    private Composite authenticationCom;

    private Composite authenticationComForImpala;

    private Group authenticationGrpForHBase;

    private Composite authenticationComForHBase;

    private Composite keyTabCompoisteForHBase;

    private Button useKerberosForHBase;

    private Button useKeyTabForHBase;

    private LabelledText principalForHBaseTxt;

    private LabelledFileField keytabForHBaseTxt;

    private Button useMaprTForHBase;

    private Composite authenticationMaprTComForHBase;

    private Composite authenticationUserPassComForHBase;

    private LabelledText maprTUsernameForHBaseTxt;

    private LabelledText maprTPasswordForHBaseTxt;

    private LabelledText maprTClusterForHBaseTxt;

    private LabelledText maprTDurationForHBaseTxt;

    //
    private Group authenticationGrpForMaprdb;

    private Composite authenticationComForMaprdb;

    private Composite keyTabCompoisteForMaprdb;

    private Button useKerberosForMaprdb;

    private Button useKeyTabForMaprdb;

    private LabelledText principalForMaprdbTxt;

    private LabelledFileField keytabForMaprdbTxt;

    private Button useMaprTForMaprdb;

    private Composite authenticationMaprTComForMaprdb;

    private Composite authenticationUserPassComForMaprdb;

    private LabelledText maprTUsernameForMaprdbTxt;

    private LabelledText maprTPasswordForMaprdbTxt;

    private LabelledText maprTClusterForMaprdbTxt;

    private LabelledText maprTDurationForMaprdbTxt;

    private SashForm sash;

    private Composite dbConnectionArea;

    private Button moveButton;

    private Composite hadoopPropertiesComposite;

    private HadoopPropertiesDialog hadoopPropertiesDialog;

    private List<Map<String, Object>> hadoopPropertiesList = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> hadoopClusterPropertiesList = new ArrayList<Map<String, Object>>();

    private Composite hiveJDBCPropertiesComposite;

    private HiveJDBCPropertiesDialog hiveJDBCPropertiesDialog;

    private List<Map<String, Object>> hiveJDBCPropertiesList = new ArrayList<Map<String, Object>>();

    private static final String UP = "^"; //$NON-NLS-1$

    private static final String DOWN = "v"; //$NON-NLS-1$

    private static final String ADDIPARASYMBOL = "?"; //$NON-NLS-1$

    private Group znodeparentGrp;

    private Button set_znode_parent;

    private LabelledText znode_parent;

    private Composite tableInfoPartOfMapRDBComp;

    private LabelledText tableNSMappingOfMapRDBTxt;

    private Composite tableInfoPartOfHbaseComp;

    private Button set_table_ns_mapping;

    private LabelledText tableNSMappingOfHbaseTxt;

    /**
     * wheather the db properties group visible
     */
    private boolean isDbPropertiesVisible = true;

    /**
     * Constructor to use by a Wizard to create a new database connection.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param ISelection
     */
    public DatabaseForm(Composite parent, ConnectionItem connectionItem, String[] existingNames, boolean isCreation) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.isCreation = isCreation;
        setConnectionItem(connectionItem); // must be first.
        // For bug TDI-22983, do not show the dialog to choose a context for the first time.
        // Changed by Marvin Wang on Oct. 29, 2012.
        this.metadataconnection = ConvertionHelper.convert(getConnection(), true);

        originalUischema = metadataconnection.getUiSchema() == null ? "" : metadataconnection.getUiSchema();
        originalURL = metadataconnection.getUrl();
        originalIsNeedReload = ConnectionHelper.getIsConnNeedReload(getConnection());

        this.typeName = EDatabaseTypeName.getTypeFromDbType(metadataconnection.getDbType());
        /* use provider for the databse didn't use JDBC,for example: HBase */
        if (typeName != null && typeName.isUseProvider()) {
            this.provider = ExtractMetaDataFromDataBase.getProviderByDbType(metadataconnection.getDbType());
        }
        setupForm(true);

        refreshHidableArea();

        addStringConnectionControls();
        GridLayout layout2 = (GridLayout) getLayout();
        layout2.marginHeight = 0;
        setLayout(layout2);
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    /**
     * refresh the hidable area when hide/visible the DbProperties group
     */
    protected void refreshHidableArea() {
        if (exportContextBtn != null) {
            exportContextBtn.getControl().getParent().getParent().setParent(sash);
        }
        moveButton.setVisible(isDbPropertiesVisible);
        sash.setSashWidth(2);
        // TDQ-14352: fix can not create db connection with TOP.
        if (isTOPStandaloneMode()) {
            sash.setWeights(new int[] { 25 });
        } else {
            sash.setWeights(new int[] { 25, 8 });
        }
        this.layout();
    }

    /**
     * initialize UI (button save & default settings or saved settings).
     */
    @Override
    public void initialize() {
        initializeByConnectionParameters();
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
        if (template != null) {
            if (isGeneralJDBC()) {
                switchBetweenTypeandGeneralDB(false);
                initializeGeneralJDBC();
            } else if (isDBTypeSelected(EDatabaseConnTemplate.HBASE)) {
                initHBaseSettings();
                initTableInfoPartOfHbase();
                initZnodeParent();
            } else if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)) {
                initMaprdbSettings();
                initTableInfoPartOfMapRDB();
                initZnodeParent();
            } else if (isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
                initImpalaSettings();
                initImpalaInfo();
            } else if (isHiveDBConnSelected()) {
                // Changed by Marvin Wang on Oct. 15, 2012 for but TDI-23235.
                doRemoveHiveSetup();
                initHiveInfo();
                doHiveDBTypeSelected();
            } else if (isOracleCustomDBConnSelected()) {
                initOracleCustomEncryptionInfo();
            } else {
                doHiveDBTypeNotSelected();
            }
        }
        // initHiveInfo();
        if (isContextMode()) {
            adaptFormToEditable();
            urlConnectionStringText.setText(getStringConnection());
        } else {
            urlConnectionStringText.setText(getConnection().getURL());

        }
        usernameText.setText(getConnection().getUsername());
        passwordText.setText(getConnection().getRawPassword());
        serverText.setText(getConnection().getServerName());
        portText.setText(getConnection().getPort());
        datasourceText.setText(getConnection().getDatasourceName());
        additionParamText.setText(getConnection().getAdditionalParams());
        sidOrDatabaseText.setText(getConnection().getSID());
        schemaText.setText(getConnection().getUiSchema());
        mappingFileText.setText(getConnection().getDbmsId());
        if (getConnection().getDbVersionString() != null) {
            dbVersionCombo.setText(getConnection().getDbVersionString());
        }

        fileField.setText(getConnection().getFileFieldName());
        directoryField.setText(getConnection().getDBRootPath());
        checkAndSetIniSQLModel();
        checkAS400SpecificCase();
        checkDatabaseProperties();

        if (isDBTypeSelected(EDatabaseConnTemplate.HBASE) || isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)
                || isDBTypeSelected(EDatabaseConnTemplate.HIVE) || isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
            initHadoopClusterSettings();
            if (isDBTypeSelected(EDatabaseConnTemplate.HBASE)) {
                fillDefaultsWhenHBaseVersionChanged();
            } else if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)) {
                fillDefaultsWhenMaprdbVersionChanged();
            } else if (isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
                fillDefaultsWhenImpalaVersionChanged();
            } else {
                fillDefaultsWhenHiveVersionChanged();
                fillDefaultsWhenHiveModeChanged(isHiveEmbeddedMode());
            }
        }

        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    private void initHadoopClusterSettings() {
        if (canLinkToHadoopCluster()) {
            String hcId = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
            if (hcId != null) {
                hcPropertyTypeCombo.select(1);
            } else {
                hcPropertyTypeCombo.select(0);
            }
            updateHCRelatedParts();
        }
    }

    private void initializeByConnectionParameters() {
        DatabaseConnection connection = getConnection();
        EMap<String, String> parameters = connection.getParameters();
        if (connection.getDatabaseType() == null) {
            connection.setDatabaseType(parameters.get(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE));
        }
        String productId = connection.getProductId();
        if (productId == null) {
            connection.setProductId(productId = parameters.get(ConnParameterKeys.CONN_PARA_KEY_DB_PRODUCT));
            String mapping = null;
            if (productId == null || productId.equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
                mapping = generalMappingFileText.getText();
            } else {
                if (MetadataTalendType.getDefaultDbmsFromProduct(productId) != null) {
                    mapping = MetadataTalendType.getDefaultDbmsFromProduct(productId).getId();
                }
            }
            if (mapping == null) {
                mapping = "mysql_id"; // default value //$NON-NLS-1$
            }
            connection.setDbmsId(mapping);
        }
        if (connection.getServerName() == null) {
            connection.setServerName(parameters.get(ConnParameterKeys.CONN_PARA_KEY_DB_SERVER));
        }
        if (connection.getPort() == null) {
            connection.setPort(parameters.get(ConnParameterKeys.CONN_PARA_KEY_DB_PORT));
        }
        if (connection.getUsername() == null) {
            connection.setUsername(parameters.get(ConnParameterKeys.CONN_PARA_KEY_USERNAME));
        }
    }

    @Override
    public void updateSpecialFieldsState() {
        if (canLinkToHadoopCluster()) {
            adaptHadoopLinkedPartToReadOnly();
        }
    }

    /**
     * DOC YeXiaowei Comment method "initializeGeneralJDBC".
     */
    private void initializeGeneralJDBC() {
        generalJdbcUrlText.setText(getConnection().getURL());
        generalJdbcClassNameText.setText(getConnection().getDriverClass());
        generalJdbcUserText.setText(getConnection().getUsername());
        generalJdbcPasswordText.setText(getConnection().getRawPassword());

        String jarPath = null;
        IPath path = null;
        jarPath = getConnection().getDriverJarPath();
        String lastSegments = "";
        if (jarPath != null) {
            final String[] split = jarPath.split(";");
            for (String oneJarPath : split) {
                path = Path.fromOSString(oneJarPath);
                if (path.lastSegment() != null) {
                    lastSegments = lastSegments + path.lastSegment() + ";";
                }
            }
            if (lastSegments.length() > 0) {
                lastSegments = lastSegments.substring(0, lastSegments.length() - 1);
            }
        }
        if (lastSegments.length() > 0) {
            generalJdbcDriverjarText.setText(lastSegments);
        } else {
            generalJdbcDriverjarText.setText(jarPath);
        }
        generalMappingFileText.setText(getConnection().getDbmsId());

        String jdbcUrlString = ""; //$NON-NLS-1$
        if (isContextMode()) {
            if (selectedContextType == null) {
                // use default when open
                selectedContextType = ConnectionContextHelper.getContextTypeForContextMode(getConnection(), null, true);
            }
            if (selectedContextType != null) {
                jdbcUrlString = ConnectionContextHelper.getOriginalValue(selectedContextType, getConnection().getURL());
            }
        } else {
            jdbcUrlString = generalJdbcUrlText.getText();
        }
        if (jdbcUrlString.contains("sqlserver")) {//$NON-NLS-1$
            // incase change from other jdbc to sqlserver jdbc ,uischema is empty
            String schema = getConnection().getUiSchema();
            if (selectedContextType != null && (schema == null || "".equals(schema))) {
                for (ContextParameterType param : (List<ContextParameterType>) selectedContextType.getContextParameter()) {
                    if (param.getName() != null && param.getName().endsWith(ConnectionContextHelper.LINE + EDBParamName.Schema)) {
                        if (getConnection().isContextMode()) {
                            schema = ContextParameterUtils.getNewScriptCode(param.getName(), ECodeLanguage.JAVA);
                        } else {
                            schema = param.getValue();
                        }
                        // ??
                        // getConnection().setUiSchema(schema);
                        break;
                    }
                }

            }
            jDBCschemaText.setText(schema);
        } else {
            jDBCschemaText.setHideWidgets(true);
        }
    }

    private void checkDatabaseProperties() {
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        getConnection().setSqlSynthax(store.getString(IDatabasePrefConstants.SQL_SYNTAX));
        getConnection().setStringQuote(store.getString(IDatabasePrefConstants.STRING_QUOTE));
        getConnection().setNullChar(store.getString(IDatabasePrefConstants.NULL_CHAR));
    }

    private void checkAS400SpecificCase() {
        boolean b = CoreUIPlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.AS400_SQL_SEG);
        getConnection().setStandardSQL(b);
        getConnection().setSystemSQL(!b);
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();

        urlConnectionStringText.setReadOnly(isReadOnly());
        usernameText.setReadOnly(isReadOnly());
        passwordText.setReadOnly(isReadOnly());
        serverText.setReadOnly(isReadOnly());
        portText.setReadOnly(isReadOnly());
        sidOrDatabaseText.setReadOnly(isReadOnly());
        schemaText.setReadOnly(isReadOnly());
        dbVersionCombo.setReadOnly(isReadOnly());
        datasourceText.setReadOnly(isReadOnly());
        additionParamText.setReadOnly(isReadOnly());
        fileField.setReadOnly(isReadOnly());
        mappingFileText.setReadOnly(isReadOnly());
        mappingSelectButton.setEnabled(isReadOnly());

    }

    @Override
    protected void addFields() {
        int width = getSize().x;
        GridLayout layout2;
        Composite parent = new Composite(this, SWT.NONE);
        parent.setLayout(new FillLayout());
        GridData parentGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        parent.setLayoutData(parentGridData);

        sash = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
        sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridLayout layout = new GridLayout();
        sash.setLayout(layout);
        dbConnectionArea = new Composite(sash, SWT.NONE);
        GridLayout dbConnAreaLayout = new GridLayout(1, false);
        dbConnectionArea.setLayout(dbConnAreaLayout);

        // The orginal high is 270.
        // databaseSettingGroup = Form.createGroup(this, 1, Messages.getString("DatabaseForm.groupDatabaseSettings"),
        // 450); //$NON-NLS-1$
        //
        scrolledComposite = new ScrolledComposite(dbConnectionArea, SWT.V_SCROLL | SWT.H_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        newParent = new Composite(scrolledComposite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        newParent.setLayout(gridLayout);
        scrolledComposite.setContent(newParent);

        compositeGroupDbSettings = Form.startNewGridLayout(newParent, 1);
        layout2 = (GridLayout) compositeGroupDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        layout2.marginLeft = 0;
        layout2.marginRight = 0;
        layout2.marginWidth = 0;

        compositeDbSettings = new Composite(compositeGroupDbSettings, SWT.NULL);
        compositeDbSettings.setLayout(new GridLayout(3, false));
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumWidth = width;
        gridData.widthHint = width;
        compositeDbSettings.setLayoutData(gridData);

        layout2 = (GridLayout) compositeDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        // Main Fields

        addFieldsForTypeDB(compositeGroupDbSettings);
        addFieldsForGeneralDB(compositeGroupDbSettings);

        switchBetweenTypeandGeneralDB(true);

        addCheckAndStandardButtons(width, compositeGroupDbSettings);

        scrolledComposite.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                Rectangle r = scrolledComposite.getClientArea();
                // scrolledComposite.setMinSize(newParent.computeSize(r.width-100, 550));
                if (getConnection().getDatabaseType() != null
                        && getConnection().getDatabaseType().equals(EDatabaseConnTemplate.HIVE.getDBDisplayName())) {
                    if (Platform.getOS().equals(Platform.OS_LINUX)) {
                        scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, 900));
                    } else {
                        scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, 820));
                    }

                } else {
                    scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, 550));
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addHelpInfoFields()
     */
    @Override
    protected void addHelpInfoFields() {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (!brandingService.isPoweredbyTalend()) {
            return;
        }

        // TDQ-10179: Remove link in TOS DQ connection wizard
        if (isTOPStandaloneMode()) {
            return;
        }

        Composite helpComposite = new Composite(this, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumHeight = 45;
        helpComposite.setLayoutData(gridData);
        GC gc = new GC(helpComposite);
        String linkUrl = Messages.getString("DatabaseForm.helpInfo.installDriverLink.url"); //$NON-NLS-1$
        String linkLabel = Messages.getString("DatabaseForm.helpInfo.installDriverLink.label"); //$NON-NLS-1$
        String linkText = "<a href=\"" + linkUrl + "\">" + linkLabel + "</a>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Point linkSize = gc.stringExtent(linkLabel);
        Link link = new Link(helpComposite, SWT.NONE);
        link.setText(linkText);
        link.setSize(linkSize.x + 15, 30);
        gc.dispose();
        link.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    // Open the url with default external browser
                    PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                }
            }
        });
    }

    /**
     * DOC YeXiaowei Comment method "addFieldForTypeDB".
     * 
     * @param width
     * @param compositeGroupDbSettings
     */
    private void addFieldsForTypeDB(Composite compositeGroupDbSettings) {
        typeDbCompositeParent = new Composite(compositeGroupDbSettings, SWT.NULL);
        typeDbCompositeParent.setLayout(new GridLayout(3, false));
        GridLayout layout2 = (GridLayout) typeDbCompositeParent.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        createHBaseSettingContents(typeDbCompositeParent);

        createMaprdbSettingContents(typeDbCompositeParent);

        createImpalaSettingContents(typeDbCompositeParent);

        createHadoopVersionInfoForHiveEmbedded(typeDbCompositeParent);

        dbVersionCombo = new LabelledCombo(typeDbCompositeParent, Messages.getString("DatabaseForm.dbversion"), Messages //$NON-NLS-1$
                .getString("DatabaseForm.dbversion.tip"), new String[0], 2, true); //$NON-NLS-1$

        createHiveServerVersionField(typeDbCompositeParent);

        setHideVersionInfoWidgets(true);

        // Field connectionString
        urlConnectionStringText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.stringConnection"), 2); //$NON-NLS-1$
        urlConnectionStringText.setEditable(false);

        // Field login & password
        usernameText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.login"), 2); //$NON-NLS-1$
        passwordText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.password"), 2, SWT.BORDER //$NON-NLS-1$
                | SWT.SINGLE | SWT.PASSWORD);

        // Another fields
        serverText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.server"), 2); //$NON-NLS-1$
        portText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.port"), 2); //$NON-NLS-1$
        // portText.setTextLimit(5);
        sidOrDatabaseText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.DataBase"), 2); //$NON-NLS-1$
        schemaText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.schema"), 2); //$NON-NLS-1$
        if (getConnection() != null
                && EDatabaseConnTemplate.INFORMIX.getDBDisplayName().equals(getConnection().getDatabaseType())) {
            datasourceText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.informixInstance"), 2); //$NON-NLS-1$
        } else {
            datasourceText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.dataSource"), 2); //$NON-NLS-1$
        }
        additionParamText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.AddParams"), 2); //$NON-NLS-1$
        additionalJDBCSettingsText = new LabelledText(typeDbCompositeParent,
                Messages.getString("DatabaseForm.hive.additionalJDBCSettings"), 2); //$NON-NLS-1$

        String[] extensions = { "*.*" }; //$NON-NLS-1$
        fileField = new LabelledFileField(typeDbCompositeParent, Messages.getString("DatabaseForm.mdbFile"), extensions); //$NON-NLS-1$
        directoryField = new LabelledDirectoryField(typeDbCompositeParent, "DB Root Path"); //$NON-NLS-1$

        mappingFileText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.general.mapping"), 1); //$NON-NLS-1$

        mappingSelectButton = new Button(typeDbCompositeParent, SWT.NONE);
        mappingSelectButton.setText("..."); //$NON-NLS-1$
        mappingSelectButton.setToolTipText(Messages.getString("DatabaseForm.selectRule")); //$NON-NLS-1$

        createHadoopUIContentsForHiveEmbedded(typeDbCompositeParent);
        createMetastoreUIContentsForHiveEmbedded(typeDbCompositeParent);
        createEncryptionGroup(typeDbCompositeParent);
        createTableInfoPartForHbase(typeDbCompositeParent);
        createTableInfoPartForMaprdb(typeDbCompositeParent);
        createZnodeParent(typeDbCompositeParent);
        createAuthenticationForHive(typeDbCompositeParent);
        createAuthenticationForImpala(typeDbCompositeParent);
        createAuthenticationForHBase(typeDbCompositeParent);
        createAuthenticationForMaprdb(typeDbCompositeParent);
        createExecutionFieldsForHive(typeDbCompositeParent);
        createHadoopPropertiesFields(typeDbCompositeParent);
        createHivePropertiesFields(typeDbCompositeParent);
    }

    private void createZnodeParent(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        znodeparentGrp = new Group(parent, SWT.NONE);
        znodeparentGrp.setText(Messages.getString("DatabaseForm.ZnodeParent.group")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(znodeparentGrp);
        GridLayout znodeparentLayout = new GridLayout(4, false);
        znodeparentLayout.marginHeight = 0;
        znodeparentGrp.setLayout(znodeparentLayout);

        set_znode_parent = new Button(znodeparentGrp, SWT.CHECK);
        set_znode_parent.setText(Messages.getString("DatabaseForm.ZnodeParent.checkBtn")); //$NON-NLS-1$
        znode_parent = new LabelledText(znodeparentGrp, "", 1); //$NON-NLS-1$
        addListenerForZnodeParent();
        initZnodeParent();
    }

    private void createTableInfoPartForMaprdb(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        tableInfoPartOfMapRDBComp = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(tableInfoPartOfMapRDBComp);
        GridLayout tableInfoPartLayout = new GridLayout(3, false);
        tableInfoPartLayout.marginHeight = 0;
        tableInfoPartOfMapRDBComp.setLayout(tableInfoPartLayout);
        tableNSMappingOfMapRDBTxt = new LabelledText(tableInfoPartOfMapRDBComp,
                Messages.getString("DatabaseForm.maprdb.tableInfo.tableNSMapping.label"), 2); //$NON-NLS-1$
        addListenerForTableInfoPartOfMapRDB();
        initTableInfoPartOfMapRDB();
    }

    private void addListenerForTableInfoPartOfMapRDB() {
        tableNSMappingOfMapRDBTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING,
                            tableNSMappingOfMapRDBTxt.getText());
                }
            }
        });
    }

    private void initTableInfoPartOfMapRDB() {
        DatabaseConnection connection = getConnection();
        boolean isShow = connection != null
                && EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(getConnection().getDatabaseType());
        hideControl(tableInfoPartOfMapRDBComp, !isShow);
        if (connection != null) {
            String tableNSMapping = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            tableNSMappingOfMapRDBTxt.setText(StringUtils.trimToEmpty(tableNSMapping));
        }
    }

    private void createTableInfoPartForHbase(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        tableInfoPartOfHbaseComp = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(tableInfoPartOfHbaseComp);
        GridLayout tableInfoPartLayout = new GridLayout(4, false);
        tableInfoPartLayout.marginHeight = 0;
        tableInfoPartOfHbaseComp.setLayout(tableInfoPartLayout);
        set_table_ns_mapping = new Button(tableInfoPartOfHbaseComp, SWT.CHECK);
        set_table_ns_mapping.setText(Messages.getString("DatabaseForm.set_table_ns_mapping.checkBtn")); //$NON-NLS-1$
        tableNSMappingOfHbaseTxt = new LabelledText(tableInfoPartOfHbaseComp, "", 2); //$NON-NLS-1$
        addListenerForTableInfoPartOfHbase();
        initTableInfoPartOfHbase();
    }

    private String getHBASEDefaultValue(String paraName) {
        if (paraName == null) {
            return null;
        }
        String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
        if (distribution == null) {
            return null;
        }
        String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
        if (version == null) {
            return null;
        }
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hadoopDistributionService == null) {
            return null;
        }
        IHDistribution hbaseDistribution = hadoopDistributionService.getHBaseDistributionManager().getDistribution(distribution,
                false);
        if (hbaseDistribution == null) {
            return null;
        }
        IHDistributionVersion hbaseVersion = hbaseDistribution.getHDVersion(version, false);
        if (hbaseVersion == null) {
            return null;
        }
        String defaultValue = null;
        if (paraName.equals(EHadoopProperties.MAPRTICKET_CLUSTER.getName())
                || paraName.equals(EHadoopProperties.MAPRTICKET_DURATION.getName())) {
            defaultValue = hbaseVersion.getDefaultConfig(distribution, paraName);
        } else {
            defaultValue = hbaseVersion.getDefaultConfig(distribution, EHadoopCategory.HBASE.getName(), paraName);
        }
        return defaultValue;
    }

    private String getMAPRDefaultValue(String paraName) {
        if (paraName == null) {
            return null;
        }
        String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
        if (distribution == null) {
            return null;
        }
        String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION);
        if (version == null) {
            return null;
        }
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hadoopDistributionService == null) {
            return null;
        }
        IHDistribution maprdbDistribution = hadoopDistributionService.getMaprdbDistributionManager().getDistribution(
                distribution, false);
        if (maprdbDistribution == null) {
            return null;
        }
        IHDistributionVersion maprdbVersion = maprdbDistribution.getHDVersion(version, false);
        if (maprdbVersion == null) {
            return null;
        }
        String defaultPort = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(),
                EHadoopProperties.PORT.getName());
        if (defaultPort != null && !isContextMode()) {
            getConnection().setPort(defaultPort);
            portText.setText(defaultPort);
        }
        String defaultValue = null;
        if (paraName.equals(EHadoopProperties.MAPRTICKET_CLUSTER.getName())
                || paraName.equals(EHadoopProperties.MAPRTICKET_DURATION.getName())) {
            defaultValue = maprdbVersion.getDefaultConfig(distribution, paraName);
        } else {
            defaultValue = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(), paraName);
        }
        return defaultValue;
    }

    private void addListenerForTableInfoPartOfHbase() {
        set_table_ns_mapping.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (set_table_ns_mapping.getSelection()) {
                    tableNSMappingOfHbaseTxt.setVisible(true);
                    tableInfoPartOfHbaseComp.layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_TABLE_NS_MAPPING,
                            Boolean.TRUE.toString());

                    String tableNSMapping = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
                    if (tableNSMapping != null && tableNSMapping.length() > 0) {
                        tableNSMappingOfHbaseTxt.setText(tableNSMapping);
                    } else {
                        String defaultTableNSMapping = getHBASEDefaultValue(EHadoopProperties.HBASE_TABLE_NS_MAPPING.getName());
                        if (defaultTableNSMapping != null) {
                            tableNSMappingOfHbaseTxt.setText(defaultTableNSMapping);
                        }
                    }
                } else {
                    tableNSMappingOfHbaseTxt.setVisible(false);
                    tableInfoPartOfHbaseComp.layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_TABLE_NS_MAPPING,
                            Boolean.FALSE.toString());
                }
            }

        });
        tableNSMappingOfHbaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING,
                            tableNSMappingOfHbaseTxt.getText());
                }
            }
        });
    }

    private void initTableInfoPartOfHbase() {
        DatabaseConnection connection = getConnection();
        boolean isShow = connection != null
                && EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(getConnection().getDatabaseType())
                && doSupportMaprTicketForHbase();
        hideControl(tableInfoPartOfHbaseComp, !isShow);
        if (connection != null) {
            String setTableNSMappingStr = connection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_TABLE_NS_MAPPING);
            String tableNSMapping = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            boolean check = Boolean.valueOf(setTableNSMappingStr);
            set_table_ns_mapping.setSelection(check);
            if (check) {
                tableNSMappingOfHbaseTxt.setText(StringUtils.trimToEmpty(tableNSMapping));
            }
            tableNSMappingOfHbaseTxt.setVisible(set_table_ns_mapping.getSelection());
        }
    }

    private void createAuthenticationForImpala(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        authenticationGrpForImpala = new Group(parent, SWT.NONE);
        authenticationGrpForImpala.setText(Messages.getString("DatabaseForm.hiveEmbedded.authentication")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(authenticationGrpForImpala);

        GridLayout authLayout = new GridLayout(4, false);
        authLayout.marginHeight = 0;
        authenticationGrpForImpala.setLayout(authLayout);

        useKerberosForImpala = new Button(authenticationGrpForImpala, SWT.CHECK);
        useKerberosForImpala.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKerberos")); //$NON-NLS-1$
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKerberosForImpala.setLayoutData(data);

        authenticationComForImpala = new Composite(authenticationGrpForImpala, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationComForImpala.setLayoutData(data);
        authenticationComForImpala.setLayout(new GridLayout(3, false));

        impalaPrincipalTxt = new LabelledText(authenticationComForImpala, Messages.getString("DatabaseForm.impalaPrincipal"), 2); //$NON-NLS-1$

        addListenerForImpalaAuthentication();
        initForImpalaAuthentication();
    }

    private void createEncryptionGroup(Composite parent) {
        encryptionGrp = new Group(parent, SWT.NONE);
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        encryptionGrp.setText(Messages.getString("DatabaseForm.encryption")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(encryptionGrp);
        encryptionGrp.setLayout(new GridLayout(1, true));

        useSSLEncryption = new Button(encryptionGrp, SWT.CHECK);
        useSSLEncryption.setText(Messages.getString("DatabaseForm.encryption.useSSLEncryption")); //$NON-NLS-1$
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER);
        gridData.horizontalSpan = 1;
        useSSLEncryption.setLayoutData(gridData);

        sslEncryptionDetailComposite = new Composite(encryptionGrp, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 1;
        sslEncryptionDetailComposite.setLayoutData(gridData);
        GridLayout sslEncryptionDetailLayout = new GridLayout(1, true);
        sslEncryptionDetailComposite.setLayout(sslEncryptionDetailLayout);

        Composite sslTrustStoreComposite = new Composite(sslEncryptionDetailComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 1;
        sslTrustStoreComposite.setLayoutData(gridData);
        GridLayout sslTrustStoreLayout = new GridLayout(2, true);
        sslTrustStoreComposite.setLayout(sslTrustStoreLayout);

        Composite leftHalfPart = new Composite(sslTrustStoreComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
        leftHalfPart.setLayoutData(gridData);
        leftHalfPart.setLayout(new GridLayout(3, false));
        trustStorePath = new LabelledFileField(leftHalfPart,
                Messages.getString("DatabaseForm.encryption.useSSLEncryption.trustStorePath"), null, 1); //$NON-NLS-1$

        Composite rightHalfPart = new Composite(sslTrustStoreComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
        rightHalfPart.setLayoutData(gridData);
        rightHalfPart.setLayout(new GridLayout(2, false));
        trustStorePassword = new LabelledText(rightHalfPart,
                Messages.getString("DatabaseForm.encryption.useSSLEncryption.trustStorePassword"), 1, //$NON-NLS-1$
                SWT.PASSWORD | SWT.SINGLE | SWT.BORDER);

        sslClientAuthComposite = new Composite(sslEncryptionDetailComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 1;
        sslClientAuthComposite.setLayoutData(gridData);
        GridLayout sslClientAuthLayout = new GridLayout(1, true);
        sslClientAuthComposite.setLayout(sslClientAuthLayout);

        needClientAuth = new Button(sslClientAuthComposite, SWT.CHECK);
        needClientAuth.setText(Messages.getString("DatabaseForm.encryption.useSSLEncryption.needClientAuth")); //$NON-NLS-1$
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER);
        gridData.horizontalSpan = 1;
        needClientAuth.setLayoutData(gridData);

        sslKeyStoreComposite = new Composite(sslClientAuthComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 1;
        sslKeyStoreComposite.setLayoutData(gridData);
        GridLayout sslKeyStoreLayout = new GridLayout(2, true);
        sslKeyStoreComposite.setLayout(sslKeyStoreLayout);

        leftHalfPart = new Composite(sslKeyStoreComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
        leftHalfPart.setLayoutData(gridData);
        leftHalfPart.setLayout(new GridLayout(3, false));
        keyStorePath = new LabelledFileField(leftHalfPart,
                Messages.getString("DatabaseForm.encryption.useSSLEncryption.keyStorePath"), null, 1); //$NON-NLS-1$

        rightHalfPart = new Composite(sslKeyStoreComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
        rightHalfPart.setLayoutData(gridData);
        rightHalfPart.setLayout(new GridLayout(2, false));
        keyStorePassword = new LabelledText(rightHalfPart,
                Messages.getString("DatabaseForm.encryption.useSSLEncryption.keyStorePassword"), 1, //$NON-NLS-1$
                SWT.PASSWORD | SWT.SINGLE | SWT.BORDER);

        disableCBCProtection = new Button(sslClientAuthComposite, SWT.CHECK);
        disableCBCProtection.setText(Messages.getString("DatabaseForm.encryption.useSSLEncryption.disableCBCProtection")); //$NON-NLS-1$
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER);
        gridData.horizontalSpan = 1;
        disableCBCProtection.setLayoutData(gridData);
        addListenersForEncryptionGroup();
    }

    private void updateSSLEncryptionDetailsDisplayStatus() {
        boolean isSupportSSLKeyStore = isSupportSSLKeyStore();
        GridData hadoopData = (GridData) sslKeyStoreComposite.getLayoutData();
        hadoopData.exclude = !isSupportSSLKeyStore;
        sslKeyStoreComposite.setVisible(isSupportSSLKeyStore);

        boolean isSupportSSLClientAuth = isSupportSSLClientAuth();
        hadoopData = (GridData) sslClientAuthComposite.getLayoutData();
        hadoopData.exclude = !isSupportSSLClientAuth;
        sslClientAuthComposite.setVisible(isSupportSSLClientAuth);

        boolean isSupportSSLTrustStore = isSupportSSLTrustStore();
        hadoopData = (GridData) sslEncryptionDetailComposite.getLayoutData();
        hadoopData.exclude = !isSupportSSLTrustStore;
        sslEncryptionDetailComposite.setVisible(isSupportSSLTrustStore);
        sslEncryptionDetailComposite.setLayoutData(hadoopData);
        sslEncryptionDetailComposite.getParent().getParent().layout();

        setSSLTrustStoreParameters(!isSupportSSLTrustStore);
        setSSLKeyStoreParameters(!isSupportSSLKeyStore);
        String url = getStringConnection();
        urlConnectionStringText.setText(url);
        getConnection().setURL(url);
    }

    private void setSSLTrustStoreParameters(boolean shouldRemove) {
        if (shouldRemove) {
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
        } else {
            updateTrustStorePathParameter();
            updateTrustStorePasswordParameter();
        }
    }

    private void setSSLKeyStoreParameters(boolean shouldRemove) {
        if (shouldRemove) {
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
        } else {
            updateKeyStorePathParameter();
            updateKeyStorePasswordParameter();
        }
    }

    private void createAuthenticationForHive(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        authenticationGrp = new Group(parent, SWT.NONE);
        authenticationGrp.setText(Messages.getString("DatabaseForm.hiveEmbedded.authentication")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(authenticationGrp);

        GridLayout authLayout = new GridLayout(4, false);
        authLayout.marginHeight = 0;
        authenticationGrp.setLayout(authLayout);

        useKerberos = new Button(authenticationGrp, SWT.CHECK);
        useKerberos.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKerberos")); //$NON-NLS-1$
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKerberos.setLayoutData(data);

        authenticationCom = new Composite(authenticationGrp, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationCom.setLayoutData(data);
        authenticationCom.setLayout(new GridLayout(3, false));

        hivePrincipalTxt = new LabelledText(authenticationCom, Messages.getString("DatabaseForm.hiveEmbedded.hivePrincipal"), 2); //$NON-NLS-1$
        metastoreUrlTxt = new LabelledText(authenticationCom, Messages.getString("DatabaseForm.hiveEmbedded.metastoreUrlTxt"), 2); //$NON-NLS-1$
        driverJarTxt = new LabelledText(authenticationCom, Messages.getString("DatabaseForm.general.jarfile"), //$NON-NLS-1$
                1);
        browseDriverJarBtn = new Button(authenticationCom, SWT.NONE);
        browseDriverJarBtn.setText("..."); //$NON-NLS-1$
        browseDriverJarBtn.setToolTipText(Messages.getString("DatabaseForm.selectJar")); //$NON-NLS-1$
        driverClassTxt = new LabelledCombo(authenticationCom, Messages.getString("DatabaseForm.hiveEmbedded.driverClass"), "", //$NON-NLS-1$ //$NON-NLS-2$
                null, 1, true, SWT.NONE);
        browseDriverClassButton = new Button(authenticationCom, SWT.NONE);
        browseDriverClassButton.setText("..."); //$NON-NLS-1$
        browseDriverClassButton.setToolTipText(Messages.getString("DatabaseForm.selectDriverClass")); //$NON-NLS-1$
        usernameTxt = new LabelledText(authenticationCom, Messages.getString("DatabaseForm.hiveEmbedded.username"), 2); //$NON-NLS-1$
        passwordTxt = new LabelledText(authenticationCom, Messages.getString("DatabaseForm.hiveEmbedded.password"), 2, //$NON-NLS-1$
                SWT.PASSWORD);

        useKeyTab = new Button(authenticationCom, SWT.CHECK);
        useKeyTab.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKeyTab")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKeyTab.setLayoutData(data);

        keyTabComponent = new Composite(authenticationCom, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        data.exclude = true;
        keyTabComponent.setLayoutData(data);
        keyTabComponent.setVisible(false);
        keyTabComponent.setLayout(new GridLayout(5, false));

        principalTxt = new LabelledText(keyTabComponent, Messages.getString("DatabaseForm.hiveEmbedded.principal"), 1); //$NON-NLS-1$
        String[] extensions = { "*.*" }; //$NON-NLS-1$
        keytabTxt = new LabelledFileField(keyTabComponent, Messages.getString("DatabaseForm.hiveEmbedded.keytab"), extensions); //$NON-NLS-1$

        // Mapr ticket
        useMaprTForHive = new Button(authenticationGrp, SWT.CHECK);
        useMaprTForHive.setText(Messages.getString("DatabaseForm.hive.useMaprTicket")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useMaprTForHive.setLayoutData(data);

        authenticationMaprTComForHive = new Composite(authenticationGrp, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationMaprTComForHive.setLayoutData(data);
        authenticationMaprTComForHive.setLayout(new GridLayout(3, false));

        authenticationUserPassComForHive = new Composite(authenticationMaprTComForHive, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationUserPassComForHive.setLayoutData(data);
        authenticationUserPassComForHive.setLayout(new GridLayout(3, false));

        maprTUsernameForHiveTxt = new LabelledText(authenticationUserPassComForHive,
                Messages.getString("DatabaseForm.hive.MaprTUsernameTxt.label"), 2); //$NON-NLS-1$
        maprTPasswordForHiveTxt = new LabelledText(authenticationUserPassComForHive,
                Messages.getString("DatabaseForm.hive.MaprTPasswordTxt.label"), 2, SWT.PASSWORD | SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$
        maprTPasswordForHiveTxt.getTextControl().setEchoChar('*');

        maprTClusterForHiveTxt = new LabelledText(authenticationMaprTComForHive,
                Messages.getString("DatabaseForm.hive.MaprTClusterTxt.label"), 2); //$NON-NLS-1$
        maprTDurationForHiveTxt = new LabelledText(authenticationMaprTComForHive,
                Messages.getString("DatabaseForm.hive.MaprTDurationTxt.label"), 2); //$NON-NLS-1$

        addListenerForAuthentication();
        initForAuthentication();
    }

    private void createAuthenticationForHBase(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        authenticationGrpForHBase = new Group(parent, SWT.NONE);
        authenticationGrpForHBase.setText(Messages.getString("DatabaseForm.hiveEmbedded.authentication")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(authenticationGrpForHBase);

        GridLayout authLayout = new GridLayout(4, false);
        authLayout.marginHeight = 0;
        authenticationGrpForHBase.setLayout(authLayout);

        useKerberosForHBase = new Button(authenticationGrpForHBase, SWT.CHECK);
        useKerberosForHBase.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKerberos")); //$NON-NLS-1$
        // TUP-17659 disable Kerberos Authentication for EMR-Hbase
        checkHBaseKerberos();
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKerberosForHBase.setLayoutData(data);

        authenticationComForHBase = new Composite(authenticationGrpForHBase, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationComForHBase.setLayoutData(data);
        authenticationComForHBase.setLayout(new GridLayout(3, false));

        hbaseMasterPrincipalTxt = new LabelledText(authenticationComForHBase,
                Messages.getString("DatabaseForm.hbaseMasterPrincipalTxt.label"), 2); //$NON-NLS-1$
        hbaseRSPrincipalTxt = new LabelledText(authenticationComForHBase,
                Messages.getString("DatabaseForm.hbaseRSPrincipalTxt.label"), 2); //$NON-NLS-1$

        useKeyTabForHBase = new Button(authenticationComForHBase, SWT.CHECK);
        useKeyTabForHBase.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKeyTab")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKeyTabForHBase.setLayoutData(data);

        keyTabCompoisteForHBase = new Composite(authenticationComForHBase, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        data.exclude = true;
        keyTabCompoisteForHBase.setLayoutData(data);
        keyTabCompoisteForHBase.setVisible(false);
        keyTabCompoisteForHBase.setLayout(new GridLayout(5, false));

        principalForHBaseTxt = new LabelledText(keyTabCompoisteForHBase,
                Messages.getString("DatabaseForm.hiveEmbedded.principal"), 1); //$NON-NLS-1$
        String[] extensions = { "*.*" }; //$NON-NLS-1$
        keytabForHBaseTxt = new LabelledFileField(keyTabCompoisteForHBase,
                Messages.getString("DatabaseForm.hiveEmbedded.keytab"), //$NON-NLS-1$
                extensions);

        // Mapr ticket
        useMaprTForHBase = new Button(authenticationGrpForHBase, SWT.CHECK);
        useMaprTForHBase.setText(Messages.getString("DatabaseForm.hbase.useMaprTicket")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useMaprTForHBase.setLayoutData(data);

        authenticationMaprTComForHBase = new Composite(authenticationGrpForHBase, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationMaprTComForHBase.setLayoutData(data);
        authenticationMaprTComForHBase.setLayout(new GridLayout(3, false));

        authenticationUserPassComForHBase = new Composite(authenticationMaprTComForHBase, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationUserPassComForHBase.setLayoutData(data);
        authenticationUserPassComForHBase.setLayout(new GridLayout(3, false));

        maprTUsernameForHBaseTxt = new LabelledText(authenticationUserPassComForHBase,
                Messages.getString("DatabaseForm.hbaseMaprTUsernameTxt.label"), 2); //$NON-NLS-1$
        maprTPasswordForHBaseTxt = new LabelledText(authenticationUserPassComForHBase,
                Messages.getString("DatabaseForm.hbaseMaprTPasswordTxt.label"), 2, SWT.PASSWORD | SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$
        maprTPasswordForHBaseTxt.getTextControl().setEchoChar('*');

        maprTClusterForHBaseTxt = new LabelledText(authenticationMaprTComForHBase,
                Messages.getString("DatabaseForm.hbaseMaprTClusterTxt.label"), 2); //$NON-NLS-1$
        maprTDurationForHBaseTxt = new LabelledText(authenticationMaprTComForHBase,
                Messages.getString("DatabaseForm.hbaseMaprTDurationTxt.label"), 2); //$NON-NLS-1$

        addListenerHBaseAuthentication();
        initForHBaseAuthentication();
    }

    private void checkHBaseKerberos() {

        useKerberosForHBase.setEnabled(hbaseDoSupportKerb());

    }

    private boolean hbaseDoSupportKerb() {
        String hadoopDistribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
        String hadoopVersion = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
        IHDistribution hBaseDistribution = getHBaseDistribution(hadoopDistribution, false);
        if (hBaseDistribution != null) {
            IHDistributionVersion hdVersion = hBaseDistribution.getHDVersion(hadoopVersion, false);
            IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
            if (hdVersion != null && hadoopDistributionService != null) {
                try {
                    return hadoopDistributionService.doSupportMethod(hdVersion, "doSupportKerberos");
                } catch (Exception e) {
                    // ignore if NoSuchMethodException
                }
            }
        }
        return false;
    }

    private void createAuthenticationForMaprdb(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        authenticationGrpForMaprdb = new Group(parent, SWT.NONE);
        authenticationGrpForMaprdb.setText(Messages.getString("DatabaseForm.hiveEmbedded.authentication")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(authenticationGrpForMaprdb);

        GridLayout authLayout = new GridLayout(4, false);
        authLayout.marginHeight = 0;
        authenticationGrpForMaprdb.setLayout(authLayout);

        useKerberosForMaprdb = new Button(authenticationGrpForMaprdb, SWT.CHECK);
        useKerberosForMaprdb.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKerberos")); //$NON-NLS-1$
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKerberosForMaprdb.setLayoutData(data);

        authenticationComForMaprdb = new Composite(authenticationGrpForMaprdb, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationComForMaprdb.setLayoutData(data);
        authenticationComForMaprdb.setLayout(new GridLayout(3, false));

        maprdbMasterPrincipalTxt = new LabelledText(authenticationComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.MasterPrincipalTxt.label"), 2); //$NON-NLS-1$
        maprdbRSPrincipalTxt = new LabelledText(authenticationComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.RSPrincipalTxt.label"), 2); //$NON-NLS-1$

        useKeyTabForMaprdb = new Button(authenticationComForMaprdb, SWT.CHECK);
        useKeyTabForMaprdb.setText(Messages.getString("DatabaseForm.hiveEmbedded.useKeyTab")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useKeyTabForMaprdb.setLayoutData(data);

        keyTabCompoisteForMaprdb = new Composite(authenticationComForMaprdb, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        data.exclude = true;
        keyTabCompoisteForMaprdb.setLayoutData(data);
        keyTabCompoisteForMaprdb.setVisible(false);
        keyTabCompoisteForMaprdb.setLayout(new GridLayout(5, false));

        principalForMaprdbTxt = new LabelledText(keyTabCompoisteForMaprdb,
                Messages.getString("DatabaseForm.hiveEmbedded.principal"), 1); //$NON-NLS-1$
        String[] extensions = { "*.*" }; //$NON-NLS-1$
        keytabForMaprdbTxt = new LabelledFileField(keyTabCompoisteForMaprdb,
                Messages.getString("DatabaseForm.hiveEmbedded.keytab"), //$NON-NLS-1$
                extensions);

        // Mapr ticket
        useMaprTForMaprdb = new Button(authenticationGrpForMaprdb, SWT.CHECK);
        useMaprTForMaprdb.setText(Messages.getString("DatabaseForm.hbase.useMaprTicket")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        useMaprTForMaprdb.setLayoutData(data);

        authenticationMaprTComForMaprdb = new Composite(authenticationGrpForMaprdb, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationMaprTComForMaprdb.setLayoutData(data);
        authenticationMaprTComForMaprdb.setLayout(new GridLayout(3, false));

        authenticationUserPassComForMaprdb = new Composite(authenticationMaprTComForMaprdb, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 4;
        authenticationUserPassComForMaprdb.setLayoutData(data);
        authenticationUserPassComForMaprdb.setLayout(new GridLayout(3, false));

        maprTUsernameForMaprdbTxt = new LabelledText(authenticationUserPassComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.MaprTUsernameTxt.label"), 2); //$NON-NLS-1$
        maprTPasswordForMaprdbTxt = new LabelledText(authenticationUserPassComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.MaprTPasswordTxt.label"), 2, SWT.PASSWORD | SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$
        maprTPasswordForMaprdbTxt.getTextControl().setEchoChar('*');

        maprTClusterForMaprdbTxt = new LabelledText(authenticationMaprTComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.MaprTClusterTxt.label"), 2); //$NON-NLS-1$
        maprTDurationForMaprdbTxt = new LabelledText(authenticationMaprTComForMaprdb,
                Messages.getString("DatabaseForm.maprdb.MaprTDurationTxt.label"), 2); //$NON-NLS-1$

        addListenerMaprdbAuthentication();
        initForMaprdbAuthentication();
    }

    private void createExecutionFieldsForHive(Composite parent) {
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        hiveExecutionGrp = new Group(parent, SWT.NONE);
        hiveExecutionGrp.setText(Messages.getString("DatabaseForm.hiveExecution.group")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(hiveExecutionGrp);

        GridLayout exeLayout = new GridLayout(4, false);
        exeLayout.marginHeight = 0;
        hiveExecutionGrp.setLayout(exeLayout);

        hiveExecutionEngineCombo = new LabelledCombo(hiveExecutionGrp, Messages.getString("DatabaseForm.hiveExecution.engine"), //$NON-NLS-1$
                "", EHiveExecutionTypes.getExecutionTypeLabels(), 1, true, SWT.BORDER | SWT.READ_ONLY); //$NON-NLS-1$
        hiveExecutionEngineCombo.select(0);

        addListenerForHiveExecution();
        hideHiveExecutionFields(true);
    }

    private void addListenerForHiveExecution() {
        hiveExecutionEngineCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }
                EHiveExecutionTypes executionType = EHiveExecutionTypes.getTypeFromLabel(hiveExecutionEngineCombo.getText());
                if (executionType != null) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_EXECUTION_ENGINE, executionType.getValue());
                }
                checkFieldsValue();
            }
        });
    }

    private void initZnodeParent() {
        DatabaseConnection connection = getConnection();
        boolean isShow = connection != null
                && (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(getConnection().getDatabaseType()) || EDatabaseConnTemplate.MAPRDB
                        .getDBDisplayName().equals(getConnection().getDatabaseType()));
        hideControl(znodeparentGrp, !isShow);
        String setZnodeParentStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_ZNODE_PARENT);
        String znodeParent = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
        boolean selected_Znode_Parent = Boolean.valueOf(setZnodeParentStr);
        set_znode_parent.setSelection(selected_Znode_Parent);
        if (selected_Znode_Parent) {
            znode_parent.setText(StringUtils.trimToEmpty(znodeParent));
        }
        znode_parent.setVisible(set_znode_parent.getSelection());
    }

    private void hideHiveExecutionFields(boolean hide) {
        GridData hadoopData = (GridData) hiveExecutionGrp.getLayoutData();
        hadoopData.exclude = hide;
        hiveExecutionGrp.setVisible(!hide);
        hiveExecutionGrp.setLayoutData(hadoopData);
        hiveExecutionGrp.getParent().layout();
    }

    private void initForHBaseAuthentication() {
        hideControl(authenticationGrpForHBase, true);
        hideControl(authenticationComForImpala, true);
    }

    private void initForMaprdbAuthentication() {
        hideControl(authenticationGrpForMaprdb, true);
    }

    private void initForImpalaAuthentication() {
        authenticationGrpForImpala.setVisible(false);
        authenticationGrpForImpala.getParent().layout();
        useKerberosForImpala.setSelection(false);
        GridData hadoopData = (GridData) authenticationComForImpala.getLayoutData();
        hadoopData.exclude = true;
        authenticationComForImpala.setVisible(false);
        authenticationComForImpala.setLayoutData(hadoopData);
        authenticationComForImpala.getParent().layout();
    }

    private void initForAuthentication() {
        useKerberos.setSelection(false);
        GridData hadoopData = (GridData) authenticationCom.getLayoutData();
        hadoopData.exclude = true;
        authenticationCom.setVisible(false);
        authenticationCom.setLayoutData(hadoopData);
        authenticationCom.getParent().layout();
    }

    private void showIfAuthentication() {
        if (isHiveDBConnSelected()) {
            if (doSupportSecurity()) {
                updateAuthenticationForHive(isHiveEmbeddedMode());
                hideControlForKerbTickt();
                setHidAuthenticationForHive(false);
            } else {
                setHidAuthenticationForHive(true);
            }
        } else {
            setHidAuthenticationForHive(true);
        }
    }

    private void hideControlForKerbTickt() {
        hideControl(useKerberos, !doSupportKerb());
        hideControl(authenticationCom, !(doSupportKerb() && useKerberos.getSelection()));
        hideControl(useKeyTab, !doSupportKerb());
        hideControl(keyTabComponent, !(doSupportKerb() && useKeyTab.getSelection()));
        hideControl(useMaprTForHive, !doSupportTicket());
        hideControl(authenticationMaprTComForHive, !(useMaprTForHive.getSelection() && doSupportTicket()));
        hideControl(authenticationUserPassComForHive, doSupportKerb() && useKerberos.getSelection() && doSupportTicket());
    }

    private void showIfAdditionalJDBCSettings() {
        setHidAdditionalJDBCSettings(!isSupportHiveAdditionalSettings());
    }

    private boolean isSupportHiveAdditionalSettings() {
        if (isHiveDBConnSelected()) {
            IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
            if (hiveDistribution != null) {
                if (isHiveExecutedThroughWebHCat()) {
                    return false;
                }
                if (!doSupportHive2()) {
                    return false;
                }
                if (!HiveServerVersionInfo.HIVE_SERVER_2.getDisplayName().equals(hiveServerVersionCombo.getText())) {
                    return false;
                }
                if (!doSupportHiveStandaloneMode()) {
                    return false;
                }
                if (!isHiveStandaloneMode()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private void setHidAdditionalJDBCSettings(boolean hide) {
        if (hide) {
            additionalJDBCSettingsText.hide();
        } else {
            additionalJDBCSettingsText.show();
        }
    }

    private void showIfSupportEncryption() {
        setHideSSLEncryption(!isSupportSSLEncryption());
    }

    private boolean isSupportSSLEncryption() {
        if (isHiveDBConnSelected()) {
            IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
            if (hiveDistribution != null) {
                if (hiveDistribution.useCustom()) {
                    return true;
                }
                if (!doSupportHiveSSL()) {
                    return false;
                }
                if (!doSupportHive2()) {
                    return false;
                }
                if (!HiveServerVersionInfo.HIVE_SERVER_2.getDisplayName().equals(hiveServerVersionCombo.getText())) {
                    return false;
                }
                return true;
            }
        } else if (isOracleCustomDBConnSelected()) {
            return true;
        }
        return false;
    }

    private boolean isSupportSSLTrustStore() {
        if (isHiveDBConnSelected()) {
            // if (!useSSLEncryption.isVisible()) {
            // return false;
            // }
            if (!isSupportSSLEncryption()) {
                return false;
            }
            if (!useSSLEncryption.getSelection()) {
                return false;
            }
            IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
            if (hiveDistribution != null) {
                if (hiveDistribution.useCustom()) {
                    return true;
                }
                // no need to check useKerberos visible or not
                if (!useKerberos.getSelection()) {
                    return true;
                }
                if (doSupportHiveSSLwithKerberos()) {
                    return true;
                }
            }
        } else if (isOracleCustomDBConnSelected()) {
            return useSSLEncryption.getSelection();
        }
        return false;
    }

    private boolean isSupportSSLClientAuth() {
        if (isOracleCustomDBConnSelected()) {
            return useSSLEncryption.getSelection();
        }
        return false;
    }

    private boolean isSupportSSLKeyStore() {
        if (isOracleCustomDBConnSelected()) {
            return useSSLEncryption.getSelection() && needClientAuth.getSelection();
        }
        return false;
    }

    private void setHideSSLEncryption(boolean hide) {
        GridData hadoopData = (GridData) encryptionGrp.getLayoutData();
        hadoopData.exclude = hide;
        encryptionGrp.setVisible(!hide);
        encryptionGrp.setLayoutData(hadoopData);
        encryptionGrp.getParent().layout();
        setSSLTrustStoreParameters(hide);
        setSSLKeyStoreParameters(hide);
        if (hide) {
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_USE_SSL);
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_NEED_CLIENT_AUTH);
            getConnection().getParameters().removeKey(ConnParameterKeys.CONN_PARA_KEY_DISABLE_CBC_PROTECTION);
        } else {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL,
                    String.valueOf(useSSLEncryption.getSelection()));
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NEED_CLIENT_AUTH,
                    String.valueOf(needClientAuth.getSelection()));
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_DISABLE_CBC_PROTECTION,
                    String.valueOf(disableCBCProtection.getSelection()));
        }
        if (isHiveDBConnSelected() || isOracleCustomDBConnSelected()) {
            String url = getStringConnection();
            urlConnectionStringText.setText(url);
            getConnection().setURL(url);
        }
    }

    private void addListenerForZnodeParent() {
        set_znode_parent.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (set_znode_parent.getSelection()) {
                    znode_parent.setVisible(true);
                    znodeparentGrp.layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_ZNODE_PARENT,
                            Boolean.TRUE.toString());
                } else {
                    znode_parent.setVisible(false);
                    znodeparentGrp.layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_ZNODE_PARENT,
                            Boolean.FALSE.toString());
                }
            }

        });

        znode_parent.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT,
                            znode_parent.getText());
                }
            }
        });
    }

    private void addListenerForImpalaAuthentication() {
        useKerberosForImpala.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKerberosForImpala.getSelection()) {
                    GridData hadoopData = (GridData) authenticationComForImpala.getLayoutData();
                    hadoopData.exclude = false;
                    authenticationComForImpala.setVisible(true);
                    authenticationComForImpala.setLayoutData(hadoopData);
                    authenticationComForImpala.getParent().layout();
                    authenticationGrpForImpala.layout();
                    authenticationGrpForImpala.getParent().layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, Boolean.TRUE.toString());
                    getConnection().getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                            impalaPrincipalTxt.getText());
                } else {
                    GridData hadoopData = (GridData) authenticationComForImpala.getLayoutData();
                    hadoopData.exclude = true;
                    authenticationComForImpala.setVisible(false);
                    authenticationComForImpala.setLayoutData(hadoopData);
                    authenticationComForImpala.getParent().layout();
                    authenticationGrpForImpala.layout();
                    authenticationGrpForImpala.getParent().layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, Boolean.FALSE.toString());
                }
                urlConnectionStringText.setText(getStringConnection());
                modifyFieldValue();
            }

        });

        impalaPrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode() && impalaPrincipalTxt.isVisiable()) {
                    getConnection().getParameters().put(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA,
                            impalaPrincipalTxt.getText());
                    urlConnectionStringText.setText(getStringConnection());
                    modifyFieldValue();
                }
            }
        });
    }

    private void addListenerHBaseAuthentication() {
        useKerberosForHBase.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKerberosForHBase.getSelection()) {
                    hideControl(authenticationComForHBase, false);
                    hideControl(authenticationUserPassComForHBase, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "true"); //$NON-NLS-1$

                    String masterPrincipal = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
                    String regionServerPrincipal = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);

                    if (masterPrincipal != null && masterPrincipal.length() > 0) {
                        hbaseMasterPrincipalTxt.setText(masterPrincipal);
                    } else {
                        String defaultValue = getHBASEDefaultValue(EHadoopProperties.HBASE_MASTER_PRINCIPAL.getName());
                        if (defaultValue != null) {
                            hbaseMasterPrincipalTxt.setText(defaultValue);
                        }
                    }

                    if (regionServerPrincipal != null && regionServerPrincipal.length() > 0) {
                        hbaseRSPrincipalTxt.setText(regionServerPrincipal);
                    } else {
                        String defaultValue = getHBASEDefaultValue(EHadoopProperties.HBASE_REGIONSERVER_PRINCIPAL.getName());
                        if (defaultValue != null) {
                            hbaseRSPrincipalTxt.setText(defaultValue);
                        }
                    }

                } else {
                    hideControl(authenticationComForHBase, true);
                    hideControl(authenticationUserPassComForHBase, !useMaprTForHBase.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "false"); //$NON-NLS-1$
                }
                authenticationGrpForHBase.layout();
                authenticationGrpForHBase.getParent().layout();
            }

        });
        useKeyTabForHBase.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKeyTabForHBase.getSelection()) {
                    hideControl(keyTabCompoisteForHBase, false);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "true"); //$NON-NLS-1$
                } else {
                    hideControl(keyTabCompoisteForHBase, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "false"); //$NON-NLS-1$
                }
                authenticationComForHBase.layout();
                authenticationComForHBase.getParent().layout();
                authenticationGrpForHBase.layout();
                authenticationGrpForHBase.getParent().layout();
            }

        });
        principalForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                            principalForHBaseTxt.getText());
                }
            }
        });
        keytabForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB, keytabForHBaseTxt.getText());
                }
            }
        });
        hbaseMasterPrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL,
                            hbaseMasterPrincipalTxt.getText());
                }
            }
        });
        hbaseRSPrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                            hbaseRSPrincipalTxt.getText());
                }
            }
        });

        useMaprTForHBase.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useMaprTForHBase.getSelection()) {
                    hideControl(authenticationMaprTComForHBase, false);
                    hideControl(authenticationUserPassComForHBase, useKerberosForHBase.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USE_MAPRTICKET,
                            "true"); //$NON-NLS-1$

                    String maprTClusterForHBase = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
                    if (!getConnection().isContextMode() && ContextParameterUtils.isContainContextParam(maprTClusterForHBase)) {
                        maprTClusterForHBase = (String) metadataconnection
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
                    }
                    String maprTDurationForHBase = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
                    if (!getConnection().isContextMode() && ContextParameterUtils.isContainContextParam(maprTDurationForHBase)) {
                        maprTDurationForHBase = (String) metadataconnection
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
                    }

                    if (maprTClusterForHBase != null && maprTClusterForHBase.length() > 0) {
                        maprTClusterForHBaseTxt.setText(maprTClusterForHBase);
                    } else {
                        String defaultValue = getHBASEDefaultValue(EHadoopProperties.MAPRTICKET_CLUSTER.getName());
                        if (defaultValue != null) {
                            maprTClusterForHBaseTxt.setText(defaultValue);
                        }
                    }

                    if (maprTDurationForHBase != null && maprTDurationForHBase.length() > 0) {
                        maprTDurationForHBaseTxt.setText(maprTDurationForHBase);
                    } else {
                        String defaultValue = getHBASEDefaultValue(EHadoopProperties.MAPRTICKET_DURATION.getName());
                        if (defaultValue != null) {
                            maprTDurationForHBaseTxt.setText(defaultValue);
                        }
                    }
                } else {
                    hideControl(authenticationMaprTComForHBase, true);
                    hideControl(authenticationUserPassComForHBase, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USE_MAPRTICKET,
                            "false"); //$NON-NLS-1$
                }
                authenticationGrpForHBase.layout();
                authenticationGrpForHBase.getParent().layout();
            }

        });

        maprTUsernameForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME,
                            maprTUsernameForHBaseTxt.getText());
                }
            }
        });
        maprTPasswordForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                            maprTPasswordForHBaseTxt.getText());
                }
            }
        });
        maprTClusterForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                            maprTClusterForHBaseTxt.getText());
                }
            }
        });
        maprTDurationForHBaseTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION,
                            maprTDurationForHBaseTxt.getText());
                }
            }
        });
    }

    private void addListenerMaprdbAuthentication() {
        useKerberosForMaprdb.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKerberosForMaprdb.getSelection()) {
                    hideControl(authenticationComForMaprdb, false);
                    hideControl(authenticationUserPassComForMaprdb, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "true"); //$NON-NLS-1$

                    String masterPrincipal = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
                    String regionServerPrincipal = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);

                    if (masterPrincipal != null && masterPrincipal.length() > 0) {
                        maprdbMasterPrincipalTxt.setText(masterPrincipal);
                    } else {
                        String defaultValue = getMAPRDefaultValue(EHadoopProperties.MAPRDB_MASTER_PRINCIPAL.getName());
                        if (defaultValue != null) {
                            maprdbMasterPrincipalTxt.setText(defaultValue);
                        }
                    }

                    if (regionServerPrincipal != null && regionServerPrincipal.length() > 0) {
                        maprdbRSPrincipalTxt.setText(regionServerPrincipal);
                    } else {
                        String defaultValue = getMAPRDefaultValue(EHadoopProperties.MAPRDB_REGIONSERVER_PRINCIPAL.getName());
                        if (defaultValue != null) {
                            maprdbRSPrincipalTxt.setText(defaultValue);
                        }
                    }
                } else {
                    hideControl(authenticationComForMaprdb, true);
                    hideControl(authenticationUserPassComForMaprdb, !useMaprTForMaprdb.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "false"); //$NON-NLS-1$
                }
                authenticationGrpForMaprdb.layout();
                authenticationGrpForMaprdb.getParent().layout();
            }

        });
        useKeyTabForMaprdb.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKeyTabForMaprdb.getSelection()) {
                    hideControl(keyTabCompoisteForMaprdb, false);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "true"); //$NON-NLS-1$
                } else {
                    hideControl(keyTabCompoisteForMaprdb, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "false"); //$NON-NLS-1$
                }
                authenticationComForMaprdb.layout();
                authenticationComForMaprdb.getParent().layout();
                authenticationGrpForMaprdb.layout();
                authenticationGrpForMaprdb.getParent().layout();
            }

        });
        principalForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL,
                            principalForMaprdbTxt.getText());
                }
            }
        });
        keytabForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB, keytabForMaprdbTxt.getText());
                }
            }
        });
        maprdbMasterPrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL,
                            maprdbMasterPrincipalTxt.getText());
                }
            }
        });
        maprdbRSPrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL,
                            maprdbRSPrincipalTxt.getText());
                }
            }
        });

        useMaprTForMaprdb.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useMaprTForMaprdb.getSelection()) {
                    hideControl(authenticationMaprTComForMaprdb, false);
                    hideControl(authenticationUserPassComForMaprdb, useKerberosForMaprdb.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USE_MAPRTICKET,
                            "true"); //$NON-NLS-1$

                    String maprTClusterForMapr = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
                    if (!getConnection().isContextMode() && ContextParameterUtils.isContainContextParam(maprTClusterForMapr)) {
                        maprTClusterForMapr = (String) metadataconnection
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
                    }
                    String maprTDurationForMapr = getConnection().getParameters().get(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
                    if (!getConnection().isContextMode() && ContextParameterUtils.isContainContextParam(maprTDurationForMapr)) {
                        maprTDurationForMapr = (String) metadataconnection
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
                    }

                    if (maprTClusterForMaprdbTxt != null && maprTClusterForMapr.length() > 0) {
                        maprTClusterForMaprdbTxt.setText(maprTClusterForMapr);
                    } else {
                        String defaultValue = getMAPRDefaultValue(EHadoopProperties.MAPRTICKET_CLUSTER.getName());
                        if (defaultValue != null) {
                            maprTClusterForMaprdbTxt.setText(defaultValue);
                        }
                    }

                    if (maprTDurationForMapr != null && maprTDurationForMapr.length() > 0) {
                        maprTDurationForMaprdbTxt.setText(maprTDurationForMapr);
                    } else {
                        String defaultValue = getMAPRDefaultValue(EHadoopProperties.MAPRTICKET_DURATION.getName());
                        if (defaultValue != null) {
                            maprTDurationForMaprdbTxt.setText(defaultValue);
                        }
                    }
                } else {
                    hideControl(authenticationMaprTComForMaprdb, true);
                    hideControl(authenticationUserPassComForMaprdb, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USE_MAPRTICKET,
                            "false"); //$NON-NLS-1$
                }
                authenticationGrpForMaprdb.layout();
                authenticationGrpForMaprdb.getParent().layout();
            }

        });

        maprTUsernameForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME,
                            maprTUsernameForMaprdbTxt.getText());
                }
            }
        });
        maprTPasswordForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD,
                            maprTPasswordForMaprdbTxt.getText());
                }
            }
        });
        maprTClusterForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER,
                            maprTClusterForMaprdbTxt.getText());
                }
            }
        });
        maprTDurationForMaprdbTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(
                            ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION,
                            maprTDurationForMaprdbTxt.getText());
                }
            }
        });
    }

    private void addListenerForAuthentication() {
        useKeyTab.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKeyTab.getSelection()) {
                    GridData hadoopData = (GridData) keyTabComponent.getLayoutData();
                    hadoopData.exclude = false;
                    keyTabComponent.setVisible(true);
                    keyTabComponent.setLayoutData(hadoopData);
                    keyTabComponent.getParent().layout();
                    authenticationGrp.layout();
                    authenticationGrp.getParent().layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "true"); //$NON-NLS-1$
                } else {
                    GridData hadoopData = (GridData) keyTabComponent.getLayoutData();
                    hadoopData.exclude = true;
                    keyTabComponent.setVisible(false);
                    keyTabComponent.setLayoutData(hadoopData);
                    keyTabComponent.getParent().layout();
                    authenticationGrp.layout();
                    authenticationGrp.getParent().layout();
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB, "false"); //$NON-NLS-1$
                }
            }

        });
        useKerberos.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useKerberos.getSelection()) {
                    GridData hadoopData = (GridData) authenticationCom.getLayoutData();
                    hadoopData.exclude = false;
                    authenticationCom.setVisible(true);
                    authenticationCom.setLayoutData(hadoopData);
                    authenticationCom.getParent().layout();
                    authenticationGrp.layout();
                    authenticationGrp.getParent().layout();
                    hideControl(authenticationUserPassComForHive, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "true");
                } else {
                    GridData hadoopData = (GridData) authenticationCom.getLayoutData();
                    hadoopData.exclude = true;
                    authenticationCom.setVisible(false);
                    authenticationCom.setLayoutData(hadoopData);
                    authenticationCom.getParent().layout();
                    authenticationGrp.layout();
                    authenticationGrp.getParent().layout();
                    hideControl(authenticationUserPassComForHive, !useMaprTForHive.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_KRB, "false");
                }
                updateSSLEncryptionDetailsDisplayStatus();
                urlConnectionStringText.setText(getStringConnection());
            }

        });
        principalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL, principalTxt.getText());
                }
            }
        });
        keytabTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_KEYTAB, keytabTxt.getText());
                }
            }
        });
        hivePrincipalTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA,
                            hivePrincipalTxt.getText());
                    urlConnectionStringText.setText(getStringConnection());
                }
            }
        });
        metastoreUrlTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL,
                            metastoreUrlTxt.getText());
                }
            }
        });
        driverJarTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(driverJarTxt.getText())) {
                        getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH,
                                driverJarTxt.getText());
                    }
                }
            }
        });
        browseDriverJarBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                SelectDatabaseJarDialog dialog = new SelectDatabaseJarDialog(getShell(), driverJarTxt.getText());
                if (dialog.open() == Window.OK) {
                    driverJarTxt.setText(dialog.getJarsString());
                }
            }

        });
        driverClassTxt.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS,
                            driverClassTxt.getText());
                }
            }
        });
        browseDriverClassButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                driverClassTxt.removeAll();
                for (String stringToFile : driverJarTxt.getText().trim().split(";")) { //$NON-NLS-1$
                    File file = new File(stringToFile);
                    if (file != null) {
                        try {
                            MyURLClassLoader cl = new MyURLClassLoader(file.toURL());
                            Class[] classes = cl.getAssignableClasses(Driver.class);
                            for (Class classe : classes) {
                                driverClassTxt.add(classe.getName());
                            }
                        } catch (Exception ex) {
                            ExceptionHandler.process(ex);
                        }
                    }
                }
                if (driverClassTxt.getItemCount() > 0) {
                    String driverClassName = driverClassTxt.getItem(0);
                    driverClassTxt.setText(driverClassName);
                }
            }
        });
        usernameTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME, usernameTxt.getText());
                }
            }
        });
        passwordTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD, passwordTxt.getText());
                }
            }
        });

        useMaprTForHive.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (useMaprTForHive.getSelection()) {
                    hideControl(authenticationMaprTComForHive, false);
                    hideControl(authenticationUserPassComForHive, useKerberos.getSelection());
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USE_MAPRTICKET,
                            "true"); //$NON-NLS-1$
                } else {
                    hideControl(authenticationMaprTComForHive, true);
                    hideControl(authenticationUserPassComForHive, true);
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USE_MAPRTICKET,
                            "false"); //$NON-NLS-1$
                }
                authenticationGrp.layout();
                authenticationGrp.getParent().layout();
            }

        });

        maprTUsernameForHiveTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME,
                            maprTUsernameForHiveTxt.getText());
                }
            }
        });
        maprTPasswordForHiveTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD,
                            maprTPasswordForHiveTxt.getText());
                }
            }
        });
        maprTClusterForHiveTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER,
                            maprTClusterForHiveTxt.getText());
                }
            }
        });
        maprTDurationForHiveTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION,
                            maprTDurationForHiveTxt.getText());
                }
            }
        });
    }

    private void addListenersForEncryptionGroup() {
        useSSLEncryption.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_SSL,
                            String.valueOf(useSSLEncryption.getSelection()));
                    updateSSLEncryptionDetailsDisplayStatus();
                    urlConnectionStringText.setText(getStringConnection());
                }
            }

        });
        trustStorePath.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    updateTrustStorePathParameter();
                    urlConnectionStringText.setText(getStringConnection());
                }
            }
        });
        trustStorePath.setAfterSetNewValueCallable(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                if (!isContextMode()) {
                    updateTrustStorePathParameter();
                    urlConnectionStringText.setText(getStringConnection());
                }
                return null;
            }
        });
        trustStorePassword.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    updateTrustStorePasswordParameter();
                }
            }
        });

        needClientAuth.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NEED_CLIENT_AUTH,
                            String.valueOf(needClientAuth.getSelection()));
                    updateSSLEncryptionDetailsDisplayStatus();
                    urlConnectionStringText.setText(getStringConnection());
                }
            }

        });
        keyStorePath.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    updateKeyStorePathParameter();
                    urlConnectionStringText.setText(getStringConnection());
                }
            }
        });
        keyStorePath.setAfterSetNewValueCallable(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                if (!isContextMode()) {
                    updateKeyStorePathParameter();
                    urlConnectionStringText.setText(getStringConnection());
                }
                return null;
            }
        });
        keyStorePassword.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isContextMode()) {
                    updateKeyStorePasswordParameter();
                }
            }
        });

        disableCBCProtection.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_DISABLE_CBC_PROTECTION,
                            String.valueOf(disableCBCProtection.getSelection()));
                    updateSSLEncryptionDetailsDisplayStatus();
                    urlConnectionStringText.setText(getStringConnection());
                }
            }

        });
    }

    private void updateTrustStorePathParameter() {
        getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH, trustStorePath.getText());
    }

    private void updateTrustStorePasswordParameter() {
        getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD,
                getConnection().getValue(trustStorePassword.getText(), true));
        String url = getStringConnection();
        urlConnectionStringText.setText(url);
        getConnection().setURL(url);
    }

    private void updateKeyStorePathParameter() {
        getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH, keyStorePath.getText());
    }

    private void updateKeyStorePasswordParameter() {
        getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD,
                getConnection().getValue(keyStorePassword.getText(), true));
        String url = getStringConnection();
        urlConnectionStringText.setText(url);
        getConnection().setURL(url);
    }

    private void setHidAuthenticationForHive(boolean hide) {
        GridData hadoopData = (GridData) authenticationGrp.getLayoutData();
        hadoopData.exclude = hide;
        authenticationGrp.setVisible(!hide);
        authenticationGrp.setLayoutData(hadoopData);
        authenticationGrp.getParent().layout();
    }

    private void updateAuthenticationForHive(boolean isEmbeded) {
        if (isEmbeded) {
            metastoreUrlTxt.show();
            driverJarTxt.show();
            hideControl(browseDriverJarBtn, false);
            driverClassTxt.setHideWidgets(false);
            hideControl(browseDriverClassButton, false);
            usernameTxt.show();
            passwordTxt.show();
        } else {
            metastoreUrlTxt.hide();
            driverJarTxt.hide();
            hideControl(browseDriverJarBtn, true);
            driverClassTxt.setHideWidgets(true);
            hideControl(browseDriverClassButton, true);
            usernameTxt.hide();
            passwordTxt.hide();
        }
    }

    private void updateHadoopPropertiesFieldsState() {
        String databaseType = getConnection().getDatabaseType();
        boolean showHp = showHadoopProperties(databaseType);
        GridData hadoopData = (GridData) hadoopPropertiesComposite.getLayoutData();
        hadoopData.exclude = !showHp;
        hadoopPropertiesComposite.setVisible(showHp);
        hadoopPropertiesComposite.setLayoutData(hadoopData);
        hadoopPropertiesComposite.getParent().layout();
    }

    private boolean showHadoopProperties(String databaseType) {
        return EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)
                || EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)
                || EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType);
    }

    private String getHadoopProperties(String databaseType) {
        EMap<String, String> parameters = getConnection().getParameters();
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
            return parameters.get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
        } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
            return parameters.get(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES);
        } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
            return parameters.get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES);
        }

        return null;
    }

    private void createHadoopPropertiesFields(Composite parent) {
        hadoopPropertiesComposite = new Composite(parent, SWT.NONE);
        GridLayout hpGridLayout = new GridLayout(1, false);
        hpGridLayout.marginHeight = 0;
        hadoopPropertiesComposite.setLayout(hpGridLayout);
        GridData hpGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        hpGridData.horizontalSpan = 3;
        hadoopPropertiesComposite.setLayoutData(hpGridData);

        hadoopPropertiesDialog = new HadoopPropertiesDialog(getShell(), hadoopClusterPropertiesList, hadoopPropertiesList) {

            @Override
            protected boolean isReadOnly() {
                return readOnly || isContextMode();
            }

            @Override
            protected List<Map<String, Object>> getLatestInitProperties() {
                refreshHadoopProperties();
                return hadoopPropertiesList;
            }

            @Override
            protected void applyProperties(List<Map<String, Object>> hdProperties) {
                updateHdProperties(hdProperties);
            }

            @Override
            protected int getMarginHeight() {
                return 0;
            }

            @Override
            protected int getMarginWidth() {
                return 0;
            }

        };
        refreshHadoopProperties();
        hadoopPropertiesDialog.createPropertiesFields(hadoopPropertiesComposite);
        updateHadoopPropertiesFieldsState();
    }

    private void updateHdProperties(List<Map<String, Object>> hdProperties) {
        String databaseType = getConnection().getDatabaseType();
        String hadoopPropertiesJsonStr = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(hdProperties);
        EMap<String, String> parameters = getConnection().getParameters();
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
            parameters.put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES, hadoopPropertiesJsonStr);
        } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
            parameters.put(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES, hadoopPropertiesJsonStr);
        } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
            parameters.put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES, hadoopPropertiesJsonStr);
        }
    }

    private List<Map<String, Object>> getHadoopClusterProperties() {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            return HadoopRepositoryUtil.getHadoopPropertiesList(hadoopClusterService.getHadoopClusterProperties(getConnection()));
        }

        return null;
    }

    private void refreshHadoopProperties() {
        if (hadoopPropertiesDialog != null) {
            String dbType = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE);
            String databaseType = dbType != null ? dbType : getConnection().getDatabaseType();
            String hadoopProperties = getHadoopProperties(databaseType);
            hadoopPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
            hadoopClusterPropertiesList = getHadoopClusterProperties();
            hadoopPropertiesDialog.setInitProperties(hadoopPropertiesList);
            hadoopPropertiesDialog.setInitPropertiesOfParent(hadoopClusterPropertiesList);
        }
    }

    private void createHivePropertiesFields(Composite parent) {
        hiveJDBCPropertiesComposite = new Composite(parent, SWT.NONE);
        GridLayout hivePropGridLayout = new GridLayout(1, false);
        hivePropGridLayout.marginHeight = 0;
        hiveJDBCPropertiesComposite.setLayout(hivePropGridLayout);
        GridData hivePropGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        hivePropGridData.horizontalSpan = 3;
        hiveJDBCPropertiesComposite.setLayoutData(hivePropGridData);

        hiveJDBCPropertiesDialog = new HiveJDBCPropertiesDialog(getShell(), hiveJDBCPropertiesList) {

            @Override
            protected boolean isReadOnly() {
                return readOnly || isContextMode();
            }

            @Override
            protected List<Map<String, Object>> getLatestInitProperties() {
                initHiveJDBCProperties();
                return hiveJDBCPropertiesList;
            }

            @Override
            protected void applyProperties(List<Map<String, Object>> hdProperties) {
                updateHiveJDBCProperties(hdProperties);
            }

            @Override
            protected int getMarginWidth() {
                return 0;
            }

        };
        initHiveJDBCProperties();
        hiveJDBCPropertiesDialog.createPropertiesFields(hiveJDBCPropertiesComposite);
        updateHiveJDBCPropertiesFieldsState();
    }

    private void initHiveJDBCProperties() {
        if (hiveJDBCPropertiesDialog != null) {
            String jdbcProperties = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
            hiveJDBCPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(jdbcProperties);
            hiveJDBCPropertiesDialog.setInitProperties(hiveJDBCPropertiesList);
        }
    }

    private void updateHiveJDBCProperties(List<Map<String, Object>> hiveJDBCProperties) {
        String databaseType = getConnection().getDatabaseType();
        String hiveJDBCPropertiesJsonStr = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(hiveJDBCProperties);
        EMap<String, String> parameters = getConnection().getParameters();
        if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
            parameters.put(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES, hiveJDBCPropertiesJsonStr);
        }
    }

    private void updateHiveJDBCPropertiesFieldsState() {
        String databaseType = getConnection().getDatabaseType();
        boolean show = EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType);
        GridData hadoopData = (GridData) hiveJDBCPropertiesComposite.getLayoutData();
        hadoopData.exclude = !show;
        hiveJDBCPropertiesComposite.setVisible(show);
        hiveJDBCPropertiesComposite.setLayoutData(hadoopData);
        hiveJDBCPropertiesComposite.getParent().layout();
    }

    private void refreshHiveJdbcProperties() {
        if (hiveJDBCPropertiesDialog != null) {
            String hiveProperties = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
            hiveJDBCPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hiveProperties);
            hiveJDBCPropertiesDialog.setInitProperties(hiveJDBCPropertiesList);
        }
    }

    private void createHBaseSettingContents(Composite parent) {
        if (canLinkToHadoopCluster()) {
            createHadoopLinkPart(parent);
        }

        hbaseSettingGroup = Form.createGroup(parent, 4, Messages.getString("DatabaseForm.hbase.settings"), 60); //$NON-NLS-1$
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).applyTo(hbaseSettingGroup);
        String[] hbaseDistributionsDisplay = new String[0];
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            hbaseDistributionsDisplay = hadoopService.getHBaseDistributionManager().getDistributionsDisplay(true);
        }

        hbaseDistributionCombo = new LabelledCombo(hbaseSettingGroup, Messages.getString("DatabaseForm.hbase.distribution"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.hbase.distribution.tooltip"), //$NON-NLS-1$
                hbaseDistributionsDisplay, 1, true);
        hbaseVersionCombo = new LabelledCombo(hbaseSettingGroup, Messages.getString("DatabaseForm.hbase.version"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.hbase.version.tooltip"), new String[0], 1, true); //$NON-NLS-1$
        hbaseVersionCombo.setHideWidgets(true);
        hbaseCustomButton = new Button(hbaseSettingGroup, SWT.NONE);
        hbaseCustomButton.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        hbaseCustomButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));
        hbaseCustomButton.setVisible(false);
        hideHBaseSettings(true);
    }

    private void createMaprdbSettingContents(Composite parent) {
        if (canLinkToHadoopCluster()) {
            createHadoopLinkPart(parent);
        }

        maprdbSettingGroup = Form.createGroup(parent, 4, Messages.getString("DatabaseForm.maprdb.settings"), 60); //$NON-NLS-1$
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).applyTo(maprdbSettingGroup);
        String[] maprdbDistributionsDisplay = new String[0];
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            maprdbDistributionsDisplay = hadoopService.getMaprdbDistributionManager().getDistributionsDisplay(true);
        }

        maprdbDistributionCombo = new LabelledCombo(maprdbSettingGroup, Messages.getString("DatabaseForm.maprdb.distribution"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.maprdb.distribution.tooltip"), //$NON-NLS-1$
                maprdbDistributionsDisplay, 1, true);
        maprdbVersionCombo = new LabelledCombo(maprdbSettingGroup, Messages.getString("DatabaseForm.maprdb.version"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.maprdb.version.tooltip"), new String[0], 1, true); //$NON-NLS-1$
        maprdbVersionCombo.setHideWidgets(true);
        maprdbCustomButton = new Button(maprdbSettingGroup, SWT.NONE);
        maprdbCustomButton.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        maprdbCustomButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));
        maprdbCustomButton.setVisible(false);
        hideMaprdbSettings(true);
    }

    private void createImpalaSettingContents(Composite parent) {
        if (canLinkToHadoopCluster()) {
            createHadoopLinkPart(parent);
        }

        impalaSettingGroup = Form.createGroup(parent, 5, Messages.getString("DatabaseForm.impala.settings"), 60); //$NON-NLS-1$
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).applyTo(impalaSettingGroup);

        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        String[] items = new String[0];
        if (hadoopService != null) {
            items = hadoopService.getImpalaDistributionManager().getDistributionsDisplay(true);
        }
        impalaDistributionCombo = new LabelledCombo(impalaSettingGroup, Messages.getString("DatabaseForm.impala.distribution"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.impala.distribution.tooltip"), //$NON-NLS-1$
                items, 1, true);
        impalaVersionCombo = new LabelledCombo(impalaSettingGroup, Messages.getString("DatabaseForm.impala.version"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.impala.version.tooltip"), new String[0], 1, true); //$NON-NLS-1$
        impalaVersionCombo.setHideWidgets(true);
        impalaCustomButton = new Button(impalaSettingGroup, SWT.NONE);
        impalaCustomButton.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        impalaCustomButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));
        impalaCustomButton.setVisible(false);
        hideImpalaSettings(true);
    }

    /**
     * DOC plv Comment method "createLinkGroup".
     * 
     * @param parent
     */
    private void createHadoopLinkPart(Composite parent) {
        if (hadoopLinkComp != null) {
            return;
        }
        hadoopLinkComp = new Composite(parent, SWT.NONE);
        hadoopLinkComp.setLayout(new GridLayout(4, false));
        GridLayout parentLayout = (GridLayout) parent.getLayout();
        GridDataFactory.fillDefaults().span(parentLayout.numColumns, 1).applyTo(hadoopLinkComp);
        String[] types = new String[] { Messages.getString("DatabaseForm.hc.link.none"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.hc.link.repository") }; //$NON-NLS-1$
        hcPropertyTypeCombo = new LabelledCombo(hadoopLinkComp, Messages.getString("DatabaseForm.hc.link.title"), "", types, 1, //$NON-NLS-1$ //$NON-NLS-2$
                true);
        hcPropertyTypeCombo.select(0);
        GridDataFactory.fillDefaults().span(1, 1).align(SWT.FILL, SWT.CENTER).applyTo(hcPropertyTypeCombo.getCombo());
        hcRepositoryText = new Text(hadoopLinkComp, SWT.BORDER);
        hcRepositoryText.setEditable(false);
        GridDataFactory.fillDefaults().grab(true, false).span(1, 1).align(SWT.FILL, SWT.CENTER).applyTo(hcRepositoryText);
        hcSelectBtn = new Button(hadoopLinkComp, SWT.PUSH);
        hcSelectBtn.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        GridDataFactory.fillDefaults().grab(false, false).align(SWT.BEGINNING, SWT.CENTER).span(1, 1).applyTo(hcSelectBtn);
        hideHCLinkSettings(true);
    }

    /**
     * DOC plv Comment method "addHadoopClusterLinkListeners".
     */
    private void addHadoopClusterLinkListeners() {
        hcPropertyTypeCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (hcPropertyTypeCombo.getSelectionIndex() == 0) {
                    updateHCRelatedParameters(null);
                }
                updateHCRelatedParts();
                adaptHadoopLinkedPartToReadOnly();
                refreshHadoopProperties();
            }
        });

        hcSelectBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                RepositoryNode node = CoreRuntimePlugin.getInstance().getRepositoryService()
                        .getRepNodeFromRepReviewDialog(getShell(), ERepositoryObjectType.METADATA, "HADOOPCLUSTER"); //$NON-NLS-1$
                if (node != null) {
                    String id = node.getObject().getId();
                    updateHCRelatedParameters(id);
                    updateHCRelatedParts();
                    refreshHadoopProperties();
                }
            }
        });
    }

    private void adaptHadoopLinkedPartToReadOnly() {
        boolean fromRepository = hcPropertyTypeCombo.getSelectionIndex() == 1;

        hcSelectBtn.setVisible(fromRepository);
        hcRepositoryText.setVisible(fromRepository);

        hbaseDistributionCombo.setReadOnly(fromRepository || isContextMode());
        hbaseVersionCombo.setReadOnly(fromRepository || isContextMode());
        hbaseCustomButton.setEnabled(!fromRepository && !isContextMode());

        maprdbDistributionCombo.setReadOnly(fromRepository || isContextMode());
        maprdbVersionCombo.setReadOnly(fromRepository || isContextMode());
        maprdbCustomButton.setEnabled(!fromRepository && !isContextMode());

        impalaDistributionCombo.setReadOnly(fromRepository || isContextMode());
        impalaVersionCombo.setReadOnly(fromRepository || isContextMode());
        impalaCustomButton.setEnabled(!fromRepository && !isContextMode());

        hiveDistributionCombo.setReadOnly(fromRepository || isContextMode());
        hiveVersionCombo.setReadOnly(fromRepository || isContextMode());
        nameNodeURLTxt.setReadOnly(fromRepository || isContextMode());
        jobTrackerURLTxt.setReadOnly(fromRepository || isContextMode());
        hiveCustomButton.setEnabled(!fromRepository && !isContextMode());

        hcPropertyTypeCombo.setReadOnly(isContextMode());
        hiveModeCombo.setReadOnly(isContextMode());
        hiveServerVersionCombo.setReadOnly(isContextMode());

        useKerberos.setEnabled(!isContextMode());
        useKeyTab.setEnabled(!isContextMode());
        browseDriverJarBtn.setEnabled(!isContextMode());
        browseDriverClassButton.setEnabled(!isContextMode());
        hivePrincipalTxt.setEditable(!isContextMode());
        metastoreUrlTxt.setEditable(!isContextMode());
        driverJarTxt.setEditable(!isContextMode());
        driverClassTxt.setReadOnly(isContextMode());
        usernameTxt.setEditable(!isContextMode());
        passwordTxt.setEditable(!isContextMode());
        principalTxt.setEditable(!isContextMode());
        keytabTxt.setEditable(!isContextMode());
        additionalJDBCSettingsText.setEditable(!isContextMode());
        useSSLEncryption.setEnabled(!isContextMode());
        trustStorePath.setEditable(!isContextMode());
        trustStorePassword.setEditable(!isContextMode());

        useMaprTForHive.setEnabled(!isContextMode());
        maprTUsernameForHiveTxt.setEditable(!isContextMode());
        maprTPasswordForHiveTxt.setEditable(!isContextMode());
        maprTClusterForHiveTxt.setEditable(!isContextMode());
        maprTDurationForHiveTxt.setEditable(!isContextMode());
        if (isContextMode()) {
            maprTPasswordForHiveTxt.getTextControl().setEchoChar('\0');
        } else {
            maprTPasswordForHiveTxt.getTextControl().setEchoChar('*');
        }
    }

    private void adaptHiveDBHadoopPartEditable() {
        if (isContextMode()) {
            trustStorePassword.getTextControl().setEchoChar('\0');
        } else {
            trustStorePassword.getTextControl().setEchoChar('*');
        }
    }

    private void adaptHbaseHadoopPartEditable() {
        hcPropertyTypeCombo.setReadOnly(isContextMode());
        hbaseDistributionCombo.setReadOnly(isContextMode());
        hbaseVersionCombo.setReadOnly(isContextMode());
        useKerberosForHBase.setEnabled(!isContextMode());
        useKeyTabForHBase.setEnabled(!isContextMode());
        hbaseMasterPrincipalTxt.setEditable(!isContextMode());
        hbaseRSPrincipalTxt.setEditable(!isContextMode());
        principalForHBaseTxt.setEditable(!isContextMode());
        keytabForHBaseTxt.setEditable(!isContextMode());

        useMaprTForHBase.setEnabled(!isContextMode());
        maprTUsernameForHBaseTxt.setEditable(!isContextMode());
        maprTPasswordForHBaseTxt.setEditable(!isContextMode());
        maprTClusterForHBaseTxt.setEditable(!isContextMode());
        maprTDurationForHBaseTxt.setEditable(!isContextMode());

        set_table_ns_mapping.setEnabled(!isContextMode());
        tableNSMappingOfHbaseTxt.setEditable(!isContextMode());
        set_znode_parent.setEnabled(!isContextMode());
        znode_parent.setEditable(!isContextMode());

        if (isContextMode()) {
            maprTPasswordForHBaseTxt.getTextControl().setEchoChar('\0');
        } else {
            maprTPasswordForHBaseTxt.getTextControl().setEchoChar('*');
        }
    }

    private void adaptMaprdbHadoopPartEditable() {
        hcPropertyTypeCombo.setReadOnly(isContextMode());
        maprdbDistributionCombo.setReadOnly(isContextMode());
        maprdbVersionCombo.setReadOnly(isContextMode());
        tableNSMappingOfMapRDBTxt.setEditable(!isContextMode());
        useKerberosForMaprdb.setEnabled(!isContextMode());
        useKeyTabForMaprdb.setEnabled(!isContextMode());
        maprdbMasterPrincipalTxt.setEditable(!isContextMode());
        maprdbRSPrincipalTxt.setEditable(!isContextMode());
        principalForMaprdbTxt.setEditable(!isContextMode());
        keytabForMaprdbTxt.setEditable(!isContextMode());

        useMaprTForMaprdb.setEnabled(!isContextMode());
        maprTUsernameForMaprdbTxt.setEditable(!isContextMode());
        maprTPasswordForMaprdbTxt.setEditable(!isContextMode());
        maprTClusterForMaprdbTxt.setEditable(!isContextMode());
        maprTDurationForMaprdbTxt.setEditable(!isContextMode());

        set_znode_parent.setEnabled(!isContextMode());
        znode_parent.setEditable(!isContextMode());

        if (isContextMode()) {
            maprTPasswordForMaprdbTxt.getTextControl().setEchoChar('\0');
        } else {
            maprTPasswordForMaprdbTxt.getTextControl().setEchoChar('*');
        }
    }

    private void adaptImpalaHadoopPartEditable() {
        useKerberosForImpala.setEnabled(!isContextMode());
        impalaPrincipalTxt.setEditable(!isContextMode());
    }

    private void adaptOracleCustomPartEditable() {
        useSSLEncryption.setEnabled(!isContextMode());
        trustStorePath.setEditable(!isContextMode());
        trustStorePassword.setEditable(!isContextMode());
        needClientAuth.setEnabled(!isContextMode());
        keyStorePath.setEditable(!isContextMode());
        keyStorePassword.setEditable(!isContextMode());
        disableCBCProtection.setEnabled(!isContextMode());
        if (isContextMode()) {
            trustStorePassword.getTextControl().setEchoChar('\0');
            keyStorePassword.getTextControl().setEchoChar('\0');
        } else {
            trustStorePassword.getTextControl().setEchoChar('*');
            keyStorePassword.getTextControl().setEchoChar('*');
        }
    }

    private void updateHadoopProperties(boolean isEditable) {
        refreshHadoopProperties();
        refreshHiveJdbcProperties();
        updatePropertiesFileds(isEditable);
    }

    private void updatePropertiesFileds(boolean isEditable) {
        if (hadoopPropertiesDialog != null) {
            hadoopPropertiesDialog.updateStatusLabel(hadoopPropertiesList);
        }
        if (hiveJDBCPropertiesDialog != null) {
            hiveJDBCPropertiesDialog.updateStatusLabel(hiveJDBCPropertiesList);
        }
    }

    private void updateHCRelatedParameters(String hcId) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            if (hcId == null) {
                getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID, hcId);
            } else {
                Map<String, String> dbParameters = hadoopClusterService.getHadoopDbParameters(hcId);
                EMap<String, String> connParameters = getConnection().getParameters();
                Iterator<Entry<String, String>> iter = dbParameters.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    connParameters.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void clearHadoopRelatedParameters() {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            hadoopClusterService.removeHadoopDbParameters(getConnection());
        }
    }

    private void updateHCRelatedParts() {
        String hcId = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
        if (hcId != null) {
            IRepositoryViewObject repObj = null;
            try {
                repObj = ProxyRepositoryFactory.getInstance().getLastVersion(hcId);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
            if (repObj != null && repObj.getProperty() != null) {
                hcRepositoryText.setText(repObj.getProperty().getLabel());
            } else {
                hcRepositoryText.setText(""); //$NON-NLS-1$
            }
        } else {
            hcRepositoryText.setText(""); //$NON-NLS-1$
        }
        if (isDBTypeSelected(EDatabaseConnTemplate.HBASE)) {
            initHBaseSettings();
        }
        if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)) {
            initMaprdbSettings();
        }
        if (isDBTypeSelected(EDatabaseConnTemplate.HIVE)) {
            initHiveInfo();
        }
        if (isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
            initImpalaSettings();
        }
    }

    private void hideHBaseSettings(boolean hide) {
        GridData hadoopData = (GridData) hbaseSettingGroup.getLayoutData();
        hbaseSettingGroup.setVisible(!hide);
        hadoopData.exclude = hide;
        hbaseDistributionCombo.setHideWidgets(hide);
    }

    private void hideMaprdbSettings(boolean hide) {
        GridData hadoopData = (GridData) maprdbSettingGroup.getLayoutData();
        maprdbSettingGroup.setVisible(!hide);
        hadoopData.exclude = hide;
        maprdbDistributionCombo.setHideWidgets(hide);
    }

    private void hideImpalaSettings(boolean hide) {
        GridData hadoopData = (GridData) impalaSettingGroup.getLayoutData();
        impalaSettingGroup.setVisible(!hide);
        hadoopData.exclude = hide;
        impalaDistributionCombo.setHideWidgets(hide);
    }

    private void hideHCLinkSettings(boolean hide) {
        if (hadoopLinkComp == null) {
            return;
        }
        GridData hadoopLinkData = (GridData) hadoopLinkComp.getLayoutData();
        hadoopLinkComp.setVisible(!hide);
        hadoopLinkData.exclude = hide;
        hcPropertyTypeCombo.setHideWidgets(hide);
    }

    private IHDistribution getHBaseDistribution(String hbaseDistribution, boolean byDisplay) {
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            return hadoopService.getHBaseDistributionManager().getDistribution(hbaseDistribution, byDisplay);
        }
        return null;
    }

    private void initHBaseSettings() {
        DatabaseConnection connection = getConnection();
        String hadoopDistribution = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
        String hadoopVersion = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);

        IHDistribution hBaseDistribution = getHBaseDistribution(hadoopDistribution, false);

        if (hBaseDistribution != null) {
            String distributionDisplayName = hBaseDistribution.getDisplayName();
            hbaseDistributionCombo.setText(distributionDisplayName);
            updateHBaseVersionPart(hBaseDistribution);
            IHDistributionVersion hdVersion = hBaseDistribution.getHDVersion(hadoopVersion, false);
            if (hdVersion != null && hdVersion.getDisplayVersion() != null) {
                hbaseVersionCombo.setText(hdVersion.getDisplayVersion());
            } else {
                hbaseVersionCombo.select(0);
            }
        } else {
            hbaseDistributionCombo.select(0);
            hbaseVersionCombo.select(0);
        }

        String useKrbString = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
        String useKeytabString = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB);
        String keytabPrincipal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        String keytab = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(keytabPrincipal)) {
            keytabPrincipal = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        }
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(keytab)) {
            keytab = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        }
        String masterPrincipal = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
        String regionServerPrincipal = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
        boolean useKrb = Boolean.valueOf(useKrbString);
        boolean useKeytab = Boolean.valueOf(useKeytabString);
        useKerberosForHBase.setSelection(useKrb);
        if (useKrb) {
            useKeyTabForHBase.setSelection(useKeytab);
            if (useKeytab) {
                principalForHBaseTxt.setText(StringUtils.trimToEmpty(keytabPrincipal));
                keytabForHBaseTxt.setText(StringUtils.trimToEmpty(keytab));
            }
            hbaseMasterPrincipalTxt.setText(StringUtils.trimToEmpty(masterPrincipal));
            hbaseRSPrincipalTxt.setText(StringUtils.trimToEmpty(regionServerPrincipal));
        }
        hideControl(keyTabCompoisteForHBase, !useKeytab);
        hideControl(authenticationComForHBase, !useKrb);
        hideControl(authenticationGrpForHBase, false);

        //
        boolean doSupportMapRTicket = false;
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && hBaseDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(hBaseDistribution.getHDVersion(hadoopVersion, false));
        }
        String useMaprTForHBaseString = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USE_MAPRTICKET);
        String maprTUsernameForHBase = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTUsernameForHBase)) {
            maprTUsernameForHBase = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
        }
        String maprTPasswordForHBase = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTPasswordForHBase)) {
            maprTPasswordForHBase = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
        }
        String maprTClusterForHBase = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTClusterForHBase)) {
            maprTClusterForHBase = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
        }
        String maprTDurationForHBase = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTDurationForHBase)) {
            maprTDurationForHBase = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
        }
        boolean checkMaprTForHBase = Boolean.valueOf(useMaprTForHBaseString);
        useMaprTForHBase.setSelection(checkMaprTForHBase);
        if (checkMaprTForHBase) {
            maprTUsernameForHBaseTxt.setText(StringUtils.trimToEmpty(maprTUsernameForHBase));
            maprTPasswordForHBaseTxt.setText(StringUtils.trimToEmpty(maprTPasswordForHBase));
            maprTClusterForHBaseTxt.setText(StringUtils.trimToEmpty(maprTClusterForHBase));
            maprTDurationForHBaseTxt.setText(maprTDurationForHBase == null ? "86400" : maprTDurationForHBase.trim()); //$NON-NLS-1$);
        }
        hideControl(useMaprTForHBase, !doSupportMapRTicket);
        hideControl(authenticationMaprTComForHBase, !(checkMaprTForHBase && doSupportMapRTicket));
        hideControl(authenticationUserPassComForHBase, useKrb && doSupportMapRTicket);
        authenticationGrpForHBase.layout();
        authenticationGrpForHBase.getParent().layout();
    }

    private void initMaprdbSettings() {
        DatabaseConnection connection = getConnection();
        String hadoopDistribution = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
        String hadoopVersion = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION);

        IHDistribution maprdbDistribution = getMaprdbDistribution(hadoopDistribution, false);

        if (maprdbDistribution != null) {
            String distributionDisplayName = maprdbDistribution.getDisplayName();
            maprdbDistributionCombo.setText(distributionDisplayName);
            updateMaprdbVersionPart(maprdbDistribution);
            IHDistributionVersion hdVersion = maprdbDistribution.getHDVersion(hadoopVersion, false);
            if (hdVersion != null && hdVersion.getDisplayVersion() != null) {
                maprdbVersionCombo.setText(hdVersion.getDisplayVersion());
            } else {
                maprdbVersionCombo.select(0);
            }
        } else {
            maprdbDistributionCombo.select(0);
            maprdbVersionCombo.select(0);
        }

        String useKrbString = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
        String useKeytabString = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB);
        String keytabPrincipal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        String keytab = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(keytabPrincipal)) {
            keytabPrincipal = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        }
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(keytab)) {
            keytab = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        }
        String masterPrincipal = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
        String regionServerPrincipal = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
        boolean useKrb = Boolean.valueOf(useKrbString);
        boolean useKeytab = Boolean.valueOf(useKeytabString);
        useKerberosForMaprdb.setSelection(useKrb);
        if (useKrb) {
            useKeyTabForMaprdb.setSelection(useKeytab);
            if (useKeytab) {
                principalForMaprdbTxt.setText(StringUtils.trimToEmpty(keytabPrincipal));
                keytabForMaprdbTxt.setText(StringUtils.trimToEmpty(keytab));
            }
            maprdbMasterPrincipalTxt.setText(StringUtils.trimToEmpty(masterPrincipal));
            maprdbRSPrincipalTxt.setText(StringUtils.trimToEmpty(regionServerPrincipal));
        }
        hideControl(keyTabCompoisteForMaprdb, !useKeytab);
        hideControl(authenticationComForMaprdb, !useKrb);
        hideControl(authenticationGrpForMaprdb, false);

        //
        boolean doSupportMapRTicket = false;
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && maprdbDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(maprdbDistribution.getHDVersion(hadoopVersion, false));
        }
        String useMaprTForMaprdbString = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USE_MAPRTICKET);
        String maprTUsernameForMaprdb = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTUsernameForMaprdb)) {
            maprTUsernameForMaprdb = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
        }
        String maprTPasswordForMaprdb = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTPasswordForMaprdb)) {
            maprTPasswordForMaprdb = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
        }
        String maprTClusterForMaprdb = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTClusterForMaprdb)) {
            maprTClusterForMaprdb = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
        }
        String maprTDurationForMaprdb = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTDurationForMaprdb)) {
            maprTDurationForMaprdb = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
        }
        boolean checkMaprTForMaprdb = Boolean.valueOf(useMaprTForMaprdbString);
        useMaprTForMaprdb.setSelection(checkMaprTForMaprdb);
        if (checkMaprTForMaprdb) {
            maprTUsernameForMaprdbTxt.setText(StringUtils.trimToEmpty(maprTUsernameForMaprdb));
            maprTPasswordForMaprdbTxt.setText(StringUtils.trimToEmpty(maprTPasswordForMaprdb));
            maprTClusterForMaprdbTxt.setText(StringUtils.trimToEmpty(maprTClusterForMaprdb));
            maprTDurationForMaprdbTxt.setText(maprTDurationForMaprdb == null ? "86400" : maprTDurationForMaprdb.trim()); //$NON-NLS-1$);
        }
        hideControl(useMaprTForMaprdb, !doSupportMapRTicket);
        hideControl(authenticationMaprTComForMaprdb, !(checkMaprTForMaprdb && doSupportMapRTicket));
        hideControl(authenticationUserPassComForMaprdb, useKrb && doSupportMapRTicket);
        authenticationGrpForMaprdb.layout();
        authenticationGrpForMaprdb.getParent().layout();
    }

    private IHDistribution getMaprdbDistribution(String maprdbDistribution, boolean byDisplay) {
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            return hadoopService.getMaprdbDistributionManager().getDistribution(maprdbDistribution, byDisplay);
        }
        return null;
    }

    private void initImpalaSettings() {
        DatabaseConnection connection = getConnection();
        String hadoopDistribution = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
        String hadoopVersion = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService == null) {
            return;
        }
        IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(hadoopDistribution,
                false);
        if (impalaDistribution != null) {
            String distributionDisplayName = impalaDistribution.getDisplayName();
            impalaDistributionCombo.setText(distributionDisplayName);
            updateImpalaVersionPart(impalaDistribution);

            IHDistributionVersion hdVersion = impalaDistribution.getHDVersion(hadoopVersion, false);
            if (hdVersion != null) {
                impalaVersionCombo.setText(hdVersion.getDisplayVersion());
            } else {
                impalaVersionCombo.select(0);
            }
        } else {
            impalaDistributionCombo.select(0);
        }

        authenticationGrpForImpala.setVisible(true);
        authenticationGrpForImpala.getParent().layout();
    }

    private void updateImpalaVersionPart(IHDistribution impalaDistribution) {
        if (impalaDistribution == null) {
            return;
        }
        boolean custom = impalaDistribution != null && impalaDistribution.useCustom();
        if (custom) {
            impalaVersionCombo.setHideWidgets(true);
            impalaCustomButton.setVisible(true);
        } else {
            impalaVersionCombo.setHideWidgets(false);
            impalaCustomButton.setVisible(false);
            if (impalaDistribution != null) {
                impalaVersionCombo.getCombo().setItems(impalaDistribution.getVersionsDisplay());
                impalaVersionCombo.getCombo().select(0);
            }
        }
    }

    private void updateHBaseVersionPart(IHDistribution hBaseDistribution) {
        if (hBaseDistribution != null && hBaseDistribution.useCustom()) {
            hbaseVersionCombo.setHideWidgets(true);
            hbaseCustomButton.setVisible(true);
        } else {
            hbaseVersionCombo.setHideWidgets(false);
            hbaseCustomButton.setVisible(false);
            String[] versionsDisplay = hBaseDistribution.getVersionsDisplay();
            hbaseVersionCombo.getCombo().setItems(versionsDisplay);
            if (versionsDisplay.length > 0) {
                IHDistributionVersion defaultVersion = hBaseDistribution.getDefaultVersion();
                if (defaultVersion != null && defaultVersion.getDisplayVersion() != null) {
                    hbaseVersionCombo.getCombo().setText(defaultVersion.getDisplayVersion());
                } else {
                    hbaseVersionCombo.getCombo().select(0);
                }
            }
        }
    }

    private void updateMaprdbVersionPart(IHDistribution maprdbDistribution) {
        if (maprdbDistribution != null && maprdbDistribution.useCustom()) {
            maprdbVersionCombo.setHideWidgets(true);
            maprdbCustomButton.setVisible(true);
        } else {
            maprdbVersionCombo.setHideWidgets(false);
            maprdbCustomButton.setVisible(false);
            String[] versionsDisplay = maprdbDistribution.getVersionsDisplay();
            maprdbVersionCombo.getCombo().setItems(versionsDisplay);
            if (versionsDisplay.length > 0) {
                IHDistributionVersion defaultVersion = maprdbDistribution.getDefaultVersion();
                if (defaultVersion != null && defaultVersion.getDisplayVersion() != null) {
                    maprdbVersionCombo.getCombo().setText(defaultVersion.getDisplayVersion());
                } else {
                    maprdbVersionCombo.getCombo().select(0);
                }
            }
        }
    }

    /**
     * 
     * Added by Marvin Wang on Mar 25, 2013.
     * 
     * @param parent
     */
    private void createHiveServerVersionField(Composite parent) {
        hiveServerVersionCombo = new LabelledCombo(parent, Messages.getString("DatabaseForm.hiveServer.version"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.hiveServer.version.tips"), //$NON-NLS-1$
                new String[] {}, 2, true);

        hiveServerVersionCombo.setHideWidgets(true);
    }

    /**
     * Added by Marvin Wang on Oct. 15, 2012.
     * 
     * @param parent
     */
    private void createHadoopVersionInfoForHiveEmbedded(Composite parent) {

        GridLayout layout2 = (GridLayout) parent.getLayout();
        hiveComposite = new Group(parent, SWT.NONE);
        hiveComposite.setText(Messages.getString("DatabaseForm.hiveEmbedded.versionInfo")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(layout2.numColumns, 1).applyTo(hiveComposite);

        hiveComposite.setLayout(new GridLayout(7, false));

        hiveDistributionCombo = new LabelledCombo(hiveComposite, Messages.getString("DatabaseForm.distribution.labelName"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.distribution.tips"), HiveMetadataHelper.getDistributionsDisplay(), 1, false);//$NON-NLS-1$

        hiveVersionCombo = new LabelledCombo(hiveComposite, Messages.getString("DatabaseForm.distroVersion.labelName"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.distroVersion.tips"), new String[] {}, 1, false);//$NON-NLS-1$

        hiveModeCombo = new LabelledCombo(hiveComposite, Messages.getString("DatabaseForm.hiveMode.labelName"), //$NON-NLS-1$
                Messages.getString("DatabaseForm.hiveMode.tips"), new String[] {}, 1, false);//$NON-NLS-1$

        hiveCustomButton = new Button(hiveComposite, SWT.NULL);
        hiveCustomButton.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        hiveCustomButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 3, 1));

        createYarnPart(hiveComposite);
    }

    private void createYarnPart(Composite parent) {
        yarnComp = new Composite(parent, SWT.NONE);
        GridLayout yarnLayout = new GridLayout();
        yarnLayout.marginWidth = 0;
        yarnLayout.marginHeight = 0;
        yarnComp.setLayout(yarnLayout);
        yarnComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 7, 1));

        useYarnButton = new Button(yarnComp, SWT.CHECK);
        useYarnButton.setText(Messages.getString("DatabaseForm.useYarn")); //$NON-NLS-1$
        useYarnButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));
    }

    /**
     * The contents only show up when the current db type is hive and hive mode is embedded. Contents include name node
     * url and job tracker url. Added by Marvin Wang on Oct. 15, 2012.
     * 
     * @param parent
     */
    private void createHadoopUIContentsForHiveEmbedded(Composite parent) {
        GridLayout layout2 = (GridLayout) parent.getLayout();
        hadoopPropGrp = new Group(parent, SWT.NONE);
        hadoopPropGrp.setText(Messages.getString("DatabaseForm.hiveEmbedded.hadoopInfo")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(layout2.numColumns, 1).align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .applyTo(hadoopPropGrp);

        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        hadoopPropGrp.setLayout(layout);

        nameNodeURLTxt = new LabelledText(hadoopPropGrp, Messages.getString("DatabaseForm.hiveEmbedded.nameNodeURL"), 1); //$NON-NLS-1$
        jobTrackerURLTxt = new LabelledText(hadoopPropGrp, Messages.getString("DatabaseForm.hiveEmbedded.jobTrackerURL"), 1); //$NON-NLS-1$

        setHideHadoopInfoWidgets(true);
    }

    /**
     * 
     * @param parent
     */
    private void createMetastoreUIContentsForHiveEmbedded(Composite parent) {
        GridLayout layout2 = (GridLayout) parent.getLayout();
        metastorePropGrp = new Group(parent, SWT.NULL);
        metastorePropGrp.setText(Messages.getString("DatabaseForm.hiveEmbedded.metastoreInfo"));//$NON-NLS-1$
        GridDataFactory.fillDefaults().span(layout2.numColumns, 1).minSize(getSize().x - 10, SWT.DEFAULT)
                .applyTo(metastorePropGrp);

        metastorePropGrp.setLayout(new GridLayout(3, false));
        metastoreConnURLTxt = new LabelledText(metastorePropGrp,
                Messages.getString("DatabaseForm.hiveEmbedded.metastore.connURL"), 2); //$NON-NLS-1$
        metastoreConnUserName = new LabelledText(metastorePropGrp,
                Messages.getString("DatabaseForm.hiveEmbedded.metastore.connUserName"), 2); //$NON-NLS-1$
        metastoreConnPassword = new LabelledText(metastorePropGrp,
                Messages.getString("DatabaseForm.hiveEmbedded.metastore.connPassword"), 2, //$NON-NLS-1$
                SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
        metastoreConnDriverJar = new LabelledText(metastorePropGrp,
                Messages.getString("DatabaseForm.hiveEmbedded.metastore.connDriverJar"), 1); //$NON-NLS-1$

        metastoreJarFilesButton = new Button(metastorePropGrp, SWT.NONE);
        metastoreJarFilesButton.setText("..."); //$NON-NLS-1$
        metastoreJarFilesButton.setToolTipText(Messages.getString("DatabaseForm.selectJar")); //$NON-NLS-1$
        metastoreConnDriverName = new LabelledCombo(metastorePropGrp,
                Messages.getString("DatabaseForm.hiveEmbedded.metastore.connDriverName"), "", null, 2, true, SWT.NONE); //$NON-NLS-1$//$NON-NLS-2$

        setHideMetastoreInfoWidgets(true);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getVersionDrivers".
     * 
     * DOC qli modify method "getVersionDrivers",just add a parameter "dbType".
     * 
     * @return
     */
    private List<String> getVersionDrivers(String dbType) {
        if (asMsSQLVersionEnable()) {
            return getMSSQLVersionDrivers(dbType);
        } else if (asSybaseVersionEnable()) {
            return getSybaseVersionDrivers(dbType);
        }
        List<String> result = new ArrayList<String>();
        List<EDatabaseVersion4Drivers> v4dList = EDatabaseVersion4Drivers.indexOfByDbType(dbType);
        for (EDatabaseVersion4Drivers v4d : v4dList) {
            if (v4d.getVersionDisplay() != null) {
                result.add(v4d.getVersionDisplay());
            }
        }
        return result;
    }

    private List<String> getMSSQLVersionDrivers(String dbType) {
        List<String> result = new ArrayList<String>();
        result.add(EDatabaseVersion4Drivers.MSSQL.getVersionDisplay());
        result.add(EDatabaseVersion4Drivers.MSSQL_PROP.getVersionDisplay());
        return result;
    }

    private List<String> getSybaseVersionDrivers(String dbType) {
        List<String> result = new ArrayList<String>();
        result.add(EDatabaseVersion4Drivers.SYBASEIQ_16.getVersionDisplay());
        result.add(EDatabaseVersion4Drivers.SYBASEASE.getVersionDisplay());

        return result;
    }

    private void addFieldsForGeneralDB(Composite parent) {

        generalDbCompositeParent = new Composite(parent, SWT.NULL);

        generalDbCompositeParent.setLayout(new GridLayout(3, false));

        GridLayout layout2 = (GridLayout) generalDbCompositeParent.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        generalJdbcUrlText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.url"), 2); //$NON-NLS-1$

        generalJdbcDriverjarText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.jarfile"), //$NON-NLS-1$
                1);

        browseJarFilesButton = new Button(generalDbCompositeParent, SWT.NONE);
        browseJarFilesButton.setText("..."); //$NON-NLS-1$
        browseJarFilesButton.setToolTipText(Messages.getString("DatabaseForm.selectJar")); //$NON-NLS-1$

        generalJdbcClassNameText = new LabelledCombo(generalDbCompositeParent,
                Messages.getString("DatabaseForm.general.classname"), "", null, 1, true, SWT.NONE); //$NON-NLS-1$ //$NON-NLS-2$

        browseClassButton = new Button(generalDbCompositeParent, SWT.NONE);
        browseClassButton.setText("..."); //$NON-NLS-1$
        browseClassButton.setToolTipText("Select class name"); //$NON-NLS-1$

        generalJdbcUserText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.user"), 2); //$NON-NLS-1$

        generalJdbcPasswordText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.password"), //$NON-NLS-1$
                2);
        generalJdbcPasswordText.getTextControl().setEchoChar('*'); // see
        // feature
        // 3629 hide
        // password
        jDBCschemaText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.schema"), 2); //$NON-NLS-1$
        generalMappingFileText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.mapping"), //$NON-NLS-1$
                1);

        generalMappingSelectButton = new Button(generalDbCompositeParent, SWT.NONE);
        generalMappingSelectButton.setText("..."); //$NON-NLS-1$
        generalMappingSelectButton.setToolTipText(Messages.getString("DatabaseForm.selectRule")); //$NON-NLS-1$

    }

    /**
     * Hidden one composite and display another one
     * <p>
     * DOC YeXiaowei Comment method "switchBetweenTypeandGeneralDB".
     * 
     * @param hiddenGeneral
     */
    private void switchBetweenTypeandGeneralDB(boolean hiddenGeneral) {

        GridData dataGeneralDb = null;
        GridData dataTypeDb = null;

        if (hiddenGeneral) {
            dataGeneralDb = new GridData(GridData.FILL_HORIZONTAL);
            dataGeneralDb.heightHint = 0;

            dataTypeDb = new GridData(GridData.FILL_BOTH);
        } else {
            dataGeneralDb = new GridData(GridData.FILL_BOTH);

            dataTypeDb = new GridData(GridData.FILL_HORIZONTAL);
            dataTypeDb.heightHint = 0;
        }

        generalDbCompositeParent.setLayoutData(dataGeneralDb);
        typeDbCompositeParent.setLayoutData(dataTypeDb);

        compositeGroupDbSettings.layout();
    }

    /**
     * DOC YeXiaowei Comment method "addCheckAndStandardButtons".
     * 
     * @param width
     * @param compositeGroupDbSettings
     */
    private void addCheckAndStandardButtons(int width, Composite compositeGroupDbSettings) {

        fileField.hide();
        directoryField.hide();

        Composite unionBtnsCompsite = new Composite(dbConnectionArea, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        unionBtnsCompsite.setLayout(formLayout);

        moveButton = new Button(unionBtnsCompsite, SWT.PUSH);
        moveButton.setText(DOWN);
        moveButton.setToolTipText(Messages.getString("DatabaseForm.hideContext")); //$NON-NLS-1$
        addMoveButtonListener();
        FormData moveButtonFormData = new FormData();
        moveButtonFormData.right = new FormAttachment(100, 0);
        moveButton.setLayoutData(moveButtonFormData);

        // Button Check
        Composite checkGroup = new Composite(unionBtnsCompsite, SWT.NONE);
        // align moveButton with checkGroup
        moveButtonFormData.top = new FormAttachment(checkGroup, 0, SWT.CENTER);
        FormData checkGroupFormData = new FormData();
        checkGroupFormData.left = new FormAttachment(0, 0);
        checkGroupFormData.right = new FormAttachment(moveButton, 0);
        checkGroup.setLayoutData(checkGroupFormData);
        GridLayout gridLayout = new GridLayout(1, false);
        checkGroup.setLayout(gridLayout);

        Composite compositeCheckButton = Form.startNewGridLayout(checkGroup, 1, false, SWT.RIGHT, SWT.BOTTOM);
        unionBtnsCompsite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        checkButton = new UtilsButton(compositeCheckButton, Messages.getString("DatabaseForm.test"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        checkButton.setEnabled(false);

        hiveModeCombo.setVisible(!CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected());
    }

    private void addMoveButtonListener() {
        // TODO Auto-generated method stub
        moveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (moveButton.getText().equals(DOWN)) {
                    sash.setWeights(new int[] { 33, 0 });
                    moveButton.setToolTipText(Messages.getString("DatabaseForm.showContext")); //$NON-NLS-1$
                    moveButton.setText(UP);
                } else if (moveButton.getText().equals(UP)) {
                    sash.setWeights(new int[] { 25, 8 });
                    moveButton.setToolTipText(Messages.getString("DatabaseForm.hideContext")); //$NON-NLS-1$
                    moveButton.setText(DOWN);
                }
            }
        });
    }

    private void checkConnection(final StringBuffer retProposedSchema) {
        if (isSqliteFileFieldInvalidate()) {
            return;
        }
        checkButton.setEnabled(false);
        if (connectionItem.getConnection() instanceof DatabaseConnection) {
            DatabaseConnection c = (DatabaseConnection) connectionItem.getConnection();
            if (!isContextMode()) {
                if (EDatabaseTypeName.ORACLEFORSID.getProduct().equals(c.getProductId())) {
                    schemaText.setText(schemaText.getText().toUpperCase());
                } else if (EDatabaseTypeName.NETEZZA.getProduct().equals(c.getProductId())
                        || MetadataFillFactory.isJdbcNetezza(c.getDatabaseType(), c.getDriverClass())) {
                    if (sidOrDatabaseText != null) {
                        sidOrDatabaseText.setText(sidOrDatabaseText.getText().toUpperCase());
                    }
                    if (urlConnectionStringText != null) {
                        urlConnectionStringText.setText(getUppercaseNetezzaUrl(urlConnectionStringText.getText()));
                    }
                    if (generalJdbcUrlText != null) {
                        generalJdbcUrlText.setText(getUppercaseNetezzaUrl(generalJdbcUrlText.getText()));
                    }
                }
            }
        }
        final ManagerConnection managerConnection = new ManagerConnection();
        StringBuffer sgb = new StringBuffer();
        if (isContextMode()) { // context mode
            String connectionTypeName = connectionItem.getConnection().getConnectionTypeName();
            if (connectionTypeName.equals(EDatabaseConnTemplate.HBASE.getDBDisplayName())
                    || connectionTypeName.equals(EDatabaseConnTemplate.HIVE.getDBDisplayName())) {
                selectedContextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            } else {
                selectedContextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection());
            }
            String urlStr = null;
            urlStr = DBConnectionContextUtils.setManagerConnectionValues(managerConnection, connectionItem, selectedContextType,
                    getConnectionDBType());
            if (isImpalaDBConnSelected()) {
                String contextName = getConnection().getContextName();
                DatabaseConnection cloneDBConn = null;
                if (contextName == null) {
                    cloneDBConn = DBConnectionContextUtils.cloneOriginalValueConnection(getConnection(), true, null);
                }
                cloneDBConn = DBConnectionContextUtils.cloneOriginalValueConnection(getConnection(), false, contextName);
                urlStr = cloneDBConn.getURL();
                managerConnection.setUrlConnectionString(urlStr);
                String versionStr = ""; //$NON-NLS-1$
                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService != null) {
                    IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(
                            impalaDistributionCombo.getText(), true);
                    if (impalaDistribution != null && !impalaDistribution.useCustom()) {
                        IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(impalaVersionCombo.getText(), true);
                        if (impalaVersion != null) {
                            versionStr = impalaVersion.getVersion();
                        }
                    }
                }
                managerConnection.setDbVersionString(versionStr);
            }
            if (urlStr == null || isHiveDBConnSelected()) {
                if (getConnectionDBType().equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
                    DatabaseConnection dbConn = (DatabaseConnection) connectionItem.getConnection();

                    ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn);
                    urlStr = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getURL());
                } else {
                    urlStr = getStringConnection();
                }
            }
            urlConnectionStringText.setText(urlStr);
        } else {
            String versionStr = dbVersionCombo.getText();
            if (isHiveDBConnSelected()) {
                HiveModeInfo hiveMode = HiveModeInfo.getByDisplay(hiveModeCombo.getText());
                versionStr = hiveMode.getName();
            } else if (isImpalaDBConnSelected()) {
                urlConnectionStringText.setText(getStringConnection());
                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService != null) {
                    IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(
                            impalaDistributionCombo.getText(), true);
                    if (impalaDistribution != null && !impalaDistribution.useCustom()) {
                        IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(impalaVersionCombo.getText(), true);
                        if (impalaVersion != null) {
                            versionStr = impalaVersion.getVersion();

                        }
                    }
                }
            } else {
                if (isOracleCustomDBConnSelected()) {
                    if (useSSLEncryption.getSelection()) {
                        String additionParamStr = additionParamText.getText();
                        Properties info = new Properties();
                        if (StringUtils.isNotEmpty(additionParamStr)) {
                            try {
                                String additionals = additionParamStr.replaceAll("&", "\n");//$NON-NLS-1$//$NON-NLS-2$
                                info.load(new java.io.ByteArrayInputStream(additionals.getBytes()));
                            } catch (IOException e) {
                                // Do nothing
                            }
                        }
                        ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.TRUSTSTORE_TYPE,
                                SSLPreferenceConstants.KEYSTORE_TYPES[2]);
                        ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.TRUSTSTORE_FILE,
                                trustStorePath.getText());
                        ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.TRUSTSTORE_PASSWORD,
                                trustStorePassword.getText());
                        if (needClientAuth.getSelection()) {
                            ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.KEYSTORE_TYPE,
                                    SSLPreferenceConstants.KEYSTORE_TYPES[2]);
                            ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.KEYSTORE_FILE,
                                    keyStorePath.getText());
                            ConvertionHelper.updateAdditionParam(sgb, info, SSLPreferenceConstants.KEYSTORE_PASSWORD,
                                    keyStorePassword.getText());
                        }
                    }
                }
                EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(versionStr);
                if (version != null) {
                    versionStr = version.getVersionValue();
                }
            }

            // TODO If the current DB type is Hive, then to identify if Hive mode is EMBEDDED, if so, then setup some
            // system
            // properties.
            // doHivePreSetup();
            // set the value
            managerConnection.setValue(0, getConnectionDBType(), isGeneralJDBC() ? generalJdbcUrlText.getText()
                    : urlConnectionStringText.getText(), serverText.getText(), isGeneralJDBC() ? generalJdbcUserText.getText()
                    : usernameText.getText(), isGeneralJDBC() ? generalJdbcPasswordText.getText() : passwordText.getText(),
                    sidOrDatabaseText.getText(), portText.getText(), fileField.getText(), datasourceText.getText(),
                    isGeneralJDBC() ? jDBCschemaText.getText() : schemaText.getText(),
                    additionParamText.getText() + sgb.toString(),
                    generalJdbcClassNameText.getText(), generalJdbcDriverjarText.getText(),
                    enableDbVersion() ? versionStr : null, metadataconnection.getOtherParameters());

            managerConnection.setDbRootPath(directoryField.getText());

        }
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        managerConnection.setValueProperties(store.getString(IDatabasePrefConstants.SQL_SYNTAX),
                store.getString(IDatabasePrefConstants.STRING_QUOTE), store.getString(IDatabasePrefConstants.NULL_CHAR));

        EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(getConnectionDBType());
        AProgressMonitorDialogWithCancel<Boolean> checkingDialog;
        if (dbType.isUseProvider()) {
            final IMetadataConnection metadataConn = ConvertionHelper.convert(connectionItem.getConnection(), true);
            checkingDialog = new AProgressMonitorDialogWithCancel<Boolean>(getShell()) {

                @Override
                protected Boolean runWithCancel(IProgressMonitor monitor) throws Throwable {
                    return managerConnection.check(metadataConn, retProposedSchema);
                }
            };
        } else if (isHiveDBConnSelected()) {
            final IMetadataConnection metadataConn = ConvertionHelper.convert(connectionItem.getConnection(), true);
            if (isHiveEmbeddedMode()) {
                doHivePreSetup((DatabaseConnection) metadataConn.getCurrentConnection());
            }
            checkingDialog = new AProgressMonitorDialogWithCancel<Boolean>(getShell()) {

                @Override
                protected Boolean runWithCancel(IProgressMonitor monitor) throws Throwable {
                    return managerConnection.checkHiveConnection(metadataConn);
                }
            };
        } else if (isImpalaDBConnSelected()) {
            final IMetadataConnection metadataConn = ConvertionHelper.convert(connectionItem.getConnection(), true);
            checkingDialog = new AProgressMonitorDialogWithCancel<Boolean>(getShell()) {

                @Override
                protected Boolean runWithCancel(IProgressMonitor monitor) throws Throwable {
                    return managerConnection.checkImpalaConnection(metadataConn);
                }
            };
        } else {
            // check the connection
            checkingDialog = new AProgressMonitorDialogWithCancel<Boolean>(getShell()) {

                @Override
                protected Boolean runWithCancel(IProgressMonitor monitor) throws Throwable {
                    return managerConnection.check(retProposedSchema);
                }
            };
        }

        String executeMessage = Messages.getString("DatabaseForm.checkConnection.executeMessage"); //$NON-NLS-1$
        Throwable executeException = null;
        try {
            checkingDialog.run(executeMessage, null, true, AProgressMonitorDialogWithCancel.ENDLESS_WAIT_TIME);
        } catch (Exception e) {
            executeException = e;
        }

        // update the button
        checkButton.setEnabled(true);
        if (checkingDialog.isUserCanncelled()) {
            return;
        }
        if (checkingDialog.getExecuteException() != null) {
            executeException = checkingDialog.getExecuteException();
        }
        if (executeException != null) {
            if (executeException instanceof InterruptedException) {
                CommonExceptionHandler.process(executeException, Priority.WARN);
                return;
            }
            databaseSettingIsValide = false;
        } else {
            databaseSettingIsValide = checkingDialog.getExecuteResult();
        }

        // show the result of check connection
        if (databaseSettingIsValide) {
            if (!isReadOnly()) {
                updateStatus(IStatus.OK, null);
            }
            MessageDialog.openInformation(getShell(), Messages.getString("DatabaseForm.checkConnectionTitle"), "\"" //$NON-NLS-1$ //$NON-NLS-2$
                    + connectionItem.getProperty().getDisplayName() + "\" " + Messages.getString("DatabaseForm.checkIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
            if (!isReadOnly()) {
                if (isContextMode()) {
                    adaptFormToEditable();
                } else {
                    setPropertiesFormEditable(true);
                }
            }
            String msg = checkDBVersion();
            if (msg != null) {
                updateStatus(IStatus.WARNING, msg);
            }
        } else {
            String mainMsg = null;
            Exception exception = managerConnection.getException();
            if (exception instanceof WarningSQLException) {
                mainMsg = exception.getMessage();
                MessageDialog.openWarning(getShell(), Messages.getString("DatabaseForm.warningTitle"), mainMsg); //$NON-NLS-1$
            } else if (exception == null && executeException != null) {
                mainMsg = executeException.getMessage();
                MessageDialog.openWarning(getShell(), Messages.getString("DatabaseForm.warningTitle"), mainMsg); //$NON-NLS-1$
            } else {
                mainMsg = Messages.getString("DatabaseForm.checkFailure") + " " //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("DatabaseForm.checkFailureTip"); //$NON-NLS-1$
                new ErrorDialogWidthDetailArea(getShell(), PID, mainMsg, managerConnection.getMessageException());
            }
            if (!isReadOnly()) {
                updateStatus(IStatus.WARNING, mainMsg);
            }
        }
    }

    private String getUppercaseNetezzaUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        String uppcaseUrl = url;
        int lastIndexOf = StringUtils.lastIndexOf(url, "/"); //$NON-NLS-1$
        if (lastIndexOf > 0 && lastIndexOf < url.length() - 1) {
            String part1 = StringUtils.substring(url, 0, lastIndexOf + 1);
            String part2 = StringUtils.substring(url, lastIndexOf + 1);
            if (!StringUtils.isEmpty(part2)) {
                int indexOf = StringUtils.indexOf(part2, "?"); //$NON-NLS-1$
                if (indexOf > -1) {
                    String sid = StringUtils.substring(part2, 0, indexOf);
                    part2 = StringUtils.upperCase(sid) + StringUtils.substring(part2, indexOf, part2.length());
                } else {
                    part2 = StringUtils.upperCase(part2);
                }
                uppcaseUrl = part1 + part2;
            }
        }
        return uppcaseUrl;
    }

    /**
     * DOC zshen check whether the file is exist
     */
    private boolean isSqliteFileFieldInvalidate() {
        String fileFullPath = null;
        String urlText = null;
        if (isGeneralJDBCSqlite()) {
            urlText = generalJdbcUrlText.getText();
        } else if (isSqlite()) {
            urlText = DBConnectionContextUtils.getUrlConnectionString(connectionItem, false);
        } else {
            return false;
        }

        String[] analyseURL = DatabaseConnStrUtil.analyseURL(getConnectionDBType(), "", urlText); //$NON-NLS-1$
        if (analyseURL[1] != null && analyseURL.length > 1) {
            fileFullPath = getSqliteFileFullPath(analyseURL[0], analyseURL[1]);
        }

        if (fileFullPath != null) {

            File file = new File(fileFullPath);
            if (file.exists()) {
                return false;
            }

            MessageDialog.openWarning(getShell(), Messages.getString("SelectDatabaseJarDialog.warningTitle"), //$NON-NLS-1$
                    Messages.getString("DatabaseForm.checkFileExist", fileFullPath)); //$NON-NLS-1$
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isGeneralJDBCSqlite".
     * 
     * @return
     */
    private boolean isGeneralJDBCSqlite() {
        if (getConnectionDBType().equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
            String urlText = generalJdbcUrlText.getText();
            if (urlText.startsWith("jdbc:sqlite:")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isSqlite".
     * 
     * @return
     */
    private boolean isSqlite() {
        return getConnectionDBType().equals(EDatabaseConnTemplate.SQLITE.getDBDisplayName());
    }

    /**
     * DOC zshen Comment method "getFileFullPath".
     * 
     * @param urlText
     * @return
     */
    private String getSqliteFileFullPath(String dbType, String fileName) {
        if (dbType.equalsIgnoreCase(EDatabaseConnTemplate.SQLITE.getDBDisplayName())) {
            return fileName;
        }
        return null;
    }

    /**
     * If the current db type is also hive, return <code>true</code>. Changed by Marvin Wang on Oct 17, 2012.
     * 
     * @return
     */
    private boolean enableDbVersion() {
        return oracleVersionEnable() || as400VersionEnable()
                || EDatabaseConnTemplate.ACCESS.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.MYSQL.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.PLUSPSQL.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.VERTICA.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.PSQL.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.MSSQL.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.SYBASEASE.getDBDisplayName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.IMPALA.getDBDisplayName().equals(getConnectionDBType());
    }

    /**
     * add StringConnection Controls.
     */
    private void addStringConnectionControls() {
        // event FocusIn
        urlConnectionStringText.addListener(SWT.FocusIn, new Listener() {

            @Override
            public void handleEvent(final Event e) {
                if (!isContextMode()) {
                    if (getConnectionDBType().length() > 0) {
                        setPropertiesFormEditable(false);
                        urlConnectionStringText.setEditable(true);
                    } else {
                        updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.alert", "DB Type")); //$NON-NLS-1$
                    }
                }
            }
        });

        urlConnectionStringText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (urlConnectionStringText.getEditable()) {
                        if (!databaseSettingIsValide) {
                            updateStatus(IStatus.INFO, Messages.getString("DatabaseForm.checkInformation")); //$NON-NLS-1$
                        }
                        databaseSettingIsValide = false;
                        checkButton.setEnabled(true);
                        String[] s = DatabaseConnStrUtil.analyseURL(getConnection().getDatabaseType(), getConnection()
                                .getDbVersionString(), urlConnectionStringText.getText());
                        // if the ConnectionString write manually don't
                        // correspond width selectedIndex of combo DbType
                        // we search if another regex corresponding at this
                        // string
                        String selection = s[0];

                        int index = 1;
                        if (s[index] != "") {//$NON-NLS-1$
                            if (selection.equals(EDatabaseConnTemplate.GODBC.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.MSODBC.getDBDisplayName())) {
                                datasourceText.setText(s[index]);
                                getConnection().setDatasourceName(s[index]);
                                getConnection().setURL(getStringConnection());
                            } else if (selection.equals(EDatabaseConnTemplate.SQLITE.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())) {
                                fileField.setText(s[index]);
                                getConnection().setFileFieldName(s[index]);
                            } else if (selection.equals(EDatabaseConnTemplate.JAVADB_EMBEDED.getDBDisplayName())) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                                getConnection().setURL(getStringConnection());
                            } else if (selection.equals(EDatabaseConnTemplate.HSQLDB_IN_PROGRESS.getDBDisplayName())) {
                                directoryField.setText(s[index]);
                                getConnection().setDBRootPath(s[index]);
                            } else {
                                serverText.setText(s[index]);
                                getConnection().setServerName(s[index]);
                            }
                        }

                        index = 2;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection.equals(EDatabaseConnTemplate.INTERBASE.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.TERADATA.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.AS400.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.HSQLDB_IN_PROGRESS.getDBDisplayName())) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                            } else if (selection.equals(EDatabaseConnTemplate.FIREBIRD.getDBDisplayName())) {
                                fileField.setText(s[index]);
                                getConnection().setFileFieldName(s[index]);
                            } else {
                                portText.setText(s[index]);
                                getConnection().setPort(s[index]);
                            }
                        }

                        index = 3;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection.equals(EDatabaseConnTemplate.AS400.getDBDisplayName())
                                    || selection.equals(EDatabaseConnTemplate.REDSHIFT.getDBDisplayName())) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                            }
                        }

                        index = 4;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection.equals(EDatabaseConnTemplate.INFORMIX.getDBDisplayName())) {
                                datasourceText.setText(s[index]);
                                getConnection().setDatasourceName(s[index]);
                            } else {
                                additionParamText.setText(s[index]);
                                getConnection().setAdditionalParams(s[index]);
                            }
                        }

                        index = 5;
                        if (s[index] != "") { //$NON-NLS-1$
                            additionParamText.setText(s[index]);
                            getConnection().setAdditionalParams(s[index]);
                        }
                    }
                }
            }
        });

    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event CheckButton
        checkButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                // ClassLoader currentContextCL = Thread.currentThread().getContextClassLoader();
                StringBuffer retProposedSchema = new StringBuffer();
                checkConnection(retProposedSchema);
                if (0 < retProposedSchema.length()) {
                    if (ManagerConnection.isSchemaFromSidOrDatabase(EDatabaseTypeName.getTypeFromDbType(getConnectionDBType()))) {
                        if (sidOrDatabaseText != null) {
                            sidOrDatabaseText.setText(retProposedSchema.toString());
                        }
                    } else if (schemaText != null) {
                        schemaText.setText(retProposedSchema.toString());
                    }
                }
                if (isHiveDBConnSelected()) {
                    if (isHiveEmbeddedMode()) {
                        // Thread.currentThread().setContextClassLoader(currentContextCL);
                        try {
                            forceSetFlagForHiveCreateDefaultDB();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        // Thread.currentThread().setContextClassLoader(currentContextCL);
                        doRemoveHiveSetup();
                    }
                }
            }
        });
    }

    /**
     * Makes the flag of createDefaultDB <code>false</code> each time when a checking is complete. Added by Marvin Wang
     * on Oct 19, 2012.
     * 
     * @throws ClassNotFoundException
     */
    private void forceSetFlagForHiveCreateDefaultDB() throws ClassNotFoundException {
        // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // ReflectionUtils.setStaticFieldValue("org.apache.hadoop.hive.metastore.HiveMetaStore$HMSHandler", classLoader,
        // //$NON-NLS-1$
        // "createDefaultDB", false); //$NON-NLS-1$
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // common Listener to force the choice of dbCombo
        Listener listener = new Listener() {

            @Override
            public void handleEvent(final Event e) {
                if (isContextMode()) {
                    //
                } else {
                    setPropertiesFormEditable(getConnectionDBType().length() > 0);
                    urlConnectionStringText.setEditable(false);
                }
            }
        };

        usernameText.addListener(SWT.FocusIn, listener);
        passwordText.addListener(SWT.FocusIn, listener);
        serverText.addListener(SWT.FocusIn, listener);
        portText.addListener(SWT.FocusIn, listener);
        sidOrDatabaseText.addListener(SWT.FocusIn, listener);
        datasourceText.addListener(SWT.FocusIn, listener);
        schemaText.addListener(SWT.FocusIn, listener);
        additionParamText.addListener(SWT.FocusIn, listener);
        urlConnectionStringText.addListener(SWT.FocusIn, listener);
        mappingFileText.addListener(SWT.FocusIn, listener);

        // serverText : Event modifyText
        serverText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setServerName(serverText.getText());
                        modifyFieldValue();
                    }
                }
            }
        });

        // portText : Event modifyText
        portText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setPort(portText.getText());
                        modifyFieldValue();
                    }
                    // Check port
                    boolean b = true;
                    String databaseType = getConnection().getDatabaseType();
                    if (databaseType != null) {
                        if (databaseType.equals("Ingres")) { //$NON-NLS-1$
                            b = Pattern.matches(Messages.getString("DatabaseForm.ingresDBRegex"), portText.getText()); //$NON-NLS-1$
                        } else {
                            b = Pattern.matches(Messages.getString("DatabaseForm.otherDBRegex"), portText.getText()); //$NON-NLS-1$
                        }
                    }
                    if (b) {
                        b = portText.getText().length() <= 5;
                    }
                    evaluateTextField(b);
                }
            }
        });

        // portText : Event Key
        portText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!Character.isLetterOrDigit(e.character) && !Character.isIdentifierIgnorable(e.character)) {
                    e.doit = false;
                }
            }
        });

        // usernameText : Event modifyText
        usernameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setUsername(usernameText.getText());
                        getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USERNAME, usernameText.getText());
                    }
                }
            }
        });

        // passwordText : Event modifyText
        passwordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setRawPassword(passwordText.getText());
                    }
                }
            }
        });

        // sidOrDatabaseText : Event modifyText
        sidOrDatabaseText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setSID(sidOrDatabaseText.getText());
                        modifyFieldValue();
                    }
                }
            }
        });

        // datasourceText : Event modifyText
        datasourceText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setDatasourceName(datasourceText.getText());
                        modifyFieldValue();
                    }
                }
            }
        });

        // schemaText : Event modifyText
        schemaText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setUiSchema(schemaText.getText());
                        modifyFieldValue();
                    }
                }
            }

        });
        // MOD yyin 20121203 TDQ-6485: when the url or schema changed, will need reload db. if neither of them changed,
        // doNOT popup the reload dialog(no need to reload)
        // check setURL only when the schema is changed.
        schemaText.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!isContextMode()) {
                    if (originalUischema != null) {
                        if (!originalUischema.equalsIgnoreCase(schemaText.getText())) {
                            ConnectionHelper.setIsConnNeedReload(getConnection(), Boolean.TRUE);
                        } else {
                            checkURLIsChanged();
                        }
                    }
                }
            }
        });

        // Db version
        dbVersionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(dbVersionCombo.getText());
                    if (version != null) {
                        getConnection().setDbVersionString(version.getVersionValue());
                    }
                    urlConnectionStringText.setText(getStringConnection());
                    checkFieldsValue();
                }
                if (EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(getConnectionDBType())) {
                    initOracleCustomEncryptionInfo();
                }
            }
        });
        hideDbVersion();

        // additional parameters: Event modifyText
        additionParamText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setAdditionalParams(additionParamText.getText());
                        modifyFieldValue();
                    }
                }
            }
        });

        additionalJDBCSettingsText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS,
                            additionalJDBCSettingsText.getText());
                    urlConnectionStringText.setText(getStringConnection());
                }
            }
        });
        // // button1 parameter:Event modifyText
        // button1.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // if (!isContextMode()) {
        // getConnection().setSQLMode(button1.getSelection());
        // }
        // }
        // });
        // // button2 parameter:Event modifyText
        // button2.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // if (!isContextMode()) {
        // getConnection().setSQLMode(!button2.getSelection());
        // }
        // }
        // });
        // Event dbTypeCombo

        // removed for bug TDI-14797 on 26 July, 2013. for support search by keyboard's letter.
        // When the DbType is selected, disabled the action of keyboard's letter
        // to modify the combo
        // utils when the user use the keyboard to write the connection string
        // dbTypeCombo.addKeyListener(new KeyAdapter() {
        //
        // @Override
        // public void keyPressed(KeyEvent e) {
        // if (dbTypeCombo.getSelectionIndex() > -1) {
        // if (Character.isLetterOrDigit(e.character)) {
        // e.doit = false;
        // }
        // }
        // }
        // });

        // Event fileField
        fileField.addListener(SWT.FocusIn, new Listener() {

            // Event FocusIn
            @Override
            public void handleEvent(final Event e) {
                if (!isContextMode()) {
                    if (getConnectionDBType().length() > 0) {
                        // dbTypeCombo.forceFocus();
                        // } else {
                        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
                        EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(getConnection()
                                .getDbVersionString());
                        if (template != null
                                && template.getUrlTemplate(version).contains(EDatabaseConnVar.FILENAME.getVariable())) {
                            setPropertiesFormEditable(true);
                            urlConnectionStringText.setEditable(false);
                        }
                        getConnection().setFileFieldName(PathUtils.getPortablePath(fileField.getText()));
                    }
                }
            }
        });
        // Event Modify
        fileField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setFileFieldName(fileField.getText());
                        modifyFieldValue();
                    }
                }
            }
        });

        directoryField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        String text = directoryField.getText();
                        getConnection().setDBRootPath(text);
                        checkFieldsValue();
                    }
                }
            }
        });

        addGeneralDbFieldsListeners();

        addHBaseSettingFieldsListeners();

        addMaprdbSettingFieldsListeners();

        addImpalaSettingFieldsListeners();

        // Registers listeners for the combos of Hive, including distribution, hive mode and hive version.
        // regHiveCombosListener();

        // Registers all listeners of hive widgets.
        regHiveRelatedWidgetsListeners();

        if (canLinkToHadoopCluster()) {
            addHadoopClusterLinkListeners();
        }
    }

    public void refreshDBForm(ConnectionItem connItem) {
        if (connItem != null) {
            this.connectionItem = connItem;
        }
        getConnection().getParameters().clear();
        getConnection().setDbVersionString(null);
        resetControls();
        urlConnectionStringText.setEditable(false);
        authenticationGrpForImpala.setVisible(false);
        authenticationGrpForImpala.getParent().layout();
        // add for bug 12649
        clearFiledsForDiffDbTypes();
        boolean isGeneralJDBC = isGeneralJDBC();
        if (isGeneralJDBC) {
            if (generalJdbcUrlText != null) {
                generalJdbcUrlText.setText(""); //$NON-NLS-1$
            }
            if (generalJdbcUserText != null) {
                generalJdbcUserText.setText(""); //$NON-NLS-1$
            }
            if (generalJdbcPasswordText != null) {
                generalJdbcPasswordText.setText(""); //$NON-NLS-1$
            }

        } else if (isHiveDBConnSelected()) {
            // Added by Marvin Wang on Oct. 15, 2012 for bug TDI-23235.
            if (urlConnectionStringText != null) {
                urlConnectionStringText.setText(""); //$NON-NLS-1$
            }
            if (usernameText != null) {

                usernameText.setText(""); //$NON-NLS-1$
            }
            if (passwordText != null) {
                passwordText.setText(""); //$NON-NLS-1$
            }
            hideControl(authenticationGrpForHBase, true);
            initHiveInfo();
        } else if (isDBTypeSelected(EDatabaseConnTemplate.HBASE)) {
            hideControl(authenticationCom, true);
            hideControl(znodeparentGrp, false);
            hideControl(tableInfoPartOfHbaseComp, !doSupportMaprTicketForHbase());
            initHBaseSettings();
            // initZnodeParent();
        } else if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)) {
            hideControl(authenticationCom, true);
            hideControl(znodeparentGrp, false);
            hideControl(tableInfoPartOfMapRDBComp, false);
            initMaprdbSettings();
        } else if (isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
            hideControl(authenticationCom, true);
            hideControl(authenticationComForHBase, true);
            initImpalaSettings();
            getConnection().setDbVersionString("");
        } else {
            if (urlConnectionStringText != null) {
                urlConnectionStringText.setText(""); //$NON-NLS-1$
            }
            if (usernameText != null) {

                usernameText.setText(""); //$NON-NLS-1$
            }
            if (passwordText != null) {
                passwordText.setText(""); //$NON-NLS-1$
            }
        }
        if (schemaText != null) {
            schemaText.setText(""); //$NON-NLS-1$
        }
        if (serverText != null) {
            serverText.setText(""); //$NON-NLS-1$
        }
        if (sidOrDatabaseText != null) {
            sidOrDatabaseText.setText(""); //$NON-NLS-1$
        }
        if (portText != null && portText.getText() != null) {
            portText.setText(""); //$NON-NLS-1$
        }
        if (fileField != null) {
            fileField.setText(""); //$NON-NLS-1$
        }
        if (datasourceText != null) {
            datasourceText.setText(""); //$NON-NLS-1$
        }
        if (additionParamText != null) {
            additionParamText.setText(""); //$NON-NLS-1$
        }
        if (generalJdbcClassNameText != null) {
            generalJdbcClassNameText.setText(""); //$NON-NLS-1$
        }
        if (generalJdbcDriverjarText != null) {
            generalJdbcDriverjarText.setText(""); //$NON-NLS-1$
        }
        if (additionParamText != null) {
            additionParamText.setText(""); //$NON-NLS-1$
        }
        if (mappingFileText != null) {
            mappingFileText.setText(""); //$NON-NLS-1$
        }

        boolean hiddenGeneral = !isGeneralJDBC();

        // change controls
        switchBetweenTypeandGeneralDB(!isGeneralJDBC());

        if (!isContextMode()) {
            getConnection().setDatabaseType(getConnectionDBType());

            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            if (template != null) {
                portText.setText(template.getDefaultPort());
            }
            final String product = EDatabaseTypeName.getTypeFromDisplayName(getConnection().getDatabaseType()).getProduct();
            getConnection().setProductId(product);

            String mapping = null;

            if (product == null || product.equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
                mapping = generalMappingFileText.getText();
            } else {
                if (MetadataTalendType.getDefaultDbmsFromProduct(product) != null) {
                    mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
                }
            }
            if (mapping == null) {
                mapping = "mysql_id"; // default value //$NON-NLS-1$
            }
            getConnection().setDbmsId(mapping);

            setPropertiesFormEditable(true);

            additionParamText.setText(EDatabaseConnTemplate.getAdditionProperty(getConnectionDBType()));
            if (getConnectionDBType().equals(EDatabaseConnTemplate.INFORMIX.getDBDisplayName())) {
                datasourceText.setLabelText(Messages.getString("DatabaseForm.informixInstance")); //$NON-NLS-1$
            }
            checkAndSetIniSQLModel();
            checkAS400SpecificCase();
            checkDatabaseProperties();
            checkFieldsValue();
            hideDbVersion();
            // see bug 0005237: Create DB Connection issue.
            if (!schemaText.getEditable()) {
                schemaText.setText(""); //$NON-NLS-1$
            }
            if (isHiveDBConnSelected()) {
                doHiveDBTypeSelected();
                fillDefaultsWhenHiveVersionChanged();
                fillDefaultsWhenHiveModeChanged(isHiveEmbeddedMode());
            } else {
                doHiveDBTypeNotSelected();
            }

            if (isHBaseDBConnSelected()) {
                fillDefaultsWhenHBaseVersionChanged();
            }
            if (isImpalaDBConnSelected()) {
                fillDefaultsWhenImpalaVersionChanged();
            }
        }

        // Added by Marvin Wang on Oct. 22, 2012 just for show the scrolled bar when a hive DB type is selected.
        // It is not the better way to do this, if the code of DB part is required to refactor, this code could
        // be removed.
        if (isHiveDBConnSelected()) {
            scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        } else if (isImpalaDBConnSelected()) {
            scrolledComposite.setMinSize(newParent.computeSize(SWT.DEFAULT, 550));
        } else {
            checkScrolledCompositeSize();
        }

        if (!isDBTypeSelected(EDatabaseConnTemplate.HBASE) && !isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)
                && !isDBTypeSelected(EDatabaseConnTemplate.HIVE) && !isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
            clearHadoopRelatedParameters();
        }

    }

    private void resetControls() {
        hideControl(authenticationGrpForHBase, true);
        hideControl(authenticationGrpForMaprdb, true);
        hideControl(znodeparentGrp, true);
        hideControl(tableInfoPartOfHbaseComp, true);
        hideControl(tableInfoPartOfMapRDBComp, true);
    }

    private void clearFiledsForDiffDbTypes() {
        if (isGeneralJDBC()) {
            if (generalJdbcUrlText != null) {
                generalJdbcUrlText.setText(""); //$NON-NLS-1$
            }
            if (generalJdbcUserText != null) {
                generalJdbcUserText.setText(""); //$NON-NLS-1$
            }
            if (generalJdbcPasswordText != null) {
                generalJdbcPasswordText.setText(""); //$NON-NLS-1$
            }

        } else if (isHiveDBConnSelected()) {
            // Added by Marvin Wang on Oct. 15, 2012 for bug TDI-23235.
            if (urlConnectionStringText != null) {
                urlConnectionStringText.setText(""); //$NON-NLS-1$
            }
            if (usernameText != null) {

                usernameText.setText(""); //$NON-NLS-1$
            }
            if (passwordText != null) {
                passwordText.setText(""); //$NON-NLS-1$
            }
            initHiveInfo();
        } else if (isDBTypeSelected(EDatabaseConnTemplate.HBASE)) {
            initHBaseSettings();
            initZnodeParent();
        } else if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB)) {
            initMaprdbSettings();
            // initZnodeParent();
        } else {
            if (urlConnectionStringText != null) {
                urlConnectionStringText.setText(""); //$NON-NLS-1$
            }
            if (usernameText != null) {

                usernameText.setText(""); //$NON-NLS-1$
            }
            if (passwordText != null) {
                passwordText.setText(""); //$NON-NLS-1$
            }
        }
        if (schemaText != null) {
            schemaText.setText(""); //$NON-NLS-1$
        }
        if (serverText != null) {
            serverText.setText(""); //$NON-NLS-1$
        }
        if (sidOrDatabaseText != null) {
            sidOrDatabaseText.setText(""); //$NON-NLS-1$
        }
        if (portText != null && portText.getText() != null) {
            portText.setText(""); //$NON-NLS-1$
        }
        if (fileField != null) {
            fileField.setText(""); //$NON-NLS-1$
        }
        if (datasourceText != null) {
            datasourceText.setText(""); //$NON-NLS-1$
        }
        if (additionParamText != null) {
            additionParamText.setText(""); //$NON-NLS-1$
        }
        if (generalJdbcClassNameText != null) {
            generalJdbcClassNameText.setText(""); //$NON-NLS-1$
        }
        if (generalJdbcDriverjarText != null) {
            generalJdbcDriverjarText.setText(""); //$NON-NLS-1$
        }
        if (additionParamText != null) {
            additionParamText.setText(""); //$NON-NLS-1$
        }
        if (mappingFileText != null) {
            mappingFileText.setText(""); //$NON-NLS-1$
        }
    }

    private void checkScrolledCompositeSize() {
        // TODO Auto-generated method stub
        int i = 0;
        if (urlConnectionStringText.isVisiable()) {
            i = i + 1;
        }
        if (usernameText.isVisiable()) {
            i = i + 1;
        }
        if (passwordText.isVisiable()) {
            i = i + 1;
        }
        if (serverText.isVisiable()) {
            i = i + 1;
        }
        if (portText.isVisiable()) {
            i = i + 1;
        }
        if (sidOrDatabaseText.isVisiable()) {
            i = i + 1;
        }
        if (schemaText.isVisiable()) {
            i = i + 1;
        }
        if (mappingFileText.isVisiable()) {
            i = i + 1;
        }
        if (datasourceText.isVisiable()) {
            i = i + 1;
        }
        if (additionParamText.isVisiable()) {
            i = i + 1;
        }
        if (i > 6) {
            scrolledComposite.setMinSize(SWT.DEFAULT, 310);
        } else {
            scrolledComposite.setMinSize(SWT.DEFAULT, 240);
        }
    }

    /**
     * 
     * ggu Comment method "checkAndSetIniSQLModel".
     * 
     * bug 12811
     */
    private void checkAndSetIniSQLModel() {
        IPreferenceStore store = CoreUIPlugin.getDefault().getPreferenceStore();
        getConnection().setSQLMode(store.getBoolean(IDatabasePrefConstants.USE_SQL_MODEL));
    }

    private void modifyFieldValue() {
        checkFieldsValue();
    }

    /**
     * DOC YeXiaowei Comment method "hideDbVersion".
     */
    private void hideDbVersion() {
        // qli comment
        // Just layout version combo when choose db type
        String dbType = getConnectionDBType();
        List<String> items = getVersionDrivers(dbType);
        String[] versions = new String[items.size()];
        items.toArray(versions);

        boolean isOracle = oracleVersionEnable();
        boolean isAS400 = as400VersionEnable();
        boolean isMySQL = asMySQLVersionEnable();
        boolean isVertica = asVerticaVersionEnable();
        boolean isSAS = asSASVersionEnable();
        boolean isSAPHana = asSAPHanaVersionEnable();
        boolean isImpala = ImpalaVersionEnable();
        boolean isMsSQL = asMsSQLVersionEnable();
        boolean isSybase = asSybaseVersionEnable();

        String selectedVersion = getConnection().getDbVersionString();
        dbVersionCombo.removeAll();
        dbVersionCombo.setHideWidgets(true);
        if (dbType.equals(EDatabaseConnTemplate.ORACLEFORSID.getDBDisplayName())
                || dbType.equals(EDatabaseConnTemplate.ORACLESN.getDBDisplayName())
                || dbType.equals(EDatabaseConnTemplate.ORACLE_OCI.getDBDisplayName())
                || dbType.equals(EDatabaseConnTemplate.ORACLE_CUSTOM.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isOracle);
        } else if (dbType.equals(EDatabaseConnTemplate.AS400.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isAS400);
        } else if (dbType.equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())) {
            // dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(true);
        } else if (dbType.equals(EDatabaseConnTemplate.MYSQL.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isMySQL);
        } else if (dbType.equals(EDatabaseConnTemplate.VERTICA.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isVertica);
        } else if (dbType.equals(EDatabaseConnTemplate.MSSQL05_08.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(false);
        } else if (dbType.equals(EDatabaseConnTemplate.MSSQL.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isMsSQL);
        } else if (dbType.equals(EDatabaseConnTemplate.SYBASEASE.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isSybase);
        } else if (dbType.equals(EDatabaseConnTemplate.SAS.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isSAS);
        } else if (dbType.equals(EDatabaseConnTemplate.PSQL.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(false);
        } else if (dbType.equals(EDatabaseConnTemplate.PLUSPSQL.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(false);
        } else if (dbType.equals(EDatabaseConnTemplate.SAPHana.getDBDisplayName())) {
            dbVersionCombo.getCombo().setItems(versions);
            dbVersionCombo.setHideWidgets(!isSAPHana);
        }
        if (selectedVersion != null && !"".equals(selectedVersion)) { //$NON-NLS-1$
            EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersion(selectedVersion);
            if (version != null) {
                dbVersionCombo.setText(version.getVersionDisplay());
            }
        } else {
            dbVersionCombo.select(0);
            EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(dbVersionCombo.getText());
            if (version != null) {
                getConnection().setDbVersionString(version.getVersionValue());
            }
        }
        urlConnectionStringText.setText(getStringConnection());
    }

    private void addHBaseSettingFieldsListeners() {
        hbaseDistributionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }

                IHDistribution newDistribution = getHBaseDistribution(hbaseDistributionCombo.getText(), true);
                if (newDistribution == null) {
                    return;
                }
                String originalDistributionName = getConnection().getParameters().get(
                        ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
                IHDistribution originalDistribution = getHBaseDistribution(originalDistributionName, false);
                //
                boolean doSupportMapRTicket = false;
                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService != null && newDistribution != null) {
                    doSupportMapRTicket = hadoopService.doSupportMapRTicket(newDistribution.getDefaultVersion());
                }
                hideControl(tableInfoPartOfHbaseComp, !doSupportMapRTicket);
                hideControl(useMaprTForHBase, !doSupportMapRTicket);
                hideControl(authenticationMaprTComForHBase, !(useMaprTForHBase.getSelection() && doSupportMapRTicket));
                hideControl(authenticationUserPassComForHBase, useKerberosForHBase.getSelection() && doSupportMapRTicket);
                authenticationGrpForHBase.layout();
                authenticationGrpForHBase.getParent().layout();
                if (originalDistribution == null || !newDistribution.getName().equals(originalDistribution.getName())) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION,
                            newDistribution.getName());
                    updateHBaseVersionPart(newDistribution);
                    fillDefaultsWhenHBaseVersionChanged();
                    checkFieldsValue();
                    checkHBaseKerberos();
                }
            }
        });

        hbaseVersionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }
                String originalDistributionName = getConnection().getParameters().get(
                        ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
                IHDistribution originalDistribution = getHBaseDistribution(originalDistributionName, false);
                if (originalDistribution == null) {
                    return;
                }
                String newVersionDisplayName = StringUtils.trimToNull(hbaseVersionCombo.getText());
                IHDistributionVersion newVersion = originalDistribution.getHDVersion(newVersionDisplayName, true);
                if (newVersion == null) {
                    return;
                }
                String originalVersionName = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
                IHDistributionVersion originalVersion = originalDistribution.getHDVersion(originalVersionName, false);

                if (originalVersion == null || newVersion.getVersion() != null
                        && !newVersion.getVersion().equals(originalVersion.getVersion())) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION, newVersion.getVersion());
                    fillDefaultsWhenHBaseVersionChanged();
                    checkFieldsValue();
                    checkHBaseKerberos();
                }
            }
        });

        hbaseCustomButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleHadoopCustomVersion(ECustomVersionType.HBASE);
            }
        });
    }

    private void addMaprdbSettingFieldsListeners() {
        maprdbDistributionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }

                IHDistribution newDistribution = getMaprdbDistribution(maprdbDistributionCombo.getText(), true);
                if (newDistribution == null) {
                    return;
                }
                String originalDistributionName = getConnection().getParameters().get(
                        ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
                IHDistribution originalDistribution = getMaprdbDistribution(originalDistributionName, false);
                //
                boolean doSupportMapRTicket = false;
                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService != null && newDistribution != null) {
                    doSupportMapRTicket = hadoopService.doSupportMapRTicket(newDistribution.getDefaultVersion());
                }
                hideControl(useMaprTForMaprdb, !doSupportMapRTicket);
                hideControl(authenticationMaprTComForMaprdb, !(useMaprTForMaprdb.getSelection() && doSupportMapRTicket));
                hideControl(authenticationUserPassComForMaprdb, useKerberosForMaprdb.getSelection() && doSupportMapRTicket);
                authenticationGrpForMaprdb.layout();
                authenticationGrpForMaprdb.getParent().layout();
                if (originalDistribution == null || !newDistribution.getName().equals(originalDistribution.getName())) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION,
                            newDistribution.getName());
                    updateMaprdbVersionPart(newDistribution);
                    fillDefaultsWhenMaprdbVersionChanged();
                    checkFieldsValue();
                }
            }
        });

        maprdbVersionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }
                String originalDistributionName = getConnection().getParameters().get(
                        ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
                IHDistribution originalDistribution = getMaprdbDistribution(originalDistributionName, false);
                if (originalDistribution == null) {
                    return;
                }
                String newVersionDisplayName = StringUtils.trimToNull(maprdbVersionCombo.getText());
                IHDistributionVersion newVersion = originalDistribution.getHDVersion(newVersionDisplayName, true);
                if (newVersion == null) {
                    return;
                }
                String originalVersionName = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION);
                IHDistributionVersion originalVersion = originalDistribution.getHDVersion(originalVersionName, false);

                if (originalVersion == null || newVersion.getVersion() != null
                        && !newVersion.getVersion().equals(originalVersion.getVersion())) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION, newVersion.getVersion());
                    fillDefaultsWhenMaprdbVersionChanged();
                    checkFieldsValue();
                }
            }
        });

        maprdbCustomButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleHadoopCustomVersion(ECustomVersionType.MAPRDB);
            }
        });
    }

    private void addImpalaSettingFieldsListeners() {
        impalaDistributionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }
                String originalDistributionName = getConnection().getParameters().get(
                        ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
                String newDistributionDisplayName = impalaDistributionCombo.getText();

                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService == null) {
                    return;
                }
                IHDistribution newDistribution = hadoopService.getImpalaDistributionManager().getDistribution(
                        newDistributionDisplayName, true);
                if (newDistribution == null) {
                    return;
                }

                if (!newDistribution.getName().equals(originalDistributionName)) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION,
                            newDistribution.getName());
                    updateImpalaVersionPart(newDistribution);
                    fillDefaultsWhenImpalaVersionChanged();
                    checkFieldsValue();
                }
            }
        });

        impalaVersionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (isContextMode()) {
                    return;
                }
                String originalVersionName = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);
                String newVersionDisplayName = StringUtils.trimToNull(impalaVersionCombo.getText());

                IHadoopDistributionService hadoopService = getHadoopDistributionService();
                if (hadoopService == null) {
                    return;
                }
                IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(
                        impalaDistributionCombo.getText(), true);
                if (impalaDistribution == null) {
                    return;
                }

                IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(newVersionDisplayName, true);

                if (impalaVersion != null && !impalaVersion.getVersion().equals(originalVersionName)) {
                    getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION,
                            impalaVersion.getVersion());
                    fillDefaultsWhenImpalaVersionChanged();
                    checkFieldsValue();
                }
            }
        });

        impalaCustomButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleHadoopCustomVersion(ECustomVersionType.HIVE);
            }
        });
    }

    private void handleHadoopCustomVersion(final ECustomVersionType type) {
        HadoopCustomVersionDefineDialog customVersionDialog = new HadoopCustomVersionDefineDialog(getShell(),
                HadoopVersionControlUtils.getCustomVersionMap(getConnection(), type.getGroup())) {

            @Override
            protected ECustomVersionType[] getDisplayTypes() {
                return new ECustomVersionType[] { type };
            }
        };
        if (customVersionDialog.open() == Window.OK) {
            HadoopVersionControlUtils.injectCustomVersionMap(getConnection(), customVersionDialog.getLibMap(), type.getGroup());
        }
    }

    /**
     * DOC YeXiaowei Comment method "addGeneralDbFieldsListeners".
     */
    private void addGeneralDbFieldsListeners() {

        generalJdbcClassNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcClassNameText.getText())) {
                        getConnection().setDriverClass(generalJdbcClassNameText.getText());
                    }
                    checkFieldsValue();
                }
            }
        });

        generalJdbcDriverjarText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcDriverjarText.getText())) {
                        String jarPath = generalJdbcDriverjarText.getText();
                        String[] jarNames = jarPath.split(";");
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < jarNames.length; i++) {
                            if (i != 0) {
                                buffer.append(";");
                            }
                            String path = jarNames[i];
                            IPath ipath = Path.fromOSString(path);
                            String lastSegment = ipath.lastSegment();
                            buffer.append(lastSegment);
                        }
                        getConnection().setDriverJarPath(buffer.toString());
                    }
                    checkFieldsValue();
                }
            }
        });

        generalJdbcUrlText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcUrlText.getText())) {
                        getConnection().setURL(generalJdbcUrlText.getText());
                    }
                    checkFieldsValue();
                }
            }
        });

        generalJdbcPasswordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (generalJdbcPasswordText.getText() != null) {
                        getConnection().setRawPassword(generalJdbcPasswordText.getText());
                    }
                    checkFieldsValue();
                }
            }
        });

        generalJdbcUserText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcUserText.getText())) {
                        getConnection().setUsername(generalJdbcUserText.getText());
                    }
                    checkFieldsValue();
                }
            }
        });

        jDBCschemaText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    String text = jDBCschemaText.getText();
                    schemaText.setText(text);
                    if (validText(schemaText.getText())) {
                        getConnection().setUiSchema(schemaText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalMappingFileText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalMappingFileText.getText())) {
                        getConnection().setDbmsId(generalMappingFileText.getText());
                    }
                    checkFieldsValue();
                }
            }
        });

        mappingFileText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(mappingFileText.getText())) {
                        getConnection().setDbmsId(mappingFileText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        browseJarFilesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String driverStr = generalJdbcDriverjarText.getText();
                String values[] = new String[0];
                if (driverStr != null && driverStr.length() > 0) {
                    values = driverStr.split(";");
                }
                List<String> asList = new ArrayList<String>(Arrays.asList(values));
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                    ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                            .getService(ILibraryManagerUIService.class);
                    IConfigModuleDialog dialog = libUiService.getConfigModuleDialog(getShell(), null);
                    if (dialog.open() == IDialogConstants.OK_ID) {
                        String selecteModule = dialog.getModuleName();
                        if (selecteModule != null && !asList.contains(selecteModule)) {
                            asList.add(selecteModule);
                        }
                    }
                }
                StringBuffer result = new StringBuffer();
                for (int i = 0; i < asList.size(); i++) {
                    result.append(asList.get(i));
                    if (i < asList.size() - 1) {
                        result.append(";");
                    }
                }
                generalJdbcDriverjarText.setText(result.toString());
            }

        });

        browseClassButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                generalJdbcClassNameText.removeAll();
                for (String jarName : generalJdbcDriverjarText.getText().trim().split(";")) { //$NON-NLS-1$
                    String stringToFile = getselecteModulePath(jarName);
                    if (stringToFile == null) {
                        stringToFile = jarName;
                    }
                    File file = new File(stringToFile);
                    if (file != null) {
                        try {
                            MyURLClassLoader cl = new MyURLClassLoader(file.toURL());
                            Class[] classes = cl.getAssignableClasses(Driver.class);
                            for (Class classe : classes) {
                                generalJdbcClassNameText.add(classe.getName());
                            }
                        } catch (Exception ex) {
                            ExceptionHandler.process(ex);
                        }
                    }
                }
                if (generalJdbcClassNameText.getItemCount() > 0) {
                    String driverClassName = generalJdbcClassNameText.getItem(0);
                    generalJdbcClassNameText.setText(driverClassName);
                }
            }
        });

        generalMappingSelectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                MappingFileSelectDialog dialog = new MappingFileSelectDialog(getShell());
                dialog.open();
                generalMappingFileText.setText(dialog.getSelectId());
            }
        });
        mappingSelectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                MappingFileSelectDialog dialog = new MappingFileSelectDialog(getShell());
                dialog.open();
                mappingFileText.setText(dialog.getSelectId());
            }
        });
    }

    /**
     * 
     * DOC YeXiaowei Comment method "isGeneralJDBC".
     * 
     * @return
     */
    private boolean isGeneralJDBC() {
        return EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName().equals(getConnectionDBType());
    }

    /**
     * To check if the current db selected is Hive. Added by Marvin Wang on Oct 15, 2012.
     * 
     * @return
     */
    private boolean isHiveDBConnSelected() {
        return EDatabaseTypeName.HIVE.getDisplayName().equals(getConnectionDBType());
    }

    private boolean isHBaseDBConnSelected() {
        return EDatabaseTypeName.HBASE.getDisplayName().equals(getConnectionDBType());
    }

    private boolean isMapRDBConnSelected() {
        return EDatabaseTypeName.MAPRDB.getDisplayName().equals(getConnectionDBType());
    }

    private boolean isImpalaDBConnSelected() {
        return EDatabaseTypeName.IMPALA.getDisplayName().equals(getConnectionDBType());
    }

    private boolean isOracleCustomDBConnSelected() {
        if (EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(getConnectionDBType())) {
            if (EDatabaseVersion4Drivers.ORACLE_12.name().equals(getConnection().getDbVersionString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     */
    @Override
    public boolean checkFieldsValue() {

        boolean checkGeneralDB = isGeneralJDBC();

        // See bug 004800
        if (!checkGeneralDB || getConnectionDBType().equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())) {
            getConnection().setURL(getStringConnection());
        }
        // boolean isTeradata = EDatabaseTypeName.TERADATA.getDisplayName().equals(getConnectionDBType());
        // sqlModeLabel.setVisible(isTeradata);
        // button1.setVisible(isTeradata);
        // button2.setVisible(isTeradata);
        /*
         * commet by bug 12811
         */
        // if (isTeradata) {
        // button1.setSelection(!isTeradata);
        // button2.setSelection(isTeradata);
        // } else {
        // button1.setSelection(getConnection().isSQLMode());
        // button2.setSelection(!getConnection().isSQLMode());
        // }

        // feature 17159
        if (CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
            if (!isSupportByTDQ()) {
                updateStatus(IStatus.WARNING, "some database types are not usable in the Data Profiler perspective."); //$NON-NLS-1$
                return false;
            }
        }

        if (isContextMode()) {
            return true;
        }
        databaseSettingIsValide = false;
        urlConnectionStringText.setText(getStringConnection());
        checkURLIsChanged();
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
        if (template != null) {
            EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersion(getConnection().getDbVersionString());
            urlConnectionStringText.setToolTipText(template.getUrlTemplate(version));
        }
        updateCheckButton();

        // if (!isModify) {
        // setPropertiesFormEditable(false);
        // }

        if ((EDatabaseConnTemplate.MARIADB.toString()).equals(dbVersionCombo.getText().toUpperCase())) {
            String strOfConnection = urlConnectionStringText.getText();
            if (strOfConnection != null && ("").equals(additionParamText.getText())) {//$NON-NLS-1$
                // minus ? on string of connection when the dbversion is MariaDb
                if (strOfConnection.contains(ADDIPARASYMBOL)) {
                    strOfConnection = strOfConnection.substring(0, strOfConnection.indexOf(ADDIPARASYMBOL));
                    urlConnectionStringText.setText(strOfConnection);
                }
            }
        }
        //
        // if (getConnectionDBType().length() <= 0) {
        //            updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.alert", "DB Type")); //$NON-NLS-1$
        // return false;
        // }

        // Show Database Properties
        // if (!isModify) {
        // setPropertiesFormEditable(true);
        // }

        if (!checkGeneralDB) {
            if (!checkTypeDBFieldValues()) {
                return false;
            }
        } else {
            if (!checkGeneralDBFieldValues()) {
                return false;
            }
        }

        if (isDBTypeSelected(EDatabaseConnTemplate.HBASE) && !checkHBaseSettings()) {
            return false;
        }

        if (isDBTypeSelected(EDatabaseConnTemplate.MAPRDB) && !checkMaprdbSettings()) {
            return false;
        }

        if (isDBTypeSelected(EDatabaseConnTemplate.IMPALA) && !checkImpalaSettings()) {
            return false;
        }

        if (!databaseSettingIsValide) {
            updateStatus(IStatus.INFO, Messages.getString("DatabaseForm.checkInformation")); //$NON-NLS-1$
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    // Added yyin 20121219 TDQ-6485, check if the url is changed, if yes ,need reload
    private void checkURLIsChanged() {
        if (originalURL != null) {
            String url = isGeneralJdbc() ? generalJdbcUrlText.getText() : urlConnectionStringText.getText();
            if (!originalURL.equalsIgnoreCase(url)) {
                ConnectionHelper.setIsConnNeedReload(getConnection(), Boolean.TRUE);
            } else if (!originalUischema.equalsIgnoreCase(schemaText.getText())) {
                ConnectionHelper.setIsConnNeedReload(getConnection(), Boolean.TRUE);
            } else {
                ConnectionHelper.setIsConnNeedReload(getConnection(), this.originalIsNeedReload);
            }
        }
    }// ~

    private boolean isGeneralJdbc() {
        boolean result = false;
        if (connectionItem != null && connectionItem.getConnection() != null) {
            Connection connection = connectionItem.getConnection();
            if (connection instanceof DatabaseConnection) {
                result = EDatabaseTypeName.GENERAL_JDBC == EDatabaseTypeName.getTypeFromDbType(((DatabaseConnection) connection)
                        .getDatabaseType());
            }
        }
        return result;
    }

    /**
     * DOC bZhou Comment method "isSupportByTDQ" for feature 17159.
     * 
     * @return
     */
    private boolean isSupportByTDQ() {
        String selectType = getConnectionDBType();
        List<String> tdqSupportDBType = MetadataConnectionUtils.getTDQSupportDBTemplate();
        if (!tdqSupportDBType.contains(selectType)) {
            return false;
        }

        return true;
    }

    private boolean checkGeneralDBFieldValues() {
        String value = generalJdbcUrlText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.urlAlert", generalJdbcUrlText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        // A valid JDBC URL must be specified
        if (!ConnectionUtils.isJDBCURL(value)) {
            updateStatus(IStatus.ERROR,
                    Messages.getString("DatabaseForm.genaralJDBC.validUrlAlert", generalJdbcUrlText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcDriverjarText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.driverAlert", generalJdbcDriverjarText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcClassNameText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.classNameAlert", generalJdbcClassNameText.getText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcUserText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.userNameAlert", generalJdbcUserText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcPasswordText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.passwordAlert", generalJdbcPasswordText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalMappingFileText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING,
                    Messages.getString("DatabaseForm.genaralJDBC.mappingFileAlert", generalMappingFileText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean validText(final String value) {
        return value != null && !value.equals(""); //$NON-NLS-1$
    }

    private boolean checkTypeDBFieldValues() {

        // Another fields depend on DbType
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
        EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersion(getConnection().getDbVersionString());
        String s = ""; //$NON-NLS-1$
        if (template != null) {
            s = template.getUrlTemplate(version);
        }

        if (s.contains(EDatabaseConnVar.HOST.getVariable()) && serverText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.serverAlert", serverText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains(EDatabaseConnVar.PORT.getVariable()) && portText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.portAlert", portText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if ((s.contains(EDatabaseConnVar.SID.getVariable()) || s.contains(EDatabaseConnVar.SERVICE_NAME.getVariable()))
                && sidOrDatabaseText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.sidAlert", sidOrDatabaseText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains(EDatabaseConnVar.FILENAME.getVariable()) && fileField.getText() == "") { //$NON-NLS-1$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.dbFileAlert", fileField.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains(EDatabaseConnVar.DATASOURCE.getVariable()) && datasourceText.getText() == "") { //$NON-NLS-1$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.dateSourceAlert", datasourceText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains(EDatabaseConnVar.DBROOTPATH.getVariable()) && directoryField.getText() == "") { //$NON-NLS-1$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.dbRootAlert", directoryField.getLabelText())); //$NON-NLS-1$
            return false;
        }

        if (EDatabaseConnTemplate.isSchemaNeeded(getConnection().getDatabaseType()) && schemaText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.schemaAlert", schemaText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        return true;

    }

    private boolean checkHBaseSettings() {
        if (hbaseDistributionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.hbase.distributionAlert")); //$NON-NLS-1$
            return false;
        }
        if (hbaseVersionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.hbase.versionAlert")); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean checkMaprdbSettings() {
        if (maprdbDistributionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.maprdb.distributionAlert")); //$NON-NLS-1$
            return false;
        }
        if (maprdbVersionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.maprdb.versionAlert")); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean checkImpalaSettings() {
        if (impalaDistributionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.impala.distributionAlert")); //$NON-NLS-1$
            return false;
        }
        if (impalaVersionCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.impala.versionAlert")); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * Set up the URL by checking the current hive mode. Added by Marvin Wang on Sep 4, 2012.
     * 
     * @return
     */
    private String getStringConnection() {
        String s = null;
        String versionStr = dbVersionCombo.getText();
        if (isContextMode()) {
            s = DBConnectionContextUtils.getUrlConnectionString(connectionItem, true);
        } else {
            if (isHiveDBConnSelected()) {
                String template = null;
                if (doSupportHive2()
                        && HiveServerVersionInfo.HIVE_SERVER_2.getDisplayName().equals(hiveServerVersionCombo.getText())) {
                    template = DbConnStrForHive.URL_HIVE_2_TEMPLATE;
                } else {
                    template = DbConnStrForHive.URL_HIVE_1_TEMPLATE;
                }
                if (!isHiveEmbeddedMode()) {
                    s = DatabaseConnStrUtil.getHiveURLStringForStandardalone(template, getConnection(), getConnection()
                            .getServerName(), getConnection().getPort(), getConnection().getSID());
                } else {
                    s = template;
                }
            } else if (isImpalaDBConnSelected()) {
                String template = DbConnStrForHive.URL_HIVE_2_TEMPLATE;
                s = DatabaseConnStrUtil.getImpalaString(getConnection(), getConnection().getServerName(), getConnection()
                        .getPort(), getConnection().getSID(), template);
                getConnection().setUiSchema(getConnection().getSID());
            } else {
                EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(versionStr);
                if (version != null) {
                    versionStr = version.getVersionValue();
                }
                s = DatabaseConnStrUtil.getURLString(getConnectionDBType(), versionStr, serverText.getText(),
                        usernameText.getText(), passwordText.getText(), portText.getText(), sidOrDatabaseText.getText(),
                        fileField.getText(), datasourceText.getText(), directoryField.getText(), additionParamText.getText());
            }
        }

        return s;
    }

    private void updateCheckButton() {
        // update checkEnable
        if (isContextMode()) {
            checkButton.setEnabled(true);
        } else {
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(getConnection()
                    .getDbVersionString());
            checkButton.setEnabled((getConnectionDBType().length() >= 0) && template != null
                    && (getStringConnection() != template.getUrlTemplate(version)));
            /* hbase/impala/mapr-db/.. has no url so need,using port or server instead */
            if (template != null
                    && template.getDbType() != null
                    && (template.getDbType().equals(EDatabaseTypeName.HBASE)
                            || template.getDbType().equals(EDatabaseTypeName.MAPRDB) || template.getDbType().equals(
                            EDatabaseTypeName.IMPALA))) {
                checkButton.setEnabled((getConnectionDBType().length() >= 0)
                        && template != null
                        && ((serverText.getText() != template.getUrlTemplate(version) || portText.getText() != template
                                .getDefaultPort())));
            }
            if (isGeneralJDBC()) {
                checkButton.setEnabled(true);
            } else if (isHiveDBConnSelected()) {
                checkButton.setEnabled(true);
            }
        }
    }

    /**
     * SetEditable fields.
     * 
     * @param boolean
     */
    @SuppressWarnings("static-access")
    private void setPropertiesFormEditable(boolean visible) {
        clearContextParams();
        EDBParamName sidOrDatabase = null;

        // hiveMode.setHideWidgets(true);
        // TODO if refectoring is failed, then revert this code.
        // doHideHiveWidgets(true);
        // update the UI label
        if (EDatabaseTypeName.ORACLEFORSID.getProduct().equals(getConnection().getProductId())) {
            if (EDatabaseConnTemplate.ORACLESN.getDBDisplayName().equals(getConnection().getDatabaseType())) {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.service_name")); //$NON-NLS-1$
                sidOrDatabaseText.setLabelWidth(65);
                sidOrDatabase = EDBParamName.ServiceName;
            } else if (EDatabaseConnTemplate.ORACLEFORSID.getDBDisplayName().equals(getConnection().getDatabaseType())) {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.sid")); //$NON-NLS-1$
                sidOrDatabase = EDBParamName.Sid;
            } else if (EDatabaseConnTemplate.ORACLE_OCI.getDBDisplayName().equals(getConnection().getDatabaseType())) {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.local_service_name")); //$NON-NLS-1$
                sidOrDatabase = EDBParamName.Sid;
            }
        } else {
            sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.DataBase")); //$NON-NLS-1$
            sidOrDatabase = EDBParamName.Database;
            if (EDatabaseConnTemplate.INFORMIX.getDBDisplayName().equals(getConnection().getDatabaseType())) {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.DataBase")); //$NON-NLS-1$
            }

        }

        if (EDatabaseTypeName.MSODBC.getDisplayName().equals(getConnectionDBType())) {
            sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.DataBase")); //$NON-NLS-1$
        }

        if (EDatabaseTypeName.GODBC.getDisplayName().equals(getConnectionDBType())) {
            sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.DataBase")); //$NON-NLS-1$
        }

        // hshen
        if (EDatabaseConnTemplate.GENERAL_JDBC.getDBTypeName().equals(getConnectionDBType())) {
            addContextParams(EDBParamName.JdbcUrl, visible);
            addContextParams(EDBParamName.DriverJar, visible);
            addContextParams(EDBParamName.MappingFile, visible);
            addContextParams(EDBParamName.ClassName, visible);
        }

        addContextParams(EDBParamName.Login, visible);
        addContextParams(EDBParamName.Password, visible);
        // update the UI Fields

        boolean isOracle = visible && oracleVersionEnable();
        boolean isAS400 = visible && as400VersionEnable();
        boolean isMySQL = visible && asMySQLVersionEnable();
        boolean isVertica = visible && asVerticaVersionEnable();
        boolean isSAS = visible && asSASVersionEnable();
        boolean isHbase = visible && asHbaseVersionEnable();
        boolean isMaprdb = visible && asMaprdbVersionEnable();
        boolean isImpala = visible && ImpalaVersionEnable();
        boolean isHive = visible && hiveVersionEnable();
        boolean isMsSQL = visible && asMsSQLVersionEnable();
        boolean isSybase = visible && asSybaseVersionEnable();

        dbVersionCombo
                .setEnabled(!isReadOnly()
                        && (isOracle || isAS400 || isMySQL || isVertica || isSAS || isImpala || isMsSQL || isSybase
                                || EDatabaseConnTemplate.PSQL.getDBTypeName().equals(getConnectionDBType())
                                || EDatabaseConnTemplate.PLUSPSQL.getDBTypeName().equals(getConnectionDBType())
                                || EDatabaseConnTemplate.ACCESS.getDBTypeName().equals(getConnectionDBType()) || EDatabaseConnTemplate.MSSQL05_08
                                .getDBDisplayName().equals(getConnectionDBType())));
        usernameText.setEditable(visible);
        passwordText.setEditable(visible);
        serverText.setEditable(false);
        portText.setEditable(false);
        sidOrDatabaseText.setEditable(false);
        datasourceText.setEditable(false);
        additionParamText.setEditable(false);
        schemaText.setEditable(false);
        fileField.setEditable(false);
        directoryField.setEditable(false);
        mappingFileText.setEditable(false);
        mappingSelectButton.setEnabled(false);

        if (EDatabaseConnTemplate.GODBC.getDBTypeName().equals(getConnectionDBType())
                || EDatabaseConnTemplate.GENERAL_JDBC.getDBTypeName().equals(getConnectionDBType())) {
            addContextParams(EDBParamName.MappingFile, true);
            mappingFileText.show();
            mappingFileText.setEditable(true);
            mappingSelectButton.setVisible(true);
            mappingSelectButton.setEnabled(true);
        } else {
            addContextParams(EDBParamName.MappingFile, false);
            mappingFileText.hide();
            mappingFileText.setEditable(false);
            mappingSelectButton.setVisible(false);
            mappingSelectButton.setEnabled(false);
        }

        if (getConnectionDBType().length() <= 0) {
            urlConnectionStringText.setEditable(false);
        } else {
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
            String s = ""; //$NON-NLS-1$
            // For bug TDI-25424, the Database text widget is missing.
            if (template != null) {
                EDatabaseVersion4Drivers version = null;
                if (isHiveDBConnSelected()) {
                    boolean isEmbeddedMode = isHiveEmbeddedMode();
                    if (doSupportHive2()) {
                        if (isEmbeddedMode) {
                            s = template.getUrlTemplate(EDatabaseVersion4Drivers.HIVE_2_EMBEDDED);
                        } else {
                            s = template.getUrlTemplate(EDatabaseVersion4Drivers.HIVE_2_STANDALONE);
                        }
                    } else {
                        if (isEmbeddedMode) {
                            s = template.getUrlTemplate(EDatabaseVersion4Drivers.HIVE_EMBEDDED);
                        } else {
                            s = template.getUrlTemplate(EDatabaseVersion4Drivers.HIVE);
                        }
                    }
                } else {
                    version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(dbVersionCombo.getText());
                    s = template.getUrlTemplate(version);
                }
            }
            if (isHbase || isMaprdb || isDBTypeSelected(EDatabaseConnTemplate.ORACLE_CUSTOM)
                    || isDBTypeSelected(EDatabaseConnTemplate.IMPALA)) {
                urlConnectionStringText.hide();
            } else {
                urlConnectionStringText.show();
            }
            if (isDBTypeSelected(EDatabaseConnTemplate.ORACLE_CUSTOM)) {
                serverText.setLabelText(Messages.getString("DatabaseForm.stringConnection"));
            } else {
                serverText.setLabelText(Messages.getString("DatabaseForm.server"));
            }

            hideHCLinkSettings(!isHbase && !isMaprdb && !isHiveDBConnSelected());
            hideHBaseSettings(!isHbase);
            hideMaprdbSettings(!isMaprdb);
            hideImpalaSettings(!isImpala);
            updateHadoopPropertiesFieldsState();
            updateHiveJDBCPropertiesFieldsState();
            showIfAdditionalJDBCSettings();
            showIfSupportEncryption();
            showIfAuthentication();
            hideHiveExecutionFields(!doSupportTez());

            urlConnectionStringText.setEditable(!visible);
            // schemaText.hide();
            boolean schemaTextIsShow = true;
            if (template == EDatabaseConnTemplate.MSSQL) {
                schemaText.show();
                schemaText.setEditable(true);
                addContextParams(EDBParamName.Schema, true);

                // for bug 13033
                // if (schemaText.getText().equals("")) { //$NON-NLS-1$
                // schemaText.setText("dbo"); //$NON-NLS-1$
                // }
            } else if (template == EDatabaseConnTemplate.VERTICA || template == EDatabaseConnTemplate.INFORMIX) {
                // add for bug 0009553 10531
                schemaText.show();
                schemaText.setEditable(true);
                addContextParams(EDBParamName.Schema, true);
            } else if (template == EDatabaseConnTemplate.GENERAL_JDBC) {
                String jdbcUrlString = ""; //$NON-NLS-1$
                if (isContextMode()) {
                    if (selectedContextType == null) {
                        selectedContextType = ConnectionContextHelper.getContextTypeForContextMode(getShell(), getConnection(),
                                true);
                    }
                    if (selectedContextType != null) {
                        jdbcUrlString = ConnectionContextHelper.getOriginalValue(selectedContextType, getConnection().getURL());
                    }

                } else {
                    jdbcUrlString = generalJdbcUrlText.getText();
                }
                if (jdbcUrlString.contains("sqlserver")) {//$NON-NLS-1$
                    jDBCschemaText.setHideWidgets(false);
                    addContextParams(EDBParamName.Schema, true);
                } else {
                    jDBCschemaText.setHideWidgets(true);
                    addContextParams(EDBParamName.Schema, false);
                }
            } else {
                // schemaText.hide();
                schemaTextIsShow = false;
                // addContextParams(EDBParamName.Schema, false);
            }
            // hbase need serverText
            if (s.contains(EDatabaseConnVar.HOST.getVariable()) || isHbase || isMaprdb || isImpala) {
                if (!EDatabaseConnTemplate.GENERAL_JDBC.getDBTypeName().equals(getConnectionDBType())) {
                    serverText.show();
                    serverText.setEditable(visible);
                    if (isHbase) {
                        String serverName = getConnection().getServerName();
                        if (serverName == null || "".equals(serverName)) { //$NON-NLS-1$
                            serverText.setText(EDatabaseConnTemplate.HBASE.getUrlTemplate(EDatabaseVersion4Drivers.HBASE));
                        }
                    }
                    if (isMaprdb) {
                        String serverName = getConnection().getServerName();
                        if (serverName == null || "".equals(serverName)) { //$NON-NLS-1$
                            serverText.setText(EDatabaseConnTemplate.MAPRDB.getUrlTemplate(EDatabaseVersion4Drivers.MAPRDB));
                        }
                    }
                    if (isImpala) {
                        String serverName = getConnection().getServerName();
                        if (serverName == null || "".equals(serverName)) { //$NON-NLS-1$
                            serverText.setText(EDatabaseConnTemplate.IMPALA.getDefaultServer(null));
                        }
                    }
                    if (isHive) {
                        String serverName = getConnection().getServerName();
                        if (serverName == null || "".equals(serverName)) { //$NON-NLS-1$
                            serverText.setText(EDatabaseConnTemplate.HIVE.getDefaultServer(EDatabaseVersion4Drivers.HIVE));
                        }
                    }
                    addContextParams(EDBParamName.Server, visible);
                }
            } else {
                if (isHiveDBConnSelected()) {
                    if (isHiveEmbeddedMode()) {
                        // Need to revert if required, changed by Marvin Wang on Nov. 22, 2012.
                        // serverText.hide();
                        // portText.hide();
                        portText.show();
                        serverText.show();
                        portText.setEditable(true);
                        serverText.setEditable(true);
                        hideMappingFileRelatedWidgets(true);
                    } else {
                        portText.show();
                        serverText.show();
                        portText.setEditable(true);
                        serverText.setEditable(visible);
                        hideMappingFileRelatedWidgets(true);
                    }
                    sidOrDatabaseText.setEditable(true);
                } else {
                    serverText.hide();
                }
                addContextParams(EDBParamName.Server, false);
            }
            if (s.contains(EDatabaseConnVar.PORT.getVariable()) || isHbase || isMaprdb || isImpala) {
                portText.show();
                portText.setEditable(visible);
                addContextParams(EDBParamName.Port, visible);
            } else {
                if (isHiveDBConnSelected()) {
                    portText.show();
                    portText.setEditable(visible);
                } else {
                    portText.hide();
                }
                addContextParams(EDBParamName.Port, false);
            }
            if (s.contains(EDatabaseConnVar.SID.getVariable()) || s.contains(EDatabaseConnVar.SERVICE_NAME.getVariable())) {
                if (!EDatabaseConnTemplate.GENERAL_JDBC.getDBTypeName().equals(getConnectionDBType())) {
                    // Added by Marvin Wang on Aug. 15, 2012 for handling the case of Hive.
                    if (EDatabaseTypeName.HIVE.getDisplayName().equalsIgnoreCase(getConnectionDBType())) {
                        if (isHiveEmbeddedMode()) {
                            // It should be reverted if emembedded is available.
                            // sidOrDatabaseText.show();
                            // sidOrDatabaseText.setEditable(true);
                            // (--Done by Marvin Wang on Oct.15, 2012.)
                            // Need to revert if required, changed by Marvin Wang on Nov. 22, 2012.
                            // serverText.hide();
                            // portText.hide();
                            portText.show();
                            serverText.show();
                            serverText.setEditable(true);
                            hideMappingFileRelatedWidgets(true);
                        } else {
                            portText.show();
                            serverText.show();
                            serverText.setEditable(true);
                            hideMappingFileRelatedWidgets(true);
                        }
                        sidOrDatabaseText.show();
                        sidOrDatabaseText.setEditable(true);
                    } else {
                        sidOrDatabaseText.show();
                        sidOrDatabaseText.setEditable(visible);
                    }
                    addContextParams(sidOrDatabase, visible);
                } else {
                    sidOrDatabaseText.hide();
                    addContextParams(sidOrDatabase, false);
                }
            } else {
                if (template != null && template.getDbType() != EDatabaseTypeName.JAVADB_EMBEDED && !isHiveDBConnSelected()) {
                    sidOrDatabaseText.hide();
                    addContextParams(sidOrDatabase, false);
                }
            }
            if (s.contains(EDatabaseConnVar.FILENAME.getVariable())) {
                fileField.show();
                fileField.setEditable(!isReadOnly() && visible);
                addContextParams(EDBParamName.File, visible);
                boolean isSqlLite = false;
                if (template.getDbType() == EDatabaseTypeName.SQLITE) {
                    isSqlLite = true;
                    usernameText.hide();
                    passwordText.hide();
                } else {
                    isSqlLite = false;
                    usernameText.show();
                    passwordText.show();
                }
                usernameText.setEditable(!isSqlLite);
                passwordText.setEditable(!isSqlLite);
                addContextParams(EDBParamName.Login, !isSqlLite);
                addContextParams(EDBParamName.Password, !isSqlLite);
            } else {
                fileField.hide();
                addContextParams(EDBParamName.File, false);
                // if (template.getDbType() != EDatabaseTypeName.SYBASEASE && template.getDbType() !=
                // EDatabaseTypeName.TERADATA) {
                // usernameText.hide();
                // passwordText.hide();
                // addContextParams(EDBParamName.Login, false);
                // addContextParams(EDBParamName.Password, false);
                // } else {

                /* hbase no need username and password */
                usernameText.show();
                passwordText.show();
                if (isHbase || isMaprdb) {
                    usernameText.hide();
                    passwordText.hide();
                } else if (isImpala) {
                    // usernameText.hide();
                    passwordText.hide();
                } else if (isHiveDBConnSelected()) {
                    if (isHiveEmbeddedMode()) {
                        // Need to revert if required, changed by Marvin Wang on Nov. 22, 2012.
                        // usernameText.hide();
                        // passwordText.hide();
                        usernameText.show();
                        passwordText.show();
                        serverText.setEditable(true);

                        hideMappingFileRelatedWidgets(true);
                    } else {
                        usernameText.show();
                        passwordText.show();

                        hideMappingFileRelatedWidgets(true);
                    }

                }
                addContextParams(EDBParamName.Login, true);
                addContextParams(EDBParamName.Password, true);
                // }

            }
            if (s.contains(EDatabaseConnVar.DATASOURCE.getVariable())) {
                datasourceText.show();
                datasourceText.setEditable(visible);
                addContextParams(EDBParamName.Datasource, visible);
            } else {
                datasourceText.hide();
                addContextParams(EDBParamName.Datasource, false);
            }

            if (s.contains(EDatabaseConnVar.DBROOTPATH.getVariable())) {
                directoryField.show();
                directoryField.setEditable(visible);
                sidOrDatabaseText.setEditable(visible);
                addContextParams(EDBParamName.DBRootPath, visible);
                addContextParams(sidOrDatabase, visible);
            } else {
                directoryField.hide();
                addContextParams(EDBParamName.DBRootPath, false);
            }

            if (EDatabaseConnTemplate.isSchemaNeeded(getConnection().getDatabaseType())) {
                schemaText.show();
                schemaText.setEditable(visible);
                // for hbase it should be using column family to replace the common schema.
                if (isHbase || isMaprdb) {
                    schemaText.setLabelText("Column Family"); //$NON-NLS-1$
                }
                addContextParams(EDBParamName.Schema, visible);
            } else {
                if (!schemaTextIsShow) {
                    schemaText.hide();
                    addContextParams(EDBParamName.Schema, false);
                }
            }
            if (EDatabaseConnTemplate.isAddtionParamsNeeded(getConnection().getDatabaseType())
                    && !EDatabaseConnTemplate.GENERAL_JDBC.getDBTypeName().equals(getConnectionDBType()) && visible) {
                additionParamText.show();
                additionParamText.setEditable(true);
                addContextParams(EDBParamName.AdditionalParams, true);
            } else {
                additionParamText.hide();
                addContextParams(EDBParamName.AdditionalParams, false);
            }
            if (EDatabaseConnTemplate.FIREBIRD.equals(template)) {
                portText.show();
                portText.setEditable(true);
                addContextParams(EDBParamName.Port, true);
            }
            if (isHiveDBConnSelected()) {
                if (isHiveEmbeddedMode()) {
                    // Need to revert if required, changed by Marvin Wang on Nov. 22, 2012.
                    // portText.hide();
                    // serverText.hide();
                    // usernameText.hide();
                    passwordText.hide();
                    portText.show();
                    serverText.show();
                    hideMappingFileRelatedWidgets(true);
                } else {
                    portText.show();
                    serverText.show();
                    usernameText.show();
                    passwordText.show();
                    hideMappingFileRelatedWidgets(true);
                }
                schemaText.hide();
            }
        }
        doHiveUIContentsLayout();
        hbaseSettingGroup.layout();
        maprdbSettingGroup.layout();
        impalaSettingGroup.layout();
        hadoopPropGrp.layout();
        metastorePropGrp.layout();
        compositeDbSettings.layout();
        typeDbCompositeParent.layout();
        newParent.layout();
        compositeGroupDbSettings.layout();
    }

    private void collectContextParams() {
        collectHiveContextParams();
        collectHBaseContextParams();
        collectMaprdbContextParams();
        collectImpalaContextParams();
        collectOracleCustomContextParams();
    }

    private void collectHiveContextParams() {
        // recollect context params for hive
        if (isHiveDBConnSelected()) {
            getConetxtParams().clear();
            addContextParams(EDBParamName.Login, true);
            addContextParams(EDBParamName.Server, true);
            addContextParams(EDBParamName.Port, true);
            addContextParams(EDBParamName.Database, true);
            addContextParams(EDBParamName.NameNode, useHadoopRepositoryParam());
            addContextParams(EDBParamName.JobTrackerOrResourceManager, useHadoopRepositoryParam());
            addContextParams(EDBParamName.Password, !isHiveEmbeddedMode());
            boolean isHivePrincipal = isHiveDBConnSelected() && doSupportSecurity() && useKerberos.getSelection();
            addContextParams(EDBParamName.HivePrincipal, isHivePrincipal);
            boolean hasAuthentication = isHivePrincipal && isHiveEmbeddedMode();
            addContextParams(EDBParamName.HiveMetastore, hasAuthentication);
            addContextParams(EDBParamName.HiveDriverJar, hasAuthentication);
            addContextParams(EDBParamName.HiveDriveClass, hasAuthentication);
            addContextParams(EDBParamName.HiveUserName, hasAuthentication);
            addContextParams(EDBParamName.HivePassword, hasAuthentication);
            addContextParams(EDBParamName.HiveKeyTabPrincipal, isHivePrincipal && useKeyTab.getSelection());
            addContextParams(EDBParamName.HiveKeyTab, isHivePrincipal && useKeyTab.getSelection());
            addContextParams(EDBParamName.hiveAdditionalJDBCParameters, isSupportHiveAdditionalSettings());
            boolean addSSLEncryptionContext = isSupportSSLEncryption() && isSupportSSLTrustStore();
            addContextParams(EDBParamName.hiveSSLTrustStorePath, addSSLEncryptionContext);
            addContextParams(EDBParamName.hiveSSLTrustStorePassword, addSSLEncryptionContext);

            addContextParams(EDBParamName.Username, !useKerberos.getSelection() && useMaprTForHive.getSelection());
            addContextParams(EDBParamName.Maprticket_Password, !useKerberos.getSelection() && useMaprTForHive.getSelection());
            addContextParams(EDBParamName.Maprticket_Cluster, useMaprTForHive.getSelection());
            addContextParams(EDBParamName.Maprticket_Duration, useMaprTForHive.getSelection());
        }
    }

    private void collectHBaseContextParams() {
        // recollect context params for Hbase
        if (isHBaseDBConnSelected()) {
            getConetxtParams().clear();
            addContextParams(EDBParamName.Server, true);
            addContextParams(EDBParamName.Port, true);
            addContextParams(EDBParamName.Schema, true);
            addContextParams(EDBParamName.TableNSMapping, set_table_ns_mapping.getSelection());
            addContextParams(EDBParamName.Znode_Parent, set_znode_parent.getSelection());
            addContextParams(EDBParamName.MasterPrincipal, useKerberosForHBase.getSelection());
            addContextParams(EDBParamName.RegionPrincipal, useKerberosForHBase.getSelection());
            addContextParams(EDBParamName.HbaseKeyTabPrincipal, useKeyTabForHBase.getSelection());
            addContextParams(EDBParamName.HbaseKeyTab, useKeyTabForHBase.getSelection());

            addContextParams(EDBParamName.Username, !useKerberosForHBase.getSelection() && useMaprTForHBase.getSelection());
            addContextParams(EDBParamName.Maprticket_Password,
                    !useKerberosForHBase.getSelection() && useMaprTForHBase.getSelection());
            addContextParams(EDBParamName.Maprticket_Cluster, useMaprTForHBase.getSelection());
            addContextParams(EDBParamName.Maprticket_Duration, useMaprTForHBase.getSelection());
        }
    }

    private void collectMaprdbContextParams() {
        // recollect context params for maprdb
        if (isMapRDBConnSelected()) {
            getConetxtParams().clear();
            addContextParams(EDBParamName.Server, true);
            addContextParams(EDBParamName.Port, true);
            addContextParams(EDBParamName.Schema, true);
            addContextParams(EDBParamName.TableNSMapping, true);
            addContextParams(EDBParamName.Znode_Parent, set_znode_parent.getSelection());
            addContextParams(EDBParamName.MasterPrincipal, useKerberosForMaprdb.getSelection());
            addContextParams(EDBParamName.RegionPrincipal, useKerberosForMaprdb.getSelection());
            addContextParams(EDBParamName.MaprdbKeyTabPrincipal, useKeyTabForMaprdb.getSelection());
            addContextParams(EDBParamName.MaprdbKeyTab, useKeyTabForMaprdb.getSelection());

            addContextParams(EDBParamName.Username, !useKerberosForMaprdb.getSelection() && useMaprTForMaprdb.getSelection());
            addContextParams(EDBParamName.Maprticket_Password,
                    !useKerberosForMaprdb.getSelection() && useMaprTForMaprdb.getSelection());
            addContextParams(EDBParamName.Maprticket_Cluster, useMaprTForMaprdb.getSelection());
            addContextParams(EDBParamName.Maprticket_Duration, useMaprTForMaprdb.getSelection());
        }
    }

    private void collectImpalaContextParams() {
        // recollect context params for impala
        if (isImpalaDBConnSelected()) {
            getConetxtParams().clear();
            addContextParams(EDBParamName.Login, true);
            addContextParams(EDBParamName.Server, true);
            addContextParams(EDBParamName.Port, true);
            addContextParams(EDBParamName.Database, true);
            addContextParams(EDBParamName.ImpalaPrincipal, useKerberosForImpala.getSelection());
        }
    }

    private void collectOracleCustomContextParams() {
        // recollect context params for Oracle Custom
        if (isOracleCustomDBConnSelected()) {
            getConetxtParams().clear();
            addContextParams(EDBParamName.Server, true);
            addContextParams(EDBParamName.Password, true);
            addContextParams(EDBParamName.Login, true);
            addContextParams(EDBParamName.Schema, true);
            addContextParams(EDBParamName.AdditionalParams, true);
            boolean addSSLEncryptionContext = isSupportSSLEncryption() && isSupportSSLTrustStore();
            addContextParams(EDBParamName.SSLTrustStorePath, addSSLEncryptionContext);
            addContextParams(EDBParamName.SSLTrustStorePassword, addSSLEncryptionContext);
            boolean addSSLClientAuthContext = isSupportSSLEncryption() && isSupportSSLClientAuth();
            addContextParams(EDBParamName.SSLKeyStorePath, addSSLClientAuthContext);
            addContextParams(EDBParamName.SSLKeyStorePassword, addSSLClientAuthContext);
        }
    }

    private boolean useHadoopRepositoryParam() {
        String hcId = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
        return hcId == null && isHiveEmbeddedMode();
    }

    /**
     * 
     * Added by Marvin Wang on Nov 29, 2012.
     * 
     * @param hide
     */
    private void hideMappingFileRelatedWidgets(boolean hide) {
        mappingFileText.setHideWidgets(hide);
        mappingFileText.setEditable(!hide);
        mappingSelectButton.setVisible(!hide);
        mappingSelectButton.setEnabled(!hide);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "oracleVersionEnable".
     * 
     * @return
     */
    private boolean oracleVersionEnable() {

        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null
                && (template == EDatabaseConnTemplate.ORACLEFORSID || template == EDatabaseConnTemplate.ORACLESN
                        || template == EDatabaseConnTemplate.ORACLE_OCI || template == EDatabaseConnTemplate.ORACLE_CUSTOM);
    }

    private boolean ImpalaVersionEnable() {

        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && (template == EDatabaseConnTemplate.IMPALA);
    }

    private boolean hiveVersionEnable() {

        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && (template == EDatabaseConnTemplate.HIVE);
    }

    /**
     * 
     * DOC qli Comment method "as400VersionEnable".
     * 
     * @return
     */
    private boolean as400VersionEnable() {

        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.AS400
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC zli Comment method "as400VersionEnable".
     * 
     * @return
     */
    private boolean asMySQLVersionEnable() {
        // for bug 11487
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.MYSQL
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC hwang Comment method "asMsSQLVersionEnable".
     * 
     * @return
     */
    private boolean asMsSQLVersionEnable() {
        // for bug 11487
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.MSSQL
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC hwang Comment method "asSybaseVersionEnable".
     * 
     * @return
     */
    private boolean asSybaseVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.SYBASEASE
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC hwang Comment method "sasVersionEnable".
     * 
     * @return
     */
    private boolean asSASVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.SAS
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC hwang Comment method "asSAPHanaVersionEnable".
     * 
     * @return
     */
    private boolean asSAPHanaVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.SAPHana
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    /**
     * 
     * DOC hwang Comment method "VerticaVersionEnable".
     * 
     * @return
     */
    private boolean asVerticaVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.VERTICA
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    private boolean asHbaseVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.HBASE
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    private boolean asMaprdbVersionEnable() {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == EDatabaseConnTemplate.MAPRDB
                && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA);
    }

    private boolean isDBTypeSelected(EDatabaseConnTemplate dbTemplate) {
        if (getConnectionDBType().length() <= 0) {
            return false;
        }
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnectionDBType());
        return template != null && template == dbTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        updateCheckButton();
        if (urlConnectionStringText.getText().equals("")) { //$NON-NLS-1$
            urlConnectionStringText.setText(getStringConnection());
        }
        if (isContextMode()) {
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            setPropertiesFormEditable(template != null);
            adaptFormToEditable();
        } else {
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            setPropertiesFormEditable(template != null);
        }
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
        /**
         * add by wzhang. Estimate the status
         */
        if (visible) {
            updateStatus(getStatusLevel(), getStatus());
        }
    }

    protected DatabaseConnection getConnection() {
        return (DatabaseConnection) connectionItem.getConnection();
    }

    protected void evaluateTextField(boolean b) {

        if (b) {
            updateStatus(IStatus.OK, null);
        } else {
            updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.portError")); //$NON-NLS-1$

        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        // dbTypeCombo.setReadOnly(isContextMode());

        urlConnectionStringText.setEditable(!isContextMode());
        usernameText.setEditable(!isContextMode());
        passwordText.setEditable(!isContextMode());
        serverText.setEditable(!isContextMode());
        portText.setEditable(!isContextMode());
        sidOrDatabaseText.setEditable(!isContextMode());
        schemaText.setEditable(!isContextMode());
        dbVersionCombo.setReadOnly(isContextMode());
        datasourceText.setEditable(!isContextMode());
        additionParamText.setEditable(!isContextMode());
        fileField.setEditable(!isContextMode());
        directoryField.setEditable(!isContextMode());

        // button1.setEnabled(!isContextMode());
        // button2.setEnabled(!isContextMode());
        // hshen
        generalJdbcUrlText.setEditable(!isContextMode());

        generalJdbcUserText.setEditable(!isContextMode());

        generalJdbcPasswordText.setEditable(!isContextMode());

        generalJdbcClassNameText.setEnabled(!isContextMode());

        generalJdbcDriverjarText.setEditable(!isContextMode());

        jDBCschemaText.setEditable(!isContextMode());

        generalMappingFileText.setEditable(!isContextMode());
        mappingFileText.setEditable(!isContextMode());
        if (isContextMode()) {
            passwordText.getTextControl().setEchoChar('\0');
            generalJdbcPasswordText.getTextControl().setEchoChar('\0');
            passwordTxt.getTextControl().setEchoChar('\0');
        } else {
            passwordText.getTextControl().setEchoChar('*');
            generalJdbcPasswordText.getTextControl().setEchoChar('*');
            passwordTxt.getTextControl().setEchoChar('*');
        }
        if (isHiveDBConnSelected()) {
            adaptHadoopLinkedPartToReadOnly();
            adaptHiveDBHadoopPartEditable();
            updateHadoopProperties(!isContextMode());
        }
        if (isHBaseDBConnSelected()) {
            adaptHbaseHadoopPartEditable();
            updateHadoopProperties(!isContextMode());
        }
        if (isMapRDBConnSelected()) {
            adaptMaprdbHadoopPartEditable();
            updateHadoopProperties(!isContextMode());
        }
        if (isImpalaDBConnSelected()) {
            adaptImpalaHadoopPartEditable();
            updateHadoopProperties(!isContextMode());
        }
        if (isOracleCustomDBConnSelected()) {
            adaptOracleCustomPartEditable();
        }
    }

    public boolean isDbTypenull() {
        return getConnectionDBType().trim().length() == 0;
    }

    private String checkDBVersion() {
        String msg = null;
        EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersionDisplay(dbVersionCombo.getText());
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        DatabaseConnection connection = getConnection();
        List<EDatabaseVersion4Drivers> dbTypeList = EDatabaseVersion4Drivers.indexOfByDbType(connection.getDatabaseType());
        if (version != null && dbTypeList.size() > 1) {
            EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(getConnection().getDatabaseType());
            if (dbType == null || dbType.equals(EDatabaseTypeName.ACCESS) || dbType.equals(EDatabaseTypeName.PSQL)
                    || dbType.equals(EDatabaseTypeName.PLUSPSQL) || dbType.equals(EDatabaseTypeName.IMPALA)) {
                // no version check for these dbs
                return null;
            }
            if (version == EDatabaseVersion4Drivers.SYBASEASE) {
                return null;
            }
            if (connection.getDriverClass() == null && dbType != EDatabaseTypeName.GENERAL_JDBC) {
                String driverClass = extractMeta.getDriverClassByDbType(connection.getDatabaseType());
                connection.setDriverClass(driverClass);
            }
            java.sql.Connection sqlConn = MetadataConnectionUtils.createConnection(connection).getObject();
            // if the dbtype is Access,it will throw a sqlException
            if (sqlConn != null) {
                try {
                    DatabaseMetaData dm = extractMeta.getDatabaseMetaData(sqlConn, connection);
                    int versionNum = dm.getDatabaseMajorVersion();
                    String[] strArray = version.getVersionValue().split("_"); //$NON-NLS-1$
                    if (strArray.length > 1 && strArray[1].startsWith(Integer.toString(versionNum))) {
                        msg = null;
                    } else {
                        msg = "Version detected on server is \"" + strArray[0] + " " + versionNum + "\"."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                } catch (SQLException exp) {
                    ExceptionHandler.process(exp);
                } finally {
                    if (sqlConn != null) {
                        ConnectionUtils.closeConnection(sqlConn);
                    }
                }
            }
        }
        return msg;
    }

    protected void initImpalaInfo() {
        DatabaseConnection connection = getConnection();

        String useKrb = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
        String impalaPrincipla = connection.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);

        if (Boolean.valueOf(useKrb)) {
            useKerberosForImpala.setSelection(true);
            GridData hadoopData = (GridData) authenticationComForImpala.getLayoutData();
            hadoopData.exclude = false;
            authenticationComForImpala.setVisible(true);
            authenticationComForImpala.setLayoutData(hadoopData);
            authenticationComForImpala.getParent().layout();
            authenticationComForImpala.layout();
            authenticationComForImpala.getParent().layout();
        }
        impalaPrincipalTxt.setText(impalaPrincipla == null ? "impala/_HOST@EXAMPLE.COM" : impalaPrincipla); //$NON-NLS-1$

    }

    /**
     * Initializes hive info for Hive UI. If the distribution value from connection is <code>null</code>, the default
     * selected indexs of hive combos like distribution, hive vesion and hive mode are 0. Added by Marvin Wang on Aug
     * 10, 2012.
     */
    protected void initHiveInfo() {
        DatabaseConnection connection = getConnection();
        String distributionObj = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String hiveVersion = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        String hiveMode = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);

        IHDistribution hiveDistribution = HiveMetadataHelper.getDistribution(distributionObj, false);
        IHDistributionVersion hdVersion = null;
        if (hiveDistribution != null) {
            hdVersion = hiveDistribution.getHDVersion(hiveVersion, false);
            updateHiveDistributionAndMakeSelection(hiveDistribution);
            updateHiveVersionAndMakeSelection(hiveDistribution, hdVersion);
            updateHiveServerAndMakeSelection(hiveDistribution, hdVersion);
            updateHiveModeAndMakeSelection(HiveModeInfo.get(hiveMode));

        } else {
            updateHiveDistributionAndMakeSelection(null);
            updateHiveVersionAndMakeSelection(null, null);
            updateHiveServerAndMakeSelection(null, null);
            updateHiveModeAndMakeSelection(null);
        }
        doHiveModeModify();

        if (isHiveEmbeddedMode()) {
            // Hadoop properties
            String nameNodeURLstr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            String jobTrackerURLStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            String hadoopUserName = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USERNAME);

            // Parameters for connecting to metastore.

            // EDatabaseTypeName.HIVE.getDisplayName()
            String metastoreConnURLStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_URL);
            String metastoreConnUserNameStr = connection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_USERNAME);
            String metastoreConnPasswordStr = connection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_PASSWORD);
            String metastoreConnDriverJarStr = connection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_DRIVER_JAR);
            String metastoreConnDriverNameStr = connection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_DRIVER_NAME);

            nameNodeURLTxt.setText(nameNodeURLstr == null ? "" : nameNodeURLstr); //$NON-NLS-1$
            jobTrackerURLTxt.setText(jobTrackerURLStr == null ? "" : jobTrackerURLStr); //$NON-NLS-1$
            usernameText.setText(hadoopUserName == null ? "" : hadoopUserName); //$NON-NLS-1$
            metastoreConnURLTxt.setText(metastoreConnURLStr == null ? "" : metastoreConnURLStr); //$NON-NLS-1$
            metastoreConnUserName.setText(metastoreConnUserNameStr == null ? "" : metastoreConnUserNameStr); //$NON-NLS-1$
            metastoreConnPassword.setText(metastoreConnPasswordStr == null ? "" : metastoreConnPasswordStr); //$NON-NLS-1$
            metastoreConnDriverJar.setText(metastoreConnDriverJarStr == null ? "" : metastoreConnDriverJarStr); //$NON-NLS-1$
            metastoreConnDriverName.setText(metastoreConnDriverNameStr == null ? "" : metastoreConnDriverNameStr); //$NON-NLS-1$
        }

        String useKrb = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
        String hivePrincipla = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
        String metastoreUrl = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
        String driverJarPath = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERJAR_PATH);
        String driverClass = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
        String username = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
        String password = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
        String useKeytabString = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB);
        String Principla = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        String keytab = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(Principla)) {
            Principla = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
        }
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(keytab)) {
            keytab = (String) metadataconnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
        }
        String additionalJDBCSettings = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS);
        boolean useSSL = Boolean.parseBoolean(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_SSL));
        String trustStorePathStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
        String trustStorePasswordStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);

        if (Boolean.valueOf(useKrb)) {
            useKerberos.setSelection(true);
            GridData hadoopData = (GridData) authenticationCom.getLayoutData();
            hadoopData.exclude = false;
            authenticationCom.setVisible(true);
            authenticationCom.setLayoutData(hadoopData);
            authenticationCom.getParent().layout();
            authenticationGrp.layout();
            authenticationGrp.getParent().layout();
        }
        hivePrincipalTxt.setText(hivePrincipla == null ? "" : hivePrincipla);
        metastoreUrlTxt.setText(metastoreUrl == null ? "" : metastoreUrl);
        driverJarTxt.setText(driverJarPath == null ? "" : driverJarPath);
        driverClassTxt.setText(driverClass == null ? "" : driverClass);
        usernameTxt.setText(username == null ? "" : username);
        passwordTxt.setText(password == null ? "" : password);
        additionalJDBCSettingsText.setText(additionalJDBCSettings == null ? "" : additionalJDBCSettings);
        useSSLEncryption.setSelection(useSSL);
        trustStorePath.setText(trustStorePathStr == null ? "" : trustStorePathStr);
        if (trustStorePasswordStr == null) {
            trustStorePasswordStr = "";
        } else {
            trustStorePasswordStr = connection.getValue(trustStorePasswordStr, false);
        }
        trustStorePassword.setText(trustStorePasswordStr);
        if (Boolean.valueOf(useKeytabString)) {
            useKeyTab.setSelection(true);
            GridData hadoopData = (GridData) keyTabComponent.getLayoutData();
            hadoopData.exclude = false;
            keyTabComponent.setVisible(true);
            keyTabComponent.setLayoutData(hadoopData);
            keyTabComponent.getParent().layout();
            authenticationGrp.layout();
            authenticationGrp.getParent().layout();
        }
        principalTxt.setText(Principla == null ? "" : Principla);
        keytabTxt.setText(keytab == null ? "" : keytab);

        String executionEngine = connection.getParameters().get(ConnParameterKeys.HIVE_EXECUTION_ENGINE);
        EHiveExecutionTypes executionType = EHiveExecutionTypes.getTypeFromValue(executionEngine);
        if (executionType != null) {
            hiveExecutionEngineCombo.setText(executionType.getLabel());
        } else {
            getConnection().getParameters().put(ConnParameterKeys.HIVE_EXECUTION_ENGINE, null);
        }

        //
        boolean doSupportMapRTicket = false;
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && hiveDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(hiveDistribution.getHDVersion(hiveVersion, false));
        }
        String useMaprTForHiveString = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USE_MAPRTICKET);
        String maprTUsernameForHive = connection.getParameters()
                .get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTUsernameForHive)) {
            maprTUsernameForHive = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
        }
        String maprTPasswordForHive = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTPasswordForHive)) {
            maprTPasswordForHive = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
        }
        String maprTClusterForHive = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTClusterForHive)) {
            maprTClusterForHive = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
        }
        String maprTDurationForHive = connection.getParameters().get(
                ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
        if (!connection.isContextMode() && ContextParameterUtils.isContainContextParam(maprTDurationForHive)) {
            maprTDurationForHive = (String) metadataconnection
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
        }
        boolean checkMaprTForHive = Boolean.valueOf(useMaprTForHiveString);
        useMaprTForHive.setSelection(checkMaprTForHive);
        if (checkMaprTForHive) {
            maprTUsernameForHiveTxt.setText(StringUtils.trimToEmpty(maprTUsernameForHive));
            maprTPasswordForHiveTxt.setText(StringUtils.trimToEmpty(maprTPasswordForHive));
            maprTClusterForHiveTxt.setText(StringUtils.trimToEmpty(maprTClusterForHive));
            maprTDurationForHiveTxt.setText(maprTDurationForHive == null ? "86400" : maprTDurationForHive.trim()); //$NON-NLS-1$
        }
        hideControl(useMaprTForHive, !doSupportMapRTicket);
        hideControl(authenticationMaprTComForHive, !(checkMaprTForHive && doSupportMapRTicket));
        hideControl(authenticationUserPassComForHive, Boolean.valueOf(useKrb) && doSupportMapRTicket);
        authenticationGrp.layout();
        authenticationGrp.getParent().layout();

        // String hadoopProperties =
        // getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
        // try {
        // propertiesTableViewForHive.getExtendedTableModel().registerDataList(
        // HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties));
        // } catch (JSONException e) {
        // ExceptionHandler.process(e);
        // }

        updateYarnStatus();

        updateYarnInfo(hiveDistribution, hdVersion);
        showIfSupportEncryption();
        updateSSLEncryptionDetailsDisplayStatus();
    }

    protected void initOracleCustomEncryptionInfo() {
        DatabaseConnection connection = getConnection();
        boolean useSSL = Boolean.parseBoolean(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_SSL));
        String trustStorePathStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH);
        String trustStorePasswordStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD);
        useSSLEncryption.setSelection(useSSL);
        trustStorePath.setText(trustStorePathStr == null ? "" : trustStorePathStr); //$NON-NLS-1$
        if (trustStorePasswordStr == null) {
            trustStorePasswordStr = ""; //$NON-NLS-1$
        } else {
            trustStorePasswordStr = connection.getValue(trustStorePasswordStr, false);
        }
        trustStorePassword.setText(trustStorePasswordStr);

        boolean sslClientAuth = Boolean
                .parseBoolean(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NEED_CLIENT_AUTH));
        String keyStorePathStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH);
        String keyStorePasswordStr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD);
        needClientAuth.setSelection(sslClientAuth);
        keyStorePath.setText(keyStorePathStr == null ? "" : keyStorePathStr); //$NON-NLS-1$
        if (keyStorePasswordStr == null) {
            keyStorePasswordStr = ""; //$NON-NLS-1$
        } else {
            keyStorePasswordStr = connection.getValue(keyStorePasswordStr, false);
        }
        keyStorePassword.setText(keyStorePasswordStr);

        boolean disableCBC = Boolean
                .parseBoolean(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_DISABLE_CBC_PROTECTION));
        disableCBCProtection.setSelection(disableCBC);
        showIfSupportEncryption();
        updateSSLEncryptionDetailsDisplayStatus();
    }

    private void updateYarnStatus() {
        DatabaseConnection connection = getConnection();
        String useYarn = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_YARN);
        useYarnButton.setSelection(Boolean.valueOf(useYarn));
    }

    /**
     * Registers a listener for the combo of Hive Mode. Added by Marvin Wang on Aug. 3, 2012.
     */
    protected void regHiveCombosListener() {
        regHiveDistributionComboListener();
        regHiveVersionComboListener();
        regHiveModeComboListener();
    }

    /**
     * Registers all listeners for the widgets of hive related. Added by Marvin Wang on Oct 17, 2012.
     */
    protected void regHiveRelatedWidgetsListeners() {
        regHiveCombosListener();
        regHiveRelatedWidgetNameNodeURLTxtListener();
        regHiveRelatedWidgetJobTrackerTxtListener();
        regHiveRelatedWidgetHiveServerComboListener();
        regHiveCustomBtnListener();
        regUseYarnBtnListener();
        // regHiveRelatedWidgetMetastoreConnURLListener();
        // regHiveRelatedWidgetMetastoreConnUserNameListener();
        // regHiveRelatedWidgetMetastoreConnPasswordListener();
        // regHiveRelatedWidgetMetastoreConnDriverJarListener();
        // regHiveRelatedWidgetMetastoreConnDriverNameListener();
        // regHiveRelatedWidgetMetastoreConnDriverJarBrowserListener();
        // regHiveMetastoreServerTxtListener();
        // regHiveMetastorePortTxtListener();
    }

    private void regHiveCustomBtnListener() {
        hiveCustomButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleHadoopCustomVersion(ECustomVersionType.HIVE);
            }
        });
    }

    /**
     * Registers a selection listener for the combo of distribution. It invokes {@link #doHiveDistributionModify()} when
     * an item is selected. Added by Marvin Wang on Aug 15, 2012.
     */
    private void regHiveDistributionComboListener() {
        hiveDistributionCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doHiveDistributionModify();
                showIfAdditionalJDBCSettings();
                showIfSupportEncryption();
                showIfAuthentication();
                hideHiveExecutionFields(!doSupportTez());
            }
        });
    }

    /**
     * Registers a selection listener for hive version combo. It invokes {@link #doHiveVersionModify()} when an item is
     * selected. Added by Marvin Wang on Aug 15, 2012.
     */
    private void regHiveVersionComboListener() {
        hiveVersionCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doHiveVersionModify();
                showIfAdditionalJDBCSettings();
                showIfSupportEncryption();
                showIfAuthentication();
                hideHiveExecutionFields(!doSupportTez());
            }
        });
    }

    private void regUseYarnBtnListener() {
        useYarnButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doUseYarnModify();
            }
        });
    }

    /**
     * Registers a selection listener for hive mode combo. It invokes {@link #doHiveModeModify()} when an item is
     * selected. Added by Marvin Wang on Aug 15, 2012.
     */
    private void regHiveModeComboListener() {
        hiveModeCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doHiveModeModify();
                showIfAdditionalJDBCSettings();
                showIfSupportEncryption();
                showIfAuthentication();
                hideHiveExecutionFields(!doSupportTez());
            }
        });
    }

    /**
     * Registers a listener for the text widget of name node url, it invokes {@link #doNameNodeModify()} when the text
     * contents is changed. Added by Marvin Wang on Oct 17, 2012.
     */
    private void regHiveRelatedWidgetNameNodeURLTxtListener() {
        nameNodeURLTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doNameNodeModify();
            }
        });
    }

    /**
     * Registers a listener for the text widget of job tracker, it invokes {@link #doJobTrackerModify()()} when the text
     * contents is changed. Added by Marvin Wang on Oct 17, 2012.
     */
    private void regHiveRelatedWidgetJobTrackerTxtListener() {
        jobTrackerURLTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doJobTrackerModify();
            }
        });
    }

    private void regHiveRelatedWidgetHiveServerComboListener() {
        hiveServerVersionCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doHiveServerSelected();
                showIfAdditionalJDBCSettings();
                showIfSupportEncryption();
                showIfAuthentication();
            }
        });
    }

    /**
     * Registers a listener for the text widget of metastore connection url, it invokes
     * {@link #doMetastoreConnURLModify()()} when the text contents is changed. Added by Marvin Wang on Oct 17, 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnURLListener() {
        metastoreConnURLTxt.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doMetastoreConnURLModify();
            }
        });
    }

    /**
     * Registers a listener for the text widget of metastore connection user name, it invokes
     * {@link #doMetastoreConnUserNameModify()()} when the text contents is changed. Added by Marvin Wang on Oct 17,
     * 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnUserNameListener() {
        metastoreConnUserName.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doMetastoreConnUserNameModify();
            }
        });
    }

    /**
     * Registers a listener for the text widget of metastore connection password, it invokes
     * {@link #doMetastoreConnPasswordModify()()} when the text contents is changed. Added by Marvin Wang on Oct 17,
     * 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnPasswordListener() {
        metastoreConnPassword.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doMetastoreConnPasswordModify();
            }
        });
    }

    /**
     * Registers a listener for the text widget of metastore connection driver jar, it invokes
     * {@link #doMetastoreConnDriverJarModify()()} when the text contents is changed. Added by Marvin Wang on Oct 17,
     * 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnDriverJarListener() {
        metastoreConnDriverJar.getTextControl().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doMetastoreConnDriverJarModify();
            }
        });
    }

    /**
     * 
     * Added by Marvin Wang on Oct 18, 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnDriverJarBrowserListener() {
        metastoreJarFilesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                SelectDatabaseJarDialog dialog = new SelectDatabaseJarDialog(getShell(), metastoreConnDriverJar.getText());
                if (dialog.open() == Window.OK) {
                    metastoreConnDriverJar.setText(dialog.getJarsString());
                }

                // generalJdbcClassNameText.removeAll();
                for (String stringToFile : metastoreConnDriverJar.getText().trim().split(";")) { //$NON-NLS-1$
                    File file = new File(stringToFile);
                    if (file != null) {
                        try {
                            ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
                            if (currentContextClassLoader instanceof HotClassLoader) {
                                HotClassLoader hotClassLoader = (HotClassLoader) currentContextClassLoader;
                                hotClassLoader.addPath(file.getPath());
                            }
                            // MyURLClassLoader cl = new MyURLClassLoader(file.toURL());
                            // Class[] classes = cl.getAssignableClasses(Driver.class);
                            // for (int i = 0; i < classes.length; ++i) {
                            // generalJdbcClassNameText.add(classes[i].getName());
                            // }
                        } catch (Exception ex) {
                            ExceptionHandler.process(ex);
                        }
                    }
                }
                // if (generalJdbcClassNameText.getItemCount() > 0) {
                // String driverClassName = generalJdbcClassNameText.getItem(0);
                // generalJdbcClassNameText.setText(driverClassName);
                // }
            }

        });
    }

    /**
     * Registers a listener for the text widget of metastore connection driver name, it invokes
     * {@link #doMetastoreConnDriverNameModify()} when the text contents is changed. Added by Marvin Wang on Oct 17,
     * 2012.
     */
    private void regHiveRelatedWidgetMetastoreConnDriverNameListener() {
        metastoreConnDriverName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                doMetastoreConnDriverNameModify();
            }
        });
    }

    /**
     * This method is invoked when an item of Hive mode is selected. If the selected is Embedded, it invokes
     * {@link #handleEmbeddedMode()}, {@link #handleStandaloneMode()} otherwise. Then it invokes the method
     * {@link #doUpdateConnection()} to update connection. Added by Marvin Wang on Aug. 3, 2012.
     */
    protected void doHiveModeModify() {
        boolean isEmbeddedMode = isHiveEmbeddedMode();
        getConnection().setURL(getStringConnection());
        if (isEmbeddedMode) {
            urlConnectionStringText.setText(DbConnStrForHive.URL_HIVE_2_TEMPLATE);
            // handleEmbeddedMode();
            handleUIWhenEmbeddedModeSelected();
        } else {
            // handleStandaloneMode();
            handleUIWhenStandaloneModeSelected();
        }
        fillDefaultsWhenHiveModeChanged(isEmbeddedMode);
        // }
        // TDQ-6407~

        doUpdateConnection();
    }

    /**
     * 
     * Added by Marvin Wang on Aug 10, 2012.
     */
    protected void doHiveDistributionModify() {
        IHDistribution newHiveDistribution = getCurrentHiveDistribution(false);
        if (newHiveDistribution == null) {
            return;
        }
        String distributionObj = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        IHDistribution originalHiveDistribution = HiveMetadataHelper.getDistribution(distributionObj, false);
        //
        boolean doSupportMapRTicket = false;
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && newHiveDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(newHiveDistribution.getDefaultVersion());
        }
        hideControl(useMaprTForHive, !doSupportMapRTicket);
        hideControl(authenticationMaprTComForHive, !(useMaprTForHive.getSelection() && doSupportMapRTicket));
        hideControl(authenticationUserPassComForHive, useKerberos.getSelection() && doSupportMapRTicket);
        authenticationGrp.layout();
        authenticationGrp.getParent().layout();
        if (originalHiveDistribution == null || !newHiveDistribution.equals(originalHiveDistribution)) {
            // 1. To update Hive Version List and make a default selection. 2. To do the same as Hive Version list
            // for Hive mode. 3. To update connection parameters.
            updateHiveVersionAndMakeSelection(newHiveDistribution, null);
            setHideVersionInfoWidgets(false);
            updateYarnInfo(newHiveDistribution, null);
            updateHiveModeAndMakeSelection(null);
            doHiveModeModify();
            fillDefaultsWhenHiveVersionChanged();
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doHiveVersionModify() {
        IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
        if (hiveDistribution == null) {
            return;
        }
        IHDistributionVersion newVersion = hiveDistribution.getHDVersion(hiveVersionCombo.getText(), true);
        if (newVersion == null) {
            return;
        }
        String versionStr = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        IHDistributionVersion originalVersion = hiveDistribution.getHDVersion(versionStr, true);
        if (originalVersion == null || !newVersion.equals(originalVersion)) {
            setHideVersionInfoWidgets(false);
            updateHiveModeAndMakeSelection(null);
            updateHiveServerAndMakeSelection(hiveDistribution, newVersion);
            updateYarnInfo(hiveDistribution, newVersion);
            doHiveModeModify();
            fillDefaultsWhenHiveVersionChanged();
        }
    }

    private void fillDefaultsWhenHiveVersionChanged() {
        if (isCreation && isNeedFillDefaults()) {
            String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
            String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
            if (distribution == null) {
                return;
            }
            IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
            if (hadoopDistributionService == null) {
                return;
            }
            IHDistribution hiveDistribution = hadoopDistributionService.getHiveDistributionManager().getDistribution(
                    distribution, false);
            if (hiveDistribution == null) {
                return;
            }
            IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(version, false);
            if (hiveVersion == null) {
                return;
            }
            boolean useYarn = Boolean.valueOf(getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_YARN));
            String defaultNN = hiveVersion.getDefaultConfig(distribution, EHadoopProperties.NAMENODE_URI.getName());
            String nameNodeURLstr = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            String jobTrackerURLStr = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            String hiveKerberosPrin = getConnection().getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            if (StringUtils.isNotEmpty(nameNodeURLstr)) {
                nameNodeURLTxt.setText(nameNodeURLstr);
            } else if (defaultNN != null) {
                nameNodeURLTxt.setText(defaultNN);
            }
            String defaultJTORRM = null;
            if (useYarn) {
                defaultJTORRM = hiveVersion.getDefaultConfig(distribution, EHadoopProperties.RESOURCE_MANAGER.getName());
            } else {
                defaultJTORRM = hiveVersion.getDefaultConfig(distribution, EHadoopProperties.JOBTRACKER.getName());
            }
            if (StringUtils.isNotEmpty(jobTrackerURLStr)) {
                jobTrackerURLTxt.setText(jobTrackerURLStr);
            } else if (defaultJTORRM != null) {
                jobTrackerURLTxt.setText(defaultJTORRM);
            }

            String defaultPrincipal = hiveVersion.getDefaultConfig(distribution, EHadoopCategory.HIVE.getName(),
                    EHadoopProperties.HIVE_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(hiveKerberosPrin)) {
                hivePrincipalTxt.setText(hiveKerberosPrin);
            } else if (defaultPrincipal != null) {
                hivePrincipalTxt.setText(defaultPrincipal);
            }
            String defaultDatabase = hiveVersion.getDefaultConfig(distribution, EHadoopCategory.HIVE.getName(),
                    EHadoopProperties.DATABASE.getName());
            if (StringUtils.isNotEmpty(getConnection().getSID())) {
                sidOrDatabaseText.setText(getConnection().getSID());
            } else if (defaultDatabase != null) {
                sidOrDatabaseText.setText(defaultDatabase);
                getConnection().setSID(defaultDatabase);
            }
            // default values for mapr ticket
            String maprticket_Cluster = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            String defaultMaprticket_Cluster = hiveVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_CLUSTER.getName());
            if (StringUtils.isNotEmpty(maprticket_Cluster)) {
                maprTClusterForHiveTxt.setText(maprticket_Cluster);
            } else if (defaultMaprticket_Cluster != null) {
                maprTClusterForHiveTxt.setText(defaultMaprticket_Cluster);
            }
            String maprticket_Duration = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
            String defaultMaprticket_Duration = hiveVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_DURATION.getName());
            if (StringUtils.isNotEmpty(maprticket_Duration)) {
                maprTDurationForHiveTxt.setText(maprticket_Duration);
            } else if (defaultMaprticket_Duration != null) {
                maprTDurationForHiveTxt.setText(defaultMaprticket_Duration);
            }
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            if (template != null) {
                serverText.setText(template.getDefaultServer(null));
            }
        }
    }

    private void fillDefaultsWhenHBaseVersionChanged() {
        if (isCreation && isNeedFillDefaults()) {
            String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
            String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
            String hbaseMasterPrincipal = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
            String hbaseRSPrincipal = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            if (distribution == null) {
                return;
            }
            IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
            if (hadoopDistributionService == null) {
                return;
            }
            IHDistribution hbaseDistribution = hadoopDistributionService.getHBaseDistributionManager().getDistribution(
                    distribution, false);
            if (hbaseDistribution == null) {
                return;
            }
            IHDistributionVersion hbaseVersion = hbaseDistribution.getHDVersion(version, false);
            if (hbaseVersion == null) {
                return;
            }
            String defaultPort = hbaseVersion.getDefaultConfig(distribution, EHadoopCategory.HBASE.getName(),
                    EHadoopProperties.PORT.getName());
            if (defaultPort != null && !isContextMode()) {
                getConnection().setPort(defaultPort);
                portText.setText(defaultPort);
            }

            String tableNSMapping = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            String defaultTableNSMapping = hbaseVersion.getDefaultConfig(distribution, EHadoopCategory.HBASE.getName(),
                    EHadoopProperties.HBASE_TABLE_NS_MAPPING.getName());
            if (StringUtils.isNotEmpty(tableNSMapping)) {
                tableNSMappingOfHbaseTxt.setText(tableNSMapping);
            } else if (defaultTableNSMapping != null) {
                tableNSMappingOfHbaseTxt.setText(defaultTableNSMapping);
            }

            String defaultHbaseMasterPrincipal = hbaseVersion.getDefaultConfig(distribution, EHadoopCategory.HBASE.getName(),
                    EHadoopProperties.HBASE_MASTER_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(hbaseMasterPrincipal)) {
                hbaseMasterPrincipalTxt.setText(hbaseMasterPrincipal);
            } else if (defaultHbaseMasterPrincipal != null) {
                hbaseMasterPrincipalTxt.setText(defaultHbaseMasterPrincipal);
            }

            String defaultHbaseRSPrincipal = hbaseVersion.getDefaultConfig(distribution, EHadoopCategory.HBASE.getName(),
                    EHadoopProperties.HBASE_REGIONSERVER_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(hbaseRSPrincipal)) {
                hbaseRSPrincipalTxt.setText(hbaseRSPrincipal);
            } else if (defaultHbaseRSPrincipal != null) {
                hbaseRSPrincipalTxt.setText(defaultHbaseRSPrincipal);
            }

            // default values for mapr ticket
            String maprticket_Cluster = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            String defaultMaprticket_Cluster = hbaseVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_CLUSTER.getName());
            if (StringUtils.isNotEmpty(maprticket_Cluster)) {
                maprTClusterForHBaseTxt.setText(maprticket_Cluster);
            } else if (defaultMaprticket_Cluster != null) {
                maprTClusterForHBaseTxt.setText(defaultMaprticket_Cluster);
            }
            String maprticket_Duration = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
            String defaultMaprticket_Duration = hbaseVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_DURATION.getName());
            if (StringUtils.isNotEmpty(maprticket_Duration)) {
                maprTDurationForHBaseTxt.setText(maprticket_Duration);
            } else if (defaultMaprticket_Duration != null) {
                maprTDurationForHBaseTxt.setText(defaultMaprticket_Duration);
            }
        }
    }

    private void fillDefaultsWhenMaprdbVersionChanged() {
        if (isCreation && isNeedFillDefaults()) {
            String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
            String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION);
            String maprdbMasterPrincipal = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
            String maprdbRSPrincipal = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            if (distribution == null) {
                return;
            }
            IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
            if (hadoopDistributionService == null) {
                return;
            }
            IHDistribution maprdbDistribution = hadoopDistributionService.getMaprdbDistributionManager().getDistribution(
                    distribution, false);
            if (maprdbDistribution == null) {
                return;
            }
            IHDistributionVersion maprdbVersion = maprdbDistribution.getHDVersion(version, false);
            if (maprdbVersion == null) {
                return;
            }
            String defaultPort = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(),
                    EHadoopProperties.PORT.getName());
            if (defaultPort != null && !isContextMode()) {
                getConnection().setPort(defaultPort);
                portText.setText(defaultPort);
            }

            String tableNSMapping = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            String defaultTableNSMapping = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(),
                    EHadoopProperties.MAPRDB_TABLE_NS_MAPPING.getName());
            if (StringUtils.isNotEmpty(tableNSMapping)) {
                tableNSMappingOfMapRDBTxt.setText(tableNSMapping);
            } else if (defaultTableNSMapping != null) {
                tableNSMappingOfMapRDBTxt.setText(defaultTableNSMapping);
            }

            String defaultHbaseMasterPrincipal = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(),
                    EHadoopProperties.MAPRDB_MASTER_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(maprdbMasterPrincipal)) {
                maprdbMasterPrincipalTxt.setText(maprdbMasterPrincipal);
            } else if (defaultHbaseMasterPrincipal != null) {
                maprdbMasterPrincipalTxt.setText(defaultHbaseMasterPrincipal);
            }

            String defaultHbaseRSPrincipal = maprdbVersion.getDefaultConfig(distribution, EHadoopCategory.MAPRDB.getName(),
                    EHadoopProperties.MAPRDB_REGIONSERVER_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(maprdbRSPrincipal)) {
                maprdbRSPrincipalTxt.setText(maprdbRSPrincipal);
            } else if (defaultHbaseRSPrincipal != null) {
                maprdbRSPrincipalTxt.setText(defaultHbaseRSPrincipal);
            }

            // default values for mapr ticket
            String maprticket_Cluster = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
            String defaultMaprticket_Cluster = maprdbVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_CLUSTER.getName());
            if (StringUtils.isNotEmpty(maprticket_Cluster)) {
                maprTClusterForMaprdbTxt.setText(maprticket_Cluster);
            } else if (defaultMaprticket_Cluster != null) {
                maprTClusterForMaprdbTxt.setText(defaultMaprticket_Cluster);
            }
            String maprticket_Duration = getConnection().getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
            String defaultMaprticket_Duration = maprdbVersion.getDefaultConfig(distribution,
                    EHadoopProperties.MAPRTICKET_DURATION.getName());
            if (StringUtils.isNotEmpty(maprticket_Duration)) {
                maprTDurationForMaprdbTxt.setText(maprticket_Duration);
            } else if (defaultMaprticket_Duration != null) {
                maprTDurationForMaprdbTxt.setText(defaultMaprticket_Duration);
            }
        }
    }

    private void fillDefaultsWhenImpalaVersionChanged() {
        if (isCreation && isNeedFillDefaults()) {
            String distribution = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            String version = getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);
            if (distribution == null) {
                return;
            }
            EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(getConnection().getDatabaseType());
            if (template != null) {
                portText.setText(template.getDefaultPort());
                serverText.setText(template.getDefaultServer(null));
                sidOrDatabaseText.setText(template.getDefaultDB(null));
            }
            initImpalaInfo();
        }
    }

    private void updateYarnInfo(IHDistribution hiveDistribution, IHDistributionVersion hiveVersion) {
        if (hiveDistribution == null) {
            hiveDistribution = getCurrentHiveDistribution(true);
        }
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hiveDistribution == null || hadoopDistributionService == null) {
            return;
        }
        IHDistributionVersion[] hdVersions = hiveDistribution.getHDVersions();
        if (hiveVersion == null && hdVersions.length > 0) {
            hiveVersion = hdVersions[0];
        }

        boolean support = false;
        try {
            support = hadoopDistributionService.doSupportMethod(hiveVersion, "isHadoop2"); //$NON-NLS-1$
        } catch (Exception e) {
            // ignore
        }

        if (support) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_YARN, String.valueOf(Boolean.TRUE));
        }
        updateJobtrackerContent();
    }

    protected void doUseYarnModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_USE_YARN,
                    String.valueOf(useYarnButton.getSelection()));
            updateJobtrackerContent();
        }
    }

    private void updateJobtrackerContent() {
        boolean useYarn = Boolean.valueOf(getConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_YARN));
        jobTrackerURLTxt.setLabelText(useYarn ? Messages.getString("DatabaseForm.resourceManager") //$NON-NLS-1$
                : Messages.getString("DatabaseForm.hiveEmbedded.jobTrackerURL")); //$NON-NLS-1$
        hadoopPropGrp.layout();
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doNameNodeModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL, nameNodeURLTxt.getText());
            checkFieldsValue();
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doJobTrackerModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL, jobTrackerURLTxt.getText());
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doMetastoreConnURLModify() {
        if (!isContextMode()) {
            getConnection().getParameters()
                    .put(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_URL, metastoreConnURLTxt.getText());
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doMetastoreConnUserNameModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_USERNAME,
                    metastoreConnUserName.getText());
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doMetastoreConnPasswordModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_PASSWORD,
                    metastoreConnPassword.getText());
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doMetastoreConnDriverJarModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_DRIVER_JAR,
                    metastoreConnDriverJar.getText());
        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doMetastoreConnDriverNameModify() {
        if (!isContextMode()) {
            getConnection().getParameters().put(ConnParameterKeys.CONN_PARA_KEY_METASTORE_CONN_DRIVER_NAME,
                    metastoreConnDriverName.getText());
        }
    }

    protected void doHiveServerSelected() {
        if (!isContextMode()) {
            modifyFieldValue();
            getConnection().getParameters().put(ConnParameterKeys.HIVE_SERVER_VERSION,
                    HiveServerVersionInfo.getByDisplay(hiveServerVersionCombo.getText()).getKey());
        }

        IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
        IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(hiveVersionCombo.getText(), true);
        updateHiveModeAndMakeSelection(null);
        updateHiveServerAndMakeSelection(hiveDistribution, hiveVersion);
    }

    protected void updateHiveDistributionAndMakeSelection(IHDistribution hiveDistribution) {
        hiveDistributionCombo.getCombo().setItems(HiveMetadataHelper.getDistributionsDisplay());
        if (hiveDistribution != null) {
            hiveDistributionCombo.setText(hiveDistribution.getDisplayName());
        } else {
            hiveDistributionCombo.select(0);
        }
    }

    private IHDistribution getCurrentHiveDistribution(boolean withDefault) {
        IHDistribution hiveDistribution = HiveMetadataHelper.getDistribution(hiveDistributionCombo.getText(), true);
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (withDefault && hiveDistribution == null && hadoopService != null) {
            IHDistribution[] distributions = hadoopService.getHiveDistributionManager().getDistributions();
            if (distributions.length > 0) {
                hiveDistribution = distributions[0];
            }
        }
        return hiveDistribution;
    }

    protected void updateHiveVersionAndMakeSelection(IHDistribution hiveDistribution, IHDistributionVersion hiveVersion) {
        if (hiveDistribution == null) {
            hiveDistribution = getCurrentHiveDistribution(true);
        }
        if (hiveDistribution == null) {
            return;
        }
        hiveVersionCombo.getCombo()
                .setItems(HiveMetadataHelper.getDistributionVersionsDisplay(hiveDistribution.getName(), false));
        if (hiveVersion != null && hiveVersion.getDisplayVersion() != null) {
            hiveVersionCombo.setText(hiveVersion.getDisplayVersion());
        } else {
            hiveVersionCombo.select(0);
        }

        updateHiveServerAndMakeSelection(hiveDistribution, hiveVersion);
    }

    protected void updateHiveModeAndMakeSelection(HiveModeInfo hiveMode) {
        IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hiveDistribution == null || hadoopDistributionService == null) {
            return;
        }
        IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(hiveVersionCombo.getText(), true);
        IHDistributionVersion[] hdVersions = hiveDistribution.getHDVersions();
        if (hiveVersion == null && hdVersions.length > 0) {
            hiveVersion = hdVersions[0];
        }
        HiveServerVersionInfo hiveServer = HiveServerVersionInfo.HIVE_SERVER_1;
        if (doSupportHive2()) {
            hiveServer = HiveServerVersionInfo.getByDisplay(hiveServerVersionCombo.getText());
        }
        String[] hiveModesDisplay = HiveMetadataHelper.getHiveModesDisplay(hiveDistribution.getName(), hiveVersion == null ? null
                : hiveVersion.getVersion(), hiveServer == null ? null : hiveServer.getKey(), false);
        hiveModeCombo.getCombo().setItems(hiveModesDisplay);

        if (hiveMode != null) {
            hiveModeCombo.setText(hiveMode.getDisplayName());
        } else {
            hiveModeCombo.select(0);
        }
    }

    /**
     * Makes sure if the hive server2 UI displays. If displaying it, then check the selection. Added by Marvin Wang on
     * Mar 25, 2013.
     * 
     * @param distributionIndex
     * @param hiveVersionIndex
     */
    protected void updateHiveServerAndMakeSelection(IHDistribution hiveDistribution, IHDistributionVersion hiveVersion) {
        if (hiveDistribution == null) {
            hiveDistribution = getCurrentHiveDistribution(true);
        }
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hiveDistribution == null || hadoopDistributionService == null) {
            return;
        }
        IHDistributionVersion[] hdVersions = hiveDistribution.getHDVersions();
        if (hiveVersion == null && hdVersions.length > 0) {
            hiveVersion = hdVersions[0];
        }

        DatabaseConnection conn = getConnection();
        String[] hiveServersDisplay = HiveMetadataHelper.getHiveServersDisplay(hiveDistribution.getName(),
                hiveVersion == null ? null : hiveVersion.getVersion(), false);
        hiveServerVersionCombo.getCombo().setItems(hiveServersDisplay);

        String hiveServerKey = conn.getParameters().get(ConnParameterKeys.HIVE_SERVER_VERSION);
        if (hiveServerKey != null) {
            HiveServerVersionInfo serverVersion = HiveServerVersionInfo.getByKey(hiveServerKey);
            if (serverVersion != null) {
                hiveServerVersionCombo.setText(serverVersion.getDisplayName());
            } else {
                hiveServerVersionCombo.select(0);
            }
        } else {
            hiveServerVersionCombo.select(0);
        }
    }

    /**
     * It is invoked when the mode STANDALONE is selected. Added by Marvin Wang on Aug. 3, 2012.
     */
    public void handleStandaloneMode() {
        urlConnectionStringText.setText(getConnection().getURL());
        // sidOrDatabaseText.show();
        // sidOrDatabaseText.setEditable(true);
        // usernameText.show();
        // passwordText.show();
        handleHiveRelatedWidgetsStatus(true);
        handleOtherWidgetsStatusForHive(false);

        doHiveUIContentsLayout();
        compositeDbSettings.layout();
        typeDbCompositeParent.layout();
        newParent.layout();
        compositeGroupDbSettings.layout();

    }

    protected void handleUIWhenEmbeddedModeSelected() {
        // handleHiveRelatedWidgetsStatus(true,false);
        // handleOtherWidgetsStatusForHive(true,true);
        handleOtherWidgetsStatusForHive(true);
        setHideHadoopInfoWidgets(false);
        setHideMetastoreInfoWidgets(true);
        doHiveUIContentsLayout();

        // fill Name Node URL and Resource Manager
        String nameNodeURLstr = ""; //$NON-NLS-1$
        String jobTrackerURLStr = ""; //$NON-NLS-1$
        DatabaseConnection connection = getConnection();
        if (connection != null) {
            EMap<String, String> parameters = connection.getParameters();
            if (parameters != null) {
                nameNodeURLstr = parameters.get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
                jobTrackerURLStr = parameters.get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            }
        }
        nameNodeURLTxt.setText(nameNodeURLstr);
        jobTrackerURLTxt.setText(jobTrackerURLStr);
    }

    protected void handleUIWhenStandaloneModeSelected() {
        handleOtherWidgetsStatusForHive(false);
        setHideHadoopInfoWidgets(true);
        setHideMetastoreInfoWidgets(true);
        doHiveUIContentsLayout();
    }

    private void fillDefaultsWhenHiveModeChanged(boolean isEmbeddedMode) {
        if (isCreation && isNeedFillDefaults()) {
            IHDistribution hiveDistribution = getCurrentHiveDistribution(false);
            if (hiveDistribution == null) {
                return;
            }
            IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(hiveVersionCombo.getText(), true);
            if (hiveVersion == null) {
                return;
            }

            String defaultPort = null;
            if (isEmbeddedMode) {
                defaultPort = hiveVersion.getDefaultConfig(hiveDistribution.getName(), EHadoopCategory.HIVE.getName(),
                        HiveModeInfo.EMBEDDED.getName(), EHadoopProperties.PORT.getName());
            } else {
                defaultPort = hiveVersion.getDefaultConfig(hiveDistribution.getName(), EHadoopCategory.HIVE.getName(),
                        HiveModeInfo.STANDALONE.getName(), EHadoopProperties.PORT.getName());
            }

            if (defaultPort != null && !isContextMode()) {
                getConnection().setPort(defaultPort);
                portText.setText(defaultPort);
            }
        }
    }

    /**
     * It is invoked when the mode EMBEDDED is selected. Added by Marvin Wang on Aug. 3, 2012.
     */
    public void handleEmbeddedMode() {
        urlConnectionStringText.setText(getConnection().getURL());
        // It should be reverted if emembedded is available. (--Done by Marvin Wang on Oct.15, 2012.)
        // sidOrDatabaseText.setVisible(true);
        // sidOrDatabaseText.setHideWidgets(true);
        // usernameText.setHideWidgets(true);
        // passwordText.setHideWidgets(true);
        handleHiveRelatedWidgetsStatus(false);
        handleOtherWidgetsStatusForHive(true);

        doHiveUIContentsLayout();
        compositeDbSettings.layout();
        typeDbCompositeParent.layout();
        newParent.layout();
        compositeGroupDbSettings.layout();
        scrolledComposite.layout();
    }

    private void doHiveUIContentsLayout() {
        hiveComposite.layout();
        hadoopPropGrp.layout();
        metastorePropGrp.layout();
        typeDbCompositeParent.layout();
        compositeDbSettings.layout();
        newParent.layout();
        compositeGroupDbSettings.layout();
        scrolledComposite.layout();
    }

    /**
     * When a db type is selected in DBTypeCombo, this method should be invoked. But before invoking this method, the
     * method {@link #initHiveInfo()} should be called first.
     */
    protected void doHiveDBTypeSelected() {
        setHideVersionInfoWidgets(false);
        // To identify what the current hive mode is, standalone or embedded, When the current DB type is hive.
        if (isHiveEmbeddedMode()) {
            // Showing UIs including hadoop property, metastaore property,version info. Hiding userName, password,
            // database, shcemas...
            handleUIWhenEmbeddedModeSelected();
        } else {
            // Hiding UIs including hadoop property, metastore property, Showing version info.
            handleUIWhenStandaloneModeSelected();
        }
    }

    protected void doHiveDBTypeNotSelected() {
        setHideVersionInfoWidgets(true);
        setHideHadoopInfoWidgets(true);
        setHideMetastoreInfoWidgets(true);
        doHiveUIContentsLayout();
    }

    /**
     * This method is used to handle the groups including version info, hadoop info and metastore info are hide or
     * visible. Added by Marvin Wang on Oct 16, 2012.
     * 
     * @param hide
     */
    private void handleHiveRelatedWidgetsStatus(boolean hide) {
        setHideHadoopInfoWidgets(hide);
        setHideMetastoreInfoWidgets(true);
    }

    /**
     * Invokes this method to handle the status of other widgets, for hive the following widgets need to handle: <li>The
     * text widget of userName, variable is <code>usernameText</code>.</li> <li>The text widget of password, variable is
     * <code>passwordText</code>.</li> <li>The text widget of database, variable is <code>sidOrDatabaseText</code>.</li>
     * <li>The text widget of schema, variable is <code>schemaText</code>.</li> All these will be hidden when the
     * current db type is <code>Hive</code> and the current hive mode is <code>STANDALONE</code>. Otherwise, all will be
     * visible when the current db type is <code>Hive</code> and the current hive mode is <code>EMBEDDED</code>. Added
     * by Marvin Wang on Oct 17, 2012.
     * 
     * @param hide
     */
    private void handleOtherWidgetsStatusForHive(boolean hide) {
        // server and port is no use for Hive Embedded mode.
        // Need to revert if required, changed by Marvin Wang on Nov. 22, 2012.
        // serverText.setHideWidgets(hide);
        // serverText.setEditable(!hide);
        // portText.setHideWidgets(hide);
        serverText.show();
        portText.show();
        usernameText.show();
        serverText.setEditable(true);
        portText.setEditable(true);
        passwordText.setHideWidgets(hide);
        sidOrDatabaseText.setHideWidgets(false);
        sidOrDatabaseText.setEditable(true);
        schemaText.setHideWidgets(true);
    }

    /**
     * Invokes this method to handle the widget status of hive version info related, hide or visible. Added by Marvin
     * Wang on Oct 16, 2012.
     * 
     * @param hide
     */
    private void setHideVersionInfoWidgets(boolean hide) {
        GridData hiveCompGD = (GridData) hiveComposite.getLayoutData();
        hiveComposite.setVisible(!hide);
        hiveCompGD.exclude = hide;
        hiveDistributionCombo.setHideWidgets(hide);
        hiveVersionCombo.setHideWidgets(hide);
        hiveModeCombo.setHideWidgets(hide);
        if (hide) {
            hiveCustomButton.setVisible(!hide);
            hiveServerVersionCombo.setHideWidgets(true);
        } else {
            GridData yarnCompGd = (GridData) yarnComp.getLayoutData();
            IHDistribution currentHiveDistribution = getCurrentHiveDistribution(false);
            if (currentHiveDistribution != null && currentHiveDistribution.useCustom()) {
                hiveVersionCombo.setHideWidgets(true);
                hiveModeCombo.setHideWidgets(false);
                hiveCustomButton.setVisible(true);
                hiveServerVersionCombo.setHideWidgets(false);
                yarnComp.setVisible(true);
                yarnCompGd.exclude = false;
            } else {
                if (doSupportHive2()) {
                    hiveServerVersionCombo.setHideWidgets(false);
                } else {
                    hiveServerVersionCombo.setHideWidgets(true);
                }
                hiveVersionCombo.setHideWidgets(false);
                hiveModeCombo.setHideWidgets(false);
                hiveCustomButton.setVisible(false);
                yarnComp.setVisible(false);
                yarnCompGd.exclude = true;
                useYarnButton.setSelection(false);
            }
            if (yarnComp.isVisible()) {
                doUseYarnModify();
            }
        }
    }

    /**
     * Invokes this method to make the widgets of hadoop info related hidden or visible. Widgets include text of name
     * node url, job tracker url. Added by Marvin Wang on Oct 17, 2012.
     * 
     * @param hide
     */
    private void setHideHadoopInfoWidgets(boolean hide) {
        // GridData hadoopPropGrpGD = (GridData)hadoopPropGrp.getLayoutData();
        // hadoopPropGrp.setVisible(!hide);
        // hadoopPropGrpGD.exclude = hide;
        // nameNodeURLTxt.setHideWidgets(hide);
        // jobTrackerURLTxt.setHideWidgets(hide);
        //
        GridData hadoopPropGrpGD = (GridData) hadoopPropGrp.getLayoutData();
        hadoopPropGrp.setVisible(!hide);
        hadoopPropGrpGD.exclude = hide;
        nameNodeURLTxt.setHideWidgets(hide);
        jobTrackerURLTxt.setHideWidgets(hide);
    }

    /**
     * Invokes this method to make the widgets of metastore connection related hidden or visible, including connection
     * URL, user name, password, driver jar directory and driver name. Added by Marvin Wang on Oct 17, 2012.
     * 
     * @param hide
     */
    private void setHideMetastoreInfoWidgets(boolean hide) {
        GridData metastorePropGrpGD = (GridData) metastorePropGrp.getLayoutData();
        metastorePropGrp.setVisible(!hide);
        metastorePropGrpGD.exclude = hide;
        metastoreConnUserName.setHideWidgets(hide);
        metastoreConnPassword.setHideWidgets(hide);
        metastoreConnURLTxt.setHideWidgets(hide);
        metastoreConnDriverJar.setHideWidgets(hide);
        metastoreConnDriverName.setHideWidgets(hide);
    }

    /**
     * Invokes this method when an existing connection is opened or a new db connection is created. For developer, if
     * you add any widgets, you have to add them here to be hide or visible.
     * 
     * Added by Marvin Wang on Oct 16, 2012.
     * 
     * @param hide
     */
    protected void doHideHiveWidgets(boolean hide) {
        GridData gridData = (GridData) hiveComposite.getLayoutData();
        hiveComposite.setVisible(!hide);
        gridData.exclude = hide;
        hiveDistributionCombo.setHideWidgets(hide);
        hiveVersionCombo.setHideWidgets(hide);
        hiveModeCombo.setHideWidgets(hide);
        // usernameText.setHideWidgets(hide);
        // passwordText.setHideWidgets(hide);
        // sidOrDatabaseText.setHideWidgets(hide);
        // schemaText.setHideWidgets(hide);

        // For hadoop properties.
        GridData hadoopPropGrpGD = (GridData) hadoopPropGrp.getLayoutData();
        hadoopPropGrp.setVisible(!hide);
        hadoopPropGrpGD.exclude = hide;
        nameNodeURLTxt.setHideWidgets(hide);
        jobTrackerURLTxt.setHideWidgets(hide);

        // For metastore connection parameters.
        GridData metastorePropGrpGD = (GridData) metastorePropGrp.getLayoutData();
        metastorePropGrp.setVisible(!hide);
        metastorePropGrpGD.exclude = hide;
        metastoreConnUserName.setHideWidgets(hide);
        metastoreConnPassword.setHideWidgets(hide);
        metastoreConnURLTxt.setHideWidgets(hide);
        metastoreConnDriverJar.setHideWidgets(hide);
        metastoreConnDriverName.setHideWidgets(hide);
    }

    /**
     * <pre>
     * Updates the parameter of Hive mode in connection. Put the following parameters in <code>DatabaseConnection</code>
     * .
     * <li>Hive distribution, the key is {@link ConnParameterKeys#CONN_PARA_KEY_HIVE_DISTRIBUTION}</li>
     * <li>Distro version, the key is {@link ConnParameterKeys#CONN_PARA_KEY_HIVE_VERSION}</li>
     * <li>Hive mode, the key is {@link ConnParameterKeys#CONN_PARA_KEY_HIVE_MODE}</li>
     * <li>Name node URL, the key is {@link ConnParameterKeys#CONN_PARA_KEY_NAME_NODE_URL}</li>
     * <li>Job Tracker URL, the key is {@link ConnParameterKeys#CONN_PARA_KEY_JOB_TRACKER_URL}</li>
     * <li>Hive Server version, the key is {@link ConnParameterKeys#HIVE_SERVER_VERSION}</li>
     */
    protected void doUpdateConnection() {
        if (!isContextMode()) {
            IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
            if (hiveDistribution == null) {
                return;
            }
            IHDistributionVersion hiveVersion = hiveDistribution.getHDVersion(hiveVersionCombo.getText(), true);
            HiveModeInfo hiveMode = HiveModeInfo.getByDisplay(hiveModeCombo.getText());
            HiveServerVersionInfo serverVersion = HiveServerVersionInfo.getByDisplay(hiveServerVersionCombo.getText());
            DatabaseConnection conn = getConnection();
            if (hiveMode != null) {
                conn.setDbVersionString(hiveMode.getName());
            }
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE, EDatabaseConnTemplate.HIVE.getDBTypeName());
            conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION, hiveDistribution.getName());
            if (hiveVersion != null) {
                conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION, hiveVersion.getVersion());
            }
            if (hiveMode != null) {
                conn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE, hiveMode.getName());
            }
            if (serverVersion != null) {
                conn.getParameters().put(ConnParameterKeys.HIVE_SERVER_VERSION, serverVersion.getKey());
            }

        }
    }

    /**
     * 
     * Added by Marvin Wang on Oct 17, 2012.
     */
    protected void doHivePreSetup(DatabaseConnection connection) {
        String dbType = getConnectionDBType();
        if (EDatabaseTypeName.HIVE.getDisplayName().equalsIgnoreCase(dbType)) {
            String hiveModeDisplayName = hiveModeCombo.getText();
            if (hiveModeDisplayName != null
                    && hiveModeDisplayName.equalsIgnoreCase(Messages.getString("DatabaseForm.hiveMode.embedded"))) { //$NON-NLS-1$

                JavaSqlFactory.doHivePreSetup(connection);
            }
        }
    }

    /**
     * When checking connection is complete, these hive properties need to remove from system property. Added by Marvin
     * Wang on Oct 17, 2012.
     */
    protected void doRemoveHiveSetup() {
        JavaSqlFactory.doHiveConfigurationClear();
    }

    /**
     * Indentifies the hive mode selected in hive mode combo is standalone or embedded. If embedded, return
     * <code>true</code>, <code>false</code> otherwise.
     * 
     * @return
     */
    protected boolean isHiveEmbeddedMode() {
        return HiveMetadataHelper.isHiveEmbeddedMode(getConnectionDBType(), hiveModeCombo.getText());
    }

    private boolean isHiveStandaloneMode() {
        return HiveMetadataHelper.isHiveStandaloneMode(getConnectionDBType(), hiveModeCombo.getText());
    }

    private boolean doSupportHive2() {
        return HiveMetadataHelper.doSupportHive2(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
    }

    private boolean isHiveExecutedThroughWebHCat() {
        return HiveMetadataHelper.isExecutedThroughWebHCat(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
    }

    private boolean doSupportHiveSSL() {
        return HiveMetadataHelper.doSupportSSL(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
    }

    private boolean doSupportHiveSSLwithKerberos() {
        return HiveMetadataHelper.doSupportSSLwithKerberos(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
    }

    private boolean doSupportHiveStandaloneMode() {
        return HiveMetadataHelper.doSupportStandaloneMode(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
    }

    private boolean doSupportSecurity() {
        return doSupportKerb() || doSupportTicket();
    }

    private boolean doSupportKerb() {
        return HiveMetadataHelper.doSupportSecurity(hiveDistributionCombo.getText(), hiveVersionCombo.getText(),
                hiveModeCombo.getText(), hiveServerVersionCombo.getText(), true);
    }

    private boolean doSupportTicket() {
        boolean doSupportMapRTicket = false;
        IHDistribution hiveDistribution = getCurrentHiveDistribution(true);
        if (hiveDistribution == null) {
            return false;
        }
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && hiveDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(hiveDistribution.getHDVersion(hiveVersionCombo.getText(),
                    true));
        }
        return doSupportMapRTicket;
    }

    private boolean doSupportMaprTicketForHbase() {
        boolean doSupportMapRTicket = false;
        IHDistribution newDistribution = getHBaseDistribution(hbaseDistributionCombo.getText(), true);
        if (newDistribution == null) {
            return false;
        }
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null && newDistribution != null) {
            doSupportMapRTicket = hadoopService.doSupportMapRTicket(newDistribution.getDefaultVersion());
        }
        return doSupportMapRTicket;
    }

    private boolean doSupportTez() {
        if (isHiveDBConnSelected()) {
            return HiveMetadataHelper.doSupportTez(hiveDistributionCombo.getText(), hiveVersionCombo.getText(), true);
        }
        return false;
    }

    private boolean canLinkToHadoopCluster() {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        return hadoopClusterService != null;
    }

    private String getselecteModulePath(String selecteModule) {
        File file = new File(selecteModule);
        if (file.exists() && file.isFile()) {
            return selecteModule;
        }
        String selecteModulePath = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            ILibraryManagerService librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            if (librairesService != null) {
                selecteModulePath = librairesService.getJarPath(selecteModule);
            }
        }
        return selecteModulePath;
    }

    /**
     * 
     * DOC zshen Comment method "getMetadataConnection".
     * 
     * @return
     */
    public IMetadataConnection getMetadataConnection() {
        return this.metadataconnection;
    }

    public LabelledCombo getHiveModeCombo() {
        return this.hiveModeCombo;
    }

    public LabelledCombo getDistributionCombo() {
        return this.hiveDistributionCombo;
    }

    public LabelledCombo getHiveVersionCombo() {
        return this.hiveVersionCombo;
    }

    public ContextType getSelectedContextType() {
        return this.selectedContextType;
    }

    /**
     * Getter for properties.
     * 
     * @return the properties
     */
    public List<HashMap<String, Object>> getProperties() {
        return this.properties;
    }

    /**
     * Sets the properties.
     * 
     * @param properties the properties to set
     */
    public void setProperties(List<HashMap<String, Object>> properties) {
        this.properties = properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.ui.wizard.AbstractForm#exportAsContext()
     */
    @Override
    protected void exportAsContext() {
        collectContextParams();
        super.exportAsContext();
    }

    private String getConnectionDBType() {
        if (getConnection() == null || getConnection().getDatabaseType() == null) {
            return "";
        }
        return getConnection().getDatabaseType();
    }
}
