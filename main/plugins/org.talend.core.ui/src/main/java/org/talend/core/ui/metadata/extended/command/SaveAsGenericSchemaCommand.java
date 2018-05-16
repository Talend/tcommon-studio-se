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
package org.talend.core.ui.metadata.extended.command;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.repository.model.IMetadataService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;

/**
 * Administrator class global comment. Detailed comment <br/>
 * 
 */
public class SaveAsGenericSchemaCommand extends Command {

    private final ExtendedTableModel extendedTableModel;

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    private final String dbmsId;

    public SaveAsGenericSchemaCommand(ExtendedTableModel extendedTableModel, String dbmsId) {
        this.extendedTableModel = extendedTableModel;
        this.dbmsId = dbmsId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        if (extendedTableModel instanceof MetadataTableEditor) {

            MetadataTableEditor editor = (MetadataTableEditor) extendedTableModel;
            IMetadataTable metadataTable = editor.getMetadataTable();
            List<IMetadataColumn> listColumns = metadataTable.getListColumns();

            if (listColumns == null || listColumns.size() == 0) {
                return;
            }

            RepositoryNode genericSchemaNode = getGenericSchemaNode();
            boolean isGenericSchemaExisting = false;
            RepositoryObject repositoryObject = null;
            if (genericSchemaNode != null) {
                // isGenericSchemaExisting = isGenericSchemaExisting(genericSchemaNode);
            }

            IMetadataService metadataService = CoreRuntimePlugin.getInstance().getMetadataService();
            if (metadataService == null) {
                return;
            }
            WizardDialog dialog = metadataService.getGenericSchemaWizardDialog(new Shell(), PlatformUI.getWorkbench(), false,
                    null, null, true);
            dialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
            dialog.create();
            Property property = null;

            if (dialog.open() != 0) {
                return;
            } else {
                property = metadataService.getPropertyFromWizardDialog();
            }

            // if (!isGenericSchemaExisting) {
            repositoryObject = new RepositoryObject();

            GenericSchemaConnectionItem item = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
            // Property property = PropertiesFactory.eINSTANCE.createProperty();
            // property
            // .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
            // .getUser());
            // property.setVersion(VersionUtils.DEFAULT_VERSION);
            // property.setStatusCode(""); //$NON-NLS-1$
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            String nextId = factory.getNextId();
            property.setId(nextId);
            // property.setLabel("test22333333");

            GenericSchemaConnection connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();
            connection.setLabel("connectionLabel"); //$NON-NLS-1$
            connection.setComment("connectionComment"); //$NON-NLS-1$

            if (this.dbmsId != null && this.dbmsId.length() > 0) {
                connection.setMappingTypeId(this.dbmsId);
                connection.setMappingTypeUsed(true);
            }

            MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getNextId();
            metadataTable.setId(nextId);
            // createMetadataTable.setConnection(connection);
            if (createMetadataTable.getNamespace() instanceof Package) {
                Package pkg = (Package) createMetadataTable.getNamespace();
                pkg.getDataManager().add(connection);
            }
            createMetadataTable.setLabel("metadata"); //$NON-NLS-1$
            for (IMetadataColumn column : listColumns) {
                MetadataColumn createMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                createMetadataColumn.setComment(column.getComment());
                createMetadataColumn.setLabel(column.getLabel());
                createMetadataColumn.setDefaultValue(column.getDefault());
                createMetadataColumn.setId(column.getId() + ""); //$NON-NLS-1$
                createMetadataColumn.setKey(column.isKey());
                Integer length = column.getLength();
                if (length == null) {
                    length = -1;
                }
                createMetadataColumn.setLength(length);
                Integer precision = column.getPrecision();
                if (precision == null) {
                    precision = -1;
                }
                createMetadataColumn.setPrecision(precision);
                createMetadataColumn.setPattern(column.getPattern());
                createMetadataColumn.setNullable(column.isNullable());
                createMetadataColumn.setOriginalField(column.getOriginalDbColumnName());
                createMetadataColumn.setTalendType(column.getTalendType());
                createMetadataColumn.setSourceType(column.getType());

                createMetadataTable.getColumns().add(createMetadataColumn);
            }
            GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection,
                    GenericPackage.class);
            if (g != null) {
                g.getOwnedElement().add(createMetadataTable);
            } else {
                GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
                PackageHelper.addMetadataTable(createMetadataTable, gpkg);
                ConnectionHelper.addPackage(gpkg, connection);

            }
            // connection.getTables().add(createMetadataTable);
            item.setProperty(property);
            item.setConnection(connection);

            // }
            IPath path = metadataService.getPathForSaveAsGenericSchema();
            if (path == null) {
                this.saveMetaData(item, new Path("")); //$NON-NLS-1$
            } else {
                this.saveMetaData(item, path);
            }
        }
    }

    // private boolean isGenericSchemaExisting(RepositoryNode genericSchemaNode) {
    // for (RepositoryNode repositoryNode : genericSchemaNode.getChildren()) {
    // String repositoryNodeLabel = repositoryNode.getLabel();
    //
    // }
    // return false;
    // }

    public RepositoryNode getGenericSchemaNode() {
        return ProjectRepositoryNode.getInstance().getRootRepositoryNode(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
    }

    /**
     * execute saveMetaData() on TableForm.
     */
    private void saveMetaData(ConnectionItem item, IPath path) {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            factory.create(item, path);
        } catch (PersistenceException e) {
            // TODO
        }
    }
}
