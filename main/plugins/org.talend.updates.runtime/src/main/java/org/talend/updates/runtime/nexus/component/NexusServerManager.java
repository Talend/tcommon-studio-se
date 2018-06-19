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
package org.talend.updates.runtime.nexus.component;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.ArtifactRepositoryBean;

/**
 * created by ycbai on 2017年5月22日 Detailled comment
 *
 */
public class NexusServerManager {

    public static final String PROP_KEY_NEXUS_URL = "components.nexus.url"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_REPOSITORY = "components.nexus.repository"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_USER = "components.nexus.user"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_PASS = "components.nexus.pass"; //$NON-NLS-1$

    private static final String DEFAULT_REPOSITORY_ID = "releases"; //$NON-NLS-1$

    private static NexusServerManager instance;

    private NexusServerManager() {
    }

    public static synchronized NexusServerManager getInstance() {
        if (instance == null) {
            instance = new NexusServerManager();
        }
        return instance;
    }

    public ArtifactRepositoryBean getLocalNexusServer() {
        INexusService nexusService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(INexusService.class)) {
            nexusService = (INexusService) GlobalServiceRegister.getDefault().getService(INexusService.class);
        }
        if (nexusService == null) {
            return null;
        }
        String repoId = System.getProperty(PROP_KEY_NEXUS_REPOSITORY, DEFAULT_REPOSITORY_ID);
        return nexusService.getPublishNexusServerBean(repoId);
    }

    public ArtifactRepositoryBean getPropertyNexusServer() {
        if (!System.getProperties().containsKey(PROP_KEY_NEXUS_URL)) {
            return null; // if not set
        }
        String nexusUrl = System.getProperty(PROP_KEY_NEXUS_URL);
        String repoId = System.getProperty(PROP_KEY_NEXUS_REPOSITORY, DEFAULT_REPOSITORY_ID);
        String nexusUser = System.getProperty(PROP_KEY_NEXUS_USER);
        String nexusPass = System.getProperty(PROP_KEY_NEXUS_PASS);

        ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
        serverBean.setServer(nexusUrl);
        serverBean.setRepositoryId(repoId);
        serverBean.setUserName(nexusUser);
        serverBean.setPassword(nexusPass);
        return serverBean;
    }

}
