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

import java.text.MessageFormat;
import java.util.Locale;

import org.talend.core.model.process.EConnectionType;
import org.talend.designer.runprocess.IPerformanceData;

/**
 * DOC zwei class global comment. Detailled comment
 */
public class CommonPerformance {

    protected EConnectionType eConnectionType;

    public CommonPerformance(EConnectionType eConnectionType) {
        this.eConnectionType = eConnectionType;
    }

    public String getLabel(String data) {
        PerformanceData perf = new PerformanceData(data);
        StringBuffer html = new StringBuffer();

        if (IPerformanceData.ACTION_PERF.equals(perf.getAction())) {
            final String perfPattern = "<font style='font-size:11px' color=''#4477BB''>" + "{0, number,#} rows - {1,number,#.##}s<br>" //$NON-NLS-1$ //$NON-NLS-2$
                    + "<b>{2,number,#.##} rows/s</b>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
            long lineCount = perf.getLineCount();
            long processingTime = perf.getProcessingTime();
            double avg = processingTime > 0 ? lineCount * 1000d / processingTime : 0d;
            MessageFormat mf = new MessageFormat(perfPattern, Locale.US);
            html.append(mf.format(new Object[] { new Long(lineCount), new Double(processingTime / 1000d), new Double(avg) }));
        } else if (IPerformanceData.ACTION_START.equals(perf.getAction())) {
            final String perfPattern = "<font style='font-size:11px' color='#AA3322'>" + "<i>Starting</i>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            html.append(perfPattern);
        } else if (IPerformanceData.ACTION_STOP.equals(perf.getAction())) {
            final String perfPattern = "<font style='font-size:11px' color=''#229922''>" + "{0, number,#} rows in {1,number,#.##}s<br>" //$NON-NLS-1$ //$NON-NLS-2$
                    + "<i>{2,number,#.##} rows/s</i>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
            long lineCount = perf.getLineCount();
            long processingTime = perf.getProcessingTime();
            double avg = processingTime > 0 ? lineCount * 1000d / processingTime : 0d;
            MessageFormat mf = new MessageFormat(perfPattern);
            html.append(mf.format(new Object[] { new Long(lineCount), new Double(processingTime / 1000d), new Double(avg) }));
        }

        return html.toString();
    }
}
