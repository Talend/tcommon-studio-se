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
package org.talend.designer.maven.tools;

import java.util.Map;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.core.model.general.Project;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProjectPomManager {

    protected static final MavenModelManager MODEL_MANAGER = MavenPlugin.getMavenModelManager();

    private IFile projectPomFile;

    /**
     * true by default, update all
     */

    public ProjectPomManager() {
        projectPomFile = getTalendProjectPom();
    }

    public void update(IProgressMonitor monitor, IProcessor processor) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (!projectPomFile.exists()) {// delete by user manually?
            // create it or nothing to do?
            return;
        }
        Model projectModel = MODEL_MANAGER.readMavenModel(projectPomFile);

        // attributes
        updateAttributes(monitor, processor, projectModel);

        PomUtil.savePom(monitor, projectModel, projectPomFile);
    }

    public void updateFromTemplate(IProgressMonitor monitor) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        Model projectModel = MODEL_MANAGER.readMavenModel(projectPomFile);
        Model templateModel = MavenTemplateManager.getCodeProjectTemplateModel();
        for (String module : projectModel.getModules()) {
            templateModel.addModule(module);
        }
 
        PomUtil.savePom(monitor, templateModel, projectPomFile);
    }

    /**
     * 
     * update the main attributes for project pom.
     * 
     */
    protected void updateAttributes(IProgressMonitor monitor, IProcessor processor, Model projectModel) throws Exception {
        final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(processor);
        Model templateModel = MavenTemplateManager.getCodeProjectTemplateModel(templateParameters);
        projectModel.setGroupId(templateModel.getGroupId());
        projectModel.setArtifactId(templateModel.getArtifactId());
        projectModel.setVersion(templateModel.getVersion());
        projectModel.setName(templateModel.getName());
        projectModel.setPackaging(templateModel.getPackaging());
    }

    public IFile getTalendProjectPom() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFolder pomsFolder = workspace.getRoot()
                .getFolder(new Path(project.getTechnicalLabel() + "/" + TalendJavaProjectConstants.DIR_POMS)); //$NON-NLS-1$
        IFile pomFile = pomsFolder.getFile(TalendMavenConstants.POM_FILE_NAME);
        return pomFile;
    }

}
