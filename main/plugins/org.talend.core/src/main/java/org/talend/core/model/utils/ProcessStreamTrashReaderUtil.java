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
package org.talend.core.model.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.talend.commons.exception.WarningException;

/**
 * cli class global comment. Detailled comment
 */
public final class ProcessStreamTrashReaderUtil {

    private ProcessStreamTrashReaderUtil() {
        super();
    }

    public static String readErrorStream(final Process process) {
        final String lineSep = System.getProperty("line.separator"); //$NON-NLS-1$
        final StringBuilder builder = new StringBuilder();

        try {
            // a fake input stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getInputStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            // nothing to do. just a fake
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            // error stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getErrorStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                            builder.append(lineSep);
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            boolean stopped = false;
            while (!stopped) {
                try {
                    process.exitValue();
                    stopped = true;
                } catch (IllegalThreadStateException itse) {
                    // nothing to do
                }
            }
        } catch (Exception ioe) {
            // nothing to do
        }

        final String string = builder.toString();
        if (string.equals("")) { //$NON-NLS-1$
            return null;
        }

        return string;
    }

    public static void readAndForget(final Process process) {
        try {
            // input stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getInputStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getInputStream " + line); //$NON-NLS-1$
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            // error stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getErrorStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getErrorStream " + line); //$NON-NLS-1$
                            throw new WarningException("AAAAA");
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            boolean stopped = false;
            while (!stopped) {
                try {
                    process.exitValue();
                    stopped = true;
                } catch (IllegalThreadStateException itse) {
                    // nothing to do
                }
            }
        } catch (Exception ioe) {
            // nothing to do
        }
    }

    public static void readAndForget(final Process process, final StringBuffer buffer) {
        try {
            // input stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getInputStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getInputStream " + line); //$NON-NLS-1$
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            // error stream thread
            new Thread() {

                public void run() {
                    InputStream is = process.getErrorStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getErrorStream " + line); //$NON-NLS-1$
                            if(line.startsWith("log4j:WARN")){
                                continue;
                            }
                            buffer.append(line);
                            buffer.append("\r\n");
                        }
                    } catch (Exception ex) {
                        // nothing to do
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                            // nothing to do
                        }
                    }
                }
            }.start();

            boolean stopped = false;
            while (!stopped) {
                try {
                    process.exitValue();
                    stopped = true;
                } catch (IllegalThreadStateException itse) {
                    // nothing to do
                }
            }
        } catch (Exception ioe) {
            // nothing to do
        }
    }
}
