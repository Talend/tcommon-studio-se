package org.talend.librariesmanager.utils;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class LicenseTextUtil {

    private static final String PLUGIN_ID = "org.talend.librariesmanager.ui";

    private static final String LICENSE_FOLDER = "licenseTexts/";

    private static final String EXT_TXT = ".txt";

    private static final String UNKNOWN_LICENSE = "UNKNOWN";


    public static String getLicenseTextByType(String licenseType) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        URL resourceURL = bundle.getEntry(LICENSE_FOLDER + licenseType + EXT_TXT);
        if (resourceURL == null) {
            return UNKNOWN_LICENSE;
        }
        try {
            File file = new File(FileLocator.toFileURL(resourceURL).getFile());
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UNKNOWN_LICENSE;
    }
}
