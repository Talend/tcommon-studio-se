// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package routines.system;

import java.util.List;
import java.util.ArrayList;

/**
 * Provides utility methods related to running of Java processes. 
 */
public abstract class ProcessUtils {

    private static final String[] RUNTIME_EXEC_COMMAND_CHARACTERS_TO_ESCAPE = new String[] { "\\", "\"" };

    /**
     * Escape arguments for execution of a Java process.
     * 
     * @param args command arguments to escape
     * @return translated command arguments
     */
    public static List<String> escapeRuntimeExecCommandArgs(final List<String> args) {
        if (EnvironmentUtils.isWindows()) {
            final List<String> result = new ArrayList<String>(args.size());
            for (String arg : args) {
                if (!arg.equals("%*")) {
                    arg = escapeArg(RUNTIME_EXEC_COMMAND_CHARACTERS_TO_ESCAPE, "\\", arg);
                }
                result.add(arg);
            }
            return result;
        }
        return args;
    }

    private static String escapeArg(final String[] charactersToEscape, String escapingCharacter, String arg) {
        for (String characterToEscape : charactersToEscape) {
            arg = arg.replace(characterToEscape, escapingCharacter + characterToEscape);
        }
        return arg;
    }
}
