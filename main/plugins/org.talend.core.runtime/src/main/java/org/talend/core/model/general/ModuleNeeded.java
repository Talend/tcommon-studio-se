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
package org.talend.core.model.general;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Path;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * This bean is use to manage needed moduless (perl) and libraries (java).<br/>
 *
 * $Id: ModuleNeeded.java 38013 2010-03-05 14:21:59Z mhirt $
 *
 */
public class ModuleNeeded {

    private static MavenResolver mavenResolver;

    private String id;

    private String context;

    private String moduleName;

    private String informationMsg;

    private boolean required;

    private boolean mrRequired = false; // That indicates if the module is
                                        // required by M/R job.

    private String requiredIf;

    // bundleName and bundleVersion for osgi system,feature 0023460
    private String bundleName;

    private String bundleVersion;

    private ELibraryInstallStatus status = ELibraryInstallStatus.UNKNOWN;

    // status installed in maven
    private ELibraryInstallStatus installStatus = ELibraryInstallStatus.UNKNOWN;

    private boolean isShow = true;

    List<String> installURL;

    private String moduleLocaion;

    private String mavenUri;

    private boolean dynamic;

    private Map<String, Object> extraAttributes = new HashMap<>();

    public static final String SINGLE_QUOTE = "'"; //$NON-NLS-1$

    public static final String QUOTATION_MARK = "\""; //$NON-NLS-1$

