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
package org.talend.updates.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InstallFeatureObserver {

    public static final Integer FEATURE_STATUS_TO_INSTALL = 0;

    public static final Integer FEATURE_STATUS_CANCELED = 1;

    public static final Integer FEATURE_STATUS_INSTALLED_SUCESSFULLY = 2;

    public static final Integer FEATURE_STATUS_INSTALLED_FAILED = 4;

    private static InstallFeatureObserver instance;

    private static final Map<String, Integer> toInstallFeatureStatus = new HashMap<String, Integer>();

    private InstallFeatureObserver() {
    }

    public static InstallFeatureObserver getInstance() {
        if (instance == null) {
            synchronized (InstallFeatureObserver.class) {
                if (instance == null) {
                    instance = new InstallFeatureObserver();
                }
            }
        }
        return instance;
    }

    public synchronized void updateInstallFeatureStatus(String featureName, int status) {
        if (featureName == null) {
            return;
        }
        toInstallFeatureStatus.put(featureName, status);
    }

    public synchronized boolean isNeedLanuchInstallWizard(String featureName) {
        if (toInstallFeatureStatus.containsKey(featureName)) {
            int status = toInstallFeatureStatus.get(featureName);
            if (FEATURE_STATUS_TO_INSTALL == status || FEATURE_STATUS_INSTALLED_SUCESSFULLY == status) {
                return false;
            }
        }
        return true;
    }

    public List<String> getInstalledFeatures() {
        List<String> featureNames = toInstallFeatureStatus.entrySet().stream()
                .filter(entry -> FEATURE_STATUS_INSTALLED_SUCESSFULLY == entry.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        return featureNames;
    }
}
