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

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.ui.ImageFactory;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractExtraFeature implements ExtraFeature {

    protected static Logger log = Logger.getLogger(AbstractExtraFeature.class);

    protected String p2IuId;// p2 installable unit id

    protected String baseRepoUriStr;// default url of the remote repo where to look for the feature to install

    protected String name;// name to be displayed to the user.

    protected String description;// Description displayed to the user.

    protected String version;// version of the p2 IU

    protected boolean mustBeInstalled;

    protected boolean useLegacyP2Install;

    protected Boolean isInstalled;// true is already installed in the current Studio

    protected FeatureCategory parentCategory;

    protected boolean needRestart = true;

    private Image image;

    private Object imageLock = new Object();

    private List<ICallBack> callBacks = Collections.synchronizedList(new LinkedList<>());

    /**
     * Getter for p2 installable unit id.
     *
     * @return the p2 installable unit id.
     */
    public String getP2IuId() {
        return this.p2IuId;
    }

    /**
     * Sets the p2 installable unit id.
     *
     * @param p2IuId the p2 installable unit id to set
     */
    public void setP2IuId(String p2IuId) {
        this.p2IuId = p2IuId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public boolean needRestart() {
        return needRestart;
    }

    public void setNeedRestart(boolean needRestart) {
        this.needRestart = needRestart;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for parentCategory.
     * 
     * @return the parentCategory
     */
    public FeatureCategory getParentCategory() {
        return this.parentCategory;
    }

    /**
     * Sets the parentCategory.
     * 
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(FeatureCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.p2IuId == null) ? 0 : this.p2IuId.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

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
        P2ExtraFeature other = (P2ExtraFeature) obj;
        if (this.p2IuId == null) {
            if (other.p2IuId != null) {
                return false;
            }
        } else if (!this.p2IuId.equals(other.p2IuId)) {
            return false;
        }
        if (this.version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!this.version.equals(other.version)) {
            return false;
        }
        return true;
    }

    /**
     * this is the base URI set in the license.
     *
     * @return the defaultRepoUriStr
     */
    public String getBaseRepoUriString() {
        return this.baseRepoUriStr;
    }

    public URI getP2RepositoryURI() {
        return getP2RepositoryURI(null, false);
    }

    public URI getP2RepositoryURI(String key, boolean isTOS) {
        String uriString = getBaseRepoUriString();
        if (key == null) {
            key = "talend.p2.repo.url"; //$NON-NLS-1$
        }
        String p2RepoUrlFromProp = System.getProperty(key);
        if (!isTOS && p2RepoUrlFromProp != null) {
            uriString = p2RepoUrlFromProp;
        } else {
            org.osgi.framework.Version studioVersion = new org.osgi.framework.Version(VersionUtils.getTalendVersion());
            String version = studioVersion.getMajor() + "." + studioVersion.getMinor() + "." + studioVersion.getMicro();
            if (uriString == null) {
                return URI.create(version);
            }
            uriString = uriString + (uriString.endsWith("/") ? "" : "/") + version; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return URI.create(uriString);
    }

    /**
     * DOC sgandon Comment method "copy".
     *
     * @param p2ExtraFeature
     * @param p2ExtraFeatureUpdate
     */
    public void copyFieldInto(AbstractExtraFeature p2ExtraFeatureUpdate) {
        p2ExtraFeatureUpdate.name = name;
        p2ExtraFeatureUpdate.description = description;
        p2ExtraFeatureUpdate.version = version;
        p2ExtraFeatureUpdate.p2IuId = p2IuId;
        p2ExtraFeatureUpdate.baseRepoUriStr = baseRepoUriStr;
        p2ExtraFeatureUpdate.mustBeInstalled = mustBeInstalled;
        p2ExtraFeatureUpdate.useLegacyP2Install = useLegacyP2Install;
    }

    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        return EnumSet.allOf(UpdateSiteLocationType.class);
    }

    @Override
    public boolean mustBeInstalled() {
        return mustBeInstalled;
    }

    /**
     * Getter for useLegacyP2Install.
     *
     * @return the useLegacyP2Install
     */
    public boolean isUseLegacyP2Install() {
        return this.useLegacyP2Install;
    }

    protected List<ICallBack> getCallBacks() {
        return callBacks;
    }

    @Override
    public Image getImage(IProgressMonitor monitor) throws Exception {
        if (image != null) {
            return image;
        }

        synchronized (imageLock) {
            if (image == null) {
                image = ImageFactory.getInstance().createFeatureImage(downloadImage(monitor));
            }
        }
        return image;
    }

    @Override
    public File downloadImage(IProgressMonitor monitor) throws Exception {
        for (ICallBack callBack : getCallBacks()) {
            try {
                File imageFile = callBack.downloadImage(monitor);
                if (imageFile != null) {
                    return imageFile;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    @Override
    public void addCallBack(ICallBack callBack) {
        getCallBacks().add(callBack);
    }

    @Override
    public void remoteCallBack(ICallBack callBack) {
        getCallBacks().remove(callBack);
    }

}
