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
 * Listener called when <code>PerformanceEvaluator</code> has finished its process. <br/>
 * 
 * $Id$
 * 
 */
public interface IPerformanceEvaluatorListener {

    public void handleEvent(PerformanceEvaluatorEvent event);

}
