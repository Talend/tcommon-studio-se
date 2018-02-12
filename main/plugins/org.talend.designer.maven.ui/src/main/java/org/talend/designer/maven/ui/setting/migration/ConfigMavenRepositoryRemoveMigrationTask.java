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
package org.talend.designer.maven.ui.setting.migration;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.maven.settings.Settings;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractProjectMigrationTask;
import org.talend.designer.maven.repository.DefaultMavenRepositoryProvider;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ConfigMavenRepositoryRemoveMigrationTask extends AbstractProjectMigrationTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 10, 30, 12, 0, 0);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(Project project) {
        try {
            // remove old one.
            File defaultRepoFile = DefaultMavenRepositoryProvider.getMavenRepoPath().toFile();
            FilesUtils.deleteFolder(defaultRepoFile, true);

            // force to sync always.
            final IMaven maven = MavenPlugin.getMaven();
            maven.reloadSettings();
            final Settings settings = maven.getSettings();
            String localRepository = settings.getLocalRepository();
            if (localRepository != null && localRepository.length() > 0) {
                File localRepoFile = new File(localRepository);
                DefaultMavenRepositoryProvider.sync(localRepoFile.getParentFile());
            }
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (CoreException e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

}
