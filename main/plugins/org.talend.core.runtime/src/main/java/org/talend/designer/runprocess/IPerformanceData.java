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
package org.talend.designer.runprocess;

/**
 * DOC qian class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (鏄熸湡浜� 29 涔?鏈�2006) nrousseau $
 * 
 */
public interface IPerformanceData {

    /** Action : Performance report. */
    public static final String ACTION_PERF = "perf"; //$NON-NLS-1$

    /** Action : process stoped. */
    public static final String ACTION_STOP = "stop"; //$NON-NLS-1$

    /** Action : process started. */
    public static final String ACTION_START = "start"; //$NON-NLS-1$

    /** Action : clear performance stats. */
    public static final String ACTION_CLEAR = "clear"; //$NON-NLS-1$

    public String getConnectionId();

    public long getLineCount();

    public long getProcessingTime();

    public String getAction();

}
