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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexFactory;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage;
import org.talend.librariesmanager.emf.librariesindex.util.LibrariesindexResourceFactoryImpl;

public class LibrariesIndexManager {

    private LibrariesIndex studioLibIndex;

    private LibrariesIndex mavenLibIndex;

    private static LibrariesIndexManager manager = new LibrariesIndexManager();;

    private static final String LIBRARIES_INDEX = "LibrariesIndex.xml";

    private static final String MAVEN_INDEX = "MavenUriIndex.xml";

    private LibrariesIndexManager() {
        loadIndexResources();
    }

    public static LibrariesIndexManager getInstance() {
        return manager;
    }

    private synchronized void loadIndexResources() {
        try {
            if (!new File(getStudioIndexPath()).exists()) {
                studioLibIndex = LibrariesindexFactory.eINSTANCE.createLibrariesIndex();
            } else {
                Resource resource = createLibrariesIndexResource(getIndexFileInstallFolder(), LIBRARIES_INDEX);
                Map optionMap = new HashMap();
                optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
                optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
                optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
                optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
                optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
                resource.load(optionMap);
                studioLibIndex = (LibrariesIndex) EcoreUtil.getObjectByType(resource.getContents(),
                        LibrariesindexPackage.eINSTANCE.getLibrariesIndex());
            }

            if (!new File(getMavenIndexPath()).exists()) {
                mavenLibIndex = LibrariesindexFactory.eINSTANCE.createLibrariesIndex();
            } else {
                Resource resource = createLibrariesIndexResource(getIndexFileInstallFolder(), MAVEN_INDEX);
                Map optionMap = new HashMap();
                optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
                optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
                optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
                optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
                optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
                resource.load(optionMap);
                mavenLibIndex = (LibrariesIndex) EcoreUtil.getObjectByType(resource.getContents(),
                        LibrariesindexPackage.eINSTANCE.getLibrariesIndex());
            }

        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        }
    }

    public void saveStudioIndexResource() {
        saveResource(studioLibIndex, LIBRARIES_INDEX);

    }

    public void saveMavenIndexResource() {
        saveResource(mavenLibIndex, MAVEN_INDEX);
    }

    private void saveResource(EObject eObject, String fileName) {
        try {
            Resource resource = createLibrariesIndexResource(getIndexFileInstallFolder(), fileName);
            resource.getContents().add(eObject);
            EmfHelper.saveResource(eObject.eResource());
        } catch (PersistenceException e1) {
            CommonExceptionHandler.process(e1);
        }

    }

    public void clearAll() {
        if (studioLibIndex != null) {
            studioLibIndex.setInitialized(false);
            studioLibIndex.getJarsToRelativePath().clear();
            saveStudioIndexResource();
        }
        if (mavenLibIndex != null) {
            mavenLibIndex.setInitialized(false);
            mavenLibIndex.getJarsToRelativePath().clear();
            saveMavenIndexResource();
        }
    }

    private Resource createLibrariesIndexResource(String installLocation, String fileName) {
        URI uri = URI.createFileURI(installLocation).appendSegment(fileName);
        LibrariesindexResourceFactoryImpl indexFact = new LibrariesindexResourceFactoryImpl();
        return indexFact.createResource(uri);
    }

    private String getIndexFileInstallFolder() {
        return new Path(Platform.getConfigurationLocation().getURL().getPath()).toFile().getAbsolutePath();
    }

    public String getStudioIndexPath() {
        return new Path(getIndexFileInstallFolder()).append(LIBRARIES_INDEX).toFile().getAbsolutePath();
    }

    public String getMavenIndexPath() {
        return new Path(getIndexFileInstallFolder()).append(MAVEN_INDEX).toFile().getAbsolutePath();
    }

    public LibrariesIndex getStudioLibIndex() {
        return studioLibIndex;
    }

    /**
     * Getter for mavenLibIndex.
     * 
     * @return the mavenLibIndex
     */
    public LibrariesIndex getMavenLibIndex() {
        return this.mavenLibIndex;
    }

    public String getMvnUriFromIndex(String jarName) {
        if (mavenLibIndex != null) {
            return this.mavenLibIndex.getJarsToRelativePath().get(jarName);
        }

        return null;
    }

}
