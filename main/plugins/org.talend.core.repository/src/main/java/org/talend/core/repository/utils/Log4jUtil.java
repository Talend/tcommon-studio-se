package org.talend.core.repository.utils;

import org.talend.core.PluginChecker;

public class Log4jUtil {

    public static boolean isEnable() {
        return PluginChecker.isTIS();
    }
}
