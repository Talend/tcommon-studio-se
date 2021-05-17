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
package org.talend.designer.maven.tools.creator;

import java.util.HashSet;
import java.util.Set;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class CreateMavenRoutinePom extends AbstractMavenCodesTemplatePom {

    public CreateMavenRoutinePom(IFile pomFile) {
        super(pomFile);
    }

    @Override
    protected Model getTemplateModel() {
        return MavenTemplateManager.getRoutinesTempalteModel(getProjectName());
    }

    @Override
    protected Set<ModuleNeeded> getDependenciesModules() {
        // if (GlobalServiceRegister.getDefault().isServiceRegistered(IRoutinesService.class)) {
        // IRoutinesService routiensService = (IRoutinesService) GlobalServiceRegister.getDefault().getService(
        // IRoutinesService.class);
        // // TODO, maybe should only add the routines, not for pigudfs and beans.
        // Set<ModuleNeeded> runningModules = routiensService.getRunningModules();
        // return runningModules;
        // }
        Set<ModuleNeeded> runningModules = new HashSet<>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                    ILibrariesService.class);
            runningModules.addAll(libService.getCodesModuleNeededs(ERepositoryObjectType.ROUTINES));
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IRepositoryService repositoryService = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
            if (PluginChecker.isBigdataRoutineLoaded() && repositoryService.isProjectLevelLog4j2()) {
                runningModules.addAll(repositoryService.getLog4j2Modules());
            }
        }
        return runningModules;
    }
}
