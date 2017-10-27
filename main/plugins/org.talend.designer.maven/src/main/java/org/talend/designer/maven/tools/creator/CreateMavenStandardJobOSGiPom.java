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
package org.talend.designer.maven.tools.creator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.core.PluginChecker;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * @see OSGIJavaScriptForESBWithMavenManager to build job
 */
public class CreateMavenStandardJobOSGiPom extends AbstractMavenProcessorPom {

    public CreateMavenStandardJobOSGiPom(IProcessor jobProcessor, IFile pomFile) {
        super(jobProcessor, pomFile, ""); // use getBundleTemplatePath instead
    }

    protected String getBundleTemplatePath() {
        return IProjectSettingTemplateConstants.PATH_OSGI_BUNDLE + '/'
                + IProjectSettingTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME;
    }

    @Override
    protected InputStream getTemplateStream() throws IOException {
        File templateFile = PomUtil.getTemplateFile(getObjectTypeFolder(), getItemRelativePath(),
                IProjectSettingTemplateConstants.OSGI_POM_FILE_NAME);
        try {
            final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(getJobProcessor());
            return MavenTemplateManager.getTemplateStream(templateFile,
                    IProjectSettingPreferenceConstants.TEMPLATE_OSGI_BUNDLE_POM, PluginChecker.MAVEN_JOB_PLUGIN_ID,
                    getBundleTemplatePath(), templateParameters);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void addDependencies(Model model) {
        // super.addDependencies(model);
        // @see JobJavaScriptsManager.setMavenDependencyElements
        // TODO
    }

}
