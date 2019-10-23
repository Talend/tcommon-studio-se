package org.talend.librariesmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LicenseTextUtil {

    private static final String PLUGIN_ID = "org.talend.librariesmanager.ui";

    private static final String LICENSE_TYPE = "type";

    private static final String LICENSE_URL = "url";

    private static final String LICENSE_TEXT = "text";

    public static String getLicenseTextByLicenseType(String licenseType) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
         URL resourceURL = bundle.getEntry("LicenseText.json");
        BufferedReader br = null;
        Map<String, String> licenseMap = new HashMap<>(0);
        try {
            File file = new File(FileLocator.toFileURL(resourceURL).getFile());
            JSONArray jsonArray = null;
            if (file.exists()) {
                jsonArray = new JSONArray();
                br = new BufferedReader(new FileReader(file));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                jsonArray = JSONArray.fromObject(buffer.toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String type = object.getString(LICENSE_TYPE);
                    String licenseText = object.getString(LICENSE_TEXT);
                    licenseMap.put(type, licenseText);
                }
                return licenseMap.get(licenseType);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {

            }
        }
        return null;
    }
}
