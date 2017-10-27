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
package org.talend.core.repository.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ItemResourceUtil {

    /**
     * the the item location path, like ".../<project>/process/<path>/XXX_0.1.properties"
     */
    public static IPath getItemLocationPath(Property p) {
        if (p != null) {
            Resource eResource = p.eResource();
            if (eResource == null) {
                try {
                    IRepositoryViewObject lastVersion = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory()
                            .getLastVersion(p.getId());
                    if (lastVersion != null) {
                        eResource = lastVersion.getProperty().eResource();
                    }
                } catch (PersistenceException e) {
                    //
                }
            }
            if (eResource != null) {
                URI uri = eResource.getURI();
                IFile file = URIHelper.getFile(uri);
                if (file != null) {
                    return file.getLocation();
                }
            }
        }
        return null;
    }

    /**
     * 
     * get the type folder of item. like ".P/process"
     */
    public static IFolder getObjectTypeFolder(Property p) {
        if (p != null) {
            Project itemProject = ProjectManager.getInstance().getProject(p);
            if (itemProject != null) {
                try {
                    IProject proj = ResourceUtils.getProject(new org.talend.core.model.general.Project(itemProject));
                    if (proj != null) {
                        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(p.getItem());
                        if (itemType != null && itemType.isResouce()) {
                            return proj.getFolder(itemType.getFolder());
                        }
                    }
                } catch (PersistenceException e) {
                    //
                }
            }
        }
        return null;
    }

}
