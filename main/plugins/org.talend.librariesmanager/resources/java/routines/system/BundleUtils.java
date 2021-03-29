// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.util.Dictionary;

public final class BundleUtils {

    private static final Class<?> BUNDLE_CONTEXT_CLASS;
    private static final Class<?> SERVICE_REFERENCE_CLASS;
    private static final Object BUNDLE;

    static {
        Class<?> bundleCtxClass;
        Class<?> svcRefClass;
        Object bundle;
        try {
            ClassLoader ld = BundleUtils.class.getClassLoader();
            Class<?> util = ld.loadClass("org.osgi.framework.FrameworkUtil");
            bundleCtxClass = ld.loadClass("org.osgi.framework.BundleContext");
            svcRefClass = ld.loadClass("org.osgi.framework.ServiceReference");
            Method getBundle = util.getMethod("getBundle", Class.class);
            bundle = getBundle.invoke(null, BundleUtils.class);
        } catch (Exception e) {
            bundleCtxClass = null;
            svcRefClass = null;
            bundle = null;
        }
        BUNDLE_CONTEXT_CLASS = bundleCtxClass;
        SERVICE_REFERENCE_CLASS = svcRefClass;
        BUNDLE = bundle;
    }

    public static  <T> T getService(Class<T> svcClass) {
        if (BUNDLE == null) {
            return null;
        }
        try {
            Method getBundleContext = BUNDLE.getClass().getMethod("getBundleContext");
            Object context = getBundleContext.invoke(BUNDLE);
            Class<?> ctxClass = context.getClass();
            Method getServiceReference = ctxClass.getMethod("getServiceReference", Class.class);
            Object serviceReference = getServiceReference.invoke(context, svcClass);
            Method getService = ctxClass.getMethod("getService", SERVICE_REFERENCE_CLASS);
            return svcClass.cast(getService.invoke(context, serviceReference));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getService(String svcClass) {
        if (BUNDLE == null) {
            return null;
        }
        try {
            Method getBundleContext = BUNDLE.getClass().getMethod("getBundleContext");
            Object context = getBundleContext.invoke(BUNDLE);
            Class<?> ctxClass = context.getClass();
            Method getServiceReference = ctxClass.getMethod("getServiceReference", String.class);
            Object serviceReference = getServiceReference.invoke(context, svcClass);
            Method getService = ctxClass.getMethod("getService", SERVICE_REFERENCE_CLASS);
            return (Object)getService.invoke(context, serviceReference);
        } catch (Exception e) {
            return null;
        }
    }

    public static  <T> T getService(Class<T> svcClass, Object bundleContext) {
        if (BUNDLE_CONTEXT_CLASS == null || bundleContext == null) {
            return null;
        }
        if (!BUNDLE_CONTEXT_CLASS.isInstance(bundleContext)) {
            return null;
        }
        try {
            Class<?> ctxClass = bundleContext.getClass();
            Method getServiceReference = ctxClass.getMethod("getServiceReference", Class.class);
            Object serviceReference = getServiceReference.invoke(bundleContext, svcClass);
            Method getService = ctxClass.getMethod("getService", SERVICE_REFERENCE_CLASS);
            return svcClass.cast(getService.invoke(bundleContext, serviceReference));
        } catch (Exception e) {
            return null;
        }
    }
    
    public static <T> Map<String, T> getServices(List<?> serviceReferences, Class<T> serviceClass ) {
    	
    	Map<String, T> services = new HashMap<String, T>();
    	
        if (BUNDLE == null || SERVICE_REFERENCE_CLASS == null ) {
            return services;
        }

        try {
        	for (Object serviceRef : serviceReferences) {
        		Object serviceId = serviceRef.getClass().getMethod("getProperty", 
        				java.lang.String.class).invoke(serviceRef, "osgi.jndi.service.name");
    	        Method getBundleContext = BUNDLE.getClass().getMethod("getBundleContext");
    	        Object context = getBundleContext.invoke(BUNDLE);
    	        Class<?> ctxClass = context.getClass();
    	        Method getService = ctxClass.getMethod("getService", SERVICE_REFERENCE_CLASS);
    	        services.put(serviceId.toString(), serviceClass.cast(getService.invoke(context, serviceRef)));
        	}
        	return services;
        } catch (Exception e) {
            return services;
        }
    }

    public static Dictionary<String, Object> getJobProperties(String jobName) {

        try {
            Object configAdminObject = getService("org.osgi.service.cm.ConfigurationAdmin");

            Method getConfigurationMethod = configAdminObject.getClass().getMethod("getConfiguration", String.class);

            Object configAdminJobConfiguration = getConfigurationMethod.invoke(configAdminObject, jobName);

            Method getPropertiesMethod = configAdminJobConfiguration.getClass().getMethod("getProperties", null);

            Dictionary<String, Object> jobProperties = (Dictionary<String, Object>)getPropertiesMethod.invoke(configAdminJobConfiguration, null);

            return jobProperties;
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean inOSGi() {
        return BUNDLE != null;
    }

    public static Class<?> getBundleContextClass() throws ClassNotFoundException {
        if (BUNDLE_CONTEXT_CLASS == null) {
            throw new ClassNotFoundException(
                    "Class org.osgi.framework.BundleContext cannot be resolved. ");
        }
        return BUNDLE_CONTEXT_CLASS;
    }

    private BundleUtils() {
        super();
    }
}
