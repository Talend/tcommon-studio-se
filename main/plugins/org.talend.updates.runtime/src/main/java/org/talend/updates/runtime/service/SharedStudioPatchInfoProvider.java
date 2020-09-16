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
package org.talend.updates.runtime.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.IInstalledPatchService;
import org.talend.updates.runtime.utils.PathUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SharedStudioPatchInfoProvider {

    private static Logger log = Logger.getLogger(SharedStudioPatchInfoProvider.class);

    private static final String INSTALLED_PATCH_RECORD_FILE = "installed_patch.json";

    private static final String PATCH_TYPE_STUDIO = "studio";

    private static final String PATCH_TYPE_CAR = "car";

    private File dataFile = null;

    private InstalledPatchInfo installedPatchInfo;

    private static SharedStudioPatchInfoProvider instance;

    private SharedStudioPatchInfoProvider() {
        File configFolder = new File(Platform.getConfigurationLocation().getURL().getFile());
        dataFile = new File(configFolder, INSTALLED_PATCH_RECORD_FILE);
        loadData();
    }

    public static SharedStudioPatchInfoProvider getInstance() {
        if (instance == null) {
            synchronized (SharedStudioPatchInfoProvider.class) {
                if (instance == null) {
                    instance = new SharedStudioPatchInfoProvider();
                }
            }
        }
        return instance;
    }

    public boolean isInstall(String patchName, String patchType) {
        for (InstalledPatch patchInfo : installedPatchInfo.getInstalledPatchList()) {
            if (StringUtils.equals(patchName, patchInfo.getName()) && StringUtils.equals(patchType, patchInfo.getType())) {
                return true;
            }
        }
        return false;
    }

    public void installedStudioPatch(String patchName) {
        installedPatch(patchName, PATCH_TYPE_STUDIO);
    }

    public void installedCarPatch(String patchName) {
        installedPatch(patchName, PATCH_TYPE_CAR);
    }

    private void installedPatch(String patchName, String patchType) {
        InstalledPatch patch = new InstalledPatch();
        patch.setName(patchName);
        patch.setType(patchType);
        installedPatchInfo.getInstalledPatchList().add(patch);
        saveData();
    }

    public File getNeedInstallStudioPatchFiles() {
        File patchFolder = PathUtils.getPatchesFolder();
        String patchName = getStudioInstalledLatestPatch();
        if (patchFolder.exists() && patchFolder.isDirectory() && patchName != null && !isInstall(patchName, PATCH_TYPE_STUDIO)) {
            boolean isNeedInstall = false;
            for (InstalledPatch patchInfo : installedPatchInfo.getInstalledPatchList()) {
                if (StringUtils.equals(patchName, patchInfo.getName())) {
                    isNeedInstall = true;
                    break;
                }
            }
            if (isNeedInstall) {
                for (File file : patchFolder.listFiles()) {
                    if (file.getName().startsWith(patchName)) {
                        return file;
                    }
                }
            }
        }
        return null;
    }

    public List<File> getNeedInstallCarFiles() {
        List<File> files = new ArrayList<File>();
        File patchFolder = PathUtils.getComponentsInstalledFolder();
        if (patchFolder.exists() && patchFolder.isDirectory()) {
            for (File file : patchFolder.listFiles()) {
                if (!isInstall(file.getName(), PATCH_TYPE_CAR)) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    private String getStudioInstalledLatestPatch() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IInstalledPatchService.class)) {
            IInstalledPatchService installedPatchService = GlobalServiceRegister.getDefault()
                    .getService(IInstalledPatchService.class);
            return installedPatchService.getLatestInstalledVersion(false);
        }
        return null;
    }

    private void loadData() {
        TypeReference<InstalledPatchInfo> typeReference = new TypeReference<InstalledPatchInfo>() {
            // no need to overwrite
        };
        if (dataFile.exists()) {
            try {
                installedPatchInfo = new ObjectMapper().readValue(dataFile, typeReference);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        } else {
            log.warn("Can't find license data file:" + dataFile.getAbsolutePath());
        }
        if (installedPatchInfo == null) {
            installedPatchInfo = new InstalledPatchInfo();
        }
    }

    private synchronized void saveData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, installedPatchInfo);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}

class InstalledPatchInfo {

    @JsonProperty("installedPatch")
    private List<InstalledPatch> installedPatchList = new ArrayList<InstalledPatch>();

    public List<InstalledPatch> getInstalledPatchList() {
        return installedPatchList;
    }

    public void setInstalledPatchList(List<InstalledPatch> installedPatchList) {
        this.installedPatchList = installedPatchList;
    }
}

class InstalledPatch {

    @JsonProperty("name")
    private String name;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("type")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
