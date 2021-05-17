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
package org.talend.updates.runtime.login;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.login.AbstractLoginTask;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsLocalNexusInstallFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.utils.OsgiBundleInstaller;

/**
 *
 * DOC ggu class global comment. Detailled comment
 */
public class InstallLocalNexusComponentsLoginTask extends AbstractLoginTask {

    private static Logger log = Logger.getLogger(InstallLocalNexusComponentsLoginTask.class);

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
        if (!NexusServerManager.getInstance().isRemoteOnlineProject()) {
            return;
        }
        if (!ComponentIndexManager.isEnableShareComponent()) {
            log.info("Component share is disabled, won't check components update");
            return;
        }
        try {
            ComponentsLocalNexusInstallFactory compInstallFactory = new ComponentsLocalNexusInstallFactory(
                    NexusServerManager.getInstance().getComponentShareRepositoryFromServer());

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
                } else {
                    System.setProperty("update.restart", Boolean.TRUE.toString()); //$NON-NLS-1$
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
            throws Exception {
        if (feature instanceof FeatureCategory) {
            Set<ExtraFeature> children = ((FeatureCategory) feature).getChildren();
            for (ExtraFeature f : children) {
                install(monitor, f, messages);
            }
        } else {
            if (feature.canBeInstalled(monitor)) {
                messages.analyzeStatus(feature.install(monitor, null));
                messages.setNeedRestart(feature.needRestart());
            }
        }
    }
}
