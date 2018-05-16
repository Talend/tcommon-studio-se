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

package org.talend.commons.utils.memory;

import java.text.NumberFormat;

/**
 * class MemoryMeasurer.
 * 
 * It is used to know the difference between the used memory at beginning and at end of measure.
 */
public class MemoryMeasurer {

    private Long usedMemoryAtBeginning;

    private Long usedMemoryAtEnd;

    /**
     * 
     * Method "begin".
     * 
     * It defines the reference of the used memory.
     */
    public void begin() {
        boolean useGCBeforeMeasure = true;
        begin(useGCBeforeMeasure);
    }

    /**
     * 
     * Method "begin".
     * 
     * It defines the reference of the used memory.
     * 
     * @param useGCBeforeMeasure true to force gc
     */
    public void begin(boolean useGCBeforeMeasure) {
        Runtime runtime = Runtime.getRuntime();
        if (useGCBeforeMeasure) {
            runtime.gc();
        }
        usedMemoryAtBeginning = runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * 
     * Method "step".
     * 
     * @param useGCBeforeMeasure true to force gc
     * @return the difference between the used memory at beginning and at end of measure
     */
    public long step() {
        boolean useGCBeforeMeasure = true;
        return step(useGCBeforeMeasure);
    }

    /**
     * 
     * Method "step".
     * 
     * @param useGCBeforeMeasure true to force gc
     * @return the difference between the used memory at beginning and at end of measure
     */
    public long step(boolean useGCBeforeMeasure) {
        if (usedMemoryAtBeginning == null) {
            throw new IllegalStateException("The method begin must be called first.");
        }
        if (useGCBeforeMeasure) {
            System.gc();
        }
        return getUsedMemoryFromBeginToCurrent(false);
    }

    /**
     * 
     * Method "end".
     * 
     * @return the difference between the used memory at beginning and at end of measure
     */
    public long end() {
        boolean useGCBeforeMeasure = true;
        return end(useGCBeforeMeasure);
    }

    /**
     * 
     * Method "end".
     * 
     * @param useGCBeforeMeasure true to force gc
     * @return the difference between the used memory at beginning and at end of measure
     */
    public long end(boolean useGCBeforeMeasure) {
        if (usedMemoryAtBeginning == null) {
            throw new IllegalStateException("The method begin must be called first.");
        }
        if (useGCBeforeMeasure) {
            System.gc();
        }
        if (usedMemoryAtEnd == null) {
            return getUsedMemoryFromBeginToCurrent(true);
        } else {
            return getUsedMemoryFromBeginToCurrent(false);
        }
    }

    /**
     * 
     * Method "printUsedMemory".
     * 
     */
    public void printUsedMemory() {
        printUsedMemory(null);
    }

    /**
     * 
     * Method "printUsedMemory".
     * 
     */
    public void printUsedMemory(String contextInfo) {
        callGC();
        String formated = String.format("%,d", getUsedMemoryFromBeginToCurrent(false));
        System.out.println((contextInfo != null ? contextInfo + " : " : "") + formated + " bytes");
    }

    private long getUsedMemoryFromBeginToCurrent(boolean isEndStep) {
        Runtime runtime = Runtime.getRuntime();
        Long usedMemory = usedMemoryAtEnd;
        if (usedMemory == null || isEndStep) {
            usedMemory = runtime.totalMemory() - runtime.freeMemory();
            if (isEndStep) {
                usedMemoryAtEnd = usedMemory;
            }
        }
        return usedMemory - usedMemoryAtBeginning;
    }

    private void callGC() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
    }

    public void printInformations() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("Free memory: " + format.format(freeMemory / 1024) + " KB  (" + format.format(freeMemory) + ") " + freeMemory
                + "\n");
        sb.append("Allocated memory: " + format.format(allocatedMemory / 1024) + " KB  (" + format.format(allocatedMemory)
                + ")\n");
        sb.append("Max memory: " + format.format(maxMemory / 1024) + " KB  (" + format.format(maxMemory) + ")\n");
        sb.append("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + " KB  ("
                + format.format(freeMemory + (maxMemory - allocatedMemory)) + ")\n");
        System.out.println(sb.toString());
    }

}
