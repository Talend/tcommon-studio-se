// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.genericshema;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.MetadataColumnsAndDbmsId;
import org.talend.core.model.metadata.MetadataSchema;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.xml.sax.SAXException;

import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportSchemaFileWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(ImportSchemaFileWizard.class);

    private MetadataColumnsAndDbmsId<MetadataColumn> metadataColumnsAndDbmsId;

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    private GenericSchemaWizardPage genericSchemaWizardPage;

    private GenericSchemaConnection connection;

    private Property connectionProperty;

    private File file;

    private boolean initOK = true;

    public ImportSchemaFileWizard(IWorkbench workbench, ISelection selection, String[] existingNames, File file) {
        super(workbench, true);
        this.selection = selection;
        this.existingNames = existingNames;
        this.file = file;

        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_WIZ));

        if (file == null) {
            return;
        }

        initWizard();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard#addPages()
     */
    @Override
    public void addPages() {

        setWindowTitle(Messages.getString("ImportSchemaFileWizard.WizardTitle"));//$NON-NLS-1$

        genericSchemaWizardPage = new GenericSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null);
        genericSchemaWizardPage.setTitle(Messages.getString("FileTableWizardPage.descriptionCreate")); //$NON-NLS-1$
        //$NON-NLS-2$
        genericSchemaWizardPage.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
        addPage(genericSchemaWizardPage);
        genericSchemaWizardPage.setPageComplete(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (genericSchemaWizardPage.isPageComplete()) {

            try {
                setPropNewName(connectionProperty);
                factory.create(connectionItem, pathToSave);

            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            } catch (BusinessException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;

    }

    public boolean isInitOK() {
        return initOK;
    }

    private void initWizard() {
        try {

            metadataColumnsAndDbmsId = MetadataSchema.loadMetadataColumnsAndDbmsIdFromFile(file);

        } catch (ParserConfigurationException e) {
            showErrorMessages(e.toString());
            return;
        } catch (SAXException e) {
            showErrorMessages(e.toString());
            return;
        } catch (IOException e) {
            showErrorMessages(e.toString());
            return;
        }
        if (metadataColumnsAndDbmsId == null) {
            initOK = false;
        }
        if (selection == null || existingNames == null) {
            initConnection();
            pathToSave = new Path(""); //$NON-NLS-1$
        } else {

            Object obj = ((IStructuredSelection) selection).getFirstElement();
            RepositoryNode node = (RepositoryNode) obj;
            switch (node.getType()) {
            case SIMPLE_FOLDER:
                pathToSave = RepositoryNodeUtilities.getPath(node);
                break;
            case SYSTEM_FOLDER:
            default:
                pathToSave = new Path(""); //$NON-NLS-1$
            }

            switch (node.getType()) {
            case SIMPLE_FOLDER:
            case SYSTEM_FOLDER:
                initConnection();
                break;
            default:
                return;
            }
        }

        connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(connection);

        String dbmsId = metadataColumnsAndDbmsId.getDbmsId();
        if (dbmsId != null && dbmsId.trim() != "") { //$NON-NLS-1$
            GenericSchemaConnection gsConnection = (GenericSchemaConnection) connection;
            gsConnection.setMappingTypeUsed(true);
            gsConnection.setMappingTypeId(dbmsId);
        }
        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId(factory.getNextId());
        metadataTable.setLabel("metadata"); //$NON-NLS-1$
        if (metadataTable.getNamespace() instanceof Package) {
            Package pkg = (Package) metadataTable.getNamespace();
            pkg.getDataManager().add(connection);
        }

        metadataTable.getColumns().addAll(metadataColumnsAndDbmsId.getMetadataColumns());

        GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), (Connection) connection,
                GenericPackage.class);
        if (g != null) { // hywang
            g.getOwnedElement().add(metadataTable);
        } else {
            GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
            PackageHelper.addMetadataTable(metadataTable, gpkg);
            ConnectionHelper.addPackage(gpkg, connection);
        }

    }

    private void initConnection() {
        connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$ 
        String name = file.getName();
        if (name.indexOf(".") > 0) { //$NON-NLS-1$
            name = name.substring(0, name.indexOf(".")); //$NON-NLS-1$
        }
        connectionProperty.setLabel(name);
        connectionProperty.setId(factory.getNextId());
        connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();

    }

    private void setPropNewName(Property theProperty) throws PersistenceException, BusinessException {
        String originalLabel = theProperty.getLabel();

        char j = 'a';
        while (!factory.isNameAvailable(theProperty.getItem(), null)) {
            String nextTry = originalLabel + "_" + (j++); //$NON-NLS-1$ //$NON-NLS-2$
            theProperty.setLabel(nextTry);
            if (j > 'z') {
                setPropNewName(theProperty);
                return;
            }
        }
    }

    private void showErrorMessages(String detailError) {
        initOK = false;
        new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                detailError);
        log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$

    }

    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }
}
