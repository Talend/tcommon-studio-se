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
package org.talend.core.nexus;

import java.util.Date;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.User;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IMavenUIService;
import org.talend.core.service.IRemoteService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by wchen on 2015年6月16日 Detailled comment
 *
 */
public class TalendLibsServerManager {

    public static final String KEY_LIB_REPO_URL = "org.talend.libraries.repo.url";

    private static String NEXUS_USER = "nexus.user";

    private static String NEXUS_PASSWORD = "nexus.password";

    private static String NEXUS_URL = "nexus.url";

    private static String NEXUS_LIB_REPO = "nexus.lib.repo";

    private static String DEFAULT_LIB_REPO = "talend-custom-libs-release";

    private static String NEXUS_LIB_SNAPSHOT_REPO = "nexus.lib.repo.snapshot";

    private static String DEFAULT_LIB_SNAPSHOT_REPO = "talend-custom-libs-snapshot";

    private static String NEXUS_LIB_SERVER_TYPE = "nexus.lib.server.type";

    public static final String KEY_NEXUS_RUL = "url";//$NON-NLS-1$

    public static final String KEY_NEXUS_USER = "username";//$NON-NLS-1$

    public static final String KEY_NEXUS_PASS = "password";//$NON-NLS-1$

    public static final String KEY_CUSTOM_LIB_REPOSITORY = "repositoryReleases";//$NON-NLS-1$

    public static final String KEY_CUSTOM_LIB_SNAPSHOT_REPOSITORY = "repositorySnapshots";//$NON-NLS-1$

    public static final String KEY_SOFTWARE_UPDATE_REPOSITORY = "repositoryID";//$NON-NLS-1$

    public static final String TALEND_LIB_SERVER = "https://talend-update.talend.com/nexus/";//$NON-NLS-1$

    public static final String TALEND_LIB_USER = "";//$NON-NLS-1$

    public static final String TALEND_LIB_PASSWORD = "";//$NON-NLS-1$

    public static final String TALEND_LIB_REPOSITORY = "libraries";//$NON-NLS-1$

    private static TalendLibsServerManager manager = null;

    private NexusServerBean artifactServerBean;

    private long artifactLastTimeInMillis = 0;

    private NexusServerBean softWareUpdateServerBean;

    private long softWareLastTimeInMillis = 0;

    private int timeGap = 5 * 60 * 1000;

    public static synchronized TalendLibsServerManager getInstance() {
        if (manager == null) {
            manager = new TalendLibsServerManager();
        }
        return manager;
    }

