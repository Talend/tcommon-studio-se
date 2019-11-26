// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.updates.runtime.engine.P2Manager;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.utils.JarMenifestUtil;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexManager {

    public static final String INDEX = "index"; //$NON-NLS-1$

    public static final String COMPONENT_GROUP_ID = "org.talend.components"; //$NON-NLS-1$

    public static final String ELEM_COMPONENTS = "components"; //$NON-NLS-1$

    public static final String ELEM_COMPONENT = "component"; //$NON-NLS-1$

    public static final String XPATH_INDEX_COMPONENT = "//" + ELEM_COMPONENTS + '/' + ELEM_COMPONENT; //$NON-NLS-1$

    class MissingSettingException extends IllegalArgumentException {

        private static final long serialVersionUID = -4386085265203515607L;

        public MissingSettingException(String s) {
            super(s);
        }

    }

    public ComponentIndexBean createIndexBean4Patch(File patchZipFile, Type type) {
        if (patchZipFile == null || !patchZipFile.exists() || patchZipFile.isDirectory()
                || !patchZipFile.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX) || type == null) {
            return null;
        }
        String name = StringUtils.removeEnd(patchZipFile.getName(), FileExtensions.ZIP_FILE_SUFFIX);
        String bundleId = null;
        String bundleVersion = null;
        String mvnUri = null;
        if (type == Type.PLAIN_ZIP) {
            bundleId = "org.talend.studio.patch.plainzip"; //$NON-NLS-1$
            bundleVersion = name;
        } else if (type == Type.P2_PATCH) {
            bundleId = "org.talend.studio.patch.updatesite"; //$NON-NLS-1$
            bundleVersion = P2Manager.getInstance().getP2Version(patchZipFile);
        }
        String artifactId = StringUtils.substringBeforeLast(name, "-"); //$NON-NLS-1$
        String artifactVersion = StringUtils.substringAfterLast(name, "-"); //$NON-NLS-1$
        mvnUri = MavenUrlHelper.generateMvnUrl(bundleId, artifactId, artifactVersion, FileExtensions.ZIP_EXTENSION, null);
        if (name != null && bundleId != null && bundleVersion != null && mvnUri != null) {
            ComponentIndexBean indexBean = new ComponentIndexBean();
            boolean set = indexBean.setRequiredFieldsValue(name, bundleId, bundleVersion, mvnUri);
            indexBean.setValue(ComponentIndexNames.types, PathUtils.convert2StringTypes(Arrays.asList(type)));
            if (set) {
                return indexBean;
            }
        }
        return null;
    }

    /**
     *
     * create one default index bean which based one the component zip file directly.
     *
     * bundleId, version, mvn_uri are required
     */
    public ComponentIndexBean create(File componentZipFile) {
        if (componentZipFile == null || !componentZipFile.exists() || componentZipFile.isDirectory()
                || !componentZipFile.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
            return null;
        }

        String name = null;
        String bundleId = null;
        String bundleVersion = null;
        String mvnUri = null;

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(componentZipFile);

            Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries();
            while (enumeration.hasMoreElements()) {
                final ZipEntry zipEntry = enumeration.nextElement();
                String path = zipEntry.getName();
                if (path.endsWith(FileExtensions.JAR_FILE_SUFFIX)) { // is jar
                    // if it's bundle, not from other folder, like lib, m2 repository.
                    IPath p = new Path(path);
                    // must be in plugins
                    if (p.segmentCount() > 1 && p.removeLastSegments(1).lastSegment().equals(UpdatesHelper.FOLDER_PLUGINS)) {
                        if (UpdatesHelper.isComponentJar(zipFile.getInputStream(zipEntry))) {
                            JarInputStream jarEntryStream = null;
                            try {
                                // must use another stream
                                jarEntryStream = new JarInputStream(zipFile.getInputStream(zipEntry));
                                // find the bundleId and version
                                Manifest manifest = jarEntryStream.getManifest();
                                if (manifest != null) {
                                    bundleId = JarMenifestUtil.getBundleSymbolicName(manifest);
                                    bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
                                }
                                boolean checkManifest = StringUtils.isBlank(bundleId) || StringUtils.isBlank(bundleVersion);

                                // find the pom.properties
                                JarEntry jarEntry = null;
                                while ((jarEntry = jarEntryStream.getNextJarEntry()) != null) {
                                    final String entryPath = jarEntry.getName();
                                    if (checkManifest && JarFile.MANIFEST_NAME.equalsIgnoreCase(entryPath)) {
                                        manifest = new Manifest();
                                        manifest.read(jarEntryStream);
                                        bundleId = JarMenifestUtil.getBundleSymbolicName(manifest);
                                        bundleVersion = JarMenifestUtil.getBundleVersion(manifest);
                                        checkManifest = false;
                                    }
                                    final Path fullPath = new Path(entryPath);
                                    final String fileName = fullPath.lastSegment();

                                    /*
                                     * for example,
                                     * META-INF/maven/org.talend.components/components-splunk/pom.properties
                                     */
                                    if (fileName.equals("pom.properties") //$NON-NLS-1$
                                            && entryPath.contains("META-INF/maven/")) { //$NON-NLS-1$

                                        // FIXME, didn't find one way to read the inner jar
                                        // final InputStream propStream = jarFile.getInputStream(jarEntry);
                                        // if (propStream != null) {
                                        // Properties pomProp = new Properties();
                                        // pomProp.load(propStream);
                                        //
                                        // String version = pomProp.getProperty("version"); //$NON-NLS-1$
                                        // String groupId = pomProp.getProperty("groupId"); //$NON-NLS-1$
                                        // String artifactId = pomProp.getProperty("artifactId"); //$NON-NLS-1$
                                        // mvnUri = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version,
                                        // FileExtensions.ZIP_FILE_SUFFIX, null);
                                        //
                                        // propStream.close();
                                        // }

                                        // FIXME, try the path way
                                        // META-INF/maven/org.talend.components/components-splunk
                                        IPath tmpMavenPath = fullPath.removeLastSegments(1);
                                        String artifactId = tmpMavenPath.lastSegment(); // components-splunk
                                        // META-INF/maven/org.talend.components
                                        tmpMavenPath = tmpMavenPath.removeLastSegments(1);
                                        String groupId = tmpMavenPath.lastSegment(); // org.talend.components

                                        mvnUri = MavenUrlHelper.generateMvnUrl(groupId, artifactId, bundleVersion,
                                                FileExtensions.ZIP_EXTENSION, null);

                                    } else
                                    /*
                                     * /OSGI-INF/installer$$splunk.xml
                                     */
                                    if (fileName.endsWith(FileExtensions.XML_FILE_SUFFIX)
                                            && fileName.startsWith(UpdatesHelper.NEW_COMPONENT_PREFIX)
                                            && entryPath.contains(UpdatesHelper.FOLDER_OSGI_INF + '/')) {
                                        name = fullPath.removeFileExtension().lastSegment();
                                        name = name.substring(name.indexOf(UpdatesHelper.NEW_COMPONENT_PREFIX)
                                                + UpdatesHelper.NEW_COMPONENT_PREFIX.length());
                                    }
                                }
                            } catch (IOException e) {
                                //
                            } finally {
                                try {
                                    if (jarEntryStream != null) {
                                        jarEntryStream.close();
                                    }
                                } catch (IOException e) {
                                    //
                                }
                            }

                        }
                    }
                }
            }

        } catch (ZipException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        } catch (IOException e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        // set the required
        if (name != null && bundleId != null && bundleVersion != null && mvnUri != null) {
            final ComponentIndexBean indexBean = new ComponentIndexBean();
            final boolean set = indexBean.setRequiredFieldsValue(name, bundleId, bundleVersion, mvnUri);
            indexBean.setValue(ComponentIndexNames.types, PathUtils.convert2StringTypes(Arrays.asList(Type.TCOMP_V0)));
            if (set) {
                return indexBean;
            }
        }
        return null;
    }

}
