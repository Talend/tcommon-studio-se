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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


/**
 * created by nrousseau on Nov 3, 2016
 * Detailled comment
 *
 */
public class FeatureCategory implements ExtraFeature {
    
    private String name;
    private String version;
    private String description;

    /**
     * use LinkedHashSet to keep children order
     */
    private Set<ExtraFeature> children = new LinkedHashSet<>();
    

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#isInstalled(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public boolean isInstalled(IProgressMonitor progress) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.updates.runtime.model.ExtraFeature#createFeatureIfUpdates(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#getVersion()
     */
    @Override
    public String getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#install(org.eclipse.core.runtime.IProgressMonitor, java.util.List)
     */
    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws Exception {
        return Status.OK_STATUS;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#getUpdateSiteCompatibleTypes()
     */
    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#mustBeInstalled()
     */
    @Override
    public boolean mustBeInstalled() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.talend.updates.runtime.model.ExtraFeature#needRestart()
     */
    @Override
    public boolean needRestart() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Getter for children.
     * @return the children
     */
    public Set<ExtraFeature> getChildren() {
        return this.children;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
