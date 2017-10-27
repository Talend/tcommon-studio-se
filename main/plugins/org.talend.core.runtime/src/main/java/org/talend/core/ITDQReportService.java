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
package org.talend.core;


/**
 * cli class global comment. Detailled comment
 */
public interface ITDQReportService extends ITDQService {

    /**
     * This method is to excute report.
     * 
     * @param names Report name, more names delimited by comma.
     * @param pathes Report relative current project path, more pathes delimited by comma.
     * @param contextName Report context name, more contextName delimited by comma.
     * @throws Exception
     */
    public void executeReport(String names, String pathes, String contextName) throws Exception;
}
