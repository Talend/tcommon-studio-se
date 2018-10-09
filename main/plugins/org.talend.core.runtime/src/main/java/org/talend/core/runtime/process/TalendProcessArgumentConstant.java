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
package org.talend.core.runtime.process;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public interface TalendProcessArgumentConstant {

    static final String ARG_GOAL = "MAVEN_GOAL";

    static final String ARG_PROGRAM_ARGUMENTS = "PROGRAM_ARGUMENTS";

    static final String ARG_ENABLE_STATS = "ENABLE_STATS";

    static final String ARG_ENABLE_TRACS = "ENABLE_TRACS";

    static final String ARG_PORT_STATS = "PORT_OF_STATS";

    static final String ARG_PORT_TRACS = "PORT_OF_TRACS";

    static final String ARG_ENABLE_APPLY_CONTEXT_TO_CHILDREN = "ENABLE_APPLY_CONTEXT_TO_CHILDREN";

    static final String ARG_GENERATE_OPTION = "GENERATE_OPTION";

    static final String ARG_NEED_CONTEXT = "NEED_CONTEXT";

    static final String ARG_CONTEXT_NAME = "CONTEXT_NAME";

    static final String ARG_ONLY_DEFAUT_CONTEXT = "ONLY_DEFAUT_CONTEXT";

    static final String ARG_CONTEXT_PARAMS = "CONTEXT_PARAMS";

    static final String ARG_ENABLE_LOG4J = "ENABLE_LOG4J";

    static final String ARG_NEED_LOG4J_LEVEL = "NEED_LOG4J_LEVEL";

    static final String ARG_LOG4J_LEVEL = "LOG4J_LEVEL";

    static final String ARG_NEED_XMLMAPPINGS = "NEED_XMLMAPPINGS";

    static final String ARG_NEED_RULES = "NEED_RULES";

    static final String ARG_ENABLE_WATCH = "ENABLE_WATCH";

    static final String ARG_NEED_PIGUDFS = "NEED_PIGUDFS";

    static final String ARG_BUILD_TYPE = "BUILD_TYPE";
    
    static final String ARG_NEED_LAUNCHER = "NEED_LAUNCHER";

    static final String ARG_LAUNCHER_NAME = "LAUNCHER_NAME";

    static final String CMD_ARG_STATS_PORT_PARAM = "stat_port";

    static final String CMD_ARG_TRACE_PORT_PARAM = "trace_port";

    static final String CMD_ARG_TRACE_HOST_PARAM = "client_host";

    /*
     * command
     */
    static final String CMD_ARG_CONTEXT_PARAMETER = "--context_param"; //$NON-NLS-1$

    static final String CMD_ARG_CONTEXT_NAME = "--context=";

    static final String CMD_ARG_LOG4J_LEVEL = "--log4jLevel=";

    static final String CMD_ARG_STATS_PORT = "--" + CMD_ARG_STATS_PORT_PARAM + "=";

    static final String CMD_ARG_TRACE_PORT = "--" + CMD_ARG_TRACE_PORT_PARAM + "=";

    static final String CMD_ARG_WATCH_PARAM = "watch";

    static final String CMD_ARG_WATCH = "--" + CMD_ARG_WATCH_PARAM;

}
