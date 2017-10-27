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
package org.talend.metadata.managment.ui.wizard.metadata.xml.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.repository.ProjectManager;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DOC hwang class global comment. Detailled comment <br/>
 * 
 */
public class CopyDeleteFileUtilForWizard {

    public static String copyToTemp(String oldFile) throws PersistenceException {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return oldFile;
        }
        String temPath = fsProject.getLocationURI().getPath() + File.separator + "temp" + File.separator + "wizard"
                + File.separator;

        String newFile;
        try {
            newFile = copyNeededFiles(oldFile, temPath);
            getImportFiles(oldFile, temPath, new HashSet<String>());
        } catch (IOException e) {
            throw new PersistenceException(e);
        } catch (URISyntaxException e) {
            throw new PersistenceException(e);
        }

        return newFile;
    }

    private static String copyNeededFiles(String fileName, String newPath) throws IOException, URISyntaxException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URI uri = null;
        File f = new File(fileName);
        if (f.exists()) {
            uri = f.toURI();
        } else {
            URL url = new URL(fileName);
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }

        // Then try to parse the input string as a url in web.
        if (uri == null) {
            uri = new URI(fileName);
        }

        File newFile = new File(newPath + f.getName());
        FilesUtils.copyFile(f, newFile);

        if (!fileName.toUpperCase().endsWith(".XSD")) {
            return newFile.getAbsolutePath();
        }
        // XMLSchemaLoader xsLoader = new XMLSchemaLoader();
        // XSModel xsModel = xsLoader.loadURI(uri.toString());
        // for (int i = 0; i < xsModel.getNamespaceItems().getLength(); i++) {
        // XSNamespaceItem item = xsModel.getNamespaceItems().item(i);
        // for (int j = 0; j < item.getDocumentLocations().getLength(); j++) {
        // String location = item.getDocumentLocations().item(j);
        // URL url = new URL(location);
        // if (!fileName.equals(url.getFile())) {
        // copyNeededFiles(url.getFile(), newPath);
        // }
        // }
        // }

        return newFile.getAbsolutePath();
    }

    public static List<String> getComplexNodes(String xsdFile) {
        List<String> attri = new ArrayList<String>();
        File file = new File(xsdFile);
        if (!file.exists()) {
            return attri;
        }

        SAXReader saxReader = new SAXReader();
        Document doc;
        try {
            URL url = file.toURI().toURL();
            saxReader.setFeature("http://xml.org/sax/features/validation", false);

            saxReader.setEntityResolver(new EntityResolver() {

                String emptyDtd = "";

                ByteArrayInputStream bytes = new ByteArrayInputStream(emptyDtd.getBytes());

                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    File file = new File(systemId);
                    if (file.exists()) {
                        return new InputSource(new FileInputStream(file));
                    }
                    // if no file, just set empty content for dtd
                    return new InputSource(bytes);
                }
            });

            doc = saxReader.read(url.getFile());
            Element root = doc.getRootElement();
            List<Element> complexList = root.elements("complexType");
            if (complexList == null) {
                return attri;
            }
            for (Element n : complexList) {
                Attribute attr = n.attribute("name");
                if (attr != null) {
                    attri.add(attr.getValue());
                }
            }
        } catch (DocumentException e) {
            ExceptionHandler.process(e);
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        } catch (SAXException e) {
            ExceptionHandler.process(e);
        }
        return attri;
    }

    private static void getImportFiles(String xsdFile, String newPath, Set<String> filePaths) {
        File file = new File(xsdFile);
        if (!file.exists()) {
            return;
        }

        String xsdFolder = file.getParent();
        SAXReader saxReader = new SAXReader();
        Document doc;
        try {
            URL url = file.toURI().toURL();
            doc = saxReader.read(url.getFile());
            Element root = doc.getRootElement();
            List<Element> elementsList = new ArrayList<Element>();
            List<Element> importList = root.elements("import");
            if (importList != null) {
                elementsList.addAll(importList);
            }
            List<Element> includeList = root.elements("include");
            if (includeList != null) {
                elementsList.addAll(includeList);
            }
            if (elementsList.size() <= 0) {
                return;
            }
            for (Element n : elementsList) {
                Attribute attr = n.attribute("schemaLocation");
                if (attr != null) {
                    String importFile = xsdFolder + File.separator + attr.getValue();
                    File f = new File(importFile);
                    if (f.exists()) {
                        File newFile = new File(newPath + attr.getValue());
                        if (!filePaths.contains(newFile.getCanonicalPath())) {
                            filePaths.add(newFile.getCanonicalPath());
                            FilesUtils.copyFile(f, newFile);
                            getImportFiles(importFile, newFile.getParent() + File.separator, filePaths);
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            ExceptionHandler.process(e);
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public static void deleteWizardTempFiles() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject fsProject = null;
        try {
            fsProject = ResourceUtils.getProject(project);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }
        if (fsProject == null) {
            return;
        }
        String tempPath = fsProject.getLocationURI().getPath() + File.separator + "temp" + File.separator + "wizard";
        File tempWizardDir = new File(tempPath);
        tempWizardDir.delete();
    }
}
