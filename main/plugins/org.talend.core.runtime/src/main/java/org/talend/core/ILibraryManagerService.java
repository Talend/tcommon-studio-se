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
package org.talend.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.TalendLibsServerManager;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public interface ILibraryManagerService extends IService {

    /**
     * DOC ycbai Comment method "isInitialized".
     * 
     * Check whether the OBR has been initialized.
     * 
     * @param monitorWrap
     * @return
     */
    public boolean isInitialized();

    public void setInitialized();

    public void installModules(Collection<ModuleNeeded> modules, IProgressMonitor monitorWrap);

    /**
     * 
     * DOC wchen Comment method "deploy". deploy jars not exist in maven repository(nexus if configured in TAC)
     * 
     * @param jarFileUri
     * @param monitorWrap
     */
    public void deploy(URI jarFileUri, IProgressMonitor... monitorWrap);

    public void deploy(URI jarFileUri, String mavenUri, IProgressMonitor... monitorWrap);

    public void deploy(URI jarFileUri, String mavenUri, boolean updateNexusJar, IProgressMonitor... monitorWrap);


    /**
     * 
     * DOC wchen Comment method "deploy".Deploy moduleName:platformUri index to LibrariesIndex.xml
     * 
     * @param libsToRelativePath
     * @param monitorWrap
     */
    public void savePlatfromURLIndex(Map<String, String> libsToRelativePath, IProgressMonitor... monitorWrap);

    /**
     * 
     * DOC Talend Comment method "deployMavenIndex".Deploy moduleName:mavenUri index to MavenUriIndex.xml
     * 
     * @param libsToMavenUri
     * @param monitorWrap
     */
    public void saveMavenIndex(Map<String, String> libsToMavenUri, IProgressMonitor... monitorWrap);

    public void createModulesIndexFromComponentAndExtension(IProgressMonitor... monitorWrap);

    /**
     * DOC ycbai Comment method "retrieve".
     * 
     * Retrieve jar file from OBR.
     * 
     * @param jarNeeded
     * @param pathToStore
     * @param monitorWrap
     * @return
     */
    public boolean retrieve(String jarNeeded, String pathToStore, IProgressMonitor... monitorWrap);

    public boolean retrieve(String jarNeeded, String pathToStore, boolean showDialog, IProgressMonitor... monitorWrap);

    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, IProgressMonitor... monitorWrap);

    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap);

    public boolean retrieve(Set<ModuleNeeded> modulesNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap);

    public boolean retrieve(ModuleNeeded module, String pathToStore, boolean showDialog, IProgressMonitor... monitorWrap);

    /**
     * 
     * Resolve the jar from custom nexus
     * 
     * @param manager
     * @param customNexusServer
     * @param uri
     * @return
     * @throws Exception
     * @throws IOException
     */
    public File resolveJar(final ArtifactRepositoryBean customNexusServer, String uri) throws Exception, IOException;

    public File resolveStatusLocally(String mvnUriStatusKey);

    /**
     * List all the jars (or other files) available.
     * 
     * @param monitorWrap
     * @return
     */
    public Set<String> list(IProgressMonitor... monitorWrap);

    /**
     * 
     * get jar path by jar name.
     * 
     * @param jarName
     * @return
     */
    public String getJarPath(String jarName);

    public String getJarPathFromMaven(String jarNameOrMavenUri);

    @Deprecated
    public Set<String> listAllDllFiles();

    public boolean delete(String jarName);

    public boolean contains(String jarName);

    public void clearCache();

    @Deprecated
    public Set<String> list(boolean withComponent, IProgressMonitor... monitorWrap);

    public boolean checkJarInstalledFromPlatform(String uriPath);

    /**
     * deploy jars from lib/java to local maven if any jar already exist in maven and need update , for jars that not
     * exist in maven won't be deploy by this funciton , those jars should be deploy when it is needed Comment method
     * "synToLocalMaven".
     */
    public void synToLocalMaven();

    public String getMavenUriFromIndex(String jarName);

    public void setCustomMavenURI(String orignalURI, String customURI);

    public String getCustomMavenURI(String orignalURI);

    public void saveCustomMavenURIMap();

    public String getPlatformURLFromIndex(String jarName);

    /**
     * DOC ycbai Comment method "isJarExistInLibFolder".
     * <p>
     * Estimate if the jar is exist in talend lib folder(and same content).
     * </p>
     * 
     * @param jarFile
     * @return
     */
    public boolean isJarExistInLibFolder(File jarFile);

    /**
     * DOC ycbai Comment method "isLocalJarSameAsNexus".
     * <p>
     * Estimate if the local jar is same as the one in Nexus by comparing the Sha1.
     * </p>
     * 
     * @param manager
     * @param customNexusServer
     * @param jarUri
     * @return
     */
    public boolean isLocalJarSameAsNexus(TalendLibsServerManager manager, final ArtifactRepositoryBean customNexusServer, String jarUri);

    /**
     * DOC ycbai Comment method "isJarNeedToBeDeployed".
     * <p>
     * Estimate if need to deploy the jar file. If the jar is not exist in svn lib folder or not same as the one in
     * Nexus will need to deploy it.
     * </p>
     * 
     * @param jarFile
     * @return
     */
    public boolean isJarNeedToBeDeployed(File jarFile);

    public void checkModuleStatus(ModuleNeeded module);

}
