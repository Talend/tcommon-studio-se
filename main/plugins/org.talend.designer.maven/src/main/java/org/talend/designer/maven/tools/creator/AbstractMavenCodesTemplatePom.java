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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractMavenCodesTemplatePom extends AbstractMavenGeneralTemplatePom {

    private IProcessor processor;

    public AbstractMavenCodesTemplatePom(IFile pomFile) {
        super(pomFile, IProjectSettingTemplateConstants.POM_CODES_TEMPLATE_FILE_NAME);
    }

    public IProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }

    protected String getProjectName() {
        return PomUtil.getProjectNameFromTemplateParameter(PomUtil.getTemplateParameters(processor));
    }

    @Override
    protected Model createModel() {
        final Model templateModel = getTemplateModel();

        this.setGroupId(templateModel.getGroupId());
        this.setArtifactId(templateModel.getArtifactId());
        String version;
        if (getDeployVersion() != null) {
            version = getDeployVersion();
        } else {
            version = templateModel.getVersion();
        }
        this.setVersion(version);
        this.setName(templateModel.getName());

        setAttributes(templateModel);
        addProperties(templateModel);

        PomUtil.checkParent(templateModel, this.getPomFile(), this.processor, getDeployVersion());

        addDependencies(templateModel);

        return templateModel;
    }

    protected abstract Model getTemplateModel();

    protected void addDependencies(Model model) {
        Set<ModuleNeeded> runningModules = getDependenciesModules();
        if (runningModules != null) {
            List<Dependency> existedDependencies = model.getDependencies();
            if (existedDependencies == null) {
                existedDependencies = new ArrayList<Dependency>();
                model.setDependencies(existedDependencies);
            }

            for (ModuleNeeded module : runningModules) {
                Dependency dependency = null;
                // TDI-37032 add dependency only if jar avialable in maven
                if (module.getDeployStatus() == ELibraryInstallStatus.DEPLOYED) {
                    dependency = PomUtil.createModuleDependency(module.getMavenUri());
                }
                if (dependency != null) {
                    existedDependencies.add(dependency);
                }
            }
        }
    }

    protected abstract Set<ModuleNeeded> getDependenciesModules();
}
