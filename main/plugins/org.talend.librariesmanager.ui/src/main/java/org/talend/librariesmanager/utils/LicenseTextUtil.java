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
package org.talend.librariesmanager.utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONObject;


public class LicenseTextUtil {

    private static final String LICENSE_FOLDER = "licenseTexts/";

    private static final String EXT_TXT = ".txt";

    private static final String UNKNOWN_LICENSE = "UNKNOWN";

    private static Map<String, String> licenseMap = new HashMap<>();

    private static final String LICENSE_MAP = "licenseMap.json";

    private static final Bundle bundle = LibManagerUiPlugin.BUNDLE;

    static {
        getMapFromJson(licenseMap);
    }

    public static void getMapFromJson(Map<String, String> licenseMap) {
        try {
            URL resourceURL = bundle.getEntry(LICENSE_FOLDER + LICENSE_MAP);
            File file = new File(FileLocator.toFileURL(resourceURL).getFile());
            if(file.exists()) {
                StringBuilder sb = new StringBuilder();
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {
                    sb.append(line);
                }
                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String url = jsonObject.getString("licenseUrl");
                    String name = jsonObject.getString("licenseName");
                    licenseMap.put(url.toLowerCase(), name.toUpperCase());
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static String getLicenseTextByUrl(String url) {
        String licenseType = licenseMap.get(url.toLowerCase());
        URL resourceURL = bundle.getEntry(LICENSE_FOLDER + licenseType + EXT_TXT);
        if (resourceURL == null) {
            resourceURL = bundle.getEntry(LICENSE_FOLDER + UNKNOWN_LICENSE + EXT_TXT);
        }
        try {
            File file = new File(FileLocator.toFileURL(resourceURL).getFile());
            if (file.exists()) {
                return getStringFromText(file);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    public static String getLicenseTextByName(String name) {
        URL resourceURL = bundle.getEntry(LICENSE_FOLDER + name.toUpperCase() + EXT_TXT);
        if (resourceURL == null) {
            return null;
        }
        try {
            File file = new File(FileLocator.toFileURL(resourceURL).getFile());
            if (file.exists()) {
                return getStringFromText(file);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    private static String getStringFromText(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
