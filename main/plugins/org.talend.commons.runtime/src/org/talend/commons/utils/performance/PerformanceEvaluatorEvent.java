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
package org.talend.commons.utils.performance;

/**
 * 
 * Event occured by <code>PerformanceEvaluator</code> when the process is finished. <br/>
 * 
 * $Id$
 * 
 */
public class PerformanceEvaluatorEvent {

    private int indicePerformance;

    public PerformanceEvaluatorEvent(int indicePerformance) {
        this.indicePerformance = indicePerformance;
    }

    /**
     * A value lower than PerformanceEvaluator.GOOD_PERFORMANCE_INDICE is considerate how best performance.
     * 
     * @return indicePerformance value
     */
    public int getIndicePerformance() {
        return indicePerformance;
    }

    public void setIndicePerformance(int indicePerformance) {
        this.indicePerformance = indicePerformance;
    }

}
