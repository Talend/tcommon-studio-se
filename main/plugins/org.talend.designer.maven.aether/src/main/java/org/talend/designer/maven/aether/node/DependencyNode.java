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
package org.talend.designer.maven.aether.node;

import java.util.List;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DependencyNode {

    private String groupId;

    private String artifactId;

    private String version;

    private String classifier;

    private String extension;

    private String scope;

    private List<ExclusionNode> exclusions;

    private List<DependencyNode> dependencies;

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassifier() {
        return this.classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<DependencyNode> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(List<DependencyNode> dependencies) {
        this.dependencies = dependencies;
    }

    public List<ExclusionNode> getExclusions() {
        return this.exclusions;
    }

    public void setExclusions(List<ExclusionNode> exclusions) {
        this.exclusions = exclusions;
    }

    public String getJarName() {
        String jarname = getArtifactId() + "-" + getVersion();
        String classifier = getClassifier();
        if (classifier != null && !classifier.isEmpty()) {
            jarname = jarname + "-" + classifier;
        }
        String extension = getExtension();
        if (extension == null || extension.isEmpty()) {
            extension = "jar";
        }
        jarname = jarname + "." + extension;
        return jarname;
    }

}
