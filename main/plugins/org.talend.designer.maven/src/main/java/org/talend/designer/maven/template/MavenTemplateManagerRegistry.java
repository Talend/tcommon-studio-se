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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.utils.RegistryReader;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.setting.project.IProjectSettingManagerProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
class MavenTemplateManagerRegistry extends RegistryReader {

    private static final Logger log = Logger.getLogger(MavenTemplateManagerRegistry.class);

    private static final String CLASS_ATTRIBUTE = "class";

    private Map<String, AbstractMavenTemplateManager> templateManagerBundleMap;

    private Map<String, IProjectSettingManagerProvider> projectSettingManagerBundleMap;

    private static final MavenTemplateManagerRegistry INSTANCE = new MavenTemplateManagerRegistry();

    private MavenTemplateManagerRegistry() {
        super(DesignerMavenPlugin.PLUGIN_ID, "mavenSetting");

        this.templateManagerBundleMap = new HashMap<String, AbstractMavenTemplateManager>();
        this.projectSettingManagerBundleMap = new HashMap<String, IProjectSettingManagerProvider>();

        readRegistry();
    }

    static MavenTemplateManagerRegistry getInstance() {
        return INSTANCE;
    }

    Map<String, AbstractMavenTemplateManager> getTemplateManagerMap() {
        return templateManagerBundleMap;
    }

    Map<String, IProjectSettingManagerProvider> getProjectSettingManagerMap() {
        return projectSettingManagerBundleMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        final String bundleName = element.getContributor().getName();
        if ("TemplateManager".equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    AbstractMavenTemplateManager templateManager = (AbstractMavenTemplateManager) element
                            .createExecutableExtension(CLASS_ATTRIBUTE);
                    if (templateManager == null) {
                        log.error("Can't create the template manager in " + bundleName);
                    } else {
                        // FIXME, means, for each bundle, should impl only one template manager.
                        templateManagerBundleMap.put(bundleName, templateManager);
                    }
                }
            });
            return true;
        } else if ("ProjectSettingManager".equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    IProjectSettingManagerProvider settingManager = (IProjectSettingManagerProvider) element
                            .createExecutableExtension(CLASS_ATTRIBUTE);
                    if (settingManager == null) {
                        log.error("Can't create the project setting manager in " + bundleName);
                    } else {
                        // FIXME, means, for each bundle, should impl only one project setting manager.
                        projectSettingManagerBundleMap.put(bundleName, settingManager);
                    }
                }
            });
            return true;
        }
        return false;
    }

}
