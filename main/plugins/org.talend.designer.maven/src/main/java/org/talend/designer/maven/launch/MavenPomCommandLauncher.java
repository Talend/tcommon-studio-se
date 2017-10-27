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
package org.talend.designer.maven.launch;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.internal.MavenPluginActivator;
import org.eclipse.m2e.core.project.MavenUpdateRequest;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;

/**
 * created by ggu on 13 Mar 2015 Detailled comment
 *
 */

public class MavenPomCommandLauncher extends MavenCommandLauncher {

    private final IFile launcherPomFile;

    public MavenPomCommandLauncher(IFile pomFile, String goals) {
        super(goals);
        Assert.isNotNull(pomFile);
        this.launcherPomFile = pomFile;
    }

    public MavenPomCommandLauncher(IFile pomFile) {
        this(pomFile, TalendMavenConstants.GOAL_COMPILE);
    }

    @Override
    protected ILaunchConfiguration createLaunchConfiguration() {
        return createLaunchConfiguration(launcherPomFile.getParent(), getGoals());
    }

    @Override
    public void execute(IProgressMonitor monitor) throws Exception {
        if (!launcherPomFile.exists()) {
            return;
        }
        if (getGoals().equals(TalendMavenConstants.GOAL_REFRESH)) {
            return;
        }
        final MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
        List<Dependency> oldProjectDependencies = new ArrayList<Dependency>();
        boolean withPackage = getGoals().contains(TalendMavenConstants.GOAL_PACKAGE);
        try {
            if (withPackage) {
                // clean all dependencies from pom.xml
                try {
                    Model model = mavenModelManager.readMavenModel(launcherPomFile);
                    List<Dependency> dependencies = model.getDependencies();
                    oldProjectDependencies.addAll(dependencies);
                    dependencies.clear();

                    PomUtil.savePom(monitor, model, launcherPomFile);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
            // non-pom file
            if (!TalendMavenConstants.POM_FILE_NAME.equals(launcherPomFile.getName())) {
                return;
            }
            super.execute(monitor);

        } finally {
            // because remove the dependencies, so need add back to make sure .Java project without compile errors.
            if (withPackage) {
                try {
                    Model model = mavenModelManager.readMavenModel(launcherPomFile);
                    List<Dependency> dependencies = model.getDependencies();
                    dependencies.clear();
                    dependencies.addAll(oldProjectDependencies);

                    PomUtil.savePom(monitor, model, launcherPomFile);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

}
