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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ISVNProviderServiceInCoreRuntime;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.librariesmanager.model.ExtensionModuleManager;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public abstract class AbstractLibrariesService implements ILibrariesService {

    private static Logger log = Logger.getLogger(AbstractLibrariesService.class);

    private ILibraryManagerService localLibraryManager = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
            ILibraryManagerService.class);

    // protected String LIBS = "libs";

    @Override
    public abstract URL getRoutineTemplate();

    @Override
    public abstract URL getBeanTemplate();

    /**
     * 
     * DOC wchen Comment method "getLibraryStatus".
     * 
     * @deprecated better call the function getLibraryStatus(String libName, String mvnURI)
     * @param libName
     * @return
     * @throws BusinessException
     */
    @Deprecated
    @Override
    public ELibraryInstallStatus getLibraryStatus(String libName) throws BusinessException {
        ModuleNeeded testModule = new ModuleNeeded("", libName, "", true);
        return testModule.getStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getLibraryStatus(java.lang.String, java.lang.String)
     */
    @Override
    public ELibraryInstallStatus getLibraryStatus(String libName, String mvnURI) {
        ModuleNeeded testModule = new ModuleNeeded("", libName, "", true);
        testModule.setMavenUri(mvnURI);
        return testModule.getStatus();
    }

    @Override
    public void deployLibrary(URL source) throws IOException {
        deployLibrary(source, null, true);
    }

    @Override
    public void deployLibrary(URL source, String mavenUri) throws IOException {
        deployLibrary(source, mavenUri, true);
    }

    @Override
    public void deployLibrary(URL source, boolean refresh) throws IOException {
        deployLibrary(source, null, refresh);
    }

    @Override
    public void deployLibrary(URL source, String mavenUri, boolean refresh) throws IOException {
        deployLibrary(source, mavenUri, refresh, true);
    }

    public void deployLibrary(URL source, String mavenUri, boolean refresh, boolean updateNexusJar) throws IOException {
        String decode = null;
        if (source.getFile().contains("%20")) {
            decode = URLDecoder.decode(source.getFile(), "UTF-8");
        } else {
            decode = source.getFile();
        }
        final File sourceFile = new File(decode);

        localLibraryManager.deploy(sourceFile.toURI(), mavenUri, updateNexusJar);

        refreshLocal(new String[] { sourceFile.getName() });
        if (refresh) {
            checkLibraries();
        }
    }

    @Override
    public void deployLibrarys(URL[] source) throws IOException {
        String[] namse = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            URL url = source[i];
            namse[i] = new File(url.toString()).getName();
            deployLibrary(url, false);
        }
        refreshLocal(namse);
        checkLibraries();
    }

    private RepositoryContext getRepositoryContext() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    private void refreshLocal(final String names[]) {
        // for feature 12877
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        final String projectLabel = currentProject.getTechnicalLabel();

        // synchronize .Java project for all new jars.
        try {
            for (String name : names) {
                String jarPath = localLibraryManager.getJarPath(name);
                if (jarPath != null) {
                    File source = new File(jarPath);
                    if (source.exists()) {
                        synJavaLibs(source);
                    }
                }
            }
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        }

        // if svn libs setup from tac then ...
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            ISVNProviderServiceInCoreRuntime service = (ISVNProviderServiceInCoreRuntime) GlobalServiceRegister.getDefault()
                    .getService(ISVNProviderServiceInCoreRuntime.class);
            if (service != null) {
                File libFile = new File(LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA));
                // check local or remote
                boolean localConnectionProvider = true;
                IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                if (proxyRepositoryFactory != null) {
                    try {
                        localConnectionProvider = proxyRepositoryFactory.isLocalConnectionProvider();
                    } catch (PersistenceException e) {
                        //
                    }
                }
                if (!localConnectionProvider && service.isSvnLibSetupOnTAC() && service.isInSvn(libFile.getAbsolutePath())
                        && !getRepositoryContext().isOffline()) {
                    List<String> jars = new ArrayList<String>();
                    for (String name : names) {
                        jars.add(libFile.getAbsolutePath() + File.separatorChar + name);
                    }
                    service.deployNewJar(jars);
                    return;
                }
            }

        }

    }

    /**
     * DOC ycbai Comment method "synJavaLibs".
     *
     * <p>
     * Synchronize the lib of the same name with this lib in .Java\lib.
     * </p>
     *
     * @param lib
     * @throws IOException
     */
    private void synJavaLibs(File lib) throws IOException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = processService.getTalendProcessJavaProject();
            if (talendProcessJavaProject != null) {
                IFolder javaLibFolder = talendProcessJavaProject.getLibFolder();
                if (javaLibFolder.exists()) {
                    File libFolder = javaLibFolder.getLocation().toFile();
                    for (File externalLib : libFolder.listFiles(FilesUtils.getAcceptJARFilesFilter())) {
                        if (externalLib.getName().equals(lib.getName())) {
                            FilesUtils.copyFile(lib, externalLib);
                        }
                    }
                }
            }
        }

    }

    protected void addResolvedClasspathPath(String libName) {
    }

    @Override
    public void undeployLibrary(String jarName) throws IOException {
        ILibraryManagerService service = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        if (service.delete(jarName)) {
            ModulesNeededProvider.userRemoveUnusedModules(jarName);
            fireLibrariesChanges();
        } else {
            CommonExceptionHandler.process(new Exception("Can not remove the module " + jarName
                    + ", this is certainly a module from a component provider and not a user module"));
        }
    }

    @Override
    public List<Problem> getProblems(INode node, IElement element) {
        List<Problem> toReturn = new ArrayList<Problem>();
        List<ModuleNeeded> list = LibrariesManagerUtils.getNotInstalledModules(node);
        for (ModuleNeeded current : list) {
            Problem problem = new Problem(element, "Module " + current.getModuleName() + " required", //$NON-NLS-1$ //$NON-NLS-2$
                    ProblemStatus.ERROR);
            problem.setKey("Module_" + current.getModuleName());//$NON-NLS-1$
            toReturn.add(problem);
        }

        return toReturn;
    }

    @Override
    public void resetModulesNeeded() {
        ModulesNeededProvider.reset();
        ModuleStatusProvider.reset();
        checkLibraries();
    }

    @Override
    public void checkLibraries() {
        fireLibrariesChanges();
    }

    @Override
    public void addChangeLibrariesListener(IChangedLibrariesListener listener) {
        ModulesNeededProvider.addChangeLibrariesListener(listener);
    }

    @Override
    public void removeChangeLibrariesListener(IChangedLibrariesListener listener) {
        ModulesNeededProvider.removeChangeLibrariesListener(listener);
    }

    private void fireLibrariesChanges() {
        ModulesNeededProvider.fireLibrariesChanges();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.general.ILibrariesService#resetModulesNeededForCurrentJob(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public void updateModulesNeededForCurrentJob(IProcess process) {
        ModulesNeededProvider.resetCurrentJobNeededModuleList(process);
        checkLibraries();

    }

    @Override
    public void cleanLibs() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = processService.getTalendProcessJavaProject();
            if (talendProcessJavaProject != null) {
                IFolder libFolder = talendProcessJavaProject.getLibFolder();
                try {
                    talendProcessJavaProject.cleanFolder(null, libFolder);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    @Override
    public Set<ModuleNeeded> getCodesModuleNeededs(ERepositoryObjectType type) {
        return ModulesNeededProvider.getCodesModuleNeededs(type, false);
    }

    @Override
    public List<ModuleNeeded> getModuleNeeded(String id, boolean isGroup) {
        return ExtensionModuleManager.getInstance().getModuleNeeded(id, isGroup);
    }

    @Override
    public void deployProjectLibrary(File source) throws IOException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            ILibraryManagerService librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            if (librairesService != null) {
                File sourceFile = new File(librairesService.getJarPath(source.getName()));
                if (sourceFile.exists()) {
                    IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(JavaUtils.JAVA_EXTENSION)
                            .append(JavaUtils.JAVA_LIB_DIRECTORY).append(source.getName());
                    File targetFile = path.toFile();
                    if (targetFile.exists()) {
                        return;
                    }
                    FilesUtils.copyFile(sourceFile, targetFile);
                }
            }
        }
    }
}
