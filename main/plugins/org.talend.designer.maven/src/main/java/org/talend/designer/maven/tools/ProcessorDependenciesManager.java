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
package org.talend.designer.maven.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
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

            // add the job modules.
            Set<ModuleNeeded> neededLibraries = processor.getNeededModules();
            for (ModuleNeeded module : neededLibraries) {
                Dependency dependency = null;
                if (module.getDeployStatus() == ELibraryInstallStatus.DEPLOYED) {
                    dependency = PomUtil.createModuleDependency(module.getMavenUri(true));
                }
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

            return updateDependencies(progressMonitor, model, neededDependencies, false);

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
    public static boolean updateDependencies(IProgressMonitor progressMonitor, Model model, List<Dependency> neededDependencies,
            boolean fresh) throws ProcessorException {
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

}
