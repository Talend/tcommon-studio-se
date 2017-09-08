// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TalendBrowserLaunchHelper {

    private static final String[] browsers = { "google-chrome",//$NON-NLS-1$
            "firefox", //$NON-NLS-1$
            "opera", //$NON-NLS-1$
            "epiphany",//$NON-NLS-1$
            "konqueror", //$NON-NLS-1$
            "conkeror", //$NON-NLS-1$
            "midori", //$NON-NLS-1$ 
            "kazehakase", //$NON-NLS-1$
            "mozilla" }; //$NON-NLS-1$

    public static void openURL(String url) {
        Class<?> name;
        try {
            name = Class.forName("java.awt.Desktop"); //$NON-NLS-1$
            name.getDeclaredMethod("browse", new Class[] { java.net.URI.class }).invoke( //$NON-NLS-1$
                    name.getDeclaredMethod("getDesktop").invoke(null), new Object[] { java.net.URI.create(url) }); //$NON-NLS-1$
        } catch (Exception ignore) {

            String osName = System.getProperty("os.name"); //$NON-NLS-1$
            if (osName.startsWith("Mac OS")) { //$NON-NLS-1$
                try {
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[] { String.class }).invoke( //$NON-NLS-1$ //$NON-NLS-2$
                            null, new Object[] { url });
                } catch (IllegalArgumentException e1) {
                    ExceptionHandler.process(e1);
                } catch (SecurityException e2) {
                    ExceptionHandler.process(e2);
                } catch (IllegalAccessException e3) {
                    ExceptionHandler.process(e3);
                } catch (InvocationTargetException e4) {
                    ExceptionHandler.process(e4);
                } catch (NoSuchMethodException e5) {
                    ExceptionHandler.process(e5);
                } catch (ClassNotFoundException e6) {
                    ExceptionHandler.process(e6);
                }
            } else if (osName.startsWith("Windows")) { //$NON-NLS-1$
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url); //$NON-NLS-1$
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            } else {
                String browser = null;
                for (String b : browsers) {
                    try {
                        if (browser == null
                                && Runtime.getRuntime().exec(new String[] { "which", b }).getInputStream().read() != -1) { //$NON-NLS-1$
                            Runtime.getRuntime().exec(new String[] { browser = b, url });
                        }
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }
                    if (browser == null) {
                        try {
                            throw new Exception(Arrays.toString(browsers));
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

}
