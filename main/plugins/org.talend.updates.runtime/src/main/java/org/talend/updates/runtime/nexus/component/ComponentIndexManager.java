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
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.updates.runtime.utils.JarMenifestUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexManager {

    public static final String ELEM_COMPONENTS = "components"; //$NON-NLS-1$

    public static final String ELEM_COMPONENT = "component"; //$NON-NLS-1$

    public static final String XPATH_INDEX_COMPONENT = "//" + ELEM_COMPONENTS + '/' + ELEM_COMPONENT; //$NON-NLS-1$

    class MissingSettingException extends IllegalArgumentException {

        private static final long serialVersionUID = -4386085265203515607L;

        public MissingSettingException(String s) {
            super(s);
        }

    }

    /**
     * get the full list of component index bean from the index file.
     */
    public List<ComponentIndexBean> parse(File indexFile) {
        if (indexFile == null || !indexFile.exists() || indexFile.isDirectory()
                || !indexFile.getName().endsWith(FileExtensions.XML_FILE_SUFFIX)) {
            return Collections.emptyList();
        }
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(indexFile);
            return parse(document);
        } catch (DocumentException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        return Collections.emptyList();

    }

    @SuppressWarnings("rawtypes")
    public List<ComponentIndexBean> parse(Document doc) {
        if (doc == null) {
            return Collections.emptyList();
        }
        List<ComponentIndexBean> indexBeans = new ArrayList<ComponentIndexBean>();

        final List componentNodes = doc.selectNodes(XPATH_INDEX_COMPONENT);
        if (componentNodes == null) {
            return Collections.emptyList();
        }
        for (Iterator iter = componentNodes.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();

            ComponentIndexBean indexBean = new ComponentIndexBean();
            // try {
            readAttribute(ComponentIndexNames.name, element, indexBean);
            readAttribute(ComponentIndexNames.bundle_id, element, indexBean);
            readAttribute(ComponentIndexNames.version, element, indexBean);
            readAttribute(ComponentIndexNames.mvn_uri, element, indexBean);
            readAttribute(ComponentIndexNames.product, element, indexBean);
            readAttribute(ComponentIndexNames.license_uri, element, indexBean);

            readChildContent(ComponentIndexNames.description, element, indexBean);
            readChildContent(ComponentIndexNames.license, element, indexBean);
            // } catch (MissingSettingException e) {
            // ExceptionHandler.process(e);
            // }

            if (indexBean.validRequired()) {
                indexBeans.add(indexBean);
            }
        }
        return indexBeans;
    }

    void readAttribute(ComponentIndexNames name, Element element, ComponentIndexBean indexBean) {
        final String value = element.attributeValue(name.getName());
        if (name.isRequired() && StringUtils.isBlank(value)) {
            throw new MissingSettingException("Missing the setting for attribute: " + name);
        }
        if (StringUtils.isNotBlank(value)) {
            indexBean.setValue(name, value);
        }
    }

    void readChildContent(ComponentIndexNames name, Element element, ComponentIndexBean indexBean) {
        final Node node = element.selectSingleNode(name.getName());
        final String value = node != null ? node.getText() : null;
        if (name.isRequired() && StringUtils.isBlank(value)) {
            throw new MissingSettingException("Missing the setting for attribute: " + name);
        }
        if (StringUtils.isNotBlank(value)) {
            indexBean.setValue(name, value);
        }
    }

    /**
     * try to add/update the component index bean in index file.
     * 
     * if same bundleId and version, try to update it. else will add new in index.
     */
    public boolean updateIndexFile(File indexFile, ComponentIndexBean indexBean) {
        if (indexBean == null || indexFile == null || !indexFile.exists() || indexFile.isDirectory()
                || !indexFile.getName().endsWith(FileExtensions.XML_FILE_SUFFIX)) {
            return false;
        }
        try {
            final List<ComponentIndexBean> existedIndexBeans = parse(new SAXReader().read(indexFile));

            List<ComponentIndexBean> newIndexList = new ArrayList<ComponentIndexBean>(existedIndexBeans);

            // if existed, remove the old one
            if (newIndexList.contains(indexBean)) { // same name, buildId, version and mvn_uri
                newIndexList.remove(indexBean);
            }

            // put the new one
            newIndexList.add(indexBean);

            // sort
            Collections.sort(newIndexList, new Comparator<ComponentIndexBean>() {

                @Override
                public int compare(ComponentIndexBean b1, ComponentIndexBean b2) {
                    return b1.toString().compareToIgnoreCase(b2.toString());
                }
            });

            return createIndexFile(indexFile, newIndexList);
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    public boolean createIndexFile(File indexFile, ComponentIndexBean indexBean) {
        if (indexBean == null || indexFile == null) {
            return false;
        }

        final ArrayList<ComponentIndexBean> newIndexList = new ArrayList<ComponentIndexBean>();
        newIndexList.add(indexBean);
        try {
            return createIndexFile(indexFile, newIndexList);
        } catch (IOException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    public boolean createIndexFile(File indexFile, List<ComponentIndexBean> newIndexList) throws IOException {
        if (newIndexList == null || newIndexList.isEmpty() || indexFile == null) {
            return false;
        }

        XMLWriter xmlWriter = null;
        boolean created = false;
        try {
            // write to index
            final DocumentFactory docFactory = DocumentFactory.getInstance();
            final Element components = docFactory.createElement(ELEM_COMPONENTS);
            Document newDoc = docFactory.createDocument(components);
            for (ComponentIndexBean b : newIndexList) {
                final Element elem = createXmlElement(b);
                if (elem != null) {
                    components.add(elem);
                }
            }

            // 4 spaces
            OutputFormat format = new OutputFormat();
            format.setEncoding("UTF-8"); //$NON-NLS-1$
            format.setIndentSize(4);
            format.setNewlines(true);
            xmlWriter = new XMLWriter(new FileOutputStream(indexFile), format);

            xmlWriter.write(newDoc);

            created = true;
            return true;
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    //
                }
            }
            if (!created && indexFile.exists()) {
                indexFile.delete(); // remove the wrong file.
            }
        }
    }

    Element createXmlElement(ComponentIndexBean indexBean) {

        if (indexBean == null) {
            return null;
        }
        if (!indexBean.validRequired()) {
            return null; // no valid
        }
        final DocumentFactory docFactory = DocumentFactory.getInstance();
        final Element component = docFactory.createElement(ComponentIndexManager.ELEM_COMPONENT);
        for (ComponentIndexNames in : ComponentIndexNames.values()) {
            final String value = indexBean.getValue(in);
            if (StringUtils.isBlank(value)) {
                continue; // not value
            }
            switch (in) {
            case name:
            case bundle_id:
            case version:
            case mvn_uri:
            case license_uri:
            case product:
            default:
                // attribute
                component.add(docFactory.createAttribute(component, in.getName(), value));
                break;
            case description:
            case license:
                // child element
                final Element child = docFactory.createElement(in.getName());
                child.setText(value);
                component.add(child);
                break;
            }
        }
        return component;
    }

    /**
     * 
     * create one default index bean which based one the component zip file directly.
     * 
     * bundleId, version, mvn_uri are required
     */
    public ComponentIndexBean create(File componentZipFile) {
        if (componentZipFile == null || !componentZipFile.exists() || componentZipFile.isDirectory()
                || !componentZipFile.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
            return null;
        }

        String name = null;
        String bundleId = null;
        String bundleVersion = null;
        String mvnUri = null;

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(componentZipFile);

            Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries();
            while (enumeration.hasMoreElements()) {
                final ZipEntry zipEntry = enumeration.nextElement();
                String path = zipEntry.getName();
                if (path.endsWith(FileExtensions.JAR_FILE_SUFFIX)) { // is jar
                    // if it's bundle, not from other folder, like lib, m2 repository.
                    IPath p = new Path(path);
                    // must be in plugins
                    if (p.segmentCount() > 1 && p.removeLastSegments(1).lastSegment().equals(UpdatesHelper.FOLDER_PLUGINS)) {
                        if (UpdatesHelper.isComponentJar(zipFile.getInputStream(zipEntry))) {
                            JarInputStream jarEntryStream = null;
                            try {
                                // must use another stream
                                jarEntryStream = new JarInputStream(zipFile.getInputStream(zipEntry));

                                // find the bundleId and version
                                final Manifest manifest = jarEntryStream.getManifest();
                                if (manifest != null) {
                                    bundleId = JarMenifestUtil.getBundleSymbolicName(manifest);
                                    bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
                                }

                                // find the pom.properties
                                JarEntry jarEntry = null;
                                while ((jarEntry = jarEntryStream.getNextJarEntry()) != null) {
                                    final String entryPath = jarEntry.getName();
                                    final Path fullPath = new Path(entryPath);
                                    final String fileName = fullPath.lastSegment();

                                    /*
                                     * for example,
                                     * META-INF/maven/org.talend.components/components-splunk/pom.properties
                                     */
                                    if (fileName.equals("pom.properties") //$NON-NLS-1$
                                            && entryPath.contains("META-INF/maven/")) { //$NON-NLS-1$

                                        // FIXME, didn't find one way to read the inner jar
                                        // final InputStream propStream = jarFile.getInputStream(jarEntry);
                                        // if (propStream != null) {
                                        // Properties pomProp = new Properties();
                                        // pomProp.load(propStream);
                                        //
                                        // String version = pomProp.getProperty("version"); //$NON-NLS-1$
                                        // String groupId = pomProp.getProperty("groupId"); //$NON-NLS-1$
                                        // String artifactId = pomProp.getProperty("artifactId"); //$NON-NLS-1$
                                        // mvnUri = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version,
                                        // FileExtensions.ZIP_FILE_SUFFIX, null);
                                        //
                                        // propStream.close();
                                        // }

                                        // FIXME, try the path way
                                        // META-INF/maven/org.talend.components/components-splunk
                                        IPath tmpMavenPath = fullPath.removeLastSegments(1);
                                        String artifactId = tmpMavenPath.lastSegment(); // components-splunk
                                        // META-INF/maven/org.talend.components
                                        tmpMavenPath = tmpMavenPath.removeLastSegments(1);
                                        String groupId = tmpMavenPath.lastSegment(); // org.talend.components

                                        mvnUri = MavenUrlHelper.generateMvnUrl(groupId, artifactId, bundleVersion,
                                                FileExtensions.ZIP_EXTENSION, null);

                                    } else
                                    /*
                                     * /OSGI-INF/installer$$splunk.xml
                                     */
                                    if (fileName.endsWith(FileExtensions.XML_FILE_SUFFIX)
                                            && fileName.startsWith(UpdatesHelper.NEW_COMPONENT_PREFIX)
                                            && entryPath.contains(UpdatesHelper.FOLDER_OSGI_INF + '/')) {
                                        name = fullPath.removeFileExtension().lastSegment();
                                        name = name.substring(name.indexOf(UpdatesHelper.NEW_COMPONENT_PREFIX)
                                                + UpdatesHelper.NEW_COMPONENT_PREFIX.length());
                                    }
                                }
                            } catch (IOException e) {
                                //
                            } finally {
                                try {
                                    if (jarEntryStream != null) {
                                        jarEntryStream.close();
                                    }
                                } catch (IOException e) {
                                    //
                                }
                            }

                        }
                    }
                }
            }

        } catch (ZipException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        } catch (IOException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        // set the required
        if (name != null && bundleId != null && bundleVersion != null && mvnUri != null) {
            final ComponentIndexBean indexBean = new ComponentIndexBean();
            final boolean set = indexBean.setRequiredFieldsValue(name, bundleId, bundleVersion, mvnUri);
            if (set) {
                return indexBean;
            }
        }
        return null;
    }

}
