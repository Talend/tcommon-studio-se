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
public class PluginRequiredMissingJarsExtraUpdatesFactory extends AbstractExtraUpdatesFactory {

    private static Logger log = Logger.getLogger(PluginRequiredMissingJarsExtraUpdatesFactory.class);

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

        // get all misssing jars
        List<ModuleNeeded> allUninstalledModules = ModulesNeededProvider
                .getAllNoInstalledModulesNeededExtensionsForPlugin(mainSubMonitor.newChild(1));
        if (monitor.isCanceled()) {
            return;
        }// else keep going
         // fetch missing jar information from remote web site.
        ArrayList<ModuleToInstall> modulesRequiredToBeInstalled = new ArrayList<ModuleToInstall>();
        IRunnableWithProgress notInstalledModulesRunnable = RemoteModulesHelper.getInstance().getNotInstalledModulesRunnable(
                allUninstalledModules, modulesRequiredToBeInstalled, true);// IRunnableWithProgress should not be part
                                                                           // of
        // jface because it adds graphical
        // dependencies.
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
                            .getString("missing.third.parties.libs.feature.name"), Messages //$NON-NLS-1$
                            .getString("missing.third.parties.libs.feature.description"), true/* mustInstall */)); //$NON-NLS-1$
        }

    }

}
