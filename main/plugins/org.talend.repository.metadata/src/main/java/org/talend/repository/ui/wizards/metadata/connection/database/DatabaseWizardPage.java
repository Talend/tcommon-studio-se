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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.check.ICheckListener;
import org.talend.core.ui.check.IChecker;
import org.talend.daikon.properties.presentation.Form;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.metadata.managment.ui.wizard.RepositoryWizard;
import org.talend.repository.metadata.i18n.Messages;

/**
 * DatabaseWizard present the DatabaseForm. Use to Use to manage the metadata connection. Page allows setting a
 * database.
 */
public class DatabaseWizardPage extends WizardPage {

    private ConnectionItem connectionItem;

    private DatabaseForm databaseForm;
    
    private Composite dynamicForm;
    
    private Composite dynamicContextForm;
    
    private Composite dynamicParentForm;
    
    private Composite compositeDbSettings;
    
    private DBTypeForm dbTypeForm;

    private final String[] existingNames;

    private final boolean isRepositoryObjectEditable;
    
    private Composite parentContainer;
    
    private boolean isCreation = false;
    
    protected IStatus genericStatus;

    /**
     * DatabaseWizardPage constructor.
     * 
     * @param connection
     * @param isRepositoryObjectEditable
     * @param existingNames
     */
    public DatabaseWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable, String[] existingNames) {
        super("wizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
    }

    /**
     * Create the composites, initialize it and add controls.
     * 
     * @see IDialogPage#createControl(Composite)
     */
    @Override
    public void createControl(final Composite parent) {
        if (this.getWizard() instanceof RepositoryWizard) {
            isCreation = ((RepositoryWizard) getWizard()).isCreation();
        }
        
        parentContainer = new Composite(parent, SWT.NONE);
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 1;
        fillLayout.marginHeight = 0;
        fillLayout.marginWidth = 0;
        parentContainer.setLayout(new FormLayout());
        GridData parentGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        parentContainer.setLayoutData(parentGridData);
        compositeDbSettings = new Composite(parentContainer, SWT.NULL);
        compositeDbSettings.setLayout(new GridLayout(3, false));
        
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        compositeDbSettings.setLayoutData(data);
        dbTypeForm = new DBTypeForm(this, compositeDbSettings, connectionItem, SWT.NONE, !isRepositoryObjectEditable, isCreation);
        
        createDBForm();
    }
    
    public void createDBForm(){
        if(parentContainer == null || parentContainer.isDisposed()){
            return;
        }

        //dynamic Composite
        createDynamicForm();
        
        //DB Composite
        createDatabaseForm();
        parentContainer.layout();
    }
    
    private void createDynamicForm(){
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService == null){
            return;
        }
        if(dbService.getExtraTypes().isEmpty()){
           return; 
        }
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(compositeDbSettings, 0);
        data.bottom = new FormAttachment(100, 0);
        
        dynamicParentForm = new Composite(parentContainer, SWT.NONE);
        dynamicParentForm.setLayoutData(data);
        dynamicParentForm.setLayout(new FormLayout());
        Map<String, Composite> map = dbService.creatDBDynamicComposite(dynamicParentForm, EComponentCategory.BASIC,
                !isRepositoryObjectEditable, isCreation, connectionItem.getProperty(), "JDBC"); 
        dynamicForm = map.get("DynamicComposite");//$NON-NLS-1$
        dynamicContextForm = map.get("ContextComposite");//$NON-NLS-1$
        if(isTCOMDB(dbTypeForm.getDBType())){
            setControl(dynamicForm);
        }
        dynamicParentForm.setVisible(isTCOMDB(dbTypeForm.getDBType()));
        addCheckListener(dbService.getDynamicChecker(dynamicForm));
        if(isCreation){
            resetDynamicConnectionItem(connectionItem);
        }
    }
    
    private void createDatabaseForm(){
        if(isTCOMDB(dbTypeForm.getDBType())){
           return; 
        }
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(compositeDbSettings, 0);
        data.bottom = new FormAttachment(100, 0);
        
        databaseForm = new DatabaseForm(parentContainer, connectionItem, existingNames, isCreation);
        databaseForm.setLayoutData(data);
        databaseForm.setReadOnly(!isRepositoryObjectEditable);
        databaseForm.updateSpecialFieldsState();

        AbstractForm.ICheckListener listener = new AbstractForm.ICheckListener() {

            @Override
            public void checkPerformed(final AbstractForm source) {
                if (dbTypeForm.getDBType() == null){
                    DatabaseWizardPage.this.setPageComplete(false);
                    setErrorMessage(Messages.getString("DatabaseForm.alert", "DB Type"));//$NON-NLS-1$  //$NON-NLS-2$
                }else if (source.isStatusOnError()) {
                    DatabaseWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        databaseForm.setListener(listener);
        if (connectionItem.getProperty().getLabel() != null && !connectionItem.getProperty().getLabel().equals("")) { //$NON-NLS-1$
            databaseForm.checkFieldsValue();
        }
        if(!isTCOMDB(dbTypeForm.getDBType())){
            setControl(databaseForm);
        }
        databaseForm.setVisible(!isTCOMDB(dbTypeForm.getDBType()));
    }
    
    public boolean isTCOMDB(String type){
        if(type == null){
            return false;
        }
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        for(ERepositoryObjectType eType:extraTypes){
            if(eType.getType().equals(type)){
               return true; 
            }
        }
        return false;
    }
    
    public boolean isGenericConn(ConnectionItem connItem){
        IGenericWizardService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            dbService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(
                    IGenericWizardService.class);
        }
        if(dbService != null){
            return dbService.isGenericItem(connItem);
        }
        return false;
    }
    
    private void resetDynamicConnectionItem(ConnectionItem connItem){
        if(connItem == null){
            return;
        }
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            dbService.resetConnectionItem(dynamicForm, connItem);
            dbService.resetConnectionItem(dynamicContextForm, connItem);
        }
    }
    
    public void refreshDBForm(ConnectionItem connItem){
        if(connItem != null){
            this.connectionItem = connItem;
            ((DatabaseWizard)getWizard()).setNewConnectionItem(connItem);
        }
        if(databaseForm == null || databaseForm.isDisposed()){
            createDatabaseForm();
        }
        if(isTCOMDB(dbTypeForm.getDBType())){
            if(dynamicParentForm == null || dynamicParentForm.isDisposed()){
                createDynamicForm();;
            }
            dynamicParentForm.setVisible(true);
            databaseForm.setVisible(false);
            setControl(dynamicForm);
            resetDynamicConnectionItem(connItem);
            String product = dbTypeForm.getDBType();
            ((DatabaseConnection)connItem.getConnection()).setProductId(product);
            String mapping = null;
            if (MetadataTalendType.getDefaultDbmsFromProduct(product) != null) {
                mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
            }
            if (mapping == null) {
                mapping = "mysql_id"; // default value //$NON-NLS-1$
            }
            ((DatabaseConnection)connItem.getConnection()).setDbmsId(mapping);
        }else{
            databaseForm.setVisible(true);
            if(dynamicParentForm != null && !dynamicParentForm.isDisposed()){
                dynamicParentForm.setVisible(false);
            }
            databaseForm.refreshDBForm(connItem);
            setControl(databaseForm);
        }
        parentContainer.layout();
    }

    
    public void disposeDBForm(){
        if(databaseForm != null && !databaseForm.isDisposed()){
            databaseForm.dispose();
        }
        if(dynamicParentForm != null && !dynamicParentForm.isDisposed()){
            dynamicParentForm.dispose();
        }
        if(dynamicForm != null && !dynamicForm.isDisposed()){
            dynamicForm.dispose();
        }
    }
    /**
     * 
     * DOC zshen Comment method "getMetadataConnection".
     * 
     * @return
     */
    public IMetadataConnection getMetadataConnection() {
        if(databaseForm != null){
            return databaseForm.getMetadataConnection();
        }
        return null;
    }

    public ContextType getSelectedContextType() {
        if(databaseForm != null){
            return databaseForm.getSelectedContextType();
        }
        return null;
    }
    
    public Form getForm(){
        if(dynamicForm != null){
            IGenericDBService dbService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                        IGenericDBService.class);
            }
            if(dbService != null){
                return dbService.getDynamicForm(dynamicForm);
            }
        }
        return null;
    }
    
    protected void addCheckListener(IChecker checker) {
        if(checker == null){
            return;
        }
        ICheckListener checkListener = new ICheckListener() {

            @Override
            public void checkPerformed(IChecker source) {
                if (source.isStatusOnError()) {
                    DatabaseWizardPage.this.setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    DatabaseWizardPage.this.setPageComplete(isRepositoryObjectEditable);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
        checker.setListener(checkListener);
    }
    
}
