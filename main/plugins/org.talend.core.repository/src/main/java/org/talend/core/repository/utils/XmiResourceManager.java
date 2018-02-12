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
package org.talend.core.repository.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.ReferenceFileItem;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.ValidationRulesConnectionItem;
import org.talend.core.model.properties.helper.ByteArrayResource;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.utils.ResourceFilenameHelper.FileName;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: XmiResourceManager.java 44692 2010-06-30 04:29:32Z nrousseau $
 * 
 */
public class XmiResourceManager {

    // PTODO mhelleboid should use a custom ResourceFactory
    // PTODO mhelleboid test duplicate resourcesUri in resourceSet !

    public TalendResourceSet resourceSet = new TalendResourceSet();

    private Map<URI, Resource> resourcesMap = new HashMap<URI, Resource>();

    private boolean useOldProjectFile;

    private boolean avoidUnloadResource;

    public XmiResourceManager() {
        setUseOldProjectFile(false);
        resetResourceSet();
    }

    public void resetResourceSet() {
        resourceSet = new TalendResourceSet();
        resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
        resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
        resourcesMap.clear();
        resourceSet.setURIResourceMap(resourcesMap);

    }

    public Project loadProject(IProject project) throws PersistenceException {
        URI uri = getProjectResourceUri(project);
        if (!isAvoidUnloadResource()) {
            unloadResource(uri.toString());
        }

        Resource resource = getResourceSet().getResource(uri, true);
        Project emfProject = (Project) EcoreUtil
                .getObjectByType(resource.getContents(), PropertiesPackage.eINSTANCE.getProject());
        emfProject.eResource().setTrackingModification(true);

        return emfProject;
    }

