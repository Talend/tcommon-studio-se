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
package org.talend.commons.utils.generation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * Utilities around perl stuff. <br/>
 * 
 * $Id: JavaUtils.java 1804 2007-02-04 09:50:15Z rli $
 * 
 */
public final class JavaUtils {

    public static final String JAVAMODULE_PLUGIN_ID = "org.talend.designer.codegen.javamodule"; //$NON-NLS-1$

    public static final String JAVA_LAUNCHCONFIGURATION = "org.talend.designer.runprocess.launchConfigurationJava"; //$NON-NLS-1$

    public static final String PROJECT_JAVA_VERSION_KEY = "talend.project.java.version"; //$NON-NLS-1$

    public static final String DEFAULT_VERSION = JavaCore.VERSION_1_8;

    public static final List<String> AVAILABLE_VERSIONS = Arrays.asList( JavaCore.VERSION_1_7, JavaCore.VERSION_1_8 );
    
    public static final String PROCESSOR_TYPE = "javaProcessor"; //$NON-NLS-1$

    public static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    public static final String JAVA_APP_NAME = "java";//$NON-NLS-1$

    /** added by rxl. */
    public static final String JAVATIP = "//The function of generating Java code haven't achive yet" //$NON-NLS-1$
            + System.getProperty("line.separator") + "public class JavaTest extends Test {}"; //$NON-NLS-1$ //$NON-NLS-2$

    /** Java File Extension. */
    public static final String JAVA_EXTENSION = ".java"; //$NON-NLS-1$

    /** drl File Extension. */
    public static final String DRL_EXTENSION = ".drl"; //$NON-NLS-1$

    /** Item File Extension. */
    public static final String ITEM_EXTENSION = ".item"; //$NON-NLS-1$

    /** Java sqltemplate Extension. */
    public static final String JAVA_SQLPATTERN_EXTENSION = ".sqltemplate"; //$NON-NLS-1$

    /** Java Context Extension. */
    public static final String JAVA_CONTEXT_EXTENSION = ".properties"; //$NON-NLS-1$

    /** Java Directory. */
    public static final String JAVA_DIRECTORY = "java"; //$NON-NLS-1$

    /** Java Routines Directory. */
    public static final String JAVA_ROUTINES_DIRECTORY = "routines"; //$NON-NLS-1$

    /** Java Pig Directory. */
    public static final String JAVA_PIG_DIRECTORY = "pig"; //$NON-NLS-1$

    /** Java Pig UDF Directory. */
    public static final String JAVA_PIGUDF_DIRECTORY = "pigudf"; //$NON-NLS-1$

    /** Java Beans Directory. */
    public static final String JAVA_BEANS_DIRECTORY = "beans"; //$NON-NLS-1$

    /** Java SQLTemplate Directory. */
    public static final String JAVA_SQLPATTERNS_DIRECTORY = "sqltemplates"; //$NON-NLS-1$

    /** Java system Directory. */
    public static final String JAVA_SYSTEM_DIRECTORY = "system"; //$NON-NLS-1$

    /** Java Routines api Directory. */
    public static final String JAVA_SYSTEM_ROUTINES_API_DIRECTORY = "api"; //$NON-NLS-1$

    /** Java UserDefined Directory */
    public static final String JAVA_USER_DEFINED = "user defined"; //$NON-NLS-1$

    /** Java Lib Directory. */
    public static final String JAVA_LIB_DIRECTORY = "lib"; //$NON-NLS-1$

    /** Java internal Directory. */
    public static final String JAVA_INTERNAL_DIRECTORY = "internal"; //$NON-NLS-1$

    /** Java DB Mapping Directory , and must be same as MetadataTalendType.MAPPING_FOLDER */
    public static final String JAVA_XML_MAPPING = "xmlMappings"; //$NON-NLS-1$

    /** Java Rules Directory. */
    public static final String JAVA_RULES_DIRECTORY = "rules"; //$NON-NLS-1$

    /** Java Rules Template Directory. */
    public static final String JAVA_RULES_TEMPLATE_DIRECTORY = "template"; //$NON-NLS-1$

