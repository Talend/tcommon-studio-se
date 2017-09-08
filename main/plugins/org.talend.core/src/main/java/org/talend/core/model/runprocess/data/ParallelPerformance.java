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
package org.talend.core.model.runprocess.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.runprocess.IPerformanceData;

/**
 * DOC zwei class global comment. Detailled comment
 */
public class ParallelPerformance extends CommonPerformance {

    public ParallelPerformance(EConnectionType eConnectionType) {
        super(eConnectionType);
    }

    /**
     * only display 4 line of execution.
     */
    private static final int DISPLAY_LIMIT = 4;

    private static final String COLOR_FINISHED = "#229922"; //$NON-NLS-1$

    private static final String COLOR_RUNNING = "#AA3322"; //$NON-NLS-1$

    /**
     * store the ids of exec that will be displayed on current row link.
     */
    private LinkedList<String> displayedExecutionId = new LinkedList<String>();

    /**
     * store entry of connetionId and IPerformanceData.
     */
    private Map<String, IPerformanceData> performanceDataMap = new HashMap<String, IPerformanceData>();

    private String label;

    private boolean isOnTop;

    public void resetStatus() {
        displayedExecutionId.clear();
        performanceDataMap.clear();
    }

    @Override
    public String getLabel(String msg) {
        IPerformanceData data = new PerformanceData(msg);

        if (StringUtils.isEmpty(msg) || !eConnectionType.equals(EConnectionType.FLOW_MAIN)
                || data.getConnectionId().indexOf('.') < 0) {
            isOnTop = true;
            // if message has format as row1, handle by super class
            return super.getLabel(msg);
        } else {
            // has format as row1.72, means have parallel execution
            String connectionId = data.getConnectionId();
            String execId = connectionId.substring(connectionId.indexOf('.') + 1);
            performanceDataMap.put(execId, data);

            displayIfPossible(execId, data);

            StringBuilder builder = new StringBuilder(1024);
            for (String id : displayedExecutionId) {
                builder.append(constructLabel(performanceDataMap.get(id)));
            }

            // set the label location
            // offset = computeLabelOffset();

            // update label
            // String oldLabel = label;
            label = builder.toString();
            // firePropertyChange(LABEL_PROP, oldLabel, label);
        }
        return this.label;
    }

    public int computeLabelOffsetY() {
        if (isOnTop) {
            return -30;
        }
        int size = performanceDataMap.keySet().size();
        if (size > DISPLAY_LIMIT) {
            size = DISPLAY_LIMIT;
        }
        if (displayedExecutionId.size() > DISPLAY_LIMIT) {
            size++;
        }
        return -14 * size;
    }

    private void displayIfPossible(String execId, IPerformanceData data) {
        if (displayedExecutionId.contains(execId)) {
            // if the execution has already been displayed, do nothing.
            return;
        }

        if (displayedExecutionId.size() >= DISPLAY_LIMIT) {
            performanceDataMap.remove(displayedExecutionId.removeFirst());
        }
        displayedExecutionId.add(execId);
    }

    /**
     * Construct a label from IPerformanceData, which will be displayed on current link.
     * 
     * @param data
     */
    private String constructLabel(IPerformanceData data) {
        String connectionId = data.getConnectionId();
        long execId = Long.parseLong(connectionId.substring(connectionId.indexOf('.') + 1)) + 1;
        long lineCount = data.getLineCount();
        long processingTime = data.getProcessingTime();
        double avg = processingTime > 0 ? lineCount * 1000.0 / processingTime : 0.0;
        String color = COLOR_RUNNING;
        String pattern = "<font style='font-size:11px' color='%1$s'>exec %2$s : %3$5d rows, %4$5.0f rows/second</font><br>"; //$NON-NLS-1$
        if (data.getAction().equals(IPerformanceData.ACTION_STOP)) {
            color = COLOR_FINISHED;
        }
        return String.format(pattern, color, execId, lineCount, avg);
    }
}
