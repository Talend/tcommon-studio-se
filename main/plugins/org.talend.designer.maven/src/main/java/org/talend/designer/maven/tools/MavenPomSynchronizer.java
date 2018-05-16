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
package org.talend.designer.maven.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ILibrariesService.IChangedLibrariesListener;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.utils.io.FilesUtils;

/**
 * created by ggu on 2 Feb 2015 Detailled comment
 *
 */
public class MavenPomSynchronizer {

    private final ITalendProcessJavaProject codeProject;

    private static boolean isListenerAdded;

    private static Object lock = new Object();

    private static IChangedLibrariesListener changedLibrariesListener;
    
    private IFile projectPomFile;

    public MavenPomSynchronizer(IProcessor processor, IFile pomFile) {
        this(processor.getTalendJavaProject());
        this.projectPomFile = pomFile;
    }

    public MavenPomSynchronizer(ITalendProcessJavaProject codeProject) {
        super();
        this.codeProject = codeProject;
        if (codeProject != null) {
            projectPomFile = codeProject.getProjectPom();
        }
    }

    /**
     * 
     * add the job to the pom modules list of project.
     */
    public void addChildModules(boolean removeOld, String... childModules) throws Exception {
        MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        Model projModel = mavenModelManager.readMavenModel(projectPomFile);
        List<String> modules = projModel.getModules();
        if (modules == null) {
            modules = new ArrayList<String>();
            projModel.setModules(modules);
        }

        boolean modifed = false;
        if (removeOld || childModules == null || childModules.length == 0) { // clean the modules
            if (!modules.isEmpty()) {
                modules.clear();
                modifed = true;
            }
        }

        final Iterator<String> iterator = modules.iterator();
        while (iterator.hasNext()) {
            String module = iterator.next();
            if (ArrayUtils.contains(childModules, module)) {
                iterator.remove(); // remove the exised one
            }
        }

        if (childModules != null) {
            // according to the arrays order to add the modules.
            for (String module : childModules) {
                if (module.length() > 0) {
                    modules.add(module);
                    modifed = true;
                }
            }
        }

        if (modifed) {
            // save pom.
            PomUtil.savePom(null, projModel, projectPomFile);
        }
    }

    /**
     * 
     * Clean the pom_xxx.xml and assembly_xxx.xml and target folder, also clean the module and dependencies.
     * 
     * another cleaning up for sources codes or such in @see DeleteAllJobWhenStartUp.
     */
    public void cleanMavenFiles(IProgressMonitor monitor) throws Exception {
        IProject jProject = codeProject.getProject();
        if (!jProject.isOpen()) {
            jProject.open(monitor);
        }
        // empty the src/main/java...
        IFolder srcFolder = codeProject.getSrcFolder();
        codeProject.cleanFolder(monitor, srcFolder);

        // empty resources
        IFolder resourcesFolder = codeProject.getExternalResourcesFolder();
        codeProject.cleanFolder(monitor, resourcesFolder);

        // empty the outputs, target
        IFolder targetFolder = codeProject.getTargetFolder();
        codeProject.cleanFolder(monitor, targetFolder);

        // empty the src/test/java
        IFolder testSrcFolder = codeProject.getTestSrcFolder();
        codeProject.cleanFolder(monitor, testSrcFolder);

        // empty the src/test/java (main for contexts)
        IFolder testResourcesFolder = codeProject.getTestResourcesFolder();
        codeProject.cleanFolder(monitor, testResourcesFolder);

        // rules
        IFolder rulesResFolder = codeProject.getResourceSubFolder(monitor, JavaUtils.JAVA_RULES_DIRECTORY);
        codeProject.cleanFolder(monitor, rulesResFolder);

        // sqltemplate
        IFolder sqlTemplateResFolder = codeProject.getResourceSubFolder(monitor, JavaUtils.JAVA_SQLPATTERNS_DIRECTORY);
        codeProject.cleanFolder(monitor, sqlTemplateResFolder);

        // clean all assemblies in src/main/assemblies
        // fullCleanupContainer(codeProject.getAssembliesFolder());

        // clean all items in src/main/items
        fullCleanupContainer(codeProject.getItemsFolder());

        // clean all items in tests
        fullCleanupContainer(codeProject.getTestsFolder());

        codeProject.getProject().refreshLocal(IResource.DEPTH_ONE, monitor);

    }

    public static void addChangeLibrariesListener() {
        if (!isListenerAdded) {
            synchronized (lock) {
                if (!isListenerAdded) {
                    if (!ProxyRepositoryFactory.getInstance().isFullLogonFinished()) {
                        return;
                    }
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        if (changedLibrariesListener == null) {
                            changedLibrariesListener = new ILibrariesService.IChangedLibrariesListener() {

                                @Override
                                public void afterChangingLibraries() {
                                    try {
                                        // update the dependencies
                                        new AggregatorPomsHelper().updateCodeProjects(new NullProgressMonitor());
                                    } catch (Exception e) {
                                        ExceptionHandler.process(e);
                                    }
                                }
                            };
                        }
                        libService.addChangeLibrariesListener(changedLibrariesListener);
                    }
                    isListenerAdded = true;
                }
            }

        }
    }

    public static void removeChangeLibrariesListener() {
        if (isListenerAdded) {
            synchronized (lock) {
                if (isListenerAdded) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        if (changedLibrariesListener != null) {
                            libService.removeChangeLibrariesListener(changedLibrariesListener);
                        }
                    }
                    isListenerAdded = false;
                }

            }
        }
    }

    private void fullCleanupContainer(IContainer container) {
        if (container != null && container.exists()) {
            FilesUtils.deleteFolder(container.getLocation().toFile(), false);
        }
    }

    private void cleanupContainer(IContainer container, FilenameFilter filter) {
        File folder = container.getLocation().toFile();
        if (filter != null) {
            deleteFiles(folder.listFiles(filter));
        } else {
            fullCleanupContainer(container);
        }
    }

    private void deleteFiles(File[] files) {
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    private static File findTestContextFile(File file) {
        if (file != null) {
            if (file.getName().endsWith(JavaUtils.JAVA_CONTEXT_EXTENSION)
                    && file.getParentFile().getName().equals(JavaUtils.JAVA_CONTEXTS_DIRECTORY)) {
                return file;
            }
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File f : listFiles) {
                        File contextFile = findTestContextFile(f);
                        if (contextFile != null) {
                            return contextFile;
                        }
                    }
                }
            }
        }
        return null;
    }

}
