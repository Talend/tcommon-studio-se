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
package org.talend.core.repository.model;

/**
 * ggu class global comment. Detailled comment
 */
public class JobletReferenceBean {

    private String jobName;

    private String jobVersion;

    private String jobPath;

    private String projectName;

    private int nodeNum = 1;

    private boolean isJob = true; // false, is joblet

    private boolean isDelete = false;

    public JobletReferenceBean(String jobName, String jobVersion, String jobPath, String projectName) {
        this.jobPath = jobPath;
        this.jobName = jobName;
        this.jobVersion = jobVersion;
        this.projectName = projectName;
    }

    public void setJobFlag(boolean isJob, boolean isDelete) {
        this.isJob = isJob;
        this.isDelete = isDelete;
    }

    public boolean isJob() {
        return this.isJob;
    }

    public boolean isDelete() {
        return this.isDelete;
    }

    public String getJobName() {
        return this.jobName;
    }

    public String getJobVersion() {
        return this.jobVersion;
    }

    public String getJobPath() {
        return this.jobPath;
    }

    public int getNodeNum() {
        return this.nodeNum;
    }

    public void addNodeNum() {
        this.nodeNum = this.nodeNum + 1;

    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(checkValue(jobName));
        sb.append(" "); //$NON-NLS-1$
        sb.append(checkValue(jobVersion));
        sb.append(" "); //$NON-NLS-1$
        sb.append(checkValue(jobPath));

        return sb.toString();
    }

    private String checkValue(String value) {
        return value == null ? "" : value; //$NON-NLS-1$
    }
}
