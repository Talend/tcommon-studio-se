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
package org.talend.designer.maven.logintask;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.login.AbstractLoginTask;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class CleanMavenLastUpdateFilesLoginTask extends AbstractLoginTask {

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        final IMaven maven = MavenPlugin.getMaven();
        String localRepositoryPath = maven.getLocalRepositoryPath();
        if (localRepositoryPath == null) {
            return;
        }
        File localRepoFolder = new File(localRepositoryPath);
        PomUtil.cleanLastUpdatedFile(localRepoFolder);

    }



}
