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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.utils.io.FilesUtils;

/**
 * @author bhe created on Jul 1, 2021
 *
 */
abstract public class BaseComponentInstallerTask implements IComponentInstallerTask {

    private int order;

    private String g;

    private String a;

    private String v;

    private String c;

    private String t;

    private String componentType;

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getComponentGroupId() {
        return this.g;
    }

    @Override
    public void setComponenGroupId(String groupId) {
        this.g = groupId;
    }

    @Override
    public String getComponenArtifactId() {
        return this.a;
    }

    @Override
    public void setComponenArtifactId(String artifactId) {
        this.a = artifactId;
    }

    @Override
    public String getComponenVersion() {
        return this.v;
    }

    @Override
    public void setComponenVersion(String version) {
        this.v = version;
    }

    @Override
    public String getComponentClassifier() {
        return this.c;
    }

    @Override
    public void setComponentClassifier(String classifier) {
        this.c = classifier;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.valueOf(componentType);
    }

    @Override
    public void setComponentType(String type) {
        this.componentType = type;
    }

    @Override
    public String getComponentPackageType() {
        return t;
    }

    @Override
    public void setComponentPackageType(String type) {
        this.t = type;
    }

    public File moveFile(File compFile) {
        if (compFile == null || !isMovingFile(compFile)) {
            return compFile;
        }

        String targetFileName = getExpectedArtifactName();
        File targetFile = Paths.get(compFile.getParent(), targetFileName).toFile();
        try {
            FilesUtils.copyFile(compFile, targetFile);
            compFile.delete();
            return targetFile;
        } catch (IOException e) {
            ExceptionHandler.process(e);
            throw new RuntimeException(e);
        }
    }

    protected boolean isMovingFile(File compFile) {
        if (compFile == null) {
            return false;
        }
        return !StringUtils.equalsIgnoreCase(compFile.getName(), getExpectedArtifactName());
    }

    protected String getExpectedArtifactName() {
        StringBuffer sb = new StringBuffer();
        sb.append(a);
        sb.append("-");
        sb.append(v);

        if (!StringUtils.isEmpty(c)) {
            sb.append("-");
            sb.append(c);
        }

        if (StringUtils.isEmpty(t)) {
            sb.append(".jar");
        } else {
            sb.append(".");
            sb.append(t);
        }
        
        return sb.toString();
    }
}
