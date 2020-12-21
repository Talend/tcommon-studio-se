package org.talend.librariesmanager.nexus.utils;

import java.util.Properties;

import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;

public class NexusServerManagerProxy {

	private static String NEXUS_USER = "nexus.user";
	private static String NEXUS_PASSWORD = "nexus.password";
	private static String NEXUS_URL = "nexus.url";
	private static String NEXUS_LIB_REPO = "nexus.lib.repo";
	private static String NEXUS_LIB_SNAPSHOT_REPO = "nexus.lib.repo.snapshot";
	private static String DEFAULT_LIB_REPO = "rep-for-test-releases";
	private static String DEFAULT_LIB_SNAPSHOT_REPO = "rep-for-test-snapshots";
	private static String NEXUS_LIB_SERVER_TYPE = "nexus.lib.server.type";
	
	private static Properties props;
	private static NexusServerManagerProxy manager = null;
	
	public static synchronized NexusServerManagerProxy getInstance() {
        if (manager == null) {
            manager = new NexusServerManagerProxy();
        }
        return manager;
    }

	public ArtifactRepositoryBean getCustomNexusServer() {
		String nexus_url = System.getProperty(NEXUS_URL);
		String nexus_user = System.getProperty(NEXUS_USER);
		String nexus_pass = System.getProperty(NEXUS_PASSWORD);
		String repositoryId = System.getProperty(NEXUS_LIB_REPO, DEFAULT_LIB_REPO);
		String snapshotRepId = System.getProperty(NEXUS_LIB_SNAPSHOT_REPO, DEFAULT_LIB_SNAPSHOT_REPO);
		String serverType = System.getProperty(NEXUS_LIB_SERVER_TYPE, "NEXUS_3");
		ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
		serverBean.setServer(nexus_url);
		serverBean.setUserName(nexus_user);
		serverBean.setPassword(nexus_pass);
		serverBean.setRepositoryId(repositoryId);
		serverBean.setSnapshotRepId(snapshotRepId);
		serverBean.setType(serverType);
		return serverBean;
	}
	
	public ArtifactRepositoryBean getDummyNexusServer() {
        ArtifactRepositoryBean serverBean = getCustomNexusServer();
        serverBean.setServer("http://localhost:0000");
        return serverBean;
    }
}
