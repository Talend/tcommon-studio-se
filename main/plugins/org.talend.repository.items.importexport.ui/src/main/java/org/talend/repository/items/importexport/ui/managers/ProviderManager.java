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
package org.talend.repository.items.importexport.ui.managers;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileManipulations;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.TarLeveledStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 */
public class ProviderManager extends AbstractImportResourcesManager {

    private IImportStructureProvider provider;

    public ProviderManager(Object provider) {
        this.provider = (IImportStructureProvider) provider;
    }

    public ProviderManager(File file) throws Exception {
        if (ArchiveFileManipulations.isTarFile(file.getPath())) {
            provider = new TarLeveledStructureProvider(new TarFile(file));
        } else if (ArchiveFileManipulations.isZipFile(file.getPath())) {
            provider = new ZipLeveledStructureProvider(new ZipFile(file));
        }

    }

    @Override
    public InputStream getStream(IPath path, ImportItem importItem) {
        return provider.getContents(path2Object.get(path));
    }

    @Override
    public boolean collectPath2Object(Object root) {
        return doCollectItemFiles(root, 0);
    }

    private boolean doCollectItemFiles(Object entry, int level) {
        List children = provider.getChildren(entry);
        if (children == null) {
            children = new ArrayList(1);
        }
        Iterator childrenEnum = children.iterator();
        while (childrenEnum.hasNext()) {
            Object child = childrenEnum.next();
            if (provider.isFolder(child)) {
                doCollectItemFiles(child, level + 1);
                addFolder(provider.getFullPath(child));
            } else {
                add(provider.getFullPath(child), child);
            }
        }
        return true;
    }

    @Override
    public void closeResource() {
        if (provider instanceof ZipLeveledStructureProvider) {
            ((ZipLeveledStructureProvider) provider).closeArchive();
        }

        if (provider instanceof TarLeveledStructureProvider) {
            ((TarLeveledStructureProvider) provider).closeArchive();
        }
    }

    /**
     * Getter for provider.
     * 
     * @return the provider
     */
    @Override
    public IImportStructureProvider getProvider() {
        return this.provider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.manager.ResourcesManager#getRoot()
     */
    @Override
    public Object getRoot() {
        if (provider instanceof ZipLeveledStructureProvider) {
            return ((ZipLeveledStructureProvider) provider).getRoot();
        }

        if (provider instanceof TarLeveledStructureProvider) {
            return ((TarLeveledStructureProvider) provider).getRoot();
        }
        return null;
    }
}
