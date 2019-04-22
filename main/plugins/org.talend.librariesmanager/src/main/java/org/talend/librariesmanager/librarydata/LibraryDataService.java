// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.util.MavenLibraryResolverProvider;

public class LibraryDataService {

    private static Logger logger = Logger.getLogger(LibraryDataService.class);

    /**
     * {@value}
     * <p>
     * System property of build studio library license file, the default value is <b>false</b>.
     */
    public static final String KEY_LIBRARIES_BUILD_LICENSE = "talend.libraries.buildLicense"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of build studio library file, the default value is <b>false</b>.
     */
    public static final String KEY_LIBRARIES_BUILD_JAR = "talend.libraries.buildJar"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of studio library data file folder path, the default value is Studio install
     * folder/configuration.
     */
    public static final String KEY_LIBRARIES_DATA_FOLDER = "talend.libraries.folder"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of retrieve library data, the default value is <b>true</b>.
     */
    public static final String KEY_BUILD_LIBRARY_IF_MISSING = "talend.libraries.buildIfMissing"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of retrieve library data, the default value is <b>false</b>.
     */
    public static final String KEY_BUILD_LIBRARY_IF_LICENSE_MISSING = "talend.libraries.buildIfLicenseMissing"; //$NON-NLS-1$

    private static final String LIBRARIES_DATA_FILE_NAME = "library_data.index"; //$NON-NLS-1$

    private static final String UNRESOLVED_LICENSE_NAME = "UNKNOW"; //$NON-NLS-1$

    private static boolean buildLibraryIfFileMissing = true;

    private boolean buildLibraryLicense = false;

    private boolean buildLibraryIfLibraryMissing = true;

    private boolean buildLibraryIfLicenseMissing = false;

    private boolean buildLibraryJarFile = false;

    private static final Map<String, Library> mvnToLibraryMap = new ConcurrentHashMap<String, Library>();

    private static LibraryDataService instance;

    private LibraryDataJsonProvider dataProvider;

    private Set<String> retievedMissingSet = new HashSet<String>();

    private int repeatTime = 3;

    private LibraryLicense unknownLicense;;

    private LibraryDataService() {
        buildLibraryLicense = Boolean.valueOf(System.getProperty(KEY_LIBRARIES_BUILD_LICENSE, Boolean.FALSE.toString()));
        buildLibraryIfLibraryMissing = Boolean.valueOf(System.getProperty(KEY_BUILD_LIBRARY_IF_MISSING, Boolean.TRUE.toString()));
        buildLibraryIfLicenseMissing = Boolean
                .valueOf(System.getProperty(KEY_BUILD_LIBRARY_IF_LICENSE_MISSING, Boolean.FALSE.toString()));
        buildLibraryJarFile = Boolean.valueOf(System.getProperty(KEY_LIBRARIES_BUILD_JAR, Boolean.FALSE.toString()));
        File libraryDataFile = getLibraryDataFile();
        if (buildLibraryLicense) {
            if (libraryDataFile.exists()) {
                libraryDataFile.delete();
            }
        }
        unknownLicense = new LibraryLicense();
        unknownLicense.setName(UNRESOLVED_LICENSE_NAME);
        dataProvider = new LibraryDataJsonProvider(libraryDataFile);
        mvnToLibraryMap.putAll(dataProvider.loadLicenseData());
    }

    public static LibraryDataService getInstance() {
        if (instance == null) {
            synchronized (LibraryDataService.class) {
                if (instance == null) {
                    instance = new LibraryDataService();
                }
            }
        }
        return instance;
    }

    public void buildLibraryLicenseData(Set<String> mvnUrlList) {
        for (String mvnUrl : mvnUrlList) {
            Library libraryObj = resolve(mvnUrl);
            mvnToLibraryMap.put(getShortMvnUrl(mvnUrl), libraryObj);

        }
        dataProvider.saveLicenseData(mvnToLibraryMap);
    }

    private Library resolve(String mvnUrl) {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
        Library libraryObj = new Library();
        libraryObj.setGroupId(artifact.getGroupId());
        libraryObj.setArtifactId(artifact.getArtifactId());
        libraryObj.setVersion(artifact.getVersion());
        libraryObj.setMvnUrl(getShortMvnUrl(mvnUrl));
        libraryObj.setType(artifact.getType());
        libraryObj.setClassifier(artifact.getClassifier());
        boolean hasError = false;
        logger.debug("Resolving artifact descriptor:" + getShortMvnUrl(mvnUrl)); //$NON-NLS-1$
        for (int repeated = 0; repeated < repeatTime; repeated++) {
            try {
                if (repeated > 0) {
                    Thread.sleep(1000); // To avoid server connection pool shut down
                }
                Map<String, Object> properties = resolveDescProperties(artifact);
                parseDescriptorResult(libraryObj, properties);
                hasError = false;
            } catch (Exception e) {
                hasError = true;
            }
            if (!hasError) {
                break;
            }
        }
        logger.debug("Resolved artifact descriptor:" + getShortMvnUrl(mvnUrl)); //$NON-NLS-1$
        if (buildLibraryJarFile) {
            boolean jarMissing = false;
            try {
                MavenLibraryResolverProvider.getInstance().resolveArtifact(artifact);
            } catch (Exception ex) {
                jarMissing = true;
            } finally {
                libraryObj.setJarMissing(jarMissing);
            }
        }

        return libraryObj;
    }

    public Map<String, Object> resolveDescProperties(MavenArtifact artifact) throws Exception {
        Map<String, Object> properties = MavenLibraryResolverProvider.getInstance().resolveDescProperties(artifact);
        return properties;
    }