    public boolean hasTalendProjectFile(IProject project) {
        URI uri = getProjectResourceUri(project);
        try {
            if (!project.isSynchronized(IResource.DEPTH_ONE)) {
                project.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        IPath path = URIHelper.convert(uri);
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        return file.exists();
    }

    public void loadScreenshots(Property property, ProcessType processType) {
        Resource screenshotResource = getScreenshotResource(property.getItem());
        if (screenshotResource != null) { // if no screenshot, we will try to add later when save
            processType.getScreenshots().addAll(screenshotResource.getContents());
        }
    }

    public Property loadProperty(IResource iResource) {
        final Map<Object, Object> oldLoadOptions = new HashMap<Object, Object>(resourceSet.getLoadOptions());
        try {
            return doLoadProperty(iResource);
        } finally {
            resourceSet.getLoadOptions().clear();
            resourceSet.getLoadOptions().putAll(oldLoadOptions);
        }
    }

    private Property doLoadProperty(IResource iResource) {

        Property property = null;
        // force unload old version, or the UI won't be synchronized all the time to the current file.
        // this is only if a user update itself a .item or .properties, or for SVN repository.
        //
        URIConverter theURIConverter = resourceSet.getURIConverter();
        URI propertyUri = URIHelper.convert(iResource.getFullPath());
        URI itemResourceURI = theURIConverter.normalize(getItemResourceURI(propertyUri));
        URI screenshotResourceURI = theURIConverter.normalize(getScreenshotResourceURI(itemResourceURI));
        List<Resource> resources = resourceSet.getResources();
        synchronized (resources) {
            for (Resource res : new ArrayList<Resource>(resources)) {
                if (res != null) {
                    URI normalizedURI = theURIConverter.normalize(res.getURI());
                    if (propertyUri.equals(normalizedURI)) {
                        res.unload();
                        resourceSet.getResources().remove(res);
                    }
                    if (itemResourceURI.equals(normalizedURI)) {
                        res.unload();
                        resourceSet.getResources().remove(res);
                    }
                    if (screenshotResourceURI.equals(normalizedURI)) {
                        res.unload();
                        resourceSet.getResources().remove(res);
                    }
                }
            }
        }
        Resource propertyResource = resourceSet.getResource(propertyUri, true);

        property = (Property) EcoreUtil
                .getObjectByType(propertyResource.getContents(), PropertiesPackage.eINSTANCE.getProperty());
        return property;
    }

    public Property forceReloadProperty(Property property) {
        URI propertyURI = property.eResource().getURI();
        unloadResources(property);
        Resource propertyResource = getResourceSet().getResource(propertyURI, true);
        return (Property) EcoreUtil.getObjectByType(propertyResource.getContents(), PropertiesPackage.eINSTANCE.getProperty());
    }

    /**
     * 
     * DOC klliu Comment method "getFolderPath".
     * 
     * @param project
     * @param repositoryObjectType
     * @param relativePath
     * @return
     * @throws PersistenceException
     */
    private IPath getFolderPath(IProject project, ERepositoryObjectType repositoryObjectType, IPath relativePath)
            throws PersistenceException {
        ERepositoryObjectType type = repositoryObjectType;
        if (ERepositoryObjectType.TDQ_SYSTEM_INDICATORS.equals(repositoryObjectType)
                || ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS.equals(repositoryObjectType)) {
            type = ERepositoryObjectType.TDQ_INDICATOR_ELEMENT;
        } else if (ERepositoryObjectType.TDQ_PATTERN_REGEX.equals(repositoryObjectType)
                || ERepositoryObjectType.TDQ_PATTERN_SQL.equals(repositoryObjectType)) {
            type = ERepositoryObjectType.TDQ_PATTERN_ELEMENT;
        }
        IFolder folder = project.getFolder(ERepositoryObjectType.getFolderName(type)).getFolder(relativePath);
        return folder.getFullPath();
    }

    public Resource createProjectResource(IProject project) {
        URI uri = getProjectResourceUri(project);
        return getResourceSet().createResource(uri);
    }

    public Resource createTempProjectResource() {
        URI uri = null;
        try {
            uri = URI.createPlatformResourceURI(Platform.getInstallLocation().getURL().getFile(), true);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return getResourceSet().createResource(uri);
    }

    private URI getProjectResourceUri(IProject project) {
        URI uri = URIHelper.convert(project.getFullPath().append(getProjectFilename()));
        return uri;
    }

    public Resource createPropertyResource(Item item, Resource itemResource) {
        URI itemUri = itemResource.getURI();
        URI propertyResourceURI = itemUri.trimFileExtension();
        if (item.isNeedVersion()) {
            propertyResourceURI = propertyResourceURI.appendFileExtension(FileConstants.PROPERTIES_EXTENSION);
        } else {
            IPath converted = URIHelper.convert(propertyResourceURI);
            IPath propertyPath = new Path(converted.toPortableString() + '_' + item.getProperty().getVersion());
            propertyResourceURI = URIHelper.convert(propertyPath);
            propertyResourceURI = propertyResourceURI.appendFileExtension(FileConstants.PROPERTIES_EXTENSION);
        }
        // URI propertyResourceURI = getPropertyResourceURI(uri);
        Resource propResource = getResourceSet().getResource(propertyResourceURI, false);
        if (propResource != null) {
            propResource.unload();
            getResourceSet().getResources().remove(propResource);
        }
        return getResourceSet().createResource(propertyResourceURI);
    }

    public Resource getReferenceFileResource(Resource itemResource, ReferenceFileItem refFile, boolean needLoad) {
        URI referenceFileURI = getReferenceFileURI(itemResource.getURI(), refFile);
        URIConverter converter = getResourceSet().getURIConverter();
        Resource referenceResource = new ByteArrayResource(referenceFileURI);
        InputStream inputStream = null;
        List<Resource> resources = new ArrayList<Resource>(getResourceSet().getResources());
        // in case ESB load reference file from the physcial file,but DI need reference from the EMF,so add this
        // flag
        if (refFile.isReloadFromFile()) {
            for (Resource res : resources) {
                if (res != null && referenceFileURI.toString().equals(res.getURI().toString())) {
                    res.unload();
                    getResourceSet().getResources().remove(res);
                }
            }

            getResourceSet().getResources().add(referenceResource);
            try {
                if (needLoad) {
                    inputStream = converter.createInputStream(referenceFileURI);
                    referenceResource.load(inputStream, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        } else {
            referenceResource = getResourceSet().getResource(referenceFileURI, true);
        }
        return referenceResource;
    }

    private URI getReferenceFileURI(URI itemResourceURI, ReferenceFileItem refFile) {
        if (refFile.getName() != null) {
            return itemResourceURI.trimSegments(1).appendSegment(refFile.getName() + "." + refFile.getExtension()); //$NON-NLS-1$
        } else {
            return itemResourceURI.trimFileExtension().appendFileExtension(refFile.getExtension());
        }
    }

    public Resource createItemResource(IProject project, Item item, IPath path, ERepositoryObjectType repositoryObjectType,
            boolean byteArrayResource) throws PersistenceException {
        URI itemResourceURI = getItemResourceURI(project, repositoryObjectType, path, item);

        Resource itemResource = createItemResource(byteArrayResource, itemResourceURI);

        return itemResource;
    }

    public Resource createScreenshotResource(IProject project, Item item, IPath path, ERepositoryObjectType repositoryObjectType,
            boolean byteArrayResource) throws PersistenceException {
        URI itemResourceURI = getScreenshotResourceURI(project, repositoryObjectType, path, item);

        Resource itemResource = createItemResource(byteArrayResource, itemResourceURI);

        return itemResource;
    }

    // MOD mzhao 2010-11-22, suppport TDQ item file extensions.(.ana, .rep, etc)
    /**
     * Please use item.setFileExtension directly. When create the extension, it will add automatically the extension
     * point needed.
     */
    @Deprecated
    public Resource createItemResourceWithExtension(IProject project, Item item, IPath path,
            ERepositoryObjectType repositoryObjectType, boolean byteArrayResource, String fileExtension)
            throws PersistenceException {
        URI itemResourceURI = getItemResourceURI(project, repositoryObjectType, path, item, fileExtension);

        Resource itemResource = createItemResource(byteArrayResource, itemResourceURI);

        return itemResource;
    }

    private Resource createItemResource(boolean byteArrayResource, URI itemResourceURI) {
        Resource itemResource;
        itemResource = getResourceSet().getResource(itemResourceURI, false);
        if (itemResource != null) {
            itemResource.unload();
            getResourceSet().getResources().remove(itemResource);
        }
        if (byteArrayResource) {
            itemResource = new ByteArrayResource(itemResourceURI);
            getResourceSet().getResources().add(itemResource);
        } else {
            itemResource = getResourceSet().createResource(itemResourceURI);
        }
        return itemResource;
    }

    public void deleteResource(Resource resource) throws PersistenceException {
        ResourceUtils.deleteFile(URIHelper.getFile(resource.getURI()));
        resourcesMap.remove(resource.getURI());
        getResourceSet().getResources().remove(resource);
    }

    public void deleteLogiclResource(Resource resource) throws PersistenceException {
        ResourceUtils.deleteRevisionFile(URIHelper.getFile(resource.getURI()));
        resourcesMap.remove(resource.getURI());
        getResourceSet().getResources().remove(resource);
    }

    public Resource getItemResource(Item item) {
        return getItemResource(item, true);
    }

    public Resource getItemResource(ResourceSet resourceSet, Item item) {
        return getItemResource(resourceSet, item, true);
    }

    public Resource getItemResource(Item item, boolean forceLoad) {
        return getItemResource(getResourceSet(), item, forceLoad);
    }

    public Resource getItemResource(ResourceSet resourceSet, Item item, boolean forceLoad) {
        if (item == null) {
            return null;
        }
        URI itemResourceURI = null;
        if (item.getFileExtension() != null) {
            itemResourceURI = getItemResourceURI(getItemURI(item), item.getFileExtension());
        } else if (item instanceof TDQItem) {
            IPath fileName = new Path(((TDQItem) item).getFilename());
            itemResourceURI = getItemResourceURI(getItemURI(item), fileName.getFileExtension());
        } else {
            itemResourceURI = getItemResourceURI(getItemURI(item));
        }
        Resource itemResource = resourceSet.getResource(itemResourceURI, false);
        if (forceLoad && itemResource == null) {
            if (item instanceof FileItem) {
                itemResource = new ByteArrayResource(itemResourceURI);
                resourceSet.getResources().add(itemResource);
            }
            itemResource = resourceSet.getResource(itemResourceURI, true);
        }
        return itemResource;
    }

    public Resource getScreenshotResource(Item item) {
        return getScreenshotResource(item, false);
    }

    /*
     * Get a resource obj from Item resource file. if the resouce file does not exist ,will create it first.
     */
    public Resource getScreenshotResource(Item item, boolean createIfNotExist) {
        return getScreenshotResource(getResourceSet(), item, createIfNotExist, false);
    }

    public Resource getScreenshotResource(Item item, boolean createIfNotExist, boolean forceReload) {
        return getScreenshotResource(getResourceSet(), item, createIfNotExist, forceReload);
    }

    public Resource getScreenshotResource(ResourceSet resourceSet, Item item, boolean createIfNotExist, boolean forceReload) {
        URI itemResourceURI = null;
        itemResourceURI = getScreenshotResourceURI(getItemURI(item));
        boolean fileExist = false;
        if (itemResourceURI.isFile()) {
            fileExist = new File(itemResourceURI.toFileString()).exists();
        } else {
            IPath path = URIHelper.convert(itemResourceURI);
            if (path != null) {
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
                if (file.exists()) {
                    fileExist = true;
                }
            } else {
                fileExist = false;
            }
        }
        Resource itemResource = null;
        if (fileExist) {
            List<Resource> resources = new ArrayList<Resource>(getResourceSet().getResources());
            if (forceReload) {
                for (Resource res : resources) {
                    if (res != null) {
                        if (itemResourceURI.toString().equals(res.getURI().toString())) {
                            res.unload();
                            getResourceSet().getResources().remove(res);
                            break;
                        }
                    }
                }
            }
            try {
                // judge whether the physical file exists or not
                itemResource = getResourceSet().getResource(itemResourceURI, true);
            } catch (Exception e) {
                // do nothing, consider the file don't exist
                itemResource = null;
            }
        }
        if (itemResource == null && createIfNotExist) {
            itemResource = getResourceSet().createResource(itemResourceURI);
        }
        return itemResource;
    }

    private URI getItemURI(Item item) {
        ProjectManager pManager = ProjectManager.getInstance();
        org.talend.core.model.general.Project project = new org.talend.core.model.general.Project(pManager.getProject(item));
        // referenced item
        if (project != null && !project.equals(pManager.getCurrentProject())) {
            String folder = null;
            if (item instanceof JobDocumentationItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.JOB_DOC);
            } else if (item instanceof JobletDocumentationItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.JOBLET_DOC);
            } else if (item instanceof DatabaseConnectionItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_CONNECTIONS);
            } else if (item instanceof DelimitedFileConnectionItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_FILE_DELIMITED);
            } else if (item instanceof EbcdicConnectionItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_FILE_EBCDIC);
            } else if (item instanceof PositionalFileConnectionItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
            } else if (item instanceof ValidationRulesConnectionItem) {
                folder = ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_VALIDATION_RULES);
            }

            if (folder != null) {
                IPath path = new Path(project.getTechnicalLabel());
                path = path.append(folder);
                path = path.append(item.getState().getPath());
                Property property = item.getProperty();
                String version = "";
                if (item.isNeedVersion()) {
                    version = "_" + property.getVersion();
                }
                String itemStr = property.getLabel() + version + "." + FileConstants.PROPERTIES_EXTENSION; //$NON-NLS-1$ 
                path = path.append(itemStr);
                return URIHelper.convert(path);
            }
        } else if (!item.isNeedVersion()) {
            IPath fullPath = null;
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            if (itemType != null && project != null && project.equals(pManager.getCurrentProject())) {
                fullPath = new Path(project.getTechnicalLabel());
                fullPath = fullPath.append(itemType.getFolder());
                fullPath = fullPath.append(item.getState().getPath());
                Property property = item.getProperty();
                String version = "";
                if (item.isNeedVersion()) {
                    version = "_" + property.getVersion();
                }
                String itemStr = property.getLabel() + version + "." + FileConstants.PROPERTIES_EXTENSION; //$NON-NLS-1$ 
                fullPath = fullPath.append(itemStr);
            } else {
                // mormally this should not happen
                Property property = item.getProperty();
                URI uri = property.eResource().getURI();
                fullPath = URIHelper.convert(uri);
                if (fullPath == null) {
                    // some uri might be start with file:
                    fullPath = new Path(uri.devicePath());
                }
                fullPath = fullPath.removeLastSegments(1);
                fullPath = fullPath.append(property.getLabel()).addFileExtension(FileConstants.PROPERTIES_EXTENSION);
            }
            return URIHelper.convert(fullPath);
        }
        return item.getProperty().eResource().getURI();
    }

