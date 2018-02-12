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
package org.talend.core.hadoop.version.custom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.manifest.ManifestAnalyzeUtil;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.Project;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * created by ycbai on 2013-3-18 Detailled comment
 * 
 */
public class CustomVersionLibsManager {

    private final static String DESC_PROP_FILE = "desc.properties"; //$NON-NLS-1$

    private final static String EMPTY_STRING = ""; //$NON-NLS-1$

    private static CustomVersionLibsManager manager = new CustomVersionLibsManager();

    private Map<String, String> descMap = new HashMap<String, String>();

    private ILibraryManagerService librairesService;

    private String tmpFolder;

    /**
     * DOC ycbai CustomVersionJarsManager constructor comment.
     */
    private CustomVersionLibsManager() {
        librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
        tmpFolder = renewTmpFolder();
    }

    public static CustomVersionLibsManager getInstance() {
        return manager;
    }

    public String getLibDescription(String jarName) {
        String desc = descMap.get(jarName);
        if (desc != null) {
            return desc;
        }
        String jarPath = tmpFolder + File.separator + jarName;
        try {
            boolean isOk = false;
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                isOk = librairesService.retrieve(jarName, tmpFolder, false, new NullProgressMonitor());
            }
            if (isOk) {
                desc = getJarDescFromManifest(jarFile);
                if (desc != null) {
                    descMap.put(jarName, desc);
                    return desc;
                }
                desc = getJarDescFromProperties(jarName);
                if (desc != null) {
                    descMap.put(jarName, desc);
                    return desc;
                }
                desc = getJarDescFromDefault(jarName);
                if (desc != null) {
                    descMap.put(jarName, desc);
                    return desc;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return EMPTY_STRING;
    }

    private String getJarDescFromManifest(File jarFile) throws IOException {
        URL url = jarFile.toURI().toURL();
        String specTitle = ManifestAnalyzeUtil.getManifestAttributeValue(url, ManifestAnalyzeUtil.SPECIFICATION_TITLE);
        if (StringUtils.trimToNull(specTitle) != null) {
            return specTitle;
        }
        String implTitle = ManifestAnalyzeUtil.getManifestAttributeValue(url, ManifestAnalyzeUtil.IMPLEMENTATION_TITLE);
        if (StringUtils.trimToNull(implTitle) != null) {
            return implTitle;
        }
        String bundleName = ManifestAnalyzeUtil.getManifestAttributeValue(url, ManifestAnalyzeUtil.BUNDLE_NAME);
        if (StringUtils.trimToNull(bundleName) != null) {
            if (bundleName.startsWith("%")) { //$NON-NLS-1$
                bundleName = null;
            }
            return bundleName;
        }

        return null;
    }

    private String getJarDescFromProperties(String jarName) {
        Properties props = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream(DESC_PROP_FILE);
            if (in != null) {
                try {
                    props.load(new BufferedInputStream(in));
                    return props.getProperty(jarName);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ee) {
                        // nothing to do
                    }
                }
            }
        } catch (IOException e) {
            // nothing to do
        }

        return null;
    }

    private String getJarDescFromDefault(String jarName) {
        String desc = jarName;
        if (desc.indexOf(".") != -1) { //$NON-NLS-1$
            desc = desc.substring(0, desc.lastIndexOf(".")); //$NON-NLS-1$
        }

        return desc;
    }

    private String renewTmpFolder() {
        File tf = getTmpFolder();
        if (tf.exists()) {
            FilesUtils.removeFolder(tf, true);
        } else {
            tf.mkdirs();
        }

        return tf.getAbsolutePath();
    }

    private static File getTmpFolder() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String tmpFolderPath = null;
        try {
            IProject physProject = ResourceUtils.getProject(project.getTechnicalLabel());
            tmpFolderPath = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
        } catch (Exception e) {
            tmpFolderPath = System.getProperty("user.dir"); //$NON-NLS-1$
        }
        tmpFolderPath = tmpFolderPath + "/hadoop/customVersion"; //$NON-NLS-1$
        File tmpFolder = new File(tmpFolderPath);

        return tmpFolder;
    }

}
