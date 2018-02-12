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
package org.talend.core.hadoop.version.custom;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.runtime.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * created by wchen on 2013-3-27 Detailled comment
 * 
 */
public class HadoopCustomLibrariesUtil {

    private ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
            .getService(ILibraryManagerService.class);

    private CustomVersionLibsManager libsManager;

    public static final String[] FILE__MASK = { "*.zip" }; //$NON-NLS-1$ 

    /**
     * DOC wchen HadoopCustomLibrariesUtil constructor comment.
     */
    public HadoopCustomLibrariesUtil() {
        libsManager = CustomVersionLibsManager.getInstance();
    }

    public void exportLibZipFile(String targetFile, Map<String, Set<LibraryFile>> LibMap) {
        File tmpFolder = null;
        ZipOutputStream outputStream = null;
        Set<LibraryFile> jarNotFound = new HashSet<LibraryFile>();
        try {
            Map<File, IPath> toExport = new HashMap<File, IPath>();
            tmpFolder = createTempFolder();
            File libFolder = new File(tmpFolder, "lib");//$NON-NLS-1$ 
            libFolder.mkdir();
            // put all needed jars to tempFolder/lib
            for (String group : LibMap.keySet()) {
                for (LibraryFile libFile : LibMap.get(group)) {
                    String jarName = libFile.getName();
                    File jarToStore = new File(libFolder, jarName);
                    boolean retreived = librairesManagerService.retrieve(jarName, libFolder.getAbsolutePath());
                    if (!retreived) {
                        jarNotFound.add(libFile);
                    } else {
                        toExport.put(jarToStore, new Path("lib/" + jarName));//$NON-NLS-1$ 
                    }
                }
            }
            if (!jarNotFound.isEmpty()) {
                String msg = "";//$NON-NLS-1$ 
                for (LibraryFile libFile : jarNotFound) {
                    msg += libFile.getName() + ",";//$NON-NLS-1$ 
                }
                msg = msg.substring(0, msg.length() - 1);
                String message = Messages.getString("HadoopCustomLibrariesUtil.libMissing", msg); //$NON-NLS-1$ 

                boolean openConfirm = MessageDialog.openConfirm(null,
                        Messages.getString("HadoopCustomLibrariesUtil.importConfirmMsg"), message);//$NON-NLS-1$ 
                if (!openConfirm) {
                    return;
                }
            }

            // create index file , only generate infor for jars needed
            createIndexFile(tmpFolder, LibMap, jarNotFound, toExport);

            // build the zip file
            outputStream = new ZipOutputStream(new FileOutputStream(targetFile));

            for (File file : toExport.keySet()) {
                IPath path = toExport.get(file);
                ZipEntry newEntry = new ZipEntry(path.toString());
                byte[] readBuffer = new byte[4096];
                outputStream.putNextEntry(newEntry);
                InputStream contentStream = new FileInputStream(file);
                int n;
                while ((n = contentStream.read(readBuffer)) > 0) {
                    outputStream.write(readBuffer, 0, n);
                }

                outputStream.closeEntry();
            }
        } catch (Exception e) {
            showError(Messages.getString("HadoopCustomLibrariesUtil.exmportFailed"), e);//$NON-NLS-1$
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    showError(Messages.getString("HadoopCustomLibrariesUtil.exmportFailed"), e);//$NON-NLS-1$
                }
            }
            if (tmpFolder != null) {
                deleteTmpDirectory(tmpFolder);
            }
        }
    }

    private void deleteTmpDirectory(File tmpDirectory) {
        if (tmpDirectory.isFile()) {
            tmpDirectory.delete();
        } else {
            for (File file : tmpDirectory.listFiles()) {
                deleteTmpDirectory(file);
            }
            tmpDirectory.delete();
        }
    }

    private File createTempFolder() throws IOException, PersistenceException {
        File tmpDirectory = null;
        int suffix = 0;
        org.talend.core.model.general.Project project = ProjectManager.getInstance().getCurrentProject();
        IProject physProject;
        String tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$

        physProject = ResourceUtils.getProject(project.getTechnicalLabel());
        tmpFolder = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$

        while (tmpDirectory == null || tmpDirectory.exists()) {
            tmpDirectory = new File(tmpFolder + File.separatorChar + "hadoopCustomLib" + suffix); //$NON-NLS-1$
            suffix++;
        }

        if (!tmpDirectory.mkdir()) {
            throw new IOException(Messages.getString("HadoopCustomLibrariesUtil.cannotCreate", tmpDirectory)); //$NON-NLS-1$
        }
        return tmpDirectory;
    }

    private void createIndexFile(File tempFolder, Map<String, Set<LibraryFile>> LibMap, Set<LibraryFile> jarNotFound,
            Map<File, IPath> toExport) throws Exception {
        OutputStreamWriter output = null;
        try {
            final DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            String indexFileName = "index.xml";//$NON-NLS-1$
            File indexFile = new File(tempFolder, indexFileName);
            final DocumentBuilder analyseur = docBuilder.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            Document document = analyseur.newDocument();
            Element customLibs = document.createElement("customLibs"); //$NON-NLS-1$
            document.appendChild(customLibs);

            for (String group : LibMap.keySet()) {
                Element libGroup = document.createElement(group);
                customLibs.appendChild(libGroup);
                Set<LibraryFile> libs = LibMap.get(group);
                for (LibraryFile lib : libs) {
                    if (jarNotFound.contains(lib)) {
                        continue;
                    }
                    Element jarElement = document.createElement("jar");//$NON-NLS-1$
                    // description
                    Attr description = document.createAttribute("description");//$NON-NLS-1$
                    description.setNodeValue(lib.getDesc());
                    jarElement.setAttributeNode(description);
                    // name
                    Attr nameElement = document.createAttribute("name");//$NON-NLS-1$
                    nameElement.setNodeValue(lib.getName());
                    jarElement.setAttributeNode(nameElement);
                    libGroup.appendChild(jarElement);
                }
            }

            // save index
            if (document != null) {
                XMLSerializer serializer = new XMLSerializer();
                OutputFormat outputFormat = new OutputFormat();
                outputFormat.setIndenting(true);
                serializer.setOutputFormat(outputFormat);

                output = new OutputStreamWriter(new FileOutputStream(indexFile), "UTF-8"); //$NON-NLS-1$
                serializer.setOutputCharStream(output);
                serializer.serialize(document);
                toExport.put(indexFile.getAbsoluteFile(), new Path(indexFileName));
            }
        } finally {
            if (output != null) {
                output.close();
            }
        }

    }

    public Map<ECustomVersionGroup, Set<LibraryFile>> readZipFile(String filePath, Set<ECustomVersionGroup> groups) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        File tempFile = null;
        try {
            ILibrariesService service = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                service = (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
            }

            if (service == null) {
                throw new Exception("Can not deploy jars");
            }
            tempFile = createTempFolder();
            Collection<URI> collection = new HashSet<URI>();

            ZipFile zipFile = new ZipFile(file);
            InputStream indexFileInputStream = null;
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                Path path = new Path(entry.getName());
                if (path.lastSegment().endsWith("index.xml")) {//$NON-NLS-1$
                    indexFileInputStream = zipFile.getInputStream(entry);
                } else if (path.lastSegment().endsWith(".jar")) {//$NON-NLS-1$
                    deployJarToDesForArchive(zipFile, entry, tempFile, collection);
                }
            }

            final DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();

            final DocumentBuilder analyseur = docBuilder.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            final Document document = analyseur.parse(indexFileInputStream);
            Map<ECustomVersionGroup, Set<LibraryFile>> libsMap = new HashMap<ECustomVersionGroup, Set<LibraryFile>>();
            for (ECustomVersionGroup group : groups) {
                Set<LibraryFile> libsInGroup = new HashSet<LibraryFile>();
                NodeList groupElement = document.getElementsByTagName(group.getName());
                if (groupElement != null) {
                    for (int i = 0; i < groupElement.getLength(); i++) {
                        Node groupItem = groupElement.item(i);
                        NodeList jarNodes = groupItem.getChildNodes();
                        if (jarNodes != null) {
                            for (int j = 0; j < jarNodes.getLength(); j++) {
                                LibraryFile libFile = new LibraryFile();
                                Node jarNode = jarNodes.item(j);
                                NamedNodeMap attributes = jarNode.getAttributes();
                                if (attributes != null) {
                                    Node description = attributes.getNamedItem("description");//$NON-NLS-1$
                                    if (description != null) {
                                        libFile.setDesc(description.getNodeValue());
                                    }

                                    Node namedItem = attributes.getNamedItem("name");//$NON-NLS-1$
                                    if (namedItem != null) {
                                        libFile.setName(namedItem.getNodeValue());
                                    }
                                    libsInGroup.add(libFile);
                                }
                            }
                        }
                        // libsInGroup.add(item.getNodeValue());
                    }
                }
                libsMap.put(group, libsInGroup);
            }

            // deploy
            File[] listFiles = tempFile.listFiles();
            URL[] listUrl = new URL[listFiles.length];
            for (int i = 0; i < listFiles.length; i++) {
                listUrl[i] = listFiles[i].toURI().toURL();
            }
            service.deployLibrarys(listUrl);

            return libsMap;
        } catch (Exception e) {
            showError(Messages.getString("HadoopCustomLibrariesUtil.importFailed"), e);//$NON-NLS-1$
        } finally {
            if (tempFile != null) {
                deleteTmpDirectory(tempFile);
            }
        }
        return null;

    }

    private void deployJarToDesForArchive(ZipFile zipFile, ZipEntry zipEntry, File dirFile, Collection collection)
            throws IOException {

        IPath tmpDir = new Path(dirFile.getAbsolutePath());

        IPath path = new Path(zipEntry.getName());
        String fileName = path.lastSegment();
        InputStream is = zipFile.getInputStream(zipEntry);
        File temFile = tmpDir.append(fileName).toFile();
        if (temFile.exists()) {
            temFile.delete();
        }
        byte[] b = new byte[1024];
        int length = 0;
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(temFile, true));
        while ((length = is.read(b)) != -1) {
            fos.write(b, 0, length);
        }
        fos.close();
        //
        collection.add(temFile.toURI());

    }

    public Set<LibraryFile> convertToLibraryFile(Collection<String> libNameSet) {
        Set<LibraryFile> libraryFiles = new HashSet<LibraryFile>();
        for (String libName : libNameSet) {
            libraryFiles.add(convertToLibraryFile(libName));
        }

        return libraryFiles;
    }

    public Set<LibraryFile> convertToLibraryFile(Object[] libNameArray) {
        Set<LibraryFile> libraryFiles = new HashSet<LibraryFile>();
        for (Object obj : libNameArray) {
            if (obj instanceof String) {
                libraryFiles.add(convertToLibraryFile((String) obj));
            }
        }

        return libraryFiles;
    }

    public LibraryFile convertToLibraryFile(String libName) {
        LibraryFile libraryFile = new LibraryFile();
        libraryFile.setName(libName);
        libraryFile.setDesc(libsManager.getLibDescription(libName));

        return libraryFile;
    }

    public Set<String> convertToLibName(Set<LibraryFile> LibraryFileSet) {
        Set<String> libNames = new HashSet<String>();
        for (LibraryFile libraryFile : LibraryFileSet) {
            libNames.add(libraryFile.getName());
        }

        return libNames;
    }

    private void showError(String shortMessage, Exception e) {
        StringBuffer sb = new StringBuffer();
        sb.append(e.toString());
        sb.append("\n");
        if (e.getStackTrace() != null) {
            for (StackTraceElement trace : e.getStackTrace()) {
                sb.append(trace.toString());
                sb.append("\n");
            }
        }
        String detailedMessage = sb.toString();
        ErrorDialogWidthDetailArea dialog = new ErrorDialogWidthDetailArea(null, "test", shortMessage, detailedMessage,
                IStatus.ERROR);
    }

}
