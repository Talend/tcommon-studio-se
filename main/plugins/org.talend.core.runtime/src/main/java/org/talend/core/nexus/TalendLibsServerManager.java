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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.ops4j.pax.url.mvn.MavenResolver;
import org.ops4j.pax.url.mvn.ServiceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.User;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.service.IRemoteService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by wchen on 2015年6月16日 Detailled comment
 *
 */
public class TalendLibsServerManager {

    private static String NEXUS_USER = "nexus.user";

    private static String NEXUS_PASSWORD = "nexus.password";

    private static String NEXUS_URL = "nexus.url";

    private static String NEXUS_LIB_REPO = "nexus.lib.repo";

    private static String DEFAULT_LIB_REPO = "talend-custom-libs-release";

    private static String NEXUS_LIB_SNAPSHOT_REPO = "nexus.lib.repo.snapshot";

    private static String DEFAULT_LIB_SNAPSHOT_REPO = "talend-custom-libs-snapshot";

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

    // public static final String TALEND_LIB_SERVER = "http://localhost:8081/nexus/";//$NON-NLS-1$
    //
    // public static final String TALEND_LIB_USER = "";//$NON-NLS-1$
    //
    // public static final String TALEND_LIB_PASSWORD = "";//$NON-NLS-1$
    //
    // public static final String TALEND_LIB_REPOSITORY = "org.talend.libraries";//$NON-NLS-1$

    private static TalendLibsServerManager manager = null;

    private MavenResolver mavenResolver = null;

    private NexusServerBean previousCustomBean;

    public static final int CONNECTION_OK = 200;

    private static Boolean lastConnectionValid;

