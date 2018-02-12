// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.template;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Activation;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.setting.project.IProjectSettingManagerProvider;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.repository.ProjectManager;

/**
 * created by ggu on 5 Feb 2015 Detailled comment
 *
 */
public class MavenTemplateManager {

    public static final String KEY_PROJECT_NAME = "ProjectName"; //$NON-NLS-1$

    public static Map<String, AbstractMavenTemplateManager> getTemplateManagerMap() {
        return MavenTemplateManagerRegistry.getInstance().getTemplateManagerMap();
    }

    public static Map<String, IProjectSettingManagerProvider> getProjectSettingManagerMap() {
        return MavenTemplateManagerRegistry.getInstance().getProjectSettingManagerMap();
    }

    /**
     * 1. get the template from the file template under the folder first.
     * 
     * 2. if file template is not existed, try to get th template from project setting.
     * 
     * 3. if the project setting is not set still, try to get the template from the bundle template.
     * 
     */
    @SuppressWarnings("resource")
    public static InputStream getTemplateStream(File templateFile, String projectSettingKey, String bundleName,
            String bundleTemplatePath, Map<String, Object> parameters) throws Exception {
        InputStream stream = null;
        // 1. from file template dirctly.
        if (templateFile != null && templateFile.exists()) {
            // will close it later.
            stream = new BufferedInputStream(new FileInputStream(templateFile));
        }

        // 2. from project setting
        if (stream == null && projectSettingKey != null) {
            stream = getProjectSettingStream(projectSettingKey, parameters);
        }
        // 3. from bundle template.
        if (stream == null && bundleName != null && bundleTemplatePath != null) {
            stream = getBundleTemplateStream(bundleName, bundleTemplatePath);
        }
        return stream;
    }

    public static String getTemplateContent(File templateFile, String projectSettingKey, String bundleName,
            String bundleTemplatePath, Map<String, Object> parameters) throws Exception {
        return getContentFromInputStream(getTemplateStream(templateFile, projectSettingKey, bundleName, bundleTemplatePath,
                parameters));
    }

    /**
     * 
     * get the template file stream from bundle.
     */
    public static InputStream getBundleTemplateStream(String bundleName, String bundleTemplatePath) throws Exception {
        if (bundleName == null || bundleTemplatePath == null) {
            return null;
        }
        Map<String, AbstractMavenTemplateManager> templateManagerMap = MavenTemplateManager.getTemplateManagerMap();
        AbstractMavenTemplateManager templateManager = templateManagerMap.get(bundleName);
        if (templateManager != null) {
            return templateManager.readBundleStream(bundleTemplatePath);
        }
        return null;
    }

    public static String getBundleTemplateContent(String bundleName, String templatePath) throws Exception {
        return getContentFromInputStream(getBundleTemplateStream(bundleName, templatePath));
    }

    /**
     * try to find the template setting in project setting one by one.
     */
    public static String getProjectSettingValue(String key, Map<String, Object> parameters) {
        Map<String, AbstractMavenTemplateManager> templateManagerMap = MavenTemplateManager.getTemplateManagerMap();

        for (String bundleName : templateManagerMap.keySet()) {
            AbstractMavenTemplateManager templateManager = templateManagerMap.get(bundleName);
            try {
                InputStream steam = templateManager.readProjectSettingStream(key, parameters);
                if (steam != null) {
                    String content = MavenTemplateManager.getContentFromInputStream(steam);
                    if (content != null) {
                        return content;
                    }
                }
            } catch (Exception e) {
                // try to find another one
            }
        }
        return null;
    }

    /**
     * try to find the template setting in project setting one by one.
     */
    public static InputStream getProjectSettingStream(String key, Map<String, Object> parameters) {
        Map<String, AbstractMavenTemplateManager> templateManagerMap = MavenTemplateManager.getTemplateManagerMap();

        for (String bundleName : templateManagerMap.keySet()) {
            AbstractMavenTemplateManager templateManager = templateManagerMap.get(bundleName);
            try {
                InputStream steam = templateManager.readProjectSettingStream(key, parameters);
                if (steam != null) {
                    return steam;
                }
            } catch (Exception e) {
                // try to find another one
            }
        }
        return null;
    }