    // public List<Resource> getAffectedResources(Property property, boolean needScreenshot) {
    // List<Resource> resources = getAffectedResources(property);
    // if (needScreenshot) {
    // Resource screenshotResource = getScreenshotResource(property.getItem());
    // if (screenshotResource != null) {
    // resources.add(screenshotResource);
    // }
    // }
    // return resources;
    // }

    public List<Resource> getAffectedResources(Property property) {
        return getAffectedResources(getResourceSet(), property);
    }

    public List<Resource> getAffectedResources(ResourceSet resourceSet, Property property) {
        List<Resource> resources = new ArrayList<Resource>();
        List<Resource> allRes = resourceSet.getResources();
        synchronized (allRes) {
            Iterator<EObject> i = property.getItem().eCrossReferences().iterator();
            while (i.hasNext()) {
                EObject object = i.next();
                Resource currentResource = object.eResource();
                if (currentResource == null) {
                    // only for invalid items !!
                    continue;
                }
                if (!resources.contains(currentResource)) {

                    // ignore the business model linking *.business_diagram file...(for update version of item...)
                    if (object instanceof org.eclipse.gmf.runtime.notation.impl.DiagramImpl) {
                        continue;
                    }

                    if (!currentResource.getURI().lastSegment().equals(getProjectFilename())) {
                        resources.add(currentResource);
                    }
                    if (!resourceSet.getResources().contains(currentResource)) {
                        resourceSet.getResources().add(currentResource);
                    }
                }
                if (object instanceof ReferenceFileItem) {
                    ReferenceFileItem fi = (ReferenceFileItem) object;
                    try {
                        ByteArray ba = fi.getContent();
                        if (ba != null) {
                            Resource fiResource = ba.eResource();
                            if (fiResource != null) {
                                resources.add(fiResource);
                            }
                        }
                    } catch (Exception e) {
                        final Resource referenceFileResource = getReferenceFileResource(property.getItem().eResource(), fi, true);
                        if (referenceFileResource != null) {
                            resources.add(referenceFileResource);
                        }
                    }
                }
            }
            i = property.getItem().eAllContents();
            while (i.hasNext()) {
                EObject object = i.next();
                Iterator<EObject> j = object.eCrossReferences().iterator();
                while (j.hasNext()) {
                    EObject childEObject = j.next();
                    Resource currentResource = childEObject.eResource();
                    if (currentResource == null) {
                        // only for invalid items !!
                        continue;
                    }
                    if (!resources.contains(currentResource)) {

                        // ignore the business model linking *.business_diagram file...(for update version of item...)
                        if (object instanceof org.eclipse.gmf.runtime.notation.impl.DiagramImpl) {
                            continue;
                        }

                        if (!currentResource.getURI().lastSegment().equals(getProjectFilename())) {
                            resources.add(currentResource);
                        }
                    }
                    if (!resourceSet.getResources().contains(currentResource)) {
                        resourceSet.getResources().add(currentResource);
                    }
                }
            }

            boolean isTestContainer = false;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                        .getDefault().getService(ITestContainerProviderService.class);
                if (testContainerService != null) {
                    isTestContainer = testContainerService.isTestContainerItem(property.getItem());
                }
            }

            if (property.getItem() instanceof ProcessItem || property.getItem() instanceof JobletProcessItem || isTestContainer) {
                if (property.eResource() != null) {
                    Resource screenshotResource = getScreenshotResource(resourceSet, property.getItem(), false, false);
                    if (screenshotResource != null) {
                        resources.add(screenshotResource);
                    }
                }
            }
        }
        return resources;
    }

    public void moveResource(Resource resource, IPath path) throws PersistenceException {
        resourcesMap.remove(resource.getURI());
        ResourceUtils.moveResource(URIHelper.getFile(resource.getURI()), path);
        resource.setURI(URIHelper.convert(path));
        resourcesMap.put(resource.getURI(), resource);
    }

    public void saveResource(Resource resource) throws PersistenceException {
        EmfHelper.saveResource(resource);
    }

    // MOD mzhao 2010-11-22, suppport TDQ item file extensions.(.ana, .rep, etc), this function do not work items that
    // do not need version
    public URI getItemResourceURI(URI propertyResourceURI, String... fileExtension) {
        return getItemResourceURI(propertyResourceURI, true, fileExtension);
    }

    public URI getItemResourceURI(URI propertyResourceURI, boolean needVersion, String... fileExtension) {
        URI trimFileExtension = propertyResourceURI.trimFileExtension();
        if (!needVersion) {
            IPath convert = URIHelper.convert(trimFileExtension);
            String portableString = convert.toPortableString();
            int lastIndexOf = portableString.lastIndexOf('_');
            String filePathWithoutVersion = portableString.substring(0, lastIndexOf);
            trimFileExtension = URIHelper.convert(new Path(filePathWithoutVersion));
        }
        if (fileExtension == null || fileExtension.length == 0 || fileExtension[0] == null) {
            return trimFileExtension.appendFileExtension(FileConstants.ITEM_EXTENSION);
        } else {
            return trimFileExtension.appendFileExtension(fileExtension[0]);
        }
    }

    // private URI getPropertyResourceURI(URI itemResourceURI) {
    // return itemResourceURI.trimFileExtension().appendFileExtension(FileConstants.PROPERTIES_EXTENSION);
    // }

    private URI getScreenshotResourceURI(URI itemResourceURI) {
        // return itemResourceURI.trimFileExtension().appendFileExtension(FileConstants.PROPERTIES_EXTENSION);
        return itemResourceURI.trimFileExtension().appendFileExtension(FileConstants.SCREENSHOT_EXTENSION);
    }

    // MOD mzhao 2010-11-22, suppport TDQ item file extensions.(.ana, .rep, etc)
    private URI getItemResourceURI(IProject project, ERepositoryObjectType repositoryObjectType, IPath path, Item item)
            throws PersistenceException {
        IPath folderPath = getFolderPath(project, repositoryObjectType, path);
        FileName fileName = ResourceFilenameHelper.create(item.getProperty());
        IPath resourcePath = null;
        if (item.getFileExtension() == null) {
            resourcePath = ResourceFilenameHelper.getExpectedFilePath(fileName, folderPath, FileConstants.ITEM_EXTENSION,
                    item.isNeedVersion());
        } else {
            resourcePath = ResourceFilenameHelper.getExpectedFilePath(fileName, folderPath, item.getFileExtension(),
                    item.isNeedVersion());
        }
        return URIHelper.convert(resourcePath);
    }

    @Deprecated
    private URI getItemResourceURI(IProject project, ERepositoryObjectType repositoryObjectType, IPath path, Item item,
            String fileExtension) throws PersistenceException {
        IPath folderPath = getFolderPath(project, repositoryObjectType, path);
        FileName fileName = ResourceFilenameHelper.create(item.getProperty());
        IPath resourcePath = ResourceFilenameHelper
                .getExpectedFilePath(fileName, folderPath, fileExtension, item.isNeedVersion());
        return URIHelper.convert(resourcePath);
    }

    // added by dlin 2011-7-14 to create the uri of file of .screenshot
    private URI getScreenshotResourceURI(IProject project, ERepositoryObjectType repositoryObjectType, IPath path, Item item,
            String... fileExtension) throws PersistenceException {
        IPath folderPath = getFolderPath(project, repositoryObjectType, path);
        FileName fileName = ResourceFilenameHelper.create(item.getProperty());
        IPath resourcePath = ResourceFilenameHelper.getExpectedFilePath(fileName, folderPath, FileConstants.SCREENSHOT_EXTENSION,
                item.isNeedVersion());
        if (fileExtension != null && fileExtension.length > 0) {
            resourcePath = ResourceFilenameHelper.getExpectedFilePath(fileName, folderPath, fileExtension[0],
                    item.isNeedVersion());
        }
        return URIHelper.convert(resourcePath);
    }

    public boolean isPropertyFile(IFile file) {
        return FileConstants.PROPERTIES_EXTENSION.equals(file.getFileExtension());
    }

    public boolean isPropertyFile(String filename) {
        return filename.endsWith(FileConstants.PROPERTIES_EXTENSION);
    }

    public boolean isProjectFile(IFile file) {
        return getProjectFilename().equals(file.getName());
    }

    public void propagateFileName(Property lastVersionProperty, Property resourceProperty) throws PersistenceException {
        // Normally the main checks are done from LocalRepositoryFactory now for the versions, but still do some here in
        // case.

        // Note that now if need to create a new version and change the label as well, it will need to call this
        // function 2 times.
        // fist time it will change the version, second time it will change the name.

        // Since when create a new version (without rename) it only needs one call (and not one for every existing
        // version), it's more simple like this
        ResourceFilenameHelper.FileName fileNameTest = ResourceFilenameHelper.create(resourceProperty.eResource(),
                resourceProperty, lastVersionProperty);
        if (!ResourceFilenameHelper.mustChangeVersion(fileNameTest)
                && ResourceFilenameHelper.hasSameNameButDifferentCase(fileNameTest)) {
            throw new PersistenceException("No change of case allowed here"); //$NON-NLS-1$
        }
        if (!ResourceFilenameHelper.mustChangeVersion(fileNameTest) && !ResourceFilenameHelper.mustChangeLabel(fileNameTest)) {
            return;
        }

        List<Resource> affectedResources = getAffectedResources(resourceProperty);
        List<Resource> resourcesToSave = new ArrayList<Resource>();
        boolean needVersion = lastVersionProperty.getItem().isNeedVersion() || resourceProperty.getItem().isNeedVersion();
        for (Resource resource : affectedResources) {
            ResourceFilenameHelper.FileName fileName = ResourceFilenameHelper.create(resource, resourceProperty,
                    lastVersionProperty);

            // only test for version OR change label.
            // to simplify now, in case of both changes needed, we simply call the function 2 times
            if (ResourceFilenameHelper.mustChangeVersion(fileName) && needVersion) {
                IPath path = ResourceFilenameHelper.getExpectedFilePath(fileName, false);
                resourcesMap.remove(resource.getURI());
                resource.setURI(URIHelper.convert(path));
                resourcesMap.put(resource.getURI(), resource);
                resourcesToSave.add(resource);
            } else if (ResourceFilenameHelper.mustChangeLabel(fileName)) {
                resourceProperty.setLabel(lastVersionProperty.getLabel());
                resourceProperty.setDisplayName(lastVersionProperty.getDisplayName());
                moveResource(resource, ResourceFilenameHelper.getExpectedFilePath(fileName, false));
                resourcesToSave.add(resource);
            }
        }

        for (Resource resource : resourcesToSave) {
            saveResource(resource);
        }
        if (!resourceProperty.equals(lastVersionProperty)) {
            // this version was only used to rename the file.
            // we can directly unload the resource to free the memory.
            Item item = resourceProperty.getItem();
            if (item.getParent() != null && item.getParent() instanceof FolderItem) {
                ((FolderItem) item.getParent()).getChildren().remove(item);
                item.setParent(null);
            }
        }
    }

    protected void copyResource(Resource resource, IPath path) throws PersistenceException {

        IFile srcFile = URIHelper.getFile(resource.getURI());
        try {
            srcFile.copy(path, true, null);
        } catch (CoreException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Copies the original screent shot of original item and writes into a new screen shot of new item. Before invoking
     * this method, client should make sure the items have screen shot. Like metadata item, context item and etc, they
     * have not screen shot. Added by Marvin Wang on Apr 15, 2013.
     * 
     * @param originalItem process, joblet or m/r process
     * @param newItem process, joblet or m/r process
     * @throws IOException
     */
    // add for bug TDI-20844,when copy or duplicate a job,joblet.just copy .screenshot file will be ok.
    public void copyScreenshotFile(Item originalItem, Item newItem) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        try {
            URI orgPropertyResourceURI = EcoreUtil.getURI(originalItem.getProperty());
            URI orgRelativePlateformDestUri = orgPropertyResourceURI.trimFileExtension().appendFileExtension(
                    FileConstants.SCREENSHOT_EXTENSION);
            URL orgFileURL = FileLocator.toFileURL(new java.net.URL(
                    "platform:/resource" + orgRelativePlateformDestUri.toPlatformString(true))); //$NON-NLS-1$

            URI newPropertyResourceURI = EcoreUtil.getURI(newItem.getProperty());
            URI newRelativePlateformDestUri = newPropertyResourceURI.trimFileExtension().appendFileExtension(
                    FileConstants.SCREENSHOT_EXTENSION);
            URL newFileURL = FileLocator.toFileURL(new java.net.URL(
                    "platform:/resource" + newRelativePlateformDestUri.toPlatformString(true))); //$NON-NLS-1$

            os = new FileOutputStream(newFileURL.getFile());
            is = new BufferedInputStream(new FileInputStream(orgFileURL.getPath()));
            byte[] bytearray = new byte[512];
            int len = 0;
            while ((len = is.read(bytearray)) != -1) {
                os.write(bytearray, 0, len);
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }

    }

    public String getProjectFilename() {
        if (useOldProjectFile) {
            return FileConstants.OLD_TALEND_PROJECT_FILENAME;
        } else {
            return FileConstants.LOCAL_PROJECT_FILENAME;
        }
    }

    public void setUseOldProjectFile(boolean useOldProjectFile) {
        this.useOldProjectFile = useOldProjectFile;
    }

    public void unloadResources() {
        List<Resource> resources = getResourceSet().getResources();
        synchronized (resources) {
            List<Resource> toRemove = new ArrayList<Resource>();
            for (Resource resource : resources) {
                if (resource != null) {
                    resource.unload();
                    toRemove.add(resource);
                }
            }
            resources.removeAll(toRemove);
        }
    }

    public void unloadResources(Property property) {
        unloadResources(getResourceSet(), property);
    }

    public void unloadResources(ResourceSet resourceSet, Property property) {
        for (Resource resource : getAffectedResources(resourceSet, property)) {
            if (resource != null) {
                resource.unload();
                resourceSet.getResources().remove(resource);
            }
        }
    }

    /**
     * Method "unloadResource" unload and remove the specification resource from the resource set. MOD mzhao
     * 
     * @param uriString the uri sting of resource.
     */
    public void unloadResource(String uriString) {
        List<Resource> resources = getResourceSet().getResources();
        synchronized (resources) {
            List<Resource> toRemove = new ArrayList<Resource>();
            for (Resource res : resources) {
                if (res != null && uriString.equals(res.getURI().toString())) {
                    res.unload();
                    toRemove.add(res);
                }
            }
            resources.removeAll(toRemove);
        }
    }

    public boolean isAvoidUnloadResource() {
        return this.avoidUnloadResource;
    }

    public void setAvoidUnloadResource(boolean avoidUnloadResource) {
        this.avoidUnloadResource = avoidUnloadResource;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }
}
