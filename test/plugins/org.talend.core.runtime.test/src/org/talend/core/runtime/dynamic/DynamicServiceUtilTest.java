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
package org.talend.core.runtime.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.dynamic.impl.DynamicConfiguration;
import org.talend.core.runtime.dynamic.impl.DynamicPlugin;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class DynamicServiceUtilTest {

    @Test
    public void testOSGiService() throws Exception {
        int expect = 10;
        BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();

        ServiceRegistration<Object> registOSGiService = DynamicServiceUtil.registOSGiService(context,
                new String[] { Dummy.class.getName() }, new Dummy(expect), null);
        ServiceReference<Dummy> serviceReference = context.getServiceReference(Dummy.class);
        Dummy service = context.getService(serviceReference);
        assertEquals(expect, service.get());

        DynamicServiceUtil.unregistOSGiService(registOSGiService);
        serviceReference = context.getServiceReference(Dummy.class);
        assertNull(serviceReference);
    }

    @Test
    public void testContribution() throws Exception {
        String extensionPoint = "org.talend.core.runtime.librariesNeeded";
        String extensionId = "extension.id1";
        String contextTag = "context";
        String contextValue = "plugin:org.talend.hadoop.distribution.cdh5100";
        String libraryNeededTag = "libraryNeeded";
        String idTag = "id";
        String idValue = "hadoop-common-cdh5.10";
        String nameTag = "name";
        String nameValue = "hadoop-common-2.6.0-cdh5.10.1.jar";
        String mvnUriTag = "mvn_uri";
        String mvnUriValue = "mvn:org.talend.libraries/hadoop-common-2.6.0-cdh5.10.1/6.1.0";

        DynamicConfiguration libraryNeeded = new DynamicConfiguration();
        libraryNeeded.setConfigurationName(libraryNeededTag);
        libraryNeeded.setAttribute(contextTag, contextValue);
        libraryNeeded.setAttribute(idTag, idValue);
        libraryNeeded.setAttribute(nameTag, nameValue);
        libraryNeeded.setAttribute(mvnUriTag, mvnUriValue);

        DynamicPlugin plugin = new DynamicPlugin();

        IDynamicExtension extension = plugin.getExtension(extensionPoint, extensionId, true);
        extension.addConfiguration(libraryNeeded);

        Bundle bundle = CoreRuntimePlugin.getInstance().getBundle();
        DynamicServiceUtil.addContribution(bundle, plugin);

        IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
        IExtension extension2 = extensionRegistry.getExtension(extensionPoint, extensionId);
        IConfigurationElement[] configurationElements = extension2.getConfigurationElements();
        assertEquals(1, configurationElements.length);
        IConfigurationElement element = configurationElements[0];
        assertEquals(libraryNeededTag, element.getName());
        assertEquals(contextValue, element.getAttribute(contextTag));
        assertEquals(idValue, element.getAttribute(idTag));
        assertEquals(nameValue, element.getAttribute(nameTag));
        assertEquals(mvnUriValue, element.getAttribute(mvnUriTag));

        DynamicServiceUtil.removeContribution(plugin);
        extension2 = extensionRegistry.getExtension(extensionPoint, extensionId);
        assertNull(extension2);
    }

    private static class Dummy {

        private int i;

        public Dummy() {
            i = 0;
        }

        public Dummy(int i) {
            this.i = i;
        }

        public int get() {
            return this.i;
        }
    }
}
