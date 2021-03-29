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
package org.talend.core.runtime.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class LastGenerationInfo {

    private HashMap<String, Set<ModuleNeeded>> modulesNeededPerJob;

    private HashMap<String, Set<ModuleNeeded>> modulesNeededWithSubjobPerJob;

    private HashMap<String, Set<String>> routinesNeededPerJob;

    private HashMap<String, Set<String>> routinesNeededWithSubjobPerJob;

    private HashMap<String, Set<ModuleNeeded>> codesJarModulesNeededPerJob;

    private HashMap<String, Set<ModuleNeeded>> codesJarModulesNeededWithSubjobPerJob;

    private HashMap<String, Set<ModuleNeeded>> highPriorityModuleNeededPerJob;

    private HashMap<String, Set<ModuleNeeded>> highPriorityModuleNeeded;

    private HashMap<String, Set<ModuleNeeded>> testcaseModuleNeeded;

    private HashMap<String, Set<String>> contextPerJob;

    private HashMap<String, Boolean> useDynamic, useRules;

    private static LastGenerationInfo instance;

    private JobInfo lastMainJob;

    private JobInfo currentBuildJob;

    private Set<JobInfo> lastGeneratedjobs; // main job + child jobs

    private LastGenerationInfo() {
        modulesNeededPerJob = new HashMap<String, Set<ModuleNeeded>>();
        contextPerJob = new HashMap<>();
        modulesNeededWithSubjobPerJob = new HashMap<>();
        highPriorityModuleNeededPerJob = new HashMap<>();
        highPriorityModuleNeeded = new HashMap<>();
        testcaseModuleNeeded = new HashMap<>();
        lastGeneratedjobs = new HashSet<>();
        routinesNeededPerJob = new HashMap<>();
        routinesNeededWithSubjobPerJob = new HashMap<>();
        codesJarModulesNeededPerJob = new HashMap<>();
        codesJarModulesNeededWithSubjobPerJob = new HashMap<>();
        useDynamic = new HashMap<>();
        useRules = new HashMap<>();
    }

    public static LastGenerationInfo getInstance() {
        if (instance == null) {
            instance = new LastGenerationInfo();
        }
        return instance;
    }

    /**
     * @return the modulesNeededPerJob
     */
    // TODO check callers to see if need to add operations for codejars
    public Set<ModuleNeeded> getModulesNeededWithSubjobPerJob(String jobId, String jobVersion) {

        String key = this.getProcessKey(jobId, jobVersion);
        if (!modulesNeededWithSubjobPerJob.containsKey(key)) {
            modulesNeededWithSubjobPerJob.put(key, new HashSet<ModuleNeeded>());
        }
        return modulesNeededWithSubjobPerJob.get(key);
    }

    /**
     * Sets the modulesNeededWithSubjobPerJob.
     *
     * @param modulesNeededWithSubjobPerJob the modulesNeededWithSubjobPerJob to set
     */
    public void setModulesNeededWithSubjobPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (modulesNeeded == null) {
            modulesNeededWithSubjobPerJob.put(key, null);
        } else {
            modulesNeededWithSubjobPerJob.put(key, new HashSet<ModuleNeeded>(modulesNeeded));
        }
    }

    /**
     *
     * @return the modulesNeededPerJob
     */
    public Set<ModuleNeeded> getModulesNeededPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!modulesNeededPerJob.containsKey(key)) {
            modulesNeededPerJob.put(key, new HashSet<ModuleNeeded>());
        }
        return modulesNeededPerJob.get(key);
    }

    /**
     * Sets the modulesNeededPerJob.
     *
     * @param modulesNeededPerJob the modulesNeededPerJob to set
     */
    public void setModulesNeededPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion); 
        modulesNeededPerJob.put(key, new HashSet<ModuleNeeded>(modulesNeeded));
    }

    /**
     * Getter for contextPerJob.
     *
     * @return the contextPerJob
     */
    public Set<String> getContextPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!contextPerJob.containsKey(key)) {
            contextPerJob.put(key, new HashSet<String>());
        }
        return contextPerJob.get(key);
    }

    /**
     * Sets the contextPerJob.
     *
     * @param contextPerJob the contextPerJob to set
     */
    public void setContextPerJob(String jobId, String jobVersion, Set<String> contexts) {
        String key = this.getProcessKey(jobId, jobVersion); 
        contextPerJob.put(key, new HashSet<String>(contexts));
    }

    public void setUseDynamic(String jobId, String jobVersion, boolean dynamic) {
        String key = this.getProcessKey(jobId, jobVersion); 
        useDynamic.put(key, dynamic);
    }

    public boolean isUseDynamic(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion); 
        if (!useDynamic.containsKey(key)) {
            return false;
        }
        return useDynamic.get(key);
    }

    public HashMap<String, Boolean> getUseDynamicMap() {
        return this.useDynamic;
    }

    public void setUseRules(String jobId, String jobVersion, boolean useRules) {
        String key = this.getProcessKey(jobId, jobVersion); 
        this.useRules.put(key, useRules);
    }

    public boolean isUseRules(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion); 
        if (!useRules.containsKey(key)) {
            return false;
        }
        return useRules.get(key);
    }

    public HashMap<String, Boolean> getUseRulesMap() {
        return this.useRules;
    }

    /**
     * Getter for lastMainJob.
     *
     * @return the lastMainJob
     */
    public JobInfo getLastMainJob() {
        return this.lastMainJob;
    }

    /**
     * Sets the lastMainJob.
     *
     * @param lastMainJob the lastMainJob to set
     */
    public void setLastMainJob(JobInfo lastMainJob) {
        this.lastMainJob = lastMainJob;
    }

    public JobInfo getCurrentBuildJob() {
        return currentBuildJob;
    }

    public boolean isCurrentMainJob() {
        if (lastMainJob != null && currentBuildJob != null && lastMainJob.equals(currentBuildJob)) {
            return true;
        }
        return false;
    }

    public void setCurrentBuildJob(JobInfo currentBuildJob) {
        this.currentBuildJob = currentBuildJob;
    }

    /**
     * Getter for lastGeneratedjobs.
     *
     * @return the lastGeneratedjobs
     */
    public Set<JobInfo> getLastGeneratedjobs() {
        return this.lastGeneratedjobs;
    }

    /**
     * Sets the lastGeneratedjobs.
     *
     * @param lastGeneratedjobs the lastGeneratedjobs to set
     */
    public void setLastGeneratedjobs(Set<JobInfo> lastGeneratedjobs) {
        this.lastGeneratedjobs = lastGeneratedjobs;
    }

    /**
     *
     * @return the modulesNeededPerJob
     */
    public Set<String> getRoutinesNeededPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!routinesNeededPerJob.containsKey(key)) {
            routinesNeededPerJob.put(key, new HashSet<String>());
        }
        return routinesNeededPerJob.get(key);
    }

    /**
     * Sets the routinesNeededPerJob.
     *
     * @param modulesNeededPerJob the modulesNeededPerJob to set
     */
    public void setRoutinesNeededPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        routinesNeededPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    /**
     *
     * @return the modulesNeededPerJob
     */
    public Set<String> getRoutinesNeededWithSubjobPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!routinesNeededWithSubjobPerJob.containsKey(key)) {
            routinesNeededWithSubjobPerJob.put(key, new HashSet<String>());
        }
        return routinesNeededWithSubjobPerJob.get(key);
    }

    /**
     * Sets the routinesNeededPerJob.
     *
     * @param modulesNeededPerJob the modulesNeededPerJob to set
     */
    public void setRoutinesNeededWithSubjobPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        routinesNeededWithSubjobPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    public Set<ModuleNeeded> getCodesJarModulesNeededPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!codesJarModulesNeededPerJob.containsKey(key)) {
            codesJarModulesNeededPerJob.put(key, new HashSet<>());
        }
        return codesJarModulesNeededPerJob.get(key);
    }

    public void setCodesJarModulesNeededPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        codesJarModulesNeededPerJob.put(key, new HashSet<>(modulesNeeded));
    }

    public Set<ModuleNeeded> getCodesJarModulesNeededWithSubjobPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!codesJarModulesNeededWithSubjobPerJob.containsKey(key)) {
            codesJarModulesNeededWithSubjobPerJob.put(key, new HashSet<>());
        }
        return codesJarModulesNeededWithSubjobPerJob.get(key);
    }

    public void setCodesJarModulesNeededWithSubjobPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        codesJarModulesNeededWithSubjobPerJob.put(key, new HashSet<>(modulesNeeded));
    }

    public Set<ModuleNeeded> getHighPriorityModuleNeededPerJob(String jobId, String jobVersion) {
        String key = getProcessKey(jobId, jobVersion);
        if (!highPriorityModuleNeededPerJob.containsKey(key)) {
            highPriorityModuleNeededPerJob.put(key, new LinkedHashSet<>());
        }
        return highPriorityModuleNeededPerJob.get(key);
    }

    public void setHighPriorityModuleNeededPerJob(String jobId, String jobVersion, Set<ModuleNeeded> moduleNeeded) {
        String key = getProcessKey(jobId, jobVersion);
        if (!highPriorityModuleNeededPerJob.containsKey(key)) {
            highPriorityModuleNeededPerJob.put(key, new LinkedHashSet<>());
        }
        highPriorityModuleNeededPerJob.get(key).addAll(moduleNeeded);
    }

    public Set<ModuleNeeded> getHighPriorityModuleNeeded(String jobId, String jobVersion) {
        String key = getProcessKey(jobId, jobVersion);
        if (!highPriorityModuleNeeded.containsKey(key)) {
            highPriorityModuleNeeded.put(key, new LinkedHashSet<>());
        }
        return highPriorityModuleNeeded.get(key);
    }

    public void setHighPriorityModuleNeeded(String jobId, String jobVersion, Set<ModuleNeeded> moduleNeeded) {
        String key = getProcessKey(jobId, jobVersion);
        if (!highPriorityModuleNeeded.containsKey(key)) {
            highPriorityModuleNeeded.put(key, new LinkedHashSet<>());
        }
        highPriorityModuleNeeded.get(key).addAll(moduleNeeded);
    }

    public Set<ModuleNeeded> getTestcaseModuleNeeded(String jobId, String jobVersion) {
        String key = getProcessKey(jobId, jobVersion);
        if (!testcaseModuleNeeded.containsKey(key)) {
            testcaseModuleNeeded.put(key, new HashSet<>());
        }
        return testcaseModuleNeeded.get(key);
    }

    public void setTestcaseModuleNeeded(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        testcaseModuleNeeded.put(getProcessKey(jobId, jobVersion), new HashSet<>(modulesNeeded));
    }

    public void clearHighPriorityModuleNeeded() {
        highPriorityModuleNeededPerJob.clear();
        highPriorityModuleNeeded.clear();
    }

    private String getProcessKey(String jobId, String jobVersion) {
        String pureJobId = ProcessUtils.getPureItemId(jobId);
        return pureJobId + "_" + jobVersion; //$NON-NLS-1$
    }

    public void clearCaches() {
        clearHighPriorityModuleNeeded();
        modulesNeededPerJob.clear();
        modulesNeededWithSubjobPerJob.clear();
        testcaseModuleNeeded.clear();
    }

    public void clean() {
        modulesNeededPerJob.clear();
        routinesNeededPerJob.clear();
        modulesNeededWithSubjobPerJob.clear();
        clearHighPriorityModuleNeeded();
        testcaseModuleNeeded.clear();
        routinesNeededWithSubjobPerJob.clear();
        contextPerJob.clear();

        getUseDynamicMap().clear();
        getUseRulesMap().clear();

        lastMainJob = null;
        lastGeneratedjobs.clear();
        currentBuildJob = null;
    }
}
