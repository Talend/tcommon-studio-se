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
package org.talend.designer.maven.tools.creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractMavenCodesTemplatePom extends AbstractMavenGeneralTemplatePom {

    private Property property;

    private String projectName;

    public AbstractMavenCodesTemplatePom(IFile pomFile) {
        super(pomFile, IProjectSettingTemplateConstants.POM_CODES_TEMPLATE_FILE_NAME);
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    protected String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    protected Model createModel() {
        final Model templateModel = getTemplateModel();

        this.setGroupId(templateModel.getGroupId());
        this.setArtifactId(templateModel.getArtifactId());
        this.setVersion(templateModel.getVersion());
        this.setName(templateModel.getName());

        setAttributes(templateModel);
        addProperties(templateModel);

        Map<String, Object> templateParameters = new HashMap<>();
        templateParameters.put(MavenTemplateManager.KEY_PROJECT_NAME, projectName);
        PomUtil.checkParent(templateModel, this.getPomFile(), templateParameters);

        addDependencies(templateModel);

        return templateModel;
    }

    protected abstract Model getTemplateModel();

    protected void addDependencies(Model model) {
        Set<ModuleNeeded> runningModules = getDependenciesModules();
        Set<ModuleNeeded> needModules = new HashSet<ModuleNeeded>();
        Set<String> uniquDependenciesSet = new HashSet<String>();
        for (ModuleNeeded module : runningModules) {
            final String mavenUri = module.getMavenUri();
            if (uniquDependenciesSet.contains(mavenUri)) {
                continue;
            }
            uniquDependenciesSet.add(mavenUri);
            needModules.add(module);
        }
        if (needModules != null) {
            List<Dependency> existedDependencies = model.getDependencies();
            if (existedDependencies == null) {
                existedDependencies = new ArrayList<Dependency>();
                model.setDependencies(existedDependencies);
            }

            for (ModuleNeeded module : needModules) {
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
