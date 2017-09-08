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
package org.talend.commons.utils.scalability;

/**
 * 
 * DOC amaumont class global comment. Detailled comment
 */
public abstract class ScalabilityTool {

    private boolean stopping;

    private static int execFrequencyMs = 1000;

    private static int startDelayS = 0;

    static {

        String systemPropertyExecFrequency = System.getProperty("scalabilityExecFrequencyMs");
        try {
            execFrequencyMs = Integer.parseInt(systemPropertyExecFrequency);
            System.out.println("System property \"scalabilityExecFrequencyMs\" with value '" + systemPropertyExecFrequency
                    + "' used.");
        } catch (NumberFormatException e) {
            System.err.println("Can't parse the system property \"scalabilityExecFrequencyMs\" with value '"
                    + systemPropertyExecFrequency + "', default value " + execFrequencyMs + " will be used");
        }

        String systemPropertyStartDelay = System.getProperty("scalabilityStartDelayS");
        try {
            startDelayS = Integer.parseInt(systemPropertyStartDelay);
            System.out.println("System property \"scalabilityStartDelayS\" with value '" + systemPropertyStartDelay + "' used.");
        } catch (NumberFormatException e) {
            System.err.println("Can't parse the system property \"scalabilityStartDelayS\" with value '"
                    + systemPropertyStartDelay + "', default value " + systemPropertyStartDelay + " will be used");
        }

    }

    private Thread displayThread;

    private String suffixThreadName;

    public ScalabilityTool(String suffixThreadName) {
        super();
        this.suffixThreadName = suffixThreadName;
    }

    public void start() {
        if (displayThread == null) {

            displayThread = new Thread("ScalabilityTool_" + suffixThreadName) {

                @Override
                public void run() {

                    try {
                        Thread.sleep(startDelayS * 1000);
                    } catch (InterruptedException e1) {
                        return;
                    }

                    while (!stopping) {

                        try {
                            Thread.sleep(execFrequencyMs);
                        } catch (InterruptedException e) {
                            stopping = true;
                        }
                        runProcesses();
                    }

                    displayThread = null;
                    stopping = false;
                }

            };
            displayThread.start();
        }
    }

    public void stop() {
        stopping = true;
    }

    public abstract void runProcesses();

}
