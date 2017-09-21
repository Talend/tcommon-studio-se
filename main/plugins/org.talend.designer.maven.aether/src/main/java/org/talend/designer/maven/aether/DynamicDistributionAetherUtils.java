// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.version.Version;
import org.eclipse.core.runtime.CoreException;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionAetherUtils {

    private static Map<String, RepositorySystem> repoSystemMap = new HashMap<>();

    private static Map<String, RepositorySystemSession> sessionMap = new HashMap<>();

    public static DependencyNode collectDepencencies(String remoteUrl, String localPath, DependencyNode dependencyNode,
            IDynamicMonitor monitor) throws Exception {

        String groupId = dependencyNode.getGroupId();
        String artifactId = dependencyNode.getArtifactId();
        String classifier = dependencyNode.getClassifier();
        String version = dependencyNode.getVersion();
        String scope = dependencyNode.getScope();

        if (scope == null) {
            scope = JavaScopes.COMPILE;
        }

        String key = remoteUrl + " | " + localPath; //$NON-NLS-1$

        RepositorySystem repoSystem = repoSystemMap.get(key);
        if (repoSystem == null) {
            repoSystem = newRepositorySystem();
            repoSystemMap.put(key, repoSystem);
        }
        RepositorySystemSession session = sessionMap.get(key);
        if (session == null) {
            session = newSession(repoSystem, localPath, monitor);
            sessionMap.put(key, session);
        }

        org.eclipse.aether.graph.Dependency dependency = new org.eclipse.aether.graph.Dependency(
                new DefaultArtifact(groupId, artifactId, classifier, null, version), scope);

        List<ExclusionNode> exclusionNodes = dependencyNode.getExclusions();
        if (exclusionNodes != null && !exclusionNodes.isEmpty()) {
            Collection<Exclusion> newExclusions = new LinkedHashSet<>();
            Collection<Exclusion> exclusions = dependency.getExclusions();
            newExclusions.addAll(exclusions);
            for (ExclusionNode exclusionNode : exclusionNodes) {
                String exclusionGroupId = exclusionNode.getGroupId();
                String exclusionArtifactId = exclusionNode.getArtifactId();
                String exclusionClassifier = exclusionNode.getClassifier();
                String exclusionExtension = exclusionNode.getExtension();
                Exclusion exclusion = new Exclusion(exclusionGroupId, exclusionArtifactId, exclusionClassifier,
                        exclusionExtension);
                newExclusions.add(exclusion);
            }
            dependency = dependency.setExclusions(newExclusions);
        }

        RemoteRepository central = new RemoteRepository.Builder("central", "default",
                remoteUrl).build();

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(central);

        org.eclipse.aether.graph.DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();

        DependencyNode convertedNode = convert(node);

        return convertedNode;

    }

    public static List<String> versionRange(String remoteUrl, String localPath, String groupId, String artifactId,
            String baseVersion, String topVersion, IDynamicMonitor monitor)
            throws Exception {
        RepositorySystem repSystem = newRepositorySystem();
        RepositorySystemSession repSysSession = newSession(repSystem, localPath, monitor);

        String base = baseVersion;
        if (base == null || base.isEmpty()) {
            base = "0";
        }
        String range = ":[" + base + ",";
        if (topVersion != null && !topVersion.isEmpty()) {
            // :[0,)
            range = range + topVersion + "]";
        } else {
            // :[0,1]
            range = range + ")";
        }

        Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + range); //$NON-NLS-1$
        RemoteRepository central = new RemoteRepository.Builder("central", "default",
                remoteUrl).build();

        VersionRangeRequest verRangeRequest = new VersionRangeRequest();
        verRangeRequest.addRepository(central);
        verRangeRequest.setArtifact(artifact);

        VersionRangeResult rangeResult = repSystem.resolveVersionRange(repSysSession, verRangeRequest);
        List<Version> versions = rangeResult.getVersions();
        List<String> versionList = new ArrayList<>();
        for (Version version : versions) {
            versionList.add(version.toString());
        }

        return versionList;
    }

    private static DependencyNode convert(org.eclipse.aether.graph.DependencyNode node) {
        DependencyNode convertedNode = new DependencyNode();

        Artifact artifact = node.getArtifact();
        convertedNode.setArtifactId(artifact.getArtifactId());
        convertedNode.setClassifier(artifact.getClassifier());
        convertedNode.setExtension(artifact.getExtension());
        convertedNode.setGroupId(artifact.getGroupId());
        convertedNode.setVersion(artifact.getVersion());

        List<org.eclipse.aether.graph.DependencyNode> children = node.getChildren();
        List<DependencyNode> convertedChildren = new ArrayList<>();
        convertedNode.setDependencies(convertedChildren);

        if (children != null) {
            for (org.eclipse.aether.graph.DependencyNode child : children) {
                DependencyNode convertedChild = convert(child);
                convertedChildren.add(convertedChild);
            }
        }

        return convertedNode;
    }

    // private static void getAllArtifact(DependencyNode node, List<Artifact> list) {
    // if (node == null) {
    // return;
    // }
    // list.add(node.getArtifact());
    // List<DependencyNode> children = node.getChildren();
    // for (DependencyNode dn : children) {
    // getAllArtifact(dn, list);
    // }
    // }
    
    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        return locator.getService(RepositorySystem.class);
    }

    private static RepositorySystemSession newSession(RepositorySystem system, String repositoryPath, IDynamicMonitor monitor)
            throws CoreException {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        // LocalRepository localRepo = new LocalRepository(MavenPlugin.getMaven().getLocalRepositoryPath());
        LocalRepository localRepo = new LocalRepository(repositoryPath);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        DependencySelector defaultSelector = session.getDependencySelector();
        DynamicDependencySelector newSelector = new DynamicDependencySelector();
        newSelector.setProxy(defaultSelector);
        newSelector.setMonitor(monitor);
        session.setDependencySelector(newSelector);

        // DependencyManager defaultDependencyManager = session.getDependencyManager();
        // DynamicDependencyManager newDependencyManager = new DynamicDependencyManager();
        // newDependencyManager.setProxy(defaultDependencyManager);
        // session.setDependencyManager(newDependencyManager);

        return session;
    }

}
