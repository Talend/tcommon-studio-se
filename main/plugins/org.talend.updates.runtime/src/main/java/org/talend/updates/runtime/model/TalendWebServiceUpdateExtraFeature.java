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
package org.talend.updates.runtime.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.librariesmanager.ui.dialogs.ModuleLicenseDialog;
import org.talend.librariesmanager.utils.DownloadModuleRunnable;
import org.talend.librariesmanager.utils.RemoteModulesHelper;
import org.talend.updates.runtime.i18n.Messages;

/**
 * Special extra feature that does not use p2 repository but the Talend update web service.
 * 
 */
public class TalendWebServiceUpdateExtraFeature implements ExtraFeature {

    private final List<ModuleToInstall> modulesToBeInstalled;

    private String                      name;

    private String                      description;

    private boolean                     mustBeInstalled;

    /**
     * DOC sgandon TalendWebServiceUpdate constructor comment.
     */
    public TalendWebServiceUpdateExtraFeature(ArrayList<ModuleToInstall> modulesToBeInstalled, String name, String description,
            boolean mustBeInstalled) {
        this.modulesToBeInstalled = modulesToBeInstalled;
        this.name = name;
        this.description = description;
        this.mustBeInstalled = mustBeInstalled;
    }

    /**
     * DOC sgandon Comment method "filterAllAutomaticInstallableModules".
     * 
     * @param modules
     * @return
     */
    public static ArrayList<ModuleToInstall> filterAllAutomaticInstallableModules(ArrayList<ModuleToInstall> modules) {
        ArrayList<ModuleToInstall> modulesForAutomaticInstall = new ArrayList<ModuleToInstall>( modules.size() );
        for (ModuleToInstall module : modules) {
            if (!MavenConstants.DOWNLOAD_MANUAL.equals( module.getDistribution() )) {
                modulesForAutomaticInstall.add( module );
            }// else not automatic so ignor
        }
        return modulesForAutomaticInstall;
    }

    /*
     * install the remote jars, ignoring the repos URIs because we do not have the right to distribute them
     * 
     * @see org.talend.updates.model.ExtraFeature#install(org.eclipse.core.runtime.IProgressMonitor, java.util.List)
     */
    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws Exception {
        // shell should not be used here cause this is a low level class but we need to ask the user to validate the
        // license
        // and I do not have the time to use a delegate pattern.

        DownloadModuleRunnable downloadModule = new DownloadModuleRunnable( modulesToBeInstalled ) {

            @Override
            protected boolean acceptLicence(ModuleToInstall module) {
                return TalendWebServiceUpdateExtraFeature.acceptLicence( module );
            }
        };
        downloadModule.run( progress );
        // compute the dowload status
        Set<String> jarsFailedToDownload = downloadModule.getDownloadFailed();
        if (jarsFailedToDownload.isEmpty()) {
            return Messages.createOkStatus( "UpdateStudioWizard.missing.jar.download.ok" ); //$NON-NLS-1$
        } else {
            Status errorStatus = Messages.createErrorStatus( null, "UpdateStudioWizard.missing.jar.download.failed", //$NON-NLS-1$
                    Arrays.toString( jarsFailedToDownload.toArray( new String[jarsFailedToDownload.size()] ) ) );
            MultiStatus multiStatus = new MultiStatus( errorStatus.getPlugin(), errorStatus.getSeverity(),
                    errorStatus.getMessage(), null );
            multiStatus.add( errorStatus );
            Set<String> installedModules = downloadModule.getInstalledModules();
            if (!installedModules.isEmpty()) {
                multiStatus.add( Messages.createOkStatus( "UpdateStudioWizard.some.jars.sucessfully.installed", //$NON-NLS-1$
                        Arrays.toString( jarsFailedToDownload.toArray( new String[jarsFailedToDownload.size()] ) ) ) );
            }
            return multiStatus;
        }
    }

    /**
     * DOC sgandon Comment method "acceptLicence".
     * 
     * @param module
     * @return
     */
    protected static boolean acceptLicence(final ModuleToInstall module) {
        Display defaultDisplay = Display.getDefault();
        if (defaultDisplay != null) {
            final AtomicBoolean accepted = new AtomicBoolean( false );// just use atomic to have a final object.
            Display.getDefault().syncExec( new Runnable() {

                @Override
                public void run() {
                    String license = RemoteModulesHelper.getInstance().getLicenseUrl( module.getLicenseType() );
                    ModuleLicenseDialog licenseDialog = new ModuleLicenseDialog( Display.getDefault().getActiveShell(), module
                            .getLicenseType(), license, module.getName() );
                    if (licenseDialog.open() != Window.OK) {
                        accepted.set( false );
                    } else {
                        accepted.set( true );
                    }
                }
            } );
            return accepted.get();
        } else {// should ask the user to agree on license.
            throw new RuntimeException( "Unable to get the default Display to show the license agreement, should never appear." ); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#isInstalled(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public boolean isInstalled(IProgressMonitor progress) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getName()
     */
    @Override
    public String getName() {
        return name + " (" + modulesToBeInstalled.size() + ")";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getVersion()
     */
    @Override
    public String getVersion() {
        return VersionUtils.getVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TalendWebServiceUpdateExtraFeature other = (TalendWebServiceUpdateExtraFeature) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals( other.name )) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#updateSiteCompatibleTypes()
     */
    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        return EnumSet.of( UpdateSiteLocationType.DEFAULT_REPO );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#mustBeInstalled()
     */
    @Override
    public boolean mustBeInstalled() {
        return mustBeInstalled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.updates.runtime.model.ExtraFeature#createFeatureIfUpdates(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws Exception {
        return null;// we do not handle udpates yet.
    }

    @Override
    public boolean needRestart() {
        return false;
    }

}
