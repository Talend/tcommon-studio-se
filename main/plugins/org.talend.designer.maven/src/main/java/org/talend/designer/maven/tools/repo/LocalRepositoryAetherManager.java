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

import io.tesla.aether.connector.AetherRepositoryConnectorFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.model.Model;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.deployment.DeployResult;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallResult;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.metadata.Metadata;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.util.artifact.SubArtifact;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * FIXME, not impl yet
 */
public class LocalRepositoryAetherManager extends LocalRepositoryManager {

    private RepositorySystem repositorySystem;

    private LocalRepository localRepo;

    protected static final MavenModelManager MODEL_MANAGER = MavenPlugin.getMavenModelManager();

    private IFolder tempFolder;

    public LocalRepositoryAetherManager(File baseDir) {
        super(baseDir);
        this.repositorySystem = newRepositorySystem();
        this.localRepo = newRepository();
    }

    public LocalRepositoryAetherManager(IContainer container) {
        this(container.getLocation().toFile());
    }

    protected RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    protected LocalRepository getLocalRepo() {
        return localRepo;
    }

    protected IFolder getTempFolder() {
        if (tempFolder == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                        IRunProcessService.class);
                ITalendProcessJavaProject talendProcessJavaProject = service.getTalendProcessJavaProject();
                if (talendProcessJavaProject != null) {
                    tempFolder = talendProcessJavaProject.createSubFolder(null, (IFolder) talendProcessJavaProject
                            .getResourcesFolder().getParent(), "temp"); //$NON-NLS-1$
                } else {
                    try {
                        tempFolder = ResourceUtils.getProject(
                                ProjectManager.getInstance().getCurrentProject().getTechnicalLabel()).getFolder("temp");
                        if (!tempFolder.exists()) {
                            tempFolder.create(true, true, null);
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return tempFolder;
    }

    protected RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, AetherRepositoryConnectorFactory.class);
        locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);

        // locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        // locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        // locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {

            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                ExceptionHandler.process(exception);
            }
        });
        return locator.getService(RepositorySystem.class);
    }

    protected LocalRepository newRepository() {
        File dir = getRepoFolder();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new LocalRepository(dir);
    }

    protected RepositorySystemSession newSession() {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(session, localRepo));

        // session.setTransferListener(new ConsoleTransferListener());
        // session.setRepositoryListener(new ConsoleRepositoryListener());

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    @Override
    public void install(File file, MavenArtifact artifact) throws Exception {
        Artifact jarArtifact = createArtifact(file, artifact);
        Artifact pomArtifact = createPomArtifact(jarArtifact);

        InstallRequest installRequest = new InstallRequest().addArtifact(jarArtifact).addArtifact(pomArtifact);

        InstallResult result = getRepositorySystem().install(newSession(), installRequest);
        Collection<Metadata> metadata = result.getMetadata();
        metadata.isEmpty();
    }

    @Override
    public void deploy(File file, MavenArtifact artifact) throws Exception {
        Artifact jarArtifact = createArtifact(file, artifact);

        DeployRequest deployRequest = new DeployRequest().addArtifact(jarArtifact);

        // Artifact pomArtifact = null;
        // pomArtifact = new SubArtifact(jarArtifact, "", "pom").setFile(new File("pom.xml"));
        // if (pomArtifact != null) {
        // installRequest.addArtifact(pomArtifact);
        // }

        DeployResult result = getRepositorySystem().deploy(newSession(), deployRequest);
    }

    protected Artifact createArtifact(File file, MavenArtifact artifact) throws InstallationException {
        if (!file.exists()) {
            throw new InstallationException("The deploy file " + file + " is not existed.");
        }
        if (artifact == null) { // use default settings
            String mvnUrl = MavenUrlHelper.generateMvnUrlForJarName(file.getName());
            artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
            if (artifact == null) {
                throw new InstallationException("Can't deploy the file: " + file);
            }
        }
        Artifact jarArtifact = new DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(),
                artifact.getType(), artifact.getVersion()).setFile(file);

        return jarArtifact;
    }

    protected Artifact createPomArtifact(Artifact jarArtifact) throws IOException, CoreException {
        File createTempFile = File.createTempFile(TalendMavenConstants.PACKAGING_POM, "");
        createTempFile.delete();
        String tmpFolderName = createTempFile.getName();
        IFolder folder = getTempFolder().getFolder(tmpFolderName);
        folder.create(true, true, null);
        IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);

        Model pomModel = new Model();
        pomModel.setModelVersion(TalendMavenConstants.POM_VERSION);
        pomModel.setModelEncoding(TalendMavenConstants.DEFAULT_ENCODING);
        pomModel.setGroupId(jarArtifact.getGroupId());
        pomModel.setArtifactId(jarArtifact.getArtifactId());
        pomModel.setVersion(jarArtifact.getVersion());

        MODEL_MANAGER.createMavenModel(pomFile, pomModel);

        Artifact pomArtifact = new SubArtifact(jarArtifact, "", TalendMavenConstants.PACKAGING_POM).setFile(pomFile.getLocation()
                .toFile());
        return pomArtifact;
    }

    @Override
    public void cleanup(IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        try {
            IFolder folder = getTempFolder();
            if (folder == null) {
                return;
            }
            folder.refreshLocal(IResource.DEPTH_INFINITE, monitor);

            if (folder.exists()) {
                FilesUtils.deleteFolder(folder.getLocation().toFile(), false);
                folder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

    }

}
