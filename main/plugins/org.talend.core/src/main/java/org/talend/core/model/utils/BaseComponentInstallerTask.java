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
package org.talend.core.model.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.core.MavenPlugin;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;

/**
 * @author bhe created on Jul 1, 2021
 *
 */
abstract public class BaseComponentInstallerTask implements IComponentInstallerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseComponentInstallerTask.class);

    private int order;

    private Set<ComponentGAV> gavs = new HashSet<ComponentGAV>();

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Set<ComponentGAV> getComponentGAV() {
        return gavs;
    }

    @Override
    public void addComponentGAV(ComponentGAV gav) {
        gavs.add(gav);
    }

    @Override
    public Set<ComponentGAV> getComponentGAV(int componentType) {
        return this.gavs.stream().filter(gav -> (gav.getComponentType() & componentType) > 0).collect(Collectors.toSet());
    }

    /**
     * Get implementation class of installer
     * 
     * @return implementation class of installer
     */
    abstract protected Class<? extends BaseComponentInstallerTask> getInstallerClass();

    /**
     * Get jar file directory
     * 
     * @return jar file directory
     */
    protected File getJarFileDir() {
        URL jarFolder = FileLocator.find(FrameworkUtil.getBundle(getInstallerClass()), new Path("repository"), null);
        File jarFileDir = null;
        if (jarFolder != null) {
            try {
                jarFileDir = new File(FileLocator.toFileURL(jarFolder).getPath());

                if (jarFileDir.isDirectory()) {
                    return jarFileDir;
                }

            } catch (IOException e) {
                LOGGER.error("Can't find jar file", e);
            }
        }
        LOGGER.info("Can't find jar file from folder {}", jarFolder);
        return null;
    }

    @Override
    public boolean needInstall() {
        boolean toInstall = false;
        Set<ComponentGAV> tcompv0Gavs = this.getComponentGAV(COMPONENT_TYPE_TCOMPV0);

        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
        if (librairesManagerService != null) {
            for (ComponentGAV gav : tcompv0Gavs) {
                File jarFile = librairesManagerService.resolveStatusLocally(gav.toMavenUri());
                if (jarFile == null) {
                    LOGGER.info("Component: {} was not installed", gav.toString());
                    toInstall = true;
                    break;
                }
            }
        }

        if (toInstall) {
            LOGGER.info("Component: {} is going to be installed", Arrays.toString(tcompv0Gavs.toArray()));
        }
        return toInstall;
    }

    @Override
    public boolean install(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (!this.needInstall()) {
            return false;
        }

        if (getJarFileDir() == null) {
            return false;
        }

        try {
            FileUtils.copyDirectory(getJarFileDir(), getM2RepositoryPath(), false);
            LOGGER.info("Jars inside: {} were copied to {}", getJarFileDir(), getM2RepositoryPath());
            return true;
        } catch (IOException e) {
            LOGGER.error("install error", e);
        }
        return false;
    }

    private File getM2RepositoryPath() {
        String configFolder = Platform.getConfigurationLocation().getURL().getPath();
        File mavenUserSettingFile = new File(configFolder, IProjectSettingTemplateConstants.MAVEN_USER_SETTING_TEMPLATE_FILE_NAME);
        File m2Repo = null;
        if (mavenUserSettingFile.exists()) {
            m2Repo = new File(MavenPlugin.getMaven().getLocalRepositoryPath());
        }
        if (m2Repo == null) {
            File m2Folder = new File(configFolder, ".m2");
            m2Repo = new File(m2Folder, "repository");
        }
        if (!m2Repo.exists()) {
            m2Repo.mkdirs();
        }
        return m2Repo;
    }

}