    public NexusServerBean getCustomNexusServer() {
        if (!org.talend.core.PluginChecker.isCoreTISPluginLoaded()) {
            return null;
        }
        Date date = new Date();
        // avoid to connect to tac too many times
        if (artifactServerBean == null && date.getTime() - artifactLastTimeInMillis > timeGap) {
            try {
                artifactLastTimeInMillis = date.getTime();
                String nexus_url = System.getProperty(NEXUS_URL);
                String nexus_user = System.getProperty(NEXUS_USER);
                String nexus_pass = System.getProperty(NEXUS_PASSWORD);
                String repositoryId = System.getProperty(NEXUS_LIB_REPO, DEFAULT_LIB_REPO);
                String snapshotRepId = System.getProperty(NEXUS_LIB_SNAPSHOT_REPO, DEFAULT_LIB_SNAPSHOT_REPO);
                String serverType = System.getProperty(NEXUS_LIB_SERVER_TYPE, "NEXUS_2");

                IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                RepositoryContext repositoryContext = factory.getRepositoryContext();
                if ((nexus_url == null && (factory.isLocalConnectionProvider() || repositoryContext.isOffline()))) {
                    return null;
                }
                if (repositoryContext != null && repositoryContext.getFields() != null && !factory.isLocalConnectionProvider()
                        && !repositoryContext.isOffline()) {
                    String adminUrl = repositoryContext.getFields().get(RepositoryConstants.REPOSITORY_URL);
                    String userName = "";
                    String password = "";
                    User user = repositoryContext.getUser();
                    if (user != null) {
                        userName = user.getLogin();
                        password = repositoryContext.getClearPassword();
                    }

                    if (adminUrl != null && !"".equals(adminUrl)
                            && GlobalServiceRegister.getDefault().isServiceRegistered(IRemoteService.class)) {
                        IRemoteService remoteService = (IRemoteService) GlobalServiceRegister.getDefault().getService(
                                IRemoteService.class);
                        NexusServerBean bean = remoteService.getLibNexusServer(userName, password, adminUrl);
                        if (bean != null) {
                            nexus_url = bean.getServer();
                            nexus_user = bean.getUserName();
                            nexus_pass = bean.getPassword();
                            repositoryId = bean.getRepositoryId();
                            snapshotRepId = bean.getSnapshotRepId();
                            System.setProperty(NEXUS_URL, nexus_url);
                            System.setProperty(NEXUS_USER, nexus_user);
                            System.setProperty(NEXUS_PASSWORD, nexus_pass);
                            System.setProperty(NEXUS_LIB_REPO, repositoryId);
                            System.setProperty(NEXUS_LIB_SNAPSHOT_REPO, snapshotRepId);
                        }
                    }
                }
                NexusServerBean serverBean = new NexusServerBean();
                serverBean.setServer(nexus_url);
                serverBean.setUserName(nexus_user);
                serverBean.setPassword(nexus_pass);
                serverBean.setRepositoryId(repositoryId);
                serverBean.setSnapshotRepId(snapshotRepId);
                serverBean.setType(serverType);

                IRepositoryArtifactHandler repHander = RepositoryArtifactHandlerManager.getRepositoryHandler(serverBean);
                if (repHander.checkConnection()) {
                    artifactServerBean = serverBean;
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IMavenUIService.class)) {
                        IMavenUIService mavenUIService = (IMavenUIService) GlobalServiceRegister.getDefault().getService(
                                IMavenUIService.class);
                        if (mavenUIService != null) {
                            mavenUIService.updateMavenResolver(true);
                        }
                    }
                }

            } catch (Exception e) {
                artifactServerBean = null;
                ExceptionHandler.process(e);
            }
        }
        return artifactServerBean;

    }

    public NexusServerBean getTalentArtifactServer() {
        NexusServerBean serverBean = new NexusServerBean();
        serverBean.setServer(System.getProperty(KEY_LIB_REPO_URL, TALEND_LIB_SERVER));
        serverBean.setUserName(TALEND_LIB_USER);
        serverBean.setPassword(TALEND_LIB_PASSWORD);
        serverBean.setRepositoryId(TALEND_LIB_REPOSITORY);
        serverBean.setOfficial(true);

        return serverBean;
    }

    public String resolveSha1(String nexusUrl, String userName, String password, String repositoryId, String groupId,
            String artifactId, String version, String type) throws Exception {
        return NexusServerUtils.resolveSha1(nexusUrl, userName, password, repositoryId, groupId, artifactId, version, type);
    }

    /**
     * 
     * DOC Talend Comment method "getSoftwareUpdateNexusServer". get nexus server configured in TAC for patch
     * 
     * @param adminUrl
     * @param userName
     * @param password
     * @return
     */
    public NexusServerBean getSoftwareUpdateNexusServer(String adminUrl, String userName, String password) {
        try {
            Date date = new Date();
            if (softWareUpdateServerBean == null && date.getTime() - softWareLastTimeInMillis > timeGap) {
                softWareLastTimeInMillis = date.getTime();
                if (adminUrl != null && !"".equals(adminUrl)
                        && GlobalServiceRegister.getDefault().isServiceRegistered(IRemoteService.class)) {
                    IRemoteService remoteService = (IRemoteService) GlobalServiceRegister.getDefault().getService(
                            IRemoteService.class);
                    NexusServerBean serverBean = remoteService.getUpdateRepositoryUrl(userName, password, adminUrl);
                    IRepositoryArtifactHandler repHander = RepositoryArtifactHandlerManager.getRepositoryHandler(serverBean);
                    if (repHander.checkConnection(true, false)) {
                        softWareUpdateServerBean = serverBean;
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (LoginException e) {
            ExceptionHandler.process(e);
        }

        return softWareUpdateServerBean;
    }

}