    private TalendLibsServerManager() {
        // the tracker is use in case the service is modifed
        final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver> serviceTracker = new ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>(
                context, org.ops4j.pax.url.mvn.MavenResolver.class,
                new ServiceTrackerCustomizer<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>() {

                    @Override
                    public org.ops4j.pax.url.mvn.MavenResolver addingService(
                            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference) {
                        return context.getService(reference);
                    }

                    @Override
                    public void modifiedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;

                    }

                    @Override
                    public void removedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;
                    }
                });
        serviceTracker.open();

    }

    public static synchronized TalendLibsServerManager getInstance() {
        if (manager == null) {
            manager = new TalendLibsServerManager();
        }
        return manager;
    }

    public void updateMavenResolver(Dictionary<String, String> props, boolean setupRemoteRepository) {
        if (props == null) {
            props = new Hashtable<String, String>();
        }
        final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        ServiceReference<ManagedService> managedServiceRef = context.getServiceReference(ManagedService.class);
        if (managedServiceRef != null) {
            if (setupRemoteRepository) {
                String repositories = "";
                NexusServerBean customServer = getCustomNexusServer();
                if (customServer != null) {
                    String custom_server = customServer.getServer();
                    String custom_user = customServer.getUserName();
                    String custom_pass = customServer.getPassword();
                    String release_rep = customServer.getRepositoryId();
                    String snapshot_rep = customServer.getSnapshotRepId();
                    if (custom_server.endsWith(NexusConstants.SLASH)) {
                        custom_server = custom_server.substring(0, custom_server.length() - 1);
                    }
                    if (custom_user != null && !"".equals(custom_user)) {//$NON-NLS-1$
                        String[] split = custom_server.split("://");//$NON-NLS-1$
                        if (split.length != 2) {
                            throw new RuntimeException("Nexus url is not valid ,please contract the administrator");
                        }
                        custom_server = split[0] + "://" + custom_user + ":" + custom_pass + "@"//$NON-NLS-1$
                                + split[1] + NexusConstants.CONTENT_REPOSITORIES;
                    }
                    String releaseUrl = custom_server + release_rep + "@id=" + release_rep;//$NON-NLS-1$
                    String snapshotUrl = custom_server + snapshot_rep + "@id=" + snapshot_rep + NexusConstants.SNAPSHOTS;//$NON-NLS-1$
                    // custom nexus server should use snapshot repository
                    repositories = releaseUrl + "," + snapshotUrl;
                }
                final NexusServerBean officailServer = getLibrariesNexusServer();
                String official_server = officailServer.getServer();
                // remove the trailing slash
                if (official_server.endsWith(NexusConstants.SLASH)) {
                    official_server = official_server.substring(0, official_server.length() - 1);
                }
                String officalUrl = official_server + NexusConstants.CONTENT_REPOSITORIES + officailServer.getRepositoryId()
                        + "@id=" + officailServer.getRepositoryId();//$NON-NLS-1$
                if (repositories.isEmpty()) {
                    repositories = officalUrl;
                } else {
                    repositories = repositories + "," + officalUrl;
                }
                props.put(ServiceConstants.PID + '.' + ServiceConstants.PROPERTY_REPOSITORIES, repositories);
            }
            ManagedService managedService = context.getService(managedServiceRef);

            try {
                managedService.updated(props);
            } catch (ConfigurationException e) {
                throw new RuntimeException("Failed to modifiy the service properties"); //$NON-NLS-1$
            }
        } else {
            throw new RuntimeException("Failed to load the service :" + ManagedService.class.getCanonicalName()); //$NON-NLS-1$
        }

    }

    public MavenResolver getMavenResolver() throws RuntimeException {
        if (mavenResolver == null) {
            final BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> mavenResolverService = context
                    .getServiceReference(org.ops4j.pax.url.mvn.MavenResolver.class);
            if (mavenResolverService != null) {
                mavenResolver = context.getService(mavenResolverService);
            } else {
                throw new RuntimeException("Unable to acquire org.ops4j.pax.url.mvn.MavenResolver");
            }
        }

        return mavenResolver;

    }
    
    public void checkAndUpdateNexusServer() {
        lastConnectionValid = null;
        getCustomNexusServer();
    }

    public NexusServerBean getCustomNexusServer() {
        if (!org.talend.core.PluginChecker.isCoreTISPluginLoaded()) {
            return null;
        }
        NexusServerBean serverBean = null;
        try {
            String nexus_url = System.getProperty(NEXUS_URL);
            String nexus_user = System.getProperty(NEXUS_USER);
            String nexus_pass = System.getProperty(NEXUS_PASSWORD);
            String repositoryId = System.getProperty(NEXUS_LIB_REPO, DEFAULT_LIB_REPO);
            String snapshotRepId = System.getProperty(NEXUS_LIB_SNAPSHOT_REPO, DEFAULT_LIB_SNAPSHOT_REPO);

            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            RepositoryContext repositoryContext = factory.getRepositoryContext();
            if ((nexus_url == null && (factory.isLocalConnectionProvider() || repositoryContext.isOffline()))
                    || Boolean.FALSE == lastConnectionValid) {
                return null;
            }
            if (lastConnectionValid != Boolean.TRUE) {
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
                        IRemoteService remoteService = (IRemoteService) GlobalServiceRegister.getDefault()
                                .getService(IRemoteService.class);
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
            }
            if (lastConnectionValid == null) {
                boolean connectionOk = NexusServerUtils.checkConnectionStatus(nexus_url, repositoryId, nexus_user, nexus_pass)
                        && NexusServerUtils.checkConnectionStatus(nexus_url, snapshotRepId, nexus_user, nexus_pass);
                lastConnectionValid = connectionOk;
                if (!connectionOk) {
                    return null;
                }
            }

            serverBean = new NexusServerBean();
            serverBean.setServer(nexus_url);
            serverBean.setUserName(nexus_user);
            serverBean.setPassword(nexus_pass);
            serverBean.setRepositoryId(repositoryId);
            serverBean.setSnapshotRepId(snapshotRepId);
        } catch (Exception e) {
            serverBean = null;
            ExceptionHandler.process(e);
        }
        if (previousCustomBean == null && serverBean != null
                || previousCustomBean != null && !previousCustomBean.equals(serverBean)) {
            mavenResolver = null;
        }
        previousCustomBean = serverBean;
        return serverBean;

    }

    public NexusServerBean getLibrariesNexusServer() {
        NexusServerBean serverBean = new NexusServerBean();
        serverBean.setServer(System.getProperty("org.talend.libraries.repo.url", TALEND_LIB_SERVER));
        serverBean.setUserName(TALEND_LIB_USER);
        serverBean.setPassword(TALEND_LIB_PASSWORD);
        serverBean.setRepositoryId(TALEND_LIB_REPOSITORY);
        serverBean.setOfficial(true);

        return serverBean;
    }

    public List<MavenArtifact> search(String nexusUrl, String userName, String password, String repositoryId,
            String groupIdToSearch, String artifactId, String versionToSearch) throws Exception {
        return NexusServerUtils.search(nexusUrl, userName, password, repositoryId, groupIdToSearch, artifactId, versionToSearch);
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
    public static NexusServerBean getSoftwareUpdateNexusServer(String adminUrl, String userName, String password) {
        try {
            if (adminUrl != null && !"".equals(adminUrl)
                    && GlobalServiceRegister.getDefault().isServiceRegistered(IRemoteService.class)) {
                IRemoteService remoteService = (IRemoteService) GlobalServiceRegister.getDefault()
                        .getService(IRemoteService.class);
                NexusServerBean serverBean = remoteService.getUpdateRepositoryUrl(userName, password, adminUrl);
                String nexus_url = serverBean.getServer();
                String nexus_user = serverBean.getUserName();
                String nexus_pass = serverBean.getPassword();
                String nexus_repository = serverBean.getRepositoryId();
                boolean connectionOK = NexusServerUtils.checkConnectionStatus(nexus_url, nexus_repository, nexus_user,
                        nexus_pass);
                if (!connectionOK) {
                    return null;
                }
                return serverBean;
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (LoginException e) {
            ExceptionHandler.process(e);
        }

        return null;
    }

}
