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
package org.talend.designer.maven.tools.extension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;

public interface IProjectPomExtension {

    void updatePom(Model model) throws Exception;

    void updatePomTemplate(Model model) throws Exception;

    public default String getPluginVersion(String key) {
        String version = null;
        String talendVersion = VersionUtils.getTalendVersion();
        Properties properties = new Properties();
        File file = new Path(Platform.getConfigurationLocation().getURL().getPath()).append("mojo_version.properties").toFile(); //$NON-NLS-1$
        if (file.exists()) {
            try (InputStream inStream = new FileInputStream(file)) {
                properties.load(inStream);
                version = properties.getProperty(key);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
            if (version != null && !version.startsWith(talendVersion)) {
                ExceptionHandler.process(
                        new Exception("Incompatible Mojo version:" + key + "[" + version + "], use default version.")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                version = null;
            }
        }
        // default version
        if (StringUtils.isBlank(version)) {
            version = talendVersion;
            String productVersion = VersionUtils.getInternalVersion();
            String revision = StringUtils.substringAfterLast(productVersion, "-"); //$NON-NLS-1$
            if (("SNAPSHOT").equals(revision) || CommonsPlugin.isJUnitTest() || Platform.inDevelopmentMode()) { //$NON-NLS-1$
                return version + "-SNAPSHOT"; //$NON-NLS-1$
            }
            Pattern pattern = Pattern.compile("M\\d{1}"); //$NON-NLS-1$
            if (pattern.matcher(revision).matches()) {
                return version + "-" + revision;
            }
        }
        return version;
    }

}