    public void fillArtifactPropertyData(Map<String, Object> properties, MavenArtifact artifact) {
        Library libraryObj = new Library();
        libraryObj.setGroupId(artifact.getGroupId());
        libraryObj.setArtifactId(artifact.getArtifactId());
        libraryObj.setVersion(artifact.getVersion());
        libraryObj.setMvnUrl(artifact.getUrl());
        libraryObj.setType(artifact.getType());
        libraryObj.setClassifier(artifact.getClassifier());

        parseDescriptorResult(libraryObj, properties);
        fillLibraryData(libraryObj, artifact);
    }

    private boolean isPackagePom(Library libraryObj) {
        if (libraryObj != null) {
            if ("pom".equalsIgnoreCase(libraryObj.getType())) {
                return true;
            }
            for (LibraryLicense license : libraryObj.getLicenses()) {
                if (MavenConstants.DOWNLOAD_MANUAL.equalsIgnoreCase(license.getDistribution())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void parseDescriptorResult(Library libraryObj, Map<String, Object> properties) {
        if (properties.size() == 0) {
            libraryObj.setPomMissing(true);
        }
        libraryObj.setType(String.valueOf(properties.get("type"))); //$NON-NLS-1$
        int licenseCount = 0;
        if (properties.containsKey("license.count")) { //$NON-NLS-1$
            licenseCount = (int) properties.get("license.count"); //$NON-NLS-1$
        }
        for (int i = 0; i < licenseCount; i++) {
            String name = getStringValue(properties.get("license." + i + ".name")); //$NON-NLS-1$ //$NON-NLS-2$
            String url = getStringValue(properties.get("license." + i + ".url")); //$NON-NLS-1$ //$NON-NLS-2$
            String comments = getStringValue(properties.get("license." + i + ".comments")); //$NON-NLS-1$ //$NON-NLS-2$
            String distribution = getStringValue(properties.get("license." + i + ".distribution")); //$NON-NLS-1$ //$NON-NLS-2$

            LibraryLicense license = new LibraryLicense();
            license.setName(name);
            license.setUrl(url);
            license.setComments(comments);
            license.setDistribution(distribution);
            libraryObj.getLicenses().add(license);
        }

        if (libraryObj.getLicenses().size() == 0) {
            libraryObj.setLicenseMissing(true);
            libraryObj.getLicenses().add(unknownLicense);
        }
    }

    private String getStringValue(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public boolean isBuildLibrariesData() {
        if (buildLibraryLicense) {
            return true;
        }
        if (buildLibraryIfFileMissing && !getLibraryDataFile().exists()) {
            return true;
        }
        return false;
    }

    private File getLibraryDataFile() {
        String folder = System.getProperty(KEY_LIBRARIES_DATA_FOLDER);
        if (folder == null) {
            folder = new File(Platform.getInstallLocation().getURL().getPath(), "configuration").getAbsolutePath(); //$NON-NLS-1$
        }
        return new File(folder, LIBRARIES_DATA_FILE_NAME);
    }

    public boolean fillLibraryData(String mvnUrl, MavenArtifact artifact) {
        boolean isExist = false;
        String shortMvnUrl = getShortMvnUrl(mvnUrl);
        Library object = mvnToLibraryMap.get(shortMvnUrl);
        if (!retievedMissingSet.contains(mvnUrl) && ((object == null && buildLibraryIfLibraryMissing)
                || (object.isLicenseMissing() && buildLibraryIfLicenseMissing))) {
            Library newObject = null;
            retievedMissingSet.add(mvnUrl);
            try {
                newObject = resolve(mvnUrl);
            } catch (Exception e) {
                logger.warn("Resolve pom failed:" + shortMvnUrl); //$NON-NLS-1$
            }
            if (newObject != null && (evaluateLibrary(newObject) > evaluateLibrary(object))) {
                object = newObject;
                mvnToLibraryMap.put(shortMvnUrl, object);
                dataProvider.saveLicenseData(mvnToLibraryMap);
            }
        }
        if (object != null) {
            if ("jar".equalsIgnoreCase(object.getType()) && !object.isJarMissing() && !object.isPomMissing()) {
                isExist = true;
            }
            fillLibraryData(object, artifact);
        }
        return isExist;
    }

    public void fillLibraryData(Library object, MavenArtifact artifact) {
        if (object != null && artifact != null) {
            List<LibraryLicense> objLicenseList = object.getLicenses();
            LibraryLicense bestLicense = null;
            if (objLicenseList.size() >= 1) {
                bestLicense = objLicenseList.get(0);
            }
            if (bestLicense != null) {
                artifact.setLicense(bestLicense.getName());
                artifact.setLicenseUrl(bestLicense.getUrl());
            }

            // URL
            if (StringUtils.isEmpty(artifact.getUrl()) && StringUtils.isNotEmpty(object.getUrl())) {
                artifact.setUrl(object.getUrl());
            }
            if (isPackagePom(object)) {
                artifact.setDistribution(MavenConstants.DOWNLOAD_MANUAL);
            }
        }
    }

    private String getShortMvnUrl(String mvnUrl) {
        if (mvnUrl.indexOf("@") > 0 && mvnUrl.indexOf("!") > 0) {
            mvnUrl = "mvn:" + mvnUrl.substring(mvnUrl.indexOf("!") + 1);
        }
        return mvnUrl;
    }

    private int evaluateLibrary(Library obj1) {
        int score = -1;
        if (obj1 != null) {
            score++;
            if (!obj1.isJarMissing()) {
                score++;
            }
            if (!obj1.isLicenseMissing()) {
                score += 2;
            }
        }
        return score;
    }
}
