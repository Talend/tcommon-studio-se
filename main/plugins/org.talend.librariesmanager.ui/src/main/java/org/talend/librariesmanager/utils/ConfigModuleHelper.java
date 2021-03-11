// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.nexus.utils.VersionUtil;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;

/*
 * Created by bhe on Sep 3, 2020
 */
public class ConfigModuleHelper {


    private static final String LOCAL_M2 = MavenPlugin.getMaven().getLocalRepositoryPath();

    private static final Set<String> IGNORED_FILE_EXTS = new HashSet<String>();
    static {
        IGNORED_FILE_EXTS.add("repositories");
        IGNORED_FILE_EXTS.add("lastUpdated");
        IGNORED_FILE_EXTS.add("sha1");
        IGNORED_FILE_EXTS.add("md5");
        IGNORED_FILE_EXTS.add("pom");
        IGNORED_FILE_EXTS.add("xml");
    }

    private ConfigModuleHelper() {

    }

    public static List<MavenArtifact> searchRemoteArtifacts(String name) throws Exception {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(customNexusServer);
        if (customerRepHandler != null) {
            List<MavenArtifact> ret = customerRepHandler.search(name, true);

            return VersionUtil.filterSnapshotArtifacts(ret);
        }
        return new ArrayList<MavenArtifact>();
    }

    public static String[] toArray(List<MavenArtifact> artifacts) {
        if (artifacts == null || artifacts.isEmpty()) {
            return new String[0];
        }

        List<String> ret = new ArrayList<String>();
        for (MavenArtifact art : artifacts) {
            ret.add(art.getFileName());
        }
        return ret.toArray(new String[0]);
    }

    public static String[] toArrayUnique(String[] arr) {
        if (arr == null) {
            return new String[0];
        }

        Set<String> ret = new HashSet<String>();
        for (String art : arr) {
            ret.add(art);
        }
        return ret.toArray(new String[0]);
    }

    public static List<MavenArtifact> searchLocalArtifacts(String name) throws Exception {
        List<MavenArtifact> ret = new ArrayList<MavenArtifact>();
        File m2Dir = new File(LOCAL_M2);
        if (m2Dir.exists()) {
            search(name, m2Dir, ret);
        }

        return VersionUtil.filterSnapshotArtifacts(ret);

    }

    private static void search(String name, File dir, List<MavenArtifact> ret) throws Exception {
        File[] fs = dir.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                search(name, f, ret);
            } else {
                if (f.isFile() && StringUtils.containsIgnoreCase(FilenameUtils.getName(f.getName()), name)) {

                    String ext = FilenameUtils.getExtension(f.getName());
                    if (IGNORED_FILE_EXTS.contains(ext)) {
                        continue;
                    }

                    String path = f.getPath().substring(LOCAL_M2.length() + 1, f.getPath().length());

                    MavenArtifact art = parse(path, ext);
                    if (art != null) {
                        ret.add(art);
                    }
                }
            }
        }
    }

    public static MavenArtifact parse(String path, String ext) {
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
        art.setType(ext);

        String baseName = FilenameUtils.getBaseName(fname);
        if (!baseName.contains(v)) {
            return null;
        }

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
            File retFile = new File(jarPathFromMaven);
            if (retFile.exists()) {
                return retFile;
            }
        }

        ModuleStatusProvider.putStatus(uri, ELibraryInstallStatus.NOT_INSTALLED);
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
        LibManagerUiPlugin.getDefault().getLibrariesService().deployLibrary(jarFile.toURL(), mvnUrl, true, deploy);
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
                    && StringUtils.equals(art.getClassifier(), artifact.getClassifier())
                    && StringUtils.equals(art.getVersion(), artifact.getVersion())
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
            List<MavenArtifact> ret = customerRepHandler.search(g, a, v, true, true);

            if (customNexusServer.getType() == ArtifactRepositoryBean.NexusType.NEXUS_2.name()) {
                // resolve sha1
                for (MavenArtifact art : ret) {
                    String sha1 = customerRepHandler.resolveRemoteSha1(art, false);
                    if (sha1 != null) {
                        art.setSha1(sha1);
                    }
                    if (StringUtils.isEmpty(art.getSha1())) {
                        sha1 = customerRepHandler.resolveRemoteSha1(art, true);
                        if (sha1 != null) {
                            art.setSha1(sha1);
                        }
                    }
                }
            }
            return VersionUtil.filterSnapshotArtifacts(ret);
        }
        return new ArrayList<MavenArtifact>();
    }

    public static void resolveSha1(MavenArtifact art) throws Exception {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(customNexusServer);
        if ((art.getSha1() == null || art.getSha1().trim().isEmpty()) && customerRepHandler != null
                && customNexusServer.getType().equals(ArtifactRepositoryBean.NexusType.NEXUS_2.name())) {
            boolean fromSnapshot = false;
            if (art.getVersion() != null && art.getVersion().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
                fromSnapshot = true;
            }
            // resolve sha1
            String sha1 = customerRepHandler.resolveRemoteSha1(art, !fromSnapshot);
            if (sha1 != null) {
                art.setSha1(sha1);
            }
        }
    }

    public static String getDetectURI(String jarPath) {
        String ext = FilenameUtils.getExtension(jarPath);
        if (ext.equalsIgnoreCase("jar")) {
            File file = new File(jarPath);
            try {
                MavenArtifact art = JarDetector.parse(file);
                if (art != null) {
                    return MavenUrlHelper.generateMvnUrl(art);
                }
            } catch (Exception e) {

            }
        }
        return "";
    }

    public static String getMavenURI(String jarPath) {
        String jarName = FilenameUtils.getName(jarPath);
        ModuleNeeded mod = null;
        for (ModuleNeeded module : ModulesNeededProvider.getAllManagedModules()) {
            if (jarName.equals(module.getModuleName())) {
                mod = module;
                break;
            }
        }
        if (mod != null) {
            return mod.getMavenUri() == null ? "" : mod.getMavenUri();
        }

        return "";
    }

    public static String getGeneratedDefaultURI(String jarPath) {
        String jarName = FilenameUtils.getName(jarPath);
        return MavenUrlHelper.generateMvnUrlForJarName(jarName);
    }

    public static boolean isSameUri(String defaultUri, String detectUri) {
        detectUri = detectUri.substring(MavenUrlHelper.MVN_PROTOCOL.length());
        return StringUtils.endsWith(defaultUri, detectUri);
    }

    public static boolean showRemoteSearch() {
        ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
        if (customNexusServer != null) {
            String repoType = customNexusServer.getType();
            if (repoType.equals(ArtifactRepositoryBean.NexusType.NEXUS_2.name())
                    || repoType.equals(ArtifactRepositoryBean.NexusType.NEXUS_3.name())
                    || repoType.equals(ArtifactRepositoryBean.NexusType.ARTIFACTORY.name())) {
                return true;
            }
        }
        return false;
    }

    public static boolean notShowConnectionWarning() {
        try {
            ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (customerRepHandler != null) {
                return customerRepHandler.checkConnection();
            }
        } catch (Exception e) {

        }

        // ignore local
        return true;
    }

}
