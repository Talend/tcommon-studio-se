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
package org.talend.repository.token;

import java.io.File;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.talend.commons.utils.time.PropertiesFileUtil;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class RepositoryActionLogger {

    private static String recordingFileName = "actions";

    private static File recordingFile;
    public static final String PREFIX_PERSPECITVE = "perspective.";
    public static final String PREFIX_ACTION = "action.";

    public static File getRecordingFile() {
        if (recordingFile == null) {
            String configurationLocation = Platform.getConfigurationLocation().getURL().getPath();
            recordingFile = new File(configurationLocation + "/data_collector/" + recordingFileName);
        }
        return recordingFile;
    }

    private static void log(String action) {
        new Thread(() -> {
            Properties props = PropertiesFileUtil.read(getRecordingFile(), false);
            int count = Integer.parseInt(props.getProperty(action, "0"));
            props.put(action, count + 1 + "");
            PropertiesFileUtil.store(getRecordingFile(), props);
        }).start();
    }
    
    public static void logPerspective(String perspective) {
        log(PREFIX_PERSPECITVE + perspective);
    }
    
    public static void logAction(String action) {
        log(PREFIX_ACTION + action);
    }
    
}
