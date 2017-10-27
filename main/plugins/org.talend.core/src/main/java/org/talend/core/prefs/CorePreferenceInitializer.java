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
package org.talend.core.prefs;

import java.io.File;
import java.util.Locale;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.GeneralParametersProvider.GeneralParameters;

/**
 * Intializer of core preferences. <br/>
 * 
 * $Id$
 * 
 */
public class CorePreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String JAVA_LINUX_INTERPRETER_PATH = GeneralParametersProvider
            .getString(GeneralParameters.DEFAULT_JAVA_INTERPRETER_SUFFIX_LINUX);

    private static final String JAVA_WIN32_INTERPRETER = GeneralParametersProvider
            .getString(GeneralParameters.DEFAULT_JAVA_INTERPRETER_SUFFIX_WIN32);

    /**
     * Construct a new CorePreferenceInitializer.
     */
    public CorePreferenceInitializer() {
        super();
    }

    /**
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {

        IEclipsePreferences node = new DefaultScope().getNode(CorePlugin.getDefault().getBundle().getSymbolicName());

        // Building temporary files directory path
        IPath tempPath = new Path(System.getProperty("user.dir")).append("temp"); // NON-NLS-1$// NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
        File tempFile = tempPath.toFile();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        node.put(ITalendCorePrefConstants.FILE_PATH_TEMP, tempPath.toOSString());

        String os = Platform.getOS();
        String javaPath = System.getProperty("java.home"); // NON-NLS-1$ //$NON-NLS-1$
        if (os.equals(Platform.OS_WIN32)) {
            node.put(ITalendCorePrefConstants.JAVA_INTERPRETER, javaPath + JAVA_WIN32_INTERPRETER);
        } else if (os.equals(Platform.OS_LINUX)) {
            node.put(ITalendCorePrefConstants.JAVA_INTERPRETER, javaPath + JAVA_LINUX_INTERPRETER_PATH);
        }

        // Sets default language
        node.put(ITalendCorePrefConstants.LANGUAGE_SELECTOR, Locale.getDefault().getLanguage());

        node.put(ITalendCorePrefConstants.PREVIEW_LIMIT, "50"); //$NON-NLS-1$

        node.putBoolean(ITalendCorePrefConstants.ALWAYS_WELCOME, true);

        String languageType = Locale.getDefault().getLanguage();
        if (Locale.getDefault().equals(Locale.CHINA)) {
            languageType = Locale.SIMPLIFIED_CHINESE.toString();
        }

        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.LANGUAGE_SELECTOR, languageType);
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.SQL_ADD_QUOTE, false);

        // As default, sets the choice which automatic generate corresponding documentation files if job is saved to
        // true:
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.DOC_GENERATION, false);
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.USE_CSS_TEMPLATE, false);
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.CONTEXT_GROUP_BY_SOURCE, true);
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.DOC_GENERATESOURCECODE, false);
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.DOC_GENERATE_LASTEST_VERSION, false);
        // CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.DOC_HIDEPASSWORDS, true);

        //
        CorePlugin.getDefault().getPreferenceStore().setDefault(ITalendCorePrefConstants.SQL_ADD_WARNING, true);

        CorePlugin.getDefault().getPreferenceStore()
                .setDefault(ITalendCorePrefConstants.FORBIDDEN_MAPPING_LENGTH_PREC_LOGIC, false);

    }

}
