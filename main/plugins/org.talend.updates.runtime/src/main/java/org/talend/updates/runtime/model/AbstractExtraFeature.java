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

import java.util.Collection;
import java.util.EnumSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.updates.runtime.feature.ImageFactory;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.storage.IFeatureStorage;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractExtraFeature implements ExtraFeature {

    protected static Logger log = Logger.getLogger(AbstractExtraFeature.class);

    private String p2IuId;// p2 installable unit id

    private String name;// name to be displayed to the user.

    private String description;// Description displayed to the user.

    private String version;// version of the p2 IU

    private String product;

    private String mvnUri;

    private String imageMvnUri;

    private String compatibleStudioVersion;

    private boolean degradable;

    private boolean mustBeInstalled;

    private boolean useLegacyP2Install;

    private boolean needRestart = true;

    private Boolean isInstalled;// true is already installed in the current Studio

    private FeatureCategory parentCategory;

    private Image image;

    private IFeatureStorage storage;

    private Object imageLock = new Object();

    private Collection<Type> types;

    private Collection<Category> categories;

    protected boolean share = true;

    public AbstractExtraFeature(String p2IuId, String name, String version, String description, String mvnUri, String imageMvnUri,
            String product, String compatibleStudioVersion, FeatureCategory parentCategory, Collection<Type> types,
            Collection<Category> categories, boolean degradable, boolean mustBeInstalled, boolean useLegacyP2Install) {
        this.p2IuId = p2IuId;
        this.name = name;
        this.version = version;
        this.description = description;
        this.mvnUri = mvnUri;
        this.imageMvnUri = imageMvnUri;
        this.product = product;
        this.parentCategory = parentCategory;
        this.mustBeInstalled = mustBeInstalled;
        this.useLegacyP2Install = useLegacyP2Install;
        this.compatibleStudioVersion = compatibleStudioVersion;
        this.degradable = degradable;
        this.types = types;
        this.categories = categories;
    }

    @Override
    public String getId() {
        return getP2IuId();
    }

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

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * Getter for parentCategory.
     * 
     * @return the parentCategory
     */
    @Override
    public FeatureCategory getParentCategory() {
        return this.parentCategory;
    }

    /**
     * Sets the parentCategory.
     * 
     * @param parentCategory the parentCategory to set
     */
    @Override
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
        AbstractExtraFeature other = (AbstractExtraFeature) obj;
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
     * DOC sgandon Comment method "copy".
     *
     * @param p2ExtraFeature
     * @param feature
     */
    public void copyFieldInto(AbstractExtraFeature feature) {
        feature.name = name;
        feature.description = description;
        feature.version = version;
        feature.p2IuId = p2IuId;
        feature.mustBeInstalled = mustBeInstalled;
        feature.useLegacyP2Install = useLegacyP2Install;
        feature.mvnUri = mvnUri;
        feature.imageMvnUri = imageMvnUri;
        feature.image = image;
        feature.categories = categories;
        feature.types = types;
        feature.compatibleStudioVersion = compatibleStudioVersion;
        feature.degradable = degradable;
    }

    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        return EnumSet.allOf(UpdateSiteLocationType.class);
    }

    @Override
    public boolean mustBeInstalled() {
        return mustBeInstalled;
    }

    public void setMustBeInstalled(boolean mustBeInstalled) {
        this.mustBeInstalled = mustBeInstalled;
    }

    /**
     * Getter for useLegacyP2Install.
     *
     * @return the useLegacyP2Install
     */
    public boolean isUseLegacyP2Install() {
        return this.useLegacyP2Install;
    }

    public void setUseLegacyP2Install(boolean useLegacyP2Install) {
        this.useLegacyP2Install = useLegacyP2Install;
    }

    public Boolean getIsInstalled() {
        return this.isInstalled;
    }

    public void setIsInstalled(Boolean isInstalled) {
        this.isInstalled = isInstalled;
    }

    @Override
    public String getMvnUri() {
        return this.mvnUri;
    }

    public void setMvnUri(String mvnUri) {
        this.mvnUri = mvnUri;
    }

    @Override
    public String getImageMvnUri() {
        return this.imageMvnUri;
    }

    public void setImageMvnUri(String imageMvnUri) {
        this.imageMvnUri = imageMvnUri;
    }

    @Override
    public IFeatureStorage getStorage() {
        return this.storage;
    }

    @Override
    public void setStorage(IFeatureStorage storage) {
        this.storage = storage;
    }

    @Override
    public Image getImage(IProgressMonitor monitor) throws Exception {
        if (image != null) {
            return image;
        }
        if (storage == null) {
            return null;
        }

        synchronized (imageLock) {
            if (image == null) {
                image = ImageFactory.getInstance().createFeatureImage(storage.getImageFile(monitor));
            }
        }
        return image;
    }

    @Override
    public boolean isInstalled(IProgressMonitor progress) throws Exception {
        try {
            InstallationStatus installationStatus = getInstallationStatus(progress);
            return installationStatus.getStatus().isInstalled();
        } catch (Exception e) {// just convert the exception
            throw new P2ExtraFeatureException(e);
        }
    }

    @Override
    public int compareTo(Object o) {
        try {
            if (o instanceof AbstractExtraFeature) {
                AbstractExtraFeature oFeature = (AbstractExtraFeature) o;
                Collection<Type> oTypes = oFeature.getTypes();
                Collection<Type> sTypes = getTypes();
                if (oTypes == null || oTypes.isEmpty() || sTypes == null || sTypes.isEmpty()) {
                    // goto super.compareTo
                } else {
                    oTypes = PathUtils.getAllTypeCategories(oTypes);
                    sTypes = PathUtils.getAllTypeCategories(sTypes);

                    // 1. if all are components, compare p2iuid first, then compare version
                    if (oTypes.contains(Type.TCOMP) && sTypes.contains(Type.TCOMP)) {
                        String oId = oFeature.getP2IuId();
                        String sId = getP2IuId();
                        if (StringUtils.equals(oId, sId)) {
                            return getVersion().compareTo(oFeature.getVersion());
                        } else {
                            return sId.compareTo(oId);
                        }
                    }

                    // 2. if all are patch
                    if (oTypes.contains(Type.PATCH) && sTypes.contains(Type.PATCH)) {
                        // 2.1 if all are plainzip patch, just compare version
                        if (oTypes.contains(Type.PLAIN_ZIP) && sTypes.contains(Type.PLAIN_ZIP)) {
                            return getVersion().compareTo(oFeature.getVersion());
                        }
                        // 2.2 if all are p2 patch, just compare version
                        if (oTypes.contains(Type.P2_PATCH) && sTypes.contains(Type.P2_PATCH)) {
                            return getVersion().compareTo(oFeature.getVersion());
                        }
                        // 2.3 in other case, p2 patch first
                        if (sTypes.contains(Type.P2_PATCH)) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }

                    // 3. in other case, patch first
                    if (sTypes.contains(Type.PATCH)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }

            return ExtraFeature.super.compareTo(o);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return 0;
    }

    @Override
    public Collection<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    @Override
    public Collection<Type> getTypes() {
        return this.types;
    }

    public void setTypes(Collection<Type> types) {
        this.types = types;
    }

    @Override
    public boolean isDegradable() {
        return this.degradable;
    }

    public void setDegradable(boolean degradable) {
        this.degradable = degradable;
    }

    @Override
    public String getCompatibleStudioVersion() {
        return this.compatibleStudioVersion;
    }

    public void setCompatibleStudioVersion(String compatibleStudioVersion) {
        this.compatibleStudioVersion = compatibleStudioVersion;
    }
}
