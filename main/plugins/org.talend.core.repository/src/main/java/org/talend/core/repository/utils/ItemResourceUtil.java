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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.util.EmfResourceUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ItemResourceUtil {

    /**
     * the the item location path, like ".../<project>/process/<path>/XXX_0.1.properties"
     */
    public static IPath getItemLocationPath(Property p) {
        return EmfResourceUtil.getItemLocationPath(p);
    }

    /**
     * 
     * get the type folder of item. like ".P/process"
     */
    public static IFolder getObjectTypeFolder(Property p) {
        return EmfResourceUtil.getObjectTypeFolder(p);
    }

    public static IPath getItemRelativePath(Property p) {
        IPath itemLocationPath = getItemLocationPath(p);
        IFolder objectTypeFolder = getObjectTypeFolder(p);
        if (itemLocationPath != null && objectTypeFolder != null) {
            return itemLocationPath.removeLastSegments(1).makeRelativeTo(objectTypeFolder.getLocation());
        }
        return null;
    }
}
