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
package org.talend.designer.maven.ui.setting.project.initializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.PluginChecker;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.AbstractMavenTemplateManager;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.extension.PomExtensionRegistry;
import org.talend.designer.maven.ui.DesignerMavenUiPlugin;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenScriptsProjectSettingInitializer extends AbstractProjectPreferenceInitializer {

    @Override
    protected IPreferenceStore getPreferenceStore() {
        return DesignerMavenUiPlugin.getDefault().getProjectPreferenceManager().getPreferenceStore();
    }

    @Override
    protected void initializeFields(IPreferenceStore preferenceStore) {
        super.initializeFields(preferenceStore);

        try {
            setDefault(preferenceStore, IProjectSettingPreferenceConstants.TEMPLATE_PROJECT_POM, DesignerMavenPlugin.PLUGIN_ID,
                    IProjectSettingTemplateConstants.PATH_GENERAL + '/'
                            + IProjectSettingTemplateConstants.PROJECT_TEMPLATE_FILE_NAME);

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    @Override
    protected void setDefault(IPreferenceStore preferenceStore, String key, String bundle, String bundleTemplatePath) {
        try {
            // set default value.
            AbstractMavenTemplateManager templateManager = MavenTemplateManager.getTemplateManagerMap().get(bundle);
            if (templateManager != null) {
                InputStream stream = templateManager.readBundleStream(bundleTemplatePath);
                Model model = MavenPlugin.getMavenModelManager().readMavenModel(stream);
                PomExtensionRegistry.getInstance().updatePomTemplate(model);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                MavenPlugin.getMaven().writeModel(model, out);
                String content = out.toString(TalendMavenConstants.DEFAULT_ENCODING);
                if (content != null) {
                    preferenceStore.setDefault(key, content);
                }
            }
            // if license change to NON-TP, check to remove docker profile
            if (!PluginChecker.isDockerPluginLoaded()) {
                String content = preferenceStore.getString(key);
                if (!StringUtils.isBlank(content)) {
                    InputStream stream = new ByteArrayInputStream(content.getBytes(TalendMavenConstants.DEFAULT_ENCODING));
                    Model model = MavenPlugin.getMavenModelManager().readMavenModel(stream);
                    Iterator<Profile> iterator = model.getProfiles().iterator();
                    boolean isModified = false;
                    while(iterator.hasNext()) {
                        Profile profile = iterator.next();
                        if ("docker".equals(profile.getId())) { //$NON-NLS-1$
                            iterator.remove();
                            isModified = true;
                        }
                    }
                    if (isModified) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        MavenPlugin.getMaven().writeModel(model, out);
                        content = out.toString(TalendMavenConstants.DEFAULT_ENCODING);
                        if (content != null) {
                            preferenceStore.setValue(key, content);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

}
