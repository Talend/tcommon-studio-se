// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.aether;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.SubArtifact;
import org.eclipse.aether.util.listener.ChainedRepositoryListener;
import org.eclipse.aether.util.listener.ChainedTransferListener;
import org.eclipse.aether.util.repository.AuthenticationBuilder;

/**
 * created by wchen on Aug 10, 2017 Detailled comment
 *
 */
public class RepositorySystemFactory {

    private static Map<LocalRepository, DefaultRepositorySystemSession> sessions = new HashMap<LocalRepository, DefaultRepositorySystemSession>();

    private static RepositorySystem system;

    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {

            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                exception.printStackTrace();
            }
        });

        return locator.getService(RepositorySystem.class);
    }

    private static DefaultRepositorySystemSession newRepositorySystemSession(String localRepositoryPath) {
        LocalRepository localRepo = new LocalRepository(localRepositoryPath);
        DefaultRepositorySystemSession repositorySystemSession = sessions.get(localRepo);
        if (repositorySystemSession == null) {
            repositorySystemSession = MavenRepositorySystemUtils.newSession();
            repositorySystemSession.setLocalRepositoryManager(system
                    .newLocalRepositoryManager(repositorySystemSession, localRepo));
            repositorySystemSession.setTransferListener(new ChainedTransferListener());
            repositorySystemSession.setRepositoryListener(new ChainedRepositoryListener());
        }

        return repositorySystemSession;
    }

    public static void deploy(File content, String localRepository, String repositoryId, String repositoryUrl, String userName,
            String password, String groupId, String artifactId, String classifier, String extension, String version)
            throws Exception {
        DefaultRepositorySystemSession session = null;
        if (system == null) {
            system = newRepositorySystem();
        }
        session = newRepositorySystemSession(localRepository);

        DeployRequest deployRequest = new DeployRequest();
        Artifact jarArtifact = new DefaultArtifact(groupId, artifactId, classifier, extension, version);
        jarArtifact = jarArtifact.setFile(content);
        deployRequest.addArtifact(jarArtifact);

        String strClassifier = classifier == null ? "" : ("-" + classifier);
        String pomPath = localRepository + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/"
                + artifactId + "-" + version + strClassifier + "." + "pom";
        File pomFile = new File(pomPath);
        if (pomFile.exists()) {
            Artifact pomArtifact = new SubArtifact(jarArtifact, "", "pom");
            pomArtifact = pomArtifact.setFile(pomFile);
            deployRequest.addArtifact(pomArtifact);
        }

        Authentication auth = new AuthenticationBuilder().addUsername(userName).addPassword(password).build();
        RemoteRepository distRepo = new RemoteRepository.Builder(repositoryId, "default", repositoryUrl).setAuthentication(auth)
                .build();

        deployRequest.setRepository(distRepo);

        system.deploy(session, deployRequest);
    }

}
