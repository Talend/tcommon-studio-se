// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.runtime.maven;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenArtifact implements Cloneable {

    private static final char GROUP_SEPARATOR = '.';

    private static final char ARTIFACT_SEPARATOR = '-';

    private String repositoryUrl, groupId, artifactId, version, type, classifier, description, url, license, licenseUrl,
            distribution, username, password, lastUpdated, sha1, md5;

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLicense() {
        return this.license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return this.licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getDistribution() {
        return this.distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     *
     * DOC ggu Comment method "getFileName".
     *
     * @return if need full path, try PomUtil.getArtifactPath
     */
    public String getFileName() {
        return getFileName(true);
    }

    /**
     *
     * DOC ggu Comment method "getFileName".
     *
     * @return if need full path, try PomUtil.getArtifactPath
     */
    public String getFileName(boolean stripVersion) {
        StringBuilder name = new StringBuilder(128);

        name.append(getArtifactId());
        if (!stripVersion || !MavenConstants.DEFAULT_LIB_GROUP_ID.equals(getGroupId())) {
            name.append(ARTIFACT_SEPARATOR).append(getVersion());
        }
        if (StringUtils.isNotEmpty(getClassifier())) {
            name.append(ARTIFACT_SEPARATOR).append(getClassifier());
        }
        name.append(GROUP_SEPARATOR);
        if (StringUtils.isNotEmpty(getType())) {
            name.append(getType());
        } else {
            name.append(MavenConstants.TYPE_JAR);
        }
        return name.toString();
    }

    public static int compareVersion(String v1, String v2) {
        if (v1 == null && v2 == null) {
            return 0;
        } else if (v1 == null) {
            return -1;
        } else if (v2 == null) {
            return 1;
        }
        String[] split1 = v1.split("\\.", 4);
        String[] split2 = v2.split("\\.", 4);
        Integer compareResult = null;
        for (int i = 0; i < split1.length && i < split2.length; i++) {
            String n1 = split1[i];
            String n2 = split2[i];

            Integer tempResult = null;
            try {
                tempResult = Integer.valueOf(n1) - Integer.valueOf(n2);
            } catch (Exception e) {
                if (CommonsPlugin.isDebugMode()) {
                    ExceptionHandler.process(new Exception("Can't compare: " + n1 + " <> " + n2, e));
                }
            }
            if (tempResult == null) {
                String prefix1 = n1;
                String suffix1 = "";
                int index = n1.indexOf("-");
                if (0 <= index) {
                    prefix1 = n1.substring(0, index);
                    suffix1 = n1.substring(index + 1);
                }

                String prefix2 = n2;
                String suffix2 = "";
                index = n2.indexOf("-");
                if (0 <= index) {
                    prefix2 = n2.substring(0, index);
                    suffix2 = n2.substring(index + 1);
                }
                try {
                    tempResult = Integer.valueOf(prefix1) - Integer.valueOf(prefix2);
                } catch (Exception e) {
                    if (CommonsPlugin.isDebugMode()) {
                        ExceptionHandler.process(new Exception("Can't compare: " + prefix1 + " <> " + prefix2, e));
                    }
                    tempResult = prefix1.compareTo(prefix2);
                }
                if (tempResult == 0) {
                    try {
                        tempResult = Integer.valueOf(suffix1) - Integer.valueOf(suffix2);
                    } catch (Exception e) {
                        if (CommonsPlugin.isDebugMode()) {
                            ExceptionHandler.process(new Exception("Can't compare: " + suffix1 + " <> " + suffix2, e));
                        }
                        tempResult = suffix1.compareTo(suffix2);
                    }
                }
            }
            if (tempResult != 0) {
                compareResult = tempResult;
                break;
            }
        }
        if (compareResult == null) {
            compareResult = split1.length - split2.length;
        }
        return compareResult;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((repositoryUrl == null) ? 0 : repositoryUrl.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((license == null) ? 0 : license.hashCode());
        result = prime * result + ((licenseUrl == null) ? 0 : licenseUrl.hashCode());
        result = prime * result + ((distribution == null) ? 0 : distribution.hashCode());
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
        if (!(obj instanceof MavenArtifact)) {
            return false;
        }
        MavenArtifact other = (MavenArtifact) obj;
        if (artifactId == null) {
            if (other.artifactId != null) {
                return false;
            }
        } else if (!artifactId.equals(other.artifactId)) {
            return false;
        }
        if (classifier == null) {
            if (other.classifier != null) {
                return false;
            }
        } else if (!classifier.equals(other.classifier)) {
            return false;
        }
        if (groupId == null) {
            if (other.groupId != null) {
                return false;
            }
        } else if (!groupId.equals(other.groupId)) {
            return false;
        }
        if (repositoryUrl == null) {
            if (other.repositoryUrl != null) {
                return false;
            }
        } else if (!repositoryUrl.equals(other.repositoryUrl)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }

        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }

        if (license == null) {
            if (other.license != null) {
                return false;
            }
        } else if (!license.equals(other.license)) {
            return false;
        }

        if (licenseUrl == null) {
            if (other.licenseUrl != null) {
                return false;
            }
        } else if (!licenseUrl.equals(other.licenseUrl)) {
            return false;
        }

        if (distribution == null) {
            if (other.distribution != null) {
                return false;
            }
        } else if (!distribution.equals(other.distribution)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MavenArtifact [groupId=" + this.groupId + ", artifactId=" + this.artifactId + ", version=" + this.version + "]";

    }

    @Override
    public MavenArtifact clone() throws CloneNotSupportedException {
        return (MavenArtifact) super.clone();
    }

}
