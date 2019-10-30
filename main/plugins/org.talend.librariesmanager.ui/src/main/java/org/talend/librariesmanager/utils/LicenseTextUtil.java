package org.talend.librariesmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;

public class LicenseTextUtil {

    private static final String LICENSE_FOLDER = "licenseTexts/";

    private static final String EXT_TXT = ".txt";

    private static final String UNKNOWN_LICENSE = "UNKNOWN";


    public static String getLicenseTextByType(String licenseType) {
        Bundle bundle = LibManagerUiPlugin.BUNDLE;
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

    private static String getStringFromText(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}
