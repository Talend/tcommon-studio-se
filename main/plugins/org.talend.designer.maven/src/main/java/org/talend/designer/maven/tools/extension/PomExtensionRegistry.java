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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.talend.designer.maven.DesignerMavenPlugin;

/**
 * @author jclaude
 */
public class PomExtensionRegistry {

    private static final Logger log = Logger.getLogger(PomExtensionRegistry.class);

    private static final String MAVEN_POM_EXTENSION_POINT = DesignerMavenPlugin.PLUGIN_ID + ".mavenPom";

    private static final String MAVEN_JOB_POM_EXTENSION_POINT_CONFIGURABLE_ELEMENT = "jobPomExtension";

    private static final String MAVEN_PROJECT_POM_EXTENSION_POINT_CONFIGURABLE_ELEMENT = "projectPomExtension";

    private static final String ATTRIBUTE_CLASS = "class";

    private static PomExtensionRegistry singleton = null;

    private List<IJobPomExtension> jobPomExtensionList;

    private List<IProjectPomExtension> projectPomExtensionList;

    public static PomExtensionRegistry getInstance() {
        if (singleton == null) {
            new PomExtensionRegistry();
        }
        return singleton;
    }

    private PomExtensionRegistry() {
        singleton = this;
        initializeExtension();
    }

    /**
     * Load implementation class from eclipse registry which provides extension to maven pom job Extension point.
     */
    private void initializeExtension() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(MAVEN_POM_EXTENSION_POINT);
        if (point == null)
            return;
        IExtension[] extensions = point.getExtensions();
        for (IExtension ext : extensions) {
            IConfigurationElement[] configurableElements = ext.getConfigurationElements();
            for (IConfigurationElement configElement : configurableElements) {
                String configElementName = configElement.getName();
                try {
                    if (MAVEN_JOB_POM_EXTENSION_POINT_CONFIGURABLE_ELEMENT.equals(configElementName)) {
                        Object instance = configElement.createExecutableExtension(ATTRIBUTE_CLASS);
                        if (instance instanceof IJobPomExtension) {
                            getJobPomExtensions().add((IJobPomExtension) instance);
                        }
                    } else if (MAVEN_PROJECT_POM_EXTENSION_POINT_CONFIGURABLE_ELEMENT.equals(configElementName)) {
                        Object instance = configElement.createExecutableExtension(ATTRIBUTE_CLASS);
                        if (instance instanceof IProjectPomExtension) {
                            getProjectPomExtensions().add((IProjectPomExtension) instance);
                        }
                    }
                } catch (CoreException e) {
                    log.error("Error Loading Maven Pom Extension", e); //$NON-NLS-1$
                }
            }
        }
    }

    private List<IJobPomExtension> getJobPomExtensions() {
        if (jobPomExtensionList == null) {
            jobPomExtensionList = new ArrayList<IJobPomExtension>();
        }
        return jobPomExtensionList;
    }

    private List<IProjectPomExtension> getProjectPomExtensions() {
        if (projectPomExtensionList == null) {
            projectPomExtensionList = new ArrayList<IProjectPomExtension>();
        }
        return projectPomExtensionList;
    }

    public void updateJobPom(IProgressMonitor monitor, IFile pomFile, Map<String, Object> args) {
        for (IJobPomExtension jobPomExtension : getJobPomExtensions()) {
            try {
                jobPomExtension.updatePom(monitor, pomFile, args);
            } catch (Exception e) {
                log.error("Error Loading Maven Pom job Extension", e); //$NON-NLS-1$
            }
        }
    }

    public void updateProjectPom(Model model) {
        for (IProjectPomExtension projectPomExtension : getProjectPomExtensions()) {
            try {
                projectPomExtension.updatePom(model);
            } catch (Exception e) {
                log.error("Error Loading Maven P om Extension", e); //$NON-NLS-1$
            }
        }
    }

    public void updatePomTemplate(Model model) {
        for (IProjectPomExtension projectPomExtension : getProjectPomExtensions()) {
            try {
                projectPomExtension.updatePomTemplate(model);
            } catch (Exception e) {
                log.error("Error Loading Maven Pom Extension", e); //$NON-NLS-1$
            }
        }
    }

}
