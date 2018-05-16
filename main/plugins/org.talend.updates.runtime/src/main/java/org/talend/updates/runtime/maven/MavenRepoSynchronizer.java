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
package org.talend.updates.runtime.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
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
        if (parentFolder != null && parentFolder.exists() && parentFolder.isDirectory()) {
            final File[] allFiles = parentFolder.listFiles();
            if (allFiles == null) {
                return;
            }
            List<File> pomFiles = new ArrayList<File>();
            List<File> subFiles = new ArrayList<File>();
            final String pomExt = '.' + TalendMavenConstants.PACKAGING_POM;
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    subFiles.add(file);
                } else if (file.isFile()) {
                    if (file.getName().endsWith(pomExt)) {
                        pomFiles.add(file);
                    }
                }
            }
            for (File pomFile : pomFiles) {
                try {
                    Model model = MavenPlugin.getMaven().readModel(pomFile);
                    String packaging = model.getPackaging();
                    if (packaging == null) {
                        packaging = TalendMavenConstants.PACKAGING_JAR;
                    }
                    // use jar instead
                    if (packaging.equals("bundle")) { //$NON-NLS-1$
                        packaging = TalendMavenConstants.PACKAGING_JAR;
                    }
                    final String groupId = (model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId());
                    final String artifactId = model.getArtifactId();
                    final String version = (model.getVersion() != null ? model.getVersion() : model.getParent().getVersion());

                    final String mvnUrl = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version, packaging, null);

                    IPath libPath = new Path(pomFile.getAbsolutePath()).removeFileExtension().addFileExtension(packaging);
                    final File libFile = libPath.toFile();
                    if (libFile.exists()) {
                        final File tempFolder = FileUtils.createTmpFolder("generate", "pom"); //$NON-NLS-1$  //$NON-NLS-2$
                        try {
                            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);

                            final String jarPath = libFile.getAbsolutePath();

                            // final String pomPath=pomFile.getAbsolutePath();
                            // TUP-17785, make sure generate new one always without any dependences, so null
                            final String pomPath = PomUtil.generatePomInFolder(tempFolder, artifact);

                            deployer.install(mvnUrl, jarPath, pomPath, deployToRemote);
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

            // children folders
            for (File subFolder : subFiles) {
                installM2RepositoryLibs(subFolder);
            }
        }
    }
}
