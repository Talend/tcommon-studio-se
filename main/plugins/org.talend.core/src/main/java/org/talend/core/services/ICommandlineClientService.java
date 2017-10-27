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
package org.talend.core.services;

import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.ITargetExecutionConfig;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.ProcessorException;

/**
 * ggu class global comment. Detailled comment
 */
public interface ICommandlineClientService extends IService {

    public Process deployAndRunByCommandline(final ITargetExecutionConfig config, String jobName, String jobVersion,
            String jobContextName, int statisticsPort, int tracePort, String watchParam, final boolean runnable,
            List<IContextParameter> jobContextParams) throws ProcessorException;

    public Process deployByCommandlineAndRunByStudio(final IProcessMessageManager processMessageManager,
            final ITargetExecutionConfig config, String jobName, String jobVersion, String jobContextName, int statisticsPort,
            int tracePort, String watchParam, String log4jLevel, List<IContextParameter> jobContextParams) throws ProcessorException;

}
