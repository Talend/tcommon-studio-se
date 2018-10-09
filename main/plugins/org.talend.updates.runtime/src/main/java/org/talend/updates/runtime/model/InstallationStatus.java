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

import org.apache.commons.lang.StringUtils;
import org.eclipse.equinox.p2.metadata.Version;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class InstallationStatus {

    private Status status;

    private String installedVersion;

    private String requiredStudioVersion;

    private boolean isCompatible = true;

    public InstallationStatus(Status status) {
        this.status = status;
    }

    public boolean canBeInstalled() {
        return this.getStatus().canBeInstalled() && isCompatible;
    }

    public String getRequiredStudioVersion() {
        return this.requiredStudioVersion;
    }

    public void setRequiredStudioVersion(String requiredStudioVersion) {
        if (StringUtils.isNotBlank(requiredStudioVersion)) {
            try {
                Version studioVersion = PathUtils.convert2Version(VersionUtils.getInternalVersion());
                Version compatibleVersion = PathUtils.convert2Version(requiredStudioVersion);
                if (compatibleVersion == null) {
                    setCompatible(false);
                } else if (studioVersion.compareTo(compatibleVersion) < 0) {
                    setCompatible(false);
                } else {
                    setCompatible(true);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        } else {
            setCompatible(true);
        }
        this.requiredStudioVersion = requiredStudioVersion;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    /**
     * return the installed version if already installed
     */
    public String getInstalledVersion() {
        return this.installedVersion;
    }

    public boolean isCompatible() {
        return this.isCompatible;
    }

    public void setCompatible(boolean isCompatible) {
        this.isCompatible = isCompatible;
    }

    public static enum Status {

        /**
         * not be installed yet, can be installed.
         */
        INSTALLABLE(false, true),

        /**
         * already installed, can be updated.
         */
        UPDATABLE(true, true),

        /**
         * already installed, can be degraded
         */
        DEGRADABLE(true, true),

        /**
         * already installed, and not update
         */
        INSTALLED(true, false),

        /**
         * already installed, can be re-installed
         */
        RE_INSTALLABLE(true, true),

        /**
         * Can't install
         */
        CANT_INSTALL(false, false),

        /**
         * Unknow status
         */
        UNKNOWN(false, false);

        //
        ;

        private boolean isInstalled;

        private boolean canBeInstalled;

        Status(boolean isInstalled, boolean canBeInstalled) {
            this.isInstalled = isInstalled;
            this.canBeInstalled = canBeInstalled;
        }

        public boolean isInstalled() {
            return this.isInstalled;
        }

        public boolean canBeInstalled() {
            return this.canBeInstalled;
        }
    }

}
