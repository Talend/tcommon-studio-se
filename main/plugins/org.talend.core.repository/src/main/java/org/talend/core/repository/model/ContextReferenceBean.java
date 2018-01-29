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
package org.talend.core.repository.model;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class ContextReferenceBean {

    private String relateName;

    private String relateVersion;

    private String relatePath;

    private String relateType;

    private String projectName;

    private boolean isrelate = true; // false, is joblet

    private boolean isDelete = false;

    public ContextReferenceBean(String relateName, String type, String relateVersion, String relatePath, String projectName) {
        this.relatePath = relatePath;
        this.relateName = relateName;
        this.relateVersion = relateVersion;
        this.projectName = projectName;
        this.relateType = type;
    }

    public void setJobFlag(boolean isrelate, boolean isDelete) {
        this.isrelate = isrelate;
        this.isDelete = isDelete;
    }

    public boolean isRelate() {
        return this.isrelate;
    }

    public boolean isDelete() {
        return this.isDelete;
    }

    public String getRelateName() {
        return this.relateName;
    }

    public String getRelateVersion() {
        return this.relateVersion;
    }

    public String getRelatePath() {
        return this.relatePath;
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

        sb.append(checkValue(relateName));
        sb.append(" "); //$NON-NLS-1$
        sb.append(checkValue(relateVersion));
        sb.append(" "); //$NON-NLS-1$
        sb.append(checkValue(relatePath));

        return sb.toString();
    }

    private String checkValue(String value) {
        return value == null ? "" : value; //$NON-NLS-1$
    }

    public String getRelateType() {
        return this.relateType;
    }

    public void setRelateType(String relateType) {
        this.relateType = relateType;
    }
}
