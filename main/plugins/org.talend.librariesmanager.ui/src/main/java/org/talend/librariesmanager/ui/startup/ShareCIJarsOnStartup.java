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
package org.talend.librariesmanager.ui.startup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.MojoType;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.librariesmanager.ui.i18n.Messages;


/*
* Created by bhe on Dec 24, 2020
*/
public class ShareCIJarsOnStartup extends ShareMavenArtifactsOnStartup {

    @Override
    public Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor) {
        SubMonitor mainSubMonitor = SubMonitor.convert(monitor, 1);
        mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.getFilesToShare")); //$NON-NLS-1$
        Map<ModuleNeeded, File> files = new HashMap<>();
        // if tos
        if (!PluginChecker.isTIS()) {
            return files;
        }
        // get plugin artifacts to share
        Stream.of(MojoType.values()).forEach(m -> {
            String mvnUrl = MavenUrlHelper.generateMvnUrl(TalendMavenConstants.DEFAULT_CI_GROUP_ID, m.getArtifactId(),
                    VersionUtils.getMojoVersion(m), null, null);
            // try to resolve locally
            String localMvnUrl = mvnUrl.replace(MavenUrlHelper.MVN_PROTOCOL,
                    MavenUrlHelper.MVN_PROTOCOL + MavenConstants.LOCAL_RESOLUTION_URL + MavenUrlHelper.REPO_SEPERATOR);
            File file = null;
            try {
                file = TalendMavenResolver.resolve(localMvnUrl);
            } catch (IOException | RuntimeException e) {
                ExceptionHandler.process(e);
            }
            if (file != null) {
                ModuleNeeded module = new ModuleNeeded("", mvnUrl, "", true);
                files.put(module, file);
            }
        });

        mainSubMonitor.worked(1);
        return files;
    }

}
