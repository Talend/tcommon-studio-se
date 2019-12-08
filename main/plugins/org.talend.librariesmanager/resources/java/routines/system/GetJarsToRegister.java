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
package routines.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class GetJarsToRegister {

    private String oozieClasspathLine;

    private boolean isOozieRuntime;

    public GetJarsToRegister() {
        try {
            this.isOozieRuntime = setJarsToRegister();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setJarsToRegister() throws IOException, org.dom4j.DocumentException {
        String jobXmlPath = new java.io.File("../../job.xml").getCanonicalPath();
        boolean isOozieExecution = isNeedAddLibsPath(jobXmlPath);
        if (!isOozieExecution) {
            jobXmlPath = new java.io.File("./job.xml").getCanonicalPath();
            isOozieExecution = isNeedAddLibsPath(jobXmlPath);
        }
        if (isOozieExecution) {

            SAXReader reader_oozie = new org.dom4j.io.SAXReader();
            org.dom4j.Document document_oozie = reader_oozie.read(jobXmlPath);
            List<Node> list_oozie = document_oozie.selectNodes("/configuration/property");
            for (Node node : list_oozie) {
                Element element_oozie = (org.dom4j.Element) node;
                String name_oozie = element_oozie.elementText("name");
                if (name_oozie.equals("mapred.cache.localFiles") || name_oozie.equals("mapreduce.job.cache.local.files")) {
                    this.oozieClasspathLine = element_oozie.elementText("value");
                    return true;
                }
            }
        }
        return false;
    }

    public String replaceJarPaths(String originalClassPathLine) throws Exception {
        return replaceJarPaths(originalClassPathLine, "");
    }

    public String replaceJarPaths(String originalClassPathLine, String scheme) throws Exception {
        return replaceJarPaths(originalClassPathLine, scheme, false);
    }

    public String replaceJarPaths(String originalClassPathLine, String scheme, boolean encodeSpaces) throws Exception {
        String classPathLine = "";
        String crcMapPath = new java.io.File("../crcMap").getCanonicalPath();

        if (isNeedAddLibsPath(crcMapPath)) {
            java.util.Map<String, String> crcMap = null;
            try (java.io.ObjectInputStream ois = new ObjectInputStream(new java.io.FileInputStream(crcMapPath))) {
                crcMap = (java.util.Map<String, String>) ois.readObject();
            }
            classPathLine = addLibsPath(originalClassPathLine, crcMap);
        } else if (this.isOozieRuntime) {
            if (this.oozieClasspathLine != null) {
                List<String> oozieJars = java.util.Arrays.asList(this.oozieClasspathLine.split(","));
                for (int j = 0; j < oozieJars.size(); j++) {
                    if (oozieJars.get(j).contains(originalClassPathLine.substring(originalClassPathLine.lastIndexOf("/")))) {
                        classPathLine = oozieJars.get(j);
                        break;
                    }
                }
            }
        } else {
            if (originalClassPathLine != null && originalClassPathLine.startsWith(".")) {
                classPathLine = originalClassPathLine;
            } else {
                classPathLine = scheme + originalClassPathLine;
            }
        }
        if(encodeSpaces){
            classPathLine = classPathLine.replaceAll("\\s", "%20");
        }
        return classPathLine;
    }

    private boolean isNeedAddLibsPath(String crcMapPath) {
        if (!(new java.io.File(crcMapPath).exists())) {// when not use cache
            return false;
        }
        return true;
    }

    private String addLibsPath(String line, java.util.Map<String, String> crcMap) {
        for (java.util.Map.Entry<String, String> entry : crcMap.entrySet()) {
            line = adaptLibPaths(line, entry);
        }
        return line;
    }

    private String adaptLibPaths(String line, java.util.Map.Entry<String, String> entry) {
        String jarName = entry.getValue();
        String crc = entry.getKey();
        String libStringFinder = "../lib/" + jarName;
        String libStringFinder2 = "./" + jarName; // for the job jar itself.

        if (line.contains(libStringFinder)) {
            line = line.replace(libStringFinder, "../../../cache/lib/" + crc + "/" + jarName);
        } else if (line.toLowerCase().contains(libStringFinder2)) {
            line = line.toLowerCase().replace(libStringFinder2, "../../../cache/lib/" + crc + "/" + jarName);
        } else if (line.toLowerCase().equals(jarName)) {
            line = "../../../cache/lib/" + crc + "/" + jarName;
        } else if (line.contains(":$ROOT_PATH/" + jarName + ":")) {
            line = line.replace(":$ROOT_PATH/" + jarName + ":", ":$ROOT_PATH/../../../cache/lib/" + crc + "/" + jarName + ":");
        } else if (line.contains(";" + jarName + ";")) {
            line = line.replace(";" + jarName + ";", ";../../../cache/lib/" + crc + "/" + jarName + ";");
        }
        return line;
    }
}