    /** Java Metadata Directory. */
    public static final String JAVA_METADATA_DIRECTORY = "metadata"; //$NON-NLS-1$

    /** Java contexts Directory. */
    public static final String JAVA_CONTEXTS_DIRECTORY = "contexts"; //$NON-NLS-1$

    /** Java datass Directory. */
    public static final String JAVA_DATAS_DIRECTORY = "datas"; //$NON-NLS-1$

    /** Java ClassPath Separator. */
    public static final String JAVA_CLASSPATH_SEPARATOR = (Platform.getOS().compareTo(Platform.WS_WIN32) == 0) ? ";" : ":"; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String JAVA_CP = "-cp"; //$NON-NLS-1$ 

    public static final String ROUTINE_JAR_DEFAULT_VERSION = "1.0";//$NON-NLS-1$

    public static final String ROUTINE_JAR_NAME = "routines"; //$NON-NLS-1$

    public static final String BEANS_JAR_NAME = "beans"; //$NON-NLS-1$

    public static final String PIGUDFS_JAR_NAME = "pigudfs"; //$NON-NLS-1$

    public static final String ROUTINES_JAR = ROUTINE_JAR_NAME + FileExtensions.JAR_FILE_SUFFIX;

    public static final String BEANS_JAR = BEANS_JAR_NAME + FileExtensions.JAR_FILE_SUFFIX;

    public static final String PIGUDFS_JAR = PIGUDFS_JAR_NAME + FileExtensions.JAR_FILE_SUFFIX;

    /*
     * for old build system JobJavaScriptsManager
     */
    public static final String SYSTEM_ROUTINE_JAR = "systemRoutines" + FileExtensions.JAR_FILE_SUFFIX; //$NON-NLS-1$

    public static final String USER_ROUTINE_JAR = "userRoutines" + FileExtensions.JAR_FILE_SUFFIX; //$NON-NLS-1$

    public static final String USER_BEANS_JAR = "userBeans" + FileExtensions.JAR_FILE_SUFFIX; //$NON-NLS-1$

    public static final String USER_PIGUDF_JAR = "pigudf" + FileExtensions.JAR_FILE_SUFFIX; //$NON-NLS-1$

    /**
     * DOC ycbai Get default jvm name.
     * 
     * @return
     */
    public static String getDefaultJVMName() {
        IVMInstall install = JavaRuntime.getDefaultVMInstall();
        if (install != null) {
            return install.getName();
        } else {
            return "nothing!";
        }
    }

    /**
     * DOC ycbai Get default javaEE name.
     * 
     * @return
     */
    public static String getDefaultEEName() {
        IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();

        IExecutionEnvironment[] environments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
        if (defaultVM != null) {
            for (IExecutionEnvironment environment : environments) {
                IVMInstall eeDefaultVM = environment.getDefaultVM();
                if (eeDefaultVM != null && defaultVM.getId().equals(eeDefaultVM.getId())) {
                    return environment.getId();
                }
            }
        }

        String defaultCC;
        if (defaultVM instanceof IVMInstall2) {
            defaultCC = getCompilerCompliance((IVMInstall2) defaultVM, JavaCore.VERSION_1_4);
        } else {
            defaultCC = JavaCore.VERSION_1_4;
        }

        for (IExecutionEnvironment environment : environments) {
            String eeCompliance = getExecutionEnvironmentCompliance(environment);
            if (defaultCC.endsWith(eeCompliance)) {
                return environment.getId();
            }
        }

        return "J2SE-1.6"; //$NON-NLS-1$
    }

    /**
     * DOC ycbai Get compiler compliance via vminstall and default compliance.
     * 
     * @param vMInstall
     * @param defaultCompliance
     * @return
     */
    public static String getCompilerCompliance(IVMInstall2 vMInstall, String defaultCompliance) {
        String version = vMInstall.getJavaVersion();
        return getJavaVersion(defaultCompliance, version);
    }
    
    public static String getProjectJavaVersion() {
        String javaVersion = CoreRuntimePlugin.getInstance().getProjectPreferenceManager().getValue(PROJECT_JAVA_VERSION_KEY);
        if (javaVersion != null && javaVersion.trim().equals("")) { //$NON-NLS-1$
            javaVersion = null;
        }
        return javaVersion;
    }
    
