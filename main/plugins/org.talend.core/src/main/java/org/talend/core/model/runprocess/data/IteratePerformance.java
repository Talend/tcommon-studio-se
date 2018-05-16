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
package org.talend.core.model.runprocess.data;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.runprocess.IPerformanceData;

/**
 * DOC zwei class global comment. Detailled comment
 */
public class IteratePerformance extends CommonPerformance {

    private boolean webData;

    public IteratePerformance(EConnectionType eConnectionType) {
        super(eConnectionType);
    }

    public IteratePerformance(EConnectionType eConnectionType, boolean fromWeb) {
        super(eConnectionType);
        // tac data like this: iterate2|exec10|stop, so this stopped size 10 need to be getted from this string.
        webData = fromWeb;
    }

    private static final String COLOR_FINISHED = "#229922"; //$NON-NLS-1$

    private static final String COLOR_RUNNING = "#AA3322"; //$NON-NLS-1$

    /**
     * store the ids of exec that have already stopped.
     */
    private Set<String> stoppeddExecutionId = new HashSet<String>();

    /**
     * store the ids of exec that are running now.
     */
    private Set<String> runningExecutionId = new HashSet<String>();

    @Override
    public String getLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            // handle by super class
            return super.getLabel(msg);
        }
        String[] part = msg.split("\\|"); //$NON-NLS-1$
        if (part != null && part.length == 3) {
            // update process status
            if (part[2].equals(IPerformanceData.ACTION_START)) {
                runningExecutionId.add(part[1]);
            } else if (part[2].equals(IPerformanceData.ACTION_STOP)) {
                stoppeddExecutionId.add(part[1]);
                runningExecutionId.remove(part[1]);
            }

            return createHtmlText(parseSize(part[1]));

        } else if (part != null && part.length == 2) { // iterate1.0|exec0, it means running.
            runningExecutionId.add(part[1]);

            return createHtmlText(parseSize(part[1]));
        }
        return null;
    }

    private int parseSize(String numStr) {
        if (numStr.contains("exec.")) {
            return new Integer(numStr.substring(5)).intValue();
        }
        return new Integer(numStr.substring(4)).intValue();
    }

    public String createHtmlText(int size) {
        StringBuilder html = new StringBuilder(150);

        String pattern = "<font style='font-size:11px' color='%1$s'>%2$s %3$s</font><br>"; //$NON-NLS-1$

        if (runningExecutionId.size() > 0) {
            int runningSize = webData ? size : runningExecutionId.size();

            if (runningSize == 1) {
                html.append(String.format(pattern, COLOR_RUNNING, runningSize, "exec running")); //$NON-NLS-1$
            } else {
                // plural forms
                html.append(String.format(pattern, COLOR_RUNNING, runningSize, "execs running")); //$NON-NLS-1$
            }
        }

        if (stoppeddExecutionId.size() > 0) {
            int stoppedSize = webData ? size : stoppeddExecutionId.size();

            if (stoppedSize == 1) {
                html.append(String.format(pattern, COLOR_FINISHED, stoppedSize, "exec finished")); //$NON-NLS-1$
            } else {
                // plural forms
                html.append(String.format(pattern, COLOR_FINISHED, stoppedSize, "execs finished")); //$NON-NLS-1$
            }
        }

        return html.toString();
    }

    public void resetStatus() {
        stoppeddExecutionId.clear();
        runningExecutionId.clear();
    }

}
