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
package org.talend.core.runtime.schedulingrule;

import org.eclipse.core.runtime.jobs.ISchedulingRule;


/**
 * created by cmeng on May 31, 2016
 * Detailled comment
 *
 */
public interface ITalendSchedulingRule extends ISchedulingRule {

    public Thread getRuleThread();

    public void setRuleThread(Thread thread);
}
