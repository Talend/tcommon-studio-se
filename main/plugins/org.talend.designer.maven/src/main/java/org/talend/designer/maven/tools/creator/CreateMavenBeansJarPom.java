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
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.designer.maven.template.MavenTemplateManager;

public class CreateMavenBeansJarPom extends AbstractMavenCodesTemplatePom {

    private CodesJarInfo info;

    public CreateMavenBeansJarPom(CodesJarInfo info, IFile pomFile) {
        super(pomFile);
        this.info = info;
    }

    @Override
    protected Model getTemplateModel() {
        return MavenTemplateManager.getBeansJarTempalteModel(info);
    }

    @Override
    protected Set<ModuleNeeded> getDependenciesModules() {
        Set<ModuleNeeded> runningModules = new HashSet<>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                    ILibrariesService.class);
            runningModules.addAll(libService.getCodesJarModuleNeededs(info));
        }
        return runningModules;
    }

    @Override
    protected boolean ignoreModuleInstallationStatus() {
        return true;
    }

}
