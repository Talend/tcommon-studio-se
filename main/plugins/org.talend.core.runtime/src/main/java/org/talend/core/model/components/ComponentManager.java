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
package org.talend.core.model.components;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCacheFactory;
import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.component_cache.util.ComponentCacheResourceFactoryImpl;

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public class ComponentManager {

    private static ComponentsCache cache = ComponentCacheFactory.eINSTANCE.createComponentsCache();

    private static final String TALEND_COMPONENT_CACHE = "ComponentsCache.";

    private static final String TALEND_FILE_NAME = "cache";

    private static boolean modified = false;

    public static ComponentsCache getComponentCache() {
        return cache;
    }

    public static void saveResource() {
        if (isModified()) {
            String installLocation = new Path(Platform.getConfigurationLocation().getURL().getPath()).toFile().getAbsolutePath();
            try {
                Resource resource = createComponentCacheResource(installLocation);
                resource.getContents().add(cache);
                EmfHelper.saveResource(cache.eResource());
            } catch (PersistenceException e1) {
                ExceptionHandler.process(e1);
            }
            setModified(false);
        }
    }

    public static Resource createComponentCacheResource(String installLocation) {
        String filePath = ComponentManager.TALEND_COMPONENT_CACHE + LanguageManager.getCurrentLanguage().toString().toLowerCase()
                + ComponentManager.TALEND_FILE_NAME;
        URI uri = URI.createFileURI(installLocation).appendSegment(filePath);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        return compFact.createResource(uri);
    }

    /**
     * Getter for modified.
     *
     * @return the modified
     */
    public static boolean isModified() {
        return modified;
    }

    /**
     * Sets the modified.
     *
     * @param modified the modified to set
     */
    public static void setModified(boolean modified) {
        ComponentManager.modified = modified;
    }

    /**
     * DOC guanglong.du Comment method "loadComponentResource".
     *
     * @param eclipseProject
     * @return
     * @throws IOException
     */
    public static ComponentsCache loadComponentCacheFile(String installLocation) throws IOException {
        String filePath = TALEND_COMPONENT_CACHE + LanguageManager.getCurrentLanguage().toString().toLowerCase()
                + TALEND_FILE_NAME;
        if (!new File(filePath).exists()) {
            return null;
        }
        URI uri = URI.createFileURI(installLocation).appendSegment(filePath);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        Resource resource = compFact.createResource(uri);
        Map optionMap = new HashMap();
        optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
        optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
        resource.load(optionMap);
        ComponentsCache cache = (ComponentsCache) EcoreUtil.getObjectByType(resource.getContents(),
                ComponentCachePackage.eINSTANCE.getComponentsCache());
        return cache;
    }
}
