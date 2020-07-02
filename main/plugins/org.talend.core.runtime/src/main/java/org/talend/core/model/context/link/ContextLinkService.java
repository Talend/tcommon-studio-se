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
package org.talend.core.model.context.link;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ContextLinkService {

    private static final Logger LOGGER = Logger.getLogger(ContextLinkService.class);

    private static final String CREATOR_EXT_ID = "org.talend.core.runtime.saveItemContextLinkService"; //$NON-NLS-1$

    public static final String LINKS_FOLDER_NAME = "links";

    public static final String LINK_FILE_POSTFIX = ".link";

    private static final List<IItemContextLinkService> registeredService = new ArrayList<IItemContextLinkService>();

    private static ContextLinkService instance = new ContextLinkService();

    private static final String CURRENT_PROJECT_LABEL = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();

    private ContextLinkService() {
        initService();
    }

    public static ContextLinkService getInstance() {
        return instance;
    }

    public synchronized boolean saveContextLink(Item item) throws PersistenceException {
        for (IItemContextLinkService service : registeredService) {
            if (service.accept(item)) {
                return service.saveItemLink(item);
            }
        }
        return false;
    }

    public synchronized boolean mergeContextLink(Item item, ItemContextLink backupContextLink, InputStream remoteLinkFile)
            throws PersistenceException {
        for (IItemContextLinkService service : registeredService) {
            if (service.accept(item)) {
                return service.mergeItemLink(item, backupContextLink, remoteLinkFile);
            }
        }
        return false;
    }

    public boolean isSupportContextLink(Item item) {
        for (IItemContextLinkService service : registeredService) {
            if (service.accept(item)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void saveContextLinkToJson(Item item, ItemContextLink itemContextLink) throws PersistenceException {
        IFolder linksFolder = getLinksFolder(getItemProjectLabel(item));
        if (!linksFolder.exists()) {
            ResourceUtils.createFolder(linksFolder);
        }
        IFile linkFile = calContextLinkFile(item);
        saveContextLinkToJson(linkFile, itemContextLink);
    }

    public synchronized void saveContextLinkToJson(IFile linkFile, ItemContextLink itemContextLink) throws PersistenceException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemContextLink);
            if (!linkFile.exists()) {
                ResourceUtils.createFile(new ByteArrayInputStream(content.getBytes()), linkFile);
            } else {
                ResourceUtils.setFileContent(new ByteArrayInputStream(content.getBytes()), linkFile);
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public synchronized boolean changeRepositoryId(Item item, Map<String, String> old2NewMap) throws PersistenceException {
        boolean isModofied = false;
        ItemContextLink itemContextLink = loadContextLinkFromJson(item);
        if (itemContextLink != null) {
            for (ContextLink contextLink : itemContextLink.getContextList()) {
                if (old2NewMap.containsKey(contextLink.getRepoId())) {
                    contextLink.setRepoId(old2NewMap.get(contextLink.getRepoId()));
                    isModofied = true;
                }
            }
        }
        if (isModofied) {
            this.saveContextLinkToJson(item, itemContextLink);
        }
        return isModofied;
    }

    public synchronized ItemContextLink loadContextLinkFromJson(Item item) throws PersistenceException {
        for (IItemContextLinkService service : registeredService) {
            if (service.accept(item)) {
                return service.loadItemLink(item);
            }
        }
        return doLoadContextLinkFromJson(item);
    }

    public synchronized ItemContextLink doLoadContextLinkFromJson(Item item) throws PersistenceException {
        IFile linkFile = calContextLinkFile(item);
        return doLoadContextLinkFromFile(linkFile);
    }

    public synchronized ItemContextLink doLoadContextLinkFromJson(String projectLabel, String id) throws PersistenceException {
        IFile linkFile = calContextLinkFile(projectLabel, id);
        return doLoadContextLinkFromFile(linkFile);
    }

    public synchronized ItemContextLink doLoadContextLinkFromFile(IFile linkFile) throws PersistenceException {
        if (linkFile == null || !linkFile.exists()) {
            return null;
        }
        ItemContextLink contextLink = null;
        try {
            contextLink = new ObjectMapper().readValue(linkFile.getLocation().toFile(), ItemContextLink.class);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
        return contextLink;
    }

    public synchronized ItemContextLink doLoadContextLinkFromFile(InputStream inputStream) throws PersistenceException {
        if (inputStream == null) {
            return null;
        }
        ItemContextLink contextLink = null;
        try {
            contextLink = new ObjectMapper().readValue(inputStream, ItemContextLink.class);
        } catch (IOException e) {
            throw new PersistenceException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.info("Close input stream failed.");
            }
        }
        return contextLink;
    }

    public synchronized void deleteContextLinkJsonFile(Item item) throws PersistenceException {
        IFile linkFile = calContextLinkFile(item);
        if (linkFile != null && linkFile.exists()) {
            try {
                linkFile.delete(true, null);
            } catch (CoreException e) {
                throw new PersistenceException(e);
            }
        }
    }

    public synchronized void updateRelatedContextParameterId(String sourceId, Map<String, String> repositoryIdChangedMap,
            Map<String, Map<String, String>> changedContextParameterId) throws PersistenceException {
        List<Relation> relationList = RelationshipItemBuilder.getInstance()
                .getItemsHaveRelationWith(sourceId, RelationshipItemBuilder.LATEST_VERSION, false);
        for (Relation relation : relationList) {
            String id = relation.getId();
            IFile linkFile = calContextLinkFile(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), id);
            ItemContextLink itemContextLink = doLoadContextLinkFromFile(linkFile);
            String newRepoId = null;
            boolean isModified = false;
            if (repositoryIdChangedMap != null && repositoryIdChangedMap.containsKey(sourceId)) {
                newRepoId = repositoryIdChangedMap.get(sourceId);
            }
            if (itemContextLink != null) {
                for (ContextLink contextLink : itemContextLink.getContextList()) {
                    for (String repoId : changedContextParameterId.keySet()) {
                        if (StringUtils.equals(repoId, contextLink.getRepoId())) {
                            Map<String, String> oldToNewId = changedContextParameterId.get(repoId);
                            for (String oldId : oldToNewId.keySet()) {
                                ContextParamLink paramLink = contextLink.getParamLinkById(oldId);
                                if (paramLink != null) {
                                    paramLink.setId(oldToNewId.get(oldId));
                                    isModified = true;
                                }
                            }
                        }
                        if (sourceId.equals(repoId) && newRepoId != null) {
                            isModified = true;
                            contextLink.setRepoId(newRepoId);
                        }
                    }
                }
                if (isModified) {
                    saveContextLinkToJson(linkFile, itemContextLink);
                }
            }
            isModified = false;
            if (newRepoId != null) {
                Item relatedItem = ContextUtils.getRepositoryContextItemById(id);
                if (relatedItem != null) {
                    List contextTypes = ContextUtils.getAllContextType(relatedItem);
                    if (contextTypes != null) {
                        for (Object object : contextTypes) {
                            if (object instanceof ContextType) {
                                ContextType context = (ContextType) object;
                                for (Object obj : context.getContextParameter()) {
                                    if (obj instanceof ContextParameterType) {
                                        ContextParameterType parameterType = (ContextParameterType) obj;
                                        if (!ContextUtils.isBuildInParameter(parameterType)
                                                && sourceId.equals(parameterType.getRepositoryContextId())) {
                                            parameterType.setRepositoryContextId(newRepoId);
                                            isModified = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (isModified) {
                    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                    factory.save(relatedItem, false);
                }
            }

        }
    }

    public static IFile calContextLinkFile(Item item) throws PersistenceException {
        if (item == null) {
            return null;
        }
        IFolder linksFolder = getLinksFolder(getItemProjectLabel(item));
        return linksFolder.getFile(calLinkFileName(item.getProperty().getId()));
    }

    public static IFile calContextLinkFile(String projectLabel, String itemId) throws PersistenceException {
        if (projectLabel == null || itemId == null) {
            return null;
        }
        IFolder linksFolder = getLinksFolder(projectLabel);
        return linksFolder.getFile(calLinkFileName(itemId));
    }

    public static String getItemProjectLabel(Item item) {
        Project project = ProjectManager.getInstance().getProject(item);
        if (project != null) {
            return project.getTechnicalLabel();
        }
        return CURRENT_PROJECT_LABEL;
    }

    private static String calLinkFileName(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(LINK_FILE_POSTFIX);
        return sb.toString();
    }

    public static IFile calLinksFile(IFolder projectFolder, String id) {
        IFolder settingFolder = projectFolder.getFolder(RepositoryConstants.SETTING_DIRECTORY);
        IFolder linksFolder = settingFolder.getFolder(LINKS_FOLDER_NAME);
        return linksFolder.getFile(getLinkFileName(id));
    }

    public static String calLinksFilePath(String projectPath, String id) {
        StringBuilder sb = new StringBuilder(projectPath);
        sb.append(File.separator).append(RepositoryConstants.SETTING_DIRECTORY);
        sb.append(File.separator).append(LINKS_FOLDER_NAME);
        sb.append(File.separator).append(getLinkFileName(id));
        return sb.toString();
    }

    public static IFile calLinksFile(IProject project, String id) {
        IFolder settingFolder = project.getFolder(RepositoryConstants.SETTING_DIRECTORY);
        IFolder linksFolder = settingFolder.getFolder(LINKS_FOLDER_NAME);
        return linksFolder.getFile(getLinkFileName(id));
    }

    public static String getLinkFileName(String id) {
        return id + LINK_FILE_POSTFIX;
    }

    private static IFolder getLinksFolder(String projectLabel) throws PersistenceException {
        IProject iProject = ResourceUtils.getProject(projectLabel);
        IFolder settingFolder = iProject.getFolder(RepositoryConstants.SETTING_DIRECTORY);
        IFolder linksFolder = settingFolder.getFolder(LINKS_FOLDER_NAME);
        return linksFolder;
    }

    private static void initService() {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(CREATOR_EXT_ID);
        if (extensionPoint != null) {
            IExtension[] extensions = extensionPoint.getExtensions();
            for (IExtension extension : extensions) {
                IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                for (IConfigurationElement configurationElement : configurationElements) {
                    try {
                        Object creator = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                        if (creator instanceof IItemContextLinkService) {
                            IItemContextLinkService service = (IItemContextLinkService) creator;
                            registeredService.add(service);
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }
}
