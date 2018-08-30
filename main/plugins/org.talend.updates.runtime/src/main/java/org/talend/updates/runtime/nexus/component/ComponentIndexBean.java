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
package org.talend.updates.runtime.nexus.component;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexBean implements Cloneable {

    static final String DOT_SNAPSHOT = ".SNAPSHOT"; //$NON-NLS-1$

    static final String SNAPSHOT = "-SNAPSHOT"; //$NON-NLS-1$

    private JSONObject settings = new JSONObject();

    public int size() {
        return settings.length();
    }

    /**
     * the name can't be null and empty "", but can be spaces and other chars
     */
    private boolean validName(String name) {
        return StringUtils.isNotEmpty(name);
    }

    public boolean setValue(ComponentIndexNames name, String value) {
        return setValue(name.getName(), value);
    }

    public boolean setValue(String name, String value) {
        try {
            if (!validName(name)) {
                return false;
            }
            // if(StringUtils.isNoneBlank(value)){
            if (value != null) { // only deal with the null, not empty and spaces
                settings.put(name, value);
                return true;
            } else if (settings.has(name)) {// existed
                return remove(name);// remove if null
            } else { // not existed
                return false;
            }
        } catch (JSONException e) {
            //
        }
        return false;
    }

    public boolean setRequiredFieldsValue(String name, String bundleId, String version, String mvnURI) {
        setValue(ComponentIndexNames.name, name);
        setValue(ComponentIndexNames.bundle_id, bundleId);
        setValue(ComponentIndexNames.version, version);
        setValue(ComponentIndexNames.mvn_uri, mvnURI);

        return validRequired();
    }

    public String getValue(String name) {
        try {
            if (validName(name) && settings.has(name)) {
                return settings.getString(name);
            }
        } catch (JSONException e) {
            //
        }
        return null;
    }

    public String getValue(ComponentIndexNames name) {
        return getValue(name.getName());
    }

    public boolean remove(String name) {
        if (validName(name) && settings.has(name)) {
            final Object obj = settings.remove(name);
            if (obj != null) {
                return true;
            }
        }

        return false;
    }

    public boolean remove(ComponentIndexNames name) {
        return remove(name.getName());
    }

    public String getName() {
        return getValue(ComponentIndexNames.name);
    }

    public String getBundleId() {
        return getValue(ComponentIndexNames.bundle_id);
    }

    public String getVersion() {
        return getValue(ComponentIndexNames.version);
    }

    public String getDescription() {
        return getValue(ComponentIndexNames.description);
    }

    public String getProduct() {
        return getValue(ComponentIndexNames.product);
    }

    public String[] getProducts() {
        final String products = getProduct();
        if (StringUtils.isNotBlank(products)) {
            return products.split(",");
        }
        return new String[0];
    }

    public String getImageMvnURI() {
        return getValue(ComponentIndexNames.image_mvn_uri);
    }

    public String getMvnURI() {
        return getValue(ComponentIndexNames.mvn_uri);
    }

    public String getLicense() {
        return getValue(ComponentIndexNames.license);
    }

    public String getLicenseURI() {
        return getValue(ComponentIndexNames.license_uri);
    }

    public String getCompatibleStudioVersion() {
        return getValue(ComponentIndexNames.compatibleStudioVersion);
    }

    public String getTypes() {
        return getValue(ComponentIndexNames.types);
    }

    public String getCategories() {
        return getValue(ComponentIndexNames.categories);
    }

    public String getDegradable() {
        return getValue(ComponentIndexNames.degradable);
    }

    public boolean validRequired() {
        for (ComponentIndexNames n : ComponentIndexNames.values()) {
            if (n.isRequired() && !settings.has(n.getName())) {
                return false;
            }
        }
        return true; // all set
    }

    public ComponentIndexBean cloneWithoutSnapshot() throws CloneNotSupportedException {
        ComponentIndexBean clone = this.clone();
        if (isSnapshot()) {
            if (!clone.validRequired()) {
                throw new IllegalArgumentException("Missing required settings");
            }
            // remove snapshot
            String version = clone.getVersion();
            if (version.endsWith(DOT_SNAPSHOT)) {
                version = version.substring(0, version.indexOf(DOT_SNAPSHOT));
            }
            if (version.endsWith(SNAPSHOT)) {
                version = version.substring(0, version.indexOf(SNAPSHOT));
            }

            // set new version
            clone.setValue(ComponentIndexNames.version, version);

            // update mvn uri
            final MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(clone.getMvnURI());
            artifact.setVersion(clone.getVersion());
            String newMvnURI = MavenUrlHelper.generateMvnUrl(artifact.getGroupId(), artifact.getArtifactId(),
                    artifact.getVersion(), artifact.getType(), artifact.getClassifier());
            clone.setValue(ComponentIndexNames.mvn_uri, newMvnURI);

        }

        return clone;
    }

    public boolean isSnapshot() {
        return getVersion().endsWith(SNAPSHOT) || getVersion().endsWith(DOT_SNAPSHOT);
    }

    public MavenArtifact getMavenArtifact() {
        return MavenUrlHelper.parseMvnUrl(getMvnURI());
    }

    @Override
    protected ComponentIndexBean clone() throws CloneNotSupportedException {
        ComponentIndexBean clone = new ComponentIndexBean();
        try {
            JSONObject cloneSettings = new JSONObject(this.settings.toString());
            clone.settings = cloneSettings;
        } catch (JSONException e) {
            //
        }
        return clone;
    }

    @Override
    public String toString() {
        return getName() + '|' + getBundleId() + '|' + getVersion() + '|' + getTypes();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getBundleId() == null) ? 0 : getBundleId().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getMvnURI() == null) ? 0 : getMvnURI().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ComponentIndexBean))
            return false;
        ComponentIndexBean other = (ComponentIndexBean) obj;

        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;

        if (getBundleId() == null) {
            if (other.getBundleId() != null)
                return false;
        } else if (!getBundleId().equals(other.getBundleId()))
            return false;

        if (getVersion() == null) {
            if (other.getVersion() != null)
                return false;
        } else if (!getVersion().equals(other.getVersion()))
            return false;

        if (getMvnURI() == null) {
            if (other.getMvnURI() != null)
                return false;
        } else if (!getMvnURI().equals(other.getMvnURI()))
            return false;

        return true;
    }

    public boolean sameComponent(ComponentIndexBean other) {
        if (other == null) {
            return false;
        }
        // same bundle, should be same component
        return other.getBundleId().equals(this.getBundleId()); // only check bundle id
    }

    public int compareVersion(ComponentIndexBean other) {
        if (other != null && sameComponent(other)) { // must be same component
            final String otherV = other.getVersion();
            final String v = this.getVersion();
            if (StringUtils.isNotBlank(otherV) && StringUtils.isNotBlank(v)) {
                try {
                    org.osgi.framework.Version oV = new org.osgi.framework.Version(otherV);
                    org.osgi.framework.Version tV = new org.osgi.framework.Version(v);
                    final int compareTo = tV.compareTo(oV);
                    // unify
                    if (compareTo > 0) {
                        return 1;
                    } else if (compareTo < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (IllegalArgumentException e) {
                    //
                }

            }
        }

        return -2; // different or unknown
    }

}
