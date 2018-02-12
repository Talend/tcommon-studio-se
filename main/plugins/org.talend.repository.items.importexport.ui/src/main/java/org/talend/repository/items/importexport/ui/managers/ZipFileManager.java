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
package org.talend.repository.items.importexport.ui.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 */
public class ZipFileManager extends AbstractImportResourcesManager {

    private ZipFile zipFile;

    public ZipFileManager(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public InputStream getStream(IPath path, ImportItem importItem) throws IOException {
        return zipFile.getInputStream((ZipEntry) path2Object.get(path));
    }

    @Override
    public boolean collectPath2Object(Object root) {
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            add(entry.getName(), entry);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.localprovider.imports.ResourcesManager#getProvider()
     */
    @Override
    public IImportStructureProvider getProvider() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.localprovider.imports.ResourcesManager#closeResource()
     */
    @Override
    public void closeResource() {
        try {
            if (zipFile != null) {
                zipFile.close();
            }
        } catch (IOException e) {
            //
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.manager.ResourcesManager#getRoot()
     */
    @Override
    public Object getRoot() {
        // TODO Auto-generated method stub
        return null;
    };

}
