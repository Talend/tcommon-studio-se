// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.general;

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * created by wchen on 2015年11月25日 Detailled comment
 *
 */
public class ModuleStatusProvider {

    static ModuleStatusProvider provider;

    /**
     * fix for TDI-34642: some ModuleNeeded instance status can not be reset , and always show as NOT_INSTALLED KEY:
     * mavenUri VALUE:ELibraryInstallStatus
     */
    private static Map<String, ELibraryInstallStatus> statusMap = new HashMap<String, ELibraryInstallStatus>();

    private static Map<String, ELibraryInstallStatus> deployStatusMap = new HashMap<String, ELibraryInstallStatus>();

    public static void putStatus(String mvnURI, ELibraryInstallStatus status) {
        statusMap.put(getKey(mvnURI), status);
    }

    public static ELibraryInstallStatus getStatus(String key) {
        return statusMap.get(getKey(key));
    }

    public static void putDeployStatus(String mvnURI, ELibraryInstallStatus status) {
        deployStatusMap.put(getKey(mvnURI), status);
    }

    public static ELibraryInstallStatus getDeployStatus(String key) {
        return deployStatusMap.get(getKey(key));
    }

    public static void reset() {
        statusMap.clear();
        deployStatusMap.clear();
    }

    /**
     * Get the map key
     * 
     * @param mvnURI
     * @return a key without user/password/repositoryURL, since they may be different
     */
    public static String getKey(String mvnURI) {
        String key = null;
        MavenArtifact ma = MavenUrlHelper.parseMvnUrl(mvnURI, false);
        if (ma != null) {
            // the key doesn't need the user/password/repositoryURL, since they may be different
            key = MavenUrlHelper.generateMvnUrl(ma.getGroupId(), ma.getArtifactId(), ma.getVersion(), ma.getType(),
                    ma.getClassifier());
        } else {
            key = mvnURI;
        }
        return key;
    }
}
