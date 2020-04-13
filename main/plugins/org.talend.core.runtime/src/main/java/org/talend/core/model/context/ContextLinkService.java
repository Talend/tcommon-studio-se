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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
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

    private static final String LINKS_FOLDER_NAME = "links";

    private static final String LINK_FILE_POSTFIX = ".link";

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

    public boolean saveContextLink(Connection connection, String id) throws PersistenceException {
        boolean modified = false;
        if (connection.isContextMode()) {
            String contextId = connection.getContextId();
            if (StringUtils.isEmpty(contextId) || IContextParameter.BUILT_IN.equals(contextId)) {
                return modified;
            }
            ContextItem contextItem = ContextUtils.getContextItemById2(contextId);
            ContextType contextType = ContextUtils.getContextTypeByName(contextItem.getContext(), connection.getContextName());
            List<ContextType> contextList = new ArrayList<ContextType>();
            List<String> contextIdList = new ArrayList<String>();
            contextList.add(contextType);
            contextIdList.add(contextId);
            if (contextList.size() > 0) {
                ContextLinkService.getInstance().saveContextLink(id, contextList, contextIdList, null);
                modified = true;
            }
        }
        return modified;
    }

    public boolean saveContextLink(ProcessType processType, String id) throws PersistenceException {
        boolean modified = false;
        List<ContextType> contextList = new ArrayList<ContextType>();
        List<String> contextIdList = new ArrayList<String>();
        Map<String, Set<String>> includeNameMap = new HashMap<String, Set<String>>();
        for (Object object : processType.getContext()) {
            if (object instanceof ContextType) {
                ContextType jobContextType = (ContextType) object;
                String repositoryContextId = getRepositoryContextId(jobContextType);
                if (StringUtils.isEmpty(repositoryContextId) || IContextParameter.BUILT_IN.equals(repositoryContextId)) {
                    continue;
                }
                Set<String> includeNameSet = new HashSet<String>();
                for (Object o : jobContextType.getContextParameter()) {
                    if (o instanceof ContextParameterType) {
                        includeNameSet.add(((ContextParameterType) o).getName());
                    }
                }
                includeNameMap.put(repositoryContextId, includeNameSet);
                ContextItem contextItem = ContextUtils.getContextItemById2(repositoryContextId);
                ContextType contextType = ContextUtils.getContextTypeByName(contextItem.getContext(), jobContextType.getName());
                contextList.add(contextType);
                contextIdList.add(repositoryContextId);
            }
        }
        if (contextList.size() > 0) {
            ContextLinkService.getInstance().saveContextLink(id, contextList, contextIdList, includeNameMap);
            modified = true;
        }
        return modified;
    }

    private String getRepositoryContextId(ContextType contextType) {
        for (Object o : contextType.getContextParameter()) {
            if (o instanceof ContextParameterType) {
                ContextParameterType param = (ContextParameterType) o;
                if (param.getRepositoryContextId() != null) {
                    return param.getRepositoryContextId();
                }
            }
        }
        return null;
    }

    private synchronized void saveContextLink(String id, List<ContextType> contextList, List<String> contextIdList,
            Map<String, Set<String>> includeNameMap) throws PersistenceException {
        File linkFile = calContextLinkFile(id);
        ItemContextLink processContext = new ItemContextLink();
        processContext.setItemId(id);
        Set<String> includeNameSet = null;

        for (int i = 0; i < contextList.size(); i++) {
            ContextType contextType = contextList.get(i);
            String contextId = contextIdList.get(i);
            ContextLink contextLink = new ContextLink();
            contextLink.setRepoId(contextId);
            contextLink.setContextName(contextType.getName());
            processContext.getContextList().add(contextLink);
            if (includeNameMap != null) {
                includeNameSet = includeNameMap.get(contextLink.getRepoId());
            }
            for (Object o : contextType.getContextParameter()) {
                if (o instanceof ContextParameterType) {
                    ContextParameterType param = (ContextParameterType) o;
                    if (includeNameSet == null || includeNameSet.contains(param.getName())) {
                        ContextParamLink paramLink = new ContextParamLink();
                        contextLink.getParameterList().add(paramLink);
                        paramLink.setName(param.getName());
                        paramLink.setId(ResourceHelper.getUUID(param));
                    }
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(linkFile, processContext);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public synchronized ItemContextLink loadContextLink(String id) throws PersistenceException {
        File linkFile = calContextLinkFile(id);
        if (linkFile == null || !linkFile.exists()) {
            return null;
        }
        ItemContextLink contextLink = null;
        try {
            contextLink = new ObjectMapper().readValue(linkFile, ItemContextLink.class);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
        return contextLink;
    }

    public synchronized void deleteContextLink(String id) throws PersistenceException {
        File linkFile = calContextLinkFile(id);
        if (linkFile != null && linkFile.exists()) {
            linkFile.delete();
        }
    }

    public File calContextLinkFile(String id) throws PersistenceException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return new File(getLinksFolder(), calLinkFileName(id));
    }

    private String calLinkFileName(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(LINK_FILE_POSTFIX);
        return sb.toString();
    }

    private File getLinksFolder() throws PersistenceException {
        IProject iProject = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel());
        IFolder settingFolder = iProject.getFolder(RepositoryConstants.SETTING_DIRECTORY);
        if (!settingFolder.exists()) {
            ResourceUtils.createFolder(settingFolder);
        }
        IFolder linksFolder = settingFolder.getFolder(LINKS_FOLDER_NAME);
        if (!linksFolder.exists()) {
            ResourceUtils.createFolder(linksFolder);
        }
        return new File(linksFolder.getLocationURI());
    }

}
