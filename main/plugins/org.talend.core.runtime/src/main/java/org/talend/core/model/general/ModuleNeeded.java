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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Version;
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

    private ELibraryInstallStatus status = ELibraryInstallStatus.NOT_INSTALLED;

    // status installed in maven
    private ELibraryInstallStatus installStatus = ELibraryInstallStatus.NOT_DEPLOYED;

    private boolean isShow = true;

    List<String> installURL;

    private String moduleLocaion;

    private String mavenUriFromConfiguration;

    private String mavenUri;

    private boolean excludeDependencies = false;

    private boolean dynamic;

    private Map<String, Object> extraAttributes = new HashMap<>();

    public static final String SINGLE_QUOTE = "'"; //$NON-NLS-1$

    public static final String QUOTATION_MARK = "\""; //$NON-NLS-1$

    public static final String UNKNOWN = "Unknown";

    ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
            ILibraryManagerService.class);

    /**
     * DOC smallet ModuleNeeded class global comment. Detailled comment <br/>
     *
     * $Id: ModuleNeeded.java 38013 2010-03-05 14:21:59Z mhirt $
     *
     */
    public enum ELibraryInstallStatus {
        INSTALLED,
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
        this(context, moduleName, informationMsg, required, null, null, null);
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
        this(context, null, informationMsg, required, null, null, mvnUri);
        MavenArtifact mavenArtifact = MavenUrlHelper.parseMvnUrl(mvnUri);
        if (MavenConstants.DEFAULT_LIB_GROUP_ID.equals(mavenArtifact.getGroupId())
                || StringUtils.isEmpty(mavenArtifact.getVersion())) {
            setModuleName(mavenArtifact.getArtifactId() + "." + mavenArtifact.getType()); //$NON-NLS-1$
        } else {
            setModuleName(mavenArtifact.getArtifactId() + "-" + mavenArtifact.getVersion() + "." + mavenArtifact.getType()); //$NON-NLS-1$//$NON-NLS-2$
        }

    }

    public ModuleNeeded(String context, String moduleName, String informationMsg, boolean required, List<String> installURL,
            String requiredIf, String mavenUrl) {
        super();
        this.context = context;
        setModuleName(moduleName);
        this.informationMsg = informationMsg;
        this.required = required;
        this.installURL = installURL;
        this.requiredIf = requiredIf;
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
            if (mn.indexOf("\\") != -1 || (mn.indexOf("/") != -1 && mn.endsWith(".jar"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                mn = new Path(mn).lastSegment();
            }else if(mn.indexOf("/") != -1){
                mn = mn.split("/")[1]+".jar";//$NON-NLS-1$
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
        ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        libManagerService.checkModuleStatus(this);
        String mvnUriStatusKey = getMavenUri();
        this.status = ModuleStatusProvider.getStatus(mvnUriStatusKey);
        return this.status;
    }

    public ELibraryInstallStatus getDeployStatus() {
        ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        libManagerService.checkModuleStatus(this);
        String mvnUriStatusKey = getMavenUri();

        this.installStatus = ModuleStatusProvider.getDeployStatus(mvnUriStatusKey);
        return this.installStatus;
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
        if (this.moduleLocaion == null) {
            moduleLocaion = libManagerService.getPlatformURLFromIndex(moduleName);
        }
        return moduleLocaion;
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
        if (this.getId() != null) {
            hashCode *= this.getId().hashCode();
        }
        if (this.getModuleName() != null) {
            hashCode *= this.getModuleName().hashCode();
        }
        if (this.getBundleName() != null) {
            hashCode *= this.getBundleName().hashCode();
        }
        if (this.getBundleVersion() != null) {
            hashCode *= this.getBundleVersion().hashCode();
        }
        if (this.getModuleLocaion() != null) {
            hashCode *= this.getModuleLocaion().hashCode();
        }

        hashCode *= this.getDefaultMavenURI().hashCode();

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

        // Module context
        if (other.getContext() == null) {
            if (this.getContext() != null) {
                return false;
            }
        } else {
            if (this.getContext() == null) {
                return false;
            } else if (!other.getContext().equals(this.getContext())) {
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

        // maven uri
        if (!other.getDefaultMavenURI().equals(this.getDefaultMavenURI())) {
            return false;
        }

        return true;

    }

    /**
     * Get the maven uri from talend configuration: component-IMPORT or librariesNeeded extension
     */
    public String getMavenURIFromConfiguration() {
        return this.mavenUriFromConfiguration;
    }

    /**
     * Get the maven uri from talend configuration or generate a default one if <mavenUriFromConfiguration> is null
     */
    public String getDefaultMavenURI() {
        mavenUri = initURI();
        return mavenUri;
    }

    /**
     * 
     * Get the maven URI with priority:custom URI ,URI from configuration, generated by default
     * 
     * @return
     */
    public String getMavenUri() {
        if (getCustomMavenUri() != null) {
            return getCustomMavenUri();
        }
        mavenUri = initURI();
        return mavenUri;
    }

    /**
     * 
     * DOC wchen Comment method "initURI".
     * 
     * @return
     */
    private String initURI() {
        if (mavenUri == null) {
            if (StringUtils.isEmpty(mavenUriFromConfiguration)) {
                // get the latest snapshot maven uri from index as default
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
                                String currentV = currentArtifact.getVersion();
                                if (!lastV.equals(currentV)) {
                                    Version lastVersion = getVerstion(lastArtifact);
                                    Version currentVersion = getVerstion(currentArtifact);
                                    if (currentVersion.compareTo(lastVersion) > 0) {
                                        maxVerstion = mvnUri;
                                    }
                                }
                            }
                        }

                    }
                    mavenUri = MavenUrlHelper.addTypeForMavenUri(maxVerstion, getModuleName());
                } else {
                    mavenUri = MavenUrlHelper.generateMvnUrlForJarName(getModuleName(), true, true);
                }
            } else {
                mavenUri = mavenUriFromConfiguration;
            }
        }
        return mavenUri;
    }

    private Version getVerstion(MavenArtifact artifact) {
        String versionStr = artifact.getVersion();
        int index = versionStr.indexOf("-");
        if (index != -1) {
            versionStr = versionStr.split("-")[0];
        }
        Version version = null;
        try {
            version = new Version(versionStr);
        } catch (Exception e) {
            version = new Version(0, 0, 0);
        }
        return version;
    }

    /**
     * Sets the mavenUrl.
     *
     * @param mavenUrl the mavenUrl to set
     */
    public void setMavenUri(String mavenUri) {
        this.mavenUriFromConfiguration = MavenUrlHelper.addTypeForMavenUri(mavenUri, getModuleName());
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

    public String getCustomMavenUri() {
        String originalURI = initURI();
        String customURI = libManagerService.getCustomMavenURI(originalURI);
        if (originalURI != null && !originalURI.equals(customURI)) {
            return customURI;
        } else {
            return null;
        }
    }

    public void setCustomMavenUri(String customURI) {
        String customURIWithType = MavenUrlHelper.addTypeForMavenUri(customURI, getModuleName());
        libManagerService.setCustomMavenURI(getDefaultMavenURI(), customURIWithType);
    }

    public boolean isExcludeDependencies() {
        return this.excludeDependencies;
    }

    public void setExcludeDependencies(boolean excludeDependencies) {
        this.excludeDependencies = excludeDependencies;
    }

}
