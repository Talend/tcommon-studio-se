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
package org.talend.commons.utils.workbench.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.IllegalPluginConfigurationException;
import org.talend.commons.i18n.internal.Messages;

/**
 * Utilities class uses to get implementation of extension points defined by plug-ins. <br/>
 * 
 * $Id$
 * 
 * @param <I> implementation of extension
 */
public abstract class ExtensionImplementationProvider<I> {

    private IExtensionPointLimiter extensionPointLimiter;

    private String plugInId;

    /**
     * Default Constructor. Must not be used.
     */
    private ExtensionImplementationProvider() {
    }

    /**
     * DOC amaumont ExtensionImplementationProviders constructor comment.
     * 
     * @param extensionPoint
     */
    public ExtensionImplementationProvider(IExtensionPointLimiter extensionPoint) {
        this(extensionPoint, null);
    }

    /**
     * DOC amaumont ExtensionImplementationProviders constructor comment.
     * 
     * @param extensionPoint
     * @param plugInId filter extensions with this plugInId
     */
    public ExtensionImplementationProvider(IExtensionPointLimiter extensionPoint, String plugInId) {
        super();
        this.extensionPointLimiter = extensionPoint;
        this.plugInId = plugInId;
    }

    /**
     * Method used to get implementation of a extension point that accepts a single plug-in.<br/>
     * 
     * This method asserts that the param ext. point accept maximum one plug-in.<br/>
     * 
     * @param <T> - the type of the implemantation returned
     * @param extensionPoint - the ext. point to searched implementation
     * @return the implementation found or <code>null</code> if none
     * @throws IllegalPluginConfigurationException - if number of found plug-in doesn't match the specified max and min
     * value of the ext. point
     */
    public static <T> T getSingleInstance(IExtensionPointLimiter extensionPointLimiter, String pluginId) {
//        Assert.isTrue(extensionPointLimiter.getMaxOcc() == 1, Messages
//                .getString("ExtensionImplementationProviders.ExtensionPointError")); //$NON-NLS-1$

        extensionPointLimiter.setMaxOcc(1);
        
        List<T> list = new ClassExtensionImplementationProvider<T>(extensionPointLimiter, pluginId).createInstances();

        return (list.isEmpty() ? null : list.get(0));
    }

    /**
     * Convenience method which call <code>getInstance(ISimpleExtensionPoint, String plugInId)</code> with
     * <code>null</code> as plugInId.<br/>
     * 
     * @param <T> - the type of the implemantation returned
     * @param extensionPoint - the ext. point to searched implementation
     * @return a list (may be empty) of found plug-in using this ext. point
     * @throws IllegalPluginConfigurationException - if number of found plug-in doesn't match the specified max and min
     * value of the ext. point
     */
    public static <T> List<T> getInstance(IExtensionPointLimiter extensionPoint)
            throws IllegalPluginConfigurationException {
        return getInstance(extensionPoint, null);
    }

    public static List<IConfigurationElement> getInstanceV2(IExtensionPointLimiter extensionPoint)
            throws IllegalPluginConfigurationException {
        return getInstanceV2(extensionPoint, null);
    }

