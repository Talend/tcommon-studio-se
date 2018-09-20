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

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Priority;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.model.P2PatchFeature;
import org.talend.updates.runtime.model.PlainZipFeature;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public abstract class AbstractExtraUpdatesFactory implements IUpdatesFactory {

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

    protected ExtraFeature createFeature(IProgressMonitor monitor, ArtifactRepositoryBean serverBean,
            ComponentIndexBean b) {
        if (b == null) {
            return null;
        }
        ExtraFeature feature = null;
        Collection<Type> types = PathUtils.convert2Types(b.getTypes());
        try {
            validateTypes(types, b.getName());
            if (types.contains(Type.TCOMP_V0)) {
                feature = createTcompv0Feature(monitor, b);
            } else if (types.contains(Type.TCOMP_V1)) {
                feature = createTcompv1Feature(monitor, b);
            } else if (types.contains(Type.TACOKIT_SDK)) {
                feature = createTacokitSdkFeature(monitor, b);
            } else if (types.contains(Type.P2_PATCH)) {
                feature = createP2PatchFeature(monitor, b);
            } else if (types.contains(Type.PLAIN_ZIP)) {
                feature = createPlainZipFeature(monitor, b);
            } else {
                ExceptionHandler.process(new Exception("Can't create feature for " + b.toString()));
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return feature;
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

    protected ExtraFeature createTcompv1Feature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        ITaCoKitUpdateService tckService = ITaCoKitUpdateService.getInstance();
        if (tckService == null) {
            throw new Exception("Can't find " + ITaCoKitUpdateService.class.getSimpleName());
        }
        return tckService.generateExtraFeature(b, monitor);
    }

    protected ExtraFeature createTcompv0Feature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected ExtraFeature createTacokitSdkFeature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        return new P2PatchFeature(b);
    }

    protected ExtraFeature createP2PatchFeature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        return new P2PatchFeature(b);
    }

    protected ExtraFeature createPlainZipFeature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        return new PlainZipFeature(b);
    }

}
