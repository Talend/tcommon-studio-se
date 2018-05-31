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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;

/**
 * created by ycbai on 2015年4月2日 Detailled comment
 *
 */
public class ProcessorDependenciesManager {

    private final IProcessor processor;

    public ProcessorDependenciesManager(IProcessor processor) {
        this.processor = processor;
    }

    /**
     * Will add the dependencies to the maven model.
     */
    public boolean updateDependencies(IProgressMonitor progressMonitor, Model model) throws ProcessorException {
        try {
            List neededDependencies = new ArrayList<Dependency>();
            Set<ModuleNeeded> neededLibraries = getAllModuleNeededWithTestCase();
            Set<String> uniquDependenciesSet = new HashSet<>();

            for (ModuleNeeded module : neededLibraries) {
                Dependency dependency = null;
                // if (module.getDeployStatus() == ELibraryInstallStatus.DEPLOYED) {
                // }
                final String mavenUri = module.getMavenUri();
                if (uniquDependenciesSet.contains(mavenUri)) {
                    continue; // must be same GAV, avoid the different other attrs for modules
                }
                uniquDependenciesSet.add(mavenUri);
                dependency = PomUtil.createModuleDependency(mavenUri);
                if (dependency != null) {
                    if (module.isExcludeDependencies()) {
                        Exclusion exclusion = new Exclusion();
                        exclusion.setGroupId("*"); //$NON-NLS-1$
                        exclusion.setArtifactId("*"); //$NON-NLS-1$
                        dependency.addExclusion(exclusion);
                    }
                    neededDependencies.add(dependency);
                }
            }

            java.util.Collections.sort(neededDependencies);
            boolean fresh = false;
            if (processor.getProperty() != null && processor.getProperty().getItem() != null
                    && processor.getProcess() instanceof IProcess2) {
                // is standard job.
                fresh = true;
            }
            return updateDependencies(progressMonitor, model, neededDependencies, fresh);

        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }

    /**
     * 
     * DOC ggu Comment method "updateDependencies". add the job Needed Libraries for current model.
     * 
     * @param model the job of pom model
     * @param fresh if true, will remove old dependencies, else will add the new dependencies in the head.
     * @return if there are some changes, will return true
     */
    public static boolean updateDependencies(IProgressMonitor progressMonitor, Model model,
            List<Dependency> neededDependencies, boolean fresh) throws ProcessorException {
        boolean changed = false;
        try {
            List<Dependency> existedDependencies = model.getDependencies();
            if (existedDependencies == null) {
                existedDependencies = new ArrayList<Dependency>();
                model.setDependencies(existedDependencies);
            }
            // record existed list
            Map<String, Dependency> existedDependenciesMap = new LinkedHashMap<String, Dependency>();
            if (!fresh) { // just in order to make the performance better.
                for (Dependency dependency : existedDependencies) {
                    // need remove the old non-existed dependencies, else won't compile the project.
                    if (!PomUtil.isAvailable(dependency)) {
                        continue;
                    }
                    existedDependenciesMap.put(PomUtil.generateMvnUrl(dependency), dependency);
                }
            }
            // clear all of existed list
            existedDependencies.clear();

            for (Dependency dependency : neededDependencies) {
                Dependency cloneDependency = dependency.clone();
                /*
                 * FIXME, Should keep the missing Dependency always. First make sure to download auto when do "mvn"
                 * command. Also enable to check which Dependencies are missed.
                 */
                // if (!PomUtil.isAvailable(cloneDependency)) {
                // continue;
                // }
                existedDependencies.add(cloneDependency); // add the needed in the head.

                if (fresh) {
                    changed = true; // after added, true always
                } else {
                    // remove it in old list.
                    String mvnUrl = PomUtil.generateMvnUrl(dependency);
                    Dependency existedDependency = existedDependenciesMap.remove(mvnUrl);
                    if (existedDependency != null) { // existed before.
                        // nothing to do.
                    } else { // added new
                        changed = true;
                    }
                }
            }

            if (!fresh) {
                // add the left dependencies.
                existedDependencies.addAll(existedDependenciesMap.values());
            }
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
        return changed;
    }

    private Set<ModuleNeeded> getAllModuleNeededWithTestCase() throws PersistenceException {
        // add the job modules.
        Set<ModuleNeeded> neededLibraries = new HashSet<>();
        boolean needTestCase = false;
        List<ProcessItem> testContainers = null;
        ProcessItem item = null;
        if (processor.getProperty() != null && processor.getProperty().getItem() instanceof ProcessItem) {
            item = (ProcessItem) processor.getProperty().getItem();
        }
        ITestContainerProviderService testContainerService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            testContainerService = (ITestContainerProviderService) GlobalServiceRegister.getDefault().getService(
                    ITestContainerProviderService.class);
            if (item != null) {
                boolean isTestCase = testContainerService.isTestContainerItem(item);
                boolean isLatestJob = false;
                if (isTestCase) {
                    item = (ProcessItem) testContainerService.getParentJobItem(item);
                } else {
                    IRepositoryViewObject object =
                            ProxyRepositoryFactory.getInstance().getLastVersion(item.getProperty().getId());
                    if (object != null
                            && VersionUtils.compareTo(item.getProperty().getVersion(), object.getVersion()) == 0) {
                        isLatestJob = true;
                    }
                }
                if (isTestCase || isLatestJob) {
                    testContainers = testContainerService.getAllTestContainers(item);
                    getAllLatestTestContainers(testContainers);
                    if (testContainers != null && !testContainers.isEmpty()) {
                        needTestCase = true;
                    }
                }
            }
        }
        Collection<ModuleNeeded> modulesNeeded = LastGenerationInfo.getInstance().getModulesNeededPerJob(
                processor.getProcess().getId(), processor.getProcess().getVersion());
        if (modulesNeeded.isEmpty()) {
            modulesNeeded = processor.getNeededModules(TalendProcessOptionConstants.MODULES_WITH_JOBLET);
        }
        neededLibraries.addAll(modulesNeeded);
        if (needTestCase) {
            for (ProcessItem testcaseItem : testContainers) {
                IProcess testcaseProcess = getDesignerCoreService().getProcessFromProcessItem(testcaseItem);
                neededLibraries.addAll(testcaseProcess.getNeededModules(TalendProcessOptionConstants.MODULES_DEFAULT));
            }
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                ILibraryManagerService repositoryBundleService =
                        (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                                ILibraryManagerService.class);
                repositoryBundleService.installModules(neededLibraries, null);
            }
        }
        return neededLibraries;
    }

    private void getAllLatestTestContainers(List<ProcessItem> testContainers) {
        Map<String, ProcessItem> latestTestContainers = new HashMap<>();
        if (testContainers != null) {
            for (ProcessItem item : testContainers) {
                String id = item.getProperty().getId();
                ProcessItem latestItem = latestTestContainers.get(id);
                if (latestItem == null) {
                    latestTestContainers.put(id, item);
                } else {
                    String currentVersion = item.getProperty().getVersion();
                    String latestVersion = latestItem.getProperty().getVersion();
                    if (VersionUtils.compareTo(currentVersion, latestVersion) > 0) {
                        latestTestContainers.put(id, item);
                    }
                }
            }
            testContainers.clear();
            testContainers.addAll(latestTestContainers.values());
        }
    }

    private IDesignerCoreService getDesignerCoreService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            return (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(IDesignerCoreService.class);
        }
        return null;
    }
}
