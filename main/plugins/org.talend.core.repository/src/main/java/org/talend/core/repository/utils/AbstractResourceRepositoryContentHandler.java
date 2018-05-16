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
package org.talend.core.repository.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.AbstractRepositoryContentHandler;
import org.talend.core.model.repository.ERepositoryObjectType;

public abstract class AbstractResourceRepositoryContentHandler extends AbstractRepositoryContentHandler {

    private XmiResourceManager xmiResourceManager;

    protected Resource create(IProject project, ProcessItem item, IPath path, ERepositoryObjectType type)
        throws PersistenceException {
        final Resource itemResource = getXmiResourceManager().createItemResource(project, item, path, type, false);
        itemResource.getContents().add(item.getProcess());
        return itemResource;
    }

    protected Resource create(IProject project, FileItem item, IPath path, ERepositoryObjectType type)
        throws PersistenceException {
        final Resource itemResource = getXmiResourceManager().createItemResource(project, item, path, type, true);
        itemResource.getContents().add(item.getContent());
        return itemResource;
    }

    protected Resource createScreenShotResource(IProject project, Item item, IPath path, ERepositoryObjectType type)
        throws PersistenceException {
        final Resource itemResource = getXmiResourceManager()
            .createScreenshotResource(project, item, path, type, false);
        itemResource.getContents().addAll(((ProcessItem) item).getProcess().getScreenshots());
        return itemResource;
    }

    protected Resource save(ProcessItem item) {
        final Resource itemResource = getXmiResourceManager().getItemResource(item);
        itemResource.getContents().clear();
        itemResource.getContents().add(item.getProcess());
        return itemResource;
    }

    protected Resource save(FileItem item) {
        final Resource itemResource = getXmiResourceManager().getItemResource(item);
        ByteArray content = item.getContent();
        itemResource.getContents().clear();
        itemResource.getContents().add(content);
        return itemResource;
    }

    protected Resource saveScreenShots(ProcessItem item) throws PersistenceException {
        Resource itemResource = getXmiResourceManager().getScreenshotResource(item, true, true);
        EMap screenshots = item.getProcess().getScreenshots();
        if (screenshots != null && !screenshots.isEmpty()) {
            itemResource.getContents().clear();
            itemResource.getContents().addAll(EcoreUtil.copyAll(screenshots));
        }
        return itemResource;
    }

    private XmiResourceManager getXmiResourceManager() {
        if (null == xmiResourceManager) {
            xmiResourceManager = new XmiResourceManager();
        }
        return xmiResourceManager;
    }
}
