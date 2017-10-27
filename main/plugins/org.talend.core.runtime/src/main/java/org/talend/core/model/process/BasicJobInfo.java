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
package org.talend.core.model.process;

/**
 * DOC nrousseau ProcessController class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getContextName()
     */
    public String getContextName() {
        return contextName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setContextName(java.lang.String)
     */
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobId()
     */
    public String getJobId() {
        return jobId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobId(java.lang.String)
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getProcess()
     */
    public IProcess getProcess() {
        return process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setProcess(org.talend.core.model.process.IProcess)
     */
    public void setProcess(IProcess process) {
        this.process = process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getContext()
     */
    public IContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setContext(org.talend.core.model.process.IContext)
     */
    public void setContext(IContext context) {
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobVersion()
     */
    public String getJobVersion() {
        return this.jobVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobVersion(java.lang.String)
     */
    public void setJobVersion(String jobVersion) {
        this.jobVersion = jobVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#isApplyContextToChildren()
     */
    public boolean isApplyContextToChildren() {
        return this.applyContextToChildren;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setApplyContextToChildren(boolean)
     */
    public void setApplyContextToChildren(boolean applyContextToChildren) {
        this.applyContextToChildren = applyContextToChildren;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobName()
     */
    public String getJobName() {
        return this.jobName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobName(java.lang.String)
     */
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
        // if (context == null) {
        // if (other.context != null) {
        // return false;
        // }
        // } else if (!context.equals(other.context)) {
        // return false;
        // }
        // if (process == null) {
        // if (other.process != null) {
        // return false;
        // }
        // } else if (!process.equals(other.process)) {
        // return false;
        // }
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getFatherJobInfo()
     */
    public BasicJobInfo getFatherJobInfo() {
        return this.fatherJobInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setFatherJobInfo(org.talend.designer.runprocess.IJobInfo)
     */
    public void setFatherJobInfo(BasicJobInfo fatherJobInfo) {
        this.fatherJobInfo = fatherJobInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#isForceRegenerate()
     */
    public boolean isForceRegenerate() {
        return this.forceRegenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setForceRegenerate(boolean)
     */
    public void setForceRegenerate(boolean forceRegenerate) {
        this.forceRegenerate = forceRegenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getProjectFolderName()
     */
    public String getProjectFolderName() {
        return this.projectFolderName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setProjectFolderName(java.lang.String)
     */
    public void setProjectFolderName(String projectFolderName) {
        this.projectFolderName = projectFolderName;
    }
}
