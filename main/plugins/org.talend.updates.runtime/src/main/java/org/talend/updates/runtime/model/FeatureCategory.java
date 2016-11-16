// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.HashSet;
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
    private Set<ExtraFeature> children = new HashSet<>();
    

    @Override
    public boolean isInstalled(IProgressMonitor progress) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws Exception {
        return Status.OK_STATUS;
    }

    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean mustBeInstalled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean needRestart() {
        // TODO Auto-generated method stub
        return false;
    }

    public Set<ExtraFeature> getChildren() {
        return this.children;
    }

    public void setName(String name) {
        this.name = name;
    }

}
