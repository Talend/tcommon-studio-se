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
package org.talend.designer.maven.ui.setting.repository.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.utils.RegistryReader;
import org.talend.designer.maven.ui.DesignerMavenUiPlugin;
import org.talend.designer.maven.ui.setting.repository.RepositoryMavenSetting;
import org.talend.designer.maven.ui.setting.repository.tester.IRepositorySettingTester;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class MavenSettingPagesRegistryReader extends RegistryReader {

    private static final Logger log = Logger.getLogger(MavenSettingPagesRegistryReader.class);

    private static final String CLASS_ATTRIBUTE = "class";

    private List<RepositoryMavenSetting> repoMavenSettings;

    private List<ERepositoryObjectType> supportTypes;

    public MavenSettingPagesRegistryReader() {
        super(DesignerMavenUiPlugin.PLUGIN_ID, "repositoryMavenSetting");
        init();
    }

    private void init() {
        if (this.repoMavenSettings == null) {
            synchronized (MavenSettingPagesRegistryReader.class) {
                this.repoMavenSettings = new ArrayList<RepositoryMavenSetting>();
                this.supportTypes = new ArrayList<ERepositoryObjectType>();

                readRegistry();

                Collections.sort(this.repoMavenSettings, new Comparator<RepositoryMavenSetting>() {

                    @Override
                    public int compare(RepositoryMavenSetting o1, RepositoryMavenSetting o2) {
                        int compare = o1.getOrder() - o2.getOrder();
                        if (compare == 0 && o1.getName() != null && o2.getName() != null) {
                            compare = o1.getName().compareToIgnoreCase(o2.getName());
                        }
                        return compare;
                    }
                });
            }
        }

    }

    public RepositoryMavenSetting[] getSettings() {
        init();
        return this.repoMavenSettings.toArray(new RepositoryMavenSetting[0]);
    }

    public ERepositoryObjectType[] getSupportTypes() {
        init();
        return this.supportTypes.toArray(new ERepositoryObjectType[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("RepositoryMavenSetting".equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {

                    // 1. init setting
                    RepositoryMavenSetting repoMavenSetting = (RepositoryMavenSetting) element
                            .createExecutableExtension(CLASS_ATTRIBUTE);
                    if (repoMavenSetting == null) {
                        log.error("Can't init the class RepositoryMavenSetting in " + element.getContributor().getName());
                        return;
                    }

                    String name = element.getAttribute("name");
                    String description = element.getAttribute("description");
                    String orderStr = element.getAttribute("order");
                    repoMavenSetting.setName(name);
                    repoMavenSetting.setDescription(description);
                    int order = 0;
                    if (orderStr != null && orderStr.length() > 0) {
                        try {
                            order = Integer.parseInt(orderStr);
                        } catch (NumberFormatException e) {
                            log.error("The order is not numeric for RepositoryMavenSetting in "
                                    + element.getContributor().getName());
                        }
                    }
                    repoMavenSetting.setOrder(order);

                    // 2. read tester
                    readTester(repoMavenSetting, element);

                    repoMavenSettings.add(repoMavenSetting);
                }
            });
            return true;

        } else if ("RepositorySupportType".equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    IConfigurationElement[] repositoryTypeChildren = element.getChildren("RepositoryType");
                    if (repositoryTypeChildren != null) {
                        for (IConfigurationElement typeChild : repositoryTypeChildren) {
                            String type = typeChild.getAttribute("type");
                            ERepositoryObjectType repoType = ERepositoryObjectType.valueOf(type);
                            if (repoType == null) {
                                log.error("The type (" + type + ") is not valid in " + element.getContributor().getName());
                            }
                            if (repoType != null && !supportTypes.contains(repoType)) {
                                supportTypes.add(repoType);
                            }
                        }
                    }
                }
            });
        }
        return false;
    }

    private void readTester(final RepositoryMavenSetting repoMavenSetting, final IConfigurationElement element) {
        SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

            @Override
            public void run() throws Exception {
                IConfigurationElement[] testerChildren = element.getChildren("SettingTester");
                if (testerChildren != null) {
                    if (testerChildren.length > 1) {
                        log.error("The Tester must be only one to set for " + repoMavenSetting.getName() + " in plugin "
                                + element.getContributor().getName());
                    } else if (testerChildren.length == 1) {
                        IRepositorySettingTester tester = (IRepositorySettingTester) testerChildren[0]
                                .createExecutableExtension(CLASS_ATTRIBUTE);
                        if (tester == null) {
                            log.error("Can't create tester in " + element.getContributor().getName());
                        }
                        repoMavenSetting.setTester(tester);
                    }
                }

            }
        });
    }

}
