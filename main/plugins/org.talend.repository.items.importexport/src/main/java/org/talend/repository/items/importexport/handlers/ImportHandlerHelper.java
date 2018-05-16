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
package org.talend.repository.items.importexport.handlers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.ResourceNotFoundException;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.repository.items.importexport.handlers.imports.ImportCacheHelper;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.i18n.Messages;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * created by ggu on Apr 15, 2014 Detailled comment
 * 
 */
public class ImportHandlerHelper {

    /**
     * According to the valid resource path, resolve the EMF properties file, and check the overwrite status or be valid
     * item or not, etc.
     * 
     * @param monitor
     * @param resManager
     * @param resourcePath according to the this path to calculate the item record.
     * @param overwrite
     * @return if not valid, will return null.
     */
    public ImportItem computeImportItem(IProgressMonitor monitor, ResourcesManager resManager, IPath resourcePath,
            boolean overwrite) throws Exception {
        if (resManager == null || resourcePath == null) {
            return null;
        }
        // only process the *.properties file.
        if (!validResourcePath(resourcePath)) {
            return null;
        }
        ImportItem importItem = new ImportItem(resourcePath);

        Resource resource = HandlerUtil.loadResource(resManager, importItem);

        if (resource != null) {
            importItem.setProperty(generateProperty(resource));
        } else {
            ResourceNotFoundException ex = new ResourceNotFoundException(Messages.getString(
                    "ImportBasicHandler_LoadEMFResourceError", //$NON-NLS-1$
                    importItem.getPath().lastSegment(), HandlerUtil.getValidItemRelativePath(resManager, importItem.getPath())));
            importItem.addError(ex.getMessage());
            // don't throw, just add in error of item. because if one item is error, will block all.
            // throw ex;
        }
        return importItem;
    }

    public boolean validResourcePath(IPath resourcePath) {
        return ImportCacheHelper.getInstance().getXmiResourceManager().isPropertyFile(resourcePath.lastSegment());
    }

    public Property generateProperty(Resource resource) {
        return (Property) EcoreUtil.getObjectByType(resource.getContents(), PropertiesPackage.eINSTANCE.getProperty());
    }
}
