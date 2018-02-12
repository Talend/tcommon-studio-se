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
package org.talend.commons.runtime.extension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;

/**
 * Template implementation of a registry reader that creates objects representing registry contents. Typically, an
 * extension contains one element, but this reader handles multiple elements per extension.
 * 
 * To start reading the extensions from the registry for an extension point, call the method <code>readRegistry</code>.
 * 
 * To read children of an IConfigurationElement, call the method <code>readElementChildren</code> from your
 * implementation of the method <code>readElement</code>, as it will not be done by default.
 * 
 */
public abstract class ExtensionRegistryReader {

    private static Logger log = Logger.getLogger(ExtensionRegistryReader.class);

    protected static final String TAG_DESCRIPTION = "description"; //$NON-NLS-1$

    private boolean isInitialized;

    private final String extensionPointId;

    private final String pluginId;

    private final IExtensionRegistry registry;

    /**
     * DOC sgandon class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
     * 
     */
    public abstract class RegistrySafeRunnable implements ISafeRunnable {

        @Override
        public void handleException(Throwable exception) {
            log.error("error reading extention registry", exception); //$NON-NLS-1$

        }
    }

    /**
     * The constructor.
     */
    protected ExtensionRegistryReader(String aPluginId, String anExtensionPoint) {
        this.registry = Platform.getExtensionRegistry();
        this.pluginId = aPluginId;
        this.extensionPointId = anExtensionPoint;
    }

    /**
     * This method extracts description as a subelement of the given element.
     * 
     * @return description string if defined, or empty string if not.
     */
    protected String getDescription(IConfigurationElement config) {
        IConfigurationElement[] children = config.getChildren(TAG_DESCRIPTION);
        if (children.length >= 1) {
            return children[0].getValue();
        }
        return "";//$NON-NLS-1$
    }

    /**
     * Logs the error in the workbench log using the provided text and the information in the configuration element.
     */
    protected static void logError(IConfigurationElement element, String text) {
        IExtension extension = element.getDeclaringExtension();
        StringBuffer buf = new StringBuffer();
        buf.append("Plugin " + extension.getNamespaceIdentifier() + ", extension " + extension.getExtensionPointUniqueIdentifier());//$NON-NLS-2$//$NON-NLS-1$
        buf.append("\n" + text);//$NON-NLS-1$
        logError(0, buf.toString(), null);
    }

    /*
     * Record an error against this plugin's log.
     * 
     * @param aCode
     * 
     * @param aMessage
     * 
     * @param anException
     */
    public static void logError(int aCode, String aMessage, Throwable anException) {
        // Bundle bundle = FrameworkUtil.getBundle(RegistryReader.class);
        log.log(Level.toLevel(aCode), aMessage, anException);
    }

    /**
     * Logs a very common registry error when a required attribute is missing.
     */
    protected static void logMissingAttribute(IConfigurationElement element, String attributeName) {
        logError(element, "Required attribute '" + attributeName + "' not defined");//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Logs a very common registry error when a required child is missing.
     */
    protected static void logMissingElement(IConfigurationElement element, String elementName) {
        logError(element, "Required sub element '" + elementName + "' not defined");//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Logs a registry error when the configuration element is unknown.
     */
    protected static void logUnknownElement(IConfigurationElement element) {
        logError(element, "Unknown extension tag found: " + element.getName());//$NON-NLS-1$
    }

    /**
     * Apply a reproducable order to the list of extensions provided, such that the order will not change as extensions
     * are added or removed.
     */
    protected IExtension[] orderExtensions(IExtension[] extensions) {
        // By default, the order is based on plugin id sorted
        // in ascending order. The order for a plugin providing
        // more than one extension for an extension point is
        // dependent in the order listed in the XML file.
        IExtension[] sortedExtension = new IExtension[extensions.length];
        System.arraycopy(extensions, 0, sortedExtension, 0, extensions.length);
        Comparator<IExtension> comparer = new Comparator<IExtension>() {

            @Override
            public int compare(IExtension arg0, IExtension arg1) {
                String s1 = arg0.getNamespaceIdentifier();
                String s2 = arg1.getNamespaceIdentifier();
                return s1.compareToIgnoreCase(s2);
            }
        };
        Collections.sort(Arrays.asList(sortedExtension), comparer);
        return sortedExtension;
    }

    /**
     * Implement this method to read element's attributes. If children should also be read, then implementor is
     * responsible for calling <code>readElementChildren</code>. Implementor is also responsible for logging missing
     * attributes.
     * 
     * @return true if element was recognized, false if not.
     */
    protected abstract boolean readElement(IConfigurationElement element);

    /**
     * Read the element's children. This is called by the subclass' readElement method when it wants to read the
     * children of the element.
     */
    protected void readElementChildren(IConfigurationElement element) {
        readElements(element.getChildren());
    }

    /**
     * Read each element one at a time by calling the subclass implementation of <code>readElement</code>.
     * 
     * Logs an error if the element was not recognized.
     */
    protected void readElements(IConfigurationElement[] elements) {
        for (int i = 0; i < elements.length; i++) {
            if (!readElement(elements[i])) {
                logUnknownElement(elements[i]);
            }
        }
    }

    /**
     * Read one extension by looping through its configuration elements.
     */
    protected void readExtension(IExtension extension) {
        readElements(extension.getConfigurationElements());
    }

    /**
     * Start the registry reading process using the supplied plugin ID and extension point.
     */
    public void readRegistry() {
        if (isInitialized) {
            return;
        }
        synchronized (this) {
            if (!isInitialized) {
                IExtensionPoint point = registry.getExtensionPoint(pluginId, extensionPointId);
                if (point == null) {
                    point = registry.getExtensionPoint(extensionPointId);
                    if (point == null) {
                        return;
                    }
                }
                IExtension[] extensions = point.getExtensions();
                extensions = orderExtensions(extensions);
                for (IExtension extension : extensions) {
                    readExtension(extension);
                }
                isInitialized = true;
            }

        }
    }
}
