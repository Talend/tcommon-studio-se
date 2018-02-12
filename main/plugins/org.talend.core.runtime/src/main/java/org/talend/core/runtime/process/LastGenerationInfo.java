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
package org.talend.core.runtime.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.JobInfo;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class LastGenerationInfo {

    private HashMap<String, Set<ModuleNeeded>> modulesNeededPerJob;

    private HashMap<String, Set<String>> routinesNeededPerJob;

    private HashMap<String, Set<String>> pigudfNeededPerJob;

    private HashMap<String, Set<ModuleNeeded>> modulesNeededWithSubjobPerJob;
    
    private Set<ModuleNeeded> highPriorityModuleNeeded;

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
        highPriorityModuleNeeded = new LinkedHashSet<ModuleNeeded>();
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        modulesNeededPerJob.put(key, new HashSet<ModuleNeeded>(modulesNeeded));
    }

    /**
     * Sets the modulesNeededWithSubjobPerJob.
     * 
     * @param modulesNeededWithSubjobPerJob the modulesNeededWithSubjobPerJob to set
     */
    public void setModulesNeededWithSubjobPerJob(String jobId, String jobVersion, Set<ModuleNeeded> modulesNeeded) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        contextPerJob.put(key, new HashSet<String>(contexts));
    }

    public void setUseDynamic(String jobId, String jobVersion, boolean dynamic) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        useDynamic.put(key, dynamic);
    }

    public boolean isUseDynamic(String jobId, String jobVersion) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        if (!useDynamic.containsKey(key)) {
            return false;
        }
        return useDynamic.get(key);
    }

    public HashMap<String, Boolean> getUseDynamicMap() {
        return this.useDynamic;
    }

    public void setUseRules(String jobId, String jobVersion, boolean useRules) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        this.useRules.put(key, useRules);
    }

    public boolean isUseRules(String jobId, String jobVersion) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        if (!useRules.containsKey(key)) {
            return false;
        }
        return useRules.get(key);
    }

    public HashMap<String, Boolean> getUseRulesMap() {
        return this.useRules;
    }

    public void setUsePigUDFs(String jobId, String jobVersion, boolean useRules) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        this.usedPigUDFs.put(key, useRules);
    }

    public boolean isUsePigUDFs(String jobId, String jobVersion) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        if (!routinesNeededPerJob.containsKey(key)) {
            routinesNeededPerJob.put(key, new HashSet<String>());
        }
        return routinesNeededPerJob.get(key);
    }
    
    public Set<ModuleNeeded> getHighPriorityModuleNeeded() {
        return highPriorityModuleNeeded;
    }
    
    public void setHighPriorityModuleNeeded(ModuleNeeded moduleNeeded) {
        highPriorityModuleNeeded.add(moduleNeeded);
    }

    /**
     * Getter for pigudfNeededPerJob.
     * 
     * @return the pigudfNeededPerJob
     */
    public Set<String> getPigudfNeededPerJob(String jobId, String jobVersion) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        routinesNeededPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    /**
     * Sets the pigudfNeededPerJob.
     * 
     * @param pigudfNeededPerJob the pigudfNeededPerJob to set
     */
    public void setPigudfNeededPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        pigudfNeededPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    /**
     * 
     * @return the modulesNeededPerJob
     */
    public Set<String> getRoutinesNeededWithSubjobPerJob(String jobId, String jobVersion) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
        routinesNeededWithSubjobPerJob.put(key, new HashSet<String>(modulesNeeded));
    }

    /**
     * Sets the pigudfNeededWithSubjobPerJob.
     * 
     * @param pigudfNeededWithSubjobPerJob the pigudfNeededWithSubjobPerJob to set
     */
    public void setPigudfNeededWithSubjobPerJob(String jobId, String jobVersion, Set<String> modulesNeeded) {
        String key = jobId + "_" + jobVersion; //$NON-NLS-1$
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
