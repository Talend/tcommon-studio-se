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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EMap;
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

    private ReentrantReadWriteLock studioLibLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock mavenLibLock = new ReentrantReadWriteLock();

    private static final Logger LOGGER = Logger.getLogger(LibrariesIndexManager.class);

    private LibrariesIndexManager() {
        loadIndexResources();
    }

    public static LibrariesIndexManager getInstance() {
        return manager;
    }

    private void loadIndexResources() {
        try {
            studioLibLock.writeLock().lock();
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

        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        } finally {
            studioLibLock.writeLock().unlock();
        }
        try {
            mavenLibLock.writeLock().lock();
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
        } finally {
            mavenLibLock.writeLock().unlock();
        }
    }

    public void saveStudioIndexResource() {
        try {
            studioLibLock.writeLock().lock();
            saveResource(studioLibIndex, LIBRARIES_INDEX);
        } finally {
            studioLibLock.writeLock().unlock();
        }
    }

    public void setInitialized(boolean init) {
        studioLibLock.writeLock().lock();
        try {
            studioLibIndex.setInitialized(init);
        } finally {
            studioLibLock.writeLock().unlock();
        }
        mavenLibLock.writeLock().lock();
        try {
            mavenLibIndex.setInitialized(true);
        } finally {
            mavenLibLock.writeLock().unlock();
        }
    }

    public void saveMavenIndexResource() {
        try {
            mavenLibLock.writeLock().lock();
            saveResource(mavenLibIndex, MAVEN_INDEX);
        } finally {
            mavenLibLock.writeLock().unlock();
        }
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
        try {
            studioLibLock.writeLock().lock();
            if (studioLibIndex != null) {
                studioLibIndex.setInitialized(false);
                studioLibIndex.getJarsToRelativePath().clear();
                saveStudioIndexResource();
            }
        } finally {
            studioLibLock.writeLock().unlock();
        }
        try {
            mavenLibLock.writeLock().lock();
            if (mavenLibIndex != null) {
                mavenLibIndex.setInitialized(false);
                mavenLibIndex.getJarsToRelativePath().clear();
                saveMavenIndexResource();
            }
        } finally {
            mavenLibLock.writeLock().unlock();
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

    public String getMvnUriFromIndex(String jarName) {
        if (mavenLibIndex != null) {
            return this.mavenLibIndex.getJarsToRelativePath().get(jarName);
        }

        return null;
    }

    /**
     * Get all contents inside studio lib index file, return as unmodifiable map
     */
    public Map<String, String> getAllStudioLibsFromIndex() {
        this.studioLibLock.readLock().lock();
        try {
            return Collections.unmodifiableMap(this.studioLibIndex.getJarsToRelativePath().map());
        } finally {
            this.studioLibLock.readLock().unlock();
        }
    }

    /**
     * Add to studio lib without saving
     */
    public void AddStudioLibs(String key, String v) {
        this.studioLibLock.writeLock().lock();
        try {
            this.studioLibIndex.getJarsToRelativePath().put(key, v);
        } finally {
            this.studioLibLock.writeLock().unlock();
        }
    }

    /**
     * whether contains key
     */
    public boolean containsStudioLibs(String key) {
        this.studioLibLock.readLock().lock();
        try {
            return this.studioLibIndex.getJarsToRelativePath().containsKey(key);
        } finally {
            this.studioLibLock.readLock().unlock();
        }
    }

    /**
     * Get all contents inside maven lib index file, return as unmodifiable map
     */
    public Map<String, String> getAllMavenLibsFromIndex() {
        this.mavenLibLock.readLock().lock();
        try {
            return Collections.unmodifiableMap(this.mavenLibIndex.getJarsToRelativePath().map());
        } finally {
            this.mavenLibLock.readLock().unlock();
        }
    }

    /**
     * Add to maven lib without saving
     */
    public void AddMavenLibs(String key, String v) {
        this.mavenLibLock.writeLock().lock();
        try {
            this.mavenLibIndex.getJarsToRelativePath().put(key, v);
        } finally {
            this.mavenLibLock.writeLock().unlock();
        }
    }

    public boolean isStudioLibInitialized() {
        this.studioLibLock.readLock().lock();
        try {
            return this.studioLibIndex.isInitialized();
        } finally {
            this.studioLibLock.readLock().unlock();
        }
    }

    public boolean isMavenLibInitialized() {
        this.mavenLibLock.readLock().lock();
        try {
            return this.mavenLibIndex.isInitialized();
        } finally {
            this.mavenLibLock.readLock().unlock();
        }
    }

}
