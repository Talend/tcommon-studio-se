// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.repository;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.maven.AbsM2Sync;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class DefaultMavenRepositoryProvider {

    public static final String PATH_REPO = "maven_repository"; //$NON-NLS-1$

    public static IPath getMavenRepoPath() {
        Path configPath = new Path(Platform.getConfigurationLocation().getURL().getPath());
        return configPath.append(PATH_REPO);
    }

    public static void sync(File dest) {
        if (dest == null) {
            return;
        }
        if (dest.exists() && dest.isFile()) {
            return;
        }
        dest.mkdirs();

        if (!dest.exists() || !dest.canWrite()) { // can't create the parent folder
            return;
        }
        try {

            URL mavenRepoUrl = FileLocator.find(DesignerMavenPlugin.getPlugin().getContext().getBundle(), new Path(
                    "/resources/repository/maven_repository.zip"), null); //$NON-NLS-1$
            if (mavenRepoUrl != null) {
                mavenRepoUrl = FileLocator.toFileURL(mavenRepoUrl);
            }
            if (mavenRepoUrl == null) { // not existed
                return;
            }

            File zipFile = new File(mavenRepoUrl.getFile());
            if (!zipFile.exists()) {
                return;
            }
            FilesUtils.unzip(zipFile.getAbsolutePath(), dest.getAbsolutePath(), false);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        Collection<AbsM2Sync> m2Syncs = getM2Syncs();
        for (AbsM2Sync m2Sync : m2Syncs) {
            try {
                m2Sync.sync(dest);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private static Collection<AbsM2Sync> getM2Syncs() {
        IConfigurationElement[] m2SyncElems = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(AbsM2Sync.EXTENTION_POINT);
        if (m2SyncElems == null || m2SyncElems.length <= 0) {
            return Collections.EMPTY_SET;
        }

        Collection<AbsM2Sync> syncs = new HashSet<>();
        for (IConfigurationElement m2Sync : m2SyncElems) {
            try {
                AbsM2Sync sync = (AbsM2Sync) m2Sync.createExecutableExtension(AbsM2Sync.EXTENTION_CLASS);
                if (sync == null) {
                    throw new Exception("Can't initialize m2Sync: " + m2Sync.toString()); //$NON-NLS-1$
                }
                syncs.add(sync);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
        return syncs;
    }

}
