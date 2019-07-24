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

    private HashMap<String, Set<String>> routinesNeededPerJob;

    private HashMap<String, Set<String>> pigudfNeededPerJob;

    private HashMap<String, Set<ModuleNeeded>> modulesNeededWithSubjobPerJob;

    private HashMap<String, Set<ModuleNeeded>> highPriorityModuleNeeded;

    private HashMap<String, Set<String>> routinesNeededWithSubjobPerJob;

    private HashMap<String, Set<String>> pigudfNeededWithSubjobPerJob;

    private HashMap<String, Set<String>> contextPerJob;

    private HashMap<String, Boolean> useDynamic, useRules, usedPigUDFs;

    private static LastGenerationInfo instance;

    private JobInfo lastMainJob;

    private JobInfo currentBuildJob;

    private Set<JobInfo> lastGeneratedjobs; // main job + child jobs

    private LastGenerationInfo() {
        modulesNeededPerJob = new HashMap<String, Set<ModuleNeeded>>();
        contextPerJob = new HashMap<String, Set<String>>();
        modulesNeededWithSubjobPerJob = new HashMap<String, Set<ModuleNeeded>>();
        highPriorityModuleNeeded = new HashMap<>();
        lastGeneratedjobs = new HashSet<JobInfo>();
        routinesNeededPerJob = new HashMap<String, Set<String>>();
        pigudfNeededPerJob = new HashMap<String, Set<String>>();
        routinesNeededWithSubjobPerJob = new HashMap<String, Set<String>>();
        pigudfNeededWithSubjobPerJob = new HashMap<String, Set<String>>();
        useDynamic = new HashMap<String, Boolean>();
        useRules = new HashMap<String, Boolean>();
        usedPigUDFs = new HashMap<String, Boolean>();
    }

    public static LastGenerationInfo getInstance() {
        if (instance == null) {
            instance = new LastGenerationInfo();
        }
        return instance;
    }

    /**
     *
     * @return the modulesNeededPerJob
     */
    public Set<ModuleNeeded> getModulesNeededWithSubjobPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!modulesNeededWithSubjobPerJob.containsKey(key)) {
            modulesNeededWithSubjobPerJob.put(key, new HashSet<ModuleNeeded>());
        }
        return modulesNeededWithSubjobPerJob.get(key);
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
     * Sets the modulesNeededPerJob.
     *
     * @param modulesNeededPerJob the modulesNeededPerJob to set
     */
    public void setModulesNeededPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion); 
        modulesNeededPerJob.put(key, new HashSet<ModuleNeeded>(modulesNeeded));
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

    public void setUsePigUDFs(String jobId, String jobVersion, boolean useRules) {
        String key = this.getProcessKey(jobId, jobVersion); 
        this.usedPigUDFs.put(key, useRules);
    }

    public boolean isUsePigUDFs(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion); 
        if (!usedPigUDFs.containsKey(key)) {
            return false;
        }
        return usedPigUDFs.get(key);
    }

    public HashMap<String, Boolean> getUsePigUDFsMap() {
        return this.usedPigUDFs;
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

    public void clearHighPriorityModuleNeeded() {
        highPriorityModuleNeeded.clear();
    }

    private String getProcessKey(String jobId, String jobVersion) {
        String pureJobId = ProcessUtils.getPureItemId(jobId);
        return pureJobId + "_" + jobVersion; //$NON-NLS-1$
    }

    /**
     * Getter for pigudfNeededPerJob.
     *
     * @return the pigudfNeededPerJob
     */
    public Set<String> getPigudfNeededPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!pigudfNeededPerJob.containsKey(key)) {
            pigudfNeededPerJob.put(key, new HashSet<String>());
        }
        return pigudfNeededPerJob.get(key);

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
     * Sets the pigudfNeededPerJob.
     *
     * @param pigudfNeededPerJob the pigudfNeededPerJob to set
     */
    public void setPigudfNeededPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        pigudfNeededPerJob.put(key, new HashSet<String>(modulesNeeded));
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
     * Getter for pigudfNeededWithSubjobPerJob.
     *
     * @return the pigudfNeededWithSubjobPerJob
     */
    public Set<String> getPigudfNeededWithSubjobPerJob(String jobId, String jobVersion) {
        String key = this.getProcessKey(jobId, jobVersion);
        if (!pigudfNeededWithSubjobPerJob.containsKey(key)) {
            pigudfNeededWithSubjobPerJob.put(key, new HashSet<String>());
        }
        return pigudfNeededWithSubjobPerJob.get(key);

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

    /**
     * Sets the pigudfNeededWithSubjobPerJob.
     *
     * @param pigudfNeededWithSubjobPerJob the pigudfNeededWithSubjobPerJob to set
     */
    public void setPigudfNeededWithSubjobPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = this.getProcessKey(jobId, jobVersion);
        pigudfNeededWithSubjobPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    public void clearModulesNeededWithSubjobPerJob() {
        if (!modulesNeededWithSubjobPerJob.isEmpty()) {
            modulesNeededWithSubjobPerJob.clear();
        }
    }

    public void clean() {
        modulesNeededPerJob.clear();
        routinesNeededPerJob.clear();
        pigudfNeededPerJob.clear();
        modulesNeededWithSubjobPerJob.clear();
        highPriorityModuleNeeded.clear();
        routinesNeededWithSubjobPerJob.clear();
        pigudfNeededWithSubjobPerJob.clear();
        contextPerJob.clear();

        getUseDynamicMap().clear();
        getUsePigUDFsMap().clear();
        getUseRulesMap().clear();

        lastMainJob = null;
        lastGeneratedjobs.clear();
        currentBuildJob = null;
    }
}
