// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core;

import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.IService;

public interface ITisLocalProviderService extends IService {

    public ResourceBundle getResourceBundle(String label);

    /**
     * Needs to create our own class loader in order to clear the cache for a ResourceBundle. Without using a new class
     * loader each time the values would not be reread from the .properties file
     * 
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4212439
     */
    public static class ResClassLoader extends ClassLoader {

        public ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }

    public AbstractUIPlugin getPlugin();
}