    public static String getContentFromInputStream(InputStream is) throws IOException {
        if (is != null) {
            try {
                StringWriter sw = new StringWriter(1000);
                int c = 0;
                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
                return sw.toString();
            } finally {
                is.close();
            }
        }
        return null;
    }

    public static void saveContent(IFile targetFile, String content, boolean overwrite) throws CoreException {
        ByteArrayInputStream source = new ByteArrayInputStream(content.getBytes());
        if (targetFile.exists()) {
            targetFile.setContents(source, true, false, new NullProgressMonitor());
        } else {
            targetFile.create(source, true, new NullProgressMonitor());
        }
    }

    public static void saveContent(File targetFile, String content, boolean overwrite) throws IOException {
        if (targetFile.exists()) {
            if (!overwrite) {
                // throw new IOException("The file have existed, must delete or with overwrite option.");
                return; // nothing to do, keep it.
            }
            if (!targetFile.isFile()) {
                throw new IOException("Can't write the template to directory, must be file.");
            }
        }

        if (content != null) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(targetFile);
                writer.write(content);
                writer.flush();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    public static Model getCodeProjectTemplateModel() {
        return getCodeProjectTemplateModel(null); // by default will be current project.
    }
    
    public static Model addCIBuilder(Model model) {
        if (!PluginChecker.isTIS()) {
            return model;
        }
        if (!model.getProfiles().isEmpty()) {
            Profile toDelete = null;
            for (Profile profile : model.getProfiles()) {
                if ("ci-builder".equals(profile.getId())) {
                    toDelete = profile;
                    break;
                }
            }
            if (toDelete != null) {
                model.getProfiles().remove(toDelete);
            }
        }
        Profile profile = new Profile();
        profile.setId("ci-builder");
        Activation activation = new Activation();
        activation.setActiveByDefault(true);
        profile.setActivation(activation);
        model.addProfile(profile);

        Plugin plugin = new Plugin();
        profile.setBuild(new Build());
        profile.getBuild().addPlugin(plugin);
        plugin.setGroupId(TalendMavenConstants.DEFAULT_GROUP_ID);
        plugin.setArtifactId("ci.builder"); //$NON-NLS-1$
        plugin.setVersion(VersionUtils.getTalendVersion());

        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution pe = new PluginExecution();
        pe.setPhase("generate-sources"); //$NON-NLS-1$
        pe.addGoal("generate"); //$NON-NLS-1$
        executions.add(pe);
        plugin.setExecutions(executions);
        
        return model;
    }

    /**
     * Try to load the project template from bundle, if load failed, use default instead.
     */
    public static Model getCodeProjectTemplateModel(Map<String, Object> parameters) {
        final String projectTechName = PomUtil.getProjectNameFromTemplateParameter(parameters);
        Model defaultModel = getDefaultCodeProjectTemplateModel(projectTechName);
        try {
            InputStream stream = MavenTemplateManager.getTemplateStream(null,
                    IProjectSettingPreferenceConstants.TEMPLATE_PROJECT_POM, DesignerMavenPlugin.PLUGIN_ID,
                    IProjectSettingTemplateConstants.PATH_GENERAL + '/'
                            + IProjectSettingTemplateConstants.PROJECT_TEMPLATE_FILE_NAME, parameters);
            if (stream != null) {
                Model model = MavenPlugin.getMavenModelManager().readMavenModel(stream);

                Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
                variablesValuesMap.put(ETalendMavenVariables.ProjectGroupId, defaultModel.getGroupId());
                variablesValuesMap.put(ETalendMavenVariables.ProjectArtifactId, defaultModel.getArtifactId());
                variablesValuesMap.put(ETalendMavenVariables.ProjectVersion, defaultModel.getVersion());

                variablesValuesMap.put(ETalendMavenVariables.ProjectName, projectTechName);

                model.setGroupId(ETalendMavenVariables.replaceVariables(model.getGroupId(), variablesValuesMap));
                model.setArtifactId(ETalendMavenVariables.replaceVariables(model.getArtifactId(), variablesValuesMap));
                model.setVersion(ETalendMavenVariables.replaceVariables(model.getVersion(), variablesValuesMap));
                model.setName(ETalendMavenVariables.replaceVariables(model.getName(), variablesValuesMap));
                
                setJavaVersionForModel(model, variablesValuesMap);

                addCIBuilder(model);

                Properties properties = model.getProperties();
                properties.put("talend.project.name", projectTechName); //$NON-NLS-1$

                return model;
            }
        } catch (Exception e) {
            // ExceptionHandler.process(e);
        }
        return defaultModel; // if error, try to use default model
    }

    private static void setJavaVersionForModel(Model model, Map<ETalendMavenVariables, String> variablesValuesMap) {
        String javaVersion = JavaUtils.getProjectJavaVersion();
        if (javaVersion == null || javaVersion.trim().equals("")) { //$NON-NLS-1$
            javaVersion = JavaUtils.DEFAULT_VERSION;
        }
        variablesValuesMap.put(ETalendMavenVariables.JavaVersion, javaVersion);
        Plugin plugin = model.getBuild().getPluginManagement().getPluginsAsMap().get("org.apache.maven.plugins:maven-compiler-plugin"); //$NON-NLS-1$
        Object object = plugin.getConfiguration();
        if (object instanceof Xpp3Dom) {
            Xpp3Dom configNode = (Xpp3Dom) object;
            Xpp3Dom sourceNode = configNode.getChild("source"); //$NON-NLS-1$
            Xpp3Dom targetNode = configNode.getChild("target"); //$NON-NLS-1$
            if (!javaVersion.equals(sourceNode.getValue())) {
                sourceNode.setValue(ETalendMavenVariables.replaceVariables(sourceNode.getValue(), variablesValuesMap));
                targetNode.setValue(ETalendMavenVariables.replaceVariables(targetNode.getValue(), variablesValuesMap));
            }
        }
    }

    private static Model getDefaultCodeProjectTemplateModel(String projectTechName) {
        Model templateCodeProjectMOdel = new Model();
        templateCodeProjectMOdel.setGroupId(PomIdsHelper.getProjectGroupId(projectTechName));
        templateCodeProjectMOdel.setArtifactId(PomIdsHelper.getProjectArtifactId());
        templateCodeProjectMOdel.setVersion(PomIdsHelper.getProjectVersion(projectTechName));
        templateCodeProjectMOdel.setPackaging(TalendMavenConstants.PACKAGING_POM);

        return templateCodeProjectMOdel;
    }

    public static Model getRoutinesTempalteModel(String projectTechName) {
        Model defaultModel = createDefaultCodesTempalteModel(
                PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_CODE),
                TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID);
        return getCodesModelFromGeneralTemplate(defaultModel, projectTechName, "Routines", JavaUtils.JAVA_ROUTINES_DIRECTORY); //$NON-NLS-1$
    }

