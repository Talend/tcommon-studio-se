// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Priority;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public abstract class AbstractExtraUpdatesFactory implements IUpdatesFactory {
    protected boolean isCheckUpdateOnLine = true;
    protected String getAcronym() {
        String acronym = "";
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);
            acronym = brandingService.getAcronym();
        }
        return acronym;
    }

    @Override
    public void retrieveAllExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> features) throws Exception {
        throw new UnsupportedOperationException("Unsupported operation, please check");
    }

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

    protected void addToCategory(FeatureCategory category, ExtraFeature extraF) {
        if (category == null || extraF == null) {
            return;
        }
        category.getChildren().add(extraF);
        extraF.setParentCategory(category);
    }

    protected void validateTypes(Collection<Type> types, String name) {
        /**
         * Can only contains one unique type
         */
        int uniqueTypeCount = 0;
        if (types == null) {
            return;
        }
        for (Type type : types) {
            if (type != null && type.isUnique()) {
                ++uniqueTypeCount;
            }
        }
        if (1 < uniqueTypeCount) {
            ExceptionHandler.process(
                    new Exception(name + ": Only one unique type is permitted, otherwise studio may create a wrong patch!"),
                    Priority.WARN);
        }
    }

    public boolean isCheckUpdateOnLine() {
        return isCheckUpdateOnLine;
    }


    public void setCheckUpdateOnLine(boolean isCheckUpdateOnLine) {
        this.isCheckUpdateOnLine = isCheckUpdateOnLine;
    }

}
