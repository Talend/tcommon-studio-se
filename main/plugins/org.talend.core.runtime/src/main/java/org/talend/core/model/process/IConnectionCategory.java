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
package org.talend.core.model.process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public interface IConnectionCategory {

    /**
     * For FLOW_MAIN / ITERATE / TABLE / FLOW_MERGE. Links considered as the Main links.
     */
    public static final int MAIN = 1;

    /**
     * For FLOW_MAIN / FLOW_REF / TABLE / FLOW_MERGE Links which contains metadata.
     */
    public static final int DATA = 1 << 1;

    /**
     * For RUN_IF, RUN_IF_OK, RUN_IF_ERROR Links with a condition for the execution (components will not run all the
     * time).
     */
    public static final int CONDITION = 1 << 2;

    /**
     * For RUN_AFTER, RUN_BEFORE. Links that will add an execution order.
     */
    public static final int EXECUTION_ORDER = 1 << 3;

    /**
     * For RUN_AFTER, RUN_BEFORE, RUN_IF, RUN_IF_OK, RUN_IF_ERROR. Links that will add a dependency to run a sub
     * process.
     */
    public static final int DEPENDENCY = 1 << 4;

    /**
     * For FLOW_MAIN, FLOW_REF, FLOW_MERGE. Flow links (or main links), so in the menu Row > Main.
     */
    public static final int FLOW = 1 << 5;

    /**
     * For FLOW_REF. Links that will create a hidden hash file (tHash component).
     */
    public static final int USE_HASH = 1 << 6;

    /**
     * For FLOW_MAIN, FLOW_REF, FLOW_MERGE, TABLE. Links that can have a specific name.
     */
    public static final int CUSTOM_NAME = 1 << 7;

    /**
     * For FLOW_MAIN, FLOW_REF, FLOW_MERGE. Links that can have a specific name. note that now: RUN_IF / ON_SUBJOB_OK /
     * ON_SUBJOB_ERROR / ON_COMPONENT_OK / ON_COMPONENT_ERROR / ITERATE also have uniqunename property, even if those
     * are only for internal use (not visible even in the default name)
     */
    public static final int UNIQUE_NAME = 1 << 8;

    /**
     * For FLOW_MERGE. Links that are only MERGE
     */
    public static final int MERGE = 1 << 9;

    /**
     * For ITERATE, ON_ROWS_END. (ON_ROWS_END only for the virtual component. it like ITERATE generate order, and like
     * ON_COMPONENT_OK generate position.)
     */
    public static final int USE_ITERATE = 1 << 10;

    /**
     * For CAMEL, for ROUTE links of course. (Links property should be overrided directly in component)
     */
    public static final int CAMEL = 1 << 11;

    /**
     * For TABLE, TABLE_REF.
     */
    public static final int TABLE = 1 << 12;
}
