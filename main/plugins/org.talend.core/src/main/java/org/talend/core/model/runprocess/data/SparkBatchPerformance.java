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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.process.EConnectionType;

/**
 * DOC zwei class global comment. Detailled comment
 */
public class SparkBatchPerformance extends CommonPerformance {

    private static final String COLOR_RUNNING = "#AA3322"; //$NON-NLS-1$

    private static final String COLOR_FINISHED = "#229922"; //$NON-NLS-1$

    private static final short LIMIT = 2;

    private Map<Integer, String> jobProgress = new HashMap<>();

    private Map<Integer, Integer> jobMode = new HashMap<>();

    public SparkBatchPerformance(EConnectionType eConnectionType) {
        super(eConnectionType);
    }

    public void resetStatus() {
        jobProgress.clear();
        jobMode.clear();
    }

    @Override
    public String getLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return super.getLabel(msg);
        }
        String[] part = msg.split("\\|"); //$NON-NLS-1$
        if (part != null && part.length == 4) {
            jobProgress.put(new Integer(part[1]), parseSize(part[3]).toString());
            jobMode.put(new Integer(part[1]), new Integer(part[2]));

            return createHtmlText();
        }
        return null;
    }

    private String parseSize(String numStr) {
        DecimalFormat df = new DecimalFormat("###.##"); //$NON-NLS-1$
        return df.format(new Float(numStr).floatValue());
    }

    public String createHtmlText() {
        StringBuilder html = new StringBuilder();
        TreeMap<Integer, String> treeMap = new TreeMap<>(jobProgress);
        short index = 0;
        for (Integer job : treeMap.descendingKeySet()) {

            html.append("<font style='font-size:11px' color='" + (jobMode.get(job) == 2 ? COLOR_FINISHED : COLOR_RUNNING) //$NON-NLS-1$
                    + "'>Job " + job + " : " + jobProgress.get(job) + "%</font>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            html.append("<br>"); //$NON-NLS-1$
            if (++index == LIMIT) {
                break;
            }
        }
        return html.toString();
    }
}