    static {
        // the tracker is use in case the service is modifed
        final Bundle bundle = CoreRuntimePlugin.getInstance().getBundle();
        ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver> serviceTracker = new ServiceTracker<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>(
                bundle.getBundleContext(), org.ops4j.pax.url.mvn.MavenResolver.class,
                new ServiceTrackerCustomizer<org.ops4j.pax.url.mvn.MavenResolver, org.ops4j.pax.url.mvn.MavenResolver>() {

                    @Override
                    public org.ops4j.pax.url.mvn.MavenResolver addingService(
                            ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference) {
                        return bundle.getBundleContext().getService(reference);
                    }

                    @Override
                    public void modifiedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;

                    }

                    @Override
                    public void removedService(ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> reference,
                            org.ops4j.pax.url.mvn.MavenResolver service) {
                        mavenResolver = null;
                    }
                });
        serviceTracker.open();
    }

    /**
     * DOC smallet ModuleNeeded class global comment. Detailled comment <br/>
     *
     * $Id: ModuleNeeded.java 38013 2010-03-05 14:21:59Z mhirt $
     *
     */
    public enum ELibraryInstallStatus {
        UNKNOWN,
        INSTALLED,
        UNUSED,
        NOT_INSTALLED,
        DEPLOYED,
        NOT_DEPLOYED;

    }

    /**
     * DOC smallet ModuleNeeded constructor comment.
     *
     * @param context
     * @param moduleName
     * @param informationMsg
     * @param required
     * @param unused
     * @param status
     */
    public ModuleNeeded(String context, String moduleName, String informationMsg, boolean required) {
        this(context, moduleName, informationMsg, required, null, null, null, ELibraryInstallStatus.UNKNOWN);
    }

    public ModuleNeeded(String context, String moduleName, String informationMsg, boolean required, ELibraryInstallStatus status) {
        this(context, moduleName, informationMsg, required, null, null, null, status);
    }

    public ModuleNeeded(String context, String moduleName, String informationMsg, boolean required, List<String> installURL,
            String requiredIf, String mavenUrl) {
        this(context, moduleName, informationMsg, required, installURL, requiredIf, mavenUrl, ELibraryInstallStatus.UNKNOWN);
    }

    /**
     * creates ModuleNeeded from its maven uri. the modeule name is the artifact_ID + "." + artifact_type
     *
     * @param context
     * @param informationMsg
     * @param required
     * @param mvnUri
     */
    public ModuleNeeded(String context, String informationMsg, boolean required, String mvnUri) {
        this(context, null, informationMsg, required, null, null, mvnUri, ELibraryInstallStatus.UNKNOWN);
        MavenArtifact mavenArtifact = MavenUrlHelper.parseMvnUrl(mvnUri);
        if (MavenConstants.DEFAULT_LIB_GROUP_ID.equals(mavenArtifact.getGroupId())
                || StringUtils.isEmpty(mavenArtifact.getVersion())) {
            setModuleName(mavenArtifact.getArtifactId() + "." + mavenArtifact.getType()); //$NON-NLS-1$
        } else {
            setModuleName(mavenArtifact.getArtifactId() + "-" + mavenArtifact.getVersion() + "." + mavenArtifact.getType()); //$NON-NLS-1$//$NON-NLS-2$
        }

    }

    public ModuleNeeded(String context, String moduleName, String informationMsg, boolean required, List<String> installURL,
            String requiredIf, String mavenUrl, ELibraryInstallStatus status) {
        super();
        this.context = context;
        setModuleName(moduleName);
        this.informationMsg = informationMsg;
        this.required = required;
        this.installURL = installURL;
        this.requiredIf = requiredIf;
        this.status = status;
        setMavenUri(mavenUrl);
    }

    public String getRequiredIf() {
        return requiredIf;
    }

    public void setRequiredIf(String requiredIf) {
        this.requiredIf = requiredIf;
    }

    /**
     * Check if the library is required depends the condition of "required if". Note that if the flag "required=true" in
     * the xml of component, it will never check in the required_if.
     *
     * In some cases where we only want to check the basic "required=true" and not the required_if (module view for
     * example), it's possible to simply give null parameter.
     *
     * @param listParam
     * @return
     */
    public boolean isRequired(List<? extends IElementParameter> listParam) {
        if (required) { // if flag required is set, then forget the "required if" test.
            return required;
        }
        boolean isRequired = false;

        if (requiredIf != null && !requiredIf.isEmpty() && listParam != null) {
            isRequired = CoreRuntimePlugin.getInstance().getDesignerCoreService().evaluate(requiredIf, listParam);
        }
        return isRequired;
    }

    /**
     * Getter for installURL.
     *
     * @return the installURL
     */
    public List<String> getInstallURL() {
        return this.installURL;
    }

    /**
     * Sets the installURL.
     *
     * @param installURL the installURL to set
     */
    public void setInstallURL(List<String> installURL) {
        this.installURL = installURL;
    }

    /**
     * Getter for component.
     *
     * @return the component
     */
    public String getContext() {
        return this.context;
    }

    /**
     * Sets the component.
     *
     * @param component the component to set
     */
    public void setContext(String component) {
        this.context = component;
    }

    public String getInformationMsg() {
        return this.informationMsg;
    }

    public void setInformationMsg(String informationMsg) {
        this.informationMsg = informationMsg;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        if (moduleName != null) {
            String mn = moduleName.replace(QUOTATION_MARK, "").replace(SINGLE_QUOTE, ""); //$NON-NLS-1$ //$NON-NLS-2$
            if (mn.indexOf("\\") != -1 || mn.indexOf("/") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
                mn = new Path(mn).lastSegment();
            }
            this.moduleName = mn;
        } else {
            this.moduleName = moduleName;
        }
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public ELibraryInstallStatus getStatus() {
        String mvnUriStatusKey = getMavenUri(true);
        final ELibraryInstallStatus eLibraryInstallStatus = ModuleStatusProvider.getStatusMap().get(mvnUriStatusKey);
        if (eLibraryInstallStatus != null) {
            return eLibraryInstallStatus;
        } else if (mvnUriStatusKey != null) {
            // compute the status of the lib.
            // first use the Library manager service
            ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            Set<String> existLibraries = libManagerService.list();
            if (existLibraries.contains(getModuleName())) {
                status = ELibraryInstallStatus.INSTALLED;
                ModuleStatusProvider.getStatusMap().put(mvnUriStatusKey, status);
            } else {// then try to resolve locally
                resolveStatusLocally(mvnUriStatusKey);
            }

        }
        return this.status;
    }

    public ELibraryInstallStatus getDeployStatus() {
        String mvnUriStatusKey = getMavenUri(true);
        final ELibraryInstallStatus eLibraryDeployStatus = ModuleStatusProvider.getDeployStatusMap().get(mvnUriStatusKey);
        if (eLibraryDeployStatus != null) {
            return eLibraryDeployStatus;
        } else if (mvnUriStatusKey != null) {
            resolveStatusLocally(mvnUriStatusKey);
        }
        return this.installStatus;
    }

    private void resolveStatusLocally(String mvnUriStatusKey) {
        // then try to resolve locally
        String localMavenUri = mvnUriStatusKey.replace("mvn:", "mvn:" + MavenConstants.LOCAL_RESOLUTION_URL + "!"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
            getMavenResolver().resolve(localMavenUri);
            status = ELibraryInstallStatus.INSTALLED;
            installStatus = ELibraryInstallStatus.DEPLOYED;
        } catch (Exception e) {
            status = ELibraryInstallStatus.NOT_INSTALLED;
            installStatus = ELibraryInstallStatus.NOT_DEPLOYED;
        }
        ModuleStatusProvider.getStatusMap().put(mvnUriStatusKey, status);
        ModuleStatusProvider.getDeployStatusMap().put(mvnUriStatusKey, installStatus);
    }

    private MavenResolver getMavenResolver() {
        if (mavenResolver == null) {
            final Bundle bundle = CoreRuntimePlugin.getInstance().getBundle();
            if (bundle != null) {
                ServiceReference<org.ops4j.pax.url.mvn.MavenResolver> mavenResolverService = bundle.getBundleContext()
                        .getServiceReference(org.ops4j.pax.url.mvn.MavenResolver.class);
                if (mavenResolverService != null) {
                    mavenResolver = CoreRuntimePlugin.getInstance().getBundle().getBundleContext()
                            .getService(mavenResolverService);
                } else {
                    throw new RuntimeException("Unable to acquire org.ops4j.pax.url.mvn.MavenResolver");
                }
            }
        }
        return mavenResolver;

    }

    /**
     * Getter for isShow.
     *
     * @return the isShow
     */
    public boolean isShow() {
        return this.isShow;
    }

    /**
     * Sets the isShow.
     *
     * @param isShow the isShow to set
     */
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    /**
     * Getter for mrRequired.
     *
     * @return the mrRequired
     */
    public boolean isMrRequired() {
        return this.mrRequired;
    }

    /**
     * Sets the mrRequired.
     *
     * @param mrRequired the mrRequired to set
     */
    public void setMrRequired(boolean mrRequired) {
        this.mrRequired = mrRequired;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (bundleName == null || "".equals(bundleName.trim())) {
            return moduleName;
        } else if (bundleVersion == null) {
            return moduleName + "[" + bundleName + "]";
        } else {
            return moduleName + "[" + bundleName + ":" + bundleVersion + "]";
        }
    }

    public String getModuleLocaion() {
        return this.moduleLocaion;
    }

    public void setModuleLocaion(String moduleLocaion) {
        this.moduleLocaion = moduleLocaion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 31;
        if (this.getModuleName() != null) {
            hashCode *= this.getModuleName().hashCode();
        }
        if (this.getBundleName() != null) {
            hashCode *= this.getBundleName().hashCode();
        }
        if (this.getBundleVersion() != null) {
            hashCode *= this.getBundleVersion().hashCode();
        }

        hashCode *= new Boolean(this.isRequired()).hashCode();
        hashCode *= new Boolean(this.isMrRequired()).hashCode();
        return hashCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ModuleNeeded)) {
            return false;
        }
        ModuleNeeded other = (ModuleNeeded) obj;

        // ModuleName
        if (other.getModuleName() == null) {
            if (this.getModuleName() != null) {
                return false;
            }
        } else {
            if (this.getModuleName() == null) {
                return false;
            } else if (!other.getModuleName().equals(this.getModuleName())) {
                return false;
            }
        }
        // BundleName
        if (other.getBundleName() == null) {
            if (this.getBundleName() != null) {
                return false;
            }
        } else {
            if (this.getBundleName() == null) {
                return false;
            } else if (!other.getBundleName().equals(this.getBundleName())) {
                return false;
            }
        }
        // BundleVersion
        if (other.getBundleVersion() == null) {
            if (this.getBundleVersion() != null) {
                return false;
            }
        } else {
            if (this.getBundleVersion() == null) {
                return false;
            } else if (!other.getBundleVersion().equals(this.getBundleVersion())) {
                return false;
            }
        }

        // Module Location
        if (other.getModuleLocaion() == null) {
            if (this.getModuleLocaion() != null) {
                return false;
            }
        } else {
            if (this.getModuleLocaion() == null) {
                return false;
            } else if (!other.getModuleLocaion().equals(this.getModuleLocaion())) {
                return false;
            }
        }

        if (other.isRequired() != this.isRequired()) {
            return false;
        }

        if (other.isMrRequired() != this.isMrRequired()) {
            return false;
        }
        return true;
    }

    public String getMavenUri(boolean autoGenerate) {
        if (autoGenerate && (mavenUri == null || "".equals(mavenUri))) { //$NON-NLS-1$
            // get the latest snapshot maven uri from index as default
            ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            String mvnUrisFromIndex = libManagerService.getMavenUriFromIndex(getModuleName());
            if (mvnUrisFromIndex != null) {
                final String[] split = mvnUrisFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                String maxVerstion = null;
                for (String mvnUri : split) {
                    if (maxVerstion == null) {
                        maxVerstion = mvnUri;
                    } else {
                        MavenArtifact lastArtifact = MavenUrlHelper.parseMvnUrl(maxVerstion);
                        MavenArtifact currentArtifact = MavenUrlHelper.parseMvnUrl(mvnUri);
                        if (lastArtifact != null && currentArtifact != null) {
                            String lastV = lastArtifact.getVersion();
                            lastV = lastV.replace(MavenConstants.SNAPSHOT, "");
                            String currentV = currentArtifact.getVersion();
                            currentV = currentV.replace(MavenConstants.SNAPSHOT, "");
                            if (!lastV.equals(currentV)) {
                                Version lastVersion = new Version(lastV);
                                Version currentVersion = new Version(currentV);
                                if (currentVersion.compareTo(lastVersion) > 0) {
                                    maxVerstion = mvnUri;
                                }
                            }
                        }
                    }

                }
                mavenUri = addTypeForMavenUri(maxVerstion, getModuleName());
            } else {
                mavenUri = MavenUrlHelper.generateMvnUrlForJarName(getModuleName(), true, true);
            }
        }
        return mavenUri;
    }

    /**
     * Getter for mavenUriSnapshot.
     *
     * @return the mavenUriSnapshot
     */
    public String getMavenUri() {
        return getMavenUri(false);
    }

    /**
     * Sets the mavenUrl.
     *
     * @param mavenUrl the mavenUrl to set
     */
    public void setMavenUri(String mavenUri) {
        this.mavenUri = addTypeForMavenUri(mavenUri, getModuleName());
    }

    private String addTypeForMavenUri(String uri, String moduleName) {
        // make sure that mvn uri have the package
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri, false);
        if (parseMvnUrl != null && parseMvnUrl.getType() == null) {
            if (moduleName != null && moduleName.lastIndexOf(".") != -1) {
                parseMvnUrl.setType(moduleName.substring(moduleName.lastIndexOf(".") + 1, moduleName.length()));
            } else {
                // set jar by default
                parseMvnUrl.setType(MavenConstants.TYPE_JAR);
            }
            uri = MavenUrlHelper.generateMvnUrl(parseMvnUrl.getGroupId(), parseMvnUrl.getArtifactId(), parseMvnUrl.getVersion(),
                    parseMvnUrl.getType(), parseMvnUrl.getClassifier());
        }
        return uri;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Map<String, Object> getExtraAttributes() {
        return this.extraAttributes;
    }

}
