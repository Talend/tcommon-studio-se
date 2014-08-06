// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import java.util.Set;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.StatusException;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public abstract class AbstractExtraUpdatesFactory {

    public abstract void retrieveUninstalledExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> uninstalledExtraFeatures)
            throws Exception;

    /**
     * This method is used to add an item to the set and use a specific realm if the Set is an IObservable, any
     * observant be notified of the set modification.
     * 
     * @param uninstalledExtraFeatures, The set to add the feature extraF, optionnaly an IObservable
     * @param extraF, the extra feature to be added to the set
     */
    protected void addToSet(final Set<ExtraFeature> uninstalledExtraFeatures, final ExtraFeature extraF) {
        Runnable setExtraFeatureRunnable = new Runnable() {

            @Override
            public void run() {
                uninstalledExtraFeatures.add(extraF);
            }
        };
        if (uninstalledExtraFeatures instanceof IObservable) {
            ((IObservable) uninstalledExtraFeatures).getRealm().exec(setExtraFeatureRunnable);
        } else {
            setExtraFeatureRunnable.run();
        }
    }

    /**
     * 
     * before install, will call this.
     */
    public void beforeInstall() throws StatusException {
        // nothing to do
    }

    /**
     * 
     * after install, will call this.
     */
    public void afterInstall() throws StatusException {
        // nothing to do
    }

    /**
     * after update studio, will do this.
     */
    public void afterUpdate() throws Exception {
        // nothing to do
    }
}
