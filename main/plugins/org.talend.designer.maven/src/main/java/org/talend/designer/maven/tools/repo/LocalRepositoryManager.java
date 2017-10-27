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
package org.talend.designer.maven.tools.repo;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class LocalRepositoryManager {

    public static final boolean ENABLE_LOCAL_REPO = true;

    /**
     * must be same as the pom_project_template.xml
     */
    public static final String REPO_FOLDER = "repo";

    public static final String REPO_ID = "LocalRepo";

    public static final LocalRepositoryManager LAUNCHER;

    // public static final LocalRepositoryManager AETHER;
    static {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = processService.getTalendProcessJavaProject();
            LAUNCHER = talendProcessJavaProject != null ? new LocalRepsitoryLauncherManager(talendProcessJavaProject.getProject())
                    : null;
            // AETHER = new LocalRepositoryAetherManager(project.getLocation().toFile());
        } else {
            LAUNCHER = null;
            // AETHER = null;
        }
    }

    private final File repoFolder;

    public LocalRepositoryManager() {
        repoFolder = null;
    }

    public LocalRepositoryManager(File baseDir) {
        super();
        Assert.isNotNull(baseDir);
        this.repoFolder = new File(baseDir, REPO_FOLDER);

    }

    public LocalRepositoryManager(IContainer container) {
        super();
        Assert.isNotNull(container);
        this.repoFolder = container.getFolder(new Path(REPO_FOLDER)).getLocation().toFile();

    }

    protected File getRepoFolder() {
        return repoFolder;
    }

    public void cleanup(IProgressMonitor monitor) {
        // nothing to do by default
    }

    public abstract void install(File file, MavenArtifact artifact) throws Exception;

    public abstract void deploy(File file, MavenArtifact artifact) throws Exception;

}
