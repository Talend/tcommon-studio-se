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
package org.talend.osgi.configurator;

import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;
import org.talend.osgi.hook.JarLoaderBundleFileWrapperFactory;

/**
 * configure the equinox hook that Talend uses to derive the jar missing in the bundle to load them in another folder.
 */
public class JarLoaderConfigurator implements HookConfigurator {

    @Override
    public void addHooks(HookRegistry hookRegistry) {
        // hookRegistry.addClassLoaderHook(new JarLoaderClassLoadingHook());
        hookRegistry.addBundleFileWrapperFactoryHook(new JarLoaderBundleFileWrapperFactory());
    }

}
