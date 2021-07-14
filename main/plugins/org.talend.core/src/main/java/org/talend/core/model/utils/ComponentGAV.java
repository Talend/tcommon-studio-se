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
package org.talend.core.model.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author bhe created on Jul 14, 2021
 *
 */
public class ComponentGAV {

    private String groupId;

    private String artifactId;

    private String version;

    private String classifier;

    private String type;

    private int componentType;

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId the artifactId to set
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the classifier
     */
    public String getClassifier() {
        return classifier;
    }

    /**
     * @param classifier the classifier to set
     */
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the componentType
     */
    public int getComponentType() {
        return componentType;
    }

    /**
     * @param componentType the componentType to set
     */
    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public String toMavenUri() {
        StringBuffer sb = new StringBuffer();
        sb.append("mvn:");
        if (!StringUtils.isEmpty(groupId)) {
            sb.append(this.groupId);
        }

        if (!StringUtils.isEmpty(artifactId)) {
            if (sb.length() > 0) {
                sb.append("/");
            }
            sb.append(this.artifactId);
        }

        if (!StringUtils.isEmpty(version)) {
            sb.append("/");
            sb.append(this.version);
        }
        if (!StringUtils.isEmpty(classifier)) {
            sb.append("/");
            sb.append(this.classifier);
        }

        sb.append("/");
        if (!StringUtils.isEmpty(type)) {
            sb.append(type);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComponentGAV)) {
            return false;
        }

        ComponentGAV thatObj = (ComponentGAV) obj;

        if (!StringUtils.equals(this.getGroupId(), thatObj.getGroupId())) {
            return false;
        }
        if (!StringUtils.equals(this.getArtifactId(), thatObj.getArtifactId())) {
            return false;
        }
        if (!StringUtils.equals(this.getVersion(), thatObj.getVersion())) {
            return false;
        }
        if (!StringUtils.equals(this.getType(), thatObj.getType())) {
            return false;
        }
        if (!StringUtils.equals(this.getClassifier(), thatObj.getClassifier())) {
            return false;
        }

        return this.getComponentType() == thatObj.getComponentType();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.getGroupId() == null ? 0 : this.getGroupId().hashCode());
        result = prime * result + (this.getArtifactId() == null ? 0 : this.getArtifactId().hashCode());
        result = prime * result + (this.getVersion() == null ? 0 : this.getVersion().hashCode());
        result = prime * result + (this.getType() == null ? 0 : this.getType().hashCode());
        result = prime * result + (this.getClassifier() == null ? 0 : this.getClassifier().hashCode());
        result = prime * result + this.getComponentType();

        return result;
    }

    @Override
    public String toString() {
        return "GAV [groupId=" + this.groupId + ", artifactId=" + this.artifactId + ", version=" + this.version + ", classifier=" + this.classifier + ", type=" + this.type + ", componentType="
                + this.componentType + "]";
    }

}
