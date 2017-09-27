// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.talend.commons.exception.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicServiceUtil {

    public static <T> ServiceRegistration<T> registOSGiService(BundleContext context, String[] clazzes, Object service,
            Dictionary<String, T> properties) throws Exception {
        return (ServiceRegistration<T>) context.registerService(clazzes, service, properties);
    }

    public static <T> void unregistOSGiService(ServiceRegistration<T> serviceRegistration)
            throws Exception {
        serviceRegistration.unregister();
    }

    public static boolean addContribution(Bundle bundle, IDynamicPlugin plugin) throws Exception {
        String xml = plugin.toXmlString();
        return addContribution(bundle, xml);
    }

    public static boolean addContribution(Bundle bundle, String xmlStr) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(xmlStr.getBytes("UTF-8")); //$NON-NLS-1$

        try {
            IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
            Object userToken = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();
            IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);

            return extensionRegistry.addContribution(is, contributor, false, null, null, userToken);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

    }

    public static boolean removeContribution(IDynamicPlugin plugin) {
        boolean succeed = true;

        List<IDynamicExtension> extensions = plugin.getAllExtensions();
        if (extensions != null && !extensions.isEmpty()) {
            IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
            Object userToken = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();
            for (IDynamicExtension extension : extensions) {
                String extensionPoint = extension.getExtensionPoint();
                String extensionId = extension.getExtensionId();
                boolean curSucceed = removeExtension(extensionRegistry, userToken, extensionPoint, extensionId);
                if (!curSucceed) {
                    succeed = false;
                }
            }
        }

        return succeed;
    }

    public static boolean removeExtension(String extensionPointId, String extensionId) {
        IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
        Object userToken = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();
        return removeExtension(extensionRegistry, userToken, extensionPointId, extensionId);
    }

    public static boolean removeExtension(IExtensionRegistry extensionRegistry, Object userToken, String extensionPointId,
            String extensionId) {
        IExtension extension = extensionRegistry.getExtension(extensionPointId, extensionId);
        if (extension != null) {
            return extensionRegistry.removeExtension(extension, userToken);
        }
        return true;
    }

    public static String formatJsonString(String string) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(string);
        String formatedString = om.writerWithDefaultPrettyPrinter().writeValueAsString(jn);
        return formatedString;
    }

    public static String readFile(File file) throws Exception {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = ""; //$NON-NLS-1$
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }
}
