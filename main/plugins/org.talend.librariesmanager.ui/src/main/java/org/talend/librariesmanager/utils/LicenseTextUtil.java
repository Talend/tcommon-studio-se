package org.talend.librariesmanager.utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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
        StringBuilder sb = new StringBuilder();
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
