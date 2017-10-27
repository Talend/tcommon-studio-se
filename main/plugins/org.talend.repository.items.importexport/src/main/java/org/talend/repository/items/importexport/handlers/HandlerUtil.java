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
package org.talend.repository.items.importexport.handlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.helper.ByteArrayResource;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class HandlerUtil {

    public static URI getURI(IPath path) {
        return URI.createURI(path.lastSegment());
    }

    public static IPath getItemPath(IPath path, Item item) {
        IPath removeFileExtension = path.removeFileExtension();
        if (!item.isNeedVersion()) {
            String portableString = removeFileExtension.toPortableString();
            String substring = portableString.substring(0, portableString.lastIndexOf('_'));
            removeFileExtension = new Path(substring);
        }
        if (item.getFileExtension() != null) {
            return removeFileExtension.addFileExtension(item.getFileExtension());
        } else {
            return removeFileExtension.addFileExtension(FileConstants.ITEM_EXTENSION);
        }
    }

    public static IPath getReferenceItemPath(IPath path, String extension) {
        return path.removeFileExtension().addFileExtension(extension);
    }

    public static IPath getValidProjectFilePath(ResourcesManager collector, IPath path) {
        IPath projectFilePath = path.removeLastSegments(1);

        while (projectFilePath.lastSegment() != null
                && !collector.getPaths().contains(projectFilePath.append(FileConstants.LOCAL_PROJECT_FILENAME))) {
            projectFilePath = projectFilePath.removeLastSegments(1);
        }
        if (collector.getPaths().contains(projectFilePath.append(FileConstants.LOCAL_PROJECT_FILENAME))) {
            return projectFilePath.append(FileConstants.LOCAL_PROJECT_FILENAME);
        }
        return null;
    }

    // added by dlin 2011-7-25 don't like .item and .property ,just copy .screenshot file will be ok
    public static void copyScreenshotFile(ResourcesManager manager, ImportItem itemRecord) throws IOException {
        // int id = itemRecord.getItem().eClass().getClassifierID();
        // if (id != PropertiesPackage.PROCESS_ITEM && id != PropertiesPackage.JOBLET_PROCESS_ITEM) {
        // return;
        // }
        OutputStream os = null;
        InputStream is = null;
        try {
            URI propertyResourceURI = EcoreUtil.getURI(itemRecord.getItem().getProperty());
            URI relativePlateformDestUri = propertyResourceURI.trimFileExtension().appendFileExtension(
                    FileConstants.SCREENSHOT_EXTENSION);
            URL fileURL = FileLocator.toFileURL(new java.net.URL(
                    "platform:/resource" + relativePlateformDestUri.toPlatformString(true))); //$NON-NLS-1$
            // for migration task ,there is not .screeenshot file in preceding version - begin
            boolean hasScreenshotFile = false;
            Iterator it = manager.getPaths().iterator();
            IPath screenshotNeeded = itemRecord.getPath().removeFileExtension()
                    .addFileExtension(FileConstants.SCREENSHOT_EXTENSION);
            while (it.hasNext()) {
                IPath path = (IPath) it.next();
                if (path.equals(screenshotNeeded)) {
                    hasScreenshotFile = true;
                    break;
                }
            }
            if (!hasScreenshotFile) {
                return;
            }
            // for migration task ,there is not .screeenshot file in preceding version - begin
            os = new FileOutputStream(fileURL.getFile());
            manager.getPaths().iterator().next();
            is = manager.getStream(screenshotNeeded, itemRecord);
            FileCopyUtils.copyStreams(is, os);
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * get relative Path of the Item
     */
    public static IPath getValidItemRelativePath(ResourcesManager resManager, IPath path) {
        IPath projectFilePath = HandlerUtil.getValidProjectFilePath(resManager, path);
        if (projectFilePath != null) {
            // remove the last segments "talend.project"
            IPath projectRootPath = projectFilePath.removeLastSegments(1);
            // relative to import project
            return path.makeRelativeTo(projectRootPath);
        }
        return null;
    }

    /**
     * 
     * load the resource
     */
    public static Resource loadResource(ResourcesManager manager, ImportItem importItem) {
        InputStream stream = null;
        try {
            final Resource resource = createResource(importItem, importItem.getPath(), false);
            stream = manager.getStream(importItem.getPath(), importItem);
            final ResourceSet resourceSet = resource.getResourceSet();

            URIConverter uriConverter = resourceSet.getURIConverter();
            resourceSet.setURIConverter(new ExtensibleURIConverterImpl() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#createInputStream(org.eclipse.
                 * emf.common.util.URI, java.util.Map)
                 */
                @Override
                public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
                    InputStream inputStream = null;
                    EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(uri.toString());
                    if (ePackage != null || !"http".equals(uri.scheme())) { //$NON-NLS-1$
                        inputStream = super.createInputStream(uri, options);
                    } else {
                        inputStream = null;
                    }
                    return inputStream;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#contentDescription(org.eclipse.emf
                 * .common.util.URI, java.util.Map)
                 */
                @Override
                public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
                    EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(uri.toString());
                    if (ePackage != null || !"http".equals(uri.scheme())) { //$NON-NLS-1$
                        return super.contentDescription(uri, options);
                    } else {
                        return Collections.emptyMap();
                    }
                }
            });
            resource.load(stream, null);
            // set back the old one
            resourceSet.setURIConverter(uriConverter);
            return resource;
        } catch (Exception e) {
            // same as old ImportItemUtil.computeProperty ignore, must be one invalid or unknown item
            if (Platform.inDebugMode()) {
                ExceptionHandler.process(e);
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // ignore
                    if (Platform.inDebugMode()) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return null;
    }

    public static Resource createResource(ImportItem importItem, IPath path, boolean byteArrayResource) {
        if (importItem == null || path == null) {
            return null;
        }
        Resource resource;
        ResourceSet resourceSet = importItem.getResourceSet();
        final URI pathUri = HandlerUtil.getURI(path);
        if (byteArrayResource) {
            resource = new ByteArrayResource(pathUri);
            resourceSet.getResources().add(resource);
        } else {
            resource = resourceSet.createResource(pathUri);
        }
        return resource;
    }
}