    public static void updateProjectJavaVersion(String javaVersion) {
        setProjectJavaVserion(javaVersion);
        applyCompilerCompliance(javaVersion);
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            service.updateProjectPomWithTemplate();
        }
    }
    
    private static void setProjectJavaVserion(String javaVersion) {
        if (javaVersion != null) {
            ProjectPreferenceManager manager = CoreRuntimePlugin.getInstance().getProjectPreferenceManager();
            manager.setValue(PROJECT_JAVA_VERSION_KEY, javaVersion);
            manager.save();
        }
    }
    
    private static void applyCompilerCompliance(String compliance) {
        if (compliance != null) {
            IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(JavaCore.PLUGIN_ID);
            Map<String, String> complianceOptions = new HashMap<String, String>();
            JavaCore.setComplianceOptions(compliance, complianceOptions);
            if (!complianceOptions.isEmpty()) {
                Set<Entry<String, String>> entrySet = complianceOptions.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    eclipsePreferences.put(entry.getKey(), entry.getValue());
                }
            }
            try {
                eclipsePreferences.flush();
            } catch (BackingStoreException e) {
                ExceptionHandler.process(e);
            }
        }
    }
    
    private static String getJavaVersion(String defaultCompliance, String version) {
        if (version == null) {
            return defaultCompliance;
        }
        if (version.startsWith(JavaCore.VERSION_1_8)) {
            return JavaCore.VERSION_1_8;
        }
        if (version.startsWith(JavaCore.VERSION_1_7)) {
            return JavaCore.VERSION_1_7;
        }
        if (version.startsWith(JavaCore.VERSION_1_6)) {
            return JavaCore.VERSION_1_6;
        }
        if (version.startsWith(JavaCore.VERSION_1_5)) {
            return JavaCore.VERSION_1_5;
        }
        if (version.startsWith(JavaCore.VERSION_1_4)) {
            return JavaCore.VERSION_1_4;
        }
        if (version.startsWith(JavaCore.VERSION_1_3)) {
            return JavaCore.VERSION_1_3;
        }
        if (version.startsWith(JavaCore.VERSION_1_2)) {
            return JavaCore.VERSION_1_3;
        }
        if (version.startsWith(JavaCore.VERSION_1_1)) {
            return JavaCore.VERSION_1_3;
        }
        return defaultCompliance;
    }

    /**
     * DOC ycbai Get execution environment compliance via execution environment.
     * 
     * @param executionEnvironment
     * @return
     */
    public static String getExecutionEnvironmentCompliance(IExecutionEnvironment executionEnvironment) {
        @SuppressWarnings("rawtypes")
        Map complianceOptions = executionEnvironment.getComplianceOptions();
        if (complianceOptions != null) {
            Object compliance = complianceOptions.get(JavaCore.COMPILER_COMPLIANCE);
            if (compliance instanceof String) {
                return (String) compliance;
            }
        }

        String desc = executionEnvironment.getId();
        if (desc.indexOf(JavaCore.VERSION_1_6) != -1) {
            return JavaCore.VERSION_1_6;
        } else if (desc.indexOf(JavaCore.VERSION_1_6) != -1) {
            return JavaCore.VERSION_1_6;
        } else if (desc.indexOf(JavaCore.VERSION_1_5) != -1) {
            return JavaCore.VERSION_1_5;
        } else if (desc.indexOf(JavaCore.VERSION_1_4) != -1) {
            return JavaCore.VERSION_1_4;
        }
        return JavaCore.VERSION_1_3;
    }

    /**
     * DOC ycbai Add the java nature to project.
     * 
     * @param project
     * @param monitor
     * @throws CoreException
     */
    public static void addJavaNature(IProject project, IProgressMonitor monitor) throws CoreException {
        if (monitor != null && monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (project != null && !project.hasNature(JavaCore.NATURE_ID)) {
            IProjectDescription description = project.getDescription();
            String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = JavaCore.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
        }
        if (monitor != null) {
            monitor.worked(1);
        }
    }

}
