// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class DBTypeForm {

    private static final int VISIBLE_DATABASE_COUNT = 20;

    private LabelledCombo dbTypeCombo;

    private ConnectionItem connectionItem;

    private boolean isReadOnly;

    private boolean isCreation;

    private DatabaseWizardPage wizardPage;

    private Composite parent;

    private String dbType;

    private ConnectionItem oriConnItem;

    public DBTypeForm(DatabaseWizardPage wizardPage, Composite parent, ConnectionItem connectionItem,int style, boolean readOnly, boolean isCreation) {
        this.parent = parent;
        this.wizardPage = wizardPage;
        this.connectionItem = connectionItem;
        this.isReadOnly = readOnly;
        this.isCreation = isCreation;
        initialize();
    }

    public void initialize() {
        this.dbType = getConnectionDBType();
        addDBSelectCombo();
        String displayConnectionDBType = getDisplayConnectionDBType();
        EDatabaseConnTemplate template = EDatabaseConnTemplate.indexOfTemplate(displayConnectionDBType);
        if (template != null) {
            if (dbTypeCombo.getText().length() == 0 || !dbTypeCombo.getText().equals(template.getDbType().getDisplayName())) {
                dbTypeCombo.setText(template.getDbType().getDisplayName());
            }
        } else if (isAdditionalJDBC(getDisplayConnectionDBType())) {
            if (dbTypeCombo.getText().length() == 0 || !dbTypeCombo.getText().equals(displayConnectionDBType)) {
                dbTypeCombo.setText(displayConnectionDBType);
            }
        }
        addListerner();
        adaptFormToReadOnly();
        if(!isCreation){
            this.oriConnItem = connectionItem;
        }
    }

    private void adaptFormToReadOnly() {
        dbTypeCombo.setReadOnly(isReadOnly);
    }

    private void addDBSelectCombo() {
        List<String> dbTypeDisplayList = EDatabaseConnTemplate.getDBTypeDisplay();

        // added by dlin for 21721,only a temporary approach to resolve it -begin
        IWorkbenchWindow workBenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (workBenchWindow != null) {
            IWorkbenchPage page = workBenchWindow.getActivePage();
            if (page != null) {
                String perId = page.getPerspective().getId();
                if ((!"".equals(perId) && null != perId)) { //$NON-NLS-1$
                    // eg : use DI, then switch to DQ : All view from DI must be hidden when switch
                    // MOD qiongli 2012-7-10 TDQ-5801,hide also 'MSsql 2005/2008' for DQ after delete that MS jars.
                    if (perId.equalsIgnoreCase(IBrandingConfiguration.PERSPECTIVE_DI_ID)
                            || perId.equalsIgnoreCase(IBrandingConfiguration.PERSPECTIVE_DQ_ID)) {
                        if (dbTypeDisplayList != null) {
                            ArrayList<String> newList = new ArrayList<String>(dbTypeDisplayList);
                            for (int i = 0; i < newList.size(); i++) {
                                if (newList.get(i).equalsIgnoreCase(("Microsoft SQL Server 2005/2008"))) {
                                    newList.remove(i);
                                }
                            }
                            dbTypeDisplayList = newList;
                        }
                    }
                }
            }
        }

        // added by dlin for 21721,only a temporary approach to resolve it -end
        if (PluginChecker.isOnlyTopLoaded()) {
            dbTypeDisplayList = filterUnavailableType(dbTypeDisplayList);
        }
        filterTypesUnloadProviders(dbTypeDisplayList);

        filterUnsupportedDBType(dbTypeDisplayList);

        dbTypeCombo = new LabelledCombo(parent, Messages.getString("DatabaseForm.dbType"), Messages //$NON-NLS-1$
                .getString("DatabaseForm.dbTypeTip"), dbTypeDisplayList.toArray(new String[0]), 2, true); //$NON-NLS-1$

        // configure the visible item of database combo
        int visibleItemCount = dbTypeCombo.getCombo().getItemCount();
        if (visibleItemCount > VISIBLE_DATABASE_COUNT) {
            visibleItemCount = VISIBLE_DATABASE_COUNT;
        }
        if(this.dbType != null){
            dbTypeCombo.setText(dbType);
        }
        dbTypeCombo.getCombo().setVisibleItemCount(visibleItemCount);
    }

    private void filterUnsupportedDBType(List<String> dbTypeDisplayList) {
        Iterator<String> it = dbTypeDisplayList.iterator();
        while (it.hasNext()) {
            String displayName = it.next();
            EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDisplayName(displayName);
            if (!type.isSupport()) {
                it.remove();
            }
        }
    }

    private void filterTypesUnloadProviders(List<String> dbTypeDisplayList) {
        Iterator<String> it = dbTypeDisplayList.iterator();
        while (it.hasNext()) {
            String displayName = it.next();
            EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDisplayName(displayName);
            // if can't find the provider for current typename,remove it from combo
            if (type != null && type.isUseProvider()) {
                String dbtypeString = type.getXmlName();
                if (dbtypeString != null && ExtractMetaDataFromDataBase.getProviderByDbType(dbtypeString) == null) {
                    it.remove();
                }
            }
        }

    }

    private List<String> filterUnavailableType(List<String> dbTypeDisplayList) {
        List<String> resultList = new ArrayList<String>();

        List<String> tdqSupportDBList = MetadataConnectionUtils.getTDQSupportDBTemplate();
        for (String dbType : dbTypeDisplayList) {
            if (tdqSupportDBList.contains(dbType)) {
                resultList.add(dbType);
            }
        }

        return resultList;
    }

    protected final boolean isContextMode() {
        if (connectionItem != null) {
            return connectionItem.getConnection().isContextMode();
        }
        return false;
    }

    private void addListerner(){
        dbTypeCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent e) {
                wizardPage.setPageComplete(!isReadOnly);
                wizardPage.setErrorMessage(null);
                String displayDBType = dbTypeCombo.getText();
                dbType = displayDBType;
                if (isAdditionalJDBC(displayDBType)) {
                    dbType = "JDBC";
                }
                String oldType = getDisplayConnectionDBType();

                if (displayDBType.equals(oldType)) {
                    return;
                }
                if (needDisposeOldForm(displayDBType, oldType)) {
                    reCreateConnection();
                    setConnectionDBType(dbType);
                    if(!wizardPage.isTCOMDB(dbType)){
                        ((DatabaseConnection)connectionItem.getConnection()).getParameters().clear();
                        ((DatabaseConnection)connectionItem.getConnection()).setDbVersionString(null);
                    }
                    wizardPage.refreshDBForm(connectionItem);
                    if(wizardPage.isTCOMDB(dbType)){
                        wizardPage.setPageComplete(true);
                    }
                }else{
                    setConnectionDBType(dbType);
                    wizardPage.refreshDBForm(null);
                }
            }
        });
    }

    private boolean isAdditionalJDBC(String dbType) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            IGenericWizardService service = GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
            if (service != null) {
                return service.getIfAdditionalJDBCDBType(dbType);
            }
        }
        return false;
    }

    public boolean needDisposeOldForm(String newType, String oldType){
        return !newType.equals(oldType);
    }

    private void reCreateConnection(){
        if(wizardPage.isTCOMDB(dbType)){
            IGenericDBService dbService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                        IGenericDBService.class);
            }
            if(dbService == null){
                return;
            }
            if(!isCreation && dbType.equals(oriConnItem.getTypeName())){
                connectionItem = oriConnItem;
                return;
            }
        }
    }

    public String getDBType(){
        return this.dbType;
    }

    private String getConnectionDBType(){
        return ((DatabaseConnection)connectionItem.getConnection()).getDatabaseType();
    }


    private void setConnectionDBType(String type){
        if(connectionItem.getConnection() instanceof DatabaseConnection){
            connectionItem.setTypeName(type);
            ((DatabaseConnection)connectionItem.getConnection()).setDatabaseType(type);
        }
    }

    public String getDisplayConnectionDBType() {
        DatabaseConnection connection = (DatabaseConnection) connectionItem.getConnection();
        String databaseType = connection.getDatabaseType();
        String productId = connection.getProductId();
        if (wizardPage.isTCOMDB(databaseType) && !databaseType.equals(productId)) {
            return productId;
        }
        return databaseType;
    }

    public String getDisplayDBType() {
        return dbTypeCombo.getText();
    }
}
