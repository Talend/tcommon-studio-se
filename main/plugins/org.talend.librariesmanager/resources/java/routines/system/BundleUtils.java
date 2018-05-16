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
package routines.system;

import java.lang.reflect.Method;

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
