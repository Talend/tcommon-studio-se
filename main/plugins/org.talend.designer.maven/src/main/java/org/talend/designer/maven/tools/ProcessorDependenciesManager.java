// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.maven.utils.SortableDependency;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;

/**
 * created by ycbai on 2015年4月2日 Detailled comment
 *
 */
public class ProcessorDependenciesManager {

    private final IProcessor processor;

    private final Property property;

    public ProcessorDependenciesManager(IProcessor processor) {
        this.processor = processor;
        property = processor.getProperty();
    }

    /**
     * Will add the dependencies to the maven model.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean updateDependencies(IProgressMonitor progressMonitor, Model model) throws ProcessorException {
        try {
            Set<ModuleNeeded> neededLibraries = new HashSet<>();
            Set<String> uniqueDependencies = new HashSet<>();
            Set<ModuleNeeded> jobModules = getProcessNeededModules();
            Set<ModuleNeeded> testcaseModules = getTestcaseNeededModules(property);
            neededLibraries.addAll(jobModules);
            neededLibraries.addAll(testcaseModules);
            if (!neededLibraries.isEmpty()) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                    ILibraryManagerService repositoryBundleService = GlobalServiceRegister.getDefault()
                            .getService(ILibraryManagerService.class);
                    repositoryBundleService.installModules(neededLibraries, null);
                }
            }
            List neededDependencies = new ArrayList<>();
            neededDependencies.addAll(convertToDependency(jobModules, uniqueDependencies, false));
            neededDependencies.addAll(convertToDependency(testcaseModules, uniqueDependencies, true));
            Collections.sort(neededDependencies);
            boolean fresh = false;
            if (property != null && property.getItem() != null && processor.getProcess() instanceof IProcess2) {
                // is standard job.
                fresh = true;
            }
            return updateDependencies(progressMonitor, model, neededDependencies, fresh);
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List convertToDependency(Set<ModuleNeeded> neededLibraries, Set<String> uniqueDependencies, boolean optional) {
        List neededDependencies = new ArrayList<>();
        for (ModuleNeeded module : neededLibraries) {
            final String mavenUri = module.getMavenUri();
            if (uniqueDependencies.contains(mavenUri)) {
                continue; // must be same GAV, avoid the different other attrs for modules
            }
            uniqueDependencies.add(mavenUri);
            Dependency dependency = PomUtil.createModuleDependency(mavenUri);
            if (dependency != null) {
                ((SortableDependency) dependency).setAssemblyOptional(optional);
                if (module.isExcludeDependencies()) {
                    Exclusion exclusion = new Exclusion();
                    exclusion.setGroupId("*"); //$NON-NLS-1$
                    exclusion.setArtifactId("*"); //$NON-NLS-1$
                    dependency.addExclusion(exclusion);
                }
                neededDependencies.add(dependency);
            }
        }
        return neededDependencies;
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

    private Set<ModuleNeeded> getProcessNeededModules() {
        Set<ModuleNeeded> neededLibraries = LastGenerationInfo.getInstance()
                .getModulesNeededPerJob(processor.getProcess().getId(), processor.getProcess().getVersion());
        if (neededLibraries.isEmpty()) {
            neededLibraries = processor.getNeededModules(TalendProcessOptionConstants.MODULES_DEFAULT);
        }
        return neededLibraries;
    }

    public static Set<ModuleNeeded> getTestcaseNeededModules(Property property) {
        if (property == null || !(property.getItem() instanceof ProcessItem)) {
            return Collections.emptySet();
        }
        Set<ModuleNeeded> testcaseModules = LastGenerationInfo.getInstance().getTestcaseModuleNeeded(property.getId(),
                property.getVersion());
        if (testcaseModules.isEmpty()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testcontainerService = GlobalServiceRegister.getDefault()
                        .getService(ITestContainerProviderService.class);
                try {
                    testcaseModules = testcontainerService.getAllJobTestcaseModules((ProcessItem) property.getItem());
                    LastGenerationInfo.getInstance().setTestcaseModuleNeeded(property.getId(), property.getVersion(),
                            testcaseModules);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return testcaseModules;
    }

}
