// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenRepoSynchronizer {

    // the folder is m2/repository
    private final File sourceM2Root;

    private final MavenArtifactsHandler deployer;

    private boolean deployToRemote;

    private static final String POM_EXT = '.' + TalendMavenConstants.PACKAGING_POM;

    public MavenRepoSynchronizer(File sourceM2Root, boolean deployToRemote) {
        super();
        this.sourceM2Root = sourceM2Root;
        this.deployToRemote = deployToRemote;

        this.deployer = new MavenArtifactsHandler();
    }

    public MavenRepoSynchronizer(File sourceM2Root) {
        this(sourceM2Root, true); // will deploy to remote by default
    }

    public void sync() {
        if (sourceM2Root != null && sourceM2Root.exists()) {
            installM2RepositoryLibs(sourceM2Root);
        }
    }

    protected void installM2RepositoryLibs(File parentFolder) {

        Set<File> allPomFiles = new HashSet<File>();

        getAllPomFiles(allPomFiles, parentFolder);

        Set<File> deployedPomFiles = getDeployedPomFiles(allPomFiles);

        for (File pomFile : allPomFiles) {
            try {
                ExtendedMavenArtifact artifact = parseArtifact(pomFile);
                if (artifact == null || artifact.getArtifact() == null) {
                    Exception e = new RuntimeException("Can not parse artifact from pomFile: " + pomFile);
                    ExceptionHandler.process(e);
                    continue;
                }

                IPath libPath = new Path(pomFile.getAbsolutePath()).removeFileExtension()
                        .addFileExtension(artifact.getArtifact().getType());
                final File libFile = libPath.toFile();
                if (libFile.exists()) {
                    final File tempFolder = FileUtils.createTmpFolder("generate", "pom"); //$NON-NLS-1$ //$NON-NLS-2$
                    try {

                        final String jarPath = libFile.getAbsolutePath();

                        // final String pomPath=pomFile.getAbsolutePath();
                        // TUP-17785, make sure generate new one always without any dependences, so null
                        String pomPath;
                        if (artifact.isMavenPlugin() || artifact.isPackagingPom()) {
                            // use original pom to keep dependencies for maven plugins
                            pomPath = pomFile.getAbsolutePath();
                        } else {
                            pomPath = PomUtil.generatePomInFolder(tempFolder, artifact.getArtifact());
                        }
                        boolean deploy = deployToRemote;
                        if (deployedPomFiles.contains(pomFile)) {
                            deploy = false;
                        }
                        deployer.install(artifact.getMvnUrl(), jarPath, pomPath, deploy);
                    } finally {
                        if (tempFolder.exists()) {
                            FilesUtils.deleteFolder(tempFolder, true);
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public static void getAllPomFiles(Set<File> allPomsFiles, File parentFolder) {
        if (parentFolder != null && parentFolder.exists() && parentFolder.isDirectory()) {
            File[] allFiles = parentFolder.listFiles();
            if (allFiles == null || allFiles.length == 0) {
                return;
            }

            for (File file : allFiles) {
                if (file.isDirectory()) {
                    getAllPomFiles(allPomsFiles, file);
                } else if (file.isFile()) {
                    if (file.getName().endsWith(POM_EXT)) {
                        allPomsFiles.add(file);
                    }
                }
            }
        }
    }

    public static ExtendedMavenArtifact parseArtifact(File pomFile) {
        try {
            Model model = MavenPlugin.getMaven().readModel(pomFile);
            String packaging = model.getPackaging();
            if (packaging == null) {
                packaging = TalendMavenConstants.PACKAGING_JAR;
            }
            boolean isMavenPlugin = packaging.equals("maven-plugin"); //$NON-NLS-1$
            boolean isPackagingPom = packaging.equals("pom"); //$NON-NLS-1$
            // use jar instead
            if (packaging.equals("bundle") || isMavenPlugin) { //$NON-NLS-1$
                packaging = TalendMavenConstants.PACKAGING_JAR;
            }
            final String groupId = (model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId());
            final String artifactId = model.getArtifactId();
            final String version = (model.getVersion() != null ? model.getVersion() : model.getParent().getVersion());

            final String mvnUrl = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version, packaging, null);
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
            return new ExtendedMavenArtifact(mvnUrl, artifact, isMavenPlugin, isPackagingPom);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    public static Set<File> getDeployedPomFiles(Set<File> allPomFiles) {
        Set<File> ret = new HashSet<File>();
        
        Map<MavenArtifact, File> artifact2Poms = new HashMap<MavenArtifact, File>();
        Set<String> releaseGroupIds = new HashSet<String>();
        Set<String> snapshotGroupIds = new HashSet<String>();
        for (File pomFile : allPomFiles) {
            ExtendedMavenArtifact eart = parseArtifact(pomFile);

            if (eart != null && eart.getArtifact() != null) {
                MavenArtifact art = eart.getArtifact();
                artifact2Poms.put(art, pomFile);
                if (art.getVersion() != null && art.getVersion().toUpperCase().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
                    if (art.getGroupId() != null) {
                        snapshotGroupIds.add(art.getGroupId());
                    }
                } else {
                    if (art.getGroupId() != null) {
                        releaseGroupIds.add(art.getGroupId());
                    }
                }
            }
        }
        
        Set<MavenArtifact> deployedArts = new HashSet<MavenArtifact>();
        // search release repository of remote artifactory server
        ArtifactRepositoryBean artifactServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        IRepositoryArtifactHandler artifactHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactServer);
        if (artifactHandler != null) {
            try {
                for (String gid : releaseGroupIds) {
                    List<MavenArtifact> searchResults = artifactHandler.search(gid, null, null, true, false);
                    if (searchResults != null && !searchResults.isEmpty()) {
                        deployedArts.addAll(searchResults);
                    }
                }
                for (String gid : snapshotGroupIds) {
                    List<MavenArtifact> searchResults = artifactHandler.search(gid, null, null, false, true);
                    if (searchResults != null && !searchResults.isEmpty()) {
                        deployedArts.addAll(searchResults);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        // check sha1
        for (MavenArtifact art : deployedArts) {
            File pomFile = artifact2Poms.get(art);
            if (pomFile != null) {
                IPath libPath = new Path(pomFile.getAbsolutePath()).removeFileExtension().addFileExtension(art.getType());
                final File libFile = libPath.toFile();
                if (libFile.exists()) {
                    String sha1 = getSHA1(libFile);
                    if (art.getSha1() != null && art.getSha1().equals(sha1)) {
                        // already deployed
                        ret.add(pomFile);
                    }
                }
            }
        }
        return ret;
    }

    public static String getSHA1(File f) {
        try (InputStream fi = new FileInputStream(f)) {
            return DigestUtils.shaHex(fi);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    static class ExtendedMavenArtifact {

        private MavenArtifact artifact;

        private boolean isMavenPlugin;

        private boolean isPackagingPom;

        private String mvnUrl;

        public ExtendedMavenArtifact(String mvnUrl, MavenArtifact artifact, boolean isMavenPlugin, boolean isPackagingPom) {
            this.artifact = artifact;
            this.isMavenPlugin = isMavenPlugin;
            this.mvnUrl = mvnUrl;
            this.isPackagingPom = isPackagingPom;
        }

        public MavenArtifact getArtifact() {
            return artifact;
        }

        public boolean isMavenPlugin() {
            return isMavenPlugin;
        }

        public String getMvnUrl() {
            return mvnUrl;
        }

        public boolean isPackagingPom() {
            return isPackagingPom;
        }
    }
}
