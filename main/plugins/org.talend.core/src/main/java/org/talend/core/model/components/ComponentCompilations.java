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
package org.talend.core.model.components;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;

/**
 * DOC mhirt class global comment. Detailled comment
 */
public class ComponentCompilations {

    private static File f = null;

    public static void deleteMarkers() {
        initFile();
        if (fileExists()) {
            f.delete();
        }
    }

    public static void addMarkers() {
        initFile();
        if (fileNotExists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static boolean getMarkers() {
        initFile();
        return fileExists();
    }

    private static boolean fileExists() {
        return f != null && f.exists();
    }

    private static boolean fileNotExists() {
        return f != null && !f.exists();
    }

    private static void initFile() {
        try {
            if (f == null) {
                String filePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
                f = new File(filePath + File.separator + ".JETEmitters" + File.separator + "FirstCompilationMarker"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}
