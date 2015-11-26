// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.talendlib;

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
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.User;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.service.IRemoteService;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.utils.json.JSONObject;

/**
 * created by wchen on 2015年6月16日 Detailled comment
 *
 */
public class TalendLibsServerManager {

    private static TalendLibsServerManager manager = null;

    private MavenResolver mavenResolver = null;

    private NexusServerBean previousCustomBean;

    public static final int CONNECTION_OK = 200;

    private TalendLibsServerManager() {
        // the tracker is use in case the service is modifed
        final BundleContext context = DesignerMavenPlugin.getPlugin().getContext();
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
        final BundleContext context = DesignerMavenPlugin.getPlugin().getContext();
        ServiceReference<ManagedService> managedServiceRef = context.getServiceReference(ManagedService.class);
        if (managedServiceRef != null) {
            if (setupRemoteRepository) {
                String repositories = "";
                NexusServerBean customServer = getCustomNexusServer();
                if (customServer != null) {
                    // custom nexus server should use snapshot repository
                    repositories = customServer.getRepositoryUrl() + NexusConstants.SNAPSHOTS + ",";
                }
                final NexusServerBean officailServer = getLibrariesNexusServer();
                repositories = repositories + officailServer.getRepositoryUrl();
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
            final BundleContext context = DesignerMavenPlugin.getPlugin().getContext();
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

    public NexusServerBean getCustomNexusServer() {
        NexusServerBean serverBean = null;
        try {
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            RepositoryContext repositoryContext = factory.getRepositoryContext();
            if (factory.isLocalConnectionProvider() || repositoryContext.isOffline()) {
                return null;
            }
            if (repositoryContext != null && repositoryContext.getFields() != null) {
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
                    JSONObject libServerObject;
                    libServerObject = remoteService.getLibNexusServer(userName, password, adminUrl);
                    if (libServerObject != null) {
                        String nexus_url = libServerObject.getString(NexusServerUtils.KEY_NEXUS_RUL);
                        String nexus_user = libServerObject.getString(NexusServerUtils.KEY_NEXUS_USER);
                        String nexus_pass = libServerObject.getString(NexusServerUtils.KEY_NEXUS_PASS);
                        String repositoryId = libServerObject.getString(NexusServerUtils.KEY_CUSTOM_LIB_REPOSITORY);

                        // TODO check if custom nexus is valid , only check http response for now , need check if it is
                        // snapshot latter
                        boolean connectionOk = NexusServerUtils.checkConnectionStatus(nexus_url, repositoryId, nexus_user,
                                nexus_pass);
                        if (!connectionOk) {
                            return null;
                        }

                        String newUrl = nexus_url;
                        if (newUrl.endsWith(NexusConstants.SLASH)) {
                            newUrl = newUrl.substring(0, newUrl.length() - 1);
                        }
                        if (nexus_user != null && !"".equals(nexus_user)) {//$NON-NLS-1$
                            String[] split = newUrl.split("://");//$NON-NLS-1$
                            if (split.length != 2) {
                                throw new RuntimeException("Nexus url is not valid ,please contract the administrator");
                            }
                            newUrl = split[0] + ":" + nexus_user + ":" + nexus_pass + "@//"//$NON-NLS-1$
                                    + split[1];
                        }
                        newUrl = newUrl + NexusConstants.CONTENT_REPOSITORIES + repositoryId + "@id=" + repositoryId;//$NON-NLS-1$ 

                        serverBean = new NexusServerBean();
                        serverBean.setServer(nexus_url);
                        serverBean.setUserName(nexus_user);
                        serverBean.setPassword(nexus_pass);
                        serverBean.setRepositoryId(repositoryId);
                        serverBean.setRepositoryUrl(newUrl);
                    }
                }

            }
        } catch (Exception e) {
            serverBean = null;
            ExceptionHandler.process(e);
        }
        if (previousCustomBean == null && serverBean != null || previousCustomBean != null
                && !previousCustomBean.equals(serverBean)) {
            mavenResolver = null;
        }
        previousCustomBean = serverBean;
        return serverBean;

    }

    public NexusServerBean getLibrariesNexusServer() {
        NexusServerBean serverBean = new NexusServerBean();
        serverBean.setServer(System.getProperty("org.talend.libraries.repo.url", NexusServerUtils.TALEND_LIB_SERVER));
        serverBean.setUserName(NexusServerUtils.TALEND_LIB_USER);
        serverBean.setPassword(NexusServerUtils.TALEND_LIB_PASSWORD);
        serverBean.setRepositoryId(NexusServerUtils.TALEND_LIB_REPOSITORY);
        serverBean.setOfficial(true);
        String server = serverBean.getServer();
        // remove the trailing slash
        if (server.endsWith(NexusConstants.SLASH)) {
            server = server.substring(0, server.length() - 1);
        }
        String newUrl = server + NexusConstants.CONTENT_REPOSITORIES + serverBean.getRepositoryId()
                + "@id=" + serverBean.getRepositoryId();//$NON-NLS-1$
        serverBean.setRepositoryUrl(newUrl);
        return serverBean;
    }

    public List<MavenArtifact> search(String nexusUrl, String userName, String password, String repositoryId,
            String groupIdToSearch, String artifactId, String versionToSearch) throws Exception {
        return NexusServerUtils.search(nexusUrl, userName, password, repositoryId, groupIdToSearch, artifactId, versionToSearch);
    }
}
