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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
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
}
