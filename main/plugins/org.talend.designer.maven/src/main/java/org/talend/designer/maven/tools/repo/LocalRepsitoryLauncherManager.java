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
package org.talend.designer.maven.tools.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.m2e.actions.MavenLaunchConstants;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.launch.MavenCommandLauncher;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * Sometimes it can't work at all.
 */
@SuppressWarnings("restriction")
public class LocalRepsitoryLauncherManager extends LocalRepositoryManager {

    public LocalRepsitoryLauncherManager() {
        super();
    }

    public LocalRepsitoryLauncherManager(File baseDir) {
        super(baseDir);
    }

    public LocalRepsitoryLauncherManager(IContainer container) {
        super(container);
    }

    /**
     * mvn install:install-file -Dfile=test_1.0.jar -DgroupId=org.talend.libraries -DartifactId=test_1.0 -Dversion=1.0.0
     * -Dpackaging=jar
     */
    @Override
    public void install(File file, final MavenArtifact artifact) throws Exception {
        CustomJarLaunchProcessor processor = new CustomJarLaunchProcessor("install:install-file", file) {

            @Override
            protected MavenArtifact createArtifact() throws Exception {
                if (artifact != null) {
                    return artifact;
                }
                return super.createArtifact();
            }
        };
        processor.launch();
    }

    /**
     * mvn deploy:deploy-file -Dfile=test_1.0.jar -DgroupId=org.talend.libraries -DartifactId=test_1.0 -Dversion=1.0.0
     * -Dpackaging=jar -Durl=file:///works/repo -DrepositoryId=LocalRepo
     */
    @Override
    public void deploy(File file, final MavenArtifact artifact) throws Exception {

        // The "Repository Id to map on the <id> under <server> section of settings.xml." for "repositoryId".
        // TODO, need set the server id for repositoryId

        CustomJarLaunchProcessor processor = new CustomJarLaunchProcessor("deploy:deploy-file", file) {

            @Override
            protected MavenArtifact createArtifact() throws Exception {
                if (artifact != null) {
                    return artifact;
                }
                return super.createArtifact();
            }

            @Override
            protected List<String> createProperties(MavenArtifact artifact) throws Exception {
                List<String> properties = super.createProperties(artifact);

                File repoFolder = getRepoFolder();
                if (!repoFolder.exists()) {
                    repoFolder.mkdirs();
                }
                URL url = repoFolder.toURI().toURL();
                properties.add("url=" + url);
                properties.add("repositoryId=" + REPO_ID);
                return properties;
            }

        };
        processor.launch();
    }

    /**
     * 
     * DOC ggu CustomJarProcessor class global comment. Detailled comment
     */
    static class CustomJarLaunchProcessor {

        private String goal;

        private File jarFile;

        public CustomJarLaunchProcessor(String goal, File jarFile) {
            super();
            this.goal = goal;
            this.jarFile = jarFile;
        }

        protected MavenArtifact createArtifact() throws Exception {
            if (!jarFile.exists()) {
                throw new FileNotFoundException(jarFile.toString());
            }

            String mvnUrl = MavenUrlHelper.generateMvnUrlForJarName(jarFile.getName());
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
            if (artifact == null) {
                throw new Exception("Can't install the file to maven repository.");
            }
            return artifact;
        }

        protected List<String> createProperties(MavenArtifact artifact) throws Exception {
            final List<String> properties = new ArrayList<String>();

            properties.add("file=" + jarFile.getAbsolutePath());
            String name = jarFile.getName();
            String fileName = name;
            String ext = null;
            int index = name.lastIndexOf('.');
            if (index >= 0) {
                fileName = name.substring(0, index);
                ext = name.substring(index + 1);

            }
            String groupId = artifact.getGroupId();
            if (groupId == null || groupId.trim().length() == 0) {
                groupId = MavenConstants.DEFAULT_LIB_GROUP_ID;
            }
            properties.add("groupId=" + groupId);

            String artifactId = artifact.getArtifactId();
            if (artifactId == null || artifactId.trim().length() == 0) {
                artifactId = fileName; // use file name
            }
            properties.add("artifactId=" + artifactId);

            String version = artifact.getVersion();
            if (version == null || version.trim().length() == 0) {
                version = MavenConstants.DEFAULT_LIB_VERSION;
            }
            properties.add("version=" + version);

            String type = artifact.getType();
            if (type == null || type.trim().length() == 0) { // use real file extension
                if (ext != null) {
                    type = ext;
                } else {
                    type = MavenConstants.TYPE_JAR; // jar by default?
                }
            }
            properties.add("packaging=" + type);

            return properties;
        }

        public void launch() throws Exception {
            // DefaultArtifactInstaller installer = new DefaultArtifactInstaller();
            // installer.install(file, arg1, arg2);

            MavenArtifact artifact = createArtifact();
            final List<String> properties = createProperties(artifact);

            MavenCommandLauncher launcher = new MavenCommandLauncher(goal) {

                @Override
                protected void setProjectConfiguration(ILaunchConfigurationWorkingCopy workingCopy, IContainer basedir) {
                    super.setProjectConfiguration(workingCopy, basedir);
                    workingCopy.setAttribute(MavenLaunchConstants.ATTR_PROPERTIES, properties);
                }

            };

            launcher.execute(new NullProgressMonitor());
        }
    }
}
