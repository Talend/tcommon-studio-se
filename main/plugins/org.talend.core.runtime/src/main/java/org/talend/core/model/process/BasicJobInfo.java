// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.process;

public class BasicJobInfo {

    private String jobId, jobName, contextName, jobVersion;

    private IProcess process;

    private IContext context;

    boolean applyContextToChildren = false;

    private BasicJobInfo fatherJobInfo;

    private boolean forceRegenerate;

    private String projectFolderName;

    public BasicJobInfo(String jobId, String contextName, String version) {
        this.jobId = jobId;
        this.contextName = contextName;
        this.jobVersion = version;
    }

    public BasicJobInfo(IProcess process, IContext context) {
        jobId = process.getId();
        jobName = process.getName();
        contextName = context.getName();
        jobVersion = process.getVersion();
        this.context = context;
        this.process = process;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public IProcess getProcess() {
        return process;
    }

    public void setProcess(IProcess process) {
        this.process = process;
    }

    public IContext getContext() {
        return context;
    }

    public void setContext(IContext context) {
        this.context = context;
    }

    public String getJobVersion() {
        return this.jobVersion;
    }

    public void setJobVersion(String jobVersion) {
        this.jobVersion = jobVersion;
    }

    public boolean isApplyContextToChildren() {
        return this.applyContextToChildren;
    }

    public void setApplyContextToChildren(boolean applyContextToChildren) {
        this.applyContextToChildren = applyContextToChildren;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
        result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
        result = prime * result + ((jobVersion == null) ? 0 : jobVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        final BasicJobInfo other = (BasicJobInfo) obj;
        if (contextName == null) {
            if (other.contextName != null) {
                return false;
            }
        } else if (!contextName.equals(other.contextName)) {
            return false;
        }
        if (jobId == null) {
            if (other.jobId != null) {
                return false;
            }
        } else if (!jobId.equals(other.jobId)) {
            return false;
        }
        if (jobVersion == null) {
            if (other.jobVersion != null) {
                return false;
            }
        } else if (!jobVersion.equals(other.jobVersion)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "job:" + jobName + " / context:" + contextName + " / version:" + jobVersion; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public BasicJobInfo getFatherJobInfo() {
        return this.fatherJobInfo;
    }

    public void setFatherJobInfo(BasicJobInfo fatherJobInfo) {
        this.fatherJobInfo = fatherJobInfo;
    }

    public boolean isForceRegenerate() {
        return this.forceRegenerate;
    }

    public void setForceRegenerate(boolean forceRegenerate) {
        this.forceRegenerate = forceRegenerate;
    }

    public String getProjectFolderName() {
        return this.projectFolderName;
    }

    public void setProjectFolderName(String projectFolderName) {
        this.projectFolderName = projectFolderName;
    }
}
