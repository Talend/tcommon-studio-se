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
package org.talend.repository.items.importexport.handlers.imports;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MetadataConnectionImportHandler extends ImportRepTypeHandler {

    private boolean isConnectionEmptyBeforeMigration = false;

    public MetadataConnectionImportHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler#beforeCreatingItem(org.talend.repository
     * .items.importexport.handlers.model.ImportItem)
     */
    @Override
    protected void beforeCreatingItem(ImportItem selectedItemRecord) {
        super.beforeCreatingItem(selectedItemRecord);
        final Item tmpItem = selectedItemRecord.getItem();
        // import has not been developed to cope with migration in mind
        // so some model may not be able to load like the ConnectionItems
        // in that case items needs to be copied before migration
        // here we check that the loading of the item failed before calling the create method
        /*
         * If load the old emf item files, in fact when resolveItem, will error to load for connection items. But the
         * resolveItem won't catch the exception at all. And in this case, before do the model migration, the
         * isConnectionEmptyBeforeMigration need to be true. Then, enable to copy the old items file manually.
         * 
         * Else, will cause the problem TDI-29262. Because after "factory.create", the isConnectionEmptyBeforeMigration
         * will be false, so don't copy the old items to prepare for model migration.
         */
        isConnectionEmptyBeforeMigration = tmpItem instanceof ConnectionItem
                && ((ConnectionItem) tmpItem).getConnection().eResource() == null
                && !selectedItemRecord.getMigrationTasksToApply().isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.imports.ImportRepTypeHandler#afterCreatedItem(org.
     * talend.repository.items.importexport.ui.wizard.imports.managers.ResourcesManager,
     * org.talend.repository.items.importexport.ui.wizard.imports.models.ItemRecord)
     */
    @Override
    protected void afterCreatedItem(ResourcesManager resManager, ImportItem importItem) throws Exception {
        final Item tmpItem = importItem.getItem();
        if (isConnectionEmptyBeforeMigration) {
            /*
             * copy the file before migration, this is bad. because it should not refer to Filesytem. but this is a
             * quick hack and anyway the migration task only works on files
             */

            InputStream is = resManager.getStream(importItem.getPath().removeFileExtension()
                    .addFileExtension(FileConstants.ITEM_EXTENSION), importItem);

            try {
                URI propertyResourceURI = EcoreUtil.getURI(((ConnectionItem) tmpItem).getProperty());
                URI relativePlateformDestUri = propertyResourceURI.trimFileExtension().appendFileExtension(
                        FileConstants.ITEM_EXTENSION);
                URL fileURL = FileLocator.toFileURL(new java.net.URL(
                        "platform:/resource" + relativePlateformDestUri.toPlatformString(true))); //$NON-NLS-1$
                OutputStream os = new FileOutputStream(fileURL.getFile());
                try {
                    FileCopyUtils.copyStreams(is, os);
                } finally {
                    os.close();
                }
            } finally {
                is.close();
            }
            ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
            repFactory.unloadResources(tmpItem.getProperty());
        } else {
            super.afterCreatedItem(resManager, importItem);
        }
    }

}
