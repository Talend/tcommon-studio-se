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
package org.talend.updates.runtime.model;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.Image;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.InstallationStatus.Status;
import org.talend.updates.runtime.storage.IFeatureStorage;

/**
 * created by sgandon on 24 sept. 2013 Interface used for element to be installed after the Studio is launched.
 * 
 */
public interface ExtraFeature {

    /**
     * Getter for isInstalled.
     * 
     * @param progress
     * 
     * @return the isInstalled returns true is the feature is already installed
     */
    public boolean isInstalled(IProgressMonitor progress) throws Exception;

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName();

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription();

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public String getVersion();

    /**
     * This installs the Feature to the current Studio is isInstalled is false
     * 
     * @param progress, the monitor to show the progress.
     * @param allRepoUris all the remote/local repositories to look into to find the current feauture, this may be
     * empty, thus indicating the ExtraFeature must fetch the data from the default repository
     * @return the Status for the install
     */
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws Exception;

    /**
     * @return the type of site comptible with this features
     */
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes();

    /**
     * @return true if the user should install this extra feature.
     * */
    public boolean mustBeInstalled();
    
    boolean needRestart();

    default String getId() {
        return getName();
    }

    default boolean canBeInstalled(IProgressMonitor progress) throws ExtraFeatureException {
        try {
            return !isInstalled(progress);
        } catch (Exception e) {
            throw new P2ExtraFeatureException(e);
        }
    }

    /**
     * Check that the remote update site has a different version from the one already installed. If that is the case
     * then a new instance of P2ExtraFeature is create, it returns null otherwhise.
     *
     * @param progress
     * @return a new P2ExtraFeature if the update site contains a new version of the feature or null.
     */
    default ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws Exception {
        return null;
    }

    default ExtraFeature getInstalledFeature(IProgressMonitor progress) throws ExtraFeatureException {
        return this;
    }

    default Image getImage(IProgressMonitor monitor) throws Exception {
        return null;
    }

    default String getImageMvnUri() {
        return null;
    }

    default String getMvnUri() {
        return null;
    }

    default FeatureCategory getParentCategory() {
        return null;
    }

    /**
     * Sets the parentCategory.
     * 
     * @param parentCategory the parentCategory to set
     */
    default void setParentCategory(FeatureCategory parentCategory) {
        // nothing to do
    }

    default String getCompatibleStudioVersion() {
        return null;
    }

    default Collection<Type> getTypes() {
        return Collections.EMPTY_LIST;
    }

    default Collection<Category> getCategories() {
        return Collections.EMPTY_LIST;
    }

    default boolean isDegradable() {
        return false;
    }

    default IFeatureStorage getStorage() {
        return null;
    }

    default void setStorage(IFeatureStorage storage) {
        // nothing to do
    }

    default InstallationStatus getInstallationStatus(IProgressMonitor monitor) throws Exception {
        if (canBeInstalled(monitor)) {
            InstallationStatus status = new InstallationStatus(Status.INSTALLABLE);
            status.setRequiredStudioVersion(getCompatibleStudioVersion());
            return status;
        } else {
            InstallationStatus status = new InstallationStatus(Status.CANT_INSTALL);
            status.setRequiredStudioVersion(getCompatibleStudioVersion());
            return status;
        }
    }
}