    public static Model getBeansTempalteModel(String projectTechName) {
        Model defaultModel = createDefaultCodesTempalteModel(
                PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_BEAN),
                TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID);
        return getCodesModelFromGeneralTemplate(defaultModel, projectTechName, "Beans", JavaUtils.JAVA_BEANS_DIRECTORY); //$NON-NLS-1$
    }

    public static Model getPigUDFsTempalteModel(String projectTechName) {
        Model defaultModel = createDefaultCodesTempalteModel(
                PomIdsHelper.getCodesGroupId(projectTechName, TalendMavenConstants.DEFAULT_PIGUDF),
                TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID);
        return getCodesModelFromGeneralTemplate(defaultModel, projectTechName, "PigUDFs", JavaUtils.JAVA_PIGUDF_DIRECTORY); //$NON-NLS-1$
    }

    private static Model createDefaultCodesTempalteModel(String groupId, String artifactId) {
        Model templateRoutinesModel = new Model();

        templateRoutinesModel.setGroupId(groupId);
        templateRoutinesModel.setArtifactId(artifactId);
        templateRoutinesModel.setVersion(PomIdsHelper.getCodesVersion());

        return templateRoutinesModel;
    }

    /**
     * Try to load the project template from bundle, if load failed, use default instead.
     */
    private static Model getCodesModelFromGeneralTemplate(Model defaultModel, String projectTechName, String codesName,
            String codesPackage) {
        try {
            if (projectTechName == null) {
                projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            }
            InputStream stream = MavenTemplateManager.getBundleTemplateStream(DesignerMavenPlugin.PLUGIN_ID,
                    IProjectSettingTemplateConstants.PATH_GENERAL + '/'
                            + IProjectSettingTemplateConstants.POM_CODES_TEMPLATE_FILE_NAME);
            if (stream != null) {
                Model model = MavenPlugin.getMavenModelManager().readMavenModel(stream);

                Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
                variablesValuesMap.put(ETalendMavenVariables.CodesGroupId, defaultModel.getGroupId());
                variablesValuesMap.put(ETalendMavenVariables.CodesArtifactId, defaultModel.getArtifactId());
                variablesValuesMap.put(ETalendMavenVariables.CodesVersion, defaultModel.getVersion());
                variablesValuesMap.put(ETalendMavenVariables.CodesName, codesName);
                variablesValuesMap.put(ETalendMavenVariables.CodesPackage, codesPackage);

                Project currentProject = ProjectManager.getInstance().getCurrentProject();
                variablesValuesMap.put(ETalendMavenVariables.ProjectName, projectTechName);

                model.setGroupId(ETalendMavenVariables.replaceVariables(model.getGroupId(), variablesValuesMap));
                model.setArtifactId(ETalendMavenVariables.replaceVariables(model.getArtifactId(), variablesValuesMap));
                model.setVersion(ETalendMavenVariables.replaceVariables(model.getVersion(), variablesValuesMap));
                model.setName(ETalendMavenVariables.replaceVariables(model.getName(), variablesValuesMap));

                Properties properties = model.getProperties();
                Iterator<Object> iterator = properties.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    Object object = properties.get(key);
                    if (object != null) {
                        String oldValue = object.toString();
                        String newValue = ETalendMavenVariables.replaceVariables(oldValue, variablesValuesMap);
                        properties.setProperty(key, newValue);
                    }
                }
                return model;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return defaultModel; // if error, try to use default model
    }

    public static Model getAggregatorFolderTemplateModel(IFile pomFile, String groupId, String artifactId, String projectTechName)
            throws Exception {
        Model model = new Model();
        model.setModelVersion("4.0.0"); //$NON-NLS-1$
        model.setGroupId(groupId);
        model.setArtifactId(artifactId);
        model.setVersion(PomIdsHelper.getProjectVersion());
        model.setPackaging(TalendMavenConstants.PACKAGING_POM);
        model.setName(projectTechName + " " + artifactId); //$NON-NLS-1$
        updateAggregatorPomModules(model, pomFile, false);
        
        return model;
    }
    
    public static void updateAggregatorPomModules(Model model, IFile pomFile, boolean save) throws Exception {
        List<String> modules = new ArrayList<>();
        IResource[] members = pomFile.getParent().members();
        for (IResource member : members) {
            if (member instanceof IFolder) {
                IFile modulePom = ((IFolder) member).getFile(TalendMavenConstants.POM_FILE_NAME);
                if (modulePom.exists()) {
                    modules.add(member.getName() + "/" + modulePom.getName()); //$NON-NLS-1$
                }
            }
        }
        model.setModules(modules);
        if (save) {
            PomUtil.savePom(null, model, pomFile);
        }
    }
}
