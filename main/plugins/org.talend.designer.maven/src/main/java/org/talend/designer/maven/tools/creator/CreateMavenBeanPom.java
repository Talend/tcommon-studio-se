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

import java.util.Collections;
import java.util.Set;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.maven.template.MavenTemplateManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class CreateMavenBeanPom extends AbstractMavenCodesTemplatePom {

    public CreateMavenBeanPom(IFile pomFile) {
        super(pomFile);
    }

    @Override
    protected Model getTemplateModel() {
        return MavenTemplateManager.getBeansTempalteModel(getProjectName());
    }

    @Override
    protected Set<ModuleNeeded> getDependenciesModules() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                    ILibrariesService.class);
            Set<ModuleNeeded> runningModules = libService.getCodesModuleNeededs(ERepositoryObjectType.getType("BEANS")); //$NON-NLS-1$
            return runningModules;
        }
        return Collections.emptySet();
    }
}
