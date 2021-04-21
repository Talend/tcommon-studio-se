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
package org.talend.commons.utils.time;

import java.io.File;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;

public class PropertiesCollectorUtil {

    private static final String ROOT_NODE = "additional_packages_records"; // additional packages

    private static final String PREF_ADDITIONAL_PACKAGES = "AdditionalPackages"; // preference node

    private static final String ADDITONAL_PACKAGE_FILE = "additionalPackages";


    public static String getAdditionalPackageRecording() {
        File file = getRecordFile(ADDITONAL_PACKAGE_FILE);

        Properties props = PropertiesFileUtil.read(file, true);
        String records = props.getProperty(ROOT_NODE, "");

        return records;
    }

    public static void storeAdditionalPackageRecording(String records) {
        File file = getRecordFile(ADDITONAL_PACKAGE_FILE);
        Properties props = PropertiesFileUtil.read(file, false);
        props.setProperty(ROOT_NODE, records);
        PropertiesFileUtil.store(file, props);
    }

    private static File getRecordFile(String fileName) {
        String configurationLocation = Platform.getConfigurationLocation().getURL().getPath();
        File file = new File(configurationLocation + "/data_collector/" + fileName);
        return file;
    }

    public static String getAdditionalPackagePreferenceNode() {
        return PREF_ADDITIONAL_PACKAGES;
    }

}
