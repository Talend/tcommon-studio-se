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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.utils.xml.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/*
 * Created by bhe on Sep 3, 2020
 */
public class JarDetector {

    private static final DocumentBuilderFactory docFactory = XmlUtils.getSecureDocumentBuilderFactory(true);

    private JarDetector() {
    }

    public static MavenArtifact parse(File jarFile) throws Exception {
        Properties p = new Properties();
        Document doc = null;
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = enumEntries.nextElement();
                if (!file.isDirectory()) {
                    String fname = file.getName();
                    if (StringUtils.contains(fname, "META-INF") && fname.endsWith("pom.xml")) {
                        try (InputStream fi = jar.getInputStream(file)) {
                            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                            Reader reader = new InputStreamReader(fi, "UTF-8");
                            InputSource is = new InputSource(reader);
                            is.setEncoding("UTF-8");
                            doc = docBuilder.parse(is);
                        }
                    }
                    if (StringUtils.contains(fname, "META-INF") && fname.endsWith("pom.properties")) {
                        try (InputStream fi = jar.getInputStream(file)) {
                            p.load(fi);
                            break;
                        }
                    }
                }
            }
        }
        if (!p.isEmpty()) {
            MavenArtifact art = new MavenArtifact();
            art.setGroupId(p.getProperty("groupId"));
            art.setArtifactId(p.getProperty("artifactId"));
            art.setVersion(p.getProperty("version"));
            art.setType(MavenConstants.TYPE_JAR);
            return art;
        }
        if (doc != null) {
            PomParser pp = new PomParser(doc);
            MavenArtifact art = new MavenArtifact();
            art.setGroupId(pp.getGroupId());
            art.setArtifactId(pp.getArtifactId());
            art.setVersion(pp.getVersion());
            art.setType(pp.getPackaging());
            return art;
        }

        return null;
    }

    public static String getMavenURL(MavenArtifact art) {
        if (art == null) {
            return "";
        }
        return String.format("mvn:%s/%s/%s/%s", art.getGroupId(), art.getArtifactId(), art.getVersion(), art.getType());
    }

}
