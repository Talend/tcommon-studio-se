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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.io.IOException;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
import org.talend.core.GlobalServiceRegister;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class OsgiBundleInstaller {

    private static BundleContext baseBundleContext = FrameworkUtil.getBundle(OsgiBundleInstaller.class).getBundleContext();

    public static boolean installAndStartBundle(File file) throws IOException, BundleException {
        Manifest manifest = JarMenifestUtil.getManifest(file);
        if (manifest != null) {
            String bundleSymbolicName = JarMenifestUtil.getBundleSymbolicName(manifest);

            Bundle bundle = Platform.getBundle(bundleSymbolicName);
            // 1. if has started old version, try to stop and uninstall?
            if (bundle != null) {
                String bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
                Version fileVersion = new Version(bundleVersion);
                Version version = bundle.getVersion();
                if (version.equals(fileVersion)) { // same version, have been started
                    return false;
                } else if (version.compareTo(fileVersion) > 0) { // try install old version
                    return false; // must keep the latest version to be installed
                } else { // if new version, try to stop and uninstall old version
                    if (bundle.getState() == Bundle.ACTIVE) {
                        bundle.stop();
                    }
                    bundle.uninstall();

                    // the bundle should be null now.
                    bundle = Platform.getBundle(bundleSymbolicName);
                    if (bundle != null) {
                        throw new BundleException("Shouldn't existed the bundle now: " + bundleSymbolicName);
                    }
                }
            }
            // 2. install and start new version
            String bundleLocation = "reference:file:plugins/" + file.getName(); //$NON-NLS-1$

            Bundle installBundle = baseBundleContext.installBundle(bundleLocation);
            // installBundle.update();
            installBundle.start();
            return true;
        }
        return false;
    }

    public static void reloadComponents() {
        ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                ICodeGeneratorService.class);
        service.refreshTemplates();
    }
}
