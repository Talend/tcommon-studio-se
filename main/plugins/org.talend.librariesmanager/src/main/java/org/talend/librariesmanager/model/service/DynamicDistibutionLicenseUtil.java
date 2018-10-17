// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.FileLocator;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * created by wchen on Sep 27, 2018
 * Detailled comment
 *
 */
public class DynamicDistibutionLicenseUtil {

    private static JSONArray licenseObjects;

    private synchronized static JSONArray loadLicenseObjects() throws URISyntaxException, IOException {
        BufferedReader br = null;
        JSONArray jsonArray = new JSONArray();
        try {
            File licenseFile = new File(
                    FileLocator
                            .toFileURL(
                                    DynamicDistibutionLicenseUtil.class.getClassLoader().getResource("distribution/license.json"))
                            .toURI());
            if (licenseFile.exists()) {
                br = new BufferedReader(new FileReader(licenseFile));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                jsonArray = JSONArray.fromObject(buffer.toString());
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return jsonArray;
    }

    public static void setupLicense(ModuleToInstall toInstall, MavenArtifact ma) {
        if (licenseObjects == null) {
            try {
                licenseObjects = loadLicenseObjects();
            } catch (Exception e) {
                ExceptionHandler.process(e);
                licenseObjects = new JSONArray();
            }
        }
        String newMvnUri = MavenUrlHelper.generateMvnUrl(ma.getGroupId(), ma.getArtifactId(), ma.getVersion(), ma.getType(),
                ma.getClassifier());
        boolean found = false;
        for (int i = 0; i < licenseObjects.size(); i++) {
            JSONObject jsonObject = licenseObjects.getJSONObject(i);
            if (newMvnUri.equals(jsonObject.getString("mvn_url"))) {
                toInstall.setLicenseType(jsonObject.getString("license"));
                String url = jsonObject.getString("license_url");
                toInstall.setLicenseUrl("".equals(url) ? null : url);
                found = true;
                break;
            }
        }
        if (!found) {
            // set default as APLV2
            toInstall.setLicenseType("Apache-2.0");
            toInstall.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0");
        }

    }

}
