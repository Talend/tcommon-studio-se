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
public interface TalendProcessOptionConstants {

    /**
     * generate options
     */
    public static final int GENERATE_MAIN_ONLY = 1 << 1;

    public static final int GENERATE_WITH_FIRST_CHILD = 1 << 2;

    public static final int GENERATE_ALL_CHILDS = 1 << 3;

    public static final int GENERATE_TESTS = 1 << 4;

    public static final int GENERATE_WITHOUT_COMPILING = 1 << 5;

    public static final int GENERATE_WITHOUT_FORMAT = 1 << 6;

    public static final int GENERATE_POM_ONLY = 1 << 7;

    /**
     * for recursive job.
     */
    public static final int GENERATE_IS_MAINJOB = 1 << 8;

    /**
     * for ESB bundle.
     */
    public static final int GENERATE_NO_CODEGEN = 1 << 9;

    public static final int GENERATE_POM_NO_FILTER = 1 << 10;

    /**
     * clean options
     */
    public static final int CLEAN_JAVA_CODES = 1;

    public static final int CLEAN_CONTEXTS = 1 << 1;

    public static final int CLEAN_DATA_SETS = 1 << 2;

    /**
     * generate modules options
     */
    public static final int MODULES_DEFAULT = 1;

    public static final int MODULES_WITH_CHILDREN = 1 << 1;

    public static final int MODULES_WITH_INDEPENDENT = 1 << 2;

    // include joblet module but without joblet's dependencies
    public static final int MODULES_WITH_JOBLET = 1 << 3;

    public static final int MODULES_FOR_MR = 1 << 4;

}
