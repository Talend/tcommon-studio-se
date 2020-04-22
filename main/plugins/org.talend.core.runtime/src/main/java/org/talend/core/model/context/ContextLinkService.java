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
package org.talend.core.model.context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ContextLinkService {

    public static final String LINKS_FOLDER_NAME = "links";

    public static final String LINK_FILE_POSTFIX = ".link";

    private static ContextLinkService instance = new ContextLinkService();

    private ContextLinkService() {
    }

    public static ContextLinkService getInstance() {
        return instance;
    }

    public boolean saveContextLink(Item item) throws PersistenceException {
        if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            return saveContextLink(processItem.getProcess(), item.getProperty().getId());
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletItem = (JobletProcessItem) item;
            return saveContextLink(jobletItem.getJobletProcess(), item.getProperty().getId());
        } else if (item instanceof ConnectionItem) {
            ConnectionItem connectionItem = (ConnectionItem) item;
            return saveContextLink(connectionItem.getConnection(), item.getProperty().getId());
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean saveContextLink(Connection connection, String id) throws PersistenceException {
        boolean modified = false;
        ItemContextLink itemContextLink = new ItemContextLink();
        itemContextLink.setItemId(id);
        if (connection.isContextMode()) {
            String contextId = connection.getContextId();
            if (StringUtils.isEmpty(contextId) || IContextParameter.BUILT_IN.equals(contextId)) {
                return modified;
            }
            ContextLink contextLink = new ContextLink();
            contextLink.setContextName(connection.getContextName());
            contextLink.setRepoId(contextId);
            itemContextLink.getContextList().add(contextLink);
            
            ContextItem contextItem = ContextUtils.getContextItemById2(contextId);
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem.getContext(), connection.getContextName());
            for (Object o : contextType.getContextParameter()) {
                if (o instanceof ContextParameterType) {
                    ContextParameterType contextParameterType = (ContextParameterType) o;
                    ContextParamLink contextParamLink = new ContextParamLink();
                    contextParamLink.setName(contextParameterType.getName());
                    contextParamLink.setId(ResourceHelper.getUUID(contextParameterType));
                    contextLink.getParameterList().add(contextParamLink);
                }
            }
        }
        if (itemContextLink.getContextList().size() > 0) {
            ContextLinkService.getInstance().saveContextLink(id, itemContextLink);
            modified = true;
        } else {
            deleteContextLink(id);
        }
        return modified;
    }

    public boolean saveContextLink(ProcessType processType, String id) throws PersistenceException {
        boolean modified = false;
        ItemContextLink itemContextLink = new ItemContextLink();
        itemContextLink.setItemId(id);
        Map<String, ContextItem> tempCache = new HashMap<String, ContextItem>();
        for (Object object : processType.getContext()) {
            if (object instanceof ContextType) {
                ContextType jobContextType = (ContextType) object;
                for (Object o : jobContextType.getContextParameter()) {
                    if (o instanceof ContextParameterType) {
                        ContextParameterType contextParameterType = (ContextParameterType) o;
                        String repositoryContextId = contextParameterType.getRepositoryContextId();
                        if (StringUtils.isEmpty(repositoryContextId) || IContextParameter.BUILT_IN.equals(repositoryContextId)) {
                            continue;
                        }
                        ContextLink contextLink = itemContextLink.findContextLink(contextParameterType.getRepositoryContextId(),
                                jobContextType.getName());
                        if (contextLink == null) {
                            contextLink = new ContextLink();
                            contextLink.setContextName(jobContextType.getName());
                            contextLink.setRepoId(repositoryContextId);
                            itemContextLink.getContextList().add(contextLink);
                        }
                        ContextParamLink contextParamLink = createParamLink(repositoryContextId, jobContextType.getName(),
                                contextParameterType.getName(), tempCache);
                        contextLink.getParameterList().add(contextParamLink);
                    }
                }
            }
        }
        if (itemContextLink.getContextList().size() > 0) {
            ContextLinkService.getInstance().saveContextLink(id, itemContextLink);
            modified = true;
        } else {
            deleteContextLink(id);
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private ContextParamLink createParamLink(String repositoryContextId, String contextName, String paramName,
            Map<String, ContextItem> tempCache) {
        ContextParamLink contextParamLink = new ContextParamLink();
        contextParamLink.setName(paramName);
        ContextItem contextItem = tempCache.get(repositoryContextId);
        if (contextItem == null) {
            contextItem = ContextUtils.getContextItemById2(repositoryContextId);
            tempCache.put(repositoryContextId, contextItem);
        }
        if (contextItem != null) {
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem.getContext(), contextName);
            ContextParameterType repoContextParameterType = ContextUtils.getContextParameterTypeByName(contextType, paramName);
            contextParamLink.setId(ResourceHelper.getUUID(repoContextParameterType));
            return contextParamLink;
        }
        return contextParamLink;
    }

    private synchronized void saveContextLink(String id, ItemContextLink itemContextLink) throws PersistenceException {
        IFile linkFile = calContextLinkFile(id);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(linkFile.getLocation().toFile(), itemContextLink);
            linkFile.refreshLocal(0, null);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public synchronized ItemContextLink loadContextLink(String id) throws PersistenceException {
        IFile linkFile = calContextLinkFile(id);
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

    public synchronized void deleteContextLink(String id) throws PersistenceException {
        IFile linkFile = calContextLinkFile(id);
        if (linkFile != null && linkFile.exists()) {
            try {
                linkFile.delete(true, null);
            } catch (CoreException e) {
                throw new PersistenceException(e);
            }
        }
    }

    public IFile calContextLinkFile(String id) throws PersistenceException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        IFolder linksFolder = getLinksFolder();
        return linksFolder.getFile(calLinkFileName(id));
    }

    private String calLinkFileName(String id) {
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

    private IFolder getLinksFolder() throws PersistenceException {
        IProject iProject = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel());
        IFolder settingFolder = iProject.getFolder(RepositoryConstants.SETTING_DIRECTORY);
        IFolder linksFolder = settingFolder.getFolder(LINKS_FOLDER_NAME);
        return linksFolder;
    }

}
