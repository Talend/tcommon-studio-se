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
package org.talend.librariesmanager.librarydata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LibraryDataJsonProvider {

    private static Logger log = Logger.getLogger(LibraryDataJsonProvider.class);

    private File dataFile;

    public LibraryDataJsonProvider(File dataFile) {
        this.dataFile = dataFile;
    }

    public Map<String, Library> loadLicenseData() {
        Map<String, Library> mvnToLibraryMap = new HashMap<String, Library>();
        TypeReference<Libraries> typeReference = new TypeReference<Libraries>() {
            // no need to overwrite
        };
        if (dataFile.exists()) {
            try {
                Libraries libraries = new ObjectMapper().readValue(dataFile, typeReference);
                mvnToLibraryMap.clear();
                for (Library obj : libraries.getLibraryList()) {
                    mvnToLibraryMap.put(obj.getMvnUrl(), obj);
                }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        } else {
            log.warn("Can't find license data file:" + dataFile.getAbsolutePath());
        }

        return mvnToLibraryMap;
    }

    public synchronized void saveLicenseData(Map<String, Library> mvnToLibraryMap) {
        Libraries libraries = new Libraries();
        for (String mvnUrl : mvnToLibraryMap.keySet()) {
            Library library = mvnToLibraryMap.get(mvnUrl);
            libraries.getLibraryList().add(library);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, libraries);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}

class Libraries {

    @JsonProperty("library")
    private List<Library> libraryList = new ArrayList<Library>();

    public List<Library> getLibraryList() {
        return libraryList;
    }

    public void setLibraryList(List<Library> libraryList) {
        this.libraryList = libraryList;
    }
}

class Library {

    @JsonProperty("mvnUrl")
    private String mvnUrl;

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("artifactId")
    private String artifactId;

    @JsonProperty("version")
    private String version;

    @JsonProperty("type")
    private String type;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("classifier")
    private String classifier;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("url")
    private String url;

    @JsonProperty("licenseMissing")
    @JsonInclude(Include.NON_DEFAULT)
    private boolean licenseMissing = false;

    @JsonProperty("pomMissing")
    @JsonInclude(Include.NON_DEFAULT)
    private boolean pomMissing = false;

    @JsonProperty("jarMissing")
    @JsonInclude(Include.NON_DEFAULT)
    private boolean jarMissing = false;

    @JsonProperty("licenses")
    List<LibraryLicense> licenses = new ArrayList<LibraryLicense>();

    public Library() {
    }

    public String getMvnUrl() {
        return mvnUrl;
    }

    public void setMvnUrl(String mvnUrl) {
        this.mvnUrl = mvnUrl;
    }

    public List<LibraryLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LibraryLicense> licenses) {
        this.licenses = licenses;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLicenseMissing() {
        return licenseMissing;
    }

    public void setLicenseMissing(boolean licenseMissing) {
        this.licenseMissing = licenseMissing;
    }

    public boolean isJarMissing() {
        return jarMissing;
    }

    public void setJarMissing(boolean jarMissing) {
        this.jarMissing = jarMissing;
    }

    public boolean isPomMissing() {
        return pomMissing;
    }

    public void setPomMissing(boolean pomMissing) {
        this.pomMissing = pomMissing;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mvnUrl: ").append(mvnUrl);
        sb.append("groupId: ").append(groupId);
        sb.append("artifactId: ").append(artifactId);
        sb.append("version: ").append(version);
        sb.append("type: ").append(type);
        for (int i = 0; i < licenses.size(); i++) {
            sb.append("License " + i + ": " + licenses.get(i).toString());
        }
        return sb.toString();
    }
}

class LibraryLicense {

    @JsonProperty("name")
    private String name;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("url")
    private String url;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("distribution")
    private String distribution;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("comments")
    private String comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name == null ? "" : name);
        sb.append("name: ").append(url == null ? "" : url);
        sb.append("distribution: ").append(distribution == null ? "" : distribution);
        sb.append("comments: ").append(comments == null ? "" : comments);
        return sb.toString();
    }

}
