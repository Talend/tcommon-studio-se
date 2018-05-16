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
package org.talend.updates.runtime.engine.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.utils.RemoteModulesHelper;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.TalendWebServiceUpdateExtraFeature;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public class PluginOptionalMissingJarsExtraUpdatesFactory extends AbstractExtraUpdatesFactory {

    private static Logger log = Logger.getLogger(PluginOptionalMissingJarsExtraUpdatesFactory.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.updates.runtime.engine.AbstractExtraUpdatesFactory#retrieveUninstalledExtraFeatures(org.eclipse.core
     * .runtime.IProgressMonitor, java.util.Set)
     */
    @Override
    public void retrieveUninstalledExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> uninstalledExtraFeatures)
            throws Exception {

        SubMonitor mainSubMonitor = SubMonitor.convert(monitor, 2);
        List<ModuleNeeded> unistalledModulesNeeded = ModulesNeededProvider
                .filterOutRequiredModulesForBundle(ModulesNeededProvider.getUnistalledModulesNeeded());
        if (monitor.isCanceled()) {
            return;
        }// else keep going
        mainSubMonitor.worked(1);
        if (!unistalledModulesNeeded.isEmpty()) {

            // fetch missing jar information from remote web site.
            ArrayList<ModuleToInstall> modulesRequiredToBeInstalled = new ArrayList<ModuleToInstall>();
            // IRunnableWithProgress should not be part of
            // jface because it adds graphical
            // dependencies.
            IRunnableWithProgress notInstalledModulesRunnable = RemoteModulesHelper.getInstance().getNotInstalledModulesRunnable(
                    unistalledModulesNeeded, modulesRequiredToBeInstalled, true);
            if (notInstalledModulesRunnable != null) {// some data need to be fetched
                try {
                    notInstalledModulesRunnable.run(mainSubMonitor.newChild(1));
                } catch (InvocationTargetException e) {
                    log.error("failed to fetch missing third parties jars information", e); //$NON-NLS-1$
                    return;
                } catch (InterruptedException e) {
                    log.error("failed to fetch missing third parties jars information", e); //$NON-NLS-1$
                    return;
                }
            }// else all data already fetched so keep going
            if (mainSubMonitor.isCanceled()) {
                return;
            }// else keep going.
            ArrayList<ModuleToInstall> modulesForAutomaticInstall = TalendWebServiceUpdateExtraFeature
                    .filterAllAutomaticInstallableModules(modulesRequiredToBeInstalled);
            if (modulesForAutomaticInstall.isEmpty()) {
                return;
            } else {
                addToSet(
                        uninstalledExtraFeatures,
                        new TalendWebServiceUpdateExtraFeature(modulesForAutomaticInstall, Messages
                                .getString("missing.optional.third.parties.libs.feature.name"), Messages //$NON-NLS-1$
                                .getString("missing.optional.third.parties.libs.feature.description"), false/* mustInstall */)); //$NON-NLS-1$
            }
        }// else nothing to install so nothing to install ;)

    }

}
