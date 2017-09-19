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
package org.talend.designer.maven.ui;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMavenConfigurationChangeListener;
import org.eclipse.m2e.core.embedder.MavenConfigurationChangeEvent;
import org.eclipse.m2e.core.internal.preferences.MavenPreferenceConstants;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.services.IMavenUIService;
import org.talend.designer.maven.ui.setting.preference.M2eUserSettingForTalendLoginTask;
import org.talend.designer.maven.ui.setting.repository.RepositoryMavenSettingManager;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenUIService implements IMavenUIService {

    @Override
    public void addCustomMavenSettingChildren(IPreferenceNode parentNode) {
        if (parentNode == null) {
            return;
        }
        RepositoryMavenSettingManager manager = new RepositoryMavenSettingManager();
        manager.init(IRepositoryView.VIEW_ID);
        IPreferenceNode[] rootSubNodes = manager.getRootSubNodes();
        for (IPreferenceNode node : rootSubNodes) {
            parentNode.add(node);
        }
    }

    @Override
    public void checkUserSettings(IProgressMonitor monitor) {
        try {
            M2eUserSettingForTalendLoginTask loginTask = new M2eUserSettingForTalendLoginTask();
            loginTask.run(monitor);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.runtime.services.IMavenUIService#getUserSettings()
     */
    @Override
    public void updateMavenResolver(boolean setupCustomLibNexus) {
        Dictionary<String, String> props = getTalendMavenSetting();
        boolean updated = false;
        if (setupCustomLibNexus) {
            NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler repositoryHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (repositoryHandler != null) {
                updated = true;
                repositoryHandler.updateMavenResolver(props);
            }
        }
        if (!updated) {
            try {
                TalendMavenResolver.updateMavenResolver(props);
            } catch (Exception e) {
                throw new RuntimeException("Failed to modifiy the service properties"); //$NON-NLS-1$
            }
        }

    }

    @Override
    public void addMavenConfigurationChangeListener() {
        MavenPlugin.getMavenConfiguration().addConfigurationChangeListener(new IMavenConfigurationChangeListener() {

            @Override
            public void mavenConfigurationChange(MavenConfigurationChangeEvent event) throws CoreException {
                if (event.getKey() != null && event.getKey().equals(MavenPreferenceConstants.P_GLOBAL_SETTINGS_FILE)
                        || event.getKey().equals(MavenPreferenceConstants.P_USER_SETTINGS_FILE)) {
                    updateMavenResolver(true);

                }
            }
        });
    }

    @Override
    public Dictionary<String, String> getTalendMavenSetting() {
        String studioUserSettingsFile = MavenPlugin.getMavenConfiguration().getUserSettingsFile();
        // apply the user settings to MavenResolver
        Dictionary<String, String> props = new Hashtable<String, String>();
        if (studioUserSettingsFile != null && !"".equals(studioUserSettingsFile)) {
            // change back to use the user settings after Pax-url-eather fix the space bug
            props.put("org.ops4j.pax.url.mvn.settings", studioUserSettingsFile);
        }
        return props;
    }
}
