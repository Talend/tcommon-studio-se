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
package org.talend.updates.runtime.nexus.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.ArtifactRepositoryBean.NexusType;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * created by ycbai on 2017年5月22日 Detailled comment
 *
 */
public class NexusServerManager {

    public static final String PROP_KEY_NEXUS_URL = "components.nexus.url"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_REPOSITORY = "components.nexus.repository"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_REPOSITORY_SNAPSHOT = "components.nexus.repository.snapshot"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_USER = "components.nexus.user"; //$NON-NLS-1$

    public static final String PROP_KEY_NEXUS_PASS = "components.nexus.pass"; //$NON-NLS-1$

    private static final String DEFAULT_REPOSITORY_ID = "releases"; //$NON-NLS-1$

    private static final String SLASH = "/"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(NexusServerManager.class);

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
        String snapshotRepoId = System.getProperty(PROP_KEY_NEXUS_REPOSITORY_SNAPSHOT);
        String nexusUser = System.getProperty(PROP_KEY_NEXUS_USER);
        String nexusPass = System.getProperty(PROP_KEY_NEXUS_PASS);
        if (StringUtils.isBlank(snapshotRepoId)) {
            snapshotRepoId = repoId;
            log.info("System property " + PROP_KEY_NEXUS_REPOSITORY_SNAPSHOT + " is not set, will reuse the release repository "
                    + repoId);
        }

        ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
        serverBean.setServer(nexusUrl);
        serverBean.setRepositoryId(repoId);
        serverBean.setSnapshotRepId(snapshotRepoId);
        serverBean.setUserName(nexusUser);
        serverBean.setPassword(nexusPass);

        String repType = StringUtils.stripEnd(nexusUrl, SLASH);
        repType = StringUtils.substringAfterLast(repType, SLASH).toLowerCase();
        NexusType type = null;
        if (repType.equals("nexus")) { //$NON-NLS-1$
            type = NexusType.NEXUS_2;
        } else if (repType.equals("artifactory")) { //$NON-NLS-1$
            type = NexusType.ARTIFACTORY;
        } else {
            type = NexusType.NEXUS_3;
        }
        if (type != null) {
            serverBean.setType(type.name());
        }
        return serverBean;
    }

    public ArtifactRepositoryBean getComponentShareRepositoryFromServer() {
        if (isRemoteOnlineProject()) {
            return TalendLibsServerManager.getInstance().getCustomNexusServer();
        }
        return null;
    }

    public boolean isRemoteOnlineProject() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IRepositoryService repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault()
                    .getService(IRepositoryService.class);
            IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
            try {
                boolean isLocalProject = repositoryFactory.isLocalConnectionProvider();
                boolean isOffline = false;
                if (!isLocalProject) {
                    RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                            .getProperty(Context.REPOSITORY_CONTEXT_KEY);
                    isOffline = repositoryContext.isOffline();
                }
                return !isLocalProject && !isOffline;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

}
