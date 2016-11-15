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
package org.talend.designer.maven.template;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.setting.project.IProjectSettingManagerProvider;

public abstract class AbstractMavenTemplateManager implements IExecutableExtension {

    protected String bundleName;

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        // init bundle name
        this.bundleName = config.getContributor().getName();
    }

    /**
     * read input stream from template file.
     */
    public InputStream readFileStream(File templateFile) throws Exception {
        if (templateFile == null) {
            throw new NullPointerException();
        }
        // need close it after using.
        return new BufferedInputStream(new FileInputStream(templateFile));
    }

    public InputStream readBundleStream(String bundleTemplatePath) throws Exception {
        if (this.bundleName == null || bundleTemplatePath == null) {
            throw new NullPointerException();
        }
        Bundle bundle = Platform.getBundle(this.bundleName);
        if (bundle == null) {
            throw new IllegalArgumentException("Can't find the bundle with name " + this.bundleName);
        }
        if (bundleTemplatePath.length() == 0) {
            throw new IllegalArgumentException("The template path shoundn't be empty");
        }
        URL templateUrl = bundle.getEntry(bundleTemplatePath);
        if (templateUrl == null) {
            throw new IllegalArgumentException("Can't load the template via path " + bundleTemplatePath + " in bundle "
                    + this.bundleName);
        }
        return templateUrl.openStream();
    }

    public InputStream readProjectSettingStream(String projectSettingTemplateKey) throws Exception {
        ProjectPreferenceManager projectPreferenceManager = getProjectPreferenceManager();
        if (projectPreferenceManager == null || projectSettingTemplateKey == null) {
            throw new NullPointerException();
        }

        if (projectSettingTemplateKey.length() == 0) {
            throw new IllegalArgumentException("The project setting key shoundn't be empty");
        }
        String value = projectPreferenceManager.getValue(projectSettingTemplateKey);
        if (value == null || value.length() == 0) {
            throw new Exception("Can't find the value by key " + projectSettingTemplateKey + " in "
                    + projectPreferenceManager.getQualifier());
        }
        return new ByteArrayInputStream(value.getBytes(TalendMavenConstants.DEFAULT_ENCODING));
    }

    public ProjectPreferenceManager getProjectPreferenceManager() {
        IProjectSettingManagerProvider provider = MavenTemplateManager.getProjectSettingManagerMap().get(bundleName);
        if (provider != null) {
            return provider.getProjectSettingManager();
        }
        return null;
    }
}
