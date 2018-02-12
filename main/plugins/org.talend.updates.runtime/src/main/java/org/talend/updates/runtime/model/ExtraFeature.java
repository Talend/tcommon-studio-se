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
import java.util.EnumSet;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

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
     * Check that the feature has an udpate with a version higher than the one already installed. If that is the case
     * then a new instance of ExtraFeature is created, it returns null otherwhise.
     * 
     * @param progress
     * 
     * @return a new feature is an upate is available or null
     */
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws Exception;

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
    
    public boolean needRestart();

}
