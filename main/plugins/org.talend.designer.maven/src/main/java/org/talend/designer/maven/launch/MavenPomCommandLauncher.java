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
package org.talend.designer.maven.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.talend.designer.maven.model.TalendMavenConstants;

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
        // non-pom file
        // if (!TalendMavenConstants.POM_FILE_NAME.equals(launcherPomFile.getName())) {
        // return;
        // }
        super.execute(monitor);
    }

}
