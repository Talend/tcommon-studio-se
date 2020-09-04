// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;
import org.talend.utils.xml.XmlUtils;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

/*
 * Created by bhe on Sep 3, 2020
 */
public class ConfigModuleHelper {

    private static final DocumentBuilderFactory docFactory = XmlUtils.getSecureDocumentBuilderFactory(true);

    private static final String LOCAL_M2 = MavenPlugin.getMaven().getLocalRepositoryPath();

    private ConfigModuleHelper() {

    }

    public static List<MavenArtifact> searchRemoteArtifacts(String name) throws Exception {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(customNexusServer);
        if (customerRepHandler != null) {
            List<MavenArtifact> ret = customerRepHandler.search(name, true);
            return ret;
        }
        return new ArrayList<MavenArtifact>();
    }

    public static String[] toArray(List<MavenArtifact> artifacts) {
        if (artifacts == null || artifacts.isEmpty()) {
            return new String[0];
        }

        List<String> ret = new ArrayList<String>();
        for (MavenArtifact art : artifacts) {
            ret.add(art.getFileName(false));
        }
        return ret.toArray(new String[0]);
    }

    public static List<MavenArtifact> searchLocalArtifacts(String name) throws Exception {
        List<MavenArtifact> ret = new ArrayList<MavenArtifact>();
        File m2Dir = new File(LOCAL_M2);
        if (m2Dir.exists()) {
            search(name, m2Dir, ret);
        }

        return ret;
    }

    private static void search(String name, File dir, List<MavenArtifact> ret) throws Exception {
        File[] fs = dir.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                search(name, f, ret);
            } else {
                if (f.isFile() && f.getName().endsWith(".jar")
                        && StringUtils.containsIgnoreCase(FilenameUtils.getBaseName(f.getName()), name)) {
                    String path = f.getPath().substring(LOCAL_M2.length() + 1, f.getPath().length());

                    MavenArtifact art = parse(path);
                    if (art != null) {
                        ret.add(art);
                    }
                }
            }
        }
    }

    public static MavenArtifact parse(String path) {
        MavenArtifact art = new MavenArtifact();
        if (path == null || StringUtils.isEmpty(path)) {
            return null;
        }
        String newPath = FilenameUtils.normalize(path, true);
        String[] segs = newPath.split("/");

        if (segs.length < 4) {
            return null;
        }

        String fname = segs[segs.length - 1];
        String v = segs[segs.length - 2];
        String a = segs[segs.length - 3];

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < segs.length - 3; i++) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(segs[i]);
        }
        art.setGroupId(sb.toString());
        art.setArtifactId(a);
        art.setVersion(v);
        art.setType("jar");

        String baseName = FilenameUtils.getBaseName(fname);
        int endIndex = a.length() + v.length() + 1;
        if (baseName.length() > endIndex + 1) {
            String classifier = baseName.substring(endIndex + 1, baseName.length());
            art.setClassifier(classifier);
        }
        return art;
    }

    public static File resolveLocal(String uri) {
        ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
                .getService(ILibraryManagerService.class);
        String jarPathFromMaven = libManagerService.getJarPathFromMaven(uri);
        if (jarPathFromMaven != null) {
            return new File(jarPathFromMaven);
        }

        try {
            File resolvedJar = libManagerService.resolveJar(null, uri);
            return resolvedJar;
        } catch (Exception e) {

        }
        return null;
    }

    public static String getSHA1(File f) {
        try (InputStream fi = new FileInputStream(f)) {
            return DigestUtils.shaHex(fi);
        } catch (Exception e) {
        }
        return null;
    }
    
    public static void install(File jarFile, String mvnUrl, boolean deploy) throws Exception {
        MavenArtifactsHandler deployer =  new MavenArtifactsHandler();
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
        
        final File tempFolder = FileUtils.createTmpFolder("generate", "pom"); //$NON-NLS-1$ //$NON-NLS-2$
        try {

            final String jarPath = jarFile.getAbsolutePath();

            String pomPath = PomUtil.generatePomInFolder(tempFolder, artifact);
            
            deployer.install(artifact.getUrl(), jarPath, pomPath, deploy);
        } finally {
            if (tempFolder.exists()) {
                FilesUtils.deleteFolder(tempFolder, true);
            }
        }
    }
    
    public static boolean canFind(Set<MavenArtifact> artifacts, File jarFile, String mvnUrl) {
        if (artifacts == null || artifacts.isEmpty()) {
            return false;
        }
        
        String jarSha1 = getSHA1(jarFile);
        MavenArtifact jarArt = MavenUrlHelper.parseMvnUrl(mvnUrl);
        jarArt.setSha1(jarSha1);

        return canFind(artifacts, jarArt);
    }
    
    public static boolean canFind(Set<MavenArtifact> artifacts, MavenArtifact artifact) {
        if (artifacts == null || artifacts.isEmpty()) {
            return false;
        }

        for (MavenArtifact art : artifacts) {
            if (StringUtils.equals(art.getGroupId(), artifact.getGroupId())
                    && StringUtils.equals(art.getArtifactId(), artifact.getArtifactId())
                    && StringUtils.equals(art.getVersion(), artifact.getVersion())
                    && StringUtils.equals(art.getClassifier(), artifact.getClassifier())
                    && StringUtils.equals(art.getType(), artifact.getType())
                    && StringUtils.equals(art.getSha1(), artifact.getSha1())) {
                return true;
            }
        }
        return false;
    }

    public static List<MavenArtifact> searchRemoteArtifacts(String g, String a, String v) throws Exception {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(customNexusServer);
        if (customerRepHandler != null) {
            boolean fromSnapshot = false;
            if (v != null && v.endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
                fromSnapshot = true;
            }
            List<MavenArtifact> ret = customerRepHandler.search(g, a, g, true, fromSnapshot);
            return ret;
        }
        return new ArrayList<MavenArtifact>();
    }

}