    public static List<IConfigurationElement> getInstanceV2(IExtensionPointLimiter extensionPointLimiter,
            String plugInId) throws IllegalPluginConfigurationException {

        ExtensionImplementationProvider<IConfigurationElement> provider = new ExtensionImplementationProvider<IConfigurationElement>(
                extensionPointLimiter, plugInId) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider#createImplementation(org.eclipse.core.runtime.IExtension,
             * org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter)
             */
            @Override
            protected IConfigurationElement createImplementation(IExtension extension,
                    IExtensionPointLimiter extensionPointLimiter, IConfigurationElement configurationElement) {
                return configurationElement;
            }

        };

        return provider.createInstances();
    }

    /**
     * Method used to get implementations of a extension point.<br/>
     * 
     * @param <T> - the type of the implemantation returned
     * @param extPointLimiter - the ext. point to searched implementation
     * @param plugInId - use to search a specific plug-in. If <code>null</code>, all plug-in are returned.
     * @return a list (may be empty) of found plug-in using this ext. point
     * @throws IllegalPluginConfigurationException - if number of found plug-in doesn't match the specified max and min
     * value of the ext. point
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getInstance(IExtensionPointLimiter extensionPointLimiter, String plugInId) {
        return new ClassExtensionImplementationProvider<T>(extensionPointLimiter, plugInId).createInstances();
    }

    /**
     * Method used to get implementations of a extension point.<br/>
     * 
     * @param <I> - the type of the implemantation returned
     * @param extensionPointLimiter - the ext. point to searched implementation
     * @param plugInId - use to search a specific plug-in. If <code>null</code>, all plug-in are returned.
     * @return a list (may be empty) of found plug-in using this ext. point
     * @throws IllegalPluginConfigurationException - if number of found plug-in doesn't match the specified max and min
     * value of the ext. point
     */
    public List<I> createInstances() {
        List<I> toReturn = new ArrayList<I>();

        IExtensionRegistry reg = Platform.getExtensionRegistry();

        IExtensionPoint pt = reg.getExtensionPoint(extensionPointLimiter.getExtPointId());
        if (pt == null) {
            String msg = Messages.getString("utils.workbench.extensions.noExtension", extensionPointLimiter //$NON-NLS-1$
                    .getExtPointId());
            throw new IllegalPluginConfigurationException(msg);
        }

        IExtension[] extensions = pt.getExtensions();

        for (IExtension extension : extensions) {

            if (plugInId == null || extension.getNamespaceIdentifier().equals(plugInId)) {
                String configurationElementName = extensionPointLimiter.getConfigurationElementName();
                if (configurationElementName != null) {
                    IConfigurationElement[] configs = extension.getConfigurationElements();
                    for (IConfigurationElement currentConfig : configs) {
                        if (configurationElementName.equals(currentConfig.getName())) {
                            createAndAddImplementation(toReturn, extension, extensionPointLimiter, currentConfig);
                        }
                    }
                } else {
                    createAndAddImplementation(toReturn, extension, extensionPointLimiter, null);
                }
            }

        }

        if (extensionPointLimiter.getMinOcc() >= 0 && toReturn.size() < extensionPointLimiter.getMinOcc()
                || extensionPointLimiter.getMaxOcc() >= 0 && toReturn.size() > extensionPointLimiter.getMaxOcc()) {
            String msg = Messages.getString("utils.workbench.extensions.badNumberOfExtension", extensionPointLimiter //$NON-NLS-1$
                    .getExtPointId(), extensionPointLimiter.getMinOcc(), extensionPointLimiter.getMaxOcc(), toReturn
                    .size());
            throw new IllegalPluginConfigurationException(msg);
        }

        return toReturn;
    }

    /**
     * DOC amaumont Comment method "createAndAddImplementation".
     * 
     * @param toReturn
     * @param extension
     * @param extensionPointLimiter
     * @param configurationElement is not null when a <code>configurationElementName</code> criteria is set in the
     * <code>extensionPointLimiter</code>
     */
    private void createAndAddImplementation(List<I> toReturn, IExtension extension,
            IExtensionPointLimiter extensionPointLimiter, IConfigurationElement configurationElement) {
        I implementation = createImplementation(extension, extensionPointLimiter, configurationElement);
        if (implementation != null) {
            toReturn.add(implementation);
        }
    }

    /**
     * Create and return an instance if needed else can return null.
     * 
     * @param extension is the current extension
     * @param extensionPointLimiter is the current extensionPointLimiter
     * @param configurationElement is not null when a <code>configurationElementName</code> criteria is set in the
     * <code>extensionPointLimiter</code>
     * @return a new instance if which is added to list, can return null if no instance to add
     */
    protected abstract I createImplementation(IExtension extension, IExtensionPointLimiter extensionPointLimiter,
            IConfigurationElement configurationElement);

}
