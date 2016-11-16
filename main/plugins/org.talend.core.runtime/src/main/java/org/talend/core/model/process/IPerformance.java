// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.process;

/**
 * Support performance be shown on gef figures.
 */
public interface IPerformance {

    /**
     * Set the data of performance.
     * 
     * @param pefData
     */
    public void setPerformanceData(String pefData);

    /**
     * clear performance status for running next time.
     */
    public void resetStatus();

    public void clearPerformanceDataOnUI();
}
