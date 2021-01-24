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
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.ui.IInstalledPatchService;
import org.talend.updates.runtime.utils.PathUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SharedStudioPatchInfoProvider {

    private static final String INSTALLED_PATCH_RECORD_FILE = "installed_patch.json";

    static final String PATCH_TYPE_STUDIO = "studio";

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

    public boolean isInstalled(String patchName, String patchType) {
        for (InstalledPatch patchInfo : installedPatchInfo.getInstalledPatchList()) {
            if (StringUtils.equals(patchName, patchInfo.getFileName()) && StringUtils.equals(patchType, patchInfo.getType())) {
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
        if (!isInstalled(patchName, patchType)) {
            InstalledPatch patch = new InstalledPatch();
            patch.setFileName(patchName);
            patch.setType(patchType);
            installedPatchInfo.getInstalledPatchList().add(patch);
            saveData();  
        }

    }

    public File getNeedInstallStudioPatchFiles() {
        File patchFolder = PathUtils.getPatchesFolder();
        String patchName = getStudioInstalledLatestPatchFileName();
        if (patchFolder.exists() && patchFolder.isDirectory() && patchName != null) {
            File patchFile = new File (patchFolder, patchName);
            if (patchFile.exists() && !isInstalled(patchFile.getName(), PATCH_TYPE_STUDIO)) {
                return patchFile;
            }
        }
        return null;
    }

    public List<File> getNeedInstallCarFiles() {
        List<File> files = new ArrayList<File>();
        File patchFolder = PathUtils.getComponentsInstalledFolder();
        if (patchFolder.exists() && patchFolder.isDirectory()) {
            for (File file : patchFolder.listFiles()) {
                if (file.getName().endsWith(FileExtensions.CAR_EXTENSION) && !isInstalled(file.getName(), PATCH_TYPE_CAR)) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public String getStudioInstalledLatestPatchFileName() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IInstalledPatchService.class)) {
            IInstalledPatchService installedPatchService = GlobalServiceRegister.getDefault()
                    .getService(IInstalledPatchService.class);
            MavenArtifact artifact = installedPatchService.getLastIntalledP2Patch();
            if (artifact != null) {
                String artifactId = artifact.getArtifactId();
                if (!artifactId.endsWith(FileExtensions.ZIP_EXTENSION)) {
                    return artifactId + "." + FileExtensions.ZIP_EXTENSION;
                }
                return artifactId;
            }
        }
        return null;
    }
    
    public boolean updateArtifactsFileSha256Hex(String artifactsFileSha256Hex) {
        String savedCode = installedPatchInfo.getLastArtifactsFileSha256Hex();
        if (!StringUtils.equals(savedCode, artifactsFileSha256Hex)) {
            installedPatchInfo.setLastArtifactsFileSha256Hex(artifactsFileSha256Hex);
            saveData();
            return true;
        }
        return false;
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
    
    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("lastArtifactsFileSha256Hex")
    private String lastArtifactsFileSha256Hex;

    public List<InstalledPatch> getInstalledPatchList() {
        return installedPatchList;
    }

    public void setInstalledPatchList(List<InstalledPatch> installedPatchList) {
        this.installedPatchList = installedPatchList;
    }

    public String getLastArtifactsFileSha256Hex() {
        return lastArtifactsFileSha256Hex;
    }

    public void setLastArtifactsFileSha256Hex(String lastArtifactsFileSha256Hex) {
        this.lastArtifactsFileSha256Hex = lastArtifactsFileSha256Hex;
    }
}

class InstalledPatch {

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("fileName")
    private String fileName;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("type")
    private String type;

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
