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
package org.talend.updates.runtime.login;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.login.AbstractLoginTask;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsNexusInstallFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.utils.OsgiBundleInstaller;

/**
 * 
 * DOC ggu class global comment. Detailled comment
 */
public class InstallLocalNexusComponentsLoginTask extends AbstractLoginTask {

    private static Logger log = Logger.getLogger(InstallLocalNexusComponentsLoginTask.class);

    class ComponentsLocalNexusInstallFactory extends ComponentsNexusInstallFactory {

        @Override
        protected Set<P2ExtraFeature> getAllExtraFeatures(IProgressMonitor monitor) {
            return getLocalNexusFeatures(monitor); // only get from local nexus
        }

        @Override
        protected ComponentNexusP2ExtraFeature createComponentFeature(ComponentIndexBean b) {
            return new ComponentNexusP2ExtraFeature(b) {

                @Override
                protected void syncComponentsToInstalledFolder(IProgressMonitor progress, File downloadedCompFile) {
                    // already shared, no need deploy again
                    // super.syncComponentsToInstalledFolder(progress, installedCompFile);

                    // already shared, so no need keep it in local, and try to delete the downloaded one.
                    if (downloadedCompFile.exists()) {
                        downloadedCompFile.delete();
                    }
                }

            };
        }

    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2017, 6, 7, 12, 0, 0);
        return gc.getTime();
    }

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            ComponentsLocalNexusInstallFactory compInstallFactory = new ComponentsLocalNexusInstallFactory();

            Set<ExtraFeature> uninstalledExtraFeatures = new LinkedHashSet<ExtraFeature>();
            InstallComponentMessages messages = new InstallComponentMessages();

            compInstallFactory.retrieveUninstalledExtraFeatures(monitor, uninstalledExtraFeatures);
            for (ExtraFeature feature : uninstalledExtraFeatures) {
                install(monitor, feature, messages);
            }

            if (messages.isOk()) {
                log.info(messages.getInstalledMessage());
                if (!messages.isNeedRestart()) {
                    OsgiBundleInstaller.reloadComponents();
                }
            }
            if (StringUtils.isNotEmpty(messages.getFailureMessage())) {
                log.error(messages.getFailureMessage());
            }
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }

    private void install(IProgressMonitor monitor, ExtraFeature feature, InstallComponentMessages messages)
            throws P2ExtraFeatureException {
        if (feature instanceof FeatureCategory) {
            Set<ExtraFeature> children = ((FeatureCategory) feature).getChildren();
            for (ExtraFeature f : children) {
                install(monitor, f, messages);
            }
        }
        if (feature instanceof ComponentNexusP2ExtraFeature) {
            ComponentNexusP2ExtraFeature compFeature = (ComponentNexusP2ExtraFeature) feature;
            if (compFeature.canBeInstalled(monitor)) {
                messages.analyzeStatus(compFeature.install(monitor));
                messages.setNeedRestart(compFeature.needRestart());
            }
        }
    }
}
