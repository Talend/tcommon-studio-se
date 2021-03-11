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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

import net.sf.json.JSONObject;

/**
 * created by wchen on Aug 18, 2017 Detailled comment
 *
 */
public class CustomUriManager {

    private JSONObject customURIObject;

    private static CustomUriManager manager = new CustomUriManager();;

    private static boolean isNeedReload = false;

    private final Object reloadingLock = new Object();

    private CustomUriManager() {
        try {
            customURIObject = loadResources(getResourcePath(), RepositoryConstants.PROJECT_SETTINGS_CUSTOM_URI_MAP);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static CustomUriManager getInstance() {
        return manager;
    }

    private JSONObject loadResources(String path, String fileName) throws IOException {
        JSONObject jsonObj = new JSONObject();
        InputStream in = null;
        try {
            File file = new File(path, fileName);
            if (file.exists()) {
                jsonObj = loadResources(new FileInputStream(file), fileName);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return jsonObj;
    }

    private JSONObject loadResources(IFolder path, String fileName) throws CoreException, IOException {
        JSONObject jsonObj = new JSONObject();
        InputStream in = null;
        try {
            IFile file = path.getFile(fileName);
            FilesUtils.executeFolderAction(new NullProgressMonitor(), path, new IWorkspaceRunnable() {

                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    file.refreshLocal(IResource.DEPTH_ZERO, monitor);
                }
            });
            if (file.isAccessible()) {
                in = file.getContents(true);
                return loadResources(in, fileName);
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
        return jsonObj;
    }

    private JSONObject loadResources(InputStream in, String fileName) throws IOException {
        BufferedReader br = null;
        JSONObject jsonObj = new JSONObject();
        try {
            br = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            jsonObj = JSONObject.fromObject(buffer.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return jsonObj;
    }

    private void saveResource(JSONObject customMap, IFolder filePath, String fileName, boolean isExport) {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        try {
            IFile file = filePath.getFile(fileName);
            out = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, customMap);
            in = new ByteArrayInputStream(out.toByteArray());
            final InputStream fin = in;
            FilesUtils.executeFolderAction(new NullProgressMonitor(), file.getParent(), new IWorkspaceRunnable() {

                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    file.refreshLocal(IResource.DEPTH_ZERO, monitor);
                    if (!file.exists()) {
                        file.create(fin, false, monitor);
                    } else {
                        file.setContents(fin, true, false, monitor);
                    }
                }
            });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
//                    ExceptionHandler.process(ex);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
//                    ExceptionHandler.process(ex);
                }
            }
        }
    }

    private void saveResource(JSONObject customMap, String filePath, String fileName, boolean isExport) {
        try {
            File file = new File(filePath, fileName);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, customMap);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public void saveCustomURIMap() {
        final RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "Save custom maven uri map") {

            @Override
            public void run() throws PersistenceException, LoginException {
                saveResource(customURIObject, getResourcePath(), RepositoryConstants.PROJECT_SETTINGS_CUSTOM_URI_MAP, false);
            }
        };
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        repositoryWorkUnit.setAvoidUnloadResources(true);
        repositoryWorkUnit.setFilesModifiedOutsideOfRWU(true);
        repositoryWorkUnit.setForceTransaction(true);
        factory.executeRepositoryWorkUnit(repositoryWorkUnit);

    }

    private IFolder getResourcePath() {
        try {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            IProject project = ResourceUtils.getProject(currentProject);
            return project.getFolder(".settings");
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    public void put(String key, String value) {
        try {
            reloadCustomMapping();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (value != null) {
            customURIObject.put(key, value);
        } else {
            customURIObject.remove(key);
        }
    }

    public String get(String key) {
        try {
            reloadCustomMapping();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (customURIObject.containsKey(key)) {
            return customURIObject.getString(key);
        }
        return null;
    }

    public Set<String> keySet() {
        return customURIObject.keySet();
    }

    public void importSettings(String filePath, String fileName) throws Exception {
        JSONObject loadResources = loadResources(filePath, fileName);
        customURIObject.clear();
        if (loadResources != null) {
            customURIObject.putAll(loadResources);
        }
        saveCustomURIMap();
    }

    public void exportSettings(String filePath, String fileName) {
        saveResource(customURIObject, filePath, fileName, true);
    }

    public void reloadCustomMapping() {
        try {
            if (isNeedReload) {
                synchronized (reloadingLock) {
                    if (isNeedReload) {
                        customURIObject.clear();
                        JSONObject loadResources = loadResources(getResourcePath(),
                                RepositoryConstants.PROJECT_SETTINGS_CUSTOM_URI_MAP);
                        customURIObject.putAll(loadResources);
                        isNeedReload = false;
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
    
    public void setForeceReloadCustomUri() {
    	isNeedReload = true;
    }
}
