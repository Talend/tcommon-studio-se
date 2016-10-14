package org.talend.designer.maven.tools.creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
 * This class provides capabilities for third party bundles to contribute to the maven pom which is used to create jobs
 * jar files.
 * 
 * @author jclaude
 */
public class PomJobExtensionRegistry {

    private static final Logger log = Logger.getLogger(PomJobExtensionRegistry.class);

    private static final String MAVEN_POM_JOB_EXTENSION_POINT = DesignerMavenPlugin.PLUGIN_ID + ".mavenPomJob";

    private static final String MAVEN_POM_JOB_EXTENSION_POINT_CONFIGURABLE_ELEMENT = "pomJobExtension";

    private static final String ATTRIBUTE_CLASS = "class";

    private static PomJobExtensionRegistry singleton = null;

    private List<IPomJobExtension> pomJobExtensionList;

    public static PomJobExtensionRegistry getInstance() {
        if (singleton == null) {
            new PomJobExtensionRegistry();
        }
        return singleton;
    }

    private PomJobExtensionRegistry() {
        singleton = this;
        initializeExtension();
    }

    /**
     * Load implementation class from eclipse registry which provides extension to maven pom job Extension point.
     */
    private void initializeExtension() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(MAVEN_POM_JOB_EXTENSION_POINT);
        if (point == null)
            return;
        IExtension[] extensions = point.getExtensions();
        for (IExtension ext : extensions) {
            IConfigurationElement[] configurableElements = ext.getConfigurationElements();
            for (IConfigurationElement configElement : configurableElements) {
                String configElementName = configElement.getName();
                if (MAVEN_POM_JOB_EXTENSION_POINT_CONFIGURABLE_ELEMENT.equals(configElementName)) {
                    try {
                        Object instance = configElement.createExecutableExtension(ATTRIBUTE_CLASS);
                        if (instance instanceof IPomJobExtension) {
                            getPomJobExtensions().add((IPomJobExtension) instance);
                        }
                    } catch (CoreException e) {
                        log.error("Error Loading Maven Pom job Extension", e); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    private List<IPomJobExtension> getPomJobExtensions() {
        if (pomJobExtensionList == null) {
            pomJobExtensionList = new ArrayList<IPomJobExtension>();
        }
        return pomJobExtensionList;
    }

    public void updatePom(IProgressMonitor monitor, IFile pomFile, Map<String, Object> args) {
        for (IPomJobExtension pomJobExtension : getPomJobExtensions()) {
            try {
                pomJobExtension.updatePom(monitor, pomFile, args);
            } catch (Exception e) {
                log.error("Error Loading Maven Pom job Extension", e); //$NON-NLS-1$
            }
        }
    }

}
