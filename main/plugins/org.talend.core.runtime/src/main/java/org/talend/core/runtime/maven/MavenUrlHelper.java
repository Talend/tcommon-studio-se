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
package org.talend.core.runtime.maven;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.utils.security.CryptoHelper;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * mvn-uri:='mvn:'[repository-url '!'] group-id '/' artifact-id ['/' [version] ['/' [type] ['/' classifier ]]]]
 */
@SuppressWarnings("nls")
public class MavenUrlHelper {

    public static final String MVN_PROTOCOL = "mvn:";

    public static final String REPO_SEPERATOR = "!";

    public static final String SEPERATOR = "/";

    public static final String MVN_INDEX_SEPARATOR = "|";

    public static final String MVN_INDEX_SPLITER = "\\|";

    public static final String VERSION_LATEST = "LATEST";

    public static final String VERSION_SNAPSHOT = "SNAPSHOT";

    public static final String USER_PASSWORD_SEPARATOR = "@";

    public static final String USER_PASSWORD_SPLITER = ":";

    private static CryptoHelper cryptoHelper;

    public static MavenArtifact parseMvnUrl(String mvnUrl) {
        return parseMvnUrl(mvnUrl, true);
    }

    public static MavenArtifact parseMvnUrl(String mvnUrl, boolean setDefaultValue) {

        if (mvnUrl == null || !mvnUrl.startsWith(MVN_PROTOCOL)) {
            return null;
        }
        MavenArtifact artifact = new MavenArtifact();
        try {
            String substring = mvnUrl.substring(MVN_PROTOCOL.length());

            // repo
            int repoUrlIndex = substring.lastIndexOf(REPO_SEPERATOR);
            if (repoUrlIndex > 0) { // has repo url
                String repoWithUserPwd = substring.substring(0, repoUrlIndex);
                String repoUrl = repoWithUserPwd;
                try {
                    URI repoWithUserPwdURI = new URI(repoWithUserPwd);
                    String userPassword = repoWithUserPwdURI.getUserInfo();
                    URI repoWithoutUserPwdURI = new URI(repoWithUserPwdURI.getScheme(), null, repoWithUserPwdURI.getHost(),
                            repoWithUserPwdURI.getPort(), repoWithUserPwdURI.getPath(), repoWithUserPwdURI.getQuery(),
                            repoWithUserPwdURI.getFragment());
                    repoUrl = repoWithoutUserPwdURI.toString();
                    try {
                        repoUrl = URLDecoder.decode(repoUrl, "UTF-8");
                    } catch (Exception e) {
                        // nothing to do
                    }

                    // username and password
                    if (StringUtils.isNotEmpty(userPassword)) {
                        int splitIndex = userPassword.indexOf(USER_PASSWORD_SPLITER);
                        if (0 < splitIndex) {
                            artifact.setUsername(userPassword.substring(0, splitIndex));
                            if (splitIndex < userPassword.length() - 1) {
                                String password = userPassword.substring(splitIndex + 1);
                                if (password != null) {
                                    String decryptedPassword = decryptPassword(password);
                                    if (decryptedPassword != null) {
                                        password = decryptedPassword;
                                    }
                                }
                                artifact.setPassword(password);
                            }
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

                artifact.setRepositoryUrl(repoUrl);
                substring = substring.substring(repoUrlIndex + 1);
            }
            String[] segments = substring.split(SEPERATOR);
            // only group-id and artifact-id is required
            if (segments.length < 2 || segments[0].trim().length() == 0 || segments[1].trim().length() == 0) {
                return null;
            }
            artifact.setGroupId(segments[0].trim());
            artifact.setArtifactId(segments[1].trim());

            // version
            if (segments.length >= 3 && segments[2].trim().length() > 0) {
                // validate the version
                String verStr = segments[2].trim();
                if (VERSION_LATEST.equals(verStr)) {
                    artifact.setVersion(VERSION_LATEST);
                } else if (VERSION_SNAPSHOT.equals(verStr)) {
                    artifact.setVersion(VERSION_SNAPSHOT);
                } else if (verStr.length() > 0) {
                    // range := ( '[' | '(' ) version ',' version ( ')' | ']' )
                    // TODO, maybe need parse the range for version.
                    if ((verStr.startsWith("[") || verStr.startsWith("(")) && (verStr.endsWith(")") || verStr.endsWith("]"))) {
                        artifact.setVersion(verStr);
                    } else {
                        artifact.setVersion(verStr);
                    }
                }
            }
            if (segments.length >= 4 && segments[3].trim().length() > 0) { // has packaging
                artifact.setType(segments[3].trim());
            }

            if (segments.length >= 5 && segments[4].trim().length() > 0) {// has classifier
                // classifier is can be null.
                artifact.setClassifier(segments[4].trim());
            }

            /*
             * set default values.
             */
            if (artifact.getVersion() == null && setDefaultValue) {
                // if not set, will be LATEST by default
                artifact.setVersion(VERSION_LATEST);
            }
            if (artifact.getType() == null && setDefaultValue) {
                // if not set, will be jar by default
                artifact.setType(MavenConstants.TYPE_JAR);
            }
        } catch (Exception e) {
            ExceptionHandler.process(new Exception("Problem happened when install this maven URL: " + mvnUrl, e));
            return null;
        }

        return artifact;

    }

    /**
     * if startsWith "mvn:", will return true.
     */
    public static boolean isMvnUrl(String str) {
        MavenArtifact parseMvnUrl = parseMvnUrl(str);
        if (parseMvnUrl != null) {
            return true;
        }
        return false;
    }

    public static String generateMvnUrlForJarName(String jarName, boolean withPackage, boolean snapshots) {
        if (jarName != null && jarName.length() > 0) {
            String artifactId = jarName;
            String type = null;
            if (jarName.endsWith(MavenConstants.TYPE_JAR)) { // remove the extension .jar
                artifactId = jarName.substring(0, jarName.lastIndexOf(MavenConstants.TYPE_JAR) - 1);
                if (withPackage) {
                    type = MavenConstants.TYPE_JAR;
                }
            } else { // need process normal files?
                int dotIndex = jarName.lastIndexOf('.');
                if (dotIndex == 0) {// don't support the file without name, only extension.
                    return null;
                } else if (dotIndex > 0) {
                    artifactId = jarName.substring(0, dotIndex);
                    if (withPackage) {
                        type = jarName.substring(dotIndex + 1);
                    }
                }
            }

            // fix for TUP-3276 , only use the defult version 6.0.0 now in the maven uri
            // String currentVersion = VersionUtils.getTalendVersion();
            // if (currentVersion == null) {
            // currentVersion = MavenConstants.DEFAULT_VERSION;
            // }
            String currentVersion = MavenConstants.DEFAULT_VERSION;
            if (snapshots) {
                currentVersion = currentVersion + MavenConstants.SNAPSHOT;
            }

            return generateMvnUrl(MavenConstants.DEFAULT_LIB_GROUP_ID, artifactId, currentVersion, type, null);

        }
        return null;

    }

    /**
     * will build the mvn url with default groupId and version.
     * "mvn:org.talend.libraries/<jarNameWithoutExtension>/currentVersion/<extension>"
     */
    public static String generateMvnUrlForJarName(String jarName) {
        return generateMvnUrlForJarName(jarName, true, true);
    }

    public static String generateMvnUrl(MavenArtifact mArt) {
        return generateMvnUrl(mArt, false);
    }

    /**
     * The generated mvn url is only used to display on UI
     * 
     * @param mArt
     * @param encryptPassword
     * @return
     */
    public static String generateMvnUrl(MavenArtifact mArt, boolean encryptPassword) {
        return generateMvnUrl(mArt.getUsername(), mArt.getPassword(), mArt.getRepositoryUrl(), mArt.getGroupId(),
                mArt.getArtifactId(), mArt.getVersion(), mArt.getType(), mArt.getClassifier(), encryptPassword);
    }

    /**
     * 
     * mvn:groupId/artifactId/version/packaging/classifier
     */
    public static String generateMvnUrl(String groupId, String artifactId, String version, String packaging, String classifier) {
        return generateMvnUrl(null, groupId, artifactId, version, packaging, classifier);
    }

    public static String generateMvnUrl(String repositoryId, String groupId, String artifactId, String version, String packaging,
            String classifier) {
        return generateMvnUrl(null, null, repositoryId, groupId, artifactId, version, packaging, classifier, true);
    }

    public static String generateMvnUrl(String username, String password, String repositoryId, String groupId, String artifactId,
            String version, String packaging, String classifier, boolean encryptPassword) {
        Assert.isNotNull(groupId);
        Assert.isNotNull(artifactId);

        StringBuffer mvnUrl = new StringBuffer(100);
        mvnUrl.append(MVN_PROTOCOL);

        if (StringUtils.isNotEmpty(repositoryId)) {
            String repositoryUrl = repositoryId;
            if (StringUtils.isNotEmpty(username)) {
                if (password == null) {
                    password = "";
                }
                if (encryptPassword) {
                    password = encryptPassword(password);
                }
                String usernamePassword = username + USER_PASSWORD_SPLITER + password;
                try {
                    URL repoWithoutUserPasswordUrl = new URL(repositoryId);
                    if (repoWithoutUserPasswordUrl != null) {
                        if (StringUtils.isEmpty(repoWithoutUserPasswordUrl.getHost())) {
                            throw new Exception("Bad url, can't resolve it: " + repositoryId);
                        } else {
                            URI repoWithUserPasswordURI = new URI(repoWithoutUserPasswordUrl.getProtocol(), usernamePassword,
                                    repoWithoutUserPasswordUrl.getHost(), repoWithoutUserPasswordUrl.getPort(),
                                    repoWithoutUserPasswordUrl.getPath(), repoWithoutUserPasswordUrl.getQuery(),
                                    repoWithoutUserPasswordUrl.getRef());
                            repositoryUrl = repoWithUserPasswordURI.toString();
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            mvnUrl.append(repositoryUrl).append(REPO_SEPERATOR);
        }

        mvnUrl.append(groupId);
        mvnUrl.append(SEPERATOR);
        mvnUrl.append(artifactId);

        if (version != null) {
            mvnUrl.append(SEPERATOR);
            mvnUrl.append(version);
        } else {
            if (packaging != null || classifier != null) { // if has packaging or classifier
                // add one empty seperator
                mvnUrl.append(SEPERATOR);
            }
        }
        if (packaging != null) {
            mvnUrl.append(SEPERATOR);
            mvnUrl.append(packaging);
        } else {
            if (classifier != null) { // if has classifier
                // add one empty seperator
                mvnUrl.append(SEPERATOR);
            }
        }
        if (classifier != null) {
            mvnUrl.append(SEPERATOR);
            mvnUrl.append(classifier);
        }

        return mvnUrl.toString();
    }

    public static String addTypeForMavenUri(String uri, String moduleName) {
        // make sure that mvn uri have the package
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri, false);
        if (parseMvnUrl != null && parseMvnUrl.getType() == null) {
            if (moduleName != null && moduleName.lastIndexOf(".") != -1) {
                parseMvnUrl.setType(moduleName.substring(moduleName.lastIndexOf(".") + 1, moduleName.length()));
            } else {
                // set jar by default
                parseMvnUrl.setType(MavenConstants.TYPE_JAR);
            }
            uri = MavenUrlHelper.generateMvnUrl(parseMvnUrl);
        }
        return uri;
    }

    private static CryptoHelper getCryptoHelper() {
        if (cryptoHelper == null) {
            cryptoHelper = CryptoHelper.getDefault();
        }
        return cryptoHelper;
    }

    public static String encryptPassword(String password) {
        return getCryptoHelper().encrypt(password);
    }

    public static String decryptPassword(String password) {
        return getCryptoHelper().decrypt(password);
    }

	public static String generateModuleNameByMavenURI(String uri) {
        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri, true);
        if (parseMvnUrl == null) {
            return null;
        }
        return parseMvnUrl.getFileName();
    }

    public static String getArtifactPath(MavenArtifact artifact) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(artifact.getGroupId().replaceAll("\\.", "/"));

        buffer.append("/");
        buffer.append(artifact.getArtifactId());

        if (artifact.getVersion() != null) {
            buffer.append("/");
            buffer.append(artifact.getVersion());
        }

        buffer.append("/");
        buffer.append(artifact.getArtifactId());
        if (artifact.getVersion() != null) {
            buffer.append("-");
            buffer.append(artifact.getVersion());
        }
        if (artifact.getClassifier() != null) {
            buffer.append("-");
            buffer.append(artifact.getClassifier());
        }
        if (artifact.getType() != null) {
            buffer.append(".");
            buffer.append(artifact.getType());
        } else {
            // add default extension
            buffer.append(".jar");
        }
        return buffer.toString();
    }
}
